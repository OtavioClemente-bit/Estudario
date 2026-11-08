import fs from 'node:fs/promises';
import path from 'node:path';
import crypto from 'node:crypto';
import {createRequire} from 'node:module';
import {fileURLToPath} from 'node:url';
const require=createRequire(import.meta.url),sharp=require('C:/Users/otavi/.cache/codex-runtimes/codex-primary-runtime/dependencies/node/node_modules/sharp');
const work=path.dirname(fileURLToPath(import.meta.url)),hash=b=>crypto.createHash('sha256').update(b).digest('hex');
const manifest=JSON.parse(await fs.readFile(path.join(work,'manifesto.json'),'utf8')),proof=JSON.parse(await fs.readFile(path.join(work,'composicao-verificacao.json'),'utf8'));
const results=[];
for(const item of manifest.assets){
 const p=path.resolve(work,item.file);if(!p.startsWith(work+path.sep))throw Error('Fora da pasta de entrega');
 const bytes=await fs.readFile(p);if(hash(bytes)!==item.sha256||bytes.length!==item.bytes)throw Error('Hash ou tamanho '+item.file);
 if(item.file.endsWith('.png')){const m=await sharp(bytes).metadata();if(m.width!==item.width||m.height!==item.height)throw Error('Dimensões '+item.file);}
 results.push({file:item.file,sha256:item.sha256,verified:true});
}
const icon=await fs.readFile(path.join(work,'play-store/icone-512.png')),im=await sharp(icon).metadata();
if(im.width!==512||im.height!==512||im.channels!==4||icon.length>1024*1024)throw Error('Especificação do ícone da Play Store');
const iconRaw=await sharp(icon).ensureAlpha().raw().toBuffer();for(let p=3;p<iconRaw.length;p+=4)if(iconRaw[p]!==255)throw Error('Ícone da loja não é opaco');
const fm=await sharp(path.join(work,'play-store/capa-1024x500.png')).metadata();if(fm.width!==1024||fm.height!==500||fm.hasAlpha||fm.channels!==3)throw Error('Capa precisa ser 1024x500 RGB sem alfa');
const ss=manifest.assets.filter(b=>/^play-store\/0[1-6]-.*\.png$/.test(b.file));if(ss.length!==6)throw Error('Faltam telas');
for(const item of ss){const m=await sharp(path.join(work,item.file)).metadata();if(m.width!==1080||m.height!==1920||m.hasAlpha||m.channels!==3)throw Error('Captura fora do padrão '+item.file);}
for(const s of proof.screenshots){
 const bytes=await fs.readFile(path.join(work,'referencias/capturas-atuais',s.source+'.png'));if(hash(bytes)!==s.sourceSha256)throw Error('Captura original mudou '+s.id);
 const m=await sharp(bytes).metadata();if(s.crop.left<0||s.crop.top<0||s.crop.left+s.crop.width>m.width||s.crop.top+s.crop.height>m.height)throw Error('Recorte fora da captura '+s.id);
 const d=s.display;if(d.x<36||d.x+d.width>1044||d.y<495||d.y+d.height>1855)throw Error('Enquadramento corta interface '+s.id);
}
const foreground=await sharp(path.join(work,'android/res/drawable-nodpi/ic_launcher_folha_fg.png')).ensureAlpha().raw().toBuffer({resolveWithObject:true});
if(foreground.info.width!==432||foreground.info.height!==432)throw Error('Camada fora de 108dp @4x');
let maxRadius=0,visible=0;
for(let y=0;y<432;y++)for(let x=0;x<432;x++)if(foreground.data[(y*432+x)*4+3]>8){visible++;maxRadius=Math.max(maxRadius,Math.hypot(x-215.5,y-215.5));}
if(maxRadius>132)throw Error('Desenho fora do círculo seguro de 66dp: '+maxRadius);
const back=await sharp(path.join(work,'android/res/drawable-nodpi/ic_launcher_folha_bg.png')).ensureAlpha().raw().toBuffer();for(let p=3;p<back.length;p+=4)if(back[p]!==255)throw Error('Fundo adaptativo não é opaco');
for(const file of ['ic_launcher.xml','ic_launcher_round.xml']){
 const xml=await fs.readFile(path.join(work,'android/res/mipmap-anydpi-v26',file),'utf8');
 const refs=[...xml.matchAll(/@drawable\/([a-z0-9_]+)/g)].map(m=>m[1]);if(refs.length!==3)throw Error('Faltam camadas '+file);
 for(const ref of refs)await fs.access(path.join(work,'android/res/drawable-nodpi',ref+'.png'));
}
const visual=JSON.parse(await fs.readFile(path.join(work,'conferencia-visual.json'),'utf8'));if(!visual.approved)throw Error('Conferência visual pendente');
const report={date:new Date().toISOString(),approved:true,verifiedAssets:results.length,icon:{width:512,height:512,channels:4,opaque:true,bytes:icon.length,within1024KB:true},feature:{width:1024,height:500,channels:3,hasAlpha:false},screenshots:{count:6,width:1080,height:1920,hasAlpha:false,sourceVersion:'3.3.116',fullAppViewportContained:true},adaptive:{layers:3,width:432,height:432,safeRadius:132,actualMaxRadius:maxRadius,visibleForegroundPixels:visible,allResourcesResolved:true},visualReview:visual,assets:results};
await fs.writeFile(path.join(work,'verificacao-final.json'),JSON.stringify(report,null,2)+'\n');
console.log(JSON.stringify({approved:true,verifiedAssets:results.length,icon512RGBA:true,iconBytes:icon.length,feature1024x500RGB:true,screenshots1080x1920RGB:6,adaptiveLayers432:3,foregroundWithinSafeZone:true,actualMaxRadius:maxRadius,resourceReferencesResolved:true}));

