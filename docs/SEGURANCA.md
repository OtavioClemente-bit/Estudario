# Segurança do servidor

Auditoria de 24/10/2026 e o que ficou de pé. Tudo que custa dinheiro é decidido no servidor; o app e
o site só mostram.

## Regras que não dependem do app

- **Pedido de IA só nasce no servidor.** `create_or_get_ai_job_and_reserve_quota` não é mais
  chamável pelo usuário; a função `ai-syllabus-jobs` cria pelo `create_ai_job_for_user` (chave
  interna). Motivo, compensação e entrada do pedido não podem vir forjados de um APK alterado.
- **Grátis nunca chama a IA**, em nenhum recurso: a trava fica no worker, antes do provedor
  (`planTierOf`), e falha fechada se o plano não puder ser lido.
- **Teto da plataforma em 24 h** (segredo `AI_DAILY_TOKEN_BUDGET`, em tokens de entrada + saída;
  sem ele não há teto). Passou, o worker não chama a IA e devolve a cota
  (`AI_DAILY_BUDGET`). Uso médio em out/2026: ~200 mil tokens/dia; pico 2,3 milhões.
- **Cache compartilhado** só guarda pedidos sem "questões a evitar" (essa lista vai para a IA e
  ficava fora da chave).
- **Cancelar depois que a IA começou** conta a cota como usada.
- **Cobrança** sempre conferida na API do Google; um token de compra vale para uma conta só;
  reembolso (aviso do Google) derruba o plano na hora e não volta.
- **Anúncio premiado**: assinatura do Google conferida, só o nosso bloco, transação única, teto
  por dia e por mês.
- **Banco**: RLS em todas as tabelas; usuário só lê o que é dele nas tabelas de cota, pedidos e
  perfil, sem permissão de escrita. `dblink` removido.
- **Arquivos**: cada conta só enxerga a própria pasta; PDFs de edital até 30 MB e 10 por dia;
  cópia na nuvem até 25 MB e 25 arquivos.
- **Prova oficial** (`exam_simulation`) só com login.

## Aviso do Google Play em tempo real (play-rtdn)

Sem isto, cancelamento e reembolso só valem quando a data de vencimento passa (no anual, um ano).

1. **Google Cloud** (o mesmo projeto ligado ao Play Console) → Pub/Sub → criar o tópico
   `play-rtdn`.
2. No tópico, em Permissões, dar o papel **Pub/Sub Publisher** para
   `google-play-developer-notifications@system.gserviceaccount.com`.
3. Criar uma **conta de serviço** só para o envio, por exemplo `play-rtdn-push`. Não precisa de
   papel nenhum.
4. Criar a **assinatura** do tópico, tipo **Push**:
   - URL: `https://gaqqilzhmvkxfpvivwwv.supabase.co/functions/v1/play-rtdn`
   - marcar **Ativar autenticação**, conta de serviço `play-rtdn-push@...`
   - público (audience): a mesma URL acima.
5. No Supabase, segredos da função:
   - `PLAY_RTDN_AUDIENCE` = a URL acima
   - `PLAY_RTDN_SERVICE_ACCOUNT` = o e-mail da conta `play-rtdn-push@...`
6. **Play Console** → Monetizar → Configuração da monetização → Notificações em tempo real:
   colar o nome do tópico (`projects/<projeto>/topics/play-rtdn`), salvar e clicar em
   **Enviar notificação de teste**. Nos registros da função deve aparecer a chamada com resposta
   200.

A função só aceita chamadas com o token do Google assinado para essa URL e esse e-mail.

## O que fica no painel do Supabase

- Auth → Rate limits: limitar e-mails por hora (o login é por código no e-mail).
- Se um dia houver criação de contas em massa: ligar CAPTCHA no Auth (o app e o site precisariam
  mandar o token do CAPTCHA no pedido do código).
