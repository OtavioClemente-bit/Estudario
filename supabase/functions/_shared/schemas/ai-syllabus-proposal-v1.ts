import { getAiSyllabusProposalSchema } from "../schema.ts";

export const AI_SYLLABUS_PROPOSAL_SCHEMA_VERSION = 1 as const;
export const AI_SYLLABUS_PROPOSAL_SCHEMA_NAME = "ai_syllabus_proposal_v1" as const;
export const AI_SYLLABUS_PROPOSAL_SCHEMA = getAiSyllabusProposalSchema(AI_SYLLABUS_PROPOSAL_SCHEMA_VERSION);
