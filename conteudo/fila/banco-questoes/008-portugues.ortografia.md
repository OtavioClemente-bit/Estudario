Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Língua Portuguesa: Ortografia oficial: emprego das letras** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Emprego de s, z, ss, ç, c, sc, sç, xc, x, ch, g, j, e, i, o, u e h; sufixos e famílias lexicais; parônimos e homônimos; maiúsculas e minúsculas segundo o Acordo Ortográfico.
Fica de fora (outras matérias tratam): Acentuação gráfica, hífen e pontuação como temas autônomos, exceto referências indispensáveis para reconhecer a grafia e o sentido dos vocábulos.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Domínio da ortografia oficial.
- Ortografia oficial.
- Domínio da ortografia oficial: emprego das letras.
- Ortografia.
- Emprego das letras.
- Ortografia e acentuação gráfica.
- Ortografia e acentuação.
- Ortografia oficial, emprego das letras e acentuação gráfica.
- Ortografia: emprego de letras, hífen e acentuação gráfica.
- Novo Acordo Ortográfico.
- Domínio da ortografia.
- Ortografia (conforme Novo Acordo vigente).
- Homônimos e parônimos.
- Ortografia: emprego de letras, do hífen e acentuação gráfica conforme sistema oficial vigente, inclusive Acordo Ortográfico vigente, conforme Decreto 6.583/2012.
- Língua Portuguesa Ortografia e acentuação.
- Ortografia oficial, acentuação gráfica e emprego do hífen.
- Ortografia oficial e mecanismos de coesão textual.
- Domínio da ortografia oficial: Emprego das letras;
- Ortografia, acentuação gráfica e notações léxicas; Acordo Ortográfico.
- Ortografia oficial e coesão textual.
- Domínio da ortografia oficial e dos mecanismos de coesão textual.
- Ortografia: emprego de letras, hífen e acentuação gráfica conforme o sistema oficial vigente e o Acordo Ortográfico.
- Sistema gráfico: ortografia.
- Ortografia oficial e regras atualizadas segundo a Reforma Ortográfica da Língua Portuguesa.
- Ortografia: emprego das letras.
- Sistema ortográfico: ortografia oficial do português do Brasil.
- Ortografia oficial, emprego de letras, acentuação, hífen, divisão silábica e relações entre sons e letras.
- Ortografia: acordo ortográfico, maiúsculas e minúsculas, acentuação e hífen.
- Ortografia: acordo ortográfico, uso de letras maiúsculas e minúsculas, acentuação gráfica e emprego do hífen.
- Ortografia: acordo ortográfico, uso de maiúsculas e minúsculas, acentuação gráfica e emprego do hífen.

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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo portugues.ortografia.banco-N.json, onde N é o lote)
```json
{
  "materia": "portugues.ortografia",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false }, { "key": "B", "text": "...", "correct": true } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (A–E com 5 opções ou A–D com 4 opções) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
