# Pedidos para o Codex: recortes de banca, lote 2 (itens 301 a 600)

O lote 1 (itens 1 a 300) já está publicado. Este traz os próximos 300 pares banca × matéria sem recorte, que juntos atendem
cerca de 15 mil editais. Muitos são de bancas médias (Quadrix, Consulpam, Fundatec, Acesse, IBGP) e de legislação local.

## Mensagem para colar no Codex (uma tarefa longa)

```
Leia AGENTS.md e conteudo/fila/PEDIDOS-LUNA-RECORTES-2.md (este arquivo) neste repositório.

TAREFA: gerar os recortes de banca dos itens <INÍCIO> a <FIM> da tabela abaixo, um arquivo por item.
Se puder usar subagentes ou trabalhar em paralelo, divida os itens entre eles.
Sobre o AGENTS.md: cada ARQUIVO deve ser gravado inteiro e pronto, mas você deve gravar VÁRIOS arquivos na mesma tarefa.
Faça um item por vez, valide, e passe ao próximo. Não pare para perguntar: continue até terminar a faixa ou acabar o limite.
Se um item não der certo, anote o motivo em conteudo/entrada/recortes-luna-2/PENDENCIAS.md e siga.
No fim, abra UM PR só com os arquivos gravados e a lista item → OK/pendente na descrição.

PARA CADA ITEM:
1. Leia a matéria inteira em conteudo/materias/<dir>/<arq>.json (o id "a.b" vira a/b.json). As questões só podem cobrar o
   que está nessa teoria. Modelo de formato: conteudo/recortes/fgv/portugues.coesao-textual.json.
2. Grave conteudo/entrada/recortes-luna-2/<pasta>/<id da matéria>.json:
   {"topic":"<id>","board":"<Banca exatamente como na tabela>","role":null,"version":1,"incidence":"ALTA|MEDIA|BAIXA",
    "howItFalls":"400 a 2.500 caracteres: como ESSA banca cobra essa matéria (formato, comandos típicos, tipo de distrator),
     com 2 exemplos de enunciado escritos por você. Sem estatística inventada.",
    "tips":[3 a 8 dicas específicas], "traps":[3 a 8 pegadinhas específicas], "questions":[40 questões]}
3. Cada questão: {"topico":"<título da matéria>","estilo":"<Banca>","statement","format","difficulty","options","explanation",
   "section":"<título EXATO de um capítulo da matéria>","errorConceptKey":"<chave de errorConcepts da matéria ou null>",
   "sourceType":"AUTHORIAL","sourceUrl":null,"board":null,"agency":null,"year":null}
   - Cebraspe (e Quadrix, que usa muito Certo/Errado): TODAS Certo/Errado ("TRUE_FALSE", opções {"key":"C","text":"Certo"} e {"key":"E","text":"Errado"}).
     EXATAMENTE 20 Certo e 20 Errado. O item Errado tem UM erro sutil no meio da frase (troca de exceção, órgão, prazo,
     conceito vizinho, "sempre/apenas" indevido), nunca um exagero óbvio.
   - Demais bancas: múltipla escolha A–E (ou A–D se a banca usa 4), cada alternativa com "comentario" curto do erro dela.
     A letra correta embaralhada SEM padrão e equilibrada (com 5 letras: 8 de cada).
   - A alternativa correta NÃO pode ser a mais longa em mais de 12 das 40. Confira antes de gravar; se passar, encurte a
     correta ou alongue as erradas.
   - Imite a banca: FGV com textos e casos; FCC com "Considere..." e assertivas I, II, III; Vunesp direta; Cesgranrio com
     situações de empresa; Consulpam/Acesse/IBGP com enunciados diretos e assertivas.
   - Quando usar "trecho", ele é um parágrafo de verdade de 3 a 5 linhas em que a frase cobrada faz parte do texto; nada
     de frases soltas de enfeite.
   - Varie o comando: nenhum tipo de pergunta em mais de 8 das 40. Não repita a mesma ideia de questão mais de 2 vezes.
   - 12 FACIL, 16 MEDIA, 12 DIFICIL. Distratores plausíveis, nada eliminável sem saber o conteúdo.
   - Explicação de 150 a 400 caracteres: por que a certa está certa e onde está o erro, SEM citar letra ou "alternativa X".
   - Não repita enunciado de questão que já existe na matéria.
4. Valide até dar OK:
   deno run --allow-read --allow-env scripts/biblioteca/conferir-recorte.ts conteudo/entrada/recortes-luna-2/<pasta>/<id>.json
5. Legislação local (estado, município, órgão): use SOMENTE números de lei, artigo, prazo e valor que estejam na matéria.
   Nunca invente norma. Não grave fora de conteudo/entrada/.
```

Sugestão de divisão: 10 tarefas de 30 itens (301–330, 331–360, ... 571–600) ou uma tarefa só com 301–600 se ele aceitar
trabalhar com agentes em paralelo.

## Lista (itens 301 a 600)

