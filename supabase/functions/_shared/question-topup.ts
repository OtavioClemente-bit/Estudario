import type { JsonSchema } from "./schema.ts";
import type { OpenAiProvider, ProviderResponse } from "./openai-provider.ts";
import { AI_TOPIC_CONTENT_SCHEMA } from "./schemas/ai-text-jobs-v1.ts";
import { CONTENT_SYSTEM_PROMPT } from "./prompts/text-jobs-v1.ts";

// Reposição de questões. Quando questões com defeito saem do material e a reserva não cobre, o
// aluno receberia menos do que pediu. Uma chamada curta, sem pesquisa (a teoria já traz o que foi
// conferido), escreve só as que faltam a partir dos capítulos prontos.

type Json = Record<string, unknown>;

const questionsSchema = (AI_TOPIC_CONTENT_SCHEMA as { properties: Record<string, JsonSchema> }).properties.questions;

export const TOPUP_SCHEMA: JsonSchema = {
  type: "object",
  additionalProperties: false,
  required: ["questions"],
  properties: { questions: questionsSchema },
};

const STYLE = { MIXED: "MISTO", FIVE_OPTIONS: "MÚLTIPLA_A_E", FOUR_OPTIONS: "MÚLTIPLA_A_D", TRUE_FALSE: "CERTO_ERRADO" } as const;
const LEVEL = { MIXED: "MISTA", EASY: "FÁCIL", MEDIUM: "MÉDIA", HARD: "DIFÍCIL" } as const;

export interface TopUpRequest {
  content: Json;
  missing: number;
  questionStyle: keyof typeof STYLE;
  difficulty: keyof typeof LEVEL;
  context: string;
}

export function topUpUserPrompt(request: TopUpRequest): string {
  const chapters = (Array.isArray(request.content.chapters) ? request.content.chapters as Json[] : [])
    .map((chapter) => `## ${chapter.title}\n${String(chapter.markdown ?? "").slice(0, 3_500)}`).join("\n\n");
  const concepts = (Array.isArray(request.content.errorConcepts) ? request.content.errorConcepts as Json[] : [])
    .map((concept) => `${concept.key}: ${concept.title}`).join("; ");
  const existing = (Array.isArray(request.content.questions) ? request.content.questions as Json[] : [])
    .map((question, index) => `${index + 1}. ${String(question.statement ?? "").slice(0, 300)}`).join("\n");
  return [
    `Escreva exatamente ${request.missing} questão(ões) NOVA(S) para completar o material abaixo. Só o campo questions.`,
    `DADOS: ${request.context}`,
    `Formato: ${STYLE[request.questionStyle]}. Dificuldade: ${LEVEL[request.difficulty]}.`,
    "Use somente o que está nos capítulos; section = título EXATO de um capítulo; errorConceptKey = uma destas chaves ou null: " + (concepts || "nenhuma"),
    "Não repita o ponto cobrado nem o raciocínio destas questões já existentes:",
    existing || "(nenhuma)",
    "",
    "CAPÍTULOS:",
    chapters,
  ].join("\n");
}

export interface TopUpOptions {
  provider: OpenAiProvider;
  jobId: string;
  model: string;
  budgetMs?: number;
  pollMs?: number;
  sleep?: (ms: number) => Promise<void>;
  now?: () => number;
  onFinished?: (response: ProviderResponse) => Promise<void>;
}

/** Questões novas para completar o pedido, ou lista vazia se não deu (o material segue como está). */
export async function runQuestionTopUp(request: TopUpRequest, options: TopUpOptions): Promise<Json[]> {
  if (request.missing <= 0) return [];
  const sleep = options.sleep ?? ((ms: number) => new Promise((resolve) => setTimeout(resolve, ms)));
  const now = options.now ?? Date.now;
  const deadline = now() + (options.budgetMs ?? 120_000);
  try {
    let response = await options.provider.start({
      jobId: options.jobId,
      idempotencyKey: `${options.jobId}:topup`,
      feature: "CONTENT_TOPUP",
      schemaName: "content_topup_v1",
      systemPrompt: CONTENT_SYSTEM_PROMPT,
      userPrompt: topUpUserPrompt(request),
      promptVersion: "content-topup-v1",
      schemaVersion: 1,
      schema: TOPUP_SCHEMA,
      model: options.model,
      background: true,
      store: true,
      maxOutputTokens: 8_000,
    });
    while (response.status === "queued" || response.status === "in_progress") {
      if (now() >= deadline) {
        await options.provider.cancel(response.id).catch(() => undefined);
        return [];
      }
      await sleep(options.pollMs ?? 4_000);
      response = await options.provider.retrieve(response.id);
    }
    await options.onFinished?.(response).catch(() => undefined);
    if (response.status !== "completed" || !response.outputText) return [];
    const parsed = JSON.parse(response.outputText) as { questions?: unknown };
    return Array.isArray(parsed.questions)
      ? parsed.questions.filter((q): q is Json => q !== null && typeof q === "object" && !Array.isArray(q)).slice(0, request.missing)
      : [];
  } catch {
    return [];
  }
}
