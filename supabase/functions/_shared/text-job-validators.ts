import type { ContentBlock, ContentGenerationOptions, PlanJobInput } from "./text-job-input.ts";
import { ProposalValidationError } from "./proposal-validator.ts";

// Regras que o JSON Schema não expressa. Uma resposta que falha aqui não chega ao app: o job é
// finalizado como falha e a cota do dia é devolvida, em vez de a pessoa receber algo inventado.

export interface ExpectedVersions {
  schemaVersion: number;
  promptVersion: string;
  modelVersion: string;
}

type Json = Record<string, unknown>;

function parse(raw: string): Json {
  if (typeof raw !== "string" || raw.trim().length === 0) {
    throw new ProposalValidationError("EMPTY_OUTPUT", "provider returned no structured output");
  }
  let value: unknown;
  try {
    value = JSON.parse(raw);
  } catch {
    throw new ProposalValidationError("SCHEMA_MISMATCH", "provider output is not JSON");
  }
  if (value === null || typeof value !== "object" || Array.isArray(value)) {
    throw new ProposalValidationError("SCHEMA_MISMATCH", "provider output is not an object");
  }
  return value as Json;
}

function reject(reason: string): never {
  throw new ProposalValidationError("SCHEMA_MISMATCH", reason);
}

function checkVersions(value: Json, expected: ExpectedVersions): void {
  const codes = [] as ("VERSION_MISMATCH_SCHEMA" | "VERSION_MISMATCH_PROMPT" | "VERSION_MISMATCH_MODEL")[];
  if (value.schemaVersion !== expected.schemaVersion) codes.push("VERSION_MISMATCH_SCHEMA");
  if (value.promptVersion !== expected.promptVersion) codes.push("VERSION_MISMATCH_PROMPT");
  if (value.modelVersion !== expected.modelVersion) codes.push("VERSION_MISMATCH_MODEL");
  if (codes.length > 0) {
    throw new ProposalValidationError("VERSION_MISMATCH", "provider output versions differ from the worker", codes);
  }
}

function array(value: unknown, field: string): Json[] {
  if (!Array.isArray(value)) reject(`${field} is not a list`);
  return value.map((item) => {
    if (item === null || typeof item !== "object" || Array.isArray(item)) reject(`${field} item is not an object`);
    return item as Json;
  });
}

function nonBlank(value: unknown, field: string, max = 60_000): string {
  if (typeof value !== "string" || value.trim().length === 0 || value.length > max) reject(`${field} is empty or too long`);
  return value;
}

function httpsUrl(value: unknown, field: string): string {
  const url = nonBlank(value, field, 2_000);
  try {
    const parsed = new URL(url);
    if (parsed.protocol !== "https:" && parsed.protocol !== "http:") reject(`${field} is not a web URL`);
  } catch {
    reject(`${field} is not a URL`);
  }
  return url;
}

export const MIN_FLASHCARDS = 12;
export const MAX_FLASHCARDS = 30;

function blank(value: unknown): boolean {
  return value === undefined || value === null || (typeof value === "string" && value.trim().length === 0) ||
    (Array.isArray(value) && value.length === 0);
}

/**
 * Conteúdo de um tópico: só as partes pedidas, a quantidade exata de questões no formato pedido,
 * coerência interna das questões, vínculos e fontes.
 */
