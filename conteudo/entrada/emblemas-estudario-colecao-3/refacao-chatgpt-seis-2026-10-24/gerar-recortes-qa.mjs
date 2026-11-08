import fs from 'node:fs/promises';
import path from 'node:path';
import {fileURLToPath} from 'node:url';
import {createRequire} from 'node:module';
const require=createRequire(import.meta.url),sharp=require('C:/Users/otavi/.cache/codex-runtimes/codex-primary-runtime/dependencies/node/node_modules/sharp');
const work=path.dirname(fileURLToPath(import.meta.url));
await fs.mkdir(path.join(work,'qa-crops'),{recursive:true});await fs.mkdir(path.join(work,'app-512'),{recursive:true});
const crops=[
 ['maratona-480',350,410,660,445],['meta-1',310,300,740,460],['quest-5000',245,320,680,460],
 ['revisao-1000',220,450,810,410],['seq-365',300,380,660,450],['topico-50',420,350,515,500]
];
for(const [id,left,top,width,height] of crops){
 const source=path.join(work,'final-png',id+'.png');
 await sharp(source).extract({left,top,width,height}).png().toFile(path.join(work,'qa-crops',id+'.png'));
 await sharp(source).resize(512,512,{fit:'contain',background:{r:0,g:0,b:0,alpha:0}}).png().toFile(path.join(work,'app-512',id+'.png'));
}
console.log(JSON.stringify({nativeCrops:crops.length,previews:6}));

