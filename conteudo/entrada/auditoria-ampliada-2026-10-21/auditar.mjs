// Auditoria local de leitura. Não publica nem grava matérias, recortes ou editais.
import fs from 'node:fs';
import path from 'node:path';
import {fileURLToPath} from 'node:url';
import {validateLibraryMaterial, validateBoardNote, normalizeAlias, normalizeBoard, deliverQuestion} from '../../../supabase/functions/_shared/library.ts';
const output=path.dirname(fileURLToPath(import.meta.url));
const root=path.resolve(output,'../../..');
function files(folder) {
  const base=path.join(root,'conteudo',folder);
  if(!fs.existsSync(base)) return [];
  const walk=dir=>fs.readdirSync(dir,{withFileTypes:true}).flatMap(e=>e.isDirectory()?walk(path.join(dir,e.name)):e.name.endsWith('.json')&&!e.name.startsWith('_')?[path.join(dir,e.name)]:[]);
  return walk(base).sort();
}
const findings={}; const counts={};
const add=(kind,file,detail)=>{(findings[kind]??=[]).push({arquivo:path.relative(root,file),detalhe:detail});};
const load=file=>{try{return JSON.parse(fs.readFileSync(file,'utf8'));}catch(e){add('json_invalido',file,e.message);return null;}};
const materials=new Map(); const aliases=new Map(); const sources=new Map();
let questions=0,short=0,shortValid=0,noConcept=0,delivered=0;
for(const file of files('materias')) {
  const m=load(file);if(!m)continue;
  if(materials.has(m.id))add('id_materia_duplicado',file,m.id);
  materials.set(m.id,{m,file});
  try{validateLibraryMaterial(m);}catch(e){add('validador_materia',file,e.problems??[e.message]);}
  for(const a of new Set([m.title,...(m.aliases??[])].map(normalizeAlias))){
    if(aliases.has(a)&&aliases.get(a)!==m.id)add('apelido_duplicado',file,{apelido:a,anterior:aliases.get(a),atual:m.id});
    aliases.set(a,m.id);
  }
  for(const source of m.sources??[]){const url=source.url??source.sourceUrl;if(url){const item=sources.get(url)??{url,arquivos:[]};item.arquivos.push(path.relative(root,file));sources.set(url,item);}}
  for(const [i,q] of (m.questions??[]).entries()) {
    questions++;
    if(q.errorConceptKey==null)noConcept++;
    if(q.explanation.trim().length<120){
      short++;
      const comments=q.format==='MULTIPLE_CHOICE'&&q.options.every(o=>typeof o.comentario==='string'&&o.comentario.trim());
      const size=q.explanation.length+q.options.reduce((s,o)=>s+(o.comentario??'').trim().length,0);
      if(comments&&q.explanation.trim().length>=40&&size>=180)shortValid++;
      else add('explicacao_curta_regra_real',file,{questao:i+1,tamanho:q.explanation.length,comComentarios:comments,totalComComentarios:size});
    }
    // Exercita o conversor usado pelo produto; não grava nem altera os itens de origem.
    const styles=q.format==='MULTIPLE_CHOICE'&&q.options.every(o=>typeof o.comentario==='string'&&o.comentario.trim())?['FIVE_OPTIONS','FOUR_OPTIONS']:['MIXED'];
    for(const style of styles){
      try{
        const out=deliverQuestion(q,style,'auditoria-2026-10-21');delivered++;
        const right=q.options.find(o=>o.correct===true),newRight=out.options.filter(o=>o.correct===true);
        if(newRight.length!==1||newRight[0]?.text!==right?.text)add('conversor_altera_resposta',file,{questao:i+1,style});
        if(out.options.length!==(style==='FOUR_OPTIONS'?4:q.options.length))add('conversor_quantidade_opcoes',file,{questao:i+1,style});
        if(out.explanation.length<q.explanation.length)add('conversor_perde_explicacao',file,{questao:i+1,style});
        if(out.options.some(o=>'comentario' in o))add('conversor_deixa_comentario',file,{questao:i+1,style});
      }catch(e){add('conversor_erro',file,{questao:i+1,style,erro:e.message});}
    }
  }
}
const notes=new Map();let noteQuestions=0;
for(const file of files('recortes')){
  const n=load(file);if(!n)continue;
  const material=materials.get(n.topic);
  if(!material){add('recorte_materia_inexistente',file,n.topic);continue;}
  try{validateBoardNote(n,material.m);}catch(e){add('validador_recorte',file,e.problems??[e.message]);}
  const k=[n.topic,normalizeBoard(n.board),normalizeAlias(n.role)].join('|');
  if(notes.has(k))add('recorte_duplicado',file,{chave:k,anterior:notes.get(k)});
  notes.set(k,path.relative(root,file));noteQuestions+=(n.questions??[]).length;
}
let topicCount=0;const qualified=new Map(),plain=new Map();
for(const file of files('editais')){
  const e=load(file);if(!e)continue;
  if(!e.disciplinas?.length)add('edital_sem_disciplinas',file,e.concurso);
  if(e.catalogo!==false&&(!e.concurso||!(e.cargoExibicao??e.cargo)))add('edital_catalogo_campos',file,e.concurso);
  for(const s of e.disciplinas??[])for(const [i,t] of (s.topicos??[]).entries()){
    topicCount++;const detail={disciplina:s.nome,numero:i+1,texto:t.texto,topico:t.topico};
    if(!t.topico)add('edital_topico_sem_materia',file,detail);
    else if(!materials.has(t.topico))add('edital_materia_inexistente',file,detail);
    if(normalizeAlias(t.texto).length<2)add('edital_topico_sem_texto',file,detail);
    if(t.topico){
      for(const [map,key] of [[qualified,`${normalizeAlias(s.nome)} ${normalizeAlias(t.texto)}`],[plain,normalizeAlias(t.texto)]]){
        const ids=map.get(key)??new Set();ids.add(t.topico);map.set(key,ids);
      }
    }
  }
}
let focusTopics=0;
for(const file of files('foco')){
  const f=load(file);if(!f)continue;const m=materials.get(f.materia)?.m;
  if(!m){add('foco_materia_inexistente',file,f.materia);continue;}
  for(const [i,t] of (f.topicos??[]).entries()){
    focusTopics++;if(t.foco==='inteira')continue;
    const badCh=(t.capitulos??[]).filter(x=>!Number.isInteger(Number(x))||Number(x)<0||Number(x)>=m.chapters.length);
    const badQ=(t.questoes??[]).filter(x=>!/^q\d+$/.test(String(x))||Number(String(x).slice(1))<1||Number(String(x).slice(1))>m.questions.length);
    if(badCh.length||badQ.length)add('foco_referencia_invalida',file,{numero:i+1,texto:t.texto,capitulos:badCh,questoes:badQ});
  }
}
for(const key of ['materias','recortes','editais','foco'])counts[key]=files(key).length;
Object.assign(counts,{questoes:questions,questoesRecortes:noteQuestions,topicosEditais:topicCount,topicosFoco:focusTopics,explicacoesCampoCurto:short,explicacoesCurtasAceitasComComentarios:shortValid,semConceitoErro:noConcept,conversoesExercitadas:delivered,fontesUrlsDistintas:sources.size,apelidosEditalMultiplasMaterias:[...plain.values()].filter(x=>x.size>1).length,apelidosQualificadosMultiplasMaterias:[...qualified.values()].filter(x=>x.size>1).length});
const report={data:'2026-10-21',commit:'b13dc28b5',metodo:'Validador oficial importado com Node --experimental-transform-types; sem publicar. Conversor em memoria com uma semente fixa. Sem certificacao semantica global.',totais:counts,contagens:Object.fromEntries(Object.entries(findings).map(([k,v])=>[k,v.length])),ocorrencias:findings};
fs.writeFileSync(path.join(output,'censo-integracao.json'),JSON.stringify(report,null,2)+'\n');
fs.writeFileSync(path.join(output,'fontes-inventario.json'),JSON.stringify([...sources.values()],null,2)+'\n');
console.log(JSON.stringify({totais:counts,contagens:report.contagens},null,2));
for(const [k,v] of Object.entries(findings))console.log(k,JSON.stringify(v.slice(0,2)));
