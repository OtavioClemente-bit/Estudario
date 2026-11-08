import fs from 'node:fs/promises';
import path from 'node:path';
import crypto from 'node:crypto';
import {fileURLToPath} from 'node:url';
import {createRequire} from 'node:module';
const require=createRequire(import.meta.url);
const sharp=require('C:/Users/otavi/.cache/codex-runtimes/codex-primary-runtime/dependencies/node/node_modules/sharp');
const out=path.dirname(fileURLToPath(import.meta.url));
const m=JSON.parse(await fs.readFile(path.join(out,'manifesto.json'),'utf8'));
const old=JSON.parse(await fs.readFile(path.resolve(out,'../emblemas-estudario-2026-10-23-v2/manifesto.json'),'utf8'));
const layers=JSON.parse(await fs.readFile(path.join(out,'producao-final/camadas-verificacao.json'),'utf8'));
const pending=JSON.parse(await fs.readFile(path.join(out,'pendencias.json'),'utf8'));
if(m.complete!==true||m.badges.length!==65||new Set(m.badges.map(b=>b.id)).size!==65)throw Error('Estado do manifesto');
if(m.badges.filter(b=>b.qaStatus!=='visualmente-conferido').length!==0||pending.badges.length!==0)throw Error('Contagem QA');
const hashes=JSON.parse((await fs.readFile(path.join(out,'colecao-2-hashes-antes.json'),'utf8')).replace(/^\uFEFF/,''));
for(const [file,hash] of Object.entries(hashes)){
 const bytes=await fs.readFile(path.resolve(out,'../emblemas-estudario-2026-10-23-v2',file));
 if(crypto.createHash('sha256').update(bytes).digest('hex')!==hash)throw Error('Coleção II alterada '+file);
}
const required=['id','category','tier','name','requirement','target','unit','categoryLabel','tierLabel','androidResource','story','png','preview','width','height','transparent'];
for(const b of m.badges){
 for(const k of required)if(b[k]===undefined)throw Error('Campo ausente '+b.id+':'+k);
 if(!b.qaNote||b.qaNote.includes('undefined'))throw Error('Nota de revisão inválida '+b.id);
 if(b.androidResource!=='emblema_'+b.id.replaceAll('-','_'))throw Error('Android '+b.id);
 const bytes=await fs.readFile(path.join(out,b.png));
 const layer=layers.badges.find(r=>r.id===b.id);
 if(crypto.createHash('sha256').update(bytes).digest('hex')!==layer.finalSha256)throw Error('Hash de montagem '+b.id);
 if(layer.opaqueFrameMismatches||layer.originalWindowPixelsWithOpacityLost)throw Error('Moldura/corte '+b.id);
 const {data,info}=await sharp(bytes).ensureAlpha().raw().toBuffer({resolveWithObject:true});
 if(info.width!==1254||info.height!==1254)throw Error('Tamanho nativo '+b.id);
 const corners=[0,info.width-1,(info.height-1)*info.width,info.width*info.height-1];
 if(corners.some(p=>data[p*4+3]!==0))throw Error('Canto não transparente '+b.id);
 const p=await sharp(path.join(out,b.preview)).metadata();
 if(p.width!==512||p.height!==512||!p.hasAlpha)throw Error('512 '+b.id);
}
for(const a of old.badges){
 const b=m.badges.find(b=>b.id===a.id);
 for(const k of ['id','category','tier','name','requirement','target','unit','categoryLabel','tierLabel'])if(b[k]!==a[k])throw Error('Metadado anterior alterado '+a.id+':'+k);
}
const expectedNew=[
 ['flash-50','flashcards',1,50,'cartões'],['flash-500','flashcards',2,500,'cartões'],['flash-2000','flashcards',3,2000,'cartões'],
 ['caderno-10','caderno',1,10,'grifos'],['caderno-100','caderno',2,100,'grifos'],['caderno-500','caderno',3,500,'grifos'],
 ['foco-10','foco',1,10,'sessões'],['foco-50','foco',2,50,'sessões'],['foco-200','foco',3,200,'sessões'],
 ['erros-20','erros',1,20,'questões'],['erros-100','erros',2,100,'questões'],['erros-300','erros',3,300,'questões'],
 ['fenix-1','especial',3,1,'resgates'],['madrugador-10','especial',2,10,'dias'],['coruja-10','especial',2,10,'dias']
];
for(const [id,category,tier,target,unit] of expectedNew){
 const b=m.badges.find(b=>b.id===id);
 if(!b||b.category!==category||b.tier!==tier||b.target!==target||b.unit!==unit)throw Error('Metadado novo incorreto '+id);
}
const gallery=JSON.parse(await fs.readFile(path.join(out,'galeria-verificacao.json'),'utf8'));
if(gallery.cards!==65||gallery.pendingLabels||gallery.brokenLoadedImages||gallery.consoleErrors||gallery.horizontalOverflow||gallery.flashcardsFilter!==3||gallery.silverFilter!==17)throw Error('Conferência da galeria');
const recipe=JSON.parse(await fs.readFile(path.join(out,'producao-final/acabamento-pendente/camadas.json'),'utf8'));
for(const j of recipe.jobs){
 const installed=await fs.readFile(path.join(out,'producao-final/png',j.id+'.png'));
 const reproducible=await fs.readFile(path.join(out,'producao-final/acabamento-pendente/png',j.id+'.png'));
 if(!installed.equals(reproducible))throw Error('Acabamento não instalado '+j.id);
}

const report={date:'2026-10-24',collection2FilesUnchanged:Object.keys(hashes).length,complete:true,generated:65,visuallyChecked:65,pendingReconstruction:0,originals1254:65,previews512:65,alphaCornersVerified:65,exactFrameReuseVerified:65,previousMetadataPreserved:old.badges.length,newMetadataVerified:expectedNew.length,layeredRepairsReproduced:recipe.jobs.length,galleryChecks:gallery,tierCounts:Object.fromEntries([1,2,3,4,5,6,7].map(t=>[t,m.badges.filter(b=>b.tier===t).length]))};
await fs.writeFile(path.join(out,'verificacao-final.json'),JSON.stringify(report,null,2)+'\n');
console.log(JSON.stringify(report));
