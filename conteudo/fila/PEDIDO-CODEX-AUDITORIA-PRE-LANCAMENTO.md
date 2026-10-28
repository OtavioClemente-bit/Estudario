# Pedido ao Codex: auditoria das 120 matérias mais usadas, antes de abrir o app ao público

## Por que isto vem antes do lançamento

Estas 120 matérias aparecem em quase todos os editais (de 484 a 3.768 cada). Um aluno novo vai cair nelas
primeiro. Um único erro de lei, prazo, regra gramatical ou gabarito numa delas derruba a confiança no app.
As questões estão sendo ampliadas por outro processo; aqui o foco é **conferir o que já está publicado**.

## O que fazer (para cada matéria da tabela, na ordem)

1. Leia `conteudo/materias/<area>/<arquivo>.json` (id = <area>.<arquivo>): capítulos, resumo, flashcards,
   dicas, pegadinhas, memorização (activeRecall), confusões (errorConcepts) e **todas** as questões.
2. Procure só **erros reais**, que fariam o aluno errar na prova:
   - **lei, artigo, súmula, prazo, valor, data** errados, revogados ou desatualizados (considere mudanças até
     outubro de 2026: reforma tributária, novas leis de licitações e processo, emendas constitucionais recentes,
     atualizações de software como Windows/Office). Se a internet estiver disponível, confira no Planalto,
     STF, STJ ou no site oficial e cite a URL. **Se não tiver certeza, não aponte como erro**: registre como
     "dúvida" com o motivo;
   - **regra gramatical** errada ou apresentada como obrigatória quando é facultativa (e vice-versa);
   - **gabarito errado**, questão com **duas alternativas corretas**, Certo/Errado ambíguo, explicação que
     contradiz o gabarito;
   - **conta errada** (matemática, raciocínio lógico, AFO): refaça cada conta;
   - contradição entre capítulos, flashcards e questões.
3. **Não** aponte estilo, preferência de redação, tamanho de alternativa ou "poderia explicar melhor".
4. **Não altere nenhum arquivo de `conteudo/materias/`.** Grave só o relatório da matéria (abaixo).

## Formato da entrega

Um arquivo por matéria em `conteudo/entrada/auditoria-pre-lancamento/<id>.json`, gravado inteiro de uma vez:

```json
{
  "id": "direito-constitucional.direitos-fundamentais",
  "revisado_em": "2026-10-28",
  "problemas": [
    {
      "onde": "questions[37]" ,
      "tipo": "gabarito | duas-certas | lei-desatualizada | fato-errado | regra-gramatical | conta | contradicao | duvida",
      "gravidade": "alta | media",
      "trecho": "o texto exato que está errado (até 300 caracteres)",
      "problema": "o que está errado, em uma ou duas frases",
      "correcao": "o texto ou o gabarito corretos, pronto para substituir",
      "fonte": "URL oficial ou referência (obrigatória para lei e jurisprudência; vazia se não houver)"
    }
  ]
}
```

- `onde` usa o caminho no JSON: `chapters[2].markdown`, `summary`, `flashcards[5]`, `questions[37]`,
  `questions[37].options[2]`, `traps[1]` etc.
- Matéria sem problemas: grave mesmo assim, com `"problemas": []`, para eu saber que foi revisada.
- Antes de começar uma matéria, veja se o relatório dela já existe na pasta; se existir, pule.
- Não pare para perguntar. Se o limite acabar, eu peço para continuar e você retoma da próxima sem relatório.
- Não faça commit.

## Ordem (as 120 matérias mais usadas nos editais)

