// A chama viva do Estudário: fogo de verdade, desenhado quadro a quadro num <canvas>, no traço da turma
// do Folha (contorno de caneta, recorte branco) e com o rosto do mascote (olhos que piscam e olham
// em volta, sorriso, bochechas). Usada na comemoração da sequência.
//
// estudarioFlame(canvas, { delay, grow }) começa a cena: a chama nasce como uma brasinha girando,
// cresce de forma contínua enquanto o giro desacelera e fica tremulando. Devolve stop().
// O app desenha a mesma chama em ui/brand/LiveFlame.kt (mesma conta, mesmos números).
(function () {
  "use strict";
  const INK = "#26234A";

  // Contorno de uma língua de fogo: gota alongada cujo topo balança com o tempo.
  function tongue(cx, base, w, h, t, phase, wild) {
    const pts = [];
    const n = 28;
    for (let i = 0; i < n; i++) {
      const a = (i / n) * Math.PI * 2;
      // a = 0 é a ponta de cima; a = PI é o fundo.
      const up = Math.max(0, Math.cos(a));             // 1 no topo, 0 da metade para baixo
      const side = Math.sin(a);
      // gota: base redonda, topo afinado e alto
      let x = side * w * (1 - 0.62 * up * up);
      let y = Math.cos(a) > 0 ? -Math.cos(a) * h : -Math.cos(a) * w * 0.82;
      // o fogo: o topo dança para os lados e estica, mais forte quanto mais alto
      const wob = Math.sin(t * 7.3 + phase + up * 2.6) * 0.5 + Math.sin(t * 11.1 + phase * 1.7 + i) * 0.22;
      x += wob * w * 0.42 * up * wild;
      y -= (Math.sin(t * 9 + phase + i * 0.6) * 0.5 + 0.5) * h * 0.12 * up * up * wild;
      pts.push([cx + x, base + y - w * 0.82]);
    }
    return pts;
  }

  function smooth(ctx, pts) {
    const n = pts.length;
    ctx.beginPath();
    const mid = (p, q) => [(p[0] + q[0]) / 2, (p[1] + q[1]) / 2];
    let m = mid(pts[n - 1], pts[0]);
    ctx.moveTo(m[0], m[1]);
    for (let i = 0; i < n; i++) {
      const p = pts[i], q = pts[(i + 1) % n];
      m = mid(p, q);
      ctx.quadraticCurveTo(p[0], p[1], m[0], m[1]);
    }
    ctx.closePath();
  }

  // Desenha a chama inteira num quadro 100 x 120 (base em y = 108).
  function drawFlame(ctx, t, look) {
    const sway = Math.sin(t * 2.2) * 2.5;
    const layers = [
      { w: 34, h: 74, c: "#FF6A1F", p: 0, wild: 1 },
      { w: 25, h: 56, c: "#FFB421", p: 1.3, wild: 0.85 },
      { w: 15, h: 34, c: "#FFE58A", p: 2.1, wild: 0.6 },
    ];
    // línguas soltas dos lados (aparecem e somem)
    const side = (dx, ph) => tongue(50 + dx + sway * 0.6, 100, 10, 30 + Math.sin(t * 5 + ph) * 8, t, ph, 1.2);
    const extras = [side(-24, 0.7), side(25, 2.4)];
    // recorte branco e contorno de caneta da camada de fora
    const outer = tongue(50 + sway, 108, layers[0].w, layers[0].h, t, 0, 1);
    ctx.lineJoin = "round";
    for (const shape of [...extras, outer]) { smooth(ctx, shape); ctx.strokeStyle = "#FFFFFF"; ctx.lineWidth = 13; ctx.stroke(); }
    for (const shape of extras) { smooth(ctx, shape); ctx.fillStyle = "#FF8A2B"; ctx.fill(); ctx.strokeStyle = INK; ctx.lineWidth = 4.5; ctx.stroke(); }
    smooth(ctx, outer); ctx.fillStyle = layers[0].c; ctx.fill(); ctx.strokeStyle = INK; ctx.lineWidth = 5; ctx.stroke();
    for (let i = 1; i < layers.length; i++) {
      const L = layers[i];
      smooth(ctx, tongue(50 + sway * (1 - i * 0.2), 106 - i * 2, L.w, L.h, t, L.p, L.wild));
      ctx.fillStyle = L.c; ctx.fill();
    }
    // o rosto: acompanha o balanço, pisca e olha em volta
    const fx = 50 + sway * 0.8 + look * 3, fy = 82;
    const blink = (t % 3.4) > 3.25 ? 0.15 : 1;
    ctx.fillStyle = "rgba(255,143,163,.75)";
    ctx.beginPath(); ctx.ellipse(fx - 13, fy + 7, 4.6, 2.6, 0, 0, Math.PI * 2); ctx.fill();
    ctx.beginPath(); ctx.ellipse(fx + 13, fy + 7, 4.6, 2.6, 0, 0, Math.PI * 2); ctx.fill();
    for (const ex of [fx - 8, fx + 8]) {
      ctx.fillStyle = INK;
      ctx.beginPath(); ctx.ellipse(ex, fy, 3.9, 5.2 * blink, 0, 0, Math.PI * 2); ctx.fill();
      if (blink > 0.5) {
        ctx.fillStyle = "#FFFFFF";
        ctx.beginPath(); ctx.arc(ex - 1.2 + look, fy - 2, 1.6, 0, Math.PI * 2); ctx.fill();
      }
    }
    // sorriso aberto
    ctx.fillStyle = INK;
    ctx.beginPath(); ctx.moveTo(fx - 6, fy + 8); ctx.quadraticCurveTo(fx, fy + 17, fx + 6, fy + 8); ctx.closePath(); ctx.fill();
    ctx.fillStyle = "#FF8FA3";
    ctx.beginPath(); ctx.ellipse(fx, fy + 12.5, 2.6, 1.3, 0, 0, Math.PI * 2); ctx.fill();
  }

  window.estudarioFlame = function (canvas, opts) {
    opts = opts || {};
    const delay = opts.delay ?? 0.3, grow = opts.grow ?? 2.2;
    const dpr = Math.min(2, window.devicePixelRatio || 1);
    const W = canvas.clientWidth || 220, H = canvas.clientHeight || 260;
    canvas.width = W * dpr; canvas.height = H * dpr;
    const ctx = canvas.getContext("2d");
    const embers = Array.from({ length: 18 }, (_, i) => ({ x: (i * 37) % 60 - 30, s: 2 + (i % 4), sp: 0.5 + (i % 5) * 0.12, ph: i * 0.37 }));
    const t0 = performance.now();
    let raf = 0, stopped = false;
    const easeOut = (x) => 1 - Math.pow(1 - x, 3);
    function frame(now) {
      if (stopped) return;
      const t = (now - t0) / 1000;
      const k = Math.min(1, Math.max(0, (t - delay) / grow));
      const g = easeOut(k);
      // cresce de forma contínua (com um leve passar do ponto no fim) e gira enquanto é pequena
      const scale = t < delay ? 0 : 0.1 + 0.9 * g + Math.sin(k * Math.PI) * 0.06;
      const spin = (1 - g) * (1 - g) * Math.PI * 4;
      ctx.setTransform(dpr, 0, 0, dpr, 0, 0);
      ctx.clearRect(0, 0, W, H);
      if (scale > 0) {
        // brilho
        const cx = W / 2, cy = H * 0.62;
        const r = Math.min(W, H) * 0.5 * (0.35 + 0.65 * g);
        const glow = ctx.createRadialGradient(cx, cy, 0, cx, cy, r);
        glow.addColorStop(0, `rgba(255,138,31,${0.5 * g})`); glow.addColorStop(0.45, `rgba(255,180,33,${0.16 * g})`); glow.addColorStop(1, "rgba(255,180,33,0)");
        ctx.fillStyle = glow; ctx.fillRect(0, 0, W, H);
        // brasas subindo
        if (g > 0.25) for (const e of embers) {
          const p = ((t * e.sp + e.ph) % 1);
          ctx.globalAlpha = (1 - p) * 0.9 * g;
          ctx.fillStyle = "#FFB421";
          ctx.beginPath(); ctx.arc(cx + e.x * (1 + p * 0.5) + Math.sin(t * 3 + e.ph) * 6, H * 0.86 - p * H * 0.8, e.s * (1 - p * 0.6), 0, Math.PI * 2); ctx.fill();
        }
        ctx.globalAlpha = 1;
        // a chama, girando enquanto pequena, com a base no chão
        const unit = Math.min(W / 128, H / 150) * scale;
        // gira em volta do meio da chama (não da base), para nada escapar da área de desenho
        ctx.translate(cx, H * 0.9 - 55 * unit);
        ctx.rotate(spin);
        ctx.scale(unit, unit);
        ctx.translate(-50, -108 + 55);
        drawFlame(ctx, t, Math.sin(t * 0.9) * (t % 5 > 3.6 ? 1 : 0.4));
      }
      raf = requestAnimationFrame(frame);
    }
    raf = requestAnimationFrame(frame);
    return { stop() { stopped = true; cancelAnimationFrame(raf); } };
  };
})();
