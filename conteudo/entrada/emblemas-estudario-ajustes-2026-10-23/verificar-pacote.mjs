// Valida os arquivos e monta uma comparação; não pinta nem modifica as ilustrações.
import fs from 'node:fs/promises';
import path from 'node:path';
import crypto from 'node:crypto';
import {fileURLToPath} from 'node:url';
import {createRequire} from 'node:module';
const require=createRequire(import.meta.url);
const sharp=require('C:/Users/otavi/.cache/codex-runtimes/codex-primary-runtime/dependencies/node/node_modules/sharp');
const here=path.dirname(fileURLToPath(import.meta.url));
const base=path.resolve(here,'../emblemas-estudario-2026-10-23-v2');
const anatomy=['maratona-360','meta-1','meta-10','meta-50','meta-100','seq-60','edital-50','edital-75'];
const ambiguous=['edital-25','revisao-25','acerto-80'];
const rubies=['plano-1000','seq-60','meta-250','quest-10000','revisao-400'];
const expected=[...new Set([...anatomy,...ambiguous,...rubies])].sort();
const before=JSON.parse(await fs.readFile(path.join(base,'correcoes-sem-pernas/hashes-antes.json'),'utf8'));
const manifest=JSON.parse(await fs.readFile(path.join(base,'manifesto.json'),'utf8'));
const hash=b=>crypto.createHash('sha256').update(b).digest('hex');
const rows=[];
for(const b of manifest.badges){
 const original=await fs.readFile(path.join(base,b.png));
 const preview=await fs.readFile(path.join(base,b.preview));
 const m=await sharp(original).metadata(),p=await sharp(preview).metadata();
 const s=await sharp(original).stats(),t=await sharp(preview).stats();
 if(m.width!==1254||m.height!==1254||p.width!==512||p.height!==512)throw Error('Dimensões: '+b.id);
 if(!m.hasAlpha||s.isOpaque||!p.hasAlpha||t.isOpaque)throw Error('Transparência: '+b.id);
 rows.push({id:b.id,sha256:hash(original),alterado:hash(original)!==before[b.id],original:[m.width,m.height],app:[p.width,p.height],transparente:true});
}
const changed=rows.filter(r=>r.alterado).map(r=>r.id).sort();
if(JSON.stringify(changed)!==JSON.stringify(expected))throw Error('Conjunto de alterações inesperado: '+changed);
for(const dir of ['png','app-512']){
 if((await fs.readdir(path.join(base,dir))).filter(f=>f.endsWith('.png')).length!==50)throw Error('Contagem: '+dir);
}
const report={data:'2026-10-23',total:rows.length,anatomiaCorrigida:anatomy,formasAmbiguasRemovidas:ambiguous,rubisDiferenciados:rubies,artesAlteradas:changed,artesOriginaisPreservadas:rows.filter(r=>!r.alterado).length,revisaoVisual:'50 cenas inspecionadas; 15 imagens editadas conferidas individualmente.',arquivos:rows};
await fs.writeFile(path.join(base,'correcoes-sem-pernas/resultado.json'),JSON.stringify(report,null,2)+'\n');
await fs.writeFile(path.join(base,'verificacao.json'),JSON.stringify({data:'2026-10-23',originais:50,app512:50,transparencia:true,dimensoesOriginais:[1254,1254],dimensoesApp:[512,512],alterados:15,preservados:35,detalhes:'correcoes-sem-pernas/resultado.json'},null,2)+'\n');
const images=await Promise.all(['plano-10','plano-1000'].map(async id=>(await fs.readFile(path.join(base,'png',id+'.png'))).toString('base64')));
const svg=`<svg xmlns="http://www.w3.org/2000/svg" width="1500" height="980"><rect width="1500" height="980" fill="#11162b"/><text x="80" y="90" fill="#f7edda" font-size="42" font-family="Georgia">Bronze e Rubi · cores próprias</text><text x="80" y="138" fill="#adb6cc" font-size="22" font-family="Segoe UI">Cobre quente no Bronze. Vermelho profundo e platina no Rubi.</text>${images.map((v,i)=>`<image x="${70+i*730}" y="190" width="630" height="630" href="data:image/png;base64,${v}"/>`).join('')}<text x="385" y="895" text-anchor="middle" fill="#dca36c" font-size="34" font-family="Georgia">Bronze</text><text x="1115" y="895" text-anchor="middle" fill="#ef91a4" font-size="34" font-family="Georgia">Rubi</text></svg>`;
await sharp(Buffer.from(svg)).png().toFile(path.join(base,'bronze-rubi.png'));
console.log(JSON.stringify({total:rows.length,alterados:changed.length,preservados:50-changed.length,comparacao:'bronze-rubi.png'}));
