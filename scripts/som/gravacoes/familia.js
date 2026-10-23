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
  missao: { nome: "Missão cumprida", onde: "Missão do Folha, dia concluído", vol: 0.8, layers: [{ riffle: "folhear", at: 0, vol: 0.5, dur: 0.35, pag: 6 }, x("carimbo", 0.4, 0.55, 0.7), x("peso", 0.4, 0.45, 1.2), x("acorde", 0.42, 0.42, 2.0)] },
  conquista: { nome: "Conquista", onde: "Fim da forja, da fênix, do nível e do presente", vol: 0.85, impacto: 0.62, layers: [
    { riffle: "folhear", at: 0, vol: 0.6, dur: 0.55, pag: 8 }, x("ultima", 0.5, 0.55, 0.35), x("palma", 0.62, 0.9, 0.5), x("carimbo", 0.63, 0.55, 0.8),
    x("peso", 0.62, 0.7, 1.6), x("acorde", 0.66, 0.45, 2.4), x("farfalhar", 0.95, 0.18, 0.8)] },
  conquista_curta: { nome: "Conquista (curta)", onde: "Fim da fênix e do nível", vol: 0.85, impacto: 0, layers: [
    x("palma", 0, 0.85, 0.5), x("carimbo", 0.01, 0.5, 0.8), x("peso", 0, 0.7, 1.6), x("acorde", 0.04, 0.48, 2.4), x("farfalhar", 0.33, 0.16, 0.8)] },
  tonto: { nome: "Folha tonto", onde: "Quatro toques rápidos no Folha", vol: 0.45, layers: [x("passarinhos", 0, 0.8, 1.0), x("passarinhos", 0.75, 0.65, 1.0, { st: 2 })] },
  cena_emblema: { nome: "Cena: a forja", onde: "Emblema novo", vol: 0.9, cena: true, layers: [
    x("clink", 0.85, 0.22, 0.5, { st: 7 }),
    // as marteladas seguem a cena (folha-live.js, FORGE): 1,35 / 2,05 / 2,8; esfria em 2,85; chega em 4,1
    x("martelo", 1.35, 0.7, 1.2), x("faisca", 1.36, 0.18, 0.5),
    x("martelo", 2.05, 0.75, 1.2, { st: -0.6 }), x("faisca", 2.06, 0.2, 0.5),
    x("marreta", 2.8, 1.4, 0.85, { st: -1 }), x("faisca", 2.81, 0.26, 0.6),
    x("vapor", 2.95, 0.15, 1.3, { fi: 0.05 }), x("sopro", 3.5, 0.07, 0.8, { st: 3, lp: 3000, fi: 0.15 }),
    { sub: "conquista", at: 4.1 - 0.62 }] },
  cena_sequencia: { nome: "Cena: a fênix", onde: "Sequência do dia", vol: 0.9, cena: true, layers: [
    x("crepitar", 0.55, 0.42, 1.2, { from: 2, fi: 0.2 }),
    x("bola", 1.65, 0.32, 0.5, { lp: 2500 }), x("jato", 1.72, 0.62, 1.2),
    x("bola", 2.12, 0.8, 1.0), x("sopro", 2.15, 0.45, 1.6, { lp: 5000 }),
    x("asa", 2.3, 0.42, 0.9), x("asaLeve", 2.75, 0.32, 0.7), x("crepitar", 2.4, 0.1, 2.4, { from: 5, fi: 0.4 }),
    { sub: "conquista_curta", at: 2.7 }] },
  cena_nivel: { nome: "Cena: subiu de nível", onde: "Nível novo", vol: 0.9, cena: true, layers: [
    ...["D5", "E5", "F#5", "A5", "B5", "D6"].map((nt, i) => n(nt, 0.15 + i * 0.17, 0.16 + i * 0.03, 0.55)),
    x("madeira", 1.25, 0.25, 0.2, { st: 4 }),
    x("bola", 1.58, 0.3, 1.0, { st: -3, lp: 1800 }),
    x("asaLeve", 1.9, 0.36, 0.7), x("asaLeve", 2.15, 0.28, 0.7, { st: 1 }),
    { sub: "conquista_curta", at: 1.6 }] },
  cena_peca: { nome: "Cena: o presente", onde: "Peça nova do armário", vol: 0.9, cena: true, layers: [
    x("caixa", 0.45, 0.8, 0.9),
    ...[1.0, 1.38, 1.74].map((t) => x("madeira", t, 0.26, 0.2, { st: -5, lp: 1500 })),
    x("fita", 2.1, 0.32, 0.5),
    x("pop", 2.45, 0.7, 0.4, { st: -5 }),
    x("folhaPapel", 2.55, 0.28, 0.7)] },
  // (no presente, o "achou!" (conquista) toca à parte: a hora depende de quantas cartas saem da caixa)
};
// Monta e toca um som da família num contexto de áudio (ao vivo ou fora do tempo real, para gerar o WAV).
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
