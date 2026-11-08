/** Composição vetorial das conquistas. Não produz questões nem conteúdo didático. */
import fs from 'node:fs/promises';
import path from 'node:path';
import { fileURLToPath, pathToFileURL } from 'node:url';
import { createRequire } from 'node:module';

const OUT = path.dirname(fileURLToPath(import.meta.url));
const ROOT = path.resolve(OUT, '../../..');
const { build, C } = await import(pathToFileURL(path.join(ROOT, 'design/turma/turma.js')));
const turma = build();
const INK = '#26234A', PAPER = '#FFFDF8', GOLD = '#F5B83D', MINT = '#7EE0B8';
const esc = s => String(s).replaceAll('&','&amp;').replaceAll('<','&lt;').replaceAll('>','&gt;').replaceAll('"','&quot;');
const p = (d, fill = 'none', stroke = INK, sw = 2, more = '') => `<path d="${d}" fill="${fill}" stroke="${stroke}" stroke-width="${sw}" stroke-linecap="round" stroke-linejoin="round" ${more}/>`;
const dot = (x,y,r,c) => `<circle cx="${x}" cy="${y}" r="${r}" fill="${c}"/>`;
const line = (d,c=INK,w=2) => p(d,'none',c,w);
const group = (transform, inner) => `<g transform="${transform}">${inner}</g>`;
const star = (x,y,s=1,c=GOLD) => group(`translate(${x} ${y}) scale(${s})`,p('M0-8 2.3-2.5 8 0 2.3 2.5 0 8-2.3 2.5-8 0-2.3-2.5Z',c,INK,1.7));
const seal = (x,y,s=1) => group(`translate(${x} ${y}) scale(${s})`,p('M-8-10-3-12 0-10 4-12 8-8 11-6 10-1 12 3 8 7 6 11 1 10-3 12-7 8-11 6-10 1-12-3-8-7Z',MINT)+line('M-5 0-1 4 6-4',INK,2.8));
const cap = (x,y,s=1) => group(`translate(${x} ${y}) scale(${s})`,p('M-13 1V9Q0 17 13 9V1Z','#33256F')+p('M-22-4 0-13 22-4 0 5Z','#5141AC')+line('M0-4 17-2V11',GOLD,2)+p('M15 10 19 10 20 18 14 18Z',GOLD));
const crown = (x,y,s=1) => group(`translate(${x} ${y}) scale(${s})`,p('M-17-8-8 0 0-13 8 0 17-8 13 12H-13Z',GOLD)+line('M-10 7H10','#FFF1BF',2.4)+dot(0,3,2.6,'#FF5C4D'));
const pencil = (x,y,s=1,rot=-25) => group(`translate(${x} ${y}) rotate(${rot}) scale(${s})`,p('M-4-20H4V15L0 24-4 15Z',GOLD)+p('M-4-20V-25Q0-29 4-25V-20Z','#FF8FA3')+p('M-4 15H4L0 24Z','#F7D9A8')+p('M-1.8 20H1.8L0 24Z',INK,INK,0)+line('M0-16V10','#FFF1BF',1.6));
const leaf = (x,y,s=1,rot=0,color=MINT) => group(`translate(${x} ${y}) rotate(${rot}) scale(${s})`,p('M0 12C-17 7-16-8-1-13 13-8 15 7 0 12Z',color)+line('M0 10 0-7',INK,1.5));
const spark = (d,c=GOLD) => line(d,c,2.6);
const book = (x,y,s=1,c='#5B4BF5') => group(`translate(${x} ${y}) scale(${s})`,p('M-17-17H9Q15-17 15-11V17H-15Q-20 16-20 11V-12Q-20-17-17-17Z',c)+p('M-15 10H15V18H-15Q-21 14-15 10Z',PAPER)+line('M-12-9H6M-12-3H1','#FFFFFF',1.8)+line('M-13 14H10','#B7B4D0',1.2));
const ticket = (x,y,s=1) => group(`translate(${x} ${y}) rotate(14) scale(${s})`,p('M-15-10H15V-4Q7 0 15 4V10H-15V4Q-7 0-15-4Z',PAPER)+line('M-3-7V7','#B7B4D0',1.6)+line('M3-4 10-4M3 1H10',INK,1.5));
const mountains = p('M43 113 66 82 85 104 109 68 146 112Z','#D7D0F5',INK,1.7)+p('M96 85 109 68 122 86 111 82 105 87Z',PAPER,INK,1.5);
const wings = p('M50 81C32 72 22 58 17 42 32 45 40 50 49 56 38 42 32 28 35 17 49 24 59 38 63 57L68 89Z','#E9E3FF')+line('M25 51 55 77M39 32 58 66','#8C7DCE',2)+p('M150 81C168 72 178 58 183 42 168 45 160 50 151 56 162 42 168 28 165 17 151 24 141 38 137 57L132 89Z','#E9E3FF')+line('M175 51 145 77M161 32 142 66','#8C7DCE',2);
const laurel = p('M51 135C31 116 30 85 42 64M149 135C169 116 170 85 158 64','none',INK,2.5)+
  p('M40 106Q18 100 25 86Q42 90 40 106M40 90Q23 79 33 68Q47 77 40 90M45 122Q23 122 27 108Q44 109 45 122M49 75Q35 59 48 51Q60 64 49 75',MINT)+
  p('M160 106Q182 100 175 86Q158 90 160 106M160 90Q177 79 167 68Q153 77 160 90M155 122Q177 122 173 108Q156 109 155 122M151 75Q165 59 152 51Q140 64 151 75',MINT);

