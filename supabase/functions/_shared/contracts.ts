export const CURRENT_AI_SCHEMA_VERSION = 1 as const;
export const SUPPORTED_AI_SCHEMA_VERSIONS = [CURRENT_AI_SCHEMA_VERSION] as const;

export type AiFeature = "SYLLABUS_GENERATION" | "PLAN_GENERATION" | "CONTENT_GENERATION";
export type AiJobStatus = "RESERVED" | "PROCESSING" | "SUCCEEDED" | "FAILED" | "EXPIRED" | "CANCELLED";
export type AiQuotaReservationStatus = "RESERVED" | "CONSUMED" | "RELEASED";
export type RemoteSyllabusSyncState = "PENDING" | "SYNCED" | "FAILED";
export type AiPriority = "LOW" | "NORMAL" | "HIGH";
export type AiWarningSeverity = "INFO" | "WARNING" | "ERROR";
export type AiWarningCode =
  | "UNREADABLE_PAGES"
  | "PARTIAL_TEXT_EXTRACTION"
  | "AMBIGUOUS_STRUCTURE"
  | "INCOMPLETE_STRUCTURE"
  | "POSSIBLE_DUPLICATE_SECTION"
  | "DOCUMENT_MISMATCH"
  | "TRUNCATED_SOURCE";
export type PrivateSyllabusVisibility = "PRIVATE";
export type PrivateSyllabusSource = "AI_GENERATED" | "IMPORTED" | "MANUAL";
export type PrivateSyllabusStatus = "ACTIVE" | "ARCHIVED" | "DELETED";

export interface AiWarning {
  code: AiWarningCode;
  severity: AiWarningSeverity;
  message: string;
  sourcePages: number[];
  ambiguity: string | null;
}

export interface AiTopicProposal {
  name: string;
  position: number;
  children: AiTopicProposal[];
  sourcePages: number[];
}

export interface AiSubjectProposal {
  name: string;
  position: number;
  suggestedPriority: AiPriority;
  topics: AiTopicProposal[];
  sourcePages: number[];
}

export interface AiSyllabusProposal {
  schemaVersion: number;
  promptVersion: string;
  modelVersion: string;
  documentTitle: string;
  subjects: AiSubjectProposal[];
  warnings: AiWarning[];
  ambiguities: string[];
}

export interface AiQuota {
  feature: AiFeature;
  limit: number;
  successfulCount: number;
  reservedCount: number;
  remaining: number;
  periodStart: string;
}

export interface AiAccess {
  authenticated: boolean;
  betaAccess: boolean;
  feature: AiFeature;
  featureEnabled: boolean;
  quota: AiQuota | null;
  canUse: boolean;
  reasonCode: string | null;
}

export interface AiJob {
  jobId: string;
  feature: AiFeature;
  status: AiJobStatus;
  schemaVersion: number | null;
  promptVersion: string | null;
  modelVersion: string | null;
  proposal: AiSyllabusProposal | null;
  warnings: AiWarning[];
  errorCode: string | null;
  errorMessage: string | null;
  createdAt: string;
  updatedAt: string;
  finishedAt: string | null;
  providerExecutionStartedAt: string | null;
}

export interface PrivateSyllabusTopic {
  remoteTopicId: string;
  externalId: string;
  parentRemoteTopicId: string | null;
  name: string;
  position: number;
  packageVersion: string;
  schemaVersion: number;
  metadata: Record<string, unknown>;
  children: PrivateSyllabusTopic[];
}

export interface PrivateSyllabusSubject {
  remoteSubjectId: string;
  externalId: string;
  name: string;
  position: number;
  suggestedPriority: AiPriority;
  packageVersion: string;
  schemaVersion: number;
  metadata: Record<string, unknown>;
  topics: PrivateSyllabusTopic[];
}

export interface PrivateSyllabus {
  remoteSyllabusId: string;
  title: string;
  position: number;
  visibility: PrivateSyllabusVisibility;
  source: PrivateSyllabusSource;
  sourceJobId: string | null;
  sourceHash: string | null;
  schemaVersion: number;
  status: PrivateSyllabusStatus;
  metadata: Record<string, unknown>;
  subjects: PrivateSyllabusSubject[];
}

export interface RemoteSyllabusSyncAcknowledgement {
  remoteSyllabusId: string | null;
  jobId: string | null;
  payloadHash: string;
  state: RemoteSyllabusSyncState;
  attemptCount: number;
  nextAttemptAt: string | null;
  safeError: string | null;
  createdAt: string;
  updatedAt: string;
  attemptToken: string | null;
}

