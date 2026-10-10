Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Matemática: Probabilidade** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Experimentos aleatórios, espaço amostral, eventos, axiomas e propriedades, probabilidade clássica e empírica, complemento, união, interseção, diagramas de Venn, probabilidade condicional, independência, contagem, com e sem reposição, probabilidade total e regra de Bayes.
Fica de fora (outras matérias tratam): Variáveis aleatórias contínuas, distribuições inferenciais, testes estatísticos e estatística avançada.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Probabilidade.
- Problemas de contagem e noções de probabilidade.
- Probabilidade: Definições básicas e axiomas.
- Princípios de probabilidade.
- Noções de probabilidade: espaço amostral.
- Eventos, união, intersecção e complementar de eventos, probabilidade condicional e independência.
- Definições básicas e axiomas.
- Probabilidade, eventos dependentes e independentes, união, interseção e probabilidade condicional.
- Conceitos básicos de probabilidade.
- Probabilidades: definições básicas, axiomas e propriedades.
- Conceitos fundamentais de probabilidade.
- Noções de probabilidade: experimento aleatório, espaços amostrais finitos e equiprováveis e eventos aleatórios.
- Cálculo de probabilidades.
- Probabilidade condicional.
- Probabilidade: experimento aleatório, espaço amostral, evento.
- Espaços equiprováveis.
- Probabilidade de Laplace.
- Espaços não equiprováveis.
- Teorema do produto.
- Teorema de Bayes.
- Probabilidades: conceito e axiomas.
- Probabilidade, certeza, impossibilidade, fenômenos aleatórios, espaço amostral e evento.
- Probabilidade: definição e propriedades.
- Cálculo de probabilidade.
- Probabilidades.
- Probabilidade, axiomas, probabilidade condicional e independência.
- Probabilidade clássica, contagem, diagrama de Venn e probabilidade condicional.
- Conceitos de probabilidade.
- Modelo de probabilidade.
- Probabilidade: conceitos básicos, eventos independentes e mutuamente exclusivos.

## Em todos os lotes
- Dificuldade: 25% fáceis, 35% médias, 40% difíceis ("difficulty": FACIL, MEDIA, DIFICIL), seguindo estas definições:
  - **FACIL:** cobra a regra principal como está na lei ou no conceito; acerta quem leu a matéria uma vez.
  - **MEDIA:** aplica a regra a um caso concreto ou cobra um detalhe (prazo, competência, exceção); exige estudo atento.
  - **DIFICIL:** combina duas ou mais regras, cobra a exceção da exceção, jurisprudência que contraria a leitura literal, ou monta caso em que a resposta intuitiva está errada; erra quem estudou só o básico. Distratores das difíceis são quase certos (diferem num detalhe decisivo).
  - A dificuldade vem do raciocínio exigido, NUNCA do tamanho do texto: questão difícil pode ser curta, e enunciado longo não torna a questão difícil.
- Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo matematica.probabilidade.banco-N.json, onde N é o lote)
```json
{
  "materia": "matematica.probabilidade",
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
