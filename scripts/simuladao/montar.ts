// Simuladão do mês: monta conteudo/simulados/<edital>.simulado-N.json (N ≥ 2) de cada edital a partir
// de questões já publicadas e conferidas dos recortes, na estrutura do simulado 1 do edital (cada
// posição com a mesma disciplina, matéria, formato e dificuldade), sem repetir questão de simulados
// anteriores do mesmo edital. Não escreve nem altera o texto de nenhuma questão (AGENTS.md, 4ª exceção).
// Posição sem questão disponível fica de fora; faltando mais de 5%, o simulado do edital não é gravado.
//
// Uso:
//   deno run --allow-read --allow-write=conteudo/simulados scripts/simuladao/montar.ts [--edital <id>] [--seco]
// Sem --edital, monta o próximo número de todos os editais que têm simulado 1. --seco só mostra.

const SIMULADOS = "conteudo/simulados";
const RECORTES = "conteudo/recortes";
const MAX_FALTA = 0.05;

type Json = Record<string, unknown>;
type Option = { key: string; text: string; correct: boolean };
type Slot = { n: number; disciplina: string; materia?: string; format: string; difficulty?: string };
type Candidate = { q: Json; banca: string; materia: string };

const args = Deno.args;
const only = args.includes("--edital") ? args[args.indexOf("--edital") + 1] : null;
const dry = args.includes("--seco");
// Um por mês: edital cujo último Simuladão tem menos de 25 dias fica para depois (--forcar ignora).
const force = args.includes("--forcar");
const MIN_DAYS = 25;

/** A mesma chave de enunciado do app, do site e do servidor (QuestionKey / questionKey). */
function questionKey(statement: string): string {
  const letters = statement.toLowerCase().replace(/[^\p{L}\p{Nd}]/gu, "").slice(0, 160);
  let hash = 2166136261;
  for (let i = 0; i < letters.length; i++) hash = Math.imul(hash ^ letters.charCodeAt(i), 16777619);
  return (hash >>> 0).toString(16).padStart(8, "0");
}

/** Número determinístico a partir de um texto (a mesma entrada dá sempre o mesmo simulado). */
function seedOf(text: string): number {
  let hash = 2166136261;
  for (let i = 0; i < text.length; i++) hash = Math.imul(hash ^ text.charCodeAt(i), 16777619);
  return hash >>> 0;
}

const slug = (s: string) => s.normalize("NFD").replace(/[̀-ͯ]/g, "").toLowerCase().replace(/[^a-z0-9]+/g, "-").replace(/^-|-$/g, "");

/** Pasta dos recortes da banca do edital ("Cebraspe" → cebraspe, "PMMG (CRS)" → pmmg-crs...). */
const bancaFolders = [...Deno.readDirSync(RECORTES)].filter((e) => e.isDirectory).map((e) => e.name);
function folderOf(banca: string): string | null {
  const s = slug(banca);
  return bancaFolders.find((f) => f === s) ?? bancaFolders.find((f) => s.startsWith(f) || f.startsWith(s.split("-")[0])) ?? null;
}

/** Questões dos recortes de uma matéria, primeiro as da banca do edital, depois as das outras. */
const recorteCache = new Map<string, Json[]>();
function recorte(folder: string, materia: string): Json[] {
  const path = `${RECORTES}/${folder}/${materia}.json`;
  if (!recorteCache.has(path)) {
    let list: Json[] = [];
    try { list = ((JSON.parse(Deno.readTextFileSync(path)) as Json).questions as Json[]) ?? []; } catch { /* sem recorte */ }
    recorteCache.set(path, list);
  }
  return recorteCache.get(path)!;
}
function candidates(materia: string, banca: string | null): Candidate[] {
  const order = banca ? [banca, ...bancaFolders.filter((f) => f !== banca)] : bancaFolders;
  return order.flatMap((f) => recorte(f, materia).map((q) => ({ q, banca: f, materia })));
}

// Os recortes chamam o Certo ou Errado de TRUE_FALSE; os simulados, de CERTO_ERRADO (as alternativas são as mesmas, C e E).
const formatOf = (q: Json) => (q.format === "CERTO_ERRADO" || q.format === "TRUE_FALSE" ? "CERTO_ERRADO" : "MULTIPLE_CHOICE");

