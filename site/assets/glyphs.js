// Os ícones do Estudário no site: tradução direta de app/.../ui/brand/BrandGlyphs.kt (mesma grade
// 100 x 100, mesmas cores e o mesmo relevo de massinha: espessura embaixo, luz de cima e brilho no
// alto). estudarioGlyph(nome, apagado) devolve o <svg>; nome é o do Material Symbols ("home",
// "calendar_month"...), traduzido pela mesma tabela de BrandIcon.kt. Sem desenho, devolve null e o
// site usa o símbolo comum (setas, fechar, mais opções continuam traços simples, como no app).
(function () {
  const hex = (h, a = 1) => { const n = parseInt(h.slice(1), 16); return [((n >> 16) & 255) / 255, ((n >> 8) & 255) / 255, (n & 255) / 255, a]; };
  const B = {
    Indigo: hex("#5B4BF5"), Green: hex("#1FB574"), Amber: hex("#FFB421"), Coral: hex("#FF5C4D"), Sky: hex("#2EA8F5"),
    Pink: hex("#F0559A"), Violet: hex("#8E5CF7"), Brown: hex("#B9772E"), Paper: hex("#FFFDF8"), Ink: hex("#2B2A4A"), Steel: hex("#8D8BA8"),
  };
  const NIGHT = hex("#1A1440"), WHITE = hex("#FFFFFF"), BLACK = hex("#000000");
  const alpha = (c, a) => [c[0], c[1], c[2], a];
  const lerp = (a, b, f) => [a[0] + (b[0] - a[0]) * f, a[1] + (b[1] - a[1]) * f, a[2] + (b[2] - a[2]) * f, a[3] + (b[3] - a[3]) * f];
  const lin = (v) => (v <= 0.04045 ? v / 12.92 : Math.pow((v + 0.055) / 1.055, 2.4));
  const luminance = (c) => 0.2126 * lin(c[0]) + 0.7152 * lin(c[1]) + 0.0722 * lin(c[2]);
  const css = (c) => `rgb(${Math.round(c[0] * 255)},${Math.round(c[1] * 255)},${Math.round(c[2] * 255)})`;
  const op = (c) => (c[3] < 1 ? ` fill-opacity="${c[3].toFixed(3)}"` : "");
  const sop = (c) => (c[3] < 1 ? ` stroke-opacity="${c[3].toFixed(3)}"` : "");
  const n = (v) => +v.toFixed(2);

  // ------------------------------------------------------------ formas: { d, b: [x0, y0, x1, y1] }
  function boundsOf(d) {
    const nums = d.match(/-?\d*\.?\d+/g).map(Number);
    let x0 = 1e9, y0 = 1e9, x1 = -1e9, y1 = -1e9;
    for (let i = 0; i + 1 < nums.length; i += 2) { x0 = Math.min(x0, nums[i]); x1 = Math.max(x1, nums[i]); y0 = Math.min(y0, nums[i + 1]); y1 = Math.max(y1, nums[i + 1]); }
    return [x0, y0, x1, y1];
  }
  const path = (d, b) => ({ d, b: b || boundsOf(d) });
  function rrD(x, y, w, h, r, rot) {
    r = Math.min(r, w / 2, h / 2);
    const P = (px, py) => {
      if (!rot) return `${n(px)} ${n(py)}`;
      const a = rot.deg * Math.PI / 180, dx = px - rot.cx, dy = py - rot.cy;
      return `${n(rot.cx + dx * Math.cos(a) - dy * Math.sin(a))} ${n(rot.cy + dx * Math.sin(a) + dy * Math.cos(a))}`;
    };
    const A = `A${n(r)} ${n(r)} 0 0 1 `;
    return `M${P(x + r, y)}L${P(x + w - r, y)}${A}${P(x + w, y + r)}L${P(x + w, y + h - r)}${A}${P(x + w - r, y + h)}L${P(x + r, y + h)}${A}${P(x, y + h - r)}L${P(x, y + r)}${A}${P(x + r, y)}Z`;
  }
  const rr = (x, y, w, h, r) => path(rrD(x, y, w, h, r), [x, y, x + w, y + h]);
  const circD = (cx, cy, r) => `M${n(cx - r)} ${n(cy)}A${n(r)} ${n(r)} 0 1 1 ${n(cx + r)} ${n(cy)}A${n(r)} ${n(r)} 0 1 1 ${n(cx - r)} ${n(cy)}Z`;
  const ovalD = (x0, y0, x1, y1) => { const rx = (x1 - x0) / 2, ry = (y1 - y0) / 2, cy = (y0 + y1) / 2; return `M${n(x0)} ${n(cy)}A${n(rx)} ${n(ry)} 0 1 1 ${n(x1)} ${n(cy)}A${n(rx)} ${n(ry)} 0 1 1 ${n(x0)} ${n(cy)}Z`; };
  const circle = (cx, cy, r) => path(circD(cx, cy, r), [cx - r, cy - r, cx + r, cy + r]);
  function poly(...v) { let d = `M${v[0]} ${v[1]}`; for (let i = 2; i < v.length; i += 2) d += `L${v[i]} ${v[i + 1]}`; return path(d + "Z"); }
  function star5(cx, cy, outer, inner) {
    let d = "";
    for (let i = 0; i < 10; i++) {
      const r = i % 2 === 0 ? outer : inner, a = (-90 + i * 36) * Math.PI / 180;
      d += `${i ? "L" : "M"}${n(cx + r * Math.cos(a))} ${n(cy + r * Math.sin(a))}`;
    }
    return path(d + "Z", [cx - outer, cy - outer, cx + outer, cy + outer]);
  }
  function flame(cx, top, hw, h) {
    const bottom = top + h;
    return path(`M${cx} ${top}C${cx + hw * 0.4} ${top + h * 0.25} ${cx + hw} ${top + h * 0.38} ${cx + hw} ${top + h * 0.66}` +
      `C${cx + hw} ${bottom - h * 0.1} ${cx + hw * 0.5} ${bottom} ${cx} ${bottom}C${cx - hw * 0.5} ${bottom} ${cx - hw} ${bottom - h * 0.1} ${cx - hw} ${top + h * 0.66}` +
      `C${cx - hw} ${top + h * 0.45} ${cx - hw * 0.5} ${top + h * 0.4} ${cx - hw * 0.3} ${top + h * 0.25}C${cx - hw * 0.1} ${top + h * 0.36} ${cx} ${top + h * 0.2} ${cx} ${top}Z`,
    [cx - hw, top, cx + hw, bottom]);
  }
  const bubble = () => path(rrD(10, 12, 80, 60, 18) + "M28 66L24 88L46 70Z", [10, 12, 90, 88]);
  function arch(cx, bottom, r) {
    const cy = bottom - 1.1 * r;
    return `M${n(cx - r)} ${n(bottom)}L${n(cx - r)} ${n(cy)}A${n(r)} ${n(0.9 * r)} 0 0 1 ${n(cx + r)} ${n(cy)}L${n(cx + r)} ${n(bottom)}`;
  }
  function arcD(startDeg, sweepDeg, x, y, w, h) {
    const cx = x + w / 2, cy = y + h / 2, rx = w / 2, ry = h / 2;
    const a0 = startDeg * Math.PI / 180, a1 = (startDeg + sweepDeg) * Math.PI / 180;
    return `M${n(cx + rx * Math.cos(a0))} ${n(cy + ry * Math.sin(a0))}A${n(rx)} ${n(ry)} 0 ${sweepDeg > 180 ? 1 : 0} 1 ${n(cx + rx * Math.cos(a1))} ${n(cy + ry * Math.sin(a1))}`;
  }

  // ------------------------------------------------------------ pintor
  function painter(muted) {
    let out = "", defs = "", uid = 0;
    const c = (color) => {
      if (!muted) return color;
      const v = 0.55 + luminance(color) * 0.42;
      return [v, v, Math.min(1, v * 1.02), color[3]];
    };
    const dark = (color, f = 0.28) => lerp(c(color), NIGHT, f);
    const light = (color, f = 0.24) => lerp(c(color), WHITE, f);
    const fill = (d, col, extra = "") => { out += `<path d="${d}" fill="${css(col)}"${op(col)}${extra}/>`; };
    const strokePath = (d, col, w, extra = "") => { out += `<path d="${d}" fill="none" stroke="${css(col)}"${sop(col)} stroke-width="${w}" stroke-linecap="round" stroke-linejoin="round"${extra}/>`; };
    const api = {
      c, dark, light,
      push(s) { out += s; },
      /** Peça de massinha: espessura embaixo, face com luz de cima e brilho no alto à esquerda. */
      clay(shape, color, depth = 6, round = 0, gloss = true) {
        const side = dark(color);
        for (const step of [depth, depth * 0.5]) {
          const t = ` transform="translate(0 ${n(step)})"`;
          fill(shape.d, side, t);
          if (round > 0) strokePath(shape.d, side, round, t);
        }
        const [x0, y0, x1, y1] = shape.b;
        const id = "g" + uid++;
        const top = light(color), base = c(color);
        defs += `<linearGradient id="${id}" gradientUnits="userSpaceOnUse" x1="0" y1="${n(y0)}" x2="0" y2="${n(y1)}"><stop offset="0" stop-color="${css(top)}"${top[3] < 1 ? ` stop-opacity="${top[3]}"` : ""}/><stop offset="1" stop-color="${css(base)}"${base[3] < 1 ? ` stop-opacity="${base[3]}"` : ""}/></linearGradient>`;
        out += `<path d="${shape.d}" fill="url(#${id})"/>`;
        if (round > 0) out += `<path d="${shape.d}" fill="none" stroke="url(#${id})" stroke-width="${round}" stroke-linecap="round" stroke-linejoin="round"/>`;
        if (gloss) {
          const cid = "k" + uid++;
          const w = x1 - x0, h = y1 - y0;
          const gw = w * 0.42, gh = Math.min(h * 0.2, 14);
          defs += `<clipPath id="${cid}"><path d="${shape.d}"/></clipPath>`;
          out += `<ellipse clip-path="url(#${cid})" cx="${n(x0 + w * 0.1 + gw / 2)}" cy="${n(y0 + h * 0.06 + gh / 2)}" rx="${n(gw / 2)}" ry="${n(gh / 2)}" fill="#fff" fill-opacity="0.3"/>`;
        }
      },
      /** Detalhe plano por cima de uma peça (linhas, ponteiros, símbolos). */
      line(color, width, a, b, last) {
        strokePath(`M${n(a[0])} ${n(a[1])}L${n(b[0])} ${n(b[1])}${last ? `L${n(last[0])} ${n(last[1])}` : ""}`, c(color), width);
      },
      dot(color, x, y, r) { fill(circD(x, y, r), c(color)); },
      flat(shape, color) { fill(shape.d, c(color)); },
      stroke(d, color, w) { strokePath(d, color, w); },
      arc(color, start, sweep, x, y, w, h, width) { strokePath(arcD(start, sweep, x, y, w, h), c(color), width); },
      group(transform, body) { out += `<g transform="${transform}">`; body(); out += "</g>"; },
      shadow() { out += `<ellipse cx="50" cy="92.5" rx="32" ry="4.5" fill="#000" fill-opacity="0.1"/>`; },
      svg() { return `<svg xmlns="http://www.w3.org/2000/svg" viewBox="-2 -2 104 104"><defs>${defs}</defs>${out}</svg>`; },
    };
    return api;
  }

  // ------------------------------------------------------------ desenhos (BrandGlyphs.kt, um a um)
  const GLYPHS = {
    House(g) {
      g.clay(rr(22, 44, 56, 40, 8), B.Sky);
      g.clay(poly(14, 50, 50, 16, 86, 50), B.Coral, 5, 10);
      g.clay(rr(42, 60, 16, 24, 5), B.Amber, 3);
      g.dot(B.Ink, 54, 73, 1.8);
    },
    Books(g) {
      g.clay(rr(12, 26, 22, 60, 6), B.Indigo);
      g.clay(rr(38, 14, 22, 72, 6), B.Green);
      g.group("rotate(9 76 86)", () => g.clay(rr(64, 30, 22, 56, 6), B.Amber));
      for (const [x, top] of [[12, 26], [38, 14]]) {
        g.flat(rr(x + 4, top + 10, 14, 5, 2.5), alpha(B.Paper, 0.85));
        g.flat(rr(x + 4, top + 19, 14, 3, 1.5), alpha(B.Paper, 0.6));
      }
    },
    OpenBook(g) {
      g.clay(rr(6, 30, 88, 54, 10), B.Indigo);
      g.clay(poly(12, 24, 48, 32, 48, 78, 12, 72), B.Paper, 3, 6, false);
      g.clay(poly(88, 24, 52, 32, 52, 78, 88, 72), B.Paper, 3, 6, false);
      for (let i = 0; i <= 2; i++) {
        const y = 42 + i * 9;
        g.line(B.Steel, 3, [20, y - 2], [41, y + 2]);
        g.line(B.Steel, 3, [80, y - 2], [59, y + 2]);
      }
    },
    Calendar(g) {
      g.clay(rr(12, 22, 76, 64, 12), B.Paper);
      g.flat(path("M12 44L12 34A12 12 0 0 1 24 22L76 22A12 12 0 0 1 88 34L88 44Z"), B.Coral);
      g.clay(rr(28, 12, 9, 18, 4.5), B.Ink, 2, 0, false);
      g.clay(rr(63, 12, 9, 18, 4.5), B.Ink, 2, 0, false);
      for (let row = 0; row <= 1; row++) for (let col = 0; col <= 2; col++) {
        const x = 24 + col * 19, y = 52 + row * 15;
        if (row === 1 && col === 2) g.clay(rr(x, y, 13, 10, 3), B.Green, 2, 0, false);
        else g.flat(rr(x, y, 13, 10, 3), alpha(B.Steel, 0.35));
      }
    },
    Timer(g) {
      g.clay(rr(42, 8, 16, 11, 4), B.Coral, 3);
      g.clay(rr(68, 22, 12, 9, 4), B.Coral, 3);
      g.clay(circle(50, 56, 32), B.Sky);
      g.flat(circle(50, 56, 24), B.Paper);
      g.line(B.Ink, 5, [50, 56], [50, 40]);
      g.line(B.Coral, 5, [50, 56], [62, 62]);
      g.dot(B.Ink, 50, 56, 4);
    },
    Clipboard(g) {
      g.clay(rr(16, 16, 68, 72, 12), B.Violet);
      g.flat(rr(25, 26, 50, 54, 6), B.Paper);
      g.clay(rr(36, 10, 28, 14, 6), B.Steel, 3);
      for (let i = 0; i <= 2; i++) {
        const y = 38 + i * 14;
        g.line(B.Green, 4.5, [31, y], [35, y + 4], [41, y - 3]);
        g.line(B.Steel, 4, [48, y + 1], [67, y + 1]);
      }
    },
    Notebook(g) {
      g.clay(rr(20, 10, 60, 76, 10), B.Green);
      g.flat(rr(20, 10, 13, 76, 6), lerp(B.Green, B.Ink, 0.25));
      g.flat(rr(42, 26, 28, 14, 4), B.Paper);
      g.line(B.Steel, 2.5, [47, 33], [64, 33]);
      g.clay(poly(58, 78, 72, 78, 72, 96, 65, 90, 58, 96), B.Coral, 2, 0, false);
    },
    Bookmark(g) { g.clay(poly(28, 12, 72, 12, 72, 86, 50, 70, 28, 86), B.Coral, 6, 8); },
    Cap(g) {
      g.clay(rr(28, 42, 44, 28, 10), lerp(B.Indigo, B.Ink, 0.3));
      g.clay(poly(50, 16, 92, 36, 50, 54, 8, 36), B.Indigo, 5, 6);
      g.line(B.Amber, 4, [50, 35], [80, 44], [80, 62]);
      g.dot(B.Amber, 80, 65, 5);
    },
    Check(g) {
      g.clay(circle(50, 50, 38), B.Green);
      g.line(B.Paper, 10, [32, 51], [45, 63], [68, 38]);
    },
    Flag(g) {
      g.clay(rr(18, 10, 9, 80, 4.5), B.Steel, 3);
      g.clay(path("M27 14C45 6 60 24 82 16L74 34L84 50C62 58 46 40 27 50Z", [27, 10, 84, 54]), B.Coral, 5, 5);
    },
    Lock(g) {
      g.stroke(arch(50, 44, 20), g.dark(B.Steel), 10);
      g.stroke(arch(50, 41, 20), g.c(B.Steel), 9);
      g.clay(rr(16, 40, 68, 46, 12), B.Amber);
      g.dot(B.Ink, 50, 58, 6);
      g.line(B.Ink, 5, [50, 60], [50, 71]);
    },
    Star(g) { g.clay(star5(50, 52, 40, 18), B.Amber, 6, 8); },
    Trophy(g) {
      g.push(`<circle cx="24" cy="34" r="13" fill="none" stroke="${css(g.dark(B.Amber))}" stroke-width="7"/>`);
      g.push(`<circle cx="76" cy="34" r="13" fill="none" stroke="${css(g.dark(B.Amber))}" stroke-width="7"/>`);
      g.clay(rr(30, 76, 40, 12, 4), B.Brown, 3);
      g.clay(rr(44, 58, 12, 20, 3), lerp(B.Amber, NIGHT, 0.1), 2, 0, false);
      g.clay(path("M24 14L76 14L74 36C72 54 62 62 50 62C38 62 28 54 26 36Z", [24, 14, 76, 62]), B.Amber, 6, 4);
      g.clay(star5(50, 36, 11, 5), B.Paper, 2, 0, false);
    },
    Medal(g) {
      g.clay(poly(30, 8, 46, 8, 56, 44, 42, 46), B.Sky, 2, 3, false);
      g.clay(poly(70, 8, 54, 8, 44, 44, 58, 46), B.Coral, 2, 3, false);
      g.clay(circle(50, 62, 25), B.Amber);
      g.clay(star5(50, 62, 12, 5.5), B.Paper, 2, 0, false);
    },
    Bulb(g) {
      for (const a of [-60, -25, 25, 60]) {
        const r = (a - 90) * Math.PI / 180;
        g.line(B.Amber, 4, [50 + 38 * Math.cos(r), 40 + 38 * Math.sin(r)], [50 + 46 * Math.cos(r), 40 + 46 * Math.sin(r)]);
      }
      g.clay(rr(38, 60, 24, 24, 7), B.Steel, 3);
      g.clay(circle(50, 42, 25), B.Amber);
      g.line(B.Paper, 4, [43, 50], [50, 42], [57, 50]);
    },
    Warning(g) {
      g.clay(poly(50, 14, 88, 80, 12, 80), B.Amber, 6, 12);
      g.line(B.Ink, 8, [50, 38], [50, 58]);
      g.dot(B.Ink, 50, 70, 4.5);
    },
    Fire(g) {
      g.clay(flame(50, 8, 36, 80), B.Coral, 6, 2);
      g.clay(flame(50, 38, 20, 50), B.Amber, 2, 0, false);
    },
    Bolt(g) { g.clay(poly(58, 8, 24, 54, 46, 54, 40, 90, 76, 42, 54, 42), B.Amber, 6, 6); },
    Heart(g) { g.clay(path("M50 84C14 60 6 40 16 26C26 12 44 16 50 30C56 16 74 12 84 26C94 40 86 60 50 84Z", [10, 16, 90, 84]), B.Pink); },
    Bell(g) {
      g.dot(B.Brown, 50, 82, 8);
      g.clay(path("M50 12C30 12 24 30 24 48C24 60 14 66 14 72L86 72C86 66 76 60 76 48C76 30 70 12 50 12Z", [14, 12, 86, 72]), B.Amber, 6, 4);
      g.clay(circle(50, 11, 5), B.Brown, 2, 0, false);
    },
    Chart(g) {
      g.clay(rr(12, 52, 20, 34, 6), B.Sky);
      g.clay(rr(40, 34, 20, 52, 6), B.Indigo);
      g.clay(rr(68, 14, 20, 72, 6), B.Green);
    },
    Cycle(g) {
      g.clay(circle(50, 50, 38), B.Sky);
      g.arc(B.Paper, 200, 230, 30, 30, 40, 40, 8);
      g.flat(poly(60, 22, 76, 30, 62, 40), B.Paper);
    },
    Target(g) {
      g.clay(circle(46, 54, 34), B.Coral);
      g.flat(circle(46, 54, 24), B.Paper);
      g.flat(circle(46, 54, 14), B.Coral);
      g.flat(circle(46, 54, 5), B.Paper);
      g.line(B.Ink, 5, [48, 52], [82, 18]);
      g.clay(poly(78, 10, 92, 8, 90, 22), B.Green, 2, 0, false);
    },
    Folder(g) {
      g.clay(poly(10, 20, 38, 20, 46, 28, 90, 28, 90, 80, 10, 80), lerp(B.Amber, NIGHT, 0.12), 2, 8, false);
      g.flat(rr(18, 32, 62, 20, 4), B.Paper);
      g.clay(poly(8, 42, 92, 42, 88, 84, 12, 84), B.Amber, 6, 8);
    },
    Gear(g) {
      let d = "";
      for (let i = 0; i < 8; i++) d += rrD(43, 10, 14, 20, 4, { deg: i * 45, cx: 50, cy: 50 });
      d += circD(50, 50, 30);
      g.clay(path(d, [10, 10, 90, 90]), B.Steel);
      g.clay(circle(50, 50, 13), B.Paper, -3, 0, false);
    },
    Help(g) {
      g.clay(bubble(), B.Sky);
      g.arc(B.Paper, 190, 250, 40, 22, 20, 20, 7);
      g.line(B.Paper, 7, [51, 42], [50, 49]);
      g.dot(B.Paper, 50, 59, 4.5);
    },
    Person(g) {
      g.clay(rr(18, 54, 64, 34, 18), B.Indigo);
      g.clay(circle(50, 32, 18), B.Amber);
    },
    Shield(g) {
      g.clay(path("M50 10L84 22C84 56 70 76 50 88C30 76 16 56 16 22Z", [16, 10, 84, 88]), B.Green, 6, 6);
      g.line(B.Paper, 8, [36, 48], [46, 58], [64, 38]);
    },
    Cards(g) {
      g.group("rotate(-12 50 50)", () => g.clay(rr(18, 18, 46, 62, 8), B.Sky));
      g.group("rotate(8 50 50)", () => {
        g.clay(rr(36, 20, 46, 62, 8), B.Paper);
        g.flat(rr(43, 30, 32, 12, 4), B.Indigo);
        g.line(B.Steel, 3.5, [44, 54], [74, 54]);
        g.line(B.Steel, 3.5, [44, 64], [66, 64]);
      });
    },
    Dice(g) {
      g.group("rotate(-8 50 50)", () => {
        g.clay(rr(16, 16, 66, 66, 16), B.Coral);
        for (const [x, y] of [[33, 33], [65, 33], [49, 49], [33, 65], [65, 65]]) g.dot(B.Paper, x, y, 5.5);
      });
    },
    Play(g) {
      g.clay(circle(50, 50, 38), B.Coral);
      g.clay(poly(42, 34, 68, 50, 42, 66), B.Paper, 2, 6, false);
    },
    Cloud(g) {
      g.clay(path(ovalD(14, 42, 50, 78) + ovalD(30, 22, 74, 66) + ovalD(52, 40, 88, 76) + rrD(30, 52, 42, 26, 4), [14, 22, 88, 78]), B.Sky);
    },
    Doc(g) {
      g.clay(poly(20, 10, 62, 10, 80, 28, 80, 88, 20, 88), B.Paper, 6, 6);
      g.flat(poly(62, 10, 80, 28, 62, 28), alpha(B.Steel, 0.5));
      g.flat(rr(28, 38, 30, 8, 4), B.Sky);
      for (let i = 0; i <= 2; i++) g.line(B.Steel, 3.5, [30, 56 + i * 10], [70 - i * 8, 56 + i * 10]);
    },
    Pencil(g) {
      g.group("rotate(45 50 50)", () => {
        g.clay(rr(38, 12, 24, 14, 5), B.Pink, 3);
        g.clay(rr(38, 24, 24, 46, 3), B.Amber);
        g.clay(poly(38, 70, 62, 70, 50, 92), hex("#F1D2A8"), 2, 2, false);
        g.flat(poly(46, 85, 54, 85, 50, 92), B.Ink);
      });
    },
    Sun(g) {
      for (let i = 0; i < 8; i++) {
        const r = i * 45 * Math.PI / 180;
        g.line(B.Amber, 6, [50 + 32 * Math.cos(r), 50 + 32 * Math.sin(r)], [50 + 42 * Math.cos(r), 50 + 42 * Math.sin(r)]);
      }
      g.clay(circle(50, 50, 22), B.Amber);
    },
    Moon(g) { g.clay(path("M60 12C30 14 14 40 22 62C30 84 62 94 84 72C56 74 40 44 60 12Z", [18, 12, 84, 90]), B.Violet); },
    Phone(g) {
      g.clay(rr(26, 8, 48, 82, 12), B.Ink);
      g.flat(rr(31, 18, 38, 58, 5), B.Sky);
      g.dot(B.Steel, 50, 82, 3.5);
    },
    Gem(g) {
      g.clay(poly(28, 18, 72, 18, 90, 38, 50, 86, 10, 38), B.Sky, 6, 4);
      g.line(alpha(B.Paper, 0.7), 3, [12, 38], [88, 38]);
      g.line(alpha(B.Paper, 0.5), 3, [38, 18], [32, 38], [50, 84]);
    },
    Brain(g) {
      g.clay(path(ovalD(12, 28, 50, 72) + ovalD(50, 28, 88, 72) + ovalD(24, 14, 56, 46) + ovalD(44, 14, 76, 46) + ovalD(26, 50, 74, 84), [12, 14, 88, 84]), B.Pink);
      const vein = lerp(B.Pink, B.Ink, 0.35);
      g.line(vein, 3.5, [50, 22], [50, 80]);
      g.line(vein, 3, [26, 48], [38, 52], [38, 62]);
      g.line(vein, 3, [74, 48], [62, 52], [62, 62]);
    },
    Trap(g) {
      g.clay(rr(8, 58, 84, 24, 8), B.Brown);
      g.line(lerp(B.Brown, B.Ink, 0.3), 2.5, [16, 66], [84, 66]);
      g.stroke(arch(40, 62, 18), g.dark(B.Steel), 5);
      g.stroke(arch(40, 60, 18), g.c(B.Steel), 4);
      g.line(B.Steel, 4, [40, 60], [40, 52]);
      g.clay(poly(56, 58, 90, 58, 90, 34), B.Amber, 4, 4);
      g.dot(lerp(B.Amber, B.Brown, 0.45), 80, 50, 3);
      g.dot(lerp(B.Amber, B.Brown, 0.45), 70, 54, 2.2);
    },
  };

  // Nome do Material Symbols → desenho (mesma tabela de BrandIcon.kt, nos nomes do site).
  const MAP = {};
  const add = (glyph, ...names) => names.forEach((name) => { MAP[name] = glyph; });
  add("House", "home");
  add("Books", "library_books");
  add("OpenBook", "menu_book", "auto_stories", "article", "toc", "chrome_reader_mode");
  add("Calendar", "calendar_month", "event", "event_available", "event_repeat", "event_busy", "date_range", "today");
  add("Timer", "timer", "schedule", "hourglass_top", "pending_actions", "history", "alarm");
  add("Clipboard", "quiz", "fact_check", "checklist", "assignment", "rule", "list_alt");
  add("Notebook", "bookmarks");
  add("Bookmark", "bookmark", "bookmark_border", "bookmark_added");
  add("Cap", "school");
  add("Pencil", "auto_awesome", "edit_note", "border_color", "edit");
  add("Brain", "psychology");
  add("Check", "check_circle", "task_alt", "verified");
  add("Flag", "flag", "outlined_flag", "directions_run", "sports_score");
  add("Lock", "lock");
  add("Star", "star", "star_border", "grade");
  add("Trophy", "emoji_events");
  add("Medal", "workspace_premium", "military_tech");
  add("Bulb", "lightbulb", "tips_and_updates");
  add("Warning", "warning", "warning_amber", "error_outline", "report_problem", "error", "error_med", "priority_high");
  add("Fire", "local_fire_department", "whatshot");
  add("Bolt", "bolt", "offline_bolt", "speed", "electric_bolt");
  add("Heart", "favorite", "favorite_border");
  add("Bell", "notifications", "notifications_active");
  add("Chart", "query_stats", "insights", "pie_chart", "timeline", "trending_up", "trending_down", "bar_chart", "monitoring", "leaderboard");
  add("Cycle", "autorenew", "replay", "restore", "sync", "refresh", "cached");
  add("Target", "gps_fixed", "center_focus_strong", "explore", "track_changes", "my_location");
  add("Folder", "folder_open", "folder", "inbox", "archive", "unarchive");
  add("Gear", "settings", "tune");
  add("Help", "help_outline", "help", "info", "contact_support");
  add("Person", "person", "account_circle");
  add("Shield", "shield", "verified_user", "security");
  add("Cards", "style");
  add("Dice", "shuffle", "casino");
  add("Play", "play_circle", "play_circle_outline", "ondemand_video");
  add("Cloud", "cloud_sync", "cloud_done", "cloud_download", "cloud_upload", "cloud_off", "cloud");
  add("Doc", "description", "upload_file", "file_open", "picture_as_pdf", "summarize", "article_shortcut");
  add("Sun", "light_mode", "wb_sunny");
  add("Moon", "dark_mode", "bedtime", "nights_stay");
  add("Phone", "devices", "phone_android", "smartphone", "computer");
  add("Gem", "fiber_new", "diamond");
  add("Trap", "pest_control_rodent");

  const cache = new Map();
  window.estudarioGlyphName = (name) => MAP[name] || (GLYPHS[name] ? name : null);
  window.estudarioGlyph = function (name, muted) {
    const glyph = MAP[name] || (GLYPHS[name] ? name : null);
    if (!glyph) return null;
    const key = glyph + (muted ? ":m" : "");
    let svg = cache.get(key);
    if (!svg) {
      const g = painter(!!muted);
      g.shadow();
      GLYPHS[glyph](g);
      svg = g.svg();
      cache.set(key, svg);
    }
    return svg;
  };
  /** Mesmo desenho como data URI, pronto para <img src>. */
  const uris = new Map();
  window.estudarioGlyphUri = function (name, muted) {
    const key = name + (muted ? ":m" : "");
    if (uris.has(key)) return uris.get(key);
    const svg = window.estudarioGlyph(name, muted);
    const uri = svg ? "data:image/svg+xml;charset=utf-8," + encodeURIComponent(svg) : null;
    uris.set(key, uri);
    return uri;
  };
})();
