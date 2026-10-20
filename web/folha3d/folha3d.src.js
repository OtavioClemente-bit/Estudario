// O Folha em 3D: um livro vivo. Fonte do web/src/jsMain/resources/folha3d.js
// (gerado por `node scripts/web/build-folha3d.mjs`, que embute o three.js; o CSP do app só aceita
// scripts do próprio site).
//
// Uso: <estudario-folha3d mood="idle|happy|thinking|talking|wave|point|wink|sad" size="220"
//        [glasses="off"] [spin]></estudario-folha3d>
// Sem WebGL, o elemento cai para o Folha 2D (<estudario-folha>).
//
// O livro: capa dura de tecido com cantos gastos -> miolo grosso -> folhas soltas com espessura
// (a de cima tem o rosto). As duas metades "respiram" (abrem e fecham de leve) e as folhas sobem
// junto. Os braços são as duas fitas marcadoras de cetim, que saem de entre as folhas e têm física
// (atrasam, balançam e nunca atravessam a página); a ponta em V é a mão. O capelo é de veludo e o
// casquete acompanha o alto do livro; a borla é de fios e balança presa ao cordão.
import * as THREE from "three";
import { RoomEnvironment } from "three/addons/environments/RoomEnvironment.js";
import { RoundedBoxGeometry } from "three/addons/geometries/RoundedBoxGeometry.js";

const V3 = (x = 0, y = 0, z = 0) => new THREE.Vector3(x, y, z);
const clamp = (x, a, b) => Math.max(a, Math.min(b, x));
const smooth = (a, b, x) => { const t = clamp((x - a) / (b - a), 0, 1); return t * t * (3 - 2 * t); };
const ease = (x) => x * x * x * (x * (x * 6 - 15) + 10);
const lerp = (a, b, t) => a + (b - a) * t;
const INK = "#26215C", MINT = "#7EE0B8";

// ================================================================ forma do livro
// Cada metade: u vai da lombada (0) à borda solta (1), v de baixo (0) a cima (1).
const Y0 = -0.66, Y1 = 0.66, HINGE = 0.03, K = 10, PAPER_T = 0.006, PH = 0.003;
const STEP_U = 0.018, STEP_V = 0.011;              // cada folha de baixo sobra um pouco (degraus nas bordas)
const UB = 1 + STEP_U * K, VB0 = -STEP_V * K, VB1 = 1 + STEP_V * K;   // tamanho do miolo
const U1 = UB + 0.1, COVER_T = 0.08;              // capa acolchoada
const yOff = (u) => -0.075 * (1 - clamp(u, 0, 1)) ** 2;
const py = (u, v) => Y0 + (Y1 - Y0) * v + yOff(u);
const xAt = (s, u) => s * (HINGE + u);          // capa
const xP = (s, u) => s * (PH + u);              // folhas: encostam na costura
const zC = (u) => -0.06 + 0.12 * u;             // face de dentro da capa
const zB = (u) => zC(u) + 0.012 + 0.26 * (1 - Math.exp(-u * 7));
const looseL = (u) => 0.1 * (1 - Math.exp(-u * 7));
const PIVZ = zC(0);                             // eixo da lombada, em volta do qual as metades respiram
const uEnd = (k) => 1 + STEP_U * (K - 1 - k) - 0.002 * (((K - 1 - k) * 5) % 3);
const vLo = (k) => -STEP_V * (K - 1 - k), vHi = (k) => 1 + STEP_V * (K - 1 - k);
function zSheetF(flap, k, u, v) {
  const f = (k + 1) / K;
  return zB(u) + looseL(u) * f
    + 0.018 * f * smooth(0.68, 1.1, u) * (0.65 + 0.35 * Math.sin(k * 2.3 + 1))
    + 0.02 * Math.pow(0.6, K - 1 - k) * smooth(0.8, 1, u) * smooth(0.72, 1, v)
    + flap * u * u * u * f;
}
// Cantos de fora arredondados (papel e capa gastos): leva o quadrado do canto para um quarto de círculo.
function roundUV(u, v, ru, rv) {
  const cu = 1 - ru;
  if (u <= cu) return [u, v];
  let cv;
  if (v < rv) cv = rv; else if (v > 1 - rv) cv = 1 - rv; else return [u, v];
  const dx = (u - cu) / ru, dy = (v - cv) / rv, L = Math.hypot(dx, dy);
  if (L < 1e-6) return [u, v];
  const k = Math.max(Math.abs(dx), Math.abs(dy)) / L;
  return [cu + dx * k * ru, cv + dy * k * rv];
}
function rotAbout(p, theta) {
  // Gira em volta do eixo da lombada (vertical, em x = 0, z = PIVZ).
  const c = Math.cos(theta), s = Math.sin(theta), x = p.x, z = p.z - PIVZ;
  p.x = x * c + z * s; p.z = PIVZ - x * s + z * c;
  return p;
}

// ================================================================ malhas
class Grid {
  constructor(nu, nv, uvFn) {
    this.nu = nu; this.nv = nv;
    const n = (nu + 1) * (nv + 1), uv = [], idx = [];
    for (let j = 0; j <= nv; j++) for (let i = 0; i <= nu; i++) uv.push(...uvFn(i / nu, j / nv));
    for (let j = 0; j < nv; j++) for (let i = 0; i < nu; i++) { const a = j * (nu + 1) + i, b = a + 1, c = a + nu + 1, d = c + 1; idx.push(a, b, c, b, d, c); }
    this.geo = new THREE.BufferGeometry();
    this.geo.setAttribute("position", new THREE.BufferAttribute(new Float32Array(n * 3), 3));
    this.geo.setAttribute("uv", new THREE.Float32BufferAttribute(uv, 2));
    this.geo.setIndex(idx);
  }
  update(fn) {
    const p = this.geo.attributes.position.array; let o = 0;
    for (let j = 0; j <= this.nv; j++) for (let i = 0; i <= this.nu; i++) { const q = fn(i / this.nu, j / this.nv); p[o++] = q.x; p[o++] = q.y; p[o++] = q.z; }
    this.geo.attributes.position.needsUpdate = true; this.geo.computeVertexNormals(); this.geo.computeBoundingSphere();
    return this;
  }
}

// Placa fina com espessura (folha, capa): frente, verso e as três bordas expostas; grupos 0/1/2.
class Slab {
  constructor(nu, nv, uvFront, uvBack = uvFront, shadeFront = 1, shadeEdge = 1) {
    this.nu = nu; this.nv = nv;
    const n = (nu + 1) * (nv + 1), ne = 4 * (nu + 1) + 2 * (nv + 1);
    this.n = n; this.total = 2 * n + ne;
    const uv = new Float32Array(this.total * 2), col = new Float32Array(this.total * 3), idx = [];
    let o = 0;
    for (let face = 0; face < 2; face++) for (let j = 0; j <= nv; j++) for (let i = 0; i <= nu; i++) { const t = (face ? uvBack : uvFront)(i / nu, j / nv); uv[o++] = t[0]; uv[o++] = t[1]; }
    for (let e = 0; e < 2; e++) for (let side = 0; side < 2; side++) for (let i = 0; i <= nu; i++) { uv[o++] = i / nu; uv[o++] = side; }
    for (let side = 0; side < 2; side++) for (let j = 0; j <= nv; j++) { uv[o++] = j / nv; uv[o++] = side; }
    for (let i = 0; i < this.total; i++) { const s = i < n ? shadeFront : i < 2 * n ? 0.9 : shadeEdge; col[i * 3] = col[i * 3 + 1] = col[i * 3 + 2] = s; }
    const quad = (a, b, c, d) => idx.push(a, b, c, b, d, c);
    for (let face = 0; face < 2; face++) {
      const base = face * n;
      for (let j = 0; j < nv; j++) for (let i = 0; i < nu; i++) { const a = base + j * (nu + 1) + i; face ? quad(a, a + nu + 1, a + 1, a + nu + 2) : quad(a, a + 1, a + nu + 1, a + nu + 2); }
    }
    const faces = idx.length;
    let e0 = 2 * n;
    for (let e = 0; e < 2; e++, e0 += 2 * (nu + 1)) for (let i = 0; i < nu; i++) quad(e0 + i, e0 + i + 1, e0 + nu + 1 + i, e0 + nu + 2 + i);
    for (let j = 0; j < nv; j++) quad(e0 + j, e0 + j + 1, e0 + nv + 1 + j, e0 + nv + 2 + j);
    this.geo = new THREE.BufferGeometry();
    this.geo.setAttribute("position", new THREE.BufferAttribute(new Float32Array(this.total * 3), 3));
    this.geo.setAttribute("uv", new THREE.BufferAttribute(uv, 2));
    this.geo.setAttribute("color", new THREE.BufferAttribute(col, 3));
    this.geo.setIndex(idx);
    this.geo.addGroup(0, faces / 2, 0); this.geo.addGroup(faces / 2, faces / 2, 1); this.geo.addGroup(faces, idx.length - faces, 2);
  }
  update(fn, thick) {
    const { nu, nv } = this, p = this.geo.attributes.position.array;
    const front = [], set = (k, q) => { p[k * 3] = q.x; p[k * 3 + 1] = q.y; p[k * 3 + 2] = q.z; };
    let k = 0;
    for (let j = 0; j <= nv; j++) for (let i = 0; i <= nu; i++) { const q = fn(i / nu, j / nv); front.push(q); set(k++, q); }
    const back = front.map((q) => V3(q.x, q.y, q.z - thick));
    for (const q of back) set(k++, q);
    for (const j of [nv, 0]) for (const f of [front, back]) for (let i = 0; i <= nu; i++) set(k++, f[j * (nu + 1) + i]);
    for (const f of [front, back]) for (let j = 0; j <= nv; j++) set(k++, f[j * (nu + 1) + nu]);
    this.geo.attributes.position.needsUpdate = true; this.geo.computeVertexNormals(); this.geo.computeBoundingSphere();
    return this;
  }
}

// ================================================================ texturas e materiais (uma vez só)
function canvas(w, h) { const c = document.createElement("canvas"); c.width = w; c.height = h; return [c, c.getContext("2d")]; }
function rng(seed) { let s = seed; return () => (s = (s * 1103515245 + 12345) & 0x7fffffff) / 0x7fffffff; }
const tex = (c, srgb = true) => { const t = new THREE.CanvasTexture(c); if (srgb) t.colorSpace = THREE.SRGBColorSpace; t.anisotropy = 8; return t; };
const SW = 2048, SH = 1024;
const toCX = (s, u) => (1 + s * u) / 2 * SW;
const spreadUV = (s) => (u, v) => [(1 + s * u) / 2, v];

function paperBase(g, r) {
  g.fillStyle = "#FFF6E4"; g.fillRect(0, 0, SW, SH);
  for (let i = 0; i < 30000; i++) { g.fillStyle = `rgba(120,100,70,${0.012 + r() * 0.035})`; g.fillRect(r() * SW, r() * SH, 1 + r() * 2.5, 1); }
  // Fibras do papel e umas manchinhas de uso.
  for (let i = 0; i < 260; i++) {
    g.strokeStyle = `rgba(140,120,90,${0.03 + r() * 0.05})`; g.lineWidth = 0.6 + r();
    const x = r() * SW, y = r() * SH, a = r() * Math.PI, l = 6 + r() * 18;
    g.beginPath(); g.moveTo(x, y); g.quadraticCurveTo(x + Math.cos(a) * l * 0.5 + r() * 4, y + Math.sin(a) * l * 0.5, x + Math.cos(a) * l, y + Math.sin(a) * l); g.stroke();
  }
  for (const s of [-1, 1]) {
    const x0 = toCX(s, 0), x1 = toCX(s, 0.28), gr = g.createLinearGradient(x0, 0, x1, 0);
    gr.addColorStop(0, "rgba(50,40,100,.42)"); gr.addColorStop(0.18, "rgba(50,40,100,.16)"); gr.addColorStop(1, "rgba(50,40,100,0)");
    g.fillStyle = gr; g.fillRect(Math.min(x0, x1), 0, Math.abs(x1 - x0), SH);
    // Borda de fora amarelada pelo dedo de quem folheia.
    const e0 = toCX(s, 1), e1 = toCX(s, 0.9), ge = g.createLinearGradient(e0, 0, e1, 0);
    ge.addColorStop(0, "rgba(165,130,80,.22)"); ge.addColorStop(1, "rgba(165,130,80,0)");
    g.fillStyle = ge; g.fillRect(Math.min(e0, e1), 0, Math.abs(e1 - e0), SH);
    const cy = SH * 0.9, cg = g.createRadialGradient(e0, cy, 0, e0, cy, 150);
    cg.addColorStop(0, "rgba(150,115,70,.16)"); cg.addColorStop(1, "rgba(150,115,70,0)");
    g.fillStyle = cg; g.fillRect(Math.min(e0, e1) - 150, cy - 150, 300, 300);
  }
}
function paragraph(g, text, x, y, w, { size = 21, lh = 29, indent = 26, hl = false } = {}) {
  g.font = `${size}px Georgia, "Times New Roman", serif`;
  const words = text.split(" "), lines = [], sp = g.measureText(" ").width;
  let line = [], width = indent;
  for (const word of words) {
    const ww = g.measureText(word).width;
    if (line.length && width + sp + ww > w) { lines.push(line); line = []; width = 0; }
    width += (line.length ? sp : 0) + ww; line.push(word);
  }
  lines.push(line);
  lines.forEach((ln, li) => {
    const x0 = x + (li === 0 ? indent : 0), avail = w - (li === 0 ? indent : 0);
    const ws = ln.map((t) => g.measureText(t).width), total = ws.reduce((a, b) => a + b, 0);
    const gap = li < lines.length - 1 && ln.length > 1 ? (avail - total) / (ln.length - 1) : sp;
    if (hl) {
      const end = x0 + total + gap * (ln.length - 1);
      g.fillStyle = "rgba(255,221,80,.6)";
      g.beginPath(); g.moveTo(x0 - 6, y - size * 0.85); g.lineTo(end + 5, y - size * 0.92); g.lineTo(end + 3, y + size * 0.3); g.lineTo(x0 - 4, y + size * 0.34); g.fill();
    }
    g.fillStyle = "rgba(38,32,70,.86)";
    let cx = x0; ln.forEach((t, i) => { g.fillText(t, cx, y); cx += ws[i] + gap; });
    y += lh;
  });
  return y + 6;
}

