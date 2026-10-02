import { SYLLABUS_SOURCE_TITLE_LIMIT } from "./syllabus-text-limits.ts";
// Entrada dos jobs de texto (conteúdo de tópico e plano de estudo).
//
// É o único dado que o modelo recebe além do prompt fixo, então passa por aqui antes de virar
// request_payload: campos conhecidos, tamanhos limitados e nada além disso. Limitar o tamanho é o
// que segura custo e impede que um cliente mande um "tópico" de 200 mil caracteres.

export class TextJobInputError extends Error {
  constructor(public readonly field: string) {
    super("INVALID_REQUEST");
    this.name = "TextJobInputError";
  }
}

export interface ContentJobInput {
  competitionName: string;
  role: string | null;
  board: string | null;
  agency: string | null;
  sphere: "FEDERAL" | "ESTADUAL" | "MUNICIPAL" | null;
  subjectName: string;
  /** Caminho do tópico no edital, da raiz até ele (o último item é o próprio tópico). */
  topicPath: string[];
  scopeCovers: string | null;
  scopeExcludes: string | null;
  /** O que a pessoa escolheu no assistente. Pedidos antigos, sem o campo, recebem o pacote completo. */
  options: ContentGenerationOptions;
  /** Enunciados das questões que a pessoa já tem no tópico ("Gerar mais questões"): não repetir. */
  avoidStatements: string[];
}

export const MAX_AVOID_STATEMENTS = 60;

export const CONTENT_BLOCKS = [
  "THEORY", "SUMMARY", "QUICK_REVIEW", "TIPS_TRAPS", "ACTIVE_RECALL", "QUESTIONS", "ERROR_CONCEPTS",
] as const;
export type ContentBlock = typeof CONTENT_BLOCKS[number];
export type QuestionStyle = "MIXED" | "FIVE_OPTIONS" | "FOUR_OPTIONS" | "TRUE_FALSE";
export type QuestionDifficulty = "MIXED" | "EASY" | "MEDIUM" | "HARD";
export type TheoryDepth = "ESSENTIAL" | "DEEP" | "BOOK";

export interface ContentGenerationOptions {
  blocks: ContentBlock[];
  depth: TheoryDepth;
  /** 0 quando QUESTIONS não foi pedido. O teto por plano é aplicado ao criar o job. */
  questionCount: number;
  questionStyle: QuestionStyle;
  difficulty: QuestionDifficulty;
}

/** Teto absoluto; o limite de cada plano (Grátis 10, Essencial 20, Pro 30) é conferido à parte. */
export const MAX_QUESTIONS_PER_REQUEST = 30;
export const DEFAULT_QUESTION_COUNT = 10;

export interface PlanSubjectInput {
  ref: string;
  name: string;
  priority: "CRITICAL" | "HIGH" | "MEDIUM" | "LOW";
  topics: { ref: string; title: string; studied: boolean }[];
}

export interface PlanJobInput {
  competitionName: string;
  startDate: string;
  endDate: string;
  examDate: string | null;
  /** Minutos por dia da semana, segunda (0) a domingo (6). */
  dayMinutes: number[];
  blockMinutes: number;
  weeklyQuestions: number;
  profile: "DO_ZERO" | "APROFUNDANDO" | "RETA_FINAL";
  preference: string | null;
  subjects: PlanSubjectInput[];
}

const MAX_TEXT = 300;
const MAX_LONG_TEXT = 800;
const MAX_PLAN_DAYS = 120;
const MAX_SUBJECTS = 40;
const MAX_TOPICS = 600;

export function isRecord(value: unknown): value is Record<string, unknown> {
  return value !== null && typeof value === "object" && !Array.isArray(value);
}

export function text(value: unknown, field: string, max = MAX_TEXT): string {
  if (typeof value !== "string") throw new TextJobInputError(field);
  // Controle e quebras viram espaço: o texto entra num prompt e não pode forjar seções dele.
  // deno-lint-ignore no-control-regex
  const clean = value.replace(/[\u0000-\u001f\u007f]+/g, " ").replace(/\s+/g, " ").trim();
  if (clean.length === 0 || clean.length > max) throw new TextJobInputError(field);
  return clean;
}

