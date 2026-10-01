import type { AiJobSpec, SyllabusWorkerJob } from "../ai-syllabus-worker/index.ts";
import { meterCost } from "../ai-syllabus-worker/index.ts";
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
import { type ExpectedVersions, validateStudyPlan, validateTopicContent } from "./text-job-validators.ts";
import { applyReview, type ContentReview, runContentReview } from "./content-review.ts";
import { resolveOpenAiModel } from "./openai-provider.ts";
import {
  AI_SIMULATION_SCHEMA,
  parseSimulationJobInput,
  SIMULATION_PROMPT_VERSION,
  SIMULATION_SCHEMA_VERSION,
  SIMULATION_SYSTEM_PROMPT,
  type SimulationJobInput,
  simulationUserPrompt,
  validateSimulation,
} from "./simulation.ts";
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
  validate: async (raw, expected, job, dependencies) => {
    const input = inputOf<ContentJobInput>(job, parseContentJobInput);
    const content = validateTopicContent(raw, expected, input.options);
    if (Deno.env.get("CONTENT_REVIEW_ENABLED") === "false") return content as unknown as WorkerProposal;
    const review = await runContentReview(content, {
      provider: dependencies.provider,
      jobId: job.id,
      model: resolveOpenAiModel(dependencies.modelForJob?.(job) ?? dependencies.model),
      onFinished: (response) => meterCost(dependencies, job, "REVIEW", response),
      context: [input.competitionName, input.role, input.board ? `banca ${input.board}` : null, input.subjectName, input.topicPath.join(" › ")]
        .filter(Boolean).join(" · "),
    });
    return withReview(content, review, expected, input) as unknown as WorkerProposal;
  },
};

/**
 * Aplica a revisão e confere o formato de novo. Se a versão corrigida não passar (ex.: sobraram
 * poucos flashcards), tenta só as correções de texto e gabarito; em último caso, fica o original.
 */
export function withReview(
  content: Record<string, unknown>,
  review: ContentReview | null,
  expected: ExpectedVersions,
  input: ContentJobInput,
): Record<string, unknown> {
  const warn = (message: string) => ({
    ...content,
    warnings: [...(content.warnings as unknown[]).slice(0, 9), { code: "SOURCE_NOT_VERIFIED", message }],
  });
  if (review === null) return warn("A revisão automática de fatos não foi concluída para este material. Confira números e artigos na fonte oficial.");
  const attempts = [review, { ...review, removeQuestions: [], removeFlashcards: [] }];
  for (const attempt of attempts) {
    const { content: reviewed } = applyReview(content, attempt);
    const questions = (reviewed.questions as unknown[]).length;
    if (input.options.questionCount > 0 && questions === 0) continue;
    try {
      return validateTopicContent(JSON.stringify(reviewed), expected, { ...input.options, questionCount: questions });
    } catch {
      // Tenta a próxima forma, mais conservadora.
    }
  }
  return warn("A revisão automática encontrou pontos a conferir, mas não pôde aplicar as correções. Confira números e artigos na fonte oficial.");
}

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

export const SIMULATION_JOB_SPEC: AiJobSpec = {
  feature: "SIMULATION_GENERATION",
  promptVersion: SIMULATION_PROMPT_VERSION,
  schemaVersion: SIMULATION_SCHEMA_VERSION,
  schema: AI_SIMULATION_SCHEMA,
  schemaName: `ai_simulation_v${SIMULATION_SCHEMA_VERSION}`,
  usesSource: false,
  // Pesquisa para confirmar lei, número e a própria banca do concurso.
  tools: [{ type: "web_search", search_context_size: "medium" }],
  prompts: (job) => ({
    systemPrompt: SIMULATION_SYSTEM_PROMPT,
    userPrompt: simulationUserPrompt(inputOf<SimulationJobInput>(job, parseSimulationJobInput)),
  }),
  validate: async (raw, expected, job, dependencies) => {
    const input = inputOf<SimulationJobInput>(job, parseSimulationJobInput);
    const part = validateSimulation(raw, expected, input);
    if (Deno.env.get("CONTENT_REVIEW_ENABLED") === "false") return part as unknown as WorkerProposal;
    const review = await runContentReview({ questions: part.questions }, {
      provider: dependencies.provider,
      jobId: job.id,
      model: resolveOpenAiModel(dependencies.modelForJob?.(job) ?? dependencies.model),
      onFinished: (response) => meterCost(dependencies, job, "REVIEW", response),
      context: [input.competitionName, input.role, input.board ? `banca ${input.board}` : null, "simulado"].filter(Boolean).join(" · "),
    });
    return withSimulationReview(part, review, expected, input) as unknown as WorkerProposal;
  },
};

/** Mesma lógica da revisão do conteúdo: corrigido e revalidado, ou o original com aviso. */
export function withSimulationReview(
  part: Record<string, unknown>,
  review: ContentReview | null,
  expected: ExpectedVersions,
  input: SimulationJobInput,
): Record<string, unknown> {
  const warn = (message: string) => ({
    ...part,
    warnings: [...(part.warnings as unknown[]).slice(0, 9), { code: "SOURCE_NOT_VERIFIED", message }],
  });
  if (review === null) return warn("A revisão automática de fatos não foi concluída para esta parte do simulado.");
  for (const attempt of [review, { ...review, removeQuestions: [], removeFlashcards: [] }]) {
    const { content: reviewed } = applyReview(part, attempt);
    try {
      return validateSimulation(reviewed, expected, input, { allowShortfall: true });
    } catch {
      // Tenta a forma mais conservadora.
    }
  }
  return warn("A revisão automática encontrou pontos a conferir, mas não pôde aplicar as correções.");
}

export function textSpecFor(job: SyllabusWorkerJob): AiJobSpec {
  if (job.feature === "CONTENT_GENERATION") return CONTENT_JOB_SPEC;
  if (job.feature === "PLAN_GENERATION") return PLAN_JOB_SPEC;
  if (job.feature === "SIMULATION_GENERATION") return SIMULATION_JOB_SPEC;
  // A fila de texto só entrega esses três; qualquer outro é dado corrompido.
  throw new Error("TEXT_JOB_INPUT_INVALID");
}
