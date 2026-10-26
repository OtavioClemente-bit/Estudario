// Portagem das curvas de ProfileIllustrations.kt, ProfileIce.kt e turma.json.
// <estudario-profile-art kind="level|flame|badges|goal" level="1..200"
//   lit="true|false" frozen="true|false" fraction="0..1" done="true|false">
// Tamanho pelo CSS do elemento; o canvas tem uma pequena margem para os contornos.
(function () {
  "use strict";
  if (!window.customElements || customElements.get("estudario-profile-art")) return;

  const PI = Math.PI, sin = Math.sin;
  const INK = "#302544", PAPER = "#FFFAEA", PURPLE = "#7551CB";
  // Somente curvas constantes entram no cache. Dobras, folhas e luzes animadas
  // usam Path2D transitórios, sem acumular uma entrada para cada quadro.
  const paths = new Map();
  const path = d => {
    if (!paths.has(d)) paths.set(d, new Path2D(d));
    return paths.get(d);
  };
  const clamp = (v, a, b) => Math.min(b, Math.max(a, v));
  const number = (v, fallback) => v !== null && v.trim() !== "" && Number.isFinite(Number(v)) ? Number(v) : fallback;
  function paint(ctx, d, fill, stroke, width = 1, alpha = 1) {
    ctx.save(); ctx.globalAlpha *= alpha;
    const p = typeof d === "string" ? path(d) : d;
    if (fill) { ctx.fillStyle = fill; ctx.fill(p); }
    if (stroke) { ctx.strokeStyle = stroke; ctx.lineWidth = width; ctx.stroke(p); }
    ctx.restore();
  }
  const shape = (c, d, color, w = 1.6) => paint(c, d, color, INK, w);
  const pen = (c, d, color = INK, w = .85, alpha = 1) => paint(c, d, null, color, w, alpha);
  function circle(c, x, y, r, fill, stroke, width = 1, alpha = 1) {
    c.save(); c.globalAlpha *= alpha; c.beginPath(); c.arc(x, y, r, 0, PI * 2);
    if (fill) { c.fillStyle = fill; c.fill(); }
    if (stroke) { c.strokeStyle = stroke; c.lineWidth = width; c.stroke(); }
    c.restore();
  }
  function transform(c, x, y, rot, sx, sy, draw) {
    c.save(); c.translate(x, y); c.rotate(rot * PI / 180); c.scale(sx, sy); c.translate(-x, -y);
    draw(); c.restore();
  }
  function spark(c, x, y, r, color, alpha = 1) {
    const p = new Path2D(); p.moveTo(x, y-r);
    p.quadraticCurveTo(x+r*.2,y-r*.2,x+r,y); p.quadraticCurveTo(x+r*.2,y+r*.2,x,y+r);
    p.quadraticCurveTo(x-r*.2,y+r*.2,x-r,y); p.quadraticCurveTo(x-r*.2,y-r*.2,x,y-r); p.closePath();
    paint(c, p, color, INK, .8, alpha);
  }
  const ranks = [
    ["#DA9B65", "#A96341", "#FFD3A0", "#695094"],
    ["#C9D5DD", "#889AAE", "#F4F7FA", "#66509B"],
    ["#F4C65E", "#D09436", "#FFE8A1", "#604493"],
    ["#9AC7EF", "#588BC5", "#DEEEFF", "#344F91"],
    ["#F1B061", "#C77B40", "#FFE1A0", "#873A61"],
    ["#F0CC6B", "#B58A39", "#FFEDB4", "#266F68"]
  ];
  const bookmarkSway = (d, p, t) => sin(p*2)*(.22+d*.055)+sin(t*4*PI)*(1-t)*(1.8+d*.18);
  function book(c, level, phase, tap) {
    const detail=clamp(Math.floor(level/5),0,10), active=tap>0 && tap<1;
    const colors=level<5 ? ["#B9AA95","#8D8173","#DFD5C7","#806E5E"] : ranks[clamp(Math.floor(level/10),0,5)];
    const [metal,shade,light,enamel]=colors, paper="#F4E6C8", ink="#AC997D";
    const lift=sin(tap*PI), breathing=1+sin(phase)*(.002+detail*.0005)+lift*.025;
    transform(c,50,82,sin(phase)*(.12+detail*.025)+sin(tap*4*PI)*(1-tap)*(1.7+detail*.12),breathing,breathing,()=>{
      // A sombra e os cadernos de papel dão volume sem trocar o traço de caneta da marca.
      paint(c,"M10 88C27 82 73 82 91 87C95 92 71 96 50 95C28 96 6 92 10 88Z",INK,null,0,.12);
      const growth=.92+detail*.006;
      transform(c,50,54,0,growth,growth,()=>{
        const cover="M7 30Q27 22 49 35Q72 21 92 28L95 79Q73 77 54 86Q50 90 46 86Q28 79 5 83Z";
        pen(c,cover,PAPER,5.5); shape(c,cover,shade,1.65);
        shape(c,"M7 30Q28 22 49 35Q71 22 92 28L93 74Q72 72 54 81Q50 85 46 81Q28 75 6 78Z",metal,1.05);
        paint(c,"M7 74Q28 71 48 81L50 86Q28 78 6 81Z",shade);
        pen(c,"M11 79Q30 76 46 82M56 82Q74 76 90 77",light,.75);
        shape(c,"M10 29Q29 23 49 36Q70 22 90 27L91 74Q71 72 51 84Q29 74 9 78Z",paper,1.05);
        pen(c,"M13 73Q29 70 46 79M13 76Q29 73 45 81M56 79Q74 71 88 72M57 82Q74 74 88 75",ink,.55);
        // As páginas continuam visíveis ao redor do marcador, mesmo a 84 px.
        const left="M11 26C24 20 37 24 49 33L50 77C36 68 23 67 10 72Z";
        const right="M49 33C62 22 77 19 90 24L91 70C77 65 63 69 50 77Z";
        shape(c,left,"#FFF4D9",1.25); shape(c,right,PAPER,1.25);
        paint(c,"M43 29L49 33L50 77L44 73Q47 54 43 29Z","#E7D6AF",null,0,.65);
        paint(c,"M49 33L55 29Q53 53 55 74L50 77Z","#E7D6AF",null,0,.4);
        pen(c,"M14 29Q28 25 40 31M60 29Q76 23 86 26","#FFFFFF",.8,.8);
        // Gravuras legíveis de folha e bússola, em vez de simular letras minúsculas.
        if(detail>=1) {
          pen(c,"M24 64C27 57 27 48 24 41",shade,.85);
          shape(c,"M25 50C18 49 16 44 18 40C24 39 28 44 25 50Z",light,.75);
          pen(c,"M19 42L25 49",shade,.55);
          spark(c,76,47,4.4,light);
        }
        if(detail>=2) {
          shape(c,"M26 58C25 51 29 47 33 47C36 52 32 57 26 58Z",light,.75);
          pen(c,"M32 49L27 56",shade,.55);
          circle(c,76,47,8,null,shade,.7);
          circle(c,76,47,1.15,enamel);
          const curl=sin(phase*2)*(.25+detail*.04)+lift*2;
          const fold=new Path2D();fold.moveTo(80-curl,23);fold.quadraticCurveTo(85,22,90,24);
          fold.lineTo(90,34+curl);fold.quadraticCurveTo(87-curl,27,80-curl,23);fold.closePath();
          shape(c,fold,paper,.75);pen(c,"M86 26L88 29","#FFFFFF",.65);
        }
        if(detail>=3) {
          pen(c,"M16 34L16 31Q23 28 30 32M70 31Q79 26 85 29L85 32M16 65L16 69Q25 68 32 72M70 72Q79 67 85 68L85 64",shade,.65);
          pen(c,"M76 36L76 38M76 56L76 58M65 47L67 47M85 47L87 47",shade,.7);
          pen(c,"M15 76Q29 73 41 79M60 79Q76 72 88 73",light,.6);
        }
        if(detail>=4) {
          const corners="M7 66L6 79Q13 77 21 79M92 62L94 76Q87 75 79 78";
          pen(c,corners,INK,2.7);pen(c,corners,light,1.25);
          shape(c,"M91 43L96 42L97 50L91 52Z","#BCA4DF",.75);
        }
        if(detail>=5) {
          shape(c,"M25 63C19 63 17 60 18 57C23 55 27 59 25 63Z",enamel,.65);
          pen(c,"M19 58L24 62",light,.55);
          pen(c,"M48 67L52 69M48 72L52 74M48 77L52 79",shade,.7);
        }
        if(detail>=6) {
          shape(c,"M80 60C73 58 71 68 79 69C75 66 76 62 80 60Z",metal,.65);
          circle(c,70,61,.7,enamel);circle(c,84,65,.7,enamel);
        }
        if(detail>=7) {
          pen(c,"M13 78Q29 76 42 82M59 82Q76 75 89 76",shade,.6);
          pen(c,"M14 80Q29 78 41 84M60 84Q76 77 89 78",light,.6);
        }
        if(detail>=8) {
          pen(c,"M8 43L8 34Q15 31 22 33M91 40L91 32Q85 29 78 32",light,.9);
          pen(c,"M12 39Q14 34 18 35M89 37Q85 32 82 34",enamel,.6);
          shape(c,"M91 55L96 54L97 62L91 64Z","#D69778",.7);
        }
        if(detail>=9) transform(c,83,75,sin(phase*2)*2.5+sin(tap*5*PI)*(1-tap)*8,1,1,()=>{
          const cord="M83 75C93 76 94 83 89 87Q87 89 88 91";
          pen(c,cord,INK,1.9);pen(c,cord,light,.95);
          pen(c,"M84 76C91 78 92 83 88 87",shade,.45);
          circle(c,88,90,1.65,metal,INK,.7);
          pen(c,"M86 92Q84 95 84 98M87 92Q86 95 86 98M88 92L88 99M89 92Q90 95 90 98M90 92Q92 95 92 97",INK,1.35);
          pen(c,"M86 92Q84 95 84 98M87 92Q86 95 86 98M88 92L88 99M89 92Q90 95 90 98M90 92Q92 95 92 97",light,.65);
          pen(c,"M85.5 92L90.5 92",shade,.8);
        });
        if(detail>=10) {
          shape(c,"M19 30L22 27L25 30L22 32Z",light,.5);
          shape(c,"M77 28L80 25L83 27L80 30Z",light,.5);
        }
        if(level>=100) pen(c,"M15 81Q29 79 40 85M61 85Q76 78 89 79",light,.65);
        if(level>=150) {
          pen(c,"M15 35L15 39M18 34L18 37M83 34L83 37M86 33L86 37",light,.65);
          circle(c,76,47,10,null,light,.5);
        }
        if(level>=200) pen(c,"M14 35L14 39M17 34L17 38M84 33L84 37M87 32L87 36",light,.7);
        if(active) {
          const turn=(tap*(1+Math.floor(detail/4)))%1,span=40*Math.cos(turn*PI),up=sin(turn*PI)*11;
          const shadow=new Path2D();shadow.moveTo(50,34);shadow.lineTo(50+span*.9,27);shadow.lineTo(50+span*.9,72);shadow.lineTo(50,78);shadow.closePath();
          paint(c,shadow,INK,null,0,sin(turn*PI)*.13);
          const sheet=new Path2D();sheet.moveTo(50,33);sheet.bezierCurveTo(50+span*.38,26-up,50+span,20-up,50+span,25);
          sheet.lineTo(50+span,70);sheet.bezierCurveTo(50+span*.8,66-up*.25,50+span*.4,70,50,77);sheet.closePath();
          shape(c,sheet,turn<.5 ? PAPER : "#F5E7CB",.85);
          const seam=new Path2D();seam.moveTo(50,34);seam.quadraticCurveTo(50+span*.2,51-up*.25,50,75);pen(c,seam,"#D6C19B",.55);
        }
      });
      // Marcador de tecido: somente o número no centro, sem texto minúsculo.
      transform(c,50,20,bookmarkSway(detail,phase,tap),1,1,()=>{
        paint(c,"M39 20Q51 17 65 20L65 66L52 61L39 68Z",INK,null,0,.15);
        const ribbon="M37 19Q49 15 63 18L63 64L50 58L37 66Z";
        pen(c,ribbon,PAPER,3.8);shape(c,ribbon,enamel,1.55);
        paint(c,"M37 19L40 18L40 61L37 66Z",shade,null,0,.35);
        pen(c,"M41 22Q50 19 59 21",level<5 ? "#C2B4A2" : light,.7,.7);
        if(detail>=3) pen(c,"M40 27L40 55M60 26L60 55",light,.55,.45);
        if(detail>=4) {
          shape(c,"M37 19Q49 15 63 18L63 23Q50 20 37 24Z",metal,.8);
          pen(c,"M42 20Q50 18 59 19",light,.65);
        }
        if(detail>=7) pen(c,"M45 56L50 53L55 56",light,.6);
        if(detail>=10) spark(c,50,20,1.8,light);
        if(level>=100) {circle(c,42,21,.65,enamel);circle(c,59,20,.65,enamel);}
        c.fillStyle=PAPER;c.textAlign="center";c.textBaseline="middle";
        if("letterSpacing" in c)c.letterSpacing="-0.3px";
        c.font=`900 ${level>=100 ? 14.5 : level>=10 ? 21 : 27}px Nunito, sans-serif`;
        c.fillText(String(level),50,40,22);
      });
      // No máximo duas pequenas luzes, próximas das páginas do livro.
      if(detail>=6) for(let i=0;i<Math.min(2,detail-5);i++) {
        const travel=(phase/(2*PI)+i*.46)%1;
        spark(c,i===0 ? 19 : 83,26-travel*9,1.1+detail*.035,light,sin(travel*PI)*.55);
      }
      if(active && detail>=2) spark(c,84+tap*3,27-tap*8,1.7,light,sin(tap*PI)*.8);
    });
  }

  function badges(c) {
    const ribbon="M31 55L49 62L40 93L30 84L18 87ZM53 62L71 55L84 87L71 84L62 94Z";
    pen(c,ribbon,"#FFFFFF",10); shape(c,ribbon,PURPLE,3.5);
    pen(c,"M32 69L28 80M68 69L73 80","#CAB6FF",2);
    const medal="M50 8C57 6 61 13 67 14C75 14 76 22 80 27C86 33 81 40 83 46C83 53 75 56 72 62C67 69 59 65 53 69C46 73 40 67 34 67C26 68 25 59 20 55C14 51 18 43 16 37C14 29 22 26 24 20C27 13 35 15 40 10Q45 6 50 8Z";
    pen(c,medal,"#FFFFFF",10); shape(c,medal,"#F4C65E",3.6);
    circle(c,50,39,23,"#FFE8A1",INK,1.6);
    shape(c,"M50 31Q39 24 31 29L31 48Q41 44 50 51Q59 44 69 48L69 29Q60 25 50 31Z",PAPER,2.8);
    pen(c,"M50 32L50 49M36 34Q41 33 45 36M36 40Q41 39 45 42M56 36Q61 33 65 34",INK,1.6);
    pen(c,"M25 33L22 34M27 50L24 52M74 32L78 30M65 60L67 63","#BA7B32",1.7);
    spark(c,85,13,6,"#FFD869");
  }
  function goal(c, fraction, done, track) {
    c.save(); c.lineWidth=4; c.strokeStyle=track;
    c.beginPath(); c.arc(43,52,40,0,PI*2); c.stroke();
    if (fraction>0) {
      c.strokeStyle=done ? "#22B58A" : "#8060D7";
      c.beginPath(); c.arc(43,52,40,-PI/2,-PI/2+fraction*PI*2); c.stroke();
    }
    c.restore();
    const target="M44 20C62 18 76 33 75 52C76 70 63 84 44 84C26 85 12 70 13 53C12 35 26 20 44 20Z";
    pen(c,target,"#FFFFFF",7); shape(c,target,PAPER,3.2);
    circle(c,44,52,24,done ? "#93E1BC" : "#EF987D",INK,1.8);
    circle(c,44,52,16,PAPER,INK,1.6);
    circle(c,44,52,8.5,done ? "#2CAE83" : "#E86B52",INK,1.6);
    pen(c,"M23 38Q26 31 32 29","#FFFFFF",2.1);
    pen(c,"M45 51L87 9",INK,6); pen(c,"M46 50L87 9","#F7CD78",2.6);
    shape(c,"M42 54L47 38L57 48Z","#F7CD78",2);
    shape(c,"M73 23L72 11L88 2L87 15L99 14L89 29L78 28Z",PURPLE,2.5);
    pen(c,"M77 22L87 12M80 18L79 13M83 23L89 23","#D7C5FF",1.5);
    if(done) {
      circle(c,78,77,15,"#FFFFFF"); circle(c,78,77,12,"#22B58A",INK,2);
      pen(c,"M72 77L77 82L85 72","#FFFFFF",3);
    }
  }

  // Paths autorais Fire de app/src/main/resources/turma.json, sem troca de personagem.
  const FIRE="M50 4c10 16 34 26 34 56 0 18-15 32-34 32S16 78 16 60c0-14 8-22 14-30 2 10 6 14 10 14-2-14 2-28 10-40z";
  const CORE="M50 46c9 9 18 16 18 27 0 10-8 17-18 17s-18-7-18-17c0-11 9-16 18-27z";
  const FIRE_FACE=[
    ["M35.04 73.1A3.91 2.21 0 1 0 42.86 73.1A3.91 2.21 0 1 0 35.04 73.1Z","#FF8FA3",null,0,.75],
    ["M57.14 73.1A3.91 2.21 0 1 0 64.96 73.1A3.91 2.21 0 1 0 57.14 73.1Z","#FF8FA3",null,0,.75],
    ["M38.7 68A3.65 4.76 0 1 0 46.01 68A3.65 4.76 0 1 0 38.7 68Z","#26234A"],
    ["M39.63 66.13A1.53 1.53 0 1 0 42.69 66.13A1.53 1.53 0 1 0 39.63 66.13Z","#FFFFFF"],
    ["M43.03 69.53A.68 .68 0 1 0 44.39 69.53A.68 .68 0 1 0 43.03 69.53Z","#FFFFFF",null,0,.75],
    ["M53.99 68A3.65 4.76 0 1 0 61.3 68A3.65 4.76 0 1 0 53.99 68Z","#26234A"],
    ["M54.93 66.13A1.53 1.53 0 1 0 57.99 66.13A1.53 1.53 0 1 0 54.93 66.13Z","#FFFFFF"],
    ["M58.33 69.53A.68 .68 0 1 0 59.69 69.53A.68 .68 0 1 0 58.33 69.53Z","#FFFFFF",null,0,.75],
    ["M37.25 60.35L45.75 62.9M62.75 60.35L54.25 62.9",null,"#26234A",2.55],
    ["M44.48 73.95Q50 83.3 55.52 73.95Z","#26234A"],
    ["M47.45 78.71A2.55 1.19 0 1 0 52.55 78.71A2.55 1.19 0 1 0 47.45 78.71Z","#FF8FA3"]
  ];
  const FIRE_SMALL=[
    ["M39.2 68A3.15 4 0 1 0 45.5 68A3.15 4 0 1 0 39.2 68Z","#26234A"],
    ["M54.5 68A3.15 4 0 1 0 60.8 68A3.15 4 0 1 0 54.5 68Z","#26234A"],
    FIRE_FACE[8],FIRE_FACE[9]
  ];
  const mutedColors=new Map();
  function tone(color, muted) {
    if (!color || !muted) return color;
    if(!mutedColors.has(color)) {
      const rgb=[1,3,5].map(i=>parseInt(color.slice(i,i+2),16)/255).map(v=>v<=.04045 ? v/12.92 : Math.pow((v+.055)/1.055,2.4));
      const v=.55+(rgb[0]*.2126+rgb[1]*.7152+rgb[2]*.0722)*.42;
      mutedColors.set(color,`rgb(${Math.round(v*255)},${Math.round(v*255)},${Math.round(Math.min(v*1.02,1)*255)})`);
    }
    return mutedColors.get(color);
  }
  function fireGlyph(c, muted, small) {
    c.save(); c.scale(100/116,100/116); c.translate(8,10);
    const draw=(d,f,s,w=1,a=1)=>paint(c,d,tone(f,muted),tone(s,muted),w,a);
    draw(FIRE,"#FFFFFF","#FFFFFF",16); draw(CORE,"#FFFFFF","#FFFFFF",15);
    c.save(); c.translate(0,5); draw(FIRE,"#E0640A","#26234A",5); c.restore();
    draw(FIRE,"#FF8A2B","#26234A",5); draw(CORE,"#FFD84A","#26234A",4);
    for(const [d,f,s,w,a] of small ? FIRE_SMALL : FIRE_FACE) draw(d,f,s,w,a);
    c.restore();
  }
  function ice(c, front) {
    const edge="#5998BE", light="#ECFAFF", blue="#ADDDF1";
    if(!front) {
      const shell="M29 14L68 12L86 28L89 70L74 90L27 92L12 73L14 33Z";
      pen(c,shell,light,6); paint(c,shell,blue,null,0,.55); pen(c,shell,edge,1.7);
      paint(c,"M29 14L68 12L86 28L63 25L34 29L14 33Z",light,null,0,.85);
      paint(c,"M86 28L89 70L74 90L72 72L77 37Z","#73B9D9",null,0,.35);
      paint(c,"M12 73L27 92L74 90L72 80L30 82Z","#8EC9E4",null,0,.7);
    } else {
      pen(c,"M24 32L32 22L44 21M19 43L18 64M78 40L80 51M33 86L48 86",light,2.7);
      pen(c,"M15 57L24 61L20 69M24 61L29 59M83 62L76 68L79 76M76 68L70 69M53 89L50 81L55 77",edge,.9,.7);
      paint(c,"M15 75L9 64L5 77L10 87L26 91Z",blue,edge,1.2);
      paint(c,"M76 89L86 78L96 83L91 94Z",blue,edge,1.2);
      pen(c,"M9 69L11 79L21 87M87 83L84 88L90 91",light,1.4);
      pen(c,"M80 14L80 23M76 18.5L84 18.5M19 20L19 26M16 23L22 23",edge,1.2);
    }
  }
  function flame(c, lit, frozen, phase, side) {
    if(frozen) ice(c,false);
    const breath=lit ? sin(phase*4) : 0;
    if(lit) {
      const gradient=c.createRadialGradient(50,60,0,50,60,49);
      gradient.addColorStop(0,`rgba(255,174,66,${.20+breath*.025})`); gradient.addColorStop(1,"rgba(255,174,66,0)");
      circle(c,50,60,49,gradient);
      c.save(); c.fillStyle="rgba(242,132,53,.12)"; c.beginPath(); c.ellipse(50,88.5,25,3.5,0,0,PI*2); c.fill(); c.restore();
    }
    transform(c,50,88,lit ? sin(phase*2)*2.6 : 0,1,1,()=>{
      transform(c,50,frozen ? 50 : 88,0,frozen ? .72 : .93-breath*.015,frozen ? .72 : .93+breath*.035,()=>fireGlyph(c,!lit,side<36));
    });
    if(frozen) ice(c,true);
    if(lit) for(let i=0;i<(side<64 ? 2 : 4);i++) {
      const travel=(phase/(2*PI)*2+i*.27)%1;
      circle(c,(i%2===0 ? 16 : 84)+sin(phase*2+i)*3.5,63-travel*48,1.8-travel*.8,i%2===0 ? "#FFB421" : "#FF7847",null,0,sin(travel*PI)*.75);
    }
  }

  // Mesmo motionOf do Android, com duração de 640 ms para estes quatro gestos.
  function motion(kind, frozen, t) {
    if(t>=1) return {dx:0,dy:0,r:0,sx:1,sy:1,pivot:92};
    const fade=1-t, s=(k,p=0)=>sin(k*PI*t+p);
    if(kind==="badges") return {dx:0,dy:0,r:10*s(3)*fade,sx:1,sy:1,pivot:8};
    if(kind==="goal") return {dx:4*s(10)*fade,dy:-2*s(8)*fade,r:7*s(10)*fade,sx:1+.06*s(1),sy:1+.06*s(1),pivot:92};
    if(frozen) return {dx:0,dy:0,r:11*s(4)*fade,sx:1,sy:1,pivot:92};
    return {dx:0,dy:0,r:3*s(4,1)*fade,sx:1-.06*s(6)*fade,sy:1+.12*s(6)*fade,pivot:92};
  }
  // FastOutSlowInEasing: a mesma curva cubic-bezier(.4,0,.2,1).
  function ease(t) {
    let lo=0,hi=1,u=t;
    for(let i=0;i<12;i++) { const x=3*(1-u)*(1-u)*u*.4+3*(1-u)*u*u*.2+u*u*u; if(x<t)lo=u;else hi=u;u=(lo+hi)/2; }
    return 3*(1-u)*u*u+u*u*u;
  }
  const mounted=new Set();
  const reduced=window.matchMedia ? window.matchMedia("(prefers-reduced-motion: reduce)") : {matches:false};
  const colorScheme=window.matchMedia ? window.matchMedia("(prefers-color-scheme: dark)") : {matches:false};
  let raf=0,lastFrame=0,listening=false,themeObserver=null;
  function tick(now) {
    raf=0;
    const dt=lastFrame ? Math.min(now-lastFrame,64) : 0; lastFrame=now;
    let again=false;
    for(const el of mounted) if(el._visible() && el._needsAnimation()) {
      el._time+=dt; el._draw(); if(el._needsAnimation())again=true;
    }
    if(again)raf=requestAnimationFrame(tick); else lastFrame=0;
  }
  function wake() {
    const active=Array.from(mounted).some(el=>el._visible() && el._needsAnimation());
    if(active && !raf) {lastFrame=0;raf=requestAnimationFrame(tick);}
    if(!active && raf) {cancelAnimationFrame(raf);raf=0;lastFrame=0;}
  }
  function refresh() {
    for(const el of mounted) {
      if(reduced.matches) {el._touchStart=-Infinity;el._progressStart=-Infinity;el._progressFrom=el._fraction;}
      if(el._visible()) el._draw();
    }
    wake();
  }
  function resize() {for(const el of mounted) el._resize();wake();}
  function refreshTheme() {
    // Trocar o tema também atualiza as artes estáticas, sem iniciar um RAF.
    for(const el of mounted) {el._readTheme();if(el._visible())el._draw();}
  }
  function listen(on) {
    if(on===listening)return;listening=on;
    const op=on ? "addEventListener" : "removeEventListener";
    document[op]("visibilitychange",refresh);window[op]("resize",resize);
    if(reduced[op])reduced[op]("change",refresh);
    else if(reduced[on ? "addListener" : "removeListener"])reduced[on ? "addListener" : "removeListener"](refresh);
    if(colorScheme[op])colorScheme[op]("change",refreshTheme);
    else if(colorScheme[on ? "addListener" : "removeListener"])colorScheme[on ? "addListener" : "removeListener"](refreshTheme);
    if(on && window.MutationObserver && document.documentElement) {
      themeObserver=new MutationObserver(refreshTheme);
      themeObserver.observe(document.documentElement,{attributes:true,attributeFilter:["data-theme","class","style"]});
    } else if(!on && themeObserver) {themeObserver.disconnect();themeObserver=null;}
    if(document.fonts)document.fonts[op]("loadingdone",refresh);
  }
  class ProfileArt extends HTMLElement {
    static get observedAttributes() {return ["kind","level","lit","frozen","fraction","done"];}
    constructor() {
      super();
      this.attachShadow({mode:"open"});
      this.shadowRoot.innerHTML='<style>:host{display:inline-block;position:relative;width:var(--profile-art-size,40px);height:var(--profile-art-size,40px);flex-shrink:0;vertical-align:middle;cursor:pointer;touch-action:manipulation;outline-offset:4px}:host([kind="level"]){width:var(--profile-art-size,116px);height:var(--profile-art-size,116px)}:host(:focus-visible){outline:2px solid #8060D7;border-radius:8px}canvas{position:absolute;pointer-events:none;display:block}</style><canvas aria-hidden="true"></canvas>';
      this._canvas=this.shadowRoot.querySelector("canvas");this._ctx=this._canvas.getContext("2d");
      this._time=0;this._touchStart=-Infinity;this._progressStart=-Infinity;this._fraction=0;this._progressFrom=0;
      this._inView=false;this._width=0;this._height=0;this._connected=false;
      this._click=e=>{e.preventDefault();e.stopPropagation();this._touch();};
      this._key=e=>{if(e.key==="Enter" || e.key===" " || e.key==="Spacebar") {e.preventDefault();e.stopPropagation();if(!e.repeat)this._touch();}};
      this._keyUp=e=>{if(e.key==="Enter" || e.key===" " || e.key==="Spacebar") {e.preventDefault();e.stopPropagation();}};
    }
    connectedCallback() {
      if(this._connected)return;this._connected=true;
      this.setAttribute("role","button");if(!this.hasAttribute("tabindex"))this.tabIndex=0;
      this.addEventListener("click",this._click);this.addEventListener("keydown",this._key);this.addEventListener("keyup",this._keyUp);
      mounted.add(this);listen(true);this._read();this._resize();
      if(window.IntersectionObserver) {
        this._intersection=new IntersectionObserver(entries=>{
          this._inView=entries.some(e=>e.isIntersecting);if(this._visible())this._draw();wake();
        });this._intersection.observe(this);
      } else this._inView=true;
      if(window.ResizeObserver) {this._observer=new ResizeObserver(()=>{this._resize();wake();});this._observer.observe(this);}
      if(document.fonts)document.fonts.load('900 25px Nunito').then(()=>{if(this._connected && this._visible())this._draw();}).catch(()=>{});
      this._draw();wake();
    }
    disconnectedCallback() {
      this._connected=false;mounted.delete(this);this._inView=false;
      if(this._intersection)this._intersection.disconnect();if(this._observer)this._observer.disconnect();
      this.removeEventListener("click",this._click);this.removeEventListener("keydown",this._key);this.removeEventListener("keyup",this._keyUp);
      this._touchStart=-Infinity;this._progressStart=-Infinity;
      if(!mounted.size)listen(false);wake();
    }
    attributeChangedCallback(name,oldValue,newValue) {
      if(oldValue===newValue || !this._connected)return;
      const from=this._progress();this._read();
      if(name==="fraction") {this._progressFrom=from;this._progressStart=reduced.matches ? -Infinity : this._time;}
      if(name==="level" || name==="kind")this._touchStart=-Infinity;
      if(name==="kind")this._resize();
      if(this._visible())this._draw();wake();
    }
    _read() {
      const kind=this.getAttribute("kind");this._kind=["level","flame","badges","goal"].includes(kind) ? kind : "level";
      this._level=clamp(Math.floor(number(this.getAttribute("level"),1)),1,200);
      this._lit=this.getAttribute("lit")==="true";this._frozen=this.getAttribute("frozen")==="true";
      this._done=this.getAttribute("done")==="true";this._fraction=clamp(number(this.getAttribute("fraction"),0),0,1);
      this.setAttribute("aria-label",this._kind==="level" ? `Folhear o livro do nível ${this._level}` : this._kind==="flame" ? this._frozen ? "Animar a chama congelada" : "Animar a chama da sequência" : this._kind==="badges" ? "Animar o emblema de conquista" : `Animar a meta do dia, ${Math.round(this._fraction*100)}%${this._done ? ", concluída" : ""}`);
      this._readTheme();
    }
    _readTheme() {
      const style=getComputedStyle(this);
      this._track=style.getPropertyValue("--profile-art-track").trim() || style.getPropertyValue("--line").trim() || "#DCD7E5";
    }
    _visible() {return this._connected && this._inView && !document.hidden && this._width>0 && this._height>0;}
    _duration() {return this._kind==="level" ? 1050+clamp(Math.floor(this._level/5),0,10)*60 : 640;}
    _tap() {return reduced.matches ? 1 : clamp((this._time-this._touchStart)/this._duration(),0,1);}
    _progress() {
      const t=reduced.matches ? 1 : clamp((this._time-this._progressStart)/700,0,1);
      return this._progressFrom+(this._fraction-this._progressFrom)*(t>=1 ? 1 : ease(t));
    }
    _needsAnimation() {
      return !reduced.matches && (this._kind==="level" || this._kind==="flame" && this._lit || this._tap()<1 || this._kind==="goal" && this._time-this._progressStart<700);
    }
    _touch() {
      if(!this._visible())return;
      this._touchStart=reduced.matches ? -Infinity : this._time;this._draw();wake();
    }
    _resize() {
      this._width=this.clientWidth;this._height=this.clientHeight;
      this._draw();
    }
    _draw() {
      if(!this._ctx || !this._connected || !this._width || !this._height)return;
      const c=this._ctx,w=this._width,h=this._height,side=Math.min(w,h),margin=side*.10;
      const dpr=window.devicePixelRatio || 1,cw=Math.max(1,Math.round((w+margin*2)*dpr)),ch=Math.max(1,Math.round((h+margin*2)*dpr));
      if(this._canvas.width!==cw || this._canvas.height!==ch) {this._canvas.width=cw;this._canvas.height=ch;}
      this._canvas.style.width=`${w+margin*2}px`;this._canvas.style.height=`${h+margin*2}px`;
      this._canvas.style.left=`${-margin}px`;this._canvas.style.top=`${-margin}px`;
      c.setTransform(1,0,0,1,0,0);c.clearRect(0,0,cw,ch);
      c.setTransform(dpr,0,0,dpr,0,0);c.translate(margin+(w-side)/2,margin+(h-side)/2);c.scale(side/100,side/100);
      c.lineCap="round";c.lineJoin="round";
      const tap=this._tap(),time=reduced.matches ? 0 : this._time;
      if(this._kind==="level")book(c,this._level,(time%6400)/6400*PI*2,tap);
      else {
        const m=motion(this._kind,this._frozen,tap);c.save();c.translate(m.dx,m.dy);
        transform(c,50,m.pivot,m.r,m.sx,m.sy,()=>{
          if(this._kind==="badges")badges(c);
          else if(this._kind==="goal")goal(c,this._progress(),this._done,this._track);
          else flame(c,this._lit,this._frozen,(time%4800)/4800*PI*2,side);
        });c.restore();
      }
    }
  }
  customElements.define("estudario-profile-art",ProfileArt);
})();
