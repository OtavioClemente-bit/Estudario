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
  isPortuguese,
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
import { CACHE_MAX_AGE_DAYS, contentCacheKey } from "./content-cache.ts";
import { assembleFromLibrary, composeMaterials, foreignStateMaterial, GENERAL_BOARD_NORM, inferFocus, LIBRARY_MODEL_VERSION, lookupAliases, normalizeAlias, normalizeBoard } from "./library.ts";
import { planSimulationFromLibrary } from "./simulation-library.ts";
import { routeTopic } from "./topic-router.ts";
import type { LibraryHit } from "../ai-syllabus-worker/index.ts";
import { ensureBoardNote } from "./board-notes.ts";
import { type DropReason, hasDraftExplanation, repairTopicContent } from "./content-repair.ts";
import type { QuestionDrop } from "../ai-syllabus-worker/index.ts";
import { runQuestionTopUp } from "./question-topup.ts";
import { QUESTION_CHUNK } from "./prompts/text-jobs-v1.ts";
import {
  AI_SIMULATION_SCHEMA,
  parseSimulationJobInput,
  SIMULATION_PROMPT_VERSION,
  SIMULATION_SCHEMA_VERSION,
  SIMULATION_SYSTEM_PROMPT,
  type SimulationJobInput,
  type SimulationValidationOptions,
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
  // Questão de gramática com distrator sutil é onde o modelo mais erra: em Português ele pensa
  // mais antes de responder. PORTUGUESE_REASONING_EFFORT troca o nível ("off" desliga).
  reasoningEffort: (job) => {
    const subject = (job.requestPayload?.input as { subjectName?: unknown } | undefined)?.subjectName;
    if (typeof subject !== "string" || !isPortuguese(subject)) return undefined;
    const level = Deno.env.get("PORTUGUESE_REASONING_EFFORT")?.trim() || "medium";
    return level === "low" || level === "medium" || level === "high" ? level : undefined;
  },
  // Grátis: das matérias do mês, no máximo FREE_AI_MATERIALS escritas pela IA (as da biblioteca
  // não contam). Lote extra não entra aqui: tem cota própria e é pequeno.
  beforeProvider: async (job, dependencies) => {
    if (job.feature !== "CONTENT_GENERATION" || !dependencies.jobs.aiMaterialsThisMonth) return null;
    const { tier, used } = await dependencies.jobs.aiMaterialsThisMonth(job.userId);
    return tier === "FREE" && used >= FREE_AI_MATERIALS ? "PLAN_AI_MATERIAL_LIMIT" : null;
  },
  cached: async (job, dependencies) => {
    const store = dependencies.jobs;
    const input = inputOf<ContentJobInput>(job, parseContentJobInput);
    const fromLibrary = await libraryContent(job, input, dependencies).catch(() => null);
    if (fromLibrary) return fromLibrary;
    if (!store.cachedContent || !store.markServed) return null;
    // Pedido de "mais questões" depende do que a pessoa já tem: nunca vem do material guardado.
    if (input.avoidStatements.length > 0) return null;
    const key = await contentCacheKey(input);
    const proposal = await store.cachedContent(key, CONTENT_PROMPT_VERSION, job.userId, CACHE_MAX_AGE_DAYS);
    if (!proposal) return null;
    await store.markServed(key, job.userId);
    return proposal as unknown as WorkerProposal;
  },
  validate: async (raw, expected, job, dependencies) => {
    const result = await validateContent(raw, expected, job, dependencies);
    // Guarda para o próximo pedido igual; falha aqui nunca derruba a entrega. Pedido com "questões a
    // evitar" não entra: essa lista vai para a IA mas fica fora da chave, e um texto escrito nela
    // poderia mudar o material que outras pessoas recebem do mesmo tópico.
    const input = inputOf<ContentJobInput>(job, parseContentJobInput);
    if (input.avoidStatements.length > 0) return result;
    await contentCacheKey(input)
      .then((key) => dependencies.jobs.storeContent?.(key, CONTENT_PROMPT_VERSION, job.userId, job.id, result as unknown as Record<string, unknown>))
      .catch(() => undefined);
    return result;
  },
};

