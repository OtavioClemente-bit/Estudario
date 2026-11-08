// Exportação proporcional, metadados e pranchas das pinturas ImageGen com acabamento em camadas.
import fs from 'node:fs/promises';
import path from 'node:path';
import crypto from 'node:crypto';
import {fileURLToPath} from 'node:url';
import {createRequire} from 'node:module';
const require=createRequire(import.meta.url);
const sharp=require('C:/Users/otavi/.cache/codex-runtimes/codex-primary-runtime/dependencies/node/node_modules/sharp');
const OUT=path.dirname(fileURLToPath(import.meta.url));
const OLD=path.resolve(OUT,'../emblemas-estudario-2026-10-23-v2');
const source=JSON.parse(await fs.readFile(path.join(OUT,'prompts.json'),'utf8'));
const qa=JSON.parse(await fs.readFile(path.join(OUT,'producao-final/status.json'),'utf8'));
const qaById=new Map(qa.badges.map(b=>[b.id,b]));
const checked=qa.badges.filter(b=>b.status==='visualmente-conferido').length,pending=qa.badges.length-checked,complete=qa.badges.length===65&&pending===0;
const previous=JSON.parse(await fs.readFile(path.join(OLD,'manifesto.json'),'utf8'));
const before=JSON.parse((await fs.readFile(path.join(OUT,'colecao-2-hashes-antes.json'),'utf8')).replace(/^\uFEFF/,''));
const hash=b=>crypto.createHash('sha256').update(b).digest('hex');
for(const [file,sha] of Object.entries(before)){
 if(hash(await fs.readFile(path.join(OLD,file)))!==sha)throw Error('Coleção II alterada: '+file);
}
if(source.badges.length!==65||new Set(source.badges.map(b=>b.id)).size!==65)throw Error('IDs ou quantidade incorretos.');
const badges=[],checks=[];
await fs.mkdir(path.join(OUT,'app-512'),{recursive:true});
for(const b of source.badges){
 const file=path.join(OUT,'png',b.id+'.png');
 const bytes=await fs.readFile(file);
 const m=await sharp(bytes).metadata(),s=await sharp(bytes).stats();
 if(m.width!==m.height||!m.hasAlpha||s.isOpaque)throw Error('Original sem transparência ou não quadrado: '+b.id);
 const preview=path.join(OUT,'app-512',b.id+'.png');
 await sharp(bytes).resize(512,512,{fit:'contain',background:{r:0,g:0,b:0,alpha:0}}).png().toFile(preview);
 const p=await sharp(preview).metadata(),ps=await sharp(preview).stats();
 if(p.width!==512||p.height!==512||!p.hasAlpha||ps.isOpaque)throw Error('Exportação incorreta: '+b.id);
 badges.push({id:b.id,category:b.category,tier:b.tier,name:b.name,requirement:b.requirement,target:b.target,unit:b.unit,categoryLabel:b.categoryLabel,tierLabel:b.tierLabel??{1:'Bronze',2:'Prata',3:'Ouro',4:'Safira',5:'Rubi',6:'Esmeralda',7:'Esmeralda'}[b.tier],androidResource:'emblema_'+b.id.replaceAll('-','_'),story:b.story,png:'png/'+b.id+'.png',preview:'app-512/'+b.id+'.png',width:m.width,height:m.height,transparent:true});
 checks.push({id:b.id,original:[m.width,m.height],app:[p.width,p.height],transparent:true,sha256:hash(bytes),pose:b.pose});
}
for(const b of badges){b.qaStatus=qaById.get(b.id).status;b.qaNote=qaById.get(b.id).note;}
const manifest={collection:'Atlas de conquistas — Coleção III',date:'2026-10-24',count:65,complete,visuallyChecked:checked,pendingReconstruction:pending,method:'ImageGen individual com acabamento em camadas autorizado: recortes, máscaras, deformação de textura e sobreposições pintadas. Sete PNGs de moldura reutilizados literalmente por faixa.',reference:'../emblemas-estudario-2026-10-23-v2/png/plano-200.png',badges};
await fs.writeFile(path.join(OUT,'manifesto.json'),JSON.stringify(manifest,null,2)+'\n');
await fs.writeFile(path.join(OUT,'dados.js'),'window.ESTUDARIO_BADGES='+JSON.stringify(badges)+';\n');
const esc=s=>String(s).replaceAll('&','&amp;').replaceAll('<','&lt;').replaceAll('>','&gt;');
const embedded=new Map();
for(const b of badges)embedded.set(b.id,(await fs.readFile(path.join(OUT,b.preview))).toString('base64'));
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
await sharp(Buffer.from(svg)).png().toFile(path.join(OUT,'prancha-completa.png'));
// Pranchas menores permitem revisar os detalhes sem reduzir toda a coleção a miniaturas.
for(let group=0;group<7;group++){
 const subset=badges.slice(group*10,group*10+10);
 let sheet=`<svg xmlns="http://www.w3.org/2000/svg" width="2650" height="1360"><rect width="2650" height="1360" fill="#151a31"/><text x="45" y="65" font-size="32" font-family="Georgia" fill="#eee6d4">Coleção III · inspeção ${group+1}</text>`;
 for(let i=0;i<subset.length;i++){
  const b=subset[i],x=15+(i%5)*530,y=100+Math.floor(i/5)*620;
  sheet+=image(b.id,x,y,500)+`<text x="${x+250}" y="${y+545}" text-anchor="middle" font-family="Segoe UI" font-size="22" fill="#eee6d4">${esc(b.id)}${b.qaStatus==='corrigir'?' · EM REVISÃO':''}</text><text x="${x+250}" y="${y+577}" text-anchor="middle" font-family="Segoe UI" font-size="21" fill="#c5c9d8">${esc(b.name)}</text>`;
 }
 await sharp(Buffer.from(sheet+'</svg>')).png().toFile(path.join(OUT,'revisao-'+(group+1)+'.png'));
}
await fs.writeFile(path.join(OUT,'verificacao.json'),JSON.stringify({date:'2026-10-24',count:65,complete,visuallyChecked:checked,pendingReconstruction:pending,originalPngCount:(await fs.readdir(path.join(OUT,'png'))).filter(n=>n.endsWith('.png')).length,app512Count:(await fs.readdir(path.join(OUT,'app-512'))).filter(n=>n.endsWith('.png')).length,tierCounts:Object.fromEntries([1,2,3,4,5,6,7].map(t=>[t,badges.filter(b=>b.tier===t).length])),collection2FilesUnchanged:Object.keys(before).length,files:checks},null,2)+'\n');
console.log(JSON.stringify({originals:badges.length,app512:badges.length,collection2FilesUnchanged:Object.keys(before).length,contactSheet:'prancha-completa.png'}));
