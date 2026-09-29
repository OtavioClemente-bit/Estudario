import type { JsonSchema } from "../schema.ts";

// Formatos estruturados (strict) dos jobs de texto. Todo campo é obrigatório e nada além deles é
// aceito: é isso que impede o modelo de acrescentar blocos inventados. As regras que o JSON Schema
// não expressa (uma alternativa correta, referências existentes, capacidade do dia) ficam no
// validador do servidor.

export const AI_TOPIC_CONTENT_SCHEMA_VERSION = 1 as const;
export const AI_STUDY_PLAN_SCHEMA_VERSION = 1 as const;

const nonEmpty: JsonSchema = { type: "string", minLength: 1 };
const nullableText: JsonSchema = { anyOf: [{ type: "null" }, { type: "string", minLength: 1 }] };

const versionFields: Record<string, JsonSchema> = {
  schemaVersion: { type: "integer" },
  promptVersion: nonEmpty,
  modelVersion: nonEmpty,
};

const warning: JsonSchema = {
  type: "object",
  additionalProperties: false,
  required: ["code", "message"],
  properties: {
    code: {
      type: "string",
      enum: ["SOURCE_NOT_VERIFIED", "LAW_VERSION_UNCERTAIN", "SCOPE_AMBIGUOUS", "INSUFFICIENT_EVIDENCE", "CAPACITY_TIGHT", "DEADLINE_TIGHT"],
    },
    message: nonEmpty,
  },
};

export const AI_TOPIC_CONTENT_SCHEMA: JsonSchema = {
  type: "object",
  additionalProperties: false,
  required: [
    "schemaVersion", "promptVersion", "modelVersion", "scope", "theoryTitle", "chapters", "summary",
    "quickReview", "tips", "traps", "activeRecall", "errorConcepts", "questions", "sources", "warnings",
  ],
  properties: {
    ...versionFields,
    scope: {
      type: "object",
      additionalProperties: false,
      required: ["covers", "excludes"],
      properties: { covers: nonEmpty, excludes: { type: "string" } },
    },
    theoryTitle: nonEmpty,
    chapters: {
      type: "array",
      minItems: 2,
      maxItems: 6,
      items: {
        type: "object",
        additionalProperties: false,
        required: ["title", "markdown"],
        properties: { title: nonEmpty, markdown: nonEmpty },
      },
    },
    summary: nonEmpty,
    quickReview: nonEmpty,
    tips: { type: "array", maxItems: 8, items: nonEmpty },
    traps: { type: "array", maxItems: 8, items: nonEmpty },
    activeRecall: { type: "array", maxItems: 10, items: nonEmpty },
    errorConcepts: {
      type: "array",
      minItems: 1,
      maxItems: 6,
      items: {
        type: "object",
        additionalProperties: false,
        required: ["key", "title", "summary"],
        properties: { key: { type: "string", enum: ["e1", "e2", "e3", "e4", "e5", "e6"] }, title: nonEmpty, summary: nonEmpty },
      },
    },
    questions: {
      type: "array",
      minItems: 10,
      maxItems: 10,
      items: {
        type: "object",
        additionalProperties: false,
        required: [
          "statement", "format", "difficulty", "options", "explanation", "section", "errorConceptKey",
          "sourceType", "board", "agency", "year", "sourceUrl",
        ],
        properties: {
          statement: nonEmpty,
          format: { type: "string", enum: ["MULTIPLE_CHOICE", "TRUE_FALSE"] },
          difficulty: { type: "string", enum: ["FACIL", "MEDIA", "DIFICIL"] },
          options: {
            type: "array",
            minItems: 2,
            maxItems: 5,
            items: {
              type: "object",
              additionalProperties: false,
              required: ["key", "text", "correct"],
              properties: {
                key: { type: "string", enum: ["A", "B", "C", "D", "E"] },
                text: nonEmpty,
                correct: { type: "boolean" },
              },
            },
          },
          explanation: nonEmpty,
          section: nonEmpty,
          errorConceptKey: { type: "string", enum: ["e1", "e2", "e3", "e4", "e5", "e6"] },
          sourceType: { type: "string", enum: ["AUTHORIAL", "REAL"] },
          board: nullableText,
          agency: nullableText,
          year: { anyOf: [{ type: "null" }, { type: "integer" }] },
          sourceUrl: nullableText,
        },
      },
    },
    sources: {
      type: "array",
      minItems: 1,
      maxItems: 12,
      items: {
        type: "object",
        additionalProperties: false,
        required: ["kind", "title", "publisher", "reference", "url", "accessedAt"],
        properties: {
          kind: { type: "string", enum: ["OFICIAL", "COMPLEMENTAR"] },
          title: nonEmpty,
          publisher: nonEmpty,
          reference: { type: "string" },
          url: nonEmpty,
          accessedAt: nonEmpty,
        },
      },
    },
    warnings: { type: "array", maxItems: 10, items: warning },
  },
};

export const AI_STUDY_PLAN_SCHEMA: JsonSchema = {
  type: "object",
  additionalProperties: false,
  required: ["schemaVersion", "promptVersion", "modelVersion", "summary", "phases", "tasks", "warnings"],
  properties: {
    ...versionFields,
    summary: nonEmpty,
    phases: {
      type: "array",
      minItems: 1,
      maxItems: 6,
      items: {
        type: "object",
        additionalProperties: false,
        required: ["name", "objective", "fromDay", "toDay"],
        properties: {
          name: nonEmpty,
          objective: nonEmpty,
          fromDay: { type: "integer" },
          toDay: { type: "integer" },
        },
      },
    },
    // Tarefas em formato curto para economizar tokens: d = dia (0 = data de início), s/t = refs
    // enviadas pelo app, k = tipo, m = minutos, q = questões.
    tasks: {
      type: "array",
      minItems: 1,
      maxItems: 600,
      items: {
        type: "object",
        additionalProperties: false,
        required: ["d", "s", "t", "k", "m", "q"],
        properties: {
          d: { type: "integer" },
          s: { anyOf: [{ type: "null" }, { type: "string" }] },
          t: { anyOf: [{ type: "null" }, { type: "string" }] },
          k: { type: "string", enum: ["THEORY", "QUESTIONS", "REVIEW", "ACTIVE_RECALL", "SIMULATION"] },
          m: { type: "integer" },
          q: { type: "integer" },
        },
      },
    },
    warnings: { type: "array", maxItems: 10, items: warning },
  },
};
