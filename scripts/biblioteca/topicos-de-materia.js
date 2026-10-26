// Lista os textos de edital ligados a uma matéria (mais frequentes primeiro). Só lista.  node topicos-de.js <id> [n]
const fs=require("fs");const ED="C:/Users/otavi/Documents/Codex/2026-09-15/vc-x20/conteudo/editais/";const [id,n]=process.argv.slice(2);const m=new Map();
for(const f of fs.readdirSync(ED).filter(f=>f.endsWith(".json"))){const j=JSON.parse(fs.readFileSync(ED+f));for(const d of j.disciplinas||[])for(const x of d.topicos)if(x.topico===id)m.set(x.texto,(m.get(x.texto)||0)+1)}
[...m].sort((a,b)=>b[1]-a[1]).slice(0,+n||60).forEach(([t,c])=>console.log(`[${c}] ${t}`));
