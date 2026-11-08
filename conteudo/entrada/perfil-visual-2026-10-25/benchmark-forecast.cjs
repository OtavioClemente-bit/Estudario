const fs=require('node:fs'),path=require('node:path'),Module=require('node:module');
const root=path.resolve('web/build/compileSync/js/main/productionExecutable/kotlin');
const file=path.join(root,'estudario.js');
let source=fs.readFileSync(file,'utf8');
source=source.replace('  mainWrapper();','  _.qa = { forecast: text => { const data=Companion_getInstance_38().zn(text); const plan=Queries_getInstance().z5i(data); const start=performance.now(); const result=Forecast_instance.q68(data,plan); return {ms: performance.now()-start, result: result != null}; } };');
const mod=new Module(file,module);mod.filename=file;mod.paths=Module._nodeModulePaths(root);mod._compile(source,file);
const today=Math.floor(Date.now()/86400000);
for(const count of [1000]) {
 const data={format:'estudario-backup',competitions:[{id:1,name:'Perf',primary:true}],subjects:[{id:1,competitionId:1,name:'Perf',position:0}],topics:Array.from({length:count},(_,i)=>({id:i+1,subjectId:1,title:'Tópico de teste '+i,position:i,status:'NAO_ESTUDADO'})),studyPlans:[{id:'perf',competitionId:1,name:'Perf',start:today,exam:today+180,active:true,profile:'DO_ZERO'}],planSubjects:[{planId:'perf',subjectId:1,name:'Perf'}],studyAvailability:Array.from({length:7},(_,i)=>({planId:'perf',day:i+1,minutes:i===6?0:120,unavailable:i===6}))};
 console.log(count,mod.exports.qa.forecast(JSON.stringify(data)));
}



