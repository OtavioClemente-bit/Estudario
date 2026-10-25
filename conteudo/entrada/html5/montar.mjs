// node conteudo/entrada/html5/montar.mjs — grava conteudo/entrada/informatica.html5.json e conteudo/materias/informatica/html5.json
import { writeFileSync } from "node:fs";
import * as c from "./conteudo.mjs";
const m = {
  ...c.materia, version: 1, status: "PUBLISHED",
  chapters: c.chapters.map(([title, markdown]) => ({ title, markdown })),
  summary: c.summary,
  flashcards: c.flashcards.map(([front, back]) => ({ front, back })),
  tips: c.tips, traps: c.traps,
  activeRecall: c.activeRecall.map(([question, answer]) => ({ question, answer })),
  errorConcepts: c.errorConcepts.map(([key, title, summary]) => ({ key, title, summary })),
  questions: c.questoes.map(([statement, difficulty, section, errorConceptKey, opts, explanation]) => ({
    statement, format: opts.length === 2 ? "TRUE_FALSE" : "MULTIPLE_CHOICE", difficulty,
    options: opts.map(([text, correct], i) => ({ key: opts.length === 2 ? (text === "Certo" ? "C" : "E") : "ABCDE"[i], text, correct })),
    explanation, section, errorConceptKey, sourceType: "AUTHORIAL", board: null, agency: null, year: null, sourceUrl: null,
  })),
  sources: c.sources.map(([kind, title, publisher, reference, url]) => ({ kind, title, publisher, reference, url, accessedAt: "2026-10-25" })),
};
const json = JSON.stringify(m, null, 2) + "\n";
writeFileSync("conteudo/entrada/informatica.html5.json", json);
writeFileSync("conteudo/materias/informatica/html5.json", json);
console.log("ok", m.questions.length, "questões;", ["FACIL", "MEDIA", "DIFICIL"].map((d) => m.questions.filter((q) => q.difficulty === d).length).join("/"));
