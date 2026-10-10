Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Administrativo: Contratação direta: dispensa e inexigibilidade** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Dever de licitar; inexigibilidade do art. 74; dispensa do art. 75 e limites monetários atualizados; licitação dispensada nas hipóteses de alienação do art. 76; instrução e publicidade do processo de contratação direta do art. 72.
Fica de fora (outras matérias tratam): Modalidades e princípios licitatórios, fases, recursos, execução contratual, sanções e crimes, tratados em tópicos próprios.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Licitação: Contratação direta: dispensa e inexigibilidade.
- Contratação direta: Dispensa e inexigibilidade.
- Obrigatoriedade, dispensa e inexigibilidade.
- Licitação: obrigatoriedade, dispensa, inexigibilidade e vedação.
- Obrigatoriedade, dispensa, inexigibilidade e vedação.
- Licitação: dispensa e inexigibilidade.
- Licitação: Contratação direta.
- Dispensa e inexigibilidade.
- Dispensa e inexigibilidade de licitação.
- Licitação e contratos administrativos: Contratação direta: dispensa e inexigibilidade.
- Disposições doutrinárias: Contratação direta.
- Fracionamento de despesas.
- Lei nº 14.133/2021: contratação direta, dispensa e inexigibilidade.
- Licitações, contratação direta, dispensa, inexigibilidade e contratos administrativos.
- Dispensa de licitação.
- Inexigibilidade de licitação.
- Disposições doutrinárias: Contratação direta: dispensa e inexigibilidade.
- Licitação de obras públicas: Hipóteses de dispensa, de inexigibilidade e de vedação.
- Obrigatoriedade da licitação e contratação direta.
- Dispensa e inexigibilidade de licitação: hipóteses legais e requisitos de instrução processual.
- Hipóteses de dispensa, de inexigibilidade e de vedação.
- Dispensa e inexigibilidade. Anulação e revogação. Controle. Aspectos penais.
- Obrigatoriedade, dispensa e inexigibilidade de licitação.
- Inexigibilidade, dispensa, inexequibilidade, superfaturamento, desclassificação e alienações.
- Da contratação direta.
- Contratação direta.
- Contratação direta, inexigibilidade, dispensa e procedimentos auxiliares.
- Licitações: Contratação direta.

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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito-administrativo.contratacao-direta.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito-administrativo.contratacao-direta",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false }, { "key": "B", "text": "...", "correct": true } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (A–E com 5 opções ou A–D com 4 opções) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