let A = null;
function assets() {
  if (A) return A;
  const paperTex = (() => {
    const [c, g] = canvas(SW, SH); paperBase(g, rng(7));
    const L = { x: 96, w: 790 }, R = { x: 1162, w: 790 };
    const center = (t, x, w, y, font) => { g.font = font; g.fillStyle = "rgba(38,32,70,.86)"; g.fillText(t, x + (w - g.measureText(t).width) / 2, y); };
    center("C O N S T I T U I Ç Ã O   F E D E R A L", L.x, L.w, 46, "15px Georgia, serif");
    g.fillStyle = "rgba(38,32,70,.3)"; g.fillRect(L.x, 58, L.w, 1.5);
    center("TÍTULO II", L.x, L.w, 108, "bold 24px Georgia, serif");
    center("Dos Direitos e Garantias Fundamentais", L.x, L.w, 142, "italic 23px Georgia, serif");
    center("CAPÍTULO I", L.x, L.w, 190, "bold 18px Georgia, serif");
    center("Dos Direitos e Deveres Individuais e Coletivos", L.x, L.w, 218, "italic 19px Georgia, serif");
    let y = paragraph(g, "Art. 5º Todos são iguais perante a lei, sem distinção de qualquer natureza, garantindo-se aos brasileiros e aos estrangeiros residentes no País a inviolabilidade do direito à vida, à liberdade, à igualdade, à segurança e à propriedade, nos termos seguintes:", L.x, 778, L.w);
    paragraph(g, "I – homens e mulheres são iguais em direitos e obrigações, nos termos desta Constituição;", L.x, y, L.w);
    g.font = "15px Georgia, serif"; g.fillStyle = "rgba(38,32,70,.6)"; g.fillText("34", L.x, 990);
    center("A R T .  5 º", R.x, R.w, 46, "15px Georgia, serif");
    g.fillStyle = "rgba(38,32,70,.3)"; g.fillRect(R.x, 58, R.w, 1.5);
    y = paragraph(g, "II – ninguém será obrigado a fazer ou deixar de fazer alguma coisa senão em virtude de lei;", R.x, 106, R.w, { hl: true });
    y = paragraph(g, "III – ninguém será submetido a tortura nem a tratamento desumano ou degradante;", R.x, y, R.w);
    paragraph(g, "IV – é livre a manifestação do pensamento, sendo vedado o anonimato;", R.x, y, R.w);
    y = paragraph(g, "V – é assegurado o direito de resposta, proporcional ao agravo, além da indenização por dano material, moral ou à imagem;", R.x, 778, R.w);
    paragraph(g, "VI – é inviolável a liberdade de consciência e de crença, sendo assegurado o livre exercício dos cultos religiosos;", R.x, y, R.w);
    g.strokeStyle = "rgba(70,70,80,.55)"; g.lineWidth = 3; g.lineCap = "round";
    g.beginPath(); g.moveTo(R.x + R.w + 22, 96); g.lineTo(R.x + R.w + 32, 108); g.lineTo(R.x + R.w + 52, 80); g.stroke();
    g.font = "15px Georgia, serif"; g.fillStyle = "rgba(38,32,70,.6)"; g.fillText("35", R.x + R.w - 20, 990);
    return tex(c);
  })();
  const paperPlain = (() => { const [c, g] = canvas(1024, 512); g.scale(0.5, 0.5); paperBase(g, rng(9)); return tex(c); })();
  const paperBump = (() => {
    const [c, g] = canvas(512, 512); const r = rng(11);
    g.fillStyle = "#808080"; g.fillRect(0, 0, 512, 512);
    for (let i = 0; i < 14000; i++) { const k = 105 + r() * 50 | 0; g.fillStyle = `rgb(${k},${k},${k})`; g.fillRect(r() * 512, r() * 512, 1 + r() * 3, 1 + r()); }
    const t = tex(c, false); t.wrapS = t.wrapT = THREE.RepeatWrapping; t.repeat.set(5, 3); return t;
  })();
  const edgeTex = (() => {
    const [c, g] = canvas(64, 1024); const r = rng(3);
    g.fillStyle = "#F0E8DA"; g.fillRect(0, 0, 64, 1024);
    for (let y = 0; y < 1024; y += 1.5 + r() * 2.5) { g.fillStyle = `rgba(110,90,60,${0.06 + r() * 0.2})`; g.fillRect(0, y, 64, 0.8 + r()); }
    const t = tex(c); t.wrapS = THREE.RepeatWrapping; return t;
  })();
  const linen = (() => {
    const [c, g] = canvas(256, 256); const r = rng(5);
    g.fillStyle = "#808080"; g.fillRect(0, 0, 256, 256);
    for (let i = 0; i < 256; i += 2) { g.fillStyle = `rgba(255,255,255,${0.06 + r() * 0.12})`; g.fillRect(0, i, 256, 1); g.fillStyle = `rgba(0,0,0,${0.06 + r() * 0.12})`; g.fillRect(i, 0, 1, 256); }
    const t = tex(c, false); t.wrapS = t.wrapT = THREE.RepeatWrapping; t.repeat.set(7, 7); return t;
  })();
  function coverTex(front) {
    const [c, g] = canvas(1024, 1280), [mc, mg] = canvas(1024, 1280);
    g.fillStyle = "#3A2FC4"; g.fillRect(0, 0, 1024, 1280); mg.fillStyle = "#000"; mg.fillRect(0, 0, 1024, 1280);
    const both = (fn) => { for (const [x, col] of [[g, "#F2C35B"], [mg, "#fff"]]) { x.fillStyle = col; x.strokeStyle = col; fn(x); } };
    both((x) => { x.lineWidth = 7; x.strokeRect(70, 70, 884, 1140); x.lineWidth = 2.5; x.strokeRect(92, 92, 840, 1096); });
    if (front) {
      both((x) => {
        x.textAlign = "center";
        x.font = "bold 92px Georgia, serif"; x.fillText("ESTUDÁRIO", 512, 520);
        x.font = "italic 40px Georgia, serif"; x.fillText("Vade Mecum do Concurseiro", 512, 600);
        x.beginPath(); x.arc(512, 760, 70, 0, Math.PI * 2); x.lineWidth = 6; x.stroke();
        x.font = "bold 84px Georgia, serif"; x.fillText("E", 512, 790);
      });
    } else {
      both((x) => { x.textAlign = "center"; x.font = "italic 36px Georgia, serif"; x.fillText("do edital à aprovação", 512, 1120); });
    }
    // Tecido puído: pontinhos claros perto das bordas.
    const r = rng(front ? 21 : 22);
    for (let i = 0; i < 2600; i++) {
      const edge = r() < 0.5, x = edge ? (r() < 0.5 ? r() * 60 : 1024 - r() * 60) : r() * 1024, y = edge ? r() * 1280 : (r() < 0.5 ? r() * 60 : 1280 - r() * 60);
      g.fillStyle = `rgba(190,180,255,${0.08 + r() * 0.18})`; g.fillRect(x, y, 1 + r() * 2, 1 + r() * 2);
    }
    return { map: tex(c), metalnessMap: tex(mc, false) };
  }
  const satin = (() => {
    // Trama do cetim no sentido do comprimento, com a ourela (bordinha) mais escura.
    const [c, g] = canvas(256, 64); const r = rng(13);
    g.fillStyle = "#ffffff"; g.fillRect(0, 0, 256, 64);
    for (let y = 0; y < 64; y++) { g.fillStyle = `rgba(0,0,0,${0.02 + r() * 0.05})`; g.fillRect(0, y, 256, 1); }
    for (const y of [3, 60]) { g.fillStyle = "rgba(0,0,0,.18)"; g.fillRect(0, y, 256, 2); }
    const t = tex(c); t.wrapS = THREE.RepeatWrapping; t.repeat.set(4, 1); return t;
  })();
  const stripes = (() => { const [c, g] = canvas(64, 8); for (let i = 0; i < 8; i++) { g.fillStyle = i % 2 ? "#ffffff" : MINT; g.fillRect(i * 8, 0, 8, 8); } const t = tex(c); t.wrapS = THREE.RepeatWrapping; t.repeat.set(3, 1); return t; })();
  const cheekTex = (() => {
    const [c, g] = canvas(128, 128), r = g.createRadialGradient(64, 64, 0, 64, 64, 64);
    r.addColorStop(0, "rgba(244,150,180,.75)"); r.addColorStop(0.55, "rgba(244,160,188,.5)"); r.addColorStop(1, "rgba(244,166,192,0)");
    g.fillStyle = r; g.fillRect(0, 0, 128, 128); return tex(c);
  })();
  const eyeTex = (() => {
    const [c, g] = canvas(64, 256), gr = g.createLinearGradient(0, 0, 0, 128);
    gr.addColorStop(0, "#120E36"); gr.addColorStop(0.7, "#1B1550"); gr.addColorStop(1, "#3B3196");
    g.fillStyle = gr; g.fillRect(0, 0, 64, 256); return tex(c);
  })();
  const capTex = (() => {
    // Veludo com as costuras do casquete.
    const [c, g] = canvas(512, 128); const r = rng(17);
    g.fillStyle = "#808080"; g.fillRect(0, 0, 512, 128);
    for (let i = 0; i < 5000; i++) { const k = 110 + r() * 40 | 0; g.fillStyle = `rgb(${k},${k},${k})`; g.fillRect(r() * 512, r() * 128, 1, 1 + r() * 2); }
    for (let i = 0; i < 6; i++) { g.fillStyle = "#4a4a4a"; g.fillRect(i * 512 / 6, 0, 3, 128); g.fillStyle = "#a8a8a8"; g.fillRect(i * 512 / 6 + 3, 0, 1, 128); }
    return tex(c, false);
  })();

  const phys = (o) => new THREE.MeshPhysicalMaterial(o);
  const cloth = { bumpMap: linen, bumpScale: 0.9, roughness: 0.6, clearcoat: 0.25, clearcoatRoughness: 0.5, sheen: 0.7, sheenColor: new THREE.Color("#8F86FF"), side: THREE.DoubleSide, vertexColors: true };
  const satinOpts = (color, sheenColor) => ({ color, map: satin, roughness: 0.3, metalness: 0.05, sheen: 1, sheenColor: new THREE.Color(sheenColor), sheenRoughness: 0.25, anisotropy: 0.75, clearcoat: 0.3, clearcoatRoughness: 0.2, side: THREE.DoubleSide });
  A = {
    paperTex,
    paper: phys({ map: paperTex, bumpMap: paperBump, bumpScale: 0.5, roughness: 0.9, sheen: 0.35, sheenColor: new THREE.Color("#fff3e0"), sheenRoughness: 0.8, vertexColors: true, side: THREE.DoubleSide }),
    paperBack: phys({ map: paperPlain, roughness: 0.92, vertexColors: true, side: THREE.DoubleSide }),
    paperEdge: phys({ color: "#F6EBD3", roughness: 0.95, vertexColors: true, side: THREE.DoubleSide }),
    blockEdge: phys({ map: edgeTex, roughness: 0.95, side: THREE.DoubleSide }),
    coverIn: phys({ ...cloth, color: "#3328B0" }),
    coverEdge: phys({ ...cloth, color: "#3A2FC4" }),
    coverOut: [coverTex(true), coverTex(false)].map((t) => phys({ ...cloth, color: "#ffffff", map: t.map, metalnessMap: t.metalnessMap, metalness: 1, roughness: 0.45 })),
    headband: phys({ map: stripes, roughness: 0.5, sheen: 1 }),
    inkGloss: phys({ color: "#1E184F", roughness: 0.14, clearcoat: 1, clearcoatRoughness: 0.04 }),
    eye: phys({ map: eyeTex, roughness: 0.12, clearcoat: 1, clearcoatRoughness: 0.03 }),
    shine: new THREE.MeshBasicMaterial({ color: "#ffffff" }),
    gold: phys({ color: "#F2B640", metalness: 1, roughness: 0.26 }),
    thread: phys({ color: "#F4BE4E", metalness: 0.75, roughness: 0.38, sheen: 1, sheenColor: new THREE.Color("#FFE3A0") }),
    cap: phys({ color: "#17114E", roughness: 0.95, envMapIntensity: 0.25, sheen: 0.6, sheenColor: new THREE.Color("#6A5FE8"), sheenRoughness: 0.35, bumpMap: capTex, bumpScale: 1.2, side: THREE.DoubleSide }),
    capBoard: phys({ color: "#17114E", roughness: 0.95, envMapIntensity: 0.25, sheen: 0.6, sheenColor: new THREE.Color("#6A5FE8"), sheenRoughness: 0.35, bumpMap: linen, bumpScale: 0.5 }),
    felt: phys({ color: "#F59AB5", roughness: 1, sheen: 1, sheenColor: new THREE.Color("#FFD0DE"), sheenRoughness: 0.6 }),
    shoeWhite: phys({ color: "#FBFAFF", roughness: 0.55, clearcoat: 0.3, clearcoatRoughness: 0.4, sheen: 0.5, sheenColor: new THREE.Color("#ffffff") }),
    shoeBlue: phys({ color: "#3A2FC4", roughness: 0.6, sheen: 0.8, sheenColor: new THREE.Color("#8F86FF"), bumpMap: linen, bumpScale: 0.6 }),
    sole: phys({ color: "#ECEAF2", roughness: 0.8 }),
    tread: phys({ color: "#B9B4CC", roughness: 0.9 }),
    arm: phys({ color: "#2F25B8", roughness: 0.5, clearcoat: 0.3, clearcoatRoughness: 0.35, sheen: 0.45, sheenColor: new THREE.Color("#8A80F0"), sheenRoughness: 0.5 }),
    glove: phys({ color: "#FDFCFF", roughness: 0.62, sheen: 1, sheenColor: new THREE.Color("#ffffff"), sheenRoughness: 0.45, clearcoat: 0.08 }),
    gloveCuff: phys({ color: "#F2F0FB", roughness: 0.6, sheen: 1, sheenColor: new THREE.Color("#ffffff"), side: THREE.DoubleSide }),
    gloveSeam: phys({ color: "#CFCBE3", roughness: 0.7 }),
    ribbonMint: phys(satinOpts("#6FD9AE", "#E9FFF6")),
    ribbonGold: phys(satinOpts("#E9AE3C", "#FFF0C8")),
    frame: phys({ color: "#2A2063", metalness: 0.55, roughness: 0.2, clearcoat: 1, clearcoatRoughness: 0.08 }),
    frameGold: phys({ color: "#E9B24E", metalness: 1, roughness: 0.18 }),
    lens: phys({ color: "#ffffff", roughness: 0.02, transmission: 1, thickness: 0, ior: 1.5, clearcoat: 1, envMapIntensity: 1.8 }),
    tongue: phys({ color: "#F08FB0", roughness: 0.35, clearcoat: 0.6 }),
    cheek: new THREE.MeshStandardMaterial({ map: cheekTex, transparent: true, depthWrite: false, roughness: 0.7, polygonOffset: true, polygonOffsetFactor: -4, side: THREE.DoubleSide }),
    flipF: phys({ map: paperTex, roughness: 0.9, sheen: 0.35, sheenColor: new THREE.Color("#fff3e0"), depthWrite: false, side: THREE.FrontSide }),
    flipB: phys({ map: paperTex, roughness: 0.9, color: "#F3EEE6", depthWrite: false, side: THREE.BackSide }),
    pencil: phys({ color: "#F7C531", roughness: 0.4, clearcoat: 0.6 }),
    ferrule: phys({ color: "#C9CBD3", metalness: 1, roughness: 0.3 }),
    eraser: phys({ color: "#F29BB6", roughness: 0.7 }),
    wood: phys({ color: "#EBC995", roughness: 0.8 }),
    lead: phys({ color: "#333038", metalness: 0.4, roughness: 0.35 }),
    confetti: new THREE.MeshStandardMaterial({ side: THREE.DoubleSide, roughness: 0.6 }),
  };
  return A;
}

