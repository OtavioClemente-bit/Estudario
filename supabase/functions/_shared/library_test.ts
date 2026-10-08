import { assert, assertEquals, assertThrows } from "jsr:@std/assert@1";
import {
  assembleFromLibrary,
  LibraryValidationError,
  lookupAliases,
  normalizeAlias,
  normalizeBoard,
  validateBoardNote,
  validateLibraryMaterial,
} from "./library.ts";
import type { ContentGenerationOptions } from "./text-job-input.ts";

type Json = Record<string, unknown>;
const long = (seed: string, size: number) => `${seed}. `.repeat(Math.ceil(size / (seed.length + 2)));

function question(index: number, format: "TF" | "MC5" | "MC4", difficulty: string): Json {
  const keys = format === "TF" ? ["C", "E"] : format === "MC4" ? ["A", "B", "C", "D"] : ["A", "B", "C", "D", "E"];
  const right = Math.floor(index / 4) % keys.length;
  return {
    statement: `Questão ${index} sobre a lei de formação da função afim e seu coeficiente angular.`,
    format: format === "TF" ? "TRUE_FALSE" : "MULTIPLE_CHOICE",
    difficulty,
    options: keys.map((key, i) => ({ key, text: format === "TF" ? (key === "C" ? "Certo" : "Errado") : `Alternativa ${key} ${index}`, correct: i === right })),
    explanation: long(`Explicação detalhada da questão ${index} com o raciocínio passo a passo`, 130),
    section: index % 2 === 0 ? "Definição" : "Gráfico",
    errorConceptKey: index % 3 === 0 ? "e1" : null,
    sourceType: "AUTHORIAL",
    board: null,
    agency: null,
    year: null,
    sourceUrl: null,
  };
}

function material(): Json {
  const formats = ["TF", "MC5", "MC5", "MC4"] as const;
  const levels = ["FACIL", "MEDIA", "DIFICIL"];
  return {
    id: "matematica.funcao-1-grau",
    subject: "Matemática",
    title: "Função do 1º grau",
    version: 1,
    status: "PUBLISHED",
    aliases: ["Função afim", "Funções de 1º grau"],
    scope: { covers: "Definição, gráfico, coeficientes, raiz e estudo do sinal.", excludes: "" },
    theoryTitle: "Função do 1º grau",
    chapters: [
      { title: "Definição", markdown: long("A função afim tem a forma f(x) = ax + b com a diferente de zero", 3_200) },
      { title: "Gráfico", markdown: long("O gráfico é uma reta cuja inclinação depende do coeficiente a", 3_200) },
    ],
    summary: long("Resumo da função afim com coeficientes, raiz e sinal", 450),
    flashcards: Array.from({ length: 12 }, (_, i) => ({ front: `Pergunta ${i}`, back: `Resposta ${i}` })),
    tips: ["Dica um sobre coeficientes", "Dica dois sobre a raiz", "Dica três sobre o sinal"],
    traps: ["Pegadinha um sobre a = 0", "Pegadinha dois sobre b", "Pegadinha três sobre o gráfico"],
    activeRecall: Array.from({ length: 5 }, (_, i) => ({ question: `Recorde ${i}?`, answer: `Resposta ${i}` })),
    errorConcepts: [
      { key: "e1", title: "Coeficiente angular", summary: "Confundir a inclinação com o ponto de corte." },
      { key: "e2", title: "Raiz", summary: "Esquecer que a raiz é -b/a e trocar o sinal." },
    ],
    questions: Array.from({ length: 64 }, (_, i) => question(i, formats[i % 4], levels[i % 3])),
    sources: [{ kind: "COMPLEMENTAR", title: "BNCC Matemática", publisher: "MEC", reference: "", url: "https://basenacionalcomum.mec.gov.br", accessedAt: "2026-10-03" }],
  };
}

const ALL: ContentGenerationOptions = {
  blocks: ["THEORY", "SUMMARY", "QUICK_REVIEW", "TIPS_TRAPS", "ACTIVE_RECALL", "QUESTIONS", "ERROR_CONCEPTS"],
  depth: "BOOK",
  questionCount: 10,
  questionStyle: "MIXED",
  difficulty: "MIXED",
};

Deno.test("normaliza apelidos e bancas", () => {
  assertEquals(normalizeAlias("1.2 Função do 1º grau."), "funcao do 1 grau");
  assertEquals(normalizeBoard("CESPE/UnB"), "cebraspe");
  assertEquals(lookupAliases({ topicPath: ["Funções", "1.2 Função afim"] }), ["funcoes funcao afim", "funcao afim"]);
});

Deno.test("matéria completa passa e monta material aceito em todos os estilos", () => {
  const m = validateLibraryMaterial(material());
  for (const questionStyle of ["MIXED", "FIVE_OPTIONS", "FOUR_OPTIONS", "TRUE_FALSE"] as const) {
    const content = assembleFromLibrary(m, null, { board: null, options: { ...ALL, questionStyle }, avoidStatements: [] }, "s");
    assert(content, questionStyle);
    assertEquals((content.questions as unknown[]).length, 10);
  }
});

