#!/usr/bin/env bash
# Só leitura: mostra os últimos pedidos de material e os tópicos que não acharam matéria pronta.
set -euo pipefail
cd "$(dirname "$0")/../.."
REF="gaqqilzhmvkxfpvivwwv"
KEY=$(npx --no-install supabase projects api-keys --project-ref "$REF" -o json 2>/dev/null | deno eval "
const a = JSON.parse(await new Response(Deno.stdin.readable).text());
const k = (Array.isArray(a) ? a : a.keys || []).find((k) => k.name === 'service_role' || k.type === 'secret');
console.log(k?.api_key ?? '');")
URL="https://$REF.supabase.co/rest/v1"
get() { curl -s "$URL/$1" -H "apikey: $KEY" -H "Authorization: Bearer $KEY"; }
echo "== Últimos pedidos (ai_jobs)"
get "ai_jobs?select=created_at,status,feature,model_version,topic:request_payload->input->topicPath&order=created_at.desc&limit=5" | deno eval "
for (const j of JSON.parse(await new Response(Deno.stdin.readable).text())) console.log(j.created_at, j.status, j.feature, j.model_version ?? '-', JSON.stringify(j.topic));"
echo "== Tópicos sem matéria pronta (library_misses)"
get "library_misses?select=last_requested_at,requests,subject,topic,board,topic_norm&order=last_requested_at.desc&limit=5" | deno eval "
for (const m of JSON.parse(await new Response(Deno.stdin.readable).text())) console.log(m.last_requested_at, m.requests + 'x', m.board ?? '-', '|', m.topic, '| norm:', m.topic_norm);"
