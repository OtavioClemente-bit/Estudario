-- Simulados oficiais prontos por edital.
--
-- Cada linha é uma prova inteira (conteudo/simulados/<edital>.simulado-N.json), montada a partir da
-- prova real do concurso: as questões na ordem e na proporção da prova, com gabarito e explicação.
-- O app usa o primeiro simulado do edital como "Prova oficial" (sem IA e sem cota); os seguintes saem
-- do banco de questões. Publicada pelo script scripts/biblioteca (service_role); leitura só do que está
-- publicado, para anon e authenticated.

create table public.exam_simulation (
  id text primary key check (char_length(id) between 3 and 160),
  catalog_id text not null references public.exam_catalog (id) on delete cascade,
  -- Ordem entre os simulados do mesmo edital (1 = a prova oficial).
  number integer not null default 1 check (number between 1 and 50),
  board text check (board is null or char_length(board) between 2 and 80),
  -- CERTO_ERRADO, MULTIPLA_ESCOLHA ou MISTO.
  format text not null check (format in ('CERTO_ERRADO', 'MULTIPLA_ESCOLHA', 'MISTO')),
  -- Estrutura da prova real ("120 itens C/E: P1 básicos 50 ... + P2 específicos 70").
  structure text check (structure is null or char_length(structure) <= 1000),
  item_count integer not null check (item_count between 1 and 300),
  -- [{ "n", "subject", "topic", "format", "difficulty", "statement", "options": [{key,text,correct}], "explanation" }]
  questions jsonb not null check (jsonb_typeof(questions) = 'array'),
  status text not null default 'PUBLISHED' check (status in ('DRAFT', 'PUBLISHED')),
  updated_at timestamptz not null default now()
);

create index exam_simulation_catalog_idx on public.exam_simulation (catalog_id, status, number);

alter table public.exam_simulation enable row level security;
revoke all on table public.exam_simulation from public, anon, authenticated;
grant select on table public.exam_simulation to anon, authenticated;

create policy exam_simulation_read_published on public.exam_simulation
  for select to anon, authenticated
  using (status = 'PUBLISHED');
