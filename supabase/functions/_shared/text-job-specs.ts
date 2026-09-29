import type { AiJobSpec, SyllabusWorkerJob } from "../ai-syllabus-worker/index.ts";
import {
  AI_STUDY_PLAN_SCHEMA,
  AI_STUDY_PLAN_SCHEMA_VERSION,
  AI_TOPIC_CONTENT_SCHEMA,
  AI_TOPIC_CONTENT_SCHEMA_VERSION,
} from "./schemas/ai-text-jobs-v1.ts";
import {
  CONTENT_PROMPT_VERSION,
  CONTENT_SYSTEM_PROMPT,
  contentUserPrompt,
  PLAN_PROMPT_VERSION,
  PLAN_SYSTEM_PROMPT,
  planUserPrompt,
} from "./prompts/text-jobs-v1.ts";
import {
  type ContentJobInput,
  parseContentJobInput,
  parsePlanJobInput,
  type PlanJobInput,
} from "./text-job-input.ts";
import { validateStudyPlan, validateTopicContent } from "./text-job-validators.ts";
import type { WorkerProposal } from "../ai-syllabus-worker/index.ts";

// Especificações dos recursos de texto para o worker compartilhado.

function inputOf<T>(job: SyllabusWorkerJob, parse: (value: unknown) => T): T {
  try {
    return parse(job.requestPayload?.input);
  } catch {
    // Código definitivo: o job falha limpo e a cota volta, sem chamar o provedor.
    throw new Error("TEXT_JOB_INPUT_INVALID");
  }
}

export const CONTENT_JOB_SPEC: AiJobSpec = {
  feature: "CONTENT_GENERATION",
  promptVersion: CONTENT_PROMPT_VERSION,
  schemaVersion: AI_TOPIC_CONTENT_SCHEMA_VERSION,
  schema: AI_TOPIC_CONTENT_SCHEMA,
  schemaName: `ai_topic_content_v${AI_TOPIC_CONTENT_SCHEMA_VERSION}`,
  usesSource: false,
  // Pesquisa web é o que sustenta a barreira de evidência. "medium" equilibra qualidade e custo.
  tools: [{ type: "web_search", search_context_size: "medium" }],
  prompts: (job) => ({
    systemPrompt: CONTENT_SYSTEM_PROMPT,
    userPrompt: contentUserPrompt(inputOf<ContentJobInput>(job, parseContentJobInput)),
  }),
  validate: (raw, expected, job) =>
    Promise.resolve(
      validateTopicContent(raw, expected, inputOf<ContentJobInput>(job, parseContentJobInput).options) as unknown as WorkerProposal,
    ),
};

export const PLAN_JOB_SPEC: AiJobSpec = {
  feature: "PLAN_GENERATION",
  promptVersion: PLAN_PROMPT_VERSION,
  schemaVersion: AI_STUDY_PLAN_SCHEMA_VERSION,
  schema: AI_STUDY_PLAN_SCHEMA,
  schemaName: `ai_study_plan_v${AI_STUDY_PLAN_SCHEMA_VERSION}`,
  usesSource: false,
  prompts: (job) => ({
    systemPrompt: PLAN_SYSTEM_PROMPT,
    userPrompt: planUserPrompt(inputOf<PlanJobInput>(job, parsePlanJobInput)),
  }),
  validate: (raw, expected, job) =>
    Promise.resolve(
      validateStudyPlan(raw, expected, inputOf<PlanJobInput>(job, parsePlanJobInput)) as unknown as WorkerProposal,
    ),
};

export function textSpecFor(job: SyllabusWorkerJob): AiJobSpec {
  if (job.feature === "CONTENT_GENERATION") return CONTENT_JOB_SPEC;
  if (job.feature === "PLAN_GENERATION") return PLAN_JOB_SPEC;
  // A fila de texto só entrega esses dois; qualquer outro é dado corrompido.
  throw new Error("TEXT_JOB_INPUT_INVALID");
}
