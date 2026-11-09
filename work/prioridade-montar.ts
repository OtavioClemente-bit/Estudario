// Monta work/prioridade-recortes.json: matéria × banca de cada item dos editais testados pelos alunos.
import { normalizeBoard } from "../supabase/functions/_shared/library.ts";
const editais = ["pf-2025--agente", "inss-2022--tecnico-seguro-social", "acarau-2026--psicologo", "rn-unificado-2026--analista-de-transito-pedagogia-de-transito-detran-rn", "uft-2026--tecnico-de-tecnologia-da-informacao"];
const out = new Set<string>();
for (const e of editais) {
  const ed = JSON.parse(Deno.readTextFileSync(`conteudo/editais/${e}.json`));
  const b = normalizeBoard(String(ed.banca ?? ""));
  for (const d of ed.disciplinas ?? []) for (const t of d.topicos ?? []) if (t.topico && !String(t.topico).startsWith("atualidades.")) out.add(`${t.topico}|${b}`);
  console.log(e, "banca:", ed.banca, "→", b);
}
Deno.writeTextFileSync("work/prioridade-recortes.json", JSON.stringify([...out], null, 1));
console.log(out.size, "pares");
