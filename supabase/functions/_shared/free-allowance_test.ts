import { assertEquals } from "jsr:@std/assert@1";
import { adCreditAllowed, decideFreeContent, type FreeUsage, parsePurpose, usageFrom } from "./free-allowance.ts";
import { derToRaw, isOurAdUnit, verifyRewardCall } from "../ad-reward-ssv/index.ts";

const none: FreeUsage = { planDayToday: 0, extrasMonth: 0, weeklyAdvances: 0, adCreditsToday: 0, adCreditsMonth: 0, adCreditsAvailable: 0 };

Deno.test("app antigo sem motivo vale como extra", () => {
  assertEquals(parsePurpose(undefined), "EXTRA");
  assertEquals(parsePurpose("qualquer"), "EXTRA");
  assertEquals(parsePurpose("PLAN_DAY"), "PLAN_DAY");
});

Deno.test("dia do plano: até 2 por dia", () => {
  assertEquals(decideFreeContent("PLAN_DAY", { ...none, planDayToday: 1 }).ok, true);
  assertEquals(decideFreeContent("PLAN_DAY", { ...none, planDayToday: 2 }), { ok: false, code: "FREE_PLAN_DAY_LIMIT" });
});

Deno.test("fora do plano não é do Grátis", () => {
  assertEquals(decideFreeContent("EXTRA", none), { ok: false, code: "FREE_EXTRA_LIMIT" });
});

Deno.test("adiantar: crédito de anúncio, senão 1 por semana", () => {
  assertEquals(decideFreeContent("ADVANCE", { ...none, adCreditsAvailable: 1, weeklyAdvances: 1 }), { ok: true, useCredit: true, weekly: false });
  assertEquals(decideFreeContent("ADVANCE", none), { ok: true, useCredit: false, weekly: true });
  assertEquals(decideFreeContent("ADVANCE", { ...none, weeklyAdvances: 1 }), { ok: false, code: "FREE_ADVANCE_NEEDS_AD" });
});

Deno.test("anúncio vira crédito até 2 por dia e 5 por mês", () => {
  assertEquals(adCreditAllowed({ adCreditsToday: 1, adCreditsMonth: 4 }), true);
  assertEquals(adCreditAllowed({ adCreditsToday: 2, adCreditsMonth: 3 }), false);
  assertEquals(adCreditAllowed({ adCreditsToday: 0, adCreditsMonth: 5 }), false);
});

Deno.test("contagem pelo horário de Brasília e crédito vencido não conta", () => {
  const now = new Date("2026-10-24T15:00:00Z"); // 12h em Brasília
  const usage = usageFrom(
    [
      { created_at: "2026-10-24T04:00:00Z", purpose: "PLAN_DAY" }, // 1h de hoje em Brasília
      { created_at: "2026-10-24T02:00:00Z", purpose: "PLAN_DAY" }, // 23h de ontem em Brasília
      { created_at: "2026-10-02T12:00:00Z" },
      { created_at: "2026-10-20T12:00:00Z", purpose: "ADVANCE", weekly: true },
    ],
    [
      { id: "a", created_at: "2026-10-24T14:00:00Z", used_at: null },
      { id: "b", created_at: "2026-10-24T05:00:00Z", used_at: null }, // 10 h atrás: venceu
    ],
    now,
  );
  assertEquals(usage.planDayToday, 1);
  assertEquals(usage.extrasMonth, 1);
  assertEquals(usage.weeklyAdvances, 1);
  assertEquals(usage.adCreditsToday, 2);
  assertEquals(usage.adCreditsAvailable, 1);
});

/** r||s (WebCrypto) para DER, como o Google manda. */
function rawToDer(raw: Uint8Array): Uint8Array {
  const int = (bytes: Uint8Array) => {
    let b = Array.from(bytes);
    while (b.length > 1 && b[0] === 0) b = b.slice(1);
    if (b[0] & 0x80) b = [0, ...b];
    return [0x02, b.length, ...b];
  };
  const body = [...int(raw.slice(0, 32)), ...int(raw.slice(32))];
  return new Uint8Array([0x30, body.length, ...body]);
}
const b64url = (bytes: Uint8Array) => btoa(String.fromCharCode(...bytes)).replace(/\+/g, "-").replace(/\//g, "_").replace(/=+$/, "");

Deno.test("assinatura do anúncio: vale só a do Google, sem alteração", async () => {
  const pair = await crypto.subtle.generateKey({ name: "ECDSA", namedCurve: "P-256" }, true, ["sign", "verify"]);
  const content = "ad_network=5450213213286189855&ad_unit=123&reward_amount=1&reward_item=adiantar&timestamp=1761300000000&transaction_id=abc&user_id=11111111-2222-3333-4444-555555555555";
  const raw = new Uint8Array(await crypto.subtle.sign({ name: "ECDSA", hash: "SHA-256" }, pair.privateKey, new TextEncoder().encode(content)));
  assertEquals(derToRaw(rawToDer(raw)), raw);
  const keys = new Map([["42", pair.publicKey]]);
  const query = `${content}&signature=${b64url(rawToDer(raw))}&key_id=42`;
  assertEquals(await verifyRewardCall(query, keys), true);
  // Trocar a conta (ou qualquer campo) quebra a assinatura.
  assertEquals(await verifyRewardCall(query.replace("user_id=1111", "user_id=9999"), keys), false);
  assertEquals(await verifyRewardCall(query.replace("key_id=42", "key_id=7"), keys), false);
  assertEquals(await verifyRewardCall(content, keys), false);
});

Deno.test("anúncio premiado: só o bloco do Estudário vira crédito", () => {
  assertEquals(isOurAdUnit("9507247864", "9507247864"), true);
  assertEquals(isOurAdUnit("ca-app-pub-6794476146182030/9507247864", "9507247864"), true);
  assertEquals(isOurAdUnit("1234567890", "9507247864"), false);
  assertEquals(isOurAdUnit(null, "9507247864"), false);
});
