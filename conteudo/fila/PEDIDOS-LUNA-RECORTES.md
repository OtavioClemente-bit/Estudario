# Pedidos para o Codex/Luna (noite): recortes de banca

**O que gerar:** RECORTES DE BANCA, isto é, questões no estilo de uma banca específica sobre uma matéria que já existe, mais o
"como cai" daquela banca. É o que mais falta. Existem 1.251 pares banca × matéria cobrados em 20 ou mais editais e ainda sem
recorte. A lista abaixo traz os 300 primeiros, que juntos atendem cerca de 38 mil editais.

**Por que recorte, e não matéria nova:** a matéria (teoria) já existe e foi revisada. O recorte é só questão e orientação, então
o risco de inventar lei é bem menor, e cada recorte serve a todos os editais daquela banca com aquela matéria.

## Prompt (cole no Codex; um recorte por tarefa, ou peça vários agentes, cada um com um item da lista)

```
Gere UM recorte de banca para o app Estudário, neste repositório. Leia AGENTS.md.

ITEM: banca "<Banca>" | pasta "<pasta>" | matéria "<id da matéria>"

1. Leia a matéria inteira em conteudo/materias/<dir>/<arq>.json (o id "a.b" vira a/b.json). As questões do recorte só podem cobrar
   o que está nessa teoria. Use como modelo de formato e de qualidade conteudo/recortes/fgv/portugues.coesao-textual.json.
2. Grave conteudo/entrada/recortes-luna/<pasta>/<id da matéria>.json com:
   {"topic":"<id>","board":"<Banca>","role":null,"version":1,"incidence":"ALTA|MEDIA|BAIXA",
    "howItFalls":"<400 a 2.500 caracteres: como ESSA banca cobra essa matéria (formato, comandos típicos, tipo de distrator), com 2
     exemplos de enunciado escritos por você. Sem estatística inventada (nada de 'cai em 37% das provas')>",
    "tips":[3 a 8 dicas específicas], "traps":[3 a 8 pegadinhas específicas],
    "questions":[40 questões]}
3. Cada questão: {"topico":"<título da matéria>","estilo":"<Banca>","statement","format","difficulty","options","explanation",
   "section":"<título EXATO de um capítulo da matéria>","errorConceptKey":"<chave de errorConcepts da matéria ou null>",
   "sourceType":"AUTHORIAL","sourceUrl":null,"board":null,"agency":null,"year":null}
   - Cebraspe: TODAS Certo/Errado (format "TRUE_FALSE", opções {"key":"C","text":"Certo"} e {"key":"E","text":"Errado"}),
     no estilo de itens da banca (afirmações longas, com o erro sutil no meio).
   - Demais bancas: múltipla escolha com 5 alternativas A–E (FCC, FGV, Vunesp, Cesgranrio...) ou 4 (A–D) se a banca usa 4
     (Instituto AOCP às vezes, Fundatec, IBFC: confira; na dúvida use 5).
   - Imite o jeito da banca: FGV com textos e casos; FCC com "Considere..." e assertivas I, II, III; Vunesp direta; Cesgranrio
     com situações de empresa.
   - 30% FACIL, 40% MEDIA, 30% DIFICIL. Distratores plausíveis (troca de exceção, de prazo, de classificação), nada absurdo.
   - Explicação de 150 a 400 caracteres: por que a certa está certa e onde está o erro das outras, SEM citar letra.
   - A certa não pode ser a mais longa em mais de 40% das questões; distribua a letra certa entre todas.
   - Não repita enunciado de questão que já existe na matéria.
4. Valide até dar OK (o validador usa a matéria do campo "topic"):
   deno run --allow-read --allow-env scripts/biblioteca/conferir-recorte.ts conteudo/entrada/recortes-luna/<pasta>/<id>.json
5. Nunca invente número de lei, artigo, súmula ou prazo que não esteja na matéria. Não grave fora de conteudo/entrada/.
   Abra PR só com os arquivos do recorte.
```

## Lista em ordem de prioridade (300 primeiros)

