import fs from 'node:fs/promises';
import path from 'node:path';
import {fileURLToPath} from 'node:url';
const work=path.dirname(fileURLToPath(import.meta.url));
const queue=JSON.parse(await fs.readFile(path.join(work,'upload-pendente.json'),'utf8')),results=[];
for(const ref of queue){
 try{
  const response=await fetch(ref.uploadUrl,{method:'POST',headers:{'Content-Type':'application/octet-stream'},body:await fs.readFile(ref.path)});
  const body=await response.text();let result;try{result=JSON.parse(body);}catch{result={body:body.slice(0,500)};}
  results.push({id:ref.id,path:ref.path,status:response.status,result});
  console.log(JSON.stringify({id:ref.id,status:response.status,result}));
  if(!response.ok){process.exitCode=1;break;}
 }catch(e){results.push({id:ref.id,error:String(e)});console.log(JSON.stringify({id:ref.id,error:String(e)}));process.exitCode=1;break;}
}
await fs.writeFile(path.join(work,'referencias-canva.json'),JSON.stringify(results,null,2)+'\n');
await fs.unlink(path.join(work,'upload-pendente.json'));
