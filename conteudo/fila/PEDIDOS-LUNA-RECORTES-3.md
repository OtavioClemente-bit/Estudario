# Pedidos para o Codex: recortes de banca, lote 3 (itens 1 a 300 desta lista)

Os lotes 1 e 2 já estão publicados. Esta lista traz os próximos 300 pares banca × matéria **sem recorte** que mais aparecem
nos editais (juntos, cerca de 12 mil editais). As regras abaixo já incluem tudo o que precisou ser corrigido à mão nos lotes
anteriores; siga-as à risca, porque cada defeito vira retrabalho.

## Mensagem para colar no Codex

```
Leia AGENTS.md e conteudo/fila/PEDIDOS-LUNA-RECORTES-3.md neste repositório e gere os recortes da tabela, na ordem,
começando pelo item <INÍCIO>. Um item por vez: escreva, valide e grave antes de passar ao próximo. Grave cada recorte em
pedaços (cabecalho.json + q1.json..q4.json com 10 questões cada) em conteudo/entrada/recortes-luna-3/_partes/<pasta>/<id>/,
junte no arquivo final conteudo/entrada/recortes-luna-3/<pasta>/<id>.json (nome = id da matéria) e valide.
Pule itens que já existam em conteudo/recortes/ ou em conteudo/entrada/recortes-luna-3/.
Se um item não der certo, anote em conteudo/entrada/recortes-luna-3/PENDENCIAS.md e siga.
Não pare para perguntar. Se o limite acabar, eu peço para continuar.
```

## Para cada item

1. Leia a matéria inteira em `conteudo/materias/<dir>/<arq>.json` (o id "a.b" vira a/b.json). As questões só cobram o
   que está nessa teoria. Modelo de formato: `conteudo/recortes/fgv/portugues.coesao-textual.json`.
2. Cabeçalho: `{"topic":"<id>","board":"<Banca EXATAMENTE como na coluna Banca da tabela, sem abreviar>","role":null,
   "version":1,"incidence":"ALTA|MEDIA|BAIXA","howItFalls":"400 a 2.500 caracteres sobre como ESSA banca cobra essa
   matéria, com 2 exemplos de enunciado escritos por você; sem estatística inventada","tips":[3 a 8],"traps":[3 a 8],
   "questions":[40]}`
3. Cada questão: `{"topico":"<título da matéria>","estilo":"<Banca>","statement","format","difficulty","options",
   "explanation","section":"<título EXATO de um capítulo>","errorConceptKey":"<chave da matéria ou null>",
   "sourceType":"AUTHORIAL","sourceUrl":null,"board":null,"agency":null,"year":null}`

## Regras das questões (todas obrigatórias)

- **Cebraspe e Quadrix:** todas Certo/Errado ("TRUE_FALSE", opções `{"key":"C","text":"Certo"}` e
  `{"key":"E","text":"Errado"}`), **exatamente 20 Certo e 20 Errado**. O item Errado troca UM detalhe plausível.
- **Demais bancas:** múltipla escolha com 5 alternativas (A–E), cada uma com `"comentario"` curto do erro dela.
- **As cinco alternativas no MESMO formato e tamanho parecido.** Se a correta é só um valor ("R$ 2.240,00."), as erradas
  também são só valores; a justificativa de cada uma vai no `comentario`, nunca no texto da alternativa.
  A correta não pode ser a mais longa em mais de 12 das 40 **nem a mais curta em mais de 12 das 40**. Confira antes de gravar.
- **Nunca cite "a matéria", "a teoria", "o texto estudado", "conforme apresentado"**: a questão tem de se sustentar
  sozinha, como numa prova de verdade.
- **Uma única correta.** Confira se nenhuma alternativa errada é também verdadeira (inclusive com outro valor equivalente
  ou forma que a norma aceita dos dois jeitos). Na dúvida, troque a alternativa.
- **Alternativas erradas plausíveis**, com erros que candidatos de verdade cometem. Proibido número impossível
  ("2,5 pessoas", "14h90"), negativo quando se pede inteiro positivo, ou alternativa absurda.
- **Enunciado que não entrega a resposta** (não escreva a regra e depois pergunte a regra) e que seja uma pergunta.
- Imite a banca: FGV com casos e trechos; FCC com "Considere..." e assertivas; Vunesp direta; Consulpam, Acesse, IBGP,
  IBAM e Selecon com enunciados diretos e assertivas. Trecho, quando houver, é um parágrafo real de 3 a 5 linhas.
- Nenhum tipo de comando em mais de 8 das 40; nenhuma ideia repetida mais de 2 vezes; nenhum enunciado igual a outro.
- 12 FÁCIL, 16 MÉDIA, 12 DIFÍCIL. Explicação de 150 a 400 caracteres, **sem citar letra** ("a alternativa B", "C está correta").
- Matemática e raciocínio lógico: resolva cada questão antes de gravar; o valor de cada alternativa errada tem de bater
  com o erro descrito no `comentario`.
- Legislação local (estado, município, órgão): só números de lei, artigo, prazo e valor que estejam na matéria. Nunca
  invente norma.

## Validar até dar OK

`deno run --allow-read --allow-env scripts/biblioteca/conferir-recorte.ts conteudo/entrada/recortes-luna-3/<pasta>/<id>.json`

## Lista

