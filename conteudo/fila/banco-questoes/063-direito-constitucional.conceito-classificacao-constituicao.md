Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Constitucional: Conceito e classificação das constituições** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Conceito, natureza, objeto, estrutura, elementos, supremacia e interpretação da Constituição; concepções sociológica, política e jurídica; classificações e ciclos constitucionais; princípios fundamentais dos arts. 1º a 4º; natureza e classificação da Constituição de 1988; poder constituinte originário e derivado, reforma e revisão.
Fica de fora (outras matérias tratam): Estudo específico dos direitos fundamentais, controle de constitucionalidade em seus procedimentos e processo legislativo geral, exceto regras estritamente necessárias à reforma constitucional do art. 60.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Conceito de Constituição.
- Constituição: Conceito, objeto, elementos e classificações.
- Constituição: conceito, classificações e princípios fundamentais.
- Constituição.
- Supremacia da Constituição.
- Constituição: conceito e classificação.
- Constituição Federal de 1988: conceito, contexto histórico, características, estrutura do texto.
- Constituição: conceito, classificações, princípios fundamentais.
- Classificações das constituições.
- Conceito, objeto, elementos e classificações da Constituição.
- Constituição: conceito, objeto, elementos, classificações e supremacia.
- Constituição da República Federativa do Brasil de 1988 e emendas: Conceito, classificações, princípios fundamentais.
- Constituição: supremacia da Constituição.
- Classificação.
- Neoconstitucionalismo.
- Direito Constitucional: conceito, objeto, elementos e classificações.
- Conceito, classificações, princípios fundamentais, emendas constitucionais.
- Conceito, natureza, classificação e estrutura da Constituição Federal de 1988.
- Constitucionalismo.
- A ordem constitucional vigente.
- Estado e Constituição.
- Classificação das Constituições.
- Constituições material e formal, Constituição-garantia e Constituição dirigente.
- Direito Constitucional: natureza, conceito e objeto.
- Direito constitucional: Noções gerais, ciclos constitucionais.
- História das constituições.
- Teoria geral da Constituição: conceito, origens, conteúdo, estrutura e classificação.
- Constituição de 1988: conceito, contexto histórico, características, estrutura do texto.
- Teoria da Constituição, poder constituinte, princípios fundamentais, interpretação e eficácia das normas constitucionais.
- Constituição: conceito, objeto, elementos, classificação, supremacia e interpretação.

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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito-constitucional.conceito-classificacao-constituicao.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito-constitucional.conceito-classificacao-constituicao",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false }, { "key": "B", "text": "...", "correct": true } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (A–E com 5 opções ou A–D com 4 opções) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
