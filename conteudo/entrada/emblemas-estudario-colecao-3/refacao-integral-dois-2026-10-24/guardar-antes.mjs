import fs from 'node:fs/promises';
import path from 'node:path';
import crypto from 'node:crypto';
import {fileURLToPath} from 'node:url';
const work=path.dirname(fileURLToPath(import.meta.url)),out=path.resolve(work,'..'),backup=path.join(out,'substituidos/2026-10-24-refacao-integral');
const ids=['erros-20','revisao-100'],hash=b=>crypto.createHash('sha256').update(b).digest('hex'),record={targets:{},protected:{}};
for(const dir of ['png','app-512','molduras'])for(const name of await fs.readdir(path.join(out,dir))){
 if(!name.endsWith('.png'))continue;
 const file=dir+'/'+name,bytes=await fs.readFile(path.join(out,file));
 if(dir!=='molduras'&&ids.includes(name.slice(0,-4))){
  await fs.mkdir(path.join(backup,dir),{recursive:true});
  try{await fs.access(path.join(backup,file));}catch{await fs.writeFile(path.join(backup,file),bytes);}
  record.targets[file]=hash(bytes);
 }else record.protected[file]=hash(bytes);
}
for(const name of ['manifesto.json','LEIA-ME.md','prancha-completa.png','revisao-4.png','revisao-6.png']){
 try{await fs.access(path.join(backup,name));}catch{await fs.copyFile(path.join(out,name),path.join(backup,name));}
}
await fs.writeFile(path.join(work,'antes.json'),JSON.stringify(record,null,2)+'\n');
console.log(JSON.stringify({backedUp:4,protected:Object.keys(record.protected).length}));
