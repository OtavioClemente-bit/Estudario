import { assert, assertEquals, assertThrows } from "https://deno.land/std@0.224.0/assert/mod.ts";
import {
  type Lease,
  processSyllabusJob,
  type SyllabusWorkerJob,
  type SyllabusWorkerStore,
} from "../ai-syllabus-worker/index.ts";
import type { OpenAiProvider, ProviderResponse, ProviderStartInput } from "../_shared/openai-provider.ts";
import { parseContentJobInput, parsePlanJobInput, TextJobInputError } from "../_shared/text-job-input.ts";
import { validateStudyPlan, validateTopicContent } from "../_shared/text-job-validators.ts";
import { textSpecFor } from "../_shared/text-job-specs.ts";
import { CONTENT_PROMPT_VERSION, PLAN_PROMPT_VERSION } from "../_shared/prompts/text-jobs-v1.ts";

const MODEL = "gpt-6-luna";
const lease: Lease = { owner: "text-worker", token: "lease-t", generation: 3 };

const contentInput = {
  competitionName: "TRT 3ª Região",
  role: "Analista Judiciário, TI",
  board: "FCC",
  agency: "Tribunal Regional do Trabalho da 3ª Região",
  sphere: "FEDERAL",
  subjectName: "Noções de Direito",
  topicPath: ["Direito Constitucional", "Dos princípios fundamentais"],
};

function question(index: number, overrides: Record<string, unknown> = {}) {
  const trueFalse = index % 3 === 2;
  const options = trueFalse
    ? [{ key: "C", text: "Certo", correct: index % 2 === 0 }, { key: "E", text: "Errado", correct: index % 2 !== 0 }]
    : ["A", "B", "C", "D", "E"].map((key, optionIndex) => ({ key, text: `Alternativa ${key} ${index}`, correct: optionIndex === index % 5 }));
  return {
    statement: `Enunciado ${index}`,
    format: trueFalse ? "TRUE_FALSE" : "MULTIPLE_CHOICE",
    difficulty: ["FACIL", "MEDIA", "DIFICIL"][index % 3],
    options,
    explanation: "Explicação com a fonte (CF, art. 1º).",
    section: "1. Fundamentos",
    errorConceptKey: "e1",
    sourceType: "AUTHORIAL",
    board: null,
    agency: null,
    year: null,
    sourceUrl: null,
    ...overrides,
  };
}

function content(overrides: Record<string, unknown> = {}) {
  return {
    schemaVersion: 4,
    promptVersion: CONTENT_PROMPT_VERSION,
    modelVersion: MODEL,
    scope: { covers: "Princípios fundamentais", excludes: "Direitos fundamentais" },
    theoryTitle: "Princípios fundamentais",
    chapters: [{ title: "1. Fundamentos", markdown: "Texto" }, { title: "2. Aplicação", markdown: "Texto" }],
    summary: "# Resumo",
    flashcards: Array.from({ length: 12 }, (_, i) => ({ front: `Pergunta ${i + 1}?`, back: `**Resposta** ${i + 1}.` })),
    tips: ["Bizu"],
    traps: ["Pegadinha"],
    activeRecall: [{ question: "Pergunta?", answer: "Resposta." }],
    errorConcepts: [{ key: "e1", title: "Fundamento x objetivo", summary: "Explicação" }],
    questions: Array.from({ length: 10 }, (_, index) => question(index)),
    sources: [{ kind: "OFICIAL", title: "Constituição Federal", publisher: "Planalto", reference: "Art. 1º", url: "https://www.planalto.gov.br/ccivil_03/constituicao/constituicao.htm", accessedAt: "2026-09-28" }],
    warnings: [],
    ...overrides,
  };
}

const expectedContent = { schemaVersion: 4, promptVersion: CONTENT_PROMPT_VERSION, modelVersion: MODEL };
// Pedido sem opções = pacote completo com 10 questões mistas, como antes.
const fullOptions = parseContentJobInput(contentInput).options;

