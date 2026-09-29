/**
 * Play Integrity (API padrão) para pedidos que gastam cota de IA.
 *
 * O app pede ao Google um token amarrado a um requestHash deste pedido. O servidor manda o token
 * para o Google decodificar e só aceita quando:
 * - o app é o binário reconhecido pela Play Store (APK modificado/re-assinado é recusado);
 * - o requestHash é o deste pedido (token de outro pedido não serve);
 * - o token é recente;
 * - o aparelho atende ao nível mínimo configurado;
 * - opcionalmente, a instalação é licenciada pela Play.
 *
 * Modos (PLAY_INTEGRITY_MODE): off (padrão, não verifica), log (verifica e só registra) e
 * enforce (recusa). Assim dá para publicar, observar em "log" e só depois bloquear.
 */

export type IntegrityMode = "off" | "log" | "enforce";
export type DeviceLevel = "BASIC" | "DEVICE" | "STRONG";

export interface IntegrityPolicy {
  mode: IntegrityMode;
  packageName: string;
  deviceLevel: DeviceLevel;
  requireLicensed: boolean;
  maxTokenAgeMillis: number;
}

export class IntegrityError extends Error {
  constructor(public readonly code: "INTEGRITY_REQUIRED" | "INTEGRITY_FAILED" | "INTEGRITY_UNAVAILABLE") {
    super(code);
    this.name = "IntegrityError";
  }
}

export interface IntegrityVerifier {
  verify(token: string | null, expectedRequestHash: string): Promise<void>;
}

export interface IntegrityTokenDecoder {
  decode(token: string): Promise<unknown>;
}

const DEVICE_LABELS: Record<DeviceLevel, readonly string[]> = {
  BASIC: ["MEETS_BASIC_INTEGRITY", "MEETS_DEVICE_INTEGRITY", "MEETS_STRONG_INTEGRITY"],
  DEVICE: ["MEETS_DEVICE_INTEGRITY", "MEETS_STRONG_INTEGRITY"],
  STRONG: ["MEETS_STRONG_INTEGRITY"],
};

/** Mesmo cálculo do app: SHA-256 hex de "estudario|<feature>|<idempotencyKey>|<sourceHash>". */
export async function integrityRequestHash(feature: string, idempotencyKey: string, sourceHash: string | null): Promise<string> {
  const input = `estudario|${feature}|${idempotencyKey}|${(sourceHash ?? "").toLowerCase()}`;
  const digest = await crypto.subtle.digest("SHA-256", new TextEncoder().encode(input));
  return [...new Uint8Array(digest)].map((byte) => byte.toString(16).padStart(2, "0")).join("");
}

function record(value: unknown): Record<string, unknown> {
  return value !== null && typeof value === "object" && !Array.isArray(value) ? value as Record<string, unknown> : {};
}

/** Motivos de recusa do veredito decodificado. Lista vazia significa aprovado. */
export function integrityFailures(
  decoded: unknown,
  expectedRequestHash: string,
  policy: IntegrityPolicy,
  now: Date,
): string[] {
  const payload = record(record(decoded).tokenPayloadExternal);
  const request = record(payload.requestDetails);
  const app = record(payload.appIntegrity);
  const device = record(payload.deviceIntegrity);
  const account = record(payload.accountDetails);
  const failures: string[] = [];

  if (request.requestPackageName !== policy.packageName) failures.push("PACKAGE_MISMATCH");
  if (request.requestHash !== expectedRequestHash) failures.push("REQUEST_HASH_MISMATCH");
  const timestamp = Number(request.timestampMillis);
  if (!Number.isFinite(timestamp) || Math.abs(now.getTime() - timestamp) > policy.maxTokenAgeMillis) {
    failures.push("TOKEN_STALE");
  }
  if (app.appRecognitionVerdict !== "PLAY_RECOGNIZED") failures.push("APP_NOT_RECOGNIZED");
  if (app.packageName !== undefined && app.packageName !== policy.packageName) failures.push("APP_PACKAGE_MISMATCH");
  const labels = Array.isArray(device.deviceRecognitionVerdict) ? device.deviceRecognitionVerdict : [];
  if (!labels.some((label) => DEVICE_LABELS[policy.deviceLevel].includes(String(label)))) {
    failures.push("DEVICE_INTEGRITY");
  }
  if (policy.requireLicensed && account.appLicensingVerdict !== "LICENSED") failures.push("NOT_LICENSED");
  return failures;
}

export class PlayIntegrityVerifier implements IntegrityVerifier {
  constructor(
    private readonly policy: IntegrityPolicy,
    private readonly decoder: IntegrityTokenDecoder | null,
    private readonly now: () => Date = () => new Date(),
    private readonly log: (entry: Record<string, unknown>) => void = (entry) => console.warn(JSON.stringify(entry)),
  ) {}

  async verify(token: string | null, expectedRequestHash: string): Promise<void> {
    if (this.policy.mode === "off") return;
    let failures: string[];
    if (!token) {
      failures = ["TOKEN_MISSING"];
    } else if (!this.decoder) {
      failures = ["DECODER_UNAVAILABLE"];
    } else {
      try {
        failures = integrityFailures(await this.decoder.decode(token), expectedRequestHash, this.policy, this.now());
      } catch {
        failures = ["DECODE_FAILED"];
      }
    }
    if (failures.length === 0) return;
    this.log({ event: "play_integrity_rejected", mode: this.policy.mode, failures });
    if (this.policy.mode !== "enforce") return;
    if (failures.includes("TOKEN_MISSING")) throw new IntegrityError("INTEGRITY_REQUIRED");
    if (failures.includes("DECODER_UNAVAILABLE") || failures.includes("DECODE_FAILED")) {
      throw new IntegrityError("INTEGRITY_UNAVAILABLE");
    }
    throw new IntegrityError("INTEGRITY_FAILED");
  }
}