const cast = (o) => { o.traverse((m) => { if (m.isMesh) { m.castShadow = true; m.receiveShadow = true; } }); return o; };
const front = (o) => { o.traverse((m) => { if (m.isMesh) m.renderOrder = 20; }); return o; };
const fillColor = (geo, v) => geo.setAttribute("color", new THREE.Float32BufferAttribute(new Array(geo.attributes.position.count * 3).fill(v), 3));

// ================================================================ fita marcadora que é braço
// Corrente de pontos (Verlet) presa entre as folhas; puxada de leve para a curva da pose, com
// gravidade e atraso, e empurrada para fora do livro. A malha é uma fita fina de cetim com a ponta em V.
const RIB_N = 24, RIB_L = 1.05, RIB_ROWS = 64, RIB_COLS = 5, RIB_W = 0.044, RIB_T = 0.0045, RIB_NOTCH = 0.055;
class Ribbon {
  constructor(s, material, L = RIB_L, hw = RIB_W) {
    this.s = s; this.L = L; this.hw = hw; this.seg = L / (RIB_N - 1); this.p = []; this.prev = []; this.ready = false;
    const R = RIB_ROWS, C = RIB_COLS, n = R * C;
    this.n = n; this.total = 2 * n + 4 * R;
    const uv = new Float32Array(this.total * 2), idx = [];
    let o = 0;
    for (let f = 0; f < 2; f++) for (let r = 0; r < R; r++) for (let c = 0; c < C; c++) { uv[o++] = r / (R - 1); uv[o++] = c / (C - 1); }
    for (let e = 0; e < 2; e++) for (let side = 0; side < 2; side++) for (let r = 0; r < R; r++) { uv[o++] = r / (R - 1); uv[o++] = e; }
    const quad = (a, b, c, d) => idx.push(a, b, c, b, d, c);
    for (let f = 0; f < 2; f++) for (let r = 0; r < R - 1; r++) for (let c = 0; c < C - 1; c++) { const a = f * n + r * C + c; quad(a, a + 1, a + C, a + C + 1); }
    for (let e = 0; e < 2; e++) { const b = 2 * n + e * 2 * R; for (let r = 0; r < R - 1; r++) quad(b + r, b + r + 1, b + R + r, b + R + r + 1); }
    this.geo = new THREE.BufferGeometry();
    this.geo.setAttribute("position", new THREE.BufferAttribute(new Float32Array(this.total * 3), 3));
    this.geo.setAttribute("uv", new THREE.BufferAttribute(uv, 2));
    this.geo.setIndex(idx);
    this.mesh = new THREE.Mesh(this.geo, material);
    this.mesh.castShadow = true; this.mesh.receiveShadow = true; this.mesh.frustumCulled = false;
    this.prevW = V3(0, 1, 0);
  }
  step(dt, root, dir0, pose, flapRest, pencil) {
    const N = RIB_N, seg = this.seg;
    const curve = new THREE.CubicBezierCurve3(root, root.clone().addScaledVector(dir0, 0.38), pose.target.clone().addScaledVector(pose.dir, -0.4), pose.target);
    if (!this.ready) { for (let i = 0; i < N; i++) { const q = curve.getPointAt(i / (N - 1)); this.p.push(q.clone()); this.prev.push(q.clone()); } this.ready = true; }
    const p = this.p, prev = this.prev;
    p[0].copy(root); p[1].copy(root).addScaledVector(dir0, seg);
    // Pontos de descanso a cada `seg` ao longo da curva; o que sobrar segue reto na direção da ponta.
    const cl = curve.getLength(), rest = (i) => { const d = i * seg; return d <= cl ? curve.getPointAt(d / cl) : pose.target.clone().addScaledVector(pose.dir, d - cl); };
    const g = -3.2 * dt * dt, f60 = dt * 60;
    for (let i = 2; i < N; i++) {
      const vx = (p[i].x - prev[i].x) * 0.92, vy = (p[i].y - prev[i].y) * 0.92, vz = (p[i].z - prev[i].z) * 0.92;
      prev[i].copy(p[i]);
      p[i].x += vx; p[i].y += vy + g; p[i].z += vz;
      const t = i / (N - 1), k = 1 - Math.pow(1 - (0.045 + 0.2 * t ** 1.6), f60);
      p[i].lerp(rest(i), k);
    }
    // Segurando o lápis: a ponta dá umas voltas em volta dele.
    if (pencil) {
      const { c, axis } = pencil, b1 = V3().crossVectors(axis, V3(0, 0, 1)).normalize(), b2 = V3().crossVectors(axis, b1);
      const M = 6, k = 1 - Math.pow(0.5, f60);
      for (let j = 0; j < M; j++) {
        const i = N - M + j, ang = j * 1.2 + 0.6;
        const q = c.clone().addScaledVector(axis, 0.014 * j - 0.04).addScaledVector(b1, Math.cos(ang) * 0.026).addScaledVector(b2, Math.sin(ang) * 0.026);
        p[i].lerp(q, k);
      }
    }
    for (let it = 0; it < 5; it++) {
      for (let i = 2; i < N; i++) {
        const a = p[i - 1], b = p[i], d = V3().subVectors(b, a), L = d.length() || 1e-6, diff = (L - seg) / L;
        if (i - 1 <= 1) b.addScaledVector(d, -diff);
        else { a.addScaledVector(d, diff * 0.5); b.addScaledVector(d, -diff * 0.5); }
      }
      // Rigidez de dobra: o cetim não faz quina.
      for (let i = 2; i < N - 1; i++) { const m = p[i - 1].clone().add(p[i + 1]).multiplyScalar(0.5); p[i].lerp(m, 0.25); }
      for (let i = 3; i < N; i++) pushOut(p[i], flapRest);
    }
  }
  build(t, pose, phase) {
    const R = RIB_ROWS, C = RIB_COLS, arr = this.geo.attributes.position.array;
    const curve = new THREE.CatmullRomCurve3(this.p, false, "centripetal", 0.5);
    const F0 = V3(0, 0, 1), Fp = V3(), q = new THREE.Quaternion();
    // Moldura da fita por transporte paralelo (sem virar de lado de uma linha para outra), puxada
    // devagar para a orientação da pose.
    const frames = [];
    let Wp = null;
    for (let r = 0; r < R; r++) {
      const sc = r / (R - 1), T = curve.getTangentAt(sc);
      Fp.copy(F0).lerp(pose.face, smooth(0, 1, sc)).normalize();
      q.setFromAxisAngle(T, pose.twist * sc + 0.28 * Math.sin(t * 2.1 - sc * 7 + phase) * sc);
      Fp.applyQuaternion(q);
      const want = V3().crossVectors(T, Fp);
      if (want.lengthSq() < 1e-6) want.copy(Wp || this.prevW); want.normalize();
      let W;
      if (!Wp) W = want;
      else {
        W = Wp.clone().addScaledVector(T, -Wp.dot(T)).normalize();
        if (want.dot(W) < 0) want.negate();
        W.lerp(want, 0.18).normalize();
      }
      Wp = W; frames.push({ T, W, F: V3().crossVectors(W, T).normalize() });
    }
    this.prevW.copy(frames[0].W);
    const fr = [], bk = [];
    for (let r = 0; r < R; r++) {
      const s = r / (R - 1);
      for (let c = 0; c < C; c++) {
        const w = -1 + (2 * c) / (C - 1), sc = Math.min(s, 1 - (RIB_NOTCH / this.L) * (1 - Math.abs(w)));
        const P = curve.getPointAt(sc), { W, F } = frames[Math.round(sc * (R - 1))];
        // O cetim faz uma "canaleta" leve no meio.
        const mid = P.clone().addScaledVector(W, w * this.hw).addScaledVector(F, 0.006 * (1 - w * w));
        fr.push(mid.clone().addScaledVector(F, RIB_T / 2)); bk.push(mid.addScaledVector(F, -RIB_T / 2));
      }
    }
    let o = 0; const put = (v) => { arr[o++] = v.x; arr[o++] = v.y; arr[o++] = v.z; };
    fr.forEach(put); bk.forEach(put);
    for (const c of [0, C - 1]) { for (let r = 0; r < R; r++) put(fr[r * C + c]); for (let r = 0; r < R; r++) put(bk[r * C + c]); }
    this.geo.attributes.position.needsUpdate = true; this.geo.computeVertexNormals();
  }
}
// Tubo com raio variável ao longo da curva (braço).
class TaperTube {
  constructor(segs, radial) {
    this.segs = segs; this.radial = radial;
    const idx = [];
    for (let i = 0; i < segs; i++) for (let j = 0; j < radial; j++) { const a = i * (radial + 1) + j, b = a + radial + 1; idx.push(a, b, a + 1, b, b + 1, a + 1); }
    const n = (segs + 1) * (radial + 1);
    this.geo = new THREE.BufferGeometry();
    this.geo.setAttribute("position", new THREE.BufferAttribute(new Float32Array(n * 3), 3));
    this.geo.setAttribute("normal", new THREE.BufferAttribute(new Float32Array(n * 3), 3));
    this.geo.setIndex(idx);
  }
  update(curve, rfn) {
    const { segs, radial } = this, fr = curve.computeFrenetFrames(segs, false);
    const p = this.geo.attributes.position.array, nn = this.geo.attributes.normal.array; let o = 0;
    for (let i = 0; i <= segs; i++) {
      const t = i / segs, c = curve.getPointAt(t), r = rfn(t), N = fr.normals[i], B = fr.binormals[i];
      for (let j = 0; j <= radial; j++) {
        const a = j / radial * Math.PI * 2, cx = Math.cos(a), sx = Math.sin(a);
        const nx = cx * N.x + sx * B.x, ny = cx * N.y + sx * B.y, nz = cx * N.z + sx * B.z;
        p[o] = c.x + r * nx; p[o + 1] = c.y + r * ny; p[o + 2] = c.z + r * nz;
        nn[o] = nx; nn[o + 1] = ny; nn[o + 2] = nz; o += 3;
      }
    }
    this.geo.attributes.position.needsUpdate = true; this.geo.attributes.normal.needsUpdate = true; this.geo.computeBoundingSphere();
  }
}

