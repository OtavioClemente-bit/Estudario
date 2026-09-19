# Prioridade de estudo — Plano de Implementação

> **Para agentes de implementação:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recomendado) ou superpowers:executing-plans para executar este plano tarefa por tarefa. Os passos usam checkboxes ([ ]) para acompanhamento.

**Objetivo:** implementar no Estudario prioridades de estudo explicáveis e editáveis para concurso, matéria, tópico e subtópico, preservando compatibilidade com dados, arquivos e planejador existentes.

**Arquitetura:** o domínio terá um modelo independente da prioridade legada e um único resolvedor para score, nível, herança e override manual. Room persistirá sugestão automática, evidências, disponibilidade da avaliação e override em cada entidade principal; o formato .estudo continuará na versão 2 com um bloco opcional de avaliação. A UI exibirá a prioridade de forma textual e editará somente o override, enquanto o planner continuará usando PlanPriority sem alteração silenciosa de distribuição.

**Tecnologias:** Kotlin, Android Compose, Room, org.json, JUnit, testes de migração Room e Gradle/Android.

**Especificação:** docs/superpowers/specs/2026-09-19-prioridade-estudo-design.md

## Restrições globais

- Score inteiro de 0 a 100: 81–100 Muito alta, 61–80 Alta, 41–60 Média, 21–40 Baixa e 0–20 Muito baixa.
- Confiança entre 0.0 e 1.0; representa confiança na classificação, não probabilidade de cobrança.
- Ausência de evidência produz avaliação neutra e não permite estatística, percentual, frequência ou ranking inventado.
- externalId existente deve ser preservado e nunca inventado a partir do nome durante a importação.
- Override manual nunca pode ser substituído por nova análise automática ou importação.
- Arquivos .estudo v1 e v2 sem priorityAssessment continuam válidos.
- Migração Room aditiva de 11 para 12, sem destructive migration.
- A importância do concurso não substitui PlanPriority nem altera StudyPlannerEngine nesta etapa.
- Verificação usa JDK 17 em C:\Users\otavi\.jdks\jbr-17.0.14.

## Foco de revisão

- Score 50 explícito não pode ser confundido com ausência de avaliação e herança do pai: testar em PriorityResolverTest.
- Importação parcial de conteúdo não pode apagar assessment ou override local: testar em EstudoPriorityImportTest.
- Backup antigo precisa continuar restaurável sem as novas colunas: testar em BackupPriorityTest.
- Enums desconhecidos e números fora dos limites não podem quebrar pacote utilizável nem chegar ao Room: testar em PriorityModelsTest e parser.
- A UI não pode comunicar prioridade apenas por cor e o planner não pode mudar: testar presentation e manter os testes atuais do planner.

---

### Tarefa 1: Modelos de domínio, normalização e resolução

**Arquivos:**
- Criar: app/src/main/java/br/com/estudario/domain/PriorityModels.kt
- Criar: app/src/main/java/br/com/estudario/domain/PriorityResolver.kt
- Criar: app/src/test/java/br/com/estudario/domain/PriorityModelsTest.kt
- Criar: app/src/test/java/br/com/estudario/domain/PriorityResolverTest.kt

**Interfaces produzidas:**
- PriorityLevel, PrioritySource, PriorityEvidenceType, PriorityEvidence, PriorityAssessment e PriorityState.
- PriorityResolver.scoreToLevel, normalizeScore, normalizeConfidence e effectivePriority.
- PriorityState contém hasAssessedPriority, assessment e userPriorityOverride.

A resolução deve ser: override manual; avaliação do próprio nó somente quando hasAssessedPriority for true; prioridade efetiva do pai; MEDIUM.

- [ ] Escrever testes das faixas e normalização:

~~~kotlin
@Test
fun scoreToLevelUsesClosedRanges() {
    assertEquals(PriorityLevel.VERY_LOW, PriorityResolver.scoreToLevel(0))
    assertEquals(PriorityLevel.VERY_LOW, PriorityResolver.scoreToLevel(20))
    assertEquals(PriorityLevel.LOW, PriorityResolver.scoreToLevel(21))
    assertEquals(PriorityLevel.LOW, PriorityResolver.scoreToLevel(40))
    assertEquals(PriorityLevel.MEDIUM, PriorityResolver.scoreToLevel(41))
    assertEquals(PriorityLevel.MEDIUM, PriorityResolver.scoreToLevel(60))
    assertEquals(PriorityLevel.HIGH, PriorityResolver.scoreToLevel(61))
    assertEquals(PriorityLevel.HIGH, PriorityResolver.scoreToLevel(80))
    assertEquals(PriorityLevel.VERY_HIGH, PriorityResolver.scoreToLevel(81))
    assertEquals(PriorityLevel.VERY_HIGH, PriorityResolver.scoreToLevel(100))
}
~~~