/**
 * Material montado da biblioteca de matérias prontas (sem custo de IA). "Mais questões" também sai
 * daqui, sem repetir as que a pessoa já tem. Quem pede de novo recebe a mesma matéria com as
 * questões em outra ordem (o embaralhamento usa o id do pedido), sem esperar a IA. Tópico que não está na biblioteca entra na fila do que gerar.
 */
async function libraryContent(
  job: SyllabusWorkerJob,
  input: ContentJobInput,
  dependencies: Parameters<NonNullable<AiJobSpec["cached"]>>[1],
): Promise<WorkerProposal | null> {
  const store = dependencies.jobs;
  if (!store.libraryLookup) return null;
  // Grátis: só o que está pronto, sem nenhuma chamada de IA (nem rota nova, nem recorte de banca,
  // nem questões completadas). Sem saber o plano, vale como Grátis.
  const free = store.planTierOf ? (await store.planTierOf(job.userId).catch(() => "FREE")) === "FREE" : false;
  const aliases = lookupAliases(input);
  let found = await store.libraryLookup(aliases, normalizeBoard(input.board), normalizeAlias(input.role));
  // Matéria de outro estado (texto curto ligado em editais de lá) não serve: vai para a rota ou para a IA.
  if (found && foreignStateMaterial(found.topicId, [input.competitionName, input.agency, input.subjectName, ...input.topicPath])) found = null;
  // Matéria (e material) a que o recorte automático da banca se refere; num tópico montado de
  // várias matérias, é a primeira.
  let primary = found ? { topicId: found.topicId, material: found.material } : null;
  if (!found) {
    const routed = await routedLibraryHit(job, input, aliases, dependencies, free).catch(() => null);
    if (routed) ({ hit: found, primary } = routed);
  }
  if (!found || !primary) {
    if (input.avoidStatements.length === 0) {
      await store.recordLibraryMiss?.({
        subject: input.subjectName,
        topic: input.topicPath.join(" › "),
        board: input.board,
        subjectNorm: normalizeAlias(input.subjectName),
        topicNorm: aliases[0] ?? "",
        boardNorm: normalizeBoard(input.board),
      }).catch(() => undefined);
    }
    return null;
  }
  // Matéria pronta sem recorte desta banca: faz o recorte uma vez (perfil da banca + tópico) e guarda.
  const boardNorm = normalizeBoard(input.board);
  if (!found.note && !free && input.board && boardNorm && Deno.env.get("BOARD_NOTES_ENABLED") !== "false") {
    found.note = await ensureBoardNote({
      provider: dependencies.provider,
      store,
      jobId: job.id,
      model: resolveOpenAiModel(dependencies.modelForJob?.(job) ?? dependencies.model),
      board: input.board,
      boardNorm,
      topicId: primary.topicId,
      material: primary.material,
      onFinished: (kind, response) => meterCost(dependencies, job, kind, response),
    });
  }
  // Sem recorte desta banca (ou concurso sem banca): o "Padrão das provas" da matéria, se houver.
  if (!found.note) {
    found.note = await store.libraryBoardNote?.(primary.topicId, GENERAL_BOARD_NORM, "").catch(() => null) ?? null;
  } else if (normalizeBoard(String(found.note.board ?? "")) !== GENERAL_BOARD_NORM) {
    // Recorte da banca com poucas questões (ou nenhuma, ou todas já vistas): as do "Padrão das
    // provas" entram depois das da banca, em vez de a IA paga escrever o que falta. O perfil e as
    // dicas da banca continuam.
    const general = await store.libraryBoardNote?.(primary.topicId, GENERAL_BOARD_NORM, "").catch(() => null) ?? null;
    const own = (found.note.questions as unknown[] | undefined) ?? [];
    const extra = (general?.questions as unknown[] | undefined) ?? [];
    if (extra.length > 0) found.note = { ...found.note, questions: [...own, ...extra], borrowedQuestions: own.length === 0, ownQuestions: own.length };
  }
  const fresh = input.avoidStatements.length === 0;
  const servedKey = `lib:${found.topicId}@${found.version}:${await contentCacheKey(input)}`;
  // Sem recorte cadastrado, o nome do tópico escolhe os capítulos (pedir "HTML5" traz só HTML5).
  const focus = found.focus ?? inferFocus(found.material, input.topicPath[input.topicPath.length - 1]);
  let content = assembleFromLibrary(found.material, found.note, input, `${job.id}:${job.userId}`, focus, true);
  // Grátis: nenhuma questão pronta no formato pedido (ex.: só Certo/Errado num tópico estreito): sai
  // com os formatos que houver, em vez de ir para a IA.
  if (!content && free && input.options.questionStyle !== "MIXED") {
    content = assembleFromLibrary(found.material, found.note, { ...input, options: { ...input.options, questionStyle: "MIXED" } }, `${job.id}:${job.userId}`, focus, true);
    if (content) content.warnings = [...(Array.isArray(content.warnings) ? content.warnings as unknown[] : []), { code: "INSUFFICIENT_EVIDENCE", message: "Este tópico não tinha questões prontas no formato escolhido: vieram em outro formato." }];
  }
  if (!content) return null;
  // A biblioteca não tinha todas (ex.: 13 de 30): a IA escreve só as que faltam, em cima da teoria
  // pronta, em vez de refazer o material inteiro. Em partes de até QUESTION_CHUNK, ao mesmo tempo
  // (acima disso o modelo erra mais), até completar o pedido.
  const have = Array.isArray(content.questions) ? (content.questions as unknown[]).length : 0;
  const missing = input.options.questionCount - have;
  if (missing > 0 && free) {
    // Grátis: completa com questões prontas de outro formato (ex.: pediu só Certo/Errado e o tópico
    // tem poucas), sem IA. Se nem assim der, entrega as que houver.
    const picked = ((content.questions as Record<string, unknown>[] | undefined) ?? []).map((q) => String(q.statement ?? ""));
    const fill = input.options.questionStyle === "MIXED" ? null : assembleFromLibrary(
      found.material, found.note,
      { ...input, options: { ...input.options, blocks: ["QUESTIONS"], questionStyle: "MIXED", questionCount: missing }, avoidStatements: [...input.avoidStatements, ...picked] },
      `${job.id}:${job.userId}:fill`, focus, true,
    );
    const extra = ((fill?.questions as unknown[] | undefined) ?? []).slice(0, missing);
    if (extra.length > 0) {
      content.questions = [...((content.questions as unknown[] | undefined) ?? []), ...extra];
      const warnings = Array.isArray(content.warnings) ? content.warnings as unknown[] : [];
      content.warnings = [...warnings, { code: "INSUFFICIENT_EVIDENCE", message: "Completamos com questões de outro formato: o banco deste tópico não tinha todas no formato escolhido." }];
    }
  } else if (missing > 0) {
    const sizes = Array.from({ length: Math.ceil(missing / QUESTION_CHUNK) }, (_, i) => Math.min(QUESTION_CHUNK, missing - i * QUESTION_CHUNK));
    const parts = await Promise.all(sizes.map((size) => libraryTopUp(content, size, input, job, dependencies).catch(() => [])));
    // Partes feitas em paralelo podem repetir enunciado: fica a primeira.
    const seen = new Set((content.questions as Record<string, unknown>[]).map((q) => normalizeStatement(q.statement)));
    const extra = parts.flat().filter((q) => {
      const key = normalizeStatement(q.statement);
      if (!key || seen.has(key)) return false;
      seen.add(key);
      return true;
    });
    content.questions = [...(content.questions as unknown[]), ...extra.slice(0, missing)];
  }
  if (fresh) await store.markServed?.(servedKey, job.userId).catch(() => undefined);
  await store.recordLibraryHit?.(found.topicId).catch(() => undefined);
  return content as unknown as WorkerProposal;
}

