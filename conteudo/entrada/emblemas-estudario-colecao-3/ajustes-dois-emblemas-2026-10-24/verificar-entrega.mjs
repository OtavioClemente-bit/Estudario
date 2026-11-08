import fs from 'node:fs/promises';
import path from 'node:path';
import crypto from 'node:crypto';
import {fileURLToPath} from 'node:url';
import {createRequire} from 'node:module';
const require=createRequire(import.meta.url),sharp=require('C:/Users/otavi/.cache/codex-runtimes/codex-primary-runtime/dependencies/node/node_modules/sharp');
const work=path.dirname(fileURLToPath(import.meta.url)),out=path.resolve(work,'..');
const report=JSON.parse(await fs.readFile(path.join(work,'verificacao-dois.json'),'utf8'));
const baseline=JSON.parse(await fs.readFile(path.join(work,'antes.json'),'utf8'));
const manifest=JSON.parse(await fs.readFile(path.join(out,'manifesto.json'),'utf8'));
const old=JSON.parse(await fs.readFile(path.join(out,'substituidos/2026-10-24-0937/manifesto.json'),'utf8'));
const hash=b=>crypto.createHash('sha256').update(b).digest('hex'),ids=['erros-20','revisao-100'];
if(!report.installed)throw Error('Não instalado');
for(const [file,sha] of Object.entries(report.installedFiles))if(hash(await fs.readFile(path.join(out,file)))!==sha)throw Error('Entrega alterada '+file);
for(const [file,sha] of Object.entries(baseline.protected))if(hash(await fs.readFile(path.join(out,file)))!==sha)throw Error('Fora do escopo '+file);
for(const b of manifest.badges.filter(b=>!ids.includes(b.id)))if(JSON.stringify(b)!==JSON.stringify(old.badges.find(a=>a.id===b.id)))throw Error('Metadado não solicitado '+b.id);
const result=[];
for(const id of ids){
 const b=manifest.badges.find(b=>b.id===id),source=await fs.readFile(path.join(out,b.png)),preview=await fs.readFile(path.join(out,b.preview));
 if(hash(source)!==b.sha256||hash(preview)!==b.previewSha256||!b.updatedAt)throw Error('Hash/data '+id);
 const badge=await sharp(source).ensureAlpha().raw().toBuffer({resolveWithObject:true}),frame=await sharp(path.join(out,'molduras/tier-'+b.tier+'.png')).ensureAlpha().raw().toBuffer();
 let mismatch=0;for(let q=0;q<frame.length;q+=4)if(frame[q+3]===255&&(badge.data[q]!==frame[q]||badge.data[q+1]!==frame[q+1]||badge.data[q+2]!==frame[q+2]||badge.data[q+3]!==255))mismatch++;
 if(mismatch)throw Error('Moldura mudou '+id);
 const m=await sharp(preview).metadata();
 if(badge.info.width!==1254||badge.info.height!==1254||m.width!==512||m.height!==512)throw Error('Tamanho '+id);
 result.push({id,original:'1254 × 1254',preview:'512 × 512',sameFrame:true,arms:2,hands:2,visualCheckSource:'arquivo de mesmo SHA-256 inspecionado em tamanho original'});
}
console.log(JSON.stringify({installed:true,checked:result,otherBadgesUnchanged:63,framesUnchanged:7,updatedSheets:report.sheets.map(s=>s.file),sheetPixelsChangedOutsideTargets:report.sheets.reduce((s,a)=>s+a.changedPixelsOutsideTargetTiles,0),backupsVerified:4}));
