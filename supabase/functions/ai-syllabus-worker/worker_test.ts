import {
  assert,
  assertEquals,
} from "https://deno.land/std@0.224.0/assert/mod.ts";
import {
  type Lease,
  LeaseLostError,
  processSyllabusJob,
  type SyllabusWorkerJob,
  type SyllabusWorkerStore,
} from "./index.ts";
import type {
  OpenAiProvider,
  ProviderResponse,
} from "../_shared/openai-provider.ts";
import type { TerminalAiTelemetry } from "../_shared/job-finalizer.ts";
import { OpenAiProviderError } from "../_shared/openai-provider.ts";

const validOutput = JSON.stringify({
  schemaVersion: 1,
  promptVersion: "syllabus-v1",
  modelVersion: "gpt-6-luna",
  documentTitle: "Edital",
  subjects: [{
    name: "Direito",
    position: 0,
    suggestedPriority: "NORMAL",
    sourcePages: [1],
    topics: [{
      name: "Constituição",
      position: 0,
      sourcePages: [1],
      children: [],
    }],
  }],
  warnings: [],
  ambiguities: [],
});

function job(overrides: Partial<SyllabusWorkerJob> = {}): SyllabusWorkerJob {
  return {
    id: "job-1",
    userId: "user-1",
    status: "PROCESSING",
    sourceObjectPath: "user-1/source.pdf",
    sourceHash: "a".repeat(64),
    sourceBytes: 10,
    sourcePages: 1,
    sourceFileCount: 1,
    openaiResponseId: "resp-1",
    providerExecutionStartedAt: "2026-09-24T12:00:00Z",
    leaseExpiresAt: "2026-09-24T12:05:00Z",
    leaseOwner: "worker-test",
    leaseToken: "lease-token-test",
    leaseGeneration: 1,
    processingDeadlineAt: "2026-09-24T13:00:00Z",
    retryCount: 0,
    ...overrides,
  };
}

function store(
  initial: SyllabusWorkerJob,
): SyllabusWorkerStore & { events: string[] } {
  const events: string[] = [];
  return {
    events,
    async claimReconciliation() {
      return null;
    },
    async completeReconciliation(id) {
      events.push(`reconciliation-complete:${id}`);
    },
    async failReconciliation(id, _lease, code) {
      events.push(`reconciliation-failed:${id}:${code}`);
    },
    async claimNext() {
      return initial;
    },
    async assertLease() {},
    async recordProviderStartOutcome(id, _lease, outcome) {
      events.push(`provider-outcome:${id}:${outcome}`);
    },
    async persistResponseId(id, responseId) {
      events.push(`response:${id}:${responseId}`);
    },
    async finalizeSuccess(id) {
      events.push(`success:${id}`);
    },
    async finalizeFailure(id, _lease, code) {
      events.push(`failure:${id}:${code}`);
    },
    async markRetry(id) {
      events.push(`retry:${id}`);
    },
    async cleanupSource(id) {
      events.push(`cleanup:${id}`);
    },
    async reconcileProvider(id, _lease, recoverable) {
      events.push(`reconcile:${id}:${recoverable}`);
    },
    async captureUsage(id, _lease, usage) {
      events.push(`usage:${id}:${usage?.totalTokens ?? "null"}`);
    },
  };
}

function provider(response: ProviderResponse): OpenAiProvider {
  return {
    async start() {
      return response;
    },
    async retrieve() {
      return response;
    },
    async cancel() {
      return response;
    },
  };
}

Deno.test("persists response id before finalizing a valid result and captures usage", async () => {
  const jobs = store(
    job({ openaiResponseId: null, providerExecutionStartedAt: null }),
  );
  await processSyllabusJob({
    jobs,
    provider: provider({
      id: "resp-1",
      status: "completed",
      outputText: validOutput,
      usage: { inputTokens: 10, outputTokens: 20, totalTokens: 30 },
    }),
    source: async () => new Uint8Array([1, 2, 3]),
    now: () => new Date("2026-09-24T12:01:00Z"),
  });
  assertEquals(jobs.events[0], "response:job-1:resp-1");
  assertEquals(jobs.events[1], "usage:job-1:30");
  assertEquals(jobs.events[2], "cleanup:job-1");
  assertEquals(jobs.events[3], "success:job-1");
});

