// Gera os sons do Estudário, sintetizados do zero (nenhuma amostra pronta): sino de papel em FM, papel
// (ruído filtrado), metal de bigorna (parciais inarmônicos), fogo (ruído com filtro varrendo) e um eco
// curto de sala. A assinatura é a sequência Lá-Ré-Fá#-Lá ("Es-tu-dá-rio") em Ré maior.
//
// Uso: node scripts/som/gerar-sons.mjs
// Saída: web/src/jsMain/resources/sons/<nome>.wav (site e cenas do app) e app/src/main/res/raw/som_<nome>.wav.
// Cada trilha de cena acompanha a linha do tempo da animação em folha-live.js (LVL, FORGE, SEQ e o presente).
import fs from "node:fs";
import path from "node:path";

const SR = 44100;
const ROOT = path.resolve(path.dirname(new URL(import.meta.url).pathname.replace(/^\/([A-Za-z]:)/, "$1")), "../..");

// ------------------------------------------------------------------ peças básicas
const buf = (sec) => new Float32Array(Math.ceil(sec * SR));
const note = (n) => 440 * 2 ** ((n - 69) / 12);                 // MIDI -> Hz
const N = { A3: 57, D4: 62, Fs4: 66, A4: 69, B4: 71, Cs5: 73, D5: 74, E5: 76, Fs5: 78, A5: 81, B5: 83, D6: 86, Fs6: 90, A6: 93 };
let seed = 12345;
const rnd = () => { seed = (seed * 1103515245 + 12345) & 0x7fffffff; return seed / 0x7fffffff; };
function add(dst, src, at, gain = 1) { const o = Math.round(at * SR); for (let i = 0; i < src.length && o + i < dst.length; i++) if (o + i >= 0) dst[o + i] += src[i] * gain; }

// filtro biquad (RBJ): passa-banda, passa-baixa, passa-alta, com frequência que pode variar no tempo
function biquad(x, type, freqAt, q = 0.7) {
  const y = new Float32Array(x.length); let x1 = 0, x2 = 0, y1 = 0, y2 = 0;
  for (let i = 0; i < x.length; i++) {
    const f = Math.min(SR * 0.45, Math.max(20, typeof freqAt === "function" ? freqAt(i / SR) : freqAt)), w = 2 * Math.PI * f / SR, a = Math.sin(w) / (2 * q), c = Math.cos(w);
    let b0, b1, b2; const a0 = 1 + a, a1 = -2 * c, a2 = 1 - a;
    if (type === "bp") { b0 = a; b1 = 0; b2 = -a; } else if (type === "lp") { b0 = (1 - c) / 2; b1 = 1 - c; b2 = (1 - c) / 2; } else { b0 = (1 + c) / 2; b1 = -(1 + c); b2 = (1 + c) / 2; }
    const v = (b0 * x[i] + b1 * x1 + b2 * x2 - a1 * y1 - a2 * y2) / a0; x2 = x1; x1 = x[i]; y2 = y1; y1 = v; y[i] = v;
  }
  return y;
}
const noise = (sec) => { const b = buf(sec); for (let i = 0; i < b.length; i++) b[i] = rnd() * 2 - 1; return b; };
// fim suave: cada nota termina em zero (cortar no meio do som dá estalo)
function fadeEnd(b, sec = 0.04) { const n = Math.min(b.length, Math.round(sec * SR)); for (let i = 0; i < n; i++) b[b.length - 1 - i] *= i / n; return b; }
function shape(b, attack, decay, hold = 0) { for (let i = 0; i < b.length; i++) { const t = i / SR; b[i] *= Math.min(1, t / Math.max(attack, 1e-4)) * (t < hold ? 1 : Math.exp(-(t - hold) * decay)); } return b; }