const META = {
  plano: {label:'Plano de estudos',glyph:'Calendar',color:'#5B4BF5',soft:'#E8E3FF',shape:'M100 28Q124 36 155 39V91Q155 126 100 151 45 126 45 91V39Q76 36 100 28Z'},
  seq: {label:'Sequência',glyph:'Fire',color:'#FF8A2B',soft:'#FFE6CC',shape:'M100 26 114 37 132 33 140 49 157 54 155 73 166 87 155 102 157 120 140 126 132 143 113 139 100 151 86 139 68 143 60 126 43 120 45 102 34 87 45 73 43 54 60 49 68 33 86 37Z'},
  meta: {label:'Metas diárias',glyph:'Flag',color:'#22C27B',soft:'#D9F6E7',shape:'M100 25Q106 25 112 30L156 66Q161 71 158 79L141 135Q138 144 128 144H72Q62 144 59 135L42 79Q39 71 44 66L88 30Q94 25 100 25Z'},
  quest: {label:'Questões',glyph:'Clipboard',color:'#38B2FF',soft:'#DDF2FF',shape:'M78 29H122L158 65V112L122 148H78L42 112V65Z'},
  acerto: {label:'Pontaria',glyph:'Target',color:'#FF5C4D',soft:'#FFE3DD',shape:'M100 28C135 28 160 54 160 89S135 150 100 150 40 124 40 89 65 28 100 28Z'},
  topico: {label:'Tópicos',glyph:'OpenBook',color:'#3FD9D0',soft:'#D7F6EF',shape:'M75 29H125L157 61V116L125 148H75L43 116V61Z'},
  edital: {label:'Edital',glyph:'Doc',color:'#FFB421',soft:'#FFF0CC',shape:'M100 22Q105 22 109 27L163 80Q170 88 163 96L109 150Q100 158 91 150L37 96Q30 88 37 80L91 27Q95 22 100 22Z'},
  revisao: {label:'Revisões',glyph:'Cycle',color:'#9563FF',soft:'#EEE3FF',shape:'M100 28C111 19 123 28 126 36 141 31 153 44 149 56 165 60 169 76 158 86 170 100 163 116 150 118 153 134 139 144 125 139 119 155 103 159 94 146 80 155 65 148 65 136 49 138 38 125 45 111 29 102 29 87 42 79 33 65 40 50 54 49 52 35 68 25 80 34 84 24 94 21 100 28Z'},
  simulado: {label:'Simulados',glyph:'Clipboard',color:'#2EA8F5',soft:'#DFEEFF',shape:'M62 30H138Q158 30 158 50V127Q158 147 138 147H62Q42 147 42 127V50Q42 30 62 30Z'},
  discursiva: {label:'Discursivas',glyph:'Pencil',color:'#F0559A',soft:'#FFE4EF',shape:'M58 29H142Q151 29 151 38V115L100 153 49 115V38Q49 29 58 29Z'},
  maratona: {label:'Maratona',glyph:'Timer',color:'#8E5CF7',soft:'#ECE2FF',shape:'M65 33H135L164 88 135 143H65L36 88Z'},
};
const METALS = {
  1:['Bronze','#D79A6C','#9E603D','#F6D3B4'],
  2:['Prata','#CBD4E5','#7D8699','#F7FAFF'],
  3:['Ouro','#F5B83D','#B07510','#FFF1BF'],
  4:['Safira','#69A8FF','#2956A8','#D8E9FF'],
  5:['Rubi','#F06F86','#A12D4E','#FFDBE3'],
  6:['Esmeralda','#4AD5A2','#14785F','#D4FFEE'],
  7:['Esmeralda','#4AD5A2','#14785F','#D4FFEE'],
};

