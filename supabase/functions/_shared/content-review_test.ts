import { assert, assertEquals } from "https://deno.land/std@0.224.0/assert/mod.ts";
import { applyReview, parseReview, REVIEW_PROMPT_VERSION, REVIEW_SCHEMA_VERSION, runContentReview } from "./content-review.ts";
import type { OpenAiProvider, ProviderResponse } from "./openai-provider.ts";

const content = () => ({
  schemaVersion: 4,
  promptVersion: "p",
  modelVersion: "m",
  sources: [{ title: "Lei 8.112, art. 15", url: "https://planalto.gov.br" }],
  chapters: [{ title: "1. Posse", markdown: "A posse ocorre em até 15 dias da nomeação (art. 13)." }],
  flashcards: [{ front: "Prazo da posse?", back: "**15 dias**" }, { front: "Errado", back: "x" }],
  questions: [{
    statement: "Prazo para posse?",
    options: [{ key: "A", text: "30 dias", correct: true }, { key: "B", text: "15 dias", correct: false }],
    explanation: "São 15 dias.",
  }, { statement: "Ambígua", options: [], explanation: "?" }],
});

Deno.test("fixes replace text everywhere except sources", () => {
  const { content: out, applied } = applyReview(content(), {
    fixes: [{ wrong: "15 dias", correct: "30 dias", reason: "art. 13 §1º" }, { wrong: "não existe", correct: "x", reason: "" }],
    answerFixes: [],
    removeQuestions: [],
    removeFlashcards: [],
  });
  assertEquals(applied, 1);
  assert(JSON.stringify(out.chapters).includes("30 dias"));
  assertEquals((out.sources as { title: string }[])[0].title, "Lei 8.112, art. 15");
});

Deno.test("answer fixes, removals and bad keys", () => {
  const { content: out } = applyReview(content(), {
    fixes: [],
    answerFixes: [{ question: 0, correctKey: "B", reason: "" }, { question: 0, correctKey: "E", reason: "no such option" }],
    removeQuestions: [1],
    removeFlashcards: [1],
  });
  const questions = out.questions as { options: { key: string; correct: boolean }[] }[];
  assertEquals(questions.length, 1);
  assertEquals(questions[0].options.map((o) => o.correct), [false, true]);
  assertEquals((out.flashcards as unknown[]).length, 1);
});

Deno.test("parseReview rejects other versions and junk", () => {
  assertEquals(parseReview("not json", { promptVersion: REVIEW_PROMPT_VERSION, schemaVersion: REVIEW_SCHEMA_VERSION }), null);
  assertEquals(parseReview(JSON.stringify({ promptVersion: "x", schemaVersion: 1 }), { promptVersion: REVIEW_PROMPT_VERSION, schemaVersion: 1 }), null);
  const ok = parseReview(
    JSON.stringify({ promptVersion: REVIEW_PROMPT_VERSION, schemaVersion: 1, fixes: [{ wrong: "a b c", correct: "d", reason: "r" }, 3], answerFixes: [], removeQuestions: [1, "x"], removeFlashcards: [] }),
    { promptVersion: REVIEW_PROMPT_VERSION, schemaVersion: 1 },
  );
  assertEquals(ok?.fixes.length, 1);
  assertEquals(ok?.removeQuestions, [1]);
});

function fakeProvider(responses: ProviderResponse[]): OpenAiProvider & { cancelled: boolean } {
  let index = 0;
  const next = () => Promise.resolve(responses[Math.min(index++, responses.length - 1)]);
  return { cancelled: false, start: next, retrieve: next, cancel(this: { cancelled: boolean }) { this.cancelled = true; return next(); } };
}

Deno.test("review polls until completed", async () => {
  const output = JSON.stringify({ promptVersion: REVIEW_PROMPT_VERSION, schemaVersion: 1, fixes: [], answerFixes: [], removeQuestions: [], removeFlashcards: [] });
  const provider = fakeProvider([
    { id: "r", status: "in_progress", outputText: null, usage: null },
    { id: "r", status: "completed", outputText: output, usage: null },
  ]);
  const review = await runContentReview(content(), { provider, jobId: "j", model: "m", context: "c", sleep: () => Promise.resolve() });
  assertEquals(review?.fixes, []);
});

Deno.test("review gives up after the budget and cancels", async () => {
  let clock = 0;
  const provider = fakeProvider([{ id: "r", status: "in_progress", outputText: null, usage: null }]);
  const review = await runContentReview(content(), {
    provider, jobId: "j", model: "m", context: "c", budgetMs: 10,
    sleep: () => { clock += 20; return Promise.resolve(); }, now: () => clock,
  });
  assertEquals(review, null);
  assert(provider.cancelled);
});

Deno.test("review errors never throw", async () => {
  const provider: OpenAiProvider = { start: () => Promise.reject(new Error("boom")), retrieve: () => Promise.reject(), cancel: () => Promise.reject() };
  assertEquals(await runContentReview(content(), { provider, jobId: "j", model: "m", context: "c" }), null);
});

Deno.test("retry reuses the finished review instead of paying for a new one", async () => {
  const output = JSON.stringify({ promptVersion: REVIEW_PROMPT_VERSION, schemaVersion: 1, fixes: [], answerFixes: [], removeQuestions: [], removeFlashcards: [] });
  let started = 0;
  const provider: OpenAiProvider = {
    start: () => { started++; return Promise.reject(new Error("não devia começar")); },
    retrieve: (id) => Promise.resolve({ id, status: "completed", outputText: output, usage: null }),
    cancel: () => Promise.reject(),
  };
  const review = await runContentReview(content(), { provider, jobId: "j", model: "m", context: "c", previousReviewId: () => Promise.resolve("r-antiga") });
  assertEquals(review?.fixes, []);
  assertEquals(started, 0);
});

Deno.test("reserve questions fill the gap left by removed ones", async () => {
  const { keepRequestedQuestions } = await import("./text-job-specs.ts");
  const content = { questions: [1, 2, 3, 4, 5, 6] };
  assertEquals((keepRequestedQuestions(content, 4).questions as number[]), [1, 2, 3, 4]);
  // O revisor tirou duas: as reservas entram e nada é cortado.
  assertEquals((keepRequestedQuestions({ questions: [1, 3, 5, 6] }, 4).questions as number[]), [1, 3, 5, 6]);
  assertEquals(keepRequestedQuestions({ questions: [] }, 0).questions, []);
});
