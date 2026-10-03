import type { JsonSchema } from "./schema.ts";
import type { OpenAiProvider, ProviderResponse } from "./openai-provider.ts";
import { validateBoardNote } from "./library.ts";

// Recorte automático de banca. Quando a matéria pronta existe mas não tem recorte da banca do
// concurso, ele é feito uma vez e guardado para todo mundo:
//  1. perfil da banca (uma vez por banca, com pesquisa web): estilo, o que mais cobra, pegadinhas;
//  2. recorte do tópico (uma vez por tópico e banca, sem pesquisa): parte do perfil e da matéria.
// Falha em qualquer passo nunca derruba o pedido: a matéria sai sem recorte, como antes.

type Json = Record<string, unknown>;

export const BOARD_PROFILE_PROMPT_VERSION = "board-profile-v1";
export const BOARD_NOTE_PROMPT_VERSION = "board-note-v1";
const SCHEMA_VERSION = 1;

export const BOARD_PROFILE_SCHEMA: JsonSchema = {
  type: "object",
  additionalProperties: false,
  required: ["board", "overview", "questionStyle", "bySubject", "traps", "sources"],
  properties: {
    board: { type: "string", minLength: 2 },
    // Como a banca é: formato das provas, nível, o que valoriza.
    overview: { type: "string", minLength: 200 },
    // Como as questões são escritas: enunciados, alternativas, tamanho de texto, Certo/Errado ou A–E.
    questionStyle: { type: "string", minLength: 100 },
    // O que costuma cobrar em cada disciplina comum de concurso, com base nas provas pesquisadas.
    bySubject: {
      type: "array",
      maxItems: 12,
      items: {
        type: "object",
        additionalProperties: false,
        required: ["subject", "notes"],
        properties: { subject: { type: "string" }, notes: { type: "string", minLength: 40 } },
      },
    },
    traps: { type: "array", maxItems: 10, items: { type: "string", minLength: 10 } },
    sources: {
      type: "array",
      maxItems: 10,
      items: {
        type: "object",
        additionalProperties: false,
        required: ["title", "url"],
        properties: { title: { type: "string" }, url: { type: "string" } },
      },
    },
  },
};

export const BOARD_NOTE_SCHEMA: JsonSchema = {
  type: "object",
  additionalProperties: false,
  required: ["howItFalls", "incidence", "tips", "traps"],
  properties: {
    howItFalls: { type: "string", minLength: 300 },
    incidence: { type: "string", enum: ["ALTA", "MEDIA", "BAIXA"] },
    tips: { type: "array", minItems: 3, maxItems: 4, items: { type: "string", minLength: 10 } },
    traps: { type: "array", minItems: 3, maxItems: 4, items: { type: "string", minLength: 10 } },
  },
};

const PROFILE_SYSTEM = `Você pesquisa bancas de concursos públicos brasileiros para um app de estudo.
Use a pesquisa web para encontrar provas anteriores, editais e análises da banca indicada. Descreva a banca com base no que encontrou: formato das provas, estilo dos enunciados e alternativas, nível, e o que ela costuma cobrar em cada disciplina.
Regras: só afirme o que as fontes sustentam. Nunca invente ano, cargo, questão ou estatística. Se não achar informação sobre uma disciplina, não a inclua. Cite em "sources" as páginas que realmente usou (provas, editais, análises). Escreva em português do Brasil.`;

const NOTE_SYSTEM = `Você escreve o recorte "como esta banca cobra este tópico" de um app de estudo para concursos.
Use SOMENTE o perfil da banca e a matéria fornecidos. Não invente provas, anos, cargos ou questões que caíram; fale do estilo e do que a banca tende a cobrar, conforme o perfil. Se o perfil não fala do assunto, descreva como o estilo geral da banca se aplica a este tópico.
"howItFalls": 300 a 900 caracteres: tipo de enunciado, o que mais aparece, profundidade e pegadinhas, ligado ao conteúdo da matéria. "tips" e "traps": 3 itens cada, curtos e práticos. "incidence": ALTA, MEDIA ou BAIXA, pela importância do tópico para a banca. Português do Brasil.`;

export interface BoardNoteStore {
  boardProfile?(boardNorm: string): Promise<Json | null>;
  saveBoardProfile?(boardNorm: string, board: string, profile: Json): Promise<void>;
  saveBoardNote?(topicId: string, boardNorm: string, board: string, note: Json): Promise<void>;
}

export interface BoardNoteOptions {
  provider: OpenAiProvider;
  store: BoardNoteStore;
  jobId: string;
  model: string;
  board: string;
  boardNorm: string;
  topicId: string;
  material: Json;
  onFinished?: (kind: "BOARD_PROFILE" | "BOARD_NOTE", response: ProviderResponse) => Promise<void>;
  sleep?: (ms: number) => Promise<void>;
  now?: () => number;
  pollMs?: number;
}

