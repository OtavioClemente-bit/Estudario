# AI syllabus worker scheduler deployment gate

`ai-syllabus-worker` is configured with `verify_jwt = false` because its
handler authenticates an opaque `AI_WORKER_AUTH_TOKEN` itself. The token must
be supplied as an Edge Function secret and is never accepted from Android or
from a normal Supabase user session.

The repository currently has no safe scheduler deployment to commit:

- local `pg_cron` and `pg_net` are available, but `vault` is not available;
- no scheduler job currently exists in the project;
- placing `AI_WORKER_AUTH_TOKEN` or a service credential in a migration would
  expose a secret in source control/database history.

Before enabling a persistent one-minute scheduler, the cloud project must
enable/configure a supported secret store (Vault or an equivalent managed
secret integration), create the recurring POST to
`/functions/v1/ai-syllabus-worker`, and inject `Authorization: Bearer
<AI_WORKER_AUTH_TOKEN>` from that secret store. The durable queue remains the
database job state; Android is not a scheduler.
