const materia = JSON.parse(await Deno.readTextFile(
  "conteudo/entrada/reescrita-codex/maranhao.conhecimentos-estaduais.json",
));
for (let i = 0; i < materia.chapters.length; i++) {
  await Deno.writeTextFile(
    `conteudo/entrada/reescrita-codex/capitulo-${i + 1}.md`,
    materia.chapters[i].markdown,
  );
  console.log(i + 1, materia.chapters[i].markdown.length);
}
