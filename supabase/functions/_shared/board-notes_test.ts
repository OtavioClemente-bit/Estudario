import { assert, assertEquals } from "https://deno.land/std@0.224.0/assert/mod.ts";
import { ensureBoardNote } from "./board-notes.ts";
import type { OpenAiProvider, ProviderResponse, ProviderStartInput } from "./openai-provider.ts";

type Json = Record<string, unknown>;
const material = JSON.parse(
  Deno.readTextFileSync(new URL("../../../conteudo/materias/portugues/crase.json", import.meta.url)),
) as Json;

const profile = {
  board: "PMMG",
  overview: "Banca própria da Polícia Militar de Minas Gerais. ".repeat(6),
  questionStyle: "Questões objetivas de quatro alternativas, enunciados curtos e diretos. ".repeat(2),
  bySubject: [{ subject: "Língua Portuguesa", notes: "Gramática normativa em frases isoladas, com crase e regência frequentes." }],
  traps: ["Alternativas que trocam um único acento grave."],
  sources: [{ title: "Prova CFSd 2023", url: "https://exemplo.gov.br/prova" }],
};
const note = {
  howItFalls: "A banca cobra crase em frases curtas do cotidiano policial, pedindo a alternativa correta ou a incorreta. ".repeat(4),
  incidence: "ALTA",
  tips: ["Comece pela regência do termo.", "Teste com palavra masculina.", "Desconfie de crase antes de verbo."],
  traps: ["Crase antes de pronome de tratamento.", "Horas com desde e após.", "Topônimos sem artigo."],
};

function fakeProvider(outputs: Json[]): OpenAiProvider & { calls: string[] } {
  const calls: string[] = [];
  let index = 0;
  const done = (): Promise<ProviderResponse> =>
    Promise.resolve({ id: `r${index}`, status: "completed", outputText: JSON.stringify(outputs[index++]), usage: null });
  return {
    calls,
    start(input: ProviderStartInput) {
      calls.push(String(input.feature));
      return done();
    },
    retrieve: done,
    cancel: done,
  } as unknown as OpenAiProvider & { calls: string[] };
}

function memoryStore(saved: Json | null = null) {
  const state = { profile: saved, notes: [] as Json[] };
  return {
    state,
    boardProfile: () => Promise.resolve(state.profile),
    saveBoardProfile: (_n: string, _b: string, p: Json) => {
      state.profile = p;
      return Promise.resolve();
    },
    saveBoardNote: (_t: string, _n: string, _b: string, n: Json) => {
      state.notes.push(n);
      return Promise.resolve();
    },
  };
}

const base = { jobId: "j", model: "m", board: "PMMG", boardNorm: "pmmg", topicId: "portugues.crase", material, sleep: () => Promise.resolve() };

Deno.test("banca nova: pesquisa o perfil, escreve o recorte e guarda os dois", async () => {
  const provider = fakeProvider([profile, note]);
  const store = memoryStore();
  const result = await ensureBoardNote({ ...base, provider, store });
  assertEquals(provider.calls, ["BOARD_PROFILE", "BOARD_NOTE"]);
  assertEquals(result?.topic, "portugues.crase");
  assertEquals(result?.board, "PMMG");
  assertEquals(store.state.profile, profile);
  assertEquals(store.state.notes.length, 1);
});

Deno.test("banca com perfil guardado: só escreve o recorte, sem pesquisar de novo", async () => {
  const provider = fakeProvider([note]);
  const store = memoryStore(profile);
  const result = await ensureBoardNote({ ...base, provider, store });
  assertEquals(provider.calls, ["BOARD_NOTE"]);
  assert(result !== null);
});

Deno.test("recorte fora do formato não é guardado nem entregue", async () => {
  const provider = fakeProvider([{ ...note, howItFalls: "curto" }]);
  const store = memoryStore(profile);
  assertEquals(await ensureBoardNote({ ...base, provider, store }), null);
  assertEquals(store.state.notes.length, 0);
});
