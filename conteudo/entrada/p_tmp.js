const j=require(process.argv[2]);
console.log("SCOPE",JSON.stringify(j.scope));
for(const c of j.chapters)console.log("## "+c.title+"\n"+c.markdown);
console.log("SUMMARY",JSON.stringify(j.summary));
console.log("TIPS",JSON.stringify(j.tips),"TRAPS",JSON.stringify(j.traps));
j.flashcards.forEach(f=>console.log("FC",f.front,"=>",f.back));
j.questions.forEach((q,i)=>console.log(`Q${i+1} [${q.format}] ${q.statement}\n`+q.options.map(o=>` ${o.key}${o.correct?"*":""} ${o.text}`).join("\n")+`\n EXP: ${q.explanation}`));
