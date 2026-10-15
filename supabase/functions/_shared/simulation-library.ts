import { deliverQuestion, fitsStyle, GENERAL_BOARD_NORM, inferFocus, type LibraryFocus, lookupAliases, normalizeAlias, normalizeBoard, shuffled } from "./library.ts";
import { questionKey, type RematchSource, type SimulationItem, type SimulationJobInput } from "./simulation.ts";

// Simulado montado da biblioteca: as questões prontas e conferidas de cada tópico da planta, sem
// custo de IA. Nada que a pessoa já tenha visto (chaves em input.seen), dificuldade na proporção
// de uma prova de verdade e, quando dá, questões em cima dos conceitos que ela mais erra. Só o que
// a biblioteca não cobre vai para a IA.

type Json = Record<string, unknown>;

export interface SimulationLibraryHit {
  topicId: string;
  material: Json;
  note: Json | null;
  focus?: LibraryFocus | null;
}

export interface SimulationLibraryStore {
  libraryLookup?(aliases: string[], boardNorm: string, roleNorm: string): Promise<SimulationLibraryHit | null>;
  libraryBoardNote?(topicId: string, boardNorm: string, roleNorm: string): Promise<Json | null>;
}

export interface SimulationLibraryPlan {
  /** Questões já prontas, no formato da parte do simulado (itemRef de cada uma). */
  questions: Json[];
  /** O que sobra para a IA (mesmas refs do pedido); nulo quando a biblioteca cobriu tudo. */
  remaining: SimulationJobInput | null;
  /** Entrada com as quantidades que a biblioteca de fato entregou (o diagnóstico redistribui). */
  delivered: SimulationJobInput;
}

/** Proporção de dificuldade por tipo de simulado (fácil, média, difícil). */
const LEVEL_MIX: Record<SimulationJobInput["mode"], [number, number, number]> = {
  // Diagnóstico mede o nível: um pouco de tudo, para separar quem sabe o básico de quem domina.
  DIAGNOSTIC: [0.35, 0.4, 0.25],
  // Do que estudou: já passou pela matéria, então pesa mais no médio e no difícil.
  STUDIED: [0.2, 0.5, 0.3],
  // Prova completa: a curva de uma prova real.
  FULL: [0.25, 0.5, 0.25],
  REMATCH: [0.2, 0.5, 0.3],
};
const LEVELS = ["FACIL", "MEDIA", "DIFICIL"] as const;

function squash(value: unknown): string {
  return normalizeAlias(typeof value === "string" ? value : "");
}

interface Candidate {
  question: Json;
  material: Json;
  key: string;
  level: string;
  weak: number | null;
}

/** Ordem de preferência das questões de um tópico, igual em todas as partes do mesmo simulado. */
function candidatesFor(hit: SimulationLibraryHit, item: Pick<SimulationItem, "topicPath">, input: SimulationJobInput, seed: string): Candidate[] {
  const material = hit.material;
  const all = (material.questions as Json[] | undefined) ?? [];
  const chapters = (material.chapters as Json[] | undefined) ?? [];
  const focus = hit.focus ?? inferFocus(material, item.topicPath.at(-1));
  const focusChapters = (focus?.capitulos ?? []).filter((i) => Number.isInteger(i) && i >= 0 && i < chapters.length);
  const direct = new Set((focus?.questoes ?? []).map((ref) => all[Number(String(ref).replace(/^q/i, "")) - 1]).filter(Boolean));
  const titles = new Set(focusChapters.map((i) => String(chapters[i].title).trim()));
  const inChapter = (q: Json) => titles.has(String(q.section ?? "").trim());
  const noteQuestions = (hit.note?.questions as Json[] | undefined) ?? [];
  // Tópico estreito: as que cobram o tópico, depois o recorte da banca e o mesmo capítulo; o resto
  // da matéria só entra quando o tópico é a matéria inteira (senão a questão fugiria do tópico).
  const narrow = focusChapters.length > 0 || direct.size > 0;
  const ordered = narrow
    ? [
      ...shuffled([...direct], seed),
      ...shuffled(noteQuestions, seed),
      ...shuffled(all.filter((q) => !direct.has(q) && inChapter(q)), seed),
    ]
    : [...shuffled(noteQuestions, seed), ...shuffled(all, seed)];
  const concepts = new Map(((material.errorConcepts as Json[] | undefined) ?? []).map((c) => [String(c.key), c]));
  const weakNorm = input.weakSpots.map((spot) => squash(spot.split(":")[0]));
  const seen = new Set(input.seen);
  const keys = new Set<string>();
  const out: Candidate[] = [];
  for (const question of ordered) {
    if (!Array.isArray(question.options) || !fitsStyle(question, input.style)) continue;
    const key = questionKey(String(question.statement ?? ""));
    if (seen.has(key) || keys.has(key)) continue;
    keys.add(key);
    const concept = concepts.get(String(question.errorConceptKey ?? ""));
    const title = squash(concept?.title);
    const weak = title ? weakNorm.findIndex((spot) => spot.length > 0 && (spot === title || spot.includes(title) || title.includes(spot))) : -1;
    out.push({ question, material, key, level: String(question.difficulty ?? "MEDIA"), weak: weak >= 0 ? weak : null });
  }
  return out;
}

