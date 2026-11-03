// Roda o validador oficial do app num arquivo de matéria (o mesmo que o servidor usa).
//   deno run --allow-read --allow-env scripts/biblioteca/conferir-materia.ts conteudo/entrada/materias-codex/<id>.json [--estrito]
// --estrito (usado pelo Codex, 02/11): também reprova enunciado de "Sim/Não". Sequência de letras não importa:
// o app e o site embaralham as alternativas a cada sessão (OptionShuffle).
import { validateLibraryMaterial } from "../../supabase/functions/_shared/library.ts";
const m = JSON.parse(await Deno.readTextFile(Deno.args[0]));
const problemas: string[] = [];
try { validateLibraryMaterial(m); } catch (e) { problemas.push(...((e as { problems?: string[] }).problems ?? [String(e)])); }
if (Deno.args.includes("--estrito")) {
  // Acentos corrompidos (UTF-8 lido duas vezes, "jurÃ­dicas", ou o caractere de troca "�"): reprovaram 32 rascunhos em 03/11.
  const bruto = await Deno.readTextFile(Deno.args[0]);
  const corrompidos = (bruto.match(/Ã[\u0080-¿]|�/g) ?? []).length;
  if (corrompidos > 3) problemas.push(`texto com acentos corrompidos (${corrompidos} trechos como "Ã©" ou "�"): regrave o arquivo inteiro em UTF-8 com os acentos certos`);
  // Apelido acima de 400 caracteres (normalizado) é recusado pelo banco e derruba a publicação inteira (03/11).
  for (const a of (m.aliases ?? []) as string[]) {
    if (a.normalize("NFD").replace(/[̀-ͯ]/g, "").toLowerCase().replace(/[^a-z0-9]+/g, " ").trim().length > 400) problemas.push(`apelido longo demais (máximo 400 caracteres): "${a.slice(0, 60)}…"; use um apelido curto`);
  }
  type Q = { options?: { text: string }[] };
  ((m.questions ?? []) as Q[]).forEach((q, i) => {
    const textos = (q.options ?? []).map((o) => o.text.trim().toLowerCase().replace(/[.!]$/, ""));
    if (textos.includes("sim") || textos.includes("não")) problemas.push(`questão ${i + 1}: alternativas "Sim/Não"; reescreva como pergunta com 5 alternativas de conteúdo`);
  });
}
if (problemas.length) { console.log(problemas.join("\n")); Deno.exit(1); }
console.log("OK");