Deno.test("uses the configured model for both provider request and output validation", async () => {
  const jobs = store(
    job({ openaiResponseId: null, providerExecutionStartedAt: null }),
  );
  const configuredModel = "gpt-configured-default";
  const output = JSON.parse(validOutput);
  output.modelVersion = configuredModel;
  let requestedModel: string | undefined;

  await processSyllabusJob({
    jobs,
    provider: {
      ...provider({
        id: "resp-configured-model",
        status: "queued",
        outputText: null,
        usage: null,
      }),
      async start(input) {
        requestedModel = input.model;
        return {
          id: "resp-configured-model",
          status: "completed",
          outputText: JSON.stringify(output),
          usage: null,
        };
      },
    },
    source: async () => new Uint8Array([1]),
    model: configuredModel,
    now: () => new Date("2026-09-24T12:01:00Z"),
  });

  assertEquals(requestedModel, configuredModel);
  assert(jobs.events.includes("success:job-1"));
});

Deno.test("logs a safe version mismatch marker without changing the public error code", async () => {
  const jobs = store(job());
  const output = JSON.parse(validOutput);
  output.promptVersion = "PROVIDER_PROMPT_VALUE_SENTINEL";
  const diagnostics: Record<string, unknown>[] = [];

  await processSyllabusJob({
    jobs,
    provider: provider({
      id: "resp-1",
      status: "completed",
      outputText: JSON.stringify(output),
      usage: null,
    }),
    source: async () => new Uint8Array([1]),
    providerDiagnostics: (event) =>
      diagnostics.push(event as unknown as Record<string, unknown>),
    now: () => new Date("2026-09-24T12:01:00Z"),
  });

  assert(jobs.events.includes("failure:job-1:VERSION_MISMATCH"));
  assertEquals(diagnostics, [{
    event: "ai_provider_diagnostic",
    jobId: "job-1",
    stage: "validation",
    outcome: "VALIDATION_REJECTED",
    code: "VERSION_MISMATCH_PROMPT",
    message:
      "Structured proposal versions did not match the effective worker configuration",
  }]);
  assert(
    !JSON.stringify(diagnostics).includes("PROVIDER_PROMPT_VALUE_SENTINEL"),
  );
});

Deno.test("reconciles an expired lease before retrying a recoverable provider response", async () => {
  const jobs = store(
    job({ leaseExpiresAt: "2026-09-24T11:00:00Z", openaiResponseId: "resp-1" }),
  );
  await processSyllabusJob({
    jobs,
    provider: provider({
      id: "resp-1",
      status: "in_progress",
      outputText: null,
      usage: null,
    }),
    source: async () => new Uint8Array([1]),
    now: () => new Date("2026-09-24T12:01:00Z"),
  });
  assert(jobs.events.includes("reconcile:job-1:true"));
  assert(jobs.events.includes("retry:job-1"));
});

Deno.test("fails terminally on empty provider output without publishing a proposal", async () => {
  const jobs = store(job());
  await processSyllabusJob({
    jobs,
    provider: provider({
      id: "resp-1",
      status: "completed",
      outputText: "",
      usage: null,
    }),
    source: async () => new Uint8Array([1]),
    now: () => new Date("2026-09-24T12:01:00Z"),
  });
  assert(jobs.events.some((event) => event === "failure:job-1:EMPTY_OUTPUT"));
  assert(!jobs.events.some((event) => event.startsWith("success:")));
});

