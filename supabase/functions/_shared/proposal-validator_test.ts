import { assertEquals, assertRejects } from "https://deno.land/std@0.224.0/assert/mod.ts";
import { validateAiSyllabusProposal, ProposalValidationError } from "./proposal-validator.ts";

const fixtureUrl = new URL("./fixtures/v1/ai-syllabus-proposal.json", import.meta.url);

Deno.test("accepts valid structured output", async () => {
  const proposal = await validateAiSyllabusProposal(await Deno.readTextFile(fixtureUrl));
  assertEquals(proposal.schemaVersion, 1);
  assertEquals(proposal.subjects[0].topics[0].sourcePages, [42, 43]);
});

Deno.test("rejects schema mismatch and empty output", async () => {
  await assertRejects(
    () => validateAiSyllabusProposal(JSON.stringify({ schemaVersion: 99 })),
    ProposalValidationError,
    "SCHEMA_MISMATCH",
  );
  await assertRejects(() => validateAiSyllabusProposal("  "), ProposalValidationError, "EMPTY_OUTPUT");
});

Deno.test("rejects duplicate sibling positions even when the JSON shape is otherwise valid", async () => {
  const proposal = JSON.parse(await Deno.readTextFile(fixtureUrl));
  proposal.subjects[0].topics.push({ ...proposal.subjects[0].topics[0], name: "Duplicada" });
  await assertRejects(
    () => validateAiSyllabusProposal(JSON.stringify(proposal)),
    ProposalValidationError,
    "SCHEMA_MISMATCH",
  );
});
