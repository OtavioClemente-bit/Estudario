// Questões reais da FGV: na página de cada concurso (conhecimento.fgv.br/concursos/<slug>) pega as provas objetivas
// "TIPO 1" e o PDF do gabarito OFICIAL DEFINITIVO, extrai as questões (5 alternativas) exatamente como estão e cruza
// cada prova com o bloco certo do gabarito. Não escreve nem altera questão nenhuma. Respeita o robots.txt da FGV
// (só bloqueia busca/admin) e baixa devagar. Saída em work/provas-oficiais/fgv/<slug>/ (fora do git).
//   deno run -A scripts/oficiais/fgv.ts trt16 [outro_slug ...]
//   deno run -A scripts/oficiais/fgv.ts --reprocessar [destino]   (refaz a extração dos PDFs já baixados, sem rede)
//   deno run -A scripts/oficiais/fgv.ts --todos     (concursos da FGV dos editais que têm link /concursos/<slug>)
const ROOT = new URL("../../", import.meta.url).pathname.replace(/^\/([A-Za-z]:)/, "$1");
const SAIDA = `${ROOT}work/provas-oficiais/fgv`;
const SITE = "https://conhecimento.fgv.br";
const HEADERS = { "User-Agent": "Mozilla/5.0" };
const sleep = (ms: number) => new Promise((r) => setTimeout(r, ms));
const norm = (s: string) => s.normalize("NFD").replace(/[̀-ͯ]/g, "").toLowerCase().replace(/[^a-z0-9]+/g, " ").trim();
type Json = Record<string, any>;

