# AI source metadata RPC authentication

`get_ai_syllabus_source_metadata(text)` remains executable only by the
`service_role` Postgres role; `anon` and `authenticated` have no `EXECUTE`.

The Supabase Data API does not turn the current `sb_secret_*` key into a
`service_role` JWT context for this restricted RPC in the deployed flow. It
therefore receives `sb_secret_*` as an API key but can still be rejected by
PostgREST with `42501`.

The Edge Function accepts an optional `SUPABASE_SERVICE_ROLE_JWT` runtime
secret. When configured, only the metadata RPC uses that legacy service-role
JWT (`apikey` plus `Authorization: Bearer`). The PDF download continues using
the publishable key and the authenticated user's JWT, preserving owner/path
validation and user-scoped access. The JWT is never sent to Android or used by
the client path.

When no legacy JWT is configured, the function retains the official secret-key
form (`apikey: sb_secret_*`, without an Authorization header), so deployments
can migrate credentials without opening the RPC to client roles. Cloud
deployment must provide `SUPABASE_SERVICE_ROLE_JWT` for this backend-only RPC
until the project confirms a secret-key Data API path that maps to
`service_role` for restricted RPC execution.
