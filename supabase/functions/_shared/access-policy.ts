import type { AiAccess, AiFeature, AiQuota } from "./contracts.ts";

const TIME_ZONE = "America/Sao_Paulo";

const FEATURE_FLAG_KEYS: Record<AiFeature, string> = {
  SYLLABUS_GENERATION: "SYLLABUS_AI_ENABLED",
  PLAN_GENERATION: "PLAN_AI_ENABLED",
  CONTENT_GENERATION: "CONTENT_AI_ENABLED",
  SIMULATION_GENERATION: "SIMULATION_AI_ENABLED",
};

export type PlanTier = "FREE" | "ESSENCIAL" | "PRO";
export const PLAN_TIERS: readonly PlanTier[] = ["FREE", "ESSENCIAL", "PRO"];
export type PlanPeriodKind = "LIFETIME" | "MONTHLY" | "DAILY";

export interface ProfileRecord {
  userId: string;
  betaAccess: boolean;
  planTier?: PlanTier;
  planRenewsAt?: string | null;
}

export interface PlanLimitRecord {
  planTier: PlanTier;
  feature: string;
  periodKind: PlanPeriodKind;
  quotaLimit: number;
  maxPerRequest: number | null;
  maxPerTopicMonth: number | null;
}

export interface FeatureFlagRecord {
  flagKey: string;
  enabled: boolean;
}

export interface QuotaUsageRecord {
  userId: string;
  feature: string;
  successfulCount: number;
  reservedCount: number;
  periodStart: string;
}

export interface AccessDataSource {
  findProfile(userId: string): Promise<ProfileRecord | null>;
  listFeatureFlags(keys: readonly string[]): Promise<FeatureFlagRecord[]>;
  findQuotaUsage(
    userId: string,
    feature: AiFeature,
    periodStart: string,
  ): Promise<QuotaUsageRecord | null>;
  listPlanLimits(planTier?: PlanTier): Promise<PlanLimitRecord[]>;
}

export interface AccessPolicyOptions {
  now?: () => Date;
}

export class AccessDataError extends Error {
  constructor() {
    super("AI_ACCESS_DATA_UNAVAILABLE");
    this.name = "AccessDataError";
  }
}

export interface SupabaseAccessEnvironment {
  supabaseUrl: string;
  authenticatedUserId: string;
  accessToken: string;
  publishableKey: string;
  fetcher?: typeof fetch;
}

export class SupabaseAccessDataSource implements AccessDataSource {
  constructor(private readonly environment: SupabaseAccessEnvironment) {
    if (
      !environment.authenticatedUserId.trim() ||
      !environment.accessToken.trim() ||
      !environment.publishableKey.trim() || !environment.supabaseUrl.trim()
    ) {
      throw new AccessDataError();
    }
  }

  async findProfile(userId: string): Promise<ProfileRecord | null> {
    this.assertOwner(userId);
    const rows = await this.select("profiles", {
      select: "user_id,beta_access,plan_tier,plan_renews_at",
      user_id: `eq.${this.environment.authenticatedUserId}`,
      limit: "1",
    });
    const row = rows[0];
    if (!row) return null;
    return {
      userId: stringField(row, "user_id"),
      betaAccess: booleanField(row, "beta_access"),
      planTier: planTierField(row, "plan_tier"),
      planRenewsAt: typeof row.plan_renews_at === "string" ? row.plan_renews_at : null,
    };
  }

  async listFeatureFlags(
    keys: readonly string[],
  ): Promise<FeatureFlagRecord[]> {
    const rows = await this.select("ai_feature_flags", {
      select: "flag_key,enabled",
      flag_key: `in.(${keys.join(",")})`,
    });
    return rows.map((row) => ({
      flagKey: stringField(row, "flag_key"),
      enabled: booleanField(row, "enabled"),
    }));
  }

