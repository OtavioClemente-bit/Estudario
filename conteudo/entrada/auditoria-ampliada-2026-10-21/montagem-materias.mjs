// Exercita montagem real de todas as matérias sem publicar ou persistir questões.
import fs from 'node:fs';
import path from 'node:path';
import {fileURLToPath} from 'node:url';
import {assembleFromLibrary} from '../../../supabase/functions/_shared/library.ts';
const here=path.dirname(fileURLToPath(import.meta.url));
const root=path.resolve(here,'../../..');
const walk=dir=>fs.readdirSync(dir,{withFileTypes:true}).flatMap(e=>e.isDirectory()?walk(path.join(dir,e.name)):e.name.endsWith('.json')?[path.join(dir,e.name)]:[]);
const counts={};const results=[];
for(const file of walk(path.join(root,'conteudo/materias')).sort()){
  const material=JSON.parse(fs.readFileSync(file,'utf8'));
  for(const style of ['MIXED','FIVE_OPTIONS','FOUR_OPTIONS','TRUE_FALSE']){
    const item=counts[style]??={pedidos:0,montados:0,semCobertura:0,erros:0};item.pedidos++;
    try{
      const out=assembleFromLibrary(material,null,{board:null,avoidStatements:[],options:{blocks:['THEORY','SUMMARY','QUICK_REVIEW','TIPS_TRAPS','ACTIVE_RECALL','QUESTIONS','ERROR_CONCEPTS'],depth:'BOOK',questionCount:10,questionStyle:style,difficulty:'MIXED'}},'auditoria-montagem');
      if(!out){item.semCobertura++;results.push({arquivo:path.relative(root,file),style,status:'SEM_COBERTURA_NESTE_PEDIDO'});continue;}
      if(out.questions.length!==10)throw new Error('Quantidade entregue diferente do pedido');
      if(out.questions.some(q=>q.options.filter(o=>o.correct===true).length!==1))throw new Error('Gabarito estrutural invalido');
      if(new Set(out.questions.map(q=>q.statement)).size!==10)throw new Error('Enunciado repetido na montagem');
      item.montados++;
    }catch(e){item.erros++;results.push({arquivo:path.relative(root,file),style,status:'ERRO',erro:e.message});}
  }
}
const materialFiles=new Map(walk(path.join(root,'conteudo/materias')).map(file=>{const m=JSON.parse(fs.readFileSync(file,'utf8'));return [m.id,file];}));
counts.RECORTE_MIXED={pedidos:0,montados:0,semCobertura:0,erros:0};
for(const file of walk(path.join(root,'conteudo/recortes')).sort()){
  const note=JSON.parse(fs.readFileSync(file,'utf8'));const item=counts.RECORTE_MIXED;item.pedidos++;
  try{
    const material=JSON.parse(fs.readFileSync(materialFiles.get(note.topic),'utf8'));
    const out=assembleFromLibrary(material,note,{board:note.board,avoidStatements:[],options:{blocks:['THEORY','SUMMARY','QUICK_REVIEW','TIPS_TRAPS','ACTIVE_RECALL','QUESTIONS','ERROR_CONCEPTS'],depth:'BOOK',questionCount:10,questionStyle:'MIXED',difficulty:'MIXED'}},'auditoria-recorte');
    if(!out){item.semCobertura++;results.push({arquivo:path.relative(root,file),style:'RECORTE_MIXED',status:'SEM_COBERTURA_NESTE_PEDIDO'});continue;}
    if(out.questions.length!==10||out.questions.some(q=>q.options.filter(o=>o.correct===true).length!==1))throw new Error('Quantidade ou gabarito estrutural invalido');
    if(new Set(out.questions.map(q=>q.statement)).size!==10)throw new Error('Enunciado repetido na montagem');
    item.montados++;
  }catch(e){item.erros++;results.push({arquivo:path.relative(root,file),style:'RECORTE_MIXED',status:'ERRO',erro:e.message});}
}
const report={parametros:'10 questoes, nivel misto, todos os blocos, uma semente por cenario: cada materia nos quatro estilos, cada recorte em MIXED. Retorno nulo indica indisponibilidade nesse pedido; nao e automaticamente defeito.',totais:counts,ocorrencias:results};
fs.writeFileSync(path.join(here,'montagem-materias.json'),JSON.stringify(report,null,2)+'\n');
console.log(JSON.stringify(counts,null,2));console.log('Primeiras ocorrencias',JSON.stringify(results.slice(0,3)));
