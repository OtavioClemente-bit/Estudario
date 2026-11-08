const { Worker } = require('node:worker_threads');
const fs=require('node:fs'),path=require('node:path'),assert=require('node:assert/strict');
const root=path.resolve('web/build/compileSync/js/main/productionExecutable/kotlin');
const benchmark=fs.readFileSync(path.join(__dirname,'benchmark-forecast.cjs'),'utf8');
const fixture=benchmark.slice(benchmark.indexOf('const today='),benchmark.lastIndexOf('console.log(count'));
// A synthetic planner load only; no educational questions or account data.
let request;
eval(fixture.replace('for(const count of [1000]) {','{ const count=1000;')+'request={snapshot:JSON.stringify(data),planId:"perf",freeRules:false,excludedTopicIds:[]}; }');
const code=`const {parentPort}=require('node:worker_threads');
global.WorkerGlobalScope=class WorkerGlobalScope{};
global.self=new WorkerGlobalScope();
self.postMessage=value=>parentPort.postMessage(value);
self.addEventListener=(type,fn)=>{if(type==='message')self.onmessage=fn;};
parentPort.on('message',data=>self.onmessage({data}));
require(${JSON.stringify(path.join(root,'estudario.js'))});
parentPort.postMessage({ready:true});`;
(async()=>{
const worker=new Worker(code,{eval:true});
let ticks=0,maxDrift=0,previous=performance.now();
const timer=setInterval(()=>{const now=performance.now();maxDrift=Math.max(maxDrift,now-previous-10);previous=now;ticks++;},10);
try {
 const result=await new Promise((resolve,reject)=>{
  worker.on('error',reject);
  worker.on('message',message=>{if(message?.ready)worker.postMessage(JSON.stringify(request));else resolve(message);});
 });
 assert.equal(result.ok,true,JSON.stringify(result));
 const forecast=JSON.parse(result.result);
 assert.equal(forecast.roadmap.totalTopics,1000);
 assert.ok(ticks>20,'UI heartbeat must keep running during the forecast');
 assert.ok(maxDrift<250,`UI heartbeat blocked for ${maxDrift}ms`);
 console.log(JSON.stringify({topics:1000,ticks,maxDriftMs:Math.round(maxDrift),forecastReturned:true}));
}finally{clearInterval(timer);await worker.terminate();}
})().catch(error=>{console.error(error);process.exitCode=1;});
