// Gera pedidos/lote-7.txt em diante: os tópicos do edital CBMMG ainda sem pedido, 2 por lote, com as
// regras próprias de cada disciplina. Rodar a partir de conteudo/: deno run --allow-read --allow-write pedidos/_gerar2.ts
const modelo = Deno.readTextFileSync("PEDIDO_CHAT_modelo.txt");
const instr = modelo.slice(modelo.indexOf("══════", modelo.indexOf("MATÉRIA 2")))
  .replace("Você é professor de Língua Portuguesa e elaborador de questões da banca IDECAN.", "Você é professor da disciplina indicada em cada matéria e elaborador de questões da banca IDECAN.")
  .replace('"subject": "Língua Portuguesa",', '"subject": "(a disciplina indicada na matéria)",')
  .replace("2. As cinco alternativas são frases completas e naturais, do dia a dia de bombeiros e do serviço público,", "2. As cinco alternativas são afirmações ou respostas completas, plausíveis e no mesmo formato entre si (siga as regras próprias da disciplina, indicadas na matéria);");

const head = "══════════════════════════════════════════════════════════════\nMATÉRIAS DESTE PEDIDO\n══════════════════════════════════════════════════════════════\nConcurso: Soldado do Corpo de Bombeiros Militar de Minas Gerais (CBMMG), nível médio. Banca: IDECAN.\n\n";

const RULES: Record<string, { prefix: string; rule: string }> = {
  "Noções de Direitos Humanos e Legislação": {
    prefix: "direito",
    rule: "- REGRAS DE LEGISLAÇÃO: a IDECAN cobra a letra da lei. Cite artigos, incisos e parágrafos pelo número e com o conteúdo fiel ao texto oficial vigente; nunca invente número de artigo, data ou redação. Se não tiver certeza da redação exata de um dispositivo, explique o conteúdo sem citar entre aspas. Distratores trocam um detalhe da lei (prazo, quem decide, \"poderá\" × \"deverá\", exceção). Fontes: planalto.gov.br, almg.gov.br, onu.org.br, cidh.oas.org, gov.br.",
  },
  "Ciências Naturais": {
    prefix: "ciencias",
    rule: "- REGRAS DE CIÊNCIAS NATURAIS: teoria com conceitos, fórmulas, unidades do SI e exemplos resolvidos passo a passo, sempre que possível ligados ao serviço de bombeiro (combustão, gases, pressão, hidráulica, eletricidade, primeiros socorros). Questões de cálculo com dados completos e resolução inteira na explicação (pode chegar a 1.200 caracteres); distratores vêm de erros típicos (unidade trocada, fórmula invertida, sinal). Confira cada conta antes de definir o gabarito.",
  },
  "Ciências Humanas": {
    prefix: "minas-gerais",
    rule: "- REGRAS DE CIÊNCIAS HUMANAS (MINAS GERAIS): use só fatos que você sabe que são verdadeiros e verificáveis. Não invente números, datas, nomes de lugares, altitudes, áreas ou populações; quando não tiver certeza de um dado, trate o assunto sem ele. Prefira conceitos, causas, consequências e exemplos conhecidos. Fontes oficiais: IBGE, governo de MG (mg.gov.br), IGAM, FEAM, IEF, Serviço Geológico do Brasil (sgb.gov.br), ANM, ICMBio.",
  },
  "Proteção e Defesa Civil": {
    prefix: "defesa-civil",
    rule: "- REGRAS DE PROTEÇÃO E DEFESA CIVIL: base legal e técnica real: Lei nº 12.608/2012 (Política Nacional de Proteção e Defesa Civil), Marco de Sendai 2015-2030, classificação de desastres (COBRADE), ciclo de gestão (prevenção, mitigação, preparação, resposta, recuperação), gestão de risco × gestão de desastre. Cite artigos só com redação fiel. Fontes: planalto.gov.br, gov.br/mdr (Defesa Civil Nacional), defesacivil.mg.gov.br.",
  },
};

const slug = (s: string, words = 5) =>
  s.normalize("NFD").replace(/[̀-ͯ]/g, "").toLowerCase().replace(/[^a-z0-9 ]+/g, " ").trim()
    .split(/\s+/).filter((w) => w.length > 2 && !["dos", "das", "nos", "nas", "com", "para", "pela", "pelo", "por", "uma", "que"].includes(w))
    .slice(0, words).join("-");
const titleOf = (t: string) => {
  const first = t.split(/[:;]|\.\s|\s[–-]\s/)[0].replace(/\.$/, "").trim();
  return first.length > 90 ? first.slice(0, 87).replace(/\s\S*$/, "") + "…" : first;
};

const edital = JSON.parse(Deno.readTextFileSync("editais/cbmmg-cfsd-bm-2027.json"));
let current = "";
const topics: { subject: string; text: string }[] = [];
const walk = (o: unknown): void => {
  if (Array.isArray(o)) return o.forEach(walk);
  if (o && typeof o === "object") {
    const r = o as Record<string, unknown>;
    if (typeof r.nome === "string") current = r.nome;
    if (typeof r.disciplina === "string") current = r.disciplina;
    if (typeof r.texto === "string" && !r.topico && RULES[current]) topics.push({ subject: current, text: r.texto });
    Object.values(r).forEach(walk);
  }
};
walk(edital);

const used = new Set<string>();
const block = (n: number, { subject, text }: { subject: string; text: string }) => {
  // Geografia de MG aparece dentro de Ciências Naturais no edital: vale a regra de fatos verificáveis.
  const r = subject === "Ciências Naturais" && /Minas Gerais/.test(text) ? RULES["Ciências Humanas"] : RULES[subject];
  let base = slug(titleOf(text));
  while (used.has(base)) base += "-2";
  used.add(base);
  const short = text.length <= 380
    ? `["${text.replaceAll('"', "'")}"]`
    : `[] (o tópico do edital é longo demais para alias; crie de 3 a 6 aliases curtos com os nomes usuais do assunto)`;
  return `MATÉRIA ${n}
- Arquivos: ${base}.json e recorte-${base}.json
- id: "${r.prefix}.${base}" | title: "${titleOf(text)}" | subject: "${subject}"
- aliases: ${short}
- Tópico do edital (cubra TODOS os itens citados nele): ${text}
${r.rule}`;
};

let lote = 7;
for (let i = 0; i < topics.length; i += 2) {
  const pair = topics.slice(i, i + 2);
  const body = pair.map((t, k) => block(k + 1, t)).join("\n\n");
  Deno.writeTextFileSync(`pedidos/lote-${lote}.txt`, head + body + "\n\n\n" + instr.replace("Entregue os 4 arquivos", pair.length === 1 ? "Entregue os 2 arquivos" : "Entregue os 4 arquivos"));
  console.log(`lote-${lote}: ${pair.map((t) => titleOf(t.text)).join(" + ")}`);
  lote++;
}
