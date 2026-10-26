// Roda o validador oficial num recorte de banca (o mesmo que o importador usa).
//   deno run --allow-read --allow-env scripts/biblioteca/conferir-recorte.ts <recorte.json>
// A matéria do campo "topic" é lida de conteudo/materias/<dir>/<arq>.json.
import { validateBoardNote } from "../../supabase/functions/_shared/library.ts";
const note = JSON.parse(await Deno.readTextFile(Deno.args[0]));
const [dir, file] = String(note.topic).split(".");
const material = JSON.parse(await Deno.readTextFile(new URL(`../../conteudo/materias/${dir}/${file}.json`, import.meta.url)));
try { validateBoardNote(note, material); console.log("OK"); } catch (e) { console.log(((e as { problems?: string[] }).problems ?? [String(e)]).join("\n")); Deno.exit(1); }
