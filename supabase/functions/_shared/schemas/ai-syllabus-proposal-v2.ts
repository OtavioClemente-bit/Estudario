import { getAiSyllabusProposalSchema } from "../schema.ts";

export const AI_SYLLABUS_PROPOSAL_SCHEMA_VERSION = 2 as const;
export const AI_SYLLABUS_PROPOSAL_SCHEMA_NAME = "ai_syllabus_proposal_v2" as const;
export const AI_SYLLABUS_PROPOSAL_SCHEMA = getAiSyllabusProposalSchema(AI_SYLLABUS_PROPOSAL_SCHEMA_VERSION);
