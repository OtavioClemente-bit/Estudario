Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Língua Portuguesa: Classes de palavras** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo portugues.classes-de-palavras.banco-N.json, onde N é o lote)
```json
{
  "materia": "portugues.classes-de-palavras",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false }, { "key": "B", "text": "...", "correct": true } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (A–E com 5 opções ou A–D com 4 opções) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