// o sino de papel: FM com o índice caindo rápido (o "ping" claro no ataque vira um tom macio)
function bell(freq, sec, { ratio = 3.5, index = 2.2, decay = 3.2, bright = 6 } = {}) {
  const b = buf(sec);
  for (let i = 0; i < b.length; i++) {
    const t = i / SR, I = index * Math.exp(-t * bright);
    const m = Math.sin(2 * Math.PI * freq * ratio * t) * I;
    b[i] = (Math.sin(2 * Math.PI * freq * t + m) + 0.25 * Math.sin(4 * Math.PI * freq * t)) * Math.min(1, t / 0.003) * Math.exp(-t * decay);
  }
  // o "papel": um sopro curto de ruído agudo no começo
  const paper = shape(biquad(noise(0.04), "bp", 5200, 1.2), 0.001, 90);
  add(b, paper, 0, 0.18);
  return fadeEnd(b, Math.min(0.25, sec * 0.3));
}
// pluck de corda (Karplus-Strong), para os sons mais suaves
function pluck(freq, sec, damp = 0.996, bright = 0.5) {
  const b = buf(sec), L = Math.round(SR / freq), line = new Float32Array(L).map(() => rnd() * 2 - 1); let p = 0;
  for (let i = 0; i < b.length; i++) { const n = (p + 1) % L, v = line[p]; line[p] = damp * (bright * v + (1 - bright) * line[n]) ; b[i] = v; p = n; }
  return fadeEnd(shape(b, 0.002, 4), Math.min(0.2, sec * 0.3));
}
// metal de bigorna: parciais inarmônicos com quedas diferentes, mais o estalo do impacto
function anvil(base, sec, big = 1) {
  const b = buf(sec), P = [[1, 1, 7], [2.76, 0.7, 9], [5.4, 0.5, 13], [8.93, 0.32, 18], [13.3, 0.2, 26], [3.9, 0.35, 11]];
  for (const [r, g, d] of P) for (let i = 0; i < b.length; i++) { const t = i / SR; b[i] += g * Math.sin(2 * Math.PI * base * r * t + r) * Math.exp(-t * d / big); }
  add(b, shape(biquad(noise(0.06), "hp", 2500, 0.8), 0.0005, 70), 0, 1.2 * big);
  add(b, thump(0.25, 110 * big, 50, 9), 0, 0.6 * big);
  return fadeEnd(b, Math.min(0.2, sec * 0.3));
}
// pancada grave (corpo): seno com o tom caindo
function thump(sec, f0 = 120, f1 = 45, decay = 10) {
  const b = buf(sec); let ph = 0;
  for (let i = 0; i < b.length; i++) { const t = i / SR, f = f1 + (f0 - f1) * Math.exp(-t * 18); ph += 2 * Math.PI * f / SR; b[i] = Math.sin(ph) * Math.exp(-t * decay) * Math.min(1, t / 0.002); }
  return fadeEnd(b, Math.min(0.1, sec * 0.3));
}
// sopro / fogo: ruído com filtro passa-banda varrendo
function whoosh(sec, f0, f1, q = 1.2, curve = 1) {
  const n = noise(sec), y = biquad(n, "bp", (t) => f0 + (f1 - f0) * Math.pow(t / sec, curve), q);
  for (let i = 0; i < y.length; i++) { const u = i / y.length; y[i] *= Math.sin(Math.PI * Math.min(1, u * 1.15)) ** 1.5; }
  return y;
}
// crepitar de fagulha: estalinhos aleatórios
function crackle(sec, rate = 40) {
  const b = buf(sec);
  for (let k = 0; k < sec * rate; k++) { const at = rnd() * sec, c = shape(biquad(noise(0.012), "bp", 2000 + rnd() * 5000, 2), 0.0003, 250 + rnd() * 200); add(b, c, at, 0.3 + rnd() * 0.7); }
  return b;
}
// cintilar: vários sininhos agudos espalhados
function shimmer(sec, count = 14, lo = N.A5, notes = [N.A5, N.D6, N.Fs6, N.A6, N.B5, N.E5 + 12]) {
  const b = buf(sec + 0.8);
  for (let k = 0; k < count; k++) { const n = notes[Math.floor(rnd() * notes.length)]; add(b, bell(note(n), 0.8, { index: 1.4, decay: 6, ratio: 4.1 }), rnd() * sec, 0.12 + rnd() * 0.1); }
  return b;
}
// a assinatura: Lá-Ré-Fá#-Lá, com um acorde de Ré no fim
function signature(step = 0.11, sustain = 1.6, gain = 1) {
  const b = buf(step * 4 + sustain + 0.5);
  [N.A4, N.D5, N.Fs5, N.A5].forEach((n, i) => add(b, bell(note(n), sustain + 0.4, { decay: 2.4 }), i * step, 0.5 * gain));
  for (const n of [N.D4, N.A4, N.D5]) add(b, pluck(note(n), sustain + 0.5, 0.998, 0.6), step * 3, 0.16 * gain);
  return b;
}
// eco curto de sala (Schroeder): dá corpo sem embolar
function room(x, mix = 0.16, size = 1) {
  const combs = [1557, 1617, 1491, 1422].map((d) => Math.round(d * size)), aps = [225, 556];
  const out = new Float32Array(x.length);
  for (const d of combs) { const z = new Float32Array(d); let p = 0, lp = 0; for (let i = 0; i < x.length; i++) { const v = z[p]; lp = v * 0.7 + lp * 0.3; z[p] = x[i] + lp * 0.78; out[i] += v / 4; p = (p + 1) % d; } }
  let y = out;
  for (const d of aps) { const z = new Float32Array(d); let p = 0; const o = new Float32Array(y.length); for (let i = 0; i < y.length; i++) { const v = z[p], inp = y[i]; z[p] = inp + v * 0.5; o[i] = v - inp * 0.5; p = (p + 1) % d; } y = o; }
  const r = new Float32Array(x.length); for (let i = 0; i < x.length; i++) r[i] = x[i] * (1 - mix) + y[i] * mix; return r;
}
// acabamento: tira o grave inútil, satura de leve, normaliza o pico e faz a cauda sumir sem estalo
function master(x, peakDb = -3, tail = 0.25) {
  let y = biquad(x, "hp", 35, 0.7);
  for (let i = 0; i < y.length; i++) y[i] = Math.tanh(y[i] * 1.1);
  let peak = 0; for (const v of y) peak = Math.max(peak, Math.abs(v));
  const g = peak > 0 ? 10 ** (peakDb / 20) / peak : 1;
  const T = Math.round(tail * SR);
  for (let i = 0; i < y.length; i++) { y[i] *= g; const k = y.length - i; if (k < T) y[i] *= k / T; if (i < 32) y[i] *= i / 32; }
  return y;
}