export function optionalText(value: unknown, field: string, max = MAX_TEXT): string | null {
  if (value === undefined || value === null || value === "") return null;
  return text(value, field, max);
}

function isoDate(value: unknown, field: string): string {
  if (typeof value !== "string" || !/^\d{4}-\d{2}-\d{2}$/.test(value) || Number.isNaN(Date.parse(`${value}T00:00:00Z`))) {
    throw new TextJobInputError(field);
  }
  return value;
}

export function integer(value: unknown, field: string, min: number, max: number): number {
  if (typeof value !== "number" || !Number.isSafeInteger(value) || value < min || value > max) {
    throw new TextJobInputError(field);
  }
  return value;
}

export function oneOf<T extends string>(value: unknown, field: string, allowed: readonly T[]): T {
  if (typeof value !== "string" || !allowed.includes(value as T)) throw new TextJobInputError(field);
  return value as T;
}

export function parseContentJobInput(value: unknown): ContentJobInput {
  if (!isRecord(value)) throw new TextJobInputError("input");
  const path = value.topicPath;
  if (!Array.isArray(path) || path.length === 0 || path.length > 6) throw new TextJobInputError("topicPath");
  return {
    competitionName: text(value.competitionName, "competitionName"),
    role: optionalText(value.role, "role"),
    board: optionalText(value.board, "board", 80),
    agency: optionalText(value.agency, "agency", 160),
    sphere: value.sphere === undefined || value.sphere === null
      ? null
      : oneOf(value.sphere, "sphere", ["FEDERAL", "ESTADUAL", "MUNICIPAL"] as const),
    subjectName: text(value.subjectName, "subjectName"),
    topicPath: path.map((item, index) => text(item, `topicPath.${index}`, SYLLABUS_SOURCE_TITLE_LIMIT)),
    scopeCovers: optionalText(value.scopeCovers, "scopeCovers", MAX_LONG_TEXT),
    scopeExcludes: optionalText(value.scopeExcludes, "scopeExcludes", MAX_LONG_TEXT),
    options: parseContentOptions(value.options),
    avoidStatements: parseAvoidStatements(value.avoidStatements),
  };
}

function parseAvoidStatements(value: unknown): string[] {
  if (value === undefined || value === null) return [];
  if (!Array.isArray(value)) throw new TextJobInputError("avoidStatements");
  return value.slice(0, MAX_AVOID_STATEMENTS)
    .filter((item): item is string => typeof item === "string" && item.trim().length > 0)
    .map((item) => item.replace(/\p{Cc}/gu, " ").trim().slice(0, 300));
}

function parseContentOptions(value: unknown): ContentGenerationOptions {
  if (value === undefined || value === null) {
    return { blocks: [...CONTENT_BLOCKS], depth: "DEEP", questionCount: DEFAULT_QUESTION_COUNT, questionStyle: "MIXED", difficulty: "MIXED" };
  }
  if (!isRecord(value)) throw new TextJobInputError("options");
  const rawBlocks = value.blocks;
  if (!Array.isArray(rawBlocks) || rawBlocks.length === 0 || rawBlocks.length > CONTENT_BLOCKS.length) {
    throw new TextJobInputError("options.blocks");
  }
  const blocks = rawBlocks.map((block, index) => oneOf(block, `options.blocks.${index}`, CONTENT_BLOCKS));
  if (new Set(blocks).size !== blocks.length) throw new TextJobInputError("options.blocks");
  const wantsQuestions = blocks.includes("QUESTIONS");
  return {
    blocks: CONTENT_BLOCKS.filter((block) => blocks.includes(block)),
    depth: value.depth === undefined ? "DEEP" : oneOf(value.depth, "options.depth", ["ESSENTIAL", "DEEP", "BOOK"] as const),
    questionCount: wantsQuestions ? integer(value.questionCount ?? DEFAULT_QUESTION_COUNT, "options.questionCount", 1, MAX_QUESTIONS_PER_REQUEST) : 0,
    questionStyle: value.questionStyle === undefined
      ? "MIXED"
      : oneOf(value.questionStyle, "options.questionStyle", ["MIXED", "FIVE_OPTIONS", "FOUR_OPTIONS", "TRUE_FALSE"] as const),
    difficulty: value.difficulty === undefined
      ? "MIXED"
      : oneOf(value.difficulty, "options.difficulty", ["MIXED", "EASY", "MEDIUM", "HARD"] as const),
  };
}

