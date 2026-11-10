// Junta as respostas do ChatGPT (conteudo/entrada/oficiais/respostas/oficial-*.json: matéria escolhida e
// explicação) às questões de prova real (work/oficiais/questoes.jsonl) e grava conteudo/recortes-oficiais/
// <banca>/<matéria>.json no formato dos recortes, com sourceType "REAL", link, banca, órgão e ano.
// Só converte: enunciado, alternativas e gabarito são os da prova. Fica de fora a questão sem matéria
// válida, com explicação curta ou com alerta (gabarito suspeito ou desatualizada), que vai para
// work/oficiais/alertas.jsonl. O importar.ts junta estes arquivos ao recorte da mesma banca.
//   deno run -A scripts/oficiais/montar.ts
const ROOT = new URL("../../", import.meta.url).pathname.replace(/^\/([A-Za-z]:)/, "$1");
const RESPOSTAS = `${ROOT}conteudo/entrada/oficiais/respostas`;
const SAIDA = `${ROOT}conteudo/recortes-oficiais`;
type Json = Record<string, any>;

const questoes = new Map<string, Json>();
for (const l of Deno.readTextFileSync(`${ROOT}work/oficiais/questoes.jsonl`).trim().split("\n")) { const q = JSON.parse(l); questoes.set(q.id, q); }
const materias = new Map<string, Json>();
for (const area of Deno.readDirSync(`${ROOT}conteudo/materias`)) {
  if (!area.isDirectory) continue;
  for (const f of Deno.readDirSync(`${ROOT}conteudo/materias/${area.name}`)) {
    if (!f.name.endsWith(".json")) continue;
    try { const m = JSON.parse(Deno.readTextFileSync(`${ROOT}conteudo/materias/${area.name}/${f.name}`)); materias.set(m.id, m); } catch { /* ignora */ }
  }
}
const norm = (s: string) => s.normalize("NFD").replace(/[̀-ͯ]/g, "").toLowerCase();
const palavras = (s: string) => new Set(norm(s).split(/[^a-z0-9]+/).filter((w) => w.length >= 4));
// Capítulo da matéria com mais palavras em comum com a questão (a seção tem de ser um capítulo dela).
function secao(m: Json, texto: string): string {
  const q = palavras(texto);
  let melhor = m.chapters[0], pontos = -1;
  for (const c of m.chapters) {
    const p = [...palavras(`${c.title} ${String(c.markdown ?? "").slice(0, 3000)}`)].filter((w) => q.has(w)).length;
    if (p > pontos) { melhor = c; pontos = p; }
  }
  return String(melhor.title).trim();
}
const slug = (banca: string) => norm(banca).replace(/[^a-z0-9]+/g, "-").replace(/^-|-$/g, "");