export class ContractValidationError extends Error {
  constructor(public readonly path: string, message: string) {
    super(`${path}: ${message}`);
    this.name = "ContractValidationError";
  }
}

const AI_FEATURES: readonly AiFeature[] = ["SYLLABUS_GENERATION", "PLAN_GENERATION", "CONTENT_GENERATION"];
const AI_JOB_STATUSES: readonly AiJobStatus[] = ["RESERVED", "PROCESSING", "SUCCEEDED", "FAILED", "EXPIRED", "CANCELLED"];
const PRIORITIES: readonly AiPriority[] = ["LOW", "NORMAL", "HIGH"];
const WARNING_SEVERITIES: readonly AiWarningSeverity[] = ["INFO", "WARNING", "ERROR"];
const WARNING_CODES: readonly AiWarningCode[] = [
  "UNREADABLE_PAGES",
  "PARTIAL_TEXT_EXTRACTION",
  "AMBIGUOUS_STRUCTURE",
  "INCOMPLETE_STRUCTURE",
  "POSSIBLE_DUPLICATE_SECTION",
  "DOCUMENT_MISMATCH",
  "TRUNCATED_SOURCE",
];
const SYNC_STATES: readonly RemoteSyllabusSyncState[] = ["PENDING", "SYNCED", "FAILED"];

type JsonObject = Record<string, unknown>;

function fail(path: string, message: string): never {
  throw new ContractValidationError(path, message);
}

function object(value: unknown, path: string): JsonObject {
  if (value === null || typeof value !== "object" || Array.isArray(value)) fail(path, "must be an object");
  return value as JsonObject;
}

function exactKeys(value: JsonObject, expected: readonly string[], path: string): void {
  const allowed = new Set(expected);
  const unknown = Object.keys(value).filter((key) => !allowed.has(key));
  if (unknown.length > 0) fail(path, `unknown field(s): ${unknown.join(", ")}`);
}

function required(value: JsonObject, key: string, path: string): unknown {
  if (!Object.prototype.hasOwnProperty.call(value, key)) fail(path, `missing required field ${key}`);
  return value[key];
}

function stringValue(value: unknown, path: string): string {
  if (typeof value !== "string" || value.trim().length === 0) fail(path, "must be a non-empty string");
  return value;
}

function nullableString(value: unknown, path: string): string | null {
  return value === null ? null : stringValue(value, path);
}

function integer(value: unknown, path: string, minimum = 0): number {
  if (typeof value !== "number" || !Number.isInteger(value) || value < minimum) {
    fail(path, `must be an integer >= ${minimum}`);
  }
  return value;
}

function booleanValue(value: unknown, path: string): boolean {
  if (typeof value !== "boolean") fail(path, "must be a boolean");
  return value;
}

function enumValue<T extends string>(value: unknown, values: readonly T[], path: string): T {
  if (typeof value !== "string" || !values.includes(value as T)) fail(path, `unsupported value ${String(value)}`);
  return value as T;
}

function arrayValue(value: unknown, path: string, minItems = 0): unknown[] {
  if (!Array.isArray(value) || value.length < minItems) fail(path, `must be an array with at least ${minItems} item(s)`);
  return value;
}

function jsonObject(value: unknown, path: string): Record<string, unknown> {
  return object(value, path);
}

function dateTime(value: unknown, path: string): string {
  const text = stringValue(value, path);
  const match = /^(\d{4})-(\d{2})-(\d{2})T(\d{2}):(\d{2}):(\d{2})(?:\.(\d{1,9}))?(Z|([+-])(\d{2}):(\d{2}))$/.exec(text);
  if (match === null) fail(path, "must be an ISO-8601 date-time");

  const year = Number(match[1]);
  const month = Number(match[2]);
  const day = Number(match[3]);
  const hour = Number(match[4]);
  const minute = Number(match[5]);
  const second = Number(match[6]);
  const offsetHour = match[10] === undefined ? 0 : Number(match[10]);
  const offsetMinute = match[11] === undefined ? 0 : Number(match[11]);
  const leapYear = year % 4 === 0 && (year % 100 !== 0 || year % 400 === 0);
  const daysInMonth = [31, leapYear ? 29 : 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];

  if (
    month < 1 || month > 12 ||
    day < 1 || day > daysInMonth[month - 1] ||
    hour > 24 || minute > 59 || second > 60 ||
    (second === 60 && (hour !== 23 || minute !== 59)) ||
    (hour === 24 && (minute !== 0 || second !== 0)) ||
    offsetMinute > 59 || offsetHour > 18 || (offsetHour === 18 && offsetMinute !== 0)
  ) {
    fail(path, "must be an ISO-8601 date-time");
  }
  return text;
}

