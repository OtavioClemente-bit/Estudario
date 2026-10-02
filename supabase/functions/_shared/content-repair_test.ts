import { assertEquals } from "https://deno.land/std@0.224.0/assert/mod.ts";
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
