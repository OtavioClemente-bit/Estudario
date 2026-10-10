Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Raciocínio Lógico e Matemático: Compreensão e análise lógica de situações** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo raciocinio-logico.analise-situacoes.banco-N.json, onde N é o lote)
```json
{
  "materia": "raciocinio-logico.analise-situacoes",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false }, { "key": "B", "text": "...", "correct": true } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (A–E com 5 opções ou A–D com 4 opções) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
