// Retoques localizados por clonagem da própria pintura e máscaras desenhadas individualmente.
// Autorizados explicitamente pelo dono; não gera personagens, questões nem novas cenas.
import fs from 'node:fs/promises';
import path from 'node:path';
import {fileURLToPath} from 'node:url';
import {createRequire} from 'node:module';
const require=createRequire(import.meta.url);
const sharp=require('C:/Users/otavi/.cache/codex-runtimes/codex-primary-runtime/dependencies/node/node_modules/sharp');
const here=path.dirname(fileURLToPath(import.meta.url));
const doc=JSON.parse(await fs.readFile(path.join(here,'retoques-camadas.json'),'utf8'));
const wanted=process.argv.slice(2),patches=doc.patches.filter(p=>!wanted.length||wanted.includes(p.id));
const ids=[...new Set(patches.map(p=>p.id))];
await fs.mkdir(path.join(here,'antes-acabamento'),{recursive:true});
for(const id of ids){
 const target=path.join(here,'png',id+'.png'),backup=path.join(here,'antes-acabamento',id+'.png');
 try{await fs.access(backup);}catch{await fs.copyFile(target,backup);}
 const original=await fs.readFile(backup);
 const {data:src,info}=await sharp(original).ensureAlpha().raw().toBuffer({resolveWithObject:true});
 const w=info.width,h=info.height,layers=[];
 for(const p of patches.filter(p=>p.id===id)){
  if(p.paintPathsBefore?.length){
   const marks=p.paintPathsBefore.map((s,i)=>`<linearGradient id="g${i}" x1="0" y1="0" x2="0.2" y2="1"><stop stop-color="#6961a5"/><stop offset=".4" stop-color="#42377e"/><stop offset="1" stop-color="#211b4d"/></linearGradient>`).join('');
   const strokes=p.paintPathsBefore.map((s,i)=>`<path d="${s.d}" fill="none" stroke="#14102c" stroke-width="${s.width+5}" stroke-linecap="round"/><path d="${s.d}" fill="none" stroke="url(#g${i})" stroke-width="${s.width}" stroke-linecap="round"/>`).join('');
   const paint=`<svg xmlns="http://www.w3.org/2000/svg" width="${w}" height="${h}"><defs>${marks}</defs>${strokes}</svg>`;
   const raw=await sharp(Buffer.from(paint)).ensureAlpha().raw().toBuffer();
   for(let q=0;q<raw.length;q+=4)if(raw[q+3]){
    const n=1+(((q*1664525+1013904223)>>>0)%101-50)/1250;
    for(let c=0;c<3;c++)raw[q+c]=Math.min(255,Math.round(raw[q+c]*n));
   }
   layers.push({input:await sharp(raw,{raw:{width:w,height:h,channels:4}}).png().toBuffer(),blend:'over'});
  }
  const svg=`<svg xmlns="http://www.w3.org/2000/svg" width="${w}" height="${h}"><polygon points="${p.polygon.map(v=>v.join(',')).join(' ')}" fill="white"/></svg>`;
  let m=sharp(Buffer.from(svg));if(p.feather)m=m.blur(p.feather);
  const mask=await m.ensureAlpha().raw().toBuffer(),layer=Buffer.alloc(w*h*4),[dx,dy]=p.sampleOffset;
  for(let y=0;y<h;y++)for(let x=0;x<w;x++){
   const q=(y*w+x)*4,a=mask[q+3];if(!a)continue;
   const sy=p.sampleMap?Math.round(p.sampleMap.sourceTop+(y-p.sampleMap.targetTop)*p.sampleMap.sourceScaleY):y+dy;
   const sx=p.sampleMap?Math.round(x+p.sampleMap.edgeSlope*(y-sy)):x+dx;
   if(sx<0||sy<0||sx>=w||sy>=h)throw Error('Amostra fora da pintura: '+id);
   const s=(sy*w+sx)*4;layer[q]=src[s];layer[q+1]=src[s+1];layer[q+2]=src[s+2];layer[q+3]=Math.round(a*src[s+3]/255);
  }
  layers.push({input:await sharp(layer,{raw:{width:w,height:h,channels:4}}).png().toBuffer(),blend:'over'});
  if(p.inkPaths?.length){
   const ink=`<svg xmlns="http://www.w3.org/2000/svg" width="${w}" height="${h}">${p.inkPaths.map(s=>`<path d="${s.d}" fill="none" stroke="${s.color}" stroke-width="${s.width}" stroke-linecap="round"/>`).join('')}</svg>`;
   layers.push({input:Buffer.from(ink),blend:'over'});
  }
 }
 await sharp(original).composite(layers).png().toFile(path.join(here,id+'-acabado.png'));
 await fs.copyFile(path.join(here,id+'-acabado.png'),target);
}
console.log(JSON.stringify({artes:ids,mascaras:patches.length,metodo:'clonagem da própria textura com máscara localizada'}));
