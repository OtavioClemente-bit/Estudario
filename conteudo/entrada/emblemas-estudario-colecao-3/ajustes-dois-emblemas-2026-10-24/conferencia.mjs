import fs from 'node:fs/promises';
import path from 'node:path';
import {createRequire} from 'node:module';
import {fileURLToPath} from 'node:url';
const require=createRequire(import.meta.url),sharp=require('C:/Users/otavi/.cache/codex-runtimes/codex-primary-runtime/dependencies/node/node_modules/sharp');
const work=path.dirname(fileURLToPath(import.meta.url));
let svg='<svg xmlns="http://www.w3.org/2000/svg" width="1320" height="805"><rect width="1320" height="805" fill="#101326"/><text x="50" y="55" font-size="32" font-family="Georgia" fill="#f7ecd8">Coleção III · correção de dois emblemas</text>';
for(const [i,id,name] of [[0,'erros-20','Errou, aprendeu'],[1,'revisao-100','Memória treinada']]){
 const data=await sharp(path.join(work,'final-png',id+'.png')).resize(600,600).png().toBuffer();
 const x=35+i*650;
 svg+='<image x="'+x+'" y="90" width="600" height="600" href="data:image/png;base64,'+data.toString('base64')+'"/><text x="'+(x+300)+'" y="733" text-anchor="middle" font-family="Georgia" font-size="27" fill="#f7ecd8">'+name+'</text><text x="'+(x+300)+'" y="772" text-anchor="middle" font-family="Segoe UI" font-size="20" fill="#c2c7d9">'+id+' · 2 braços · 2 mãos</text>';
}
await sharp(Buffer.from(svg+'</svg>')).png().toFile(path.join(work,'conferencia-dois.png'));
