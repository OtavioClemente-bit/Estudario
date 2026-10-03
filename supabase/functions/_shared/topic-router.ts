import type { JsonSchema } from "./schema.ts";
import type { OpenAiProvider, ProviderResponse } from "./openai-provider.ts";

// Enciclopédia: quando o nome do tópico do edital não bate com nenhum apelido, uma chamada curta
// (sem pesquisa, raciocínio baixo) escolhe quais matérias prontas cobrem o tópico. A escolha fica
// guardada; o próximo pedido com o mesmo tópico não chama a IA. Só "cobre por inteiro" é servido
// da biblioteca: cobertura parcial segue para a geração por IA, como antes.

export const ROUTER_PROMPT_VERSION = "topic-router-v1";

export interface CatalogEntry {
  id: string;
  title: string;
  subject: string;
  covers: string;
}

export interface TopicRoute {
  topicIds: string[];
  coverage: "FULL" | "PARTIAL" | "NONE";
}

export const TOPIC_ROUTE_SCHEMA: JsonSchema = {
  type: "object",
  additionalProperties: false,
  required: ["topicIds", "coverage", "reason"],
  properties: {
    topicIds: { type: "array", maxItems: 3, items: { type: "string" } },
    coverage: { type: "string", enum: ["FULL", "PARTIAL", "NONE"] },
    reason: { type: "string" },
  },
};

const SYSTEM = `Você liga tópicos de editais de concurso às matérias prontas de uma biblioteca.
Recebe o tópico (com a disciplina) e o catálogo de matérias (id, título, disciplina e o que cada uma abrange).
Escolha o MENOR conjunto de matérias (até 3) que, juntas, cobrem o conteúdo do tópico.
"coverage": FULL só se as matérias escolhidas cobrem todo o conteúdo pedido no tópico; PARTIAL se cobrem só parte (o tópico pede assuntos que nenhuma matéria tem); NONE se nenhuma serve.
Não force: matéria de outra disciplina ou de assunto apenas parecido não serve. Na dúvida entre FULL e PARTIAL, escolha PARTIAL.
Use só ids que estão no catálogo.`;

export function routerUserPrompt(subject: string, topic: string, catalog: CatalogEntry[]): string {
  const lines = catalog.map((entry) => `- ${entry.id} | ${entry.subject} | ${entry.title} | ${entry.covers.slice(0, 400)}`);
  return `DISCIPLINA: ${subject}\nTÓPICO DO EDITAL: ${topic}\n\nCATÁLOGO:\n${lines.join("\n")}`;
}

/** Valida a resposta da IA contra o catálogo: id inexistente derruba para NONE. */
export function parseRoute(raw: string | null, catalog: CatalogEntry[]): TopicRoute | null {
  try {
    const value = JSON.parse(raw ?? "");
    const known = new Set(catalog.map((entry) => entry.id));
    const ids = Array.isArray(value.topicIds) ? [...new Set(value.topicIds.map(String))] as string[] : [];
    if (!["FULL", "PARTIAL", "NONE"].includes(value.coverage)) return null;
    if (ids.some((id) => !known.has(id))) return { topicIds: [], coverage: "NONE" };
    if (value.coverage !== "NONE" && ids.length === 0) return { topicIds: [], coverage: "NONE" };
    return { topicIds: ids.slice(0, 3), coverage: value.coverage };
  } catch {
    return null;
  }
}

export interface RouteOptions {
  provider: OpenAiProvider;
  jobId: string;
  model: string;
  subject: string;
  topic: string;
  catalog: CatalogEntry[];
  onFinished?: (response: ProviderResponse) => Promise<void>;
  sleep?: (ms: number) => Promise<void>;
  now?: () => number;
  budgetMs?: number;
}

/** Pergunta à IA quais matérias cobrem o tópico. Nulo se não deu (quem chama segue sem biblioteca). */
export async function routeTopic(options: RouteOptions): Promise<TopicRoute | null> {
  if (options.catalog.length === 0) return { topicIds: [], coverage: "NONE" };
  const sleep = options.sleep ?? ((ms: number) => new Promise((resolve) => setTimeout(resolve, ms)));
  const now = options.now ?? Date.now;
  const deadline = now() + (options.budgetMs ?? 60_000);
  try {
    let response = await options.provider.start({
      jobId: options.jobId,
      idempotencyKey: `${options.jobId}:route`,
      feature: "TOPIC_ROUTE",
      schemaName: "topic_route_v1",
      schema: TOPIC_ROUTE_SCHEMA,
      systemPrompt: SYSTEM,
      userPrompt: routerUserPrompt(options.subject, options.topic, options.catalog),
      promptVersion: ROUTER_PROMPT_VERSION,
      schemaVersion: 1,
      model: options.model,
      reasoningEffort: "low",
      background: true,
      store: true,
      maxOutputTokens: 1_500,
    });
    while (response.status === "queued" || response.status === "in_progress") {
      if (now() >= deadline) {
        await options.provider.cancel(response.id).catch(() => undefined);
        return null;
      }
      await sleep(2_000);
      response = await options.provider.retrieve(response.id);
    }
    await options.onFinished?.(response).catch(() => undefined);
    return response.status === "completed" ? parseRoute(response.outputText, options.catalog) : null;
  } catch {
    return null;
  }
}
