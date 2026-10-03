import type { ContentJobInput } from "./text-job-input.ts";
import { AI_TOPIC_CONTENT_SCHEMA_VERSION } from "./schemas/ai-text-jobs-v1.ts";
import { CONTENT_PROMPT_VERSION } from "./prompts/text-jobs-v1.ts";
import { validateTopicContent } from "./text-job-validators.ts";

// Biblioteca de matérias prontas (conteudo/ no repositório, tabelas library_* no banco).
//
// A matéria canônica é escrita e conferida fora do app, com um banco de questões grande. Quando um
// pedido bate num tópico da biblioteca, o material sai montado daqui, no mesmo formato da IA, sem
// custo: só as partes pedidas, as questões no estilo e dificuldade pedidos e o recorte da banca.

type Json = Record<string, unknown>;

export const LIBRARY_MODEL_VERSION = "biblioteca";

/** Minúsculas, sem acento, sem numeração de edital ("1.2 Função afim." → "funcao afim"). */
export function normalizeAlias(value: string | null | undefined): string {
  return (value ?? "")
    .normalize("NFD").replace(/[̀-ͯ]/g, "")
    .replace(/[º°ª]/g, "")
    .toLowerCase()
    .replace(/^[\s\d.\-–)]+(?=[a-z])/, "")
    .replace(/[^a-z0-9]+/g, " ")
    .trim();
}

const BOARD_NAMES: Record<string, string> = {
  "cespe": "cebraspe",
  "cespe unb": "cebraspe",
  "cebraspe cespe": "cebraspe",
  "fundacao carlos chagas": "fcc",
  "fundacao getulio vargas": "fgv",
  "instituto aocp": "aocp",
  "instituto ibfc": "ibfc",
  "instituto consulplan": "consulplan",
};

/** Nome da banca no formato da chave: "CESPE/UnB" e "Cebraspe" viram a mesma banca. */
export function normalizeBoard(value: string | null | undefined): string {
  const name = normalizeAlias(value);
  return BOARD_NAMES[name] ?? name;
}

/** Textos do pedido que podem bater num apelido, do mais específico ao mais geral. */
export function lookupAliases(input: Pick<ContentJobInput, "topicPath">): string[] {
  const path = input.topicPath.map(normalizeAlias).filter((part) => part.length > 0);
  const candidates = [path.join(" "), path.at(-1) ?? ""].filter((alias) => alias.length >= 2);
  return [...new Set(candidates)];
}

// ---------------------------------------------------------------------------------------------
// Conferência na importação: o que entra na biblioteca precisa servir qualquer pedido.

export class LibraryValidationError extends Error {
  constructor(public readonly problems: string[]) {
    super(problems.join("\n"));
    this.name = "LibraryValidationError";
  }
}

/** Banco mínimo de questões para servir qualquer combinação de estilo e dificuldade. */
export const LIBRARY_MIN = {
  chapters: 2,
  chapterChars: 800,
  theoryChars: 6_000,
  summaryChars: 400,
  flashcards: 12,
  tips: 3,
  traps: 3,
  activeRecall: 5,
  errorConcepts: 2,
  questions: 40,
  trueFalse: 15,
  fiveOptions: 15,
  perDifficulty: 8,
  explanationChars: 120,
  howItFallsChars: 300,
} as const;

