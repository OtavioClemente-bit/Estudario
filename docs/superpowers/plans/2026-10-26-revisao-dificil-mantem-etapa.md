# Revisão difícil mantém a etapa

> **For agentic workers:** Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Difícil repete amanhã sem avançar; Normal/Fácil avançam uma etapa. O usuário escolheu deslocar as próximas datas preservando os intervalos.

**Architecture:** Uma política Kotlin compartilhada calcula a agenda para Android e web. A sessão e o esforço são registrados em qualquer dificuldade, mas completedAt da etapa só é preenchido em Normal/Fácil. Guardar intervalDays e updatedAt na agenda evita inflar intervalos após repetições e preserva a sincronização das alterações pendentes. Migração Room aditiva, backups antigos aceitos.

**Tech Stack:** Kotlin Multiplatform, kotlinx-datetime, Room, snapshots JSON, Compose.

**Constraints:** Sem commit, push, publicação ou alteração retroativa de históricos. Preservar alterações já existentes na pasta.

## Tasks

- [x] Reproduzir o avanço indevido com testes de Actions.completeReview; cobrir última etapa e tentativa de pular etapa.
- [x] Implementar ReviewStageProgression no módulo shared: repetir amanhã; deslocar próximas etapas por seus intervalos; bloquear conclusão antecipada/fora de ordem; manter ciclo perpétuo e Fácil.
- [x] Adicionar intervalDays nullable e updatedAt à agenda, migração Room 23→24, JSON e backup compatíveis. Integrar a mesma política ao StudyRepository e Actions.
- [x] Alinhar prévia, textos de dificuldade e fechamento da sessão: esforço salvo, etapa mantida quando Difícil.
- [x] Rodar testes web, Android e shared; validar migração aditiva e revisar alterações.

## Review focus

Repetidas avaliações Difícil não avançam stage; próximas etapas não aparecem antes da repetição; Normal/Fácil avançam uma só etapa; a última etapa difícil não cria sucessora; intervalos não crescem pelo número de tentativas; datas respeitam o calendário local; histórico e sincronização preservados.

## Verification

Três testes web falharam antes da implementação pelo motivo esperado. Verificação final: `:web:jsNodeTest` (40), `:app:testDebugUnitTest` (571) e `:shared:jvmTest` (29): 640 testes, zero falhas/erros/ignorados. Migração 23→24 executada em SQLite em memória com o SQL de produção e comparada ao schema Room 24 gerado; registros anteriores e valores padrão preservados. Revisão independente apontou uma rota de revisão ignorada que ainda abria no web; corrigida e coberta por teste. Sem outros bloqueios na revisão final. Sem publicação.
