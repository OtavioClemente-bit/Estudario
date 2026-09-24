import {
  ContractValidationError,
  parseAiAccess,
  parseAiJob,
  parseAiSyllabusProposal,
  parseProviderAiSyllabusProposal,
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
  const proposal = parseProviderAiSyllabusProposal(fixture);
  const schema = getAiSyllabusProposalSchema(proposal.schemaVersion);
  const defs = schema.$defs as Record<string, Record<string, unknown>>;
  const properties = schema.properties as Record<string, any>;
  const subjectsSchema = properties.subjects as Record<string, any>;
  const topicProperties = defs.topic.properties as Record<string, any>;
  if (proposal.schemaVersion !== 1) throw new Error("fixture version was not preserved");
  if (schema.additionalProperties !== false || defs.topic.additionalProperties !== false || defs.warning.additionalProperties !== false) {
    throw new Error("proposal schema is not strict");
  }
  if (properties.documentTitle.pattern !== "\\S" || topicProperties.name.pattern !== "\\S") {
    throw new Error("schema does not enforce non-whitespace proposal text");
  }
  if (schema.$comment === undefined || subjectsSchema.$comment === undefined) {
    throw new Error("schema does not declare the provider validation boundary");
  }
});

Deno.test("provider boundary rejects schema/runtime divergence", async () => {
  const raw = await Deno.readTextFile(new URL("./fixtures/v1/ai-syllabus-proposal.json", import.meta.url));
  const whitespaceTitle = JSON.parse(raw) as Record<string, any>;
  whitespaceTitle.documentTitle = "   ";
  const duplicateSiblingPosition = JSON.parse(raw) as Record<string, any>;
  duplicateSiblingPosition.subjects[0].topics.push({
    ...duplicateSiblingPosition.subjects[0].topics[0],
    name: "Tópico duplicado",
  });

  for (const invalid of [whitespaceTitle, duplicateSiblingPosition]) {
    try {
      parseProviderAiSyllabusProposal(JSON.stringify(invalid));
      throw new Error("expected provider validation boundary to reject the payload");
    } catch (error) {
      if (!(error instanceof ContractValidationError)) throw error;
    }
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

Deno.test("requires unique remote subject and topic IDs across the validated tree", async () => {
  const subjectDuplicate = JSON.parse(await Deno.readTextFile(new URL("./fixtures/v1/private-syllabus.json", import.meta.url))) as Record<string, any>;
  subjectDuplicate.subjects.push({
    ...subjectDuplicate.subjects[0],
    externalId: "subject-outro",
    position: 1,
  });
  const topicDuplicate = JSON.parse(await Deno.readTextFile(new URL("./fixtures/v1/private-syllabus.json", import.meta.url))) as Record<string, any>;
  topicDuplicate.subjects[0].topics[0].children[0].remoteTopicId = topicDuplicate.subjects[0].topics[0].remoteTopicId;

  for (const invalid of [subjectDuplicate, topicDuplicate]) {
    try {
      parsePrivateSyllabus(invalid);
      throw new Error("expected duplicate remote ID to be rejected");
    } catch (error) {
      if (!(error instanceof ContractValidationError)) throw error;
    }
  }
});

Deno.test("rejects remote unknown fields, unsupported versions, and duplicate sibling positions", async () => {
  const read = async () => JSON.parse(await Deno.readTextFile(new URL("./fixtures/v1/private-syllabus.json", import.meta.url))) as Record<string, any>;
  const unknown = await read();
  unknown.subjects[0].unexpected = true;
  const unsupported = await read();
  unsupported.subjects[0].schemaVersion = 2;
  const duplicatePosition = await read();
  const topic = duplicatePosition.subjects[0].topics[0];
  topic.children.push({ ...topic.children[0], remoteTopicId: "remote-topic-3", externalId: "topic-extra" });

  for (const invalid of [unknown, unsupported, duplicatePosition]) {
    try {
      parsePrivateSyllabus(invalid);
      throw new Error("expected malformed remote payload to be rejected");
    } catch (error) {
      if (!(error instanceof ContractValidationError)) throw error;
    }
  }
});

Deno.test("rejects blank IDs, malformed timestamps, and blank nullable text", async () => {
  const job = JSON.parse(await Deno.readTextFile(new URL("./fixtures/v1/job-reserved.json", import.meta.url))) as Record<string, any>;
  const library = JSON.parse(await Deno.readTextFile(new URL("./fixtures/v1/private-syllabus.json", import.meta.url))) as Record<string, any>;
  const acknowledgement = JSON.parse(await Deno.readTextFile(new URL("./fixtures/v1/sync-pending.json", import.meta.url))) as Record<string, any>;
  const access = JSON.parse(await Deno.readTextFile(new URL("./fixtures/v1/access-quota-null.json", import.meta.url))) as Record<string, any>;

  const invalids = [
    () => parseAiJob({ ...job, jobId: " " }),
    () => parseAiJob({ ...job, createdAt: "not-a-timestamp" }),
    () => parseAiAccess({ ...access, reasonCode: " " }),
    () => parsePrivateSyllabus({ ...library, sourceJobId: " " }),
    () => parseRemoteSyllabusSyncAcknowledgement({ ...acknowledgement, jobId: " " }),
    () => parseRemoteSyllabusSyncAcknowledgement({ ...acknowledgement, updatedAt: "not-a-timestamp" }),
  ];

  for (const parseInvalid of invalids) {
    try {
      parseInvalid();
      throw new Error("expected malformed shared DTO to be rejected");
    } catch (error) {
      if (!(error instanceof ContractValidationError)) throw error;
    }
  }
});

Deno.test("rejects impossible calendar dates in job and sync timestamps", async () => {
  const job = JSON.parse(await Deno.readTextFile(new URL("./fixtures/v1/job-reserved.json", import.meta.url))) as Record<string, any>;
  const acknowledgement = JSON.parse(await Deno.readTextFile(new URL("./fixtures/v1/sync-pending.json", import.meta.url))) as Record<string, any>;

  for (const parseInvalid of [
    () => parseAiJob({ ...job, createdAt: "2026-02-30T12:00:00Z" }),
    () => parseRemoteSyllabusSyncAcknowledgement({ ...acknowledgement, updatedAt: "2026-02-30T12:00:00Z" }),
  ]) {
    try {
      parseInvalid();
      throw new Error("expected impossible calendar date to be rejected");
    } catch (error) {
      if (!(error instanceof ContractValidationError)) throw error;
    }
  }
});

Deno.test("matches Kotlin Instant.parse second and midnight boundaries", async () => {
  const job = JSON.parse(await Deno.readTextFile(new URL("./fixtures/v1/job-reserved.json", import.meta.url))) as Record<string, any>;
  const acknowledgement = JSON.parse(await Deno.readTextFile(new URL("./fixtures/v1/sync-pending.json", import.meta.url))) as Record<string, any>;

  for (const parseInvalid of [
    () => parseAiJob({ ...job, createdAt: "2026-02-28T00:00:60Z" }),
    () => parseRemoteSyllabusSyncAcknowledgement({ ...acknowledgement, updatedAt: "2026-02-28T23:58:60Z" }),
  ]) {
    try {
      parseInvalid();
      throw new Error("expected unsupported second boundary to be rejected");
    } catch (error) {
      if (!(error instanceof ContractValidationError)) throw error;
    }
  }

  const leapSecondJob = parseAiJob({ ...job, createdAt: "2026-02-28T23:59:60Z" });
  const midnightAcknowledgement = parseRemoteSyllabusSyncAcknowledgement({
    ...acknowledgement,
    updatedAt: "2026-02-28T24:00:00.123456789+03:00",
  });
  if (leapSecondJob.createdAt !== "2026-02-28T23:59:60Z" || midnightAcknowledgement.updatedAt !== "2026-02-28T24:00:00.123456789+03:00") {
    throw new Error("Kotlin-compatible boundary timestamps were not preserved");
  }
});

Deno.test("parses every versioned shared fixture", async () => {
  const read = (name: string) => Deno.readTextFile(new URL(`./fixtures/v1/${name}`, import.meta.url));
  parseProviderAiSyllabusProposal(await read("ai-syllabus-proposal.json"));
  parseAiJob(JSON.parse(await read("job-reserved.json")));
  parseAiAccess(JSON.parse(await read("access-quota-null.json")));
  parsePrivateSyllabus(JSON.parse(await read("private-syllabus.json")));
  parseRemoteSyllabusSyncAcknowledgement(JSON.parse(await read("sync-pending.json")));
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
