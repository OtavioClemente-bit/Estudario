// Confere e publica a biblioteca de matérias (pasta conteudo/) no Supabase.
//
//   deno run --allow-read --allow-env --allow-net scripts/biblioteca/importar.ts            só confere
//   deno run --allow-read --allow-env --allow-net scripts/biblioteca/importar.ts --publicar confere e envia
//
// Para publicar: SUPABASE_URL e SUPABASE_SERVICE_ROLE_KEY no ambiente (nunca no repositório).
// Nada é enviado se qualquer arquivo tiver problema.

import {
  LibraryValidationError,
  normalizeAlias,
  normalizeBoard,
  validateBoardNote,
  validateLibraryMaterial,
} from "../../supabase/functions/_shared/library.ts";

type Json = Record<string, unknown>;

const root = new URL("../../conteudo/", import.meta.url);
const publish = Deno.args.includes("--publicar");

async function jsonFiles(folder: string): Promise<{ path: string; value: Json }[]> {
  const result: { path: string; value: Json }[] = [];
  const walk = async (dir: URL, relative: string) => {
    let entries: Deno.DirEntry[];
    try {
      entries = [...Deno.readDirSync(dir)];
    } catch {
      return;
    }
    for (const entry of entries.sort((a, b) => a.name.localeCompare(b.name))) {
      const path = `${relative}/${entry.name}`;
      if (entry.isDirectory) await walk(new URL(`${entry.name}/`, dir), path);
      else if (entry.name.endsWith(".json") && !entry.name.startsWith("_")) {
        try {
          result.push({ path, value: JSON.parse(await Deno.readTextFile(new URL(entry.name, dir))) });
        } catch (error) {
          problems.push(`${path}: JSON inválido (${(error as Error).message})`);
        }
      }
    }
  };
  await walk(new URL(`${folder}/`, root), `conteudo/${folder}`);
  return result;
}

const problems: string[] = [];
const report = (path: string, error: unknown) => {
  if (error instanceof LibraryValidationError) error.problems.forEach((problem) => problems.push(`${path}: ${problem}`));
  else problems.push(`${path}: ${(error as Error).message}`);
};

// Matérias.
const materials = new Map<string, Json>();
const aliasOwner = new Map<string, string>();
for (const { path, value } of await jsonFiles("materias")) {
  try {
    validateLibraryMaterial(value);
  } catch (error) {
    report(path, error);
    continue;
  }
  const id = String(value.id);
  if (materials.has(id)) problems.push(`${path}: id ${id} repetido`);
  materials.set(id, value);
  const aliases = new Set([normalizeAlias(String(value.title)), ...(value.aliases as string[]).map(normalizeAlias)]);
  for (const alias of aliases) {
    const owner = aliasOwner.get(alias);
    if (owner && owner !== id) problems.push(`${path}: apelido "${alias}" já é de ${owner}`);
    aliasOwner.set(alias, id);
  }
}

// Recortes por banca/cargo.
const notes: { topic: string; boardNorm: string; roleNorm: string; value: Json }[] = [];
for (const { path, value } of await jsonFiles("recortes")) {
  const material = materials.get(String(value.topic));
  if (!material) {
    problems.push(`${path}: matéria ${value.topic} não existe (ou tem problema)`);
    continue;
  }
  try {
    validateBoardNote(value, material);
    const entry = { topic: String(value.topic), boardNorm: normalizeBoard(String(value.board)), roleNorm: normalizeAlias(value.role as string | null), value };
    if (notes.some((n) => n.topic === entry.topic && n.boardNorm === entry.boardNorm && n.roleNorm === entry.roleNorm)) {
      problems.push(`${path}: recorte repetido para ${entry.topic} / ${entry.boardNorm} / ${entry.roleNorm || "qualquer cargo"}`);
    }
    notes.push(entry);
  } catch (error) {
    report(path, error);
  }
}

