Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Noções de Direitos Humanos e Legislação: Declaração Universal dos Direitos Humanos** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Preâmbulo e arts. 1º a 30 da Declaração Universal dos Direitos Humanos: princípios, direitos civis e políticos, direitos econômicos, sociais e culturais, deveres e regras de interpretação.
Fica de fora (outras matérias tratam): Tratados posteriores, sistemas regionais de proteção, declarações distintas e análise de casos concretos de direito interno.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Declaração Universal dos Direitos Humanos.
- Declaração Universal dos Direitos Humanos e Princípios de Yogyakarta +10.
- Declaração Universal dos Direitos Humanos (Resolução 217-A (III) - da Assembleia Geral das Nações Unidas, 1948).
- A Declaração Universal dos Direitos do Homem (ONU).
- Declaração Universal dos Direitos Humanos e Agenda 2030 da ONU.
- Fontes dos Direitos Humanos: Declaração Universal dos Direitos Humanos.
- Direitos civis, políticos, econômicos e culturais.
- Declaração Universal dos Direitos Humanos de 1948.
- Resolução nº 217-A (III) 1948 - Declaração Universal dos Direitos Humanos.
- Declaração Universal dos Direitos Humanos, adotada pela Resolução nº 217 da Assembleia Geral da ONU.
- Direitos civis, políticos, econômicos, sociais e culturais.
- Instrumentos internacionais e Declaração Universal dos Direitos Humanos.
- Fontes, instrumentos internacionais e Declaração Universal dos Direitos Humanos.
- Declaração Universal dos Direitos Humanos e Código de Conduta para Funcionários Responsáveis pela Aplicação da Lei.
- Declaração Universal dos Direitos Humanos – adotada pela Assembleia Geral das Nações Unidas em 10 de dezembro de 1948.
- Tratados Internacionais de Proteção aos Direitos Humanos: Declaração Universal dos Direitos Humanos (1948).
- A Declaração Universal dos Direitos Humanos e o Direito Interno Brasileiro.
- Declaração Universal dos Direitos Humanos, proclamada pela Resolução nº 217A (III) da Assembleia Geral das Nações Unidas, de 10 de dezembro de 1948.
- Sistema Global de Proteção dos Direitos Humanos: Declaração Universal dos Direitos Humanos (1948).
- Declaração Universal dos Direitos Humanos, proclamada pela Assembleia Geral das Nações Unidas, de 10 de dezembro de 1948.
- Declaração Universal dos Direitos Humanos (1948).
- Declaração Universal dos Direitos Humanos (ONU).
- Declaração Universal.
- Fontes e instrumentos internacionais, Declaração Universal e Convenção Americana sobre Direitos Humanos.

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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito.declaracao-universal-direitos-humanos.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito.declaracao-universal-direitos-humanos",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false }, { "key": "B", "text": "...", "correct": true } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (A–E com 5 opções ou A–D com 4 opções) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