- [ ] Rodar os testes e confirmar falha:

~~~text
$env:JAVA_HOME='C:\Users\otavi\.jdks\jbr-17.0.14'; .\gradlew.bat testDebugUnitTest --tests 'br.com.estudario.domain.PriorityModelsTest' --tests 'br.com.estudario.domain.PriorityResolverTest' --no-daemon
~~~

Expected: falha porque os modelos ainda não existem.

- [ ] Implementar os enums e data classes, com score e confiança normalizados antes de persistir.
- [ ] Testar que score 50 explícito permanece MEDIUM, nó sem avaliação herda HIGH do pai, nó sem avaliação e sem pai retorna MEDIUM e override LOW vence avaliação VERY_HIGH.
- [ ] Rodar os testes direcionados e confirmar PASS.
- [ ] Commitar:

~~~text
git add app/src/main/java/br/com/estudario/domain app/src/test/java/br/com/estudario/domain
git commit -m "feat: add explainable study priority domain"
~~~

### Tarefa 2: Persistência Room e migração 11 para 12

**Arquivos:**
- Modificar: app/src/main/java/br/com/estudario/data/local/Entities.kt
- Modificar: app/src/main/java/br/com/estudario/data/local/Converters.kt
- Modificar: app/src/main/java/br/com/estudario/data/local/AppDatabase.kt
- Criar: app/src/main/java/br/com/estudario/data/local/PriorityEvidenceCodec.kt
- Modificar: app/src/androidTest/java/br/com/estudario/data/local/AppDatabaseMigrationTest.kt
- Criar: app/src/test/java/br/com/estudario/data/local/PriorityEvidenceCodecTest.kt

**Interfaces produzidas:**
As entidades CompetitionEntity, SubjectEntity e TopicEntity recebem:
- assessedPriorityScore: Int = 50
- assessedPrioritySource: PrioritySource = DEFAULT
- assessedPriorityConfidence: Float = 0f
- assessedPriorityRationale: String? = null
- assessedPriorityEvidenceJson: String = "[]"
- hasAssessedPriority: Boolean = false
- userPriorityOverride: PriorityLevel? = null

PriorityEvidenceCodec.encode(List<PriorityEvidence>): String e decode(String): List<PriorityEvidence> serão tolerantes a JSON vazio ou inválido. O DAO mantém os updates existentes.

- [ ] Escrever teste de round-trip do codec e de JSON inválido retornando lista vazia.
- [ ] Implementar codec com org.json, validando tipo, descrição e valor opcional; item inválido é ignorado.
- [ ] Adicionar converters para PrioritySource, PriorityLevel? e manter converters legados.
- [ ] Atualizar AppDatabase para versão 12 e registrar MIGRATION_11_12.
- [ ] Criar sete colunas por tabela com defaults SQL. Usar source DEFAULT, confidence 0, evidence [], hasAssessedPriority false e override NULL.
- [ ] Converter prioridade legada de topics: ALTA para score 70, NORMAL para 50 e BAIXA para 30; marcar hasAssessedPriority = 1. Concurso e matéria migrados ficam sem avaliação disponível.
- [ ] Testar schema 11 com IDs, externalId, progresso e prioridade legada; validar tudo após MIGRATION_11_12.
- [ ] Rodar:

~~~text
$env:JAVA_HOME='C:\Users\otavi\.jdks\jbr-17.0.14'; .\gradlew.bat testDebugUnitTest connectedDebugAndroidTest --no-daemon
~~~

Expected: testes unitários PASS; instrumentados PASS quando houver alvo conectado.

- [ ] Commitar:

~~~text
git add app/src/main/java/br/com/estudario/data/local app/src/androidTest/java/br/com/estudario/data/local/AppDatabaseMigrationTest.kt app/src/test/java/br/com/estudario/data/local/PriorityEvidenceCodecTest.kt
git commit -m "feat: persist study priority assessments"
~~~

### Tarefa 3: Formato .estudo, parser e importação sem perda de override

**Arquivos:**
- Modificar: app/src/main/java/br/com/estudario/data/transfer/EstudoPackageService.kt
- Não modificar: app/src/main/java/br/com/estudario/data/StudyRepository.kt; a importação existente já opera na transação do AppDatabase e deve preservar os campos com copy nomeado.
- Modificar: app/src/test/java/br/com/estudario/data/transfer/EstudoExamplesTest.kt
- Criar: app/src/test/java/br/com/estudario/data/transfer/EstudoPriorityImportTest.kt
- Não modificar: app/src/test/java/br/com/estudario/data/prompt/PromptBuildersTest.kt nesta tarefa; os contratos dos skeletons pertencem à Tarefa 4.

