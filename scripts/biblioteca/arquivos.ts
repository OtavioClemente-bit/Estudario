// Os arquivos da biblioteca no bucket privado "library" do Supabase: cada matéria e cada recorte de
// banca é um JSON (a biblioteca saiu do banco, que no plano grátis tem 500 MB; ver a migração
// 20261028180000_library_in_storage.sql). Usado pelo importar.ts e pelo mover-para-arquivos.ts.

/** Hash curto e estável do conteúdo: entra no nome do arquivo, então cada versão é um arquivo novo. */
export function contentHash(value: unknown): string {
  const text = JSON.stringify(value);
  let h1 = 0x811c9dc5, h2 = 0x01000193;
  for (let i = 0; i < text.length; i++) {
    const c = text.charCodeAt(i);
    h1 = Math.imul(h1 ^ c, 0x01000193);
    h2 = Math.imul(h2 ^ c, 0x5bd1e995);
  }
  return (h1 >>> 0).toString(36) + (h2 >>> 0).toString(36) + text.length.toString(36);
}

/** Caminho do arquivo da matéria. */
export function topicPath(id: string, version: number, material: unknown): string {
  return `topics/${id}/v${version}-${contentHash(material)}.json.gz`;
}

/** Caminho do arquivo do recorte (banca e cargo viram partes seguras do nome). */
export function notePath(topic: string, boardNorm: string, roleNorm: string, note: unknown): string {
  const safe = (s: string) => s.normalize("NFD").replace(/[̀-ͯ]/g, "").toLowerCase().replace(/[^a-z0-9]+/g, "-").replace(/^-|-$/g, "") || "geral";
  return `notes/${topic}/${safe(boardNorm)}/${safe(roleNorm)}-${contentHash(note)}.json.gz`;
}

/** Envia (ou substitui) um arquivo da biblioteca. */
export async function uploadLibraryFile(url: string, key: string, path: string, value: unknown): Promise<void> {
  // Caminho ".gz": o arquivo vai compactado com gzip (14/11: o bucket passou de 1,1 GB do plano grátis). Compactado,
  // o firewall não enxerga mais os exemplos de ataque, mas o escape continua por segurança (JSON idêntico ao ler).
  const text = JSON.stringify(value).replace(/[<>'();=|&`/]/g, (c) => "\\u" + c.charCodeAt(0).toString(16).padStart(4, "0"));
  const gz = path.endsWith(".gz");
  const body = gz ? new Uint8Array(await new Response(new Blob([text]).stream().pipeThrough(new CompressionStream("gzip"))).arrayBuffer()) : text;
  const response = await fetch(`${url}/storage/v1/object/library/${path.split("/").map(encodeURIComponent).join("/")}`, {
    method: "POST",
    headers: { apikey: key, authorization: `Bearer ${key}`, "content-type": "application/json", "x-upsert": "true", "cache-control": "max-age=31536000" },
    body,
  });
  if (!response.ok) throw new Error(`arquivo ${path}: ${response.status} ${await response.text()}`);
  await response.body?.cancel().catch(() => {});
}

/** Lê o JSON de um arquivo baixado da biblioteca, compactado com gzip ou não (decide pelos dois primeiros bytes). */
export async function readLibraryJson(response: Response): Promise<unknown> {
  const bytes = new Uint8Array(await response.arrayBuffer());
  const raw = bytes[0] === 0x1f && bytes[1] === 0x8b
    ? new Uint8Array(await new Response(new Blob([bytes]).stream().pipeThrough(new DecompressionStream("gzip"))).arrayBuffer())
    : bytes;
  return JSON.parse(new TextDecoder().decode(raw));
}
