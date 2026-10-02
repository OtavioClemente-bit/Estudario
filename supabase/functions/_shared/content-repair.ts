import type { ContentGenerationOptions } from "./text-job-input.ts";
import { MAX_FLASHCARDS } from "./text-job-validators.ts";

// Conserto do material antes da validação. O validador é tudo ou nada: um título de capítulo
// escrito um pouco diferente na questão ou um ano preenchido numa questão autoral jogava fora o
// material inteiro, já pago. Aqui se corrige o que tem conserto seguro e se descarta só a peça
// defeituosa (a questão com duas corretas, a fonte sem URL). O que sobra passa pelo validador.

type Json = Record<string, unknown>;

const isObject = (value: unknown): value is Json => value !== null && typeof value === "object" && !Array.isArray(value);
const text = (value: unknown) => (typeof value === "string" ? value.trim() : "");
const normal = (value: string) =>
  value.normalize("NFD").replace(/[̀-ͯ]/g, "").toLowerCase().replace(/^\s*\d+[.)-]?\s*/, "").replace(/[^a-z0-9 ]/g, " ").replace(/\s+/g, " ").trim();

/** Título de capítulo mais parecido com o que a questão citou (inclusão ou mais palavras em comum). */
function closestTitle(section: string, titles: string[]): string {
  const wanted = normal(section);
  const exact = titles.find((title) => normal(title) === wanted);
  if (exact) return exact;
  const contains = titles.find((title) => normal(title).includes(wanted) || wanted.includes(normal(title)));
  if (contains) return contains;
  const words = new Set(wanted.split(" ").filter((word) => word.length > 3));
  let best = titles[0];
  let score = -1;
  for (const title of titles) {
    const common = normal(title).split(" ").filter((word) => words.has(word)).length;
    if (common > score) [best, score] = [title, common];
  }
  return best;
}

function validUrl(value: unknown): boolean {
  try {
    const url = new URL(text(value));
    return url.protocol === "https:" || url.protocol === "http:";
  } catch {
    return false;
  }
}

/**
 * Explicação em que o próprio modelo admite que a questão está errada ("com a correção da
 * alternativa A", "a alternativa correta deveria refletir"): o gabarito não é confiável.
 */
const DRAFT_EXPLANATION = /deve(?:ria)? ser corrigid|com a corre[cç][aã]o d[ao]|deveria refletir|observando as op[cç][oõ]es|gabarito (?:deve|precisa) ser|alternativa .{0,20}precisa ser ajustad/i;

export function hasDraftExplanation(question: Record<string, unknown>): boolean {
  return typeof question.explanation === "string" && DRAFT_EXPLANATION.test(question.explanation);
}

const CHART_FENCE = /```[ \t]*(gr[aá]fico|geometria|figura|fun[cç][aã]o|pizza|barras|linha|chart)[ \t]*\n/gi;

function renameChartFences(value: unknown): unknown {
  if (typeof value === "string") return value.replace(CHART_FENCE, "```grafico\n");
  if (Array.isArray(value)) return value.map(renameChartFences);
  if (isObject(value)) return Object.fromEntries(Object.entries(value).map(([key, item]) => [key, renameChartFences(item)]));
  return value;
}

// Letra citada como alternativa na explicação: "A combina…", "B e E confundem", "Gabarito: C.",
// "alternativa D". O artigo "A" ("A tabela…") não entra porque o verbo seguinte não está na lista.
const LETTER_REFERENCE = new RegExp(
  "(?<![\\p{L}\\d\\u0001])([A-E])(?=\\)|[,;:.]|\\s+(?:e|ou|é|são|está|estão|está|traz|confunde|confundem|troca|trocam|inverte|invertem|ignora|ignoram|usa|usam|erra|erram|atribui|atribuem|apresenta|descreve|combina|generaliza|soma|omite|omitem|considera|calcula|resulta|resultam|aplica|desloca|mantém|informa|não|também|expressa|enuncia|reúne|embaralha|embaralham|correta|incorreta|incluem|inclui|decorre|decorrem|dobra|duplica|subtrai|mistura|reduz|amplia|restringe|nega|inventa|supõe|trata|limita|faz|leva)(?![\\p{L}\\d]))",
  "gu",
);