// Uma direção de composição escrita para cada ID real do catálogo.
// part: personagem oficial, x, y, largura. Os adereços abaixo são caminhos novos.
const ARTS = [
  ['plano-10','Calendar',61,52,78, pencil(144,98,.65), 'O primeiro plano ganha vida com calendário e lápis.'],
  ['plano-50','Calendar',56,52,80, seal(144,111,.8)+spark('M47 70 39 65M46 79H35'), 'O ritmo aparece no selo e nos pequenos pulsos de movimento.'],
  ['plano-200','Calendar',68,65,65, book(60,106,.55)+book(145,112,.5,'#22C27B')+cap(100,52,.6), 'O calendário se torna o centro de uma rotina de estudos.'],
  ['plano-500','Gear',61,56,78, seal(100,95,1.65)+pencil(143,105,.55,20), 'Uma engrenagem com confirmação, para a máquina de cumprir.'],
  ['plano-1000','Pergaminho',60,57,82, crown(100,43,.7)+seal(143,117,.7), 'Um pergaminho coroado transforma o plano em compromisso.'],
  ['seq-3','Fire',65,51,70, p('M65 125 139 114 143 123 70 135Z','#C98A4B')+line('M77 126 91 124M118 119 130 118','#F7D9A8',1.8), 'Uma chama pequena sobre o primeiro graveto.'],
  ['seq-7','Fire',65,44,70, p('M85 116H115L110 134H90Z',GOLD)+p('M91 109H109V119H91Z','#A86E32')+star(144,71,.55), 'A chama da primeira semana já ocupa uma pequena tocha.'],
  ['seq-14','Fire',57,55,85, leaf(51,109,.65,-30)+leaf(149,109,.65,30), 'Uma labareda maior ganha dois ramos de resistência.'],
  ['seq-30','Fire',66,55,68, p('M61 51Q100 29 139 51V128H61Z','none',INK,3)+p('M61 53H139V62H61Z',GOLD)+p('M73 127H127L135 137H65Z',GOLD)+line('M99 31V23',INK,3), 'A lanterna protege a chama ao longo de um mês.'],
  ['seq-60','Fire',62,48,77, p('M60 107Q39 89 42 67 57 70 66 88M140 107Q161 89 158 67 143 70 134 88','#FFB421')+star(146,50,.6), 'Asas de fogo representam a constância de dois meses.'],
  ['seq-100','Fire',65,52,72, crown(100,44,.72)+star(51,78,.65)+star(149,78,.65)+spark('M50 116 41 122M150 116 159 122'), 'A chama centenária recebe coroa e brilho de conquista.'],
  ['seq-365','OpenBook',60,69,80, cap(100,62,.88)+p('M78 123 91 126 91 141 85 136 78 141Z',MINT)+star(55,55,.65)+star(147,59,.65)+crown(100,26,.72), 'O ano inteiro culmina no Folha formado, coroado e cercado de estrelas.'],
  ['meta-1','Flag',61,49,80, leaf(76,124,.48,-25), 'A primeira bandeira nasce junto de uma pequena folha.'],
  ['meta-10','Flag',57,50,80, seal(145,112,.9)+star(53,73,.45), 'Dez dias: bandeira confirmada com um selo de conquista.'],
  ['meta-50','Flag',66,48,75, mountains, 'A bandeira sobe ao primeiro cume.'],
  ['meta-100','Flag',61,45,78, p('M65 123Q100 110 135 123V134H65Z',GOLD)+star(48,86,.55)+star(150,76,.55)+crown(100,42,.55), 'A centena de metas ganha um pódio e coroa.'],
  ['meta-250','Shield',62,53,79, group('translate(75 63) scale(.5)',p('M22 8v86','none',INK,7)+p('M26 12q16-8 32 0t32 0v38q-16 8-32 0t-32 0z','#FF5C4D'))+seal(142,119,.65), 'Uma bandeira dentro do escudo para a rotina blindada.'],
  ['quest-100','Clipboard',63,49,74, pencil(143,112,.63,24), 'A primeira centena começa com prancheta e lápis.'],
  ['quest-500','Clipboard',62,51,76, ticket(57,111,.7)+seal(145,105,.65), 'Bilhetes de resposta e selo para o meio milhar.'],
  ['quest-2000','Cards',55,56,90, pencil(144,105,.6,26)+star(56,61,.5), 'Cartões de treino se multiplicam na marca de dois mil.'],
  ['quest-5000','Clipboard',65,58,70, book(58,104,.55,'#38B2FF')+book(144,112,.55,'#9563FF')+cap(100,49,.63), 'Um acervo de treino acompanha as cinco mil respostas.'],
  ['quest-10000','Trophy',63,53,74, ticket(55,116,.65)+ticket(149,112,.65)+crown(100,43,.58), 'O troféu da grande coleção de dez mil questões.'],
  ['acerto-70','Target',59,47,84, star(148,113,.5), 'O alvo estabelece a primeira pontaria consistente.'],
  ['acerto-80','Target',60,48,82, leaf(49,110,.62,-30)+seal(149,116,.7)+spark('M143 46 149 39M150 53H159'), 'A pontaria ganha selo e linhas precisas de movimento.'],
  ['acerto-90','Target',62,53,78, crown(100,43,.7)+star(48,84,.6)+star(150,118,.55), 'Um alvo coroado para quem chega quase ao acerto perfeito.'],
  ['topico-10','OpenBook',58,55,84, p('M132 82V109L140 104 147 109V82Z',MINT), 'Um livro com o primeiro marcador de leitura.'],
  ['topico-50','Books',59,54,82, leaf(146,109,.7,20)+pencil(57,111,.5,-18), 'Uma pequena biblioteca começa a se formar.'],
  ['topico-150','OpenBook',60,57,80, book(54,119,.45,'#3FD9D0')+book(146,121,.45)+cap(100,46,.72), 'O livro aberto ganha repertório e capelo.'],
  ['topico-400','Books',61,57,79, cap(100,46,.85)+star(49,82,.6)+star(150,82,.6)+seal(140,123,.65), 'A enciclopédia da preparação, celebrada com formação e selo.'],
  ['edital-25','Doc',64,49,73, p('M135 94A16 16 0 1 1 134.9 94Z',PAPER)+p('M135 110H151A16 16 0 0 1 135 126Z',MINT), 'Um documento e um quarto marcado na roda de cobertura.'],
  ['edital-50','Doc',64,50,73, p('M135 94A16 16 0 0 1 135 126Z',MINT)+p('M135 94A16 16 0 0 0 135 126Z',PAPER), 'A meia roda torna visível a metade do edital.'],
  ['edital-75','Doc',65,55,71, p('M135 110V94A16 16 0 1 0 151 110Z',MINT)+p('M135 110H151A16 16 0 0 0 135 94Z',PAPER)+star(55,72,.5), 'Três quartos da roda preenchidos acompanham o documento.'],
  ['edital-100','Pergaminho',61,55,80, seal(139,117,1)+cap(100,45,.66)+star(52,84,.5), 'O edital fechado vira pergaminho certificado.'],
  ['revisao-1','Cycle',59,52,82, leaf(141,119,.48,20), 'O primeiro retorno é uma volta que faz nascer memória.'],
  ['revisao-10','Cycle',59,53,82, ticket(55,116,.55)+seal(147,118,.6), 'O ciclo em dia recebe seu primeiro registro.'],
  ['revisao-25','Brain',60,52,80, p('M45 85A56 56 0 0 1 131 46M155 100A56 56 0 0 1 76 139','none','#9563FF',4)+p('M128 38 142 49 127 55Z',MINT)+p('M79 130 64 136 76 147Z',MINT), 'O revisor faz o cérebro circular entre as lembranças.'],
  ['revisao-100','Brain',59,52,82, book(55,122,.48)+book(146,122,.48,'#9563FF')+star(54,65,.55), 'A memória treinada conecta repertório e estudo.'],
  ['revisao-400','Cycle',61,60,78, p('M73 40Q100 23 127 40','none',GOLD,3)+crown(100,39,.65)+seal(147,121,.65), 'Um ciclo coroado para conservar tudo o que foi aprendido.'],
  ['revisao-1000','Brain',71,58,65, p('M74 79C57 65 45 74 48 93 49 106 62 109 75 97Z','#ABA2FF')+p('M126 80Q146 73 148 88L147 119Q144 137 130 134L129 124Q140 129 140 117V100L125 96Z','#ABA2FF')+p('M76 111V133H87V116M112 113V133H123V110Z','#ABA2FF')+crown(101,46,.7)+dot(136,84,2.4,INK), 'Memória de elefante: cérebro, orelha, tromba e coroa numa peça exclusiva.'],
  ['simulado-1','Clipboard',63,51,74, ticket(143,114,.65), 'A primeira entrada na sala de prova.'],
  ['simulado-5','Clipboard',67,53,70, group('translate(48 95) scale(.39)',p('M8-15H-8V-23H8Z',GOLD)+p('M0-15A26 26 0 1 1-.1-15Z',PAPER)+line('M0 9V-7M0 9 12 14',INK,3))+pencil(144,109,.5,20), 'Prancheta e cronômetro introduzem o treino com tempo.'],
  ['simulado-20','Timer',63,55,75, ticket(53,113,.6)+seal(146,117,.7)+cap(100,45,.62), 'O tempo já faz parte do treino, com vinte provas completas.'],
  ['simulado-50','House',60,55,80, ticket(57,113,.55)+pencil(145,106,.5,20)+cap(100,47,.8), 'A sala de prova vira casa: casinha com capelo e material de prova.'],
  ['discursiva-1','Doc',63,50,74, pencil(134,99,1.1,29)+line('M77 116H103', '#9563FF',2.6), 'Uma folha e um lápis grande para a primeira redação.'],
  ['discursiva-5','Pencil',60,51,80, p('M65 116Q86 107 106 117T144 115','none','#9563FF',2.4)+ticket(56,108,.55), 'A escrita ganha movimento, páginas e assinatura.'],
  ['discursiva-20','Pena',58,51,84, p('M120 119Q124 106 138 109L143 128H117Z','#5B4BF5')+p('M121 109H139V115H121Z',GOLD)+line('M65 130Q85 123 104 130',INK,2.2)+crown(99,44,.6), 'Uma pena, tinteiro e assinatura para o punho treinado.'],
  ['maratona-120','Timer',63,50,75, leaf(50,116,.55,-20)+star(148,85,.45), 'A primeira sessão longa é uma ampulheta com energia tranquila.'],
  ['maratona-240','Timer',62,53,77, book(54,119,.46)+seal(146,121,.65)+spark('M48 66 40 61M48 77H37'), 'Quatro horas combinam tempo, leitura e confirmação.'],
  ['maratona-360','Timer',64,54,74, mountains+star(147,59,.65)+leaf(51,114,.6,-15), 'Uma ampulheta sobre a montanha de seis horas de estudo.'],
  ['maratona-480','Timer',65,59,72, group('translate(55 65) scale(.48)',p('M0-17A17 17 0 1 1-.1-17Z',GOLD)+line('M0-24V-29M-24 0H-29M17-17 21-21',GOLD,3))+group('translate(148 68) scale(.6)',p('M4-16A18 18 0 1 0 17 12A16 16 0 0 1 4-16Z','#D9CBFF'))+crown(100,46,.65), 'O dia inteiro atravessa sol e lua, sem sugerir estudo sem descanso.'],
];