function concursosDosEditais(): string[] {
  const set = new Set<string>();
  for (const e of Deno.readDirSync(`${ROOT}conteudo/editais`)) {
    if (!e.name.endsWith(".json")) continue;
    const j = JSON.parse(Deno.readTextFileSync(`${ROOT}conteudo/editais/${e.name}`).replace(/^﻿/, ""));
    const m = String(j.url ?? "").match(/conhecimento\.fgv\.br\/concursos\/([^/?#]+)/i);
    if (m) set.add(m[1]);
  }
  return [...set].sort();
}
async function baixar(url: string, destino: string): Promise<boolean> {
  try { Deno.statSync(destino); return true; } catch { /* novo */ }
  const r = await fetch(url, { headers: HEADERS });
  if (!r.ok) return false;
  Deno.writeFileSync(destino, new Uint8Array(await r.arrayBuffer()));
  await sleep(2500);
  return true;
}
function texto(pdf: string, layout: boolean): string {
  const o = new Deno.Command("pdftotext", { args: ["-enc", "UTF-8", ...(layout ? ["-layout"] : []), pdf, "-"], stdout: "piped", stderr: "piped" }).outputSync();
  return new TextDecoder().decode(o.stdout);
}

// ---------------------------------------------------------------- gabarito: blocos "<cargo> – TIPO 1" + linhas de números/letras
// Turno: "PROVAS APLICADAS EM 23/06/2024 - MANHÃ" no gabarito; "manha-"/"tarde-" no nome da prova ou na capa.
// 10/11: o gabarito da CVM 2024 só tinha a MANHÃ e os títulos dos blocos são iguais nos dois turnos; as provas da
// tarde levaram o gabarito da manhã. Agora prova e bloco só casam no mesmo turno.
type Turno = "manha" | "tarde" | "noite" | null;
const turnoDe = (s: string): Turno => { const n = norm(s); return /\bmanha\b/.test(n) ? "manha" : /\btarde\b/.test(n) ? "tarde" : /\bnoite\b/.test(n) ? "noite" : null; };
function doTurno<T extends { turno?: Turno }>(blocos: T[], turno: Turno): T[] {
  if (!turno || !blocos.some((b) => b.turno)) return blocos;
  return blocos.filter((b) => b.turno === turno);
}
function lerGabarito(t: string): { titulo: string; resp: Map<number, string>; turno: Turno }[] {
  const blocos: { titulo: string; resp: Map<number, string>; turno: Turno }[] = [];
  const L = t.split(/\r?\n/);
  let atual: { titulo: string; resp: Map<number, string>; turno: Turno } | null = null;
  let turno: Turno = null;
  for (let i = 0; i < L.length; i++) {
    const l = L[i].trim();
    if (/PROVAS?\s+APLICADAS?/i.test(l)) { turno = turnoDe(l); continue; }
    if (/TIPO\s*\d/i.test(l) && !/^\d/.test(l)) { atual = { titulo: l, resp: new Map(), turno }; blocos.push(atual); continue; }
    if (!atual || !/^\d+(\s+\d+)+$/.test(l)) continue;
    const nums = l.split(/\s+/).map(Number);
    const letras = (L[i + 1] ?? "").replace(/\s+/g, "").toUpperCase();
    if (letras.length !== nums.length) continue; // linha estranha: não arrisca
    nums.forEach((n, k) => atual!.resp.set(n, letras[k]));
    i++;
  }
  return blocos.filter((b) => /TIPO\s*1\b/i.test(b.titulo) && b.resp.size);
}

// ---------------------------------------------------------------- questões: número, enunciado, (A)..(E)
const CAB = [/^FGV$/, /^TRIBUNAL|^JUSTIÇA|^MINIST|^PREFEITURA|^SECRETARIA/i];
function lerProva(t: string, total: number) {
  const L = t.split(/\r?\n/).map((l) => l.trim());
  // Rodapés repetidos de página (ex.: "Analista Judiciário (Área ...)") saem
  const freq = new Map<string, number>();
  for (const l of L) if (l) freq.set(l, (freq.get(l) ?? 0) + 1);
  const linhas = L.filter((l) => l && !((freq.get(l) ?? 0) >= 3 && !/^\(?[A-E]\)/.test(l)) && !CAB.some((r) => r.test(l) && l.length < 60));
  const qs: { numero: number; bruto: string }[] = [];
  // Prova em duas colunas sai fora de ordem: aceita cada número (1..total) uma vez, em qualquer ordem, se a linha é só
  // o número ou começa uma frase (não "25 (C) 19." de uma alternativa).
  const vistos = new Set<number>();
  for (const l of linhas) {
    const m = l.match(/^(\d{1,3})(?:\s+(\S.*))?$/);
    const n = m ? Number(m[1]) : 0;
    if (m && n >= 1 && n <= total && !vistos.has(n) && (!m[2] || /^[A-ZÁÉÍÓÚÂÊÔÃÕÇ"“']/.test(m[2]))) { vistos.add(n); qs.push({ numero: n, bruto: m[2] ?? "" }); continue; }
    if (qs.length) qs[qs.length - 1].bruto += "\n" + l;
  }
  return qs.map((q) => {
    const partes = q.bruto.split(/\s*\(([A-E])\)\s+/);
    // partes = [enunciado, "A", textoA, "B", textoB, ...]
    const alternativas: string[] = [];
    for (let k = 1; k < partes.length; k += 2) alternativas.push(partes[k + 1] ?? "");
    // Depois da (E) vêm o título da próxima seção e o texto-base das questões seguintes ("Língua Inglesa / READ THE
    // TEXT..."): a E acaba na primeira quebra de linha depois de fim de frase quando o resto é título curto ou longo.
    if (alternativas.length === 5) alternativas[4] = cortarE(alternativas[4]);
    const letras = partes.filter((_, k) => k % 2 === 1).join("");
    return { numero: q.numero, enunciado: partes[0].replace(/\s+/g, " ").trim(), alternativas: alternativas.map((a) => a.replace(/\s+/g, " ").trim()), ok: letras === "ABCDE" };
  });
}
function cortarE(e: string): string {
  const ls = e.split("\n");
  for (let k = 1; k < ls.length; k++) {
    const antes = ls.slice(0, k).join(" ").trim(), resto = ls.slice(k).join("\n").trim(), l1 = ls[k].trim();
    if (!/[.;!?…]["”)]?$/.test(antes)) continue;
    const titulo = l1.length <= 60 && /^[A-ZÁÉÍÓÚÂÊÔÃÕÇ]/.test(l1) && !/[.;:!?]$/.test(l1);
    if (titulo || resto.length >= 150) return ls.slice(0, k).join("\n");
  }
  return e;
}

// casa a prova com o bloco do gabarito pelo cabeçalho da capa (cargo + especialidade)
// Compara pelo começo das palavras (enfermeiro/enfermeiros/enfermagem) e só aceita com folga clara sobre o 2º colocado.
const GENERICAS = new Set(["tipo", "nivel", "superior", "medio", "fundamental", "prova", "area", "cargo", "branca"]);
const raiz = (w: string) => w.slice(0, 6);
function casar(capa: string, blocos: { titulo: string }[]) {
  const pc = new Set(norm(capa).split(" ").filter((w) => w.length > 3).map(raiz));
  const palavras = (s: string) => [...new Set(norm(s).split(" ").filter((w) => w.length > 3 && !GENERICAS.has(w)).map(raiz))];
  // Título "Cargo - Área - Especialidade – TIPO 1": o primeiro pedaço (cargo) e o ÚLTIMO (especialidade) têm de estar
  // inteiros na capa; entre os candidatos fica o de especialidade mais longa (o mais específico). Empate: não arrisca.
  // 10/11 (TCE-GO 2024): "Analista de Controle Externo - Controle Externo" tem a especialidade igual ao cargo, então
  // batia com TODA capa "ANALISTA DE CONTROLE EXTERNO ..." e, por ter 2 palavras, ganhava de "Jurídica"/"Engenharia".
  // Agora a especialidade conta só com as palavras que não são do cargo; se não sobra nenhuma, a capa tem de repetir
  // a especialidade (cargo + especialidade = 2 vezes cada palavra) e não pode trazer a especialidade de outro bloco.
  const tokCapa = norm(capa).split(" ").filter((w) => w.length > 3).map(raiz);
  const vezes = (w: string) => tokCapa.filter((x) => x === w).length;
  const info = blocos.map((b) => {
    const partes = b.titulo.replace(/[–-]\s*TIPO\s*\d.*$/i, "").split(/\s+[-–]\s+|\s*[-–]\s*(?=[A-ZÁÉÍÓÚ])/).map((s) => s.trim()).filter(Boolean);
    const cargo = palavras(partes[0] ?? ""), esp = palavras(partes[partes.length - 1] ?? "");
    // título de uma parte só ("Contador - TIPO 1"): cargo e especialidade são o mesmo pedaço, vale a regra antiga
    return { cargo, esp, espP: partes.length > 1 ? esp.filter((w) => !cargo.includes(w)) : esp };
  });
  const cand = blocos.map((b, i) => {
    const { cargo, esp, espP } = info[i];
    const outraEsp = info.some((o, j) => j !== i && o.espP.length > 0 && o.espP.every((w) => pc.has(w)) && cargo.every((w) => o.cargo.includes(w)));
    const espOk = espP.length > 0 ? espP.every((w) => pc.has(w)) : esp.length > 0 && esp.every((w) => vezes(w) >= 2) && !outraEsp;
    const ok = cargo.every((w) => pc.has(w)) && esp.length > 0 && espOk;
    const meio = esp.filter((w) => pc.has(w)).length / Math.max(1, esp.length);
    return { i, ok, meio, nEsp: esp.length, tam: espP.length + palavras(b.titulo).length / 100 };
  });
  const ok = cand.filter((x) => x.ok).sort((a, b) => b.tam - a.tam);
  if (!ok.length || (ok.length > 1 && ok[0].tam === ok[1].tam)) return -1;
  // trava: um bloco de especialidade MAIOR bate pela metade ou mais com a capa ("Oficial de Justiça Avaliador...")
  // e não foi o escolhido → a capa provavelmente é dele: não arrisca.
  // (bloco cuja especialidade é só o nome do cargo, como "Controle Externo", não trava: ele bate com toda capa do cargo)
  if (cand.some((x) => x.i !== ok[0].i && !x.ok && info[x.i].espP.length > 0 && x.nEsp > ok[0].nEsp && x.meio >= 0.5)) return -1;
  return ok[0].i;
}

const marcar = (q: Json, resp: Map<number, string>): Json => ({ ...q, gabarito: resp.get(q.numero), anulada: !/^[A-E]$/.test(resp.get(q.numero) ?? ""), temFigura: /figura|gráfico|tabela|imagem|foto|ilustra/i.test(q.enunciado) });

// --reprocessar [destino]: refaz a extração dos PDFs já baixados (sem rede), com o gabarito local e o mesmo bloco
// (título "prova" gravado). Número, arquivo e concurso continuam os mesmos, então os ids não mudam.
function reprocessar(destino = SAIDA) {
  let provas = 0, antes = 0, depois = 0;
  for (const d of Deno.readDirSync(SAIDA)) {
    if (!d.isDirectory) continue;
    const dir = `${SAIDA}/${d.name}`;
    let blocos: { titulo: string; resp: Map<number, string>; turno: Turno }[];
    try { blocos = lerGabarito(texto(`${dir}/_gabarito_definitivo.pdf`, true)); } catch { continue; }
    for (const f of Deno.readDirSync(dir)) {
      if (!f.name.endsWith(".json") || f.name.startsWith("_")) continue;
      let reg: Json; try { reg = JSON.parse(Deno.readTextFileSync(`${dir}/${f.name}`)); } catch { continue; }
      const turnoProva = turnoDe(f.name);
      const b = doTurno(blocos, turnoProva).filter((x) => x.titulo === reg.prova);
      // Sem bloco do mesmo turno: a prova fica sem questões (antes levava o gabarito do outro turno).
      if (b.length === 0 && turnoProva && blocos.some((x) => x.turno)) {
        antes += (reg.questoes ?? []).length; reg.questoes = []; reg.extraidas = 0; reg.completas = 0; reg.semGabaritoDoTurno = turnoProva; provas++;
        Deno.mkdirSync(`${destino}/${d.name}`, { recursive: true });
        Deno.writeTextFileSync(`${destino}/${d.name}/${f.name}`, JSON.stringify(reg, null, 1));
        continue;
      }
      if (b.length !== 1) continue;
      try { Deno.statSync(`${dir}/${reg.arquivo}`); } catch { continue; }
      // o bloco gravado pode ter sido casado errado (TCE-GO 2024): casa de novo pela capa; sem certeza, fica sem questões
      const tProva = texto(`${dir}/${reg.arquivo}`, false);
      const capa = tProva.split(/\r?\n/).slice(0, 12).join(" ");
      const mesmoTurno = doTurno(blocos, turnoProva ?? turnoDe(capa));
      const bt = casar(capa, mesmoTurno);
      if (bt < 0) {
        antes += (reg.questoes ?? []).length; reg.questoes = []; reg.extraidas = 0; reg.completas = 0; reg.semBlocoCerto = true; provas++;
        Deno.mkdirSync(`${destino}/${d.name}`, { recursive: true });
        Deno.writeTextFileSync(`${destino}/${d.name}/${f.name}`, JSON.stringify(reg, null, 1));
        continue;
      }
      b[0] = mesmoTurno[bt];
      reg.prova = b[0].titulo;
      const qs = lerProva(tProva, b[0].resp.size).map((q) => marcar(q, b[0].resp));
      antes += (reg.questoes ?? []).length; depois += qs.length; provas++;
      reg.questoes = qs; reg.extraidas = qs.length; reg.completas = qs.filter((q) => q.ok).length;
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

async function concurso(slug: string): Promise<string> {
  const r = await fetch(`${SITE}/concursos/${slug}`, { headers: HEADERS });
  if (!r.ok) return `${slug}: página ${r.status}`;
  const html = await r.text();
  const links = [...html.matchAll(/href="([^"]+\.pdf)"/gi)].map((m) => m[1].startsWith("http") ? m[1] : SITE + m[1]);
  const gab = links.find((u) => /gabarito/i.test(u) && /definitiv/i.test(u));
  const provas = links.filter((u) => /tipo[_-]?1\.pdf$/i.test(u) && !/gabarito|resultado|padrao|discursiva/i.test(u));
  if (!gab || !provas.length) return `${slug}: ${provas.length} provas, ${gab ? "com" : "sem"} gabarito definitivo`;
  const dir = `${SAIDA}/${slug}`;
  Deno.mkdirSync(dir, { recursive: true });
  if (!(await baixar(gab, `${dir}/_gabarito_definitivo.pdf`))) return `${slug}: gabarito não baixou`;
  const blocos = lerGabarito(texto(`${dir}/_gabarito_definitivo.pdf`, true));
  let ok = 0, itens = 0;
  const avisos: string[] = [];
  for (const u of provas) {
    const nome = decodeURIComponent(u.split("/").pop()!);
    if (!(await baixar(u, `${dir}/${nome}`))) { avisos.push(`${nome}: não baixou`); continue; }
    const t = texto(`${dir}/${nome}`, false);
    const capa = t.split(/\r?\n/).slice(0, 12).join(" ");
    const turnoProva = turnoDe(nome) ?? turnoDe(capa);
    const doMesmoTurno = doTurno(blocos, turnoProva);
    if (!doMesmoTurno.length) { avisos.push(`${nome}: o gabarito não traz o turno ${turnoProva}`); continue; }
    const bt = casar(capa, doMesmoTurno);
    if (bt < 0) { avisos.push(`${nome}: não achei o bloco do gabarito com certeza`); continue; }
    const b = blocos.indexOf(doMesmoTurno[bt]);
    const resp = (blocos[b] as Json).resp as Map<number, string>;
    const qs = lerProva(t, resp.size);
    const saida = qs.map((q) => marcar(q, resp));
    Deno.writeTextFileSync(`${dir}/${nome.replace(/\.pdf$/i, "")}.json`, JSON.stringify({
      banca: "FGV", concurso: slug, ano: Number(capa.match(/20\d\d/)?.[0]) || null, prova: blocos[b].titulo, arquivo: nome, url: u, gabaritoUrl: gab,
      questoesNoGabarito: resp.size, extraidas: saida.length, completas: saida.filter((q) => q.ok).length, questoes: saida,
    }, null, 1));
    ok++; itens += saida.filter((q) => q.ok && !q.anulada).length;
    if (saida.filter((q) => q.ok).length < resp.size) avisos.push(`${nome}: ${saida.filter((q) => q.ok).length} de ${resp.size} completas`);
  }
  return `${slug}: ${ok} de ${provas.length} provas, ${itens} questões completas` + (avisos.length ? "\n   " + avisos.join("\n   ") : "");
}

// --catalogo: percorre a listagem paginada de conhecimento.fgv.br/concursos (?page=0,1,2...) até não aparecer
// concurso novo, mais os dos editais.
async function catalogo(): Promise<string[]> {
  const set = new Set<string>(concursosDosEditais());
  for (let p = 0; p < 200; p++) {
    const html = await (await fetch(`${SITE}/concursos?page=${p}`, { headers: HEADERS })).text();
    const antes = set.size;
    for (const m of html.matchAll(/href="\/concursos\/([a-z0-9_-]+)"/gi)) set.add(m[1]);
    if (set.size === antes) break;
    await sleep(1500);
  }
  return [...set];
}
const lista = Deno.args.includes("--catalogo") ? await catalogo() : Deno.args.includes("--todos") ? concursosDosEditais() : Deno.args.filter((a) => !a.startsWith("--"));
if (!lista.length) { console.log("uso: deno run -A scripts/oficiais/fgv.ts trt16 | --todos"); Deno.exit(1); }
for (const s of lista) {
  try { console.log(await concurso(s)); } catch (e) { console.log(`${s}: erro ${String((e as Error).message ?? e).slice(0, 150)}`); }
}