/**
 * Questões que faltam num material da biblioteca: uma chamada curta, sem pesquisa, a partir dos
 * capítulos prontos; passam pelo conserto e (3 ou mais) pelo revisor. Se der errado, nada é somado.
 */
function normalizeStatement(value: unknown): string {
  return String(value ?? "").toLowerCase().normalize("NFD").replace(/[\u0300-\u036f]/g, "").replace(/[^a-z0-9]+/g, " ").trim();
}

async function libraryTopUp(
  content: Record<string, unknown>,
  missing: number,
  input: ContentJobInput,
  job: SyllabusWorkerJob,
  dependencies: Parameters<NonNullable<AiJobSpec["cached"]>>[1],
): Promise<Record<string, unknown>[]> {
  if (missing <= 0 || !dependencies.provider) return [];
  const context = [input.competitionName, input.role, input.board ? `banca ${input.board}` : null, input.subjectName, input.topicPath.join(" › ")].filter(Boolean).join(" · ");
  const model = resolveOpenAiModel(dependencies.modelForJob?.(job) ?? dependencies.model);
  // Pede com reserva: se o revisor tirar alguma, a conta ainda fecha (pedia 20 e vinham 19).
  const raw = await runQuestionTopUp(
    { content, missing: missing + RESERVE_QUESTIONS, questionStyle: input.options.questionStyle, difficulty: input.options.difficulty, context },
    {
      provider: dependencies.provider,
      jobId: job.id,
      model,
      part: "biblioteca",
      onFinished: (response) => meterCost(dependencies, job, "TOPUP", response),
    },
  );
  if (raw.length === 0) return [];
  try {
    const repaired = JSON.parse(repairTopicContent(JSON.stringify({ ...content, questions: raw }), { ...input.options, questionCount: missing }, 0)) as Record<string, unknown>;
    let extra = (repaired.questions as Record<string, unknown>[]) ?? [];
    if (extra.length >= 3 && Deno.env.get("CONTENT_REVIEW_ENABLED") !== "false") {
      const review = await runContentReview({ ...repaired, questions: extra }, {
        provider: dependencies.provider,
        jobId: job.id,
        model,
        part: "biblioteca",
        context,
        onFinished: (response) => meterCost(dependencies, job, "REVIEW", response),
      }).catch(() => null);
      if (review) {
        const { content: applied } = applyReview({ ...repaired, questions: extra }, review);
        extra = (applied.questions as Record<string, unknown>[]).filter((q) => !hasDraftExplanation(q));
      }
    }
    return extra.slice(0, missing);
  } catch {
    return [];
  }
}

