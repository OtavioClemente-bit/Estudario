export interface AuthenticatedUser {
  userId: string;
}

export interface AuthEnvironment {
  supabaseUrl: string;
  publishableKey: string;
  fetcher?: typeof fetch;
}

export class AuthError extends Error {
  constructor(
    public readonly code: "AUTH_REQUIRED" | "AUTH_INVALID" | "AUTH_UNAVAILABLE",
    public readonly status: 401 | 503,
  ) {
    super(code);
    this.name = "AuthError";
  }
}

function environmentFromRuntime(): AuthEnvironment {
  const supabaseUrl = Deno.env.get("SUPABASE_URL")?.trim();
  const publishableKey = (
    Deno.env.get("SUPABASE_ANON_KEY") ?? Deno.env.get("SUPABASE_PUBLISHABLE_KEY")
  )?.trim();
  if (!supabaseUrl || !publishableKey) {
    throw new AuthError("AUTH_UNAVAILABLE", 503);
  }
  return { supabaseUrl, publishableKey };
}

function bearerToken(request: Request): string {
  const header = request.headers.get("authorization")?.trim() ?? "";
  const match = /^Bearer\s+(\S+)$/i.exec(header);
  if (!match) throw new AuthError("AUTH_REQUIRED", 401);
  return match[1];
}

export async function authenticateSupabaseRequest(
  request: Request,
  environment?: AuthEnvironment,
): Promise<AuthenticatedUser> {
  const token = bearerToken(request);
  const resolved = environment ?? environmentFromRuntime();
  const fetcher = resolved.fetcher ?? fetch;

  let response: Response;
  try {
    response = await fetcher(
      `${resolved.supabaseUrl.replace(/\/$/, "")}/auth/v1/user`,
      {
        headers: {
          apikey: resolved.publishableKey,
          authorization: `Bearer ${token}`,
        },
      },
    );
  } catch {
    throw new AuthError("AUTH_UNAVAILABLE", 503);
  }

  if (!response.ok) throw new AuthError("AUTH_INVALID", 401);

  let payload: unknown;
  try {
    payload = await response.json();
  } catch {
    throw new AuthError("AUTH_INVALID", 401);
  }
  const userId = typeof payload === "object" && payload !== null && "id" in payload
    ? (payload as { id?: unknown }).id
    : null;
  if (typeof userId !== "string" || userId.trim().length === 0) {
    throw new AuthError("AUTH_INVALID", 401);
  }
  return { userId };
}
