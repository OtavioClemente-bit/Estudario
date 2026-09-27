import {
  assert,
  assertEquals,
  assertRejects,
} from "https://deno.land/std@0.224.0/assert/mod.ts";
import {
  ProposalValidationError,
  validateAiSyllabusProposal,
} from "./proposal-validator.ts";

const fixtureUrl = new URL(
  "./fixtures/v1/ai-syllabus-proposal.json",
  import.meta.url,
);

Deno.test("accepts valid structured output", async () => {
  const proposal = await validateAiSyllabusProposal(
    await Deno.readTextFile(fixtureUrl),
  );
  assertEquals(proposal.schemaVersion, 1);
  assertEquals(proposal.subjects[0].topics[0].sourcePages, [42, 43]);
});

Deno.test("rejects schema mismatch and empty output", async () => {
  await assertRejects(
    () => validateAiSyllabusProposal(JSON.stringify({ schemaVersion: 99 })),
    ProposalValidationError,
    "SCHEMA_MISMATCH",
  );
  await assertRejects(
    () => validateAiSyllabusProposal("  "),
    ProposalValidationError,
    "EMPTY_OUTPUT",
  );
});

Deno.test("rejects duplicate sibling positions even when the JSON shape is otherwise valid", async () => {
  const proposal = JSON.parse(await Deno.readTextFile(fixtureUrl));
  proposal.subjects[0].topics.push({
    ...proposal.subjects[0].topics[0],
    name: "Duplicada",
  });
  await assertRejects(
    () => validateAiSyllabusProposal(JSON.stringify(proposal)),
    ProposalValidationError,
    "SCHEMA_MISMATCH",
  );
});

Deno.test("reports only a safe field marker for each version mismatch", async () => {
  const expected = {
    schemaVersion: 1,
    promptVersion: "syllabus-v1",
    modelVersion: "gpt-6-luna",
  };
  const cases = [
    { field: "schemaVersion", value: 91, marker: "VERSION_MISMATCH_SCHEMA" },
    {
      field: "promptVersion",
      value: "PROVIDER_PROMPT_VALUE_SENTINEL",
      marker: "VERSION_MISMATCH_PROMPT",
    },
    {
      field: "modelVersion",
      value: "PROVIDER_MODEL_VALUE_SENTINEL",
      marker: "VERSION_MISMATCH_MODEL",
    },
  ] as const;

  for (const { field, value, marker } of cases) {
    const proposal = JSON.parse(await Deno.readTextFile(fixtureUrl));
    proposal[field] = value;
    const error = await assertRejects(
      () => validateAiSyllabusProposal(JSON.stringify(proposal), { expected }),
      ProposalValidationError,
      "VERSION_MISMATCH",
    );
    assertEquals(error.code, "VERSION_MISMATCH");
    assertEquals(error.diagnosticCodes, [marker]);
    assert(!error.message.includes(String(value)));
  }
});
