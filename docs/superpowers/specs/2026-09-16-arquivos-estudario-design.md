# Arquivos do Estudario — Design da Pipeline Única de Importação

## Objetivo

Transformar a importação em uma funcionalidade única do aplicativo, capaz de receber arquivos pelo seletor interno ou por Intents do Android, identificar o formato pelo conteúdo, resolver vínculos com dados locais, importar de forma transacional e navegar para o conteúdo resultante.

O fluxo deve permitir tocar em um arquivo compatível em Downloads, Google Files, gerenciadores de fabricantes, mensageiros, e-mail, Drive ou navegador e abri-lo diretamente no Estudario. Os botões internos atuais continuam existindo, mas passam a usar a mesma pipeline.

## Arquitetura encontrada

O projeto é um aplicativo Android nativo com Kotlin 2.2.20, Java 17, Jetpack Compose, Material 3, Navigation Compose, ViewModel, Flow, coroutines, Room/SQLite, DataStore e WorkManager. O banco está na versão 5.

As responsabilidades atuais estão distribuídas desta forma:

- `MainActivity` lê `ACTION_VIEW` por `ContentResolver` e escolhe um fluxo com `IncomingFileFormat`;
- `IncomingFileFormat` detecta formatos pela raiz JSON;
- `IncomingFileCoordinator` encaminha somente planos externos para a interface;
- `EstudoPackageService` contém parser, prévia e importação transacional de `.estudo`;
- `StudyPlanCodec`, `StudyPlanValidation`, `StudyPlanImportResolver` e `StudyPlanTransferService` cuidam de `.plano`;
- `BackupService` e `PlannerBackupCodec` exportam e restauram o backup completo em uma transação;
- `MoreScreen` e `PlanScreen` possuem seletores internos que leem os arquivos separadamente;
- `EstudarioApp` observa planos externos e navega para a área de planos.

Já existe suporte parcial a abertura externa, mas ele não cobre `ACTION_SEND`, não considera nome/extensão do arquivo, não centraliza os seletores internos, não representa conflitos de resolução e não encaminha todos os resultados pela mesma máquina de estados.

## Formatos reais

### Pacote de estudo

- extensão de produto: `.estudo`;
- versões aceitas: 1 e 2;
- identidade para repetição: `packageId`;
- assinaturas atuais: estrutura legada com `schemaVersion` e `competition`, estrutura v1, estrutura hierárquica v2 e estrutura simples v2 reconhecidas pelo parser existente;
- importação: transacional, com modos existentes `SKIP`, `UPDATE` e `COPY`.

### Plano de estudos

- extensão: `.plano`;
- formato: `estudario-plano`;
- versão aceita: 1;
- identidade: `planId` UUID;
- importação: transacional, com modos existentes `CREATE`, `MERGE` e `REPLACE_FUTURE`.

### Backup completo

- arquivo JSON exportado atualmente com nome `estudario-backup-<data>.json`;
- formato: `estudario-backup`;
- versões aceitas: 1 a 5;
- restauração: substitui o banco local dentro de transação.

O backup entra no detector e no coordenador comum, mas sempre sai para um fluxo separado de restauração com confirmação explícita. Abrir um backup externamente nunca inicia restauração automática.

Não foram encontrados outros formatos próprios importáveis ou exportáveis. Os arquivos de contexto do plano em `.json` e `.txt` são exportações para consulta ou IA, não formatos importáveis.

## Causa do erro de `externalId`

`StudyPlanCodec.decode` chama `requireText` para `concurso.externalId` e para os IDs das prioridades. Em seguida, ainda dentro do codec, `StudyPlanValidation.validate` exige esses valores novamente. O `StudyPlanImportResolver` só é executado depois do decode e procura exclusivamente pelos IDs fornecidos.

Assim, uma referência vazia é rejeitada antes de qualquer tentativa de localizar o concurso pelo nome. A correção será separar validação estrutural, resolução de vínculos e validação semântica final.

## Arquitetura proposta

