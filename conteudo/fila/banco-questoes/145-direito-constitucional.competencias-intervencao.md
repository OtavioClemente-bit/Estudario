Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Constitucional: Repartição de competências e intervenção** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Competências materiais exclusivas da União (art. 21); competências legislativas privativas da União e delegação a Estados (art. 22); competências administrativas comuns (art. 23); competências legislativas concorrentes e regras de suplementação (art. 24); competências remanescentes dos Estados (art. 25); competências municipais de interesse local e suplementação (art. 30); competências do Distrito Federal (art. 32, § 1º); intervenção federal e estadual, hipóteses, iniciativa, decreto e controle (arts. 34 a 36).
Fica de fora (outras matérias tratam): Organização dos Poderes, processo legislativo geral, repartição tributária e competências específicas de órgãos ou políticas fora dos arts. 21 a 30 e das regras de intervenção dos arts. 34 a 36.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Organização do Estado: Intervenção federal.
- Organização do Estado: Intervenção dos estados nos municípios.
- Das competências da União, dos Estados e dos Municípios.
- Intervenção federal.
- O Processo Legislativo na Constituição da República de 1988: competências constitucionais exclusivas, concorrentes e privativas no ato de legislar.
- Ação Direta de Inconstitucionalidade Interventiva.
- Intervenção dos estados nos municípios.
- Repartição de competências.
- Intervenção do Estado nos Municípios.
- Intervenção.
- Divisão de competências entre os entes federados.
- Intervenção federal nos estados.
- Intervenção nos Municípios.
- Intervenção federal e estadual.
- Organização do Estado, repartição de competências, federalismo e intervenção.
- Intervenção federal e intervenção dos Estados nos Municípios.
- Organização político-administrativa: competências da União, estados e municípios.
- Organização político administrativa: federação brasileira, competências da União, estados e municípios, intervenção federal.
- Da intervenção.
- Repartição de competências na Federação e suas técnicas.
- A repartição de competência na Constituição de 1988.
- Intervenção federal nos municípios.
- Estado federal, repartição de competências e Federação brasileira.
- Organização político-administrativa do Estado: competências da União, Estados, Distrito Federal e Municípios.
- Sistemas de repartição de competência.
- Discriminação de competência na Constituição de 1988.
- União: competência.
- Atribuições, competências e relações entre esferas governamentais no regime federativo.
- Competências e relações entre esferas de governo no regime federativo.
- Atribuições, competências e relações entre esferas de governo no regime federativo na CF/88.

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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito-constitucional.competencias-intervencao.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito-constitucional.competencias-intervencao",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
