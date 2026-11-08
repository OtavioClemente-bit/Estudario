// Diagnóstico (só leitura): tarefas do plano atrasadas na foto da conta e se o mesmo tópico já foi concluído em outra tarefa.
// Uso: bash scripts/biblioteca/com-chave.sh scripts/biblioteca/diag-atrasadas.ts <email>
const url = Deno.env.get("SUPABASE_URL")!;
const key = Deno.env.get("SUPABASE_SERVICE_ROLE_KEY")!;
const h = { apikey: key, Authorization: `Bearer ${key}` };
const email = Deno.args[0];
let uid = "";
for (let page = 1; page < 50 && !uid; page++) {
  const r = await fetch(`${url}/auth/v1/admin/users?page=${page}&per_page=200`, { headers: h });
  const j = await r.json();
  const u = (j.users ?? []).find((x: { email?: string }) => x.email === email);
  if (u) uid = u.id;
  if ((j.users ?? []).length < 200) break;
}
if (!uid) { console.log("usuário não achado"); Deno.exit(1); }
const heads = await (await fetch(`${url}/rest/v1/sync_heads?user_id=eq.${uid}&select=*`, { headers: h })).json();
const head = heads[0];
console.log("head keys:", Object.keys(head ?? {}).join(","), "updated:", head?.updated_at);
const path = head.object_path ?? head.objectPath;
const obj = await fetch(`${url}/storage/v1/object/authenticated/user-sync/${path}`, { headers: h });
const snap = await obj.json();
const plans = snap.studyPlans ?? [];
const active = plans.find((p: { status?: string; isActive?: boolean }) => p.isActive || p.status === "ATIVO") ?? plans[0];
console.log("planos:", plans.length, "ativo:", active?.id, active?.status);
console.log("task keys:", Object.keys((snap.planTasks ?? [])[0] ?? {}).join(","));
const tasks = (snap.planTasks ?? []).filter((t: { planId: string }) => t.planId === active?.id);
const today = Math.floor((Date.now() - 3 * 3600_000) / 86_400_000);
const late = tasks.filter((t: { status: string; day: number }) => ["PLANEJADA", "EM_ANDAMENTO"].includes(t.status) && t.day < today);
console.log("tarefas do plano:", tasks.length, "atrasadas:", late.length);
const byStatus: Record<string, number> = {};
for (const t of tasks) byStatus[t.status] = (byStatus[t.status] ?? 0) + 1;
console.log("por status:", JSON.stringify(byStatus));
for (const t of late.slice(0, 15)) {
  const same = tasks.filter((o: { id: string; topicId: number; type: string }) => o.id !== t.id && o.topicId === t.topicId && o.type === t.type);
  console.log(`- dia ${t.day - today} ${t.type} "${t.topicName}" upd=${t.updatedAt} | mesmo tópico: ${same.map((o: { status: string; day: number }) => o.status + "@" + (o.day - today)).join(" ")}`);
}
// sessões de estudo/registro de conclusão para esses tópicos
const logs = snap.planTaskLogs ?? snap.taskExecutions ?? [];
console.log("tabelas:", Object.keys(snap).filter((k) => Array.isArray(snap[k])).map((k) => `${k}:${snap[k].length}`).join(" "));
console.log("head device:", head.device_label, head.device_id);
const ex = snap.studyTaskExecutions ?? [];
const exByTask = new Map<string, unknown[]>();
for (const e of ex) { const k = e.taskId ?? e.planTaskId; if (!exByTask.has(k)) exByTask.set(k, []); exByTask.get(k)!.push(e); }
const nr = tasks.filter((t: { status: string }) => t.status === "NAO_REALIZADA");
for (const t of nr) {
  const same = tasks.filter((o: { id: string; topicId: number; type: string; status: string }) => o.id !== t.id && o.topicId === t.topicId && o.type === t.type && o.status !== "REPROGRAMADA");
  console.log(`NR dia ${t.day - today} ${t.type} "${t.topicName}" upd=${new Date(t.updatedAt).toISOString()} exec=${(exByTask.get(t.id) ?? []).length} | outras: ${same.map((o: { status: string; day: number }) => o.status + "@" + (o.day - today)).join(" ")}`);
}
const recentDone = tasks.filter((t: { status: string }) => t.status === "CONCLUIDA").sort((a: { updatedAt: number }, b: { updatedAt: number }) => b.updatedAt - a.updatedAt).slice(0, 8);
for (const t of recentDone) console.log(`OK dia ${t.day - today} ${t.type} "${t.topicName}" upd=${new Date(t.updatedAt).toISOString()}`);
console.log("exemplo exec:", JSON.stringify(ex[ex.length - 1]).slice(0, 300));
