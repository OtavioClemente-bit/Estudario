Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Língua Portuguesa: Reescrita de frases e parágrafos** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Paráfrase contextual, substituição lexical, conectores, voz verbal, ordem dos termos e pontuação, redução e desenvolvimento de orações, discurso direto e indireto, nominalização, paralelismo e distinção entre preservação de sentido, alteração semântica e erro gramatical.
Fica de fora (outras matérias tratam): Interpretação global de textos extensos, ortografia e acentuação como conteúdo autônomo, regência e concordância fora do necessário para avaliar a reescrita, e produção de redação dissertativa.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Reescrita de frases e parágrafos do texto.
- Reescrita de frases e parágrafos do texto: Significação das palavras.
- Reescrita de frases e parágrafos do texto: Substituição de palavras ou de trechos de texto.
- Reescrita de frases e parágrafos do texto: Reorganização da estrutura de orações e de períodos do texto.
- Substituição de palavras ou de trechos de texto.
- Reorganização da estrutura de orações e de períodos do texto.
- Reescritura de frases e parágrafos do texto: substituição de palavras ou de trechos de texto.
- Estrutura da frase portuguesa: operações de deslocamento, substituição, modificação e correção.
- Reescritura de frases e parágrafos do texto.
- Redação (confronto e reconhecimento de frases corretas e incorretas; organização e reorganização de orações e períodos; equivalência e transformação de estruturas).
- Reescritura de frases e parágrafos do texto: retextualização de diferentes gêneros e níveis de formalidade.
- Tipos de discurso.
- Os tipos de discurso: direto, indireto e indireto livre.
- Equivalência e transformação de estruturas.
- Problemas estruturais das frases.
- Reescrita de frases, parágrafos e textos de diferentes gêneros e níveis de formalidade.
- Reescrita, significação, substituição e reorganização textual.
- Redação (confronto e reconhecimento de frases corretas e incorretas).
- Reescrita, significação, substituição e reorganização de frases e parágrafos.
- Reescritura de frases: substituição, deslocamento, paralelismo.
- Tipos de discurso: direto, indireto e indireto livre.
- Reescrita de frases, parágrafos e textos: significação, substituição e reorganização de estruturas.
- Reescrita de frases (substituição, deslocamento, paralelismo).
- Reescrita de orações, períodos, frases e parágrafos.
- Reescritura de frases.
- Discursos direto, indireto e indireto livre.
- Reescrita de frases: substituição, deslocamento, paralelismo.
- Reescrita, significação e reorganização textual.
- Reescrita, significação e substituição de palavras e reorganização textual.
- Reescrita de frases, parágrafos e textos de gêneros e níveis de formalidade variados.

## Cada lote de 50
- 25 de múltipla escolha A a E (5 alternativas), 13 de múltipla escolha A a D (4 alternativas), 12 de Certo/Errado.
- Dificuldade: 15 fáceis, 20 médias, 15 difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, comparação entre conceitos parecidos. Use casos DIFERENTES entre si; nada de repetir o mesmo caso com outras palavras.

## Regras de qualidade
1. Regra precisa, com exemplos concretos; nunca invente regra ou dado.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Explicação (mínimo 120 caracteres): por que a certa está certa e por que CADA errada erra, com o conceito específico. Em Certo/Errado começa com "Certo." ou "Errado.". Escreva cada explicação de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo portugues.reescrita.banco-N.json, onde N é o lote)
```json
{
  "materia": "portugues.reescrita",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false }, { "key": "B", "text": "...", "correct": true } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (A–E com 5 opções ou A–D com 4 opções) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
