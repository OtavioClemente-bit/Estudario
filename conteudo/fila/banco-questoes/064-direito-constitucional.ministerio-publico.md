Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Constitucional: Ministério Público e MPT** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Arts. 127 a 130-A da Constituição: perfil e princípios do Ministério Público, autonomia, ramos do MPU e MPT, chefia, ingresso, garantias, vedações, funções, CNMP e atuação constitucional trabalhista (art. 114, § 3º), com LC nº 75/1993 como apoio.
Fica de fora (outras matérias tratam): Organização infraconstitucional detalhada das carreiras; ritos processuais; atribuições de ramos e órgãos que ultrapassem a compreensão constitucional do MP/MPT; estudo aprofundado de Advocacia Pública, advocacia e Defensoria Pública, citadas apenas em contraste.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Ministério Público.
- Funções essenciais à justiça: Ministério Público.
- Funções essenciais à justiça: Ministério Público, Advocacia Pública.
- Funções essenciais à justiça: Ministério Público, Advocacia Pública e Defensoria Pública.
- Funções essenciais à Justiça: Ministério Público e advocacia pública.
- Das funções essenciais à Justiça: do Ministério Público.
- Conselho Nacional do Ministério Público.
- Funções essenciais à Justiça: Ministério Público, advocacia e defensoria públicas.
- Ministério Público, Ministério Público junto aos tribunais de contas e advocacia pública.
- Constituição da República Federativa do Brasil de 1988: Funções essenciais à justiça.
- Funções essenciais à Justiça: Ministério Público, advocacia e Defensoria Pública.
- Constituição Federal: Título IV, Capítulo IV, Seção I.
- Lei Complementar nº 75/1993.
- Funções essenciais à Justiça, Ministério Público, Advocacia Pública, advocacia e Defensoria Pública.
- Funções essenciais à justiça. Ministério Público e Advocacia Pública.
- Ministério Público: Princípios, garantias, vedações, organização e competências.
- Ministério Público: Conselho Nacional do Ministério Público.
- Ramos do Ministério Público e funções exclusivas e concorrentes.
- Membros do MPU: ingresso, promoção, aposentadoria, garantias, prerrogativas e vedações.
- Conselho Nacional do Ministério Público: composição e atribuições constitucionais.
- Funções essenciais à Justiça: Ministério Público, princípios, garantias, vedações, organização e competências.
- Funções essenciais à Justiça: Ministério Público: princípios, garantias, vedações, organização e competências.
- Funções essenciais à Justiça: Ministério Público, Advocacia Pública, advocacia e Defensoria Pública.
- Ministério Público e defesa do Estado e das instituições democráticas.
- Ministério Público, advocacia pública.
- Funções essenciais à Justiça: Ministério Público, Advocacia, Advocacia Pública e Defensoria Pública.
- Funções essenciais à Justiça: Ministério Público;
- Ministério Público do Trabalho: atribuições, garantias e vedações;
- Ministério Público e funções essenciais à justiça.
- Ministério Público: princípios constitucionais.

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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito-constitucional.ministerio-publico.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito-constitucional.ministerio-publico",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false }, { "key": "B", "text": "...", "correct": true } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (A–E com 5 opções ou A–D com 4 opções) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
