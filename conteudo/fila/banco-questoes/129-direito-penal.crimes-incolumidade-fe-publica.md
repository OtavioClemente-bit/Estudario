Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Penal: Crimes contra a incolumidade pública e a fé pública** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Noções sobre crimes de perigo comum, incêndio, explosão e outras condutas perigosas, falsificação de moeda, falsidade documental, uso de documento falso e proteção penal da fé pública.
Fica de fora (outras matérias tratam): Crimes contra a vida e o patrimônio em geral, delitos funcionais e legislação especial que não esteja relacionada ao recorte.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Crimes contra a fé pública.
- Crimes contra a incolumidade pública.
- Crimes contra a Fé Pública: falsidade de títulos e outros papéis públicos.
- Falsidade documental.
- Fraudes em certames de interesse público.
- Crimes contra a fé pública. Falsidade de títulos e outros papéis públicos; falsidade documental; fraudes em certames de interesse público.
- Crimes contra a fé-pública.
- Crimes em espécie previstos no Código Penal: Crimes contra a incolumidade pública.
- Crimes em espécie previstos no Código Penal: Crimes contra a paz pública.
- Crimes em espécie previstos no Código Penal: Crimes contra a fé pública.
- Dos crimes contra a fé pública: da falsidade documental.
- Dos crimes contra a incolumidade pública.
- Crime: crimes contra a incolumidade pública.
- Crime: crimes contra a fé pública.
- Crimes contra a incolumidade pública e a paz pública.
- Crimes contra a fé pública de interesse da Administração Pública.
- Penas: Crimes contra a incolumidade pública.
- Penas: Crimes contra a paz pública.
- Penas: Crimes contra a fé pública.
- Crimes contra a fé pública em detrimento do INSS.
- Parte Especial: crimes contra a fé pública.
- Crimes contra a fé pública: falsidade de títulos, papéis públicos e documentos.
- Crimes contra a fé pública: falsidade de títulos e papéis públicos, falsidade documental e fraudes em certames.
- Crimes em espécie do Código Penal: Crimes contra a incolumidade pública.
- Crimes em espécie do Código Penal: Crimes contra a paz pública.
- Crimes em espécie do Código Penal: Crimes contra a fé pública.
- Crimes contra o patrimônio, contra a incolumidade pública e contra a fé pública.
- Crimes contra a fé pública e falsidade documental.
- Dos crimes contra a fé pública e delitos das fraudes em certames de interesse público.
- Crimes contra a incolumidade pública. Crimes contra a paz pública.

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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito-penal.crimes-incolumidade-fe-publica.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito-penal.crimes-incolumidade-fe-publica",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false }, { "key": "B", "text": "...", "correct": true } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (A–E com 5 opções ou A–D com 4 opções) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
