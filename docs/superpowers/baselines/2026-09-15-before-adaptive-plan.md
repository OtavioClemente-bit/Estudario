# Linha de base antes do Plano de Estudos Adaptativo

Data: 2026-09-15

## Ambiente

- Java inicial: Oracle JDK 25.0.1. O Gradle falhava antes de configurar o projeto com `IllegalArgumentException: 25.0.1`.
- Java usado na linha de base: Eclipse Temurin 17.0.20.1.
- Android SDK: `C:\Users\otavi\AppData\Local\Android\Sdk`, configurado por variável de ambiente no processo de build.
- Gradle Wrapper: 8.14.
- Android Gradle Plugin: 8.13.2.
- Kotlin: 2.2.20.

## Resultados

- `gradlew test`: sucesso.
- Testes unitários: 38 executados, 0 falhas, 0 erros, 0 ignorados.
- `gradlew lintDebug assembleDebug`: sucesso.
- Lint: 24 avisos preexistentes, 0 erros.
- APK debug: gerado em `app/build/outputs/apk/debug/app-debug.apk`.

## Avisos preexistentes relevantes

- Ícones Compose direcionais depreciados em telas atuais, recomendando variantes AutoMirrored.
- `LocalClipboardManager` depreciado em `ImportGuideScreen`.
- `libandroidx.graphics.path.so` e `libdatastore_shared_counter.so` empacotadas sem strip de símbolos.
- Opções de processamento `room.schemaLocation` e `kapt.kotlin.generated` não reconhecidas durante tarefas de teste sem processadores aplicáveis.

Esses avisos existiam antes do módulo novo e não impedem testes, lint ou build.

## Git

O workspace não é um repositório Git isolado. O único `.git` encontrado está em `C:\Users\otavi`, não possui `HEAD` e não deve receber commits deste projeto.
