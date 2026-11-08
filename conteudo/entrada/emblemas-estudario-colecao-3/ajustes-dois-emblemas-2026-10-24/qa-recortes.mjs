import fs from 'node:fs/promises';
import path from 'node:path';
import {createRequire} from 'node:module';
const require=createRequire(import.meta.url),sharp=require('C:/Users/otavi/.cache/codex-runtimes/codex-primary-runtime/dependencies/node/node_modules/sharp');
const root="C:/Users/otavi/Documents/Codex/2026-09-15/vc-x20/conteudo/entrada/emblemas-estudario-colecao-3/ajustes-dois-emblemas-2026-10-24";
for(const [id,x,y,w,h] of [['revisao-100',270,435,420,290],['erros-20',180,540,450,220]]){
 const bytes=await sharp(path.join(root,process.argv[2]||'png',id+'.png')).extract({left:x,top:y,width:w,height:h}).resize(w*2,h*2).png().toBuffer();
 let svg='<svg xmlns="http://www.w3.org/2000/svg" width="'+w*2+'" height="'+h*2+'">';
 for(let n=Math.ceil(x/25)*25;n<x+w;n+=25){const xx=(n-x)*2;svg+='<path d="M '+xx+' 0 V '+h*2+'" stroke="#ff00c8" opacity=".4"/><text x="'+(xx+3)+'" y="18" font-family="Segoe UI" font-size="15" fill="white" stroke="black" stroke-width=".3">'+n+'</text>';}
 for(let n=Math.ceil(y/25)*25;n<y+h;n+=25){const yy=(n-y)*2;svg+='<path d="M 0 '+yy+' H '+w*2+'" stroke="#ff00c8" opacity=".4"/><text x="3" y="'+(yy+17)+'" font-family="Segoe UI" font-size="15" fill="white" stroke="black" stroke-width=".3">'+n+'</text>';}
 await sharp(bytes).composite([{input:Buffer.from(svg+'</svg>')}]).png().toFile(path.join(root,id+'-'+(process.argv[2]||'png')+'-grade.png'));
}