// ------------------------------------------------------------------ os sons
const S = {};
// tocar no Folha: um "bup" que sobe, com um toquezinho de papel
S.toque = () => { const b = buf(0.22); let ph = 0; for (let i = 0; i < b.length; i++) { const t = i / SR, f = 520 + 300 * (1 - Math.exp(-t * 40)); ph += 2 * Math.PI * f / SR; b[i] = Math.sin(ph) * Math.exp(-t * 22) * Math.min(1, t / 0.004) * 0.6; } add(b, shape(biquad(noise(0.03), "bp", 4500, 1.5), 0.0005, 120), 0, 0.12); return master(room(b, 0.1), -9); };
// acerto: Ré e Lá subindo, claro
S.acerto = () => { const b = buf(0.9); add(b, bell(note(N.D5), 0.8), 0, 0.6); add(b, bell(note(N.A5), 0.8), 0.075, 0.6); return master(room(b, 0.14), -6); };
// erro: duas notas suaves descendo, abafadas (sem buzina de erro)
S.erro = () => { const b = buf(0.7); add(b, biquad(pluck(note(N.Fs4), 0.6, 0.994, 0.45), "lp", 1800), 0, 0.7); add(b, biquad(pluck(note(N.D4), 0.6, 0.994, 0.45), "lp", 1500), 0.11, 0.7); return master(room(b, 0.12), -9); };
// sequência de acertos: arpejo rápido subindo e brilho
S.combo = () => { const b = buf(1.2); [N.D5, N.Fs5, N.A5, N.D6].forEach((n, i) => add(b, bell(note(n), 0.8, { decay: 4 }), i * 0.055, 0.45)); add(b, shimmer(0.35, 6), 0.2, 0.7); return master(room(b, 0.16), -5); };
// fim de bateria, dia concluído, missão cumprida: a assinatura
S.resultado = () => master(room(signature(0.1, 1.2, 1), 0.2), -5);
S.missao = () => { const b = buf(2.4); add(b, signature(0.085, 1.3, 1), 0); add(b, shimmer(0.6, 8), 0.3, 0.8); return master(room(b, 0.2), -5); };

