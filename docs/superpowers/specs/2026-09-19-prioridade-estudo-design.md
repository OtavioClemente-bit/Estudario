# Prioridade de estudo por concurso, matéria e tópico

## Objetivo

Adicionar ao Estudario uma avaliação de importância de estudo para o concurso, cada matéria e cada tópico/subtópico, sem confundir importância para a prova com desempenho pessoal. A avaliação automática será fundamentada em evidências declaradas pela IA, terá score e confiança validados pelo aplicativo, e poderá ser substituída por uma decisão manual do usuário.

O sistema deve continuar funcionando sem internet depois que a avaliação for salva, preservar `externalId` já existente e não alterar o comportamento atual do planejador nesta primeira etapa.

## Estado atual relevante

- `CompetitionEntity` e `SubjectEntity` possuem `externalId`, mas não possuem prioridade.
- `TopicEntity` possui o enum legado `Priority` com `BAIXA`, `NORMAL` e `ALTA`.
- O `EstudoPackageParser` aceita arquivos `.estudo` v1 e v2, tópicos recursivos e o campo legado `prioridade`.
- `EstudoPackageService` atualiza a prioridade legada do tópico durante importações.
- O Room está na versão 11 e utiliza migrações explícitas.
- `BackupService` exporta e restaura a prioridade legada dos tópicos.
- A UI principal do edital lista matérias e tópicos, mas não exibe nem edita prioridade.
- O planner usa `PlanPriority` em entidades e modelos próprios. Essa prioridade é configuração do plano e não deve ser substituída automaticamente pela importância do concurso.

## Decisões de arquitetura

### Modelo de prioridade

Criar no domínio:

```kotlin
enum class PriorityLevel {
    VERY_HIGH, HIGH, MEDIUM, LOW, VERY_LOW
}

enum class PrioritySource {
    OFFICIAL_EXAM_STRUCTURE,
    HISTORICAL_EVIDENCE,
    AI_INFERENCE,
    DEFAULT,
    USER,
}

enum class PriorityEvidenceType {
    OFFICIAL_QUESTION_COUNT,
    OFFICIAL_WEIGHT,
    OFFICIAL_SCORE,
    ELIMINATION_CRITERION,
    OFFICIAL_DISTRIBUTION,
    BOARD_HISTORY,
    ROLE_OR_AREA_HISTORY,
    TOPIC_RECURRENCE,
    STRUCTURAL_RELEVANCE,
    ABSENCE_OF_EVIDENCE,
}
```

O score válido será um inteiro entre 0 e 100. O nível será sempre derivado por uma única função de domínio:

- 81–100: `VERY_HIGH`
- 61–80: `HIGH`
- 41–60: `MEDIUM`
- 21–40: `LOW`
- 0–20: `VERY_LOW`

Valores recebidos de arquivos serão normalizados para o intervalo válido. Valores fora do intervalo ou enums desconhecidos não poderão chegar ao banco; a entrada será convertida para avaliação neutra (`score = 50`, `MEDIUM`, `DEFAULT`, confiança `0.0`) e o importador registrará a decisão de fallback no resultado de importação quando a infraestrutura atual permitir.

O domínio terá uma representação de avaliação equivalente a:

```kotlin
data class PriorityEvidence(
    val type: PriorityEvidenceType,
    val description: String,
    val value: Double? = null,
)

data class PriorityAssessment(
    val score: Int,
    val source: PrioritySource,
    val confidence: Float,
    val rationale: String?,
    val evidence: List<PriorityEvidence>,
) {
    val level: PriorityLevel get() = PriorityLevel.fromScore(score)
}
```

`confidence` será normalizada para 0.0..1.0 e representará confiança na classificação, não probabilidade de cobrança na prova.

### Persistência

Para evitar uma tabela polimórfica sem chaves estrangeiras reais, os campos de avaliação serão adicionados às entidades existentes `CompetitionEntity`, `SubjectEntity` e `TopicEntity`:

- `assessedPriorityScore: Int` com default 50;
- `assessedPrioritySource: PrioritySource` com default `DEFAULT`;
- `assessedPriorityConfidence: Float` com default 0f;
- `assessedPriorityRationale: String?`;
- `assessedPriorityEvidenceJson: String` com default `[]`;
- `hasAssessedPriority: Boolean` com default `false`;
- `userPriorityOverride: PriorityLevel?`.

O campo legado `TopicEntity.priority: Priority` será mantido para compatibilidade de código e de arquivos. A avaliação nova será a fonte principal do novo fluxo. Na migração, `ALTA`, `NORMAL` e `BAIXA` legados serão convertidos respectivamente para scores aproximados 70, 50 e 30, com fonte `DEFAULT`, sem criar override manual.

