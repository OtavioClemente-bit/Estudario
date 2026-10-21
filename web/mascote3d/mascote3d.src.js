// O Folha em 3D: o modelo final (design/mascote/folha-personagem.glb) com esqueleto e animações feitos no
// Blender por scripts/web/mascote/folha-blender.py -> web/src/jsMain/resources/folha.glb. Aqui só se toca a
// animação de cada humor (com transição suave entre eles) e se acrescentam o piscar e a piscadela (as
// pálpebras vão até a pose da animação "eyes_shut"), a boca falando (osso "mouth") e a troca da boca
// (camada "boca": tira de quadros 0 = sorriso, 1 = sorrisão, 2 = "hum", 3 = triste; a metade de baixo da
// imagem é a transparência, usada como alphaMap, porque o GLB leva as texturas em JPEG).
// Gera web/src/jsMain/resources/mascote3d.js com `node scripts/web/build-mascote3d.mjs`.
//
// Uso: <estudario-mascote3d mood="idle|happy|wave|talking|thinking|sad|point|wink|run|run-left"
//        size="160" [spin]>
// Sem WebGL (ou com muitos na tela) cai no recorte animado (<estudario-mascote>).
import * as THREE from "three";
import { GLTFLoader } from "three/addons/loaders/GLTFLoader.js";
import { RoomEnvironment } from "three/addons/environments/RoomEnvironment.js";

const V3 = (x = 0, y = 0, z = 0) => new THREE.Vector3(x, y, z);
const lerp = (a, b, t) => a + (b - a) * t;
const H = 1.7;   // altura do modelo (unidades do GLB); a cena usa o Folha com altura 1

const base = (() => { try { const s = document.currentScript && document.currentScript.src; return s ? s.replace(/[^/]*$/, "") : ""; } catch { return ""; } })();

// Humor -> animação do GLB.
// Humor -> quadro da boca.
const MOUTH_FRAME = { happy: 1, wave: 1, thinking: 2, sad: 3 };
const MOUTH_FRAMES = 4;
const CLIP = { idle: "idle", happy: "happy", wave: "wave", talking: "talking", thinking: "thinking", sad: "sad", point: "point", wink: "idle", run: "run", "run-left": "run" };

let MODEL = null;
function loadModel() {
  if (MODEL) return MODEL;
  // Baixa o GLB e faz o parse com o TextureLoader comum (sem createImageBitmap/fetch de blob:, que o
  // CSP do app barra): sem createImageBitmap na hora em que o parser nasce, o three usa <img>.
  MODEL = fetch((typeof window !== "undefined" && window.ESTUDARIO_FOLHA_GLB) || base + "folha.glb").then((r) => { if (!r.ok) throw new Error("folha.glb " + r.status); return r.arrayBuffer(); }).then((buf) => new Promise((res, rej) => {
    const cib = window.createImageBitmap;
    try { window.createImageBitmap = undefined; } catch (_) { /* ok */ }
    new GLTFLoader().parse(buf, base, (g) => res(g), rej);
    try { window.createImageBitmap = cib; } catch (_) { /* ok */ }
  }));
  return MODEL;
}

const MAT = {};
function confettiMat() { return MAT.confetti || (MAT.confetti = new THREE.MeshStandardMaterial({ side: THREE.DoubleSide, roughness: 0.6 })); }

// Cópia de uma cena com malha esqueletada (cada Folha na tela anima sozinho; a geometria e as texturas
// são compartilhadas).
function cloneSkinned(src) {
  const map = new Map(), clone = src.clone(true);
  const walk = (a, b) => { map.set(a, b); for (let i = 0; i < a.children.length; i++) walk(a.children[i], b.children[i]); };
  walk(src, clone);
  src.traverse((o) => {
    if (!o.isSkinnedMesh) return;
    const c = map.get(o), bones = o.skeleton.bones.map((b) => map.get(b));
    c.bind(new THREE.Skeleton(bones, o.skeleton.boneInverses), o.bindMatrix);
  });
  return clone;
}

