// O Folha em 3D: o modelo da arte oficial (design/mascote/folha-meshy.glb, gerado pelo Meshy a partir
// da folha de vistas; scripts/web/mascote/extrair-glb.html tira só a figura de frente e reduz as
// texturas -> web/src/jsMain/resources/folha.glb). Aqui ele ganha um esqueleto (corpo, braços com
// cotovelo, pés, capelo, borla e fita), com pesos calculados pela posição dos vértices, e é animado.
// Gera web/src/jsMain/resources/mascote3d.js com `node scripts/web/build-mascote3d.mjs`.
//
// Uso: <estudario-mascote3d mood="idle|happy|wave|talking|thinking|sad|point|wink" size="160" [spin]>
// Sem WebGL (ou com muitos na tela) cai no recorte animado (<estudario-mascote>).
import * as THREE from "three";
import { GLTFLoader } from "three/addons/loaders/GLTFLoader.js";
import { RoomEnvironment } from "three/addons/environments/RoomEnvironment.js";

const V3 = (x = 0, y = 0, z = 0) => new THREE.Vector3(x, y, z);
const clamp = (v, a, b) => Math.max(a, Math.min(b, v));
const lerp = (a, b, t) => a + (b - a) * t;
const smooth = (a, b, x) => { const t = clamp((x - a) / (b - a), 0, 1); return t * t * (3 - 2 * t); };

const base = (() => { try { const s = document.currentScript && document.currentScript.src; return s ? s.replace(/[^/]*$/, "") : ""; } catch { return ""; } })();

// ------------------------------------------------------------------ juntas (modelo com 1 de altura, pés em y = 0)
const J = {
  body: [0.02, 0.30, 0.03],
  cap: [0.04, 0.835, 0.0], tassel: [0.235, 0.858, 0.19],
  armL: [-0.345, 0.55, 0.02], foreL: [-0.44, 0.565, 0.02],
  armR: [0.385, 0.52, 0.02], foreR: [0.45, 0.585, 0.02],
  ribbon: [0.01, 0.315, 0.12],
  footL: [-0.15, 0.14, -0.01], footR: [0.2, 0.14, -0.01],
};
const PARENT = { body: "root", cap: "body", tassel: "cap", armL: "body", foreL: "armL", armR: "body", foreR: "armR", ribbon: "body", footL: "root", footR: "root" };
const ORDER = ["root", "body", "cap", "tassel", "armL", "foreL", "armR", "foreR", "ribbon", "footL", "footR"];

