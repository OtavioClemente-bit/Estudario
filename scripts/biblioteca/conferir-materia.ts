// Roda o validador oficial do app num arquivo de matéria (o mesmo que o servidor usa).
//   deno run --allow-read --allow-env scripts/biblioteca/conferir-materia.ts conteudo/entrada/materias-codex/<id>.json
import { validateLibraryMaterial } from "../../supabase/functions/_shared/library.ts";
const m = JSON.parse(await Deno.readTextFile(Deno.args[0]));
try { validateLibraryMaterial(m); console.log("OK"); } catch (e) { console.log(((e as { problems?: string[] }).problems ?? [String(e)]).join("\n")); Deno.exit(1); }
