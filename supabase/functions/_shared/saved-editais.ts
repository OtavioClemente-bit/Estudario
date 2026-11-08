// Limite de editais salvos (SAVED_EDITAIS) conferido no servidor. Os editais vivem na cópia da nuvem
// da conta (user-sync); o servidor conta a partir dela, guardando o resumo em admin_account_summary
// (só relê quando a conta sincronizou de novo). Quem passou do limite do plano não gera conteúdo:
// editar o site ou o app para salvar mais editais não libera nada.
import { summaryOf } from "../admin-editais-usage/index.ts";

export interface SavedEditais { count: number; limit: number }

/** Passou do limite? Conta exatamente como o app e o site (cada concurso com nome). */
export function overSavedEditais(value: SavedEditais | null): boolean {
  return value !== null && value.limit > 0 && value.count > value.limit;
}

export function savedEditaisChecker(url: string, key: string) {
  const headers = { apikey: key, authorization: `Bearer ${key}`, accept: "application/json", "content-type": "application/json" };
  const rest = async <T>(path: string, init: RequestInit = {}): Promise<T> => {
    const response = await fetch(`${url}/rest/v1/${path}`, { ...init, headers: { ...headers, ...(init.headers ?? {}) } });
    if (!response.ok) { await response.body?.cancel().catch(() => {}); throw new Error(`rest ${response.status}`); }
    return (response.status === 204 ? null : await response.json()) as T;
  };
  /** null quando não dá para saber (sem cópia na nuvem ou falha): não bloqueia por isso. */
  return async (userId: string, tier: string): Promise<SavedEditais | null> => {
    const id = encodeURIComponent(userId);
    const [limitRow] = await rest<{ quota_limit: number }[]>(`ai_plan_limits?select=quota_limit&plan_tier=eq.${encodeURIComponent(tier)}&feature=eq.SAVED_EDITAIS&limit=1`);
    if (!limitRow) return null;
    const [head] = await rest<{ object_path: string | null; updated_at: string }[]>(`sync_heads?select=object_path,updated_at&user_id=eq.${id}&limit=1`);
    if (!head?.object_path) return null;
    const [saved] = await rest<{ sync_updated_at: string; summary: { editais?: unknown[] } }[]>(`admin_account_summary?select=sync_updated_at,summary&user_id=eq.${id}&limit=1`);
    if (saved && Date.parse(saved.sync_updated_at) >= Date.parse(head.updated_at)) {
      return { count: saved.summary?.editais?.length ?? 0, limit: limitRow.quota_limit };
    }
    const path = head.object_path.split("/").map(encodeURIComponent).join("/");
    const response = await fetch(`${url}/storage/v1/object/authenticated/user-sync/${path}`, { headers: { apikey: key, authorization: `Bearer ${key}` } });
    if (!response.ok) { await response.body?.cancel().catch(() => {}); return null; }
    const summary = summaryOf(await response.json());
    await rest("admin_account_summary?on_conflict=user_id", {
      method: "POST", headers: { Prefer: "resolution=merge-duplicates,return=minimal" },
      body: JSON.stringify([{ user_id: userId, sync_updated_at: head.updated_at, summary, computed_at: new Date().toISOString() }]),
    }).catch(() => null);
    return { count: summary.editais.length, limit: limitRow.quota_limit };
  };
}
