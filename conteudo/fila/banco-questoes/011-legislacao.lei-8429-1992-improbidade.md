Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Legislação: Lei de Improbidade Administrativa (Lei 8.429/1992)** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Sistema de responsabilização por improbidade nos arts. 1º a 23-C da Lei nº 8.429/1992, redação após Lei nº 14.230/2021 e controle constitucional até 08/10/2026: dolo, sujeitos, tipos dos arts. 9º a 11, sanções, investigação, indisponibilidade, ação, acordo, prescrição e precedentes.
Fica de fora (outras matérias tratam): Regimes disciplinares, crimes contra a Administração, responsabilidade civil geral e licitações como disciplinas autônomas; são mencionados apenas quando necessários para compreender tipos e distinções da LIA.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Improbidade administrativa.
- Lei n. 8.429/1992.
- Improbidade Administrativa (Lei n. 8.429/1992).
- Improbidade administrativa: Lei nº 8.429/1992.
- Lei nº 8.429/1992 (Lei de Improbidade Administrativa).
- Lei nº 8.429/1992: improbidade administrativa.
- Lei nº 8.429/1992.
- Lei nº 8.429/1992: disposições gerais e atos de improbidade administrativa.
- Improbidade Administrativa (Lei nº 8.429/1992).
- Lei nº 8.429/1992 e suas alterações: Disposições gerais.
- Ação de improbidade administrativa.
- Improbidade administrativa: sanções penais e civis — Lei nº 8.429/1992 e alterações.
- Lei nº 8.429/1992, alterada pela Lei nº 14.230/2021: improbidade administrativa.
- Ética no setor público: Lei n.º 8.429/1992 e suas alterações.
- Lei n.º 8.429/1992 e suas alterações: Disposições gerais.
- Lei n.º 8.429/1992 e suas alterações: Atos de improbidade administrativa.
- Ética no setor público: Lei nº 8.429/1992 e suas alterações.
- Lei nº 8.429/1992 e suas alterações: Atos de improbidade administrativa.
- Controle da administração pública: Lei nº 8.429/1992 e suas alterações (improbidade administrativa).
- Lei de Improbidade Administrativa (Lei nº 8.429/1992 com redação dada pela Lei nº 14.230/2021).
- Lei nº 8.429/1992 e suas alterações (Lei de Improbidade Administrativa).
- Atos de improbidade administrativa.
- Lei de Improbidade Administrativa (Lei nº 8.429/1992 e suas alterações).
- Improbidade administrativa: sanções penais e civis — Lei nº 8.429/1992 e suas alterações.
- Improbidade Administrativa (Lei nº 8.429/1992 e suas alterações).
- Lei de Improbidade Administrativa — Lei nº 8.429, de 02 de junho de 1992.
- Improbidade administrativa: Lei nº 8.429/1992 e alterações.
- Ética no setor público: Lei nº 8.429/1992 e alterações, disposições gerais e atos de improbidade administrativa.
- Lei nº 8.429/1992 e alterações.
- Agentes públicos e servidores públicos: improbidade administrativa.

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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo legislacao.lei-8429-1992-improbidade.banco-N.json, onde N é o lote)
```json
{
  "materia": "legislacao.lei-8429-1992-improbidade",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
