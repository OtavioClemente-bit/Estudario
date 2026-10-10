Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Língua Portuguesa: Interpretação de textos** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 5 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (CEBRASPE, 50 questões):** mais 50 itens de Certo/Errado no estilo Cebraspe, cobrindo outros pontos e casos que o lote 1.
- **Lote 3 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 4 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 5 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Compreensão global e localizada, tema, ideia central, informação explícita, inferência, pressupostos, implícitos, tese, argumentos, vozes, coesão referencial, conectores, paráfrase e efeitos de sentido em textos verbais e multimodais.
Fica de fora (outras matérias tratam): Classificação sistemática de gêneros e tipos textuais, ortografia, pontuação e análise morfossintática, salvo quando uma forma linguística for necessária à interpretação do sentido.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Compreensão e interpretação de textos de gêneros variados.
- Compreensão e interpretação de textos.
- Compreensão e interpretação de textos: informações literais e inferências possíveis.
- Interpretação e Compreensão de texto.
- Interpretação e compreensão de texto e organização estrutural dos textos.
- Interpretação de texto: decodificação dos diversos tipos de mensagem.
- Compreensão de texto: observação dos processos que constroem os significados textuais.
- Relação do texto com seu contexto histórico.
- Intertextualidade.
- Compreensão e interpretação de textos de gêneros variados e reconhecimento de tipos e gêneros textuais.
- Ideias principais e secundárias e recursos de argumentação.
- Leitura e interpretação textual.
- Leitura, compreensão e interpretação de textos.
- Leitura, interpretação e relação entre as ideias de textos de gêneros diversos.
- Fato e opinião e intencionalidade discursiva.
- Implícitos, subentendidos e efeitos de sentido.
- Língua Portuguesa: Interpretação e Compreensão de texto.
- Compreensão e interpretação de texto.
- Identificação de informações explícitas e implícitas.
- Distinção entre fato e opinião.
- Interpretação com o auxílio de material gráfico diverso.
- Análise e interpretação de texto, compreensão global, ponto de vista do autor, ideias centrais e inferências.
- Interpretação e organização interna do texto.
- As estruturas linguísticas no processo de construção de mensagens adequadas: Intertextualidade.
- Interpretação e organização interna.
- Compreensão, interpretação e reescritura de textos.
- Compreensão e interpretação de textos de gêneros variados; reconhecimento de tipos e gêneros textuais.
- Interpretação e compreensão de texto: decodificação de mensagens e processos de construção dos significados textuais.
- Interpretação, compreensão e organização estrutural dos textos.
- Compreensão e interpretação de textos e tipologia textual.

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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo portugues.interpretacao-textual.banco-N.json, onde N é o lote)
```json
{
  "materia": "portugues.interpretacao-textual",
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