| # | Banca | Pasta | id da matéria | Título | Editais |
|---|---|---|---|---|---|
| 1 | Idecan | idecan | legislacao-municipal.lei-organica-municipal | Lei Orgânica Municipal: estrutura e conteúdo típico | 64 |
| 2 | Fundatec | fundatec | portugues.coerencia-textual | Coerência textual | 54 |
| 3 | Fundatec | fundatec | portugues.morfologia-estrutura-formacao-classes | Morfologia completa: estrutura, formação, classes e flexões | 54 |
| 4 | IBGP | ibgp | portugues.concordancia | Concordância verbal e nominal | 53 |
| 5 | IBGP | ibgp | portugues.regencia-verbal-nominal | Regência verbal e nominal | 53 |
| 6 | IBGP | ibgp | portugues.crase | Crase | 53 |
| 7 | IBGP | ibgp | portugues.colocacao-pronominal | Colocação pronominal | 53 |
| 8 | Instituto Quadrix | instituto-quadrix | portugues.interpretacao-textual | Interpretação de textos | 53 |
| 9 | Fundatec | fundatec | legislacao.lei-8429-1992-improbidade | Lei de Improbidade Administrativa (Lei 8.429/1992) | 48 |
| 10 | Instituto Avalia | instituto-avalia | portugues.funcoes-que-se | Funções de que e se | 48 |
| 11 | Fundatec | fundatec | direito-constitucional.administracao-publica | Administração pública | 48 |
| 12 | FGV | fgv | legislacao-municipal.lei-organica-municipal | Lei Orgânica Municipal: estrutura e conteúdo típico | 48 |
| 13 | Instituto AOCP | aocp | saude.cuidados-paliativos-bioetica | Cuidados paliativos e bioética | 48 |
| 14 | IDIB | idib | matematica.mmc-mdc-divisibilidade | MMC, MDC e divisibilidade | 47 |
| 15 | Instituto Consulplan | consulplan | portugues.coesao-textual | Coesão textual | 47 |
| 16 | Instituto Consulplan | consulplan | matematica.geometria-espacial-volumes | Geometria espacial: áreas e volumes | 47 |
| 17 | IBGP | ibgp | saude-publica.epidemiologia-vigilancia | Epidemiologia e vigilância em saúde | 47 |
| 18 | Fundatec | fundatec | legislacao.lei-8112-1990-visao-geral | Lei 8.112/1990: visão geral do regime jurídico | 47 |
| 19 | Fundatec | fundatec | etica.codigo-etica-servidor-federal | Código de Ética do servidor público civil federal | 47 |
| 20 | Instituto Avalia | instituto-avalia | matematica.quantificadores-diagramas | Quantificadores e diagramas | 46 |
| 21 | Instituto Avalia | instituto-avalia | legislacao.lei-8429-1992-improbidade | Lei de Improbidade Administrativa (Lei 8.429/1992) | 46 |
| 22 | Acesse Concursos | acesse-concursos | portugues.interpretacao-textual | Interpretação de textos | 46 |
| 23 | Acesse Concursos | acesse-concursos | portugues.significacao-contextual-palavras-expressoes | Significação contextual de palavras e expressões | 46 |
| 24 | Acesse Concursos | acesse-concursos | portugues.coesao-textual | Coesão textual | 46 |
| 25 | Acesse Concursos | acesse-concursos | portugues.ortografia | Ortografia oficial: emprego das letras | 46 |
| 26 | Acesse Concursos | acesse-concursos | portugues.acentuacao | Acentuação gráfica | 46 |
| 27 | Acesse Concursos | acesse-concursos | portugues.classes-de-palavras | Classes de palavras | 46 |
| 28 | Acesse Concursos | acesse-concursos | portugues.verbos | Emprego e correlação de tempos e modos verbais | 46 |
| 29 | Acesse Concursos | acesse-concursos | portugues.formacao-de-palavras | Formação de palavras | 46 |
| 30 | Acesse Concursos | acesse-concursos | portugues.termos-da-oracao | Termos da oração | 46 |
| 31 | Acesse Concursos | acesse-concursos | portugues.frase-oracao-periodo | Frase, oração e período | 46 |
| 32 | Acesse Concursos | acesse-concursos | portugues.concordancia | Concordância verbal e nominal | 46 |
| 33 | Acesse Concursos | acesse-concursos | portugues.pontuacao | Pontuação | 46 |
| 34 | Acesse Concursos | acesse-concursos | portugues.reescrita | Reescrita de frases e parágrafos | 46 |
| 35 | Acesse Concursos | acesse-concursos | matematica.numeros-racionais | Números racionais: frações e decimais | 46 |
| 36 | Acesse Concursos | acesse-concursos | matematica.razao-proporcao | Razão e proporção | 46 |
| 37 | Acesse Concursos | acesse-concursos | matematica.regra-de-tres | Regra de três simples e composta | 46 |
| 38 | Acesse Concursos | acesse-concursos | matematica.porcentagem | Porcentagem e variação percentual | 46 |
| 39 | Acesse Concursos | acesse-concursos | matematica.sistema-legal-medidas | Sistema Legal de Medidas | 46 |
| 40 | Acesse Concursos | acesse-concursos | raciocinio-logico.problemas-aritmeticos | Problemas aritméticos, geométricos e matriciais | 46 |
| 41 | Acesse Concursos | acesse-concursos | matematica.estatistica-descritiva | Estatística descritiva | 46 |
| 42 | Acesse Concursos | acesse-concursos | matematica.geometria-plana | Geometria plana | 46 |
| 43 | Acesse Concursos | acesse-concursos | matematica.sequencias-logicas | Sequências lógicas | 46 |
| 44 | Acesse Concursos | acesse-concursos | raciocinio-logico.relacoes-arbitrarias | Estrutura lógica de relações e dedução | 46 |
| 45 | Acesse Concursos | acesse-concursos | matematica.graficos-tabelas-mapas-escalas | Leitura de gráficos, tabelas, mapas e escalas | 46 |
| 46 | Acesse Concursos | acesse-concursos | raciocinio-logico.analise-situacoes | Compreensão e análise lógica de situações | 46 |
| 47 | IBAM | ibam | matematica.porcentagem | Porcentagem e variação percentual | 45 |
| 48 | Fundatec | fundatec | educacao.ldb-bncc-didatica | LDB, BNCC e didática | 45 |
| 49 | Fundatec | fundatec | legislacao.pessoa-com-deficiencia-acessibilidade-leis-esparsas | Pessoa com deficiência: acessibilidade, avaliação e leis esparsas | 45 |
| 50 | Instituto Avalia | instituto-avalia | direito-administrativo.licitacao-principios-modalidades | Licitação na Lei 14.133/2021: conceito, princípios e modalidades | 45 |
| 51 | Instituto Avalia | instituto-avalia | portugues.figuras-de-linguagem | Figuras de linguagem | 44 |
| 52 | Instituto Consulpam | instituto-consulpam | portugues.coesao-textual | Coesão textual | 44 |
| 53 | Instituto Consulpam | instituto-consulpam | portugues.significacao-contextual-palavras-expressoes | Significação contextual de palavras e expressões | 44 |
| 54 | Instituto Consulpam | instituto-consulpam | portugues.verbos | Emprego e correlação de tempos e modos verbais | 44 |
| 55 | Instituto Consulpam | instituto-consulpam | portugues.fonologia-fonemas | Fonologia, fonemas e divisão silábica | 44 |
| 56 | Instituto Consulpam | instituto-consulpam | portugues.termos-da-oracao | Termos da oração | 44 |
| 57 | Instituto Consulpam | instituto-consulpam | portugues.subordinacao | Subordinação entre orações e entre termos | 44 |
| 58 | Instituto Consulpam | instituto-consulpam | portugues.figuras-de-linguagem | Figuras de linguagem | 44 |
| 59 | Instituto Consulpam | instituto-consulpam | portugues.reescrita | Reescrita de frases e parágrafos | 44 |
| 60 | Instituto Consulpam | instituto-consulpam | direito-constitucional.principios-fundamentais | Princípios fundamentais (arts. 1º a 4º) | 44 |
| 61 | Instituto Consulpam | instituto-consulpam | direito-constitucional.direitos-individuais-coletivos | Direitos e deveres individuais e coletivos (art. 5º) | 44 |
| 62 | Instituto Consulpam | instituto-consulpam | direito-constitucional.organizacao-politico-administrativa | Organização político-administrativa e entes federativos | 44 |
| 63 | Instituto Consulpam | instituto-consulpam | direito-administrativo.agentes-publicos | Agentes públicos: classificação, cargo, emprego e função | 44 |
| 64 | Instituto Consulpam | instituto-consulpam | direito-administrativo.organizacao-administrativa | Organização administrativa: direta, indireta e órgãos públicos | 44 |
| 65 | Instituto Consulpam | instituto-consulpam | direito-administrativo.ato-administrativo | Ato administrativo | 44 |
| 66 | Instituto Consulpam | instituto-consulpam | direito-administrativo.poderes-administrativos | Poderes administrativos e abuso de poder | 44 |
| 67 | Instituto Consulpam | instituto-consulpam | direito-administrativo.licitacao-principios-modalidades | Licitação na Lei 14.133/2021: conceito, princípios e modalidades | 44 |
| 68 | Instituto Consulpam | instituto-consulpam | legislacao.lei-8429-1992-improbidade | Lei de Improbidade Administrativa (Lei 8.429/1992) | 44 |
| 69 | Instituto Consulpam | instituto-consulpam | informatica.windows-10-11 | Windows 10 e 11: recursos básicos | 44 |
| 70 | Instituto Consulpam | instituto-consulpam | informatica.aplicativos-escritorio-office-libreoffice | Aplicativos de escritório: Microsoft Office e LibreOffice (texto, planilhas e apresentações) | 44 |
| 71 | Instituto Consulpam | instituto-consulpam | informatica.redes-computadores | Redes de computadores | 44 |
| 72 | Instituto Consulpam | instituto-consulpam | informatica.internet-busca-redes-sociais | Internet: busca, redes sociais, grupos de discussão e wikis | 44 |
| 73 | Instituto Consulpam | instituto-consulpam | informatica.correio-eletronico-outlook-2016-navegacao | Correio eletrônico (Outlook 2016) e navegação (Google Chrome) | 44 |
| 74 | Instituto Consulpam | instituto-consulpam | informatica.ferramentas-colaborativas | Ferramentas colaborativas e nuvem para o usuário | 44 |
| 75 | Instituto Consulpam | instituto-consulpam | informatica.computacao-nuvem | Computação em nuvem | 44 |
| 76 | Instituto Consulpam | instituto-consulpam | informatica.seguranca | Segurança | 44 |
| 77 | Fundatec | fundatec | legislacao-penal-especial.maria-da-penha | Lei Maria da Penha | 44 |
| 78 | IBAM | ibam | matematica.razao-proporcao | Razão e proporção | 44 |
| 79 | IBAM | ibam | matematica.regra-de-tres | Regra de três simples e composta | 44 |
| 80 | IBAM | ibam | matematica.estatistica-descritiva | Estatística descritiva | 44 |
| 81 | Fundatec | fundatec | saude-publica.equidade-genero-raca-trabalhadoras-sus | Equidade de gênero, raça e valorização das trabalhadoras no SUS — Portaria 230/2023 | 44 |
| 82 | FGV | fgv | legislacao-penal-especial.estatuto-desarmamento | Estatuto do Desarmamento | 43 |
| 83 | Instituto AOCP | aocp | matematica.probabilidade | Probabilidade | 43 |
| 84 | Instituto AOCP | aocp | matematica.logica-proposicional | Lógica proposicional e tabela-verdade | 43 |
| 85 | Instituto Consulpam | instituto-consulpam | direito-administrativo.principios-administracao | Princípios da Administração Pública | 43 |
| 86 | Fundatec | fundatec | legislacao.eca-direitos-fundamentais | ECA: direitos fundamentais, medidas de proteção e Conselho Tutelar | 43 |
| 87 | IBAM | ibam | matematica.graficos-tabelas-mapas-escalas | Leitura de gráficos, tabelas, mapas e escalas | 43 |
| 88 | IDIB | idib | informatica.seguranca | Segurança | 42 |
| 89 | Cesgranrio | cesgranrio | portugues.termos-da-oracao | Termos da oração | 42 |
| 90 | Instituto Consulplan | consulplan | informatica.sistema-operacional-windows | Sistema Operacional Windows 10 | 42 |
| 91 | Vunesp | vunesp | direito-constitucional.direitos-individuais-coletivos | Direitos e deveres individuais e coletivos (art. 5º) | 42 |
| 92 | Fundatec | fundatec | direito-constitucional.ordem-social | Ordem social na Constituição | 42 |
| 93 | Fundatec | fundatec | administracao-publica.lei-acesso-informacao | Lei de Acesso à Informação | 42 |
| 94 | Fundatec | fundatec | saude-publica.epidemiologia-vigilancia | Epidemiologia e vigilância em saúde | 42 |
| 95 | Cebraspe | cebraspe | educacao.eja-educacao-campo | Educação de jovens e adultos e educação do campo | 42 |
| 96 | Cebraspe | cebraspe | direito-trabalho.suspensao-interrupcao | Suspensão e interrupção do contrato | 41 |
| 97 | FCC | fcc | direito-administrativo.parcerias-publico-privadas | Parcerias público-privadas (Lei 11.079/2004) | 41 |
| 98 | FCC | fcc | direito-constitucional.organizacao-poderes | Organização dos Poderes e separação das funções | 41 |
| 99 | Cesgranrio | cesgranrio | seguranca-trabalho.gestao-sst-investigacao-acidentes-controle-perdas | Gestão de SST, investigação de acidentes e controle de perdas | 41 |
| 100 | Instituto Avalia | instituto-avalia | matematica.logica-proposicional | Lógica proposicional e tabela-verdade | 41 |
| 101 | Instituto Consulplan | consulplan | informatica.seguranca | Segurança | 41 |
| 102 | IBAM | ibam | matematica.probabilidade | Probabilidade | 41 |
| 103 | IBAM | ibam | informatica.microsoft-excel-2016 | Microsoft Excel 2016 | 41 |
| 104 | IBAM | ibam | informatica.seguranca | Segurança | 41 |
| 105 | Instituto Selecon | instituto-selecon | informatica.microsoft-excel-2016 | Microsoft Excel 2016 | 41 |
| 106 | Instituto Selecon | instituto-selecon | informatica.internet-navegadores | Internet e navegadores | 41 |
| 107 | IBFC | ibfc | portugues.concordancia | Concordância verbal e nominal | 41 |
| 108 | IBFC | ibfc | portugues.regencia-verbal-nominal | Regência verbal e nominal | 41 |
| 109 | IBFC | ibfc | informatica.windows-10-11 | Windows 10 e 11: recursos básicos | 41 |
| 110 | Fundatec | fundatec | direito-constitucional.organizacao-poderes | Organização dos Poderes e separação das funções | 41 |
| 111 | Fundatec | fundatec | direito-constitucional.defesa-estado-instituicoes | Defesa do Estado e das Instituições Democráticas | 41 |
| 112 | IBAM | ibam | matematica.numeros-racionais | Números racionais: frações e decimais | 41 |
| 113 | IBAM | ibam | matematica.sistema-legal-medidas | Sistema Legal de Medidas | 41 |
| 114 | Fundatec | fundatec | informatica.windows-10-11 | Windows 10 e 11: recursos básicos | 41 |
| 115 | Fundatec | fundatec | administracao-geral.comportamento-organizacional | Comportamento organizacional: motivação, liderança, grupos, conflito, poder e mudança | 41 |
| 116 | Fundatec | fundatec | educacao.diversidade-inclusao-temas-transversais | Educação inclusiva, diversidade e temas transversais | 41 |
| 117 | Cebraspe | cebraspe | direito-trabalho.aviso-previo | Aviso prévio | 40 |
| 118 | FGV | fgv | portugues.retextualizacao | Retextualização de gêneros e níveis de formalidade | 40 |
| 119 | Cebraspe | cebraspe | estatistica.probabilidade-avancada | Probabilidade avançada e distribuições discretas | 40 |
| 120 | Instituto Avalia | instituto-avalia | portugues.variacao-linguistica-norma-linguistica | Variação linguística e norma linguística | 40 |
| 121 | IBAM | ibam | informatica.windows-10-11 | Windows 10 e 11: recursos básicos | 40 |
| 122 | IBAM | ibam | informatica.microsoft-word-2016 | Microsoft Word 2016 | 40 |
| 123 | IBAM | ibam | informatica.internet-navegadores | Internet e navegadores | 40 |
| 124 | IBAM | ibam | informatica.correio-eletronico-outlook-2016-navegacao | Correio eletrônico (Outlook 2016) e navegação (Google Chrome) | 40 |
| 125 | Vunesp | vunesp | direito-constitucional.administracao-publica | Administração pública | 40 |
| 126 | Instituto Consulplan | consulplan | raciocinio-logico.analise-situacoes | Compreensão e análise lógica de situações | 40 |
| 127 | Fundatec | fundatec | administracao-publica.planejamento-estrategico | Planejamento estratégico, BSC, SWOT e indicadores | 40 |
| 128 | Vunesp | vunesp | enfermagem.saude-ciclos-vida | Enfermagem na saúde da mulher, da criança e do idoso | 40 |
| 129 | Vunesp | vunesp | saude-publica.vigilancia-ambiental-zoonoses | Vigilância ambiental e controle de vetores | 40 |
| 130 | Fundatec | fundatec | direito.assedio-discriminacao-genero-judiciario | Assédio, discriminação e perspectiva de gênero no Judiciário | 40 |
| 131 | IBAM | ibam | saude-publica.politicas-nacionais-saude | Políticas Nacionais de Saúde: Atenção Básica, Promoção, Humanização e Redes | 40 |
| 132 | FGV | fgv | espirito-santo.mpes-lei-organica-lc-95-1997 | Lei Orgânica do MPES — LC 95/1997, organização e atribuições | 40 |
| 133 | FGV | fgv | espirito-santo.mpes-lei-7233-2002-carreiras | Carreiras dos servidores do MPES — Lei 7.233/2002 | 40 |
| 134 | FGV | fgv | espirito-santo.mpes-privacidade-protecao-dados | Privacidade e proteção de dados no MPES — Portaria 897/2023 | 40 |
| 135 | FGV | fgv | espirito-santo.mpes-estrategia-governanca | Estratégia e governança do MPES — Portaria 8.565/2017 | 40 |
| 136 | FGV | fgv | espirito-santo.mpes-planejamento-estrategico-2024-2032 | Planejamento Estratégico 2024–2032 do MPES | 40 |
| 137 | FGV | fgv | espirito-santo.ministerio-publico-ods-agenda-2030 | Ministério Público e ODS da Agenda 2030 — abordagem institucional exigida | 40 |
| 138 | FGV | fgv | espirito-santo.mpes-estrutura-organizacional | Estrutura organizacional do MPES — Resolução 12/2012 | 40 |
| 139 | IBGP | ibgp | portugues.discurso-modalizadores-atos-fala | Discurso, modalização e atos de fala | 40 |
| 140 | IBGP | ibgp | portugues.argumentacao-adequacao-textual | Argumentação, pertinência e adequação textual | 40 |
| 141 | IBGP | ibgp | portugues.pronomes-relativos-funcoes | Pronomes relativos e funções sintáticas | 40 |
| 142 | IBGP | ibgp | informatica.sistemas-saude-ti | Informática em saúde | 40 |
| 143 | Instituto Quadrix | instituto-quadrix | educacao.tendencias-pedagogicas-politicas-educacionais | Tendências pedagógicas, políticas educacionais e coordenação pedagógica | 40 |
| 144 | Instituto Quadrix | instituto-quadrix | educacao.diversidade-inclusao-temas-transversais | Educação inclusiva, diversidade e temas transversais | 40 |
| 145 | Instituto Quadrix | instituto-quadrix | educacao.ead-tecnologias-andragogia | Educação de adultos, EaD e tecnologias educacionais | 40 |
| 146 | IDIB | idib | informatica.windows-10-11 | Windows 10 e 11: recursos básicos | 39 |
| 147 | IDIB | idib | informatica.microsoft-excel-2016 | Microsoft Excel 2016 | 39 |
| 148 | IDIB | idib | informatica.microsoft-powerpoint-2016 | Microsoft PowerPoint 2016 | 39 |
| 149 | FGV | fgv | administracao-publica.gestao-participativa-resultados-comunicacao | Gestão Pública: Participação, Resultados e Comunicação | 39 |
| 150 | FCC | fcc | direito.processo-legislativo-estadual | Processo legislativo e Poder Legislativo estadual | 39 |
| 151 | FCC | fcc | roraima.alerr-codigo-etica-parlamentar | Código de Ética Parlamentar da ALE/RR (Resolução 29/1995) | 39 |
| 152 | FCC | fcc | roraima.alerr-resolucao-15-2024-estrutura | Estrutura administrativa da ALE/RR: Resolução Legislativa 15/2024 | 39 |
| 153 | FCC | fcc | roraima.conhecimentos-estaduais | Roraima: história, geografia e economia | 39 |
| 154 | Cesgranrio | cesgranrio | direito.assedio-discriminacao-genero-judiciario | Assédio, discriminação e perspectiva de gênero no Judiciário | 39 |
| 155 | Instituto Avalia | instituto-avalia | portugues.subordinacao | Subordinação entre orações e entre termos | 39 |
| 156 | IBAM | ibam | informatica.arquivos-digitais-backup | Arquivos digitais, transferência e backup | 39 |
| 157 | IBAM | ibam | informatica.microsoft-powerpoint-2016 | Microsoft PowerPoint 2016 | 39 |
| 158 | IBAM | ibam | informatica.ferramentas-colaborativas | Ferramentas colaborativas e nuvem para o usuário | 39 |
| 159 | Instituto AOCP | aocp | portugues.subordinacao | Subordinação entre orações e entre termos | 39 |
| 160 | Instituto AOCP | aocp | matematica.progressoes-pa-pg | Progressões aritméticas e geométricas | 39 |
| 161 | Instituto Selecon | instituto-selecon | portugues.interpretacao-textual | Interpretação de textos | 39 |
| 162 | Instituto Selecon | instituto-selecon | portugues.acentuacao | Acentuação gráfica | 39 |
| 163 | Instituto Selecon | instituto-selecon | portugues.coesao-textual | Coesão textual | 39 |
| 164 | Instituto Selecon | instituto-selecon | portugues.pontuacao | Pontuação | 39 |
| 165 | Instituto Selecon | instituto-selecon | portugues.crase | Crase | 39 |
| 166 | Vunesp | vunesp | saude-publica.saude-mental-raps-internacao | Saúde mental: RAPS, política nacional e internação psiquiátrica | 39 |
| 167 | Vunesp | vunesp | saude-publica.epidemiologia-clinica-evidencias-ats | Epidemiologia clínica, revisão sistemática e avaliação de tecnologias em saúde | 39 |
| 168 | Fundatec | fundatec | legislacao.lei-12772-magisterio-federal | Carreira do Magistério Federal (Lei 12.772/2012) | 39 |
| 169 | Fundatec | fundatec | educacao.educacao-profissional-tecnologica | Educação profissional e tecnológica e currículo integrado | 39 |
| 170 | Fundatec | fundatec | educacao.proeja-decreto-5840 | PROEJA — Decreto 5.840/2006 | 39 |
| 171 | Acesse Concursos | acesse-concursos | portugues.generos-textuais | Gêneros textuais e tipos textuais | 39 |
| 172 | Acesse Concursos | acesse-concursos | portugues.coerencia-textual | Coerência textual | 39 |
| 173 | Acesse Concursos | acesse-concursos | portugues.coordenacao | Coordenação entre orações e entre termos | 39 |
| 174 | Acesse Concursos | acesse-concursos | portugues.subordinacao | Subordinação entre orações e entre termos | 39 |
| 175 | Acesse Concursos | acesse-concursos | portugues.regencia-verbal-nominal | Regência verbal e nominal | 39 |
| 176 | Acesse Concursos | acesse-concursos | portugues.crase | Crase | 39 |
| 177 | Acesse Concursos | acesse-concursos | portugues.colocacao-pronominal | Colocação pronominal | 39 |
| 178 | Acesse Concursos | acesse-concursos | portugues.pronomes | Emprego e colocação de pronomes | 39 |
| 179 | Acesse Concursos | acesse-concursos | portugues.vozes-verbais | Vozes verbais | 39 |
| 180 | Acesse Concursos | acesse-concursos | portugues.discurso-modalizadores-atos-fala | Discurso, modalização e atos de fala | 39 |
| 181 | Acesse Concursos | acesse-concursos | portugues.figuras-de-linguagem | Figuras de linguagem | 39 |
| 182 | Acesse Concursos | acesse-concursos | portugues.funcoes-linguagem-teoria | História da língua, níveis e funções da linguagem | 39 |
| 183 | Acesse Concursos | acesse-concursos | portugues.variacao-linguistica-norma-linguistica | Variação linguística e norma linguística | 39 |
| 184 | Acesse Concursos | acesse-concursos | matematica.juros-simples-compostos | Juros simples e compostos | 39 |
| 185 | Acesse Concursos | acesse-concursos | matematica.equacoes-1-2-grau | Equações do 1º e do 2º grau | 39 |
| 186 | Acesse Concursos | acesse-concursos | matematica.analise-combinatoria | Análise combinatória | 39 |
| 187 | Acesse Concursos | acesse-concursos | matematica.probabilidade | Probabilidade | 39 |
| 188 | Acesse Concursos | acesse-concursos | matematica.logica-proposicional | Lógica proposicional e tabela-verdade | 39 |
| 189 | Acesse Concursos | acesse-concursos | matematica.argumentacao-logica | Argumentação lógica | 39 |
| 190 | Acesse Concursos | acesse-concursos | matematica.quantificadores-diagramas | Quantificadores e diagramas | 39 |
| 191 | IBAM | ibam | saude-publica.sus-legislacao | SUS: legislação, princípios e controle social | 39 |
| 192 | IBAM | ibam | saude-publica.epidemiologia-vigilancia | Epidemiologia e vigilância em saúde | 39 |
| 193 | Instituto Avalia | instituto-avalia | direito-constitucional.administracao-publica | Administração pública | 39 |
| 194 | Instituto Avalia | instituto-avalia | legislacao.lei-9784-1999-processo-administrativo | Processo administrativo federal (Lei 9.784/1999) | 39 |
| 195 | Instituto Quadrix | instituto-quadrix | educacao.interdisciplinaridade-curriculo-planejamento-pedagogico | Interdisciplinaridade, currículo e planejamento do trabalho pedagógico | 39 |
| 196 | Instituto Quadrix | instituto-quadrix | meio-ambiente.gestao-licenciamento-unidades-conservacao | Gestão ambiental: licenciamento, unidades de conservação, recursos naturais e educação ambiental | 39 |
| 197 | Instituto Quadrix | instituto-quadrix | educacao.psicologia-aprendizagem-didatica | Psicologia da Aprendizagem e Didática | 39 |
| 198 | Instituto Consulplan | consulplan | legislacao-municipal.lei-organica-municipal | Lei Orgânica Municipal: estrutura e conteúdo típico | 38 |
| 199 | IBAM | ibam | portugues.termos-da-oracao | Termos da oração | 38 |
| 200 | Instituto AOCP | aocp | etica.cidadania-democracia | Ética, democracia e exercício da cidadania | 38 |
| 201 | Instituto Selecon | instituto-selecon | portugues.verbos | Emprego e correlação de tempos e modos verbais | 38 |
| 202 | Instituto Selecon | instituto-selecon | portugues.classes-de-palavras | Classes de palavras | 38 |
| 203 | Instituto Selecon | instituto-selecon | informatica.microsoft-word-2016 | Microsoft Word 2016 | 38 |
| 204 | Instituto Selecon | instituto-selecon | informatica.microsoft-powerpoint-2016 | Microsoft PowerPoint 2016 | 38 |
| 205 | Instituto Consulplan | consulplan | administracao-publica.planejamento-estrategico | Planejamento estratégico, BSC, SWOT e indicadores | 38 |
| 206 | Instituto Avalia | instituto-avalia | direito-administrativo.organizacao-administrativa | Organização administrativa: direta, indireta e órgãos públicos | 38 |
| 207 | Vunesp | vunesp | portugues.ortografia | Ortografia oficial: emprego das letras | 38 |
| 208 | Vunesp | vunesp | saude.seguranca-paciente-qualidade | Segurança do paciente e qualidade em saúde | 38 |
| 209 | Vunesp | vunesp | ciencias-sociais.antropologia-teoria-social | Antropologia e teoria social clássica | 38 |
| 210 | Fundatec | fundatec | controle-externo.cgu-controle-interno-federal | CGU e auditoria interna do Poder Executivo federal | 38 |
| 211 | IBFC | ibfc | legislacao-municipal.estatuto-servidor-municipal-estadual | Estatuto do servidor municipal e estadual: conteúdo típico | 38 |
| 212 | Fundatec | fundatec | legislacao-municipal.administracao-municipal | Administração municipal: organização, carreira e posturas | 38 |
| 213 | Fundatec | fundatec | legislacao-estadual.constituicoes-estaduais | Constituições estaduais: estrutura e simetria | 38 |
| 214 | IBFC | ibfc | legislacao-municipal.lei-organica-municipal | Lei Orgânica Municipal: estrutura e conteúdo típico | 38 |
| 215 | IBFC | ibfc | saude-publica.epidemiologia-vigilancia | Epidemiologia e vigilância em saúde | 38 |
| 216 | Instituto Consulplan | consulplan | saude.educacao-em-saude | Educação em saúde e educação popular em saúde | 38 |
| 217 | Cesgranrio | cesgranrio | portugues.verbos | Emprego e correlação de tempos e modos verbais | 37 |
| 218 | Cebraspe | cebraspe | distrito-federal.ii-plano-distrital-mulheres | II Plano Distrital de Política para Mulheres | 37 |
| 219 | Cebraspe | cebraspe | direito-administrativo.jurisprudencia-tribunais-superiores | Jurisprudência dos Tribunais Superiores em Direito Administrativo | 37 |
| 220 | Instituto AOCP | aocp | portugues.coordenacao | Coordenação entre orações e entre termos | 37 |
| 221 | Instituto AOCP | aocp | matematica.geometria-plana | Geometria plana | 37 |
| 222 | Instituto AOCP | aocp | santa-catarina.conhecimentos-estaduais | História e Geografia de Santa Catarina | 37 |
| 223 | Instituto AOCP | aocp | santa-catarina.casan-codigos-politicas | Códigos e políticas da CASAN exigidos no edital | 37 |
| 224 | Instituto AOCP | aocp | saude.controle-infeccao-esterilizacao | Controle de infecção, esterilização e microbiologia | 37 |
| 225 | Instituto Selecon | instituto-selecon | portugues.ortografia | Ortografia oficial: emprego das letras | 37 |
| 226 | Instituto Selecon | instituto-selecon | portugues.formacao-de-palavras | Formação de palavras | 37 |
| 227 | Instituto Selecon | instituto-selecon | portugues.significacao-contextual-palavras-expressoes | Significação contextual de palavras e expressões | 37 |
| 228 | Instituto Selecon | instituto-selecon | portugues.termos-da-oracao | Termos da oração | 37 |
| 229 | Instituto Selecon | instituto-selecon | direito-constitucional.administracao-publica | Administração pública | 37 |
| 230 | Instituto Avalia | instituto-avalia | direito-administrativo.principios-administracao | Princípios da Administração Pública | 37 |
| 231 | Vunesp | vunesp | portugues.acentuacao | Acentuação gráfica | 37 |
| 232 | Vunesp | vunesp | medicina.pediatria-neonatologia | Pediatria e neonatologia: crescimento, desenvolvimento, recém-nascido e doenças comuns da infância | 37 |
| 233 | Vunesp | vunesp | saude-publica.bioestatistica | Bioestatística aplicada à saúde | 37 |
| 234 | Fundatec | fundatec | rio-grande-do-sul.igualdade-racial | Igualdade racial no Rio Grande do Sul — Lei 13.694/2011 e Decreto 48.598/2011 | 37 |
| 235 | IBGP | ibgp | medicina.saude-trabalhador-agravos-ocupacionais | Medicina do trabalho: fisiologia, agentes e agravos ocupacionais | 37 |
| 236 | IBFC | ibfc | minas-gerais.belo-horizonte-codigo-etica | Ética do agente público de Belo Horizonte — Decreto 14.635/2011 | 37 |
| 237 | IBFC | ibfc | minas-gerais.belo-horizonte-lgpd-decreto | LGPD na administração de Belo Horizonte — Decreto 18.608/2024 | 37 |
| 238 | IBFC | ibfc | saude-publica.politicas-nacionais-saude | Políticas Nacionais de Saúde: Atenção Básica, Promoção, Humanização e Redes | 37 |
| 239 | FGV | fgv | direito-tributario.simples-nacional-lc-123 | Simples Nacional (Lei Complementar nº 123/2006) | 36 |
| 240 | FGV | fgv | direito-empresarial.propriedade-industrial-direitos-autorais | Propriedade industrial e direitos autorais (Leis 9.279/1996, 9.610/1998 e 9.609/1998) | 36 |
| 241 | FGV | fgv | direito-ambiental.recursos-hidricos-lei-9433 | Recursos Hídricos: Política Nacional e SINGREH (Lei 9.433/1997) | 36 |
| 242 | Cesgranrio | cesgranrio | portugues.coordenacao | Coordenação entre orações e entre termos | 36 |
| 243 | Cesgranrio | cesgranrio | portugues.subordinacao | Subordinação entre orações e entre termos | 36 |
| 244 | Instituto AOCP | aocp | matematica.trigonometria | Trigonometria | 36 |
| 245 | Instituto Selecon | instituto-selecon | portugues.coerencia-textual | Coerência textual | 36 |
| 246 | Instituto Selecon | instituto-selecon | portugues.pronomes | Emprego e colocação de pronomes | 36 |
| 247 | Instituto Selecon | instituto-selecon | direito-constitucional.ordem-social | Ordem social na Constituição | 36 |
| 248 | FCC | fcc | matematica.mmc-mdc-divisibilidade | MMC, MDC e divisibilidade | 36 |
| 249 | Fundatec | fundatec | etica.etica-moral-funcao-publica | Ética, moral e função pública | 36 |
| 250 | Cebraspe | cebraspe | educacao.sinaes-docencia-ensino-superior | Avaliação da educação superior (SINAES, Lei 10.861/2004) e docência no ensino superior | 36 |
| 251 | FGV | fgv | direito-ambiental.florestas-codigo-florestal-mata-atlantica | Proteção da Vegetação: Código Florestal, Mata Atlântica e Florestas Públicas | 35 |
| 252 | Cebraspe | cebraspe | seguranca-trabalho.normas-regulamentadoras | Normas Regulamentadoras (NRs) de segurança e saúde no trabalho | 35 |
| 253 | Cebraspe | cebraspe | direito-tributario.simples-nacional-lc-123 | Simples Nacional (Lei Complementar nº 123/2006) | 35 |
| 254 | Instituto AOCP | aocp | portugues.pronomes | Emprego e colocação de pronomes | 35 |
| 255 | Instituto AOCP | aocp | legislacao-estadual.constituicoes-estaduais | Constituições estaduais: estrutura e simetria | 35 |
| 256 | Instituto AOCP | aocp | santa-catarina.fundo-estadual-saneamento-decreto-3253 | Decreto 3.253/2010 de SC: Fundo Estadual de Saneamento | 35 |
| 257 | IBFC | ibfc | portugues.crase | Crase | 35 |
| 258 | FGV | fgv | saude-publica.pessoa-deficiencia-pnaispd-rcpd | Política e Rede de Cuidados à Pessoa com Deficiência — Portaria 1.526/2023 | 35 |
| 259 | Fundatec | fundatec | legislacao.lei-13460-usuario-servicos-publicos | Direitos do usuário dos serviços públicos e ouvidorias (Lei 13.460/2017) | 35 |
| 260 | FCC | fcc | informatica.sistema-operacional-windows | Sistema Operacional Windows 10 | 35 |
| 261 | Instituto Consulplan | consulplan | informatica.sistemas-saude-ti | Informática em saúde | 35 |
| 262 | Cesgranrio | cesgranrio | direito-administrativo.licitacao-principios-modalidades | Licitação na Lei 14.133/2021: conceito, princípios e modalidades | 34 |
| 263 | FGV | fgv | gestao-pessoas.cultura-clima-organizacional | Cultura e Clima Organizacional | 34 |
| 264 | FGV | fgv | informatica.microsoft-powerpoint-2016 | Microsoft PowerPoint 2016 | 34 |
| 265 | FCC | fcc | roraima.alerr-resolucao-1-2024-licitacoes | Licitações e contratos na ALE/RR: Resolução Legislativa 1/2024 | 34 |
| 266 | FCC | fcc | roraima.fiplan-sistema-integrado | Sistema de planejamento, contabilidade e finanças de Roraima (FIPLAN) | 34 |
| 267 | FGV | fgv | raciocinio-logico.logica-primeira-ordem | Lógica de primeira ordem | 34 |
| 268 | Cesgranrio | cesgranrio | comunicacao.comunicacao-institucional-arquivamento-linguagem-simples | Comunicação institucional: arquivamento, rastreabilidade e linguagem simples | 34 |
| 269 | Idecan | idecan | educacao.diversidade-inclusao-temas-transversais | Educação inclusiva, diversidade e temas transversais | 34 |
| 270 | Vunesp | vunesp | informatica.microsoft-word-2016 | Microsoft Word 2016 | 34 |
| 271 | Vunesp | vunesp | informatica.correio-eletronico-outlook-2016-navegacao | Correio eletrônico (Outlook 2016) e navegação (Google Chrome) | 34 |
| 272 | Vunesp | vunesp | informatica.internet-navegadores | Internet e navegadores | 34 |
| 273 | Vunesp | vunesp | informatica.ferramentas-colaborativas | Ferramentas colaborativas e nuvem para o usuário | 34 |
| 274 | Instituto Consulplan | consulplan | etica.etica-profissional-conselhos | Ética profissional: conselhos de classe, responsabilidade técnica e sigilo | 34 |
| 275 | Instituto Consulplan | consulplan | nutricao.dietoterapia-avaliacao-politicas | Nutrição: alimentação normal, avaliação nutricional, dietoterapia e políticas de alimentação | 34 |
| 276 | IBAM | ibam | matematica.equacoes-1-2-grau | Equações do 1º e do 2º grau | 34 |
| 277 | Acesse Concursos | acesse-concursos | legislacao-municipal.estatuto-servidor-municipal-estadual | Estatuto do servidor municipal e estadual: conteúdo típico | 34 |
| 278 | Instituto de Avaliação Nacional (IAN) | instituto-de-avaliacao-nacional-ian | farmacia.assistencia-farmaceutica-sus | Assistência farmacêutica e políticas de medicamentos no SUS | 34 |
| 279 | Cebraspe | cebraspe | educacao.regulacao-educacao-superior-decreto-9235 | Regulação e supervisão da educação superior — Decreto 9.235/2017 | 34 |
| 280 | Cebraspe | cebraspe | direito-notarial-registral.registro-imoveis | Registro de Imóveis: Princípios, Matrícula, Prenotação e Procedimento de Dúvida | 33 |
| 281 | Cebraspe | cebraspe | direito-trabalho.greve | Direito de greve | 33 |
| 282 | Idecan | idecan | educacao.avaliacao-gestao-escolar | Avaliação da aprendizagem e gestão escolar | 33 |
| 283 | Idecan | idecan | educacao.ead-tecnologias-andragogia | Educação de adultos, EaD e tecnologias educacionais | 33 |
| 284 | Idecan | idecan | educacao.ldb-bncc-didatica | LDB, BNCC e didática | 33 |
| 285 | Cebraspe | cebraspe | raciocinio-logico.problemas-logica-contagem-probabilidade | Problemas de lógica, contagem e noções de probabilidade | 33 |
| 286 | Cesgranrio | cesgranrio | matematica.probabilidade | Probabilidade | 33 |
| 287 | Instituto AOCP | aocp | direito-constitucional.administracao-publica | Administração pública | 33 |
| 288 | Instituto Avalia | instituto-avalia | direito-administrativo.agentes-publicos | Agentes públicos: classificação, cargo, emprego e função | 33 |
| 289 | FGV | fgv | processo-penal.pacote-anticrime | Pacote Anticrime (Lei nº 13.964/2019) | 33 |
| 290 | Cebraspe | cebraspe | direito-constitucional.tribunais-juizes-trabalho-csjt | Tribunais e juízes do trabalho e CSJT | 33 |
| 291 | Instituto Consulplan | consulplan | medicina.urgencias-emergencias | Medicina: urgências, emergências e trauma | 33 |
| 292 | Instituto Consulplan | consulplan | medicina.infectologia | Infectologia clínica e antimicrobianos | 33 |
| 293 | Instituto de Avaliação Nacional (IAN) | instituto-de-avaliacao-nacional-ian | medicina.urgencias-emergencias | Medicina: urgências, emergências e trauma | 33 |
| 294 | IDIB | idib | matematica.progressoes-pa-pg | Progressões aritméticas e geométricas | 32 |
| 295 | Cebraspe | cebraspe | informatica.testes-unitarios-integracao-junit-mockito | Testes de Software: Testes Unitários e de Integração, JUnit e Mockito | 32 |
| 296 | Cebraspe | cebraspe | direito-civil.locacao-imoveis-urbanos | Locação de imóveis urbanos | 32 |
| 297 | Cebraspe | cebraspe | processo-civil.jurisprudencia-tribunais-superiores | Jurisprudência dos Tribunais Superiores em Processo Civil | 32 |
| 298 | FGV | fgv | estatistica.amostragem-pesquisas | Amostragem e pesquisas: planos amostrais e tamanho da amostra | 32 |
| 299 | Idecan | idecan | educacao.plano-nacional-educacao | Plano Nacional de Educação (PNE) | 32 |
| 300 | Idecan | idecan | educacao.tendencias-pedagogicas-politicas-educacionais | Tendências pedagógicas, políticas educacionais e coordenação pedagógica | 32 |
