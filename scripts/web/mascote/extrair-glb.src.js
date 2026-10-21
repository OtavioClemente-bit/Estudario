// Fonte da ferramenta de extração: empacotar com esbuild e abrir num servidor local junto do GLB do Meshy;
// ela devolve (window.FILES.glb) o folha.glb que vai para web/src/jsMain/resources.
// Tira do GLB do Meshy (4 figuras numa malha) só a figura de frente, centraliza com os pés em y = 0,
// altura 1, junta os vértices usados, recorta/reduz as texturas e exporta um GLB pequeno.
import * as THREE from "three";
import { GLTFLoader } from "three/addons/loaders/GLTFLoader.js";
import { GLTFExporter } from "three/addons/exporters/GLTFExporter.js";

const q = new URLSearchParams(location.search);
new GLTFLoader().load("../../../design/mascote/folha-meshy.glb", async (g) => {
  let mesh; g.scene.traverse((o) => { if (o.isMesh) mesh = o; });
  g.scene.updateMatrixWorld(true);
  const src = mesh.geometry.clone().applyMatrix4(mesh.matrixWorld);
  const P = src.attributes.position, N = src.attributes.normal, UV = src.attributes.uv, idx = src.index.array;
  const keep = [];
  for (let i = 0; i < idx.length; i += 3) {
    let cx = 0, cy = 0; for (let k = 0; k < 3; k++) { cx += P.getX(idx[i + k]); cy += P.getY(idx[i + k]); }
    if (cx < 0 && cy > 0) keep.push(idx[i], idx[i + 1], idx[i + 2]);
  }
  const map = new Map(), pos = [], nor = [], uv = [], ind = [];
  for (const i of keep) {
    if (!map.has(i)) { map.set(i, map.size); pos.push(P.getX(i), P.getY(i), P.getZ(i)); nor.push(N.getX(i), N.getY(i), N.getZ(i)); uv.push(UV.getX(i), UV.getY(i)); }
    ind.push(map.get(i));
  }
  // Centraliza, pés no chão, altura 1.
  let min = [1e9, 1e9, 1e9], max = [-1e9, -1e9, -1e9];
  for (let i = 0; i < pos.length; i += 3) for (let k = 0; k < 3; k++) { min[k] = Math.min(min[k], pos[i + k]); max[k] = Math.max(max[k], pos[i + k]); }
  const s = 1 / (max[1] - min[1]), cx = (min[0] + max[0]) / 2, cz = (min[2] + max[2]) / 2;
  for (let i = 0; i < pos.length; i += 3) { pos[i] = (pos[i] - cx) * s; pos[i + 1] = (pos[i + 1] - min[1]) * s; pos[i + 2] = (pos[i + 2] - cz) * s; }
  // Recorte das texturas: só a região de UV usada pela figura.
  let u0 = 1, u1 = 0, v0 = 1, v1 = 0;
  for (let i = 0; i < uv.length; i += 2) { u0 = Math.min(u0, uv[i]); u1 = Math.max(u1, uv[i]); v0 = Math.min(v0, uv[i + 1]); v1 = Math.max(v1, uv[i + 1]); }
  const pad = 0.004; u0 = Math.max(0, u0 - pad); v0 = Math.max(0, v0 - pad); u1 = Math.min(1, u1 + pad); v1 = Math.min(1, v1 + pad);
  for (let i = 0; i < uv.length; i += 2) { uv[i] = (uv[i] - u0) / (u1 - u0); uv[i + 1] = (uv[i + 1] - v0) / (v1 - v0); }
  const mat = mesh.material, OUT = +(q.get("size") || 1024);
  const crop = (tex, size, srgb) => {
    if (!tex) return null;
    const im = tex.image, W = im.width, H = im.height;
    const c = document.createElement("canvas"); c.width = size; c.height = size;
    // glTF: v = 0 no alto da imagem (flipY false)
    c.getContext("2d").drawImage(im, u0 * W, v0 * H, (u1 - u0) * W, (v1 - v0) * H, 0, 0, size, size);
    const t = new THREE.CanvasTexture(c); t.flipY = false; if (srgb) t.colorSpace = THREE.SRGBColorSpace; t.userData.mimeType = "image/jpeg"; return t;
  };
  const geo = new THREE.BufferGeometry();
  geo.setAttribute("position", new THREE.Float32BufferAttribute(pos, 3));
  geo.setAttribute("normal", new THREE.Float32BufferAttribute(nor, 3));
  geo.setAttribute("uv", new THREE.Float32BufferAttribute(uv, 2));
  geo.setIndex(ind);
  const out = new THREE.MeshStandardMaterial({ map: crop(mat.map, OUT, true), normalMap: crop(mat.normalMap, OUT / 2, false), roughness: 0.75, metalness: 0 });
  const m = new THREE.Mesh(geo, out); m.name = "folha";
  const res = await new GLTFExporter().parseAsync(m, { binary: true });
  const u8 = new Uint8Array(res); let bin = ""; for (let i = 0; i < u8.length; i += 32768) bin += String.fromCharCode.apply(null, u8.subarray(i, i + 32768)); const b64 = btoa(bin);
  window.FILES = { glb: "data:model/gltf-binary;base64," + b64 };
  window.INFO = JSON.stringify({ verts: pos.length / 3, tris: ind.length / 3, uv: [u0, u1, v0, v1].map((v) => v.toFixed(3)), bytes: res.byteLength, size: [(max[0] - min[0]) * s, 1, (max[2] - min[2]) * s].map((v) => v.toFixed(3)) });
  window.DONE = 1;
});
