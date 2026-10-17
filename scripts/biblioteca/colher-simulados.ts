// Colhe as questões que a IA escreveu nos simulados (as que não vieram da biblioteca) para revisão.
// Só lê: não altera nada no banco. Não lê quem pediu (user_id), só o concurso e o tópico.
//
// Uso (pelo colher-simulados.sh, que obtém a chave pelo Supabase CLI):
//   bash scripts/biblioteca/colher-simulados.sh            # simulados ainda não colhidos
//
// Saída: conteudo/entrada/simulado-ia/colheita-AAAA-MM-DD.json, agrupada por matéria > tópico,
// sem repetir enunciado já colhido (conteudo/entrada/simulado-ia/_vistas.json).

const URL_BASE = Deno.env.get("SUPABASE_URL");
const KEY = Deno.env.get("SUPABASE_SERVICE_ROLE_KEY");
if (!URL_BASE || !KEY) throw new Error("faltam SUPABASE_URL e SUPABASE_SERVICE_ROLE_KEY");

const SAIDA = "conteudo/entrada/simulado-ia";
const VISTAS = `${SAIDA}/_vistas.json`;

type Json = Record<string, unknown>;

/** Mesma ideia do questionKey do app: letras e algarismos do começo do enunciado, em minúsculas. */
function chave(enunciado: string): string {
  const base = enunciado.normalize("NFD").replace(/[̀-ͯ]/g, "").toLowerCase().replace(/[^a-z0-9]/g, "").slice(0, 160);
  let h = 0x811c9dc5;
  for (let i = 0; i < base.length; i++) { h ^= base.charCodeAt(i); h = Math.imul(h, 0x01000193) >>> 0; }
  return h.toString(16).padStart(8, "0");
}

async function lerJson<T>(caminho: string, padrao: T): Promise<T> {
  try { return JSON.parse(await Deno.readTextFile(caminho)) as T; } catch { return padrao; }
}

const vistas = Deno.env.get("COLHER_REFAZER") ? { ultimoJob: null, chaves: [] as string[] } : await lerJson<{ ultimoJob: string | null; chaves: string[] }>(VISTAS, { ultimoJob: null, chaves: [] });
const jaVistas = new Set(vistas.chaves);

const linhas: Json[] = [];
let desde = vistas.ultimoJob;
for (let pagina = 0; pagina < 200; pagina++) {
  const filtro = desde ? `&created_at=gt.${encodeURIComponent(desde)}` : "";
  const resp = await fetch(
    `${URL_BASE}/rest/v1/ai_jobs?select=id,created_at,request_payload,proposal&feature=eq.SIMULATION_GENERATION&status=eq.SUCCEEDED${filtro}&order=created_at.asc&limit=200`,
    { headers: { apikey: KEY, Authorization: `Bearer ${KEY}` } },
  );
  if (!resp.ok) throw new Error(`ai_jobs: ${resp.status} ${await resp.text()}`);
  const lote = await resp.json() as Json[];
  if (lote.length === 0) break;
  linhas.push(...lote);
  desde = String(lote[lote.length - 1].created_at);
  if (lote.length < 200) break;
}

const grupos = new Map<string, Json[]>();
let novas = 0, daBiblioteca = 0, repetidas = 0;
for (const job of linhas) {
  const pedido = (job.request_payload ?? {}) as Json;
  const entrada = ((pedido.input ?? pedido) as Json);
  const itens = new Map(((entrada.items ?? []) as Json[]).map((item) => [String(item.ref), item]));
  const questoes = (((job.proposal ?? {}) as Json).questions ?? []) as Json[];
  for (const q of questoes) {
    if (q.fromLibrary === true) { daBiblioteca++; continue; }
    const k = chave(String(q.statement ?? ""));
    if (jaVistas.has(k)) { repetidas++; continue; }
    jaVistas.add(k);
    const item = itens.get(String(q.itemRef)) ?? {};
    const materia = String(item.subjectName ?? "(revanche ou sem tópico)");
    const topico = ((item.topicPath ?? []) as string[]).join(" > ");
    const grupo = `${materia} || ${topico}`;
    if (!grupos.has(grupo)) grupos.set(grupo, []);
    grupos.get(grupo)!.push({
      job: job.id,
      criado: job.created_at,
      concurso: entrada.competitionName ?? null,
      banca: entrada.board ?? null,
      cargo: entrada.role ?? null,
      questao: { statement: q.statement, format: q.format, difficulty: q.difficulty, options: q.options, explanation: q.explanation, trap: q.trap ?? "" },
    });
    novas++;
  }
}

const ordenado = [...grupos.entries()]
  .sort((a, b) => b[1].length - a[1].length)
  .map(([grupo, itens]) => {
    const [materia, topico] = grupo.split(" || ");
    return { materia, topico, quantidade: itens.length, itens };
  });

await Deno.mkdir(SAIDA, { recursive: true });
const hoje = new Date().toISOString().slice(0, 10);
if (novas > 0) await Deno.writeTextFile(`${SAIDA}/colheita-${hoje}.json`, JSON.stringify(ordenado, null, 1));
await Deno.writeTextFile(VISTAS, JSON.stringify({ ultimoJob: desde, chaves: [...jaVistas] }));

console.log(`simulados lidos: ${linhas.length} | questões da IA novas: ${novas} | da biblioteca: ${daBiblioteca} | repetidas: ${repetidas}`);
for (const g of ordenado.slice(0, 25)) console.log(`${String(g.quantidade).padStart(4)}  ${g.materia} > ${g.topico}`);
if (novas > 0) console.log(`gravado: ${SAIDA}/colheita-${hoje}.json`);