**Interfaces produzidas:**
- PriorityAssessmentInput interno ao transfer, com assessment e wasProvided.
- PackagePlan, SubjectPlan e TopicPlan terão assessment opcional; TopicPlan.priority legado continua existindo.
- ImportResult receberá contadores de avaliações normalizadas com valores default.
- Assessment presente atualiza somente sugestão automática; assessment ausente não modifica assessment ou override já salvos.
- Entidade nova sem assessment terá hasAssessedPriority = false.
- Prioridade legada explícita será usada somente como inicialização compatível, não para apagar assessment novo em entidade existente.

- [ ] Escrever testes de v1/v2 sem assessment, v2 com assessment em concurso/matéria/tópico/subtópico e valores inválidos.
- [ ] Rodar os testes direcionados e confirmar falha porque o parser ainda não conhece priorityAssessment.
- [ ] Implementar leitura com optJSONObject("priorityAssessment"), normalizando score e confiança e convertendo source/evidence desconhecidos para fallback neutro.
- [ ] Preservar se prioridade legada estava presente; não confundir campo ausente com NORMAL explícito.
- [ ] Importar cada assessment em seu próprio nível; não propagar assessment do pai para filhos.
- [ ] Escrever teste com tópico existente assessment HIGH e override LOW, importar assessment VERY_HIGH e verificar assessment atualizado, override LOW preservado e efetividade LOW.
- [ ] Escrever teste sem assessment e verificar que os campos automáticos existentes permanecem iguais.
- [ ] Validar IDs hierárquicos e externalId sem gerar identificador a partir de nome.
- [ ] Rodar:

~~~text
$env:JAVA_HOME='C:\Users\otavi\.jdks\jbr-17.0.14'; .\gradlew.bat testDebugUnitTest --tests 'br.com.estudario.data.transfer.EstudoPriorityImportTest' --tests 'br.com.estudario.data.transfer.EstudoExamplesTest' --no-daemon
~~~

Expected: PASS.

- [ ] Commitar:

~~~text
git add app/src/main/java/br/com/estudario/data/transfer/EstudoPackageService.kt app/src/main/java/br/com/estudario/data/StudyRepository.kt app/src/test/java/br/com/estudario/data/transfer app/src/test/java/br/com/estudario/data/prompt/PromptBuildersTest.kt
git commit -m "feat: import explainable priorities without losing overrides"
~~~

### Tarefa 4: Prompts de edital e conteúdo

**Arquivos:**
- Modificar: app/src/main/java/br/com/estudario/data/prompt/PromptBuilders.kt
- Modificar: app/src/main/java/br/com/estudario/data/preferences/PromptTemplates.kt somente nos templates persistidos usados pelo builder.
- Modificar: app/src/test/java/br/com/estudario/data/prompt/PromptBuildersTest.kt

- [ ] Escrever testes que exijam no prompt, nessa ordem: quantidade oficial; peso; pontuação; critério eliminatório; distribuição; histórico da banca/cargo; recorrência; relevância estrutural; inferência somente por último.
- [ ] Testar exigência de score 0..100, confidence 0.0..1.0, source permitido, rationale, evidence, MEDIUM sem evidência e proibição de estatísticas inventadas.
- [ ] Rodar o teste e confirmar falha nas novas asserções.
- [ ] Atualizar EditalPromptBuilder para gerar priorityAssessment opcional e manter prioridade legada no skeleton.
- [ ] Atualizar ContentPromptBuilder para copiar assessment local quando disponível e nunca recalcular importância genérica.
- [ ] Preservar o formato do prompt .plano.
- [ ] Rodar:

~~~text
$env:JAVA_HOME='C:\Users\otavi\.jdks\jbr-17.0.14'; .\gradlew.bat testDebugUnitTest --tests 'br.com.estudario.data.prompt.PromptBuildersTest' --no-daemon
~~~

Expected: PASS.

- [ ] Commitar:

~~~text
git add app/src/main/java/br/com/estudario/data/prompt/PromptBuilders.kt app/src/main/java/br/com/estudario/data/preferences/PromptTemplates.kt app/src/test/java/br/com/estudario/data/prompt/PromptBuildersTest.kt
git commit -m "feat: request evidence-based study priorities"
~~~

### Tarefa 5: Backup e restauração compatíveis

**Arquivos:**
- Modificar: app/src/main/java/br/com/estudario/data/transfer/BackupService.kt
- Criar: app/src/test/java/br/com/estudario/data/transfer/BackupPriorityTest.kt

