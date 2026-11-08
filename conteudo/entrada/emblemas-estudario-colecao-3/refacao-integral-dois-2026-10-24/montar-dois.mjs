// Acabamento em camadas autorizado pelo dono em 23/10/2026.
// Reutiliza literalmente o mesmo PNG de moldura por faixa; não inventa cenas.
import fs from 'node:fs/promises';
import path from 'node:path';
import crypto from 'node:crypto';
import {fileURLToPath} from 'node:url';
import {createRequire} from 'node:module';
const require=createRequire(import.meta.url);
const sharp=require('C:/Users/otavi/.cache/codex-runtimes/codex-primary-runtime/dependencies/node/node_modules/sharp');
const work=path.dirname(fileURLToPath(import.meta.url)),here=path.resolve(work,'..');
await fs.mkdir(path.join(work,'final-png'),{recursive:true});
const doc=JSON.parse(await fs.readFile(path.join(here,'prompts.json'),'utf8'));
const targets=process.argv.slice(2);
const badges=targets.length?targets.map(id=>doc.badges.find(b=>b.id===id)):doc.badges;
if(badges.some(b=>!b))throw Error('ID inválido.');
const frameCache=new Map(),reports=[];
const hash=b=>crypto.createHash('sha256').update(b).digest('hex');
for(const b of badges){
 let frame=frameCache.get(b.tier);
 if(!frame){
  const bytes=await fs.readFile(path.join(here,'molduras','tier-'+b.tier+'.png'));
  const {data,info}=await sharp(bytes).ensureAlpha().raw().toBuffer({resolveWithObject:true});
  const {width:w,height:h}=info,n=w*h,mask=new Uint8Array(n),queue=new Int32Array(n);
  let head=0,tail=0;const seed=Math.floor(h/2)*w+Math.floor(w/2);
  if(data[seed*4+3]>5)throw Error('Centro da moldura não é transparente: '+b.tier);
  queue[tail++]=seed;mask[seed]=255;
  while(head<tail){
   const p=queue[head++],x=p%w,y=Math.floor(p/w);
   for(const q of [x>0?p-1:-1,x<w-1?p+1:-1,y>0?p-w:-1,y<h-1?p+w:-1]){
    if(q>=0&&!mask[q]&&data[q*4+3]<128){mask[q]=255;queue[tail++]=q;}
   }
  }
  // Margem sob a borda antialias: o PNG constante da moldura cobre a transição.
  const dilated=mask.slice();let expandedTail=tail;
  for(let pass=0;pass<3;pass++){
   const limit=expandedTail;
   for(let i=0;i<limit;i++){
    const p=queue[i],x=p%w,y=Math.floor(p/w);
    for(const q of [x>0?p-1:-1,x<w-1?p+1:-1,y>0?p-w:-1,y<h-1?p+w:-1]){
     if(q>=0&&!dilated[q]){dilated[q]=255;queue[expandedTail++]=q;}
    }
   }
  }
  const rgba=Buffer.alloc(n*4);
  for(let p=0;p<n;p++){rgba[p*4]=255;rgba[p*4+1]=255;rgba[p*4+2]=255;rgba[p*4+3]=dilated[p];}
  const maskPng=await sharp(rgba,{raw:{width:w,height:h,channels:4}}).png().toBuffer();
  const opaque=[];for(let p=0;p<n;p++)if(data[p*4+3]===255)opaque.push(p);
  if(rgba[seed*4+3]!==255||rgba[3]!==0)throw Error('Máscara central inválida: '+b.tier);
  frame={bytes,data,w,h,mask,maskPng,opaque,sha256:hash(bytes)};frameCache.set(b.tier,frame);
 }
 const sourceFile=path.join(work,'png',b.id+'.png');
 const source=await fs.readFile(sourceFile);
 const sourceRaw=await sharp(source).ensureAlpha().raw().toBuffer();
 const m=await sharp(source).metadata();
 if(m.width!==frame.w||m.height!==frame.h)throw Error('Dimensões incompatíveis: '+b.id);
 const inner=await sharp(source).ensureAlpha().composite([{input:frame.maskPng,blend:'dest-in'}]).png().toBuffer();
 const final=await sharp(inner).composite([{input:frame.bytes,blend:'over'}]).png().toBuffer();
 const actual=await sharp(final).ensureAlpha().raw().toBuffer();
 let mismatches=0;
 for(const p of frame.opaque){const q=p*4;if(actual[q]!==frame.data[q]||actual[q+1]!==frame.data[q+1]||actual[q+2]!==frame.data[q+2]||actual[q+3]!==255)mismatches++;}
 if(mismatches)throw Error('Moldura mudou em '+mismatches+' pixels opacos: '+b.id);
 let windowPixels=0,windowOpaque=0,opacityLost=0;
 for(let p=0;p<frame.mask.length;p++)if(frame.mask[p]){windowPixels++;if(actual[p*4+3]>250)windowOpaque++;if(actual[p*4+3]+1<sourceRaw[p*4+3])opacityLost++;}
 const windowOpaqueRatio=windowOpaque/windowPixels;
 if(opacityLost)throw Error('A composição perdeu opacidade original em '+opacityLost+' pixels: '+b.id);
 await fs.writeFile(path.join(work,'final-png',b.id+'.png'),final);
 reports.push({id:b.id,tier:b.tier,frame:'molduras/tier-'+b.tier+'.png',frameSha256:frame.sha256,opaqueFramePixels:frame.opaque.length,opaqueFrameMismatches:0,windowOpaqueRatio,originalWindowPixelsWithOpacityLost:opacityLost,sourceSha256:hash(source),finalSha256:hash(final)});
}
await fs.writeFile(path.join(work,'montagem-verificacao.json'),JSON.stringify({authorization:'Acabamento manual por recortes, máscaras e sobreposições autorizado explicitamente pelo dono em 2026-10-23.',count:reports.length,badges:reports},null,2)+'\n');
console.log(JSON.stringify({montados:reports.length,moldurasReutilizadas:frameCache.size,pixelsOpacosDivergentes:0}));