// cena do nível: orbes enchendo (tom subindo), racha, estouro com o acorde e as asas abrindo
S.cena_nivel = () => {
  const b = buf(4.2);
  const rise = buf(1.3); let ph = 0; for (let i = 0; i < rise.length; i++) { const t = i / SR, f = 220 * 2 ** (t / 1.25 * 1.6); ph += 2 * Math.PI * f / SR; rise[i] = (Math.sin(ph) + 0.3 * Math.sin(2 * ph)) * (t / 1.3) ** 2 * 0.25; }
  add(b, rise, 0.1); add(b, whoosh(1.25, 400, 3000, 1.5, 2), 0.2, 0.25);
  for (let k = 0; k < 10; k++) add(b, bell(note([N.A5, N.D6, N.Fs6][k % 3]), 0.25, { index: 1, decay: 14 }), 0.15 + k * 0.1, 0.08);
  add(b, crackle(0.35, 60), 1.25, 0.6);
  add(b, thump(0.9, 140, 40, 4), 1.6, 1.1); add(b, whoosh(0.9, 3000, 300, 0.9, 0.5), 1.6, 0.5);
  add(b, signature(0.07, 2.0, 1.1), 1.6, 1);
  add(b, whoosh(0.5, 800, 2500, 2, 1), 1.9, 0.3); add(b, shimmer(1.2, 16), 1.95, 0.9);
  return master(room(b, 0.2, 1.1), -3, 0.6);
};
// a forja: o disco cai (tlim), três marteladas na bigorna (a última maior), vapor, a medalha sobe e o acorde
S.cena_emblema = () => {
  const b = buf(5.2);
  add(b, whoosh(0.3, 500, 1500, 1.5), 0.55, 0.15);
  add(b, anvil(1150, 0.6, 0.6), 0.85, 0.35); add(b, anvil(1600, 0.3, 0.4), 1.02, 0.12);
  for (const [at, big] of [[1.25, 0.9], [1.7, 0.95], [2.15, 1.25]]) { add(b, anvil(820, 1.2, big), at, 0.75 * big); add(b, crackle(0.4, 50), at, 0.35 * big); add(b, whoosh(0.18, 3000, 900, 1, 1), at - 0.12, 0.15); }
  const steam = biquad(noise(1.2), "bp", (t) => 6000 - t * 2500, 0.6); shape(steam, 0.05, 2.6); add(b, steam, 2.22, 0.5);
  add(b, whoosh(0.65, 600, 4000, 2, 1.4), 2.85, 0.3); add(b, shimmer(0.6, 8), 2.9, 0.5);
  add(b, thump(0.6, 120, 45, 6), 3.45, 0.6); add(b, signature(0.07, 1.8, 1.1), 3.42, 1); add(b, shimmer(1.0, 14), 3.6, 0.8);
  return master(room(b, 0.17, 1.1), -3, 0.6);
};
// a fênix: estalos da faísca correndo, acende, jato de fogo e estouro, bater de asas e o número em sino
S.cena_sequencia = () => {
  const b = buf(4.6);
  add(b, crackle(1.05, 55), 0.6, 0.55); add(b, whoosh(1.05, 1500, 4500, 3, 1), 0.6, 0.12);
  add(b, thump(0.5, 90, 40, 8), 1.65, 0.6); add(b, whoosh(0.5, 300, 1200, 0.8), 1.65, 0.5);
  add(b, whoosh(0.45, 400, 2600, 0.9, 1.3), 1.72, 0.7);
  add(b, thump(1.0, 130, 35, 3.5), 2.15, 1.1); add(b, whoosh(1.2, 2500, 250, 0.7, 0.6), 2.15, 0.8); add(b, crackle(1.5, 30), 2.2, 0.5);
  for (let k = 0; k < 3; k++) add(b, whoosh(0.32, 300, 900, 1.2, 1), 2.25 + k * 0.42, 0.35);
  add(b, signature(0.08, 1.7, 1.1), 2.66, 1); add(b, shimmer(1.0, 10, N.D6), 2.8, 0.6);
  return master(room(b, 0.18, 1.1), -3, 0.6);
};
// o presente: a caixa cai, se mexe (chocalho), a fita corre, a tampa pula, a carta sobe e o clarão
S.cena_peca = () => {
  const b = buf(5.4);
  add(b, whoosh(0.4, 400, 1200, 1.2), 0.05, 0.2); add(b, thump(0.4, 110, 60, 12), 0.45, 0.9); add(b, shape(biquad(noise(0.08), "bp", 900, 0.8), 0.001, 40), 0.45, 0.4);
  for (const at of [1.0, 1.38, 1.74]) for (let k = 0; k < 4; k++) add(b, shape(biquad(noise(0.03), "bp", 1400 + k * 300, 2), 0.001, 90), at + k * 0.045, 0.35);
  const zip = biquad(noise(0.3), "bp", (t) => 2000 + t * 9000, 4); shape(zip, 0.01, 6); add(b, zip, 2.1, 0.5);
  { const p = buf(0.25); let ph = 0; for (let i = 0; i < p.length; i++) { const t = i / SR, f = 300 + 900 * Math.exp(-t * 25); ph += 2 * Math.PI * f / SR; p[i] = Math.sin(ph) * Math.exp(-t * 20); } add(b, p, 2.45, 0.6); }
  add(b, shimmer(0.9, 18), 2.45, 0.9);
  { const r = buf(0.7); let ph = 0; for (let i = 0; i < r.length; i++) { const t = i / SR, f = 500 * 2 ** (t / 0.55 * 1.5); ph += 2 * Math.PI * f / SR; r[i] = Math.sin(ph) * Math.sin(Math.PI * t / 0.7) * 0.2; } add(b, r, 2.55); }
  add(b, whoosh(0.3, 800, 3000, 2), 3.4, 0.3);
  add(b, thump(0.5, 140, 50, 7), 3.65, 0.5); add(b, signature(0.07, 1.6, 1.1), 3.62, 1); add(b, shimmer(1.0, 14), 3.75, 0.7);
  return master(room(b, 0.18, 1.1), -3, 0.6);
};

