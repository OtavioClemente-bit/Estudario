// O Folha da arte oficial (design/mascote/folha-turnaround.webp), animado com os pixels dela.
// A vista de frente foi recortada em partes (scripts/web/mascote): livro, braços, pernas, tênis,
// capelo, borla, fita, bochechas, olhos e boca. Cada parte gira/estica no próprio pivô. Por cima da
// arte: olhos abertos que piscam e olham, folha que vira em perspectiva (a mão empurra, a outra
// alisa), lápis que escreve na página, sombras de contato. Arrastando, vira para 3/4, lado e costas.
//
// Uso: <estudario-mascote mood="idle|happy|wave|talking|thinking|sad|point|wink" size="160" [spin]>
// Canvas 2D (sem WebGL). Para fora da tela e com a aba escondida, para.
(function () {
  if (!window.customElements || customElements.get("estudario-mascote")) return;

  // ------------------------------------------------------------ geometria (px do recorte 606 x 610)
  const L = {"parts":{"cheekL":{"x":164,"y":272,"w":56,"h":34,"pivot":[192,294]},"cheekR":{"x":409,"y":273,"w":55,"h":35,"pivot":[436,296]},"tassel":{"x":419,"y":78,"w":52,"h":107,"pivot":[445,88]},"cap":{"x":178,"y":18,"w":275,"h":143,"pivot":[318,146]},"ribbon":{"x":298,"y":390,"w":40,"h":69,"pivot":[320,400]},"mouth":{"x":284,"y":278,"w":58,"h":39,"pivot":[312,280]},"eyeL":{"x":190,"y":225,"w":75,"h":43,"pivot":[226,262]},"eyeR":{"x":363,"y":226,"w":74,"h":43,"pivot":[399,262]},"armL":{"x":12,"y":162,"w":108,"h":162,"pivot":[108,290]},"armR":{"x":504,"y":206,"w":93,"h":128,"pivot":[522,306]},"legL":{"x":213,"y":396,"w":90,"h":129,"pivot":[262,412]},"legR":{"x":330,"y":396,"w":77,"h":129,"pivot":[366,412]},"shoeL":{"x":126,"y":477,"w":161,"h":128,"pivot":[200,600]},"shoeR":{"x":343,"y":478,"w":140,"h":127,"pivot":[412,600]},"body":{"x":0,"y":0,"w":606,"h":610}},"views":{"v34":{"w":482,"h":581},"side":{"w":268,"h":591},"back":{"w":566,"h":590}},"front":{"x":14,"y":20,"w":581,"h":583}};
  const P = L.parts;
  const CUFF = { L: [231, 490], R: [381, 490] };       // onde a perna entra no tênis
  const HIP = { L: [263, 414], R: [352, 414] };        // onde a perna sai de baixo da capa
  const BOOK_BASE = [313, 428];                         // pé do livro: pivô do corpo
  const GROUND = L.front.y + L.front.h, CX = L.front.x + L.front.w / 2;
  const GUT = 316, PT = 128, PB = 370, PWR = 182, PWL = 186;   // costura e folhas de cima
  const FIST = { L: [48, 197], R: [562, 246] };         // centro dos punhos na arte
  const EYE = { L: [227, 250], R: [400, 250] }, MOUTH = [313, 298];
  const WRITE = { x0: 410, x1: 484, y0: 330, dy: 14, lines: 3 };   // onde ele escreve (página direita)

  const base = (document.currentScript && document.currentScript.src) ? document.currentScript.src.replace(/[^/]*$/, "") : "";
  const NAMES = ["body", "armL", "armR", "shoeL", "shoeR", "ribbon", "cap", "tassel", "eyeL", "eyeR", "mouth", "cheekL", "cheekR", "v34", "side", "back"];
  let imgs = null, ready = null;
  function load() {
    if (ready) return ready;
    imgs = {};
    ready = Promise.all(NAMES.map((n) => new Promise((res) => {
      const im = new Image(); im.decoding = "async";
      im.onload = () => res(); im.onerror = () => res();
      im.src = base + "mascote-" + n + ".png"; imgs[n] = im;
    })));
    return ready;
  }

  const reduce = window.matchMedia ? window.matchMedia("(prefers-reduced-motion: reduce)") : { matches: false };
  const clamp = (v, a, b) => Math.max(a, Math.min(b, v));
  const ease = (x) => x * x * (3 - 2 * x);
  const wrap = (a) => Math.atan2(Math.sin(a), Math.cos(a));
  const spring = (s, target, k, d, dt) => { s.v += ((target - s.x) * k - s.v * d) * dt; s.x += s.v * dt; return s.x; };
  const follow = (cur, target, rate, dt) => cur + (target - cur) * (1 - Math.exp(-dt * rate));

  // Ângulo e escala para o punho (preso no ombro) chegar num ponto.
  function reach(side, tx, ty) {
    const S = P["arm" + side].pivot, F = FIST[side];
    const a = Math.atan2(ty - S[1], tx - S[0]) - Math.atan2(F[1] - S[1], F[0] - S[0]);
    const d = Math.hypot(tx - S[0], ty - S[1]), d0 = Math.hypot(F[0] - S[0], F[1] - S[1]);
    return { ang: wrap(a), scale: d / d0 };
  }

  class Mascote {
    constructor(canvas) {
      this.c = canvas; this.g = canvas.getContext("2d");
      this.mood = "idle"; this.t = 0;
      this.cap = { x: 0, v: 0 }; this.tas = { x: 0, v: 0 }; this.rib = { x: 0, v: 0 };
      this.jump = 0; this.jumpV = 0; this.pokeT = -1;
      this.blinkAt = 1.6; this.blinkT = -1; this.double = false;
      this.look = { x: 0, y: 0 }; this.pointer = null; this.glance = { x: 0, y: 0, until: 0, next: 3 };
      this.yaw = 0; this.yawV = 0; this.dragging = false;
      this.arm = { L: { ang: 0, scl: 1 }, R: { ang: 0, scl: 1 } };
      this.flip = null; this.nextFlip = 7 + Math.random() * 4; this.peek = null; this.nextPeek = 4; this.smoothT = 0;
      this.writer = null; this.ink = null; this.flipTex = null;
      this.prevRot = 0; this.prevBob = 0; this.k = 1;
    }
    setMood(m) { this.mood = m; }
    poke() {
      if (this.jump <= 0.001) { this.jumpV = 520; this.pokeT = 0; }
      if (!this.flip && !this.peek) this.peek = { p: 0, max: 0.16 };
    }
    // ---- folha que vira: posição horizontal, escala de perspectiva e se mostra o verso, para cada u
    geom(p, u) {
      const lead = Math.sin(2 * Math.PI * p) * 0.22;
      const e = ease(clamp(p + lead * u, 0, 1)), th = Math.PI * e, W = PWR + (PWL - PWR) * e;
      const z = u * W * Math.sin(th);
      return { x: GUT + u * W * Math.cos(th), s: 1 + z * 0.0011, z, back: Math.cos(th) < 0, th };
    }
    grabPoint(p, v = 0.8) {
      const q = this.geom(p, 1), H = PB - PT;
      return [q.x, PT - (q.s - 1) * H * 0.5 - q.z * 0.06 + H * q.s * v];
    }
    startFlip(hand) {
      if (this.flip || reduce.matches || !imgs) return;
      this.ensureCanvases();
      const f = this.flipTex.getContext("2d");
      f.clearRect(0, 0, 606, 610); f.drawImage(imgs.body, 0, 0); f.drawImage(this.ink, 0, 0);
      this.ink.getContext("2d").clearRect(0, 0, 606, 610);
      this.flip = { p: 0, dur: hand ? 1.25 : 0.8, wait: hand ? 0.35 : 0, hand, released: !hand, caught: false };
      this.peek = null;
    }
    ensureCanvases() {
      if (this.ink) return;
      const mk = () => { const c = document.createElement("canvas"); c.width = 606; c.height = 610; return c; };
      this.ink = mk(); this.flipTex = mk();
    }

    // ---- pose do momento (radianos; positivo = horário na tela)
    pose(t) {
      const m = this.mood, still = reduce.matches;
      const p = { bob: 0, rot: 0, sx: 1, sy: 1, armL: 0, armR: 0, liftL: 0, liftR: 0, pitchL: 0, pitchR: 0, mouth: 1, mouthKind: "art", eyes: "open", lid: 1 };
      const br = still ? 0 : Math.sin(t * 2.1);
      p.sy = 1 + 0.012 * br; p.sx = 1 - 0.006 * br;
      p.armL = still ? 0 : -0.05 * Math.sin(t * 1.8); p.armR = still ? 0 : 0.05 * Math.sin(t * 1.8 + 0.6);
      if (m === "happy") {
        p.eyes = "arc"; p.mouth = 1.08;
        if (!still) {
          const ph = t * 6.2, s = Math.sin(ph);
          p.bob = -Math.abs(Math.sin(ph)) * 16; p.rot = 0.05 * Math.sin(ph * 0.5);
          p.armL = -0.28 * Math.abs(Math.sin(ph)); p.armR = 0.28 * Math.abs(Math.sin(ph + 1.2));
          p.liftL = Math.max(0, s) * 22; p.liftR = Math.max(0, -s) * 22; p.pitchL = -Math.max(0, s) * 0.35; p.pitchR = Math.max(0, -s) * 0.35;
          p.mouth = 1.1 + 0.1 * Math.abs(s);
        }
      } else if (m === "wave") {
        if (!still) { p.armR = 0.15 + 0.38 * Math.sin(t * 9); p.rot = 0.03; }
      } else if (m === "talking") {
        if (!still) { p.mouth = 0.55 + 0.55 * Math.abs(Math.sin(t * 10.5)) * (0.6 + 0.4 * Math.sin(t * 2.7)); p.armR = 0.22 * Math.sin(t * 3.1); p.rot = 0.02 * Math.sin(t * 1.7); p.bob = -2 * Math.abs(Math.sin(t * 3.1)); }
      } else if (m === "thinking") {
        p.rot = -0.03; p.mouthKind = "hmm"; p.armL = -0.08;
      } else if (m === "point") {
        p.armR = 0.75; p.rot = 0.04;
      } else if (m === "wink") {
        p.eyes = "wink"; p.armR = 0.2; p.rot = 0.04; p.bob = -3;
      } else if (m === "sad") {
        p.armL = -0.55; p.armR = 0.5; p.rot = 0.03; p.bob = 7; p.sy *= 0.97; p.mouthKind = "frown"; p.lid = 0.82;
      }
      return p;
    }

    update(dt) {
      const t = (this.t += dt), still = reduce.matches;
      const p = this.pose(t);
      // Pulo do toque e amassado no pouso.
      if (this.jump > 0 || this.jumpV > 0) { this.jumpV -= 2200 * dt; this.jump += this.jumpV * dt; if (this.jump <= 0) { this.jump = 0; this.jumpV = 0; this.pokeT = 0.0001; } }
      let land = 0;
      if (this.pokeT >= 0 && this.jump === 0) { this.pokeT += dt; land = Math.exp(-this.pokeT * 9) * Math.sin(this.pokeT * 28); if (this.pokeT > 0.8) this.pokeT = -1; }
      p.sy *= 1 - 0.07 * land; p.sx *= 1 + 0.05 * land;
      if (this.jump > 0) { const v = clamp(this.jumpV / 520, -1, 1); p.sy *= 1 + v * 0.05; p.sx *= 1 - v * 0.03; }

      // Piscar (às vezes duas vezes).
      if (t > this.blinkAt && this.blinkT < 0) this.blinkT = 0;
      let lid = 1;
      if (this.blinkT >= 0) {
        this.blinkT += dt; const q = this.blinkT / 0.16; lid = q < 1 ? Math.abs(Math.cos(Math.PI * q)) : 1;
        if (q >= 1) { this.blinkT = -1; if (!this.double && Math.random() < 0.25) { this.double = true; this.blinkAt = t + 0.12; } else { this.double = false; this.blinkAt = t + 2.2 + Math.random() * 3.2; } }
      }
      p.lid *= Math.max(0.08, lid);

      // ---- folhear: de vez em quando, parado ou falando, ele vira a folha com a mão
      if (!still && !this.flip && t > this.nextFlip && (this.mood === "idle" || this.mood === "talking")) { this.startFlip(true); this.nextFlip = t + 10 + Math.random() * 6; }
      if (!still && !this.flip && !this.peek && t > this.nextPeek && this.mood !== "happy") { this.peek = { p: 0, max: 0.1 + Math.random() * 0.06 }; this.nextPeek = t + 5 + Math.random() * 5; }
      if (this.peek) { this.peek.p += dt / 0.9; if (this.peek.p >= 1) this.peek = null; }
      const f = this.flip;
      let reachR = null, reachL = null;
      if (f) {
        if (f.wait > 0) f.wait -= dt; else f.p = Math.min(1, f.p + dt / f.dur);
        const gp = this.grabPoint(f.p);
        if (f.hand) {
          const r = reach("R", gp[0] + 6, gp[1]);
          if (!f.released && (r.scale > 1.15 || f.p > 0.5)) f.released = true;
          if (!f.released) reachR = r;
          const l = reach("L", gp[0] - 4, gp[1]);
          if (!f.caught && f.p > 0.62 && l.scale < 1.12) f.caught = true;
          if (f.caught) reachL = l;
        }
        if (f.p >= 1) { if (f.caught) this.smoothT = 0.35; this.flip = null; }
      }
      if (this.smoothT > 0) { this.smoothT -= dt; const gp = this.grabPoint(1); reachL = reach("L", gp[0] - 4, gp[1]); }

      // ---- escrever: pensando, o lápis corre pela página e, depois de três linhas, ele vira a folha
      this.pen = null;
      if (this.mood === "thinking" && !still && imgs) {
        this.ensureCanvases();
        const w = this.writer || (this.writer = { line: 0, x: WRITE.x0, last: null, pause: 0.6 });
        if (!this.flip) {
          if (w.pause > 0) w.pause -= dt;
          else {
            w.x += dt * 26;
            if (w.x > WRITE.x1) { w.line++; w.x = WRITE.x0; w.last = null; w.pause = 0.35; if (w.line >= WRITE.lines) { w.line = 0; this.startFlip(true); } }
          }
        }
        const ph = w.x * 0.95, ly = WRITE.y0 + w.line * WRITE.dy;
        const tx = w.x + 2.4 * Math.cos(ph), ty = ly + 3.4 * Math.sin(ph) + 1.2 * Math.sin(ph * 0.37);
        const down = !this.flip && w.pause <= 0 && Math.sin(w.x * 0.19) < 0.86;
        this.pen = { x: tx, y: ty - (down ? 0 : 6), down };
        if (down) {
          const g = this.ink.getContext("2d");
          if (w.last) { g.strokeStyle = "rgba(55,52,66,.82)"; g.lineWidth = 1.5; g.lineCap = "round"; g.lineJoin = "round"; g.beginPath(); g.moveTo(w.last[0], w.last[1]); g.lineTo(tx, ty); g.stroke(); }
          w.last = [tx, ty];
        } else w.last = null;
        if (!reachR) reachR = reach("R", tx + 34, ty - 42);
      }

      // Braços: alvo da pose, ou o punho indo até a folha/lápis.
      for (const side of ["L", "R"]) {
        const a = this.arm[side], r = side === "R" ? reachR : reachL;
        const tgtAng = r ? r.ang : p["arm" + side], tgtScl = r ? clamp(r.scale, 0.8, 1.12) : 1, rate = r ? 18 : 10;
        a.ang = follow(a.ang, a.ang + wrap(tgtAng - a.ang), rate, dt);
        a.scl = follow(a.scl, tgtScl, rate, dt);
        a.front = side === "L" || !!r || Math.abs(a.ang) > 1.1;
      }

      // Olhar: cursor, olhadelas, o lápis quando escreve, a folha quando vira.
      let lx = 0, ly = 0;
      if (this.pointer) { lx = clamp(this.pointer.x, -1, 1); ly = clamp(this.pointer.y, -1, 1); }
      if (t > this.glance.next) this.glance = { x: Math.random() * 2 - 1, y: Math.random() - 0.5, until: t + 0.9, next: t + 3 + Math.random() * 4 };
      if (t < this.glance.until && !this.pointerActive) { lx = this.glance.x; ly = this.glance.y; }
      if (this.pen) { lx = 0.8; ly = 0.9; }
      if (this.flip) { const gp = this.grabPoint(this.flip.p); lx = clamp((gp[0] - 313) / 150, -1, 1); ly = 0.3; }
      if (this.mood === "point") { lx = 1; ly = 0; }
      if (this.mood === "sad") ly = 0.7;
      this.look.x = follow(this.look.x, lx, 9, dt); this.look.y = follow(this.look.y, ly, 9, dt);

      // Molas: capelo, borla e fita atrasam em relação ao corpo.
      const dRot = (p.rot - this.prevRot) / Math.max(dt, 1e-3), dBob = p.bob - this.prevBob;
      this.prevRot = p.rot; this.prevBob = p.bob;
      p.cap = spring(this.cap, -p.rot * 0.5 - dBob * 0.004, 160, 9, dt);
      this.tas.v += -dRot * 2.2 + dBob * 0.02 - this.jumpV * 0.00004;
      p.tas = spring(this.tas, -p.rot - p.cap * 0.5 + 0.03 * Math.sin(t * 1.3), 90, 3.2, dt);
      this.rib.v += -dRot * 1.6;
      p.rib = spring(this.rib, -p.rot * 0.8 + 0.04 * Math.sin(t * 1.7), 70, 3, dt);
      if (!this.dragging) { this.yawV += (-this.yaw * 30 - this.yawV * 8) * dt; this.yaw += this.yawV * dt; }
      this.p = p;
    }

    draw() {
      const { c, g, p } = this; if (!p || !imgs) return;
      const W = c.width, H = c.height;
      g.setTransform(1, 0, 0, 1, 0, 0); g.clearRect(0, 0, W, H);
      const fw = L.front.w + 60, fh = L.front.h + 70, k = Math.min(W / fw, H / fh);
      this.k = k;
      g.setTransform(k, 0, 0, k, W / 2 - CX * k, H - 14 * k - GROUND * k);
      const up = this.jump;
      // Sombras no chão: uma sob o livro e uma por pé (some quando o pé sobe).
      const blob = (x, y, rx, a) => { g.save(); g.translate(x, y); g.scale(1, 0.13); const gr = g.createRadialGradient(0, 0, 0, 0, 0, rx); gr.addColorStop(0, `rgba(30,24,80,${a})`); gr.addColorStop(1, "rgba(30,24,80,0)"); g.fillStyle = gr; g.beginPath(); g.arc(0, 0, rx, 0, Math.PI * 2); g.fill(); g.restore(); };
      blob(CX, GROUND - 3, 240 * (1 - Math.min(0.4, up / 300)), 0.2 - Math.min(0.12, up / 800));
      const yaw = ((this.yaw % 360) + 540) % 360 - 180;
      if (Math.abs(yaw) <= 8) {
        blob(205, GROUND - 2, 88, 0.3 * (1 - clamp((p.liftL + up) / 60, 0, 0.8)));
        blob(412, GROUND - 2, 84, 0.3 * (1 - clamp((p.liftR + up) / 60, 0, 0.8)));
      }
      g.translate(0, -up);
      if (Math.abs(yaw) > 8) { this.drawView(yaw); return; }
      this.drawFront();
    }
    // Vista girada: frente, 3/4, lado e costas da arte; entre duas a figura "vira" (encolhe e abre na seguinte).
    drawView(yaw) {
      const g = this.g, a = Math.abs(yaw), mirror = yaw < 0;
      const stops = [[0, "front"], [45, "v34"], [90, "side"], [180, "back"]];
      let i = 0; while (i < stops.length - 1 && a > stops[i + 1][0]) i++;
      const [a0, n0] = stops[i], [a1, n1] = stops[Math.min(i + 1, 3)], f = a1 === a0 ? 0 : (a - a0) / (a1 - a0);
      const name = f < 0.5 ? n0 : n1, squeeze = 1 - 0.55 * Math.sin(Math.PI * f);
      g.save();
      g.translate(CX, GROUND); g.scale((mirror ? -1 : 1) * squeeze, 1); g.translate(-CX, -GROUND + Math.sin(this.t * 2.1) * 2);
      if (name === "front") this.drawFront(true);
      else { const im = imgs[name], v = L.views[name]; g.drawImage(im, CX - v.w / 2, GROUND - v.h, v.w, v.h); }
      g.restore();
    }

    drawFront(noPose) {
      const g = this.g, k = this.k;
      const p = noPose ? { bob: 0, rot: 0, sx: 1, sy: 1, liftL: 0, liftR: 0, pitchL: 0, pitchR: 0, mouth: 1, mouthKind: "art", eyes: "open", lid: 1, cap: 0, tas: 0, rib: 0 } : this.p;
      const arm = noPose ? { L: { ang: 0, scl: 1, front: true }, R: { ang: 0, scl: 1, front: false } } : this.arm;
      const part = (name) => { const q = P[name]; g.drawImage(imgs[name], q.x, q.y, q.w, q.h); };
      const shadow = (on, blur = 9, ox = 3, oy = 6, a = 0.26) => {
        if (on) { g.shadowColor = `rgba(28,20,72,${a})`; g.shadowBlur = blur * k; g.shadowOffsetX = ox * k; g.shadowOffsetY = oy * k; }
        else { g.shadowColor = "transparent"; g.shadowBlur = 0; g.shadowOffsetX = 0; g.shadowOffsetY = 0; }
      };
      const bodyM = new DOMMatrix().translate(BOOK_BASE[0], BOOK_BASE[1] + p.bob).rotate(p.rot * 180 / Math.PI).scale(p.sx, p.sy).translate(-BOOK_BASE[0], -BOOK_BASE[1]);
      const shoeM = (side) => { const q = P["shoe" + side]; return new DOMMatrix().translate(q.pivot[0], q.pivot[1] - p["lift" + side]).rotate(p["pitch" + side] * 180 / Math.PI).translate(-q.pivot[0], -q.pivot[1]); };
      const withM = (M, fn) => { g.save(); g.transform(M.a, M.b, M.c, M.d, M.e, M.f); fn(); g.restore(); };
      const drawArm = (side) => { const a = arm[side], q = P["arm" + side]; g.save(); g.translate(q.pivot[0], q.pivot[1]); g.rotate(a.ang); g.scale(a.scl, a.scl); g.translate(-q.pivot[0], -q.pivot[1]); part("arm" + side); g.restore(); };

      // Braço direito atrás da capa (quando não está na frente do livro).
      if (!arm.R.front) withM(bodyM, () => drawArm("R"));
      // Pernas: tubo azul com o mesmo sombreado da arte (brilho à esquerda, corpo escuro, contraluz à
      // direita), do quadril (vai com o corpo) até dentro do punho do tênis (vai com o pé). O joelho
      // dobra quando o pé sobe; as pontas ficam escondidas atrás do livro e dentro do tênis.
      for (const side of ["L", "R"]) {
        const hip0 = HIP[side], cuff0 = CUFF[side];
        const h = bodyM.transformPoint(new DOMPoint(hip0[0], hip0[1] - 8)), c2 = shoeM(side).transformPoint(new DOMPoint(cuff0[0], cuff0[1] + 14));
        const l0 = Math.hypot(cuff0[0] - hip0[0], cuff0[1] + 22 - hip0[1]), l = Math.hypot(c2.x - h.x, c2.y - h.y);
        const bend = Math.sqrt(Math.max(0, l0 * l0 - l * l)) * 0.5, sgn = side === "L" ? -1 : 1;
        const nx = -(c2.y - h.y) / (l || 1), ny = (c2.x - h.x) / (l || 1);
        const out = sgn < 0 ? 1 : -1;   // n aponta para a esquerda numa perna para baixo: joelho para fora
        const kx = (h.x + c2.x) / 2 + nx * bend * out, ky = (h.y + c2.y) / 2 + ny * bend * out;
        const at = (t) => [(1 - t) * (1 - t) * h.x + 2 * (1 - t) * t * kx + t * t * c2.x, (1 - t) * (1 - t) * h.y + 2 * (1 - t) * t * ky + t * t * c2.y];
        const N = 14, w0 = 36, w1 = 33;
        for (let i = 0; i < N; i++) {
          const t0 = i / N, t1 = (i + 1) / N, [x0, y0] = at(t0), [x1, y1] = at(t1);
          const ang = Math.atan2(y1 - y0, x1 - x0), len = Math.hypot(x1 - x0, y1 - y0) + 1.2, w = w0 + (w1 - w0) * (t0 + t1) / 2;
          g.save(); g.translate(x0, y0); g.rotate(ang);
          const gr = g.createLinearGradient(0, w / 2, 0, -w / 2);
          gr.addColorStop(0, "#5A6BE6"); gr.addColorStop(0.08, "#3141C6"); gr.addColorStop(0.26, "#5459CF"); gr.addColorStop(0.46, "#1E36A6");
          gr.addColorStop(0.72, "#11288C"); gr.addColorStop(0.92, "#12247E"); gr.addColorStop(1, "#5E66B3");
          g.fillStyle = gr; g.fillRect(-0.6, -w / 2, len, w); g.restore();
        }
      }
      for (const side of ["L", "R"]) withM(shoeM(side), () => part("shoe" + side));

      withM(bodyM, () => {
        part("body");
        if (this.ink) g.drawImage(this.ink, 0, 0);
        if (!noPose) this.drawPages();
        // Rosto por cima de tudo que é papel (como o Folha de sempre).
        part("cheekL"); part("cheekR");
        this.drawEyes(p);
        this.drawMouth(p);
        g.save(); g.translate(P.ribbon.pivot[0], P.ribbon.pivot[1]); g.rotate(p.rib); g.translate(-P.ribbon.pivot[0], -P.ribbon.pivot[1]); part("ribbon"); g.restore();
        // Lápis (na mão direita) e braços da frente, com sombra no papel.
        if (this.pen && !noPose) this.drawPencil(this.pen);
        shadow(true); drawArm("L"); if (arm.R.front) drawArm("R"); shadow(false);
        // Capelo e borla, com sombra macia nas folhas.
        g.save(); g.translate(P.cap.pivot[0], P.cap.pivot[1]); g.rotate(p.cap); g.translate(-P.cap.pivot[0], -P.cap.pivot[1]);
        shadow(true, 10, 0, 5, 0.22); part("cap"); shadow(false);
        g.save(); g.translate(P.tassel.pivot[0], P.tassel.pivot[1]); g.rotate(p.tas); g.translate(-P.tassel.pivot[0], -P.tassel.pivot[1]);
        shadow(true, 6, 2, 5, 0.25); part("tassel"); shadow(false); g.restore();
        g.restore();
      });
    }

    // Folha virando (e a "espiadinha" do canto), desenhada em fatias verticais com perspectiva e sombra.
    drawPages() {
      const g = this.g, f = this.flip, pk = this.peek;
      let p = null, tex = imgs.body;
      if (f) { p = f.p; tex = this.flipTex; }
      else if (pk) p = Math.sin(Math.PI * pk.p) * pk.max;
      if (p == null || p <= 0.001) return;
      const H = PB - PT, N = 44;
      // Sombra que a folha joga na página de baixo.
      const edge = this.geom(p, 1), sh = Math.sin(edge.th);
      if (!edge.back) { const x1 = edge.x + 26 * sh, gr = g.createLinearGradient(GUT, 0, x1, 0); gr.addColorStop(0, `rgba(40,30,80,${0.28 * sh})`); gr.addColorStop(1, "rgba(40,30,80,0)"); g.fillStyle = gr; g.fillRect(GUT, PT + 4, x1 - GUT, H - 8); }
      else { const x1 = edge.x - 26 * sh, gr = g.createLinearGradient(GUT, 0, x1, 0); gr.addColorStop(0, `rgba(40,30,80,${0.28 * sh})`); gr.addColorStop(1, "rgba(40,30,80,0)"); g.fillStyle = gr; g.fillRect(x1, PT + 4, GUT - x1, H - 8); }
      for (let i = 0; i < N; i++) {
        const u0 = i / N, u1 = (i + 1) / N, a = this.geom(p, u0), b = this.geom(p, u1), m = this.geom(p, (u0 + u1) / 2);
        const dx = Math.min(a.x, b.x), dw = Math.abs(b.x - a.x) + 0.8;
        if (dw < 0.3) continue;
        const dy = PT - (m.s - 1) * H * 0.5 - m.z * 0.06, dh = H * m.s;
        if (!m.back) g.drawImage(tex, GUT + u0 * PWR, PT, PWR / N + 0.5, H, dx, dy, dw, dh);
        else g.drawImage(imgs.body, GUT - u1 * PWL, PT, PWL / N + 0.5, H, dx, dy, dw, dh);
        // Luz: a folha escurece de lado e clareia de frente; o verso é um pouco mais escuro.
        const c = Math.abs(Math.cos(m.th));
        g.fillStyle = `rgba(36,28,80,${0.24 * (1 - c) + (m.back ? 0.06 : 0)})`; g.fillRect(dx, dy, dw, dh);
        if (c > 0.6 && Math.sin(m.th) > 0.05) { g.fillStyle = `rgba(255,255,255,${0.12 * Math.sin(m.th)})`; g.fillRect(dx, dy, dw, dh); }
      }
      // Fio de luz na borda solta.
      const top = PT - (edge.s - 1) * H * 0.5 - edge.z * 0.06;
      g.strokeStyle = "rgba(255,255,255,.7)"; g.lineWidth = 1.2;
      g.beginPath(); g.moveTo(edge.x, top + 3); g.lineTo(edge.x, top + H * edge.s - 3); g.stroke();
    }

    // Olhos abertos no estilo da arte (laca azul-marinho, reflexos), ou os arcos originais (feliz/piscadela).
    drawEyes(p) {
      const g = this.g, lx = this.look.x, ly = this.look.y;
      for (const side of ["L", "R"]) {
        const arc = p.eyes === "arc" || (p.eyes === "wink" && side === "R");
        if (arc) {
          const q = P["eye" + side]; g.save(); g.translate(q.pivot[0], q.pivot[1]); g.scale(1, 0.85 + 0.15 * p.lid); g.translate(-q.pivot[0], -q.pivot[1]); g.drawImage(imgs["eye" + side], q.x, q.y, q.w, q.h); g.restore();
          continue;
        }
        const [cx, cy] = EYE[side];
        g.save(); g.translate(cx + lx * 3.5, cy + ly * 3); g.scale(1, p.lid);
        g.fillStyle = "rgba(60,40,120,0.16)"; g.beginPath(); g.ellipse(1.5, 4, 18.5, 24.5, 0, 0, Math.PI * 2); g.fill();
        const gr = g.createRadialGradient(-6, -10, 1, 0, 0, 27);
        gr.addColorStop(0, "#4C44B4"); gr.addColorStop(0.32, "#241C74"); gr.addColorStop(1, "#0C082E");
        g.fillStyle = gr; g.beginPath(); g.ellipse(0, 0, 17, 23, 0, 0, Math.PI * 2); g.fill();
        g.strokeStyle = "rgba(150,140,255,.38)"; g.lineWidth = 2.2; g.beginPath(); g.ellipse(0, 0, 14.5, 20.5, 0, Math.PI * 0.18, Math.PI * 0.82); g.stroke();
        if (p.lid > 0.4) {
          g.fillStyle = "rgba(255,255,255,.96)"; g.beginPath(); g.ellipse(-6 + lx * 1.5, -9 + ly * 1.2, 6, 7.6, -0.45, 0, Math.PI * 2); g.fill();
          g.fillStyle = "rgba(255,255,255,.82)"; g.beginPath(); g.arc(6, 9, 2.7, 0, Math.PI * 2); g.fill();
        }
        g.restore();
        if (this.mood === "sad") { const s = side === "L" ? 1 : -1; g.strokeStyle = "#1C1550"; g.lineWidth = 4; g.lineCap = "round"; g.beginPath(); g.moveTo(cx - 16 * s, cy - 30); g.lineTo(cx + 12 * s, cy - 36); g.stroke(); }
      }
    }
    drawMouth(p) {
      const g = this.g;
      if (p.mouthKind === "art") {
        const q = P.mouth; g.save(); g.translate(q.pivot[0], q.pivot[1]); g.scale(1 + (p.mouth - 1) * 0.15, p.mouth); g.translate(-q.pivot[0], -q.pivot[1]); g.drawImage(imgs.mouth, q.x, q.y, q.w, q.h); g.restore();
        return;
      }
      const [mx, my] = MOUTH;
      g.lineCap = "round"; g.strokeStyle = "#231850"; g.lineWidth = 6;
      g.beginPath();
      if (p.mouthKind === "frown") { g.moveTo(mx - 16, my + 6); g.quadraticCurveTo(mx, my - 8, mx + 16, my + 6); }
      else { g.moveTo(mx - 12, my + 1); g.bezierCurveTo(mx - 4, my + 6, mx + 4, my - 4, mx + 13, my); }
      g.stroke();
      g.strokeStyle = "rgba(255,255,255,.35)"; g.lineWidth = 1.4; g.stroke();
    }
    // Lápis desenhado: corpo amarelo sextavado, madeira, grafite, virola de metal e borracha.
    drawPencil(pen) {
      const g = this.g, dx = 34, dy = -42, n = Math.hypot(dx, dy), L2 = n + 38;
      g.save(); g.translate(pen.x, pen.y); g.rotate(Math.atan2(dy, dx));
      g.shadowColor = "rgba(28,20,72,.25)"; g.shadowBlur = 6 * this.k; g.shadowOffsetX = 3 * this.k; g.shadowOffsetY = 5 * this.k;
      const w = 6.5;
      g.fillStyle = "#2E2A33"; g.beginPath(); g.moveTo(0, 0); g.lineTo(5, -1.3); g.lineTo(5, 1.3); g.closePath(); g.fill();
      g.shadowColor = "transparent";
      g.fillStyle = "#E9C79A"; g.beginPath(); g.moveTo(4.5, -1.4); g.lineTo(14, -w / 2); g.lineTo(14, w / 2); g.lineTo(4.5, 1.4); g.closePath(); g.fill();
      const body = g.createLinearGradient(0, -w / 2, 0, w / 2);
      body.addColorStop(0, "#FFE07A"); body.addColorStop(0.35, "#F7C531"); body.addColorStop(0.65, "#E0A91C"); body.addColorStop(1, "#B9840F");
      g.fillStyle = body; g.fillRect(14, -w / 2, L2 - 26, w);
      g.strokeStyle = "rgba(150,100,10,.5)"; g.lineWidth = 0.6; g.beginPath(); g.moveTo(14, -0.8); g.lineTo(L2 - 12, -0.8); g.moveTo(14, 1.4); g.lineTo(L2 - 12, 1.4); g.stroke();
      const fer = g.createLinearGradient(0, -w / 2, 0, w / 2); fer.addColorStop(0, "#F2F2F6"); fer.addColorStop(0.5, "#B8BAC6"); fer.addColorStop(1, "#8C8E9A");
      g.fillStyle = fer; g.fillRect(L2 - 12, -w / 2 - 0.3, 6, w + 0.6);
      const er = g.createLinearGradient(0, -w / 2, 0, w / 2); er.addColorStop(0, "#FFB3C7"); er.addColorStop(1, "#E0708E");
      g.fillStyle = er; g.beginPath(); g.moveTo(L2 - 6, -w / 2); g.lineTo(L2 - 1.5, -w / 2); g.quadraticCurveTo(L2 + 1, 0, L2 - 1.5, w / 2); g.lineTo(L2 - 6, w / 2); g.closePath(); g.fill();
      g.restore();
    }
  }

  // ------------------------------------------------------------ um laço só para todos
  const live = new Set();
  let raf = 0, last = 0;
  const MAX_DT = location.search.includes("shot") ? 0.2 : 1 / 30;
  function loop(now) {
    const dt = Math.max(1 / 240, Math.min(MAX_DT, last ? (now - last) / 1000 : 1 / 60)); last = now;
    for (const el of live) if (el._visible && el._m) { el._m.update(dt); el._m.draw(); }
    raf = live.size ? requestAnimationFrame(loop) : 0; if (!raf) last = 0;
  }
  let hooked = false;
  function hookPointer() {
    if (hooked) return; hooked = true;
    window.addEventListener("pointermove", (e) => {
      for (const el of live) {
        const r = el.getBoundingClientRect(), m = el._m;
        m.pointer = { x: (e.clientX - (r.left + r.width / 2)) / Math.max(220, window.innerWidth / 3), y: (e.clientY - (r.top + r.height * 0.4)) / Math.max(220, window.innerHeight / 3) };
        m.pointerActive = true; clearTimeout(el._pt); el._pt = setTimeout(() => { m.pointerActive = false; m.pointer = null; }, 2500);
      }
    }, { passive: true });
  }

  customElements.define("estudario-mascote", class extends HTMLElement {
    static get observedAttributes() { return ["mood"]; }
    connectedCallback() {
      if (this._m) { load().then(() => { live.add(this); if (!raf) raf = requestAnimationFrame(loop); }); return; }
      const size = +(this.getAttribute("size") || 0);
      if (size) { this.style.width = this.style.width || size + "px"; this.style.height = this.style.height || size + "px"; }
      if (!this.style.display) this.style.display = "inline-block";
      if (!this.style.position) this.style.position = "relative";
      const c = document.createElement("canvas");
      c.style.cssText = "position:absolute;left:-15%;top:-15%;width:130%;height:130%;pointer-events:none";
      this.appendChild(c);
      this._m = new Mascote(c); this._m.setMood(this.getAttribute("mood") || "idle");
      const fit = () => { const dpr = Math.min(window.devicePixelRatio || 1, 2); c.width = Math.max(1, Math.round(this.clientWidth * 1.3 * dpr)); c.height = Math.max(1, Math.round(this.clientHeight * 1.3 * dpr)); };
      this._ro = new ResizeObserver(fit); this._ro.observe(this); fit();
      this._io = new IntersectionObserver((es) => { for (const e of es) this._visible = e.isIntersecting && !document.hidden; }); this._io.observe(this);
      this._visible = true;
      let down = null;
      this.addEventListener("pointerdown", (e) => { down = { x: e.clientX, moved: 0 }; if (this.hasAttribute("spin")) { this._m.dragging = true; try { this.setPointerCapture(e.pointerId); } catch (_) { /* ok */ } } });
      this.addEventListener("pointermove", (e) => { if (!down || !this._m.dragging) return; const dx = e.clientX - down.x; down.moved += Math.abs(dx); down.x = e.clientX; this._m.yaw += dx * 0.6; this._m.yawV = dx * 30; });
      this.addEventListener("pointerup", () => { if (down && down.moved < 6) { this._m.poke(); this.dispatchEvent(new CustomEvent("folha-poke", { bubbles: true })); } down = null; this._m.dragging = false; });
      hookPointer();
      load().then(() => { live.add(this); if (!raf) raf = requestAnimationFrame(loop); });
    }
    disconnectedCallback() { live.delete(this); }
    attributeChangedCallback(n, _, v) { if (this._m && n === "mood") this._m.setMood(v || "idle"); }
  });
  document.addEventListener("visibilitychange", () => { for (const el of live) el._visible = !document.hidden; });
})();
