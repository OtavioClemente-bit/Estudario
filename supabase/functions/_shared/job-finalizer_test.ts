import { assertEquals, assertNotEquals } from "https://deno.land/std@0.224.0/assert/mod.ts";
import { requestFingerprint } from "./job-finalizer.ts";

Deno.test("request fingerprint canonicalizes nested objects and preserves nested values", async () => {
  const first = await requestFingerprint({ feature: "SYLLABUS_GENERATION", target: { title: "TRT-3 TI", nested: { role: "Técnico" } } });
  const reordered = await requestFingerprint({ target: { nested: { role: "Técnico" }, title: "TRT-3 TI" }, feature: "SYLLABUS_GENERATION" });
  const differentTarget = await requestFingerprint({ feature: "SYLLABUS_GENERATION", target: { title: "TRT-3 Administrativo", nested: { role: "Técnico" } } });
  assertEquals(first, reordered);
  assertNotEquals(first, differentTarget);
});
