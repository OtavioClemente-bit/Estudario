# Matérias que faltam — prioridade (08/10/2026)

## Como a lista foi feita
- **Base:** os 550 editais de `conteudo/editais/`, cruzados com as 480 matérias de `conteudo/materias/` e as 35 de `conteudo/entrada/` (ondas 6, 7 e 8).
- **Tópicos sem ligação:** 66.338 tópicos dos editais estão com `"topico": null`. **Cerca de 93% deles já têm matéria parecida e só faltam ser ligados** (rodar `ligar-editais.ts`). Exemplos: IA, matemática básica, falácias e silogismos, CNJ, Lei 14.133, abuso de autoridade. Não é para gerar matéria nova para esses temas.
- **Pontuação:** para cada tema, somei os editais em que ele aparece. Cada edital conta mais ou menos conforme dois fatores:
  - **Recência:** 2022 ou depois vale 1; de 2017 a 2021 vale 0,6; antes disso vale 0,3.
  - **Nível do concurso:** tribunais, tribunais de contas, fisco, PF/PRF, Legislativo federal, BC/CVM, MP, Defensoria, AGU e CGU valem ×1,3.
- **Colunas da tabela:** "Pontos" é essa soma ponderada. "Editais" é o total bruto. "Recentes" conta os de 2022 em diante.
- **Fora da lista:**
  - **Específicos de um órgão:** regimentos internos, legislação estadual, RICMS de cada estado, história e geografia de cada estado. Esses ficam para a IA.
  - **Atualidades:** mudam toda hora e não servem para matéria fixa.
- **Já em andamento no Codex:** ação civil pública. As outras 7 matérias da lista do Codex já estão prontas na branch `claude/eager-brahmagupta-rjxmsi`: lavagem, anticorrupção, interceptação, crimes tributários, contratos em espécie, execução penal, e arquitetura e contêineres.

## Prioridade A — alta procura e lacuna real
| # | id sugerido | Escopo | Pontos | Editais | Recentes | Quem |
|---|---|---|---|---|---|---|
| A1 | `biologia.citologia-genetica` | Célula (procarionte e eucarionte), organelas, divisão celular, DNA/RNA, genética mendeliana, biotecnologia em noções | 95* | 125* | 67* | Codex |
| A2 | `biologia.ecologia-ciclos` | Ecologia, cadeias e teias, relações ecológicas, ciclos biogeoquímicos, biomas em noções, impactos | (*) | | | Codex |
| A3 | `biologia.seres-vivos-classificacao` | Reinos, zoologia (poríferos a cordados), botânica (briófitas a angiospermas), vírus e bactérias | (*) | | | Codex |
| A4 | `biologia.corpo-humano-saude` | Sistemas do corpo humano, doenças transmissíveis, vacinas e imunidade | (*) | | | Claude |
| A5 | `informatica.power-bi-visualizacao` | Power BI (Power Query, modelo, DAX básico, visuais), dashboards, storytelling com dados, boas práticas | 50 | 47 | 42 | Claude |
| A6 | `contabilidade-publica.conta-unica-programacao-financeira` | Conta Única do Tesouro, programação financeira, cotas, SIAFI em noções, restos a pagar na execução financeira | 36 | 56 | 15 | Claude |
| A7 | `direito-administrativo.lei-estatais` | Lei 13.303/2016: regime das estatais, governança, licitações e contratos das estatais | 37 | 40 | 24 | Claude |
| A8 | `informatica.ferramentas-colaborativas` | Microsoft 365 (Teams, OneDrive, SharePoint), Google Workspace, Zoom, videoconferência, nuvem para o usuário | 32 | 33 | 31 | Codex |
| A9 | `saude-publica.epidemiologia-vigilancia` | Epidemiologia básica (indicadores, incidência e prevalência), vigilância epidemiológica e sanitária, notificação, biossegurança | 47 | 50 | 38 | Claude |
| A10 | `administracao-publica.regulacao-economica` | Teoria da regulação, falhas de mercado, captura, AIR (análise de impacto regulatório), agências (sem repetir a matéria de autarquias) | 28 | 29 | 23 | Claude |
| A11 | `legislacao.igualdade-racial-povos-tradicionais` | Lei Caó (7.437/1985), Convenção 169 da OIT, povos indígenas e quilombolas, sem repetir a matéria de crimes de preconceito | 32 | 32 | 26 | Claude |

