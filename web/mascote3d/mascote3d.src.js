// O Folha em 3D: o modelo da arte oficial (design/mascote/folha-meshy.glb, gerado pelo Meshy a partir
// da folha de vistas; scripts/web/mascote/extrair-glb.src.js tira só a figura de frente e reduz as
// texturas -> web/src/jsMain/resources/folha.glb). Aqui ele ganha vida:
//  - esqueleto (corpo, braços "de mangueira" que esticam da capa, punhos, pés, capelo e borla), com os
//    pesos tirados da posição e da cor da textura;
//  - rosto vivo: os olhos e a boca pintados saem da textura (preenchidos com o papel da própria página)
//    e voltam como olhos 3D que piscam e olham, arcos de feliz e uma boca que fala de verdade;
//  - óculos de armação fina com aro dourado e lentes que refletem;
//  - folha que vira: a página da direita (copiada da própria textura) descola, a mão empurra, a outra
//    alisa do outro lado, e o que ele escreveu vai junto;
//  - caderno: lápis escrevendo, visto e marca-texto nas linhas da página;
//  - corrida (login), tchau, comemoração com confete, erro (balança a cabeça), fala.
// Gera web/src/jsMain/resources/mascote3d.js com `node scripts/web/build-mascote3d.mjs`.
//
// Uso: <estudario-mascote3d mood="idle|happy|wave|talking|thinking|sad|point|wink|run|run-left"
//        size="160" [spin]>
// Sem WebGL (ou com muitos na tela) cai no recorte animado (<estudario-mascote>).
import * as THREE from "three";
import { GLTFLoader } from "three/addons/loaders/GLTFLoader.js";
import { RoomEnvironment } from "three/addons/environments/RoomEnvironment.js";

const V3 = (x = 0, y = 0, z = 0) => new THREE.Vector3(x, y, z);
const clamp = (v, a, b) => Math.max(a, Math.min(b, v));
const lerp = (a, b, t) => a + (b - a) * t;
const smooth = (a, b, x) => { const t = clamp((x - a) / (b - a), 0, 1); return t * t * (3 - 2 * t); };
const ease = (x) => x * x * x * (x * (x * 6 - 15) + 10);

const base = (() => { try { const s = document.currentScript && document.currentScript.src; return s ? s.replace(/[^/]*$/, "") : ""; } catch { return ""; } })();

// ------------------------------------------------------------------ medidas do modelo (1 de altura, pés em y = 0)
const J = {
  body: [0.02, 0.30, 0.03],
  cap: [0.04, 0.835, 0.0], tassel: [0.235, 0.858, 0.19],
  armL: [-0.345, 0.575, 0.0], foreL: [-0.426, 0.652, 0.03],
  armR: [0.375, 0.545, 0.0], foreR: [0.432, 0.57, 0.03],
  ribbon: [0.01, 0.46, -0.06],
  footL: [-0.15, 0.14, -0.01], footR: [0.2, 0.14, -0.01],
};
const FIST0 = { L: V3(-0.426, 0.652, 0.03), R: V3(0.432, 0.57, 0.03) };    // pulso (pulseira) em repouso
const PARENT = { body: "root", cap: "body", tassel: "cap", armL: "body", foreL: "body", armR: "body", foreR: "body", ribbon: "body", footL: "root", footR: "root" };
const ORDER = ["root", "body", "cap", "tassel", "armL", "foreL", "armR", "foreR", "ribbon", "footL", "footR"];
const FACE = {
  eyeL: [-0.128, 0.604], eyeR: [0.16, 0.604],
  // Caixas (x0, x1, y0, y1) do que sai da textura pintada.
  erase: [[-0.205, -0.048, 0.565, 0.668], [0.078, 0.243, 0.565, 0.668], [-0.052, 0.088, 0.474, 0.572]],
  mouth: [-0.07, 0.1, 0.455, 0.59],
};
const PAGE = { x0: 0.004, x1: 0.338, y0: 0.33, y1: 0.815 };          // folha da direita (a que vira)

// ------------------------------------------------------------------ altura da página (frente do livro)
function makeHeight(P, N) {
  const x0 = -0.37, y0 = 0.3, res = 0.01, nx = 76, ny = 56, d = new Float32Array(nx * ny).fill(-9);
  for (let i = 0; i < P.count; i++) {
    if (N.getZ(i) < 0.3) continue;
    const ix = Math.round((P.getX(i) - x0) / res), iy = Math.round((P.getY(i) - y0) / res);
    if (ix < 0 || iy < 0 || ix >= nx || iy >= ny) continue;
    const k = iy * nx + ix; d[k] = Math.max(d[k], P.getZ(i));
  }
  for (let pass = 0; pass < 6; pass++) for (let iy = 0; iy < ny; iy++) for (let ix = 0; ix < nx; ix++) {
    const k = iy * nx + ix; if (d[k] > -9) continue;
    let s = 0, c = 0; for (const [dx, dy] of [[1, 0], [-1, 0], [0, 1], [0, -1]]) { const xx = ix + dx, yy = iy + dy; if (xx >= 0 && yy >= 0 && xx < nx && yy < ny && d[yy * nx + xx] > -9) { s += d[yy * nx + xx]; c++; } }
    if (c) d[k] = s / c;
  }
  // Mediana 3x3: tira picos (borla, fita) e buracos.
  const m = new Float32Array(d);
  for (let iy = 1; iy < ny - 1; iy++) for (let ix = 1; ix < nx - 1; ix++) { const a = []; for (let dy = -1; dy <= 1; dy++) for (let dx = -1; dx <= 1; dx++) a.push(d[(iy + dy) * nx + ix + dx]); a.sort((p, q) => p - q); m[iy * nx + ix] = a[4]; }
  return (x, y) => {
    const fx = clamp((x - x0) / res, 0, nx - 1.001), fy = clamp((y - y0) / res, 0, ny - 1.001), ix = Math.floor(fx), iy = Math.floor(fy), tx = fx - ix, ty = fy - iy;
    const g = (a, b) => m[b * nx + a];
    return lerp(lerp(g(ix, iy), g(ix + 1, iy), tx), lerp(g(ix, iy + 1), g(ix + 1, iy + 1), tx), ty);
  };
}

