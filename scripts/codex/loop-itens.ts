// Mantém o Codex trabalhando sem parar na lista de itens sem matéria: chama `codex exec` de novo toda vez
// que ele termina uma rodada, até existirem as 700 decisões. Se o limite do plano acabar, espera e tenta de novo.
//   deno run -A scripts/codex/loop-itens.ts
// O Codex continua sendo quem escreve (como na tarefa feita à mão); este programa só repete o pedido.
const ROOT = new URL("../../", import.meta.url).pathname.replace(/^\/([A-Za-z]:)/, "$1");
const CODEX = `${Deno.env.get("APPDATA")}/npm/codex.cmd`;
const LISTA = `${ROOT}conteudo/fila/itens-sem-materia.tsv`;
const SAIDA = `${ROOT}conteudo/entrada/itens-sem-materia`;
const LOG = `${ROOT}conteudo/entrada/itens-sem-materia-log.txt`;
const sleep = (ms: number) => new Promise((r) => setTimeout(r, ms));
const log = (m: string) => {
  const l = `[${new Date().toLocaleString("pt-BR")}] ${m}`;
  console.log(l);
  Deno.writeTextFileSync(LOG, l + "\n", { append: true });
};
const total = Deno.readTextFileSync(LISTA).trim().split("\n").length - 1;
const feitos = () => { try { return [...Deno.readDirSync(SAIDA)].filter((e) => /^\d{3}\.json$/.test(e.name)).length; } catch { return 0; } };

const PEDIDO = `Leia AGENTS.md e conteudo/fila/PEDIDO-CODEX-ITENS-SEM-MATERIA.md e continue a lista
conteudo/fila/itens-sem-materia.tsv a partir da primeira linha que ainda não tem decisão gravada em
conteudo/entrada/itens-sem-materia/ (arquivos 001.json, 002.json...). Siga em sequência, gravando cada decisão
antes de passar à próxima. "amplo" só para disciplina ou lei inteira; assunto específico vira materia-nova.
Se uma matéria nova continuar falhando no conferidor depois de 3 tentativas, grave a decisão "materia-nova" com o id,
deixe o arquivo como está em conteudo/entrada/materias-codex-2/ e siga. Não pare para perguntar; trabalhe o máximo
de linhas que conseguir nesta rodada.`;

log(`loop do Codex iniciado: ${feitos()} de ${total} itens já decididos`);
let semAvanco = 0;
while (feitos() < total) {
  const antes = feitos();
  log(`rodada começando no item ${antes + 1}`);
  const o = await new Deno.Command(CODEX, {
    args: ["exec", "-s", "workspace-write", "--skip-git-repo-check", "-C", ROOT, PEDIDO],
    stdin: "null", stdout: "piped", stderr: "piped",
  }).output();
  const txt = new TextDecoder().decode(o.stdout) + new TextDecoder().decode(o.stderr);
  const depois = feitos();
  log(`rodada terminou: ${depois - antes} itens novos (agora ${depois} de ${total})`);
  if (depois > antes) { semAvanco = 0; await sleep(10_000); continue; }
  semAvanco++;
  const limite = /usage limit|rate limit|quota|too many requests|429|limite/i.test(txt);
  // Sem avanço: limite do plano (espera 30 min) ou travou num item (espera 5 min e tenta de novo).
  const espera = limite ? 30 : Math.min(5 * semAvanco, 30);
  log(`sem avanço (${limite ? "limite do plano" : "rodada não gravou nada"}); esperando ${espera} minutos. Fim da saída: ${txt.slice(-300).replace(/\s+/g, " ")}`);
  await sleep(espera * 60_000);
}
log("lista concluída");
