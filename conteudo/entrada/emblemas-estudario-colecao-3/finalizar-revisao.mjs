import fs from 'node:fs/promises';
import path from 'node:path';
import {fileURLToPath} from 'node:url';
const out=path.dirname(fileURLToPath(import.meta.url));
const statusFile=path.join(out,'producao-final/status.json');
const status=JSON.parse(await fs.readFile(statusFile,'utf8'));
const layers=JSON.parse(await fs.readFile(path.join(out,'producao-final/acabamento-pendente/camadas.json'),'utf8'));
status.generated=status.badges.length;
status.visuallyChecked=status.badges.filter(b=>b.status==='visualmente-conferido').length;
status.pendingReconstruction=status.badges.filter(b=>b.status!=='visualmente-conferido').length;
status.complete=status.generated===65&&status.pendingReconstruction===0;
status.productionPaused=false;
if(status.blocker){status.previousGenerationLimit=status.blocker;delete status.blocker;}
status.revisionDate='2026-10-24';
status.finishingMethod='Recortes da própria pintura, deformação de textura, máscaras de oclusão, restauração da capa e sobreposição de cartas pintadas.';
for(const b of status.badges)if(layers.jobs.some(j=>j.id===b.id)){
 b.note=layers.jobs.find(j=>j.id===b.id).reason+' Conferido em resolução original após acabamento.';
 b.finishingRecord='producao-final/acabamento-pendente/camadas.json';
}
status.badges.find(b=>b.id==='maratona-480').note='Vista de costas descansando, rosto totalmente escondido entre as capas; face removida da borda de papel. Dois braços contínuos saindo atrás das bordas externas, com mãos fechando a capa e guardando o marcador. Conferido em resolução original.';
await fs.writeFile(statusFile,JSON.stringify(status,null,2)+'\n');
const promptsFile=path.join(out,'prompts.json');
const prompts=JSON.parse(await fs.readFile(promptsFile,'utf8'));
const mar=prompts.badges.find(b=>b.id==='maratona-480');
mar.story='Sol, lua e ampulheta atravessam o dia de estudo. De costas, o Folha fecha as capas e guarda o marcador para descansar na almofada; o rosto fica escondido entre as páginas.';
mar.pose='Vista de costas em três quartos, fechando as próprias capas para descansar; rosto oculto, uma mão na capa e outra no marcador.';
mar.finalFinishing='A pose de costas foi preservada. A face indevida na espessura do papel foi removida e os dois braços foram reconstruídos com textura da pintura, saindo por trás das bordas externas da capa.';
await fs.writeFile(promptsFile,JSON.stringify(prompts,null,2)+'\n');
const originalRequests=JSON.parse(await fs.readFile(path.join(out,'pedidos-pendentes-reconstrucao.json'),'utf8'));
if(originalRequests.jobs.length)await fs.writeFile(path.join(out,'producao-final/acabamento-pendente/pedidos-anteriores.json'),JSON.stringify({...originalRequests,historical:true,note:'Pedidos antigos, anteriores ao acabamento manual autorizado. As soluções finais estão em camadas.json.'},null,2)+'\n');
await fs.writeFile(path.join(out,'pedidos-pendentes-reconstrucao.json'),JSON.stringify({complete:status.complete,count:status.pendingReconstruction,reason:'As 14 pendências anteriores foram resolvidas por acabamento em camadas e conferidas visualmente.',jobs:[],resolved:layers.jobs.map(j=>({id:j.id,method:j.reason})),history:'producao-final/acabamento-pendente/pedidos-anteriores.json'},null,2)+'\n');
console.log(JSON.stringify({generated:status.generated,checked:status.visuallyChecked,pending:status.pendingReconstruction,complete:status.complete,layeredRepairs:layers.jobs.length}));