// ------------------------------------------------------------------ pesos do esqueleto
function weightsFor(P, UV, color, index, pageZ) {
  const n = P.count, idx = new Uint16Array(n * 4), wts = new Float32Array(n * 4);
  const lumOf = (c) => 0.3 * c[0] + 0.59 * c[1] + 0.11 * c[2];
  const isArmColor = (i) => { const c = color(UV.getX(i), UV.getY(i)), lum = lumOf(c); return lum > 150 || (c[0] > 150 && c[2] < 120) || (c[2] > 150 && c[1] > 62 && c[1] > c[0] * 1.6); };
  const isFistColor = (i) => { const c = color(UV.getX(i), UV.getY(i)); return lumOf(c) > 150 || (c[0] > 150 && c[2] < 120); };
  // Braços: parte do que com certeza é braço e cresce pelos vértices ligados com cor de braço.
  const key = (i) => Math.round(P.getX(i) * 1e4) + "," + Math.round(P.getY(i) * 1e4) + "," + Math.round(P.getZ(i) * 1e4);
  const same = new Map(), grp = new Int32Array(n);
  for (let i = 0; i < n; i++) { const k = key(i); if (!same.has(k)) same.set(k, same.size); grp[i] = same.get(k); }
  const G = same.size, members = Array.from({ length: G }, () => []), adj = Array.from({ length: G }, () => new Set());
  for (let i = 0; i < n; i++) members[grp[i]].push(i);
  for (let t = 0; t < index.length; t += 3) { const a = grp[index[t]], b = grp[index[t + 1]], c = grp[index[t + 2]]; adj[a].add(b); adj[a].add(c); adj[b].add(a); adj[b].add(c); adj[c].add(a); adj[c].add(b); }
  const arm = new Int8Array(n), fistV = new Uint8Array(n);
  for (const [sd, seed, lim] of [[-1, (x) => x < -0.392, (x) => x < -0.338], [1, (x) => x > 0.43, (x) => x > 0.37]]) {
    const seen = new Uint8Array(G), q = [];
    for (let gI = 0; gI < G; gI++) { const v = members[gI][0], y = P.getY(v); if (seed(P.getX(v)) && y > 0.43 && y < 0.8) { seen[gI] = 1; q.push(gI); } }
    while (q.length) {
      const gI = q.pop(); for (const v of members[gI]) arm[v] = sd;
      for (const nb of adj[gI]) { if (seen[nb]) continue; const v = members[nb][0], y = P.getY(v); if (lim(P.getX(v)) && y > 0.43 && y < 0.8 && members[nb].some(isArmColor)) { seen[nb] = 1; q.push(nb); } }
    }
    for (let it = 0; it < 4; it++) {
      const add = [];
      for (let gI = 0; gI < G; gI++) {
        if (seen[gI]) continue; const v = members[gI][0], y = P.getY(v); if (!lim(P.getX(v)) || y < 0.43 || y > 0.8) continue;
        let k = 0, tot = 0; for (const nb of adj[gI]) { tot++; if (seen[nb]) k++; }
        if (tot && k / tot >= 0.5) add.push(gI);
      }
      for (const gI of add) { seen[gI] = 1; for (const v of members[gI]) arm[v] = sd; }
    }
    // Tudo o que está claramente fora da capa, na altura dos braços, é braço (sem fiapos presos ao livro).
    const outside = sd < 0 ? (x) => x < -0.366 : (x) => x > 0.402;
    for (let gI = 0; gI < G; gI++) { const v = members[gI][0], y = P.getY(v); if (!seen[gI] && outside(P.getX(v)) && y > 0.45 && y < 0.78) { seen[gI] = 1; for (const w of members[gI]) arm[w] = sd; } }
    // Luva e pulseira (branco/dourado) vão com o punho; o resto do braço é manga.
    for (let gI = 0; gI < G; gI++) if (seen[gI] && members[gI].some(isFistColor)) for (const v of members[gI]) fistV[v] = 1;
    // Vizinhos da luva que ficaram de fora também vão com o punho.
    for (let it = 0; it < 2; it++) { const add = []; for (let gI = 0; gI < G; gI++) { if (!seen[gI] || members[gI].some((v) => fistV[v])) continue; let k = 0; for (const nb of adj[gI]) if (fistV[members[nb][0]]) k++; if (k >= 2 && outside(P.getX(members[gI][0]))) add.push(gI); } for (const gI of add) for (const v of members[gI]) fistV[v] = 1; }
  }
  const B = Object.fromEntries(ORDER.map((k, i) => [k, i]));
  for (let i = 0; i < n; i++) {
    const x = P.getX(i), y = P.getY(i), z = P.getZ(i), w = new Map();
    const add = (k, v) => { if (v > 1e-4) w.set(B[k], (w.get(B[k]) || 0) + v); };
    const side = x < 0.03 ? "L" : "R";
    // Borla: pendurada na frente da página direita.
    if (x > 0.19 && x < 0.285 && y > 0.69 && y < 0.875 && z > pageZ(x, y) + 0.008) { const t = smooth(0.86, 0.875, y); add("tassel", 1 - t); add("cap", t); }
    // Fita marcadora original: some para dentro do livro (a nova fica do lado).
    else if (Math.abs(x - 0.01) < 0.05 && y > 0.17 && y < 0.335 && z > 0.0) add("ribbon", 1);
    // Pernas e pés: o pé manda embaixo; a perna estica entre o pé e o corpo.
    else if (y < 0.31) { const t = smooth(0.15, 0.3, y); add("foot" + side, 1 - t); add("body", t); }
    // Braços de mangueira: a raiz da manga fica na capa e o resto estica até o punho.
    else if (arm[i]) {
      const s = arm[i] < 0 ? "L" : "R";
      // Luva e pulseira vão com o pulso; a manga original é escondida (vira o braço de tubo).
      if (fistV[i]) add("fore" + s, 1); else add("arm" + s, 1);
    }
    else if (y > 0.82) { const t = smooth(0.825, 0.85, y); add("cap", t); add("body", 1 - t); }
    else add("body", 1);
    const top = [...w.entries()].sort((a, b) => b[1] - a[1]).slice(0, 4), sum = top.reduce((s, e) => s + e[1], 0) || 1;
    top.forEach(([b, v], k) => { idx[i * 4 + k] = b; wts[i * 4 + k] = v / sum; });
  }
  let sc = [0, 0, 0, 0];
  for (let i = 0; i < n; i++) if (arm[i] && !fistV[i]) { const c = color(UV.getX(i), UV.getY(i)); if (c[2] > 120) { sc[0] += c[0]; sc[1] += c[1]; sc[2] += c[2]; sc[3]++; } }
  const sleeve = sc[3] ? sc.slice(0, 3).map((v) => Math.round(v / sc[3])) : [40, 70, 200];
  // Tira da malha a manga original e corta limpo entre a luva e o livro (o braço de tubo substitui).
  const cls = (i) => (arm[i] ? (fistV[i] ? 2 : 1) : 0), keep = [];
  for (let t = 0; t < index.length; t += 3) {
    const a = cls(index[t]), b = cls(index[t + 1]), c = cls(index[t + 2]);
    if (a === 1 || b === 1 || c === 1) continue;
    if ((a === 2 || b === 2 || c === 2) && !(a === 2 && b === 2 && c === 2)) continue;
    keep.push(index[t], index[t + 1], index[t + 2]);
  }
  return { idx, wts, sleeve, keep };
}

// ------------------------------------------------------------------ textura: tira olhos/boca e copia as páginas
function prepareTexture(geo, img, pageZ) {
  const S = img.width, cv = document.createElement("canvas"); cv.width = cv.height = S;
  const g = cv.getContext("2d", { willReadFrequently: true }); g.drawImage(img, 0, 0);
  const data = g.getImageData(0, 0, S, S).data;
  const P = geo.attributes.position, UV = geo.attributes.uv, I = geo.index.array;
  const texel = (u, v) => { const x = clamp(Math.floor(u * S), 0, S - 1), y = clamp(Math.floor(v * S), 0, S - 1), k = (y * S + x) * 4; return [data[k], data[k + 1], data[k + 2]]; };
  // Cor do papel por coluna (x), tirada de uma faixa limpa da página, abaixo da boca.
  const bins = new Map();
  for (let i = 0; i < P.count; i++) {
    const x = P.getX(i), y = P.getY(i); if (y < 0.425 || y > 0.46 || Math.abs(x) > 0.34 || P.getZ(i) < pageZ(x, y) - 0.01) continue;
    const c = texel(UV.getX(i), UV.getY(i)); if (c[0] + c[1] + c[2] < 560) continue;
    const b = Math.round(x * 100); const a = bins.get(b) || [0, 0, 0, 0]; a[0] += c[0]; a[1] += c[1]; a[2] += c[2]; a[3]++; bins.set(b, a);
  }
  const paperAt = (x) => { for (let d = 0; d < 20; d++) for (const b of [Math.round(x * 100) + d, Math.round(x * 100) - d]) { const a = bins.get(b); if (a && a[3]) return a.slice(0, 3).map((v) => Math.round(v / a[3])); } return [246, 243, 236]; };
  const inBox = (x, y, [a, b, c, d]) => x > a && x < b && y > c && y < d;
  const tri = (t) => [I[t], I[t + 1], I[t + 2]];
  for (let t = 0; t < I.length; t += 3) {
    const v = tri(t); let cx = 0, cy = 0, cz = 0; for (const i of v) { cx += P.getX(i) / 3; cy += P.getY(i) / 3; cz += P.getZ(i) / 3; }
    if (!FACE.erase.some((bx) => inBox(cx, cy, bx)) || cz < pageZ(cx, cy) - 0.014) continue;
    const c = paperAt(cx); g.fillStyle = g.strokeStyle = `rgb(${c[0]},${c[1]},${c[2]})`; g.lineWidth = 3; g.lineJoin = "round";
    g.beginPath(); v.forEach((i, k) => { const X = UV.getX(i) * S, Y = UV.getY(i) * S; k ? g.lineTo(X, Y) : g.moveTo(X, Y); }); g.closePath(); g.fill(); g.stroke();
  }
  // Cópia plana das duas páginas (frente da folha da direita; e a esquerda, espelhada, para o verso).
  const bake = (sign) => {
    const W = 512, H = Math.round(W * (PAGE.y1 - PAGE.y0) / (PAGE.x1 - PAGE.x0)), c = document.createElement("canvas"); c.width = W; c.height = H;
    const k = c.getContext("2d"); const pc = paperAt(sign * 0.2); k.fillStyle = `rgb(${pc[0]},${pc[1]},${pc[2]})`; k.fillRect(0, 0, W, H);
    const toC = (x, y) => [(sign * x - PAGE.x0) / (PAGE.x1 - PAGE.x0) * W, (PAGE.y1 - y) / (PAGE.y1 - PAGE.y0) * H];
    for (let t = 0; t < I.length; t += 3) {
      const v = tri(t); let cx = 0, cy = 0, cz = 0; for (const i of v) { cx += P.getX(i) / 3; cy += P.getY(i) / 3; cz += P.getZ(i) / 3; }
      const sx = sign * cx; if (sx < PAGE.x0 - 0.01 || sx > PAGE.x1 + 0.01 || cy < PAGE.y0 - 0.01 || cy > PAGE.y1 + 0.01 || cz < pageZ(cx, cy) - 0.012) continue;
      const s = v.map((i) => [UV.getX(i) * S, UV.getY(i) * S]), d = v.map((i) => toC(P.getX(i), P.getY(i)));
      // Transformação afim do triângulo da textura para o triângulo da página.
      const [[x0, y0], [x1, y1], [x2, y2]] = s, [[u0, v0], [u1, v1], [u2, v2]] = d;
      const den = (x1 - x0) * (y2 - y0) - (x2 - x0) * (y1 - y0); if (Math.abs(den) < 1e-6) continue;
      const a = ((u1 - u0) * (y2 - y0) - (u2 - u0) * (y1 - y0)) / den, b = ((u2 - u0) * (x1 - x0) - (u1 - u0) * (x2 - x0)) / den;
      const cc = ((v1 - v0) * (y2 - y0) - (v2 - v0) * (y1 - y0)) / den, dd = ((v2 - v0) * (x1 - x0) - (v1 - v0) * (x2 - x0)) / den;
      const e = u0 - a * x0 - b * y0, f = v0 - cc * x0 - dd * y0;
      const mx = (u0 + u1 + u2) / 3, my = (v0 + v1 + v2) / 3, grow = (p) => [p[0] + (p[0] - mx) * 0.08 + Math.sign(p[0] - mx) * 0.6, p[1] + (p[1] - my) * 0.08 + Math.sign(p[1] - my) * 0.6];
      k.save(); k.beginPath(); d.map(grow).forEach((p, i) => (i ? k.lineTo(...p) : k.moveTo(...p))); k.closePath(); k.clip();
      k.setTransform(a, cc, b, dd, e, f);
      const bx = Math.max(0, Math.floor(Math.min(x0, x1, x2)) - 2), by = Math.max(0, Math.floor(Math.min(y0, y1, y2)) - 2), bw = Math.min(S - bx, Math.ceil(Math.max(x0, x1, x2)) - bx + 4), bh = Math.min(S - by, Math.ceil(Math.max(y0, y1, y2)) - by + 4);
      if (bw > 0 && bh > 0) k.drawImage(cv, bx, by, bw, bh, bx, by, bw, bh);
      k.restore();
    }
    return c;
  };
  return { canvas: cv, pageR: bake(1), pageL: bake(-1), paper: paperAt(0.2) };
}