function pages(value: unknown, path: string): number[] {
  const values = arrayValue(value, path, 1).map((page, index) => integer(page, `${path}[${index}]`, 1));
  if (new Set(values).size !== values.length) fail(path, "must not contain duplicate pages");
  return values;
}

function assertDistinct(values: readonly number[], path: string): void {
  if (new Set(values).size !== values.length) fail(path, "positions must be unique among siblings");
}

function assertSupportedSchemaVersion(value: unknown, path: string): number {
  const version = integer(value, path, 1);
  if (!SUPPORTED_AI_SCHEMA_VERSIONS.includes(version as (typeof SUPPORTED_AI_SCHEMA_VERSIONS)[number])) {
    fail(path, `unsupported schema version ${version}`);
  }
  return version;
}

function warning(value: unknown, path: string): AiWarning {
  const item = object(value, path);
  exactKeys(item, ["code", "severity", "message", "sourcePages", "ambiguity"], path);
  return {
    code: enumValue(required(item, "code", path), WARNING_CODES, `${path}.code`),
    severity: enumValue(required(item, "severity", path), WARNING_SEVERITIES, `${path}.severity`),
    message: stringValue(required(item, "message", path), `${path}.message`),
    sourcePages: pages(required(item, "sourcePages", path), `${path}.sourcePages`),
    ambiguity: nullableString(required(item, "ambiguity", path), `${path}.ambiguity`),
  };
}

function topic(value: unknown, path: string): AiTopicProposal {
  const item = object(value, path);
  exactKeys(item, ["name", "position", "children", "sourcePages"], path);
  const children = arrayValue(required(item, "children", path), `${path}.children`).map((child, index) => topic(child, `${path}.children[${index}]`));
  assertDistinct(children.map((child) => child.position), `${path}.children`);
  return {
    name: stringValue(required(item, "name", path), `${path}.name`),
    position: integer(required(item, "position", path), `${path}.position`),
    children,
    sourcePages: pages(required(item, "sourcePages", path), `${path}.sourcePages`),
  };
}

function subject(value: unknown, path: string): AiSubjectProposal {
  const item = object(value, path);
  exactKeys(item, ["name", "position", "suggestedPriority", "topics", "sourcePages"], path);
  const topics = arrayValue(required(item, "topics", path), `${path}.topics`, 1).map((item, index) => topic(item, `${path}.topics[${index}]`));
  assertDistinct(topics.map((item) => item.position), `${path}.topics`);
  return {
    name: stringValue(required(item, "name", path), `${path}.name`),
    position: integer(required(item, "position", path), `${path}.position`),
    suggestedPriority: enumValue(required(item, "suggestedPriority", path), PRIORITIES, `${path}.suggestedPriority`),
    topics,
    sourcePages: pages(required(item, "sourcePages", path), `${path}.sourcePages`),
  };
}

export function parseAiSyllabusProposal(value: unknown): AiSyllabusProposal {
  const item = object(value, "proposal");
  exactKeys(item, ["schemaVersion", "promptVersion", "modelVersion", "documentTitle", "subjects", "warnings", "ambiguities"], "proposal");
  const subjects = arrayValue(required(item, "subjects", "proposal"), "proposal.subjects", 1).map((value, index) => subject(value, `proposal.subjects[${index}]`));
  assertDistinct(subjects.map((value) => value.position), "proposal.subjects");
  const ambiguities = arrayValue(required(item, "ambiguities", "proposal"), "proposal.ambiguities").map((value, index) => stringValue(value, `proposal.ambiguities[${index}]`));
  if (new Set(ambiguities).size !== ambiguities.length) fail("proposal.ambiguities", "must not contain duplicate entries");
  return {
    schemaVersion: assertSupportedSchemaVersion(required(item, "schemaVersion", "proposal"), "proposal.schemaVersion"),
    promptVersion: stringValue(required(item, "promptVersion", "proposal"), "proposal.promptVersion"),
    modelVersion: stringValue(required(item, "modelVersion", "proposal"), "proposal.modelVersion"),
    documentTitle: stringValue(required(item, "documentTitle", "proposal"), "proposal.documentTitle"),
    subjects,
    warnings: arrayValue(required(item, "warnings", "proposal"), "proposal.warnings").map((value, index) => warning(value, `proposal.warnings[${index}]`)),
    ambiguities,
  };
}

