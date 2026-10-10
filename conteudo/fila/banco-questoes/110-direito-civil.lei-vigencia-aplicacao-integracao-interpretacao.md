Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Civil: Lei: vigência, aplicação no tempo e no espaço, integração e interpretação** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Vigência, vacatio legis, revogação, aplicação da lei no tempo e no espaço, regras de direito internacional privado dos arts. 7º a 19 da LINDB, integração de lacunas e interpretação conforme os fins sociais e o bem comum.
Fica de fora (outras matérias tratam): Personalidade, capacidade, pessoas jurídicas e domicílio como temas autônomos; regras de gestão pública dos arts. 20 a 30 da LINDB; conteúdo material de outros ramos além das conexões necessárias à aplicação da lei.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Conflito das leis no tempo.
- Lei de Introdução às Normas do Direito Brasileiro.
- Vigência, aplicação, obrigatoriedade, interpretação e integração das leis.
- Eficácia das leis no espaço.
- Lei de Introdução às normas do Direito Brasileiro: vigência, aplicação, interpretação e integração das leis.
- Lei de introdução às normas do direito brasileiro: Conflito das leis no tempo.
- Lei de introdução às normas do direito brasileiro: Eficácia das leis no espaço.
- Eficácia da lei no espaço.
- Lei de Introdução ao Código Civil.
- Integração e interpretação da lei.
- Vigência, aplicação, interpretação e integração das leis.
- Interpretação e integração da norma jurídica.
- Lei: vigência; aplicação da lei no tempo e no espaço.
- Introdução ao Direito Civil.
- Conflito das leis no tempo e eficácia das leis no espaço.
- Lei de Introdução às Normas do Direito Brasileiro. Vigência, aplicação, obrigatoriedade, interpretação e integração das leis.
- Parte geral: Decreto-Lei nº 4.657/1942, alterado pela Lei nº 12.376/2010 (Lei de Introdução às Normas do Direito Brasileiro).
- Lei de introdução às normas do direito brasileiro: eficácia da lei no espaço.
- Interpretação e integração da norma jurídica e LINDB.
- Normas jurídicas.
- Lacunas.
- Lei de Introdução às Normas do Direito Brasileiro: vigência, aplicação, obrigatoriedade, interpretação e integração das leis.
- Direito Civil Lei de Introdução às Normas do Direito Brasileiro.
- Decreto-lei nº 4.657/1942 (Lei de Introdução às normas do Direito Brasileiro): arts. 1º a 19.
- Introdução ao Direito Civil: Conflito das leis no tempo.
- Introdução ao Direito Civil: Eficácia das leis no espaço.
- Interpretação e integração da norma jurídica e Lei de Introdução às Normas do Direito Brasileiro.
- Introdução ao Direito Civil e LINDB: vigência, aplicação, obrigatoriedade, interpretação e integração das leis.
- Lei de introdução às normas do direito brasileiro: vigência, aplicação, interpretação, integração e eficácia.
- Aplicação da lei no tempo.

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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito-civil.lei-vigencia-aplicacao-integracao-interpretacao.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito-civil.lei-vigencia-aplicacao-integracao-interpretacao",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false }, { "key": "B", "text": "...", "correct": true } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (A–E com 5 opções ou A–D com 4 opções) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
