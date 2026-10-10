Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Constitucional: Poder Judiciário: disposições gerais e magistratura** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Organização constitucional do Poder Judiciário e seus órgãos (art. 92); Estatuto da Magistratura (art. 93); quinto constitucional (art. 94); garantias e vedações (art. 95); autonomia e administração (arts. 96 a 99); regime geral de precatórios e RPV (art. 100), conforme EC 134/2024 e EC 136/2025.
Fica de fora (outras matérias tratam): Composição e competências específicas das cortes superiores e do conselho; aprofundamento de ramos da Justiça e das modalidades de controle de constitucionalidade; rito de execução e cálculos individualizados de precatórios.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Poder Judiciário: disposições gerais.
- Poder Judiciário.
- Poder judiciário: Órgãos do poder judiciário.
- Órgãos do Poder Judiciário.
- Órgãos do poder judiciário: Organização e competências.
- Organização dos poderes no Estado: Poder judiciário.
- Poder judiciário: Organização e competências.
- Órgãos do Poder Judiciário: Competências.
- Do Poder Judiciário: disposições gerais.
- Disciplina constitucional dos precatórios.
- Constituição da República Federativa do Brasil de 1988: Poder Judiciário.
- Poder Judiciário: órgãos, organização e competências e Conselho Nacional de Justiça.
- Poder judiciário. Disposições gerais.
- Órgãos do poder judiciário. Organização e competências, Conselho Nacional de Justiça.
- Poder Judiciário: órgãos, organização, competências e Conselho Nacional de Justiça.
- Órgãos do poder judiciário e suas competências.
- Organização e competências.
- Poder Judiciário, órgãos, organização e competências e Conselho Nacional de Justiça.
- Garantias do Poder Judiciário.
- Do Poder Judiciário.
- Poder Judiciário: Órgãos e competências do Poder Judiciário.
- Disposições gerais do Poder Judiciário.
- Poder Judiciário, órgãos, organização, competências e Conselho Nacional de Justiça.
- Poder Judiciário: órgãos, competências e Conselho Nacional de Justiça.
- Poder Judiciário, órgãos e competências e Conselho Nacional de Justiça.
- Poder Judiciário: órgãos, composição, garantias e competências.
- Poder Judiciário: disposições gerais, órgãos, competências e Conselho Nacional de Justiça.
- Lei Orgânica da Magistratura Nacional.
- As garantias do Poder Judiciário.
- Direitos e deveres funcionais da magistratura.

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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito-constitucional.poder-judiciario-disposicoes-gerais.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito-constitucional.poder-judiciario-disposicoes-gerais",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false }, { "key": "B", "text": "...", "correct": true } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (A–E com 5 opções ou A–D com 4 opções) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
