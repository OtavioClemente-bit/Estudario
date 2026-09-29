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
}

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

function isRecord(value: unknown): value is Record<string, unknown> {
  return value !== null && typeof value === "object" && !Array.isArray(value);
}

function text(value: unknown, field: string, max = MAX_TEXT): string {
  if (typeof value !== "string") throw new TextJobInputError(field);
  // Controle e quebras viram espaço: o texto entra num prompt e não pode forjar seções dele.
  const clean = value.replace(/[\u0000-\u001f\u007f]+/g, " ").replace(/\s+/g, " ").trim();
  if (clean.length === 0 || clean.length > max) throw new TextJobInputError(field);
  return clean;
}

function optionalText(value: unknown, field: string, max = MAX_TEXT): string | null {
  if (value === undefined || value === null || value === "") return null;
  return text(value, field, max);
}

function isoDate(value: unknown, field: string): string {
  if (typeof value !== "string" || !/^\d{4}-\d{2}-\d{2}$/.test(value) || Number.isNaN(Date.parse(`${value}T00:00:00Z`))) {
    throw new TextJobInputError(field);
  }
  return value;
}

function integer(value: unknown, field: string, min: number, max: number): number {
  if (typeof value !== "number" || !Number.isSafeInteger(value) || value < min || value > max) {
    throw new TextJobInputError(field);
  }
  return value;
}

function oneOf<T extends string>(value: unknown, field: string, allowed: readonly T[]): T {
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
    topicPath: path.map((item, index) => text(item, `topicPath.${index}`, MAX_LONG_TEXT)),
    scopeCovers: optionalText(value.scopeCovers, "scopeCovers", MAX_LONG_TEXT),
    scopeExcludes: optionalText(value.scopeExcludes, "scopeExcludes", MAX_LONG_TEXT),
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