function build(edital: string, first: Json, previous: Json[], number: number): { file: Json; missing: number; total: number } {
  const banca = folderOf(String(first.banca ?? ""));
  const used = new Set<string>();
  for (const sim of [first, ...previous]) for (const q of (sim.questions as Json[])) used.add(questionKey(String(q.statement ?? "")));
  const slots = (first.questions as Json[]).map((q, i) => ({
    n: Number(q.n ?? i + 1), disciplina: String(q.disciplina ?? ""), materia: q.materia ? String(q.materia) : undefined,
    format: formatOf(q), difficulty: q.difficulty ? String(q.difficulty) : undefined,
  } as Slot));
  const out: Json[] = [];
  const sameDiscipline = new Map<string, string[]>();
  for (const s of slots) if (s.materia) sameDiscipline.set(s.disciplina, [...new Set([...(sameDiscipline.get(s.disciplina) ?? []), s.materia])]);
  let missing = 0;
  for (const slot of slots) {
    if (!slot.materia) { missing++; continue; }
    let pool = candidates(slot.materia, banca)
      .filter((c) => formatOf(c.q) === slot.format)
      .filter((c) => Array.isArray(c.q.options) && (c.q.options as Option[]).some((o) => o.correct))
      .filter((c) => !used.has(questionKey(String(c.q.statement ?? ""))));
    // Sem questão da matéria exata: outra matéria da mesma disciplina desta prova (como faria quem monta
    // a prova à mão). Ainda sem nada, a posição fica de fora.
    if (pool.length === 0) {
      for (const other of sameDiscipline.get(slot.disciplina) ?? []) {
        if (other === slot.materia) continue;
        pool = candidates(other, banca).filter((c) => formatOf(c.q) === slot.format)
          .filter((c) => Array.isArray(c.q.options) && (c.q.options as Option[]).some((o) => o.correct))
          .filter((c) => !used.has(questionKey(String(c.q.statement ?? ""))));
        if (pool.length > 0) break;
      }
    }
    if (pool.length === 0) { missing++; continue; }
    // A banca do edital e a mesma dificuldade primeiro; entre as iguais, escolha fixa pela semente.
    const rank = (c: Candidate) => (c.banca === banca ? 0 : 2) + (c.q.difficulty === slot.difficulty ? 0 : 1);
    const best = Math.min(...pool.map(rank));
    const tier = pool.filter((c) => rank(c) === best);
    const chosen = tier[seedOf(`${edital}|${number}|${slot.n}`) % tier.length];
    const pick = chosen.q;
    used.add(questionKey(String(pick.statement)));
    out.push({
      n: out.length + 1,
      disciplina: slot.disciplina,
      materia: chosen.materia,
      topico: pick.topico ?? null,
      format: slot.format,
      difficulty: pick.difficulty ?? slot.difficulty ?? "MEDIA",
      statement: pick.statement,
      options: (pick.options as Option[]).map((o) => ({ key: o.key, text: o.text, correct: o.correct === true })),
      explanation: pick.explanation ?? "",
      pegadinha: null,
      origem: `recorte ${chosen.banca} · ${chosen.materia}`,
    });
  }
  return {
    file: { edital, banca: first.banca, estrutura: first.estrutura, numero: number, questions: out },
    missing,
    total: slots.length,
  };
}

const files = [...Deno.readDirSync(SIMULADOS)].map((e) => e.name);
const editais = files.map((f) => f.match(/^(.+)\.simulado-1\.json$/)?.[1]).filter((e): e is string => !!e).filter((e) => !only || e === only);
let written = 0;
for (const edital of editais.sort()) {
  const numbers = files.map((f) => f.match(new RegExp(`^${edital.replace(/[.*+?^${}()|[\]\\]/g, "\\$&")}\\.simulado-(\\d+)\\.json$`))?.[1]).filter(Boolean).map(Number);
  const next = Math.max(...numbers) + 1;
  if (!force && next > 2) {
    const last = Deno.statSync(`${SIMULADOS}/${edital}.simulado-${next - 1}.json`).mtime?.getTime() ?? 0;
    if (Date.now() - last < MIN_DAYS * 86_400_000) { console.log(`${edital}: o último Simuladão tem menos de ${MIN_DAYS} dias, fica para o mês que vem.`); continue; }
  }
  const read = (n: number) => JSON.parse(Deno.readTextFileSync(`${SIMULADOS}/${edital}.simulado-${n}.json`)) as Json;
  const first = read(1);
  const previous = numbers.filter((n) => n > 1).map(read);
  const { file, missing, total } = build(edital, first, previous, next);
  const share = missing / total;
  const status = share > MAX_FALTA ? `NÃO gravado (faltam ${missing} de ${total})` : `${total - missing}/${total} questões`;
  console.log(`${edital} · simulado ${next}: ${status}`);
  if (share > MAX_FALTA || dry) continue;
  Deno.writeTextFileSync(`${SIMULADOS}/${edital}.simulado-${next}.json`, JSON.stringify(file, null, 2) + "\n");
  written++;
}
console.log(`\n${written} Simuladão(ões) gravado(s) em ${SIMULADOS}.`);
