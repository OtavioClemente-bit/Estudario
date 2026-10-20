// Revisão à mão dos tópicos que abrem a matéria inteira ("foco": "inteira" em conteudo/foco).
// Não escreve conteúdo: só mostra dados para a revisão e aplica as decisões tomadas por quem revisou.
//
//   deno run -A scripts/biblioteca/foco-revisar.ts lista <de> <até>     matérias por ordem alfabética (1 = primeira)
//   deno run -A scripts/biblioteca/foco-revisar.ts aplicar <decisoes.json>
//
// decisoes.json: { "<materia>": { "<n do tópico>": [capítulos 0-based] | "outra.materia" | "outra.materia:0,2" } }
// Tópico não citado continua "inteira". "outra.materia" move a ligação (editais e foco) para a outra matéria,
// que abre inteira ou nos capítulos depois dos dois-pontos.

const dir = "conteudo/foco";
const files = [...Deno.readDirSync(dir)].map((f) => f.name).filter((n) => n.endsWith(".json")).sort();
const readJson = (p: string) => JSON.parse(Deno.readTextFileSync(p));
const materia = (id: string) => { const [a, b] = id.split("."); return readJson(`conteudo/materias/${a}/${b}.json`); };
const inteiras = (j: any): string[] => [...new Set<string>(j.topicos.filter((t: any) => t.foco === "inteira").map((t: any) => t.texto))];
const comInteira = files.filter((f) => inteiras(readJson(`${dir}/${f}`)).length > 0);

const [cmd, a1, a2] = Deno.args;
if (cmd === "proximas") {
  // Próximas N matérias ainda não revisadas (a lista de revisadas fica em conteudo/foco/_revisadas.txt).
  let feitas: string[] = []; try { feitas = Deno.readTextFileSync(`${dir}/_revisadas.txt`).split("\n").filter(Boolean); } catch { /* nenhuma */ }
  const pend = comInteira.filter((f) => !feitas.includes(f.replace(/\.json$/, "")));
  for (const f of pend.slice(0, Number(a1))) {
    const j = readJson(`${dir}/${f}`);
    let m: any; try { m = materia(j.materia); } catch { continue; }
    console.log(`\n### ${j.materia} — ${m.title}`);
    m.chapters.forEach((c: any, i: number) => console.log(`  cap ${i}: ${String(c.title).replace(/^\d+\.\s*/, "")}`));
    inteiras(j).forEach((t, i) => console.log(`  ${i + 1}) ${t.slice(0, 120)}`));
  }
  console.log(`\n(pendentes: ${pend.length})`);
} else if (cmd === "feitas") {
  // Marca como revisadas as matérias do arquivo de decisões (e as citadas depois dele).
  const ids = [...Object.keys(JSON.parse(Deno.readTextFileSync(a1))), ...Deno.args.slice(2)];
  Deno.writeTextFileSync(`${dir}/_revisadas.txt`, ids.join("\n") + "\n", { append: true });
} else if (cmd === "lista") {
  const de = Number(a1), ate = Number(a2);
  comInteira.slice(de - 1, ate).forEach((f, k) => {
    const j = readJson(`${dir}/${f}`);
    let m: any; try { m = materia(j.materia); } catch { return; }
    console.log(`\n### [${de + k}] ${j.materia} — ${m.title}`);
    m.chapters.forEach((c: any, i: number) => console.log(`  cap ${i}: ${String(c.title).replace(/^\d+\.\s*/, "")}`));
    inteiras(j).forEach((t, i) => console.log(`  ${i + 1}) ${t.slice(0, 120)}`));
  });
  console.log(`\n(total de matérias com tópicos inteiros: ${comInteira.length})`);
} else if (cmd === "aplicar") {
  const dec = readJson(a1) as Record<string, Record<string, unknown>>;
  let recortes = 0, movidos = 0;
  const moves: { texto: string; de: string; para: string; caps: number[] | null }[] = [];
  for (const [id, topicos] of Object.entries(dec)) {
    const path = `${dir}/${id}.json`;
    const j = readJson(path);
    const lista = inteiras(j);
    const nCaps = materia(id).chapters.length;
    for (const [n, d] of Object.entries(topicos)) {
      const texto = lista[Number(n) - 1];
      if (!texto) throw new Error(`${id}: tópico ${n} não existe`);
      if (Array.isArray(d)) {
        const caps = d.map(Number);
        if (caps.some((c) => !Number.isInteger(c) || c < 0 || c >= nCaps)) throw new Error(`${id} ${n}: capítulo fora da matéria`);
        if (caps.length === nCaps) continue;
        for (const t of j.topicos) if (t.texto === texto && t.foco === "inteira") { delete t.foco; delete t.questoes; t.capitulos = caps; recortes++; }
      } else {
        const [para, c] = String(d).split(":");
        moves.push({ texto, de: id, para, caps: c ? c.split(",").map(Number) : null });
        j.topicos = j.topicos.filter((t: any) => t.texto !== texto);
      }
    }
    Deno.writeTextFileSync(path, JSON.stringify(j, null, 2) + "\n");
  }
  if (moves.length) {
    for (const mv of moves) {
      const destino = materia(mv.para);
      if (mv.caps && mv.caps.some((c) => c < 0 || c >= destino.chapters.length)) throw new Error(`${mv.para}: capítulo fora`);
      const p = `${dir}/${mv.para}.json`;
      let j: any; try { j = readJson(p); } catch { j = { materia: mv.para, topicos: [] }; }
      j.topicos = j.topicos.filter((t: any) => t.texto !== mv.texto);
      j.topicos.push(mv.caps && mv.caps.length < destino.chapters.length ? { texto: mv.texto, capitulos: mv.caps } : { texto: mv.texto, foco: "inteira" });
      Deno.writeTextFileSync(p, JSON.stringify(j, null, 2) + "\n");
    }
    for (const f of Deno.readDirSync("conteudo/editais")) {
      if (!f.name.endsWith(".json")) continue;
      const p = `conteudo/editais/${f.name}`, raw = Deno.readTextFileSync(p);
      const e = JSON.parse(raw); let mudou = false;
      for (const d of e.disciplinas ?? []) for (const t of d.topicos ?? []) {
        const mv = moves.find((m) => m.texto === t.texto && m.de === t.topico);
        if (mv) { t.topico = mv.para; mudou = true; movidos++; }
      }
      if (mudou) Deno.writeTextFileSync(p, JSON.stringify(e, null, 2) + (raw.endsWith("\n") ? "\n" : ""));
    }
  }
  console.log(`recortados: ${recortes} · ligações movidas nos editais: ${movidos} (${moves.length} textos)`);
}