// Pesos de cada vértice pela posição (o modelo é uma malha só, sem esqueleto).
function weightsFor(P, UV, color, index) {
  const n = P.count, idx = new Uint16Array(n * 4), wts = new Float32Array(n * 4);
  // Braços: parte do que com certeza é braço (para fora da capa) e cresce pelos vértices ligados que
  // têm cor de braço (luva branca, pulseira dourada, manga azul clara); o azul-marinho da capa barra.
  const isArmColor = (i) => { const [cr, cg, cb] = color(UV.getX(i), UV.getY(i)), lum = 0.3 * cr + 0.59 * cg + 0.11 * cb; return lum > 150 || (cr > 150 && cb < 120) || (cb > 150 && lum > 70); };
  const key = (i) => Math.round(P.getX(i) * 1e4) + "," + Math.round(P.getY(i) * 1e4) + "," + Math.round(P.getZ(i) * 1e4);
  const same = new Map(), grp = new Int32Array(n);
  for (let i = 0; i < n; i++) { const k = key(i); if (!same.has(k)) same.set(k, same.size); grp[i] = same.get(k); }
  const G = same.size, members = Array.from({ length: G }, () => []), adj = Array.from({ length: G }, () => new Set());
  for (let i = 0; i < n; i++) members[grp[i]].push(i);
  for (let t = 0; t < index.length; t += 3) { const a = grp[index[t]], b = grp[index[t + 1]], c = grp[index[t + 2]]; adj[a].add(b); adj[a].add(c); adj[b].add(a); adj[b].add(c); adj[c].add(a); adj[c].add(b); }
  const arm = new Int8Array(n);   // 0 nada, -1 esquerdo, 1 direito
  for (const [sd, seed, lim] of [[-1, (x) => x < -0.385, (x) => x < -0.33], [1, (x) => x > 0.425, (x) => x > 0.355]]) {
    const seen = new Uint8Array(G), q = [];
    for (let gI = 0; gI < G; gI++) { const v = members[gI][0], y = P.getY(v); if (seed(P.getX(v)) && y > 0.43 && y < 0.8) { seen[gI] = 1; q.push(gI); } }
    while (q.length) {
      const gI = q.pop(); for (const v of members[gI]) arm[v] = sd;
      for (const nb of adj[gI]) { if (seen[nb]) continue; const v = members[nb][0], y = P.getY(v); if (lim(P.getX(v)) && y > 0.43 && y < 0.8 && members[nb].some(isArmColor)) { seen[nb] = 1; q.push(nb); } }
    }
    // Fecha buracos: vértice da região cercado de braço (dobras escuras da luva) vira braço também.
    for (let it = 0; it < 4; it++) {
      const add = [];
      for (let gI = 0; gI < G; gI++) {
        if (seen[gI]) continue; const v = members[gI][0], y = P.getY(v); if (!lim(P.getX(v)) || y < 0.43 || y > 0.8) continue;
        let k = 0, tot = 0; for (const nb of adj[gI]) { tot++; if (seen[nb]) k++; }
        if (tot && k / tot >= 0.5) add.push(gI);
      }
      for (const gI of add) { seen[gI] = 1; for (const v of members[gI]) arm[v] = sd; }
    }
  }
  // Altura da folha atrás da borla (para separar a borla da página): maior z nas faixas vizinhas.
  const pz = {};
  for (let y = 0.68; y <= 0.885; y += 0.01) {
    let z = -9;
    for (let i = 0; i < n; i++) { const x = P.getX(i), yy = P.getY(i); if (Math.abs(yy - y) < 0.012 && ((x > 0.12 && x < 0.18) || (x > 0.29 && x < 0.33))) z = Math.max(z, P.getZ(i)); }
    pz[y.toFixed(2)] = z;
  }
  const B = Object.fromEntries(ORDER.map((k, i) => [k, i]));
  for (let i = 0; i < n; i++) {
    const x = P.getX(i), y = P.getY(i), z = P.getZ(i), w = new Map();
    const add = (k, v) => { if (v > 1e-4) w.set(B[k], (w.get(B[k]) || 0) + v); };
    const side = x < 0.03 ? "L" : "R";
    const zp = pz[clamp(Math.round(y * 100) / 100, 0.68, 0.88).toFixed(2)];
    // Borla: pendurada na frente da página direita.
    if (x > 0.19 && x < 0.285 && y > 0.69 && y < 0.875 && z > zp + 0.008) { const t = smooth(0.86, 0.875, y); add("tassel", 1 - t); add("cap", t); }
    // Fita marcadora: no meio, embaixo do livro, na frente.
    else if (Math.abs(x - 0.01) < 0.045 && y > 0.18 && y < 0.33 && z > 0.02) { const t = smooth(0.29, 0.325, y); add("ribbon", 1 - t); add("body", t); }
    // Pernas e pés: o pé manda embaixo; a perna estica entre o pé e o corpo.
    else if (y < 0.31) { const t = smooth(0.15, 0.3, y); add("foot" + side, 1 - t); add("body", t); }
    // Braços (para fora da capa) com cotovelo; o punho e o antebraço vão com o "fore".
    else if (arm[i] === -1) { const a = 1, f = smooth(0.555, 0.6, y); add("body", 1 - a); add("armL", a - f); add("foreL", f); }
    else if (arm[i] === 1) { const a = 1, f = smooth(0.55, 0.59, y); add("body", 1 - a); add("armR", a - f); add("foreR", f); }
    // Capelo.
    else if (y > 0.82) { const t = smooth(0.825, 0.85, y); add("cap", t); add("body", 1 - t); }
    else add("body", 1);
    const top = [...w.entries()].sort((a, b) => b[1] - a[1]).slice(0, 4), sum = top.reduce((s, e) => s + e[1], 0) || 1;
    top.forEach(([b, v], k) => { idx[i * 4 + k] = b; wts[i * 4 + k] = v / sum; });
  }
  return { idx, wts };
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
    // Lê a textura de cor (reduzida) para separar o que é braço do que é capa.
    const img = mesh.material.map.image, cs = 512, cv = document.createElement("canvas"); cv.width = cv.height = cs;
    const cx = cv.getContext("2d", { willReadFrequently: true }); cx.drawImage(img, 0, 0, cs, cs);
    const px = cx.getImageData(0, 0, cs, cs).data;
    const color = (u, v) => { const x = Math.min(cs - 1, Math.max(0, Math.floor(u * cs))), y = Math.min(cs - 1, Math.max(0, Math.floor(v * cs))), k = (y * cs + x) * 4; return [px[k], px[k + 1], px[k + 2]]; };
    const geo = mesh.geometry, { idx, wts } = weightsFor(geo.attributes.position, geo.attributes.uv, color, geo.index.array);
    geo.setAttribute("skinIndex", new THREE.Uint16BufferAttribute(idx, 4));
    geo.setAttribute("skinWeight", new THREE.Float32BufferAttribute(wts, 4));
    const mat = mesh.material; mat.roughness = 0.62; mat.envMapIntensity = 0.9;
    res({ geo, mat });
    }, rej);
    try { window.createImageBitmap = cib; } catch (_) { /* ok */ }
  }));
  return MODEL;
}