Deno.test("emits structured safe telemetry only after terminal success or failure", async () => {
  const events: TerminalAiTelemetry[] = [];
  const success = store(job());
  await processSyllabusJob({
    jobs: success,
    provider: provider({
      id: "resp-1",
      status: "completed",
      outputText: validOutput,
      usage: { inputTokens: 10, outputTokens: 20, totalTokens: 30 },
    }),
    source: async () => new Uint8Array([1]),
    now: () => new Date("2026-09-24T12:01:00Z"),
    telemetry: (event) => events.push(event),
  });
  assertEquals(events.length, 1);
  assertEquals(events[0].terminalStatus, "SUCCEEDED");
  assertEquals(events[0].feature, "SYLLABUS_GENERATION");
  assertEquals(events[0].jobId, "job-1");
  assertEquals(events[0].totalTokens, 30);
  assert(events[0].userPseudonym !== "user-1");
  assert(!JSON.stringify(events[0]).includes("Constituição"));
  assert(!JSON.stringify(events[0]).includes("source.pdf"));
  const failed = store(job());
  await processSyllabusJob({
    jobs: failed,
    provider: provider({
      id: "resp-1",
      status: "completed",
      outputText: "",
      usage: null,
    }),
    source: async () => new Uint8Array([1]),
    now: () => new Date("2026-09-24T12:01:00Z"),
    telemetry: (event) => events.push(event),
  });
  assertEquals(events[1].terminalStatus, "FAILED");
  assertEquals(events[1].totalTokens, null);
});

Deno.test("retains provider usage for terminal provider and proposal validation failures", async () => {
  const events: TerminalAiTelemetry[] = [];
  for (
    const output of [
      { status: "failed" as const, outputText: null },
      { status: "completed" as const, outputText: "invalid proposal" },
    ]
  ) {
    const jobs = store(job());
    await processSyllabusJob({
      jobs,
      provider: provider({
        id: "resp-1",
        ...output,
        usage: { inputTokens: 11, outputTokens: 7, totalTokens: 18 },
      }),
      source: async () => new Uint8Array([1]),
      now: () => new Date("2026-09-24T12:01:00Z"),
      telemetry: (event) => events.push(event),
    });
  }
  assertEquals(events.length, 2);
  for (const event of events) {
    assertEquals(event.terminalStatus, "FAILED");
    assertEquals(event.inputTokens, 11);
    assertEquals(event.outputTokens, 7);
    assertEquals(event.totalTokens, 18);
  }
});

Deno.test("local NOT_SENT after mark-start uses one atomic terminal operation", async () => {
  const jobs = store(
    job({ openaiResponseId: null, providerExecutionStartedAt: null }),
  );
  jobs.markProviderStarted = async () => {
    jobs.events.push("marked");
  };
  (jobs as SyllabusWorkerStore & {
    finalizeNotSent: (id: string, lease: Lease, code: string) => Promise<void>;
  }).finalizeNotSent = async (id, _lease, code) => {
    jobs.events.push(`not-sent:${id}:${code}`);
  };
  let starts = 0;
  await processSyllabusJob({
    jobs,
    provider: {
      ...provider({
        id: "unused",
        status: "failed",
        outputText: null,
        usage: null,
      }),
      async start() {
        starts++;
        throw new OpenAiProviderError("OPENAI_API_KEY_MISSING", "NOT_SENT");
      },
    },
    source: async () => new Uint8Array([1]),
    now: () => new Date("2026-09-24T12:01:00Z"),
  });
  assertEquals(starts, 1);
  assertEquals(jobs.events.filter((event) => event.startsWith("not-sent:")), [
    "not-sent:job-1:OPENAI_API_KEY_MISSING",
  ]);
  assert(
    !jobs.events.some((event) =>
      event.startsWith("failure:") || event.startsWith("retry:") ||
      event.startsWith("reconcile:")
    ),
  );
});