// Alertas que eu conferi e liberei (gabarito oficial certo e norma ainda vigente): work/oficiais/alertas-ok.json.
let alertasLiberados = new Set<string>();
try { alertasLiberados = new Set((JSON.parse(Deno.readTextFileSync(`${ROOT}work/oficiais/alertas-ok.json`)) as string[]).map(String)); } catch { /* nenhum */ }
// Explicações que deixaram de bater com a questão (08/11: provas da Fundatec com dois cadernos de mesma numeração;
// depois da nova extração parte das explicações era do outro caderno): work/oficiais/explicacoes-bloqueadas.json.
let bloqueadas = new Set<string>();
try { bloqueadas = new Set((JSON.parse(Deno.readTextFileSync(`${ROOT}work/oficiais/explicacoes-bloqueadas.json`)) as string[]).map(String)); } catch { /* nenhuma */ }
const porRecorte = new Map<string, { banca: string; topic: string; questions: Json[] }>();
const alertas: Json[] = [];
const contagem = { respostas: 0, aceitas: 0, semMateria: 0, curtas: 0, alerta: 0, desconhecidas: 0 };
let arquivos: string[] = [];
try { arquivos = [...Deno.readDirSync(RESPOSTAS)].filter((e) => e.name.endsWith(".json")).map((e) => e.name).sort(); } catch { /* nenhuma ainda */ }
// Explicações reescritas por Claude depois que o gabarito foi corrigido (10/11: Fundatec e TCE-GO, arquivos
// oficial-refeitas-*.json): vêm primeiro, valem mesmo se o id estava bloqueado, e a resposta antiga do mesmo id é ignorada.
const refeitas = arquivos.filter((a) => a.startsWith("oficial-refeitas-"));
arquivos = [...refeitas, ...arquivos.filter((a) => !a.startsWith("oficial-refeitas-"))];
const vistos = new Set<string>();
for (const arquivo of arquivos) {
  let r: Json; try { r = JSON.parse(Deno.readTextFileSync(`${RESPOSTAS}/${arquivo}`)); } catch { continue; }
  const refeita = arquivo.startsWith("oficial-refeitas-");
  for (const it of r.itens ?? []) {
    contagem.respostas++;
    const q = questoes.get(String(it.id));
    if (!q) { contagem.desconhecidas++; continue; }
    if (vistos.has(String(it.id))) continue;
    vistos.add(String(it.id));
    if (!refeita && bloqueadas.has(String(it.id))) { contagem.alerta++; continue; }
    const escolhida = String(it.materia ?? "").trim().split(/\s/)[0];
    const m = materias.get(escolhida);
    // Basta a matéria existir: os 5 candidatos mudam quando o preparar.ts roda de novo com a biblioteca maior, e a
    // escolha feita sobre a lista antiga continua válida (04/11: exigir a lista atual derrubava ~3 mil explicações).
    if (!m) { contagem.semMateria++; continue; }
    const explicacao = String(it.explicacao ?? "").trim();
    if (String(it.alerta ?? "").trim() && !alertasLiberados.has(String(it.id))) { contagem.alerta++; alertas.push({ id: q.id, alerta: it.alerta, explicacao }); continue; }
    if (explicacao.length < 120) { contagem.curtas++; continue; }
    const ce = q.alternativas.length === 0;
    // Alternativas repetidas (extração colou a mesma linha duas vezes) travam a publicação: a questão fica de fora (10/11).
    if (!ce && new Set(q.alternativas.map((t: string) => norm(String(t)).trim())).size !== q.alternativas.length) { contagem.curtas++; continue; }
    // Tira só o cabeçalho/rodapé de caderno que a extração colou no texto ("658001_01N770539 CEBRASPE | IBAMA – Edital:
    // 2021 -- CONHECIMENTOS ESPECÍFICOS --", "Página 3 de 20"). O texto da questão não muda (09/11).
    const limpo = (s: string) => s
      // Instruções padrão do caderno do Cebraspe coladas antes do texto (09/11: ~300 questões bloqueadas por isso).
      .replace(/Cada um dos itens d[ao]s? provas? objetivas? está vinculado ao comando[\s\S]{0,700}?único documento válido para a correção d[ao]s? suas? provas? objetivas?\.\s*/i, "")
      .replace(/^\s*•\s*Caso haja item\(ns\) constituído\(s\) pela estrutura Situação hipotética[\s\S]{0,300}?assertiva proposta\.\s*/im, "")
      .replace(/^\s*•?\s*Eventuais espaços livres[\s\S]{0,250}?rascunhos etc\.\s*/im, "")
      .replace(/^\s*--\s*PROVAS?\s+OBJETIVAS?\s*--\s*/gim, "")
      // Cabeçalho em linha própria no meio (página nova): o que vem antes é sobra da página anterior ("resposta.").
      .replace(/^[\s\S]{0,200}?\n\s*(?:[\d_]*[A-Z0-9]{6,}\s*\n\s*)?(?:CEBRASPE|FGV|FUNDATEC|CESGRANRIO|AOCP)\s*\|\s*[^\n]{1,60}Edital:\s*\d{4}\s*\n/i, "")
      .replace(/^\s*(?:[\d_]*[A-Z0-9]{6,}\s+)?(?:CEBRASPE|FGV|FUNDATEC|CESGRANRIO|AOCP)\s*\|\s*[^\n–-]{1,60}[–-]\s*Edital:\s*\d{4}\s*/i, "")
      .replace(/^\s*--\s*CONHECIMENTOS\s+(?:B[ÁA]SICOS|ESPEC[ÍI]FICOS|GERAIS|COMPLEMENTARES)\s*--\s*/i, "")
      .replace(/\s*P[áa]gina\s+\d+\s*(?:de|\/)\s*\d+\s*/gi, " ")
      .trim();
    const statement = `${q.base && q.base.length > 20 && !/^\(?Dispon[ií]vel em/i.test(q.base) ? `${limpo(q.base)}\n\n` : ""}${limpo(String(q.enunciado))}`;
    // O validador aceita enunciado de 20 a 4.000 caracteres (texto-base longo de inglês passava disso, 07/11).
    if (statement.length < 20 || statement.length > 4000) { contagem.curtas++; continue; }
    const questao: Json = {
      topico: null, estilo: String(q.banca).toUpperCase(), statement,
      format: ce ? "TRUE_FALSE" : "MULTIPLE_CHOICE", difficulty: "MEDIA",
      options: ce
        ? [{ key: "C", text: "Certo", correct: q.gabarito === "C" }, { key: "E", text: "Errado", correct: q.gabarito === "E" }]
        : q.alternativas.map((t: string, k: number) => ({ key: "ABCDE"[k], text: String(t).trim(), correct: "ABCDE"[k] === q.gabarito })),
      explanation: explicacao, section: secao(m, statement),
      errorConceptKey: m.errorConcepts?.[0]?.key ?? null,
      sourceType: "REAL", sourceUrl: q.url, board: q.banca, agency: q.concurso, year: q.ano,
    };
    const chave = `${slug(q.banca)}/${escolhida}`;
    const grupo = porRecorte.get(chave) ?? { banca: q.banca, topic: escolhida, questions: [] as Json[] };
    // A mesma questão cai em provas de vários cargos do mesmo concurso (conhecimentos básicos): entra uma vez só.
    const chaveEnunciado = norm(statement).replace(/[^a-z0-9]+/g, " ").trim();
    if (grupo.questions.some((x) => norm(String(x.statement)).replace(/[^a-z0-9]+/g, " ").trim() === chaveEnunciado)) continue;
    // O validador do importar.ts aceita no máximo 600 questões por recorte (07/11: um recorte passou e travou a publicação).
    if (grupo.questions.length >= 600) continue;
    grupo.questions.push(questao);
    porRecorte.set(chave, grupo);
    contagem.aceitas++;
  }
}
// Trava: se muitas respostas não acham a questão (questoes.jsonl incompleto, ex.: preparado enquanto a coleta
// regravava as provas), não mexe nos recortes publicados. Em 04/11 isso tiraria metade das provas do app.
// 08/11: o preparar.ts agora grava o arquivo inteiro de uma vez e filtra de propósito questões com figura, símbolo
// ilegível ou caderno duplicado (~2.500 respostas ficam sem questão). A trava só dispara se o arquivo encolheu muito.
if (contagem.desconhecidas > Math.max(50, contagem.respostas * 0.02) && questoes.size < 100_000) {
  console.error(`questoes.jsonl incompleto (${contagem.desconhecidas} respostas sem questão): recortes-oficiais mantidos como estão; rode o preparar.ts de novo com a coleta parada.`);
  console.log(contagem);
  Deno.exit(0);
}
try { Deno.removeSync(SAIDA, { recursive: true }); } catch { /* primeira vez */ }
for (const [chave, g] of porRecorte) {
  Deno.mkdirSync(`${SAIDA}/${chave.split("/")[0]}`, { recursive: true });
  Deno.writeTextFileSync(`${SAIDA}/${chave}.json`, JSON.stringify({ topic: g.topic, board: g.banca, role: null, questions: g.questions }, null, 1) + "\n");
}
// Pares banca × matéria com prova real e sem recorte da banca: o gerador do Antigravity põe estes na frente (08/11).
const faltando: Record<string, number> = {};
for (const [chave, g] of porRecorte) {
  const [pasta, topic] = chave.split("/");
  try { Deno.statSync(`${ROOT}conteudo/recortes/${pasta}/${topic}.json`); } catch { faltando[`${topic}|${pasta}`] = g.questions.length; }
}
Deno.writeTextFileSync(`${ROOT}work/oficiais/recortes-faltando.json`, JSON.stringify(faltando, null, 1));
Deno.writeTextFileSync(`${ROOT}work/oficiais/alertas.jsonl`, alertas.map((a) => JSON.stringify(a)).join("\n") + (alertas.length ? "\n" : ""));
console.log({ ...contagem, recortes: porRecorte.size });