// Editais: cada texto do edital vira um apelido da matéria a que foi ligado.
const missing: string[] = [];
const materialAliases = new Set(aliasOwner.keys());
const ambiguous = new Set<string>();
const qualifiedAmbiguous = new Set<string>();
/** Apelido com disciplina → texto do tópico sem ela (o foco do tópico vale para os dois). */
const qualifiedPlain = new Map<string, string>();
for (const { path, value } of await jsonFiles("editais")) {
  const subjects = Array.isArray(value.disciplinas) ? value.disciplinas as Json[] : [];
  if (subjects.length === 0) problems.push(`${path}: sem disciplinas`);
  for (const subject of subjects) {
    for (const topic of (Array.isArray(subject.topicos) ? subject.topicos : []) as Json[]) {
      const text = String(topic.texto ?? "");
      const alias = normalizeAlias(text);
      if (alias.length < 2) {
        problems.push(`${path}: tópico sem texto em ${subject.nome}`);
        continue;
      }
      if (topic.topico === null || topic.topico === undefined) {
        missing.push(`${value.concurso} › ${subject.nome} › ${text}`);
        continue;
      }
      const id = String(topic.topico);
      if (!materials.has(id)) {
        problems.push(`${path}: "${text}" aponta para ${id}, que não existe em conteudo/materias`);
        continue;
      }
      // Apelido com a disciplina na frente ("direito penal principios basicos"): o servidor tenta
      // este primeiro, então um nome genérico ("Princípios básicos.") cai na matéria certa da
      // disciplina em vez da primeira matéria que tem esse nome (que era a de física).
      const qualified = `${normalizeAlias(String(subject.nome ?? ""))} ${alias}`.trim();
      if (qualified !== alias && qualified.length <= 400 && !qualifiedAmbiguous.has(qualified) && !materialAliases.has(qualified)) {
        const owner = aliasOwner.get(qualified);
        if (owner && owner !== id) {
          aliasOwner.delete(qualified);
          qualifiedAmbiguous.add(qualified);
        } else {
          aliasOwner.set(qualified, id);
          qualifiedPlain.set(qualified, alias);
        }
      }
      // O banco guarda apelidos de até 400 caracteres; tópico de edital mais longo que isso é
      // atendido pela enciclopédia (rota tópico → matérias), não pelo apelido.
      if (alias.length > 400) continue;
      // Texto genérico ("Provas.", "Competência.") que editais diferentes ligam a matérias
      // diferentes não vira apelido: o apelido é global e não sabe de que edital veio. Se o
      // texto for apelido próprio de uma matéria, continua dela.
      if (ambiguous.has(alias)) continue;
      const owner = aliasOwner.get(alias);
      if (owner && owner !== id) {
        if (!materialAliases.has(alias)) {
          aliasOwner.delete(alias);
          ambiguous.add(alias);
        }
        continue;
      }
      aliasOwner.set(alias, id);
    }
  }
}

// Foco por tópico (conteudo/foco/<id>.json): tópico estreito abre a matéria nos capítulos e nas
// questões dele. Só vale para o apelido que é mesmo daquela matéria; "inteira" fica sem foco.
const focusByAlias = new Map<string, { capitulos: number[]; questoes: string[] }>();
const focusByPlain = new Map<string, { capitulos: number[]; questoes: string[] }>();
for (const { path, value } of await jsonFiles("foco")) {
  const material = materials.get(String(value.materia));
  if (!material) {
    problems.push(`${path}: matéria ${value.materia} não existe`);
    continue;
  }
  const chapterCount = (material.chapters as unknown[]).length;
  const questionCount = (material.questions as unknown[]).length;
  for (const topic of (Array.isArray(value.topicos) ? value.topicos : []) as Json[]) {
    if (topic.foco === "inteira") continue;
    const alias = normalizeAlias(String(topic.texto ?? ""));
    const capitulos = ((topic.capitulos ?? []) as unknown[]).map(Number).filter((i) => Number.isInteger(i) && i >= 0 && i < chapterCount);
    const questoes = ((topic.questoes ?? []) as unknown[]).map(String).filter((q) => /^q\d+$/.test(q) && Number(q.slice(1)) >= 1 && Number(q.slice(1)) <= questionCount);
    if (capitulos.length === 0 && questoes.length === 0) continue;
    if (aliasOwner.get(alias) === material.id) focusByAlias.set(alias, { capitulos, questoes });
    if (!focusByPlain.has(`${material.id}|${alias}`)) focusByPlain.set(`${material.id}|${alias}`, { capitulos, questoes });
  }
}
// O apelido com disciplina herda o foco do mesmo texto na mesma matéria.
for (const [qualified, plain] of qualifiedPlain) {
  const focus = focusByPlain.get(`${aliasOwner.get(qualified)}|${plain}`);
  if (focus) focusByAlias.set(qualified, focus);
}

