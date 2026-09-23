import {
  ContractValidationError,
  parseAiAccess,
  parseAiJob,
  parseAiSyllabusProposal,
  parseAiSyllabusProposalJson,
  parsePrivateSyllabus,
  parseRemoteSyllabusSyncAcknowledgement,
} from "./contracts.ts";
import { getAiSyllabusProposalSchema } from "./schema.ts";

const validProposal = {
  schemaVersion: 1,
  promptVersion: "syllabus-v1",
  modelVersion: "gpt-6-luna",
  documentTitle: "Edital TRT-3",
  subjects: [
    {
      name: "Direito Constitucional",
      position: 0,
      suggestedPriority: "NORMAL",
      topics: [
        {
          name: "Direitos fundamentais",
          position: 0,
          children: [
            {
              name: "Remédios constitucionais",
              position: 0,
              children: [],
              sourcePages: [43],
            },
          ],
          sourcePages: [42, 43],
        },
      ],
      sourcePages: [42, 43],
    },
  ],
  warnings: [
    {
      code: "AMBIGUOUS_STRUCTURE",
      severity: "WARNING",
      message: "O texto não deixa claro se este item é uma matéria ou um tópico.",
      sourcePages: [44],
      ambiguity: "A seção pode pertencer a duas matérias.",
    },
  ],
  ambiguities: ["A seção pode pertencer a duas matérias."],
};

Deno.test("accepts a valid nested proposal with warnings and source pages", () => {
  const proposal = parseAiSyllabusProposal(validProposal);

  if (proposal.subjects[0].topics[0].children[0].name !== "Remédios constitucionais") {
    throw new Error("nested topic was not preserved");
  }
  if (proposal.warnings[0].sourcePages[0] !== 44) {
    throw new Error("warning source page was not preserved");
  }
});

Deno.test("rejects an unsupported proposal schema version", () => {
  const unsupported = { ...validProposal, schemaVersion: 2 };

  try {
    parseAiSyllabusProposal(unsupported);
    throw new Error("expected unsupported schema version to be rejected");
  } catch (error) {
    if (!(error instanceof ContractValidationError) || !error.message.includes("schemaVersion")) {
      throw error;
    }
  }
});

Deno.test("rejects unknown fields and definitive local identifiers", () => {
  for (const invalid of [
    { ...validProposal, unexpected: true },
    { ...validProposal, subjects: [{ ...validProposal.subjects[0], id: 42 }] },
    { ...validProposal, subjects: [{ ...validProposal.subjects[0], topics: [{ ...validProposal.subjects[0].topics[0], roomId: 42 }] }] },
  ]) {
    try {
      parseAiSyllabusProposal(invalid);
      throw new Error("expected malformed proposal to be rejected");
    } catch (error) {
      if (!(error instanceof ContractValidationError)) throw error;
    }
  }
});

Deno.test("rejects missing required proposal fields", () => {
  const missingTitle = { ...validProposal };
  delete (missingTitle as Record<string, unknown>).documentTitle;

  try {
    parseAiSyllabusProposal(missingTitle);
    throw new Error("expected missing field to be rejected");
  } catch (error) {
    if (!(error instanceof ContractValidationError)) throw error;
  }
});

Deno.test("parses the versioned proposal fixture and exposes a strict schema", async () => {
  const fixture = await Deno.readTextFile(new URL("./fixtures/v1/ai-syllabus-proposal.json", import.meta.url));
  const proposal = parseAiSyllabusProposalJson(fixture);
  const schema = getAiSyllabusProposalSchema(proposal.schemaVersion);
  const defs = schema.$defs as Record<string, Record<string, unknown>>;
  if (proposal.schemaVersion !== 1) throw new Error("fixture version was not preserved");
  if (schema.additionalProperties !== false || defs.topic.additionalProperties !== false || defs.warning.additionalProperties !== false) {
    throw new Error("proposal schema is not strict");
  }
});

Deno.test("rejects malformed topic positions and warning structures", () => {
  const invalidTopic = {
    ...validProposal,
    subjects: [{
      ...validProposal.subjects[0],
      topics: [{ ...validProposal.subjects[0].topics[0], position: -1 }],
    }],
  };
  const invalidWarning = {
    ...validProposal,
    warnings: [{ ...validProposal.warnings[0], sourcePages: [0] }],
  };

  for (const invalid of [invalidTopic, invalidWarning]) {
    try {
      parseAiSyllabusProposal(invalid);
      throw new Error("expected invalid structure to be rejected");
    } catch (error) {
      if (!(error instanceof ContractValidationError)) throw error;
    }
  }
});

