import fs from 'node:fs/promises';
import path from 'node:path';
import {fileURLToPath} from 'node:url';
const out=path.dirname(fileURLToPath(import.meta.url)),file=path.join(out,'galeria.html');
const current=JSON.parse(await fs.readFile(path.join(out,'manifesto.json'),'utf8'));
if(current.complete)throw Error('Script histórico da prévia: a coleção já foi concluída. Use montar-camadas.mjs e montar-pacote.mjs.');
let s=await fs.readFile(file,'utf8');
const edits=[
['<title>Estudário · Atlas de conquistas</title>','<title>Estudário · Coleção III · Prévia em revisão</title>'],
['<section class="hero">','<aside class="review-banner"><strong>Prévia em revisão · 51 cenas conferidas de 65</strong><p>14 cenas ainda precisam de reconstrução dos braços ou do livro. Elas aparecem identificadas e não são versões finais. <a href="PENDENCIAS.md">Ver pendências</a>.</p></aside><section class="hero">'],
['</style>',' .review-banner{border:1px solid var(--gold);border-radius:12px;padding:18px 22px;margin-top:24px;background:var(--surface)}.review-banner p{font-size:13px;color:var(--muted);margin:8px 0 0}.review-banner a{color:var(--gold)}.qa-pending{border-color:#b96759}.qa-label{font-size:10px;margin-top:8px;color:var(--muted)}.qa-pending .qa-label{color:#e9a397}</style>'],
["let cat='all',tier='all',locked=false","let cat='all',tier='all',locked=false"],
["'<button class=\"card\" data-id=\"'+b.id", "'<button class=\"card '+(b.qaStatus==='corrigir'?'qa-pending':'')+'\" data-id=\"'+b.id"],
["+(locked?' · A conquistar':'')+'</span></button>'", "+(locked?' · A conquistar':'')+'</span><span class=\"qa-label\">'+(b.qaStatus==='corrigir'?'EM REVISÃO · anatomia pendente':'Conferido nesta rodada')+'</span></button>'"],
["$('detail-tier').textContent=b.tierLabel+' · Nível '+b.tier;", "$('detail-tier').textContent=b.tierLabel+' · Nível '+b.tier+(b.qaStatus==='corrigir'?' · EM REVISÃO':' · Conferido');"],
["$('story').textContent=b.story;", "$('story').textContent=b.story+(b.qaStatus==='corrigir'?' REVISÃO PENDENTE: '+b.qaNote:'');"],
['65 histórias ilustradas · sete auréolas em evolução','65 histórias ilustradas · sete auréolas em evolução · 14 reconstruções pendentes']
];
for(const [a,b] of edits){if(!s.includes(a))throw Error('Trecho ausente: '+a);s=s.replace(a,b);}
await fs.writeFile(file,s);
let readme=await fs.readFile(path.join(out,'LEIA-ME.md'),'utf8');
readme=readme.replace('Os 124 arquivos','Os 125 arquivos').replace('verificacao.json e camadas-verificacao.json','verificacao.json e producao-final/camadas-verificacao.json');
await fs.writeFile(path.join(out,'LEIA-ME.md'),readme);
console.log('Galeria sinaliza as 14 cenas pendentes.');

