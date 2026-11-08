// Questões reais do Cebraspe: baixa a prova objetiva e o gabarito DEFINITIVO de cada concurso e extrai os itens
// exatamente como estão no PDF oficial. Não escreve nem altera questão nenhuma: só copia o texto e cruza com o gabarito.
// Os PDFs e o texto extraído ficam em work/provas-oficiais/cebraspe/<CONCURSO>/ (fora do git).
//   deno run -A scripts/oficiais/cebraspe.ts ABIN_17 [OUTRO_CONCURSO ...]
//   deno run -A scripts/oficiais/cebraspe.ts --todos        (todos os concursos do Cebraspe que estão nos editais)
//   deno run -A scripts/oficiais/cebraspe.ts --reprocessar [destino]   (refaz a extração dos PDFs já baixados)
// Precisa do pdftotext (vem com o Git para Windows).
const ROOT = new URL("../../", import.meta.url).pathname.replace(/^\/([A-Za-z]:)/, "$1");
const SAIDA = `${ROOT}work/provas-oficiais/cebraspe`;
const API = "https://apis.cebraspe.org.br/cebraspe/eventos/";
const CDN = "https://cdn.cebraspe.org.br/concursos/";
const HEADERS = { "User-Agent": "Mozilla/5.0", "Referer": "https://www.cebraspe.org.br/" };
const sleep = (ms: number) => new Promise((r) => setTimeout(r, ms));
type Json = Record<string, any>;

// ---------------------------------------------------------------- quais concursos
function concursosDosEditais(): string[] {
  const set = new Set<string>();
  for (const e of Deno.readDirSync(`${ROOT}conteudo/editais`)) {
    if (!e.name.endsWith(".json")) continue;
    const j = JSON.parse(Deno.readTextFileSync(`${ROOT}conteudo/editais/${e.name}`).replace(/^﻿/, ""));
    const m = String(j.url ?? "").match(/cebraspe\.org\.br\/concursos\/([^/]+)/i);
    if (m) set.add(m[1]);
  }
  return [...set].sort();
}

// ---------------------------------------------------------------- baixar
async function baixar(url: string, destino: string): Promise<boolean> {
  try { Deno.statSync(destino); return true; } catch { /* ainda não baixado */ }
  const r = await fetch(url, { headers: HEADERS });
  if (!r.ok) return false;
  Deno.writeFileSync(destino, new Uint8Array(await r.arrayBuffer()));
  await sleep(2000); // devagar: não sobrecarrega o site
  return true;
}
function pdfParaTexto(pdf: string, layout: boolean): string {
  const args = ["-enc", "UTF-8", ...(layout ? ["-layout"] : []), pdf, "-"];
  const o = new Deno.Command("pdftotext", { args, stdout: "piped", stderr: "piped" }).outputSync();
  return new TextDecoder().decode(o.stdout);
}

// ---------------------------------------------------------------- gabarito definitivo
// Tabela "Item 51 52 ..." / "Gabarito E C ..." (X = anulado).
export function lerGabarito(texto: string): Map<number, string> {
  const g = new Map<number, string>();
  const linhas = texto.split(/\r?\n/);
  for (let i = 0; i < linhas.length; i++) {
    const m = linhas[i].match(/^\s*Item\s+(.+)$/);
    if (!m) continue;
    const nums = m[1].trim().split(/\s+/).map(Number);
    let j = i + 1;
    while (j < linhas.length && !/^\s*Gabarito\s/.test(linhas[j])) j++;
    if (j >= linhas.length) break;
    const vals = linhas[j].replace(/^\s*Gabarito\s+/, "").trim().split(/\s+/);
    nums.forEach((n, k) => { if (n > 0 && vals[k] && vals[k] !== "0") g.set(n, vals[k].toUpperCase()); });
    i = j;
  }
  return g;
}

