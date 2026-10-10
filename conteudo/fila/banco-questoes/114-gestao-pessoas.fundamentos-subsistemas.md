Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Gestão de Pessoas: Gestão de pessoas: fundamentos e subsistemas** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Conceito e evolução da gestão de pessoas (de recursos humanos a parceiros); processos ou subsistemas (agregar, aplicar, recompensar, desenvolver, manter e monitorar); recrutamento interno, externo e misto; técnicas de seleção, validade e fidedignidade; treinamento, desenvolvimento e educação, levantamento de necessidades e avaliação de resultados em quatro níveis; avaliação de desempenho (métodos, 360 graus, erros do avaliador); gestão por competências (CHA, mapeamento de lacunas); clima organizacional e sua diferença para cultura; qualidade de vida no trabalho; comportamento organizacional (níveis de análise, motivação, liderança em noções, grupos e conflitos).
Fica de fora (outras matérias tratam): Regime jurídico dos servidores públicos, estágio probatório e avaliação especial previstos em lei, direito do trabalho, folha de pagamento e cálculos trabalhistas, administração geral (planejamento, organização, direção e controle), gestão de projetos e qualidade, psicologia clínica.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Conceitos, importância, relação com os outros sistemas de organização.
- Gestão por competências.
- Análise e descrição de cargos.
- Gestão de desempenho.
- Tendências em gestão de pessoas no setor público.
- Gestão de pessoas.
- Objetivos, desafios e características da gestão de pessoas.
- Fundamentos, teorias e escolas da administração e o seu impacto na gestão de pessoas.
- Comportamento organizacional: Qualidade de vida.
- Função do órgão de recursos humanos.
- Função do órgão de recursos humanos: atribuições básicas e objetivos.
- Função do órgão de recursos humanos: Políticas e sistemas de informações gerenciais.
- Recrutamento e seleção.
- Principais técnicas de seleção de pessoas: características, vantagens e desvantagens.
- Gestão por competências: competências organizacionais, coletivas e individuais.
- Administração de cargos, carreiras e salários.
- Qualidade de vida no trabalho.
- Gestão de pessoas: equilíbrio organizacional.
- Capacitação de pessoas.
- Análise e descrição de cargos: objetivos, métodos, vantagens e desvantagens.
- Métodos de avaliação de desempenho: características, vantagens e desvantagens.
- Gestão e avaliação de desempenho.
- Recrutamento e seleção de pessoas: objetivos e características.
- Recrutamento e seleção de pessoas: principais tipos, características, vantagens e desvantagens.
- Recrutamento e seleção de pessoas.
- Gestão de desempenho: objetivos.
- Desenvolvimento e capacitação de pessoal.
- Desenvolvimento e capacitação de pessoal: levantamento de necessidades.
- Gestão de pessoas: Gestão de desempenho.
- Gestão de pessoas: Gestão por competências.

## Cada lote de 50
- 25 de múltipla escolha A a E (5 alternativas), 13 de múltipla escolha A a D (4 alternativas), 12 de Certo/Errado.
- Dificuldade: 15 fáceis, 20 médias, 15 difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, comparação entre conceitos parecidos. Use casos DIFERENTES entre si; nada de repetir o mesmo caso com outras palavras.

## Regras de qualidade
1. Regra precisa, com exemplos concretos; nunca invente regra ou dado.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Explicação (mínimo 120 caracteres): por que a certa está certa e por que CADA errada erra, com o conceito específico. Em Certo/Errado começa com "Certo." ou "Errado.". Escreva cada explicação de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo gestao-pessoas.fundamentos-subsistemas.banco-N.json, onde N é o lote)
```json
{
  "materia": "gestao-pessoas.fundamentos-subsistemas",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false }, { "key": "B", "text": "...", "correct": true } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (A–E com 5 opções ou A–D com 4 opções) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
