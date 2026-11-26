const U=Deno.env.get("SUPABASE_URL")!,K=Deno.env.get("SUPABASE_SERVICE_ROLE_KEY")!;const H={apikey:K,Authorization:`Bearer ${K}`};
const D="work/auditoria-simulados-2026-11-26/";
const rows:any[]=[];
for(let o=0;;o+=50){const r=await fetch(`${U}/rest/v1/exam_simulation?select=id,catalog_id,number,board,format,structure,item_count,questions&status=eq.PUBLISHED&order=id&offset=${o}&limit=50`,{headers:H});const j=await r.json();if(!Array.isArray(j)){console.log(o,j);o-=50;continue;}rows.push(...j);if(j.length<50)break;}
const norm=(s:string)=>(s||"").toLowerCase().normalize("NFD").replace(/[̀-ͯ]/g,"").replace(/[^a-z0-9]+/g," ").trim();
const pesos=JSON.parse(await Deno.readTextFile("conteudo/pesos-provas.json"));const peso=new Map<string,number>();
// 26/11: o total conta também os blocos ("blocos": [{questoes}]) e as matérias em objeto ({questoes, nota}); antes só somava os números soltos.
for(const p of pesos){const t=(Object.values(p.materias||{}).reduce((a:any,b:any)=>a+(typeof b==="number"?b:(+b?.questoes||0)),0) as number)+((p.blocos||[]).reduce((a:number,b:any)=>a+(+b.questoes||0),0));for(const e of p.editais||[])peso.set(e,t);}
const c:Record<string,string[]>={};const add=(k:string,id:string)=>{(c[k]??=[]).push(id)};
const byCat=new Map<string,any[]>();const boards:Record<string,number>={};let totalQ=0;
for(const s of rows){(byCat.get(s.catalog_id)??byCat.set(s.catalog_id,[]).get(s.catalog_id)!).push(s);boards[s.board]=(boards[s.board]||0)+1;
 const qs=s.questions||[];totalQ+=qs.length;
 if(s.item_count!==qs.length)add("item_count_diverge",`${s.id} (${s.item_count} vs ${qs.length})`);
 const fmts=new Set(qs.map((q:any)=>q.format));if(fmts.size>1)add("mistura_formato",`${s.id} ${[...fmts]}`);
 const ce=/cebraspe|cespe/i.test(s.board||"");const hasMC=fmts.has("MULTIPLE_CHOICE");
 if(ce&&hasMC)add("cebraspe_com_AE",s.id);
 if(!ce&&fmts.has("CERTO_ERRADO")&&!hasMC)add("nao_cebraspe_so_CE",`${s.id} [${s.board}]`);
 const seen=new Set<string>();const subj:Record<string,number>={};
 for(const q of qs){const qid=`${s.id}#${q.n}`;subj[q.subject]=(subj[q.subject]||0)+1;
  const op=q.options||[];const nc=op.filter((o:any)=>o.correct).length;
  if(nc!==1)add("gabarito_invalido(n_corretas!=1)",`${qid} (${nc})`);
  const exp=q.format==="CERTO_ERRADO"?2:5;if(op.length!==exp)add("n_alternativas_errado",`${qid} (${op.length}, ${q.format})`);
  const ot=op.map((o:any)=>(o.text||"").trim().toLowerCase().replace(/\s+/g," "));if(new Set(ot).size!==ot.length)add("alternativas_duplicadas",qid);
  if(op.some((o:any)=>!(o.text||"").trim()))add("alternativa_vazia",qid);
  const ks=op.map((o:any)=>o.key);if(new Set(ks).size!==ks.length)add("letras_duplicadas",qid);
  if(!norm(q.explanation)||norm(q.explanation).length<40)add("explicacao_vazia_ou_curta(<40)",qid);
  if(!norm(q.statement))add("enunciado_vazio",qid);
  const k=norm(q.statement)+"|"+ot.join("|");if(seen.has(k))add("questao_duplicada_na_prova",qid);seen.add(k);
  if(!q.subject)add("sem_disciplina",qid);
 }
 const n=qs.length;for(const [m,v] of Object.entries(subj)){if(n>=20&&v/n>0.6&&Object.keys(subj).length>1)add("disciplina_domina>60%",`${s.id} ${m} ${v}/${n}`);}
 const pe=peso.get(s.catalog_id);if(pe&&Math.abs(n-pe)/pe>0.25)add("tamanho_vs_prova_real>25%",`${s.id} ${n} vs ${pe}`);
 if(n<10)add("prova_com_<10_questoes",`${s.id} (${n})`);
}
const nums:Record<string,number>={};
for(const [cat,ss] of byCat){nums[ss.length]=(nums[ss.length]||0)+1;if(ss.length>3)add("edital_com_>3_provas",`${cat} (${ss.length})`);
 const ns=ss.map(s=>s.number).sort();if(!ns.includes(1))add("edital_sem_simulado_1",cat);
 const st=new Map<string,string>();
 for(const s of ss.sort((a,b)=>a.number-b.number))for(const q of s.questions||[]){const k=norm(q.statement);const prev=st.get(k);if(prev&&prev!==s.id)add("questao_repetida_entre_provas",`${s.id}#${q.n} = ${prev}`);else if(!prev)st.set(k,s.id);}
}
const out={provas:rows.length,editais:byCat.size,questoes:totalQ,provasPorEdital:nums,bancas:boards,defeitos:Object.fromEntries(Object.entries(c).map(([k,v])=>[k,{n:v.length,ex:v.slice(0,8)}]))};
await Deno.writeTextFile(D+"resultado.json",JSON.stringify(out,null,1));
await Deno.writeTextFile(D+"defeitos-completo.json",JSON.stringify(c));
// sample dump for manual read
const want=["cebraspe","fgv","fcc","vunesp","cesgranrio","ibfc","idecan","quadrix","aocp","consulplan"];const samp:any[]=[];
for(const b of want){const cands=rows.filter(r=>(r.board||"").toLowerCase().includes(b));if(!cands.length)continue;const s=cands[Math.floor(cands.length*0.37)];
 samp.push({id:s.id,board:s.board,structure:s.structure,qs:(s.questions||[]).filter((_:any,i:number)=>i%Math.ceil(s.questions.length/8)===0).slice(0,8).map((q:any)=>({n:q.n,subj:q.subject,st:q.statement,op:q.options.map((o:any)=>`${o.key}${o.correct?"*":""}) ${o.text}`),ex:q.explanation}))});}
await Deno.writeTextFile(D+"amostra.json",JSON.stringify(samp,null,1));
console.log(JSON.stringify(out,null,1).slice(0,6000));
