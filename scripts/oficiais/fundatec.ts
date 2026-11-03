// Questões reais da Fundatec. Para cada concurso (código N dos links "index_concursos.php?concurso=N" dos editais):
//  - lista das provas: provas_anteriores_ajax.php (function=2) → nome do PDF e código do cargo;
//  - PDF da prova: www.fundatec.org.br/home/portal/concursos/provas/<nome>;
//  - gabarito DEFINITIVO: o edital "Gabaritos Definitivos" da página de publicações (Anexo II, por código de cargo).
// Extrai as questões exatamente como estão (QUESTÃO NN – ... A) ... E) ...) e cruza pelo código do cargo.
// Não escreve nem altera questão nenhuma. O robots.txt da Fundatec não restringe; baixa devagar.
// Saída em work/provas-oficiais/fundatec/<N>/ (fora do git).
//   deno run -A scripts/oficiais/fundatec.ts 1055 [outro ...]   |   --todos
const ROOT = new URL("../../", import.meta.url).pathname.replace(/^\/([A-Za-z]:)/, "$1");
const SAIDA = `${ROOT}work/provas-oficiais/fundatec`;
const B = "https://www.fundatec.org.br/portal/concursos";
const UA = { "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 Chrome/120 Safari/537.36" };
const sleep = (ms: number) => new Promise((r) => setTimeout(r, ms));
type Json = Record<string, any>;

function concursosDosEditais(): string[] {
  const set = new Set<string>();
  for (const e of Deno.readDirSync(`${ROOT}conteudo/editais`)) {
    if (!e.name.endsWith(".json")) continue;
    const j = JSON.parse(Deno.readTextFileSync(`${ROOT}conteudo/editais/${e.name}`).replace(/^﻿/, ""));
    const m = String(j.url ?? "").match(/fundatec\.org\.br\/portal\/concursos\/index_concursos\.php\?concurso=(\d+)/i);
    if (m) set.add(m[1]);
  }
  return [...set].sort();
}
async function baixar(url: string, destino: string): Promise<boolean> {
  try { Deno.statSync(destino); return true; } catch { /* novo */ }
  const r = await fetch(url, { headers: UA });
  if (!r.ok || !(r.headers.get("content-type") ?? "").includes("pdf")) return false;
  Deno.writeFileSync(destino, new Uint8Array(await r.arrayBuffer()));
  await sleep(2500);
  return true;
}
const texto = (pdf: string, layout: boolean) => new TextDecoder().decode(new Deno.Command("pdftotext", { args: ["-enc", "UTF-8", ...(layout ? ["-layout"] : []), pdf, "-"], stdout: "piped", stderr: "piped" }).outputSync().stdout);
const latin1 = (b: ArrayBuffer) => new TextDecoder("latin1").decode(b);

// Anexo II: "41 - MÉDICO (PATOLOGIA)" e depois pares "01 - B" (A–E; * = anulada), até o próximo cargo.
function lerGabarito(t: string): Map<number, Map<number, string>> {
  const porCargo = new Map<number, Map<number, string>>();
  // a tabela começa na primeira página "ANEXO II  Gabaritos Deﬁnitivos" e continua nas seguintes (ﬁ vem como ligadura)
  const ini = t.search(/ANEXO II\s+Gabaritos De(ﬁ|fi)nitivos/);
  let atual: Map<number, string> | null = null;
  for (const l of t.slice(ini >= 0 ? ini : 0).split(/\r?\n/)) {
    const h = l.match(/^\s*(\d{1,3})\s+-\s+([A-ZÁÉÍÓÚÂÊÔÃÕÇ][^0-9]{3,})$/);
    if (h && !/\d{2}\s+-\s+[A-E*]\b/.test(l)) { atual = new Map(); porCargo.set(Number(h[1]), atual); continue; }
    if (!atual) continue;
    for (const m of l.matchAll(/\b(\d{2})\s+-\s+([A-E*])(?=\s|$)/g)) atual.set(Number(m[1]), m[2]);
  }
  return porCargo;
}

// "QUESTÃO 01 – enunciado ... A) ... B) ... C) ... D) ... E) ..." (texto-base do grupo vem antes)
function lerProva(t: string) {
  const blocos = t.split(/\n?QUESTÃO\s+(\d{1,3})\s*[–-]\s*/);
  const qs: Json[] = [];
  const lixo = /Execução: Fundatec|^NÍVEL |_BASE_|\d{2}\/\d{2}\/\d{4}|RASCUNHO|Assinatura|INSTRUÇÕES|CADERNO DE QUESTÕES/;
  // texto de leitura antes da QUESTÃO 01 (depois da capa e das instruções)
  let base = (blocos[0] ?? "").split(/\n\s*\n/).filter((b) => b.trim().length > 200 && !lixo.test(b)).join("\n\n");
  for (let i = 1; i < blocos.length; i += 2) {
    const n = Number(blocos[i]);
    const corpo = blocos[i + 1] ?? "";
    const p = corpo.split(/(?:^|\s)([A-E])\)\s+/);
    const letras = p.filter((_, k) => k % 2 === 1).slice(0, 5).join("");
    const alternativas: string[] = [];
    for (let k = 1; k < Math.min(p.length, 11); k += 2) alternativas.push((p[k + 1] ?? "").trim());
    // depois da alternativa E vem o resto da página: rodapé e, às vezes, o texto-base das próximas questões
    let sobra = "";
    if (alternativas.length === 5) {
      const [e, ...resto] = alternativas[4].split(/\n\s*\n/);
      alternativas[4] = e;
      sobra = resto.join("\n\n");
    }
    qs.push({ numero: n, base, enunciado: p[0].replace(/\s+/g, " ").trim(), alternativas: alternativas.map((a) => a.replace(/\s+/g, " ").trim()), ok: letras === "ABCDE" });
    const lixo = /Execução: Fundatec|^NÍVEL |_BASE_|\d{2}\/\d{2}\/\d{4}|RASCUNHO|Assinatura/;
    const util = sobra.split(/\n\s*\n/).filter((b) => b.trim().length > 200 && !lixo.test(b)).join("\n\n");
    if (util) base = util; // um texto longo depois da E é o texto-base das questões seguintes
  }
  return qs;
}

