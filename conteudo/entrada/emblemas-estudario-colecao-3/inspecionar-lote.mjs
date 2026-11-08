// Prancha de QA: posiciona proporcionalmente imagens já geradas, sem repintar.
import fs from 'node:fs/promises';
import path from 'node:path';
import {fileURLToPath} from 'node:url';
import {createRequire} from 'node:module';
const require=createRequire(import.meta.url);
const sharp=require('C:/Users/otavi/.cache/codex-runtimes/codex-primary-runtime/dependencies/node/node_modules/sharp');
const here=path.dirname(fileURLToPath(import.meta.url));
const all=JSON.parse(await fs.readFile(path.join(here,'prompts.json'),'utf8')).badges;
const requested=process.argv.slice(2);
const selected=requested.length?requested.map(id=>all.find(b=>b.id===id)):all.slice(0,10);
if(selected.some(b=>!b)||selected.length>10)throw Error('Escolher de 1 a 10 IDs existentes.');
const esc=s=>String(s).replaceAll('&','&amp;').replaceAll('<','&lt;').replaceAll('>','&gt;');
let svg='<svg xmlns="http://www.w3.org/2000/svg" width="2650" height="1420"><rect width="2650" height="1420" fill="#ece5d5"/><text x="35" y="60" font-size="32" font-family="Georgia" fill="#252548">Coleção III · inspeção de anatomia, pose e moldura</text>';
for(let i=0;i<selected.length;i++){
 const b=selected[i],x=15+(i%5)*530,y=90+Math.floor(i/5)*650;
 const bytes=await sharp(path.join(here,'producao-final/png',b.id+'.png')).resize(500,500,{fit:'contain'}).png().toBuffer();
 svg+=`<image x="${x}" y="${y}" width="500" height="500" href="data:image/png;base64,${bytes.toString('base64')}"/><text x="${x+250}" y="${y+537}" text-anchor="middle" font-size="25" font-family="Segoe UI" fill="#252548">${b.id} · tier ${b.tier}</text><text x="${x+250}" y="${y+573}" text-anchor="middle" font-size="22" font-family="Segoe UI" fill="#575469">${esc(b.name)}</text>`;
}
const destination=path.join(here,'revisao-bracos','lotes');
await fs.mkdir(destination,{recursive:true});
const file=path.join(destination,selected.map(b=>b.id).join('_')+'.png');
await sharp(Buffer.from(svg+'</svg>')).png().toFile(file);
console.log(file);
