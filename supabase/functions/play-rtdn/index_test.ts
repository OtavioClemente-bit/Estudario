import { assertEquals } from "jsr:@std/assert@1";
import { notificationOf, verifyPubSubToken } from "./index.ts";
import { base64url } from "../_shared/play.ts";

const expected = { audience: "https://exemplo.supabase.co/functions/v1/play-rtdn", email: "rtdn@projeto.iam.gserviceaccount.com" };

async function keyPair() {
  return await crypto.subtle.generateKey({ name: "RSASSA-PKCS1-v1_5", modulusLength: 2048, publicExponent: new Uint8Array([1, 0, 1]), hash: "SHA-256" }, true, ["sign", "verify"]);
}

async function sign(privateKey: CryptoKey, claims: Record<string, unknown>, kid = "k1"): Promise<string> {
  const head = base64url(JSON.stringify({ alg: "RS256", kid, typ: "JWT" }));
  const body = base64url(JSON.stringify(claims));
  const signature = await crypto.subtle.sign("RSASSA-PKCS1-v1_5", privateKey, new TextEncoder().encode(`${head}.${body}`));
  return `${head}.${body}.${base64url(new Uint8Array(signature))}`;
}

const good = () => ({ iss: "https://accounts.google.com", aud: expected.audience, email: expected.email, email_verified: true, exp: Math.floor(Date.now() / 1000) + 600 });

Deno.test("aceita o token do Pub/Sub assinado pelo Google", async () => {
  const pair = await keyPair();
  const keys = new Map([["k1", pair.publicKey]]);
  assertEquals(await verifyPubSubToken(await sign(pair.privateKey, good()), expected, keys), true);
});

Deno.test("recusa assinatura de outra chave, outro público, outro e-mail ou token vencido", async () => {
  const pair = await keyPair(), other = await keyPair();
  const keys = new Map([["k1", pair.publicKey]]);
  assertEquals(await verifyPubSubToken(await sign(other.privateKey, good()), expected, keys), false);
  assertEquals(await verifyPubSubToken(await sign(pair.privateKey, { ...good(), aud: "https://outro" }), expected, keys), false);
  assertEquals(await verifyPubSubToken(await sign(pair.privateKey, { ...good(), email: "x@y.com" }), expected, keys), false);
  assertEquals(await verifyPubSubToken(await sign(pair.privateKey, { ...good(), exp: 1000 }), expected, keys), false);
  assertEquals(await verifyPubSubToken(await sign(pair.privateKey, good(), "desconhecida"), expected, keys), false);
  assertEquals(await verifyPubSubToken("lixo", expected, keys), false);
});

Deno.test("lê o aviso dentro de message.data", () => {
  const data = base64url(JSON.stringify({ packageName: "br.com.estudario", voidedPurchaseNotification: { purchaseToken: "t" } }));
  assertEquals(notificationOf({ message: { data } })?.voidedPurchaseNotification?.purchaseToken, "t");
  assertEquals(notificationOf({ message: {} }), null);
});
