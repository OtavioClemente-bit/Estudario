Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Matemática e Raciocínio Lógico: Números racionais: frações e decimais** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Conjuntos N, Z, Q, I e R; comparação e ordenação; equivalência, simplificação e operações com frações; decimais exatos e dízimas periódicas; fração geratriz; potências e raízes de racionais; expressões numéricas e problemas com frações de um todo.
Fica de fora (outras matérias tratam): Equações e inequações avançadas, números complexos, logaritmos, porcentagens compostas como tópico autônomo e operações algébricas com polinômios.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Números inteiros, racionais e reais e suas operações, porcentagem e juros.
- Números inteiros e racionais: operações (adição, subtração, multiplicação, divisão, potenciação).
- Expressões numéricas.
- Frações e operações com frações.
- Conjuntos numéricos: números inteiros, racionais e reais.
- Números inteiros, racionais e reais e suas operações.
- Números inteiros, racionais e reais e suas operações, porcentagem.
- Conjuntos numéricos.
- Números inteiros, racionais e reais; problemas de contagem.
- Números inteiros, racionais e reais.
- Conjunto numérico: operações com números inteiros, fracionários e decimais.
- Campos Numéricos (números naturais, inteiros e racionais).
- Números inteiros: operações e propriedades.
- Números naturais e inteiros: operações e relação de ordem.
- Números reais: operações (adição, subtração, multiplicação, divisão, radiciação e potenciação); problemas.
- Números inteiros, racionais e reais, porcentagem e juros.
- Números inteiros, racionais e reais e operações.
- Números inteiros, racionais e reais e problemas de contagem.
- Operações com números inteiros, fracionários e decimais.
- Números inteiros, racionais e reais e operações numéricas.
- Aritmética e álgebra: operações com números inteiros, fracionários e decimais.
- Números inteiros e números racionais em representação fracionária e decimal: operações e propriedades.
- Aritmética: operações com números racionais.
- Conjuntos numéricos, intervalos e operações.
- Números racionais, representação fracionária e decimal: operações e propriedades.
- Números reais e suas operações.
- Números racionais, representação fracionária e decimal, operações e propriedades.
- Números inteiros, racionais e reais e suas operações e porcentagem.
- Operações e representações com números racionais.
- Operações com irracionais e aproximações por racionais.

## Cada lote de 50
- 38 de múltipla escolha A a E (5 alternativas) e 12 de Certo/Errado. (O app transforma A–E em A–D tirando uma errada, por isso cada alternativa tem comentário próprio.)
- Dificuldade: 15 fáceis, 20 médias, 15 difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, cálculo com todos os dados no enunciado, interpretação de tabela ou gráfico, comparação entre conceitos parecidos. Use casos DIFERENTES entre si; nada de repetir o mesmo caso com outras palavras.

## Regras de qualidade
1. Regra precisa, com exemplos concretos; nunca invente regra ou dado.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo matematica.numeros-racionais.banco-N.json, onde N é o lote)
```json
{
  "materia": "matematica.numeros-racionais",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