// ------------------------------------------------------------------ o personagem
export class Mascote3D {
  constructor(container, model, { mood = "idle", bleed = false } = {}) {
    this.mood = mood; this.bleed = bleed;
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

    // Esqueleto.
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

    this.t = 0; this.yaw = 0; this.yawV = 0; this.dragging = false;
    this.jump = 0; this.jumpV = 0; this.pokeT = -1;
    this.sp = { cap: { x: 0, v: 0 }, tasX: { x: 0, v: 0 }, tasZ: { x: 0, v: 0 }, rib: { x: 0, v: 0 } };
    this.prev = { lean: 0, bob: 0 }; this.cur = null;
    this.resize(container.clientWidth || 400, container.clientHeight || 400);
    this.update(1 / 60);
  }
  setMood(m) { this.mood = m; }
  poke() { if (this.jump <= 0.0001) { this.jumpV = 1.15; this.pokeT = 0; } }

  // Pose alvo (ângulos em radianos; positivo em z = anti-horário de frente).
  pose(t) {
    const m = this.mood, br = Math.sin(t * 2.0);
    const p = { bob: 0.004 * br, sy: 1 + 0.012 * br, sx: 1 - 0.005 * br, lean: 0.012 * Math.sin(t * 0.8), tilt: 0, twist: 0.04 * Math.sin(t * 0.5),
      armL: 0.06 * Math.sin(t * 1.7), foreL: 0.08 * Math.sin(t * 1.7 + 0.5), armR: -0.06 * Math.sin(t * 1.7 + 0.8), foreR: -0.08 * Math.sin(t * 1.7 + 1.2),
      armLx: 0, armRx: 0, armLup: 0, armRup: 0, liftL: 0, liftR: 0, pitchL: 0, pitchR: 0 };
    if (m === "happy") {
      const ph = t * 5.6, s = Math.sin(ph), up = Math.abs(s);
      p.bob = 0.035 * up; p.sy = 1 + 0.03 * up - 0.02; p.sx = 1 - 0.015 * up + 0.01; p.lean = 0.07 * Math.sin(ph * 0.5); p.twist = 0.12 * Math.sin(ph * 0.5);
      p.armLup = 0.05 * up; p.armRup = 0.05 * Math.abs(Math.sin(ph + 1.4)); p.armL = -0.25 - 0.25 * up; p.foreL = -0.35 * Math.abs(Math.sin(ph + 0.4)); p.armR = 0.25 + 0.25 * Math.abs(Math.sin(ph + 1.4)); p.foreR = 0.35 * Math.abs(Math.sin(ph + 1.8));
      p.liftL = Math.max(0, s) * 0.06; p.pitchL = -Math.max(0, s) * 0.45; p.liftR = Math.max(0, -s) * 0.06; p.pitchR = -Math.max(0, -s) * 0.45;
    } else if (m === "wave") {
      const w = Math.sin(t * 8);
      p.armR = 0.35; p.armRup = 0.08; p.foreR = 0.1 + 0.6 * w; p.lean = -0.05; p.twist = -0.1;
    } else if (m === "talking") {
      const g = Math.sin(t * 3.2);
      p.armR = -0.3 + 0.18 * g; p.foreR = 0.25 + 0.2 * g; p.armRx = 0.45; p.bob = 0.008 * Math.abs(Math.sin(t * 6)); p.twist = 0.08 * Math.sin(t * 1.3); p.lean = 0.02 * g;
    } else if (m === "thinking") {
      p.armR = 0.35; p.foreR = 0.9; p.armRx = 0.9; p.lean = 0.06; p.tilt = 0.05; p.twist = 0.14; p.armL = 0.1;
    } else if (m === "point") {
      p.armR = -0.85; p.foreR = 0; p.armRx = 0.3; p.twist = -0.18; p.lean = -0.03;
    } else if (m === "wink") {
      p.armR = 0.3; p.foreR = 0.4; p.lean = -0.05; p.twist = 0.1;
    } else if (m === "sad") {
      p.bob = -0.02; p.sy = 0.97; p.tilt = 0.12; p.lean = 0.02;
      p.armL = 1.95; p.foreL = 0; p.armR = -2.05; p.foreR = 0; p.armLx = 0.2; p.armRx = 0.2; p.twist = 0;
    }
    return p;
  }

