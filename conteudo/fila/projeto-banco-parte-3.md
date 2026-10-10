==================================================
# BANCO 101 — direito-penal.teoria-do-crime
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Penal: Teoria do crime** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Conceitos de infração penal e crime; fato típico, tipicidade, conduta, resultado, nexo causal; dolo e culpa; consumação e tentativa; desistência voluntária, arrependimento eficaz e posterior; ilicitude e causas de justificação.
Fica de fora (outras matérias tratam): Imputabilidade e concurso de pessoas, penas, processo penal e crimes em espécie.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Culpabilidade.
- Desistência voluntária e arrependimento eficaz.
- Crime impossível.
- Arrependimento posterior.
- Relação de causalidade.
- O fato típico e seus elementos.
- Ilicitude.
- Crime consumado e tentado.
- Ilicitude e causas de exclusão.
- Excesso punível.
- Consumação e tentativa.
- Crime.
- Dolo e culpa.
- Erro de tipo.
- Resultado.
- Tipicidade.
- Descriminantes putativas.
- Causas de exclusão da culpabilidade.
- Erro de proibição.
- Crime: erro determinado por terceiro.
- Crime: erro sobre a pessoa.
- Pena da tentativa.
- Teoria do tipo.
- Crime qualificado pelo resultado e crime preterdoloso.
- Classificação jurídica dos crimes.
- Crimes de dano e de perigo.
- Iter criminis.
- Desistência voluntária, arrependimento eficaz e arrependimento posterior.
- Teoria geral do crime.
- Causas de exclusão da ilicitude: estado de necessidade, legítima defesa, estrito cumprimento do dever legal e exercício regular de direito.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, literalidade da lei com o detalhe que a prova troca, jurisprudência e súmulas, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Cite artigo, lei, súmula e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Se não tiver certeza de um número, explique sem ele — nunca invente.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito-penal.teoria-do-crime.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito-penal.teoria-do-crime",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 102 — raciocinio-logico.logica-primeira-ordem
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Raciocínio Lógico: Lógica de primeira ordem** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Predicados e sentenças abertas; universo de discurso e conjunto-verdade; quantificadores universal e existencial em notação simbólica; variáveis livres e ligadas; valor lógico de proposições quantificadas em universos finitos e numéricos; verdade por vacuidade; negação de proposições quantificadas, inclusive com condicional, conjunção e quantificadores encadeados; quantificadores múltiplos e a importância da ordem; tradução entre português e fórmulas (todo, algum, nenhum, somente, existe exatamente um); distribuição dos quantificadores sobre conjunção e disjunção; validade de argumentos com instanciação e generalização e construção de contraexemplos.
Fica de fora (outras matérias tratam): Leitura básica de todo, algum e nenhum com diagramas de Venn (matematica.quantificadores-diagramas); conectivos, tabelas-verdade e classificação de fórmulas proposicionais (matematica.logica-proposicional); sistemas formais de dedução completos, teoremas de completude e indecidibilidade, lógica de segunda ordem e lógicas modais.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Lógica de primeira ordem.
- Lógica de primeira ordem, operações com conjuntos e problemas aritméticos, geométricos e matriciais.
- Diagramas lógicos e lógica de primeira ordem.
- Sentenças abertas.
- Diagramas e lógica de primeira ordem.
- Quantificadores, afirmações e negações.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, cálculo com todos os dados no enunciado, interpretação de tabela ou gráfico, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Regra precisa, com exemplos concretos; nunca invente regra ou dado.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo raciocinio-logico.logica-primeira-ordem.banco-N.json, onde N é o lote)
```json
{
  "materia": "raciocinio-logico.logica-primeira-ordem",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 103 — direito-administrativo.abuso-autoridade
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Administrativo: Lei de Abuso de Autoridade (Lei 13.869/2019)** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Lei nº 13.869/2019: conceito e finalidade específica; sujeitos ativos; divergência interpretativa; ação penal; efeitos da condenação; penas restritivas; independência de instâncias; tipos penais dos arts. 9º a 38, incluindo violência institucional do art. 15-A, e regras gerais de procedimento.
Fica de fora (outras matérias tratam): Exame autônomo dos crimes funcionais de outras leis, improbidade administrativa, procedimento disciplinar geral e responsabilidade civil do Estado; apenas os pontos de interface expressos nos arts. 4º e 6º a 8º são abordados.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Lei nº 13.869/2019.
- Crimes de abuso de autoridade.
- Crimes de abuso de autoridade (Lei nº 13.869/2019 e suas alterações).
- Abuso de autoridade.
- Lei nº 4.898/1965 (abuso de autoridade).
- Lei nº 13.869/2019, e suas alterações (abuso de autoridade).
- Lei nº 13.869/2019: Crimes de abuso de autoridade.
- Lei nº 13.869/2019 (Crimes de abuso de autoridade).
- Lei nº 13.869/2019 (abuso de autoridade).
- Lei 13.869/2019 Lei de abuso de autoridade.
- Abuso de autoridade: Lei nº 13.869/2019.
- Legislação penal especial: abuso de autoridade.
- Crimes de abuso de autoridade (Lei nº 13.869/2019).
- Lei Federal nº 13.869/2019, que dispõe sobre os crimes de abuso de autoridade.
- Lei Federal nº 13.869/2019 (Lei de Abuso de Autoridade).
- Lei de Abuso de Autoridade (Lei nº 13.869/2019).
- Lei nº 13.869/2019: abuso de autoridade.
- Lei nº 13.869/2019 e suas alterações (abuso de autoridade).
- Lei nº 13.869/2019 (Lei de Abuso de Autoridade).
- Lei Federal nº 13.869/2019 - Lei de Abuso de Autoridade.
- Abuso de autoridade (Lei nº 13.869/2019).
- Lei de abuso de autoridade (Lei nº 13.869/2019 e suas alterações).
- Lei de abuso de autoridade (Lei nº 13.869/2019 e alterações).
- Direito de representação e processo de responsabilidade administrativa, civil e penal nos casos de abuso de autoridade.
- Processo nos crimes: de abuso de autoridade, de responsabilidade dos funcionários públicos.
- Leis Federais n. 13.869/2019.
- Abuso de autoridade (Lei nº 13.869/2019 e suas alterações).
- Lei nº 13.869/2019 e suas alterações: Crimes de abuso de autoridade.
- Lei Federal nº 13.869/2019 - Crimes de Abuso de Autoridade.
- Penais especiais: Lei nº 13.869/19, Abuso de Autoridade.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, literalidade da lei com o detalhe que a prova troca, jurisprudência e súmulas, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Cite artigo, lei, súmula e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Se não tiver certeza de um número, explique sem ele — nunca invente.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito-administrativo.abuso-autoridade.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito-administrativo.abuso-autoridade",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 104 — matematica.numeros-racionais
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Matemática e Raciocínio Lógico: Números racionais: frações e decimais** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Conjuntos N, Z, Q, I e R; comparação e ordenação; equivalência, simplificação e operações com frações; decimais exatos e dízimas periódicas; fração geratriz; potências e raízes de racionais; expressões numéricas e problemas com frações de um todo.
Fica de fora (outras matérias tratam): Equações e inequações avançadas, números complexos, logaritmos, porcentagens compostas como tópico autônomo e operações algébricas com polinômios.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Números inteiros, racionais e reais e suas operações, porcentagem e juros.
- Números inteiros e racionais: operações (adição, subtração, multiplicação, divisão, potenciação).
- Expressões numéricas.
- Frações e operações com frações.
- Conjuntos numéricos: números inteiros, racionais e reais.
- Números inteiros, racionais e reais e suas operações.
- Números inteiros, racionais e reais e suas operações, porcentagem.
- Conjuntos numéricos.
- Números inteiros, racionais e reais; problemas de contagem.
- Números inteiros, racionais e reais.
- Conjunto numérico: operações com números inteiros, fracionários e decimais.
- Campos Numéricos (números naturais, inteiros e racionais).
- Números inteiros: operações e propriedades.
- Números naturais e inteiros: operações e relação de ordem.
- Números reais: operações (adição, subtração, multiplicação, divisão, radiciação e potenciação); problemas.
- Números inteiros, racionais e reais, porcentagem e juros.
- Números inteiros, racionais e reais e operações.
- Números inteiros, racionais e reais e problemas de contagem.
- Operações com números inteiros, fracionários e decimais.
- Números inteiros, racionais e reais e operações numéricas.
- Aritmética e álgebra: operações com números inteiros, fracionários e decimais.
- Números inteiros e números racionais em representação fracionária e decimal: operações e propriedades.
- Aritmética: operações com números racionais.
- Conjuntos numéricos, intervalos e operações.
- Números racionais, representação fracionária e decimal: operações e propriedades.
- Números reais e suas operações.
- Números racionais, representação fracionária e decimal, operações e propriedades.
- Números inteiros, racionais e reais e suas operações e porcentagem.
- Operações e representações com números racionais.
- Operações com irracionais e aproximações por racionais.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, cálculo com todos os dados no enunciado, interpretação de tabela ou gráfico, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Regra precisa, com exemplos concretos; nunca invente regra ou dado.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo matematica.numeros-racionais.banco-N.json, onde N é o lote)
```json
{
  "materia": "matematica.numeros-racionais",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 105 — matematica.razao-proporcao
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Matemática e Raciocínio Lógico: Razão e proporção** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Razões entre grandezas, taxas unitárias, razões equivalentes, proporções, propriedade fundamental, divisão proporcional e reconhecimento de proporcionalidade direta e inversa.
Fica de fora (outras matérias tratam): Regra de três como procedimento geral, porcentagens, semelhança geométrica e matemática financeira.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Proporcionalidade direta e inversa.
- Razões e proporções.
- Razão e proporção.
- Razões e proporções: Divisão proporcional.
- Números e grandezas proporcionais: razões e proporções.
- Divisão em partes proporcionais.
- Divisão proporcional.
- Proporcionalidades.
- Proporções e divisão proporcional.
- Razões e proporções; divisão proporcional.
- Conjuntos numéricos, sistema legal de medidas, razões, proporções, divisão proporcional e regras de três.
- Razões, proporções e divisão em partes proporcionais.
- Razões e proporções e divisão em partes proporcionais.
- Proporcionalidade: razões e proporções; problemas.
- Divisão em partes diretamente e inversamente proporcionais.
- Proporcionalidade direta e inversa e medidas de comprimento, área, volume, massa e tempo.
- Proporcionalidade, regras de três e divisão de grandezas em partes proporcionais.
- Números racionais; razão, proporção e grandezas proporcionais.
- Razões, proporções, porcentagens, juros e proporcionalidade direta e inversa.
- Proporções.
- Variação de grandezas: razão e proporção.
- Taxas de variação de grandezas: razão e proporção com aplicações.
- Proporcionalidade: grandezas diretamente proporcionais, grandezas inversamente proporcionais, regra de três simples e composta, gráficos e tabelas.
- Geometria: razão entre comprimentos.
- Proporção.
- Variação de grandezas.
- Sistema legal de medidas, razões, proporções e grandezas proporcionais.
- Números e grandezas proporcionais: razões e proporções e divisão em partes proporcionais.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, cálculo com todos os dados no enunciado, interpretação de tabela ou gráfico, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Regra precisa, com exemplos concretos; nunca invente regra ou dado.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo matematica.razao-proporcao.banco-N.json, onde N é o lote)
```json
{
  "materia": "matematica.razao-proporcao",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 106 — lingua-inglesa.interpretacao-texto-ingles
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Língua Inglesa: Interpretação de texto em inglês** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Leitura de textos curtos em inglês; ideia central, dados explícitos, inferência, predição, propósito, tom, vocabulário em contexto, falsos cognatos, tempos verbais, modais, condicionais, conectivos, coesão e relações intratextuais e intertextuais.
Fica de fora (outras matérias tratam): Tradução integral de textos longos, fonética avançada, produção de redação e terminologia técnica especializada.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Compreensão de texto escrito em língua inglesa.
- Compreensão de textos em língua inglesa e itens gramaticais relevantes para o entendimento dos sentidos dos textos.
- Compreensão de textos variados: domínio do vocabulário e da estrutura da língua, ideias principais e secundárias, explícitas e implícitas, relações intratextuais e intertextuais.
- Compreensão de textos em Língua Inglesa.
- Compreensão de textos escritos em língua inglesa.
- Uso de palavras mais frequentes, sinonímia e antonímia.
- Reconhecimento de informações específicas.
- Capacidade de análise e síntese.
- Reconhecimento de organização semântica e discursiva.
- Estratégias de leitura: compreensão geral, reconhecimento de informações específicas, análise, síntese, inferência e predição.
- Organização semântica e discursiva, palavras frequentes, sinonímia, antonímia e funções retóricas; metáfora e metonímia.
- Palavras e expressões equivalentes.
- Inferência e predição.
- Compreensão de textos variados: vocabulário, estrutura, ideias principais e secundárias, explícitas e implícitas e relações intratextuais e intertextuais.
- Compreensão de textos, vocabulário, estrutura, ideias explícitas e implícitas e relações textuais.
- Conhecimento de um vocabulário fundamental para a compreensão de textos.
- Conhecimento de vocabulário fundamental para a compreensão de textos.
- Elementos de referência.
- Estratégias de leitura em língua inglesa: compreensão geral de texto.
- Inglês Estratégias de leitura em língua inglesa: compreensão geral de texto.
- Inglês técnico.
- Compreensão de textos em inglês: ideias principais e secundárias, explícitas e implícitas e relações intratextuais e intertextuais.
- Compreensão de textos escritos em língua inglesa e itens gramaticais relevantes à compreensão semântica.
- Compreensão de textos em língua inglesa e itens gramaticais relevantes à compreensão semântica.
- Compreensão de textos variados, vocabulário, estrutura da língua e identificação de ideias explícitas, implícitas e relações textuais.
- Compreensão de textos variados, domínio do vocabulário e da estrutura da língua e identificação de ideias e relações textuais.
- Compreensão de textos variados, vocabulário, estrutura da língua e ideias e relações textuais.
- Compreensão de textos variados, vocabulário, estrutura da língua e ideias explícitas e implícitas.
- Compreensão de textos variados, vocabulário, estrutura da língua e relações textuais.
- Compreensão de textos variados.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Regra precisa, com exemplos concretos; nunca invente regra ou dado.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo lingua-inglesa.interpretacao-texto-ingles.banco-N.json, onde N é o lote)
```json
{
  "materia": "lingua-inglesa.interpretacao-texto-ingles",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 107 — matematica.equivalencias-negacoes-logicas
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Matemática: Equivalências e negações lógicas** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Negação, conjunção, disjunção, condicional, bicondicional e equivalências fundamentais, inclusive leis de De Morgan e contraposição.
Fica de fora (outras matérias tratam): Dedução em sistemas formais, lógica de predicados avançada e demonstrações algébricas extensas.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Equivalências.
- Lógica sentencial (ou proposicional): Equivalências.
- Leis de De Morgan.
- Lógica sentencial (ou proposicional): Leis de De Morgan.
- Equivalências lógicas.
- Leis de Morgan.
- Lógica sentencial (ou proposicional): Leis de Morgan.
- Lógica sentencial ou proposicional: equivalências.
- Lógica sentencial ou proposicional: leis de De Morgan.
- Equivalências e negações.
- Tabelas-verdade, equivalências, leis de Morgan e problemas.
- Equivalências lógicas e raciocínio crítico.
- Implicação lógica e contrapositiva.
- Leis de Morgan, tautologia, contradição e contingência.
- Equivalências lógicas e leis de Morgan.
- Equivalências e implicações lógicas.
- Equivalências e leis de De Morgan.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, cálculo com todos os dados no enunciado, interpretação de tabela ou gráfico, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Regra precisa, com exemplos concretos; nunca invente regra ou dado.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo matematica.equivalencias-negacoes-logicas.banco-N.json, onde N é o lote)
```json
{
  "materia": "matematica.equivalencias-negacoes-logicas",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 108 — direito-constitucional.nacionalidade
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Constitucional: Direitos de nacionalidade (arts. 12 e 13)** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Nacionalidade e cidadania; brasileiros natos e naturalizados; critérios constitucionais, registro e opção; naturalização; portugueses com igualdade de direitos em reciprocidade; igualdade e distinções constitucionais; perda e reaquisição; idioma oficial e símbolos da República e dos entes subnacionais.
Fica de fora (outras matérias tratam): Direitos políticos em geral, regime jurídico detalhado de estrangeiros e procedimento legal infraconstitucional de naturalização além dos critérios constitucionais.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Nacionalidade.
- Direitos de nacionalidade.
- Direitos e garantias fundamentais: Nacionalidade.
- Dos direitos de nacionalidade.
- Direitos e garantias fundamentais: Da Nacionalidade.
- Nacionalidade e cidadania.
- Dos direitos e garantias fundamentais: dos direitos de nacionalidade.
- Direito à nacionalidade.
- Nacionalidade e naturalização.
- Poder constituinte: Nacionalidade.
- Direitos e deveres de nacionais no exterior.
- Dupla e/ou múltipla nacionalidade.
- Direitos e garantias fundamentais: direitos de nacionalidade.
- Constituição Federal: Da Nacionalidade.
- Nacionalidade (conceito e natureza, direitos dos estrangeiros).
- Opção de nacionalidade.
- Nacionalidade e direitos políticos.
- Aquisição e perda da nacionalidade.
- Apatridia e polipatria.
- Estatuto da Igualdade.
- Conceito de nacionalidade.
- Nacionalidade e condição jurídica das pessoas.
- Aquisição, perda e reaquisição.
- Múltipla nacionalidade.
- Apatridia.
- Estatuto de igualdade.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, literalidade da lei com o detalhe que a prova troca, jurisprudência e súmulas, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Cite artigo, lei, súmula e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Se não tiver certeza de um número, explique sem ele — nunca invente.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito-constitucional.nacionalidade.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito-constitucional.nacionalidade",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 109 — direito-civil.negocio-juridico
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Civil: Fatos e negócios jurídicos: forma, prova e defeitos** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Fatos, atos e negócios jurídicos; planos de existência, validade e eficácia; classificação, interpretação, representação, condição, termo e encargo; forma e prova; erro, dolo, coação, estado de perigo, lesão e fraude contra credores; simulação, nulidade, anulabilidade, atos ilícitos e abuso de direito.
Fica de fora (outras matérias tratam): Prescrição e decadência como matérias autônomas, teoria geral das obrigações, contratos típicos e responsabilidade civil extracontratual em espécie; marcos decadenciais indispensáveis à invalidade são tratados pontualmente.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Fato jurídico.
- Negócio jurídico: Disposições gerais.
- Negócio jurídico.
- Atos jurídicos lícitos e ilícitos.
- Negócio jurídico: Elementos.
- Negócio jurídico: Defeitos do negócio jurídico.
- Negócio jurídico: Simulação.
- Invalidade.
- Simulação.
- Defeitos do negócio jurídico.
- Prova do fato jurídico.
- Invalidade do negócio jurídico.
- Existência, eficácia, validade, invalidade e nulidade do negócio jurídico.
- Negócio jurídico: Representação.
- Negócio jurídico: Condição, termo e encargo.
- Representação.
- Ato jurídico: fato e ato jurídico.
- Fatos jurídicos.
- Negócio jurídico: Classificação e interpretação.
- Condição, termo e encargo.
- Validade, invalidade e nulidade do negócio jurídico.
- Ato jurídico.
- Defeitos.
- Da prova.
- Atos jurídicos: Lícitos e ilícitos.
- Negócio jurídico: classificação, interpretação.
- Negócio jurídico: representação, condição.
- Negócio jurídico: termo.
- Negócio jurídico: encargo.
- Termo.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, literalidade da lei com o detalhe que a prova troca, jurisprudência e súmulas, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Cite artigo, lei, súmula e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Se não tiver certeza de um número, explique sem ele — nunca invente.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito-civil.negocio-juridico.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito-civil.negocio-juridico",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 110 — direito-civil.lei-vigencia-aplicacao-integracao-interpretacao
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Civil: Lei: vigência, aplicação no tempo e no espaço, integração e interpretação** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Vigência, vacatio legis, revogação, aplicação da lei no tempo e no espaço, regras de direito internacional privado dos arts. 7º a 19 da LINDB, integração de lacunas e interpretação conforme os fins sociais e o bem comum.
Fica de fora (outras matérias tratam): Personalidade, capacidade, pessoas jurídicas e domicílio como temas autônomos; regras de gestão pública dos arts. 20 a 30 da LINDB; conteúdo material de outros ramos além das conexões necessárias à aplicação da lei.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Conflito das leis no tempo.
- Lei de Introdução às Normas do Direito Brasileiro.
- Vigência, aplicação, obrigatoriedade, interpretação e integração das leis.
- Eficácia das leis no espaço.
- Lei de Introdução às normas do Direito Brasileiro: vigência, aplicação, interpretação e integração das leis.
- Lei de introdução às normas do direito brasileiro: Conflito das leis no tempo.
- Lei de introdução às normas do direito brasileiro: Eficácia das leis no espaço.
- Eficácia da lei no espaço.
- Lei de Introdução ao Código Civil.
- Integração e interpretação da lei.
- Vigência, aplicação, interpretação e integração das leis.
- Interpretação e integração da norma jurídica.
- Lei: vigência; aplicação da lei no tempo e no espaço.
- Introdução ao Direito Civil.
- Conflito das leis no tempo e eficácia das leis no espaço.
- Lei de Introdução às Normas do Direito Brasileiro. Vigência, aplicação, obrigatoriedade, interpretação e integração das leis.
- Parte geral: Decreto-Lei nº 4.657/1942, alterado pela Lei nº 12.376/2010 (Lei de Introdução às Normas do Direito Brasileiro).
- Lei de introdução às normas do direito brasileiro: eficácia da lei no espaço.
- Interpretação e integração da norma jurídica e LINDB.
- Normas jurídicas.
- Lacunas.
- Lei de Introdução às Normas do Direito Brasileiro: vigência, aplicação, obrigatoriedade, interpretação e integração das leis.
- Direito Civil Lei de Introdução às Normas do Direito Brasileiro.
- Decreto-lei nº 4.657/1942 (Lei de Introdução às normas do Direito Brasileiro): arts. 1º a 19.
- Introdução ao Direito Civil: Conflito das leis no tempo.
- Introdução ao Direito Civil: Eficácia das leis no espaço.
- Interpretação e integração da norma jurídica e Lei de Introdução às Normas do Direito Brasileiro.
- Introdução ao Direito Civil e LINDB: vigência, aplicação, obrigatoriedade, interpretação e integração das leis.
- Lei de introdução às normas do direito brasileiro: vigência, aplicação, interpretação, integração e eficácia.
- Aplicação da lei no tempo.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, literalidade da lei com o detalhe que a prova troca, jurisprudência e súmulas, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Cite artigo, lei, súmula e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Se não tiver certeza de um número, explique sem ele — nunca invente.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito-civil.lei-vigencia-aplicacao-integracao-interpretacao.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito-civil.lei-vigencia-aplicacao-integracao-interpretacao",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 111 — informatica.ia-generativa-etica
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Informática: IA, aprendizado de máquina, IA generativa e ética** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Conceito de inteligência artificial e sua relação com aprendizado de máquina, aprendizado profundo e IA generativa; IA estreita, geral e superinteligência; IA simbólica (sistemas especialistas) e conexionista; teste de Turing e marcos históricos; subáreas (PLN, visão computacional, voz, recomendação); visão rápida dos tipos de aprendizado, incluindo autossupervisionado; neurônio artificial, pesos, viés e funções de ativação (degrau, sigmoide, tanh, ReLU, softmax); perceptron e XOR, MLP, retropropagação, gradiente descendente e taxa de aprendizado; CNN, RNN, LSTM e transformer com atenção; IA generativa: modelos discriminativos e generativos, LLM, tokens, embeddings, previsão do próximo token, pré-treinamento, ajuste fino, RLHF, janela de contexto, temperatura, data de corte, alucinação, RAG, GAN, difusão e multimodalidade; engenharia de prompt (zero-shot, few-shot, cadeia de raciocínio) e ferramentas de mercado; ética da IA (transparência, explicabilidade, justiça, privacidade, responsabilidade, supervisão humana, segurança, sustentabilidade); fontes de viés; riscos da IA generativa (vazamento, injeção de prompt, envenenamento, deepfakes, direitos autorais); LGPD e decisões automatizadas; regulação baseada em risco e normas de gestão de IA; uso de IA no setor público.
Fica de fora (outras matérias tratam): Data warehouse, OLAP, ETL, CRISP-DM, tarefas e algoritmos clássicos de mineração (árvore de decisão, k-NN, Naive Bayes, k-means, Apriori) e métricas de associação (estão em informatica.bi-mineracao-dados); ciclo de análise de dados, big data, métricas de avaliação de modelos e sobreajuste em detalhe (estão em informatica.analise-dados-ia); programação de modelos em Python ou bibliotecas específicas, matemática do gradiente, PLN clássico em detalhe (estemização, n-gramas, modelagem de tópicos) e texto artigo por artigo de projetos de lei em tramitação.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Conceitos básicos de inteligência artificial.
- Aprendizado de máquina, inteligência artificial e processamento de linguagem natural.
- Técnicas de prompts.
- Inteligência Artificial: fundamentos e aplicações: conceitos de inteligência artificial.
- Aprendizado da máquina.
- Introdução aos modelos generativos e modelos de linguagem.
- Ética, governança e privacidade em IA.
- Noções de aprendizado de máquina.
- Inteligência artificial e Direito.
- Inovação na gestão pública: Inteligência Artificial.
- Redes neurais, funções de ativação, gradiente, backpropagation, regularização e CNN.
- Inteligência artificial, aprendizado de máquina e sistemas de recomendação.
- Inteligência artificial: conceitos, tipos, modelos, ética e desafios.
- Inteligência Artificial.
- Deep learning.
- Redes neurais.
- Conceitos básicos de inteligência artificial: engenharia de prompts.
- Conceitos básicos de inteligência artificial: IA generativa: conceitos, exemplos e casos de uso.
- Ética e responsabilidade digital no serviço público.
- Inteligência Artificial (IA).
- Conceitos de Machine Learning.
- Principais ferramentas de mercado (Copilot, ChatGPT, META).
- Noções de aprendizado de máquina, inteligência artificial.
- Inteligência Artificial Generativa.
- Compreensão básica de Grandes Modelos de Linguagem (LLM) e de engenharia de prompt.
- Aprendizado de máquina, processamento de linguagem natural, visão computacional e deep learning.
- Transparência e imparcialidade nos usos da inteligência artificial no âmbito do serviço público.
- Impacto da Inteligência Artificial nos processos de trabalho.
- A ética na produção de conteúdo com inteligência artificial generativa.
- Noções de processamento de linguagem natural.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Regra precisa, com exemplos concretos; nunca invente regra ou dado.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo informatica.ia-generativa-etica.banco-N.json, onde N é o lote)
```json
{
  "materia": "informatica.ia-generativa-etica",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 112 — direito-constitucional.ordem-economica-financeira
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Constitucional: Ordem econômica e financeira na Constituição (arts. 170 a 192)** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Ordem econômica constitucional, fundamentos e princípios do art. 170, livre iniciativa, capital estrangeiro, exploração empresarial estatal e regulação, concessões e permissões, recursos minerais e monopólios, transportes, pequenos negócios, turismo, política urbana e usucapião, política agrícola e fundiária, reforma agrária, terras públicas, usucapião rural e Sistema Financeiro Nacional (arts. 170 a 192).
Fica de fora (outras matérias tratam): Sistema tributário e repartição de receitas, orçamento e finanças públicas, ordem social, direito concorrencial infraconstitucional aprofundado, crimes econômicos e procedimento detalhado de desapropriação.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Ordem econômica e financeira: princípios gerais da atividade econômica.
- Ordem econômica e financeira.
- Atuação do Estado no domínio econômico.
- Intervenção do Estado no domínio econômico.
- Formas e dimensões da intervenção da Administração na economia.
- Intervenção no domínio econômico: desapropriação.
- Política urbana, agrícola e fundiária e reforma agrária.
- Ordem econômica e financeira: Política urbana, agrícola e fundiária e reforma agrária.
- Ordem econômica e financeira e princípios gerais da atividade econômica.
- Ordem constitucional econômica: princípios gerais da atividade econômica.
- Modalidades de intervenção do Estado brasileiro na ordem econômica: Intervenção direta.
- Ordem econômica e financeira: Sistema Financeiro Nacional.
- Intervenção no domínio econômico.
- Princípios gerais da atividade econômica.
- Intervenção do Estado sobre a propriedade privada: Intervenção do Estado no domínio econômico.
- Ordem Econômica na Constituição Federal de 1988.
- Ordem econômica.
- Direito Administrativo Econômico.
- Constituição econômica.
- Princípios gerais.
- Monopólio.
- Repressão ao abuso do poder econômico.
- Intervenção direta.
- Regulação.
- Fomento.
- Ordem econômica e financeira e intervenção do Estado no domínio econômico.
- Ordem constitucional econômica, políticas urbana e agrícola, política fundiária e reforma agrária.
- Ordem jurídico-econômica e liberdade econômica.
- Sujeitos econômicos e intervenção do Estado no domínio econômico.
- Reforma Agrária (Lei nº 8.629/93).

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, literalidade da lei com o detalhe que a prova troca, jurisprudência e súmulas, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Cite artigo, lei, súmula e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Se não tiver certeza de um número, explique sem ele — nunca invente.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito-constitucional.ordem-economica-financeira.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito-constitucional.ordem-economica-financeira",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 113 — sustentabilidade.desenvolvimento-sustentavel
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Sustentabilidade: Desenvolvimento sustentável, ODS e A3P** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Conceito de desenvolvimento sustentável (Relatório Brundtland), dimensões da sustentabilidade e tripé ambiental-social-econômico; marcos internacionais (Estocolmo 1972, Rio 92 e Agenda 21, Objetivos de Desenvolvimento do Milênio, Rio+20); Agenda 2030 e os 17 ODS; Agenda Ambiental na Administração Pública (A3P), seus eixos temáticos e a política dos 5 Rs; compras e contratações públicas sustentáveis; noções da Política Nacional de Resíduos Sólidos (hierarquia, logística reversa, responsabilidade compartilhada); noções de mudanças climáticas (efeito estufa, mitigação e adaptação, Kyoto, Acordo de Paris, Política Nacional sobre Mudança do Clima).
Fica de fora (outras matérias tratam): Direito ambiental em profundidade (licenciamento, crimes e infrações ambientais, SNUC), biomas e geografia física, normas internas de tribunais específicos sobre sustentabilidade e o regime completo de licitações.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Agenda 2030 da ONU.
- Agenda Ambiental da Administração Pública (A3P), do Ministério do Meio Ambiente e Mudança do Clima (antigo Ministério do Meio Ambiente).
- Sustentabilidade pública e acessibilidade.
- Desenvolvimento sustentável.
- Agenda Ambiental da Administração Pública (A3P).
- Conceito de Desenvolvimento Sustentável (Relatório Brundtland).
- Resolução CNJ n° 400/2021 (Dispõe sobre a política de sustentabilidade no âmbito do Poder Judiciário).
- Resolução CNJ nº 400/2021 (Dispõe sobre a política de sustentabilidade no âmbito do Poder Judiciário).
- Sustentabilidade das contratações.
- Política de Sustentabilidade no Superior Tribunal de Justiça (IN/GDG n.º 4/2024) e do Poder Judiciário (Resolução CNJ n.º 400/2021).
- Resolução do CNJ nº 400/2021 (Política Nacional de Sustentabilidade no Âmbito do Poder Judiciário).
- Competências das unidades socioambientais no Poder Judiciário e Plano de Logística Sustentável (Resolução CNJ nº 400/2021).
- Meio ambiente e desenvolvimento sustentável.
- Do Meio Ambiente (Constituição Federal de 1988, Art. 225): Conceito de Desenvolvimento Sustentável (Relatório Brundtland).
- Sustentabilidade pública.
- Política Nacional de Mudanças no Clima (Lei 12.187/2009).
- Política Nacional de Resíduos Sólidos (Lei 12.305/2010).
- Desenvolvimento sustentável, Agenda Ambiental da Administração Pública e meio ambiente na Constituição Federal.
- Agenda 2030 e os 17 Objetivos de Desenvolvimento Sustentável.
- Desenvolvimento sustentável conforme o Relatório Brundtland.
- Objetivos do desenvolvimento sustentável.
- Desenvolvimento sustentável, Pacto Global e Objetivos de Desenvolvimento Sustentável.
- Desenvolvimento sustentável (Pacto global e Objetivos de Desenvolvimento Sustentável - ODS).
- Princípios de sustentabilidade em licitações e contratações públicas.
- Unidades socioambientais do Poder Judiciário e Plano de Logística Sustentável, Resolução CNJ nº 400/2021.
- Agenda 2030 do Desenvolvimento Sustentável.
- Sustentabilidade organizacional.
- Desenvolvimento sustentável, meio ambiente e mudança climática.
- Economia ambiental e desenvolvimento sustentável.
- Sustentabilidade econômica, social e ambiental.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Regra precisa, com exemplos concretos; nunca invente regra ou dado.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo sustentabilidade.desenvolvimento-sustentavel.banco-N.json, onde N é o lote)
```json
{
  "materia": "sustentabilidade.desenvolvimento-sustentavel",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 114 — gestao-pessoas.fundamentos-subsistemas
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Gestão de Pessoas: Gestão de pessoas: fundamentos e subsistemas** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Conceito e evolução da gestão de pessoas (de recursos humanos a parceiros); processos ou subsistemas (agregar, aplicar, recompensar, desenvolver, manter e monitorar); recrutamento interno, externo e misto; técnicas de seleção, validade e fidedignidade; treinamento, desenvolvimento e educação, levantamento de necessidades e avaliação de resultados em quatro níveis; avaliação de desempenho (métodos, 360 graus, erros do avaliador); gestão por competências (CHA, mapeamento de lacunas); clima organizacional e sua diferença para cultura; qualidade de vida no trabalho; comportamento organizacional (níveis de análise, motivação, liderança em noções, grupos e conflitos).
Fica de fora (outras matérias tratam): Regime jurídico dos servidores públicos, estágio probatório e avaliação especial previstos em lei, direito do trabalho, folha de pagamento e cálculos trabalhistas, administração geral (planejamento, organização, direção e controle), gestão de projetos e qualidade, psicologia clínica.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Conceitos, importância, relação com os outros sistemas de organização.
- Gestão por competências.
- Análise e descrição de cargos.
- Gestão de desempenho.
- Tendências em gestão de pessoas no setor público.
- Gestão de pessoas.
- Objetivos, desafios e características da gestão de pessoas.
- Fundamentos, teorias e escolas da administração e o seu impacto na gestão de pessoas.
- Comportamento organizacional: Qualidade de vida.
- Função do órgão de recursos humanos.
- Função do órgão de recursos humanos: atribuições básicas e objetivos.
- Função do órgão de recursos humanos: Políticas e sistemas de informações gerenciais.
- Recrutamento e seleção.
- Principais técnicas de seleção de pessoas: características, vantagens e desvantagens.
- Gestão por competências: competências organizacionais, coletivas e individuais.
- Administração de cargos, carreiras e salários.
- Qualidade de vida no trabalho.
- Gestão de pessoas: equilíbrio organizacional.
- Capacitação de pessoas.
- Análise e descrição de cargos: objetivos, métodos, vantagens e desvantagens.
- Métodos de avaliação de desempenho: características, vantagens e desvantagens.
- Gestão e avaliação de desempenho.
- Recrutamento e seleção de pessoas: objetivos e características.
- Recrutamento e seleção de pessoas: principais tipos, características, vantagens e desvantagens.
- Recrutamento e seleção de pessoas.
- Gestão de desempenho: objetivos.
- Desenvolvimento e capacitação de pessoal.
- Desenvolvimento e capacitação de pessoal: levantamento de necessidades.
- Gestão de pessoas: Gestão de desempenho.
- Gestão de pessoas: Gestão por competências.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Regra precisa, com exemplos concretos; nunca invente regra ou dado.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo gestao-pessoas.fundamentos-subsistemas.banco-N.json, onde N é o lote)
```json
{
  "materia": "gestao-pessoas.fundamentos-subsistemas",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 115 — administracao-geral.comportamento-organizacional
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Administração Geral: Comportamento organizacional: motivação, liderança, grupos, conflito, poder e mudança** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Conceito e níveis do comportamento organizacional; motivação (intensidade, direção e persistência, intrínseca e extrínseca), teorias de conteúdo e de processo em profundidade: ERG de Alderfer, necessidades adquiridas de McClelland, expectativa de Vroom, equidade de Adams e justiça organizacional, fixação de objetivos de Locke, reforço de Skinner, modelo das características do trabalho, enriquecimento e ampliação de cargos; liderança: traços, estudos de Ohio e Michigan, grade gerencial, Fiedler, caminho-meta, situacional (comparação), LMX, transacional, transformacional, carismática, servidora e substitutos da liderança; grupos e equipes: tipos, estágios de Tuckman, equilíbrio pontuado, papéis, normas, coesão, folga social, pensamento grupal, polarização, grupo x equipe; conflito: visões, tipos, processo, estilos de Thomas e Kilmann, negociação distributiva e integrativa, MAANA; poder: dependência, bases de French e Raven, tipologia de Etzioni, táticas de influência, política organizacional e empowerment; mudança: forças, agentes, tipos, Lewin e campo de forças, oito etapas de Kotter, resistência e táticas de Kotter e Schlesinger, desenvolvimento organizacional, aprendizagem de circuito simples e duplo e organizações que aprendem.
Fica de fora (outras matérias tratam): Teorias da administração, funções PODC, comunicação, controle, tomada de decisão e cultura organizacional (matéria de funções administrativas); subsistemas de gestão de pessoas, clima organizacional e qualidade de vida no trabalho (matéria de gestão de pessoas); Maslow, Herzberg e McGregor aparecem aqui só como comparação; mediação e conciliação judiciais; psicologia clínica.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Comportamento organizacional.
- Motivação.
- Comportamento organizacional: relações indivíduo/organização.
- Comportamento organizacional: Liderança, motivação e desempenho.
- Liderança.
- Gestão de conflitos.
- Gestão da mudança.
- Comportamento organizacional: relações indivíduo/organização, motivação, liderança, desempenho.
- Atitudes e satisfação no trabalho.
- Direção: motivação e liderança.
- Gerenciamento de conflitos.
- Trabalho em equipe.
- Motivação e liderança.
- Trabalho em equipe: relacionamento interpessoal e empatia.
- Comportamento organizacional e relações indivíduo-organização.
- Liderança, motivação, desempenho e qualidade de vida.
- Gestão de processos de mudança organizacional: Conceito de mudança.
- Mudança e inovação organizacional.
- Grupos e equipes de trabalho.
- Liderança; Estilos de liderança e situações de trabalho.
- Teorias da motivação.
- Liderança, autoliderança e liderança de equipes.
- Aspectos comportamentais da organização: liderança, motivação, comunicação e desempenho.
- Competência interpessoal e gerenciamento de conflitos.
- Liderança e motivação.
- O indivíduo e o contexto organizacional: variáveis individuais, grupais e organizacionais.
- O indivíduo e o contexto organizacional: variáveis individuais, grupais e organizacionais: Comportamento humano no trabalho: satisfação e comprometimento.
- O indivíduo e o contexto organizacional: variáveis individuais, grupais e organizacionais: Equipes e grupos de trabalho.
- O indivíduo e o contexto organizacional: variáveis individuais, grupais e organizacionais: Competência interpessoal.
- Equipes de trabalho.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Regra precisa, com exemplos concretos; nunca invente regra ou dado.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo administracao-geral.comportamento-organizacional.banco-N.json, onde N é o lote)
```json
{
  "materia": "administracao-geral.comportamento-organizacional",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 116 — direito-administrativo.contratacao-direta
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Administrativo: Contratação direta: dispensa e inexigibilidade** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Dever de licitar; inexigibilidade do art. 74; dispensa do art. 75 e limites monetários atualizados; licitação dispensada nas hipóteses de alienação do art. 76; instrução e publicidade do processo de contratação direta do art. 72.
Fica de fora (outras matérias tratam): Modalidades e princípios licitatórios, fases, recursos, execução contratual, sanções e crimes, tratados em tópicos próprios.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Licitação: Contratação direta: dispensa e inexigibilidade.
- Contratação direta: Dispensa e inexigibilidade.
- Obrigatoriedade, dispensa e inexigibilidade.
- Licitação: obrigatoriedade, dispensa, inexigibilidade e vedação.
- Obrigatoriedade, dispensa, inexigibilidade e vedação.
- Licitação: dispensa e inexigibilidade.
- Licitação: Contratação direta.
- Dispensa e inexigibilidade.
- Dispensa e inexigibilidade de licitação.
- Licitação e contratos administrativos: Contratação direta: dispensa e inexigibilidade.
- Disposições doutrinárias: Contratação direta.
- Fracionamento de despesas.
- Lei nº 14.133/2021: contratação direta, dispensa e inexigibilidade.
- Licitações, contratação direta, dispensa, inexigibilidade e contratos administrativos.
- Dispensa de licitação.
- Inexigibilidade de licitação.
- Disposições doutrinárias: Contratação direta: dispensa e inexigibilidade.
- Licitação de obras públicas: Hipóteses de dispensa, de inexigibilidade e de vedação.
- Obrigatoriedade da licitação e contratação direta.
- Dispensa e inexigibilidade de licitação: hipóteses legais e requisitos de instrução processual.
- Hipóteses de dispensa, de inexigibilidade e de vedação.
- Dispensa e inexigibilidade. Anulação e revogação. Controle. Aspectos penais.
- Obrigatoriedade, dispensa e inexigibilidade de licitação.
- Inexigibilidade, dispensa, inexequibilidade, superfaturamento, desclassificação e alienações.
- Da contratação direta.
- Contratação direta.
- Contratação direta, inexigibilidade, dispensa e procedimentos auxiliares.
- Licitações: Contratação direta.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, literalidade da lei com o detalhe que a prova troca, jurisprudência e súmulas, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Cite artigo, lei, súmula e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Se não tiver certeza de um número, explique sem ele — nunca invente.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito-administrativo.contratacao-direta.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito-administrativo.contratacao-direta",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 117 — matematica.regra-de-tres
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Matemática e Raciocínio Lógico: Regra de três simples e composta** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Proporcionalidade direta e inversa; regra de três simples; regra de três composta com três ou mais grandezas; análise da relação entre cada grandeza e a incógnita; aplicações em compras, consumo, escalas de trabalho, produção, transporte, estoques e obras, com unidades compatíveis.
Fica de fora (outras matérias tratam): Porcentagens sucessivas, juros, escalas cartográficas em profundidade, semelhança geométrica, velocidade com variação não uniforme, produtividade variável, taxas com parcelas fixas e situações em que não há proporcionalidade. Esses temas só aparecem como contexto quando a hipótese de proporcionalidade é explicitada.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Regra de três.
- Razões e proporções: Regras de três simples e compostas.
- Regras de três simples e composta.
- Regra de três simples e composta.
- Regra de três simples.
- Proporcionalidade direta e inversa.
- Regra de três composta.
- Regra de 3 simples e composta.
- Regra de três simples e composta, proporcionalidades e porcentagens.
- Regras de três simples e compostas.
- Regra de três (simples e composta).
- Regra de três, proporcionalidade e porcentagens.
- Regra de três simples e composta, proporcionalidade e porcentagens.
- Proporcionalidade e medidas de comprimento, área, volume, massa e tempo.
- Razões, proporções, regras de três simples e composta.
- Razão, proporção, variação de grandezas e regras de três simples e composta.
- Regra de três simples e composta e porcentagens.
- Divisão proporcional, regra de sociedade e regra de três.
- Regra de três simples e composta; porcentagens.
- Regra de três simples e composta e proporcionalidade.
- Divisão proporcional: Regras de três simples e composta.
- Matemática financeira: regra de três simples e composta.
- Regra de três simples e composta, porcentagem, escalas e frações.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, cálculo com todos os dados no enunciado, interpretação de tabela ou gráfico, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Regra precisa, com exemplos concretos; nunca invente regra ou dado.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo matematica.regra-de-tres.banco-N.json, onde N é o lote)
```json
{
  "materia": "matematica.regra-de-tres",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 118 — informatica.criptografia-certificacao
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Noções de Informática: Criptografia, certificação digital e segurança de redes** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Criptografia simétrica e assimétrica, funções hash, assinatura digital, certificado digital e infraestrutura de chaves públicas, noções de ICP-Brasil, VPN, firewall e sistemas de detecção e prevenção de intrusão (IDS/IPS).
Fica de fora (outras matérias tratam): Tipos de malware, phishing, antivírus e cuidados básicos do usuário (ficam em informatica.seguranca); matemática dos algoritmos, configuração de equipamentos por fabricante e perícia forense.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Assinatura e certificação digital.
- Certificação digital.
- Princípios de segurança, confidencialidade e assinatura digital.
- Criptografia.
- Redes de computadores: Redes privadas virtuais (VPN).
- Criptografia: Conceitos básicos e aplicações.
- Criptografia simétrica e assimétrica.
- Segurança da informação: certificação digital, conceito e funcionalidades.
- Infraestrutura de chaves públicas e certificação digital.
- Protocolos e mecanismos de segurança: VPN, SSL/TLS.
- Noções de criptografia e proteção de dados: hash criptográfico (MD5, SHA-1, SHA-256), assinaturas digitais.
- Infraestrutura de chaves públicas — public key infrastructure (PKI).
- Segurança de redes de computadores.
- IDS, IPS e SIEM.
- Autenticação, criptografia, certificado digital e assinatura digital.
- Criptografia e proteção de dados em trânsito e em repouso; sistemas criptográficos simétricos e assimétricos e principais protocolos.
- Tokens e outros dispositivos de segurança.
- Comunicação segura com SSL e TLS.
- Criptografia simétrica e assimétrica;
- Certificação digital;
- Mecanismos de segurança: firewall, detecção de intrusão e autenticação.
- Criptografia, assinatura e certificação digital.
- Protocolos SSL, TLS e IPsec.
- Conceitos de Firewall.
- Ambiente de rede seguro.
- Segurança física e lógica, criptografia, protocolos, assinatura, certificação digital, hashes e esteganografia.
- Infraestrutura de chaves públicas e ICP-Brasil.
- Protocolos criptográficos.
- Hashes e algoritmos de hash.
- Esteganografia e criptoanálise.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Regra precisa, com exemplos concretos; nunca invente regra ou dado.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo informatica.criptografia-certificacao.banco-N.json, onde N é o lote)
```json
{
  "materia": "informatica.criptografia-certificacao",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 119 — legislacao.lei-8112-1990-direitos-vantagens
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Legislação: Lei 8.112/1990: direitos e vantagens** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Direitos e vantagens previstos no Título III da Lei 8.112/1990: vencimento e remuneração; indenizações, gratificações e adicionais; férias; licenças; afastamentos; concessões; tempo de serviço; direito de petição.
Fica de fora (outras matérias tratam): Provimento, vacância, deveres, proibições, responsabilização disciplinar, processo administrativo e benefícios previdenciários, tratados em tópicos próprios, salvo referência indispensável à compreensão de dispositivo incluído no escopo.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Dos Direitos e Vantagens: Do Vencimento e da Remuneração, Das Vantagens, Das Férias, Das Licenças e Dos Afastamentos;
- Remuneração.
- Direitos e vantagens.
- Lei nº 8.112/1990 e alterações: direitos e vantagens.
- Dos direitos e vantagens: do vencimento e da remuneração, das vantagens, das férias, das licenças e dos afastamentos.
- Vencimento e remuneração, vantagens, férias, licenças, afastamentos, direito de petição.
- Agentes públicos: Remuneração.
- Disposições doutrinárias: Remuneração.
- Direito de Petição.
- Dos Servidores Públicos – Lei n.º 8.112/90: do direito de petição.
- Direitos e vantagens e regime disciplinar dos servidores públicos federais.
- Regime jurídico dos servidores públicos civis da União (Lei nº 8.112/1990 e alterações): direitos e vantagens.
- Direitos e deveres dos servidores estatutários.
- Lei nº 8.112/1990: direitos e vantagens, vencimento e remuneração, vantagens, férias, licenças, afastamentos.
- Lei nº 8.112/1990: direito de petição.
- Direitos e deveres dos agentes públicos.
- Regime jurídico dos servidores públicos federais: vencimento básico.
- Regime jurídico dos servidores públicos federais: licença.
- Efetividade, estabilidade, vitaliciedade, remuneração, direitos, deveres e responsabilidade dos agentes públicos.
- Vencimento, remuneração, vantagens, férias, licenças, afastamentos e direito de petição.
- Remuneração, direitos, deveres e responsabilidade dos agentes públicos.
- Lei nº 8.112/1990: direitos e vantagens; regime disciplinar.
- Licenças e aposentadoria de servidores públicos federais.
- Direitos e vantagens dos servidores públicos.
- Lei nº 8.112/1990: férias, licenças, vantagens, jornada, progressão, promoção, remuneração e auxílios.
- Férias, licenças, vantagens, jornada de trabalho, remuneração, auxílios, progressão e promoção dos servidores.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, literalidade da lei com o detalhe que a prova troca, jurisprudência e súmulas, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Cite artigo, lei, súmula e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Se não tiver certeza de um número, explique sem ele — nunca invente.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo legislacao.lei-8112-1990-direitos-vantagens.banco-N.json, onde N é o lote)
```json
{
  "materia": "legislacao.lei-8112-1990-direitos-vantagens",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 120 — direito-civil.bens
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Civil: Bens** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Bens: conceito, regimes e classificações em si, reciprocamente considerados e públicos; fatos e negócios jurídicos, forma e prova, vícios, invalidade, ilícitos, prescrição e decadência; obrigações e formas de adimplemento e extinção; contratos em geral; responsabilidade civil e indenização.
Fica de fora (outras matérias tratam): Posse e direitos reais em espécie aprofundados; família e sucessões em regime completo; contratos nominados em detalhe; consumo e regimes especiais de responsabilidade, salvo distinção das normas gerais do Código Civil.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Bens.
- Bens imóveis, móveis e públicos.
- Bens: Diferentes classes.
- Bens Corpóreos e incorpóreos.
- Bens no comércio e fora do comércio.
- Bem de família.
- Dos Bens: classificação, afetação e desafetação.
- Bens considerados em si mesmos.
- Bens reciprocamente considerados.
- Dos Bens.
- Bens: classificação dos bens (fungíveis e infungíveis, móveis e imóveis, públicos e particulares), bens de uso comum, bens dominicais e bens indisponíveis da Administração Pública.
- Bens: Conceito e classificações.
- Bens e diferentes classes de bens.
- Bens, diferentes classes de bens.
- Bens: Conceito e espécies.
- Bens considerados em si mesmos: móveis, imóveis, fungíveis, consumíveis, divisíveis, singulares e coletivos.
- Bens reciprocamente considerados: principais, acessórios e benfeitorias.
- Bens: Conceito, espécies e classificação.
- Bens e fatos jurídicos.
- Coisas e bens: imóveis, móveis, fungíveis e consumíveis.
- Das coisas divisíveis e indivisíveis.
- Das coisas singulares e coletivas.
- Dos bens reciprocamente considerados.
- Dos bens públicos e particulares.
- Das coisas que estão fora do comércio.
- Domicílio e bens.
- Bens corpóreos e incorpóreos, classificação dos bens e bens públicos.
- Parte geral: bens.
- Pessoas. Bens. Atos e Negócios Jurídicos.
- Bens e suas classes: bens corpóreos e incorpóreos, bens no comércio e fora dele.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, literalidade da lei com o detalhe que a prova troca, jurisprudência e súmulas, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Cite artigo, lei, súmula e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Se não tiver certeza de um número, explique sem ele — nunca invente.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito-civil.bens.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito-civil.bens",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 121 — informatica.seguranca-normas-iso27001
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Informática: Gestão de segurança da informação: ISO/IEC 27001 e 27002** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Pilares da segurança da informação (confidencialidade, integridade, disponibilidade); família ISO/IEC 27000; sistema de gestão de segurança da informação (SGSI); estrutura da ISO/IEC 27001:2022 (cláusulas 4 a 10, avaliação e tratamento de riscos, Declaração de Aplicabilidade, auditoria interna, análise crítica, melhoria contínua e certificação); ISO/IEC 27002:2022 (93 controles em 4 temas, atributos, controles novos) e diferenças em relação à versão de 2013; gestão de incidentes de segurança (evento, incidente, resposta, lições aprendidas, evidências); gestão de continuidade de negócios (análise de impacto, RTO, RPO, plano de continuidade, prontidão de TIC).
Fica de fora (outras matérias tratam): Tipos de malware e golpes (outra matéria), criptografia em detalhe, configuração de firewall e ferramentas, ISO/IEC 27005 e ISO 31000 em profundidade, LGPD, normas complementares do governo federal e frameworks como NIST CSF, CIS Controls e COBIT.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Segurança da informação.
- Gestão de segurança da informação.
- Gestão de Segurança da Informação e Privacidade.
- Conhecimentos em estruturação da gestão de segurança da informação, elaboração de Políticas e Normas de segurança, e acompanhamento do desempenho.
- Gestão de riscos da segurança da Informação.
- Conhecimentos na estruturação da disciplina de Gestão de Riscos de SI, e na condução de Análises de Riscos da SI.
- Tecnologia da informação e segurança da informação.
- Tecnologia da informação e segurança de dados.
- Políticas de segurança da informação.
- Referências principais: ISO 31000, ISO 31010, ISSO 27005 (em suas versões mais recentes).
- Planejamento, identificação e análise de riscos.
- Plano de continuidade de negócio.
- Classificação e controle de ativos de informação, segurança de ambientes físicos e lógicos, controles de acesso.
- Definição, implantação e gestão de políticas de segurança e auditoria.
- Prevenção e tratamento de incidentes.
- Gestão de riscos de segurança da informação.
- Normas ABNT NBR ISO/IEC 27001:2022 e 27002:2022.
- Políticas, procedimentos e gerenciamento da segurança da informação.
- Procedimentos de segurança, conceitos gerais de gerenciamento.
- Noções de segurança da informação, incluindo conceitos de confidencialidade, integridade, disponibilidade e autenticidade.
- Gestão de riscos e continuidade de negócio.
- Gestão de riscos.
- Confiabilidade, integridade e disponibilidade.
- Gestão de segurança da informação: NBR ISO/IEC 27001 e NBR ISO/IEC 27002.
- Noções de segurança da informação.
- Gestão de continuidade do negócio.
- Prevenção e tratamento de incidentes de segurança da informação.
- Segurança da Informação e Proteção de Dados: princípios de confidencialidade, integridade, disponibilidade e rastreabilidade.
- ABNT NBR 27002:2019;
- ABNT NBR 27035-3:2021;

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Regra precisa, com exemplos concretos; nunca invente regra ou dado.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo informatica.seguranca-normas-iso27001.banco-N.json, onde N é o lote)
```json
{
  "materia": "informatica.seguranca-normas-iso27001",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 122 — direito-administrativo.procedimento-licitatorio
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Administrativo: Procedimento licitatório, anulação, revogação e recursos** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Fases e inversão de fases do art. 17; anulação e revogação pelo art. 71; recursos e pedidos de reconsideração dos arts. 165 a 168 da Lei 14.133/2021.
Fica de fora (outras matérias tratam): Contratação direta, modalidades em profundidade, critérios de julgamento e habilitação além do necessário para compreender as fases, execução e sanções contratuais.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Licitação: Procedimento.
- Procedimento licitatório.
- Anulação, revogação e recursos administrativos.
- Licitação: procedimento, revogação e anulação.
- Noções de Licitações e Contratos (Lei nº 14.133/2021 e suas alterações): Estudo Técnico Preliminar.
- Noções de Licitações e Contratos (Lei nº 14.133/2021 e suas alterações): Termo de Referência.
- Procedimento, revogação e anulação.
- Disposições doutrinárias: Procedimento.
- Disposições doutrinárias: Anulação e revogação.
- Licitação e contratos administrativos: Procedimento.
- Licitação: anulação, revogação e recursos administrativos.
- Habilitação.
- Julgamento.
- Fases do procedimento licitatório.
- Anulação e revogação da licitação.
- Adjudicação, homologação e formalização contratual.
- Anulação, revogação, recursos, sanções e crimes em licitações e contratos.
- Lei nº 14.133/2021: procedimento.
- Procedimento, revogação e anulação da licitação.
- Noções de Licitações e Contratos (Lei nº 14.133/2021): Estudo Técnico Preliminar.
- Noções de Licitações e Contratos (Lei nº 14.133/2021): Termo de Referência.
- Procedimento.
- Licitações e contratos administrativos: Elaboração de projetos básicos para contratação de bens e serviços.
- Habilitação e impedimentos.
- Licitações e contratos. 22.1 § 1º do Art. 65 e Art. 69 da Lei nº 14.133/2021.
- Licitação de obras públicas: Procedimentos.
- Licitação de obras públicas: Revogação e anulação.
- Licitação de obras públicas: Objeto da licitação, homologação e adjudicação.
- Procedimentos da licitação.
- Artefatos de Planejamento das Contratações: estudo técnico preliminar (ETP), termo de referência e projeto básico.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, literalidade da lei com o detalhe que a prova troca, jurisprudência e súmulas, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Cite artigo, lei, súmula e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Se não tiver certeza de um número, explique sem ele — nunca invente.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito-administrativo.procedimento-licitatorio.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito-administrativo.procedimento-licitatorio",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 123 — portugues.termos-da-oracao
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Língua Portuguesa: Termos da oração** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Identificação e análise do sujeito e do predicado, objetos direto e indireto, complemento nominal, adjuntos adnominal e adverbial, predicativo, aposto e vocativo, com distinção entre termos ligados ao verbo, ao nome e à oração.
Fica de fora (outras matérias tratam): Período composto em profundidade, classificação completa das orações subordinadas e estudo isolado da regência verbal e nominal.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Organização sintática das frases: termos e orações.
- Morfossintaxe.
- Sintaxe.
- Ordem direta e inversa.
- Termos da oração.
- Sintaxe: termos da oração e relações sintático-semânticas estabelecidas entre termos, orações, períodos ou parágrafos (período simples e período composto por coordenação e por subordinação).
- Análise morfossintática.
- Organização sintática, termos e orações e ordem direta e inversa.
- Termos essenciais, integrantes e acessórios da oração e vocativo.
- Termos da oração, vocativo, crase e pronomes: emprego, formas de tratamento e colocação.
- Sintaxe: funções sintáticas e suas relações no período simples e no período composto.
- Sintaxe e construção frasal.
- Organização sintática das frases: termos e orações; ordem direta e inversa.
- Análise sintática: termos essenciais, integrantes e acessórios da oração.
- Termos essenciais, integrantes e acessórios da oração, vocativo e crase.
- Termos essenciais, integrantes e acessórios da oração, vocativo, crase e pronomes.
- Sintaxe: funções sintáticas e suas relações nos períodos simples e compostos.
- Língua portuguesa: modalidade culta usada contemporaneamente no Brasil: Morfossintaxe.
- Sintaxe: termos essenciais, integrantes e acessórios da oração.
- Sintaxe: reconhecimento dos termos da oração.
- Análise morfossintática de período simples.
- Funções sintáticas.
- Termos e orações e ordem direta e inversa.
- Termos da oração e relações sintático-semânticas entre orações, períodos e parágrafos.
- Textos da esfera jornalística/publicitária — Análise linguística: Analise a estruturação sintática de sentenças nos textos, observando como a ordem dos termos (sujeito, verbo, complemento) reflete o foco jornalístico (em manchetes, por exemplo).

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Regra precisa, com exemplos concretos; nunca invente regra ou dado.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo portugues.termos-da-oracao.banco-N.json, onde N é o lote)
```json
{
  "materia": "portugues.termos-da-oracao",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 124 — direito-administrativo.sancoes-crimes-licitacoes
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Administrativo: Sanções administrativas e crimes em licitações** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Infrações administrativas dos arts. 155 a 163 da Lei nº 14.133/2021; advertência, multa, impedimento de licitar e contratar, declaração de inidoneidade, processo sancionador, seus efeitos, prescrição, cadastros, mora e reabilitação; crimes dos arts. 337-E a 337-P do Código Penal.
Fica de fora (outras matérias tratam): Regras gerais de licitação, habilitação e execução contratual fora do necessário para compreender as infrações delimitadas; crimes de outros capítulos, responsabilização civil geral e sanções de outras leis, salvo conexão expressa dos arts. 155 a 163.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Sanções e procedimento sancionatório.
- Crimes em licitações e contratos administrativos.
- Elaboração e fiscalização de contratos: Definição e aplicação de penalidades e sanções administrativas.
- Licitação: sanções.
- Definição e aplicação de penalidades e sanções administrativas.
- Crimes e sanções penais na licitação (Lei nº 14.133/2021 e suas alterações).
- Sanções penais.
- Crimes em licitações e contratos administrativos (Lei nº 14.133/2021 e suas alterações).
- Disposições doutrinárias: Sanções administrativas.
- Sanções e meios de controle.
- Crimes nas licitações e contratos da Administração Pública.
- Infrações e sanções administrativas.
- Crimes da Lei de Licitações e Contratos.
- Crimes relativos à licitação (Lei nº 8.666, de 21/6/1993).
- Licitações e Contratos Administrativos.
- Crimes e sanções em licitações.
- Legislação penal especial: licitações.
- Leis Federais n. 8.666/1993.
- Leis Federais n. 14.133/2021.
- Responsabilidade administrativa por infrações.
- Lei nº 8.666/1993 (crimes nas licitações e contratos da administração pública).
- Lei nº 8.666/1993 e suas alterações (Crimes nas licitações e contratos da administração pública).
- Penalidades e sanções administrativas por inexecução contratual.
- Crimes previstos na Lei nº 14.133/2021 e suas alterações (Lei de licitações).
- Licitação pública - Lei nº 14.133/2021: Das irregularidades.
- Lei nº 14.133/2021 (Crimes em licitações e contratos administrativos).
- Fraude na Execução do Contrato.
- Crimes e sanções penais na licitação (Lei nº 8.666/1993 e Lei nº 14.133/2021).
- Licitações e contratos administrativos: Definição e aplicação de penalidades e sanções administrativas.
- Infrações contratuais e sanções em contratos com a administração pública.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, literalidade da lei com o detalhe que a prova troca, jurisprudência e súmulas, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Cite artigo, lei, súmula e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Se não tiver certeza de um número, explique sem ele — nunca invente.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito-administrativo.sancoes-crimes-licitacoes.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito-administrativo.sancoes-crimes-licitacoes",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 125 — direito-constitucional.defesa-estado-instituicoes
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Constitucional: Defesa do Estado e das Instituições Democráticas** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Sistema constitucional de crises; Conselhos da República e de Defesa Nacional; estados de defesa e sítio (pressupostos, consulta, autorização, duração, restrições, controles e efeitos); noções institucionais das Forças Armadas; organização constitucional da segurança pública, atribuições, subordinação, guardas municipais e segurança viária.
Fica de fora (outras matérias tratam): Intervenção federal em profundidade; regime detalhado das Forças Armadas e serviço militar; regime dos militares estaduais e normas infraconstitucionais de segurança pública.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Defesa do Estado e das instituições democráticas.
- Defesa do Estado e das instituições democráticas: Segurança pública.
- Poder executivo: Conselho da República e de Defesa Nacional.
- Defesa do Estado e das instituições democráticas: estado de defesa, estado de sítio, Forças Armadas e segurança pública.
- Conselho da República e de Defesa Nacional.
- Estado de defesa e estado de sítio.
- Poder Executivo: Do Conselho da República e do Conselho de Defesa Nacional.
- Conselho da república.
- Conselho de defesa nacional.
- Defesa do Estado e das instituições democráticas: Organização da segurança pública.
- Conselho da República e Conselho de Defesa Nacional.
- Defesa do Estado e das instituições democráticas: segurança pública; organização da segurança pública.
- Estado de Exceção.
- Estado de defesa.
- Estado de sítio.
- Defesa do Estado e das instituições democráticas, segurança pública e organização da segurança pública.
- Defesa do Estado e das instituições democráticas: Estado de defesa e estado de sítio.
- Conselho da República e Conselho de Defesa.
- Segurança pública: Organização da segurança pública.
- Constituição Federal: defesa do Estado e das instituições democráticas.
- Segurança Pública conforme o artigo 144 da Constituição Federal.
- Funções essenciais à Justiça, defesa do Estado e instituições democráticas.
- Sistema constitucional das crises.
- Organização da Segurança Pública.
- Defesa do Estado e das instituições democráticas: Atribuições constitucionais da Polícia Judiciária.
- Atribuições constitucionais da Polícia Judiciária.
- Funções essenciais à justiça: Defesa do Estado e das instituições democráticas.
- Segurança pública e artigo 144 da Constituição Federal.
- Constituição Federal: defesa do Estado, instituições democráticas e segurança pública.
- Defesa do Estado e das instituições democráticas; segurança pública; forças armadas.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, literalidade da lei com o detalhe que a prova troca, jurisprudência e súmulas, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Cite artigo, lei, súmula e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Se não tiver certeza de um número, explique sem ele — nunca invente.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito-constitucional.defesa-estado-instituicoes.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito-constitucional.defesa-estado-instituicoes",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 126 — direito-civil.pessoas-juridicas
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Civil: Pessoas jurídicas** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Conceito, classificação e espécies de pessoas jurídicas, entes despersonalizados, constituição, registro, capacidade e administração, domicílio e responsabilidade, associações e fundações privadas, desconsideração da personalidade jurídica pelo art. 50 do Código Civil e extinção.
Fica de fora (outras matérias tratam): Pessoas naturais, modalidades de sociedades empresárias em espécie, rito processual da desconsideração da personalidade jurídica e direitos reais.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Pessoas jurídicas.
- Desconsideração da personalidade jurídica.
- Pessoas jurídicas: Disposições gerais.
- Pessoas jurídicas: Constituição.
- Pessoas jurídicas: Extinção.
- Associações.
- Fundações.
- Pessoas jurídicas: Desconsideração da personalidade jurídica.
- Pessoas jurídicas: Conceito e elementos caracterizadores.
- Pessoas jurídicas: Capacidade e direitos da personalidade.
- Pessoas jurídicas: Sociedades de fato.
- Pessoas jurídicas: Associações.
- Pessoas jurídicas: Fundações.
- Teoria da desconsideração da personalidade jurídica.
- Pessoas jurídicas: domicílio.
- Pessoas jurídicas: Grupos despersonalizados.
- Pessoas jurídicas: Responsabilidade da pessoa jurídica e dos sócios.
- Das Pessoas Jurídicas.
- Desconsideração inversa.
- Sociedades de fato, grupos despersonalizados, associações.
- Constituição.
- Pessoas jurídicas: sociedades, fundações.
- Pessoas jurídicas: responsabilidade.
- Sociedades, fundações.
- Extinção.
- Pessoas jurídicas. Disposições gerais. Constituição. Extinção.
- Pessoas jurídicas: sociedades de fato, grupos despersonalizados, associações.
- Pessoa jurídica.
- Extinção da pessoa jurídica.
- Domicílio da pessoa jurídica.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, literalidade da lei com o detalhe que a prova troca, jurisprudência e súmulas, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Cite artigo, lei, súmula e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Se não tiver certeza de um número, explique sem ele — nunca invente.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito-civil.pessoas-juridicas.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito-civil.pessoas-juridicas",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 127 — direito-civil.contratos
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Civil: Contratos em geral** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Princípios, função social, boa-fé e interpretação; classificação; formação e negócios com terceiros; contratos aleatórios e preliminares; vícios redibitórios; evicção; extinção e resolução por onerosidade excessiva.
Fica de fora (outras matérias tratam): Contratos típicos específicos, direitos reais, obrigações em geral e responsabilidade extracontratual.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Contratos em geral.
- Contratos.
- Contratos: Disposições gerais.
- Contratos: Princípios.
- Contratos: Extinção.
- Contratos: Interpretação.
- Contratos: Classificação.
- Contratos: Contratos em geral.
- Princípios.
- Contratos em geral: disposições gerais.
- Dos contratos em geral e em espécie.
- Dos vícios redibitórios e da Evicção.
- Resolução por onerosidade excessiva.
- Evicção.
- Classificação dos contratos.
- Extinção dos contratos.
- Disposições gerais.
- Dos contratos em geral.
- Contratos. Princípios.
- Contratos em geral. Disposições gerais.
- Contratos em geral: princípios contratuais.
- Formação, validade, interpretação, efeitos e extinção dos contratos.
- Revisão e resolução contratual.
- Arras.
- Teoria geral dos contratos.
- Extinção do contrato.
- Vícios redibitórios.
- Contrato preliminar.
- Contratos mercantis: características.
- Interpretação dos contratos.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, literalidade da lei com o detalhe que a prova troca, jurisprudência e súmulas, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Cite artigo, lei, súmula e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Se não tiver certeza de um número, explique sem ele — nunca invente.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito-civil.contratos.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito-civil.contratos",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 128 — direito-civil.responsabilidade-civil
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Civil: Responsabilidade civil** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Atos ilícitos, abuso de direito, excludentes de ilicitude, responsabilidade subjetiva e objetiva, nexo causal, dano material, moral e estético, quantificação da indenização, excludentes do nexo, responsabilidade por fato de terceiro, do animal e da coisa.
Fica de fora (outras matérias tratam): Responsabilidade civil do Estado, responsabilidade nas relações de consumo e trabalhistas como temas autônomos, inadimplemento contratual e prescrição.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Responsabilidade civil.
- Responsabilidade civil objetiva e subjetiva.
- Atos ilícitos.
- Dano material.
- Obrigação de indenizar.
- Da responsabilidade civil.
- Dano moral.
- Responsabilidade civil: Obrigação de indenizar.
- Responsabilidade civil: Indenização.
- Da obrigação de indenizar.
- Responsabilidade contratual e extracontratual.
- Da indenização.
- Responsabilidade civil objetiva e subjetiva, obrigação de indenizar e dano material.
- Fatos jurídicos: Atos jurídicos ilícitos.
- Responsabilidade civil objetiva e subjetiva: Obrigação de indenizar.
- Responsabilidade civil objetiva e subjetiva: Dano material.
- Nexo causal.
- Atos jurídicos lícitos e ilícitos, requisitos de configuração do ato ilícito e excludentes.
- Responsabilidade civil, obrigação de indenizar e novo direito de danos, inclusive reflexos na imputação e no nexo causal.
- Responsabilidade por fato de outrem.
- Responsabilidade por fato da coisa.
- Dano moral e material.
- Indenização.
- Responsabilidade civil, obrigação de indenizar e indenização.
- Atos jurídicos ilícitos; abuso de direito.
- Responsabilidade civil: obrigação de indenizar; indenização.
- Responsabilidade objetiva e subjetiva.
- Caso fortuito ou de força maior, fato de terceiro, fato do credor e ausência de culpa.
- Responsabilidade civil e preferências e privilégios creditórios.
- Obrigações por atos ilícitos.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, literalidade da lei com o detalhe que a prova troca, jurisprudência e súmulas, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Cite artigo, lei, súmula e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Se não tiver certeza de um número, explique sem ele — nunca invente.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito-civil.responsabilidade-civil.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito-civil.responsabilidade-civil",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 129 — direito-penal.crimes-incolumidade-fe-publica
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Penal: Crimes contra a incolumidade pública e a fé pública** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Noções sobre crimes de perigo comum, incêndio, explosão e outras condutas perigosas, falsificação de moeda, falsidade documental, uso de documento falso e proteção penal da fé pública.
Fica de fora (outras matérias tratam): Crimes contra a vida e o patrimônio em geral, delitos funcionais e legislação especial que não esteja relacionada ao recorte.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Crimes contra a fé pública.
- Crimes contra a incolumidade pública.
- Crimes contra a Fé Pública: falsidade de títulos e outros papéis públicos.
- Falsidade documental.
- Fraudes em certames de interesse público.
- Crimes contra a fé pública. Falsidade de títulos e outros papéis públicos; falsidade documental; fraudes em certames de interesse público.
- Crimes contra a fé-pública.
- Crimes em espécie previstos no Código Penal: Crimes contra a incolumidade pública.
- Crimes em espécie previstos no Código Penal: Crimes contra a paz pública.
- Crimes em espécie previstos no Código Penal: Crimes contra a fé pública.
- Dos crimes contra a fé pública: da falsidade documental.
- Dos crimes contra a incolumidade pública.
- Crime: crimes contra a incolumidade pública.
- Crime: crimes contra a fé pública.
- Crimes contra a incolumidade pública e a paz pública.
- Crimes contra a fé pública de interesse da Administração Pública.
- Penas: Crimes contra a incolumidade pública.
- Penas: Crimes contra a paz pública.
- Penas: Crimes contra a fé pública.
- Crimes contra a fé pública em detrimento do INSS.
- Parte Especial: crimes contra a fé pública.
- Crimes contra a fé pública: falsidade de títulos, papéis públicos e documentos.
- Crimes contra a fé pública: falsidade de títulos e papéis públicos, falsidade documental e fraudes em certames.
- Crimes em espécie do Código Penal: Crimes contra a incolumidade pública.
- Crimes em espécie do Código Penal: Crimes contra a paz pública.
- Crimes em espécie do Código Penal: Crimes contra a fé pública.
- Crimes contra o patrimônio, contra a incolumidade pública e contra a fé pública.
- Crimes contra a fé pública e falsidade documental.
- Dos crimes contra a fé pública e delitos das fraudes em certames de interesse público.
- Crimes contra a incolumidade pública. Crimes contra a paz pública.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, literalidade da lei com o detalhe que a prova troca, jurisprudência e súmulas, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Cite artigo, lei, súmula e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Se não tiver certeza de um número, explique sem ele — nunca invente.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito-penal.crimes-incolumidade-fe-publica.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito-penal.crimes-incolumidade-fe-publica",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 130 — direito.declaracao-universal-direitos-humanos
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Noções de Direitos Humanos e Legislação: Declaração Universal dos Direitos Humanos** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Preâmbulo e arts. 1º a 30 da Declaração Universal dos Direitos Humanos: princípios, direitos civis e políticos, direitos econômicos, sociais e culturais, deveres e regras de interpretação.
Fica de fora (outras matérias tratam): Tratados posteriores, sistemas regionais de proteção, declarações distintas e análise de casos concretos de direito interno.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Declaração Universal dos Direitos Humanos.
- Declaração Universal dos Direitos Humanos e Princípios de Yogyakarta +10.
- Declaração Universal dos Direitos Humanos (Resolução 217-A (III) - da Assembleia Geral das Nações Unidas, 1948).
- A Declaração Universal dos Direitos do Homem (ONU).
- Declaração Universal dos Direitos Humanos e Agenda 2030 da ONU.
- Fontes dos Direitos Humanos: Declaração Universal dos Direitos Humanos.
- Direitos civis, políticos, econômicos e culturais.
- Declaração Universal dos Direitos Humanos de 1948.
- Resolução nº 217-A (III) 1948 - Declaração Universal dos Direitos Humanos.
- Declaração Universal dos Direitos Humanos, adotada pela Resolução nº 217 da Assembleia Geral da ONU.
- Direitos civis, políticos, econômicos, sociais e culturais.
- Instrumentos internacionais e Declaração Universal dos Direitos Humanos.
- Fontes, instrumentos internacionais e Declaração Universal dos Direitos Humanos.
- Declaração Universal dos Direitos Humanos e Código de Conduta para Funcionários Responsáveis pela Aplicação da Lei.
- Declaração Universal dos Direitos Humanos – adotada pela Assembleia Geral das Nações Unidas em 10 de dezembro de 1948.
- Tratados Internacionais de Proteção aos Direitos Humanos: Declaração Universal dos Direitos Humanos (1948).
- A Declaração Universal dos Direitos Humanos e o Direito Interno Brasileiro.
- Declaração Universal dos Direitos Humanos, proclamada pela Resolução nº 217A (III) da Assembleia Geral das Nações Unidas, de 10 de dezembro de 1948.
- Sistema Global de Proteção dos Direitos Humanos: Declaração Universal dos Direitos Humanos (1948).
- Declaração Universal dos Direitos Humanos, proclamada pela Assembleia Geral das Nações Unidas, de 10 de dezembro de 1948.
- Declaração Universal dos Direitos Humanos (1948).
- Declaração Universal dos Direitos Humanos (ONU).
- Declaração Universal.
- Fontes e instrumentos internacionais, Declaração Universal e Convenção Americana sobre Direitos Humanos.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, literalidade da lei com o detalhe que a prova troca, jurisprudência e súmulas, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Cite artigo, lei, súmula e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Se não tiver certeza de um número, explique sem ele — nunca invente.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito.declaracao-universal-direitos-humanos.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito.declaracao-universal-direitos-humanos",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 131 — direito.definicao-conceito-direitos-humanos
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Noções de Direitos Humanos e Legislação: Definição e conceito de direitos humanos** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Conceito, fundamento na dignidade humana, titularidade, características, princípios, dimensões didáticas, reconhecimento internacional e proteção constitucional brasileira dos direitos humanos.
Fica de fora (outras matérias tratam): Estudo detalhado de tratados temáticos, sistemas internacionais de petições, regras processuais de tribunais e direitos de grupos específicos, salvo referências breves necessárias à compreensão.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Teoria geral dos Direitos Humanos.
- Direitos Humanos e Direitos Fundamentais.
- Teoria geral dos direitos fundamentais.
- Os fundamentos filosóficos dos Direitos Humanos.
- Fontes, classificação, princípios, características e gerações de direitos humanos.
- Direitos humanos.
- Teoria geral dos direitos humanos: Conceitos, terminologia, estrutura normativa, fundamentação.
- Conceito, terminologia, estrutura normativa, fundamentação.
- Conceito e fundamentação.
- Direitos Humanos na CRFB/88.
- Teoria geral dos direitos humanos: evolução histórica, conceito, classificações e características.
- Evolução histórica, conceito, classificações e características dos direitos humanos.
- Teoria geral dos Direitos Humanos: Direitos humanos e direitos fundamentais.
- Teoria geral dos Direitos Humanos: Gerações ou dimensões dos direitos fundamentais.
- Fontes dos Direitos Humanos.
- Teoria geral dos direitos humanos: conceito, terminologia, eficácia vertical e horizontal e características.
- Gerações de direitos humanos.
- Direitos humanos no ordenamento brasileiro e na Constituição Federal de 1988.
- Conceitos, terminologia, estrutura normativa, fundamentação.
- Direitos humanos: conceito.
- Direitos humanos: abrangência.
- Direitos humanos, direitos fundamentais e dimensões dos direitos fundamentais.
- Tratados internacionais no Brasil, natureza jurídica, incorporação e controles de constitucionalidade e convencionalidade.
- Teoria geral, conceitos, terminologia, eficácia, características, gerações e afirmação histórica dos direitos humanos.
- Direitos humanos e responsabilidade do Estado, no ordenamento brasileiro e na Constituição.
- Direitos humanos, discriminação e exclusão social, inclusive situação de grupos vulneráveis.
- Direitos humanos, discriminação e exclusão social de grupos minoritários e vulneráveis.
- Direitos Humanos: Conceito, princípios essenciais e gerações de direitos humanos.
- Definição e conceito de direitos humanos.
- Teoria geral dos direitos humanos: conceitos, terminologia, estrutura normativa e fundamentação.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, literalidade da lei com o detalhe que a prova troca, jurisprudência e súmulas, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Cite artigo, lei, súmula e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Se não tiver certeza de um número, explique sem ele — nunca invente.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito.definicao-conceito-direitos-humanos.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito.definicao-conceito-direitos-humanos",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 132 — controle-externo.tribunais-contas
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Controle externo e administração pública: Controle externo, tribunais de contas e tomada de contas especial** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Fiscalização contábil, financeira, orçamentária, operacional e patrimonial; relação entre Legislativo e tribunais de contas; competências, processos de contas e tomada de contas especial.
Fica de fora (outras matérias tratam): Ritos internos específicos de cada corte, prazos processuais locais, valores de alçada e análise de caso concreto ou de imputação individual.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Controle pelos Tribunais de Contas.
- Tribunais de Contas: funções, natureza jurídica e eficácia das decisões.
- Controle da atividade financeira do Estado: espécies e sistemas.
- Tomadas e prestações de contas.
- Controle externo.
- Procedimentos em processos de prestação de contas da Administração Pública Federal.
- Tribunal de Contas da União (TCU), Tribunais de Contas dos Estados e do Distrito Federal.
- Sistemas de controle externo.
- Ministério Público de Contas.
- Tribunais de Contas.
- Controle da administração pública: controle pelos tribunais de contas.
- Controle legislativo e pelos Tribunais de Contas.
- O Poder Legislativo e os Tribunais de Contas.
- Tribunais de contas: Natureza jurídica.
- Conceito, abrangência e espécies.
- Controles: conformidade e avaliação de políticas públicas.
- Controle de constitucionalidade exercido pelos Tribunais de Contas.
- Competências constitucionais dos Tribunais de Contas: emissão de parecer prévio, julgamento de contas, apreciação da legalidade atos de pessoal, poder geral de cautela, dever de representação, apreciação de denúncias, auditorias e inspeções.
- Precedentes do STF - Supremo Tribunal Federal sobre competências constitucionais dos Tribunais de Contas.
- O princípio do devido processo legal aplicado aos Tribunais de Contas.
- Tomada de contas.
- Prestação e tomada de contas.
- Controle externo no Brasil.
- Contas de governo, contas de gestão e tomada de contas especial.
- Tribunais de Contas: TCU, tribunais estaduais e do Distrito Federal; natureza, competência, jurisdição e organização do TCE-PA.
- Tribunal de Contas da União: natureza, competência e jurisdição.
- Tribunal de Contas da União: natureza, competência e jurisdição: Organização.
- Julgamento e fiscalização.
- Controle interno e externo da contabilidade pública.
- E controle externo da arrecadação tributária pelos Tribunais de Contas.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, literalidade da lei com o detalhe que a prova troca, jurisprudência e súmulas, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Cite artigo, lei, súmula e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Se não tiver certeza de um número, explique sem ele — nunca invente.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo controle-externo.tribunais-contas.banco-N.json, onde N é o lote)
```json
{
  "materia": "controle-externo.tribunais-contas",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 133 — processo-civil.acao
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Processual Civil: Ação** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Natureza jurídica e elementos da ação; interesse e legitimidade; teorias, carência, pedidos e cumulação; jurisdição, competência e critérios gerais; conexão, continência; processo, procedimento e pressupostos; sujeitos, capacidades, representação; formação, suspensão e extinção processual.
Fica de fora (outras matérias tratam): Intervenção de terceiros, substituição processual aprofundada, litisconsórcio em detalhe, atos e prazos processuais como tema próprio, nulidades em profundidade, recursos e procedimentos especiais.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Jurisdição e competência.
- Ação.
- Condições da ação.
- Pressupostos processuais.
- Jurisdição e ação: conceito, natureza e características.
- Ação: Classificação.
- A Ação.
- A Ação: Conceito, natureza, elementos e características.
- A Ação: Condições da ação.
- A Ação: Classificação.
- Processo e procedimento.
- Jurisdição e ação.
- Ação: Conceito, natureza, elementos e características.
- Da jurisdição e da ação: conceito, natureza e características.
- Das condições da ação.
- Direito de ação.
- Concurso e cumulação de ações.
- Ação: Condições da ação.
- Ação: Condições da ação, Classificação.
- Ação: conceito, natureza jurídica, teorias, condições, identificação e classificação.
- Elementos da ação.
- Classificação das ações.
- Classificação e critérios identificadores.
- Jurisdição, ação e processo.
- Ação: Condições.
- Ação: Conexão e continência.
- Ação: Concurso e cumulação.
- Jurisdição e ação: condições da ação.
- Processo: conceito, natureza jurídica, teorias e pressupostos processuais.
- Ação: conceito, natureza, elementos, características, condições, classificação, conexão, continência, concurso e cumulação.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, literalidade da lei com o detalhe que a prova troca, jurisprudência e súmulas, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Cite artigo, lei, súmula e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Se não tiver certeza de um número, explique sem ele — nunca invente.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo processo-civil.acao.banco-N.json, onde N é o lote)
```json
{
  "materia": "processo-civil.acao",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 134 — portugues.variacao-linguistica-norma-linguistica
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Língua Portuguesa: Variação linguística e norma linguística** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Variação geográfica, social, situacional, histórica e de modalidade; adequação ao contexto; conceito de norma linguística; distinções entre norma-padrão, norma culta e usos efetivos; prestígio, preconceito e mudança linguística.
Fica de fora (outras matérias tratam): Análise sintática detalhada, classificação morfológica, ortografia e história externa da língua sem relação direta com variação e norma.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Linguística: variação linguística, norma linguística.
- Norma culta.
- Norma padrão.
- Registros de linguagem.
- Variação linguística: norma culta.
- Norma-padrão da língua portuguesa.
- Variação linguística: norma padrão.
- Variação linguística.
- Níveis de linguagem, adequação linguística, análise de atos oficiais.
- Uso da língua em diversos contextos sociais.
- A variação linguística: as diversas modalidades do uso da língua adequada às várias situações de comunicação.
- Variação linguística e suas implicações para o ensino de Língua Portuguesa.
- Língua portuguesa: modalidade culta usada contemporaneamente no Brasil.
- ANÁLISE LINGUÍSTICA: norma culta e variedades linguísticas.
- A relação entre a oralidade e a escrita.
- A linguagem da Internet.
- Textos da esfera pública/oficial — Análise linguística: Avalie a adequação e a necessidade do uso da norma de referência do português brasileiro em virtude da formalidade exigida pelo contexto.
- Textos da esfera jornalística/publicitária — Análise linguística: Identifique a adequação à variedade linguística e as eventuais transgressões estilísticas aceitas na publicidade para se aproximar do público-alvo.
- Textos da esfera literária — Análise linguística: Compreenda o uso literário de expressões coloquiais e variedades regionais na fala das personagens, reconhecendo o distanciamento intencional do texto em relação à norma de referência.
- A ênfase é na adequação sociocomunicativa da linguagem.
- Textos da esfera da vida cotidiana — Análise linguística: Analise usos da oralidade e inovações linguísticas expressivas (gírias, abreviações digitais, entre outros recursos lexicais) próprios da escrita espontânea interpessoal.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Regra precisa, com exemplos concretos; nunca invente regra ou dado.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo portugues.variacao-linguistica-norma-linguistica.banco-N.json, onde N é o lote)
```json
{
  "materia": "portugues.variacao-linguistica-norma-linguistica",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 135 — raciocinio-logico.analise-situacoes
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Raciocínio Lógico e Matemático: Compreensão e análise lógica de situações** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Sequências numéricas, alfabéticas e figurais descritas verbalmente; analogias e classificação; conceitos e discriminação de elementos; calendário, relógio, intervalos, direções, rotações, vistas e planificações; problemas cotidianos com dados suficientes e conclusões lógicas.
Fica de fora (outras matérias tratam): Demonstrações formais avançadas, geometria analítica, cálculo, probabilidade avançada e problemas sem informação suficiente para uma conclusão única.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Problemas de lógica e raciocínio.
- Compreensão e análise da lógica de uma situação, utilizando as funções intelectuais: raciocínio verbal, raciocínio matemático, raciocínio sequencial, orientação espacial e temporal, formação de conceitos, discriminação de elementos.
- Compreensão e elaboração da lógica das situações por meio de: raciocínio verbal, raciocínio matemático, raciocínio sequencial, orientação espacial e temporal, formação de conceitos, discriminação de elementos.
- Raciocínios verbal, matemático, sequencial, espacial e temporal.
- Compreensão e análise da lógica de uma situação, utilizando as funções intelectuais: raciocínio verbal, raciocínio matemático, raciocínio sequencial, reconhecimento de padrões, orientação espacial e temporal, formação de conceitos, discriminação de elementos.
- Raciocínio sequencial.
- Orientação espacial e temporal.
- Raciocínio verbal, matemático, sequencial, espacial e temporal.
- Compreensão e análise da lógica de uma situação, utilizando as funções intelectuais: raciocínio verbal, raciocínio matemático, raciocínio sequencial, reconhecimento de padrões, orientação espacial e temporal, formação de conceitos, discriminação de elementos; compreensão de dados apresentados em gráficos e tabelas.
- Compreensão e elaboração da lógica das situações por meio de: raciocínio verbal; raciocínio matemático; raciocínio sequencial; orientação espacial e temporal; formação de conceitos; discriminação de elementos.
- Analogias, inferências, deduções e conclusões.
- Raciocínio verbal, matemático, sequencial, espacial e temporal e formação de conceitos.
- Raciocínio verbal, matemático, sequencial, orientação espacial e temporal, formação de conceitos e discriminação de elementos.
- Compreensão e elaboração da lógica das situações por meio de: raciocínio verbal.
- Raciocínio matemático.
- Compreensão e análise lógica de situações-problema.
- Compreensão e análise da lógica de uma situação.
- Raciocínio verbal.
- Formação de conceitos.
- Discriminação de elementos.
- Operações lógicas e resolução de problemas.
- Raciocínio lógico analítico.
- Reconhecimento de padrões, orientação espacial e temporal, conceitos e discriminação de elementos.
- Raciocínio lógico e resolução de situações-problema.
- Raciocínio verbal, matemático e sequencial; orientação espacial e temporal; formação de conceitos e discriminação de elementos.
- Análise lógica de situações: raciocínio verbal, matemático, sequencial, espacial e temporal; formação de conceitos e discriminação de elementos.
- Relações lógicas e dedução de informações e raciocínios verbal, matemático, sequencial, espacial e temporal.
- Raciocínio verbal, matemático e sequencial e orientação espacial e temporal.
- Formação de conceitos e discriminação de elementos.
- Competências Gerais: Compreensão, análise e resolução de situações-problema concretas, abstratas ou hipotéticas, deduzindo novas informações a partir das informações e relações fornecidas.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, cálculo com todos os dados no enunciado, interpretação de tabela ou gráfico, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Regra precisa, com exemplos concretos; nunca invente regra ou dado.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo raciocinio-logico.analise-situacoes.banco-N.json, onde N é o lote)
```json
{
  "materia": "raciocinio-logico.analise-situacoes",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 136 — direito-administrativo.bens-publicos
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Administrativo: Bens públicos** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Classificação em bens de uso comum do povo, de uso especial e dominicais; afetação e desafetação; inalienabilidade relativa, impenhorabilidade e imprescritibilidade; autorização, permissão e concessão de uso.
Fica de fora (outras matérias tratam): Licitações e contratos em geral, desapropriação e intervenção na propriedade, salvo distinções indispensáveis à alienação e ao uso dos bens.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Bens Públicos.
- Bens públicos: regime jurídico, classificação, administração, aquisição e alienação, formas de utilização por terceiros.
- Bens públicos: classificação e caracteres jurídicos.
- Bens públicos: Regime jurídico.
- Domínio público.
- Vias públicas, cemitérios públicos e portos.
- Bens públicos: natureza jurídica do domínio público.
- Terrenos de marinha e seus acrescidos.
- Bens públicos: Aquisição e alienação dos bens públicos.
- Bens públicos: Formas de utilização dos bens públicos pelos particulares.
- Domínio público: conceito e classificação dos bens públicos.
- Administração, utilização e alienação dos bens públicos.
- Imprescritibilidade, impenhorabilidade e não oneração dos bens públicos.
- Natureza jurídica do domínio público.
- Utilização dos bens públicos: autorização, permissão e concessão de uso, ocupação, aforamento, concessão de domínio pleno.
- Aquisição e alienação dos bens públicos.
- Formas de utilização dos bens públicos pelos particulares.
- Formas de utilização dos bens públicos por terceiros.
- Regime jurídico dos bens públicos.
- Patrimônio público: bens de uso comum, bens de uso especial, bens dominicais.
- Bens públicos: utilização dos bens públicos, autorização, permissão e concessão de uso, ocupação, aforamento, concessão de domínio pleno.
- Bens públicos e distinção dos bens particulares.
- Bens públicos: espécies, prerrogativas, vedações.
- Gestão dos bens públicos.
- Bens públicos, espécies, classificações.
- Bens público em espécie.
- Regimes jurídicos.
- Aquisição e alienação.
- Uso de bem público por particular.
- Bens públicos: classificação, regime jurídico, bens da União e legislação patrimonial.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, literalidade da lei com o detalhe que a prova troca, jurisprudência e súmulas, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Cite artigo, lei, súmula e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Se não tiver certeza de um número, explique sem ele — nunca invente.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito-administrativo.bens-publicos.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito-administrativo.bens-publicos",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 137 — economia.economia-setor-publico
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Economia: Economia do Setor Público** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Funções alocativa, distributiva e estabilizadora do governo; bens públicos e recursos comuns; externalidades e instrumentos corretivos; déficit e dívida pública; princípios e efeitos econômicos da tributação.
Fica de fora (outras matérias tratam): Regras jurídicas específicas, percentuais legais de repartição ou limites fiscais, alíquotas vigentes e dados atuais de dívida ou orçamento.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- O papel do Estado e a atuação do governo nas finanças públicas.
- Objetivos, metas, abrangência e definição de Finanças Públicas.
- Externalidades.
- Visão clássica das funções do Estado e evolução das funções do Governo.
- Falhas de mercado, bens públicos e externalidades; papel do Governo.
- Objetivos da política fiscal e políticas alocativas, distributivas e de estabilização.
- Financiamento dos gastos públicos, tributação, equidade e tipos de tributos.
- Déficit público e financiamento do déficit.
- Resultado Fiscal do Governo (NFSP): resultado primário e resultado nominal.
- Noções de Economia do Setor Público: equilíbrio competitivo e eficiência econômica.
- Noções sobre teoremas de bem-estar.
- Formas e dimensões da intervenção da administração na economia.
- Conceitos de déficit e dívida pública.
- As necessidades públicas e as formas de atuação dos governos.
- Política fiscal, tributos e gastos do governo.
- Déficit público.
- O papel do Estado e a atuação do governo nas finanças públicas: Formas e dimensões da intervenção da administração na economia.
- O conceito de Ótimo de Pareto.
- Resultado Fiscal do Governo (Necessidade de Financiamento do Setor Público – NFSP): Resultado Primário e Resultado Nominal.
- Noções sobre economia do setor público.
- Efeitos da atuação do Estado na economia.
- Políticas alocativas, distributivas e de estabilização.
- Política tributária: como os impostos influem nas decisões de consumo, poupança e gasto.
- A função estabilizadora do sistema tributário: a política fiscal e estabilizadores automáticos.
- Falhas de Mercado: Externalidades e ineficiência de mercado.
- Externalidades positivas e negativas.
- Soluções privadas para o problema das externalidades.
- Teorema de Coase.
- Custos de Transação e os limites das soluções privadas ao problema das externalidades.
- Políticas Públicas para as externalidades: Regulamentação.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, cálculo com todos os dados no enunciado, interpretação de tabela ou gráfico, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Regra precisa, com exemplos concretos; nunca invente regra ou dado.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo economia.economia-setor-publico.banco-N.json, onde N é o lote)
```json
{
  "materia": "economia.economia-setor-publico",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 138 — orcamento-publico.conceitos
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Orçamento Público: Conceitos** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Conceito e funções do orçamento público; princípios orçamentários; orçamento-programa e seus objetivos; estrutura constitucional dos orçamentos; elaboração, discussão, emendas, votação e aprovação da proposta orçamentária.
Fica de fora (outras matérias tratam): Estudo detalhado do conteúdo, vigência, prazos e metas próprios do PPA, da LDO e da LOA; execução orçamentária, créditos adicionais e classificação detalhada de receitas e despesas.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Princípios orçamentários.
- Conceitos e papel do orçamento público; evolução do orçamento no Brasil.
- O orçamento público no Brasil.
- Métodos, técnicas e instrumentos do orçamento público.
- Orçamento na Constituição Federal.
- Ciclo orçamentário: elaboração da proposta, discussão, votação e aprovação da lei de orçamento.
- Emendas parlamentares ao Orçamento.
- Orçamento público no Brasil: Estrutura programática.
- Orçamento-programa.
- Planejamento no orçamento-programa.
- Estrutura programática.
- Orçamento público: elaboração, acompanhamento e fiscalização.
- Normas legais aplicáveis ao orçamento público.
- Prática de elaboração de orçamento público.
- Direito Financeiro Orçamento na Constituição de 1988.
- Processo de aprovação da proposta orçamentária.
- Orçamento-Programa: conceitos e objetivos.
- Proposta orçamentária: elaboração, discussão, votação e aprovação.
- Conceito.
- Orçamento na Constituição Federal e Lei de Diretrizes Orçamentárias.
- Orçamento na Constituição Federal, LDO e LOA.
- Bases constitucionais das finanças públicas.
- Orçamentos Públicos.
- Estrutura, princípios e normas constitucionais orçamentárias.
- Elaboração da Lei Orçamentária.
- Natureza jurídica do orçamento.
- Planejamento Governamental, Orçamento Público e Controle.
- Métodos, técnicas e instrumentos do orçamento público; normas legais aplicáveis.
- Sistemas e processos orçamentários.
- A prática brasileira do orçamento-programa.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, literalidade da lei com o detalhe que a prova troca, jurisprudência e súmulas, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Cite artigo, lei, súmula e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Se não tiver certeza de um número, explique sem ele — nunca invente.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo orcamento-publico.conceitos.banco-N.json, onde N é o lote)
```json
{
  "materia": "orcamento-publico.conceitos",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 139 — informatica.engenharia-software-ageis
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Informática: Engenharia de software e métodos ágeis** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Conceito de engenharia de software; atividades genéricas do processo; modelos de processo (cascata, incremental, prototipação, espiral, Processo Unificado/RUP); engenharia de requisitos (requisitos funcionais e não funcionais, elicitação, especificação, validação e gerenciamento, histórias de usuário); UML (diagramas estruturais e comportamentais, casos de uso com include e extend, classes, sequência, atividades e estados); testes de software (níveis, caixa-preta e caixa-branca, regressão, verificação e validação, TDD); Manifesto Ágil; Scrum segundo o Guia de 2020; Kanban; XP; noções de DevOps, integração e entrega contínuas.
Fica de fora (outras matérias tratam): Gerenciamento de projetos pelo PMBOK, ITIL e COBIT, métricas como pontos de função em detalhe, CMMI e MPS.BR em detalhe, SAFe e outros frameworks de escala, ferramentas específicas de DevOps (Docker, Kubernetes, Jenkins) e programação em linguagens específicas.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Engenharia de requisitos.
- Metodologias Ágeis.
- Engenharia de requisitos: Produto mínimo viável (MVP).
- Engenharia de requisitos: Gestão de backlog.
- Qualidade: Teste unitário.
- Qualidade: Teste de integração.
- Análise e projeto orientados a objetos.
- Técnicas de priorização, de estimativas (Análise de Pontos de Função, Story Points).
- Análise e projeto.
- Engenharia de software: Unified Modeling Language (UML).
- Metodologias ágeis para o desenvolvimento de software: Scrum, XP, Lean.
- Engenharia de software: Desenvolvimento orientado a testes (TDD).
- Engenharia de software: Testes automatizados.
- Processos de desenvolvimento de software.
- Técnicas de validação de requisitos.
- Técnicas de Elicitação de Requisitos.
- Metodologias de desenvolvimento de software.
- UML: visão geral, modelos e diagramas.
- Kanban.
- Qualidade de software.
- Análise de requisitos, especificação, ambientes de testes, homologação, produção e suporte.
- Engenharia de Software: ciclo de vida do software.
- Noções sobre desenvolvimento e manutenção de sistemas e aplicações.
- Noções sobre metodologias de análise, projeto e desenvolvimento de sistemas.
- Engenharia de requisitos, gestão de backlog e produto mínimo viável.
- Gerenciamento de produtos de software por métodos Scrum, Kanban, XP e Lean.
- Desenvolvimento orientado a testes (TDD).
- Especificação de requisitos.
- Scrum.
- Metodologias ágeis, lean manufacturing e Scrum.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Regra precisa, com exemplos concretos; nunca invente regra ou dado.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo informatica.engenharia-software-ageis.banco-N.json, onde N é o lote)
```json
{
  "materia": "informatica.engenharia-software-ageis",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 140 — matematica.juros-simples-compostos
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Matemática e Raciocínio Lógico: Juros simples e compostos** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Capital, juros, montante, taxa e prazo em regimes simples e compostos; conversão de taxa e período; cálculos diretos, determinação de variável e comparação entre regimes.
Fica de fora (outras matérias tratam): Descontos simples ou compostos, equivalência avançada de taxas, séries uniformes, amortização, inflação e aportes periódicos.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Juros simples.
- Juros compostos.
- Juros simples e compostos.
- Capitalização.
- Juros simples e compostos: capitalização e descontos.
- Cálculo financeiro.
- Juros simples: montante e juros.
- Juros compostos: montante e juros.
- Capitalização contínua.
- Conceitos gerais: valor do dinheiro no tempo, valor presente, valor futuro, juro, taxa de juro, prazo da operação.
- Juros simples: cálculo do montante, dos juros, da taxa, do principal e do prazo.
- Juros compostos: cálculo do montante, dos juros, da taxa, do principal e do prazo.
- Juros simples e compostos: cálculos e aplicações.
- Matemática financeira aplicada: Juros simples.
- Matemática financeira aplicada: Juros compostos.
- Juros compostos. Montante e juros. Taxa real e taxa efetiva. Taxas equivalentes. Capitais equivalentes.
- Montante e juros.
- Conceito de juros e regimes de capitalizações.
- Capitalização simples: cálculo de juros e montantes.
- Capitalização composta: cálculo de juros e montantes.
- Matemática Financeira / Estatística Matemática Financeira: Regimes de capitalização em juros simples e compostos.
- Montante, juros e número de períodos.
- Juros simples e compostos: capitalização e desconto.
- Juros simples e juros compostos.
- Valor do dinheiro no tempo, capital, juros, taxas, capitalização e carência.
- Juros simples e compostos: montante, juros, taxas, principal e prazo.
- Juros simples e compostos, capitalização, descontos e taxas de juros.
- Juros simples e compostos, capitalização, desconto e taxas nominais, efetivas, equivalentes, reais e aparentes.
- Matemática financeira: juros simples e compostos e precificação e operações com títulos públicos federais.
- Juros simples: capitalização.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, cálculo com todos os dados no enunciado, interpretação de tabela ou gráfico, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Regra precisa, com exemplos concretos; nunca invente regra ou dado.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo matematica.juros-simples-compostos.banco-N.json, onde N é o lote)
```json
{
  "materia": "matematica.juros-simples-compostos",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 141 — raciocinio-logico.relacoes-arbitrarias
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Raciocínio Lógico e Matemático: Estrutura lógica de relações e dedução** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Modelagem de relações por tabelas, associação entre pessoas, funções, locais e objetos, ordenação linear e circular, posições, verdades e mentiras, proposições, conectivos, tabelas-verdade, negações, equivalências, contrapositiva, quantificadores, diagramas e validade de argumentos.
Fica de fora (outras matérias tratam): Cálculo de probabilidade, análise combinatória avançada e lógica matemática formal além do necessário para interpretar premissas e deduzir conclusões.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Estrutura lógica de relações arbitrárias entre pessoas, lugares, objetos ou eventos fictícios.
- Estrutura lógica de relações arbitrárias entre pessoas, lugares, objetos ou eventos fictícios; dedução de novas informações das relações fornecidas e avaliação das condições usadas para estabelecer a estrutura daquelas relações.
- Deduzir novas informações das relações fornecidas e avaliar as condições usadas para estabelecer a estrutura daquelas relações.
- Dedução de novas informações das relações fornecidas e avaliação das condições usadas para estabelecer a estrutura daquelas relações.
- Dedução de novas informações das relações fornecidas e avaliação das condições usadas para estabelecer a estrutura daquelas relações Compreensão de dados apresentados em gráficos e tabelas.
- Relações arbitrárias e dedução de novas informações.
- Estrutura lógica de relações arbitrária entre pessoas, lugares, objetos ou eventos fictícios.
- Dedução de novas informações daquelas relações.
- Estrutura lógica de relações arbitrárias entre pessoas, lugares, objetos ou eventos fictícios; deduzir novas informações das relações fornecidas e avaliar as condições usadas para estabelecer a estrutura daquelas relações.
- Raciocínio lógico e estruturas lógicas.
- Relações arbitrárias, dedução, gráficos e tabelas.
- Relações arbitrárias entre pessoas, lugares, objetos ou eventos fictícios e dedução de novas informações.
- Medidas e relações lógicas com dedução de informações.
- Estruturas lógicas de relações arbitrárias entre pessoas, lugares, objetos ou eventos fictícios.
- Raciocínio Lógico: Estrutura lógica de relações arbitrárias entre pessoas, lugares, objetos ou eventos fictícios.
- Estruturas lógicas e dedução.
- Dedução de novas informações a partir das relações fornecidas.
- Avaliação das condições utilizadas para estabelecer a estrutura lógica das relações apresentadas.
- Estrutura lógica de relações arbitrárias entre pessoas, lugares, objetos ou eventos fictícios;
- Proposições, conectivos, equivalências, quantificadores e predicados.
- Estrutura lógica de relações arbitrárias e dedução de novas informações.
- Relações arbitrárias, dedução de informações e avaliação das condições lógicas.
- Relações arbitrárias e dedução de informações e avaliação de condições lógicas.
- Relações arbitrárias, dedução de informações e condições lógicas.
- Relações arbitrárias, dedução de informações e avaliação de condições lógicas.
- Relações arbitrárias, dedução de informações e avaliação de condições.
- Relações lógicas e dedução de novas informações.
- Relações lógicas arbitrárias e dedução de novas informações.
- Estruturas de relações, dedução, raciocínio verbal, matemático e sequencial.
- Relações lógicas arbitrárias, dedução de informações e avaliação de condições.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, cálculo com todos os dados no enunciado, interpretação de tabela ou gráfico, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Regra precisa, com exemplos concretos; nunca invente regra ou dado.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo raciocinio-logico.relacoes-arbitrarias.banco-N.json, onde N é o lote)
```json
{
  "materia": "raciocinio-logico.relacoes-arbitrarias",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 142 — processo-civil.remedios-constitucionais-mandado-seguranca-habeas
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Processual Civil: Remédios constitucionais, mandado de segurança, habeas data, mandado de injunção e ação popular** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Habeas corpus, mandado de segurança individual e coletivo, habeas data, mandado de injunção, ação popular, ação civil pública, recursos cíveis em geral e meios extrajudiciais de solução de conflitos na Administração Pública.
Fica de fora (outras matérias tratam): Procedimentos penais específicos, recursos de legislação especial não indicados e estudo exaustivo de arbitragem privada.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Ação civil pública.
- Habeas corpus, mandado de segurança, mandado de injunção e habeas data.
- Mandado de segurança individual e coletivo.
- Mandado de segurança.
- Procedimentos especiais: mandado de segurança, ação popular, ação civil pública, ação de improbidade administrativa.
- Habeas Corpus.
- Mandado de Segurança Coletivo.
- Mandado de segurança, mandado de injunção, ação popular, habeas data e habeas corpus.
- Remédios constitucionais: habeas data, habeas corpus, mandado de segurança, ação popular e mandado de injunção.
- Ações diversas: Mandado de segurança.
- Ações diversas: Ação civil pública.
- Direitos e garantias fundamentais: habeas corpus, mandado de segurança, mandado de injunção e habeas data.
- Processo coletivo e tutela de direitos difusos, coletivos e individuais homogêneos.
- Ações coletivas.
- Mandado de segurança, ação popular, ação civil pública, improbidade, mandado de injunção e habeas data.
- Remédios constitucionais, mandado de segurança, habeas data, mandado de injunção, Ação popular.
- Procedimentos extrajudiciais de solução de conflitos na Administração Pública.
- Ação civil pública (Lei nº 7.347/1985 e alterações).
- Ação civil pública (Lei nº 7.347/1985 e suas alterações).
- Processos nos tribunais, recursos, mandado de segurança, ação popular, ação civil pública e improbidade.
- Remédios constitucionais: habeas-corpus, mandado de segurança.
- Ações específicas: Ação civil pública.
- – Legislação Extravagante: Lei nº 7.347/85 (Ação civil pública).
- Lei nº 12.016/2009 (Mandado de Segurança).
- Ação civil pública, ação popular e ação de improbidade administrativa.
- Habeas corpus, habeas data e mandado de injunção.
- Mandado de segurança, mandado de injunção, habeas data e mandado de segurança coletivo.
- Processo civil no controle de constitucionalidade, ações constitucionais e declaração incidental de inconstitucionalidade.
- Tutela judicial ambiental, ação popular, ação civil pública, mandados constitucionais e tutela de urgência.
- O processo civil e o controle judicial dos atos administrativos: mandado de segurança; Ação popular; Ação civil pública; Ação de improbidade administrativa.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, literalidade da lei com o detalhe que a prova troca, jurisprudência e súmulas, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Cite artigo, lei, súmula e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Se não tiver certeza de um número, explique sem ele — nunca invente.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo processo-civil.remedios-constitucionais-mandado-seguranca-habeas.banco-N.json, onde N é o lote)
```json
{
  "materia": "processo-civil.remedios-constitucionais-mandado-seguranca-habeas",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 143 — matematica.porcentagem
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Matemática e Raciocínio Lógico: Porcentagem e variação percentual** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Porcentagem, frações e decimais; variação percentual e pontos percentuais; fatores sucessivos; lucro e prejuízo com bases explícitas.
Fica de fora (outras matérias tratam): Juros, capitalização e demais matemática financeira com remuneração do capital pelo tempo.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Porcentagem.
- Porcentagens.
- Porcentagem e problemas envolvendo regra de três simples, cálculos de porcentagem, acréscimos e descontos.
- Razões e proporções: Porcentagens.
- Porcentagem e problemas.
- Porcentagem e juros.
- Cálculos com porcentagens.
- Porcentagem; problemas.
- Números, operações, porcentagem, juros e proporcionalidade.
- Números reais e operações, porcentagem e juros.
- Conjuntos, números, porcentagem, juros e proporcionalidade.
- Porcentagem e variações percentuais.
- Matemática financeira: Porcentagem.
- Matemática financeira: Descontos e acréscimos.
- Porcentagem e proporcionalidade.
- Percentagem, variação percentual e operações sobre mercadorias.
- Divisão proporcional: Porcentagens.
- Matemática financeira: percentagens.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, cálculo com todos os dados no enunciado, interpretação de tabela ou gráfico, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Regra precisa, com exemplos concretos; nunca invente regra ou dado.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo matematica.porcentagem.banco-N.json, onde N é o lote)
```json
{
  "materia": "matematica.porcentagem",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 144 — direito-civil.obrigacoes
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Civil: Direito das obrigações** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Modalidades de dar, fazer, não fazer, alternativas e facultativas; divisibilidade e solidariedade; cessão de crédito, assunção de dívida; pagamento e modos especiais de extinção; inadimplemento, mora, perdas e danos, juros legais e cláusula penal.
Fica de fora (outras matérias tratam): Contratos em geral, responsabilidade civil extracontratual e prescrição como matérias autônomas.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Obrigações.
- Transmissão das obrigações.
- Obrigações: Características.
- Adimplemento pelo pagamento.
- Inadimplemento das obrigações – disposições gerais e mora.
- Adimplemento e extinção das obrigações.
- Inadimplemento das obrigações.
- Preferências e privilégios creditórios.
- Obrigações solidárias.
- Direito das obrigações.
- Obrigações de execução instantânea, diferida e continuada.
- Obrigações puras e simples, condicionais, a termo e modais.
- Obrigações de dar.
- Atos unilaterais.
- Obrigações de fazer e de não fazer.
- Obrigações divisíveis e indivisíveis.
- Obrigações líquidas e ilíquidas.
- Obrigações principais e acessórias.
- Do Direito das Obrigações.
- Obrigações civis e naturais, obrigações de meio, de resultado e de garantia.
- Características.
- Cláusula penal.
- Obrigações: transmissão das obrigações.
- Obrigações: adimplemento e extinção das obrigações.
- Obrigações: inadimplemento das obrigações.
- Obrigações alternativas.
- Enriquecimento sem causa.
- Mora.
- Do inadimplemento das obrigações.
- Obrigações: modalidades, transmissão, adimplemento, extinção, inadimplemento e atos unilaterais.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, literalidade da lei com o detalhe que a prova troca, jurisprudência e súmulas, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Cite artigo, lei, súmula e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Se não tiver certeza de um número, explique sem ele — nunca invente.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito-civil.obrigacoes.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito-civil.obrigacoes",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 145 — direito-constitucional.competencias-intervencao
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Constitucional: Repartição de competências e intervenção** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Competências materiais exclusivas da União (art. 21); competências legislativas privativas da União e delegação a Estados (art. 22); competências administrativas comuns (art. 23); competências legislativas concorrentes e regras de suplementação (art. 24); competências remanescentes dos Estados (art. 25); competências municipais de interesse local e suplementação (art. 30); competências do Distrito Federal (art. 32, § 1º); intervenção federal e estadual, hipóteses, iniciativa, decreto e controle (arts. 34 a 36).
Fica de fora (outras matérias tratam): Organização dos Poderes, processo legislativo geral, repartição tributária e competências específicas de órgãos ou políticas fora dos arts. 21 a 30 e das regras de intervenção dos arts. 34 a 36.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Organização do Estado: Intervenção federal.
- Organização do Estado: Intervenção dos estados nos municípios.
- Das competências da União, dos Estados e dos Municípios.
- Intervenção federal.
- O Processo Legislativo na Constituição da República de 1988: competências constitucionais exclusivas, concorrentes e privativas no ato de legislar.
- Ação Direta de Inconstitucionalidade Interventiva.
- Intervenção dos estados nos municípios.
- Repartição de competências.
- Intervenção do Estado nos Municípios.
- Intervenção.
- Divisão de competências entre os entes federados.
- Intervenção federal nos estados.
- Intervenção nos Municípios.
- Intervenção federal e estadual.
- Organização do Estado, repartição de competências, federalismo e intervenção.
- Intervenção federal e intervenção dos Estados nos Municípios.
- Organização político-administrativa: competências da União, estados e municípios.
- Organização político administrativa: federação brasileira, competências da União, estados e municípios, intervenção federal.
- Da intervenção.
- Repartição de competências na Federação e suas técnicas.
- A repartição de competência na Constituição de 1988.
- Intervenção federal nos municípios.
- Estado federal, repartição de competências e Federação brasileira.
- Organização político-administrativa do Estado: competências da União, Estados, Distrito Federal e Municípios.
- Sistemas de repartição de competência.
- Discriminação de competência na Constituição de 1988.
- União: competência.
- Atribuições, competências e relações entre esferas governamentais no regime federativo.
- Competências e relações entre esferas de governo no regime federativo.
- Atribuições, competências e relações entre esferas de governo no regime federativo na CF/88.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, literalidade da lei com o detalhe que a prova troca, jurisprudência e súmulas, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Cite artigo, lei, súmula e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Se não tiver certeza de um número, explique sem ele — nunca invente.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito-constitucional.competencias-intervencao.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito-constitucional.competencias-intervencao",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 146 — legislacao.igualdade-racial-crimes-preconceito
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Legislação: Igualdade racial e crimes de preconceito** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Estatuto da Igualdade Racial (Lei nº 12.288/2010) e Lei nº 7.716/1989: igualdade de oportunidades, direitos étnicos, discriminação e preconceito por raça, cor, etnia, religião ou procedência nacional, crimes de preconceito, injúria racial e interpretação constitucional sobre homotransfobia.
Fica de fora (outras matérias tratam): Debates sociológicos sem conexão com as leis indicadas, discriminações por motivos fora do recorte, legislação local e procedimentos penais não relacionados diretamente aos dispositivos estudados.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Estatuto da Igualdade Racial.
- Lei nº 12.288/2010 (Estatuto da Igualdade Racial).
- Lei nº 7.716/1989 (crimes resultantes de preconceitos de raça ou de cor).
- Estatuto de igualdade racial (Lei n.º 12.288/2010).
- Estatuto de igualdade racial (Lei nº 12.288/2010 e suas alterações).
- Crimes resultantes de preconceito de raça ou de cor (Lei nº 7.716/1989).
- Lei Federal n° 12.288/2010 - Estatuto da Igualdade Racial.
- Lei Federal nº 7.716/1989 - Define os crimes resultantes de preconceito de raça ou de cor.
- Lei Federal nº 7.437/1985 - Lei Caó.
- Lei Federal nº 7.716/1989 - Crimes resultantes de preconceito de raça, cor, etnia, religião ou procedência nacional.
- Lei nº 7.716/1989 e suas alterações (Crimes resultantes de preconceitos de raça ou de cor).
- Estatuto da Igualdade Racial (Lei federal nº 12.288, de 20 de julho de 2010).
- Lei federal nº 7.716, de 5 de janeiro de 1989 (define os crimes resultantes de preconceito de raça ou de cor) e Lei federal nº 9.459, de 13 de maio de 1997 (tipificação dos crimes resultantes de preconceito de raça ou de cor).
- Lei Caó (Lei federal nº 7.437, de 20 de dezembro de 1985).
- Ações afirmativas.
- Legislação Especial: Crimes resultantes de preconceitos de raça ou de cor (Lei nº 7.716/1989 e Lei nº 14.532/2023).
- Diversidade étnico-racial.
- Estatuto Nacional da Igualdade Racial. Lei nº 10.973/2004.
- Lei nº 7.716/1989 e suas alterações (preconceito de raça ou cor).
- Lei 7.716/1989 (Crimes resultantes de preconceitos de raça ou cor).
- Lei 12.288/2010 (Estatuto da Igualdade Racial).
- Crimes resultantes de preconceito de raça ou de cor (Lei nº 7.716/1989 e suas alterações).
- Crimes de preconceito (Lei nº 7.716/1989 e suas alterações).
- Conceitos Fundamentais do Racismo, Sexismo, Intolerância Religiosa, LGBTQIA+fobia.
- Crime: Lei nº 7.716/1989 e alterações (crimes resultantes de preconceitos de raça ou de cor).
- Legislação penal especial: racismo.
- Crime de preconceito (Lei nº 7.716, de 5/1/1989).
- Estatuto Nacional da Igualdade Racial.
- Lei nº 7.716/1989: crimes resultantes de preconceito de raça ou de cor.
- Leis Federais n. 7.716/1989.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, literalidade da lei com o detalhe que a prova troca, jurisprudência e súmulas, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Cite artigo, lei, súmula e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Se não tiver certeza de um número, explique sem ele — nunca invente.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo legislacao.igualdade-racial-crimes-preconceito.banco-N.json, onde N é o lote)
```json
{
  "materia": "legislacao.igualdade-racial-crimes-preconceito",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 147 — matematica.graficos-tabelas-mapas-escalas
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Matemática: Leitura de gráficos, tabelas, mapas e escalas** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Leitura e comparação de tabelas e gráficos; frequências e percentuais apresentados; séries estatísticas e temporais; leitura de mapas e plantas; escala numérica e gráfica; conversão entre medidas e cálculo de distâncias reais ou representadas.
Fica de fora (outras matérias tratam): Estatística inferencial, cartografia técnica avançada, projeções cartográficas complexas e elaboração de pesquisas amostrais.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Compreensão de dados apresentados em gráficos e tabelas.
- Leitura e interpretação de gráficos (histogramas, setores, infográficos) e tabelas.
- Interpretação de gráficos e tabelas.
- Compreensão de dados em gráficos e tabelas.
- Gráficos e tabelas.
- Interpretação de gráficos e tabelas (dados estatísticos).
- Séries estatísticas.
- Interpretação de dados em gráficos e tabelas.
- Relação entre grandezas: tabelas e gráficos.
- Análise de gráficos e tabelas (interpretação de dados).
- Interpretação de gráficos.
- Representações em gráficos e tabelas (linhas, colunas, setores e histogramas).
- Gráficos, tabelas, problemas aritméticos, geométricos e matriciais.
- Leitura de dados apresentados em gráficos e tabelas.
- Construção e interpretação de gráficos e tabelas.
- Estatística e representações gráficas: leitura e interpretação de tabelas e gráficos (barras, setores, linhas).
- Relação entre grandezas, tabelas e gráficos.
- Interpretação de figuras, desenhos, mapas, gráficos, tabelas, séries e plantas; escalas.
- Análise e interpretação de desenhos, mapas, gráficos, tabelas, séries estatísticas, séries temporais e plantas; utilização de escalas.
- Figuras planas, desenhos, mapas e plantas; escalas; figuras espaciais, projeções, planificações e cortes.
- Gráficos (histogramas, setores, infográficos).
- Noções de estatística: Gráficos.
- Noções de estatística: Tabelas.
- Representação de dados em gráficos e tabelas.
- Quadros, tabelas e gráficos: tipos e interpretação.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, cálculo com todos os dados no enunciado, interpretação de tabela ou gráfico, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Regra precisa, com exemplos concretos; nunca invente regra ou dado.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo matematica.graficos-tabelas-mapas-escalas.banco-N.json, onde N é o lote)
```json
{
  "materia": "matematica.graficos-tabelas-mapas-escalas",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 148 — processo-civil.sujeitos-processo
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Processual Civil: Sujeitos do processo** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Partes e procuradores (capacidade, deveres, litigância de má-fé, ato atentatório, despesas, honorários, gratuidade, procuração, sucessão); litisconsórcio; intervenção de terceiros (assistência, denunciação da lide, chamamento ao processo, desconsideração da personalidade jurídica, amicus curiae); juiz (poderes, deveres, responsabilidade, impedimento e suspeição); auxiliares da justiça; Ministério Público, Defensoria Pública e advocacia pública no processo civil.
Fica de fora (outras matérias tratam): Jurisdição e competência, ação e suas condições, atos e prazos processuais em geral, petição inicial, procedimento comum e recursos, tratados em matérias próprias.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Sujeitos do processo.
- Intervenção de terceiros.
- Litisconsórcio e assistência.
- Sujeitos do processo: Capacidade processual e postulatória.
- Sujeitos do processo: Procuradores.
- Sujeitos do processo: Sucessão das partes e dos procuradores.
- Partes e Procuradores.
- Auxiliares da Justiça.
- Litisconsórcio.
- Intervenção de terceiros: oposição, nomeação à autoria, denunciação à lide e chamamento ao processo.
- Da intervenção de terceiros.
- Sujeitos do processo: Deveres das partes e procuradores.
- Poderes, deveres e responsabilidade do juiz.
- Sujeitos do processo: Litisconsórcio.
- Juiz.
- O juiz.
- Partes e procuradores: capacidade processual e postulatória.
- Amicus curiae.
- Substituição processual.
- Sujeitos do processo: Partes e procuradores.
- Deveres e substituição das partes e procuradores.
- Litisconsórcio e intervenção de terceiros.
- Magistratura.
- Do Ministério Público.
- Advocacia.
- Das partes e procuradores: da capacidade processual e postulatória.
- Do litisconsórcio e da assistência.
- Do Juiz.
- Impedimento e suspeição.
- Capacidade processual e postulatória.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, literalidade da lei com o detalhe que a prova troca, jurisprudência e súmulas, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Cite artigo, lei, súmula e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Se não tiver certeza de um número, explique sem ele — nunca invente.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo processo-civil.sujeitos-processo.banco-N.json, onde N é o lote)
```json
{
  "materia": "processo-civil.sujeitos-processo",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 149 — informatica.arquitetura-software
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Informática: Arquitetura de software: camadas, MVC, microsserviços e padrões de projeto** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Conceito de arquitetura de software, componentes, conectores e atributos de qualidade; estilos arquiteturais (cliente-servidor, ponto a ponto, duto e filtro, orientada a eventos, serverless em noções); arquitetura em camadas (apresentação, negócio e dados), camadas lógicas x físicas, camadas estritas x relaxadas, vantagens e desvantagens; MVC, fluxo entre Model, View e Controller, MVC na web e front controller, MVC x três camadas, MVP e MVVM; noções de arquitetura hexagonal e Clean Architecture; monólito, SOA e microsserviços (características, comunicação síncrona e assíncrona, banco por serviço, API gateway, service discovery, circuit breaker, saga, consistência eventual, strangler fig, lei de Conway); padrões de projeto GoF (propósito, escopo, os 23 padrões de criação, estruturais e comportamentais, com exemplos em Java) e noções de GRASP.
Fica de fora (outras matérias tratam): Fundamentos e pilares da orientação a objetos e princípios SOLID (em informatica.orientacao-objetos); UML, modelos de processo e métodos ágeis (em informatica.engenharia-software-ageis); HTTP, métodos, códigos de resposta e APIs em detalhe (em informatica.python-r-api); SOAP, WSDL e UDDI em detalhe; Docker, Kubernetes e ferramentas de nuvem; frameworks específicos (Spring, Java EE/Jakarta EE) e padrões Java EE; DDD em profundidade.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Arquitetura de software.
- Arquitetura de software: Interoperabilidade de sistemas.
- Arquitetura de software: Arquitetura orientada a serviços.
- Arquitetura orientada a serviços: Web services.
- Arquitetura de software: Arquitetura orientada a objetos.
- Arquitetura de software: Arquitetura de aplicações para ambiente web.
- Arquitetura de aplicações para ambiente web: Servidor de aplicações.
- Noções de Arquitetura SOA (Service Oriented Architecture).
- Padrões de projeto.
- Noções de Arquitetura Cliente-Servidor.
- Engenharia de software: Padrões de projeto e SOLID.
- SOA e Web Services: UDDI, WSDL e SOAP.
- Desenho de arquitetura de soluções.
- Camadas de Aplicação, processos, frontend, backend.
- Arquitetura cliente-servidor multicamadas.
- Design Patterns.
- Arquitetura MVC.
- SOA e web services: conceitos básicos e aplicações.
- Padrões de desenvolvimento e reuso.
- Arquitetura de software. Interoperabilidade de sistemas.
- Design de software: arquitetura hexagonal, microsserviços (orquestração de serviços e API gateway) e containers.
- Arquitetura.
- Arquiteturas de integração, SOA, Webservices e REST.
- Padrão MVC.
- Arquitetura de desenvolvimento da Plataforma Digital do Poder Judiciário (PDPJ-Br): a) Arquitetura distribuída de microsserviços: API RESTful;
- Arquitetura Limpa (Clean Architecture).
- Padrão MVC (Model-View-Controller) aplicado à web.
- Arquiteturas em camadas, baseada em serviços, microsserviços (orquestração de serviços e API gateway), orientação a eventos, cliente-servidor, serverless.
- Padrões: GoF.
- Padrões: GRASP.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Regra precisa, com exemplos concretos; nunca invente regra ou dado.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo informatica.arquitetura-software.banco-N.json, onde N é o lote)
```json
{
  "materia": "informatica.arquitetura-software",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 150 — lingua-inglesa.gramatica-para-leitura
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Língua Inglesa: Gramática inglesa para leitura** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Estrutura da frase, artigos, classes e formação de palavras; concordância essencial; tempos e aspectos verbais; modais e condicionais; voz passiva; pronomes e referentes; conectores; comparativos e superlativos; preposições; inferências de leitura e falsos cognatos frequentes.
Fica de fora (outras matérias tratam): Conversação, produção de textos extensos, fonética avançada e vocabulário especializado de área profissional.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Itens gramaticais relevantes para a compreensão dos conteúdos semânticos.
- Itens gramaticais relevantes para compreensão de conteúdos semânticos.
- Conhecimento e uso das formas contemporâneas da linguagem inglesa.
- Itens gramaticais relevantes para a compreensão de conteúdos semânticos.
- Aspectos gramaticais básicos para a compreensão de textos.
- Vocabulário fundamental e aspectos gramaticais básicos para a compreensão de textos.
- Tempos e modos verbais.
- Uso de preposições, conjunções, pronomes e modais.
- Voz passiva, discurso direto e indireto.
- Aspectos sintático-gramaticais: artigos definidos e indefinidos, tempos e modos verbais, preposições, conjunções, pronomes, modais, concordância nominal e verbal, formação e classe de palavras.
- Expressões idiomáticas, subordinação e coordenação, voz passiva, discurso direto e indireto.
- Itens gramaticais relevantes à compreensão semântica.
- Aspectos sintático-gramaticais relevantes à compreensão de texto: artigos definidos e indefinidos.
- Formação e classe de palavras.
- Relações de subordinação e coordenação.
- Itens gramaticais relevantes à compreensão dos conteúdos semânticos.
- Vocabulário e morfossintaxe da língua inglesa.
- Itens gramaticais relevantes para compreensão semântica e formas contemporâneas do inglês.
- Domínio do vocabulário e da estrutura da língua.
- Itens gramaticais relevantes para compreensão semântica.
- Itens gramaticais relevantes para a compreensão semântica.
- Itens gramaticais relevantes para a compreensão de conteúdos semânticos em língua inglesa.
- Aspectos sintático-gramaticais: artigos definidos e indefinidos, tempos e modos verbais, preposições, conjunções, pronomes e modais.
- Concordância nominal e verbal, formação e classe de palavras, expressões idiomáticas, subordinação e coordenação, voz passiva, discurso direto e indireto.
- Estruturas gramaticais: adjectives, adverbs, nouns, articles, conjunctions, modals, prepositions, pronouns, verb tenses, passive voice e wh-questions.
- Elementos de referência.
- Itens gramaticais relevantes para compreensão dos conteúdos semânticos.
- Vocabulário e estruturas gramaticais necessários à compreensão de textos.
- Itens gramaticais relevantes para a compreensão dos conteúdos semânticos (inglês).
- Aspectos gramaticais relevantes à compreensão de texto.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Regra precisa, com exemplos concretos; nunca invente regra ou dado.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo lingua-inglesa.gramatica-para-leitura.banco-N.json, onde N é o lote)
```json
{
  "materia": "lingua-inglesa.gramatica-para-leitura",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