export function parseAiSyllabusProposalJson(raw: string): AiSyllabusProposal {
  return parseProviderAiSyllabusProposal(raw);
}

/**
 * The only provider-output acceptance boundary. The JSON Schema describes the
 * versioned wire shape, while this parser enforces runtime-only invariants
 * such as non-whitespace text and unique sibling positions.
 */
export function parseProviderAiSyllabusProposal(raw: string): AiSyllabusProposal {
  let value: unknown;
  try {
    value = JSON.parse(raw);
  } catch (error) {
    fail("proposal", `invalid JSON: ${error instanceof Error ? error.message : String(error)}`);
  }
  const item = object(value, "proposal");
  assertSupportedSchemaVersion(required(item, "schemaVersion", "proposal"), "proposal.schemaVersion");
  return parseAiSyllabusProposal(item);
}

function quota(value: unknown, path: string): AiQuota {
  const item = object(value, path);
  exactKeys(item, ["feature", "limit", "successfulCount", "reservedCount", "remaining", "periodStart"], path);
  const limit = integer(required(item, "limit", path), `${path}.limit`, 1);
  const successfulCount = integer(required(item, "successfulCount", path), `${path}.successfulCount`);
  const reservedCount = integer(required(item, "reservedCount", path), `${path}.reservedCount`);
  const remaining = integer(required(item, "remaining", path), `${path}.remaining`);
  if (successfulCount + reservedCount + remaining !== limit) fail(path, "successfulCount + reservedCount + remaining must equal limit");
  return {
    feature: enumValue(required(item, "feature", path), AI_FEATURES, `${path}.feature`),
    limit,
    successfulCount,
    reservedCount,
    remaining,
    periodStart: stringValue(required(item, "periodStart", path), `${path}.periodStart`),
  };
}

export function parseAiAccess(value: unknown): AiAccess {
  const item = object(value, "access");
  exactKeys(item, ["authenticated", "betaAccess", "feature", "featureEnabled", "quota", "canUse", "reasonCode"], "access");
  return {
    authenticated: booleanValue(required(item, "authenticated", "access"), "access.authenticated"),
    betaAccess: booleanValue(required(item, "betaAccess", "access"), "access.betaAccess"),
    feature: enumValue(required(item, "feature", "access"), AI_FEATURES, "access.feature"),
    featureEnabled: booleanValue(required(item, "featureEnabled", "access"), "access.featureEnabled"),
    quota: required(item, "quota", "access") === null ? null : quota(required(item, "quota", "access"), "access.quota"),
    canUse: booleanValue(required(item, "canUse", "access"), "access.canUse"),
    reasonCode: nullableString(required(item, "reasonCode", "access"), "access.reasonCode"),
  };
}

export function parseAiJob(value: unknown): AiJob {
  const item = object(value, "job");
  exactKeys(item, ["jobId", "feature", "status", "schemaVersion", "promptVersion", "modelVersion", "proposal", "warnings", "errorCode", "errorMessage", "createdAt", "updatedAt", "finishedAt", "providerExecutionStartedAt"], "job");
  const schemaVersion = required(item, "schemaVersion", "job");
  const proposal = required(item, "proposal", "job");
  return {
    jobId: stringValue(required(item, "jobId", "job"), "job.jobId"),
    feature: enumValue(required(item, "feature", "job"), AI_FEATURES, "job.feature"),
    status: enumValue(required(item, "status", "job"), AI_JOB_STATUSES, "job.status"),
    schemaVersion: schemaVersion === null ? null : assertSupportedSchemaVersion(schemaVersion, "job.schemaVersion"),
    promptVersion: nullableString(required(item, "promptVersion", "job"), "job.promptVersion"),
    modelVersion: nullableString(required(item, "modelVersion", "job"), "job.modelVersion"),
    proposal: proposal === null ? null : parseAiSyllabusProposal(proposal),
    warnings: arrayValue(required(item, "warnings", "job"), "job.warnings").map((value, index) => warning(value, `job.warnings[${index}]`)),
    errorCode: nullableString(required(item, "errorCode", "job"), "job.errorCode"),
    errorMessage: nullableString(required(item, "errorMessage", "job"), "job.errorMessage"),
    createdAt: dateTime(required(item, "createdAt", "job"), "job.createdAt"),
    updatedAt: dateTime(required(item, "updatedAt", "job"), "job.updatedAt"),
    finishedAt: required(item, "finishedAt", "job") === null ? null : dateTime(required(item, "finishedAt", "job"), "job.finishedAt"),
    providerExecutionStartedAt: required(item, "providerExecutionStartedAt", "job") === null ? null : dateTime(required(item, "providerExecutionStartedAt", "job"), "job.providerExecutionStartedAt"),
  };
}

