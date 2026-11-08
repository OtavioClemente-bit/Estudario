import fs from 'node:fs/promises';
import path from 'node:path';
import {fileURLToPath} from 'node:url';
const here=path.dirname(fileURLToPath(import.meta.url));
const current=JSON.parse(await fs.readFile(path.join(here,'manifesto.json'),'utf8'));
if(current.complete)throw Error('Script histórico da prévia: a coleção já foi concluída. Use montar-camadas.mjs e montar-pacote.mjs.');
const file=path.join(here,'montar-pacote.mjs');
let s=await fs.readFile(file,'utf8');
s=s.replace('Estudário · Coleção III · 65 conquistas','Estudário · Coleção III · PRÉVIA EM REVISÃO');
s=s.replace('Sete auréolas progressivas · 65 direções de pose · comparação com a coleção anterior na última faixa','65 cenas geradas · 51 conferidas · 14 reconstruções pendentes · referências da coleção anterior na última faixa');
s=s.replace('${b.id} · ${b.tierLabel}</text>','${b.id} · ${b.tierLabel}${b.qaStatus===\'corrigir\'?\' · EM REVISÃO\':\'\'}</text>');
s=s.replace('${esc(b.id)}</text>','${esc(b.id)}${b.qaStatus===\'corrigir\'?\' · EM REVISÃO\':\'\'}</text>');
s=s.replace("date:'2026-10-23',count:65,originalPngCount","date:'2026-10-23',count:65,complete:false,visuallyChecked:51,pendingReconstruction:14,originalPngCount");
await fs.writeFile(file,s);