**Interfaces produzidas:**
- Backup versão 6, leitura de versões 1 a 6.
- competitions, subjects e topics exportarão avaliação e override, incluindo hasAssessedPriority.
- Versões antigas recebem score 50, DEFAULT, confiança 0, evidence [], hasAssessedPriority false e override null.

- [ ] Escrever teste de export/import de score 88, source AI_INFERENCE, evidência e override LOW.
- [ ] Escrever teste de restauração de JSON versão 5 sem campos novos, conferindo defaults.
- [ ] Rodar testes e confirmar falha porque BackupService ainda aceita somente até a versão 5.
- [ ] Serializar os campos novos e ler campos opcionais com defaults.
- [ ] Usar argumentos nomeados ao reconstruir entidades com novos campos; preservar IDs, relações, campos legados e dados do planner.
- [ ] Rodar:

~~~text
$env:JAVA_HOME='C:\Users\otavi\.jdks\jbr-17.0.14'; .\gradlew.bat testDebugUnitTest --tests 'br.com.estudario.data.transfer.BackupPriorityTest' --no-daemon
~~~

Expected: PASS.

- [ ] Commitar:

~~~text
git add app/src/main/java/br/com/estudario/data/transfer/BackupService.kt app/src/test/java/br/com/estudario/data/transfer/BackupPriorityTest.kt
git commit -m "feat: preserve study priorities in backups"
~~~

### Tarefa 6: Repository, ViewModel e editor reutilizável

**Arquivos:**
- Modificar: app/src/main/java/br/com/estudario/data/StudyRepository.kt
- Modificar: app/src/main/java/br/com/estudario/ui/AppViewModel.kt
- Criar: app/src/main/java/br/com/estudario/ui/components/PriorityEditorDialog.kt
- Criar: app/src/main/java/br/com/estudario/ui/components/PriorityPresentation.kt
- Criar: app/src/test/java/br/com/estudario/ui/components/PriorityPresentationTest.kt

**Interfaces produzidas:**
- Repository: setCompetitionPriorityOverride(id: Long, override: PriorityLevel?), setSubjectPriorityOverride(id: Long, override: PriorityLevel?), setTopicPriorityOverride(id: Long, override: PriorityLevel?).
- AppViewModel expõe métodos públicos homônimos e usa launchCatching.
- PriorityPresentation.label(PriorityLevel) retorna “Muito alta”, “Alta”, “Média”, “Baixa” e “Muito baixa”.
- PriorityPresentation.sourceLabel(PrioritySource, hasOverride) retorna “Definida por você” ou “Sugerida automaticamente”.
- PriorityEditorDialog recebe estado e callbacks; não acessa Room.

- [ ] Escrever testes das cinco labels e da origem textual.
- [ ] Rodar e confirmar falha porque os tipos ainda não existem.
- [ ] Implementar repository/ViewModel alterando somente userPriorityOverride; nunca alterar assessedPriority.
- [ ] Implementar diálogo com cinco opções, “Usar sugestão automática”, “Por que esta prioridade?” e aviso de que é estimativa.
- [ ] Implementar detalhes com rationale, confiança formatada e evidências; ausência de evidência deve aparecer como estimativa neutra.
- [ ] Rodar:

~~~text
$env:JAVA_HOME='C:\Users\otavi\.jdks\jbr-17.0.14'; .\gradlew.bat testDebugUnitTest --tests 'br.com.estudario.ui.components.PriorityPresentationTest' --no-daemon
~~~

Expected: PASS.

- [ ] Commitar:

~~~text
git add app/src/main/java/br/com/estudario/data/StudyRepository.kt app/src/main/java/br/com/estudario/ui/AppViewModel.kt app/src/main/java/br/com/estudario/ui/components/PriorityEditorDialog.kt app/src/main/java/br/com/estudario/ui/components/PriorityPresentation.kt app/src/test/java/br/com/estudario/ui/components/PriorityPresentationTest.kt
git commit -m "feat: add priority override interaction layer"
~~~

### Tarefa 7: Exibição e edição no edital e no detalhe do tópico

**Arquivos:**
- Modificar: app/src/main/java/br/com/estudario/ui/screens/EditalScreen.kt
- Modificar: app/src/main/java/br/com/estudario/ui/screens/TopicDetailScreen.kt

- [ ] Exibir “Prioridade: nível” textualmente no concurso selecionado, SubjectCard, TopicRow e detalhe do tópico.
- [ ] Adicionar ação discreta no cabeçalho do concurso; adicionar “Prioridade” aos menus de matéria e tópico.
- [ ] Passar pai recursivo ao resolver, sem criar regra de prioridade local na tela.
- [ ] Adicionar teste de pai HIGH, filho sem assessment e neto LOW: filho herda HIGH e neto permanece LOW.
- [ ] Rodar:

