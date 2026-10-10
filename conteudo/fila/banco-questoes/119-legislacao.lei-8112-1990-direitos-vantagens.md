Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Legislação: Lei 8.112/1990: direitos e vantagens** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Direitos e vantagens previstos no Título III da Lei 8.112/1990: vencimento e remuneração; indenizações, gratificações e adicionais; férias; licenças; afastamentos; concessões; tempo de serviço; direito de petição.
Fica de fora (outras matérias tratam): Provimento, vacância, deveres, proibições, responsabilização disciplinar, processo administrativo e benefícios previdenciários, tratados em tópicos próprios, salvo referência indispensável à compreensão de dispositivo incluído no escopo.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Dos Direitos e Vantagens: Do Vencimento e da Remuneração, Das Vantagens, Das Férias, Das Licenças e Dos Afastamentos;
- Remuneração.
- Direitos e vantagens.
- Lei nº 8.112/1990 e alterações: direitos e vantagens.
- Dos direitos e vantagens: do vencimento e da remuneração, das vantagens, das férias, das licenças e dos afastamentos.
- Vencimento e remuneração, vantagens, férias, licenças, afastamentos, direito de petição.
- Agentes públicos: Remuneração.
- Disposições doutrinárias: Remuneração.
- Direito de Petição.
- Dos Servidores Públicos – Lei n.º 8.112/90: do direito de petição.
- Direitos e vantagens e regime disciplinar dos servidores públicos federais.
- Regime jurídico dos servidores públicos civis da União (Lei nº 8.112/1990 e alterações): direitos e vantagens.
- Direitos e deveres dos servidores estatutários.
- Lei nº 8.112/1990: direitos e vantagens, vencimento e remuneração, vantagens, férias, licenças, afastamentos.
- Lei nº 8.112/1990: direito de petição.
- Direitos e deveres dos agentes públicos.
- Regime jurídico dos servidores públicos federais: vencimento básico.
- Regime jurídico dos servidores públicos federais: licença.
- Efetividade, estabilidade, vitaliciedade, remuneração, direitos, deveres e responsabilidade dos agentes públicos.
- Vencimento, remuneração, vantagens, férias, licenças, afastamentos e direito de petição.
- Remuneração, direitos, deveres e responsabilidade dos agentes públicos.
- Lei nº 8.112/1990: direitos e vantagens; regime disciplinar.
- Licenças e aposentadoria de servidores públicos federais.
- Direitos e vantagens dos servidores públicos.
- Lei nº 8.112/1990: férias, licenças, vantagens, jornada, progressão, promoção, remuneração e auxílios.
- Férias, licenças, vantagens, jornada de trabalho, remuneração, auxílios, progressão e promoção dos servidores.

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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo legislacao.lei-8112-1990-direitos-vantagens.banco-N.json, onde N é o lote)
```json
{
  "materia": "legislacao.lei-8112-1990-direitos-vantagens",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false }, { "key": "B", "text": "...", "correct": true } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (A–E com 5 opções ou A–D com 4 opções) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