// ---------------------------------------------------------------- itens da prova
// Cabeçalho de página colado ao texto ("CEBRASPE – CGE/RJ – Edital: 2023 Certo pensador..."): tira só o cabeçalho.
// Antes a linha inteira era descartada e o texto-base ou o fim de item que vinha colado nela se perdia.
const CABECALHO_COLADO = /^CEBRASPE\s*[–-][^\n]{0,80}?(?:Edital|Aplicação):\s*\d{4}\s*/i;
// Fim de frase: item terminado assim está completo; sem isso, as linhas seguintes ainda são dele.
const FIM_FRASE = /[.?!;:)\]”"»…]$/;
// Início de item que não é letra nem número: fórmula (fontes de símbolo saem como glifos fora do latim).
const INICIO_SIMBOLO = /^[^\sa-zà-úA-ZÀ-Ú0-9.,;:!?)\]}\-–—]/;
// Início de item que é conta com número ("96 38±1,7 representa..."): número seguido de operador. Só vale para o
// número logo seguinte ao item anterior (a numeração de linha dos textos não tem operador depois).
const INICIO_CONTA = /^\d[\d.,]*\s*[±=<>≤≥+×÷−\-/%^]\s*\S/;
// Item que remete a algo visual que não vem no texto (circuito, esquema, box-plot "a seguir"): fica marcado
// precisaFigura e o preparar.ts o deixa de fora. Nunca se completa.
const VISUAL = new RegExp("\\b(quadros?|esquemas?|diagramas?|circuitos?|plantas?|croquis?|box-?plots?|histogramas?|fluxogramas?|organogramas?|" +
  "mapas?|desenhos?|modelos?|telas?|grafos?|árvores?|matriz|matrizes|planilhas?|fotografias?|cartogramas?|perfis?)\\b(?:\\s+[^\\s.;:]+){0,4}?\\s+" +
  "(a seguir|seguintes?|abaixo|acima|precedentes?|apresentad[oa]s?|mostrad[oa]s?|representad[oa]s?|esquematizad[oa]s?)\\b", "i");
