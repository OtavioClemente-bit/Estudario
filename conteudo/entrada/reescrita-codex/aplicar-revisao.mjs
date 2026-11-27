const directory = "conteudo/entrada/reescrita-codex";
const target = `${directory}/maranhao.conhecimentos-estaduais.json`;
const original = await Deno.readTextFile(target);
const newline = original.includes("\r\n") ? "\r\n" : "\n";
const lines = original.split(/\r?\n/);
for (let index = 0; index < 5; index++) {
  const markdown = (await Deno.readTextFile(`${directory}/capitulo-${index + 1}.md`))
    .replace(/\r?\n+$/, "");
  const titleLine = lines.findIndex((line) =>
    line.includes(`"title": "${index + 1}.`)
  );
  if (titleLine < 0 || !lines[titleLine + 1].trimStart().startsWith('"markdown": ')) {
    throw new Error(`Campo markdown ausente no capítulo ${index + 1}`);
  }
  const comma = lines[titleLine + 1].trimEnd().endsWith(",") ? "," : "";
  lines[titleLine + 1] = `      "markdown": ${JSON.stringify(markdown)}${comma}`;
}
await Deno.writeTextFile(target, lines.join(newline));