/**
 * Enciclopédia: o nome não bateu com nenhum apelido, então uma chamada curta escolhe as matérias
 * que cobrem o tópico (guardada para os próximos pedidos) e o material é montado com elas.
 */
async function routedLibraryHit(
  job: SyllabusWorkerJob,
  input: ContentJobInput,
  aliases: string[],
  dependencies: Parameters<NonNullable<AiJobSpec["cached"]>>[1],
  /** Grátis: só rota já guardada (escolher uma rota nova chama a IA). */
  savedOnly = false,
): Promise<{ hit: LibraryHit; primary: { topicId: string; material: Record<string, unknown> } } | null> {
  const store = dependencies.jobs;
  const topicNorm = aliases[0];
  if (!topicNorm || !store.libraryCatalog || !store.libraryMaterials || Deno.env.get("LIBRARY_ROUTER_ENABLED") === "false") return null;
  const catalog = await store.libraryCatalog();
  // Grátis: rota completa já guardada vale mesmo se a biblioteca cresceu depois (a matéria que ela
  // aponta continua lá); refazer a rota chamaria a IA.
  let route = await store.libraryRoute?.(topicNorm, savedOnly ? null : catalog.length) ?? null;
  if (!route && savedOnly) return null;
  if (!route) {
    route = await routeTopic({
      provider: dependencies.provider,
      jobId: job.id,
      model: resolveOpenAiModel(dependencies.modelForJob?.(job) ?? dependencies.model),
      subject: input.subjectName,
      topic: input.topicPath.join(" › "),
      catalog,
      onFinished: (response) => meterCost(dependencies, job, "TOPIC_ROUTE", response),
    });
    if (!route) return null;
    await store.saveLibraryRoute?.(topicNorm, normalizeAlias(input.subjectName), route, catalog.length).catch(() => undefined);
  }
  if (route.coverage !== "FULL" || route.topicIds.length === 0) return null;
  const materials = await store.libraryMaterials(route.topicIds);
  if (materials.length !== route.topicIds.length) return null;
  const material = composeMaterials(materials, input.topicPath.at(-1) ?? String(materials[0].title));
  const note = await store.libraryBoardNote?.(route.topicIds[0], normalizeBoard(input.board), normalizeAlias(input.role)).catch(() => null) ?? null;
  return {
    hit: { topicId: route.topicIds.join("+"), version: Number(material.version ?? 1), material, note },
    primary: { topicId: route.topicIds[0], material: materials[0] },
  };
}

