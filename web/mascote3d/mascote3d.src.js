// O Folha em 3D, modelado em cima da arte oficial (design/mascote/folha-turnaround.webp: frente, 3/4,
// lado e costas). Medidas tiradas da arte (1 unidade = 100 px da vista de frente). O rosto e as linhas
// da página são os pixels da própria arte, aplicados na folha de cima como textura.
// Gera web/src/jsMain/resources/mascote3d.js com `node scripts/web/build-mascote3d.mjs`.
//
// Uso: <estudario-mascote3d mood="idle|happy|wave|talking|thinking|sad|point|wink" size="160" [spin]>
import * as THREE from "three";
import { RoomEnvironment } from "three/addons/environments/RoomEnvironment.js";
import { RoundedBoxGeometry } from "three/addons/geometries/RoundedBoxGeometry.js";

const V3 = (x = 0, y = 0, z = 0) => new THREE.Vector3(x, y, z);
const clamp = (v, a, b) => Math.max(a, Math.min(b, v));
const lerp = (a, b, t) => a + (b - a) * t;

// ------------------------------------------------------------------ medidas da arte
const GROUND = -3.56;
const PAGE = { top: 1.23, bot: -1.22, wl: 1.91, wr: 1.84, front: 1.05 };   // folha de cima (rosto)
const LAYERS = 10, LAYER_D = 0.18;
const SHOE_S = 1.22;                                                         // tênis grandes, como na arte                                           // miolo: 10 folhas grossas
const COVER = { w: 2.22, h: 3.12, d: 0.3, y: -0.28, z: -0.92 };
const GUTTER = (x) => -0.22 * Math.exp(-((x / 0.5) ** 2));                   // a costura afunda

// ------------------------------------------------------------------ texturas
const base = (() => { try { const s = document.currentScript && document.currentScript.src; return s ? s.replace(/[^/]*$/, "") : ""; } catch { return ""; } })();
function canvas(w, h) { const c = document.createElement("canvas"); c.width = w; c.height = h; return [c, c.getContext("2d")]; }
function rng(seed) { let s = seed; return () => (s = (s * 1103515245 + 12345) & 0x7fffffff) / 0x7fffffff; }
const loadImg = (src) => new Promise((res) => { const i = new Image(); i.onload = () => res(i); i.onerror = () => res(null); i.src = src; });

