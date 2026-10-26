# Revisão personalizável

**Pedido:** engrenagem na página de revisões com o desenho do app; quantidades configuráveis; flashcards reversíveis; leitor de grifos opcional após os flashcards, dentro da revisão, sem edição. Usuário escolheu **5 questões** por padrão.

**Decisões:** manter o fluxo Lembrar → Consultar → Testar → Avaliar. Opções para próximas sessões: 0–50 questões, 0–30 flashcards, prioridade de erros/inéditas/misturada, cartões em ordem ou embaralhados, resumo rápido, oferta dos grifos e notas dos grifos. Guardar localmente no navegador/aparelho, usando DataStore no Android. Capturar as escolhas ao iniciar a sessão para não reiniciar respostas ao mudar preferências. O leitor do Caderno já é somente leitura: reutilizá-lo com cartões sem ação de abrir outra tela. Não alterar a regra Difícil/Normal/Fácil.

**Plano:**

- [x] Testar seleção de questões, limites, prioridade e quantidades no módulo shared; usar a mesma seleção no app e no site.
- [x] Persistir preferências e montar configurações com os contadores, botões e engrenagem existentes.
- [x] Aplicar configuração à sessão e oferecer grifos somente do tópico, em leitor com anterior/próximo, sem edição/navegação externa.
- [x] Permitir revelar/esconder o verso sem contar resposta nem avançar; registrar resposta só em Lembrei/Não lembrei.
- [x] Verificar as suítes, compilações, layout e revisão independente. Sem commit/push. O AAB 155 foi preservado.

## Verificação e publicação autorizada

O usuário autorizou publicar todas as mudanças após a implementação. 649 testes passaram (web 41, Android 572, shared 36), sem erros ou ignorados; build web de produção passou. Revisão independente apontou riscos de sincronização durante a sessão e links ativos no leitor: corrigidos com material congelado ao abrir a sessão e modo sem navegação, sem bloqueios restantes. Conferência no navegador: configurações persistem e só alteram a próxima sessão; janela na raiz não fica sob o cabeçalho. Padrão de 5 questões restaurado ao terminar a conferência.

Site publicado em 26/10/2026 no projeto estudario-app: https://b3c83706.estudario-app.pages.dev e https://app.estudario.com.br. Conferida a engrenagem e as configurações no domínio de produção. Android 3.3.128 (156) gerado com bundleRelease/lintVitalRelease, assinatura verificada e mesmo certificado do 155; envio à Play Console Alpha em andamento. O pacote 155 já estava em análise quando o Console foi aberto.
