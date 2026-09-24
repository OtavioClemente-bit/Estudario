import type { AiAccess, AiFeature, AiQuota } from "./contracts.ts";

const LIMIT = 1;
const DAILY_FEATURE: AiFeature = "CONTENT_GENERATION";
const TIME_ZONE = "America/Sao_Paulo";

const FEATURE_FLAG_KEYS: Record<AiFeature, string> = {
  SYLLABUS_GENERATION: "SYLLABUS_AI_ENABLED",
  PLAN_GENERATION: "PLAN_AI_ENABLED",
  CONTENT_GENERATION: "CONTENT_AI_ENABLED",
};

export interface ProfileRecord {
  userId: string;
  betaAccess: boolean;
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
  findQuotaUsage(userId: string, feature: AiFeature, periodStart: string): Promise<QuotaUsageRecord | null>;
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

interface SupabaseAccessEnvironment {
  supabaseUrl: string;
  serviceRoleKey: string;
  fetcher?: typeof fetch;
}

function runtimeEnvironment(): SupabaseAccessEnvironment {
  const supabaseUrl = Deno.env.get("SUPABASE_URL")?.trim();
  const serviceRoleKey = Deno.env.get("SUPABASE_SERVICE_ROLE_KEY")?.trim();
  if (!supabaseUrl || !serviceRoleKey) throw new AccessDataError();
  return { supabaseUrl, serviceRoleKey };
}

export class SupabaseAccessDataSource implements AccessDataSource {
  constructor(private readonly environment?: SupabaseAccessEnvironment) {}

  async findProfile(userId: string): Promise<ProfileRecord | null> {
    const rows = await this.select("profiles", {
      select: "user_id,beta_access",
      user_id: `eq.${userId}`,
      limit: "1",
    });
    const row = rows[0];
    if (!row) return null;
    return {
      userId: stringField(row, "user_id"),
      betaAccess: booleanField(row, "beta_access"),
    };
  }

  async listFeatureFlags(keys: readonly string[]): Promise<FeatureFlagRecord[]> {
    const rows = await this.select("ai_feature_flags", {
      select: "flag_key,enabled",
      flag_key: `in.(${keys.join(",")})`,
    });
    return rows.map((row) => ({
      flagKey: stringField(row, "flag_key"),
      enabled: booleanField(row, "enabled"),
    }));
  }

  async findQuotaUsage(userId: string, feature: AiFeature, periodStart: string): Promise<QuotaUsageRecord | null> {
    const rows = await this.select("ai_quota_usage", {
      select: "user_id,feature,successful_count,reserved_count,period_start",
      user_id: `eq.${userId}`,
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

  private async select(table: string, filters: Record<string, string>): Promise<Record<string, unknown>[]> {
    const environment = this.environment ?? runtimeEnvironment();
    const url = new URL(`${environment.supabaseUrl.replace(/\/$/, "")}/rest/v1/${table}`);
    Object.entries(filters).forEach(([key, value]) => url.searchParams.set(key, value));
    const fetcher = environment.fetcher ?? fetch;
    let response: Response;
    try {
      response = await fetcher(url, {
        headers: {
          apikey: environment.serviceRoleKey,
          authorization: `Bearer ${environment.serviceRoleKey}`,
          accept: "application/json",
        },
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
    return payload.filter((row): row is Record<string, unknown> => typeof row === "object" && row !== null);
  }
}

function stringField(row: Record<string, unknown>, key: string): string {
  const value = row[key];
  if (typeof value !== "string" || value.trim().length === 0) throw new AccessDataError();
  return value;
}

function booleanField(row: Record<string, unknown>, key: string): boolean {
  if (typeof row[key] !== "boolean") throw new AccessDataError();
  return row[key] as boolean;
}

function integerField(row: Record<string, unknown>, key: string): number {
  if (typeof row[key] !== "number" || !Number.isInteger(row[key])) throw new AccessDataError();
  return row[key] as number;
}

function dateInSaoPaulo(now: Date): string {
  const parts = new Intl.DateTimeFormat("en-US", {
    timeZone: TIME_ZONE,
    year: "numeric",
    month: "2-digit",
    day: "2-digit",
  }).formatToParts(now);
  const values = Object.fromEntries(parts.map((part) => [part.type, part.value]));
  return `${values.year}-${values.month}-${values.day}`;
}

export function quotaPeriodStart(feature: AiFeature, now: Date): string {
  return feature === DAILY_FEATURE ? dateInSaoPaulo(now) : "1970-01-01";
}

export class ClosedBetaAiPolicy {
  private readonly now: () => Date;

  constructor(
    private readonly dataSource: AccessDataSource,
    options: AccessPolicyOptions = {},
  ) {
    this.now = options.now ?? (() => new Date());
  }

  async canUse(userId: string, feature: AiFeature): Promise<boolean> {
    return (await this.getAccess(userId, feature)).canUse;
  }

  async getAccess(userId: string, feature: AiFeature): Promise<AiAccess> {
    const periodStart = quotaPeriodStart(feature, this.now());
    const featureFlagKey = FEATURE_FLAG_KEYS[feature];
    const [profile, flags, usage] = await Promise.all([
      this.dataSource.findProfile(userId),
      this.dataSource.listFeatureFlags(["AI_BETA_ENABLED", featureFlagKey]),
      this.dataSource.findQuotaUsage(userId, feature, periodStart),
    ]);
    const flagMap = new Map(flags.map((flag) => [flag.flagKey, flag.enabled]));
    const betaAccess = profile?.betaAccess === true;
    const betaEnabled = flagMap.get("AI_BETA_ENABLED") === true;
    const featureEnabled = betaEnabled && flagMap.get(featureFlagKey) === true;
    const successfulCount = usage?.successfulCount ?? 0;
    const reservedCount = usage?.reservedCount ?? 0;
    const remaining = Math.max(LIMIT - successfulCount - reservedCount, 0);
    const quota: AiQuota | null = successfulCount >= LIMIT
      ? null
      : {
        feature,
        limit: LIMIT,
        successfulCount,
        reservedCount,
        remaining,
        periodStart,
      };

    let reasonCode: string | null = null;
    if (!betaAccess) reasonCode = "BETA_ACCESS_REQUIRED";
    else if (!betaEnabled) reasonCode = "BETA_DISABLED";
    else if (!featureEnabled) reasonCode = "FEATURE_DISABLED";
    else if (successfulCount >= LIMIT) reasonCode = "QUOTA_EXHAUSTED";
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