export function validateTopicContent(raw: string, expected: ExpectedVersions, options: ContentGenerationOptions): Json {
  const value = parse(raw);
  checkVersions(value, expected);
  const wants = (block: ContentBlock) => options.blocks.includes(block);
  const onlyWhenAsked = (block: ContentBlock, field: string) => {
    if (!wants(block) && !blank(value[field])) reject(`${field} was not requested`);
  };

  const chapters = array(value.chapters, "chapters");
  if (wants("THEORY") ? chapters.length < 2 || chapters.length > 6 : chapters.length !== 0) reject("chapter count");
  const chapterTitles = new Set(chapters.map((chapter, index) => nonBlank(chapter.title, `chapters.${index}.title`, 200).trim()));
  chapters.forEach((chapter, index) => nonBlank(chapter.markdown, `chapters.${index}.markdown`));
  if (wants("SUMMARY")) nonBlank(value.summary, "summary");
  // Baralho: quantidade que dá para estudar de verdade, cartões curtos e sem repetição.
  const cards = array(value.flashcards, "flashcards");
  if (wants("QUICK_REVIEW") ? cards.length < MIN_FLASHCARDS || cards.length > MAX_FLASHCARDS : cards.length !== 0) reject("flashcard count");
  const fronts = new Set<string>();
  cards.forEach((card, index) => {
    const front = nonBlank(card.front, `flashcards.${index}.front`, 240).trim().toLowerCase();
    nonBlank(card.back, `flashcards.${index}.back`, 3_000);
    if (fronts.has(front)) reject(`flashcards.${index} repeats a front`);
    fronts.add(front);
  });
  onlyWhenAsked("SUMMARY", "summary");
  onlyWhenAsked("ACTIVE_RECALL", "activeRecall");
  if (!wants("TIPS_TRAPS") && (!blank(value.tips) || !blank(value.traps))) reject("tips were not requested");

  const concepts = array(value.errorConcepts, "errorConcepts");
  const conceptKeys = new Set(concepts.map((concept) => String(concept.key)));
  if (conceptKeys.size !== concepts.length) reject("duplicated error concept key");
  if (wants("ERROR_CONCEPTS") && concepts.length === 0) reject("error concepts missing");

  const questions = array(value.questions, "questions");
  if (questions.length !== options.questionCount) reject("question count");
  const statements = new Set<string>();
  questions.forEach((question, index) => {
    const field = `questions.${index}`;
    const statement = nonBlank(question.statement, `${field}.statement`, 4_000).trim().toLowerCase();
    if (statements.has(statement)) reject(`${field} repeats a statement`);
    statements.add(statement);
    const answerOptions = array(question.options, `${field}.options`);
    const keys = answerOptions.map((option) => String(option.key));
    const style = options.questionStyle;
    if (style === "TRUE_FALSE" && question.format !== "TRUE_FALSE") reject(`${field} must be Certo/Errado`);
    if ((style === "FIVE_OPTIONS" || style === "FOUR_OPTIONS") && question.format !== "MULTIPLE_CHOICE") reject(`${field} must be multiple choice`);
    const expectedKeys = question.format === "TRUE_FALSE"
      ? ["C", "E"]
      : style === "FOUR_OPTIONS" ? ["A", "B", "C", "D"] : ["A", "B", "C", "D", "E"];
    if (keys.join() !== expectedKeys.join()) reject(`${field} has unexpected option keys`);
    if (answerOptions.filter((option) => option.correct === true).length !== 1) reject(`${field} must have exactly one correct option`);
    answerOptions.forEach((option, optionIndex) => nonBlank(option.text, `${field}.options.${optionIndex}.text`, 2_000));
    nonBlank(question.explanation, `${field}.explanation`, 8_000);
    const section = nonBlank(question.section, `${field}.section`, 200).trim();
    if (chapterTitles.size > 0 && !chapterTitles.has(section)) reject(`${field}.section is not a chapter title`);
    if (question.errorConceptKey !== null && !conceptKeys.has(String(question.errorConceptKey))) reject(`${field}.errorConceptKey is unknown`);
    const difficulty = { EASY: "FACIL", MEDIUM: "MEDIA", HARD: "DIFICIL", MIXED: null }[options.difficulty];
    if (difficulty !== null && question.difficulty !== difficulty) reject(`${field} difficulty differs from the request`);
    if (question.sourceType === "REAL") {
      httpsUrl(question.sourceUrl, `${field}.sourceUrl`);
      nonBlank(question.board, `${field}.board`, 120);
      if (typeof question.year !== "number" || question.year < 1980 || question.year > 2100) reject(`${field}.year`);
    } else if (question.sourceUrl !== null || question.board !== null || question.agency !== null || question.year !== null) {
      reject(`${field} authorial question must not claim an origin`);
    }
  });

  const sources = array(value.sources, "sources");
  if (sources.length === 0) reject("no sources");
  sources.forEach((source, index) => {
    nonBlank(source.title, `sources.${index}.title`, 400);
    httpsUrl(source.url, `sources.${index}.url`);
    if (typeof source.accessedAt !== "string" || !/^\d{4}-\d{2}-\d{2}$/.test(source.accessedAt)) reject(`sources.${index}.accessedAt`);
  });
  return value;
}

/** Plano: só refs enviadas, dias dentro do prazo e minutos dentro de cada dia. */
export function validateStudyPlan(raw: string, expected: ExpectedVersions, input: PlanJobInput): Json {
  const value = parse(raw);
  checkVersions(value, expected);
  nonBlank(value.summary, "summary", 4_000);

  const start = Date.parse(`${input.startDate}T00:00:00Z`);
  const lastDay = Math.round((Date.parse(`${input.endDate}T00:00:00Z`) - start) / 86_400_000);
  const subjectOfTopic = new Map<string, string>();
  const subjectRefs = new Set<string>();
  for (const subject of input.subjects) {
    subjectRefs.add(subject.ref);
    for (const topic of subject.topics) subjectOfTopic.set(topic.ref, subject.ref);
  }

  array(value.phases, "phases").forEach((phase, index) => {
    nonBlank(phase.name, `phases.${index}.name`, 200);
    const from = phase.fromDay, to = phase.toDay;
    if (typeof from !== "number" || typeof to !== "number" || from < 0 || to > lastDay || from > to) reject(`phases.${index} days`);
  });

  const usedByDay = new Map<number, number>();
  const tasks = array(value.tasks, "tasks");
  if (tasks.length === 0) reject("no tasks");
  tasks.forEach((task, index) => {
    const field = `tasks.${index}`;
    const day = task.d;
    if (typeof day !== "number" || !Number.isSafeInteger(day) || day < 0 || day > lastDay) reject(`${field}.d`);
    const minutes = task.m, questions = task.q;
    if (typeof minutes !== "number" || !Number.isSafeInteger(minutes) || minutes < 5 || minutes > 16 * 60) reject(`${field}.m`);
    if (typeof questions !== "number" || !Number.isSafeInteger(questions) || questions < 0 || questions > 300) reject(`${field}.q`);
    if (task.k === "SIMULATION") {
      if (task.s !== null || task.t !== null) reject(`${field} simulation must not be linked`);
    } else {
      if (typeof task.s !== "string" || !subjectRefs.has(task.s)) reject(`${field}.s is unknown`);
      if (task.t !== null) {
        if (typeof task.t !== "string" || subjectOfTopic.get(task.t) !== task.s) reject(`${field}.t is unknown or in another subject`);
      }
    }
    if (task.k === "QUESTIONS" && questions === 0) reject(`${field} question task without questions`);
    const weekday = (new Date(start + day * 86_400_000).getUTCDay() + 6) % 7;
    const used = (usedByDay.get(day) ?? 0) + minutes;
    if (used > input.dayMinutes[weekday]) reject(`${field} exceeds the day capacity`);
    usedByDay.set(day, used);
  });
  return value;
}