Deno.test("preserves stable external IDs in the private library contract", () => {
  const library = parsePrivateSyllabus({
    remoteSyllabusId: "remote-syllabus-1",
    title: "Edital TRT-3",
    position: 0,
    visibility: "PRIVATE",
    source: "AI_GENERATED",
    sourceJobId: "job-1",
    sourceHash: "a".repeat(64),
    schemaVersion: 1,
    status: "ACTIVE",
    metadata: { packageVersion: "estudo-v2" },
    subjects: [{
      remoteSubjectId: "remote-subject-1",
      externalId: "subject-direito-constitucional",
      name: "Direito Constitucional",
      position: 0,
      suggestedPriority: "NORMAL",
      packageVersion: "estudo-v2",
      schemaVersion: 1,
      metadata: {},
      topics: [{
        remoteTopicId: "remote-topic-1",
        externalId: "topic-direitos-fundamentais",
        parentRemoteTopicId: null,
        name: "Direitos fundamentais",
        position: 0,
        packageVersion: "estudo-v2",
        schemaVersion: 1,
        metadata: {},
        children: [],
      }],
    }],
  });

  if (library.subjects[0].externalId !== "subject-direito-constitucional") {
    throw new Error("subject externalId was not preserved");
  }
  if (library.subjects[0].topics[0].externalId !== "topic-direitos-fundamentais") {
    throw new Error("topic externalId was not preserved");
  }
});

Deno.test("rejects a remote topic whose parent link disagrees with the tree", async () => {
  const value = JSON.parse(await Deno.readTextFile(new URL("./fixtures/v1/private-syllabus.json", import.meta.url))) as Record<string, any>;
  value.subjects[0].topics[0].parentRemoteTopicId = "wrong-parent";

  try {
    parsePrivateSyllabus(value);
    throw new Error("expected invalid parent link to be rejected");
  } catch (error) {
    if (!(error instanceof ContractValidationError)) throw error;
  }
});

Deno.test("keeps job, access, and sync acknowledgment nullability explicit", () => {
  const job = parseAiJob({
    jobId: "job-1",
    feature: "SYLLABUS_GENERATION",
    status: "RESERVED",
    schemaVersion: null,
    promptVersion: null,
    modelVersion: null,
    proposal: null,
    warnings: [],
    errorCode: null,
    errorMessage: null,
    createdAt: "2026-09-23T12:00:00Z",
    updatedAt: "2026-09-23T12:00:00Z",
    finishedAt: null,
    providerExecutionStartedAt: null,
  });
  const access = parseAiAccess({
    authenticated: true,
    betaAccess: true,
    feature: "SYLLABUS_GENERATION",
    featureEnabled: true,
    quota: null,
    canUse: false,
    reasonCode: "QUOTA_EXHAUSTED",
  });
  const quotaAccess = parseAiAccess({
    authenticated: true,
    betaAccess: true,
    feature: "SYLLABUS_GENERATION",
    featureEnabled: true,
    quota: {
      feature: "SYLLABUS_GENERATION",
      limit: 1,
      successfulCount: 0,
      reservedCount: 1,
      remaining: 0,
      periodStart: "2026-09-23",
    },
    canUse: false,
    reasonCode: "QUOTA_RESERVED",
  });
  const acknowledgement = parseRemoteSyllabusSyncAcknowledgement({
    remoteSyllabusId: null,
    jobId: "job-1",
    payloadHash: "b".repeat(64),
    state: "PENDING",
    attemptCount: 0,
    nextAttemptAt: null,
    safeError: null,
    createdAt: "2026-09-23T12:00:00Z",
    updatedAt: "2026-09-23T12:00:00Z",
    attemptToken: null,
  });

  if (job.proposal !== null || access.quota !== null || quotaAccess.quota?.reservedCount !== 1 || acknowledgement.remoteSyllabusId !== null) {
    throw new Error("nullable fields were not preserved");
  }
});
