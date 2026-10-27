// A turma do Folha: a fonte única dos ícones do Estudário (app e site).
//
// Linguagem: adesivo de caderno (recorte branco, traço de caneta, aba de profundidade embaixo, brilho de
// marca-texto) + o rosto do mascote Folha (olhos ovais com brilho, bochecha rosada, boquinha). Cada
// ícone é um personagem cuja expressão diz o que ele faz.
//
// Saída de build(): { Nome: { anim, body: [prim], face: [prim], faceSmall: [prim], fx: [prim] } }
// prim = { d, f?, s?, w?, o?, dy? }  (d: caminho SVG na grade 100 x 100; f: preenchimento; s: traço;
// w: largura do traço; o: opacidade; dy: deslocamento vertical, usado na aba de profundidade).
// body leva o recorte branco (halo) e as abas; face é o rosto completo, faceSmall o rosto do tamanho
// de menu; fx só aparece na animação do site (confete, fumaça, faíscas).
// Rodar: deno run --allow-read --allow-write scripts/icones/gerar.ts

export const VIEWBOX = [-8, -10, 116, 116];
export const INK = "#26234A";
const CHEEK = "#FF8FA3";
const WHITE = "#FFFFFF";
export const C = {
  indigo: ["#5B4BF5", "#3E2FC9"], green: ["#22C27B", "#13965A"], amber: ["#FFB421", "#E08A00"],
  coral: ["#FF5C4D", "#D9392B"], sky: ["#38B2FF", "#1884CF"], violet: ["#9563FF", "#6D3CE0"],
  paper: ["#FFFDF8", "#E3DACB"], sun: ["#FFD84A", "#F0B400"], glass: ["#D9F2FF", "#A9DCF7"],
  orange: ["#FF8A2B", "#E0640A"], skin: ["#FFC9A0", "#E8A27A"], pink: ["#FF7BB0", "#D94E8A"],
  brown: ["#C98A4B", "#9A6430"], steel: ["#B7B4D0", "#8B87AD"], teal: ["#3FD9D0", "#18A79F"],
  cloud: ["#E9F6FF", "#B8DDF5"], moon: ["#FFE38A", "#E8B93A"], wood: ["#F7D9A8", "#D9AE6A"],
};
const f1 = (v) => +v.toFixed(2);

// ------------------------------------------------------------------ formas
const ell = (cx, cy, rx, ry = rx) =>
  `M${f1(cx - rx)} ${f1(cy)}A${f1(rx)} ${f1(ry)} 0 1 0 ${f1(cx + rx)} ${f1(cy)}A${f1(rx)} ${f1(ry)} 0 1 0 ${f1(cx - rx)} ${f1(cy)}Z`;
const rr = (x, y, w, h, r) => {
  r = Math.min(r, w / 2, h / 2);
  return `M${x + r} ${y}H${x + w - r}A${r} ${r} 0 0 1 ${x + w} ${y + r}V${y + h - r}A${r} ${r} 0 0 1 ${x + w - r} ${y + h}` +
    `H${x + r}A${r} ${r} 0 0 1 ${x} ${y + h - r}V${y + r}A${r} ${r} 0 0 1 ${x + r} ${y}Z`;
};
const poly = (...v) => { let d = `M${v[0]} ${v[1]}`; for (let i = 2; i < v.length; i += 2) d += `L${v[i]} ${v[i + 1]}`; return d + "Z"; };
const star = (cx, cy, outer, inner, n = 5) => {
  let d = "";
  for (let i = 0; i < n * 2; i++) {
    const r = i % 2 ? inner : outer, a = (-90 + i * 180 / n) * Math.PI / 180;
    d += `${i ? "L" : "M"}${f1(cx + r * Math.cos(a))} ${f1(cy + r * Math.sin(a))}`;
  }
  return d + "Z";
};
function gear(cx, cy, outer, inner, teeth) {
  let d = "";
  const step = Math.PI * 2 / teeth;
  for (let i = 0; i < teeth; i++) {
    const a = i * step - Math.PI / 2;
    const pts = [[inner, a - step * 0.32], [outer, a - step * 0.2], [outer, a + step * 0.2], [inner, a + step * 0.32]];
    for (const [r, t] of pts) d += `${d ? "L" : "M"}${f1(cx + r * Math.cos(t))} ${f1(cy + r * Math.sin(t))}`;
  }
  return d + "Z";
}

// ------------------------------------------------------------------ camadas do corpo
// solid: cor com aba embaixo e traço de caneta. flat: sem aba. line: só traço. shine: brilho.
const solid = (d, col, o = {}) => ({ kind: "solid", d, col, ...o });
const flat = (d, col, o = {}) => ({ kind: "flat", d, col, ...o });
const line = (d, col, w = 5, o = {}) => ({ kind: "line", d, col, w, ...o });
const paint = (d, fill, o = {}) => ({ kind: "paint", d, fill, ...o }); // sem traço nem halo
const shine = (d) => ({ kind: "shine", d });
// Linha com contorno de caneta (traço escuro largo + cor por cima).
const inkLine = (d, col, w = 8) => [line(d, "ink", w + 7), line(d, col, w, { noHalo: true })];

function colorOf(col) { return col === "ink" ? [INK, INK] : (C[col] || [col, col]); }

function toPrims(layers) {
  const halo = [], body = [];
  for (const l of layers.flat()) {
    const w = l.w ?? 5;
    if (!l.noHalo && l.kind !== "shine" && l.kind !== "paint") halo.push({ d: l.d, f: l.kind === "line" ? null : WHITE, s: WHITE, w: w + 11 });
    const [base, dark] = colorOf(l.col);
    if (l.kind === "line") body.push({ d: l.d, s: base, w });
    else if (l.kind === "shine") body.push({ d: l.d, s: WHITE, w: 4.5, o: 0.55 });
    else if (l.kind === "paint") body.push({ d: l.d, f: l.fill, o: l.o });
    else {
      if (l.kind === "solid" && l.lip !== false) body.push({ d: l.d, f: dark, s: INK, w, dy: l.lipY ?? 5 });
      body.push({ d: l.d, f: base, s: INK, w });
    }
  }
  return [...halo, ...body];
}

