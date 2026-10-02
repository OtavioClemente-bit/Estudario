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

export function repairTopicContent(raw: string, options: ContentGenerationOptions, reserve: number): string {
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
      const statement = text(q.statement).toLowerCase();
      if (!statement || statements.has(statement) || !text(q.explanation) || hasDraftExplanation(q)) return false;
      const answers = Array.isArray(q.options) ? q.options.filter(isObject) : [];
      const keys = answers.map((option) => String(option.key)).join();
      const expected = q.format === "TRUE_FALSE" ? "C,E" : options.questionStyle === "FOUR_OPTIONS" ? "A,B,C,D" : "A,B,C,D,E";
      if (keys !== expected) return false;
      if (answers.filter((option) => option.correct === true).length !== 1) return false;
      if (answers.some((option) => !text(option.text))) return false;
      statements.add(statement);
      return true;
    }).slice(0, options.questionCount + reserve);
  }

  if (Array.isArray(value.sources)) {
    value.sources = value.sources.filter(isObject).map((source) => {
      const accessed = text(source.accessedAt);
      return /^\d{4}-\d{2}-\d{2}/.test(accessed) ? { ...source, accessedAt: accessed.slice(0, 10) } : source;
    }).filter((source) => validUrl(source.url) && text(source.title) && /^\d{4}-\d{2}-\d{2}$/.test(text(source.accessedAt)));
  }

  return JSON.stringify(value);
}