let MODEL = null;
function loadModel() {
  if (MODEL) return MODEL;
  // Baixa o GLB e faz o parse com o TextureLoader comum (sem createImageBitmap/fetch de blob:, que o
  // CSP do app barra): sem createImageBitmap na hora em que o parser nasce, o three usa <img>.
  MODEL = fetch((typeof window !== "undefined" && window.ESTUDARIO_FOLHA_GLB) || base + "folha.glb").then((r) => { if (!r.ok) throw new Error("folha.glb " + r.status); return r.arrayBuffer(); }).then((buf) => new Promise((res, rej) => {
    const cib = window.createImageBitmap;
    try { window.createImageBitmap = undefined; } catch (_) { /* ok */ }
    new GLTFLoader().parse(buf, base, (g) => {
      let mesh; g.scene.traverse((o) => { if (o.isMesh) mesh = o; });
      const geo = mesh.geometry, P = geo.attributes.position;
      const pageZ = makeHeight(P, geo.attributes.normal);
      const img = mesh.material.map.image, cs = 512, cv = document.createElement("canvas"); cv.width = cv.height = cs;
      const cx = cv.getContext("2d", { willReadFrequently: true }); cx.drawImage(img, 0, 0, cs, cs);
      const px = cx.getImageData(0, 0, cs, cs).data;
      const color = (u, v) => { const x = Math.min(cs - 1, Math.max(0, Math.floor(u * cs))), y = Math.min(cs - 1, Math.max(0, Math.floor(v * cs))), k = (y * cs + x) * 4; return [px[k], px[k + 1], px[k + 2]]; };
      const { idx, wts, sleeve, keep } = weightsFor(P, geo.attributes.uv, color, geo.index.array, pageZ);
      geo.setIndex(keep);
      geo.setAttribute("skinIndex", new THREE.Uint16BufferAttribute(idx, 4));
      geo.setAttribute("skinWeight", new THREE.Float32BufferAttribute(wts, 4));
      const prep = prepareTexture(geo, img, pageZ);
      const map = new THREE.CanvasTexture(prep.canvas); map.flipY = false; map.colorSpace = THREE.SRGBColorSpace; map.anisotropy = 8;
      const mat = mesh.material; mat.map = map; mat.roughness = 0.62; mat.envMapIntensity = 0.9; mat.needsUpdate = true;
      res({ geo, mat, pageZ, prep, sleeve });
    }, rej);
    try { window.createImageBitmap = cib; } catch (_) { /* ok */ }
  }));
  return MODEL;
}

// ------------------------------------------------------------------ peças
const phys = (o) => new THREE.MeshPhysicalMaterial(o);
const MAT = {};
function mats() {
  if (MAT.ink) return MAT;
  Object.assign(MAT, {
    ink: phys({ color: "#1C1652", roughness: 0.12, clearcoat: 1, clearcoatRoughness: 0.05 }),
    shine: new THREE.MeshBasicMaterial({ color: "#ffffff" }),
    frame: phys({ color: "#241D5E", metalness: 0.65, roughness: 0.22, clearcoat: 1, clearcoatRoughness: 0.06 }),
    goldThin: phys({ color: "#E8B34E", metalness: 1, roughness: 0.18 }),
    lens: phys({ color: "#EEF0FF", roughness: 0.03, metalness: 0, clearcoat: 1, clearcoatRoughness: 0.02, transparent: true, opacity: 0.16, envMapIntensity: 2.2, depthWrite: false, side: THREE.DoubleSide }),
    pencil: phys({ color: "#F6C232", roughness: 0.38, clearcoat: 0.6 }),
    wood: phys({ color: "#E9C495", roughness: 0.8 }),
    lead: phys({ color: "#2F2C35", roughness: 0.35, metalness: 0.4 }),
    ferrule: phys({ color: "#C8CAD4", metalness: 1, roughness: 0.28 }),
    eraser: phys({ color: "#F29AB6", roughness: 0.7 }),
    marker: phys({ color: "#FFE14A", roughness: 0.35, clearcoat: 0.8 }),
    markerCap: phys({ color: "#2B2470", roughness: 0.3, clearcoat: 0.8 }),
    ribbon: phys({ color: "#3ED1C2", roughness: 0.32, sheen: 1, sheenColor: new THREE.Color("#CBFFF7"), sheenRoughness: 0.3, side: THREE.DoubleSide }),
    confetti: new THREE.MeshStandardMaterial({ side: THREE.DoubleSide, roughness: 0.6 }),
  });
  return MAT;
}
const shadowed = (o) => { o.traverse((m) => { if (m.isMesh) { m.castShadow = true; m.receiveShadow = false; } }); return o; };