// Luva de desenho animado feita direito: punho enrolado, palma fofa, três dedos com duas falanges,
// polegar articulado e as três costuras no dorso. Dedos para +Y, palma para +Z, punho na origem.
function makeGlove(a, s) {
  const g = new THREE.Group();
  const cuff = new THREE.Mesh(new THREE.LatheGeometry([V3(0.036, -0.04), V3(0.043, -0.016), V3(0.055, 0.004), V3(0.06, 0.018), V3(0.052, 0.03), V3(0.044, 0.028)], 36), a.gloveCuff);
  const palm = new THREE.Mesh(new THREE.SphereGeometry(1, 30, 22), a.glove); palm.scale.set(0.074, 0.072, 0.047); palm.position.y = 0.078;
  g.add(cuff, palm);
  const fingers = [-1, 0, 1].map((i) => {
    const base = new THREE.Group(); base.position.set(i * 0.035, 0.128 - Math.abs(i) * 0.01, 0.002);
    const p1 = new THREE.Mesh(new THREE.CapsuleGeometry(0.0228, 0.028, 8, 14), a.glove); p1.position.y = 0.022;
    const mid = new THREE.Group(); mid.position.y = 0.047;
    const p2 = new THREE.Mesh(new THREE.CapsuleGeometry(0.0212, 0.02 - Math.abs(i) * 0.005, 8, 14), a.glove); p2.position.y = 0.018;
    mid.add(p2); base.add(p1, mid); g.add(base);
    return { base, mid, i };
  });
  const thumb = new THREE.Group(); thumb.position.set(-s * 0.056, 0.058, 0.016);
  const t1 = new THREE.Mesh(new THREE.CapsuleGeometry(0.0225, 0.022, 8, 14), a.glove); t1.position.y = 0.02;
  const tmid = new THREE.Group(); tmid.position.y = 0.042;
  const t2 = new THREE.Mesh(new THREE.CapsuleGeometry(0.0205, 0.016, 8, 14), a.glove); t2.position.y = 0.016;
  tmid.add(t2); thumb.add(t1, tmid); g.add(thumb);
  for (const x of [-0.022, 0, 0.022]) { const sm = new THREE.Mesh(new THREE.CapsuleGeometry(0.0024, 0.05, 4, 6), a.gloveSeam); sm.position.set(x, 0.075, -0.0445); g.add(sm); }
  return { g, fingers, thumb, tmid };
}

// Braço: ombro preso na borda da capa, braço e antebraço resolvidos por IK de dois ossos
// (o cotovelo aponta para fora e para baixo), afinando até o punho.
const ARM_L1 = 0.35, ARM_L2 = 0.33, GLOVE_S = 1.4;
const PEN_GRIP = V3(0.004, 0.12, 0.036), PEN_TIP = V3(0.004, 0.12 + 0.175, 0.036);   // na luva
class Arm {
  constructor(s, a) {
    this.s = s; this.tube = new TaperTube(40, 18);
    this.mesh = new THREE.Mesh(this.tube.geo, a.arm);
    this.socket = new THREE.Mesh(new THREE.SphereGeometry(0.066, 24, 16), a.arm);
    Object.assign(this, makeGlove(a, s));
    this.g.scale.setScalar(GLOVE_S);
    this.pos = V3(); this.q = new THREE.Quaternion(); this.curls = [0.3, 0.3, 0.3]; this.thumbC = 0.3; this.ready = false;
  }
  apply(dt, S, P) {
    const k = this.ready ? 1 - Math.exp(-dt * (P.fast ? 26 : 9)) : 1;
    this.ready = true;
    this.pos.lerp(P.pos, k);
    for (let i = 0; i < 3; i++) this.curls[i] += (P.curls[i] - this.curls[i]) * k;
    this.thumbC += (P.thumb - this.thumbC) * k;
    const D = this.pos.clone().sub(S); let d = D.length(); D.normalize();
    const max = (ARM_L1 + ARM_L2) * 0.995;
    d = clamp(d, Math.abs(ARM_L1 - ARM_L2) + 0.05, max);
    const W = S.clone().addScaledVector(D, d);
    const along = (ARM_L1 * ARM_L1 - ARM_L2 * ARM_L2 + d * d) / (2 * d), h = Math.sqrt(Math.max(0, ARM_L1 * ARM_L1 - along * along));
    const pole = V3(this.s * 0.75, -0.5, -0.3); pole.addScaledVector(D, -pole.dot(D)).normalize();
    const E = S.clone().addScaledVector(D, along).addScaledVector(pole, h);
    const curve = new THREE.CatmullRomCurve3([S, S.clone().lerp(E, 0.5), E, E.clone().lerp(W, 0.5), W], false, "centripetal");
    this.tube.update(curve, (t) => 0.06 - 0.016 * t + 0.005 * Math.sin(2 * Math.PI * t));
    this.socket.position.copy(S);
    const fore = W.clone().sub(E).normalize();
    const yAx = (P.dir ? P.dir.clone().normalize().lerp(fore, 0.15) : fore).normalize();
    const xAx = V3().crossVectors(yAx, P.face.clone().normalize()); if (xAx.lengthSq() < 1e-4) xAx.set(1, 0, 0);
    xAx.normalize(); const zAx = V3().crossVectors(xAx, yAx);
    const want = new THREE.Quaternion().setFromRotationMatrix(new THREE.Matrix4().makeBasis(xAx, yAx, zAx));
    if (k === 1) this.q.copy(want); else this.q.slerp(want, k);
    this.g.position.copy(W); this.g.quaternion.copy(this.q);
    for (const f of this.fingers) { const c = this.curls[f.i + 1]; f.base.rotation.set(c * 1.15, 0, -f.i * 0.14 * (1 - c)); f.mid.rotation.x = c * 1.4; }
    const tc = this.thumbC;
    this.thumb.rotation.set(tc * 0.55, 0, this.s * (0.9 - tc * 0.55)); this.tmid.rotation.x = tc * 0.9;
    return W;
  }
}

// Tênis branco e azul: solado com cravos, biqueira branca, painel e gola azuis, cadarço.
// Origem no tornozelo, bico para +Z, sola para -Y.
function makeShoe(a, s) {
  const g = new THREE.Group();
  const sole = new THREE.Mesh(new RoundedBoxGeometry(0.15, 0.045, 0.29, 4, 0.02), a.sole); sole.position.set(0, -0.09, 0.045);
  const tread = new THREE.Group();
  for (let i = 0; i < 7; i++) { const b = new THREE.Mesh(new RoundedBoxGeometry(0.12, 0.008, 0.016, 2, 0.003), a.tread); b.position.set(0, -0.114, -0.07 + i * 0.038); tread.add(b); }
  const body = new THREE.Mesh(new THREE.SphereGeometry(1, 32, 20), a.shoeWhite); body.scale.set(0.074, 0.062, 0.14); body.position.set(0, -0.045, 0.045);
  const toe = new THREE.Mesh(new THREE.SphereGeometry(1, 28, 16), a.shoeWhite); toe.scale.set(0.072, 0.05, 0.075); toe.position.set(0, -0.055, 0.115);
  const panel = new THREE.Mesh(new THREE.SphereGeometry(1, 28, 16), a.shoeBlue); panel.scale.set(0.077, 0.05, 0.075); panel.position.set(0, -0.04, -0.035);
  const collar = new THREE.Mesh(new THREE.TorusGeometry(0.052, 0.02, 12, 32), a.shoeBlue); collar.rotation.x = Math.PI / 2 - 0.15; collar.position.set(0, 0.0, -0.01);
  const tongue = new THREE.Mesh(new RoundedBoxGeometry(0.07, 0.022, 0.09, 3, 0.01), a.shoeBlue); tongue.position.set(0, 0.002, 0.05); tongue.rotation.x = 0.35;
  g.add(sole, tread, body, toe, panel, collar, tongue);
  for (let i = 0; i < 3; i++) { const l = new THREE.Mesh(new THREE.CapsuleGeometry(0.008, 0.06, 4, 8), a.shoeWhite); l.rotation.z = Math.PI / 2; l.position.set(0, 0.012 - i * 0.012, 0.03 + i * 0.03); l.rotation.y = (i % 2 ? 0.2 : -0.2); g.add(l); }
  const stripe = new THREE.Mesh(new THREE.TorusGeometry(0.045, 0.008, 8, 24, Math.PI), a.shoeBlue); stripe.position.set(s * 0.07, -0.05, 0.05); stripe.rotation.y = s * Math.PI / 2; g.add(stripe);
  return g;
}
// Perna: quadril embaixo da capa, coxa e canela por IK (joelho para a frente), tênis no tornozelo.
const LEG_L1 = 0.23, LEG_L2 = 0.22;
class Leg {
  constructor(s, a) {
    this.s = s; this.tube = new TaperTube(32, 16); this.mesh = new THREE.Mesh(this.tube.geo, a.arm);
    this.shoe = makeShoe(a, s); this.shoe.scale.setScalar(1.3); this.ankle = V3(); this.ready = false; this.pitch = 0;
  }
  apply(dt, H, target, pitch) {
    const k = this.ready ? 1 - Math.exp(-dt * 14) : 1; this.ready = true;
    this.ankle.lerp(target, k); this.pitch += (pitch - this.pitch) * k;
    const D = this.ankle.clone().sub(H); let d = D.length(); D.normalize();
    d = clamp(d, 0.08, (LEG_L1 + LEG_L2) * 0.995);
    const A = H.clone().addScaledVector(D, d);
    const along = (LEG_L1 * LEG_L1 - LEG_L2 * LEG_L2 + d * d) / (2 * d), h = Math.sqrt(Math.max(0, LEG_L1 * LEG_L1 - along * along));
    const pole = V3(this.s * 0.25, 0, 1); pole.addScaledVector(D, -pole.dot(D)).normalize();
    const Kn = H.clone().addScaledVector(D, along).addScaledVector(pole, h);
    this.tube.update(new THREE.CatmullRomCurve3([H, H.clone().lerp(Kn, 0.5), Kn, Kn.clone().lerp(A, 0.5), A], false, "centripetal"), (t) => 0.062 - 0.016 * t + 0.004 * Math.sin(2 * Math.PI * t));
    this.shoe.position.copy(A);
    this.shoe.rotation.set(this.pitch, this.s * 0.14, 0);
    return A;
  }
}

// Empurra um ponto para fora do volume do livro (para a frente ou para trás, o que for mais perto).
function pushOut(p, flap) {
  const ax = Math.abs(p.x);
  if (ax > UB + 0.1 || p.y < Y0 - 0.14 || p.y > Y1 + 0.1) return;
  const u = clamp(ax - PH, 0, 1), v = clamp((p.y - Y0) / (Y1 - Y0), 0, 1);
  const zf = zSheetF(flap, K - 1, u, v) + 0.045, zb = zC(u) - COVER_T - 0.05;
  if (p.z < zf && p.z > zb) p.z = p.z > (zf + zb) / 2 - 0.05 ? zf : zb;
}

