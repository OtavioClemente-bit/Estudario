// O Folha animado do site: tradução direta de app/.../ui/assistant/Folha.kt (mesma grade 100 x 100,
// mesmas cores e os mesmos movimentos), desenhado em <canvas>.
//
// Uso: <estudario-folha mood="idle|thinking|talking|happy|sad|wave|point|wink" size="96" clickable>
// Ele respira, pisca em intervalos irregulares (às vezes duas vezes), olha em volta, a borla do
// capelo e a fita balançam; pensando, folheia e escreve; feliz, pula e solta confete. Um toque faz
// ele amassar, pular e piscar um olho. Um laço de animação só para todos, parado fora da tela, e
// quadro fixo para quem pede menos movimento no sistema.
(function () {
  if (window.customElements && customElements.get("estudario-folha")) return;

  const INK = "#26215C", PAGE_LINE = "#D8D4E6", CAP = "#231C6B", CAP_TOP = "#2F2789";
  const GOLD = "#F5B83D", MINT = "#7EE0B8", MINT_DARK = "#3FBF8F", CHEEK = "#F4A6C0", SPARK = "#FFE07A";
  const PAPER = "#FFFDF8", PAPER_MID = "#F7F4EE", PAPER_EDGE = "#E9E4DA", PAPER_EDGE_DARK = "#CFC8BC", GUTTER = "#3A3170";
  const PI = Math.PI, sin = Math.sin, cos = Math.cos, abs = Math.abs;

  const reduceMotion = window.matchMedia ? window.matchMedia("(prefers-reduced-motion: reduce)") : { matches: false };

  // ------------------------------------------------------------------ cor
  function hex(c) {
    const n = parseInt(c.slice(1), 16);
    return [(n >> 16) & 255, (n >> 8) & 255, n & 255];
  }
  function rgba(c, a) { const [r, g, b] = hex(c); return `rgba(${r},${g},${b},${a})`; }
  function lerpColor(a, b, f) {
    const x = hex(a), y = hex(b);
    const m = (i) => Math.round(x[i] + (y[i] - x[i]) * f);
    return `rgb(${m(0)},${m(1)},${m(2)})`;
  }
  const clamp = (v, lo, hi) => Math.max(lo, Math.min(hi, v));

  // Grão do papel fixo (semente 7), como no app: sempre os mesmos pontinhos.
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

  // ------------------------------------------------------------------ formas
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
  function vGrad(g, stops, y0, y1) {
    const gr = g.createLinearGradient(0, y0, 0, y1);
    stops.forEach((c, i) => gr.addColorStop(Array.isArray(c) ? c[0] : i / (stops.length - 1), Array.isArray(c) ? c[1] : c));
    return gr;
  }
  function hGrad(g, stops, x0, x1) {
    const gr = g.createLinearGradient(x0, 0, x1, 0);
    stops.forEach((c, i) => gr.addColorStop(Array.isArray(c) ? c[0] : i / (stops.length - 1), Array.isArray(c) ? c[1] : c));
    return gr;
  }
  function line(g, color, x0, y0, x1, y1, w, round = true) {
    g.beginPath(); g.moveTo(x0, y0); g.lineTo(x1, y1);
    g.strokeStyle = color; g.lineWidth = w; g.lineCap = round ? "round" : "butt"; g.stroke();
  }
  function stroke(g, path, color, w) { g.strokeStyle = color; g.lineWidth = w; g.lineCap = "round"; g.stroke(path); }
  function oval(g, color, x, y, w, h) { g.beginPath(); g.ellipse(x + w / 2, y + h / 2, Math.max(w / 2, 0.01), Math.max(h / 2, 0.01), 0, 0, 2 * PI); g.fillStyle = color; g.fill(); }
  function circle(g, color, r, x, y) { g.beginPath(); g.arc(x, y, Math.max(r, 0.01), 0, 2 * PI); g.fillStyle = color; g.fill(); }

  // ------------------------------------------------------------------ desenho (grade 100 x 100)
  function drawFolha(g, s) {
    const { t, mood } = s;
    const hop = mood === "happy" ? -abs(sin(t * 5.2)) * 5 : sin(t * 2.1) * 1.4;
    const tilt = mood === "talking" ? sin(t * 3.4) * 3 : mood === "thinking" ? sin(t * 1.3) * 2 : mood === "sad" ? -4 : sin(t * 0.9) * 1.2;
    const squashX = 1 + 0.10 * s.bounce, squashY = 1 - 0.12 * s.bounce;

    // Sombra no chão: menor quando ele sobe.
    const lift = Math.max(-hop, 0);
    g.save();
    g.translate(50, 95); g.scale(1, 0.18); g.translate(-50, -95);
    const r1 = 36 - lift * 1.6;
    let rg = g.createRadialGradient(50, 95, 0, 50, 95, r1);
    rg.addColorStop(0, rgba(INK, 0.28)); rg.addColorStop(1, rgba(INK, 0));
    circle(g, rg, r1, 50, 95);
    rg = g.createRadialGradient(50, 95, 0, 50, 95, 26);
    rg.addColorStop(0, rgba(INK, 0.30 * clamp(1 - lift / 6, 0.3, 1))); rg.addColorStop(1, rgba(INK, 0));
    circle(g, rg, 26, 50, 95);
    g.restore();

    g.save();
    g.translate(0, hop);
    g.translate(50, 88); g.scale(squashX, squashY); g.translate(-50, -88);
    g.translate(50, 80); g.rotate(tilt * PI / 180); g.translate(-50, -80);
    drawArms(g, t, mood);
    drawBook(g, t, mood);
    drawPageLines(g, t, mood);
    if (mood === "thinking") drawTurningPage(g, t);
    drawFace(g, s);
    drawCap(g, t, tilt);
    g.restore();
    if (mood === "happy") drawCelebration(g, t);
  }

  function drawBook(g, t, mood) {
    const cover = new Path2D();
    cover.moveTo(8.5, 43.5); cover.bezierCurveTo(22, 40.5, 38, 41.5, 50, 47.5); cover.bezierCurveTo(62, 41.5, 78, 40.5, 91.5, 43.5);
    cover.lineTo(91.5, 89); cover.bezierCurveTo(78, 86.4, 62, 87.4, 50, 93); cover.bezierCurveTo(38, 87.4, 22, 86.4, 8.5, 89); cover.closePath();
    g.save(); g.translate(0, 1.7); g.fillStyle = "#1A1370"; g.fill(cover); g.restore();
    g.fillStyle = vGrad(g, ["#5247E0", "#3A2FC4", "#2A209E"], 41, 93); g.fill(cover);
    let lg = g.createLinearGradient(8, 44, 40, 70);
    lg.addColorStop(0, "rgba(255,255,255,0.14)"); lg.addColorStop(1, "rgba(255,255,255,0)");
    g.fillStyle = lg; g.fill(cover);
    // Trama de linho da capa.
    g.save(); g.clip(cover);
    for (let x = -50; x < 100; x += 1.5) {
      line(g, "rgba(255,255,255,0.045)", x, 40, x + 54, 95, 0.3, false);
      line(g, "rgba(0,0,0,0.06)", x + 54, 40, x, 95, 0.3, false);
    }
    g.restore();

    drawRibbon(g, t, mood);

    for (const side of [-1, 1]) {
      const block = pageBlock(side, 3.6);
      g.fillStyle = vGrad(g, [PAPER_EDGE, PAPER_EDGE_DARK], 80, 91); g.fill(block);
      for (let i = 1; i <= 4; i++) {
        const d = i * 0.72, p = new Path2D();
        p.moveTo(50 + side * (38 + d * 0.45), 84.4 + d);
        p.bezierCurveTo(50 + side * 26.2, 80.6 + d, 50 + side * 10, 81.4 + d, 50, 87 + d);
        stroke(g, p, "rgba(140,130,112,0.22)", 0.28);
      }
      for (let i = 1; i <= 3; i++) {
        const x = 50 + side * (38 + i * 0.42);
        line(g, "rgba(140,130,112,0.18)", x, 41 + i * 0.3, x, 84.4 + i * 0.9, 0.25, false);
      }
    }

    const left = page(-1), right = page(1);
    g.fillStyle = hGrad(g, [PAPER_MID, PAPER, PAPER, PAPER_MID], 12, 50); g.fill(left);
    g.fillStyle = hGrad(g, [PAPER_MID, PAPER, PAPER, "#F1EDE6"], 50, 88); g.fill(right);
    const gutter = [[0, rgba(GUTTER, 0)], [0.55, rgba(GUTTER, 0.035)], [0.85, rgba(GUTTER, 0.10)], [1, rgba(GUTTER, 0.20)]];
    g.fillStyle = hGrad(g, gutter, 39, 50); g.fill(left);
    g.fillStyle = hGrad(g, gutter, 61, 50); g.fill(right);
    for (const s of GRAIN) {
      if (s.long) line(g, `rgba(154,143,122,${s.a})`, s.x, s.y, s.x + 1.4, s.y + 0.3, 0.18);
      else circle(g, `rgba(154,143,122,${s.a})`, s.r, s.x, s.y);
    }
    g.fillStyle = hGrad(g, [rgba(GUTTER, 0.06), rgba(GUTTER, 0)], 12, 17); g.fill(left);
    g.fillStyle = hGrad(g, [rgba(GUTTER, 0), rgba(GUTTER, 0.08)], 83, 88); g.fill(right);
    line(g, rgba(GUTTER, 0.16), 50, 44.3, 50, 86.8, 0.35, false);
    for (const side of [-1, 1]) {
      const rim = new Path2D();
      rim.moveTo(50 + side * 3, 41.9);
      rim.bezierCurveTo(50 + side * 12, 37.9, 50 + side * 26, 36.9, 50 + side * 36.5, 40.2);
      stroke(g, rim, "rgba(255,255,255,0.9)", 0.5);
    }
  }

  const TURN_SHARE = 0.32;

  function drawTurningPage(g, t) {
    const cycle = (t % 2.4) / 2.4;
    const raw = clamp(cycle / TURN_SHARE, 0, 1);
    if (raw <= 0 || raw >= 1) return;
    const p = raw * raw * (3 - 2 * raw);
    const ang = PI * p, c = cos(ang), lift = sin(ang);
    const edgeX = 50 + 38 * c, w = edgeX - 50, up = lift * 6, dir = c >= 0 ? 1 : -1;
    const sheet = new Path2D();
    sheet.moveTo(50, 44);
    sheet.bezierCurveTo(50 + w * 0.28, 37.6 - up * 0.7, 50 + w * 0.7, 36.4 - up, edgeX, 40.4 - up * 0.8);
    sheet.quadraticCurveTo(edgeX + dir * 2.2 * lift, 62 - up, edgeX, 84.4 - up * 0.8);
    sheet.bezierCurveTo(50 + w * 0.7, 80.6 - up, 50 + w * 0.28, 81.4 - up * 0.5, 50, 87);
    sheet.closePath();
    g.save(); g.translate(dir * 2.5 * lift, 2.2 * lift); g.fillStyle = rgba(GUTTER, 0.13 * lift); g.fill(sheet); g.restore();
    const front = c >= 0, facing = abs(c), base = front ? PAPER : "#F2EEE6";
    if (abs(w) > 0.6) {
      g.fillStyle = hGrad(g, [lerpColor(base, GUTTER, 0.16 + 0.1 * lift), lerpColor(base, GUTTER, 0.05 * (1 - facing)), lerpColor(base, "#FFFFFF", 0.5 * facing)], 50, edgeX);
      g.fill(sheet);
      if (front) {
        for (const [y, len] of [[45, 0.5], [49, 0.7], [79.5, 0.45]]) {
          const yy = y - up * 0.85;
          line(g, rgba(PAGE_LINE, facing), 50 + w * 0.88, yy, 50 + w * (0.88 - len), yy + 1.5 * (len - 0.4), 1.6 * (0.5 + 0.5 * facing));
        }
      }
    }
    line(g, "rgba(255,255,255,0.8)", edgeX, 40.6 - up * 0.8, edgeX + dir * 1.6 * lift, 62 - up, 0.45);
    stroke(g, sheet, rgba(GUTTER, 0.18), 0.25);
  }

  function drawPageLines(g, t, mood) {
    const writing = mood === "thinking";
    const cycle = (t % 2.4) / 2.4;
    const ln = (x0, y0, len, slope, index, mirror) => {
      const p = !writing ? 1 : clamp((cycle - TURN_SHARE) / (1 - TURN_SHARE) * 6 - index, 0, 1);
      if (p <= 0) return;
      const x1 = mirror ? x0 - len * p : x0 + len * p;
      line(g, PAGE_LINE, x0, y0, x1, y0 + slope * len * p, 1.6);
    };
    ln(17, 45, 20, 0.1, 0, false); ln(17, 49, 26, 0.12, 1, false);
    ln(83, 45, 20, 0.1, 2, true); ln(83, 49, 26, 0.12, 3, true);
    ln(17, 79.5, 18, -0.03, 4, false); ln(83, 79.5, 18, -0.03, 5, true);
  }

  function drawFace(g, s) {
    const { t, mood, blink, lookX, lookY, wink } = s;
    const eyeL = [31 + lookX * 1.6, 61 + lookY * 1.4], eyeR = [69 + lookX * 1.6, 61 + lookY * 1.4];
    const cheekA = mood === "happy" ? 0.7 : 0.45;
    oval(g, rgba(CHEEK, cheekA), 19, 67, 9, 5);
    oval(g, rgba(CHEEK, cheekA), 72, 67, 9, 5);
    const eye = ([x, y], closed, isWink) => {
      if (mood === "happy" || isWink) {
        const p = new Path2D(); p.moveTo(x - 4.5, y + 1.5); p.quadraticCurveTo(x, y - 5, x + 4.5, y + 1.5);
        stroke(g, p, INK, 2.6); return;
      }
      const ry = 5.8 * Math.max(1 - closed, 0.08);
      oval(g, INK, x - 4.4, y - ry, 8.8, ry * 2);
      if (closed < 0.6) {
        circle(g, "#FFFFFF", 1.7, x - 1.4 + lookX * 0.6, y - 2.2);
        circle(g, "rgba(255,255,255,0.7)", 0.8, x + 1.6, y + 1.8);
      }
    };
    const b = mood === "happy" ? 0 : blink;
    eye(eyeL, b, false);
    eye(eyeR, b, wink);
    if (mood === "thinking") {
      line(g, INK, 26, 51.5, 35, 50, 1.8); line(g, INK, 65, 49, 74, 50.5 + sin(t * 2), 1.8);
    } else if (mood === "sad") {
      line(g, INK, 26, 51, 35, 53, 1.8); line(g, INK, 65, 53, 74, 51, 1.8);
    }
    const mx = 50, my = 73;
    if (mood === "talking") {
      const open = (0.5 + 0.5 * sin(t * 13)) * (0.55 + 0.45 * abs(sin(t * 3.1)));
      const h = 1.2 + 5 * open;
      oval(g, INK, mx - 4.2, my - h / 2, 8.4, h);
      if (h > 3) oval(g, CHEEK, mx - 2.4, my + h / 2 - 2.2, 4.8, 2);
    } else if (mood === "happy" || mood === "wave") {
      const p = new Path2D(); p.moveTo(mx - 6, my - 1.5); p.quadraticCurveTo(mx, my + 9, mx + 6, my - 1.5); p.closePath();
      g.fillStyle = INK; g.fill(p);
      oval(g, CHEEK, mx - 3, my + 2, 6, 2.6);
    } else if (mood === "sad") {
      const p = new Path2D(); p.moveTo(mx - 4.5, my + 2.5); p.quadraticCurveTo(mx, my - 2.5, mx + 4.5, my + 2.5); stroke(g, p, INK, 2.2);
    } else if (mood === "thinking") {
      const p = new Path2D(); p.moveTo(mx - 3, my + 0.5); p.quadraticCurveTo(mx + 1, my + 2.5, mx + 4, my - 0.5); stroke(g, p, INK, 2.2);
    } else {
      const p = new Path2D(); p.moveTo(mx - 5, my - 1); p.quadraticCurveTo(mx, my + 5, mx + 5, my - 1); stroke(g, p, INK, 2.3);
    }
  }

  function drawCap(g, t, tilt) {
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
    line(g, "rgba(255,255,255,0.18)", 33, 27.5, 50, 22.5, 1.2);
    const swing = sin(t * 2.6) * 4 - tilt * 0.8;
    const bx = 50, by = 29.5, cx = 74, cy = 30.5;
    line(g, GOLD, bx, by, cx, cy, 1.3);
    const ex = cx + swing * 0.5, ey = cy + 12;
    const cord = new Path2D(); cord.moveTo(cx, cy); cord.quadraticCurveTo(cx + swing * 0.2, cy + 6, ex, ey);
    stroke(g, cord, GOLD, 1.3);
    const tassel = new Path2D();
    tassel.moveTo(ex - 1.4, ey); tassel.lineTo(ex + 1.4, ey);
    tassel.lineTo(ex + 2.4 + swing * 0.15, ey + 6.5); tassel.lineTo(ex - 2.4 + swing * 0.15, ey + 6.5); tassel.closePath();
    g.fillStyle = vGrad(g, [GOLD, "#D89422"], ey, ey + 6.5); g.fill(tassel);
    circle(g, GOLD, 1.8, bx, by);
    circle(g, "#FFE3A0", 0.7, bx - 0.5, by - 0.5);
  }

  function drawRibbon(g, t, mood) {
    const sad = mood === "sad";
    const sway = sad ? 0 : sin(t * 1.9 + 0.6) * 1.3;
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

  function drawCelebration(g, t) {
    const colors = [SPARK, MINT, CHEEK];
    for (let i = 0; i < 5; i++) {
      const p = (t * 0.7 + i * 0.21) % 1;
      const side = i % 2 === 0 ? -1 : 1;
      circle(g, rgba(colors[i % 3], 1 - p), 2.6 * (1 - p) * 0.9, 50 + side * (30 + i * 3), 70 - p * 48);
    }
  }

  function drawArms(g, t, mood) {
    const arm = (sx, sy, hx, hy) => {
      const ex = (sx + hx) / 2 + (hx < 50 ? -2 : 2), ey = (sy + hy) / 2 + 2;
      const p = new Path2D(); p.moveTo(sx, sy); p.quadraticCurveTo(ex, ey, hx, hy);
      stroke(g, p, "#3326CE", 3.2);
      circle(g, "#FFFFFF", 3.3, hx, hy);
      g.beginPath(); g.arc(hx, hy, 3.3, 0, 2 * PI); g.strokeStyle = "#3326CE"; g.lineWidth = 1.4; g.stroke();
    };
    const L = [12, 66], R = [88, 66];
    if (mood === "talking") { const wv = sin(t * 7) * 5; arm(...L, 3, 76); arm(...R, 97 + wv * 0.3, 52 + wv); }
    else if (mood === "happy") { const pump = abs(sin(t * 5.2)) * 4; arm(...L, 2, 48 - pump); arm(...R, 98, 48 - pump); }
    else if (mood === "sad") { arm(...L, 6, 84); arm(...R, 94, 84); }
    else if (mood === "thinking") { arm(...L, 4, 76 + sin(t * 1.5)); arm(...R, 93, 70 + sin(t * 2.2) * 1.5); }
    else if (mood === "wave") { const wv = sin(t * 13) * 9; arm(...L, 4, 78); arm(...R, 100 + wv * 0.4, 30 + wv * 0.5); }
    else { arm(...L, 4, 78 + sin(t * 2.1) * 1.2); arm(...R, 96, 78 - sin(t * 2.1) * 1.2); }
  }

  // ------------------------------------------------------------------ animação
  const ease = (x) => 1 - (1 - x) * (1 - x); // desacelera, como o tween padrão
  const live = new Set();
  let rafId = 0;

  function tick(now) {
    rafId = 0;
    for (const el of live) el._frame(now);
    if (live.size) rafId = requestAnimationFrame(tick);
  }
  function wake() { if (!rafId && live.size) rafId = requestAnimationFrame(tick); }

  // "point" e "wink" são humores do site antigo: point acena, wink fica piscando um olho.
  const MOODS = { idle: "idle", thinking: "thinking", talking: "talking", happy: "happy", sad: "sad", wave: "wave", point: "wave", wink: "idle" };

  class EstudarioFolha extends HTMLElement {
    static get observedAttributes() { return ["mood", "size"]; }

    constructor() {
      super();
      this._start = performance.now();
      this._blink = 0; this._blinkPlan = []; this._nextBlink = this._start + 1800 + Math.random() * 2800;
      this._look = { x: 0, y: 0, fx: 0, fy: 0, tx: 0, ty: 0, from: 0, dur: 1, next: 0 };
      this._bounceAt = -1; this._wink = false; this._winkUntil = 0;
      this._visible = true;
    }

    connectedCallback() {
      if (!this._canvas) {
        this._canvas = document.createElement("canvas");
        this._canvas.style.display = "block";
        this._canvas.style.width = "100%";
        this._canvas.style.height = "100%";
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

    attributeChangedCallback(name) { if (name === "size") this._size(); }

    get mood() { return MOODS[this.getAttribute("mood") || "idle"] || "idle"; }

    /** Toque: amassa, pula e pisca um olho. */
    poke() {
      this._bounceAt = performance.now();
      this._wink = true; this._winkUntil = this._bounceAt + 110 + 600;
      live.add(this); wake();
    }

    _size() {
      if (!this._canvas) return;
      const size = parseInt(this.getAttribute("size") || "96", 10);
      this.style.width = size + "px";
      this.style.height = size + "px";
      const dpr = Math.min(window.devicePixelRatio || 1, 2.5);
      this._canvas.width = Math.round(size * dpr);
      this._canvas.height = Math.round(size * dpr);
      this._px = size * dpr;
    }

    _updateBlink(now) {
      if (now >= this._nextBlink && !this._blinkPlan.length) {
        const twice = Math.random() < 0.2;
        let at = now;
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
      const p = clamp((now - L.from) / L.dur, 0, 1);
      L.x = L.fx + (L.tx - L.fx) * ease(p);
      L.y = L.fy + (L.ty - L.fy) * ease(p);
      if (p < 1 || now < L.next) return;
      L.fx = L.x; L.fy = L.y; L.from = now;
      if (mood === "thinking") {
        // Lendo: da esquerda para a direita, olhando para baixo, na página.
        const toLeft = L.x > 0;
        L.tx = toLeft ? -1 : 1; L.ty = 0.6; L.dur = toLeft ? 260 : 900; L.next = now + L.dur;
      } else if (mood === "idle") {
        L.tx = Math.random() * 2 - 1; L.ty = Math.random() * 1.2 - 0.6; L.dur = 380;
        L.next = now + 380 + 900 + Math.random() * 1700;
      } else {
        L.tx = 0; L.ty = 0; L.dur = 300; L.next = now + 1100;
      }
    }

    _bounce(now) {
      if (this._bounceAt < 0) return 0;
      const d = now - this._bounceAt;
      if (d < 110) return d / 110;
      const s = (d - 110) / 1000;
      if (s > 1.4) { this._bounceAt = -1; return 0; }
      // Mola frouxa (amortecimento 0,32): passa do ponto e volta, como borracha.
      return Math.exp(-s * 5.5) * cos(s * 17);
    }

    _frame(now) {
      if (!this._visible || !this._canvas) return;
      const g = this._canvas.getContext("2d");
      if (!g) return;
      const mood = this.mood;
      const still = reduceMotion.matches;
      const t = still ? 0.35 : (now - this._start) / 1000;
      if (!still) { this._updateBlink(now); this._updateLook(now, mood); }
      if (this._wink && now > this._winkUntil) this._wink = false;
      const winkAttr = this.getAttribute("mood") === "wink";
      g.setTransform(1, 0, 0, 1, 0, 0);
      g.clearRect(0, 0, this._canvas.width, this._canvas.height);
      const k = this._px / 100;
      g.setTransform(k, 0, 0, k, 0, 0);
      drawFolha(g, {
        t, mood,
        blink: still ? 0 : this._blink,
        lookX: still ? 0.3 : this._look.x,
        lookY: still ? -0.2 : this._look.y,
        wink: this._wink || winkAttr,
        bounce: this._bounce(now),
      });
      // Quadro fixo para quem pede menos movimento: desenha uma vez e sai do laço.
      if (still && this._bounceAt < 0) live.delete(this);
    }
  }

  customElements.define("estudario-folha", EstudarioFolha);
  if (reduceMotion.addEventListener) reduceMotion.addEventListener("change", () => {
    document.querySelectorAll("estudario-folha").forEach((el) => { if (el.isConnected) { live.add(el); } });
    wake();
  });
})();
