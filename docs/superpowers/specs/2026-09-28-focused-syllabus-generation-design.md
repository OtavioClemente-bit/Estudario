# Geração de edital focada no cargo selecionado

## Problema confirmado

O fluxo atual já conhece o edital de destino no app (`targetTitle`), mas a chamada que cria o job não envia esse valor. O backend persiste somente arquivo, MIME, hash e tamanho no `request_payload`. O worker recebe o PDF inteiro e usa `syllabus-v1`, cujo prompt pede a extração completa sem cargo ou área como contexto. Na geração de 28/09/2026 do Anexo II do TRT da 3ª Região, isso produziu 31 seções, incluindo especialidades alheias ao edital de Técnico Judiciário — TI.

## Objetivo e critérios de aceite

- Uma geração iniciada a partir de um edital de destino chamado, por exemplo, “TRT 3ª Região — Técnico Judiciário — TI” deve incluir somente matérias comuns cuja aplicabilidade ao cargo esteja explícita no edital e o conteúdo específico correspondente à área/especialidade de TI.
- Se o PDF for de outro concurso, não trouxer a seleção solicitada, ou não permitir determinar com segurança quais matérias comuns se aplicam, o app deve mostrar uma mensagem clara de que não foi possível localizar/confirmar o conteúdo do edital para aquela seleção. Não deve salvar uma proposta parcial como se estivesse correta.
- A hierarquia, sequência e paginação de origem devem refletir o PDF; conteúdo de outras áreas/especialidades deve ser excluído e conteúdo repetido só pode ser consolidado quando sua equivalência e aplicabilidade estiverem explícitas.
- O cargo/área enviado deve sobreviver a retomada e recuperação do mesmo job. Alterar a seleção deve alterar o fingerprint de idempotência para impedir reutilização acidental de uma geração com outro alvo.
- Manter PDF como entrada não confiável: instruções no documento não podem substituir alvo, regras ou formato de saída.
- Manter uma chamada de geração por job. A seleção não deve disparar retries automáticos.
- Reduzir tokens de saída removendo seções fora do alvo. O PDF completo ainda será analisado nesta etapa; não prometer redução de tokens de entrada sem uma etapa separada de seleção de páginas.

## Desenho

1. Reusar o `targetTitle` que o app já tem. Enviá-lo como `target` no corpo de criação do job e persistir um objeto de alvo validado no `request_payload`, com limite de tamanho, normalização Unicode e remoção de controles. Incluir o valor normalizado no fingerprint.
2. Recuperar o alvo persistido no worker a partir do registro retornado pelo RPC de claim; nunca depender apenas do estado em memória ou da requisição HTTP inicial.
3. Criar `syllabus-v2`. O prompt recebe o alvo como contexto de tarefa fora do PDF. Instrui a extrair somente matérias gerais comprovadamente aplicáveis ao alvo e a parte específica compatível, mantendo a hierarquia original, ordem e `sourcePages`; proíbe inferir equivalência só por proximidade de nomes ou incluir outras especialidades.
4. Versionar o contrato da proposta para incluir `targetMatch` com estado `MATCHED`, `NOT_FOUND` ou `AMBIGUOUS`. Para `MATCHED`, exigir conteúdo não vazio. Para `NOT_FOUND`/`AMBIGUOUS`, exigir ausência de disciplinas; o worker converte o resultado em estado terminal seguro, sem proposal, com códigos internos distintos e mensagem amigável no app. Resultado fora dessas combinações é rejeitado como inválido.
5. Atualizar validações e testes do backend e Android para versão 2, e mapear os novos erros terminais para texto claro com encerramento da sessão pendente. Clientes ainda instalados com schema v1 continuam lendo jobs existentes; nenhum job em andamento é reprocessado.

## Segurança e dados

- `targetTitle` é entrada do usuário, não autorização nem fonte factual; limitar tamanho e caracteres de controle antes de persistir. Escapá-lo/separá-lo do conteúdo do PDF no prompt.
- Não registrar o conteúdo do PDF, prompt completo, nomes de matérias ou proposta em logs. Diagnóstico pode manter apenas IDs, versão, estado de correspondência e código allowlisted.
- Preservar autenticação existente, RLS, armazenamento privado e token interno do worker. Não adicionar segredo nem tornar endpoint público.
- A falha de correspondência não consome a cota bem-sucedida; seguir o comportamento terminal de falha já adotado.

## Fora de escopo

- Alterar provedor, quota, Cron, limites globais de processamento ou retry do worker.
- Buscar o edital na internet, fazer OCR novo ou pré-selecionar páginas antes do provedor.
- Criar uma tela nova para perguntar cargo/área: o app já possui e persiste o nome do edital de destino. Se esse título estiver genérico, comportamento correto é pedir ao usuário que renomeie/selecione um destino descritivo antes de gerar, não adivinhar.
- Reexecutar a geração já concluída automaticamente.

## Riscos e resposta

- Um `targetTitle` genérico (como “Meu edital”) não identifica cargo/área. Rejeitar como ambíguo e explicar que é necessário indicar cargo/área no nome do edital.
- Modelos podem omitir ou alucinar `targetMatch`. Validar a enumeração, consistência estado/conteúdo e páginas; falhar fechado em saída ambígua/inválida.
- Mudança de contrato requer coordenar Kotlin/Android e backend. Incrementar schema/prompt e preservar compatibilidade de leitura dos jobs v1.
- O alvo restringe a seleção, mas o modelo ainda precisa ler o PDF completo, então tokens de entrada não caem nesta entrega.

## Verificação

- Testes de contrato: entrada v2 válida para `MATCHED`; `NOT_FOUND`/`AMBIGUOUS` sem disciplinas; rejeitar alvo ausente, grande, malformado, resposta inconsistente e saída que viola limites.
- Testes de API: `target` é validado, persistido e participa do fingerprint/idempotência; solicitações antigas continuam aceitas com alvo legado explícito como desconhecido e resultam em aviso, nunca extração indiscriminada.
- Testes do worker: prompt inclui alvo com isolamento contra prompt injection; `MATCHED` salva sucesso; `NOT_FOUND`/`AMBIGUOUS` finaliza com falha amigável, sem proposal e sem registrar conteúdo sensível.
- Testes Android: novo campo é enviado e retomado; novos erros viram estado terminal legível e limpam sessão pendente; schema v1/v2 são lidos conforme compatibilidade definida.
- Verificar caso TRT-3 TI com fixture controlada derivada do documento: conhecimentos gerais explicitamente aplicáveis + Técnico Judiciário, Apoio Especializado, Tecnologia da Informação; excluir Administrativo, Enfermagem, Analista e demais especialidades; referências de página corretas.
- Executar testes focados, testes relevantes existentes, build Android e checagem de tipos/contratos Deno. Fazer uma geração real apenas quando o usuário iniciar manualmente uma nova tentativa, sem criar job de teste pago.
