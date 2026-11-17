Nota do aluno: 8/10

ERROS DE CONTEÚDO (corrija todos):
- Cap. 6 afirma que 2PL garante execução serializável sem ressalvar que gera conflito-serializabilidade e não evita deadlock (deadlock é tratado à parte; aceitável)
- Teorema CAP apresentado de forma simplificada ('escolhe-se uma das duas'), aceitável para prova

O QUE FALTA (acrescente na teoria, dentro dos capítulos existentes):
- Produtos específicos (MySQL, PostgreSQL, SQL Server, Oracle, SQLite, Access)
- Cálculo relacional
- Concorrência otimista x pessimista
- CREATE DOMAIN, sequências, sinônimos
- Window functions e consultas hierárquicas mais completas
- Funções de texto/data
- Backup e otimização de desempenho
- Bancos em memória e textuais
- Acesso a banco por linguagens de programação
- Modelagem funcional; IDEF1X/Crow's Foot/UML mais desenvolvidos

ITENS DE EDITAL QUE A MATÉRIA COBRE SÓ EM PARTE:
- "Bancos de Dados: conceitos, características, teclas de função, ícones e atalhos de teclado, uso dos recursos.": falta Item mira um aplicativo de BD de escritório (Access/Base): teclas, ícones e atalhos não são tratados.
- "Notação IDEF1X.": falta IDEF1X tem uma linha só; faltam símbolos, entidades dependentes/independentes, relacionamentos identificadores com exemplos.
- "Fundamentos: finalidades, níveis de abstração, modelagem de dados, modelagem funcional.": falta Modelagem funcional (DFD, análise funcional) não é tratada.
- "Base de dados, documentação e prototipação.": falta Documentação de modelos e prototipação não são abordadas.
- "Administração de banco de dados.": falta Papel do DBA só listado; faltam backup/restore, monitoramento, gestão de usuários e armazenamento.
- "Administração de banco de dados: Arquitetura e políticas de armazenamento de dados.": falta Políticas de armazenamento (tablespaces, particionamento, retenção, camadas) não são tratadas.
- "Banco de dados. Arquitetura de banco de dados: relacional (PostgreSQL 18.4, Oracle 26ai, SQLServer 2025), não relacional (orientado a documento, chave-valor, grafo, colunar, time series)...": falta Faltam os produtos/versões citados e bancos time series; NoSQL só em tabela resumida.
- "ElasticSearch, bancos relacionais, tabelas, visões, índices, chaves e relacionamentos.": falta Elasticsearch não é tratado.
- "Banco de dados relacional: ...; álgebra e cálculo relacional; SQL básica.": falta Cálculo relacional (de tuplas e de domínios) não é tratado.
- "Conceitos: Padrão ANSI para arquitetura de SGBD; ...; Métodos de controle de concorrência (otimistas e pessimistas).": falta Falta controle de concorrência otimista x pessimista e timestamps; serialização só via 2PL.
- "Linguagem ANSI SQL 92: Níveis de Isolamento; Tipos de dados; Criação de domínios; ...": falta CREATE DOMAIN (criação de domínios) não é tratado.
- "Bancos de Dados. Fundamentos ... SQL. MySQL X SQL Server X Oracle X PostgreSQL.": falta Falta a comparação entre os SGBDs citados (só a diferença LIMIT/TOP/FETCH).
- "Bancos de dados relacionais e SGBD Microsoft SQL Server e PostgreSQL: conceitos, administração e SQL.": falta Administração e particularidades de SQL Server e PostgreSQL não são tratadas.
- "SQL (ANSI): definição, consulta e manipulação de dados. Transact-Sql.": falta T-SQL só citado de nome; faltam sintaxe e recursos (variáveis @, TOP, TRY/CATCH).
- "Bancos de Dados: ... extensão espacial (Postgresql/PostGIS) ...": falta PostGIS e dados espaciais não são tratados.
- "Comandos SQL SELECT, INSERT, UPDATE e DELETE executados via PHP.": falta Execução via PHP (PDO/mysqli, prepared statements) não é mostrada.
- "SQL e gerenciamento de backups.": falta Backup (completo, incremental, diferencial, restore) não é tratado.
- "SGBDs Oracle 11g, MySQL 5, PostgreSQL 9, SQL Server 2019: SQL: DML, DDL, DCL, DTL e DQL.": falta Particularidades de cada SGBD citado não são tratadas.
- "Ferramentas de modelagem de dados.": falta Nenhuma ferramenta de modelagem (ERwin, MySQL Workbench, brModelo) é apresentada.
- "Banco de Dados Relacionais. SQL, ..., tuning de performance, views, roles e permissões.": falta Tuning de performance não é tratado.
- "Arquitetura de banco de dados: relacional, hierárquico, rede, lista invertida e orientado a objetos.": falta Modelo de lista invertida não é citado.
- "Bancos de dados. ... multidimensionais. SQLite. SQL. Transações: características e análise de logs.": falta SQLite e análise de logs de transação (forense) não são tratados.
- "Bancos de dados: ...; estruturas de registro e análise de logs; big data.": falta Análise de logs e big data não são tratados.
- "Manipulação de valores, textos e datas, subconsultas e tratamento de valores faltantes ou duplicados.": falta Funções de texto e data (SUBSTRING, UPPER, EXTRACT, datas) e deduplicação em SQL não são tratadas.
- "Banco de dados. ... SQL (ANSI). Microsoft SQL Server, MySql e PostgreSQL. Transact-SQL (T-SQL).": falta Produtos e T-SQL não são tratados.
- "Bancos de dados. ... Linguagens de consulta – SQL, conceitos e comandos SQL SERVER e DB2.": falta Comandos específicos de SQL Server e DB2 não são tratados.
- "Entidades, relacionamentos, chaves, integridade, normalização, transações, concorrência, índices, particionamento, visões, segurança, backup e recuperação.": falta Particionamento de tabelas e backup não são tratados.
- "Funções Analíticas (Window Functions): OVER(), PARTITION BY, ROW_NUMBER, RANK, DENSE_RANK, LEAD, LAG.": falta Só dois exemplos; faltam RANK x DENSE_RANK, LEAD, LAG.
- "SGBD: tabelas, visões, índices, sequências, sinônimos, restrições, dicionário de dados, tipos de dados, SQL e PL/SQL.": falta Sequências e sinônimos não são tratados.
- "Fundamentos e arquitetura de Sistemas Gerenciadores de Bancos de Dados relacionais e textuais.": falta Bancos textuais (full-text) não são tratados.
- "Banco de dados: ... MySQL em Linux, PostgreSQL em Linux e Oracle em Linux.": falta Instalação/operação dos SGBDs em Linux não é tratada.
- "XML, CSV, JSON. Banco de Dados Relacional.": falta Formatos XML e CSV não são tratados; JSON só definido.
- "Modelagem de dados relacional. Linguagem SQL. SGBDs MySQL, SQL Server 2019, PostgreSQL 17 e Oracle 23ai.": falta Produtos e versões não são tratados.
- "Arquitetura de Dados: Melhoria de performance de banco de dados.": falta Só noções de índice; faltam plano de execução, estatísticas, reescrita de consultas.
- "Bancos de dados, SGBDs Oracle, PostgreSQL e MySQL, SQL, PL/SQL, consultas, gatilhos, views, funções, procedimentos, packages, cursores e arrays.": falta Arrays/coleções em PL/SQL e detalhes de packages e cursores (OPEN/FETCH/CLOSE) não aparecem.
- "Modelos e diagramas entidade-relacionamento, EER, notação Crow's Foot e classes UML 2.5.": falta Crow's Foot e UML só em uma linha cada; faltam símbolos e exemplos.
- "Modelo entidade-relacionamento estendido, notação Crow's Foot e classes UML 2.5.": falta EER (disjunção/sobreposição, total/parcial), Crow's Foot e UML pouco desenvolvidos.
- "SQL ANSI, Oracle 19C, PostgreSQL e MongoDB.": falta Oracle 19c, PostgreSQL e MongoDB não são tratados.
- "Consultas hierárquicas e analíticas.": falta Consultas hierárquicas (CONNECT BY, CTE recursiva) não são tratadas.
- "Banco de dados: álgebra relacional, MER, Propriedades de um SGBD (ACID), SQL-ANSI, MySQL e PostgreSQL.": falta MySQL e PostgreSQL não são tratados.
