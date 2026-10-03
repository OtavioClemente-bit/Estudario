// Pedidos do TRT 3ª Região (banca FUMARC): conhecimentos gerais + Técnico Administrativo + Analista
// Judiciário e Administrativo. Cada tópico do edital vira apelido de exatamente UMA matéria; tópicos
// curtos e vizinhos da mesma disciplina ficam juntos, e cada lei/resolução/regimento fica sozinho.
// Rodar a partir de conteudo/: deno run --allow-read --allow-write pedidos/_gerar-trt3.ts
const modelo = Deno.readTextFileSync("PEDIDO_CHAT_modelo.txt");
const instr = modelo.slice(modelo.indexOf("══════", modelo.indexOf("MATÉRIA 2")))
  .replaceAll("IDECAN", "FUMARC")
  .replace("Você é professor de Língua Portuguesa e elaborador de questões da banca FUMARC.", "Você é professor da disciplina indicada em cada matéria e elaborador de questões da banca FUMARC (concursos de tribunais).")
  .replace('"subject": "Língua Portuguesa",', '"subject": "(a disciplina indicada na matéria)",')
  .replace("2. As cinco alternativas são frases completas e naturais, do dia a dia de bombeiros e do serviço público,", "2. As alternativas (a FUMARC usa quatro, A–D, ou cinco, A–E; use cinco) são afirmações completas e plausíveis, do contexto de tribunais e do serviço público;");
const head = "══════════════════════════════════════════════════════════════\nMATÉRIAS DESTE PEDIDO\n══════════════════════════════════════════════════════════════\nConcurso: Tribunal Regional do Trabalho da 3ª Região (TRT-MG), cargos de Técnico e Analista Judiciário. Banca: FUMARC.\n\n";

const LAW = "- REGRAS DE DIREITO/LEGISLAÇÃO: a banca cobra a letra da lei. Cite artigos, incisos e parágrafos pelo número, com o conteúdo fiel à redação VIGENTE (considere as alterações posteriores); nunca invente número de artigo, prazo ou redação. Se não tiver certeza da redação exata, explique o conteúdo sem citar entre aspas. Distratores trocam um detalhe da lei (prazo, competência, \"poderá\" × \"deverá\", exceção). Fontes: planalto.gov.br, cnj.jus.br, csjt.jus.br, trt3.jus.br, stf.jus.br, tst.jus.br.";
const INFO = "- REGRAS DE INFORMÁTICA: conteúdo prático (menus, atalhos de teclado, caminhos, funções e fórmulas), conferido nas versões citadas no edital. Não invente atalho nem nome de menu; se mudou entre versões, diga qual versão. Questões com situações de escritório de tribunal.";
const ADM = "- REGRAS DE ADMINISTRAÇÃO E ORÇAMENTO: conceitos dos autores e normas consagrados; cite leis e resoluções pelo número só com conteúdo fiel. Questões situacionais de gestão em tribunal.";
const DOC = "- ATENÇÃO: este conteúdo é um documento próprio do tribunal. ANEXE nesta conversa o texto oficial (baixe em trt3.jus.br) e use SOMENTE ele como fonte; não invente artigos. Se o texto não estiver anexado, responda pedindo o anexo em vez de gerar.";

type Spec = { area: string; subject: string; rule: string; topics: string[]; title?: string; doc?: boolean };
const norm = (s: string) => s.normalize("NFD").replace(/[̀-ͯ]/g, "").toLowerCase().replace(/[^a-z0-9]+/g, " ").trim();
const slug = (s: string, words = 5) => norm(s).split(" ").filter((w) => w.length > 2 && !["dos", "das", "nos", "nas", "com", "para", "lei", "por"].includes(w)).slice(0, words).join("-");
// "Lei n. 9.784/1999." não pode virar "Lei n": o ponto seguido de número não encerra o título.
const titleOf = (t: string) => t.split(/[:;]|\.\s(?!\d)/)[0].replace(/\.$/, "").trim().slice(0, 90);
const ownDoc = /Regimento Interno|Código de Ética do TRT|Resolução GP N\. 49|Ato n\. 84\/CSJT/i;
const standalone = /^(Lei|Lei Complementar|Resolução|Decreto|Código|Regimento|Ato|Instrução|Súmula|Emenda|Consolidação)\b/i;

