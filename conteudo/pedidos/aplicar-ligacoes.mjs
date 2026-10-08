// Aplica nos editais as ligações tópico → matéria propostas pelos agentes (08/10/2026).
// Uso: node conteudo/pedidos/aplicar-ligacoes.mjs [--com-media] [--gravar]
//   sem --gravar só mostra quantas mudariam. Por padrão aplica só as de confiança ALTA.
// Só preenche tópicos que ainda estão com "topico": null e cujo texto bate com o da proposta.
import fs from "node:fs";
const gravar = process.argv.includes("--gravar");
const arquivos = ["conteudo/pedidos/ligacoes-alta.json"];
if (process.argv.includes("--com-media")) arquivos.push("conteudo/pedidos/ligacoes-media.json");
const ids = new Set();
const walk = (d) => { for (const e of fs.readdirSync(d, { withFileTypes: true })) { const p = `${d}/${e.name}`; if (e.isDirectory()) walk(p); else if (e.name.endsWith(".json")) { try { ids.add(JSON.parse(fs.readFileSync(p, "utf8")).id); } catch {} } } };
walk("conteudo/materias");
const porEdital = new Map();
for (const a of arquivos) for (const r of JSON.parse(fs.readFileSync(a, "utf8"))) (porEdital.get(r.edital) ?? porEdital.set(r.edital, []).get(r.edital)).push(r);
let ok = 0, semMateria = 0, mudou = 0;
for (const [ed, regs] of porEdital) {
  const p = `conteudo/editais/${ed}`; if (!fs.existsSync(p)) continue;
  const j = JSON.parse(fs.readFileSync(p, "utf8")); let n = 0;
  for (const r of regs) {
    const t = j.disciplinas[r.disciplina]?.topicos[r.topico];
    if (!t || t.topico !== null || t.texto !== r.texto) { mudou++; continue; }
    if (!ids.has(r.materia)) { semMateria++; continue; } // matéria ainda em entrada/, não publicada
    t.topico = r.materia; n++;
  }
  ok += n; if (gravar && n) fs.writeFileSync(p, JSON.stringify(j, null, 2) + "\n");
}
console.log({ aplicados: ok, materiaAindaNaoPublicada: semMateria, topicoMudouOuJaLigado: mudou, gravado: gravar });