async function concurso(cd: string): Promise<string> {
  const lista = await (await fetch(`${B}/provas_anteriores_ajax.php`, { method: "POST", headers: { ...UA, "Content-Type": "application/x-www-form-urlencoded" }, body: `function=2&cd_concurso=${cd}` })).json().catch(() => ({ data: [] }));
  const provas: Json[] = lista.data ?? [];
  if (!provas.length) return `${cd}: sem provas publicadas`;
  const pub = latin1(await (await fetch(`${B}/pagina_editais.php?concurso=${cd}`, { headers: UA })).arrayBuffer());
  const gabUrl = [...pub.matchAll(/href="(https:\/\/concursos-publicacoes[^"]+\.pdf[^"]*)"/gi)].map((m) => m[1]).find((u) => /gabarito/i.test(u) && /definitiv/i.test(u));
  if (!gabUrl) return `${cd}: ${provas.length} provas, sem gabarito definitivo publicado`;
  const dir = `${SAIDA}/${cd}`;
  Deno.mkdirSync(dir, { recursive: true });
  if (!(await baixar(gabUrl, `${dir}/_gabarito_definitivo.pdf`))) return `${cd}: gabarito não baixou`;
  const gab = lerGabarito(texto(`${dir}/_gabarito_definitivo.pdf`, true));
  let ok = 0, total = 0;
  const avisos: string[] = [];
  for (const p of provas) {
    const nome = String(p.nome_prova);
    const cargo = Number(p.codigo);
    const resp = gab.get(cargo);
    if (!resp?.size) { avisos.push(`${nome}: cargo ${cargo} não está no gabarito definitivo`); continue; }
    if (!(await baixar(`https://www.fundatec.org.br/home/portal/concursos/provas/${encodeURIComponent(nome)}`, `${dir}/${nome}`))) { avisos.push(`${nome}: não baixou`); continue; }
    const qs: Json[] = lerProva(texto(`${dir}/${nome}`, false)).map((q: Json) => ({ ...q, gabarito: resp.get(q.numero), anulada: resp.get(q.numero) === "*", temFigura: /figura|gráfico|tabela|imagem|ilustra/i.test(q.enunciado),
      // depende de um texto de leitura que não foi capturado junto: fica de fora até a revisão
      precisaTexto: !q.base && /\btexto\b|\blinhas?\b|parágrafo|trecho|\(l\./i.test(q.enunciado) }));
    Deno.writeTextFileSync(`${dir}/${nome.replace(/\.pdf$/i, "")}.json`, JSON.stringify({
      banca: "Fundatec", concurso: cd, cargo: p.ds_cargo, codigoCargo: cargo, ano: Number(nome.match(/20\d\d/)?.[0]) || null,
      arquivo: nome, url: `https://www.fundatec.org.br/home/portal/concursos/provas/${nome}`, gabaritoUrl: gabUrl,
      questoesNoGabarito: resp.size, extraidas: qs.length, completas: qs.filter((q) => q.ok && q.gabarito).length, questoes: qs,
    }, null, 1));
    ok++; total += qs.filter((q) => q.ok && q.gabarito && !q.anulada && !q.precisaTexto).length;
    const c = qs.filter((q) => q.ok && q.gabarito).length;
    if (c < resp.size) avisos.push(`${nome}: ${c} de ${resp.size} completas`);
  }
  return `${cd}: ${ok} de ${provas.length} provas, ${total} questões` + (avisos.length ? "\n   " + avisos.slice(0, 8).join("\n   ") + (avisos.length > 8 ? `\n   (+${avisos.length - 8} avisos)` : "") : "");
}

// --catalogo: todos os concursos da lista de "Provas anteriores" do site (a mesma do campo Concurso), mais novos primeiro;
// seleção de estagiários fica de fora.
async function catalogo(): Promise<string[]> {
  const r = await fetch(`${B}/provas_anteriores_ajax.php`, { method: "POST", headers: { ...UA, "Content-Type": "application/x-www-form-urlencoded" }, body: "function=1" });
  const data: Json[] = (await r.json()).data ?? [];
  return data.filter((x) => !/estagi/i.test(`${x.ds_processo} ${x.ds_concurso}`)).sort((a, b) => Number(b.ano) - Number(a.ano)).map((x) => String(x.cd_concurso));
}
const lista = Deno.args.includes("--catalogo") ? await catalogo() : Deno.args.includes("--todos") ? concursosDosEditais() : Deno.args.filter((a) => !a.startsWith("--"));
if (!lista.length) { console.log("uso: deno run -A scripts/oficiais/fundatec.ts 1055 | --todos"); Deno.exit(1); }
for (const c of lista) {
  try { console.log(await concurso(c)); } catch (e) { console.log(`${c}: erro ${String((e as Error).message ?? e).slice(0, 150)}`); }
}
