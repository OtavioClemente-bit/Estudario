// Estudário sem internet: guarda o próprio site (código, estilos, fontes) para abrir no metrô ou no
// ônibus. Sempre tenta a rede primeiro (o site novo chega na hora); sem rede, usa o que guardou.
// Os dados da conta (Supabase) nunca passam por aqui: a cópia deles fica no IndexedDB (offline.js).
const CACHE = "estudario-site-v1";
const SHELL = ["./", "index.html", "estudario.js", "styles.css", "icon.png", "folha.js", "folha-live.js", "turma.js", "splash.js",
  "process.js", "katex.min.js", "highlight.js", "ambient.js", "rocket.js", "workshop.js", "offline.js"];

self.addEventListener("install", (event) => {
  event.waitUntil(caches.open(CACHE).then((cache) => Promise.all(SHELL.map((path) => cache.add(path).catch(() => null)))));
  self.skipWaiting();
});

self.addEventListener("activate", (event) => {
  event.waitUntil(
    caches.keys().then((keys) => Promise.all(keys.filter((k) => k.startsWith("estudario-site-") && k !== CACHE).map((k) => caches.delete(k))))
      .then(() => self.clients.claim()),
  );
});

self.addEventListener("fetch", (event) => {
  const request = event.request;
  if (request.method !== "GET") return;
  const url = new URL(request.url);
  const sameOrigin = url.origin === self.location.origin;
  const fonts = url.host === "fonts.googleapis.com" || url.host === "fonts.gstatic.com";
  if (!sameOrigin && !fonts) return;
  event.respondWith(
    fetch(request)
      .then((response) => {
        if (response && (response.ok || response.type === "opaque")) {
          const copy = response.clone();
          caches.open(CACHE).then((cache) => cache.put(request, copy)).catch(() => null);
        }
        return response;
      })
      .catch(() =>
        caches.match(request, { ignoreSearch: request.mode === "navigate" })
          .then((hit) => hit || (request.mode === "navigate" ? caches.match("index.html") : undefined))
          .then((hit) => hit || new Response("", { status: 504, statusText: "Sem internet" })),
      ),
  );
});