async function validateContent(
  raw: string,
  expected: ExpectedVersions,
  job: SyllabusWorkerJob,
  dependencies: Parameters<AiJobSpec["validate"]>[3],
): Promise<WorkerProposal> {
  {
    const input = inputOf<ContentJobInput>(job, parseContentJobInput);
    // Toda questão descartada fica registrada com o motivo, para saber o que o modelo mais erra.
    const drops: QuestionDrop[] = [];
    const dropped = (stage: QuestionDrop["stage"]) => (reason: DropReason, question: Record<string, unknown>) =>
      drops.push({ stage, reason, statement: String(question.statement ?? "").slice(0, 300) });
    const saveDrops = () => drops.length === 0 ? Promise.resolve() : dependencies.jobs.recordQuestionDrops?.(job.id, drops).catch(() => undefined);
    const repaired = repairTopicContent(raw, input.options, RESERVE_QUESTIONS, dropped("GENERATION"));
    const content = validateTopicContent(repaired, expected, { ...input.options, questionCount: questionsDelivered(repaired, input.options.questionCount) });
    if (Deno.env.get("CONTENT_REVIEW_ENABLED") === "false") {
      await saveDrops();
      return keepRequestedQuestions(content, input.options.questionCount) as unknown as WorkerProposal;
    }
    const review = await runContentReview(content, {
      provider: dependencies.provider,
      jobId: job.id,
      model: resolveOpenAiModel(dependencies.modelForJob?.(job) ?? dependencies.model),
      onFinished: (response) => meterCost(dependencies, job, "REVIEW", response),
      previousReviewId: () => dependencies.jobs.completedReviewId?.(job.id) ?? Promise.resolve(null),
      context: [input.competitionName, input.role, input.board ? `banca ${input.board}` : null, input.subjectName, input.topicPath.join(" › ")]
        .filter(Boolean).join(" · "),
    });
    const reviewed = withReview(content, review, expected, input, dropped("REVIEW"));
    const complete = await completeQuestions(reviewed, expected, input, job, dependencies, dropped("TOPUP"));
    await saveDrops();
    return complete as unknown as WorkerProposal;
  }
}

/**
 * Questões com defeito saem do material; se a reserva não cobriu, as que faltam são escritas numa
 * chamada curta e passam pelo mesmo conserto e validação. Se der errado, fica o que já havia.
 */
