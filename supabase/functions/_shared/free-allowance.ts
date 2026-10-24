// Regras do plano Grátis para matérias, conferidas aqui no servidor (o app só mostra).
//
// O Grátis nunca usa IA: só matéria pronta da biblioteca (a trava fica no worker, antes de chamar o
// provedor). Por cima disso, cada pedido de matéria diz para que é:
//   PLAN_DAY  a matéria do plano de hoje: até PLAN_DAY_PER_DAY por dia, sem gastar as extras;
//   ADVANCE   adiantar o próximo dia do plano: um crédito de anúncio confirmado pelo Google
//             (ad-reward-ssv, até 2 por dia e 10 por mês) ou, sem anúncio, 1 por semana;
//   EXTRA     qualquer outra matéria do edital: até EXTRAS_PER_MONTH por mês.
// App antigo não manda o motivo: vale como EXTRA (o mesmo limite de 10 de antes).
// O plano de estudo mora no aparelho, então o servidor não confere se o tópico é mesmo "do dia";
// por isso o teto diário: quem burlar o app ganha no máximo isso, e só matéria pronta (custo zero).

export type ContentPurpose = "PLAN_DAY" | "ADVANCE" | "EXTRA";

export const FREE_RULES = {
  planDayPerDay: 3,
  extrasPerMonth: 10,
  adAdvancesPerDay: 2,
  adAdvancesPerMonth: 10,
  freeAdvancesPerWeek: 1,
  /** Crédito de anúncio vale por este tempo (assistiu, tem esse prazo para adiantar). */
  creditTtlMs: 6 * 3_600_000,
} as const;

export function parsePurpose(value: unknown): ContentPurpose {
  return value === "PLAN_DAY" || value === "ADVANCE" ? value : "EXTRA";
}

/** O que a conta já usou no período de cada regra. */
export interface FreeUsage {
  planDayToday: number;
  extrasMonth: number;
  /** Adiantamentos sem anúncio nos últimos 7 dias. */
  weeklyAdvances: number;
  /** Créditos de anúncio confirmados: hoje, no mês e ainda não usados (dentro do prazo). */
  adCreditsToday: number;
  adCreditsMonth: number;
  adCreditsAvailable: number;
}

export type FreeDecision =
  | { ok: true; useCredit: boolean; weekly: boolean }
  | { ok: false; code: "FREE_PLAN_DAY_LIMIT" | "FREE_EXTRA_LIMIT" | "FREE_ADVANCE_NEEDS_AD" };

export function decideFreeContent(purpose: ContentPurpose, usage: FreeUsage): FreeDecision {
  if (purpose === "PLAN_DAY") {
    return usage.planDayToday < FREE_RULES.planDayPerDay ? { ok: true, useCredit: false, weekly: false } : { ok: false, code: "FREE_PLAN_DAY_LIMIT" };
  }
  if (purpose === "ADVANCE") {
    if (usage.adCreditsAvailable > 0) return { ok: true, useCredit: true, weekly: false };
    if (usage.weeklyAdvances < FREE_RULES.freeAdvancesPerWeek) return { ok: true, useCredit: false, weekly: true };
    return { ok: false, code: "FREE_ADVANCE_NEEDS_AD" };
  }
  return usage.extrasMonth < FREE_RULES.extrasPerMonth ? { ok: true, useCredit: false, weekly: false } : { ok: false, code: "FREE_EXTRA_LIMIT" };
}

/** O anúncio pode dar mais um crédito hoje? (conferido quando o Google confirma o anúncio). */
export function adCreditAllowed(usage: Pick<FreeUsage, "adCreditsToday" | "adCreditsMonth">): boolean {
  return usage.adCreditsToday < FREE_RULES.adAdvancesPerDay && usage.adCreditsMonth < FREE_RULES.adAdvancesPerMonth;
}

/** Início do dia e do mês no horário de Brasília (UTC−3), em ISO. */
export function brasiliaBounds(now: Date): { dayStart: string; monthStart: string; weekAgo: string } {
  const local = new Date(now.getTime() - 3 * 3_600_000);
  const y = local.getUTCFullYear(), m = local.getUTCMonth(), d = local.getUTCDate();
  return {
    dayStart: new Date(Date.UTC(y, m, d, 3)).toISOString(),
    monthStart: new Date(Date.UTC(y, m, 1, 3)).toISOString(),
    weekAgo: new Date(now.getTime() - 7 * 86_400_000).toISOString(),
  };
}

interface JobRow { created_at: string; purpose?: unknown; ad_credit?: unknown; weekly?: unknown }
interface CreditRow { id: string; created_at: string; used_at: string | null }