/** Converte a questão do banco no formato da parte do simulado. */
function toSimulationQuestion(candidate: Candidate, itemRef: string, input: SimulationJobInput, seed: string): Json {
  const raw = candidate.question;
  const concept = ((candidate.material.errorConcepts as Json[] | undefined) ?? []).find((c) => String(c.key) === String(raw.errorConceptKey ?? ""));
  const pegadinha = typeof raw.pegadinha === "string" ? raw.pegadinha.trim() : "";
  const delivered = deliverQuestion(raw, input.style === "MIXED" ? "MIXED" : input.style, seed);
  // A pegadinha do banco já vai na explicação (deliverQuestion); a armadilha à parte é o conceito
  // que essa questão testa, que é exatamente onde a pessoa erra.
  const trap = pegadinha ? "" : concept ? `${String(concept.title).trim()}${concept.summary ? `: ${String(concept.summary).trim()}` : ""}` : "";
  return {
    itemRef,
    statement: delivered.statement,
    format: delivered.format,
    difficulty: LEVELS.includes(candidate.level as typeof LEVELS[number]) ? candidate.level : "MEDIA",
    options: (delivered.options as Json[]).map((option) => ({ key: option.key, text: option.text, correct: option.correct === true })),
    explanation: delivered.explanation,
    trap,
    targetsWeakSpot: candidate.weak,
    fromLibrary: true,
  };
}

/** Escolhe n questões seguindo a proporção de dificuldade da parte e puxando os pontos fracos. */
class Picker {
  private readonly deficit: number[];
  private weakLeft: number;
  constructor(total: number, mode: SimulationJobInput["mode"], hasWeak: boolean) {
    this.deficit = LEVEL_MIX[mode].map((share) => share * total);
    // Cerca de 1 em cada 4 questões em cima de uma confusão da pessoa.
    this.weakLeft = hasWeak ? Math.round(total / 4) : 0;
  }
  take(pool: Candidate[], used: Set<string>): Candidate | null {
    const free = pool.filter((c) => !used.has(c.key));
    if (free.length === 0) return null;
    const order = [0, 1, 2].sort((a, b) => this.deficit[b] - this.deficit[a]);
    const byLevel = (list: Candidate[]) => {
      for (const index of order) {
        const found = list.find((c) => c.level === LEVELS[index]);
        if (found) return found;
      }
      return list[0] ?? null;
    };
    let chosen: Candidate | null = null;
    if (this.weakLeft > 0) chosen = byLevel(free.filter((c) => c.weak !== null));
    if (chosen) this.weakLeft--;
    chosen ??= byLevel(free);
    if (!chosen) return null;
    const level = LEVELS.indexOf(chosen.level as typeof LEVELS[number]);
    if (level >= 0) this.deficit[level] -= 1;
    used.add(chosen.key);
    return chosen;
  }
}

/** Item dividido entre partes: cada fatia fica com posições alternadas da mesma lista, sem repetir. */
function slice(pool: Candidate[], chunk: number, chunks: number): Candidate[] {
  if (chunks <= 1) return pool;
  const own = chunk % chunks;
  return pool.filter((_, index) => index % chunks === own);
}

async function hitFor(store: SimulationLibraryStore, subjectName: string | null, topicPath: string[], input: SimulationJobInput): Promise<SimulationLibraryHit | null> {
  if (!store.libraryLookup || topicPath.length === 0) return null;
  const boardNorm = normalizeBoard(input.board);
  const hit = await store.libraryLookup(lookupAliases({ topicPath, subjectName }), boardNorm, normalizeAlias(input.role)).catch(() => null);
  if (!hit) return null;
  if (!hit.note) hit.note = await store.libraryBoardNote?.(hit.topicId, GENERAL_BOARD_NORM, "").catch(() => null) ?? null;
  return hit;
}

/**
 * Planta da parte resolvida pela biblioteca. No diagnóstico (o do plano Grátis), o que a biblioteca
 * não cobre passa para os outros tópicos da mesma matéria (ou de outra): ele nunca chama a IA
 * quando há biblioteca para ao menos um tópico.
 */