const source = await fs.readFile(path.join(ROOT,'shared/src/commonMain/kotlin/br/com/estudario/domain/BadgeCatalog.kt'),'utf8');
const catalog = [];
for (const match of source.matchAll(/BadgeCategory\.(\w+),\s*"([^"]+)",\s*"([^"]+)",\s*listOf\(([\s\S]*?)\n\s*\),/g)) {
  let tier = 0;
  for (const row of match[4].matchAll(/Triple\(([\d_]+),\s*"([^"]+)",\s*"([^"]+)"\)/g)) {
    const target = Number(row[1].replaceAll('_',''));
    catalog.push({id:`${match[2]}-${target}`,category:match[2],tier:++tier,name:row[2],requirement:row[3],target,unit:match[3]});
  }
}
if (catalog.length !== 50 || ARTS.length !== 50) throw new Error(`Catálogo ou artes incompletos: ${catalog.length}/${ARTS.length}`);
if (new Set(ARTS.map(a=>a[0])).size !== ARTS.length) throw new Error('IDs de arte duplicados');
for (const b of catalog) if (!ARTS.find(a=>a[0]===b.id)) throw new Error(`Falta arte: ${b.id}`);
for (const a of ARTS) if (!catalog.find(b=>b.id===a[0]) || !turma[a[1]]) throw new Error(`Arte sem catálogo ou personagem: ${a[0]} / ${a[1]}`);