// Catálogo de editais que o app mostra na busca ("PMMG", "polícia federal"). Cada arquivo de
// conteudo/editais vira uma linha; "catalogo": false deixa um edital só como fonte de apelidos.
const catalogRows: Json[] = [];
for (const { path, value } of await jsonFiles("editais")) {
  if (value.catalogo === false) continue;
  const id = path.split("/").pop()!.replace(/\.json$/, "");
  const text = (field: string) => typeof value[field] === "string" && (value[field] as string).trim() ? (value[field] as string).trim() : null;
  const shortName = text("concurso");
  const role = text("cargoExibicao") ?? text("cargo");
  if (!shortName || !role) {
    problems.push(`${path}: catálogo precisa de "concurso" e "cargo"`);
    continue;
  }
  const subjects = ((value.disciplinas ?? []) as Json[]).map((subject) => ({
    name: String(subject.nome ?? "").trim(),
    topics: ((subject.topicos ?? []) as Json[]).map((topic) => String(topic.texto ?? "").trim()).filter(Boolean),
    // Questões da matéria na prova real (quadro do edital); o app transforma em prioridade.
    ...(Number.isInteger(subject.questoes) && (subject.questoes as number) >= 0 ? { questions: subject.questoes } : {}),
    ...(typeof subject.peso === "number" && subject.peso > 0 && subject.peso !== 1 ? { weight: subject.peso } : {}),
    ...(typeof subject.notaBloco === "string" && subject.notaBloco.trim() ? { blockNote: subject.notaBloco.trim() } : {}),
  })).filter((subject) => subject.name && subject.topics.length > 0);
  const topics = ((value.disciplinas ?? []) as Json[]).flatMap((subject) => (subject.topicos ?? []) as Json[]);
  const synonyms = Array.isArray(value.sinonimos) ? (value.sinonimos as unknown[]).map(String) : [];
  const year = typeof value.ano === "number" ? value.ano : null;
  const searchTerms = [shortName, text("orgao"), text("cargo"), role, text("banca"), year?.toString(), ...synonyms];
  const searchNorm = [...new Set(searchTerms.filter((term): term is string => !!term).map(normalizeAlias).filter(Boolean))].join(" | ");
  if (!/^[a-z0-9]+(-[a-z0-9]+)*(--[a-z0-9]+(-[a-z0-9]+)*)?$/.test(id)) problems.push(`${path}: nome de arquivo inválido para o catálogo`);
  if (subjects.length === 0) problems.push(`${path}: catálogo sem matérias`);
  catalogRows.push({
    id,
    short_name: shortName,
    agency: text("orgao") ?? shortName,
    role,
    board: text("banca"),
    year,
    edital_ref: text("referencia"),
    source_url: text("url")?.startsWith("http") ? text("url") : null,
    notice: ((o) => (o && o.length > 400 ? o.slice(0, 397).trimEnd() + "…" : o))(text("observacao")),
    search_norm: searchNorm.slice(0, 2000),
    subjects,
    subject_count: subjects.length,
    topic_count: subjects.reduce((sum, subject) => sum + subject.topics.length, 0),
    ready_topic_count: topics.filter((topic) => topic.topico !== null && topic.topico !== undefined).length,
    status: "PUBLISHED",
    updated_at: new Date().toISOString(),
  });
}

