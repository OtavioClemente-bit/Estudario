import type { ContentJobInput } from "./text-job-input.ts";
import { AI_TOPIC_CONTENT_SCHEMA_VERSION } from "./schemas/ai-text-jobs-v1.ts";
import { CONTENT_PROMPT_VERSION } from "./prompts/text-jobs-v1.ts";
import { MIN_FLASHCARDS as MIN_LIBRARY_FLASHCARDS, validateTopicContent } from "./text-job-validators.ts";
import { breakAssertivas } from "./content-repair.ts";

// Biblioteca de matérias prontas (conteudo/ no repositório, tabelas library_* no banco).
//
// A matéria canônica é escrita e conferida fora do app, com um banco de questões grande. Quando um
// pedido bate num tópico da biblioteca, o material sai montado daqui, no mesmo formato da IA, sem
// custo: só as partes pedidas, as questões no estilo e dificuldade pedidos e o recorte da banca.

type Json = Record<string, unknown>;

/** Foco de um apelido (tópico estreito): capítulos (índices) e questões (ids) que cobram o tópico. */
export interface LibraryFocus {
  capitulos?: number[];
  questoes?: string[];
}

export const LIBRARY_MODEL_VERSION = "biblioteca";

/** Minúsculas, sem acento, sem numeração de edital ("1.2 Função afim." → "funcao afim"). */
/**
 * Divide um capítulo pelas seções dele ("## " ou "### "), de 2 a 6 partes: o recorte de um capítulo
 * só continua com o formato de matéria. Nulo quando o capítulo não tem seções para dividir.
 */
export function splitChapter(chapter: Json): Json[] | null {
  const lines = String(chapter.markdown ?? "").split("\n");
  const level = ["## ", "### "].find((mark) => lines.filter((line) => line.startsWith(mark)).length >= 2);
  if (!level) return null;
  const parts: { title: string; body: string[] }[] = [];
  const intro: string[] = [];
  for (const line of lines) {
    if (line.startsWith(level)) parts.push({ title: line.slice(level.length).replace(/[*_]/g, "").trim(), body: [] });
    else (parts.length ? parts[parts.length - 1].body : intro).push(line);
  }
  if (intro.join("").trim()) parts[0].body.unshift(...intro);
  // Mais de 6 seções: as últimas entram na sexta.
  while (parts.length > 6) {
    const last = parts.pop()!;
    parts[parts.length - 1].body.push("", level + last.title, ...last.body);
  }
  const out = parts
    .map((part) => ({ title: part.title, markdown: part.body.join("\n").trim() }))
    .filter((part) => part.title && part.markdown);
  return out.length >= 2 ? out : null;
}

/** Palavras que não dizem de que parte do material o tópico fala. */
const FOCUS_STOPWORDS = new Set([
  "de", "da", "do", "das", "dos", "e", "em", "a", "o", "as", "os", "para", "com", "sem", "por", "na", "no", "nas", "nos", "um", "uma",
  "front", "end", "back", "web", "desenvolvimento", "nocoes", "conceitos", "basicos", "fundamentos", "introducao", "linguagem", "linguagens",
]);

/**
 * Sem recorte cadastrado: acha os capítulos do material que o nome do tópico cita (ex.: "Front-end:
 * HTML5." -> o capítulo de HTML5). Só recorta quando o tópico cobre no máximo metade dos capítulos;
 * se o nome é o do material inteiro, segue sem recorte.
 */
export function inferFocus(material: Json, topicTitle: string | null | undefined): LibraryFocus | null {
  const words = normalizeAlias(topicTitle).split(" ").filter((w) => w.length >= 3 && !FOCUS_STOPWORDS.has(w));
  const chapters = (material.chapters as Json[] | undefined) ?? [];
  if (words.length === 0 || chapters.length < 2) return null;
  // "html" acha "html5" e "css" acha "css3": a palavra do tópico abre uma palavra do título.
  const picked = chapters.flatMap((chapter, i) => {
    const titleWords = normalizeAlias(String(chapter.title ?? "")).split(" ");
    return words.some((w) => titleWords.some((t) => t.startsWith(w))) ? [i] : [];
  });
  if (picked.length === 0) return null;
  if (picked.length > Math.floor(chapters.length / 2)) return null;
  return { capitulos: picked, questoes: [] };
}

/**
 * Estilos de questão do banco (campo "estilo") mais parecidos com cada banca, em ordem de preferência,
 * para quando a banca do aluno não tem recorte com questões próprias.
 */
const STYLE_AFFINITY: Record<string, string[]> = {
  "cebraspe": ["CEBRASPE"],
  "fgv": ["FGV", "FCC"],
  "fcc": ["FCC", "VUNESP"],
  "vunesp": ["VUNESP", "FCC"],
  "cesgranrio": ["CESGRANRIO", "FGV"],
  "ibfc": ["CESGRANRIO", "FCC"],
  "aocp": ["FCC", "CESGRANRIO"],
  "idecan": ["CESGRANRIO", "FCC"],
  "consulplan": ["FCC", "CESGRANRIO"],
  "fumarc": ["FCC", "CESGRANRIO"],
  "iades": ["CEBRASPE", "FCC"],
  "quadrix": ["CEBRASPE", "FCC"],
};

/** Estilos preferidos para a banca pedida; bancas não mapeadas ficam com as de múltipla escolha mais comuns. */
export function preferredStyles(board: string | null | undefined): string[] {
  const norm = normalizeBoard(board);
  if (!norm || norm === GENERAL_BOARD_NORM) return [];
  return STYLE_AFFINITY[norm] ?? ["FCC", "CESGRANRIO", "VUNESP"];
}

function styleRank(prefer: string[], question: Json): number {
  const i = prefer.indexOf(String(question.estilo ?? "").toUpperCase());
  return i < 0 ? prefer.length : i;
}

/** Palavras significativas de um tópico de edital, para comparar com o "topico" das questões do banco. */
export function topicWords(value: unknown): string[] {
  return [...new Set(normalizeAlias(typeof value === "string" ? value : "").split(" ").filter((w) => w.length >= 4 && !FOCUS_STOPWORDS.has(w)))];
}

/** A questão é do tópico pedido quando o tópico dela cobre ao menos metade das palavras do pedido. */
export function topicMatches(asked: string[], questionTopic: unknown): boolean {
  if (asked.length === 0) return false;
  const own = new Set(topicWords(questionTopic));
  if (own.size === 0) return false;
  const hits = asked.filter((w) => own.has(w)).length;
  return hits >= Math.max(1, Math.ceil(asked.length / 2));
}

export function normalizeAlias(value: string | null | undefined): string {
  return (value ?? "")
    .normalize("NFD").replace(/[̀-ͯ]/g, "")
    .replace(/[º°ª]/g, "")
    .toLowerCase()
    .replace(/^[\s\d.\-–)]+(?=[a-z])/, "")
    .replace(/[^a-z0-9]+/g, " ")
    .trim();
}

const BOARD_NAMES: Record<string, string> = {
  "cespe": "cebraspe",
  "cespe unb": "cebraspe",
  "cebraspe cespe": "cebraspe",
  "fundacao carlos chagas": "fcc",
  "fundacao getulio vargas": "fgv",
  "instituto aocp": "aocp",
  "instituto ibfc": "ibfc",
  "instituto consulplan": "consulplan",
  "fundacao cesgranrio": "cesgranrio",
  "fundacao fafipa": "fafipa",
  "instituto brasileiro de formacao e capacitacao ibfc": "ibfc",
  "instituto brasileiro de formacao e capacitacao": "ibfc",
  "fundacao getulio vargas mpgo": "fgv",
  "crs pmmg": "pmmg crs",
  "comissao organizadora e examinadora do trf3": "comissao do trf3",
};

/** Recorte "Padrão das provas" (board "GERAL"): vale quando o concurso não tem banca ou a banca não tem recorte. */
export const GENERAL_BOARD_NORM = "geral";

