import { assertEquals } from "https://deno.land/std@0.224.0/assert/mod.ts";
import { parseRoute, routeTopic } from "./topic-router.ts";
import { assembleFromLibrary, composeMaterials } from "./library.ts";
import type { OpenAiProvider, ProviderResponse } from "./openai-provider.ts";

const catalog = [
  { id: "portugues.crase", title: "Crase", subject: "Língua Portuguesa", covers: "acento grave" },
  { id: "portugues.acentuacao", title: "Acentuação gráfica", subject: "Língua Portuguesa", covers: "acentos" },
];

Deno.test("rota: aceita ids do catálogo e descarta inventados", () => {
  assertEquals(parseRoute(JSON.stringify({ topicIds: ["portugues.crase"], coverage: "FULL", reason: "" }), catalog), { topicIds: ["portugues.crase"], coverage: "FULL" });
  assertEquals(parseRoute(JSON.stringify({ topicIds: ["portugues.inventada"], coverage: "FULL", reason: "" }), catalog), { topicIds: [], coverage: "NONE" });
  assertEquals(parseRoute(JSON.stringify({ topicIds: [], coverage: "FULL", reason: "" }), catalog), { topicIds: [], coverage: "NONE" });
  assertEquals(parseRoute("não é json", catalog), null);
});

Deno.test("rota: chama a IA uma vez e devolve a escolha", async () => {
  const done: ProviderResponse = { id: "r", status: "completed", outputText: JSON.stringify({ topicIds: ["portugues.acentuacao", "portugues.crase"], coverage: "FULL", reason: "" }), usage: null };
  const provider = { start: () => Promise.resolve(done), retrieve: () => Promise.resolve(done), cancel: () => Promise.resolve(done) } as unknown as OpenAiProvider;
  const route = await routeTopic({ provider, jobId: "j", model: "m", subject: "Português", topic: "Acentuação e crase", catalog });
  assertEquals(route, { topicIds: ["portugues.acentuacao", "portugues.crase"], coverage: "FULL" });
});

Deno.test("matérias juntas cabem no formato do app (até 6 capítulos e 6 conceitos)", () => {
  const load = (name: string) => JSON.parse(Deno.readTextFileSync(new URL(`../../../conteudo/materias/portugues/${name}.json`, import.meta.url)));
  const options = { depth: "BOOK", blocks: ["THEORY", "SUMMARY", "QUICK_REVIEW", "TIPS_TRAPS", "ACTIVE_RECALL", "QUESTIONS", "ERROR_CONCEPTS"], difficulty: "MIXED", questionCount: 20, questionStyle: "MIXED" };
  for (const names of [["ortografia", "acentuacao"], ["ortografia", "acentuacao", "crase"]]) {
    const material = composeMaterials(names.map(load), "Ortografia, acentuação e crase");
    const out = assembleFromLibrary(material, null, { board: null, options, avoidStatements: [] } as never, "s");
    assertEquals(out !== null, true, names.join("+"));
    assertEquals((out!.chapters as unknown[]).length <= 6, true);
  }
});