Deno.test("content input keeps known fields, strips control characters and rejects oversize text", () => {
  const parsed = parseContentJobInput({ ...contentInput, subjectName: "Noções\nde\u0000 Direito" });
  assertEquals(parsed.subjectName, "Noções de Direito");
  assertThrows(() => parseContentJobInput({ ...contentInput, subjectName: "x".repeat(301) }), TextJobInputError);
  assertThrows(() => parseContentJobInput({ ...contentInput, topicPath: [] }), TextJobInputError);
  assertThrows(() => parseContentJobInput({ ...contentInput, sphere: "GALACTICA" }), TextJobInputError);
});

Deno.test("valid topic content passes the server validator", () => {
  const value = validateTopicContent(JSON.stringify(content()), expectedContent, fullOptions);
  assertEquals((value.questions as unknown[]).length, 10);
});

Deno.test("content validator rejects invented or incoherent material", () => {
  const cases: Record<string, unknown>[] = [
    { questions: [question(0, { options: [{ key: "A", text: "a", correct: true }, { key: "B", text: "b", correct: true }, { key: "C", text: "c", correct: false }, { key: "D", text: "d", correct: false }, { key: "E", text: "e", correct: false }] }), ...Array.from({ length: 9 }, (_, i) => question(i + 1))] },
    { questions: [question(0, { section: "Capítulo inventado" }), ...Array.from({ length: 9 }, (_, i) => question(i + 1))] },
    { questions: [question(0, { sourceUrl: "https://exemplo.com/prova" }), ...Array.from({ length: 9 }, (_, i) => question(i + 1))] },
    { questions: [question(0, { sourceType: "REAL" }), ...Array.from({ length: 9 }, (_, i) => question(i + 1))] },
    { questions: [question(0, { errorConceptKey: "e5" }), ...Array.from({ length: 9 }, (_, i) => question(i + 1))] },
    { sources: [{ kind: "OFICIAL", title: "Sem link", publisher: "X", reference: "", url: "não é url", accessedAt: "2026-09-28" }] },
    { flashcards: [] },
    { flashcards: Array.from({ length: 12 }, () => ({ front: "Mesma frente?", back: "Verso." })) },
    { modelVersion: "outro-modelo" },
  ];
  for (const override of cases) {
    assertThrows(() => validateTopicContent(JSON.stringify(content(override)), expectedContent, fullOptions));
  }
});

const planInput = {
  competitionName: "TRT 3",
  startDate: "2026-09-28", // segunda-feira
  endDate: "2026-10-11",
  examDate: null,
  dayMinutes: [120, 120, 120, 120, 120, 60, 0],
  blockMinutes: 50,
  weeklyQuestions: 100,
  profile: "DO_ZERO",
  preference: null,
  subjects: [
    { ref: "s1", name: "Português", priority: "HIGH", topics: [{ ref: "t1", title: "Interpretação", studied: false }] },
    { ref: "s2", name: "Direito", priority: "MEDIUM", topics: [{ ref: "t2", title: "Princípios", studied: true }] },
  ],
};

function plan(tasks: Record<string, unknown>[]) {
  return JSON.stringify({
    schemaVersion: 1,
    promptVersion: PLAN_PROMPT_VERSION,
    modelVersion: MODEL,
    summary: "Estratégia.",
    phases: [{ name: "Base", objective: "Fundamentos", fromDay: 0, toDay: 13 }],
    tasks,
    warnings: [],
  });
}

const expectedPlan = { schemaVersion: 1, promptVersion: PLAN_PROMPT_VERSION, modelVersion: MODEL };

Deno.test("plan input rejects malformed refs, long horizons and empty weeks", () => {
  const parsed = parsePlanJobInput(planInput);
  assertEquals(parsed.subjects.length, 2);
  assertThrows(() => parsePlanJobInput({ ...planInput, endDate: "2027-06-01" }), TextJobInputError);
  assertThrows(() => parsePlanJobInput({ ...planInput, dayMinutes: [0, 0, 0, 0, 0, 0, 0] }), TextJobInputError);
  assertThrows(() => parsePlanJobInput({ ...planInput, subjects: [{ ...planInput.subjects[0], ref: "materia-1" }] }), TextJobInputError);
  assertThrows(() => parsePlanJobInput({ ...planInput, subjects: [planInput.subjects[0], { ...planInput.subjects[1], ref: "s1" }] }), TextJobInputError);
});