/** Conta o uso a partir dos jobs e dos créditos (só a service role lê). */
export function usageFrom(jobs: JobRow[], credits: CreditRow[], now: Date): FreeUsage {
  const { dayStart, monthStart, weekAgo } = brasiliaBounds(now);
  const purposeOf = (row: JobRow) => parsePurpose(row.purpose);
  const after = (iso: string, bound: string) => Date.parse(iso) >= Date.parse(bound);
  return {
    planDayToday: jobs.filter((j) => purposeOf(j) === "PLAN_DAY" && after(j.created_at, dayStart)).length,
    extrasMonth: jobs.filter((j) => purposeOf(j) === "EXTRA" && after(j.created_at, monthStart)).length,
    weeklyAdvances: jobs.filter((j) => purposeOf(j) === "ADVANCE" && j.weekly === true && after(j.created_at, weekAgo)).length,
    adCreditsToday: credits.filter((c) => after(c.created_at, dayStart)).length,
    adCreditsMonth: credits.filter((c) => after(c.created_at, monthStart)).length,
    adCreditsAvailable: credits.filter((c) => c.used_at === null && now.getTime() - Date.parse(c.created_at) < FREE_RULES.creditTtlMs).length,
  };
}

export interface FreeAllowanceStore {
  usage(userId: string): Promise<FreeUsage>;
  /** Marca um crédito livre como usado; devolve o id dele, ou null se não havia (corrida). */
  consumeCredit(userId: string): Promise<string | null>;
  /** Devolve o crédito quando o pedido não chegou a virar job. */
  releaseCredit(creditId: string): Promise<void>;
  /** Liga o crédito ao job que ele pagou (auditoria). */
  linkCredit(creditId: string, jobId: string): Promise<void>;
}

/** Leitura e escrita pela API REST com a service role (as tabelas não têm acesso do app). */
export class SupabaseFreeAllowance implements FreeAllowanceStore {
  constructor(private readonly url: string, private readonly serviceKey: string, private readonly now: () => Date = () => new Date()) {}

  private async rest(path: string, init: RequestInit = {}): Promise<Response> {
    return await fetch(`${this.url.replace(/\/$/, "")}/rest/v1/${path}`, {
      ...init,
      headers: { apikey: this.serviceKey, authorization: `Bearer ${this.serviceKey}`, "content-type": "application/json", accept: "application/json", ...(init.headers ?? {}) },
    });
  }

  async usage(userId: string): Promise<FreeUsage> {
    const now = this.now();
    const { monthStart, weekAgo } = brasiliaBounds(now);
    const since = Date.parse(weekAgo) < Date.parse(monthStart) ? weekAgo : monthStart;
    const user = encodeURIComponent(userId);
    const jobs = await this.rest(
      `ai_jobs?select=created_at,purpose:request_payload->purpose,weekly:request_payload->weekly&user_id=eq.${user}` +
        `&feature=eq.CONTENT_GENERATION&status=not.in.(FAILED,EXPIRED,CANCELLED)&created_at=gte.${encodeURIComponent(since)}&limit=500`,
    );
    const credits = await this.rest(`ad_reward_credits?select=id,created_at,used_at&user_id=eq.${user}&created_at=gte.${encodeURIComponent(since)}&limit=200`);
    if (!jobs.ok || !credits.ok) throw new Error("FREE_USAGE_UNAVAILABLE");
    return usageFrom(await jobs.json() as JobRow[], await credits.json() as CreditRow[], now);
  }

  async consumeCredit(userId: string): Promise<string | null> {
    const freshSince = new Date(this.now().getTime() - FREE_RULES.creditTtlMs).toISOString();
    const list = await this.rest(
      `ad_reward_credits?select=id&user_id=eq.${encodeURIComponent(userId)}&used_at=is.null&created_at=gte.${encodeURIComponent(freshSince)}&order=created_at.asc&limit=3`,
    );
    if (!list.ok) throw new Error("FREE_CREDIT_UNAVAILABLE");
    for (const { id } of await list.json() as { id: string }[]) {
      // Só marca se ainda estava livre: dois pedidos ao mesmo tempo não usam o mesmo crédito.
      const patch = await this.rest(`ad_reward_credits?id=eq.${encodeURIComponent(id)}&used_at=is.null`, {
        method: "PATCH",
        headers: { prefer: "return=representation" },
        body: JSON.stringify({ used_at: this.now().toISOString() }),
      });
      if (patch.ok && (await patch.json() as unknown[]).length === 1) return id;
    }
    return null;
  }

  async releaseCredit(creditId: string): Promise<void> {
    const r = await this.rest(`ad_reward_credits?id=eq.${encodeURIComponent(creditId)}`, { method: "PATCH", body: JSON.stringify({ used_at: null, used_job_id: null }) });
    await r.body?.cancel().catch(() => {});
  }

  async linkCredit(creditId: string, jobId: string): Promise<void> {
    const r = await this.rest(`ad_reward_credits?id=eq.${encodeURIComponent(creditId)}`, { method: "PATCH", body: JSON.stringify({ used_job_id: jobId }) });
    await r.body?.cancel().catch(() => {});
  }
}
