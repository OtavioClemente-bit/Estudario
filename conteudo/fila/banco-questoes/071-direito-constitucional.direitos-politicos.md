Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Constitucional: Direitos políticos e partidos (arts. 14 a 17)** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Soberania popular, participação direta, sufrágio, cidadania, alistamento e voto, elegibilidade, inelegibilidades, reeleição, suspensão e perda de direitos políticos, anterioridade eleitoral e regime constitucional dos partidos políticos.
Fica de fora (outras matérias tratam): Procedimento eleitoral infraconstitucional aprofundado, crimes eleitorais, propaganda eleitoral, financiamento em detalhes e organização administrativa do processo eleitoral.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Partidos políticos.
- Direitos políticos.
- Cidadania e direitos políticos.
- Direitos e garantias fundamentais: Direitos políticos.
- Cidadania.
- Dos direitos políticos.
- Lei nº 9.709/1998 (regulamenta a execução do disposto nos incisos I, II e III do art. 14 da CRFB/88 – plebiscito, referendo e iniciativa popular).
- Direitos sociais, nacionalidade, cidadania, direitos políticos e partidos políticos.
- Direitos políticos, partidos políticos.
- Direitos e garantias fundamentais: Dos Direitos Políticos.
- Direitos políticos: Direitos fundamentais e direitos políticos.
- Direitos políticos: Privação dos direitos políticos.
- Poder representativo: Sufrágio.
- Sufrágio: Natureza.
- Sufrágio: Extensão do sufrágio.
- Sufrágio: Valor do sufrágio.
- Sufrágio: Modo de sufrágio.
- Sufrágio: Formas de sufrágio.
- Direitos políticos e partidos políticos.
- Nacionalidade, direitos políticos e partidos políticos.
- Nacionalidade, cidadania, direitos políticos e partidos políticos.
- Plebiscito.
- Nacionalidade, cidadania e direitos políticos.
- Dos direitos e garantias fundamentais: dos direitos políticos.
- Alistamento.
- Suspensão e perda dos direitos políticos.
- Referendo.
- Iniciativa popular.
- Direitos e deveres fundamentais: Partidos políticos.
- Direitos e garantias fundamentais: partidos políticos.

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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito-constitucional.direitos-politicos.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito-constitucional.direitos-politicos",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false }, { "key": "B", "text": "...", "correct": true } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (A–E com 5 opções ou A–D com 4 opções) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
