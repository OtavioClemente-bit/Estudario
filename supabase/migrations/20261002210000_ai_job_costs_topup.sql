-- Reposição de questões (chamada curta, sem pesquisa) também entra na conta de custo.
alter table public.ai_job_costs drop constraint ai_job_costs_kind_check;
alter table public.ai_job_costs add constraint ai_job_costs_kind_check check (kind in ('MAIN', 'REVIEW', 'TOPUP'));
