# Planos e limites da IA do Estudário

Todo limite que protege gasto é aplicado no Supabase. O app só exibe o saldo.

## Onde ficam

- `public.profiles.plan_tier`: `FREE`, `ESSENCIAL` ou `PRO`. O app não consegue alterar.
- `public.profiles.plan_renews_at`: fim do período pago. Vencido, a conta volta a ser Grátis.
  Nulo em plano pago significa concessão manual sem vencimento.
- `public.ai_plan_limits`: limites por plano e recurso.

| Plano | Editais | Planos | Conteúdo/mês | Lotes de questões/mês | Questões por lote | Por tópico/mês | Anúncio |
|---|---|---|---|---|---|---|---|
| Grátis | 1 no total | 1 no total | 10 | 5 | 10 | 20 | +1 conteúdo, até 2/dia e 10/mês (desligado no teste fechado) |
| Essencial | 3/mês | 3/mês | 60 | 30 | 20 | 60 | não tem |
| Pro | 8/mês | 8/mês | 200 | 100 | 30 | 120 | não tem |

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
supabase functions deploy ai-access ai-plan ai-syllabus-jobs ai-syllabus-worker
```
