Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Matemática e Raciocínio Lógico: Regra de três simples e composta** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Proporcionalidade direta e inversa; regra de três simples; regra de três composta com três ou mais grandezas; análise da relação entre cada grandeza e a incógnita; aplicações em compras, consumo, escalas de trabalho, produção, transporte, estoques e obras, com unidades compatíveis.
Fica de fora (outras matérias tratam): Porcentagens sucessivas, juros, escalas cartográficas em profundidade, semelhança geométrica, velocidade com variação não uniforme, produtividade variável, taxas com parcelas fixas e situações em que não há proporcionalidade. Esses temas só aparecem como contexto quando a hipótese de proporcionalidade é explicitada.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Regra de três.
- Razões e proporções: Regras de três simples e compostas.
- Regras de três simples e composta.
- Regra de três simples e composta.
- Regra de três simples.
- Proporcionalidade direta e inversa.
- Regra de três composta.
- Regra de 3 simples e composta.
- Regra de três simples e composta, proporcionalidades e porcentagens.
- Regras de três simples e compostas.
- Regra de três (simples e composta).
- Regra de três, proporcionalidade e porcentagens.
- Regra de três simples e composta, proporcionalidade e porcentagens.
- Proporcionalidade e medidas de comprimento, área, volume, massa e tempo.
- Razões, proporções, regras de três simples e composta.
- Razão, proporção, variação de grandezas e regras de três simples e composta.
- Regra de três simples e composta e porcentagens.
- Divisão proporcional, regra de sociedade e regra de três.
- Regra de três simples e composta; porcentagens.
- Regra de três simples e composta e proporcionalidade.
- Divisão proporcional: Regras de três simples e composta.
- Matemática financeira: regra de três simples e composta.
- Regra de três simples e composta, porcentagem, escalas e frações.

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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo matematica.regra-de-tres.banco-N.json, onde N é o lote)
```json
{
  "materia": "matematica.regra-de-tres",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false }, { "key": "B", "text": "...", "correct": true } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (A–E com 5 opções ou A–D com 4 opções) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
