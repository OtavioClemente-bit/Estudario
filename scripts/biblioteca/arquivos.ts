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
  return `topics/${id}/v${version}-${contentHash(material)}.json`;
}

/** Caminho do arquivo do recorte (banca e cargo viram partes seguras do nome). */
export function notePath(topic: string, boardNorm: string, roleNorm: string, note: unknown): string {
  const safe = (s: string) => s.normalize("NFD").replace(/[̀-ͯ]/g, "").toLowerCase().replace(/[^a-z0-9]+/g, "-").replace(/^-|-$/g, "") || "geral";
  return `notes/${topic}/${safe(boardNorm)}/${safe(roleNorm)}-${contentHash(note)}.json`;
}

/** Envia (ou substitui) um arquivo da biblioteca. */
export async function uploadLibraryFile(url: string, key: string, path: string, value: unknown): Promise<void> {
  const response = await fetch(`${url}/storage/v1/object/library/${path.split("/").map(encodeURIComponent).join("/")}`, {
    method: "POST",
    headers: { apikey: key, authorization: `Bearer ${key}`, "content-type": "application/json", "x-upsert": "true", "cache-control": "max-age=31536000" },
    body: JSON.stringify(value),
  });
  if (!response.ok) throw new Error(`arquivo ${path}: ${response.status} ${await response.text()}`);
  await response.body?.cancel().catch(() => {});
}
