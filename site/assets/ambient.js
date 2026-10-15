// O fundo vivo do Estudário (página de apresentação e tela de entrada do site).
//
// A mesa de estudo: o papel pontilhado do caderno, e o cursor vira um marca-texto. Os pontos perto
// dele crescem e ganham a cor da marca, e o caminho do mouse deixa uma passada amarela que seca e
// some. Um clique no papel (fora de botões e links) carimba um adesivo da turma, que pula e sobe.
// Só com mouse (no toque fica o papel parado) e nada se mexe para quem pede menos movimento.
//
// Uso: estudarioAmbient(document.body)  ou  estudarioAmbient(elemento, { stamps: false })
(function () {
  const reduce = window.matchMedia && matchMedia("(prefers-reduced-motion: reduce)").matches;
  const fine = window.matchMedia && matchMedia("(pointer: fine)").matches;

  function css(el, name, fallback) {
    const v = getComputedStyle(el).getPropertyValue(name).trim();
    return v || fallback;
  }
  function rgb(color) {
    const c = document.createElement("canvas").getContext("2d");
    c.fillStyle = color;
    const h = c.fillStyle;
    if (h.startsWith("#")) { const n = parseInt(h.slice(1), 16); return [(n >> 16) & 255, (n >> 8) & 255, n & 255]; }
    const m = h.match(/[\d.]+/g) || [0, 0, 0];
    return [+m[0], +m[1], +m[2], m[3] == null ? 1 : +m[3]];
  }

  window.estudarioAmbient = function (host, opts) {
    opts = opts || {};
    const stampsOn = opts.stamps !== false;
    const canvas = document.createElement("canvas");
    canvas.className = "ambient-canvas";
    canvas.setAttribute("aria-hidden", "true");
    canvas.style.cssText = "position:fixed;inset:0;width:100vw;height:100vh;pointer-events:none;z-index:0";
    host.prepend(canvas);
    const g = canvas.getContext("2d");
    let W = 0, H = 0, dpr = 1;
    const GAP = 26;

    let dot, brand, mark, dark, dotA = .55;
    function readColors() {
      const root = document.documentElement;
      dot = rgb(css(root, "--dot", "#D9D0BC"));
      // Ponto com transparência (o --dot do site é translúcido) vira um ponto chapado mais forte.
      dotA = dot[3] >= 1 ? .55 : Math.min(.6, dot[3] * 4);
      brand = rgb(css(root, "--primary", "#5B4BF5"));
      mark = rgb(css(root, "--hl", "#FFD23F"));
      dark = matchMedia("(prefers-color-scheme: dark)").matches && root.dataset.theme !== "light" || root.dataset.theme === "dark";
    }

    function resize() {
      dpr = Math.min(window.devicePixelRatio || 1, 2);
      W = innerWidth; H = innerHeight;
      canvas.width = Math.round(W * dpr); canvas.height = Math.round(H * dpr);
      g.setTransform(dpr, 0, 0, dpr, 0, 0);
      readColors();
      kick();
    }

    // Cursor suavizado: o efeito segue o mouse com um pouco de atraso, como tinta.
    const mouse = { x: -999, y: -999, sx: -999, sy: -999, on: false, last: 0 };
    const trail = [];
    const stamps = [];
    const LIFE = 760;

    // Adesivos para carimbar (imagens da turma, desenhadas uma vez).
    const stickerImgs = [];
    if (stampsOn && window.estudarioGlyphUri && window.estudarioTurma) {
      const pick = ["Star", "Heart", "Fire", "Bolt", "Trophy", "Check", "Bulb", "Medal", "Gem", "Books", "Pencil", "Target", "Cap"];
      pick.forEach((n) => { const u = window.estudarioGlyphUri(n); if (u) { const i = new Image(); i.src = u; stickerImgs.push(i); } });
    }

    function drawDots(now) {
      const R = 150;
      const mx = mouse.sx, my = mouse.sy;
      const fade = mouse.on ? 1 : Math.max(0, 1 - (now - mouse.last) / 600);
      const ox = (W % GAP) / 2, oy = (H % GAP) / 2;
      for (let y = oy; y < H + GAP; y += GAP) {
        for (let x = ox; x < W + GAP; x += GAP) {
          let px = x, py = y, r = 1.15, k = 0;
          const dx = x - mx, dy = y - my, d = Math.hypot(dx, dy);
          if (d < R && fade > 0) {
            k = (1 - d / R) * fade;
            k = k * k * (3 - 2 * k);
            // Os pontos se afastam um pouco do cursor, como limalha perto de um ímã.
            const push = k * 7 / (d || 1);
            px += dx * push; py += dy * push;
            r += k * 1.9;
          }
          const c = k > 0 ? [0, 1, 2].map((i) => Math.round(dot[i] + (brand[i] - dot[i]) * k)) : dot;
          g.fillStyle = `rgba(${c[0]},${c[1]},${c[2]},${dotA + k * (1 - dotA)})`;
          g.beginPath(); g.arc(px, py, r, 0, 6.283); g.fill();
        }
      }
    }

    function drawTrail(now) {
      while (trail.length && now - trail[0].t > LIFE) trail.shift();
      if (trail.length < 2) return;
      g.save();
      g.globalCompositeOperation = dark ? "screen" : "multiply";
      g.lineCap = "round"; g.lineJoin = "round";
      for (let i = 1; i < trail.length; i++) {
        const a = trail[i - 1], b = trail[i];
        const age = (now - b.t) / LIFE;
        const alpha = (1 - age) * (dark ? 0.32 : 0.55);
        if (alpha <= 0) continue;
        g.strokeStyle = `rgba(${mark[0]},${mark[1]},${mark[2]},${alpha})`;
        // A ponta do marca-texto é chanfrada: mais larga na passada nova, afina ao secar.
        g.lineWidth = 16 * (1 - age * 0.55);
        g.beginPath();
        const mx = (a.x + b.x) / 2, my = (a.y + b.y) / 2;
        g.moveTo(a.x, a.y); g.quadraticCurveTo(a.x, a.y, mx, my); g.lineTo(b.x, b.y);
        g.stroke();
      }
      g.restore();
    }

    function drawStamps(now) {
      for (let i = stamps.length - 1; i >= 0; i--) {
        const s = stamps[i], t = (now - s.t) / 1300;
        if (t >= 1) { stamps.splice(i, 1); continue; }
        // Carimbo: entra grande, assenta com um quique e sobe sumindo.
        const pop = t < 0.18 ? 1.5 - (t / 0.18) * 0.6 : t < 0.32 ? 0.9 + ((t - 0.18) / 0.14) * 0.12 : 1.02;
        const rise = t > 0.45 ? (t - 0.45) * 70 : 0;
        const alpha = t > 0.55 ? 1 - (t - 0.55) / 0.45 : Math.min(1, t * 8);
        const size = 54 * pop;
        g.save();
        g.globalAlpha = alpha;
        g.translate(s.x, s.y - rise);
        g.rotate(s.rot + Math.sin(t * 9) * 0.06 * (1 - t));
        if (s.img.complete) g.drawImage(s.img, -size / 2, -size / 2, size, size);
        g.restore();
        // Faíscas em volta, na hora do carimbo.
        if (t < 0.5) {
          g.save();
          g.globalAlpha = (1 - t / 0.5) * 0.9;
          g.strokeStyle = `rgb(${brand[0]},${brand[1]},${brand[2]})`;
          g.lineWidth = 2.5; g.lineCap = "round";
          for (let k = 0; k < 6; k++) {
            const ang = s.rot + k * 1.047, r1 = 34 + t * 40, r2 = r1 + 9 * (1 - t);
            g.beginPath(); g.moveTo(s.x + Math.cos(ang) * r1, s.y + Math.sin(ang) * r1); g.lineTo(s.x + Math.cos(ang) * r2, s.y + Math.sin(ang) * r2); g.stroke();
          }
          g.restore();
        }
      }
    }

    let raf = 0;
    function frame(now) {
      raf = 0;
      mouse.sx += (mouse.x - mouse.sx) * 0.22;
      mouse.sy += (mouse.y - mouse.sy) * 0.22;
      g.clearRect(0, 0, W, H);
      drawDots(now);
      if (!reduce) { drawTrail(now); drawStamps(now); }
      const busy = mouse.on && now - mouse.last < 900 || trail.length > 1 || stamps.length || now - mouse.last < 700;
      if (busy) kick();
    }
    function kick() { if (!raf) raf = requestAnimationFrame(frame); }

    // Os ouvintes ficam guardados para a tela poder desligar o fundo (destroy) ao sair.
    const offs = [];
    const on = (target, type, fn, opt) => { target.addEventListener(type, fn, opt); offs.push(() => target.removeEventListener(type, fn, opt)); };
    if (fine && !reduce) {
      on(window, "pointermove", (e) => {
        if (e.pointerType !== "mouse") return;
        const now = performance.now();
        if (!mouse.on) { mouse.sx = e.clientX; mouse.sy = e.clientY; }
        mouse.x = e.clientX; mouse.y = e.clientY; mouse.on = true; mouse.last = now;
        const prev = trail[trail.length - 1];
        if (!prev || Math.hypot(prev.x - e.clientX, prev.y - e.clientY) > 3) trail.push({ x: e.clientX, y: e.clientY, t: now });
        if (trail.length > 90) trail.shift();
        kick();
      }, { passive: true });
      on(document, "pointerleave", () => { mouse.on = false; kick(); });
      if (stampsOn) on(window, "click", (e) => {
        if (!stickerImgs.length || e.button !== 0) return;
        // Só no papel: clique em botão, link, campo ou texto selecionável é do conteúdo.
        if (e.target.closest("a,button,input,textarea,select,label,summary,details,iframe,[role=button],.no-stamp,p,h1,h2,h3,li,dd,dt")) return;
        if (String(getSelection && getSelection()).length) return;
        stamps.push({ x: e.clientX, y: e.clientY, t: performance.now(), rot: (Math.random() - 0.5) * 0.5, img: stickerImgs[(Math.random() * stickerImgs.length) | 0] });
        if (stamps.length > 12) stamps.shift();
        kick();
      });
    }
    on(window, "resize", resize);
    if (window.matchMedia) on(matchMedia("(prefers-color-scheme: dark)"), "change", () => { readColors(); kick(); });
    resize();
    return {
      refresh() { readColors(); kick(); },
      destroy() { offs.forEach((f) => f()); if (raf) cancelAnimationFrame(raf); raf = 0; canvas.remove(); },
    };
  };
})();
