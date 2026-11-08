import fs from 'node:fs/promises';
import path from 'node:path';
import crypto from 'node:crypto';
import {fileURLToPath} from 'node:url';
import {createRequire} from 'node:module';
const require=createRequire(import.meta.url),sharp=require('C:/Users/otavi/.cache/codex-runtimes/codex-primary-runtime/dependencies/node/node_modules/sharp');
const work=path.dirname(fileURLToPath(import.meta.url)),OUT=path.resolve(work,'..'),OLD=path.resolve(OUT,'../emblemas-estudario-2026-10-23-v2');
const baseline=JSON.parse(await fs.readFile(path.join(work,'antes.json'),'utf8'));
const plan=JSON.parse(await fs.readFile(path.join(work,'prompts.json'),'utf8'));
const manifest=JSON.parse(await fs.readFile(path.join(OUT,baseline.backup,'manifesto.json'),'utf8'));
const qa=JSON.parse(await fs.readFile(path.join(work,'conferencia-visual.json'),'utf8'));
const targets=baseline.ids,badges=manifest.badges,stamp=new Date().toISOString();
const hash=b=>crypto.createHash('sha256').update(b).digest('hex');
for(const [file,sha] of Object.entries(baseline.protected))if(hash(await fs.readFile(path.join(OUT,file)))!==sha)throw Error('Protegido alterado '+file);
await fs.mkdir(path.join(work,'app-512'),{recursive:true});
for(const id of targets){
 const item=qa.badges.find(b=>b.id===id);
 if(!item?.approved||item.arms!==2||item.hands!==2||item.legs!==0||!item.rootsHiddenByLateralCover)throw Error('Conferência visual pendente '+id);
 const bytes=await fs.readFile(path.join(work,'final-png',id+'.png')),m=await sharp(bytes).metadata();
 if(m.width!==1254||m.height!==1254||!m.hasAlpha)throw Error('Original inválido '+id);
 const preview=await sharp(bytes).resize(512,512,{fit:'contain',background:{r:0,g:0,b:0,alpha:0}}).png().toBuffer();
 await fs.writeFile(path.join(work,'app-512',id+'.png'),preview);
 const b=badges.find(b=>b.id===id),job=plan.jobs.find(j=>j.id===id);
 b.story=item.story??job.story;b.sha256=hash(bytes);b.previewSha256=hash(preview);b.updatedAt=stamp;
 b.generationProvider='ImageGen integrado';b.qaStatus='conferido';b.qaNote=item.note;
 b.anatomyCheck={method:'conferência visual da nova pintura em resolução original e prévia 512',arms:2,hands:2,handsConnected:true,noSurplusArms:true,noBodyLoops:true,rootsHiddenByLateralCover:true,legs:0,...(id==='revisao-1000'?{elephantTrunks:1}:{})};
}
manifest.date='2026-10-24';manifest.updatedAt=stamp;
await fs.writeFile(path.join(work,'manifesto.json'),JSON.stringify(manifest,null,2)+'\n');
await fs.writeFile(path.join(work,'dados.js'),'window.ESTUDARIO_BADGES='+JSON.stringify(badges)+';\n');
const prompts=JSON.parse(await fs.readFile(path.join(OUT,baseline.backup,'prompts.json'),'utf8'));
const executions=JSON.parse(await fs.readFile(path.join(work,'prompts-executados.json'),'utf8'));
for(const id of targets){
 const b=prompts.badges.find(b=>b.id===id),j=plan.jobs.find(j=>j.id===id),v=qa.badges.find(b=>b.id===id);
 b.story=v.story??j.story;b.pose=v.pose??j.story;b.direction=j.scene;
 b.referencePaths=j.references;b.prompt=j.prompt;
 b.currentGeneration={provider:'ImageGen integrado',date:stamp,promptHistory:'refacao-chatgpt-seis-2026-10-24/prompts-executados.json',selectedSource:v.source,edits:executions.calls.filter(c=>c.id===id&&c.variant!=='nova').map(c=>({variant:c.variant,prompt:c.prompt,refs:c.refs}))};
}
await fs.writeFile(path.join(work,'prompts-colecao.json'),JSON.stringify(prompts,null,2)+'\n');
const esc=s=>String(s).replaceAll('&','&amp;').replaceAll('<','&lt;').replaceAll('>','&gt;');
const embedded=new Map();
for(const b of badges)embedded.set(b.id,(await fs.readFile(path.join(targets.includes(b.id)?work:OUT,b.preview))).toString('base64'));
const previous=JSON.parse(await fs.readFile(path.join(OLD,'manifesto.json'),'utf8'));
const comparisons=['plano-10','plano-50','plano-200','plano-500','plano-1000','seq-100','seq-365'].map(id=>previous.badges.find(b=>b.id===id));
for(const b of comparisons)embedded.set('old-'+b.id,(await fs.readFile(path.join(OLD,b.preview))).toString('base64'));
const image=(id,x,y,w)=>`<image x="${x}" y="${y}" width="${w}" height="${w}" href="data:image/png;base64,${embedded.get(id)}"/>`;
let svg='<svg xmlns="http://www.w3.org/2000/svg" width="3010" height="5760"><rect width="3010" height="5760" fill="#f1e9d9"/><text x="65" y="88" fill="#24244c" font-family="Georgia" font-size="48">Estudário · Coleção III · 65 conquistas</text><text x="65" y="140" fill="#55566e" font-family="Segoe UI" font-size="25">65 cenas conferidas · sete auréolas progressivas · referências da coleção anterior na última faixa</text>';
for(let i=0;i<badges.length;i++){
 const b=badges[i],x=25+(i%7)*430,y=200+Math.floor(i/7)*480;
 svg+=image(b.id,x,y,400)+`<text x="${x+200}" y="${y+423}" text-anchor="middle" font-family="Segoe UI" font-size="21" fill="#24244c">${esc(b.name)}</text><text x="${x+200}" y="${y+453}" text-anchor="middle" font-family="Segoe UI" font-size="18" fill="#646177">${b.id} · ${b.tierLabel}${b.qaStatus==='corrigir'?' · EM REVISÃO':''}</text>`;
}
svg+='<rect x="0" y="5020" width="3010" height="740" fill="#deded4"/><text x="65" y="5080" font-family="Georgia" font-size="34" fill="#424454">Referências preservadas · Coleção II</text>';
for(let i=0;i<comparisons.length;i++){const b=comparisons[i],x=25+i*430,y=5130;svg+=image('old-'+b.id,x,y,400)+`<text x="${x+200}" y="${y+438}" text-anchor="middle" font-family="Segoe UI" font-size="21" fill="#424454">${b.id} · ${b.tierLabel}</text>`;}
svg+='<text x="65" y="5720" font-family="Segoe UI" font-size="22" fill="#646177">Legendas externas às artes. Fitas vazias. Originais separados preservam transparência e resolução integral.</text></svg>';
await sharp(Buffer.from(svg)).png().toFile(path.join(work,'prancha-completa.png'));
const groups=[];
for(let group=0;group<7;group++){
 const subset=badges.slice(group*10,group*10+10);if(!subset.some(b=>targets.includes(b.id)))continue;groups.push(group+1);
 let sheet=`<svg xmlns="http://www.w3.org/2000/svg" width="2650" height="1360"><rect width="2650" height="1360" fill="#151a31"/><text x="45" y="65" font-size="32" font-family="Georgia" fill="#eee6d4">Coleção III · inspeção ${group+1}</text>`;
 for(let i=0;i<subset.length;i++){const b=subset[i],x=15+(i%5)*530,y=100+Math.floor(i/5)*620;
 sheet+=image(b.id,x,y,500)+`<text x="${x+250}" y="${y+545}" text-anchor="middle" font-family="Segoe UI" font-size="22" fill="#eee6d4">${esc(b.id)}${b.qaStatus==='corrigir'?' · EM REVISÃO':''}</text><text x="${x+250}" y="${y+577}" text-anchor="middle" font-family="Segoe UI" font-size="21" fill="#c5c9d8">${esc(b.name)}</text>`;}
 await sharp(Buffer.from(sheet+'</svg>')).png().toFile(path.join(work,'revisao-'+(group+1)+'.png'));
}
const rankIds=['seq-3','seq-7','seq-14','seq-30','seq-60','seq-100','seq-365'];
const labels=['Bronze · aro simples','Prata · gravuras e ramos','Ouro · louros completos','Safira · camada lapidada','Rubi · ramificações e platina','Esmeralda · filigrana e ouro','Anual · halo e quatro estações'];
let ranks='<svg xmlns="http://www.w3.org/2000/svg" width="2000" height="1400"><rect width="2000" height="1400" fill="#13182e"/><text x="65" y="80" font-size="46" font-family="Georgia" fill="#f7ecd8">Estudário · a conquista evolui por fora também</text><text x="65" y="130" font-size="24" font-family="Segoe UI" fill="#b8c4ce">Coleção III · sete níveis de auréola, do cobre simples ao anual radiante</text>';
for(let i=0;i<7;i++){const id=rankIds[i],x=i<4?25+i*495:270+(i-4)*495,y=i<4?190:790;
 const b=await sharp(path.join(targets.includes(id)?work:OUT,targets.includes(id)?'final-png':'png',id+'.png')).resize(470,470,{fit:'contain'}).png().toBuffer();
 ranks+=`<image x="${x}" y="${y}" width="470" height="470" href="data:image/png;base64,${b.toString('base64')}"/><text x="${x+235}" y="${y+515}" text-anchor="middle" font-size="23" font-family="Segoe UI" fill="#efe8d8">${labels[i]}</text>`;}