let A = null;
async function assets() {
  if (A) return A;
  // Página dupla desenhada em alta resolução, nos lugares da arte (coordenadas do recorte, px):
  // papel creme, vinco da costura, as linhas de texto e as bochechas. Olhos e boca são 3D.
  const X0 = 120, Y0 = 122, X1 = 506, Y1 = 374, SC = 2048 / (X1 - X0);
  const [pc, pg] = canvas(2048, Math.round((Y1 - Y0) * SC));
  pg.fillStyle = "#FBF6EE"; pg.fillRect(0, 0, pc.width, pc.height);
  pg.save(); pg.scale(SC, SC); pg.translate(-X0, -Y0);
  { const r0 = rng(3); for (let i = 0; i < 9000; i++) { pg.fillStyle = "rgba(150,130,100," + (0.02 + r0() * 0.03) + ")"; pg.fillRect(X0 + r0() * (X1 - X0), Y0 + r0() * (Y1 - Y0), 0.3 + r0() * 0.6, 0.25); } }
  const gutter = pg.createLinearGradient(296, 0, 336, 0);
  gutter.addColorStop(0, "rgba(120,100,150,0)"); gutter.addColorStop(0.42, "rgba(120,100,150,.16)"); gutter.addColorStop(0.5, "rgba(110,90,140,.28)"); gutter.addColorStop(0.58, "rgba(120,100,150,.16)"); gutter.addColorStop(1, "rgba(120,100,150,0)");
  pg.fillStyle = gutter; pg.fillRect(296, Y0, 40, Y1 - Y0);
  pg.strokeStyle = "rgba(196,188,216,.9)"; pg.lineCap = "round"; pg.lineWidth = 6.5;
  const stroke = (pts) => { pg.beginPath(); pg.moveTo(pts[0], pts[1]); pg.quadraticCurveTo(pts[2], pts[3], pts[4], pts[5]); pg.stroke(); };
  stroke([166, 162, 222, 158, 280, 166]); stroke([166, 186, 226, 181, 286, 191]); pg.lineWidth = 6; stroke([166, 206, 176, 205, 188, 203]);
  pg.lineWidth = 6.5; stroke([346, 182, 404, 170, 462, 168]); stroke([340, 200, 404, 186, 470, 185]); pg.lineWidth = 6; stroke([446, 210, 458, 211, 470, 212]);
  for (const [cx, cy] of [[192, 294], [436, 296]]) {
    pg.save(); pg.translate(cx, cy); pg.scale(1, 0.6);
    const g = pg.createRadialGradient(0, 0, 0, 0, 0, 28); g.addColorStop(0, "rgba(246,150,172,.95)"); g.addColorStop(0.72, "rgba(247,160,180,.85)"); g.addColorStop(1, "rgba(248,170,188,0)");
    pg.fillStyle = g; pg.beginPath(); pg.arc(0, 0, 28, 0, Math.PI * 2); pg.fill(); pg.restore();
  }
  pg.restore();
  const pages = new THREE.CanvasTexture(pc); pages.colorSpace = THREE.SRGBColorSpace; pages.anisotropy = 8;
  const half = (u0, u1) => { const t = pages.clone(); t.repeat.set(u1 - u0, 1); t.offset.set(u0, 0); t.needsUpdate = true; return t; };
  const gut = (316 - X0) / (X1 - X0);
  // Tecido (relevo) e papel.
  const noise = (seed, n, rep) => {
    const [c, g] = canvas(256, 256), r = rng(seed); g.fillStyle = "#808080"; g.fillRect(0, 0, 256, 256);
    for (let i = 0; i < n; i++) { const k = 100 + r() * 60 | 0; g.fillStyle = `rgb(${k},${k},${k})`; g.fillRect(r() * 256, r() * 256, 1 + r() * 2, 1 + r() * 2); }
    const t = new THREE.CanvasTexture(c); t.wrapS = t.wrapT = THREE.RepeatWrapping; t.repeat.set(rep, rep); return t;
  };
  const fabric = noise(5, 9000, 5), fine = noise(9, 6000, 3);
  const phys = (o) => new THREE.MeshPhysicalMaterial(o);
  A = {
    pageL: phys({ map: half(0, gut), roughness: 0.82, sheen: 0.3, sheenColor: new THREE.Color("#fff4e2") }),
    pageR: phys({ map: half(gut, 1), roughness: 0.82, sheen: 0.3, sheenColor: new THREE.Color("#fff4e2") }),
    paper: phys({ color: "#F8F1E3", roughness: 0.85, bumpMap: fine, bumpScale: 0.3 }),
    paperDark: phys({ color: "#EDE3D0", roughness: 0.88 }),
    cover: phys({ color: "#1E1A8C", roughness: 0.72, sheen: 0.7, sheenColor: new THREE.Color("#6F67FF"), sheenRoughness: 0.5, bumpMap: fabric, bumpScale: 0.9 }),
    cap: phys({ color: "#1C1884", side: THREE.DoubleSide, roughness: 0.8, sheen: 0.8, sheenColor: new THREE.Color("#6A62F0"), sheenRoughness: 0.45, bumpMap: fabric, bumpScale: 1.1 }),
    limb: phys({ color: "#1D2CB4", roughness: 0.42, clearcoat: 0.35, clearcoatRoughness: 0.35, sheen: 0.3, sheenColor: new THREE.Color("#8C96FF") }),
    white: phys({ color: "#F7F7FB", roughness: 0.55, sheen: 0.6, sheenColor: new THREE.Color("#ffffff"), clearcoat: 0.15 }),
    sole: phys({ color: "#F1F0F6", roughness: 0.65 }),
    shoeBlue: phys({ color: "#1F28A8", roughness: 0.6, sheen: 0.7, sheenColor: new THREE.Color("#7E86FF"), bumpMap: fine, bumpScale: 0.5 }),
    ink: phys({ color: "#1B1650", roughness: 0.16, clearcoat: 1, clearcoatRoughness: 0.06 }),
    mouthIn: phys({ color: "#2A1238", roughness: 0.5 }),
    tongue: phys({ color: "#F27C9A", roughness: 0.4, clearcoat: 0.5 }),
    shine: new THREE.MeshBasicMaterial({ color: "#ffffff" }),
    gold: phys({ color: "#F4B236", metalness: 0.55, roughness: 0.28, clearcoat: 0.6 }),
    teal: phys({ color: "#36C6BA", roughness: 0.38, sheen: 0.6, sheenColor: new THREE.Color("#C8FFF6"), side: THREE.DoubleSide }),
  };
  return A;
}

// ------------------------------------------------------------------ geometria
// Entorta uma geometria pela costura (z += f(x)), ajustando as normais na mão (sem costura visível).
function bendGutter(geo, ox, amount = 1) {
  const P = geo.attributes.position, N = geo.attributes.normal, e = 1e-3;
  for (let i = 0; i < P.count; i++) {
    const x = P.getX(i) + ox, f = GUTTER(x) * amount, fx = (GUTTER(x + e) - GUTTER(x - e)) / (2 * e) * amount;
    P.setZ(i, P.getZ(i) + f);
    const n = V3(N.getX(i) - fx * N.getZ(i), N.getY(i), N.getZ(i)).normalize(); N.setXYZ(i, n.x, n.y, n.z);
  }
  P.needsUpdate = N.needsUpdate = true; geo.computeBoundingSphere();
  return geo;
}
// Caixa arredondada já posicionada no mundo (para poder entortar pela costura).
function slab(w, h, d, r, cx, cy, cz, bend = 1) {
  const g = new RoundedBoxGeometry(w, h, d, 5, r);
  g.translate(cx, cy, cz);
  return bendGutter(g, 0, bend);
}
const shade = (o) => { o.traverse((m) => { if (m.isMesh) { m.castShadow = true; m.receiveShadow = true; } }); return o; };

// Tubo de raio constante por uma curva (braços e pernas), refeito quando a pose muda.
class Tube {
  constructor(mat, radius, segs = 32, radial = 20) { this.r = radius; this.segs = segs; this.radial = radial; this.mesh = shade(new THREE.Mesh(new THREE.BufferGeometry(), mat)); this.capA = shade(new THREE.Mesh(new THREE.SphereGeometry(radius, 20, 14), mat)); this.capB = shade(new THREE.Mesh(new THREE.SphereGeometry(radius, 20, 14), mat)); }
  set(points, rEnd) {
    const curve = new THREE.CatmullRomCurve3(points, false, "centripetal");
    const geo = new THREE.TubeGeometry(curve, this.segs, this.r, this.radial, false);
    if (rEnd) { // afina até o fim
      const P = geo.attributes.position, c = V3(), p = V3();
      for (let i = 0; i <= this.segs; i++) { const t = i / this.segs, k = lerp(1, rEnd / this.r, t); curve.getPointAt(t, c); for (let j = 0; j <= this.radial; j++) { const idx = i * (this.radial + 1) + j; p.fromBufferAttribute(P, idx).sub(c).multiplyScalar(k).add(c); P.setXYZ(idx, p.x, p.y, p.z); } }
    }
    this.mesh.geometry.dispose(); this.mesh.geometry = geo;
    this.capA.position.copy(points[0]); this.capB.position.copy(points[points.length - 1]);
    if (rEnd) this.capB.scale.setScalar(rEnd / this.r);
    return curve;
  }
  add(parent) { parent.add(this.mesh, this.capA, this.capB); }
}

