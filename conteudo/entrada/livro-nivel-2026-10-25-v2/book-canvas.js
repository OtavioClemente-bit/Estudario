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
