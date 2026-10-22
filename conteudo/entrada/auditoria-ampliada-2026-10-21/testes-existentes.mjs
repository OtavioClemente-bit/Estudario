// Executa os testes existentes sob Node; não equivale ao runtime Deno ou Android.
import {registerHooks} from 'node:module';
import fs from 'node:fs';
const assertionURL=new URL('./assert-node.mjs',import.meta.url).href;
registerHooks({resolve(specifier,context,next){
  if(specifier==='jsr:@std/assert@1'||specifier==='https://deno.land/std@0.224.0/assert/mod.ts')return {url:assertionURL,shortCircuit:true};
  return next(specifier,context);
}});
const tests=[];
globalThis.Deno={test(name,fn){tests.push({name,fn});}};
await import('../../../supabase/functions/_shared/library_test.ts');
await import('../../../supabase/functions/_shared/simulation_library_test.ts');
const results=[];
for(const t of tests){try{await t.fn();results.push({nome:t.name,passou:true});}catch(e){results.push({nome:t.name,passou:false,erro:e.stack});}}
const report={runtime:'Node 24 com transformacao TypeScript e adaptador de assertions/Deno.test; nao e execucao nativa Deno',total:results.length,passaram:results.filter(r=>r.passou).length,resultados:results};
fs.writeFileSync(new URL('./testes-existentes.json',import.meta.url),JSON.stringify(report,null,2)+'\n');
console.log(JSON.stringify(report,null,2));
if(report.passaram!==report.total)process.exitCode=1;