  async findQuotaUsage(
    userId: string,
    feature: AiFeature,
    periodStart: string,
  ): Promise<QuotaUsageRecord | null> {
    this.assertOwner(userId);
    const rows = await this.select("ai_quota_usage", {
      select: "user_id,feature,successful_count,reserved_count,period_start",
      user_id: `eq.${this.environment.authenticatedUserId}`,
      feature: `eq.${feature}`,
      period_start: `eq.${periodStart}`,
      limit: "1",
    });
    const row = rows[0];
    if (!row) return null;
    return {
      userId: stringField(row, "user_id"),
      feature: stringField(row, "feature"),
      successfulCount: integerField(row, "successful_count"),
      reservedCount: integerField(row, "reserved_count"),
      periodStart: stringField(row, "period_start"),
    };
  }

  async listPlanLimits(planTier?: PlanTier): Promise<PlanLimitRecord[]> {
    const filters: Record<string, string> = {
      select: "plan_tier,feature,period_kind,quota_limit,max_per_request,max_per_topic_month",
    };
    if (planTier) filters.plan_tier = `eq.${planTier}`;
    const rows = await this.select("ai_plan_limits", filters);
    return rows.map((row) => ({
      planTier: planTierField(row, "plan_tier"),
      feature: stringField(row, "feature"),
      periodKind: periodKindField(row, "period_kind"),
      quotaLimit: integerField(row, "quota_limit"),
      maxPerRequest: nullableIntegerField(row, "max_per_request"),
      maxPerTopicMonth: nullableIntegerField(row, "max_per_topic_month"),
    }));
  }

  private async select(
    table: string,
    filters: Record<string, string>,
  ): Promise<Record<string, unknown>[]> {
    const url = new URL(
      `${this.environment.supabaseUrl.replace(/\/$/, "")}/rest/v1/${table}`,
    );
    Object.entries(filters).forEach(([key, value]) =>
      url.searchParams.set(key, value)
    );
    const fetcher = this.environment.fetcher ?? fetch;
    const headers: Record<string, string> = {
      apikey: this.environment.publishableKey,
      authorization: `Bearer ${this.environment.accessToken}`,
      accept: "application/json",
    };
    let response: Response;
    try {
      response = await fetcher(url, {
        headers,
      });
    } catch {
      throw new AccessDataError();
    }
    if (!response.ok) throw new AccessDataError();
    let payload: unknown;
    try {
      payload = await response.json();
    } catch {
      throw new AccessDataError();
    }
    if (!Array.isArray(payload)) throw new AccessDataError();
    return payload.filter((row): row is Record<string, unknown> =>
      typeof row === "object" && row !== null
    );
  }

  private assertOwner(userId: string): void {
    if (userId !== this.environment.authenticatedUserId) {
      throw new AccessDataError();
    }
  }
}

function stringField(row: Record<string, unknown>, key: string): string {
  const value = row[key];
  if (typeof value !== "string" || value.trim().length === 0) {
    throw new AccessDataError();
  }
  return value;
}

function booleanField(row: Record<string, unknown>, key: string): boolean {
  if (typeof row[key] !== "boolean") throw new AccessDataError();
  return row[key] as boolean;
}

function integerField(row: Record<string, unknown>, key: string): number {
  if (typeof row[key] !== "number" || !Number.isInteger(row[key])) {
    throw new AccessDataError();
  }
  return row[key] as number;
}

function nullableIntegerField(row: Record<string, unknown>, key: string): number | null {
  if (row[key] === null || row[key] === undefined) return null;
  return integerField(row, key);
}

function planTierField(row: Record<string, unknown>, key: string): PlanTier {
  const value = row[key] ?? "FREE";
  if (typeof value !== "string" || !PLAN_TIERS.includes(value as PlanTier)) {
    throw new AccessDataError();
  }
  return value as PlanTier;
}

function periodKindField(row: Record<string, unknown>, key: string): PlanPeriodKind {
  const value = row[key];
  if (value !== "LIFETIME" && value !== "MONTHLY" && value !== "DAILY") {
    throw new AccessDataError();
  }
  return value;
}

