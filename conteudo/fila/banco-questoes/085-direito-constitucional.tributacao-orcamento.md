Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Constitucional: Tributação e orçamento na Constituição** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Sistema Tributário Nacional: espécies comuns aos entes, capacidade contributiva, lei complementar e reforma do consumo; repartição direta e indireta das receitas, finanças públicas e orçamentos constitucionais — PPA, LDO, LOA, emendas, créditos, vedações, duodécimos e pessoal.
Fica de fora (outras matérias tratam): Competência tributária individualizada e limitações em profundidade; controle externo e tribunais de contas em detalhe; técnica operacional de AFO e Lei de Responsabilidade Fiscal em profundidade.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Repartição das receitas tributárias.
- Sistema Tributário Nacional.
- Finanças públicas.
- Sistema Tributário Nacional: Princípios gerais.
- Sistema Tributário Nacional: Repartição das receitas tributárias.
- Sistema Tributário Nacional. Princípios gerais.
- Orçamento na Constituição de 1988: Plano Plurianual (PPA), Lei de Diretrizes Orçamentárias (LDO), Lei Orçamentária Anual (LOA).
- Finanças públicas: normas gerais e orçamento público.
- Finanças públicas: Normas gerais.
- Orçamentos.
- Tributação e orçamento.
- Orçamento na Constituição Federal de 1988.
- Da Repartição das Receitas Tributárias.
- Dos princípios gerais.
- Direito financeiro na Constituição Federal de 1988.
- Finanças públicas na Constituição de 1988.
- Finanças públicas: Orçamentos.
- Sistema Tributário Nacional na Constituição Federal: Da Tributação e do Orçamento.
- Do sistema tributário nacional.
- Direito Financeiro na Constituição Federal: das Finanças Públicas (arts. 165 a 169 da CF/88).
- Dívida ativa, repartição de receitas e federalismo fiscal.
- Vinculação e desvinculação de receitas.
- Orçamento na Constituição de 1988.
- Repartição de receitas.
- Orçamento.
- Noções sobre o Sistema Tributário Nacional.
- Tributação, orçamento, Sistema Tributário Nacional e finanças públicas.
- Sistema tributário nacional e repartição das receitas tributárias.
- Direito Financeiro Orçamento na Constituição de 1988.
- Receitas de transferências constitucionais e legais.

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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito-constitucional.tributacao-orcamento.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito-constitucional.tributacao-orcamento",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false }, { "key": "B", "text": "...", "correct": true } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (A–E com 5 opções ou A–D com 4 opções) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
