Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Língua Portuguesa: Pontuação** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Emprego e efeitos de vírgula, ponto final, ponto e vírgula, dois-pontos, travessão, parênteses, aspas, reticências e sinais interrogativos/exclamativos quando necessários à análise do período, conforme sintaxe e sentido.
Fica de fora (outras matérias tratam): Ortografia, acentuação e análise sintática que não interferem diretamente na pontuação do período.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Emprego dos sinais de pontuação.
- Pontuação.
- Domínio da estrutura morfossintática do período: Emprego dos sinais de pontuação.
- Pontuação e sinais gráficos.
- Pontuação: regras e implicações de sentido.
- Emprego dos sinais de pontuação e sua função no texto.
- Pontuação, coesão e coerência textual.
- Norma culta, pontuação e sinais gráficos.
- Pontuação: uso correto dos sinais de pontuação.
- emprego dos sinais de pontuação;
- Pontuação, sinais gráficos, ordem direta e inversa e tipos de discurso.
- Norma culta, pontuação, sinais gráficos e sintaxe.
- Norma culta, pontuação, sinais gráficos e organização sintática.
- Sinais de pontuação.
- Sistema gráfico: pontuação.
- Pontuação: uso e função do ponto final, vírgula, ponto e vírgula, dois-pontos, aspas, parênteses, travessão e reticências.
- Pontuação e classes de palavras e seus empregos e sentidos.
- Pontuação e acentuação gráfica.
- Pontuação e efeitos de sentido.
- Pontuação e uso dos porquês.
- Pontuação, sinais gráficos e organização sintática das frases.
- Norma culta, pontuação, sinais gráficos e organização sintática de termos e orações em ordem direta e inversa.
- Textos da esfera pública/oficial — Análise linguística: Examine o emprego das normas ortográficas vigentes e da pontuação exigido pelo rigor e grau de formalidade da escrita pública nacional.
- Textos da esfera da vida cotidiana — Análise linguística: Analise a expressividade dos sinais de pontuação não convencionais e outros recursos empregados nos textos escritos instantâneos (várias exclamações, caixa alta, reticências etc.).

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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo portugues.pontuacao.banco-N.json, onde N é o lote)
```json
{
  "materia": "portugues.pontuacao",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false }, { "key": "B", "text": "...", "correct": true } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (A–E com 5 opções ou A–D com 4 opções) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
