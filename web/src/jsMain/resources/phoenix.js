// A fênix da sequência, pintada com fogo em tempo real (site e app, nas cenas de jogo).
// <estudario-fenix state="sleep|awake" wake-at="ms">
//   sleep  : cinza de cinzas, asas recolhidas, cabeça baixa, uma brasa no peito soltando fumaça
//   awake  : penas de fogo (três camadas somadas: brilho, chama e núcleo), asas batendo, brasas subindo
//   wake-at: acorda sozinha depois de tantos milissegundos (explode em fogo e abre as asas)
// O desenho é feito num espaço de 1000 x 1000 e escalado para o tamanho do elemento; o canvas passa da
// caixa (sobra de 22%) para o brilho não cortar.
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
    const out = [], mir = (ang) => side < 0 ? ang : 180 - ang;
    for (let i = 0; i < 8; i++) {
      const k = i / 7, base = lerp2(shoulder, wrist, k);
      out.push(plume(base, mir(150 + 30 * k), 230 - 40 * k, 40, 20 + i * 2.3 + (side > 0 ? 9 : 0), 26 * side, { wing: side }));
    }
    for (let i = 0; i < 9; i++) {
      const k = i / 8, base = lerp2(wrist, hand, k * .8);
      out.push(plume(base, mir(196 + 66 * k), 250 + 120 * sin(k * PI * .9), 36 - 8 * k, i * 1.7 + (side > 0 ? 9 : 0), -34 * side, { wing: side }));
    }
    const m1 = lerp2(shoulder, wrist, .5), m2 = lerp2(wrist, hand, .5);
    out.push({ c: [shoulder[0], shoulder[1], m1[0], m1[1] - 40, wrist[0], wrist[1], m2[0], m2[1]], w: 54, seed: 40 + (side > 0 ? 9 : 0), wing: side, bright: true });
    return out;
  }
  const WING_L = wing(SHOULDER_L, [310, 250], [170, 70], -1);
  const WING_R = wing(SHOULDER_R, [745, 240], [880, 60], 1);
  // a cauda: sete plumas largas em S abrindo para baixo, e fitas finas de fogo
  const TAIL_BASE = [512, 648];
  const TAIL = [];
  for (let i = 0; i < 7; i++) {
    const k = i / 6, ang = 168 - 156 * k;
    TAIL.push(plume(TAIL_BASE, ang, 360 + 60 * sin(k * PI), 78 - 18 * Math.abs(k - .5), 51 + i, (i % 2 ? 1 : -1) * 70));
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
    { c: [612, 258, 584, 214, 552, 194, 506, 140], w: 26, seed: 81 },
    { c: [620, 250, 606, 196, 596, 150, 560, 96], w: 24, seed: 82 },
    { c: [630, 248, 642, 196, 654, 160, 642, 104], w: 20, seed: 83 },
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
    const N = 26, L = [], R = [];
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

  // ------------------------------------------------------------------ o elemento
  class Fenix extends HTMLElement {
    static get observedAttributes() { return ["state"]; }
    constructor() {
      super();
      this.attachShadow({ mode: "open" });
      this.shadowRoot.innerHTML = '<style>:host{display:block;position:relative;pointer-events:none}canvas{position:absolute;left:-22%;top:-22%;width:144%;height:144%}</style><canvas></canvas>';
      this._c = this.shadowRoot.querySelector("canvas"); this._g = this._c.getContext("2d");
      this._glow = document.createElement("canvas"); this._gg = this._glow.getContext("2d");
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
    }
    _size() {
      const r = this.getBoundingClientRect(), dpr = Math.min(window.devicePixelRatio || 1, 2);
      const w = Math.max(1, Math.round(r.width * 1.44 * dpr)), h = Math.max(1, Math.round(r.height * 1.44 * dpr));
      if (this._c.width !== w || this._c.height !== h) { this._c.width = w; this._c.height = h; this._glow.width = Math.ceil(w / 2); this._glow.height = Math.ceil(h / 2); }
    }
    _loop() {
      const step = () => { this._frame(); if (!reduced.matches) this._raf = requestAnimationFrame(step); };
      this._raf = requestAnimationFrame(step);
    }
    _frame() {
      const now = performance.now(), time = (now - this._t0) / 1000;
      if (this._wakeStart >= 0) { this._wake = Math.min(1, (now - this._wakeStart) / 900); if (this._wake >= 1) this._wakeStart = -1; }
      const wake = reduced.matches ? (this._wake > 0 ? 1 : 0) : this._wake;
      const g = this._g, W = this._c.width, H = this._c.height;
      g.setTransform(1, 0, 0, 1, 0, 0); g.clearRect(0, 0, W, H);
      // o desenho (1000 x 1000) ocupa a caixa do elemento, que fica no meio do canvas
      const s = Math.min(W, H) / 1.44 / 1000, ox = (W - 1000 * s) / 2, oy = (H - 1000 * s) / 2;
      const view = (ctx, k) => ctx.setTransform(s * k, 0, 0, s * k, ox * k, oy * k);
      const e = wake < 1 ? 1 - Math.pow(1 - wake, 3) : 1;
      const fold = 1 - e;
      const flap = sin(time * 2.4) * .09 * e, bob = sin(time * 2.4) * 10 * e - fold * 30;
      const breathe = sin(time * 1.3);
      // as asas: abertas em V, batendo; dormindo, caídas e recolhidas
      const wingL = WING_L.map(f => ({ ...f, c: rotatePts(f.c, SHOULDER_L, -1.55 * fold - flap, 1 - .38 * fold) }));
      const wingR = WING_R.map(f => ({ ...f, c: rotatePts(f.c, SHOULDER_R, 1.55 * fold + flap, 1 - .38 * fold) }));
      // a cabeça baixa quando dorme
      const neckPivot = [575, 400], headDrop = .55 * fold;
      const body = BODY.map((f, i) => i === 2 ? { ...f, c: rotatePts(f.c, neckPivot, headDrop, 1) } : f);
      const crest = CREST.map(f => ({ ...f, c: rotatePts(f.c, neckPivot, headDrop, 1) }));
      const head = rotatePts([HEAD[0], HEAD[1], 0, 0, 0, 0, 0, 0], neckPivot, headDrop, 1);
      const all = [...TAIL, ...wingL, ...wingR, ...body, ...crest];
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
        // brilho: as penas largas num canvas pela metade, borrado e somado
        const gg = this._gg; gg.setTransform(1, 0, 0, 1, 0, 0); gg.clearRect(0, 0, this._glow.width, this._glow.height);
        view(gg, .5); gg.globalCompositeOperation = "lighter";
        for (const f of feathers) { gg.fillStyle = `rgba(255,${f.bright ? 110 : 60},20,${.55 * e})`; gg.fill(strip(f.c, f.w, f.seed, time, 1, 1.9)); }
        g.save(); g.setTransform(1, 0, 0, 1, 0, 0); g.globalCompositeOperation = "lighter";
        g.filter = `blur(${Math.max(6, Math.round(14 * s * 1000 / 400))}px)`;
        g.drawImage(this._glow, 0, 0, W, H); g.filter = "none"; g.restore();
        // halo atrás do peito
        g.save(); view(g, 1); g.globalCompositeOperation = "lighter";
        const halo = g.createRadialGradient(540, 470 + bob, 0, 540, 470 + bob, 420);
        halo.addColorStop(0, `rgba(255,200,90,${.35 * e})`); halo.addColorStop(.5, `rgba(255,90,30,${.15 * e})`); halo.addColorStop(1, "rgba(255,60,20,0)");
        g.fillStyle = halo; g.fillRect(100, 50, 880, 880);
        // chama (vermelho → laranja) e núcleo (amarelo → branco), ao longo de cada pena
        for (const f of feathers) {
          const a = bez(f.c, 0), b = bez(f.c, 1);
          const lg = g.createLinearGradient(a[0], a[1], b[0], b[1]);
          lg.addColorStop(0, `rgba(255,140,40,${.95 * e})`); lg.addColorStop(.45, `rgba(240,70,20,${.85 * e})`); lg.addColorStop(1, "rgba(160,20,10,0)");
          g.fillStyle = lg; g.fill(strip(f.c, f.w, f.seed, time, 1, f.thin ? 1 : .95));
          const cg = g.createLinearGradient(a[0], a[1], b[0], b[1]);
          cg.addColorStop(0, `rgba(255,255,220,${(f.bright ? .95 : .75) * e})`); cg.addColorStop(.35, `rgba(255,214,90,${.7 * e})`); cg.addColorStop(.8, "rgba(255,150,40,0)");
          g.fillStyle = cg; g.fill(strip(f.c, f.w, f.seed + .5, time, 1, f.thin ? .5 : .38));
        }
        g.restore();
        // a cabeça: chama clara, bico curvo escuro, olho branco que brilha
        g.save(); view(g, 1); g.globalAlpha = e;
        const hg = g.createRadialGradient(hx - 6, hy - 6, 2, hx, hy, 44);
        hg.addColorStop(0, "#FFFBE0"); hg.addColorStop(.5, "#FFC23A"); hg.addColorStop(1, "rgba(255,110,30,.9)");
        g.fillStyle = hg; g.beginPath(); g.ellipse(hx, hy, 44, 35, -.45, 0, PI * 2); g.fill();
        g.fillStyle = "#3A1208"; g.beginPath();
        g.moveTo(hx + 26, hy - 6); g.quadraticCurveTo(hx + 62, hy - 2, hx + 66, hy + 22); g.quadraticCurveTo(hx + 50, hy + 10, hx + 28, hy + 12); g.closePath(); g.fill();
        g.fillStyle = "#2A0A05"; g.beginPath(); g.arc(hx + 6, hy - 6, 9, 0, PI * 2); g.fill();
        g.fillStyle = "#FFFFFF"; g.shadowColor = "#FFF3B0"; g.shadowBlur = 18; g.beginPath(); g.arc(hx + 8, hy - 8, 4.5, 0, PI * 2); g.fill();
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
