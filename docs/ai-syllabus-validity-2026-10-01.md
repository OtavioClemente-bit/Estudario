# Validade da proposta de edital: aplicabilidade e textos literais

Correção local sobre `main`, a partir de `c58c4ef`, em 01/10/2026. Sem deploy, alteração de produção, geração remota, commit ou publicação.

## Causas e decisões

1. O prompt misturava seleção por cargo com seleção de matérias sem explicar herança de blocos comuns. Trecho antigo real: `When the PDF covers several roles and the requested role is present, keep its supported subjects.` Isso permite interpretar a especialidade como filtro de seção. A extração local não filtra por cargo: um teste do anexo TRT preserva conhecimentos gerais, exceções, Técnico TI e Analista TI. Não foi reexecutado nem inspecionado remotamente o job informado; a causa identificada é a ambiguidade do contrato de instruções, compatível com os resultados relatados.
2. A nova regra percorre todas as seções e resolve **aplicabilidade**, incluindo herança de grupos, regras gerais, exclusões e alternativas. Não busca títulos por substring, não inclui específicos de cargo inequivocamente diferente e não adiciona matérias por conhecimento prévio.
3. `FULL`: todo conteúdo programático aplicável, comum/geral/básico + específico. `BASIC_AND_SPECIFIC`: a mesma união, excluindo anexos sem conteúdo programático. `SPECIFIC_ONLY`: exclui intencionalmente comuns/gerais/básicos, mas respeita aplicabilidade e exceções dos específicos.
4. Especialidade presente em vários cargos: preserva comuns inequivocamente aplicáveis a todos os candidatos; mantém específicos candidatos separados e identificados com cargo/área/especialidade e páginas; emite `AMBIGUOUS_STRUCTURE`. Não afirma que a união dos candidatos se aplica a um único cargo nem escolhe silenciosamente Técnico ou Analista.
5. O Android usava `requireName()` e `MAX_NAME_LENGTH = 200` também para `topic.name`. O prompt, entretanto, exigia preservar o item literal do edital como pai. Isso rejeitava um resultado legítimo SUCCEEDED ao validar o draft.
6. Limites separados, sem mudança de formato/schemaVersion: nomes curtos (matérias/identificadores de versão) **200**; títulos provenientes da fonte (`topic.name`, inclusive children, e `documentTitle`) **4.000**; descrições (`warning.message`, `warning.ambiguity`, ambiguities) **8.000**. Todos exigem conteúdo não vazio e rejeitam controles inválidos. Títulos e descrições aceitam LF, CR e tabulação. IDs continuam com suas próprias restrições.
7. O teto de 4.000 acomoda parágrafos compostos com margem de mais de nove vezes para o item real, sem aceitar textos arbitrariamente enormes. O caminho de geração de conteúdo permanece limitado a seis títulos. TS e Kotlin validam o comprimento UTF-16; o schema declara os mesmos tetos (seu maxLength Unicode pode ser menos restritivo para caracteres suplementares, mas o parser runtime continua sendo a fronteira de aceitação).
8. O fixture literal informado tem **430 caracteres / 446 bytes UTF-8**. DTO, draft, mapper, `.estudo`, UI e Room preservam o texto e seus filhos. Nenhum `take(200)`, resumo ou alteração de splitter foi introduzido. Os dois construtores Android de conteúdo deixaram de cortar títulos em 800; agora validam e preservam até 4.000. O servidor local aceita o mesmo teto para `topicPath`. Campos de contexto e outros recursos mantêm suas regras existentes.
9. O mapper não cria material para o pai. O pedido de conteúdo envia ancestrais como contexto e filho como último item/escopo; o prompt de conteúdo explicita essa distinção. O resultado do edital é JSONB no backend; Room usa `TEXT`; import/export copia títulos completos. Não há migração de banco ou mudança de estrutura de jobs. Fixtures v1 antigos continuam aceitos.
10. Seleção/preflight/confirmação/loading/upload/retry/quota não foram alterados. O mínimo de **24.000 tokens** do worker foi preservado. PDF/sourceText continua autoritativo para syllabus, sem pesquisa externa nem preenchimento por conhecimento prévio.

