Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Matemática e Raciocínio Lógico: Razão e proporção** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Razões entre grandezas, taxas unitárias, razões equivalentes, proporções, propriedade fundamental, divisão proporcional e reconhecimento de proporcionalidade direta e inversa.
Fica de fora (outras matérias tratam): Regra de três como procedimento geral, porcentagens, semelhança geométrica e matemática financeira.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Proporcionalidade direta e inversa.
- Razões e proporções.
- Razão e proporção.
- Razões e proporções: Divisão proporcional.
- Números e grandezas proporcionais: razões e proporções.
- Divisão em partes proporcionais.
- Divisão proporcional.
- Proporcionalidades.
- Proporções e divisão proporcional.
- Razões e proporções; divisão proporcional.
- Conjuntos numéricos, sistema legal de medidas, razões, proporções, divisão proporcional e regras de três.
- Razões, proporções e divisão em partes proporcionais.
- Razões e proporções e divisão em partes proporcionais.
- Proporcionalidade: razões e proporções; problemas.
- Divisão em partes diretamente e inversamente proporcionais.
- Proporcionalidade direta e inversa e medidas de comprimento, área, volume, massa e tempo.
- Proporcionalidade, regras de três e divisão de grandezas em partes proporcionais.
- Números racionais; razão, proporção e grandezas proporcionais.
- Razões, proporções, porcentagens, juros e proporcionalidade direta e inversa.
- Proporções.
- Variação de grandezas: razão e proporção.
- Taxas de variação de grandezas: razão e proporção com aplicações.
- Proporcionalidade: grandezas diretamente proporcionais, grandezas inversamente proporcionais, regra de três simples e composta, gráficos e tabelas.
- Geometria: razão entre comprimentos.
- Proporção.
- Variação de grandezas.
- Sistema legal de medidas, razões, proporções e grandezas proporcionais.
- Números e grandezas proporcionais: razões e proporções e divisão em partes proporcionais.

## Cada lote de 50
- 25 de múltipla escolha A a E (5 alternativas), 13 de múltipla escolha A a D (4 alternativas), 12 de Certo/Errado.
- Dificuldade: 15 fáceis, 20 médias, 15 difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, cálculo com todos os dados no enunciado, interpretação de tabela ou gráfico, comparação entre conceitos parecidos. Use casos DIFERENTES entre si; nada de repetir o mesmo caso com outras palavras.

## Regras de qualidade
1. Regra precisa, com exemplos concretos; nunca invente regra ou dado.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Explicação (mínimo 120 caracteres): por que a certa está certa e por que CADA errada erra, com o conceito específico. Em Certo/Errado começa com "Certo." ou "Errado.". Escreva cada explicação de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo matematica.razao-proporcao.banco-N.json, onde N é o lote)
```json
{
  "materia": "matematica.razao-proporcao",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false }, { "key": "B", "text": "...", "correct": true } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (A–E com 5 opções ou A–D com 4 opções) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
