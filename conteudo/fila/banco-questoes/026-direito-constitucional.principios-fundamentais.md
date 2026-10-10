Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Constitucional: Princípios fundamentais (arts. 1º a 4º)** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Constituição Federal, arts. 1º a 4º: fundamentos da República, titularidade e exercício do poder, separação dos Poderes, objetivos fundamentais, princípios das relações internacionais e parágrafo único do art. 4º.
Fica de fora (outras matérias tratam): Nacionalidade, direitos e garantias fundamentais, organização dos Poderes em detalhe, defesa do Estado e ordem econômica, tratados em matérias próprias.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Princípios fundamentais.
- Constituição da República Federativa do Brasil de 1988: princípios fundamentais.
- Dos Princípios fundamentais.
- Constituição Federal de 1988 e princípios fundamentais.
- Princípios fundamentais da Constituição Federal de 1988.
- Princípios fundamentais da CF/88.
- Constituição: princípios fundamentais.
- Constituição Federal de 1988: princípios fundamentais, artigos 1º ao 4º.
- Constituição da República Federativa do Brasil de 1988. Princípios fundamentais.
- Constituição da República Federativa do Brasil de 1988, emendas constitucionais e emendas constitucionais de revisão: princípios fundamentais.
- Princípios fundamentais da Constituição Federal.
- Constituição da República Federativa do Brasil de 1988 (Artigos 1º, 3º, 4° e 5°).
- Princípios constitucionais.
- Constituição Federal: princípios fundamentais e aplicabilidade das normas.
- Constituição da República Federativa do Brasil (artigos 1º, 3º, 4º e 5º).
- Dignidade da pessoa humana.
- Estado de direito e a Constituição Federal de 1988: consolidação da democracia, representação política e participação cidadã.
- Constituição da República Federativa do Brasil de 1988 e princípios fundamentais.
- Constituição Federal, princípios e aplicabilidade das normas constitucionais.
- Constituição Federal de 1988: Princípios Fundamentais, Art. 1º ao 4º.
- Constituição da República Federativa do Brasil de 1988: Preâmbulo e princípios fundamentais.
- Princípios constitucionais do Estado brasileiro.
- O Estado brasileiro.
- Estado Democrático de Direito.
- A República Federativa do Brasil.
- Princípios fundamentais da CF/88 (arts. 1º a 4º).
- Constituição Federal de 1988: princípios fundamentais.
- Princípios fundamentais do Direito Constitucional.
- Pluralismo.
- Fundamentos, objetivos e princípios fundamentais da República Federativa do Brasil.

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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito-constitucional.principios-fundamentais.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito-constitucional.principios-fundamentais",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false }, { "key": "B", "text": "...", "correct": true } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (A–E com 5 opções ou A–D com 4 opções) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
