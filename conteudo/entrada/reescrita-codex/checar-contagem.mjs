const materia = JSON.parse(await Deno.readTextFile(
  "conteudo/entrada/reescrita-codex/maranhao.conhecimentos-estaduais.json",
));
console.log({
  version: materia.version,
  chapters: materia.chapters.length,
  questions: materia.questions.length,
  theoryCharacters: materia.chapters.reduce((sum, chapter) => sum + chapter.markdown.length, 0),
});
