// Questões reais do Instituto AOCP: para cada concurso do portal (link.institutoaocp.org.br/api/concursos) abre o
// "Visualizar Cadernos de Questões e Gabarito Definitivo" (portal público, sem login), baixa a prova objetiva
// (TIPO 1) de cada cargo e lê o GABARITO DEFINITIVO exibido na mesma página; extrai as questões (5 alternativas)
// exatamente como estão no PDF oficial (pdftotext). Não escreve nem altera questão nenhuma. Anuladas (X) ficam
// marcadas, questões com destaque/figura também. Respeita robots.txt (o do AOCP dá 404 = sem restrição), User-Agent
// genérico e 2-3 s entre downloads. Saída em work/provas-oficiais/aocp/<slug>/ (fora do git).
//   deno run -A scripts/oficiais/aocp.ts pmpe2023 [outro_slug ...]    (slug = campo "edital" da API, ex.: pmdf23chos)
//   deno run -A scripts/oficiais/aocp.ts --catalogo                    (todos os concursos do portal, pula os já baixados)
//   deno run -A scripts/oficiais/aocp.ts --reprocessar [destino]           (refaz a extração dos PDFs já baixados, sem rede)
const ROOT = new URL("../../", import.meta.url).pathname.replace(/^\/([A-Za-z]:)/, "$1");
const SAIDA = `${ROOT}work/provas-oficiais/aocp`;
const API = "https://link.institutoaocp.org.br/api/concursos";
const HEADERS = { "User-Agent": "EstudarioColetor/1.0" };
const sleep = (ms: number) => new Promise((r) => setTimeout(r, ms + Math.random() * 800));
type Json = Record<string, any>;

function texto(pdf: string): string {
  const o = new Deno.Command("pdftotext", { args: ["-enc", "UTF-8", pdf, "-"], stdout: "piped", stderr: "piped" }).outputSync();
  return new TextDecoder().decode(o.stdout);
}
async function get(url: string, jar: Map<string, string>, init: RequestInit = {}): Promise<Response> {
  let u = url, r!: Response;
  for (let i = 0; i < 5; i++) {
    const cookie = [...jar].map(([k, v]) => `${k}=${v}`).join("; ");
    r = await fetch(u, { ...init, redirect: "manual", headers: { ...HEADERS, ...(cookie ? { Cookie: cookie } : {}), ...(init.headers ?? {}) } });
    for (const c of r.headers.getSetCookie()) { const m = c.match(/^([^=]+)=([^;]*)/); if (m) jar.set(m[1], m[2]); }
    const loc = r.headers.get("location");
    if (r.status >= 300 && r.status < 400 && loc) { u = new URL(loc, u).href; init = {}; await r.body?.cancel(); continue; }
    break;
  }
  return r;
}

// ---------------------------------------------------------------- catálogo
async function listaConcursos(): Promise<Json[]> {
  const r = await fetch(API, { headers: HEADERS });
  if (!r.ok) throw new Error(`API ${r.status}`);
  return await r.json();
}

// ---------------------------------------------------------------- gabarito (HTML do portal)
// Cada linha: <strong>número</strong> e <strong>letra</strong>; anulada aparece como <label ...>X</label>.
function lerGabarito(html: string): Map<number, string> {
  const g = new Map<number, string>();
  const i = html.indexOf("<strong>Gabarito</strong>");
  if (i < 0) return g;
  for (const b of html.slice(i).split("border-bottom: solid").slice(1)) {
    const n = b.match(/<strong>\s*(\d+)\s*<\/strong>/);
    const v = b.match(/<strong>\s*([A-E])\s*<\/strong>\s*<\/div>/) ?? b.match(/<label[^>]*>\s*([^<\s]+)\s*<\/label>/);
    if (n && v) g.set(Number(n[1]), v[1].toUpperCase());
  }
  return g;
}

