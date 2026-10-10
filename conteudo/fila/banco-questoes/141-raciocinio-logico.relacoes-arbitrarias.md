Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Raciocínio Lógico e Matemático: Estrutura lógica de relações e dedução** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

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

## Cada lote de 50
- 25 de múltipla escolha A a E (5 alternativas), 13 de múltipla escolha A a D (4 alternativas), 12 de Certo/Errado.
- Dificuldade: 15 fáceis, 20 médias, 15 difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, cálculo com todos os dados no enunciado, interpretação de tabela ou gráfico, comparação entre conceitos parecidos. Use casos DIFERENTES entre si; nada de repetir o mesmo caso com outras palavras.

## Regras de qualidade
1. Regra precisa, com exemplos concretos; nunca invente regra ou dado.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Explicação (mínimo 120 caracteres): por que a certa está certa e por que CADA errada erra, com o conceito específico. Em Certo/Errado começa com "Certo." ou "Errado.". Escreva cada explicação de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo raciocinio-logico.relacoes-arbitrarias.banco-N.json, onde N é o lote)
```json
{
  "materia": "raciocinio-logico.relacoes-arbitrarias",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false }, { "key": "B", "text": "...", "correct": true } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (A–E com 5 opções ou A–D com 4 opções) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
