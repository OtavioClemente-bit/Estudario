import fs from 'node:fs/promises';
import path from 'node:path';
import crypto from 'node:crypto';
const out="C:/Users/otavi/Documents/Codex/2026-09-15/vc-x20/conteudo/entrada/emblemas-estudario-colecao-3",work="C:/Users/otavi/Documents/Codex/2026-09-15/vc-x20/conteudo/entrada/emblemas-estudario-colecao-3/ajustes-dois-emblemas-2026-10-24",backup="C:/Users/otavi/Documents/Codex/2026-09-15/vc-x20/conteudo/entrada/emblemas-estudario-colecao-3/substituidos/2026-10-24-0937";
const ids=['erros-20','revisao-100'],sha=b=>crypto.createHash('sha256').update(b).digest('hex');
await fs.mkdir(work,{recursive:true});await fs.mkdir(path.join(work,'originais'),{recursive:true});
const baseline={date:'2026-10-24',targets:{},protected:{}};
for(const dir of ['png','app-512','molduras']){
 for(const name of await fs.readdir(path.join(out,dir))){
  if(!name.endsWith('.png'))continue;
  const relative=dir+'/'+name,bytes=await fs.readFile(path.join(out,relative));
  if(ids.includes(name.slice(0,-4))&&dir!=='molduras'){
   await fs.mkdir(path.join(backup,dir),{recursive:true});
   try{await fs.access(path.join(backup,relative));}catch{await fs.writeFile(path.join(backup,relative),bytes);}
   baseline.targets[relative]=sha(bytes);
  }else baseline.protected[relative]=sha(bytes);
 }
}
for(const name of ['manifesto.json','LEIA-ME.md']){
 await fs.copyFile(path.join(out,name),path.join(backup,name));
}
for(const id of ids)await fs.copyFile(path.join(out,'producao-final/acabamento-pendente/originais',id+'.png'),path.join(work,'originais',id+'.png'));
await fs.writeFile(path.join(work,'antes.json'),JSON.stringify(baseline,null,2)+'\n');
console.log(JSON.stringify({backedUp:4,protected:Object.keys(baseline.protected).length,backup}));
