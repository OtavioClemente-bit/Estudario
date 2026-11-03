// Fila de pedidos curtos para o projeto "Gerador Estudário" no ChatGPT (as regras e o formato ficam nas
// instruções do projeto; cada pedido é uma linha). Eu digito os pedidos no chat; o ChatGPT escreve o conteúdo.
//   deno run -A scripts/chatgpt/fila.ts montar        monta conteudo/entrada/chatgpt/fila.json (mantém o que já foi enviado)
//   deno run -A scripts/chatgpt/fila.ts proximos 10   mostra os próximos 10 e marca como enviados
//   deno run -A scripts/chatgpt/fila.ts status
const ROOT = new URL("../../", import.meta.url).pathname.replace(/^\/([A-Za-z]:)/, "$1");
const FILA = `${ROOT}conteudo/entrada/chatgpt/fila.json`;
const RESERVA = `${ROOT}conteudo/entrada/materias-reservadas.txt`;
type Item = { nome: string; tipo: "MATERIA" | "QUESTOES"; pedido: string; enviado?: string };
const ler = (p: string) => JSON.parse(Deno.readTextFileSync(p).replace(/^﻿/, ""));
const slug = (s: string) => s.normalize("NFD").replace(/[̀-ͯ]/g, "").toLowerCase().replace(/[^a-z0-9]+/g, "-").replace(/^-|-$/g, "");
const existeMateria = (id: string) => { const [a, ...b] = id.split("."); try { Deno.statSync(`${ROOT}conteudo/materias/${a}/${b.join(".")}.json`); return true; } catch { return false; } };

// "30/10/2026, 16:15:07" → ms ("pulado" e outros textos contam como antigos).
function dataEnvio(s: string): number {
  const m = String(s).match(/^(\d{2})\/(\d{2})\/(\d{4}), (\d{2}):(\d{2})/);
  return m ? new Date(+m[3], +m[2] - 1, +m[1], +m[4], +m[5]).getTime() : 0;
}

function montar(): Item[] {
  const antiga: Item[] = (() => { try { return ler(FILA); } catch { return []; } })();
  const ja = new Set(antiga.map((i) => i.nome));
  const novos: Item[] = [];

  // 1) Matérias novas decididas pelo Codex e ainda sem arquivo (só as específicas: lei, regimento, órgão, estado).
  const porId = new Map<string, string[]>();
  for (const e of Deno.readDirSync(`${ROOT}conteudo/entrada/itens-sem-materia`)) {
    if (!e.name.endsWith(".json")) continue;
    let j: Record<string, any>;
    try { j = ler(`${ROOT}conteudo/entrada/itens-sem-materia/${e.name}`); } catch { continue; }
    if (!["ligar", "materia-nova"].includes(j.decisao) || !j.id) continue;
    porId.set(j.id, [...(porId.get(j.id) ?? []), String(j.texto)]);
  }
  const ESTADOS = /^(acre|alagoas|amapa|amazonas|bahia|ceara|distrito-federal|espirito-santo|goias|maranhao|mato-grosso|mato-grosso-do-sul|minas-gerais|para|paraiba|parana|pernambuco|piaui|rio-de-janeiro|rio-grande-do-norte|rio-grande-do-sul|rondonia|roraima|santa-catarina|sao-paulo|sergipe|tocantins)\./;
  const especifico = (id: string) => ESTADOS.test(id) || /\d|regimento|codigo|estatuto|lei-|decreto|resolucao|organica|nav-brasil|telebras|infra-sa/.test(id);
  for (const [id, itens] of porId) {
    if (ja.has(id) || existeMateria(id) || !especifico(id)) continue;
    try { Deno.statSync(`${ROOT}conteudo/entrada/materias-codex-2/${id}.json`); continue; } catch { /* sem rascunho */ }
    const item = itens.sort((a, b) => b.length - a.length)[0].replace(/\s+/g, " ").slice(0, 300);
    novos.push({ nome: id, tipo: "MATERIA", pedido: `MATÉRIA: id=${id}; tema=${item}; item=${item}` });
  }

  // 2) Lotes de questões para recortes magros dos assuntos que mais caem (banca x tópico).
  const editais = new Map<string, number>();
  for (const e of Deno.readDirSync(`${ROOT}conteudo/editais`)) {
    if (!e.name.endsWith(".json")) continue;
    const ed = ler(`${ROOT}conteudo/editais/${e.name}`);
    const b = slug(String(ed.banca ?? ""));
    const vistos = new Set<string>();
    for (const d of ed.disciplinas ?? []) for (const t of d.topicos ?? []) if (t.topico) vistos.add(`${b}/${t.topico}`);
    for (const k of vistos) editais.set(k, (editais.get(k) ?? 0) + 1);
  }
  const agy: Record<string, { ultimo?: number }> = (() => { try { return ler(`${ROOT}conteudo/entrada/agy/estado.json`).pares ?? {}; } catch { return {}; } })();
  const lotes: { k: string; peso: number; item: Item }[] = [];
  for (const banca of Deno.readDirSync(`${ROOT}conteudo/recortes`)) {
    if (!banca.isDirectory) continue;
    for (const f of Deno.readDirSync(`${ROOT}conteudo/recortes/${banca.name}`)) {
      if (!f.name.endsWith(".json")) continue;
      const topic = f.name.replace(/\.json$/, "");
      if (topic.startsWith("atualidades")) continue;
      const n = editais.get(`${banca.name}/${topic}`) ?? 0;
      if (n < 5) continue;
      const r = ler(`${ROOT}conteudo/recortes/${banca.name}/${f.name}`);
      const qs = (r.questions ?? []).length;
      if (qs >= 200) continue;
      // Rodadas: o mesmo recorte pode receber novo lote (chatgpt-2, -3…) quando o anterior foi enviado há mais
      // de 2 dias; nunca dois lotes do mesmo recorte esperando ao mesmo tempo. 7 e 8xx ficam para outras fontes.
      const prefixo = `${banca.name}.${topic}.chatgpt-`;
      const anteriores = antiga.filter((i) => i.nome.startsWith(prefixo));
      const recente = anteriores.some((i) => !i.enviado || Date.now() - dataEnvio(i.enviado) < 2 * 86_400_000);
      if (recente) continue;
      // O gerador do Antigravity trabalhou neste recorte nos últimos 2 dias: deixa com ele (evita os dois no mesmo).
      if (Date.now() - (agy[`${topic}|${banca.name.replace(/-/g, " ")}`]?.ultimo ?? 0) < 2 * 86_400_000) continue;
      let rodada = anteriores.length + 1;
      if (rodada === 7) rodada = 9;
      const [a, ...b] = topic.split(".");
      let secoes: string[] = [];
      let titulo = topic;
      try { const mt = ler(`${ROOT}conteudo/materias/${a}/${b.join(".")}.json`); secoes = mt.chapters.map((c: any) => c.title); titulo = mt.title; } catch { continue; }
      const ce = /cebraspe|quadrix/.test(banca.name);
      const topico = titulo;
      const estilo = String(r.board ?? banca.name);
      const nome = `${prefixo}${rodada}`;
      if (ja.has(nome)) continue;
      lotes.push({ k: nome, peso: n / (qs + 10), item: { nome, tipo: "QUESTOES", pedido: `QUESTÕES: banca=${estilo}; topico=${topico}; formato=${ce ? "Certo/Errado" : "múltipla escolha"}; quantidade=40; secoes=${secoes.join(" | ")}; nome=${nome}` } });
    }
  }
  lotes.sort((x, y) => y.peso - x.peso);
  // Intercala: 1 matéria para cada 2 lotes de questões, enquanto houver matérias.
  const out: Item[] = [];
  let m = 0, q = 0;
  while (m < novos.length || q < lotes.length) {
    if (m < novos.length) out.push(novos[m++]);
    for (let i = 0; i < 2 && q < lotes.length; i++) out.push(lotes[q++].item);
  }
  return [...antiga, ...out];
}