function swapLetters(text: string, a: string, b: string): string {
  // Marca cada letra encontrada antes de trocar, para "alternativa A combina" não ser trocada duas vezes.
  const mark = (letter: string) => "\u0001" + letter;
  return text
    .replace(/(alternativas?\s+)([A-E])(?![\p{L}\d])/gu, (_, prefix: string, letter: string) => prefix + mark(letter))
    .replace(LETTER_REFERENCE, mark)
    .replace(/\u0001([A-E])/g, (_, letter: string) => (letter === a ? b : letter === b ? a : letter));
}

/**
 * O modelo põe a correta quase sempre em "A" (9 de 10 num material), e o aluno aprende a chutar.
 * Quando uma letra passa da conta, a alternativa correta troca de lugar com a de uma letra menos
 * usada, e as letras citadas na explicação acompanham a troca.
 */
export function balanceAnswerKeys(questions: Json[]): Json[] {
  const multiple = questions.filter((q) => q.format === "MULTIPLE_CHOICE" && Array.isArray(q.options));
  if (multiple.length < 3) return questions;
  const letters = (multiple[0].options as Json[]).map((option) => String(option.key));
  const count = new Map(letters.map((letter) => [letter, 0]));
  const correctOf = (q: Json) => String(((q.options as Json[]).find((option) => option.correct === true) ?? {}).key);
  multiple.forEach((q) => count.set(correctOf(q), (count.get(correctOf(q)) ?? 0) + 1));
  const limit = Math.ceil(multiple.length / letters.length) + 1;
  return questions.map((q) => {
    if (q.format !== "MULTIPLE_CHOICE" || !Array.isArray(q.options)) return q;
    const from = correctOf(q);
    if ((count.get(from) ?? 0) <= limit) return q;
    const to = [...count.entries()].filter(([letter]) => letters.includes(letter)).sort((x, y) => x[1] - y[1])[0][0];
    if (to === from) return q;
    count.set(from, (count.get(from) ?? 0) - 1);
    count.set(to, (count.get(to) ?? 0) + 1);
    const options = (q.options as Json[]).map((option) => ({ ...option }));
    const i = options.findIndex((option) => option.key === from);
    const j = options.findIndex((option) => option.key === to);
    [options[i].text, options[j].text] = [options[j].text, options[i].text];
    [options[i].correct, options[j].correct] = [options[j].correct, options[i].correct];
    return { ...q, options, explanation: swapLetters(String(q.explanation ?? ""), from, to) };
  });
}

/** Por que uma questão saiu do material; fica registrado para saber o que o modelo mais erra. */
export type DropReason =
  | "EMPTY_STATEMENT" | "DUPLICATE" | "NO_EXPLANATION" | "DRAFT_EXPLANATION" | "WRONG_OPTION_KEYS"
  | "NOT_ONE_CORRECT" | "EMPTY_OPTION" | "MIXED_UNITS" | "REVIEW_REMOVED" | "REVIEW_DRAFT";

// Unidade no fim de uma alternativa numérica ("26 m", "52 m²", "4800 N").
const TRAILING_UNIT = /\d\s*(km|cm|mm|m|m²|cm²|mm²|km²|m³|cm³|kg|g|mg|kPa|Pa|kN|N|mA|A|kV|V|kΩ|Ω|kW|W|kJ|J|kWh|L|mL|s|min|h|%)\.?$/;

/** Alternativas numéricas com unidades diferentes: a unidade entrega o gabarito (ou ele está errado). */
function mixedUnits(texts: string[]): boolean {
  const units = texts.map((value) => value.match(TRAILING_UNIT)?.[1]).filter((unit): unit is string => unit !== undefined);
  return units.length >= 3 && units.length === texts.length && new Set(units).size > 1;
}

function brokenQuestion(q: Json, statements: Set<string>, options: ContentGenerationOptions): DropReason | null {
  const statement = text(q.statement).toLowerCase();
  if (!statement) return "EMPTY_STATEMENT";
  if (statements.has(statement)) return "DUPLICATE";
  if (!text(q.explanation)) return "NO_EXPLANATION";
  if (hasDraftExplanation(q)) return "DRAFT_EXPLANATION";
  const answers = Array.isArray(q.options) ? q.options.filter(isObject) : [];
  const keys = answers.map((option) => String(option.key)).join();
  const expected = q.format === "TRUE_FALSE" ? "C,E" : options.questionStyle === "FOUR_OPTIONS" ? "A,B,C,D" : "A,B,C,D,E";
  if (keys !== expected) return "WRONG_OPTION_KEYS";
  if (answers.filter((option) => option.correct === true).length !== 1) return "NOT_ONE_CORRECT";
  if (answers.some((option) => !text(option.text))) return "EMPTY_OPTION";
  if (mixedUnits(answers.map((option) => text(option.text)))) return "MIXED_UNITS";
  return null;
}

