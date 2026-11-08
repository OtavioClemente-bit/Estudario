import fs from 'node:fs/promises';
import path from 'node:path';
import {fileURLToPath} from 'node:url';
import {spawnSync} from 'node:child_process';
const work=path.dirname(fileURLToPath(import.meta.url));
const adb=path.join(process.env.LOCALAPPDATA,'Android/Sdk/platform-tools/adb.exe');
const run=args=>{const r=spawnSync(adb,args,{maxBuffer:24*1024*1024,windowsHide:true});if(r.status!==0)throw Error(r.stderr.toString());return r.stdout;};
const [mode,arg,...rest]=process.argv.slice(2);
if(['arvore','tocar','voltar','rolar'].includes(mode)){
 const focus=run(['shell','dumpsys','window']).toString().match(/mCurrentFocus=.*$/m)?.[0]??'';
 if(!focus.includes('br.com.estudario'))throw Error('Ação suspensa: outro aplicativo ficou ativo.');
}
if(mode==='abrir'){console.log(run(['shell','monkey','-p','br.com.estudario','-c','android.intent.category.LAUNCHER','1']).toString());}
else if(mode==='capturar'){
 const focus=run(['shell','dumpsys','window']).toString().match(/mCurrentFocus=.*$/m)?.[0]??'';
 if(!focus.includes('br.com.estudario'))throw Error('Estudário não está na frente: '+focus);
 if(!/^[a-z0-9-]+$/.test(arg))throw Error('Nome inválido');
 await fs.mkdir(path.join(work,'referencias/capturas-atuais'),{recursive:true});
 const png=run(['exec-out','screencap','-p']);
 if(png.subarray(1,4).toString()!=='PNG')throw Error('Captura inválida');
 const file=path.join(work,'referencias/capturas-atuais',arg+'.png');await fs.writeFile(file,png);console.log(file);
}
else if(mode==='arvore'){
 const xml=run(['exec-out','uiautomator','dump','/dev/tty']).toString();
 const nodes=[...xml.matchAll(/<node\b[^>]*>/g)].map(m=>{const attrs=Object.fromEntries([...m[0].matchAll(/([\w-]+)="([^"]*)"/g)].map(x=>[x[1],x[2]]));return {text:attrs.text,description:attrs['content-desc'],clickable:attrs.clickable,bounds:attrs.bounds,package:attrs.package};}).filter(n=>n.package==='br.com.estudario'&&(n.text||n.description));
 await fs.mkdir(path.join(work,'referencias'),{recursive:true});
 await fs.writeFile(path.join(work,'referencias/ultima-arvore.json'),JSON.stringify(nodes,null,2)+'\n');
 console.log(JSON.stringify(arg?nodes.filter(n=>new RegExp(arg,'i').test(n.text+' '+n.description)):nodes,null,2));
}
else if(mode==='tocar'){console.log(run(['shell','input','tap',arg,rest[0]]).toString());}
else if(mode==='voltar'){console.log(run(['shell','input','keyevent','4']).toString());}
else if(mode==='rolar'){console.log(run(['shell','input','swipe',arg,...rest]).toString());}
else throw Error('Modo inválido');

