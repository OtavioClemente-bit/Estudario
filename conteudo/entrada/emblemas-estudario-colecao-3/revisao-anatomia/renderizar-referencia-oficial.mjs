// Rasteriza o SVG oficial existente apenas como guia de anatomia, sem alterar o app.
import fs from 'node:fs/promises';
import path from 'node:path';
import vm from 'node:vm';
import {fileURLToPath} from 'node:url';
import {createRequire} from 'node:module';
const require=createRequire(import.meta.url);
const sharp=require('C:/Users/otavi/.cache/codex-runtimes/codex-primary-runtime/dependencies/node/node_modules/sharp');
const here=path.dirname(fileURLToPath(import.meta.url));
const context=vm.createContext({window:{}});
vm.runInContext(await fs.readFile(path.resolve(here,'../../../../site/assets/folha.js'),'utf8'),context);
for(const mood of ['idle','point','happy']){
 const svg=context.window.folhaSvg({mood,size:1200,look:[0,0],shadow:false,sparkles:false,confetti:false});
 await sharp(Buffer.from(svg)).png().toFile(path.join(here,'folha-oficial-'+mood+'.png'));
}
console.log('Três referências do SVG oficial renderizadas; código do app preservado.');