// Punho de luva: palma redonda, dedos dobrados por cima, polegar de lado. Dedos para +Y, frente +Z.
function makeFist(a, s) {
  const g = new THREE.Group();
  const palm = new THREE.Mesh(new THREE.SphereGeometry(1, 32, 24), a.white); palm.scale.set(0.3, 0.27, 0.26); palm.position.y = 0.16;
  g.add(palm);
  for (let i = 0; i < 4; i++) {   // nós dos dedos dobrados, de um lado ao outro
    const k = new THREE.Mesh(new THREE.CapsuleGeometry(0.085, 0.07, 8, 16), a.white);
    k.rotation.x = Math.PI / 2; k.position.set(-0.19 + i * 0.125, 0.33 - Math.abs(i - 1.5) * 0.02, 0.12); g.add(k);
  }
  const thumb = new THREE.Mesh(new THREE.CapsuleGeometry(0.08, 0.12, 8, 16), a.white);
  thumb.position.set(-s * 0.26, 0.15, 0.13); thumb.rotation.set(0.3, 0, -s * 0.9); g.add(thumb);
  const cuff = new THREE.Mesh(new THREE.CylinderGeometry(0.2, 0.23, 0.12, 28), a.white); cuff.position.y = -0.04; g.add(cuff);
  g.scale.setScalar(1.42);
  return shade(g);
}

// Tênis de cano alto, como na arte: solado branco alto de borda arredondada, biqueira branca em cúpula,
// cabedal azul quadradão, gola branca acolchoada, tira dourada sobre o peito do pé, dois filetes brancos
// e o botão dourado na lateral de fora. Origem no tornozelo; bico para +Z.
function makeShoe(a, s) {
  const g = new THREE.Group();
  const sole = new THREE.Mesh(new RoundedBoxGeometry(0.96, 0.28, 1.36, 6, 0.13), a.sole); sole.position.set(0, -1.0, 0.1);
  const toe = new THREE.Mesh(new THREE.SphereGeometry(1, 40, 24, 0, Math.PI * 2, 0, Math.PI / 2), a.white); toe.scale.set(0.44, 0.33, 0.4); toe.position.set(0, -0.86, 0.36);
  const upper = new THREE.Mesh(new RoundedBoxGeometry(0.9, 0.66, 0.96, 6, 0.24), a.shoeBlue); upper.position.set(0, -0.56, -0.12);
  const lathe = (pts) => new THREE.LatheGeometry(pts.map(([x, y]) => V3(x, y)), 48);
  const collar = new THREE.Mesh(lathe([[0.18, -0.3], [0.36, -0.3], [0.4, -0.24], [0.41, -0.06], [0.38, 0.0], [0.27, 0.03], [0.16, 0.02]]), a.white); collar.position.set(0, -0.08, -0.12);
  const strap = new THREE.Mesh(new RoundedBoxGeometry(0.66, 0.1, 0.24, 4, 0.045), a.gold); strap.position.set(0, -0.32, 0.27); strap.rotation.set(0.62, s * 0.08, 0);
  const line = (y, z) => { const m = new THREE.Mesh(new THREE.CapsuleGeometry(0.022, 0.5, 6, 12), a.white); m.rotation.z = Math.PI / 2; m.rotation.x = 0.7; m.position.set(0, y, z); return m; };
  const button = new THREE.Mesh(new THREE.SphereGeometry(0.13, 24, 16), a.gold); button.scale.set(0.42, 1, 1); button.position.set(s * 0.46, -0.6, -0.08);
  g.add(sole, toe, upper, collar, strap, line(-0.45, 0.43), line(-0.52, 0.5), button);
  for (const c of g.children) c.position.z += 0.12;
  g.scale.setScalar(SHOE_S);
  return shade(g);
}