// ------------------------------------------------------------------ grava
function wav(x) {
  const n = x.length, b = Buffer.alloc(44 + n * 2);
  b.write("RIFF", 0); b.writeUInt32LE(36 + n * 2, 4); b.write("WAVE", 8); b.write("fmt ", 12); b.writeUInt32LE(16, 16); b.writeUInt16LE(1, 20); b.writeUInt16LE(1, 22);
  b.writeUInt32LE(SR, 24); b.writeUInt32LE(SR * 2, 28); b.writeUInt16LE(2, 32); b.writeUInt16LE(16, 34); b.write("data", 36); b.writeUInt32LE(n * 2, 40);
  for (let i = 0; i < n; i++) b.writeInt16LE(Math.max(-32767, Math.min(32767, Math.round(x[i] * 32767))), 44 + i * 2);
  return b;
}
const webDir = path.join(ROOT, "web/src/jsMain/resources/sons"), rawDir = path.join(ROOT, "app/src/main/res/raw");
fs.mkdirSync(webDir, { recursive: true }); fs.mkdirSync(rawDir, { recursive: true });
for (const [name, make] of Object.entries(S)) {
  seed = 12345 + name.length * 977;
  const x = make(); let peak = 0, sum = 0, bad = 0;
  for (const v of x) { if (!Number.isFinite(v)) bad++; peak = Math.max(peak, Math.abs(v)); sum += v * v; }
  const data = wav(x);
  fs.writeFileSync(path.join(webDir, name + ".wav"), data);
  fs.writeFileSync(path.join(rawDir, "som_" + name + ".wav"), data);
  console.log(name.padEnd(16), (x.length / SR).toFixed(2) + " s", "pico " + (20 * Math.log10(peak)).toFixed(1) + " dB", "rms " + (10 * Math.log10(sum / x.length)).toFixed(1) + " dB", bad ? "INVÁLIDOS " + bad : "ok", (data.length / 1024).toFixed(0) + " KB");
}