// ================================================================ o personagem
export class Folha3D {
  constructor(container, { mood = "idle", glasses = true, spin = false, intro = true, confetti = true, quality = 1, bleed = false } = {}) {
    const a = assets();
    this.container = container; this.mood = mood; this.intro = intro; this.bleed = bleed; this.glassesOn = glasses; this.spinEnabled = spin;
    this.renderer = new THREE.WebGLRenderer({ antialias: true, alpha: true, powerPreference: "low-power" });
    const r = this.renderer;
    r.setPixelRatio(Math.min(window.devicePixelRatio || 1, 2) * quality);
    r.toneMapping = THREE.ACESFilmicToneMapping; r.toneMappingExposure = 1.02;
    r.shadowMap.enabled = true; r.shadowMap.type = THREE.VSMShadowMap;
    r.domElement.style.display = "block"; r.domElement.style.width = "100%"; r.domElement.style.height = "100%";
    container.appendChild(r.domElement);

    const scene = this.scene = new THREE.Scene();
    const pmrem = new THREE.PMREMGenerator(r);
    scene.environment = pmrem.fromScene(new RoomEnvironment(), 0.04).texture; pmrem.dispose();
    scene.environmentIntensity = 0.6;
    this.camera = new THREE.PerspectiveCamera(27, 1, 0.1, 50);
    const key = new THREE.DirectionalLight("#fff3e2", 2.4);
    key.position.set(-2.4, 3.6, 3.4); key.castShadow = true;
    key.shadow.mapSize.set(1536, 1536);
    Object.assign(key.shadow.camera, { left: -2.2, right: 2.2, top: 2.2, bottom: -2.2, near: 0.5, far: 12 });
    key.shadow.radius = 10; key.shadow.blurSamples = 16; key.shadow.bias = -0.0008;
    scene.add(key);
    const rim = new THREE.DirectionalLight("#A497FF", 2.2); rim.position.set(2.6, 1.8, -3.2); scene.add(rim);
    const rim2 = new THREE.DirectionalLight("#C7B8FF", 1.1); rim2.position.set(-2.8, 1.2, -2.6); scene.add(rim2);
    scene.add(new THREE.HemisphereLight("#ffffff", "#B9AEEB", 0.45));

    this.GROUND = -1.27;
    const ground = new THREE.Mesh(new THREE.PlaneGeometry(14, 14), new THREE.ShadowMaterial({ opacity: 0.17, color: INK }));
    ground.rotation.x = -Math.PI / 2; ground.position.y = this.GROUND; ground.receiveShadow = true; scene.add(ground);
    this.blob = (() => {
      const [c, g] = canvas(256, 256), rg = g.createRadialGradient(128, 128, 0, 128, 128, 128);
      rg.addColorStop(0, "rgba(38,33,92,.6)"); rg.addColorStop(0.5, "rgba(38,33,92,.22)"); rg.addColorStop(1, "rgba(38,33,92,0)");
      g.fillStyle = rg; g.fillRect(0, 0, 256, 256);
      const m = new THREE.Mesh(new THREE.PlaneGeometry(2.9, 1.0), new THREE.MeshBasicMaterial({ map: new THREE.CanvasTexture(c), transparent: true, depthWrite: false }));
      m.rotation.x = -Math.PI / 2; m.position.y = this.GROUND + 0.003; scene.add(m); return m;
    })();

    const rig = this.rig = new THREE.Group(), body = this.body = new THREE.Group();
    scene.add(rig); rig.add(body);
    this.flap = 0; this.flapVel = 0; this.breath = 0.02;
    this.morphs = [];
    this.halves = {};

    // ---- as duas metades (capa, miolo, folhas soltas), cada uma num pivô na lombada
    for (const s of [-1, 1]) {
      const pivot = new THREE.Group(), inner = new THREE.Group();
      pivot.position.z = PIVZ; inner.position.z = -PIVZ; pivot.add(inner); body.add(pivot);
      this.halves[s] = { pivot, inner };
      // Capa dura acolchoada: caixa de cantos bem arredondados, entortada para o formato do livro
      // aberto (normais transformadas na mão, sem costura); a borda de fora abana com as folhas.
      {
        const H = (VB1 - VB0) * (Y1 - Y0) + 0.2, yc = Y0 + (Y1 - Y0) * (VB0 + VB1) / 2;
        const geo = new RoundedBoxGeometry(U1, H, COVER_T, 6, 0.038);
        const P = geo.attributes.position, N = geo.attributes.normal, n = P.count;
        const delta = new Float32Array(n * 3);
        for (let i = 0; i < n; i++) {
          const x = P.getX(i) + U1 / 2, y = P.getY(i) + yc, z = P.getZ(i) - COVER_T / 2;
          const yo = yOff(x), dyo = 0.15 * (1 - clamp(x, 0, 1));          // derivada de yOff
          P.setXYZ(i, s * (HINGE + x), y + yo, zC(x) - 0.003 + z);
          const nx = N.getX(i) - 0.12 * N.getZ(i) - dyo * N.getY(i);
          const v = V3(s * nx, N.getY(i), N.getZ(i)).normalize(); N.setXYZ(i, v.x, v.y, v.z);
          delta[i * 3 + 2] = 0.5 * x * x * x;
        }
        geo.morphAttributes.position = [new THREE.BufferAttribute(delta, 3)]; geo.morphTargetsRelative = true;
        geo.computeBoundingSphere(); geo.boundingSphere.radius += 0.1;
        fillColor(geo, 1);
        // Grupos da caixa: +x, -x, +y, -y, +z (dentro), -z (fora).
        const outer = a.coverOut[s < 0 ? 0 : 1];
        const mesh = cast(new THREE.Mesh(geo, [a.coverEdge, a.coverEdge, a.coverEdge, a.coverEdge, a.coverIn, outer]));
        mesh.updateMorphTargets(); mesh.morphTargetInfluences[0] = 0; this.morphs.push(mesh);
        inner.add(mesh);
      }

      // Miolo: bloco de folhas com as bordas listradas (um pouco maior que as folhas soltas).
      const bot = (u) => zC(u) + 0.004;
      const blockSide = (fn) => inner.add(cast(new THREE.Mesh(new Grid(40, 8, (u, t) => [u, t]).update(fn).geo, a.blockEdge)));
      blockSide((u, t) => { const uu = u * UB; return V3(xP(s, uu), py(uu, VB1), bot(uu) + (zB(uu) - bot(uu)) * t); });
      blockSide((u, t) => { const uu = u * UB; return V3(xP(s, uu), py(uu, VB0), bot(uu) + (zB(uu) - bot(uu)) * t); });
      blockSide((v, t) => { const vv = VB0 + v * (VB1 - VB0); return V3(xP(s, UB), py(UB, vv), bot(UB) + (zB(UB) - bot(UB)) * t); });
      const top = new Grid(36, 10, spreadUV(s)).update((u, v) => V3(xP(s, u * UB), py(u * UB, VB0 + v * (VB1 - VB0)), zB(u * UB)));
      fillColor(top.geo, 0.8);
      inner.add(cast(new THREE.Mesh(top.geo, a.paperBack)));

      for (let k = 0; k < K; k++) {
        const shade = 0.8 + 0.2 * (k / (K - 1)), edgeShade = 0.88 + 0.12 * rng(k * 13 + 5)();
        const sheet = new Slab(36, 14, (u, v) => spreadUV(s)(u * uEnd(k), vLo(k) + v * (vHi(k) - vLo(k))), undefined, shade, edgeShade);
        const fn = (u, v) => {
          const [ru, rv] = roundUV(u, v, 0.035, 0.03);
          const uu = ru * uEnd(k), vv = vLo(k) + rv * (vHi(k) - vLo(k));
          return V3(xP(s, uu), py(uu, vv), zSheetF(this.flap, k, uu, vv));
        };
        const mesh = cast(new THREE.Mesh(sheet.geo, [a.paper, a.paperBack, a.paperEdge]));
        this.morph(mesh, () => sheet.update(fn, PAPER_T));
        inner.add(mesh);
      }
    }
    // Lombada por trás, com nervuras, e o cabeceado listrado.
    {
      const h = (VB1 - VB0) * (Y1 - Y0) + 0.08, R = HINGE + COVER_T * 0.5, yc = (Y0 + Y1) / 2 + yOff(0);
      const sp = new THREE.Mesh(new THREE.CylinderGeometry(R, R, h, 32, 1, true, Math.PI / 2, Math.PI), a.coverEdge);
      fillColor(sp.geometry, 1); sp.position.set(0, yc, zC(0) - COVER_T * 0.5); body.add(cast(sp));
      for (const y of [-0.42, -0.14, 0.14, 0.42]) {
        const band = new THREE.Mesh(new THREE.CylinderGeometry(R + 0.008, R + 0.008, 0.034, 32, 1, true, Math.PI / 2, Math.PI), a.coverEdge);
        fillColor(band.geometry, 1.15); band.position.set(0, yc + y, zC(0) - COVER_T * 0.5); body.add(cast(band));
      }
      for (const v of [0, 1]) {
        const hb = new THREE.Mesh(new THREE.CylinderGeometry(0.017, 0.017, 2 * HINGE + 0.03, 16), a.headband);
        hb.rotation.z = Math.PI / 2; hb.position.set(0, py(0, v) + (v ? 0.004 : -0.004), zC(0) + 0.012); body.add(cast(hb));
      }
    }

    // ---- folhas que viram
    this.HZ = zB(0);
    this.flips = Array.from({ length: 4 }, (_, i) => {
      const fg = new Grid(40, 14, spreadUV(1)), bg = new Grid(40, 14, spreadUV(-1));
      fg.update(this.flipShape(0));
      bg.geo.setAttribute("position", fg.geo.attributes.position); bg.geo.setAttribute("normal", fg.geo.attributes.normal); bg.geo.setIndex(fg.geo.index);
      const mf = new THREE.Mesh(fg.geo, a.flipF), mb = new THREE.Mesh(bg.geo, a.flipB);
      for (const m of [mf, mb]) { m.castShadow = true; m.renderOrder = 10 + i; m.visible = false; m.frustumCulled = false; body.add(m); }
      return { fg, mf, mb, p: -1, dur: 0.9 };
    });

    // ---- rosto
    this.EYE_U = 0.42; this.EYE_V = 0.565;
    const endCaps = (mesh, pts, rad) => { for (const p of pts) { const c = new THREE.Mesh(new THREE.SphereGeometry(rad, 12, 8), a.inkGloss); c.position.copy(p); mesh.add(c); } return mesh; };
    this.eyes = [-1, 1].map((s) => {
      const g = new THREE.Group();
      const domeGeo = new THREE.SphereGeometry(1, 48, 24, 0, Math.PI * 2, 0, Math.PI / 2); domeGeo.rotateX(Math.PI / 2);
      const ball = new THREE.Mesh(domeGeo, a.eye); ball.scale.set(0.074, 0.1, 0.036);
      const hi1 = new THREE.Mesh(new THREE.SphereGeometry(1, 20, 12), a.shine); hi1.scale.set(0.024, 0.03, 0.01);
      const hi2 = new THREE.Mesh(new THREE.SphereGeometry(1, 16, 10), a.shine); hi2.scale.set(0.011, 0.011, 0.006);
      const arc = endCaps(new THREE.Mesh(new THREE.TubeGeometry(new THREE.QuadraticBezierCurve3(V3(-0.075, -0.025, 0), V3(0, 0.085, 0.02), V3(0.075, -0.025, 0)), 28, 0.016, 10), a.inkGloss), [V3(-0.075, -0.025, 0), V3(0.075, -0.025, 0)], 0.016);
      const brow = new THREE.Group();
      brow.add(endCaps(new THREE.Mesh(new THREE.TubeGeometry(new THREE.QuadraticBezierCurve3(V3(-0.065, -0.008, 0), V3(0, 0.02, 0.01), V3(0.065, -0.008, 0)), 20, 0.0125, 8), a.inkGloss), [V3(-0.065, -0.008, 0), V3(0.065, -0.008, 0)], 0.0125));
      g.add(ball, hi1, hi2, arc, brow);
      this.halves[s].inner.add(front(g));
      return { s, g, ball, hi1, hi2, arc, brow, browY: 0.16, browR: 0 };
    });
    this.cheeks = [-1, 1].map((s) => {
      const c = new THREE.Mesh(new THREE.SphereGeometry(1, 28, 14, 0, Math.PI * 2, 0, Math.PI / 2), a.felt);
      c.geometry.rotateX(Math.PI / 2); c.scale.set(0.085, 0.052, 0.012);
      this.halves[s].inner.add(front(c)); return { s, c };
    });
    this.mouth = new THREE.Group();
    const smileCurve = () => new THREE.TubeGeometry(new THREE.QuadraticBezierCurve3(V3(-0.1, 0.03, 0), V3(0, -0.085, 0.014), V3(0.1, 0.03, 0)), 28, 0.02, 10);
    this.mouths = {
      smile: endCaps(new THREE.Mesh(smileCurve(), a.inkGloss), [V3(-0.1, 0.03, 0), V3(0.1, 0.03, 0)], 0.02),
      frown: endCaps(new THREE.Mesh(smileCurve(), a.inkGloss), [V3(-0.1, 0.03, 0), V3(0.1, 0.03, 0)], 0.02),
      think: endCaps(new THREE.Mesh(new THREE.TubeGeometry(new THREE.CubicBezierCurve3(V3(-0.06, 0, 0), V3(-0.02, -0.03, 0.01), V3(0.03, 0.02, 0.01), V3(0.07, 0, 0)), 24, 0.018, 10), a.inkGloss), [V3(-0.06, 0, 0), V3(0.07, 0, 0)], 0.018),
      open: new THREE.Group(), grin: new THREE.Group(),
    };
    this.mouths.frown.rotation.z = Math.PI; this.mouths.frown.scale.setScalar(0.7);
    {
      const o = new THREE.Mesh(new THREE.SphereGeometry(1, 32, 16), a.inkGloss); o.scale.set(0.072, 0.05, 0.02);
      const tg = new THREE.Mesh(new THREE.SphereGeometry(1, 24, 12), a.tongue); tg.scale.set(0.04, 0.018, 0.012); tg.position.set(0, -0.024, 0.012);
      this.mouths.open.add(o, tg); this.mouths.open.userData = { o, tg };
      const sh = new THREE.Shape(); sh.moveTo(-0.12, 0.03); sh.quadraticCurveTo(0, -0.25, 0.12, 0.03); sh.quadraticCurveTo(0, 0.045, -0.12, 0.03);
      const grin = new THREE.Mesh(new THREE.ExtrudeGeometry(sh, { depth: 0.012, bevelEnabled: true, bevelThickness: 0.008, bevelSize: 0.008, bevelSegments: 4, curveSegments: 28 }), a.inkGloss);
      const tg2 = new THREE.Mesh(new THREE.SphereGeometry(1, 24, 12), a.tongue); tg2.scale.set(0.05, 0.024, 0.01); tg2.position.set(0, -0.068, 0.022);
      this.mouths.grin.add(grin, tg2);
    }
    for (const m of Object.values(this.mouths)) this.mouth.add(m);
    body.add(front(this.mouth));

    // ---- óculos
    const glassesG = this.glasses = new THREE.Group();
    const GL = { cx: PH + this.EYE_U, R: 0.205 };
    for (const s of [-1, 1]) {
      const rimM = new THREE.Mesh(new THREE.TorusGeometry(GL.R, 0.017, 18, 80), a.frame); rimM.position.x = s * GL.cx;
      const inner = new THREE.Mesh(new THREE.TorusGeometry(GL.R - 0.013, 0.0055, 8, 80), a.frameGold); inner.position.set(s * GL.cx, 0, 0.011);
      const lens = new THREE.Mesh(new THREE.CircleGeometry(GL.R - 0.004, 64), a.lens); lens.position.x = s * GL.cx; lens.userData.lens = true;
      const hinge = new THREE.Mesh(new RoundedBoxGeometry(0.05, 0.034, 0.03, 2, 0.008), a.frameGold); hinge.position.set(s * (GL.cx + GL.R + 0.012), 0.025, -0.006);
      glassesG.add(rimM, inner, lens, hinge);
    }
    {
      const b = GL.cx - GL.R;
      glassesG.add(new THREE.Mesh(new THREE.TubeGeometry(new THREE.CubicBezierCurve3(V3(-b - 0.004, 0.06, 0), V3(-b * 0.5, 0.15, 0.03), V3(b * 0.5, 0.15, 0.03), V3(b + 0.004, 0.06, 0)), 32, 0.0145, 10), a.frame));
      const g0 = this.glassesRest(), loc = (x, y, z) => V3(x, y - g0.y, z - g0.z), zE = this.zTop(1, this.EYE_V);
      for (const s of [-1, 1]) {
        const pts = [V3(s * (GL.cx + GL.R + 0.03), 0.025, -0.01), loc(s * 0.92, g0.y + 0.03, zE + 0.07), loc(s * (UB + 0.035), g0.y + 0.01, zE + 0.01), loc(s * (UB + 0.065), g0.y - 0.02, zE - 0.07), loc(s * (UB + 0.025), g0.y - 0.08, zE - 0.12)];
        glassesG.add(new THREE.Mesh(new THREE.TubeGeometry(new THREE.CatmullRomCurve3(pts), 48, 0.0115, 8), a.frame));
        const tip = new THREE.Mesh(new THREE.SphereGeometry(0.016, 12, 8), a.frame); tip.position.copy(pts[4]); glassesG.add(tip);
      }
    }
    body.add(front(cast(glassesG)));
    glassesG.traverse((m) => { if (m.isMesh) { m.receiveShadow = false; if (m.userData.lens) m.castShadow = false; } });
    this.glassesK = 0; this.glassBob = 0; this.glassVel = 0;

    // ---- capelo: casquete oval (do formato do alto do livro), assentado sobre as folhas; a tábua,
    // o cordão e a borla giram à parte.
    const cap = this.cap = new THREE.Group(), capInner = this.capInner = new THREE.Group();
    this.capBase = V3(0, py(0.34, VB1) + 0.004, 0.11);
    const BOARD = 0.94;
    this.CORNER = V3(BOARD / Math.SQRT2 - 0.006, 0.232, 0);
    cap.position.copy(this.capBase); cap.rotation.set(0.05, 0, -0.05);
    capInner.rotation.y = -0.28;
    {
      const prof = [V3(0.33, -0.005), V3(0.332, 0.03), V3(0.32, 0.1), V3(0.302, 0.16), V3(0.286, 0.2), V3(0.27, 0.216)];
      const skullGeo = new THREE.LatheGeometry(prof, 96);
      const P = skullGeo.attributes.position, v = V3();
      for (let i = 0; i < P.count; i++) {
        v.fromBufferAttribute(P, i);
        const ang = Math.atan2(v.z, v.x), sc = 1 + 0.01 * Math.sin(ang * 6 + v.y * 8) + 0.005 * Math.sin(ang * 13);   // dobras do tecido
        P.setXYZ(i, v.x * sc, v.y, v.z * sc * 0.62);
      }
      skullGeo.computeVertexNormals();
      const skull = new THREE.Mesh(skullGeo, a.cap);
      const bandGeo = new THREE.TorusGeometry(0.333, 0.013, 10, 96); bandGeo.rotateX(Math.PI / 2); bandGeo.scale(1, 1, 0.62);
      const band = new THREE.Mesh(bandGeo, a.cap); band.position.y = 0.012;
      const board = new THREE.Mesh(new RoundedBoxGeometry(BOARD, 0.044, BOARD, 4, 0.016), a.capBoard); board.rotation.y = Math.PI / 4; board.position.y = 0.21;
      const button = new THREE.Mesh(new THREE.SphereGeometry(0.04, 24, 12, 0, Math.PI * 2, 0, Math.PI / 2), a.capBoard); button.scale.y = 0.6; button.position.y = 0.231;
      cap.add(skull, band);
      capInner.add(board, button);
      cap.add(capInner);
    }
    body.add(cast(cap));
    this.cord = new THREE.Mesh(new THREE.BufferGeometry(), a.thread); this.cord.castShadow = true; capInner.add(this.cord);
    this.tasselG = new THREE.Group();
    this.STRANDS = 46; this.CORD_L = 0.2;
    this.strands = new THREE.InstancedMesh(new THREE.CylinderGeometry(0.0042, 0.0034, 1, 5, 1), a.thread, this.STRANDS);
    {
      const head = new THREE.Mesh(new THREE.LatheGeometry([V3(0.001, 0.0), V3(0.016, 0.004), V3(0.03, -0.022), V3(0.027, -0.045), V3(0.022, -0.052)], 32), a.gold);
      const ring = new THREE.Mesh(new THREE.TorusGeometry(0.025, 0.0065, 8, 32), a.gold); ring.rotation.x = Math.PI / 2; ring.position.y = -0.056;
      const charm = new THREE.Mesh(new THREE.CylinderGeometry(0.03, 0.03, 0.006, 32), a.gold); charm.rotation.x = Math.PI / 2; charm.position.set(0.045, -0.02, 0.02);
      this.tasselG.add(head, ring, this.strands, charm);
    }
    capInner.add(cast(this.tasselG));
    this.tas = { p: null, prev: null, flare: 0 };
    this.capOff = intro ? 1.8 : 0; this.capVel = 0;

    // ---- braços
    this.arms = [-1, 1].map((s) => { const arm = new Arm(s, a); body.add(cast(arm.mesh), cast(arm.socket), cast(arm.g)); return arm; });

    // ---- pernas
    this.legs = [-1, 1].map((s) => { const leg = new Leg(s, a); body.add(cast(leg.mesh), cast(leg.shoe)); return leg; });

    // ---- fita marcadora (menta), pendurada da lombada
    this.bookmark = new Ribbon(0, a.ribbonMint, 0.42, 0.04); body.add(this.bookmark.mesh);

    // ---- lápis, na mão direita (a ponta fica para o lado dos dedos)
    const pencil = this.pencil = new THREE.Group();
    {
      const stick = new THREE.Mesh(new THREE.CylinderGeometry(0.0145, 0.0145, 0.24, 6), a.pencil);
      const fer = new THREE.Mesh(new THREE.CylinderGeometry(0.0155, 0.0155, 0.03, 20), a.ferrule); fer.position.y = 0.135;
      const eraser = new THREE.Mesh(new THREE.CapsuleGeometry(0.0148, 0.012, 6, 16), a.eraser); eraser.position.y = 0.158;
      const wood = new THREE.Mesh(new THREE.CylinderGeometry(0.0145, 0.004, 0.045, 6), a.wood); wood.position.y = -0.1425;
      const lead = new THREE.Mesh(new THREE.ConeGeometry(0.0042, 0.012, 12), a.lead); lead.rotation.x = Math.PI; lead.position.y = -0.169;
      pencil.add(stick, fer, eraser, wood, lead);
    }
    pencil.position.copy(PEN_GRIP); pencil.rotation.x = Math.PI;
    pencil.scale.setScalar(0.001); pencil.visible = false; this.arms[1].g.add(cast(pencil)); this.pencilK = 0;

    // ---- escrita a lápis na página da direita (decalque recortado, desenhado antes da folha que vira)
    {
      const [wc, wg] = canvas(1024, 512);
      this.wCanvas = wc; this.wCtx = wg; this.wDirty = false; this.wFrame = 0;
      this.writeTex = tex(wc);
      const wm = new THREE.MeshStandardMaterial({ map: this.writeTex, alphaTest: 0.35, depthWrite: false, roughness: 0.55, metalness: 0.25, polygonOffset: true, polygonOffsetFactor: -3 });
      const wgrid = new Grid(36, 14, spreadUV(1));
      const wmesh = new THREE.Mesh(wgrid.geo, wm); wmesh.renderOrder = 5;
      this.morph(wmesh, () => wgrid.update((u, v) => this.topPos(1, u, v, 0.0012)));
      this.halves[1].inner.add(wmesh);
      const [fc, fgc] = canvas(1024, 512);
      this.fCanvas = fc; this.fCtx = fgc; this.flipTex = tex(fc);
      this.flipFMat = a.flipF.clone(); this.flipFMat.map = this.flipTex;
      this.writer = { line: 0, u: 0.2, last: null };
    }
    this.penTip = V3(); this.penN = V3(0, 0, 1);
    this.handFlip = null; this.smoothT = 0;

    // ---- confete
    this.conf = [];
    if (confetti) {
      const CONF = 150;
      this.confetti = new THREE.InstancedMesh(new THREE.PlaneGeometry(0.04, 0.024), a.confetti, CONF);
      this.confetti.frustumCulled = false; this.confetti.castShadow = true;
      const cols = ["#F5B83D", "#7EE0B8", "#F4A6C0", "#6F64FF", "#ffffff", "#FFE07A"].map((c) => new THREE.Color(c));
      for (let i = 0; i < CONF; i++) { this.confetti.setColorAt(i, cols[i % cols.length]); this.conf.push({ life: 0, p: V3(), v: V3(), w: V3(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5).normalize(), spin: 4 + Math.random() * 8, ph: Math.random() * 6 }); }
      scene.add(this.confetti);
    }

    // ---- estado
    this.t = 0; this.look = new THREE.Vector2(); this.pointer = new THREE.Vector2();
    this.spin = 0; this.spinVel = 0; this.yaw = 0;
    this.blinkAt = 2.2; this.blinkT = -1; this.doubleBlink = false; this.winkT = -1; this.pokeT = -1;
    this.hop = 0; this.hopVel = 0; this.prevHopVel = 0;
    this.nextFlip = intro ? 1.3 : 3 + Math.random() * 4; this.introFlips = intro ? 4 : 0; this.readT = -1; this.nextRead = 6; this.prevSpin = 0;
    if (!intro) this.glassesK = this.glassesOn ? 1 : 0;
    this.reduce = window.matchMedia ? window.matchMedia("(prefers-reduced-motion: reduce)").matches : false;
    this.showMouth();
    this.resize(container.clientWidth || 300, container.clientHeight || 260);
  }

