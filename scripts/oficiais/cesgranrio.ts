// Questões reais da Cesgranrio: o site oficial (www.cesgranrio.org.br) é WordPress; cada concurso é um item de
// /wp-json/wp/v2/concurso e a página lista os PDFs (hospedados em inscricao.cesgranrio.com.br). Baixa as provas
// objetivas e os gabaritos, extrai as questões (5 alternativas) exatamente como estão no PDF e cruza cada prova com o
// gabarito certo (pelo nome do arquivo e, nas provas com "Gabarito 1..5", pelo bloco do número). Não escreve nem altera
// questão nenhuma. O robots.txt dos dois endereços dá 404 (sem bloqueio); baixa devagar (2,5 s entre downloads).
// Saída em work/provas-oficiais/cesgranrio/<slug>/ (fora do git), no mesmo formato do fgv.ts (lido por preparar.ts).
//   deno run -A scripts/oficiais/cesgranrio.ts caixa-economica-federal-caixa-1-2021-nm [outro_slug ...]
//   deno run -A scripts/oficiais/cesgranrio.ts --catalogo     (todos os concursos do site; pula os já baixados)
const ROOT = new URL("../../", import.meta.url).pathname.replace(/^\/([A-Za-z]:)/, "$1");
const SAIDA = `${ROOT}work/provas-oficiais/cesgranrio`;
const SITE = "https://www.cesgranrio.org.br";
const STORAGE = "https://inscricao.cesgranrio.com.br/storage.ashx?file=";
const HEADERS = { "User-Agent": "EstudarioColetor/1.0", "Accept-Encoding": "identity" }; // sem isso a API às vezes devolve versão enxuta (sem o conteúdo da página)
const sleep = (ms: number) => new Promise((r) => setTimeout(r, ms));
const norm = (s: string) => s.normalize("NFD").replace(/[̀-ͯ]/g, "").toLowerCase().replace(/[^a-z0-9]+/g, " ").trim();
type Json = Record<string, any>;