```text
ACTION_VIEW / ACTION_SEND / seletor interno
                    ↓
          EstudarioFileCoordinator
                    ↓
       leitura segura via ContentResolver
                    ↓
           EstudarioFileDetector
       nome + extensão + MIME + JSON
                    ↓
     handler do formato detectado
       ├── EstudoFileHandler
       ├── StudyPlanFileHandler
       └── BackupFileHandler
                    ↓
       parser estrutural específico
                    ↓
 ImportLinkResolver, quando necessário
                    ↓
        validação semântica final
                    ↓
   decisão automática ou interação
                    ↓
     serviço transacional existente
                    ↓
       navegação + mensagem curta
```

Os nomes finais podem ser ajustados ao padrão do projeto. As fronteiras de responsabilidade devem permanecer equivalentes.

### Fonte do arquivo

O coordenador receberá um objeto com:

- `Uri`;
- nome exibido obtido por `OpenableColumns.DISPLAY_NAME`, quando disponível;
- MIME informado pelo `ContentResolver` ou Intent;
- origem: seletor interno, `ACTION_VIEW` ou `ACTION_SEND`;
- intenção esperada opcional do botão interno, como “importar plano”.

O conteúdo será lido exclusivamente por `ContentResolver.openInputStream`. Nenhum fluxo dependerá de caminho físico, acesso amplo ao armazenamento ou `MANAGE_EXTERNAL_STORAGE`.

O leitor imporá um limite de tamanho coerente com os formatos locais para evitar carregar entradas arbitrariamente grandes em memória. Falhas de abertura, leitura e codificação produzirão mensagens compreensíveis.

### Detector de formato

O detector produzirá uma classificação ou um erro tipado. A extensão e o MIME servem como sinais; a assinatura JSON é a autoridade final.

- `.plano` exige `format: "estudario-plano"` e uma versão reconhecível;
- backup exige `format: "estudario-backup"`;
- `.estudo` usa exatamente as raízes aceitas pelo parser atual, incluindo compatibilidade legada;
- JSON sintaticamente inválido é rejeitado antes do handler;
- extensão própria com conteúdo de outro formato é rejeitada como divergência;
- MIME genérico é aceito somente quando nome ou conteúdo identifica inequivocamente um formato suportado;
- JSON sem extensão própria pode ser aceito pelo seletor interno quando a assinatura é inequívoca;
- um arquivo arbitrário não se torna importável apenas por usar uma extensão conhecida.

O detector não validará todas as regras internas do formato; ele apenas identifica com segurança o handler correto.

### Registro de handlers

Uma coleção pequena de handlers evitará condicionais crescentes na Activity. Cada handler declara:

- as assinaturas que reconhece;
- como fazer o parse estrutural;
- se precisa resolver vínculos;
- como validar semanticamente;
- como detectar repetição ou conflito;
- qual serviço efetua a persistência;
- o destino de navegação após sucesso.

Isso será uma abstração leve, sem sistema dinâmico de plugins. Adicionar um formato futuro exigirá adicionar um handler e registrá-lo no coordenador.

`MainActivity` ficará responsável apenas por extrair o URI do Intent e entregar a fonte ao coordenador. Ela não conhecerá parsers nem regras de importação.

## Intents e associação Android

O Manifest aceitará `ACTION_VIEW` e, quando o provedor compartilhar uma URI de arquivo, `ACTION_SEND`. `CATEGORY_DEFAULT` será usada; `CATEGORY_BROWSABLE` só será mantida se necessária para uma origem web verificável, pois `content://` de gerenciadores de arquivo não a exige.

Os filtros serão restritos tanto quanto as limitações do Android permitirem. Não será registrado um filtro amplo para todo `text/plain`, `application/json` ou `application/octet-stream` sem restrições que faça o app aparecer para documentos não relacionados. Como filtros por extensão são pouco confiáveis com `content://`, a associação inicial será conservadora e a decisão definitiva acontecerá em runtime.

Quando um provedor entregar MIME genérico, o coordenador usará nome exibido e assinatura JSON. O app aceitará permissão temporária de leitura concedida no Intent; não solicitará permissões amplas de armazenamento.

`ACTION_SEND` aceitará uma única URI em `Intent.EXTRA_STREAM`. Texto compartilhado sem arquivo e múltiplos anexos ficam fora deste escopo inicial.

## Parse e validação do plano

O plano terá duas representações:

1. uma representação estrutural importável, na qual referências externas podem estar ausentes;
2. `StudyPlanFileV1`, já resolvido, usado pela validação semântica, prévia, exportação e persistência existentes.

