import { assert, assertEquals } from "https://deno.land/std@0.224.0/assert/mod.ts";
import { repairTopicContent } from "./content-repair.ts";
import type { ContentGenerationOptions } from "./text-job-input.ts";

const options = { blocks: ["THEORY", "QUESTIONS"], depth: "DEEP", questionCount: 2, questionStyle: "FIVE_OPTIONS", difficulty: "HARD" } as unknown as ContentGenerationOptions;
const option = (key: string, correct = false) => ({ key, text: `opção ${key}`, correct });
const question = (statement: string, extra: Record<string, unknown> = {}) => ({
  statement, explanation: "Porque.", section: "1. Função quadrática", errorConceptKey: "e1", difficulty: "DIFICIL", format: "MULTIPLE_CHOICE",
  sourceType: "AUTHORIAL", sourceUrl: null, board: null, agency: null, year: null,
  options: [option("A", true), option("B"), option("C"), option("D"), option("E")], ...extra,
});

Deno.test("repair fixes what is safe and drops only the broken question", () => {
  const raw = JSON.stringify({
    chapters: [{ title: "1. Função quadrática: raízes e vértice", markdown: "x" }, { title: "2. Gráfico", markdown: "y" }],
    errorConcepts: [{ key: "e1" }],
    questions: [
      question("Q1", { section: "Função quadrática", board: "FCC", year: 2022 }),
      question("Q2", { errorConceptKey: "e9", difficulty: "MEDIA" }),
      question("Q3", { options: [option("A", true), option("B", true), option("C"), option("D"), option("E")] }),
      question("Q1"),
    ],
    sources: [{ title: "Fonte", url: "https://a.gov.br", accessedAt: "2026-10-02T10:00:00Z" }, { title: "Sem URL", url: "", accessedAt: "2026-10-02" }],
  });
  const out = JSON.parse(repairTopicContent(raw, options, 2));
  assertEquals(out.questions.map((q: { statement: string }) => q.statement), ["Q1", "Q2"]);
  assertEquals(out.questions[0].section, "1. Função quadrática: raízes e vértice");
  assertEquals([out.questions[0].board, out.questions[0].year], [null, null]);
  assertEquals(out.questions[1].errorConceptKey, null);
  assertEquals(out.questions[1].difficulty, "DIFICIL");
  assertEquals(out.sources, [{ title: "Fonte", url: "https://a.gov.br", accessedAt: "2026-10-02" }]);
});

Deno.test("repair leaves invalid JSON untouched", () => {
  assertEquals(repairTopicContent("{oops", options, 2), "{oops");
});

Deno.test("question whose explanation admits it needs fixing is dropped", () => {
  const raw = JSON.stringify({
    chapters: [{ title: "1. Função quadrática", markdown: "x" }, { title: "2. Gráfico", markdown: "y" }],
    errorConcepts: [{ key: "e1" }],
    questions: [question("Q1"), question("Q10", { explanation: "F = 240 N. Portanto, com a correção da alternativa A para 240 N, ela é a única correta." })],
    sources: [],
  });
  const out = JSON.parse(repairTopicContent(raw, options, 2));
  assertEquals(out.questions.map((q: { statement: string }) => q.statement), ["Q1"]);
});

Deno.test("figure block labelled with its type is renamed so the app draws it", () => {
  const raw = JSON.stringify({
    chapters: [{ title: "1. Áreas", markdown: "Veja:\n\n```geometria\n{\"tipo\":\"geometria\"}\n```\n" }, { title: "2. Volume", markdown: "y" }],
    questions: [],
    sources: [],
  });
  const out = JSON.parse(repairTopicContent(raw, options, 2));
  assertEquals(out.chapters[0].markdown, "Veja:\n\n```grafico\n{\"tipo\":\"geometria\"}\n```\n");
});

Deno.test("numeric options with different units are dropped (the unit gives the answer away)", () => {
  const units = (texts: string[]) => ({ options: texts.map((t, i) => ({ key: "ABCDE"[i], text: t, correct: i === 1 })) });
  const raw = JSON.stringify({
    chapters: [{ title: "1. Função quadrática", markdown: "x" }, { title: "2. Gráfico", markdown: "y" }],
    errorConcepts: [{ key: "e1" }],
    questions: [
      question("Perímetro?", units(["24 m", "26 m", "36 m²", "13 m", "52 m²"])),
      question("Área?", units(["14 m²", "28 m²", "42 m²", "49 m²", "56 m²"])),
    ],
    sources: [],
  });
  const out = JSON.parse(repairTopicContent(raw, options, 2));
  assertEquals(out.questions.map((q: { statement: string }) => q.statement), ["Área?"]);
});

Deno.test("answer keys stuck on A are spread out and the explanation letters follow", async () => {
  const { balanceAnswerKeys } = await import("./content-repair.ts");
  const make = (n: number) => ({
    statement: `Q${n}`, format: "MULTIPLE_CHOICE",
    options: ["A", "B", "C", "D", "E"].map((key) => ({ key, text: key === "A" ? `certa ${n}` : `errada ${key}${n}`, correct: key === "A" })),
    explanation: "A tabela moderna segue Z. A combina corretamente período e grupo. B confunde grupo; C troca o período; D e E erram o bloco. Gabarito: A.",
  });
  const out = balanceAnswerKeys(Array.from({ length: 10 }, (_, i) => make(i)));
  const keys = out.map((q) => (q.options as { key: string; correct: boolean }[]).find((o) => o.correct)!.key);
  assert(Math.max(...["A", "B", "C", "D", "E"].map((k) => keys.filter((x) => x === k).length)) <= 3);
  const moved = out.find((q) => (q.options as { key: string; correct: boolean }[]).find((o) => o.correct)!.key !== "A")!;
  const to = (moved.options as { key: string; correct: boolean; text: string }[]).find((o) => o.correct)!;
  assert(to.text.startsWith("certa"));
  const explanation = String(moved.explanation);
  assert(explanation.startsWith("A tabela moderna"), "o artigo A não muda");
  assert(explanation.includes(`${to.key} combina corretamente`));
  assert(explanation.endsWith(`Gabarito: ${to.key}.`));
});
