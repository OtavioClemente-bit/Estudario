import {
  authorizeWorkerRequest,
  environmentNumber,
  runSyllabusWorker,
  runtimeEnvironment,
  SupabaseSyllabusWorkerStore,
  TEXT_QUEUE,
} from "../ai-syllabus-worker/index.ts";
import { createOpenAiProvider, resolveOpenAiModel } from "../_shared/openai-provider.ts";
import { textSpecFor } from "../_shared/text-job-specs.ts";

// Worker de conteúdo de tópico e plano de estudo. Mesmo ciclo endurecido do edital (lease,
// conciliação com o provedor, finalização que devolve a cota em falha), consumindo só a fila de
// texto. Chamado pelo agendador com o token do worker, igual ao do edital.

function modelFor(feature: string | undefined): string {
  const specific = feature === "CONTENT_GENERATION"
    ? Deno.env.get("CONTENT_AI_MODEL")
    : feature === "PLAN_GENERATION"
    ? Deno.env.get("PLAN_AI_MODEL")
    : feature === "SIMULATION_GENERATION"
    ? Deno.env.get("SIMULATION_AI_MODEL")
    : undefined;
  return resolveOpenAiModel(specific?.trim() || undefined);
}

if (import.meta.main) {
  Deno.serve(async (request) => {
    if (request.method !== "POST") {
      return new Response(null, { status: 405, headers: { allow: "POST" } });
    }
    try {
      const environment = runtimeEnvironment();
      if (!authorizeWorkerRequest(request, environment.endpointAuthToken)) {
        return Response.json({
          error: { code: "AI_WORKER_UNAUTHORIZED", message: "AI worker authorization required" },
        }, { status: 401 });
      }
      const store = new SupabaseSyllabusWorkerStore(environment, null, `text-worker:${crypto.randomUUID()}`, TEXT_QUEUE);
      const processed = await runSyllabusWorker({
        jobs: store,
        provider: createOpenAiProvider({
          background: true,
          // Teto por recurso: conteúdo é texto longo; o plano, compacto.
          maxOutputTokens: environmentNumber("TEXT_MAX_OUTPUT_TOKENS", 24_000),
          timeoutMs: environmentNumber("OPENAI_TIMEOUT_MS", 30_000),
          store: true,
        }),
        source: () => Promise.reject(new Error("SOURCE_NOT_FOUND")),
        specForJob: textSpecFor,
        modelForJob: (job) => modelFor(job.feature),
        leaseSeconds: environmentNumber("AI_WORKER_LEASE_SECONDS", 300),
        maxRetries: environmentNumber("AI_MAX_RETRIES", 3),
        maxOutputTokens: environmentNumber("TEXT_MAX_OUTPUT_TOKENS", 24_000),
        maxProcessingSeconds: environmentNumber("TEXT_MAX_PROCESSING_SECONDS", 900),
      }, environmentNumber("AI_WORKER_BATCH_SIZE", 1));
      return Response.json({ processed }, { headers: { "cache-control": "no-store" } });
    } catch {
      return Response.json({
        error: { code: "AI_WORKER_UNAVAILABLE", message: "AI worker is unavailable" },
      }, { status: 503 });
    }
  });
}