Deno.test("plan validator accepts only provided refs inside each day's capacity", () => {
  const input = parsePlanJobInput(planInput);
  validateStudyPlan(plan([
    { d: 0, s: "s1", t: "t1", k: "THEORY", m: 50, q: 0 },
    { d: 0, s: "s1", t: "t1", k: "QUESTIONS", m: 50, q: 15 },
    { d: 5, s: null, t: null, k: "SIMULATION", m: 60, q: 40 },
  ]), expectedPlan, input);

  const bad = [
    [{ d: 0, s: "s9", t: null, k: "THEORY", m: 50, q: 0 }],          // matéria inventada
    [{ d: 0, s: "s1", t: "t2", k: "THEORY", m: 50, q: 0 }],          // tópico de outra matéria
    [{ d: 6, s: "s1", t: "t1", k: "THEORY", m: 50, q: 0 }],          // domingo sem tempo
    [{ d: 0, s: "s1", t: "t1", k: "THEORY", m: 121, q: 0 }],         // passa da capacidade
    [{ d: 14, s: "s1", t: "t1", k: "THEORY", m: 50, q: 0 }],         // depois do fim
    [{ d: 0, s: "s1", t: "t1", k: "QUESTIONS", m: 50, q: 0 }],       // questões sem questões
    [{ d: 0, s: "s1", t: null, k: "SIMULATION", m: 50, q: 0 }],      // simulado ligado a matéria
  ];
  for (const tasks of bad) assertThrows(() => validateStudyPlan(plan(tasks), expectedPlan, input));
});

function textJob(feature: "CONTENT_GENERATION" | "PLAN_GENERATION", input: unknown): SyllabusWorkerJob {
  return {
    id: "job-text",
    userId: "user-1",
    status: "PROCESSING",
    sourceObjectPath: "",
    sourceHash: "",
    sourceBytes: 0,
    sourcePages: 0,
    sourceFileCount: 0,
    requestPayload: { feature, input },
    feature,
    openaiResponseId: null,
    providerExecutionStartedAt: null,
    leaseExpiresAt: "2026-09-28T12:05:00Z",
    leaseOwner: lease.owner,
    leaseToken: lease.token,
    leaseGeneration: lease.generation,
    processingDeadlineAt: "2026-09-28T13:00:00Z",
    retryCount: 0,
  };
}

function store(job: SyllabusWorkerJob) {
  const events: string[] = [];
  const proposals: unknown[] = [];
  const jobs = {
    async claimReconciliation() { return null; },
    async completeReconciliation() {},
    async failReconciliation() {},
    async claimNext() { return job; },
    async assertLease() {},
    async recordProviderStartOutcome(_id: string, _l: Lease, outcome: string) { events.push(`outcome:${outcome}`); },
    async persistResponseId(_id: string, responseId: string) { events.push(`response:${responseId}`); },
    async finalizeNotSent(_id: string, _l: Lease, code: string) { events.push(`not-sent:${code}`); },
    async reconcileProvider() { events.push("reconcile"); },
    async markRetry() { events.push("retry"); },
    async captureUsage() { events.push("usage"); },
    async finalizeSuccess(_id: string, _l: Lease, proposal: unknown) { proposals.push(proposal); events.push("success"); },
    async finalizeFailure(_id: string, _l: Lease, code: string, _m: string, status: string) { events.push(`failure:${code}:${status}`); },
    async cleanupSource() { events.push("cleanup"); },
  } as unknown as SyllabusWorkerStore;
  return { jobs, events, proposals };
}

function completed(output: string): ProviderResponse {
  return { id: "resp_1", outcome: "ACCEPTED", status: "completed", outputText: output, usage: null };
}