console.log(`Matérias: ${materials.size} · Recortes: ${notes.length} · Apelidos: ${aliasOwner.size} · Editais no catálogo: ${catalogRows.length}`);
console.log(`Apelidos com foco (tópico estreito): ${focusByAlias.size}`);
if (ambiguous.size > 0) console.log(`Textos de edital ligados a matérias diferentes (ficam sem apelido): ${ambiguous.size}`);
if (missing.length > 0) {
  console.log(`\nTópicos de edital ainda sem matéria (${missing.length}):`);
  missing.forEach((line) => console.log(`  - ${line}`));
}
if (problems.length > 0) {
  console.error(`\n${problems.length} problema(s); nada foi enviado:`);
  problems.forEach((problem) => console.error(`  ✗ ${problem}`));
  Deno.exit(1);
}
console.log("\nTudo certo.");
if (!publish) {
  console.log("Para enviar ao Supabase, rode de novo com --publicar.");
  Deno.exit(0);
}

const url = Deno.env.get("SUPABASE_URL")?.replace(/\/$/, "");
const key = Deno.env.get("SUPABASE_SERVICE_ROLE_KEY");
if (!url || !key) {
  console.error("Defina SUPABASE_URL e SUPABASE_SERVICE_ROLE_KEY no ambiente.");
  Deno.exit(1);
}

// Banco recém-reiniciado ou ocupado devolve 500/503/504 por tempo: tenta de novo com pausa crescente.
async function rest(path: string, method: string, body?: unknown, prefer = "return=minimal") {
  for (let attempt = 1; ; attempt++) {
    try {
      return await restOnce(path, method, body, prefer);
    } catch (error) {
      if (attempt >= 6 || !/: (500|502|503|504) /.test(String(error))) throw error;
      await new Promise((r) => setTimeout(r, attempt * 5000));
    }
  }
}

async function restOnce(path: string, method: string, body?: unknown, prefer = "return=minimal") {
  const response = await fetch(`${url}/rest/v1/${path}`, {
    method,
    headers: { apikey: key!, authorization: `Bearer ${key}`, "content-type": "application/json", prefer },
    body: body === undefined ? undefined : JSON.stringify(body),
  });
  if (!response.ok) throw new Error(`${method} ${path}: ${response.status} ${await response.text()}`);
  await response.body?.cancel().catch(() => {});
}

// Envio incremental (o plano grátis tem orçamento de Disk IO: regravar 265 mil apelidos e 4,6 mil
// recortes a cada publicação o esgotava). Guarda um hash por linha já publicada e só envia o que mudou;
// apaga só o que saiu. Sem o arquivo (primeira vez, outra máquina) ou com --tudo, envia tudo, como antes.
const CACHE_PATH = "scripts/biblioteca/.publicado-cache.json";
type Cache = Record<string, Record<string, string>>;
let cache: Cache = {};
if (!Deno.args.includes("--tudo")) {
  try { cache = JSON.parse(await Deno.readTextFile(CACHE_PATH)) as Cache; } catch { cache = {}; }
}
const nextCache: Cache = {};
// --so-registrar: o banco já está igual ao repositório (acabou de ser publicado); só grava o histórico local, sem enviar nada.
const onlyMark = Deno.args.includes("--so-registrar");
function hashOf(value: unknown): string {
  const text = JSON.stringify(value);
  let h1 = 0x811c9dc5, h2 = 0x01000193;
  for (let i = 0; i < text.length; i++) {
    const c = text.charCodeAt(i);
    h1 = Math.imul(h1 ^ c, 0x01000193);
    h2 = Math.imul(h2 ^ c, 0x5bd1e995);
  }
  return (h1 >>> 0).toString(36) + (h2 >>> 0).toString(36) + text.length.toString(36);
}
/** Linhas novas ou alteradas desde a última publicação, e as chaves que saíram. */
function diff<T extends Record<string, unknown>>(table: string, rows: T[], keyOf: (row: T) => string, ignore: string[] = ["updated_at"]) {
  const before = cache[table];
  const now: Record<string, string> = {};
  const send: T[] = [];
  for (const row of rows) {
    const key = keyOf(row);
    const h = hashOf(Object.fromEntries(Object.entries(row).filter(([field]) => !ignore.includes(field))));
    now[key] = h;
    if (!before || before[key] !== h) send.push(row);
  }
  const removed = before ? Object.keys(before).filter((key) => !(key in now)) : [];
  if (onlyMark) return { send: [] as T[], removed: [] as string[], known: true, commit: () => { nextCache[table] = now; } };
  return { send, removed, known: !!before, commit: () => { nextCache[table] = now; } };
}
async function saveCache() {
  await Deno.writeTextFile(CACHE_PATH, JSON.stringify({ ...cache, ...nextCache }));
}