// ------------------------------------------------------------------ o personagem
export class Mascote3D {
  constructor(container, model, { mood = "idle", bleed = false } = {}) {
    const M = mats();
    this.model = model; this.mood = mood; this.bleed = bleed;
    const r = this.renderer = new THREE.WebGLRenderer({ antialias: true, alpha: true, powerPreference: "low-power" });
    r.setPixelRatio(Math.min(window.devicePixelRatio || 1, 2));
    r.toneMapping = THREE.NeutralToneMapping; r.toneMappingExposure = 1.0;
    r.shadowMap.enabled = true; r.shadowMap.type = THREE.VSMShadowMap;
    r.domElement.style.cssText = "display:block;width:100%;height:100%";
    container.appendChild(r.domElement);
    const scene = this.scene = new THREE.Scene();
    const pm = new THREE.PMREMGenerator(r); scene.environment = pm.fromScene(new RoomEnvironment(), 0.04).texture; pm.dispose();
    scene.environmentIntensity = 0.85;
    this.camera = new THREE.PerspectiveCamera(22, 1, 0.05, 20);
    const key = new THREE.DirectionalLight("#fff6ea", 1.6); key.position.set(-0.6, 3.2, 2.2); key.castShadow = true;
    key.shadow.mapSize.set(1024, 1024); Object.assign(key.shadow.camera, { left: -1, right: 1, top: 1, bottom: -1, near: 0.5, far: 8 });
    key.shadow.radius = 6; key.shadow.blurSamples = 12; key.shadow.bias = -0.0005;
    scene.add(key);
    const rim = new THREE.DirectionalLight("#c4b8ff", 0.9); rim.position.set(1.5, 1.2, -2); scene.add(rim);
    const ground = new THREE.Mesh(new THREE.PlaneGeometry(6, 6), new THREE.ShadowMaterial({ opacity: 0.18, color: "#221c58" }));
    ground.rotation.x = -Math.PI / 2; ground.receiveShadow = true; scene.add(ground);

    // ---- esqueleto
    const bones = this.bones = {};
    for (const k of ORDER) { const b = new THREE.Bone(); b.name = k; bones[k] = b; }
    for (const k of ORDER) {
      if (k === "root") continue;
      const p = J[k], pp = PARENT[k] === "root" ? [0, 0, 0] : J[PARENT[k]];
      bones[k].position.set(p[0] - pp[0], p[1] - pp[1], p[2] - pp[2]);
      bones[PARENT[k]].add(bones[k]);
    }
    this.rest = Object.fromEntries(ORDER.map((k) => [k, bones[k].position.clone()]));
    const mesh = this.mesh = new THREE.SkinnedMesh(model.geo, model.mat);
    mesh.castShadow = true; mesh.receiveShadow = true; mesh.frustumCulled = false;
    mesh.add(bones.root); mesh.bind(new THREE.Skeleton(ORDER.map((k) => bones[k])));
    this.rig = new THREE.Group(); this.rig.add(mesh); scene.add(this.rig);
    bones.ribbon.scale.setScalar(0.001);   // a fita original some para dentro do livro

    const pageZ = this.pageZ = model.pageZ;
    const B0 = V3(...J.body), toBody = (x, y, z) => V3(x, y, z).sub(B0);
    this.toBody = toBody;
    const normalAt = (x, y) => { const e = 0.01; return V3(-(pageZ(x + e, y) - pageZ(x - e, y)) / (2 * e), -(pageZ(x, y + e) - pageZ(x, y - e)) / (2 * e), 1).normalize(); };
    this.normalAt = normalAt;
    // Grade que acompanha a página (para os decalques e a folha).
    const conform = (x0, x1, y0, y1, nu, nv, lift) => {
      const g = new THREE.PlaneGeometry(1, 1, nu, nv), Pp = g.attributes.position, uv = g.attributes.uv;
      for (let i = 0; i < Pp.count; i++) { const u = uv.getX(i), v = uv.getY(i), x = lerp(x0, x1, u), y = lerp(y0, y1, v); const p = toBody(x, y, pageZ(x, y) + lift); Pp.setXYZ(i, p.x, p.y, p.z); }
      g.computeVertexNormals(); return g;
    };
    const decal = (canvas, box, lift, order) => {
      const t = new THREE.CanvasTexture(canvas); t.colorSpace = THREE.SRGBColorSpace; t.anisotropy = 4;
      const m = new THREE.Mesh(conform(box[0], box[1], box[2], box[3], 24, 24, lift), new THREE.MeshStandardMaterial({ map: t, transparent: true, depthWrite: false, roughness: 0.7, polygonOffset: true, polygonOffsetFactor: -2 }));
      m.renderOrder = order; bones.body.add(m); return { mesh: m, tex: t, canvas, g: canvas.getContext("2d"), box };
    };

    // ---- caderno: o que ele escreve e marca na página da direita
    { const c = document.createElement("canvas"); c.width = 512; c.height = Math.round(512 * (PAGE.y1 - PAGE.y0) / (PAGE.x1 - PAGE.x0)); this.ink = decal(c, [PAGE.x0, PAGE.x1, PAGE.y0, PAGE.y1], 0.0012, 2); }
    // ---- boca
    { const c = document.createElement("canvas"); c.width = 256; c.height = Math.round(256 * (FACE.mouth[3] - FACE.mouth[2]) / (FACE.mouth[1] - FACE.mouth[0])); this.mouthD = decal(c, FACE.mouth, 0.002, 4); this.mouthKey = ""; }

    // ---- olhos
    const face = this.face = new THREE.Group(); bones.body.add(face);
    this.eyes = {};
    for (const k of ["L", "R"]) {
      const [ex, ey] = FACE["eye" + k], g = new THREE.Group();
      g.position.copy(toBody(ex, ey, pageZ(ex, ey) + 0.002)); g.quaternion.setFromUnitVectors(V3(0, 0, 1), normalAt(ex, ey));
      const dome = new THREE.SphereGeometry(1, 40, 20, 0, Math.PI * 2, 0, Math.PI / 2); dome.rotateX(Math.PI / 2);
      const open = new THREE.Group(), ball = new THREE.Mesh(dome, M.ink); ball.scale.set(0.026, 0.035, 0.01);
      const h1 = new THREE.Mesh(new THREE.SphereGeometry(1, 16, 10), M.shine); h1.scale.set(0.0088, 0.011, 0.003); h1.position.set(-0.008, 0.013, 0.01);
      const h2 = new THREE.Mesh(new THREE.SphereGeometry(1, 12, 8), M.shine); h2.scale.set(0.0035, 0.0035, 0.002); h2.position.set(0.008, -0.01, 0.0085);
      open.add(ball, h1, h2);
      const arcPts = []; for (let i = 0; i <= 20; i++) { const t = i / 20, x = lerp(-0.036, 0.036, t); arcPts.push(V3(x, -0.012 + 0.034 * Math.sin(Math.PI * t) ** 0.9, 0.003)); }
      const arc = new THREE.Group(); arc.add(new THREE.Mesh(new THREE.TubeGeometry(new THREE.CatmullRomCurve3(arcPts), 32, 0.0068, 10), M.ink));
      for (const p of [arcPts[0], arcPts[20]]) { const c = new THREE.Mesh(new THREE.SphereGeometry(0.0068, 10, 8), M.ink); c.position.copy(p); arc.add(c); }
      const brow = new THREE.Mesh(new THREE.TubeGeometry(new THREE.QuadraticBezierCurve3(V3(-0.024, 0, 0.003), V3(0, 0.006, 0.004), V3(0.024, 0, 0.003)), 12, 0.004, 8), M.ink); brow.position.y = 0.05;
      g.add(open, arc, brow); face.add(g);
      for (const o of [ball, h1, h2, ...arc.children, brow]) o.renderOrder = 5;
      this.eyes[k] = { g, open, ball, h1, h2, arc, brow };
    }

    // ---- óculos: aro fino de metal com filete dourado, lentes que refletem, ponte e hastes até a capa
    {
      const gl = this.glasses = new THREE.Group(), R = 0.066, zOff = 0.05;
      const cL = FACE.eyeL, cR = FACE.eyeR, y = 0.606, mid = (cL[0] + cR[0]) / 2;
      gl.position.copy(toBody(mid, y, Math.max(pageZ(cL[0], y), pageZ(cR[0], y)) + zOff));
      for (const [ex] of [cL, cR]) {
        const lx = ex - mid;
        const rim = new THREE.Mesh(new THREE.TorusGeometry(R, 0.0042, 12, 72), M.frame); rim.position.x = lx;
        const inner = new THREE.Mesh(new THREE.TorusGeometry(R - 0.0042, 0.0013, 6, 72), M.goldThin); inner.position.set(lx, 0, 0.003);
        const lens = new THREE.Mesh(new THREE.CircleGeometry(R - 0.001, 48), M.lens); lens.position.x = lx; lens.renderOrder = 7;
        gl.add(rim, inner, lens);
      }
      const bx = (cR[0] - cL[0]) / 2 - R;
      gl.add(new THREE.Mesh(new THREE.TubeGeometry(new THREE.CubicBezierCurve3(V3(-bx - 0.002, 0.012, 0), V3(-bx * 0.4, 0.034, 0.012), V3(bx * 0.4, 0.034, 0.012), V3(bx + 0.002, 0.012, 0)), 24, 0.0036, 8), M.frame));
      for (const s of [-1, 1]) {
        const ex = (s < 0 ? cL[0] : cR[0]) - mid, hx = ex + s * (R + 0.004);
        const hinge = new THREE.Mesh(new THREE.BoxGeometry(0.012, 0.009, 0.01), M.goldThin); hinge.position.set(hx, 0.012, -0.002); gl.add(hinge);
        // Haste: corre por cima da página até a borda e dobra para trás da capa.
        const wx = (x) => x - mid, wz = (x, yy) => pageZ(x, yy) + 0.012 - (gl.position.z + B0.z);
        const X1 = mid + hx, Xe = s * 0.338;
        const pts = [V3(hx, 0.012, -0.003), V3(wx(lerp(X1, Xe, 0.5)), 0.014, wz(lerp(X1, Xe, 0.5), y)), V3(wx(Xe), 0.012, wz(Xe, y)), V3(wx(s * 0.352), 0.01, wz(Xe, y) - 0.04), V3(wx(s * 0.35), 0.0, -0.17), V3(wx(s * 0.33), -0.02, -0.2)];
        gl.add(new THREE.Mesh(new THREE.TubeGeometry(new THREE.CatmullRomCurve3(pts), 48, 0.003, 8), M.frame));
      }
      gl.traverse((o) => { if (o.isMesh && o.renderOrder === 0) o.renderOrder = 6; });
      shadowed(gl); gl.traverse((o) => { if (o.material === M.lens) o.castShadow = false; });
      bones.body.add(gl);
      this.glassesRest = gl.position.clone();
    }

    // ---- fita marcadora nova, saindo de baixo da página direita, para o lado
    {
      const w = 0.032, L = 0.15, shape = new THREE.Shape();
      shape.moveTo(-w / 2, 0); shape.lineTo(w / 2, 0); shape.lineTo(w / 2, -L); shape.lineTo(0, -L + 0.022); shape.lineTo(-w / 2, -L); shape.closePath();
      const geo = new THREE.ShapeGeometry(shape, 16); geo.userData.base = geo.attributes.position.array.slice();
      const rb = this.ribbon = new THREE.Mesh(geo, M.ribbon); rb.castShadow = true;
      rb.position.copy(toBody(0.235, 0.338, pageZ(0.235, 0.36) - 0.025)); bones.body.add(rb);
    }

    // ---- lápis e marca-texto (presos ao corpo; a mão vai até eles)
    {
      const pen = this.pencil = new THREE.Group();
      const body = new THREE.Mesh(new THREE.CylinderGeometry(0.0062, 0.0062, 0.13, 6), M.pencil); body.position.y = 0.085;
      const wood = new THREE.Mesh(new THREE.CylinderGeometry(0.0062, 0.0018, 0.02, 6), M.wood); wood.position.y = 0.01;
      const lead = new THREE.Mesh(new THREE.ConeGeometry(0.0018, 0.006, 8), M.lead); lead.rotation.x = Math.PI; lead.position.y = -0.0015;
      const fer = new THREE.Mesh(new THREE.CylinderGeometry(0.0066, 0.0066, 0.014, 16), M.ferrule); fer.position.y = 0.157;
      const er = new THREE.Mesh(new THREE.CapsuleGeometry(0.0062, 0.006, 4, 12), M.eraser); er.position.y = 0.169;
      pen.add(body, wood, lead, fer, er);
      const mk = this.marker = new THREE.Group();
      const mb = new THREE.Mesh(new THREE.CylinderGeometry(0.011, 0.011, 0.11, 16), M.marker); mb.position.y = 0.08;
      const tip = new THREE.Mesh(new THREE.BoxGeometry(0.012, 0.016, 0.005), M.marker); tip.position.y = 0.012;
      const cap = new THREE.Mesh(new THREE.CylinderGeometry(0.0115, 0.0115, 0.035, 16), M.markerCap); cap.position.y = 0.13;
      mk.add(mb, tip, cap);
      for (const t of [pen, mk]) { shadowed(t); t.visible = false; bones.body.add(t); }
      this.toolK = 0;
    }

    // ---- folha que vira
    {
      const nu = 22, nv = 18, geo = new THREE.PlaneGeometry(1, 1, nu, nv);
      this.sheetBase = []; const Pp = geo.attributes.position, uv = geo.attributes.uv;
      for (let i = 0; i < Pp.count; i++) { const u = uv.getX(i), v = uv.getY(i); this.sheetBase.push([lerp(PAGE.x0, PAGE.x1, u), lerp(PAGE.y0, PAGE.y1, v), u]); }
      const front = document.createElement("canvas"); front.width = model.prep.pageR.width; front.height = model.prep.pageR.height;
      this.sheetCanvas = front;
      const tF = new THREE.CanvasTexture(front); tF.colorSpace = THREE.SRGBColorSpace; tF.anisotropy = 4;
      const tB = new THREE.CanvasTexture(model.prep.pageL); tB.colorSpace = THREE.SRGBColorSpace; tB.anisotropy = 4;
      const geoB = geo.clone(); { const ub = geoB.attributes.uv; for (let i = 0; i < ub.count; i++) ub.setX(i, ub.getX(i)); }
      geoB.setAttribute("position", geo.attributes.position); geoB.setAttribute("normal", geo.attributes.normal);
      const mf = new THREE.Mesh(geo, new THREE.MeshStandardMaterial({ map: tF, roughness: 0.85, side: THREE.FrontSide, polygonOffset: true, polygonOffsetFactor: -1 }));
      const mb = new THREE.Mesh(geoB, new THREE.MeshStandardMaterial({ map: tB, roughness: 0.85, side: THREE.BackSide, color: "#f4f1ec", polygonOffset: true, polygonOffsetFactor: -1 }));
      for (const m of [mf, mb]) { m.renderOrder = 3; m.castShadow = true; m.visible = false; m.frustumCulled = false; bones.body.add(m); }
      this.sheet = { geo, mf, mb, tF, p: -1 };
    }

    // ---- braços de tubo (cor da manga), do ombro na capa até o pulso
    {
      const [sr, sg, sb] = model.sleeve, armMat = phys({ color: new THREE.Color(`rgb(${sr},${sg},${sb})`).convertSRGBToLinear ? new THREE.Color().setRGB(sr / 255, sg / 255, sb / 255, THREE.SRGBColorSpace) : "#2a4fd0", roughness: 0.45, clearcoat: 0.3, clearcoatRoughness: 0.4 });
      this.tubes = {};
      for (const sd of ["L", "R"]) {
        const m = new THREE.Mesh(new THREE.BufferGeometry(), armMat); m.castShadow = true; m.frustumCulled = false; bones.body.add(m);
        const cap = new THREE.Mesh(new THREE.SphereGeometry(0.0175, 16, 12), armMat); cap.castShadow = true; bones.body.add(cap);
        this.tubes[sd] = { m, cap };
      }
      bones.armL.scale.setScalar(0.001); bones.armR.scale.setScalar(0.001);
    }

    // ---- confete
    {
      const N = 120; this.conf = [];
      const cf = this.confetti = new THREE.InstancedMesh(new THREE.PlaneGeometry(0.016, 0.01), M.confetti, N);
      cf.frustumCulled = false; const cols = ["#F5B83D", "#4FD3C6", "#F4A6C0", "#6F64FF", "#ffffff", "#FFE07A"].map((c) => new THREE.Color(c));
      for (let i = 0; i < N; i++) { cf.setColorAt(i, cols[i % cols.length]); this.conf.push({ life: 0, p: V3(), v: V3(), w: V3(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5).normalize(), spin: 4 + Math.random() * 8, ph: Math.random() * 6 }); }
      scene.add(cf);
    }

    // ---- estado
    this.t = 0; this.yaw = 0; this.yawV = 0; this.dragging = false; this.faceYaw = 0;
    this.jump = 0; this.jumpV = 0; this.pokeT = -1; this.moodT = 0;
    this.blinkAt = 1.8; this.blinkT = -1; this.dbl = false;
    this.look = { x: 0, y: 0 }; this.glance = { x: 0, y: 0, until: 0, next: 2.5 };
    this.sp = { cap: { x: 0, v: 0 }, tasX: { x: 0, v: 0 }, tasZ: { x: 0, v: 0 }, rib: { x: 0, v: 0 }, gl: { x: 0, v: 0 }, wristL: { x: 0, v: 0 }, wristR: { x: 0, v: 0 } };
    this.prev = { lean: 0, bob: 0 }; this.cur = null;
    this.fist = { L: FIST0.L.clone(), R: FIST0.R.clone() };
    this.nextFlip = 9 + Math.random() * 5; this.nextShift = 3;
    this.writer = null;
    this.resize(container.clientWidth || 400, container.clientHeight || 400);
    this.setMood(mood, true);
    this.update(1 / 60);
  }

