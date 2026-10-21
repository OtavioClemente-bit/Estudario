// O Folha da arte oficial (design/mascote/folha-turnaround.webp), animado só com os pixels dela.
// A vista de frente foi recortada em partes (scripts/web/mascote/recortar.html): livro, braços, pernas,
// tênis, capelo, borla, fita, olhos e boca. Cada parte gira/estica em volta do próprio pivô; o resto
// da folha (3/4, lado, costas) aparece quando a pessoa arrasta para girar.
//
// Uso: <estudario-mascote mood="idle|happy|wave|talking|thinking|sad|point|wink" size="160" [spin]>
// Canvas 2D (sem WebGL): leve, roda em qualquer celular. Para fora da tela e com a aba escondida, para.
(function () {
  if (window.customElements && customElements.get("estudario-mascote")) return;

  // Coordenadas do recorte da vista de frente (606 x 610 px da folha original).
  const L = {"parts":{"tassel":{"x":419,"y":78,"w":52,"h":107,"pivot":[445,88]},"cap":{"x":178,"y":18,"w":275,"h":143,"pivot":[318,146]},"ribbon":{"x":299,"y":390,"w":38,"h":69,"pivot":[320,400]},"mouth":{"x":284,"y":278,"w":58,"h":39,"pivot":[312,280]},"eyeL":{"x":190,"y":225,"w":75,"h":43,"pivot":[226,262]},"eyeR":{"x":363,"y":226,"w":74,"h":43,"pivot":[399,262]},"armL":{"x":12,"y":162,"w":105,"h":162,"pivot":[108,290]},"armR":{"x":504,"y":206,"w":93,"h":128,"pivot":[522,306]},"legL":{"x":213,"y":396,"w":90,"h":95,"pivot":[262,412]},"legR":{"x":323,"y":396,"w":83,"h":96,"pivot":[366,412]},"shoeL":{"x":126,"y":476,"w":161,"h":129,"pivot":[200,600]},"shoeR":{"x":338,"y":476,"w":146,"h":129,"pivot":[412,600]},"body":{"x":0,"y":0,"w":606,"h":610}},"views":{"v34":{"w":482,"h":581},"side":{"w":268,"h":591},"back":{"w":566,"h":590}},"front":{"x":14,"y":20,"w":581,"h":583}};
  const CUFF = { L: [250, 488], R: [366, 488] };     // onde a perna entra no tênis
  const BOOK_BASE = [313, 428];                       // embaixo do livro: pivô do corpo
  const GROUND = L.front.y + L.front.h;                // sola dos tênis
  const CX = L.front.x + L.front.w / 2;

  const base = (document.currentScript && document.currentScript.src) ? document.currentScript.src.replace(/[^/]*$/, "") : "";
  const NAMES = ["body", "armL", "armR", "legL", "legR", "shoeL", "shoeR", "ribbon", "cap", "tassel", "eyeL", "eyeR", "mouth", "v34", "side", "back"];
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
  const spring = (s, target, k, d, dt) => { s.v += ((target - s.x) * k - s.v * d) * dt; s.x += s.v * dt; return s.x; };

  class Mascote {
    constructor(canvas) {
      this.c = canvas; this.g = canvas.getContext("2d");
      this.mood = "idle"; this.t = Math.random() * 10;
      this.cap = { x: 0, v: 0 }; this.tas = { x: 0, v: 0 }; this.rib = { x: 0, v: 0 }; this.lean = { x: 0, v: 0 };
      this.jump = 0; this.jumpV = 0; this.pokeT = -1; this.blinkAt = 2; this.blinkT = -1;
      this.yaw = 0; this.yawV = 0; this.dragging = false;
      this.arm = { L: { x: 0, v: 0 }, R: { x: 0, v: 0 } };
      this.prevBodyX = 0; this.prevRot = 0;
    }
    poke() { if (this.jump <= 0.001) { this.jumpV = 520; this.pokeT = 0; } }
    // Pose do momento: deslocamentos e ângulos (radianos; positivo = horário na tela).
    pose(t) {
      const m = this.mood, still = reduce.matches;
      const p = { bob: 0, rot: 0, sx: 1, sy: 1, armL: 0, armR: 0, liftL: 0, liftR: 0, pitchL: 0, pitchR: 0, mouth: 1, eye: 1 };
      const br = still ? 0 : Math.sin(t * 2.1);
      p.sy = 1 + 0.012 * br; p.sx = 1 - 0.006 * br;
      p.armL = still ? 0 : -0.05 * Math.sin(t * 1.8); p.armR = still ? 0 : 0.05 * Math.sin(t * 1.8 + 0.6);
      if (still) return p;
      if (m === "happy") {
        const ph = t * 6.2, s = Math.sin(ph);
        p.bob = -Math.abs(Math.sin(ph)) * 16; p.rot = 0.05 * Math.sin(ph * 0.5);
        p.armL = -0.28 * Math.abs(Math.sin(ph)); p.armR = 0.28 * Math.abs(Math.sin(ph + 1.2));
        p.liftL = Math.max(0, s) * 22; p.liftR = Math.max(0, -s) * 22; p.pitchL = -Math.max(0, s) * 0.35; p.pitchR = Math.max(0, -s) * 0.35;
        p.mouth = 1.1 + 0.1 * Math.abs(s);
      } else if (m === "wave") {
        p.armR = 0.15 + 0.38 * Math.sin(t * 9); p.rot = 0.03; p.armL = -0.04 * Math.sin(t * 2);
      } else if (m === "talking") {
        p.mouth = 0.55 + 0.55 * Math.abs(Math.sin(t * 10.5)) * (0.6 + 0.4 * Math.sin(t * 2.7));
        p.armR = 0.22 * Math.sin(t * 3.1); p.rot = 0.02 * Math.sin(t * 1.7); p.bob = -2 * Math.abs(Math.sin(t * 3.1));
      } else if (m === "thinking") {
        p.rot = -0.05 + 0.02 * Math.sin(t * 1.3); p.armR = 0.5 + 0.06 * Math.sin(t * 2.4); p.armL = -0.1; p.eye = 0.8; p.mouth = 0.8;
      } else if (m === "point") {
        p.armR = 0.75; p.rot = 0.04; p.armL = -0.05;
      } else if (m === "wink") {
        p.armR = 0.2; p.rot = 0.04; p.bob = -3;
      } else if (m === "sad") {
        p.armL = -0.75; p.armR = 0.75; p.rot = 0.03; p.bob = 7; p.sy *= 0.97; p.mouth = 0.6; p.eye = 0.85;
      }
      return p;
    }
    update(dt) {
      const t = (this.t += dt);
      const p = this.pose(t);
      // Pulo do toque: corpo e pés sobem juntos; no pouso, amassa.
      if (this.jump > 0 || this.jumpV > 0) { this.jumpV -= 2200 * dt; this.jump += this.jumpV * dt; if (this.jump <= 0) { this.jump = 0; this.jumpV = 0; this.pokeT = 0.0001; } }
      let land = 0;
      if (this.pokeT >= 0 && this.jump === 0) { this.pokeT += dt; land = Math.exp(-this.pokeT * 9) * Math.sin(this.pokeT * 28); if (this.pokeT > 0.8) this.pokeT = -1; }
      p.sy *= 1 - 0.07 * land; p.sx *= 1 + 0.05 * land;
      if (this.jump > 0) { p.sy *= 1 + clamp(this.jumpV / 520, -1, 1) * 0.05; p.sx *= 1 - clamp(this.jumpV / 520, -1, 1) * 0.03; }
      // Piscadas: os olhos (arcos) se espremem rapidinho de vez em quando.
      if (t > this.blinkAt && this.blinkT < 0) this.blinkT = 0;
      if (this.blinkT >= 0) { this.blinkT += dt; const q = this.blinkT / 0.18; p.eye *= q < 1 ? 1 - 0.45 * Math.sin(Math.PI * q) : 1; if (q >= 1) { this.blinkT = -1; this.blinkAt = t + 2.5 + Math.random() * 3; } }
      // Molas: braços suavizados, capelo, borla e fita atrasam em relação ao corpo.
      p.armL = spring(this.arm.L, p.armL, 220, 22, dt);
      p.armR = spring(this.arm.R, p.armR, 220, 22, dt);
      const bodyX = p.rot, dRot = (p.rot - this.prevRot) / Math.max(dt, 1e-3), dBob = p.bob - (this.prevBob || 0);
      this.prevRot = p.rot; this.prevBob = p.bob;
      p.cap = spring(this.cap, -p.rot * 0.5 - dBob * 0.004, 160, 9, dt);
      this.tas.v += (-dRot * 2.2 + dBob * 0.02) ;
      p.tas = spring(this.tas, -p.rot - p.cap * 0.5, 90, 3.2, dt);
      this.rib.v += (-dRot * 1.6) ;
      p.rib = spring(this.rib, -p.rot * 0.8 + 0.04 * Math.sin(t * 1.7), 70, 3, dt);
      // Giro (arrastar): volta sozinho para a frente.
      if (!this.dragging) { this.yawV += (-this.yaw * 30 - this.yawV * 8) * dt; this.yaw += this.yawV * dt; }
      this.p = p;
    }
    draw() {
      const { c, g, p } = this; if (!p || !imgs) return;
      const W = c.width, H = c.height;
      g.setTransform(1, 0, 0, 1, 0, 0); g.clearRect(0, 0, W, H);
      // Enquadramento: o personagem inteiro com folga para os braços e o pulo.
      const fw = L.front.w + 60, fh = L.front.h + 70, k = Math.min(W / fw, H / fh);
      g.setTransform(k, 0, 0, k, W / 2 - CX * k, H - 14 * k - GROUND * k);
      // Sombra no chão.
      const up = this.jump;
      g.save(); g.translate(CX, GROUND - 4); g.scale(1, 0.12);
      const r = 230 * (1 - Math.min(0.4, up / 300)), sh = g.createRadialGradient(0, 0, 0, 0, 0, r);
      sh.addColorStop(0, `rgba(38,33,92,${0.32 - Math.min(0.2, up / 600)})`); sh.addColorStop(1, "rgba(38,33,92,0)");
      g.fillStyle = sh; g.beginPath(); g.arc(0, 0, r, 0, Math.PI * 2); g.fill(); g.restore();
      g.translate(0, -up);
      const yaw = ((this.yaw % 360) + 540) % 360 - 180;
      if (Math.abs(yaw) > 8) { this.drawView(yaw); return; }
      this.drawFront();
    }
    // Vista girada: a arte tem frente, 3/4, lado e costas. Entre duas vistas a figura "vira" (encolhe na
    // horizontal até a metade do caminho e abre já na vista seguinte), sem misturar duas imagens.
    drawView(yaw) {
      const g = this.g, a = Math.abs(yaw), mirror = yaw < 0;
      const stops = [[0, "front"], [45, "v34"], [90, "side"], [180, "back"]];
      let i = 0; while (i < stops.length - 1 && a > stops[i + 1][0]) i++;
      const [a0, n0] = stops[i], [a1, n1] = stops[Math.min(i + 1, 3)], f = a1 === a0 ? 0 : (a - a0) / (a1 - a0);
      const name = f < 0.5 ? n0 : n1, squeeze = 1 - 0.55 * Math.sin(Math.PI * f);
      const bob = Math.sin(this.t * 2.1) * 2;
      g.save();
      g.translate(CX, GROUND); g.scale((mirror ? -1 : 1) * squeeze, 1); g.translate(-CX, -GROUND + bob);
      if (name === "front") this.drawFront(true);
      else { const im = imgs[name], v = L.views[name]; g.drawImage(im, CX - v.w / 2, GROUND - v.h, v.w, v.h); }
      g.restore();
    }
    drawFront(noPose) {
      const g = this.g, p = noPose ? { bob: 0, rot: 0, sx: 1, sy: 1, armL: 0, armR: 0, liftL: 0, liftR: 0, pitchL: 0, pitchR: 0, mouth: 1, eye: 1, cap: 0, tas: 0, rib: 0 } : this.p;
      const P = L.parts;
      const part = (name) => { const q = P[name]; g.drawImage(imgs[name], q.x, q.y, q.w, q.h); };
      // Transformação do corpo (livro e tudo preso nele): sobe/desce, inclina e amassa no pé do livro.
      const bodyM = new DOMMatrix().translate(BOOK_BASE[0], BOOK_BASE[1] + p.bob).rotate(p.rot * 180 / Math.PI).scale(p.sx, p.sy).translate(-BOOK_BASE[0], -BOOK_BASE[1]);
      const shoeM = (side) => {
        const q = P["shoe" + side], lift = p["lift" + side], pitch = p["pitch" + side];
        return new DOMMatrix().translate(q.pivot[0], q.pivot[1] - lift).rotate(pitch * 180 / Math.PI).translate(-q.pivot[0], -q.pivot[1]);
      };
      const withM = (M, fn) => { g.save(); g.transform(M.a, M.b, M.c, M.d, M.e, M.f); fn(); g.restore(); };
      const rotAt = (pv, ang, fn) => { g.save(); g.translate(pv[0], pv[1]); g.rotate(ang); g.translate(-pv[0], -pv[1]); fn(); g.restore(); };
      // Braço de trás (direita da tela) atrás da capa.
      withM(bodyM, () => rotAt(P.armR.pivot, p.armR, () => part("armR")));
      // Pernas: do quadril (que vai com o corpo) até o punho do tênis (que vai com o pé).
      for (const side of ["L", "R"]) {
        const leg = P["leg" + side], hip0 = leg.pivot, cuff0 = CUFF[side];
        const hip1 = bodyM.transformPoint(new DOMPoint(hip0[0], hip0[1])), cuff1 = shoeM(side).transformPoint(new DOMPoint(cuff0[0], cuff0[1]));
        const a0 = Math.atan2(cuff0[1] - hip0[1], cuff0[0] - hip0[0]), a1 = Math.atan2(cuff1.y - hip1.y, cuff1.x - hip1.x);
        const len0 = Math.hypot(cuff0[0] - hip0[0], cuff0[1] - hip0[1]), len1 = Math.hypot(cuff1.x - hip1.x, cuff1.y - hip1.y);
        g.save(); g.translate(hip1.x, hip1.y); g.rotate(a1); g.scale(len1 / len0, 1); g.rotate(-a0); g.translate(-hip0[0], -hip0[1]); part("leg" + side); g.restore();
      }
      for (const side of ["L", "R"]) withM(shoeM(side), () => part("shoe" + side));
      withM(bodyM, () => {
        part("body");
        // Olhos e boca por cima do livro (mesmos pixels, espremidos/abertos no pivô).
        for (const e of ["eyeL", "eyeR"]) { const q = P[e]; g.save(); g.translate(q.pivot[0], q.pivot[1]); g.scale(1, p.eye); g.translate(-q.pivot[0], -q.pivot[1]); part(e); g.restore(); }
        { const q = P.mouth; g.save(); g.translate(q.pivot[0], q.pivot[1]); g.scale(1 + (p.mouth - 1) * 0.15, p.mouth); g.translate(-q.pivot[0], -q.pivot[1]); part("mouth"); g.restore(); }
        rotAt(P.ribbon.pivot, p.rib, () => part("ribbon"));
        rotAt(P.armL.pivot, p.armL, () => part("armL"));
        rotAt(P.cap.pivot, p.cap, () => { part("cap"); rotAt(P.tassel.pivot, p.tas, () => part("tassel")); });
      });
    }
  }

  // Um laço só para todos os mascotes da página.
  const live = new Set();
  let raf = 0, last = 0;
  const MAX_DT = location.search.includes("shot") ? 0.2 : 1 / 30;
  function loop(now) {
    const dt = Math.max(1 / 240, Math.min(MAX_DT, last ? (now - last) / 1000 : 1 / 60)); last = now;
    for (const el of live) if (el._visible && el._m) { el._m.update(dt); el._m.draw(); }
    raf = live.size ? requestAnimationFrame(loop) : 0; if (!raf) last = 0;
  }

  customElements.define("estudario-mascote", class extends HTMLElement {
    static get observedAttributes() { return ["mood"]; }
    connectedCallback() {
      if (this._m) return;
      const size = +(this.getAttribute("size") || 0);
      if (size) { this.style.width = this.style.width || size + "px"; this.style.height = this.style.height || size + "px"; }
      if (!this.style.display) this.style.display = "inline-block";
      this.style.position = this.style.position || "relative";
      const c = document.createElement("canvas");
      c.style.cssText = "position:absolute;left:-15%;top:-15%;width:130%;height:130%;pointer-events:none";
      this.appendChild(c);
      this._c = c; this._m = new Mascote(c); this._m.mood = this.getAttribute("mood") || "idle";
      const fit = () => { const dpr = Math.min(window.devicePixelRatio || 1, 2), w = this.clientWidth * 1.3, h = this.clientHeight * 1.3; c.width = Math.max(1, Math.round(w * dpr)); c.height = Math.max(1, Math.round(h * dpr)); };
      this._ro = new ResizeObserver(fit); this._ro.observe(this); fit();
      this._io = new IntersectionObserver((es) => { for (const e of es) this._visible = e.isIntersecting; }); this._io.observe(this);
      this._visible = true;
      let down = null;
      this.addEventListener("pointerdown", (e) => { down = { x: e.clientX, y: e.clientY, moved: 0 }; if (this.hasAttribute("spin")) { this._m.dragging = true; this.setPointerCapture(e.pointerId); } });
      this.addEventListener("pointermove", (e) => { if (!down || !this._m.dragging) return; const dx = e.clientX - down.x; down.moved += Math.abs(dx); down.x = e.clientX; this._m.yaw += dx * 0.6; this._m.yawV = dx * 30; });
      this.addEventListener("pointerup", () => { if (down && down.moved < 6) { this._m.poke(); this.dispatchEvent(new CustomEvent("folha-poke", { bubbles: true })); } down = null; this._m.dragging = false; });
      load().then(() => { live.add(this); if (!raf) raf = requestAnimationFrame(loop); });
    }
    disconnectedCallback() { live.delete(this); this._ro && this._ro.disconnect(); this._io && this._io.disconnect(); }
    attributeChangedCallback(n, _, v) { if (this._m && n === "mood") this._m.mood = v || "idle"; }
  });
  document.addEventListener("visibilitychange", () => { for (const el of live) el._visible = !document.hidden; });
})();
