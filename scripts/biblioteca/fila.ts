// Fila de demanda → pedidos para o chat. Lê os tópicos mais pedidos que ainda não têm matéria
// (library_misses, sem rota FULL na enciclopédia) e escreve conteudo/pedidos/fila-AAAA-MM-DD-N.txt,
// 2 matérias por arquivo, no formato do PEDIDO_CHAT_modelo.txt. Só lê o banco.
// bash scripts/biblioteca/com-chave.sh scripts/biblioteca/fila.ts [quantos=10]
import { normalizeAlias } from "../../supabase/functions/_shared/library.ts";
const url = Deno.env.get("SUPABASE_URL")!;
const key = Deno.env.get("SUPABASE_SERVICE_ROLE_KEY")!;
const limit = Number(Deno.args[0] ?? 10);
const get = async (path: string) => {
  const r = await fetch(`${url}/rest/v1/${path}`, { headers: { apikey: key, authorization: `Bearer ${key}` } });
  if (!r.ok) throw new Error(`${path}: ${r.status}`);
  return await r.json();
};

type Miss = { subject: string; topic: string; board: string | null; topic_norm: string; requests: number };
const misses = await get("library_misses?select=subject,topic,board,topic_norm,requests&order=requests.desc,last_requested_at.desc&limit=200") as Miss[];
const routes = await get("library_topic_routes?select=topic_norm,coverage&coverage=eq.FULL") as { topic_norm: string }[];
const aliases = await get("library_topic_aliases?select=alias_norm") as { alias_norm: string }[];
// Já resolvidos: rota FULL na enciclopédia ou nome que hoje bate com um apelido (inteiro ou a última parte).
const covered = new Set([...routes.map((r) => r.topic_norm), ...aliases.map((a) => a.alias_norm)]);
const lastPart = (m: Miss) => normalizeAlias(m.topic.split(" › ").at(-1) ?? m.topic);
const pending = misses.filter((m) => !covered.has(m.topic_norm) && !covered.has(lastPart(m))).slice(0, limit);
if (pending.length === 0) {
  console.log("Fila vazia: nenhum tópico pedido sem matéria.");
  Deno.exit(0);
}

const slug = (s: string, words = 5) =>
  s.normalize("NFD").replace(/[̀-ͯ]/g, "").toLowerCase().replace(/[^a-z0-9 ]+/g, " ").trim()
    .split(/\s+/).filter((w) => w.length > 2).slice(0, words).join("-");
const modelo = Deno.readTextFileSync("conteudo/PEDIDO_CHAT_modelo.txt");
const instr = modelo.slice(modelo.indexOf("══════", modelo.indexOf("MATÉRIA 2")))
  .replace("Você é professor de Língua Portuguesa e elaborador de questões da banca IDECAN.", "Você é professor da disciplina indicada em cada matéria e elaborador de questões de concursos públicos.")
  .replace('"subject": "Língua Portuguesa",', '"subject": "(a disciplina indicada na matéria)",')
  .replace("2. As cinco alternativas são frases completas e naturais, do dia a dia de bombeiros e do serviço público,", "2. As cinco alternativas são afirmações ou respostas completas e plausíveis, no mesmo formato entre si,")
  .replace("━━ RECORTE IDECAN (um por matéria) ━━", "━━ RECORTE DA BANCA (um por matéria, se a banca estiver indicada) ━━")
  .replace('"board": "IDECAN",', '"board": "(a banca indicada)",');

const day = new Date().toISOString().slice(0, 10);
const files: string[] = [];
for (let i = 0; i < pending.length; i += 2) {
  const pair = pending.slice(i, i + 2);
  const blocks = pair.map((m, k) => {
    const topic = m.topic.split(" › ").at(-1) ?? m.topic;
    const base = slug(topic);
    return `MATÉRIA ${k + 1}
- Arquivos: ${base}.json e recorte-${base}.json
- id: "${slug(m.subject, 2) || "geral"}.${base}" | subject: "${m.subject}"
- aliases: ["${topic.replaceAll('"', "'")}"] (acrescente outros nomes usuais do assunto)
- Tópico pedido pelos alunos (${m.requests} pedido(s)): ${m.topic}
- Banca do concurso: ${m.board ?? "não informada (faça o recorte só se houver banca)"}`;
  });
  const name = `conteudo/pedidos/fila-${day}-${files.length + 1}.txt`;
  Deno.writeTextFileSync(name, "MATÉRIAS DESTE PEDIDO\n\n" + blocks.join("\n\n") + "\n\n\n" + instr);
  files.push(name);
}
console.log(pending.map((m) => `${m.requests}x  ${m.subject} › ${m.topic}${m.board ? `  (${m.board})` : ""}`).join("\n"));
console.log(`\n${files.length} pedido(s): ${files.join(", ")}`);
