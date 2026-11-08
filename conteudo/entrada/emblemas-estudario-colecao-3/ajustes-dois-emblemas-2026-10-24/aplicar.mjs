import fs from 'node:fs/promises';
import path from 'node:path';
import crypto from 'node:crypto';
import {fileURLToPath} from 'node:url';
const work=path.dirname(fileURLToPath(import.meta.url)),out=path.resolve(work,'..'),backup="C:/Users/otavi/Documents/Codex/2026-09-15/vc-x20/conteudo/entrada/emblemas-estudario-colecao-3/substituidos/2026-10-24-0937";
const hash=b=>crypto.createHash('sha256').update(b).digest('hex');
const baseline=JSON.parse(await fs.readFile(path.join(work,'antes.json'),'utf8'));
const report=JSON.parse(await fs.readFile(path.join(work,'verificacao-dois.json'),'utf8'));
if(!report.approvedForInstallation)throw Error('Conferência não concluída');
for(const [file,sha] of Object.entries(baseline.protected))if(hash(await fs.readFile(path.join(out,file)))!==sha)throw Error('Arquivo protegido alterado '+file);
const mappings=[['final-png/erros-20.png','png/erros-20.png'],['final-png/revisao-100.png','png/revisao-100.png'],['app-512/erros-20.png','app-512/erros-20.png'],['app-512/revisao-100.png','app-512/revisao-100.png'],['manifesto.json','manifesto.json'],['LEIA-ME.md','LEIA-ME.md'],['prancha-completa.png','prancha-completa.png'],['revisao-4.png','revisao-4.png'],['revisao-6.png','revisao-6.png']];
const before={},after={};
for(const [source,target] of mappings){
 const oldBytes=await fs.readFile(path.join(out,target)),newBytes=await fs.readFile(path.join(work,source));
 if(baseline.targets[target]&&hash(oldBytes)!==baseline.targets[target]&&hash(oldBytes)!==hash(newBytes))throw Error('Destino mudou durante a revisão '+target);
 await fs.mkdir(path.dirname(path.join(backup,target)),{recursive:true});
 try{await fs.access(path.join(backup,target));}catch{await fs.writeFile(path.join(backup,target),oldBytes);}
 before[target]=hash(await fs.readFile(path.join(backup,target)));after[target]=hash(newBytes);
}
for(const [source,target] of mappings){
 const absolute=path.resolve(out,target);
 if(!absolute.startsWith(out+path.sep))throw Error('Destino fora da pasta');
 await fs.writeFile(absolute,await fs.readFile(path.join(work,source)));
}
for(const [file,sha] of Object.entries(after))if(hash(await fs.readFile(path.join(out,file)))!==sha)throw Error('Falha ao gravar '+file);
for(const [file,sha] of Object.entries(baseline.protected))if(hash(await fs.readFile(path.join(out,file)))!==sha)throw Error('Arquivo protegido alterado '+file);
report.installedAt=new Date().toISOString();
report.installedFiles=after;report.previousFiles=before;
report.installed=true;
await fs.writeFile(path.join(work,'verificacao-dois.json'),JSON.stringify(report,null,2)+'\n');
console.log(JSON.stringify({installed:true,replacedBadgeFiles:4,updatedManifest:true,updatedReadme:true,updatedSheets:3,otherBadgesUnchanged:63,framesUnchanged:7,backup:path.relative(out,backup)}));
