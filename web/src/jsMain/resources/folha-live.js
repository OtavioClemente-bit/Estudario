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
// Uso: <estudario-folha mood="idle|thinking|talking|happy|sad|wave|point|wink" size="96" clickable>
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
  function pose(mood, t, look, fid) {
    const p = {
      hop: sin(t * 2.1) * 1.4, tilt: sin(t * 0.9) * 1.2,
      hL: [REST_L[0], REST_L[1] + sin(t * 2.1) * 1.2], hR: [REST_R[0], REST_R[1] - sin(t * 2.1) * 1.2],
      rotL: 0, rotR: 0, bendL: 2, bendR: 2, shapeL: "mitt", shapeR: "mitt",
      up: 0, low: 0, droop: 0, eyeScale: 1, pupil: 1, lookX: look.x, lookY: look.y,
      browY: 0, browIn: 0, browR: 0, browShow: 1,
      mW: 5, mCurve: 3.4, mOpen: 0, cheek: 0.45, capRot: 0, tongue: 0,
    };
    const env = (u) => { u = clamp(u, 0, 1); return sin(PI * u) ** 0.7; };
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
    }
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

    g.save();
    g.translate(0, hop);
    g.translate(50, 88); g.scale(squashX / Math.sqrt(stretch), squashY * stretch); g.translate(-50, -88);
    g.translate(50, 80); g.rotate(tilt * PI / 180); g.translate(-50, -80);
    // quem fica na frente do livro: as duas mãos folheando ou com a cara de susto; só a do lápis no foco
    const front = mood === "thinking" || mood === "surprised" ? "LR" : mood === "focus" ? "R" : "";
    drawArms(g, s, "LR".replace(new RegExp("[" + (front || "_") + "]", "g"), ""));
    drawBook(g, t, mood, small);
    drawPageLines(g, t, mood, small);
    if (!small) drawDogEar(g, t, mood);
    if (mood === "thinking") drawTurningPage(g, t);
    drawFace(g, s);
    g.save(); g.translate(50, 38); g.rotate((P.capRot || 0) * PI / 180); g.translate(-50, -38);
    drawCap(g, t, tilt, s.vy);
    g.restore();
    if (front) drawArms(g, s, front);
    g.restore();
    if (mood === "happy") drawCelebration(g, t, small);
    if (mood === "proud") drawSparkles(g, t, small);
    if (mood === "sleepy") drawZzz(g, t, small);
    if (mood === "dizzy") drawDizzyStars(g, t);
    if (mood === "focus" && !small) drawFocusMarks(g, t);
  }

  function drawCorners(g) {
    const corner = (x, y, sx, sy) => {
      const p = new Path2D();
      p.moveTo(x, y + sy * 6.5); p.lineTo(x, y + sy * 1.2); p.quadraticCurveTo(x, y, x + sx * 1.2, y); p.lineTo(x + sx * 6.5, y);
      p.lineTo(x + sx * 6.5, y + sy * 1.9); p.lineTo(x + sx * 1.9, y + sy * 1.9); p.lineTo(x + sx * 1.9, y + sy * 6.5); p.closePath();
      g.fillStyle = vGrad(g, ["#FFE3A0", GOLD, "#C98A12"], Math.min(y, y + sy * 6.5), Math.max(y, y + sy * 6.5)); g.fill(p);
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

  function drawBook(g, t, mood, small) {
    const cover = coverPath();
    // contorno macio: claro por fora (fundo escuro) e tinta por dentro (fundo claro)
    stroke(g, cover, "rgba(255,255,255,0.28)", small ? 3 : 2);
    stroke(g, cover, "rgba(14,10,69,0.85)", small ? 1.6 : 1);
    g.save(); g.translate(0, 1.7); g.fillStyle = "#1A1370"; g.fill(cover); g.restore();
    g.fillStyle = vGrad(g, ["#5247E0", "#3A2FC4", "#2A209E"], 41, 93); g.fill(cover);
    const lg = g.createLinearGradient(8, 44, 40, 70); lg.addColorStop(0, "rgba(255,255,255,0.14)"); lg.addColorStop(1, "rgba(255,255,255,0)");
    g.fillStyle = lg; g.fill(cover);
    if (!small) {
      g.save(); g.clip(cover);
      for (let x = -50; x < 100; x += 1.5) { line(g, "rgba(255,255,255,0.045)", x, 40, x + 54, 95, 0.3, false); line(g, "rgba(0,0,0,0.06)", x + 54, 40, x, 95, 0.3, false); }
      g.restore();
    }
    for (const side of [-1, 1]) {
      const ed = new Path2D(), x0 = 50 + side * 41.5;
      ed.moveTo(x0, 43.5); ed.lineTo(x0 + side * 1.6, 44.4); ed.lineTo(x0 + side * 1.6, 88.2); ed.lineTo(x0, 89); ed.closePath();
      g.fillStyle = vGrad(g, ["#2A209E", "#1A1370"], 44, 89); g.fill(ed);
    }
    if (!small) drawCorners(g);
    drawRibbon(g, t, mood);

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

  function drawCap(g, t, tilt, vy) {
    const base = new Path2D();
    base.moveTo(38, 32); base.lineTo(62, 32); base.lineTo(60.5, 42.5); base.quadraticCurveTo(50, 46.5, 39.5, 42.5); base.closePath();
    g.fillStyle = vGrad(g, [CAP, "#15104A"], 32, 46); g.fill(base);
    const edge = new Path2D();
    edge.moveTo(23, 29.5); edge.lineTo(50, 38); edge.lineTo(77, 29.5); edge.lineTo(77, 31.3); edge.lineTo(50, 39.8); edge.lineTo(23, 31.3); edge.closePath();
    g.fillStyle = "#120D3F"; g.fill(edge);
    const board = new Path2D();
    board.moveTo(50, 21); board.lineTo(77, 29.5); board.lineTo(50, 38); board.lineTo(23, 29.5); board.closePath();
    const lg = g.createLinearGradient(30, 22, 70, 38); lg.addColorStop(0, CAP_TOP); lg.addColorStop(1, CAP);
    g.fillStyle = lg; g.fill(board);
    stroke(g, board, "rgba(255,255,255,0.22)", 0.5);
    line(g, "rgba(255,255,255,0.18)", 33, 27.5, 50, 22.5, 1.2);
    // borla: balança com o corpo e reage aos pulos (atrasada)
    const swing = sin(t * 2.6) * 4 - tilt * 0.8 + clamp(vy * 0.08, -5, 5);
    const bx = 50, by = 29.5, cx = 74, cy = 30.5;
    line(g, GOLD, bx, by, cx, cy, 1.3);
    const ex = cx + swing * 0.5, ey = cy + 12 - Math.max(0, -vy) * 0.03;
    const cord = new Path2D(); cord.moveTo(cx, cy); cord.quadraticCurveTo(cx + swing * 0.2, cy + 6, ex, ey);
    stroke(g, cord, GOLD, 1.3);
    const tassel = new Path2D();
    tassel.moveTo(ex - 1.4, ey); tassel.lineTo(ex + 1.4, ey);
    tassel.lineTo(ex + 2.4 + swing * 0.15, ey + 6.5); tassel.lineTo(ex - 2.4 + swing * 0.15, ey + 6.5); tassel.closePath();
    g.fillStyle = vGrad(g, [GOLD, "#D89422"], ey, ey + 6.5); g.fill(tassel);
    circle(g, GOLD, 1.8, bx, by); circle(g, "#FFE3A0", 0.7, bx - 0.5, by - 0.5);
  }

  function drawRibbon(g, t, mood) {
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
    g.fillStyle = hGrad(g, [MINT_DARK, MINT, "#C6F7E2", MINT], x0, x0 + w + 0.6 + sway); g.fill(r);
    g.fillStyle = vGrad(g, ["rgba(14,10,69,0.45)", "rgba(14,10,69,0)"], 88, 91.5); g.fillRect(x0, 88, w + 0.3, 3.5);
    line(g, "rgba(255,255,255,0.55)", x0 + 0.5, edge - 0.4, x0 + w, edge - 0.4, 0.5, false);
    g.fillStyle = vGrad(g, ["rgba(31,122,90,0.55)", "rgba(31,122,90,0)"], edge, edge + 1.8); g.fillRect(x0 + 0.3, edge, w, 1.8);
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
    if (which.includes("L")) drawArm(g, [12, 66], [P.hL0, P.hL1], P.bendL, -1, P.shapeL, P.rotL, s.popL, s.small);
    if (which.includes("R")) drawArm(g, [88, 66], [P.hR0, P.hR1], P.bendR, 1, P.shapeR, P.rotR, s.popR, s.small);
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
  function drawArm(g, sh, h, bend, side, shape, rot, pop, small) {
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
    g.fillStyle = ARM; g.fill(p);
    const edgeSide = side < 0 ? R1 : L1, hl = new Path2D(); hl.moveTo(...edgeSide[1]); for (let i = 2; i < N - 1; i++) hl.lineTo(...edgeSide[i]);
    stroke(g, hl, rgba(ARM_L, 0.7), 0.6);
    // mão: na direção do fim do braço, mais o giro do humor; "pop" quando troca de forma
    if (shape === "pen") drawPencil(g, h[0] - 2.4, h[1] + 3.2, small);
    const ang = Math.atan2(h[1] - cy, h[0] - cx) + rot * PI / 180;
    g.save(); g.translate(h[0], h[1]); g.rotate(ang); if (side < 0) g.scale(1, -1);
    const sc = (small ? 1.12 : 1) * (1 + 0.28 * pop); g.scale(sc, sc);
    drawHand(g, shape, small);
    g.restore();
  }
  function drawHand(g, shape, small) {
    const OUT = ARM, ow = small ? 1.0 : 0.6, fill = "#FFFFFF";
    // punho da luva e pulseira dourada
    g.beginPath(); g.ellipse(-1.2, 0, 1.6, 2.5, 0, 0, 2 * PI); g.fillStyle = "#ECE8F6"; g.fill(); g.strokeStyle = OUT; g.lineWidth = ow; g.stroke();
    line(g, GOLD, -1.2, -2.2, -1.2, 2.2, 0.7, false);
    const outline = (path) => { g.fillStyle = fill; g.fill(path); g.strokeStyle = OUT; g.lineWidth = ow; g.lineJoin = "round"; g.stroke(path); };
    const finger = (x, y, a, len, r) => { const p = new Path2D(); p.moveTo(x, y); p.lineTo(x + cos(a) * len, y + sin(a) * len); g.lineCap = "round"; g.strokeStyle = OUT; g.lineWidth = r * 2 + ow * 2; g.stroke(p); g.strokeStyle = fill; g.lineWidth = r * 2; g.stroke(p); };
    if (shape === "open") {
      [-0.95, -0.35, 0.25, 0.85].forEach((a, i) => finger(2.2, 0, a * 0.8, 2.6 + (i === 1 || i === 2 ? 0.5 : 0), 0.95));
      finger(0.9, -1.8, -1.9, 1.9, 0.95);
      const palm = new Path2D(); palm.ellipse(1.6, 0, 2.6, 2.5, 0, 0, 2 * PI); outline(palm);
      if (!small) line(g, "rgba(51,38,206,0.25)", 2.6, -0.8, 3.1, 0.8, 0.35);
    } else if (shape === "fist" || shape === "pen") {
      const th = new Path2D(); th.ellipse(1.2, -2.3, 1.5, 1.0, -0.4, 0, 2 * PI); outline(th);
      const f = new Path2D(); f.ellipse(1.7, 0, 3.1, 2.9, 0, 0, 2 * PI); outline(f);
      if (!small) { const k = new Path2D(); k.moveTo(3.4, -1.1); k.quadraticCurveTo(4.4, 0, 3.4, 1.1); stroke(g, k, "rgba(51,38,206,0.3)", 0.4); }
    } else if (shape === "point") {
      finger(3.2, -0.6, 0, 4.2, 0.95);
      const th = new Path2D(); th.ellipse(1.4, -1.9, 1.4, 0.9, -0.3, 0, 2 * PI); outline(th);
      const f = new Path2D(); f.ellipse(1.6, 0.4, 2.8, 2.5, 0, 0, 2 * PI); outline(f);
    } else { // luva relaxada (a bolinha de sempre, com polegar)
      const th = new Path2D(); th.ellipse(1.0, -2.6, 1.2, 1.6, -0.6, 0, 2 * PI); outline(th);
      const palm = new Path2D(); palm.ellipse(1.7, 0, 3.1, 3.0, 0, 0, 2 * PI); outline(palm);
      if (!small) { const fl = new Path2D(); fl.moveTo(3.4, -1.2); fl.quadraticCurveTo(4.6, 0, 3.6, 1.4); stroke(g, fl, "rgba(51,38,206,0.3)", 0.4); }
    }
  }

  // ------------------------------------------------------------------ animação
  const ease = (x) => 1 - (1 - x) * (1 - x);
  const live = new Set();
  let rafId = 0;
  function tick(now) { rafId = 0; for (const el of live) el._frame(now); if (live.size) rafId = requestAnimationFrame(tick); }
  function wake() { if (!rafId && live.size) rafId = requestAnimationFrame(tick); }

  const MOODS = { idle: "idle", thinking: "thinking", talking: "talking", happy: "happy", sad: "sad", wave: "wave", point: "point", wink: "idle",
    surprised: "surprised", proud: "proud", focus: "focus", sleepy: "sleepy", dizzy: "dizzy" };

  // Onde está o ponteiro e quando a pessoa mexeu pela última vez (para os olhos seguirem e para ele cochilar).
  const POINTER = { x: 0, y: 0, at: -1e9 };
  let lastActivity = performance.now();
  const touch = (e) => { lastActivity = performance.now(); if (e && e.clientX != null) { POINTER.x = e.clientX; POINTER.y = e.clientY; POINTER.at = lastActivity; } };
  ["pointermove", "pointerdown"].forEach((ev) => window.addEventListener(ev, touch, { passive: true }));
  ["keydown", "scroll", "wheel", "touchstart"].forEach((ev) => window.addEventListener(ev, () => { lastActivity = performance.now(); }, { passive: true }));
  const DOZE_AFTER = 40000;            // parado 40 s sem ninguém mexer: cochila

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
      live.add(this); wake();
    }

    _size() {
      if (!this._canvas) return;
      const size = parseInt(this.getAttribute("size") || "96", 10);
      this._small = size < SMALL;
      this.style.width = size + "px"; this.style.height = size + "px";
      // O desenho passa da grade 100 x 100 (mãos para o alto, aceno, borla): o canvas sobra MARGIN% para cada
      // lado, fora da caixa do elemento, sem pegar cliques.
      if (getComputedStyle(this).position === "static") this.style.position = "relative";
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
    _advance(mood, t, dt, fid) {
      const T = pose(mood, t, this._look, fid);
      const flat = { ...T, hL0: T.hL[0], hL1: T.hL[1], hR0: T.hR[0], hR1: T.hR[1] };
      delete flat.hL; delete flat.hR; delete flat.shapeL; delete flat.shapeR;
      if (!this._st) { this._st = { x: { ...flat }, v: {} }; this._mood = mood; this._shape = { L: T.shapeL, R: T.shapeR }; }
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
      const t = still ? 0.35 : (now - this._start) / 1000;
      const dt = still || !this._last ? 0 : Math.min(0.05, (now - this._last) / 1000);
      this._last = now;
      if (!still) { this._updateBlink(now); this._updateLook(now, mood); }
      else { this._look.x = 0.3; this._look.y = -0.2; }
      if (this._wink && now > this._winkUntil) this._wink = false;
      this._advance(mood, t, dt, still ? null : this._fidget(now, mood));
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
        speech,
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
    const dpr = Math.min(window.devicePixelRatio || 1, 2.5), key = mood + ":" + size + ":" + dpr;
    if (STATIC.has(key)) return STATIC.get(key);
    const T = pose(mood === "thinking" ? "idle" : mood, 0.35, { x: 0.3, y: -0.2 });
    const P = { ...T, hL0: T.hL[0], hL1: T.hL[1], hR0: T.hR[0], hR1: T.hR[1] };
    const c = document.createElement("canvas"); c.width = c.height = Math.round(size * dpr);
    const g = c.getContext("2d"), k = c.width / 100, fit = 0.9;      // cabe a mão para o alto dentro da caixa
    g.setTransform(k * fit, 0, 0, k * fit, c.width * (1 - fit) / 2, c.width * (1 - fit) / 2 + k * 2);
    drawFolha(g, { t: 0.35, mood: mood === "thinking" ? "idle" : mood, small: size < SMALL, P, vy: 0, blink: 0, wink: mood === "wink" ? 1 : 0, popL: 0, popR: 0, squash: 0, gulp: 0 });
    const url = c.toDataURL("image/png"); STATIC.set(key, url); return url;
  };
  if (reduceMotion.addEventListener) reduceMotion.addEventListener("change", () => {
    document.querySelectorAll("estudario-folha").forEach((el) => { if (el.isConnected) live.add(el); });
    wake();
  });
})();
