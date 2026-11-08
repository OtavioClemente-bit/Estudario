import fs from 'node:fs/promises';
import path from 'node:path';
import crypto from 'node:crypto';
import {createRequire} from 'node:module';
import {fileURLToPath} from 'node:url';
const require=createRequire(import.meta.url);
const libs='C:/Users/otavi/.cache/codex-runtimes/codex-primary-runtime/dependencies/node/node_modules/';
const sharp=require(libs+'sharp'),{createCanvas,loadImage,GlobalFonts}=require(libs+'@napi-rs/canvas');
const work=path.dirname(fileURLToPath(import.meta.url)),repo=path.resolve(work,'../../..');
const out=(...p)=>path.join(work,...p),hash=b=>crypto.createHash('sha256').update(b).digest('hex');
for(const dir of ['play-store','android/res/drawable-nodpi','android/res/mipmap-anydpi-v26','previas','editaveis'])await fs.mkdir(out(dir),{recursive:true});
GlobalFonts.registerFromPath(path.join(repo,'app/src/main/res/font/nunito_900.ttf'),'NunitoBlack');
GlobalFonts.registerFromPath(path.join(repo,'app/src/main/res/font/nunito_700.ttf'),'NunitoBold');
GlobalFonts.registerFromPath(path.join(repo,'app/src/main/res/font/nunito_600.ttf'),'NunitoSemi');
const C={ink:'#21194F',night:'#19133F',purple:'#5D46D9',mint:'#8BE8C2',cream:'#FFF5E1',amber:'#F7C552',muted:'#C6BEE6'};
const paint=await loadImage(out('originais/universo-editorial.png')),hero=await loadImage(out('originais/capa-com-folha.png')),folha=await loadImage(out('originais/folha-icone.png'));
const font=(ctx,size,family='NunitoBlack')=>{ctx.font=size+'px '+family;ctx.textBaseline='alphabetic';};
function text(ctx,t,x,y,size,color=C.cream,family='NunitoBlack',maxWidth){font(ctx,size,family);ctx.fillStyle=color;if(maxWidth&&ctx.measureText(t).width>maxWidth)throw Error('Texto excede a coluna: '+t);ctx.fillText(t,x,y);}
function rr(ctx,x,y,w,h,r,fill,stroke,line=1){ctx.beginPath();ctx.roundRect(x,y,w,h,r);if(fill){ctx.fillStyle=fill;ctx.fill();}if(stroke){ctx.lineWidth=line;ctx.strokeStyle=stroke;ctx.stroke();}}
function brand(ctx,x,y,size,dark=false){
 const a='estud',b='á',c='rio';font(ctx,size);const wa=ctx.measureText(a).width,wb=ctx.measureText(b).width,wc=ctx.measureText(c).width;
 ctx.fillStyle=dark?'#F2CD70':'#D9A744';ctx.globalAlpha=.42;rr(ctx,x-2,y-size*.19,wa+wb+wc+8,size*.25,size*.04,ctx.fillStyle);ctx.globalAlpha=1;
 text(ctx,a,x,y,size,dark?C.ink:C.cream);text(ctx,b,x+wa,y,size,C.amber);text(ctx,c,x+wa+wb,y,size,dark?C.ink:C.cream);
}
const assetRecords=[];
async function save(canvas,file,options={}){
 let pipeline=sharp(canvas.toBuffer('image/png'));if(file.startsWith('play-store/'))pipeline=pipeline.removeAlpha();
 const data=await pipeline.png({compressionLevel:9}).toBuffer();
 await fs.writeFile(out(file),data);const m=await sharp(data).metadata();
 assetRecords.push({file,width:m.width,height:m.height,bytes:data.length,sha256:hash(data),...options});
 return data;
}
function gradientBg(ctx,w,h){
 const g=ctx.createLinearGradient(0,0,w,h);g.addColorStop(0,'#5134C1');g.addColorStop(.5,'#352183');g.addColorStop(1,'#1E164C');ctx.fillStyle=g;ctx.fillRect(0,0,w,h);
 ctx.save();ctx.globalAlpha=.29;ctx.drawImage(paint,0,0,paint.width*.49,paint.height*.85,0,0,w,h);ctx.restore();
 const glow=ctx.createRadialGradient(w*.3,h*.25,1,w*.3,h*.25,w*.7);glow.addColorStop(0,'#8C78F04A');glow.addColorStop(1,'#8C78F000');ctx.fillStyle=glow;ctx.fillRect(0,0,w,h);
 ctx.beginPath();ctx.moveTo(-w*.15,h*.88);ctx.bezierCurveTo(w*.22,h*.62,w*.56,h*1.18,w*1.2,h*.73);ctx.lineTo(w*1.2,h*1.05);ctx.lineTo(-w*.1,h*1.15);ctx.closePath();
 const mint=ctx.createLinearGradient(0,h*.7,w,h);mint.addColorStop(0,'#8BE8C2');mint.addColorStop(.45,'#459DA7');mint.addColorStop(1,'#594BA4');ctx.fillStyle=mint;ctx.fill();
}
// A nova pintura é encaixada num círculo seguro antes de exportar as camadas adaptativas.
const fgRaw=await sharp(out('originais/folha-icone.png')).ensureAlpha().raw().toBuffer({resolveWithObject:true});
const {width:fw,height:fh}=fgRaw.info;let xmin=fw,ymin=fh,xmax=0,ymax=0;
for(let y=0;y<fh;y++)for(let x=0;x<fw;x++)if(fgRaw.data[(y*fw+x)*4+3]>8){xmin=Math.min(xmin,x);xmax=Math.max(xmax,x);ymin=Math.min(ymin,y);ymax=Math.max(ymax,y);}
const cx=(xmin+xmax)/2,cy=(ymin+ymax)/2;let rmax=0;
for(let y=ymin;y<=ymax;y++)for(let x=xmin;x<=xmax;x++)if(fgRaw.data[(y*fw+x)*4+3]>8)rmax=Math.max(rmax,Math.hypot(x-cx,y-cy));
const scale=319/rmax,tx=540-cx*scale,ty=540-cy*scale;
const foreground=createCanvas(1080,1080),fc=foreground.getContext('2d');fc.drawImage(folha,tx,ty,fw*scale,fh*scale);
const background=createCanvas(1080,1080),bc=background.getContext('2d');gradientBg(bc,1080,1080);
const fg1080=await save(foreground,'editaveis/icone-primeiro-plano-1080.png',{role:'foreground adaptativo, transparência'});
const bg1080=await save(background,'editaveis/icone-fundo-1080.png',{role:'fundo opaco adaptativo'});
await sharp(fg1080).resize(432,432).png().toFile(out('android/res/drawable-nodpi/ic_launcher_folha_fg.png'));
await sharp(bg1080).resize(432,432).png().toFile(out('android/res/drawable-nodpi/ic_launcher_folha_bg.png'));
const composite=createCanvas(1080,1080),cc=composite.getContext('2d');cc.drawImage(background,0,0);cc.drawImage(foreground,0,0);
await save(composite,'editaveis/icone-completo-1080.png',{role:'master com margem adaptativa'});
const staticIcon=await sharp(composite.toBuffer('image/png')).extract({left:180,top:180,width:720,height:720}).resize(512,512).ensureAlpha().png({compressionLevel:9}).toBuffer();
await fs.writeFile(out('play-store/icone-512.png'),staticIcon);
assetRecords.push({file:'play-store/icone-512.png',width:512,height:512,bytes:staticIcon.length,sha256:hash(staticIcon),role:'ícone da listagem, fundo opaco, sem cantos ou sombra de contorno'});
// Variante temática: silhueta pintada com recortes dos olhos e da boca.
const monoData=Buffer.alloc(fw*fh*4);
const holes=[{x:.36*fw,y:.51*fh,rx:.08*fw,ry:.09*fh},{x:.63*fw,y:.56*fh,rx:.085*fw,ry:.095*fh},{x:.48*fw,y:.615*fh,rx:.10*fw,ry:.065*fh}];
for(let y=0;y<fh;y++)for(let x=0;x<fw;x++){
 const p=(y*fw+x)*4,d=fgRaw.data,a=d[p+3],luma=.2126*d[p]+.7152*d[p+1]+.0722*d[p+2];
 const inFaceHole=holes.some(h=>((x-h.x)/h.rx)**2+((y-h.y)/h.ry)**2<1);
 monoData[p]=monoData[p+1]=monoData[p+2]=255;monoData[p+3]=inFaceHole&&luma<150?0:a;
}
const smoothAlpha=await sharp(monoData,{raw:{width:fw,height:fh,channels:4}}).extractChannel(3).blur(2.2).threshold(128).raw().toBuffer();
for(let p=0;p<fw*fh;p++)monoData[p*4+3]=smoothAlpha[p];
const monoSource=await sharp(monoData,{raw:{width:fw,height:fh,channels:4}}).png().toBuffer(),mi=await loadImage(monoSource);
const mono=createCanvas(1080,1080),mc=mono.getContext('2d');mc.drawImage(mi,tx,ty,fw*scale,fh*scale);
await save(mono,'editaveis/icone-monocromatico-1080.png',{role:'camada temática com os olhos e a boca vazados'});
await sharp(mono.toBuffer('image/png')).resize(432,432).png().toFile(out('android/res/drawable-nodpi/ic_launcher_folha_mono.png'));
const adaptive='<?xml version="1.0" encoding="utf-8"?>\n<adaptive-icon xmlns:android="http://schemas.android.com/apk/res/android">\n    <background android:drawable="@drawable/ic_launcher_folha_bg" />\n    <foreground android:drawable="@drawable/ic_launcher_folha_fg" />\n    <monochrome android:drawable="@drawable/ic_launcher_folha_mono" />\n</adaptive-icon>\n';
for(const name of ['ic_launcher.xml','ic_launcher_round.xml'])await fs.writeFile(out('android/res/mipmap-anydpi-v26',name),adaptive);
// Capa: arte panorâmica nova e tipografia separada, sem rasterizar texto no gerador.
const cover=createCanvas(2048,1000),cv=cover.getContext('2d');cv.drawImage(hero,0,0,2048,1000);
const veil=cv.createLinearGradient(0,0,1150,0);veil.addColorStop(0,'#1E134A88');veil.addColorStop(.75,'#1E134A22');veil.addColorStop(1,'#1E134A00');cv.fillStyle=veil;cv.fillRect(0,0,1150,1000);
brand(cv,138,202,64);
text(cv,'Seu próximo',132,374,128,C.cream,'NunitoBlack',900);
text(cv,'capítulo',132,510,140,C.cream,'NunitoBlack',900);
text(cv,'começa hoje.',132,646,128,C.mint,'NunitoBlack',960);
text(cv,'Planeje. Estude. Evolua.',139,758,43,'#E4DCF6','NunitoSemi',900);
const coverMaster=await save(cover,'editaveis/capa-2048x1000.png',{role:'capa com tipografia em tamanho de trabalho'});
const feature=await sharp(coverMaster).resize(1024,500,{fit:'contain',background:C.ink}).removeAlpha().png({compressionLevel:9}).toBuffer();
await fs.writeFile(out('play-store/capa-1024x500.png'),feature);assetRecords.push({file:'play-store/capa-1024x500.png',width:1024,height:500,bytes:feature.length,sha256:hash(feature),role:'feature graphic RGB, sem transparência'});
const panels=[
 {id:'01-seu-estudo',source:'01-inicio',dark:true,title:['Seu estudo.','Um novo capítulo.'],sub:['Seu objetivo ganha direção,','um dia de cada vez.'],size:96,x:306,art:true,alt:'Início do Estudário com progresso do edital, próxima atividade e missão do dia.'},
 {id:'02-seu-edital',source:'02-concursos',dark:false,title:['Seu edital.','Tudo no lugar.'],sub:['Matérias e tópicos organizados','para saber por onde começar.'],size:105,x:142,side:['Matérias.','Tópicos.','Progresso.'],alt:'Matérias e tópicos de um concurso organizados no Estudário, com progresso e materiais.'},
 {id:'03-seu-plano',source:'03-plano',dark:true,title:['Um plano','para a sua vida.'],sub:['Teoria, questões e revisões','no tempo que você tem.'],size:104,x:215,alt:'Plano de estudos do Estudário com atividades, missão diária e calendário semanal.'},
 {id:'04-seu-material',source:'04-topico',dark:false,title:['Estudar é','entender.'],sub:['Teoria, dicas e questões','reunidas no mesmo tópico.'],size:116,x:292,side:['Leia.','Conecte.','Aprenda.'],alt:'Tópico de HTML5 no Estudário com teoria em capítulos, revisões, dicas e questões.'},
 {id:'05-seu-treino',source:'05-questoes',dark:true,title:['Treine com','mais direção.'],sub:['Questões por tópico e histórico','para orientar a próxima tentativa.'],size:108,x:142,side:['Pratique.','Entenda.','Avance.'],alt:'Área de questões de um tópico no Estudário, com histórico de acertos e opção de treino.'},
 {id:'06-sua-revisao',source:'06-revisoes',dark:false,title:['O que você','aprende, fica.'],sub:['Revisão espaçada para voltar','ao conteúdo no momento certo.'],size:107,x:215,art:true,alt:'Revisões espaçadas do Estudário com etapas de retorno ao conteúdo e revisões pendentes.'}
];
const compositionProof=[];
for(let i=0;i<panels.length;i++){
 const p=panels[i],c=createCanvas(1080,1920),ctx=c.getContext('2d'),fg=p.dark?C.cream:C.ink,muted=p.dark?'#CCC4E7':'#625A79';
 const bg=ctx.createLinearGradient(0,0,1080,1920);bg.addColorStop(0,p.dark?'#201548':'#FFF7E9');bg.addColorStop(1,p.dark?'#352577':'#E6EEE4');ctx.fillStyle=bg;ctx.fillRect(0,0,1080,1920);
 const decor=createCanvas(1080,1920),dc=decor.getContext('2d');dc.drawImage(paint,-270,1140,1600,1600*paint.height/paint.width);
 const fade=dc.createLinearGradient(0,1138,0,1590);fade.addColorStop(0,'#FFFFFF00');fade.addColorStop(1,'#FFFFFFFF');dc.globalCompositeOperation='destination-in';dc.fillStyle=fade;dc.fillRect(0,0,1080,1920);
 ctx.save();ctx.globalAlpha=p.dark?.28:.11;ctx.drawImage(decor,0,0);ctx.restore();
 const aura=ctx.createRadialGradient(570,1200,40,570,1200,800);aura.addColorStop(0,p.dark?'#7353C744':'#86D6B944');aura.addColorStop(1,'#86D6B900');ctx.fillStyle=aura;ctx.fillRect(0,500,1080,1420);
 brand(ctx,78,112,40,!p.dark);text(ctx,String(i+1).padStart(2,'0')+' / 06',900,109,25,p.dark?'#BDB2E3':'#827A94','NunitoBold',130);
 for(let j=0;j<2;j++)text(ctx,p.title[j],74,251+j*118,p.size,j===1?(p.dark?C.mint:'#4B35AC'):fg,'NunitoBlack',930);
 for(let j=0;j<p.sub.length;j++)text(ctx,p.sub[j],80,436+j*42,35,muted,'NunitoSemi',920);
 const shotPath=out('referencias/capturas-atuais',p.source+'.png'),shotBytes=await fs.readFile(shotPath),m=await sharp(shotBytes).metadata();
 // Somente barras de status e navegação do Android são retiradas. Nenhum componente do Estudário é recortado ou refeito.
 const crop={left:0,top:112,width:m.width,height:2488};
 const clean=await sharp(shotBytes).extract(crop).png().toBuffer(),screen=await loadImage(clean),w=650,h=w*crop.height/crop.width,x=p.x,y=515,pad=9;
 if(x-pad<36||x+w+pad>1044||y-pad<495||y+h+pad>1855)throw Error('Tela fora da margem '+p.id);
 ctx.save();ctx.shadowColor=p.dark?'#09061EA6':'#33265E45';ctx.shadowBlur=62;ctx.shadowOffsetY=25;rr(ctx,x-pad,y-pad,w+pad*2,h+pad*2,45,p.dark?'#8C7CCC':'#9E95BF');ctx.restore();
 rr(ctx,x-pad,y-pad,w+pad*2,h+pad*2,45,p.dark?'#8C7CCC':'#9E95BF');
 ctx.save();ctx.beginPath();ctx.roundRect(x,y,w,h,36);ctx.clip();ctx.drawImage(screen,x,y,w,h);ctx.restore();
 if(p.art){const aw=p.x>260?225:175,ax=p.x>260?44:16,ay=1392;ctx.drawImage(folha,ax,ay,aw,aw*folha.height/folha.width);}
 if(p.side){const sx=p.x<200?835:58,sy=1120;for(let j=0;j<p.side.length;j++){rr(ctx,sx,sy+j*67-28,5,35,2,p.dark?C.mint:'#7761BA');text(ctx,p.side[j],sx+18,sy+j*67,27,p.dark?'#E0D8F0':'#74658B','NunitoBold',205);}}
 text(ctx,'Um passo de cada vez.',80,1885,27,muted,'NunitoSemi',800);
 await save(c,'play-store/'+p.id+'.png',{role:'captura promocional independente',source:'referencias/capturas-atuais/'+p.source+'.png',alt:p.alt});
 await fs.writeFile(out('previas',p.id+'-360.png'),await sharp(c.toBuffer('image/png')).resize(360,640).png().toBuffer());
 compositionProof.push({id:p.id,source:p.source,sourceSha256:hash(shotBytes),crop,display:{x,y,width:w,height:h},wholeAppUiContained:true});
}
// Prévia do ícone sob máscaras reais e em tamanhos de uso.
const iconImage=await loadImage(staticIcon);
const iconQA=createCanvas(1600,900),iq=iconQA.getContext('2d');iq.fillStyle='#F1EBDF';iq.fillRect(0,0,1600,900);
text(iq,'Estudário · um ícone para acompanhar todo dia',60,75,42,C.ink);
for(let i=0;i<3;i++){const x=60+i*500,y=135,w=420;iq.save();iq.beginPath();if(i===0)iq.rect(x,y,w,w);else if(i===1)iq.arc(x+w/2,y+w/2,w/2,0,Math.PI*2);else iq.roundRect(x,y,w,w,w*.24);iq.clip();iq.drawImage(iconImage,x,y,w,w);iq.restore();text(iq,['Play Store · quadrado','Android · circular','Android · arredondado'][i],x,y+w+46,28,C.ink,'NunitoBold');}
for(let i=0;i<3;i++){const size=[96,64,48][i],x=110+i*200,y=710;iq.save();iq.beginPath();iq.roundRect(x,y,size,size,size*.24);iq.clip();iq.drawImage(iconImage,x,y,size,size);iq.restore();text(iq,size+' px',x,y+size+36,24,C.ink,'NunitoBold');}
const monoPreview=await loadImage(mono.toBuffer('image/png'));iq.fillStyle='#D7F3DE';rr(iq,1040,650,180,180,45,'#D7F3DE');iq.save();iq.globalCompositeOperation='source-over';
const mt=createCanvas(720,720),mtc=mt.getContext('2d');mtc.drawImage(monoPreview,180,180,720,720,0,0,720,720);mtc.globalCompositeOperation='source-in';mtc.fillStyle='#274437';mtc.fillRect(0,0,720,720);iq.drawImage(mt,1040,650,180,180);iq.restore();text(iq,'Ícone temático',1250,744,27,C.ink,'NunitoBold');
await save(iconQA,'previas/icone-mascaras-e-tamanhos.png',{role:'comparação de máscaras e leitura em tamanhos pequenos'});
const board=createCanvas(2220,1740),bd=board.getContext('2d');bd.fillStyle='#12102A';bd.fillRect(0,0,2220,1740);text(bd,'Estudário · nova apresentação',64,85,50,C.cream);
bd.drawImage(await loadImage(feature),64,126,1536,750);bd.save();bd.beginPath();bd.roundRect(1680,238,420,420,100);bd.clip();bd.drawImage(iconImage,1680,238,420,420);bd.restore();text(bd,'O Folha, todo dia.',1700,720,30,C.mint);
for(let i=0;i<panels.length;i++){const p=panels[i],img=await loadImage(out('play-store',p.id+'.png'));bd.drawImage(img,64+i*350,956,326,580);}
text(bd,'Capa horizontal · ícone · seis peças independentes com telas atuais',64,1645,32,'#C9C1DE','NunitoSemi');
await save(board,'previas/prancha-geral.png',{role:'visão geral da entrega'});
await fs.writeFile(out('composicao-verificacao.json'),JSON.stringify({date:new Date().toISOString(),adaptive:{canvas:1080,safeZoneDiameter:660,actualRadiusLimit:319,sourceCenter:{cx,cy},scale,translate:{x:tx,y:ty}},screenshots:compositionProof,provider:'ImageGen integrado; diagramação e exportação em camadas'},null,2)+'\n');
for(const file of ['android/res/drawable-nodpi/ic_launcher_folha_fg.png','android/res/drawable-nodpi/ic_launcher_folha_bg.png','android/res/drawable-nodpi/ic_launcher_folha_mono.png','android/res/mipmap-anydpi-v26/ic_launcher.xml','android/res/mipmap-anydpi-v26/ic_launcher_round.xml','originais/folha-icone.png','originais/universo-editorial.png','originais/capa-com-folha.png']){
 const bytes=await fs.readFile(out(file)),m=file.endsWith('.png')?await sharp(bytes).metadata():{};
 assetRecords.push({file,width:m.width,height:m.height,hasAlpha:m.hasAlpha,bytes:bytes.length,sha256:hash(bytes),role:file.startsWith('android/')?'recurso pronto para integração Android':'pintura original do ImageGen'});
}
await fs.writeFile(out('manifesto.json'),JSON.stringify({date:new Date().toISOString(),brand:'Estudário',appVersionCaptured:'3.3.116',appVersionCode:144,scope:'Arquivos novos em conteudo/entrada; recursos do app e da listagem existentes não foram substituídos.',assets:assetRecords},null,2)+'\n');
console.log(JSON.stringify({assets:assetRecords.length,icon:512,feature:[1024,500],screenshots:panels.length,adaptiveLayers:[432,432],safeRadius:319}));

