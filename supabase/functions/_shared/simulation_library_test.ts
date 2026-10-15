import { assert, assertEquals } from "https://deno.land/std@0.224.0/assert/mod.ts";
import { planSimulationFromLibrary } from "./simulation-library.ts";
import { parseSimulationJobInput, questionKey, validateSimulation } from "./simulation.ts";

type Json = Record<string, unknown>;

const levels = ["FACIL", "MEDIA", "DIFICIL"];
function material(name: string, size: number): Json {
  return {
    chapters: [{ title: "1. Tudo", markdown: "texto" }],
    errorConcepts: [{ key: "e1", title: `Confusão de ${name}`, summary: "troca a regra pela exceção" }],
    questions: Array.from({ length: size }, (_, i) => ({
      statement: `Questão ${i + 1} sobre ${name}: qual é a regra aplicável neste caso concreto?`,
      format: "MULTIPLE_CHOICE",
      difficulty: levels[i % 3],
      section: "1. Tudo",
      errorConceptKey: i % 4 === 0 ? "e1" : null,
      explanation: `Gabarito ${"ABCDE"[i % 5]}. Explicação longa o bastante da questão ${i + 1}.`,
      options: ["A", "B", "C", "D", "E"].map((key, k) => ({ key, text: `Alternativa ${key} da questão ${i + 1}`, correct: k === i % 5 })),
    })),
  };
}

const library: Record<string, Json> = { crase: material("crase", 30), licitacao: material("licitação", 30) };
const store = {
  libraryLookup: (aliases: string[]) => {
    const name = aliases.find((alias) => library[alias]);
    return Promise.resolve(name ? { topicId: name, material: library[name], note: null, focus: null } : null);
  },
};
const expected = { schemaVersion: 1, promptVersion: "simulation-v1", modelVersion: "biblioteca" };
const item = (topic: string, count: number, extra: Json = {}) => ({ subjectName: "Matéria", topicPath: [topic], count, difficulty: "MISTA", ...extra });
const partOf = (questions: Json[]) => ({ ...expected, detectedBoard: { name: null, sourceUrl: null }, questions, sources: [], warnings: [] });

Deno.test("simulado sai inteiro da biblioteca, sem repetir o que a pessoa já viu", async () => {
  const seen = (library.crase.questions as Json[]).slice(0, 10).map((q) => questionKey(String(q.statement)));
  const input = parseSimulationJobInput({ competitionName: "TJ", mode: "FULL", partCount: 1, partIndex: 0, items: [item("crase", 10), item("licitacao", 10)], seed: "s1", seen });
  const plan = await planSimulationFromLibrary(input, store, "job");
  assertEquals(plan.remaining, null);
  assertEquals(plan.questions.length, 20);
  assert(plan.questions.every((q) => !seen.includes(questionKey(String(q.statement)))));
  validateSimulation(partOf(plan.questions), expected, plan.delivered);
  // Dificuldade misturada, como numa prova.
  assertEquals(new Set(plan.questions.map((q) => q.difficulty)).size, 3);
});

Deno.test("tópico sem biblioteca vai para a IA; no diagnóstico, passa para os tópicos cobertos", async () => {
  const items = [item("crase", 5), item("topico sem materia", 5)];
  const full = await planSimulationFromLibrary(parseSimulationJobInput({ competitionName: "TJ", mode: "FULL", partCount: 1, partIndex: 0, items, seed: "s" }), store, "job");
  assertEquals(full.remaining?.items.map((i) => [i.ref, i.count]), [["i2", 5]]);
  const diagnostic = await planSimulationFromLibrary(parseSimulationJobInput({ competitionName: "TJ", mode: "DIAGNOSTIC", partCount: 1, partIndex: 0, items, seed: "s" }), store, "job");
  assertEquals(diagnostic.remaining, null);
  assertEquals(diagnostic.questions.length, 10);
  validateSimulation(partOf(diagnostic.questions), expected, diagnostic.delivered);
});

Deno.test("tópico dividido entre duas partes não repete questão", async () => {
  const plan = (chunk: number) =>
    planSimulationFromLibrary(parseSimulationJobInput({ competitionName: "TJ", mode: "FULL", partCount: 2, partIndex: chunk, items: [item("crase", 8, { chunk, chunks: 2 })], seed: "s" }), store, `job${chunk}`);
  const [a, b] = await Promise.all([plan(0), plan(1)]);
  const keys = new Set(a.questions.map((q) => questionKey(String(q.statement))));
  assertEquals(b.questions.filter((q) => keys.has(questionKey(String(q.statement)))).length, 0);
  assertEquals(a.questions.length + b.questions.length, 16);
});

Deno.test("pontos fracos puxam questões do conceito e marcam targetsWeakSpot", async () => {
  const input = parseSimulationJobInput({ competitionName: "TJ", mode: "STUDIED", partCount: 1, partIndex: 0, items: [item("crase", 8)], seed: "s", weakSpots: ["Confusão de crase: troca a regra"] });
  const plan = await planSimulationFromLibrary(input, store, "job");
  // Cerca de 1 em 4 de propósito (2 de 8); outras podem cair no mesmo conceito por acaso.
  assert(plan.questions.filter((q) => q.targetsWeakSpot === 0).length >= 2);
});

Deno.test("revanche sai da biblioteca com outra questão do mesmo conceito", async () => {
  const original = (library.crase.questions as Json[])[0];
  const input = parseSimulationJobInput({
    competitionName: "TJ", mode: "REMATCH", partCount: 1, partIndex: 0, items: [], seed: "s",
    rematch: [{ statement: original.statement, options: original.options, subjectName: "Português", topicPath: ["crase"] }],
  });
  const plan = await planSimulationFromLibrary(input, store, "job");
  assertEquals(plan.remaining, null);
  assertEquals(plan.questions.length, 1);
  const variant = (library.crase.questions as Json[]).find((q) => q.statement === plan.questions[0].statement)!;
  assertEquals(variant.errorConceptKey, "e1");
  assert(variant !== original);
});

Deno.test("tópico sem questão inédita vai para a IA com modelos da biblioteca (questões irmãs, sem pesquisa)", async () => {
  const seen = (library.crase.questions as Json[]).map((q) => questionKey(String(q.statement)));
  const input = parseSimulationJobInput({ competitionName: "TJ", mode: "FULL", partCount: 1, partIndex: 0, items: [item("crase", 4)], seed: "s", seen });
  const plan = await planSimulationFromLibrary(input, store, "job");
  assertEquals(plan.questions.length, 0);
  const left = plan.remaining!.items[0];
  assertEquals(left.count, 4);
  assertEquals(left.bases?.length, 2);
  assert(left.bases!.every((base) => base.statement.includes("crase") && base.answer.length > 0));
});
