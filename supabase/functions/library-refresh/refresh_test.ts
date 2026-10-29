import { assert, assertEquals } from "https://deno.land/std@0.224.0/assert/mod.ts";
import { itemsOf, type LookupRow, matchQuestions, type RefreshDeps, refreshItem, stampOf } from "./index.ts";

// Uma matéria real da biblioteca, para a montagem ser a mesma do gerador.
const material = JSON.parse(await Deno.readTextFile(new URL("../../../conteudo/materias/portugues/crase.json", import.meta.url))) as Record<string, unknown>;
const questions = material.questions as Array<Record<string, unknown>>;
const chapters = material.chapters as Array<Record<string, unknown>>;

function deps(row: Partial<LookupRow> = {}) {
  const fetched: string[] = [];
  const d: RefreshDeps = {
    lookup: async () => ({ topic_id: "portugues.crase", version: 4, material_path: "topics/portugues.crase/v4-aaa.json", note_path: null, material: null, note: null, focus: null, ...row }),
    generalNote: async () => ({ note: null, path: null }),
    file: async (path) => { fetched.push(String(path)); return path?.startsWith("topics/") ? material : null; },
  };
  return { d, fetched };
}

const base = itemsOf({ items: [{ id: 7, subjectName: "Língua Portuguesa", topicPath: ["Língua Portuguesa", "Crase"], board: null, stamp: null, blocks: ["THEORY", "SUMMARY", "QUESTIONS"], chapterTitles: chapters.slice(0, 3).map((c) => c.title), questions: [] }] })[0];

Deno.test("mesma versão: responde igual sem baixar nada", async () => {
  const { d, fetched } = deps();
  const stamp = stampOf({ topic_id: "portugues.crase", version: 4, material_path: "topics/portugues.crase/v4-aaa.json" }, null);
  const result = await refreshItem({ ...base, stamp }, d, { left: 5 });
  assertEquals(result.status, "same");
  assertEquals(fetched.length, 0);
});

Deno.test("versão nova: devolve o texto montado e as questões da pessoa corrigidas", async () => {
  const { d } = deps();
  const mine = [String(questions[0].statement), "Uma questão que não é da biblioteca e não deve casar com nada daqui.", String(questions[43].statement).slice(0, 120) + " (texto do fim mudou na correção)"];
  const result = await refreshItem({ ...base, stamp: "3.antigo", questions: mine }, d, { left: 5 });
  assertEquals(result.status, "update");
  if (result.status !== "update") return;
  assert((result.content.chapters as unknown[]).length > 0);
  assert(String(result.content.summary).length > 0);
  assertEquals(result.content.flashcards, []); // não tinha pedido revisão rápida
  assertEquals(result.questions.map((q) => q.index), [0, 2]); // a 3ª (índice 2) casou pelo começo
  assertEquals(result.questions[0].question.statement !== undefined, true);
  assertEquals(result.libraryTopic, "portugues.crase");
});

Deno.test("matéria antiga só é adotada se os capítulos batem com os da biblioteca", async () => {
  const { d } = deps();
  const adopted = await refreshItem(base, d, { left: 5 });
  assertEquals(adopted.status, "update");
  const fromAi = await refreshItem({ ...base, chapterTitles: ["Introdução geral", "Casos especiais do acento grave", "Exercícios"] }, d, { left: 5 });
  assertEquals(fromAi.status, "skip");
});

Deno.test("sem matéria na biblioteca ou sem orçamento de montagem", async () => {
  const none: RefreshDeps = { lookup: async () => null, generalNote: async () => ({ note: null, path: null }), file: async () => null };
  assertEquals((await refreshItem(base, none, { left: 5 })).status, "none");
  const { d } = deps();
  assertEquals((await refreshItem({ ...base, stamp: "velho" }, d, { left: 0 })).status, "later");
});

Deno.test("casamento de questões: enunciado igual, com assertivas quebradas ou pelo começo", () => {
  const pool = [{ statement: "Julgue os itens a seguir sobre o uso do acento grave indicativo de crase em expressões adverbiais femininas." }];
  assertEquals(matchQuestions(["Julgue os itens a seguir sobre o uso do acento grave indicativo de crase em expressões adverbiais femininas."], pool).length, 1);
  assertEquals(matchQuestions(["Julgue os itens a seguir sobre o uso do acento grave indicativo de crase em expressões adverbiais (versão corrigida)."], pool).length, 1);
  assertEquals(matchQuestions(["Outra coisa totalmente diferente sobre regência verbal e nominal no texto."], pool).length, 0);
});