// ---------------------------------------------------------------- questões
const RODAPE = /^(Tipo\s*\d+\s*[–-]\s*P[áa]gina\s*\d+|INSTITUTO AOCP|P[áa]gina\s*\d+(\s*de\s*\d+)?)$/i;
const FIGURA = /\b(figura|gr[áa]fico|tabela|imagem|foto|ilustra|charge|tirinha|mapa|fluxograma|organograma|planta|esquema|quadro)\b|em destaque|destacad[ao]s?|sublinhad[ao]s?|grifad[ao]s?|negrito|it[áa]lico/i;
function lerProva(t: string, total: number) {
  const L = t.replace(/\f/g, "\n").split(/\r?\n/).map((l) => l.trim());
  // Rodapés/cabeçalhos repetidos em toda página (cargo, sigla do órgão) saem; o número da questão fica.
  const freq = new Map<string, number>();
  for (const l of L) if (l) freq.set(l, (freq.get(l) ?? 0) + 1);
  let blocoVisto = false;
  const limpo: string[] = [];
  for (let i = 0; i < L.length; i++) {
    const l = L[i];
    // "BLOCO I" + nome da disciplina (linha curta) são cabeçalho de seção, não texto da questão
    if (/^BLOCO\s+[IVX\d]+\b/i.test(l)) { if (L[i + 1] && L[i + 1].length < 60 && !/[.:;]$/.test(L[i + 1])) i++; if (!blocoVisto) { limpo.length = 0; blocoVisto = true; } continue; }
    if (RODAPE.test(l) || /^-{20,}/.test(l)) continue;
    if (l && (freq.get(l) ?? 0) >= 4 && l.length < 90 && !/^\(?[A-E]\)/.test(l) && !/^\d+$/.test(l)) continue;
    if (!l && limpo[limpo.length - 1] === "") continue;
    limpo.push(l);
  }
  // Questão n começa na linha que é só o número n. Algumas provas trazem questões fora de ordem (27 antes de 26):
  // aceita número ainda não visto até 3 de distância do último, para não confundir com números soltos no texto.
  const blocos: { numero: number; linhas: string[]; antes: string[] }[] = [];
  const vistos = new Set<number>();
  let ultimo = 0, pre: string[] = [];
  for (const l of limpo) {
    const n = /^[0-9]{1,3}$/.test(l) ? Number(l) : 0;
    if (n >= 1 && n <= total && !vistos.has(n) && Math.abs(n - ultimo) <= 3) {
      vistos.add(n); ultimo = n;
      blocos.push({ numero: n, linhas: [], antes: pre }); pre = []; continue;
    }
    if (blocos.length) blocos[blocos.length - 1].linhas.push(l); else pre.push(l);
  }
  const qs: Json[] = [];
  let base = "", corridos = 0;
  blocos.forEach((b, idx) => {
    // Separa o que vem depois da alternativa (E): continuação da alternativa até a 1ª linha em branco; o resto
    // (se longo) é o texto-base das questões seguintes.
    const iA = b.linhas.findIndex((l) => /^\(A\)/.test(l));
    const enun = (iA < 0 ? b.linhas : b.linhas.slice(0, iA)).join(" ").replace(/\s+/g, " ").trim();
    let alt = iA < 0 ? [] : b.linhas.slice(iA);
    let sobra = "";
    const iE = alt.findIndex((l) => /^\(E\)/.test(l));
    if (iE >= 0) {
      // fim da (E): linha em branco ou título "TEXT 2"/"Texto 3" do texto-base seguinte, quando vem colado sem linha em branco
      let fim = alt.findIndex((l, k) => k > iE && (l === "" || /^TEXT[O]?\s*([IVX]+|\d+)?\s*$/i.test(l)));
      if (fim < 0) fim = alt.length;
      const resto = alt.slice(fim).join(" ").replace(/\s+/g, " ").trim();
      if (resto.length >= 150 && idx < blocos.length - 1) { sobra = alt.slice(fim).filter(Boolean).join("\n"); alt = alt.slice(0, fim); }
      else if (idx === blocos.length - 1) alt = alt.slice(0, fim); // depois da última: redação/instruções
    }
    const partes = alt.filter((l) => l !== "" || true).join(" ").split(/\s*\(([A-E])\)\s+/);
    const alternativas: string[] = [];
    for (let k = 1; k < partes.length; k += 2) alternativas.push((partes[k + 1] ?? "").replace(/\s+/g, " ").trim());
    const letras = partes.filter((_, k) => k % 2 === 1).join("");
    const antes = b.antes.filter(Boolean).join("\n").trim();
    if (antes.length >= 150) { base = antes; corridos = 0; }
    const q: Json = { numero: b.numero, enunciado: enun, alternativas, ok: letras === "ABCDE" && alternativas.every(Boolean) };
    // fração desmontada pelo PDF: o denominador sobra no fim da alternativa ("(0, 16). 3")
    if (alternativas.some((a) => /[.)]\s+\d{1,3}$/.test(a))) q.extracaoRuim = true;
    if (base && corridos < 10) { q.base = base; corridos++; }
    qs.push(q);
    if (sobra.length >= 150) { base = sobra; corridos = 0; }
  });
  return qs.sort((a, b) => a.numero - b.numero);
}

