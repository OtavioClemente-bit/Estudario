// Servidor local só para pré-visualizar o app web compilado (não vai para produção).
const root = new URL("../web/build/dist/js/productionExecutable/", import.meta.url);
const types: Record<string, string> = { html: "text/html; charset=utf-8", js: "text/javascript", css: "text/css", png: "image/png", svg: "image/svg+xml", json: "application/json", wasm: "application/wasm", ttf: "font/ttf", map: "application/json" };
Deno.serve({ port: 8789 }, async (req) => {
  let path = decodeURIComponent(new URL(req.url).pathname).replace(/^\/+/, "");
  if (path === "" || path.endsWith("/")) path += "index.html";
  if (path.includes("..")) return new Response("no", { status: 400 });
  try {
    const body = await Deno.readFile(new URL(path, root));
    return new Response(body, { headers: { "content-type": types[path.split(".").pop() ?? ""] ?? "application/octet-stream", "cache-control": "no-store" } });
  } catch {
    // Rotas do app (ex.: /plano) caem no index.html.
    const body = await Deno.readFile(new URL("index.html", root));
    return new Response(body, { headers: { "content-type": types.html, "cache-control": "no-store" } });
  }
});
