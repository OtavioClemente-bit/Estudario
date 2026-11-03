// Reescrita didática das matérias antigas pelo Codex (fila: conteudo/fila/reescrita-didatica-faltam.txt).
// O Codex copia a matéria publicada para conteudo/entrada/reescrita-codex/<id>.json e reescreve À MÃO o markdown de
// cada capítulo no padrão didático (o mesmo de conteudo/fila/PROMPT-REESCRITA-DIDATICA.md). Este programa só repete
// o pedido e confere: títulos dos capítulos e questões idênticos aos da publicada, teoria entre 15 e 30 mil
// caracteres e conferidor de matéria OK. Eu reviso e publico depois.
//   deno run -A scripts/codex/loop-reescrita.ts
const ROOT = new URL("../../", import.meta.url).pathname.replace(/^\/([A-Za-z]:)/, "$1");
const CODEX_JS = `${Deno.env.get("APPDATA")}/npm/node_modules/@openai/codex/bin/codex.js`;
const SAIDA = `${ROOT}conteudo/entrada/reescrita-codex`;
const LOG = `${ROOT}conteudo/entrada/reescrita-codex-log.txt`;
const FALHAS = `${ROOT}conteudo/entrada/reescrita-codex-falhas.json`;
const MAX_TOTAL = 8;
const sleep = (ms: number) => new Promise((r) => setTimeout(r, ms));
const log = (m: string) => {
  const l = `[${new Date().toLocaleString("pt-BR")}] ${m}`;
  console.log(l);
  Deno.writeTextFileSync(LOG, l + "\n", { append: true });
};
type Json = Record<string, any>;
const ler = (p: string) => JSON.parse(Deno.readTextFileSync(p).replace(/^﻿/, ""));
Deno.mkdirSync(SAIDA, { recursive: true });

const publicada = (id: string) => { const [a, ...b] = id.split("."); return `${ROOT}conteudo/materias/${a}/${b.join(".")}.json`; };
const linhas = (p: string) => { try { return Deno.readTextFileSync(`${ROOT}${p}`).split("\n").map((l) => l.split("\t")[0].trim().split(" ")[0]).filter(Boolean); } catch { return []; } };

