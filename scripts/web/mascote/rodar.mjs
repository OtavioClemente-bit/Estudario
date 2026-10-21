// Refaz as partes do Folha animado a partir da arte (design/mascote/folha-turnaround.webp).
// Precisa do Edge instalado e do puppeteer-core (npm i --no-save puppeteer-core):
//   node scripts/web/mascote/rodar.mjs
// 1) fundo.html tira o fundo cinza (crescimento de região a partir das bordas) -> recorte.png
// 2) partes.html corta a vista de frente em partes com pivôs e preenche o livro onde elas estavam
//    -> web/src/jsMain/resources/mascote-*.png (o layout impresso vai para o topo de mascote.js).
import puppeteer from "puppeteer-core"; import fs from "node:fs";
import { spawn, spawnSync } from "node:child_process"; import { tmpdir } from "node:os";
import { dirname, join } from "node:path"; import { fileURLToPath, pathToFileURL } from "node:url";
const here = dirname(fileURLToPath(import.meta.url)), res = join(here, "..", "..", "..", "web", "src", "jsMain", "resources");
const edge = spawn("C:/Program Files (x86)/Microsoft/Edge/Application/msedge.exe", ["--headless=new", "--remote-debugging-port=9499", "--user-data-dir=" + join(tmpdir(), "mascote-" + Date.now()), "--no-first-run", "--allow-file-access-from-files", "about:blank"], { stdio: "ignore" });
await new Promise((r) => setTimeout(r, 2000));
const b = await puppeteer.connect({ browserURL: "http://127.0.0.1:9499", defaultViewport: null, protocolTimeout: 0 });
const p = (await b.pages())[0];
async function run(file) {
  await p.goto(pathToFileURL(join(here, file)).href);
  while (!(await p.evaluate(() => window.DONE))) await new Promise((r) => setTimeout(r, 300));
  return p.evaluate(() => window.FILES || { "": window.PNG });
}
const save = (url, path) => fs.writeFileSync(path, Buffer.from(url.split(",")[1], "base64"));
save((await run("fundo.html"))[""], join(here, "recorte.png"));
const files = await run("partes.html");
for (const [name, url] of Object.entries(files)) {
  if (name === "mask") continue;
  if (name === "layout") { console.log("layout:", Buffer.from(url.split(",")[1], "base64").toString()); continue; }
  save(url, join(res, "mascote-" + name + ".png"));
}
await b.close().catch(() => {}); spawnSync("taskkill", ["/PID", String(edge.pid), "/T", "/F"]);
console.log("partes geradas em", res);