const META = new Set(["id", "subject", "title", "version", "status", "aliases"]);
// Em lotes: com a biblioteca inteira num só envio, o banco cancela por tempo (statement timeout).
const topicRows = [...materials.values()].map((m) => ({
  id: m.id,
  subject: m.subject,
  title: m.title,
  version: m.version,
  status: m.status,
  material: Object.fromEntries(Object.entries(m).filter(([field]) => !META.has(field))),
  question_count: (m.questions as unknown[]).length,
  updated_at: new Date().toISOString(),
}));
const topicDiff = diff("library_topics", topicRows, (r) => String(r.id));
for (let i = 0; i < topicDiff.send.length; i += 10) {
  await rest("library_topics?on_conflict=id", "POST", topicDiff.send.slice(i, i + 10), "resolution=merge-duplicates,return=minimal");
}
topicDiff.commit();
await saveCache();

// Apelidos e recortes espelham o repositório: o que saiu daqui sai do banco.
// Banco ainda sem a coluna focus (migração não aplicada): envia sem foco, para nunca apagar os
// apelidos e falhar ao recriá-los.
const hasFocusColumn = await fetch(`${url}/rest/v1/library_topic_aliases?select=focus&limit=1`, {
  headers: { apikey: key!, authorization: `Bearer ${key}` },
}).then(async (r) => { await r.body?.cancel().catch(() => {}); return r.ok; }).catch(() => false);
if (!hasFocusColumn && focusByAlias.size > 0) console.log("Aviso: o banco ainda não tem a coluna focus; apelidos enviados sem foco.");
// Atualiza em lotes (upsert) e só depois apaga os que saíram: com dezenas de milhares de apelidos um
// envio único estoura o tempo do banco, e apagar tudo antes deixaria a biblioteca sem apelidos.
const aliasRows = [...aliasOwner].map(([alias_norm, topic_id]) =>
  hasFocusColumn ? { alias_norm, topic_id, focus: focusByAlias.get(alias_norm) ?? null } : { alias_norm, topic_id });
