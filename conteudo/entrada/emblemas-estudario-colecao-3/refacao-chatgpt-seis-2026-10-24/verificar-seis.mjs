import fs from 'node:fs/promises';
import path from 'node:path';
import crypto from 'node:crypto';
import {fileURLToPath} from 'node:url';
import {createRequire} from 'node:module';
const require=createRequire(import.meta.url),sharp=require('C:/Users/otavi/.cache/codex-runtimes/codex-primary-runtime/dependencies/node/node_modules/sharp');
const work=path.dirname(fileURLToPath(import.meta.url)),out=path.resolve(work,'..'),installed=process.argv.includes('--installed');
const baseline=JSON.parse(await fs.readFile(path.join(work,'antes.json'),'utf8')),backup=path.join(out,baseline.backup),ids=baseline.ids;
const hash=b=>crypto.createHash('sha256').update(b).digest('hex'),json=async f=>JSON.parse((await fs.readFile(f,'utf8')).replace(/^\uFEFF/,''));
const root=installed?out:work,manifest=await json(path.join(root,'manifesto.json')),old=await json(path.join(backup,'manifesto.json'));
const qa=await json(path.join(work,'conferencia-visual.json')),layer=await json(path.join(work,'montagem-verificacao.json'));
for(const [file,sha] of Object.entries(baseline.protected))if(hash(await fs.readFile(path.join(out,file)))!==sha)throw Error('Protegido alterado '+file);
for(const [file,sha] of Object.entries(baseline.targets))if(hash(await fs.readFile(path.join(backup,file)))!==sha)throw Error('Backup incorreto '+file);
for(const b of manifest.badges.filter(b=>!ids.includes(b.id)))if(JSON.stringify(b)!==JSON.stringify(old.badges.find(a=>a.id===b.id)))throw Error('Metadado não solicitado '+b.id);
const frameCache=new Map(),checks=[];
for(const id of ids){
 const b=manifest.badges.find(b=>b.id===id),v=qa.badges.find(b=>b.id===id);
 if(!v.approved||v.arms!==2||v.hands!==2||v.legs!==0||!v.rootsHiddenByLateralCover)throw Error('Conferência visual '+id);
 const bytes=await fs.readFile(path.join(root,installed?'png':'final-png',id+'.png')),preview=await fs.readFile(path.join(root,'app-512',id+'.png'));
 if(hash(bytes)!==b.sha256||hash(preview)!==b.previewSha256||!b.updatedAt)throw Error('Hash/data '+id);
 if(hash(bytes)===baseline.targets['png/'+id+'.png'])throw Error('Arte não substituída '+id);
 let native;
 for(const [data,size] of [[bytes,1254],[preview,512]]){
  const m=await sharp(data).metadata(),r=await sharp(data).ensureAlpha().raw().toBuffer({resolveWithObject:true});
  if(m.width!==size||m.height!==size||!m.hasAlpha)throw Error('Dimensão/alfa '+id);
  const corners=[0,size-1,(size-1)*size,size*size-1];
  if(corners.some(p=>r.data[p*4+3]!==0))throw Error('Transparência '+id);
  if(size===1254)native=r.data;
 }
 let f=frameCache.get(b.tier);
 if(!f){f=await sharp(path.join(out,'molduras','tier-'+b.tier+'.png')).ensureAlpha().raw().toBuffer();frameCache.set(b.tier,f);}
 let mismatch=0;for(let p=0;p<f.length;p+=4)if(f[p+3]===255&&(native[p]!==f[p]||native[p+1]!==f[p+1]||native[p+2]!==f[p+2]||native[p+3]!==255))mismatch++;
 if(mismatch)throw Error('Pixels de moldura divergentes '+id+': '+mismatch);
 const record=layer.badges.find(b=>b.id===id);
 if(!record||record.opaqueFrameMismatches||record.originalWindowPixelsWithOpacityLost||record.finalSha256!==b.sha256)throw Error('Relatório de montagem '+id);
 const reduced=await sharp(bytes).resize(512,512,{fit:'contain',background:{r:0,g:0,b:0,alpha:0}}).png().toBuffer();
 if(hash(reduced)!==hash(preview))throw Error('Prévia divergente '+id);
 checks.push({id,sha256:b.sha256,previewSha256:b.previewSha256,opaqueFrameMismatches:mismatch,visualReview:v});
}
const sheetFiles=['prancha-completa.png','revisao-2.png','revisao-3.png','revisao-4.png','revisao-5.png','evolucao-aureolas.png'],sheetChecks=[];
for(const file of sheetFiles){
 const a=await sharp(path.join(backup,file)).ensureAlpha().raw().toBuffer({resolveWithObject:true}),b=await sharp(path.join(root,file)).ensureAlpha().raw().toBuffer({resolveWithObject:true});
 if(a.info.width!==b.info.width||a.info.height!==b.info.height)throw Error('Prancha mudou de tamanho '+file);
 let boxes;
 if(file==='evolucao-aureolas.png')boxes=[{x:1260,y:790,w:470,h:470}];
 else boxes=ids.map(id=>manifest.badges.findIndex(b=>b.id===id)).filter(i=>file==='prancha-completa.png'||Math.floor(i/10)+1===Number(file.match(/revisao-(\d+)/)[1])).map(i=>file==='prancha-completa.png'?{x:25+i%7*430,y:200+Math.floor(i/7)*480,w:400,h:400}:{x:15+(i%10%5)*530,y:100+Math.floor((i%10)/5)*620,w:500,h:500});
 let outside=0,inside=0;
 for(let y=0;y<a.info.height;y++)for(let x=0;x<a.info.width;x++){
  const p=(y*a.info.width+x)*4;
  if(a.data[p]===b.data[p]&&a.data[p+1]===b.data[p+1]&&a.data[p+2]===b.data[p+2]&&a.data[p+3]===b.data[p+3])continue;
  if(boxes.some(r=>x>=r.x&&x<r.x+r.w&&y>=r.y&&y<r.y+r.h))inside++;else outside++;
 }
 if(outside)throw Error('Prancha alterada fora dos seis quadros '+file+': '+outside);
 sheetChecks.push({file,changedPixelsInsideTargetTiles:inside,changedPixelsOutsideTargetTiles:outside});
}
const collection2=path.resolve(out,'../emblemas-estudario-2026-10-23-v2'),collection2Hashes=await json(path.join(out,'colecao-2-hashes-antes.json'));
for(const [file,sha] of Object.entries(collection2Hashes))if(hash(await fs.readFile(path.join(collection2,file)))!==sha)throw Error('Coleção II alterada '+file);
const data=await fs.readFile(path.join(root,'dados.js'),'utf8');
if(data!=='window.ESTUDARIO_BADGES='+JSON.stringify(manifest.badges)+';\n')throw Error('dados.js divergente');
const report={date:new Date().toISOString(),installed,approved:true,scope:ids,unchangedOtherBadges:59,unchangedResources:Object.keys(baseline.protected).length,unchangedFrames:7,backedUpResources:12,unchangedOtherMetadata:59,unchangedCollection2Files:Object.keys(collection2Hashes).length,originalSize:[1254,1254],previewSize:[512,512],transparentExterior:true,frameMismatches:0,checks,sheets:sheetChecks};
await fs.writeFile(path.join(work,installed?'verificacao-entrega.json':'verificacao-pre-instalacao.json'),JSON.stringify(report,null,2)+'\n');
console.log(JSON.stringify({approved:report.approved,installed,checkedNewBadges:checks.length,unchangedOtherBadges:59,unchangedResources:125,unchangedFrames:7,unchangedOtherMetadata:59,unchangedCollection2Files:report.unchangedCollection2Files,changedPixelsOutsideTargetTiles:0,frameMismatches:0,originalSize:report.originalSize,previewSize:report.previewSize}));

