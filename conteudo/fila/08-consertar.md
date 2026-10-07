# Consertar 38 matérias do Codex (06/10/2026)

Triagem feita com `deno run --allow-read conteudo/fila/triagem.ts`. As outras 99 matérias novas já foram
publicadas. Estas ficaram em `conteudo/entrada` com o problema anotado. Os recortes de
`conteudo/entrada/recorte-*.json` destas matérias só entram depois que a matéria for publicada.

O que cada problema pede:
- **JSON inválido**: achar a vírgula ou colchete errado e consertar (sem perder questão).
- **formato diferente**: reescrever no formato das outras matérias (statement, options com key/text/correct, explanation).
- **AUTHORIAL deixa sourceUrl, board, agency e year como null**: em toda questão de origem AUTHORIAL esses quatro campos ficam null.
- **certa mais longa** / **gabarito concentrado**: reescrever À MÃO as alternativas das questões afetadas (encurtar a certa ou alongar as erradas, trocar a letra da certa mudando o texto) até a certa ser a mais longa em no máximo metade das questões e nenhuma letra passar de 40%. Proibido script para embaralhar.
- **≠1 correta** / **explicação admite outra certa**: corrigir para uma só certa e a explicação bater com o gabarito.
- **apelido já usado**: tirar esse apelido desta matéria.
- **teoria curta**: completar a teoria até passar de 6000 caracteres.

## Mensagem para o Codex (trocar o BLOCO)

    Leia o AGENTS.md e C:\Users\otavi\Documents\Codex\2026-09-15\vc-x20\conteudo\fila\08-consertar.md. Faça SÓ o BLOCO X: para cada arquivo, abra conteudo/entrada/<arquivo>, conserte exatamente o problema anotado, salve em UTF-8 sem BOM com a ferramenta de editar arquivos e confira com: deno run --allow-read conteudo/fila/conferir.ts conteudo/entrada/<arquivo>. Repita até o conferidor não apontar erro. Não descarte questões, não use script para gerar ou embaralhar, não peça confirmação. No fim, diga quais consertou.

## Bloco 1

- [ ] `afo.orcamento-publico-principios.json` · apelido já usado: Princípios orçamentários→orcamento-publico.conceitos
- [ ] `ciencias.quimica-organica-isomeria-reacoes.json` · validador(2): questions: precisa ter de 40 a 300 itens (tem 29) · só 29 questões
- [ ] `contabilidade.custos.json` · JSON: Unexpected token ']', ..."eço."},
- [ ] `contabilidade.dfc-dva-analise.json` · validador(34): questions[12]: questão AUTHORIAL deixa sourceUrl, board, agency e year como null
- [ ] `contabilidade.estoques-ativo-imobilizado.json` · validador(27): questions[19]: questão AUTHORIAL deixa sourceUrl, board, agency e year como null

## Bloco 2

- [ ] `contabilidade.passivo-patrimonio-liquido.json` · validador(34): questions[11]: questão AUTHORIAL deixa sourceUrl, board, agency e year como null
- [ ] `contratos.json` · validador(1): questions: a alternativa certa é a mais longa em 11 de 15 (máximo 40%); equilibre o tamanh · certa mais longa 11/15 · apelido já usado: Dos contratos em geral.→direito-civil.bens
- [ ] `crimes-dignidade-sexual.json` · validador(1): questions: a alternativa certa é a mais longa em 16 de 24 (máximo 40%); equilibre o tamanh · certa mais longa 16/24
- [ ] `crimes-militares-especie.json` · validador(2): questions: gabarito concentrado demais numa letra · certa mais longa 21/24 · gabarito concentrado {"A":24}

## Bloco 3

- [ ] `direito-civil.pessoas-naturais.json` · validador(1): questions: a alternativa certa é a mais longa em 19 de 25 (máximo 40%); equilibre o tamanh · certa mais longa 19/25
- [ ] `disposicoes-especiais.json` · JSON: Unexpected token ',', ..."ull},
- [ ] `eca-ato-infracional.json` · validador(1): questions: a alternativa certa é a mais longa em 22 de 25 (máximo 40%); equilibre o tamanh · certa mais longa 22/25
- [ ] `estatistica.probabilidade-avancada.json` · validador(36): questions[12]: questão AUTHORIAL deixa sourceUrl, board, agency e year como null
- [ ] `estatistica.series-temporais-numeros-indices.json` · validador(45): questions[1]: questão AUTHORIAL deixa sourceUrl, board, agency e year como null

## Bloco 4

