# Redesign da tela inicial e do plano

## Objetivo

Reorganizar as telas Início e Plano do Estudário para que a pessoa entenda em poucos segundos o que precisa estudar, qual é seu progresso e quando concluirá o edital. A experiência deve parecer um produto de estudos premium, com leitura confortável em celulares pequenos, ações claras e todos os textos visíveis em português.

## Problemas observados

- A tela Início comprime cards lado a lado, reduzindo tipografia e legibilidade.
- A hierarquia não deixa claro qual é a próxima ação recomendada.
- A tela Plano mistura calendário, resumo e missões sem uma sequência de leitura simples.
- As missões não têm destaque suficiente para matéria, tópico, duração e ação principal.
- Existem rótulos técnicos ou em inglês na interface.
- O redesign recente removeu telas antigas sem atualizar toda a suíte de testes.

## Direção visual e de UX

### Início

Usar uma coluna vertical como padrão, com espaçamento generoso e cards ocupando a largura útil da tela. A ordem da tela será:

1. Cabeçalho com saudação, nome do concurso e avatar.
2. Card principal “Estudar agora”, mostrando a próxima missão ou um estado vazio orientado.
3. Card “Plano de hoje”, com minutos previstos, missões concluídas e progresso circular maior.
4. Lista das missões de hoje, cada uma com matéria, tópico, tipo, duração, status e botão de ação.
5. Card “Seu progresso”, com cobertura do edital, domínio estimado e previsão de conclusão.
6. Card “Sua semana”, com ritmo de estudo e próximos dias carregados.
7. Card secundário de revisão, erros ou próxima conquista.

Os cards secundários não devem competir visualmente com “Estudar agora”. Em larguras pequenas, nenhum conteúdo essencial será colocado em duas colunas; componentes lado a lado só serão usados para métricas curtas quando houver espaço suficiente.

### Plano

O topo terá título, nome do plano e ações de ajuda, importação, criação e gerenciamento. Abaixo haverá um resumo de progresso com:

- missões concluídas e totais;
- minutos feitos e planejados;
- cobertura do edital;
- previsão de conclusão.

O conteúdo principal será organizado por filtros em português: “Hoje”, “Semana”, “Mês” e “Visão geral”. “Hoje” será a visão inicial. Cada visão deverá preservar o mesmo padrão de leitura: período, resumo, calendário ou agrupamento, lista de missões e estados vazios.

O calendário semanal será a visualização padrão, com dias clicáveis, marcação de dias com tarefas e acesso explícito à visão mensal. A lista de missões será agrupada por dia ou período e usará cards largos, com o botão principal sempre visível.

### Missões

Cada missão exibirá, em português:

- matéria em destaque;
- tópico;
- tipo: Teoria, Questões, Revisão, Recordação ativa, Flashcards, Simulado ou Discursiva;
- duração planejada;
- quantidade de questões quando houver;
- prioridade;
- status: Planejada, Em andamento, Concluída, Reprogramada, Não realizada ou Pausada;
- ação: Começar, Continuar, Concluída, Reprogramar ou Pular.

As ações devem ter área mínima confortável para toque e não depender de menus escondidos para a ação principal.

## Português da interface

Criar uma camada de apresentação para converter enums, tipos de tarefa, status e mensagens do motor em textos em português. IDs, nomes de classes e valores persistidos permanecem estáveis para não quebrar dados existentes. O escopo inicial cobre Início, Plano, calendário, missões, widget e diálogos ligados ao plano.

## Limites técnicos

- Não alterar o motor de planejamento, o banco ou o formato de importação/exportação.
- Reutilizar `ActivePlanUiState`, `PlannerTaskUi`, `StudyPlanViewModel` e os cálculos existentes.
- Concentrar mudanças visuais em `HomeScreen`, `PlanScreen`, `CalendarScreen`, `MissionCard` e componentes auxiliares.
- Manter o `applicationId` `br.com.estudario`.
- Preservar estados de carregamento, erro, plano vazio, dia livre e plano concluído.
- Widgets e sincronização de calendário devem continuar opcionais e não podem bloquear a abertura do app.

## Critérios de sucesso

- Em uma tela de 360 dp de largura, a missão principal e sua ação ficam legíveis sem reduzir texto para tamanho de rótulo.
- Ao abrir Início, a pessoa identifica a próxima ação em até três segundos.
- Ao abrir Plano, a pessoa entende o dia selecionado, o progresso e a próxima missão sem abrir menus.
- Não há palavras em inglês na interface dessas áreas, salvo nomes próprios inevitáveis.
- O APK compila, os testes unitários passam e os testes instrumentados compilam.
- A inicialização do app não depende de permissão de calendário, widget ou integração externa.