  // O abano das folhas é linear (z = base + abano * u³ * f): a malha é feita duas vezes e a GPU
  // mistura as duas (morph target), sem refazer nada a cada quadro.
  morph(mesh, build) {
    const keep = this.flap;
    this.flap = 0; build();
    const geo = mesh.geometry, p0 = geo.attributes.position.array.slice(), n0 = geo.attributes.normal.array.slice();
    this.flap = 1; build();
    const p1 = geo.attributes.position.array, d = new Float32Array(p0.length);
    for (let i = 0; i < d.length; i++) d[i] = p1[i] - p0[i];
    geo.attributes.position.array.set(p0); geo.attributes.normal.array.set(n0);
    geo.attributes.position.needsUpdate = geo.attributes.normal.needsUpdate = true;
    geo.morphAttributes.position = [new THREE.BufferAttribute(d, 3)];
    geo.morphTargetsRelative = true;
    geo.computeBoundingSphere(); geo.boundingSphere.radius += 0.1;
    mesh.updateMorphTargets(); mesh.morphTargetInfluences[0] = 0;
    this.morphs.push(mesh);
    this.flap = keep;
  }

  // ---- geometria do livro, com o abano/respiração das folhas desta instância
  zTop(u, v) { return zSheetF(this.flap, K - 1, u, v); }
  topPos(s, u, v, lift = 0) { return V3(xP(s, u), py(u, v), this.zTop(u, v) + lift); }
  topNormal(s, u, v) {
    const e = 0.01, du = this.topPos(s, u + e, v).sub(this.topPos(s, u - e, v)), dv = this.topPos(s, u, v + e).sub(this.topPos(s, u, v - e));
    const n = du.cross(dv).normalize();
    return s < 0 ? n.negate() : n;
  }
  glassesRest() { return V3(0, py(this.EYE_U, this.EYE_V) + 0.004, zSheetF(0, K - 1, this.EYE_U, this.EYE_V) + 0.15); }
  flipShape(p) {
    const w = Math.sin(2 * Math.PI * p) * 0.26, d = Math.sin(Math.PI * p) * 0.14, HZ = zB(0), b = this.breath;
    return (u, v) => {
      const xr = PH + u, zr = this.zTop(u, v) + 0.0035 - HZ;
      const r = Math.hypot(xr, zr), a = Math.atan2(zr, xr);
      const e = ease(clamp(p + w * u + d * u * (1 - v), 0, 1));
      const ang = a + (Math.PI - 2 * a) * e;
      return rotAbout(V3(r * Math.cos(ang), py(u, v), HZ + r * Math.sin(ang)), lerp(-b, b, e));
    };
  }
  // Vira uma folha. Com a mão: a direita vai até o canto de baixo, levanta e solta; a esquerda
  // pega no fim e alisa. O que estiver escrito vai junto com a folha (a de baixo está em branco).
  startFlip(dur = 0.9, hand = false) {
    if (hand && this.handFlip) return;
    const f = this.flips.find((x) => x.p < 0); if (!f) return;
    f.p = 0; f.dur = dur; f.wait = hand ? 0.38 : 0; f.released = !hand; f.catch = false;
    if (this.wDirty) {
      const g = this.fCtx; g.clearRect(0, 0, 1024, 512);
      g.drawImage(A.paperTex.image, 0, 0, 1024, 512); g.drawImage(this.wCanvas, 0, 0);
      this.flipTex.needsUpdate = true; f.mf.material = this.flipFMat;
      this.wCtx.clearRect(0, 0, 1024, 512); this.writeTex.needsUpdate = true; this.wDirty = false;
    } else f.mf.material = A.flipF;
    if (hand) this.handFlip = f;
  }
  corner(p) {
    const sh = this.flipShape(p), pt = sh(0.97, 0.16);
    const du = sh(1, 0.16).sub(sh(0.94, 0.16)).normalize(), dv = sh(0.97, 0.2).sub(sh(0.97, 0.12)).normalize();
    const n = V3().crossVectors(du, dv).normalize(); if (n.z < 0) n.negate();
    return { pt, du, n };
  }
  grabPose(p) {
    const { pt, du, n } = this.corner(p);
    return { pos: pt.clone().addScaledVector(n, 0.07).addScaledVector(du, 0.1), dir: du.clone().negate(), face: n.clone().negate(), curls: [0.42, 0.42, 0.48], thumb: 0.7, fast: true };
  }
  shoulder(s) { return rotAbout(V3(s * (HINGE + U1 + 0.01), -0.04, zC(U1) - COVER_T * 0.5), -s * this.breath); }
  hip(s) { return V3(s * 0.3, Y0 + (Y1 - Y0) * VB0 - 0.02, 0.1); }
  showMouth() {
    const m = this.mood;
    const k = m === "happy" ? "grin" : m === "talking" || m === "wave" ? "open" : m === "thinking" ? "think" : m === "sad" ? "frown" : "smile";
    for (const [n, x] of Object.entries(this.mouths)) x.visible = n === k;
  }
  setMood(m) {
    if (m === this.mood) return;
    this.mood = m; this.showMouth();
    if (m === "happy") { this.hopVel = 3.4; this.burst(); }
    if (m === "wink") this.winkT = 0;
    if (m === "thinking") this.nextFlip = Math.min(this.nextFlip, this.t + 0.3);
  }
  setGlasses(on) { this.glassesOn = on; }
  setPointer(x, y) { this.pointer.set(clamp(x, -1.5, 1.5), clamp(y, -1.5, 1.5)); }
  poke() { this.pokeT = 0; this.hopVel = 3.8; this.winkT = 0; this.startFlip(0.7); if (this.mood === "happy") this.burst(60); }
  hits(clientX, clientY) {
    const rect = this.renderer.domElement.getBoundingClientRect();
    const ray = new THREE.Raycaster();
    ray.setFromCamera(new THREE.Vector2((clientX - rect.left) / rect.width * 2 - 1, -((clientY - rect.top) / rect.height) * 2 + 1), this.camera);
    return ray.intersectObject(this.rig, true).length > 0;
  }
  burst(n = 110) {
    let k = 0;
    for (const c of this.conf) {
      if (c.life > 0 || k >= n) continue; k++;
      c.life = 3 + Math.random() * 1.5; c.p.set((Math.random() - 0.5) * 0.8, 0.9 + Math.random() * 0.2, 0.3);
      const ang = Math.random() * Math.PI * 2, sp = 1.2 + Math.random() * 2.2;
      c.v.set(Math.cos(ang) * sp * 0.8, 2.4 + Math.random() * 2.2, Math.sin(ang) * sp * 0.5 + 0.6);
    }
  }
  resize(w, h) {
    this.renderer.setSize(w, h, false);
    const asp = w / Math.max(1, h); this.camera.aspect = asp;
    // Quanto do mundo cabe: com sangria (no app) o livro enche a caixa e o resto do canvas sobra em volta.
    const tan = 2 * Math.tan(THREE.MathUtils.degToRad(this.camera.fov / 2));
    const [fw, fh] = this.bleed ? [3.1, 3.45] : [3.7, 3.55];
    const d = Math.max(fh / tan, fw / (tan * asp));
    this.camera.position.set(0, 0.26 * d, d); this.camera.lookAt(0, -0.16, 0);
    this.camera.updateProjectionMatrix();
  }
  dispose() {
    this.renderer.dispose(); this.renderer.forceContextLoss();
    this.renderer.domElement.remove();
  }