/** Same rule as public.ai_effective_plan_tier: an expired paid plan falls back to FREE. */
export function effectivePlanTier(profile: ProfileRecord | null, now: Date): PlanTier {
  const tier = profile?.planTier ?? "FREE";
  if (tier === "FREE") return tier;
  const renewsAt = profile?.planRenewsAt;
  if (!renewsAt) return tier;
  return Date.parse(renewsAt) > now.getTime() ? tier : "FREE";
}

/** Same rule as public.ai_plan_quota_row: LIFETIME wins over MONTHLY; DAILY is ignored. */
export function quotaLimitRow(
  limits: readonly PlanLimitRecord[],
  tier: PlanTier,
  feature: string,
): PlanLimitRecord | null {
  const rows = limits.filter((row) => row.planTier === tier && row.feature === feature);
  return rows.find((row) => row.periodKind === "LIFETIME") ??
    rows.find((row) => row.periodKind === "MONTHLY") ?? null;
}

function dateInSaoPaulo(now: Date): string {
  const parts = new Intl.DateTimeFormat("en-US", {
    timeZone: TIME_ZONE,
    year: "numeric",
    month: "2-digit",
    day: "2-digit",
  }).formatToParts(now);
  const values = Object.fromEntries(
    parts.map((part) => [part.type, part.value]),
  );
  return `${values.year}-${values.month}-${values.day}`;
}

export function quotaPeriodStart(periodKind: PlanPeriodKind | null, now: Date): string {
  if (periodKind !== "MONTHLY") return "1970-01-01";
  const [year, month] = dateInSaoPaulo(now).split("-");
  return `${year}-${month}-01`;
}

function saoPauloMidnight(year: number, month: number, day: number): string {
  const midnightUtc = new Date(Date.UTC(year, month - 1, day));
  const offset = new Intl.DateTimeFormat("en-US", {
    timeZone: TIME_ZONE,
    timeZoneName: "shortOffset",
  }).formatToParts(midnightUtc).find((part) => part.type === "timeZoneName")
    ?.value ?? "GMT";
  const match = /^GMT(?:(\+|-)(\d{1,2})(?::(\d{2}))?)?$/.exec(offset);
  if (!match) throw new AccessDataError();
  const offsetMinutes = match[1]
    ? (match[1] === "+" ? 1 : -1) *
      (Number(match[2]) * 60 + Number(match[3] ?? 0))
    : 0;
  return new Date(midnightUtc.getTime() - offsetMinutes * 60_000).toISOString();
}

export function nextMonthlyReset(now: Date): string {
  const [year, month] = dateInSaoPaulo(now).split("-").map(Number);
  return saoPauloMidnight(month === 12 ? year + 1 : year, month === 12 ? 1 : month + 1, 1);
}

const SERVER_FEATURES: readonly AiFeature[] = [
  "SYLLABUS_GENERATION",
  "PLAN_GENERATION",
  "CONTENT_GENERATION",
  "SIMULATION_GENERATION",
];

export interface PlanSummary {
  planTier: PlanTier;
  planRenewsAt: string | null;
  betaAccess: boolean;
  plans: {
    planTier: PlanTier;
    limits: {
      feature: string;
      periodKind: PlanPeriodKind;
      quotaLimit: number;
      maxPerRequest: number | null;
      maxPerTopicMonth: number | null;
    }[];
  }[];
  usage: {
    feature: AiFeature;
    periodKind: PlanPeriodKind | null;
    limit: number;
    used: number;
    remaining: number;
    periodStart: string;
    resetAt: string | null;
  }[];
}

export class ClosedBetaAiPolicy {
  private readonly now: () => Date;

  constructor(
    private readonly dataSource: AccessDataSource,
    options: AccessPolicyOptions = {},
  ) {
    this.now = options.now ?? (() => new Date());
  }