// ---------------------------------------------------------------- um concurso
async function concurso(slug: string, lista: Json[]): Promise<string> {
  const c = lista.find((x) => x.edital === slug);
  if (!c) return `${slug}: não está na lista do portal`;
  const dir = `${SAIDA}/${slug}`;
  const r0 = await fetch(`${API}/${c.id}`, { headers: HEADERS });
  if (!r0.ok) return `${slug}: detalhe ${r0.status}`;
  const det = (await r0.json())[0] as Json;
  await sleep(1500);
  const link = (det.links ?? []).find((l: Json) => /gabarito (definitivo|p[óo]s-recurso)/i.test(l.nome) && /caderno|prova/i.test(l.nome));
  if (!link) return `${slug}: sem link de caderno + gabarito definitivo/pós-recurso (${c.status})`;
  const jar = new Map<string, string>();
  const rp = await get(link.url, jar);
  const pagina = rp.url, htmlSel = await rp.text();
  const cargos = [...htmlSel.matchAll(/<option\s*(?:SELECTED)?\s*value="(\d+)">([^<]*)<\/option>/gi)].filter((m) => m[2].trim() && !/selecione/i.test(m[2]))
    .filter((m) => /^\s*\d+\s*-/.test(m[2]) || true);
  const tiposSel = [...(htmlSel.match(/id="tipo_prova"[\s\S]*?<\/select>/)?.[0] ?? "").matchAll(/value="(\d+)"/g)].map((m) => m[1]);
  // só as opções do select de cargo (as do tipo_prova também casam o regex acima)
  const selCargo = htmlSel.match(/id="cargo"[\s\S]*?<\/select>/)?.[0] ?? "";
  const lc = [...selCargo.matchAll(/value="(\d+)"[^>]*>([^<]*)<\/option>/gi)].map((m) => ({ id: m[1], nome: m[2].replace(/\s+/g, " ").replace(/\s*-\s*$/, "").trim() }));
  void cargos;
  if (!lc.length || !tiposSel.length) return `${slug}: portal sem cargos/provas (${lc.length} cargos)`;
  Deno.mkdirSync(dir, { recursive: true });
  const ano = Number(String(c.numeroEdital ?? "").match(/20\d\d/)?.[0] ?? slug.match(/20\d\d/)?.[0] ?? (slug.match(/\D(\d\d)\D*$/)?.[1] ? "20" + slug.match(/\D(\d\d)\D*$/)![1] : "")) || null;
  let ok = 0, itens = 0;
  const avisos: string[] = [];
  for (const cg of lc) {
    const base = `${dir}/${cg.id}-tipo1`;
    try { Deno.statSync(`${base}.json`); ok++; continue; } catch { /* novo */ }
    const tipo = tiposSel.includes("1") ? "1" : tiposSel[0];
    const rr = await get(pagina, jar, { method: "POST", body: new URLSearchParams({ cargo: cg.id, tipo_prova: tipo }), headers: { "Content-Type": "application/x-www-form-urlencoded" } });
    const html = await rr.text();
    await sleep(1500);
    const pdfUrl = html.match(/<iframe\s+src=([^\s>]+)/i)?.[1]?.replace(/&amp;/g, "&");
    const gab = lerGabarito(html);
    if (!pdfUrl || !gab.size) { avisos.push(`${cg.nome}: ${pdfUrl ? "sem" : "sem prova e"} gabarito no portal`); continue; }
    const rd = await fetch(pdfUrl, { headers: HEADERS });
    if (!rd.ok) { avisos.push(`${cg.nome}: PDF ${rd.status}`); continue; }
    Deno.writeFileSync(`${base}.pdf`, new Uint8Array(await rd.arrayBuffer()));
    await sleep(2200);
    const t = texto(`${base}.pdf`);
    const qs = lerProva(t, gab.size);
    const saida = qs.map((q): Json => {
      const g = gab.get(q.numero);
      return { ...q, gabarito: g, anulada: !/^[A-E]$/.test(g ?? ""), temFigura: FIGURA.test(q.enunciado) || q.alternativas.some((a: string) => FIGURA.test(a)) };
    });
    const completas = saida.filter((q) => q.ok).length;
    Deno.writeTextFileSync(`${base}.json`, JSON.stringify({
      banca: "Instituto AOCP", concurso: slug, nomeConcurso: String(c.nome).trim(), cargo: cg.nome, ano, prova: `${cg.nome} – TIPO ${tipo}`,
      arquivo: `${cg.id}-tipo1.pdf`, url: pdfUrl.split("?")[0], gabaritoUrl: link.url,
      questoesNoGabarito: gab.size, extraidas: saida.length, completas, questoes: saida,
    }, null, 1));
    ok++; itens += saida.filter((q) => q.ok && !q.anulada && !q.temFigura).length;
    if (completas < gab.size) avisos.push(`${cg.nome}: ${completas} de ${gab.size} completas`);
  }
  if (!avisos.some((a) => /PDF \d|portal/.test(a))) Deno.writeTextFileSync(`${dir}/_concluido.json`, JSON.stringify({ em: new Date().toISOString(), cargos: lc.length }));
  return `${slug} (${String(c.nome).trim()}): ${ok} de ${lc.length} cargos, ${itens} questões completas` + (avisos.length ? "\n   " + avisos.join("\n   ") : "");
}

