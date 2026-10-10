Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Língua Inglesa: Interpretação de texto em inglês** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Leitura de textos curtos em inglês; ideia central, dados explícitos, inferência, predição, propósito, tom, vocabulário em contexto, falsos cognatos, tempos verbais, modais, condicionais, conectivos, coesão e relações intratextuais e intertextuais.
Fica de fora (outras matérias tratam): Tradução integral de textos longos, fonética avançada, produção de redação e terminologia técnica especializada.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Compreensão de texto escrito em língua inglesa.
- Compreensão de textos em língua inglesa e itens gramaticais relevantes para o entendimento dos sentidos dos textos.
- Compreensão de textos variados: domínio do vocabulário e da estrutura da língua, ideias principais e secundárias, explícitas e implícitas, relações intratextuais e intertextuais.
- Compreensão de textos em Língua Inglesa.
- Compreensão de textos escritos em língua inglesa.
- Uso de palavras mais frequentes, sinonímia e antonímia.
- Reconhecimento de informações específicas.
- Capacidade de análise e síntese.
- Reconhecimento de organização semântica e discursiva.
- Estratégias de leitura: compreensão geral, reconhecimento de informações específicas, análise, síntese, inferência e predição.
- Organização semântica e discursiva, palavras frequentes, sinonímia, antonímia e funções retóricas; metáfora e metonímia.
- Palavras e expressões equivalentes.
- Inferência e predição.
- Compreensão de textos variados: vocabulário, estrutura, ideias principais e secundárias, explícitas e implícitas e relações intratextuais e intertextuais.
- Compreensão de textos, vocabulário, estrutura, ideias explícitas e implícitas e relações textuais.
- Conhecimento de um vocabulário fundamental para a compreensão de textos.
- Conhecimento de vocabulário fundamental para a compreensão de textos.
- Elementos de referência.
- Estratégias de leitura em língua inglesa: compreensão geral de texto.
- Inglês Estratégias de leitura em língua inglesa: compreensão geral de texto.
- Inglês técnico.
- Compreensão de textos em inglês: ideias principais e secundárias, explícitas e implícitas e relações intratextuais e intertextuais.
- Compreensão de textos escritos em língua inglesa e itens gramaticais relevantes à compreensão semântica.
- Compreensão de textos em língua inglesa e itens gramaticais relevantes à compreensão semântica.
- Compreensão de textos variados, vocabulário, estrutura da língua e identificação de ideias explícitas, implícitas e relações textuais.
- Compreensão de textos variados, domínio do vocabulário e da estrutura da língua e identificação de ideias e relações textuais.
- Compreensão de textos variados, vocabulário, estrutura da língua e ideias e relações textuais.
- Compreensão de textos variados, vocabulário, estrutura da língua e ideias explícitas e implícitas.
- Compreensão de textos variados, vocabulário, estrutura da língua e relações textuais.
- Compreensão de textos variados.

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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo lingua-inglesa.interpretacao-texto-ingles.banco-N.json, onde N é o lote)
```json
{
  "materia": "lingua-inglesa.interpretacao-texto-ingles",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
