// Auditoria (só leitura) do plano na foto da conta: revisões do plano, horas por dia x disponibilidade, mudanças entre dias.
// Uso: bash scripts/biblioteca/com-chave.sh scripts/biblioteca/diag-plano.ts <email>
const url = Deno.env.get("SUPABASE_URL")!;
const key = Deno.env.get("SUPABASE_SERVICE_ROLE_KEY")!;
const h = { apikey: key, Authorization: `Bearer ${key}` };
const email = Deno.args[0];
let uid = "";
for (let page = 1; page < 50 && !uid; page++) {
  const j = await (await fetch(`${url}/auth/v1/admin/users?page=${page}&per_page=200`, { headers: h })).json();
  const u = (j.users ?? []).find((x: { email?: string }) => x.email === email);
  if (u) uid = u.id;
  if ((j.users ?? []).length < 200) break;
}
const head = (await (await fetch(`${url}/rest/v1/sync_heads?user_id=eq.${uid}&select=*`, { headers: h })).json())[0];
const snap = await (await fetch(`${url}/storage/v1/object/authenticated/user-sync/${head.object_path}`, { headers: h })).json();
const plan = snap.studyPlans[0];
console.log("plano:", JSON.stringify(Object.fromEntries(Object.entries(plan).filter(([k]) => !/prompt|notes|json/i.test(k)))).slice(0, 900));
console.log("disponibilidade:", JSON.stringify(snap.studyAvailability));
console.log("overrides:", JSON.stringify(snap.studyDayOverrides));
const revs = snap.studyPlanRevisions;
console.log("revisões:", revs.length, "chaves:", Object.keys(revs[0] ?? {}).join(","));
const byDay: Record<string, Record<string, number>> = {};
for (const r of revs) {
  const d = new Date(r.createdAt ?? r.at ?? 0).toISOString().slice(0, 10);
  const why = String(r.reason ?? r.kind ?? r.source ?? r.summary ?? "?").slice(0, 60);
  byDay[d] ??= {}; byDay[d][why] = (byDay[d][why] ?? 0) + 1;
}
for (const [d, m] of Object.entries(byDay).slice(-10)) console.log(d, JSON.stringify(m));
const today = Math.floor((Date.now() - 3 * 3600_000) / 86_400_000);
const live = snap.planTasks.filter((t: { planId: string; status: string }) => t.planId === plan.id && ["PLANEJADA", "EM_ANDAMENTO", "CONCLUIDA"].includes(t.status));
const avail = Object.fromEntries(snap.studyAvailability.map((a: { day: number; minutes?: number; availableMinutes?: number; unavailable?: boolean }) => [a.day, a.unavailable ? 0 : (a.availableMinutes ?? a.minutes ?? 0)]));
console.log("dia | dow | planejado | limite | tipos");
for (let d = today - 3; d < today + 14; d++) {
  const ts = live.filter((t: { day: number }) => t.day === d);
  const dow = ((new Date(d * 86_400_000).getUTCDay() + 6) % 7) + 1;
  const min = ts.reduce((s: number, t: { minutes: number }) => s + t.minutes, 0);
  const tipos: Record<string, number> = {};
  for (const t of ts) tipos[t.type] = (tipos[t.type] ?? 0) + 1;
  console.log(d - today, dow, min, avail[dow], JSON.stringify(tipos));
}
// quantas vezes cada tópico teve tarefa reprogramada (instabilidade)
const rep = snap.planTasks.filter((t: { planId: string; status: string }) => t.planId === plan.id && t.status === "REPROGRAMADA");
const lastDays = rep.filter((t: { updatedAt: number }) => t.updatedAt > Date.now() - 4 * 86_400_000);
console.log("reprogramadas nos últimos 4 dias:", lastDays.length);