async function completeQuestions(
  content: Record<string, unknown>,
  expected: ExpectedVersions,
  input: ContentJobInput,
  job: SyllabusWorkerJob,
  dependencies: Parameters<AiJobSpec["validate"]>[3],
  onDrop?: (reason: DropReason, question: Record<string, unknown>) => void,
): Promise<Record<string, unknown>> {
  const current = Array.isArray(content.questions) ? content.questions as Record<string, unknown>[] : [];
  // Pela IA, no máximo QUESTION_CHUNK questões por pedido (acima disso o modelo erra mais); a
  // reposição só cobre as que o revisor tirou. O resto a pessoa pede em lotes de 10.
  const missing = Math.min(input.options.questionCount, QUESTION_CHUNK) - current.length;
  if (missing <= 0) return content;
  const context = [input.competitionName, input.role, input.board ? `banca ${input.board}` : null, input.subjectName, input.topicPath.join(" › ")].filter(Boolean).join(" · ");
  const model = resolveOpenAiModel(dependencies.modelForJob?.(job) ?? dependencies.model);
  // Pedido grande (20 ou 30): o que falta sai em partes de até QUESTION_CHUNK, todas ao mesmo tempo.
  const parts = Math.ceil(missing / QUESTION_CHUNK);
  const batches = await Promise.all(Array.from({ length: parts }, (_, i) =>
    runQuestionTopUp(
      {
        content,
        missing: Math.min(QUESTION_CHUNK, missing - i * QUESTION_CHUNK) + RESERVE_QUESTIONS,
        questionStyle: input.options.questionStyle,
        difficulty: input.options.difficulty,
        context: parts > 1 ? `${context} · parte ${i + 1} de ${parts}: cubra pontos e casos diferentes das outras partes` : context,
      },
      {
        provider: dependencies.provider,
        jobId: job.id,
        model,
        // A primeira parte mantém a chave antiga: nova tentativa do job não paga de novo.
        part: i === 0 ? undefined : String(i + 1),
        // Falha da parte fica registrada junto das questões descartadas, para saber o porquê.
        onError: (message) => onDrop?.("TOPUP_FAILED", { statement: `parte ${i + 1}: ${message}` }),
        onFinished: (response) => meterCost(dependencies, job, "TOPUP", response),
      },
    )
  ));
  let extra = batches.flat();
  if (extra.length === 0) return content;
  // As repostas também passam pelo revisor (só elas), como as da primeira chamada.
  if (Deno.env.get("CONTENT_REVIEW_ENABLED") !== "false" && extra.length >= 3) {
    try {
      const onlyExtra = JSON.parse(repairTopicContent(JSON.stringify({ ...content, questions: extra }), input.options, 0, onDrop)) as Record<string, unknown>;
      const review = await runContentReview(onlyExtra, {
        provider: dependencies.provider,
        jobId: job.id,
        model,
        part: "extra",
        context,
        onFinished: (response) => meterCost(dependencies, job, "REVIEW", response),
      });
      if (review) {
        const { content: applied } = applyReview(onlyExtra, review);
        extra = (applied.questions as Record<string, unknown>[]).filter((q) => !hasDraftExplanation(q));
      }
    } catch {
      // Sem a segunda revisão, seguem as repostas já consertadas pela validação abaixo.
    }
  }
  if (extra.length === 0) return content;
  try {
    const merged = repairTopicContent(JSON.stringify({ ...content, questions: [...current, ...extra] }), input.options, 0, onDrop);
    // A reserva que sobrou não vai: fica o número pedido.
    const kept = keepRequestedQuestions(JSON.parse(merged), input.options.questionCount);
    const count = (kept.questions as unknown[]).length;
    return validateTopicContent(JSON.stringify(kept), expected, { ...input.options, questionCount: count }) as Record<string, unknown>;
  } catch {
    return content;
  }
}

/** Questões pedidas a mais, de reserva: as que o revisor remover são repostas por elas. */
export const RESERVE_QUESTIONS = 2;

/** No Grátis, matérias por mês que a IA pode escrever (as da biblioteca não contam). */
export const FREE_AI_MATERIALS = 2;

/** Aceita de 1 a N + reserva questões; fora disso vale o número pedido (e a validação recusa). */
function questionsDelivered(raw: string, requested: number): number {
  if (requested === 0) return 0;
  try {
    const count = (JSON.parse(raw) as { questions?: unknown[] }).questions?.length ?? requested;
    // Depois do conserto, questões defeituosas já saíram: vale o que sobrou, desde que sobre alguma.
    return count >= 1 && count <= requested + RESERVE_QUESTIONS ? count : requested;
  } catch {
    return requested;
  }
}

/** Fica com as N primeiras; a reserva só aparece no lugar das removidas. */
export function keepRequestedQuestions(content: Record<string, unknown>, requested: number): Record<string, unknown> {
  const questions = content.questions as unknown[] | undefined;
  if (!questions || requested === 0 || questions.length <= requested) return content;
  return { ...content, questions: questions.slice(0, requested) };
}

/**
 * Aplica a revisão e confere o formato de novo. Se a versão corrigida não passar (ex.: sobraram
 * poucos flashcards), tenta só as correções de texto e gabarito; em último caso, fica o original.
 */
