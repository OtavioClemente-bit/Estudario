Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Língua Portuguesa: Frase, oração e período** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Frase como enunciado comunicativo; frase nominal e verbal; modalidades; oração organizada em torno de verbo ou locução verbal; formas nominais, reduzidas e reconhecimento dos núcleos; período simples e composto.
Fica de fora (outras matérias tratam): Classificação aprofundada das subordinadas e coordenadas, funções sintáticas detalhadas e pontuação avançada do período composto.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Domínio da estrutura morfossintática do período.
- Sintaxe da oração e do período.
- Tipologia da frase portuguesa.
- Estrutura morfossintática do período e classes de palavras.
- Sintaxe do período simples e composto.
- Sintaxe e construção frasal.
- Sintaxe: frase, oração, período e termos da oração.
- Sintaxe: frase, oração e período.
- Organização das frases nas situações comunicativas.
- Estrutura da frase portuguesa: deslocamento, substituição, modificação e correção.
- Sintaxe: frase, oração e período; termos da oração.
- Tipologia, estrutura e organização sintática da frase portuguesa.
- Frase, oração e período.
- Tipologia e estrutura da frase, norma padrão, pontuação e organização sintática.
- Tipologia e estrutura da frase portuguesa, norma culta, pontuação e organização sintática.
- Estrutura da oração e do período: aspectos sintáticos e semânticos.
- A organização das frases nas situações comunicativas.
- Tipologia, estrutura e problemas da frase portuguesa; norma culta e sintaxe.
- Estrutura da frase, norma culta, pontuação e sintaxe.
- Estrutura da frase, norma culta, pontuação e organização sintática.
- Estrutura da frase portuguesa e operações de deslocamento, substituição, modificação e correção.
- Frase portuguesa: tipologia, estrutura, operações estruturais e problemas de construção.
- Tipologia e estrutura da frase portuguesa e problemas estruturais das frases.
- Frase portuguesa: tipologia, estrutura, sintaxe, norma culta e problemas estruturais.
- Tipologia, estrutura, sintaxe e problemas estruturais da frase portuguesa.
- Tipologia, estrutura e sintaxe da frase portuguesa.
- Tipologia da frase portuguesa e estrutura da frase.
- Organização sintática do período.
- Frase, oração e período: classificação e estrutura.
- Sintaxe: reconhecimento das orações num período.

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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo portugues.frase-oracao-periodo.banco-N.json, onde N é o lote)
```json
{
  "materia": "portugues.frase-oracao-periodo",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false }, { "key": "B", "text": "...", "correct": true } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (A–E com 5 opções ou A–D com 4 opções) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