async function run(
  options: BoardNoteOptions,
  kind: "BOARD_PROFILE" | "BOARD_NOTE",
  input: { schemaName: string; schema: JsonSchema; system: string; user: string; tools?: unknown[]; promptVersion: string; maxOutputTokens: number; budgetMs: number },
): Promise<Json | null> {
  const sleep = options.sleep ?? ((ms: number) => new Promise((resolve) => setTimeout(resolve, ms)));
  const now = options.now ?? Date.now;
  const deadline = now() + input.budgetMs;
  let response = await options.provider.start({
    jobId: options.jobId,
    idempotencyKey: `${options.jobId}:${kind}:${options.boardNorm}`,
    feature: kind,
    schemaName: input.schemaName,
    schema: input.schema,
    tools: input.tools,
    systemPrompt: input.system,
    userPrompt: input.user,
    promptVersion: input.promptVersion,
    schemaVersion: SCHEMA_VERSION,
    model: options.model,
    background: true,
    store: true,
    maxOutputTokens: input.maxOutputTokens,
  });
  while (response.status === "queued" || response.status === "in_progress") {
    if (now() >= deadline) {
      await options.provider.cancel(response.id).catch(() => undefined);
      return null;
    }
    await sleep(options.pollMs ?? 4_000);
    response = await options.provider.retrieve(response.id);
  }
  await options.onFinished?.(kind, response).catch(() => undefined);
  if (response.status !== "completed") return null;
  try {
    const parsed = JSON.parse(response.outputText ?? "");
    return parsed && typeof parsed === "object" ? parsed as Json : null;
  } catch {
    return null;
  }
}

/** Perfil guardado da banca, ou um novo (com pesquisa web) se ainda não existe. */
async function profileOf(options: BoardNoteOptions): Promise<Json | null> {
  const saved = await options.store.boardProfile?.(options.boardNorm).catch(() => null);
  if (saved) return saved;
  const profile = await run(options, "BOARD_PROFILE", {
    schemaName: `board_profile_v${SCHEMA_VERSION}`,
    schema: BOARD_PROFILE_SCHEMA,
    system: PROFILE_SYSTEM,
    user: `Banca: ${options.board}\nPesquise provas e editais recentes desta banca e descreva o perfil dela.`,
    tools: [{ type: "web_search", search_context_size: "medium" }],
    promptVersion: BOARD_PROFILE_PROMPT_VERSION,
    maxOutputTokens: 6_000,
    budgetMs: 150_000,
  });
  if (!profile || typeof profile.overview !== "string") return null;
  await options.store.saveBoardProfile?.(options.boardNorm, options.board, profile).catch(() => undefined);
  return profile;
}

function materialDigest(material: Json): string {
  const chapters = (material.chapters as Json[] | undefined ?? []).map((c) => `- ${String(c.title)}`).join("\n");
  return `Título: ${String(material.title)}\nDisciplina: ${String(material.subject)}\nAbrange: ${String((material.scope as Json | undefined)?.covers ?? "")}\nCapítulos:\n${chapters}\nResumo: ${String(material.summary ?? "").slice(0, 1_500)}`;
}

/**
 * Recorte da banca para a matéria, feito na hora e guardado. Nulo se não der (sem banca, falha
 * da IA ou do formato): quem chama entrega a matéria sem recorte.
 */
export async function ensureBoardNote(options: BoardNoteOptions): Promise<Json | null> {
  try {
    const profile = await profileOf(options);
    if (!profile) return null;
    const draft = await run(options, "BOARD_NOTE", {
      schemaName: `board_note_v${SCHEMA_VERSION}`,
      schema: BOARD_NOTE_SCHEMA,
      system: NOTE_SYSTEM,
      user: `PERFIL DA BANCA (${options.board}):\n${JSON.stringify(profile).slice(0, 8_000)}\n\nMATÉRIA:\n${materialDigest(options.material)}`,
      promptVersion: BOARD_NOTE_PROMPT_VERSION,
      maxOutputTokens: 2_500,
      budgetMs: 90_000,
    });
    if (!draft) return null;
    const note: Json = {
      topic: options.topicId,
      board: options.board,
      role: null,
      version: 1,
      incidence: draft.incidence,
      howItFalls: draft.howItFalls,
      tips: draft.tips,
      traps: draft.traps,
      questions: [],
    };
    validateBoardNote(note, { ...options.material, id: options.topicId });
    await options.store.saveBoardNote?.(options.topicId, options.boardNorm, options.board, note).catch(() => undefined);
    return note;
  } catch {
    return null;
  }
}