function prims(list) {
  return list.map(a => p(a.d,a.f || 'none',a.s || 'none',a.w || 0,`${a.o != null?`opacity="${a.o}"`:''}${a.dy ? ` transform="translate(0 ${a.dy})"`:''}`)).join('');
}
function character(name,x,y,w,small=false) {
  const art = turma[name];
  return group(`translate(${x} ${y}) scale(${w/100})`,prims(art.body)+prims(small?art.faceSmall:art.face));
}
function frame(b) {
  const m = META[b.category], [label,face,edge,light] = METALS[b.tier];
  const d = m.shape;
  const rim = group('translate(100 89) scale(.89) translate(-100 -89)',p(d,PAPER,INK,1.8));
  const field = group('translate(100 89) scale(.79) translate(-100 -89)',p(d,m.soft,'none',0));
  let behind = '';
  if (b.tier >= 6) behind += wings;
  if (b.tier >= 3) behind += laurel;
  if (b.tier === 7) behind += p('M57 26Q100 1 143 26','none',GOLD,2.3)+star(52,27,.48)+star(148,27,.48)+dot(100,9,2.1,GOLD);
  const body = group('translate(0 5)',p(d,edge,INK,2.6))+p(d,face,INK,2.6)+rim+field+
    line('M69 43Q81 36 94 34',light,3)+line('M150 100Q148 115 140 123',edge,2.2);
  const gem = b.tier >= 4 ? p('M100 20 109 28 100 36 91 28Z',light,INK,1.8)+p('M100 20V36L109 28Z',face,INK,.8) : '';
  return behind+body+gem;
}
function ribbon(b) {
  const [,face,edge,light] = METALS[b.tier];
  const roman = ['','I','II','III','IV','V','VI','VII'][b.tier];
  return p('M61 137 36 134 42 146 36 159 66 157Z',edge)+p('M139 137 164 134 158 146 164 159 134 157Z',edge)+
    p('M61 137 57 153 68 163V143Z',edge)+p('M139 137 143 153 132 163V143Z',edge)+
    p('M58 135Q100 143 142 135L140 157Q100 167 60 157Z',face,INK,2.2)+
    line('M67 141Q100 146 133 141',light,1.5)+
    `<text x="100" y="156" text-anchor="middle" font-family="Georgia,serif" font-size="13" font-weight="bold" fill="${INK}" letter-spacing="2">${roman}</text>`+
    dot(74,151,1.8,light)+dot(126,151,1.8,light);
}
function render(b,locked=false) {
  const a = ARTS.find(a=>a[0]===b.id), m = META[b.category];
  // Os cenários de montanha ficam atrás do personagem. Os outros adereços vão à frente.
  const bg = a[5].includes(mountains) ? a[5].replace(mountains,'') : null;
  const art = frame(b)+(bg !== null ? mountains : '')+character(a[1],a[2],a[3],a[4])+(bg !== null ? bg : a[5])+ribbon(b);
  const lock = group('translate(143 124)',p('M-6 0V-5A6 6 0 0 1 6-5V0','none',INK,3)+p('M-9 0H9V15H-9Z',PAPER)+dot(0,6,2,INK)+line('M0 7V10',INK,2));
  const body = locked ? `<g filter="url(#muted)">${art}</g>${lock}` : art;
  return `<svg xmlns="http://www.w3.org/2000/svg" width="512" height="512" viewBox="0 0 200 200" role="img" aria-labelledby="title desc"><title id="title">${esc(b.name)}${locked?' — a conquistar':''}</title><desc id="desc">${esc(a[6])} ${esc(b.requirement)}. ${METALS[b.tier][0]}, nível ${b.tier}.</desc>${locked?'<defs><filter id="muted" color-interpolation-filters="sRGB"><feColorMatrix type="saturate" values="0"/><feComponentTransfer><feFuncR type="linear" slope=".65" intercept=".21"/><feFuncG type="linear" slope=".65" intercept=".21"/><feFuncB type="linear" slope=".72" intercept=".22"/></feComponentTransfer></filter></defs>':''}<g stroke-linejoin="round">${body}</g></svg>`;
}

