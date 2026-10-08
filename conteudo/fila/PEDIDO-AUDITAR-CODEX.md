# Auditar matéria escrita pelo Codex (pedido para agente)

Faça a auditoria de `conteudo/entrada/<id>.json` seguindo os passos 2 a 6 de
`conteudo/fila/PEDIDO-AUDITORIA-AGENTE.md`, direto nesse arquivo (mantenha `"version": 1`; não copie nem
apague arquivos; edite só com Edit, à mão). A matéria foi escrita por outro modelo (GPT, via Codex).

Confira com rigor:
- cada gabarito e cada fato (lei, número de artigo, prazo, data, nome, conta); refaça as contas;
- só uma alternativa certa; distratores plausíveis — troque à mão os **caricatos** ("sempre",
  "exclusivamente", "inexistente", alternativa absurda que se elimina sem saber a matéria) por erros
  típicos de quem estudou mal;
- explicações que batem com o gabarito (C/E começa com "Certo." ou "Errado.");
- fontes reais e verificáveis (abra as URLs se puder);
- fórmulas e gráficos no formato de `conteudo/fila/FORMATO-VISUAL.md` (nunca "|" dentro de fórmula em
  linha de tabela; nada de cifrão simples).
- **Lei:** número de lei, artigo, súmula ou prazo de que você não tenha certeza → explique o conteúdo
  sem o número. Nunca deixe norma inventada ou revogada como vigente.

Depois rode `deno run --allow-read conteudo/fila/conferir.ts conteudo/entrada/<id>.json` até "validador: OK".
No fim responda: nº de correções (lista curta: gabarito / fato / distrator / explicação / fonte) e uma nota
de 0 a 10 para a qualidade original. Não faça commit.
