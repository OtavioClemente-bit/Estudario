// Sexta exceção do AGENTS.md (27/11/2026, autorizada pelo dono): matéria do ChatGPT já revisada (amostra/web) que
// saiu duplicada de uma matéria publicada. As QUESTÕES da duplicada são SOMADAS à publicada (cópia literal, sem
// mudar uma palavra; enunciado repetido fica de fora) e a TEORIA da publicada só é trocada pela da duplicada se a
// nova for bem maior (≥ 1,5× o texto dos capítulos). Id, título, escopo e editais da publicada não mudam.
// Nada é escrito, completado nem embaralhado: só se copia texto que já passou pela revisão. Se a matéria somada não
// passar no conferidor (--estrito), fica como estava.
//   deno run -A scripts/biblioteca/somar-duplicadas.ts [--gravar]
import { validateLibraryMaterial, normalizeAlias } from "../../supabase/functions/_shared/library.ts";
const ROOT = new URL("../../", import.meta.url).pathname.replace(/^\/([A-Za-z]:)/, "$1");
const ENTRADA = `${ROOT}conteudo/entrada/chatgpt`;
const FEITAS = `${ROOT}work/duplicadas-somadas.json`;
const gravar = Deno.args.includes("--gravar");
type J = Record<string, any>;
const ler = (p: string) => JSON.parse(Deno.readTextFileSync(p));
const teoria = (m: J) => (m.chapters ?? []).reduce((a: number, c: J) => a + String(c.markdown ?? "").length, 0);
const chave = (s: string) => String(s ?? "").normalize("NFD").replace(/[̀-ͯ]/g, "").toLowerCase().replace(/[^a-z0-9]+/g, " ").trim().slice(0, 90);

let feitas: Record<string, string> = {};
try { feitas = ler(FEITAS); } catch { /* nenhuma */ }

// Índice das publicadas por apelido e título normalizados.
const porApelido = new Map<string, string>();
for (const d of Deno.readDirSync(`${ROOT}conteudo/materias`)) {
  if (!d.isDirectory) continue;
  for (const e of Deno.readDirSync(`${ROOT}conteudo/materias/${d.name}`)) {
    if (!e.name.endsWith(".json")) continue;
    const p = `${ROOT}conteudo/materias/${d.name}/${e.name}`;
    try { const m = ler(p); for (const a of [m.title, ...(m.aliases ?? [])]) porApelido.set(normalizeAlias(String(a)), p); } catch { /* pula */ }
  }
}
const duplicadas = Deno.readTextFileSync(`${ROOT}work/duplicadas.txt`).split(/\s+/).filter(Boolean);
let somadas = 0, trocadas = 0, novasQ = 0, falhas = 0;
const relatorio: string[] = [];
for (const id of [...new Set(duplicadas)]) {
  let nova: J;
  try { nova = ler(`${ENTRADA}/${id}.json`); } catch { continue; }
  if (feitas[id]) continue;
  const alvo = [nova.title, ...(nova.aliases ?? [])].map((a) => porApelido.get(normalizeAlias(String(a)))).find(Boolean);
  if (!alvo) continue;
  const velha: J = ler(alvo);
  const m: J = structuredClone(velha);
  const trocar = teoria(nova) >= 1.5 * teoria(velha);
  if (trocar) {
    for (const k of ["theoryTitle", "chapters", "summary", "flashcards", "tips", "traps", "activeRecall"]) if (nova[k] != null) m[k] = nova[k];
    m.sources = [...(velha.sources ?? []), ...(nova.sources ?? [])];
  }
  // Conceitos de erro: união (as questões novas apontam para os da nova).
  const conceitos = new Map<string, J>();
  for (const c of [...(velha.errorConcepts ?? []), ...(nova.errorConcepts ?? [])]) if (c?.key && !conceitos.has(c.key)) conceitos.set(c.key, c);
  m.errorConcepts = [...conceitos.values()];
  const vistos = new Set((velha.questions ?? []).map((q: J) => chave(q.statement)));
  const extras = (nova.questions ?? []).filter((q: J) => { const k = chave(q.statement); if (vistos.has(k)) return false; vistos.add(k); return true; });
  m.questions = [...(velha.questions ?? []), ...extras];
  // "section" é só a ligação da questão com um capítulo (não é texto da questão): se o capítulo citado não existe na
  // matéria somada, liga ao capítulo de título mais parecido (palavras em comum).
  const ajustarSecoes = () => {
    const titulos: string[] = (m.chapters ?? []).map((c: J) => String(c.title));
    const pal = (s: string) => new Set(chave(s).split(" ").filter((w) => w.length > 3));
    m.questions = m.questions.map((q: J) => {
      if (titulos.includes(q.section)) return q;
      const p = pal(q.section ?? "");
      let melhor = titulos[0], nota = -1;
      for (const t of titulos) { const n = [...pal(t)].filter((w) => p.has(w)).length; if (n > nota) { nota = n; melhor = t; } }
      return { ...q, section: melhor };
    });
  };
  ajustarSecoes();
  // Apelido da nova que já é de outra matéria publicada derrubava a publicação (27/11): fica de fora.
  const deOutra = (a: string) => { const d = porApelido.get(normalizeAlias(a)); return d !== undefined && d !== alvo; };
  m.aliases = [...new Set([...(velha.aliases ?? []), ...(nova.aliases ?? []).filter((a: string) => !deOutra(a))])];
  m.version = (Number(velha.version) || 1) + 1;
  let ok = true;
  try { validateLibraryMaterial(m); } catch (e) { ok = false; relatorio.push(`✗ ${alvo.replace(ROOT, "")}: ${((e as J).problems ?? [String(e)]).slice(0, 2).join(" | ")}`); }
  if (!ok && trocar) {
    // Tenta só somar as questões, mantendo a teoria antiga.
    for (const k of ["theoryTitle", "chapters", "summary", "flashcards", "tips", "traps", "activeRecall", "sources"]) m[k] = velha[k];
    m.questions = [...(velha.questions ?? []), ...extras];
    ajustarSecoes();
    try { validateLibraryMaterial(m); ok = true; relatorio.push(`  ↳ ok só somando questões`); } catch { /* segue falha */ }
  }
  if (!ok) { falhas++; continue; }
  const trocou = m.chapters === nova.chapters;
  somadas++; if (trocou) trocadas++; novasQ += extras.length;
  relatorio.push(`${trocou ? "T" : "·"} ${alvo.replace(`${ROOT}conteudo/materias/`, "")}: ${velha.questions?.length ?? 0}+${extras.length} questões${trocou ? `, teoria ${teoria(velha)}→${teoria(nova)}` : ""}`);
  if (gravar) {
    Deno.writeTextFileSync(alvo, JSON.stringify(m, null, 2) + "\n");
    feitas[id] = alvo.replace(ROOT, "");
  }
}
if (gravar) Deno.writeTextFileSync(FEITAS, JSON.stringify(feitas, null, 1));
console.log(relatorio.join("\n"));
console.log(`\n${somadas} matérias somadas (${trocadas} com teoria trocada), +${novasQ} questões; ${falhas} ficaram como estavam${gravar ? "" : " (simulação; use --gravar)"}`);
