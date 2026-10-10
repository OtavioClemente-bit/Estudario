Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Informática: Banco de dados e SQL** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo informatica.banco-dados-sql.banco-N.json, onde N é o lote)
```json
{
  "materia": "informatica.banco-dados-sql",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false }, { "key": "B", "text": "...", "correct": true } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (A–E com 5 opções ou A–D com 4 opções) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