const PLACEHOLDERS = /lorem ipsum|\btodo\b|a ser preenchid|\[inserir|\[completar|conte[uú]do gen[eé]rico|xxx/i;
const KEYS = ["e1", "e2", "e3", "e4", "e5", "e6"];

class Checker {
  problems: string[] = [];
  fail(message: string) {
    this.problems.push(message);
  }
  text(value: unknown, field: string, min = 1, max = 60_000): string {
    if (typeof value === "string" && PLACEHOLDERS.test(value)) this.fail(`${field}: tem texto de rascunho/genérico`);
    if (typeof value !== "string" || value.trim().length < min || value.length > max) {
      this.fail(`${field}: texto ausente, curto demais (mínimo ${min}) ou longo demais`);
      return "";
    }
    return value;
  }
  list(value: unknown, field: string, min: number, max: number): Json[] {
    if (!Array.isArray(value)) {
      this.fail(`${field}: não é uma lista`);
      return [];
    }
    if (value.length < min || value.length > max) this.fail(`${field}: precisa ter de ${min} a ${max} itens (tem ${value.length})`);
    return value.filter((item) => item !== null && typeof item === "object" && !Array.isArray(item)) as Json[];
  }
  strings(value: unknown, field: string, min: number, max: number): string[] {
    if (!Array.isArray(value)) {
      this.fail(`${field}: não é uma lista`);
      return [];
    }
    if (value.length < min || value.length > max) this.fail(`${field}: precisa ter de ${min} a ${max} itens (tem ${value.length})`);
    return value.map((item, index) => this.text(item, `${field}[${index}]`, 10, 2_000));
  }
}

function checkQuestion(c: Checker, question: Json, field: string, chapterTitles: Set<string>, conceptKeys: Set<string>) {
  c.text(question.statement, `${field}.statement`, 20, 4_000);
  const options = c.list(question.options, `${field}.options`, 2, 5);
  const keys = options.map((option) => String(option.key)).join();
  if (question.format === "TRUE_FALSE") {
    if (keys !== "C,E") c.fail(`${field}: Certo/Errado precisa das opções C e E`);
  } else if (question.format === "MULTIPLE_CHOICE") {
    if (keys !== "A,B,C,D,E" && keys !== "A,B,C,D") c.fail(`${field}: múltipla escolha precisa de A–D ou A–E`);
  } else {
    c.fail(`${field}.format: use MULTIPLE_CHOICE ou TRUE_FALSE`);
  }
  if (options.filter((option) => option.correct === true).length !== 1) c.fail(`${field}: precisa de exatamente uma opção correta`);
  options.forEach((option, index) => c.text(option.text, `${field}.options[${index}].text`, 1, 2_000));
  const texts = options.map((option) => String(option.text ?? "").trim().toLowerCase());
  if (new Set(texts).size !== texts.length) c.fail(`${field}: alternativas repetidas`);
  c.text(question.explanation, `${field}.explanation`, LIBRARY_MIN.explanationChars, 8_000);
  if (!["FACIL", "MEDIA", "DIFICIL"].includes(String(question.difficulty))) c.fail(`${field}.difficulty: use FACIL, MEDIA ou DIFICIL`);
  if (!chapterTitles.has(String(question.section ?? "").trim())) c.fail(`${field}.section: precisa ser o título exato de um capítulo`);
  if (question.errorConceptKey !== null && !conceptKeys.has(String(question.errorConceptKey))) {
    c.fail(`${field}.errorConceptKey: use null ou uma chave de errorConcepts`);
  }
  if (question.sourceType === "REAL") {
    if (typeof question.sourceUrl !== "string" || !/^https?:\/\//.test(question.sourceUrl)) c.fail(`${field}.sourceUrl: questão REAL precisa do link`);
    if (typeof question.board !== "string" || question.board.trim() === "") c.fail(`${field}.board: questão REAL precisa da banca`);
    if (typeof question.year !== "number" || question.year < 1980 || question.year > 2100) c.fail(`${field}.year: questão REAL precisa do ano`);
  } else if (question.sourceType === "AUTHORIAL") {
    if (question.sourceUrl !== null || question.board !== null || question.agency !== null || question.year !== null) {
      c.fail(`${field}: questão AUTHORIAL deixa sourceUrl, board, agency e year como null`);
    }
  } else {
    c.fail(`${field}.sourceType: use AUTHORIAL ou REAL`);
  }
}

function checkStatementsUnique(c: Checker, questions: Json[], field: string, seen = new Set<string>()) {
  questions.forEach((question, index) => {
    const statement = String(question.statement ?? "").trim().toLowerCase();
    if (seen.has(statement)) c.fail(`${field}[${index}]: enunciado repetido`);
    seen.add(statement);
  });
  return seen;
}

/** Confere uma matéria da biblioteca. Lança [LibraryValidationError] com todos os problemas. */
export function validateLibraryMaterial(material: Json): Json {
  const c = new Checker();
  if (typeof material.id !== "string" || !/^[a-z0-9]+(-[a-z0-9]+)*\.[a-z0-9]+(-[a-z0-9]+)*$/.test(material.id)) {
    c.fail("id: use disciplina.topico em minúsculas com hífens (ex.: matematica.funcao-1-grau)");
  }
  c.text(material.subject, "subject", 2, 120);
  c.text(material.title, "title", 2, 200);
  if (!Number.isInteger(material.version) || (material.version as number) < 1) c.fail("version: inteiro a partir de 1");
  if (!["DRAFT", "REVIEWED", "PUBLISHED"].includes(String(material.status))) c.fail("status: DRAFT, REVIEWED ou PUBLISHED");
  const aliases = c.strings(material.aliases, "aliases", 1, 60).map(normalizeAlias);
  if (aliases.some((alias) => alias.length < 2)) c.fail("aliases: apelido vazio depois de normalizar");
  const scope = (material.scope ?? {}) as Json;
  c.text(scope.covers, "scope.covers", 20, 2_000);
  if (typeof scope.excludes !== "string") c.fail("scope.excludes: texto (pode ser vazio)");
  c.text(material.theoryTitle, "theoryTitle", 2, 200);

  const chapters = c.list(material.chapters, "chapters", LIBRARY_MIN.chapters, 6);
  const titles = chapters.map((chapter, index) => c.text(chapter.title, `chapters[${index}].title`, 2, 200).trim());
  const chapterTitles = new Set(titles);
  if (chapterTitles.size !== titles.length) c.fail("chapters: títulos repetidos");
  const theory = chapters.reduce((total, chapter, index) =>
    total + c.text(chapter.markdown, `chapters[${index}].markdown`, LIBRARY_MIN.chapterChars).length, 0);
  if (theory < LIBRARY_MIN.theoryChars) c.fail(`chapters: teoria curta demais (${theory} de ${LIBRARY_MIN.theoryChars} caracteres)`);

  c.text(material.summary, "summary", LIBRARY_MIN.summaryChars);
  const cards = c.list(material.flashcards, "flashcards", LIBRARY_MIN.flashcards, 30);
  const fronts = new Set<string>();
  cards.forEach((card, index) => {
    const front = c.text(card.front, `flashcards[${index}].front`, 3, 240).trim().toLowerCase();
    c.text(card.back, `flashcards[${index}].back`, 3, 3_000);
    if (fronts.has(front)) c.fail(`flashcards[${index}]: frente repetida`);
    fronts.add(front);
  });
  c.strings(material.tips, "tips", LIBRARY_MIN.tips, 8);
  c.strings(material.traps, "traps", LIBRARY_MIN.traps, 8);
  c.list(material.activeRecall, "activeRecall", LIBRARY_MIN.activeRecall, 10).forEach((item, index) => {
    c.text(item.question, `activeRecall[${index}].question`, 5, 1_000);
    c.text(item.answer, `activeRecall[${index}].answer`, 5, 3_000);
  });
  const concepts = c.list(material.errorConcepts, "errorConcepts", LIBRARY_MIN.errorConcepts, 6);
  const conceptKeys = new Set(concepts.map((concept) => String(concept.key)));
  if (conceptKeys.size !== concepts.length || [...conceptKeys].some((key) => !KEYS.includes(key))) {
    c.fail("errorConcepts: chaves e1…e6 sem repetir");
  }
  concepts.forEach((concept, index) => {
    c.text(concept.title, `errorConcepts[${index}].title`, 3, 200);
    c.text(concept.summary, `errorConcepts[${index}].summary`, 20, 2_000);
  });

  const questions = c.list(material.questions, "questions", LIBRARY_MIN.questions, 300);
  questions.forEach((question, index) => checkQuestion(c, question, `questions[${index}]`, chapterTitles, conceptKeys));
  checkStatementsUnique(c, questions, "questions");
  const count = (test: (q: Json) => boolean) => questions.filter(test).length;
  const trueFalse = count((q) => q.format === "TRUE_FALSE");
  const fiveOptions = count((q) => q.format === "MULTIPLE_CHOICE" && (q.options as unknown[])?.length === 5);
  if (trueFalse < LIBRARY_MIN.trueFalse) c.fail(`questions: só ${trueFalse} Certo/Errado (mínimo ${LIBRARY_MIN.trueFalse})`);
  if (fiveOptions < LIBRARY_MIN.fiveOptions) c.fail(`questions: só ${fiveOptions} de 5 alternativas (mínimo ${LIBRARY_MIN.fiveOptions})`);
  for (const level of ["FACIL", "MEDIA", "DIFICIL"]) {
    const total = count((q) => q.difficulty === level);
    if (total < LIBRARY_MIN.perDifficulty) c.fail(`questions: só ${total} ${level} (mínimo ${LIBRARY_MIN.perDifficulty})`);
  }
  // A alternativa certa não pode cair quase sempre na mesma letra.
  const letters = new Map<string, number>();
  questions.filter((q) => q.format === "MULTIPLE_CHOICE").forEach((q) => {
    const key = String((q.options as Json[] | undefined)?.find((o) => o.correct === true)?.key ?? "");
    letters.set(key, (letters.get(key) ?? 0) + 1);
  });
  const multiple = questions.length - trueFalse;
  if (multiple >= 10 && Math.max(0, ...letters.values()) > multiple * 0.4) c.fail("questions: gabarito concentrado demais numa letra");
  // Quem chuta "a mais longa" (ou "a mais curta") não pode acertar quase sempre.
  const extremes = { longest: 0, shortest: 0 };
  questions.filter((q) => q.format === "MULTIPLE_CHOICE").forEach((q) => {
    const sizes = ((q.options as Json[] | undefined) ?? []).map((o) => String(o.text ?? "").length);
    const right = ((q.options as Json[] | undefined) ?? []).findIndex((o) => o.correct === true);
    if (right < 0) return;
    const unique = (value: number) => sizes.filter((size) => size === value).length === 1;
    if (sizes[right] === Math.max(...sizes) && unique(sizes[right])) extremes.longest++;
    if (sizes[right] === Math.min(...sizes) && unique(sizes[right])) extremes.shortest++;
  });
  if (multiple >= 10 && extremes.longest > multiple * 0.4) {
    c.fail(`questions: a alternativa certa é a mais longa em ${extremes.longest} de ${multiple} (máximo 40%); equilibre o tamanho das alternativas`);
  }
  if (multiple >= 10 && extremes.shortest > multiple * 0.4) {
    c.fail(`questions: a alternativa certa é a mais curta em ${extremes.shortest} de ${multiple} (máximo 40%); equilibre o tamanho das alternativas`);
  }
  // Rabicho de molde: a mesma expressão colada no fim de muitas alternativas ("…, no contexto do
  // relato") só serve para igualar tamanho e vira pista (a certa costuma ser a que não tem).
  const tails = new Map<string, number>();
  let optionCount = 0;
  questions.forEach((q) => {
    ((q.options as Json[] | undefined) ?? []).forEach((o) => {
      optionCount++;
      const text = String(o.text ?? "").trim().replace(/[.;!?]+$/, "");
      const comma = text.lastIndexOf(",");
      if (comma < 0) return;
      const tail = text.slice(comma + 1).trim().toLowerCase();
      if (tail.length >= 12 && tail.split(/\s+/).length >= 3) tails.set(tail, (tails.get(tail) ?? 0) + 1);
    });
  });
  for (const [tail, total] of tails) {
    if (total > Math.max(4, optionCount * 0.03)) {
      c.fail(`questions: ${total} alternativas terminam em ", ${tail}"; tire o rabicho de molde e iguale o tamanho com conteúdo`);
    }
  }
  // Pergunta de verdade varia: a mesma última linha de enunciado em quase todas é molde.
  const asks = new Map<string, number>();
  questions.forEach((q) => {
    const ask = String(q.statement ?? "").trim().split("\n").pop()!.replace(/^\d+[.)]\s*/, "").trim().toLowerCase();
    if (ask.length >= 20) asks.set(ask, (asks.get(ask) ?? 0) + 1);
  });
  for (const [ask, total] of asks) {
    if (questions.length >= 20 && total > questions.length * 0.2) {
      c.fail(`questions: a pergunta "${ask.slice(0, 80)}" aparece em ${total} questões; varie o que se pergunta (inferência, sentido no contexto, referência, reescrita…)`);
    }
  }
  // Troca automática de palavra deixa frase quebrada ("qualquer o desmatamento").
  const garbled = /\bqualquer\s+(o|a|os|as)\b/i;
  questions.forEach((q, index) => {
    const texts = [q.statement, q.explanation, ...((q.options as Json[] | undefined) ?? []).map((o) => o.text)];
    if (texts.some((t) => garbled.test(String(t ?? "")))) c.fail(`questions[${index}]: frase quebrada com "qualquer o/a"; revise o texto`);
  });
  // Explicação de verdade fala da questão: a mesma frase colada em muitas é texto de molde.
  const sentences = new Map<string, number>();
  questions.forEach((q) => {
    for (const sentence of new Set(String(q.explanation ?? "").split(/(?<=[.!?])\s+/).map((s) => s.trim()))) {
      if (sentence.length >= 40) sentences.set(sentence, (sentences.get(sentence) ?? 0) + 1);
    }
  });
  for (const [sentence, total] of sentences) {
    if (questions.length >= 20 && total > questions.length * 0.15) {
      c.fail(`questions: a frase "${sentence.slice(0, 80)}…" se repete em ${total} explicações; explique cada questão de forma própria`);
    }
  }

  c.list(material.sources, "sources", 1, 12).forEach((source, index) => {
    if (!["OFICIAL", "COMPLEMENTAR"].includes(String(source.kind))) c.fail(`sources[${index}].kind: OFICIAL ou COMPLEMENTAR`);
    c.text(source.title, `sources[${index}].title`, 3, 400);
    c.text(source.publisher, `sources[${index}].publisher`, 2, 200);
    if (typeof source.reference !== "string") c.fail(`sources[${index}].reference: texto (pode ser vazio)`);
    if (typeof source.url !== "string" || !/^https?:\/\/\S+$/.test(source.url)) c.fail(`sources[${index}].url: link http(s)`);
    if (typeof source.accessedAt !== "string" || !/^\d{4}-\d{2}-\d{2}$/.test(source.accessedAt)) c.fail(`sources[${index}].accessedAt: AAAA-MM-DD`);
  });

  if (c.problems.length === 0) {
    // Prova real: a matéria precisa montar um material aceito pelo app em todos os estilos.
    for (const questionStyle of ["MIXED", "FIVE_OPTIONS", "TRUE_FALSE"] as const) {
      const options = { blocks: ALL_BLOCKS, depth: "BOOK" as const, questionCount: 10, questionStyle, difficulty: "MIXED" as const };
      if (!assembleFromLibrary(material, null, { board: null, options, avoidStatements: [] }, "importacao")) {
        c.fail(`não foi possível montar um material completo no estilo ${questionStyle}`);
      }
    }
  }
  if (c.problems.length > 0) throw new LibraryValidationError(c.problems);
  return material;
}

/** Confere um recorte "como cai" contra a matéria a que ele pertence. */
export function validateBoardNote(note: Json, material: Json): Json {
  const c = new Checker();
  if (note.topic !== material.id) c.fail(`topic: precisa ser o id da matéria (${material.id})`);
  c.text(note.board, "board", 2, 120);
  if (note.role !== null && note.role !== undefined) c.text(note.role, "role", 2, 160);
  if (!Number.isInteger(note.version) || (note.version as number) < 1) c.fail("version: inteiro a partir de 1");
  c.text(note.howItFalls, "howItFalls", LIBRARY_MIN.howItFallsChars, 12_000);
  if (!["ALTA", "MEDIA", "BAIXA"].includes(String(note.incidence))) c.fail("incidence: ALTA, MEDIA ou BAIXA");
  c.strings(note.tips, "tips", 0, 8);
  c.strings(note.traps, "traps", 0, 8);
  const chapterTitles = new Set((material.chapters as Json[]).map((chapter) => String(chapter.title).trim()));
  const conceptKeys = new Set((material.errorConcepts as Json[]).map((concept) => String(concept.key)));
  const questions = c.list(note.questions ?? [], "questions", 0, 100);
  questions.forEach((question, index) => checkQuestion(c, question, `questions[${index}]`, chapterTitles, conceptKeys));
  checkStatementsUnique(c, questions, "questions", checkStatementsUnique(new Checker(), material.questions as Json[], "material"));
  if (c.problems.length > 0) throw new LibraryValidationError(c.problems);
  return note;
}

// ---------------------------------------------------------------------------------------------
// Montagem do material para um pedido.

const ALL_BLOCKS: ContentJobInput["options"]["blocks"] = [
  "THEORY", "SUMMARY", "QUICK_REVIEW", "TIPS_TRAPS", "ACTIVE_RECALL", "QUESTIONS", "ERROR_CONCEPTS",
];

/** Embaralha igual para a mesma semente: pessoas diferentes recebem questões em ordens diferentes. */
function shuffled<T>(items: T[], seed: string): T[] {
  let state = 2166136261;
  for (const char of seed) state = Math.imul(state ^ char.charCodeAt(0), 16777619);
  const random = () => {
    state = Math.imul(state ^ (state >>> 15), 2246822507);
    state = Math.imul(state ^ (state >>> 13), 3266489909);
    return ((state ^= state >>> 16) >>> 0) / 4294967296;
  };
  const copy = [...items];
  for (let i = copy.length - 1; i > 0; i--) {
    const j = Math.floor(random() * (i + 1));
    [copy[i], copy[j]] = [copy[j], copy[i]];
  }
  return copy;
}

function fitsStyle(question: Json, style: ContentJobInput["options"]["questionStyle"]): boolean {
  const optionCount = (question.options as unknown[]).length;
  if (question.format === "TRUE_FALSE") return style === "TRUE_FALSE" || style === "MIXED";
  if (optionCount === 4) return style === "FOUR_OPTIONS";
  return style === "FIVE_OPTIONS" || style === "MIXED";
}

const DIFFICULTY = { EASY: "FACIL", MEDIUM: "MEDIA", HARD: "DIFICIL", MIXED: null } as const;

/**
 * Material no formato da IA, montado da biblioteca. Nulo quando a biblioteca não cobre o pedido
 * (questões insuficientes no estilo pedido): aí a IA gera, como antes.
 */
export function assembleFromLibrary(
  material: Json,
  note: Json | null,
  input: Pick<ContentJobInput, "board" | "options" | "avoidStatements">,
  seed: string,
): Json | null {
  const options = input.options;
  const wants = (block: string) => options.blocks.includes(block as never);
  const avoid = new Set(input.avoidStatements.map((statement) => statement.trim().toLowerCase()));
  const wanted = DIFFICULTY[options.difficulty];
  const pick = (pool: Json[]) =>
    shuffled(pool.filter((q) =>
      fitsStyle(q, options.questionStyle) && (wanted === null || q.difficulty === wanted) &&
      !avoid.has(String(q.statement).trim().toLowerCase())
    ), seed);
  // Questões do recorte da banca primeiro; depois as da matéria.
  const questions = [...pick((note?.questions as Json[] | undefined) ?? []), ...pick(material.questions as Json[])]
    .slice(0, options.questionCount)
    .map((question) => structuredClone(question));
  if (questions.length < options.questionCount) return null;

  const chapters = wants("THEORY") ? structuredClone(material.chapters as Json[]) : [];
  const board = typeof note?.board === "string" ? note.board : input.board;
  const howItFalls = typeof note?.howItFalls === "string" ? note.howItFalls : null;
  let summary = wants("SUMMARY") ? String(material.summary) : "";
  if (howItFalls && wants("THEORY") && chapters.length < 6) {
    chapters.push({ title: `Como a ${board} cobra este tópico`, markdown: howItFalls });
  } else if (howItFalls && wants("SUMMARY")) {
    summary = `**Como a ${board} cobra:** ${howItFalls}\n\n${summary}`;
  }
  const merged = (field: "tips" | "traps") =>
    wants("TIPS_TRAPS") ? [...new Set([...((note?.[field] as string[]) ?? []), ...(material[field] as string[])])].slice(0, 8) : [];

  const content: Json = {
    schemaVersion: AI_TOPIC_CONTENT_SCHEMA_VERSION,
    promptVersion: CONTENT_PROMPT_VERSION,
    modelVersion: LIBRARY_MODEL_VERSION,
    scope: structuredClone(material.scope),
    theoryTitle: material.theoryTitle,
    chapters,
    summary,
    flashcards: wants("QUICK_REVIEW") ? (material.flashcards as Json[]).slice(0, 30) : [],
    tips: merged("tips"),
    traps: merged("traps"),
    activeRecall: wants("ACTIVE_RECALL") ? (material.activeRecall as Json[]).slice(0, 10) : [],
    errorConcepts: structuredClone(material.errorConcepts),
    questions,
    sources: (material.sources as Json[]).slice(0, 12),
    warnings: [],
  };
  try {
    // Mesma conferência do material gerado pela IA: o app recebe exatamente o formato de sempre.
    return validateTopicContent(
      JSON.stringify(content),
      { schemaVersion: AI_TOPIC_CONTENT_SCHEMA_VERSION, promptVersion: CONTENT_PROMPT_VERSION, modelVersion: LIBRARY_MODEL_VERSION },
      options,
    );
  } catch {
    return null;
  }
}
