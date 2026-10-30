// Vigia do ChatGPT: se nenhum arquivo novo chegar em conteudo/entrada/chatgpt por VIGIA_MINUTOS (padrão 60),
// fecha o Chrome e abre de novo na página do projeto (o Tampermonkey retoma sozinho). Confere a cada 5 minutos.
// Depois de reabrir, espera outro intervalo inteiro antes de tentar de novo.
//   deno run -A scripts/chatgpt/vigia.ts
const ROOT = new URL("../../", import.meta.url).pathname.replace(/^\/([A-Za-z]:)/, "$1");
const DIR = `${ROOT}conteudo/entrada/chatgpt`;
const LOG = `${DIR}/automatico-log.txt`;
const CHROME = "C:/Program Files/Google/Chrome/Application/chrome.exe";
const URL_PROJ = "https://chatgpt.com/g/g-p-6ae29815eb9c81918f823336b21516cf/project";
const LIMITE = Number(Deno.env.get("VIGIA_MINUTOS") ?? 60) * 60_000;
const sleep = (ms: number) => new Promise((r) => setTimeout(r, ms));
const log = (m: string) => {
  const l = `[${new Date().toLocaleString("pt-BR")}] [vigia] ${m}`;
  console.log(l);
  Deno.writeTextFileSync(LOG, l + "\n", { append: true });
};
const ultimoArquivo = () => {
  let t = 0;
  for (const e of Deno.readDirSync(DIR)) {
    if (/\.chatgpt-\d+\.json$/.test(e.name)) t = Math.max(t, Deno.statSync(`${DIR}/${e.name}`).mtime?.getTime() ?? 0);
  }
  return t;
};

let reaberto = 0;
log(`iniciado; reabre o Chrome após ${LIMITE / 60_000} min sem arquivo novo`);
while (true) {
  const parado = Date.now() - Math.max(ultimoArquivo(), reaberto);
  if (parado > LIMITE) {
    log(`${Math.round(parado / 60_000)} min sem arquivo novo; fechando e reabrindo o Chrome`);
    await new Deno.Command("taskkill", { args: ["/IM", "chrome.exe"], stdout: "null", stderr: "null" }).output();
    await sleep(15_000);
    await new Deno.Command("taskkill", { args: ["/F", "/IM", "chrome.exe"], stdout: "null", stderr: "null" }).output();
    await sleep(5_000);
    new Deno.Command(CHROME, { args: ["--restore-last-session", URL_PROJ], stdout: "null", stderr: "null" }).spawn();
    reaberto = Date.now();
  }
  await sleep(5 * 60_000);
}