Deno.test("known response ID is recovered after lease-bound persistence fails", async () => {
  const jobs = store(
    job({ openaiResponseId: null, providerExecutionStartedAt: null }),
  );
  let starts = 0;
  jobs.markProviderStarted = async () => {};
  jobs.persistResponseId = async () => {
    jobs.events.push("persist-failed");
    throw new LeaseLostError();
  };
  (jobs as SyllabusWorkerStore & {
    recoverResponseId: (id: string, responseId: string) => Promise<void>;
  }).recoverResponseId = async (id, responseId) => {
    jobs.events.push(`recover:${id}:${responseId}`);
  };
  await processSyllabusJob({
    jobs,
    provider: {
      ...provider({
        id: "resp-recovered",
        status: "in_progress",
        outputText: null,
        usage: null,
      }),
      async start() {
        starts++;
        return {
          id: "resp-recovered",
          status: "in_progress",
          outputText: null,
          usage: null,
        };
      },
    },
    source: async () => new Uint8Array([1]),
    now: () => new Date("2026-09-24T12:01:00Z"),
  });
  assertEquals(starts, 1);
  assertEquals(jobs.events.slice(0, 2), [
    "persist-failed",
    "recover:job-1:resp-recovered",
  ]);
  assert(
    !jobs.events.some((event) =>
      event.startsWith("retry:") || event.startsWith("failure:")
    ),
  );
});

Deno.test("persists provider delivery classes without retrying or releasing quarantine", async () => {
  for (
    const outcome of [
      "PROVIDER_REJECTED",
      "TRANSPORT_AMBIGUOUS",
      "RESPONSE_AMBIGUOUS",
    ] as const
  ) {
    const jobs = store(
      job({ openaiResponseId: null, providerExecutionStartedAt: null }),
    );
    jobs.markProviderStarted = async () => {};
    const outcomes: string[] = [];
    (jobs as SyllabusWorkerStore & {
      recordProviderStartOutcome: (
        id: string,
        lease: Lease,
        outcome: string,
      ) => Promise<void>;
    }).recordProviderStartOutcome = async (_id, _lease, value) => {
      outcomes.push(value);
    };
    let starts = 0;
    await processSyllabusJob({
      jobs,
      provider: {
        ...provider({
          id: "unused",
          status: "failed",
          outputText: null,
          usage: null,
        }),
        async start() {
          starts++;
          throw new OpenAiProviderError("OPENAI_PROVIDER_ERROR", outcome);
        },
      },
      source: async () => new Uint8Array([1]),
      now: () => new Date("2026-09-24T12:01:00Z"),
    });
    assertEquals(starts, 1, `${outcome} cannot replay generation`);
    assertEquals(outcomes, [outcome], `${outcome} is persisted exactly once`);
    assert(
      !jobs.events.some((event) =>
        event.startsWith("retry:") || event.startsWith("failure:")
      ),
    );
  }
});

Deno.test("persists and retrieves an accepted ID when start status parsing is ambiguous", async () => {
  const jobs = store(
    job({
      openaiResponseId: null,
      providerExecutionStartedAt: null,
      providerStartOutcome: "NOT_STARTED",
      providerQuarantinedAt: null,
    }),
  );
  jobs.markProviderStarted = async () => {
    jobs.events.push("marked");
  };
  let retrieved = 0;
  await processSyllabusJob({
    jobs,
    provider: {
      ...provider({
        id: "resp-valid",
        status: "failed",
        outputText: null,
        usage: null,
      }),
      async start() {
        throw new OpenAiProviderError(
          "OPENAI_RESPONSE_INVALID",
          "RESPONSE_AMBIGUOUS",
          undefined,
          "resp-valid",
        );
      },
      async retrieve(id) {
        retrieved++;
        assertEquals(id, "resp-valid");
        return {
          id,
          status: "completed",
          outputText: validOutput,
          usage: null,
        };
      },
    },
    source: async () => new Uint8Array([1]),
    now: () => new Date("2026-09-24T12:01:00Z"),
  });
  assertEquals(retrieved, 1);
  assert(jobs.events.includes("response:job-1:resp-valid"));
  assert(jobs.events.includes("success:job-1"));
  assert(
    !jobs.events.some((event) =>
      event.startsWith("provider-outcome:job-1:RESPONSE_AMBIGUOUS")
    ),
  );
});

