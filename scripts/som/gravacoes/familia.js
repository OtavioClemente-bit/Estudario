// A família de sons do Estudário: papel (o Folha é um livro) + piano, tudo em Ré maior.
// Escola do Duolingo: no dia a dia, sons curtos (menos de 1 s), suaves e com duas ou três notas; o festivo
// é mais rico. O acerto é a dupla de notas subindo; o erro é grave, abafado e nunca pune.
const SAMPLES = {
  // nota de piano gravada em Dó5 e Fá4 (mesmo piano) e piano abafado em Ré6; o acorde de piano em Fá vira Ré (-3)
  pC5: { f: "pnota_334537", from: 0.11, norm: 3.0 }, pF4: { f: "pnota_334541", from: 0.11, norm: 2.95 }, pD6: { f: "pnota_448618", from: 0, norm: 0.74 },
  acorde: { f: "piano_528026", from: 0, norm: 1.36, st: -3 },
  pagina: { f: "folhear_188485", from: 0.06, norm: 0.87 }, folhear: { f: "folhear_466375", from: 0.28, norm: 4.9 }, ultima: { f: "folhear_484912", from: 0.76, norm: 0.7 },
  palma: { f: "livro_793442", from: 0.03, norm: 0.83 }, carimbo: { f: "carimbo_844058", from: 0.08, norm: 0.7 }, carimboSeco: { f: "carimbo_362622", from: 0.04, norm: 1.66 },
  peso: { f: "livro_719022", from: 0, norm: 0.73 },
  martelo: { f: "martelo_270589", from: 0.27, norm: 1, lp: 5000 }, marreta: { f: "martelo_554012", from: 0.07, norm: 1, lp: 5000 }, clink: { f: "martelo_475416", from: 0.73, norm: 1 },
  faisca: { f: "fogo_422851", from: 0.67, norm: 1 }, vapor: { f: "vapor_234782", from: 0.13, norm: 1, lp: 6000 }, sopro: { f: "fogo_178886", from: 0.27, norm: 1 },
  crepitar: { f: "fogo_565304", from: 0.61, norm: 1 }, bola: { f: "fogo_431174", from: 0, norm: 1 }, jato: { f: "fogo_346917", from: 0.47, norm: 1 },
  asa: { f: "asas_561364", from: 0.13, norm: 1 }, asaLeve: { f: "asas_389634", from: 0.15, norm: 1 },
  caixa: { f: "caixa_346169", from: 0.55, norm: 1 }, madeira: { f: "madeira_630492", from: 0, norm: 1 }, fita: { f: "fita_342541", from: 0.07, norm: 1, lp: 7000 },
  pop: { f: "pop_411462", from: 0.17, norm: 1 }, folhaPapel: { f: "papel_144110", from: 0.59, norm: 1, hp: 500 },
  passarinhos: { f: "tonto_714572", from: 0.5, norm: 2.6, lp: 5000 }, farfalhar: { f: "papel_353125", from: 0.26, norm: 1.35, lp: 3500 }, lapis: { f: "lapis_181056", from: 0.08, norm: 1.0, lp: 3500 },
};
// notas da escala de Ré maior a partir das três gravações de piano (semitons de deslocamento)
const NOTE = { D4: ["pF4", -3], "F#4": ["pF4", 1], A4: ["pF4", 4], D5: ["pC5", 2], E5: ["pC5", 4], "F#5": ["pC5", 6], A5: ["pD6", -5], B5: ["pD6", -3], D6: ["pD6", 0] };
const n = (note, at, vol, len = 0.9, extra = {}) => ({ s: NOTE[note][0], st: NOTE[note][1], at, vol, len, ...extra });
const x = (s, at, vol, len = 1, extra = {}) => ({ s, at, vol, len, ...extra });
const FAMILIA = {
  toque: { nome: "Toque no Folha", onde: "Tocar no Folha", vol: 0.8, layers: [x("pagina", 0, 0.7, 0.2, { hp: 500, lp: 3200 }), n("D6", 0.025, 0.22, 0.6)] },
  acerto: { nome: "Acerto", onde: "Resposta certa", vol: 0.62, layers: [x("lapis", 0, 0.12, 0.25), n("D5", 0.0, 0.6, 0.45), n("F#5", 0.085, 0.68, 0.95)] },
  erro: { nome: "Erro", onde: "Resposta errada", vol: 0.42, layers: [n("F#4", 0, 0.5, 0.35, { lp: 1800 }), n("D4", 0.12, 0.55, 0.75, { lp: 1500 })] },
  combo: { nome: "Sequência de acertos", onde: "3, 5, 10 seguidas", vol: 0.7, layers: [n("D5", 0, 0.5, 0.4), n("F#5", 0.07, 0.52, 0.4), n("A5", 0.14, 0.55, 0.5), n("D6", 0.21, 0.58, 1.1)] },
  lembrei: { nome: "Lembrei", onde: "Flashcard e revisão", vol: 0.75, layers: [x("pagina", 0, 0.25, 0.18, { hp: 700 }), n("F#5", 0.02, 0.55, 0.8)] },
  nao_lembrei: { nome: "Não lembrei", onde: "Flashcard e revisão", vol: 0.42, layers: [n("D4", 0, 0.45, 0.55, { lp: 1500 })] },
  concluir: { nome: "Concluir tarefa", onde: "Tarefa do plano, tópico estudado", vol: 0.8, layers: [x("carimboSeco", 0, 0.55, 0.45), n("D5", 0.03, 0.42, 0.9), n("A5", 0.03, 0.36, 0.9)] },
  resultado: { nome: "Fim da bateria", onde: "Resultado das questões", vol: 0.8, layers: [n("D5", 0, 0.5, 0.5), n("F#5", 0.1, 0.52, 0.5), n("A5", 0.2, 0.55, 0.6), n("D6", 0.3, 0.6, 1.4), x("farfalhar", 0.5, 0.12, 0.6)] },
  foco_fim: { nome: "Fim do foco", onde: "Sessão do modo foco encerrada", vol: 0.7, layers: [
    // sino de templo em ré, calmo: o foco acabou, sem festa; a quinta por cima e um brilho bem baixo
    { syn: "bell", note: "D5", at: 0, vol: 0.32, len: 2.6 }, { syn: "bell", note: "A5", at: 0.16, vol: 0.18, len: 2.2 },
    { syn: "pad", notes: ["D4", "A4"], at: 0, vol: 0.06, len: 2.4 }, { syn: "glass", note: "F#6", at: 0.5, vol: 0.06, len: 1 }] },
  missao: { nome: "Missão cumprida", onde: "Missão do Folha, dia concluído", vol: 0.8, layers: [{ riffle: "folhear", at: 0, vol: 0.5, dur: 0.35, pag: 6 }, x("carimbo", 0.4, 0.55, 0.7), x("peso", 0.4, 0.45, 1.2), x("acorde", 0.42, 0.42, 2.0)] },
  // ---- comemorações (2.0): as gravações físicas da cena (martelo, fogo, asas, caixa, a palma do livro) ficam;
  // a parte musical agora é sintetizada e limpa (marimba, sino, vidro, pad, baque e a subida de ar), com a
  // assinatura do Estudário: lá-si-RÉ e o sino em fá# por cima, sempre igual, em Ré maior.
  conquista: { nome: "Conquista", onde: "Fim da forja e do presente", vol: 0.85, impacto: 0.62, layers: [
    { riffle: "folhear", at: 0, vol: 0.45, dur: 0.5, pag: 8 },
    { syn: "riser", at: 0.08, vol: 0.3, len: 0.56 },
    x("palma", 0.62, 0.75, 0.45), { syn: "boom", at: 0.62, vol: 0.85 },
    { sub: "assinatura", at: 0.62 }] },
  conquista_curta: { nome: "Conquista (curta)", onde: "Fim da fênix e do nível", vol: 0.85, impacto: 0, layers: [
    x("palma", 0, 0.7, 0.45), { syn: "boom", at: 0, vol: 0.8 }, { sub: "assinatura", at: 0 }] },
  // o motivo musical que fecha toda comemoração: acorde de sino, fanfarra de marimba, o sino agudo e o brilho
  assinatura: { nome: "Assinatura", onde: "Dentro das comemorações", vol: 1, layers: [
    ...["D5", "F#5", "A5", "D6"].map((nt) => ({ syn: "bell", note: nt, at: 0.0, vol: 0.17, len: 2.4 })),
    { syn: "pad", notes: ["D4", "A4", "D5", "F#5"], at: 0.02, vol: 0.12, len: 2.6 },
    { syn: "marimba", note: "A5", at: 0.0, vol: 0.42, len: 0.4 },
    { syn: "marimba", note: "B5", at: 0.12, vol: 0.42, len: 0.4 },
    { syn: "marimba", note: "D6", at: 0.24, vol: 0.5, len: 0.9 },
    { syn: "bell", note: "F#6", at: 0.38, vol: 0.26, len: 1.8 },
    { syn: "spark", at: 0.42, vol: 0.16, len: 1.5, count: 11 },
    x("acorde", 0.03, 0.18, 2.2)] },
  tonto: { nome: "Folha tonto", onde: "Quatro toques rápidos no Folha", vol: 0.45, layers: [x("passarinhos", 0, 0.8, 1.0), x("passarinhos", 0.75, 0.65, 1.0, { st: 2 })] },
  cena_emblema: { nome: "Cena: a forja", onde: "Emblema novo", vol: 0.9, cena: true, layers: [
    x("clink", 0.85, 0.22, 0.5, { st: 7 }),
    // as marteladas seguem a cena (folha-live.js, FORGE): 1,35 / 2,05 / 2,8; esfria em 2,85; chega em 4,1.
    // Cada martelada também canta na bigorna, subindo (lá, ré, lá): a medalha "afinando" até ficar pronta.
    x("martelo", 1.35, 0.6, 1.0), x("faisca", 1.36, 0.16, 0.5), { syn: "anvil", note: "A4", at: 1.35, vol: 0.32, len: 1.1 },
    x("martelo", 2.05, 0.65, 1.0, { st: -0.6 }), x("faisca", 2.06, 0.18, 0.5), { syn: "anvil", note: "D5", at: 2.05, vol: 0.34, len: 1.1 },
    x("marreta", 2.8, 1.2, 0.85, { st: -1 }), x("faisca", 2.81, 0.24, 0.6), { syn: "anvil", note: "A5", at: 2.8, vol: 0.4, len: 1.5 },
    x("vapor", 2.95, 0.13, 1.3, { fi: 0.05 }),
    { sub: "conquista", at: 4.1 - 0.62 }] },
  cena_sequencia: { nome: "Cena: a fênix", onde: "Sequência do dia e sequência salva", vol: 0.9, cena: true, layers: [
    x("crepitar", 0.55, 0.38, 1.2, { from: 2, fi: 0.2 }),
    // a chama corre pelos braseiros: uma nota de vidro em cada um, subindo
    ...["D5", "E5", "F#5", "A5", "B5", "D6"].map((nt, i) => ({ syn: "glass", note: nt, at: 0.65 + i * 0.16, vol: 0.14 + i * 0.015, len: 0.6 })),
    x("bola", 1.65, 0.28, 0.5, { lp: 2500 }), { syn: "riser", at: 1.6, vol: 0.32, len: 0.55 }, x("jato", 1.72, 0.55, 1.2),
    x("bola", 2.12, 0.7, 1.0), x("sopro", 2.15, 0.38, 1.6, { lp: 5000 }), { syn: "boom", at: 2.18, vol: 0.55 },
    x("asa", 2.3, 0.4, 0.9), x("asaLeve", 2.75, 0.3, 0.7), x("crepitar", 2.4, 0.1, 2.4, { from: 5, fi: 0.4 }),
    { sub: "assinatura", at: 2.7 }] },
  // o Folha salva a sequência (folha-live.js, SAV): noite quieta, o "shhh", a bola de fogo no braseiro que
  // faltou (que pega roxo e dourado), o jato até a fênix e ela acordando; no fim, a assinatura e um brilho baixinho
  cena_salvou: { nome: "Cena: sequência salva", onde: "Volta depois de um dia salvo pelo marca-página", vol: 0.9, cena: true, layers: [
    x("crepitar", 0.2, 0.16, 2.6, { from: 2, fi: 0.5, lp: 2500 }),
    { syn: "pad", notes: ["D3", "A3", "F#4"], at: 0.1, vol: 0.07, len: 3.4 },
    ...[0.45, 0.95].map((t, i) => ({ syn: "glass", note: i ? "A5" : "F#5", at: t, vol: 0.05, len: 0.7 })),
    { syn: "shh", at: 1.3, vol: 0.32, len: 0.9 },
    { syn: "glass", note: "D6", at: 2.72, vol: 0.12, len: 0.4 },
    { syn: "riser", at: 2.75, vol: 0.2, len: 0.32 }, x("bola", 3.02, 0.45, 0.7), x("sopro", 3.05, 0.22, 0.6, { lp: 4000 }),
    x("bola", 3.6, 0.6, 0.9), { syn: "boom", at: 3.6, vol: 0.35 }, { syn: "bell", note: "F#5", at: 3.62, vol: 0.18, len: 1.4 }, { syn: "glass", note: "A6", at: 3.64, vol: 0.14, len: 0.9 },
    { syn: "riser", at: 3.8, vol: 0.3, len: 0.45 }, x("jato", 3.82, 0.55, 1.0),
    x("bola", 4.25, 0.7, 1.0), x("asa", 4.35, 0.42, 0.9), x("asaLeve", 4.8, 0.3, 0.7), x("crepitar", 4.4, 0.1, 2.2, { from: 5, fi: 0.4 }),
    { sub: "conquista_curta", at: 4.25 },
    { syn: "spark", at: 5.2, vol: 0.08, len: 0.9, count: 5 }] },
  cena_nivel: { nome: "Cena: subiu de nível", onde: "Nível novo", vol: 0.9, cena: true, layers: [
    // folha-live.js, LVL: as orbes enchem o anel de 0,15 a 1,2 s chegando rápido no começo e devagar no fim
    // (outC); cada nota do arpejo cai quando o anel passa por mais um sétimo, então elas vêm juntas no começo e
    // espaçam no fim, como a animação. Em 1,25 o emblema antigo racha, o anel acelera até 1,6 (impacto e
    // número novo), as asas abrem em 1,9 e a faixa "NÍVEL" sobe em 2,25.
    ...["D5", "F#5", "A5", "D6", "F#6", "A6", "D7"].map((nt, i) => ({ syn: "marimba", note: nt, at: 0.15 + 1.05 * (1 - Math.cbrt(1 - (i + 1) / 7)), vol: 0.22 + i * 0.03, len: 0.5 })),
    x("madeira", 1.25, 0.3, 0.2, { st: 4 }), { syn: "anvil", note: "D6", at: 1.25, vol: 0.12, len: 0.4 },
    { syn: "riser", at: 1.2, vol: 0.36, len: 0.4 },
    x("bola", 1.58, 0.26, 1.0, { st: -3, lp: 1800 }),
    x("asaLeve", 1.9, 0.34, 0.7), x("asaLeve", 2.12, 0.26, 0.7, { st: 1 }),
    x("fita", 2.25, 0.22, 0.5), { syn: "glass", note: "A6", at: 2.3, vol: 0.08, len: 0.5 },
    { sub: "conquista_curta", at: 1.6 }] },
  cena_peca: { nome: "Cena: o presente", onde: "Peça nova do armário", vol: 0.9, cena: true, layers: [
    x("caixa", 0.45, 0.75, 0.9),
    // três batidinhas na caixa, cada uma com um sininho mais agudo (a curiosidade subindo)
    ...[1.0, 1.38, 1.74].map((t) => x("madeira", t, 0.24, 0.2, { st: -5, lp: 1500 })),
    ...[["F#5", 1.0], ["A5", 1.38], ["D6", 1.74]].map(([nt, t]) => ({ syn: "glass", note: nt, at: t + 0.01, vol: 0.18, len: 0.5 })),
    x("fita", 2.1, 0.3, 0.5), { syn: "riser", at: 2.0, vol: 0.22, len: 0.45 },
    x("pop", 2.45, 0.65, 0.4, { st: -5 }), { syn: "spark", at: 2.47, vol: 0.18, len: 1.1, count: 8 },
    x("folhaPapel", 2.55, 0.26, 0.7)] },
  // (no presente, o "achou!" (conquista) toca à parte: a hora depende de quantas cartas saem da caixa)
};
// Monta e toca um som da família num contexto de áudio (ao vivo ou fora do tempo real, para gerar o WAV).
// ---- sintetizadores das comemorações (tudo determinístico: o mesmo som toda vez)
function hz(name) {
  const m = /^([A-G])(#?)(\d)$/.exec(name); const base = { C: 0, D: 2, E: 4, F: 5, G: 7, A: 9, B: 11 }[m[1]] + (m[2] ? 1 : 0);
  return 440 * Math.pow(2, (base + (Number(m[3]) + 1) * 12 - 69) / 12);
}
let _ruido = null;
function ruidoDe(ctx) {
  if (_ruido && _ruido.sampleRate === ctx.sampleRate) return _ruido;
  const n = ctx.sampleRate * 2, b = ctx.createBuffer(1, n, ctx.sampleRate), d = b.getChannelData(0); let seed = 11;
  for (let i = 0; i < n; i++) { seed = (seed * 1103515245 + 12345) & 0x7fffffff; d[i] = seed / 0x7fffffff * 2 - 1; }
  return (_ruido = b);
}
function sintetizar(ctx, L, t, bus) {
  const env = (g, peak, a, len, curve = 0.0008) => { g.gain.setValueAtTime(0.0001, t); g.gain.linearRampToValueAtTime(peak, t + a); g.gain.exponentialRampToValueAtTime(curve, t + len); g.gain.linearRampToValueAtTime(0, t + len + 0.02); };
  const osc = (type, f, peak, a, len, dest = bus, detune = 0) => { const o = ctx.createOscillator(); o.type = type; o.frequency.value = f; o.detune.value = detune; const g = ctx.createGain(); env(g, peak, a, len); o.connect(g).connect(dest); o.start(t); o.stop(t + len + 0.05); return o; };
  const v = L.vol, len = L.len || 1;
  if (L.syn === "marimba") {
    // marimba: fundamental redonda, o 4º harmônico (a "madeira") que some rápido e um clique de baqueta
    const f = hz(L.note);
    osc("sine", f, v, 0.003, len);
    osc("sine", f * 3.93, v * 0.28, 0.002, Math.min(len, 0.16));
    osc("sine", f * 9.8, v * 0.07, 0.001, 0.05);
    const s = ctx.createBufferSource(); s.buffer = ruidoDe(ctx); const bp = ctx.createBiquadFilter(); bp.type = "bandpass"; bp.frequency.value = f * 4; bp.Q.value = 2; const g = ctx.createGain(); env(g, v * 0.25, 0.001, 0.025); s.connect(bp).connect(g).connect(bus); s.start(t, 0.3, 0.06);
  } else if (L.syn === "bell" || L.syn === "anvil") {
    // sino FM: a modulação começa forte (o "ting" metálico) e vai limpando até sobrar a nota pura.
    // A bigorna é o mesmo sino com parciais inarmônicas mais fortes e cauda mais curta.
    const f = hz(L.note), anvil = L.syn === "anvil";
    const car = ctx.createOscillator(); car.frequency.value = f;
    const mod = ctx.createOscillator(); mod.frequency.value = f * (anvil ? 2.756 : 3.5);
    const mg = ctx.createGain(); mg.gain.setValueAtTime(f * (anvil ? 4 : 2.2), t); mg.gain.exponentialRampToValueAtTime(f * 0.02, t + (anvil ? 0.25 : 0.7));
    mod.connect(mg).connect(car.frequency);
    const g = ctx.createGain(); env(g, v, 0.002, len); car.connect(g).connect(bus);
    car.start(t); mod.start(t); car.stop(t + len + 0.05); mod.stop(t + len + 0.05);
    osc("sine", f * (anvil ? 5.4 : 2.76), v * (anvil ? 0.22 : 0.12), 0.001, len * 0.35);
    osc("sine", f * 2, v * 0.18, 0.002, len * 0.7);
  } else if (L.syn === "glass") {
    // sininho de vidro: duas senoides quase em oitava (o leve batimento é o brilho)
    const f = hz(L.note);
    osc("sine", f * 2, v, 0.002, len); osc("sine", f * 4.02, v * 0.35, 0.001, len * 0.5); osc("sine", f * 6.1, v * 0.1, 0.001, len * 0.2);
  } else if (L.syn === "pad") {
    // pad quente: dentes-de-serra levemente desafinados, filtro abrindo devagar e fechando no fim
    const lp = ctx.createBiquadFilter(); lp.type = "lowpass"; lp.Q.value = 0.7;
    lp.frequency.setValueAtTime(500, t); lp.frequency.linearRampToValueAtTime(2600, t + len * 0.35); lp.frequency.linearRampToValueAtTime(900, t + len);
    const g = ctx.createGain(); g.gain.setValueAtTime(0.0001, t); g.gain.linearRampToValueAtTime(v, t + 0.18); g.gain.setValueAtTime(v, t + len * 0.5); g.gain.linearRampToValueAtTime(0, t + len);
    lp.connect(g).connect(bus);
    for (const nt of L.notes) for (const d of [-7, 6]) { const o = ctx.createOscillator(); o.type = "sawtooth"; o.frequency.value = hz(nt); o.detune.value = d; const og = ctx.createGain(); og.gain.value = 0.25; o.connect(og).connect(lp); o.start(t); o.stop(t + len + 0.05); }
  } else if (L.syn === "shh") {
    // o "shhh" do Folha: ruído agudo e macio, entrando devagar e saindo mais devagar ainda
    const s = ctx.createBufferSource(); s.buffer = ruidoDe(ctx); s.loop = true;
    const hp = ctx.createBiquadFilter(); hp.type = "highpass"; hp.frequency.value = 2400;
    const lp = ctx.createBiquadFilter(); lp.type = "lowpass"; lp.frequency.value = 7500;
    const g = ctx.createGain(); g.gain.setValueAtTime(0.0001, t); g.gain.linearRampToValueAtTime(v, t + len * 0.25); g.gain.setValueAtTime(v, t + len * 0.45); g.gain.linearRampToValueAtTime(0, t + len);
    s.connect(hp).connect(lp).connect(g).connect(bus); s.start(t, 0.5); s.stop(t + len + 0.05);
  } else if (L.syn === "riser") {
    // subida de ar: ruído num filtro que abre de grave para agudo, crescendo até o impacto
    const s = ctx.createBufferSource(); s.buffer = ruidoDe(ctx); s.loop = true;
    const bp = ctx.createBiquadFilter(); bp.type = "bandpass"; bp.Q.value = 1.4; bp.frequency.setValueAtTime(350, t); bp.frequency.exponentialRampToValueAtTime(7000, t + len);
    const g = ctx.createGain(); g.gain.setValueAtTime(0.0001, t); g.gain.exponentialRampToValueAtTime(v, t + len * 0.95); g.gain.linearRampToValueAtTime(0, t + len + 0.03);
    s.connect(bp).connect(g).connect(bus); s.start(t); s.stop(t + len + 0.06);
  } else if (L.syn === "boom") {
    // baque grave no impacto: seno caindo de 120 para 40 Hz e um "tum" de ruído abafado
    const o = ctx.createOscillator(); o.frequency.setValueAtTime(125, t); o.frequency.exponentialRampToValueAtTime(42, t + 0.35);
    const g = ctx.createGain(); env(g, v, 0.004, 0.7); o.connect(g).connect(bus); o.start(t); o.stop(t + 0.75);
    const s = ctx.createBufferSource(); s.buffer = ruidoDe(ctx); const lp = ctx.createBiquadFilter(); lp.type = "lowpass"; lp.frequency.value = 260; const g2 = ctx.createGain(); env(g2, v * 0.6, 0.002, 0.12); s.connect(lp).connect(g2).connect(bus); s.start(t, 0.7, 0.15);
  } else if (L.syn === "spark") {
    // brilho: pinguinhos de vidro bem agudos na escala de Ré, espalhados e sumindo
    const esc = ["D7", "E7", "F#7", "A7", "B7", "D7", "A6", "F#7"]; let seed = 5;
    for (let i = 0; i < (L.count || 8); i++) {
      seed = (seed * 1103515245 + 12345) & 0x7fffffff; const r = seed / 0x7fffffff;
      const at = t + (i / (L.count || 8)) * len * 0.8 + r * 0.05, u = i / (L.count || 8);
      sintetizar(ctx, { syn: "glass", note: esc[(i * 3 + Math.floor(r * 5)) % esc.length], vol: v * (1 - u * 0.7), len: 0.35 }, at, bus);
    }
  }
}
async function tocar(ctx, nome, buffers, t0 = ctx.currentTime + 0.03, saida = ctx.destination) {
  const rec = FAMILIA[nome];
  const lp = ctx.createBiquadFilter(); lp.type = "lowpass"; lp.frequency.value = 6000; lp.Q.value = 0.5;
  const comp = ctx.createDynamicsCompressor(); comp.threshold.value = -14; comp.knee.value = 10; comp.ratio.value = 6; comp.attack.value = 0.003; comp.release.value = 0.25;
  const master = ctx.createGain(); master.gain.value = rec.vol;
  const sala = ctx.createConvolver(); sala.buffer = salaDe(ctx);
  const dry = ctx.createGain(), wet = ctx.createGain(); dry.gain.value = 0.84; wet.gain.value = 0.32;
  const bus = ctx.createGain(); bus.connect(dry).connect(lp); bus.connect(sala); sala.connect(wet).connect(lp); lp.connect(comp).connect(master).connect(saida);
  const hit = (sm, at, len, rate, vol, flt, fi = 0.004) => {
    const b = buffers[sm.f]; const src = ctx.createBufferSource(); src.buffer = b; src.playbackRate.value = rate;
    let node = src; for (const [type, fr] of flt) { if (!fr) continue; const f = ctx.createBiquadFilter(); f.type = type; f.frequency.value = fr; f.Q.value = 0.6; node.connect(f); node = f; }
    const gn = ctx.createGain(); node.connect(gn).connect(bus);
    const fo = Math.min(0.5, len * 0.5);
    gn.gain.setValueAtTime(0, at); gn.gain.linearRampToValueAtTime(vol, at + (fi || 0.004)); gn.gain.setValueAtTime(vol, at + Math.max(0.005, len - fo)); gn.gain.linearRampToValueAtTime(0, at + len);
    src.start(at, Math.max(0, sm.from || 0), len + 0.02);
  };
  for (const L of rec.layers) {
    if (L.sub) { await tocar(ctx, L.sub, buffers, t0 + L.at, saida); continue; }
    if (L.syn) { sintetizar(ctx, L, t0 + L.at, bus); continue; }
    if (L.riffle) {
      // páginas cada vez mais juntas, mais fortes e um tico mais agudas: quem procura a página certa
      const sm = SAMPLES[L.riffle], gaps = []; for (let i = 0; i < L.pag - 1; i++) gaps.push(Math.pow(0.78, i)); const k = L.dur / gaps.reduce((a, c) => a + c, 0); let tt = 0;
      for (let i = 0; i < L.pag; i++) { const u = i / (L.pag - 1); hit({ ...sm, from: sm.from + ((i * 0.137) % 0.6) }, t0 + L.at + tt, 0.14, Math.pow(2, (u * 2.5) / 12), L.vol * sm.norm * (0.6 + 0.4 * u), []); if (i < L.pag - 1) tt += gaps[i] * k; }
      continue;
    }
    const sm = SAMPLES[L.s]; const rate = Math.pow(2, ((sm.st || 0) + (L.st || 0)) / 12);
    hit({ ...sm, from: L.from != null ? L.from : sm.from }, t0 + L.at, L.len, rate, L.vol * sm.norm, [["lowpass", L.lp || sm.lp], ["highpass", L.hp || sm.hp]], L.fi);
  }
}
let _sala = null;
function salaDe(ctx) {
  // sala pequena e macia (a mesma resposta sempre, para o som ser igual toda vez)
  const n = Math.round(ctx.sampleRate * 1.1), b = ctx.createBuffer(2, n, ctx.sampleRate); let seed = 7;
  const rnd = () => { seed = (seed * 1103515245 + 12345) & 0x7fffffff; return seed / 0x7fffffff; };
  for (let c = 0; c < 2; c++) { const d = b.getChannelData(c); let lp = 0; for (let i = 0; i < n; i++) { lp = lp * 0.55 + (rnd() * 2 - 1) * 0.45; d[i] = lp * Math.pow(1 - i / n, 3.4); } }
  return b;
}
