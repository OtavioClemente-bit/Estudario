Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Constitucional: Advocacia Pública, Advocacia e Defensoria Pública** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Arts. 131 a 135 da Constituição: representação da União, consultoria e assessoramento do Executivo, procuradorias estaduais e distrital, peculiaridade municipal, advocacia privada e Defensoria Pública, seus princípios, autonomia e garantias.
Fica de fora (outras matérias tratam): Organização detalhada de outras funções essenciais à justiça, organização judiciária e questões processuais específicas que extrapolem o núcleo constitucional.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Funções essenciais à Justiça.
- Defensoria Pública.
- Funções essenciais à Justiça: defensoria pública.
- Advocacia pública.
- Funções essenciais à justiça: Advocacia Pública.
- Da Advocacia e da Defensoria Pública.
- Advocacia e Defensoria Pública.
- Funções essenciais à justiça: Advocacia e Defensoria Pública.
- Advocacia pública consultiva.
- Advocacia e Defensoria Públicas.
- Funções essenciais à Justiça: Ministério Público, advocacia e defensorias públicas.
- Funções essenciais à justiça: Ministério Público, advocacia pública, advocacia privada e Defensoria Pública.
- Funções essenciais à Justiça: Ministério Público, advocacia e defensoria públicas.
- Da Advocacia Pública.
- Das Funções Essenciais à Justiça.
- Funções essenciais da justiça.
- Advocacia Pública (arts. 44 a 132).
- Advocacia Pública;
- Advocacia-Geral da União: representação, consultoria e assessoramento jurídico.
- Competências constitucionais, história, estrutura, órgãos e organização da Advocacia-Geral da União.
- Advogado-Geral da União: provimento e competências.
- Consultoria e assessoramento jurídico ao Poder Executivo federal.
- Representação judicial e extrajudicial da União e de agentes públicos.
- Advocacia-Geral da União: perfil constitucional e funções institucionais.
- As funções essenciais à Justiça.
- A Advocacia-Geral da União na CF/88.
- Advocacia-Geral da União.
- Procuradoria-Geral Federal.
- Advocacia pública consultiva e hipóteses de manifestação obrigatória.
- Advocacia Pública: representação judicial e extrajudicial das pessoas jurídicas de direito público.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, literalidade da lei com o detalhe que a prova troca, jurisprudência e súmulas, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Cite artigo, lei, súmula e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Se não tiver certeza de um número, explique sem ele — nunca invente.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito-constitucional.advocacia-defensoria.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito-constitucional.advocacia-defensoria",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
