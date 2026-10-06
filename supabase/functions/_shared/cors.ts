/** Origens do app web que podem chamar as funções pelo navegador. */
const WEB_ORIGINS = new Set(["https://app.estudario.com.br", "https://estudario-app.pages.dev", "http://localhost:8788"]);

export function corsHeaders(request: Request): Record<string, string> {
  const origin = request.headers.get("origin") ?? "";
  const allowed = WEB_ORIGINS.has(origin) || /^https:\/\/[a-z0-9-]+\.estudario-app\.pages\.dev$/.test(origin);
  if (!allowed) return {};
  return {
    "access-control-allow-origin": origin,
    "access-control-allow-methods": "GET, POST, OPTIONS",
    "access-control-allow-headers": "authorization, apikey, content-type, idempotency-key, x-estudario-client, x-estudario-device, x-turnstile-token, x-play-integrity-token",
    "access-control-max-age": "600",
    vary: "Origin",
  };
}

/** Responde o preflight (OPTIONS) e acrescenta os cabeçalhos de CORS na resposta do app web. */
export function withCors(handler: (request: Request) => Promise<Response>): (request: Request) => Promise<Response> {
  return async (request) => {
    const cors = corsHeaders(request);
    const allowed = Object.keys(cors).length > 0;
    if (request.method === "OPTIONS") return new Response(null, { status: allowed ? 204 : 403, headers: cors });
    const response = await handler(request);
    if (!allowed) return response;
    const headers = new Headers(response.headers);
    for (const [key, value] of Object.entries(cors)) headers.set(key, value);
    return new Response(response.body, { status: response.status, statusText: response.statusText, headers });
  };
}
