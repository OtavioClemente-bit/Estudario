# Planos e limites da IA do Estudário

Todo limite que protege gasto é aplicado no Supabase. O app só exibe o saldo.

## Onde ficam

- `public.profiles.plan_tier`: `FREE`, `ESSENCIAL` ou `PRO`. O app não consegue alterar.
- `public.profiles.plan_renews_at`: fim do período pago. Vencido, a conta volta a ser Grátis.
  Nulo em plano pago significa concessão manual sem vencimento.
- `public.ai_plan_limits`: limites por plano e recurso.

Valores conferidos no banco em 14/10/2026 (`ai_plan_limits` e `plan_catalog`):

| Plano | Preço | Material/mês | Lotes de questões/mês (até 10 cada) | Simulados (até 30 questões) | Edital pelo PDF | Editais salvos | Planos | Anúncio |
|---|---|---|---|---|---|---|---|---|
| Grátis | R$ 0 | 10 | 3 | 1 no total | não | 5 | 1 no total | +1 conteúdo, até 2/dia e 10/mês |
| Essencial | R$ 24,90/mês ou R$ 249/ano | 25 | 10 | 3/mês | 2/mês | 15 | 3/mês | não tem |
| Pro | R$ 49,90/mês ou R$ 499/ano | 40 | 25 | 6/mês | 5/mês | sem limite (1000) | 10/mês | não tem |

A página estudario.com.br (`site/index.html`) repete esses números: mudou aqui, muda lá.

## Grátis só com matéria pronta (24/10/2026)

O Grátis não usa IA em nada. A trava fica no worker, logo antes de chamar a OpenAI: conta grátis
(ou sem conseguir ler o plano) nunca chama, em nenhum recurso. Matéria, lote de questões e o
simulado diagnóstico saem só da biblioteca; faltando questão num formato, completa com outro
formato. Tópico sem matéria pronta entra na fila (`library_misses`) e o app mostra a oferta.

Matérias do Grátis, conferidas na criação do pedido (`_shared/free-allowance.ts`):

| Motivo (o app manda em `purpose`) | Regra |
|---|---|
| `PLAN_DAY` matéria do plano de hoje | livre, até 3 por dia |
| `ADVANCE` adiantar o próximo dia do plano | 1 crédito de anúncio confirmado pelo Google (até 2 por dia, 10 por mês) ou 1 por semana sem anúncio |
| `EXTRA` qualquer outra matéria do edital (e app antigo, sem o campo) | não é do Grátis (`FREE_EXTRA_LIMIT`); o app e o site nem abrem o assistente, mostram a oferta |

A cota `CONTENT_GENERATION` do Grátis (120/mês) virou só o teto de segurança da soma.

O plano do Grátis segue duas regras no motor (`PlannerSnapshotBuilder.Input.freeRules`, ligado por
`LibraryGaps.free`): no máximo 3 tópicos com teoria por dia, por mais horas que a pessoa ponha (o
resto do tempo vai para questões e revisões), e, se hoje já tem estudo, replanejar (trocar edital,
horas ou matérias) não mexe no dia de hoje. O Grátis também não apaga plano. Adiantar é pelo botão
**Adiantar** nas tarefas dos próximos dias (e na seção que aparece quando o dia termina); o anúncio
só é oferecido nessa hora.

Nos pagos, matéria do plano de hoje (`PLAN_DAY`) e adiantada (`ADVANCE`) que sai pronta da biblioteca devolve a cota (`ai_refund_plan_day_quota`): adiantar é livre e as 25/40 do mês ficam para o que é fora do plano.

O plano do Grátis só leva tópicos com matéria pronta: o app e o site perguntam à função
`library-ready` (mesma regra do gerador: apelido direto ou rota completa guardada) e deixam os
outros de fora (`LibraryGaps` → `PlannerSnapshotBuilder.excludedTopicIds`). Quando a matéria é
publicada, o tópico entra sozinho no próximo replanejamento. Se o aluno grátis pedir um tópico que
não está pronto, aparece "Esta matéria está em produção".

