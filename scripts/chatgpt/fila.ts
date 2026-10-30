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
  const lotes: { k: string; peso: number; item: Item }[] = [];
  for (const banca of Deno.readDirSync(`${ROOT}conteudo/recortes`)) {
    if (!banca.isDirectory) continue;
    for (const f of Deno.readDirSync(`${ROOT}conteudo/recortes/${banca.name}`)) {
      if (!f.name.endsWith(".json")) continue;
      const topic = f.name.replace(/\.json$/, "");
      if (topic.startsWith("atualidades")) continue;
      const n = editais.get(`${banca.name}/${topic}`) ?? 0;
      if (n < 15) continue;
      const r = ler(`${ROOT}conteudo/recortes/${banca.name}/${f.name}`);
      const qs = (r.questions ?? []).length;
      if (qs >= 120) continue;
      const [a, ...b] = topic.split(".");
      let secoes: string[] = [];
      let titulo = topic;
      try { const mt = ler(`${ROOT}conteudo/materias/${a}/${b.join(".")}.json`); secoes = mt.chapters.map((c: any) => c.title); titulo = mt.title; } catch { continue; }
      const ce = /cebraspe|quadrix/.test(banca.name);
      const topico = titulo;
      const estilo = String(r.board ?? banca.name);
      const nome = `${banca.name}.${topic}.chatgpt-1`;
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
  const f: Item[] = ler(FILA);
  const n = Number(Deno.args[1] ?? 10);
  const sel = f.filter((i) => !i.enviado).slice(0, n);
  const agora = new Date().toLocaleString("pt-BR");
  for (const i of sel) i.enviado = agora;
  Deno.writeTextFileSync(FILA, JSON.stringify(f, null, 1));
  // Matérias pedidas ao ChatGPT ficam reservadas para o Codex não escrever a mesma.
  const res = new Set(Deno.readTextFileSync(RESERVA).split(/\s+/).filter(Boolean));
  for (const i of sel) if (i.tipo === "MATERIA") res.add(i.nome);
  Deno.writeTextFileSync(RESERVA, [...res].join("\n") + "\n");
  for (const i of sel) console.log(i.pedido);
} else {
  const f: Item[] = ler(FILA);
  console.log(`${f.filter((i) => i.enviado).length} enviados de ${f.length}`);
}
