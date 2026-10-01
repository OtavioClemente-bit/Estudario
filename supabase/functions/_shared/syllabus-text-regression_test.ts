import {
  assertEquals,
  assertThrows,
} from "https://deno.land/std@0.224.0/assert/mod.ts";
import {
  ContractValidationError,
  parseProviderAiSyllabusProposal,
} from "./contracts.ts";
import { getAiSyllabusProposalSchema } from "./schema.ts";
import { parseContentJobInput } from "./text-job-input.ts";
import {
  CONTENT_SYSTEM_PROMPT,
  contentUserPrompt,
} from "./prompts/text-jobs-v1.ts";

const literal = (await Deno.readTextFile(
  new URL("./fixtures/v1/long-topic-name.txt", import.meta.url),
)).trimEnd();
const base = JSON.parse(
  await Deno.readTextFile(
    new URL("./fixtures/v1/ai-syllabus-proposal.json", import.meta.url),
  ),
);

Deno.test("literal IA ML parent survives provider parsing and serialization", () => {
  assertEquals(literal.length, 430);
  assertEquals(new TextEncoder().encode(literal).length, 446);
  const value = structuredClone(base);
  value.subjects[0].topics[0].name = literal;
  const parsed = parseProviderAiSyllabusProposal(JSON.stringify(value));
  assertEquals(parsed.subjects[0].topics[0].name, literal);
  assertEquals(parseProviderAiSyllabusProposal(JSON.stringify(parsed)), parsed);
});

Deno.test("proposal text uses semantic ceilings and rejects unsafe controls", () => {
  for (const name of ["", "bad\u0000text", "bad\u0085text", "x".repeat(4001)]) {
    const value = structuredClone(base);
    value.subjects[0].topics[0].name = name;
    assertThrows(
      () => parseProviderAiSyllabusProposal(JSON.stringify(value)),
      ContractValidationError,
    );
  }
  const value = structuredClone(base);
  value.subjects[0].topics[0].name = "x".repeat(4000);
  value.documentTitle = "d".repeat(4000);
  value.warnings[0].message = "m".repeat(8000);
  value.warnings[0].ambiguity = "a".repeat(8000);
  parseProviderAiSyllabusProposal(JSON.stringify(value));
  for (
    const change of [
      (v: typeof value) => v.subjects[0].name = "s".repeat(201),
      (v: typeof value) => v.warnings[0].message = "m".repeat(8001),
      (v: typeof value) => v.warnings[0].ambiguity = "a\u0000b",
    ]
  ) {
    const invalid = structuredClone(value);
    change(invalid);
    assertThrows(
      () => parseProviderAiSyllabusProposal(JSON.stringify(invalid)),
      ContractValidationError,
    );
  }
  const schema = getAiSyllabusProposalSchema(1);
  const defs = schema.$defs as Record<
    string,
    { properties: Record<string, { maxLength?: number }> }
  >;
  assertEquals(defs.topic.properties.name.maxLength, 4000);
  assertEquals(defs.warning.properties.message.maxLength, 8000);
});

Deno.test("subsequent child content keeps a long parent as context and child last", () => {
  const parent = "x".repeat(4000);
  const result = parseContentJobInput({
    competitionName: "TRT",
    subjectName: "TI",
    topicPath: [parent, "Modelos preditivos"],
  });
  assertEquals(result.topicPath, [parent, "Modelos preditivos"]);
  assertEquals(
    contentUserPrompt(result).includes(
      "Tópico selecionado (escopo): Modelos preditivos",
    ),
    true,
  );
  assertEquals(
    CONTENT_SYSTEM_PROMPT.includes("os ancestrais fornecem somente contexto"),
    true,
  );
  assertThrows(() =>
    parseContentJobInput({
      competitionName: "TRT",
      subjectName: "TI",
      topicPath: [parent + "x", "Modelos"],
    })
  );
});