A página **Equipe › Cobertura** do site (só a conta do dono) mostra a cobertura de cada edital do
catálogo; abrindo um edital, os tópicos que faltam são conferidos ao vivo e "Copiar pedido" monta a
lista para pedir as matérias em lote.

Nos pagos, a matéria do plano de hoje que sai pronta (sem IA) devolve a cota
(`ai_refund_plan_day_quota`): os 25/40 do mês são para usar além do plano do dia. Matéria escrita
pela IA conta sempre.

### Anúncio premiado (AdMob)

O app nunca diz sozinho que a pessoa viu o anúncio: o AdMob chama a função `ad-reward-ssv`
assinada pelo Google, e só então o crédito existe (`ad_reward_credits`). Para ligar:

1. No AdMob, crie o app (Android, br.com.estudario) e um bloco **Premiado**.
2. No bloco: **Verificação do lado do servidor** → URL
   `https://gaqqilzhmvkxfpvivwwv.supabase.co/functions/v1/ad-reward-ssv`.
3. Os ids reais (app `ca-app-pub-6794476146182030~3847727077`, bloco premiado `ca-app-pub-6794476146182030/9507247864`)
   estão em `app/build.gradle.kts` e valem só na versão da loja; debug e o "Estudário Teste" usam os de teste do Google.
4. Play Console: declarar que o app **contém anúncios** e atualizar a **Segurança dos dados**
   (ID de publicidade).
5. Ligar no servidor: `supabase secrets set AD_REWARDS_ENABLED=true`. Antes disso o botão nem aparece.

Hoje só a geração de edital passa pelo backend. Os demais limites já estão cadastrados para a
tela de planos e para quando conteúdo, plano e questões forem gerados no servidor.

## Operações comuns (SQL Editor do Supabase)

Dar Pro para uma conta por 30 dias:

```sql
update public.profiles
set plan_tier = 'PRO', plan_renews_at = now() + interval '30 days'
where user_id = '<uuid da conta>';
```

### Plano TESTE (só pelo servidor)

Mais que o Grátis, sem aparecer no app nem no site (lá a conta continua com a cara do Grátis).
O saldo não renova por mês: vale até você zerar. Limites em `ai_plan_limits` com `plan_tier = 'TESTE'`:
10 materiais, 1 edital, 1 simulado (até 30 questões), 1 plano e 5 lotes de questões extras.

```sql
select public.admin_grant_test_plan('email@da.conta');    -- liga o TESTE com o saldo cheio
select * from public.admin_test_plan_usage('email@da.conta'); -- quanto já usou no ciclo
select public.admin_reset_test_plan('email@da.conta');    -- zera o saldo (ciclo novo; o histórico fica)
select public.admin_revoke_test_plan('email@da.conta');   -- volta para o Grátis
```

Mudar um limite:

```sql
update public.ai_plan_limits
set quota_limit = 15, updated_at = now()
where plan_tier = 'FREE' and feature = 'CONTENT_GENERATION' and period_kind = 'MONTHLY';
```

Ver o interesse em anúncio recompensado:

```sql
select day, count(*) as pessoas, sum(taps) as toques
from public.ai_ad_reward_interest
group by day order by day desc;
```

## Regras da cota

- A unidade é reservada ao criar o pedido e só é debitada quando o resultado é válido.
- Falha ou cancelamento antes da entrega libera a reserva.
- Repetir com a mesma `Idempotency-Key` reaproveita o pedido e não cobra de novo.
- Recurso sem linha em `ai_plan_limits` fica fechado (limite 0), nunca ilimitado.

## Publicação

```bash
supabase db push
supabase functions deploy ai-access ai-plan ai-syllabus-jobs ai-syllabus-worker ai-text-worker ad-reward-ssv library-ready
```