| # | id | Matéria | Editais |
|---|---|---|---|
| 1 | portugues.interpretacao-textual | Interpretação de textos | 3768 |
| 2 | portugues.pontuacao | Pontuação | 3644 |
| 3 | portugues.ortografia | Ortografia oficial: emprego das letras | 3477 |
| 4 | portugues.classes-de-palavras | Classes de palavras | 3300 |
| 5 | portugues.concordancia | Concordância verbal e nominal | 3253 |
| 6 | portugues.significacao-contextual-palavras-expressoes | Significação contextual de palavras e expressões | 3133 |
| 7 | portugues.regencia-verbal-nominal | Regência verbal e nominal | 2990 |
| 8 | portugues.crase | Crase | 2923 |
| 9 | portugues.coesao-textual | Coesão textual | 2818 |
| 10 | portugues.verbos | Emprego e correlação de tempos e modos verbais | 2415 |
| 11 | portugues.generos-textuais | Gêneros textuais e tipos textuais | 2308 |
| 12 | portugues.colocacao-pronominal | Colocação pronominal | 2224 |
| 13 | portugues.reescrita | Reescrita de frases e parágrafos | 2165 |
| 14 | portugues.acentuacao | Acentuação gráfica | 1893 |
| 15 | informatica.seguranca | Segurança | 1741 |
| 16 | portugues.subordinacao | Subordinação entre orações e entre termos | 1711 |
| 17 | portugues.termos-da-oracao | Termos da oração | 1687 |
| 18 | portugues.coordenacao | Coordenação entre orações e entre termos | 1592 |
| 19 | informatica.internet-navegadores | Internet e navegadores | 1591 |
| 20 | matematica.logica-proposicional | Lógica proposicional e tabela-verdade | 1504 |
| 21 | portugues.formacao-de-palavras | Formação de palavras | 1492 |
| 22 | legislacao.lei-13709-2018-lgpd | Lei Geral de Proteção de Dados (Lei 13.709/2018) | 1488 |
| 23 | legislacao.lei-8429-1992-improbidade | Lei de Improbidade Administrativa (Lei 8.429/1992) | 1474 |
| 24 | matematica.argumentacao-logica | Argumentação lógica | 1419 |
| 25 | direito-administrativo.licitacao-principios-modalidades | Licitação na Lei 14.133/2021: conceito, princípios e modalidades | 1384 |
| 26 | portugues.figuras-de-linguagem | Figuras de linguagem | 1289 |
| 27 | direito-constitucional.direitos-individuais-coletivos | Direitos e deveres individuais e coletivos (art. 5º) | 1268 |
| 28 | portugues.frase-oracao-periodo | Frase, oração e período | 1257 |
| 29 | raciocinio-logico.conjuntos | Operações com conjuntos | 1246 |
| 30 | direito-constitucional.administracao-publica | Administração pública | 1245 |
| 31 | matematica.numeros-racionais | Números racionais: frações e decimais | 1229 |
| 32 | matematica.porcentagem | Porcentagem e variação percentual | 1213 |
| 33 | direito-administrativo.organizacao-administrativa | Organização administrativa: direta, indireta e órgãos públicos | 1212 |
| 34 | direito-administrativo.ato-administrativo | Ato administrativo | 1196 |
| 35 | matematica.razao-proporcao | Razão e proporção | 1175 |
| 36 | matematica.analise-combinatoria | Análise combinatória | 1168 |
| 37 | direito-administrativo.poderes-administrativos | Poderes administrativos e abuso de poder | 1166 |
| 38 | informatica.microsoft-word-2016 | Microsoft Word 2016 | 1157 |
| 39 | direito-administrativo.principios-administracao | Princípios da Administração Pública | 1154 |
| 40 | administracao-publica.lei-acesso-informacao | Lei de Acesso à Informação | 1100 |
| 41 | matematica.estatistica-descritiva | Estatística descritiva | 1093 |
| 42 | raciocinio-logico.analise-situacoes | Compreensão e análise lógica de situações | 1081 |
| 43 | matematica.regra-de-tres | Regra de três simples e composta | 1059 |
| 44 | direito-administrativo.agentes-publicos | Agentes públicos: classificação, cargo, emprego e função | 1055 |
| 45 | direito-constitucional.organizacao-politico-administrativa | Organização político-administrativa e entes federativos | 1041 |
| 46 | legislacao.lei-9784-1999-processo-administrativo | Processo administrativo federal (Lei 9.784/1999) | 1039 |
| 47 | informatica.correio-eletronico-outlook-2016-navegacao | Correio eletrônico (Outlook 2016) e navegação (Google Chrome) | 1039 |
| 48 | raciocinio-logico.problemas-aritmeticos | Problemas aritméticos, geométricos e matriciais | 1019 |
| 49 | direito-administrativo.controle-administracao | Controle da Administração Pública | 1016 |
| 50 | portugues.variacao-linguistica-norma-linguistica | Variação linguística e norma linguística | 1005 |
| 51 | informatica.microsoft-excel-2016 | Microsoft Excel 2016 | 953 |
| 52 | saude-publica.sus-legislacao | SUS: legislação, princípios e controle social | 944 |
| 53 | direito-constitucional.principios-fundamentais | Princípios fundamentais (arts. 1º a 4º) | 937 |
| 54 | matematica.probabilidade | Probabilidade | 926 |
| 55 | matematica.equacoes-1-2-grau | Equações do 1º e do 2º grau | 925 |
| 56 | etica.etica-moral-funcao-publica | Ética, moral e função pública | 921 |
| 57 | informatica.ferramentas-colaborativas | Ferramentas colaborativas e nuvem para o usuário | 915 |
| 58 | portugues.coerencia-textual | Coerência textual | 910 |
| 59 | informatica.windows-10-11 | Windows 10 e 11: recursos básicos | 908 |
| 60 | matematica.quantificadores-diagramas | Quantificadores e diagramas | 906 |
| 61 | portugues.redacao-oficial | Redação oficial | 906 |
| 62 | direito-administrativo.responsabilidade-civil-estado | Responsabilidade civil do Estado | 854 |
| 63 | informatica.sistema-operacional-windows | Sistema Operacional Windows 10 | 851 |
| 64 | informatica.arquivos-digitais-backup | Arquivos digitais, transferência e backup | 836 |
| 65 | raciocinio-logico.relacoes-arbitrarias | Estrutura lógica de relações e dedução | 834 |
| 66 | matematica.sistema-legal-medidas | Sistema Legal de Medidas | 833 |
| 67 | saude-publica.politicas-nacionais-saude | Políticas Nacionais de Saúde: Atenção Básica, Promoção, Humanização e Redes | 823 |
| 68 | portugues.pronomes | Emprego e colocação de pronomes | 807 |
| 69 | legislacao.lei-13146-2015-inclusao | Estatuto da Pessoa com Deficiência (Lei 13.146/2015) | 794 |
| 70 | informatica.hardware-conceitos-basicos | Hardware: conceitos básicos | 782 |
| 71 | direito-constitucional.ordem-social | Ordem social na Constituição | 768 |
| 72 | direito-constitucional.nacionalidade | Direitos de nacionalidade (arts. 12 e 13) | 755 |
| 73 | informatica.internet-busca-redes-sociais | Internet: busca, redes sociais, grupos de discussão e wikis | 753 |
| 74 | portugues.discurso-modalizadores-atos-fala | Discurso, modalização e atos de fala | 752 |
| 75 | direito-constitucional.direitos-sociais | Direitos sociais (arts. 6º a 11) | 751 |
| 76 | matematica.geometria-plana | Geometria plana | 748 |
| 77 | direito-constitucional.direitos-politicos | Direitos políticos e partidos (arts. 14 a 17) | 746 |
| 78 | informatica.computacao-nuvem | Computação em nuvem | 734 |
| 79 | matematica.equivalencias-negacoes-logicas | Equivalências e negações lógicas | 731 |
| 80 | saude-publica.epidemiologia-vigilancia | Epidemiologia e vigilância em saúde | 728 |
| 81 | matematica.sequencias-logicas | Sequências lógicas | 724 |
| 82 | portugues.tipologia-textual | Tipologia textual | 708 |
| 83 | atualidades.politica-economia-relacoes-internacionais | Atualidades: política, economia, relações internacionais e tecnologia | 707 |
| 84 | matematica.juros-simples-compostos | Juros simples e compostos | 686 |
| 85 | legislacao-municipal.estatuto-servidor-municipal-estadual | Estatuto do servidor municipal e estadual: conteúdo típico | 683 |
| 86 | informatica.redes-computadores | Redes de computadores | 679 |
| 87 | portugues.funcoes-linguagem-teoria | História da língua, níveis e funções da linguagem | 667 |
| 88 | direito-administrativo.regime-juridico-fontes | Direito Administrativo: conceito, fontes e regime jurídico-administrativo | 664 |
| 89 | matematica.graficos-tabelas-mapas-escalas | Leitura de gráficos, tabelas, mapas e escalas | 659 |
| 90 | direito-constitucional.poder-judiciario-disposicoes-gerais | Poder Judiciário: disposições gerais e magistratura | 652 |
| 91 | direito-constitucional.poder-executivo | Poder Executivo | 643 |
| 92 | direito-constitucional.poder-legislativo | Poder Legislativo e estatuto dos congressistas | 638 |
| 93 | direito-constitucional.aplicabilidade-normas-constitucionais | Aplicabilidade e interpretação das normas constitucionais | 636 |
| 94 | direito-administrativo.contratos-administrativos | Contratos administrativos na Lei 14.133/2021 | 634 |
| 95 | informatica.microsoft-powerpoint-2016 | Microsoft PowerPoint 2016 | 625 |
| 96 | administracao-geral.projetos-qualidade | Gestão de projetos e gestão da qualidade | 602 |
| 97 | portugues.fonologia-fonemas | Fonologia, fonemas e divisão silábica | 600 |
| 98 | sustentabilidade.desenvolvimento-sustentavel | Desenvolvimento sustentável, ODS e A3P | 599 |
| 99 | afo.ppa-ldo-loa-ciclo | Planejamento e ciclo orçamentário | 585 |
| 100 | matematica.progressoes-pa-pg | Progressões aritméticas e geométricas | 580 |
| 101 | legislacao.eca-direitos-fundamentais | ECA: direitos fundamentais, medidas de proteção e Conselho Tutelar | 569 |
| 102 | afo.lei-responsabilidade-fiscal | Responsabilidade na gestão fiscal | 563 |
| 103 | raciocinio-logico.logica-primeira-ordem | Lógica de primeira ordem | 552 |
| 104 | direito-constitucional.ministerio-publico | Ministério Público e MPT | 552 |
| 105 | etica.cidadania-democracia | Ética, democracia e exercício da cidadania | 548 |
| 106 | etica.codigo-etica-servidor-federal | Código de Ética do servidor público civil federal | 542 |
| 107 | administracao-publica.planejamento-estrategico | Planejamento estratégico, BSC, SWOT e indicadores | 541 |
| 108 | legislacao.lei-8112-1990-regime-disciplinar | Lei 8.112/1990: regime disciplinar | 529 |
| 109 | direito-administrativo.servico-publico | Serviço público: conceito, princípios e classificação | 525 |
| 110 | matematica.funcoes | Funções afim e quadrática | 523 |
| 111 | educacao.ldb-bncc-didatica | LDB, BNCC e didática | 510 |
| 112 | legislacao.lei-8112-1990-visao-geral | Lei 8.112/1990: visão geral do regime jurídico | 504 |
| 113 | afo.orcamento-publico-principios | Orçamento público e princípios orçamentários | 504 |
| 114 | administracao-geral.comportamento-organizacional | Comportamento organizacional: motivação, liderança, grupos, conflito, poder e mudança | 502 |
| 115 | afo.receita-despesa-publica | Receita e despesa pública | 499 |
| 116 | direito-constitucional.fiscalizacao-contabil-financeira | Fiscalização contábil, financeira e orçamentária (arts. 70 a 75) | 493 |
| 117 | direito-constitucional.conceito-classificacao-constituicao | Conceito e classificação das constituições | 488 |
| 118 | direito-administrativo.terceiro-setor | Terceiro setor, entes de colaboração e convênios | 486 |
| 119 | administracao-publica.modelos-governanca | Modelos de administração pública e governança | 485 |
| 120 | direito-administrativo.autarquias-agencias-reguladoras-executivas | Autarquias, Agências reguladoras e executivas | 484 |
