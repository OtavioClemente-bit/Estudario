Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Legislação: Lei 8.112/1990: disposições preliminares, provimento, vacância, remoção, redistribuição e substituição** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Lei nº 8.112/1990, arts. 1º a 39: disposições preliminares, requisitos de investidura, concurso, nomeação, posse, exercício, estágio probatório e estabilidade, formas de provimento, vacância, remoção, redistribuição e substituição; leitura constitucional e regulamentar indispensável.
Fica de fora (outras matérias tratam): Vencimentos e remuneração em geral, vantagens, férias, licenças e afastamentos em detalhe, deveres, responsabilidades e processo disciplinar, ressalvadas referências estritamente necessárias à interpretação dos arts. 1º a 39.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Lei n. 8.112/1990 e alterações: Das Disposições Preliminares;
- Do Provimento, Da Vacância, Da Remoção, Da Redistribuição e Da Substituição;
- Formas de provimento e vacância dos cargos públicos.
- Vacância.
- Estágio probatório.
- Provimento, vacância, remoção, redistribuição e substituição.
- Cargo, emprego e função pública: Vacância.
- Lei nº 8.112/1990 e alterações: provimento, vacância, remoção, redistribuição e substituição.
- Lei nº 8.112/1990 e alterações: Das disposições preliminares.
- Do Provimento, Vacância, Remoção, Redistribuição e Substituição: do provimento, da vacância, da remoção, da redistribuição e da substituição.
- Lei nº 8.112/1990 (Regime Jurídico dos Servidores Públicos Civis da União): disposições preliminares.
- Disposições doutrinárias: Vacância.
- Provimento.
- Efetividade, estabilidade e vitaliciedade.
- Regime jurídico: provimento, vacância, remoção, redistribuição e substituição.
- Dos Servidores Públicos – Lei n.º 8.112/90: disposições preliminares, provimento, vacância.
- Regime Jurídico dos Servidores Públicos Civis da União: disposições preliminares, provimento, vacância, remoção, redistribuição e substituição.
- Regime jurídico único: provimento, vacância, remoção, redistribuição e substituição.
- Regime jurídico dos servidores públicos civis da União (Lei nº 8.112/1990 e alterações): disposições preliminares.
- Regime jurídico dos servidores públicos civis da União (Lei nº 8.112/1990 e alterações): provimento.
- Regime jurídico dos servidores públicos civis da União (Lei nº 8.112/1990 e alterações): vacância.
- Agentes públicos e servidores públicos: estágio probatório.
- Agentes públicos e servidores públicos: formas de provimento e vacância dos cargos públicos.
- Agentes públicos e servidores públicos: exigência constitucional de concurso público para investidura em cargo ou emprego público.
- Lei nº 8.112/1990 (Regime Jurídico dos Servidores Públicos Civis da União e alterações): disposições preliminares.
- Lei nº 8.112/1990: provimento, vacância, remoção, redistribuição e substituição.
- Provimento e vacância dos cargos públicos e exigência de concurso público.
- Provimento e movimentação.
- Lei nº 8.112/1990, provimento, vacância e concurso público.
- Funcionário efetivo e vitalício: garantias e estágio probatório.

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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo legislacao.lei-8112-1990-provimento.banco-N.json, onde N é o lote)
```json
{
  "materia": "legislacao.lei-8112-1990-provimento",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