// ------------------------------------------------------------------ rosto (língua do Folha)
// mood: smile | grin | happy | wink | calm | sleepy | oops | curious | fierce | star | proud | worried
// opts: { mouth: false } para usar outra coisa como boca (o ✓ do Check, a fechadura do Lock).
function face(cx, cy, s, mood, detail, opts = {}) {
  const P = [];
  const dx = 9 * s, ey = cy, my = cy + 9 * s;
  const stroke = (d, w) => P.push({ d, s: INK, w: w * s });
  const fill = (d, color = INK, o) => P.push({ d, f: color, o });
  if (detail && !["oops", "worried"].includes(mood) && opts.cheeks !== false) {
    fill(ell(cx - dx - 4 * s, ey + 6 * s, 4.6 * s, 2.6 * s), CHEEK, 0.75);
    fill(ell(cx + dx + 4 * s, ey + 6 * s, 4.6 * s, 2.6 * s), CHEEK, 0.75);
  }
  const arc = (x, up) => stroke(`M${f1(x - 4.4 * s)} ${f1(ey + (up ? 1.5 : -1) * s)}Q${f1(x)} ${f1(ey + (up ? -5 : 4) * s)} ${f1(x + 4.4 * s)} ${f1(ey + (up ? 1.5 : -1) * s)}`, 2.8);
  const oval = (x, k = 1) => {
    if (!detail) { fill(ell(x, ey, 3.7 * s * k, 4.7 * s * k)); return; }
    fill(ell(x, ey, 4.3 * s * k, 5.6 * s * k));
    fill(ell(x - 1.4 * s, ey - 2.2 * s, 1.8 * s * k), WHITE);
    fill(ell(x + 1.6 * s, ey + 1.8 * s, 0.8 * s), WHITE, 0.75);
  };
  if (mood === "happy") { arc(cx - dx, true); arc(cx + dx, true); }
  else if (mood === "calm" || mood === "sleepy") { arc(cx - dx, false); arc(cx + dx, false); }
  else if (mood === "wink") { oval(cx - dx); arc(cx + dx, true); }
  else if (mood === "star") {
    fill(star(cx - dx, ey, 6.4 * s, 2.7 * s)); fill(star(cx + dx, ey, 6.4 * s, 2.7 * s));
    if (detail) { fill(ell(cx - dx - 1, ey - 1.4 * s, 1.2 * s), WHITE); fill(ell(cx + dx - 1, ey - 1.4 * s, 1.2 * s), WHITE); }
  } else { const k = mood === "oops" || mood === "curious" ? 1.15 : 1; oval(cx - dx, k); oval(cx + dx, k); }
  if (mood === "fierce") stroke(`M${f1(cx - dx - 6 * s)} ${f1(ey - 9 * s)}L${f1(cx - dx + 4 * s)} ${f1(ey - 6 * s)}M${f1(cx + dx + 6 * s)} ${f1(ey - 9 * s)}L${f1(cx + dx - 4 * s)} ${f1(ey - 6 * s)}`, 3);
  if (mood === "worried") stroke(`M${f1(cx - dx - 5 * s)} ${f1(ey - 7 * s)}L${f1(cx - dx + 4 * s)} ${f1(ey - 10 * s)}M${f1(cx + dx + 5 * s)} ${f1(ey - 7 * s)}L${f1(cx + dx - 4 * s)} ${f1(ey - 10 * s)}`, 2.6);
  if (opts.mouth === false) return P;
  if (mood === "oops" || mood === "curious") {
    fill(ell(cx, my + 1 * s, 3 * s, 3.6 * s));
    if (mood === "oops" && detail) P.push({ d: `M${f1(cx + dx + 9 * s)} ${f1(ey - 12 * s)}q${f1(4 * s)} ${f1(6 * s)} 0 ${f1(9 * s)}q${f1(-4 * s)} ${f1(-3 * s)} 0 ${f1(-9 * s)}z`, f: "#7FD3FF", s: INK, w: 1.6 * s });
  } else if (mood === "worried") {
    stroke(`M${f1(cx - 4 * s)} ${f1(my + 2 * s)}Q${f1(cx)} ${f1(my - 2 * s)} ${f1(cx + 4 * s)} ${f1(my + 2 * s)}`, 2.6);
  } else if (mood === "calm" || mood === "sleepy") {
    stroke(`M${f1(cx - 3.5 * s)} ${f1(my)}Q${f1(cx)} ${f1(my + 3 * s)} ${f1(cx + 3.5 * s)} ${f1(my)}`, 2.6);
  } else if (mood === "smile" || mood === "proud" || mood === "fierce" && opts.smile) {
    stroke(`M${f1(cx - 5 * s)} ${f1(my - 1 * s)}Q${f1(cx)} ${f1(my + 5 * s)} ${f1(cx + 5 * s)} ${f1(my - 1 * s)}`, 2.7);
  } else {
    fill(`M${f1(cx - 6.5 * s)} ${f1(my - 2 * s)}Q${f1(cx)} ${f1(my + 9 * s)} ${f1(cx + 6.5 * s)} ${f1(my - 2 * s)}Z`);
    if (detail) fill(ell(cx, my + 3.6 * s, 3 * s, 1.4 * s), CHEEK);
  }
  return P;
}
const fxLine = (d, color, w = 3.5) => ({ d, s: color, w });
const fxFill = (d, color, o) => ({ d, f: color, s: INK, w: 2, o });

// a mão (Mostrar e Acenar): quatro dedos, o polegar e a palma num contorno só
const HAND = "M30 62V28a6 6 0 0 1 12 0V40V18a6 6 0 0 1 12 0V40V20a6 6 0 0 1 12 0V40V30a6 6 0 0 1 12 0V66a24 24 0 0 1-24 24H50a20 20 0 0 1-15-7L15 64a6 6 0 0 1 9-8l6 7z";

