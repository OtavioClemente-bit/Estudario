import { CURRENT_AI_SCHEMA_VERSION } from "./contracts.ts";

export type JsonSchema = Record<string, unknown>;

const warningSchema: JsonSchema = {
  type: "object",
  additionalProperties: false,
  required: ["code", "severity", "message", "sourcePages", "ambiguity"],
  properties: {
    code: {
      type: "string",
      enum: [
        "UNREADABLE_PAGES",
        "PARTIAL_TEXT_EXTRACTION",
        "AMBIGUOUS_STRUCTURE",
        "INCOMPLETE_STRUCTURE",
        "POSSIBLE_DUPLICATE_SECTION",
        "DOCUMENT_MISMATCH",
        "TRUNCATED_SOURCE",
      ],
    },
    severity: { type: "string", enum: ["INFO", "WARNING", "ERROR"] },
    message: { type: "string", minLength: 1 },
    sourcePages: { $ref: "#/$defs/sourcePages" },
    ambiguity: { type: ["string", "null"], minLength: 1 },
  },
};

const topicSchema: JsonSchema = {
  type: "object",
  additionalProperties: false,
  required: ["name", "position", "children", "sourcePages"],
  properties: {
    name: { type: "string", minLength: 1 },
    position: { type: "integer", minimum: 0 },
    children: { type: "array", items: { $ref: "#/$defs/topic" } },
    sourcePages: { $ref: "#/$defs/sourcePages" },
  },
};

const proposalSchemaV1: JsonSchema = {
  $schema: "https://json-schema.org/draft/2020-12/schema",
  $id: "https://estudario.com/contracts/ai-syllabus-proposal/v1",
  title: "AiSyllabusProposal v1",
  type: "object",
  additionalProperties: false,
  required: ["schemaVersion", "promptVersion", "modelVersion", "documentTitle", "subjects", "warnings", "ambiguities"],
  properties: {
    schemaVersion: { const: CURRENT_AI_SCHEMA_VERSION },
    promptVersion: { type: "string", minLength: 1 },
    modelVersion: { type: "string", minLength: 1 },
    documentTitle: { type: "string", minLength: 1 },
    subjects: {
      type: "array",
      minItems: 1,
      items: {
        type: "object",
        additionalProperties: false,
        required: ["name", "position", "suggestedPriority", "topics", "sourcePages"],
        properties: {
          name: { type: "string", minLength: 1 },
          position: { type: "integer", minimum: 0 },
          suggestedPriority: { type: "string", enum: ["LOW", "NORMAL", "HIGH"] },
          topics: { type: "array", minItems: 1, items: { $ref: "#/$defs/topic" } },
          sourcePages: { $ref: "#/$defs/sourcePages" },
        },
      },
    },
    warnings: { type: "array", items: { $ref: "#/$defs/warning" } },
    ambiguities: { type: "array", items: { type: "string", minLength: 1 } },
  },
  $defs: {
    sourcePages: { type: "array", minItems: 1, uniqueItems: true, items: { type: "integer", minimum: 1 } },
    warning: warningSchema,
    topic: topicSchema,
  },
};

export const AI_SYLLABUS_PROPOSAL_SCHEMAS: Readonly<Record<number, JsonSchema>> = {
  [CURRENT_AI_SCHEMA_VERSION]: proposalSchemaV1,
};

export function getAiSyllabusProposalSchema(schemaVersion: number): JsonSchema {
  const schema = AI_SYLLABUS_PROPOSAL_SCHEMAS[schemaVersion];
  if (!schema) throw new Error(`Unsupported AI syllabus proposal schema version: ${schemaVersion}`);
  return schema;
}