export async function planSimulationFromLibrary(input: SimulationJobInput, store: SimulationLibraryStore, jobSeed: string): Promise<SimulationLibraryPlan> {
  const seed = input.seed || jobSeed;
  const total = input.items.reduce((sum, item) => sum + item.count, 0) + input.rematch.length;
  const picker = new Picker(total, input.mode, input.weakSpots.length > 0);
  const used = new Set<string>();
  const questions: Json[] = [];
  const pools = await Promise.all(input.items.map(async (item) => {
    const hit = await hitFor(store, item.subjectName, item.topicPath, input);
    if (!hit) return null;
    const pool = candidatesFor(hit, item, input, `${seed}:${item.topicPath.join(">")}`);
    return slice(pool, item.chunk, item.chunks);
  }));
  const got = new Map<string, number>();
  const short = new Map<string, number>();
  input.items.forEach((item, index) => {
    const pool = pools[index];
    let taken = 0;
    for (let i = 0; i < item.count && pool; i++) {
      const chosen = picker.take(pool, used);
      if (!chosen) break;
      questions.push(toSimulationQuestion(chosen, item.ref, input, seed));
      taken++;
    }
    got.set(item.ref, taken);
    if (taken < item.count) short.set(item.ref, item.count - taken);
  });
  // Diagnóstico: o que faltou vai para os tópicos cobertos, primeiro os da mesma matéria.
  if (input.mode === "DIAGNOSTIC" && short.size > 0) {
    for (const [ref, missing] of [...short]) {
      const subject = input.items.find((item) => item.ref === ref)!.subjectName;
      const donors = input.items
        .map((item, index) => ({ item, pool: pools[index] }))
        .filter(({ item, pool }) => pool && item.ref !== ref)
        .sort((a, b) => Number(b.item.subjectName === subject) - Number(a.item.subjectName === subject));
      let left = missing;
      for (let round = 0; left > 0 && round < 10; round++) {
        let progressed = false;
        for (const { item, pool } of donors) {
          if (left === 0) break;
          const chosen = picker.take(pool!, used);
          if (!chosen) continue;
          // Entra logo depois das outras do mesmo item, para a prova continuar na ordem do edital.
          const at = questions.map((q) => q.itemRef).lastIndexOf(item.ref);
          questions.splice(at + 1, 0, toSimulationQuestion(chosen, item.ref, input, seed));
          got.set(item.ref, (got.get(item.ref) ?? 0) + 1);
          left--;
          progressed = true;
        }
        if (!progressed) break;
      }
      if (left === 0) short.delete(ref);
      else short.set(ref, left);
    }
  }
  // Revanche: outra questão do mesmo conceito (ou do mesmo capítulo) da que a pessoa errou.
  const rematchLeft: RematchSource[] = [];
  for (const source of input.rematch) {
    const variant = await rematchFromLibrary(source, input, store, seed, used).catch(() => null);
    if (variant) questions.push(variant);
    else rematchLeft.push(source);
  }
  const remainingItems = input.items.filter((item) => short.has(item.ref)).map((item) => ({ ...item, count: short.get(item.ref)! }));
  const remaining = remainingItems.length > 0 || rematchLeft.length > 0 ? { ...input, items: remainingItems, rematch: rematchLeft } : null;
  const delivered: SimulationJobInput = {
    ...input,
    items: input.items.filter((item) => (got.get(item.ref) ?? 0) > 0).map((item) => ({ ...item, count: got.get(item.ref)! })),
    rematch: input.rematch.filter((source) => !rematchLeft.includes(source)),
  };
  return { questions, remaining, delivered };
}

async function rematchFromLibrary(source: RematchSource, input: SimulationJobInput, store: SimulationLibraryStore, seed: string, used: Set<string>): Promise<Json | null> {
  const hit = await hitFor(store, source.subjectName, source.topicPath, input);
  if (!hit) return null;
  const all = (hit.material.questions as Json[] | undefined) ?? [];
  const originalKey = questionKey(source.statement);
  const original = all.find((q) => questionKey(String(q.statement ?? "")) === originalKey);
  const pool = candidatesFor(hit, { topicPath: source.topicPath }, input, `${seed}:r:${originalKey}`)
    .filter((c) => c.key !== originalKey && !used.has(c.key));
  // Mesmo conceito de erro primeiro (é a mesma confusão com outro cenário), depois o mesmo capítulo.
  const sameConcept = original?.errorConceptKey != null
    ? pool.filter((c) => c.question.errorConceptKey === original.errorConceptKey)
    : [];
  const sameSection = original ? pool.filter((c) => String(c.question.section ?? "") === String(original.section ?? "")) : [];
  const chosen = sameConcept[0] ?? sameSection[0] ?? (original ? null : pool[0]) ?? null;
  if (!chosen) return null;
  used.add(chosen.key);
  return toSimulationQuestion(chosen, source.ref, input, seed);
}
