// node docs/media/store-2026-10-25/demo-prf/montar-demo.mjs <saida/demo.json>
// Junta os textos de conteudo.mjs aos campos fixos de cada tabela, no formato do backup do app.
import { writeFileSync } from "node:fs";
import * as c from "./conteudo.mjs";

const t0 = 1791281772359;
const prio = { assessedPriorityScore: 50, assessedPrioritySource: "DEFAULT", assessedPriorityConfidence: 0, assessedPriorityRationale: null, assessedPriorityEvidenceJson: "[]", hasAssessedPriority: false, userPriorityOverride: null };
const day = 86_400_000;
let optId = 1;
const empty = ["theoryMarks", "contentSources", "attempts", "errors", "errorConceptEntries", "reviews", "reviewHistory", "reviewSessions", "queueEvents", "sessions", "focusSessions", "questionSessions", "importPackages", "notes", "tags", "questionTags", "examProfiles", "simulations", "studyPlans", "studyPlanRevisions", "studyAvailability", "studyDayOverrides", "planSubjects", "annualPhases", "monthlyPlans", "weeklyPlans", "annualPhaseSubjects", "annualPhaseTopics", "monthlyPlanSubjects", "monthlyPlanTopics", "planTasks", "planTaskDependencies", "studyTaskExecutions"];
const data = {
  format: "estudario-backup", version: 8,
  competitions: [{ id: 1, name: c.competicao, primary: true, createdAt: t0, externalId: null, ...prio }],
  subjects: c.materias.map(([id, name], i) => ({ id, competitionId: 1, name, position: i, externalId: null, ...prio })),
  topics: c.topicos.map(([id, subjectId, title, done], i) => ({
    id, subjectId, parentTopicId: null, title, description: "", position: c.topicos.filter((t) => t[1] === subjectId).findIndex((t) => t[0] === id),
    status: done ? "ESTUDADO" : "NAO_ESTUDADO", firstStudiedAt: done ? t0 - (20 - i) * day : null, lastStudiedAt: done ? t0 - (20 - i) * day : null, lastReviewedAt: null,
    notes: "", priority: "NORMAL", externalId: null, contentOriginType: "EDITAL", scopeCovers: null, scopeExcludes: null, ...prio,
  })),
  summaries: c.resumos.map((s) => ({ ...s, favorite: false, ownNotes: "", externalId: "prf-resumo-" + s.id, createdAt: t0, updatedAt: t0, kind: "RAPIDO" })),
  snippets: c.dicas.map(([id, topicId, kind, text], i) => ({ id, topicId, kind, text, favorite: false, externalId: "prf-dica-" + id, position: i, createdAt: t0, updatedAt: t0, answer: null })),
  theories: c.teorias.map((t) => ({ ...t, externalId: "prf-teoria-" + t.id, lastReadBlock: -1, createdAt: t0, updatedAt: t0 })),
  questions: c.questoes.map(([id, topicId, statement, explanation, options]) => ({
    id, topicId, externalId: "prf-q-" + id, board: "Cebraspe", agency: "PRF", year: null, difficulty: "MEDIA", source: null, statement, explanation,
    notes: "", tags: "", importedAt: t0, answerCount: 0, correctCount: 0, errorCount: 0, lastAnswer: null, lastAnsweredAt: null, favorite: false,
    questionSourceType: "AUTHORIAL", sourceId: null, sourceUrl: null, normalizedHash: null, reviewAnchor: null, errorConceptExternalId: null, hidden: false, simulationId: null,
    options: options.map(([text, correct], p) => ({ id: optId++, key: options.length === 2 ? (text === "Certo" ? "C" : "E") : "ABCDE"[p], text, correct, position: p })),
  })),
  errorConcepts: [{ id: 1, topicId: 4, title: "Gravíssima não vale 10 pontos", summary: "O fator multiplicador aumenta a multa; a gravíssima continua somando 7 pontos.", favorite: false, externalId: "prf-erro-1", createdAt: t0, updatedAt: t0, errorCount: 0, correctAfterErrorCount: 0, lastErrorAt: null, lastReviewedAt: null, priority: "NORMAL", mastered: false }],
  queue: [{ id: 1, topicId: 7, position: 0, paused: false, enqueuedAt: t0, postponements: 0 }, { id: 2, topicId: 5, position: 1, paused: false, enqueuedAt: t0, postponements: 0 }],
};
for (const k of empty) data[k] = [];
writeFileSync(process.argv[2], JSON.stringify(data, null, 2));
console.log("ok", data.topics.length, "tópicos,", data.questions.length, "questões");