const entidades = (s: string) => s.replace(/&#038;|&amp;/g, "&").replace(/&#8211;|&#8212;/g, "-").replace(/&#8217;/g, "'").replace(/&#(\d+);/g, (_, n) => String.fromCharCode(+n)).replace(/&nbsp;/g, " ");
const semTags = (s: string) => entidades(s.replace(/<[^>]+>/g, " ")).replace(/\s+/g, " ").trim();

async function baixar(url: string, destino: string): Promise<boolean> {
  try { if (Deno.statSync(destino).size > 0) return true; } catch { /* novo */ }
  const r = await fetch(url, { headers: HEADERS });
  const buf = new Uint8Array(await r.arrayBuffer());
  await sleep(2500);
  // o servidor responde 200 com uma página HTML quando o arquivo não existe
  if (!r.ok || String.fromCharCode(...buf.slice(0, 5)) !== "%PDF-") return false;
  Deno.writeFileSync(destino, buf);
  return true;
}
function texto(pdf: string, layout: boolean): string {
  const o = new Deno.Command("pdftotext", { args: ["-enc", "UTF-8", ...(layout ? ["-layout"] : []), pdf, "-"], stdout: "piped", stderr: "null" }).outputSync();
  return new TextDecoder().decode(o.stdout);
}

// ---------------------------------------------------------------- gabarito: "12 - C" / "33 - Anulada", em blocos "GABARITO k"
type Bloco = { resp: Map<number, string>; conflito: boolean };
function lerGabarito(t: string): Map<string, Bloco> {
  const blocos = new Map<string, Bloco>();
  let chave = "";
  for (const l0 of t.split(/\r?\n/)) {
    const l = l0.trim();
    const h = l.match(/^GABARITO\s*(\d)$/i);
    if (h) { chave = h[1]; continue; }
    for (const m of l.matchAll(/(?<![\d/])(\d{1,3})\s*-\s*(Anulada|[A-E])(?![A-Za-zÀ-ú])/gi)) {
      const b = blocos.get(chave) ?? blocos.set(chave, { resp: new Map(), conflito: false }).get(chave)!;
      const n = Number(m[1]), r = m[2].length > 1 ? "ANULADA" : m[2].toUpperCase();
      if (b.resp.has(n) && b.resp.get(n) !== r) b.conflito = true;
      b.resp.set(n, r);
    }
  }
  return blocos;
}

// ---------------------------------------------------------------- prova: número, enunciado, (A)..(E)
const RODAPE = /^\d{1,3}\s+(?=.*[A-ZÁÉÍÓÚÂÊÔÃÕÇ]{4})[A-ZÁÉÍÓÚÂÊÔÃÕÇ0-9 –\-\/()]+$/; // "4 TÉCNICO BANCÁRIO NOVO"
function lerProva(t: string, total: number) {
  const L = t.split(/\r?\n/).map((l) => l.trim());
  const freq = new Map<string, number>();
  for (const l of L) if (l) freq.set(l, (freq.get(l) ?? 0) + 1);
  const linhas = L.filter((l) => !(l && (((freq.get(l) ?? 0) >= 4 && !/^\(?[A-E]\)/.test(l) && l.length < 80) || RODAPE.test(l) || /^RASCUNHO$/.test(l))));
  type Q = { numero: number; raw: string[]; seenE: boolean; fechada: boolean };
  const qs: Q[] = [];
  let cur: Q | null = null;
  // Prova em duas colunas sai fora de ordem: aceita cada número (1..total) uma vez, em qualquer ordem, quando a linha
  // começa com ele, a questão anterior já terminou e logo adiante aparecem (A) e (E).
  const vistos = new Set<number>();
  for (let i = 0; i < linhas.length; i++) {
    const l = linhas[i];
    const m = l.match(/^([1-9]\d{0,2})\s+(?![-–]\s)(\S.*)$/);
    const n = m ? Number(m[1]) : 0, adiante = linhas.slice(i, i + 40).join(" ");
    if (m && n <= total && !vistos.has(n) && (!cur || cur.seenE) && /(A)/.test(adiante) && /(E)/.test(adiante)) {
      cur = { numero: n, raw: [m[2]], seenE: /(E)/.test(m[2]), fechada: false };
      vistos.add(n); qs.push(cur); continue;
    }
    if (!cur || cur.fechada) continue;
    if (l === "") { if (cur.seenE) cur.fechada = true; else cur.raw.push(""); continue; }
    cur.raw.push(l);
    if (/\(E\)/.test(l)) cur.seenE = true;
  }
  return qs.map((q) => {
    const bruto = q.raw.join("\n").replace(/([a-zà-ú])-\n([a-zà-ú])/g, "$1$2");
    const partes = bruto.split(/\s*\(([A-E])\)\s+/);
    const alternativas: string[] = [];
    for (let k = 1; k < partes.length; k += 2) alternativas.push((partes[k + 1] ?? "").replace(/\s+/g, " ").trim());
    const letras = partes.filter((_, k) => k % 2 === 1).join("");
    return { numero: q.numero, enunciado: partes[0].replace(/\s+/g, " ").trim(), alternativas, ok: letras === "ABCDE" && alternativas.every(Boolean) && partes[0].length < 2500 && !q.raw.some((l) => /^[\d.,]+\s+\([A-E]\)/.test(l)) }; // número colado antes de "(A)" = fração/expoente que o PDF desmontou
  });
}

// ---------------------------------------------------------------- pareamento prova x gabarito pelo nome do arquivo
const PARADAS = new Set(["prova", "gabarito", "gabaritos", "alterado", "alterada", "alterados", "alteradas", "apos", "recursos", "recurso", "final", "em", "de", "pdf",
  "janeiro", "fevereiro", "marco", "abril", "maio", "junho", "julho", "agosto", "setembro", "outubro", "novembro", "dezembro", "gab"]);
function fichaNome(nome: string) {
  const n = norm(nome);
  const k = n.match(/gabarito (\d)\b/)?.[1] ?? null;
  const tokens = new Set(n.replace(/gabarito \d\b/g, " ").split(" ").filter((w) => w && !PARADAS.has(w) && !/^\d{2,}$/.test(w)));
  return { k, tokens, alterado: /alterad|apos recurso|final/.test(n) };
}
const jaccard = (a: Set<string>, b: Set<string>) => { const i = [...a].filter((x) => b.has(x)).length; return i / Math.max(1, a.size + b.size - i); };

type Link = { url: string; nome: string; rotulo: string };
function listaLinks(html: string): Link[] {
  const out: Link[] = [], vistos = new Set<string>();
  for (const m of html.matchAll(/<a [^>]*href="([^"]+)"[^>]*>([\s\S]*?)<\/a>/g)) {
    let h = entidades(m[1]);
    if (h.startsWith("[#storage#]")) h = STORAGE + h.slice(11);
    else if (h.startsWith("/")) h = SITE + h;
    if (!/^https?:/.test(h) || !/\.pdf$/i.test(h.split("?").pop()!)) continue;
    const nome = decodeURIComponent(h.split("/").pop()!);
    const rotulo = semTags(m[2]);
    if (!/prova|gabarit|caderno/i.test(nome + " " + rotulo)) continue;
    if (/resultado|aprovad|edital|padr[aã]o|discursiv|isen[cç]|t[ií]tulo|rela[cç][aã]o|locais|pedidorevisao|aviso|instru|minimopontos|desvio/i.test(nome)) continue;
    if (vistos.has(h)) continue;
    vistos.add(h); out.push({ url: new URL(h).href, nome, rotulo });
  }
  return out;
}

