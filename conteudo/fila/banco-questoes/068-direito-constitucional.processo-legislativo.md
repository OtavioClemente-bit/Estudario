Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Constitucional: Processo legislativo** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Espécies normativas do art. 59; emendas constitucionais e limites; iniciativa e emendas; tramitação, deliberação, sanção, veto, promulgação e publicação; medidas provisórias; leis delegadas; leis complementares e ordinárias; decretos legislativos, resoluções e técnica legislativa básica.
Fica de fora (outras matérias tratam): Organização e estatuto dos congressistas; competências institucionais detalhadas; controle financeiro e orçamentário aprofundado; processos estaduais e municipais além de referência indispensável.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Processo legislativo.
- Do Processo Legislativo.
- Poder legislativo: Processo legislativo.
- Do Poder Legislativo: do processo legislativo.
- Procedimento Legislativo: definição; tipos: normal ou ordinário, abreviado, sumário, sumaríssimo, especial, concentrado.
- Processo Legislativo: definição, natureza jurídica, princípios gerais.
- Noções básicas: anteprojeto, autógrafos, unicameralismo e bicameralismo, blocos parlamentares, comissões, correção de erro, deliberação, destaque, emendas, iniciativa, legislatura.
- Noções básicas: sanção, sessões legislativas, turnos, urgência, veto, votação, voto vencido em separado.
- Iniciativa do processo de elaboração das leis: concorrente, reservada ou exclusiva, vinculada, popular.
- Tramitação de proposições: projeto de lei ordinária; projeto de lei complementar; projeto de decreto legislativo, projeto de resolução, indicação, parecer, emenda, requerimentos.
- Processo legislativo federal: conceito, espécies normativas, modalidades, fases.
- Processo legislativo estadual, distrital e municipal: normas constitucionais federais aplicáveis.
- Poder Legislativo: Do Processo Legislativo.
- Espécies normativas.
- Processo legislativo estadual.
- Poder Legislativo, processo legislativo e fiscalização.
- Do Processo Legislativo, da fiscalização contábil, financeira e orçamentária.
- Processo Legislativo: fundamentos e garantias de independência, conceito, objetos, atos e procedimentos.
- Processo legislativo: fundamento e garantias de independência, conceito, objetos, atos e procedimentos.
- Processo legislativo: conceito, objeto, espécies de atos normativos e procedimentos.
- Iniciativa.
- Emendas.
- Votação, sanção, veto, promulgação e publicação da lei.
- Medida Provisória.
- Direito Constitucional Aplicado ao Processo Legislativo.
- Processo legislativo constitucional: iniciativa, discussão, votação, sanção, veto, promulgação e publicação.
- Processo Legislativo Estadual e Organização do Poder Legislativo.
- Iniciativa legislativa: iniciativa parlamentar, iniciativa do Governador do Estado, iniciativa do Poder Judiciário, iniciativa do Ministério Público, iniciativa da Defensoria Pública, iniciativa popular, iniciativa privativa, iniciativa compartilhada.
- Limites constitucionais à iniciativa legislativa estadual.
- Tramitação de proposições legislativas.

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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito-constitucional.processo-legislativo.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito-constitucional.processo-legislativo",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false }, { "key": "B", "text": "...", "correct": true } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (A–E com 5 opções ou A–D com 4 opções) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