// --reprocessar [destino]: refaz a extração dos PDFs já baixados (sem rede). O gabarito do AOCP vem da página do portal
// (não fica arquivo local): usa o que já está gravado em cada questão. Número, arquivo e concurso não mudam (ids iguais).
if (Deno.args.includes("--reprocessar")) {
  const destino = (Deno.args.filter((a) => !a.startsWith("--"))[0] ?? SAIDA).replace(/\\/g, "/");
  let provas = 0, antes = 0, depois = 0;
  for (const d of Deno.readDirSync(SAIDA)) {
    if (!d.isDirectory) continue;
    for (const f of Deno.readDirSync(`${SAIDA}/${d.name}`)) {
      if (!f.name.endsWith(".json") || f.name.startsWith("_")) continue;
      let reg: Json; try { reg = JSON.parse(Deno.readTextFileSync(`${SAIDA}/${d.name}/${f.name}`)); } catch { continue; }
      const pdf = `${SAIDA}/${d.name}/${reg.arquivo}`;
      try { Deno.statSync(pdf); } catch { continue; }
      const gab = new Map<number, string>((reg.questoes ?? []).filter((q: Json) => q.gabarito).map((q: Json) => [q.numero, q.gabarito]));
      const qs = lerProva(texto(pdf), Number(reg.questoesNoGabarito) || gab.size).map((q): Json => {
        const g = gab.get(q.numero);
        return { ...q, gabarito: g, anulada: !/^[A-E]$/.test(g ?? ""), temFigura: FIGURA.test(q.enunciado) || q.alternativas.some((a: string) => FIGURA.test(a)) };
      });
      antes += (reg.questoes ?? []).length; depois += qs.length; provas++;
      reg.questoes = qs; reg.extraidas = qs.length; reg.completas = qs.filter((q) => q.ok).length;
      Deno.mkdirSync(`${destino}/${d.name}`, { recursive: true });
      Deno.writeTextFileSync(`${destino}/${d.name}/${f.name}`, JSON.stringify(reg, null, 1));
    }
  }
  console.log({ provas, antes, depois });
  Deno.exit(0);
}
const args = Deno.args.filter((a) => !a.startsWith("--"));
if (!args.length && !Deno.args.includes("--catalogo")) { console.log("uso: deno run -A scripts/oficiais/aocp.ts pmpe2023 | --catalogo"); Deno.exit(1); }
const lista = await listaConcursos();
let slugs = args;
if (Deno.args.includes("--catalogo")) {
  slugs = lista.filter((x) => x.status !== "SUBSCRIBE" && x.edital).map((x) => String(x.edital));
  slugs = slugs.filter((s) => { try { Deno.statSync(`${SAIDA}/${s}/_concluido.json`); return false; } catch { return true; } });
  console.log(`${slugs.length} concursos a processar`);
}
for (const s of slugs) {
  try { console.log(await concurso(s, lista)); } catch (e) { console.log(`${s}: erro ${String((e as Error).message ?? e).slice(0, 150)}`); }
  await sleep(1500);
}
