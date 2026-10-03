#!/usr/bin/env bash
# Publica a biblioteca (conteudo/) no Supabase de produção.
# A chave service_role é lida na hora pelo Supabase CLI logado e nunca é impressa nem gravada.
# Só publica se o conferidor não apontar problema (o importar.ts não envia nada se houver ✗).
set -euo pipefail
cd "$(dirname "$0")/../.."

REF="gaqqilzhmvkxfpvivwwv"
KEY=$(npx --no-install supabase projects api-keys --project-ref "$REF" -o json 2>/dev/null | deno eval "
const a = JSON.parse(await new Response(Deno.stdin.readable).text());
const k = (Array.isArray(a) ? a : a.keys || []).find((k) => k.name === 'service_role' || k.type === 'secret');
console.log(k?.api_key ?? '');")
if [ -z "$KEY" ]; then
  echo "Não consegui obter a chave pelo Supabase CLI (faça 'npx supabase login')." >&2
  exit 1
fi

SUPABASE_URL="https://$REF.supabase.co" SUPABASE_SERVICE_ROLE_KEY="$KEY" \
  deno run --allow-read --allow-env --allow-net scripts/biblioteca/importar.ts --publicar