await sharp(Buffer.from(ranks+'</svg>')).png().toFile(path.join(work,'evolucao-aureolas.png'));
let contact='<svg xmlns="http://www.w3.org/2000/svg" width="1620" height="1300"><rect width="1620" height="1300" fill="#151a31"/><text x="40" y="55" font-size="33" font-family="Georgia" fill="#f4ead9">Estudário · seis novas pinturas · 24/10/2026</text>';
for(let i=0;i<targets.length;i++){const id=targets[i],b=badges.find(b=>b.id===id),x=14+i%3*540,y=85+Math.floor(i/3)*605;
 contact+=image(id,x,y,512)+`<text x="${x+256}" y="${y+545}" text-anchor="middle" font-size="23" font-family="Segoe UI" fill="#f4ead9">${id} · ${b.tierLabel}</text><text x="${x+256}" y="${y+575}" text-anchor="middle" font-size="21" font-family="Segoe UI" fill="#bcc5d7">${esc(b.name)}</text>`;}
await sharp(Buffer.from(contact+'</svg>')).png().toFile(path.join(work,'prancha-seis.png'));
let readme=await fs.readFile(path.join(OUT,baseline.backup,'LEIA-ME.md'),'utf8');
readme=readme.replace(/\*\*65 emblemas na coleção[^\n]*/, '**65 emblemas na coleção. A atualização mais recente (24/10/2026) refaz seis pinturas com o ImageGen integrado.** São 50 conquistas redesenhadas da Coleção II e 15 novas. Nesta rodada, os outros 59 emblemas e todas as sete molduras permanecem iguais.');
readme=readme.replace('Em maratona-480, Folha descansa de costas, fechando as capas e guardando o marcador; a face indevida na espessura de papel foi removida. Essa solução preserva a pose traseira e evita repetir um retrato frontal.','Na nova pintura de maratona-480, Folha descansa aberto sobre uma almofada: o rosto ocupa as páginas internas, os braços ficam nas laterais e não há pernas.');
const section=[
 '## Seis pinturas refeitas com ImageGen — 24/10/2026','',
 'Escopo desta rodada: **maratona-480, meta-1, quest-5000, revisao-1000, seq-365 e topico-50**. Cada cena foi pintada novamente por uma geração individual; ajustes posteriores pelo próprio ImageGen corrigiram encaixes pontuais. As molduras fixas foram reaplicadas literalmente, sem redesenho. As outras 59 artes e suas prévias não foram alteradas.','',
 ...targets.map(id=>'- **'+id+'**: '+qa.badges.find(b=>b.id===id).note),'',
 'Originais em png/ (1254 × 1254, transparência externa) e versões em app-512/ (512 × 512, sem recorte). As versões anteriores estão em substituidos/2026-10-24-chatgpt-seis/. O manifesto registra hash do original, hash da prévia, data e provedor nos seis IDs. Os metadados das outras 59 entradas foram preservados.','',
 'Pranchas atualizadas: prancha-completa.png, revisao-2.png a revisao-5.png e evolucao-aureolas.png. A prancha específica está em refacao-chatgpt-seis-2026-10-24/prancha-seis.png.','',
 'Registros vigentes destes seis IDs: refacao-chatgpt-seis-2026-10-24/conferencia-visual.json, montagem-verificacao.json e verificacao-entrega.json. Pedidos completos e edições: prompts-executados.json nessa pasta. Os relatórios globais anteriores são históricos para estes seis IDs; não constituem a verificação desta nova pintura. As seções antigas deste LEIA-ME registram rodadas anteriores.','',
 'A conferência visual abrangeu dois braços, duas mãos, seus encaixes laterais, o rosto e a geometria do livro, em resolução original; no elefante, uma única tromba. Conferidas também as prévias de 512. A checagem técnica compara dimensões, canal alfa, pixels opacos da moldura, os 125 recursos protegidos e os demais metadados.',''
].join('\n');
readme=readme.replace('## Correção localizada — 24/10/2026',section+'\n## Correção localizada anterior — 24/10/2026');
await fs.writeFile(path.join(work,'LEIA-ME.md'),readme);
await fs.writeFile(path.join(work,'preparacao.json'),JSON.stringify({date:stamp,ids:targets,groups,protected:Object.keys(baseline.protected).length},null,2)+'\n');
console.log(JSON.stringify({originals:6,previews:6,protected:125,groups,sheets:7}));

