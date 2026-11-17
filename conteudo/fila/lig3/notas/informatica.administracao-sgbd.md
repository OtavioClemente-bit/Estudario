Nota do aluno: 7/10

ERROS DE CONTEÚDO (corrija todos):
- nenhum

O QUE FALTA (acrescente na teoria, dentro dos capítulos existentes):
- Níveis de isolamento e anomalias nomeados
- 2PL, timestamp e serializabilidade
- Técnicas de recuperação (ARIES, atualização adiada/imediata)
- Organização de arquivos e métodos de acesso
- Arquitetura interna Oracle/SQL Server/PostgreSQL
- Detalhes SQL Server (MDF/NDF/LDF, TempDB, DENY, DDM, RECOVERY/NORECOVERY, FCI, log shipping)
- Instalação/configuração MySQL
- Banco em memória em profundidade
- ADABAS em profundidade
- DB2 e MongoDB
- Mascaramento de dados
- Cálculo volumétrico e particionamento
- Programação PL/SQL

ITENS DE EDITAL QUE A MATÉRIA COBRE SÓ EM PARTE:
- "Banco de dados em memória.": falta Só um parágrafo; faltam arquitetura (column store em memória, SAP HANA, Redis, Oracle In-Memory, SQL Server In-Memory OLTP), persistência por snapshot/log e casos de uso.
- "PostgreSQL.": falta Sem arquitetura de processos (postmaster, backends, shared_buffers), postgresql.conf/pg_hba.conf, psql, schemas, tipos e extensões.
- "Banco de dados: Oracle 21C, MySql, ADABAS e MS-SQLSERVER 2019.": falta Produtos só em tabela de termos; falta arquitetura Oracle (SGA/PGA, processos de fundo, CDB/PDB), ADABAS (Natural, FDT, MU/PE) e SQL Server (instâncias, bancos de sistema).
- "Bancos de Dados: Linguagem SQL e PL/SQL: Consultas, administração, otimização de performance.": falta Não ensina SQL de consulta nem PL/SQL além de um exemplo de exceção.
- "Controle de concorrência.": falta Faltam bloqueio em duas fases (2PL), bloqueios compartilhado/exclusivo, granularidade, timestamp, serializabilidade de escalonamentos.
- "Transações, propriedades ACID e controle de concorrência.": falta Concorrência sem 2PL, escalonamentos, serializabilidade e protocolos de timestamp.
- "Administração de banco de dados: fundamentos, SGBD, organização de arquivos, técnicas de armazenamento, métodos de acesso, tipos de bancos de dados, projeto de bancos de dados.": falta Organização de arquivos (heap, sequencial, hash), métodos de acesso (índices B+, hash, ISAM), tipos de banco e projeto não são tratados.
- "Administração de bancos de dados: arquivos, armazenamento, métodos de acesso e tipos de banco.": falta Organização de arquivos e métodos de acesso (heap, hash, árvore B+) ausentes.
- "Cálculo volumétrico, replicação, cluster, particionamento e esquemas de bancos de dados.": falta Cálculo volumétrico (tamanho de linha x quantidade x crescimento) e particionamento (range, list, hash) não explicados; cluster só citado.
- "Administração do SQL Server: Instalação e configuração, tipos de instalação (standalone, cluster, FCI, Always On), edições..., TempDB, Arquivos MDF, NDF e LDF.": falta Faltam edições, collation, max/min server memory, Configuration Manager, TempDB e arquivos MDF/NDF/LDF.
- "Segurança do SQL Server: Autenticação, logins, users, roles, permissões (GRANT, REVOKE, DENY), Criptografia, Auditoria e SQL Server Audit, Dynamic Data Masking...": falta Faltam DENY e sua precedência, modos de autenticação (Windows/misto), papéis fixos, SQL Server Audit e Dynamic Data Masking.
- "Backup, Restore e Recuperação: ... restaurações (RECOVERY, NORECOVERY), estratégias de backup.": falta RESTORE WITH RECOVERY/NORECOVERY/STANDBY, tail-log backup e copy-only não explicados.
- "Alta disponibilidade e recuperação de desastres (HA/DR): Always On Availability Groups, FCI, Replicação (Transactional, Snapshot, Merge), Log Shipping, Diferença entre HA x DR.": falta FCI, log shipping e tipos de replicação do SQL Server (transacional, snapshot, merge) não são explicados.
- "Bancos de dados: conceitos, fundamentos e administração de SGBD IBM DB2 e MS SQL Server.": falta IBM DB2 não é abordado.
- "Transações, propriedades ACID, controle de concorrência e níveis de isolamento.": falta Não nomeia os níveis (READ UNCOMMITTED, READ COMMITTED, REPEATABLE READ, SERIALIZABLE) nem as anomalias (leitura suja, não repetível, fantasma).
- "Fundamentos de SGBD: ACID, deadlock, controle de integridade e concorrência; bancos de mercado; noções de NoSQL e cache (p. ex. Redis).": falta NoSQL e Redis não são tratados.
- "Administração de banco de dados Oracle versões 11g, 12c e 18c: conceitos, arquitetura, backup, restore, modelo físico, desempenho, armazenamento, criação e manutenção de objetos de banco, índices, importação e exportação de dados.": falta Arquitetura Oracle (SGA, PGA, processos), multitenant 12c, Data Pump (expdp/impdp) e criação de objetos não são tratados.
- "SQL ANSI, Oracle 19C, MySQL, PostgreSQL, MongoDB e MS-SQL Server 2019.": falta MongoDB e SQL ANSI não são abordados.
- "Processamento de transações, controle de concorrência e recuperação.": falta Técnicas de recuperação (atualização adiada/imediata, shadow paging, ARIES) e protocolos de concorrência ausentes.
- "Processamento de consultas, otimização e ajustes de bancos de dados.": falta Processamento de consultas (análise, álgebra relacional, algoritmos de junção nested loop/hash/merge, heurísticas) não é tratado.
- "APLICAÇÕES DISTRIBUÍDAS: Monitores de processos e transações (TP monitors); Gerência e protocolos de transações distribuídas.": falta TP monitors (CICS, Tuxedo, XA) não abordados; 2PC está coberto.
- "Banco de Dados MySQL: Instalação do MySQL Server.": falta Instalação do MySQL (mysqld, mysql_secure_installation, porta 3306) não é tratada.
- "Configuração do MySQL Server.": falta my.cnf/my.ini, variáveis (innodb_buffer_pool_size etc.) não tratados.
- "Instalação e configuração do sistema gerenciador de banco de dados MySQL.": falta Instalação e configuração do MySQL ausentes.
- "Definição e gerenciamento de STORAGE de tabelas e índices.": falta Cláusulas de storage, extents, segmentos, fillfactor/PCTFREE não explicados.
- "Administração de bancos de dados em ambientes virtualizados e em nuvem (serviços de banco de dados gerenciados).": falta DBaaS (RDS, Azure SQL, Cloud SQL) e responsabilidade compartilhada não tratados.
- "Instalação de correções (patches), upgrades e downgrades.": falta Só uma menção a atualizações; falta procedimento de patch/upgrade (pg_upgrade, OPatch, CU do SQL Server, rollback).
- "Gerenciamento de sessões, processos e conexões.": falta Encerrar sessões (pg_terminate_backend, KILL, ALTER SYSTEM KILL SESSION), limites de conexão e pools não tratados.
- "Parâmetros de inicialização e configuração de recursos (memória, CPU, disco) para otimização do SGBD.": falta Parâmetros concretos (shared_buffers, work_mem, SGA_TARGET, max server memory, MAXDOP) ausentes.
- "Mascaramento e anonimização de dados sensíveis.": falta Mascaramento (estático, dinâmico), anonimização x pseudonimização não abordados.
- "Migração e Integração de Dados: Técnicas de importação e exportação (Data Pump, SSIS, pg_dump).": falta Data Pump e SSIS não explicados; só pg_dump.
- "Ferramentas nativas (Oracle AWR/ADDM, SQL Server Profiler, pg_stat_activity).": falta ADDM, Profiler/Extended Events e pg_stat_activity não explicados.
- "Banco de dados relacional em plataforma baixa, MySQL em Linux, PostgreSQL em Linux, Oracle em Linux, ADABAS e XML.": falta XML em banco e instalação em Linux não abordados.
- "Linguagens procedurais de SGBDs Oracle, MySQL e PostgreSQL.": falta Só cita PL/SQL, T-SQL e PL/pgSQL; não ensina sintaxe nem stored procedures do MySQL.
- "Administração de bancos de dados Oracle, PostgreSQL, SQL Server, MySQL e MongoDB - Conceitos, estruturas, comandos, utilitários, configuração e instalação.": falta MongoDB ausente; comandos, utilitários e instalação dos produtos pouco tratados.
- "Administração de banco de dados relacionais. Técnicas de modelagem e otimização de bases de dados multidimensionais.": falta Modelagem multidimensional (estrela, floco de neve, cubos, agregados) não abordada.
- "PostgreSQL 18, MySQL 8.4 LTS, Microsoft SQL Server 2025 e MongoDB 8.0, ou versões mais recentes.": falta MongoDB e novidades das versões citadas não tratados.
- "Banco de Dados e Engenharia de Dados: bancos de dados relacionais, colunares e NoSQL, SQL e variações, PL/SQL, administração de SGBDs (Oracle, SQL Server, PostgreSQL).": falta Bancos colunares e NoSQL, SQL e PL/SQL não ensinados.
- "Instalação, configuração e administração de PostgreSQL, SQL Server, MySQL, IBM DB2, Lucene, Solr e Elasticsearch.": falta IBM DB2, Lucene, Solr e Elasticsearch ausentes.
- "Plataformas de SGBD: ADABAS.": falta ADABAS só em um parágrafo: faltam FDT, descritores/superdescritores, campos MU/PE, Natural, utilitários.
