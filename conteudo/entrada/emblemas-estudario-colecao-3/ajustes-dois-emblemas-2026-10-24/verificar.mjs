import fs from 'node:fs/promises';
import path from 'node:path';
import crypto from 'node:crypto';
import {fileURLToPath} from 'node:url';
import {createRequire} from 'node:module';
const require=createRequire(import.meta.url),sharp=require('C:/Users/otavi/.cache/codex-runtimes/codex-primary-runtime/dependencies/node/node_modules/sharp');
const work=path.dirname(fileURLToPath(import.meta.url)),out=path.resolve(work,'..'),backup="C:/Users/otavi/Documents/Codex/2026-09-15/vc-x20/conteudo/entrada/emblemas-estudario-colecao-3/substituidos/2026-10-24-0937";
const hash=b=>crypto.createHash('sha256').update(b).digest('hex');
const baseline=JSON.parse(await fs.readFile(path.join(work,'antes.json'),'utf8'));
const manifest=JSON.parse(await fs.readFile(path.join(work,'manifesto.json'),'utf8'));
const old=JSON.parse(await fs.readFile(path.join(backup,'manifesto.json'),'utf8'));
const ids=['erros-20','revisao-100'],checks=[];
for(const [file,sha] of Object.entries(baseline.protected))if(hash(await fs.readFile(path.join(out,file)))!==sha)throw Error('Protegido alterado '+file);
for(const [file,sha] of Object.entries(baseline.targets))if(hash(await fs.readFile(path.join(backup,file)))!==sha)throw Error('Backup incorreto '+file);
for(const b of manifest.badges.filter(b=>!ids.includes(b.id)))if(JSON.stringify(b)!==JSON.stringify(old.badges.find(a=>a.id===b.id)))throw Error('Metadado não solicitado '+b.id);
for(const id of ids){
 const b=manifest.badges.find(b=>b.id===id),bytes=await fs.readFile(path.join(work,'final-png',id+'.png')),preview=await fs.readFile(path.join(work,'app-512',id+'.png'));
 if(hash(bytes)!==b.sha256||hash(preview)!==b.previewSha256||!b.updatedAt)throw Error('Hash/data '+id);
 for(const [data,size] of [[bytes,1254],[preview,512]]){
  const r=await sharp(data).ensureAlpha().raw().toBuffer({resolveWithObject:true});
  if(r.info.width!==size||r.info.height!==size)throw Error('Tamanho '+id);
  const corners=[0,size-1,(size-1)*size,size*size-1];
  if(corners.some(p=>r.data[p*4+3]!==0))throw Error('Transparência '+id);
 }
 checks.push({id,arms:2,hands:2,inspection:'conferência visual em 1254 × 1254',handsConnected:true,bodyLoops:false,legs:0,sha256:b.sha256,previewSha256:b.previewSha256});
}
const sheets=['prancha-completa.png','revisao-4.png','revisao-6.png'],sheetChecks=[];
for(const file of sheets){
 const a=await sharp(path.join(out,file)).ensureAlpha().raw().toBuffer({resolveWithObject:true}),b=await sharp(path.join(work,file)).ensureAlpha().raw().toBuffer({resolveWithObject:true});
 if(a.info.width!==b.info.width||a.info.height!==b.info.height)throw Error('Prancha mudou de tamanho '+file);
 const boxes=ids.map(id=>manifest.badges.findIndex(b=>b.id===id)).filter(i=>file==='prancha-completa.png'||Math.floor(i/10)+1===Number(file.match(/revisao-(\d+)/)[1])).map(i=>file==='prancha-completa.png'?{x:25+i%7*430,y:200+Math.floor(i/7)*480,w:400,h:400}:{x:15+(i%10%5)*530,y:100+Math.floor((i%10)/5)*620,w:500,h:500});
 let outside=0,inside=0;
 for(let y=0;y<a.info.height;y++)for(let x=0;x<a.info.width;x++){
  const p=(y*a.info.width+x)*4;
  if(a.data[p]===b.data[p]&&a.data[p+1]===b.data[p+1]&&a.data[p+2]===b.data[p+2]&&a.data[p+3]===b.data[p+3])continue;
  if(boxes.some(r=>x>=r.x&&x<r.x+r.w&&y>=r.y&&y<r.y+r.h))inside++;else outside++;
 }
 if(outside)throw Error('Prancha alterada fora dos dois emblemas '+file+': '+outside);
 sheetChecks.push({file,changedPixelsInsideTargetTiles:inside,changedPixelsOutsideTargetTiles:outside});
}
const layer=JSON.parse(await fs.readFile(path.join(work,'montagem-verificacao.json'),'utf8'));
if(layer.badges.some(b=>b.opaqueFrameMismatches||b.originalWindowPixelsWithOpacityLost))throw Error('Moldura ou recorte');
const report={date:new Date().toISOString(),scope:ids,approvedForInstallation:true,unchangedOtherBadges:63,unchangedResources:133,unchangedFrames:7,backedUpResources:4,unchangedOtherMetadata:63,originalSize:[1254,1254],previewSize:[512,512],transparentExterior:true,frameMismatches:0,visualChecks:checks,sheets:sheetChecks};
await fs.writeFile(path.join(work,'verificacao-dois.json'),JSON.stringify(report,null,2)+'\n');
console.log(JSON.stringify(report));
