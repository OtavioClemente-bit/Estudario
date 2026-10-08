# Lote 2 do Codex — 22 matérias novas (08/10/2026)

Antes de começar, leia no projeto (só leitura): `AGENTS.md`, `conteudo/fila/PEDIDO-MATERIA-AGENTE.md`,
`conteudo/fila/FORMATO-VISUAL.md` e o modelo `conteudo/materias/direito-penal/teoria-do-crime.json`.
As duas matérias de Biologia que você já fez foram auditadas (notas 8,5 e 8): o que mais pesou foram
**distratores caricatos** ("sempre", "exclusivamente", "inexistente", alternativa absurda que se elimina
sem saber a matéria) e texto prolixo. Distrator bom é o erro típico de quem estudou mal.

## Regras
- Grave cada matéria como `<id>.json` **na pasta desta conversa** (o Claude copia para o projeto).
- Uma por vez, inteira, à mão (sem script gerando questões, alternativas ou explicações).
- 5 capítulos, 45 questões (25 A–E e 20 Certo/Errado; 15 fáceis, 15 médias, 15 difíceis), 15+ flashcards,
  dicas, pegadinhas, recordação ativa e fontes reais e verificáveis.
- Fórmula, gráfico e figura no formato de `FORMATO-VISUAL.md` sempre que o assunto pedir.
- Lei: só cite número de lei, artigo, súmula ou prazo se tiver certeza e for regra vigente. Na dúvida,
  explique o conteúdo sem o número. Nunca invente.
- Depois de cada uma rode
  `deno run --allow-read C:\Users\otavi\Documents\Codex\2026-09-15\vc-x20\conteudo\fila\conferir.ts <id>.json`
  e corrija até "validador: OK".
- Não faça commit nem push. Não escreva no projeto.

## Matérias (ids exatos), nesta ordem
1. `biologia.seres-vivos-classificacao` — reinos, vírus e bactérias, zoologia (poríferos a cordados), botânica (briófitas a angiospermas)
2. `biologia.corpo-humano-saude` — sistemas do corpo humano, doenças transmissíveis, vacinas e imunidade
3. `informatica.ferramentas-colaborativas` — Microsoft 365 (Teams, OneDrive, SharePoint), Google Workspace, videoconferência, nuvem para o usuário
4. `informatica.power-bi-visualizacao` — Power BI (Power Query, modelo, DAX básico, visuais), dashboards, boas práticas de visualização
5. `informatica.blockchain-criptoativos` — blockchain, hash e consenso, contratos inteligentes, criptoativos e regulação em noções
6. `informatica.forense-pericia-digital` — perícia digital, cadeia de custódia, aquisição e imagem, hash, análise de mídia
7. `direito.filosofia-sociologia-juridica` — jusnaturalismo, positivismo (Kelsen, Hart), pós-positivismo, hermenêutica, Weber, Durkheim, Marx
8. `educacao.ldb-bncc-didatica` — LDB, BNCC, diretrizes curriculares, PPP, avaliação, educação inclusiva
9. `direito-administrativo.direito-agrario-terras-publicas` — terras devolutas, faixa de fronteira, reforma agrária, função social da propriedade rural
10. `historia.historia-geral` — Antiguidade até a Guerra Fria
11. `saude-publica.epidemiologia-vigilancia` — indicadores, incidência e prevalência, vigilância epidemiológica e sanitária, notificação, biossegurança
12. `administracao-publica.regulacao-economica` — teoria da regulação, falhas de mercado, captura, análise de impacto regulatório, agências (sem repetir autarquias)
13. `economia.federalismo-fiscal-tributacao` — federalismo fiscal, transferências, incidência, progressividade, eficiência e peso morto
14. `controle-externo.intosai-issai` — INTOSAI, ISSAI, Declarações de Lima e do México, independência das EFS
15. `contabilidade-publica.conta-unica-programacao-financeira` — Conta Única do Tesouro, programação financeira, cotas, SIAFI em noções, restos a pagar na execução
16. `direito-administrativo.lei-estatais` — regime jurídico das empresas estatais, governança, licitações e contratos das estatais
17. `informatica.contratacoes-ti` — contratação de soluções de TI na Administração Pública em noções (sem repetir licitação geral)
18. `legislacao.igualdade-racial-povos-tradicionais` — igualdade racial, Convenção 169 da OIT, povos indígenas e quilombolas (sem repetir crimes de preconceito)
19. `legislacao-penal-especial.violencia-crianca-adolescente` — proteção da criança e do adolescente contra a violência doméstica e familiar
20. `seguranca-trabalho.normas-regulamentadoras` — NRs mais cobradas, CIPA, EPI, SESMT, insalubridade e periculosidade em noções
21. `psicologia.psicologia-organizacional` — motivação, liderança, clima e cultura, grupos, recrutamento e seleção, saúde mental no trabalho
22. `filosofia-sociologia.fundamentos` — principais filósofos e correntes, ética, sociologia clássica (nível médio)

Se o limite acabar no meio, pare depois de terminar a matéria atual e diga até qual chegou.