const require = createRequire(import.meta.url);
const sharp = require('C:/Users/otavi/.cache/codex-runtimes/codex-primary-runtime/dependencies/node/node_modules/sharp');
await fs.mkdir(path.join(OUT,'svg'),{recursive:true});
await fs.mkdir(path.join(OUT,'png'),{recursive:true});
await fs.mkdir(path.join(OUT,'bloqueados/svg'),{recursive:true});
await fs.mkdir(path.join(OUT,'bloqueados/png'),{recursive:true});

const manifest = [];
const allSVG = new Map();
for (const b of catalog) {
  const a = ARTS.find(a=>a[0]===b.id);
  const svg = render(b), muted = render(b,true);
  allSVG.set(b.id,svg);
  await fs.writeFile(path.join(OUT,'svg',b.id+'.svg'),svg);
  await fs.writeFile(path.join(OUT,'bloqueados/svg',b.id+'.svg'),muted);
  await sharp(Buffer.from(svg)).png().toFile(path.join(OUT,'png',b.id+'.png'));
  await sharp(Buffer.from(muted)).png().toFile(path.join(OUT,'bloqueados/png',b.id+'.png'));
  manifest.push({...b,categoryLabel:META[b.category].label,tierLabel:METALS[b.tier][0],artDirection:a[6],svg:`svg/${b.id}.svg`,png:`png/${b.id}.png`,lockedSvg:`bloqueados/svg/${b.id}.svg`,lockedPng:`bloqueados/png/${b.id}.png`,androidResource:`emblema_${b.id.replaceAll('-','_')}`,width:512,height:512,transparent:true});
}
await fs.writeFile(path.join(OUT,'manifesto.json'),JSON.stringify({collection:'Caderno de conquistas',date:'2026-10-23',count:50,source:'design/turma/turma.js + composições vetoriais colecao.mjs',badges:manifest},null,2)+'\n');

