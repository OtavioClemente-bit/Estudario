import fs from 'node:fs/promises';
import path from 'node:path';
import crypto from 'node:crypto';
import {fileURLToPath} from 'node:url';
const work=path.dirname(fileURLToPath(import.meta.url)),out=path.resolve(work,'..'),backup=path.join(out,'substituidos/2026-10-24-chatgpt-seis');
const ids=["maratona-480","meta-1","quest-5000","revisao-1000","seq-365","topico-50"],hash=b=>crypto.createHash('sha256').update(b).digest('hex'),record={date:new Date().toISOString(),ids,targets:{},protected:{},backup:path.relative(out,backup)};
for(const dir of ['png','app-512','molduras'])for(const name of await fs.readdir(path.join(out,dir))){
 if(!name.endsWith('.png'))continue;
 const relative=dir+'/'+name,bytes=await fs.readFile(path.join(out,relative));
 if(dir!=='molduras'&&ids.includes(name.slice(0,-4))){
  await fs.mkdir(path.dirname(path.join(backup,relative)),{recursive:true});
  try{await fs.access(path.join(backup,relative));}catch{await fs.writeFile(path.join(backup,relative),bytes);}
  record.targets[relative]=hash(bytes);
 }else record.protected[relative]=hash(bytes);
}
for(const name of ['manifesto.json','LEIA-ME.md','prancha-completa.png','revisao-2.png','revisao-3.png','revisao-4.png','revisao-5.png','evolucao-aureolas.png','dados.js']){
 try{await fs.access(path.join(backup,name));}catch{await fs.copyFile(path.join(out,name),path.join(backup,name));}
}
await fs.mkdir(path.join(work,'geradas'),{recursive:true});await fs.mkdir(path.join(work,'png'),{recursive:true});
await fs.writeFile(path.join(work,'antes.json'),JSON.stringify(record,null,2)+'\n');
console.log(JSON.stringify({targetResources:Object.keys(record.targets).length,protectedResources:Object.keys(record.protected).length,backup:record.backup}));
