# Conta Google e backup no Drive

O app continua funcionando inteiro sem internet e sem login. A conta Google é opcional e serve
para duas coisas: preencher nome, e-mail e foto do perfil, e guardar o backup completo numa pasta
privada do app dentro do Drive da pessoa.

## Como funciona por dentro

- **Autorização**: `AuthorizationClient` (`com.google.android.gms:play-services-auth`). Um pedido só,
  com três escopos: `userinfo.profile`, `userinfo.email` e `drive.appdata`. Na primeira vez o Google
  mostra a tela de consentimento; depois o token volta sem interromper ninguém — por isso "Backup" e
  "Restaurar" são um toque só. Código em `ui/profile/GoogleAuth.kt`.
- **Chamadas**: `data/account/GoogleDriveBackupService.kt` fala direto com a API REST por HTTPS
  (`HttpURLConnection` + `org.json`). Não entra a biblioteca cliente do Drive, que sozinha pesaria
  mais que o app inteiro.
- **Onde o backup fica**: `appDataFolder` — a pasta privada do app. Não aparece no Meu Drive, não
  polui os arquivos da pessoa e nenhum outro app consegue ler. O conteúdo é o mesmo JSON do
  "Exportar backup completo".
- **Token**: fica só na memória, nunca é gravado. Cada ação pede um novo.
- **Restaurar** sempre passa por confirmação, porque substitui os dados do aparelho.

## O que falta configurar (só você pode fazer)

Sem isso o botão abre e o Google devolve erro 10 (`DEVELOPER_ERROR`) — o app já traduz esse caso
numa mensagem clara na tela.

1. **Google Cloud Console** → criar (ou reusar) um projeto.
2. **APIs e serviços → Biblioteca** → ativar a **Google Drive API**.
3. **Tela de permissão OAuth** → tipo **Externo**, preencher nome do app, e-mail de suporte e de
   contato. Enquanto o app estiver em "Teste", adicione seu Gmail em **Usuários de teste**.
4. **Credenciais → Criar credenciais → ID do cliente OAuth → Android**:
   - Nome do pacote: `br.com.estudario`
   - Impressão digital SHA-1 do certificado
5. Nada para baixar e nada para colar no código: o `AuthorizationClient` identifica o app pelo par
   pacote + SHA-1.

### SHA-1 a cadastrar

**Release (`work/estudario-upload.keystore`, alias `estudario-upload`):**

```
E0:73:CB:BD:BB:83:38:DE:9F:5B:BA:40:D4:E7:BC:9C:E1:AB:98:1A
```

**Debug** (para testar a build de debug no aparelho) — rode no PowerShell:

```powershell
keytool -list -v -keystore "$env:USERPROFILE\.android\debug.keystore" -alias androiddebugkey -storepass android -keypass android
```

Cadastre os dois SHA-1 (dá para criar dois clientes Android no mesmo projeto).

**Se publicar na Play Store com o Play App Signing**, o APK entregue é reassinado pelo Google: pegue
também o SHA-1 em **Play Console → Configuração → Integridade do app → Assinatura de apps** e
cadastre-o, senão o login funciona no seu aparelho e falha para quem baixar da loja.

## Publicação

Enquanto a tela de permissão estiver em "Teste", só os usuários de teste conseguem entrar. Para
liberar geral é preciso publicar a tela de permissão. Os escopos usados (`userinfo.*` e
`drive.appdata`) **não são sensíveis nem restritos**, então a publicação não exige verificação de
segurança nem auditoria — é só mudar o status para "Em produção".
