import fs from 'node:fs/promises';
import path from 'node:path';
import {fileURLToPath} from 'node:url';
import {createRequire} from 'node:module';
const require=createRequire(import.meta.url);
const sharp=require('C:/Users/otavi/.cache/codex-runtimes/codex-primary-runtime/dependencies/node/node_modules/sharp');
const out=path.dirname(fileURLToPath(import.meta.url));
const status=JSON.parse(await fs.readFile(path.join(out,'producao-final/status.json'),'utf8'));
const m=JSON.parse(await fs.readFile(path.join(out,'manifesto.json'),'utf8'));
for(const b of m.badges){
 const q=status.badges.find(q=>q.id===b.id);
 if(!q?.note||q.note.includes('undefined'))throw Error('Nota de revisão inválida '+b.id);
 b.qaStatus=q.status;b.qaNote=q.note;
}
await fs.writeFile(path.join(out,'manifesto.json'),JSON.stringify(m,null,2)+'\n');
await fs.writeFile(path.join(out,'dados.js'),'window.ESTUDARIO_BADGES='+JSON.stringify(m.badges)+';\n');
const gallery=JSON.parse(await fs.readFile(path.join(out,'galeria-verificacao.json'),'utf8'));
const raw=path.join(out,'galeria-pagina-completa.png');
const im=await sharp(raw).metadata();
await sharp(raw).extract({left:0,top:0,width:im.width,height:Math.min(im.height,gallery.capture.bottom)}).png().toFile(path.join(out,'galeria-final.png'));
await fs.copyFile(path.join(out,'galeria-final.png'),path.join(out,'galeria-previa.png'));
const check=JSON.parse(await fs.readFile(path.join(out,'verificacao-final.json'),'utf8'));
check.galleryChecks=gallery;
await fs.writeFile(path.join(out,'verificacao-final.json'),JSON.stringify(check,null,2)+'\n');
await fs.writeFile(path.join(out,'verificacao-final-previa.json'),JSON.stringify(check,null,2)+'\n');
console.log(JSON.stringify({notes:65,screenshot:[im.width,gallery.capture.bottom],gallery:gallery.cards}));
