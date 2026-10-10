Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Penal: Teoria do crime** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

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

## Cada lote de 50
- 38 de múltipla escolha A a E (5 alternativas) e 12 de Certo/Errado. (O app transforma A–E em A–D tirando uma errada, por isso cada alternativa tem comentário próprio.)
- Dificuldade: 15 fáceis, 20 médias, 15 difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, literalidade da lei com o detalhe que a prova troca, jurisprudência e súmulas, comparação entre conceitos parecidos. Use casos DIFERENTES entre si; nada de repetir o mesmo caso com outras palavras.

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
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