// ------------------------------------------------------------------ a turma
// anim: hop | wiggle | spin | flicker | cheer | pulse | zap | sway | float | write | ring | roll | peek
export const TURMA = {
  House: { anim: "hop", about: "Início: casinha acolhedora", draw: () => ({
    body: [
      solid("M64 16h12v24H64z", "coral"),
      solid("M22 48v34a6 6 0 0 0 6 6h44a6 6 0 0 0 6-6V48L50 24z", "paper"),
      solid("M8 52 50 14l42 38a5 5 0 0 1-6.6 7.5L50 27.5 14.6 59.5A5 5 0 0 1 8 52z", "coral", { lipY: 4 }),
      flat("M43 88V78a7 7 0 0 1 14 0v10z", "indigo", { w: 4 }),
      shine("M20 50 46 27"),
    ],
    face: [50, 60, 1, "smile"],
    fx: [fxFill("M70 10q-5-7 2-11 7 3 1 11z", "#FFFFFF"), fxFill("M80 2q-4-5 2-8 5 3 0 8z", "#FFFFFF")],
  }) },
  Folder: { anim: "peek", about: "Meus concursos: pasta caçadora de editais", draw: () => ({
    body: [
      flat("M12 30a6 6 0 0 1 6-6h22l8 8h34a6 6 0 0 1 6 6v42a6 6 0 0 1-6 6H18a6 6 0 0 1-6-6z", "amber"),
      flat("M26 48 28 12l44 3-2 33z", "paper"),
      line("M35 22h26M34 30h20", "#B7AFCC", 4, { noHalo: true }),
      flat("M58 14v16l6-5 6 5V15", "coral", { w: 4 }),
      solid("M10 46a6 6 0 0 1 6-6h68a6 6 0 0 1 5.9 7l-5 33a6 6 0 0 1-6 5H20a6 6 0 0 1-6-5z", "amber"),
    ],
    face: [50, 60, 1, "proud"],
  }) },
  Calendar: { anim: "hop", about: "Plano de estudos: calendário organizado", draw: () => ({
    body: [
      solid("M12 32a12 12 0 0 1 12-12h52a12 12 0 0 1 12 12v46a12 12 0 0 1-12 12H24a12 12 0 0 1-12-12z", "paper"),
      flat("M12 40v-8a12 12 0 0 1 12-12h52a12 12 0 0 1 12 12v8z", "indigo"),
      ...inkLine("M31 8v18M69 8v18", "paper", 3),
      solid(ell(83, 80, 13), "green"),
      line("M77 80l4.5 4.5L89 76", WHITE, 4.5, { noHalo: true }),
    ],
    face: [46, 60, 1, "wink"],
  }) },
  Target: { anim: "zap", about: "Treinar questões: alvo que não erra", draw: () => ({
    body: [
      solid(ell(48, 56, 38), "coral"),
      flat(ell(48, 56, 26), "paper"),
      ...inkLine("M66 38 88 16", "paper", 4),
      flat("M84 20 82 6 94 0 96 10 92 14 100 16 94 28 80 26z", "amber", { w: 4.5 }),
    ],
    face: [48, 56, 0.95, "fierce", { smile: true }],
    fx: [fxLine("M70 46l8 2M70 36l6-4M62 30l2-7", "#FFB421")],
  }) },
  Notebook: { anim: "hop", about: "Caderno de estudo: caderno nerd", draw: () => ({
    body: [
      solid("M24 12a8 8 0 0 1 8-8h46a8 8 0 0 1 8 8v74a8 8 0 0 1-8 8H32a8 8 0 0 1-8-8z", "green"),
      ...inkLine("M16 20h14M16 36h14M16 52h14M16 68h14M16 84h14", "paper", 2.4),
      flat("M42 14h32v12H42z", "paper", { w: 4 }),
      shine("M80 30v26"),
    ],
    face: [57, 58, 1, "smile", { cheeks: false }],
    extra: (detail) => [
      { d: ell(48, 58, 8.5), f: "rgba(255,255,255,.35)", s: INK, w: detail ? 3 : 3.6 },
      { d: ell(66, 58, 8.5), f: "rgba(255,255,255,.35)", s: INK, w: detail ? 3 : 3.6 },
      { d: "M56.5 57h1", s: INK, w: 3 },
    ],
  }) },
  Cycle: { anim: "spin", about: "Revisões espaçadas: cartão que volta na hora certa", draw: () => ({
    body: [
      ...inkLine("M18 58A34 34 0 0 1 72 23M82 42A34 34 0 0 1 28 77", "#22C27B", 8),
      flat("M83 33 80 16 66 31z", "green", { w: 4.5 }),
      flat("M17 67 20 84 34 69z", "green", { w: 4.5 }),
      solid("M32 30a8 8 0 0 1 8-8h20a8 8 0 0 1 8 8v40a8 8 0 0 1-8 8H40a8 8 0 0 1-8-8z", "sky"),
      line("M41 68h18", WHITE, 4, { noHalo: true }),
    ],
    face: [50, 46, 0.85, "happy"],
  }) },
  Oops: { anim: "wiggle", about: "Caderno de erros: o “opa” que vira acerto", draw: () => ({
    body: [
      solid("M22 14a4 4 0 0 1 4-4h36l18 18v58a4 4 0 0 1-4 4H26a4 4 0 0 1-4-4z", "paper"),
      flat("M62 10v18h18z", "#E3DACB", { w: 4 }),
      solid(ell(74, 76, 14), "coral"),
      line("M69 71l10 10M79 71 69 81", WHITE, 4.5, { noHalo: true }),
    ],
    face: [44, 46, 1, "oops"],
  }) },
  Timer: { anim: "flip", about: "Modo foco: ampulheta concentrada", draw: () => ({
    body: [
      flat("M28 20h44q0 22-17 30 17 8 17 30H28q0-22 17-30-17-8-17-30z", "glass"),
      paint("M50 56q12 7 15 20H35q3-13 15-20z", C.sun[0]),
      line("M28 20h44q0 22-17 30 17 8 17 30H28q0-22 17-30-17-8-17-30z", "ink", 5, { noHalo: true }),
      solid(rr(18, 7, 64, 14, 5), "violet", { lipY: 3 }),
      solid(rr(18, 75, 64, 14, 5), "violet", { lipY: 3 }),
      line("M14 40q0-20 36-22 36 2 36 22", "ink", 6),
      flat("M8 36a6 6 0 0 1 6-6h4v20h-4a6 6 0 0 1-6-6z", "coral", { w: 4.5 }),
      flat("M82 30h4a6 6 0 0 1 6 6v8a6 6 0 0 1-6 6h-4z", "coral", { w: 4.5 }),
    ],
    face: [50, 34, 0.75, "calm"],
    fx: [fxLine("M92 14l6-4M96 22h7", "#9563FF", 3)],
  }) },
  Fire: { anim: "flicker", about: "Sequência: chama que não apaga", draw: () => ({
    body: [
      solid("M50 4c10 16 34 26 34 56 0 18-15 32-34 32S16 78 16 60c0-14 8-22 14-30 2 10 6 14 10 14-2-14 2-28 10-40z", "orange"),
      flat("M50 46c9 9 18 16 18 27 0 10-8 17-18 17s-18-7-18-17c0-11 9-16 18-27z", "sun", { w: 4 }),
    ],
    face: [50, 68, 0.85, "fierce"],
    fx: [fxFill(star(14, 20, 6, 2.5, 4), "#FFD84A"), fxFill(star(88, 30, 5, 2, 4), "#FFD84A")],
  }) },
  Bolt: { anim: "zap", about: "XP: raio animado", draw: () => ({
    body: [solid("M62 4 18 58h26l-8 38 48-58H58l10-34z", "amber"), shine("M54 18 36 42")],
    face: [51, 50, 0.72, "star"],
    fx: [fxLine("M8 30l8 4M10 74l8-6M92 60l-8 2", "#FFB421")],
  }) },
  Trophy: { anim: "cheer", about: "Conquistas: troféu orgulhoso", draw: () => ({
    body: [
      line("M26 24q-16 0-16 13 0 15 20 17M74 24q16 0 16 13 0 15-20 17", "ink", 6),
      solid("M24 8h52v30a26 26 0 0 1-52 0z", "amber"),
      flat("M43 64h14v10H43z", "amber"),
      solid("M26 78a6 6 0 0 1 6-6h36a6 6 0 0 1 6 6v10H26z", "indigo"),
      shine("M32 16v18"),
    ],
    face: [50, 32, 0.9, "happy"],
    fx: [
      { d: rr(4, 0, 6, 10, 2), f: "#FF5C4D" }, { d: rr(90, 4, 6, 10, 2), f: "#22C27B" },
      { d: ell(14, 64, 3.5), f: "#38B2FF" }, { d: ell(88, 66, 3.5), f: "#FF7BB0" },
      { d: star(50, -6, 6, 2.6), f: "#FFD84A" },
    ],
  }) },
  Person: { anim: "hop", about: "Perfil: você, de capelo", draw: () => ({
    body: [
      solid("M16 92q0-30 34-30t34 30z", "indigo"),
      solid(ell(50, 42, 20), "skin"),
      solid("M50 6 90 21 50 36 10 21z", "indigo", { lipY: 4 }),
      line("M82 24v18", "ink", 4),
      flat(ell(82, 46, 5), "amber", { w: 3.5 }),
      line("M40 78h20", WHITE, 4.5, { noHalo: true }),
    ],
    face: [50, 46, 0.8, "smile"],
  }) },
  Books: { anim: "hop", about: "Biblioteca: estante de livros", draw: () => ({
    body: [
      solid(rr(10, 30, 22, 58, 5), "coral"),
      line("M14 42h14M14 76h14", WHITE, 3.5, { noHalo: true }),
      solid("M70 32 86 26 98 82 82 88z", "green"),
      solid(rr(34, 14, 32, 74, 6), "indigo"),
      flat(rr(40, 22, 20, 8, 3), "sun", { w: 3.5 }),
      line("M40 76h20", WHITE, 3.5, { noHalo: true }),
    ],
    face: [50, 48, 0.8, "smile"],
  }) },
  OpenBook: { anim: "hop", about: "Teoria: livro aberto", draw: () => ({
    body: [
      solid("M6 26V84Q28 78 50 90 72 78 94 84V26Q72 20 50 30 28 20 6 26z", "indigo"),
      flat("M50 30Q32 18 12 22V78Q32 74 50 84z", "paper"),
      flat("M50 30Q68 18 88 22V78Q68 74 50 84z", "paper"),
      line("M20 34q12-2 22 3M20 66q12-2 22 3M58 66q10-5 22-3", "#B7AFCC", 3.5, { noHalo: true }),
      flat("M70 12v26l6-5 6 5V12", "coral", { w: 3.5 }),
    ],
    face: [50, 52, 0.85, "smile"],
  }) },
  Clipboard: { anim: "wiggle", about: "Questões: prancheta da prova", draw: () => ({
    body: [
      solid(rr(16, 12, 68, 82, 10), "brown"),
      flat(rr(24, 22, 52, 64, 6), "paper"),
      solid(rr(36, 6, 28, 14, 5), "violet", { lipY: 3 }),
      line("M32 72l4 4 8-8M52 74h16", "#22C27B", 4, { noHalo: true }),
    ],
    face: [50, 44, 0.85, "grin"],
  }) },
  Bookmark: { anim: "hop", about: "Salvos: marcador", draw: () => ({
    body: [solid("M26 6h48v86L50 74 26 92z", "coral"), shine("M34 16v22")],
    face: [50, 38, 0.9, "happy"],
  }) },
  Cap: { anim: "hop", about: "Formação: capelo", draw: () => ({
    body: [
      solid("M22 42v24q28 18 56 0V42L50 54z", "indigo"),
      solid("M50 14 96 34 50 54 4 34z", "indigo", { lipY: 4 }),
      line("M84 39v22", "ink", 4),
      flat(ell(84, 66, 6), "amber", { w: 3.5 }),
    ],
    face: [50, 64, 0.62, "smile"],
  }) },
  Check: { anim: "hop", about: "Feito: o ✓ é o sorriso", draw: () => ({
    body: [solid(ell(50, 50, 40), "green"), shine("M22 34a32 32 0 0 1 16-16"), line("M32 58l12 12 24-24", WHITE, 9, { noHalo: true })],
    face: [50, 38, 0.85, "happy", { mouth: false }],
  }) },
  Flag: { anim: "sway", about: "Meta: bandeira", draw: () => ({
    body: [
      line("M22 8v86", "ink", 7),
      solid("M26 12q16-8 32 0t32 0v38q-16 8-32 0t-32 0z", "coral"),
      flat(ell(22, 8, 6), "amber", { w: 3.5 }),
    ],
    face: [56, 30, 0.8, "proud"],
  }) },
  Lock: { anim: "wiggle", about: "Bloqueado: cadeado", draw: () => ({
    body: [
      ...inkLine("M32 44V32a18 18 0 0 1 36 0v12", "steel", 8),
      solid(rr(16, 42, 68, 50, 14), "amber"),
      paint("M50 72a5 5 0 1 1 .1 0zM47 75h6l1 9h-8z", INK),
    ],
    face: [50, 58, 0.85, "worried", { mouth: false }],
  }) },
  Star: { anim: "spin", about: "Favorito: estrela", draw: () => ({
    body: [solid(star(50, 54, 44, 21), "amber", { lipY: 4 }), shine("M38 30 44 22")],
    face: [50, 56, 0.8, "happy"],
  }) },
  Medal: { anim: "sway", about: "Destaque: medalha", draw: () => ({
    body: [
      solid("M28 4h18l12 36H40z", "coral"),
      solid("M72 4H54L42 40h18z", "sky"),
      solid(ell(50, 64, 28), "amber"),
      flat(ell(50, 64, 20), "sun", { w: 3.5 }),
    ],
    face: [50, 64, 0.72, "proud"],
  }) },
  Bulb: { anim: "zap", about: "Dica: lâmpada com ideia", draw: () => ({
    body: [
      solid(rr(36, 70, 28, 20, 6), "steel"),
      line("M36 78h28", "ink", 3.5, { noHalo: true }),
      solid("M50 6a30 30 0 0 1 18 54v10H32V60A30 30 0 0 1 50 6z", "sun"),
      shine("M30 26a22 22 0 0 1 10-10"),
    ],
    face: [50, 36, 0.85, "grin"],
    fx: [fxLine("M6 30h8M86 30h8M16 6l6 6M84 6l-6 6", "#FFB421")],
  }) },
  Warning: { anim: "wiggle", about: "Atenção: placa preocupada", draw: () => ({
    body: [solid("M44 12a7 7 0 0 1 12 0l38 66a7 7 0 0 1-6 10H12a7 7 0 0 1-6-10z", "amber")],
    face: [50, 58, 0.8, "worried"],
  }) },
  Heart: { anim: "pulse", about: "Gostei: coração", draw: () => ({
    body: [solid("M50 88C20 66 8 50 8 34 8 20 18 10 31 10c9 0 15 5 19 12 4-7 10-12 19-12 13 0 23 10 23 24 0 16-12 32-42 54z", "coral"), shine("M18 28a12 12 0 0 1 10-8")],
    face: [50, 46, 0.85, "happy"],
    fx: [fxFill(ell(8, 8, 4), "#FF7BB0"), fxFill(ell(92, 12, 3), "#FF7BB0")],
  }) },
  Bell: { anim: "ring", about: "Avisos: sininho", draw: () => ({
    body: [
      solid(ell(50, 80, 8), "orange"),
      solid(ell(50, 8, 6), "amber"),
      solid("M50 12c18 0 28 14 28 32v16l10 12H12l10-12V44c0-18 10-32 28-32z", "amber"),
      shine("M30 40a20 20 0 0 1 8-16"),
    ],
    face: [50, 46, 0.85, "grin"],
    fx: [fxLine("M2 30q-4 10 0 20M98 30q4 10 0 20", "#FFB421")],
  }) },
  Chart: { anim: "hop", about: "Estatísticas: gráfico crescendo", draw: () => ({
    body: [
      solid(rr(8, 58, 24, 32, 6), "sky"),
      solid(rr(38, 38, 24, 52, 6), "violet"),
      solid(rr(68, 14, 24, 76, 6), "green"),
      shine("M74 22v14"),
    ],
    face: [80, 44, 0.55, "happy"],
    fx: [fxLine("M10 40 30 26 46 30 62 8", "#FFB421", 4)],
  }) },
  Gear: { anim: "spin", about: "Ajustes: engrenagem", draw: () => ({
    body: [solid(gear(50, 50, 44, 34, 9), "steel"), flat(ell(50, 50, 24), "paper", { w: 4 })],
    face: [50, 50, 0.72, "calm"],
  }) },
  Help: { anim: "wiggle", about: "Ajuda: balão curioso", draw: () => ({
    body: [
      solid(rr(8, 12, 78, 62, 22) + "M26 70l-6 20 22-16z", "sky"),
      solid(ell(82, 16, 13), "amber", { lipY: 3 }),
      line("M77 12q5-6 10-1t-5 8v3", "ink", 3.8, { noHalo: true }),
      paint(ell(82, 26.5, 2.3), INK),
    ],
    face: [46, 44, 0.85, "curious"],
  }) },
  Shield: { anim: "hop", about: "Segurança: escudo", draw: () => ({
    body: [solid("M50 6 86 18v30c0 22-16 36-36 44C30 84 14 70 14 48V18z", "green"), shine("M24 26v16")],
    face: [50, 46, 0.85, "proud"],
  }) },
  Cards: { anim: "flip", about: "Flashcards: cartas", draw: () => ({
    body: [
      solid("M14 22 54 12l14 62-40 10z", "violet"),
      solid(rr(40, 22, 50, 66, 9), "sky"),
      line("M50 76h30", WHITE, 4, { noHalo: true }),
    ],
    face: [65, 50, 0.8, "wink"],
  }) },
  Dice: { anim: "roll", about: "Sortear: dado", draw: () => ({
    body: [
      solid(rr(12, 12, 76, 76, 18), "paper"),
      paint(ell(28, 28, 5.5) + ell(72, 28, 5.5) + ell(28, 72, 5.5) + ell(72, 72, 5.5), C.coral[0]),
    ],
    face: [50, 48, 0.85, "grin"],
  }) },
  Play: { anim: "hop", about: "Vídeo: telinha", draw: () => ({
    body: [
      solid(rr(6, 14, 80, 62, 16), "coral"),
      line("M34 76l-6 14M58 76l6 14", "ink", 6),
      solid(ell(80, 72, 16), "paper", { lipY: 4 }),
      paint("M75 63v18l15-9z", C.coral[0]),
    ],
    face: [44, 42, 0.9, "grin"],
  }) },
  Cloud: { anim: "float", about: "Sincronizar: nuvem", draw: () => ({
    body: [solid("M28 82a20 20 0 0 1-3-40 26 26 0 0 1 50-6 22 22 0 0 1 3 46z", "cloud")],
    face: [50, 60, 0.85, "smile"],
    fx: [fxFill(ell(14, 18, 4), "#38B2FF"), fxFill(ell(88, 14, 3), "#38B2FF")],
  }) },
  Doc: { anim: "hop", about: "Documento: folha de papel", draw: () => ({
    body: [
      solid("M20 12a4 4 0 0 1 4-4h38l18 18v62a4 4 0 0 1-4 4H24a4 4 0 0 1-4-4z", "paper"),
      flat("M62 8v18h18z", "steel", { w: 4 }),
      line("M32 74h36M32 82h22", "#B7AFCC", 4, { noHalo: true }),
    ],
    face: [48, 46, 0.9, "smile"],
  }) },
  Pencil: { anim: "write", about: "Criar com o Estudário: lápis mágico", draw: () => ({
    body: [
      solid("M20.2 66.2 58.2 28.2 73.8 43.8 35.8 81.8z", "amber"),
      flat("M58.2 28.2 80.5 21.5 73.8 43.8z", "wood", { w: 4 }),
      paint("M74.5 26.5 80.5 21.5 78 28z", INK),
      solid("M20.2 66.2 35.8 81.8 28.7 88.9 13.1 73.3z", "pink", { lipY: 3 }),
      flat(star(88, 52, 8, 3.2, 4), "sun", { w: 3 }),
      flat(star(70, 8, 6, 2.4, 4), "sun", { w: 3 }),
    ],
    face: [46, 56, 0.72, "grin"],
  }) },
  Sun: { anim: "spin", about: "Tema claro: sol", draw: () => ({
    body: [
      ...inkLine("M50 2v10M50 88v10M2 50h10M88 50h10M16 16l7 7M77 77l7 7M16 84l7-7M77 23l7-7", "amber", 6),
      solid(ell(50, 50, 30), "sun"),
    ],
    face: [50, 50, 0.85, "happy"],
  }) },
  Moon: { anim: "float", about: "Tema escuro: lua dormindo", draw: () => ({
    body: [solid("M60 8a42 42 0 1 0 32 62A34 34 0 0 1 60 8z", "moon"), shine("M24 38a30 30 0 0 1 10-16")],
    face: [40, 54, 0.8, "sleepy"],
    fx: [{ d: "M72 18h10l-10 12h10", s: "#9563FF", w: 3.5 }, { d: "M86 2h7l-7 8h7", s: "#9563FF", w: 3 }],
  }) },
  Hand: { anim: "hop", about: "Mostrar: mão aberta para a frente", draw: () => ({
    body: [solid(HAND, "skin"), shine("M36 30v12")],
    face: [54, 68, 0.72, "grin"],
  }) },
  Wave: { anim: "wiggle", about: "Acenar: mão dando tchau", draw: () => ({
    body: [...inkLine("M88 24q9 14 0 28", "amber", 4), ...inkLine("M8 34q-7 12 0 24", "amber", 4), solid(HAND, "skin"), shine("M36 30v12")],
    face: [54, 68, 0.72, "happy"],
    fx: [fxLine("M10 22q-8 12 0 24M90 30q8 12 0 24", "#FFB421"), fxLine("M2 16q-8 18 0 36", "#FFB421", 3)],
  }) },
  Party: { anim: "cheer", about: "Comemorar: lança-confete estourando", draw: () => ({
    body: [
      ...inkLine("M58 40q4-18 22-16", "coral", 5),
      ...inkLine("M66 52q16-6 22 8", "sky", 5),
      solid(poly(12, 92, 40, 36, 68, 64), "violet"),
      flat(poly(22, 74, 30, 58, 50, 76, 36, 82), "sun", { w: 3 }),
      shine("M30 52l6-10"),
    ],
    face: [38, 68, 0.6, "happy"],
    fx: [fxFill(star(80, 10, 7, 3), "#FFD84A"), fxFill(ell(92, 40, 4), "#FF7BB0"), fxFill(rr(50, 12, 8, 8, 2), "#38B2FF"), fxFill(ell(70, 4, 3), "#22C27B")],
  }) },
  Coffee: { anim: "float", about: "Manhã: caneca de café fumegando", draw: () => ({
    body: [
      ...inkLine("M70 42c18 0 18 30 0 30", "paper", 7),
      solid(rr(16, 30, 56, 60, 14), "paper"),
      flat("M16 46h56v14H16z", "indigo", { w: 4 }),
      flat(ell(44, 33, 24, 6), "brown", { w: 4 }),
      shine("M24 70a20 20 0 0 0 4 10"),
    ],
    face: [44, 70, 0.7, "smile"],
    fx: [{ d: "M34 22c-6-6 6-10 0-16", s: "#B7B4D0", w: 4 }, { d: "M50 20c-6-6 6-10 0-16", s: "#B7B4D0", w: 4 }],
  }) },
  Hanger: { anim: "sway", about: "Armário do Folha: cabide", draw: () => ({
    body: [
      ...inkLine("M50 36v-6a9 9 0 1 1 9-9", "steel", 6),
      solid("M50 34 92 70a7 7 0 0 1-4.6 12H12.6A7 7 0 0 1 8 70z", "violet"),
      flat(star(80, 20, 8, 3.2, 4), "sun", { w: 3 }),
    ],
    face: [50, 66, 0.7, "happy"],
    fx: [fxFill(star(14, 22, 6, 2.4, 4), "#FFD84A")],
  }) },
  Phone: { anim: "ring", about: "Aparelhos: celular", draw: () => ({
    body: [solid(rr(24, 6, 52, 88, 14), "indigo"), flat(rr(31, 16, 38, 62, 7), "sky", { w: 4 }), line("M44 85h12", WHITE, 4, { noHalo: true })],
    face: [50, 44, 0.8, "smile"],
  }) },
  Gem: { anim: "spin", about: "Novidade: joia", draw: () => ({
    body: [
      solid("M28 14h44l22 26-44 52L6 40z", "teal"),
      line("M6 40h88M28 14l12 26 10 52M72 14 60 40 50 92M40 40l10-26 10 26", "rgba(255,255,255,.6)", 3, { noHalo: true }),
    ],
    face: [50, 52, 0.75, "wink"],
    fx: [fxFill(star(92, 10, 7, 2.6, 4), "#FFFFFF"), fxFill(star(8, 70, 5, 2, 4), "#FFFFFF")],
  }) },
  Brain: { anim: "pulse", about: "Memorização: cérebro", draw: () => ({
    body: [
      solid("M50 16c-6-6-20-6-24 4-12 0-18 12-12 22-8 8-4 22 8 24 2 12 18 16 28 6 10 10 26 6 28-6 12-2 16-16 8-24 6-10 0-22-12-22-4-10-18-10-24-4z", "pink"),
      line("M50 18v54M30 36q8 2 10 10M70 36q-8 2-10 10", "rgba(38,35,74,.35)", 3.5, { noHalo: true }),
    ],
    face: [50, 52, 0.75, "calm"],
    fx: [fxFill(star(12, 10, 6, 2.4, 4), "#FFD84A"), fxFill(star(90, 8, 6, 2.4, 4), "#FFD84A")],
  }) },
  Trap: { anim: "wiggle", about: "Pegadinha: queijo na ratoeira", draw: () => ({
    body: [
      solid(rr(6, 66, 88, 22, 7), "brown"),
      ...inkLine("M16 66V48q0-6 6-6h20", "steel", 5),
      solid("M38 66 70 36l16 30z", "sun"),
      paint(ell(66, 56, 3.5) + ell(76, 60, 2.6) + ell(58, 62, 2.4), C.sun[1]),
    ],
    face: [66, 52, 0.55, "oops"],
  }) },
  // ---------------------------------------------------------------- matérias e áreas de concurso
  Idioma: { anim: "wiggle", about: "Idiomas: balões conversando", draw: () => ({
    body: [
      solid(rr(40, 6, 54, 40, 14) + "M80 44l8 12-18-10z", "coral"),
      line("M57 36 66 16 75 36M60.5 29h11", WHITE, 4, { noHalo: true }),
      solid(rr(6, 32, 62, 48, 16) + "M18 76l-6 18 22-14z", "sky"),
    ],
    face: [37, 56, 0.85, "grin"],
  }) },
  Pena: { anim: "write", about: "Português: tinteiro com pena", draw: () => ({
    body: [
      solid("M50 50 84 6q12 16-4 32L58 56z", "paper", { lipY: 3 }),
      line("M54 52 80 18", "#B7AFCC", 3, { noHalo: true }),
      flat(rr(24, 46, 32, 12, 4), "steel", { w: 4 }),
      solid(rr(12, 54, 56, 38, 12), "indigo"),
      shine("M20 64v14"),
    ],
    face: [40, 72, 0.78, "smile"],
  }) },
  Calculadora: { anim: "hop", about: "Raciocínio e matemática: calculadora", draw: () => ({
    body: [
      solid(rr(18, 6, 64, 88, 14), "violet"),
      flat(rr(26, 14, 48, 20, 6), "glass", { w: 4 }),
      line("M60 20v8M66 20v8", INK, 3, { noHalo: true }),
      paint(rr(27, 62, 12, 10, 3) + rr(44, 62, 12, 10, 3) + rr(27, 77, 12, 10, 3) + rr(44, 77, 12, 10, 3), WHITE),
      paint(rr(61, 62, 12, 25, 3), C.amber[0]),
    ],
    face: [50, 46, 0.6, "happy"],
  }) },
  Laptop: { anim: "hop", about: "Informática e TI: notebook", draw: () => ({
    body: [
      solid(rr(16, 12, 68, 50, 8), "indigo"),
      flat(rr(23, 19, 54, 36, 4), "sky", { w: 4 }),
      solid("M4 68h92l-10 18H14z", "steel", { lipY: 4 }),
      line("M42 77h16", WHITE, 3.5, { noHalo: true }),
    ],
    face: [50, 39, 0.72, "smile"],
  }) },
  Colunas: { anim: "hop", about: "Direito constitucional: colunas do Estado", draw: () => ({
    body: [
      solid(rr(14, 34, 15, 46, 3), "paper"),
      solid(rr(42.5, 34, 15, 46, 3), "paper"),
      solid(rr(71, 34, 15, 46, 3), "paper"),
      solid(rr(6, 78, 88, 14, 5), "steel", { lipY: 3 }),
      solid("M4 34 50 6 96 34z", "indigo", { lipY: 3 }),
    ],
    face: [50, 25, 0.55, "proud"],
  }) },
  Martelo: { anim: "zap", about: "Direito penal e tribunais: martelo de juiz", draw: () => ({
    body: [
      solid(rr(56, 76, 38, 16, 6), "brown", { lipY: 3 }),
      solid("M44 46h12v36a6 6 0 0 1-12 0z", "wood"),
      solid(rr(10, 14, 60, 32, 10), "brown"),
      flat(rr(14, 12, 8, 36, 3), "amber", { w: 3.5 }),
      flat(rr(58, 12, 8, 36, 3), "amber", { w: 3.5 }),
    ],
    face: [40, 30, 0.62, "fierce", { smile: true }],
    fx: [fxLine("M78 62l8-6M84 70h9", "#FFB421")],
  }) },
  Moedas: { anim: "hop", about: "Contabilidade e finanças: moedas", draw: () => ({
    body: [
      solid(ell(36, 82, 28, 9), "amber", { lipY: 3 }),
      solid(ell(36, 70, 28, 9), "amber", { lipY: 3 }),
      solid(ell(36, 58, 28, 9), "amber", { lipY: 3 }),
      solid(ell(66, 48, 28), "sun"),
      flat(ell(66, 48, 20), "amber", { w: 3, lip: false }),
      shine("M50 30a22 22 0 0 1 10-8"),
    ],
    face: [66, 48, 0.7, "happy"],
  }) },
  Maleta: { anim: "hop", about: "Administração e gestão: maleta", draw: () => ({
    body: [
      ...inkLine("M36 24v-8a6 6 0 0 1 6-6h16a6 6 0 0 1 6 6v8", "steel", 6),
      solid(rr(8, 24, 84, 64, 12), "brown"),
      line("M8 46h84", "ink", 4, { noHalo: true }),
      flat(rr(43, 40, 14, 14, 3), "amber", { w: 3.5 }),
    ],
    face: [50, 68, 0.8, "smile"],
  }) },
  Pergaminho: { anim: "sway", about: "Ética e legislação: pergaminho com selo", draw: () => ({
    body: [
      solid(rr(20, 14, 60, 72, 4), "paper"),
      line("M30 28h40M30 37h32", "#B7AFCC", 3.5, { noHalo: true }),
      solid(rr(12, 6, 76, 14, 7), "wood", { lipY: 3 }),
      solid(rr(12, 80, 76, 14, 7), "wood", { lipY: 3 }),
      solid(ell(72, 66, 9), "coral", { lipY: 3 }),
    ],
    face: [44, 56, 0.72, "calm"],
  }) },
  Balanca: { anim: "sway", about: "Direito: balança da justiça", draw: () => ({
    body: [
      ...inkLine("M50 18v62", "steel", 5),
      ...inkLine("M12 28h76", "steel", 5),
      line("M14 28 4 56M14 28l10 28M86 28 76 56M86 28l10 28", "ink", 2.6),
      solid("M2 56q12 16 24 0z", "amber", { lipY: 3 }),
      solid("M74 56q12 16 24 0z", "amber", { lipY: 3 }),
      solid(rr(28, 80, 44, 12, 5), "indigo", { lipY: 3 }),
      solid(ell(50, 50, 15), "indigo"),
      flat(ell(50, 16, 7), "amber", { w: 3.5 }),
    ],
    face: [50, 50, 0.55, "proud"],
  }) },
  Globo: { anim: "spin", about: "Atualidades e conhecimentos gerais: globo", draw: () => ({
    body: [
      ...inkLine("M20 86q30 12 60 0", "steel", 5),
      solid(ell(50, 46, 38), "sky"),
      paint("M18 34q6-14 18-14 8 4 2 12-10 2-12 10-6 4-8-8z", C.green[0]),
      paint("M64 66q8-8 18-6 2 10-8 16-8 2-10-10z", C.green[0]),
      paint("M70 16q10 4 12 14-8 2-12-6z", C.green[0]),
    ],
    face: [50, 48, 0.82, "happy"],
  }) },
  KitMedico: { anim: "pulse", about: "Saúde: maleta de primeiros socorros", draw: () => ({
    body: [
      ...inkLine("M36 26v-8a6 6 0 0 1 6-6h16a6 6 0 0 1 6 6v8", "steel", 6),
      solid(rr(8, 26, 84, 62, 14), "coral"),
      flat("M70 34h8v8h8v8h-8v8h-8v-8h-8v-8h8z", "paper", { w: 3.5 }),
    ],
    face: [40, 60, 0.85, "smile"],
  }) },
  Frasco: { anim: "wiggle", about: "Ciências: frasco borbulhando", draw: () => ({
    body: [
      solid("M38 10h24v24l24 46a8 8 0 0 1-7 12H21a8 8 0 0 1-7-12l24-46z", "glass"),
      paint("M27 60h46l13 22a6 6 0 0 1-5 9H19a6 6 0 0 1-5-9z", C.green[0]),
      line("M38 10h24v24l24 46a8 8 0 0 1-7 12H21a8 8 0 0 1-7-12l24-46z", "ink", 5, { noHalo: true }),
      flat(rr(32, 4, 36, 10, 4), "steel", { w: 4 }),
    ],
    face: [50, 70, 0.66, "curious"],
    fx: [fxFill(ell(44, -2, 4), "#22C27B"), fxFill(ell(58, -10, 3), "#22C27B")],
  }) },
  Barco: { anim: "sway", about: "Marinha e navegação: barquinho", draw: () => ({
    body: [
      line("M50 8v48", "ink", 5),
      solid("M46 12v42H14z", "paper"),
      flat("M54 18v36h28z", "sky", { w: 4 }),
      solid("M4 58h92l-14 28H18z", "coral"),
    ],
    face: [50, 70, 0.62, "happy"],
    fx: [fxLine("M4 92q8-6 16 0t16 0M64 92q8-6 16 0t16 0", "#38B2FF", 3)],
  }) },
  Lupa: { anim: "zap", about: "Controle e auditoria: lupa", draw: () => ({
    body: [
      ...inkLine("M66 66l24 24", "brown", 9),
      solid(ell(42, 42, 34), "steel"),
      flat(ell(42, 42, 26), "glass", { w: 4 }),
      shine("M24 32a20 20 0 0 1 10-10"),
    ],
    face: [42, 44, 0.8, "curious"],
  }) },
  Recibo: { anim: "hop", about: "Fiscal e tributária: nota fiscal", draw: () => ({
    body: [
      solid("M20 6h60v86l-10-6-10 6-10-6-10 6-10-6-10 6z", "paper"),
      line("M32 62h36M32 72h24", "#B7AFCC", 3.5, { noHalo: true }),
      solid(ell(70, 22, 9), "green", { lipY: 3 }),
      line("M66 22l3 3 6-6", WHITE, 3, { noHalo: true }),
    ],
    face: [46, 38, 0.8, "smile"],
  }) },
  Urna: { anim: "hop", about: "Legislativo: urna de votação", draw: () => ({
    body: [
      solid("M32 6h36v40H32z", "paper", { lipY: 3 }),
      line("M41 24l6 6 11-11", "#22C27B", 4, { noHalo: true }),
      solid(rr(8, 40, 84, 50, 10), "indigo"),
      line("M30 40h40", "ink", 5, { noHalo: true }),
      shine("M18 52v20"),
    ],
    face: [50, 66, 0.8, "proud"],
  }) },
  Cofrinho: { anim: "hop", about: "Bancos: cofrinho", draw: () => ({
    body: [
      solid(rr(22, 74, 12, 18, 4), "pink", { lipY: 3 }),
      solid(rr(60, 74, 12, 18, 4), "pink", { lipY: 3 }),
      solid("M28 32 32 12 46 26z", "pink"),
      solid(ell(48, 56, 40, 30), "pink"),
      flat(ell(88, 58, 7, 10), "pink", { w: 4 }),
      paint(ell(86, 55, 1.6) + ell(90, 61, 1.6), INK),
      line("M40 28h16", "ink", 4, { noHalo: true }),
      flat(ell(48, 14, 9), "sun", { w: 3.5 }),
    ],
    face: [44, 56, 0.85, "happy"],
  }) },
};

// ------------------------------------------------------------------ montagem
export function build() {
  const out = {};
  for (const [name, def] of Object.entries(TURMA)) {
    const g = def.draw();
    const [cx, cy, s, mood, opts] = g.face;
    out[name] = {
      anim: def.anim,
      about: def.about,
      body: toPrims(g.body),
      face: [...face(cx, cy, s, mood, true, opts || {}), ...(g.extra ? g.extra(true) : [])],
      faceSmall: [...face(cx, cy, s, mood, false, opts || {}), ...(g.extra ? g.extra(false) : [])],
      fx: g.fx || [],
    };
  }
  return out;
}