  // ---- pose das fitas: alvo da ponta, direção da ponta, para onde a face aponta, torção
  pose(s, t) {
    const m = this.mood, hf = this.handFlip;
    if (hf) {
      if (s > 0 && !hf.released) return this.grabPose(hf.wait > 0 ? 0 : hf.p);
      if (s < 0 && hf.catch) return this.grabPose(Math.min(1, hf.p));
    }
    if (s < 0 && this.smoothT > 0) return this.grabPose(1);
    if (s > 0 && m === "thinking" && this.pencilK > 0.3) return this.writePose();
    if (m === "happy") return { pos: V3(s * 1.3, 0.5 + Math.sin(t * 8 + s) * 0.05, 0.38), face: V3(0, 0, 1), curls: [0.95, 0.95, 0.95], thumb: 0.85 };
    if (m === "sad") return { pos: V3(s * 1.16, -0.78, 0.3), dir: V3(0, -1, 0.15).normalize(), face: V3(-s, 0, 0.4).normalize(), curls: [0.35, 0.35, 0.35], thumb: 0.3 };
    if (s > 0) {
      if (m === "wave") { const w = Math.sin(t * 9); return { pos: V3(1.26, 0.58, 0.38), dir: V3(w * 0.45, 1, 0.05).normalize(), face: V3(0, 0, 1), curls: [0.05, 0.05, 0.1], thumb: 0.1 }; }
      if (m === "point") return { pos: V3(1.4, 0.1, 0.44), dir: V3(1, 0.12, 0.15).normalize(), face: V3(0, -0.3, 1).normalize(), curls: [0, 0.95, 0.95], thumb: 0.75 };
      if (m === "talking") return { pos: V3(1.3, 0.0 + Math.sin(t * 3.2) * 0.08, 0.48), dir: V3(0.35, 1, 0.35).normalize(), face: V3(-0.3, 0.5, 0.8).normalize(), curls: [0.12, 0.15, 0.2], thumb: 0.15 };
      if (m === "wink") return { pos: V3(1.28, 0.18, 0.46), dir: V3(-0.25, 0.05, 1).normalize(), face: V3(-1, 0, 0), curls: [0.95, 0.95, 0.95], thumb: 0 };
    }
    // Parado: as mãos seguram as bordas de baixo do livro, polegar na frente da página.
    const pt = rotAbout(this.topPos(s, 0.94, 0.2), -s * this.breath);
    return { pos: pt.add(V3(s * 0.12, -0.04 + Math.sin(t * 1.6 + s) * 0.006, 0.09)), dir: V3(-s * 0.8, 0.45, -0.15).normalize(), face: V3(s * 0.2, 0, -1).normalize(), curls: [0.28, 0.32, 0.38], thumb: 0.25 };
  }
  // Escrevendo: a luva é orientada pelo lápis (ponta no papel, borracha para cima e para fora).
  writePose() {
    const A = this.penN.clone().multiplyScalar(0.7).add(V3(0.5, 0.45, 0)).normalize();
    const yAx = A.clone().negate(), face = this.penN.clone().negate();
    const xAx = V3().crossVectors(yAx, face).normalize(), zAx = V3().crossVectors(xAx, yAx);
    const q = new THREE.Quaternion().setFromRotationMatrix(new THREE.Matrix4().makeBasis(xAx, yAx, zAx));
    const wrist = this.penTip.clone().sub(PEN_TIP.clone().multiplyScalar(GLOVE_S).applyQuaternion(q));
    return { pos: wrist, dir: yAx, face, curls: [0.55, 0.68, 0.8], thumb: 0.62, fast: true };
  }

