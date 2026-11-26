// Conserto dos simulados do edital apontados pela auditoria de 26/11/2026
// (work/auditoria-simulados-2026-11-26/relatorio.md). Usa os próprios montadores (AGENTS.md, 4ª e 5ª exceções):
// só ESCOLHE questões já publicadas e conferidas; não escreve, não altera nem completa texto de questão.
//
// Para cada edital (catálogo), olha as provas em conteudo/simulados e marca como defeituosa a que tem:
//   - questão com nº de alternativas diferente do formato (5 em A–E, C/E em Certo/Errado);
//   - enunciado repetido dentro da prova ou de outra prova do mesmo edital (a de número maior é a refeita);
//   - disciplina acima da fatia da prova real (pesos-provas.json) ou, sem quadro, acima de 40% (ou da fatia igual);
//   - tamanho mais de 25% diferente da prova real (só editais com quadro em pesos-provas.json);
//   - questão de área básica (português, matemática, informática, idioma) numa disciplina que não fala dela.
// Refaz no lugar (mesmo id e número) só a prova montada por programa que ninguém fez (exam_simulation_results);
// prova escrita à mão (sem "origem"), prova já feita por alguém e as Cebraspe em A–E ficam como estão (listadas).
// Edital com mais de 3 provas: fica a prova escrita à mão como nº 1 e as de número 2 e 3 do edital; as outras
// recebem "status": "DRAFT" (saem do ar na publicação, sem apagar).
//
//   bash scripts/biblioteca/com-chave.sh scripts/simuladao/consertar.ts [--seco] [--edital <id>]
import { build } from "./montar.ts";
import { montar, prepararEdital, textoAproximado } from "./montar-primeiro.ts";
import { alternativasCertas, editaisComQuadro, MAX_PROVAS, normKey, topicoCabe, Usadas } from "./regras.ts";

type Json = Record<string, unknown>;
const SIM = "conteudo/simulados", ED = "conteudo/editais";
const dry = Deno.args.includes("--seco");
const only = Deno.args.includes("--edital") ? Deno.args[Deno.args.indexOf("--edital") + 1] : null;
const MAX_FALTA = 0.05;
const ler = (p: string) => JSON.parse(Deno.readTextFileSync(p).replace(/^﻿/, "")) as Json;
const gravar = (p: string, j: Json) => { if (!dry) Deno.writeTextFileSync(p, JSON.stringify(j, null, 2) + "\n"); };
const QUADRO = editaisComQuadro("");

// 1. Provas que alguém já fez: nunca mudam.
const U = Deno.env.get("SUPABASE_URL"), K = Deno.env.get("SUPABASE_SERVICE_ROLE_KEY");
if (!U || !K) throw new Error("Rode com scripts/biblioteca/com-chave.sh (precisa ver quem já fez cada prova).");
const feitas = new Set<string>();
for (let o = 0; ; o += 1000) {
  const r = await fetch(`${U}/rest/v1/exam_simulation_results?select=exam_simulation_id&offset=${o}&limit=1000`, { headers: { apikey: K, Authorization: `Bearer ${K}` } });
  const j = await r.json();
  if (!Array.isArray(j)) throw new Error("Não deu para ler exam_simulation_results: " + JSON.stringify(j));
  for (const x of j) feitas.add(String(x.exam_simulation_id));
  if (j.length < 1000) break;
}
console.log(`Provas já feitas por alguém: ${feitas.size}`);

type Prova = { id: string; path: string; numero: number; j: Json };
const provas: Prova[] = [];
for (const e of Deno.readDirSync(SIM)) {
  const m = e.name.match(/^(.+)\.simulado-(\d+)\.json$/);
  if (!m) continue;
  provas.push({ id: `${m[1]}.simulado-${m[2]}`, path: `${SIM}/${e.name}`, numero: Number(m[2]), j: ler(`${SIM}/${e.name}`) });
}
const catalogos = new Set([...Deno.readDirSync(ED)].map((f) => f.name.replace(/\.json$/, "")));

// pmerj-cfsd-2023.simulado-1 (prova escrita à mão do CFSD) apontava para pmerj-soldado-2023: o soldado ficava
// com 4 provas e o CFSD sem a nº 1. O edital do arquivo é o do nome do arquivo.
for (const p of provas) if (p.id === "pmerj-cfsd-2023.simulado-1" && p.j.edital !== "pmerj-cfsd-2023" && !feitas.has(p.id)) {
  p.j.edital = "pmerj-cfsd-2023"; gravar(p.path, p.j); console.log(`${p.id}: edital corrigido para pmerj-cfsd-2023`);
}

