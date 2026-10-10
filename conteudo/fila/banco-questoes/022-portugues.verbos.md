Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Língua Portuguesa: Emprego e correlação de tempos e modos verbais** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Valores do indicativo, subjuntivo e imperativo; correlação temporal; formas nominais; conjugação de verbos irregulares; vozes ativa, passiva analítica e sintética; e locuções verbais.
Fica de fora (outras matérias tratam): Regência, concordância nominal, colocação pronominal, conjugação perifrástica em outras línguas e análise literária de valores estilísticos não relacionados ao emprego verbal.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Emprego de tempos e modos verbais.
- Domínio dos mecanismos de coesão textual: Emprego de tempos e modos verbais.
- Emprego/correlação de tempos e modos verbais.
- Correlação de tempos e modos verbais.
- Domínio dos mecanismos de coesão textual: emprego e correlação de tempos e modos verbais.
- Tempos e modos verbais.
- Emprego e correlação de tempos e modos verbais.
- Tempos e modos verbais e estrutura morfossintática do período.
- Domínio dos mecanismos de coesão textual: Emprego/correlação de tempos e modos verbais.
- Flexão verbal.
- Emprego de tempos e modos dos verbos na Língua Portuguesa.
- Locuções verbais (perífrases verbais).
- Tempos, modos e flexões verbais.
- Emprego de tempos e modos dos verbos em português.
- Emprego de tempos e modos verbais e estrutura morfossintática do período.
- Tempos e modos verbais, estrutura morfossintática e classes de palavras.
- Tempos, modos e vozes verbais.
- Emprego dos modos e tempos verbais; infinitivo; verbo haver.
- Classes de palavras: verbo.
- Emprego de certas formas e palavras: modos verbais, aspectos verbais, pronome relativo, conjunção, pronome de tratamento, pontuação, ortografia.
- Flexão verbal regular e irregular.
- Valores dos tempos, modos e vozes verbais e efeitos de sentido da ordem de expressões.
- Emprego dos tempos e modos verbais.
- Emprego de tempos e modos verbais e vozes do verbo.
- Textos da esfera literária — Análise linguística: Compreenda a articulação dinâmica entre os tempos verbais utilizados (pretérito, presente histórico) na fluência da descrição ou narração da trama.

## Cada lote de 50
- 25 de múltipla escolha A a E (5 alternativas), 13 de múltipla escolha A a D (4 alternativas), 12 de Certo/Errado.
- Dificuldade: 15 fáceis, 20 médias, 15 difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, comparação entre conceitos parecidos. Use casos DIFERENTES entre si; nada de repetir o mesmo caso com outras palavras.

## Regras de qualidade
1. Regra precisa, com exemplos concretos; nunca invente regra ou dado.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Explicação (mínimo 120 caracteres): por que a certa está certa e por que CADA errada erra, com o conceito específico. Em Certo/Errado começa com "Certo." ou "Errado.". Escreva cada explicação de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo portugues.verbos.banco-N.json, onde N é o lote)
```json
{
  "materia": "portugues.verbos",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false }, { "key": "B", "text": "...", "correct": true } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (A–E com 5 opções ou A–D com 4 opções) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
