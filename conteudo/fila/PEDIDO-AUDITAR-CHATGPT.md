# Auditar matéria reescrita pelo ChatGPT (pedido para agente)

Audite `conteudo/entrada/<id>.chatgpt.json` (escrita pelo ChatGPT 6; vai substituir a matéria publicada;
mantenha o "version" que está no arquivo) seguindo os passos 2 a 6 de
`conteudo/fila/PEDIDO-AUDITORIA-AGENTE.md`, direto no arquivo, só com Edit, à mão. Não copie nem apague
arquivos. Não faça commit.

Prioridades:
1. Conferir cada lei, artigo, inciso, súmula, súmula vinculante e tema citado (abra Planalto/STF/STJ se
   conseguir). Atenção a redação desatualizada (ex.: Lei 8.429 após a Lei 14.230/2021; Lei 4.898
   revogada pela 13.869/2019). O que não confirmar, troque por explicação sem número.
   **Lei, emenda ou tema de 2024–2026 que você não conhece: pesquise na web (WebSearch: "Lei nº X/2026",
   site do Planalto, Câmara, Senado) ANTES de tirar.** O ChatGPT pesquisa e costuma acertar leis novas;
   já foram removidas por engano leis reais (EC 139/2026, Lei 15.484/2026, LC 227/2026). Só retire se
   a busca não achar a norma ou mostrar conteúdo diferente.
2. Trocar distratores caricatos (eliminam-se sem saber a matéria) por erros típicos de quem estudou mal,
   ajustando a explicação. Até ~8 trocas, nas piores.
3. Gabaritos, uma única certa, explicações coerentes com a alternativa, C/E começando com "Certo."/
   "Errado." e explicando o próprio item (sem falar de "alternativas").
4. Não pode aparecer a palavra "banca". Os avisos "PROIBIDA" de termos técnicos legítimos (militares
   na Constituição, viatura em responsabilidade do Estado, lesão corporal) são falsos positivos: mantenha.

Rode `deno run --allow-read conteudo/fila/conferir.ts conteudo/entrada/<id>.chatgpt.json` até
"validador: OK". Responda: correções por tipo (lei/fato, gabarito, distrator, explicação, validador) e
nota de 0 a 10 para a qualidade original.