export function withReview(
  content: Record<string, unknown>,
  review: ContentReview | null,
  expected: ExpectedVersions,
  input: ContentJobInput,
  onDrop?: (reason: DropReason, question: Record<string, unknown>) => void,
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
      // A correção do revisor também pode deixar texto de rascunho na explicação: essa questão sai.
      const drafts = (reviewed.questions as Record<string, unknown>[]).filter(hasDraftExplanation);
      const clean = { ...reviewed, questions: (reviewed.questions as Record<string, unknown>[]).filter((q) => !hasDraftExplanation(q)) };
      const original = Array.isArray(content.questions) ? content.questions as Record<string, unknown>[] : [];

      const kept = keepRequestedQuestions(clean, input.options.questionCount);
      const valid = validateTopicContent(JSON.stringify(kept), expected, { ...input.options, questionCount: (kept.questions as unknown[]).length });
      // Só registra o que saiu na forma da revisão que foi de fato aplicada.
      attempt.removeQuestions.forEach((index) => original[index] && onDrop?.("REVIEW_REMOVED", original[index]));
      drafts.forEach((q) => onDrop?.("REVIEW_DRAFT", q));
      return valid;
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
  // Pesquisa para confirmar lei, número e a própria banca do concurso. Quando tudo o que sobrou
  // para a IA tem modelo da biblioteca (questões irmãs) ou é revanche, não pesquisa: a fonte é a
  // própria questão conferida.
  tools: (job) => {
    const input = SIMULATION_LEFTOVER.get(job.id) ?? inputOf<SimulationJobInput>(job, parseSimulationJobInput);
    const needsWeb = input.detectBoard || input.items.some((item) => !item.bases || item.bases.length === 0);
    return needsWeb ? [{ type: "web_search", search_context_size: "medium" }] : undefined;
  },
  // A IA só recebe o que a biblioteca não cobriu (o worker refaz o pedido depois de `cached`).
  prompts: (job) => ({
    systemPrompt: SIMULATION_SYSTEM_PROMPT,
    userPrompt: simulationUserPrompt(SIMULATION_LEFTOVER.get(job.id) ?? inputOf<SimulationJobInput>(job, parseSimulationJobInput)),
  }),
  // Primeiro a biblioteca: questões prontas e conferidas, sem custo. Se cobriu tudo, a parte sai
  // daqui; senão a IA escreve só as que faltaram.
  cached: async (job, dependencies) => {
    const input = inputOf<SimulationJobInput>(job, parseSimulationJobInput);
    if (Deno.env.get("SIMULATION_LIBRARY_ENABLED") === "false") return null;
    const plan = await planSimulationFromLibrary(input, dependencies.jobs, `${job.id}:${job.userId}`);
    // Grátis (o diagnóstico): sai só com as questões prontas, mesmo que menor, sem IA para o resto.
    const free = dependencies.jobs.planTierOf ? (await dependencies.jobs.planTierOf(job.userId).catch(() => "FREE")) === "FREE" : false;
    if (plan.remaining && !(free && plan.questions.length > 0)) {
      SIMULATION_LEFTOVER.set(job.id, plan.remaining);
      return null;
    }
    const expected = { schemaVersion: SIMULATION_SCHEMA_VERSION, promptVersion: SIMULATION_PROMPT_VERSION, modelVersion: LIBRARY_MODEL_VERSION };
    const part = { ...expected, detectedBoard: { name: null, sourceUrl: null }, questions: plan.questions, sources: [], warnings: [] };
    return validateSimulation(part, expected, plan.delivered) as unknown as WorkerProposal;
  },
  validate: async (raw, expected, job, dependencies) => {
    const full = inputOf<SimulationJobInput>(job, parseSimulationJobInput);
    // A resposta da IA cobre só o que faltou: refaz a escolha da biblioteca (a mesma, pela semente)
    // para saber o que foi pedido e juntar as duas no fim.
    const plan = Deno.env.get("SIMULATION_LIBRARY_ENABLED") === "false"
      ? null
      : await planSimulationFromLibrary(full, dependencies.jobs, `${job.id}:${job.userId}`).catch(() => null);
    SIMULATION_LEFTOVER.delete(job.id);
    const input = plan?.remaining ?? full;
    const merge = (part: Record<string, unknown>) => plan?.remaining ? mergeWithLibrary(part, plan.questions, full) : part;
    // Com a biblioteca entregando o resto, uma questão da IA com defeito sai sozinha (refazer a parte pagaria a IA de novo).
    const lenient = plan?.remaining ? { dropInvalid: true } : {};
    const part = validateSimulation(raw, expected, input, lenient);
    if (Deno.env.get("CONTENT_REVIEW_ENABLED") === "false") return merge(part) as unknown as WorkerProposal;
    const review = await runContentReview({ questions: part.questions }, {
      provider: dependencies.provider,
      jobId: job.id,
      model: resolveOpenAiModel(dependencies.modelForJob?.(job) ?? dependencies.model),
      onFinished: (response) => meterCost(dependencies, job, "REVIEW", response),
      previousReviewId: () => dependencies.jobs.completedReviewId?.(job.id) ?? Promise.resolve(null),
      context: [input.competitionName, input.role, input.board ? `banca ${input.board}` : null, "simulado"].filter(Boolean).join(" · "),
    });
    return merge(withSimulationReview(part, review, expected, input, lenient)) as unknown as WorkerProposal;
  },
};

