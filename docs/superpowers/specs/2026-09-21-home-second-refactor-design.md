# Segunda refatoração da Home do Estudário

## Objetivo

Transformar a Home em uma página pessoal de acompanhamento do estudante, com uma narrativa visual clara: contexto do concurso, situação atual, próxima ação, cobertura do edital e evolução. A tela deve corrigir a sobreposição da status bar em edge-to-edge, preservar todos os dados do edital e funcionar bem em larguras próximas a 360 dp.

O escopo é a experiência da Home e o shell de navegação que a hospeda. O motor de planejamento, as entidades Room, o formato de importação/exportação e a lógica de geração do plano permanecem inalterados.

## Diagnóstico confirmado

- `EstudarioTopBar` é uma `Row` customizada que não consome os insets da status bar. Com edge-to-edge no Android recente, a barra começa sob o relógio e os demais ícones do sistema.
- O `Scaffold` não define uma política explícita para os insets enquanto o top bar e o bottom bar são componentes customizados/sem configuração coordenada. Isso deixa o conteúdo dependente dos defaults e dificulta garantir que os insets não sejam aplicados duas vezes.
- `SyllabusCoverage` usa `coverage.subjects.take(4)` e mostra “e mais …”; a lista chega completa ao modelo, mas a UI a oculta.
- `NextUpStrip` concatena matéria, tópico e duração numa linha com `maxLines = 1`, sacrificando legibilidade de nomes longos.
- A Home mostra a missão “Agora” antes de uma síntese de jornada, usa peso excessivo no cartão/CTA e mantém rótulos de seção em caixa alta, dando aparência de relatório técnico.
- Desempenho sem dados é representado por um traço grande e o nível mostra apenas XP acumulado, sem deixar explícito o progresso até o próximo nível.
- Os previews atuais usam nomes curtos e não exercitam suficientemente os nomes reais longos nem a quantidade de matérias do edital.

## Direção visual e hierarquia

A Home será composta como uma sequência de bandas de informação, sem transformar cada item em um card independente:

1. **Shell de identidade**: menu, marca e avatar em uma top bar compacta; busca continua disponível sem competir com o contexto da Home. O avatar usa foto salva quando existir, depois iniciais e, por fim, o fallback gráfico já existente.
2. **Contexto**: saudação discreta, nome completo do concurso e objetivo/cargo quando houver. O usuário deve reconhecer imediatamente em qual concurso está.
3. **Onde estou**: componente integrado de jornada com porcentagem de cobertura, contagem de tópicos, trilha segmentada por matéria, sequência e nível. Não usar donut, velocímetro ou três cards isolados.
4. **Agora**: componente compacto de maior ação. A matéria é contexto, o tópico ocupa a maior hierarquia e pode ocupar até três linhas. Metadados ficam separados do título e o CTA é forte, mas não ocupa toda a largura nem domina a primeira dobra.
5. **Depois**: itens verticais com matéria, tópico e duração em linhas próprias. Nunca concatenar conteúdo essencial em uma linha única; exibir no máximo as próximas atividades relevantes e deixar a agenda completa na aba Plano.
6. **Cobertura do edital**: mostrar todos os assuntos disponíveis no estado, na ordem do edital. Cada matéria terá nome com até duas linhas, percentual preservado em uma coluna própria e uma indicação de progresso fina abaixo/ao lado, sem reduzir a barra por causa do nome.
7. **Ritmo atual**: previsão de conclusão derivada do domínio. Com data de prova, comunicar a folga ou o atraso; sem data, mostrar a previsão e uma ação discreta para cadastrar a data. Não inventar dias restantes.
8. **Evolução**: desempenho e sequência em um resumo integrado. Sem histórico, usar empty states curtos (“Ainda sem histórico de questões.” / “Comece hoje.”) e ações contextuais; não mostrar estatísticas falsas ou um traço gigante.
9. **Nível**: nível, título, XP dentro do nível e XP necessário para o próximo, com uma representação discreta e identidade própria. O nível não deve virar um sistema de RPG dominante.

A tipografia usará capitalização normal na maior parte do conteúdo. Rótulos curtos podem ser diferenciados por tamanho, peso e cor, não por caixa alta em todas as seções. O espaçamento deve favorecer densidade confortável e deixar visíveis concurso, jornada e Agora na primeira dobra de um aparelho próximo a 360 dp.

## Insets e shell de navegação

A política de insets será explícita e única:

