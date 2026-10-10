Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Legislação: Lei 8.112/1990: sindicância e processo administrativo disciplinar** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Sindicâncias investigativa, patrimonial e acusatória; afastamento preventivo; PAD ordinário e sumário; instauração, comissão, inquérito, defesa, relatório, julgamento, nulidades, recursos, revisão e Súmula Vinculante 5.
Fica de fora (outras matérias tratam): Descrição aprofundada de deveres, proibições, espécies de penalidades e prescrição material; regimes disciplinares estaduais, municipais, militares ou de empregados celetistas.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Processo administrativo disciplinar.
- Espécies de Procedimento Disciplinar: sindicâncias investigativa, patrimonial e acusatória; processo administrativo disciplinar (ritos ordinário e sumário).
- Fases do processo administrativo disciplinar: instauração, inquérito e julgamento.
- Comissão Disciplinar: requisitos, suspeição, impedimento e prazo para conclusão dos trabalhos (prorrogação e recondução).
- Agentes públicos: Processo administrativo disciplinar.
- Do processo administrativo disciplinar.
- Processo administrativo disciplinar e Lei nº 8.112/1990.
- Direito administrativo disciplinar: Procedimentos disciplinares da administração pública.
- Disposições doutrinárias: Processo administrativo disciplinar.
- Lei nº 8.112/1990 e processo disciplinar.
- Processo administrativo disciplinar e sua revisão.
- Responsabilidade do agente público: sindicância.
- Responsabilidade do agente público: processo administrativo disciplinar e termo de ajustamento de conduta.
- Regime e processo disciplinar.
- Processo administrativo disciplinar – noções.
- Processo administrativo sancionador e disciplinar.
- Responsabilidade e processo administrativo disciplinar dos servidores.
- Sistema de correição do poder executivo federal (Decreto nº 5.480/2005, Decreto nº 5.683/2006, Decreto nº 7.128/2010, Portaria CGU nº 335/2006).
- Responsabilidade e processo administrativo disciplinar; Lei nº 8.112/1990.
- Processo disciplinar e Lei nº 8.112/1990.
- Processo administrativo disciplinar e sindicância.
- Regime disciplinar e processo administrativo disciplinar.
- Processo administrativo disciplinar e responsabilização de agentes públicos.
- Procedimentos disciplinares da administração pública.
- Processos administrativos em espécie e processo administrativo disciplinar.
- Processo Administrativo Disciplinar (PAD).
- Responsabilidade, penalidades e processo administrativo disciplinar.
- Sindicância e processo administrativo disciplinar.
- A sindicância.
- Processo administrativo disciplinar. Processo sumário. Sindicância. Verdade sabida.

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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo legislacao.lei-8112-1990-processo-disciplinar.banco-N.json, onde N é o lote)
```json
{
  "materia": "legislacao.lei-8112-1990-processo-disciplinar",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
