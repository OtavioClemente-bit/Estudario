// node render.mjs <arquivo.html?query> <largura> <altura> <saida.png> [escala]
import { createRequire } from "node:module";
const require = createRequire("C:/Users/otavi/Documents/Codex/2026-09-15/vc-x20/package.json");
const puppeteer = require("puppeteer-core");
const [, , page, w, h, out, scale] = process.argv;
const browser = await puppeteer.launch({
  executablePath: "C:/Program Files (x86)/Microsoft/Edge/Application/msedge.exe",
  headless: "new",
  args: ["--allow-file-access-from-files", "--disable-web-security", "--force-color-profile=srgb"],
});
const tab = await browser.newPage();
await tab.setViewport({ width: Number(w), height: Number(h), deviceScaleFactor: Number(scale || 1) });
const url = page.startsWith("http") ? page : "file:///" + page.replace(/\\/g, "/");
await tab.goto(url, { waitUntil: "networkidle0", timeout: 120000 });
await tab.evaluate(async () => { await document.fonts.ready; await Promise.all([...document.images].map((i) => i.decode().catch(() => 0))); });
await new Promise((r) => setTimeout(r, Number(process.env.WAIT || 600)));
const jpg = out.endsWith(".jpg");
await tab.screenshot({ path: out, clip: { x: 0, y: 0, width: Number(w), height: Number(h) }, omitBackground: !jpg, ...(jpg ? { type: "jpeg", quality: 95 } : {}) });
await browser.close();
console.log("ok", out);
