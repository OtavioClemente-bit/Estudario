// Questões reais da Fundatec. Para cada concurso (código N dos links "index_concursos.php?concurso=N" dos editais):
//  - lista das provas: provas_anteriores_ajax.php (function=2) → nome do PDF e código do cargo;
//  - PDF da prova: www.fundatec.org.br/home/portal/concursos/provas/<nome>;
//  - gabarito DEFINITIVO: o edital "Gabaritos Definitivos" da página de publicações (Anexo II, por código de cargo).
// Extrai as questões exatamente como estão (QUESTÃO NN – ... A) ... E) ...) e cruza pelo código do cargo.
// Não escreve nem altera questão nenhuma. O robots.txt da Fundatec não restringe; baixa devagar.
// Saída em work/provas-oficiais/fundatec/<N>/ (fora do git).
//   deno run -A scripts/oficiais/fundatec.ts 1055 [outro ...]   |   --todos
//   deno run -A scripts/oficiais/fundatec.ts --reprocessar [destino]   (refaz a extração dos PDFs já baixados, sem rede)
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
// Rodapé/cabeçalho de página da Fundatec: sai antes de separar as questões (antes ficava no meio do enunciado ou
// colado na alternativa E: "Execução: Fundatec", "15/09/2025 15:18:01", "1006_BASE_NS_DM").
const RODAPE_LINHA = /^(Execução: Fundatec|NÍVEL (SUPERIOR|MÉDIO|FUNDAMENTAL|TÉCNICO|ALFABETIZADO)\b.*|\d{3,4}_[A-Z0-9_]+|V\d+_\d{2}\/\d{2}\/\d{4}.*|\d{2}\/\d{2}\/\d{4} \d{2}:\d{2}:\d{2})$/;
// Título de seção ("LÍNGUA PORTUGUESA", "CONHECIMENTOS ESPECÍFICOS"): o texto-base de antes não vale depois dele.
const SECAO = /^[A-ZÁÉÍÓÚÂÊÔÃÕÇ][A-ZÁÉÍÓÚÂÊÔÃÕÇ ,/()–-]{4,80}$/;
const FIM = /[.;!?…)"”:]$/;
// "Para responder às questões 05 e 06" / "questões de 01 a 10": faixa do texto-base.
function faixa(base: string): [number, number] | null {
  const m = base.match(/quest(?:ões|oes)\s+(?:de\s+)?(\d{1,3})\s+(?:a|e|até)\s+(\d{1,3})/i);
  return m ? [Number(m[1]), Number(m[2])] : null;
}
function lerProva(t0: string) {
  const L = t0.split(/\r?\n/).map((l) => l.replace(/(^|\s)\d{2}\/\d{2}\/\d{4} \d{2}:\d{2}:\d{2}(?=\s|$)/g, " ").replace(/\s+$/, ""));
  const t = L.map((l) => RODAPE_LINHA.test(l.trim()) ? "" : l).join("\n");
  const blocos = t.split(/\n?QUESTÃO\s+(\d{1,3})\s*[–-]\s*/);
  const qs: Json[] = [];
  const lixo = /Execução: Fundatec|^NÍVEL |_BASE_|\d{2}\/\d{2}\/\d{4}|RASCUNHO|Assinatura|INSTRUÇÕES|CADERNO DE QUESTÕES|Transcreva a frase|Grade de Respostas|QR code/i;
  // Texto-base: a partir de "Para responder às questões..." / "Instrução:" (o texto de leitura costuma vir uma linha por
  // parágrafo, então juntam-se os pedaços curtos); depois de um título de seção, tudo o que vem; senão, só parágrafos longos.
  const util = (s: string, inteiro = false) => {
    const i = s.search(/(^|\n)\s*(Para responder|Instrução|Leia o texto)/i);
    const pars = (i >= 0 ? s.slice(i) : s).split(/\n\s*\n/).map((b) => b.trim()).filter((b) => b && !lixo.test(b));
    const t = i >= 0 || inteiro ? pars.join("\n") : pars.filter((b) => b.length > 200).join("\n\n");
    return t.length > 200 ? t : "";
  };
  // texto de leitura antes da QUESTÃO 01 (depois da capa e das instruções: vale o que vem depois do último título de seção)
  let base = "", fx: [number, number] | null = null;
  const novaBase = (raw: string, inteiro = false) => { base = util(raw, inteiro); fx = base ? faixa(raw) : null; };
  const L0 = (blocos[0] ?? "").split("\n");
  const iSec0 = L0.findLastIndex((l) => SECAO.test(l.trim()) && !/CONCURSO|INSTITUTO|UNIVERSIDADE|PREFEITURA|PROFESSOR/.test(l));
  novaBase(iSec0 >= 0 ? L0.slice(iSec0 + 1).join("\n") : L0.join("\n"), iSec0 >= 0);
  const vistos = new Set<number>();
  for (let i = 1; i < blocos.length; i += 2) {
    const n = Number(blocos[i]);
    // O PDF de alguns concursos traz o caderno Tipo 1 e depois o Tipo 2 (mesmas questões, outra ordem): o gabarito é
    // do Tipo 1, então número repetido é pulado (antes o Tipo 2 saía com o mesmo id e o gabarito do Tipo 1).
    if (vistos.has(n)) continue; // fica a 1ª ocorrência de cada número (caderno "COMPLETA" com prova 2 de 41 a 80 continua igual)
    vistos.add(n);
    const corpo = blocos[i + 1] ?? "";
    const p = corpo.split(/(?:^|\s)([A-E])\)\s+/);
    const letras = p.filter((_, k) => k % 2 === 1).slice(0, 5).join("");
    const alternativas: string[] = [];
    for (let k = 1; k < Math.min(p.length, 11); k += 2) alternativas.push((p[k + 1] ?? "").trim());
    // depois da alternativa E vem o resto da página: rodapé e, às vezes, o texto-base das próximas questões
    let sobra = "";
    if (alternativas.length === 5) {
      const [e0, ...resto] = alternativas[4].split(/\n\s*\n/);
      let e = e0;
      // alternativa E que quebrou de página (o rodapé saiu e deixou linha em branco): o pedaço curto seguinte, em minúscula, é dela
      while (resto.length && !FIM.test(e.trim()) && resto[0].trim().length < 300 && /^[a-zà-ú]/.test(resto[0].trim())) e += " " + resto.shift();
      alternativas[4] = e;
      sobra = resto.join("\n\n");
    }
    const b = !base || (fx && (n < fx[0] || n > fx[1])) ? "" : base;
    qs.push({ numero: n, base: b, enunciado: p[0].replace(/\s+/g, " ").trim(), alternativas: alternativas.map((a) => a.replace(/\s+/g, " ").trim()), ok: letras === "ABCDE" });
    // um título de seção encerra o texto-base anterior; um texto longo depois da E é o texto-base das seguintes
    const partes = sobra.split(/\n/);
    const iSec = partes.findIndex((l) => SECAO.test(l.trim()));
    if (iSec >= 0) { novaBase(partes.slice(iSec + 1).join("\n"), true); continue; }
    if (util(sobra)) novaBase(sobra);
  }
  return qs;
}
const marcar = (q: Json, resp: Map<number, string>) => ({ ...q, gabarito: resp.get(q.numero), anulada: resp.get(q.numero) === "*", temFigura: /figura|gráfico|tabela|imagem|ilustra/i.test(q.enunciado),
  // depende de um texto de leitura que não foi capturado junto: fica de fora até a revisão
  precisaTexto: !q.base && /\btexto\b|\blinhas?\b|parágrafo|trecho|\(l\./i.test(q.enunciado) });

// --reprocessar [destino]: refaz a extração dos PDFs já baixados (sem rede), com o gabarito local de cada concurso.
// Número, arquivo e concurso continuam os mesmos, então os ids não mudam.
function reprocessar(destino = SAIDA) {
  let provas = 0, antes = 0, depois = 0;
  for (const d of Deno.readDirSync(SAIDA)) {
    if (!d.isDirectory) continue;
    const dir = `${SAIDA}/${d.name}`;
    let gab: Map<number, Map<number, string>>;
    try { gab = lerGabarito(texto(`${dir}/_gabarito_definitivo.pdf`, true)); } catch { continue; }
    for (const f of Deno.readDirSync(dir)) {
      if (!f.name.endsWith(".json") || f.name.startsWith("_")) continue;
      let reg: Json; try { reg = JSON.parse(Deno.readTextFileSync(`${dir}/${f.name}`)); } catch { continue; }
      const resp = gab.get(Number(reg.codigoCargo));
      try { Deno.statSync(`${dir}/${reg.arquivo}`); } catch { continue; }
      if (!resp?.size) continue;
      const qs: Json[] = lerProva(texto(`${dir}/${reg.arquivo}`, false)).map((q) => marcar(q, resp));
      antes += (reg.questoes ?? []).length; depois += qs.length; provas++;
      reg.questoes = qs; reg.extraidas = qs.length; reg.completas = qs.filter((q) => q.ok && q.gabarito).length;
      Deno.mkdirSync(`${destino}/${d.name}`, { recursive: true });
      Deno.writeTextFileSync(`${destino}/${d.name}/${f.name}`, JSON.stringify(reg, null, 1));
    }
  }
  return { provas, antes, depois };
}
if (Deno.args.includes("--reprocessar")) {
  const destino = Deno.args.filter((a) => !a.startsWith("--"))[0];
  console.log(reprocessar(destino ? destino.replace(/\\/g, "/") : undefined));
  Deno.exit(0);
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
    const qs: Json[] = lerProva(texto(`${dir}/${nome}`, false)).map((q: Json) => marcar(q, resp));
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