const escrita = (p: Prova) => p.j.montado !== true && !(p.j.questions as Json[]).some((q) => q.origem);
const formatoDe = (q: Json) => (q.format === "CERTO_ERRADO" ? "CERTO_ERRADO" : "MULTIPLE_CHOICE");
const cebraspeAE = (p: Prova) => /cebraspe|cespe/i.test(String(p.j.banca ?? "")) && (p.j.questions as Json[]).some((q) => formatoDe(q) === "MULTIPLE_CHOICE");
const publicada = (p: Prova) => p.j.status !== "DRAFT" && catalogos.has(String(p.j.edital ?? ""));

const porCat = new Map<string, Prova[]>();
for (const p of provas) if (publicada(p)) (porCat.get(String(p.j.edital)) ?? porCat.set(String(p.j.edital), []).get(String(p.j.edital))!).push(p);

const rel = { refeitas: [] as string[], tiradas: [] as string[], feitas: [] as string[], escritas: [] as string[], cebraspeAE: [] as string[], naoDeu: [] as string[], motivos: {} as Record<string, number> };
let feitosCat = 0, indiceCat = 0;
const parte = Deno.args.includes("--parte") ? Deno.args[Deno.args.indexOf("--parte") + 1].split("/").map(Number) : null;
const conta = (k: string) => (rel.motivos[k] = (rel.motivos[k] ?? 0) + 1);

/** Defeitos de uma prova (as outras provas do edital entram só na checagem de repetição). */
function defeitos(p: Prova, anteriores: Usadas, edital: Json | null, base: Prova | null): string[] {
  const d: string[] = [];
  const qs = p.j.questions as Json[];
  if (qs.some((q) => !alternativasCertas(q, formatoDe(q)))) d.push("alternativas");
  const vistas = new Set<string>();
  for (const q of qs) { const k = normKey(q.textoBase ? `${q.textoBase}\n\n${q.statement}` : q.statement); if (vistas.has(k)) { d.push("repetida na prova"); break; } vistas.add(k); }
  if (qs.some((q) => anteriores.has(q))) d.push("repetida no edital");
  const n = qs.length;
  const cont = new Map<string, number>();
  for (const q of qs) cont.set(String(q.disciplina ?? ""), (cont.get(String(q.disciplina ?? "")) ?? 0) + 1);
  const cat = String(p.j.edital);
  const ds = ((edital?.disciplinas as Json[]) ?? []);
  const real = ds.reduce((a, x) => a + (Number(x.questoes) || 0), 0);
  if (QUADRO.has(cat) && real >= 20) {
    if (Math.abs(n - real) / real > 0.25) d.push("tamanho");
    for (const [nome, v] of cont) { const r = Number(ds.find((x) => x.nome === nome)?.questoes) || 0; if (n >= 20 && v / n > Math.max(0.4, (r / real) * 1.25 + 0.05)) { d.push("disciplina domina"); break; } }
  } else if (base && escrita(base)) {
    // Estrutura da prova escrita à mão a partir da prova real: é ela a referência.
    const bc = new Map<string, number>(); for (const q of base.j.questions as Json[]) bc.set(String(q.disciplina), (bc.get(String(q.disciplina)) ?? 0) + 1);
    const bn = (base.j.questions as Json[]).length;
    for (const [nome, v] of cont) if (n >= 20 && v / n > Math.max(0.4, ((bc.get(nome) ?? 0) / bn) * 1.25 + 0.05)) { d.push("disciplina domina"); break; }
  } else {
    const teto = Math.max(0.4, 1 / Math.max(1, cont.size)) + 0.05;
    for (const [, v] of cont) if (n >= 20 && cont.size > 1 && v / n > teto) { d.push("disciplina domina"); break; }
    if (n >= 20 && cont.size === 1 && ds.filter((x) => ((x.topicos as Json[]) ?? []).some((t) => t.topico)).length > 1) d.push("disciplina domina");
  }
  if (edital) for (const q of qs) {
    const disc = ds.find((x) => x.nome === q.disciplina);
    if (!disc || !q.materia) continue;
    const textos = ((disc.topicos as Json[]) ?? []).filter((t) => t.topico === q.materia).map((t) => String(t.texto ?? "")).join(" ");
    if (!topicoCabe(String(disc.nome), textos, String(q.materia))) { d.push("disciplina errada"); break; }
  }
  return d;
}

