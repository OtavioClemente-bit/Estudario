Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Matemática e Raciocínio Lógico: Regra de três simples e composta** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

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

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, cálculo com todos os dados no enunciado, interpretação de tabela ou gráfico, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Regra precisa, com exemplos concretos; nunca invente regra ou dado.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo matematica.regra-de-tres.banco-N.json, onde N é o lote)
```json
{
  "materia": "matematica.regra-de-tres",
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
