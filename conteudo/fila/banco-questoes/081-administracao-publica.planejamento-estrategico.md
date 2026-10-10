Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Administração Pública: Planejamento estratégico, BSC, SWOT e indicadores** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Conceito e características do planejamento estratégico; etapas do processo (referencial estratégico, diagnóstico, formulação, implementação e controle); negócio, missão, visão e valores; análise dos ambientes interno e externo, matriz SWOT e estratégias cruzadas; análise de cenários; matriz GUT e plano 5W2H como apoio; estratégias genéricas de Porter, cinco forças, redes e alianças; desdobramento em objetivos, metas, indicadores e iniciativas; Balanced Scorecard: perspectivas, mapa estratégico, relações de causa e efeito, temas, ativos intangíveis e adaptação ao setor público; OKR (noções); indicadores de desempenho: conceito, atributos, componentes, tipos (insumo, processo, produto, resultado e impacto), eficiência, eficácia, efetividade e economicidade; particularidades do planejamento estratégico na administração pública.
Fica de fora (outras matérias tratam): Teorias da administração, funções planejar, organizar, dirigir e controlar e a noção introdutória dos níveis estratégico, tático e operacional (matéria administracao-geral.funcoes-administrativas); ferramentas da qualidade como PDCA, Ishikawa e Pareto; gestão de projetos; PPA, LDO e LOA; normas específicas de planejamento do Poder Judiciário; planejamento de TI (PETI e PDTI).

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Planejamento estratégico.
- Balanced Scorecard.
- Indicadores de desempenho.
- Gestão estratégica.
- Planejamento estratégico: Conceitos, métodos e técnicas.
- Negócio, missão, visão de futuro, valores.
- Variáveis componentes dos indicadores.
- Ferramentas de análise de ambiente: análise SWOT, análise de cenários, matriz GUT.
- Tipos de indicadores.
- Análise competitiva e estratégias genéricas.
- Sistema de medição de desempenho organizacional.
- Indicadores de desempenho: conceito, formulação e análise.
- Referencial Estratégico das Organizações.
- Planejamento estratégico de negócio.
- Balanced Scorecard e processo decisório.
- Processo de planejamento: Análise competitiva e estratégias genéricas.
- Processo de planejamento: Redes e alianças.
- Processo de planejamento: Balanced scorecard.
- Controle: Sistema de medição de desempenho organizacional.
- Planejamento e gestão estratégica: conceitos, princípios, etapas, níveis, métodos e ferramentas.
- Balanced Scorecard (BSC).
- Estabelecimento de objetivos e metas organizacionais.
- Implementação de estratégias.
- Análise de cenários.
- Metodologias para medição de desempenho.
- Planejamento estratégico: conceitos, princípios, etapas, níveis, métodos e ferramentas.
- Planejamento nas organizações públicas: análise do ambiente, objetivos estratégicos, missão, visão e valores; ciclo PDCA.
- Gerenciamento de indicadores, metas e resultados.
- Planejamento estratégico: visão, missão e análise SWOT, matriz GUT e ferramenta 5W2H.
- Formulação e construção de indicadores.

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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo administracao-publica.planejamento-estrategico.banco-N.json, onde N é o lote)
```json
{
  "materia": "administracao-publica.planejamento-estrategico",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
