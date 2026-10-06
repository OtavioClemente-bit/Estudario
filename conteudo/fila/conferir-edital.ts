// Confere um arquivo de edital gerado para o catálogo (formato de conteudo/fila/PEDIDO-EDITAIS.md).
// Só lê: aponta o que precisa ser corrigido e não altera nada.
//   deno run --allow-read conteudo/fila/conferir-edital.ts conteudo/entrada/edital-<nome>.json [...]

type Json = Record<string, unknown>;
const norm = (s: string) => s.normalize("NFD").replace(/[̀-ͯ]/g, "").toLowerCase().replace(/[^a-z0-9]+/g, " ").trim();

let failed = false;
for (const path of Deno.args) {
  const problems: string[] = [];
  const warnings: string[] = [];
  const raw = await Deno.readFile(path);
  if (raw[0] === 0xef && raw[1] === 0xbb && raw[2] === 0xbf) problems.push("arquivo com BOM: grave em UTF-8 sem BOM");
  let e: Json;
  try {
    e = JSON.parse(new TextDecoder().decode(raw).replace(/^﻿/, ""));
  } catch (error) {
    console.log(`✗ ${path}\n   JSON inválido: ${(error as Error).message}`);
    failed = true;
    continue;
  }

  const name = path.split(/[\\/]/).pop()!.replace(/^edital-/, "").replace(/\.json$/, "");
  if (!/^[a-z0-9]+(-[a-z0-9]+)*(--[a-z0-9]+(-[a-z0-9]+)*)?$/.test(name)) problems.push(`nome do arquivo "${name}" fora do padrão orgao-cargo-ano`);

  for (const field of ["concurso", "orgao", "cargo"]) {
    if (typeof e[field] !== "string" || !(e[field] as string).trim()) problems.push(`falta "${field}"`);
  }
  if (e.ano !== undefined && (typeof e.ano !== "number" || e.ano < 2000 || e.ano > 2035)) problems.push(`"ano" deve ser número (ex.: 2025)`);
  if (e.url !== undefined && !String(e.url).startsWith("http")) problems.push(`"url" deve ser um link (http...) ou sair do arquivo`);
  for (const field of ["banca", "referencia", "ano"]) if (e[field] === undefined) warnings.push(`sem "${field}" (tudo bem se não houver certeza)`);

  const synonyms = Array.isArray(e.sinonimos) ? (e.sinonimos as unknown[]).map(String) : [];
  if (synonyms.length < 4 || synonyms.length > 15) problems.push(`"sinonimos" precisa de 4 a 15 termos (tem ${synonyms.length})`);
  synonyms.forEach((s) => { if (s !== s.toLowerCase() || s !== s.normalize("NFD").replace(/[̀-ͯ]/g, "")) problems.push(`sinônimo "${s}" deve ser minúsculo e sem acento`); });

  const subjects = Array.isArray(e.disciplinas) ? e.disciplinas as Json[] : [];
  if (subjects.length === 0) problems.push("sem disciplinas");
  let topicCount = 0;
  const subjectNames = new Set<string>();
  for (const s of subjects) {
    const subjectName = String(s.nome ?? "").trim();
    if (!subjectName) problems.push("disciplina sem nome");
    if (subjectNames.has(norm(subjectName))) problems.push(`disciplina repetida: ${subjectName}`);
    subjectNames.add(norm(subjectName));
    const topics = Array.isArray(s.topicos) ? s.topicos as Json[] : [];
    if (topics.length === 0) problems.push(`${subjectName}: sem tópicos`);
    const seen = new Set<string>();
    for (const t of topics) {
      topicCount++;
      const text = String(t.texto ?? "").trim();
      const where = `${subjectName} › "${text.slice(0, 60)}${text.length > 60 ? "…" : ""}"`;
      if (text.length < 3 || text.length > 400) problems.push(`${where}: texto deve ter de 3 a 400 caracteres`);
      if (/^\s*(\d+(\.\d+)*[.)-]?|[a-z]\))\s/i.test(text)) problems.push(`${where}: tire a numeração do começo`);
      if (text && text[0] !== text[0].toUpperCase()) warnings.push(`${where}: comece com maiúscula`);
      if (text && !/[.?!)]$/.test(text)) warnings.push(`${where}: termine com ponto`);
      if (t.topico !== null) problems.push(`${where}: "topico" deve ser null (o Claude faz a ligação)`);
      if (seen.has(norm(text))) problems.push(`${where}: tópico repetido na disciplina`);
      seen.add(norm(text));
      // Item que junta vários assuntos costuma ter três ou mais partes separadas por ";".
      if (text.split(";").length >= 4) warnings.push(`${where}: parece juntar vários assuntos; separe em tópicos se forem matérias diferentes`);
    }
  }

  const status = problems.length ? "✗" : "✓";
  console.log(`${status} ${path}  (${subjects.length} disciplinas, ${topicCount} tópicos)`);
  problems.forEach((p) => console.log(`   ✗ ${p}`));
  warnings.forEach((w) => console.log(`   · ${w}`));
  if (problems.length) failed = true;
}
if (Deno.args.length === 0) console.log("Uso: deno run --allow-read conteudo/fila/conferir-edital.ts <arquivo.json> [...]");
Deno.exit(failed ? 1 : 0);
