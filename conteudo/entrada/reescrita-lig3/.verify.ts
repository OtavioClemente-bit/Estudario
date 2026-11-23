const source = JSON.parse(await Deno.readTextFile("conteudo/materias/processo-penal/procedimentos-criminais-comuns-especiais.json"));
const output = JSON.parse(await Deno.readTextFile("conteudo/entrada/reescrita-lig3/processo-penal.procedimentos-criminais-comuns-especiais.json"));
const chars = output.chapters.reduce((total: number, chapter: { markdown: string }) => total + chapter.markdown.length, 0);
const normalized = structuredClone(output);
normalized.version = source.version;
for (let i = 0; i < normalized.chapters.length; i++) normalized.chapters[i].markdown = source.chapters[i].markdown;
const onlyRequestedFieldsChanged = JSON.stringify(normalized) === JSON.stringify(source);
const formatOk = output.chapters.every((chapter: { title: string; markdown: string }) =>
  chapter.markdown.startsWith("### ") && chapter.markdown.includes("**Pegadinha:**") && chapter.markdown.includes("**Resumo para a prova**")
);
console.log(JSON.stringify({ version: output.version, chapters: output.chapters.length, markdownChars: chars, onlyRequestedFieldsChanged, formatOk }));
if (output.version !== source.version + 1 || chars < 15000 || chars > 45000 || !onlyRequestedFieldsChanged || !formatOk) Deno.exit(1);
