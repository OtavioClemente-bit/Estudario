Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Informática: BI, data warehouse, OLAP, ETL e mineração de dados** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Sistemas transacionais (OLTP) e analíticos (OLAP); características do data warehouse (orientado a assunto, integrado, não volátil e variante no tempo); data mart, ODS, data lake e lakehouse; abordagens de Inmon e de Kimball; modelagem dimensional: fatos, dimensões, granularidade, medidas aditivas, semiaditivas e não aditivas, chave substituta, hierarquias, esquemas estrela, floco de neve e constelação, dimensões que mudam lentamente; ETL e ELT, área de preparação e qualidade dos dados; operações OLAP (drill down, roll up, slice, dice, pivot, drill across e drill through) e arquiteturas ROLAP, MOLAP e HOLAP; KDD e CRISP-DM; tarefas de mineração (classificação, regressão, agrupamento, associação com suporte, confiança e lift, detecção de anomalias); algoritmos clássicos (árvore de decisão, k-NN, Naive Bayes, k-means, Apriori) e tipos de aprendizado de máquina.
Fica de fora (outras matérias tratam): Visão geral do ciclo de análise, big data, ética, viés e explicabilidade de IA e transformação digital (estão em informatica.analise-dados-ia); SQL e normalização de bancos relacionais; programação de modelos, redes neurais em detalhe e uso passo a passo de ferramentas comerciais de BI.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Modelagem dimensional.
- Técnicas de Integração e Ingestão de Dados (ETL/ELT, Transferência de Arquivos e Integração via Base de Dados).
- Técnicas de modelagem e otimização de bases de dados multidimensionais.
- Business Intelligence.
- Mineração de dados: conceituação e características.
- Técnicas e tarefas de mineração de dados.
- Noções de modelagem dimensional. Conceito e aplicações.
- Noções de mineração de dados. Conceituação e características.
- Técnicas e tarefas de mineração de dados. Classificação. Regras de associação.
- Business Intelligence (BI).
- ETL, tratamento, manipulação e visualização de dados.
- ETL, manipulação, tratamento e visualização de dados.
- Arquitetura e aplicações de data warehouse com ETL e OLAP.
- Arquitetura de business intelligence.
- Conceitos, fundamentos, características, técnicas e métodos de business intelligence (BI).
- Regras de associação.
- Noções de mineração de dados: conceituação e características.
- Conceito de DataWarehouse, DataMart, DataLake, DataMesh.
- Noções de Business Intelligence: Ferramentas e aplicabilidade.
- ETL/ELT (Extract, Transform, Load).
- Pipeline de Dados.
- Modelagem dimensional e aplicações.
- Arquitetura de Inteligência de Negócio.
- OLAP.
- Sistemas transacionais.
- Sistemas de suporte à decisão.
- Conceitos básicos, arquiteturas e aplicações de datawarehousing, ETL, Olap e data mining.
- Data warehouse e data mining.
- Mineração de Dados.
- Definições e conceitos de data warehouse e data mining.

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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo informatica.bi-mineracao-dados.banco-N.json, onde N é o lote)
```json
{
  "materia": "informatica.bi-mineracao-dados",
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