  /** Plano efetivo, catálogo de planos e uso atual; alimenta a tela "Planos e uso". */
  async getPlanSummary(userId: string): Promise<PlanSummary> {
    const now = this.now();
    const [profile, limits] = await Promise.all([
      this.dataSource.findProfile(userId),
      this.dataSource.listPlanLimits(),
    ]);
    const tier = effectivePlanTier(profile, now);
    const usage = await Promise.all(
      SERVER_FEATURES.map(async (feature) => {
        const row = quotaLimitRow(limits, tier, feature);
        const periodKind = row?.periodKind ?? null;
        const periodStart = quotaPeriodStart(periodKind, now);
        const record = await this.dataSource.findQuotaUsage(userId, feature, periodStart);
        const used = (record?.successfulCount ?? 0) + (record?.reservedCount ?? 0);
        const limit = row?.quotaLimit ?? 0;
        return {
          feature,
          periodKind,
          limit,
          used,
          remaining: Math.max(limit - used, 0),
          periodStart,
          resetAt: periodKind === "MONTHLY" ? nextMonthlyReset(now) : null,
        };
      }),
    );
    return {
      planTier: tier,
      planRenewsAt: tier === "FREE" ? null : profile?.planRenewsAt ?? null,
      betaAccess: profile?.betaAccess === true,
      plans: PLAN_TIERS.map((planTier) => ({
        planTier,
        limits: limits
          .filter((row) => row.planTier === planTier)
          .map(({ feature, periodKind, quotaLimit, maxPerRequest, maxPerTopicMonth }) => ({
            feature,
            periodKind,
            quotaLimit,
            maxPerRequest,
            maxPerTopicMonth,
          })),
      })),
      usage,
    };
  }

  async canUse(userId: string, feature: AiFeature): Promise<boolean> {
    return (await this.getAccess(userId, feature)).canUse;
  }

  async getAccess(userId: string, feature: AiFeature): Promise<AiAccess> {
    const now = this.now();
    const featureFlagKey = FEATURE_FLAG_KEYS[feature];
    const [profile, flags] = await Promise.all([
      this.dataSource.findProfile(userId),
      this.dataSource.listFeatureFlags(["AI_BETA_ENABLED", featureFlagKey]),
    ]);
    const tier = effectivePlanTier(profile, now);
    const limitRow = quotaLimitRow(await this.dataSource.listPlanLimits(tier), tier, feature);
    const periodKind = limitRow?.periodKind ?? null;
    const periodStart = quotaPeriodStart(periodKind, now);
    // Sem configuração o recurso fica fechado, igual a public.ai_quota_limit.
    const limit = limitRow?.quotaLimit ?? 0;
    const usage = await this.dataSource.findQuotaUsage(userId, feature, periodStart);
    const flagMap = new Map(flags.map((flag) => [flag.flagKey, flag.enabled]));
    const betaAccess = profile?.betaAccess === true;
    const betaEnabled = flagMap.get("AI_BETA_ENABLED") === true;
    const featureEnabled = betaEnabled && flagMap.get(featureFlagKey) === true;
    const successfulCount = usage?.successfulCount ?? 0;
    const reservedCount = usage?.reservedCount ?? 0;
    const remaining = Math.max(limit - successfulCount - reservedCount, 0);
    const quota: AiQuota = {
      feature,
      limit,
      successfulCount,
      reservedCount,
      used: successfulCount + reservedCount,
      remaining,
      periodStart,
      resetAt: periodKind === "MONTHLY" ? nextMonthlyReset(now) : null,
    };

    let reasonCode: string | null = null;
    if (!betaAccess) reasonCode = "BETA_ACCESS_REQUIRED";
    else if (!betaEnabled) reasonCode = "BETA_DISABLED";
    else if (!featureEnabled) reasonCode = "FEATURE_DISABLED";
    else if (successfulCount >= limit) reasonCode = "QUOTA_EXHAUSTED";
    else if (remaining === 0) reasonCode = "QUOTA_RESERVED";

    return {
      authenticated: true,
      betaAccess,
      feature,
      featureEnabled,
      quota,
      canUse: reasonCode === null,
      reasonCode,
    };
  }
}