  update(dt) {
    const t = (this.t += dt), p = this.pose(t);
    // Pulo do toque.
    if (this.jumpV > 0 || this.jump > 0) { this.jumpV -= 5.5 * dt; this.jump += this.jumpV * dt; if (this.jump <= 0) { this.jump = 0; this.jumpV = 0; this.pokeT = 0.0001; } }
    if (this.pokeT >= 0 && this.jump === 0) { this.pokeT += dt; const k = Math.exp(-this.pokeT * 8) * Math.sin(this.pokeT * 24); p.sy -= 0.07 * k; p.sx += 0.045 * k; if (this.pokeT > 0.9) this.pokeT = -1; }
    // Suaviza tudo (troca de humor sem tranco).
    if (!this.cur) this.cur = { ...p };
    const c = this.cur, k = 1 - Math.exp(-dt * 9), kf = 1 - Math.exp(-dt * 20);
    for (const key of Object.keys(p)) c[key] = lerp(c[key], p[key], key === "bob" || key === "sy" || key === "sx" || key.startsWith("lift") || key.startsWith("pitch") ? kf : k);

    const B = this.bones, R = this.rest;
    // Corpo: sobe/desce, amassa e inclina em volta da base do livro; o pulo leva o corpo todo.
    B.body.position.copy(R.body).add(V3(0, c.bob + this.jump, 0));
    B.body.rotation.set(c.tilt, c.twist, c.lean);
    B.body.scale.set(c.sx, c.sy, 1 + (1 - c.sy) * 0.5);
    // Pés: no chão (ou levantando na dança); no pulo vão junto.
    B.footL.position.copy(R.footL).add(V3(0, c.liftL + this.jump, c.liftL * 0.5)); B.footL.rotation.set(c.pitchL, 0, 0);
    B.footR.position.copy(R.footR).add(V3(0, c.liftR + this.jump, c.liftR * 0.5)); B.footR.rotation.set(c.pitchR, 0, 0);
    // Braços.
    B.armL.position.copy(R.armL).add(V3(0, c.armLup, 0)); B.armR.position.copy(R.armR).add(V3(0, c.armRup, 0));
    B.armL.rotation.set(c.armLx, 0, c.armL); B.foreL.rotation.set(0, 0, c.foreL);
    B.armR.rotation.set(c.armRx, 0, c.armR); B.foreR.rotation.set(0, 0, c.foreR);
    // Capelo, borla e fita atrasam (molas em passos curtos: estáveis com qualquer taxa de quadros).
    const dLean = (c.lean - this.prev.lean) / Math.max(dt, 1e-3), dBob = (c.bob + this.jump - this.prev.bob) / Math.max(dt, 1e-3);
    this.prev.lean = c.lean; this.prev.bob = c.bob + this.jump;
    const S = this.sp, kick = Math.min(1, dt * 60);
    const sp = (s, tgt, kk, dd) => { const n = Math.ceil(dt / (1 / 120)), h = dt / n; for (let i = 0; i < n; i++) { s.v += ((tgt - s.x) * kk - s.v * dd) * h; s.x += s.v * h; } return s.x; };
    S.cap.v += -dLean * 0.25 * kick; S.tasZ.v += (-dLean * 2.2 - dBob * 0.6) * kick; S.tasX.v += dBob * 0.9 * kick; S.rib.v += (-dLean * 1.5 + dBob * 1.2) * kick;
    B.cap.rotation.z = sp(S.cap, 0, 160, 10);
    B.tassel.rotation.set(sp(S.tasX, 0.04 * Math.sin(t * 0.9), 45, 2.6), 0, sp(S.tasZ, -c.lean * 0.8 + 0.05 * Math.sin(t * 1.3), 45, 2.6));
    B.ribbon.rotation.set(sp(S.rib, 0.06 * Math.sin(t * 1.6), 50, 3), 0, -c.lean * 0.6);
    // Giro (arrastar) volta sozinho para a frente.
    if (!this.dragging) { this.yawV += (-this.yaw * 25 - this.yawV * 7) * dt; this.yaw += this.yawV * dt; }
    this.rig.rotation.y = this.yaw;
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
      const fallback = () => { const f = document.createElement("estudario-mascote"); f.setAttribute("mood", this.getAttribute("mood") || "idle"); if (size) f.setAttribute("size", String(size)); this.appendChild(f); this._fallback = f; };
      if (!webglAvailable() || live.size >= MAX_LIVE) { fallback(); return; }
      let host = this;
      if (size) { host = document.createElement("div"); host.style.cssText = "position:absolute;left:-20%;top:-20%;width:140%;height:140%;pointer-events:none"; this.appendChild(host); }
      let model; try { model = await loadModel(); } catch { fallback(); return; }
      this.m = new Mascote3D(host, model, { mood: this.getAttribute("mood") || "idle", bleed: !!size });
      this.m.resize(host.clientWidth || 1, host.clientHeight || 1);
      this._ro = new ResizeObserver(() => this.m.resize(host.clientWidth || 1, host.clientHeight || 1)); this._ro.observe(this);
      this._io = new IntersectionObserver((es) => { for (const e of es) this._visible = e.isIntersecting && !document.hidden; }); this._io.observe(this);
      this._visible = true;
      let down = null;
      this.addEventListener("pointerdown", (e) => { down = { x: e.clientX, moved: 0 }; if (this.hasAttribute("spin")) { this.m.dragging = true; try { this.setPointerCapture(e.pointerId); } catch (_) { /* ok */ } } });
      this.addEventListener("pointermove", (e) => { if (!down || !this.m.dragging) return; const dx = e.clientX - down.x; down.moved += Math.abs(dx); down.x = e.clientX; this.m.yaw += dx * 0.012; this.m.yawV = dx * 0.6; });
      this.addEventListener("pointerup", () => { if (down && down.moved < 6) { this.m.poke(); this.dispatchEvent(new CustomEvent("folha-poke", { bubbles: true })); } down = null; this.m.dragging = false; });
      live.add(this); if (!raf) raf = requestAnimationFrame(loop);
    }
    disconnectedCallback() { live.delete(this); }
    attributeChangedCallback(n, _, v) { if (n !== "mood") return; if (this.m) this.m.setMood(v || "idle"); if (this._fallback) this._fallback.setAttribute("mood", v || "idle"); }
  });
}
if (typeof window !== "undefined") window.EstudarioMascote3D = { Mascote3D, webglAvailable };