O parser estrutural continuará exigindo:

- JSON válido;
- `format` e `version` suportados;
- `planId`, datas, números, enums e coleções com tipos válidos;
- nomes necessários para qualquer referência que precise de fallback;
- IDs estruturais de fases, semanas e tarefas válidos e não duplicados.

Ele não exigirá `externalId` antes da resolução. Após a resolução, o DTO final passará pelas regras completas de `StudyPlanValidation`.

## Resolução de vínculos

`ImportLinkResolver` centralizará a resolução de concurso, matéria e tópico. A normalização será usada somente como chave de comparação:

1. `trim`;
2. conversão para minúsculas com locale estável;
3. remoção de diacríticos;
4. colapso de sequências de espaços.

O texto persistido nunca será alterado por essa normalização. A comparação será de igualdade normalizada, sem similaridade aproximada.

### Estados de uma referência

O resolvedor distinguirá:

- `PresentAndMatched`: ID informado e registro correto encontrado;
- `MissingResolvedByName`: ID ausente e nome com correspondência única;
- `DivergentResolvedByName`: ID informado, mas inexistente ou incompatível, e nome com correspondência única;
- `Ambiguous`: mais de uma correspondência dentro do contexto permitido;
- `NotFound`: nenhuma correspondência;
- `MatchedWithoutOfficialExternalId`: registro por nome existe, mas não possui ID oficial;
- `ContextMismatch`: ID existe, mas pertence a concurso ou matéria incompatível.

Um ID informado que não corresponda ao banco é uma divergência registrada. A resolução por nome único pode ser proposta, porém nunca substitui silenciosamente um ID divergente em caso ambíguo. A UI apresentará a divergência e o vínculo encontrado antes de qualquer persistência sempre que a troca puder alterar a identidade pretendida.

### Concurso

1. Se o `externalId` existe e localiza um concurso, ele é usado.
2. Se está ausente, o nome é procurado entre os concursos por igualdade normalizada.
3. Se foi informado, mas não existe, o nome também pode produzir uma correspondência única, marcada como divergência.
4. Correspondência única só é utilizável se o concurso local tiver `externalId` oficial.
5. Correspondência ambígua exige escolha explícita entre registros que tenham ID oficial.
6. Sem correspondência, o usuário pode escolher um concurso existente com ID oficial ou receber orientação para importar/cadastrar o concurso pelo mecanismo oficial.
7. Um concurso local sem `externalId` não conclui a resolução do plano.

### Matéria

1. O contexto obrigatório é o concurso resolvido.
2. Um ID encontrado fora desse concurso resulta em `ContextMismatch`.
3. Sem ID, ou com ID divergente, `materiaNome` é comparado apenas com matérias do concurso.
4. Correspondência única exige `externalId` oficial.
5. Ambiguidade exige escolha; ausência pode permitir omitir apenas vínculos opcionais de tarefas. Prioridades obrigatórias do plano precisam ser resolvidas antes da importação.

### Tópico

1. O contexto obrigatório é a matéria resolvida para a tarefa.
2. Um tópico encontrado em outra matéria nunca é aceito.
3. Sem ID, ou com ID divergente, `topicoNome` é comparado somente com tópicos daquela matéria.
4. Correspondência única exige `externalId` oficial.
5. Como `topicId` é opcional no modelo de tarefa, ausência sem correspondência pode permanecer sem vínculo após o usuário revisar o caso; ambiguidade nunca é resolvida automaticamente.

### Origem de IDs válidos

IDs criados por mecanismos oficiais do Estudario são válidos. Isso inclui IDs recebidos e persistidos por importadores oficiais, ou criados por uma função oficial que tenha uma política explícita de geração de identidade estável.

É proibido derivar um ID do nome, gerar um UUID ou fabricar qualquer identificador durante a resolução apenas para fazer o arquivo passar. A criação de uma entidade e a atribuição de sua identidade devem ocorrer somente por um mecanismo oficial separado, auditável e coerente com o modelo do app.

## Máquina de estados da importação

O coordenador exporá uma máquina de estados observável pela interface:

- `Idle`;
- `Reading` com nome curto do arquivo;
- `Resolving`;
- `NeedsLinkChoice` com uma ou mais referências pendentes;
- `NeedsDuplicateDecision`;
- `NeedsDestructiveConfirmation` para backup e substituições;
- `Importing`;
- `Success` com destino e mensagem;
- `Error` com mensagem pública e causa técnica preservada para log.

Ao abrir externamente um arquivo novo e completamente resolvível, a importação seguirá automaticamente até o sucesso. Não haverá uma tela intermediária apenas para confirmar uma importação não destrutiva.

Interação será exigida para ambiguidade, divergência de identidade relevante, ausência de vínculo obrigatório, duplicidade, substituição, restauração de backup, versão incompatível ou arquivo inválido.

## Duplicidade e conflitos

### Plano

O `planId` continua sendo a identidade. Se já existir:

- nenhuma alteração é aplicada automaticamente;
- a UI oferece abrir o existente;
- `MERGE`, `REPLACE_FUTURE` ou criação de cópia exigem escolha explícita;
- histórico, execuções, tarefas concluídas e bloqueios preservam as regras atuais.

Criar cópia deve usar o mecanismo oficial existente para remapear IDs, não alterar o arquivo de entrada informalmente.

### Estudo

O `packageId` e o histórico de `ImportPackageEntity` continuam sendo a identidade. Pacote novo e sem conflito pode ser importado automaticamente no modo seguro atual. Pacote já conhecido exige a decisão atual entre atualizar, criar cópia ou cancelar.

### Backup

Backup nunca é considerado uma atualização comum. Ele sempre abre a prévia/confirmador de restauração e informa que os dados locais serão substituídos.

## Transações e consistência

Os serviços atuais já usam `RoomDatabase.withTransaction` para `.estudo`, `.plano` e backup. Essa responsabilidade continuará nos serviços, não no coordenador de UI.

A pipeline fará leitura, detecção, parse, resolução e validação antes de abrir a transação. A transação conterá somente a persistência necessária. Qualquer falha crítica causará rollback completo.

Escolhas de vínculo serão convertidas em um objeto de resolução imutável antes da persistência. O serviço revalidará os registros escolhidos dentro da operação para reduzir risco de referências obsoletas.

## Navegação e mensagens

Após sucesso:

- `.plano`: navegar para `plan`, selecionar ou abrir o plano importado e mostrar “Plano importado com sucesso.”;
- `.estudo`: navegar para a área mais específica disponível, preferindo o tópico importado quando houver um único destino; caso contrário, abrir o edital e mostrar “Estudo importado com sucesso.”;
- backup: após confirmação e restauração concluída, reconstruir o estado da aplicação e mostrar “Backup restaurado com sucesso.”.

Mensagens públicas serão curtas e orientadas à ação. Exceções técnicas permanecerão disponíveis em logs de desenvolvimento.

Exemplos:

- ambiguidade: “Encontramos mais de um concurso compatível. Escolha a qual este plano pertence.”;
- inexistente: “Não encontramos este concurso no aplicativo. Importe ou cadastre o concurso com um identificador oficial.”;
- formato incorreto: “Este arquivo não parece ser um Plano do Estudario.”;
- versão futura: “Este plano foi criado em uma versão de formato ainda não suportada.”;
- divergência: “O identificador informado não existe neste aparelho, mas encontramos um concurso com o mesmo nome. Revise o vínculo antes de continuar.”.

## Compatibilidade dos fluxos atuais

Os launchers de `MoreScreen` e `PlanScreen` entregarão a URI ao mesmo coordenador usado por `MainActivity`. Eles poderão fornecer uma expectativa de formato para melhorar a mensagem quando o usuário escolher o arquivo errado, mas não executarão parse ou importação diretamente.

O parser e os serviços existentes serão preservados onde suas responsabilidades já estão corretas. A mudança do codec de plano será compatível com arquivos válidos já exportados. Nenhuma migração de banco é prevista, pois a resolução produz os IDs externos já esperados e o plano persiste relações por IDs locais.

## Segurança e privacidade

- nenhuma permissão ampla de armazenamento;
- nenhuma resolução de caminho físico de `content://`;
- nenhuma importação baseada somente na extensão;
- nenhum ID inventado durante importação;
- nenhum vínculo aproximado ou fora do contexto;
- nenhum overwrite silencioso;
- nenhuma restauração automática de backup;
- limite de leitura e tratamento de streams inválidos;
- Intents limitados a uma URI e aos formatos suportados.

