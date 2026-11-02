// Roda o validador oficial do app num arquivo de matéria (o mesmo que o servidor usa).
//   deno run --allow-read --allow-env scripts/biblioteca/conferir-materia.ts conteudo/entrada/materias-codex/<id>.json [--estrito]
// --estrito (usado pelo Codex, 02/11): também reprova letra certa repetida em sequência e enunciado de "Sim/Não".
import { validateLibraryMaterial } from "../../supabase/functions/_shared/library.ts";
const m = JSON.parse(await Deno.readTextFile(Deno.args[0]));
const problemas: string[] = [];
try { validateLibraryMaterial(m); } catch (e) { problemas.push(...((e as { problems?: string[] }).problems ?? [String(e)])); }
if (Deno.args.includes("--estrito")) {
  type Q = { format?: string; options?: { key: string; text: string; correct?: boolean }[] };
  const qs: Q[] = m.questions ?? [];
  let letra = "", seguidas = 0, inicio = 0;
  qs.forEach((q, i) => {
    if (q.format === "TRUE_FALSE") return;
    const certa = q.options?.find((o) => o.correct)?.key ?? "";
    if (certa === letra) seguidas++; else { letra = certa; seguidas = 1; inicio = i + 1; }
    if (seguidas === 4) problemas.push(`questões ${inicio} a ${i + 1}: a letra certa é ${letra} em 4 ou mais seguidas; espalhe (no máximo 2 seguidas)`);
    const textos = (q.options ?? []).map((o) => o.text.trim().toLowerCase().replace(/[.!]$/, ""));
    if (textos.includes("sim") || textos.includes("não")) problemas.push(`questão ${i + 1}: alternativas "Sim/Não"; reescreva como pergunta com 5 alternativas de conteúdo`);
  });
}
if (problemas.length) { console.log(problemas.join("\n")); Deno.exit(1); }
console.log("OK");