Deno.test("emits allowlisted structured provider diagnostics for start rejection", async () => {
  const jobs = store(
    job({
      openaiResponseId: null,
      providerExecutionStartedAt: null,
      providerStartOutcome: "NOT_STARTED",
      providerQuarantinedAt: null,
    }),
  );
  jobs.markProviderStarted = async () => {};
  const diagnostics: Record<string, unknown>[] = [];
  await processSyllabusJob(
    {
      jobs,
      provider: {
        ...provider({
          id: "unused",
          status: "failed",
          outputText: null,
          usage: null,
        }),
        async start() {
          throw new OpenAiProviderError(
            "OPENAI_PROVIDER_ERROR",
            "PROVIDER_REJECTED",
            {
              status: 429,
              type: "rate_limit_error",
              code: "rate_limit_exceeded",
              requestId: "req_safe_12345678",
              model: "gpt-6-luna",
              message: "Provider rejected the request",
            },
          );
        },
      },
      source: async () => new Uint8Array([1]),
      now: () => new Date("2026-09-24T12:01:00Z"),
      providerDiagnostics: (event) =>
        diagnostics.push(event as unknown as Record<string, unknown>),
    } as Parameters<typeof processSyllabusJob>[0] & {
      providerDiagnostics: (event: unknown) => void;
    },
  );
  assertEquals(diagnostics, [{
    event: "ai_provider_diagnostic",
    jobId: "job-1",
    stage: "start",
    outcome: "PROVIDER_REJECTED",
    code: "OPENAI_PROVIDER_ERROR",
    status: 429,
    type: "rate_limit_error",
    providerCode: "rate_limit_exceeded",
    requestId: "req_safe_12345678",
    model: "gpt-6-luna",
    message: "Provider rejected the request",
  }]);
});

Deno.test("cancellation winning mark-start prevents provider call and retry", async () => {
  const jobs = store(
    job({
      openaiResponseId: null,
      providerExecutionStartedAt: null,
      providerStartOutcome: "NOT_STARTED",
      providerQuarantinedAt: null,
    }),
  );
  jobs.markProviderStarted = async () => {
    throw new Error("AI_JOB_CANCELLATION_PENDING");
  };
  let starts = 0;
  await processSyllabusJob({
    jobs,
    provider: {
      ...provider({
        id: "unused",
        status: "failed",
        outputText: null,
        usage: null,
      }),
      async start() {
        starts++;
        throw new Error("unexpected start");
      },
    },
    source: async () => new Uint8Array([1]),
    now: () => new Date("2026-09-24T12:01:00Z"),
  });
  assertEquals(starts, 0);
  assertEquals(jobs.events, []);
});

Deno.test("worker drains a known-ID cancellation reconciliation before generation", async () => {
  const jobs = store(
    job({
      openaiResponseId: "resp-cancel",
      providerStartOutcome: "ACCEPTED",
      cancellationRequestedAt: "2026-09-24T12:00:00Z",
    }),
  );
  jobs.claimReconciliation = async () => {
    jobs.events.push("reconciliation-claim");
    return job({
      openaiResponseId: "resp-cancel",
      providerStartOutcome: "ACCEPTED",
      cancellationRequestedAt: "2026-09-24T12:00:00Z",
    });
  };
  jobs.completeReconciliation = async (id) => {
    jobs.events.push(`reconciliation-complete:${id}`);
  };
  let starts = 0;
  await processSyllabusJob({
    jobs,
    provider: {
      ...provider({
        id: "resp-cancel",
        status: "completed",
        outputText: validOutput,
        usage: null,
      }),
      async start() {
        starts++;
        throw new Error("reconciliation cannot start a new response");
      },
    },
    source: async () => new Uint8Array([1]),
    now: () => new Date("2026-09-24T12:01:00Z"),
  });
  assertEquals(starts, 0);
  assertEquals(jobs.events[0], "reconciliation-claim");
  assert(jobs.events.includes("reconciliation-complete:job-1"));
});