// ------------------------------------------------------------------ o personagem
export class Mascote3D {
  constructor(container, a, { mood = "idle", bleed = false } = {}) {
    this.a = a; this.mood = mood; this.bleed = bleed;
    const r = this.renderer = new THREE.WebGLRenderer({ antialias: true, alpha: true, powerPreference: "low-power" });
    r.setPixelRatio(Math.min(window.devicePixelRatio || 1, 2));
    r.toneMapping = THREE.NeutralToneMapping; r.toneMappingExposure = 1.0;
    r.shadowMap.enabled = true; r.shadowMap.type = THREE.VSMShadowMap;
    r.domElement.style.cssText = "display:block;width:100%;height:100%";
    container.appendChild(r.domElement);
    const scene = this.scene = new THREE.Scene();
    const pm = new THREE.PMREMGenerator(r); scene.environment = pm.fromScene(new RoomEnvironment(), 0.04).texture; pm.dispose();
    scene.environmentIntensity = 0.55;
    this.camera = new THREE.PerspectiveCamera(20, 1, 0.5, 100);
    const key = new THREE.DirectionalLight("#fff6ea", 2.2); key.position.set(-1.6, 12, 6.5);   // sombra cai embaixo dele, não sai da caixa key.castShadow = true;
    key.shadow.mapSize.set(2048, 2048); Object.assign(key.shadow.camera, { left: -5, right: 5, top: 5, bottom: -5, near: 1, far: 40 });
    key.shadow.radius = 8; key.shadow.blurSamples = 16; key.shadow.bias = -0.0005;
    scene.add(key);
    const rim = new THREE.DirectionalLight("#b3a8ff", 1.5); rim.position.set(6, 4, -8); scene.add(rim);
    scene.add(new THREE.HemisphereLight("#ffffff", "#9d97c7", 0.55));
    const ground = new THREE.Mesh(new THREE.PlaneGeometry(40, 40), new THREE.ShadowMaterial({ opacity: 0.16, color: "#221c58" }));
    ground.rotation.x = -Math.PI / 2; ground.position.y = GROUND; ground.receiveShadow = true; scene.add(ground);

    const rig = this.rig = new THREE.Group(); scene.add(rig);
    const body = this.body = new THREE.Group(); rig.add(body);         // livro e tudo preso nele (sobe/desce, inclina)

    // ---- livro: capa acolchoada, lombada, miolo em camadas e a folha do rosto
    for (const s of [-1, 1]) {
      body.add(shade(new THREE.Mesh(slab(COVER.w, COVER.h, COVER.d, 0.14, s * COVER.w / 2, COVER.y, COVER.z, 0.5), a.cover)));
      const w0 = s < 0 ? PAGE.wl : PAGE.wr, h0 = PAGE.top - PAGE.bot, y0 = (PAGE.top + PAGE.bot) / 2;
      for (let i = 0; i < LAYERS; i++) {
        const w = w0 + 0.022 * i + 0.02, h = h0 + 0.05 * i, y = y0 - 0.025 * i, z = PAGE.front - LAYER_D / 2 - i * LAYER_D;
        const geo = slab(w, h, LAYER_D, 0.085, s * (w / 2 - 0.02), y, z, 1 - i * 0.06);
        const mats = i === 0 ? [a.paper, a.paper, a.paper, a.paper, s < 0 ? a.pageL : a.pageR, a.paper] : i % 2 ? a.paperDark : a.paper;
        body.add(shade(new THREE.Mesh(geo, mats)));
      }
    }
    { const sp = shade(new THREE.Mesh(new THREE.CapsuleGeometry(0.3, COVER.h - 0.6, 10, 24), a.cover)); sp.position.set(0, COVER.y, COVER.z - 0.06); sp.scale.z = 0.8; body.add(sp); }

    // ---- rosto: tudo sobre a folha de cima (z da folha + vinco da costura)
    {
      const ax = (xc) => (xc - 316) / 98.5, ay = (yc) => PAGE.top - (yc - 122) * (PAGE.top - PAGE.bot) / 252;
      const onPage = (x, y, lift = 0.012) => V3(x, y, PAGE.front + GUTTER(x) + lift);
      const face = this.face = new THREE.Group(); body.add(face);
      const arc = (x0, x1, yb, ytop) => {
        const pts = []; for (let i = 0; i <= 24; i++) { const t = i / 24, x = lerp(x0, x1, t), y = yb + (ytop - yb) * Math.sin(Math.PI * t) ** 0.85; pts.push(onPage(x, y, 0.03)); }
        const g = new THREE.Group(), m = new THREE.Mesh(new THREE.TubeGeometry(new THREE.CatmullRomCurve3(pts), 48, 0.05, 14), a.ink);
        for (const p of [pts[0], pts[24]]) { const c = new THREE.Mesh(new THREE.SphereGeometry(0.05, 16, 12), a.ink); c.position.copy(p); g.add(c); }
        g.add(m); return g;
      };
      this.arcs = { L: arc(ax(193), ax(262), ay(265), ay(229)), R: arc(ax(366), ax(436), ay(265), ay(229)) };
      const eyeOpen = (cx) => {
        const g = new THREE.Group(); g.position.copy(onPage(cx, ay(248), 0.02));
        const geo = new THREE.SphereGeometry(1, 40, 24, 0, Math.PI * 2, 0, Math.PI / 2); geo.rotateX(Math.PI / 2);
        const ball = new THREE.Mesh(geo, a.ink); ball.scale.set(0.17, 0.23, 0.07);
        const h1 = new THREE.Mesh(new THREE.SphereGeometry(1, 16, 12), a.shine); h1.scale.set(0.055, 0.07, 0.02); h1.position.set(-0.05, 0.08, 0.07);
        const h2 = new THREE.Mesh(new THREE.SphereGeometry(1, 12, 8), a.shine); h2.scale.set(0.025, 0.025, 0.01); h2.position.set(0.06, -0.08, 0.065);
        g.add(ball, h1, h2); g.userData = { ball, h1, h2 }; return g;
      };
      this.eyes = { L: eyeOpen(ax(227)), R: eyeOpen(ax(400)) };
      for (const k of ["L", "R"]) face.add(shade(this.arcs[k]), shade(this.eyes[k]));
      const mouthOpen = new THREE.Group();
      { const sh = new THREE.Shape(), w = 0.3, h = 0.4; sh.moveTo(-w, 0); sh.lineTo(w, 0); sh.bezierCurveTo(w, -h * 0.6, w * 0.55, -h, 0, -h); sh.bezierCurveTo(-w * 0.55, -h, -w, -h * 0.6, -w, 0);
        const m = new THREE.Mesh(new THREE.ExtrudeGeometry(sh, { depth: 0.02, bevelEnabled: true, bevelThickness: 0.015, bevelSize: 0.02, bevelSegments: 4, curveSegments: 24 }), a.mouthIn);
        const tg = new THREE.Mesh(new THREE.SphereGeometry(1, 28, 16), a.tongue); tg.scale.set(0.17, 0.12, 0.03); tg.position.set(0, -0.29, 0.03);
        mouthOpen.add(m, tg); }
      mouthOpen.position.copy(onPage(ax(313), ay(281), 0.005));
      const tubeMouth = (pts) => { const g = new THREE.Group(); const p3 = pts.map(([x, y]) => onPage(ax(x), ay(y), 0.03)); g.add(new THREE.Mesh(new THREE.TubeGeometry(new THREE.CatmullRomCurve3(p3), 32, 0.04, 12), a.ink)); for (const p of [p3[0], p3[p3.length - 1]]) { const c = new THREE.Mesh(new THREE.SphereGeometry(0.04, 12, 8), a.ink); c.position.copy(p); g.add(c); } return g; };
      this.mouths = { open: mouthOpen, smile: tubeMouth([[290, 292], [313, 306], [336, 292]]), frown: tubeMouth([[292, 305], [313, 294], [334, 305]]), hmm: tubeMouth([[294, 300], [306, 303], [320, 297], [332, 299]]) };
      for (const m of Object.values(this.mouths)) face.add(shade(m));
      this.blinkAt = 2; this.blinkT = -1;
    }

    // ---- fita marcadora
    {
      const sh = new THREE.Shape(), w = 0.36, L = 0.86;
      sh.moveTo(-w / 2, 0); sh.lineTo(w / 2, 0); sh.lineTo(w / 2, -L); sh.lineTo(0, -L + 0.17); sh.lineTo(-w / 2, -L); sh.closePath();
      const geo = new THREE.ShapeGeometry(sh, 24); const P = geo.attributes.position;
      for (let i = 0; i < P.count; i++) P.setZ(i, 0.05 * Math.sin(P.getX(i) * 4) + 0.12 * P.getY(i) * P.getY(i));
      geo.computeVertexNormals();
      this.ribbon = shade(new THREE.Mesh(geo, a.teal)); this.ribbon.position.set(0.02, -1.32, 0.86); body.add(this.ribbon);
    }

    // ---- capelo: casquete de pé em cima do livro; a tábua gira como losango e inclina para quem olha
    // (como na arte); cordão e borla presos na tábua, com a borla sempre caindo na vertical.
    {
      const cap = this.cap = new THREE.Group();
      const prof = [V3(0.0, -0.04), V3(0.8, -0.04), V3(0.8, 0.12), V3(0.78, 0.3), V3(0.75, 0.42), V3(0.0, 0.42)];
      const skull = new THREE.Mesh(new THREE.LatheGeometry(prof, 72), a.cap); skull.scale.z = 0.9;
      const band = new THREE.Mesh(new THREE.TorusGeometry(0.8, 0.05, 10, 72), a.cap); band.rotation.x = Math.PI / 2; band.position.y = 0.0; band.scale.y = 0.9;
      const TILT = 0.21, SPIN = 0.36;
      const board = this.board = new THREE.Group(); board.position.set(0, 0.47, 0.05); board.rotation.set(TILT, SPIN, 0, "XYZ");
      const plate = new THREE.Mesh(new RoundedBoxGeometry(2.3, 0.11, 2.3, 4, 0.04), a.cap);
      const button = new THREE.Mesh(new THREE.SphereGeometry(0.11, 24, 16), a.gold); button.position.y = 0.09; button.scale.y = 0.75;
      board.add(plate, button);
      const corner = V3(1.13, 0.07, 1.13);   // depois do giro da tábua, este canto fica à direita
      const cord = new THREE.Mesh(new THREE.TubeGeometry(new THREE.CatmullRomCurve3([V3(0, 0.1, 0), corner.clone().multiplyScalar(0.5).setY(0.1), corner.clone().setY(0.08)]), 24, 0.035, 8), a.gold);
      board.add(cord);
      const tas = this.tassel = new THREE.Group(); tas.position.copy(corner).setY(0.06);
      const inner = this.tasselInner = new THREE.Group(); tas.add(inner);
      const str = new THREE.Mesh(new THREE.CylinderGeometry(0.032, 0.032, 0.3, 8), a.gold); str.position.y = -0.15;
      const knot = new THREE.Mesh(new THREE.SphereGeometry(0.09, 18, 12), a.gold); knot.position.y = -0.33;
      const head = new THREE.Mesh(new THREE.CylinderGeometry(0.08, 0.13, 0.14, 20), a.gold); head.position.y = -0.45;
      inner.add(str, knot, head);
      for (let i = 0; i < 20; i++) { const an = i / 20 * Math.PI * 2, fr = new THREE.Mesh(new THREE.CylinderGeometry(0.036, 0.03, 0.5, 8), a.gold); fr.position.set(Math.cos(an) * 0.1, -0.76, Math.sin(an) * 0.1); fr.rotation.set(Math.sin(an) * 0.12, 0, -Math.cos(an) * 0.12); inner.add(fr); }
      board.add(tas);
      cap.add(skull, band, board);
      cap.position.set(0, COVER.y + COVER.h / 2 - 0.02, 0.3); cap.rotation.set(0.06, 0, -0.04);
      body.add(shade(cap));
    }

    // ---- braços (ombro na lateral do livro, cotovelo, punho com pulseira dourada e luva)
    this.arms = {};
    for (const s of [-1, 1]) {
      const tube = new Tube(a.limb, 0.19); tube.add(body);
      const bracelet = shade(new THREE.Mesh(new THREE.TorusGeometry(0.205, 0.065, 16, 40), a.gold));
      const fist = makeFist(a, s);
      body.add(bracelet, fist);
      this.arms[s] = { s, tube, bracelet, fist };
    }
    // ---- pernas e tênis (fora do corpo: os pés ficam no chão)
    this.legs = {};
    for (const s of [-1, 1]) {
      const tube = new Tube(a.limb, 0.17); tube.add(rig);
      const shoe = makeShoe(a, s); rig.add(shoe);
      this.legs[s] = { s, tube, shoe };
    }

    this.legRest = {}; this.t = 0; this.yaw = 0; this.yawV = 0; this.dragging = false; this.look = V3();
    this.springs = { cap: { x: 0, v: 0 }, tas: { x: 0, v: 0 }, tasZ: { x: 0, v: 0 }, rib: { x: 0, v: 0 } };
    this.jump = 0; this.jumpV = 0; this.pokeT = -1; this.prev = { bob: 0, rot: 0 };
    this.cur = null;
    this.resize(container.clientWidth || 400, container.clientHeight || 400);
    this.update(0.016);
  }

