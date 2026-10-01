# Geração de edital com IA: revisão de 2026-10-01

## Escopo

Alterações locais sobre `main`, a partir de `cb2523c`. Foram lidos o fluxo de criação/importação, setup, Assistente, extração de PDF, requests/jobs, recuperação, revisão/aplicação e geração posterior por tópico, incluindo os testes relacionados. Não houve deploy, alteração de Supabase remoto, commit ou push.

## Fluxo resultante

1. O concurso, cargo e PDF escolhidos acompanham a entrada no Assistente.
2. A seleção do PDF cria um snapshot privado, calcula seu hash e extrai texto localmente.
3. O preflight classifica a fonte e mostra a conferência. Nenhum job, reserva, upload ou processamento remoto é iniciado nessa etapa.
4. O usuário pode editar informações ou trocar o PDF. Uma fonte incerta exige confirmação explícita; divergências são avisos. Arquivos claramente inadequados ou documentos administrativos sem conteúdo identificado pedem outra fonte.
5. Somente a confirmação inicia a solicitação. O identificador da tentativa permanece estável em confirmações repetidas; o repositório serializa operações sobre a mesma solicitação.
6. A geração termina em revisão. Aplicar o edital continua sendo uma ação explícita.

## Causas corrigidas

- **Contexto antigo:** o contexto se perdia nas fronteiras de navegação, persistência do target e reutilização do ViewModel. Preferências explícitas agora acompanham a solicitação; o setup não transporta cargo/PDF de outro concurso. A identidade da entrada separa sessões. A troca de conta invalida o estado transitório anterior.
- **Warnings longas:** mensagens e ambiguidades tinham validação de nome com limite de 200 caracteres. Agora usam validação descritiva independente, com limite de 8.000 caracteres e rejeição de controles inválidos. Nomes mantêm o limite original. Warnings informativas não invalidam uma proposta estruturalmente válida.
- **Geração ao selecionar PDF:** seleção e preparação foram separadas da confirmação e do início remoto. O snapshot confirmado é verificado novamente por hash e tamanho antes do uso.
- **Erro sem troca de fonte:** falha e revisão oferecem troca de PDF pelo mesmo percurso de preflight/conferência. Trocar a fonte gera uma nova identidade de tentativa; retry continua sendo recuperação/repetição da solicitação existente.
- **RESERVED abandonado:** a recuperação consulta o estado efetivo do servidor, conclui upload/bind pendentes e chama process quando necessário. Não transforma timeout em PROCESSING fictício. A solicitação durável agenda recuperação antes de depender dos callbacks da UI. WorkManager e tela compartilham a serialização da solicitação.

## Preflight e contrato

O preflight usa evidências combinadas, normalização de acentos/pontuação e comparação conservadora. Siglas curtas e evidência insuficiente não causam rejeição por divergência. PDFs sem texto ou com extração parcial podem continuar após confirmação explícita. A identificação exibida é uma estimativa local, não uma garantia sobre todo o documento.

O schema v1 foi mantido, inclusive `subjects.minItems = 1`, preservando jobs antigos. O prompt local foi ajustado para exigir conteúdo sustentado pelo PDF/sourceText, usar o contexto apenas para desambiguação e proibir pesquisa externa ou matérias típicas inferidas. Uma fonte sem conteúdo suportado deve falhar na extração, em vez de produzir uma proposta vazia incompatível com o schema ou matérias inventadas.

O worker local garante pelo menos **24.000 tokens de saída para syllabus**, inclusive em configurações inferiores. Essas alterações do worker/prompt não estão implantadas remotamente. Não foi feita chamada real ao provedor para validar comportamento de modelo em produção.

## Verificação

- **452 testes JVM:** passaram, sem falhas ou skips. Incluem regressões de warnings longas, confirmação idempotente, snapshot imutável, nova fonte, RESERVED e agendamento durável antes de cancelamento da UI.
- **Instrumentação Android:** execução final `OK (50 tests)`, com 49 testes aprovados e 1 skip. O teste antigo dependente de um PDF externo foi ignorado por ausência da fixture; o novo teste cria um PDF determinístico e valida extração real, preflight e ausência de chamadas remotas. Inclui UI, troca de conta, contexto explícito, confirmação, recuperação durável e aplicação no Room.
- **Build:** `:app:assembleDebug` e `:app:assembleDebugAndroidTest` passaram.
- **Deno:** 93 testes passaram, incluindo jobs, worker, prompts, limite de saída e rejeição de proposta sem matérias. `deno check` passou para jobs/worker. `deno fmt --check` passou nos quatro arquivos TypeScript modificados.
- **Deno lint:** passou nos arquivos modificados com `--rules-exclude=no-import-prefix,require-await`, compatível com imports por URL e mocks async já utilizados. O lint padrão acusa esses padrões existentes; não foi alterada a configuração global.
- **Android lint:** executado; falhou com 11 erros, 87 warnings e 3 hints em arquivos não alterados deste fluxo: FocusMode, EstudarioWidget, AiApiClient, AiTextJobs, PrivateSyllabusRepository, FocusNavigationTest e IncomingFileFormat. Não foi criado baseline para ocultar esses erros.
- **Kotlin:** não há configuração de ktlint/detekt/Spotless nos arquivos Gradle principais. Compilação, testes, lint Android e verificação de whitespace foram executados.

### Comandos e evidências locais

Logs em `work/ai-flow-build-test.log`, `work/ai-flow-instrumentation.log`, `work/ai-flow-worker.log`, `work/ai-flow-deno-check.log`, `work/ai-flow-deno-fmt.log`, `work/ai-flow-deno-lint-project.log` e `work/ai-flow-android-checks.log`.

```powershell
.\gradlew.bat :app:testDebugUnitTest :app:assembleDebug :app:assembleDebugAndroidTest --console=plain
.\gradlew.bat :app:lintDebug --console=plain
deno test --no-config --node-modules-dir=none --allow-env --allow-read supabase/functions/ai-syllabus-worker supabase/functions/ai-syllabus-jobs supabase/functions/_shared/prompts/syllabus-v1_test.ts supabase/tests/functions/ai-syllabus-jobs_test.ts
deno check --no-config --node-modules-dir=none supabase/functions/ai-syllabus-jobs/index.ts supabase/functions/ai-syllabus-worker/index.ts
```

Os testes instrumentados usam o emulador Pixel_7/API 35, APIs simuladas e banco local. Não consomem quota de IA real nem alteram dados do celular do usuário.
