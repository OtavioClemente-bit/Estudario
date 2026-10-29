// Quando o Codex termina a lista de itens sem matéria, acrescenta ao fim de conteudo/fila/itens-sem-materia.tsv
// os próximos itens sem matéria que mais se repetem nos editais (só os que ainda não estão na lista).
//   deno run -A scripts/codex/proxima-lista.ts [quantos=300]
const ROOT = new URL("../../", import.meta.url).pathname.replace(/^\/([A-Za-z]:)/, "$1");
const LISTA = `${ROOT}conteudo/fila/itens-sem-materia.tsv`;
const QUANTOS = Number(Deno.args[0] ?? 300);
const MIN_EDITAIS = 3;
const norm = (s: string) => s.normalize("NFD").replace(/[̀-ͯ]/g, "").toLowerCase().replace(/[^a-z0-9]+/g, " ").trim();
const limpo = (s: string) => s.replace(/[\t\r\n]+/g, " ").trim();

const linhas = Deno.readTextFileSync(LISTA).trimEnd().split("\n");
const jaNaLista = new Set(linhas.slice(1).map((l) => norm(l.split("\t")[1] ?? "")));

const porTexto = new Map<string, { texto: string; disciplina: string; exemplo: string; editais: Set<string> }>();
for (const e of Deno.readDirSync(`${ROOT}conteudo/editais`)) {
  if (!e.name.endsWith(".json")) continue;
  const j = JSON.parse(Deno.readTextFileSync(`${ROOT}conteudo/editais/${e.name}`).replace(/^﻿/, ""));
  const slug = e.name.replace(/\.json$/, "");
  for (const d of j.disciplinas ?? []) for (const t of d.topicos ?? []) {
    if (t.topico) continue;
    const k = norm(String(t.texto ?? ""));
    if (k.length < 4 || jaNaLista.has(k)) continue;
    const it = porTexto.get(k) ?? { texto: limpo(String(t.texto)), disciplina: limpo(String(d.nome ?? "")), exemplo: slug, editais: new Set<string>() };
    it.editais.add(slug);
    porTexto.set(k, it);
  }
}
// Quando acabam os itens em 3+ editais, passa a aceitar os que aparecem em 2 (pedido do dono, 29/10).
let novos = [...porTexto.values()].filter((i) => i.editais.size >= MIN_EDITAIS);
if (!novos.length) novos = [...porTexto.values()].filter((i) => i.editais.size >= 2);
novos = novos.sort((a, b) => b.editais.size - a.editais.size).slice(0, QUANTOS);
if (novos.length) {
  Deno.writeTextFileSync(LISTA, linhas.join("\n") + "\n" + novos.map((i) => `${i.editais.size}\t${i.texto}\t${i.disciplina}\t${i.exemplo}`).join("\n") + "\n");
}
console.log(`${novos.length} itens novos acrescentados (lista agora com ${linhas.length - 1 + novos.length})`);
// Sai com 3 quando não há mais nada a acrescentar, para o .cmd saber que acabou.
if (!novos.length) Deno.exit(3);
