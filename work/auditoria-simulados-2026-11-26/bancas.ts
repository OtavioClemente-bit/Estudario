// Grafias de banca unificadas (auditoria de 26/11): só o campo "banca"; o resto do arquivo fica igual.
const MAPA: Record<string, string> = {
  "Fundação Getulio Vargas (FGV)": "FGV", "Fundação Getúlio Vargas (FGV)": "FGV", "Fundação Getulio Vargas": "FGV",
  "Fundação Carlos Chagas (FCC)": "FCC", "Fundação Carlos Chagas": "FCC",
  "IDECAN": "Idecan", "Instituto Idecan": "Idecan",
  "FUNDATEC": "Fundatec", "Fundação Fundatec": "Fundatec",
  "Fundação Vunesp": "Vunesp", "VUNESP": "Vunesp",
  "AOCP": "Instituto AOCP",
};
const c: Record<string, number> = {};
for (const dir of ["conteudo/simulados", "conteudo/editais"]) for (const e of Deno.readDirSync(dir)) {
  if (!e.name.endsWith(".json")) continue;
  const p = `${dir}/${e.name}`; const raw = Deno.readTextFileSync(p);
  const m = raw.match(/"banca": "([^"]*)"/); if (!m || !MAPA[m[1]]) continue;
  const novo = raw.replace(`"banca": "${m[1]}"`, `"banca": "${MAPA[m[1]]}"`);
  for (let t = 0; ; t++) { try { Deno.writeTextFileSync(p, novo); break; } catch (x) { if (t > 20) throw x; await new Promise((r) => setTimeout(r, 500)); } }
  c[`${dir.split("/")[1]}: ${m[1]} → ${MAPA[m[1]]}`] = (c[`${dir.split("/")[1]}: ${m[1]} → ${MAPA[m[1]]}`] ?? 0) + 1;
}
console.log(c);