export function repairTopicContent(
  raw: string,
  options: ContentGenerationOptions,
  reserve: number,
  onDrop?: (reason: DropReason, question: Record<string, unknown>) => void,
): string {
  let value: Json;
  try {
    const parsed = JSON.parse(raw);
    if (!isObject(parsed)) return raw;
    value = parsed;
  } catch {
    return raw;
  }

  // Capítulos a mais viram parte do último permitido, em vez de recusar a teoria.
  if (Array.isArray(value.chapters) && value.chapters.length > 6) {
    const chapters = value.chapters.filter(isObject);
    const extra = chapters.slice(5);
    value.chapters = [
      ...chapters.slice(0, 5),
      { ...extra[0], markdown: extra.map((chapter, i) => (i === 0 ? text(chapter.markdown) : `## ${text(chapter.title)}\n\n${text(chapter.markdown)}`)).join("\n\n") },
    ];
  }
  const titles = (Array.isArray(value.chapters) ? value.chapters : []).filter(isObject).map((chapter) => text(chapter.title)).filter(Boolean);

  if (Array.isArray(value.flashcards)) {
    const fronts = new Set<string>();
    value.flashcards = value.flashcards.filter((card) => {
      if (!isObject(card)) return false;
      const front = text(card.front).toLowerCase();
      if (!front || !text(card.back) || fronts.has(front)) return false;
      fronts.add(front);
      return true;
    }).slice(0, MAX_FLASHCARDS);
  }

  if (Array.isArray(value.errorConcepts)) {
    const keys = new Set<string>();
    value.errorConcepts = value.errorConcepts.filter((concept) => {
      if (!isObject(concept)) return false;
      const key = String(concept.key);
      if (keys.has(key)) return false;
      keys.add(key);
      return true;
    });
  }
  const conceptKeys = new Set((Array.isArray(value.errorConcepts) ? value.errorConcepts : []).filter(isObject).map((concept) => String(concept.key)));

  if (Array.isArray(value.questions)) {
    const difficulty = { EASY: "FACIL", MEDIUM: "MEDIA", HARD: "DIFICIL", MIXED: null }[options.difficulty];
    const statements = new Set<string>();
    value.questions = value.questions.filter(isObject).map((question) => {
      const q: Json = { ...question };
      if (difficulty) q.difficulty = difficulty;
      if (options.questionStyle === "FIVE_OPTIONS" || options.questionStyle === "FOUR_OPTIONS") q.format = "MULTIPLE_CHOICE";
      if (options.questionStyle === "TRUE_FALSE") q.format = "TRUE_FALSE";
      if (titles.length > 0 && !titles.includes(text(q.section))) q.section = closestTitle(text(q.section), titles);
      if (q.errorConceptKey !== null && !conceptKeys.has(String(q.errorConceptKey))) q.errorConceptKey = null;
      const realIsComplete = q.sourceType === "REAL" && validUrl(q.sourceUrl) && text(q.board).length > 0 &&
        typeof q.year === "number" && q.year >= 1980 && q.year <= 2100;
      if (!realIsComplete) Object.assign(q, { sourceType: "AUTHORIAL", sourceUrl: null, board: null, agency: null, year: null });
      return q;
    }).filter((q) => {
      // Questão sem conserto seguro sai; a reserva cobre o lugar dela.
      const reason = brokenQuestion(q, statements, options);
      if (reason) {
        onDrop?.(reason, q);
        return false;
      }
      statements.add(text(q.statement).toLowerCase());
      return true;
    }).slice(0, options.questionCount + reserve);
    value.questions = balanceAnswerKeys(value.questions as Json[]);
  }

  // Bloco de figura marcado com o tipo ("```geometria") em vez de "```grafico": o app mostrava o
  // código. Troca o nome em todo o texto, para valer também em versões antigas do app.
  value = renameChartFences(value) as Json;

  if (Array.isArray(value.sources)) {
    value.sources = value.sources.filter(isObject).map((source) => {
      const accessed = text(source.accessedAt);
      return /^\d{4}-\d{2}-\d{2}/.test(accessed) ? { ...source, accessedAt: accessed.slice(0, 10) } : source;
    }).filter((source) => validUrl(source.url) && text(source.title) && /^\d{4}-\d{2}-\d{2}$/.test(text(source.accessedAt)));
  }

  return JSON.stringify(value);
}
