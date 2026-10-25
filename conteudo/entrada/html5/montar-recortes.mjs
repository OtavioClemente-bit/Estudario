// node conteudo/entrada/html5/montar.mjs — grava conteudo/recortes/<banca>/informatica.html5.json
import { mkdirSync, writeFileSync } from "node:fs";
import { recortes } from "./recortes.mjs";
for (const r of recortes) {
  const note = {
    topic: "informatica.html5", board: r.board, role: null, version: 1, incidence: r.incidence,
    howItFalls: r.howItFalls, tips: r.tips, traps: r.traps,
    questions: r.questoes.map(([statement, difficulty, section, errorConceptKey, opts, explanation]) => ({
      topico: "HTML5", estilo: r.estilo, statement,
      format: opts.length === 2 ? "TRUE_FALSE" : "MULTIPLE_CHOICE", difficulty,
      options: opts.map(([text, correct, comentario], i) => ({ key: opts.length === 2 ? (text === "Certo" ? "C" : "E") : "ABCDE"[i], text, correct, ...(comentario ? { comentario } : {}) })),
      explanation, section, errorConceptKey, sourceType: "AUTHORIAL", sourceUrl: null, board: null, agency: null, year: null,
    })),
  };
  mkdirSync(`conteudo/recortes/${r.pasta}`, { recursive: true });
  writeFileSync(`conteudo/recortes/${r.pasta}/informatica.html5.json`, JSON.stringify(note, null, 2) + "\n");
  console.log(r.pasta, note.questions.length, "questões");
}