Deno.test("worker generates topic content from text only, with web search and no PDF or cleanup", async () => {
  const { jobs, events, proposals } = store(textJob("CONTENT_GENERATION", contentInput));
  const starts: ProviderStartInput[] = [];
  const provider: OpenAiProvider = {
    start: (input) => { starts.push(input); return Promise.resolve(completed(JSON.stringify(content()))); },
    retrieve: () => Promise.reject(new Error("retrieve must not run")),
    cancel: () => Promise.reject(new Error("cancel must not run")),
  };
  await processSyllabusJob({
    jobs,
    provider,
    source: () => Promise.reject(new Error("PDF must not be read")),
    specForJob: textSpecFor,
    now: () => new Date("2026-09-28T12:01:00Z"),
  });
  // Segunda chamada: o revisor de fatos, com pesquisa web e chave idempotente própria.
  assertEquals(starts.length, 2);
  assertEquals(starts[1].feature, "CONTENT_REVIEW");
  assertEquals(starts[1].idempotencyKey, `${starts[0].idempotencyKey}:review`);
  assertEquals(starts[0].source, undefined);
  assertEquals(starts[0].feature, "CONTENT_GENERATION");
  assertEquals(starts[0].schemaName, "ai_topic_content_v4");
  assert(JSON.stringify(starts[0].tools).includes("web_search"));
  assert(starts[0].userPrompt!.includes("Dos princípios fundamentais"));
  assert(events.includes("success"));
  assert(!events.includes("cleanup"));
  assertEquals(proposals.length, 1);
});

Deno.test("worker fails a text job with invalid stored input before calling the provider", async () => {
  const { jobs, events } = store(textJob("CONTENT_GENERATION", { competitionName: "" }));
  let called = false;
  await processSyllabusJob({
    jobs,
    provider: {
      start: () => { called = true; return Promise.reject(new Error("no")); },
      retrieve: () => Promise.reject(new Error("no")),
      cancel: () => Promise.reject(new Error("no")),
    },
    source: () => Promise.reject(new Error("no")),
    specForJob: textSpecFor,
    now: () => new Date("2026-09-28T12:01:00Z"),
  });
  assert(!called);
  assert(events.includes("failure:TEXT_JOB_INPUT_INVALID:FAILED"));
});

Deno.test("worker rejects a plan that uses a topic the app never sent", async () => {
  const { jobs, events } = store(textJob("PLAN_GENERATION", planInput));
  await processSyllabusJob({
    jobs,
    provider: {
      start: () => Promise.resolve(completed(plan([{ d: 0, s: "s1", t: "t99", k: "THEORY", m: 50, q: 0 }]))),
      retrieve: () => Promise.reject(new Error("no")),
      cancel: () => Promise.reject(new Error("no")),
    },
    source: () => Promise.reject(new Error("no")),
    specForJob: textSpecFor,
    now: () => new Date("2026-09-28T12:01:00Z"),
  });
  assert(events.includes("failure:SCHEMA_MISMATCH:FAILED"));
  assert(!events.includes("success"));
});

function cachingStore(job: SyllabusWorkerJob, saved: Map<string, Record<string, unknown>>, served: Set<string>) {
  const base = store(job);
  Object.assign(base.jobs, {
    async cachedContent(key: string, _v: string, userId: string) { return served.has(`${key}:${userId}`) ? null : saved.get(key) ?? null; },
    async storeContent(key: string, _v: string, userId: string, _j: string, proposal: Record<string, unknown>) { saved.set(key, proposal); served.add(`${key}:${userId}`); },
    async markServed(key: string, userId: string) { served.add(`${key}:${userId}`); },
  });
  return base;
}

