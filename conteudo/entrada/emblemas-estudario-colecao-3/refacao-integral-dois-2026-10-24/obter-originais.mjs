import fs from 'node:fs/promises';
import path from 'node:path';
import {fileURLToPath} from 'node:url';
import {createRequire} from 'node:module';
const require=createRequire(import.meta.url),sharp=require('C:/Users/otavi/.cache/codex-runtimes/codex-primary-runtime/dependencies/node/node_modules/sharp');
const work=path.dirname(fileURLToPath(import.meta.url));
const sources=JSON.parse(await fs.readFile(path.join(work,'downloads-originais.json'),'utf8'));
await fs.mkdir(path.join(work,'geradas'),{recursive:true});
await fs.mkdir(path.join(work,'png'),{recursive:true});
const results=[];
for(const source of sources){
 // Use only the unmodified original-file URL exposed by Canva after sign-in.
 const response=await fetch(source.url),bytes=Buffer.from(await response.arrayBuffer());
 if(!response.ok)throw Error('Download recusado '+source.id+': '+response.status);
 const m=await sharp(bytes).metadata();
 if(m.width!==1264||m.height!==1264)throw Error('Arquivo recebido não é o original 1264 × 1264: '+source.id+' '+m.width+'x'+m.height);
 await sharp(bytes).png().toFile(path.join(work,'geradas',source.id+'.png'));
 await sharp(bytes).resize(1254,1254,{fit:'contain'}).png().toFile(path.join(work,'png',source.id+'.png'));
 results.push({id:source.id,sourceWidth:m.width,sourceHeight:m.height,sourceFormat:m.format,canvas:[1254,1254]});
}
await fs.writeFile(path.join(work,'arquivos-canva.json'),JSON.stringify(results,null,2)+'\n');
console.log(JSON.stringify(results));
