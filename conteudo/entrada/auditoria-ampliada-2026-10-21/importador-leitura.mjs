// Adaptador de runtime: executa o importador oficial sem --publicar e sem acesso a segredos.
import fs from 'node:fs';
globalThis.Deno={
  args:[],
  readDirSync(dir){return fs.readdirSync(dir,{withFileTypes:true}).map(e=>({name:e.name,isDirectory:e.isDirectory(),isFile:e.isFile()}));},
  readTextFile(file){return fs.promises.readFile(file,'utf8');},
  exit(code){process.exit(code);},
};
await import('../../../scripts/biblioteca/importar.ts');
