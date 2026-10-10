Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Administrativo: Controle judicial** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Controle judicial da Administração; elementos, atributos, limites e revisão dos atos administrativos; processo administrativo federal da Lei nº 9.784/1999; responsabilidade civil extracontratual do Estado e de seus agentes; categorias de agentes públicos; cargos, empregos e funções; Lei nº 8.112/1990, direitos, deveres, proibições, provimento, vacância, remoção, redistribuição, substituição e processo disciplinar; crimes contra a Administração Pública.
Fica de fora (outras matérias tratam): Regimes próprios de servidores estaduais e municipais, crimes não relacionados à Administração, improbidade em regime completo e processo judicial civil ou penal, exceto quando necessários para delimitar os tópicos indicados.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Controle da administração pública: Controle judicial.
- Controle judicial.
- Controle jurisdicional da Administração Pública no direito brasileiro.
- Sistemas de controle jurisdicional da administração pública: Contencioso administrativo e sistema da jurisdição una.
- Sistemas de controle jurisdicional da Administração Pública.
- Controle judiciário da administração pública.
- Controle judicial dos atos administrativos.
- Controle e responsabilização da administração: controle judicial.
- Controle judicial da administração.
- Sistemas administrativos: sistema inglês, sistema francês e sistema adotado no Brasil.
- Discricionariedade administrativa e controle judicial.
- Controle jurisdicional e meios de controle jurisdicional.
- Controle jurisdicional da Administração Pública e da atividade financeira do Estado.
- Controle da administração pública: sistemas de controle jurisdicional da administração pública, contencioso administrativo e sistema da jurisdição una.
- Controle da administração pública: controle jurisdicional da administração pública no direito brasileiro.
- Controle administrativo e jurisdicional.
- Limites do controle jurisdicional.
- Iniciativa de promover a apreciação judicial.
- Contencioso administrativo e sistema da jurisdição una; controle jurisdicional no Direito brasileiro.
- Sistemas de controle jurisdicional da administração pública (contencioso administrativo e sistema da jurisdição una).
- Controle administrativo, controle judicial e Administração Pública em juízo.
- Controle judicial dos atos administrativos discricionários.
- Sistemas de controle jurisdicional da Administração Pública e controle jurisdicional no Direito brasileiro.
- Controle jurisdicional da Administração Pública e sistemas de jurisdição una e contencioso administrativo.
- O processo civil e o controle judicial dos atos administrativos.
- Contencioso administrativo e sistema da jurisdição una.
- Meios de controle judicial da administração pública.
- Controle de mérito e de legalidade dos atos administrativos.
- Controle jurisdicional: sistemas, inafastabilidade, inexigência de esgotamento da via administrativa, alcance, consequências. Administração em juízo.
- Os meios de controle judicial.

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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito-administrativo.controle-judicial.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito-administrativo.controle-judicial",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false }, { "key": "B", "text": "...", "correct": true } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (A–E com 5 opções ou A–D com 4 opções) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
