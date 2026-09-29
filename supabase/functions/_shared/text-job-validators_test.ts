import { assertEquals, assertThrows } from "https://deno.land/std@0.224.0/assert/mod.ts";
import { parseContentJobInput, TextJobInputError } from "./text-job-input.ts";
import { validateTopicContent } from "./text-job-validators.ts";
import { ProposalValidationError } from "./proposal-validator.ts";

const expected = { schemaVersion: 2, promptVersion: "topic-content-v2", modelVersion: "m" };
const baseInput = { competitionName: "TRT-3", subjectName: "Português", topicPath: ["Crase"] };

function question(index: number, overrides: Record<string, unknown> = {}) {
  return {
    statement: `Enunciado ${index}`,
    format: "MULTIPLE_CHOICE",
    difficulty: "MEDIA",
    options: ["A", "B", "C", "D", "E"].map((key) => ({ key, text: `Alt ${key}`, correct: key === "B" })),
    explanation: "Porque sim, art. 1.",
    section: "Questões",
    errorConceptKey: "e1",
    sourceType: "AUTHORIAL",
    board: null,
    agency: null,
    year: null,
    sourceUrl: null,
    ...overrides,
  };
}

function output(overrides: Record<string, unknown> = {}) {
  return JSON.stringify({
    ...expected,
    scope: { covers: "Crase", excludes: "" },
    theoryTitle: "Crase",
    chapters: [],
    summary: "",
    quickReview: "",
    tips: [],
    traps: [],
    activeRecall: [],
    errorConcepts: [{ key: "e1", title: "Crase antes de masculino", summary: "Não ocorre." }],
    questions: [question(1), question(2), question(3)],
    sources: [{ kind: "OFICIAL", title: "Gramática", publisher: "X", reference: "", url: "https://example.org", accessedAt: "2026-09-29" }],
    warnings: [],
    ...overrides,
  });
}

Deno.test("pedido antigo sem options recebe o pacote completo com 10 questões", () => {
  const input = parseContentJobInput(baseInput);
  assertEquals(input.options.questionCount, 10);
  assertEquals(input.options.blocks.length, 7);
});

Deno.test("recusa quantidade acima do teto absoluto e bloco desconhecido", () => {
  assertThrows(() => parseContentJobInput({ ...baseInput, options: { blocks: ["QUESTIONS"], questionCount: 31 } }), TextJobInputError);
  assertThrows(() => parseContentJobInput({ ...baseInput, options: { blocks: ["VIDEO"] } }), TextJobInputError);
});

Deno.test("só questões: aceita a quantidade exata, sem teoria", () => {
  const options = parseContentJobInput({ ...baseInput, options: { blocks: ["QUESTIONS"], questionCount: 3, difficulty: "MEDIUM", questionStyle: "FIVE_OPTIONS" } }).options;
  validateTopicContent(output(), expected, options);
});

Deno.test("recusa quantidade diferente da pedida e parte não pedida", () => {
  const options = parseContentJobInput({ ...baseInput, options: { blocks: ["QUESTIONS"], questionCount: 4 } }).options;
  assertThrows(() => validateTopicContent(output(), expected, options), ProposalValidationError);
  const three = { ...options, questionCount: 3 };
  assertThrows(() => validateTopicContent(output({ summary: "Resumo que ninguém pediu" }), expected, three), ProposalValidationError);
});

Deno.test("respeita formato A a D e a dificuldade pedida", () => {
  const options = parseContentJobInput({ ...baseInput, options: { blocks: ["QUESTIONS"], questionCount: 1, questionStyle: "FOUR_OPTIONS", difficulty: "HARD" } }).options;
  const fourOptions = question(1, { difficulty: "DIFICIL", options: ["A", "B", "C", "D"].map((key) => ({ key, text: key, correct: key === "A" })) });
  validateTopicContent(output({ questions: [fourOptions] }), expected, options);
  assertThrows(() => validateTopicContent(output({ questions: [question(1, { difficulty: "DIFICIL" })] }), expected, options), ProposalValidationError);
  assertThrows(() => validateTopicContent(output({ questions: [{ ...fourOptions, difficulty: "FACIL" }] }), expected, options), ProposalValidationError);
});