const edital = (cargo: string) => JSON.parse(Deno.readTextFileSync(`editais/trt3-2022--${cargo}.json`)) as { disciplinas: { nome: string; topicos: { texto: string }[] }[] };
const seen = new Set<string>();
const specs: Spec[] = [];

/** Agrupa tópicos consecutivos (até 5); lei e documento próprio sempre sozinhos. */
function chunk(area: string, subject: string, rule: string, topics: string[]) {
  let cur: string[] = [];
  const flush = () => {
    if (cur.length) specs.push({ area, subject, rule, topics: cur });
    cur = [];
  };
  for (const t of topics) {
    const k = norm(t);
    if (seen.has(k)) continue;
    seen.add(k);
    if (ownDoc.test(t)) {
      flush();
      specs.push({ area, subject, rule: DOC, topics: [t], doc: true });
      continue;
    }
    if (standalone.test(t)) {
      flush();
      cur = [t];
      continue;
    }
    cur.push(t);
    if (cur.length >= 5) flush();
  }
  flush();
}

const disc = (cargo: string, re: RegExp) => edital(cargo).disciplinas.find((d) => re.test(d.nome))!.topicos.map((t) => t.texto);

// Português: o que já existe na biblioteca fica de fora; o tópico de norma-padrão (a–f) é atendido
// pela enciclopédia juntando as matérias prontas + Regência.
specs.push({ area: "portugues", subject: "Língua Portuguesa", rule: "", topics: ["Significação contextual de palavras e expressões."], title: "Significação contextual de palavras e expressões" });
specs.push({ area: "portugues", subject: "Língua Portuguesa", rule: "", topics: ["Linguística: variação linguística, norma linguística."], title: "Variação linguística e norma linguística" });
specs.push({ area: "portugues", subject: "Língua Portuguesa", rule: "", topics: ["Regência nominal e verbal", "Regência verbal e nominal", "d) regência nominal e verbal"], title: "Regência verbal e nominal" });

// Informática: um item do edital por matéria; Outlook e Chrome juntos.
const info = disc("analista-administrativa", /INFORMÁTICA/);
for (const t of info) if (!/Outlook|Chrome/.test(t)) specs.push({ area: "informatica", subject: "Noções de Informática", rule: INFO, topics: [t] });
specs.push({ area: "informatica", subject: "Noções de Informática", rule: INFO, topics: info.filter((t) => /Outlook|Chrome/.test(t)), title: "Correio eletrônico (Outlook 2016) e navegação (Google Chrome)" });

// Legislação: 8.112 em 3 matérias; cada lei sozinha; documentos do TRT3 à parte.
const leg = disc("analista-administrativa", /LEGISLA/);
specs.push({ area: "legislacao", subject: "Legislação", rule: LAW, topics: leg.slice(0, 2), title: "Lei 8.112/1990: disposições preliminares, provimento, vacância, remoção, redistribuição e substituição" });
specs.push({ area: "legislacao", subject: "Legislação", rule: LAW, topics: [leg[2]], title: "Lei 8.112/1990: direitos e vantagens" });
specs.push({ area: "legislacao", subject: "Legislação", rule: LAW, topics: leg.slice(3, 5), title: "Lei 8.112/1990: regime disciplinar e processo administrativo disciplinar" });
leg.slice(0, 5).forEach((t) => seen.add(norm(t)));
chunk("legislacao", "Legislação", LAW, leg.slice(5));

