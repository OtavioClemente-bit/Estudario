Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Informática: Engenharia de software e métodos ágeis** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Conceito de engenharia de software; atividades genéricas do processo; modelos de processo (cascata, incremental, prototipação, espiral, Processo Unificado/RUP); engenharia de requisitos (requisitos funcionais e não funcionais, elicitação, especificação, validação e gerenciamento, histórias de usuário); UML (diagramas estruturais e comportamentais, casos de uso com include e extend, classes, sequência, atividades e estados); testes de software (níveis, caixa-preta e caixa-branca, regressão, verificação e validação, TDD); Manifesto Ágil; Scrum segundo o Guia de 2020; Kanban; XP; noções de DevOps, integração e entrega contínuas.
Fica de fora (outras matérias tratam): Gerenciamento de projetos pelo PMBOK, ITIL e COBIT, métricas como pontos de função em detalhe, CMMI e MPS.BR em detalhe, SAFe e outros frameworks de escala, ferramentas específicas de DevOps (Docker, Kubernetes, Jenkins) e programação em linguagens específicas.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Engenharia de requisitos.
- Metodologias Ágeis.
- Engenharia de requisitos: Produto mínimo viável (MVP).
- Engenharia de requisitos: Gestão de backlog.
- Qualidade: Teste unitário.
- Qualidade: Teste de integração.
- Análise e projeto orientados a objetos.
- Técnicas de priorização, de estimativas (Análise de Pontos de Função, Story Points).
- Análise e projeto.
- Engenharia de software: Unified Modeling Language (UML).
- Metodologias ágeis para o desenvolvimento de software: Scrum, XP, Lean.
- Engenharia de software: Desenvolvimento orientado a testes (TDD).
- Engenharia de software: Testes automatizados.
- Processos de desenvolvimento de software.
- Técnicas de validação de requisitos.
- Técnicas de Elicitação de Requisitos.
- Metodologias de desenvolvimento de software.
- UML: visão geral, modelos e diagramas.
- Kanban.
- Qualidade de software.
- Análise de requisitos, especificação, ambientes de testes, homologação, produção e suporte.
- Engenharia de Software: ciclo de vida do software.
- Noções sobre desenvolvimento e manutenção de sistemas e aplicações.
- Noções sobre metodologias de análise, projeto e desenvolvimento de sistemas.
- Engenharia de requisitos, gestão de backlog e produto mínimo viável.
- Gerenciamento de produtos de software por métodos Scrum, Kanban, XP e Lean.
- Desenvolvimento orientado a testes (TDD).
- Especificação de requisitos.
- Scrum.
- Metodologias ágeis, lean manufacturing e Scrum.

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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo informatica.engenharia-software-ageis.banco-N.json, onde N é o lote)
```json
{
  "materia": "informatica.engenharia-software-ageis",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
