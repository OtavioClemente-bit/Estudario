import { assertEquals } from "https://deno.land/std@0.224.0/assert/mod.ts";
import { overSavedEditais } from "./saved-editais.ts";

Deno.test("limite de editais: só bloqueia quem passou do limite do plano", () => {
  assertEquals(overSavedEditais(null), false); // sem cópia na nuvem: não bloqueia
  assertEquals(overSavedEditais({ count: 5, limit: 5 }), false); // no limite: ok
  assertEquals(overSavedEditais({ count: 6, limit: 5 }), true); // forçou um a mais: bloqueia
  assertEquals(overSavedEditais({ count: 40, limit: 1000 }), false);
});
