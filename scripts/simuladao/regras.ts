// Regras comuns dos montadores de Simuladão (montar-primeiro.ts e montar.ts), da auditoria de 26/11/2026
// (work/auditoria-simulados-2026-11-26/relatorio.md). Só decidem QUAIS questões prontas podem entrar e
// quantas por disciplina; nenhuma regra escreve, altera ou completa texto de questão (AGENTS.md).

type Json = Record<string, unknown>;
type Option = { key: string; text: string; correct?: boolean };

/** No máximo 3 provas por edital (26/11, dono). */
export const MAX_PROVAS = 3;

/** Enunciado normalizado (a mesma chave da auditoria: sem acento, sem pontuação, minúsculas). */
export const normKey = (s: unknown) =>
  String(s ?? "").toLowerCase().normalize("NFD").replace(/[̀-ͯ]/g, "").replace(/[^a-z0-9]+/g, " ").trim();

/** Chave do enunciado como o app guarda (questionKey dos montadores antigos), para não perder repetição antiga. */
export function appKey(statement: unknown): string {
  const letters = String(statement ?? "").toLowerCase().replace(/[^\p{L}\p{Nd}]/gu, "").slice(0, 160);
  let hash = 2166136261;
  for (let i = 0; i < letters.length; i++) hash = Math.imul(hash ^ letters.charCodeAt(i), 16777619);
  return (hash >>> 0).toString(16).padStart(8, "0");
}

/** Texto que vai para a prova publicada (o importar.ts põe o texto-base na frente do enunciado). */
export const textoPublicado = (q: Json) => (q.textoBase ? `${q.textoBase}\n\n${q.statement}` : String(q.statement ?? ""));

/** Conjunto de enunciados já usados: guarda as duas chaves de cada questão. */
const chavesCache = new WeakMap<Json, string[]>();
const chaves = (q: Json) => {
  let c = chavesCache.get(q);
  if (!c) { c = [...new Set([textoPublicado(q), String(q.statement ?? "")].flatMap((t) => ["n:" + normKey(t), "a:" + appKey(t)]))]; chavesCache.set(q, c); }
  return c;
};
export class Usadas {
  private s = new Set<string>();
  add(q: Json) { for (const k of chaves(q)) this.s.add(k); }
  has(q: Json) { return chaves(q).some((k) => this.s.has(k)); }
}

/** A questão tem exatamente as alternativas do formato da prova: A–E (5) ou C/E (2), com uma certa só. */
export function alternativasCertas(q: Json, formato: string): boolean {
  const o = q.options as Option[] | undefined;
  if (!Array.isArray(o)) return false;
  if (o.filter((x) => x.correct === true).length !== 1) return false;
  if (o.some((x) => !String(x.text ?? "").trim())) return false;
  const keys = o.map((x) => String(x.key).toUpperCase()).join("");
  return formato === "CERTO_ERRADO" ? keys === "CE" : keys === "ABCDE";
}

// Aderência do tópico à disciplina: matéria de área "básica" (português, matemática/RLM, informática,
// língua estrangeira) só entra numa disciplina cujo nome ou texto do tópico no edital fale dessa área.
// Pega casos como função afim dentro de "Técnicas de Gestão Ambiental" (auditoria de 26/11).
const AREAS: Record<string, { areas: string[]; re: RegExp }> = {
  pt: { areas: ["portugues", "lingua-portuguesa", "redacao-oficial", "linguistica"], re: /portugu|l[ií]ngua|linguage|reda[cç]|gram[aá]t|texto|textua|comunica|letras|literat|ortogra|leitura|escrit/i },
  mat: { areas: ["matematica", "matematica-financeira", "matematica-grandezas", "raciocinio-logico", "estatistica", "estatistica-e-probabilidade"], re: /matem|racioc|l[oó]gic|quantitat|estat[ií]st|probabil|financ|c[aá]lcul|exat|n[uú]mer|aritm|geometr|[aá]lgebra|fun[cç][aã]o|porcent|propor|medida|grandeza|contab|econom|f[ií]sica|engenhar|dosage|juros|equa[cç]/i },
  inf: { areas: ["informatica"], re: /inform[aá]t|tecnolog|comput|\bti\b|sistema|dados|digital|software|rede|seguran[cç]a da informa|internet|planilh|editor|office|windows|linux|program/i },
  lang: { areas: ["ingles", "ingles-tecnico", "lingua-inglesa", "lingua-espanhola", "lingua-estrangeira", "lingua-francesa", "lingua-alema", "lingua-japonesa"], re: /ingl|espanh|estrangeir|idioma|l[ií]ngua|franc[eê]s|alem[aã]|japon|english|spanish/i },
};
const GERAL = /b[aá]sic|gerais|geral\b|espec[ií]fic|cargo|profiss|complementar|m[uú]ltipl|diversos|parte \w+$/i;
export function topicoCabe(disciplina: string, textoTopico: string, materia: string): boolean {
  const area = materia.split(".")[0];
  for (const g of Object.values(AREAS)) {
    if (!g.areas.includes(area)) continue;
    return g.re.test(disciplina) || g.re.test(textoTopico) || GERAL.test(disciplina);
  }
  return true;
}

/**
 * Divide `total` questões entre disciplinas pelo peso (nº de tópicos com questão disponível), sem nenhuma
 * passar do teto (40%, ou a fatia igual quando há poucas disciplinas). Usado só quando a prova real não diz
 * quantas questões tem cada disciplina (modo aproximado, autorizado em 24/11).
 */
export function dividir(total: number, pesos: number[], teto = 0.4): number[] {
  const k = pesos.filter((p) => p > 0).length;
  if (k === 0) return pesos.map(() => 0);
  const max = Math.max(Math.floor(total * Math.max(teto, 1 / k)), Math.ceil(total / k));
  const out = pesos.map(() => 0);
  let resto = total;
  let abertos = pesos.map((p, i) => (p > 0 ? i : -1)).filter((i) => i >= 0);
  // Enche por proporção; quem bate no teto sai e o resto é redividido entre os outros.
  while (resto > 0 && abertos.length) {
    const soma = abertos.reduce((a, i) => a + pesos[i], 0);
    const quer = abertos.map((i) => Math.max(1, Math.floor((resto * pesos[i]) / soma)));
    let deu = 0;
    abertos.forEach((i, j) => { const n = Math.min(quer[j], max - out[i], resto - deu); if (n > 0) { out[i] += n; deu += n; } });
    resto -= deu;
    abertos = abertos.filter((i) => out[i] < max);
    if (deu === 0) break;
  }
  // Sobra de arredondamento: um a um, para quem tem mais peso e ainda cabe.
  const ordem = pesos.map((p, i) => i).filter((i) => pesos[i] > 0).sort((a, b) => pesos[b] - pesos[a]);
  for (let t = 0; resto > 0 && t < total * 4; t++) { const i = ordem[t % ordem.length]; if (out[i] < max) { out[i]++; resto--; } }
  return out;
}

/** Quadro de questões por disciplina lido à mão dos editais (conteudo/pesos-provas.json): os editais com prova real conhecida. */
export function editaisComQuadro(root: string): Set<string> {
  try {
    const p = JSON.parse(Deno.readTextFileSync(`${root}conteudo/pesos-provas.json`)) as { editais?: string[] }[];
    return new Set(p.flatMap((x) => x.editais ?? []));
  } catch { return new Set(); }
}
