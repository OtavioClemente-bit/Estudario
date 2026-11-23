const base = "conteudo/entrada/reescrita-lig3/direito-constitucional";
const target = "conteudo/entrada/reescrita-lig3/direito-constitucional.advocacia-defensoria.json";
let json = await Deno.readTextFile(target);

function replaceMarkdown(source: string, markdown: string, from: number): { text: string; next: number } {
  const marker = '"markdown": ';
  const start = source.indexOf(marker, from);
  if (start < 0) throw new Error("Campo markdown não encontrado");
  const valueStart = start + marker.length;
  if (source[valueStart] !== '"') throw new Error("Campo markdown inválido");
  let escaped = false;
  for (let i = valueStart + 1; i < source.length; i++) {
    const char = source[i];
    if (escaped) escaped = false;
    else if (char === "\\") escaped = true;
    else if (char === '"') {
      const encoded = JSON.stringify(markdown);
      const text = source.slice(0, valueStart) + encoded + source.slice(i + 1);
      return { text, next: valueStart + encoded.length };
    }
  }
  throw new Error("Fim do campo markdown não encontrado");
}

let cursor = 0;
for (let i = 1; i <= 5; i++) {
  const markdown = await Deno.readTextFile(`${base}/c${i}.md`);
  const replaced = replaceMarkdown(json, markdown, cursor);
  json = replaced.text;
  cursor = replaced.next;
}

await Deno.writeTextFile(target, json);