Deno.test("ambiguous reconciliation transport stays pending and never starts another response", async () => {
  const jobs = store(
    job({
      openaiResponseId: "resp-cancel",
      providerStartOutcome: "ACCEPTED",
      cancellationRequestedAt: "2026-09-24T12:00:00Z",
    }),
  );
  jobs.claimReconciliation = async () =>
    job({
      openaiResponseId: "resp-cancel",
      providerStartOutcome: "ACCEPTED",
      cancellationRequestedAt: "2026-09-24T12:00:00Z",
    });
  let starts = 0, retrieves = 0, cancels = 0;
  await processSyllabusJob({
    jobs,
    provider: {
      async start() {
        starts++;
        throw new Error("reconciliation must never start");
      },
      async retrieve() {
        retrieves++;
        throw new OpenAiProviderError("OPENAI_TIMEOUT", "TRANSPORT_AMBIGUOUS");
      },
      async cancel() {
        cancels++;
        throw new Error("cancel must not follow failed retrieve");
      },
    },
    source: async () => new Uint8Array([1]),
    now: () => new Date("2026-09-24T12:01:00Z"),
  });
  assertEquals({ starts, retrieves, cancels }, {
    starts: 0,
    retrieves: 1,
    cancels: 0,
  });
  assert(
    jobs.events.some((event) =>
      event === "reconciliation-failed:job-1:PROVIDER_TIMEOUT"
    ),
  );
  assert(
    !jobs.events.some((event) =>
      event.startsWith("success:") || event.startsWith("failure:")
    ),
  );
});

Deno.test("reconciliation rejects a response whose ID differs from the claimed ID", async () => {
  const jobs = store(
    job({
      openaiResponseId: "resp-expected",
      providerStartOutcome: "ACCEPTED",
      cancellationRequestedAt: "2026-09-24T12:00:00Z",
    }),
  );
  jobs.claimReconciliation = async () =>
    job({
      openaiResponseId: "resp-expected",
      providerStartOutcome: "ACCEPTED",
      cancellationRequestedAt: "2026-09-24T12:00:00Z",
    });
  let starts = 0;
  await processSyllabusJob({
    jobs,
    provider: {
      async start() {
        starts++;
        throw new Error("reconciliation cannot start");
      },
      async retrieve() {
        return {
          id: "resp-other",
          status: "completed",
          outputText: validOutput,
          usage: null,
        };
      },
      async cancel() {
        throw new Error("unexpected cancellation");
      },
    },
    source: async () => new Uint8Array([1]),
    now: () => new Date("2026-09-24T12:01:00Z"),
  });
  assertEquals(starts, 0);
  assert(
    jobs.events.includes(
      "reconciliation-failed:job-1:AI_PROVIDER_RESPONSE_ID_MISMATCH",
    ),
  );
  assert(
    !jobs.events.some((event) =>
      event.startsWith("success:") || event.startsWith("failure:")
    ),
  );
});

Deno.test("active provider response is cancelled and terminal result is finalized once", async () => {
  const jobs = store(
    job({
      openaiResponseId: "resp-cancel",
      providerStartOutcome: "ACCEPTED",
      cancellationRequestedAt: "2026-09-24T12:00:00Z",
    }),
  );
  jobs.claimReconciliation = async () =>
    job({
      openaiResponseId: "resp-cancel",
      providerStartOutcome: "ACCEPTED",
      cancellationRequestedAt: "2026-09-24T12:00:00Z",
    });
  jobs.finalizeFailure = async (id, _lease, code, _message, status) => {
    jobs.events.push(`failure:${id}:${code}:${status}`);
  };
  let starts = 0, retrieves = 0, cancels = 0;
  await processSyllabusJob({
    jobs,
    provider: {
      async start() {
        starts++;
        throw new Error("reconciliation cannot start");
      },
      async retrieve() {
        retrieves++;
        return {
          id: "resp-cancel",
          status: "in_progress",
          outputText: null,
          usage: null,
        };
      },
      async cancel() {
        cancels++;
        return {
          id: "resp-cancel",
          status: "cancelled",
          outputText: null,
          usage: null,
        };
      },
    },
    source: async () => new Uint8Array([1]),
    now: () => new Date("2026-09-24T12:01:00Z"),
  });
  assertEquals({ starts, retrieves, cancels }, {
    starts: 0,
    retrieves: 1,
    cancels: 1,
  });
  assert(
    jobs.events.some((event) =>
      event === "failure:job-1:PROVIDER_RESULT_UNAVAILABLE:CANCELLED"
    ),
  );
  assert(jobs.events.includes("reconciliation-complete:job-1"));
});