  // ---- pose: pontos-chave em unidades da arte. A pose de repouso é a da arte (punhos para cima).
  rest() {
    return {
      bob: 0, lean: 0, twist: 0, sx: 1, sy: 1,
      armL: { e: V3(-2.45, -0.55, -0.1), w: V3(-2.62, -0.12, 0.0), f: V3(-2.7, 0.45, 0.05), roll: 0.25 },
      armR: { e: V3(2.36, -0.95, -0.1), w: V3(2.5, -0.55, 0.0), f: V3(2.55, 0.0, 0.05), roll: -0.2 },
      footL: { p: V3(-0.86, 0, 0.1), lift: 0, pitch: 0, yaw: -0.38 },
      footR: { p: V3(0.74, 0, 0.1), lift: 0, pitch: 0, yaw: 0.38 },
    };
  }
  pose(t) {
    const p = this.rest(), m = this.mood;
    const br = Math.sin(t * 2.0);
    p.sy = 1 + 0.01 * br; p.bob = 0.02 * br;
    // Braços respiram junto (o punho sobe um pouco).
    for (const k of ["armL", "armR"]) { const s = k === "armL" ? 1 : -1; p[k].f.y += 0.04 * Math.sin(t * 2 + s); p[k].w.y += 0.025 * Math.sin(t * 2 + s); }
    if (m === "happy") {
      const ph = t * 5.4, s = Math.sin(ph), up = Math.abs(Math.sin(ph));
      p.bob = 0.16 * up - 0.05; p.lean = 0.05 * s; p.twist = 0.08 * s;
      p.armL.f.add(V3(-0.1, 0.35 * up, 0.05)); p.armL.w.add(V3(-0.05, 0.25 * up, 0));
      p.armR.f.add(V3(0.1, 0.35 * Math.abs(Math.sin(ph + 1.3)) + 0.45, 0.05)); p.armR.w.add(V3(0.05, 0.4, 0)); p.armR.e.add(V3(0.05, 0.25, 0));
      p.footL.lift = Math.max(0, s) * 0.32; p.footL.pitch = -Math.max(0, s) * 0.35;
      p.footR.lift = Math.max(0, -s) * 0.32; p.footR.pitch = -Math.max(0, -s) * 0.35;
    } else if (m === "wave") {
      const w = Math.sin(t * 8.5);
      p.armR.e.set(2.45, -0.45, 0.0); p.armR.w.set(2.75 + 0.18 * w, 0.2, 0.2); p.armR.f.set(2.85 + 0.32 * w, 0.75, 0.25); p.armR.roll = 0.6 * w;
      p.lean = -0.04; p.twist = -0.06;
    } else if (m === "talking") {
      const g = Math.sin(t * 3.1);
      p.armR.e.set(2.35, -0.85, 0.2); p.armR.w.set(2.55, -0.55 + 0.12 * g, 0.55); p.armR.f.set(2.6, -0.15 + 0.15 * g, 0.85);
      p.bob += 0.03 * Math.abs(Math.sin(t * 6)); p.twist = 0.05 * Math.sin(t * 1.4);
    } else if (m === "thinking") {
      p.armR.e.set(2.2, -1.2, 0.35); p.armR.w.set(1.5, -1.05, 1.2); p.armR.f.set(1.0, -0.75, 1.45);
      p.lean = 0.06; p.twist = 0.1;
    } else if (m === "point") {
      p.armR.e.set(2.5, -0.75, 0.0); p.armR.w.set(2.95, -0.55, 0.2); p.armR.f.set(3.45, -0.45, 0.3); p.twist = -0.08;
    } else if (m === "wink") {
      p.armR.f.add(V3(0.05, 0.3, 0.1)); p.lean = -0.05;
    } else if (m === "sad") {
      p.bob = -0.12; p.lean = 0.03;
      p.armL.e.set(-2.3, -0.9, -0.1); p.armL.w.set(-2.4, -1.35, 0.05); p.armL.f.set(-2.42, -1.9, 0.1);
      p.armR.e.set(2.3, -1.1, -0.1); p.armR.w.set(2.4, -1.5, 0.05); p.armR.f.set(2.42, -2.0, 0.1);
    }
    if (this.jump > 0) p.bob += this.jump;
    return p;
  }

