Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Português: História da língua, níveis e funções da linguagem** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Formação histórica do português e contato linguístico; língua, linguagem, fala e norma; variações e registros; elementos da comunicação; seis funções da linguagem; noções de discurso relatado e intertextualidade na leitura funcional de textos.
Fica de fora (outras matérias tratam): Morfologia e sintaxe detalhadas, redação oficial, regras ortográficas pormenorizadas e análise aprofundada de gêneros textuais.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Funções da linguagem.
- Elementos dos atos de comunicação.
- Os diversos níveis de linguagem.
- As funções da linguagem.
- Registros de linguagem.
- As estruturas linguísticas no processo de construção de mensagens adequadas.
- Linguagem e comunicação: situação comunicativa e variações linguísticas.
- Níveis de linguagem.
- As estruturas linguísticas no processo de construção de mensagens adequadas: Os diversos níveis de linguagem.
- As estruturas linguísticas no processo de construção de mensagens adequadas: As funções da linguagem.
- Discurso, registros, funções da linguagem e atos de comunicação.
- Funções da linguagem: Elementos dos atos de comunicação.
- Tipos de discurso, registros e funções da linguagem e elementos dos atos de comunicação.
- Elementos da comunicação e funções da linguagem.
- Elementos de comunicação.
- Comunicação, linguagem e variações linguísticas.
- Comunicação, linguagem, variações linguísticas, gêneros e tipologias textuais.
- Funções da linguagem e elementos dos atos de comunicação.
- Registros e funções da linguagem.
- Registros e funções da linguagem e elementos dos atos de comunicação.
- Tipos de discurso, registros e funções da linguagem.
- Tipos de discurso, registros e funções da linguagem e elementos da comunicação.
- Registros e funções da linguagem e atos de comunicação.
- Registros, funções da linguagem, discurso e comunicação.
- Registros, funções da linguagem e elementos dos atos de comunicação.
- Ordem direta e inversa, tipos de discurso, registros e funções da linguagem.
- Discurso, registros, funções da linguagem e elementos da comunicação.
- Teoria da linguagem: história da língua, níveis de linguagem e funções da linguagem.
- Níveis de linguagem, funções da linguagem e intertextualidade.
- Pragmática, significado contextual, níveis e funções da linguagem e intertextualidade.

## Cada lote de 50
- 38 de múltipla escolha A a E (5 alternativas) e 12 de Certo/Errado. (O app transforma A–E em A–D tirando uma errada, por isso cada alternativa tem comentário próprio.)
- Dificuldade: 15 fáceis, 20 médias, 15 difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, comparação entre conceitos parecidos. Use casos DIFERENTES entre si; nada de repetir o mesmo caso com outras palavras.

## Regras de qualidade
1. Regra precisa, com exemplos concretos; nunca invente regra ou dado.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo portugues.funcoes-linguagem-teoria.banco-N.json, onde N é o lote)
```json
{
  "materia": "portugues.funcoes-linguagem-teoria",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