- O shell principal adotará edge-to-edge de forma explícita pela Activity, compatível com Android recente e versões anteriores suportadas.
- O `Scaffold` usará `contentWindowInsets = WindowInsets(0, 0, 0, 0)` para não combinar seus defaults com barras customizadas.
- A top bar aplicará `WindowInsets.statusBars`/`windowInsetsPadding` e terá altura real incluindo a área segura superior.
- A `NavigationBar` aplicará `WindowInsets.navigationBars`; o conteúdo receberá o `innerPadding` real do `Scaffold`.
- O drawer respeitará `WindowInsets.safeDrawing`.
- A Home removerá o bottom padding expansivo usado como compensação. O espaço final será apenas o necessário para a última seção respirar, pois a área da NavigationBar já estará refletida no `innerPadding`.
- Nenhum componente filho aplicará novamente `statusBarsPadding` ou `navigationBarsPadding` se já estiver dentro do espaço consumido pelo shell.

Essa política deve funcionar com notch, câmera central, diferentes alturas de status bar, navegação gestual e navegação de três botões.

## Dados e fronteiras

`HomeScreen` continuará coletando os mesmos `StateFlow`s do `AppViewModel` e `StudyPlanViewModel`. A tradução para modelos simples de apresentação permanecerá separada dos composables.

- Cobertura geral continuará sendo calculada a partir dos tópicos da competição ativa.
- Cobertura por matéria será construída para todas as `SubjectEntity` com tópicos, sem `take`, `subList` ou limite silencioso. A ordem preferencial será a ordem do edital (`position`), preservando o nome original.
- Previsão continuará vindo de `StudyPaceEvaluator` e dos dados reais do plano.
- Próxima atividade continuará sendo consumida do plano existente; não haverá alteração na engine nem na seleção persistida de tarefas.
- Desempenho só exibirá percentual quando houver base mínima de tentativas; caso contrário, exibirá o empty state.
- XP usará `ProgressSummary.xpIntoLevel` e `xpForNextLevel` para a leitura “dentro do nível / próximo nível”.

O modelo de cobertura por matéria poderá ganhar apenas campos de apresentação necessários, como posição do edital; não haverá migração de banco.

## Navegação

A navegação inferior continuará com `Início`, `Edital`, `Plano` e `Treinar`, pois são destinos de uso diário e não duplicam o conteúdo da Home. Perfil terá presença própria no avatar e no drawer. A Home não criará uma quinta aba de Perfil nem moverá o Plano para dentro dela.

O drawer seguirá abrigando revisões, erros, desempenho completo, conquistas, fontes, ajustes, notificações e ajuda. A top bar continuará acionando o drawer e o perfil; a busca existente será preservada ou reposicionada somente se necessário para o equilíbrio visual, sem remover seu destino.

## Estados e verificações visuais

Os previews e testes devem cobrir:

- nomes reais longos, incluindo `LÍNGUA PORTUGUESA (NÍVEL MÉDIO/SUPERIOR)` e `ANALISTA JUDICIÁRIO – ÁREA APOIO ESPECIALIZADO – TECNOLOGIA DA INFORMAÇÃO`;
- cinco ou mais matérias e mais de cem tópicos, com todas as matérias renderizadas;
- 0%, progresso intermediário e 100% de cobertura;
- sequência zero e sequência alta;
- ausência e presença de questões;
- ausência e presença de data de prova;
- sem plano, sem atividade hoje, tarefa em andamento e dia concluído;
- tema escuro e fonte ampliada;
- viewport próximo a 360 dp com título de duas ou três linhas;
- conteúdo rolando até o fim sem ser coberto pela NavigationBar.

Os testes automatizados devem proteger principalmente a política de lista completa, os empty states e a navegação em português. A inspeção visual final deve usar os previews e, quando disponível, um dispositivo/emulador próximo ao screenshot fornecido.

## Fora de escopo

- Alterar o algoritmo, a geração, a reprogramação ou a persistência do plano.
- Alterar o banco, o formato `.estudo`/`.plano` ou a importação/exportação.
- Criar uma nova aba de Perfil ou duplicar destinos no drawer.
- Adicionar gradientes aleatórios, glassmorphism, neon ou ilustrações decorativas à Home.
- Resolver o problema com padding fixo baseado na altura presumida da status bar.

## Critérios de aceitação

- Nenhum conteúdo da Home ou da top bar fica sob a status bar em aparelhos edge-to-edge.
- O fim da lista permanece acessível acima da NavigationBar em navegação gestual e de três botões.
- Todas as matérias recebidas para a competição ativa aparecem na seção de cobertura, sem “e mais …” morto.
- Nomes longos quebram de forma legível sem esmagar o percentual nem cortar o tópico principal de modo arbitrário.
- Na primeira dobra de uma tela próxima a 360 dp é possível reconhecer concurso, progresso/jornada e a ação Agora.
- A Home parece uma página pessoal do estudante, não uma lista de campos técnicos.
- O build do app passa e os testes relevantes passam; qualquer limitação de dispositivo conectado será reportada com o erro exato.