  setMood(m) { this.mood = m; }
  faceFor(m) {
    return {
      eyes: m === "happy" || m === "wave" ? "arc" : m === "wink" ? "wink" : "open",
      mouth: m === "thinking" ? "hmm" : m === "sad" ? "frown" : m === "point" ? "smile" : "open",
    };
  }
  poke() { if (this.jump <= 0.0001) { this.jumpV = 4.2; this.pokeT = 0; } }

  update(dt) {
    const t = (this.t += dt);
    const p = this.pose(t);
    // Pulo: sobe, cai e amassa no pouso.
    if (this.jumpV > 0 || this.jump > 0) { this.jumpV -= 16 * dt; this.jump += this.jumpV * dt; if (this.jump <= 0) { this.jump = 0; this.jumpV = 0; this.pokeT = 0.0001; } }
    if (this.pokeT >= 0 && this.jump === 0) { this.pokeT += dt; const k = Math.exp(-this.pokeT * 8) * Math.sin(this.pokeT * 24); p.sy -= 0.06 * k; p.sx += 0.04 * k; if (this.pokeT > 0.9) this.pokeT = -1; }
    // Suaviza a pose (nada pula de um quadro para o outro).
    const k = this.cur ? 1 - Math.exp(-dt * 10) : 1;
    if (!this.cur) this.cur = p;
    const c = this.cur;
    for (const f of ["bob", "lean", "twist", "sx", "sy"]) c[f] = lerp(c[f], p[f], f === "bob" || f === "sy" || f === "sx" ? 1 - Math.exp(-dt * 18) : k);
    for (const arm of ["armL", "armR"]) { for (const q of ["e", "w", "f"]) c[arm][q].lerp(p[arm][q], k); c[arm].roll = lerp(c[arm].roll, p[arm].roll, k); }
    for (const foot of ["footL", "footR"]) { c[foot].lift = lerp(c[foot].lift, p[foot].lift, 1 - Math.exp(-dt * 16)); c[foot].pitch = lerp(c[foot].pitch, p[foot].pitch, 1 - Math.exp(-dt * 16)); }

    // Corpo: pivô no pé do livro.
    const body = this.body;
    body.position.set(0, c.bob, 0);
    body.rotation.set(0, c.twist, c.lean);
    body.scale.set(c.sx, c.sy, 1);
    body.updateMatrixWorld(true);

    // Braços: ombro na lateral (vai com o corpo); cotovelo e punho da pose.
    for (const s of [-1, 1]) {
      const arm = this.arms[s], q = s < 0 ? c.armL : c.armR;
      const S = V3(s * (COVER.w - 0.06), s < 0 ? -0.5 : -0.95, -0.42);
      const curve = arm.tube.set([S, q.e.clone(), q.w.clone()]);
      const dir = q.f.clone().sub(q.w).normalize();
      arm.bracelet.position.copy(q.w); arm.bracelet.quaternion.setFromUnitVectors(V3(0, 0, 1), dir);
      arm.fist.position.copy(q.w).addScaledVector(dir, 0.05);
      const qa = new THREE.Quaternion().setFromUnitVectors(V3(0, 1, 0), dir);
      arm.fist.quaternion.copy(qa).multiply(new THREE.Quaternion().setFromAxisAngle(V3(0, 1, 0), q.roll + (s < 0 ? 0.35 : -0.35)));
      void curve;
    }
    // Pernas: quadril embaixo do livro (vai com o corpo), tornozelo no tênis (no chão, ou levantado).
    for (const s of [-1, 1]) {
      const leg = this.legs[s], f = s < 0 ? c.footL : c.footR, rest = (s < 0 ? this.rest().footL : this.rest().footR);
      const hip = this.body.localToWorld(V3(s < 0 ? -0.52 : 0.4, -1.55, 0.0));
      this.rig.worldToLocal(hip);
      const ankle = V3(rest.p.x, GROUND + 1.14 * SHOE_S + f.lift, rest.p.z + f.lift * 0.4);
      leg.shoe.position.copy(ankle); leg.shoe.rotation.set(f.pitch, rest.yaw, 0);
      // Joelho: com a perna mais curta que o normal, ele dobra para a frente e para fora.
      const L0 = this.legRest[s] || (this.legRest[s] = hip.distanceTo(ankle)), d = hip.distanceTo(ankle), bend = Math.sqrt(Math.max(0, L0 * L0 - d * d)) * 0.5;
      const knee = hip.clone().lerp(ankle, 0.5).add(V3(s * 0.15 * bend, 0, bend));
      leg.tube.set([hip, knee, ankle.clone().addScaledVector(ankle.clone().sub(knee).normalize(), 0.22)]);
    }

    // Rosto: olhos (arco da arte, ou abertos piscando e olhando), boca do humor (a aberta fala).
    {
      const fc = this.faceFor(this.mood);
      if (t > this.blinkAt && this.blinkT < 0) this.blinkT = 0;
      let lid = 1;
      if (this.blinkT >= 0) { this.blinkT += dt; const q = this.blinkT / 0.16; lid = q < 1 ? Math.abs(Math.cos(Math.PI * q)) : 1; if (q >= 1) { this.blinkT = -1; this.blinkAt = t + 2.2 + Math.random() * 3; } }
      const lx = 0.04 * Math.sin(t * 0.7) + (this.mood === "point" ? 0.06 : 0), ly = this.mood === "sad" ? -0.05 : this.mood === "thinking" ? 0.05 : 0.01 * Math.sin(t * 0.5);
      for (const k of ["L", "R"]) {
        const arcOn = fc.eyes === "arc" || (fc.eyes === "wink" && k === "R");
        this.arcs[k].visible = arcOn; this.eyes[k].visible = !arcOn;
        const e = this.eyes[k].userData; e.ball.scale.y = 0.23 * Math.max(0.06, lid) * (this.mood === "sad" ? 0.85 : 1);
        e.h1.visible = e.h2.visible = lid > 0.4;
        e.h1.position.set(-0.05 + lx, 0.08 + ly, 0.07); e.ball.position.set(lx * 0.4, ly * 0.4, 0);
      }
      for (const [k, m] of Object.entries(this.mouths)) m.visible = k === fc.mouth;
      const o = this.mood === "talking" ? 0.45 + 0.55 * Math.abs(Math.sin(t * 10)) * (0.6 + 0.4 * Math.sin(t * 2.7)) : this.mood === "happy" ? 1.08 : 1;
      this.mouths.open.scale.set(1, o, 1);
    }

    // Capelo, borla e fita atrasam (molas) em relação ao corpo.
    const dRot = (c.lean - this.prev.rot) / Math.max(dt, 1e-3), dBob = (c.bob - this.prev.bob) / Math.max(dt, 1e-3);
    this.prev.rot = c.lean; this.prev.bob = c.bob;
    // Molas integradas em passos curtos: estáveis com qualquer taxa de quadros.
    const S = this.springs, sp = (s, tgt, kk, dd) => { const n = Math.ceil(dt / (1 / 120)), h = dt / n; for (let i = 0; i < n; i++) { s.v += ((tgt - s.x) * kk - s.v * dd) * h; s.x += s.v * h; } return s.x; };
    const kick = Math.min(1, dt * 60); S.cap.v += -dBob * 0.02 * kick; S.tas.v += (-dRot * 1.2 - dBob * 0.25) * kick; S.tasZ.v += dBob * 0.12 * kick; S.rib.v += (-dRot * 0.8 + dBob * 0.3) * kick;
    this.cap.rotation.z = -0.03 + sp(S.cap, 0, 140, 9);
    this.tassel.quaternion.copy(this.board.quaternion).invert();
    this.tasselInner.rotation.set(-0.3 + sp(S.tasZ, 0.03 * Math.sin(t * 0.9), 40, 2.5), 0, sp(S.tas, -c.lean * 0.8 + 0.04 * Math.sin(t * 1.3), 40, 2.5));
    this.ribbon.rotation.x = sp(S.rib, 0.05 * Math.sin(t * 1.6), 50, 3);

    // Giro (arrastar) volta sozinho para a frente.
    if (!this.dragging) { this.yawV += (-this.yaw * 25 - this.yawV * 7) * dt; this.yaw += this.yawV * dt; }
    this.rig.rotation.y = this.yaw;
  }
  resize(w, h) {
    this.renderer.setSize(w, h, false);
    const cam = this.camera, asp = w / Math.max(1, h); cam.aspect = asp;
    const tan = 2 * Math.tan(THREE.MathUtils.degToRad(cam.fov / 2)), fw = this.bleed ? 5.6 : 6.6, fh = this.bleed ? 6.2 : 6.9;
    const d = Math.max(fh / tan, fw / (tan * asp));
    cam.position.set(0, 1.0 + d * 0.11, d); cam.lookAt(0, -0.55, 0);
    cam.updateProjectionMatrix();
  }
  render() { this.renderer.render(this.scene, this.camera); }
  dispose() { this.renderer.dispose(); this.renderer.forceContextLoss(); this.renderer.domElement.remove(); }
}

