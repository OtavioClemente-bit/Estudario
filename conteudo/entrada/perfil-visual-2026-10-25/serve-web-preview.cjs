const http = require("node:http");
const fs = require("node:fs");
const path = require("node:path");
const root = path.resolve(__dirname, "../../../web/build/dist/js/productionExecutable");
const types = { ".html": "text/html", ".js": "text/javascript", ".css": "text/css", ".json": "application/json", ".png": "image/png", ".webp": "image/webp", ".svg": "image/svg+xml", ".wav": "audio/wav", ".woff2": "font/woff2" };
http.createServer((req, res) => {
  let file;
  try {
    const name = decodeURIComponent(new URL(req.url, "http://127.0.0.1:4173").pathname);
    file = path.resolve(root, "." + (name === "/" ? "/index.html" : name));
  } catch { res.writeHead(400).end(); return; }
  if (!file.startsWith(root + path.sep)) { res.writeHead(403).end(); return; }
  fs.stat(file, (error, stat) => {
    if (error || !stat.isFile()) { res.writeHead(404).end(); return; }
    res.writeHead(200, { "Content-Type": types[path.extname(file)] || "application/octet-stream", "Cache-Control": "no-store" });
    if (req.method === "HEAD") res.end();
    else fs.createReadStream(file).pipe(res);
  });
}).listen(4173, "127.0.0.1", () => console.log("Web preview: http://127.0.0.1:4173/?demo#/perfil"));
