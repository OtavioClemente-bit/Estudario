Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Língua Inglesa: Gramática inglesa para leitura** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Estrutura da frase, artigos, classes e formação de palavras; concordância essencial; tempos e aspectos verbais; modais e condicionais; voz passiva; pronomes e referentes; conectores; comparativos e superlativos; preposições; inferências de leitura e falsos cognatos frequentes.
Fica de fora (outras matérias tratam): Conversação, produção de textos extensos, fonética avançada e vocabulário especializado de área profissional.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Itens gramaticais relevantes para a compreensão dos conteúdos semânticos.
- Itens gramaticais relevantes para compreensão de conteúdos semânticos.
- Conhecimento e uso das formas contemporâneas da linguagem inglesa.
- Itens gramaticais relevantes para a compreensão de conteúdos semânticos.
- Aspectos gramaticais básicos para a compreensão de textos.
- Vocabulário fundamental e aspectos gramaticais básicos para a compreensão de textos.
- Tempos e modos verbais.
- Uso de preposições, conjunções, pronomes e modais.
- Voz passiva, discurso direto e indireto.
- Aspectos sintático-gramaticais: artigos definidos e indefinidos, tempos e modos verbais, preposições, conjunções, pronomes, modais, concordância nominal e verbal, formação e classe de palavras.
- Expressões idiomáticas, subordinação e coordenação, voz passiva, discurso direto e indireto.
- Itens gramaticais relevantes à compreensão semântica.
- Aspectos sintático-gramaticais relevantes à compreensão de texto: artigos definidos e indefinidos.
- Formação e classe de palavras.
- Relações de subordinação e coordenação.
- Itens gramaticais relevantes à compreensão dos conteúdos semânticos.
- Vocabulário e morfossintaxe da língua inglesa.
- Itens gramaticais relevantes para compreensão semântica e formas contemporâneas do inglês.
- Domínio do vocabulário e da estrutura da língua.
- Itens gramaticais relevantes para compreensão semântica.
- Itens gramaticais relevantes para a compreensão semântica.
- Itens gramaticais relevantes para a compreensão de conteúdos semânticos em língua inglesa.
- Aspectos sintático-gramaticais: artigos definidos e indefinidos, tempos e modos verbais, preposições, conjunções, pronomes e modais.
- Concordância nominal e verbal, formação e classe de palavras, expressões idiomáticas, subordinação e coordenação, voz passiva, discurso direto e indireto.
- Estruturas gramaticais: adjectives, adverbs, nouns, articles, conjunctions, modals, prepositions, pronouns, verb tenses, passive voice e wh-questions.
- Elementos de referência.
- Itens gramaticais relevantes para compreensão dos conteúdos semânticos.
- Vocabulário e estruturas gramaticais necessários à compreensão de textos.
- Itens gramaticais relevantes para a compreensão dos conteúdos semânticos (inglês).
- Aspectos gramaticais relevantes à compreensão de texto.

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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo lingua-inglesa.gramatica-para-leitura.banco-N.json, onde N é o lote)
```json
{
  "materia": "lingua-inglesa.gramatica-para-leitura",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
