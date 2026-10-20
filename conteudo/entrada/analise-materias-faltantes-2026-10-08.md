# Estudário — matérias que faltam e prioridades

Análise de 08/10/2026. Repositório: [OtavioClemente-bit/Estudario](https://github.com/OtavioClemente-bit/Estudario). Referência conferida: commit `db53022b74c8f2d82aafcf2b177c348b08be6c8a`, igual ao HEAD remoto na hora da consulta.

## Resultado principal

A biblioteca tem **467 matérias canônicas**, todas com `status: PUBLISHED` nos arquivos, e **550 arquivos de edital/cargo**. Isso confirma o conteúdo no GitHub; não confirma que o servidor de produção recebeu todas as publicações.

A tarefa mais útil combina duas frentes: **ligar conteúdos já existentes aos editais** e **gerar assuntos com escopo realmente ausente**. As listas antigas não podem ser usadas diretamente como lista de geração.

| Verificação | Resultado |
|---|---:|
| Matérias em `conteudo/materias/`, presentes no commit | 467 |
| IDs canônicos distintos | 467 |
| Arquivos de edital/cargo em `conteudo/editais/` | 550 |
| Linhas de tópicos nos editais | 84.143 |
| Linhas com referência de matéria | 17.708 |
| Linhas com referência nula | 66.435 |
| Referências preenchidas apontando para ID inexistente | 0 |
| Matérias locais em `entrada/` | 520 arquivos, 396 IDs |
| IDs de matéria em `entrada/` ainda ausentes do catálogo canônico | 0 |
| Matérias locais em `pendentes/` | 28 arquivos, 28 IDs já existentes no catálogo |

**As 66.435 linhas sem ligação não são 66.435 matérias faltantes.** Há tópicos repetidos, subdivisões de editais, expressões genéricas e conteúdos já existentes com nomes diferentes. A contagem é de **35.483 textos distintos sem ligação**; isso ainda não equivale a assuntos distintos.

Os arquivos locais de entrada e pendentes podem conter versões diferentes ou correções. A coincidência de ID não comprova equivalência de conteúdo, qualidade ou aprovação da versão local.

## 1. O que eu priorizaria primeiro

A ordem abaixo considera reaproveitamento no catálogo e diferença de escopo. Não foi consultado o histórico de pedidos dos usuários no servidor. A coluna de frequência é uma **triagem por termos em tópicos sem ligação**, contando arquivos de edital/cargo distintos; inclui concursos antigos e cargos do mesmo concurso. Não representa demanda atual nem garante que a nova matéria cubra todas as ocorrências.

| Prioridade | Matéria | ID previsto | Evidência e motivo |
|---|---|---|---|
| Alta | Ação civil pública | `processo-civil.acao-civil-publica` | Pedido do TRT, lote 064, sem arquivo próprio. O termo aparece em 63 arquivos de edital na busca geral; defesa coletiva do consumidor cobre apenas parte do assunto. |
| Alta | Lavagem de dinheiro, abordagem penal e processual | `legislacao-penal-especial.lavagem-dinheiro` | 55 arquivos com tópicos sem ligação. A matéria bancária existente tem outro enfoque. |
| Alta | Lei anticorrupção e acordo de leniência | `direito-administrativo.improbidade-anticorrupcao` | 48 arquivos com tópicos sem ligação. A fila manda complementar anticorrupção, sem repetir a matéria de improbidade. |
| Alta | Interceptação telefônica e telemática | `legislacao-penal-especial.interceptacao-telefonica` | 47 arquivos com tópicos sem ligação. Falta a matéria específica, além das noções de prova. |
| Alta | Crimes contra a ordem tributária | `legislacao-penal-especial.crimes-ordem-tributaria` | 47 arquivos com tópicos sem ligação. Reaproveitável em carreiras jurídicas, fiscais e policiais. |
| Alta | Arquitetura de software | `informatica.arquitetura-software` | 46 arquivos com tópicos sem ligação. Complementa engenharia de software e orientação a objetos. |
| Alta | Contratos civis em espécie | `direito-civil.contratos-especie` | 43 arquivos com tópicos sem ligação. A matéria de contratos em geral exclui expressamente contratos típicos. |
| Alta | Execução penal | `legislacao-penal-especial.execucao-penal` | 40 arquivos com tópicos sem ligação. A matéria de prova processual penal exclui execução penal. |
| Alta | Nuvem avançada, contêineres e implantação | `informatica.cloud-devops-containers` | 37 arquivos com tópicos sem ligação. Nuvem atual é introdutória; engenharia de software exclui ferramentas como Docker e Kubernetes. |
| Alta para TRT | Código de Ética do TRT3 e Regimento Interno do TRT3 | Ainda sem pedido de matéria específico identificado | Cada assunto está sem ligação em 30 arquivos de cargo do TRT3. São conteúdos institucionais; o código federal de ética não substitui o código do tribunal. |

**Primeira sequência sugerida:** corrigir ligações comuns; depois ação civil pública, lavagem de dinheiro, anticorrupção, interceptação, crimes tributários, contratos em espécie, execução penal e arquitetura de software. Se a prioridade comercial for TRT3, antecipar os dois conteúdos institucionais e as lacunas trabalhistas indicadas abaixo.

## 2. Planejamento mais recente: os 23 IDs ainda sem arquivo

Fonte: [fila de cobertura, ondas 6 e 7](https://github.com/OtavioClemente-bit/Estudario/blob/db53022b74c8f2d82aafcf2b177c348b08be6c8a/conteudo/fila/11-cobertura.md). Nenhum dos 23 IDs abaixo apareceu como matéria em `materias/`, `entrada/` ou `pendentes/`.

**Ausência do ID é diferente de ausência do conteúdo.** Dos 23, seis merecem reaproveitamento ou complementação antes de uma nova geração.

| ID da fila | Decisão sugerida |
|---|---|
| `criminalistica.fundamentos-pericia` | Gerar o conteúdo pericial específico; cadeia de custódia já aparece em `processo-penal.prova`. |
| `medicina-legal.traumatologia-tanatologia` | Gerar. Não há matéria canônica de medicina legal. |
| `legislacao-penal-especial.lavagem-dinheiro` | Gerar abordagem penal/processual. Não duplicar o enfoque de prevenção bancária. |
| `legislacao-penal-especial.crimes-ordem-tributaria` | Gerar. |
| `legislacao-penal-especial.interceptacao-telefonica` | Gerar. |
| `legislacao-penal-especial.execucao-penal` | Gerar. |
| `informatica.cloud-devops-containers` | Gerar o escopo avançado. |
| `informatica.arquitetura-software` | Gerar. |
| `arquivologia.gestao-documentos` | Reaproveitar noções de arquivologia; verificar apenas a lacuna de preservação digital e eventual aprofundamento. |
| `direito-civil.contratos-especie` | Gerar. |
| `historia.brasil-colonia-imperio` | Gerar. História de Minas Gerais não substitui História do Brasil. |
| `historia.brasil-republica` | Gerar. |
| `lingua-espanhola.interpretacao-gramatica` | Gerar. Há inglês, mas não espanhol. |
| `administracao-geral.comportamento-organizacional` | Reaproveitar fundamentos de gestão de pessoas e funções administrativas; aprofundar somente o que faltar. |
| `administracao-publica.gestao-processos` | Já há gestão por processos em processo organizacional. Verificar necessidade de BPM/BPMN e mapeamento aprofundado. |
| `direito-administrativo.improbidade-anticorrupcao` | Gerar o complemento de anticorrupção, mantendo o escopo definido na fila. |
| `direito-processual-civil.procedimentos-especiais` | Gerar os ritos específicos não cobertos. Atenção: a fila usa esse prefixo, enquanto as matérias existentes usam `processo-civil`; alinhar o ID no planejamento antes de uma futura produção. |
| `informatica.ia-generativa-etica` | Gerar o complemento de IA generativa e ética. Já existem IA e análise de dados em nível geral. |
| `informatica.sistemas-operacionais-conceitos` | Gerar o conteúdo técnico de processos, threads e memória. Linux/Windows de uso cotidiano não o substituem. |
| `informatica.redes-sem-fio-voip` | Gerar o aprofundamento e VoIP. Wi-Fi básico já está em redes de computadores. |
| `estatistica.amostragem-pesquisas` | Reaproveitar amostragem/correlação/TCL; verificar tamanho de amostra e desenho de pesquisas. |
| `matematica.geometria-espacial-areas` | Reaproveitar: a matéria de volumes já se chama “áreas e volumes” e ensina áreas laterais e totais. |
| `direito.direitos-humanos-sistema-interamericano` | Reaproveitar Convenção Americana, que já tem capítulo sobre Comissão e Corte. |

O item `legislacao.estatuto-servidor-estadual-generico` não entrou nesses 23: a própria fila diz **NÃO FAZER**, pois o conteúdo depende do estado.

### Os seis casos com cobertura existente

| Assunto marcado como pendente | Matéria atual para conferir | O que fazer |
|---|---|---|
| Gestão de documentos | [Noções de Arquivologia](https://github.com/OtavioClemente-bit/Estudario/blob/db53022b74c8f2d82aafcf2b177c348b08be6c8a/conteudo/materias/administracao-publica/nocoes-arquivologia.json) | Protocolo, classificação, temporalidade e destinação já constam. Revisar preservação digital. |
| Comportamento organizacional | [Fundamentos e subsistemas de gestão de pessoas](https://github.com/OtavioClemente-bit/Estudario/blob/db53022b74c8f2d82aafcf2b177c348b08be6c8a/conteudo/materias/gestao-pessoas/fundamentos-subsistemas.json) | Há motivação, liderança, grupos e conflitos em noções. Complementar profundidade, quando exigida. |
| Gestão de processos | [Processo organizacional](https://github.com/OtavioClemente-bit/Estudario/blob/db53022b74c8f2d82aafcf2b177c348b08be6c8a/conteudo/materias/administracao-publica/processo-organizacional.json) | Há gestão por processos e aliases correspondentes. Verificar BPMN e ferramentas avançadas. |
| Amostragem | [Amostragem, correlação e TCL](https://github.com/OtavioClemente-bit/Estudario/blob/db53022b74c8f2d82aafcf2b177c348b08be6c8a/conteudo/materias/estatistica/amostragem-correlacao-tcl.json) | Há planos amostrais, vieses e erro padrão. Verificar dimensionamento e pesquisa. |
| Áreas de sólidos | [Geometria espacial: áreas e volumes](https://github.com/OtavioClemente-bit/Estudario/blob/db53022b74c8f2d82aafcf2b177c348b08be6c8a/conteudo/materias/matematica/geometria-espacial-volumes.json) | Ligar ao conteúdo atual; não criar outra matéria só pela diferença no nome do arquivo. |
| Sistema interamericano | [Convenção Americana sobre Direitos Humanos](https://github.com/OtavioClemente-bit/Estudario/blob/db53022b74c8f2d82aafcf2b177c348b08be6c8a/conteudo/materias/direito/convencao-americana-sobre-direitos-humanos.json) | Comissão, Corte, petições e competência já estão no capítulo 4. |

## 3. TRT: lacunas relevantes além das ondas novas

Os pedidos ativos `trt3n-lote-001` a `078` contêm **155 IDs**. Desses, **114 têm o ID exato no catálogo** e **41 não têm**. Muitos dos 41 foram absorvidos por matérias mais amplas. O número 41 não deve virar automaticamente um lote de 41 gerações.

### Conteúdos que precisam de matéria específica ou complemento

| Pedido | ID ausente | Por que conferir/produzir |
|---|---|---|
| [Lote 030](https://github.com/OtavioClemente-bit/Estudario/blob/db53022b74c8f2d82aafcf2b177c348b08be6c8a/conteudo/pedidos/trt3n-lote-030.txt) | `direito-trabalho.empregado-empregador` | Relação de emprego e alteração contratual cobrem partes do assunto; não substituem automaticamente a classificação de empregados/empregadores e todos os poderes. |
| [Lote 039](https://github.com/OtavioClemente-bit/Estudario/blob/db53022b74c8f2d82aafcf2b177c348b08be6c8a/conteudo/pedidos/trt3n-lote-039.txt) | `direito-trabalho.ccp-renuncia-transacao` | Falta o conjunto específico de CCP, renúncia, transação, quitação anual e PDV. |
| Lote 061 | `processo-civil.citacao-resposta-reu` | Citação já está em atos processuais; a matéria de procedimento comum exclui resposta do réu e revelia em profundidade. |
| Lote 062 | `processo-civil.sentenca` | Formação/extinção e coisa julgada são relacionadas, mas não cobrem automaticamente elementos, fundamentação e demais regras da sentença. |
| Lote 062 | `processo-civil.execucao-fazenda-titulos` | Títulos têm cobertura; execução contra a Fazenda aparece apenas em visão geral no cumprimento de sentença. |
| Lote 063 | `processo-civil.consignacao-possessorias` | Faltam os ritos próprios; contratos e direitos reais são a base, não o procedimento completo. |
| Lote 063 | `processo-civil.busca-apreensao-alienacao-fiduciaria` | Conferir os ritos civis específicos. Busca e apreensão penal não serve de substituição. |
| [Lote 064](https://github.com/OtavioClemente-bit/Estudario/blob/db53022b74c8f2d82aafcf2b177c348b08be6c8a/conteudo/pedidos/trt3n-lote-064.txt) | `processo-civil.desapropriacao` | Intervenção na propriedade existe, mas seu escopo exclui o procedimento judicial detalhado. |
| Lote 064 | `processo-civil.acao-civil-publica` | Defesa coletiva do consumidor é parcial; falta matéria própria no escopo do pedido. |
| [Lote 065](https://github.com/OtavioClemente-bit/Estudario/blob/db53022b74c8f2d82aafcf2b177c348b08be6c8a/conteudo/pedidos/trt3n-lote-065.txt) | `processo-civil.solucao-extrajudicial-administracao` | Falta o tratamento específico de mediação, autocomposição e arbitragem com a Administração. |
| Lote 071 | `administracao-publica.sustentabilidade-cnj-400` | Desenvolvimento sustentável genérico não cobre integralmente as normas de sustentabilidade do Judiciário. |
| Lote 075 | `gestao-pessoas.modelos-gestao-pessoas` | Fundamentos já existem; conferir o recorte de evolução dos modelos e gestão no setor público. |
| Lote 076 | `gestao-pessoas.mudanca-organizacional` | Há cultura e noções gerais; conferir mudança e inovação conforme o escopo específico. |
| Lote 078 | `gestao-pessoas.avaliacao-gestao-publica` | Modelos/governança têm excelência em noções, mas o pedido exige GesPública, critérios e autoavaliação. |

### Outros 27 IDs ausentes: reaproveitar ou revisar cobertura primeiro

Esta tabela registra todos os demais IDs exatos ausentes do pedido TRT. A indicação de uma matéria relacionada não equivale a aprovação de cobertura integral.

| ID solicitado, sem arquivo próprio | Matéria(s) relacionada(s) existente(s) |
|---|---|
| `processo-civil.processo-pressupostos` | `processo-civil.acao` e `processo-civil.formacao-suspensao-extincao` |
| `processo-civil.partes-substituicao` | `processo-civil.sujeitos-processo` |
| `processo-civil.litisconsorcio-intervencao` | `processo-civil.sujeitos-processo` |
| `processo-civil.atos-prazos` | `processo-civil.atos-prazos-processuais` |
| `processo-civil.nulidades` | `processo-civil.atos-prazos-processuais`, capítulo de nulidades |
| `processo-civil.julgamento-estado-processo` | `processo-civil.formacao-suspensao-extincao` |
| `administracao-publica.estrutura-organizacional` | `administracao-geral.funcoes-administrativas` |
| `administracao-publica.gestao-publica-privada` | `administracao-publica.modelos-governanca`; revisar a comparação público/privado |
| `administracao-publica.comunicacao-redes` | `administracao-geral.funcoes-administrativas` e `administracao-publica.planejamento-estrategico`; revisar redes organizacionais |
| `administracao-publica.gestao-desempenho` | `gestao-pessoas.fundamentos-subsistemas` |
| `administracao-publica.gestao-projetos-processos` | `administracao-geral.projetos-qualidade` e `administracao-publica.processo-organizacional` |
| `administracao-publica.gestao-contratos` | `direito-administrativo.contratos-administrativos`, execução e fiscalização |
| `administracao-publica.gestao-qualidade` | `administracao-geral.projetos-qualidade` e `administracao-publica.modelos-governanca` |
| `administracao-publica.motivacao` | `administracao-geral.funcoes-administrativas` e `gestao-pessoas.fundamentos-subsistemas` |
| `administracao-publica.lideranca` | `administracao-geral.funcoes-administrativas` e `gestao-pessoas.fundamentos-subsistemas` |
| `administracao-publica.processo-decisorio` | `administracao-geral.funcoes-administrativas` |
| `administracao-publica.governanca-publica` | `administracao-publica.modelos-governanca`; revisar o referencial exigido |
| `administracao-publica.etica-administracao` | `etica.etica-moral-funcao-publica` |
| `orcamento-publico.conceitos-principios` | `afo.orcamento-publico-principios` |
| `orcamento-publico.orcamento-constituicao` | `afo.ppa-ldo-loa-ciclo` e `direito-constitucional.tributacao-orcamento` |
| `orcamento-publico.receita-publica` | `afo.receita-despesa-publica` |
| `orcamento-publico.despesa-publica` | `afo.receita-despesa-publica` |
| `orcamento-publico.creditos-adicionais` | `afo.ppa-ldo-loa-ciclo` |
| `orcamento-publico.lrf` | `afo.lei-responsabilidade-fiscal`; conferir detalhes exigidos no pedido |
| `gestao-pessoas.planejamento-clima-cultura` | `gestao-pessoas.fundamentos-subsistemas` e `administracao-geral.funcoes-administrativas` |
| `gestao-pessoas.planejamento-estrategico` | `administracao-publica.planejamento-estrategico` |
| `gestao-pessoas.indicadores-desempenho` | `administracao-publica.planejamento-estrategico` |

Preservar os IDs e escopos dos pedidos quando uma futura geração for solicitada. Se o planejamento optar por reaproveitamento, a alteração deve ser decidida no processo de conferência/publicação; este relatório não modifica os pedidos.

## 4. Corrigir ligações pode dar resultado antes de gerar

Exemplos de textos repetidos sem ligação para os quais já existem matérias relacionadas:

| Texto do edital sem ligação | Ocorrências de linhas | Conteúdo atual a usar na revisão |
|---|---:|---|
| Estruturas lógicas. | 87 | `matematica.logica-proposicional` |
| Lógica de argumentação: analogias, inferências, deduções e conclusões. | 82 | `matematica.argumentacao-logica` |
| Diagramas lógicos. | 77 | `matematica.quantificadores-diagramas` |
| Organização e gerenciamento de informações, arquivos, pastas e programas. | 76 | Windows; verificar o escopo da disciplina |
| Autarquias, fundações, empresas públicas e sociedades de economia mista. | 68 | `direito-administrativo.organizacao-administrativa` |
| Procedimentos de backup. | 65 | `informatica.seguranca` e `informatica.computacao-nuvem` |
| Comissões parlamentares de inquérito. | 60 | `direito-constitucional.poder-legislativo` |
| Noções de vírus, worms e pragas virtuais. | 60 | `informatica.seguranca` |
| Articulação textual: referências, nexos, operadores, coerência e coesão. | 30 | `portugues.coesao-textual` e `portugues.coerencia-textual` |
| Descrição extensa de Microsoft Word 2016 nos editais TRT3. | 30 | `informatica.microsoft-word-2016` |

Cada correspondência precisa de revisão contextual. Um tópico que reúne várias matérias pode exigir divisão ou uma forma de associar mais de um conteúdo. Acrescentar um alias amplo a uma única matéria pode esconder lacunas.

## 5. Listas antigas que já ficaram para trás

- [Fila PMMG](https://github.com/OtavioClemente-bit/Estudario/blob/db53022b74c8f2d82aafcf2b177c348b08be6c8a/conteudo/fila/02-pmmg.md): todos os **15 IDs** listados já existem em `materias/`.
- [Fila TRT “77 faltantes”](https://github.com/OtavioClemente-bit/Estudario/blob/db53022b74c8f2d82aafcf2b177c348b08be6c8a/conteudo/fila/03-trt-restante.md): está desatualizada; vários itens estão publicados e outros foram cobertos por matérias com IDs diferentes.
- [LEIA-ME da fila](https://github.com/OtavioClemente-bit/Estudario/blob/db53022b74c8f2d82aafcf2b177c348b08be6c8a/conteudo/fila/LEIA-ME.md): informa 373 matérias, mas o catálogo atual tem **467**.
- [Fila fiscal/bancos](https://github.com/OtavioClemente-bit/Estudario/blob/db53022b74c8f2d82aafcf2b177c348b08be6c8a/conteudo/fila/06-fiscal-bancos.md): muitas caixas continuam abertas apesar de já haver matérias dessas disciplinas; não usar as caixas como prova de ausência.

## 6. Próximas áreas para mapear

O catálogo de editais inclui cargos de saúde, engenharia, educação e serviço social, mas a biblioteca não tem disciplinas canônicas próprias para várias dessas especialidades. Isso sugere expansão futura. Não foi feita uma auditoria integral de cada um desses programas, portanto não há aqui uma lista exaustiva de matérias dessas áreas.

Para definir a próxima onda depois dos assuntos confirmados, combinar cobertura dos editais com os pedidos reais sem matéria pronta (`library_misses`, mencionado na fila). Esse histórico do servidor não foi acessado nesta análise.

## Limites e arquivos

Foi conferida a presença de arquivos, IDs, status, vínculos e escopos selecionados. Não foi realizada auditoria de todas as questões, de todas as fontes legais ou da qualidade didática das 467 matérias. Não se deve interpretar `PUBLISHED` no JSON como certificação de conteúdo ou de implantação no servidor.

Este relatório foi salvo somente em `conteudo/entrada/`. Nenhuma matéria, edital, pedido ou fila foi alterado. Não houve commit, push ou publicação. Os links do GitHub apontam para a versão examinada; o link do relatório entregue no chat é um arquivo local.
