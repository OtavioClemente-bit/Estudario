/**
 * Confere, sem IA, se o texto de um PDF tem cara de edital (ou de conteúdo programático) antes de
 * reservar cota. Mesma regra de `EditalGuard.kt` (módulo shared, usado no app e no site): mantenha iguais.
 * Texto curto demais (PDF escaneado) não é julgado.
 */
export const SECTION_TERMS = [
  "conteudo programatico", "conteudos programaticos", "objetos de avaliacao", "objeto de avaliacao",
  "programa das provas", "programas das provas", "conhecimentos basicos", "conhecimentos gerais",
  "conhecimentos especificos", "conteudo das provas",
];
export const CONTEST_TERMS = [
  "edital", "concurso publico", "candidato", "inscricao", "inscricoes", "cargo", "vagas", "prova objetiva",
  "provas objetivas", "banca examinadora", "homologacao", "processo seletivo", "selecao publica", "nomeacao",
  "cadastro de reserva", "taxa de inscricao",
];
export const SUBJECT_TERMS = [
  "lingua portuguesa", "matematica", "raciocinio logico", "informatica", "direito constitucional",
  "direito administrativo", "direito penal", "direito civil", "direito processual", "direito tributario",
  "direito do trabalho", "legislacao", "nocoes de", "atualidades", "contabilidade", "administracao publica",
  "administracao geral", "etica", "lingua inglesa", "lingua espanhola", "historia", "geografia", "fisica",
  "quimica", "biologia", "estatistica", "economia", "auditoria", "arquivologia", "redacao",
];

export interface EditalVerdict { ok: boolean; judged: boolean; sections: number; contest: number; subjects: number }

export function checkEditalText(text: string | null | undefined): EditalVerdict {
  const normalized = (text ?? "").normalize("NFD").replace(/\p{M}+/gu, "").toLowerCase().replace(/\s+/g, " ");
  if (normalized.length < 400) return { ok: true, judged: false, sections: 0, contest: 0, subjects: 0 };
  const sections = SECTION_TERMS.filter((t) => normalized.includes(t)).length;
  const contest = CONTEST_TERMS.filter((t) => normalized.includes(t)).length;
  const subjects = SUBJECT_TERMS.filter((t) => normalized.includes(t)).length;
  const ok = subjects >= 4 || (sections >= 1 && subjects >= 2) || contest >= 3;
  return { ok, judged: true, sections, contest, subjects };
}

/**
 * A IA recusou por não haver conteúdo programático (ela devolve uma "proposta" de 1 tópico avisando
 * isso). Tratar como falha, para a cota voltar à pessoa.
 */
export function isRefusalProposal(proposal: unknown): boolean {
  if (!proposal || typeof proposal !== "object") return false;
  const p = proposal as { subjects?: Array<{ topics?: unknown[] }>; warnings?: unknown[] };
  const topics = (p.subjects ?? []).reduce((n, s) => n + (Array.isArray(s?.topics) ? s.topics.length : 0), 0);
  if (topics > 3) return false;
  const words = JSON.stringify(p).normalize("NFD").replace(/\p{M}+/gu, "").toLowerCase();
  return /recusad|nao contem|nao ha conteudo|sem conteudo programatico|no syllabus|not a syllabus|does not contain/.test(words);
}
