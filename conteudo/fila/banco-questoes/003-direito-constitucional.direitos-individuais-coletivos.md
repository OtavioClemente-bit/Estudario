Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Constitucional: Direitos e deveres individuais e coletivos (art. 5º)** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Art. 5º da Constituição: fundamentos, destinatários, direitos à vida, igualdade, liberdades, privacidade, propriedade, garantias penais e processuais, proteção de dados, características dos direitos fundamentais e visão geral dos remédios constitucionais.
Fica de fora (outras matérias tratam): Aprofundamento específico dos direitos sociais, nacionalidade, direitos políticos, partidos políticos, organização dos Poderes, direitos coletivos previstos em outros artigos e disciplina processual exaustiva das ações constitucionais.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Direitos e garantias fundamentais.
- Direitos e garantias fundamentais: Direitos e deveres individuais e coletivos.
- Direitos e deveres individuais e coletivos, direitos sociais, direitos de nacionalidade, direitos políticos, partidos políticos.
- Dos direitos e garantias fundamentais: dos direitos e deveres individuais e coletivos; dos direitos sociais; dos direitos de nacionalidade; dos direitos políticos.
- Garantias constitucionais individuais.
- Dos direitos e garantias fundamentais: dos direitos e deveres individuais e coletivos.
- Direito à vida, à liberdade, à igualdade, à segurança e à propriedade.
- Tratados internacionais de Direitos Humanos em face da Constituição da República Federativa do Brasil de 1988.
- Disposições constitucionais aplicáveis ao direito penal.
- Garantias dos direitos coletivos, sociais e políticos.
- Direitos e deveres individuais e coletivos, direitos sociais, nacionalidade, cidadania, direitos políticos, partidos políticos.
- Direitos e garantias fundamentais: direitos e deveres individuais e coletivos, direitos sociais, nacionalidade, direitos políticos.
- Direitos e garantias fundamentais, direitos sociais, nacionalidade, direitos políticos e partidos políticos.
- Direitos Humanos e Direitos Fundamentais na Constituição Federal de 1988 (arts. 5º ao 15).
- Constituição Federal de 1988: direitos e garantias fundamentais, artigos 5º ao 17.
- Constituição da República Federativa do Brasil de 1988: Direitos e garantias fundamentais.
- Direitos e garantias fundamentais: Dos Direitos e Deveres Individuais e Coletivos.
- Direitos e garantias fundamentais, direitos sociais, nacionalidade e direitos políticos.
- Direitos e deveres fundamentais.
- Direitos e garantias fundamentais: direitos e deveres individuais e coletivos, direitos sociais, direitos de nacionalidade, direitos políticos, partidos políticos.
- Teoria geral dos direitos fundamentais.
- Direito de propriedade.
- Direitos e garantias individuais e coletivos.
- Princípios, direitos e garantias fundamentais na Constituição Federal.
- Direitos e garantias fundamentais: direitos individuais e coletivos, sociais, de nacionalidade e políticos; partidos políticos.
- Direitos e garantias fundamentais, direitos individuais e coletivos, direitos sociais, nacionalidade, direitos políticos e partidos políticos.
- Direitos e garantias fundamentais, direitos individuais e coletivos, sociais, de nacionalidade e políticos e partidos políticos.
- Remédios do direito constitucional.
- Fundamentos constitucionais dos direitos e deveres fundamentais.
- Direitos e garantias fundamentais: direitos e deveres individuais e coletivos; direito à vida, à liberdade, à igualdade, à segurança e à propriedade.

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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito-constitucional.direitos-individuais-coletivos.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito-constitucional.direitos-individuais-coletivos",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false }, { "key": "B", "text": "...", "correct": true } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (A–E com 5 opções ou A–D com 4 opções) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
