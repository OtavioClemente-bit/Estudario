#!/usr/bin/env bash
# Roda um script deno da biblioteca com SUPABASE_URL e a chave service_role lidas na hora pelo
# Supabase CLI logado (a chave nunca é impressa nem gravada).
# Uso: bash scripts/biblioteca/com-chave.sh scripts/biblioteca/fila.ts [args...]
set -euo pipefail
cd "$(dirname "$0")/../.."
REF="gaqqilzhmvkxfpvivwwv"
KEY=$(npx --no-install supabase projects api-keys --project-ref "$REF" -o json 2>/dev/null | deno eval "
const a = JSON.parse(await new Response(Deno.stdin.readable).text());
const k = (Array.isArray(a) ? a : a.keys || []).find((k) => k.name === 'service_role' || k.type === 'secret');
console.log(k?.api_key ?? '');")
[ -n "$KEY" ] || { echo "Sem chave (faça 'npx supabase login')." >&2; exit 1; }
script="$1"; shift
SUPABASE_URL="https://$REF.supabase.co" SUPABASE_SERVICE_ROLE_KEY="$KEY" deno run --allow-read --allow-write --allow-env --allow-net "$script" "$@"
