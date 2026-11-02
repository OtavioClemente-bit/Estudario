// Roda o validador oficial do app num arquivo de matéria (o mesmo que o servidor usa).
//   deno run --allow-read --allow-env scripts/biblioteca/conferir-materia.ts conteudo/entrada/materias-codex/<id>.json [--estrito]
// --estrito (usado pelo Codex, 02/11): também reprova enunciado de "Sim/Não". Sequência de letras não importa:
// o app e o site embaralham as alternativas a cada sessão (OptionShuffle).
import { validateLibraryMaterial } from "../../supabase/functions/_shared/library.ts";
const m = JSON.parse(await Deno.readTextFile(Deno.args[0]));
const problemas: string[] = [];
try { validateLibraryMaterial(m); } catch (e) { problemas.push(...((e as { problems?: string[] }).problems ?? [String(e)])); }
if (Deno.args.includes("--estrito")) {
  type Q = { options?: { text: string }[] };
  ((m.questions ?? []) as Q[]).forEach((q, i) => {
    const textos = (q.options ?? []).map((o) => o.text.trim().toLowerCase().replace(/[.!]$/, ""));
    if (textos.includes("sim") || textos.includes("não")) problemas.push(`questão ${i + 1}: alternativas "Sim/Não"; reescreva como pergunta com 5 alternativas de conteúdo`);
  });
}
if (problemas.length) { console.log(problemas.join("\n")); Deno.exit(1); }
console.log("OK");
