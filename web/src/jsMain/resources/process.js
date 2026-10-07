// A espera "trabalhando" do Estudário no site: tradução de EstudarioProcessScene em
// app/.../ui/components/ProcessLoader.kt. O Folha (folha-live.js, lendo e escrevendo) fica no meio;
// atrás dele, aurora neon, galáxia girando, feixes de luz e a metade de trás das órbitas; na frente,
// a outra metade das órbitas, letras sugadas em espiral, raios e o estouro de cada chegada.
//
// Uso: <estudario-process size="300"></estudario-process>. Com menos movimento pedido no sistema, a
// cena fica parada (três folhas em volta do Folha).
(function () {
  if (!window.customElements || customElements.get("estudario-process")) return;

  const FLIGHT = 4.8, ORBIT_PART = 0.7, STARS = 90;
  const NEON = ["#8B6CFF", "#22E3C4", "#FF4FD8"];
  const GLYPHS = ["A", "§", "?", "✓", "%", "Σ", "B", "¶"];
  const SHEETS = ["PDF", "LIST", "QUESTION", "HIGHLIGHT", "OUTLINE"];
  const PI = Math.PI, sin = Math.sin, cos = Math.cos;
  const reduce = window.matchMedia ? window.matchMedia("(prefers-reduced-motion: reduce)") : { matches: false };

  function rgb(c) {
    c = c.trim();
    if (c.startsWith("#")) {
      const v = c.length === 4 ? c.slice(1).split("").map((x) => x + x).join("") : c.slice(1);
      const n = parseInt(v, 16);
      return [(n >> 16) & 255, (n >> 8) & 255, n & 255];
    }
    const m = c.match(/[\d.]+/g);
    return m ? m.slice(0, 3).map(Number) : [79, 70, 229];
  }
  const rgba = (c, a) => `rgba(${c[0]},${c[1]},${c[2]},${Math.max(0, Math.min(1, a))})`;
  const mix = (a, b, f) => [0, 1, 2].map((i) => Math.round(a[i] + (b[i] - a[i]) * f));
  const NEON_RGB = NEON.map(rgb);

  function hash(v) {
    let x = Math.imul(v, 0x45d9f3b);
    x = Math.imul(x ^ (x >>> 16), 0x45d9f3b);
    return (x ^ (x >>> 16)) & 0x7fffffff;
  }

  function orbiter(i, count, t, cx, cy, radius) {
    const p = (((t % FLIGHT) + FLIGHT) % FLIGHT) / FLIGHT;
    const ringAngle = i % 2 === 0 ? -0.5 : 0.42;
    const flatten = 0.34;
    const dive = Math.max(0, Math.min(1, (p - ORBIT_PART) / (1 - ORBIT_PART)));
    const d = dive * dive * (3 - 2 * dive);
    const theta = (i * 2 * PI / count) + p * 2 * PI * 1.35;
    const r = radius * 0.86 * (1 - 0.97 * d);
    const x0 = cos(theta) * r;
    const y0 = sin(theta) * r * (flatten + (1 - flatten) * d * 0.3);
    const x = x0 * cos(ringAngle) - y0 * sin(ringAngle);
    const y = x0 * sin(ringAngle) + y0 * cos(ringAngle);
    const z = sin(theta) * (1 - d) + d;
    const scale = (0.72 + 0.28 * (z + 1) / 2) * (1 - 0.8 * Math.pow(d, 1.4));
    const alpha = (p < 0.07 ? p / 0.07 : dive > 0.82 ? Math.max(0, (1 - dive) / 0.18) : 1) * (0.55 + 0.45 * (z + 1) / 2);
    return { x: cx + x, y: cy + y, z, scale, alpha, tilt: sin(theta) * 18 + d * 220, dive };
  }

  function drawSheet(g, kind, x, y, h, tiltDeg, alpha, c) {
    if (alpha <= 0.01 || h <= 1) return;
    const w = h * 0.78;
    g.save();
    g.translate(x, y);
    g.rotate(tiltDeg * PI / 180);
    g.translate(-w / 2, -h / 2);
    const k = h * 0.09, fold = h * 0.2;
    g.fillStyle = `rgba(0,0,0,${0.12 * alpha})`;
    g.beginPath(); g.roundRect ? g.roundRect(h * 0.03, h * 0.05, w, h, k) : g.rect(h * 0.03, h * 0.05, w, h); g.fill();
    const paper = new Path2D();
    paper.moveTo(0, k); paper.quadraticCurveTo(0, 0, k, 0); paper.lineTo(w - fold, 0); paper.lineTo(w, fold);
    paper.lineTo(w, h - k); paper.quadraticCurveTo(w, h, w - k, h); paper.lineTo(k, h); paper.quadraticCurveTo(0, h, 0, h - k); paper.closePath();
    g.fillStyle = rgba(c.paper, alpha); g.fill(paper);
    g.strokeStyle = rgba(c.paperEdge, c.paperEdgeA * alpha); g.lineWidth = h * 0.012; g.stroke(paper);
    g.fillStyle = rgba(c.line, 0.55 * alpha);
    g.beginPath(); g.moveTo(w - fold, 0); g.lineTo(w - fold, fold); g.lineTo(w, fold); g.closePath(); g.fill();
    const lw = h * 0.045;
    const bar = (yy, from, to, col = c.line, a = 1) => {
      g.strokeStyle = rgba(col, a * alpha); g.lineWidth = lw; g.lineCap = "round";
      g.beginPath(); g.moveTo(w * from, h * yy); g.lineTo(w * to, h * yy); g.stroke();
    };
    const dot = (cx, cy, r, col, a = 1, stroke) => {
      g.beginPath(); g.arc(cx, cy, r, 0, 2 * PI);
      if (stroke) { g.strokeStyle = rgba(col, a * alpha); g.lineWidth = stroke; g.stroke(); } else { g.fillStyle = rgba(col, a * alpha); g.fill(); }
    };
    switch (kind) {
      case "PDF": {
        g.fillStyle = rgba(c.pdf, alpha);
        g.beginPath(); g.roundRect ? g.roundRect(w * 0.12, h * 0.14, w * 0.44, h * 0.15, h * 0.03) : g.rect(w * 0.12, h * 0.14, w * 0.44, h * 0.15); g.fill();
        g.font = `900 ${h * 0.1}px Nunito, system-ui, sans-serif`; g.fillStyle = `rgba(255,255,255,${alpha})`; g.textBaseline = "middle";
        g.fillText("PDF", w * 0.16, h * 0.217);
        bar(0.44, 0.14, 0.86); bar(0.56, 0.14, 0.78); bar(0.68, 0.14, 0.86); bar(0.8, 0.14, 0.6);
        break;
      }
      case "LIST":
        bar(0.2, 0.14, 0.6, c.accent);
        for (const yy of [0.38, 0.52, 0.66, 0.8]) { dot(w * 0.18, h * yy, h * 0.03, c.accent); bar(yy, 0.3, yy > 0.7 ? 0.66 : 0.84); }
        break;
      case "QUESTION":
        bar(0.18, 0.14, 0.86); bar(0.28, 0.14, 0.6);
        [0.46, 0.6, 0.74].forEach((yy, i) => {
          if (i === 1) dot(w * 0.2, h * yy, h * 0.045, c.success); else dot(w * 0.2, h * yy, h * 0.042, c.line, 1, h * 0.016);
          bar(yy, 0.34, i === 2 ? 0.62 : 0.82);
        });
        break;
      case "HIGHLIGHT":
        g.fillStyle = rgba(c.attention, 0.55 * alpha); g.fillRect(w * 0.1, h * 0.36, w * 0.72, h * 0.1);
        bar(0.2, 0.14, 0.8); bar(0.41, 0.14, 0.78); bar(0.6, 0.14, 0.86); bar(0.74, 0.14, 0.7);
        break;
      default:
        bar(0.2, 0.14, 0.7, c.accent); bar(0.36, 0.14, 0.62); bar(0.5, 0.3, 0.84); bar(0.64, 0.3, 0.76); bar(0.8, 0.14, 0.58);
        g.strokeStyle = rgba(c.line, 0.6 * alpha); g.lineWidth = h * 0.014;
        g.beginPath(); g.moveTo(w * 0.2, h * 0.42); g.lineTo(w * 0.2, h * 0.66); g.stroke();
    }
    g.restore();
  }

  function orbitLayer(g, t, c, count, front, cx, cy, radius) {
    const items = [];
    for (let i = 0; i < count; i++) {
      const ti = t + i * FLIGHT / count;
      const o = orbiter(i, count, ti, cx, cy, radius);
      if ((o.z >= 0) === front) items.push({ i, cycle: Math.floor(ti / FLIGHT), o, ti });
    }
    items.sort((a, b) => a.o.z - b.o.z);
    for (const { i, cycle, o, ti } of items) {
      for (let k = 0; k < 10; k++) {
        const past = orbiter(i, count, ti - 0.035 * (k + 1), cx, cy, radius);
        if ((past.z >= 0) !== front || past.alpha <= 0.02) continue;
        const f = 1 - k / 10;
        g.beginPath(); g.arc(past.x, past.y, (4.2 * past.scale * f + 0.6) * c.dp, 0, 2 * PI);
        g.fillStyle = rgba(i % 2 === 0 ? c.accent : c.success, past.alpha * 0.32 * f); g.fill();
      }
      const kind = SHEETS[hash(cycle * 31 + i * 7) % SHEETS.length];
      drawSheet(g, kind, o.x, o.y, radius * 0.3 * o.scale, o.tilt, o.alpha, c);
    }
  }

  function drawBack(g, t, c, count, still, W, H) {
    const cx = W / 2, cy = H / 2, radius = Math.min(W, H) / 2;
    g.globalCompositeOperation = "source-over";
    NEON_RGB.forEach((base, k) => {
      const a = t * (0.35 + k * 0.08) + k * 2.1;
      const x = cx + cos(a) * radius * 0.3, y = cy + sin(a * 1.3) * radius * 0.24;
      const col = mix(base, NEON_RGB[(k + 1) % 3], (sin(t * 0.4 + k) + 1) / 2);
      const gr = g.createRadialGradient(x, y, 0, x, y, radius * 0.8);
      gr.addColorStop(0, rgba(col, 0.38)); gr.addColorStop(0.5, rgba(col, 0.1)); gr.addColorStop(1, rgba(col, 0));
      g.fillStyle = gr; g.beginPath(); g.arc(x, y, radius * 0.8, 0, 2 * PI); g.fill();
    });
    if (!still) {
      g.globalCompositeOperation = "lighter";
      for (let k = 0; k < STARS; k++) {
        const arm = k % 2, life = 5.5 + (k % 7) * 0.4;
        const q = ((t + k * 0.173) % life) / life;
        const r = radius * (1.05 - 0.98 * q);
        const a = arm * PI + 3.2 * (1 - q) - t * 0.9 + (k % 5) * 0.07;
        const x = cx + cos(a) * r, y = cy + sin(a) * r * 0.8;
        const tw = 0.55 + 0.45 * sin(t * 6 + k);
        const fade = (q < 0.1 ? q / 0.1 : 1) * (0.4 + 0.6 * q);
        g.fillStyle = rgba(NEON_RGB[k % 3], 0.22 * fade * tw); g.beginPath(); g.arc(x, y, (5.5 * (1 - q) + 2) * c.dp, 0, 2 * PI); g.fill();
        g.fillStyle = `rgba(255,255,255,${0.75 * fade * tw})`; g.beginPath(); g.arc(x, y, (1.5 * (1 - q) + 0.6) * c.dp, 0, 2 * PI); g.fill();
      }
      g.globalCompositeOperation = "source-over";
    }
    // Feixes de luz girando como um farol.
    g.save(); g.translate(cx, cy); g.rotate(t * 9 * PI / 180);
    for (let k = 0; k < 12; k++) {
      const a = k * 2 * PI / 12, spread = 0.11;
      const reach = radius * (0.9 + 0.1 * sin(t * 1.7 + k));
      const gr = g.createRadialGradient(0, 0, 0, 0, 0, reach);
      gr.addColorStop(0, rgba(c.glow, k % 2 === 0 ? 0.13 : 0.07)); gr.addColorStop(1, rgba(c.glow, 0));
      g.fillStyle = gr; g.beginPath(); g.moveTo(0, 0);
      g.lineTo(cos(a - spread) * reach, sin(a - spread) * reach); g.lineTo(cos(a + spread) * reach, sin(a + spread) * reach); g.closePath(); g.fill();
    }
    g.restore();
    if (still) {
      [["PDF", -2.3], ["QUESTION", -0.5], ["LIST", 2.4]].forEach(([kind, a]) => {
        drawSheet(g, kind, cx + cos(a) * radius * 0.74, cy + sin(a) * radius * 0.74, radius * 0.25, a * 8, 1, c);
      });
      return;
    }
    orbitLayer(g, t, c, count, false, cx, cy, radius);
  }

  function drawFront(g, t, c, count, W, H) {
    const cx = W / 2, cy = H / 2, radius = Math.min(W, H) / 2;
    const glyphColors = [c.accent, c.success, c.attention];
    g.textAlign = "center"; g.textBaseline = "middle";
    for (let k = 0; k < 14; k++) {
      const period = 2.2 + (k % 5) * 0.37;
      const q = ((t + k * 0.61) % period) / period, e = q * q;
      const a = k * 2.39996 + e * 4.2, r = radius * (1.02 - 0.92 * e);
      const x = cx + cos(a) * r, y = cy + sin(a) * r * 0.86;
      const fade = (q < 0.15 ? q / 0.15 : 1) * Math.max(0, 1 - e);
      const col = glyphColors[k % 3];
      for (let j = 0; j < 3; j++) {
        const eb = Math.max(0, q - 0.03 * (j + 1)) ** 2;
        const ab = k * 2.39996 + eb * 4.2, rb = radius * (1.02 - 0.92 * eb);
        g.fillStyle = rgba(col, fade * (0.4 - j * 0.12));
        g.beginPath(); g.arc(cx + cos(ab) * rb, cy + sin(ab) * rb * 0.86, (2.2 - j * 0.5) * c.dp, 0, 2 * PI); g.fill();
      }
      const s = 0.55 + 0.5 * (1 - e);
      g.font = `900 ${18 * s * c.dp}px Nunito, system-ui, sans-serif`;
      g.fillStyle = rgba(col, fade * 0.9);
      g.fillText(GLYPHS[k % GLYPHS.length], x, y);
    }
    orbitLayer(g, t, c, count, true, cx, cy, radius);

    // Raios: de tempos em tempos uma folha da frente solta um relâmpago até o Folha.
    for (let i = 0; i < count; i++) {
      const ti = t + i * FLIGHT / count;
      const o = orbiter(i, count, ti, cx, cy, radius);
      const win = (ti * 1.3 + i * 0.37) % 2.2;
      if (o.z < 0.15 || o.dive > 0.1 || win > 0.16) continue;
      const strike = win / 0.16, seed = hash(Math.floor(ti * 1.3 / 2.2) * 13 + i);
      const bolt = new Path2D(); bolt.moveTo(o.x, o.y);
      const dx = cy - o.y, dy = o.x - cx, len = Math.max(1, Math.hypot(dx, dy));
      for (let s = 1; s < 7; s++) {
        const f = s / 7, bx = o.x + (cx - o.x) * f, by = o.y + (cy - o.y) * f;
        const nn = ((hash(seed + s * 97) % 200) / 100 - 1) * radius * 0.09 * (1 - f * 0.5);
        bolt.lineTo(bx + dx / len * nn, by + dy / len * nn);
      }
      bolt.lineTo(cx, cy);
      const fade = 1 - strike, col = NEON_RGB[(i + 1) % 3];
      g.lineCap = "round"; g.lineJoin = "round";
      g.globalCompositeOperation = "lighter";
      g.strokeStyle = rgba(col, 0.35 * fade); g.lineWidth = 7 * c.dp; g.stroke(bolt);
      g.globalCompositeOperation = "source-over";
      g.strokeStyle = `rgba(255,255,255,${0.95 * fade})`; g.lineWidth = 1.6 * c.dp; g.stroke(bolt);
      g.globalCompositeOperation = "lighter";
      g.fillStyle = rgba(col, 0.5 * fade); g.beginPath(); g.arc(cx, cy, radius * 0.12 * (1 + strike), 0, 2 * PI); g.fill();
      g.globalCompositeOperation = "source-over";
    }
    // Estouro de cada chegada: raios curtos saindo do Folha e faíscas espirrando.
    for (let i = 0; i < count; i++) {
      const since = (t + i * FLIGHT / count) % FLIGHT;
      if (since > 0.5) continue;
      const b = since / 0.5, fade = Math.pow(1 - b, 1.5);
      for (let k = 0; k < 10; k++) {
        const a = k * 2 * PI / 10 + i * 0.7, r0 = radius * (0.2 + 0.35 * b), r1 = r0 + radius * 0.16 * (1 - b);
        g.strokeStyle = rgba(k % 2 === 0 ? c.attention : c.success, fade); g.lineWidth = (3 * (1 - b) + 1) * c.dp; g.lineCap = "round";
        g.beginPath(); g.moveTo(cx + cos(a) * r0, cy + sin(a) * r0); g.lineTo(cx + cos(a) * r1, cy + sin(a) * r1); g.stroke();
      }
      for (let k = 0; k < 8; k++) {
        const a = k * 0.785 + i * 1.3 + 0.39, r = radius * (0.22 + 0.62 * b);
        g.fillStyle = rgba(c.spark, fade); g.beginPath();
        g.arc(cx + cos(a) * r, cy + sin(a) * r - radius * 0.2 * b * b, (2.6 * (1 - b) + 0.8) * c.dp, 0, 2 * PI); g.fill();
      }
    }
  }

  const live = new Set();
  let raf = 0;
  function tick(now) { raf = 0; live.forEach((el) => el._frame(now)); if (live.size) raf = requestAnimationFrame(tick); }
  function wake() { if (!raf && live.size) raf = requestAnimationFrame(tick); }

  class EstudarioProcess extends HTMLElement {
    static get observedAttributes() { return ["size"]; }
    connectedCallback() {
      if (!this._built) {
        this._built = true;
        this.setAttribute("role", "img");
        if (!this.hasAttribute("aria-label")) this.setAttribute("aria-label", "Processando");
        this.style.position = "relative";
        this.style.display = "block";
        this._back = document.createElement("canvas");
        this._front = document.createElement("canvas");
        for (const cv of [this._back, this._front]) Object.assign(cv.style, { position: "absolute", inset: "0", width: "100%", height: "100%", pointerEvents: "none" });
        this._folha = document.createElement("estudario-folha");
        this._folha.setAttribute("mood", "thinking");
        this._folha.className = "process-folha";
        this.append(this._back, this._folha, this._front);
        this._start = performance.now();
      }
      this._size();
      if ("IntersectionObserver" in window) {
        this._io = new IntersectionObserver((e) => { this._visible = e.some((x) => x.isIntersecting); if (this._visible) { live.add(this); wake(); } else live.delete(this); });
        this._io.observe(this);
      }
      this._visible = true;
      live.add(this); wake();
    }
    disconnectedCallback() { live.delete(this); if (this._io) this._io.disconnect(); }
    attributeChangedCallback() { this._size(); }
    _size() {
      if (!this._back) return;
      const size = parseInt(this.getAttribute("size") || "300", 10);
      this.style.width = size + "px"; this.style.height = size + "px";
      const dpr = Math.min(window.devicePixelRatio || 1, 2);
      for (const cv of [this._back, this._front]) { cv.width = Math.round(size * dpr); cv.height = Math.round(size * dpr); }
      this._px = size * dpr; this._dp = dpr * size / 320;
      const fs = Math.round(size * 0.56);
      this._folha.setAttribute("size", String(fs));
      Object.assign(this._folha.style, { position: "absolute", left: `${(size - fs) / 2}px`, top: `${(size - fs) / 2}px` });
      this._colors = null;
    }
    _palette() {
      const st = getComputedStyle(this);
      const v = (name, fb) => rgb(st.getPropertyValue(name) || fb);
      const dark = (() => { const bg = v("--bg", "#FAF9F5"); return (bg[0] + bg[1] + bg[2]) / 3 < 128; })();
      const primary = v("--primary", "#4F46E5");
      return {
        glow: primary, accent: primary, success: v("--green", "#0B7A56"), attention: rgb("#F2B33D"),
        paper: dark ? rgb("#EDEBF7") : [255, 255, 255], paperEdge: dark ? [0, 0, 0] : rgb("#1B1464"), paperEdgeA: dark ? 0.2 : 0.12,
        line: rgb("#B8B4CE"), pdf: rgb("#E5484D"), spark: dark ? rgb("#FFF4D6") : primary, dp: this._dp,
      };
    }
    _frame(now) {
      if (!this._visible) return;
      if (!this._colors) this._colors = this._palette();
      const c = this._colors;
      const still = reduce.matches;
      const elapsed = (now - this._start) / 1000;
      const t = still ? 1.35 : 1.35 + elapsed;
      const W = this._px, H = this._px;
      const b = this._back.getContext("2d"), f = this._front.getContext("2d");
      b.setTransform(1, 0, 0, 1, 0, 0); b.clearRect(0, 0, W, H);
      f.setTransform(1, 0, 0, 1, 0, 0); f.clearRect(0, 0, W, H);
      drawBack(b, t, c, 5, still, W, H);
      if (!still) drawFront(f, t, c, 5, W, H);
      // O livro abre ao entrar: a capa gira de lado até ficar de frente, e depois respira.
      const o = still ? 1 : Math.min(1, elapsed / 0.9), e = 1 - Math.pow(1 - o, 3);
      const breath = still ? 1 : 1 + 0.025 * sin(t * 2.2);
      const sc = (0.82 + 0.18 * e) * breath;
      this._folha.style.transform = `perspective(600px) rotateY(${(1 - e) * 88}deg) scale(${sc})`;
      this._folha.style.opacity = String(0.2 + 0.8 * e);
      if (still) live.delete(this);
    }
  }
  customElements.define("estudario-process", EstudarioProcess);
})();
