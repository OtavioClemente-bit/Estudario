// Pedidos do TRT3, versão 2: uma matéria por assunto, pelo plano em _plano-trt3.ts (mesmo critério de
// divisão da IA do app). Pula matérias que já existem em conteudo/materias.
// Rodar a partir de conteudo/: deno run --allow-read --allow-write pedidos/_gerar-trt3-v2.ts
import { plano } from "./_plano-trt3.ts";

const old = Deno.readTextFileSync("pedidos/_gerar-trt3.ts");
const constOf = (name: string) => JSON.parse(old.match(new RegExp(`const ${name} = ("(?:[^"\\\\]|\\\\.)*");`))![1]) as string;
const RULES = { LAW: constOf("LAW"), ADM: constOf("ADM"), PORT: "- REGRAS DE PORTUGUÊS: textos de apoio curtos escritos por você; questões de leitura e reescrita no estilo da FUMARC." };

const modelo = Deno.readTextFileSync("PEDIDO_CHAT_modelo.txt");
const instr = modelo.slice(modelo.indexOf("══════", modelo.indexOf("MATÉRIA 2")))
  .replaceAll("IDECAN", "FUMARC")
  .replace("Você é professor de Língua Portuguesa e elaborador de questões da banca FUMARC.", "Você é professor da disciplina indicada em cada matéria e elaborador de questões da banca FUMARC.")
  .replace('"subject": "Língua Portuguesa",', '"subject": "(a disciplina indicada na matéria)",')
  .replace("2. As cinco alternativas são frases completas e naturais, do dia a dia de bombeiros e do serviço público,", "2. As cinco alternativas são frases completas e naturais, do dia a dia do serviço público e da Justiça do Trabalho,");
const head = "══════════════════════════════════════════════════════════════\nMATÉRIAS DESTE PEDIDO\n══════════════════════════════════════════════════════════════\nConcurso: TRT da 3ª Região (banca FUMARC), cargos de Técnico e Analista Judiciário.\nCada matéria trata de UM assunto. Fique no escopo indicado: o que estiver fora dele tem matéria própria.\n\n";

const exists = (id: string) => { const [a, b] = id.split("."); try { Deno.statSync(`materias/${a}/${b}.json`); return true; } catch { return false; } };
const todo = plano.filter((m) => !exists(m.id));
const block = (m: (typeof plano)[number], n: number) => {
  const base = m.id.split(".")[1];
  const aliases = [m.title, ...m.topics.filter((t) => t.length <= 380)].map((t) => `"${t.replaceAll('"', "'")}"`).join(", ");
  return `MATÉRIA ${n}
- Arquivos: ${base}.json e recorte-${base}.json
- id: "${m.id}" | title: "${m.title}" | subject: "${m.subject}"
- aliases: [${aliases}] (copie exatamente; acrescente outros nomes usuais do assunto)
- Escopo (cubra TUDO isto e só isto): ${m.scope}
${RULES[m.rule]}`;
};

for (const f of Deno.readDirSync("pedidos")) if (/^trt3n-lote-\d+\.txt$/.test(f.name)) Deno.removeSync(`pedidos/${f.name}`);
let n = 0;
for (let i = 0; i < todo.length; i += 2) {
  n++;
  Deno.writeTextFileSync(`pedidos/trt3n-lote-${String(n).padStart(3, "0")}.txt`, head + todo.slice(i, i + 2).map((m, k) => block(m, k + 1)).join("\n\n") + "\n\n\n" + instr);
}
const index = todo.map((m, i) => `trt3n-lote-${String(Math.floor(i / 2) + 1).padStart(3, "0")} | ${m.subject} | ${m.title}`).join("\n");
Deno.writeTextFileSync("pedidos/trt3n-INDICE.txt", index + "\n");
console.log(`${todo.length} matérias em ${n} lotes (índice em pedidos/trt3n-INDICE.txt)`);