// ------------------------------------------------------------------ o personagem
export class Mascote3D {
  constructor(container, gltf, { mood = "idle", bleed = false } = {}) {
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

    // ---- o modelo (cada elemento tem a sua cópia, com o esqueleto próprio)
    const model = this.model = cloneSkinned(gltf.scene);
    model.traverse((o) => { if (o.isMesh) { o.castShadow = true; o.receiveShadow = true; o.frustumCulled = false; } });
    this.rig = new THREE.Group(); this.rig.scale.setScalar(1 / H); this.rig.add(model); scene.add(this.rig);
    const bone = (n) => model.getObjectByName(n) || model.getObjectByName(n.replace(".", "")) || model.getObjectByName(n.replace(".", "_"));
    this.face = { lidL: bone("lidU.L"), lidR: bone("lidU.R"), lowL: bone("lidD.L"), lowR: bone("lidD.R"), browL: bone("brow.L"), browR: bone("brow.R"), mouth: bone("mouth") };
    // O mixer só reescreve um osso quando a animação muda o valor dele; como os ajustes do rosto vão por
    // cima, antes de cada quadro o osso volta ao último valor que a animação deu.
    this.faceBones = Object.values(this.face).filter(Boolean);
    // Boca: a camada "boca" (um material só desta instância, para cada Folha ter o seu quadro). A cor fica
    // na metade de cima da imagem e a transparência na de baixo.
    model.traverse((o) => { if (o.isMesh && o.material && o.material.name === "boca") this.mouthMesh = o; });
    if (this.mouthMesh) {
      const m = this.bocaMat = this.mouthMesh.material.clone();
      m.map = m.map.clone(); m.map.needsUpdate = true;
      m.alphaMap = m.map.clone(); m.alphaMap.offset.y = 0.5; m.alphaMap.needsUpdate = true;
      // Desenhada por cima da página (a dobra do meio do livro sobe um pouco e cortaria a boca); quando o
      // livro mostra o verso para a câmera, ela some (update).
      Object.assign(m, { transparent: true, depthWrite: false, depthTest: false });
      this.mouthMesh.material = m; this.mouthMesh.renderOrder = 5; this.mouthMesh.castShadow = false;
    }
    this.mouthFrame = -1;
    this.faceBase = null;
    // Olho fechado: a pose das pálpebras na animação "eyes_shut" (as duas se encontram).
    const shut = gltf.animations.find((c) => c.name === "eyes_shut"), qOf = (b) => {
      const tr = shut && b && shut.tracks.find((t) => t.name === b.name + ".quaternion");
      return tr ? new THREE.Quaternion().fromArray(tr.values, 0) : null;
    };
    this.shut = { lidL: qOf(this.face.lidL), lidR: qOf(this.face.lidR), lowL: qOf(this.face.lowL), lowR: qOf(this.face.lowR) };


    // ---- animações
    this.mixer = new THREE.AnimationMixer(model);
    this.actions = {};
    for (const c of gltf.animations) this.actions[c.name] = this.mixer.clipAction(c);
    for (const k of ["sad", "jump"]) { const a = this.actions[k]; if (a) { a.setLoop(THREE.LoopOnce, 1); a.clampWhenFinished = true; } }
    this.mixer.addEventListener("finished", (e) => { if (e.action === this.actions.jump) this.fadeTo(CLIP[this.mood] || "idle", 0.25); });

    // ---- confete
    {
      const N = 120; this.conf = [];
      const cf = this.confetti = new THREE.InstancedMesh(new THREE.PlaneGeometry(0.016, 0.01), confettiMat(), N);
      cf.frustumCulled = false; const cols = ["#F5B83D", "#4FD3C6", "#F4A6C0", "#6F64FF", "#ffffff", "#FFE07A"].map((c) => new THREE.Color(c));
      for (let i = 0; i < N; i++) { cf.setColorAt(i, cols[i % cols.length]); this.conf.push({ life: 0, p: V3(), v: V3(), w: V3(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5).normalize(), spin: 4 + Math.random() * 8, ph: Math.random() * 6 }); }
      scene.add(cf);
    }

    // ---- estado
    this.t = 0; this.yaw = 0; this.yawV = 0; this.dragging = false; this.faceYaw = 0; this.moodT = 0;
    this.blinkAt = 1.8; this.blinkT = -1; this.dbl = false; this.cur = null;
    this.resize(container.clientWidth || 400, container.clientHeight || 400);
    this.setMood(mood, true);
    this.update(1 / 60);
  }

  fadeTo(name, dur = 0.35) {
    const next = this.actions[name] || this.actions.idle; if (!next) return;
    if (next === this.cur && next.isRunning()) return;
    next.reset().setEffectiveWeight(1).play();
    if (this.cur && this.cur !== next && dur > 0) this.cur.crossFadeTo(next, dur, false);
    else if (this.cur && this.cur !== next) this.cur.stop();
    this.cur = next;
  }
  setMood(m, first) {
    if (m === this.mood && !first) return;
    this.mood = m; this.moodT = 0;
    if (m === "happy" && !first) this.burst();
    // Se ele estiver no meio do pulo, o humor novo entra quando ele aterrissa.
    if (!(this.cur === this.actions.jump && this.cur.isRunning())) this.fadeTo(CLIP[m] || "idle", first ? 0 : 0.35);
  }
  poke() {
    const j = this.actions.jump;
    if (j && this.cur !== j) this.fadeTo("jump", 0.12);
    if (this.mood === "happy") this.burst(60);
  }
  burst(n = 110) {
    let k = 0;
    for (const c of this.conf) {
      if (c.life > 0 || k >= n) continue; k++;
      c.life = 2.6 + Math.random() * 1.4; c.p.set((Math.random() - 0.5) * 0.3, 0.98 + Math.random() * 0.05, 0.15);
      const a = Math.random() * Math.PI * 2, sp = 0.4 + Math.random() * 0.7;
      c.v.set(Math.cos(a) * sp * 0.6, 0.9 + Math.random() * 0.8, Math.sin(a) * sp * 0.4 + 0.2);
    }
  }

