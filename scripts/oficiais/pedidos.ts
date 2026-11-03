// Monta os pedidos ao ChatGPT para as questões de prova real (work/oficiais/questoes.jsonl, de preparar.ts):
// em cada pedido, 25 questões com o gabarito oficial e 5 matérias candidatas; o ChatGPT escolhe a matéria
// certa e escreve a explicação (não muda enunciado, alternativas nem gabarito). Grava a fila em
// work/oficiais/fila.json; fila.ts manda 1 de cada 5 pedidos do ChatGPT dela, até acabar.
//   deno run -A scripts/oficiais/pedidos.ts
const ROOT = new URL("../../", import.meta.url).pathname.replace(/^\/([A-Za-z]:)/, "$1");
const ENTRADA = `${ROOT}work/oficiais/questoes.jsonl`;
const PASTA = `${ROOT}conteudo/entrada/oficiais`;
const FILA = `${ROOT}work/oficiais/fila.json`; // fica fora do git: tem o texto das provas
const POR_PEDIDO = 25;
type Json = Record<string, any>;

const INSTRUCAO = "EXPLICAR (pedido diferente de QUESTÕES/MATÉRIA): abaixo vêm questões REAIS de concurso com o gabarito OFICIAL da banca. " +
  "Não mude enunciado, alternativas nem gabarito. Para cada questão: (1) em \"materia\", copie o id da matéria certa para estudar a questão, " +
  "escolhido entre os \"candidatos\" (ou \"nenhuma\" se nenhum servir); (2) em \"explicacao\", escreva de 150 a 300 caracteres dizendo por que " +
  "o gabarito oficial está certo e onde erram as outras opções (ou o item), sem citar letra; (3) em \"alerta\", deixe vazio, ou diga em poucas " +
  "palavras se o gabarito oficial parece errado ou se a questão ficou desatualizada por lei ou norma mais nova. Grave /mnt/data/<nome>.json com " +
  "{\"itens\":[{\"id\":\"...\",\"materia\":\"...\",\"explicacao\":\"...\",\"alerta\":\"\"}]} (um item por questão, na mesma ordem) e responda só com o link.";

const linhas = Deno.readTextFileSync(ENTRADA).trim().split("\n").map((l) => JSON.parse(l) as Json);
let fila: Json[] = [];
try { fila = JSON.parse(Deno.readTextFileSync(`${FILA}`)); } catch { /* primeira vez */ }
const jaNaFila = new Set(fila.flatMap((p) => p.ids as string[]));
// As de matéria mais clara primeiro; dentro disso, por banca e prova (pedidos de uma prova só).
const novas = linhas.filter((q) => !jaNaFila.has(q.id))
  .sort((a, b) => Number(b.firme) - Number(a.firme) || String(a.id).localeCompare(String(b.id)));
let n = fila.length;
for (let i = 0; i < novas.length; i += POR_PEDIDO) {
  const grupo = novas.slice(i, i + POR_PEDIDO);
  const nome = `oficial-${String(++n).padStart(5, "0")}`;
  const questoes = grupo.map((q) => ({
    id: q.id, banca: q.banca, prova: `${q.concurso}${q.ano ? ` (${q.ano})` : ""}`,
    ...(q.base && q.base.length > 20 ? { texto_base: q.base } : {}),
    enunciado: q.enunciado,
    ...(q.alternativas.length ? { alternativas: q.alternativas.map((a: string, k: number) => `${"ABCDE"[k]}) ${a}`) } : {}),
    gabarito_oficial: q.alternativas.length ? q.gabarito : q.gabarito === "C" ? "Certo" : "Errado",
    candidatos: q.cands.map((c: Json) => `${c.id} (${c.title})`),
  }));
  // Uma linha só: o servidor da fila entrega um pedido por linha.
  const pedido = `${INSTRUCAO} nome=${nome}; QUESTOES=${JSON.stringify(questoes).replace(/[\r\n]+/g, " ")}`;
  fila.push({ nome, ids: grupo.map((q) => q.id), pedido, enviado: null });
}
Deno.mkdirSync(`${PASTA}/respostas`, { recursive: true });
Deno.writeTextFileSync(FILA, JSON.stringify(fila));
console.log(`${fila.length} pedidos na fila (${novas.length} questões novas)`);
