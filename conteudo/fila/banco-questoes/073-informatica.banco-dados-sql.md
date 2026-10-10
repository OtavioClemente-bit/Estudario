Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Informática: Banco de dados e SQL** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Conceitos de banco de dados e SGBD; modelo relacional, tabelas, atributos, chaves e integridade; normalização; SQL para definição, manipulação e consulta, junções, agregação e transações; noções de data warehouse, data lake e ETL.
Fica de fora (outras matérias tratam): Administração avançada de produtos específicos, sintaxe proprietária extensa, ajuste de desempenho em larga escala e implementação física de mecanismos de armazenamento.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Linguagem de consulta estruturada (SQL).
- Linguagem de definição de dados (DDL).
- Linguagem de manipulação de dados (DML).
- Banco de dados.
- Propriedades de banco de dados.
- Banco de dados: Conceitos básicos.
- Integridade referencial.
- Avaliação de modelos de dados.
- Banco de dados: Arquitetura.
- SGBD.
- Normalização das estruturas de dados.
- Banco de dados: Estrutura de dados.
- Banco de dados: Modelagem e normalização de dados.
- Banco de dados: Noções de administração de dados e de banco de dados.
- Modelagem de dados (conceitual, lógica e física).
- Chaves e relacionamentos.
- Modelagem e normalização de dados.
- Abordagem relacional.
- Banco de dados: organização de arquivos, métodos de acesso, abstração e modelos de dados.
- Integridade referencial e metadados.
- SQL, DDL e DML.
- Noções de administração de dados e de banco de dados.
- Organização e gerenciamento de bancos de dados.
- Arquivos, modelos de dados e sistemas gerenciadores de banco de dados.
- Linguagens de definição e manipulação de dados e SQL.
- Controle de proteção, segurança e integridade de bancos de dados.
- SQL.
- Projeto e modelagem de banco de dados relacional.
- Linguagem SQL.
- Noções de bancos de dados.

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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo informatica.banco-dados-sql.banco-N.json, onde N é o lote)
```json
{
  "materia": "informatica.banco-dados-sql",
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
