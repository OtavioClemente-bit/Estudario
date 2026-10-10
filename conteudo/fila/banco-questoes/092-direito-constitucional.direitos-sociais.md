Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Constitucional: Direitos sociais (arts. 6º a 11)** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Direitos sociais do art. 6º; direitos dos trabalhadores do art. 7º em visão geral; liberdade sindical, greve e participação laboral dos arts. 8º a 11; reserva do possível e mínimo existencial.
Fica de fora (outras matérias tratam): Direitos e garantias individuais do art. 5º, regimes legais trabalhistas detalhados e controvérsias processuais sobre dissídios coletivos.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Direitos sociais.
- Direitos e garantias fundamentais: Direitos sociais.
- Dos direitos sociais.
- Direitos e garantias fundamentais: Dos Direitos Sociais.
- Princípios constitucionais do trabalho.
- Direitos sociais e sua efetivação.
- Dos direitos e garantias fundamentais: dos direitos sociais.
- Direitos e deveres fundamentais: Direitos sociais, nacionalidade, cidadania e direitos políticos.
- Direitos e deveres fundamentais: Direitos sociais e sua efetivação.
- Direitos sociais, dignidade do trabalhador e proteção constitucional ao trabalho.
- Liberdade e organização sindical, contribuição sindical e direito de greve.
- Direitos sociais e sua efetivação, direito à saúde e Sistema Único de Saúde.
- Poder constituinte: Direitos sociais.
- Direitos e garantias fundamentais: direitos sociais e direitos políticos.
- Direitos sociais, nacionalidade e cidadania.
- Direitos sociais em espécie.
- Direitos sociais: conceito e classificação.
- Direitos dos trabalhadores (individuais e coletivos).
- Fundamentos constitucionais dos direitos sociais. Ordem social e direitos sociais. Direitos sociais e direitos econômicos. Conceito e classificação dos direitos sociais.
- Direitos sociais: direito à alimentação, à moradia, à saúde, à educação, ao trabalho, direitos constitucionais trabalhistas, do direito ao futuro.
- Organização sindical.
- Os direitos constitucionais dos trabalhadores.
- A Saúde e a Teoria dos Direitos Sociais.
- Saúde e a Teoria dos Direitos Sociais.
- Direitos sociais, econômicos, culturais e ambientais.
- Direitos sociais e sua relação com a dignidade do trabalhador.
- Proteção constitucional ao trabalho e princípios da dignidade humana e valorização do trabalho.
- Liberdade sindical, organização dos trabalhadores, contribuição sindical e autonomia das entidades sindicais.
- Direito de greve de trabalhadores e servidores públicos.

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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito-constitucional.direitos-sociais.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito-constitucional.direitos-sociais",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