/** Nome da banca no formato da chave: "CESPE/UnB" e "Cebraspe" viram a mesma banca. */
export function normalizeBoard(value: string | null | undefined): string {
  const name = normalizeAlias(value);
  return BOARD_NAMES[name] ?? name;
}

/** Textos do pedido que podem bater num apelido, do mais específico ao mais geral. */
// Matérias de um estado (área = estado): nome, sigla e capital, já normalizados como os apelidos.
// Pará e Sergipe ficam sem a palavra solta ("para", "se" são palavras comuns).
const STATE_MARKERS: Record<string, string[]> = {
  "acre": ["acre", "ac", "rio branco"], "alagoas": ["alagoas", "al", "maceio"], "amapa": ["amapa", "ap", "macapa"],
  "amazonas": ["amazonas", "am", "manaus"], "bahia": ["bahia", "ba", "salvador"], "ceara": ["ceara", "ce", "fortaleza"],
  "distrito-federal": ["distrito federal", "df", "brasilia"], "espirito-santo": ["espirito santo", "es", "vitoria"],
  "goias": ["goias", "go", "goiania"], "maranhao": ["maranhao", "ma", "sao luis"], "mato-grosso": ["mato grosso", "mt", "cuiaba"],
  "mato-grosso-do-sul": ["mato grosso do sul", "ms", "campo grande"], "minas-gerais": ["minas gerais", "mg", "belo horizonte"],
  "para": ["estado do para", "pa", "belem"], "paraiba": ["paraiba", "pb", "joao pessoa"], "parana": ["parana", "pr", "curitiba"],
  "pernambuco": ["pernambuco", "pe", "recife"], "piaui": ["piaui", "pi", "teresina"], "rio-de-janeiro": ["rio de janeiro", "rj"],
  "rio-grande-do-norte": ["rio grande do norte", "rn", "natal"], "rio-grande-do-sul": ["rio grande do sul", "rs", "porto alegre"],
  "rondonia": ["rondonia", "ro", "porto velho"], "roraima": ["roraima", "rr", "boa vista"], "santa-catarina": ["santa catarina", "sc", "florianopolis"],
  "sao-paulo": ["sao paulo", "sp"], "sergipe": ["sergipe", "aracaju"], "tocantins": ["tocantins", "to", "palmas"],
};

/**
 * Matéria de um estado servida a pedido de outro lugar: o texto curto do tópico ("Hidrografia.")
 * foi ligado em editais de Minas e virou apelido de "Hidrografia de Minas Gerais"; quem estuda
 * Ji-Paraná receberia os rios de Minas. A matéria estadual só vale quando o estado (nome, sigla
 * ou capital) aparece no concurso, no órgão, na disciplina ou no tópico.
 */
export function foreignStateMaterial(topicId: string, context: Array<string | null | undefined>): boolean {
  const markers = STATE_MARKERS[String(topicId).split(".")[0]];
  if (!markers) return false;
  const text = ` ${context.map((part) => normalizeAlias(part)).join(" ")} `;
  return !markers.some((marker) => text.includes(` ${marker} `));
}

export function lookupAliases(input: Pick<ContentJobInput, "topicPath"> & { subjectName?: string | null }): string[] {
  const path = input.topicPath.map(normalizeAlias).filter((part) => part.length > 0);
  // Tópico de nome curto ("Java") dentro de uma matéria ("Linguagens de Programação"): tenta também
  // "matéria + tópico", que é específico o bastante para ter apelido na biblioteca.
  const subject = normalizeAlias(input.subjectName);
  const withSubject = subject && path.length > 0 ? `${subject} ${path.at(-1)}` : "";
  // "Matéria + tópico" vem primeiro: o mesmo nome genérico ("Princípios básicos.") é de matérias
  // diferentes conforme a disciplina, e só sem ela caía na primeira que tinha o nome.
  const candidates = [withSubject, path.join(" "), path.at(-1) ?? ""].filter((alias) => alias.length >= 2);
  return [...new Set(candidates)];
}

// ---------------------------------------------------------------------------------------------
// Conferência na importação: o que entra na biblioteca precisa servir qualquer pedido.

export class LibraryValidationError extends Error {
  constructor(public readonly problems: string[]) {
    super(problems.join("\n"));
    this.name = "LibraryValidationError";
  }
}

/** Banco mínimo de questões para servir qualquer combinação de estilo e dificuldade. */
export const LIBRARY_MIN = {
  chapters: 2,
  chapterChars: 800,
  theoryChars: 6_000,
  summaryChars: 400,
  flashcards: 12,
  tips: 3,
  traps: 3,
  activeRecall: 5,
  errorConcepts: 2,
  questions: 40,
  trueFalse: 15,
  fiveOptions: 15,
  perDifficulty: 8,
  explanationChars: 120,
  howItFallsChars: 300,
} as const;

