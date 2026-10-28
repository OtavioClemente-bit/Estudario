// A fênix da sequência, pintada com fogo em tempo real (site e app, nas cenas de jogo).
// <estudario-fenix state="sleep|awake" wake-at="ms">
//   sleep  : cinza de cinzas, asas recolhidas, cabeça baixa, uma brasa no peito soltando fumaça
//   awake  : penas de fogo (três camadas somadas: brilho, chama e núcleo), asas batendo, brasas subindo
//   wake-at: acorda sozinha depois de tantos milissegundos (explode em fogo e abre as asas)
// O desenho é feito num espaço de 1000 x 1000 e escalado para o tamanho do elemento; o canvas passa da
// caixa (sobra de 35% dos lados, 45% em cima, para o fogo subir) para o brilho não cortar.
(function () {
  "use strict";
  if (!window.customElements || customElements.get("estudario-fenix")) return;
  const PI = Math.PI, sin = Math.sin, cos = Math.cos;
  const reduced = window.matchMedia ? window.matchMedia("(prefers-reduced-motion: reduce)") : { matches: false };

  // ------------------------------------------------------------------ o esqueleto (pose acordada)
  // Cada pena é uma curva cúbica [x0,y0, x1,y1, x2,y2, x3,y3] com largura na raiz e uma semente.
  const SHOULDER_L = [468, 432], SHOULDER_R = [588, 422];
  const D = PI / 180;
  // uma pena que sai de [base] na direção [ang] (graus; 0 = direita, 90 = baixo), com a ponta curvando
  function plume(base, ang, len, w, seed, curl, extra) {
    const a = ang * D, dx = cos(a), dy = sin(a), nx = -dy, ny = dx;
    const p1 = [base[0] + dx * len * .33 + nx * curl * .4, base[1] + dy * len * .33 + ny * curl * .4];
    const p2 = [base[0] + dx * len * .7 + nx * curl, base[1] + dy * len * .7 + ny * curl];
    const p3 = [base[0] + dx * len + nx * curl * .3, base[1] + dy * len + ny * curl * .3];
    return Object.assign({ c: [base[0], base[1], p1[0], p1[1], p2[0], p2[1], p3[0], p3[1]], w, seed }, extra || {});
  }
  const lerp2 = (a, b, k) => [a[0] + (b[0] - a[0]) * k, a[1] + (b[1] - a[1]) * k];
  // a asa erguida: bordo grosso do ombro ao pulso e à mão; 9 penas longas em leque saindo da mão e
  // 8 secundárias caindo do braço. [side] -1 = esquerda, 1 = direita (espelho dos ângulos).
  function wing(shoulder, wrist, hand, side) {
    const out = [], mir = (ang) => side < 0 ? ang : 180 - ang, sd = side > 0 ? 9 : 0;
    // secundárias: do braço, caindo para fora (a parte de trás da asa)
    for (let i = 0; i < 10; i++) {
      const k = i / 9, base = lerp2(shoulder, wrist, k);
      out.push(plume(base, mir(146 + 36 * k), 250 - 50 * k, 42, 20 + i * 2.3 + sd, 30 * side, { wing: side, k: .2 + .3 * k }));
    }
    // primárias: da mão, em leque até lá em cima (as mais longas no meio)
    for (let i = 0; i < 12; i++) {
      const k = i / 11, base = lerp2(wrist, hand, k * .85);
      out.push(plume(base, mir(194 + 70 * k), 270 + 150 * sin(k * PI * .88), 38 - 10 * k, i * 1.7 + sd, -38 * side, { wing: side, k: .5 + .5 * k, tip: i % 2 === 0 }));
    }
    // coberteiras: penas curtas por cima, perto do braço, dando volume
    for (let i = 0; i < 8; i++) {
      const k = i / 7, base = lerp2(lerp2(shoulder, wrist, .1), lerp2(wrist, hand, .35), k);
      out.push(plume(base, mir(160 + 50 * k), 130 - 20 * k, 34, 90 + i * 3.1 + sd, 18 * side, { wing: side, k: .3 + .4 * k, covert: true }));
    }
    const m1 = lerp2(shoulder, wrist, .5), m2 = lerp2(wrist, hand, .5);
    out.push({ c: [shoulder[0], shoulder[1], m1[0], m1[1] - 40, wrist[0], wrist[1], m2[0], m2[1]], w: 58, seed: 40 + sd, wing: side, bright: true, k: .3 });
    return out;
  }
  const WING_L = wing(SHOULDER_L, [310, 250], [170, 70], -1);
  const WING_R = wing(SHOULDER_R, [745, 240], [880, 60], 1);
  // a cauda: sete plumas largas em S abrindo para baixo, e fitas finas de fogo
  const TAIL_BASE = [512, 648];
  const TAIL = [];
  for (let i = 0; i < 9; i++) {
    const k = i / 8, ang = 128 - 76 * k, base = [TAIL_BASE[0] - 40 + 80 * k, TAIL_BASE[1] - 20 + 30 * Math.abs(k - .5)];
    TAIL.push(plume(base, ang, 430 + 130 * sin(k * PI), 80 - 20 * Math.abs(k - .5), 51 + i, (i % 2 ? 1 : -1) * (120 + 40 * Math.abs(k - .5)), { tail: k }));
  }
  TAIL.push(plume([500, 600], 200, 300, 14, 61, -60, { thin: true }));
  TAIL.push(plume([560, 600], -20, 300, 14, 62, 60, { thin: true }));
  TAIL.push(plume([520, 660], 120, 420, 16, 63, 80, { thin: true }));
  TAIL.push(plume([530, 660], 60, 420, 16, 64, -80, { thin: true }));
  const BODY = [
    { c: [505, 680, 460, 600, 490, 470, 565, 390], w: 124, seed: 71, bright: true },
    { c: [522, 640, 545, 545, 565, 450, 595, 355], w: 84, seed: 72, bright: true },
    { c: [578, 395, 595, 340, 612, 300, 628, 280], w: 56, seed: 73, bright: true }, // pescoço
  ];
  const CREST = [
    { c: [612, 258, 580, 210, 540, 186, 486, 120], w: 30, seed: 81, tip: true },
    { c: [620, 250, 604, 190, 592, 136, 552, 70], w: 28, seed: 82, tip: true },
    { c: [630, 248, 644, 188, 660, 146, 646, 80], w: 24, seed: 83, tip: true },
    { c: [636, 254, 664, 214, 690, 196, 720, 150], w: 18, seed: 84, tip: true },
  ];
  const HEAD = [632, 272];

  // ------------------------------------------------------------------ geometria
  const bez = (c, t) => {
    const u = 1 - t;
    return [u * u * u * c[0] + 3 * u * u * t * c[2] + 3 * u * t * t * c[4] + t * t * t * c[6], u * u * u * c[1] + 3 * u * u * t * c[3] + 3 * u * t * t * c[5] + t * t * t * c[7]];
  };
  function rotatePts(c, pivot, ang, sc) {
    const out = c.slice(), ca = cos(ang), sa = sin(ang);
    for (let i = 0; i < 8; i += 2) {
      const dx = (c[i] - pivot[0]) * sc, dy = (c[i + 1] - pivot[1]) * sc;
      out[i] = pivot[0] + dx * ca - dy * sa; out[i + 1] = pivot[1] + dx * sa + dy * ca;
    }
    return out;
  }
  // a pena como faixa que afina, com a borda em labaredas que se mexem
  function strip(c, w, seed, time, flick, widthK) {
    const N = 18, L = [], R = [];
    for (let i = 0; i <= N; i++) {
      const t = i / N, p = bez(c, t), q = bez(c, Math.min(1, t + .02)), p0 = bez(c, Math.max(0, t - .02));
      let dx = q[0] - p0[0], dy = q[1] - p0[1]; const len = Math.hypot(dx, dy) || 1; dx /= len; dy /= len;
      const nx = -dy, ny = dx;
      const wave = flick * sin(time * 5.2 + seed * 3 + t * 9) * 10 * t;
      const rag = 1 + flick * .28 * sin(time * 9 + seed * 7 + t * 31) * t;
      const half = w * widthK * (1 - t * .92) * (t < .12 ? .55 + t * 3.75 : 1) * rag;
      const cx = p[0] + nx * wave, cy = p[1] + ny * wave;
      L.push([cx + nx * half, cy + ny * half]); R.push([cx - nx * half, cy - ny * half]);
    }
    const path = new Path2D(); path.moveTo(L[0][0], L[0][1]);
    for (let i = 1; i < L.length; i++) path.lineTo(L[i][0], L[i][1]);
    for (let i = R.length - 1; i >= 0; i--) path.lineTo(R[i][0], R[i][1]);
    path.closePath();
    return path;
  }

  // as barbas de uma pena: fios que saem do eixo para os dois lados, inclinados para a raiz, tremulando
  function barbs(c, w, seed, time) {
    const path = new Path2D(), N = 13;
    for (let i = 1; i <= N; i++) {
      const jit = sin(seed * 13.7 + i * 7.31) * .5 + .5, t = (i + jit * .6 - .3) / (N + 1), p = bez(c, t), q = bez(c, Math.min(1, t + .02)), p0 = bez(c, Math.max(0, t - .02));
      let dx = q[0] - p0[0], dy = q[1] - p0[1]; const len = Math.hypot(dx, dy) || 1; dx /= len; dy /= len;
      const nx = -dy, ny = dx, wave = sin(time * 5.2 + seed * 3 + t * 9) * 10 * t;
      const cx = p[0] + nx * wave, cy = p[1] + ny * wave, half = w * (1 - t * .92) * 1.12;
      for (const sd of [-1, 1]) {
        const wig = sin(time * 6 + seed + i * 1.3 + sd) * 3;
        const sx = cx + nx * sd * half * .15, sy = cy + ny * sd * half * .15;
        const reach = half * (.75 + .55 * (sin(seed * 3.1 + i * 2.7 + sd) * .5 + .5)) + wig, back = half * (.55 + .4 * jit);
        const ex = cx + nx * sd * reach - dx * back, ey = cy + ny * sd * reach - dy * back;
        path.moveTo(sx, sy); path.quadraticCurveTo(cx + nx * sd * half * .75, cy + ny * sd * half * .75, ex, ey);
      }
    }
    return path;
  }

  // ------------------------------------------------------------------ o elemento
  class Fenix extends HTMLElement {
    static get observedAttributes() { return ["state"]; }
    constructor() {
      super();
      this.attachShadow({ mode: "open" });
      this.shadowRoot.innerHTML = '<style>:host{display:block;position:relative;pointer-events:none}canvas{position:absolute;left:-35%;top:-45%;width:170%;height:170%}</style><canvas></canvas>';
      this._c = this.shadowRoot.querySelector("canvas"); this._g = this._c.getContext("2d");
      this._glow = document.createElement("canvas"); this._gg = this._glow.getContext("2d");
      this._fire = document.createElement("canvas"); this._fg = this._fire.getContext("2d"); this._flames = [];
      this._soft = document.createElement("canvas"); this._sg = this._soft.getContext("2d");
      this._wake = 0; this._wakeStart = -1; this._embers = []; this._smoke = []; this._t0 = 0; this._raf = 0;
    }
    connectedCallback() {
      this._t0 = performance.now();
      this._wake = this.getAttribute("state") === "awake" ? 1 : 0;
      const at = Number(this.getAttribute("wake-at"));
      if (this._wake === 0 && Number.isFinite(at) && at > 0) this._timer = setTimeout(() => this.ignite(), at);
      if ("ResizeObserver" in window) { this._ro = new ResizeObserver(() => this._size()); this._ro.observe(this); }
      this._size(); this._loop();
    }
    disconnectedCallback() { cancelAnimationFrame(this._raf); clearTimeout(this._timer); if (this._ro) this._ro.disconnect(); }
    attributeChangedCallback(n, a, b) { if (n === "state" && b === "awake" && this.isConnected && this._wake < 1 && this._wakeStart < 0) this.ignite(); }
    /** Acorda: explode em fogo e abre as asas. */
    ignite() {
      if (this._wakeStart >= 0 || this._wake >= 1) return;
      this._wakeStart = performance.now();
      for (let i = 0; i < 70; i++) {
        const a = Math.random() * PI * 2, s = 4 + Math.random() * 14;
        this._embers.push({ x: 540, y: 460, vx: cos(a) * s, vy: sin(a) * s - 3, life: 1, r: 3 + Math.random() * 7 });
      }
      for (let i = 0; i < 180; i++) {
        const a = -PI / 2 + (Math.random() - .5) * 2.6, sp = 2 + Math.random() * 9;
        this._flames.push({ x: 520 + (Math.random() - .5) * 160, y: 520 + (Math.random() - .5) * 160, vx: cos(a) * sp, vy: sin(a) * sp, life: 1, decay: .012 + Math.random() * .012, s: 18 + Math.random() * 34, seed: Math.random() * 10 });
      }
    }
    /** Labaredas novas ao longo das penas e o movimento das que já existem. */
    _burn(feathers, time, e) {
      const spawn = reduced.matches ? 0 : Math.round(5 * e);
      for (let i = 0; i < spawn; i++) {
        const pool = this._tips || (this._tips = feathers.map((f, i) => (f.tip || (f.wing && (f.k || 0) > .55 && !f.covert)) ? i : -1).filter(i => i >= 0));
        const f = feathers[pool[(Math.random() * pool.length) | 0]], t = .78 + Math.random() * .22, p = bez(f.c, t);
        const sz = 12 + Math.random() * 18;
        this._flames.push({ x: p[0] + (Math.random() - .5) * f.w * .7, y: p[1], vx: (Math.random() - .5) * .8, vy: -(1.4 + Math.random() * 2.6), life: 1, decay: .02 + Math.random() * .02, s: sz, seed: Math.random() * 10 });
      }
      if (this._flames.length > 300) this._flames.splice(0, this._flames.length - 300);
      this._flames = this._flames.filter(q => (q.life -= q.decay) > 0);
      for (const q of this._flames) { q.x += q.vx + sin(time * 4 + q.seed + q.y * .02) * .7; q.y += q.vy; q.vy -= .035; q.vx *= .98; }
    }
    /** As labaredas no canvas do fogo: cada uma é uma língua que afina para cima, tremendo. */
    _paintFire(time, view) {
      const fg = this._fg; fg.setTransform(1, 0, 0, 1, 0, 0); fg.clearRect(0, 0, this._fire.width, this._fire.height);
      view(fg, .4); fg.globalCompositeOperation = "lighter";
      const bands = [new Path2D(), new Path2D(), new Path2D(), new Path2D()];
      for (const q of this._flames) {
        const L = q.life, sz = q.s * (.55 + .45 * L), lean = sin(time * 6 + q.seed) * sz * .35;
        const p = bands[L > .75 ? 0 : L > .5 ? 1 : L > .25 ? 2 : 3];
        p.moveTo(q.x + lean, q.y - sz * 2.3);
        p.quadraticCurveTo(q.x + sz * 1.05, q.y - sz * .3, q.x, q.y + sz * .7);
        p.quadraticCurveTo(q.x - sz * 1.05, q.y - sz * .3, q.x + lean, q.y - sz * 2.3);
        p.closePath();
      }
      const colors = ["rgba(255,236,170,.6)", "rgba(255,170,60,.45)", "rgba(255,95,30,.32)", "rgba(200,40,20,.16)"];
      bands.forEach((p, i) => { fg.fillStyle = colors[i]; fg.fill(p); });
    }
    _size() {
      const r = this.getBoundingClientRect(), side = Math.max(r.width, r.height) * 1.7 || 1;
      // no máximo ~1000 px de lado: no celular (tela 3x) o canvas passaria de 2500 px e cada quadro pesaria
      const dpr = Math.min(window.devicePixelRatio || 1, 1.5, 1000 / side);
      const w = Math.max(1, Math.round(r.width * 1.7 * dpr)), h = Math.max(1, Math.round(r.height * 1.7 * dpr));
      if (this._c.width !== w || this._c.height !== h) { this._c.width = w; this._c.height = h; this._glow.width = Math.ceil(w / 4); this._glow.height = Math.ceil(h / 4); this._fire.width = Math.ceil(w * .4); this._fire.height = Math.ceil(h * .4); this._soft.width = this._glow.width; this._soft.height = this._glow.height; }
    }
    _loop() {
      let odd = false;
      const step = () => { odd = !odd; if (odd) this._frame(); if (!reduced.matches) this._raf = requestAnimationFrame(step); };
      this._raf = requestAnimationFrame(step);
    }
    _frame() {
      const now = performance.now(), time = (now - this._t0) / 1000;
      if (this._wakeStart >= 0) { this._wake = Math.min(1, (now - this._wakeStart) / 900); if (this._wake >= 1) this._wakeStart = -1; }
      const wake = reduced.matches ? (this._wake > 0 ? 1 : 0) : this._wake;
      const g = this._g, W = this._c.width, H = this._c.height;
      g.setTransform(1, 0, 0, 1, 0, 0); g.clearRect(0, 0, W, H);
      // o desenho (1000 x 1000) ocupa a caixa do elemento, que fica no meio do canvas
      const s = Math.min(W, H) / 1.7 / 1000, ox = (W - 1000 * s) / 2, oy = (H - 1000 * s) * (45 / 70);
      const view = (ctx, k) => ctx.setTransform(s * k, 0, 0, s * k, ox * k, oy * k);
      const e = wake < 1 ? 1 - Math.pow(1 - wake, 3) : 1;
      const fold = 1 - e;
      const flap = sin(time * 2.4) * .09 * e, bob = sin(time * 2.4) * 10 * e - fold * 30;
      const breathe = sin(time * 1.3);
      // as asas: abertas em V, batendo; dormindo, caídas e recolhidas
      // cada pena bate um pouco depois da anterior (do ombro para a ponta), como asa de verdade
      const lag = (k) => sin(time * 2.4 - (k || 0) * 1.3) * (.07 + .07 * (k || 0)) * e;
      const wingL = WING_L.map(f => ({ ...f, c: rotatePts(f.c, SHOULDER_L, -1.55 * fold - lag(f.k), 1 - .38 * fold) }));
      const wingR = WING_R.map(f => ({ ...f, c: rotatePts(f.c, SHOULDER_R, 1.55 * fold + lag(f.k), 1 - .38 * fold) }));
      const tail = TAIL.map((f, i) => ({ ...f, c: rotatePts(f.c, TAIL_BASE, sin(time * 1.6 + i * .7) * .05 * e, 1) }));
      // a cabeça baixa quando dorme
      const neckPivot = [575, 400], headDrop = .55 * fold;
      const body = BODY.map((f, i) => i === 2 ? { ...f, c: rotatePts(f.c, neckPivot, headDrop, 1) } : f);
      const crest = CREST.map(f => ({ ...f, c: rotatePts(f.c, neckPivot, headDrop, 1) }));
      const head = rotatePts([HEAD[0], HEAD[1], 0, 0, 0, 0, 0, 0], neckPivot, headDrop, 1);
      const all = [...tail, ...wingL, ...wingR, ...body, ...crest];
      const lift = (f) => ({ ...f, c: f.c.map((v, i) => i % 2 ? v + bob : v) });
      const feathers = all.map(lift);
      const hx = head[0], hy = head[1] + bob;

      // ---- dormindo: cinza de cinzas (some ao acordar)
      if (wake < 1) {
        g.save(); view(g, 1); g.globalAlpha = 1 - e;
        // a brasa do peito, respirando
        const ember = g.createRadialGradient(530, 480 + bob, 0, 530, 480 + bob, 160);
        ember.addColorStop(0, `rgba(255,120,40,${.35 + .15 * breathe})`); ember.addColorStop(1, "rgba(255,120,40,0)");
        g.fillStyle = ember; g.fillRect(300, 250, 460, 460);
        for (const f of feathers) {
          const p = strip(f.c, f.w, f.seed, time, .15, 1);
          const a = bez(f.c, 0), b = bez(f.c, 1), lg = g.createLinearGradient(a[0], a[1], b[0], b[1]);
          lg.addColorStop(0, f.bright ? "#4A4258" : "#3A3346"); lg.addColorStop(.7, "#2A2434"); lg.addColorStop(1, "rgba(40,34,52,0)");
          g.fillStyle = lg; g.fill(p);
        }
        // veias de brasa: o fogo ainda vivo por dentro das penas, pulsando com a respiração
        g.globalCompositeOperation = "lighter";
        for (const f of feathers) {
          const a = bez(f.c, 0), b = bez(f.c, 1), vg = g.createLinearGradient(a[0], a[1], b[0], b[1]);
          vg.addColorStop(0, `rgba(255,110,40,${.45 + .2 * breathe})`); vg.addColorStop(.6, "rgba(255,80,30,.12)"); vg.addColorStop(1, "rgba(255,80,30,0)");
          g.fillStyle = vg; g.fill(strip(f.c, f.w, f.seed, time, .1, .12));
        }
        g.globalCompositeOperation = "source-over";
        // cabeça e olho fechado
        g.fillStyle = "#4A4258"; g.beginPath(); g.ellipse(hx, hy, 40, 32, -.4 + headDrop, 0, PI * 2); g.fill();
        g.strokeStyle = "rgba(255,140,60,.6)"; g.lineWidth = 4; g.beginPath(); g.arc(hx + 6, hy - 4, 10, .2, PI - .2); g.stroke();
        g.restore();
        // fumaça subindo da brasa
        if (Math.random() < .25) this._smoke.push({ x: 520 + Math.random() * 60, y: 430, vx: (Math.random() - .5) * .6, vy: -1 - Math.random(), life: 1, r: 12 + Math.random() * 14 });
      }

      // ---- acordada: fogo (brilho borrado + chama + núcleo, tudo somando luz)
      if (e > 0) {
        // brilho de fundo: as penas largas num canvas pela metade, borrado e somado (só ele estoura a luz)
        for (const f of feathers) f._p = strip(f.c, f.w, f.seed, time, 1, f.thin ? .9 : 1);
        const gg = this._gg; gg.setTransform(1, 0, 0, 1, 0, 0); gg.clearRect(0, 0, this._glow.width, this._glow.height);
        view(gg, .25); gg.globalCompositeOperation = "lighter"; 
        for (const f of feathers) { if (f.covert || f.thin) continue; gg.fillStyle = `rgba(255,${f.bright ? 80 : 45},15,${.4 * e})`; gg.fill(strip(f.c, f.w, f.seed, time, 1, 1.8)); }
        g.save(); g.setTransform(1, 0, 0, 1, 0, 0); g.globalCompositeOperation = "lighter";
        const sg = this._sg; sg.setTransform(1, 0, 0, 1, 0, 0); sg.clearRect(0, 0, this._soft.width, this._soft.height);
        sg.filter = "blur(4px)"; sg.drawImage(this._glow, 0, 0); sg.filter = "none";
        g.drawImage(this._soft, 0, 0, W, H); g.restore();
        g.save(); view(g, 1);
        // halo atrás do corpo, suave
        g.globalCompositeOperation = "lighter";
        const halo = g.createRadialGradient(540, 470 + bob, 0, 540, 470 + bob, 430);
        halo.addColorStop(0, `rgba(255,160,60,${.18 * e})`); halo.addColorStop(.55, `rgba(200,40,20,${.08 * e})`); halo.addColorStop(1, "rgba(200,40,20,0)");
        g.fillStyle = halo; g.fillRect(100, 40, 880, 900);
        // o fogo: nasce nas penas e sobe; atrás dela, borrado (o calor)
        this._burn(feathers, time, e); this._paintFire(time, view);
        g.save(); g.setTransform(1, 0, 0, 1, 0, 0); g.globalCompositeOperation = "lighter";
        g.drawImage(this._fire, 0, 0, W, H); g.restore();
        // a membrana: a área entre as penas da asa preenchida de fogo escuro (a asa fica cheia)
        g.globalCompositeOperation = "source-over";
        for (const [shoulder, wingF] of [[SHOULDER_L, feathers.filter(f => f.wing < 0 && !f.covert)], [SHOULDER_R, feathers.filter(f => f.wing > 0 && !f.covert)]]) {
          const tips = wingF.map(f => bez(f.c, .72));
          const cx = tips.reduce((a, p) => a + p[0], 0) / tips.length, cy = tips.reduce((a, p) => a + p[1], 0) / tips.length;
          tips.sort((p, q) => Math.atan2(p[1] - cy, p[0] - cx) - Math.atan2(q[1] - cy, q[0] - cx));
          const mem = new Path2D(); mem.moveTo(tips[0][0], tips[0][1]); for (const p of tips) mem.lineTo(p[0], p[1]); mem.closePath();
          const mg = g.createRadialGradient(shoulder[0], shoulder[1] + bob, 20, shoulder[0], shoulder[1] + bob, 520);
          mg.addColorStop(0, `rgba(255,120,40,${.55 * e})`); mg.addColorStop(.5, `rgba(190,30,25,${.45 * e})`); mg.addColorStop(1, `rgba(90,10,40,${.1 * e})`);
          g.fillStyle = mg; g.fill(mem);
        }
        // 1) a base de cada pena, pintada por cima (sem somar luz): vermelho profundo que vai a carmim e some
        g.globalCompositeOperation = "source-over";
        for (const f of feathers) {
          const a = bez(f.c, 0), b = bez(f.c, 1), lg = g.createLinearGradient(a[0], a[1], b[0], b[1]);
          if (f.bright || f.covert) { lg.addColorStop(0, `rgba(255,${f.covert ? 175 : 150},${f.covert ? 70 : 50},${e})`); lg.addColorStop(.5, `rgba(235,80,25,${e})`); lg.addColorStop(1, `rgba(170,25,20,${.6 * e})`); }
          else { lg.addColorStop(0, `rgba(250,110,35,${e})`); lg.addColorStop(.4, `rgba(205,40,25,${.95 * e})`); lg.addColorStop(.85, `rgba(120,12,30,${.55 * e})`); lg.addColorStop(1, "rgba(90,8,40,0)"); }
          g.fillStyle = lg; g.fill(f._p);
        }
        // a borda dourada: o contorno das penas acende, desenhando a forma da fênix
        g.globalCompositeOperation = "lighter"; g.lineWidth = 2.2; g.lineJoin = "round";
        for (const f of feathers) { if (f.thin) continue; g.strokeStyle = `rgba(255,${f.bright ? 215 : 175},${f.bright ? 120 : 80},${(f.covert ? .3 : .42) * e})`; g.stroke(f._p); }
        // 2) as barbas: fios finos saindo do eixo de cada pena, inclinados para trás (a textura de pincelada)
        g.globalCompositeOperation = "lighter";
        const allBarbs = new Path2D();
        for (const f of feathers) { if (!(f.covert || f.thin || f.bright)) allBarbs.addPath(barbs(f.c, f.w, f.seed, time)); }
        g.strokeStyle = `rgba(255,200,100,${.3 * e})`; g.lineWidth = 1.5; g.lineCap = "round"; g.stroke(allBarbs);
        // 3) o eixo de cada pena: um fio dourado que brilha mais perto da raiz
        for (const f of feathers) {
          const a = bez(f.c, 0), b = bez(f.c, 1), cg = g.createLinearGradient(a[0], a[1], b[0], b[1]);
          cg.addColorStop(0, "rgba(255,248,210,0)"); cg.addColorStop(.14, `rgba(255,248,210,${(f.bright ? .45 : .6) * e})`); cg.addColorStop(.5, `rgba(255,200,90,${.38 * e})`); cg.addColorStop(1, "rgba(255,140,40,0)");
          g.fillStyle = cg; g.fill(strip(f.c, f.w, f.seed + .5, time, 1, f.thin ? .35 : .14));
        }
        g.restore();
        // a cabeça: de ave de rapina, fina e aerodinâmica, afinando para trás até a crista; plumas de fogo
        // saindo da nuca, bico curto em gancho dourado, olho amendoado aceso sob a crista óssea (olhar firme)
        g.save(); view(g, 1); g.globalAlpha = e;
        g.translate(hx, hy); g.rotate(-.3);
        // plumas da nuca (fogo somando luz, atrás da cabeça)
        g.globalCompositeOperation = "lighter";
        for (let i = 0; i < 3; i++) {
          const sw = sin(time * 3 + i * 1.7) * 6, tipX = -118 - i * 18, tipY = -58 + i * 26 + sw;
          const pg = g.createLinearGradient(-30, -14, tipX, tipY);
          pg.addColorStop(0, `rgba(255,190,80,${.75 * e})`); pg.addColorStop(1, "rgba(255,70,20,0)");
          g.fillStyle = pg; g.beginPath();
          g.moveTo(-26, -24 + i * 12); g.quadraticCurveTo(-70, -40 + i * 18 + sw * .5, tipX, tipY);
          g.quadraticCurveTo(-70, -26 + i * 18 + sw * .5, -26, -10 + i * 12); g.closePath(); g.fill();
        }
        g.globalCompositeOperation = "source-over";
        // a cabeça
        const hg = g.createLinearGradient(-60, -30, 50, 20);
        hg.addColorStop(0, "#C8261A"); hg.addColorStop(.4, "#FF8A26"); hg.addColorStop(.8, "#FFD36A"); hg.addColorStop(1, "#FFF1C0");
        g.fillStyle = hg; g.beginPath();
        g.moveTo(44, 0);
        g.bezierCurveTo(38, -22, 16, -36, -14, -34);
        g.bezierCurveTo(-38, -32, -56, -36, -76, -46);
        g.bezierCurveTo(-60, -26, -48, -10, -36, 0);
        g.bezierCurveTo(-18, 14, 12, 20, 30, 14);
        g.bezierCurveTo(38, 10, 44, 6, 44, 0); g.closePath(); g.fill();
        // luz de borda no alto da cabeça
        g.globalCompositeOperation = "lighter"; g.strokeStyle = `rgba(255,220,140,${.55 * e})`; g.lineWidth = 2.2;
        g.beginPath(); g.moveTo(40, -6); g.bezierCurveTo(32, -24, 12, -36, -14, -34); g.bezierCurveTo(-38, -32, -56, -36, -74, -45); g.stroke();
        g.globalCompositeOperation = "source-over";
        // bico curto em gancho, dourado
        const bk = g.createLinearGradient(38, -10, 72, 22); bk.addColorStop(0, "#FFF0B8"); bk.addColorStop(.6, "#F2B13A"); bk.addColorStop(1, "#9A4A0E");
        g.fillStyle = bk; g.beginPath();
        g.moveTo(38, -8); g.bezierCurveTo(54, -10, 68, -2, 72, 12); g.bezierCurveTo(73, 18, 70, 24, 66, 26);
        g.bezierCurveTo(66, 18, 62, 13, 56, 11); g.bezierCurveTo(50, 12, 44, 12, 38, 8); g.closePath(); g.fill();
        g.strokeStyle = "rgba(90,30,6,.55)"; g.lineWidth = 1.6; g.beginPath(); g.moveTo(40, 4); g.quadraticCurveTo(52, 6, 60, 12); g.stroke();
        g.fillStyle = "rgba(120,14,20,.85)"; g.beginPath();
        g.moveTo(8, -8); g.bezierCurveTo(-10, -12, -30, -10, -46, -2); g.bezierCurveTo(-30, -2, -14, 2, 2, 8); g.bezierCurveTo(6, 2, 8, -2, 8, -8); g.closePath(); g.fill();
        // olho amendoado e inclinado, com a íris acesa
        g.fillStyle = "#2A0A05"; g.beginPath();
        g.moveTo(1, -6); g.quadraticCurveTo(16, -21, 35, -11); g.quadraticCurveTo(21, 2, 1, -6); g.closePath(); g.fill();
        g.fillStyle = "#FFC23A"; g.shadowColor = "#FFE7A0"; g.shadowBlur = 20;
        g.beginPath(); g.ellipse(19, -9, 6, 5.2, -.2, 0, PI * 2); g.fill();
        g.fillStyle = "#3A0A04"; g.shadowBlur = 0; g.beginPath(); g.ellipse(20, -9, 2.2, 3.6, 0, 0, PI * 2); g.fill();
        g.fillStyle = "#FFFFFF"; g.shadowColor = "#FFF3B0"; g.shadowBlur = 10; g.beginPath(); g.arc(21.5, -11.5, 1.8, 0, PI * 2); g.fill();
        g.shadowBlur = 0;
        // a crista óssea sobre o olho (a "sobrancelha" da águia), dourada
        g.fillStyle = "#FFD36A"; g.beginPath();
        g.moveTo(-4, -13); g.quadraticCurveTo(14, -28, 38, -15); g.quadraticCurveTo(16, -20, -4, -13); g.closePath(); g.fill();
        g.restore();

        // brasas saindo das pontas
        if (!reduced.matches && Math.random() < .9 * e) {
          const f = feathers[(Math.random() * feathers.length) | 0], p = bez(f.c, .7 + Math.random() * .3);
          this._embers.push({ x: p[0], y: p[1], vx: (Math.random() - .5) * 2, vy: -2 - Math.random() * 3, life: 1, r: 2 + Math.random() * 4 });
        }
      }

      // ---- partículas: brasas (somam luz) e fumaça
      g.save(); view(g, 1);
      g.globalCompositeOperation = "lighter";
      this._embers = this._embers.filter(p => (p.life -= .014) > 0);
      for (const p of this._embers) {
        p.x += p.vx; p.y += p.vy; p.vx *= .985; p.vy = p.vy * .985 - .04;
        g.fillStyle = `rgba(255,${150 + 100 * p.life | 0},60,${p.life})`; g.beginPath(); g.arc(p.x, p.y, p.r * p.life, 0, PI * 2); g.fill();
      }
      g.globalCompositeOperation = "source-over";
      this._smoke = this._smoke.filter(p => (p.life -= .008) > 0);
      for (const p of this._smoke) {
        p.x += p.vx + sin(time + p.y * .02) * .3; p.y += p.vy; p.r += .25;
        g.fillStyle = `rgba(150,145,170,${.18 * p.life * (1 - e)})`; g.beginPath(); g.arc(p.x, p.y, p.r, 0, PI * 2); g.fill();
      }
      // o clarão de quando acorda
      if (wake > 0 && wake < 1) {
        g.globalCompositeOperation = "lighter";
        const fl = g.createRadialGradient(540, 470, 0, 540, 470, 200 + 600 * wake);
        fl.addColorStop(0, `rgba(255,250,220,${.9 * (1 - wake)})`); fl.addColorStop(1, "rgba(255,120,40,0)");
        g.fillStyle = fl; g.fillRect(-200, -200, 1400, 1400);
      }
      g.restore();
    }
  }
  customElements.define("estudario-fenix", Fenix);
})();
