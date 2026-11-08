// Acabamento autorizado: transforma recortes da pintura existente; sem gerar desenhos substitutos.
import fs from 'node:fs/promises';
import path from 'node:path';
import {fileURLToPath} from 'node:url';
import {createRequire} from 'node:module';
const require=createRequire(import.meta.url),sharp=require('C:/Users/otavi/.cache/codex-runtimes/codex-primary-runtime/dependencies/node/node_modules/sharp');
const here=path.dirname(fileURLToPath(import.meta.url)),root=path.resolve(here,'..');
const doc=JSON.parse(await fs.readFile(path.join(here,'camadas.json'),'utf8')),wanted=process.argv.slice(2);
await fs.mkdir(path.join(here,'png'),{recursive:true});await fs.mkdir(path.join(here,'originais'),{recursive:true});
const interp=(v,knots)=>{if(v<=knots[0][0])return knots[0][1];for(let i=1;i<knots.length;i++)if(v<=knots[i][0]){const a=knots[i-1],b=knots[i];return a[1]+(b[1]-a[1])*(v-a[0])/(b[0]-a[0]);}return knots.at(-1)[1];};
const curve=points=>{
 const a=[];let length=0;
 for(let i=0;i<points.length-1;i++)for(let k=0;k<24;k++){
  const t=k/24,p0=points[Math.max(0,i-1)],p1=points[i],p2=points[i+1],p3=points[Math.min(points.length-1,i+2)];
  const p=[0,1,2].map(c=>.5*((2*p1[c])+(-p0[c]+p2[c])*t+(2*p0[c]-5*p1[c]+4*p2[c]-p3[c])*t*t+(-p0[c]+3*p1[c]-3*p2[c]+p3[c])*t*t*t));
  if(a.length)length+=Math.hypot(p[0]-a.at(-1).x,p[1]-a.at(-1).y);a.push({x:p[0],y:p[1],r:p[2],length});
 }
 const p=points.at(-1);length+=Math.hypot(p[0]-a.at(-1).x,p[1]-a.at(-1).y);a.push({x:p[0],y:p[1],r:p[2],length});
 for(let i=0;i<a.length;i++){const l=a[Math.max(0,i-1)],r=a[Math.min(a.length-1,i+1)],d=Math.hypot(r.x-l.x,r.y-l.y);a[i].nx=-(r.y-l.y)/d;a[i].ny=(r.x-l.x)/d;a[i].t=a[i].length/length;}return a;
};
const homography=(from,to)=>{
 const a=[];
 for(let i=0;i<4;i++){const [x,y]=from[i],[u,v]=to[i];a.push([x,y,1,0,0,0,-u*x,-u*y,u],[0,0,0,x,y,1,-v*x,-v*y,v]);}
 for(let i=0;i<8;i++){let p=i;for(let r=i+1;r<8;r++)if(Math.abs(a[r][i])>Math.abs(a[p][i]))p=r;[a[i],a[p]]=[a[p],a[i]];const d=a[i][i];if(Math.abs(d)<1e-10)throw Error('Perspectiva degenerada');for(let c=i;c<9;c++)a[i][c]/=d;for(let r=0;r<8;r++)if(r!==i){const f=a[r][i];for(let c=i;c<9;c++)a[r][c]-=f*a[i][c];}}
 return a.map(r=>r[8]);
};
for(const job of doc.jobs.filter(j=>!wanted.length||wanted.includes(j.id))){
 const input=path.join(here,'originais',job.id+'.png');
 try{await fs.access(input);}catch{await fs.copyFile(path.join(root,'producao-final/png',job.id+'.png'),input);}
 const original=await fs.readFile(input),{data:src,info}=await sharp(original).ensureAlpha().raw().toBuffer({resolveWithObject:true}),w=info.width,h=info.height;
 const layers=[],baseSrc=src;
 for(const s of job.steps){
  let src=baseSrc;
  if(s.sourceFile){const other=await sharp(path.join(root,s.sourceFile)).ensureAlpha().raw().toBuffer({resolveWithObject:true});if(other.info.width!==w||other.info.height!==h)throw Error('Referência de tamanho diferente');src=other.data;}
  const edge=s.expand?' stroke="white" stroke-width="'+(s.expand*2)+'" stroke-linejoin="round"':'';
  const shape=s.maskPath?'<path d="'+s.maskPath+'" fill="white"'+edge+'/>':'<polygon points="'+s.polygon.map(p=>p.join(',')).join(' ')+'" fill="white"'+edge+'/>';
  let mk=sharp(Buffer.from('<svg xmlns="http://www.w3.org/2000/svg" width="'+w+'" height="'+h+'">'+shape+'</svg>'));if(s.feather)mk=mk.blur(s.feather);
  const mask=await mk.ensureAlpha().raw().toBuffer(),layer=Buffer.alloc(w*h*4);
  if(s.blueOnly)for(let q=0;q<mask.length;q+=4)if(src[q+2]<src[q]*1.02||src[q+2]<src[q+1]*1.03)mask[q+3]=0;
  if(s.removeGreen)for(let q=0;q<mask.length;q+=4)if(src[q+1]>src[q]+3)mask[q+3]=0;
  if(s.type==='clone'){
   for(let y=0;y<h;y++)for(let x=0;x<w;x++){
    const q=(y*w+x)*4,a=mask[q+3];if(!a)continue;
    const sy=s.sampleAffine?s.sampleAffine[1]+(y-s.sampleAffine[3])*s.sampleAffine[5]:s.sampleMap?s.sampleMap.sourceTop+(y-s.sampleMap.targetTop)*s.sampleMap.sourceScaleY:y+s.sampleOffset[1];
    const sx=s.sampleAffine?s.sampleAffine[0]+(x-s.sampleAffine[2])*s.sampleAffine[4]:s.sampleMap?x+s.sampleMap.edgeSlope*(y-sy):x+s.sampleOffset[0];
    if(sx<0||sx>=w||sy<0||sy>=h)throw Error('Amostra fora da pintura '+job.id);
    const x0=Math.floor(sx),y0=Math.floor(sy),fx=sx-x0,fy=sy-y0,acc=[0,0,0,0];
    for(let yy=0;yy<2;yy++)for(let xx=0;xx<2;xx++){const z=(Math.min(h-1,y0+yy)*w+Math.min(w-1,x0+xx))*4,weight=(xx?fx:1-fx)*(yy?fy:1-fy);for(let c=0;c<4;c++)acc[c]+=src[z+c]*weight;}
    for(let c=0;c<3;c++)layer[q+c]=Math.round(acc[c]);layer[q+3]=Math.round(a*acc[3]/255);
   }
  }else if(s.type==='quad'){
   const m=homography(s.destQuad,s.srcQuad),minX=Math.max(0,Math.floor(Math.min(...s.destQuad.map(p=>p[0]))-2)),maxX=Math.min(w-1,Math.ceil(Math.max(...s.destQuad.map(p=>p[0]))+2)),minY=Math.max(0,Math.floor(Math.min(...s.destQuad.map(p=>p[1]))-2)),maxY=Math.min(h-1,Math.ceil(Math.max(...s.destQuad.map(p=>p[1]))+2));
   for(let y=minY;y<=maxY;y++)for(let x=minX;x<=maxX;x++){
    const denominator=m[6]*x+m[7]*y+1,sx=(m[0]*x+m[1]*y+m[2])/denominator,sy=(m[3]*x+m[4]*y+m[5])/denominator,x0=Math.floor(sx),y0=Math.floor(sy),fx=sx-x0,fy=sy-y0;if(x0<0||x0+1>=w||y0<0||y0+1>=h)continue;
    const q=(y*w+x)*4,acc=[0,0,0];let alpha=0;
    for(let yy=0;yy<2;yy++)for(let xx=0;xx<2;xx++){const z=((y0+yy)*w+x0+xx)*4,weight=(xx?fx:1-fx)*(yy?fy:1-fy),v=mask[z+3]*src[z+3]/255*weight;alpha+=v;for(let c=0;c<3;c++)acc[c]+=src[z+c]*v;}
    if(alpha<.1)continue;for(let c=0;c<3;c++)layer[q+c]=Math.round(acc[c]/alpha);layer[q+3]=Math.round(alpha);
   }
  }else if(s.type==='ribbon'){
   const from=curve(s.sourceLine),to=curve(s.destLine),minX=Math.max(0,Math.floor(Math.min(...to.map(p=>p.x-p.r-2)))),maxX=Math.min(w-1,Math.ceil(Math.max(...to.map(p=>p.x+p.r+2)))),minY=Math.max(0,Math.floor(Math.min(...to.map(p=>p.y-p.r-2)))),maxY=Math.min(h-1,Math.ceil(Math.max(...to.map(p=>p.y+p.r+2))));
   for(let y=minY;y<=maxY;y++)for(let x=minX;x<=maxX;x++){
    let best=Infinity,at=0;
    for(let i=0;i<to.length;i++){const p=to[i],d=(x-p.x)**2+(y-p.y)**2;if(d<best){best=d;at=i;}}
    const p=to[at],v=((x-p.x)*p.nx+(y-p.y)*p.ny)/p.r;if(Math.abs(v)>1.1||best>(p.r+2)**2)continue;
    const sf=p.t*(from.length-1),si=Math.min(from.length-2,Math.floor(sf)),f=sf-si,a=from[si],b=from[si+1],cx=a.x+(b.x-a.x)*f,cy=a.y+(b.y-a.y)*f,r=a.r+(b.r-a.r)*f,nx=a.nx+(b.nx-a.nx)*f,ny=a.ny+(b.ny-a.ny)*f;
    const sx=cx+nx*v*r,sy=cy+ny*v*r,x0=Math.floor(sx),y0=Math.floor(sy),fx=sx-x0,fy=sy-y0;if(x0<0||x0+1>=w||y0<0||y0+1>=h)continue;
    const q=(y*w+x)*4,acc=[0,0,0];let alpha=0;
    for(let yy=0;yy<2;yy++)for(let xx=0;xx<2;xx++){const z=((y0+yy)*w+x0+xx)*4,weight=(xx?fx:1-fx)*(yy?fy:1-fy),value=mask[z+3]*src[z+3]/255*weight;alpha+=value;for(let c=0;c<3;c++)acc[c]+=src[z+c]*value;}
    if(alpha<.1)continue;for(let c=0;c<3;c++)layer[q+c]=Math.round(acc[c]/alpha);layer[q+3]=Math.round(alpha);
   }
  }else if(s.type==='warp'){
   const inverseX=s.shiftXByX?.map(([x,d])=>[x+d,x]);
   for(let y=0;y<h;y++)for(let x=0;x<w;x++){
    const sx=inverseX?interp(x,inverseX):x-interp(y,s.shiftX),sy=y-(s.shiftYByX?interp(sx,s.shiftYByX):0),x0=Math.floor(sx),y0=Math.floor(sy),fx=sx-x0,fy=sy-y0;
    if(x0<0||x0+1>=w||y0<0||y0+1>=h)continue;
    const q=(y*w+x)*4,acc=[0,0,0];let a=0;
    for(let yy=0;yy<2;yy++)for(let xx=0;xx<2;xx++){const z=((y0+yy)*w+x0+xx)*4,weight=(xx?fx:1-fx)*(yy?fy:1-fy),v=mask[z+3]*src[z+3]/255*weight;a+=v;for(let c=0;c<3;c++)acc[c]+=src[z+c]*v;}
    if(a<.1)continue;for(let c=0;c<3;c++)layer[q+c]=Math.round(acc[c]/a);layer[q+3]=Math.round(a);
   }
  }else throw Error('Operação desconhecida '+s.type);
  if(s.destinationClipPath){
   const clip=await sharp(Buffer.from('<svg xmlns="http://www.w3.org/2000/svg" width="'+w+'" height="'+h+'"><path d="'+s.destinationClipPath+'" fill="white" stroke="white" stroke-width="'+(s.clipExpand||0)+'"/></svg>')).blur(s.clipFeather||.5).ensureAlpha().raw().toBuffer();
   for(let q=0;q<layer.length;q+=4)layer[q+3]=Math.round(layer[q+3]*clip[q+3]/255);
  }
  layers.push({input:await sharp(layer,{raw:{width:w,height:h,channels:4}}).png().toBuffer(),blend:'over'});
 }
 await sharp(original).composite(layers).png().toFile(path.join(here,'png',job.id+'.png'));
 console.log(job.id+': '+job.steps.length+' camadas de acabamento');
}