| # | Banca | Pasta | id da matéria | Título | Editais |
|---|---|---|---|---|---|
| 301 | FGV | fgv | legislacao-estadual.constituicoes-estaduais | Constituições estaduais: estrutura e simetria | 174 |
| 302 | FGV | fgv | legislacao-municipal.estatuto-servidor-municipal-estadual | Estatuto do servidor municipal e estadual: conteúdo típico | 158 |
| 303 | Cebraspe | cebraspe | legislacao-municipal.estatuto-servidor-municipal-estadual | Estatuto do servidor municipal e estadual: conteúdo típico | 114 |
| 304 | Cebraspe | cebraspe | legislacao-estadual.constituicoes-estaduais | Constituições estaduais: estrutura e simetria | 101 |
| 305 | Instituto AOCP | instituto-aocp | amapa.conhecimentos-estaduais | História e Geografia do Amapá | 89 |
| 306 | FGV | fgv | tocantins.conhecimentos-estaduais | Tocantins: história, geografia e economia | 73 |
| 307 | Cotec/Fadenor | cotec-fadenor | legislacao-municipal.lei-organica-municipal | Lei Orgânica Municipal: estrutura e conteúdo típico | 72 |
| 308 | Cotec/Fadenor | cotec-fadenor | legislacao-municipal.estatuto-servidor-municipal-estadual | Estatuto do servidor municipal e estadual: conteúdo típico | 72 |
| 309 | Cotec/Fadenor | cotec-fadenor | legislacao-municipal.administracao-municipal | Administração municipal: organização, carreira e posturas | 70 |
| 310 | Cotec/Fadenor | cotec-fadenor | minas-gerais.santa-luzia-processo-administrativo | Processo administrativo de Santa Luzia/MG — Lei 4.055/2019 | 70 |
| 311 | FCC | fcc | legislacao-estadual.constituicoes-estaduais | Constituições estaduais: estrutura e simetria | 65 |
| 312 | Idecan | idecan | paraiba.campina-grande-historia | História de Campina Grande/PB | 65 |
| 313 | Idecan | idecan | legislacao-municipal.lei-organica-municipal | Lei Orgânica Municipal: estrutura e conteúdo típico | 64 |
| 314 | Instituto de Avaliação Nacional (IAN) | instituto-de-avaliacao-nacional-ian | legislacao-municipal.lei-organica-municipal | Lei Orgânica Municipal: estrutura e conteúdo típico | 64 |
| 315 | Instituto de Avaliação Nacional (IAN) | instituto-de-avaliacao-nacional-ian | legislacao-municipal.estatuto-servidor-municipal-estadual | Estatuto do servidor municipal e estadual: conteúdo típico | 64 |
| 316 | Vunesp | vunesp | saude-publica.politicas-nacionais-saude | Políticas Nacionais de Saúde: Atenção Básica, Promoção, Humanização e Redes | 58 |
| 317 | Cotec/Fadenor | cotec-fadenor | matematica.logica-proposicional | Lógica proposicional e tabela-verdade | 58 |
| 318 | Cotec/Fadenor | cotec-fadenor | raciocinio-logico.relacoes-arbitrarias | Estrutura lógica de relações e dedução | 58 |
| 319 | Cotec/Fadenor | cotec-fadenor | raciocinio-logico.analise-situacoes | Compreensão e análise lógica de situações | 58 |
| 320 | Cotec/Fadenor | cotec-fadenor | matematica.graficos-tabelas-mapas-escalas | Leitura de gráficos, tabelas, mapas e escalas | 58 |
| 321 | Cotec/Fadenor | cotec-fadenor | raciocinio-logico.problemas-logica-contagem-probabilidade | Problemas de lógica, contagem e noções de probabilidade | 58 |
| 322 | Cotec/Fadenor | cotec-fadenor | matematica.argumentacao-logica | Argumentação lógica | 58 |
| 323 | Cotec/Fadenor | cotec-fadenor | matematica.estatistica-descritiva | Estatística descritiva | 58 |
| 324 | FCC | fcc | legislacao.lei-8112-1990-provimento | Lei 8.112/1990: disposições preliminares, provimento, vacância, remoção, redistribuição e substituição | 58 |
| 325 | Instituto AOCP | instituto-aocp | saude.seguranca-paciente-qualidade | Segurança do paciente e qualidade em saúde | 58 |
| 326 | Instituto Avalia | instituto-avalia | portugues.formacao-de-palavras | Formação de palavras | 57 |
| 327 | IBGP | ibgp | matematica.equacoes-1-2-grau | Equações do 1º e do 2º grau | 57 |
| 328 | IBGP | ibgp | matematica.porcentagem | Porcentagem e variação percentual | 57 |
| 329 | IBGP | ibgp | raciocinio-logico.conjuntos | Operações com conjuntos | 57 |
| 330 | IBAM | ibam | portugues.ortografia | Ortografia oficial: emprego das letras | 57 |
| 331 | Vunesp | vunesp | medicina.codigo-etica-medica | Código de Ética Médica (Resolução CFM 2.217/2018) | 57 |
| 332 | Cebraspe | cebraspe | saude-publica.politicas-nacionais-saude | Políticas Nacionais de Saúde: Atenção Básica, Promoção, Humanização e Redes | 56 |
| 333 | FGV | fgv | portugues.coordenacao | Coordenação entre orações e entre termos | 56 |
| 334 | Instituto Avalia | instituto-avalia | portugues.fonologia-fonemas | Fonologia, fonemas e divisão silábica | 56 |
| 335 | Instituto Consulplan | consulplan | rondonia.ji-parana-historia-geografia | História, Geografia e Cultura de Ji-Paraná/RO | 56 |
| 336 | Instituto Consulplan | consulplan | economia.crescimento-desenvolvimento-ciclos | Crescimento, desenvolvimento e ciclos econômicos | 56 |
| 337 | FGV | fgv | legislacao.seguranca-nuclear-radiologica | Normas de segurança nuclear e radiológica (ANSN e CNEN) | 55 |
| 338 | Cebraspe | cebraspe | processo-civil.tutela-coletiva-acp-inquerito-civil | Tutela coletiva: ação civil pública, inquérito civil e compromisso de ajustamento de conduta | 54 |
| 339 | FGV | fgv | legislacao-penal-especial.maria-da-penha | Lei Maria da Penha | 54 |
| 340 | Idecan | idecan | direito-constitucional.ordem-social | Ordem social na Constituição | 54 |
| 341 | Cebraspe | cebraspe | direito-previdenciario.previdencia-complementar | Previdência Complementar (LC nº 108/2001 e LC nº 109/2001) | 54 |
| 342 | Instituto AOCP | instituto-aocp | matematica.juros-simples-compostos | Juros simples e compostos | 54 |
| 343 | IBFC | ibfc | portugues.classes-de-palavras | Classes de palavras | 54 |
| 344 | Instituto Consulplan | consulplan | medicina.codigo-etica-medica | Código de Ética Médica (Resolução CFM 2.217/2018) | 54 |
| 345 | Instituto Avalia | instituto-avalia | legislacao.lei-13709-2018-lgpd | Lei Geral de Proteção de Dados (Lei 13.709/2018) | 54 |
| 346 | Fundatec | fundatec | portugues.coerencia-textual | Coerência textual | 54 |
| 347 | Fundatec | fundatec | portugues.morfologia-estrutura-formacao-classes | Morfologia completa: estrutura, formação, classes e flexões | 54 |
| 348 | IBGP | ibgp | portugues.concordancia | Concordância verbal e nominal | 53 |
| 349 | IBGP | ibgp | portugues.regencia-verbal-nominal | Regência verbal e nominal | 53 |
| 350 | IBGP | ibgp | portugues.crase | Crase | 53 |
| 351 | IBGP | ibgp | portugues.colocacao-pronominal | Colocação pronominal | 53 |
| 352 | Instituto Quadrix | instituto-quadrix | portugues.interpretacao-textual | Interpretação de textos | 53 |
| 353 | Instituto Quadrix | instituto-quadrix | portugues.ortografia | Ortografia oficial: emprego das letras | 53 |
| 354 | Instituto Quadrix | instituto-quadrix | portugues.classes-de-palavras | Classes de palavras | 53 |
| 355 | Instituto Quadrix | instituto-quadrix | portugues.colocacao-pronominal | Colocação pronominal | 53 |
| 356 | Instituto Quadrix | instituto-quadrix | portugues.coesao-textual | Coesão textual | 53 |
| 357 | Instituto Quadrix | instituto-quadrix | portugues.verbos | Emprego e correlação de tempos e modos verbais | 53 |
| 358 | Instituto Quadrix | instituto-quadrix | portugues.coordenacao | Coordenação entre orações e entre termos | 53 |
| 359 | Instituto Quadrix | instituto-quadrix | portugues.concordancia | Concordância verbal e nominal | 53 |
| 360 | Instituto Quadrix | instituto-quadrix | portugues.regencia-verbal-nominal | Regência verbal e nominal | 53 |
| 361 | Instituto Quadrix | instituto-quadrix | portugues.crase | Crase | 53 |
| 362 | Instituto Quadrix | instituto-quadrix | portugues.pontuacao | Pontuação | 53 |
| 363 | Instituto Quadrix | instituto-quadrix | portugues.reescrita | Reescrita de frases e parágrafos | 53 |
| 364 | Instituto Quadrix | instituto-quadrix | direito-administrativo.organizacao-administrativa | Organização administrativa: direta, indireta e órgãos públicos | 53 |
| 365 | Instituto Quadrix | instituto-quadrix | direito-administrativo.agentes-publicos | Agentes públicos: classificação, cargo, emprego e função | 53 |
| 366 | Instituto Quadrix | instituto-quadrix | direito-administrativo.ato-administrativo | Ato administrativo | 53 |
| 367 | Instituto Quadrix | instituto-quadrix | direito-administrativo.poderes-administrativos | Poderes administrativos e abuso de poder | 53 |
| 368 | Instituto Quadrix | instituto-quadrix | direito-administrativo.controle-administracao | Controle da Administração Pública | 53 |
| 369 | Instituto Consulpam | instituto-consulpam | portugues.interpretacao-textual | Interpretação de textos | 53 |
| 370 | Instituto Consulpam | instituto-consulpam | portugues.generos-textuais | Gêneros textuais e tipos textuais | 53 |
| 371 | Instituto Consulpam | instituto-consulpam | portugues.variacao-linguistica-norma-linguistica | Variação linguística e norma linguística | 53 |
| 372 | Instituto Consulpam | instituto-consulpam | portugues.ortografia | Ortografia oficial: emprego das letras | 53 |
| 373 | Instituto Consulpam | instituto-consulpam | portugues.pontuacao | Pontuação | 53 |
| 374 | Instituto Consulpam | instituto-consulpam | portugues.classes-de-palavras | Classes de palavras | 53 |
| 375 | Instituto Consulpam | instituto-consulpam | portugues.redacao-dissertativo-argumentativa | Redação dissertativo-argumentativa | 53 |
| 376 | Instituto Consulpam | instituto-consulpam | portugues.regencia-verbal-nominal | Regência verbal e nominal | 53 |
| 377 | Instituto Consulpam | instituto-consulpam | portugues.colocacao-pronominal | Colocação pronominal | 53 |
| 378 | IBAM | ibam | portugues.concordancia | Concordância verbal e nominal | 53 |
| 379 | IBAM | ibam | portugues.reescrita | Reescrita de frases e parágrafos | 53 |
| 380 | Cebraspe | cebraspe | roraima.boa-vista-pccr-geral | PCCR geral de Boa Vista/RR — Lei 2.474/2023 | 53 |
| 381 | Cebraspe | cebraspe | roraima.boa-vista-pccr-saude | PCCR da saúde de Boa Vista/RR — Lei 2.527/2024 | 53 |
| 382 | FGV | fgv | direito-tributario.legislacao-tributaria | Legislação Tributária: Fontes, Vigência, Aplicação, Interpretação e Integração | 52 |
| 383 | FGV | fgv | legislacao-penal-especial.crimes-hediondos-tortura | Crimes Hediondos e Tortura | 52 |
| 384 | Instituto Avalia | instituto-avalia | portugues.coerencia-textual | Coerência textual | 52 |
| 385 | Instituto Consulplan | consulplan | matematica.polinomios-equacoes-polinomiais | Polinômios e equações polinomiais | 52 |
| 386 | IBFC | ibfc | portugues.termos-da-oracao | Termos da oração | 52 |
| 387 | Fundatec | fundatec | informatica.seguranca | Segurança | 52 |
| 388 | Instituto de Avaliação Nacional (IAN) | instituto-de-avaliacao-nacional-ian | saude.controle-infeccao-esterilizacao | Controle de infecção, esterilização e microbiologia | 52 |
| 389 | IDIB | idib | matematica.equacoes-1-2-grau | Equações do 1º e do 2º grau | 51 |
| 390 | FGV | fgv | direito-administrativo.licitacoes-contratos-estatais-lei-13303 | Licitações e contratos das estatais (Lei 13.303/2016, arts. 28 a 91) | 51 |
| 391 | IBGP | ibgp | portugues.variacao-linguistica-norma-linguistica | Variação linguística e norma linguística | 51 |
| 392 | Instituto Consulplan | consulplan | portugues.classes-de-palavras | Classes de palavras | 51 |
| 393 | Instituto Quadrix | instituto-quadrix | direito-administrativo.regime-juridico-fontes | Direito Administrativo: conceito, fontes e regime jurídico-administrativo | 51 |
| 394 | Cebraspe | cebraspe | maranhao.conhecimentos-estaduais | História e Geografia do Maranhão | 51 |
| 395 | IBGP | ibgp | saude-publica.politicas-nacionais-saude | Políticas Nacionais de Saúde: Atenção Básica, Promoção, Humanização e Redes | 51 |
| 396 | Fundatec | fundatec | matematica.equivalencias-negacoes-logicas | Equivalências e negações lógicas | 51 |
| 397 | Fundatec | fundatec | matematica.argumentacao-logica | Argumentação lógica | 51 |
| 398 | Fundatec | fundatec | raciocinio-logico.logica-primeira-ordem | Lógica de primeira ordem | 51 |
| 399 | Instituto de Avaliação Nacional (IAN) | instituto-de-avaliacao-nacional-ian | saude-publica.sus-legislacao | SUS: legislação, princípios e controle social | 51 |
| 400 | Instituto de Avaliação Nacional (IAN) | instituto-de-avaliacao-nacional-ian | saude-publica.politicas-nacionais-saude | Políticas Nacionais de Saúde: Atenção Básica, Promoção, Humanização e Redes | 51 |
| 401 | Instituto de Avaliação Nacional (IAN) | instituto-de-avaliacao-nacional-ian | saude-publica.epidemiologia-vigilancia | Epidemiologia e vigilância em saúde | 51 |
| 402 | Instituto de Avaliação Nacional (IAN) | instituto-de-avaliacao-nacional-ian | saude-publica.imunizacao-pni | Imunização: Programa Nacional de Imunizações, calendário vacinal e rede de frio | 51 |
| 403 | Instituto de Avaliação Nacional (IAN) | instituto-de-avaliacao-nacional-ian | saude.seguranca-paciente-qualidade | Segurança do paciente e qualidade em saúde | 51 |
| 404 | Cebraspe | cebraspe | distrito-federal.lei-organica-df | Lei Orgânica do Distrito Federal | 50 |
| 405 | Instituto Avalia | instituto-avalia | raciocinio-logico.problemas-aritmeticos | Problemas aritméticos, geométricos e matriciais | 50 |
| 406 | IBGP | ibgp | matematica.funcao-modular | Função modular | 50 |
| 407 | IBGP | ibgp | matematica.estatistica-descritiva | Estatística descritiva | 50 |
| 408 | IBGP | ibgp | matematica.logica-proposicional | Lógica proposicional e tabela-verdade | 50 |
| 409 | IBGP | ibgp | matematica.argumentacao-logica | Argumentação lógica | 50 |
| 410 | IBGP | ibgp | matematica.quantificadores-diagramas | Quantificadores e diagramas | 50 |
| 411 | IBGP | ibgp | raciocinio-logico.relacoes-arbitrarias | Estrutura lógica de relações e dedução | 50 |
| 412 | IBGP | ibgp | matematica.analise-combinatoria | Análise combinatória | 50 |
| 413 | IBGP | ibgp | matematica.probabilidade | Probabilidade | 50 |
| 414 | IBGP | ibgp | raciocinio-logico.analise-situacoes | Compreensão e análise lógica de situações | 50 |
| 415 | Instituto Consulplan | consulplan | matematica.trigonometria | Trigonometria | 50 |
| 416 | Instituto Consulplan | consulplan | matematica.funcoes | Funções afim e quadrática | 50 |
| 417 | Instituto Consulplan | consulplan | matematica.geometria-analitica | Geometria analítica | 50 |
| 418 | Instituto Consulplan | consulplan | matematica.matrizes-determinantes | Matrizes e determinantes | 50 |
| 419 | Instituto AOCP | instituto-aocp | medicina.codigo-etica-medica | Código de Ética Médica (Resolução CFM 2.217/2018) | 50 |
| 420 | IBAM | ibam | portugues.argumentacao-adequacao-textual | Argumentação, pertinência e adequação textual | 50 |
| 421 | Instituto Quadrix | instituto-quadrix | portugues.generos-textuais | Gêneros textuais e tipos textuais | 50 |
| 422 | Instituto Quadrix | instituto-quadrix | portugues.subordinacao | Subordinação entre orações e entre termos | 50 |
| 423 | Instituto Quadrix | instituto-quadrix | portugues.figuras-de-linguagem | Figuras de linguagem | 50 |
| 424 | Instituto Quadrix | instituto-quadrix | informatica.seguranca | Segurança | 50 |
| 425 | Instituto Quadrix | instituto-quadrix | informatica.ferramentas-colaborativas | Ferramentas colaborativas e nuvem para o usuário | 50 |
| 426 | Instituto Quadrix | instituto-quadrix | informatica.sistema-operacional-windows | Sistema Operacional Windows 10 | 50 |
| 427 | Instituto Quadrix | instituto-quadrix | informatica.internet-navegadores | Internet e navegadores | 50 |
| 428 | Instituto Quadrix | instituto-quadrix | informatica.arquivos-digitais-backup | Arquivos digitais, transferência e backup | 50 |
| 429 | Instituto Quadrix | instituto-quadrix | distrito-federal.lc-840-2011-servidores | Regime dos servidores do Distrito Federal — LC 840/2011 | 50 |
| 430 | Instituto Quadrix | instituto-quadrix | distrito-federal.realidade-df-ride | Realidade do Distrito Federal e da RIDE | 50 |
| 431 | Instituto Quadrix | instituto-quadrix | atualidades.politica-economia-relacoes-internacionais | Atualidades: política, economia, relações internacionais e tecnologia | 50 |
| 432 | Instituto Quadrix | instituto-quadrix | direito-constitucional.ordem-social | Ordem social na Constituição | 50 |
| 433 | Instituto Quadrix | instituto-quadrix | educacao.ldb-bncc-didatica | LDB, BNCC e didática | 50 |
| 434 | Instituto Quadrix | instituto-quadrix | legislacao.eca-direitos-fundamentais | ECA: direitos fundamentais, medidas de proteção e Conselho Tutelar | 50 |
| 435 | Instituto Quadrix | instituto-quadrix | legislacao.lei-13146-2015-inclusao | Estatuto da Pessoa com Deficiência (Lei 13.146/2015) | 50 |
| 436 | Instituto Quadrix | instituto-quadrix | educacao.alfabetizacao-letramento-educacao-infantil | Alfabetização, letramento e educação infantil | 50 |
| 437 | Instituto Quadrix | instituto-quadrix | educacao.eja-educacao-campo | Educação de jovens e adultos e educação do campo | 50 |
| 438 | Instituto Quadrix | instituto-quadrix | distrito-federal.lei-organica-df | Lei Orgânica do Distrito Federal | 50 |
| 439 | Instituto Quadrix | instituto-quadrix | distrito-federal.resolucao-cedf-2-2020-educacao-basica | Normas do Conselho de Educação do DF — Resolução 2/2020 citada no edital | 50 |
| 440 | Instituto Quadrix | instituto-quadrix | distrito-federal.regimento-escolar-rede-publica | Regimento Escolar da Rede Pública de Ensino do DF | 50 |
| 441 | Instituto Quadrix | instituto-quadrix | distrito-federal.plano-distrital-educacao-2015-2024 | Plano Distrital de Educação — versão exigida no edital | 50 |
| 442 | Instituto Quadrix | instituto-quadrix | distrito-federal.curriculo-em-movimento-pressupostos | Currículo em Movimento do Distrito Federal — pressupostos teóricos | 50 |
| 443 | Instituto Quadrix | instituto-quadrix | educacao.avaliacao-gestao-escolar | Avaliação da aprendizagem e gestão escolar | 50 |
| 444 | Instituto Quadrix | instituto-quadrix | distrito-federal.ii-plano-distrital-mulheres | II Plano Distrital de Política para Mulheres | 50 |
| 445 | Instituto Quadrix | instituto-quadrix | distrito-federal.magisterio-publico-lei-5105-2013 | Carreira do Magistério Público do DF — Lei 5.105/2013 | 50 |
| 446 | Instituto Quadrix | instituto-quadrix | distrito-federal.lei-5106-2013-carreira-assistencia-educacao | Carreira de Assistência à Educação do DF — Lei 5.106/2013 | 50 |
| 447 | IDIB | idib | matematica.probabilidade | Probabilidade | 49 |
| 448 | IDIB | idib | matematica.estatistica-descritiva | Estatística descritiva | 49 |
| 449 | IDIB | idib | matematica.geometria-plana | Geometria plana | 49 |
| 450 | Instituto Avalia | instituto-avalia | portugues.coesao-textual | Coesão textual | 49 |
| 451 | Instituto Consulplan | consulplan | matematica.probabilidade | Probabilidade | 49 |
| 452 | Instituto Consulplan | consulplan | matematica.numeros-racionais | Números racionais: frações e decimais | 49 |
| 453 | Instituto Avalia | instituto-avalia | portugues.redacao-oficial | Redação oficial | 49 |
| 454 | IDIB | idib | matematica.quantificadores-diagramas | Quantificadores e diagramas | 48 |
| 455 | FGV | fgv | medicina.codigo-etica-medica | Código de Ética Médica (Resolução CFM 2.217/2018) | 48 |
| 456 | Instituto Avalia | instituto-avalia | matematica.equacoes-1-2-grau | Equações do 1º e do 2º grau | 48 |
| 457 | IBGP | ibgp | portugues.formacao-de-palavras | Formação de palavras | 48 |
| 458 | Vunesp | vunesp | portugues.classes-de-palavras | Classes de palavras | 48 |
| 459 | Fundatec | fundatec | legislacao.lei-8429-1992-improbidade | Lei de Improbidade Administrativa (Lei 8.429/1992) | 48 |
| 460 | Instituto Avalia | instituto-avalia | portugues.funcoes-que-se | Funções de que e se | 48 |
| 461 | Fundatec | fundatec | direito-constitucional.administracao-publica | Administração pública | 48 |
| 462 | FGV | fgv | legislacao-municipal.lei-organica-municipal | Lei Orgânica Municipal: estrutura e conteúdo típico | 48 |
| 463 | Cebraspe | cebraspe | educacao.plano-nacional-educacao-direitos-humanos | Plano Nacional de Educação em Direitos Humanos (PNEDH) | 48 |
| 464 | Instituto AOCP | instituto-aocp | saude.cuidados-paliativos-bioetica | Cuidados paliativos e bioética | 48 |
| 465 | IDIB | idib | matematica.mmc-mdc-divisibilidade | MMC, MDC e divisibilidade | 47 |
| 466 | Cebraspe | cebraspe | informatica.redes-sem-fio-voip | Redes sem fio e VoIP | 47 |
| 467 | Instituto Consulplan | consulplan | portugues.coesao-textual | Coesão textual | 47 |
| 468 | Instituto Consulplan | consulplan | matematica.logica-proposicional | Lógica proposicional e tabela-verdade | 47 |
| 469 | Instituto Consulplan | consulplan | matematica.geometria-espacial-volumes | Geometria espacial: áreas e volumes | 47 |
| 470 | IBGP | ibgp | saude-publica.epidemiologia-vigilancia | Epidemiologia e vigilância em saúde | 47 |
| 471 | Fundatec | fundatec | legislacao.lei-8112-1990-visao-geral | Lei 8.112/1990: visão geral do regime jurídico | 47 |
| 472 | Fundatec | fundatec | etica.codigo-etica-servidor-federal | Código de Ética do servidor público civil federal | 47 |
| 473 | FGV | fgv | etica.etica-profissional-conselhos | Ética profissional: conselhos de classe, responsabilidade técnica e sigilo | 46 |
| 474 | Instituto Avalia | instituto-avalia | matematica.quantificadores-diagramas | Quantificadores e diagramas | 46 |
| 475 | Instituto Avalia | instituto-avalia | legislacao.lei-8429-1992-improbidade | Lei de Improbidade Administrativa (Lei 8.429/1992) | 46 |
| 476 | Acesse Concursos | acesse-concursos | portugues.interpretacao-textual | Interpretação de textos | 46 |
| 477 | Acesse Concursos | acesse-concursos | portugues.significacao-contextual-palavras-expressoes | Significação contextual de palavras e expressões | 46 |
| 478 | Acesse Concursos | acesse-concursos | portugues.coesao-textual | Coesão textual | 46 |
| 479 | Acesse Concursos | acesse-concursos | portugues.ortografia | Ortografia oficial: emprego das letras | 46 |
| 480 | Acesse Concursos | acesse-concursos | portugues.acentuacao | Acentuação gráfica | 46 |
| 481 | Acesse Concursos | acesse-concursos | portugues.classes-de-palavras | Classes de palavras | 46 |
| 482 | Acesse Concursos | acesse-concursos | portugues.verbos | Emprego e correlação de tempos e modos verbais | 46 |
| 483 | Acesse Concursos | acesse-concursos | portugues.formacao-de-palavras | Formação de palavras | 46 |
| 484 | Acesse Concursos | acesse-concursos | portugues.termos-da-oracao | Termos da oração | 46 |
| 485 | Acesse Concursos | acesse-concursos | portugues.frase-oracao-periodo | Frase, oração e período | 46 |
| 486 | Acesse Concursos | acesse-concursos | portugues.concordancia | Concordância verbal e nominal | 46 |
| 487 | Acesse Concursos | acesse-concursos | portugues.pontuacao | Pontuação | 46 |
| 488 | Acesse Concursos | acesse-concursos | portugues.reescrita | Reescrita de frases e parágrafos | 46 |
| 489 | Acesse Concursos | acesse-concursos | matematica.numeros-racionais | Números racionais: frações e decimais | 46 |
| 490 | Acesse Concursos | acesse-concursos | matematica.razao-proporcao | Razão e proporção | 46 |
| 491 | Acesse Concursos | acesse-concursos | matematica.regra-de-tres | Regra de três simples e composta | 46 |
| 492 | Acesse Concursos | acesse-concursos | matematica.porcentagem | Porcentagem e variação percentual | 46 |
| 493 | Acesse Concursos | acesse-concursos | matematica.sistema-legal-medidas | Sistema Legal de Medidas | 46 |
| 494 | Acesse Concursos | acesse-concursos | raciocinio-logico.problemas-aritmeticos | Problemas aritméticos, geométricos e matriciais | 46 |
| 495 | Acesse Concursos | acesse-concursos | matematica.estatistica-descritiva | Estatística descritiva | 46 |
| 496 | Acesse Concursos | acesse-concursos | matematica.geometria-plana | Geometria plana | 46 |
| 497 | Acesse Concursos | acesse-concursos | matematica.sequencias-logicas | Sequências lógicas | 46 |
| 498 | Acesse Concursos | acesse-concursos | raciocinio-logico.relacoes-arbitrarias | Estrutura lógica de relações e dedução | 46 |
| 499 | Acesse Concursos | acesse-concursos | matematica.graficos-tabelas-mapas-escalas | Leitura de gráficos, tabelas, mapas e escalas | 46 |
| 500 | Acesse Concursos | acesse-concursos | raciocinio-logico.analise-situacoes | Compreensão e análise lógica de situações | 46 |
| 501 | IDIB | idib | matematica.sistema-legal-medidas | Sistema Legal de Medidas | 45 |
| 502 | Instituto Consulplan | consulplan | informatica.internet-navegadores | Internet e navegadores | 45 |
| 503 | Vunesp | vunesp | administracao-publica.lei-acesso-informacao | Lei de Acesso à Informação | 45 |
| 504 | Fundatec | fundatec | legislacao-municipal.estatuto-servidor-municipal-estadual | Estatuto do servidor municipal e estadual: conteúdo típico | 45 |
| 505 | Instituto Avalia | instituto-avalia | portugues.colocacao-pronominal | Colocação pronominal | 45 |
| 506 | IBAM | ibam | matematica.porcentagem | Porcentagem e variação percentual | 45 |
| 507 | Fundatec | fundatec | saude-publica.pessoa-deficiencia-pnaispd-rcpd | Política e Rede de Cuidados à Pessoa com Deficiência — Portaria 1.526/2023 | 45 |
| 508 | Fundatec | fundatec | educacao.ldb-bncc-didatica | LDB, BNCC e didática | 45 |
| 509 | Fundatec | fundatec | legislacao.pessoa-com-deficiencia-acessibilidade-leis-esparsas | Pessoa com deficiência: acessibilidade, avaliação e leis esparsas | 45 |
| 510 | Instituto Avalia | instituto-avalia | direito-administrativo.licitacao-principios-modalidades | Licitação na Lei 14.133/2021: conceito, princípios e modalidades | 45 |
| 511 | Cebraspe | cebraspe | gestao-pessoas.qualidade-vida-trabalho | Qualidade de Vida no Trabalho | 44 |
| 512 | Instituto Avalia | instituto-avalia | portugues.figuras-de-linguagem | Figuras de linguagem | 44 |
| 513 | Instituto Consulpam | instituto-consulpam | portugues.coesao-textual | Coesão textual | 44 |
| 514 | Instituto Consulpam | instituto-consulpam | portugues.significacao-contextual-palavras-expressoes | Significação contextual de palavras e expressões | 44 |
| 515 | Instituto Consulpam | instituto-consulpam | portugues.verbos | Emprego e correlação de tempos e modos verbais | 44 |
| 516 | Instituto Consulpam | instituto-consulpam | portugues.fonologia-fonemas | Fonologia, fonemas e divisão silábica | 44 |
| 517 | Instituto Consulpam | instituto-consulpam | portugues.termos-da-oracao | Termos da oração | 44 |
| 518 | Instituto Consulpam | instituto-consulpam | portugues.subordinacao | Subordinação entre orações e entre termos | 44 |
| 519 | Instituto Consulpam | instituto-consulpam | portugues.figuras-de-linguagem | Figuras de linguagem | 44 |
| 520 | Instituto Consulpam | instituto-consulpam | portugues.reescrita | Reescrita de frases e parágrafos | 44 |
| 521 | Instituto Consulpam | instituto-consulpam | direito-constitucional.principios-fundamentais | Princípios fundamentais (arts. 1º a 4º) | 44 |
| 522 | Instituto Consulpam | instituto-consulpam | direito-constitucional.direitos-individuais-coletivos | Direitos e deveres individuais e coletivos (art. 5º) | 44 |
| 523 | Instituto Consulpam | instituto-consulpam | direito-constitucional.organizacao-politico-administrativa | Organização político-administrativa e entes federativos | 44 |
| 524 | Instituto Consulpam | instituto-consulpam | direito-administrativo.agentes-publicos | Agentes públicos: classificação, cargo, emprego e função | 44 |
| 525 | Instituto Consulpam | instituto-consulpam | direito-administrativo.organizacao-administrativa | Organização administrativa: direta, indireta e órgãos públicos | 44 |
| 526 | Instituto Consulpam | instituto-consulpam | direito-administrativo.ato-administrativo | Ato administrativo | 44 |
| 527 | Instituto Consulpam | instituto-consulpam | direito-administrativo.poderes-administrativos | Poderes administrativos e abuso de poder | 44 |
| 528 | Instituto Consulpam | instituto-consulpam | direito-administrativo.licitacao-principios-modalidades | Licitação na Lei 14.133/2021: conceito, princípios e modalidades | 44 |
| 529 | Instituto Consulpam | instituto-consulpam | legislacao.lei-8429-1992-improbidade | Lei de Improbidade Administrativa (Lei 8.429/1992) | 44 |
| 530 | Instituto Consulpam | instituto-consulpam | informatica.windows-10-11 | Windows 10 e 11: recursos básicos | 44 |
| 531 | Instituto Consulpam | instituto-consulpam | informatica.aplicativos-escritorio-office-libreoffice | Aplicativos de escritório: Microsoft Office e LibreOffice (texto, planilhas e apresentações) | 44 |
| 532 | Instituto Consulpam | instituto-consulpam | informatica.redes-computadores | Redes de computadores | 44 |
| 533 | Instituto Consulpam | instituto-consulpam | informatica.internet-busca-redes-sociais | Internet: busca, redes sociais, grupos de discussão e wikis | 44 |
| 534 | Instituto Consulpam | instituto-consulpam | informatica.correio-eletronico-outlook-2016-navegacao | Correio eletrônico (Outlook 2016) e navegação (Google Chrome) | 44 |
| 535 | Instituto Consulpam | instituto-consulpam | informatica.ferramentas-colaborativas | Ferramentas colaborativas e nuvem para o usuário | 44 |
| 536 | Instituto Consulpam | instituto-consulpam | informatica.computacao-nuvem | Computação em nuvem | 44 |
| 537 | Instituto Consulpam | instituto-consulpam | informatica.seguranca | Segurança | 44 |
| 538 | Fundatec | fundatec | legislacao-penal-especial.maria-da-penha | Lei Maria da Penha | 44 |
| 539 | IBAM | ibam | matematica.razao-proporcao | Razão e proporção | 44 |
| 540 | IBAM | ibam | matematica.regra-de-tres | Regra de três simples e composta | 44 |
| 541 | IBAM | ibam | matematica.estatistica-descritiva | Estatística descritiva | 44 |
| 542 | Fundatec | fundatec | saude-publica.equidade-genero-raca-trabalhadoras-sus | Equidade de gênero, raça e valorização das trabalhadoras no SUS — Portaria 230/2023 | 44 |
| 543 | FGV | fgv | raciocinio-logico.problemas-logica-contagem-probabilidade | Problemas de lógica, contagem e noções de probabilidade | 44 |
| 544 | Cebraspe | cebraspe | legislacao.resolucoes-cnj-acessibilidade | Acessibilidade e inclusão no Poder Judiciário (Resoluções CNJ 230/2016 e 401/2021) | 44 |
| 545 | FGV | fgv | legislacao-penal-especial.estatuto-desarmamento | Estatuto do Desarmamento | 43 |
| 546 | Instituto AOCP | instituto-aocp | matematica.probabilidade | Probabilidade | 43 |
| 547 | Instituto AOCP | instituto-aocp | matematica.logica-proposicional | Lógica proposicional e tabela-verdade | 43 |
| 548 | Instituto Consulpam | instituto-consulpam | direito-administrativo.principios-administracao | Princípios da Administração Pública | 43 |
| 549 | Fundatec | fundatec | legislacao.eca-direitos-fundamentais | ECA: direitos fundamentais, medidas de proteção e Conselho Tutelar | 43 |
| 550 | IBAM | ibam | matematica.graficos-tabelas-mapas-escalas | Leitura de gráficos, tabelas, mapas e escalas | 43 |
| 551 | Fundatec | fundatec | saude-publica.sus-desastres-reconstrucao-cuidado | Atuação do SUS em desastres e reconstrução do cuidado — bibliografia exigida | 43 |
| 552 | IDIB | idib | informatica.seguranca | Segurança | 42 |
| 553 | Cebraspe | cebraspe | informatica.seguranca-infraestrutura-rede-ferramentas | Segurança de Infraestrutura de Rede: Firewall, IDS, IPS, SIEM, ZTNA, PAM, VPN, Web Proxy e NGAV | 42 |
| 554 | Cebraspe | cebraspe | direito-empresarial.propriedade-industrial-direitos-autorais | Propriedade industrial e direitos autorais (Leis 9.279/1996, 9.610/1998 e 9.609/1998) | 42 |
| 555 | FGV | fgv | processo-civil.tutela-coletiva-acp-inquerito-civil | Tutela coletiva: ação civil pública, inquérito civil e compromisso de ajustamento de conduta | 42 |
| 556 | FGV | fgv | rondonia.ale-ro-pccr-lc-731 | Plano de carreira da ALE/RO: Lei Complementar 731/2013 | 42 |
| 557 | FGV | fgv | rondonia.processo-administrativo-lei-3830 | Processo administrativo de Rondônia: Lei estadual 3.830/2016 | 42 |
| 558 | Cesgranrio | cesgranrio | portugues.termos-da-oracao | Termos da oração | 42 |
| 559 | Instituto Consulplan | consulplan | informatica.sistema-operacional-windows | Sistema Operacional Windows 10 | 42 |
| 560 | Vunesp | vunesp | direito-constitucional.direitos-individuais-coletivos | Direitos e deveres individuais e coletivos (art. 5º) | 42 |
| 561 | Fundatec | fundatec | direito-constitucional.ordem-social | Ordem social na Constituição | 42 |
| 562 | Fundatec | fundatec | administracao-publica.lei-acesso-informacao | Lei de Acesso à Informação | 42 |
| 563 | Fundatec | fundatec | saude-publica.pnaes-atencao-especializada | Política Nacional de Atenção Especializada em Saúde (PNAES) | 42 |
| 564 | Fundatec | fundatec | saude-publica.saude-coletiva-trabalho-vivo-redes | Saúde coletiva: trabalho em saúde, educação permanente e redes de atenção | 42 |
| 565 | Fundatec | fundatec | saude-publica.epidemiologia-vigilancia | Epidemiologia e vigilância em saúde | 42 |
| 566 | FGV | fgv | legislacao.codigo-etica-ministerio-publico | Código de Ética do Ministério Público — Resolução CNMP 261/2023 citada no edital | 42 |
| 567 | Cebraspe | cebraspe | educacao.interdisciplinaridade-curriculo-planejamento-pedagogico | Interdisciplinaridade, currículo e planejamento do trabalho pedagógico | 42 |
| 568 | Cebraspe | cebraspe | educacao.eja-educacao-campo | Educação de jovens e adultos e educação do campo | 42 |
| 569 | Cebraspe | cebraspe | direito-trabalho.suspensao-interrupcao | Suspensão e interrupção do contrato | 41 |
| 570 | FGV | fgv | rondonia.ale-ro-licitacoes-resolucao-593 | Licitações e contratos na ALE/RO: Resolução 593/2024 | 41 |
| 571 | FCC | fcc | direito-administrativo.parcerias-publico-privadas | Parcerias público-privadas (Lei 11.079/2004) | 41 |
| 572 | FCC | fcc | direito-constitucional.organizacao-poderes | Organização dos Poderes e separação das funções | 41 |
| 573 | Cesgranrio | cesgranrio | seguranca-trabalho.gestao-sst-investigacao-acidentes-controle-perdas | Gestão de SST, investigação de acidentes e controle de perdas | 41 |
| 574 | Instituto Avalia | instituto-avalia | matematica.logica-proposicional | Lógica proposicional e tabela-verdade | 41 |
| 575 | Instituto Consulplan | consulplan | informatica.seguranca | Segurança | 41 |
| 576 | IBAM | ibam | matematica.probabilidade | Probabilidade | 41 |
| 577 | IBAM | ibam | informatica.microsoft-excel-2016 | Microsoft Excel 2016 | 41 |
| 578 | IBAM | ibam | informatica.seguranca | Segurança | 41 |
| 579 | Instituto AOCP | instituto-aocp | atualidades.politica-economia-relacoes-internacionais | Atualidades: política, economia, relações internacionais e tecnologia | 41 |
| 580 | Instituto AOCP | instituto-aocp | atualidades.politica-externa-brasileira | Relações internacionais e política externa brasileira | 41 |
| 581 | Instituto Selecon | instituto-selecon | informatica.microsoft-excel-2016 | Microsoft Excel 2016 | 41 |
| 582 | Instituto Selecon | instituto-selecon | informatica.internet-navegadores | Internet e navegadores | 41 |
| 583 | IBFC | ibfc | portugues.concordancia | Concordância verbal e nominal | 41 |
| 584 | IBFC | ibfc | portugues.regencia-verbal-nominal | Regência verbal e nominal | 41 |
| 585 | IBFC | ibfc | informatica.windows-10-11 | Windows 10 e 11: recursos básicos | 41 |
| 586 | Fundatec | fundatec | direito-constitucional.organizacao-poderes | Organização dos Poderes e separação das funções | 41 |
| 587 | Fundatec | fundatec | direito-constitucional.defesa-estado-instituicoes | Defesa do Estado e das Instituições Democráticas | 41 |
| 588 | IBAM | ibam | matematica.numeros-racionais | Números racionais: frações e decimais | 41 |
| 589 | IBAM | ibam | matematica.sistema-legal-medidas | Sistema Legal de Medidas | 41 |
| 590 | Fundatec | fundatec | informatica.windows-10-11 | Windows 10 e 11: recursos básicos | 41 |
| 591 | Fundatec | fundatec | administracao-geral.comportamento-organizacional | Comportamento organizacional: motivação, liderança, grupos, conflito, poder e mudança | 41 |
| 592 | Fundatec | fundatec | educacao.diversidade-inclusao-temas-transversais | Educação inclusiva, diversidade e temas transversais | 41 |
| 593 | FGV | fgv | educacao.bncc-computacao-cultura-digital | BNCC Computação, pensamento computacional e cultura digital | 41 |
| 594 | Cebraspe | cebraspe | alagoas.constituicao-estadual | Constituição do Estado de Alagoas | 40 |
| 595 | Cebraspe | cebraspe | informatica.contratacoes-tic-in-94 | Contratações de Soluções de TIC (IN SGD/ME 94/2022) | 40 |
| 596 | Cebraspe | cebraspe | direito-trabalho.aviso-previo | Aviso prévio | 40 |
| 597 | FGV | fgv | portugues.retextualizacao | Retextualização de gêneros e níveis de formalidade | 40 |
| 598 | FCC | fcc | roraima.lc-53-2001-servidores | Regime dos servidores de Roraima — LC 53/2001 | 40 |
| 599 | FCC | fcc | roraima.processo-administrativo-lei-418 | Processo administrativo de Roraima: Lei estadual 418/2004 | 40 |
| 600 | FCC | fcc | roraima.lc-373-2026-emendas-parlamentares | LC 373/2026 de Roraima: emendas parlamentares à LOA | 40 |
