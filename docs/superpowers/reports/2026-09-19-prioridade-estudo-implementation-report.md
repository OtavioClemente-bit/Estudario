# Relatório de implementação — prioridade de estudo

Data: 19/09/2026  
Projeto: Estudario  
Execução: nativa no workspace do Android Studio

## Entrega

- Prioridade explicável em concurso, matéria, tópico e subtópico.
- Score normalizado de 0 a 100, confiança de 0 a 1 e níveis textuais de “Muito alta” a “Muito baixa”.
- Herança recursiva: item sem avaliação própria herda a prioridade efetiva do pai; override manual sempre vence.
- Evidências, justificativa e origem ficam disponíveis no editor “Por que esta prioridade?”.
- Importação `.estudo` e backup preservam avaliações e overrides sem apagar decisões locais.
- Migração Room aditiva 11→12, sem destructive migration.
- Prompts de edital/content pedem evidências e proíbem estatísticas inventadas.
- Planner preservado; adaptador isolado para `PlanPriority`, sem alterar o algoritmo existente.
- UI do edital e detalhe do tópico exibem a prioridade e oferecem edição pelo menu/cabeçalho.

## Verificações executadas

Com JDK 17 em `C:\Users\otavi\.jdks\jbr-17.0.14`:

- `gradlew.bat :app:testDebugUnitTest --no-daemon` — BUILD SUCCESSFUL.
- `gradlew.bat :app:assembleDebug --no-daemon` — BUILD SUCCESSFUL.
- `gradlew.bat :app:assembleDebugAndroidTest --no-daemon` — BUILD SUCCESSFUL.
- `gradlew.bat :app:connectedDebugAndroidTest --no-daemon` — não executado por ausência de dispositivo conectado (`DeviceException: No connected devices!`).
- `git diff --check` — sem erros.

Os testes unitários cobrem domínio, codec, parser, prompts, backup, apresentação, herança recursiva e adaptador do planner. Os testes instrumentados de migração/importação/backup estão compilando no APK de teste, mas precisam de um emulador ou telefone autorizado e conectado para executar.

## APK

[Baixar app-debug.apk](C:/Users/otavi/Documents/Codex/2026-09-15/vc-x20/app/build/outputs/apk/debug/app-debug.apk)

## Commits da implementação

- `bb74118` domínio de prioridade.
- `ed0da89` persistência Room e migração.
- `51f0641` importação sem perder overrides.
- `479f733` prompts baseados em evidências.
- `ac8a6f4` backup/restauração.
- `3285191` editor e camada de interação.
- `752ab94` controles nas telas.
- `ecfb594` adaptador do planner.
- `8dc77a9` correção da herança no card de matéria.

As alterações anteriores dos tutoriais em vídeo e do aviso de geração múltipla foram preservadas.