## Estratégia de testes

### Testes unitários

- detector com `.plano`, `.estudo`, backup e assinaturas legadas reais;
- divergência entre extensão e conteúdo;
- MIME genérico com assinatura válida;
- JSON inválido e formato desconhecido;
- versões incompatíveis com mensagens por formato;
- normalização de espaços, caixa e acentos sem alterar os nomes;
- ID correto;
- ID ausente com nome único;
- ID informado e inexistente com nome único, marcado como divergência;
- nome ambíguo;
- registro sem `externalId` oficial;
- matéria limitada ao concurso;
- tópico limitado à matéria;
- nomes de tópicos iguais em matérias diferentes;
- DTO estrutural de plano com referências ausentes;
- validação final rejeitando referências obrigatórias não resolvidas;
- estado de duplicidade por `planId` e `packageId`.

### Testes instrumentados

- `.plano` válido com ID correto;
- resolução e importação por nome único;
- falha de persistência com rollback;
- URI `content://` entregue por `ACTION_VIEW`;
- URI em `ACTION_SEND`;
- importação manual usando a mesma pipeline;
- `.estudo` externo usando o serviço existente;
- backup externo abrindo confirmação sem restaurar;
- navegação para plano e tópico/edital após sucesso;
- repetição do mesmo `planId` sem duplicação.

Para testar `ContentResolver`, será usado um provedor de teste ou documento temporário exposto como `content://`, evitando caminhos físicos artificiais.

### Verificação final

- `testDebugUnitTest`;
- testes instrumentados com dispositivo ou emulador disponível;
- `lintDebug`;
- `assembleDebug`;
- `bundleRelease` somente se a entrega exigir novo artefato de publicação;
- inspeção final do Manifest e dos filtros resolvidos;
- teste manual com Downloads e ao menos uma origem de compartilhamento.

## Arquivos e áreas previstos

As alterações devem ficar concentradas em:

- `AndroidManifest.xml`;
- `MainActivity.kt`;
- pacote `data.transfer` para fonte, detector, coordenador, handlers e estados;
- pacote `data.transfer.planner` para parse estrutural, resolução e validação em fases;
- `EstudarioApplication` para disponibilizar dependências compartilhadas;
- `AppViewModel`, `StudyPlanViewModel` e `EstudarioApp` para observar estados e navegar;
- `MoreScreen` e `PlanScreen` para usar a entrada comum;
- testes unitários e instrumentados de transferência.

Não haverá refatoração de domínio, motor de planejamento ou telas sem relação direta com importação.

## Critérios de aceitação

O trabalho estará concluído quando:

1. tocar em `.plano` válido via `content://` abrir o Estudario;
2. o app identificar o formato pelo JSON e recusar discrepâncias;
3. um concurso sem ID no arquivo for resolvido pelo nome para um único registro com ID oficial;
4. matéria e tópico forem resolvidos somente em seus contextos;
5. ambiguidades e divergências pedirem decisão;
6. nenhuma identidade for fabricada;
7. plano novo for importado sem confirmação redundante;
8. `planId` repetido nunca gerar duplicação silenciosa;
9. `.estudo` externo usar a mesma entrada e preservar a semântica atual;
10. backup externo exigir confirmação destrutiva;
11. `ACTION_SEND` com URI única funcionar;
12. seletores internos usarem a mesma pipeline;
13. falhas críticas deixarem o banco inalterado;
14. o destino correto abrir após sucesso;
15. testes, lint e compilação passarem.

## Limitações intencionais

- múltiplos anexos em um único `ACTION_SEND_MULTIPLE` não serão aceitos inicialmente;
- texto JSON compartilhado sem URI não será importado;
- comparação por nome será exata após normalização, sem heurística de similaridade;
- um registro local sem `externalId` oficial não poderá satisfazer vínculo obrigatório de `.plano`;
- o backup continuará usando `.json`, sem nova extensão inventada;
- associação perfeita por extensão depende do gerenciador de arquivos; a validação em runtime garante segurança mesmo quando o MIME é genérico.
