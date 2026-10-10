Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Língua Portuguesa: Concordância verbal e nominal** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Concordância do verbo com sujeito simples, composto, posposto, oracional e especial; concordância nominal de determinantes, adjetivos e particípios; voz passiva sintética, indeterminação do sujeito, construções impessoais e locuções verbais, com atenção a variantes reconhecidas da norma-padrão.
Fica de fora (outras matérias tratam): Regência verbal e nominal como tema autônomo, colocação pronominal e questões desvinculadas da estrutura sintática do período.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Concordância verbal e nominal.
- Concordância verbal.
- Concordância nominal.
- Domínio da estrutura morfossintática do período: Concordância verbal e nominal.
- Concordância nominal e verbal.
- Concordância nominal e concordância verbal.
- Domínio da estrutura morfossintática do período: Concordância verbal.
- Domínio da estrutura morfossintática do período: Concordância nominal.
- Concordâncias nominal e verbal, pontuação, uso dos porquês e figuras de linguagem.
- Concordância nominal e verbal, regência nominal e verbal e colocação dos pronomes átonos.
- Sintaxe de concordância verbal e nominal.
- concordância verbal e nominal;
- Pontuação, concordância nominal e verbal e regência nominal e verbal.
- Sintaxe de concordância.
- Aspectos normativos: regras padrão de concordância, regência e colocação.
- Sintaxe de concordância verbal.
- Sintaxe de concordância nominal.
- Concordâncias nominal e verbal.
- Classes de palavras, coordenação, subordinação, pontuação, concordância, regência, crase e colocação pronominal.
- Textos da esfera jornalística/publicitária — Análise linguística: Analise a adequação do texto jornalístico à norma de referência do português brasileiro, com atenção aos casos mais frequentes de concordância, regência, crase e colocação pronominal.

## Cada lote de 50
- 38 de múltipla escolha A a E (5 alternativas) e 12 de Certo/Errado. (O app transforma A–E em A–D tirando uma errada, por isso cada alternativa tem comentário próprio.)
- Dificuldade: 15 fáceis, 20 médias, 15 difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, comparação entre conceitos parecidos. Use casos DIFERENTES entre si; nada de repetir o mesmo caso com outras palavras.

## Regras de qualidade
1. Regra precisa, com exemplos concretos; nunca invente regra ou dado.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo portugues.concordancia.banco-N.json, onde N é o lote)
```json
{
  "materia": "portugues.concordancia",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