async function concurso(slug: string): Promise<string> {
  const r = await fetch(`${SITE}/wp-json/wp/v2/concurso?per_page=100`, { headers: HEADERS });
  if (!r.ok) return `${slug}: API ${r.status}`;
  const itens = await r.json();
  const achado = itens.filter((x: Json) => x.slug === slug);
  if (!achado.length) return `${slug}: concurso não existe no site`;
  await sleep(1500);
  return await processar(achado[0]);
}

async function processar(c: Json): Promise<string> {
  const slug: string = c.slug, nomeConcurso = semTags(c.title.rendered);
  if (!c.content?.rendered) throw new Error("API devolveu a versão enxuta (sem conteúdo)");
  const links = listaLinks(c.content.rendered);
  if (!links.length) return `${slug}: sem provas/gabaritos em PDF na página`;
  const dir = `${SAIDA}/${slug}`;
  Deno.mkdirSync(dir, { recursive: true });
  const avisos: string[] = [];
  const provas: { link: Link; arq: string; t: string }[] = [], gabs: { link: Link; blocos: Map<string, Bloco> }[] = [];
  const usados = new Set<string>();
  for (const lk of links) {
    let arq = lk.nome.replace(/[^\w.\-À-ú ]+/g, "_");
    if (usados.has(arq)) arq = lk.url.split("/").slice(-2, -1)[0] + "__" + arq;
    usados.add(arq);
    if (!(await baixar(lk.url, `${dir}/${arq}`))) { avisos.push(`${lk.nome}: não baixou`); continue; }
    const lay = texto(`${dir}/${arq}`, true);
    const nA = (lay.match(/\([A-E]\)/g) ?? []).length;
    const blocos = lerGabarito(lay);
    const nG = [...blocos.values()].reduce((s, b) => s + b.resp.size, 0);
    if (nG >= 15 && nA < 10) gabs.push({ link: lk, blocos });
    else if (nA >= 15) provas.push({ link: lk, arq, t: texto(`${dir}/${arq}`, false) });
  }
  if (!provas.length || !gabs.length) return `${slug}: ${provas.length} provas, ${gabs.length} gabaritos${avisos.length ? "\n   " + avisos.join("\n   ") : ""}`;

  let ok = 0, itens = 0;
  const jaVistas = new Set<string>();
  for (const p of provas) {
    const f = fichaNome(p.link.nome + " " + p.link.rotulo.replace(/gabarito/gi, ""));
    const fk = fichaNome(p.link.nome);
    const k = fk.k ?? p.t.slice(0, 1500).match(/GABARITO\s*(\d)\b/i)?.[1] ?? null;
    // candidatos: maior semelhança de nome; empate entre "alterado" e original fica com o alterado; empate entre iguais não arrisca
    const cand = gabs.map((g) => {
      const fg = fichaNome(g.link.nome);
      return { g, s: jaccard(fk.tokens, fg.tokens), alt: fg.alterado, bloco: g.blocos.get(k ?? "") ?? (g.blocos.size === 1 && !k ? [...g.blocos.values()][0] : undefined) };
    }).filter((x) => x.s > 0 && x.bloco).sort((a, b) => b.s - a.s || Number(b.alt) - Number(a.alt));
    void f;
    if (!cand.length) { avisos.push(`${p.link.nome}: não achei o gabarito com certeza`); continue; }
    if (cand.length > 1 && cand[0].s === cand[1].s && cand[0].alt === cand[1].alt) { avisos.push(`${p.link.nome}: gabarito ambíguo`); continue; }
    const esc = cand[0], resp = esc.bloco!.resp;
    if (esc.bloco!.conflito) { avisos.push(`${p.link.nome}: gabarito com respostas conflitantes`); continue; }
    const qs = lerProva(p.t, resp.size);
    const boas = qs.filter((q) => q.ok);
    if (boas.length < resp.size * 0.5) { avisos.push(`${p.link.nome}: ${boas.length} de ${resp.size} completas (não grava)`); continue; }
    const chaves = boas.map((q) => norm(q.enunciado).slice(0, 100));
    if (chaves.filter((x) => jaVistas.has(x)).length > chaves.length / 2) { avisos.push(`${p.link.nome}: mesma prova de outra versão (pulada)`); continue; }
    chaves.forEach((x) => jaVistas.add(x));
    const capa = p.t.split(/\r?\n/).slice(0, 40).join(" ");
    const ano = Number(capa.match(/realizada em:?\s*\d\d\/\d\d\/(20\d\d)/i)?.[1] ?? capa.match(/20\d\d/)?.[0] ?? slug.match(/20\d\d/)?.[0]) || null;
    const cargo = p.t.split(/\r?\n/).map((l) => l.trim()).filter(Boolean).slice(0, 3).join(" - ").slice(0, 160);
    const saida = qs.map((q) => {
      const g = resp.get(q.numero);
      return { ...q, gabarito: g && g !== "ANULADA" ? g : undefined, anulada: g === "ANULADA" || !g, temFigura: /figura|gr[áa]fico|tabela|imagem|foto|ilustra|charge|tirinha/i.test(q.enunciado) || q.enunciado.length < 15 };
    });
    Deno.writeTextFileSync(`${dir}/${p.arq.replace(/\.pdf$/i, "")}.json`, JSON.stringify({
      banca: "Cesgranrio", concurso: slug, nomeConcurso, ano, cargo, prova: p.link.rotulo, arquivo: p.arq, url: p.link.url, gabaritoUrl: esc.g.link.url,
      gabaritoTipo: esc.alt ? "alterado/após recursos" : "original", questoesNoGabarito: resp.size, extraidas: qs.length, completas: boas.length, questoes: saida,
    }, null, 1));
    ok++; itens += saida.filter((q) => q.ok && !q.anulada).length;
  }
  return `${slug}: ${ok} de ${provas.length} provas (${gabs.length} gabaritos), ${itens} questões completas` + (avisos.length ? "\n   " + avisos.join("\n   ") : "");
}

