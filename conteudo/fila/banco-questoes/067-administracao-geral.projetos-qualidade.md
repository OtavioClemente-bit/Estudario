Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Administração Geral: Gestão de projetos e gestão da qualidade** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Conceito de projeto, programa, portfólio e operação; ciclo de vida do projeto e estruturas organizacionais; escritório de projetos; noções do Guia PMBOK (grupos de processos e áreas de conhecimento da 6ª edição, princípios e domínios da 7ª); escopo e EAP; cronograma, caminho crítico e folga; custos e valor agregado; riscos e respostas; partes interessadas; evolução da qualidade e seus teóricos (Deming, Juran, Crosby, Ishikawa, Feigenbaum); qualidade total; ciclo PDCA; ferramentas da qualidade (Pareto, Ishikawa, histograma, folha de verificação, dispersão, carta de controle, fluxograma, 5W2H, GUT, brainstorming, benchmarking); custos da qualidade; programa 5S; qualidade no serviço público.
Fica de fora (outras matérias tratam): Métodos ágeis em detalhe (Scrum, Kanban, XP), PRINCE2, certificação ISO 9001 em detalhe, Seis Sigma estatístico, modelos de excelência em gestão pública e reformas administrativas (tratados em matérias próprias).

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Gestão da qualidade e modelo de excelência gerencial.
- Gestão de projetos.
- Gestão de projetos: elaboração, análise e avaliação de projetos.
- Processos, grupos de processos e áreas de conhecimento.
- Ferramentas de gestão da qualidade.
- Gestão da qualidade.
- Processos, grupos de processos e área de conhecimento.
- Gerenciamento de projetos (PMBOK 7ª edição).
- Principais teóricos e suas contribuições para a gestão da qualidade.
- Gestão por Projetos.
- Gestão da qualidade e modelo de excelência gerencial: principais teóricos e suas contribuições para a gestão da qualidade.
- Principais características dos modelos de gestão de projetos.
- Projetos e suas etapas.
- Projetos e a organização.
- Gestão de projetos: Principais características dos modelos de gestão de projetos.
- Gestão de projetos: Projetos e suas etapas.
- Ciclo de vida de projeto e ciclo de vida do produto.
- Ciclo PDCA.
- Gestão de riscos.
- Elaboração, análise e avaliação de projetos.
- Gestão da qualidade e modelo de excelência gerencial: Ferramentas de gestão da qualidade.
- Gestão de projetos: Noções de elaboração, análise, avaliação e gerenciamento de projetos.
- Gerência de projetos: conceitos.
- Gestão da qualidade em serviços.
- Gestão de projetos: elaboração, análise, avaliação, modelos e etapas.
- Gestão de projetos: elaboração, análise, avaliação e gerenciamento.
- Evolução da administração: Qualidade na Administração Pública.
- Formulação de programas e projetos.
- Integração de escopo, prazos, custos, riscos, qualidade, documentação e comunicação.
- Gestão de programas e portfólio de projetos.

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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo administracao-geral.projetos-qualidade.banco-N.json, onde N é o lote)
```json
{
  "materia": "administracao-geral.projetos-qualidade",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