export function webglAvailable() { try { const c = document.createElement("canvas"); return !!(c.getContext("webgl2") || c.getContext("webgl")); } catch { return false; } }

// ------------------------------------------------------------------ <estudario-mascote3d>
const live = new Set();
const MAX_LIVE = 6;   // o navegador só dá uns 16 contextos WebGL; além disso, o Folha 2D
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
      if (this.m || this._starting) { if (this.m) { live.add(this); if (!raf) raf = requestAnimationFrame(loop); } return; }
      this._starting = true;
      const size = +(this.getAttribute("size") || 0);
      if (size) { this.style.width = this.style.width || size + "px"; this.style.height = this.style.height || size + "px"; }
      if (!this.style.display) this.style.display = "inline-block";
      if (!this.style.position) this.style.position = "relative";
      if (!webglAvailable() || live.size >= MAX_LIVE) { const f = document.createElement("estudario-mascote"); f.setAttribute("mood", this.getAttribute("mood") || "idle"); if (size) f.setAttribute("size", String(size)); this.appendChild(f); return; }
      let host = this;
      if (size) { host = document.createElement("div"); host.style.cssText = "position:absolute;left:-20%;top:-20%;width:140%;height:140%;pointer-events:none"; this.appendChild(host); }
      const a = await assets();
      this.m = new Mascote3D(host, a, { mood: this.getAttribute("mood") || "idle", bleed: !!size });
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
    attributeChangedCallback(n, _, v) { if (this.m && n === "mood") this.m.setMood(v || "idle"); }
  });
}
if (typeof window !== "undefined") window.EstudarioMascote3D = { Mascote3D, webglAvailable };
