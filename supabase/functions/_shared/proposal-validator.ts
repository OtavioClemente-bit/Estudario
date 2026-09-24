import { ContractValidationError, parseProviderAiSyllabusProposal, type AiSyllabusProposal } from "./contracts.ts";

export interface ProposalValidationLimits {
  maxSubjects?: number;
  maxTopics?: number;
  maxTopicDepth?: number;
}

export class ProposalValidationError extends Error {
  constructor(public readonly code: "EMPTY_OUTPUT" | "SCHEMA_MISMATCH" | "OUTPUT_LIMIT_EXCEEDED", message: string) {
    super(`${code}: ${message}`);
    this.name = "ProposalValidationError";
  }
}

function topicStats(topics: AiSyllabusProposal["subjects"][number]["topics"], depth = 1): { count: number; maxDepth: number } {
  return topics.reduce((stats, topic) => {
    const child = topicStats(topic.children, depth + 1);
    return { count: stats.count + 1 + child.count, maxDepth: Math.max(stats.maxDepth, child.maxDepth) };
  }, { count: 0, maxDepth: depth });
}

function enforceLimits(proposal: AiSyllabusProposal, limits: ProposalValidationLimits): void {
  if (limits.maxSubjects !== undefined && proposal.subjects.length > limits.maxSubjects) {
    throw new ProposalValidationError("OUTPUT_LIMIT_EXCEEDED", "subject limit exceeded");
  }
  const stats = proposal.subjects.reduce((total, subject) => {
    const current = topicStats(subject.topics);
    return { count: total.count + current.count, maxDepth: Math.max(total.maxDepth, current.maxDepth) };
  }, { count: 0, maxDepth: 0 });
  if (limits.maxTopics !== undefined && stats.count > limits.maxTopics) {
    throw new ProposalValidationError("OUTPUT_LIMIT_EXCEEDED", "topic limit exceeded");
  }
  if (limits.maxTopicDepth !== undefined && stats.maxDepth > limits.maxTopicDepth) {
    throw new ProposalValidationError("OUTPUT_LIMIT_EXCEEDED", "topic depth limit exceeded");
  }
}

export async function validateAiSyllabusProposal(raw: string, limits: ProposalValidationLimits = {}): Promise<AiSyllabusProposal> {
  if (typeof raw !== "string" || raw.trim().length === 0) throw new ProposalValidationError("EMPTY_OUTPUT", "provider returned no structured output");
  try {
    const proposal = parseProviderAiSyllabusProposal(raw);
    enforceLimits(proposal, limits);
    return proposal;
  } catch (error) {
    if (error instanceof ProposalValidationError) throw error;
    if (error instanceof ContractValidationError || error instanceof SyntaxError) {
      throw new ProposalValidationError("SCHEMA_MISMATCH", "provider output did not match the versioned proposal contract");
    }
    throw new ProposalValidationError("SCHEMA_MISMATCH", "provider output was rejected");
  }
}