\* O bloco de Biologia (A1 a A4) soma 125 editais: PM, bombeiros, agências, saúde e nível médio. A biblioteca tem química e física em `ciencias/`, mas **nenhuma matéria de Biologia**.

## Prioridade B — procura média ou concursos de alto nível
| # | id sugerido | Escopo | Pontos | Editais | Quem |
|---|---|---|---|---|---|
| B1 | `economia.federalismo-fiscal-tributacao` | Federalismo fiscal, transferências, incidência, progressividade e regressividade, eficiência e peso morto (fisco e CGU) | 22 | 26 | Claude |
| B2 | `informatica.contratacoes-ti` | Contratação de soluções de TI na Administração Pública (IN SGD/ME 94/2022 em noções), sem repetir licitação | 17 | 17 | Claude |
| B3 | `informatica.blockchain-criptoativos` | Blockchain, hash e consenso, contratos inteligentes, criptoativos e regulação em noções | 17 | 17 | Codex |
| B4 | `informatica.forense-pericia-digital` | Perícia digital, cadeia de custódia digital, aquisição e imagem, hash, esteganografia, análise de mídia | 16 | 19 | Claude |
| B5 | `direito.filosofia-sociologia-juridica` | Jusnaturalismo, positivismo (Kelsen, Hart), pós-positivismo, hermenêutica, sociologia jurídica (Weber, Durkheim, Marx) | 16 | 25 | Codex |
| B6 | `legislacao-penal-especial.henry-borel-crianca` | Lei 14.344/2022 e proteção da criança contra a violência doméstica | 10 | 10 (todos recentes) | Claude |
| B7 | `controle-externo.intosai-issai` | INTOSAI, ISSAI, Declarações de Lima e do México, independência das EFS | 12 | 12 (TCs e TCU) | Claude |
| B8 | `educacao.ldb-bncc-didatica` | LDB, BNCC, diretrizes curriculares, PPP, avaliação, educação inclusiva (professores de IF e redes estaduais) | 18 | 27 | Codex |
| B9 | `direito-administrativo.direito-agrario-terras-publicas` | Terras devolutas, faixa de fronteira, reforma agrária, função social da propriedade rural | 9 | 17 | Codex |
| B10 | `historia.historia-geral` | Antiguidade a Guerra Fria (diplomacia, ABIN, EsPCEx) | 10 | 14 | Codex |

## Prioridade C — carreiras específicas (deixar para a IA ou fazer por último)
- **Segurança do trabalho:** NRs, CIPA e EPI (32 editais, quase só para cargos de engenharia e segurança do trabalho).
- **Psicologia:** organizacional, jurídica e psicopatologia (16).
- **Engenharia e arquitetura:** ergonomia, luminotécnica, instalações (39, cada uma de uma área diferente).
- **Filosofia e sociologia gerais:** nível médio, PM e professor (21).
- **Comunicação social e jornalismo** (16).
- **Medicina e odontologia por especialidade:** cada uma aparece em 1 edital.

## Antes de gerar mais
1. **Publicar as ondas 6 a 8:** são as 35 matérias da branch. Depois rodar o `ligar-editais.ts`, que deve ligar dezenas de milhares de tópicos sem precisar de matéria nova.
2. **Consultar a `library_misses` no Supabase**, que guarda o que os alunos pediram e não achou matéria pronta, e reordenar esta lista pela procura real.
3. **Divisão sugerida entre Claude e Codex:**
   - **Claude:** temas com lei para conferir número a número ou com código para rodar (A4 a A7, A9 a A11, B1, B2, B4, B6, B7).
   - **Codex:** conteúdo estável, de menor risco jurídico (A1 a A3, A8, B3, B5, B8 a B10).
   - Cada um faz só os itens marcados com o seu nome, para não repetir o que aconteceu com as 7 duplicadas.
