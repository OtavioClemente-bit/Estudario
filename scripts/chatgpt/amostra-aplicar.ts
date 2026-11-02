// Aplica os vereditos da auditoria por amostra (work/amostra-res/N.json): marca "auditado": "amostra" nos
// arquivos ok (só o campo de controle; nenhuma questão é tocada), move os reprovados para chatgpt-reprovados/
// e junta os "ler" em work/amostra-ler.json para leitura completa.
//   deno run -A scripts/chatgpt/amostra-aplicar.ts
const ROOT = new URL("../../", import.meta.url).pathname.replace(/^\/([A-Za-z]:)/, "$1");
const PASTA = `${ROOT}conteudo/entrada/chatgpt`;
const RES = `${ROOT}work/amostra-res`;
const LER = `${ROOT}work/amostra-ler.json`;
let ler: Record<string, string> = {};
try { ler = JSON.parse(Deno.readTextFileSync(LER)); } catch { /* primeira vez */ }
let ok = 0, rep = 0;
for (const e of Deno.readDirSync(RES)) {
  if (!e.name.endsWith(".json")) continue;
  const r = JSON.parse(Deno.readTextFileSync(`${RES}/${e.name}`));
  for (const a of r.ok ?? []) {
    const p = `${PASTA}/${a}`;
    let j; try { j = JSON.parse(Deno.readTextFileSync(p)); } catch { continue; }
    if (j.auditado) continue;
    Deno.writeTextFileSync(p, JSON.stringify({ auditado: "amostra", ...j }, null, 2) + "\n");
    ok++;
  }
  for (const a of Object.keys(r.reprovar ?? {})) {
    try { Deno.renameSync(`${PASTA}/${a}`, `${ROOT}conteudo/entrada/chatgpt-reprovados/${a}`); rep++; } catch { /* já movido */ }
  }
  Object.assign(ler, r.ler ?? {});
}
Deno.writeTextFileSync(LER, JSON.stringify(ler, null, 1));
console.log(`ok marcados: ${ok}; reprovados movidos: ${rep}; para ler inteiro: ${Object.keys(ler).length}`);
