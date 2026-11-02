// Auditoria por amostra dos arquivos do ChatGPT ainda não auditados: sorteia 5 questões por arquivo
// (só escolhe quais conferir; não escreve nem altera questão) e divide em lotes de 20 arquivos.
//   deno run -A scripts/chatgpt/amostra.ts --montar      → grava work/amostra-chatgpt.json
//   deno run -A scripts/chatgpt/amostra.ts --lote=N      → imprime as questões sorteadas do lote N
const ROOT = new URL("../../", import.meta.url).pathname.replace(/^\/([A-Za-z]:)/, "$1");
const PASTA = `${ROOT}conteudo/entrada/chatgpt`;
const SAIDA = `${ROOT}work/amostra-chatgpt.json`;
const POR_LOTE = 20, POR_ARQUIVO = 5;
type Q = { enunciado?: string; alternativas?: string[]; gabarito?: string; explicacao?: string };

if (Deno.args.includes("--montar")) {
  const itens: { arquivo: string; indices: number[] }[] = [];
  for (const e of [...Deno.readDirSync(PASTA)].sort((a, b) => a.name.localeCompare(b.name))) {
    if (!e.name.endsWith(".json") || e.name === "fila.json") continue;
    let j; try { j = JSON.parse(Deno.readTextFileSync(`${PASTA}/${e.name}`)); } catch { continue; }
    const qs: Q[] = j.questoes ?? [];
    if (j.auditado || !qs.length) continue;
    // Arquivos de 4 alternativas não entram nos recortes (juntar.ts exige 5); ficam fora da amostra.
    if (qs.some((q) => Array.isArray(q.alternativas) && q.alternativas.length > 2 && q.alternativas.length !== 5)) continue;
    const pool = qs.map((_, i) => i);
    for (let i = pool.length - 1; i > 0; i--) { const k = Math.floor(Math.random() * (i + 1)); [pool[i], pool[k]] = [pool[k], pool[i]]; }
    itens.push({ arquivo: e.name, indices: pool.slice(0, POR_ARQUIVO).sort((a, b) => a - b) });
  }
  const lotes = [];
  for (let i = 0; i < itens.length; i += POR_LOTE) lotes.push(itens.slice(i, i + POR_LOTE));
  Deno.mkdirSync(`${ROOT}work`, { recursive: true });
  Deno.writeTextFileSync(SAIDA, JSON.stringify(lotes));
  console.log(`${itens.length} arquivos em ${lotes.length} lotes`);
} else {
  const n = Number(Deno.args.find((a) => a.startsWith("--lote="))?.slice(7));
  const lote = JSON.parse(Deno.readTextFileSync(SAIDA))[n - 1] ?? [];
  for (const { arquivo, indices } of lote) {
    const qs: Q[] = JSON.parse(Deno.readTextFileSync(`${PASTA}/${arquivo}`)).questoes ?? [];
    console.log(`\n### ${arquivo} (${qs.length} questões)`);
    for (const i of indices) {
      const q = qs[i];
      if (!q) continue;
      console.log(`[${i}] ${q.enunciado}`);
      (q.alternativas ?? []).forEach((a, k) => console.log(`   ${"ABCDE"[k]}) ${a}`));
      console.log(`   gabarito: ${q.gabarito} | explicação: ${q.explicacao}`);
    }
  }
}
