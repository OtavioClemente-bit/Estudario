// aplica textos escritos à mão:
//  "i": texto da correta | "i:L": texto da alternativa L |
//  "i=": {statement?, options:[5 textos, "*" marca a correta], explanation?} (letras A–E na ordem escrita)
const fs=require('fs');const [f,p]=process.argv.slice(2);const j=JSON.parse(fs.readFileSync(f,'utf8'));const P=JSON.parse(fs.readFileSync(p,'utf8'));
for(const [k,v] of Object.entries(P)){
  if(k.endsWith('=')){const q=j.questions[+k.slice(0,-1)];if(v.statement)q.statement=v.statement;if(v.explanation)q.explanation=v.explanation;
    if(v.options)q.options=v.options.map((t,n)=>({key:'ABCDE'[n],text:t.replace(/^\*/,''),correct:t.startsWith('*')}));continue;}
  const [i,key]=k.split(':');const q=j.questions[+i];const o=key?q.options.find(o=>o.key===key):q.options.find(o=>o.correct);o.text=v;}
fs.writeFileSync(f,JSON.stringify(j,null,2)+'\n');
