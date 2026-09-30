import { assert, assertEquals, assertStringIncludes, assertThrows } from "https://deno.land/std@0.224.0/assert/mod.ts";
import {
  boardGuide,
  parseSimulationJobInput,
  SIMULATION_PROMPT_VERSION,
  simulationUserPrompt,
  validateSimulation,
} from "./simulation.ts";
import { withSimulationReview } from "./text-job-specs.ts";

const expected = { schemaVersion: 1, promptVersion: SIMULATION_PROMPT_VERSION, modelVersion: "m" };

const baseInput = {
  competitionName: "TRT 3ª Região",
  role: "Analista Judiciário",
  board: "FCC",
  mode: "STUDIED",
  style: "FIVE_OPTIONS",
  partIndex: 0,
  partCount: 1,
  items: [
    { subjectName: "Direito Administrativo", topicPath: ["Lei 8.112/1990", "Provimento"], count: 2, difficulty: "MEDIA" },
    { subjectName: "Português", topicPath: ["Crase"], count: 1, difficulty: "MISTA" },
  ],
  weakSpots: ["confunde posse com exercício"],
  avoid: ["Qual o prazo para a posse do servidor nomeado?"],
};

const option = (key: string, correct: boolean, text = `alternativa ${key}`) => ({ key, text, correct });
const question = (itemRef: string, statement: string, correctKey = "B", difficulty = "MEDIA") => ({
  itemRef,
  statement,
  format: "MULTIPLE_CHOICE",
  difficulty,
  options: ["A", "B", "C", "D", "E"].map((key) => option(key, key === correctKey, `${statement.slice(0, 10)} ${key}`)),
  explanation: "A correta segue o art. 13 da Lei 8.112; as demais trocam prazo ou sujeito.",
  trap: "Confundir posse com exercício.",
  targetsWeakSpot: null,
});
const output = (questions: ReturnType<typeof question>[]) => ({
  schemaVersion: 1,
  promptVersion: SIMULATION_PROMPT_VERSION,
  modelVersion: "m",
  detectedBoard: { name: null, sourceUrl: null },
  questions,
  sources: [],
  warnings: [],
});
const good = () => output([
  question("i1", "João foi nomeado para cargo efetivo no TRT e ainda não tomou posse. Sobre o prazo, assinale:", "B"),
  question("i1", "Maria tomou posse e não entrou em exercício no prazo legal. A consequência é:", "C"),
  question("i2", "Assinale a frase em que o acento indicativo de crase está empregado corretamente:", "D", "FACIL"),
]);

Deno.test("input requires items for a normal simulado and rematch sources for a rematch", () => {
  const input = parseSimulationJobInput(baseInput);
  assertEquals(input.items.map((item) => item.ref), ["i1", "i2"]);
  assertThrows(() => parseSimulationJobInput({ ...baseInput, items: [] }));
  assertThrows(() => parseSimulationJobInput({ ...baseInput, mode: "REMATCH" }));
  const rematch = parseSimulationJobInput({
    ...baseInput,
    mode: "REMATCH",
    items: [],
    rematch: [{ statement: "Enunciado original da questão errada", options: [option("A", true), option("B", false)] }],
  });
  assertEquals(rematch.rematch[0].ref, "r1");
});

Deno.test("input caps a part at 30 questions", () => {
  const items = Array.from({ length: 4 }, () => ({ subjectName: "X", topicPath: ["Y"], count: 10, difficulty: "MEDIA" }));
  assertThrows(() => parseSimulationJobInput({ ...baseInput, items }));
});

Deno.test("prompt carries the board guide, weak spots and what to avoid", () => {
  const prompt = simulationUserPrompt(parseSimulationJobInput(baseInput));
  assertStringIncludes(prompt, "ESTILO DA BANCA (FCC)");
  assertStringIncludes(prompt, "literalidade");
  assertStringIncludes(prompt, "0: confunde posse com exercício");
  assertStringIncludes(prompt, "JÁ EXISTEM");
  assertEquals(boardGuide("CEBRASPE (antigo Cespe/UnB)").name, "Cebraspe");
  assertEquals(boardGuide(null).name, null);
});

Deno.test("valid part passes", () => {
  const input = parseSimulationJobInput(baseInput);
  const value = validateSimulation(JSON.stringify(good()), expected, input);
  assertEquals((value.questions as unknown[]).length, 3);
});

Deno.test("rejects wrong counts, copies, two correct and wrong difficulty", () => {
  const input = parseSimulationJobInput(baseInput);
  const broken = (mutate: (value: ReturnType<typeof good>) => void) => {
    const value = good();
    mutate(value);
    return () => validateSimulation(JSON.stringify(value), expected, input);
  };
  assertThrows(broken((value) => value.questions.pop()));
  assertThrows(broken((value) => { value.questions[2].statement = "Qual o prazo para a posse do servidor nomeado?"; }));
  assertThrows(broken((value) => { value.questions[0].options[0].correct = true; }));
  assertThrows(broken((value) => { value.questions[0].difficulty = "FACIL"; }));
  assertThrows(broken((value) => { (value.questions[0] as { targetsWeakSpot: unknown }).targetsWeakSpot = 3; }));
});

Deno.test("detected board without a source is dropped, not trusted", () => {
  const input = parseSimulationJobInput(baseInput);
  const value = good() as Record<string, unknown>;
  value.detectedBoard = { name: "FGV", sourceUrl: null };
  const result = validateSimulation(value, expected, input);
  assertEquals((result.detectedBoard as { name: unknown }).name, null);
});

Deno.test("review fixes the key; dropping past the 20% shortfall keeps the question instead", () => {
  const input = parseSimulationJobInput(baseInput);
  const part = validateSimulation(good(), expected, input);
  const reviewed = withSimulationReview(part, { fixes: [], answerFixes: [{ question: 0, correctKey: "A", reason: "" }], removeQuestions: [2], removeFlashcards: [] }, expected, input);
  const questions = reviewed.questions as { options: { correct: boolean }[] }[];
  assertEquals(questions.length, 3);
  assert(questions[0].options[0].correct);
  const skipped = withSimulationReview(part, null, expected, input);
  assertEquals((skipped.warnings as { code: string }[])[0].code, "SOURCE_NOT_VERIFIED");
});
