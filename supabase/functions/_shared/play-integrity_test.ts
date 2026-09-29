import { assertEquals, assertRejects } from "https://deno.land/std@0.224.0/assert/mod.ts";
import {
  type IntegrityPolicy,
  IntegrityError,
  integrityFailures,
  integrityRequestHash,
  PlayIntegrityVerifier,
} from "./play-integrity.ts";

const NOW = new Date("2026-09-29T12:00:00Z");
const POLICY: IntegrityPolicy = {
  mode: "enforce",
  packageName: "br.com.estudario",
  deviceLevel: "BASIC",
  requireLicensed: true,
  maxTokenAgeMillis: 600_000,
};

function verdict(overrides: Record<string, unknown> = {}) {
  return {
    tokenPayloadExternal: {
      requestDetails: { requestPackageName: "br.com.estudario", requestHash: "h", timestampMillis: String(NOW.getTime() - 1000) },
      appIntegrity: { appRecognitionVerdict: "PLAY_RECOGNIZED", packageName: "br.com.estudario" },
      deviceIntegrity: { deviceRecognitionVerdict: ["MEETS_DEVICE_INTEGRITY"] },
      accountDetails: { appLicensingVerdict: "LICENSED" },
      ...overrides,
    },
  };
}

Deno.test("genuine Play install passes", () => {
  assertEquals(integrityFailures(verdict(), "h", POLICY, NOW), []);
});

Deno.test("re-signed or sideloaded APK is rejected", () => {
  const failures = integrityFailures(
    verdict({ appIntegrity: { appRecognitionVerdict: "UNRECOGNIZED_VERSION" }, accountDetails: { appLicensingVerdict: "UNLICENSED" } }),
    "h",
    POLICY,
    NOW,
  );
  assertEquals(failures, ["APP_NOT_RECOGNIZED", "NOT_LICENSED"]);
});

Deno.test("token for another request or too old is rejected", () => {
  assertEquals(integrityFailures(verdict(), "other", POLICY, NOW), ["REQUEST_HASH_MISMATCH"]);
  const old = verdict({ requestDetails: { requestPackageName: "br.com.estudario", requestHash: "h", timestampMillis: "0" } });
  assertEquals(integrityFailures(old, "h", POLICY, NOW), ["TOKEN_STALE"]);
});

Deno.test("device level is configurable", () => {
  const basicOnly = verdict({ deviceIntegrity: { deviceRecognitionVerdict: ["MEETS_BASIC_INTEGRITY"] } });
  assertEquals(integrityFailures(basicOnly, "h", POLICY, NOW), []);
  assertEquals(integrityFailures(basicOnly, "h", { ...POLICY, deviceLevel: "DEVICE" }, NOW), ["DEVICE_INTEGRITY"]);
  const emulator = verdict({ deviceIntegrity: {} });
  assertEquals(integrityFailures(emulator, "h", POLICY, NOW), ["DEVICE_INTEGRITY"]);
});

Deno.test("request hash matches the app formula", async () => {
  assertEquals(
    await integrityRequestHash("SYLLABUS_GENERATION", "key", "ABC"),
    await integrityRequestHash("SYLLABUS_GENERATION", "key", "abc"),
  );
  assertEquals((await integrityRequestHash("SYLLABUS_GENERATION", "key", null)).length, 64);
});

Deno.test("modes: off skips, log allows, enforce blocks", async () => {
  const decoder = { decode: () => Promise.resolve(verdict({ appIntegrity: { appRecognitionVerdict: "UNEVALUATED" } })) };
  const logs: unknown[] = [];
  await new PlayIntegrityVerifier({ ...POLICY, mode: "off" }, null, () => NOW).verify(null, "h");
  await new PlayIntegrityVerifier({ ...POLICY, mode: "log" }, decoder, () => NOW, (entry) => logs.push(entry)).verify("t", "h");
  assertEquals(logs.length, 1);
  await assertRejects(
    () => new PlayIntegrityVerifier(POLICY, decoder, () => NOW, () => {}).verify("t", "h"),
    IntegrityError,
    "INTEGRITY_FAILED",
  );
  await assertRejects(
    () => new PlayIntegrityVerifier(POLICY, decoder, () => NOW, () => {}).verify(null, "h"),
    IntegrityError,
    "INTEGRITY_REQUIRED",
  );
});

Deno.test("decoder signs a service-account JWT and calls the decode endpoint", async () => {
  const { GooglePlayIntegrityDecoder } = await import("./play-integrity.ts");
  const pair = await crypto.subtle.generateKey(
    { name: "RSASSA-PKCS1-v1_5", modulusLength: 2048, publicExponent: new Uint8Array([1, 0, 1]), hash: "SHA-256" },
    true,
    ["sign", "verify"],
  );
  const pkcs8 = new Uint8Array(await crypto.subtle.exportKey("pkcs8", pair.privateKey));
  const pem = `-----BEGIN PRIVATE KEY-----\n${btoa(String.fromCharCode(...pkcs8))}\n-----END PRIVATE KEY-----\n`;
  const calls: string[] = [];
  const fetcher = (async (input: string | URL | Request, init?: RequestInit) => {
    const url = String(input);
    calls.push(url);
    if (url.includes("oauth2")) {
      const assertion = new URLSearchParams(String(init?.body)).get("assertion")!;
      const [header, claims, signature] = assertion.split(".");
      const decode = (value: string) => Uint8Array.from(atob(value.replace(/-/g, "+").replace(/_/g, "/")), (c) => c.charCodeAt(0));
      const valid = await crypto.subtle.verify("RSASSA-PKCS1-v1_5", pair.publicKey, decode(signature), new TextEncoder().encode(`${header}.${claims}`));
      return new Response(JSON.stringify(valid ? { access_token: "google-token", expires_in: 3600 } : {}), { status: valid ? 200 : 400 });
    }
    assertEquals(init?.headers && (init.headers as Record<string, string>).authorization, "Bearer google-token");
    return new Response(JSON.stringify(verdict()), { status: 200 });
  }) as typeof fetch;
  const decoder = new GooglePlayIntegrityDecoder({ client_email: "sa@x.iam.gserviceaccount.com", private_key: pem }, "br.com.estudario", fetcher);
  assertEquals(integrityFailures(await decoder.decode("token"), "h", POLICY, NOW), []);
  await decoder.decode("token");
  assertEquals(calls.filter((url) => url.includes("oauth2")).length, 1);
  assertEquals(calls[1], "https://playintegrity.googleapis.com/v1/br.com.estudario:decodeIntegrityToken");
});
