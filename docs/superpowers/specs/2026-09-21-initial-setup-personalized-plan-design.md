# Configuração inicial com plano personalizado e completo

## Objetivo

Fazer a primeira configuração produzir um plano que reflita a rotina e as dificuldades reais da pessoa, tanto no caminho automático quanto no caminho com IA externa. O plano usa o edital cadastrado, não uma lista genérica; cobre seu conteúdo no horizonte selecionado; e deixa claro quanto cabe por dia e o que significa a duração do bloco.

## Entendimento aprovado

- Em “Seu ritmo”, a pessoa ajusta os minutos de cada dia arrastando um controle, com valores legíveis como “2 h” e “2 h 15 min”, em vez de opções fixas como “120m”.
- A tela explica que o bloco é a unidade de duração das tarefas, não a disponibilidade total do dia.
- A pessoa informa a própria dificuldade por matéria. Esse sinal personaliza a distribuição sem reescrever a importância oficial sugerida pelo edital.
- O plano com IA cobre até a data da prova quando houver uma; sem data, gera um ciclo inicial de quatro semanas.
- O conteúdo e as prioridades são os mesmos dados reais usados pelo caminho automático. Um plano incompleto não é apresentado como completo.

## Diagnóstico do fluxo atual

- `AvailabilityStep` oferece apenas 0, 60, 120 ou 180 minutos por dia, e mostra valores não nulos no formato compacto “120m”.
- A etapa já permite selecionar o tamanho do bloco, mas não explica sua relação com as tarefas planejadas.
- O caminho automático chama o serviço de plano existente com disponibilidade e perfil, sem uma avaliação pessoal persistida para cada matéria.
- O caminho de IA monta `PlanSubjectInfo` apenas com nomes de matérias, tópicos vazios e prioridade padrão; não envia a duração do bloco. O construtor de prompt já aceita tópicos e prioridades, mas essa tela não os fornece.
- O horizonte padrão do prompt é de quatro semanas, mesmo quando a prova está mais distante.
- A importação de `.plano` valida estrutura e referências, mas a configuração inicial ainda não confere se todos os tópicos fornecidos receberam tarefas.

## Experiência proposta

### Ritmo e blocos

- Cada dia da semana tem um `Slider` de 15 em 15 minutos. Zero é apresentado como “Folga”; os demais valores usam horas e minutos em português. O total semanal fica visível e é recalculado durante o arraste.
- Manter compatibilidade com o intervalo já aceito pelo domínio (0–1.440 minutos por dia), sem descartar valores existentes. O controle continua acessível por toque e leitor de tela, além do arraste.
- As opções de bloco existentes passam a usar rótulos “25 min”, “45 min” etc.
- Abaixo delas, exibir: “O bloco é o tamanho-base de cada tarefa do plano. Não é o total de estudo do dia; as tarefas usam múltiplos do bloco dentro do tempo disponível.”

### Dificuldade por matéria

- Depois de carregar as matérias do edital, mostrar cada uma em uma lista curta com três escolhas: “Tenho facilidade”, “Intermediária” e “Tenho dificuldade”.
- Persistir as escolhas no estado recuperável do onboarding, vinculadas aos IDs das matérias. Uma matéria nova começa como “Intermediária”; matérias removidas do edital deixam de participar do rascunho.
- Traduzir dificuldade para um sinal de planejamento baixo/médio/alto. A prioridade oficial e sua evidência continuam intactas; para a distribuição do plano, o sinal efetivo é o mais alto entre a prioridade oficial normalizada e a dificuldade informada. Assim, a pessoa pode elevar uma matéria difícil sem apagar a relevância oficial da prova.
- A revisão final mostra a disponibilidade por dia, o bloco e as prioridades escolhidas, permitindo voltar e ajustar.

### Plano automático e plano por IA

- Ambos recebem as mesmas matérias, tópicos, prioridades efetivas, disponibilidade diária, bloco, perfil e data de prova.
- Para IA externa, construir `PlanSubjectInfo` com a árvore real do edital e as prioridades efetivas, e incluir bloco e horizonte em `PlanPromptOptions`. Não inventar nomes, IDs ou tópicos.
- Com data de prova, o fim do cronograma gerado é essa data; sem data, usar quatro semanas a partir do início. O `.plano` contém fases e metas coerentes com o horizonte, além de tarefas vinculadas a matérias/tópicos reais para estudo novo e consolidação (questões, revisão e recuperação ativa) conforme conteúdo e perfil.
- Antes de aceitar o arquivo, comparar tarefas com os IDs do edital fornecido. Todo tópico disponível deve aparecer em ao menos uma tarefa no horizonte; se a matéria não tiver tópicos cadastrados, ela ainda deve receber tarefa vinculada por ID/nome válido. Também validar referências, datas, capacidade diária, tamanho de bloco e estrutura do arquivo.
- Se faltar cobertura, não concluir a configuração como plano completo. Mostrar a lista do que falta e oferecer gerar/importar novamente ou usar o plano automático; preservar o arquivo e o rascunho atuais até uma opção válida ser confirmada.
- Se a capacidade declarada não comportar a cobertura no horizonte, informar a limitação e permitir ajustar disponibilidade ou corrigir o edital, sem encurtar silenciosamente o plano nem inventar carga horária.

## Persistência e compatibilidade

- Evoluir `InitialSetupSnapshot` e seu codec de forma retrocompatível. Snapshots antigos carregam matérias existentes com dificuldade neutra e preservam a disponibilidade já salva.
- Não alterar os campos oficiais de prioridade do edital nem introduzir migração Room só para guardar respostas temporárias do onboarding. A prioridade efetiva passa ao plano por sua configuração existente.
- Manter o formato `.plano` atual, salvo se a inspeção de implementação encontrar uma lacuna indispensável; qualquer mudança deverá manter leitura de arquivos existentes.

## Testes e critérios de aceite

1. Arrastar para 0, 15, 60, 120 e valores acima de 180 minutos atualiza/persiste o dia correto; rótulos e total semanal são formatados corretamente.
2. Snapshots antigos continuam decodificando sem perda de disponibilidade e recebem prioridade neutra.
3. Dificuldade eleva a prioridade efetiva sem modificar a avaliação oficial; uma prioridade oficial mais alta é preservada.
4. O prompt inclui todas as matérias/tópicos reais, prioridade, disponibilidade, bloco e horizonte; não contém listas fictícias.
5. Com data de prova distante, o cronograma termina na prova; sem data, cobre quatro semanas.
6. O validador lista matéria/tópico omitido, rejeita IDs inexistentes e não conclui setup com arquivo incompleto.
7. O caminho automático continua criando um plano real e usa as mesmas escolhas de dificuldade e disponibilidade.
8. Testar codec, mapeamento, horizonte, prompt, cobertura, acessibilidade dos controles e regressão de importação/criação automática.

## Fora de escopo

- Executar modelos de IA dentro do app. O caminho continua usando IA externa e importação validada.
- Alterar o avaliador de desempenho geral ou a navegação do menu lateral; esses itens têm especificação própria.
