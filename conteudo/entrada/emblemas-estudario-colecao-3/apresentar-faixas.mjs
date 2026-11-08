import fs from 'node:fs/promises';
import path from 'node:path';
import {fileURLToPath} from 'node:url';
import {createRequire} from 'node:module';
const require=createRequire(import.meta.url);
const sharp=require('C:/Users/otavi/.cache/codex-runtimes/codex-primary-runtime/dependencies/node/node_modules/sharp');
const base=path.dirname(fileURLToPath(import.meta.url));
const ids=['seq-3','seq-7','seq-14','seq-30','seq-60','seq-100','seq-365'];
const labels=['Bronze · aro simples','Prata · gravuras e ramos','Ouro · louros completos','Safira · camada lapidada','Rubi · ramificações e platina','Esmeralda · filigrana e ouro','Anual · halo e quatro estações'];
let svg='<svg xmlns="http://www.w3.org/2000/svg" width="2000" height="1400"><rect width="2000" height="1400" fill="#13182e"/><text x="65" y="80" font-size="46" font-family="Georgia" fill="#f7ecd8">Estudário · a conquista evolui por fora também</text><text x="65" y="130" font-size="24" font-family="Segoe UI" fill="#b8c4ce">Coleção III · sete níveis de auréola, do cobre simples ao anual radiante</text>';
for(let i=0;i<7;i++){
 const x=i<4?25+i*495:270+(i-4)*495,y=i<4?190:790;
 const buffer=await sharp(path.join(base,'png',ids[i]+'.png')).resize(470,470,{fit:'contain'}).png().toBuffer();
 svg+=`<image x="${x}" y="${y}" width="470" height="470" href="data:image/png;base64,${buffer.toString('base64')}"/><text x="${x+235}" y="${y+515}" text-anchor="middle" font-size="23" font-family="Segoe UI" fill="#efe8d8">${labels[i]}</text>`;
}
await sharp(Buffer.from(svg+'</svg>')).png().toFile(path.join(base,'evolucao-aureolas.png'));
console.log('evolucao-aureolas.png');