Deno.test("matéria rasa ou genérica é recusada com os motivos", () => {
  const m = material();
  m.questions = (m.questions as Json[]).slice(0, 10);
  (m.chapters as Json[])[0].markdown = "TODO: escrever";
  const error = assertThrows(() => validateLibraryMaterial(m), LibraryValidationError);
  assert(error.problems.some((p) => p.includes("questions")));
  assert(error.problems.some((p) => p.includes("rascunho")));
});

Deno.test("só as partes pedidas; sem questões suficientes devolve nulo", () => {
  const m = material();
  const only = assembleFromLibrary(m, null, { board: null, options: { ...ALL, blocks: ["QUESTIONS"], questionCount: 5 }, avoidStatements: [] }, "s")!;
  assertEquals(only.chapters, []);
  assertEquals(only.flashcards, []);
  assertEquals(assembleFromLibrary(m, null, { board: null, options: { ...ALL, questionStyle: "FOUR_OPTIONS", difficulty: "HARD", questionCount: 30 }, avoidStatements: [] }, "s"), null);
});

Deno.test("foco: questões do tópico primeiro, depois as do capítulo; teoria na ordem original", () => {
  const m = validateLibraryMaterial(material());
  // q2, q6 e q10 (posições 1, 5 e 9: cinco alternativas, seção "Gráfico"); capítulo 1 = "Gráfico".
  const content = assembleFromLibrary(m, null, { board: null, options: { ...ALL, questionCount: 10 }, avoidStatements: [] }, "s", { capitulos: [1], questoes: ["q2", "q6", "q10"] })!;
  const statements = (content.questions as Json[]).map((q) => String(q.statement));
  const focused = [1, 5, 9].map((i) => `Questão ${i} sobre`);
  assert(statements.slice(0, 3).every((s) => focused.some((f) => s.startsWith(f))), statements.slice(0, 3).join(" | "));
  assert(statements.slice(3).every((s) => Number(s.match(/Questão (\d+)/)![1]) % 2 === 1), "o resto vem do capítulo Gráfico");
  assertEquals((content.chapters as Json[])[0].title, "Definição"); // teoria na ordem original
  assertEquals((content.chapters as Json[]).length, 2);
  // Sem foco, nada muda.
  const plain = assembleFromLibrary(m, null, { board: null, options: ALL, avoidStatements: [] }, "s", null)!;
  assertEquals((plain.chapters as Json[])[0].title, "Definição");
});

Deno.test("mais questões não repete as que a pessoa já tem", () => {
  const m = material();
  const first = assembleFromLibrary(m, null, { board: null, options: ALL, avoidStatements: [] }, "a")!;
  const seen = (first.questions as Json[]).map((q) => String(q.statement));
  const more = assembleFromLibrary(m, null, { board: null, options: ALL, avoidStatements: seen }, "b")!;
  assert((more.questions as Json[]).every((q) => !seen.includes(String(q.statement))));
});

Deno.test("recorte da banca entra como capítulo, dicas e questões primeiro", () => {
  const m = material();
  const note = validateBoardNote({
    topic: m.id,
    board: "VUNESP",
    role: "Soldado PM",
    version: 1,
    incidence: "ALTA",
    howItFalls: long("A VUNESP cobra interpretação de gráfico e problemas com tabela de preços", 400),
    tips: ["Dica da VUNESP sobre gráficos"],
    traps: [],
    questions: [{ ...question(900, "MC5", "MEDIA"), statement: "Questão no estilo VUNESP sobre tarifa de táxi como função afim." }],
  }, m);
  const content = assembleFromLibrary(m, note, { board: "Vunesp", options: ALL, avoidStatements: [] }, "s")!;
  assertEquals((content.chapters as Json[]).at(-1)!.title, "Como a VUNESP cobra este tópico");
  assertEquals((content.tips as string[])[0], "Dica da VUNESP sobre gráficos");
  assertEquals((content.questions as Json[])[0].statement, "Questão no estilo VUNESP sobre tarifa de táxi como função afim.");
});

Deno.test("dificuldade pedida sem questões suficientes completa com o nível mais próximo", () => {
  const m = material();
  const five = (m.questions as Json[]).filter((q) => (q.options as Json[]).length === 5);
  const hard = five.filter((q) => q.difficulty === "DIFICIL").length;
  const medium = five.filter((q) => q.difficulty === "MEDIA").length;
  const count = hard + medium;
  const out = assembleFromLibrary(m, null, { board: null, options: { ...ALL, blocks: ["QUESTIONS"], questionStyle: "FIVE_OPTIONS", difficulty: "HARD", questionCount: count }, avoidStatements: [] }, "s")!;  const picked = (out.questions as Json[]).map((q) => String(q.statement));
  const hardStatements = new Set(five.filter((q) => q.difficulty === "DIFICIL").map((q) => String(q.statement)));
  assert(hard < count, "o teste precisa de menos difíceis do que o pedido");
  assertEquals(picked.length, count);
  assertEquals(picked.slice(0, hard).every((s) => hardStatements.has(s)), true);
  assertEquals(picked.slice(hard).some((s) => hardStatements.has(s)), false);
  assertEquals((out.questions as Json[]).every((q) => q.difficulty === "DIFICIL"), true);
});
