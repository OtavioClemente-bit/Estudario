const path = "conteudo/entrada/reescrita-lig3/processo-civil.tutela-provisoria.json";
const materia = JSON.parse(await Deno.readTextFile(path));
for (let i = 0; i < 5; i++) {
  materia.chapters[i].markdown = await Deno.readTextFile(`conteudo/entrada/.tmp-reescrita/c${i + 1}.md`);
}
materia.version++;
await Deno.writeTextFile(path, JSON.stringify(materia, null, 2));