  setMood(m, first) {
    if (m === this.mood && !first) return;
    const was = this.mood; this.mood = m; this.moodT = 0;
    if (m === "happy" && !first) this.burst();
    if (m === "thinking" && !this.writer) this.writer = { step: 0, t: 0, last: null };
    if (was === "thinking" && m !== "thinking") this.writer = null;
  }
  poke() {
    if (this.jump <= 0.0001) { this.jumpV = 1.15; this.pokeT = 0; }
    if (this.mood === "happy") this.burst(60); else if (!this.sheetBusy()) this.startFlip(false);
  }
  burst(n = 110) {
    let k = 0;
    for (const c of this.conf) {
      if (c.life > 0 || k >= n) continue; k++;
      c.life = 2.6 + Math.random() * 1.4; c.p.set((Math.random() - 0.5) * 0.3, 0.9 + Math.random() * 0.05, 0.15);
      const a = Math.random() * Math.PI * 2, sp = 0.4 + Math.random() * 0.7;
      c.v.set(Math.cos(a) * sp * 0.6, 0.9 + Math.random() * 0.8, Math.sin(a) * sp * 0.4 + 0.2);
    }
  }
  sheetBusy() { return this.sheet.p >= 0; }

  // ---- folha: copia a página (com a escrita) para a folha e limpa o caderno
  startFlip(hand) {
    if (this.sheetBusy() || (window.matchMedia && window.matchMedia("(prefers-reduced-motion: reduce)").matches)) return;
    const c = this.sheetCanvas.getContext("2d");
    c.clearRect(0, 0, this.sheetCanvas.width, this.sheetCanvas.height);
    c.drawImage(this.model.prep.pageR, 0, 0); c.drawImage(this.ink.canvas, 0, 0, this.sheetCanvas.width, this.sheetCanvas.height);
    this.sheet.tF.needsUpdate = true;
    this.ink.g.clearRect(0, 0, this.ink.canvas.width, this.ink.canvas.height); this.ink.tex.needsUpdate = true;
    Object.assign(this.sheet, { p: 0, dur: hand ? 1.35 : 0.9, wait: hand ? 0.4 : 0, hand, released: !hand, caught: false });
  }
  sheetPoint(p, u, v) {
    // Posição (corpo) de um ponto da folha: gira em volta da costura, com a borda solta adiantada.
    const x = lerp(PAGE.x0, PAGE.x1, u), y = lerp(PAGE.y0, PAGE.y1, v), pz = this.pageZ;
    const zg = pz(0.0, y), z = pz(x, y) + 0.0035, dx = x, dz = z - zg, r = Math.hypot(dx, dz), a0 = Math.atan2(dz, dx);
    const lead = Math.sin(2 * Math.PI * p) * 0.22, curl = Math.sin(Math.PI * p) * 0.12;
    const e = ease(clamp(p + lead * u + curl * u * (1 - v), 0, 1)), a = a0 + (Math.PI - 2 * a0) * e;
    const lift = Math.sin(Math.PI * e) * 0.035 * u;
    return this.toBody(r * Math.cos(a), y, zg + r * Math.sin(a) + lift);
  }
  updateSheet(dt) {
    const s = this.sheet; if (s.p < 0) return;
    if (s.wait > 0) s.wait -= dt; else s.p = Math.min(1, s.p + dt / s.dur);
    const P = s.geo.attributes.position;
    for (let i = 0; i < P.count; i++) { const [, , u] = this.sheetBase[i], v = (this.sheetBase[i][1] - PAGE.y0) / (PAGE.y1 - PAGE.y0); const q = this.sheetPoint(s.p, u, v); P.setXYZ(i, q.x, q.y, q.z); }
    P.needsUpdate = true; s.geo.computeVertexNormals(); s.mf.visible = s.mb.visible = true;
    // Some no fim: ela deita sobre a página da esquerda, igualzinha a ela.
    const fade = 1 - smooth(0.9, 1, s.p);
    for (const m of [s.mf, s.mb]) { m.material.transparent = fade < 1; m.material.opacity = fade; }
    if (s.p >= 1) { s.p = -1; s.mf.visible = s.mb.visible = false; this.smoothT = s.caught ? 0.35 : 0; }
  }

