# Meu Concurso

> Estude para concursos com método, contexto e progresso visível.

Aplicativo Android nativo, offline e local-first para transformar um edital em
uma rotina de estudos executável. O Meu Concurso reúne teoria, questões,
revisões espaçadas, caderno de erros, fila de estudos e planejamento adaptativo
sem depender de login, backend ou internet em tempo de execução.

## Destaques

- **Plano adaptativo:** visão Hoje, Semana, Mês e Ano para vários planos por concurso.
- **Planejamento determinístico:** replaneja o futuro sem alterar execuções concluídas.
- **Teoria em Markdown:** livros, capítulos, resumos, marcações e progresso.
- **Questões e revisão:** tentativas imutáveis, caderno de erros e ciclos D+1/D+7/D+30.
- **Importação portátil:** pacotes `.estudo` e `.plano` com validação e prévia.
- **Privacidade local-first:** dados no aparelho, sem anúncios, Firebase ou pagamentos.

## Experiência

O app foi pensado para o ciclo completo: entender o edital, estudar um bloco,
revisar no momento certo, registrar erros e adaptar o próximo plano com base no
histórico real. A execução é separada do planejamento para preservar o histórico
mesmo quando a estratégia muda.

## Arquitetura

```text
UI Compose / Navigation
        ↓
ViewModels e repositórios
        ↓
Room / DataStore
        ↘
Motor de planejamento puro e determinístico
```

## Tecnologias

- Kotlin 2.2.20 e Java 17
- Jetpack Compose + Material 3
- Room/SQLite, Flow, ViewModel e coroutines
- DataStore Preferences
- WorkManager e notificações nativas
- Gradle 8.14 / Android Gradle Plugin 8.13.2
- `compileSdk`/`targetSdk` 36 e `minSdk` 26

## Como executar

Abra a pasta raiz no Android Studio com JDK 17, sincronize o Gradle e execute em
um aparelho ou emulador Android 8.0+.

```bash
./gradlew testDebugUnitTest assembleDebug
```

O APK de desenvolvimento é gerado em `app/build/outputs/apk/debug/app-debug.apk`.

## Documentação

- [Formato de estudos](docs/ESTUDO_FORMAT.md)
- [Formato de planos](docs/PLANO_FORMAT.md)
- [Prompts para gerar estudos](docs/PROMPT_GERAR_ESTUDO.md)
- [Exemplos importáveis](examples/)

## Status

Projeto em evolução ativa, com foco em planejamento adaptativo, acessibilidade,
simulados e sincronização opt-in no futuro.

## Licença

Projeto privado. Consulte o autor antes de reutilizar código, marca ou assets.
