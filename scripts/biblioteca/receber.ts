// Recebe os JSON do chat (pasta Downloads), coloca cada um no lugar certo de conteudo/ e aponta
// sinais de problema que o conferidor não pega. Não altera o servidor.
// deno run --allow-read --allow-write scripts/biblioteca/receber.ts <pasta> [arquivo...]
const dir = Deno.args[0];
const only = new Set(Deno.args.slice(1));
type Json = Record<string, any>;
const norm = (s: string) => s.normalize("NFD").replace(/[̀-ͯ]/g, "").toLowerCase();

const report: string[] = [];
for (const entry of [...Deno.readDirSync(dir)].sort((a, b) => a.name.localeCompare(b.name))) {
  if (!entry.name.endsWith(".json") || (only.size && !only.has(entry.name))) continue;
  let data: Json;
  try { data = JSON.parse(Deno.readTextFileSync(`${dir}/${entry.name}`)); } catch { report.push(`✗ ${entry.name}: JSON inválido`); continue; }
  if (typeof data.topic === "string" && data.board) {
    const boardDir = `conteudo/recortes/${String(data.board).normalize("NFD").replace(/[̀-ͯ]/g, "").toLowerCase().replace(/[^a-z0-9]+/g, "-")}`;
    Deno.mkdirSync(boardDir, { recursive: true });
    Deno.writeTextFileSync(`${boardDir}/${data.topic}.json`, JSON.stringify(data, null, 2) + "\n");
    continue;
  }
  if (typeof data.id !== "string" || !Array.isArray(data.questions)) continue;
  const area = data.id.split(".")[0];
  Deno.mkdirSync(`conteudo/materias/${area}`, { recursive: true });
  const target = `conteudo/materias/${area}/${data.id.split(".")[1]}.json`;
  Deno.writeTextFileSync(target, JSON.stringify(data, null, 2) + "\n");

  const qs: Json[] = data.questions;
  const mc = qs.filter((q) => q.format === "MULTIPLE_CHOICE");
  const letters: Record<string, number> = {};
  const levels: Record<string, number> = {};
  const flags: string[] = [];
  qs.forEach((q, i) => {
    levels[`${q.format === "TRUE_FALSE" ? "CE" : "ME"}-${q.difficulty}`] = (levels[`${q.format === "TRUE_FALSE" ? "CE" : "ME"}-${q.difficulty}`] ?? 0) + 1;
    const right = (q.options ?? []).filter((o: Json) => o.correct);
    if (right.length !== 1) flags.push(`#${i} tem ${right.length} corretas`);
    const exp = String(q.explanation ?? "");
    if (/n[aã]o (possui|tem) resposta|duas respostas|mais de uma (resposta|alternativa) (correta|poss[ií]vel)|item (est[aá]|fica) (inv[aá]lido|amb[ií]guo)|corrigir (a|o) (quest|item|alternativa)|anular/i.test(exp)) flags.push(`#${i} explicação admite problema`);
    if (q.format === "MULTIPLE_CHOICE" && right.length === 1) {
      letters[right[0].key] = (letters[right[0].key] ?? 0) + 1;
      // "A é a correta", "Gabarito: C", "C está correta" com letra diferente da marcada
      const said = [...exp.matchAll(/(?:^|[\s(])([A-E])\)?\s+(?:é a (?:correta|resposta|alternativa correta)|está correta|é o gabarito|atende)|gabarito[:\s]+([A-E])\b|(?:correta|resposta)\s+(?:é|:)\s+(?:a\s+)?(?:letra\s+)?([A-E])\b/g)].map((m) => m[1] ?? m[2] ?? m[3]);
      if (said.length && !said.includes(right[0].key)) flags.push(`#${i} explicação aponta ${said.join("/")}, gabarito ${right[0].key}`);
    }
    if (q.format === "TRUE_FALSE" && right.length === 1) {
      const start = norm(exp).slice(0, 7);
      const key = right[0].key;
      if ((key === "C" && start.startsWith("errado")) || (key === "E" && start.startsWith("certo"))) flags.push(`#${i} explicação começa contrária ao gabarito`);
    }
    for (const o of q.options ?? []) if (/\*\*|[^.]\.\s+[a-zà-ú]/.test(String(o.text))) flags.push(`#${i} alternativa com ** ou trecho colado`);
  });
  report.push(`${data.id}: ${qs.length} questões (${mc.length} A–E) | letras ${JSON.stringify(letters)} | ${JSON.stringify(levels)}${flags.length ? "\n    ⚠ " + flags.join("\n    ⚠ ") : ""}`);
}
console.log(report.join("\n"));