const REPRESENTADO = /\b(representad[oa]s?|ilustrad[oa]s?|mostrad[oa]s?|esquematizad[oa]s?|desenhad[oa]s?)\s+(a seguir|abaixo|acima|na (figura|imagem))\b/i;
// Colunas misturadas pelo pdftotext: frase que acaba e continua em minúscula com o texto da outra coluna
// ("...foi inferior a 5. de 150 propriedades rurais..."). Abreviações (art., spp., pv.) e código não contam.
const MISTURA = /(?<!\.|\b(?:art|arts|n|nº|inc|al|p|pp|fl|fls|ex|etc|Dr|Sr|Sra|cf|v|vol|ed|op|cit|Ltda|min|máx|aprox|séc|spp|sp|subsp|pv|var|cv|[A-Za-z]))\.\s+([a-zà-ú]{2,}(?:\s+[a-zà-ú,]+){3})/;
function misturado(s: string): boolean {
  const m = s.match(MISTURA);
  if (!m) return false;
  const antes = s.slice(Math.max(0, m.index! - 40), m.index!), depois = s.slice(m.index!, m.index! + 80);
  return !(/a seguir|seguinte|abaixo|:\s*$/.test(antes) || /[{}\/;=<>_|$#\\]|--|\w\(\)/.test(depois));
}
const CABECALHO = [/^\|\|.*\|\|$/, /^CESPE\s*\|\s*CEBRASPE/i, /^CEBRASPE\s*[–-]/i, /^CONHECIMENTOS (BÁSICOS|ESPECÍFICOS|GERAIS)\s*$/i, /^Espaço livre$/i, /^-- \d+ --$/];
const COMANDO = /julgue|assinale|considerando|com base|acerca|a respeito|à luz|tendo em vista|no que (se refere|concerne|diz respeito)|em relação|quanto a/i;
type Item = { numero: number; base: string; enunciado: string };
export function lerProva(texto: string, numeros: Set<number>): Item[] {
  const itens: Item[] = [];
  // Os textos de leitura numeram as linhas na margem (1, 4, 7...): só vale como item o PRÓXIMO número do gabarito,
  // e só se a linha começa como frase (maiúscula, aspas ou parêntese), não no meio de uma frase do texto.
  const ordem = [...numeros].sort((a, b) => a - b);
  let base = "", bloco: string[] = [];
  const vistos = new Set<number>();
  const MAIUSC = /^[A-ZÁÉÍÓÚÂÊÔÃÕÀÇ"“'(«¿¡]/;
  // Item colado no fim da linha anterior (ex.: "...<= 2); 70 Na passagem..."): separa em duas linhas.
  const linhas: string[] = [];
  for (const bruta of texto.split(/\r?\n/)) {
    let l = bruta.trim().replace(CABECALHO_COLADO, "").replace(/\s+Espaço livre$/i, "");
    for (;;) {
      const i = ordem.findIndex((n) => n > 0 && new RegExp(`\\S\\s${n}\\s+[A-ZÁÉÍÓÚÂÊÔÃÕÀÇ"“]`).test(l));
      if (i < 0) break;
      const k = l.search(new RegExp(`\\s${ordem[i]}\\s+[A-ZÁÉÍÓÚÂÊÔÃÕÀÇ"“]`));
      linhas.push(l.slice(0, k).trim());
      l = l.slice(k).trim();
      if (!new RegExp(`\\S\\s\\d{1,3}\\s+[A-Z]`).test(l)) break;
    }
    linhas.push(l);
  }
  // Item que começa com fórmula ("104 ... a 0,9. 105 ܲሺܺ ൐ 0ሻ ൌ 0,9."): só separa o número logo seguinte ao
  // item que abre a linha, e só depois de fim de frase, para não confundir com números do próprio texto.
  // (o item pode ter quebrado de linha: vale o último número de item que abriu uma linha antes desta.)
  let n = 0;
  for (let i = 0; i < linhas.length; i++) {
    const m = linhas[i].match(/^(\d{1,3})\s/);
    if (m && numeros.has(+m[1]) && +m[1] > n) n = +m[1];
    if (!n) continue;
    while (numeros.has(n + 1)) {
      const k = linhas[i].search(new RegExp(`(?<=[.?!)”"]\\s)${n + 1}\\s+\\S`));
      if (k < 0) break;
      const resto = linhas[i].slice(k).replace(/^\d+\s+/, "");
      if (!INICIO_SIMBOLO.test(resto) && !MAIUSC.test(resto) && !INICIO_CONTA.test(resto)) break;
      linhas.splice(i + 1, 0, linhas[i].slice(k).trim());
      linhas[i] = linhas[i].slice(0, k).trim();
      i++; n++;
    }
  }
  for (const l of linhas) {
    if (!l || CABECALHO.some((r) => r.test(l))) continue;
    const m = l.match(/^(\d{1,3})\s+(\S.*)$/);
    const n = m ? Number(m[1]) : 0;
    // Prova em duas colunas sai fora de ordem no texto: aceita qualquer número do gabarito ainda não visto,
    // desde que a linha comece como frase (as numerações de linha dos textos continuam frases em minúscula).
    const anterior = itens.length ? itens[itens.length - 1].numero : 0;
    const comeco = m && (MAIUSC.test(m[2]) && m[2].length > 15 || n === anterior + 1 && (INICIO_SIMBOLO.test(m[2]) || INICIO_CONTA.test(m[2])) && m[2].length > 3);
    if (m && numeros.has(n) && !vistos.has(n) && comeco) {
      vistos.add(n);
      // Linhas soltas antes deste item: se trazem um comando ("julgue os itens..."), são o novo texto-base;
      // senão, são continuação do item anterior (quebra de linha no PDF).
      if (bloco.length) {
        if (bloco.some((b) => COMANDO.test(b)) || !itens.length) {
          // O fim do item anterior que quebrou de linha vem antes do novo comando: devolve-o ao item (antes ia
          // todo para o texto-base do item seguinte: um ficava truncado e o outro contaminado).
          const ant = itens[itens.length - 1];
          while (ant && bloco.length > 1 && !FIM_FRASE.test(ant.enunciado.trim()) && !/^Texto\s/i.test(bloco[0])) ant.enunciado += " " + bloco.shift();
          base = bloco.join("\n");
        }
        else itens[itens.length - 1].enunciado += " " + bloco.join(" ");
        bloco = [];
      }
      itens.push({ numero: n, base, enunciado: m[2] });
    } else bloco.push(l);
  }
  // Sobra depois do último item (rodapé, instruções): continuação só se for curta e sem cara de instrução.
  if (bloco.length && itens.length && bloco.join(" ").length < 400 && !bloco.some((b) => COMANDO.test(b))) itens[itens.length - 1].enunciado += " " + bloco.join(" ");
  return itens;
}

// Prova cujo PDF traz as justificativas escondidas ("JUSTIFICATIVA - Certo. ... <FimJust>") sai com colunas e
// justificativas embaralhadas no texto: os itens ficam marcados extracaoRuim e o preparar.ts os deixa de fora.
export function marcar(itens: Item[], gabarito: Map<number, string>, textoProva: string) {
  const ruim = /<FimJust>|JUSTIFICATIVA\s*[–-]\s*(Certo|Errado)/.test(textoProva);
  return itens.map((it) => ({
    ...it,
    gabarito: gabarito.get(it.numero),
    anulado: gabarito.get(it.numero) === "X",
    temFigura: /figura|gráfico|tabela|imagem|ilustra|quadro (a seguir|acima|abaixo)/i.test(it.base + " " + it.enunciado),
    ...(VISUAL.test(it.base + " " + it.enunciado) || REPRESENTADO.test(it.base + " " + it.enunciado) ? { precisaFigura: true } : {}),
    ...(ruim || misturado(it.enunciado) ? { extracaoRuim: true } : {}),
  }));
}

// Refaz a extração a partir dos PDFs já baixados (sem rede). Grava os .json em <destino>/<CONCURSO>/ (padrão: no
// próprio lugar). Só troca os itens: número, arquivo e concurso continuam os mesmos, então os ids não mudam.
export function reprocessar(destino = SAIDA): { provas: number; itensAntes: number; itensDepois: number } {
  let provas = 0, itensAntes = 0, itensDepois = 0;
  for (const d of Deno.readDirSync(SAIDA)) {
    if (!d.isDirectory) continue;
    for (const f of Deno.readDirSync(`${SAIDA}/${d.name}`)) {
      if (!f.name.endsWith(".json")) continue;
      let reg: Json; try { reg = JSON.parse(Deno.readTextFileSync(`${SAIDA}/${d.name}/${f.name}`)); } catch { continue; }
      if (!reg.arquivo || !reg.gabaritoUrl) continue;
      const pdf = `${SAIDA}/${d.name}/${reg.arquivo}`, gab = `${SAIDA}/${d.name}/${String(reg.gabaritoUrl).split("/").pop()}`;
      try { Deno.statSync(pdf); Deno.statSync(gab); } catch { continue; }
      const gabarito = lerGabarito(pdfParaTexto(gab, true));
      const textoProva = pdfParaTexto(pdf, false);
      const itens = lerProva(textoProva, new Set(gabarito.keys()));
      itensAntes += (reg.itens ?? []).length; itensDepois += itens.length; provas++;
      reg.itens = marcar(itens, gabarito, textoProva);
      reg.itensExtraidos = itens.length;
      reg.faltando = [...gabarito.keys()].filter((n) => !itens.some((i) => i.numero === n));
      Deno.mkdirSync(`${destino}/${d.name}`, { recursive: true });
      Deno.writeTextFileSync(`${destino}/${d.name}/${f.name}`, JSON.stringify(reg, null, 1));
    }
  }
  return { provas, itensAntes, itensDepois };
}

// ---------------------------------------------------------------- um concurso
const VERSAO_ESPECIAL = /ADAPTADA|AMPLIADA|SUPER|LIBRAS|BRAILLE|LEDOR|TRANSCRI/i;
async function concurso(codigo: string): Promise<Json> {
  const r = await fetch(API + codigo, { headers: HEADERS });
  if (!r.ok) return { codigo, erro: `API ${r.status}` };
  const ev = await r.json();
  const arquivos: Json[] = [...(ev.arquivosGabarito ?? []), ...(ev.arquivosEdital ?? [])];
  const nomes = new Map(arquivos.map((a) => [String(a.nomeArquivo).toUpperCase(), String(a.nomeArquivo)])); // nomes em maiúscula ou minúscula, conforme o ano
  const provas = arquivos.filter((a) => /^PROVA OBJETIVA/i.test(String(a.descricaoArquivo ?? "")) && !VERSAO_ESPECIAL.test(String(a.descricaoArquivo) + a.nomeArquivo));
  const dir = `${SAIDA}/${codigo}`;
  Deno.mkdirSync(dir, { recursive: true });
  const resumo: Json = { codigo, nome: ev.eventoNomeCompleto ?? ev.eventoNomeAbreviado, ano: ev.eventoAno, provas: [] };
  for (const p of provas) {
    const nome = String(p.nomeArquivo);
    const gab = nomes.get(`GAB_DEFINITIVO_${nome}`.toUpperCase()) ?? "";
    if (!gab) { resumo.provas.push({ nome, erro: "sem gabarito definitivo" }); continue; }
    const okP = await baixar(CDN + `${codigo}/arquivos/${nome}`, `${dir}/${nome}`);
    const okG = await baixar(CDN + `${codigo}/arquivos/${gab}`, `${dir}/${gab}`);
    if (!okP || !okG) { resumo.provas.push({ nome, erro: "download falhou" }); continue; }
    const gabarito = lerGabarito(pdfParaTexto(`${dir}/${gab}`, true));
    const textoProva = pdfParaTexto(`${dir}/${nome}`, false);
    const itens = lerProva(textoProva, new Set(gabarito.keys()));
    const ano = textoProva.match(/Aplicação:\s*(\d{4})/)?.[1] ?? ev.eventoAno;
    const saida = marcar(itens, gabarito, textoProva);
    const faltando = [...gabarito.keys()].filter((n) => !itens.some((i) => i.numero === n));
    const registro = {
      banca: "Cebraspe", concurso: codigo, nomeConcurso: resumo.nome, ano: Number(ano) || null,
      prova: String(p.descricaoArquivo), arquivo: nome, url: CDN + `${codigo}/arquivos/${nome}`,
      gabaritoUrl: CDN + `${codigo}/arquivos/${gab}`, itensNoGabarito: gabarito.size, itensExtraidos: itens.length,
      faltando, itens: saida,
    };
    Deno.writeTextFileSync(`${dir}/${nome.replace(/\.pdf$/i, "")}.json`, JSON.stringify(registro, null, 1));
    resumo.provas.push({ nome, descricao: p.descricaoArquivo, gabarito: gabarito.size, extraidos: itens.length, faltando: faltando.length, anulados: saida.filter((i) => i.anulado).length });
  }
  return resumo;
}

// ---------------------------------------------------------------- principal
const args = Deno.args;
// --catalogo: todos os concursos encerrados do site (a mesma lista da aba "Encerrados"), mais novos primeiro,
// mais os dos editais que não estiverem nela.
async function catalogo(): Promise<string[]> {
  const r = await fetch("https://apis.cebraspe.org.br/cebraspe/eventos/tipo/concursos/fase/encerrado", { headers: HEADERS });
  const ev: Json[] = ((await r.json())?.[0]?.eventos ?? []).sort((a: Json, b: Json) => Number(b.eventoAno) - Number(a.eventoAno));
  return [...new Set([...ev.map((e) => String(e.eventoURL)), ...concursosDosEditais()])];
}
// --reprocessar [destino]: refaz a extração dos PDFs já baixados, sem baixar nada.
if (import.meta.main && args.includes("--reprocessar")) {
  const destino = args.filter((a) => !a.startsWith("--"))[0];
  console.log(reprocessar(destino ? destino.replace(/\\/g, "/") : undefined));
  Deno.exit(0);
}
const lista = !import.meta.main ? [] : args.includes("--catalogo") ? await catalogo() : args.includes("--todos") ? concursosDosEditais() : args.filter((a) => !a.startsWith("--"));
if (import.meta.main && !lista.length) { console.log("uso: deno run -A scripts/oficiais/cebraspe.ts ABIN_17 | --todos"); Deno.exit(1); }
for (const c of lista) {
  try {
    const r = await concurso(c);
    const ok = (r.provas ?? []).filter((p: Json) => !p.erro);
    console.log(`${c}: ${ok.length} provas, ${ok.reduce((s: number, p: Json) => s + p.extraidos, 0)} itens extraídos de ${ok.reduce((s: number, p: Json) => s + p.gabarito, 0)} no gabarito${r.erro ? " — " + r.erro : ""}`);
    for (const p of r.provas ?? []) if (p.erro || p.faltando) console.log(`   ${p.nome}: ${p.erro ?? `faltaram ${p.faltando}`}`);
  } catch (e) { console.log(`${c}: erro ${String((e as Error).message ?? e).slice(0, 150)}`); }
}