  // ---- caderno: desenha na página da direita (coordenadas do modelo)
  inkXY(x, y) { const c = this.ink.canvas; return [(x - PAGE.x0) / (PAGE.x1 - PAGE.x0) * c.width, (PAGE.y1 - y) / (PAGE.y1 - PAGE.y0) * c.height]; }
  inkStroke(a, b, style, width) {
    const g = this.ink.g, [x0, y0] = this.inkXY(a[0], a[1]), [x1, y1] = this.inkXY(b[0], b[1]);
    g.strokeStyle = style; g.lineWidth = width; g.lineCap = "round"; g.lineJoin = "round"; g.beginPath(); g.moveTo(x0, y0); g.lineTo(x1, y1); g.stroke();
    this.ink.dirty = true;
  }
  // Roteiro do "pensando": escreve 3 linhas, faz um visto, passa marca-texto numa linha e vira a folha.
  writeStep(dt) {
    const w = this.writer; if (!w || this.sheetBusy()) return null;
    w.t += dt;
    const LINES = [[0.06, 0.455, 0.27], [0.06, 0.425, 0.3], [0.06, 0.395, 0.21]];
    const step = w.step;
    if (step < 3) {
      const [x0, y, x1] = LINES[step], dur = 1.7, k = w.t / dur;
      if (k >= 1) { w.step++; w.t = -0.25; w.last = null; return { tool: "pencil", x: x1, y, down: false }; }
      if (k < 0) return { tool: "pencil", x: x0, y, down: false };
      // Letrinhas: laços pequenos, com espaços entre "palavras".
      const x = lerp(x0, x1, k), ph = (x - x0) * 520, yy = y + 0.006 * Math.sin(ph) + 0.002 * Math.sin(ph * 0.37);
      const xx = x + 0.0035 * Math.cos(ph), down = Math.sin(x * 160) < 0.82;
      if (down && w.last) this.inkStroke(w.last, [xx, yy], "rgba(52,48,66,.85)", 2.1);
      w.last = down ? [xx, yy] : null;
      return { tool: "pencil", x: xx, y: yy, down };
    }
    if (step === 3) {   // visto na margem
      const pts = [[0.288, 0.668], [0.298, 0.655], [0.32, 0.69]], dur = 0.55, k = clamp(w.t / dur, 0, 1);
      if (w.t >= dur + 0.2) { w.step++; w.t = -0.3; w.last = null; }
      const seg = k < 0.4 ? 0 : 1, kk = seg ? (k - 0.4) / 0.6 : k / 0.4, a = pts[seg], b = pts[seg + 1], p = [lerp(a[0], b[0], kk), lerp(a[1], b[1], kk)];
      if (w.t > 0 && w.last) this.inkStroke(w.last, p, "rgba(40,120,90,.9)", 3);
      w.last = p; return { tool: "pencil", x: p[0], y: p[1], down: w.t > 0 && w.t < dur };
    }
    if (step === 4) {   // marca-texto na primeira linha impressa
      const y = 0.731, x0 = 0.07, x1 = 0.255, dur = 1.1, k = clamp(w.t / dur, 0, 1), x = lerp(x0, x1, ease(k));
      if (w.t > 0 && w.last) this.inkStroke(w.last, [x, y], "rgba(255,214,40,.42)", 13);
      w.last = [x, y];
      if (w.t >= dur + 0.35) { w.step++; w.t = 0; w.last = null; }
      return { tool: "marker", x, y, down: w.t > 0 && w.t < dur };
    }
    // vira a folha com a mão e recomeça
    this.startFlip(true); w.step = 0; w.t = -0.6; w.last = null;
    return null;
  }

  // ---- pose alvo
  pose(t) {
    const m = this.mood, br = Math.sin(t * 2.0), mt = this.moodT;
    const p = { bob: 0.004 * br, sy: 1 + 0.012 * br, sx: 1 - 0.005 * br, lean: 0.012 * Math.sin(t * 0.8), tilt: 0, twist: 0.04 * Math.sin(t * 0.5), yaw: 0,
      fL: V3(-0.04, 0.012 + 0.006 * Math.sin(t * 1.7), 0.03), fR: V3(0.04, 0.012 + 0.006 * Math.sin(t * 1.7 + 0.8), 0.03), wristL: 0, wristR: 0,
      liftL: 0, liftR: 0, pitchL: 0, pitchR: 0, stepL: 0, stepR: 0, shift: 0,
      eyes: "open", lid: 1, mouth: "open", open: 0.55, brow: 0, browTilt: 0 };
    // Troca o peso de um pé para o outro de vez em quando (parado/falando).
    if (m === "idle" || m === "talking") { const sh = Math.sin(t * 0.6); p.shift = 0.008 * sh; p.liftL = Math.max(0, Math.sin(t * 0.6 + 2.6)) ** 8 * 0.018; }
    if (m === "happy") {
      const ph = t * 5.6, s = Math.sin(ph), up = Math.abs(s);
      p.bob = 0.035 * up; p.sy = 1 + 0.03 * up - 0.02; p.sx = 1 - 0.015 * up + 0.01; p.lean = 0.07 * Math.sin(ph * 0.5); p.twist = 0.12 * Math.sin(ph * 0.5);
      p.fL = V3(-0.02, 0.06 + 0.05 * up, 0.04); p.fR = V3(0.02, 0.1 + 0.05 * Math.abs(Math.sin(ph + 1.4)), 0.04); p.wristL = 0.3 * Math.sin(ph); p.wristR = -0.3 * Math.sin(ph + 1);
      p.liftL = Math.max(0, s) * 0.06; p.pitchL = -Math.max(0, s) * 0.45; p.liftR = Math.max(0, -s) * 0.06; p.pitchR = -Math.max(0, -s) * 0.45;
      p.eyes = "arc"; p.mouth = "open"; p.open = 1;
    } else if (m === "wave") {
      // Tchau: braço sobe por inteiro, o punho abana com atraso e o corpo acompanha de leve.
      const w = Math.sin(t * 7.5);
      p.fR = V3(0.06, 0.2, 0.06); p.wristR = 0.55 * w; p.lean = -0.035 + 0.012 * w; p.twist = -0.12; p.bob = 0.006 * Math.abs(w);
      p.eyes = (Math.floor(t / 2.2) % 3 === 2) ? "wink" : "arc"; p.mouth = "open"; p.open = 0.85;
    } else if (m === "talking") {
      const g = Math.sin(t * 3.1);
      p.fR = V3(0.0, 0.03 + 0.04 * g, 0.12); p.wristR = 0.3 * Math.sin(t * 3.1 + 0.6); p.fL = V3(0, 0.01 * Math.sin(t * 2.3), 0.04);
      p.bob = 0.006 * Math.abs(Math.sin(t * 5.2)); p.twist = 0.08 * Math.sin(t * 1.3); p.lean = 0.02 * g;
      p.mouth = "talk"; p.brow = 0.004 * Math.max(0, Math.sin(t * 2.1));
    } else if (m === "thinking") {
      p.lean = 0.03; p.tilt = 0.05; p.twist = 0.06; p.mouth = "hmm"; p.brow = 0.006; p.browTilt = 0.18;
    } else if (m === "point") {
      p.fR = V3(0.12, -0.04, 0.12); p.twist = -0.16; p.lean = -0.03; p.mouth = "smile";
    } else if (m === "wink") {
      p.fR = V3(0.02, 0.06, 0.06); p.lean = -0.05; p.twist = 0.1; p.eyes = "wink"; p.mouth = "open"; p.open = 0.7;
    } else if (m === "sad") {
      // Errou: balança a cabeça ("não") no começo e murcha.
      const shake = mt < 1.2 ? Math.sin(mt * 16) * 0.16 * (1 - mt / 1.2) : 0;
      p.bob = -0.02; p.sy = 0.975; p.tilt = 0.1; p.lean = 0.02; p.twist = shake;
      p.fL = V3(0.035, -0.2, 0.05); p.fR = V3(-0.035, -0.17, 0.05); p.wristL = 0.4; p.wristR = -0.4;
      p.mouth = "frown"; p.lid = 0.82; p.brow = -0.002; p.browTilt = -0.3;
    } else if (m === "run" || m === "run-left") {
      // Corrida: de lado para a direção, inclinado para a frente, pés alternando de verdade, braços balançando.
      const ph = t * 13, s = Math.sin(ph), c = Math.cos(ph);
      p.yaw = m === "run" ? 1.05 : -1.05; p.tilt = 0.16; p.bob = 0.022 * Math.abs(s); p.sy = 1 + 0.02 * Math.abs(s) - 0.01; p.twist = 0.06 * s;
      p.stepL = 0.08 * s; p.stepR = -0.08 * s; p.liftL = Math.max(0, c) * 0.055; p.liftR = Math.max(0, -c) * 0.055; p.pitchL = -0.5 * Math.max(0, c) + 0.2 * s; p.pitchR = -0.5 * Math.max(0, -c) - 0.2 * s;
      p.fL = V3(0, 0.02, -0.11 * s); p.fR = V3(0, 0.02, 0.11 * s); p.mouth = "open"; p.open = 0.75; p.brow = 0.003;
    }
    return p;
  }