// "TODO" só em maiúsculas: "todo"/"Todo" é palavra comum em português.
const PLACEHOLDERS = {
  test: (value: string) =>
    /lorem ipsum|[[(]a ser preenchid|^a ser preenchid|\[inserir|\[completar|conte[uú]do gen[eé]rico/im.test(value) || /\bxxx\b/.test(value) || /\bTODO\b/.test(value),
};
const KEYS = ["e1", "e2", "e3", "e4", "e5", "e6"];

class Checker {
  problems: string[] = [];
  fail(message: string) {
    this.problems.push(message);
  }
  text(value: unknown, field: string, min = 1, max = 60_000): string {
    if (typeof value === "string" && PLACEHOLDERS.test(value)) this.fail(`${field}: tem texto de rascunho/genérico`);
    if (typeof value !== "string" || value.trim().length < min || value.length > max) {
      this.fail(`${field}: texto ausente, curto demais (mínimo ${min}) ou longo demais`);
      return "";
    }
    return value;
  }
  list(value: unknown, field: string, min: number, max: number): Json[] {
    if (!Array.isArray(value)) {
      this.fail(`${field}: não é uma lista`);
      return [];
    }
    if (value.length < min || value.length > max) this.fail(`${field}: precisa ter de ${min} a ${max} itens (tem ${value.length})`);
    return value.filter((item) => item !== null && typeof item === "object" && !Array.isArray(item)) as Json[];
  }
  strings(value: unknown, field: string, min: number, max: number): string[] {
    if (!Array.isArray(value)) {
      this.fail(`${field}: não é uma lista`);
      return [];
    }
    if (value.length < min || value.length > max) this.fail(`${field}: precisa ter de ${min} a ${max} itens (tem ${value.length})`);
    return value.map((item, index) => this.text(item, `${field}[${index}]`, 10, 2_000));
  }
}

function checkQuestion(c: Checker, question: Json, field: string, chapterTitles: Set<string>, conceptKeys: Set<string>) {
  c.text(question.statement, `${field}.statement`, 20, 4_000);
  const options = c.list(question.options, `${field}.options`, 2, 5);
  const keys = options.map((option) => String(option.key)).join();
  if (question.format === "TRUE_FALSE") {
    if (keys !== "C,E") c.fail(`${field}: Certo/Errado precisa das opções C e E`);
  } else if (question.format === "MULTIPLE_CHOICE") {
    if (keys !== "A,B,C,D,E" && keys !== "A,B,C,D") c.fail(`${field}: múltipla escolha precisa de A–D ou A–E`);
  } else {
    c.fail(`${field}.format: use MULTIPLE_CHOICE ou TRUE_FALSE`);
  }
  if (options.filter((option) => option.correct === true).length !== 1) c.fail(`${field}: precisa de exatamente uma opção correta`);
  options.forEach((option, index) => c.text(option.text, `${field}.options[${index}].text`, 1, 2_000));
  const texts = options.map((option) => String(option.text ?? "").trim().toLowerCase());
  if (new Set(texts).size !== texts.length) c.fail(`${field}: alternativas repetidas`);
  // Com comentário em cada alternativa, o aluno lê a explicação seguida dos comentários
  // (deliverQuestion): o mínimo vale para o conjunto, e a explicação sozinha só precisa do fundamento.
  if (question.format === "MULTIPLE_CHOICE" && options.length > 0 && hasOptionComments(question) && String(question.explanation ?? "").trim().length < LIBRARY_MIN.explanationChars) {
    c.text(question.explanation, `${field}.explanation`, 40, 8_000);
    const delivered = String(question.explanation ?? "").length + options.reduce((sum, option) => sum + String(option.comentario).trim().length, 0);
    if (delivered < LIBRARY_MIN.explanationChars + 60) c.fail(`${field}: explicação e comentários das alternativas curtos demais (mínimo ${LIBRARY_MIN.explanationChars + 60} somados)`);
  } else {
    c.text(question.explanation, `${field}.explanation`, LIBRARY_MIN.explanationChars, 8_000);
  }
  if (!["FACIL", "MEDIA", "DIFICIL"].includes(String(question.difficulty))) c.fail(`${field}.difficulty: use FACIL, MEDIA ou DIFICIL`);
  if (!chapterTitles.has(String(question.section ?? "").trim())) c.fail(`${field}.section: precisa ser o título exato de um capítulo`);
  if (question.errorConceptKey !== null && !conceptKeys.has(String(question.errorConceptKey))) {
    c.fail(`${field}.errorConceptKey: use null ou uma chave de errorConcepts`);
  }
  if (question.sourceType === "REAL") {
    if (typeof question.sourceUrl !== "string" || !/^https?:\/\//.test(question.sourceUrl)) c.fail(`${field}.sourceUrl: questão REAL precisa do link`);
    if (typeof question.board !== "string" || question.board.trim() === "") c.fail(`${field}.board: questão REAL precisa da banca`);
    if (typeof question.year !== "number" || question.year < 1980 || question.year > 2100) c.fail(`${field}.year: questão REAL precisa do ano`);
  } else if (question.sourceType === "AUTHORIAL") {
    if (question.sourceUrl !== null || question.board !== null || question.agency !== null || question.year !== null) {
      c.fail(`${field}: questão AUTHORIAL deixa sourceUrl, board, agency e year como null`);
    }
  } else {
    c.fail(`${field}.sourceType: use AUTHORIAL ou REAL`);
  }
}

function checkStatementsUnique(c: Checker, questions: Json[], field: string, seen = new Set<string>()) {
  questions.forEach((question, index) => {
    const statement = String(question.statement ?? "").trim().toLowerCase();
    if (seen.has(statement)) c.fail(`${field}[${index}]: enunciado repetido`);
    seen.add(statement);
  });
  return seen;
}

/** Confere uma matéria da biblioteca. Lança [LibraryValidationError] com todos os problemas. */
export function validateLibraryMaterial(material: Json): Json {
  const c = new Checker();
  if (typeof material.id !== "string" || !/^[a-z0-9]+(-[a-z0-9]+)*\.[a-z0-9]+(-[a-z0-9]+)*$/.test(material.id)) {
    c.fail("id: use disciplina.topico em minúsculas com hífens (ex.: matematica.funcao-1-grau)");
  }
  c.text(material.subject, "subject", 2, 120);
  c.text(material.title, "title", 2, 200);
  if (!Number.isInteger(material.version) || (material.version as number) < 1) c.fail("version: inteiro a partir de 1");
  if (!["DRAFT", "REVIEWED", "PUBLISHED"].includes(String(material.status))) c.fail("status: DRAFT, REVIEWED ou PUBLISHED");
  const aliases = c.strings(material.aliases, "aliases", 1, 60).map(normalizeAlias);
  if (aliases.some((alias) => alias.length < 2)) c.fail("aliases: apelido vazio depois de normalizar");
  const scope = (material.scope ?? {}) as Json;
  c.text(scope.covers, "scope.covers", 20, 2_000);
  if (typeof scope.excludes !== "string") c.fail("scope.excludes: texto (pode ser vazio)");
  c.text(material.theoryTitle, "theoryTitle", 2, 200);

  const chapters = c.list(material.chapters, "chapters", LIBRARY_MIN.chapters, 6);
  const titles = chapters.map((chapter, index) => c.text(chapter.title, `chapters[${index}].title`, 2, 200).trim());
  const chapterTitles = new Set(titles);
  if (chapterTitles.size !== titles.length) c.fail("chapters: títulos repetidos");
  const theory = chapters.reduce((total, chapter, index) =>
    total + c.text(chapter.markdown, `chapters[${index}].markdown`, LIBRARY_MIN.chapterChars).length, 0);
  if (theory < LIBRARY_MIN.theoryChars) c.fail(`chapters: teoria curta demais (${theory} de ${LIBRARY_MIN.theoryChars} caracteres)`);

  c.text(material.summary, "summary", LIBRARY_MIN.summaryChars);
  const cards = c.list(material.flashcards, "flashcards", LIBRARY_MIN.flashcards, 30);
  const fronts = new Set<string>();
  cards.forEach((card, index) => {
    const front = c.text(card.front, `flashcards[${index}].front`, 3, 240).trim().toLowerCase();
    c.text(card.back, `flashcards[${index}].back`, 3, 3_000);
    if (fronts.has(front)) c.fail(`flashcards[${index}]: frente repetida`);
    fronts.add(front);
  });
  c.strings(material.tips, "tips", LIBRARY_MIN.tips, 8);
  c.strings(material.traps, "traps", LIBRARY_MIN.traps, 8);
  c.list(material.activeRecall, "activeRecall", LIBRARY_MIN.activeRecall, 10).forEach((item, index) => {
    c.text(item.question, `activeRecall[${index}].question`, 5, 1_000);
    c.text(item.answer, `activeRecall[${index}].answer`, 5, 3_000);
  });
  const concepts = c.list(material.errorConcepts, "errorConcepts", LIBRARY_MIN.errorConcepts, 6);
  const conceptKeys = new Set(concepts.map((concept) => String(concept.key)));
  if (conceptKeys.size !== concepts.length || [...conceptKeys].some((key) => !KEYS.includes(key))) {
    c.fail("errorConcepts: chaves e1…e6 sem repetir");
  }
  concepts.forEach((concept, index) => {
    c.text(concept.title, `errorConcepts[${index}].title`, 3, 200);
    c.text(concept.summary, `errorConcepts[${index}].summary`, 20, 2_000);
  });

  const questions = c.list(material.questions, "questions", LIBRARY_MIN.questions, 300);
  questions.forEach((question, index) => checkQuestion(c, question, `questions[${index}]`, chapterTitles, conceptKeys));
  checkStatementsUnique(c, questions, "questions");
  const count = (test: (q: Json) => boolean) => questions.filter(test).length;
  const trueFalse = count((q) => q.format === "TRUE_FALSE");
  const fiveOptions = count((q) => q.format === "MULTIPLE_CHOICE" && (q.options as unknown[])?.length === 5);
  if (trueFalse < LIBRARY_MIN.trueFalse) c.fail(`questions: só ${trueFalse} Certo/Errado (mínimo ${LIBRARY_MIN.trueFalse})`);
  if (fiveOptions < LIBRARY_MIN.fiveOptions) c.fail(`questions: só ${fiveOptions} de 5 alternativas (mínimo ${LIBRARY_MIN.fiveOptions})`);
  for (const level of ["FACIL", "MEDIA", "DIFICIL"]) {
    const total = count((q) => q.difficulty === level);
    if (total < LIBRARY_MIN.perDifficulty) c.fail(`questions: só ${total} ${level} (mínimo ${LIBRARY_MIN.perDifficulty})`);
  }
  // A alternativa certa não pode cair quase sempre na mesma letra.
  const letters = new Map<string, number>();
  questions.filter((q) => q.format === "MULTIPLE_CHOICE").forEach((q) => {
    const key = String((q.options as Json[] | undefined)?.find((o) => o.correct === true)?.key ?? "");
    letters.set(key, (letters.get(key) ?? 0) + 1);
  });
  const multiple = questions.length - trueFalse;
  if (multiple >= 10 && Math.max(0, ...letters.values()) > multiple * 0.4) c.fail("questions: gabarito concentrado demais numa letra");
  // Quem chuta "a mais longa" (ou "a mais curta") não pode acertar quase sempre.
  const extremes = { longest: 0, shortest: 0 };
  questions.filter((q) => q.format === "MULTIPLE_CHOICE").forEach((q) => {
    const sizes = ((q.options as Json[] | undefined) ?? []).map((o) => String(o.text ?? "").length);
    const right = ((q.options as Json[] | undefined) ?? []).findIndex((o) => o.correct === true);
    if (right < 0) return;
    const unique = (value: number) => sizes.filter((size) => size === value).length === 1;
    if (sizes[right] === Math.max(...sizes) && unique(sizes[right])) extremes.longest++;
    if (sizes[right] === Math.min(...sizes) && unique(sizes[right])) extremes.shortest++;
  });
  if (multiple >= 10 && extremes.longest > multiple * 0.4) {
    c.fail(`questions: a alternativa certa é a mais longa em ${extremes.longest} de ${multiple} (máximo 40%); equilibre o tamanho das alternativas`);
  }
  if (multiple >= 10 && extremes.shortest > multiple * 0.4) {
    c.fail(`questions: a alternativa certa é a mais curta em ${extremes.shortest} de ${multiple} (máximo 40%); equilibre o tamanho das alternativas`);
  }
  // Rabicho de molde: a mesma expressão colada no fim de muitas alternativas ("…, no contexto do
  // relato") só serve para igualar tamanho e vira pista (a certa costuma ser a que não tem).
  const tails = new Map<string, number>();
  let optionCount = 0;
  questions.forEach((q) => {
    ((q.options as Json[] | undefined) ?? []).forEach((o) => {
      optionCount++;
      const text = String(o.text ?? "").trim().replace(/[.;!?]+$/, "");
      const comma = text.lastIndexOf(",");
      if (comma < 0) return;
      const tail = text.slice(comma + 1).trim().toLowerCase();
      if (tail.length >= 12 && tail.split(/\s+/).length >= 3) tails.set(tail, (tails.get(tail) ?? 0) + 1);
    });
  });
  for (const [tail, total] of tails) {
    if (total > Math.max(4, optionCount * 0.03)) {
      c.fail(`questions: ${total} alternativas terminam em ", ${tail}"; tire o rabicho de molde e iguale o tamanho com conteúdo`);
    }
  }
  // Pergunta de verdade varia: a mesma última linha de enunciado em quase todas é molde.
  const asks = new Map<string, number>();
  questions.forEach((q) => {
    const ask = String(q.statement ?? "").trim().split("\n").pop()!.replace(/^\d+[.)]\s*/, "").trim().toLowerCase();
    if (ask.length >= 20) asks.set(ask, (asks.get(ask) ?? 0) + 1);
  });
  for (const [ask, total] of asks) {
    if (questions.length >= 20 && total > questions.length * 0.2) {
      c.fail(`questions: a pergunta "${ask.slice(0, 80)}" aparece em ${total} questões; varie o que se pergunta (inferência, sentido no contexto, referência, reescrita…)`);
    }
  }
  // Explicação que só repete o item (e ainda o põe entre aspas como se fosse do texto) não explica
  // nada e esconde gabarito errado.
  questions.forEach((q, index) => {
    const item = String(q.statement ?? "").trim().split("\n").pop()!.replace(/^\d+[.)]\s*/, "").trim();
    if (item.length >= 30 && String(q.explanation ?? "").includes(item)) {
      c.fail(`questions[${index}]: a explicação copia o próprio item; diga com o texto-base por que ele está certo ou errado`);
    }
  });
  // Certo/Errado feito copiando uma alternativa de outra questão é a mesma questão duas vezes; e a
  // explicação herdada ainda fala de "alternativas" que o item C/E não tem.
  const optionTexts = new Set<string>();
  questions.forEach((q) => ((q.options as Json[] | undefined) ?? []).forEach((o) => {
    const text = String(o.text ?? "").trim();
    if (text.length >= 30) optionTexts.add(text);
  }));
  let mirrored = 0;
  questions.forEach((q, index) => {
    if (q.format !== "TRUE_FALSE") return;
    const statement = String(q.statement ?? "");
    if ([...optionTexts].some((text) => statement.includes(text))) mirrored++;
    // Só a referência a alternativas de questão ("alternativa B", "a opção correta", "as demais
    // alternativas"); termos técnicos como "obrigação alternativa" e "opção de compra" passam.
    const explanation = String(q.explanation ?? "");
    if (/\b[Aa]lternativas?\s+\(?[A-E]\b|\b[Oo]p[çc](ão|ões)\s+\(?[A-E]\b/.test(explanation) ||
      /\b(alternativas?|op[çc](ão|ões))\s+(corret|incorret|cert[ao]|errad)|\b(demais|outras|todas as|nenhuma das)\s+(alternativas|op[çc]ões)\b/i.test(explanation)) {
      c.fail(`questions[${index}]: item Certo/Errado com explicação que fala de alternativas/opções; explique o próprio item`);
    }
  });
  if (mirrored > 2) c.fail(`questions: ${mirrored} itens Certo/Errado repetem uma alternativa de outra questão; crie itens próprios`);
  // Enchimento colado depois do ponto ("…à Bahia. após a missão") para igualar tamanho.
  questions.forEach((q, index) => {
    if (((q.options as Json[] | undefined) ?? []).some((o) => /[^.]\.\s+[a-zà-ú]/.test(String(o.text ?? "")))) {
      c.fail(`questions[${index}]: alternativa com trecho colado depois do ponto final; reescreva a frase inteira`);
    }
  });
  // Troca automática de palavra deixa frase quebrada ("qualquer o desmatamento").
  // (?!\p{L}): "qualquer ação" não é "qualquer a" (o \b do JS trata "ç" como fim de palavra).
  const garbled = /\bqualquer\s+(o|a|os|as)(?!\p{L})/iu;
  questions.forEach((q, index) => {
    const texts = [q.statement, q.explanation, ...((q.options as Json[] | undefined) ?? []).map((o) => o.text)];
    if (texts.some((t) => garbled.test(String(t ?? "")))) c.fail(`questions[${index}]: frase quebrada com "qualquer o/a"; revise o texto`);
  });
  // Explicação de verdade fala da questão: a mesma frase colada em muitas é texto de molde.
  const sentences = new Map<string, number>();
  questions.forEach((q) => {
    for (const sentence of new Set(String(q.explanation ?? "").split(/(?<=[.!?])\s+/).map((s) => s.trim()))) {
      if (sentence.length >= 40) sentences.set(sentence, (sentences.get(sentence) ?? 0) + 1);
    }
  });
  for (const [sentence, total] of sentences) {
    if (questions.length >= 20 && total > questions.length * 0.15) {
      c.fail(`questions: a frase "${sentence.slice(0, 80)}…" se repete em ${total} explicações; explique cada questão de forma própria`);
    }
  }

  c.list(material.sources, "sources", 1, 12).forEach((source, index) => {
    if (!["OFICIAL", "COMPLEMENTAR"].includes(String(source.kind))) c.fail(`sources[${index}].kind: OFICIAL ou COMPLEMENTAR`);
    c.text(source.title, `sources[${index}].title`, 3, 400);
    c.text(source.publisher, `sources[${index}].publisher`, 2, 200);
    if (typeof source.reference !== "string") c.fail(`sources[${index}].reference: texto (pode ser vazio)`);
    if (typeof source.url !== "string" || !/^https?:\/\/\S+$/.test(source.url)) c.fail(`sources[${index}].url: link http(s)`);
    if (typeof source.accessedAt !== "string" || !/^\d{4}-\d{2}-\d{2}$/.test(source.accessedAt)) c.fail(`sources[${index}].accessedAt: AAAA-MM-DD`);
  });

  if (c.problems.length === 0) {
    // Prova real: a matéria precisa montar um material aceito pelo app em todos os estilos.
    for (const questionStyle of ["MIXED", "FIVE_OPTIONS", "TRUE_FALSE"] as const) {
      const options = { blocks: ALL_BLOCKS, depth: "BOOK" as const, questionCount: 10, questionStyle, difficulty: "MIXED" as const };
      if (!assembleFromLibrary(material, null, { board: null, options, avoidStatements: [] }, "importacao")) {
        c.fail(`não foi possível montar um material completo no estilo ${questionStyle}`);
      }
    }
  }
  if (c.problems.length > 0) throw new LibraryValidationError(c.problems);
  return material;
}

/** Confere um recorte "como cai" contra a matéria a que ele pertence. */
export function validateBoardNote(note: Json, material: Json): Json {
  const c = new Checker();
  if (note.topic !== material.id) c.fail(`topic: precisa ser o id da matéria (${material.id})`);
  c.text(note.board, "board", 2, 120);
  if (note.role !== null && note.role !== undefined) c.text(note.role, "role", 2, 160);
  if (!Number.isInteger(note.version) || (note.version as number) < 1) c.fail("version: inteiro a partir de 1");
  c.text(note.howItFalls, "howItFalls", LIBRARY_MIN.howItFallsChars, 12_000);
  if (!["ALTA", "MEDIA", "BAIXA"].includes(String(note.incidence))) c.fail("incidence: ALTA, MEDIA ou BAIXA");
  c.strings(note.tips, "tips", 0, 8);
  c.strings(note.traps, "traps", 0, 8);
  const chapterTitles = new Set((material.chapters as Json[]).map((chapter) => String(chapter.title).trim()));
  const conceptKeys = new Set((material.errorConcepts as Json[]).map((concept) => String(concept.key)));
  // Até 600: o recorte da banca guarda também o banco de questões daquela banca (e o Padrão, os lotes do banco 2).
  const questions = c.list(note.questions ?? [], "questions", 0, 600);
  questions.forEach((question, index) => checkQuestion(c, question, `questions[${index}]`, chapterTitles, conceptKeys));
  checkStatementsUnique(c, questions, "questions", checkStatementsUnique(new Checker(), material.questions as Json[], "material"));
  if (c.problems.length > 0) throw new LibraryValidationError(c.problems);
  return note;
}

// ---------------------------------------------------------------------------------------------
// Montagem do material para um pedido.

const ALL_BLOCKS: ContentJobInput["options"]["blocks"] = [
  "THEORY", "SUMMARY", "QUICK_REVIEW", "TIPS_TRAPS", "ACTIVE_RECALL", "QUESTIONS", "ERROR_CONCEPTS",
];

/** Embaralha igual para a mesma semente: pessoas diferentes recebem questões em ordens diferentes. */
export function shuffled<T>(items: T[], seed: string): T[] {
  let state = 2166136261;
  for (const char of seed) state = Math.imul(state ^ char.charCodeAt(0), 16777619);
  const random = () => {
    state = Math.imul(state ^ (state >>> 15), 2246822507);
    state = Math.imul(state ^ (state >>> 13), 3266489909);
    return ((state ^= state >>> 16) >>> 0) / 4294967296;
  };
  const copy = [...items];
  for (let i = copy.length - 1; i > 0; i--) {
    const j = Math.floor(random() * (i + 1));
    [copy[i], copy[j]] = [copy[j], copy[i]];
  }
  return copy;
}

/** Questão do banco: cada alternativa tem "comentario" e a explicação não cita letras. */
function hasOptionComments(question: Json): boolean {
  return (question.options as Json[]).every((option) => typeof option.comentario === "string" && option.comentario.trim() !== "");
}

export function fitsStyle(question: Json, style: ContentJobInput["options"]["questionStyle"]): boolean {
  const optionCount = (question.options as unknown[]).length;
  if (question.format === "TRUE_FALSE") return style === "TRUE_FALSE" || style === "MIXED";
  if (optionCount === 4) return style === "FOUR_OPTIONS";
  // A–E com comentário por alternativa vira A–D tirando uma errada (deliverQuestion).
  if (style === "FOUR_OPTIONS") return optionCount === 5 && hasOptionComments(question);
  return style === "FIVE_OPTIONS" || style === "MIXED";
}

/**
 * Formato que o app conhece: tira os campos do banco ("comentario", "pegadinha", "estilo",
 * "topico") e os leva para a explicação. Em A–D, descarta uma alternativa errada (escolhida pela
 * semente) e refaz as letras; por isso só vale para questão com comentário em cada alternativa.
 */
export function deliverQuestion(question: Json, style: ContentJobInput["options"]["questionStyle"], seed: string): Json {
  const q = structuredClone(question);
  if (typeof q.statement === "string") q.statement = breakAssertivas(q.statement);
  let options = q.options as Json[];
  const commented = q.format === "MULTIPLE_CHOICE" && hasOptionComments(q);
  if (commented && style === "FOUR_OPTIONS" && options.length === 5) {
    const wrong = options.map((option, index) => ({ option, index })).filter(({ option }) => option.correct !== true);
    let hash = 2166136261;
    for (const char of `${seed}:${q.statement}`) hash = Math.imul(hash ^ char.charCodeAt(0), 16777619);
    const drop = wrong[(hash >>> 0) % wrong.length].index;
    options = options.filter((_, index) => index !== drop).map((option, index) => ({ ...option, key: "ABCD"[index] }));
  }
  let explanation = String(q.explanation ?? "");
  if (commented) explanation += `\n\n${options.map((option) => `${option.key}) ${String(option.comentario).trim()}`).join("\n")}`;
  if (typeof q.pegadinha === "string" && q.pegadinha.trim() !== "") explanation += `\n\n**Pegadinha:** ${q.pegadinha.trim()}`;
  q.options = options.map(({ comentario: _comentario, ...option }) => option);
  q.explanation = explanation;
  delete q.pegadinha;
  delete q.estilo;
  delete q.topico;
  return q;
}

const DIFFICULTY = { EASY: "FACIL", MEDIUM: "MEDIA", HARD: "DIFICIL", MIXED: null } as const;

/**
 * Junta até 3 matérias numa só, para um tópico de edital que reúne vários assuntos ("Ortografia.
 * Acentuação. Crase."). O app aceita no máximo 6 capítulos e 6 conceitos de erro: os capítulos de
 * cada matéria são agrupados (cabem 5, sobrando 1 para o recorte da banca) e as questões passam a
 * apontar para o capítulo agrupado e para os conceitos que ficaram.
 */
export function composeMaterials(materials: Json[], title: string): Json {
  if (materials.length === 1) return materials[0];
  const parts = materials.slice(0, 3);
  const perMaterial = Math.max(1, Math.floor(5 / parts.length));
  const conceptsPer = Math.max(1, Math.floor(6 / parts.length));
  const chapters: Json[] = [];
  const errorConcepts: Json[] = [];
  const questions: Json[] = [];
  const seen = new Set<string>();
  parts.forEach((material) => {
    const own = material.chapters as Json[];
    const size = Math.ceil(own.length / perMaterial);
    const sectionOf = new Map<string, string>();
    for (let start = 0; start < own.length; start += size) {
      const group = own.slice(start, start + size);
      const heading = `${String(material.title)}: ${group.map((c) => String(c.title).replace(/^\d+\.\s*/, "")).join(" / ")}`.slice(0, 200);
      group.forEach((c) => sectionOf.set(String(c.title).trim(), heading));
      chapters.push({ title: heading, markdown: group.map((c) => `### ${String(c.title)}\n${String(c.markdown)}`).join("\n\n") });
    }
    const keyOf = new Map<string, string>();
    (material.errorConcepts as Json[]).slice(0, conceptsPer).forEach((concept) => {
      const key = `e${errorConcepts.length + 1}`;
      keyOf.set(String(concept.key), key);
      errorConcepts.push({ ...concept, key });
    });
    for (const question of material.questions as Json[]) {
      const statement = String(question.statement).trim().toLowerCase();
      if (seen.has(statement)) continue;
      seen.add(statement);
      const section = sectionOf.get(String(question.section).trim()) ?? chapters[chapters.length - 1].title;
      const concept = question.errorConceptKey == null ? null : keyOf.get(String(question.errorConceptKey)) ?? null;
      questions.push({ ...question, section, errorConceptKey: concept });
    }
  });
  const flat = (field: string) => parts.flatMap((m) => (m[field] as unknown[] | undefined) ?? []);
  const sources = new Map<string, Json>();
  for (const source of flat("sources") as Json[]) sources.set(String(source.url ?? source.title), source);
  return {
    id: parts.map((m) => m.id).join("+"),
    subject: parts[0].subject,
    title,
    version: parts.reduce((sum, m) => sum + Number(m.version ?? 1), 0),
    scope: {
      covers: parts.map((m) => `${String(m.title)}: ${String((m.scope as Json).covers)}`).join(" "),
      excludes: parts.map((m) => String((m.scope as Json).excludes)).join(" "),
    },
    theoryTitle: title,
    chapters,
    summary: parts.map((m) => `**${String(m.title)}.** ${String(m.summary)}`).join("\n\n"),
    // Intercala para que o recorte de 30/10 itens traga um pouco de cada matéria.
    flashcards: interleave(parts.map((m) => m.flashcards as Json[])),
    tips: interleave(parts.map((m) => m.tips as string[])),
    traps: interleave(parts.map((m) => m.traps as string[])),
    activeRecall: interleave(parts.map((m) => m.activeRecall as Json[])),
    errorConcepts,
    questions,
    sources: [...sources.values()],
  };
}

function interleave<T>(lists: T[][]): T[] {
  const out: T[] = [];
  for (let i = 0; lists.some((list) => i < (list?.length ?? 0)); i++) {
    for (const list of lists) if (list && i < list.length) out.push(list[i]);
  }
  return out;
}

/**
 * Material no formato da IA, montado da biblioteca. Nulo quando a biblioteca não cobre o pedido
 * (questões insuficientes no estilo pedido): aí a IA gera, como antes.
 */
/** Recorte com ao menos isto de cartões próprios fica só com eles. */
const MIN_FOCUSED_FLASHCARDS = 4;

export function assembleFromLibrary(
  material: Json,
  note: Json | null,
  input: Pick<ContentJobInput, "board" | "options" | "avoidStatements"> & { topicPath?: string[] },
  seed: string,
  focus: LibraryFocus | null = null,
  /** Entrega o material mesmo com menos questões que o pedido (quem chama completa as que faltam). */
  partial = false,
): Json | null {
  const options = input.options;
  const wants = (block: string) => options.blocks.includes(block as never);
  // O app manda até 300 caracteres de cada enunciado: o site, começo + fim (itens agrupados repetem o
  // mesmo texto longo no início); o app Android, só o começo. Compara pelas duas formas.
  // O app pode ter guardado o enunciado com as assertivas quebradas em parágrafos (breakAssertivas):
  // compara a forma original e a quebrada, sem espaços, nos dois recortes (começo + fim e começo).
  const squash = (text: string) => text.replace(/\s+/g, "").toLowerCase();
  const headTail = (text: string) => text.length <= 300 ? text : text.slice(0, 150) + text.slice(-150);
  const avoid = new Set(input.avoidStatements.map((statement) => squash(String(statement))));
  const seen = (statement: unknown) => {
    const raw = String(statement).trim();
    return [raw, breakAssertivas(raw)].some((form) => avoid.has(squash(headTail(form))) || avoid.has(squash(form.slice(0, 300))));
  };
  const wanted = DIFFICULTY[options.difficulty];
  // Primeiro as do nível pedido; se não bastarem, completa com o nível mais próximo (difícil →
  // médio → fácil). Assim um pedido "difícil" sai da biblioteca em vez de ir inteiro para a IA.
  const nearest: Record<string, string[]> = { FACIL: ["FACIL", "MEDIA", "DIFICIL"], MEDIA: ["MEDIA", "DIFICIL", "FACIL"], DIFICIL: ["DIFICIL", "MEDIA", "FACIL"] };
  const usable = (pool: Json[]) =>
    pool.filter((q) => fitsStyle(q, options.questionStyle) && !seen(q.statement));
  // As questões do banco trazem o tópico do edital de onde vieram ("topico"): as do mesmo tópico do
  // pedido vêm antes das outras questões da banca sobre a matéria.
  const boardAll = usable((note?.questions as Json[] | undefined) ?? []);
  const asked = topicWords(input.topicPath?.at(-1));
  const sameTopic = boardAll.filter((q) => topicMatches(asked, q.topico));
  const boardPool = [...sameTopic, ...boardAll.filter((q) => !sameTopic.includes(q))];
  const topicPool = usable(material.questions as Json[]);
  // Em cada nível, as questões do recorte da banca vêm antes das da matéria.
  const atLevel = (level: string | null) => (q: Json) => level === null || q.difficulty === level;
  // Banca sem recorte próprio (veio o "Padrão das provas"): as questões no estilo mais parecido com o
  // da banca do aluno vêm antes (ex.: Vunesp → estilo Vunesp e FCC).
  const borrowed = note?.borrowedQuestions === true || typeof note?.ownQuestions === "number" || normalizeBoard(typeof note?.board === "string" ? note.board : "") === GENERAL_BOARD_NORM;
  const prefer = borrowed ? preferredStyles(input.board) : [];
  const byStyle = (pool: Json[]) => prefer.length === 0 ? pool : [...pool].sort((a, b) => styleRank(prefer, a) - styleRank(prefer, b));
  // Recorte da banca completado com o "Padrão das provas": as da própria banca vêm antes.
  const noteQuestions = (note?.questions as Json[] | undefined) ?? [];
  const ownCount = typeof note?.ownQuestions === "number" ? note.ownQuestions : noteQuestions.length;
  const own = new Set(noteQuestions.slice(0, ownCount));
  const ownFirst = (pool: Json[]) => [...pool.filter((q) => own.has(q)), ...byStyle(pool.filter((q) => !own.has(q)))];
  const tier = (board: Json[], topic: Json[]) => (level: string | null) => [
    ...ownFirst(byStyle(shuffled(board.filter((q) => sameTopic.includes(q)).filter(atLevel(level)), seed))),
    ...ownFirst(byStyle(shuffled(board.filter((q) => !sameTopic.includes(q)).filter(atLevel(level)), seed))),
    ...shuffled(topic.filter((q) => level === null || q.difficulty === level), seed),
  ];
  const byLevel = (board: Json[], topic: Json[]) => wanted === null ? tier(board, topic)(null) : nearest[wanted].flatMap(tier(board, topic));
  const allChapters = material.chapters as Json[];
  const focusChapters = (focus?.capitulos ?? []).filter((i) => Number.isInteger(i) && i >= 0 && i < allChapters.length);
  // "q12" é a 12ª questão da matéria, pela posição (muitas matérias não têm id nas questões).
  const allQuestions = material.questions as Json[];
  const focusQuestions = new Set((focus?.questoes ?? []).map((ref) => allQuestions[Number(String(ref).replace(/^q/i, "")) - 1]).filter(Boolean));
  let ordered: Json[];
  if (focusChapters.length === 0 && focusQuestions.size === 0) {
    ordered = byLevel(boardPool, topicPool);
  } else {
    // Tópico estreito: primeiro as questões que cobram o tópico, depois as do mesmo capítulo e só
    // então o resto, para completar a quantidade pedida sem mandar o pedido para a IA.
    const titles = new Set(focusChapters.map((i) => String(allChapters[i].title).trim()));
    const direct = topicPool.filter((q) => focusQuestions.has(q));
    const sameChapter = topicPool.filter((q) => !focusQuestions.has(q) && titles.has(String(q.section).trim()));
    const rest = topicPool.filter((q) => !focusQuestions.has(q) && !titles.has(String(q.section).trim()));
    // Recorte por capítulo: só as questões dele (as que faltarem a IA completa sobre a mesma teoria);
    // as de outros capítulos falariam de assunto que a teoria recortada nem traz.
    // Do recorte da banca, também só as do tópico ou dos capítulos entregues.
    // O "Padrão das provas" junta questões da matéria inteira, às vezes com a seção errada: só as que
    // falam do assunto dos capítulos do recorte.
    const questionFits = focusChapters.length > 0 && focusChapters.length < allChapters.length ? chapterAffinity(allChapters, focusChapters) : null;
    const boardInFocus = boardPool.filter((q) =>
      (sameTopic.includes(q) || titles.has(String(q.section).trim())) &&
      (questionFits === null || questionFits(`${q.statement} ${((q.options as Json[]) ?? []).map((o) => o.text).join(" ")}`)));
    ordered = [...byLevel([], direct), ...byLevel(boardInFocus, sameChapter), ...(focusChapters.length > 0 ? [] : byLevel([], rest))];
  }
  // Misturado: o "Padrão das provas" costuma ser só A–E e vinha antes de tudo, então o aluno não via
  // nenhum Certo/Errado. Reserva cerca de 30% para C/E quando o banco tiver.
  if (options.questionStyle === "MIXED") {
    const trueFalse = ordered.filter((q) => q.format === "TRUE_FALSE");
    const share = Math.min(trueFalse.length, Math.round(options.questionCount * 0.3));
    const picked = new Set([...trueFalse.slice(0, share), ...ordered.filter((q) => q.format !== "TRUE_FALSE").slice(0, options.questionCount - share)]);
    ordered = [...ordered.filter((q) => picked.has(q)), ...ordered.filter((q) => !picked.has(q))];
  }
  const questions = ordered
    .slice(0, options.questionCount)
    .map((question) => deliverQuestion(question, options.questionStyle, seed));
  // "Mais questões" (só o bloco de questões): entrega as inéditas que a biblioteca ainda tem, mesmo
  // que sejam menos que o pedido; a IA só entra quando não sobrou nenhuma.
  const questionsOnly = options.blocks.length === 1 && wants("QUESTIONS");
  if (questions.length === 0 || (questions.length < options.questionCount && !questionsOnly && !partial)) return null;
  // As que completaram a cota vêm de nível vizinho; o app espera o nível pedido em todas, como na
  // geração por IA (content-repair faz o mesmo).
  if (wanted !== null) for (const question of questions) question.difficulty = wanted;

  // A teoria fica sempre na ordem original: capítulos numerados fora de ordem ("3., 4., 1., 2.")
  // pareciam matéria embaralhada. Com recorte, só os capítulos do recorte: pedir "HTML5" não pode
  // trazer CSS, HTTP e APIs junto.
  const focused = focusChapters.length > 0 ? allChapters.filter((_, i) => focusChapters.includes(i)) : allChapters;
  // Um capítulo só não fecha uma matéria (o app pede de 2 a 6): ele vira capítulos pelas seções dele.
  // Um capítulo sem seções não se divide: aí vai a matéria inteira (e o título geral).
  const delivered = focused.length === 1 ? splitChapter(focused[0]) ? focused : allChapters : focused;
  const chapters = !wants("THEORY") ? [] : structuredClone(focused.length === 1 ? splitChapter(focused[0]) ?? allChapters : focused);
  // A seção de cada questão tem que ser um capítulo da teoria entregue (o recorte pode ter mudado os títulos).
  if (chapters.length > 0) {
    const titles = new Set(chapters.map((chapter) => String(chapter.title).trim()));
    for (const question of questions) if (!titles.has(String(question.section ?? "").trim())) question.section = String(chapters[0].title).trim();
  }
  // Com recorte, resumo, flashcards, dicas, pegadinhas e memorização também ficam no recorte: tirar
  // da teoria o capítulo de Flyway e mandar flashcard de Flyway deixava o material incoerente.
  const wholeTheory = focused.length === allChapters.length || (focused.length === 1 && !splitChapter(focused[0]));
  const outside = wholeTheory ? [] : outsideTerms(allChapters.filter((chapter) => !focused.includes(chapter)), focused);
  const fits = wholeTheory ? null : chapterAffinity(allChapters, allChapters.map((chapter, i) => focused.includes(chapter) ? i : -1).filter((i) => i >= 0));
  // Palavra (radical) que só aparece nos capítulos de fora: "interrupção mascarável" num recorte de
  // processos e threads. O item que a usa fala de algo que a teoria entregue não explica.
  const absent = wholeTheory ? null : outsideOnlyStems(allChapters.filter((chapter) => !focused.includes(chapter)), focused);
  const usesAbsent = (value: unknown) => absent !== null && absent.size > 0 && new Set(stems(typeof value === "string" ? value : JSON.stringify(value ?? "")).filter((stem) => absent.has(stem))).size >= 3;
  const mentionsOutside = (value: unknown) =>
    (outside.length > 0 && outside.some((term) => term.test(JSON.stringify(value ?? "")))) || (fits !== null && !fits(value)) || usesAbsent(value);
  // O recorte da banca foi escrito à parte e às vezes cobra o que a matéria nem explica ("interrupção
  // mascarável", "escalonamento cooperativo" num material sem isso). Dica, pegadinha ou frase do
  // recorte apoiada em termos que a teoria entregue não usa fica de fora.
  const taught = new Set(stems(focused.map((chapter) => `${chapter.title} ${chapter.markdown}`).join(" ")));
  const unexplained = (value: unknown) => {
    if (wholeTheory) return false;
    const own = [...new Set(stems(typeof value === "string" ? value : JSON.stringify(value ?? "")))].filter((stem) => !NOTE_PHRASING.has(stem));
    const missing = own.filter((stem) => !taught.has(stem)).length;
    return missing >= 3 && missing / Math.max(own.length, 1) >= 0.35;
  };
  const board = typeof note?.board === "string" ? note.board : input.board;
  const who = normalizeBoard(board) === GENERAL_BOARD_NORM ? "prova" : board;
  // O texto da banca fala da matéria inteira: com recorte, ficam só as frases do assunto entregue.
  const fullHowItFalls = typeof note?.howItFalls === "string" ? note.howItFalls : null;
  const keptHowItFalls = fullHowItFalls
    ? fullHowItFalls.split(/(?<=[.!?])\s+/).filter((sentence) => !mentionsOutside(sentence) && !unexplained(sentence)).join(" ").trim()
    : fullHowItFalls;
  const howItFalls = keptHowItFalls === fullHowItFalls || (keptHowItFalls && keptHowItFalls.length >= 80) ? keptHowItFalls : null;
  let summary = wants("SUMMARY") ? String(material.summary) : "";
  if (summary && (outside.length > 0 || fits !== null)) {
    const kept = summary.split(/(?<=[.!?])\s+/).filter((sentence) => !mentionsOutside(sentence)).join(" ").trim();
    if (kept.length >= 80) summary = kept;
  }
  if (howItFalls && wants("THEORY") && chapters.length < 6) {
    chapters.push({ title: `Como a ${who} cobra este tópico`, markdown: howItFalls });
  } else if (howItFalls && wants("SUMMARY")) {
    summary = `**Como a ${who} cobra:** ${howItFalls}\n\n${summary}`;
  }
  const inFocus = <T>(items: T[]) => outside.length === 0 && fits === null ? items : items.filter((item) => !mentionsOutside(item));
  const merged = (field: "tips" | "traps") =>
    wants("TIPS_TRAPS") ? [...new Set([...inFocus((note?.[field] as string[]) ?? []).filter((item) => !unexplained(item)), ...inFocus(material[field] as string[])])].slice(0, 8) : [];
  // Com recorte, só os cartões do que a teoria entregue ensina, mesmo que sejam poucos. Nunca o baralho
  // inteiro: o aluno decorava cartão de capítulo que nem estava estudando. Poucos demais: entram os
  // que mais tocam o assunto dos capítulos entregues (metade ou mais do vocabulário próprio deles),
  // nunca os de capítulo alheio.
  const ratio = wholeTheory ? null : chapterAffinityRatio(allChapters, allChapters.map((chapter, i) => focused.includes(chapter) ? i : -1).filter((i) => i >= 0));
  const closest = (items: Json[]) => {
    const kept = inFocus(items);
    if (ratio === null || kept.length >= MIN_FOCUSED_FLASHCARDS) return kept;
    const near = items
      .filter((item) => !kept.includes(item) && !(outside.length > 0 && outside.some((term) => term.test(JSON.stringify(item)))) && !usesAbsent(item))
      .map((item) => ({ item, r: ratio(item) }))
      .filter(({ r }) => r >= 0.5)
      .sort((a, b) => b.r - a.r)
      .map(({ item }) => item);
    const extra = new Set(near.slice(0, MIN_FOCUSED_FLASHCARDS - kept.length));
    return items.filter((item) => kept.includes(item) || extra.has(item));
  };
  const cards = closest(material.flashcards as Json[]);
  const recall = closest(material.activeRecall as Json[]);

  const content: Json = {
    schemaVersion: AI_TOPIC_CONTENT_SCHEMA_VERSION,
    promptVersion: CONTENT_PROMPT_VERSION,
    modelVersion: LIBRARY_MODEL_VERSION,
    scope: structuredClone(material.scope),
    // Com recorte, o título geral ("HTML5, CSS3, HTTP, APIs...") prometia o que a teoria não traz:
    // o título passa a ser o dos capítulos entregues.
    theoryTitle: focused.length < allChapters.length
      ? focused.map((chapter) => String(chapter.title).replace(/^\d+\.\s*/, "")).join(" / ").slice(0, 200)
      : material.theoryTitle,
    chapters,
    summary,
    flashcards: wants("QUICK_REVIEW") ? cards.slice(0, 30) : [],
    tips: merged("tips"),
    traps: merged("traps"),
    activeRecall: wants("ACTIVE_RECALL") ? recall.slice(0, 10) : [],
    errorConcepts: structuredClone(material.errorConcepts),
    questions,
    sources: (material.sources as Json[]).slice(0, 12),
    warnings: [],
  };
  try {
    // Mesma conferência do material gerado pela IA: o app recebe exatamente o formato de sempre.
    return validateTopicContent(
      JSON.stringify(content),
      { schemaVersion: AI_TOPIC_CONTENT_SCHEMA_VERSION, promptVersion: CONTENT_PROMPT_VERSION, modelVersion: LIBRARY_MODEL_VERSION },
      // Confere pelo que a biblioteca entrega: com menos que o pedido (parcial ou "mais questões"),
      // quem chama completa o resto. Conferir pelo pedido recusava o material e mandava tudo para a IA.
      { ...options, questionCount: questions.length },
      Math.min(MIN_LIBRARY_FLASHCARDS, cards.length),
    );
  } catch {
    return null;
  }
}

/**
 * Nomes próprios e técnicos (Flyway, Envers, PostgreSQL...) que só existem nos capítulos fora do
 * recorte: escritos com maiúscula no meio da frase pelo menos duas vezes lá e ausentes da teoria
 * entregue. Servem para tirar do resumo, dos flashcards e das dicas o que a teoria recortada não traz.
 */
const AFFINITY_STOP = new Set("para pela pelo pelos pelas como mais menos entre sobre sem com que quando onde qual quais cada todo toda todos todas outro outra outros outras este esta esse essa isso isto aquele aquela seus suas sua seu deve pode podem sendo forma caso casos regra regras prova questao questoes banca exemplo assim apenas ainda tambem porque pois nao sim ser sao foi tem ter".split(" "));
function affinityWords(text: string): string[] {
  return text.normalize("NFD").replace(/[̀-ͯ]/g, "").toLowerCase().match(/[a-z0-9]{4,}/g)?.filter((w) => !AFFINITY_STOP.has(w)) ?? [];
}

/**
 * Para um recorte por capítulo: diz se um item (flashcard, dica, frase do resumo...) é do assunto
 * dos capítulos entregues. Cada item vai para o capítulo cujo vocabulário próprio (palavras que não
 * aparecem em todos os capítulos) ele mais usa; sem vocabulário próprio, fica (é assunto geral).
 * Em direito os assuntos são palavras comuns ("mutação", "eficácia contida"), que outsideTerms não pega.
 */
export function chapterAffinity(chapters: Json[], focusedIdx: number[]): (item: unknown) => boolean {
  const ratio = chapterAffinityRatio(chapters, focusedIdx);
  return (item: unknown) => ratio(item) >= 0.8;
}

/** Quanto o item é dos capítulos entregues: 1 = é deles (ou assunto geral), 0 = é só de capítulo de fora. */
export function chapterAffinityRatio(chapters: Json[], focusedIdx: number[]): (item: unknown) => number {
  const sets = chapters.map((chapter) => new Set(affinityWords(`${chapter.title} ${chapter.markdown}`)));
  const df = new Map<string, number>();
  for (const set of sets) for (const word of set) df.set(word, (df.get(word) ?? 0) + 1);
  const n = chapters.length;
  return (item: unknown) => {
    const words = new Set(affinityWords(typeof item === "string" ? item : JSON.stringify(item ?? "")));
    const scores = sets.map((set) => {
      let score = 0;
      for (const word of words) if (set.has(word)) score += Math.log(n / (df.get(word) ?? n));
      return score;
    });
    const best = Math.max(...scores);
    if (best <= 0) return 1;
    const inside = Math.max(...focusedIdx.map((i) => scores[i] ?? 0));
    return inside / best;
  };
}

/** Palavras de redação dos recortes ("alternativa", "afirma"...), que não são assunto. */
const NOTE_PHRASING = new Set(("alternativa alternativas afirma afirmação afirmativa afirmativas enunciado item itens verifique "
  + "confira observe considere avalie pergunte procure quando aparecer encontrar citarem situação imagine errado errada correta "
  + "correto certo resposta resolver atenção cobra cobrar cobrança costuma frequente mistura misturam generalização falsa "
  + "verdadeira verdadeiros qualificadores absolutos sempre nunca necessariamente apenas somente todas qualquer detalhe "
  + "palavra trecho afirmar escreve prova provas questão questões formato preparação núcleo estudo treino técnico "
  + "possibilidade instante condições sistema determinado característica justamente impede garantia motivar mudança "
  + "também outra outro alguma algum diferença distinguir distinção conceito conceitos").split(" ").flatMap((word) => stems(word)));

function stems(text: string): string[] {
  return affinityWords(text).filter((word) => word.length >= 5).map((word) => word.slice(0, 7));
}

/** Radicais usados nos capítulos de fora do recorte e em nenhum lugar da teoria entregue. */
export function outsideOnlyStems(outside: Json[], inside: Json[]): Set<string> {
  const insideStems = new Set(stems(inside.map((chapter) => `${chapter.title} ${chapter.markdown}`).join(" ")));
  const counts = new Map<string, number>();
  for (const stem of stems(outside.map((chapter) => `${chapter.title} ${chapter.markdown}`).join(" "))) if (!insideStems.has(stem)) counts.set(stem, (counts.get(stem) ?? 0) + 1);
  // Só os termos do assunto de fora (repetidos lá), não palavra solta de redação.
  return new Set([...counts].filter(([, count]) => count >= 2).map(([stem]) => stem));
}

export function outsideTerms(outside: Json[], inside: Json[]): RegExp[] {
  const insideText = inside.map((chapter) => `${chapter.title} ${chapter.markdown}`).join(" ").toLowerCase();
  const counts = new Map<string, number>();
  for (const chapter of outside) {
    const text = `${chapter.title}\n${chapter.markdown}`;
    for (const match of text.matchAll(/(?<=[a-zà-ú,;)] )([A-Z][A-Za-z0-9]{2,})/g)) {
      counts.set(match[1], (counts.get(match[1]) ?? 0) + 1);
    }
  }
  return [...counts]
    .filter(([word, count]) => count >= 2 && !insideText.includes(word.toLowerCase()))
    .map(([word]) => new RegExp(`\\b${word}\\b`, "i"));
}