function hash(value: unknown, path: string): string | null {
  if (value === null) return null;
  const result = stringValue(value, path);
  if (!/^[0-9a-f]{64}$/.test(result)) fail(path, "must be a lowercase SHA-256 hash");
  return result;
}

function remoteTopic(value: unknown, path: string, expectedParentRemoteTopicId: string | null = null): PrivateSyllabusTopic {
  const item = object(value, path);
  exactKeys(item, ["remoteTopicId", "externalId", "parentRemoteTopicId", "name", "position", "packageVersion", "schemaVersion", "metadata", "children"], path);
  const remoteTopicId = stringValue(required(item, "remoteTopicId", path), `${path}.remoteTopicId`);
  const parentRemoteTopicId = nullableString(required(item, "parentRemoteTopicId", path), `${path}.parentRemoteTopicId`);
  if (parentRemoteTopicId !== expectedParentRemoteTopicId) fail(`${path}.parentRemoteTopicId`, "must point to the containing topic");
  const children = arrayValue(required(item, "children", path), `${path}.children`).map((value, index) => remoteTopic(value, `${path}.children[${index}]`, remoteTopicId));
  assertDistinct(children.map((value) => value.position), `${path}.children`);
  return {
    remoteTopicId,
    externalId: stringValue(required(item, "externalId", path), `${path}.externalId`),
    parentRemoteTopicId,
    name: stringValue(required(item, "name", path), `${path}.name`),
    position: integer(required(item, "position", path), `${path}.position`),
    packageVersion: stringValue(required(item, "packageVersion", path), `${path}.packageVersion`),
    schemaVersion: assertSupportedSchemaVersion(required(item, "schemaVersion", path), `${path}.schemaVersion`),
    metadata: jsonObject(required(item, "metadata", path), `${path}.metadata`),
    children,
  };
}

function remoteSubject(value: unknown, path: string): PrivateSyllabusSubject {
  const item = object(value, path);
  exactKeys(item, ["remoteSubjectId", "externalId", "name", "position", "suggestedPriority", "packageVersion", "schemaVersion", "metadata", "topics"], path);
  const topics = arrayValue(required(item, "topics", path), `${path}.topics`).map((value, index) => remoteTopic(value, `${path}.topics[${index}]`));
  assertDistinct(topics.map((value) => value.position), `${path}.topics`);
  const externalIds = topics.flatMap((value) => [value.externalId, ...descendantExternalIds(value)]);
  if (new Set(externalIds).size !== externalIds.length) fail(`${path}.topics`, "externalId must be unique within a subject");
  return {
    remoteSubjectId: stringValue(required(item, "remoteSubjectId", path), `${path}.remoteSubjectId`),
    externalId: stringValue(required(item, "externalId", path), `${path}.externalId`),
    name: stringValue(required(item, "name", path), `${path}.name`),
    position: integer(required(item, "position", path), `${path}.position`),
    suggestedPriority: enumValue(required(item, "suggestedPriority", path), PRIORITIES, `${path}.suggestedPriority`),
    packageVersion: stringValue(required(item, "packageVersion", path), `${path}.packageVersion`),
    schemaVersion: assertSupportedSchemaVersion(required(item, "schemaVersion", path), `${path}.schemaVersion`),
    metadata: jsonObject(required(item, "metadata", path), `${path}.metadata`),
    topics,
  };
}

function descendantExternalIds(value: PrivateSyllabusTopic): string[] {
  return value.children.flatMap((child) => [child.externalId, ...descendantExternalIds(child)]);
}

function descendantRemoteTopicIds(value: PrivateSyllabusTopic): string[] {
  return [value.remoteTopicId, ...value.children.flatMap((child) => descendantRemoteTopicIds(child))];
}