`hasAssessedPriority` diferencia “não há avaliação neste nó” de “há uma avaliação neutra com score 50”. Ele será `true` quando uma avaliação vier do arquivo, da análise automática ou da conversão determinística da prioridade legada; será `false` para entidades novas sem avaliação e para backups antigos sem esses campos.

O domínio não dependerá de getter importante em entidade Room. Mappers/helpers centralizados montarão `PriorityAssessment` e resolverão:

```text
effectivePriority(node, parent) =
    userPriorityOverride
    ?: if (node.hasAssessedPriority) node.assessedPriority.level else null
    ?: parent.effectivePriority
    ?: MEDIUM
```

Assim, um score 50 recebido explicitamente continua sendo `MEDIUM`, enquanto um nó sem avaliação pode herdar a prioridade efetiva do pai. O override manual sempre vence a avaliação e a herança.

### Migração Room

Atualizar a versão do `AppDatabase` de 11 para 12 e criar `MIGRATION_11_12` com `ALTER TABLE` aditivo nas três tabelas. Nenhum dado será removido e nenhuma migração destrutiva será usada.

O teste de migração criará um banco v11 com concurso, matéria e tópico existentes, aplicará a migração e verificará:

- nomes, IDs, `externalId` e progresso preservados;
- score neutro e campos de evidência preenchidos com defaults;
- prioridade legada convertida de modo determinístico;
- `hasAssessedPriority` definido como `true` quando a prioridade legada for convertida;
- `userPriorityOverride` nulo.

### `.estudo` e importação

O formato continuará na versão 2 para manter compatibilidade. Cada objeto `concurso`, matéria e tópico poderá receber:

```json
"priorityAssessment": {
  "score": 88,
  "source": "OFFICIAL_EXAM_STRUCTURE",
  "confidence": 0.87,
  "rationale": "Peso oficial informado no edital.",
  "evidence": [
    {
      "type": "OFFICIAL_WEIGHT",
      "description": "Disciplina com maior peso no bloco específico.",
      "value": 2
    }
  ]
}
```

O campo legado `prioridade` continuará sendo aceito e será emitido nos esqueletos quando necessário para compatibilidade com versões anteriores. Quando `priorityAssessment` existir, o score será a fonte de verdade e o nível será derivado pelo app. Um `level` recebido pela IA, se presente, será ignorado ou validado contra o score; nunca haverá duas regras de conversão.

Regras do parser:

- arquivos v1 e v2 sem `priorityAssessment` continuam válidos;
- ausência de avaliação em uma importação de conteúdo não substitui a avaliação já salva;
- avaliação nova atualiza apenas a sugestão automática;
- `hasAssessedPriority` fica `true` quando houver uma avaliação válida, inclusive com score neutro;
- `userPriorityOverride` existente nunca é alterado por importação;
- novos nós recebem score neutro, mas com `hasAssessedPriority = false`, se não houver avaliação;
- `externalId` continua sendo lido exatamente do arquivo e nunca é inventado a partir do nome;
- a árvore recursiva continua usando `parentTopicId` sem assumir profundidade fixa.

O `ImportResult` poderá informar avaliações normalizadas/rebaixadas sem tornar inválio um arquivo cujo conteúdo principal seja utilizável.

### Prompt da IA

Integrar as instruções no `EditalPromptBuilder` existente e nos esqueletos de conteúdo que preservam o contexto do tópico. Não será criado um prompt paralelo.

O prompt deverá instruir a IA a usar, nesta ordem:

1. quantidade oficial de questões;
2. peso oficial;
3. pontuação;
4. critérios eliminatórios;
5. distribuição oficial;
6. provas anteriores da mesma banca quando fornecidas;
7. histórico do cargo/órgão/área quando fornecido;
8. recorrência demonstrável do tópico;
9. relevância estrutural;
10. inferência contextual apenas quando não houver evidência superior.

O prompt também deverá exigir:

- `score` inteiro 0..100;
- `confidence` 0.0..1.0;
- `source` limitado ao enum;
- `rationale` curto;
- evidências resumidas e verificáveis;
- `MEDIUM` e confiança reduzida quando não houver evidência suficiente;
- nenhuma estatística, percentual, frequência ou ranking inventado;
- distinção explícita entre fato oficial, histórico fornecido e inferência.

O prompt de conteúdo não recalculará importância genérica. Quando o pacote incluir contexto de prioridade, ele será preservado; quando não incluir, o importador manterá a avaliação local existente.

### Backup e restauração

O `BackupService` passará a exportar os campos de avaliação e o override de concursos, matérias e tópicos. O formato de backup será incrementado para a próxima versão do codec, mantendo a leitura das versões antigas.

