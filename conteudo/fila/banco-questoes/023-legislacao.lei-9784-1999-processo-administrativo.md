Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Legislação: Processo administrativo federal (Lei 9.784/1999)** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Princípios e critérios; conceito, fases, instauração e modalidades; direitos e deveres; interessados; competência, delegação e avocação; impedimento e suspeição; atos, intimação, provas e instrução; prazos; decisão, motivação e decisão coordenada; recursos e revisão; anulação, revogação, convalidação, decadência e prioridade na tramitação, conforme a Lei nº 9.784/1999.
Fica de fora (outras matérias tratam): Processo administrativo disciplinar com disciplina legal específica; processos administrativos estaduais e municipais; controle judicial de atos administrativos; regime especial substantivo de licitações.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Processo administrativo.
- Lei n. 9.784/1999.
- Processo administrativo (Lei n. 9.784/1999).
- Processo administrativo: conceito, princípios, fases e modalidades.
- Processo administrativo federal.
- Lei nº 9.784/1999.
- Lei nº 9.784/1999: processo administrativo federal.
- Processo administrativo: Lei nº 9.784/1999.
- Competência administrativa: conceito e critérios de distribuição.
- Avocação e delegação de competência.
- Lei nº 9.784/1999: processo administrativo.
- Processo administrativo e Lei nº 9.784/1999.
- Lei nº 9.784/1999 (processo administrativo federal).
- Lei nº 9.784/1999 e alterações.
- Lei nº 9.784/1999: processo administrativo no âmbito da Administração Pública Federal.
- Processo administrativo federal: Lei nº 9.784/1999.
- Lei Federal nº 9.784/1999: processo administrativo federal.
- Lei nº 9.784/1999 e suas alterações.
- Atos administrativos: processo administrativo.
- Lei nº 9.784/1999 (processo administrativo).
- Lei nº 9.784/1999 e suas alterações (processo administrativo).
- Processo administrativo no âmbito da Administração Pública Federal (Lei nº 9.784/1999 e suas alterações).
- Recursos administrativos.
- Direito administrativo disciplinar: processo Administrativo Federal (Lei nº 9.784/1999 e suas alterações).
- Lei nº 9.784/99 (regula o processo administrativo no âmbito da Administração Pública Federal, aplicável ao Distrito Federal por força da Lei Distrital nº 2834/2001).
- Processo administrativo federal (Lei n.º 9.784/1999).
- Lei nº 9.784/1999 e alterações: processo administrativo.
- Processos administrativos.
- Legislação pertinente: Lei nº 9.784/1999.
- Procedimento administrativo.

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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo legislacao.lei-9784-1999-processo-administrativo.banco-N.json, onde N é o lote)
```json
{
  "materia": "legislacao.lei-9784-1999-processo-administrativo",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false }, { "key": "B", "text": "...", "correct": true } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (A–E com 5 opções ou A–D com 4 opções) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