export function parsePrivateSyllabus(value: unknown): PrivateSyllabus {
  const item = object(value, "privateSyllabus");
  exactKeys(item, ["remoteSyllabusId", "title", "position", "visibility", "source", "sourceJobId", "sourceHash", "schemaVersion", "status", "metadata", "subjects"], "privateSyllabus");
  const subjects = arrayValue(required(item, "subjects", "privateSyllabus"), "privateSyllabus.subjects", 1).map((value, index) => remoteSubject(value, `privateSyllabus.subjects[${index}]`));
  assertDistinct(subjects.map((value) => value.position), "privateSyllabus.subjects");
  if (new Set(subjects.map((value) => value.externalId)).size !== subjects.length) fail("privateSyllabus.subjects", "externalId must be unique");
  if (new Set(subjects.map((value) => value.remoteSubjectId)).size !== subjects.length) fail("privateSyllabus.subjects", "remoteSubjectId must be unique");
  const remoteTopicIds = subjects.flatMap((subject) => subject.topics.flatMap((topic) => descendantRemoteTopicIds(topic)));
  if (new Set(remoteTopicIds).size !== remoteTopicIds.length) fail("privateSyllabus.subjects", "remoteTopicId must be unique");
  return {
    remoteSyllabusId: stringValue(required(item, "remoteSyllabusId", "privateSyllabus"), "privateSyllabus.remoteSyllabusId"),
    title: stringValue(required(item, "title", "privateSyllabus"), "privateSyllabus.title"),
    position: integer(required(item, "position", "privateSyllabus"), "privateSyllabus.position"),
    visibility: enumValue(required(item, "visibility", "privateSyllabus"), ["PRIVATE"], "privateSyllabus.visibility"),
    source: enumValue(required(item, "source", "privateSyllabus"), ["AI_GENERATED", "IMPORTED", "MANUAL"], "privateSyllabus.source"),
    sourceJobId: nullableString(required(item, "sourceJobId", "privateSyllabus"), "privateSyllabus.sourceJobId"),
    sourceHash: hash(required(item, "sourceHash", "privateSyllabus"), "privateSyllabus.sourceHash"),
    schemaVersion: assertSupportedSchemaVersion(required(item, "schemaVersion", "privateSyllabus"), "privateSyllabus.schemaVersion"),
    status: enumValue(required(item, "status", "privateSyllabus"), ["ACTIVE", "ARCHIVED", "DELETED"], "privateSyllabus.status"),
    metadata: jsonObject(required(item, "metadata", "privateSyllabus"), "privateSyllabus.metadata"),
    subjects,
  };
}

export function parseRemoteSyllabusSyncAcknowledgement(value: unknown): RemoteSyllabusSyncAcknowledgement {
  const item = object(value, "syncAcknowledgement");
  exactKeys(item, ["remoteSyllabusId", "jobId", "payloadHash", "state", "attemptCount", "nextAttemptAt", "safeError", "createdAt", "updatedAt", "attemptToken"], "syncAcknowledgement");
  const payloadHash = hash(required(item, "payloadHash", "syncAcknowledgement"), "syncAcknowledgement.payloadHash");
  if (payloadHash === null) fail("syncAcknowledgement.payloadHash", "must not be null");
  return {
    remoteSyllabusId: nullableString(required(item, "remoteSyllabusId", "syncAcknowledgement"), "syncAcknowledgement.remoteSyllabusId"),
    jobId: nullableString(required(item, "jobId", "syncAcknowledgement"), "syncAcknowledgement.jobId"),
    payloadHash,
    state: enumValue(required(item, "state", "syncAcknowledgement"), SYNC_STATES, "syncAcknowledgement.state"),
    attemptCount: integer(required(item, "attemptCount", "syncAcknowledgement"), "syncAcknowledgement.attemptCount"),
    nextAttemptAt: required(item, "nextAttemptAt", "syncAcknowledgement") === null ? null : dateTime(required(item, "nextAttemptAt", "syncAcknowledgement"), "syncAcknowledgement.nextAttemptAt"),
    safeError: nullableString(required(item, "safeError", "syncAcknowledgement"), "syncAcknowledgement.safeError"),
    createdAt: dateTime(required(item, "createdAt", "syncAcknowledgement"), "syncAcknowledgement.createdAt"),
    updatedAt: dateTime(required(item, "updatedAt", "syncAcknowledgement"), "syncAcknowledgement.updatedAt"),
    attemptToken: nullableString(required(item, "attemptToken", "syncAcknowledgement"), "syncAcknowledgement.attemptToken"),
  };
}
