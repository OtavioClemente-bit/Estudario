Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Economia: Economia do Setor Público** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Funções alocativa, distributiva e estabilizadora do governo; bens públicos e recursos comuns; externalidades e instrumentos corretivos; déficit e dívida pública; princípios e efeitos econômicos da tributação.
Fica de fora (outras matérias tratam): Regras jurídicas específicas, percentuais legais de repartição ou limites fiscais, alíquotas vigentes e dados atuais de dívida ou orçamento.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- O papel do Estado e a atuação do governo nas finanças públicas.
- Objetivos, metas, abrangência e definição de Finanças Públicas.
- Externalidades.
- Visão clássica das funções do Estado e evolução das funções do Governo.
- Falhas de mercado, bens públicos e externalidades; papel do Governo.
- Objetivos da política fiscal e políticas alocativas, distributivas e de estabilização.
- Financiamento dos gastos públicos, tributação, equidade e tipos de tributos.
- Déficit público e financiamento do déficit.
- Resultado Fiscal do Governo (NFSP): resultado primário e resultado nominal.
- Noções de Economia do Setor Público: equilíbrio competitivo e eficiência econômica.
- Noções sobre teoremas de bem-estar.
- Formas e dimensões da intervenção da administração na economia.
- Conceitos de déficit e dívida pública.
- As necessidades públicas e as formas de atuação dos governos.
- Política fiscal, tributos e gastos do governo.
- Déficit público.
- O papel do Estado e a atuação do governo nas finanças públicas: Formas e dimensões da intervenção da administração na economia.
- O conceito de Ótimo de Pareto.
- Resultado Fiscal do Governo (Necessidade de Financiamento do Setor Público – NFSP): Resultado Primário e Resultado Nominal.
- Noções sobre economia do setor público.
- Efeitos da atuação do Estado na economia.
- Políticas alocativas, distributivas e de estabilização.
- Política tributária: como os impostos influem nas decisões de consumo, poupança e gasto.
- A função estabilizadora do sistema tributário: a política fiscal e estabilizadores automáticos.
- Falhas de Mercado: Externalidades e ineficiência de mercado.
- Externalidades positivas e negativas.
- Soluções privadas para o problema das externalidades.
- Teorema de Coase.
- Custos de Transação e os limites das soluções privadas ao problema das externalidades.
- Políticas Públicas para as externalidades: Regulamentação.

## Cada lote de 50
- 38 de múltipla escolha A a E (5 alternativas) e 12 de Certo/Errado. (O app transforma A–E em A–D tirando uma errada, por isso cada alternativa tem comentário próprio.)
- Dificuldade: 15 fáceis, 20 médias, 15 difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, cálculo com todos os dados no enunciado, interpretação de tabela ou gráfico, comparação entre conceitos parecidos. Use casos DIFERENTES entre si; nada de repetir o mesmo caso com outras palavras.

## Regras de qualidade
1. Regra precisa, com exemplos concretos; nunca invente regra ou dado.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo economia.economia-setor-publico.banco-N.json, onde N é o lote)
```json
{
  "materia": "economia.economia-setor-publico",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