// Direito: versões completas (Analista Judiciário) e as "noções" do Técnico como apelidos extras
// da matéria de mesma disciplina com mais palavras em comum.
const big: [string, string, RegExp][] = [
  ["direito-constitucional", "Direito Constitucional", /^DIREITO CONSTITUCIONAL/],
  ["direito-administrativo", "Direito Administrativo", /^DIREITO ADMINISTRATIVO/],
  ["direito-trabalho", "Direito do Trabalho", /^DIREITO DO TRABALHO/],
  ["processo-trabalho", "Direito Processual do Trabalho", /^DIREITO PROCESSUAL DO TRABALHO/],
  ["direito-civil", "Direito Civil", /^DIREITO CIVIL/],
  ["processo-civil", "Direito Processual Civil", /^DIREITO PROCESSUAL CIVIL/],
];
for (const [area, subject, re] of big) chunk(area, subject, LAW, disc("analista-judiciaria", re));
chunk("administracao-publica", "Administração Pública", ADM, disc("analista-administrativa", /ADMINISTRAÇÃO PÚBLICA/));
chunk("orcamento-publico", "Orçamento Público", ADM, disc("analista-administrativa", /ORÇAMENTO/));
chunk("gestao-pessoas", "Gestão de Pessoas", ADM, disc("analista-administrativa", /GESTÃO DE PESSOAS/));

const words = (s: string) => new Set(norm(s).split(" ").filter((w) => w.length > 3));
const noções: [RegExp, string][] = [
  [/NOÇÕES DE DIREITO CONSTITUCIONAL/, "direito-constitucional"], [/NOÇÕES DE DIREITO ADMINISTRATIVO/, "direito-administrativo"],
  [/NOÇÕES DE DIREITO DO TRABALHO/, "direito-trabalho"], [/NOÇÕES DE DIREITO PROCESSUAL DO TRABALHO/, "processo-trabalho"],
];
for (const [re, area] of noções) {
  for (const t of disc("tecnico-administrativa", re)) {
    if (seen.has(norm(t))) continue;
    seen.add(norm(t));
    const w = words(t);
    const best = specs.filter((s) => s.area === area && !s.doc)
      .map((s) => ({ s, score: s.topics.reduce((n, x) => n + [...words(x)].filter((y) => w.has(y)).length, 0) }))
      .sort((a, b) => b.score - a.score)[0];
    if (best && best.score > 0) best.s.topics.push(t);
  }
}

const used = new Set<string>();
const blockOf = (s: Spec, n: number) => {
  const title = s.title ?? titleOf(s.topics[0]);
  let base = slug(title);
  while (used.has(`${s.area}.${base}`)) base += "-2";
  used.add(`${s.area}.${base}`);
  const aliases = s.topics.filter((t) => t.length <= 380).map((t) => `"${t.replaceAll('"', "'")}"`).join(", ");
  return `MATÉRIA ${n}
- Arquivos: ${base}.json e recorte-${base}.json
- id: "${s.area}.${base}" | title: "${title}" | subject: "${s.subject}"
- aliases: [${aliases}] (copie exatamente; acrescente outros nomes usuais do assunto)
- Tópicos do edital que a matéria cobre (TODOS): ${s.topics.join(" | ")}
${s.rule}`.trimEnd();
};

const regular = specs.filter((s) => !s.doc);
const docs = specs.filter((s) => s.doc);
let n = 0;
for (let i = 0; i < regular.length; i += 2) {
  n++;
  const pair = regular.slice(i, i + 2);
  Deno.writeTextFileSync(`pedidos/trt3-lote-${String(n).padStart(2, "0")}.txt`, head + pair.map((s, k) => blockOf(s, k + 1)).join("\n\n") + "\n\n\n" + instr);
}
docs.forEach((s, i) => Deno.writeTextFileSync(`pedidos/trt3-documento-${i + 1}.txt`, head + blockOf(s, 1) + "\n\n\n" + instr.replace("Entregue os 4 arquivos", "Entregue os 2 arquivos")));
console.log(`${regular.length} matérias em ${n} lotes + ${docs.length} documentos próprios do TRT3:`);
regular.forEach((s) => console.log(`  ${s.area} | ${s.title ?? titleOf(s.topics[0])} (${s.topics.length} tópico(s))`));
docs.forEach((s) => console.log(`  [documento] ${s.topics[0]}`));