// Prancha de inspeção: todos os 50, na ordem do catálogo, com nome e faixa.
let sheet = `<svg xmlns="http://www.w3.org/2000/svg" width="1500" height="${180+11*260}" viewBox="0 0 1500 ${180+11*260}"><rect width="1500" height="100%" fill="#F7F3E9"/><text x="60" y="68" font-family="Trebuchet MS,Arial" font-size="40" font-weight="bold" fill="${INK}">estudário · caderno de conquistas</text><text x="60" y="108" font-family="Trebuchet MS,Arial" font-size="20" fill="#68627D">50 emblemas · 11 famílias · a turma do Folha</text>`;
let row=0;
for (const [cat,meta] of Object.entries(META)) {
  const y=180+row*260;
  sheet+=`<text x="45" y="${y+12}" font-family="Trebuchet MS,Arial" font-size="20" font-weight="bold" fill="${INK}">${esc(meta.label)}</text><path d="M45 ${y+27}H1455" stroke="#D9D0BD"/>`;
  const entries=catalog.filter(b=>b.category===cat);
  entries.forEach((b,i)=>{
    const x=50+i*201;
    sheet+=`<svg x="${x}" y="${y+25}" width="184" height="184" viewBox="0 0 200 200">${allSVG.get(b.id).replace(/^<svg[^>]*>/,'').replace(/<\/svg>$/,'')}</svg><text x="${x+92}" y="${y+216}" text-anchor="middle" font-family="Trebuchet MS,Arial" font-size="14" font-weight="bold" fill="${INK}">${esc(b.name)}</text><text x="${x+92}" y="${y+238}" text-anchor="middle" font-family="Trebuchet MS,Arial" font-size="12" fill="#68627D">${METALS[b.tier][0]} · ${b.id}</text>`;
  });
  row++;
}
sheet+='</svg>';
await fs.writeFile(path.join(OUT,'colecao-completa.svg'),sheet);
await sharp(Buffer.from(sheet)).png().toFile(path.join(OUT,'colecao-completa.png'));

