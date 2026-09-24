import { assertRejects } from "https://deno.land/std@0.224.0/assert/mod.ts";
import { validateAiSyllabusProposal, ProposalValidationError } from "./proposal-validator.ts";

Deno.test("rejects output whose schema, prompt, or model version differs from the effective worker versions", async () => {
  const proposal = JSON.parse(await Deno.readTextFile(new URL("./fixtures/v1/ai-syllabus-proposal.json", import.meta.url)));
  await assertRejects(
    () => validateAiSyllabusProposal(JSON.stringify({ ...proposal, schemaVersion: 2 }), {
      expected: { schemaVersion: 1, promptVersion: "syllabus-v1", modelVersion: "gpt-6-luna" },
    }),
    ProposalValidationError,
    "VERSION_MISMATCH",
  );
  await assertRejects(
    () => validateAiSyllabusProposal(JSON.stringify({ ...proposal, promptVersion: "syllabus-v0" }), {
      expected: { schemaVersion: 1, promptVersion: "syllabus-v1", modelVersion: "gpt-6-luna" },
    }),
    ProposalValidationError,
    "VERSION_MISMATCH",
  );
  await assertRejects(
    () => validateAiSyllabusProposal(JSON.stringify({ ...proposal, modelVersion: "other-model" }), {
      expected: { schemaVersion: 1, promptVersion: "syllabus-v1", modelVersion: "gpt-6-luna" },
    }),
    ProposalValidationError,
    "VERSION_MISMATCH",
  );
});
