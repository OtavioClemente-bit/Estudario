Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Constitucional: Organização dos Poderes e separação das funções** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Unidade do poder estatal; separação e coordenação dos Poderes Legislativo, Executivo e Judiciário; funções típicas e atípicas; freios e contrapesos; independência, autonomia e garantias institucionais.
Fica de fora (outras matérias tratam): Organização detalhada do processo legislativo e das Casas legislativas; atribuições pormenorizadas da Chefia do Executivo; estrutura judiciária; técnicas de controle de constitucionalidade; tratados e competências materiais específicas.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Da organização dos Poderes.
- Organização dos poderes no Estado: Mecanismos de freios e contrapesos.
- Organização dos poderes no Estado.
- Organização dos Poderes.
- Poderes do Estado e respectivas funções.
- Organização dos Poderes: Poder Legislativo, Poder Executivo e Poder Judiciário.
- Os poderes do Estado e as respectivas funções.
- Organização dos Poderes: Poder Legislativo e Poder Executivo.
- Mecanismos de freios e contrapesos.
- Poderes Executivo, Legislativo e Judiciário.
- Poderes Executivo, Legislativo e Judiciário, processo legislativo e fiscalização.
- Poderes do Estado: executivo, legislativo e judiciário.
- Organização dos Poderes e mecanismos de freios e contrapesos.
- Separação de Poderes.
- Divisão e coordenação de Poderes da República.
- Organização dos Poderes: mecanismos de freios e contrapesos.
- Organização dos Poderes e funções essenciais à Justiça.
- Poder e divisão de poderes.
- Poderes da União.
- Poder executivo, legislativo e Judiciário.
- Teoria Geral do Estado e poderes do Estado.
- Organização e separação dos Poderes.
- Organização dos Poderes, freios e contrapesos e funções essenciais à Justiça.
- Separação Poderes.
- Mecanismo de freios e contrapesos.
- A unidade do poder estatal e a separação de poderes.
- Estado Democrático de Direito e organização dos Poderes.
- Poderes Legislativo, Executivo e Judiciário na CF/88.
- Organização do Estado e organização dos Poderes.
- Divisão e coordenação dos Poderes da República.

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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito-constitucional.organizacao-poderes.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito-constitucional.organizacao-poderes",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false }, { "key": "B", "text": "...", "correct": true } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (A–E com 5 opções ou A–D com 4 opções) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