Deno.test("empty reconciliation queue falls through to normal generation claim", async () => {
  const jobs = store(job());
  jobs.claimReconciliation = async () => {
    jobs.events.push("reconciliation-claim");
    return null;
  };
  jobs.claimNext = async () => {
    jobs.events.push("generation-claim");
    return null;
  };
  assertEquals(
    await processSyllabusJob({
      jobs,
      provider: provider({
        id: "unused",
        status: "failed",
        outputText: null,
        usage: null,
      }),
      source: async () => new Uint8Array([1]),
    }),
    false,
  );
  assertEquals(jobs.events, ["reconciliation-claim", "generation-claim"]);
});

Deno.test("maps confirmed provider terminal statuses exactly during reconciliation", async () => {
  const cases = [
    { provider: "cancelled" as const, expected: "CANCELLED" },
    { provider: "expired" as const, expected: "EXPIRED" },
    { provider: "failed" as const, expected: "FAILED" },
    { provider: "incomplete" as const, expected: "FAILED" },
  ];
  for (const item of cases) {
    const jobs = store(
      job({
        openaiResponseId: "resp-cancel",
        providerStartOutcome: "ACCEPTED",
        cancellationRequestedAt: "2026-09-24T12:00:00Z",
      }),
    );
    jobs.claimReconciliation = async () =>
      job({
        openaiResponseId: "resp-cancel",
        providerStartOutcome: "ACCEPTED",
        cancellationRequestedAt: "2026-09-24T12:00:00Z",
      });
    let terminalStatus: string | null = null, starts = 0;
    jobs.finalizeFailure = async (_id, _lease, _code, _message, status) => {
      terminalStatus = status;
    };
    await processSyllabusJob({
      jobs,
      provider: {
        async start() {
          starts++;
          throw new Error("terminal reconciliation cannot start a response");
        },
        async retrieve() {
          return {
            id: "resp-cancel",
            status: item.provider,
            outputText: null,
            usage: null,
          };
        },
        async cancel() {
          throw new Error("terminal response does not need cancellation");
        },
      },
      source: async () => new Uint8Array([1]),
      now: () => new Date("2026-09-24T12:01:00Z"),
    });
    assertEquals(
      terminalStatus,
      item.expected,
      `${item.provider} maps to ${item.expected}`,
    );
    assertEquals(starts, 0);
    assert(jobs.events.includes("reconciliation-complete:job-1"));
  }
});

Deno.test("measures first-attempt telemetry duration from provider start", async () => {
  const jobs = store(
    job({ openaiResponseId: null, providerExecutionStartedAt: null }),
  );
  const events: TerminalAiTelemetry[] = [];
  jobs.markProviderStarted = async () => {};
  const moments = [0, 0, 1, 2, 3, 6, 7].map((seconds) =>
    new Date(`2026-09-24T12:00:0${seconds}Z`)
  );
  let tick = 0;
  await processSyllabusJob({
    jobs,
    provider: provider({
      id: "resp-1",
      status: "completed",
      outputText: validOutput,
      usage: null,
    }),
    source: async () => new Uint8Array([1]),
    now: () => moments[Math.min(tick++, moments.length - 1)],
    telemetry: (event) => events.push(event),
  });
  assertEquals(events.length, 1);
  assertEquals(events[0].durationMs, 4000);
});