/** Parte ainda por pedir à IA, por job (vale só dentro da mesma execução do worker). */
const SIMULATION_LEFTOVER = new Map<string, SimulationJobInput>();

/** Questões da biblioteca + as da IA, na ordem dos itens da planta (que é a ordem da prova). */
function mergeWithLibrary(part: Record<string, unknown>, library: Record<string, unknown>[], input: SimulationJobInput): Record<string, unknown> {
  const order = new Map([...input.items.map((item) => item.ref), ...input.rematch.map((source) => source.ref)].map((ref, index) => [ref, index]));
  const all = [...library, ...(part.questions as Record<string, unknown>[])];
  const rank = (q: Record<string, unknown>) => order.get(String(q.itemRef)) ?? order.size;
  return { ...part, questions: all.map((q, index) => ({ q, index })).sort((a, b) => rank(a.q) - rank(b.q) || a.index - b.index).map(({ q }) => q) };
}

/** Mesma lógica da revisão do conteúdo: corrigido e revalidado, ou o original com aviso. */
export function withSimulationReview(
  part: Record<string, unknown>,
  review: ContentReview | null,
  expected: ExpectedVersions,
  input: SimulationJobInput,
  options: SimulationValidationOptions = {},
): Record<string, unknown> {
  const warn = (message: string) => ({
    ...part,
    warnings: [...(part.warnings as unknown[]).slice(0, 9), { code: "SOURCE_NOT_VERIFIED", message }],
  });
  if (review === null) return warn("A revisão automática de fatos não foi concluída para esta parte do simulado.");
  for (const attempt of [review, { ...review, removeQuestions: [], removeFlashcards: [] }]) {
    const { content: reviewed } = applyReview(part, attempt);
    try {
      return validateSimulation(reviewed, expected, input, { allowShortfall: true, ...options });
    } catch {
      // Tenta a forma mais conservadora.
    }
  }
  return warn("A revisão automática encontrou pontos a conferir, mas não pôde aplicar as correções.");
}

export function textSpecFor(job: SyllabusWorkerJob): AiJobSpec {
  // Lote extra de questões é um pedido de conteúdo só com o bloco QUESTIONS, com cota própria.
  if (job.feature === "CONTENT_GENERATION" || job.feature === "QUESTION_BATCH") return CONTENT_JOB_SPEC;
  if (job.feature === "PLAN_GENERATION") return PLAN_JOB_SPEC;
  if (job.feature === "SIMULATION_GENERATION") return SIMULATION_JOB_SPEC;
  // A fila de texto só entrega esses três; qualquer outro é dado corrompido.
  throw new Error("TEXT_JOB_INPUT_INVALID");
}
