# Lote 3 do Codex — 18 matérias novas (08/10/2026)

Mesmas regras do `PEDIDO-CODEX-LOTE-2.md` (leia antes, só leitura: `AGENTS.md`,
`conteudo/fila/PEDIDO-MATERIA-AGENTE.md`, `conteudo/fila/FORMATO-VISUAL.md` e o modelo
`conteudo/materias/direito-penal/teoria-do-crime.json`). São assuntos que os editais pedem e a
biblioteca ainda não tem. Não repita o que já existe: confira `conteudo/materias/<área>/` antes.

## O que mais derrubou nota nos lotes anteriores
- **Distratores caricatos** ("sempre", "exclusivamente", alternativa absurda). Distrator bom = erro de
  quem estudou mal: instituto vizinho, prazo/número trocado, exceção esquecida.
- **Referência inventada** (resolução, tema, informativo, número de julgado). Na dúvida, sem número.
- A alternativa certa não pode ser sistematicamente a mais longa.
- Capítulos rasos: cada um com 2.500+ caracteres de conteúdo útil, com tabelas comparativas.

## Regras
- Grave cada matéria como `<id>.json` **na pasta desta conversa**. Uma por vez, inteira, à mão.
- 5 capítulos, 45 questões (25 A–E e 20 Certo/Errado; 15/15/15 de dificuldade; gabaritos equilibrados),
  15+ flashcards, dicas, pegadinhas, recordação ativa, conceitos de erro e fontes reais verificáveis.
- Rode `deno run --allow-read C:\Users\otavi\Documents\Codex\2026-09-15\vc-x20\conteudo\fila\conferir.ts <id>.json`
  até "validador: OK". Não faça commit nem push. Não escreva no projeto.

## Matérias (ids exatos), nesta ordem
1. `direito-empresarial.microempresa-epp-simples` — LC 123/2006: enquadramento, MEI, Simples Nacional, tratamento diferenciado em licitações e no acesso à justiça
2. `direito-empresarial.operacoes-societarias` — transformação, incorporação, fusão, cisão; sociedades coligadas, controladas, grupos e consórcios; dissolução e liquidação
3. `direito-empresarial.contratos-empresariais-propriedade-industrial` — compra e venda mercantil, franquia (Lei 13.966/2019), representação, distribuição, leasing, factoring; Lei 9.279/1996 (patente, marca, desenho industrial)
4. `direito-empresarial.protesto-instituicoes-financeiras` — protesto de títulos (Lei 9.492/1997), intervenção e liquidação extrajudicial de instituições financeiras, prepostos e auxiliares do empresário
5. `direito-tributario.fontes-vigencia-interpretacao` — legislação tributária no CTN: fontes, vigência, aplicação, interpretação e integração (arts. 96 a 112)
6. `direito-civil.atos-unilaterais-preferencias` — promessa de recompensa, gestão de negócios, pagamento indevido, enriquecimento sem causa; preferências e privilégios creditórios
7. `direito-civil.registros-publicos-locacao` — Lei 6.015/1973 (registro civil e de imóveis em noções), Lei 8.245/1991 (locação urbana, ações locatícias)
8. `processo-civil.acoes-coletivas-reclamacao` — ação civil pública, ação coletiva do CDC, microssistema coletivo, reclamação, IRDR e IAC em noções
9. `direito-internacional.privado-cortes-internacionais` — LINDB no DIPr, nacionalidade e estrangeiro (Lei 13.445/2017), asilo e refúgio, imunidade de jurisdição, TPI, CIJ, domínio público internacional
10. `direito-eleitoral.acoes-crimes-eleitorais` — AIJE, AIME, RCED, representações, condutas vedadas, inelegibilidades (LC 64/1990), crimes eleitorais
11. `direito-previdenciario.rpps-previdencia-complementar` — RPPS (art. 40 CF após EC 103/2019), contagem recíproca e compensação, previdência complementar (LC 108 e 109/2001)
12. `contabilidade.valor-justo-avp-notas` — CPC 46 (valor justo), CPC 12 (ajuste a valor presente), notas explicativas, políticas contábeis e eventos subsequentes, NBC em noções
13. `administracao-publica.gestao-resultados-participacao` — gestão por resultados, indicadores de desempenho, contratualização, accountability, participação e controle social
14. `informatica.qualidade-testes-software` — TDD, BDD, DDD, tipos e níveis de teste, CI, métricas e qualidade (ISO 25010 em noções)
15. `informatica.acessibilidade-portais-web` — eMAG, WCAG, portais corporativos, usabilidade, SEO básico
16. `direito-ambiental.codigo-florestal-recursos-hidricos` — Lei 12.651/2012 (APP, reserva legal, CAR), Lei 9.433/1997 (política de recursos hídricos, outorga, cobrança)
17. `educacao-fiscal.cidadania-tributos` — função social do tributo, orçamento e cidadania, controle social do gasto, programa nacional de educação fiscal
18. `administracao-geral.gestao-pessoas-competencias` — gestão por competências, avaliação de desempenho, treinamento e desenvolvimento, clima e cultura (confira antes se já existe; se existir, pule)
