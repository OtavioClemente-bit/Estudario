import {
  assertEquals,
  assertThrows,
} from "https://deno.land/std@0.224.0/assert/mod.ts";
import {
  ContractValidationError,
  CURRENT_AI_SCHEMA_VERSION,
  parseProviderAiSyllabusProposal,
} from "./contracts.ts";
import { getAiSyllabusProposalSchema } from "./schema.ts";

Deno.test("OpenAI schema omits uniqueItems at every depth", () => {
  const schema = getAiSyllabusProposalSchema(1);
  const containsKey = (value: unknown, key: string): boolean => {
    if (Array.isArray(value)) {
      return value.some((item) => containsKey(item, key));
    }
    if (value === null || typeof value !== "object") return false;
    return Object.entries(value).some(([name, child]) =>
      name === key || containsKey(child, key)
    );
  };

  assertEquals(containsKey(schema, "uniqueItems"), false);
});

Deno.test("schemaVersion remains a single supported integer and parser rejects other versions", async () => {
  const schema = getAiSyllabusProposalSchema(1);
  const properties = schema.properties as Record<string, any>;
  assertEquals(properties.schemaVersion.type, "integer");
  assertEquals(properties.schemaVersion.enum, [CURRENT_AI_SCHEMA_VERSION]);

  const raw = await Deno.readTextFile(
    new URL("./fixtures/v1/ai-syllabus-proposal.json", import.meta.url),
  );
  const unsupported = JSON.parse(raw);
  unsupported.schemaVersion = CURRENT_AI_SCHEMA_VERSION + 1;
  assertThrows(
    () => parseProviderAiSyllabusProposal(JSON.stringify(unsupported)),
    ContractValidationError,
    "schemaVersion",
  );
});

Deno.test("sourcePages retains non-empty positive integer constraints and shared references", () => {
  const schema = getAiSyllabusProposalSchema(1);
  const defs = schema.$defs as Record<string, any>;
  const sourcePages = defs.sourcePages;
  const root = schema.properties as Record<string, any>;
  const subject = root.subjects.items.properties as Record<string, any>;
  const topic = defs.topic.properties as Record<string, any>;
  const warning = defs.warning.properties as Record<string, any>;

  assertEquals(sourcePages.type, "array");
  assertEquals(sourcePages.minItems, 1);
  assertEquals(sourcePages.items.type, "integer");
  assertEquals(sourcePages.items.minimum, 1);
  for (
    const field of [subject.sourcePages, topic.sourcePages, warning.sourcePages]
  ) {
    assertEquals(field, { $ref: "#/$defs/sourcePages" });
  }
});

Deno.test("local proposal parser rejects duplicate source pages without changing DTO keys", async () => {
  const raw = await Deno.readTextFile(
    new URL("./fixtures/v1/ai-syllabus-proposal.json", import.meta.url),
  );
  const duplicate = JSON.parse(raw);
  duplicate.subjects[0].sourcePages = [42, 42];

  assertThrows(
    () => parseProviderAiSyllabusProposal(JSON.stringify(duplicate)),
    ContractValidationError,
    "duplicate pages",
  );

  const proposal = parseProviderAiSyllabusProposal(raw);
  assertEquals(Object.keys(proposal).sort(), [
    "ambiguities",
    "documentTitle",
    "modelVersion",
    "promptVersion",
    "schemaVersion",
    "subjects",
    "warnings",
  ]);
  assertEquals(Object.keys(proposal.subjects[0]).sort(), [
    "name",
    "position",
    "sourcePages",
    "suggestedPriority",
    "topics",
  ]);
});