  update(dt) {
    const t = (this.t += dt); this.moodT += dt;
    const p = this.pose(t), M = mats(), B = this.bones, Rr = this.rest, still = window.matchMedia && window.matchMedia("(prefers-reduced-motion: reduce)").matches;
    // Pulo do toque.
    if (this.jumpV > 0 || this.jump > 0) { this.jumpV -= 5.5 * dt; this.jump += this.jumpV * dt; if (this.jump <= 0) { this.jump = 0; this.jumpV = 0; this.pokeT = 0.0001; this.sp.gl.v -= 0.5; } }
    if (this.pokeT >= 0 && this.jump === 0) { this.pokeT += dt; const k = Math.exp(-this.pokeT * 8) * Math.sin(this.pokeT * 24); p.sy -= 0.07 * k; p.sx += 0.045 * k; if (this.pokeT > 0.9) this.pokeT = -1; }

    // Folheia sozinho de vez em quando (parado ou falando).
    if (!still && t > this.nextFlip && (this.mood === "idle" || this.mood === "talking") && !this.sheetBusy()) { this.startFlip(true); this.nextFlip = t + 11 + Math.random() * 6; }
    // Caderno e ferramentas.
    const pen = this.mood === "thinking" && !still ? this.writeStep(dt) : null;
    this.updateSheet(dt);

    // ---- alvos das mãos (no espaço do corpo)
    const tgt = { L: this.toBody(FIST0.L.x, FIST0.L.y, FIST0.L.z).add(p.fL), R: this.toBody(FIST0.R.x, FIST0.R.y, FIST0.R.z).add(p.fR) };
    let fast = { L: false, R: false };
    const s = this.sheet;
    if (s.p >= 0 && s.hand) {
      const edge = this.sheetPoint(s.wait > 0 ? 0 : s.p, 1, 0.3);
      if (!s.released) { tgt.R = edge.clone().add(V3(0.03, 0.0, 0.035)); fast.R = true; if (s.p > 0.42) s.released = true; }
      if (!s.caught && s.p > 0.62) s.caught = true;
      if (s.caught) { tgt.L = edge.clone().add(V3(-0.03, 0.0, 0.035)); fast.L = true; }
    }
    if (this.smoothT > 0) { this.smoothT -= dt; tgt.L = this.sheetPoint(1, 1, 0.3).add(V3(-0.03, 0, 0.035)); fast.L = true; }
    // Ferramenta na mão direita: a ponta encosta na página e o punho segura mais acima.
    let tool = null;
    if (pen) {
      const tip = this.toBody(pen.x, pen.y, this.pageZ(pen.x, pen.y) + (pen.down ? 0.001 : 0.016));
      const dir = V3(0.34, 0.5, 0.8).normalize();
      tool = { tip, dir, kind: pen.tool };
      tgt.R = tip.clone().addScaledVector(dir, 0.105).add(V3(0.022, 0.018, 0.02)); fast.R = true;
    }
    this.toolK = lerp(this.toolK, tool ? 1 : 0, 1 - Math.exp(-dt * 10));
    for (const [obj, kind] of [[this.pencil, "pencil"], [this.marker, "marker"]]) {
      const on = tool && tool.kind === kind;
      obj.visible = on && this.toolK > 0.05;
      if (on) { obj.position.copy(tool.tip); obj.quaternion.setFromUnitVectors(V3(0, 1, 0), tool.dir); obj.scale.setScalar(Math.max(0.001, this.toolK)); }
    }

    // ---- suavização da pose
    if (!this.cur) this.cur = { ...p };
    const c = this.cur, k = 1 - Math.exp(-dt * 9), kf = 1 - Math.exp(-dt * 20);
    for (const key of Object.keys(p)) {
      if (typeof p[key] !== "number") { c[key] = p[key]; continue; }
      c[key] = lerp(c[key], p[key], ["bob", "sy", "sx"].includes(key) || key.startsWith("lift") || key.startsWith("pitch") || key.startsWith("step") ? kf : k);
    }
    for (const sd of ["L", "R"]) this.fist[sd].lerp(tgt[sd], 1 - Math.exp(-dt * (fast[sd] ? 22 : 10)));

    // ---- corpo
    B.body.position.copy(Rr.body).add(V3(c.shift, c.bob + this.jump, 0));
    B.body.rotation.set(c.tilt, c.twist, c.lean);
    B.body.scale.set(c.sx, c.sy, 1 + (1 - c.sy) * 0.5);
    // ---- pés
    B.footL.position.copy(Rr.footL).add(V3(0, c.liftL + this.jump, c.stepL + c.liftL * 0.3)); B.footL.rotation.set(c.pitchL, 0, 0);
    B.footR.position.copy(Rr.footR).add(V3(0, c.liftR + this.jump, c.stepR + c.liftR * 0.3)); B.footR.rotation.set(c.pitchR, 0, 0);
    // ---- braços de mangueira: o punho vai ao alvo; o braço gira só um pouco e estica o resto
    const sp = (st, target, kk, dd) => { const n = Math.ceil(dt / (1 / 120)), h = dt / n; for (let i = 0; i < n; i++) { st.v += ((target - st.x) * kk - st.v * dd) * h; st.x += st.v * h; } return st.x; };
    for (const sd of ["L", "R"]) {
      const sgn = sd === "L" ? -1 : 1, fore = B["fore" + sd], W = this.fist[sd], S = Rr["arm" + sd];
      // O punho fica de pé, inclinado para o lado de onde vem o braço, mais o aceno do pulso.
      const dir = W.clone().sub(S); const lean = clamp(Math.atan2(-dir.x, Math.max(0.02, dir.y + 0.06)) * 0.45, -0.9, 0.9);
      fore.position.copy(W); fore.rotation.set(0, 0, lean + sp(this.sp["wrist" + sd], c["wrist" + sd], 120, 9));
      // Tubo: sai para fora da capa e faz uma curva macia até o pulso.
      const out = S.clone().add(V3(sgn * 0.045, 0.004, 0.012)), up = V3(Math.sin(-fore.rotation.z), -Math.cos(fore.rotation.z), 0);
      const wristIn = W.clone().addScaledVector(up, 0.02);
      const curve = new THREE.CubicBezierCurve3(S, out, wristIn.clone().addScaledVector(up, 0.045), wristIn);
      // A manga original (escondida) encolhe para dentro da pulseira, sem esticar triângulos até o ombro.
      B["arm" + sd].position.copy(wristIn);
      const tb = this.tubes[sd]; tb.m.geometry.dispose(); tb.m.geometry = new THREE.TubeGeometry(curve, 20, 0.0175, 12, false); tb.cap.position.copy(wristIn);
    }

    // ---- rosto
    if (t > this.blinkAt && this.blinkT < 0) this.blinkT = 0;
    let lid = 1;
    if (this.blinkT >= 0) {
      this.blinkT += dt; const q = this.blinkT / 0.15; lid = q < 1 ? Math.abs(Math.cos(Math.PI * q)) : 1;
      if (q >= 1) { this.blinkT = -1; if (!this.dbl && Math.random() < 0.25) { this.dbl = true; this.blinkAt = t + 0.12; } else { this.dbl = false; this.blinkAt = t + 2 + Math.random() * 3.2; } }
    }
    if (t > this.glance.next) this.glance = { x: Math.random() * 2 - 1, y: Math.random() - 0.5, until: t + 0.8, next: t + 2.5 + Math.random() * 3.5 };
    let lx = t < this.glance.until ? this.glance.x : 0, ly = t < this.glance.until ? this.glance.y : 0;
    if (pen) { lx = 0.6; ly = -0.9; }
    if (s.p >= 0) { const e = this.sheetPoint(Math.max(0, s.p), 1, 0.5); lx = clamp((e.x + this.rest.body.x) / 0.3, -1, 1); ly = -0.2; }
    if (this.mood === "point") { lx = 1; ly = 0; }
    if (this.mood === "sad") ly = -0.8;
    if (this.mood === "thinking" && !pen) { lx = 0.5; ly = 0.8; }
    this.look.x = lerp(this.look.x, lx, 1 - Math.exp(-dt * 10)); this.look.y = lerp(this.look.y, ly, 1 - Math.exp(-dt * 10));
    for (const sd of ["L", "R"]) {
      const e = this.eyes[sd], arc = c.eyes === "arc" || (c.eyes === "wink" && sd === "R");
      e.arc.visible = arc; e.open.visible = !arc;
      e.open.scale.y = Math.max(0.06, lid * c.lid); e.open.position.set(this.look.x * 0.006, this.look.y * 0.006, 0);
      e.h1.visible = e.h2.visible = lid > 0.4;
      e.brow.visible = Math.abs(c.brow) > 0.0005 || Math.abs(c.browTilt) > 0.02;
      e.brow.position.y = 0.05 + c.brow * 2; e.brow.rotation.z = c.browTilt * (sd === "L" ? 1 : -1);
    }
    this.drawMouth(c.mouth, c.mouth === "talk" ? this.talkOpen(t) : c.open, t);
    if (this.ink.dirty) { this.ink.tex.needsUpdate = true; this.ink.dirty = false; }

    // ---- molas: óculos, capelo, borla, fita
    const dLean = (c.lean - this.prev.lean) / Math.max(dt, 1e-3), dBob = (c.bob + this.jump - this.prev.bob) / Math.max(dt, 1e-3);
    this.prev.lean = c.lean; this.prev.bob = c.bob + this.jump;
    const S = this.sp, kick = Math.min(1, dt * 60);
    S.gl.v += -dBob * 0.012 * kick; S.cap.v += -dLean * 0.25 * kick; S.tasZ.v += (-dLean * 2.2 - dBob * 0.6) * kick; S.tasX.v += dBob * 0.9 * kick; S.rib.v += (-dLean * 1.5 + dBob * 1.4) * kick;
    this.glasses.position.copy(this.glassesRest).add(V3(0, sp(S.gl, 0, 260, 12), 0));
    B.cap.rotation.z = sp(S.cap, 0, 160, 10);
    B.tassel.rotation.set(sp(S.tasX, 0.04 * Math.sin(t * 0.9), 45, 2.6), 0, sp(S.tasZ, -c.lean * 0.8 + 0.05 * Math.sin(t * 1.3), 45, 2.6));
    { const sw = sp(S.rib, 0.08 * Math.sin(t * 1.5), 40, 2.6), base = this.ribbon.geometry.userData.base, arr = this.ribbon.geometry.attributes.position.array;
      for (let i = 0; i < arr.length; i += 3) { const d = -base[i + 1]; arr[i] = base[i] + Math.sin(t * 2.1 - d * 30) * d * 0.06 - c.lean * d * 0.5; arr[i + 2] = base[i + 2] + d * sw + d * d * 1.2; }
      this.ribbon.geometry.attributes.position.needsUpdate = true; this.ribbon.geometry.computeVertexNormals(); }

    // ---- confete
    let any = false; const M4 = new THREE.Matrix4(), Q = new THREE.Quaternion(), Sc = V3();
    this.conf.forEach((cf, i) => {
      if (cf.life <= 0) { this.confetti.setMatrixAt(i, M4.makeScale(0, 0, 0)); return; }
      any = true; cf.life -= dt; cf.v.y -= 2.2 * dt; cf.v.multiplyScalar(1 - 1.6 * dt); if (cf.v.y < -0.28) cf.v.y = -0.28;
      cf.p.addScaledVector(cf.v, dt); cf.p.x += Math.sin(t * 3 + cf.ph) * 0.0015; if (cf.p.y < 0.003) { cf.p.y = 0.003; cf.v.set(0, 0, 0); }
      Q.setFromAxisAngle(cf.w, t * cf.spin + cf.ph); const sc = Math.min(1, cf.life * 2);
      this.confetti.setMatrixAt(i, M4.compose(cf.p, Q, Sc.set(sc, sc, sc)));
    });
    this.confetti.visible = any; this.confetti.instanceMatrix.needsUpdate = true;

    // ---- giro: arrastar, ou de lado na corrida
    if (!this.dragging) { this.yawV += (-this.yaw * 25 - this.yawV * 7) * dt; this.yaw += this.yawV * dt; }
    this.faceYaw = lerp(this.faceYaw, c.yaw || 0, 1 - Math.exp(-dt * 8));
    this.rig.rotation.y = this.yaw + this.faceYaw;
    void M;
  }