  // ---- um passo da animação
  update(dt) {
    const t = (this.t += dt), m = this.mood, reduce = this.reduce;
    const rig = this.rig, body = this.body;

    if (!this.drag) { this.spinVel += (-this.spin * 6 - this.spinVel * 3.2) * dt; this.spin += this.spinVel * dt; }
    this.look.lerp(this.pointer, 1 - Math.exp(-dt * 5));
    const look = this.look;
    rig.rotation.y = this.spin - 0.24 + this.yaw + look.x * 0.3 + (reduce ? 0 : Math.sin(t * 0.45) * 0.035);
    rig.rotation.x = -look.y * 0.1 + 0.05;
    rig.rotation.z = m === "thinking" ? Math.sin(t * 1.1) * 0.025 + 0.04 : m === "sad" ? 0.05 : Math.sin(t * 0.8) * 0.01;
    this.hopVel -= 15 * dt; this.hop += this.hopVel * dt;
    let landed = false;
    if (this.hop < 0) { landed = this.prevHopVel < -1; this.hop = 0; this.hopVel = m === "happy" && !reduce ? 3.1 : 0; }
    rig.position.y = this.hop * 0.2 + (reduce ? 0 : Math.sin(t * 2) * 0.014);
    this.blob.scale.setScalar(1 - this.hop * 0.14); this.blob.material.opacity = 1 - this.hop * 0.3;
    let sx = 1 + Math.sin(t * 1.6) * 0.004, sy = 1 + Math.sin(t * 1.6 + 0.6) * 0.009;
    if (landed && this.pokeT < 0) this.pokeT = 0.05;
    if (this.pokeT >= 0) { this.pokeT += dt; const k = Math.exp(-this.pokeT * 5) * Math.sin(this.pokeT * 22); sx += k * 0.06; sy -= k * 0.08; if (this.pokeT > 1.2) this.pokeT = -1; }
    body.scale.set(sx, sy, 1);

    // Respiração: as metades abrem e fecham de leve e as folhas sobem junto; pulo faz as folhas abanarem.
    const inhale = reduce ? 0 : Math.sin(t * 1.6);
    this.breath = (m === "sad" ? 0.045 : 0.02) + 0.014 * inhale;
    for (const s of [-1, 1]) this.halves[s].pivot.rotation.y = -s * this.breath;
    this.flapVel += (-this.flap * 120 - this.flapVel * 8 + (this.hop > 0 ? -this.hopVel * 0.12 : 0)) * dt;
    if (landed) this.flapVel += 0.9;
    this.flap = clamp(this.flap + this.flapVel * dt, -0.05, 0.08);
    const flapNow = this.flap + 0.005 * inhale;
    const keepFlap = this.flap; this.flap = flapNow;
    for (const mm of this.morphs) mm.morphTargetInfluences[0] = flapNow;

    // Folhear: a entrada é uma rajada; parado, de vez em quando ele vira uma folha com a mão.
    if (!reduce && t > this.nextFlip) {
      if (this.introFlips > 0) { this.startFlip(0.55); this.introFlips--; this.nextFlip = t + 0.32; }
      else if (m !== "thinking") { if (m === "idle" || m === "talking") this.startFlip(1.15, true); this.nextFlip = t + 7 + Math.random() * 5; }
      else this.nextFlip = t + 1;
    }
    const SR = this.shoulder(1), SL = this.shoulder(-1);
    for (const f of this.flips) {
      if (f.p < 0) continue;
      if (f.wait > 0) { f.wait -= dt; f.fg.update(this.flipShape(0)); f.mf.visible = f.mb.visible = true; continue; }
      f.p += dt / f.dur;
      if (f === this.handFlip) {
        const c = this.corner(Math.min(1, f.p)).pt;
        if (!f.released && c.distanceTo(SR) > (ARM_L1 + ARM_L2) * 0.97) f.released = true;
        if (!f.catch && f.p > 0.55 && c.distanceTo(SL) < (ARM_L1 + ARM_L2) * 0.95) f.catch = true;
      }
      if (f.p >= 1) {
        f.p = -1; f.mf.visible = f.mb.visible = false;
        if (f === this.handFlip) { this.handFlip = null; this.smoothT = f.catch ? 0.35 : 0; }
        continue;
      }
      f.fg.update(this.flipShape(f.p)); f.mf.visible = f.mb.visible = true;
    }
    this.smoothT = Math.max(0, this.smoothT - dt);

    // Olhos.
    if (t > this.blinkAt && this.blinkT < 0) this.blinkT = 0;
    let lid = 1;
    if (this.blinkT >= 0) {
      this.blinkT += dt; const p = this.blinkT / 0.16; lid = p < 1 ? Math.abs(1 - 2 * p) : 1;
      if (p >= 1) { this.blinkT = -1; if (!this.doubleBlink && Math.random() < 0.25) { this.doubleBlink = true; this.blinkAt = t + 0.1; } else { this.doubleBlink = false; this.blinkAt = t + 2 + Math.random() * 3.5; } }
    }
    if (this.winkT >= 0) { this.winkT += dt; if (this.winkT > 0.7 && m !== "wink") this.winkT = -1; }
    if (m === "idle" && t > this.nextRead && this.readT < 0) this.readT = 0;
    let readX = 0;
    if (this.readT >= 0) { this.readT += dt; readX = Math.sin(this.readT * 2.6) * 0.7; if (this.readT > 2.4) { this.readT = -1; this.nextRead = t + 8 + Math.random() * 6; } }
    const lx = clamp(look.x + readX, -1, 1), ly = look.y + (m === "thinking" ? 0.75 : m === "sad" ? -0.6 : 0) - (this.readT >= 0 ? 0.4 : 0);
    for (const c of this.cheeks) {
      c.c.position.copy(this.topPos(c.s, 0.73, 0.43, 0.002));
      c.c.quaternion.setFromUnitVectors(V3(0, 0, 1), this.topNormal(c.s, 0.73, 0.43));
      const k = m === "happy" ? 1.15 : 1; c.c.scale.set(0.085 * k, 0.052 * k, 0.012);
    }
    for (const e of this.eyes) {
      const u = this.EYE_U + e.s * lx * 0.06, v = this.EYE_V + ly * 0.04;
      e.g.position.copy(this.topPos(e.s, u, v, 0.004));
      e.g.quaternion.setFromUnitVectors(V3(0, 0, 1), this.topNormal(e.s, u, v));
      const closed = m === "happy" || (e.s > 0 && this.winkT >= 0);
      e.ball.visible = !closed; e.arc.visible = closed;
      e.ball.scale.y = 0.1 * Math.max(0.06, lid) * (m === "sad" ? 0.85 : 1);
      e.hi1.visible = e.hi2.visible = !closed && lid > 0.45;
      e.hi1.position.set(-0.022 + lx * 0.008, 0.036 + ly * 0.006, 0.03);
      e.hi2.position.set(0.026, -0.034, 0.026);
      let by = 0.165, br = 0;
      if (m === "happy") by = 0.2;
      else if (m === "thinking") { by = e.s > 0 ? 0.2 : 0.15; br = e.s > 0 ? -0.18 : 0.12; }
      else if (m === "talking") by = 0.17 + Math.abs(Math.sin(t * 4.3)) * 0.02;
      else if (m === "wave" || m === "wink") by = 0.19;
      else if (m === "sad") { by = 0.15; br = 0.3; }
      if (this.pokeT >= 0) by += 0.03 * Math.exp(-this.pokeT * 3);
      e.browY += (by - e.browY) * (1 - Math.exp(-dt * 10)); e.browR += (br - e.browR) * (1 - Math.exp(-dt * 10));
      e.brow.position.set(0, e.browY, 0.004); e.brow.rotation.z = e.browR * e.s;
    }
    this.mouth.position.copy(this.topPos(1, 0.09, 0.4, 0.012)); this.mouth.position.x = 0;
    if (m === "talking" || m === "wave") {
      const open = Math.abs(Math.sin(t * 11)) * (0.55 + 0.45 * Math.sin(t * 3.1));
      this.mouths.open.userData.o.scale.y = 0.024 + 0.048 * open;
      this.mouths.open.userData.tg.position.y = -0.01 - 0.03 * open;
    }

    // Óculos.
    this.glassesK += ((this.glassesOn && (t > 0.7 || !this.intro) ? 1 : 0) - this.glassesK) * (1 - Math.exp(-dt * 6));
    this.glassVel += (-this.glassBob * 160 - this.glassVel * 9) * dt; if (landed) this.glassVel -= 0.35; this.glassBob += this.glassVel * dt;
    const gr = this.glassesRest(), gk = this.glassesK;
    this.glasses.visible = gk > 0.02;
    this.glasses.position.set(0, gr.y + (1 - gk) * 1.5 + this.glassBob, gr.z + (1 - gk) * 0.7);
    this.glasses.rotation.set((1 - gk) * -1 + (m === "thinking" ? 0.05 : 0), (1 - gk) * 0.5, (1 - gk) * 0.5 + this.glassBob * 0.6 + Math.sin(t * 2.1) * 0.005);

    // Capelo.
    if (t < 0.25 && this.capOff > 1) { this.capOff = 1.8; this.capVel = 0; }
    this.capVel += (-this.capOff * 140 - this.capVel * 9) * dt; this.capOff += this.capVel * dt;
    if (this.capOff < 0) { this.capOff = 0; this.capVel = Math.abs(this.capVel) * 0.3; }
    this.cap.position.set(this.capBase.x, this.capBase.y + this.capOff, this.capBase.z);
    this.cap.rotation.z = -0.05 + clamp(this.capOff, 0, 0.4) * 0.5;

    // Borla.
    rig.updateMatrixWorld(true);
    const CORNER = this.CORNER, capInner = this.capInner, tas = this.tas;
    const cornerL = CORNER.clone().add(V3(0, 0.01, 0)), cornerW = capInner.localToWorld(cornerL.clone());
    if (!tas.p) { tas.p = cornerW.clone().add(V3(0.02, -this.CORD_L, 0)); tas.prev = tas.p.clone(); }
    const vel = tas.p.clone().sub(tas.prev);
    tas.prev.copy(tas.p);
    tas.p.add(vel.multiplyScalar(0.985)).add(V3(0, -9.8 * dt * dt, 0));
    for (let i = 0; i < 3; i++) {
      const d = tas.p.clone().sub(cornerW);
      tas.p.copy(cornerW).add(d.multiplyScalar(this.CORD_L / Math.max(1e-6, d.length())));
      const loc = body.worldToLocal(tas.p.clone()), uu = clamp(Math.abs(loc.x) - PH, 0, 1), zMin = this.zTop(uu, 0.95) + 0.07;
      if (loc.z < zMin && Math.abs(loc.x) < 1.05) { loc.z = zMin; tas.p.copy(body.localToWorld(loc)); }
    }
    tas.flare += (clamp(vel.length() / dt * 0.5, 0, 1) - tas.flare) * (1 - Math.exp(-dt * 6));
    const endL = capInner.worldToLocal(tas.p.clone()), dirL = endL.clone().sub(cornerL).normalize();
    const sag = cornerL.clone().lerp(endL, 0.5).add(V3(0, 0, 0.012));
    const cordCurve = new THREE.CatmullRomCurve3([V3(0, 0.244, 0), V3(CORNER.x * 0.5, 0.244, 0.004), V3(CORNER.x - 0.03, 0.246, 0), cornerL.clone().add(V3(0.008, -0.004, 0)), sag, endL]);
    this.cord.geometry.dispose(); this.cord.geometry = new THREE.TubeGeometry(cordCurve, 48, 0.0075, 8);
    this.tasselG.position.copy(endL);
    this.tasselG.quaternion.setFromUnitVectors(V3(0, -1, 0), dirL);
    const M4 = new THREE.Matrix4(), Q = new THREE.Quaternion(), E = new THREE.Euler(), Pv = V3(), S = V3();
    for (let i = 0; i < this.STRANDS; i++) {
      const ang = i / this.STRANDS * Math.PI * 2 + (i % 3) * 0.4, r0 = 0.012 + (i % 4) * 0.004, len = 0.19 + ((i * 7) % 5) * 0.008;
      const fl = 0.09 + tas.flare * 0.14 + Math.sin(t * 3 + i) * 0.01;
      Q.setFromEuler(E.set(Math.sin(ang) * fl, 0, -Math.cos(ang) * fl));
      Pv.set(Math.cos(ang) * (r0 + len * 0.5 * Math.sin(fl)), -0.058 - len / 2, Math.sin(ang) * (r0 + len * 0.5 * Math.sin(fl)));
      this.strands.setMatrixAt(i, M4.compose(Pv, Q, S.set(1, len, 1)));
    }
    this.strands.instanceMatrix.needsUpdate = true;

    // Escrita: pensando, ele escreve duas linhas e vira a folha com a mão.
    this.pencilK += ((m === "thinking" ? 1 : 0) - this.pencilK) * (1 - Math.exp(-dt * 7));
    this.pencil.visible = this.pencilK > 0.01; this.pencil.scale.setScalar(Math.max(0.001, this.pencilK));
    if (m === "thinking" && !this.handFlip) {
      const w = this.writer;
      w.u += dt * 0.24;
      if (w.u > 0.8) { w.line++; w.u = 0.2; w.last = null; if (w.line >= 2) { w.line = 0; if (!reduce) this.startFlip(1.15, true); } }
      const ph = w.u * 300, v0 = 0.318 - w.line * 0.042;
      const tu = w.u + 0.0045 * Math.cos(ph), tv = v0 + 0.009 * Math.sin(ph) + 0.003 * Math.sin(ph * 0.37);
      const down = this.pencilK > 0.9 && w.u > 0.215 && Math.sin(w.u * 41) < 0.86;
      this.penN.copy(this.topNormal(1, tu, tv));
      this.penTip.copy(rotAbout(this.topPos(1, tu, tv, down ? 0.003 : 0.03), -this.breath));
      if (down) {
        const x = (1 + tu) / 2 * 1024, y = (1 - tv) * 512;
        if (w.last) { const g = this.wCtx; g.strokeStyle = "rgba(52,50,64,.85)"; g.lineWidth = 2.3; g.lineCap = "round"; g.beginPath(); g.moveTo(w.last[0], w.last[1]); g.lineTo(x, y); g.stroke(); this.wDirty = true; }
        w.last = [x, y];
      } else w.last = null;
      if (++this.wFrame % 3 === 0) this.writeTex.needsUpdate = true;
    }
    for (const arm of this.arms) arm.apply(dt, this.shoulder(arm.s), this.pose(arm.s, t));

    // Pernas: os pés ficam no chão (o corpo sobe e desce por cima); feliz, ele dança chutando.
    {
      const gy = (this.GROUND - rig.position.y) / body.scale.y + 0.152;
      const kick = m === "happy" && !reduce ? Math.sin(t * 5) : 0;
      for (const leg of this.legs) {
        const s = leg.s, lift = Math.max(0, kick * s);
        const sway = m === "talking" ? Math.sin(t * 2.2) * 0.02 * s : 0;
        const tgt = V3(s * (0.33 + lift * 0.12) + sway, gy + lift * 0.3, 0.12 + lift * 0.28);
        leg.apply(dt, this.hip(s), tgt, -lift * 0.9);
      }
    }

    // Fita marcadora: sai de entre as folhas, embaixo, perto da lombada.
    {
      const root = V3(0.05, py(0.05, 0) + 0.012, zB(0.05) + 0.03), dir0 = V3(0.02, -1, 0.12).normalize();
      const P = { target: root.clone().add(V3(0.05, -0.44, 0.1 + this.hop * 0.05)), dir: V3(0.08, -1, 0.15).normalize(), face: V3(0, 0, 1), twist: 0.35 };
      this.bookmark.step(dt, root, dir0, P, flapNow, null);
      this.bookmark.build(t, P, 0.8);
    }
    this.flap = keepFlap;
    this.prevHopVel = this.hopVel;

    // Confete.
    if (this.confetti) {
      let any = false;
      this.conf.forEach((c, i) => {
        if (c.life <= 0) { this.confetti.setMatrixAt(i, M4.makeScale(0, 0, 0)); return; }
        any = true; c.life -= dt;
        c.v.y -= 6 * dt; c.v.multiplyScalar(1 - 1.8 * dt); if (c.v.y < -0.7) c.v.y = -0.7;
        c.p.addScaledVector(c.v, dt); c.p.x += Math.sin(t * 3 + c.ph) * 0.004;
        if (c.p.y < this.GROUND + 0.01) { c.p.y = this.GROUND + 0.01; c.v.set(0, 0, 0); }
        Q.setFromAxisAngle(c.w, t * c.spin + c.ph);
        const sc = Math.min(1, c.life * 2);
        this.confetti.setMatrixAt(i, M4.compose(c.p, Q, S.set(sc, sc, sc)));
      });
      this.confetti.visible = any; this.confetti.instanceMatrix.needsUpdate = true;
    }
  }
  render() { this.renderer.render(this.scene, this.camera); }
}

export function webglAvailable() {
  try { const c = document.createElement("canvas"); return !!(c.getContext("webgl2") || c.getContext("webgl")); } catch { return false; }
}

// ================================================================ <estudario-folha3d>
// Um laço só para todos; cada um para quando sai da tela ou a aba fica escondida.
const live = new Set();
const MAX_LIVE = 6;   // o navegador só dá uns 16 contextos WebGL; além disso, Folha 2D
let raf = 0, last = 0;
const MAX_DT = typeof location !== "undefined" && location.search.includes("shot") ? 0.2 : 1 / 30;
function loop(now) {
  const dt = Math.max(1 / 240, Math.min(MAX_DT, last ? (now - last) / 1000 : 1 / 60)); last = now;
  for (const el of live) if (el._visible) { el.folha.update(dt); el.folha.render(); }
  raf = live.size ? requestAnimationFrame(loop) : 0;
  if (!raf) last = 0;
}
let pointerHooked = false;
function hookPointer() {
  if (pointerHooked) return; pointerHooked = true;
  window.addEventListener("pointermove", (e) => {
    for (const el of live) {
      const r = el.getBoundingClientRect(), cx = r.left + r.width / 2, cy = r.top + r.height * 0.45;
      el.folha.setPointer((e.clientX - cx) / Math.max(200, window.innerWidth / 2), -(e.clientY - cy) / Math.max(200, window.innerHeight / 2));
      if (el._drag) { const dx = e.clientX - el._drag.x; el.folha.spinVel = dx * 0.3; el.folha.spin += dx * 0.01; el._drag.x = e.clientX; el._drag.moved += Math.abs(dx); }
    }
  }, { passive: true });
}

if (typeof customElements !== "undefined" && !customElements.get("estudario-folha3d")) {
  customElements.define("estudario-folha3d", class extends HTMLElement {
    static get observedAttributes() { return ["mood", "glasses", "size"]; }
    connectedCallback() {
      if (this.folha || this._fallback) return;
      const size = +(this.getAttribute("size") || 0);
      if (size) { this.style.width = this.style.width || `${size}px`; this.style.height = this.style.height || `${size}px`; }
      if (!this.style.display) this.style.display = "inline-block";
      if (!webglAvailable() || live.size >= MAX_LIVE) {
        // Sem WebGL: o Folha 2D de sempre.
        this._fallback = document.createElement("estudario-folha");
        this._fallback.setAttribute("mood", this.getAttribute("mood") || "idle");
        this._fallback.setAttribute("size", String(size || 200));
        this.appendChild(this._fallback);
        return;
      }
      // Com tamanho (no app): o canvas é 1,5x a caixa, centrado e sem receber toque, para as fitas e o
      // confete poderem sair da caixa sem mexer no layout.
      let host = this;
      if (size) {
        if (getComputedStyle(this).position === "static") this.style.position = "relative";
        host = document.createElement("div");
        host.style.cssText = "position:absolute;left:-25%;top:-25%;width:150%;height:150%;pointer-events:none";
        this.appendChild(host);
      }
      this._host = host;
      this.folha = new Folha3D(host, {
        bleed: !!size,
        mood: this.getAttribute("mood") || "idle",
        glasses: this.getAttribute("glasses") !== "off",
        spin: this.hasAttribute("spin"),
        intro: this.getAttribute("intro") !== "off",
      });
      this._visible = true;
      this._ro = new ResizeObserver(() => this.folha && this.folha.resize(this._host.clientWidth || 1, this._host.clientHeight || 1));
      this._ro.observe(this);
      this._io = new IntersectionObserver((es) => { for (const e of es) this._visible = e.isIntersecting && !document.hidden; });
      this._io.observe(this);
      this.addEventListener("pointerdown", (e) => { this._drag = this.hasAttribute("spin") ? { x: e.clientX, moved: 0 } : null; this._down = { x: e.clientX, y: e.clientY }; });
      this.addEventListener("pointerup", (e) => {
        const moved = this._drag ? this._drag.moved : Math.hypot(e.clientX - this._down.x, e.clientY - this._down.y);
        this._drag = null;
        if (moved < 6 && this.folha.hits(e.clientX, e.clientY)) { this.folha.poke(); this.dispatchEvent(new CustomEvent("folha-poke", { bubbles: true })); }
      });
      window.addEventListener("pointerup", this._up = () => { this._drag = null; });
      hookPointer();
      live.add(this);
      if (!raf) raf = requestAnimationFrame(loop);
    }
    disconnectedCallback() {
      live.delete(this);
      this._ro?.disconnect(); this._io?.disconnect();
      if (this._up) window.removeEventListener("pointerup", this._up);
      if (this.folha) { this.folha.dispose(); this.folha = null; }
      if (this._fallback) { this._fallback.remove(); this._fallback = null; }
      if (this._host && this._host !== this) { this._host.remove(); this._host = null; }
    }
    attributeChangedCallback(name, _, value) {
      if (this._fallback && name === "mood") this._fallback.setAttribute("mood", value || "idle");
      if (!this.folha) return;
      if (name === "mood") this.folha.setMood(value || "idle");
      if (name === "glasses") this.folha.setGlasses(value !== "off");
    }
  });
}

if (typeof window !== "undefined") window.EstudarioFolha3D = { Folha3D, webglAvailable };
