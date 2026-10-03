// Gera conteudo/editais/trt3-2022--<cargo>.json (um por cargo) a partir do texto do Anexo II do
// edital do TRT da 3ª Região (pdftotext -layout, convertido para UTF-8).
// deno run --allow-read --allow-write scripts/biblioteca/edital-trt3.ts <anexo.txt>
const lines = Deno.readTextFileSync(Deno.args[0]).split(/\r?\n/).map((l) => l.replace(/­/g, "-").replace(/\s+/g, " ").trim());

const isPageNoise = (l: string) => /^\d+$/.test(l) || /^(TRIBUNAL REGIONAL|CONCURSO PÚBLICO|DO QUADRO PERMANENTE|ANEXO II)/.test(l);
const upper = (l: string) => l.length >= 6 && l === l.toUpperCase() && /[A-ZÀ-Ú]{3}/.test(l) && !/^\(/.test(l);

type Disc = { nome: string; texto: string[] };
type Block = { titulo: string; disciplinas: Disc[] };
const blocks: Block[] = [];
let block: Block | null = null;
let disc: Disc | null = null;
let pendingTitle = "";

for (let i = 0; i < lines.length; i++) {
  const l = lines[i];
  if (!l || isPageNoise(l)) continue;
  if (/^CONHECIMENTOS (GERAIS|ESPECÍFICOS)/.test(l)) {
    if (/GERAIS/.test(l)) {
      block = { titulo: `GERAIS ${blocks.filter((b) => b.titulo.startsWith("GERAIS")).length + 1}`, disciplinas: [] };
      blocks.push(block);
    }
    disc = null;
    continue;
  }
  if (/^\((NÍVEL|Para os cargos)/.test(l) || (/^(Analista|Judiciário|Técnico)/.test(l) && !disc)) continue;
  if (/^(TÉCNICO|ANALISTA) JUDICIÁRIO/.test(l)) {
    pendingTitle = l;
    // título do cargo pode quebrar em duas linhas
    if (/(ESPECIALIDADE|SERVIÇO|JUSTIÇA|AGENTE DE)$/.test(l) && upper(lines[i + 1] ?? "")) {
      pendingTitle += " " + lines[++i];
    }
    block = { titulo: pendingTitle.replace(/ - /g, " – "), disciplinas: [] };
    blocks.push(block);
    disc = null;
    continue;
  }
  if (upper(l) && !/^\d/.test(l) && !/\d\/\d{4}\.?$/.test(l)) {
    if (/^NOÇÕES DE DIREITO$/.test(l)) continue;
    disc = { nome: l.replace(/:$/, "").replace(/\.$/, ""), texto: [] };
    block?.disciplinas.push(disc);
    continue;
  }
  if (!disc && block) {
    // cargo sem subtítulo de disciplina: o conteúdo é a própria especialidade
    disc = { nome: "Conhecimentos específicos", texto: [] };
    block.disciplinas.push(disc);
  }
  disc?.texto.push(l);
}

const topicsOf = (text: string) =>
  text.replace(/\s+/g, " ").split(/(?<=[.;])\s+(?=[A-ZÀ-Ú(])/)
    .map((t) => t.trim()).filter((t) => t.length >= 4);

const general = blocks.filter((b) => b.titulo.startsWith("GERAIS"));
const cargos = blocks.filter((b) => !b.titulo.startsWith("GERAIS"));
const superiorGeral = /OFICIAL DE JUSTIÇA|ÁREA JUDICIÁRIA$|ÁREA ADMINISTRATIVA$/;
const slug = (s: string) =>
  s.normalize("NFD").replace(/[̀-ͯ]/g, "").toLowerCase()
    .replace(/(tecnico|analista) judiciario/, "$1").replace(/\b(area|especialidade|apoio especializado)\b/g, " ")
    .replace(/[^a-z0-9]+/g, "-").replace(/^-|-$/g, "");

Deno.mkdirSync("conteudo/editais", { recursive: true });
const written: string[] = [];
for (const cargo of cargos) {
  const geral = superiorGeral.test(cargo.titulo) && cargo.titulo.startsWith("ANALISTA") ? general[1] : general[0];
  const especialidade = cargo.titulo.split(/ESPECIALIDADE /).at(-1)!.replace(/^(TÉCNICO|ANALISTA) JUDICIÁRIO – /, "");
  const disciplinas = [...(geral?.disciplinas ?? []), ...cargo.disciplinas]
    .map((d) => ({
      nome: d.nome === "Conhecimentos específicos" ? especialidade.charAt(0) + especialidade.slice(1).toLowerCase() : d.nome,
      topicos: topicsOf(d.texto.join(" ")).map((texto) => ({ texto, topico: null })),
    }))
    // restos de cabeçalho de página viram "disciplinas" de um tópico só: fora
    .filter((d) => d.topicos.length > 1);
  const file = `conteudo/editais/trt3-2022--${slug(cargo.titulo)}.json`;
  Deno.writeTextFileSync(file, JSON.stringify({
    concurso: "TRT 3ª Região",
    cargo: cargo.titulo.replace(/^(TÉCNICO|ANALISTA) JUDICIÁRIO/, (m) => m.charAt(0) + m.slice(1).toLowerCase()),
    banca: "FUMARC",
    ano: 2022,
    url: "https://www.trt3.jus.br",
    disciplinas,
  }, null, 2) + "\n");
  written.push(`${file}  (${disciplinas.length} disciplinas, ${disciplinas.reduce((n, d) => n + d.topicos.length, 0)} tópicos)`);
}
console.log(written.join("\n"));
