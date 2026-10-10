Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Língua Portuguesa: Classes de palavras** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 5 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (CEBRASPE, 50 questões):** mais 50 itens de Certo/Errado no estilo Cebraspe, cobrindo outros pontos e casos que o lote 1.
- **Lote 3 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 4 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 5 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Reconhecimento contextual de substantivos, adjetivos, advérbios, preposições, conjunções e interjeições; flexão, locuções, valores semânticos e modalização.
Fica de fora (outras matérias tratam): Estudo central de pronomes, artigos, numerais e verbos, bem como análise sintática aprofundada.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Emprego das classes de palavras.
- Domínio da estrutura morfossintática do período: Emprego das classes de palavras.
- Flexão nominal e flexão verbal.
- Flexão nominal e verbal.
- Classes de palavras; os aspectos morfológicos, sintáticos, semânticos e textuais de substantivos, adjetivos, artigos, numerais, pronomes, verbos, advérbios, conjunções e interjeições; os modalizadores.
- Classes de palavras.
- Estrutura morfossintática do período e emprego das classes de palavras.
- Os aspectos morfológicos, sintáticos, semânticos e textuais de substantivos, adjetivos, artigos, numerais, pronomes, verbos, advérbios, conjunções e interjeições.
- Classes de palavras e seus aspectos morfológicos, sintáticos, semânticos e textuais.
- Morfologia: reconhecimento, emprego e sentido das classes gramaticais.
- Morfologia: elementos mórficos, formação de palavras, classes de palavras e flexão nominal e verbal.
- Funções das classes de palavras.
- Função textual dos vocábulos.
- Flexão nominal.
- Morfologia.
- Morfologia: classes de palavras, flexões, estrutura e formação de palavras e vozes verbais.
- Mecanismos de flexão dos nomes e verbos.
- Classes de palavras e relações de coordenação e subordinação.
- Classes de palavras, coordenação e subordinação.
- Classe e emprego de palavras.
- Domínio da estrutura morfossintática do período e emprego das classes de palavras.
- Classes de palavras e aspectos morfológicos, sintáticos, semânticos e textuais.
- Características básicas da textualidade e estruturas linguísticas na construção de mensagens, com destaque para as classes de palavras.
- Classificação e flexão das palavras, tempos e modos verbais.
- Classes de palavras variáveis e invariáveis e seus empregos no texto.
- Morfologia: classes de palavras variáveis e invariáveis e seus empregos no texto.
- Emprego e diferenciação das classes de palavras: substantivo, adjetivo, numeral, pronome, artigo, verbo, advérbio, preposição e conjunção.
- Flexão de substantivos e adjetivos (gênero e número).
- Emprego das classes de palavras e funções sintáticas.
- Classificação gramatical.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Regra precisa, com exemplos concretos; nunca invente regra ou dado.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo portugues.classes-de-palavras.banco-N.json, onde N é o lote)
```json
{
  "materia": "portugues.classes-de-palavras",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
