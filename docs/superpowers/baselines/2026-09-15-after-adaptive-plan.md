# Verificação final — Plano de Estudos Adaptativo

Data: 2026-09-15  
Ambiente: Eclipse Temurin JDK 17, Gradle 8.14, Android SDK 36, AVD `Medium_Phone_API_36.1`.

## Resultado

- `clean testDebugUnitTest lintDebug assembleDebug assembleDebugAndroidTest --stacktrace`: sucesso.
- Testes unitários: 46 executados, 0 falhas, 0 erros, 0 ignorados, em 13 suítes.
- Testes instrumentados: 19 executados no AVD, 0 falhas e 0 ignorados.
- Lint: 0 erros e 22 avisos; a linha de base anterior tinha 24 avisos. Nenhum aviso novo específico do planejador.
- APK: `app/build/outputs/apk/debug/app-debug.apk` (20.511.817 bytes na execução registrada).
- APK de testes: `app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk` (1.244.761 bytes na execução registrada antes do build limpo final; recriado com sucesso depois).
- Room: migração 4→5 executada no AVD com validação de schema e preservação dos dados v4.
- Git: checkpoint não criado porque `git rev-parse --show-toplevel` retorna `C:/Users/otavi`, não o workspace `vc-x20`.

## Cobertura funcional validada

- capacidade líquida, déficit explícito, previsão e limite flexível por matéria;
- determinismo, dependências, manutenção mínima e priorização de fraqueza com amostra;
- redução 4h→2h, aumento de carga, falta, bloqueio de dia/tarefa e redistribuição;
- execução parcial 25/60 preservando 25 e criando saldo de 35;
- exclusividade de plano ativo e Plano Mestre na persistência;
- rejeição de proposta obsoleta por `baseRevision`;
- duplicação sem execuções e com novos IDs próprios;
- `.plano` v1 válido, inválido, cíclico e de versão futura;
- CREATE, MERGE, REPLACE_FUTURE, vínculo por `externalId` e precedência do histórico local;
- backup v5 do grafo do planejador e restauração compatível com versões anteriores;
- barra principal Início/Edital/Plano/Treinar/Mais, Hoje padrão e Erros dentro de Mais;
- componentes Hoje/Semana/Mês/Ano e ações principais da tarefa.

## Decisões relevantes

- `StudyPlannerEngine` permanece Kotlin puro e recebe um snapshot completo.
- `StudyPlanApplicationService` aplica mudanças em transação e reivindica a revisão otimisticamente antes de escrever.
- execuções são registros próprios; o motor nunca escreve ou exclui histórico.
- DTO `.plano` é separado das entidades Room e usa `externalId` do edital.
- fases anuais, meses e semanas são entidades versionáveis reais.
- Home foi preservada sem resumo adicional nesta entrega.
- Espresso foi atualizado de 3.6.1 para 3.7.0 para compatibilidade com Android 16.1; a versão anterior refletia uma API de `InputManager` removida.

## Limitações remanescentes

- não há API, nuvem, pagamentos ou IA embutida, conforme escopo;
- a previsão é determinística e baseada na demanda persistida/capacidade atual, não uma promessa de aprovação;
- alterações permanentes no edital continuam pertencendo ao fluxo existente do Edital; o planejador permite pausar e repriorizar matérias dentro de cada plano.
