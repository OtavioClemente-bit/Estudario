Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Legislação: Igualdade racial e crimes de preconceito** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

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

## Cada lote de 50
- 25 de múltipla escolha A a E (5 alternativas), 13 de múltipla escolha A a D (4 alternativas), 12 de Certo/Errado.
- Dificuldade: 15 fáceis, 20 médias, 15 difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, literalidade da lei com o detalhe que a prova troca, jurisprudência e súmulas, comparação entre conceitos parecidos. Use casos DIFERENTES entre si; nada de repetir o mesmo caso com outras palavras.

## Regras de qualidade
1. Cite artigo, lei, súmula e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Se não tiver certeza de um número, explique sem ele — nunca invente.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Explicação (mínimo 120 caracteres): por que a certa está certa e por que CADA errada erra, com o conceito específico. Em Certo/Errado começa com "Certo." ou "Errado.". Escreva cada explicação de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo legislacao.igualdade-racial-crimes-preconceito.banco-N.json, onde N é o lote)
```json
{
  "materia": "legislacao.igualdade-racial-crimes-preconceito",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false }, { "key": "B", "text": "...", "correct": true } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (A–E com 5 opções ou A–D com 4 opções) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
