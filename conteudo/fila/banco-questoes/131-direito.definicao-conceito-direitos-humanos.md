Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Noções de Direitos Humanos e Legislação: Definição e conceito de direitos humanos** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Conceito, fundamento na dignidade humana, titularidade, características, princípios, dimensões didáticas, reconhecimento internacional e proteção constitucional brasileira dos direitos humanos.
Fica de fora (outras matérias tratam): Estudo detalhado de tratados temáticos, sistemas internacionais de petições, regras processuais de tribunais e direitos de grupos específicos, salvo referências breves necessárias à compreensão.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Teoria geral dos Direitos Humanos.
- Direitos Humanos e Direitos Fundamentais.
- Teoria geral dos direitos fundamentais.
- Os fundamentos filosóficos dos Direitos Humanos.
- Fontes, classificação, princípios, características e gerações de direitos humanos.
- Direitos humanos.
- Teoria geral dos direitos humanos: Conceitos, terminologia, estrutura normativa, fundamentação.
- Conceito, terminologia, estrutura normativa, fundamentação.
- Conceito e fundamentação.
- Direitos Humanos na CRFB/88.
- Teoria geral dos direitos humanos: evolução histórica, conceito, classificações e características.
- Evolução histórica, conceito, classificações e características dos direitos humanos.
- Teoria geral dos Direitos Humanos: Direitos humanos e direitos fundamentais.
- Teoria geral dos Direitos Humanos: Gerações ou dimensões dos direitos fundamentais.
- Fontes dos Direitos Humanos.
- Teoria geral dos direitos humanos: conceito, terminologia, eficácia vertical e horizontal e características.
- Gerações de direitos humanos.
- Direitos humanos no ordenamento brasileiro e na Constituição Federal de 1988.
- Conceitos, terminologia, estrutura normativa, fundamentação.
- Direitos humanos: conceito.
- Direitos humanos: abrangência.
- Direitos humanos, direitos fundamentais e dimensões dos direitos fundamentais.
- Tratados internacionais no Brasil, natureza jurídica, incorporação e controles de constitucionalidade e convencionalidade.
- Teoria geral, conceitos, terminologia, eficácia, características, gerações e afirmação histórica dos direitos humanos.
- Direitos humanos e responsabilidade do Estado, no ordenamento brasileiro e na Constituição.
- Direitos humanos, discriminação e exclusão social, inclusive situação de grupos vulneráveis.
- Direitos humanos, discriminação e exclusão social de grupos minoritários e vulneráveis.
- Direitos Humanos: Conceito, princípios essenciais e gerações de direitos humanos.
- Definição e conceito de direitos humanos.
- Teoria geral dos direitos humanos: conceitos, terminologia, estrutura normativa e fundamentação.

## Cada lote de 50
- 38 de múltipla escolha A a E (5 alternativas) e 12 de Certo/Errado. (O app transforma A–E em A–D tirando uma errada, por isso cada alternativa tem comentário próprio.)
- Dificuldade: 15 fáceis, 20 médias, 15 difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, literalidade da lei com o detalhe que a prova troca, jurisprudência e súmulas, comparação entre conceitos parecidos. Use casos DIFERENTES entre si; nada de repetir o mesmo caso com outras palavras.

## Regras de qualidade
1. Cite artigo, lei, súmula e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Se não tiver certeza de um número, explique sem ele — nunca invente.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito.definicao-conceito-direitos-humanos.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito.definicao-conceito-direitos-humanos",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