  // Fala: abre e fecha em "sílabas", com pausas entre as palavras.
  talkOpen(t) {
    const word = Math.sin(t * 1.9) + 0.35 * Math.sin(t * 4.7) > -0.55;
    const syl = Math.max(0, Math.sin(t * 13) * 0.6 + Math.sin(t * 21.7 + 1) * 0.4);
    return word ? 0.25 + 0.75 * syl : 0.05;
  }
  drawMouth(kind, open, t) {
    const d = this.mouthD, key = kind + ":" + open.toFixed(2);
    if (key === this.mouthKey) return; this.mouthKey = key;
    const g = d.g, W = d.canvas.width, H = d.canvas.height, [x0, x1, y0, y1] = FACE.mouth;
    const X = (x) => (x - x0) / (x1 - x0) * W, Y = (y) => (y1 - y) / (y1 - y0) * H;
    g.clearRect(0, 0, W, H);
    const cx = X(0.014), top = Y(0.556), hw = X(0.062) - cx, ink = "#24123A";
    g.lineCap = "round"; g.lineJoin = "round";
    if (kind === "open" || kind === "talk") {
      const o = clamp(open, 0, 1.2), depth = (Y(0.49) - top) * (0.18 + 0.82 * o), w = hw * (kind === "talk" ? 0.72 + 0.2 * o : 1);
      g.fillStyle = ink; g.beginPath(); g.moveTo(cx - w, top); g.quadraticCurveTo(cx, top - depth * 0.08, cx + w, top);
      g.bezierCurveTo(cx + w, top + depth * 0.62, cx + w * 0.55, top + depth, cx, top + depth); g.bezierCurveTo(cx - w * 0.55, top + depth, cx - w, top + depth * 0.62, cx - w, top); g.fill();
      if (depth > 6) { g.save(); g.clip(); g.fillStyle = "#F07C9A"; g.beginPath(); g.ellipse(cx, top + depth * 0.98, w * 0.6, depth * 0.42, 0, 0, Math.PI * 2); g.fill(); g.restore(); }
      g.strokeStyle = "rgba(255,255,255,.25)"; g.lineWidth = 2; g.beginPath(); g.moveTo(cx - w * 0.7, top + 2); g.quadraticCurveTo(cx, top + 1, cx + w * 0.7, top + 2); g.stroke();
    } else {
      g.strokeStyle = ink; g.lineWidth = H * 0.075; g.beginPath();
      if (kind === "smile") { g.moveTo(cx - hw * 0.62, top + 4); g.quadraticCurveTo(cx, top + H * 0.32, cx + hw * 0.62, top + 4); }
      else if (kind === "frown") { g.moveTo(cx - hw * 0.55, top + H * 0.24); g.quadraticCurveTo(cx, top - H * 0.02, cx + hw * 0.55, top + H * 0.24); }
      else { g.moveTo(cx - hw * 0.45, top + H * 0.12); g.bezierCurveTo(cx - hw * 0.15, top + H * 0.2, cx + hw * 0.15, top + H * 0.02, cx + hw * 0.5, top + H * 0.1); }
      g.stroke();
    }
    d.tex.needsUpdate = true;
  }

  resize(w, h) {
    this.renderer.setSize(w, h, false);
    const cam = this.camera, asp = w / Math.max(1, h); cam.aspect = asp;
    const tan = 2 * Math.tan(THREE.MathUtils.degToRad(cam.fov / 2)), fw = this.bleed ? 0.95 : 1.15, fh = this.bleed ? 1.12 : 1.3;
    const d = Math.max(fh / tan, fw / (tan * asp));
    cam.position.set(0, 0.55 + d * 0.12, d); cam.lookAt(0, this.bleed ? 0.5 : 0.48, 0);
    cam.updateProjectionMatrix();
  }
  render() { this.renderer.render(this.scene, this.camera); }
  dispose() { this.renderer.dispose(); this.renderer.forceContextLoss(); this.renderer.domElement.remove(); }
}

export function webglAvailable() { try { const c = document.createElement("canvas"); return !!(c.getContext("webgl2") || c.getContext("webgl")); } catch { return false; } }

// ------------------------------------------------------------------ <estudario-mascote3d>
const live = new Set();
const MAX_LIVE = 6;   // o navegador só dá uns 16 contextos WebGL; além disso, o recorte 2D
let raf = 0, last = 0;
const MAX_DT = typeof location !== "undefined" && location.search.includes("shot") ? 0.2 : 1 / 30;
function loop(now) {
  const dt = Math.max(1 / 240, Math.min(MAX_DT, last ? (now - last) / 1000 : 1 / 60)); last = now;
  for (const el of live) if (el._visible && el.m) { el.m.update(dt); el.m.render(); }
  raf = live.size ? requestAnimationFrame(loop) : 0; if (!raf) last = 0;
}
if (typeof customElements !== "undefined" && !customElements.get("estudario-mascote3d")) {
  customElements.define("estudario-mascote3d", class extends HTMLElement {
    static get observedAttributes() { return ["mood"]; }
    async connectedCallback() {
      if (this.m) { live.add(this); if (!raf) raf = requestAnimationFrame(loop); return; }
      if (this._starting) return;
      this._starting = true;
      const size = +(this.getAttribute("size") || 0);
      if (size) { this.style.width = this.style.width || size + "px"; this.style.height = this.style.height || size + "px"; }
      if (!this.style.display) this.style.display = "inline-block";
      if (!this.style.position) this.style.position = "relative";
      const fallback = () => { const f = document.createElement("estudario-mascote"); f.setAttribute("mood", (this.getAttribute("mood") || "idle").replace(/^run.*/, "happy")); if (size) f.setAttribute("size", String(size)); this.appendChild(f); this._fallback = f; };
      if (!webglAvailable() || live.size >= MAX_LIVE) { fallback(); return; }
      let host = this;
      if (size) { host = document.createElement("div"); host.style.cssText = "position:absolute;left:-20%;top:-20%;width:140%;height:140%;pointer-events:none"; this.appendChild(host); }
      let model; try { model = await loadModel(); } catch (e) { console.warn("Folha 3D:", e); fallback(); return; }
      this.m = new Mascote3D(host, model, { mood: this.getAttribute("mood") || "idle", bleed: !!size });
      this.m.resize(host.clientWidth || 1, host.clientHeight || 1);
      this._ro = new ResizeObserver(() => this.m.resize(host.clientWidth || 1, host.clientHeight || 1)); this._ro.observe(this);
      this._io = new IntersectionObserver((es) => { for (const e of es) this._visible = e.isIntersecting && !document.hidden; }); this._io.observe(this);
      this._visible = true;
      let down = null;
      this.addEventListener("pointerdown", (e) => { down = { x: e.clientX, moved: 0 }; if (this.hasAttribute("spin")) { this.m.dragging = true; try { this.setPointerCapture(e.pointerId); } catch (_) { /* ok */ } } });
      this.addEventListener("pointermove", (e) => { if (!down || !this.m.dragging) return; const dx = e.clientX - down.x; down.moved += Math.abs(dx); down.x = e.clientX; this.m.yaw += dx * 0.012; this.m.yawV = dx * 0.6; });
      this.addEventListener("pointerup", () => { if (down && down.moved < 6) { this.m.poke(); this.dispatchEvent(new CustomEvent("folha-poke", { bubbles: true })); } down = null; this.m.dragging = false; });
      // Na reação de acerto das questões ele comemora com confete.
      if (this.closest(".folha-cheer")) this.m.burst(90);
      live.add(this); if (!raf) raf = requestAnimationFrame(loop);
    }
    disconnectedCallback() { live.delete(this); }
    attributeChangedCallback(n, _, v) { if (n !== "mood") return; if (this.m) this.m.setMood(v || "idle"); if (this._fallback) this._fallback.setAttribute("mood", (v || "idle").replace(/^run.*/, "happy")); }
  });
}
if (typeof window !== "undefined") window.EstudarioMascote3D = { Mascote3D, webglAvailable };
