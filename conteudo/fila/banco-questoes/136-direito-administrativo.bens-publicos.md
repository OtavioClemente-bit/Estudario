Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Administrativo: Bens públicos** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Classificação em bens de uso comum do povo, de uso especial e dominicais; afetação e desafetação; inalienabilidade relativa, impenhorabilidade e imprescritibilidade; autorização, permissão e concessão de uso.
Fica de fora (outras matérias tratam): Licitações e contratos em geral, desapropriação e intervenção na propriedade, salvo distinções indispensáveis à alienação e ao uso dos bens.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Bens Públicos.
- Bens públicos: regime jurídico, classificação, administração, aquisição e alienação, formas de utilização por terceiros.
- Bens públicos: classificação e caracteres jurídicos.
- Bens públicos: Regime jurídico.
- Domínio público.
- Vias públicas, cemitérios públicos e portos.
- Bens públicos: natureza jurídica do domínio público.
- Terrenos de marinha e seus acrescidos.
- Bens públicos: Aquisição e alienação dos bens públicos.
- Bens públicos: Formas de utilização dos bens públicos pelos particulares.
- Domínio público: conceito e classificação dos bens públicos.
- Administração, utilização e alienação dos bens públicos.
- Imprescritibilidade, impenhorabilidade e não oneração dos bens públicos.
- Natureza jurídica do domínio público.
- Utilização dos bens públicos: autorização, permissão e concessão de uso, ocupação, aforamento, concessão de domínio pleno.
- Aquisição e alienação dos bens públicos.
- Formas de utilização dos bens públicos pelos particulares.
- Formas de utilização dos bens públicos por terceiros.
- Regime jurídico dos bens públicos.
- Patrimônio público: bens de uso comum, bens de uso especial, bens dominicais.
- Bens públicos: utilização dos bens públicos, autorização, permissão e concessão de uso, ocupação, aforamento, concessão de domínio pleno.
- Bens públicos e distinção dos bens particulares.
- Bens públicos: espécies, prerrogativas, vedações.
- Gestão dos bens públicos.
- Bens públicos, espécies, classificações.
- Bens público em espécie.
- Regimes jurídicos.
- Aquisição e alienação.
- Uso de bem público por particular.
- Bens públicos: classificação, regime jurídico, bens da União e legislação patrimonial.

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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito-administrativo.bens-publicos.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito-administrativo.bens-publicos",
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