  update(dt) {
    const t = (this.t += dt); this.moodT += dt;
    const still = window.matchMedia && window.matchMedia("(prefers-reduced-motion: reduce)").matches;
    if (this.faceBase) for (const [b, p, s, q] of this.faceBase) { b.position.copy(p); b.scale.copy(s); b.quaternion.copy(q); }
    this.mixer.update(still ? 0 : dt);
    this.faceBase = this.faceBones.map((b) => [b, b.position.clone(), b.scale.clone(), b.quaternion.clone()]);

    // ---- rosto, por cima da animação: piscar (às vezes duas vezes), piscadela e a boca falando
    const F = this.face;
    if (t > this.blinkAt && this.blinkT < 0) this.blinkT = 0;
    let blink = 0;
    if (this.blinkT >= 0) {
      this.blinkT += dt; const q = this.blinkT / 0.17; blink = q < 1 ? Math.sin(Math.PI * q) : 0;
      if (q >= 1) { this.blinkT = -1; if (!this.dbl && Math.random() < 0.25) { this.dbl = true; this.blinkAt = t + 0.12; } else { this.dbl = false; this.blinkAt = t + 2 + Math.random() * 3.2; } }
    }
    // Piscadela: fixa no humor "wink"; no tchau, uma a cada três acenos.
    const wk = this.mood === "wink" ? Math.min(1, this.moodT / 0.2) : this.mood === "wave" && Math.floor(t / 2.2) % 3 === 2 ? Math.sin(Math.min(1, (t % 2.2) / 0.2) * Math.PI / 2) : 0;
    for (const [k, amt] of [["lidL", blink], ["lowL", blink], ["lidR", Math.max(blink, wk)], ["lowR", Math.max(blink, wk)]]) {
      if (amt > 0 && F[k] && this.shut[k]) F[k].quaternion.slerp(this.shut[k], amt);
    }
    if (F.browL && blink) F.browL.position.y -= 0.004 * blink;
    if (F.browR) F.browR.position.y -= 0.006 * Math.max(blink, wk);
    if (F.mouth && this.mood === "talking") F.mouth.scale.y *= 0.65 + 0.75 * this.talkOpen(t);
    if (F.mouth && wk) F.mouth.scale.y *= 1 + 0.15 * wk;
    this.setMouth(MOUTH_FRAME[this.mood] || 0);


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
    const side = this.mood === "run" ? 1.05 : this.mood === "run-left" ? -1.05 : 0;
    this.faceYaw = lerp(this.faceYaw, side, 1 - Math.exp(-dt * 8));
    this.rig.rotation.y = this.yaw + this.faceYaw;
    if (this.mouthMesh) {
      // De frente, por cima de tudo (a dobra do livro não corta a boca); de lado, com profundidade (não passa
      // por cima dos óculos); de costas, some.
      const cy = Math.cos(this.rig.rotation.y);
      this.mouthMesh.visible = cy > 0.25; this.bocaMat.depthTest = cy < 0.93;
    }
  }

  setMouth(frame) {
    if (frame === this.mouthFrame || !this.bocaMat) return;
    this.mouthFrame = frame;
    this.bocaMat.map.offset.x = this.bocaMat.alphaMap.offset.x = frame / MOUTH_FRAMES;
  }

  // Fala: abre e fecha em "sílabas", com pausas entre as palavras.
  talkOpen(t) {
    const word = Math.sin(t * 1.9) + 0.35 * Math.sin(t * 4.7) > -0.55;
    const syl = Math.max(0, Math.sin(t * 13) * 0.6 + Math.sin(t * 21.7 + 1) * 0.4);
    return word ? 0.25 + 0.75 * syl : 0.0;
  }

  resize(w, h) {
    this.renderer.setSize(w, h, false);
    const cam = this.camera, asp = w / Math.max(1, h); cam.aspect = asp;
    const tan = 2 * Math.tan(THREE.MathUtils.degToRad(cam.fov / 2)), fw = this.bleed ? 1.12 : 1.3, fh = this.bleed ? 1.12 : 1.3;
    const d = Math.max(fh / tan, fw / (tan * asp));
    cam.position.set(0, 0.55 + d * 0.12, d); cam.lookAt(0, this.bleed ? 0.55 : 0.52, 0);
    cam.updateProjectionMatrix();
  }
  render() { this.renderer.render(this.scene, this.camera); }
  dispose() { this.mixer.stopAllAction(); this.renderer.dispose(); this.renderer.forceContextLoss(); this.renderer.domElement.remove(); }
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
      let gltf; try { gltf = await loadModel(); } catch (e) { console.warn("Folha 3D:", e); fallback(); return; }
      this.m = new Mascote3D(host, gltf, { mood: this.getAttribute("mood") || "idle", bleed: !!size });
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
if (typeof window !== "undefined") window.EstudarioMascote3D = { Mascote3D, webglAvailable, loadModel };