## Arquivos de implementação

- `supabase/functions/_shared/prompts/syllabus-v1.ts`: aplicabilidade, scopes e ambiguidade.
- `supabase/functions/_shared/syllabus-text-limits.ts`: tetos semânticos TS.
- `supabase/functions/_shared/schema.ts`: limites do JSON Schema.
- `supabase/functions/_shared/contracts.ts`: validação runtime antes de aceitar/salvar propostas.
- `supabase/functions/_shared/text-job-input.ts`: caminho de tópico de até 4.000 por item.
- `supabase/functions/_shared/prompts/text-jobs-v1.ts`: contexto ancestral e escopo do filho.
- `app/src/main/java/br/com/estudario/data/ai/AiSyllabusTextLimits.kt`: tetos e validação Kotlin.
- `app/src/main/java/br/com/estudario/data/ai/AiModels.kt`: DTO validado por tipo de texto.
- `app/src/main/java/br/com/estudario/domain/ai/AiSyllabusProposalValidator.kt`: draft com limites corretos.
- `app/src/main/java/br/com/estudario/data/ai/AiContentGeneration.kt` e `AiTextJobs.kt`: caminho completo sem corte em 800.

## Regressões

- `supabase/functions/_shared/prompts/syllabus-v1_test.ts`: 11 testes de contrato do prompt, cobrindo comuns + específicos, fora/dentro de exceções, role só no específico, especialidade em dois cargos, role incompleto, grupos herdados, outro cargo e os três scopes; contexto TRT/TI em FULL/BASIC_AND_SPECIFIC.
- `app/src/test/java/br/com/estudario/data/ai/EditalSectionFinderTest.kt`: anexo TRT mantém gerais, exceção e ambos os candidatos TI antes da IA.
- `supabase/functions/_shared/syllabus-text-regression_test.ts`: fixture real, round trip provider, tetos/controles, warning/ambiguity e parent como contexto/child como escopo.
- `app/src/test/java/br/com/estudario/domain/ai/AiLongTopicRegressionTest.kt`: quatro testes de DTO/draft/mapper/export/import, quatro filhos, limites e controles, warnings, sourceTitle e ambos os caminhos de conteúdo sem truncamento.
- `app/src/androidTest/java/br/com/estudario/ui/ai/AiReviewScreenTest.kt`: tópico literal completo, edição multiline e botão Usar habilitado/clicável.
- `app/src/androidTest/java/br/com/estudario/data/syllabus/SyllabusApplicationServiceTest.kt`: aplicação real em Room preserva pai literal e filhos.
- `supabase/functions/_shared/fixtures/v1/long-topic-name.txt` e cópia idêntica `app/src/androidTest/assets/long-topic-name.txt`.

Os testes novos falharam antes das correções: 3 JVM, 11 instruções de prompt e 2 Deno de segurança/caminho. O teste UI inicialmente comparava também o rótulo do campo; foi corrigida a asserção para verificar o texto editável e repetida a suíte.

## Comandos e resultados finais

Executados com JAVA_HOME do JBR do Android Studio:

```powershell
.\gradlew.bat :app:testDebugUnitTest :app:assembleDebug :app:assembleDebugAndroidTest --console=plain
.\gradlew.bat :app:lintDebug --console=plain
deno test --allow-read --allow-env --allow-net supabase/functions/_shared supabase/functions/ai-syllabus-worker/worker_test.ts supabase/functions/ai-text-worker/text_jobs_test.ts
deno check supabase/functions/ai-syllabus-worker/index.ts supabase/functions/ai-text-worker/index.ts
deno lint --rules-exclude=no-import-prefix,require-await supabase/functions/_shared/syllabus-text-limits.ts supabase/functions/_shared/syllabus-text-regression_test.ts supabase/functions/_shared/contracts.ts supabase/functions/_shared/schema.ts supabase/functions/_shared/text-job-input.ts supabase/functions/_shared/prompts/syllabus-v1.ts supabase/functions/_shared/prompts/syllabus-v1_test.ts supabase/functions/_shared/prompts/text-jobs-v1.ts
deno fmt --check supabase/functions/_shared/syllabus-text-regression_test.ts supabase/functions/_shared/syllabus-text-limits.ts supabase/functions/_shared/prompts/syllabus-v1.ts supabase/functions/_shared/prompts/syllabus-v1_test.ts
git diff --check
adb -s emulator-5554 install -r app/build/outputs/apk/debug/app-debug.apk
adb -s emulator-5554 install -r app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk
adb -s emulator-5554 shell am instrument -w -r -e class br.com.estudario.ui.ai.AiReviewScreenTest,br.com.estudario.data.syllabus.SyllabusApplicationServiceTest br.com.estudario.test/androidx.test.runner.AndroidJUnitRunner
```

Resultados: **459 JVM**, **131 Deno**, **22 instrumentados** sem falhas; APK debug e APK de testes compilados; typecheck dos dois workers, lint Deno, check de formato selecionado e diff check passaram. Logs em `work/syllabus-two-bugs-*.log`. Os arquivos TS antigos mantêm seu formato fora dos trechos corrigidos; não foi feita reformatação global. Não há tarefa ktlint/detekt/spotless configurada.

Android lint: **11 erros, 87 warnings, 3 hints**, existentes no estado base: FocusMode (permissão), EstudarioWidget (ResourceType/RestrictedApi), AiApiClient/AiTextJobs/PrivateSyllabusRepository (NewApi), FocusNavigationTest (ViewModel em Compose) e IncomingFileFormat (BOM). A linha NewApi de AiTextJobs não foi alterada. Não foi criada baseline nem suprimido erro para declarar a suíte limpa.

Limitação: testes de prompt protegem as instruções; não demonstram seleção semântica determinística de um modelo real. Não houve execução remota para reavaliar o PDF ou os jobs relatados. As correções do prompt/schema/backend só terão efeito remoto após um deploy autorizado; esta tarefa não fez deploy. Títulos de 801–4.000 enviados por um novo app também exigem que o backend receba a correção local de limite; o item real de 430 já cabe no limite remoto anterior de 800 para conteúdo.

## Envio à Play Console autorizado posteriormente

Após o pedido “Sobe pra gente na play console PF”, foi preparada a versão **3.3.3 (31)** na mesma branch. `:app:bundleRelease` passou, incluindo `lintVitalRelease`. Artefato: `work/estudario-3.3.3-31.aab`, 37.880.707 bytes; SHA-256 `218EDAF5FEB07444AF4D5E6C67CC0FE694A5CF073DE830E342C09282F1E4D2F6`. Assinatura JAR verificada e certificado SHA-256 igual ao pacote 3.3.2 (30): `B7:21:57:32:31:69:4B:2B:43:71:AF:7D:D8:9D:05:EE:3C:84:C7:62:0A:D7:C1:1F:99:2B:95:F6:31:A3:51:63`.

A Play Console aceitou `31 (3.3.3)`, pacote `br.com.estudario`, API 26+, target SDK 36, na faixa **Teste fechado - Alpha**. Nome: “Estudário 3.3.3 (31) - Revisão de edital IA”. Notas pt-BR descrevem as correções Android, sem anunciar a alteração ainda local do prompt. Dois avisos não bloqueantes sobre desofuscação e símbolos nativos.

O envio para revisão foi confirmado. Estado observado: **Alterações em análise**, verificações rápidas em andamento, com envio à revisão após as verificações. Ainda não disponível aos testadores no momento da entrega. Comprovante: `work/play-3.3.3-31-review.jpg`. Nenhum deploy Supabase ou commit Git foi realizado nesta etapa.