// Problemas da reescrita (vazio = aprovada).
function conferir(id: string): string[] | null {
  let novo: Json, velho: Json;
  try { novo = ler(`${SAIDA}/${id}.json`); } catch (e) { try { Deno.statSync(`${SAIDA}/${id}.json`); return [`JSON inválido: ${e}`]; } catch { return null; } }
  try { velho = ler(publicada(id)); } catch { return ["matéria publicada não encontrada"]; }
  const erros: string[] = [];
  const tn = (novo.chapters ?? []).map((c: Json) => c.title), tv = (velho.chapters ?? []).map((c: Json) => c.title);
  if (JSON.stringify(tn) !== JSON.stringify(tv)) erros.push(`os títulos dos capítulos mudaram; têm de ser exatamente: ${JSON.stringify(tv)}`);
  if (JSON.stringify(novo.questions) !== JSON.stringify(velho.questions)) erros.push("as questões mudaram; copie-as de volta exatamente como estão na matéria publicada");
  const teoria = (novo.chapters ?? []).reduce((s: number, c: Json) => s + String(c.markdown ?? "").length, 0);
  if (teoria < 15_000 || teoria > 30_000) erros.push(`teoria com ${teoria} caracteres; precisa ficar entre 15.000 e 30.000`);
  const iguais = (novo.chapters ?? []).filter((c: Json, i: number) => c.markdown === velho.chapters?.[i]?.markdown).length;
  if (iguais) erros.push(`${iguais} capítulo(s) continuam com o texto antigo; reescreva todos`);
  // Capítulo com texto de outra matéria (03/11: 10 reescritas vieram com UTI, ISS, Campo Geral etc. no lugar).
  const pal = (s: string) => new Set(s.normalize("NFD").replace(/[̀-ͯ]/g, "").toLowerCase().split(/[^a-z0-9]+/).filter((w) => w.length >= 6));
  for (const [i, c] of (novo.chapters ?? []).entries()) {
    const md = String(c.markdown ?? "");
    const antigo = pal(String(velho.chapters?.[i]?.markdown ?? "")), novas = [...pal(md)];
    if (novas.length && novas.filter((w) => antigo.has(w)).length / novas.length < 0.13) erros.push(`capítulo ${i + 1} fala de outro assunto; reescreva o capítulo "${c.title}" desta matéria (${velho.title})`);
    if (!/Pegadinha/.test(md)) erros.push(`capítulo ${i + 1} sem "**Pegadinha:**"`);
    if (!/^###\s/m.test(md)) erros.push(`capítulo ${i + 1} sem subtítulos ###`);
  }
  const o = new Deno.Command("deno", { args: ["run", "--allow-read", "--allow-env", `${ROOT}scripts/biblioteca/conferir-materia.ts`, `${SAIDA}/${id}.json`], stdout: "piped", stderr: "piped" }).outputSync();
  const saida = (new TextDecoder().decode(o.stdout) + new TextDecoder().decode(o.stderr)).trim();
  if (!saida.endsWith("OK")) erros.push(`conferidor: ${saida.slice(0, 1500)}`);
  return erros;
}

const total: Record<string, number> = (() => { try { return ler(FALHAS); } catch { return {}; } })();
const contar = (id: string) => { total[id] = (total[id] ?? 0) + 1; Deno.writeTextFileSync(FALHAS, JSON.stringify(total, null, 1)); };
function pendentes(): string[] {
  const feitas = new Set(linhas("conteudo/fila/reescrita-didatica-feitas.txt"));
  return linhas("conteudo/fila/reescrita-didatica-faltam.txt").filter((id) => {
    if (feitas.has(id) || (total[id] ?? 0) >= MAX_TOTAL) return false;
    try { Deno.statSync(publicada(id)); } catch { return false; }
    const e = conferir(id);
    return e === null || e.length > 0;
  });
}

const PARALELO = Number(Deno.env.get("CODEX_PARALELO") ?? 6);
const emAndamento = new Set<string>();
log(`reescrita iniciada: ${pendentes().length} matérias na fila, ${PARALELO} em paralelo`);
async function trabalhador(n: number) {
  await sleep(n * 15_000);
  while (true) {
    const id = pendentes().find((p) => !emAndamento.has(p));
    if (!id) { log(`[r${n}] nada mais a pegar`); break; }
    emAndamento.add(id);
    const erros = conferir(id);
    const destino = `conteudo/entrada/reescrita-codex/${id}.json`;
    log(`[r${n}] ${erros ? "correção" : "reescrita"}: ${id}`);
    const pedido = erros
      ? `Leia AGENTS.md e conteudo/fila/PROMPT-REESCRITA-DIDATICA.md. A reescrita didática de ${destino} já começou; NÃO recomece.
O conferidor apontou só isto:
${erros.join("\n").slice(0, 3000)}
Corrija AGORA esses pontos editando o arquivo à mão (apply_patch), capítulo por capítulo. Se for o caso, recopie as questões
e os títulos da matéria publicada ${publicada(id).replace(ROOT, "")}. Escreva cada texto você mesmo, sem script.
Não faça outra coisa nesta rodada.`
      : `Leia AGENTS.md e o padrão em conteudo/fila/PROMPT-REESCRITA-DIDATICA.md (modelo pronto:
conteudo/materias/processo-civil/acao-popular-injuncao-habeas-data.json).
Faça a REESCRITA DIDÁTICA COMPLETA da matéria ${publicada(id).replace(ROOT, "")}:
1. Copie o arquivo inteiro para ${destino} (copiar com o terminal é permitido) e suba "version" em 1.
2. Reescreva VOCÊ MESMO, À MÃO, o "markdown" de TODOS os capítulos, um por um (apply_patch), no padrão: abre explicando
   o problema que o assunto resolve em linguagem simples; subtítulos ###; toda sigla e termo técnico explicado na primeira
   vez; exemplo concreto passo a passo com personagem; tabela comparativa quando houver institutos parecidos;
   "**Pegadinha:**" com o erro que a banca explora; "**Resumo para a prova**" no fim de cada capítulo.
   Teoria total entre 15.000 e 30.000 caracteres. Conteúdo concreto e correto, nada de generalidade.
3. NÃO mude os títulos dos capítulos nem as questões (elas apontam para os títulos). Não mexa em nada fora do markdown e da versão.
4. Nunca invente número de lei, artigo, prazo, valor ou súmula: use os que já estão no texto ou que você conferiu na fonte
   oficial; na dúvida, explique sem o número.
5. Valide com: deno run --allow-read --allow-env scripts/biblioteca/conferir-materia.ts ${destino} e corrija até OK.
Nenhum script gera, completa ou reescreve texto. Não faça mais nada nesta rodada.`;
    const o = await new Deno.Command("node", {
      args: [CODEX_JS, "exec", "-c", `model_reasoning_effort="${Deno.env.get("CODEX_ESFORCO") ?? "high"}"`, "-s", "workspace-write", "--skip-git-repo-check", "-C", ROOT, pedido],
      stdin: "null", stdout: "piped", stderr: "piped",
    }).output();
    const txt = new TextDecoder().decode(o.stdout) + new TextDecoder().decode(o.stderr);
    const depois = conferir(id);
    if (depois && depois.length === 0) { log(`   [r${n}] ok: ${id} reescrita e aprovada`); emAndamento.delete(id); await sleep(10_000); continue; }
    if (/hit your usage limit|usage limit (reached|has been reached)|try again (in|at) \d|rate_limit_exceeded/i.test(txt.slice(-2_000))) {
      log(`   [r${n}] limite do plano; esperando 30 min`);
      await sleep(30 * 60_000); // segura a matéria durante a espera
      emAndamento.delete(id);
      continue;
    }
    contar(id);
    log(`   [r${n}] ${id}: ${depois ? `faltam ${depois.length} correções` : "não gravou"} (tentativa ${total[id]} de ${MAX_TOTAL})`);
    emAndamento.delete(id);
  }
}
await Promise.all([...Array(PARALELO).keys()].map(trabalhador));
log("reescrita: fila concluída");