// --catalogo: lista /wp-json/wp/v2/concurso (um item por concurso), sem vestibulares, pulando os já baixados.
async function catalogo() {
  const todos: Json[] = [];
  for (let p = 1; p < 20; p++) {
    const r = await fetch(`${SITE}/wp-json/wp/v2/concurso?per_page=100&page=${p}`, { headers: HEADERS });
    if (!r.ok) break;
    const a = await r.json();
    todos.push(...a);
    if (a.length < 100) break;
    await sleep(1500);
  }
  for (const c of todos) {
    if (/vestibular/i.test(c.slug)) continue;
    let feito = false;
    try { feito = [...Deno.readDirSync(`${SAIDA}/${c.slug}`)].some((e) => e.name.endsWith(".json")); } catch { /* novo */ }
    if (feito) { console.log(`${c.slug}: já baixado`); continue; }
    try { console.log(await processar(c)); } catch (e) { console.log(`${c.slug}: erro ${String((e as Error).message ?? e).slice(0, 150)}`); }
  }
}

if (Deno.args.includes("--catalogo")) await catalogo();
else {
  const lista = Deno.args.filter((a) => !a.startsWith("--"));
  if (!lista.length) { console.log("uso: deno run -A scripts/oficiais/cesgranrio.ts <slug> | --catalogo"); Deno.exit(1); }
  for (const s of lista) {
    try { console.log(await concurso(s)); } catch (e) { console.log(`${s}: erro ${String((e as Error).message ?? e).slice(0, 150)}`); }
  }
}
