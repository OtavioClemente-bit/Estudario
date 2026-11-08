import fs from 'node:fs/promises';
import path from 'node:path';
import crypto from 'node:crypto';
import {fileURLToPath} from 'node:url';
const work=path.dirname(fileURLToPath(import.meta.url)),out=path.resolve(work,'..'),entrada=path.resolve(out,'..');
const baseline=JSON.parse(await fs.readFile(path.join(work,'antes.json'),'utf8')),backup=path.join(out,baseline.backup);
const verify=JSON.parse(await fs.readFile(path.join(work,'verificacao-pre-instalacao.json'),'utf8'));
const hash=b=>crypto.createHash('sha256').update(b).digest('hex');
if(!verify.approved||verify.installed||verify.scope.join()!==baseline.ids.join())throw Error('Verificação prévia ausente');
if(!out.startsWith(entrada+path.sep)||!work.startsWith(out+path.sep)||!backup.startsWith(out+path.sep))throw Error('Pasta fora do escopo');
for(const [file,sha] of Object.entries({...baseline.targets,...baseline.protected}))if(hash(await fs.readFile(path.join(out,file)))!==sha)throw Error('Baseline mudou '+file);
const mappings=baseline.ids.flatMap(id=>[['final-png/'+id+'.png','png/'+id+'.png'],['app-512/'+id+'.png','app-512/'+id+'.png']]);
for(const [from,to] of [['manifesto.json','manifesto.json'],['prompts-colecao.json','prompts.json'],['LEIA-ME.md','LEIA-ME.md'],['dados.js','dados.js'],...['prancha-completa.png','revisao-2.png','revisao-3.png','revisao-4.png','revisao-5.png','evolucao-aureolas.png'].map(f=>[f,f])])mappings.push([from,to]);
for(const [from,to] of mappings){
 const source=path.resolve(work,from),dest=path.resolve(out,to),old=path.resolve(backup,to);
 if(!source.startsWith(work+path.sep)||!dest.startsWith(out+path.sep)||!old.startsWith(backup+path.sep))throw Error('Caminho fora do pacote');
 await fs.access(source);await fs.mkdir(path.dirname(old),{recursive:true});
 try{await fs.access(old);}catch{await fs.copyFile(dest,old);}
}
const changed=[];
try{
 for(const [from,to] of mappings){await fs.copyFile(path.join(work,from),path.join(out,to));changed.push(to);}
 for(const [from,to] of mappings)if(hash(await fs.readFile(path.join(work,from)))!==hash(await fs.readFile(path.join(out,to))))throw Error('Cópia divergente '+to);
 for(const [file,sha] of Object.entries(baseline.protected))if(hash(await fs.readFile(path.join(out,file)))!==sha)throw Error('Protegido alterado '+file);
}catch(error){for(const to of changed)await fs.copyFile(path.join(backup,to),path.join(out,to));throw error;}
await fs.writeFile(path.join(work,'instalacao.json'),JSON.stringify({date:new Date().toISOString(),changed,backup:baseline.backup,protectedResourcesUnchanged:125},null,2)+'\n');
console.log(JSON.stringify({installedFiles:changed.length,ids:baseline.ids,protectedResourcesUnchanged:125,backup:baseline.backup}));

