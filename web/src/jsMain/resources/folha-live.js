// O Folha animado do site (2.0). Nasceu da tradução de app/.../ui/assistant/Folha.kt (mesma grade 100 x 100,
// mesmas cores e o mesmo rosto) e ganhou acabamento de mascote:
//  - cada humor é uma POSE; o Folha vai de uma pose para a outra por molas (nada teleporta), com antecipação
//    (agacha antes de pular, o braço volta antes de acenar) e passa um pouco do ponto antes de assentar;
//  - estica subindo e amassa ao aterrissar; as mãos vêm um tiquinho atrasadas em relação ao corpo;
//  - braços que afinam e dobram, mãos de verdade (luva, aberta para acenar, fechada para comemorar,
//    apontando), com punho e pulseira dourada;
//  - olhos com pálpebras de cima e de baixo (feliz em meia-lua, triste caído, piscadela), pupila que cresce
//    quando ele se anima, sobrancelhas em todos os humores; triste de verdade (a ponta de dentro sobe);
//  - livro com cara de livro do Estudário (cantoneiras douradas, capa com espessura, miolo com várias folhas,
//    cabeceado, orelha, anotações com marca-texto) e contorno macio para ler bem em fundo claro e escuro;
//    pequeno (menos de 72 px), some o detalhe miúdo e os traços engrossam.
// O canvas sobra para fora da caixa (MARGIN), para mão e borla nunca serem cortadas.
//
// Uso: <estudario-folha mood="idle|thinking|talking|happy|sad|wave|point|wink|surprised|proud|focus|sleepy|relieved|ecstatic"
//        size="96" [say="texto"] [cover|ribbon|hat|eyes|neck|hand|aura|gloves|pet="peça do armário"] [plain] [noscene] [noenter]>
// Armário e contexto do dia: window.folhaSetLook({ cover, hat, ..., streak, atRisk }); saída: window.folhaLeave(raiz, fim).
// Toque: amassa, pula e pisca. el.gulp (0..1) abre a boca para "engolir" folhas (tela de processamento).
// Um laço de animação só para todos, parado fora da tela; quadro fixo para quem pede menos movimento.
(function () {
  if (window.customElements && customElements.get("estudario-folha")) return;

  const INK = "#26215C", PAGE_LINE = "#D8D4E6", CAP = "#231C6B", CAP_TOP = "#2F2789", ARM = "#3326CE", ARM_L = "#6158F0";
  const GOLD = "#F5B83D", MINT = "#7EE0B8", MINT_DARK = "#3FBF8F", CHEEK = "#F4A6C0", SPARK = "#FFE07A";
  const PAPER = "#FFFDF8", PAPER_MID = "#F7F4EE", PAPER_EDGE = "#E9E4DA", PAPER_EDGE_DARK = "#CFC8BC", GUTTER = "#3A3170";
  const PI = Math.PI, sin = Math.sin, cos = Math.cos, abs = Math.abs;
  const MARGIN = 18;                          // % de sobra do canvas para cada lado da caixa
  const SMALL = 72;                           // abaixo disso (px), desenho simplificado

  const reduceMotion = window.matchMedia ? window.matchMedia("(prefers-reduced-motion: reduce)") : { matches: false };

  // ------------------------------------------------------------------ armário (FolhaWardrobe.kt) e contexto do dia
  // Capas: degradê (claro, meio, escuro), a sombra de baixo, o metal das cantoneiras e a textura. Os braços
  // acompanham a capa (arm: cor do braço e o brilho dele), para o Folha ser uma peça só.
  const COVERS = {
    indigo: { c: ["#5247E0", "#3A2FC4", "#2A209E"], under: "#1A1370", corner: "gold", arm: ["#3326CE", "#6158F0"] },
    esmeralda: { c: ["#36C897", "#1F9E75", "#147A5A"], under: "#0B4F3A", corner: "gold", arm: ["#178A63", "#4FD3A2"] },
    rubi: { c: ["#E8566D", "#C42F4B", "#961D37"], under: "#5E0F22", corner: "gold", arm: ["#B3263F", "#EE6B80"] },
    ametista: { c: ["#AA68EE", "#8746D3", "#662DAB"], under: "#3E1670", corner: "gold", arm: ["#7438C4", "#B07CF0"] },
    bolinhas: { c: ["#FF8FA3", "#F06A85", "#D14D6B"], under: "#7A1F35", corner: "silver", pattern: "dots", arm: ["#D9506D", "#FF9DB0"] },
    couro: { c: ["#BC7D4E", "#965A31", "#6D3D1F"], under: "#43240E", corner: "gold", pattern: "leather", stitch: "rgba(255,226,180,0.75)", arm: ["#82491F", "#C08453"] },
    jeans: { c: ["#5B86C4", "#3F6AA8", "#2C4F84"], under: "#1B3358", corner: "silver", pattern: "denim", stitch: "rgba(242,166,74,0.9)", arm: ["#355E99", "#6E98D4"] },
    xadrez: { c: ["#D2463C", "#B0322B", "#86231E"], under: "#4F1410", corner: "gold", pattern: "plaid", arm: ["#A12C26", "#DE6A5F"] },
    grafite: { c: ["#61667C", "#43475C", "#2D3042"], under: "#1A1C28", corner: "silver", arm: ["#3A3E52", "#6B7088"] },
    noite: { c: ["#36388A", "#212059", "#121138"], under: "#07061E", corner: "silver", pattern: "stars", arm: ["#26246A", "#4B4AA8"] },
    holografica: { c: ["#C9B6FF", "#9FD8FF", "#FFC2E2"], under: "#5B4C9A", corner: "silver", pattern: "holo", arm: ["#8E7CE0", "#C7B8FF"] },
    ouro: { c: ["#FFDD84", "#EBB13A", "#BD8214"], under: "#7A4F05", corner: "silver", pattern: "shine", arm: ["#C98A12", "#FFD978"] },
    obsidiana: { c: ["#34343F", "#1B1B24", "#0C0C12"], under: "#000000", corner: "gold", pattern: "foil", arm: ["#22222C", "#4A4A5C"] },
    diamante: { c: ["#EAF6FF", "#B9DDF7", "#86BCE6"], under: "#4F7FA6", corner: "silver", pattern: "facets", arm: ["#6FA9D9", "#BFE3FF"] },
  };
  // Fitas: escuro, meio e claro (quatro cores = degradê pronto, como o arco-íris).
  const RIBBONS = {
    menta: [MINT_DARK, MINT, "#C6F7E2"], ceu: ["#3D8BEA", "#6FB0FF", "#D6EAFF"], coral: ["#E2604A", "#FF8C75", "#FFD6CC"],
    lilas: ["#9466DD", "#B995F5", "#ECDFFF"], preta: ["#111118", "#2F2F3A", "#77778A"], dourada: ["#C98A12", GOLD, "#FFEBB5"],
    arcoiris: ["#E8566D", "#F5B83D", "#36C897", "#6FB0FF"],
  };
  // Luvas: preenchimento, punho e o tipo (arco-íris tem degradê; boxe muda o formato da mão).
  const GLOVES = {
    branca: ["#FFFFFF", "#ECE8F6"], menta: ["#C9F7E4", "#7EE0B8"], coral: ["#FFD3C9", "#FF8C75"],
    preta: ["#3A3A46", "#24242E"], dourada: ["#FFE9A6", "#F5B83D"], arcoiris: ["#FFFFFF", "#B995F5", "rainbow"],
    boxe: ["#E5373F", "#FFFFFF", "boxe"],
  };
  // O que a pessoa escolheu e o contexto do dia; o site atualiza por window.folhaSetLook (veja o fim do arquivo).
  // streak: dias seguidos (medalha no capelo a partir de 7); atRisk: a sequência pode cair hoje; hour: para testes.
  const SLOTS = ["cover", "ribbon", "hat", "eyes", "neck", "hand", "aura", "gloves", "pet"];
  const LOOK = { cover: "indigo", ribbon: "menta", hat: "capelo", eyes: "nenhum", neck: "nenhum", hand: "nenhum", aura: "nenhum", gloves: "branca", pet: "nenhum", streak: 0, atRisk: false, hour: null };
  // Escolhas antigas (um "acessório" só) viram a peça equivalente.
  const LEGACY_ACC = { oculos: ["eyes", "leitura"], gravata: ["neck", "borboleta"], estrela: ["neck", "broche"], louros: ["hat", "louros"], lapis: ["hand", "lapis"] };
  function resolveLook(el, over) {
    const a = (n) => (over && over[n]) || (el && el.getAttribute(n)) || LOOK[n];
    const h = LOOK.hour != null ? LOOK.hour : new Date().getHours();
    const plain = !!(over && over.plain) || (el && el.hasAttribute("plain"));   // vitrine: só as peças, sem medalha nem hora do dia
    const cover = COVERS[a("cover")] || COVERS.indigo;
    const scene = !plain && !(el && el.hasAttribute("noscene"));   // noscene: sem café nem touca (vitrine do armário)
    return {
      cover, coverId: a("cover"), ribbon: RIBBONS[a("ribbon")] || RIBBONS.menta, hat: a("hat"), eyes: a("eyes"), neck: a("neck"), hand: a("hand"),
      aura: a("aura"), gloves: GLOVES[a("gloves")] || GLOVES.branca, arm: cover.arm, pet: a("pet"),
      streak: plain ? 0 : LOOK.streak, atRisk: !plain && LOOK.atRisk,
      morning: scene && h >= 5 && h < 11, night: scene && (h >= 22 || h < 5),
    };
  }
  const sstep = (x) => { x = clamp(x, 0, 1); return x * x * (3 - 2 * x); };

  // ------------------------------------------------------------------ cor e formas básicas
  function hex(c) { const n = parseInt(c.slice(1), 16); return [(n >> 16) & 255, (n >> 8) & 255, n & 255]; }
  function rgba(c, a) { const [r, g, b] = hex(c); return `rgba(${r},${g},${b},${a})`; }
  function lerpColor(a, b, f) { const x = hex(a), y = hex(b), m = (i) => Math.round(x[i] + (y[i] - x[i]) * f); return `rgb(${m(0)},${m(1)},${m(2)})`; }
  const clamp = (v, lo, hi) => Math.max(lo, Math.min(hi, v));
  const lerp = (a, b, f) => a + (b - a) * f;

  const GRAIN = (function () {
    let s = 7;
    const rnd = () => { s = (s * 1103515245 + 12345) & 0x7fffffff; return s / 0x7fffffff; };
    const out = [];
    for (let i = 0; i < 80; i++) {
      const side = i % 2 === 0 ? -1 : 1;
      out.push({ x: 50 + side * (3 + rnd() * 32), y: 44 + rnd() * 36, r: 0.15 + rnd() * 0.22, a: 0.05 + rnd() * 0.08, long: Math.floor(rnd() * 4) === 0 });
    }
    return out;
  })();

  function page(side, drop = 0, spread = 0) {
    const x = (v) => 50 + side * (v + spread * (v / 38));
    const p = new Path2D();
    p.moveTo(x(0), 44 + drop * 0.2);
    p.bezierCurveTo(x(10), 37.6, x(26), 36.4, x(38), 40.4 + drop);
    p.lineTo(x(38), 84.4 + drop);
    p.bezierCurveTo(x(26), 80.6 + drop, x(10), 81.4 + drop, x(0), 87 + drop);
    p.closePath();
    return p;
  }
  function pageBlock(side, depth) {
    const outer = 50 + side * 38, outerLow = 50 + side * (38 + depth * 0.45);
    const p = new Path2D();
    p.moveTo(outer, 40.4); p.lineTo(outer, 84.4);
    p.bezierCurveTo(50 + side * 26, 80.6, 50 + side * 10, 81.4, 50, 87);
    p.lineTo(50, 87 + depth);
    p.bezierCurveTo(50 + side * 10, 81.4 + depth, 50 + side * 26.5, 80.6 + depth, outerLow, 84.4 + depth);
    p.lineTo(outerLow, 41.6 + depth * 0.3);
    p.closePath();
    return p;
  }
  function coverPath() {
    const c = new Path2D();
    c.moveTo(8.5, 43.5); c.bezierCurveTo(22, 40.5, 38, 41.5, 50, 47.5); c.bezierCurveTo(62, 41.5, 78, 40.5, 91.5, 43.5);
    c.lineTo(91.5, 89); c.bezierCurveTo(78, 86.4, 62, 87.4, 50, 93); c.bezierCurveTo(38, 87.4, 22, 86.4, 8.5, 89); c.closePath();
    return c;
  }
  function vGrad(g, stops, y0, y1) { const gr = g.createLinearGradient(0, y0, 0, y1); stops.forEach((c, i) => gr.addColorStop(Array.isArray(c) ? c[0] : i / (stops.length - 1), Array.isArray(c) ? c[1] : c)); return gr; }
  function hGrad(g, stops, x0, x1) { const gr = g.createLinearGradient(x0, 0, x1, 0); stops.forEach((c, i) => gr.addColorStop(Array.isArray(c) ? c[0] : i / (stops.length - 1), Array.isArray(c) ? c[1] : c)); return gr; }
  function line(g, color, x0, y0, x1, y1, w, round = true) { g.beginPath(); g.moveTo(x0, y0); g.lineTo(x1, y1); g.strokeStyle = color; g.lineWidth = w; g.lineCap = round ? "round" : "butt"; g.stroke(); }
  function stroke(g, path, color, w) { g.strokeStyle = color; g.lineWidth = w; g.lineCap = "round"; g.lineJoin = "round"; g.stroke(path); }
  function oval(g, color, x, y, w, h) { g.beginPath(); g.ellipse(x + w / 2, y + h / 2, Math.max(w / 2, 0.01), Math.max(h / 2, 0.01), 0, 0, 2 * PI); g.fillStyle = color; g.fill(); }
  function circle(g, color, r, x, y) { g.beginPath(); g.arc(x, y, Math.max(r, 0.01), 0, 2 * PI); g.fillStyle = color; g.fill(); }

  // ------------------------------------------------------------------ folha virando (pensando)
  const TURN_SHARE = 0.32, TURN_CYCLE = 2.4;
  function turnState(t) {
    const cycle = (t % TURN_CYCLE) / TURN_CYCLE, raw = clamp(cycle / TURN_SHARE, 0, 1);
    const p = raw * raw * (3 - 2 * raw), c = cos(PI * p), lift = sin(PI * p);
    return { cycle, raw, p, c, lift, edgeX: 50 + 38 * c, up: lift * 6, turning: raw > 0 && raw < 1 };
  }

  // ------------------------------------------------------------------ poses
  // Valores numéricos vão por mola de uma pose para outra; a forma da mão troca com um "pop".
  const REST_L = [4, 78], REST_R = [96, 78];
  // ctx: tm = segundos desde que o humor começou (cenas com começo, meio e fim); worried = sequência em risco.
  function pose(mood, t, look, fid, ctx) {
    const tm = ctx && ctx.tm != null ? ctx.tm : 9;
    const p = {
      hop: sin(t * 2.1) * 1.4, tilt: sin(t * 0.9) * 1.2,
      hL: [REST_L[0], REST_L[1] + sin(t * 2.1) * 1.2], hR: [REST_R[0], REST_R[1] - sin(t * 2.1) * 1.2],
      rotL: 0, rotR: 0, bendL: 2, bendR: 2, shapeL: "mitt", shapeR: "mitt",
      up: 0, low: 0, droop: 0, eyeScale: 1, pupil: 1, lookX: look.x, lookY: look.y,
      browY: 0, browIn: 0, browR: 0, browShow: 1,
      mW: 5, mCurve: 3.4, mOpen: 0, cheek: 0.45, capRot: 0, tongue: 0,
    };
    const env = (u) => { u = clamp(u, 0, 1); return sin(PI * u) ** 0.7; };
    // sequência em risco (fim do dia, meta ainda não batida): parado, fica preocupadinho
    if (mood === "idle" && ctx && ctx.worried) { p.browIn = 0.6; p.browY = 0.3; p.mW = 3.8; p.mCurve = 0.6; p.cheek = 0.35; }
    // manias do parado: depois de um tempo quieto ele faz alguma coisa sozinho
    if (mood === "idle" && fid) {
      const e = env(fid.u);
      if (fid.kind === "cap") {          // ajeita o capelo
        p.hR = [lerp(p.hR[0], 74, e), lerp(p.hR[1], 34, e)]; p.shapeR = e > 0.3 ? "open" : p.shapeR; p.rotR = -40 * e; p.bendR = lerp(2, -6, e);
        p.capRot = 7 * sin(PI * clamp((fid.u - 0.3) / 0.5, 0, 1)); p.lookX = 0.7 * e; p.lookY = -0.6 * e; p.browR = 1.2 * e;
      } else if (fid.kind === "yawn") {  // espreguiça e boceja
        p.hL = [lerp(p.hL[0], 8, e), lerp(p.hL[1], 40, e)]; p.hR = [lerp(p.hR[0], 92, e), lerp(p.hR[1], 40, e)];
        p.shapeL = p.shapeR = e > 0.3 ? "fist" : "mitt"; p.bendL = p.bendR = -5 * e; p.hop = -1.6 * e;
        p.up = 0.95 * clamp((fid.u - 0.2) / 0.3, 0, 1) * (1 - clamp((fid.u - 0.8) / 0.15, 0, 1));
        p.mOpen = 1.1 * e; p.mW = lerp(5, 3.2, e); p.mCurve = lerp(3.4, 0.4, e); p.browY = -1.6 * e;
      } else if (fid.kind === "glance") { // olha de lado, desconfiado
        const side = fid.side || 1; p.lookX = side * e; p.lookY = -0.1; p.up = 0.25 * e; p.browR = 1.4 * e * (side > 0 ? 1 : 0); p.browY = -0.4 * e; p.mW = lerp(5, 3.6, e); p.mCurve = lerp(3.4, 1, e);
      } else if (fid.kind === "bounce") { // pulinho de animação
        p.hop = -3.2 * abs(sin(PI * fid.u * 2)); p.low = 0.45 * e; p.mOpen = 0.6 * e; p.mW = 5.6; p.mCurve = 4.4; p.cheek = lerp(0.45, 0.7, e);
        p.hL = [lerp(p.hL[0], 4, e), lerp(p.hL[1], 58, e)]; p.hR = [lerp(p.hR[0], 96, e), lerp(p.hR[1], 58, e)];
      }
    }
    if (mood === "happy") {
      const pump = abs(sin(t * 5.2)) * 4;
      p.hop = -abs(sin(t * 5.2)) * 5; p.tilt = sin(t * 2.6) * 2;
      p.hL = [2, 46 - pump]; p.hR = [98, 46 - pump]; p.shapeL = p.shapeR = "fist"; p.rotL = -18; p.rotR = 18; p.bendL = p.bendR = -4;
      p.low = 0.64; p.pupil = 1.25; p.browY = -2.2; p.mW = 6.4; p.mCurve = 5; p.mOpen = 1; p.cheek = 0.75;
    } else if (mood === "wave") {
      const wv = sin(t * 13);
      p.hR = [100 + wv * 2, 30 + wv * 1.2]; p.shapeR = "open"; p.rotR = -10 + wv * 26; p.bendR = -5;
      p.low = 0.32; p.browY = -1.4; p.mW = 5.6; p.mCurve = 4.4; p.mOpen = 0.55; p.cheek = 0.6; p.tilt = -1.5 + wv * 0.6;
    } else if (mood === "point") {
      p.hR = [106, 60 + sin(t * 4) * 1.2]; p.shapeR = "point"; p.rotR = -6; p.bendR = -2; p.tilt = -2;
      p.lookX = 1; p.lookY = 0; p.browY = -1.2; p.browR = 1.2; p.mW = 5; p.mCurve = 4; p.mOpen = 0.25;
    } else if (mood === "talking") {
      const wv = sin(t * 7) * 5, beat = sin(t * 3.4);
      p.tilt = beat * 3; p.hL = [3, 76]; p.hR = [97 + wv * 0.3, 52 + wv]; p.shapeR = "open"; p.rotR = beat * 18; p.bendR = -3;
      p.browY = -0.8 * Math.max(0, sin(t * 2.3)); p.mW = 4.6; p.mCurve = 2.4;
    } else if (mood === "thinking") {
      p.tilt = sin(t * 1.3) * 2; p.hL = [4, 76 + sin(t * 1.5)]; p.hR = [93, 70 + sin(t * 2.2) * 1.5]; p.shapeR = "fist"; p.bendR = 3;
      p.up = 0.15; p.browR = 1.8; p.browY = -0.4; p.mW = 3.4; p.mCurve = 1.2; p.mOpen = 0;
      // folheando: a mão direita vai até o canto da página, pega e empurra até passar da costura; a esquerda
      // recebe a folha do outro lado e alisa
      const T = turnState(t), sm = (x) => { x = clamp(x, 0, 1); return x * x * (3 - 2 * x); };
      const grab = T.cycle > 0.86 ? sm((T.cycle - 0.86) / 0.14) : T.turning ? 1 - sm((T.p - 0.5) / 0.22) : 0;
      if (grab > 0.001) {
        const ex = T.turning ? T.edgeX : 88, ey = 80 - (T.turning ? T.up * 0.8 : 0);
        p.hR = [lerp(p.hR[0], ex + 1.5, grab), lerp(p.hR[1], ey, grab)]; p.shapeR = "open"; p.rotR = lerp(0, 150, grab); p.bendR = lerp(3, -6, grab);
      }
      const catchK = T.turning ? sm((T.p - 0.62) / 0.2) : 0;
      if (catchK > 0.001) { p.hL = [lerp(p.hL[0], T.edgeX - 1.5, catchK), lerp(p.hL[1], 80 - T.up * 0.8, catchK)]; p.shapeL = "open"; p.rotL = lerp(0, -150, catchK); p.bendL = -6; }
      if (T.turning) { p.lookX = clamp((T.edgeX - 50) / 30, -1, 1); p.lookY = 0.4; p.mOpen = 0.3 * T.lift; p.mW = 3 + T.lift; p.up = 0; p.browR = 0.6; }
    } else if (mood === "surprised") {
      p.hop = -2; p.hL = [18, 72]; p.hR = [82, 72]; p.shapeL = p.shapeR = "open"; p.rotL = 60; p.rotR = -60; p.bendL = p.bendR = -4;
      p.eyeScale = 1.22; p.pupil = 1.45; p.browY = -3.6; p.mOpen = 0.9; p.mW = 3.2; p.mCurve = 0; p.cheek = 0.55;
    } else if (mood === "proud") {
      p.hop = -1.2 + sin(t * 2) * 0.5; p.tilt = -2 + sin(t * 0.9) * 0.6;
      p.hL = [9, 74]; p.hR = [91, 74]; p.shapeL = p.shapeR = "fist"; p.rotL = 150; p.rotR = -150; p.bendL = p.bendR = 6;
      p.up = 0.38; p.browY = -1.8; p.browR = 0.8; p.mW = 5.6; p.mCurve = 4.4; p.cheek = 0.65; p.lookX = -0.2; p.lookY = -0.4;
    } else if (mood === "focus") {
      const scr = sin(t * 9), sc2 = cos(t * 4.6);
      p.tilt = 1.5; p.hop = 0.6 + sin(t * 1.6) * 0.4;
      p.hR = [66 + scr * 4.5 + sin(t * 0.9) * 3, 76 + sc2 * 0.9]; p.shapeR = "pen"; p.rotR = 0; p.bendR = -4;
      p.up = 0.28; p.lookX = 0.55 + scr * 0.15; p.lookY = 0.85; p.browIn = -0.55; p.browY = 0.8;
      p.mW = 2.8; p.mCurve = 0.4; p.tongue = 1;
    } else if (mood === "sleepy") {
      p.hop = 1.4 + sin(t * 1.1) * 1.2; p.tilt = 3.5 + sin(t * 0.55) * 1.2;
      p.hL = [7, 84]; p.hR = [93, 84]; p.up = 1; p.browY = 1; p.mOpen = 0.18 + 0.12 * (0.5 + 0.5 * sin(t * 1.1)); p.mW = 2.4; p.mCurve = 0; p.cheek = 0.35;
    } else if (mood === "dizzy") {
      p.tilt = sin(t * 6) * 7; p.hop = sin(t * 3) * 1.2; p.hL = [5 + sin(t * 6) * 3, 72]; p.hR = [95 + sin(t * 6 + 1) * 3, 72];
      p.mW = 4.4; p.mCurve = 0; p.mOpen = 0; p.browY = -1; p.cheek = 0.6;
    } else if (mood === "sad") {
      p.hop = 1.6 + sin(t * 1.4) * 0.6; p.tilt = sin(t * 0.8) * 0.8;
      p.hL = [6, 85]; p.hR = [94, 85]; p.rotL = 30; p.rotR = -30; p.bendL = p.bendR = 4;
      p.up = 0.42; p.droop = 1; p.pupil = 0.9; p.lookY = 0.9;
      p.browIn = 1; p.browY = 0.6; p.mW = 4.6; p.mCurve = -3.4; p.cheek = 0.3;
    } else if (mood === "relieved") {
      // "Ufa!": de olhos fechados, passa a mão na testa e solta o ar; depois relaxa num sorriso
      const w = 1 - sstep((tm - 2.1) / 0.6), sw = 0.5 + 0.5 * sin(tm * 4.4 - 1.2);
      p.hop = lerp(-0.6, 1.4, w) + sin(t * 2) * 0.4; p.tilt = lerp(-1.5, 2.5, w);
      p.hR = [lerp(96, lerp(80, 60, sw), w), lerp(76, 50, w)]; p.shapeR = w > 0.4 ? "open" : "mitt"; p.rotR = -95 * w; p.bendR = lerp(2, -7, w);
      p.hL = [lerp(5, 8, w), lerp(76, 82, w)];
      p.up = lerp(0, 1, w); p.low = lerp(0.5, 0, w); p.browIn = 0.45 * w; p.browY = lerp(-1.2, -0.4, w);
      p.mW = lerp(5.4, 2.3, w); p.mCurve = lerp(4.2, 0, w); p.mOpen = lerp(0.3, 0.45, w); p.cheek = lerp(0.65, 0.55, w);
    } else if (mood === "present") {
      // mostrando o que tem nas mãos: a esquerda levanta o item para a frente, a direita abre a luva
      const b = sin(t * 3);
      p.hop = -1 + sin(t * 2) * 0.6; p.tilt = -2 + b * 1.2;
      p.hL = [4, 45 + b * 1.2]; p.shapeL = "fist"; p.rotL = -10; p.bendL = -4;
      p.hR = [96, 46 - b * 1.2]; p.shapeR = "open"; p.rotR = -20 + b * 12; p.bendR = -4;
      p.low = 0.4; p.browY = -1.6; p.mW = 5.8; p.mCurve = 4.6; p.mOpen = 0.5; p.cheek = 0.7; p.lookX = -0.5; p.lookY = -0.3;
    } else if (mood === "ecstatic") {
      // em chamas (muitos acertos seguidos): olhos de estrela, pulos altos e socos no ar alternados
      const a = sin(t * 6.2);
      p.hop = -abs(a) * 7; p.tilt = sin(t * 3.1) * 3;
      p.hL = [0, 40 - Math.max(0, a) * 7]; p.hR = [100, 40 - Math.max(0, -a) * 7]; p.shapeL = p.shapeR = "fist"; p.rotL = -24; p.rotR = 24; p.bendL = p.bendR = -6;
      p.pupil = 1.3; p.browY = -3; p.mW = 6.8; p.mCurve = 5.4; p.mOpen = 1.15; p.cheek = 0.85;
    }
    // segurando um item: a mão esquerda parada sobe um pouco para a frente, fechada, para o item aparecer
    if (ctx && ctx.holding && p.hL[0] < 10 && p.hL[1] > 73) { p.hL = [11, 71 + (p.hL[1] - 78) * 0.5]; p.rotL = -12; p.bendL = 4; if (p.shapeL === "mitt") p.shapeL = "fist"; }
    return p;
  }

  // ------------------------------------------------------------------ mola
  function springTo(st, key, target, dt, k) {
    const z = 0.62, v = st.v[key] || 0, x = st.x[key];
    const a = k * (target - x) - 2 * z * Math.sqrt(k) * v;
    st.v[key] = v + a * dt; st.x[key] = x + st.v[key] * dt;
  }
  const FAST = new Set(["hop", "tilt", "lookX", "lookY"]);
  const SLOW = new Set(["hL0", "hL1", "hR0", "hR1", "rotL", "rotR", "bendL", "bendR"]);

  // ------------------------------------------------------------------ desenho
  function drawFolha(g, s) {
    const { t, mood, P, small } = s;
    const hop = P.hop, tilt = P.tilt;
    const squashX = 1 + 0.10 * s.squash, squashY = 1 - 0.12 * s.squash;
    const stretch = 1 + clamp(abs(s.vy) * 0.006, 0, 0.07);

    // sombra: menor quando ele sobe
    const lift = Math.max(-hop, 0);
    g.save();
    g.translate(50, 95); g.scale(1, 0.18); g.translate(-50, -95);
    const r1 = 36 - lift * 1.6;
    let rg = g.createRadialGradient(50, 95, 0, 50, 95, r1); rg.addColorStop(0, rgba(INK, 0.28)); rg.addColorStop(1, rgba(INK, 0));
    circle(g, rg, r1, 50, 95);
    rg = g.createRadialGradient(50, 95, 0, 50, 95, 26); rg.addColorStop(0, rgba(INK, 0.30 * clamp(1 - lift / 6, 0.3, 1))); rg.addColorStop(1, rgba(INK, 0));
    circle(g, rg, 26, 50, 95);
    g.restore();

    const lookP = s.look || resolveLook(null);
    drawPet(g, t, lookP.pet, mood, small, s.burst || 0);
    g.save();
    g.translate(0, hop);
    g.translate(50, 88); g.scale(squashX / Math.sqrt(stretch), squashY * stretch); g.translate(-50, -88);
    g.translate(50, 80); g.rotate(tilt * PI / 180); g.translate(-50, -80);
    // quem fica na frente do livro: as duas mãos folheando ou com a cara de susto; só a do lápis no foco
    const look = s.look || resolveLook(null);
    drawAura(g, t, look.aura, small, s.burst || 0, true);
    // na frente do livro: as duas mãos folheando, no susto ou passando na testa (ufa); só a do lápis no foco
    let front = mood === "thinking" || mood === "surprised" || mood === "present" ? "LR" : mood === "focus" || mood === "relieved" ? "R" : "";
    // segurando um item: a mão esquerda fica na frente do livro, para o item aparecer
    if (look.hand && look.hand !== "nenhum" && !front.includes("L")) front = "L" + front;
    drawArms(g, s, "LR".replace(new RegExp("[" + (front || "_") + "]", "g"), ""));
    drawBook(g, t, mood, small, look);
    drawPageLines(g, t, mood, small);
    if (!small) drawDogEar(g, t, mood);
    if (mood === "thinking") drawTurningPage(g, t);
    drawFace(g, s);
    drawEyewear(g, P, small, look.eyes, t);
    drawNeckwear(g, t, small, look.neck);
    // à noite, parado ou cochilando, a touca entra no lugar do capelo (outro chapéu escolhido fica)
    const nightcap = look.night && look.hat === "capelo" && (mood === "idle" || mood === "sleepy");
    g.save(); g.translate(50, 38); g.rotate((P.capRot || 0) * PI / 180); g.translate(-50, -38);
    if (nightcap) drawNightcap(g, t, tilt, s.vy); else drawHat(g, s, look, tilt);
    g.restore();
    if (front) drawArms(g, s, front);
    if (mood === "relieved" && !small) drawRelief(g, s.tm || 0);
    if (look.atRisk && mood === "idle" && !small) drawSweat(g, t);
    g.restore();
    if (!small && look.morning && /^(idle|talking|thinking|focus|wave|point|proud)$/.test(mood)) drawMug(g, t);
    if (!small && look.night && /^(idle|sleepy|focus|thinking)$/.test(mood)) drawMoon(g, t);
    if (mood === "happy") drawCelebration(g, t, small);
    if (mood === "ecstatic") { drawFlames(g, t, small); drawCelebration(g, t, small); }
    if (mood === "proud") drawSparkles(g, t, small);
    drawAura(g, t, look.aura, small, s.burst || 0, false);
    drawBurst(g, s.burst || 0);
    if (mood === "sleepy") drawZzz(g, t, small);
    if (mood === "dizzy") drawDizzyStars(g, t);
    if (mood === "focus" && !small) drawFocusMarks(g, t);
  }

  function drawCorners(g, metal) {
    const stops = metal === "silver" ? ["#FFFFFF", "#CDD0DC", "#8E92A6"] : ["#FFE3A0", GOLD, "#C98A12"];
    const corner = (x, y, sx, sy) => {
      const p = new Path2D();
      p.moveTo(x, y + sy * 6.5); p.lineTo(x, y + sy * 1.2); p.quadraticCurveTo(x, y, x + sx * 1.2, y); p.lineTo(x + sx * 6.5, y);
      p.lineTo(x + sx * 6.5, y + sy * 1.9); p.lineTo(x + sx * 1.9, y + sy * 1.9); p.lineTo(x + sx * 1.9, y + sy * 6.5); p.closePath();
      g.fillStyle = vGrad(g, stops, Math.min(y, y + sy * 6.5), Math.max(y, y + sy * 6.5)); g.fill(p);
      g.strokeStyle = "rgba(120,70,0,0.35)"; g.lineWidth = 0.3; g.stroke(p);
      circle(g, "rgba(255,255,255,0.75)", 0.45, x + sx * 0.95, y + sy * 0.95);
    };
    corner(8.5, 43.5, 1, 1); corner(91.5, 43.5, -1, 1); corner(8.5, 89, 1, -1); corner(91.5, 89, -1, -1);
  }
  function drawDogEar(g, t, mood) {
    const k = mood === "thinking" ? 0.7 + 0.3 * abs(sin(t * 1.2)) : 1;
    const x = 87.7, y = 84.4, d = 5 * k;
    const under = new Path2D(); under.moveTo(x - d, y + 0.1); under.lineTo(x + 0.2, y - d - 0.1); under.lineTo(x + 0.2, y + 0.1); under.closePath();
    g.fillStyle = PAPER_EDGE; g.fill(under);
    const fold = new Path2D(); fold.moveTo(x - d, y + 0.1); fold.lineTo(x + 0.2, y - d - 0.1); fold.lineTo(x - d * 0.8, y - d * 0.78); fold.closePath();
    g.save(); g.shadowColor = "rgba(38,33,92,0.25)"; g.shadowBlur = 1.2; g.shadowOffsetX = -0.4; g.shadowOffsetY = 0.4;
    g.fillStyle = hGrad(g, ["#FFFFFF", "#ECE6DA"], x - d, x); g.fill(fold); g.restore();
    stroke(g, fold, rgba(GUTTER, 0.18), 0.25);
  }

  // Textura da capa (só aparece nas bordas e no pé do livro, onde a capa fica à mostra).
  function drawCoverPattern(g, t, C, cover) {
    const pat = C.pattern;
    if (pat === "leather" || pat === "denim") {
      if (pat === "leather") for (const s of GRAIN) circle(g, `rgba(40,18,4,${s.a * 1.3})`, s.r * 1.3, s.x < 50 ? 8.5 + (s.x - 15) * 0.1 : 91.5 - (85 - s.x) * 0.1, s.y + 6);
      else for (let x = -50; x < 100; x += 0.9) line(g, "rgba(255,255,255,0.07)", x, 40, x + 54, 95, 0.25, false);
    } else if (pat === "plaid") {
      for (let x = 4; x < 100; x += 6) { line(g, "rgba(20,10,10,0.28)", x, 40, x, 95, 1.6, false); line(g, "rgba(255,230,160,0.35)", x + 2.5, 40, x + 2.5, 95, 0.3, false); }
      for (let y = 42; y < 95; y += 6) { line(g, "rgba(20,10,10,0.28)", 0, y, 100, y, 1.6, false); line(g, "rgba(255,230,160,0.35)", 0, y + 2.5, 100, y + 2.5, 0.3, false); }
    } else if (pat === "dots") {
      for (let y = 44; y < 94; y += 3.2) for (let x = (y % 6.4 < 3.2 ? 8.6 : 10.2); x < 92; x += 3.2) circle(g, "rgba(255,255,255,0.75)", 0.55, x, y);
    } else if (pat === "holo") {
      const h = (t * 40) % 360, hg = g.createLinearGradient(0, 40, 100, 95);
      for (let i = 0; i <= 4; i++) hg.addColorStop(i / 4, `hsla(${(h + i * 72) % 360},90%,72%,0.55)`);
      g.fillStyle = hg; g.fill(cover);
    } else if (pat === "foil") {
      for (let x = -50; x < 100; x += 7) line(g, "rgba(245,184,61,0.45)", x, 40, x + 54, 95, 0.25, false);
    } else if (pat === "facets") {
      for (let x = -50; x < 100; x += 5) { line(g, "rgba(255,255,255,0.35)", x, 40, x + 30, 95, 0.3, false); line(g, "rgba(80,130,180,0.25)", x + 30, 40, x, 95, 0.3, false); }
    } else for (let x = -50; x < 100; x += 1.5) { line(g, "rgba(255,255,255,0.045)", x, 40, x + 54, 95, 0.3, false); line(g, "rgba(0,0,0,0.06)", x + 54, 40, x, 95, 0.3, false); }
    if (C.stitch) {   // costura pontilhada rente à borda
      g.save(); g.setLineDash([1.1, 0.9]);
      const st = new Path2D();
      st.moveTo(10.2, 45.8); st.lineTo(10.2, 87.4); st.bezierCurveTo(22, 85.3, 38, 86.2, 50, 91.4); st.bezierCurveTo(62, 86.2, 78, 85.3, 89.8, 87.4); st.lineTo(89.8, 45.8);
      stroke(g, st, C.stitch, 0.35); g.restore();
    }
    if (pat === "stars") for (const [x, y, r] of [[9.8, 50, 0.35], [10.6, 63, 0.25], [9.6, 76, 0.4], [90.2, 55, 0.3], [89.6, 70, 0.4], [90.4, 82, 0.25], [24, 88.6, 0.3], [40, 90.4, 0.35], [62, 90.2, 0.3], [77, 88.4, 0.35]]) {
      circle(g, `rgba(255,248,214,${0.55 + 0.45 * abs(sin(t * 1.3 + x))})`, r, x, y);
    }
    if (pat === "shine" || pat === "facets" || pat === "foil") {   // um brilho passando de tempos em tempos
      const sx = -30 + ((t * 0.32) % 1) * 170, sh = g.createLinearGradient(sx, 40, sx + 18, 95);
      sh.addColorStop(0, "rgba(255,255,255,0)"); sh.addColorStop(0.5, `rgba(255,255,255,${pat === "shine" ? 0.55 : 0.4})`); sh.addColorStop(1, "rgba(255,255,255,0)");
      g.fillStyle = sh; g.fill(cover);
    }
  }

  function drawBook(g, t, mood, small, look) {
    const cover = coverPath(), C = look.cover;
    // contorno macio: claro por fora (fundo escuro) e tinta por dentro (fundo claro)
    stroke(g, cover, "rgba(255,255,255,0.28)", small ? 3 : 2);
    stroke(g, cover, rgba(C.under, 0.85), small ? 1.6 : 1);
    g.save(); g.translate(0, 1.7); g.fillStyle = C.under; g.fill(cover); g.restore();
    g.fillStyle = vGrad(g, C.c, 41, 93); g.fill(cover);
    const lg = g.createLinearGradient(8, 44, 40, 70); lg.addColorStop(0, "rgba(255,255,255,0.14)"); lg.addColorStop(1, "rgba(255,255,255,0)");
    g.fillStyle = lg; g.fill(cover);
    if (!small) {
      g.save(); g.clip(cover);
      drawCoverPattern(g, t, C, cover);
      g.restore();
    }
    for (const side of [-1, 1]) {
      const ed = new Path2D(), x0 = 50 + side * 41.5;
      ed.moveTo(x0, 43.5); ed.lineTo(x0 + side * 1.6, 44.4); ed.lineTo(x0 + side * 1.6, 88.2); ed.lineTo(x0, 89); ed.closePath();
      g.fillStyle = vGrad(g, [C.c[2], C.under], 44, 89); g.fill(ed);
    }
    if (!small) drawCorners(g, C.corner);
    drawRibbon(g, t, mood, look.ribbon);

    for (const side of [-1, 1]) {
      const block = pageBlock(side, 3.6);
      g.fillStyle = vGrad(g, [PAPER_EDGE, PAPER_EDGE_DARK], 80, 91); g.fill(block);
      if (!small) {
        for (let i = 1; i <= 6; i++) {
          const d = i * 0.52, p = new Path2D();
          p.moveTo(50 + side * (38 + d * 0.45), 84.4 + d);
          p.bezierCurveTo(50 + side * 26.2, 80.6 + d, 50 + side * 10, 81.4 + d, 50, 87 + d);
          stroke(g, p, "rgba(140,130,112,0.22)", 0.28);
        }
        for (let i = 1; i <= 5; i++) { const x = 50 + side * (38 + i * 0.3); line(g, "rgba(140,130,112,0.18)", x, 41 + i * 0.3, x, 84.4 + i * 0.9, 0.25, false); }
      }
    }
    const left = page(-1), right = page(1);
    g.fillStyle = hGrad(g, [PAPER_MID, PAPER, PAPER, PAPER_MID], 12, 50); g.fill(left);
    g.fillStyle = hGrad(g, [PAPER_MID, PAPER, PAPER, "#F1EDE6"], 50, 88); g.fill(right);
    const gutter = [[0, rgba(GUTTER, 0)], [0.55, rgba(GUTTER, 0.035)], [0.85, rgba(GUTTER, 0.10)], [1, rgba(GUTTER, 0.20)]];
    g.fillStyle = hGrad(g, gutter, 39, 50); g.fill(left);
    g.fillStyle = hGrad(g, gutter, 61, 50); g.fill(right);
    if (!small) for (const s of GRAIN) {
      if (s.long) line(g, `rgba(154,143,122,${s.a})`, s.x, s.y, s.x + 1.4, s.y + 0.3, 0.18);
      else circle(g, `rgba(154,143,122,${s.a})`, s.r, s.x, s.y);
    }
    g.fillStyle = hGrad(g, [rgba(GUTTER, 0.06), rgba(GUTTER, 0)], 12, 17); g.fill(left);
    g.fillStyle = hGrad(g, [rgba(GUTTER, 0), rgba(GUTTER, 0.08)], 83, 88); g.fill(right);
    line(g, rgba(GUTTER, 0.16), 50, 44.3, 50, 86.8, 0.35, false);
    if (!small) { oval(g, "#C0392B", 48.6, 43.2, 2.8, 1.6); line(g, "rgba(255,230,170,0.85)", 48.9, 43.9, 51.1, 43.9, 0.35, false); }
    for (const side of [-1, 1]) {
      const rim = new Path2D();
      rim.moveTo(50 + side * 3, 41.9); rim.bezierCurveTo(50 + side * 12, 37.9, 50 + side * 26, 36.9, 50 + side * 36.5, 40.2);
      stroke(g, rim, "rgba(255,255,255,0.9)", 0.5);
    }
  }

  function drawTurningPage(g, t) {
    const T = turnState(t);
    if (!T.turning) return;
    const { p, c, lift, edgeX, up } = T, w = edgeX - 50, dir = c >= 0 ? 1 : -1;
    // o canto de baixo (onde a mão segura) vai adiantado: a folha enrola em diagonal
    const lead = lift * 7 * (c >= 0 ? -1 : 1) * 0.6, curl = lift * 3.2;
    const sheet = new Path2D();
    sheet.moveTo(50, 44);
    sheet.bezierCurveTo(50 + w * 0.28, 37.6 - up * 0.7, 50 + w * 0.7, 36.4 - up, edgeX - lead * 0.4, 40.4 - up * 0.8);
    sheet.quadraticCurveTo(edgeX + dir * 2.2 * lift - lead * 0.2, 62 - up, edgeX + lead, 84.4 - up * 0.8 - curl);
    sheet.bezierCurveTo(50 + w * 0.7 + lead * 0.5, 80.6 - up - curl * 0.5, 50 + w * 0.28, 81.4 - up * 0.5, 50, 87);
    sheet.closePath();
    g.save(); g.translate(dir * 2.5 * lift, 2.2 * lift); g.fillStyle = rgba(GUTTER, 0.13 * lift); g.fill(sheet); g.restore();
    const front = c >= 0, facing = abs(c), base = front ? PAPER : "#F2EEE6";
    if (abs(w) > 0.6) {
      g.fillStyle = hGrad(g, [lerpColor(base, GUTTER, 0.16 + 0.1 * lift), lerpColor(base, GUTTER, 0.05 * (1 - facing)), lerpColor(base, "#FFFFFF", 0.5 * facing)], 50, edgeX);
      g.fill(sheet);
      if (front) for (const [y, len] of [[45, 0.5], [49, 0.7], [79.5, 0.45]]) {
        const yy = y - up * 0.85;
        line(g, rgba(PAGE_LINE, facing), 50 + w * 0.88, yy, 50 + w * (0.88 - len), yy + 1.5 * (len - 0.4), 1.6 * (0.5 + 0.5 * facing));
      }
    }
    line(g, "rgba(255,255,255,0.8)", edgeX - lead * 0.4, 40.6 - up * 0.8, edgeX + dir * 1.6 * lift, 62 - up, 0.45);
    stroke(g, sheet, rgba(GUTTER, 0.18), 0.25);
    // dobrinha do canto enrolado (verso aparecendo)
    if (lift > 0.15) {
      const cx = edgeX + lead, cy = 84.4 - up * 0.8 - curl, k = lift * 4;
      const cu = new Path2D(); cu.moveTo(cx, cy); cu.lineTo(cx - dir * k * 0.9, cy + k * 0.15); cu.quadraticCurveTo(cx - dir * k * 0.3, cy - k * 0.5, cx - dir * k * 0.15, cy - k * 1.1); cu.closePath();
      g.fillStyle = "#EFEAE0"; g.fill(cu); stroke(g, cu, rgba(GUTTER, 0.2), 0.25);
    }
  }

  function drawPageLines(g, t, mood, small) {
    const writing = mood === "thinking", cycle = (t % TURN_CYCLE) / TURN_CYCLE, lw = small ? 2.4 : 1.6;
    const ln = (x0, y0, len, slope, index, mirror) => {
      const p = !writing ? 1 : clamp((cycle - TURN_SHARE) / (1 - TURN_SHARE) * 6 - index, 0, 1);
      if (p <= 0) return;
      const x1 = mirror ? x0 - len * p : x0 + len * p;
      line(g, PAGE_LINE, x0, y0, x1, y0 + slope * len * p, lw);
    };
    if (!small) {
      const hl = !writing ? 1 : clamp((cycle - TURN_SHARE) / (1 - TURN_SHARE) * 6 - 1.5, 0, 1);
      if (hl > 0) { g.save(); g.globalAlpha = 0.55; line(g, "#FFE36B", 16.6, 49.4, 16.6 + 24 * hl, 49.4 + 2.9 * hl, 3.2); g.restore(); }
      if (hl >= 1) {
        const c = new Path2D(); c.moveTo(78.6, 74.4); c.lineTo(79.8, 75.8); c.lineTo(82.4, 72.6); stroke(g, c, "#3FBF8F", 0.9);
        const sx = 21, sy = 54.6, sp = new Path2D();
        sp.moveTo(sx, sy - 1.6); sp.quadraticCurveTo(sx, sy, sx + 1.6, sy); sp.quadraticCurveTo(sx, sy, sx, sy + 1.6); sp.quadraticCurveTo(sx, sy, sx - 1.6, sy); sp.quadraticCurveTo(sx, sy, sx, sy - 1.6);
        g.fillStyle = "rgba(245,184,61,0.85)"; g.fill(sp);
      }
    }
    ln(17, 45, 20, 0.1, 0, false); ln(17, 49, 26, 0.12, 1, false);
    ln(83, 45, 20, 0.1, 2, true); ln(83, 49, 26, 0.12, 3, true);
    if (!small) { ln(17, 79.5, 18, -0.03, 4, false); ln(83, 79.5, 18, -0.03, 5, true); }
  }

  // ---- rosto
  function drawFace(g, s) {
    const { t, mood, P, blink, wink, small } = s;
    const gulp = s.gulp || 0;
    const eyeL = [31 + P.lookX * 1.6, 61 + P.lookY * 1.4], eyeR = [69 + P.lookX * 1.6, 61 + P.lookY * 1.4];
    oval(g, rgba(CHEEK, P.cheek), 19, 67, 9, 5);
    oval(g, rgba(CHEEK, P.cheek), 72, 67, 9, 5);
    const eye = ([x, y], side, extraLid) => {
      if (mood === "dizzy") {           // espiral girando
        const sp = new Path2D(), a0 = t * 7 * side;
        for (let i = 0; i <= 40; i++) { const a = a0 + i * 0.42, rr = 0.25 + i * 0.12; i ? sp.lineTo(x + cos(a) * rr, y + sin(a) * rr * 1.15) : sp.moveTo(x + cos(a) * rr, y + sin(a) * rr * 1.15); }
        stroke(g, sp, INK, small ? 1.6 : 1.2); return;
      }
      if (mood === "ecstatic" && blink < 0.5) {   // olhos de estrela, pulsando
        const st = star5(g, x, y, (small ? 6.4 : 5.8) * (1 + 0.1 * sin(t * 9)));
        g.fillStyle = vGrad(g, ["#FFF1BF", GOLD, "#E39A1E"], y - 6, y + 5); g.fill(st);
        g.strokeStyle = INK; g.lineWidth = small ? 1.2 : 0.8; g.lineJoin = "round"; g.stroke(st);
        if (!small) circle(g, "rgba(255,255,255,0.9)", 0.9, x - 1.2, y - 1.6);
        return;
      }
      const rx = 4.4 * P.eyeScale * (small ? 1.18 : 1), ry = 5.8 * P.eyeScale * (small ? 1.1 : 1);
      const up = clamp(Math.max(P.up, blink, extraLid), 0, 1), low = clamp(P.low * (1 - blink), 0, 1);
      if (small && low > 0.45 && blink < 0.5) {   // pequeno e feliz: o arco clássico (^^) lê melhor
        const p = new Path2D(); p.moveTo(x - 4.5, y + 1.5); p.quadraticCurveTo(x, y - 5, x + 4.5, y + 1.5); stroke(g, p, INK, 2.8);
        return;
      }
      if (up > 0.96) {         // fechado: só o traço
        const p = new Path2D(); p.moveTo(x - 4.4, y + 0.4); p.quadraticCurveTo(x, y + 2.4, x + 4.4, y + 0.4); stroke(g, p, INK, small ? 2.2 : 1.8);
        return;
      }
      g.save();
      g.beginPath(); g.ellipse(x, y, rx, ry, 0, 0, 2 * PI); g.fillStyle = INK; g.fill();
      if (up < 0.85 && low < 0.9) {
        const pr = P.pupil;
        circle(g, "#FFFFFF", 1.7 * pr, x - 1.4 + P.lookX * 0.6, y - 2.2);
        circle(g, "rgba(255,255,255,0.7)", 0.8 * pr, x + 1.6, y + 1.8);
      }
      // pálpebras (cor do papel, cobrindo o olho como uma cortina; a borda é um traço de tinta)
      g.beginPath(); g.ellipse(x, y, rx + 0.6, ry + 0.6, 0, 0, 2 * PI); g.clip();
      if (up > 0.01) {
        const yl = y - ry - 0.8 + (2 * ry + 0.8) * up, dr = P.droop * 2.6;
        const yo = yl + dr, yi = yl - dr * 0.4;                  // caído no canto de fora
        const xa = x - rx - 1, xb = x + rx + 1;
        const ya = side < 0 ? yo : yi, yb = side < 0 ? yi : yo;
        const mid = (ya + yb) / 2 + 1.4 * (1 - up);
        const lid = new Path2D(); lid.moveTo(xa, y - ry - 2); lid.lineTo(xb, y - ry - 2); lid.lineTo(xb, yb); lid.quadraticCurveTo(x, mid, xa, ya); lid.closePath();
        g.fillStyle = PAPER; g.fill(lid);
        const e = new Path2D(); e.moveTo(xa, ya); e.quadraticCurveTo(x, mid, xb, yb); stroke(g, e, INK, 1.2);
      }
      if (low > 0.01) {
        // de baixo (feliz): sobe curvada e deixa o olho em meia-lua
        const yl = y + ry + 0.8 - (2 * ry + 0.8) * low;
        const lid = new Path2D(); lid.moveTo(x - rx - 1, y + ry + 2); lid.lineTo(x + rx + 1, y + ry + 2); lid.lineTo(x + rx + 1, yl + 2.4 * low); lid.quadraticCurveTo(x, yl - 3.2 * low, x - rx - 1, yl + 2.4 * low); lid.closePath();
        g.fillStyle = PAPER; g.fill(lid);
      }
      g.restore();
    };
    eye(eyeL, -1, 0);
    eye(eyeR, 1, wink);
    // sobrancelhas: sobem e descem; browIn levanta a ponta de dentro (triste); browR ergue só a direita
    // pequeno, a sobrancelha só aparece quando muda a expressão (neutra, ela só embola o rosto)
    const browA = small ? clamp((abs(P.browY) + P.browIn * 2 + P.browR) / 1.2 - 0.3, 0, 1) : P.browShow;
    if (browA > 0.05) for (const [[x, y], side] of [[eyeL, -1], [eyeR, 1]]) {
      const by = y - 9.8 + P.browY - (side > 0 ? P.browR : 0) + (blink > 0.5 ? 0.6 : 0);
      const inner = [x - side * 4.2, by - P.browIn * 2.2 + (1 - P.browIn) * 0.4], outer = [x + side * 4.4, by + P.browIn * 1.2];
      const b = new Path2D(); b.moveTo(outer[0], outer[1]); b.quadraticCurveTo(x, by - 1.4 + P.browIn * 0.6, inner[0], inner[1]);
      stroke(g, b, rgba(INK, 0.9 * browA), small ? 2.2 : 1.7);
    }
    // lágrima (triste)
    if (mood === "sad" && !small) {
      const tp = (t % 3.2) / 3.2;
      if (tp < 0.8) {
        const ty = 64.5 + tp * 9, ta = tp < 0.1 ? tp / 0.1 : 1 - Math.max(0, tp - 0.6) / 0.2;
        const d = new Path2D(); d.moveTo(36.2, ty - 2.2); d.quadraticCurveTo(38.2, ty + 0.4, 36.2, ty + 1.2); d.quadraticCurveTo(34.2, ty + 0.4, 36.2, ty - 2.2);
        g.fillStyle = "rgba(126,200,255," + (0.85 * ta) + ")"; g.fill(d); circle(g, "rgba(255,255,255," + (0.8 * ta) + ")", 0.35, 35.8, ty - 0.2);
      }
    }
    // boca: largura, curva (sorri > 0, triste < 0) e abertura, tudo por mola
    const mx = 50, my = 73;
    if (gulp > 0.08) {
      const r = 1.5 + 3.2 * gulp;
      oval(g, INK, mx - r, my - r * 1.1, r * 2, r * 2.2);
      for (let i = 0; i < 4; i++) { const a = i * (PI / 2) + 0.5; circle(g, rgba(SPARK, gulp), 1.8 * gulp * 0.9, 50 + cos(a) * (22 + 14 * (1 - gulp)), 58 + sin(a) * (16 + 10 * (1 - gulp))); }
      return;
    }
    let open = P.mOpen, w = P.mW, c = P.mCurve;
    if (mood === "talking") {
      if (s.speech) { open = s.speech.open; w = 4.6 - 1.7 * s.speech.round; c = 2.4 * (1 - s.speech.round); }   // boca no ritmo do texto
      else { open = (0.5 + 0.5 * sin(t * 13)) * (0.55 + 0.45 * abs(sin(t * 3.1))); w = 4.2 + 0.8 * open; }
    }
    if (mood === "surprised") {         // "Oh!"
      oval(g, INK, mx - 2.6, my - 3, 5.2, 6.4); oval(g, CHEEK, mx - 1.6, my + 0.9, 3.2, 1.8); return;
    }
    if (mood === "dizzy") {             // boca ondulada
      const wv = new Path2D(); for (let i = 0; i <= 12; i++) { const x = mx - 5 + i * (10 / 12), y = my + sin(i * 1.4 + t * 8) * 0.9; i ? wv.lineTo(x, y) : wv.moveTo(x, y); }
      stroke(g, wv, INK, small ? 2.4 : 1.8); return;
    }
    if (mood === "sad") c += sin(t * 9) * 0.35;
    const corner = my - c * 0.3;
    if (open < 0.12) {
      const p = new Path2D(); p.moveTo(mx - w, corner); p.quadraticCurveTo(mx, corner + c * 1.6, mx + w, corner);
      stroke(g, p, INK, small ? 2.8 : 2.3);
      if (P.tongue > 0.5) { oval(g, CHEEK, mx + w - 2.4, corner - 0.2, 2.6, 2.2); stroke(g, p, INK, small ? 2.8 : 2.3); }   // língua no canto (concentrado)
    } else {
      const top = corner + c * 0.15 - open * 0.6, depth = Math.max(1.4, c * 1.1) + open * 5.4;
      const p = new Path2D(); p.moveTo(mx - w, corner); p.quadraticCurveTo(mx, top, mx + w, corner); p.bezierCurveTo(mx + w * 0.95, corner + depth * 1.25, mx - w * 0.95, corner + depth * 1.25, mx - w, corner); p.closePath();
      g.fillStyle = INK; g.fill(p);
      g.save(); g.clip(p); oval(g, CHEEK, mx - w * 0.55, corner + depth - 1.6 * open - 0.6, w * 1.1, 3.4 * open + 1); g.restore();
    }
  }

  function drawRibbon(g, t, mood, colors) {
    const cs = colors || RIBBONS.menta, [dark, base, light] = cs;
    const sad = mood === "sad", sway = sad ? 0 : sin(t * 1.9 + 0.6) * 1.3;
    const w = 3.4, x0 = 51.4, lieTop = 86, edge = 94.6, hang = sad ? 3.2 : 5.4;
    const tipY = edge + hang, tipX = x0 + 0.5 + sway;
    const r = new Path2D();
    r.moveTo(x0, lieTop); r.lineTo(x0 + w, lieTop); r.lineTo(x0 + w + 0.3, edge);
    r.quadraticCurveTo(x0 + w + 0.3 + sway * 0.3, edge + hang * 0.5, tipX + w, tipY);
    r.lineTo(tipX + w / 2, tipY - 1.6); r.lineTo(tipX, tipY);
    r.quadraticCurveTo(x0 + 0.3 + sway * 0.3, edge + hang * 0.5, x0 + 0.3, edge);
    r.closePath();
    g.save(); g.translate(0.7, 0.5); g.fillStyle = "rgba(14,10,69,0.35)"; g.fill(r); g.restore();
    if (cs.length === 4) { g.fillStyle = vGrad(g, cs, lieTop, tipY); g.fill(r); g.fillStyle = hGrad(g, ["rgba(0,0,0,0.12)", "rgba(255,255,255,0.25)", "rgba(0,0,0,0.08)"], x0, x0 + w + 0.6 + sway); g.fill(r); }
    else { g.fillStyle = hGrad(g, [dark, base, light, base], x0, x0 + w + 0.6 + sway); g.fill(r); }
    g.fillStyle = vGrad(g, ["rgba(14,10,69,0.45)", "rgba(14,10,69,0)"], 88, 91.5); g.fillRect(x0, 88, w + 0.3, 3.5);
    line(g, "rgba(255,255,255,0.55)", x0 + 0.5, edge - 0.4, x0 + w, edge - 0.4, 0.5, false);
    g.fillStyle = vGrad(g, [rgba(dark, 0.55), rgba(dark, 0)], edge, edge + 1.8); g.fillRect(x0 + 0.3, edge, w, 1.8);
  }

  // ---- companheiro: um bichinho (ou plantinha) no chão, ao lado esquerdo do Folha
  // Fica no chão (não pula junto); comemora junto, cochila junto e pula quando tocam no Folha.
  function drawPet(g, t, kind, mood, small, burst) {
    if (!kind || kind === "nenhum" || small) return;   // pequeno demais, só atrapalharia
    const party = /^(happy|ecstatic|proud|wave|present)$/.test(mood), sleepy = mood === "sleepy";
    const hop = party ? -abs(sin(t * 5.2 + 0.8)) * 3 : burst > 0 ? -sin(PI * (1 - burst)) * 4 : -abs(sin(t * 1.3)) * 0.4;
    const blink = sleepy || (t % 3.7) < 0.13;
    const x = -9, base = 95, k = 1.45;
    g.save(); g.translate(x, base); g.scale(1, 0.22); circle(g, rgba(INK, 0.2), (7 + hop * 0.3) * k, 0, 0); g.restore();
    g.save(); g.translate(x, base + hop * k); g.scale(k, k);
    const eye = (ex, ey, r = 0.75) => {
      if (blink) { const p = new Path2D(); p.moveTo(ex - r * 1.2, ey); p.quadraticCurveTo(ex, ey + r * 0.9, ex + r * 1.2, ey); stroke(g, p, INK, 0.5); return; }
      circle(g, INK, r, ex, ey); circle(g, "#FFFFFF", r * 0.4, ex - r * 0.3, ey - r * 0.35);
    };
    const smile = (mx, my, w = 1.1) => { const p = new Path2D(); p.moveTo(mx - w, my); p.quadraticCurveTo(mx, my + w * (party ? 1.2 : 0.7), mx + w, my); stroke(g, p, INK, 0.45); };
    if (kind === "planta") {
      // vaso de barro com folhas que balançam (e uma flor quando ele comemora)
      const sway = sin(t * 1.4) * 0.15;
      for (const [a, len, c] of [[-0.55, 9, "#2E9E72"], [0.05, 11, "#3FBF8F"], [0.6, 8.5, "#2E9E72"]]) {
        g.save(); g.translate(0, -8); g.rotate(a + sway);
        line(g, "#2E9E72", 0, 0, 0, -len * 0.7, 0.6);
        g.beginPath(); g.ellipse(0, -len * 0.62, 2, len * 0.4, 0, 0, 2 * PI); g.fillStyle = c; g.fill();
        line(g, "rgba(255,255,255,0.35)", 0, -len * 0.3, 0, -len * 0.9, 0.3);
        g.restore();
      }
      if (party) { g.save(); g.translate(sin(t * 1.4) * 1.2, -19); for (let i = 0; i < 5; i++) { const a = i * 2 * PI / 5 + t; circle(g, "#FF8FA3", 1.1, cos(a) * 1.3, sin(a) * 1.3); } circle(g, GOLD, 0.8, 0, 0); g.restore(); }
      const pot = new Path2D(); pot.moveTo(-5, -7); pot.lineTo(5, -7); pot.lineTo(4, 0); pot.lineTo(-4, 0); pot.closePath();
      g.fillStyle = hGrad(g, ["#E8895A", "#D9774B", "#B85A33"], -5, 5); g.fill(pot);
      const rim = new Path2D(); rim.roundRect(-5.8, -8.6, 11.6, 2.2, 0.8); g.fillStyle = "#C4643A"; g.fill(rim);
      eye(-1.8, -4.2, 0.6); eye(1.8, -4.2, 0.6); smile(0, -2.6, 0.9);
      circle(g, rgba(CHEEK, 0.6), 0.7, -3, -3); circle(g, rgba(CHEEK, 0.6), 0.7, 3, -3);
    } else if (kind === "patinho") {
      // patinho de borracha, balançando como na água
      g.rotate(sin(t * 2) * 0.06);
      const body = new Path2D(); body.ellipse(0, -4.4, 6.2, 4.3, 0, 0, 2 * PI); g.fillStyle = vGrad(g, ["#FFE27A", "#FFD84A", "#F0B400"], -9, 0); g.fill(body);
      const tail = new Path2D(); tail.moveTo(-5.4, -6); tail.lineTo(-7.6, -9); tail.lineTo(-4.2, -7.6); tail.closePath(); g.fillStyle = "#F0B400"; g.fill(tail);
      const wing = new Path2D(); wing.moveTo(-2.6, -5.4); wing.quadraticCurveTo(0.6, -3, 2.4, -5); stroke(g, wing, "#E0A000", 0.6);
      circle(g, "#FFD84A", 3.5, 2.6, -10.2);
      g.beginPath(); g.ellipse(6, -9.6, 2, 1, 0.1, 0, 2 * PI); g.fillStyle = "#FF8A2B"; g.fill();
      eye(3.6, -11.1, 0.7); circle(g, rgba(CHEEK, 0.7), 0.8, 2.2, -9.2);
      g.beginPath(); g.ellipse(1, -12.4, 1.2, 0.6, -0.4, 0, 2 * PI); g.fillStyle = "rgba(255,255,255,0.6)"; g.fill();
    } else if (kind === "gatinho") {
      // gatinho sentado, rabo abanando e bigode
      const tw = sin(t * 2.2) * 2;
      const tail = new Path2D(); tail.moveTo(4, -2); tail.quadraticCurveTo(10, -3 + tw * 0.3, 8.5 + tw * 0.4, -10 + tw); stroke(g, tail, "#D9803A", 1.8);
      const body = new Path2D(); body.ellipse(0, -4.6, 5, 4.8, 0, 0, 2 * PI); g.fillStyle = vGrad(g, ["#F7B36E", "#E58F45"], -9, 0); g.fill(body);
      g.beginPath(); g.ellipse(0, -3.6, 2.6, 3, 0, 0, 2 * PI); g.fillStyle = "#FFE7CF"; g.fill();
      for (const s of [-1, 1]) { const ear = new Path2D(); ear.moveTo(s * 3.8, -14.4); ear.lineTo(s * 2.6, -18.4); ear.lineTo(s * 0.6, -15.4); ear.closePath(); g.fillStyle = "#E58F45"; g.fill(ear);
        const inner = new Path2D(); inner.moveTo(s * 3, -14.8); inner.lineTo(s * 2.5, -17); inner.lineTo(s * 1.3, -15.3); inner.closePath(); g.fillStyle = "#FFB3C7"; g.fill(inner); }
      circle(g, "#F7B36E", 4.5, 0, -12);
      line(g, "rgba(200,110,40,0.6)", -1, -16, -1, -14.6, 0.5); line(g, "rgba(200,110,40,0.6)", 1, -16, 1, -14.6, 0.5);
      eye(-1.7, -12.4, 0.8); eye(1.7, -12.4, 0.8);
      const nose = new Path2D(); nose.moveTo(-0.5, -10.9); nose.lineTo(0.5, -10.9); nose.lineTo(0, -10.3); nose.closePath(); g.fillStyle = "#FF8FA3"; g.fill(nose);
      const m = new Path2D(); m.moveTo(-1, -9.8); m.quadraticCurveTo(-0.5, -9.2, 0, -9.9); m.quadraticCurveTo(0.5, -9.2, 1, -9.8); stroke(g, m, INK, 0.4);
      for (const s of [-1, 1]) { line(g, "rgba(38,33,92,0.5)", s * 1.6, -10.4, s * 4.8, -10.9, 0.25); line(g, "rgba(38,33,92,0.5)", s * 1.6, -10, s * 4.6, -9.6, 0.25); }
      circle(g, rgba(CHEEK, 0.6), 0.8, -2.8, -10.6); circle(g, rgba(CHEEK, 0.6), 0.8, 2.8, -10.6);
      for (const s of [-1, 1]) { g.beginPath(); g.ellipse(s * 2, -0.6, 1.5, 0.9, 0, 0, 2 * PI); g.fillStyle = "#FFF4E6"; g.fill(); }
    } else if (kind === "coruja") {
      // corujinha estudiosa, de capelo, piscando devagar e batendo as asas quando comemora
      const flap = party ? sin(t * 12) * 0.5 : 0;
      for (const s of [-1, 1]) { g.save(); g.translate(s * 4.6, -8); g.rotate(s * (0.3 + flap)); g.beginPath(); g.ellipse(0, 1.5, 1.8, 4, 0, 0, 2 * PI); g.fillStyle = "#7A5230"; g.fill(); g.restore(); }
      const body = new Path2D(); body.ellipse(0, -7, 5.4, 7, 0, 0, 2 * PI); g.fillStyle = vGrad(g, ["#B9834C", "#8C5E33"], -14, 0); g.fill(body);
      g.beginPath(); g.ellipse(0, -4.6, 3.6, 4.4, 0, 0, 2 * PI); g.fillStyle = "#F1DDB3"; g.fill();
      for (let i = 0; i < 3; i++) { const p = new Path2D(); p.moveTo(-1.2, -6 + i * 1.6); p.lineTo(0, -5.2 + i * 1.6); p.lineTo(1.2, -6 + i * 1.6); stroke(g, p, "rgba(140,94,51,0.6)", 0.35); }
      for (const s of [-1, 1]) { const tuft = new Path2D(); tuft.moveTo(s * 4.4, -11.4); tuft.lineTo(s * 4.4, -14.6); tuft.lineTo(s * 2.4, -12.6); tuft.closePath(); g.fillStyle = "#8C5E33"; g.fill(tuft); }
      for (const s of [-1, 1]) { circle(g, "#FFFFFF", 2.3, s * 2.2, -10); g.strokeStyle = "#C98A4B"; g.lineWidth = 0.4; g.beginPath(); g.arc(s * 2.2, -10, 2.3, 0, 2 * PI); g.stroke(); eye(s * 2.2, -10, 1.1); }
      const beak = new Path2D(); beak.moveTo(-0.8, -8.6); beak.lineTo(0.8, -8.6); beak.lineTo(0, -7.2); beak.closePath(); g.fillStyle = "#FF8A2B"; g.fill(beak);
      const cap = new Path2D(); cap.moveTo(0, -17.2); cap.lineTo(5, -15.4); cap.lineTo(0, -13.6); cap.lineTo(-5, -15.4); cap.closePath(); g.fillStyle = CAP; g.fill(cap);
      line(g, GOLD, 0, -15.4, 4, -15, 0.4); line(g, GOLD, 4, -15, 4.2 + sin(t * 2.6) * 0.4, -12.6, 0.45);
    } else if (kind === "robo") {
      // robozinho: antena piscando, rosto de tela e esteiras
      const tread = new Path2D(); tread.roundRect(-5.2, -2.8, 10.4, 2.8, 1.4); g.fillStyle = "#2D3042"; g.fill(tread);
      for (let i = -1; i <= 1; i++) circle(g, "#8E92A6", 0.7, i * 3, -1.4);
      const body = new Path2D(); body.roundRect(-4.2, -9.4, 8.4, 6.8, 1.6); g.fillStyle = vGrad(g, ["#5A4FE6", "#3A2FC4"], -9.4, -2.6); g.fill(body);
      g.fillStyle = "#FF8FA3"; g.fill(heart(0, -6.2, 1.3));
      const head = new Path2D(); head.roundRect(-5.2, -17.6, 10.4, 8, 2.2); g.fillStyle = vGrad(g, ["#E3E6F0", "#B7B4D0"], -17.6, -9.6); g.fill(head);
      const scr = new Path2D(); scr.roundRect(-4, -16.4, 8, 5.6, 1.4); g.fillStyle = "#1E1B2E"; g.fill(scr);
      for (const s of [-1, 1]) { if (blink) line(g, MINT, s * 1.8 - 0.8, -13.4, s * 1.8 + 0.8, -13.4, 0.5); else { const e = new Path2D(); e.roundRect(s * 1.8 - 0.7, -14.6, 1.4, 2.2, 0.6); g.fillStyle = MINT; g.fill(e); } }
      const sm = new Path2D(); sm.moveTo(-1, -12); sm.quadraticCurveTo(0, -11.2, 1, -12); stroke(g, sm, MINT, 0.4);
      line(g, "#8E92A6", 0, -17.6, 0, -20.2, 0.5);
      circle(g, sin(t * 4) > 0 ? "#FF5C4D" : MINT, 0.9, 0, -20.6);
      line(g, "rgba(255,255,255,0.6)", -4.4, -16.6, -2.6, -17, 0.4);
    } else if (kind === "dragao") {
      // dragãozinho: asas batendo, rabo com espinho e baforada de fogo quando comemora
      const flap = sin(t * (party ? 10 : 3)) * 0.35;
      for (const s of [-1, 1]) { g.save(); g.translate(-1 + s * 1.5, -9); g.rotate(-0.6 * s + flap * s); const w = new Path2D(); w.moveTo(0, 0); w.quadraticCurveTo(s * -6, -5, s * -7, 1); w.quadraticCurveTo(s * -4, 0, s * -3.5, 2.4); w.quadraticCurveTo(s * -2, 1, 0, 2); w.closePath(); g.fillStyle = "#9563FF"; g.fill(w); g.restore(); }
      const tail = new Path2D(); tail.moveTo(-3.5, -2); tail.quadraticCurveTo(-9, -1, -10.5, -5 + sin(t * 2) * 0.8); stroke(g, tail, "#1F9E75", 1.6);
      const spike = new Path2D(); spike.moveTo(-11.6, -5.6 + sin(t * 2) * 0.8); spike.lineTo(-9.6, -6.4 + sin(t * 2) * 0.8); spike.lineTo(-10.6, -3.8 + sin(t * 2) * 0.8); spike.closePath(); g.fillStyle = "#9563FF"; g.fill(spike);
      const body = new Path2D(); body.ellipse(0, -5.2, 4.8, 5, 0, 0, 2 * PI); g.fillStyle = vGrad(g, ["#4FD3A2", "#1F9E75"], -10, 0); g.fill(body);
      g.beginPath(); g.ellipse(0.6, -4, 2.6, 3.2, 0, 0, 2 * PI); g.fillStyle = "#D9FBEE"; g.fill();
      circle(g, "#36C897", 4, 2.2, -12);
      g.beginPath(); g.ellipse(5.4, -11.2, 2.4, 1.7, 0, 0, 2 * PI); g.fillStyle = "#36C897"; g.fill();
      circle(g, INK, 0.3, 6.6, -11.6); circle(g, INK, 0.3, 6.2, -10.6);
      for (const hx of [0.4, 2.8]) { const h = new Path2D(); h.moveTo(hx, -15.2); h.lineTo(hx + 0.6, -17.6); h.lineTo(hx + 1.4, -15.2); h.closePath(); g.fillStyle = "#F5B83D"; g.fill(h); }
      eye(3.2, -13, 0.8); circle(g, rgba(CHEEK, 0.6), 0.8, 1.2, -11);
      if (party) {
        const k = 0.6 + 0.4 * abs(sin(t * 8)), f = new Path2D(); f.moveTo(7.6, -11.6); f.quadraticCurveTo(10 + 2 * k, -14, 12 + 2.5 * k, -11.2); f.quadraticCurveTo(10 + 2 * k, -9, 7.6, -10.6); f.closePath();
        g.fillStyle = hGrad(g, ["#FFE07A", "#FF9F3A", "#F2552C"], 7.6, 13); g.fill(f);
      }
    }
    g.restore();
    if (sleepy) { g.save(); g.font = "800 4px Nunito, system-ui, sans-serif"; g.fillStyle = rgba(INK, 0.6 + 0.4 * sin(t * 2)); g.fillText("z", x + 8, base - 26 + sin(t * 1.4) * 1.5); g.restore(); }
  }

  // ------------------------------------------------------------------ armário: chapéus, óculos, pescoço, mão e aura
  // Tudo é desenhado dentro do corpo do Folha (pula, inclina, amassa e estica junto); chapéu gira com o capelo,
  // óculos seguem o olhar, o item da mão segue a mão esquerda.
  function star5(g, x, y, r, sy = 1) {
    const p = new Path2D();
    for (let i = 0; i < 10; i++) { const a = -PI / 2 + i * PI / 5, rr = i % 2 ? r * 0.45 : r; i ? p.lineTo(x + cos(a) * rr, y + sin(a) * rr * sy) : p.moveTo(x + cos(a) * rr, y + sin(a) * rr * sy); }
    p.closePath(); return p;
  }
  function heart(x, y, r) {
    const p = new Path2D(); p.moveTo(x, y + r * 0.9);
    p.bezierCurveTo(x - r * 1.5, y - r * 0.1, x - r * 0.9, y - r * 1.25, x, y - r * 0.45);
    p.bezierCurveTo(x + r * 0.9, y - r * 1.25, x + r * 1.5, y - r * 0.1, x, y + r * 0.9); p.closePath(); return p;
  }
  const GOLD3 = ["#FFF1BF", GOLD, "#C98A12"];
  function drawPencilAt(g, x0, y0, x1, y1, r) {
    // lápis da ponta (x0, y0) até a borracha (x1, y1)
    const L = Math.hypot(x1 - x0, y1 - y0);
    g.save(); g.translate(x0, y0); g.rotate(Math.atan2(y1 - y0, x1 - x0));
    g.fillStyle = INK; g.beginPath(); g.moveTo(0, 0); g.lineTo(1.2, -r * 0.45); g.lineTo(1.2, r * 0.45); g.closePath(); g.fill();
    g.fillStyle = "#F0D3A8"; g.beginPath(); g.moveTo(1.1, -r * 0.42); g.lineTo(3.2, -r); g.lineTo(3.2, r); g.lineTo(1.1, r * 0.42); g.closePath(); g.fill();
    g.fillStyle = GOLD; g.fillRect(3.2, -r, L - 6.4, r * 2);
    g.fillStyle = "#E39A1E"; g.fillRect(3.2, r * 0.15, L - 6.4, r * 0.85);
    g.fillStyle = "#C8CAD6"; g.fillRect(L - 3.2, -r, 1.5, r * 2);
    g.fillStyle = "#F59AB8"; g.beginPath(); g.ellipse(L - 1.0, 0, 1.4, r, 0, 0, 2 * PI); g.fill();
    g.restore();
  }

  // ---- chapéus (no lugar do capelo; dentro do giro do capelo)
  function drawCap(g, t, tilt, vy, gold) {
    const top = gold ? "#FFE08A" : CAP_TOP, mid = gold ? "#E9AE34" : CAP, low = gold ? "#A86F0C" : "#15104A", edgeC = gold ? "#7A4F05" : "#120D3F";
    const base = new Path2D();
    base.moveTo(38, 32); base.lineTo(62, 32); base.lineTo(60.5, 42.5); base.quadraticCurveTo(50, 46.5, 39.5, 42.5); base.closePath();
    g.fillStyle = vGrad(g, [mid, low], 32, 46); g.fill(base);
    const edge = new Path2D();
    edge.moveTo(23, 29.5); edge.lineTo(50, 38); edge.lineTo(77, 29.5); edge.lineTo(77, 31.3); edge.lineTo(50, 39.8); edge.lineTo(23, 31.3); edge.closePath();
    g.fillStyle = edgeC; g.fill(edge);
    const board = new Path2D();
    board.moveTo(50, 21); board.lineTo(77, 29.5); board.lineTo(50, 38); board.lineTo(23, 29.5); board.closePath();
    const lg = g.createLinearGradient(30, 22, 70, 38); lg.addColorStop(0, top); lg.addColorStop(1, mid);
    g.fillStyle = lg; g.fill(board);
    stroke(g, board, "rgba(255,255,255,0.22)", 0.5);
    line(g, "rgba(255,255,255,0.18)", 33, 27.5, 50, 22.5, 1.2);
    // borla: balança com o corpo e reage aos pulos (atrasada)
    const tas = gold ? "#FFFFFF" : GOLD, tasD = gold ? "#D9D4EE" : "#D89422";
    const swing = sin(t * 2.6) * 4 - tilt * 0.8 + clamp(vy * 0.08, -5, 5);
    const bx = 50, by = 29.5, cx = 74, cy = 30.5;
    line(g, tas, bx, by, cx, cy, 1.3);
    const ex = cx + swing * 0.5, ey = cy + 12 - Math.max(0, -vy) * 0.03;
    const cord = new Path2D(); cord.moveTo(cx, cy); cord.quadraticCurveTo(cx + swing * 0.2, cy + 6, ex, ey);
    stroke(g, cord, tas, 1.3);
    const tassel = new Path2D();
    tassel.moveTo(ex - 1.4, ey); tassel.lineTo(ex + 1.4, ey);
    tassel.lineTo(ex + 2.4 + swing * 0.15, ey + 6.5); tassel.lineTo(ex - 2.4 + swing * 0.15, ey + 6.5); tassel.closePath();
    g.fillStyle = vGrad(g, [tas, tasD], ey, ey + 6.5); g.fill(tassel);
    circle(g, tas, 1.8, bx, by); circle(g, gold ? "#FFFFFF" : "#FFE3A0", 0.7, bx - 0.5, by - 0.5);
  }
  function drawLaurels(g, small) {
    // coroa de louros dourada: dois ramos abraçando a base do capelo
    const n = small ? 4 : 6;
    for (const side of [-1, 1]) {
      const P0 = [50 + side * 3, 45], P1 = [50 + side * 15, 45.5], P2 = [50 + side * 16.5, 33];
      const at = (u) => [(1 - u) ** 2 * P0[0] + 2 * (1 - u) * u * P1[0] + u * u * P2[0], (1 - u) ** 2 * P0[1] + 2 * (1 - u) * u * P1[1] + u * u * P2[1]];
      const stem = new Path2D(); stem.moveTo(...P0); stem.quadraticCurveTo(...P1, ...P2); stroke(g, stem, "#B07A12", small ? 0.9 : 0.55);
      for (let i = 1; i <= n; i++) {
        const u = i / n, [x, y] = at(u), [x2, y2] = at(Math.min(1, u + 0.02)), tang = Math.atan2(y2 - y, x2 - x);
        for (const k of i === n ? [0] : [-1, 1]) {
          g.save(); g.translate(x, y); g.rotate(tang + k * 0.75);
          const leaf = new Path2D(); leaf.ellipse(1.5, 0, 1.9, 0.85, 0, 0, 2 * PI);
          g.fillStyle = hGrad(g, ["#C98A12", GOLD, "#FFE3A0"], 0, 3.2); g.fill(leaf);
          if (!small) { g.strokeStyle = "rgba(120,70,0,0.35)"; g.lineWidth = 0.22; g.stroke(leaf); }
          g.restore();
        }
      }
    }
  }
  function drawStreakMedal(g, t, days, tilt) {
    // medalha da sequência pendurada na aba do capelo: bronze (7+), prata (30+), ouro (100+)
    const m = days >= 100 ? GOLD3 : days >= 30 ? ["#FFFFFF", "#CDD0DC", "#8E92A6"] : ["#F6C79A", "#D08B4E", "#94562A"];
    const sw = sin(t * 2.2 + 1) * 1.2 - tilt * 0.4, x = 25.5, y = 31, mx = x + sw * 0.4, my = y + 8;
    const rb = new Path2D(); rb.moveTo(x - 1.2, y); rb.lineTo(x + 1.2, y); rb.lineTo(mx + 0.9, my - 2); rb.lineTo(mx - 0.9, my - 2); rb.closePath();
    g.fillStyle = "#E0475E"; g.fill(rb); line(g, "rgba(255,255,255,0.6)", x, y + 0.2, mx, my - 2.2, 0.35);
    circle(g, m[2], 2.9, mx, my + 0.3);
    g.beginPath(); g.arc(mx, my, 2.6, 0, 2 * PI); g.fillStyle = vGrad(g, m, my - 2.6, my + 2.6); g.fill();
    const fl = new Path2D(); fl.moveTo(mx, my - 1.6); fl.quadraticCurveTo(mx + 1.5, my, mx, my + 1.4); fl.quadraticCurveTo(mx - 1.5, my, mx, my - 1.6);
    g.fillStyle = rgba(m[2], 0.9); g.fill(fl);
  }
  function drawNightcap(g, t, tilt, vy) {
    // touca de dormir listrada (à noite, parado ou cochilando), com pompom que balança
    const sw = sin(t * 1.6) * 1.6 - tilt * 0.6 + clamp(vy * 0.05, -3, 3), tx = 79 + sw, ty = 35 + abs(sw) * 0.3;
    const cone = new Path2D(); cone.moveTo(36.5, 41); cone.quadraticCurveTo(38, 23, 55, 21.5); cone.quadraticCurveTo(70 + sw * 0.4, 21, tx, ty);
    cone.quadraticCurveTo(68 + sw * 0.3, 28, 62.5, 31); cone.quadraticCurveTo(63, 36, 63.5, 41); cone.closePath();
    g.save(); g.clip(cone);
    g.fillStyle = "#4B3FD8"; g.fillRect(20, 10, 70, 40);
    for (let i = -6; i < 12; i++) { const b = new Path2D(), x = 20 + i * 6; b.moveTo(x, 50); b.lineTo(x + 3, 50); b.lineTo(x + 23, 10); b.lineTo(x + 20, 10); b.closePath(); g.fillStyle = "#DCD7FA"; g.fill(b); }
    g.fillStyle = vGrad(g, ["rgba(255,255,255,0.18)", "rgba(14,10,69,0.25)"], 20, 42); g.fillRect(20, 10, 70, 40);
    g.restore();
    stroke(g, cone, "rgba(14,10,69,0.35)", 0.4);
    const rim = new Path2D(); rim.moveTo(35, 41.5); rim.quadraticCurveTo(50, 37, 65, 41.5); rim.quadraticCurveTo(66.5, 44, 64.5, 45.4); rim.quadraticCurveTo(50, 41.4, 35.5, 45.4); rim.quadraticCurveTo(33.5, 44, 35, 41.5);
    g.fillStyle = vGrad(g, ["#FFFFFF", "#E4E0F2"], 38, 46); g.fill(rim); stroke(g, rim, "rgba(14,10,69,0.2)", 0.3);
    const pom = g.createRadialGradient(tx - 1, ty, 0.3, tx, ty + 1, 3.2); pom.addColorStop(0, "#FFFFFF"); pom.addColorStop(1, "#D9D4EE");
    g.beginPath(); g.arc(tx, ty + 1, 3, 0, 2 * PI); g.fillStyle = pom; g.fill();
  }
  function drawBone(g) {
    // boné de time: copa em gomos, aba virada de lado, botão em cima e o livrinho bordado
    const crown = new Path2D(); crown.moveTo(35, 43); crown.bezierCurveTo(34, 30, 41.5, 24.5, 50, 24.5); crown.bezierCurveTo(58.5, 24.5, 66, 30, 65, 43); crown.quadraticCurveTo(50, 40.4, 35, 43); crown.closePath();
    g.fillStyle = vGrad(g, ["#5A4FE6", "#3A2FC4", "#241B95"], 24, 43); g.fill(crown);
    g.save(); g.clip(crown);
    for (const x of [42.5, 57.5]) { const s = new Path2D(); s.moveTo(50, 24.6); s.quadraticCurveTo(x + (x < 50 ? -3 : 3), 30, x + (x < 50 ? -1.5 : 1.5), 43); stroke(g, s, "rgba(255,255,255,0.22)", 0.4); }
    g.fillStyle = "rgba(255,255,255,0.12)"; g.beginPath(); g.ellipse(43, 30, 5, 3, -0.5, 0, 2 * PI); g.fill();
    g.restore();
    const brim = new Path2D(); brim.moveTo(48, 42); brim.quadraticCurveTo(62, 39.8, 75, 41.6); brim.quadraticCurveTo(80.5, 43, 79, 45.4); brim.quadraticCurveTo(66, 45.6, 52, 45.2); brim.closePath();
    g.fillStyle = vGrad(g, ["#2A209E", "#120D3F"], 40, 46); g.fill(brim); line(g, "rgba(255,255,255,0.3)", 54, 42.6, 74, 42.2, 0.45);
    const band = new Path2D(); band.moveTo(35, 43); band.quadraticCurveTo(50, 40.4, 65, 43); stroke(g, band, MINT, 1.2);
    circle(g, MINT, 1.5, 50, 24.8); circle(g, "rgba(255,255,255,0.6)", 0.5, 49.5, 24.3);
    const lg = new Path2D(); lg.moveTo(45.5, 33.5); lg.quadraticCurveTo(48, 32, 50, 33.4); lg.quadraticCurveTo(52, 32, 54.5, 33.5); lg.lineTo(54.5, 37.5); lg.quadraticCurveTo(52, 36.2, 50, 37.4); lg.quadraticCurveTo(48, 36.2, 45.5, 37.5); lg.closePath();
    g.fillStyle = "#FFFDF8"; g.fill(lg); line(g, "#3A2FC4", 50, 33.6, 50, 37.2, 0.35);
  }
  function drawBoina(g) {
    // boina de artista, caída para o lado
    const b = new Path2D(); b.ellipse(47, 35.5, 18.5, 7.6, -0.14, 0, 2 * PI);
    const rg = g.createRadialGradient(42, 31, 1, 47, 36, 20); rg.addColorStop(0, "#F06A7E"); rg.addColorStop(1, "#A3203A"); g.fillStyle = rg; g.fill(b);
    const band = new Path2D(); band.ellipse(50, 41.8, 12.8, 2.5, 0, 0, 2 * PI); g.fillStyle = "#7A1428"; g.fill(band);
    stroke(g, (() => { const p = new Path2D(); p.moveTo(46, 28.4); p.quadraticCurveTo(45.4, 26.4, 46.6, 25.6); return p; })(), "#7A1428", 1.1);
    g.fillStyle = "rgba(255,255,255,0.18)"; g.beginPath(); g.ellipse(40, 32.5, 6, 2.2, -0.3, 0, 2 * PI); g.fill();
  }
  function drawExplorador(g) {
    // chapéu de explorador: copa redonda, aba larga e fita marrom
    const brim = new Path2D(); brim.ellipse(50, 41.5, 23, 4.6, 0, 0, 2 * PI);
    g.fillStyle = vGrad(g, ["#D9BC82", "#A9874D"], 37, 46); g.fill(brim);
    const dome = new Path2D(); dome.moveTo(37, 41); dome.bezierCurveTo(36, 28, 43, 24.5, 50, 24.5); dome.bezierCurveTo(57, 24.5, 64, 28, 63, 41); dome.quadraticCurveTo(50, 43, 37, 41); dome.closePath();
    g.fillStyle = vGrad(g, ["#E8D3A2", "#C9A86A", "#A6834A"], 24, 42); g.fill(dome);
    const band = new Path2D(); band.moveTo(37.2, 37); band.quadraticCurveTo(50, 39, 62.8, 37); band.lineTo(63, 40.4); band.quadraticCurveTo(50, 42.4, 37, 40.4); band.closePath();
    g.fillStyle = "#6B4A26"; g.fill(band);
    stroke(g, (() => { const p = new Path2D(); p.moveTo(50, 25); p.quadraticCurveTo(49, 32, 50, 38.6); return p; })(), "rgba(110,80,40,0.35)", 0.5);
    g.fillStyle = "rgba(255,255,255,0.2)"; g.beginPath(); g.ellipse(43.5, 30, 4, 2.5, -0.5, 0, 2 * PI); g.fill();
  }
  function drawFones(g, t) {
    // fones de ouvido: arco por cima e as conchas nas bordas do livro (perto dos olhos)
    const arc = new Path2D(); arc.moveTo(13.5, 54); arc.bezierCurveTo(12, 19, 88, 19, 86.5, 54);
    stroke(g, arc, "#24242E", 3.2); stroke(g, arc, "#4A4A5C", 1.2);
    const pad = new Path2D(); pad.moveTo(40, 28.6); pad.quadraticCurveTo(50, 25.6, 60, 28.6); stroke(g, pad, "#3A2FC4", 3.6);
    for (const side of [-1, 1]) {
      const x = side < 0 ? 7.5 : 85, cup = new Path2D(); cup.roundRect(x, 50, 7.5, 14, 3.2);
      g.fillStyle = vGrad(g, ["#4B3FD8", "#2A209E"], 50, 64); g.fill(cup);
      const ring = new Path2D(); ring.roundRect(side < 0 ? x + 4.5 : x, 51.2, 3, 11.6, 1.5); g.fillStyle = MINT; g.fill(ring);
      line(g, "rgba(255,255,255,0.35)", x + 1.6, 52, x + 1.6, 57, 0.6);
    }
  }
  function drawCartola(g) {
    // cartola de gala com faixa dourada
    const brim = new Path2D(); brim.ellipse(50, 41.6, 17.5, 3.6, 0, 0, 2 * PI);
    g.fillStyle = vGrad(g, ["#3A3550", "#14121F"], 38, 45); g.fill(brim);
    const body = new Path2D(); body.moveTo(39.2, 41); body.lineTo(40.4, 16.5); body.lineTo(59.6, 16.5); body.lineTo(60.8, 41); body.quadraticCurveTo(50, 43, 39.2, 41); body.closePath();
    g.fillStyle = hGrad(g, ["#1E1B2E", "#3D3858", "#1A1726"], 39, 61); g.fill(body);
    const band = new Path2D(); band.moveTo(39.6, 34.8); band.lineTo(60.4, 34.8); band.lineTo(60.6, 38.6); band.quadraticCurveTo(50, 40, 39.4, 38.6); band.closePath();
    g.fillStyle = vGrad(g, GOLD3, 34.8, 39); g.fill(band);
    const top = new Path2D(); top.ellipse(50, 16.5, 9.6, 2.2, 0, 0, 2 * PI); g.fillStyle = "#2E2A42"; g.fill(top);
    line(g, "rgba(255,255,255,0.22)", 43, 18.5, 42.4, 33, 0.9);
  }
  function drawCoroa(g, t) {
    // coroa de ouro com pedras
    const c = new Path2D();
    c.moveTo(36, 43); c.lineTo(35.5, 30); c.lineTo(41, 35.2); c.lineTo(43.5, 26); c.lineTo(47, 33.6); c.lineTo(50, 21.5); c.lineTo(53, 33.6); c.lineTo(56.5, 26);
    c.lineTo(59, 35.2); c.lineTo(64.5, 30); c.lineTo(64, 43); c.quadraticCurveTo(50, 40.4, 36, 43); c.closePath();
    g.fillStyle = vGrad(g, GOLD3, 21, 43); g.fill(c); stroke(g, c, "rgba(120,70,0,0.45)", 0.35);
    const band = new Path2D(); band.moveTo(36, 38.6); band.quadraticCurveTo(50, 36.2, 64, 38.6); stroke(g, band, "rgba(160,100,10,0.6)", 0.5);
    for (const [x, y] of [[35.5, 30], [43.5, 26], [50, 21.5], [56.5, 26], [64.5, 30]]) circle(g, "#FFF4D0", 1.15, x, y);
    circle(g, "#E0475E", 1.7, 50, 40.2); circle(g, "#6FB0FF", 1.15, 42.5, 40.8); circle(g, "#36C897", 1.15, 57.5, 40.8);
    circle(g, "rgba(255,255,255,0.7)", 0.5, 49.4, 39.6);
    const k = Math.max(0, sin(t * 1.4)) ** 8; if (k > 0.02) star4(g, 47.5, 27, 3 * k, `rgba(255,255,255,${k})`);
  }
  function drawMago(g, t, tilt, vy) {
    // chapéu de mago: cone alto que balança na ponta, com estrelas e lua
    const sway = sin(t * 1.8) * 1.6 - tilt * 0.5 + clamp(vy * 0.05, -3, 3), tx = 60 + sway, ty = 5 + abs(sway) * 0.3;
    const brim = new Path2D(); brim.ellipse(50, 42.4, 21, 3.8, 0, 0, 2 * PI);
    g.fillStyle = vGrad(g, ["#4A31A8", "#24165F"], 38, 46); g.fill(brim);
    const cone = new Path2D(); cone.moveTo(34, 42); cone.bezierCurveTo(40, 31, 47, 16, tx, ty); cone.bezierCurveTo(57.5 + sway * 0.4, 16, 59, 30, 66, 42); cone.quadraticCurveTo(50, 39.4, 34, 42); cone.closePath();
    g.fillStyle = hGrad(g, ["#6A4BD6", "#4B32AE", "#33227F"], 34, 66); g.fill(cone);
    g.save(); g.clip(cone);
    const st = (x, y, r) => { g.fillStyle = vGrad(g, GOLD3, y - r, y + r); g.fill(star5(g, x, y, r)); };
    st(44, 34, 1.9); st(55.5, 26, 1.5); st(51, 37.5, 1.2); st(56.5, 14.5, 1.1);
    g.restore();
    const band = new Path2D(); band.moveTo(34.6, 40.6); band.quadraticCurveTo(50, 38, 65.4, 40.6); stroke(g, band, GOLD, 1.3);
    star4(g, tx, ty, 2.2 + 0.5 * sin(t * 3), "rgba(255,240,180,0.95)");
  }
  function drawAureola(g, t) {
    // auréola dourada flutuando
    const y = 23 + sin(t * 2) * 1.2;
    g.save(); g.shadowColor = "rgba(255,214,110,0.9)"; g.shadowBlur = 3;
    g.beginPath(); g.ellipse(50, y, 13, 3.3, 0, 0, 2 * PI); g.strokeStyle = "#FFD877"; g.lineWidth = 2; g.stroke();
    g.restore();
    g.beginPath(); g.ellipse(50, y, 13, 3.3, 0, PI * 1.05, PI * 1.6); g.strokeStyle = "rgba(255,255,255,0.85)"; g.lineWidth = 0.6; g.stroke();
  }
  /** O chapéu escolhido (no frame girado do capelo). */
  function drawHat(g, s, look, tilt) {
    const { t, small } = s, vy = s.vy, hat = look.hat;
    if (hat === "bone") drawBone(g);
    else if (hat === "boina") drawBoina(g);
    else if (hat === "explorador") drawExplorador(g);
    else if (hat === "fones") drawFones(g, t);
    else if (hat === "cartola") drawCartola(g);
    else if (hat === "coroa") drawCoroa(g, t);
    else if (hat === "mago") drawMago(g, t, tilt, vy);
    else if (hat === "aureola") drawAureola(g, t);
    else {
      drawCap(g, t, tilt, vy, hat === "capelo-ouro");
      if (hat === "louros") drawLaurels(g, small);
      if (look.streak >= 7 && !small) drawStreakMedal(g, t, look.streak, tilt);
    }
  }

  // ---- óculos (seguem o rosto, um pouco menos que os olhos)
  function drawEyewear(g, P, small, kind, t) {
    if (!kind || kind === "nenhum") return;
    const dx = P.lookX * 0.7, dy = P.lookY * 0.5, w = small ? 1.5 : 0.95;
    const L = [31 + dx, 61 + dy], R = [69 + dx, 61 + dy];
    const bridge = (color, lw, y0 = 60, y1 = 56.8) => { const b = new Path2D(); b.moveTo(38 + dx, y0 + dy); b.quadraticCurveTo(50 + dx, y1 + dy, 62 + dx, y0 + dy); stroke(g, b, color, lw); };
    const arms = (color, lw) => { line(g, color, 24 + dx, 59.5 + dy, 18.5, 58, lw); line(g, color, 76 + dx, 59.5 + dy, 81.5, 58, lw); };
    const glare = (x, y, rx, ry) => { if (small) return; g.beginPath(); g.ellipse(x, y, rx, ry, 0, PI * 1.08, PI * 1.42); g.strokeStyle = "rgba(255,255,255,0.8)"; g.lineWidth = 0.55; g.lineCap = "round"; g.stroke(); };
    if (kind === "leitura") {
      for (const [x, y] of [L, R]) { g.beginPath(); g.ellipse(x, y, 7, 6.8, 0, 0, 2 * PI); g.fillStyle = "rgba(255,255,255,0.10)"; g.fill(); g.strokeStyle = "#B47A1C"; g.lineWidth = w; g.stroke(); glare(x, y, 5.6, 5.4); }
      bridge("#B47A1C", w); arms("#B47A1C", w);
    } else if (kind === "escuros") {
      // óculos escuros de aviador: escondem os olhos (as sobrancelhas continuam falando)
      for (const [x, y] of [L, R]) {
        const side = x < 50 ? -1 : 1, p = new Path2D();
        p.moveTo(x - 7.4, y - 4.6); p.quadraticCurveTo(x, y - 6.4, x + 7.4, y - 4.6); p.quadraticCurveTo(x + 7.8 * (side > 0 ? 1 : 0.85), y + 2, x + side * 2, y + 6.2);
        p.quadraticCurveTo(x, y + 7.4, x - side * 2, y + 6.2); p.quadraticCurveTo(x - 7.8 * (side > 0 ? 0.85 : 1), y + 2, x - 7.4, y - 4.6); p.closePath();
        g.fillStyle = vGrad(g, ["#2B2B40", "#151522", "#3A3A5C"], y - 6, y + 7); g.fill(p); stroke(g, p, "#C9CCD8", small ? 1.1 : 0.7);
        if (!small) { const gl = new Path2D(); gl.moveTo(x - 4.5, y - 2.5); gl.lineTo(x - 1, y - 4); gl.lineTo(x - 4, y + 2); gl.closePath(); g.fillStyle = "rgba(255,255,255,0.35)"; g.fill(gl); }
      }
      bridge("#C9CCD8", small ? 1.1 : 0.7, 57.5, 56.4); arms("#C9CCD8", small ? 1.1 : 0.7);
    } else if (kind === "nerd") {
      for (const [x, y] of [L, R]) { const p = new Path2D(); p.roundRect(x - 7.2, y - 5.6, 14.4, 11.6, 3); g.fillStyle = "rgba(255,255,255,0.10)"; g.fill(p); stroke(g, p, "#1E1B2E", small ? 2 : 1.6); glare(x, y, 5.2, 4.4); }
      bridge("#1E1B2E", small ? 2 : 1.6, 59.2, 58.6); arms("#1E1B2E", small ? 2 : 1.6);
      if (!small) { const tp = new Path2D(); tp.rect(48.2 + dx, 57.6 + dy, 3.6, 2.6); g.fillStyle = "#FFFDF8"; g.fill(tp); stroke(g, tp, "rgba(0,0,0,0.2)", 0.2); }
    } else if (kind === "estrela") {
      for (const [x, y] of [L, R]) { const p = star5(g, x, y, 8.6); g.fillStyle = "rgba(255,170,215,0.22)"; g.fill(p); g.strokeStyle = "#F0559A"; g.lineWidth = small ? 1.6 : 1.1; g.lineJoin = "round"; g.stroke(p); }
      bridge("#F0559A", small ? 1.6 : 1.1, 59, 57.5); arms("#F0559A", small ? 1.6 : 1.1);
      if (!small) { const k = 0.5 + 0.5 * sin(t * 3); star4(g, R[0] + 7, R[1] - 7, 1.8 * k, `rgba(255,255,255,${0.9 * k})`); }
    } else if (kind === "coracao") {
      for (const [x, y] of [L, R]) { const p = heart(x, y + 0.6, 6.8); g.fillStyle = "rgba(255,90,130,0.30)"; g.fill(p); g.strokeStyle = "#D63A5E"; g.lineWidth = small ? 1.6 : 1.1; g.lineJoin = "round"; g.stroke(p); }
      bridge("#D63A5E", small ? 1.6 : 1.1, 58.4, 56.6); arms("#D63A5E", small ? 1.6 : 1.1);
    } else if (kind === "monoculo") {
      const [x, y] = R; g.beginPath(); g.ellipse(x, y, 7.2, 7.2, 0, 0, 2 * PI); g.fillStyle = "rgba(255,255,255,0.10)"; g.fill(); g.strokeStyle = vGrad(g, GOLD3, y - 7, y + 7); g.lineWidth = small ? 1.6 : 1.2; g.stroke();
      glare(x, y, 5.8, 5.8);
      const ch = new Path2D(); ch.moveTo(x + 5, y + 5.2); ch.quadraticCurveTo(x + 9, y + 14, x + 13.5, y + 13.5);
      g.save(); g.setLineDash([0.9, 0.6]); stroke(g, ch, GOLD, 0.6); g.restore();
    }
  }

  // ---- pescoço / peito (embaixo da boca, no pé das páginas)
  function drawNeckwear(g, t, small, kind) {
    if (!kind || kind === "nenhum") return;
    if (kind === "borboleta") {
      const x = 50, y = 83.4, red = "#E0475E", dk = "#A8233A";
      for (const side of [-1, 1]) {
        const p = new Path2D(); p.moveTo(x, y); p.quadraticCurveTo(x + side * 3, y - 3.6, x + side * 6, y - 2.6); p.quadraticCurveTo(x + side * 6.8, y, x + side * 6, y + 2.6); p.quadraticCurveTo(x + side * 3, y + 3.6, x, y); p.closePath();
        g.fillStyle = hGrad(g, side < 0 ? [red, dk] : [dk, red], side < 0 ? x - 6 : x, side < 0 ? x : x + 6); g.fill(p);
        if (!small) { const f = new Path2D(); f.moveTo(x + side * 1.5, y - 0.6); f.quadraticCurveTo(x + side * 3.6, y - 1.2, x + side * 5, y - 0.4); stroke(g, f, "rgba(255,255,255,0.35)", 0.35); }
      }
      g.beginPath(); g.ellipse(x, y, 1.6, 1.9, 0, 0, 2 * PI); g.fillStyle = dk; g.fill();
      circle(g, "rgba(255,255,255,0.35)", 0.45, x - 0.5, y - 0.7);
    } else if (kind === "gravata") {
      // gravata listrada que balança de leve
      const sw = sin(t * 1.9) * 0.8;
      const knot = new Path2D(); knot.moveTo(47.6, 80.6); knot.lineTo(52.4, 80.6); knot.lineTo(51.4, 83.8); knot.lineTo(48.6, 83.8); knot.closePath();
      const blade = new Path2D(); blade.moveTo(48.6, 83.6); blade.lineTo(51.4, 83.6); blade.lineTo(53.2 + sw, 93.5); blade.lineTo(50 + sw, 96.6); blade.lineTo(46.8 + sw, 93.5); blade.closePath();
      g.fillStyle = vGrad(g, ["#4B3FD8", "#2A209E"], 83, 97); g.fill(blade);
      g.save(); g.clip(blade); for (let i = 0; i < 6; i++) { const yy = 84 + i * 2.4; line(g, rgba(GOLD, 0.85), 44, yy + 3, 56, yy - 1, 0.7, false); } g.restore();
      g.fillStyle = "#2A209E"; g.fill(knot); line(g, "rgba(255,255,255,0.3)", 48.4, 81.2, 51.6, 81.2, 0.4);
    } else if (kind === "cachecol") {
      // cachecol listrado na base, com a ponta caindo e franjas
      const sw = sin(t * 1.7) * 0.8;
      const band = new Path2D(); band.moveTo(27, 79.6); band.quadraticCurveTo(50, 84, 73, 79.6); band.lineTo(73.4, 84.8); band.quadraticCurveTo(50, 89.4, 26.6, 84.8); band.closePath();
      g.fillStyle = "#E0475E"; g.fill(band);
      g.save(); g.clip(band); for (let x = 22; x < 80; x += 5) { const s = new Path2D(); s.rect(x, 76, 2.2, 16); g.fillStyle = "#FFFDF8"; g.fill(s); } g.restore();
      const end = new Path2D(); end.moveTo(58, 85.4); end.lineTo(63, 84.6); end.lineTo(65.4 + sw, 95.5); end.lineTo(60.2 + sw, 96.2); end.closePath();
      g.fillStyle = "#C42F4B"; g.fill(end);
      g.save(); g.clip(end); for (let i = 0; i < 4; i++) line(g, "#FFFDF8", 55, 87.5 + i * 2.6, 70, 85.8 + i * 2.6, 1.1, false); g.restore();
      for (let i = 0; i < 5; i++) line(g, "#C42F4B", 60.4 + sw + i * 1.15, 96, 60.2 + sw + i * 1.15, 98, 0.5);
    } else if (kind === "medalha") {
      // medalha de ouro de campeão
      const sw = sin(t * 2) * 0.6;
      const strap = (x0) => { const p = new Path2D(); p.moveTo(x0, 77.6); p.lineTo(x0 + (x0 < 50 ? 2.6 : -2.6), 77.6); p.lineTo(50 + sw + (x0 < 50 ? 1 : -1), 86); p.lineTo(50 + sw, 86); p.closePath(); return p; };
      g.fillStyle = "#3D8BEA"; g.fill(strap(43)); g.fillStyle = "#E0475E"; g.fill(strap(57));
      const mx = 50 + sw, my = 89.4;
      circle(g, "#B97E12", 4.6, mx, my + 0.4);
      g.beginPath(); g.arc(mx, my, 4.2, 0, 2 * PI); g.fillStyle = vGrad(g, GOLD3, my - 4.2, my + 4.2); g.fill();
      g.fillStyle = "rgba(160,100,10,0.85)"; g.fill(star5(g, mx, my + 0.2, 2.4));
      circle(g, "rgba(255,255,255,0.8)", 0.6, mx - 1.8, my - 2);
    } else if (kind === "broche") {
      const s = star5(g, 23.5, 79.6, small ? 4 : 3.2);
      g.fillStyle = vGrad(g, GOLD3, 76, 83); g.fill(s); g.strokeStyle = "rgba(120,70,0,0.45)"; g.lineWidth = 0.3; g.stroke(s);
      if (!small) { const k = Math.max(0, sin(t * 1.7)) ** 6; if (k > 0.02) star4(g, 25.4, 77.8, 2.2 * k, `rgba(255,255,255,${0.95 * k})`); }
    } else if (kind === "flor") {
      // flor na lapela (no canto da página)
      const x = 76.5, y = 79.6, rot = sin(t * 1.5) * 0.15;
      g.save(); g.translate(x, y); g.rotate(rot);
      for (let i = 0; i < 5; i++) { const a = i * 2 * PI / 5; g.beginPath(); g.ellipse(cos(a) * 2.2, sin(a) * 2.2, 1.9, 1.3, a, 0, 2 * PI); g.fillStyle = i % 2 ? "#FFB3C7" : "#FF8FA3"; g.fill(); }
      circle(g, "#F5B83D", 1.3, 0, 0); circle(g, "rgba(255,255,255,0.6)", 0.4, -0.4, -0.4);
      g.restore();
      line(g, "#3FBF8F", x - 1.2, y + 3, x - 3, y + 6.5, 0.6);
    } else if (kind === "roseta") {
      // roseta de campeão: pregas, duas caudas e a estrela no meio
      const x = 24.5, y = 78.6;
      for (const [dx, c] of [[-1.6, "#3A2FC4"], [1.6, "#2A209E"]]) { const tl = new Path2D(); tl.moveTo(x + dx * 0.6, y + 2); tl.lineTo(x + dx * 2.2, y + 9); tl.lineTo(x + dx * 1.1, y + 8); tl.lineTo(x + dx * 0.4, y + 9.4); tl.lineTo(x - dx * 0.2, y + 2.4); tl.closePath(); g.fillStyle = c; g.fill(tl); }
      for (let i = 0; i < 14; i++) { const a = i * 2 * PI / 14; g.beginPath(); g.ellipse(x + cos(a) * 3.2, y + sin(a) * 3.2, 1.5, 1.05, a, 0, 2 * PI); g.fillStyle = i % 2 ? "#F5B83D" : "#E9AE34"; g.fill(); }
      circle(g, "#3A2FC4", 2.4, x, y); g.fillStyle = "#FFE3A0"; g.fill(star5(g, x, y, 1.6));
    }
  }

  // ---- item na mão esquerda (desenhado antes da mão, que fica por cima segurando)
  function drawHeld(g, kind, h, ang, t, small) {
    const [x, y] = h, tiltA = clamp(ang, -0.6, 0.6) * 0.5;
    g.save(); g.translate(x, y); g.rotate(tiltA); g.scale(small ? 1.45 : 1.8, small ? 1.45 : 1.8);
    if (kind === "lapis") drawPencilAt(g, 2.5, 8, -3, -9, small ? 1.4 : 1.1);
    else if (kind === "marcatexto") {
      g.save(); g.rotate(-0.35);
      const body = new Path2D(); body.roundRect(-1.9, -9.5, 3.8, 14, 1.2); g.fillStyle = hGrad(g, ["#FFF27A", "#F5E03A", "#D9C21F"], -1.9, 1.9); g.fill(body);
      const cap = new Path2D(); cap.roundRect(-2.1, -12.6, 4.2, 4, 1.2); g.fillStyle = "#3FBF8F"; g.fill(cap);
      const tip = new Path2D(); tip.moveTo(-1.3, 4.4); tip.lineTo(1.3, 4.4); tip.lineTo(0.6, 7); tip.lineTo(-1.3, 6.2); tip.closePath(); g.fillStyle = "#3FBF8F"; g.fill(tip);
      line(g, "rgba(255,255,255,0.6)", -0.9, -8.8, -0.9, 3.5, 0.5);
      g.restore();
    } else if (kind === "livro") {
      g.save(); g.rotate(-0.12);
      const b = new Path2D(); b.roundRect(-4.6, -11, 8.4, 10.5, 0.8); g.fillStyle = vGrad(g, ["#E8566D", "#961D37"], -11, -0.5); g.fill(b);
      g.fillStyle = "#FFFDF8"; g.fillRect(3.2, -10.4, 1, 9.4); line(g, GOLD, -3.6, -8.6, 2.4, -8.6, 0.5); line(g, GOLD, -3.6, -3, 2.4, -3, 0.5);
      g.fillStyle = "#3FBF8F"; g.fillRect(-1.4, -0.6, 1.4, 2.6);
      g.restore();
    } else if (kind === "balao") {
      // balão que flutua acima da mão, preso por um barbante que acompanha
      const bx = -5 + sin(t * 1.3) * 2.2, by = -25 + sin(t * 1.7) * 1.4;
      const str = new Path2D(); str.moveTo(0, -1); str.bezierCurveTo(-2, -7, bx + 2, by + 12, bx, by + 6.6); stroke(g, str, "rgba(60,50,110,0.6)", 0.35);
      g.beginPath(); g.ellipse(bx, by, 5.2, 6.2, sin(t * 1.3) * 0.12, 0, 2 * PI);
      const rg = g.createRadialGradient(bx - 2, by - 2.5, 0.5, bx, by, 7); rg.addColorStop(0, "#FF9DB0"); rg.addColorStop(1, "#D9284A"); g.fillStyle = rg; g.fill();
      const k = new Path2D(); k.moveTo(bx - 0.9, by + 6.9); k.lineTo(bx + 0.9, by + 6.9); k.lineTo(bx, by + 5.8); k.closePath(); g.fillStyle = "#B81F3E"; g.fill(k);
      g.beginPath(); g.ellipse(bx - 2.1, by - 2.6, 1.2, 2, -0.5, 0, 2 * PI); g.fillStyle = "rgba(255,255,255,0.55)"; g.fill();
    } else if (kind === "varinha") {
      line(g, "#2A2240", 2, 6, -4.5, -11, 1.3); line(g, "#FFFFFF", -3.6, -8.8, -4.5, -11, 1.3);
      const sx = -4.8, sy = -12.6, r = 3 + 0.4 * sin(t * 6);
      g.save(); g.translate(sx, sy); g.rotate(t * 1.5); g.fillStyle = vGrad(g, GOLD3, -r, r); g.fill(star5(g, 0, 0, r)); g.restore();
      for (let i = 0; i < 4; i++) { const ph = (t * 0.9 + i / 4) % 1, a = i * 1.7 + t; star4(g, sx + cos(a) * (3 + ph * 7), sy + sin(a) * (3 + ph * 7) - ph * 3, 1.3 * (1 - ph), `rgba(255,240,180,${1 - ph})`); }
    } else if (kind === "trofeu") {
      const cup = new Path2D(); cup.moveTo(-4.4, -13); cup.lineTo(4.4, -13); cup.quadraticCurveTo(4.4, -6.4, 0.9, -5.6); cup.lineTo(0.9, -3.4); cup.lineTo(-0.9, -3.4); cup.lineTo(-0.9, -5.6); cup.quadraticCurveTo(-4.4, -6.4, -4.4, -13); cup.closePath();
      for (const s of [-1, 1]) { const hd = new Path2D(); hd.moveTo(s * 4.2, -12); hd.quadraticCurveTo(s * 7, -11.5, s * 4, -8); stroke(g, hd, "#C98A12", 0.9); }
      g.fillStyle = vGrad(g, GOLD3, -13, -3); g.fill(cup);
      const base = new Path2D(); base.roundRect(-3, -3.6, 6, 2.4, 0.6); g.fillStyle = "#7A4F05"; g.fill(base);
      g.fillStyle = "rgba(255,255,255,0.7)"; g.fill(star5(g, 0, -9.8, 1.6));
    } else if (kind === "lupa") {
      line(g, "#6B4A26", 1.2, 5, -2.4, -4.6, 1.8);
      const lx = -4.6, ly = -10;
      g.beginPath(); g.arc(lx, ly, 4.4, 0, 2 * PI); g.fillStyle = "rgba(190,225,255,0.35)"; g.fill(); g.strokeStyle = "#8E92A6"; g.lineWidth = 1.2; g.stroke();
      g.beginPath(); g.arc(lx, ly, 3.2, PI * 1.1, PI * 1.45); g.strokeStyle = "rgba(255,255,255,0.85)"; g.lineWidth = 0.6; g.stroke();
    }
    g.restore();
  }

  // ---- aura: em volta do Folha (fora do corpo); "behind" desenha a parte de trás
  function drawAura(g, t, kind, small, burst, behind) {
    if (!kind || kind === "nenhum") return;
    const boost = burst > 0 ? 1 + burst * 1.5 : 1;
    if (kind === "brilhos" && !behind) {
      const spots = [[10, 40], [90, 36], [6, 70], [95, 66], [22, 20], [80, 16]];
      for (let i = 0; i < (small ? 3 : 6); i++) { const ph = (t * 0.8 + i * 0.19) % 1, a = sin(PI * ph); const [x, y] = spots[i]; star4(g, x, y, (small ? 4.6 : 4.2) * a * boost, i % 2 ? `rgba(255,255,255,${a})` : rgba(GOLD, a)); }
    } else if (kind === "coracoes" && !behind) {
      for (let i = 0; i < (small ? 2 : 4); i++) { const ph = (t * 0.35 + i / 4) % 1, side = i % 2 ? 1 : -1; const x = 50 + side * (40 + sin(ph * 6 + i) * 3), y = 80 - ph * 60; g.fillStyle = `rgba(240,85,120,${sin(PI * ph) * 0.9})`; g.fill(heart(x, y, 3.4 * boost * (0.7 + ph * 0.5))); }
    } else if (kind === "notas" && !behind) {
      for (let i = 0; i < (small ? 2 : 4); i++) {
        const ph = (t * 0.32 + i / 4) % 1, side = i % 2 ? 1 : -1, x = 50 + side * (38 + sin(ph * 5 + i) * 4), y = 76 - ph * 56, a = sin(PI * ph), c = `rgba(58,47,196,${a})`;
        g.save(); g.translate(x, y); g.rotate(sin(t * 2 + i) * 0.2); g.scale(1.5 * boost, 1.5 * boost);
        g.beginPath(); g.ellipse(0, 0, 1.6, 1.2, -0.4, 0, 2 * PI); g.fillStyle = c; g.fill(); line(g, c, 1.4, -0.4, 1.4, -6, 0.6);
        if (i % 2) { g.beginPath(); g.ellipse(4.4, -1, 1.6, 1.2, -0.4, 0, 2 * PI); g.fill(); line(g, c, 5.8, -1.4, 5.8, -7, 0.6); line(g, c, 1.4, -6, 5.8, -7, 1); }
        else { const f = new Path2D(); f.moveTo(1.4, -6); f.quadraticCurveTo(4, -4.6, 3, -2.4); stroke(g, f, c, 0.6); }
        g.restore();
      }
    } else if (kind === "orbita") {
      // três estrelas em órbita: passam por trás e pela frente do Folha
      for (let i = 0; i < 3; i++) {
        const a = t * 1.6 + i * 2 * PI / 3, front = sin(a) > 0; if (front === behind) continue;
        const x = 50 + cos(a) * 50, y = 62 + sin(a) * 12, r = (front ? 3.8 : 2.6) * boost;
        g.fillStyle = front ? vGrad(g, GOLD3, y - r, y + r) : "rgba(245,184,61,0.55)"; g.fill(star5(g, x, y, r));
      }
    } else if (kind === "chamas" && behind) {
      for (let i = 0; i < (small ? 2 : 4); i++) {
        const side = i % 2 ? 1 : -1, row = Math.floor(i / 2), x = 50 + side * (40 + row * 7), base = 94 - row * 12;
        const hgt = (13 + 3 * sin(t * 9 + i * 1.7)) * boost, wd = 4.2;
        const p = new Path2D(); p.moveTo(x - wd, base); p.bezierCurveTo(x - wd * 1.1, base - hgt * 0.5, x - wd * 0.2 + sin(t * 7 + i) * 0.8, base - hgt * 0.7, x + sin(t * 6 + i) * 1.2, base - hgt);
        p.bezierCurveTo(x + wd * 0.6, base - hgt * 0.6, x + wd * 1.1, base - hgt * 0.45, x + wd, base); p.quadraticCurveTo(x, base + 1.8, x - wd, base);
        g.fillStyle = vGrad(g, ["#FFE07A", "#FF9F3A", "#F2552C"], base - hgt, base); g.fill(p);
      }
    } else if (kind === "confete" && !behind) {
      const colors = [SPARK, MINT, CHEEK, "#6FB0FF"];
      for (let i = 0; i < (small ? 5 : 10); i++) {
        const ph = (t * 0.28 + i * 0.1) % 1, x = 8 + ((i * 37) % 84) + sin(t * 2 + i) * 3, y = -2 + ph * 100;
        if (y > 40 && y < 92 && x > 12 && x < 88) continue;   // não cai na frente do rosto
        g.save(); g.translate(x, y); g.rotate(t * 3 + i); g.fillStyle = rgba(colors[i % 4], 0.9); g.fillRect(-2, -0.9, 4 * boost, 1.8); g.restore();
      }
    } else if (kind === "galaxia") {
      // anel de estrelinhas e poeira cósmica em volta (metade passa por trás)
      for (let i = 0; i < 18; i++) {
        const a = t * 0.7 + i * 2 * PI / 18, front = sin(a) > 0; if (front === behind) continue;
        const x = 50 + cos(a) * 54, y = 60 + sin(a) * 14, c = i % 3 === 0 ? "#FFFFFF" : i % 3 === 1 ? "#B995F5" : "#6FB0FF";
        circle(g, rgba(c, front ? 0.95 : 0.5), (front ? 1.3 : 0.8) * boost, x, y);
        if (i % 6 === 0) star4(g, x, y, (front ? 3.4 : 2.2) * boost, rgba(c, front ? 0.9 : 0.5));
      }
    }
  }
  // Toque com aura: um estouro de brilhos rápido.
  function drawBurst(g, burst) {
    if (burst <= 0) return;
    const u = 1 - burst;
    for (let i = 0; i < 8; i++) { const a = i * PI / 4, r = 30 + u * 26; star4(g, 50 + cos(a) * r, 62 + sin(a) * r * 0.7, 3 * burst, `rgba(255,224,122,${burst})`); }
  }

  // ---- cenário do dia
  function drawRelief(g, tm) {
    // "ufa": nuvenzinha de ar saindo da boca e gota de suor voando da testa
    const a = clamp((tm - 0.5) / 1.4, 0, 1);
    if (a > 0 && a < 1) for (let i = 0; i < 3; i++) {
      const u = clamp(a * 1.4 - i * 0.18, 0, 1); if (u <= 0 || u >= 1) continue;
      circle(g, `rgba(255,255,255,${0.85 * (1 - u)})`, 1.4 + u * 2.4 - i * 0.3, 44 - u * 16 - i * 2.5, 75 + u * 4 + i);
      circle(g, `rgba(170,165,215,${0.35 * (1 - u)})`, 1.4 + u * 2.4 - i * 0.3, 44 - u * 16 - i * 2.5, 75 + u * 4 + i + 0.4);
    }
    const d = clamp((tm - 0.9) / 0.8, 0, 1);
    if (d > 0 && d < 1) {
      const x = 74 + d * 12, y = 50 - sin(PI * d) * 7 + d * 6;
      const p = new Path2D(); p.moveTo(x, y - 2); p.quadraticCurveTo(x + 1.6, y + 0.4, x, y + 1.1); p.quadraticCurveTo(x - 1.6, y + 0.4, x, y - 2);
      g.fillStyle = `rgba(126,200,255,${1 - d * 0.6})`; g.fill(p);
    }
  }
  function drawSweat(g, t) {
    // sequência em risco: uma gotinha de suor de vez em quando
    const ph = (t % 4.5) / 4.5; if (ph > 0.55) return;
    const a = ph < 0.1 ? ph / 0.1 : 1 - Math.max(0, ph - 0.4) / 0.15, y = 51 + ph * 6;
    const p = new Path2D(); p.moveTo(80, y - 2.2); p.quadraticCurveTo(81.8, y + 0.4, 80, y + 1.2); p.quadraticCurveTo(78.2, y + 0.4, 80, y - 2.2);
    g.fillStyle = `rgba(126,200,255,${0.9 * a})`; g.fill(p); circle(g, `rgba(255,255,255,${0.8 * a})`, 0.35, 79.6, y - 0.1);
  }
  function drawMug(g, t) {
    // de manhã: a caneca de café do lado do livro, fumegando
    const x = 99, y = 85.5, w = 8.4, h = 9.2;
    g.save(); g.translate(x + w / 2, 95); g.scale(1, 0.22); circle(g, rgba(INK, 0.22), 6, 0, 0); g.restore();
    const hd = new Path2D(); hd.ellipse(x + w + 0.2, y + 4.4, 2.6, 2.8, 0, -PI / 2, PI / 2); stroke(g, hd, "#E9E5F5", 1.5); stroke(g, hd, rgba(INK, 0.25), 0.3);
    const body = new Path2D(); body.moveTo(x, y); body.lineTo(x + w, y); body.lineTo(x + w - 0.3, y + h - 1.6); body.quadraticCurveTo(x + w - 0.5, y + h, x + w - 2, y + h); body.lineTo(x + 2, y + h); body.quadraticCurveTo(x + 0.5, y + h, x + 0.3, y + h - 1.6); body.closePath();
    g.fillStyle = hGrad(g, ["#FFFFFF", "#F2EFFA", "#D9D4EE"], x, x + w); g.fill(body);
    g.save(); g.clip(body); g.fillStyle = "#3A2FC4"; g.fillRect(x, y + 3.6, w, 2.4); g.restore();
    oval(g, "#6B4226", x + 0.5, y - 0.7, w - 1, 1.8); oval(g, "rgba(255,230,200,0.35)", x + 1.6, y - 0.5, 2.4, 0.6);
    stroke(g, body, rgba(INK, 0.25), 0.3);
    for (let i = 0; i < 2; i++) {
      const ph = (t * 0.45 + i * 0.5) % 1, a = sin(PI * ph) * 0.55, sx = x + 2.6 + i * 3;
      const st = new Path2D(); st.moveTo(sx, y - 1.5 - ph * 3); st.bezierCurveTo(sx - 1.5, y - 3 - ph * 4, sx + 1.5, y - 5 - ph * 4, sx, y - 7 - ph * 5);
      stroke(g, st, `rgba(255,255,255,${a})`, 0.8); stroke(g, st, `rgba(120,110,170,${a * 0.35})`, 0.3);
    }
  }
  function drawMoon(g, t) {
    // à noite: lua minguante e duas estrelinhas piscando
    g.save();
    const cut = new Path2D(); cut.rect(-40, -40, 180, 180); cut.arc(13.6, 11.2, 4.6, 0, 2 * PI);
    g.clip(cut, "evenodd");
    g.beginPath(); g.arc(11, 13, 5, 0, 2 * PI); g.fillStyle = vGrad(g, ["#FFF4C8", "#FFD877"], 8, 18); g.fill();
    g.restore();
    star4(g, 21, 7, 1.3 + 0.5 * sin(t * 2.1), "rgba(255,230,150,0.9)");
    star4(g, 4, 22, 1 + 0.4 * sin(t * 1.7 + 2), "rgba(255,230,150,0.8)");
  }
  function drawFlames(g, t, small) {
    // em chamas: labaredas tremulando dos dois lados
    for (let i = 0; i < (small ? 2 : 4); i++) {
      const side = i % 2 ? 1 : -1, row = Math.floor(i / 2);
      const x = 50 + side * (44 + row * 6), base = 92 - row * 14, hgt = 9 + 2.5 * sin(t * 9 + i * 1.7) + (small ? 3 : 0), wd = 3.4 + (small ? 1 : 0);
      const p = new Path2D(); p.moveTo(x - wd, base); p.bezierCurveTo(x - wd * 1.1, base - hgt * 0.5, x - wd * 0.2 + sin(t * 7 + i) * 0.8, base - hgt * 0.7, x + sin(t * 6 + i) * 1.2, base - hgt);
      p.bezierCurveTo(x + wd * 0.6, base - hgt * 0.6, x + wd * 1.1, base - hgt * 0.45, x + wd, base); p.quadraticCurveTo(x, base + 1.8, x - wd, base);
      g.fillStyle = vGrad(g, ["#FFE07A", "#FF9F3A", "#F2552C"], base - hgt, base); g.fill(p);
      const c = new Path2D(); c.ellipse(x, base - hgt * 0.28, wd * 0.45, hgt * 0.26, 0, 0, 2 * PI); g.fillStyle = "rgba(255,248,200,0.85)"; g.fill(c);
    }
  }

  function star4(g, x, y, r, color) {
    const p = new Path2D(); p.moveTo(x, y - r); p.quadraticCurveTo(x, y, x + r, y); p.quadraticCurveTo(x, y, x, y + r); p.quadraticCurveTo(x, y, x - r, y); p.quadraticCurveTo(x, y, x, y - r);
    g.fillStyle = color; g.fill(p);
  }
  function drawSparkles(g, t, small) {
    for (let i = 0; i < (small ? 2 : 4); i++) {
      const ph = (t * 0.9 + i * 0.27) % 1, a = sin(PI * ph);
      const [x, y] = [[14, 30], [86, 26], [8, 52], [93, 50]][i];
      star4(g, x, y, (small ? 3.4 : 2.6) * a, rgba(GOLD, 0.95 * a));
    }
  }
  function drawZzz(g, t, small) {
    g.save(); g.font = "800 7px Nunito, system-ui, sans-serif"; g.textAlign = "center";
    for (let i = 0; i < (small ? 2 : 3); i++) {
      const ph = (t * 0.35 + i / 3) % 1, a = sin(PI * ph);
      g.globalAlpha = a; g.fillStyle = INK;
      g.save(); g.translate(76 + ph * 14 + sin(ph * 6) * 2, 34 - ph * 22); g.scale(0.6 + ph * 0.7, 0.6 + ph * 0.7); g.fillText("z", 0, 0); g.restore();
    }
    g.restore();
  }
  function drawDizzyStars(g, t) {
    for (let i = 0; i < 4; i++) { const a = t * 3.2 + i * PI / 2; star4(g, 50 + cos(a) * 22, 22 + sin(a) * 5, 2.4, i % 2 ? SPARK : MINT); }
  }
  function drawFocusMarks(g, t) {
    // rabisco aparecendo debaixo do lápis
    const k = (t * 0.45) % 1, n = Math.floor(k * 24);
    g.save(); g.strokeStyle = INK; g.globalAlpha = 0.55; g.lineWidth = 0.6; g.lineCap = "round"; g.beginPath();
    for (let i = 0; i <= n; i++) { const x = 60 + i * 0.95, y = 81.5 + sin(i * 1.7) * 0.7; i ? g.lineTo(x, y) : g.moveTo(x, y); }
    g.stroke(); g.restore();
  }
  function drawCelebration(g, t, small) {
    const colors = [SPARK, MINT, CHEEK];
    for (let i = 0; i < (small ? 3 : 6); i++) {
      const p = (t * 0.7 + i * 0.17) % 1, side = i % 2 === 0 ? -1 : 1;
      const x = 50 + side * (30 + i * 3), y = 70 - p * 50, rr = 2.6 * (1 - p) * 0.9;
      if (i % 3 === 2) { g.save(); g.translate(x, y); g.rotate(t * 4 + i); g.fillStyle = rgba(colors[i % 3], 1 - p); g.fillRect(-rr, -rr * 0.5, rr * 2, rr); g.restore(); }
      else circle(g, rgba(colors[i % 3], 1 - p), rr, x, y);
    }
  }

  // ---- braços e mãos
  function drawArms(g, s, which = "LR") {
    const P = s.P;
    const look = s.look || resolveLook(null);
    if (which.includes("L")) drawArm(g, [12, 66], [P.hL0, P.hL1], P.bendL, -1, P.shapeL, P.rotL, s.popL, s.small, look, s.t);
    if (which.includes("R")) drawArm(g, [88, 66], [P.hR0, P.hR1], P.bendR, 1, P.shapeR, P.rotR, s.popR, s.small, look, s.t);
  }
  function drawPencil(g, x, y, small) {
    // lápis amarelo inclinado, ponta encostando na página, atrás do punho
    g.save(); g.translate(x, y); g.rotate(0.5);
    const L = 12, R = small ? 1.3 : 1.05;
    g.fillStyle = "#F5B83D"; g.fillRect(-R, -L * 0.75, R * 2, L * 0.75);
    g.fillStyle = "#E39A1E"; g.fillRect(R * 0.2, -L * 0.75, R * 0.8, L * 0.75);
    g.fillStyle = "#C8CAD6"; g.fillRect(-R, -L * 0.75 - 1.4, R * 2, 1.4);
    g.fillStyle = "#F59AB8"; g.beginPath(); g.ellipse(0, -L * 0.75 - 2.2, R, 1.1, 0, 0, 2 * PI); g.fill();
    g.fillStyle = "#F0D3A8"; g.beginPath(); g.moveTo(-R, 0); g.lineTo(R, 0); g.lineTo(0, 3.1); g.closePath(); g.fill();
    g.fillStyle = INK; g.beginPath(); g.moveTo(-0.45, 2.1); g.lineTo(0.45, 2.1); g.lineTo(0, 3.2); g.closePath(); g.fill();
    g.restore();
  }
  function drawArm(g, sh, h, bend, side, shape, rot, pop, small, look, t) {
    const [armC, armL] = (look && look.arm) || [ARM, ARM_L], glove = (look && look.gloves) || GLOVES.branca;
    // braço de borracha que afina: curva do ombro à mão, com "cotovelo" (bend) e brilho em cima
    const mx = (sh[0] + h[0]) / 2, my = (sh[1] + h[1]) / 2, dx = h[0] - sh[0], dy = h[1] - sh[1], L = Math.hypot(dx, dy) || 1;
    const nx = -dy / L, ny = dx / L;
    const cx = mx + nx * bend * side + side * 1.5, cy = my + ny * bend * side + 2;
    const N = 14, w0 = small ? 2.3 : 1.9, w1 = small ? 1.7 : 1.3, L1 = [], R1 = [];
    for (let i = 0; i <= N; i++) {
      const u = i / N, a = (1 - u) * (1 - u), b = 2 * (1 - u) * u, c = u * u;
      const x = a * sh[0] + b * cx + c * h[0], y = a * sh[1] + b * cy + c * h[1];
      const tx = 2 * (1 - u) * (cx - sh[0]) + 2 * u * (h[0] - cx), ty = 2 * (1 - u) * (cy - sh[1]) + 2 * u * (h[1] - cy), tl = Math.hypot(tx, ty) || 1;
      const w = lerp(w0, w1, u);
      L1.push([x - ty / tl * w, y + tx / tl * w]); R1.push([x + ty / tl * w, y - tx / tl * w]);
    }
    const p = new Path2D(); p.moveTo(...L1[0]); for (const q of L1) p.lineTo(...q); for (let i = R1.length - 1; i >= 0; i--) p.lineTo(...R1[i]); p.closePath();
    g.fillStyle = armC; g.fill(p);
    const edgeSide = side < 0 ? R1 : L1, hl = new Path2D(); hl.moveTo(...edgeSide[1]); for (let i = 2; i < N - 1; i++) hl.lineTo(...edgeSide[i]);
    stroke(g, hl, rgba(armL, 0.7), 0.6);
    // mão: na direção do fim do braço, mais o giro do humor; "pop" quando troca de forma
    if (shape === "pen") drawPencil(g, h[0] - 2.4, h[1] + 3.2, small);
    const ang = Math.atan2(h[1] - cy, h[0] - cx) + rot * PI / 180;
    // o item da mão esquerda vai junto com a mão (some quando a mão abre para pegar a folha ou no susto)
    if (side < 0 && look && look.hand && look.hand !== "nenhum" && (shape === "mitt" || shape === "fist")) drawHeld(g, look.hand, h, ang - PI, t || 0, small);
    g.save(); g.translate(h[0], h[1]); g.rotate(ang); if (side < 0) g.scale(1, -1);
    const sc = (small ? 1.35 : 1.25) * (1 + 0.28 * pop); g.scale(sc, sc);
    drawHand(g, shape, small, armC, glove);
    g.restore();
  }
  function drawHand(g, shape, small, armC, glove) {
    glove = glove || GLOVES.branca;
    const OUT = armC || ARM, ow = small ? 1.0 : 0.6, kind = glove[2];
    if (kind === "boxe") {   // luva de boxe: a mão vira a luva, em qualquer gesto
      const cuff = new Path2D(); cuff.roundRect(-2.8, -2.7, 2.6, 5.4, 1); g.fillStyle = "#FFFFFF"; g.fill(cuff); g.strokeStyle = "#8E1A22"; g.lineWidth = ow; g.stroke(cuff);
      line(g, "#E5373F", -1.5, -2.6, -1.5, 2.6, 0.5, false);
      const th = new Path2D(); th.ellipse(1.2, -3, 1.7, 1.3, -0.5, 0, 2 * PI);
      const gl = new Path2D(); gl.ellipse(2.6, 0.1, 4, 3.6, 0, 0, 2 * PI);
      const fillG = g.createRadialGradient(1.6, -1.4, 0.4, 2.6, 0, 4.4); fillG.addColorStop(0, "#FF7A80"); fillG.addColorStop(1, "#C3212B");
      for (const p of [gl, th]) { g.fillStyle = fillG; g.fill(p); g.strokeStyle = "#8E1A22"; g.lineWidth = ow; g.stroke(p); }
      g.beginPath(); g.ellipse(1.6, -1.5, 1.4, 0.8, -0.4, 0, 2 * PI); g.fillStyle = "rgba(255,255,255,0.55)"; g.fill();
      return;
    }
    let fill = glove[0];
    if (kind === "rainbow") { const rg = g.createLinearGradient(-1, -3, 5, 3); ["#FF8FA3", "#FFD84A", "#7EE0B8", "#6FB0FF", "#B995F5"].forEach((c, i) => rg.addColorStop(i / 4, c)); fill = rg; }
    // punho da luva e pulseira dourada
    g.beginPath(); g.ellipse(-1.2, 0, 1.6, 2.5, 0, 0, 2 * PI); g.fillStyle = glove[1]; g.fill(); g.strokeStyle = OUT; g.lineWidth = ow; g.stroke();
    line(g, GOLD, -1.2, -2.2, -1.2, 2.2, 0.7, false);
    const outline = (path) => { g.fillStyle = fill; g.fill(path); g.strokeStyle = OUT; g.lineWidth = ow; g.lineJoin = "round"; g.stroke(path); };
    const finger = (x, y, a, len, r) => { const p = new Path2D(); p.moveTo(x, y); p.lineTo(x + cos(a) * len, y + sin(a) * len); g.lineCap = "round"; g.strokeStyle = OUT; g.lineWidth = r * 2 + ow * 2; g.stroke(p); g.strokeStyle = fill; g.lineWidth = r * 2; g.stroke(p); };
    if (shape === "open") {
      [-0.95, -0.35, 0.25, 0.85].forEach((a, i) => finger(2.2, 0, a * 0.8, 2.6 + (i === 1 || i === 2 ? 0.5 : 0), 0.95));
      finger(0.9, -1.8, -1.9, 1.9, 0.95);
      const palm = new Path2D(); palm.ellipse(1.6, 0, 2.6, 2.5, 0, 0, 2 * PI); outline(palm);
      if (!small) line(g, rgba(OUT, 0.25), 2.6, -0.8, 3.1, 0.8, 0.35);
    } else if (shape === "fist" || shape === "pen") {
      const th = new Path2D(); th.ellipse(1.2, -2.3, 1.5, 1.0, -0.4, 0, 2 * PI); outline(th);
      const f = new Path2D(); f.ellipse(1.7, 0, 3.1, 2.9, 0, 0, 2 * PI); outline(f);
      if (!small) { const k = new Path2D(); k.moveTo(3.4, -1.1); k.quadraticCurveTo(4.4, 0, 3.4, 1.1); stroke(g, k, rgba(OUT, 0.3), 0.4); }
    } else if (shape === "point") {
      finger(3.2, -0.6, 0, 4.2, 0.95);
      const th = new Path2D(); th.ellipse(1.4, -1.9, 1.4, 0.9, -0.3, 0, 2 * PI); outline(th);
      const f = new Path2D(); f.ellipse(1.6, 0.4, 2.8, 2.5, 0, 0, 2 * PI); outline(f);
    } else { // luva relaxada (a bolinha de sempre, com polegar)
      const th = new Path2D(); th.ellipse(1.0, -2.6, 1.2, 1.6, -0.6, 0, 2 * PI); outline(th);
      const palm = new Path2D(); palm.ellipse(1.7, 0, 3.1, 3.0, 0, 0, 2 * PI); outline(palm);
      if (!small) { const fl = new Path2D(); fl.moveTo(3.4, -1.2); fl.quadraticCurveTo(4.6, 0, 3.6, 1.4); stroke(g, fl, rgba(OUT, 0.3), 0.4); }
    }
    // brilho de luva (dá volume e deixa a cor da luva aparecer)
    if (!small && shape !== "open") { g.beginPath(); g.ellipse(1, -1.1, 1.2, 0.7, -0.4, 0, 2 * PI); g.fillStyle = "rgba(255,255,255,0.45)"; g.fill(); }
  }

  // ------------------------------------------------------------------ animação
  const ease = (x) => 1 - (1 - x) * (1 - x);
  const live = new Set();
  let rafId = 0;
  function tick(now) { rafId = 0; for (const el of live) el._frame(now); if (live.size) rafId = requestAnimationFrame(tick); }
  function wake() { if (!rafId && live.size) rafId = requestAnimationFrame(tick); }

  const MOODS = { idle: "idle", thinking: "thinking", talking: "talking", happy: "happy", sad: "sad", wave: "wave", point: "point", wink: "idle",
    surprised: "surprised", proud: "proud", focus: "focus", sleepy: "sleepy", dizzy: "dizzy", relieved: "relieved", ecstatic: "ecstatic", present: "present" };

  // Onde está o ponteiro e quando a pessoa mexeu pela última vez (para os olhos seguirem e para ele cochilar).
  const POINTER = { x: 0, y: 0, at: -1e9 };
  let lastActivity = performance.now();
  const touch = (e) => { lastActivity = performance.now(); if (e && e.clientX != null) { POINTER.x = e.clientX; POINTER.y = e.clientY; POINTER.at = lastActivity; } };
  ["pointermove", "pointerdown"].forEach((ev) => window.addEventListener(ev, touch, { passive: true }));
  ["keydown", "scroll", "wheel", "touchstart"].forEach((ev) => window.addEventListener(ev, () => { lastActivity = performance.now(); }, { passive: true }));
  const DOZE_AFTER = 40000;            // parado 40 s sem ninguém mexer: cochila
  const LEAVE_WAVE = 480, LEAVE_MS = 760;  // saída: tchau, depois some subindo

  // Fala: o mesmo ritmo do FolhaTalking (16 ms por letra, pausas em espaço, vírgula e ponto).
  function speechPlan(text) {
    const out = []; let at = 0;
    for (const ch of text) { out.push([at, ch]); at += /[.!?:]/.test(ch) ? 220 : ch === "," ? 110 : ch === " " ? 18 : 16; }
    return { steps: out, end: at };
  }
  function mouthFor(ch) {
    const c = (ch || "").toLowerCase();
    if (/[aáàâã]/.test(c)) return { open: 1, round: 0 };
    if (/[oóôõ]/.test(c)) return { open: 0.8, round: 0.8 };
    if (/[uú]/.test(c)) return { open: 0.45, round: 1 };
    if (/[eéêiíy]/.test(c)) return { open: 0.55, round: 0 };
    if (/[mbp]/.test(c)) return { open: 0, round: 0 };
    if (/[a-zç]/.test(c)) return { open: 0.28, round: 0.1 };
    if (c === " ") return { open: 0.12, round: 0 };
    return { open: 0, round: 0 };
  }

  class EstudarioFolha extends HTMLElement {
    static get observedAttributes() { return ["mood", "size", "say"]; }

    constructor() {
      super();
      this._start = performance.now(); this._last = 0;
      this._blink = 0; this._blinkPlan = []; this._nextBlink = this._start + 1800 + Math.random() * 2800;
      this._look = { x: 0, y: 0, fx: 0, fy: 0, tx: 0, ty: 0, from: 0, dur: 1, next: 0 };
      this._bounceAt = -1; this._wink = false; this._winkUntil = 0;
      this._visible = true;
      this._st = null; this._sq = { x: { s: 0 }, v: {} }; this._mood = null; this._shape = { L: "mitt", R: "mitt" }; this._pop = { L: 0, R: 0 };
      this._prevVy = 0;
    }

    connectedCallback() {
      if (!this._canvas) {
        this._canvas = document.createElement("canvas");
        this._canvas.style.display = "block";
        this.appendChild(this._canvas);
        this.setAttribute("role", "img");
        if (!this.hasAttribute("aria-label")) this.setAttribute("aria-label", "Folha, do Estudário");
        this.addEventListener("click", () => this.poke());
        // chega pulando na tela (cai de leve, amassa ao pousar), a não ser que peça para entrar parado
        if (!reduceMotion.matches && !this.hasAttribute("noenter")) this._enterAt = performance.now();
      }
      this._size();
      if ("IntersectionObserver" in window) {
        this._io = new IntersectionObserver((entries) => {
          this._visible = entries.some((e) => e.isIntersecting);
          if (this._visible) { live.add(this); wake(); } else live.delete(this);
        });
        this._io.observe(this);
      }
      live.add(this); wake();
    }
    disconnectedCallback() { live.delete(this); if (this._io) this._io.disconnect(); }
    attributeChangedCallback(name) {
      if (name === "size") this._size();
      if (name === "say") { const t = this.getAttribute("say"); this._say = t ? speechPlan(t) : null; this._sayAt = performance.now(); }
    }
    get mood() { return MOODS[this.getAttribute("mood") || "idle"] || "idle"; }

    /** Toque: amassa, pula e pisca um olho. */
    poke() {
      const now = performance.now();
      this._bounceAt = now; this._wink = true; this._winkUntil = now + 110 + 600;
      this._taps = (this._taps || []).filter((x) => now - x < 1400); this._taps.push(now);
      if (this._taps.length >= 4) { this._dizzyUntil = now + 2200; this._taps = []; }
      this._burstAt = now;
      live.add(this); wake();
    }

    /** Saída: acena tchau e sobe sumindo. Quem fecha a tela espera LEAVE_MS (veja folhaLeave). */
    leave() {
      if (reduceMotion.matches) return;
      this._leaveAt = performance.now(); live.add(this); wake();
    }

    _size() {
      if (!this._canvas) return;
      const size = parseInt(this.getAttribute("size") || "96", 10);
      this._small = size < SMALL;
      this.style.width = size + "px"; this.style.height = size + "px";
      // O desenho passa da grade 100 x 100 (mãos para o alto, aceno, borla): o canvas sobra MARGIN% para cada
      // lado, fora da caixa do elemento, sem pegar cliques.
      const cs = getComputedStyle(this);
      if (cs.position === "static") this.style.position = "relative";
      // elemento novo nasce "inline" (largura e altura ignoradas): sem CSS da página, vira bloco em linha
      if (cs.display === "inline") this.style.display = "inline-block";
      const c = this._canvas.style, M = MARGIN;
      c.position = "absolute"; c.left = c.top = -M + "%"; c.width = c.height = 100 + 2 * M + "%"; c.pointerEvents = "none";
      const dpr = Math.min(window.devicePixelRatio || 1, 2.5), full = size * (1 + 2 * M / 100);
      this._canvas.width = Math.round(full * dpr); this._canvas.height = Math.round(full * dpr); this._px = full * dpr;
    }

    _updateBlink(now) {
      if (now >= this._nextBlink && !this._blinkPlan.length) {
        const twice = Math.random() < 0.2; let at = now;
        for (let i = 0; i < (twice ? 2 : 1); i++) { this._blinkPlan.push(at); at += 70 + 110 + 90; }
        this._nextBlink = at + 1800 + Math.random() * 2800;
      }
      this._blink = 0;
      this._blinkPlan = this._blinkPlan.filter((start) => {
        const d = now - start;
        if (d < 0) return true;
        if (d < 70) { this._blink = Math.max(this._blink, d / 70); return true; }
        if (d < 180) { this._blink = Math.max(this._blink, 1 - (d - 70) / 110); return true; }
        return false;
      });
    }
    _updateLook(now, mood) {
      const L = this._look;
      // ponteiro mexendo por perto: os olhos acompanham
      if (now - POINTER.at < 2500 && /^(idle|talking|wave|happy|point|proud|surprised)$/.test(mood)) {
        if (!this._rect || now - this._rectAt > 400) { this._rect = this.getBoundingClientRect(); this._rectAt = now; }
        const R = this._rect, cx = R.left + R.width / 2, cy = R.top + R.height * 0.6;
        const tx = clamp((POINTER.x - cx) / 260, -1, 1), ty = clamp((POINTER.y - cy) / 260, -1, 1);
        L.x += (tx - L.x) * 0.18; L.y += (ty - L.y) * 0.18; L.fx = L.x; L.fy = L.y; L.tx = L.x; L.ty = L.y; L.from = now; L.dur = 1; L.next = now + 900;
        return;
      }
      const p = clamp((now - L.from) / L.dur, 0, 1);
      L.x = L.fx + (L.tx - L.fx) * ease(p); L.y = L.fy + (L.ty - L.fy) * ease(p);
      if (p < 1 || now < L.next) return;
      L.fx = L.x; L.fy = L.y; L.from = now;
      if (mood === "thinking") { const toLeft = L.x > 0; L.tx = toLeft ? -1 : 1; L.ty = 0.6; L.dur = toLeft ? 260 : 900; L.next = now + L.dur; }
      else if (mood === "idle") { L.tx = Math.random() * 2 - 1; L.ty = Math.random() * 1.2 - 0.6; L.dur = 380; L.next = now + 380 + 900 + Math.random() * 1700; }
      else if (mood === "sad") { L.tx = (Math.random() - 0.5) * 0.6; L.ty = 0.9; L.dur = 700; L.next = now + 1800; }
      else { L.tx = 0; L.ty = 0; L.dur = 300; L.next = now + 1100; }
    }
    _bounce(now) {
      if (this._bounceAt < 0) return 0;
      const d = now - this._bounceAt;
      if (d < 110) return d / 110;
      const s = (d - 110) / 1000;
      if (s > 1.4) { this._bounceAt = -1; return 0; }
      return Math.exp(-s * 5.5) * cos(s * 17);
    }

    _fidget(now, mood) {
      if (mood !== "idle") { this._fid = null; this._nextFid = now + 5000 + Math.random() * 4000; return null; }
      if (!this._nextFid) this._nextFid = now + 6000 + Math.random() * 5000;
      if (!this._fid && now > this._nextFid) {
        const kinds = [["cap", 1700], ["yawn", 2300], ["glance", 1500], ["bounce", 1100]];
        let k = kinds[Math.floor(Math.random() * kinds.length)];
        if (this._lastFid === k[0]) k = kinds[(kinds.indexOf(k) + 1) % kinds.length];
        this._fid = { kind: k[0], start: now, dur: k[1], side: Math.random() < 0.5 ? -1 : 1 }; this._lastFid = k[0];
      }
      if (!this._fid) return null;
      const u = (now - this._fid.start) / this._fid.dur;
      if (u >= 1) { this._fid = null; this._nextFid = now + 7000 + Math.random() * 6000; return null; }
      return { kind: this._fid.kind, u, side: this._fid.side };
    }

    // Avança as molas até a pose do humor. Na troca de humor: antecipação (amassa) e "pop" nas mãos.
    _advance(mood, t, dt, fid, ctx) {
      const T = pose(mood, t, this._look, fid, ctx);
      const flat = { ...T, hL0: T.hL[0], hL1: T.hL[1], hR0: T.hR[0], hR1: T.hR[1] };
      delete flat.hL; delete flat.hR; delete flat.shapeL; delete flat.shapeR;
      if (!this._st) {
        this._st = { x: { ...flat }, v: {} }; this._mood = mood; this._shape = { L: T.shapeL, R: T.shapeR };
        this._enterPending = !!this._enterAt;
      }
      if (mood !== this._mood) {
        this._mood = mood;
        this._sq.v.s = (this._sq.v.s || 0) + 9;                                    // antecipação: agacha antes de mudar
        for (const k of ["hL1", "hR1"]) this._st.v[k] = (this._st.v[k] || 0) + 40;  // o braço volta antes de ir
      }
      for (const side of ["L", "R"]) {
        const want = T["shape" + side];
        if (want !== this._shape[side]) { this._shape[side] = want; this._pop[side] = 1; }
        this._pop[side] = Math.max(0, this._pop[side] - dt * 4);
      }
      if (dt <= 0) { this._st.x = { ...flat }; this._st.v = {}; return; }
      if (this._enterPending) { this._enterPending = false; this._st.x.hop = -16; this._st.x.hL1 -= 10; this._st.x.hR1 -= 10; }   // entrada: vem do alto
      const steps = Math.ceil(dt / (1 / 120)), h = dt / steps;
      const handK = mood === "thinking" ? 700 : mood === "focus" ? 600 : 120;
      for (let i = 0; i < steps; i++) for (const k in flat) springTo(this._st, k, flat[k], h, FAST.has(k) ? 420 : SLOW.has(k) ? handK : 200);
      // amasso de aterrissagem: vinha descendo rápido e chegou no chão
      const vy = this._st.v.hop || 0;
      if (this._prevVy > 18 && vy < this._prevVy * 0.4 && this._st.x.hop > -1) this._sq.v.s = (this._sq.v.s || 0) + this._prevVy * 0.12;
      this._prevVy = vy;
      for (let i = 0; i < steps; i++) springTo(this._sq, "s", 0, h, 260);
    }

    _frame(now) {
      if (!this._visible || !this._canvas) return;
      const g = this._canvas.getContext("2d");
      if (!g) return;
      let mood = this.mood; const still = reduceMotion.matches;
      if (!still && this._dizzyUntil && now < this._dizzyUntil) mood = "dizzy";
      else if (!still && mood === "idle" && now - lastActivity > DOZE_AFTER) mood = "sleepy";
      if (this._wasAsleep && mood !== "sleepy") { this._bounceAt = now; this._lastFid = null; }   // acordou num susto
      this._wasAsleep = mood === "sleepy" && this.mood === "idle";
      // saída: tchau por um instante, depois sobe sumindo
      let fade = this._enterAt ? clamp((now - this._enterAt) / 160, 0, 1) : 1, rise = 0;
      if (this._leaveAt) {
        const el = now - this._leaveAt; mood = "wave";
        if (el > LEAVE_WAVE) { const u = clamp((el - LEAVE_WAVE) / (LEAVE_MS - LEAVE_WAVE), 0, 1); fade = 1 - u; rise = u * u * 30; }
      }
      this._canvas.style.opacity = fade < 1 ? String(fade) : "";
      this._canvas.style.transform = rise ? `translateY(${-rise}%)` : "";
      if (mood !== this._moodName) { this._moodName = mood; this._moodAt = now; }
      const look = resolveLook(this);
      const t = still ? 0.35 : (now - this._start) / 1000;
      const dt = still || !this._last ? 0 : Math.min(0.05, (now - this._last) / 1000);
      this._last = now;
      if (!still) { this._updateBlink(now); this._updateLook(now, mood); }
      else { this._look.x = 0.3; this._look.y = -0.2; }
      if (this._wink && now > this._winkUntil) this._wink = false;
      const tm = still ? 9 : (now - this._moodAt) / 1000;
      this._advance(mood, t, dt, still ? null : this._fidget(now, mood), { tm, worried: look.atRisk, holding: look.hand && look.hand !== "nenhum" });
      // fala: a boca segue a letra atual do texto (com mola, para não tremer)
      let speech = null;
      if (this._say && mood === "talking") {
        const el = now - this._sayAt, st = this._say.steps;
        let ch = "";
        if (el < this._say.end) { let lo = 0, hi = st.length - 1; while (lo < hi) { const m = (lo + hi + 1) >> 1; if (st[m][0] <= el) lo = m; else hi = m - 1; } ch = st[lo][1]; }
        const want = mouthFor(ch), k = dt ? 1 - Math.exp(-dt * 28) : 1;
        this._mouth = this._mouth || { open: 0, round: 0 };
        this._mouth.open += (want.open - this._mouth.open) * k; this._mouth.round += (want.round - this._mouth.round) * k;
        speech = this._mouth;
      }
      const winkAttr = this.getAttribute("mood") === "wink";
      g.setTransform(1, 0, 0, 1, 0, 0);
      g.clearRect(0, 0, this._canvas.width, this._canvas.height);
      const k = this._px / (100 + 2 * MARGIN);
      g.setTransform(k, 0, 0, k, MARGIN * k, MARGIN * k);
      drawFolha(g, {
        t, mood, small: this._small,
        P: { ...this._st.x, shapeL: this._shape.L, shapeR: this._shape.R },
        vy: this._st.v.hop || 0,
        blink: still ? 0 : this._blink,
        wink: this._wink || winkAttr ? 1 : 0,
        popL: this._pop.L, popR: this._pop.R,
        squash: Math.max(this._bounce(now), (this.gulp || 0) * 0.45) + clamp(this._sq.x.s * 0.1, -0.2, 0.25),
        gulp: still ? 0 : (this.gulp || 0),
        speech, look, tm,
        burst: this._burstAt ? clamp(1 - (now - this._burstAt) / 650, 0, 1) : 0,
      });
      if (still && this._bounceAt < 0) live.delete(this);
    }
  }

  customElements.define("estudario-folha", EstudarioFolha);

  // Folha parado (miniaturas e botões): o mesmo desenho do 2.0, um quadro só, em PNG. Guarda em cache por
  // humor e tamanho. folhaPng({ mood, size }) -> data URL.
  const STATIC = new Map();
  window.folhaPng = function (o) {
    const mood = MOODS[(o && o.mood) || "happy"] || "idle", size = Math.max(8, Math.round((o && o.size) || 96));
    // peças no pedido ({ hat: "coroa" }...): vitrine do armário (só as peças, sem medalha nem hora do dia)
    const shelf = o && SLOTS.some((k) => o[k]);
    const look = resolveLook(null, shelf ? Object.assign({ plain: true }, o) : null);
    const dpr = Math.min(window.devicePixelRatio || 1, 2.5);
    const key = [mood, size, dpr, shelf ? "shelf" : "", ...SLOTS.map((k) => (shelf && o[k]) || LOOK[k]),
      shelf ? 0 : look.streak >= 100 ? 3 : look.streak >= 30 ? 2 : look.streak >= 7 ? 1 : 0, look.night, look.morning].join(":");
    if (STATIC.has(key)) return STATIC.get(key);
    const T = pose(mood === "thinking" ? "idle" : mood, 0.35, { x: 0.3, y: -0.2 }, null, { tm: 9, holding: look.hand && look.hand !== "nenhum" });
    const P = { ...T, hL0: T.hL[0], hL1: T.hL[1], hR0: T.hR[0], hR1: T.hR[1] };
    const c = document.createElement("canvas"); c.width = c.height = Math.round(size * dpr);
    const pet = look.pet && look.pet !== "nenhum" && size >= SMALL;
    const g = c.getContext("2d"), k = c.width / 100, fit = pet ? 0.76 : mood === "present" ? 0.78 : 0.9;      // cabe a mão para o alto (e o companheiro) dentro da caixa
    g.setTransform(k * fit, 0, 0, k * fit, c.width * (1 - fit) / 2 + (pet ? k * 7 : 0), c.width * (1 - fit) / 2 + k * (pet ? 1 : mood === "present" ? 5 : 2));
    drawFolha(g, { t: 0.35, mood: mood === "thinking" ? "idle" : mood, small: size < SMALL, P, vy: 0, blink: 0, wink: mood === "wink" ? 1 : 0, popL: 0, popR: 0, squash: 0, gulp: 0, look, tm: 9 });
    const url = c.toDataURL("image/png"); STATIC.set(key, url); return url;
  };

  // O site avisa o que a pessoa escolheu no armário e o contexto do dia: { cover, ribbon, hat, eyes, neck, hand, aura, gloves, streak, atRisk }.
  window.folhaSetLook = function (o) {
    let changed = false;
    o = Object.assign({}, o || {});
    if (o.acc && LEGACY_ACC[o.acc]) { const [k, v] = LEGACY_ACC[o.acc]; if (!o[k]) o[k] = v; }
    for (const k in o) if (k in LOOK && LOOK[k] !== o[k]) { LOOK[k] = o[k]; changed = true; }
    if (!changed) return;
    document.querySelectorAll("estudario-folha").forEach((el) => { if (el.isConnected) live.add(el); });
    wake();
    window.dispatchEvent(new Event("folha-look"));
  };
  // Saída das telas que fecham: os Folhas dentro de `root` acenam e somem; `done` roda quando terminam.
  window.folhaLeave = function (root, done) {
    const els = root ? Array.from(root.querySelectorAll("estudario-folha")) : [];
    if (!els.length || reduceMotion.matches) { done(); return; }
    els.forEach((el) => el.leave());
    setTimeout(done, LEAVE_MS);
  };
  if (reduceMotion.addEventListener) reduceMotion.addEventListener("change", () => {
    document.querySelectorAll("estudario-folha").forEach((el) => { if (el.isConnected) live.add(el); });
    wake();
  });
})();