const cmd = Deno.args[0];
if (cmd === "montar") {
  const f = montar();
  Deno.writeTextFileSync(FILA, JSON.stringify(f, null, 1));
  console.log(`fila: ${f.length} pedidos (${f.filter((i) => i.tipo === "MATERIA").length} matérias, ${f.filter((i) => i.tipo === "QUESTOES").length} lotes), ${f.filter((i) => i.enviado).length} já enviados`);
} else if (cmd === "proximos") {
  let f: Item[] = ler(FILA);
  const n = Number(Deno.args[1] ?? 10);
  const tipo = Deno.args[2]; // "QUESTOES" para quem só faz questões (Gemini)
  const livres = () => f.filter((i) => !i.enviado && (!tipo || i.tipo === tipo));
  // Fila quase vazia: monta de novo sozinha (novas rodadas dos recortes que ainda têm poucas questões).
  if (livres().length < 40) { f = montar(); Deno.writeTextFileSync(FILA, JSON.stringify(f, null, 1)); }
  // Matéria que o Codex começou depois que a fila foi montada (já tem rascunho ou já foi publicada): não manda ao
  // ChatGPT, senão as duas escrevem a mesma.
  for (const i of livres()) {
    if (i.tipo !== "MATERIA") continue;
    let comCodex = existeMateria(i.nome);
    try { Deno.statSync(`${ROOT}conteudo/entrada/materias-codex-2/${i.nome}.json`); comCodex = true; } catch { /* não começou */ }
    if (comCodex) i.enviado = "pulado (Codex)";
  }
  // Explicações das questões de prova real (scripts/oficiais/pedidos.ts): 1 de cada 5 pedidos do ChatGPT
  // (o Gemini, que só faz QUESTOES, não recebe), até a fila delas acabar.
  const FILA_OFICIAIS = `${ROOT}work/oficiais/fila.json`;
  let oficiais: Array<{ pedido: string; enviado: string | null }> = [];
  if (!tipo) try { oficiais = JSON.parse(Deno.readTextFileSync(FILA_OFICIAIS)); } catch { /* sem fila de oficiais */ }
  const vezOficiais = oficiais.filter((p) => !p.enviado).slice(0, Math.max(1, Math.floor(n / 5)));
  const sel = livres().slice(0, n - vezOficiais.length);
  const agora = new Date().toLocaleString("pt-BR");
  for (const i of sel) i.enviado = agora;
  Deno.writeTextFileSync(FILA, JSON.stringify(f, null, 1));
  if (vezOficiais.length) {
    for (const p of vezOficiais) p.enviado = agora;
    Deno.writeTextFileSync(FILA_OFICIAIS, JSON.stringify(oficiais));
    for (const p of vezOficiais) console.log(p.pedido);
  }
  // Matérias pedidas ao ChatGPT ficam reservadas para o Codex não escrever a mesma.
  const res = new Set(Deno.readTextFileSync(RESERVA).split(/\s+/).filter(Boolean));
  for (const i of sel) if (i.tipo === "MATERIA") res.add(i.nome);
  Deno.writeTextFileSync(RESERVA, [...res].join("\n") + "\n");
  for (const i of sel) console.log(i.pedido);
} else {
  const f: Item[] = ler(FILA);
  console.log(`${f.filter((i) => i.enviado).length} enviados de ${f.length}`);
}
