import fs from 'node:fs/promises';
import path from 'node:path';
import {fileURLToPath} from 'node:url';
const here=path.dirname(fileURLToPath(import.meta.url));
const file=path.join(here,'status.json');
const report=JSON.parse(await fs.readFile(file,'utf8'));
const [status,...ids]=process.argv.slice(2);
if(!status||!ids.length)throw Error('Informar status e IDs.');
for(const id of ids){
 const b=report.badges.find(b=>b.id===id);if(!b)throw Error('ID desconhecido: '+id);
 b.status=status;b.note=status==='visualmente-conferido'?'Conferido visualmente; geometria do livro e braços sem defeito aparente.':b.note;
}
report.generated=report.badges.length;
report.visuallyChecked=report.badges.filter(b=>b.status==='visualmente-conferido').length;
report.pendingReconstruction=report.generated-report.visuallyChecked;
report.complete=report.generated===65&&report.pendingReconstruction===0;
await fs.writeFile(file,JSON.stringify(report,null,2)+'\n');
console.log(JSON.stringify({status,atualizados:ids.length,conferidos:report.badges.filter(b=>b.status==='visualmente-conferido').length}));