- [ ] `execucao.json` · status REVIEWED · apelido já usado: Execução no processo do trabalho→processo-trabalho.sentenca-coisa-julgada
- [ ] `greve.json` · JSON: Expected ',' or ']' after array element in JSON at position 70944 (line 105 colu
- [ ] `inquerito-policial-militar.json` · validador(1): questions: a alternativa certa é a mais longa em 24 de 25 (máximo 40%); equilibre o tamanh · certa mais longa 24/25
- [ ] `juizados-especiais-criminais.json` · validador(1): questions: a alternativa certa é a mais longa em 20 de 28 (máximo 40%); equilibre o tamanh · certa mais longa 20/28
- [ ] `legislacao-penal-especial.crimes-hediondos-tortura.json` · validador(2): questions: gabarito concentrado demais numa letra · certa mais longa 21/21 · gabarito concentrado {"A":15,"B":6}

## Bloco 5

- [ ] `legislacao-penal-especial.maria-da-penha.json` · validador(2): questions: gabarito concentrado demais numa letra · certa mais longa 19/23 · gabarito concentrado {"A":15,"B":8}
- [ ] `legislacao.lei-6858-1980.json` · validador(1): questions: a alternativa certa é a mais longa em 16 de 30 (máximo 40%); equilibre o tamanh
- [ ] `matematica.exponencial-logaritmos.json` · apelido já usado: Funções exponenciais→matematica.funcao-exponencial-logaritmica
- [ ] `negocio-juridico.json` · validador(4): questions: precisa ter de 40 a 300 itens (tem 39) · certa mais longa 10/14 · gabarito concentrado {"A":6,"C":3,"B":4,"D":1} · apelido já usado: Fatos e atos jurídicos: forma e prova dos atos jurídicos; defeitos dos negócios jurídicos; nulidade e anulabilidade dos atos jurídicos; atos jurídicos ilícitos; abuso de direito; prescrição e decadência.→direito-civil.bens
- [ ] `obrigacoes.json` · validador(5): questions: a alternativa certa é a mais longa em 15 de 16 (máximo 40%); equilibre o tamanh · certa mais longa 15/16 · apelido já usado: Direito das obrigações: constituição, extinção, espécies e cumprimento.→direito-civil.bens

## Bloco 6

- [ ] `organizacoes-criminosas.json` · validador(1): questions: a alternativa certa é a mais longa em 23 de 25 (máximo 40%); equilibre o tamanh · certa mais longa 23/25
- [ ] `pessoas-juridicas.json` · validador(1): questions: a alternativa certa é a mais longa em 13 de 15 (máximo 40%); equilibre o tamanh · certa mais longa 13/15
- [ ] `prescricao-decadencia.json` · validador(9): chapters: teoria curta demais (5951 de 6000 caracteres) · certa mais longa 9/13
- [ ] `principios-processo.json` · JSON: Expected ',' or ']' after array element in JSON at position 72224 (line 153 colu

## Bloco 7

- [ ] `processo-civil.tutela-provisoria.json` · apelido já usado: Tutelas antecipatórias.→processo-civil.procedimento-comum
- [ ] `processo-penal-militar.json` · validador(1): questions: a alternativa certa é a mais longa em 16 de 24 (máximo 40%); equilibre o tamanh · certa mais longa 16/24
- [ ] `processo-trabalho.citacao-garantia-penhora.json` · validador(2): questions: gabarito concentrado demais numa letra · certa mais longa 37/43 · gabarito concentrado {"A":7,"B":24,"C":9,"D":3}
- [ ] `processo-trabalho.dissidio-coletivo.json` · validador(2): questions: gabarito concentrado demais numa letra · certa mais longa 21/31 · gabarito concentrado {"C":4,"A":7,"B":17,"D":2,"E":1} · apelido já usado: Dissídios coletivos e sentença normativa→processo-trabalho.sentenca-coisa-julgada
- [ ] `processo-trabalho.embargos-execucao.json` · validador(2): questions: gabarito concentrado demais numa letra · certa mais longa 32/37 · gabarito concentrado {"A":20,"B":13,"C":2,"D":2}

## Bloco 8

- [ ] `processo-trabalho.praca-leilao-arrematacao.json` · validador(2): questions: gabarito concentrado demais numa letra · certa mais longa 22/28 · gabarito concentrado {"A":26,"B":2}
- [ ] `processo-trabalho.processo-judicial-eletronico.json` · validador(1): questions: a alternativa certa é a mais longa em 20 de 30 (máximo 40%); equilibre o tamanh · certa mais longa 20/30
- [ ] `processo-trabalho.recursos.json` · validador(2): questions: gabarito concentrado demais numa letra · certa mais longa 20/27 · gabarito concentrado {"A":27}
- [ ] `servicos-auxiliares-peritos.json` · JSON: Expected double-quoted property name in JSON at position 68038 (line 587 column 
- [ ] `tutela-provisoria.json` · apelido já usado: Tutelas antecipatórias.→processo-civil.procedimento-comum