const aliasDiff = diff("library_topic_aliases", aliasRows as Record<string, unknown>[], (r) => String(r.alias_norm));
for (let i = 0; i < aliasDiff.send.length; i += 250) {
  await rest("library_topic_aliases?on_conflict=alias_norm", "POST", aliasDiff.send.slice(i, i + 250), "resolution=merge-duplicates,return=minimal");
}
let stale: string[] = aliasDiff.removed;
if (!aliasDiff.known) {
  // Sem histórico local: descobre no banco o que saiu (varredura completa, só desta vez).
  stale = [];
  for (let from = 0; ; from += 1000) {
    const page = await fetch(`${url}/rest/v1/library_topic_aliases?select=alias_norm&order=alias_norm`, {
      headers: { apikey: key!, authorization: `Bearer ${key}`, range: `${from}-${from + 999}` },
    }).then((r) => r.json()) as { alias_norm: string }[];
    for (const r of page) if (!aliasOwner.has(r.alias_norm)) stale.push(r.alias_norm);
    if (page.length < 1000) break;
  }
}
for (let i = 0; i < stale.length; i += 50) {
  const list = stale.slice(i, i + 50).map((a) => `"${a.replace(/"/g, '\\"')}"`).join(",");
  await rest(`library_topic_aliases?alias_norm=in.(${encodeURIComponent(list)})`, "DELETE");
}
aliasDiff.commit();
await saveCache();
// Em lotes: milhares de recortes num POST só estouram o tempo do banco.
const noteRows = notes.map((n) => ({
  topic_id: n.topic,
  board_norm: n.boardNorm,
  role_norm: n.roleNorm,
  board: n.value.board,
  role: n.value.role ?? null,
  version: n.value.version,
  note: n.value,
  origin: "MANUAL",
}));
const noteDiff = diff("library_board_notes", noteRows, (r) => `${r.topic_id}|${r.board_norm}|${r.role_norm}`);
if (!noteDiff.known) {
  // Sem histórico local: só os recortes escritos aqui (MANUAL) são refeitos; os automáticos ficam,
  // salvo quando um recorte revisado da mesma banca os substitui (upsert abaixo).
  await rest("library_board_notes?origin=eq.MANUAL", "DELETE");
} else {
  for (const removedKey of noteDiff.removed) {
    const [topic, board, role] = removedKey.split("|");
    await rest(`library_board_notes?origin=eq.MANUAL&topic_id=eq.${encodeURIComponent(topic)}&board_norm=eq.${encodeURIComponent(board)}&role_norm=eq.${encodeURIComponent(role)}`, "DELETE");
  }
}
for (let i = 0; i < noteDiff.send.length; i += 25) {
  await rest("library_board_notes?on_conflict=topic_id,board_norm,role_norm", "POST", noteDiff.send.slice(i, i + 25), "resolution=merge-duplicates,return=minimal");
}
noteDiff.commit();
await saveCache();
// Rotas revisadas (conteudo/pedidos/rotas.json): tópico amplo de edital servido por 2 ou 3 matérias
// juntas. Gravadas com o tamanho atual do catálogo, para o app não refazer a rota com IA.
try {
  const routes = JSON.parse(await Deno.readTextFile("conteudo/pedidos/rotas.json")) as { texto: string; ids: string[]; cobertura?: string }[];
  const routeRows = routes
    .filter((r) => r.ids.length > 0 && r.ids.every((id) => materials.has(id)))
    // "PARTIAL": as matérias cobrem só parte do tópico; o resto fica com a IA.
    .map((r) => ({ topic_norm: normalizeAlias(r.texto), subject_norm: "", topic_ids: r.ids, coverage: r.cobertura === "PARTIAL" ? "PARTIAL" : "FULL", catalog_size: materials.size, updated_at: new Date().toISOString() }))
    // Textos diferentes podem dar o mesmo topic_norm: fica uma linha por chave (o banco recusa repetida no mesmo envio).
    .filter((row, i, all) => all.findIndex((other) => other.topic_norm === row.topic_norm) === i);
  const routeDiff = diff("library_topic_routes", routeRows, (r) => String(r.topic_norm));
  for (let i = 0; i < routeDiff.send.length; i += 200) {
    await rest("library_topic_routes?on_conflict=topic_norm", "POST", routeDiff.send.slice(i, i + 200), "resolution=merge-duplicates,return=minimal");
  }
  routeDiff.commit();
  await saveCache();
} catch (error) {
  if (!(error instanceof Deno.errors.NotFound)) throw error;
}
// O catálogo também espelha o repositório: edital que saiu de conteudo/editais sai do app.
const catalogDiff = diff("exam_catalog", catalogRows as Record<string, unknown>[], (r) => String(r.id));
for (let i = 0; i < catalogDiff.send.length; i += 20) {
  await rest("exam_catalog?on_conflict=id", "POST", catalogDiff.send.slice(i, i + 20), "resolution=merge-duplicates,return=minimal");
}
// Apaga só os que saíram, em lotes: a lista inteira de ids no "not.in" estoura o tamanho da URL.
const keepSet = new Set(catalogRows.map((row) => row.id as string));
let staleCatalog: string[] = catalogDiff.removed;
if (!catalogDiff.known) {
  staleCatalog = [];
  for (let from = 0; ; from += 1000) {
    const page = await fetch(`${url}/rest/v1/exam_catalog?select=id&order=id`, {
      headers: { apikey: key!, authorization: `Bearer ${key}`, range: `${from}-${from + 999}` },
    }).then((r) => r.json()) as { id: string }[];
    for (const r of page) if (!keepSet.has(r.id)) staleCatalog.push(r.id);
    if (page.length < 1000) break;
  }
}
for (let i = 0; i < staleCatalog.length; i += 50) {
  const list = staleCatalog.slice(i, i + 50).map((id) => `"${id}"`).join(",");
  await rest(`exam_catalog?id=in.(${encodeURIComponent(list)})`, "DELETE");
}
catalogDiff.commit();
await saveCache();
// Simulados oficiais (conteudo/simulados/<edital>.simulado-N.json): a prova inteira, na ordem real,
// para o app usar como "Prova oficial" do edital. Banco ainda sem a tabela: pula com aviso.
const hasSimulationTable = await fetch(`${url}/rest/v1/exam_simulation?select=id&limit=1`, {
  headers: { apikey: key!, authorization: `Bearer ${key}` },
}).then(async (r) => { await r.body?.cancel().catch(() => {}); return r.ok; }).catch(() => false);
let simulationCount = 0;
if (!hasSimulationTable) console.log("Aviso: o banco ainda não tem a tabela exam_simulation; simulados oficiais não enviados.");
else {
  const simulationRows: Json[] = [];
  for await (const entry of Deno.readDir("conteudo/simulados")) {
    const match = entry.name.match(/^(.+)\.simulado-(\d+)\.json$/);
    if (!entry.isFile || !match) continue;
    const sim = JSON.parse(await Deno.readTextFile(`conteudo/simulados/${entry.name}`)) as Json;
    const catalogId = String(sim.edital ?? "");
    if (!keepSet.has(catalogId)) { console.log(`Aviso: ${entry.name} aponta para edital fora do catálogo (${catalogId}).`); continue; }
    const questions = (sim.questions as Json[]).map((q, index) => ({
      n: Number(q.n ?? index + 1),
      subject: String(q.disciplina ?? ""),
      topic: q.topico ? String(q.topico) : null,
      material: q.materia ? String(q.materia) : null,
      format: q.format === "CERTO_ERRADO" ? "CERTO_ERRADO" : "MULTIPLE_CHOICE",
      difficulty: ["FACIL", "MEDIA", "DIFICIL"].includes(String(q.difficulty)) ? q.difficulty : "MEDIA",
      statement: q.textoBase ? `${q.textoBase}\n\n${q.statement}` : String(q.statement),
      options: (q.options as Json[]).map((o) => ({ key: String(o.key), text: String(o.text), correct: o.correct === true })),
      explanation: String(q.explanation ?? ""),
      trap: q.pegadinha ? String(q.pegadinha) : null,
    }));
    const formats = new Set(questions.map((q) => q.format));
    simulationRows.push({
      id: `${match[1]}.simulado-${match[2]}`,
      catalog_id: catalogId,
      number: Number(match[2]),
      board: sim.banca ? String(sim.banca).slice(0, 80) : null,
      format: formats.size > 1 ? "MISTO" : formats.has("CERTO_ERRADO") ? "CERTO_ERRADO" : "MULTIPLA_ESCOLHA",
      structure: sim.estrutura ? String(sim.estrutura).slice(0, 1000) : null,
      item_count: questions.length,
      questions,
      status: "PUBLISHED",
      updated_at: new Date().toISOString(),
    });
  }
  const simulationDiff = diff("exam_simulation", simulationRows, (r) => String(r.id));
  for (const row of simulationDiff.send) {
    await rest("exam_simulation?on_conflict=id", "POST", [row], "resolution=merge-duplicates,return=minimal");
  }
  simulationDiff.commit();
  await saveCache();
  simulationCount = simulationRows.length;
}
const sent = Object.entries(nextCache).map(([table, rows]) => {
  const before = cache[table];
  const changed = before ? Object.entries(rows).filter(([k, h]) => before[k] !== h).length : Object.keys(rows).length;
  return `${table.replace(/^library_|^exam_/, "")} ${changed}`;
}).join(", ");
console.log(`Publicado: ${materials.size} matéria(s), ${aliasOwner.size} apelido(s), ${notes.length} recorte(s), ${catalogRows.length} edital(is) no catálogo, ${simulationCount} simulado(s) oficial(is).`);
if (onlyMark) console.log("Só registrado (nada enviado ao banco).");
else console.log(`Linhas enviadas nesta publicação (o resto já estava igual no banco): ${sent}.`);