Deno.test("same topic for another person comes from the cache, without calling the provider", async () => {
  const saved = new Map<string, Record<string, unknown>>();
  const served = new Set<string>();
  let starts = 0;
  const provider: OpenAiProvider = {
    start: () => { starts++; return Promise.resolve(completed(JSON.stringify(content()))); },
    retrieve: () => Promise.reject(new Error("no retrieve")),
    cancel: () => Promise.reject(new Error("no cancel")),
  };
  const run = async (userId: string) => {
    const job = { ...textJob("CONTENT_GENERATION", contentInput), userId };
    const { jobs, proposals } = cachingStore(job, saved, served);
    await processSyllabusJob({ jobs, provider, source: () => Promise.reject(new Error("no pdf")), specForJob: textSpecFor, now: () => new Date("2026-09-28T12:01:00Z") });
    return proposals.length;
  };
  assertEquals(await run("ana"), 1);
  assertEquals(starts, 2); // geração + revisor
  assertEquals(await run("bia"), 1);
  assertEquals(starts, 2, "bia recebe o material guardado");
  assertEquals(await run("ana"), 1);
  assertEquals(starts, 4, "ana pediu de novo: ganha outro material");
});

Deno.test("cache key ignores accents, case and spacing but not the options", async () => {
  const { contentCacheKey } = await import("../_shared/content-cache.ts");
  const input = parseContentJobInput(contentInput);
  const same = parseContentJobInput({ ...contentInput, competitionName: `  ${contentInput.competitionName.toUpperCase()} ` });
  assertEquals(await contentCacheKey(input), await contentCacheKey(same));
  const other = parseContentJobInput({ ...contentInput, options: { ...input.options, difficulty: "EASY" } });
  assert(await contentCacheKey(input) !== await contentCacheKey(other));
});

Deno.test("Portuguese requests carry the example questions; other subjects do not", async () => {
  const { contentUserPrompt, isPortuguese } = await import("../_shared/prompts/text-jobs-v1.ts");
  assert(isPortuguese("LÍNGUA PORTUGUESA (NÍVEL MÉDIO/SUPERIOR)"));
  assert(isPortuguese("Português"));
  assert(!isPortuguese("Raciocínio Lógico-Matemático"));
  const input = parseContentJobInput(contentInput);
  assert(!contentUserPrompt(input).includes("NÍVEL ESPERADO EM LÍNGUA PORTUGUESA"));
  assert(contentUserPrompt({ ...input, subjectName: "Língua Portuguesa" }).includes("NÍVEL ESPERADO EM LÍNGUA PORTUGUESA"));
});

Deno.test("only Portuguese material asks the model to reason more", () => {
  const spec = textSpecFor(textJob("CONTENT_GENERATION", contentInput));
  assertEquals(spec.reasoningEffort?.(textJob("CONTENT_GENERATION", contentInput)), undefined);
  assertEquals(spec.reasoningEffort?.(textJob("CONTENT_GENERATION", { ...contentInput, subjectName: "Língua Portuguesa" })), "medium");
});

Deno.test("missing questions are written by a short top-up call without web search", async () => {
  const { jobs, proposals } = store(textJob("CONTENT_GENERATION", contentInput));
  const starts: ProviderStartInput[] = [];
  const provider: OpenAiProvider = {
    start: (input) => {
      starts.push(input);
      if (input.feature === "CONTENT_TOPUP") {
        return Promise.resolve(completed(JSON.stringify({ questions: [question(20), question(21), question(22)] })));
      }
      // Geração com só 7 questões; o revisor responde algo que não é revisão (segue o original).
      return Promise.resolve(completed(JSON.stringify(content({ questions: Array.from({ length: 7 }, (_, i) => question(i)) }))));
    },
    retrieve: () => Promise.reject(new Error("no retrieve")),
    cancel: () => Promise.reject(new Error("no cancel")),
  };
  await processSyllabusJob({ jobs, provider, source: () => Promise.reject(new Error("no pdf")), specForJob: textSpecFor, now: () => new Date("2026-09-28T12:01:00Z") });
  const topUp = starts.find((s) => s.feature === "CONTENT_TOPUP");
  assert(topUp, "chamou a reposição");
  assertEquals(topUp.tools, undefined);
  assert(topUp.userPrompt!.includes("exatamente 3"));
  assertEquals(((proposals[0] as { questions: unknown[] }).questions).length, 10);
});
