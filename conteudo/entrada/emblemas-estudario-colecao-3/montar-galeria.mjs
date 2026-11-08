import fs from 'node:fs/promises';
import path from 'node:path';
import {fileURLToPath} from 'node:url';
const here=path.dirname(fileURLToPath(import.meta.url));
let html=await fs.readFile(path.resolve(here,'../emblemas-estudario-2026-10-23-v2/galeria.html'),'utf8');
html=html.replaceAll('Coleção II','Coleção III').replaceAll('50 histórias ilustradas','65 histórias ilustradas').replaceAll('Ver os 50 em uma prancha','Ver os 65 em uma prancha');
html=html.replace("const tierNames=['Todas as faixas','Bronze','Prata','Ouro','Safira','Rubi','Esmeralda'];","const tierNames=['Todas as faixas','Bronze','Prata','Ouro','Safira','Rubi','Esmeralda','Anual'];");
html=html.replace("b.tierLabel===tierNames[Number(tier)]","b.tier===Number(tier)");
html=html.replaceAll("'<img src=\"'+b.png+'\"", "'<img src=\"'+b.preview+'\"");
// Conservar a imagem original no detalhe e nos downloads; miniaturas usam a exportação de 512 px.
html=html.replaceAll("<img src=\"'+s.png+'\"", "<img src=\"'+s.preview+'\"");
html=html.replaceAll("<img src=\"'+b.png+'\" alt=\"\" loading=\"lazy\"", "<img src=\"'+b.preview+'\" alt=\"\" loading=\"lazy\"");
html=html.replace('<div class="caption">65 histórias ilustradas · Bronze, prata, ouro e pedras preciosas</div>', '<div class="caption">65 histórias ilustradas · sete auréolas em evolução</div><a href="evolucao-aureolas.png" target="_blank" rel="noopener" style="display:block;max-width:1000px;margin:35px auto"><img src="evolucao-aureolas.png" alt="Evolução das sete auréolas: Bronze, Prata, Ouro, Safira, Rubi, Esmeralda e anual" style="display:block;width:100%;height:auto;border-radius:14px"></a>');
html=html.replace("$('ladder').innerHTML=B.filter", "$('ladder').setAttribute('aria-label',b.category==='especial'?'Outras conquistas especiais':'Evolução da família');$('ladder').innerHTML=B.filter");
await fs.writeFile(path.join(here,'galeria.html'),html);
console.log('Galeria da Coleção III criada sem modificar a Coleção II.');