Ao restaurar backup antigo, os novos campos receberão defaults neutros e `hasAssessedPriority = false`. Ao restaurar backup novo, o override será restaurado junto com a avaliação automática e o estado de avaliação disponível.

### Interface

Na lista do edital:

- `SubjectCard` exibirá o texto “Prioridade: Alta”, sem depender somente de cor;
- `TopicRow` exibirá a prioridade efetiva de forma discreta;
- ícone ou chip poderá complementar o texto, mas não será a única indicação.

O menu da matéria e do tópico abrirá um editor compacto com:

- prioridade atual;
- origem: “Sugerida automaticamente” ou “Definida por você”;
- sugestão automática quando houver override;
- opções Muito alta, Alta, Média, Baixa e Muito baixa;
- opção “Usar sugestão automática”;
- ação “Por que esta prioridade?” exibindo rationale, confiança humana e evidências resumidas;
- aviso de que a prioridade é uma estimativa de planejamento, não garantia de cobrança.

O concurso selecionado terá acesso ao mesmo editor por uma ação discreta no cabeçalho, sem criar uma nova tela grande.

### Planner

O `StudyPlannerEngine` continuará usando `PlanPriority` e as configurações do plano. Não será aplicada uma multiplicação automática de importância, desempenho e urgência nesta etapa.

Será criado um adaptador de domínio testado entre `PriorityLevel` e `PlanPriority` para uma evolução futura. A criação de planos existentes não será alterada silenciosamente. Caso o fluxo atual de criação de plano já tenha um ponto seguro para valor inicial, ele poderá usar a prioridade efetiva apenas como sugestão visual, mantendo a escolha explícita do usuário.

### Logs de debug

Em build de debug, o domínio poderá registrar apenas identificadores locais, nível automático, fonte, override e nível efetivo. Não serão registrados rationale completo, conteúdo do edital, URLs ou texto sensível.

## Testes

Serão adicionados testes para:

- score 0, 20, 21, 40, 41, 60, 61, 80, 81 e 100;
- normalização de score e confiança inválidos;
- distinção entre score neutro explicitamente avaliado e ausência de avaliação para herança;
- conversão única score → nível;
- fallback para pai e média neutra;
- override manual, remoção do override e nova avaliação automática preservando override;
- parser de arquivo antigo sem avaliação;
- parser de arquivo novo com avaliação em concurso, matéria, tópico e subtópico;
- rejeição/normalização controlada de enums inválidos;
- round-trip de `.estudo` mantendo IDs e avaliação;
- backup antigo recebendo defaults;
- backup novo preservando avaliação e override;
- migração Room 11→12 preservando dados;
- prompt contendo as regras de evidência e proibição de estatísticas inventadas;
- árvore recursiva com prioridades independentes entre pai e filho.

O comportamento do `StudyPlannerEngine` existente será coberto pelos testes atuais e não deverá mudar como efeito colateral.

## Arquivos previstos

### Criar

- `app/src/main/java/br/com/estudario/domain/PriorityModels.kt`
- `app/src/main/java/br/com/estudario/domain/PriorityResolver.kt`
- testes unitários correspondentes.

### Modificar

- `app/src/main/java/br/com/estudario/data/local/Entities.kt`
- `app/src/main/java/br/com/estudario/data/local/Converters.kt`
- `app/src/main/java/br/com/estudario/data/local/AppDao.kt`
- `app/src/main/java/br/com/estudario/data/local/AppDatabase.kt`
- `app/src/main/java/br/com/estudario/data/transfer/EstudoPackageService.kt`
- `app/src/main/java/br/com/estudario/data/transfer/BackupService.kt`
- `app/src/main/java/br/com/estudario/data/prompt/PromptBuilders.kt`
- `app/src/main/java/br/com/estudario/ui/AppViewModel.kt`
- `app/src/main/java/br/com/estudario/ui/screens/EditalScreen.kt`
- `app/src/main/java/br/com/estudario/ui/screens/TopicDetailScreen.kt`
- testes de prompt, parser, backup e migração.

O `StudyPlannerEngine` só será alterado se a inspeção durante a implementação confirmar um ponto seguro para o adaptador, sem mudar a distribuição atual.

## Critérios de aceite

1. Um arquivo novo pode sugerir prioridade em todos os níveis sem inventar estatísticas.
2. Um arquivo antigo continua sendo importado.
3. Uma nova análise automática atualiza a sugestão, mas preserva override manual.
4. O usuário consegue aplicar, remover e revisar um override em concurso, matéria e tópico.
5. Backup e restauração preservam a decisão manual.
6. A migração Room preserva dados existentes sem destructive migration.
7. A prioridade efetiva é resolvida em um único ponto de domínio.
8. O planner existente mantém seus testes e comportamento.
9. Testes unitários, testes de migração disponíveis e build debug passam.
