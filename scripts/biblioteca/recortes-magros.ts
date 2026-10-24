// Recortes magros: tópicos do edital que abrem só um pedaço de uma matéria (conteudo/foco) e, com o
// filtro do recorte, ficam com poucos flashcards ou sem memorização ativa (ex.: "Biblioteca Flyway"
// dentro de "Persistência com JPA e Hibernate": 2 cartões, nenhuma memorização).
// Não escreve conteúdo: roda a mesma montagem do servidor (assembleFromLibrary) em cada recorte e
// conta o que sobra, para a equipe completar à mão.
//
//   deno run -A scripts/biblioteca/recortes-magros.ts            mostra o resumo e grava work/recortes-magros.json
//   bash scripts/biblioteca/com-chave.sh scripts/biblioteca/recortes-magros.ts --gravar
//                                                                  também troca a lista da página Equipe › Cobertura
import { assembleFromLibrary } from "../../supabase/functions/_shared/library.ts";

/** Abaixo disso o recorte entra na lista (o app mostra a seção, mas quase vazia). */
const MIN_FLASHCARDS = 4;
const MIN_RECALL = 2;

// deno-lint-ignore no-explicit-any
type Json = any;
const readJson = (path: string): Json => JSON.parse(Deno.readTextFileSync(path));
const materials = new Map<string, Json | null>();
function material(id: string): Json | null {
  if (!materials.has(id)) {
    const [area, name] = id.split(".");
    try { materials.set(id, readJson(`conteudo/materias/${area}/${name}.json`)); } catch { materials.set(id, null); }
  }
  return materials.get(id)!;
}

const input = {
  board: null,
  avoidStatements: [],
  options: {
    blocks: ["THEORY", "SUMMARY", "QUICK_REVIEW", "TIPS_TRAPS", "ACTIVE_RECALL", "QUESTIONS", "ERROR_CONCEPTS"],
    depth: "BOOK", questionCount: 10, questionStyle: "MIXED", difficulty: "MIXED",
  },
};

interface Slice {
  key: string; topic_id: string; title: string; chapters: string; flashcards: number; recall: number;
  total_flashcards: number; total_recall: number; topics: string[]; topic_count: number;
}
const slices = new Map<string, Slice>();
let checked = 0;
for (const file of [...Deno.readDirSync("conteudo/foco")].map((f) => f.name).filter((n) => n.endsWith(".json")).sort()) {
  const foco = readJson(`conteudo/foco/${file}`);
  const m = material(String(foco.materia));
  if (!m) continue;
  const chapters = (m.chapters as Json[]) ?? [];
  for (const topico of foco.topicos as Json[]) {
    const capitulos = Array.isArray(topico.capitulos) ? (topico.capitulos as number[]).filter((i) => Number.isInteger(i) && i >= 0 && i < chapters.length) : [];
    // Matéria inteira não é recorte: vem com todos os cartões.
    if (capitulos.length === 0 || capitulos.length >= chapters.length) continue;
    const key = `${foco.materia}|${[...capitulos].sort((a, b) => a - b).join(",")}`;
    const known = slices.get(key);
    if (known) { known.topic_count++; if (known.topics.length < 8) known.topics.push(String(topico.texto).slice(0, 300)); continue; }
    checked++;
    let content: Json | null = null;
    try {
      content = assembleFromLibrary(m, null, input as never, `recorte:${key}`, { capitulos, questoes: topico.questoes ?? [] }, true);
    } catch { /* recorte que nem monta: entra na lista como vazio */ }
    slices.set(key, {
      key,
      topic_id: String(foco.materia),
      title: String(m.title ?? foco.materia),
      chapters: capitulos.map((i) => String(chapters[i]?.title ?? `cap ${i}`).replace(/^\d+\.\s*/, "")).join(" / ").slice(0, 400),
      flashcards: (content?.flashcards as Json[] | undefined)?.length ?? 0,
      recall: (content?.activeRecall as Json[] | undefined)?.length ?? 0,
      total_flashcards: (m.flashcards as Json[] | undefined)?.length ?? 0,
      total_recall: (m.activeRecall as Json[] | undefined)?.length ?? 0,
      topics: [String(topico.texto).slice(0, 300)],
      topic_count: 1,
    });
  }
}

const thin = [...slices.values()]
  .filter((s) => s.flashcards < MIN_FLASHCARDS || s.recall < MIN_RECALL)
  .sort((a, b) => b.topic_count - a.topic_count || a.flashcards + a.recall - (b.flashcards + b.recall));
console.log(`Recortes conferidos: ${checked} · magros: ${thin.length} (menos de ${MIN_FLASHCARDS} flashcards ou de ${MIN_RECALL} itens de memorização)`);
console.log(`Sem nenhuma memorização: ${thin.filter((s) => s.recall === 0).length} · com 0 ou 1 flashcard: ${thin.filter((s) => s.flashcards <= 1).length}`);
for (const s of thin.slice(0, 15)) console.log(`  ${s.flashcards} cartões · ${s.recall} memorização · ${s.topic_count} tópico(s) · ${s.title} › ${s.chapters.slice(0, 80)}`);
Deno.mkdirSync("work", { recursive: true });
Deno.writeTextFileSync("work/recortes-magros.json", JSON.stringify(thin, null, 1));
console.log("Lista completa em work/recortes-magros.json");

if (Deno.args.includes("--gravar")) {
  const url = Deno.env.get("SUPABASE_URL"), key = Deno.env.get("SUPABASE_SERVICE_ROLE_KEY");
  if (!url || !key) throw new Error("Rode pelo com-chave.sh para gravar.");
  const headers = { apikey: key, authorization: `Bearer ${key}`, "content-type": "application/json", prefer: "return=minimal" };
  const del = await fetch(`${url}/rest/v1/library_thin_slices?key=neq.`, { method: "DELETE", headers });
  if (!del.ok) throw new Error(`Não deu para limpar a lista (${del.status}): ${await del.text()}`);
  for (let i = 0; i < thin.length; i += 500) {
    const res = await fetch(`${url}/rest/v1/library_thin_slices`, { method: "POST", headers, body: JSON.stringify(thin.slice(i, i + 500)) });
    if (!res.ok) throw new Error(`Falhou ao gravar (${res.status}): ${await res.text()}`);
  }
  console.log(`Lista da página Cobertura trocada: ${thin.length} recortes.`);
}
