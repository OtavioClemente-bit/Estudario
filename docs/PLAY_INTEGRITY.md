# Play Integrity: proteção contra APK pirata

Pedidos que reservam cota de IA (hoje: geração de edital) levam um token do Play Integrity
amarrado ao próprio pedido. O Supabase manda o token para o Google decodificar e, no modo
`enforce`, recusa quando:

- o app não é o binário reconhecido pela Play Store (APK modificado ou re-assinado);
- o token é de outro pedido ou tem mais de 10 minutos;
- o aparelho não atende ao nível mínimo (padrão: integridade básica);
- a instalação não é licenciada pela Play (pode ser desligado).

A recusa acontece **antes** de reservar cota ou chamar a IA. O app nunca recebe segredo:
a conta de serviço fica só nos secrets do Supabase.

## 1. Play Console

1. Abra o app no Play Console > **Teste e lançamento > Integridade do app**.
2. Em **API Play Integrity**, clique em **Vincular projeto do Cloud** e escolha (ou crie) um
   projeto do Google Cloud.
3. Anote o **número do projeto** (só dígitos) mostrado no Google Cloud > Painel.
4. Em **Configurações de resposta**, deixe marcados o veredito de reconhecimento do app, de
   integridade do dispositivo e de licenciamento.

## 2. Google Cloud: conta de serviço

1. No projeto vinculado: **APIs e serviços > Biblioteca** > ative **Google Play Integrity API**.
2. **IAM e administrador > Contas de serviço > Criar conta de serviço** (ex.: `play-integrity`).
   Não precisa de papel no projeto.
3. Na conta criada: **Chaves > Adicionar chave > JSON**. Guarde o arquivo baixado com cuidado
   e não coloque no repositório.

## 3. Secrets do Supabase

```bash
supabase secrets set PLAY_INTEGRITY_SERVICE_ACCOUNT_JSON="$(cat caminho/da-chave.json)"
supabase secrets set PLAY_INTEGRITY_PACKAGE_NAME=br.com.estudario
supabase secrets set PLAY_INTEGRITY_DEVICE_LEVEL=BASIC       # BASIC, DEVICE ou STRONG
supabase secrets set PLAY_INTEGRITY_REQUIRE_LICENSED=true
supabase secrets set PLAY_INTEGRITY_MODE=log                 # off, log ou enforce
supabase functions deploy ai-syllabus-jobs
```

## 4. App

Em `~/.gradle/gradle.properties` (ou nas variáveis do build de release):

```
estudario.playIntegrity.cloudProjectNumber=123456789012
```

Sem esse valor o app não envia token. Com `PLAY_INTEGRITY_MODE=enforce`, a geração pela IA
do Estudário fica bloqueada nesse build, o que também vale para builds de debug instalados
fora da Play.

## 5. Ativação segura

1. Publique o app com o número do projeto e o servidor em `log`.
2. Instale pela faixa de teste fechado da Play e gere um edital.
3. Nos logs da função `ai-syllabus-jobs`, procure `play_integrity_rejected`. Não deve aparecer
   para instalações da Play. Os motivos (`failures`) mostram o que falhou.
4. Sem recusas indevidas por alguns dias, mude para `enforce`.

Para testar sem a Play, o Play Console permite configurar respostas de teste em
**Integridade do app > Testes**.
