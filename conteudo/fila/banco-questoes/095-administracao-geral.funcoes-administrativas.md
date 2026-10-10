Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Administração Geral: Funções administrativas: planejar, organizar, dirigir e controlar** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Noções das teorias da administração (clássica, científica, relações humanas, burocrática, neoclássica, sistêmica e contingencial); processo administrativo e funções planejar, organizar, dirigir e controlar; planejamento estratégico, tático e operacional, missão, visão e análise SWOT; organização: estrutura formal e informal, organograma, departamentalização, centralização, descentralização, delegação, amplitude de controle, estruturas linear, funcional, linha-staff, matricial e em rede; direção: liderança, motivação (Maslow, Herzberg, McGregor, expectativa) e comunicação; controle: tipos, níveis e etapas; tomada de decisão: tipos, modelo racional, racionalidade limitada e heurísticas; cultura organizacional: níveis e elementos.
Fica de fora (outras matérias tratam): Gestão de pessoas (recrutamento, treinamento, avaliação de desempenho, competências), gestão de projetos e da qualidade, modelos de administração pública e governança, e organização da administração pública no direito administrativo, tratados em matérias próprias.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Cultura organizacional.
- Características básicas das organizações formais modernas: tipos de estrutura organizacional, natureza, finalidades e critérios de departamentalização.
- Funções da Administração: planejamento, organização, direção e controle.
- Funções de administração: planejamento, organização, direção e controle.
- Tipos de departamentalização: características, vantagens e desvantagens de cada tipo.
- Organização informal.
- Administração por objetivos.
- Evolução da administração: principais abordagens da administração (clássica até contingencial).
- Processo decisório.
- Controle: tipos, vantagens e desvantagens.
- Estrutura organizacional.
- Conceitos básicos em administração: eficiência, eficácia, efetividade, qualidade.
- Teorias da administração.
- Planejamento tático.
- Planejamento operacional.
- Controle: características.
- Motivação, liderança, comunicação, descentralização e delegação.
- Processo administrativo e funções de planejamento, organização, direção e controle.
- Planejamento: princípios e conceitos básicos, níveis estratégico, tático e operacional.
- Processo administrativo: Processo de planejamento.
- Evolução da administração.
- Comunicação.
- Processo Administrativo: planejamento, organização, direção e controle.
- Organização.
- Processo decisório: tipos de decisões.
- Processo de planejamento: Planejamento tático.
- Processo de planejamento: Planejamento operacional.
- Processo de planejamento: Administração por objetivos.
- Processo de planejamento: Processo decisório.
- Características básicas das organizações formais modernas: tipos de estrutura organizacional, natureza e finalidades.

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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo administracao-geral.funcoes-administrativas.banco-N.json, onde N é o lote)
```json
{
  "materia": "administracao-geral.funcoes-administrativas",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