export function parsePlanJobInput(value: unknown): PlanJobInput {
  if (!isRecord(value)) throw new TextJobInputError("input");
  const startDate = isoDate(value.startDate, "startDate");
  const endDate = isoDate(value.endDate, "endDate");
  const examDate = value.examDate === null || value.examDate === undefined ? null : isoDate(value.examDate, "examDate");
  const span = (Date.parse(`${endDate}T00:00:00Z`) - Date.parse(`${startDate}T00:00:00Z`)) / 86_400_000 + 1;
  if (span < 1 || span > MAX_PLAN_DAYS) throw new TextJobInputError("endDate");
  if (examDate !== null && examDate < endDate) throw new TextJobInputError("examDate");

  const days = value.dayMinutes;
  if (!Array.isArray(days) || days.length !== 7) throw new TextJobInputError("dayMinutes");
  const dayMinutes = days.map((minutes, index) => integer(minutes, `dayMinutes.${index}`, 0, 16 * 60));
  if (dayMinutes.every((minutes) => minutes === 0)) throw new TextJobInputError("dayMinutes");

  const rawSubjects = value.subjects;
  if (!Array.isArray(rawSubjects) || rawSubjects.length === 0 || rawSubjects.length > MAX_SUBJECTS) {
    throw new TextJobInputError("subjects");
  }
  const refs = new Set<string>();
  const uniqueRef = (ref: unknown, field: string, prefix: "s" | "t"): string => {
    if (typeof ref !== "string" || !new RegExp(`^${prefix}\\d{1,4}$`).test(ref) || refs.has(ref)) {
      throw new TextJobInputError(field);
    }
    refs.add(ref);
    return ref;
  };
  let topicCount = 0;
  const subjects = rawSubjects.map((subject, index): PlanSubjectInput => {
    if (!isRecord(subject) || !Array.isArray(subject.topics)) throw new TextJobInputError(`subjects.${index}`);
    topicCount += subject.topics.length;
    if (topicCount > MAX_TOPICS) throw new TextJobInputError("subjects.topics");
    return {
      ref: uniqueRef(subject.ref, `subjects.${index}.ref`, "s"),
      name: text(subject.name, `subjects.${index}.name`),
      priority: oneOf(subject.priority, `subjects.${index}.priority`, ["CRITICAL", "HIGH", "MEDIUM", "LOW"] as const),
      topics: subject.topics.map((topic, topicIndex) => {
        if (!isRecord(topic)) throw new TextJobInputError(`subjects.${index}.topics.${topicIndex}`);
        return {
          ref: uniqueRef(topic.ref, `subjects.${index}.topics.${topicIndex}.ref`, "t"),
          title: text(topic.title, `subjects.${index}.topics.${topicIndex}.title`, MAX_LONG_TEXT),
          studied: topic.studied === true,
        };
      }),
    };
  });

  return {
    competitionName: text(value.competitionName, "competitionName"),
    startDate,
    endDate,
    examDate,
    dayMinutes,
    blockMinutes: integer(value.blockMinutes, "blockMinutes", 15, 180),
    weeklyQuestions: integer(value.weeklyQuestions, "weeklyQuestions", 0, 2000),
    profile: oneOf(value.profile, "profile", ["DO_ZERO", "APROFUNDANDO", "RETA_FINAL"] as const),
    preference: optionalText(value.preference, "preference", MAX_LONG_TEXT),
    subjects,
  };
}
