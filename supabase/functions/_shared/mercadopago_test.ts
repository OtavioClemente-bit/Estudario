import { assertEquals } from "https://deno.land/std@0.224.0/assert/mod.ts";
import { addPeriod, parseReference, reference } from "./mercadopago.ts";

const user = "1f75cd98-6341-4cc2-b606-e8ecd53a6c8a";

Deno.test("referência ida e volta", () => {
  assertEquals(parseReference(reference("sub", user, "PRO", "YEAR")), { kind: "sub", userId: user, tier: "PRO", period: "YEAR" });
  assertEquals(parseReference(reference("once", user, "ESSENCIAL", "MONTH"))?.kind, "once");
});

Deno.test("referência estranha não vale", () => {
  assertEquals(parseReference("sub|x|PRO|YEAR"), null);
  assertEquals(parseReference(`sub|${user}|TESTE|YEAR`), null);
  assertEquals(parseReference(`sub|${user}|PRO|DAY`), null);
  assertEquals(parseReference(null), null);
});

Deno.test("período soma mês e ano", () => {
  assertEquals(addPeriod(new Date("2026-11-23T10:00:00Z"), "MONTH").toISOString(), "2026-12-23T10:00:00.000Z");
  assertEquals(addPeriod(new Date("2026-11-23T10:00:00Z"), "YEAR").toISOString(), "2027-11-23T10:00:00.000Z");
});