for (const [cat, lista0] of [...porCat].sort((a, b) => a[0].localeCompare(b[0]))) {
  if (only && cat !== only) continue;
  // --parte i/n: só a i-ésima fatia dos editais (cada processo com memória limpa; o cache de recortes cresce muito).
  if (parte && (indiceCat++ % parte[1]) !== parte[0] - 1) continue;
  if (++feitosCat % 250 === 0) console.log(`… ${feitosCat}/${porCat.size} editais, ${rel.refeitas.length} refeitas`);
  let edital: Json | null = null; try { edital = ler(`${ED}/${cat}.json`); } catch { /* sem edital */ }
  let lista = lista0.sort((a, b) => a.numero - b.numero || Number(escrita(b)) - Number(escrita(a)));
  // 2. Mais de 3 provas: a escrita à mão vale como nº 1; depois as do próprio edital com nº 2 e 3.
  if (lista.length > MAX_PROVAS) {
    const fica: Prova[] = [];
    const n1 = lista.find((p) => p.numero === 1 && escrita(p)) ?? lista.find((p) => p.numero === 1);
    if (n1) fica.push(n1);
    for (let n = 2; n <= MAX_PROVAS; n++) { const p = lista.find((x) => x.numero === n && x.id === `${cat}.simulado-${n}`) ?? lista.find((x) => x.numero === n); if (p) fica.push(p); }
    for (const p of lista) if (!fica.includes(p)) {
      if (feitas.has(p.id)) { rel.feitas.push(`${p.id} (prova extra, já feita)`); fica.push(p); continue; }
      p.j.status = "DRAFT"; gravar(p.path, p.j); rel.tiradas.push(p.id);
    }
    lista = fica.sort((a, b) => a.numero - b.numero);
  }
  // 3. Defeitos, na ordem dos números (a repetida é a de número maior).
  const base = lista.find((p) => p.numero === 1) ?? null;
  const marcadas = new Map<Prova, string[]>();
  const ant = new Usadas();
  for (const p of lista) {
    const d = defeitos(p, ant, edital, base);
    for (const q of p.j.questions as Json[]) ant.add(q);
    if (d.length) marcadas.set(p, d);
  }
  if (!marcadas.size) continue;
  const podeRefazer = (p: Prova) => {
    if (feitas.has(p.id)) { rel.feitas.push(p.id); return false; }
    if (cebraspeAE(p)) { rel.cebraspeAE.push(`${p.id} · ${String(p.j.estrutura ?? "").slice(0, 140)}`); return false; }
    if (escrita(p)) { rel.escritas.push(`${p.id} (${marcadas.get(p)?.join(", ")})`); return false; }
    return true;
  };
  const refazer = new Set([...marcadas.keys()].filter(podeRefazer));
  // Prova 1 refeita muda a estrutura: as 2 e 3 (não feitas, montadas) vão junto.
  if (base && refazer.has(base)) for (const p of lista) if (p !== base && !feitas.has(p.id) && !escrita(p) && !cebraspeAE(p)) refazer.add(p);
  if (!refazer.size) continue;
  for (const p of refazer) for (const m of marcadas.get(p) ?? ["acompanha a prova 1"]) conta(m);
  const outras = (p: Prova) => { const u = new Usadas(); for (const x of lista) if (x !== p && !refazer.has(x)) for (const q of x.j.questions as Json[]) u.add(q); return u; };
  for (const p of [...refazer].sort((a, b) => a.numero - b.numero)) {
    let novo: Json | null = null, faltam = 0, total = 0;
    if (p === base) {
      const prep = edital ? prepararEdital(cat, edital) : null;
      if (prep) {
        const r = montar(cat, prep.e, 1, outras(p));
        if (prep.aproximado) r.file.estrutura = textoAproximado(String(r.file.estrutura));
        novo = r.file; faltam = r.faltam; total = r.total;
      }
    } else if (base) {
      // Depois de refeita a 1 (ou com a 1 escrita à mão), a 2 e a 3 seguem a estrutura dela; nada das outras provas entra.
      const others = lista.filter((x) => x !== p && x !== base).map((x) => x.j);
      const r = build(cat, base.j, others, p.numero);
      novo = r.file; faltam = r.missing; total = r.total;
    }
    if (!novo || total < 20 || faltam / total > MAX_FALTA) { rel.naoDeu.push(`${p.id} (${marcadas.get(p)?.join(", ") ?? "acompanha a 1"}; ${novo ? `faltam ${faltam} de ${total}` : "sem edital/estrutura"})`); continue; }
    novo.edital = cat; novo.numero = p.numero;
    p.j = novo; gravar(p.path, novo); rel.refeitas.push(p.id);
  }
}

console.log(`Refeitas: ${rel.refeitas.length} · tiradas do ar (DRAFT): ${rel.tiradas.length} · com defeito mas já feitas: ${rel.feitas.length} · escritas à mão (ficam): ${rel.escritas.length} · Cebraspe A–E (ficam): ${rel.cebraspeAE.length} · não deu para refazer: ${rel.naoDeu.length}`);
console.log("Motivos (provas refeitas):", JSON.stringify(rel.motivos));
Deno.mkdirSync("work/auditoria-simulados-2026-11-26", { recursive: true });
Deno.writeTextFileSync(`work/auditoria-simulados-2026-11-26/conserto${dry ? "-seco" : ""}${parte ? `-${parte[0]}de${parte[1]}` : ""}.json`, JSON.stringify(rel, null, 1));
