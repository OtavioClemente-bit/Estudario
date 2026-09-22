# Menu lateral global e avaliação real de desempenho

## Objetivo

Permitir que a pessoa abra o menu do Estudário enquanto navega por qualquer tela completa do app e transformar Desempenho em uma análise útil, consistente e sustentada pelo histórico real — não uma coleção de números soltos ou uma nota inventada.

## Entendimento aprovado

- Ajustes, notificações, histórico/revisões, fontes, desempenho e os demais destinos do menu continuam acessíveis sem voltar à Home.
- Existe um único acionador do menu lateral do Estudário; não duplicar o ícone de três linhas em menus internos.
- A aba inferior continua reservada às quatro áreas principais; o menu lateral é global.
- Desempenho cruza estudo, questões, revisões e cumprimento do plano, respeita o período selecionado e explica quando ainda faltam dados para avaliar.

## Diagnóstico confirmado

- Em `EstudarioApp.kt`, o acionador da top bar e os gestos do drawer estão condicionados a `showBottom`; rotas secundárias não oferecem acesso consistente ao menu.
- A janela usa edge-to-edge e o `Scaffold` global declara `WindowInsets(0, 0, 0, 0)`. Em `topic/{id}`, sem top/bottom bar do shell, `TopicDetailScreen` preenche a janela com uma `LazyColumn` sem insets seguros; assim, o conteúdo pode ficar sob o relógio/status bar e a área de navegação do celular.
- `StatisticsScreen` filtra tentativas de questões pelo período, mas calcula sequência/atividade sobre histórico completo. O tempo mostrado soma apenas sessões de questões, e a comparação por matéria depende do banco de questões presente.
- O app já armazena tentativas, sessões de questões, sessões de estudo, revisões e execuções de tarefas do plano. O `StudyPaceEvaluator` existente mede previsão de cobertura versus data da prova, não substituindo a avaliação de desempenho.

## Navegação global

- Manter um único `ModalNavigationDrawer` como parte do shell que envolve o `NavHost`, disponível em todas as rotas completas: destinos inferiores, detalhes, ajustes, notificações, histórico/revisões, fontes, desempenho e ajuda.
- A top bar mantém uma única ação para abrir o menu do Estudário. Em rotas empilhadas, preservar também a ação de voltar, sem apresentar dois hambúrgueres.
- Manter os quatro destinos inferiores atuais e seus estados/back stack. Abrir um destino do drawer não deve duplicar uma rota principal nem reiniciar o conteúdo atual sem necessidade.
- Gestos do drawer e botão funcionam em todas as rotas compatíveis, sem interceptar gestos próprios de leitura, quiz, foco ou controles horizontais.
- Telas completas recebem o shell comum; diálogos, folhas modais temporárias, seletores de arquivo e telas de autenticação do sistema não ganham um drawer independente.
- A top bar e o drawer respeitam insets em notch, status bar e navegação gestual ou por botões. O trabalho existente de edge-to-edge/Home deve ser preservado.
- Em detalhes de tópico, título, ações e conteúdo permanecem dentro da área realmente visível: abaixo da status bar/relógio e acima da navegação do sistema. O conteúdo longo continua rolável até o fim; insets não são aplicados duas vezes quando a barra comum já reservou a área superior.

## Avaliação de desempenho

### Dados e consistência

- Criar uma unidade de domínio pura para montar a avaliação a partir de dados reais já disponíveis:
  - tentativas respondidas para acerto e evolução;
  - histórico de revisões para frequência/conclusão;
  - sessões de estudo e de questões para tempo registrado por tipo;
  - tarefas planejadas e execuções para aderência ao plano e carga planejada versus realizada.
- Não somar durações de fontes que possam representar a mesma sessão sem um vínculo que prove que são distintas. Quando não houver deduplicação segura, mostrar subtotais com sua origem em vez de publicar um total enganoso.
- Períodos de 7, 30 e 90 dias, mais “Tudo”, usam limites de data locais claros e filtram toda métrica, gráfico e comparação. Para períodos finitos, comparar com a janela imediatamente anterior de mesma duração; em “Tudo”, omitir comparação.
- A sequência atual pode continuar sendo calculada até hoje, mas deve estar identificada como tal e não ser apresentada como se pertencesse apenas ao filtro atual.

### O que a pessoa vê

- **Consistência:** dias ativos e sessões no período, com atividade por dia.
- **Aprendizado:** questões respondidas, percentual de acerto, mudança frente ao período anterior e resultados por matéria/tópico com amostra suficiente.
- **Revisão:** revisões concluídas no período; não inferir retenção ou qualidade que o histórico não registra.
- **Execução do plano:** tarefas previstas e concluídas no período, atrasos relevantes e minutos executados, com denominador explícito.
- **Tempo:** sessões de estudo e de questões separadas quando não for possível garantir que as durações não se sobreponham.
- **Leitura por matéria:** forças, pontos a reforçar e volume de dados; abaixo de 10 tentativas, marcar “Poucos dados” e não produzir diagnóstico definitivo de acerto.
- **Orientações acionáveis:** regras determinísticas e explicáveis apoiadas nos dados, como retomar revisão atrasada, praticar matéria com acerto baixo e amostra suficiente, ou reajustar carga quando a execução ficar consistentemente abaixo do plano.
- Sem amostra suficiente, exibir um estado vazio instrutivo e dizer qual atividade gera o dado necessário. Não fabricar estatísticas nem reduzir tudo a uma nota geral opaca de 0–100.

### Fontes de verdade e isolamento

- `StatisticsScreen` apresenta modelos vindos de um agregador/view model; não duplica regra de negócio em composables.
- Reutilizar entidades e fluxos Room existentes para tentativas, sessões, revisões e execuções. Só propor migração se for comprovada uma lacuna de dado; não armazenar score derivado como evento.
- Manter `StudyPaceEvaluator` responsável somente por ritmo/previsão do edital.

## Testes e critérios de aceite

1. O menu abre por botão em todas as rotas completas citadas e o back stack continua correto; há um único acionador de drawer.
2. Testar janela compacta e fontes ampliadas com status bar e navegação do sistema: em `topic/{id}`, cabeçalho não fica sob o relógio, ações não são cortadas e o último conteúdo pode ser alcançado acima da navegação. Verificar também navegação por gesto/botão e que gestos do quiz/foco não abram o drawer por acidente.
3. Cada métrica usa a mesma janela; limites de período e comparação anterior têm testes de fronteira.
4. Tempo não duplica fontes sem vínculo; revisões não são tratadas como retenção medida.
5. Amostras pequenas exibem contagem e “Poucos dados”; tendências e orientações usam somente eventos do período.
6. Aderência exclui tarefas canceladas/pausadas do denominador e lida com execução ausente/parcial sem falsos 100%.
7. Estados sem dados, dados parciais e histórico real possuem testes de apresentação; navegação e Home continuam cobertas por regressão.

## Fora de escopo

- Trocar a navegação inferior, criar mais abas ou duplicar o menu “Mais”.
- Prever nota/classificação na prova sem um modelo validado, ou usar IA para inventar diagnósticos.
- Reescrever eventos históricos ou modificar silenciosamente planos existentes.