~~~text
$env:JAVA_HOME='C:\Users\otavi\.jdks\jbr-17.0.14'; .\gradlew.bat testDebugUnitTest assembleDebug --no-daemon
~~~

Expected: PASS e APK em app/build/outputs/apk/debug/app-debug.apk.

- [ ] Commitar:

~~~text
git add app/src/main/java/br/com/estudario/ui/screens/EditalScreen.kt app/src/main/java/br/com/estudario/ui/screens/TopicDetailScreen.kt
git commit -m "feat: expose study priorities in syllabus UI"
~~~

### Tarefa 8: Adaptador do planner sem alterar o algoritmo

**Arquivos:**
- Criar: app/src/main/java/br/com/estudario/domain/planner/PriorityLevelAdapter.kt
- Criar: app/src/test/java/br/com/estudario/domain/planner/PriorityLevelAdapterTest.kt
- Não modificar: app/src/main/java/br/com/estudario/domain/planner/StudyPlannerEngine.kt

**Interface:** PriorityLevel.toPlanPriority(): PlanPriority com mapeamento VERY_HIGH→CRITICAL, HIGH→HIGH, MEDIUM→MEDIUM, LOW→LOW e VERY_LOW→LOW. O adaptador não será usado automaticamente nesta etapa.

- [ ] Escrever teste de todos os cinco mapeamentos.
- [ ] Implementar a extensão e rodar o teste direcionado.
- [ ] Rodar os testes existentes de br.com.estudario.domain.planner.* e confirmar que os resultados não mudaram.
- [ ] Commitar:

~~~text
git add app/src/main/java/br/com/estudario/domain/planner/PriorityLevelAdapter.kt app/src/test/java/br/com/estudario/domain/planner/PriorityLevelAdapterTest.kt
git commit -m "feat: add planner priority adapter"
~~~

### Tarefa 9: Integração, regressão e relatório de entrega

**Arquivos:**
- Criar: docs/superpowers/reports/2026-09-19-prioridade-estudo-implementation-report.md
- Não alterar testes anteriores nesta tarefa; qualquer ajuste de compatibilidade deve ser feito na tarefa que introduziu o contrato correspondente.

- [ ] Rodar suíte unitária completa:

~~~text
$env:JAVA_HOME='C:\Users\otavi\.jdks\jbr-17.0.14'; .\gradlew.bat testDebugUnitTest --no-daemon
~~~

Expected: PASS.

- [ ] Rodar instrumentados:

~~~text
$env:JAVA_HOME='C:\Users\otavi\.jdks\jbr-17.0.14'; .\gradlew.bat connectedDebugAndroidTest --no-daemon
~~~

Expected: PASS com dispositivo/emulador; se não houver alvo, registrar a indisponibilidade sem declarar PASS.

- [ ] Gerar APK:

~~~text
$env:JAVA_HOME='C:\Users\otavi\.jdks\jbr-17.0.14'; .\gradlew.bat assembleDebug --no-daemon
~~~

Expected: arquivo C:\Users\otavi\Documents\Codex\2026-09-15\vc-x20\app\build\outputs\apk\debug\app-debug.apk.

- [ ] Rodar git diff --check e confirmar AppDatabase.version = 12, MIGRATION_11_12 registrada e nenhuma mudança anterior de vídeo/tutorial revertida.
- [ ] Escrever relatório somente com resultados reais, comandos executados, limitações ambientais, arquivo APK e commits.
- [ ] Commitar:

~~~text
git add app/src/test docs/superpowers/reports/2026-09-19-prioridade-estudo-implementation-report.md
git commit -m "test: verify study priority feature"
~~~

## Cobertura da especificação

- Domínio, faixas, confiança e fallback: Tarefa 1.
- Entidades, codec e migração Room: Tarefa 2.
- Compatibilidade v1/v2, IDs, importação parcial e override: Tarefa 3.
- Evidências e proibição de estatísticas inventadas: Tarefa 4.
- Backup antigo/novo e override: Tarefa 5.
- Edição manual e explicação: Tarefas 6 e 7.
- Planner sem alteração: Tarefa 8.
- Testes, build e relatório: Tarefa 9.

## Dependências e ordem

Executar 1 → 2 → 3 → 4 → 5 → 6 → 7 → 8 → 9. Cada tarefa termina com teste e commit. Não iniciar implementação antes de o usuário revisar este plano e escolher o modo de execução.
