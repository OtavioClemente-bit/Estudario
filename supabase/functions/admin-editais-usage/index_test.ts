import { assertEquals } from "jsr:@std/assert@1";
import { aggregate, type CatalogRow, editaisOf, matchCatalog } from "./index.ts";

const row = (id: string, role: string, subjects: string[]): CatalogRow => ({
  id, short_name: "TRT 3", role, agency: "TRT da 3ª Região", board: "FCC", year: 2026, topic_count: 100, ready_topic_count: 80,
  subjects: subjects.map((name) => ({ name })),
});
const catalog = [
  row("trt3-tec", "Técnico Judiciário", ["Português", "Direito do Trabalho"]),
  row("trt3-ti", "Analista de TI", ["Português", "Banco de Dados", "Redes"]),
];

const snapshot = (name: string, subjects: string[], primary = true) => ({
  competitions: [{ id: 1, name, primary }],
  subjects: subjects.map((s, i) => ({ id: i + 10, competitionId: 1, name: s })),
  topics: [{ id: 1, subjectId: 10, status: "ESTUDADO" }, { id: 2, subjectId: 10, status: "NAO_ESTUDADO" }],
});

Deno.test("lê os concursos da cópia da nuvem", () => {
  const [e] = editaisOf(snapshot("TRT 3", ["Português", "Redes"]));
  assertEquals(e, { name: "TRT 3", primary: true, subjects: ["Português", "Redes"], topics: 2, studied: 1 });
});

Deno.test("casa o cargo pelas matérias em comum", () => {
  assertEquals(matchCatalog(editaisOf(snapshot("TRT 3", ["Banco de Dados", "Redes"]))[0], catalog)?.id, "trt3-ti");
  assertEquals(matchCatalog(editaisOf(snapshot("TRT 3 · Técnico Judiciário", []))[0], catalog)?.id, "trt3-tec");
  assertEquals(matchCatalog(editaisOf(snapshot("Meu concurso", ["Português"]))[0], catalog), null);
});

Deno.test("conta pessoas por edital, sem repetir a mesma conta", () => {
  const ti = editaisOf(snapshot("TRT 3", ["Banco de Dados"]));
  const result = aggregate([
    { editais: [...ti, ...ti], paid: true, updatedAt: "2026-10-24T10:00:00Z" },
    { editais: ti, paid: false, updatedAt: "2026-10-23T10:00:00Z" },
    { editais: [], paid: false, updatedAt: null },
  ], catalog);
  assertEquals(result.totals, { accounts: 3, withEditais: 2, editais: 1, fromCatalog: 1 });
  assertEquals(result.items[0].users, 2);
  assertEquals(result.items[0].paidUsers, 1);
  assertEquals(result.items[0].readyPercent, 80);
  assertEquals(result.items[0].lastActive, "2026-10-24T10:00:00Z");
});

Deno.test("resumo da conta: editais, matérias geradas, questões e último estudo", async () => {
  const { summaryOf } = await import("./index.ts");
  const s = summaryOf({
    competitions: [{ id: 1, name: "PMMG", primary: true }],
    subjects: [{ id: 10, competitionId: 1, name: "Português" }],
    topics: [{ id: 100, subjectId: 10, title: "Crase", status: "ESTUDADO" }, { id: 101, subjectId: 10, title: "Vírgula", status: "NAO_ESTUDADO" }],
    theories: [{ id: 1, topicId: 100 }],
    questions: [{ id: 1, topicId: 100, answerCount: 2 }, { id: 2, topicId: 100, answerCount: 0 }, { id: 3, topicId: 100, hidden: true }],
    errors: [{ id: 1, status: "NOVO" }, { id: 2, status: "CORRIGIDO" }],
    attempts: [{ answeredAt: 1000 }, { answeredAt: 5000 }],
  });
  assertEquals(s.materials, [{ topic: "Crase", subject: "Português", competition: "PMMG", questions: 2 }]);
  assertEquals([s.questions, s.answered, s.errors, s.lastStudyAt], [2, 1, 1, 5000]);
  assertEquals(s.editais[0].studied, 1);
});
