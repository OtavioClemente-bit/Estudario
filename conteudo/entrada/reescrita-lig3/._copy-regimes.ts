const target = "conteudo/entrada/reescrita-lig3/direito-tributario.regimes-especiais.json";
const chapterFiles = [
  "conteudo/entrada/reescrita-lig3/._regimes-c1.md",
  "conteudo/entrada/reescrita-lig3/._regimes-c2.md",
  "conteudo/entrada/reescrita-lig3/._regimes-c3.md",
  "conteudo/entrada/reescrita-lig3/._regimes-c4.md",
  "conteudo/entrada/reescrita-lig3/._regimes-c5.md",
];
const matter = JSON.parse(await Deno.readTextFile(target));
for (let index = 0; index < matter.chapters.length; index++) {
  matter.chapters[index].markdown = await Deno.readTextFile(chapterFiles[index]);
}
matter.version += 1;
await Deno.writeTextFile(target, JSON.stringify(matter, null, 2) + "\n");