// Prancha de apresentação: uma seleção de peças finais no claro e no escuro.
const featured=['seq-365','plano-1000','quest-10000','topico-400','revisao-1000','discursiva-20'];
let cover=`<svg xmlns="http://www.w3.org/2000/svg" width="1440" height="1010" viewBox="0 0 1440 1010"><rect width="1440" height="1010" fill="#F7F3E9"/><rect y="570" width="1440" height="440" fill="#110F26"/><path d="M57 106H460" stroke="#F5B83D" stroke-width="14"/><text x="55" y="103" font-family="Trebuchet MS,Arial" font-size="54" font-weight="bold" fill="${INK}">Feitos para conquistar.</text><text x="58" y="151" font-family="Trebuchet MS,Arial" font-size="22" fill="#68627D">estudário / caderno de conquistas / 50 emblemas</text>`;
featured.forEach((id,i)=>{
  const b=catalog.find(b=>b.id===id), x=68+(i%3)*465, y=i<3?190:596;
  cover+=`<svg x="${x+77}" y="${y}" width="280" height="280" viewBox="0 0 200 200">${allSVG.get(id).replace(/^<svg[^>]*>/,'').replace(/<\/svg>$/,'')}</svg><text x="${x+217}" y="${y+300}" text-anchor="middle" font-family="Trebuchet MS,Arial" font-size="24" font-weight="bold" fill="${i<3?INK:PAPER}">${esc(b.name)}</text><text x="${x+217}" y="${y+333}" text-anchor="middle" font-family="Trebuchet MS,Arial" font-size="17" fill="${i<3?'#68627D':'#B9B3DC'}">${esc(META[b.category].label)} · ${METALS[b.tier][0]}</text>`;
});
cover+='</svg>';
await fs.writeFile(path.join(OUT,'apresentacao.svg'),cover);
await sharp(Buffer.from(cover)).png().toFile(path.join(OUT,'apresentacao.png'));

const template=await fs.readFile(path.join(OUT,'galeria.template.html'),'utf8');
const payload=JSON.stringify(manifest.map(b=>({...b,art:allSVG.get(b.id),lockedArt:render(b,true)}))).replaceAll('<','\\u003c');
await fs.writeFile(path.join(OUT,'galeria.html'),template.replace('/*__DATA__*/',payload));

const report=[];
for (const b of manifest) {
  for (const f of [b.png,b.lockedPng]) {
    const im=sharp(path.join(OUT,f)), meta=await im.metadata(), stats=await im.stats();
    if(meta.width!==512 || meta.height!==512 || !meta.hasAlpha || stats.isOpaque) throw new Error(`PNG inválido: ${f}`);
  }
  report.push(`${b.id}: SVG, PNG e variantes bloqueadas presentes`);
}
await fs.writeFile(path.join(OUT,'verificacao.txt'),`Catálogo: ${catalog.length}\nArtes: ${ARTS.length}\nCategorias: ${Object.keys(META).length}\nPNG: 100 arquivos, 512×512, canal alfa e transparência confirmados\nNível anual: ${manifest.find(b=>b.id==='seq-365').tier}\n\n${report.join('\n')}\n`);
console.log(`Coleção exportada e conferida: ${manifest.length} emblemas, 100 SVG, 100 PNG.\n${OUT}`);
