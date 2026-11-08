import fs from 'node:fs/promises';
import path from 'node:path';
import {fileURLToPath} from 'node:url';
import {createRequire} from 'node:module';
const require=createRequire(import.meta.url);
const sharp=require('C:/Users/otavi/.cache/codex-runtimes/codex-primary-runtime/dependencies/node/node_modules/sharp');
const here=path.dirname(fileURLToPath(import.meta.url));
const [id,x,y,w,h,...flags]=process.argv.slice(2);
const out=path.join(here,'zoom');await fs.mkdir(out,{recursive:true});
const from=flags.includes('--antes')?'antes-acabamento':'png';
let pipeline=sharp(path.join(here,from,id+'.png')).extract({left:+x,top:+y,width:+w,height:+h}).resize({width:800,kernel:'nearest'});
if(flags.includes('--grade')){
 const scale=800/(+w),height=Math.round((+h)*scale);let grid=`<svg xmlns="http://www.w3.org/2000/svg" width="800" height="${height}">`;
 for(let i=0;i<=+w;i+=20)grid+=`<path d="M${i*scale} 0V${height}" stroke="#ff5050" stroke-width="1" opacity=".65"/><text x="${i*scale+2}" y="15" font-size="13" fill="#ff2525">${+x+i}</text>`;
 for(let j=0;j<=+h;j+=20)grid+=`<path d="M0 ${j*scale}H800" stroke="#ff5050" stroke-width="1" opacity=".65"/><text x="2" y="${j*scale+14}" font-size="13" fill="#ff2525">${+y+j}</text>`;
 pipeline=pipeline.composite([{input:Buffer.from(grid+'</svg>')}]);
}
const name=id+'-'+x+'-'+y+(flags.includes('--antes')?'-antes':'')+(flags.includes('--grade')?'-grade':'')+'.png';
await pipeline.png().toFile(path.join(out,name));
console.log(path.join(out,name));