interface ServiceAccount {
  client_email: string;
  private_key: string;
  token_uri?: string;
}

function base64Url(bytes: Uint8Array | string): string {
  const data = typeof bytes === "string" ? new TextEncoder().encode(bytes) : bytes;
  let binary = "";
  data.forEach((byte) => binary += String.fromCharCode(byte));
  return btoa(binary).replace(/\+/g, "-").replace(/\//g, "_").replace(/=+$/, "");
}

function pemToPkcs8(pem: string): ArrayBuffer {
  const body = pem.replace(/-----(BEGIN|END) PRIVATE KEY-----/g, "").replace(/\s+/g, "");
  const binary = atob(body);
  const bytes = new Uint8Array(binary.length);
  for (let index = 0; index < binary.length; index++) bytes[index] = binary.charCodeAt(index);
  return bytes.buffer;
}

/** Decodifica tokens na API do Google com uma conta de serviço (segredo só no servidor). */
export class GooglePlayIntegrityDecoder implements IntegrityTokenDecoder {
  private cached: { token: string; expiresAt: number } | null = null;

  constructor(
    private readonly serviceAccount: ServiceAccount,
    private readonly packageName: string,
    private readonly fetcher: typeof fetch = fetch,
  ) {}

  static fromEnvironmentJson(json: string, packageName: string): GooglePlayIntegrityDecoder | null {
    try {
      const parsed = JSON.parse(json) as ServiceAccount;
      if (typeof parsed.client_email !== "string" || typeof parsed.private_key !== "string") return null;
      return new GooglePlayIntegrityDecoder(parsed, packageName);
    } catch {
      return null;
    }
  }

  async decode(token: string): Promise<unknown> {
    const accessToken = await this.accessToken();
    const response = await this.fetcher(
      `https://playintegrity.googleapis.com/v1/${encodeURIComponent(this.packageName)}:decodeIntegrityToken`,
      {
        method: "POST",
        headers: { authorization: `Bearer ${accessToken}`, "content-type": "application/json" },
        body: JSON.stringify({ integrity_token: token }),
      },
    );
    if (!response.ok) throw new IntegrityError("INTEGRITY_UNAVAILABLE");
    return await response.json();
  }

  private async accessToken(): Promise<string> {
    const nowSeconds = Math.floor(Date.now() / 1000);
    if (this.cached && this.cached.expiresAt - 60 > nowSeconds) return this.cached.token;
    const tokenUri = this.serviceAccount.token_uri ?? "https://oauth2.googleapis.com/token";
    const header = base64Url(JSON.stringify({ alg: "RS256", typ: "JWT" }));
    const claims = base64Url(JSON.stringify({
      iss: this.serviceAccount.client_email,
      scope: "https://www.googleapis.com/auth/playintegrity",
      aud: tokenUri,
      iat: nowSeconds,
      exp: nowSeconds + 3600,
    }));
    const key = await crypto.subtle.importKey(
      "pkcs8",
      pemToPkcs8(this.serviceAccount.private_key),
      { name: "RSASSA-PKCS1-v1_5", hash: "SHA-256" },
      false,
      ["sign"],
    );
    const signature = new Uint8Array(
      await crypto.subtle.sign("RSASSA-PKCS1-v1_5", key, new TextEncoder().encode(`${header}.${claims}`)),
    );
    const response = await this.fetcher(tokenUri, {
      method: "POST",
      headers: { "content-type": "application/x-www-form-urlencoded" },
      body: new URLSearchParams({
        grant_type: "urn:ietf:params:oauth:grant-type:jwt-bearer",
        assertion: `${header}.${claims}.${base64Url(signature)}`,
      }),
    });
    if (!response.ok) throw new IntegrityError("INTEGRITY_UNAVAILABLE");
    const body = await response.json() as { access_token?: string; expires_in?: number };
    if (typeof body.access_token !== "string") throw new IntegrityError("INTEGRITY_UNAVAILABLE");
    this.cached = { token: body.access_token, expiresAt: nowSeconds + (body.expires_in ?? 3600) };
    return body.access_token;
  }
}

/** Monta o verificador a partir das variáveis de ambiente da função. */
export function integrityVerifierFromEnvironment(env: (name: string) => string | undefined): PlayIntegrityVerifier {
  const rawMode = env("PLAY_INTEGRITY_MODE")?.trim().toLowerCase();
  const mode: IntegrityMode = rawMode === "log" || rawMode === "enforce" ? rawMode : "off";
  const rawLevel = env("PLAY_INTEGRITY_DEVICE_LEVEL")?.trim().toUpperCase();
  const deviceLevel: DeviceLevel = rawLevel === "DEVICE" || rawLevel === "STRONG" ? rawLevel : "BASIC";
  const packageName = env("PLAY_INTEGRITY_PACKAGE_NAME")?.trim() || "br.com.estudario";
  const serviceAccount = env("PLAY_INTEGRITY_SERVICE_ACCOUNT_JSON");
  const decoder = serviceAccount ? GooglePlayIntegrityDecoder.fromEnvironmentJson(serviceAccount, packageName) : null;
  return new PlayIntegrityVerifier(
    {
      mode,
      packageName,
      deviceLevel,
      requireLicensed: env("PLAY_INTEGRITY_REQUIRE_LICENSED")?.trim().toLowerCase() !== "false",
      maxTokenAgeMillis: 10 * 60 * 1000,
    },
    decoder,
  );
}