| # | Banca | Pasta | id da matéria | Título | Editais |
|---|---|---|---|---|---|
| 1 | Cebraspe | cebraspe | portugues.concordancia | Concordância verbal e nominal | 955 |
| 2 | Cebraspe | cebraspe | portugues.crase | Crase | 933 |
| 3 | Cebraspe | cebraspe | portugues.colocacao-pronominal | Colocação pronominal | 877 |
| 4 | Cebraspe | cebraspe | portugues.subordinacao | Subordinação entre orações e entre termos | 858 |
| 5 | Cebraspe | cebraspe | portugues.coordenacao | Coordenação entre orações e entre termos | 846 |
| 6 | Cebraspe | cebraspe | portugues.regencia-verbal-nominal | Regência verbal e nominal | 826 |
| 7 | Cebraspe | cebraspe | legislacao.lei-8429-1992-improbidade | Lei de Improbidade Administrativa (Lei 8.429/1992) | 560 |
| 8 | FGV | fgv | portugues.crase | Crase | 476 |
| 9 | Cebraspe | cebraspe | matematica.logica-proposicional | Lógica proposicional e tabela-verdade | 461 |
| 10 | FGV | fgv | portugues.formacao-de-palavras | Formação de palavras | 445 |
| 11 | Cebraspe | cebraspe | direito-administrativo.controle-administracao | Controle da Administração Pública | 424 |
| 12 | Cebraspe | cebraspe | direito-constitucional.administracao-publica | Administração pública | 424 |
| 13 | Cebraspe | cebraspe | raciocinio-logico.logica-primeira-ordem | Lógica de primeira ordem | 387 |
| 14 | FGV | fgv | matematica.logica-proposicional | Lógica proposicional e tabela-verdade | 367 |
| 15 | Cebraspe | cebraspe | direito-constitucional.direitos-politicos | Direitos políticos e partidos (arts. 14 a 17) | 366 |
| 16 | Cebraspe | cebraspe | informatica.microsoft-word-2016 | Microsoft Word 2016 | 358 |
| 17 | Cebraspe | cebraspe | direito-constitucional.poder-judiciario-disposicoes-gerais | Poder Judiciário: disposições gerais e magistratura | 332 |
| 18 | Cebraspe | cebraspe | informatica.internet-busca-redes-sociais | Internet: busca, redes sociais, grupos de discussão e wikis | 315 |
| 19 | FGV | fgv | portugues.funcoes-linguagem-teoria | História da língua, níveis e funções da linguagem | 307 |
| 20 | Cebraspe | cebraspe | direito-administrativo.principios-administracao | Princípios da Administração Pública | 304 |
| 21 | FGV | fgv | portugues.concordancia | Concordância verbal e nominal | 299 |
| 22 | FGV | fgv | legislacao.lei-8429-1992-improbidade | Lei de Improbidade Administrativa (Lei 8.429/1992) | 298 |
| 23 | FGV | fgv | portugues.regencia-verbal-nominal | Regência verbal e nominal | 285 |
| 24 | Cebraspe | cebraspe | matematica.quantificadores-diagramas | Quantificadores e diagramas | 281 |
| 25 | Cebraspe | cebraspe | portugues.retextualizacao | Retextualização de gêneros e níveis de formalidade | 273 |
| 26 | FGV | fgv | raciocinio-logico.analise-situacoes | Compreensão e análise lógica de situações | 265 |
| 27 | FGV | fgv | matematica.probabilidade | Probabilidade | 254 |
| 28 | FGV | fgv | direito-administrativo.principios-administracao | Princípios da Administração Pública | 240 |
| 29 | Cebraspe | cebraspe | legislacao.lei-8112-1990-visao-geral | Lei 8.112/1990: visão geral do regime jurídico | 230 |
| 30 | FGV | fgv | portugues.colocacao-pronominal | Colocação pronominal | 213 |
| 31 | Cebraspe | cebraspe | etica.codigo-etica-servidor-federal | Código de Ética do servidor público civil federal | 212 |
| 32 | FCC | fcc | portugues.concordancia | Concordância verbal e nominal | 210 |
| 33 | FCC | fcc | portugues.regencia-verbal-nominal | Regência verbal e nominal | 210 |
| 34 | FGV | fgv | matematica.sistema-legal-medidas | Sistema Legal de Medidas | 207 |
| 35 | FGV | fgv | matematica.quantificadores-diagramas | Quantificadores e diagramas | 203 |
| 36 | FCC | fcc | portugues.crase | Crase | 203 |
| 37 | FCC | fcc | portugues.vozes-verbais | Vozes verbais | 198 |
| 38 | FGV | fgv | portugues.coerencia-textual | Coerência textual | 184 |
| 39 | FGV | fgv | direito-administrativo.controle-administracao | Controle da Administração Pública | 183 |
| 40 | FGV | fgv | saude-publica.politicas-nacionais-saude | Políticas Nacionais de Saúde: Atenção Básica, Promoção, Humanização e Redes | 175 |
| 41 | FGV | fgv | direito-constitucional.administracao-publica | Administração pública | 173 |
| 42 | Instituto AOCP | instituto-aocp | portugues.interpretacao-textual | Interpretação de textos | 173 |
| 43 | Instituto AOCP | instituto-aocp | portugues.coesao-textual | Coesão textual | 173 |
| 44 | Cebraspe | cebraspe | direito-constitucional.organizacao-poderes | Organização dos Poderes e separação das funções | 162 |
| 45 | Cebraspe | cebraspe | matematica.sistema-legal-medidas | Sistema Legal de Medidas | 150 |
| 46 | Instituto AOCP | instituto-aocp | portugues.significacao-contextual-palavras-expressoes | Significação contextual de palavras e expressões | 148 |
| 47 | Instituto AOCP | instituto-aocp | portugues.funcoes-que-se | Funções de que e se | 148 |
| 48 | Instituto AOCP | instituto-aocp | portugues.crase | Crase | 148 |
| 49 | Instituto AOCP | instituto-aocp | portugues.formacao-de-palavras | Formação de palavras | 148 |
| 50 | Instituto AOCP | instituto-aocp | portugues.concordancia | Concordância verbal e nominal | 148 |
| 51 | Instituto AOCP | instituto-aocp | portugues.regencia-verbal-nominal | Regência verbal e nominal | 148 |
| 52 | Instituto AOCP | instituto-aocp | portugues.colocacao-pronominal | Colocação pronominal | 148 |
| 53 | Instituto AOCP | instituto-aocp | matematica.porcentagem | Porcentagem e variação percentual | 148 |
| 54 | FGV | fgv | seguranca-trabalho.normas-regulamentadoras | Normas Regulamentadoras (NRs) de segurança e saúde no trabalho | 138 |
| 55 | Cebraspe | cebraspe | raciocinio-logico.analise-situacoes | Compreensão e análise lógica de situações | 138 |
| 56 | Cebraspe | cebraspe | matematica.probabilidade | Probabilidade | 137 |
| 57 | FGV | fgv | portugues.subordinacao | Subordinação entre orações e entre termos | 136 |
| 58 | Cebraspe | cebraspe | direito-constitucional.defesa-estado-instituicoes | Defesa do Estado e das Instituições Democráticas | 135 |
| 59 | FCC | fcc | portugues.formacao-de-palavras | Formação de palavras | 135 |
| 60 | FGV | fgv | saude.seguranca-paciente-qualidade | Segurança do paciente e qualidade em saúde | 133 |
| 61 | Fundatec | fundatec | portugues.regencia-verbal-nominal | Regência verbal e nominal | 132 |
| 62 | Fundatec | fundatec | portugues.concordancia | Concordância verbal e nominal | 132 |
| 63 | FCC | fcc | raciocinio-logico.analise-situacoes | Compreensão e análise lógica de situações | 132 |
| 64 | FCC | fcc | portugues.subordinacao | Subordinação entre orações e entre termos | 130 |
| 65 | Cebraspe | cebraspe | legislacao.lei-8112-1990-provimento | Lei 8.112/1990: disposições preliminares, provimento, vacância, remoção, redistribuição e substituição | 127 |
| 66 | Instituto Avalia | instituto-avalia | portugues.interpretacao-textual | Interpretação de textos | 126 |
| 67 | Instituto Avalia | instituto-avalia | portugues.ortografia | Ortografia oficial: emprego das letras | 126 |
| 68 | Instituto AOCP | instituto-aocp | matematica.sequencias-logicas | Sequências lógicas | 126 |
| 69 | IDIB | idib | portugues.interpretacao-textual | Interpretação de textos | 125 |
| 70 | IDIB | idib | portugues.significacao-contextual-palavras-expressoes | Significação contextual de palavras e expressões | 125 |
| 71 | IDIB | idib | portugues.classes-de-palavras | Classes de palavras | 125 |
| 72 | FCC | fcc | direito-constitucional.administracao-publica | Administração pública | 125 |
| 73 | Instituto Avalia | instituto-avalia | portugues.significacao-contextual-palavras-expressoes | Significação contextual de palavras e expressões | 125 |
| 74 | Cebraspe | cebraspe | lingua-inglesa.gramatica-para-leitura | Gramática inglesa para leitura | 124 |
| 75 | IDIB | idib | portugues.ortografia | Ortografia oficial: emprego das letras | 124 |
| 76 | IDIB | idib | portugues.acentuacao | Acentuação gráfica | 124 |
| 77 | FGV | fgv | direito-constitucional.direitos-politicos | Direitos políticos e partidos (arts. 14 a 17) | 124 |
| 78 | FCC | fcc | direito-administrativo.controle-administracao | Controle da Administração Pública | 124 |
| 79 | Fundatec | fundatec | portugues.colocacao-pronominal | Colocação pronominal | 124 |
| 80 | Fundatec | fundatec | portugues.periodo-composto-coordenacao-subordinacao | Período composto: coordenação e subordinação | 123 |
| 81 | Fundatec | fundatec | legislacao.lei-13146-2015-inclusao | Estatuto da Pessoa com Deficiência (Lei 13.146/2015) | 123 |
| 82 | IDECAN | idecan | portugues.concordancia | Concordância verbal e nominal | 121 |
| 83 | Fundatec | fundatec | portugues.variacao-linguistica-norma-linguistica | Variação linguística e norma linguística | 120 |
| 84 | FGV | fgv | direito-constitucional.poder-judiciario-disposicoes-gerais | Poder Judiciário: disposições gerais e magistratura | 116 |
| 85 | IDIB | idib | matematica.sequencias-logicas | Sequências lógicas | 115 |
| 86 | IDIB | idib | raciocinio-logico.analise-situacoes | Compreensão e análise lógica de situações | 115 |
| 87 | IDECAN | idecan | portugues.regencia-verbal-nominal | Regência verbal e nominal | 115 |
| 88 | IDECAN | idecan | portugues.formacao-de-palavras | Formação de palavras | 115 |
| 89 | IDIB | idib | portugues.concordancia | Concordância verbal e nominal | 114 |
| 90 | IDIB | idib | matematica.logica-proposicional | Lógica proposicional e tabela-verdade | 113 |
| 91 | IDIB | idib | raciocinio-logico.conjuntos | Operações com conjuntos | 113 |
| 92 | IDIB | idib | matematica.analise-combinatoria | Análise combinatória | 112 |
| 93 | Instituto Consulplan | instituto-consulplan | portugues.interpretacao-textual | Interpretação de textos | 112 |
| 94 | IDIB | idib | matematica.razao-proporcao | Razão e proporção | 111 |
| 95 | IDIB | idib | raciocinio-logico.problemas-aritmeticos | Problemas aritméticos, geométricos e matriciais | 111 |
| 96 | IDIB | idib | portugues.formacao-de-palavras | Formação de palavras | 110 |
| 97 | IDIB | idib | matematica.porcentagem | Porcentagem e variação percentual | 109 |
| 98 | IDIB | idib | matematica.regra-de-tres | Regra de três simples e composta | 109 |
| 99 | Cesgranrio | cesgranrio | portugues.concordancia | Concordância verbal e nominal | 109 |
| 100 | Instituto Consulplan | instituto-consulplan | portugues.significacao-contextual-palavras-expressoes | Significação contextual de palavras e expressões | 109 |
| 101 | Cesgranrio | cesgranrio | portugues.crase | Crase | 108 |
| 102 | IDIB | idib | portugues.pontuacao | Pontuação | 107 |
| 103 | Instituto Consulplan | instituto-consulplan | portugues.pontuacao | Pontuação | 107 |
| 104 | Instituto Consulplan | instituto-consulplan | portugues.concordancia | Concordância verbal e nominal | 107 |
| 105 | Instituto Consulplan | instituto-consulplan | portugues.regencia-verbal-nominal | Regência verbal e nominal | 107 |
| 106 | Instituto Consulplan | instituto-consulplan | portugues.crase | Crase | 107 |
| 107 | IDIB | idib | informatica.hardware-conceitos-basicos | Hardware: conceitos básicos | 106 |
| 108 | Instituto AOCP | instituto-aocp | portugues.frase-oracao-periodo | Frase, oração e período | 106 |
| 109 | IDIB | idib | informatica.internet-navegadores | Internet e navegadores | 105 |
| 110 | IDIB | idib | legislacao.lei-13709-2018-lgpd | Lei Geral de Proteção de Dados (Lei 13.709/2018) | 105 |
| 111 | Instituto Avalia | instituto-avalia | portugues.pontuacao | Pontuação | 105 |
| 112 | IDIB | idib | portugues.reescrita | Reescrita de frases e parágrafos | 104 |
| 113 | FGV | fgv | informatica.microsoft-word-2016 | Microsoft Word 2016 | 104 |
| 114 | Instituto Avalia | instituto-avalia | portugues.verbos | Emprego e correlação de tempos e modos verbais | 104 |
| 115 | Cebraspe | cebraspe | gestao-pessoas.cultura-clima-organizacional | Cultura e Clima Organizacional | 103 |
| 116 | IDIB | idib | portugues.coesao-textual | Coesão textual | 103 |
| 117 | IDIB | idib | informatica.microsoft-word-2016 | Microsoft Word 2016 | 103 |
| 118 | IDIB | idib | informatica.correio-eletronico-outlook-2016-navegacao | Correio eletrônico (Outlook 2016) e navegação (Google Chrome) | 103 |
| 119 | Instituto AOCP | instituto-aocp | informatica.ferramentas-colaborativas | Ferramentas colaborativas e nuvem para o usuário | 103 |
| 120 | Cebraspe | cebraspe | legislacao-penal-especial.maria-da-penha | Lei Maria da Penha | 102 |
| 121 | FCC | fcc | legislacao.lei-8429-1992-improbidade | Lei de Improbidade Administrativa (Lei 8.429/1992) | 101 |
| 122 | Cesgranrio | cesgranrio | portugues.coesao-textual | Coesão textual | 100 |
| 123 | Instituto Avalia | instituto-avalia | portugues.concordancia | Concordância verbal e nominal | 99 |
| 124 | FCC | fcc | direito-constitucional.direitos-politicos | Direitos políticos e partidos (arts. 14 a 17) | 97 |
| 125 | Instituto AOCP | instituto-aocp | saude-publica.epidemiologia-vigilancia | Epidemiologia e vigilância em saúde | 97 |
| 126 | IDECAN | idecan | direito-administrativo.principios-administracao | Princípios da Administração Pública | 96 |
| 127 | IDIB | idib | portugues.regencia-verbal-nominal | Regência verbal e nominal | 95 |
| 128 | Instituto Avalia | instituto-avalia | portugues.classes-de-palavras | Classes de palavras | 95 |
| 129 | FGV | fgv | regulacao-sanitaria.legislacao-anvisa-snvs-infracoes | Legislação da Anvisa: SNVS, Infrações Sanitárias e Registro de Produtos | 95 |
| 130 | IDIB | idib | informatica.autenticacao-controle-acesso-sso | Autenticação, controle de acesso, OAuth2 e SSO | 94 |
| 131 | Cebraspe | cebraspe | direito-administrativo.abuso-autoridade | Lei de Abuso de Autoridade (Lei 13.869/2019) | 93 |
| 132 | IDIB | idib | informatica.ferramentas-colaborativas | Ferramentas colaborativas e nuvem para o usuário | 93 |
| 133 | Instituto AOCP | instituto-aocp | informatica.libreoffice | LibreOffice | 93 |
| 134 | FGV | fgv | saude-publica.controle-social-conselhos-saude | Controle social no SUS: conselhos, conferências e direitos dos usuários | 93 |
| 135 | IDIB | idib | portugues.coerencia-textual | Coerência textual | 92 |
| 136 | IDIB | idib | portugues.coordenacao | Coordenação entre orações e entre termos | 92 |
| 137 | IDIB | idib | portugues.verbos | Emprego e correlação de tempos e modos verbais | 92 |
| 138 | IDIB | idib | matematica.equivalencias-negacoes-logicas | Equivalências e negações lógicas | 92 |
| 139 | Cebraspe | cebraspe | educacao.psicologia-aprendizagem-didatica | Psicologia da Aprendizagem e Didática | 92 |
| 140 | IDIB | idib | portugues.pronomes | Emprego e colocação de pronomes | 91 |
| 141 | IDIB | idib | informatica.arquivos-digitais-backup | Arquivos digitais, transferência e backup | 91 |
| 142 | IDIB | idib | informatica.internet-busca-redes-sociais | Internet: busca, redes sociais, grupos de discussão e wikis | 91 |
| 143 | IDIB | idib | informatica.ataques-ddos-forca-bruta-phishing-apt | Ataques Cibernéticos: DDoS, Amplificação, Smurf, Força Bruta, Phishing, Spear Phishing e APT | 91 |
| 144 | IDIB | idib | informatica.governanca-qualidade-dados | Governança, qualidade e gestão de dados | 91 |
| 145 | Instituto AOCP | instituto-aocp | saude-publica.sus-legislacao | SUS: legislação, princípios e controle social | 91 |
| 146 | Instituto AOCP | instituto-aocp | informatica.softwares-utilitarios | Softwares utilitários: compactadores, e-mail, chat, mídia e proteção | 91 |
| 147 | Instituto AOCP | instituto-aocp | saude-publica.politicas-nacionais-saude | Políticas Nacionais de Saúde: Atenção Básica, Promoção, Humanização e Redes | 91 |
| 148 | Instituto AOCP | instituto-aocp | informatica.internet-busca-redes-sociais | Internet: busca, redes sociais, grupos de discussão e wikis | 90 |
| 149 | Cebraspe | cebraspe | direito-administrativo.lei-8666-1993-pregao-10520 | Lei 8.666/1993 e pregão (Lei 10.520/2002): regime licitatório anterior à Lei 14.133/2021 | 89 |
| 150 | IDIB | idib | matematica.argumentacao-logica | Argumentação lógica | 89 |
| 151 | Instituto AOCP | instituto-aocp | saude-publica.financiamento-saude-lc-141 | Financiamento da saúde — LC 141/2012 | 89 |
| 152 | FCC | fcc | direito-administrativo.principios-administracao | Princípios da Administração Pública | 85 |
| 153 | Cebraspe | cebraspe | administracao-publica.gestao-participativa-resultados-comunicacao | Gestão Pública: Participação, Resultados e Comunicação | 84 |
| 154 | IDECAN | idecan | informatica.microsoft-word-2016 | Microsoft Word 2016 | 84 |
| 155 | Instituto Consulplan | instituto-consulplan | portugues.termos-da-oracao | Termos da oração | 84 |
| 156 | Cebraspe | cebraspe | direito-administrativo.parcerias-publico-privadas | Parcerias público-privadas (Lei 11.079/2004) | 83 |
| 157 | FGV | fgv | direito-administrativo.abuso-autoridade | Lei de Abuso de Autoridade (Lei 13.869/2019) | 83 |
| 158 | Cesgranrio | cesgranrio | portugues.regencia-verbal-nominal | Regência verbal e nominal | 83 |
| 159 | Instituto Avalia | instituto-avalia | portugues.crase | Crase | 83 |
| 160 | Instituto Consulplan | instituto-consulplan | portugues.redacao-oficial | Redação oficial | 83 |
| 161 | Instituto Avalia | instituto-avalia | portugues.generos-textuais | Gêneros textuais e tipos textuais | 83 |
| 162 | Fundatec | fundatec | legislacao.estatuto-pessoa-idosa | Estatuto da Pessoa Idosa | 83 |
| 163 | Cesgranrio | cesgranrio | portugues.colocacao-pronominal | Colocação pronominal | 82 |
| 164 | FGV | fgv | direito-constitucional.organizacao-poderes | Organização dos Poderes e separação das funções | 81 |
| 165 | Cebraspe | cebraspe | direito-ambiental.florestas-codigo-florestal-mata-atlantica | Proteção da Vegetação: Código Florestal, Mata Atlântica e Florestas Públicas | 81 |
| 166 | FGV | fgv | direito.processo-legislativo-estadual | Processo legislativo e Poder Legislativo estadual | 81 |
| 167 | Cebraspe | cebraspe | direito-tributario.legislacao-tributaria | Legislação Tributária: Fontes, Vigência, Aplicação, Interpretação e Integração | 80 |
| 168 | FGV | fgv | saude.cuidados-paliativos-bioetica | Cuidados paliativos e bioética | 80 |
| 169 | IDIB | idib | administracao-publica.governo-digital | Governo digital e Lei 14.129/2021 | 79 |
| 170 | Fundatec | fundatec | portugues.coesao-textual | Coesão textual | 79 |
| 171 | Fundatec | fundatec | legislacao.igualdade-racial-crimes-preconceito | Igualdade racial e crimes de preconceito | 79 |
| 172 | FGV | fgv | meio-ambiente.gestao-licenciamento-unidades-conservacao | Gestão ambiental: licenciamento, unidades de conservação, recursos naturais e educação ambiental | 79 |
| 173 | Fundatec | fundatec | portugues.formacao-de-palavras | Formação de palavras | 78 |
| 174 | Instituto Avalia | instituto-avalia | portugues.termos-da-oracao | Termos da oração | 77 |
| 175 | Cebraspe | cebraspe | legislacao-penal-especial.crimes-hediondos-tortura | Crimes Hediondos e Tortura | 77 |
| 176 | Fundatec | fundatec | direito-administrativo.crimes-administracao-publica | Crimes contra a Administração Pública | 77 |
| 177 | Cebraspe | cebraspe | direito-administrativo.licitacoes-contratos-estatais-lei-13303 | Licitações e contratos das estatais (Lei 13.303/2016, arts. 28 a 91) | 76 |
| 178 | Instituto Avalia | instituto-avalia | portugues.regencia-verbal-nominal | Regência verbal e nominal | 76 |
| 179 | Instituto Avalia | instituto-avalia | portugues.pronomes | Emprego e colocação de pronomes | 74 |
| 180 | Fundatec | fundatec | portugues.vozes-verbais | Vozes verbais | 74 |
| 181 | Cebraspe | cebraspe | direito-previdenciario.regime-proprio-servidores | Regime Próprio de Previdência dos Servidores (art. 40 da CF, EC nº 103/2019 e Lei nº 9.717/1998) | 73 |
| 182 | FGV | fgv | matematica.funcoes | Funções afim e quadrática | 73 |
| 183 | FCC | fcc | direito-constitucional.poder-judiciario-disposicoes-gerais | Poder Judiciário: disposições gerais e magistratura | 73 |
| 184 | Instituto Consulplan | instituto-consulplan | matematica.sequencias-logicas | Sequências lógicas | 71 |
| 185 | Instituto Consulplan | instituto-consulplan | matematica.regra-de-tres | Regra de três simples e composta | 71 |
| 186 | Instituto Consulplan | instituto-consulplan | matematica.razao-proporcao | Razão e proporção | 71 |
| 187 | Instituto Consulplan | instituto-consulplan | raciocinio-logico.conjuntos | Operações com conjuntos | 71 |
| 188 | Instituto Consulplan | instituto-consulplan | matematica.geometria-plana | Geometria plana | 71 |
| 189 | Instituto Consulplan | instituto-consulplan | matematica.equacoes-1-2-grau | Equações do 1º e do 2º grau | 71 |
| 190 | Cotec/Fadenor | cotec-fadenor | portugues.interpretacao-textual | Interpretação de textos | 71 |
| 191 | Cotec/Fadenor | cotec-fadenor | portugues.significacao-contextual-palavras-expressoes | Significação contextual de palavras e expressões | 71 |
| 192 | Cotec/Fadenor | cotec-fadenor | portugues.fonologia-fonemas | Fonologia, fonemas e divisão silábica | 71 |
| 193 | Cotec/Fadenor | cotec-fadenor | portugues.ortografia | Ortografia oficial: emprego das letras | 71 |
| 194 | Cotec/Fadenor | cotec-fadenor | portugues.formacao-de-palavras | Formação de palavras | 71 |
| 195 | Cotec/Fadenor | cotec-fadenor | portugues.pontuacao | Pontuação | 71 |
| 196 | Cotec/Fadenor | cotec-fadenor | portugues.classes-de-palavras | Classes de palavras | 71 |
| 197 | FGV | fgv | direito-administrativo.parcerias-publico-privadas | Parcerias público-privadas (Lei 11.079/2004) | 70 |
| 198 | FGV | fgv | informatica.sistemas-saude-ti | Informática em saúde | 70 |
| 199 | Cotec/Fadenor | cotec-fadenor | portugues.verbos | Emprego e correlação de tempos e modos verbais | 70 |
| 200 | Instituto Consulplan | instituto-consulplan | matematica.argumentacao-logica | Argumentação lógica | 69 |
| 201 | Instituto Consulplan | instituto-consulplan | matematica.analise-combinatoria | Análise combinatória | 69 |
| 202 | Instituto Consulplan | instituto-consulplan | matematica.progressoes-pa-pg | Progressões aritméticas e geométricas | 69 |
| 203 | Vunesp | vunesp | portugues.interpretacao-textual | Interpretação de textos | 69 |
| 204 | Vunesp | vunesp | portugues.significacao-contextual-palavras-expressoes | Significação contextual de palavras e expressões | 69 |
| 205 | Vunesp | vunesp | portugues.pontuacao | Pontuação | 69 |
| 206 | Vunesp | vunesp | portugues.concordancia | Concordância verbal e nominal | 69 |
| 207 | Vunesp | vunesp | portugues.regencia-verbal-nominal | Regência verbal e nominal | 69 |
| 208 | Vunesp | vunesp | portugues.colocacao-pronominal | Colocação pronominal | 69 |
| 209 | Vunesp | vunesp | portugues.crase | Crase | 69 |
| 210 | Cotec/Fadenor | cotec-fadenor | portugues.concordancia | Concordância verbal e nominal | 69 |
| 211 | Cotec/Fadenor | cotec-fadenor | portugues.regencia-verbal-nominal | Regência verbal e nominal | 69 |
| 212 | Cotec/Fadenor | cotec-fadenor | portugues.colocacao-pronominal | Colocação pronominal | 69 |
| 213 | Cotec/Fadenor | cotec-fadenor | portugues.crase | Crase | 69 |
| 214 | Cotec/Fadenor | cotec-fadenor | portugues.figuras-de-linguagem | Figuras de linguagem | 69 |
| 215 | Cotec/Fadenor | cotec-fadenor | portugues.variacao-linguistica-norma-linguistica | Variação linguística e norma linguística | 69 |
| 216 | Cotec/Fadenor | cotec-fadenor | informatica.windows-10-11 | Windows 10 e 11: recursos básicos | 69 |
| 217 | Cotec/Fadenor | cotec-fadenor | informatica.internet-navegadores | Internet e navegadores | 69 |
| 218 | Cotec/Fadenor | cotec-fadenor | informatica.seguranca | Segurança | 69 |
| 219 | Instituto Consulplan | instituto-consulplan | raciocinio-logico.problemas-aritmeticos | Problemas aritméticos, geométricos e matriciais | 68 |
| 220 | Cotec/Fadenor | cotec-fadenor | portugues.funcoes-linguagem-teoria | História da língua, níveis e funções da linguagem | 68 |
| 221 | Cotec/Fadenor | cotec-fadenor | informatica.aplicativos-escritorio-office-libreoffice | Aplicativos de escritório: Microsoft Office e LibreOffice (texto, planilhas e apresentações) | 68 |
| 222 | Cotec/Fadenor | cotec-fadenor | informatica.correio-eletronico-outlook-2016-navegacao | Correio eletrônico (Outlook 2016) e navegação (Google Chrome) | 68 |
| 223 | IDIB | idib | informatica.linux | Linux: conceitos, diretórios, permissões e comandos | 67 |
| 224 | IDIB | idib | arquivologia.gestao-documentos | Gestão de documentos: classificação, temporalidade e preservação digital | 67 |
| 225 | Cebraspe | cebraspe | direito-ambiental.recursos-hidricos-lei-9433 | Recursos Hídricos: Política Nacional e SINGREH (Lei 9.433/1997) | 67 |
| 226 | Fundatec | fundatec | informatica.internet-navegadores | Internet e navegadores | 67 |
| 227 | IDIB | idib | informatica.computacao-nuvem | Computação em nuvem | 66 |
| 228 | IDIB | idib | informatica.criptografia-certificacao | Criptografia, certificação digital e segurança de redes | 66 |
| 229 | Fundatec | fundatec | informatica.ia-generativa-etica | IA, aprendizado de máquina, IA generativa e ética | 66 |
| 230 | Fundatec | fundatec | informatica.microsoft-excel-2016 | Microsoft Excel 2016 | 66 |
| 231 | IDIB | idib | administracao-publica.transparencia-administracao-publica | Transparência na Administração Pública | 65 |
| 232 | Instituto Consulplan | instituto-consulplan | saude-publica.sus-legislacao | SUS: legislação, princípios e controle social | 65 |
| 233 | Fundatec | fundatec | informatica.microsoft-word-2016 | Microsoft Word 2016 | 65 |
| 234 | Fundatec | fundatec | informatica.correio-eletronico-outlook-2016-navegacao | Correio eletrônico (Outlook 2016) e navegação (Google Chrome) | 65 |
| 235 | Fundatec | fundatec | matematica.sequencias-logicas | Sequências lógicas | 65 |
| 236 | IDIB | idib | informatica.libreoffice | LibreOffice | 64 |
| 237 | IDIB | idib | comunicacao.comunicacao-institucional-arquivamento-linguagem-simples | Comunicação institucional: arquivamento, rastreabilidade e linguagem simples | 64 |
| 238 | IDIB | idib | informatica.sistemas-informacao | Sistemas de informação | 64 |
| 239 | IDIB | idib | informatica.auditoria-sistemas-ti | Auditoria de sistemas de tecnologia da informação | 64 |
| 240 | IDIB | idib | matematica.graficos-tabelas-mapas-escalas | Leitura de gráficos, tabelas, mapas e escalas | 64 |
| 241 | Instituto Consulplan | instituto-consulplan | portugues.fonologia-fonemas | Fonologia, fonemas e divisão silábica | 64 |
| 242 | Instituto de Avaliação Nacional (IAN) | instituto-de-avaliacao-nacional-ian | portugues.interpretacao-textual | Interpretação de textos | 64 |
| 243 | Instituto de Avaliação Nacional (IAN) | instituto-de-avaliacao-nacional-ian | portugues.significacao-contextual-palavras-expressoes | Significação contextual de palavras e expressões | 64 |
| 244 | Instituto de Avaliação Nacional (IAN) | instituto-de-avaliacao-nacional-ian | portugues.ortografia | Ortografia oficial: emprego das letras | 64 |
| 245 | Instituto de Avaliação Nacional (IAN) | instituto-de-avaliacao-nacional-ian | portugues.pontuacao | Pontuação | 64 |
| 246 | Instituto de Avaliação Nacional (IAN) | instituto-de-avaliacao-nacional-ian | portugues.pronomes | Emprego e colocação de pronomes | 64 |
| 247 | Instituto de Avaliação Nacional (IAN) | instituto-de-avaliacao-nacional-ian | portugues.verbos | Emprego e correlação de tempos e modos verbais | 64 |
| 248 | Instituto de Avaliação Nacional (IAN) | instituto-de-avaliacao-nacional-ian | portugues.classes-de-palavras | Classes de palavras | 64 |
| 249 | Instituto de Avaliação Nacional (IAN) | instituto-de-avaliacao-nacional-ian | portugues.termos-da-oracao | Termos da oração | 64 |
| 250 | Instituto de Avaliação Nacional (IAN) | instituto-de-avaliacao-nacional-ian | portugues.subordinacao | Subordinação entre orações e entre termos | 64 |
| 251 | Instituto de Avaliação Nacional (IAN) | instituto-de-avaliacao-nacional-ian | portugues.frase-oracao-periodo | Frase, oração e período | 64 |
| 252 | Instituto de Avaliação Nacional (IAN) | instituto-de-avaliacao-nacional-ian | portugues.concordancia | Concordância verbal e nominal | 64 |
| 253 | Instituto de Avaliação Nacional (IAN) | instituto-de-avaliacao-nacional-ian | portugues.regencia-verbal-nominal | Regência verbal e nominal | 64 |
| 254 | Instituto de Avaliação Nacional (IAN) | instituto-de-avaliacao-nacional-ian | portugues.crase | Crase | 64 |
| 255 | Fundatec | fundatec | raciocinio-logico.relacoes-arbitrarias | Estrutura lógica de relações e dedução | 63 |
| 256 | Fundatec | fundatec | matematica.quantificadores-diagramas | Quantificadores e diagramas | 63 |
| 257 | Fundatec | fundatec | matematica.logica-proposicional | Lógica proposicional e tabela-verdade | 63 |
| 258 | Cebraspe | cebraspe | legislacao-penal-especial.estatuto-desarmamento | Estatuto do Desarmamento | 62 |
| 259 | Cebraspe | cebraspe | direito.convencao-americana-sobre-direitos-humanos | Convenção Americana sobre Direitos Humanos | 62 |
| 260 | FGV | fgv | legislacao.ebserh | Legislação da Ebserh: lei de criação, estatuto, regimento, código de ética e regulamento de pessoal | 62 |
| 261 | FGV | fgv | saude-publica.financiamento-saude-lc-141 | Financiamento da saúde — LC 141/2012 | 62 |
| 262 | FGV | fgv | saude-publica.equidade-genero-raca-trabalhadoras-sus | Equidade de gênero, raça e valorização das trabalhadoras no SUS — Portaria 230/2023 | 62 |
| 263 | Vunesp | vunesp | saude-publica.epidemiologia-vigilancia | Epidemiologia e vigilância em saúde | 62 |
| 264 | Instituto Consulplan | instituto-consulplan | saude-publica.politicas-nacionais-saude | Políticas Nacionais de Saúde: Atenção Básica, Promoção, Humanização e Redes | 62 |
| 265 | FCC | fcc | portugues.retextualizacao | Retextualização de gêneros e níveis de formalidade | 62 |
| 266 | IBGP | ibgp | portugues.ortografia | Ortografia oficial: emprego das letras | 61 |
| 267 | IBGP | ibgp | portugues.classes-de-palavras | Classes de palavras | 61 |
| 268 | IBGP | ibgp | portugues.interpretacao-textual | Interpretação de textos | 61 |
| 269 | Cebraspe | cebraspe | direito-ambiental.povos-comunidades-tradicionais | Povos Indígenas, Quilombolas e Comunidades Tradicionais: Direitos Territoriais e Socioambientais | 60 |
| 270 | FCC | fcc | direito-administrativo.abuso-autoridade | Lei de Abuso de Autoridade (Lei 13.869/2019) | 60 |
| 271 | Cebraspe | cebraspe | educacao.avaliacao-gestao-escolar | Avaliação da aprendizagem e gestão escolar | 60 |
| 272 | IBGP | ibgp | portugues.frase-oracao-periodo | Frase, oração e período | 60 |
| 273 | IBGP | ibgp | portugues.pontuacao | Pontuação | 60 |
| 274 | Fundatec | fundatec | saude-publica.politicas-nacionais-saude | Políticas Nacionais de Saúde: Atenção Básica, Promoção, Humanização e Redes | 60 |
| 275 | FGV | fgv | direito-constitucional.defesa-estado-instituicoes | Defesa do Estado e das Instituições Democráticas | 59 |
| 276 | Cebraspe | cebraspe | estatistica.amostragem-pesquisas | Amostragem e pesquisas: planos amostrais e tamanho da amostra | 59 |
| 277 | Cebraspe | cebraspe | educacao.politica-nacional-educacao-ambiental | Política Nacional de Educação Ambiental (Lei 9.795/1999) | 59 |
| 278 | Instituto Avalia | instituto-avalia | matematica.argumentacao-logica | Argumentação lógica | 59 |
| 279 | IBGP | ibgp | portugues.acentuacao | Acentuação gráfica | 59 |
| 280 | IBAM | ibam | portugues.interpretacao-textual | Interpretação de textos | 59 |
| 281 | IBAM | ibam | portugues.coesao-textual | Coesão textual | 59 |
| 282 | IBAM | ibam | portugues.classes-de-palavras | Classes de palavras | 59 |
| 283 | IBAM | ibam | portugues.significacao-contextual-palavras-expressoes | Significação contextual de palavras e expressões | 59 |
| 284 | IBAM | ibam | portugues.pontuacao | Pontuação | 59 |
| 285 | IBAM | ibam | portugues.crase | Crase | 59 |
| 286 | Instituto Consulplan | instituto-consulplan | portugues.morfologia-estrutura-formacao-classes | Morfologia completa: estrutura, formação, classes e flexões | 59 |
| 287 | Instituto Consulplan | instituto-consulplan | saude-publica.epidemiologia-vigilancia | Epidemiologia e vigilância em saúde | 59 |
| 288 | Cebraspe | cebraspe | direito-penal.jurisprudencia-tribunais-superiores | Jurisprudência dos Tribunais Superiores em Direito Penal | 58 |
| 289 | IBGP | ibgp | matematica.numeros-racionais | Números racionais: frações e decimais | 58 |
| 290 | IBGP | ibgp | matematica.regra-de-tres | Regra de três simples e composta | 58 |
| 291 | IBGP | ibgp | matematica.mmc-mdc-divisibilidade | MMC, MDC e divisibilidade | 58 |
| 292 | IBGP | ibgp | matematica.sistema-legal-medidas | Sistema Legal de Medidas | 58 |
| 293 | IBGP | ibgp | matematica.sequencias-logicas | Sequências lógicas | 58 |
| 294 | IBGP | ibgp | atualidades.politica-economia-relacoes-internacionais | Atualidades: política, economia, relações internacionais e tecnologia | 58 |
| 295 | IBGP | ibgp | atualidades.sociedade-cultura-meio-ambiente | Atualidades: sociedade, cultura, educação e meio ambiente | 58 |
| 296 | IBGP | ibgp | etica.cidadania-democracia | Ética, democracia e exercício da cidadania | 58 |
| 297 | IBGP | ibgp | direito-constitucional.nacionalidade | Direitos de nacionalidade (arts. 12 e 13) | 58 |
| 298 | IBGP | ibgp | portugues.generos-textuais | Gêneros textuais e tipos textuais | 58 |
| 299 | IBGP | ibgp | portugues.coerencia-textual | Coerência textual | 58 |
| 300 | IBAM | ibam | portugues.regencia-verbal-nominal | Regência verbal e nominal | 58 |
