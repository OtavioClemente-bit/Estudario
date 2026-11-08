import fs from 'node:fs/promises';
import path from 'node:path';
import crypto from 'node:crypto';
import {fileURLToPath} from 'node:url';
import {createRequire} from 'node:module';
const require=createRequire(import.meta.url),sharp=require('C:/Users/otavi/.cache/codex-runtimes/codex-primary-runtime/dependencies/node/node_modules/sharp');
const work=path.dirname(fileURLToPath(import.meta.url)),OUT=path.resolve(work,'..'),OLD=path.resolve(OUT,'../emblemas-estudario-2026-10-23-v2');
const manifest=JSON.parse(await fs.readFile(path.join(OUT,'manifesto.json'),'utf8'));
const previous=JSON.parse(await fs.readFile(path.join(OLD,'manifesto.json'),'utf8'));
const targets=['erros-20','revisao-100'],badges=manifest.badges;
const stamp=new Date().toISOString(),hash=b=>crypto.createHash('sha256').update(b).digest('hex');
const oldManifest=JSON.parse(await fs.readFile(path.join(OUT,'substituidos/2026-10-24-0937/manifesto.json'),'utf8'));
const baseline=JSON.parse(await fs.readFile(path.join(work,'antes.json'),'utf8'));
for(const [file,sha] of Object.entries(baseline.protected))if(hash(await fs.readFile(path.join(OUT,file)))!==sha)throw Error('Arquivo protegido alterado '+file);
await fs.mkdir(path.join(work,'app-512'),{recursive:true});
for(const id of targets){
 const file=path.join(work,'final-png',id+'.png'),bytes=await fs.readFile(file);
 const {width,height,hasAlpha}=await sharp(bytes).metadata();
 if(width!==1254||height!==1254||!hasAlpha)throw Error('Original inválido '+id);
 const preview=await sharp(bytes).resize(512,512,{fit:'contain',background:{r:0,g:0,b:0,alpha:0}}).png().toBuffer();
 await fs.writeFile(path.join(work,'app-512',id+'.png'),preview);
 const b=badges.find(b=>b.id===id);
 b.sha256=hash(bytes);b.previewSha256=hash(preview);b.updatedAt=stamp;
 b.qaNote=id==='erros-20'
  ? 'Conferido em 1254 × 1254: dois braços índigo ligados às laterais da capa e duas mãos creme, uma com o X e outra com o check. Carta e volta por trás removidas.'
  : 'Conferido em 1254 × 1254: dois braços ligados às laterais da capa, duas mãos segurando o regador pela frente. Removida a volta do braço por trás do livro.';
 b.anatomyCheck={method:'conferência visual em resolução original',arms:2,hands:2,handsConnected:true,noSurplusArms:true,noBodyLoops:true,legs:0};
}
for(const b of badges)if(!targets.includes(b.id)&&JSON.stringify(b)!==JSON.stringify(oldManifest.badges.find(a=>a.id===b.id)))throw Error('Outro metadado alterado '+b.id);
manifest.date='2026-10-24';manifest.updatedAt=stamp;
await fs.writeFile(path.join(work,'manifesto.json'),JSON.stringify(manifest,null,2)+'\n');
const esc=s=>String(s).replaceAll('&','&amp;').replaceAll('<','&lt;').replaceAll('>','&gt;');
const embedded=new Map();
for(const b of badges)embedded.set(b.id,(await fs.readFile(path.join(targets.includes(b.id)?work:OUT,b.preview))).toString('base64'));
const comparisons=['plano-10','plano-50','plano-200','plano-500','plano-1000','seq-100','seq-365'].map(id=>previous.badges.find(b=>b.id===id));
for(const b of comparisons)embedded.set('old-'+b.id,(await fs.readFile(path.join(OLD,b.preview))).toString('base64'));
const image=(id,x,y,w)=>`<image x="${x}" y="${y}" width="${w}" height="${w}" href="data:image/png;base64,${embedded.get(id)}"/>`;
let svg='<svg xmlns="http://www.w3.org/2000/svg" width="3010" height="5760"><rect width="3010" height="5760" fill="#f1e9d9"/><text x="65" y="88" fill="#24244c" font-family="Georgia" font-size="48">Estudário · Coleção III · 65 conquistas</text><text x="65" y="140" fill="#55566e" font-family="Segoe UI" font-size="25">65 cenas conferidas · sete auréolas progressivas · referências da coleção anterior na última faixa</text>';
for(let i=0;i<badges.length;i++){
 const b=badges[i],x=25+(i%7)*430,y=200+Math.floor(i/7)*480;
 svg+=image(b.id,x,y,400);
 svg+=`<text x="${x+200}" y="${y+423}" text-anchor="middle" font-family="Segoe UI" font-size="21" fill="#24244c">${esc(b.name)}</text><text x="${x+200}" y="${y+453}" text-anchor="middle" font-family="Segoe UI" font-size="18" fill="#646177">${b.id} · ${b.tierLabel}${b.qaStatus==='corrigir'?' · EM REVISÃO':''}</text>`;
}
svg+='<rect x="0" y="5020" width="3010" height="740" fill="#deded4"/><text x="65" y="5080" font-family="Georgia" font-size="34" fill="#424454">Referências preservadas · Coleção II</text>';
for(let i=0;i<comparisons.length;i++){
 const b=comparisons[i],x=25+i*430,y=5130;
 svg+=image('old-'+b.id,x,y,400)+`<text x="${x+200}" y="${y+438}" text-anchor="middle" font-family="Segoe UI" font-size="21" fill="#424454">${b.id} · ${b.tierLabel}</text>`;
}
svg+='<text x="65" y="5720" font-family="Segoe UI" font-size="22" fill="#646177">Legendas externas às artes. Fitas vazias. Originais separados preservam transparência e resolução integral.</text></svg>';
await sharp(Buffer.from(svg)).png().toFile(path.join(work,'prancha-completa.png'));
// Pranchas menores permitem revisar os detalhes sem reduzir toda a coleção a miniaturas.
for(let group=0;group<7;group++){
 const subset=badges.slice(group*10,group*10+10);
 if(!subset.some(b=>targets.includes(b.id)))continue;
 let sheet=`<svg xmlns="http://www.w3.org/2000/svg" width="2650" height="1360"><rect width="2650" height="1360" fill="#151a31"/><text x="45" y="65" font-size="32" font-family="Georgia" fill="#eee6d4">Coleção III · inspeção ${group+1}</text>`;
 for(let i=0;i<subset.length;i++){
  const b=subset[i],x=15+(i%5)*530,y=100+Math.floor(i/5)*620;
  sheet+=image(b.id,x,y,500)+`<text x="${x+250}" y="${y+545}" text-anchor="middle" font-family="Segoe UI" font-size="22" fill="#eee6d4">${esc(b.id)}${b.qaStatus==='corrigir'?' · EM REVISÃO':''}</text><text x="${x+250}" y="${y+577}" text-anchor="middle" font-family="Segoe UI" font-size="21" fill="#c5c9d8">${esc(b.name)}</text>`;
 }
 await sharp(Buffer.from(sheet+'</svg>')).png().toFile(path.join(work,'revisao-'+(group+1)+'.png'));
}

console.log(JSON.stringify({originals:2,previews:2,protected:Object.keys(baseline.protected).length,sheets:badges.map((b,i)=>targets.includes(b.id)?Math.floor(i/10)+1:0).filter(Boolean),manifest:'manifesto.json',completeSheet:'prancha-completa.png'}));
