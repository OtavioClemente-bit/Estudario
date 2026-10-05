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
      // O banco guarda apelidos de até 400 caracteres; tópico de edital mais longo que isso é
      // atendido pela enciclopédia (rota tópico → matérias), não pelo apelido.
      if (alias.length > 400) continue;
      const owner = aliasOwner.get(alias);
      if (owner && owner !== id) problems.push(`${path}: "${text}" aponta para ${id}, mas esse texto já é de ${owner}`);
      aliasOwner.set(alias, id);
    }
  }
}

console.log(`Matérias: ${materials.size} · Recortes: ${notes.length} · Apelidos: ${aliasOwner.size}`);
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

async function rest(path: string, method: string, body?: unknown, prefer = "return=minimal") {
  const response = await fetch(`${url}/rest/v1/${path}`, {
    method,
    headers: { apikey: key!, authorization: `Bearer ${key}`, "content-type": "application/json", prefer },
    body: body === undefined ? undefined : JSON.stringify(body),
  });
  if (!response.ok) throw new Error(`${method} ${path}: ${response.status} ${await response.text()}`);
  await response.body?.cancel().catch(() => {});
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
for (let i = 0; i < topicRows.length; i += 20) {
  await rest("library_topics?on_conflict=id", "POST", topicRows.slice(i, i + 20), "resolution=merge-duplicates,return=minimal");
}

// Apelidos e recortes espelham o repositório: o que saiu daqui sai do banco.
await rest("library_topic_aliases?alias_norm=not.is.null", "DELETE");
await rest("library_topic_aliases", "POST", [...aliasOwner].map(([alias_norm, topic_id]) => ({ alias_norm, topic_id })));
// Só os recortes escritos aqui (MANUAL) são refeitos; os automáticos ficam, salvo quando um
// recorte revisado da mesma banca os substitui (upsert abaixo).
await rest("library_board_notes?origin=eq.MANUAL", "DELETE");
if (notes.length > 0) {
  await rest("library_board_notes?on_conflict=topic_id,board_norm,role_norm", "POST", notes.map((n) => ({
    topic_id: n.topic,
    board_norm: n.boardNorm,
    role_norm: n.roleNorm,
    board: n.value.board,
    role: n.value.role ?? null,
    version: n.value.version,
    note: n.value,
    origin: "MANUAL",
  })), "resolution=merge-duplicates,return=minimal");
}
console.log(`Publicado: ${materials.size} matéria(s), ${aliasOwner.size} apelido(s), ${notes.length} recorte(s).`);
