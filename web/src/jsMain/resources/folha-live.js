// O Folha animado do site (2.0). Nasceu da tradução de app/.../ui/assistant/Folha.kt (mesma grade 100 x 100,
// mesmas cores e o mesmo rosto) e ganhou acabamento de mascote:
//  - cada humor é uma POSE; o Folha vai de uma pose para a outra por molas (nada teleporta), com antecipação
//    (agacha antes de pular, o braço volta antes de acenar) e passa um pouco do ponto antes de assentar;
//  - estica subindo e amassa ao aterrissar; as mãos vêm um tiquinho atrasadas em relação ao corpo;
//  - braços que afinam e dobram, mãos de verdade (luva, aberta para acenar, fechada para comemorar,
//    apontando), com punho e pulseira dourada;
//  - olhos com pálpebras de cima e de baixo (feliz em meia-lua, triste caído, piscadela), pupila que cresce
//    quando ele se anima, sobrancelhas em todos os humores; triste de verdade (a ponta de dentro sobe);
//  - livro com cara de livro do Estudário (cantoneiras douradas, capa com espessura, miolo com várias folhas,
//    cabeceado, orelha, anotações com marca-texto) e contorno macio para ler bem em fundo claro e escuro;
//    pequeno (menos de 72 px), some o detalhe miúdo e os traços engrossam.
// O canvas sobra para fora da caixa (MARGIN), para mão e borla nunca serem cortadas.
//
// Uso: <estudario-folha mood="idle|thinking|talking|happy|sad|wave|point|wink|surprised|proud|focus|sleepy|relieved|ecstatic"
//        size="96" [say="texto"] [cover|ribbon|hat|eyes|neck|hand|back|aura|gloves|pet="peça do armário"] [plain] [noscene] [noenter]>
// Armário e contexto do dia: window.folhaSetLook({ cover, hat, ..., streak, atRisk }); saída: window.folhaLeave(raiz, fim).
// Toque: amassa, pula e pisca. el.gulp (0..1) abre a boca para "engolir" folhas (tela de processamento).
// Um laço de animação só para todos, parado fora da tela; quadro fixo para quem pede menos movimento.
(function () {
  if (window.customElements && customElements.get("estudario-folha")) return;

  const INK = "#26215C", PAGE_LINE = "#D8D4E6", CAP = "#231C6B", CAP_TOP = "#2F2789", ARM = "#3326CE", ARM_L = "#6158F0";
  const GOLD = "#F5B83D", MINT = "#7EE0B8", MINT_DARK = "#3FBF8F", CHEEK = "#F4A6C0", SPARK = "#FFE07A";
  const PAPER = "#FFFDF8", PAPER_MID = "#F7F4EE", PAPER_EDGE = "#E9E4DA", PAPER_EDGE_DARK = "#CFC8BC", GUTTER = "#3A3170";
  const PI = Math.PI, sin = Math.sin, cos = Math.cos, abs = Math.abs;
  const MARGIN = 22;                          // % de sobra do canvas para cada lado da caixa (cabe o companheiro e o item na mão)
  const SMALL = 72;                           // abaixo disso (px), desenho simplificado

  const reduceMotion = window.matchMedia ? window.matchMedia("(prefers-reduced-motion: reduce)") : { matches: false };
  // Canvas desenhado pelo processador, não pela placa de vídeo. O Chrome passa para a placa os canvas
  // maiores que ~128 px, e em algumas placas/drivers eles saíam como um quadrado preto (o Folha pequeno
  // seguia normal). Pedir "willReadFrequently" no primeiro getContext deixa o canvas na memória comum.
  const cpuCanvas = () => { const c = document.createElement("canvas"); try { c.getContext("2d", { willReadFrequently: true }); } catch (e) { /* navegador antigo */ } return c; };

  // ------------------------------------------------------------------ armário (FolhaWardrobe.kt) e contexto do dia
  // Capas: degradê (claro, meio, escuro), a sombra de baixo, o metal das cantoneiras e a textura. Os braços
  // acompanham a capa (arm: cor do braço e o brilho dele), para o Folha ser uma peça só.
  const COVERS = {
    indigo: { c: ["#5247E0", "#3A2FC4", "#2A209E"], under: "#1A1370", corner: "gold", arm: ["#3326CE", "#6158F0"] },
    esmeralda: { c: ["#36C897", "#1F9E75", "#147A5A"], under: "#0B4F3A", corner: "gold", arm: ["#178A63", "#4FD3A2"] },
    rubi: { c: ["#E8566D", "#C42F4B", "#961D37"], under: "#5E0F22", corner: "gold", arm: ["#B3263F", "#EE6B80"] },
    ametista: { c: ["#AA68EE", "#8746D3", "#662DAB"], under: "#3E1670", corner: "gold", arm: ["#7438C4", "#B07CF0"] },
    bolinhas: { c: ["#FF8FA3", "#F06A85", "#D14D6B"], under: "#7A1F35", corner: "silver", pattern: "dots", arm: ["#D9506D", "#FF9DB0"] },
    couro: { c: ["#BC7D4E", "#965A31", "#6D3D1F"], under: "#43240E", corner: "gold", pattern: "leather", stitch: "rgba(255,226,180,0.75)", arm: ["#82491F", "#C08453"] },
    jeans: { c: ["#5B86C4", "#3F6AA8", "#2C4F84"], under: "#1B3358", corner: "silver", pattern: "denim", stitch: "rgba(242,166,74,0.9)", arm: ["#355E99", "#6E98D4"] },
    xadrez: { c: ["#D2463C", "#B0322B", "#86231E"], under: "#4F1410", corner: "gold", pattern: "plaid", arm: ["#A12C26", "#DE6A5F"] },
    grafite: { c: ["#61667C", "#43475C", "#2D3042"], under: "#1A1C28", corner: "silver", arm: ["#3A3E52", "#6B7088"] },
    noite: { c: ["#36388A", "#212059", "#121138"], under: "#07061E", corner: "silver", pattern: "stars", arm: ["#26246A", "#4B4AA8"] },
    holografica: { c: ["#C9B6FF", "#9FD8FF", "#FFC2E2"], under: "#5B4C9A", corner: "silver", pattern: "holo", arm: ["#8E7CE0", "#C7B8FF"] },
    ouro: { c: ["#FFDD84", "#EBB13A", "#BD8214"], under: "#7A4F05", corner: "silver", pattern: "shine", arm: ["#C98A12", "#FFD978"] },
    obsidiana: { c: ["#34343F", "#1B1B24", "#0C0C12"], under: "#000000", corner: "gold", pattern: "foil", arm: ["#22222C", "#4A4A5C"] },
    diamante: { c: ["#EAF6FF", "#B9DDF7", "#86BCE6"], under: "#4F7FA6", corner: "silver", pattern: "facets", arm: ["#6FA9D9", "#BFE3FF"] },
    tweed: { c: ["#9A8A78", "#7A6B5B", "#5A4D40"], under: "#2E241B", corner: "silver", pattern: "tweed", stitch: "rgba(232,214,186,0.7)", arm: ["#6A5B4C", "#A4937F"] },
    real: { c: ["#8A4FD6", "#6430A8", "#45187C"], under: "#22093F", corner: "gold", pattern: "fleur", arm: ["#5A2A9C", "#9A6BE0"] },
    // coleções de carreira: verde-oliva com camuflado, jaleco branco e o Vade Mecum de couro vinho
    oliva: { c: ["#86935A", "#66733D", "#48542A"], under: "#262E12", corner: "silver", pattern: "camo", stitch: "rgba(214,204,150,0.7)", arm: ["#5C6934", "#929F62"] },
    jaleco: { c: ["#FFFFFF", "#EEF2F8", "#D3DBE7"], under: "#8D99AD", corner: "silver", pattern: "jaleco", stitch: "rgba(70,140,200,0.7)", arm: ["#B8C4D6", "#E9EEF6"] },
    vademecum: { c: ["#B23A45", "#8C1F2C", "#62121C"], under: "#3A0910", corner: "gold", pattern: "codigo", arm: ["#7E1A26", "#BE4A55"] },
    // farda azul-marinho da polícia, lousa verde do professor e o terno chumbo de risca de giz do controle
    farda: { c: ["#3A4A8C", "#22306A", "#141C46"], under: "#0A0F2A", corner: "gold", pattern: "farda", stitch: "rgba(233,174,52,0.6)", arm: ["#1C2858", "#3A4A8C"] },
    lousa: { c: ["#3F7A5C", "#2D5E45", "#1F4532"], under: "#10271B", corner: "silver", pattern: "lousa", arm: ["#2A5641", "#4E8A6A"] },
    chumbo: { c: ["#555A66", "#3C404B", "#272A33"], under: "#14161C", corner: "silver", pattern: "risca", arm: ["#33363F", "#626775"] },
    // armário 2.0: carreiras (saúde, militar, polícia, jurídico, fiscal, bancos, legislativo, TI) e estampas
    "pijama-cirurgico": { c: ["#6FD0C4", "#3FA69B", "#2A8078"], under: "#134640", corner: "silver", pattern: "pijama", stitch: "rgba(225,255,250,0.6)", arm: ["#2E8A82", "#74D6CA"] },
    deserto: { c: ["#E2CDA4", "#C9AD7C", "#A3875A"], under: "#5A4628", corner: "silver", pattern: "deserto", stitch: "rgba(255,240,210,0.6)", arm: ["#A88C5E", "#E0CBA0"] },
    colete: { c: ["#34405E", "#1F2740", "#121726"], under: "#06080E", corner: "silver", pattern: "colete", arm: ["#1C2338", "#3E4A6A"] },
    processo: { c: ["#F2DCA8", "#DFC184", "#C4A262"], under: "#7A5E2C", corner: "silver", pattern: "processo", arm: ["#BE9C5C", "#F0D8A2"] },
    "nota-fiscal": { c: ["#FFFFFF", "#F5F2EA", "#E2DDCF"], under: "#9A937F", corner: "silver", pattern: "nota", arm: ["#B9B3A2", "#F2EEE4"] },
    bancario: { c: ["#3368C4", "#1F4796", "#14306C"], under: "#0A1A40", corner: "gold", pattern: "bancario", arm: ["#1A3E86", "#4678D0"] },
    plenario: { c: ["#479866", "#2C6E45", "#1C4E2F"], under: "#0D2A18", corner: "gold", pattern: "plenario", arm: ["#245C3A", "#55A672"] },
    espiral: { c: ["#FF946F", "#F06A48", "#C94E32"], under: "#6E2614", corner: "silver", pattern: "espiral", arm: ["#D8583A", "#FFA283"] },
    circuito: { c: ["#33A862", "#1E7E46", "#135A30"], under: "#08301A", corner: "gold", pattern: "circuito", arm: ["#1A6E3C", "#45B872"] },
    "pasta-arquivo": { c: ["#86B8E8", "#5E96D2", "#4074B0"], under: "#1F4270", corner: "silver", pattern: "pasta", arm: ["#4A80BE", "#94C2EE"] },
    girassol: { c: ["#78C0F2", "#4C98DA", "#3076B8"], under: "#163E6A", corner: "gold", pattern: "girassol", arm: ["#3A82C6", "#86C8F4"] },
    oceano: { c: ["#46BCE6", "#1F8EC6", "#11649A"], under: "#073A5C", corner: "silver", pattern: "ondas", arm: ["#187CB2", "#58C6EC"] },
    melancia: { c: ["#62C870", "#3A9E4A", "#257A33"], under: "#0F4219", corner: "silver", pattern: "melancia", arm: ["#2F8A3E", "#70D27E"] },
    cosmos: { c: ["#43237E", "#22104E", "#0B0626"], under: "#000010", corner: "silver", pattern: "cosmos", arm: ["#2A1460", "#5A36A0"] },
  };
  // Fitas: escuro, meio e claro (quatro cores = degradê pronto, como o arco-íris).
  const RIBBONS = {
    menta: [MINT_DARK, MINT, "#C6F7E2"], ceu: ["#3D8BEA", "#6FB0FF", "#D6EAFF"], coral: ["#E2604A", "#FF8C75", "#FFD6CC"],
    lilas: ["#9466DD", "#B995F5", "#ECDFFF"], preta: ["#111118", "#2F2F3A", "#77778A"], dourada: ["#C98A12", GOLD, "#FFEBB5"],
    arcoiris: ["#E8566D", "#F5B83D", "#36C897", "#6FB0FF"],
    "verde-cirurgico": ["#2E8A82", "#5FC4B8", "#CDF2EC"], camuflada: ["#48542A", "#86935A", "#C9CFA0"], zebrada: ["#2A2410", "#FFD21F", "#FFF3A6"],
    vinho: ["#5E1220", "#8C1F2C", "#D97A88"], cedula: ["#2E7A4A", "#5BAE76", "#CDEBD6"], relatorio: ["#1E4592", "#3A6FD0", "#CFE0FF"],
    "ouro-velho": ["#7A5814", "#B08A2C", "#E8D08A"], senado: ["#123E8C", "#1F5BC4", "#BFD6FF"],
    giz: ["#FF9DB8", "#FFE36B", "#9FE8C4", "#9FD3FF"], "cabo-rede": ["#1F5FC4", "#3D8BEA", "#BFDCFF"], clipes: ["#7E8296", "#CDD0DC", "#FFFFFF"],
  };
  // Fitas com estampa ou formato próprio (o resto é só degradê).
  const RIBBON_PAT = { camuflada: "camo", zebrada: "zebra", cedula: "cedula", "ouro-velho": "brilho", giz: "giz", "cabo-rede": "cabo", clipes: "clipes" };
  // Luvas: preenchimento, punho e o tipo (arco-íris tem degradê; boxe muda o formato da mão).
  const GLOVES = {
    branca: ["#FFFFFF", "#ECE8F6"], menta: ["#C9F7E4", "#7EE0B8"], coral: ["#FFD3C9", "#FF8C75"],
    preta: ["#3A3A46", "#24242E"], dourada: ["#FFE9A6", "#F5B83D"], arcoiris: ["#FFFFFF", "#B995F5", "rainbow"],
    boxe: ["#E5373F", "#FFFFFF", "boxe"],
    // armário 2.0: látex azul, táticas com protetor no nó dos dedos, gala com botão de pérola, couro com costura,
    // giz (brancas com pó) e RGB (as cores vão mudando devagar)
    procedimento: ["#A8D2F8", "#6FA6E6", "latex"], taticas: ["#4A4E40", "#2A2D22", "tatica"], operacional: ["#2E3A50", "#161C2A", "tatica"],
    gala: ["#FFFFFF", "#F2EEFF", "gala"], "couro-marrom": ["#A8693C", "#6D3D1F", "couro"], chumbo: ["#5E636F", "#3C404B"],
    marinho: ["#2E3C7C", "#1A2350"], plenario: ["#3E8A5A", "#1C4E2F"], giz: ["#FFFFFF", "#E8E4DA", "giz"],
    rgb: ["#FF5A7A", "#6FB0FF", "rgb"], areia: ["#E8D6AC", "#C4A878"],
  };
  // O que a pessoa escolheu e o contexto do dia; o site atualiza por window.folhaSetLook (veja o fim do arquivo).
  // streak: dias seguidos (medalha no capelo a partir de 7); atRisk: a sequência pode cair hoje; hour: para testes.
  // O próprio Folha: cor da tinta (olhos, sobrancelhas, boca) e das bochechas.
  const INKS = { marinho: INK, azul: "#1F4FD8", vermelha: "#C42F4B", verde: "#0F8A5F", grafite: "#3A3A44", roxa: "#6D3CE0", cafe: "#6B3E1E", ameixa: "#7A1F6A", floresta: "#14523A", dourada: "#A8700A", nanquim: "#0B0B10", terminal: "#139A44", "rosa-choque": "#E0177A", laranja: "#E0661A", turquesa: "#0E9AA0" };
  const CHEEKS = { rosa: CHEEK, pessego: "#FFB98A", lilas: "#C9A6F4", coral: "#FF8C75", nenhuma: null };
  const SLOTS = ["cover", "pages", "ink", "cheeks", "ribbon", "gloves", "hat", "eyes", "neck", "hand", "back", "aura", "pet"];
  const LOOK = { cover: "indigo", pages: "lisa", ink: "marinho", cheeks: "rosa", ribbon: "menta", hat: "capelo", eyes: "nenhum", neck: "nenhum", hand: "nenhum", back: "nada", aura: "nenhum", gloves: "branca", pet: "nenhum", streak: 0, atRisk: false, hour: null, bond: 0, name: "", bondLines: [], tapLines: null, tapMorning: null, tapNight: null, petLines: null, backLines: null };
  // Escolhas antigas (um "acessório" só) viram a peça equivalente.
  const LEGACY_ACC = { oculos: ["eyes", "leitura"], gravata: ["neck", "borboleta"], estrela: ["neck", "broche"], louros: ["hat", "louros"], lapis: ["hand", "lapis"] };
  function resolveLook(el, over) {
    const a = (n) => (over && over[n]) || (el && el.getAttribute(n)) || LOOK[n];
    const h = LOOK.hour != null ? LOOK.hour : new Date().getHours();
    const plain = !!(over && over.plain) || (el && el.hasAttribute("plain"));   // vitrine: só as peças, sem medalha nem hora do dia
    const cover = COVERS[a("cover")] || COVERS.indigo;
    const scene = !plain && !(el && el.hasAttribute("noscene"));   // noscene: sem café nem touca (vitrine do armário)
    return {
      cover, coverId: a("cover"), ribbon: RIBBONS[a("ribbon")] || RIBBONS.menta, ribbonPat: RIBBON_PAT[a("ribbon")], hat: a("hat"), eyes: a("eyes"), neck: a("neck"), hand: a("hand"),
      aura: a("aura"), back: a("back"), gloves: GLOVES[a("gloves")] || GLOVES.branca, arm: cover.arm, pet: a("pet"),
      pages: a("pages"), ink: INKS[a("ink")] || INK, cheek: a("cheeks") in CHEEKS ? CHEEKS[a("cheeks")] : CHEEK,
      streak: plain ? 0 : LOOK.streak, atRisk: !plain && LOOK.atRisk,
      morning: scene && h >= 5 && h < 11, night: scene && (h >= 22 || h < 5),
    };
  }
  const sstep = (x) => { x = clamp(x, 0, 1); return x * x * (3 - 2 * x); };

  // ------------------------------------------------------------------ cor e formas básicas
  function hex(c) { const n = parseInt(c.slice(1), 16); return [(n >> 16) & 255, (n >> 8) & 255, n & 255]; }
  function rgba(c, a) { const [r, g, b] = hex(c); return `rgba(${r},${g},${b},${a})`; }
  function lerpColor(a, b, f) { const x = hex(a), y = hex(b), m = (i) => Math.round(x[i] + (y[i] - x[i]) * f); return `rgb(${m(0)},${m(1)},${m(2)})`; }
  const clamp = (v, lo, hi) => Math.max(lo, Math.min(hi, v));
  const lerp = (a, b, f) => a + (b - a) * f;

  const GRAIN = (function () {
    let s = 7;
    const rnd = () => { s = (s * 1103515245 + 12345) & 0x7fffffff; return s / 0x7fffffff; };
    const out = [];
    for (let i = 0; i < 80; i++) {
      const side = i % 2 === 0 ? -1 : 1;
      out.push({ x: 50 + side * (3 + rnd() * 32), y: 44 + rnd() * 36, r: 0.15 + rnd() * 0.22, a: 0.05 + rnd() * 0.08, long: Math.floor(rnd() * 4) === 0 });
    }
    return out;
  })();

  function page(side, drop = 0, spread = 0) {
    const x = (v) => 50 + side * (v + spread * (v / 38));
    const p = new Path2D();
    p.moveTo(x(0), 44 + drop * 0.2);
    p.bezierCurveTo(x(10), 37.6, x(26), 36.4, x(38), 40.4 + drop);
    p.lineTo(x(38), 84.4 + drop);
    p.bezierCurveTo(x(26), 80.6 + drop, x(10), 81.4 + drop, x(0), 87 + drop);
    p.closePath();
    return p;
  }
  function pageBlock(side, depth) {
    const outer = 50 + side * 38, outerLow = 50 + side * (38 + depth * 0.45);
    const p = new Path2D();
    p.moveTo(outer, 40.4); p.lineTo(outer, 84.4);
    p.bezierCurveTo(50 + side * 26, 80.6, 50 + side * 10, 81.4, 50, 87);
    p.lineTo(50, 87 + depth);
    p.bezierCurveTo(50 + side * 10, 81.4 + depth, 50 + side * 26.5, 80.6 + depth, outerLow, 84.4 + depth);
    p.lineTo(outerLow, 41.6 + depth * 0.3);
    p.closePath();
    return p;
  }
  function coverPath() {
    const c = new Path2D();
    c.moveTo(8.5, 43.5); c.bezierCurveTo(22, 40.5, 38, 41.5, 50, 47.5); c.bezierCurveTo(62, 41.5, 78, 40.5, 91.5, 43.5);
    c.lineTo(91.5, 89); c.bezierCurveTo(78, 86.4, 62, 87.4, 50, 93); c.bezierCurveTo(38, 87.4, 22, 86.4, 8.5, 89); c.closePath();
    return c;
  }
  function vGrad(g, stops, y0, y1) { const gr = g.createLinearGradient(0, y0, 0, y1); stops.forEach((c, i) => gr.addColorStop(Array.isArray(c) ? c[0] : i / (stops.length - 1), Array.isArray(c) ? c[1] : c)); return gr; }
  function hGrad(g, stops, x0, x1) { const gr = g.createLinearGradient(x0, 0, x1, 0); stops.forEach((c, i) => gr.addColorStop(Array.isArray(c) ? c[0] : i / (stops.length - 1), Array.isArray(c) ? c[1] : c)); return gr; }
  function line(g, color, x0, y0, x1, y1, w, round = true) { g.beginPath(); g.moveTo(x0, y0); g.lineTo(x1, y1); g.strokeStyle = color; g.lineWidth = w; g.lineCap = round ? "round" : "butt"; g.stroke(); }
  function stroke(g, path, color, w) { g.strokeStyle = color; g.lineWidth = w; g.lineCap = "round"; g.lineJoin = "round"; g.stroke(path); }
  function oval(g, color, x, y, w, h) { g.beginPath(); g.ellipse(x + w / 2, y + h / 2, Math.max(w / 2, 0.01), Math.max(h / 2, 0.01), 0, 0, 2 * PI); g.fillStyle = color; g.fill(); }
  function circle(g, color, r, x, y) { g.beginPath(); g.arc(x, y, Math.max(r, 0.01), 0, 2 * PI); g.fillStyle = color; g.fill(); }

  // ------------------------------------------------------------------ folha virando (pensando)
  const TURN_SHARE = 0.32, TURN_CYCLE = 2.4;
  function turnState(t) {
    const cycle = (t % TURN_CYCLE) / TURN_CYCLE, raw = clamp(cycle / TURN_SHARE, 0, 1);
    const p = raw * raw * (3 - 2 * raw), c = cos(PI * p), lift = sin(PI * p);
    return { cycle, raw, p, c, lift, edgeX: 50 + 38 * c, up: lift * 6, turning: raw > 0 && raw < 1 };
  }

  // ------------------------------------------------------------------ poses
  // Valores numéricos vão por mola de uma pose para outra; a forma da mão troca com um "pop".
  const REST_L = [4, 78], REST_R = [96, 78];
  // ctx: tm = segundos desde que o humor começou (cenas com começo, meio e fim); worried = sequência em risco.
  function pose(mood, t, look, fid, ctx) {
    const tm = ctx && ctx.tm != null ? ctx.tm : 9;
    const p = {
      hop: sin(t * 2.1) * 1.4, tilt: sin(t * 0.9) * 1.2,
      hL: [REST_L[0], REST_L[1] + sin(t * 2.1) * 1.2], hR: [REST_R[0], REST_R[1] - sin(t * 2.1) * 1.2],
      rotL: 0, rotR: 0, bendL: 2, bendR: 2, shapeL: "mitt", shapeR: "mitt",
      up: 0, low: 0, droop: 0, eyeScale: 1, pupil: 1, lookX: look.x, lookY: look.y,
      browY: 0, browIn: 0, browR: 0, browShow: 1,
      mW: 5, mCurve: 3.4, mOpen: 0, cheek: 0.45, capRot: 0, tongue: 0,
    };
    const env = (u) => { u = clamp(u, 0, 1); return sin(PI * u) ** 0.7; };
    // sequência em risco (fim do dia, meta ainda não batida): parado, fica preocupadinho
    if (mood === "idle" && ctx && ctx.worried) { p.browIn = 0.6; p.browY = 0.3; p.mW = 3.8; p.mCurve = 0.6; p.cheek = 0.35; }
    // manias do parado: depois de um tempo quieto ele faz alguma coisa sozinho
    if (mood === "idle" && fid) {
      const e = env(fid.u);
      if (fid.kind === "cap") {          // ajeita o capelo
        p.hR = [lerp(p.hR[0], 74, e), lerp(p.hR[1], 34, e)]; p.shapeR = e > 0.3 ? "open" : p.shapeR; p.rotR = -40 * e; p.bendR = lerp(2, -6, e);
        p.capRot = 7 * sin(PI * clamp((fid.u - 0.3) / 0.5, 0, 1)); p.lookX = 0.7 * e; p.lookY = -0.6 * e; p.browR = 1.2 * e;
      } else if (fid.kind === "yawn") {  // espreguiça e boceja
        p.hL = [lerp(p.hL[0], 8, e), lerp(p.hL[1], 40, e)]; p.hR = [lerp(p.hR[0], 92, e), lerp(p.hR[1], 40, e)];
        p.shapeL = p.shapeR = e > 0.3 ? "fist" : "mitt"; p.bendL = p.bendR = -5 * e; p.hop = -1.6 * e;
        p.up = 0.95 * clamp((fid.u - 0.2) / 0.3, 0, 1) * (1 - clamp((fid.u - 0.8) / 0.15, 0, 1));
        p.mOpen = 1.1 * e; p.mW = lerp(5, 3.2, e); p.mCurve = lerp(3.4, 0.4, e); p.browY = -1.6 * e;
      } else if (fid.kind === "glance") { // olha de lado, desconfiado
        const side = fid.side || 1; p.lookX = side * e; p.lookY = -0.1; p.up = 0.25 * e; p.browR = 1.4 * e * (side > 0 ? 1 : 0); p.browY = -0.4 * e; p.mW = lerp(5, 3.6, e); p.mCurve = lerp(3.4, 1, e);
      } else if (fid.kind === "read") {   // lê a própria página: os olhos correm a linha
        p.lookY = lerp(p.lookY, 0.85, e); p.lookX = e * sin(fid.u * 14) * 0.8; p.up = 0.3 * e; p.browY = 0.4 * e;
        p.mW = lerp(5, 3.4, e); p.mCurve = lerp(3.4, 1.4, e); p.tilt += 1.5 * e;
      } else if (fid.kind === "whistle") { // assobia olhando para cima, balançando no ritmo
        p.mW = lerp(5, 1.8, e); p.mCurve = lerp(3.4, 0, e); p.mOpen = 0.55 * e; p.lookX = 0.5 * e; p.lookY = -0.6 * e; p.browY = -0.8 * e;
        p.tilt += sin(fid.u * 18) * 2.2 * e; p.hop = -abs(sin(fid.u * 9)) * 1.2 * e;
      } else if (fid.kind === "peek") {  // inclina para o lado e dá um tchauzinho para você
        const side = fid.side || 1, wv = sin(t * 13) * e;
        p.tilt = side * 7 * e; p.hop = -1.2 * e; p.low = 0.4 * e; p.mW = 5.6; p.mCurve = 4.4; p.mOpen = 0.4 * e; p.cheek = lerp(0.45, 0.65, e); p.lookX = side * 0.8 * e; p.lookY = -0.2 * e;
        if (side > 0) { p.hR = [lerp(p.hR[0], 100, e), lerp(p.hR[1], 32, e) + wv * 1.4]; if (e > 0.3) p.shapeR = "open"; p.rotR = (-10 + 24 * sin(t * 13)) * e; p.bendR = -5 * e; }
        else { p.hL = [lerp(p.hL[0], 0, e), lerp(p.hL[1], 32, e) + wv * 1.4]; if (e > 0.3) p.shapeL = "open"; p.rotL = (10 - 24 * sin(t * 13)) * e; p.bendL = -5 * e; }
      } else if (fid.kind === "hatflip") { // joga o chapéu para cima e pega (o chapéu voa em _frame)
        const k = sin(PI * fid.u);
        p.lookY = -k; p.lookX = 0; p.mOpen = 0.5 * k; p.mW = lerp(5, 3.4, k); p.mCurve = lerp(3.4, 2, k); p.browY = -1.6 * k;
        p.hR = [lerp(p.hR[0], 88, k), lerp(p.hR[1], 42, k)]; if (k > 0.3) p.shapeR = "open"; p.rotR = -30 * k; p.bendR = -4 * k;
      } else if (fid.kind === "bounce") { // pulinho de animação
        p.hop = -3.2 * abs(sin(PI * fid.u * 2)); p.low = 0.45 * e; p.mOpen = 0.6 * e; p.mW = 5.6; p.mCurve = 4.4; p.cheek = lerp(0.45, 0.7, e);
        p.hL = [lerp(p.hL[0], 4, e), lerp(p.hL[1], 58, e)]; p.hR = [lerp(p.hR[0], 96, e), lerp(p.hR[1], 58, e)];
      }
    }
    if (mood === "happy") {
      const pump = abs(sin(t * 5.2)) * 4;
      p.hop = -abs(sin(t * 5.2)) * 5; p.tilt = sin(t * 2.6) * 2;
      p.hL = [2, 46 - pump]; p.hR = [98, 46 - pump]; p.shapeL = p.shapeR = "fist"; p.rotL = -18; p.rotR = 18; p.bendL = p.bendR = -4;
      p.low = 0.64; p.pupil = 1.25; p.browY = -2.2; p.mW = 6.4; p.mCurve = 5; p.mOpen = 1; p.cheek = 0.75;
    } else if (mood === "wave") {
      const wv = sin(t * 13);
      p.hR = [100 + wv * 2, 30 + wv * 1.2]; p.shapeR = "open"; p.rotR = -10 + wv * 26; p.bendR = -5;
      p.low = 0.32; p.browY = -1.4; p.mW = 5.6; p.mCurve = 4.4; p.mOpen = 0.55; p.cheek = 0.6; p.tilt = -1.5 + wv * 0.6;
    } else if (mood === "shh") {
      // pedindo silêncio: o dedo em pé na frente da boca (a mão vem por cima do livro)
      p.hR = [54, 76]; p.shapeR = "point"; p.rotR = 140; p.bendR = 8;
      p.mW = 2; p.mCurve = 0.1; p.mOpen = 0.25; p.browY = -0.5; p.browIn = 0.3; p.cheek = 0.6; p.tilt = sin(t * 2) * 1.5;
    } else if (mood === "aim") {
      // mirando lá em cima com o braço esticado (para jogar a chama na fênix)
      p.hR = [98, 16]; p.shapeR = "point"; p.rotR = -115; p.bendR = -6;
      p.lookX = -0.3; p.lookY = -1; p.mW = 3.4; p.mCurve = 1.6; p.mOpen = 0.3; p.browY = -0.9; p.tilt = -3;
    } else if (mood === "point") {
      p.hR = [106, 60 + sin(t * 4) * 1.2]; p.shapeR = "point"; p.rotR = -6; p.bendR = -2; p.tilt = -2;
      p.lookX = 1; p.lookY = 0; p.browY = -1.2; p.browR = 1.2; p.mW = 5; p.mCurve = 4; p.mOpen = 0.25;
    } else if (mood === "talking") {
      const wv = sin(t * 7) * 5, beat = sin(t * 3.4);
      p.tilt = beat * 3; p.hL = [3, 76]; p.hR = [97 + wv * 0.3, 52 + wv]; p.shapeR = "open"; p.rotR = beat * 18; p.bendR = -3;
      p.browY = -0.8 * Math.max(0, sin(t * 2.3)); p.mW = 4.6; p.mCurve = 2.4;
    } else if (mood === "thinking") {
      p.tilt = sin(t * 1.3) * 2; p.hL = [4, 76 + sin(t * 1.5)]; p.hR = [93, 70 + sin(t * 2.2) * 1.5]; p.shapeR = "fist"; p.bendR = 3;
      p.up = 0.15; p.browR = 1.8; p.browY = -0.4; p.mW = 3.4; p.mCurve = 1.2; p.mOpen = 0;
      // folheando: a mão direita vai até o canto da página, pega e empurra até passar da costura; a esquerda
      // recebe a folha do outro lado e alisa
      const T = turnState(t), sm = (x) => { x = clamp(x, 0, 1); return x * x * (3 - 2 * x); };
      const grab = T.cycle > 0.86 ? sm((T.cycle - 0.86) / 0.14) : T.turning ? 1 - sm((T.p - 0.5) / 0.22) : 0;
      if (grab > 0.001) {
        const ex = T.turning ? T.edgeX : 88, ey = 80 - (T.turning ? T.up * 0.8 : 0);
        p.hR = [lerp(p.hR[0], ex + 1.5, grab), lerp(p.hR[1], ey, grab)]; p.shapeR = "open"; p.rotR = lerp(0, 150, grab); p.bendR = lerp(3, -6, grab);
      }
      const catchK = T.turning ? sm((T.p - 0.62) / 0.2) : 0;
      if (catchK > 0.001) { p.hL = [lerp(p.hL[0], T.edgeX - 1.5, catchK), lerp(p.hL[1], 80 - T.up * 0.8, catchK)]; p.shapeL = "open"; p.rotL = lerp(0, -150, catchK); p.bendL = -6; }
      if (T.turning) { p.lookX = clamp((T.edgeX - 50) / 30, -1, 1); p.lookY = 0.4; p.mOpen = 0.3 * T.lift; p.mW = 3 + T.lift; p.up = 0; p.browR = 0.6; }
    } else if (mood === "surprised") {
      p.hop = -2; p.hL = [18, 72]; p.hR = [82, 72]; p.shapeL = p.shapeR = "open"; p.rotL = 60; p.rotR = -60; p.bendL = p.bendR = -4;
      p.eyeScale = 1.22; p.pupil = 1.45; p.browY = -3.6; p.mOpen = 0.9; p.mW = 3.2; p.mCurve = 0; p.cheek = 0.55;
    } else if (mood === "proud") {
      p.hop = -1.2 + sin(t * 2) * 0.5; p.tilt = -2 + sin(t * 0.9) * 0.6;
      p.hL = [9, 74]; p.hR = [91, 74]; p.shapeL = p.shapeR = "fist"; p.rotL = 150; p.rotR = -150; p.bendL = p.bendR = 6;
      p.up = 0.38; p.browY = -1.8; p.browR = 0.8; p.mW = 5.6; p.mCurve = 4.4; p.cheek = 0.65; p.lookX = -0.2; p.lookY = -0.4;
    } else if (mood === "focus") {
      const scr = sin(t * 9), sc2 = cos(t * 4.6);
      p.tilt = 1.5; p.hop = 0.6 + sin(t * 1.6) * 0.4;
      p.hR = [66 + scr * 4.5 + sin(t * 0.9) * 3, 76 + sc2 * 0.9]; p.shapeR = "pen"; p.rotR = 0; p.bendR = -4;
      p.up = 0.28; p.lookX = 0.55 + scr * 0.15; p.lookY = 0.85; p.browIn = -0.55; p.browY = 0.8;
      p.mW = 2.8; p.mCurve = 0.4; p.tongue = 1;
    } else if (mood === "sleepy") {
      p.hop = 1.4 + sin(t * 1.1) * 1.2; p.tilt = 3.5 + sin(t * 0.55) * 1.2;
      p.hL = [7, 84]; p.hR = [93, 84]; p.up = 1; p.browY = 1; p.mOpen = 0.18 + 0.12 * (0.5 + 0.5 * sin(t * 1.1)); p.mW = 2.4; p.mCurve = 0; p.cheek = 0.35;
    } else if (mood === "dizzy") {
      p.tilt = sin(t * 6) * 7; p.hop = sin(t * 3) * 1.2; p.hL = [5 + sin(t * 6) * 3, 72]; p.hR = [95 + sin(t * 6 + 1) * 3, 72];
      p.mW = 4.4; p.mCurve = 0; p.mOpen = 0; p.browY = -1; p.cheek = 0.6;
    } else if (mood === "sad") {
      p.hop = 1.6 + sin(t * 1.4) * 0.6; p.tilt = sin(t * 0.8) * 0.8;
      p.hL = [6, 85]; p.hR = [94, 85]; p.rotL = 30; p.rotR = -30; p.bendL = p.bendR = 4;
      p.up = 0.42; p.droop = 1; p.pupil = 0.9; p.lookY = 0.9;
      p.browIn = 1; p.browY = 0.6; p.mW = 4.6; p.mCurve = -3.4; p.cheek = 0.3;
    } else if (mood === "relieved") {
      // "Ufa!": de olhos fechados, passa a mão na testa e solta o ar; depois relaxa num sorriso
      const w = 1 - sstep((tm - 2.1) / 0.6), sw = 0.5 + 0.5 * sin(tm * 4.4 - 1.2);
      p.hop = lerp(-0.6, 1.4, w) + sin(t * 2) * 0.4; p.tilt = lerp(-1.5, 2.5, w);
      p.hR = [lerp(96, lerp(80, 60, sw), w), lerp(76, 50, w)]; p.shapeR = w > 0.4 ? "open" : "mitt"; p.rotR = -95 * w; p.bendR = lerp(2, -7, w);
      p.hL = [lerp(5, 8, w), lerp(76, 82, w)];
      p.up = lerp(0, 1, w); p.low = lerp(0.5, 0, w); p.browIn = 0.45 * w; p.browY = lerp(-1.2, -0.4, w);
      p.mW = lerp(5.4, 2.3, w); p.mCurve = lerp(4.2, 0, w); p.mOpen = lerp(0.3, 0.45, w); p.cheek = lerp(0.65, 0.55, w);
    } else if (mood === "present") {
      // mostrando o que tem nas mãos: a esquerda levanta o item para a frente, a direita abre a luva
      const b = sin(t * 3);
      p.hop = -1 + sin(t * 2) * 0.6; p.tilt = -2 + b * 1.2;
      p.hL = [4, 45 + b * 1.2]; p.shapeL = "fist"; p.rotL = -10; p.bendL = -4;
      p.hR = [96, 46 - b * 1.2]; p.shapeR = "open"; p.rotR = -20 + b * 12; p.bendR = -4;
      p.low = 0.4; p.browY = -1.6; p.mW = 5.8; p.mCurve = 4.6; p.mOpen = 0.5; p.cheek = 0.7; p.lookX = -0.5; p.lookY = -0.3;
    } else if (mood === "ecstatic") {
      // em chamas (muitos acertos seguidos): olhos de estrela, pulos altos e socos no ar alternados
      const a = sin(t * 6.2);
      p.hop = -abs(a) * 7; p.tilt = sin(t * 3.1) * 3;
      p.hL = [0, 40 - Math.max(0, a) * 7]; p.hR = [100, 40 - Math.max(0, -a) * 7]; p.shapeL = p.shapeR = "fist"; p.rotL = -24; p.rotR = 24; p.bendL = p.bendR = -6;
      p.pupil = 1.3; p.browY = -3; p.mW = 6.8; p.mCurve = 5.4; p.mOpen = 1.15; p.cheek = 0.85;
    }
    // segurando um item: a mão esquerda parada sobe um pouco para a frente, fechada, para o item aparecer
    // no susto, segurando um item: a mão do item se encolhe junto ao peito (sem soltar) e só a outra vai ao rosto
    if (ctx && ctx.holding && mood === "surprised") { p.hL = [10, 68]; p.shapeL = "fist"; p.rotL = -20; p.bendL = 6; }
    if (ctx && ctx.holding && p.hL[0] < 10 && p.hL[1] > 73) { p.hL = [11, 71 + (p.hL[1] - 78) * 0.5]; p.rotL = -12; p.bendL = 4; if (p.shapeL === "mitt") p.shapeL = "fist"; }
    return p;
  }

  // ------------------------------------------------------------------ mola
  function springTo(st, key, target, dt, k) {
    const z = 0.62, v = st.v[key] || 0, x = st.x[key];
    const a = k * (target - x) - 2 * z * Math.sqrt(k) * v;
    st.v[key] = v + a * dt; st.x[key] = x + st.v[key] * dt;
  }
  const FAST = new Set(["hop", "tilt", "lookX", "lookY"]);
  const SLOW = new Set(["hL0", "hL1", "hR0", "hR1", "rotL", "rotR", "bendL", "bendR"]);

  // ------------------------------------------------------------------ desenho
  function drawFolha(g, s) {
    const { t, mood, P, small } = s;
    const hop = P.hop, tilt = P.tilt;
    const squashX = 1 + 0.10 * s.squash, squashY = 1 - 0.12 * s.squash;
    const stretch = 1 + clamp(abs(s.vy) * 0.006, 0, 0.07);

    // sombra: menor quando ele sobe
    const lift = Math.max(-hop, 0);
    g.save();
    g.translate(50, 95); g.scale(1, 0.18); g.translate(-50, -95);
    const r1 = 36 - lift * 1.6;
    let rg = g.createRadialGradient(50, 95, 0, 50, 95, r1); rg.addColorStop(0, rgba(INK, 0.28)); rg.addColorStop(1, rgba(INK, 0));
    circle(g, rg, r1, 50, 95);
    rg = g.createRadialGradient(50, 95, 0, 50, 95, 26); rg.addColorStop(0, rgba(INK, 0.30 * clamp(1 - lift / 6, 0.3, 1))); rg.addColorStop(1, rgba(INK, 0));
    circle(g, rg, 26, 50, 95);
    g.restore();

    const lookP = s.look || resolveLook(null);
    drawFloorAura(g, t, lookP.aura, small);
    g.save();
    g.translate(0, hop);
    g.translate(50, 88); g.scale(squashX / Math.sqrt(stretch), squashY * stretch); g.translate(-50, -88);
    g.translate(50, 80); g.rotate(tilt * PI / 180); g.translate(-50, -80);
    const ex = s.ex || {};
    // girando no lugar (reação ao toque): o corpo achata e vira, como em 3D
    if (ex.turn != null && ex.turn !== 1) { g.translate(50, 0); g.scale(Math.abs(ex.turn) < 0.04 ? 0.04 * Math.sign(ex.turn || 1) : ex.turn, 1); g.translate(-50, 0); }
    // quem fica na frente do livro: as duas mãos folheando ou com a cara de susto; só a do lápis no foco
    const look = s.look || resolveLook(null);
    drawAura(g, t, look.aura, small, s.burst || 0, true);
    drawBack(g, t, look.back, mood, small, P.hop);
    // na frente do livro: as duas mãos folheando, no susto ou passando na testa (ufa); só a do lápis no foco
    let front = mood === "thinking" || mood === "surprised" || mood === "present" ? "LR" : mood === "shh" ? "R" : mood === "focus" || mood === "relieved" ? "R" : "";
    // segurando um item: a mão esquerda fica na frente do livro, para o item aparecer
    if (look.hand && look.hand !== "nenhum" && !front.includes("L")) front = "L" + front;
    if (ex.armsFront) front = "LR";
    drawArms(g, s, "LR".replace(new RegExp("[" + (front || "_") + "]", "g"), ""));
    drawBook(g, t, mood, small, look);
    drawPageLines(g, t, mood, small, look.ink);
    if (!small) drawDogEar(g, t, mood);
    if (mood === "thinking") drawTurningPage(g, t);
    drawFace(g, s);
    drawEyewear(g, P, small, look.eyes, t);
    drawNeckwear(g, t, small, look.neck);
    // à noite, parado ou cochilando, a touca entra no lugar do capelo (outro chapéu escolhido fica)
    const nightcap = look.night && look.hat === "capelo" && (mood === "idle" || mood === "sleepy");
    // no pulo de comemoração o chapéu sai um pouco da cabeça e volta (atrasado em relação ao corpo)
    const party = mood === "happy" || mood === "ecstatic", fly = party ? Math.max(0, sin(t * 5.2 - 0.9)) ** 2 : 0;
    g.save(); g.translate(50, 38 - fly * (mood === "ecstatic" ? 6 : 4) - (ex.hatUp || 0)); g.rotate(((P.capRot || 0) + fly * 8 * sin(t * 2.6) + (ex.hatSpin || 0)) * PI / 180); g.translate(-50, -38);
    if (nightcap) drawNightcap(g, t, tilt, s.vy); else drawHat(g, s, look, tilt);
    g.restore();
    if (front) drawArms(g, s, front);
    if (mood === "relieved" && !small) drawRelief(g, s.tm || 0);
    if (look.atRisk && mood === "idle" && !small) drawSweat(g, t);
    g.restore();
    // o companheiro na frente de tudo o que fica atrás do Folha (mochila, manto, toga...)
    drawPet(g, t, lookP.pet, mood, small, s.burst || 0);
    if (!small && look.morning && /^(idle|talking|thinking|focus|wave|point|proud)$/.test(mood)) drawMug(g, t);
    if (!small && look.night && /^(idle|sleepy|focus|thinking)$/.test(mood)) drawMoon(g, t);
    if (mood === "happy") drawCelebration(g, t, small);
    if (mood === "ecstatic") { drawFlames(g, t, small); drawCelebration(g, t, small); }
    if (mood === "proud") drawSparkles(g, t, small);
    drawAura(g, t, look.aura, small, s.burst || 0, false);
    if (look.aura === "holofote") drawSpotLamps(g, t);
    drawBurst(g, s.burst || 0);
    if (s.ex) drawLife(g, t, s.ex, small);
    if (mood === "sleepy") drawZzz(g, t, small);
    if (mood === "dizzy") drawDizzyStars(g, t);
    if (mood === "focus" && !small) drawFocusMarks(g, t);
  }

  function drawCorners(g, metal) {
    const stops = metal === "silver" ? ["#FFFFFF", "#CDD0DC", "#8E92A6"] : ["#FFE3A0", GOLD, "#C98A12"];
    const corner = (x, y, sx, sy) => {
      const p = new Path2D();
      p.moveTo(x, y + sy * 6.5); p.lineTo(x, y + sy * 1.2); p.quadraticCurveTo(x, y, x + sx * 1.2, y); p.lineTo(x + sx * 6.5, y);
      p.lineTo(x + sx * 6.5, y + sy * 1.9); p.lineTo(x + sx * 1.9, y + sy * 1.9); p.lineTo(x + sx * 1.9, y + sy * 6.5); p.closePath();
      g.fillStyle = vGrad(g, stops, Math.min(y, y + sy * 6.5), Math.max(y, y + sy * 6.5)); g.fill(p);
      g.strokeStyle = "rgba(120,70,0,0.35)"; g.lineWidth = 0.3; g.stroke(p);
      circle(g, "rgba(255,255,255,0.75)", 0.45, x + sx * 0.95, y + sy * 0.95);
    };
    corner(8.5, 43.5, 1, 1); corner(91.5, 43.5, -1, 1); corner(8.5, 89, 1, -1); corner(91.5, 89, -1, -1);
  }
  function drawDogEar(g, t, mood) {
    const k = mood === "thinking" ? 0.7 + 0.3 * abs(sin(t * 1.2)) : 1;
    const x = 87.7, y = 84.4, d = 5 * k;
    const under = new Path2D(); under.moveTo(x - d, y + 0.1); under.lineTo(x + 0.2, y - d - 0.1); under.lineTo(x + 0.2, y + 0.1); under.closePath();
    g.fillStyle = PAPER_EDGE; g.fill(under);
    const fold = new Path2D(); fold.moveTo(x - d, y + 0.1); fold.lineTo(x + 0.2, y - d - 0.1); fold.lineTo(x - d * 0.8, y - d * 0.78); fold.closePath();
    g.save(); g.shadowColor = "rgba(38,33,92,0.25)"; g.shadowBlur = 1.2; g.shadowOffsetX = -0.4; g.shadowOffsetY = 0.4;
    g.fillStyle = hGrad(g, ["#FFFFFF", "#ECE6DA"], x - d, x); g.fill(fold); g.restore();
    stroke(g, fold, rgba(GUTTER, 0.18), 0.25);
  }

  // Padrão do papel (pautado, quadriculado, pontilhado, partitura), bem leve, por baixo do rosto.
  // Papéis coloridos: o claro e a borda um pouco mais forte.
  const PAPERS = { creme: ["rgba(255,240,205,0.6)", "rgba(240,215,165,0.7)"], rosa: ["rgba(255,218,230,0.6)", "rgba(250,190,210,0.7)"], menta: ["rgba(210,247,230,0.6)", "rgba(170,230,205,0.7)"], ceu: ["rgba(214,234,255,0.65)", "rgba(175,210,250,0.75)"], lavanda: ["rgba(232,222,255,0.62)", "rgba(205,188,250,0.72)"], kraft: ["rgba(214,176,128,0.62)", "rgba(190,145,95,0.72)"] };
  // Papéis do armário 2.0 (um lado por vez, já recortado na página; side -1 = esquerda). Tudo bem leve,
  // concentrado no alto e no pé da página, para não brigar com o rosto no meio.
  const PAGES2 = new Set(["receituario", "mapa", "boletim", "timbrado", "livro-caixa", "planilha", "extrato", "diario-oficial", "caligrafia", "terminal", "formulario"]);
  function drawPagePattern2(g, kind, page, side, t) {
    const X = (v) => 50 + side * v;   // v = distância da lombada (0 no meio do livro, 38 na borda)
    const lo = Math.min(X(4), X(35)), hi = Math.max(X(4), X(35));
    const txt = (s, x, y, size, color, weight = 700, font = "Georgia, serif") => { g.save(); g.fillStyle = color; g.font = `${weight} ${size}px ${font}`; g.textAlign = "center"; g.textBaseline = "middle"; g.fillText(s, x, y); g.restore(); };
    if (kind === "receituario") {
      // receituário: faixa verde-água no alto, o "Rx" na esquerda e a linha da assinatura com carimbo na direita
      g.fillStyle = "rgba(60,170,160,0.22)"; g.fillRect(10, 36, 80, 8.6); line(g, "rgba(40,140,130,0.55)", lo, 44.6, hi, 44.6, 0.3, false);
      if (side < 0) { txt("Rx", X(30), 48.6, 3.4, "rgba(30,120,110,0.75)", 900); for (let y = 78; y < 86; y += 2.6) line(g, "rgba(90,150,200,0.35)", X(34), y, X(6), y + 0.4, 0.22, false); }
      else {
        line(g, "rgba(60,60,90,0.5)", X(8), 80, X(30), 79.4, 0.25, false);
        const sg = new Path2D(); sg.moveTo(X(10), 79.4); sg.bezierCurveTo(X(13), 76.6, X(15), 81, X(18), 78.2); sg.bezierCurveTo(X(20), 76.8, X(22), 79.6, X(26), 78); stroke(g, sg, "rgba(31,79,216,0.5)", 0.3);
        g.beginPath(); g.arc(X(28), 74.6, 2.2, 0, 2 * PI); g.strokeStyle = "rgba(40,140,130,0.4)"; g.lineWidth = 0.35; g.stroke(); line(g, "rgba(40,140,130,0.4)", X(26.6), 74.6, X(29.4), 74.6, 0.3, false);
      }
    } else if (kind === "mapa") {
      // mapa de campanha: papel envelhecido, curvas de nível, rota tracejada e o X do objetivo; rosa dos ventos na direita
      g.fillStyle = "rgba(228,200,140,0.32)"; g.fill(page);
      for (const [cx, cy] of [[X(26), 48], [X(12), 80]]) for (let r = 2; r < 9; r += 2) { g.beginPath(); g.ellipse(cx, cy, r * 1.3, r * 0.8, 0.3, 0, 2 * PI); g.strokeStyle = "rgba(120,140,70,0.32)"; g.lineWidth = 0.22; g.stroke(); }
      g.save(); g.setLineDash([1, 0.8]); const rt = new Path2D(); rt.moveTo(X(34), 84); rt.bezierCurveTo(X(26), 76, X(36), 70, X(30), 62); stroke(g, rt, "rgba(200,50,60,0.5)", 0.35); g.restore();
      if (side < 0) { line(g, "rgba(200,50,60,0.7)", X(31.2), 60.8, X(28.8), 63.2, 0.45); line(g, "rgba(200,50,60,0.7)", X(28.8), 60.8, X(31.2), 63.2, 0.45); }
      else {
        const cx = X(30), cy = 47.4; for (const [a, l] of [[0, 3], [PI / 2, 2.2], [PI, 2.2], [-PI / 2, 2.2]]) { const p = new Path2D(); p.moveTo(cx, cy); p.lineTo(cx + cos(a - 0.3) * 0.8, cy + sin(a - 0.3) * 0.8); p.lineTo(cx + cos(a - PI / 2) * l, cy + sin(a - PI / 2) * l); p.lineTo(cx + cos(a + 0.3) * 0.8, cy + sin(a + 0.3) * 0.8); p.closePath(); g.fillStyle = a === 0 ? "rgba(200,50,60,0.6)" : "rgba(90,70,40,0.5)"; g.fill(p); }
        txt("N", cx, cy - 4, 1.6, "rgba(90,70,40,0.6)");
      }
    } else if (kind === "boletim") {
      // boletim de ocorrência: cabeçalho cinza com brasão, campos em caixinhas e linhas de relato no pé
      g.fillStyle = "rgba(70,90,130,0.18)"; g.fillRect(10, 36, 80, 9); line(g, "rgba(50,70,110,0.55)", lo, 45, hi, 45, 0.35, false);
      if (side < 0) { const sh = new Path2D(); sh.moveTo(X(31), 40.6); sh.lineTo(X(27), 40.6); sh.lineTo(X(27), 43); sh.quadraticCurveTo(X(29), 44.8, X(31), 43); sh.closePath(); g.fillStyle = "rgba(50,70,110,0.45)"; g.fill(sh); }
      else txt("B.O.", X(28), 42.4, 2.2, "rgba(50,70,110,0.6)", 900, "Arial, sans-serif");
      for (const [a, b] of [[34, 22], [21, 6]]) { const r = new Path2D(); r.rect(Math.min(X(a), X(b)), 47, Math.abs(X(a) - X(b)), 3); stroke(g, r, "rgba(50,70,110,0.35)", 0.22); }
      for (let y = 78; y < 87; y += 2.4) line(g, "rgba(50,70,110,0.3)", X(34), y, X(5), y + 0.5, 0.2, false);
    } else if (kind === "timbrado") {
      // papel timbrado: emblema e faixa dupla no alto, rodapé com filete fino
      line(g, "rgba(31,79,216,0.55)", lo, 44.2, hi, 44.2, 0.5, false); line(g, "rgba(245,184,61,0.7)", lo, 45.2, hi, 45.2, 0.25, false);
      if (side < 0) { const eg = g.createRadialGradient(X(30), 40.6, 0.2, X(30), 41, 2.2); eg.addColorStop(0, "rgba(90,130,240,0.6)"); eg.addColorStop(1, "rgba(31,79,216,0.6)"); circle(g, eg, 2, X(30), 41); circle(g, "rgba(255,255,255,0.8)", 0.8, X(30), 41); for (const y of [40.2, 41.8]) line(g, "rgba(31,79,216,0.4)", X(26.6), y, X(18), y, 0.4, false); }
      line(g, "rgba(31,79,216,0.35)", X(34), 81.6, X(6), 82.4, 0.22, false);
      for (const y of [83.4]) line(g, "rgba(31,79,216,0.25)", X(26), y, X(14), y + 0.2, 0.3, false);
    } else if (kind === "livro-caixa") {
      // livro-caixa: papel esverdeado, pauta azul e colunas vermelhas de débito e crédito
      g.fillStyle = "rgba(210,236,214,0.4)"; g.fill(page);
      for (let y = 44; y < 88; y += 2.8) line(g, "rgba(90,150,200,0.4)", 10, y, 90, y + 0.5, 0.2, false);
      for (const v of [6, 11, 33]) { line(g, "rgba(220,70,80,0.5)", X(v), 38, X(v), 90, 0.22, false); line(g, "rgba(220,70,80,0.5)", X(v + 0.6), 38, X(v + 0.6), 90, 0.22, false); }
      if (side > 0) for (const [y, w] of [[47, 3], [52.6, 2.2], [80.6, 3.4]]) line(g, "rgba(40,50,80,0.4)", X(10), y, X(10 - w), y, 0.35, false);
    } else if (kind === "planilha") {
      // planilha: grade cinza, cabeçalho de colunas e linhas em cinza e uma célula selecionada em verde
      for (let y = 38; y < 90; y += 3) line(g, "rgba(120,130,150,0.3)", 10, y, 90, y, 0.18, false);
      for (let v = 2; v < 39; v += 6) line(g, "rgba(120,130,150,0.3)", X(v), 36, X(v), 90, 0.18, false);
      g.fillStyle = "rgba(150,160,180,0.2)"; g.fillRect(10, 36, 80, 5);
      if (side < 0) { g.fillStyle = "rgba(150,160,180,0.2)"; g.fillRect(10, 41, 4, 50); }
      else { const c = new Path2D(); c.rect(X(26), 77, 6, 3); stroke(g, c, "rgba(30,150,80,0.8)", 0.4); g.fillStyle = "rgba(30,150,80,0.85)"; g.fillRect(X(32) - 0.5, 79.5, 1, 1); }
    } else if (kind === "extrato") {
      // extrato bancário: faixas cinza alternadas, descrição à esquerda e valores à direita (um vermelho)
      g.fillStyle = "rgba(31,79,216,0.18)"; g.fillRect(10, 36, 80, 7.6);
      for (let i = 0, y = 45; y < 88; y += 3, i++) {
        if (i % 2 === 0) { g.fillStyle = "rgba(120,130,150,0.1)"; g.fillRect(10, y - 1.2, 80, 3); }
        if (i < 3 || y > 76) {
          line(g, "rgba(60,60,80,0.35)", X(side < 0 ? 34 : 18), y + 0.2, X(side < 0 ? 24 : 12), y + 0.2, 0.4, false);
          if (side > 0) line(g, i % 3 === 1 ? "rgba(200,50,60,0.55)" : "rgba(30,130,80,0.5)", X(33), y + 0.2, X(28), y + 0.2, 0.45, false);
        }
      }
    } else if (kind === "diario-oficial") {
      // diário oficial: cabeçalho com filete grosso e fino, brasão, e colunas de texto miúdo no pé
      line(g, "rgba(20,20,30,0.55)", lo, 44.4, hi, 44.4, 0.6, false); line(g, "rgba(20,20,30,0.4)", lo, 45.4, hi, 45.4, 0.2, false);
      if (side < 0) { circle(g, "rgba(40,110,60,0.45)", 1.8, X(30), 41.4); circle(g, "rgba(245,196,81,0.6)", 0.9, X(30), 41.4); for (const y of [40.6, 42.2]) line(g, "rgba(20,20,30,0.4)", X(27), y, X(10), y, 0.6, false); }
      else txt("DIÁRIO OFICIAL", X(20), 41.6, 1.7, "rgba(20,20,30,0.55)", 900);
      for (const [a, b] of [[34, 21], [19, 6]]) for (let y = 74; y < 88; y += 1.4) line(g, "rgba(40,40,60,0.28)", X(a), y, X(b + ((y * 7) % 3)), y, 0.35, false);
      line(g, "rgba(40,40,60,0.3)", X(20), 72, X(20), 88, 0.15, false);
    } else if (kind === "caligrafia") {
      // caderno de caligrafia: grupos de quatro linhas (a do meio tracejada) e guias inclinadas
      for (const y0 of [42, 76]) {
        for (const [dy, c, dash] of [[0, "rgba(232,70,105,0.4)", false], [2.4, "rgba(90,150,240,0.45)", true], [4.8, "rgba(90,150,240,0.55)", false], [7.2, "rgba(232,70,105,0.4)", false]]) {
          g.save(); if (dash) g.setLineDash([0.8, 0.6]); line(g, c, 10, y0 + dy, 90, y0 + dy + 0.8, 0.22, false); g.restore();
        }
        for (let x = 12; x < 90; x += 4) line(g, "rgba(90,150,240,0.2)", x, y0 + 7.6, x + 2.4, y0 - 0.2, 0.18, false);
      }
      if (side < 0) { const a = new Path2D(); a.moveTo(X(32), 81); a.bezierCurveTo(X(33), 78, X(30), 77.6, X(30.4), 80.6); a.bezierCurveTo(X(30), 81.4, X(29), 80, X(28.4), 77.2); a.lineTo(X(27.6), 81); stroke(g, a, "rgba(31,79,216,0.5)", 0.3); }
    } else if (kind === "terminal") {
      // terminal: fundo escuro (mais claro no meio, onde fica o rosto), linhas de comando verdes e o cursor piscando
      const bg = g.createRadialGradient(50, 64, 6, 50, 64, 44); bg.addColorStop(0, "rgba(40,64,50,0.55)"); bg.addColorStop(1, "rgba(10,18,14,0.88)"); g.fillStyle = bg; g.fill(page);
      if (side < 0) for (const [c, x] of [["#FF5F57", 31], ["#FEBC2E", 29.4], ["#28C840", 27.8]]) circle(g, c, 0.55, X(x), 42.6);
      const gr = "rgba(80,240,130,0.7)";
      for (const [y, a, b] of side < 0 ? [[47, 33, 26], [49.6, 33, 18], [78.6, 33, 22], [81.2, 33, 28]] : [[47, 6, 20], [78.6, 6, 26], [81.2, 6, 14]]) {
        line(g, gr, X(a), y, X(b), y, 0.55, false);
        if (side < 0 && a === 33) txt(">", X(34.6), y, 1.8, gr, 900, "monospace");
      }
      if (side > 0 && (t % 1.1) < 0.6) { g.fillStyle = gr; g.fillRect(X(16) - 0.5, 80.2, 1.2, 1.8); }
    } else if (kind === "formulario") {
      // formulário: campos com rótulo e linha, caixinhas de marcar (uma marcada) no pé
      g.fillStyle = "rgba(120,130,160,0.14)"; g.fillRect(10, 36, 80, 8.4);
      for (const y of [47.4, 50.6]) { line(g, "rgba(60,70,100,0.4)", X(34), y - 0.8, X(30), y - 0.8, 0.35, false); line(g, "rgba(60,70,100,0.35)", X(29), y, X(6), y, 0.2, false); }
      for (const [i, y] of [[0, 78], [1, 81.4], [2, 84.8]]) {
        const bx = Math.min(X(33), X(31)), b = new Path2D(); b.rect(bx, y - 1, 2, 2); stroke(g, b, "rgba(60,70,100,0.5)", 0.25);
        line(g, "rgba(60,70,100,0.3)", X(29.4), y, X(side < 0 ? 18 : 20), y, 0.35, false);
        if (i === (side < 0 ? 0 : 1)) { const c = new Path2D(); c.moveTo(bx + 0.4, y); c.lineTo(bx + 0.9, y + 0.6); c.lineTo(bx + 1.7, y - 0.8); stroke(g, c, "rgba(31,79,216,0.8)", 0.35); }
      }
    }
  }
  // O papel das páginas: pauta, quadriculado, pontilhado e papéis coloridos, sem rabisco na cara dele.
  function drawPagePattern(g, kind, left, right, ink, t) {
    if (!kind || kind === "lisa") return;
    for (const page of [left, right]) {
      g.save(); g.clip(page);
      if (kind === "pautada") {
        for (let y = 44; y < 88; y += 3.6) line(g, "rgba(90,150,240,0.5)", 10, y, 90, y + 1, 0.28, false);
        line(g, "rgba(232,70,105,0.55)", 16, 38, 16, 90, 0.35, false); line(g, "rgba(232,70,105,0.55)", 84, 38, 84, 90, 0.35, false);
      } else if (kind === "quadriculada") {
        for (let y = 39; y < 90; y += 3.2) line(g, "rgba(90,150,240,0.4)", 10, y, 90, y, 0.22, false);
        for (let x = 11.6; x < 90; x += 3.2) line(g, "rgba(90,150,240,0.4)", x, 36, x, 90, 0.22, false);
      } else if (kind === "pontilhada") {
        for (let y = 42; y < 88; y += 3.2) for (let x = 13; x < 88; x += 3.2) circle(g, "rgba(70,70,120,0.45)", 0.3, x, y);
      } else if (kind === "partitura") {
        for (const y0 of [43, 79]) for (let i = 0; i < 5; i++) line(g, "rgba(50,40,100,0.36)", 10, y0 + i * 1.3, 90, y0 + i * 1.3 + 0.6, 0.22, false);
      } else if (PAPERS[kind]) {
        // papel colorido, liso, com a fibra bem de leve
        const [c1, c2] = PAPERS[kind];
        g.fillStyle = hGrad(g, [c2, c1, c1, c2], 10, 90); g.fill(page);
        if (kind === "kraft") for (let i = 0; i < 26; i++) line(g, "rgba(120,80,40,0.16)", 12 + ((i * 37) % 76), 40 + ((i * 23) % 46), 14 + ((i * 37) % 76), 40.4 + ((i * 23) % 46), 0.25);
      } else if (kind === "pergaminho") {
        // papel antigo: amarelado, com as bordas queimadas
        g.fillStyle = "rgba(226,190,120,0.42)"; g.fill(page);
        const vig = g.createRadialGradient(50, 64, 18, 50, 64, 46); vig.addColorStop(0, "rgba(150,90,30,0)"); vig.addColorStop(1, "rgba(150,90,30,0.42)"); g.fillStyle = vig; g.fill(page);
        for (let i = 0; i < 18; i++) circle(g, "rgba(140,90,40,0.12)", 0.4 + (i % 3) * 0.3, 14 + ((i * 37) % 72), 42 + ((i * 23) % 44));
      } else if (kind === "aquarela") {
        // manchas de aquarela nas pontas, com borda mais escura como tinta secando
        for (const [x, y, r, c] of [[16, 46, 9, "#FF9DB8"], [84, 48, 9, "#9FD3FF"], [18, 82, 8, "#B9F2CF"], [82, 82, 8, "#FFE36B"], [50, 42, 7, "#C9B6FF"]]) {
          const bl = g.createRadialGradient(x, y, 0, x, y, r); bl.addColorStop(0, rgba(c, 0.32)); bl.addColorStop(0.75, rgba(c, 0.42)); bl.addColorStop(0.92, rgba(c, 0.6)); bl.addColorStop(1, rgba(c, 0));
          g.fillStyle = bl; g.beginPath(); g.arc(x, y, r, 0, 2 * PI); g.fill();
        }
      } else if (PAGES2.has(kind)) {
        drawPagePattern2(g, kind, page, page === left ? -1 : 1, t || 0);
      } else if (kind === "ouro") {
        // folhas de ouro: filete dourado em volta, cantos de filigrana e um brilho que passa
        const fr = new Path2D(); fr.moveTo(13, 42.8); fr.bezierCurveTo(23, 39.6, 36, 39.2, 47.6, 45.6); fr.lineTo(47.6, 85.4); fr.bezierCurveTo(36, 80.2, 23, 79.8, 13, 82.4); fr.closePath();
        const fr2 = new Path2D(); fr2.moveTo(87, 42.8); fr2.bezierCurveTo(77, 39.6, 64, 39.2, 52.4, 45.6); fr2.lineTo(52.4, 85.4); fr2.bezierCurveTo(64, 80.2, 77, 79.8, 87, 82.4); fr2.closePath();
        stroke(g, fr, "rgba(201,138,18,0.8)", 0.45); stroke(g, fr2, "rgba(201,138,18,0.8)", 0.45);
        for (const [x, y, sx, sy] of [[15, 45, 1, 1], [85, 45, -1, 1], [15, 80, 1, -1], [85, 80, -1, -1]]) {
          const c = new Path2D(); c.moveTo(x, y + sy * 5); c.quadraticCurveTo(x, y, x + sx * 5, y); c.moveTo(x + sx * 1.6, y + sy * 1.6); c.quadraticCurveTo(x + sx * 4, y + sy * 0.6, x + sx * 3.4, y + sy * 3.2); c.quadraticCurveTo(x + sx * 2.2, y + sy * 4.4, x + sx * 1.6, y + sy * 1.6);
          stroke(g, c, "rgba(201,138,18,0.9)", 0.4); circle(g, GOLD, 0.6, x + sx * 2.4, y + sy * 2.4);
        }
        const sh = ((t || 0) * 0.25) % 1, x = lerp(0, 110, sh), gl = g.createLinearGradient(x - 8, 30, x + 8, 92);
        gl.addColorStop(0, "rgba(255,236,170,0)"); gl.addColorStop(0.5, "rgba(255,236,170,0.4)"); gl.addColorStop(1, "rgba(255,236,170,0)"); g.fillStyle = gl; g.fill(page);
      }
      g.restore();
    }
  }

  // Capas do armário 2.0. A capa só aparece nas lombadas (x 8,5–12 e 88–91,5) e numa faixinha no pé,
  // então o desenho de cada uma se concentra ali.
  const COVER_PAT2 = new Set(["pijama", "deserto", "colete", "processo", "nota", "bancario", "plenario", "espiral", "circuito", "pasta", "girassol", "ondas", "melancia", "cosmos"]);
  function blotches(g, seed, cols, n) {   // manchas arredondadas de camuflado, sempre no mesmo lugar
    for (let i = 0; i < n; i++) {
      const x = 6 + ((i * seed) % 88), y = 41 + ((i * 29) % 54), r = 2 + ((i * 7) % 5) * 0.5, a = i * 1.3;
      const p = new Path2D(); p.moveTo(x + r, y);
      for (let k = 1; k <= 6; k++) { const ang = k * PI / 3, rr = r * (0.7 + 0.3 * sin(a + k * 2.1)); p.quadraticCurveTo(x + cos(ang - PI / 6) * rr * 1.25, y + sin(ang - PI / 6) * rr * 0.9, x + cos(ang) * rr, y + sin(ang) * rr * 0.75); }
      p.closePath(); g.fillStyle = cols[i % cols.length]; g.fill(p);
    }
  }
  function drawCoverPattern2(g, t, pat, cover) {
    const spines = [10.2, 89.8];
    if (pat === "pijama") {
      // pijama cirúrgico: trama de algodão, bolso com caneta na lombada esquerda e crachá na direita
      for (let x = -50; x < 100; x += 1.2) line(g, "rgba(255,255,255,0.05)", x, 40, x + 54, 95, 0.25, false);
      const pk = new Path2D(); pk.roundRect(8.9, 62, 2.8, 6.4, [0, 0, 0.8, 0.8]);
      g.fillStyle = "rgba(225,255,250,0.35)"; g.fill(pk); stroke(g, pk, "rgba(20,80,74,0.9)", 0.4); line(g, "rgba(225,255,250,0.55)", 9, 62.7, 11.6, 62.7, 0.2, false);
      g.fillStyle = vGrad(g, ["#5B8CF0", "#2F5FC4"], 58.6, 63); g.fillRect(10.3, 58.6, 0.9, 4); line(g, "#C9D2E8", 11.3, 59, 11.3, 61.6, 0.25, false);
      const bd = new Path2D(); bd.roundRect(88.6, 52, 2.6, 3.8, 0.4); g.fillStyle = "#FFFFFF"; g.fill(bd); stroke(g, bd, "rgba(20,80,74,0.6)", 0.25);
      g.fillStyle = "#2F8FD8"; g.fillRect(88.6, 52, 2.6, 0.9); line(g, "rgba(40,40,60,0.55)", 89.1, 53.9, 90.7, 53.9, 0.25, false); line(g, "rgba(40,40,60,0.4)", 89.1, 54.9, 90.2, 54.9, 0.25, false);
      line(g, "rgba(200,205,220,0.9)", 89.9, 49.6, 89.9, 52, 0.25, false);
    } else if (pat === "deserto") {
      // camuflado do deserto: manchas areia, terra e café, com pontinhos de areia
      blotches(g, 43, ["rgba(140,104,60,0.5)", "rgba(240,226,192,0.55)", "rgba(178,140,92,0.5)"], 34);
      for (let i = 0; i < 30; i++) circle(g, "rgba(90,66,36,0.35)", 0.22, 7 + ((i * 31) % 86), 42 + ((i * 17) % 52));
    } else if (pat === "colete") {
      // colete tático: tecido grosso, fitas de fixação na lombada, bolso com aba e a faixa "POLÍCIA" refletiva
      for (let x = 6; x < 96; x += 1.6) line(g, "rgba(255,255,255,0.04)", x, 40, x, 95, 0.3, false);
      for (const x of spines) for (let y = 62; y < 88; y += 3.4) { g.fillStyle = "rgba(0,0,0,0.35)"; g.fillRect(x - 1.7, y, 3.4, 1.3); line(g, "rgba(255,255,255,0.14)", x - 1.7, y + 0.15, x + 1.7, y + 0.15, 0.2, false); }
      for (const x of spines) {
        const fl = new Path2D(); fl.roundRect(x - 1.5, 44.6, 3, 1.8, [0.3, 0.3, 0.8, 0.8]); g.fillStyle = "rgba(10,14,24,0.6)"; g.fill(fl); stroke(g, fl, "rgba(255,255,255,0.15)", 0.2);
        const st = new Path2D(); st.roundRect(x - 1.7, 53.6, 3.4, 3.6, 0.3); g.fillStyle = vGrad(g, ["#F4F4F0", "#C8CAD0"], 53.6, 57.2); g.fill(st);
        for (let i = 0; i < 4; i++) line(g, "#1A2236", x - 1.2 + i * 0.8, 54.6, x - 0.8 + i * 0.8, 54.6, 0.5, false);
        line(g, "rgba(26,34,54,0.6)", x - 1.2, 56.2, x + 1.2, 56.2, 0.3, false);
      }
    } else if (pat === "processo") {
      // capa de processo: cartolina com fibras, etiqueta de autuação na lombada esquerda e grampos na direita
      for (let i = 0; i < 30; i++) line(g, "rgba(140,100,40,0.18)", 7 + ((i * 37) % 86), 42 + ((i * 23) % 50), 8.4 + ((i * 37) % 86), 42.3 + ((i * 23) % 50), 0.25);
      const lb = new Path2D(); lb.roundRect(8.9, 49, 2.9, 10, 0.3); g.fillStyle = "#FFFFFF"; g.fill(lb); stroke(g, lb, "rgba(120,90,40,0.5)", 0.25);
      g.fillStyle = "#D8434E"; g.fillRect(8.9, 49, 2.9, 1.2);
      for (const y of [51.6, 53.2, 54.8, 56.4]) line(g, "rgba(60,60,80,0.45)", 9.4, y, y === 56.4 ? 10.4 : 11.3, y, 0.25, false);
      for (const y of [48, 82]) {
        const gr = new Path2D(); gr.roundRect(88.4, y, 2.8, 3, 0.5); g.fillStyle = vGrad(g, ["#FFFFFF", "#B8BCC8", "#8E92A6"], y, y + 3); g.fill(gr); stroke(g, gr, "rgba(70,70,90,0.6)", 0.25);
        line(g, "rgba(70,70,90,0.7)", 89.1, y + 1.5, 90.5, y + 1.5, 0.35); circle(g, "rgba(255,255,255,0.9)", 0.25, 88.9, y + 0.6);
      }
      circle(g, "rgba(216,67,78,0.25)", 1.3, 10.3, 76); g.beginPath(); g.arc(10.3, 76, 1.3, 0, 2 * PI); g.strokeStyle = "rgba(216,67,78,0.55)"; g.lineWidth = 0.25; g.stroke();
    } else if (pat === "nota") {
      // nota fiscal: cabeçalho azul, linhas de campos, picote na esquerda e código de barras na direita
      for (const x of spines) { g.fillStyle = "rgba(50,110,210,0.7)"; g.fillRect(x - 1.8, 44, 3.6, 5); line(g, "rgba(30,70,150,0.6)", x - 1.2, 46.5, x + 1, 46.5, 0.4, false); }
      for (let y = 51; y < 88; y += 2.4) for (const x of spines) line(g, "rgba(70,70,100,0.45)", x - 1.7, y, x + 1.7, y, 0.3, false);
      for (let y = 50; y < 89; y += 1.2) circle(g, "rgba(120,115,100,0.45)", 0.2, 11.5, y);
      const bars = [0.5, 0.2, 0.3, 0.6, 0.2, 0.4, 0.2, 0.5, 0.3, 0.2, 0.6, 0.2, 0.3, 0.4, 0.2, 0.5, 0.2, 0.3, 0.6, 0.2, 0.4, 0.3];
      let y = 66; g.fillStyle = "#FFFFFF"; g.fillRect(88.3, 65.4, 3.1, 19.4);
      for (let i = 0; i < bars.length; i++) { if (i % 2 === 0) { g.fillStyle = "#1A1A22"; g.fillRect(88.7, y, 2.3, bars[i]); } y += bars[i] + 0.25; }
    } else if (pat === "bancario") {
      // azul de agência: risca finíssima, filete dourado rente às páginas e a moeda-emblema nas lombadas
      for (let x = 6; x < 96; x += 1.6) line(g, "rgba(200,220,255,0.14)", x, 40, x, 95, 0.15, false);
      line(g, "rgba(245,196,81,0.9)", 11.4, 45, 11.4, 88, 0.35, false); line(g, "rgba(245,196,81,0.9)", 88.6, 45, 88.6, 88, 0.35, false);
      for (const x of [9.8, 90.2]) {
        const cg = g.createRadialGradient(x - 0.4, 56.6, 0.2, x, 57, 1.5); cg.addColorStop(0, "#FFF0B8"); cg.addColorStop(1, "#C98A12");
        g.beginPath(); g.arc(x, 57, 1.4, 0, 2 * PI); g.fillStyle = cg; g.fill(); g.strokeStyle = "rgba(120,70,0,0.6)"; g.lineWidth = 0.2; g.stroke();
        g.beginPath(); g.arc(x, 57, 0.85, 0, 2 * PI); g.strokeStyle = "rgba(150,95,10,0.7)"; g.stroke();
        line(g, "rgba(150,95,10,0.85)", x, 56.4, x, 57.6, 0.3, false);
      }
    } else if (pat === "plenario") {
      // verde do plenário: couro de poltrona com grão, filetes dourados e a fileira de tachas
      for (const s of GRAIN) circle(g, `rgba(0,20,8,${s.a * 1.2})`, s.r * 1.2, s.x < 50 ? 8.5 + (s.x - 15) * 0.1 : 91.5 - (85 - s.x) * 0.1, s.y + 6);
      for (const x of [9.6, 90.4]) for (const y of [47, 49, 82, 84]) line(g, "rgba(245,196,81,0.85)", x - 1.2, y, x + 1.2, y, 0.35, false);
      for (const x of [9.6, 90.4]) for (let y = 53; y < 80; y += 3.2) { const tg = g.createRadialGradient(x - 0.2, y - 0.2, 0.05, x, y, 0.6); tg.addColorStop(0, "#FFF0B8"); tg.addColorStop(1, "#B47A10"); circle(g, tg, 0.55, x, y); }
    } else if (pat === "espiral") {
      // caderno de espiral: furos na capa e a espiral de arame prateado na lombada esquerda
      for (let x = -50; x < 100; x += 1.5) line(g, "rgba(255,255,255,0.05)", x, 40, x + 54, 95, 0.3, false);
      for (let y = 46; y < 88; y += 2.4) {
        circle(g, "rgba(60,20,10,0.75)", 0.45, 11, y + 0.3);
        const back = new Path2D(); back.ellipse(9.4, y, 1.9, 0.6, -0.25, PI * 0.1, PI * 0.9); stroke(g, back, "rgba(80,84,100,0.8)", 0.4);
        const front = new Path2D(); front.ellipse(9.4, y, 1.9, 0.6, -0.25, PI * 1.05, PI * 1.95);
        stroke(g, front, "#6C7084", 0.6); stroke(g, front, "#E8EAF2", 0.3);
      }
    } else if (pat === "circuito") {
      // placa de circuito: trilhas douradas com ilhas de solda e LEDs piscando
      const tr = "rgba(255,206,90,0.95)";
      for (const pts of [[[9, 45], [9, 53], [11, 55], [11, 63]], [[10.6, 45], [10.6, 49]], [[9.2, 66], [9.2, 74], [11, 76], [11, 86]], [[91, 48], [91, 58], [89, 60], [89, 68]], [[89.4, 72], [89.4, 80], [91, 82], [91, 88]], [[16, 87.4], [26, 87], [30, 88]], [[84, 87.4], [74, 87], [70, 88]]]) {
        const p = new Path2D(); p.moveTo(...pts[0]); for (const q of pts.slice(1)) p.lineTo(...q); stroke(g, p, tr, 0.6);
        for (const q of [pts[0], pts[pts.length - 1]]) { circle(g, "#F0C45A", 0.5, q[0], q[1]); circle(g, "#135A30", 0.2, q[0], q[1]); }
      }
      for (const [x, y, c, ph] of [[10.4, 69, "#FF4D5E", 0], [89.6, 63, "#5CFF8A", 1.7], [90, 84.6, "#6FB0FF", 3.1], [10, 82, "#FFD84A", 4.4]]) {
        const on = 0.35 + 0.65 * (sin(t * 3 + ph) > 0 ? 1 : 0.3);
        g.save(); g.shadowColor = c; g.shadowBlur = 3 * on; circle(g, rgba(c, on), 0.95, x, y); g.restore();
        circle(g, "rgba(255,255,255,0.8)", 0.2, x - 0.2, y - 0.2);
      }
    } else if (pat === "pasta") {
      // pasta-arquivo: cartolina com fibra e o elástico preto passando pelas lombadas
      for (let i = 0; i < 34; i++) circle(g, "rgba(255,255,255,0.12)", 0.25 + (i % 3) * 0.15, 7 + ((i * 37) % 86), 42 + ((i * 23) % 50));
      for (const y of [58, 76]) for (const [a, b] of [[8, 12.2], [87.8, 92]]) {
        g.fillStyle = vGrad(g, ["#3A3A46", "#15151C", "#2A2A34"], y - 0.6, y + 0.6); g.fillRect(a, y - 0.6, b - a, 1.2);
        line(g, "rgba(255,255,255,0.3)", a, y - 0.4, b, y - 0.4, 0.15, false);
      }
      const tab = new Path2D(); tab.roundRect(88.6, 46, 2.4, 6, 0.5); g.fillStyle = "rgba(255,255,255,0.85)"; g.fill(tab); stroke(g, tab, "rgba(40,70,120,0.5)", 0.25);
      line(g, "rgba(40,70,120,0.6)", 89.2, 48, 90.4, 48, 0.25, false); line(g, "rgba(40,70,120,0.45)", 89.2, 49.4, 90, 49.4, 0.25, false);
    } else if (pat === "girassol") {
      // girassóis em céu azul: pétalas amarelas que balançam de leve, miolo marrom e folhas
      // girassóis grandes, cortados pela borda como estampa (lombadas e faixa do pé)
      for (const [x, y, s, ph] of [[9.6, 49, 2.3, 0], [11, 62, 2, 1.4], [9.4, 75, 2.3, 2.1], [10.8, 87, 2, 3], [90.4, 54, 2.3, 2.6], [89, 67, 2, 3.8], [90.6, 80, 2.3, 4.4], [22, 90.6, 2.2, 5], [36, 92.2, 2, 5.6], [64, 92.2, 2, 6.2], [78, 90.6, 2.2, 0.7]]) {
        const sw = sin(t * 1.4 + ph) * 0.1;
        for (let k = 0; k < 12; k++) {
          const a = k * PI / 6 + sw; g.beginPath(); g.ellipse(x + cos(a) * 0.95 * s, y + sin(a) * 0.95 * s, 0.6 * s, 0.27 * s, a, 0, 2 * PI);
          g.fillStyle = k % 2 ? "#FFE04A" : "#FFC21F"; g.fill(); g.strokeStyle = "rgba(190,110,0,0.7)"; g.lineWidth = 0.18; g.stroke();
        }
        const cg = g.createRadialGradient(x - 0.15 * s, y - 0.15 * s, 0.1, x, y, 0.55 * s); cg.addColorStop(0, "#8A4E1C"); cg.addColorStop(1, "#3E1E06");
        circle(g, cg, 0.48 * s, x, y);
        for (const [dx, dy] of [[-0.18, -0.12], [0.14, 0.06], [-0.03, 0.2]]) circle(g, "rgba(255,200,120,0.6)", 0.06 * s, x + dx * s, y + dy * s);
      }
    } else if (pat === "ondas") {
      // ondas do mar: cristas brancas bem marcadas, em arcos, correndo devagar, e espuma no pé
      for (let y = 45, i = 0; y < 96; y += 3.4, i++) {
        const ph = (t * 0.8 + i * 0.5) % 1, p = new Path2D();
        p.moveTo(2, y);
        for (let x = 2 - ph * 4 + (i % 2) * 2; x < 98; x += 4) p.quadraticCurveTo(x + 2, y - 1.8, x + 4, y);
        stroke(g, p, "rgba(255,255,255,0.8)", 0.55);
        g.save(); g.translate(0, 0.9); stroke(g, p, "rgba(10,60,110,0.35)", 0.4); g.restore();
      }
      for (let x = 14; x < 88; x += 3) circle(g, "rgba(255,255,255,0.85)", 0.5, x, 90.4 + abs(x - 50) * -0.05 + sin(x + t * 2) * 0.2);
    } else if (pat === "melancia") {
      // melancia: casca verde-escura listrada e, na faixa do pé e nas lombadas, a fatia vermelha com sementes
      for (let x = 5; x < 96; x += 3.4) {
        const p = new Path2D(); for (let y = 40; y <= 95; y += 1.2) { const xx = x + ((y / 1.2) % 2 < 1 ? -0.5 : 0.5); y === 40 ? p.moveTo(xx, y) : p.lineTo(xx, y); }
        stroke(g, p, "rgba(14,60,22,0.75)", 1.3);
      }
      // fatia: polpa vermelha seguindo a borda de baixo, com a linha branca da casca embaixo
      const sl = new Path2D(); sl.moveTo(8, 84.6); sl.bezierCurveTo(22, 82, 38, 83, 50, 88.6); sl.bezierCurveTo(62, 83, 78, 82, 92, 84.6);
      sl.lineTo(92, 87.6); sl.bezierCurveTo(78, 85, 62, 86, 50, 91.6); sl.bezierCurveTo(38, 86, 22, 85, 8, 87.6); sl.closePath();
      g.fillStyle = vGrad(g, ["#FF6A78", "#E8324A"], 83, 91); g.fill(sl);
      const rd = new Path2D(); rd.moveTo(8, 87.8); rd.bezierCurveTo(22, 85.2, 38, 86.2, 50, 91.8); rd.bezierCurveTo(62, 86.2, 78, 85.2, 92, 87.8); stroke(g, rd, "#F2FBE4", 0.6);
      for (const [a, side] of [[10.4, 1], [89.6, -1]]) {
        g.fillStyle = "#F2FBE4"; g.fillRect(side > 0 ? a - 0.5 : a, 44, 0.5, 42);
        g.fillStyle = hGrad(g, side > 0 ? ["#FF6A78", "#E8324A"] : ["#E8324A", "#FF6A78"], side > 0 ? a : a - 2, side > 0 ? a + 2 : a);
        g.fillRect(side > 0 ? a : a - 2, 44, 2, 42);
        for (let y = 48; y < 84; y += 3.6) { g.beginPath(); g.ellipse(a + side * 1, y, 0.28, 0.5, side * 0.4, 0, 2 * PI); g.fillStyle = "#1A1210"; g.fill(); }
      }
      for (const x of [16, 26, 36, 64, 74, 84]) { const y = 85.6 + (abs(x - 50) < 20 ? 1.6 : 0.4); g.beginPath(); g.ellipse(x, y, 0.5, 0.28, 0.3, 0, 2 * PI); g.fillStyle = "#1A1210"; g.fill(); }
    } else if (pat === "cosmos") {
      // cosmos: nebulosas coloridas, estrelas cintilando, um planeta com anel e, às vezes, uma estrela cadente
      // nebulosa rosa/roxa clara bem visível nas lombadas e no pé
      for (const [x, y, r, c] of [[10, 54, 9, "255,150,225"], [10, 78, 8, "200,150,255"], [90, 60, 9, "200,150,255"], [90, 82, 8, "255,150,225"], [30, 92, 12, "190,140,255"], [70, 92, 12, "255,150,225"]]) {
        const ng = g.createRadialGradient(x, y, 0, x, y, r); ng.addColorStop(0, `rgba(${c},0.85)`); ng.addColorStop(0.5, `rgba(${c},0.4)`); ng.addColorStop(1, `rgba(${c},0)`); g.fillStyle = ng; g.fill(cover);
      }
      for (const [x, y, r] of [[9.4, 47, 0.5], [11, 52, 0.35], [9.2, 63, 0.65], [10.8, 70, 0.4], [9.8, 82, 0.55], [90.6, 49, 0.65], [89.2, 57, 0.4], [90.8, 70, 0.55], [89.4, 77, 0.4], [90.4, 86, 0.5], [20, 89.4, 0.55], [36, 90.8, 0.45], [64, 90.8, 0.55], [80, 89.4, 0.45]]) {
        const tw = 0.5 + 0.5 * abs(sin(t * 1.7 + x * 0.7 + y));
        circle(g, `rgba(255,252,235,${tw})`, r, x, y);
        if (r > 0.45) { line(g, `rgba(255,252,235,${tw * 0.85})`, x - r * 2.8, y, x + r * 2.8, y, 0.2); line(g, `rgba(255,252,235,${tw * 0.85})`, x, y - r * 2.8, x, y + r * 2.8, 0.2); }
      }
      const pg = g.createRadialGradient(9.6, 83.4, 0.1, 10, 84, 1.3); pg.addColorStop(0, "#FFD3A0"); pg.addColorStop(1, "#C2603A"); circle(g, pg, 1.2, 10, 84);
      g.beginPath(); g.ellipse(10, 84, 2, 0.5, -0.3, 0, 2 * PI); g.strokeStyle = "rgba(255,226,170,0.85)"; g.lineWidth = 0.25; g.stroke();
      const k = (t * 0.22) % 1;
      if (k < 0.18) { const u = k / 0.18, x = 87 + u * 4, y = 46 + u * 10; line(g, `rgba(255,255,255,${0.9 * (1 - u)})`, x - 1.6, y - 4, x, y, 0.25); circle(g, "#FFFFFF", 0.3, x, y); }
      const sx = -30 + ((t * 0.25) % 1) * 170, sh = g.createLinearGradient(sx, 40, sx + 18, 95);
      sh.addColorStop(0, "rgba(255,255,255,0)"); sh.addColorStop(0.5, "rgba(220,200,255,0.25)"); sh.addColorStop(1, "rgba(255,255,255,0)"); g.fillStyle = sh; g.fill(cover);
    }
  }

  // Textura da capa (só aparece nas bordas e no pé do livro, onde a capa fica à mostra).
  function drawCoverPattern(g, t, C, cover) {
    const pat = C.pattern;
    if (pat === "leather" || pat === "denim") {
      if (pat === "leather") for (const s of GRAIN) circle(g, `rgba(40,18,4,${s.a * 1.3})`, s.r * 1.3, s.x < 50 ? 8.5 + (s.x - 15) * 0.1 : 91.5 - (85 - s.x) * 0.1, s.y + 6);
      else for (let x = -50; x < 100; x += 0.9) line(g, "rgba(255,255,255,0.07)", x, 40, x + 54, 95, 0.25, false);
    } else if (pat === "plaid") {
      for (let x = 4; x < 100; x += 6) { line(g, "rgba(20,10,10,0.28)", x, 40, x, 95, 1.6, false); line(g, "rgba(255,230,160,0.35)", x + 2.5, 40, x + 2.5, 95, 0.3, false); }
      for (let y = 42; y < 95; y += 6) { line(g, "rgba(20,10,10,0.28)", 0, y, 100, y, 1.6, false); line(g, "rgba(255,230,160,0.35)", 0, y + 2.5, 100, y + 2.5, 0.3, false); }
    } else if (pat === "dots") {
      for (let y = 44; y < 94; y += 3.2) for (let x = (y % 6.4 < 3.2 ? 8.6 : 10.2); x < 92; x += 3.2) circle(g, "rgba(255,255,255,0.75)", 0.55, x, y);
    } else if (pat === "holo") {
      const h = (t * 40) % 360, hg = g.createLinearGradient(0, 40, 100, 95);
      for (let i = 0; i <= 4; i++) hg.addColorStop(i / 4, `hsla(${(h + i * 72) % 360},90%,72%,0.55)`);
      g.fillStyle = hg; g.fill(cover);
    } else if (pat === "tweed") {
      // espinha de peixe: zigue-zague miúdo em duas tonalidades
      for (let y = 40; y < 96; y += 1.6) for (let x = 6; x < 96; x += 3.2) { const k = ((x + y) / 3.2) % 2 < 1; line(g, k ? "rgba(255,240,220,0.16)" : "rgba(30,20,10,0.22)", x, y, x + 1.6, y + (k ? 1.2 : -1.2), 0.35, false); }
    } else if (pat === "fleur") {
      // veludo com losangos dourados bordados
      for (let y = 44; y < 94; y += 4) for (let x = (y % 8 < 4 ? 9 : 11); x < 92; x += 4) { const d = new Path2D(); d.moveTo(x, y - 0.9); d.lineTo(x + 0.7, y); d.lineTo(x, y + 0.9); d.lineTo(x - 0.7, y); d.closePath(); g.fillStyle = "rgba(245,196,81,0.55)"; g.fill(d); }
      const sh = g.createLinearGradient(8, 40, 92, 95); sh.addColorStop(0, "rgba(255,255,255,0.10)"); sh.addColorStop(0.5, "rgba(255,255,255,0)"); sh.addColorStop(1, "rgba(0,0,0,0.12)"); g.fillStyle = sh; g.fill(cover);
    } else if (pat === "foil") {
      for (let x = -50; x < 100; x += 7) line(g, "rgba(245,184,61,0.45)", x, 40, x + 54, 95, 0.25, false);
    } else if (pat === "facets") {
      for (let x = -50; x < 100; x += 5) { line(g, "rgba(255,255,255,0.35)", x, 40, x + 30, 95, 0.3, false); line(g, "rgba(80,130,180,0.25)", x + 30, 40, x, 95, 0.3, false); }
    } else if (pat === "camo") {
      // camuflado: manchas arredondadas em três tons (escuro, terra e claro), sempre no mesmo lugar
      const cols = ["rgba(38,46,18,0.55)", "rgba(110,86,48,0.5)", "rgba(178,186,120,0.45)"];
      for (let i = 0; i < 34; i++) {
        const x = 6 + ((i * 41) % 88), y = 41 + ((i * 29) % 54), r = 2.2 + ((i * 7) % 5) * 0.55, a = i * 0.9;
        const p = new Path2D(); p.moveTo(x + r, y);
        for (let k = 1; k <= 6; k++) { const ang = k * PI / 3, rr = r * (0.7 + 0.3 * sin(a + k * 2.1)); p.quadraticCurveTo(x + cos(ang - PI / 6) * rr * 1.25, y + sin(ang - PI / 6) * rr * 0.9, x + cos(ang) * rr, y + sin(ang) * rr * 0.75); }
        p.closePath(); g.fillStyle = cols[i % 3]; g.fill(p);
      }
    } else if (pat === "jaleco") {
      // jaleco: tecido liso com trama fininha, cruz de saúde nas lombadas e botões na barra
      for (let x = -50; x < 100; x += 1.2) line(g, "rgba(120,140,170,0.05)", x, 40, x + 54, 95, 0.25, false);
      for (const x of [10, 90]) { g.fillStyle = "#E0475E"; g.fillRect(x - 0.45, 49, 0.9, 3.2); g.fillRect(x - 1.6, 50.15, 3.2, 0.9); }
      for (const x of [30, 70]) { circle(g, "rgba(120,140,170,0.55)", 0.85, x, 90.2 - abs(x - 50) * 0.06); circle(g, "#FFFFFF", 0.6, x - 0.15, 90 - abs(x - 50) * 0.06); }
    } else if (pat === "farda") {
      // farda: tecido rip-stop (quadriculado miúdo), o brasão dourado nas lombadas e a faixa refletiva na barra
      for (let x = 6; x < 96; x += 2.2) line(g, "rgba(255,255,255,0.05)", x, 40, x, 95, 0.25, false);
      for (let y = 41; y < 95; y += 2.2) line(g, "rgba(255,255,255,0.05)", 6, y, 94, y, 0.25, false);
      for (const x of [10, 90]) { const sh = new Path2D(); sh.moveTo(x - 1.6, 48.4); sh.lineTo(x + 1.6, 48.4); sh.lineTo(x + 1.6, 51); sh.quadraticCurveTo(x, 53.6, x - 1.6, 51); sh.closePath(); g.fillStyle = "#E9AE34"; g.fill(sh); circle(g, "#22306A", 0.6, x, 50.4); }
      const band = new Path2D(); band.moveTo(10, 81.6); band.bezierCurveTo(22, 79.5, 38, 80.4, 50, 85.6); band.bezierCurveTo(62, 80.4, 78, 79.5, 90, 81.6); band.lineTo(90, 83.6); band.bezierCurveTo(78, 81.5, 62, 82.4, 50, 87.6); band.bezierCurveTo(38, 82.4, 22, 81.5, 10, 83.6); band.closePath();
      g.fillStyle = hGrad(g, ["#B8BCC8", "#F2F4FA", "#9EA3B2", "#F2F4FA", "#B8BCC8"], 10, 90); g.fill(band);
    } else if (pat === "lousa") {
      // lousa: verde fosco com pó de giz, um triângulo e uma conta meio apagados e a moldura de madeira nas lombadas
      for (let i = 0; i < 40; i++) circle(g, "rgba(255,255,255,0.06)", 0.4 + (i % 3) * 0.3, 8 + ((i * 37) % 84), 42 + ((i * 23) % 50));
      const ch = "rgba(255,255,255,0.32)";
      const tri = new Path2D(); tri.moveTo(14, 86); tri.lineTo(14, 77); tri.lineTo(24, 86); tri.closePath(); stroke(g, tri, ch, 0.4);
      line(g, ch, 14, 84.4, 15.6, 84.4, 0.3, false); line(g, ch, 15.6, 84.4, 15.6, 86, 0.3, false);
      for (const [x0, y0, x1, y1] of [[77, 78, 80, 82], [77, 82, 80, 78], [82, 79.4, 84.4, 79.4], [82, 80.8, 84.4, 80.8], [86, 78, 87.6, 77.4], [87.6, 77.4, 87.6, 82]]) line(g, ch, x0, y0, x1, y1, 0.4, false);
      const sw = new Path2D(); sw.moveTo(70, 86); sw.bezierCurveTo(74, 84, 80, 87, 86, 85); stroke(g, sw, "rgba(255,255,255,0.12)", 1.6);
      for (const x of [9.2, 90.8]) { g.fillStyle = "rgba(150,100,55,0.75)"; g.fillRect(x - 1.2, 43, 2.4, 46); line(g, "rgba(255,220,170,0.35)", x - 0.6, 43, x - 0.6, 89, 0.3, false); }
    } else if (pat === "risca") {
      // terno de risca de giz, com os botões nas lombadas
      for (let x = 6; x < 96; x += 2.6) line(g, "rgba(230,232,240,0.22)", x, 40, x, 95, 0.22, false);
      for (const x of [10, 90]) for (const y of [62, 70]) circle(g, "rgba(200,205,220,0.7)", 0.7, x, y);
    } else if (pat === "codigo") {
      // Vade Mecum: couro com grão, filetes dourados nas lombadas e na barra, e o parágrafo em relevo
      for (const s of GRAIN) circle(g, `rgba(30,0,6,${s.a * 1.2})`, s.r * 1.2, s.x < 50 ? 8.5 + (s.x - 15) * 0.1 : 91.5 - (85 - s.x) * 0.1, s.y + 6);
      for (const x of [9.2, 90.8]) for (const y of [47, 49]) line(g, "rgba(245,196,81,0.85)", x - 1.4, y, x + 1.4, y, 0.4, false);
      for (const x of [9.2, 90.8]) for (const y of [80, 82]) line(g, "rgba(245,196,81,0.85)", x - 1.4, y, x + 1.4, y, 0.4, false);
      g.save(); g.fillStyle = "rgba(245,196,81,0.9)"; g.font = "900 3px Georgia, serif"; g.textAlign = "center"; g.textBaseline = "middle";
      g.fillText("§", 9.2, 64.5); g.fillText("§", 90.8, 64.5); g.restore();
    } else if (COVER_PAT2.has(pat)) {
      drawCoverPattern2(g, t, pat, cover);
    } else for (let x = -50; x < 100; x += 1.5) { line(g, "rgba(255,255,255,0.045)", x, 40, x + 54, 95, 0.3, false); line(g, "rgba(0,0,0,0.06)", x + 54, 40, x, 95, 0.3, false); }
    if (C.stitch) {   // costura pontilhada rente à borda
      g.save(); g.setLineDash([1.1, 0.9]);
      const st = new Path2D();
      st.moveTo(10.2, 45.8); st.lineTo(10.2, 87.4); st.bezierCurveTo(22, 85.3, 38, 86.2, 50, 91.4); st.bezierCurveTo(62, 86.2, 78, 85.3, 89.8, 87.4); st.lineTo(89.8, 45.8);
      stroke(g, st, C.stitch, 0.35); g.restore();
    }
    if (pat === "stars") for (const [x, y, r] of [[9.8, 50, 0.35], [10.6, 63, 0.25], [9.6, 76, 0.4], [90.2, 55, 0.3], [89.6, 70, 0.4], [90.4, 82, 0.25], [24, 88.6, 0.3], [40, 90.4, 0.35], [62, 90.2, 0.3], [77, 88.4, 0.35]]) {
      circle(g, `rgba(255,248,214,${0.55 + 0.45 * abs(sin(t * 1.3 + x))})`, r, x, y);
    }
    if (pat === "shine" || pat === "facets" || pat === "foil") {   // um brilho passando de tempos em tempos
      const sx = -30 + ((t * 0.32) % 1) * 170, sh = g.createLinearGradient(sx, 40, sx + 18, 95);
      sh.addColorStop(0, "rgba(255,255,255,0)"); sh.addColorStop(0.5, `rgba(255,255,255,${pat === "shine" ? 0.55 : 0.4})`); sh.addColorStop(1, "rgba(255,255,255,0)");
      g.fillStyle = sh; g.fill(cover);
    }
  }

  function drawBook(g, t, mood, small, look) {
    const cover = coverPath(), C = look.cover;
    // contorno macio: claro por fora (fundo escuro) e tinta por dentro (fundo claro)
    stroke(g, cover, "rgba(255,255,255,0.28)", small ? 3 : 2);
    stroke(g, cover, rgba(C.under, 0.85), small ? 1.6 : 1);
    g.save(); g.translate(0, 1.7); g.fillStyle = C.under; g.fill(cover); g.restore();
    g.fillStyle = vGrad(g, C.c, 41, 93); g.fill(cover);
    const lg = g.createLinearGradient(8, 44, 40, 70); lg.addColorStop(0, "rgba(255,255,255,0.14)"); lg.addColorStop(1, "rgba(255,255,255,0)");
    g.fillStyle = lg; g.fill(cover);
    if (!small) {
      g.save(); g.clip(cover);
      drawCoverPattern(g, t, C, cover);
      g.restore();
    }
    for (const side of [-1, 1]) {
      const ed = new Path2D(), x0 = 50 + side * 41.5;
      ed.moveTo(x0, 43.5); ed.lineTo(x0 + side * 1.6, 44.4); ed.lineTo(x0 + side * 1.6, 88.2); ed.lineTo(x0, 89); ed.closePath();
      g.fillStyle = vGrad(g, [C.c[2], C.under], 44, 89); g.fill(ed);
    }
    if (!small) drawCorners(g, C.corner);
    drawRibbon(g, t, mood, look.ribbon, look.ribbonPat);

    for (const side of [-1, 1]) {
      const block = pageBlock(side, 3.6);
      g.fillStyle = vGrad(g, [PAPER_EDGE, PAPER_EDGE_DARK], 80, 91); g.fill(block);
      if (!small) {
        for (let i = 1; i <= 6; i++) {
          const d = i * 0.52, p = new Path2D();
          p.moveTo(50 + side * (38 + d * 0.45), 84.4 + d);
          p.bezierCurveTo(50 + side * 26.2, 80.6 + d, 50 + side * 10, 81.4 + d, 50, 87 + d);
          stroke(g, p, "rgba(140,130,112,0.22)", 0.28);
        }
        for (let i = 1; i <= 5; i++) { const x = 50 + side * (38 + i * 0.3); line(g, "rgba(140,130,112,0.18)", x, 41 + i * 0.3, x, 84.4 + i * 0.9, 0.25, false); }
      }
    }
    const left = page(-1), right = page(1);
    g.fillStyle = hGrad(g, [PAPER_MID, PAPER, PAPER, PAPER_MID], 12, 50); g.fill(left);
    g.fillStyle = hGrad(g, [PAPER_MID, PAPER, PAPER, "#F1EDE6"], 50, 88); g.fill(right);
    if (!small) drawPagePattern(g, look.pages, left, right, look.ink, t);
    const gutter = [[0, rgba(GUTTER, 0)], [0.55, rgba(GUTTER, 0.035)], [0.85, rgba(GUTTER, 0.10)], [1, rgba(GUTTER, 0.20)]];
    g.fillStyle = hGrad(g, gutter, 39, 50); g.fill(left);
    g.fillStyle = hGrad(g, gutter, 61, 50); g.fill(right);
    if (!small) for (const s of GRAIN) {
      if (s.long) line(g, `rgba(154,143,122,${s.a})`, s.x, s.y, s.x + 1.4, s.y + 0.3, 0.18);
      else circle(g, `rgba(154,143,122,${s.a})`, s.r, s.x, s.y);
    }
    g.fillStyle = hGrad(g, [rgba(GUTTER, 0.06), rgba(GUTTER, 0)], 12, 17); g.fill(left);
    g.fillStyle = hGrad(g, [rgba(GUTTER, 0), rgba(GUTTER, 0.08)], 83, 88); g.fill(right);
    line(g, rgba(GUTTER, 0.16), 50, 44.3, 50, 86.8, 0.35, false);
    if (!small) { oval(g, "#C0392B", 48.6, 43.2, 2.8, 1.6); line(g, "rgba(255,230,170,0.85)", 48.9, 43.9, 51.1, 43.9, 0.35, false); }
    for (const side of [-1, 1]) {
      const rim = new Path2D();
      rim.moveTo(50 + side * 3, 41.9); rim.bezierCurveTo(50 + side * 12, 37.9, 50 + side * 26, 36.9, 50 + side * 36.5, 40.2);
      stroke(g, rim, "rgba(255,255,255,0.9)", 0.5);
    }
  }

  function drawTurningPage(g, t) {
    const T = turnState(t);
    if (!T.turning) return;
    const { p, c, lift, edgeX, up } = T, w = edgeX - 50, dir = c >= 0 ? 1 : -1;
    // o canto de baixo (onde a mão segura) vai adiantado: a folha enrola em diagonal
    const lead = lift * 7 * (c >= 0 ? -1 : 1) * 0.6, curl = lift * 3.2;
    const sheet = new Path2D();
    sheet.moveTo(50, 44);
    sheet.bezierCurveTo(50 + w * 0.28, 37.6 - up * 0.7, 50 + w * 0.7, 36.4 - up, edgeX - lead * 0.4, 40.4 - up * 0.8);
    sheet.quadraticCurveTo(edgeX + dir * 2.2 * lift - lead * 0.2, 62 - up, edgeX + lead, 84.4 - up * 0.8 - curl);
    sheet.bezierCurveTo(50 + w * 0.7 + lead * 0.5, 80.6 - up - curl * 0.5, 50 + w * 0.28, 81.4 - up * 0.5, 50, 87);
    sheet.closePath();
    g.save(); g.translate(dir * 2.5 * lift, 2.2 * lift); g.fillStyle = rgba(GUTTER, 0.13 * lift); g.fill(sheet); g.restore();
    const front = c >= 0, facing = abs(c), base = front ? PAPER : "#F2EEE6";
    if (abs(w) > 0.6) {
      g.fillStyle = hGrad(g, [lerpColor(base, GUTTER, 0.16 + 0.1 * lift), lerpColor(base, GUTTER, 0.05 * (1 - facing)), lerpColor(base, "#FFFFFF", 0.5 * facing)], 50, edgeX);
      g.fill(sheet);
      if (front) for (const [y, len] of [[45, 0.5], [49, 0.7], [79.5, 0.45]]) {
        const yy = y - up * 0.85;
        line(g, rgba(PAGE_LINE, facing), 50 + w * 0.88, yy, 50 + w * (0.88 - len), yy + 1.5 * (len - 0.4), 1.6 * (0.5 + 0.5 * facing));
      }
    }
    line(g, "rgba(255,255,255,0.8)", edgeX - lead * 0.4, 40.6 - up * 0.8, edgeX + dir * 1.6 * lift, 62 - up, 0.45);
    stroke(g, sheet, rgba(GUTTER, 0.18), 0.25);
    // dobrinha do canto enrolado (verso aparecendo)
    if (lift > 0.15) {
      const cx = edgeX + lead, cy = 84.4 - up * 0.8 - curl, k = lift * 4;
      const cu = new Path2D(); cu.moveTo(cx, cy); cu.lineTo(cx - dir * k * 0.9, cy + k * 0.15); cu.quadraticCurveTo(cx - dir * k * 0.3, cy - k * 0.5, cx - dir * k * 0.15, cy - k * 1.1); cu.closePath();
      g.fillStyle = "#EFEAE0"; g.fill(cu); stroke(g, cu, rgba(GUTTER, 0.2), 0.25);
    }
  }

  function drawPageLines(g, t, mood, small, ink) {
    const writing = mood === "thinking", cycle = (t % TURN_CYCLE) / TURN_CYCLE, lw = small ? 2.4 : 1.6;
    const ln = (x0, y0, len, slope, index, mirror) => {
      const p = !writing ? 1 : clamp((cycle - TURN_SHARE) / (1 - TURN_SHARE) * 6 - index, 0, 1);
      if (p <= 0) return;
      const x1 = mirror ? x0 - len * p : x0 + len * p;
      line(g, PAGE_LINE, x0, y0, x1, y0 + slope * len * p, lw);
    };
    if (!small) {
      const hl = !writing ? 1 : clamp((cycle - TURN_SHARE) / (1 - TURN_SHARE) * 6 - 1.5, 0, 1);
      if (hl > 0) { g.save(); g.globalAlpha = 0.55; line(g, "#FFE36B", 16.6, 49.4, 16.6 + 24 * hl, 49.4 + 2.9 * hl, 3.2); g.restore(); }
      if (hl >= 1) {
        const c = new Path2D(); c.moveTo(78.6, 74.4); c.lineTo(79.8, 75.8); c.lineTo(82.4, 72.6); stroke(g, c, "#3FBF8F", 0.9);
        const sx = 21, sy = 54.6, sp = new Path2D();
        sp.moveTo(sx, sy - 1.6); sp.quadraticCurveTo(sx, sy, sx + 1.6, sy); sp.quadraticCurveTo(sx, sy, sx, sy + 1.6); sp.quadraticCurveTo(sx, sy, sx - 1.6, sy); sp.quadraticCurveTo(sx, sy, sx, sy - 1.6);
        g.fillStyle = "rgba(245,184,61,0.85)"; g.fill(sp);
      }
    }
    ln(17, 45, 20, 0.1, 0, false); ln(17, 49, 26, 0.12, 1, false);
    ln(83, 45, 20, 0.1, 2, true); ln(83, 49, 26, 0.12, 3, true);
    if (!small) { ln(17, 79.5, 18, -0.03, 4, false); ln(83, 79.5, 18, -0.03, 5, true); }
  }

  // ---- rosto
  function drawFace(g, s) {
    const INKC = (s.look && s.look.ink) || INK, CHEEKC = s.look && "cheek" in s.look ? s.look.cheek : CHEEK;
    const { t, mood, P, blink, wink, small } = s;
    const gulp = s.gulp || 0;
    const eyeL = [31 + P.lookX * 1.6, 61 + P.lookY * 1.4], eyeR = [69 + P.lookX * 1.6, 61 + P.lookY * 1.4];
    if (CHEEKC) { oval(g, rgba(CHEEKC, P.cheek), 19, 67, 9, 5); oval(g, rgba(CHEEKC, P.cheek), 72, 67, 9, 5); }
    const eye = ([x, y], side, extraLid) => {
      if (mood === "dizzy") {           // espiral girando
        const sp = new Path2D(), a0 = t * 7 * side;
        for (let i = 0; i <= 40; i++) { const a = a0 + i * 0.42, rr = 0.25 + i * 0.12; i ? sp.lineTo(x + cos(a) * rr, y + sin(a) * rr * 1.15) : sp.moveTo(x + cos(a) * rr, y + sin(a) * rr * 1.15); }
        stroke(g, sp, INKC, small ? 1.6 : 1.2); return;
      }
      if (mood === "ecstatic" && blink < 0.5) {   // olhos de estrela, pulsando
        const st = star5(g, x, y, (small ? 6.4 : 5.8) * (1 + 0.1 * sin(t * 9)));
        g.fillStyle = vGrad(g, ["#FFF1BF", GOLD, "#E39A1E"], y - 6, y + 5); g.fill(st);
        g.strokeStyle = INKC; g.lineWidth = small ? 1.2 : 0.8; g.lineJoin = "round"; g.stroke(st);
        if (!small) circle(g, "rgba(255,255,255,0.9)", 0.9, x - 1.2, y - 1.6);
        return;
      }
      const rx = 4.4 * P.eyeScale * (small ? 1.18 : 1), ry = 5.8 * P.eyeScale * (small ? 1.1 : 1);
      const up = clamp(Math.max(P.up, blink, extraLid), 0, 1), low = clamp(P.low * (1 - blink), 0, 1);
      if (small && low > 0.45 && blink < 0.5) {   // pequeno e feliz: o arco clássico (^^) lê melhor
        const p = new Path2D(); p.moveTo(x - 4.5, y + 1.5); p.quadraticCurveTo(x, y - 5, x + 4.5, y + 1.5); stroke(g, p, INKC, 2.8);
        return;
      }
      if (up > 0.96) {         // fechado: só o traço
        const p = new Path2D(); p.moveTo(x - 4.4, y + 0.4); p.quadraticCurveTo(x, y + 2.4, x + 4.4, y + 0.4); stroke(g, p, INKC, small ? 2.2 : 1.8);
        return;
      }
      g.save();
      g.beginPath(); g.ellipse(x, y, rx, ry, 0, 0, 2 * PI); g.fillStyle = INKC; g.fill();
      if (up < 0.85 && low < 0.9) {
        const pr = P.pupil;
        circle(g, "#FFFFFF", 1.7 * pr, x - 1.4 + P.lookX * 0.6, y - 2.2);
        circle(g, "rgba(255,255,255,0.7)", 0.8 * pr, x + 1.6, y + 1.8);
      }
      // pálpebras (cor do papel, cobrindo o olho como uma cortina; a borda é um traço de tinta)
      g.beginPath(); g.ellipse(x, y, rx + 0.6, ry + 0.6, 0, 0, 2 * PI); g.clip();
      if (up > 0.01) {
        const yl = y - ry - 0.8 + (2 * ry + 0.8) * up, dr = P.droop * 2.6;
        const yo = yl + dr, yi = yl - dr * 0.4;                  // caído no canto de fora
        const xa = x - rx - 1, xb = x + rx + 1;
        const ya = side < 0 ? yo : yi, yb = side < 0 ? yi : yo;
        const mid = (ya + yb) / 2 + 1.4 * (1 - up);
        const lid = new Path2D(); lid.moveTo(xa, y - ry - 2); lid.lineTo(xb, y - ry - 2); lid.lineTo(xb, yb); lid.quadraticCurveTo(x, mid, xa, ya); lid.closePath();
        g.fillStyle = PAPER; g.fill(lid);
        const e = new Path2D(); e.moveTo(xa, ya); e.quadraticCurveTo(x, mid, xb, yb); stroke(g, e, INKC, 1.2);
      }
      if (low > 0.01) {
        // de baixo (feliz): sobe curvada e deixa o olho em meia-lua
        const yl = y + ry + 0.8 - (2 * ry + 0.8) * low;
        const lid = new Path2D(); lid.moveTo(x - rx - 1, y + ry + 2); lid.lineTo(x + rx + 1, y + ry + 2); lid.lineTo(x + rx + 1, yl + 2.4 * low); lid.quadraticCurveTo(x, yl - 3.2 * low, x - rx - 1, yl + 2.4 * low); lid.closePath();
        g.fillStyle = PAPER; g.fill(lid);
      }
      g.restore();
    };
    eye(eyeL, -1, 0);
    eye(eyeR, 1, wink);
    // sobrancelhas: sobem e descem; browIn levanta a ponta de dentro (triste); browR ergue só a direita
    // pequeno, a sobrancelha só aparece quando muda a expressão (neutra, ela só embola o rosto)
    const browA = small ? clamp((abs(P.browY) + P.browIn * 2 + P.browR) / 1.2 - 0.3, 0, 1) : P.browShow;
    if (browA > 0.05) for (const [[x, y], side] of [[eyeL, -1], [eyeR, 1]]) {
      const by = y - 9.8 + P.browY - (side > 0 ? P.browR : 0) + (blink > 0.5 ? 0.6 : 0);
      const inner = [x - side * 4.2, by - P.browIn * 2.2 + (1 - P.browIn) * 0.4], outer = [x + side * 4.4, by + P.browIn * 1.2];
      const b = new Path2D(); b.moveTo(outer[0], outer[1]); b.quadraticCurveTo(x, by - 1.4 + P.browIn * 0.6, inner[0], inner[1]);
      stroke(g, b, rgba(INKC, 0.9 * browA), small ? 2.2 : 1.7);
    }
    // lágrima (triste)
    if (mood === "sad" && !small) {
      const tp = (t % 3.2) / 3.2;
      if (tp < 0.8) {
        const ty = 64.5 + tp * 9, ta = tp < 0.1 ? tp / 0.1 : 1 - Math.max(0, tp - 0.6) / 0.2;
        const d = new Path2D(); d.moveTo(36.2, ty - 2.2); d.quadraticCurveTo(38.2, ty + 0.4, 36.2, ty + 1.2); d.quadraticCurveTo(34.2, ty + 0.4, 36.2, ty - 2.2);
        g.fillStyle = "rgba(126,200,255," + (0.85 * ta) + ")"; g.fill(d); circle(g, "rgba(255,255,255," + (0.8 * ta) + ")", 0.35, 35.8, ty - 0.2);
      }
    }
    // boca: largura, curva (sorri > 0, triste < 0) e abertura, tudo por mola
    const mx = 50, my = 73;
    if (gulp > 0.08) {
      const r = 1.5 + 3.2 * gulp;
      oval(g, INKC, mx - r, my - r * 1.1, r * 2, r * 2.2);
      for (let i = 0; i < 4; i++) { const a = i * (PI / 2) + 0.5; circle(g, rgba(SPARK, gulp), 1.8 * gulp * 0.9, 50 + cos(a) * (22 + 14 * (1 - gulp)), 58 + sin(a) * (16 + 10 * (1 - gulp))); }
      return;
    }
    let open = P.mOpen, w = P.mW, c = P.mCurve;
    if (mood === "talking") {
      if (s.speech) { open = s.speech.open; w = 4.6 - 1.7 * s.speech.round; c = 2.4 * (1 - s.speech.round); }   // boca no ritmo do texto
      else { open = (0.5 + 0.5 * sin(t * 13)) * (0.55 + 0.45 * abs(sin(t * 3.1))); w = 4.2 + 0.8 * open; }
    }
    if (mood === "surprised") {         // "Oh!"
      oval(g, INKC, mx - 2.6, my - 3, 5.2, 6.4); oval(g, CHEEK, mx - 1.6, my + 0.9, 3.2, 1.8); return;
    }
    if (mood === "dizzy") {             // boca ondulada
      const wv = new Path2D(); for (let i = 0; i <= 12; i++) { const x = mx - 5 + i * (10 / 12), y = my + sin(i * 1.4 + t * 8) * 0.9; i ? wv.lineTo(x, y) : wv.moveTo(x, y); }
      stroke(g, wv, INKC, small ? 2.4 : 1.8); return;
    }
    if (mood === "sad") c += sin(t * 9) * 0.35;
    const corner = my - c * 0.3;
    if (open < 0.12) {
      const p = new Path2D(); p.moveTo(mx - w, corner); p.quadraticCurveTo(mx, corner + c * 1.6, mx + w, corner);
      stroke(g, p, INKC, small ? 2.8 : 2.3);
      if (P.tongue > 0.5) { oval(g, CHEEK, mx + w - 2.4, corner - 0.2, 2.6, 2.2); stroke(g, p, INKC, small ? 2.8 : 2.3); }   // língua no canto (concentrado)
    } else {
      const top = corner + c * 0.15 - open * 0.6, depth = Math.max(1.4, c * 1.1) + open * 5.4;
      const p = new Path2D(); p.moveTo(mx - w, corner); p.quadraticCurveTo(mx, top, mx + w, corner); p.bezierCurveTo(mx + w * 0.95, corner + depth * 1.25, mx - w * 0.95, corner + depth * 1.25, mx - w, corner); p.closePath();
      g.fillStyle = INKC; g.fill(p);
      g.save(); g.clip(p); oval(g, CHEEK, mx - w * 0.55, corner + depth - 1.6 * open - 0.6, w * 1.1, 3.4 * open + 1); g.restore();
    }
  }

  function drawRibbon(g, t, mood, colors, pat) {
    const cs = colors || RIBBONS.menta, [dark, base, light] = cs;
    const sad = mood === "sad", sway = sad ? 0 : sin(t * 1.9 + 0.6) * 1.3;
    const w = 3.4, x0 = 51.4, lieTop = 86, edge = 94.6, hang = sad ? 3.2 : 5.4;
    const tipY = edge + hang, tipX = x0 + 0.5 + sway;
    if (pat === "cabo" || pat === "clipes") { drawRibbonObject(g, t, pat, cs, x0 + w / 2, lieTop, edge, tipX + w / 2, tipY, sway); return; }
    const r = new Path2D();
    r.moveTo(x0, lieTop); r.lineTo(x0 + w, lieTop); r.lineTo(x0 + w + 0.3, edge);
    r.quadraticCurveTo(x0 + w + 0.3 + sway * 0.3, edge + hang * 0.5, tipX + w, tipY);
    r.lineTo(tipX + w / 2, tipY - 1.6); r.lineTo(tipX, tipY);
    r.quadraticCurveTo(x0 + 0.3 + sway * 0.3, edge + hang * 0.5, x0 + 0.3, edge);
    r.closePath();
    g.save(); g.translate(0.7, 0.5); g.fillStyle = "rgba(14,10,69,0.35)"; g.fill(r); g.restore();
    if (cs.length === 4) { g.fillStyle = vGrad(g, cs, lieTop, tipY); g.fill(r); g.fillStyle = hGrad(g, ["rgba(0,0,0,0.12)", "rgba(255,255,255,0.25)", "rgba(0,0,0,0.08)"], x0, x0 + w + 0.6 + sway); g.fill(r); }
    else { g.fillStyle = hGrad(g, [dark, base, light, base], x0, x0 + w + 0.6 + sway); g.fill(r); }
    if (pat) { g.save(); g.clip(r); drawRibbonPattern(g, t, pat, x0, lieTop, tipY, w, sway); g.restore(); }
    g.fillStyle = vGrad(g, ["rgba(14,10,69,0.45)", "rgba(14,10,69,0)"], 88, 91.5); g.fillRect(x0, 88, w + 0.3, 3.5);
    line(g, "rgba(255,255,255,0.55)", x0 + 0.5, edge - 0.4, x0 + w, edge - 0.4, 0.5, false);
    g.fillStyle = vGrad(g, [rgba(dark, 0.55), rgba(dark, 0)], edge, edge + 1.8); g.fillRect(x0 + 0.3, edge, w, 1.8);
  }
  // Estampa por cima da fita (já recortada no formato dela).
  function drawRibbonPattern(g, t, pat, x0, y0, y1, w, sway) {
    if (pat === "camo") {
      // manchas de camuflado em três tons
      for (const [x, y, r, c] of [[0.8, 88, 1.3, "rgba(38,46,18,0.6)"], [2.6, 91, 1.1, "rgba(110,86,48,0.55)"], [1.2, 94, 1.2, "rgba(38,46,18,0.6)"], [2.8, 96.6, 1, "rgba(178,186,120,0.55)"], [1.4, 98.6, 0.9, "rgba(110,86,48,0.55)"]]) {
        g.beginPath(); g.ellipse(x0 + x + sway * (y - y0) / (y1 - y0), y, r * 1.2, r * 0.8, 0.5, 0, 2 * PI); g.fillStyle = c; g.fill();
      }
    } else if (pat === "zebra") {
      // fita zebrada de isolamento: listras pretas em diagonal
      for (let y = y0 - 4; y < y1 + 2; y += 2.2) { const p = new Path2D(); p.moveTo(x0 - 1, y); p.lineTo(x0 + w + 3, y + 3); p.lineTo(x0 + w + 3, y + 4); p.lineTo(x0 - 1, y + 1); p.closePath(); g.fillStyle = "rgba(24,20,10,0.9)"; g.fill(p); }
    } else if (pat === "cedula") {
      // verde-cédula: guilhoché (ondinhas finas) e o filete claro no meio, como nota de dinheiro
      for (let k = 0; k < 3; k++) { const p = new Path2D(); for (let y = y0; y <= y1; y += 0.5) { const x = x0 + 0.7 + k * 1 + sin(y * 2.2 + k) * 0.35 + sway * (y - y0) / (y1 - y0); y === y0 ? p.moveTo(x, y) : p.lineTo(x, y); } stroke(g, p, "rgba(230,255,235,0.45)", 0.18); }
      line(g, "rgba(255,255,255,0.35)", x0 + w / 2, y0, x0 + w / 2 + sway, y1, 0.3, false);
    } else if (pat === "brilho") {
      // ouro velho: um brilho fosco que desce pela fita de vez em quando
      const sy = y0 - 6 + ((t * 0.3) % 1) * 22, sh = g.createLinearGradient(0, sy, 0, sy + 4);
      sh.addColorStop(0, "rgba(255,240,190,0)"); sh.addColorStop(0.5, "rgba(255,240,190,0.55)"); sh.addColorStop(1, "rgba(255,240,190,0)"); g.fillStyle = sh; g.fillRect(x0 - 2, sy, w + 6, 4);
    } else if (pat === "giz") {
      // giz colorido: pó de giz pontilhado e riscos claros
      for (let i = 0; i < 16; i++) circle(g, "rgba(255,255,255,0.55)", 0.18 + (i % 3) * 0.08, x0 + 0.4 + ((i * 1.7) % w), y0 + 1 + ((i * 2.9) % (y1 - y0 - 2)));
      for (const y of [89, 93.5, 97]) line(g, "rgba(255,255,255,0.4)", x0 + 0.6, y, x0 + w - 0.4, y - 0.5, 0.25);
    }
  }
  // Fitas que viram objeto: o cabo de rede (com plugue na ponta) e a corrente de clipes.
  function drawRibbonObject(g, t, pat, cs, xa, ya, yb, xt, yt, sway) {
    const path = new Path2D(); path.moveTo(xa, ya); path.lineTo(xa + 0.15, yb); path.quadraticCurveTo(xa + 0.15 + sway * 0.3, yb + (yt - yb) * 0.5, xt, yt - (pat === "cabo" ? 2.6 : 0));
    if (pat === "cabo") {
      g.save(); g.translate(0.6, 0.5); stroke(g, path, "rgba(14,10,69,0.35)", 1.9); g.restore();
      stroke(g, path, "#123C80", 1.9); stroke(g, path, cs[1], 1.4); stroke(g, path, rgba(cs[2], 0.7), 0.35);
      // plugue RJ45: corpo transparente com os contatos dourados e a trava
      g.save(); g.translate(xt, yt - 1.6); g.rotate(sway * 0.06);
      const pl = new Path2D(); pl.roundRect(-1.4, -1.4, 2.8, 3.2, 0.5);
      g.fillStyle = vGrad(g, ["rgba(240,248,255,0.95)", "rgba(190,210,235,0.95)"], -1.4, 1.8); g.fill(pl); stroke(g, pl, "#5A6E90", 0.3);
      for (let i = 0; i < 4; i++) line(g, GOLD, -0.9 + i * 0.6, 0.6, -0.9 + i * 0.6, 1.6, 0.25, false);
      const tr = new Path2D(); tr.moveTo(-0.6, -1.4); tr.lineTo(-0.4, -2.2); tr.lineTo(0.4, -2.2); tr.lineTo(0.6, -1.4); stroke(g, tr, "#5A6E90", 0.3);
      g.restore();
      return;
    }
    // corrente de clipes: um clipe atrás do outro ao longo da fita, girando de leve
    const pts = [[xa, ya + 1.2], [xa + 0.1, ya + 4], [xa + 0.15, yb - 0.6], [xa + 0.15 + sway * 0.25, yb + (yt - yb) * 0.45], [xt, yt - 1.6]];
    pts.forEach(([x, y], i) => {
      g.save(); g.translate(x, y); g.rotate((i % 2 ? 0.18 : -0.18) + sway * 0.04);
      const c = new Path2D(); c.moveTo(-0.5, 1.2); c.lineTo(-0.5, -1); c.arc(0, -1, 0.5, PI, 0); c.lineTo(0.5, 1.4); c.arc(0.05, 1.4, 0.45, 0, PI); c.lineTo(-0.4, -0.6); c.arc(0, -0.6, 0.4, PI, 0); c.lineTo(0.4, 0.8);
      stroke(g, c, "rgba(14,10,69,0.55)", 0.55); stroke(g, c, i % 3 === 1 ? "#F06A85" : cs[1], 0.35); stroke(g, c, "rgba(255,255,255,0.7)", 0.1);
      g.restore();
    });
  }

  // ---- companheiro: um bichinho (ou plantinha) no chão, ao lado esquerdo do Folha
  // Fica no chão (não pula junto) e vive em função dele: olha para o Folha, de tempos em tempos manda um
  // coraçãozinho, chega pertinho e consola quando ele fica triste, cochila junto e comemora do seu jeito
  // (a plantinha joga pétalas, o patinho grasna, o gato bate "toca aqui", a coruja canta, o robô fica de
  // olhos de coração, o coelho espalha estrelinhas e o dragão solta anéis de fumaça).
  function drawPet(g, t, kind, mood, small, burst) {
    if (!kind || kind === "nenhum" || small) return;   // pequeno demais, só atrapalharia
    const party = /^(happy|ecstatic|proud|wave|present)$/.test(mood), sleepy = mood === "sleepy", sad = mood === "sad";
    const hop = party ? -abs(sin(t * 5.2 + 0.8)) * 3 : burst > 0 ? -sin(PI * (1 - burst)) * 4 : sad ? 0.3 : -abs(sin(t * 1.3)) * 0.4;
    const blink = sleepy || (t % 3.7) < 0.13;
    const x = sad ? -3 : -6, base = 95, k = 1.45;
    g.save(); g.translate(x, base); g.scale(1, 0.22); circle(g, rgba(INK, 0.2), (7 + hop * 0.3) * k, 0, 0); g.restore();
    g.save(); g.translate(x, base + hop * k); g.scale(k, k);
    let tearAt = null;
    const eye = (ex, ey, r = 0.75) => {
      if (sad && !sleepy) {   // olhinho triste: pupila menor, sobrancelha subindo por dentro e uma lágrima no primeiro olho
        const inner = ex < 0 ? 1 : -1;
        circle(g, INK, r * 0.8, ex, ey + 0.2); circle(g, "#FFFFFF", r * 0.3, ex - r * 0.25, ey - 0.1);
        line(g, INK, ex - inner * r * 1.4, ey - r * 1.5, ex + inner * r * 1.1, ey - r * 2.4, 0.45);
        if (!tearAt) tearAt = [ex, ey + r];
        return;
      }
      if (blink) { const p = new Path2D(); p.moveTo(ex - r * 1.2, ey); p.quadraticCurveTo(ex, ey + r * 0.9, ex + r * 1.2, ey); stroke(g, p, INK, 0.5); return; }
      circle(g, INK, r, ex + r * 0.28, ey - r * 0.15); circle(g, "#FFFFFF", r * 0.4, ex - r * 0.02, ey - r * 0.5);
    };
    const smile = (mx, my, w = 1.1) => { const p = new Path2D(); p.moveTo(mx - w, my); p.quadraticCurveTo(mx, my + w * (party ? 1.2 : 0.7), mx + w, my); stroke(g, p, INK, 0.45); };
    // elipse preenchida (devolve o caminho para contornar) e o contorno de tinta suave dos bichinhos novos
    const ell = (cx, cy, rx, ry, f, rot = 0) => { const p = new Path2D(); p.ellipse(cx, cy, rx, ry, rot, 0, 2 * PI); g.fillStyle = f; g.fill(p); return p; };
    const OUT = "rgba(38,33,92,0.35)";
    if (kind === "planta") {
      // vaso de barro com folhas que balançam (e uma flor quando ele comemora)
      const sway = sin(t * 1.4) * 0.15;
      for (const [a, len, c] of [[-0.55, 9, "#2E9E72"], [0.05, 11, "#3FBF8F"], [0.6, 8.5, "#2E9E72"]]) {
        g.save(); g.translate(0, -8); g.rotate(a + sway);
        line(g, "#2E9E72", 0, 0, 0, -len * 0.7, 0.6);
        g.beginPath(); g.ellipse(0, -len * 0.62, 2, len * 0.4, 0, 0, 2 * PI); g.fillStyle = c; g.fill();
        line(g, "rgba(255,255,255,0.35)", 0, -len * 0.3, 0, -len * 0.9, 0.3);
        g.restore();
      }
      if (party) { g.save(); g.translate(sin(t * 1.4) * 1.2, -19); for (let i = 0; i < 5; i++) { const a = i * 2 * PI / 5 + t; circle(g, "#FF8FA3", 1.1, cos(a) * 1.3, sin(a) * 1.3); } circle(g, GOLD, 0.8, 0, 0); g.restore(); }
      const pot = new Path2D(); pot.moveTo(-5, -7); pot.lineTo(5, -7); pot.lineTo(4, 0); pot.lineTo(-4, 0); pot.closePath();
      g.fillStyle = hGrad(g, ["#E8895A", "#D9774B", "#B85A33"], -5, 5); g.fill(pot);
      const rim = new Path2D(); rim.roundRect(-5.8, -8.6, 11.6, 2.2, 0.8); g.fillStyle = "#C4643A"; g.fill(rim);
      eye(-1.8, -4.2, 0.6); eye(1.8, -4.2, 0.6); smile(0, -2.6, 0.9);
      circle(g, rgba(CHEEK, 0.6), 0.7, -3, -3); circle(g, rgba(CHEEK, 0.6), 0.7, 3, -3);
    } else if (kind === "patinho") {
      // patinho de borracha, balançando como na água
      g.rotate(sin(t * 2) * 0.06);
      const body = new Path2D(); body.ellipse(0, -4.4, 6.2, 4.3, 0, 0, 2 * PI); g.fillStyle = vGrad(g, ["#FFE27A", "#FFD84A", "#F0B400"], -9, 0); g.fill(body);
      const tail = new Path2D(); tail.moveTo(-5.4, -6); tail.lineTo(-7.6, -9); tail.lineTo(-4.2, -7.6); tail.closePath(); g.fillStyle = "#F0B400"; g.fill(tail);
      const wing = new Path2D(); wing.moveTo(-2.6, -5.4); wing.quadraticCurveTo(0.6, -3, 2.4, -5); stroke(g, wing, "#E0A000", 0.6);
      circle(g, "#FFD84A", 3.5, 2.6, -10.2);
      g.beginPath(); g.ellipse(6, -9.6, 2, 1, 0.1, 0, 2 * PI); g.fillStyle = "#FF8A2B"; g.fill();
      eye(3.6, -11.1, 0.7); circle(g, rgba(CHEEK, 0.7), 0.8, 2.2, -9.2);
      g.beginPath(); g.ellipse(1, -12.4, 1.2, 0.6, -0.4, 0, 2 * PI); g.fillStyle = "rgba(255,255,255,0.6)"; g.fill();
    } else if (kind === "gatinho") {
      // gatinho sentado, rabo abanando e bigode
      const tw = sin(t * 2.2) * 2;
      const tail = new Path2D(); tail.moveTo(4, -2); tail.quadraticCurveTo(10, -3 + tw * 0.3, 8.5 + tw * 0.4, -10 + tw); stroke(g, tail, "#D9803A", 1.8);
      const body = new Path2D(); body.ellipse(0, -4.6, 5, 4.8, 0, 0, 2 * PI); g.fillStyle = vGrad(g, ["#F7B36E", "#E58F45"], -9, 0); g.fill(body);
      g.beginPath(); g.ellipse(0, -3.6, 2.6, 3, 0, 0, 2 * PI); g.fillStyle = "#FFE7CF"; g.fill();
      for (const s of [-1, 1]) { const ear = new Path2D(); ear.moveTo(s * 3.8, -14.4); ear.lineTo(s * 2.6, -18.4); ear.lineTo(s * 0.6, -15.4); ear.closePath(); g.fillStyle = "#E58F45"; g.fill(ear);
        const inner = new Path2D(); inner.moveTo(s * 3, -14.8); inner.lineTo(s * 2.5, -17); inner.lineTo(s * 1.3, -15.3); inner.closePath(); g.fillStyle = "#FFB3C7"; g.fill(inner); }
      circle(g, "#F7B36E", 4.5, 0, -12);
      line(g, "rgba(200,110,40,0.6)", -1, -16, -1, -14.6, 0.5); line(g, "rgba(200,110,40,0.6)", 1, -16, 1, -14.6, 0.5);
      eye(-1.7, -12.4, 0.8); eye(1.7, -12.4, 0.8);
      const nose = new Path2D(); nose.moveTo(-0.5, -10.9); nose.lineTo(0.5, -10.9); nose.lineTo(0, -10.3); nose.closePath(); g.fillStyle = "#FF8FA3"; g.fill(nose);
      const m = new Path2D(); m.moveTo(-1, -9.8); m.quadraticCurveTo(-0.5, -9.2, 0, -9.9); m.quadraticCurveTo(0.5, -9.2, 1, -9.8); stroke(g, m, INK, 0.4);
      for (const s of [-1, 1]) { line(g, "rgba(38,33,92,0.5)", s * 1.6, -10.4, s * 4.8, -10.9, 0.25); line(g, "rgba(38,33,92,0.5)", s * 1.6, -10, s * 4.6, -9.6, 0.25); }
      circle(g, rgba(CHEEK, 0.6), 0.8, -2.8, -10.6); circle(g, rgba(CHEEK, 0.6), 0.8, 2.8, -10.6);
      for (const s of [-1, 1]) { g.beginPath(); g.ellipse(s * 2, -0.6, 1.5, 0.9, 0, 0, 2 * PI); g.fillStyle = "#FFF4E6"; g.fill(); }
    } else if (kind === "coruja") {
      // corujinha estudiosa, de capelo, piscando devagar e batendo as asas quando comemora
      const flap = party ? sin(t * 12) * 0.5 : 0;
      for (const s of [-1, 1]) { g.save(); g.translate(s * 4.6, -8); g.rotate(s * (0.3 + flap)); g.beginPath(); g.ellipse(0, 1.5, 1.8, 4, 0, 0, 2 * PI); g.fillStyle = "#7A5230"; g.fill(); g.restore(); }
      const body = new Path2D(); body.ellipse(0, -7, 5.4, 7, 0, 0, 2 * PI); g.fillStyle = vGrad(g, ["#B9834C", "#8C5E33"], -14, 0); g.fill(body);
      g.beginPath(); g.ellipse(0, -4.6, 3.6, 4.4, 0, 0, 2 * PI); g.fillStyle = "#F1DDB3"; g.fill();
      for (let i = 0; i < 3; i++) { const p = new Path2D(); p.moveTo(-1.2, -6 + i * 1.6); p.lineTo(0, -5.2 + i * 1.6); p.lineTo(1.2, -6 + i * 1.6); stroke(g, p, "rgba(140,94,51,0.6)", 0.35); }
      for (const s of [-1, 1]) { const tuft = new Path2D(); tuft.moveTo(s * 4.4, -11.4); tuft.lineTo(s * 4.4, -14.6); tuft.lineTo(s * 2.4, -12.6); tuft.closePath(); g.fillStyle = "#8C5E33"; g.fill(tuft); }
      for (const s of [-1, 1]) { circle(g, "#FFFFFF", 2.3, s * 2.2, -10); g.strokeStyle = "#C98A4B"; g.lineWidth = 0.4; g.beginPath(); g.arc(s * 2.2, -10, 2.3, 0, 2 * PI); g.stroke(); eye(s * 2.2, -10, 1.1); }
      const beak = new Path2D(); beak.moveTo(-0.8, -8.6); beak.lineTo(0.8, -8.6); beak.lineTo(0, -7.2); beak.closePath(); g.fillStyle = "#FF8A2B"; g.fill(beak);
      const cap = new Path2D(); cap.moveTo(0, -17.2); cap.lineTo(5, -15.4); cap.lineTo(0, -13.6); cap.lineTo(-5, -15.4); cap.closePath(); g.fillStyle = CAP; g.fill(cap);
      line(g, GOLD, 0, -15.4, 4, -15, 0.4); line(g, GOLD, 4, -15, 4.2 + sin(t * 2.6) * 0.4, -12.6, 0.45);
    } else if (kind === "robo") {
      // robozinho: antena piscando, rosto de tela e esteiras
      const tread = new Path2D(); tread.roundRect(-5.2, -2.8, 10.4, 2.8, 1.4); g.fillStyle = "#2D3042"; g.fill(tread);
      for (let i = -1; i <= 1; i++) circle(g, "#8E92A6", 0.7, i * 3, -1.4);
      const body = new Path2D(); body.roundRect(-4.2, -9.4, 8.4, 6.8, 1.6); g.fillStyle = vGrad(g, ["#5A4FE6", "#3A2FC4"], -9.4, -2.6); g.fill(body);
      g.fillStyle = "#FF8FA3"; g.fill(heart(0, -6.2, 1.3));
      const head = new Path2D(); head.roundRect(-5.2, -17.6, 10.4, 8, 2.2); g.fillStyle = vGrad(g, ["#E3E6F0", "#B7B4D0"], -17.6, -9.6); g.fill(head);
      const scr = new Path2D(); scr.roundRect(-4, -16.4, 8, 5.6, 1.4); g.fillStyle = "#1E1B2E"; g.fill(scr);
      for (const s of [-1, 1]) { if (blink) line(g, MINT, s * 1.8 - 0.8, -13.4, s * 1.8 + 0.8, -13.4, 0.5); else if (party) { g.fillStyle = "#FF8FA3"; g.fill(heart(s * 1.8 + 0.3, -13.4, 1)); } else { const e = new Path2D(); e.roundRect(s * 1.8 - 0.4, -14.8, 1.4, 2.2, 0.6); g.fillStyle = MINT; g.fill(e); } }
      const sm = new Path2D(); sm.moveTo(-1, -12); sm.quadraticCurveTo(0, -11.2, 1, -12); stroke(g, sm, MINT, 0.4);
      line(g, "#8E92A6", 0, -17.6, 0, -20.2, 0.5);
      circle(g, sin(t * 4) > 0 ? "#FF5C4D" : MINT, 0.9, 0, -20.6);
      line(g, "rgba(255,255,255,0.6)", -4.4, -16.6, -2.6, -17, 0.4);
    } else if (kind === "coelho") {
      // coelhinho: orelhas compridas que balançam, nariz que mexe e pompom de rabo
      const ear = sin(t * 2.2) * 0.12, twitch = abs(sin(t * 7)) * 0.4;
      for (const s of [-1, 1]) {
        g.save(); g.translate(s * 1.6, -14); g.rotate(s * 0.18 + ear * s + (party ? sin(t * 9) * 0.15 : 0) + (sad ? s * 0.9 : 0));
        const e = new Path2D(); e.ellipse(0, -5, 1.7, 5.4, 0, 0, 2 * PI); g.fillStyle = "#F6F2FF"; g.fill(e); stroke(g, e, "rgba(150,140,190,0.6)", 0.3);
        const inner = new Path2D(); inner.ellipse(0, -5, 0.8, 4.2, 0, 0, 2 * PI); g.fillStyle = "#FFC2D4"; g.fill(inner);
        g.restore();
      }
      circle(g, "#FFFFFF", 1.8, -4.6, -3);
      const body = new Path2D(); body.ellipse(0, -4.6, 5, 4.6, 0, 0, 2 * PI); g.fillStyle = vGrad(g, ["#FFFFFF", "#E6E1F5"], -9, 0); g.fill(body); stroke(g, body, "rgba(150,140,190,0.5)", 0.3);
      const head = new Path2D(); head.ellipse(0, -11.6, 4.4, 4, 0, 0, 2 * PI); g.fillStyle = vGrad(g, ["#FFFFFF", "#ECE8F8"], -15.6, -7.6); g.fill(head); stroke(g, head, "rgba(150,140,190,0.5)", 0.3);
      eye(-1.6, -12, 0.75); eye(1.6, -12, 0.75);
      const nose = new Path2D(); nose.moveTo(-0.5, -10.4 - twitch * 0.3); nose.lineTo(0.5, -10.4 - twitch * 0.3); nose.lineTo(0, -9.8); nose.closePath(); g.fillStyle = "#FF8FA3"; g.fill(nose);
      const m = new Path2D(); m.moveTo(-0.9, -9.2); m.quadraticCurveTo(-0.45, -8.7, 0, -9.4); m.quadraticCurveTo(0.45, -8.7, 0.9, -9.2); stroke(g, m, INK, 0.35);
      circle(g, rgba(CHEEK, 0.6), 0.8, -2.8, -10.4); circle(g, rgba(CHEEK, 0.6), 0.8, 2.8, -10.4);
      for (const s of [-1, 1]) { g.beginPath(); g.ellipse(s * 2.1, -0.5, 1.6, 0.9, 0, 0, 2 * PI); g.fillStyle = "#F6F2FF"; g.fill(); }
    } else if (kind === "dragao") {
      // dragãozinho bebê, virado para o Folha: corpo gordinho com a barriga de placas, espinhos dourados nas
      // costas, asas de morcego com nervuras que batem (rápido na festa), rabo enrolado com a ponta de espada,
      // focinho com narinas, chifrinhos e o dentinho no sorriso; na festa ele solta fogo
      const flap = sin(t * (party ? 10 : 2.6)) * (party ? 0.45 : 0.22), sw = sin(t * 1.8) * 0.6, OL = "rgba(14,86,62,0.55)";
      // asas (atrás)
      for (const s of [-1, 1]) {
        g.save(); g.translate(-1.2 + s * 0.6, -9.6); g.rotate(-0.35 - s * 0.25 + flap * (s < 0 ? 1 : 0.7)); if (s > 0) g.scale(0.82, 0.82);
        const w = new Path2D(); w.moveTo(0, 0); w.quadraticCurveTo(-4, -7.5, -9.4, -6.6); w.quadraticCurveTo(-8.4, -4.2, -9, -2.4); w.quadraticCurveTo(-7, -2.8, -6.6, -0.6); w.quadraticCurveTo(-4.6, -1.4, -3.8, 1.4); w.quadraticCurveTo(-2, 0.6, 0, 1.6); w.closePath();
        g.fillStyle = vGrad(g, s < 0 ? ["#B995F5", "#7B4FE0"] : ["#9E7AE8", "#6A3FCC"], -7, 2); g.fill(w); stroke(g, w, "rgba(60,30,130,0.55)", 0.35);
        for (const [x, y] of [[-9.4, -6.6], [-9, -2.4], [-3.8, 1.4]]) line(g, "rgba(70,35,150,0.7)", 0, 0, x * 0.92, y * 0.92, 0.35);
        g.restore();
      }
      // rabo enrolado com a ponta de espada
      const tail = new Path2D(); tail.moveTo(-3, -2.2); tail.bezierCurveTo(-9, -1, -11.4, -4.6, -9.6 + sw * 0.3, -8.2); tail.quadraticCurveTo(-8.6, -9.8, -7.4 + sw * 0.4, -8.8);
      stroke(g, tail, "#1A8A66", 2.4); stroke(g, tail, "#3CC795", 1.3);
      const tp = new Path2D(); const tx = -7.4 + sw * 0.4, ty = -8.8; tp.moveTo(tx - 0.6, ty + 0.4); tp.lineTo(tx + 1.4, ty - 2.2); tp.lineTo(tx + 1.6, ty + 0.9); tp.closePath(); g.fillStyle = "#9563FF"; g.fill(tp);
      // pezinhos
      for (const fx of [-2.2, 2.4]) { g.beginPath(); g.ellipse(fx, -0.6, 2, 1.1, 0, 0, 2 * PI); g.fillStyle = "#1F9E75"; g.fill(); for (const c of [-0.8, 0, 0.8]) circle(g, "#F6FFF9", 0.28, fx + 0.8 + c, -0.2); }
      // corpo e barriga de placas
      const body = new Path2D(); body.moveTo(0, -11); body.bezierCurveTo(4.6, -11, 5.6, -4.6, 4.6, -1.6); body.quadraticCurveTo(3.4, 0.2, 0, 0.2); body.quadraticCurveTo(-3.6, 0.2, -4.8, -1.8); body.bezierCurveTo(-5.6, -5, -4.4, -11, 0, -11); body.closePath();
      const bg = g.createRadialGradient(-1.4, -7.4, 0.4, 0, -5, 7); bg.addColorStop(0, "#6FE6B8"); bg.addColorStop(0.6, "#36C897"); bg.addColorStop(1, "#1A8A66"); g.fillStyle = bg; g.fill(body); stroke(g, body, OL, 0.35);
      const belly = new Path2D(); belly.ellipse(1.2, -4.4, 2.7, 3.7, 0.1, 0, 2 * PI); g.fillStyle = vGrad(g, ["#FFF6D6", "#F3E3A8"], -8, -0.8); g.fill(belly);
      g.save(); g.clip(belly); for (let i = 0; i < 4; i++) { const p = new Path2D(); p.moveTo(-1.6, -6.8 + i * 1.6); p.quadraticCurveTo(1.2, -5.8 + i * 1.6, 4, -6.8 + i * 1.6); stroke(g, p, "rgba(190,150,60,0.55)", 0.28); } g.restore();
      // espinhos dourados nas costas
      for (const [x, y, a] of [[-4.2, -8.6, -1.9], [-4.9, -6, -1.75], [-5, -3.4, -1.6]]) { g.save(); g.translate(x, y); g.rotate(a); const sp = new Path2D(); sp.moveTo(-0.9, 0); sp.lineTo(0, -1.8); sp.lineTo(0.9, 0); sp.closePath(); g.fillStyle = "#F5B83D"; g.fill(sp); g.restore(); }
      // cabeça
      const hx = 2.6, hy = -14;
      for (const [x, a] of [[hx - 2.2, -0.5], [hx + 0.6, -0.15]]) { g.save(); g.translate(x, hy - 3.6); g.rotate(a); const h = new Path2D(); h.moveTo(-0.8, 0.6); h.quadraticCurveTo(-0.7, -1.6, 0.6, -2.8); h.quadraticCurveTo(0.4, -1, 0.9, 0.6); h.closePath(); g.fillStyle = vGrad(g, ["#FFE59A", "#E9A82B"], -2.8, 0.6); g.fill(h); g.restore(); }
      const fin = new Path2D(); fin.moveTo(hx - 3.6, hy - 0.6); fin.lineTo(hx - 6, hy - 2.2); fin.lineTo(hx - 3.8, hy + 1.2); fin.closePath(); g.fillStyle = "#9563FF"; g.fill(fin);
      const head = new Path2D(); head.ellipse(hx, hy, 4.4, 4, 0, 0, 2 * PI);
      const hg = g.createRadialGradient(hx - 1.4, hy - 1.8, 0.3, hx, hy, 5); hg.addColorStop(0, "#7AEBC0"); hg.addColorStop(0.65, "#3CC795"); hg.addColorStop(1, "#1F9E75"); g.fillStyle = hg; g.fill(head); stroke(g, head, OL, 0.35);
      const snout = new Path2D(); snout.ellipse(hx + 3.6, hy + 1.2, 2.6, 1.9, 0.1, 0, 2 * PI); g.fillStyle = vGrad(g, ["#6FE6B8", "#36C897"], hy - 0.7, hy + 3.1); g.fill(snout); stroke(g, snout, OL, 0.3);
      circle(g, "#0E563E", 0.32, hx + 4.8, hy + 0.5); circle(g, "#0E563E", 0.32, hx + 5.4, hy + 1.3);
      const sm = new Path2D(); sm.moveTo(hx + 1.4, hy + 2.2); sm.quadraticCurveTo(hx + 3, hy + (party ? 3.6 : 3), hx + 4.8, hy + 2.4); stroke(g, sm, INK, 0.4);
      const fang = new Path2D(); fang.moveTo(hx + 3.2, hy + 2.9); fang.lineTo(hx + 3.7, hy + 3.9); fang.lineTo(hx + 4.1, hy + 2.8); fang.closePath(); g.fillStyle = "#FFFFFF"; g.fill(fang);
      eye(hx + 0.6, hy - 0.8, 1); circle(g, rgba(CHEEK, 0.7), 0.9, hx + 1.2, hy + 1.6);
      if (party) {
        const k = 0.6 + 0.4 * abs(sin(t * 8)), x0 = hx + 6, y0 = hy + 1.2;
        const f = new Path2D(); f.moveTo(x0, y0 - 0.6); f.quadraticCurveTo(x0 + 2.4 + 2 * k, y0 - 3, x0 + 4.6 + 2.5 * k, y0); f.quadraticCurveTo(x0 + 2.4 + 2 * k, y0 + 2.2, x0, y0 + 0.6); f.closePath();
        g.fillStyle = hGrad(g, ["#FFE07A", "#FF9F3A", "#F2552C"], x0, x0 + 6); g.fill(f);
      }
    } else if (kind === "ursinho") {
      // ursinho de pelúcia (saúde): orelhinhas redondas, focinho claro, coração na barriga e um curativo na testa
      g.rotate(sin(t * 1.5) * 0.05);
      for (const s of [-1, 1]) ell(s * 4.3, -3, 1.5, 2.4, "#A8703F", s * 0.3);
      const body = ell(0, -4.6, 5.2, 4.8, vGrad(g, ["#D39B62", "#A8703F"], -9.4, 0)); stroke(g, body, OUT, 0.35);
      ell(0, -3.9, 2.9, 3, "#F3DDBF"); g.fillStyle = "#FF8FA3"; g.fill(heart(0, -4, 1.1));
      for (const s of [-1, 1]) { ell(s * 2.3, -0.6, 1.7, 1, "#C08650"); ell(s * 2.3, -0.5, 0.9, 0.55, "#F3DDBF"); }
      for (const s of [-1, 1]) { const e = ell(s * 3.5, -15.3, 1.9, 1.9, "#B57B45"); stroke(g, e, OUT, 0.3); ell(s * 3.5, -15.2, 1, 1, "#F3C3A8"); }
      const head = ell(0, -11.6, 4.7, 4.3, vGrad(g, ["#DEA76D", "#B57B45"], -15.9, -7.3)); stroke(g, head, OUT, 0.35);
      ell(0, -9.9, 2.2, 1.6, "#F3DDBF"); ell(0, -10.7, 0.85, 0.6, INK); circle(g, "rgba(255,255,255,0.7)", 0.25, -0.3, -10.9);
      eye(-1.8, -12.4, 0.75); eye(1.8, -12.4, 0.75); smile(0, -9.4, 0.8);
      circle(g, rgba(CHEEK, 0.6), 0.8, -3.1, -10.4); circle(g, rgba(CHEEK, 0.6), 0.8, 3.1, -10.4);
      // o curativo: fita bege com a almofadinha no meio e os furinhos
      g.save(); g.translate(-1.6, -14.5); g.rotate(0.45);
      const ba = new Path2D(); ba.roundRect(-2.3, -0.75, 4.6, 1.5, 0.75); g.fillStyle = "#FFD9B8"; g.fill(ba); stroke(g, ba, "rgba(170,110,70,0.6)", 0.25);
      const pad = new Path2D(); pad.roundRect(-0.7, -0.6, 1.4, 1.2, 0.3); g.fillStyle = "#FFF4EA"; g.fill(pad);
      for (const dx of [-1.6, 1.6]) for (const dy of [-0.25, 0.25]) circle(g, "rgba(170,110,70,0.55)", 0.13, dx, dy);
      g.restore();
    } else if (kind === "pastor") {
      // pastor-alemão (militar): orelhas em pé, focinho de máscara escura, sela preta nas costas, coleira
      // verde-oliva com a plaquinha dourada e o rabo peludo que abana (rápido na festa)
      const wag = sin(t * (party ? 9 : 2)) * (party ? 0.5 : 0.2);
      g.save(); g.translate(-4.2, -2.6); g.rotate(-0.4 + wag); const tl = new Path2D(); tl.moveTo(0, 0); tl.quadraticCurveTo(-4, -0.8, -4.8, -6); stroke(g, tl, "#3A2A22", 2.4); stroke(g, tl, "#6B4A33", 1); g.restore();
      const body = ell(-0.4, -4.8, 5.2, 4.9, vGrad(g, ["#D9A15C", "#B07434"], -9.7, 0));
      g.save(); g.clip(body); ell(-3.8, -6.6, 4.2, 3.8, vGrad(g, ["#4A3A30", "#2A1E18"], -10.4, -2.8)); g.restore();
      stroke(g, body, OUT, 0.35);
      ell(1.8, -3.8, 2.3, 3, "#F0CF9C");
      for (const fx of [-1.2, 2.8]) ell(fx, -0.6, 1.6, 1, "#C98A45");
      for (const [a, b, c] of [[[-2.6, -14.4], [-2.8, -19.8], [0.4, -15.8]], [[1.8, -15.8], [4, -20], [4.6, -14.4]]]) {
        const ear = new Path2D(); ear.moveTo(...a); ear.lineTo(...b); ear.lineTo(...c); ear.closePath(); g.fillStyle = "#3A2A22"; g.fill(ear); stroke(g, ear, OUT, 0.3);
        const inn = new Path2D(); inn.moveTo(a[0] * 0.7 + b[0] * 0.3 + 0.3, a[1] * 0.7 + b[1] * 0.3 + 0.6); inn.lineTo(b[0] * 0.85 + (a[0] + c[0]) * 0.075, b[1] * 0.85 + (a[1] + c[1]) * 0.075); inn.lineTo(c[0] * 0.7 + b[0] * 0.3 - 0.3, c[1] * 0.7 + b[1] * 0.3 + 0.6); inn.closePath(); g.fillStyle = "#9A6A45"; g.fill(inn);
      }
      const head = ell(1, -12.2, 4.3, 4, vGrad(g, ["#DDA764", "#B87A38"], -16.2, -8.2));
      g.save(); g.clip(head); ell(0.4, -16, 5, 2.4, "#3A2A22"); g.restore(); stroke(g, head, OUT, 0.35);
      const snout = ell(4.7, -10.6, 2.6, 1.7, vGrad(g, ["#4A3A30", "#2A1E18"], -12.3, -8.9)); stroke(g, snout, OUT, 0.3);
      ell(6.9, -11.1, 0.8, 0.65, "#120E0C"); circle(g, "rgba(255,255,255,0.6)", 0.22, 6.6, -11.4);
      circle(g, "#ECC07F", 0.5, -0.1, -14.2); circle(g, "#ECC07F", 0.5, 2.7, -14.2);
      eye(-0.1, -12.7, 0.75); eye(2.7, -12.7, 0.75);
      const m = new Path2D(); m.moveTo(3.4, -9.4); m.quadraticCurveTo(4.6, -8.6, 6, -9.4); stroke(g, m, "#F0CF9C", 0.4);
      if (party) { const tg = ell(4.9, -8.4, 0.75, 1.05, "#FF7F96"); line(g, "rgba(200,60,90,0.6)", 4.9, -8.9, 4.9, -7.8, 0.2); stroke(g, tg, "rgba(200,60,90,0.5)", 0.2); }
      line(g, "#5B6B2F", -3, -8.3, 4, -8.7, 1.2);
      const tag = ell(1.2, -7.2, 0.85, 0.85, vGrad(g, GOLD3, -8, -6.4)); stroke(g, tag, "#A86A08", 0.25);
      circle(g, rgba(CHEEK, 0.55), 0.7, -1.7, -10.8);
    } else if (kind === "k9") {
      // cão farejador (polícia): orelhas caídas que balançam, focinho que fareja, rabo em pé de ponta branca
      // e o colete azul-marinho com a faixa refletiva e "K9" em amarelo
      const sniff = abs(sin(t * 6)) * 0.35, ear = sin(t * 2.1) * 0.12, tw = sin(t * 3) * 0.8;
      const tl = new Path2D(); tl.moveTo(-4.2, -3.2); tl.quadraticCurveTo(-7, -4.6, -6.6 + tw * 0.3, -9 + tw * 0.2); stroke(g, tl, "#A86530", 1.6); circle(g, "#FFF4E6", 0.9, -6.6 + tw * 0.3, -9 + tw * 0.2);
      const body = ell(-0.2, -4.6, 5.2, 4.7, vGrad(g, ["#D08A48", "#A86530"], -9.3, 0));
      g.save(); g.clip(body);
      const vest = new Path2D(); vest.roundRect(-5.6, -8.8, 9.2, 6.6, 1.2); g.fillStyle = vGrad(g, ["#33438A", "#1A2350"], -8.8, -2.2); g.fill(vest);
      line(g, "#C9D2E8", -5.6, -3.4, 3.6, -3.4, 0.55); line(g, "rgba(255,255,255,0.8)", -5.6, -3.55, 3.6, -3.55, 0.15);
      g.restore(); stroke(g, body, OUT, 0.35);
      // "K9" a traço
      for (const [w, c] of [[0.9, "rgba(14,10,69,0.5)"], [0.42, "#FFD23F"]]) {
        line(g, c, -2.6, -7.4, -2.6, -4.6, w); line(g, c, -2.6, -5.9, -1.2, -7.4, w); line(g, c, -2.1, -6.3, -1.1, -4.6, w);
        g.beginPath(); g.arc(0.4, -6.6, 0.75, 0, 2 * PI); g.strokeStyle = c; g.lineWidth = w; g.stroke(); line(g, c, 1.15, -6.6, 0.9, -4.6, w);
      }
      for (const fx of [-1.6, 2.6]) ell(fx, -0.6, 1.6, 1, "#FFF4E6");
      const head = ell(1, -12.4, 4, 3.8, vGrad(g, ["#D69350", "#AE6A33"], -16.2, -8.6)); stroke(g, head, OUT, 0.35);
      ell(1.2, -13.9, 0.8, 2, "#FFF4E6");
      const muz = ell(3.9, -10.6, 2.5, 1.7, "#FFF4E6"); stroke(g, muz, OUT, 0.25);
      ell(6.1 + sniff * 0.3, -11.2, 0.9, 0.7, INK); circle(g, "rgba(255,255,255,0.6)", 0.22, 5.8 + sniff * 0.3, -11.5);
      for (const [ex, a] of [[-2.6, 0.3 + ear], [4.4, -0.3 - ear]]) { const e = ell(ex, -11.4, 1.6, 3.2, vGrad(g, ["#8A5025", "#6A3A18"], -14.6, -8.2), a); stroke(g, e, OUT, 0.3); }
      eye(-0.2, -12.9, 0.75); eye(2.4, -12.9, 0.75);
      const m = new Path2D(); m.moveTo(3, -9.6); m.quadraticCurveTo(4.2, -8.8, 5.4, -9.6); stroke(g, m, INK, 0.38);
      circle(g, rgba(CHEEK, 0.55), 0.7, 1.6, -10.4);
    } else if (kind === "tartaruga") {
      // tartaruga sábia (tribunais): casco de domo com placas hexagonais e borda creme, pescoço que espicha
      // devagar e os óculos redondos de leitura
      const nk = sin(t * 0.9) * 0.5 + (party ? 0.8 : 0);
      for (const fx of [-3.8, 3.2]) { const f = ell(fx, -0.9, 1.6, 1.2, "#7FBF6A"); stroke(g, f, OUT, 0.25); }
      const tail = new Path2D(); tail.moveTo(-5.8, -2.8); tail.lineTo(-8, -1.6); tail.lineTo(-5.6, -1.4); tail.closePath(); g.fillStyle = "#7FBF6A"; g.fill(tail);
      const neck = new Path2D(); neck.moveTo(3.4, -3); neck.quadraticCurveTo(5.6, -3.6, 6.4 + nk, -6.6); stroke(g, neck, "#6FAE5C", 2.9); stroke(g, neck, "#8FCC78", 2.1);
      const hx = 6.8 + nk, hy = -7.6;
      const head = ell(hx, hy, 2.7, 2.4, vGrad(g, ["#A8DC92", "#78B862"], hy - 2.4, hy + 2.4)); stroke(g, head, OUT, 0.3);
      const shell = new Path2D(); shell.moveTo(-6.4, -2.2); shell.bezierCurveTo(-6.2, -10.6, 5.8, -10.6, 6, -2.2); shell.closePath();
      g.fillStyle = vGrad(g, ["#86C563", "#4E8A3E"], -8.6, -2.2); g.fill(shell);
      g.save(); g.clip(shell);
      const hex = (cx, cy, r) => { const p = new Path2D(); for (let i = 0; i < 6; i++) { const a = i * PI / 3; i ? p.lineTo(cx + cos(a) * r, cy + sin(a) * r * 0.8) : p.moveTo(cx + cos(a) * r, cy + sin(a) * r * 0.8); } p.closePath(); return p; };
      for (const [cx, cy, r] of [[-0.2, -5.6, 2.1], [-4.4, -4, 1.8], [4, -4, 1.8], [-2.4, -8.6, 1.5], [2.2, -8.6, 1.5]]) { const p = hex(cx, cy, r); g.fillStyle = "rgba(170,220,120,0.45)"; g.fill(p); stroke(g, p, "rgba(40,80,30,0.55)", 0.35); }
      g.restore(); stroke(g, shell, OUT, 0.35);
      const rim = new Path2D(); rim.roundRect(-6.9, -2.9, 13.4, 1.5, 0.75); g.fillStyle = vGrad(g, ["#E8D49A", "#C9B27A"], -2.9, -1.4); g.fill(rim); stroke(g, rim, OUT, 0.25);
      ell(-2.8, -7.8, 1.6, 0.7, "rgba(255,255,255,0.35)", -0.3);
      eye(hx - 0.7, hy - 0.4, 0.55); eye(hx + 1.1, hy - 0.4, 0.55);
      for (const ex of [hx - 0.7, hx + 1.1]) { g.beginPath(); g.arc(ex, hy - 0.4, 0.95, 0, 2 * PI); g.strokeStyle = INK; g.lineWidth = 0.28; g.stroke(); g.fillStyle = "rgba(200,230,255,0.25)"; g.fill(); }
      line(g, INK, hx - 0.05, hy - 0.5, hx + 0.45, hy - 0.5, 0.25); line(g, INK, hx - 1.65, hy - 0.5, hx - 2.6, hy - 1, 0.25);
      smile(hx + 0.4, hy + 1.1, 0.6); circle(g, rgba(CHEEK, 0.6), 0.55, hx + 1.9, hy + 0.7);
    } else if (kind === "leaozinho") {
      // leãozinho (fiscal): juba fofa de tufos (estufa na festa), focinho branquinho, rabo com pompom
      const tw = sin(t * 2) * 1.6, fluff = party ? abs(sin(t * 8)) * 0.4 : 0;
      const tl = new Path2D(); tl.moveTo(4, -1.8); tl.quadraticCurveTo(9.4, -2, 8.6 + tw * 0.3, -8 + tw * 0.6); stroke(g, tl, "#E0A040", 1.3);
      circle(g, "#B8541E", 1.4, 8.6 + tw * 0.3, -8.4 + tw * 0.6);
      const body = ell(0, -4.6, 5, 4.7, vGrad(g, ["#FFD27A", "#E9A93E"], -9.3, 0)); stroke(g, body, OUT, 0.35);
      ell(0, -3.7, 2.6, 2.9, "#FFF0C9");
      for (const s of [-1, 1]) { const f = ell(s * 2, -0.6, 1.5, 0.95, "#FFE6A8"); stroke(g, f, OUT, 0.25); }
      ell(0, -12, 6.6 + fluff, 6.2 + fluff, "#B8541E");
      for (let i = 0; i < 12; i++) { const a = i * 2 * PI / 12 + sin(t * 1.6) * 0.04; circle(g, i % 2 ? "#E8812E" : "#D26A22", 2.2 + fluff * 0.5, cos(a) * (4.5 + fluff), -12 + sin(a) * (4.3 + fluff)); }
      for (const s of [-1, 1]) { circle(g, "#F2B850", 1.3, s * 3.2, -15.4); circle(g, "#FFB3A0", 0.6, s * 3.2, -15.3); }
      const face = ell(0, -12, 3.8, 3.6, vGrad(g, ["#FFDA88", "#F0B24A"], -15.6, -8.4)); stroke(g, face, OUT, 0.3);
      for (const s of [-1, 1]) ell(s * 0.9, -10.3, 1.2, 0.95, "#FFF6E0");
      const nose = new Path2D(); nose.moveTo(-0.75, -11.4); nose.lineTo(0.75, -11.4); nose.lineTo(0, -10.6); nose.closePath(); g.fillStyle = "#7A3E1A"; g.fill(nose);
      eye(-1.6, -12.7, 0.75); eye(1.6, -12.7, 0.75);
      const m = new Path2D(); m.moveTo(-0.9, -9.6); m.quadraticCurveTo(-0.45, -9, 0, -9.7); m.quadraticCurveTo(0.45, -9, 0.9, -9.6); stroke(g, m, INK, 0.35);
      circle(g, rgba(CHEEK, 0.6), 0.75, -2.6, -10.9); circle(g, rgba(CHEEK, 0.6), 0.75, 2.6, -10.9);
    } else if (kind === "castor") {
      // castor contador (controle): rabo de remo quadriculado que bate, dentões, óculos redondos e o lápis
      // atrás da orelha
      const slap = party ? abs(sin(t * 8)) * 0.35 : sin(t * 1.6) * 0.06;
      g.save(); g.translate(-3.6, -1.4); g.rotate(-0.25 - slap);
      const tl = new Path2D(); tl.ellipse(-3.6, 0, 3.8, 1.7, 0, 0, 2 * PI); g.fillStyle = vGrad(g, ["#6E5040", "#4A3328"], -1.7, 1.7); g.fill(tl);
      g.save(); g.clip(tl); for (let i = -4; i <= 4; i++) { line(g, "rgba(25,15,10,0.45)", -7.4 + i * 1.2, -2, -5.4 + i * 1.2, 2, 0.22); line(g, "rgba(25,15,10,0.45)", -5.4 + i * 1.2, -2, -7.4 + i * 1.2, 2, 0.22); } g.restore();
      stroke(g, tl, OUT, 0.3); g.restore();
      const body = ell(0, -4.8, 5, 4.8, vGrad(g, ["#B07A4E", "#8A5A36"], -9.6, 0)); stroke(g, body, OUT, 0.35);
      ell(0, -4, 2.8, 3.2, "#D9B48C");
      for (const s of [-1, 1]) ell(s * 2.2, -0.6, 1.6, 0.9, "#5E4030");
      // o lápis atrás da orelha direita
      g.save(); g.translate(3.4, -15.6); g.rotate(-0.75);
      g.fillStyle = "#FF8FA3"; g.fillRect(-4.4, -0.45, 0.8, 0.9); g.fillStyle = "#C9CCD6"; g.fillRect(-3.6, -0.45, 0.6, 0.9);
      g.fillStyle = vGrad(g, ["#FFE27A", "#F5B83D"], -0.45, 0.45); g.fillRect(-3, -0.45, 5, 0.9);
      const tip = new Path2D(); tip.moveTo(2, -0.45); tip.lineTo(3.4, 0); tip.lineTo(2, 0.45); tip.closePath(); g.fillStyle = "#F3D3A5"; g.fill(tip); circle(g, INK, 0.2, 3.2, 0);
      g.restore();
      for (const s of [-1, 1]) { circle(g, "#8A5A36", 1.25, s * 3.4, -15); circle(g, "#C99872", 0.55, s * 3.4, -15); }
      const head = ell(0, -11.8, 4.4, 4, vGrad(g, ["#BE8A5C", "#94643E"], -15.8, -7.8)); stroke(g, head, OUT, 0.35);
      ell(0, -10, 2.3, 1.6, "#E1C29C"); ell(0, -10.9, 0.9, 0.6, "#3A2418");
      for (const s of [-1, 1]) { const th = new Path2D(); th.roundRect(s > 0 ? 0.05 : -0.95, -9.3, 0.9, 1.5, 0.25); g.fillStyle = "#FFFFFF"; g.fill(th); stroke(g, th, "rgba(38,33,92,0.45)", 0.2); }
      eye(-1.7, -12.6, 0.7); eye(1.7, -12.6, 0.7);
      for (const s of [-1, 1]) { g.beginPath(); g.arc(s * 1.7, -12.6, 1.25, 0, 2 * PI); g.strokeStyle = INK; g.lineWidth = 0.32; g.stroke(); line(g, INK, s * 2.95, -12.8, s * 4.2, -13.3, 0.28); }
      line(g, INK, -0.45, -12.8, 0.45, -12.8, 0.3);
      circle(g, rgba(CHEEK, 0.6), 0.7, -3, -10.6); circle(g, rgba(CHEEK, 0.6), 0.7, 3, -10.6);
    } else if (kind === "porquinho") {
      // porquinho-cofre de louça (bancos): fenda de moeda nas costas, focinho de disco, rabinho enrolado,
      // uma florzinha pintada no lado e o brilho de cerâmica; balança de leve
      g.rotate(sin(t * 1.7) * 0.04);
      for (const fx of [-3.6, -1.2, 2, 4.2]) { const l = new Path2D(); l.roundRect(fx - 0.9, -2.4, 1.8, 2.4, 0.6); g.fillStyle = "#E77E9C"; g.fill(l); }
      const tl = new Path2D(); tl.moveTo(-6.2, -6.4); tl.bezierCurveTo(-8.6, -7.8, -8.8, -4.6, -7.4, -5.2); tl.bezierCurveTo(-6.6, -5.6, -7.2, -7, -8, -6.6); stroke(g, tl, "#E77E9C", 0.6);
      const back = new Path2D(); back.moveTo(-0.6, -9.9); back.lineTo(0.4, -12.4); back.lineTo(1.8, -10.3); back.closePath(); g.fillStyle = "#E77E9C"; g.fill(back);
      const body = ell(0, -6, 6.6, 4.9, vGrad(g, ["#FFC2D1", "#F79AB4", "#E77E9C"], -10.9, -1.1)); stroke(g, body, "rgba(170,60,90,0.45)", 0.35);
      const ear = new Path2D(); ear.moveTo(1.6, -9.8); ear.lineTo(3, -12.8); ear.lineTo(4.2, -9.9); ear.closePath(); g.fillStyle = "#F28AA8"; g.fill(ear); stroke(g, ear, "rgba(170,60,90,0.4)", 0.25);
      const slot = new Path2D(); slot.roundRect(-3.4, -10.9, 3.4, 0.9, 0.45); g.fillStyle = "#5A2338"; g.fill(slot); line(g, "rgba(255,255,255,0.6)", -3, -9.8, -0.4, -9.8, 0.25);
      const sn = ell(6.4, -5.8, 1.5, 1.9, vGrad(g, ["#FFB3C6", "#F08AA6"], -7.7, -3.9)); stroke(g, sn, "rgba(170,60,90,0.45)", 0.3);
      ell(6.5, -6.5, 0.3, 0.45, "#B84A6A"); ell(6.5, -5.1, 0.3, 0.45, "#B84A6A");
      eye(3.6, -7.4, 0.75); smile(4.2, -4.8, 0.7); circle(g, "rgba(255,120,150,0.55)", 0.9, 3.4, -5.6);
      for (let i = 0; i < 5; i++) { const a = i * 2 * PI / 5; circle(g, "rgba(255,255,255,0.85)", 0.55, -2.4 + cos(a) * 0.75, -4.8 + sin(a) * 0.75); } circle(g, GOLD, 0.45, -2.4, -4.8);
      ell(-2.4, -8.4, 2, 0.75, "rgba(255,255,255,0.55)", -0.2); circle(g, "rgba(255,255,255,0.8)", 0.35, 0.2, -9.3);
    } else if (kind === "pombo") {
      // pombo-correio (legislativo): cinza com pescoço furta-cor, cabeça que vai e vem (bem de pombo), asa de
      // listras e a cartinha com selo de coração no bico
      const bob = party ? sin(t * 10) * 0.8 : ((t * 1.2) % 1 < 0.5 ? 0.8 : 0), flap = party ? sin(t * 14) * 0.5 : 0;
      for (const fx of [-1, 1.6]) { line(g, "#F08A5D", fx, -2.2, fx, -0.3, 0.5); line(g, "#F08A5D", fx - 0.7, 0, fx + 0.9, 0, 0.45); }
      const tail = new Path2D(); tail.moveTo(-4, -5.2); tail.lineTo(-8.4, -3.8); tail.lineTo(-7.8, -2); tail.lineTo(-3.6, -3.2); tail.closePath(); g.fillStyle = "#6E7490"; g.fill(tail); stroke(g, tail, OUT, 0.25);
      const body = ell(-0.4, -5, 5, 3.6, vGrad(g, ["#C9CCDC", "#8E93AE"], -8.6, -1.4)); stroke(g, body, OUT, 0.35);
      g.save(); g.translate(-1.6, -6.4); g.rotate(-0.15 - flap);
      const wg = ell(-1, 0.6, 3.6, 2, vGrad(g, ["#B3B7CC", "#7A809E"], -1.4, 2.6), 0.15); stroke(g, wg, OUT, 0.25);
      line(g, "#4E536E", -2.6, 0.4, -0.6, 1.4, 0.4); line(g, "#4E536E", -3.6, 1.2, -1.6, 2.2, 0.4);
      g.restore();
      ell(2.8 + bob * 0.5, -8.4, 2.2, 2.4, hGrad(g, ["#5FB88C", "#8A6FC8"], 0.6, 5));
      const hd = ell(3.6 + bob, -11, 2.6, 2.5, vGrad(g, ["#C2C6D8", "#8E93AE"], -13.5, -8.5)); stroke(g, hd, OUT, 0.3);
      const bk = new Path2D(); bk.moveTo(5.8 + bob, -11.5); bk.lineTo(7.7 + bob, -10.9); bk.lineTo(5.8 + bob, -10.3); bk.closePath(); g.fillStyle = "#E8A07A"; g.fill(bk); circle(g, "#F4F2FA", 0.4, 5.9 + bob, -11.5);
      // a cartinha
      g.save(); g.translate(7.4 + bob, -10.8); g.rotate(0.15 + sin(t * 2) * 0.05);
      const env = new Path2D(); env.roundRect(-0.4, -1.4, 4.4, 2.9, 0.35); g.fillStyle = vGrad(g, ["#FFFFFF", "#ECEAF6"], -1.4, 1.5); g.fill(env); stroke(g, env, "rgba(38,33,92,0.6)", 0.28);
      const v = new Path2D(); v.moveTo(-0.4, -1.4); v.lineTo(1.8, 0.2); v.lineTo(4, -1.4); stroke(g, v, "rgba(38,33,92,0.45)", 0.25);
      g.fillStyle = "#FF5C6E"; g.fill(heart(1.8, 0.3, 0.6));
      g.restore();
      circle(g, "#FFB07A", 1.05, 3.9 + bob, -11.6); eye(3.9 + bob, -11.6, 0.7); circle(g, rgba(CHEEK, 0.6), 0.6, 4.6 + bob, -10.1);
    } else if (kind === "minhoca") {
      // minhoca leitora (educação): sai do buraquinho de uma maçã vermelha, de gomos rosados que balançam,
      // com óculos redondos de quem leu muito
      const sw = sin(t * 1.8) * (party ? 1.6 : 0.8);
      const ap = new Path2D(); ap.moveTo(0, -8.8); ap.bezierCurveTo(3, -11.2, 6.4, -9.4, 6, -5.2); ap.bezierCurveTo(5.8, -1.4, 3, 0.4, 0, -0.6); ap.bezierCurveTo(-3, 0.4, -5.8, -1.4, -6, -5.2); ap.bezierCurveTo(-6.4, -9.4, -3, -11.2, 0, -8.8); ap.closePath();
      g.fillStyle = vGrad(g, ["#FF6B6B", "#E0384A", "#B82A3A"], -10.4, 0); g.fill(ap); stroke(g, ap, "rgba(110,20,35,0.5)", 0.35);
      line(g, "#7A4A2A", 0, -9, 0.4, -11.4, 0.6);
      const lf = ell(1.9, -11.3, 1.6, 0.7, vGrad(g, ["#5FD3A0", "#2E9E72"], -12, -10.6), -0.5); line(g, "rgba(20,90,60,0.6)", 0.6, -10.7, 3.1, -11.9, 0.2);
      ell(-3.4, -6.4, 0.9, 1.8, "rgba(255,255,255,0.5)", 0.3);
      ell(2.6, -6.4, 1.4, 1, "#5A1A22");
      const P = (u) => [2.6 + 2.4 * u + sw * u * u, -6.8 - 6.2 * u];
      for (let i = 0; i <= 5; i++) { const [px, py] = P(i / 5 * 0.9); circle(g, "rgba(170,60,90,0.45)", 1.45, px, py); circle(g, i % 2 ? "#FF9EB5" : "#F47E9E", 1.2, px, py); }
      const [hx, hy] = P(1.15);
      const hd = ell(hx, hy, 2.1, 1.9, vGrad(g, ["#FFB8CA", "#F48AA6"], hy - 1.9, hy + 1.9)); stroke(g, hd, "rgba(170,60,90,0.45)", 0.3);
      eye(hx - 0.75, hy - 0.2, 0.48); eye(hx + 0.75, hy - 0.2, 0.48);
      for (const s of [-1, 1]) { g.beginPath(); g.arc(hx + s * 0.75, hy - 0.2, 0.8, 0, 2 * PI); g.strokeStyle = INK; g.lineWidth = 0.26; g.stroke(); }
      line(g, INK, hx - 0.05, hy - 0.3, hx + 0.05, hy - 0.3, 0.26);
      smile(hx, hy + 0.9, 0.5); circle(g, rgba(CHEEK, 0.7), 0.45, hx - 1.5, hy + 0.6); circle(g, rgba(CHEEK, 0.7), 0.45, hx + 1.5, hy + 0.6);
    } else if (kind === "pixel") {
      // gatinho de pixel (TI): o gatinho em blocos 8-bit, com contorno de bloco, realce em cima, olhos de um
      // pixel que piscam e o rabo que troca de quadro (como animação de jogo antigo)
      const PX = [".d.....d..", ".dd...dd..", ".ooooooo..", "ooeoooeoo.", "ocoopooco.", ".ooooooo..", "..ooooo...", ".oooowoo..", ".ooowwwoo.", ".ooowwwoo.", ".ww...ww.."];
      const COL = { o: "#F7A44A", d: "#D9772B", w: "#FFF1DD", p: "#FF8FA3", c: "#FFB3C7", e: INK };
      const c = 1.35, x0 = -6.6, y0 = -14.85, fr = Math.floor(t * (party ? 6 : 1.5)) % 2;
      const cells = [];
      PX.forEach((row, r) => [...row].forEach((ch, q) => { if (ch !== ".") cells.push([q, r, ch]); }));
      cells.push([9, 8, "d"], [9, 7, "d"], fr ? [10, 6, "d"] : [9, 6, "d"]);
      for (const [q, r] of cells) { g.fillStyle = "rgba(38,33,92,0.55)"; g.fillRect(x0 + q * c - 0.35, y0 + r * c - 0.35, c + 0.7, c + 0.7); }
      for (const [q, r, ch] of cells) {
        const x = x0 + q * c, y = y0 + r * c;
        if (ch === "e") {
          if (blink) { g.fillStyle = COL.o; g.fillRect(x, y, c + 0.02, c + 0.02); g.fillStyle = INK; g.fillRect(x, y + c * 0.55, c, c * 0.35); }
          else { g.fillStyle = INK; g.fillRect(x, y, c + 0.02, c + 0.02); if (!sad) { g.fillStyle = "#FFFFFF"; g.fillRect(x + 0.15, y + 0.15, 0.45, 0.45); } else if (!tearAt) tearAt = [x + c / 2, y + c]; }
          continue;
        }
        g.fillStyle = COL[ch]; g.fillRect(x, y, c + 0.02, c + 0.02);
        if (ch === "o" && (r === 0 || PX[r - 1][q] === ".")) { g.fillStyle = "rgba(255,255,255,0.35)"; g.fillRect(x, y, c, 0.35); }
      }
    } else if (kind === "peixinho") {
      // peixinho dourado no aquário redondo (administrativo): água com marolinha, pedrinhas e alga; o peixe
      // nada de um lado para o outro (vira na ponta) e solta bolhas; vidro com brilho e borda de cima
      const R = 6.6, cy = -7, sp = party ? 1.8 : 0.9;
      const bowl = new Path2D(); bowl.arc(0, cy, R, 0, 2 * PI);
      g.fillStyle = "rgba(220,240,255,0.35)"; g.fill(bowl);
      g.save(); g.clip(bowl);
      const wy = -9.4, wv = new Path2D(); wv.moveTo(-8, wy); for (let x = -8; x <= 8; x += 0.5) wv.lineTo(x, wy + sin(x * 1.2 + t * 3) * 0.3); wv.lineTo(8, 2); wv.lineTo(-8, 2); wv.closePath();
      g.fillStyle = vGrad(g, ["rgba(130,205,255,0.8)", "rgba(60,140,230,0.9)"], wy, -0.4); g.fill(wv);
      line(g, "rgba(255,255,255,0.6)", -5, wy + 0.2, 5, wy + 0.2, 0.3);
      for (const [px, pr, pc] of [[-3.6, 1.1, "#F5C842"], [-1.6, 0.9, "#B995F5"], [0.4, 1.2, "#FF8FA3"], [2.6, 1, "#F5C842"], [4.2, 0.9, "#7EE0B8"]]) circle(g, pc, pr, px, -0.8);
      const sw = new Path2D(); sw.moveTo(-3.4, -1.2); sw.quadraticCurveTo(-4.4 + sin(t * 2) * 0.6, -3.6, -3 + sin(t * 2.2) * 0.8, -6); stroke(g, sw, "#2E9E72", 0.8);
      const fx = sin(t * sp) * 2.6, dir = cos(t * sp) >= 0 ? 1 : -1, fy = -5.4 + sin(t * 2.3) * 0.5, wig = sin(t * 8) * 0.4;
      g.save(); g.translate(fx, fy); g.scale(dir, 1);
      const ft = new Path2D(); ft.moveTo(-1.5, 0); ft.lineTo(-3.1, -1.2 + wig); ft.lineTo(-3.1, 1.2 + wig); ft.closePath(); g.fillStyle = "#FF8A2B"; g.fill(ft);
      const fb = ell(0, 0, 2, 1.4, hGrad(g, ["#FF7A2B", "#FFB347"], -2, 2)); stroke(g, fb, "rgba(160,60,10,0.45)", 0.22);
      ell(-0.2, -1.3, 0.9, 0.4, "#FF8A2B", -0.2);
      if (sad) circle(g, INK, 0.45, 0.9, -0.3); else eye(0.9, -0.3, 0.5);
      g.restore();
      for (let i = 0; i < (party ? 5 : 3); i++) { const ph = (t * 0.6 + i / (party ? 5 : 3)) % 1; g.beginPath(); g.arc(fx + dir * 2.2 + sin(t * 3 + i) * 0.4, fy - 0.6 - ph * (fy - wy), 0.35 + ph * 0.25, 0, 2 * PI); g.strokeStyle = `rgba(255,255,255,${0.9 * (1 - ph * 0.6)})`; g.lineWidth = 0.22; g.stroke(); }
      g.restore();
      stroke(g, bowl, "rgba(120,170,220,0.9)", 0.5);
      const rim = ell(0, cy - R + 0.5, 3.6, 0.9, "rgba(255,255,255,0.35)"); stroke(g, rim, "#8FB8E6", 0.7);
      g.beginPath(); g.arc(0, cy, R - 1.2, PI * 1.08, PI * 1.38); g.strokeStyle = "rgba(255,255,255,0.85)"; g.lineWidth = 0.6; g.lineCap = "round"; g.stroke();
      circle(g, "rgba(255,255,255,0.8)", 0.35, -4.4, -2.6);
    } else if (kind === "cachorrinho") {
      // cachorrinho branquinho: orelhas caídas marrons, mancha no olho, linguinha de fora, coleira vermelha e
      // o rabo que não para (abana mais rápido na festa)
      const wag = sin(t * (party ? 14 : 4)) * (party ? 0.6 : 0.35), ear = sin(t * 2.4) * 0.1;
      g.save(); g.translate(-4.2, -3.4); g.rotate(-0.6 + wag); const tl = new Path2D(); tl.moveTo(0, 0); tl.quadraticCurveTo(-1.8, -2, -1.1, -4.4); stroke(g, tl, "rgba(150,120,90,0.6)", 1.9); stroke(g, tl, "#FFFFFF", 1.3); circle(g, "#C98A50", 0.75, -1.1, -4.4); g.restore();
      const body = ell(0, -4.6, 5, 4.7, vGrad(g, ["#FFFFFF", "#EDE3D6"], -9.3, 0));
      g.save(); g.clip(body); ell(-3, -5.8, 1.8, 1.4, "#D9A066"); g.restore(); stroke(g, body, OUT, 0.35);
      for (const s of [-1, 1]) { const f = ell(s * 2, -0.6, 1.5, 0.9, "#FFFFFF"); stroke(g, f, OUT, 0.25); }
      line(g, "#FF5C6E", -3.2, -8.6, 3.2, -8.6, 1); circle(g, GOLD, 0.6, 0.2, -7.8);
      const head = ell(0, -12, 4.4, 4, vGrad(g, ["#FFFFFF", "#EFE6DA"], -16, -8)); stroke(g, head, OUT, 0.35);
      g.save(); g.clip(head); ell(1.9, -12.8, 1.6, 1.5, "#E0AD78"); g.restore();
      for (const s of [-1, 1]) { const e = ell(s * 4.1, -11.6, 1.5, 3.1, vGrad(g, ["#C98A50", "#A8703F"], -14.7, -8.5), -s * (0.35 + ear)); stroke(g, e, OUT, 0.3); }
      ell(0, -10.2, 2, 1.4, "#FFF8F0"); ell(0, -11, 0.8, 0.6, INK); circle(g, "rgba(255,255,255,0.7)", 0.2, -0.25, -11.2);
      eye(-1.7, -12.7, 0.8); eye(1.7, -12.7, 0.8);
      const m = new Path2D(); m.moveTo(-1, -9.8); m.quadraticCurveTo(-0.5, -9.2, 0, -9.9); m.quadraticCurveTo(0.5, -9.2, 1, -9.8); stroke(g, m, INK, 0.38);
      if (!sad) { const tg = ell(0.35, -8.9, 0.65, 0.85, "#FF8FA3"); line(g, "rgba(200,60,90,0.5)", 0.35, -9.3, 0.35, -8.4, 0.18); }
      circle(g, rgba(CHEEK, 0.6), 0.75, -2.8, -10.6); circle(g, rgba(CHEEK, 0.6), 0.75, 2.8, -10.6);
    } else if (kind === "pinguim") {
      // pinguinzinho: corpo de ovo azul-escuro, barriga branca, bico e pés laranja, cachecol vermelho de
      // listras e nadadeiras que batem; bamboleia de um lado para o outro
      const flap = party ? sin(t * 14) * 0.6 : sin(t * 2.4) * 0.12;
      g.rotate(sin(t * 2.4) * 0.08);
      for (const s of [-1, 1]) ell(s * 1.8, -0.4, 1.7, 0.8, "#FF9F3A");
      for (const s of [-1, 1]) { g.save(); g.translate(s * 4.5, -8); g.rotate(s * (0.35 + flap)); ell(0, 2.6, 1.2, 3.2, "#2B2E4A"); g.restore(); }
      const body = ell(0, -8, 5.2, 7.8, vGrad(g, ["#4A4F78", "#2B2E4A", "#1E2036"], -15.8, -0.2)); stroke(g, body, OUT, 0.35);
      ell(0, -5.6, 3.7, 5.2, vGrad(g, ["#FFFFFF", "#E8ECF6"], -10.8, -0.4)); ell(0, -11, 3.4, 2.8, "#FFFFFF");
      eye(-1.4, -11.8, 0.75); eye(1.4, -11.8, 0.75);
      const bk = new Path2D(); bk.moveTo(-0.9, -10.5); bk.lineTo(0.9, -10.5); bk.lineTo(0, -9.3); bk.closePath(); g.fillStyle = "#FF9F3A"; g.fill(bk);
      circle(g, rgba(CHEEK, 0.7), 0.7, -2.4, -10.2); circle(g, rgba(CHEEK, 0.7), 0.7, 2.4, -10.2);
      const sc = new Path2D(); sc.roundRect(-4.7, -8.9, 9.4, 1.7, 0.85); g.fillStyle = vGrad(g, ["#FF7A88", "#E0475E"], -8.9, -7.2); g.fill(sc);
      g.save(); g.translate(2.2, -7.8); g.rotate(-0.15 + sin(t * 2) * 0.08); const en = new Path2D(); en.roundRect(-0.8, 0, 1.7, 3.4, 0.5); g.fillStyle = "#E0475E"; g.fill(en); line(g, "rgba(255,255,255,0.8)", -0.8, 1.6, 0.9, 1.6, 0.35); g.restore();
      for (const x of [-2.6, 0, 2.6]) line(g, "rgba(255,255,255,0.75)", x, -8.9, x, -7.2, 0.35);
      ell(-2, -13.8, 1.4, 0.6, "rgba(255,255,255,0.35)", -0.3);
    } else if (kind === "abelha") {
      // abelhinha voando: paira no ar balançando, asas transparentes batendo rápido, listras, ferrãozinho,
      // anteninhas e um rastro pontilhado de voo; com sono ela pousa no chão e fecha as asas
      const fl = sleepy ? 0 : 1, X = sin(t * 1.1) * 1.4 * fl, Y = sleepy ? -3.4 : -10 + sin(t * 3.2) * 1.2;
      if (fl) for (let k = 1; k <= 6; k++) circle(g, `rgba(38,33,92,${0.28 * (1 - k / 7)})`, 0.35, X - 4.6 - k * 1.5, Y + 0.6 + sin(t * 3.2 - k * 0.9) * 1.6);
      const stg = new Path2D(); stg.moveTo(X - 4.4, Y - 0.4); stg.lineTo(X - 6, Y + 0.2); stg.lineTo(X - 4.4, Y + 0.8); stg.closePath(); g.fillStyle = INK; g.fill(stg);
      const body = ell(X, Y, 4.6, 3.3, vGrad(g, ["#FFE27A", "#FFC93C", "#F0A800"], Y - 3.3, Y + 3.3));
      g.save(); g.clip(body); for (const sx of [-2.8, -0.6, 1.6]) { g.fillStyle = "rgba(38,33,92,0.92)"; g.fillRect(X + sx, Y - 4, 1.1, 8); } g.restore();
      stroke(g, body, OUT, 0.35); ell(X - 1, Y - 2, 1.6, 0.5, "rgba(255,255,255,0.45)", -0.1);
      const wsc = fl ? 0.45 + 0.55 * abs(sin(t * 28)) : 0.5;
      for (const [wx, a] of [[-1.2, -0.45], [0.6, 0.25]]) { g.save(); g.translate(X + wx, Y - 2.6); g.rotate(a); g.scale(1, wsc); const w = ell(0, -2.4, 1.8, 2.7, "rgba(225,242,255,0.7)"); stroke(g, w, "rgba(110,150,210,0.75)", 0.3); line(g, "rgba(110,150,210,0.5)", 0, 0, 0, -3.8, 0.2); g.restore(); }
      for (const [ax, tx] of [[3.6, 3.4], [4.8, 5.6 + sin(t * 3) * 0.3]]) { const an = new Path2D(); an.moveTo(X + ax, Y - 2.6); an.quadraticCurveTo(X + ax, Y - 4.6, X + tx, Y - 5.4); stroke(g, an, INK, 0.3); circle(g, INK, 0.4, X + tx, Y - 5.4); }
      const hd = ell(X + 4.2, Y - 0.6, 2.5, 2.4, vGrad(g, ["#FFE89A", "#FFCC4A"], Y - 3, Y + 1.8)); stroke(g, hd, OUT, 0.3);
      eye(X + 3.5, Y - 1, 0.55); eye(X + 5.2, Y - 1, 0.55); smile(X + 4.4, Y + 0.4, 0.55);
      circle(g, rgba(CHEEK, 0.7), 0.5, X + 2.8, Y + 0.3); circle(g, rgba(CHEEK, 0.7), 0.5, X + 6, Y + 0.2);
    } else if (kind === "polvo") {
      // polvinho: cabeça de domo lilás com pintinhas e olhos grandes, seis tentáculos que ondulam com
      // ventosas; na festa os tentáculos das pontas acenam
      for (let i = 0; i < 6; i++) {
        const bx = -4 + i * 1.6, sd = i < 3 ? -1 : 1, wave = (i === 0 || i === 5) && party ? -abs(sin(t * 8 + i)) * 3.4 : 0;
        const ex = bx + cos(t * 2.4 + i) * 0.9 + sd * 1, ey = -0.5 + wave;
        const tp = new Path2D(); tp.moveTo(bx, -4.6); tp.quadraticCurveTo(bx + sin(t * 2.4 + i) * 1.4 + sd * 0.6, -2.2 + wave * 0.5, ex, ey);
        stroke(g, tp, "#8E57D6", 2); stroke(g, tp, "#B983F0", 1.3);
        circle(g, "#B983F0", 0.75, ex + sd * 0.3, ey - 0.2); circle(g, "#F3D6FF", 0.25, (bx + ex) / 2, (-4.6 + ey) / 2 + 0.6);
      }
      ell(0, -4.6, 5, 1.6, "#A46CE2");
      const head = new Path2D(); head.ellipse(0, -9, 5.2, 5.6, 0, 0, 2 * PI);
      const hg = g.createRadialGradient(-1.6, -11.4, 0.3, 0, -9, 6.4); hg.addColorStop(0, "#E9C4FF"); hg.addColorStop(0.6, "#B983F0"); hg.addColorStop(1, "#8E57D6"); g.fillStyle = hg; g.fill(head); stroke(g, head, "rgba(80,40,140,0.5)", 0.35);
      for (const [x, y, r] of [[-2.8, -12.2, 0.8], [2.9, -12.6, 0.55], [-0.5, -14, 0.5], [3.6, -10.4, 0.4]]) circle(g, "#D9A8FF", r, x, y);
      for (const s of [-1, 1]) { circle(g, "#FFFFFF", 1.45, s * 1.8, -8.6); eye(s * 1.8, -8.6, 0.95); }
      smile(0, -6.4, 0.8); circle(g, rgba(CHEEK, 0.75), 0.75, -3.4, -7); circle(g, rgba(CHEEK, 0.75), 0.75, 3.4, -7);
      ell(-2.2, -12.8, 1.4, 0.7, "rgba(255,255,255,0.45)", -0.4);
    }
    // ---- reage ao Folha (à direita dele)
    const ci = (t % 8.5) / 8.5, check = !party && !sleepy && !sad && ci > 0.8 ? sin(PI * (ci - 0.8) / 0.2) : 0;
    const headY = { planta: -12, patinho: -14, gatinho: -17, coruja: -17, robo: -22, coelho: -18, dragao: -17,
      ursinho: -18, pastor: -20, k9: -18, tartaruga: -12, leaozinho: -19, castor: -17, porquinho: -13, pombo: -15, minhoca: -17,
      pixel: -16, peixinho: -15, cachorrinho: -17, pinguim: -16, abelha: -16, polvo: -15 }[kind] || -15;
    const bub = sad ? 0.85 + 0.15 * sin(t * 3) : check;
    if (bub > 0.02) {
      // balãozinho de coração: o carinho de tempos em tempos e o consolo quando ele está triste
      const bx = 6.5, by = headY - 5 - (sad ? sin(t * 2) * 0.6 : (1 - bub) * 2);
      g.save(); g.globalAlpha = bub;
      const b = new Path2D(); b.ellipse(bx, by, 3.6, 3, 0, 0, 2 * PI); g.fillStyle = "#FFFFFF"; g.fill(b); stroke(g, b, "rgba(29,22,112,0.25)", 0.3);
      circle(g, "#FFFFFF", 0.8, bx - 2.6, by + 3.2); circle(g, "#FFFFFF", 0.45, bx - 3.8, by + 4.4);
      g.fillStyle = "#FF6B86"; g.fill(heart(bx, by + 0.3, 1.5 * (1 + 0.12 * sin(t * 6))));
      g.restore();
    }
    if (party) {
      if (kind === "planta") for (let i = 0; i < 5; i++) {
        const ph = (t * 0.7 + i / 5) % 1, px = 2 + ph * 18, py = -19 - sin(PI * ph) * 7 + ph * 6;
        g.save(); g.globalAlpha = 1 - ph; g.translate(px, py); g.rotate(t * 3 + i); g.beginPath(); g.ellipse(0, 0, 1, 0.55, 0, 0, 2 * PI); g.fillStyle = "#FF8FA3"; g.fill(); g.restore();
      }
      else if (kind === "patinho") { const q = (t * 3) % 1; for (const a of [-0.5, 0, 0.5]) line(g, `rgba(255,138,43,${1 - q})`, 8.6 + cos(a) * (1 + q * 2), -9.8 + sin(a) * (1 + q * 2), 8.6 + cos(a) * (2 + q * 2.5), -9.8 + sin(a) * (2 + q * 2.5), 0.4); }
      else if (kind === "gatinho") {
        const w = sin(t * 8) * 0.6; g.save(); g.translate(4.6, -6.6 + w);
        circle(g, "#F7B36E", 1.7, 0, 0); circle(g, "#FFB3C7", 0.7, 0, 0.3); for (const d of [-0.9, 0, 0.9]) circle(g, "#FFB3C7", 0.32, d, -1);
        g.restore();
        const hp = (t * 0.8) % 1; g.save(); g.globalAlpha = sin(PI * hp); g.fillStyle = "#FF6B86"; g.fill(heart(9 + sin(t * 2), -20 - hp * 4, 1.2)); g.restore();
      }
      else if (kind === "coruja") for (let i = 0; i < 2; i++) {
        const ph = (t * 0.6 + i / 2) % 1, nx = 5 + ph * 10, ny = -14 - ph * 8;
        g.save(); g.globalAlpha = sin(PI * ph); circle(g, INK, 0.8, nx, ny); line(g, INK, nx + 0.7, ny, nx + 0.7, ny - 3, 0.35); line(g, INK, nx + 0.7, ny - 3, nx + 2, ny - 2.4, 0.35); g.restore();
      }
      else if (kind === "robo") for (let i = 0; i < 4; i++) { const ph = (t * 1.4 + i / 4) % 1; circle(g, `rgba(126,224,184,${1 - ph})`, 0.45, ph * 12, -20.6 - sin(PI * ph) * 5); }
      else if (kind === "coelho") for (let i = 0; i < 3; i++) { const ph = (t * 0.9 + i / 3) % 1; star4(g, -4 + i * 5, -24 - ph * 4, 1.3 * sin(PI * ph), `rgba(255,214,110,${sin(PI * ph)})`); }
      else if (kind === "dragao") { const ph = (t * 0.9) % 1; g.save(); g.globalAlpha = (1 - ph) * 0.9; g.beginPath(); g.ellipse(13 + ph * 6, -11.5 - ph * 3, 1.2 + ph * 2, 0.8 + ph * 1.4, 0, 0, 2 * PI); g.strokeStyle = "rgba(170,165,190,0.9)"; g.lineWidth = 0.5; g.stroke(); g.restore(); }
      // comemorações dos bichinhos novos
      else if (kind === "ursinho") for (let i = 0; i < 3; i++) {   // cruzinhas de saúde subindo
        const ph = (t * 0.7 + i / 3) % 1, a = sin(PI * ph), cx = 4 + i * 4.5, cy = -17 - ph * 7;
        for (const [w, c] of [[1.5, `rgba(255,255,255,${a})`], [0.8, `rgba(240,82,96,${a})`]]) { line(g, c, cx - 1.1, cy, cx + 1.1, cy, w); line(g, c, cx, cy - 1.1, cx, cy + 1.1, w); }
      }
      else if (kind === "pastor" || kind === "leaozinho") {   // latido / rugidinho
        const q = (t * 2.6) % 1, ox = kind === "pastor" ? 8.6 : 5.6, oy = kind === "pastor" ? -11 : -10.4, c = kind === "pastor" ? "58,42,34" : "210,106,34";
        for (const a of [-0.5, 0, 0.5]) line(g, `rgba(${c},${1 - q})`, ox + cos(a) * (1 + q * 2), oy + sin(a) * (1 + q * 2), ox + cos(a) * (2 + q * 2.5), oy + sin(a) * (2 + q * 2.5), 0.45);
      }
      else if (kind === "k9") {   // fareja: baforadinhas saindo do focinho e o brilho do distintivo
        for (let i = 0; i < 3; i++) { const ph = (t * 1.2 + i / 3) % 1; circle(g, `rgba(170,165,190,${(1 - ph) * 0.7})`, 0.4 + ph * 0.8, 7.4 + ph * 5, -11.2 - sin(PI * ph) * 1.5); }
        star4(g, 3.2, -7.6, 1.3 * abs(sin(t * 4)), "rgba(255,236,160,0.95)");
      }
      else if (kind === "tartaruga") {   // a lâmpada da ideia acende sobre a cabeça
        const a = 0.6 + 0.4 * sin(t * 6), bx = 9, by = -15.5;
        const gl = g.createRadialGradient(bx, by, 0.2, bx, by, 4); gl.addColorStop(0, `rgba(255,224,122,${0.6 * a})`); gl.addColorStop(1, "rgba(255,224,122,0)"); g.fillStyle = gl; g.beginPath(); g.arc(bx, by, 4, 0, 2 * PI); g.fill();
        circle(g, "#FFE07A", 1.6, bx, by); circle(g, "rgba(255,255,255,0.8)", 0.5, bx - 0.5, by - 0.5);
        const bs = new Path2D(); bs.roundRect(bx - 0.8, by + 1.2, 1.6, 1.1, 0.3); g.fillStyle = "#9A9EB4"; g.fill(bs);
        for (let i = 0; i < 5; i++) { const an = -PI * 0.9 + i * PI * 0.2; line(g, rgba(GOLD, a), bx + cos(an) * 2.2, by + sin(an) * 2.2, bx + cos(an) * 3.2, by + sin(an) * 3.2, 0.35); }
      }
      else if (kind === "castor") for (let i = 0; i < 3; i++) {   // contas: "+" e "=" subindo
        const ph = (t * 0.6 + i / 3) % 1, a = sin(PI * ph), x = 5 + i * 4, y = -17 - ph * 6, c = i % 2 ? `rgba(46,158,114,${a})` : `rgba(58,47,196,${a})`;
        if (i % 2) { line(g, c, x - 1, y, x + 1, y, 0.5); line(g, c, x, y - 1, x, y + 1, 0.5); } else { line(g, c, x - 1, y - 0.45, x + 1, y - 0.45, 0.5); line(g, c, x - 1, y + 0.45, x + 1, y + 0.45, 0.5); }
      }
      else if (kind === "porquinho") {   // a moeda cai girando dentro da fenda e brilha
        const ph = (t * 0.9) % 1;
        if (ph < 0.6) { const u = ph / 0.6, y = -22 + u * 10.6; g.save(); g.translate(-1.7, y); g.scale(Math.max(0.2, abs(cos(t * 9))), 1); circle(g, "#B57A0C", 1.7, 0, 0); circle(g, "#FFD877", 1.3, 0, 0); g.restore(); }
        else { const u = sin(PI * (ph - 0.6) / 0.4); star4(g, -1.7, -12, 2.2 * u, rgba(GOLD, u)); }
      }
      else if (kind === "pombo") for (let i = 0; i < 2; i++) {   // coraçõezinhos saindo da cartinha
        const ph = (t * 0.7 + i / 2) % 1; g.save(); g.globalAlpha = sin(PI * ph); g.fillStyle = "#FF6B86"; g.fill(heart(10 + ph * 4 + sin(t * 3 + i), -13 - ph * 8, 0.9)); g.restore();
      }
      else if (kind === "minhoca") {   // o livrinho aberto flutuando com brilho
        const by = -20 + sin(t * 2) * 1;
        g.save(); g.translate(9, by); g.rotate(-0.1);
        for (const s of [-1, 1]) { const pg = new Path2D(); pg.moveTo(0, 0.8); pg.quadraticCurveTo(s * 1.4, 0, s * 2.8, 0.6); pg.lineTo(s * 2.8, -1.6); pg.quadraticCurveTo(s * 1.4, -2.2, 0, -1.4); pg.closePath(); g.fillStyle = "#FFFFFF"; g.fill(pg); stroke(g, pg, "rgba(38,33,92,0.6)", 0.25); for (let k = 0; k < 2; k++) line(g, "rgba(111,143,224,0.7)", s * 0.6, -0.9 + k * 0.7, s * 2.2, -0.7 + k * 0.7, 0.18); }
        g.restore(); star4(g, 12.2, by - 2.4, 1.2 * abs(sin(t * 4)), rgba(GOLD, 0.9));
      }
      else if (kind === "pixel") for (let i = 0; i < 2; i++) {   // coraçõezinhos de pixel
        const ph = (t * 0.6 + i / 2) % 1, a = sin(PI * ph), hx = 6 + i * 5, hy = -19 - ph * 6, H = [".x.x.", "xxxxx", ".xxx.", "..x.."];
        g.fillStyle = `rgba(255,92,120,${a})`; H.forEach((row, r) => [...row].forEach((ch, q) => { if (ch === "x") g.fillRect(hx + q * 0.7, hy + r * 0.7, 0.72, 0.72); }));
      }
      else if (kind === "peixinho") for (let i = 0; i < 4; i++) {   // bolhas escapando por cima do aquário
        const ph = (t * 0.8 + i / 4) % 1; g.beginPath(); g.arc(-1.5 + i + sin(t * 3 + i) * 0.8, -13.6 - ph * 8, 0.4 + ph * 0.5, 0, 2 * PI); g.strokeStyle = `rgba(111,176,255,${1 - ph})`; g.lineWidth = 0.28; g.stroke();
      }
      else if (kind === "cachorrinho") {   // um ossinho girando no ar
        const ph = (t * 0.5) % 1; g.save(); g.globalAlpha = sin(PI * ph); g.translate(9, -18 - ph * 5); g.rotate(t * 3);
        g.fillStyle = "#FFFFFF"; g.strokeStyle = "rgba(150,120,90,0.8)"; g.lineWidth = 0.25; g.fillRect(-1.6, -0.45, 3.2, 0.9); g.strokeRect(-1.6, -0.45, 3.2, 0.9);
        for (const [x, y] of [[-1.7, -0.55], [-1.7, 0.55], [1.7, -0.55], [1.7, 0.55]]) { g.beginPath(); g.arc(x, y, 0.6, 0, 2 * PI); g.fill(); g.stroke(); }
        g.restore();
      }
      else if (kind === "pinguim") for (let i = 0; i < 3; i++) { const ph = (t * 0.8 + i / 3) % 1; star4(g, 5 + i * 4, -17 - ph * 5, 1.4 * sin(PI * ph), `rgba(150,210,255,${sin(PI * ph)})`); }
      else if (kind === "abelha") for (let i = 0; i < 3; i++) { const ph = (t * 0.7 + i / 3) % 1; g.save(); g.globalAlpha = sin(PI * ph); g.fillStyle = "#FFC93C"; g.fill(heart(7 + i * 3.5, -16 - ph * 6, 0.9)); g.restore(); }
      else if (kind === "polvo") for (let i = 0; i < 4; i++) { const ph = (t * 0.7 + i / 4) % 1; g.beginPath(); g.arc(4 + i * 2 + sin(t * 2 + i) * 0.6, -14 - ph * 8, 0.45 + ph * 0.4, 0, 2 * PI); g.strokeStyle = `rgba(111,176,255,${1 - ph})`; g.lineWidth = 0.28; g.stroke(); }
    }
    if (tearAt) {
      const ph = (t % 1.8) / 1.8, ty = tearAt[1] + 1 + ph * 4, a = ph < 0.15 ? ph / 0.15 : 1 - Math.max(0, ph - 0.7) / 0.3;
      const d = new Path2D(); d.moveTo(tearAt[0], ty - 1.2); d.quadraticCurveTo(tearAt[0] + 0.9, ty + 0.3, tearAt[0], ty + 0.7); d.quadraticCurveTo(tearAt[0] - 0.9, ty + 0.3, tearAt[0], ty - 1.2);
      g.fillStyle = `rgba(126,200,255,${0.9 * a})`; g.fill(d);
    }
    g.restore();
    if (sleepy) { g.save(); g.font = "800 4px Nunito, system-ui, sans-serif"; g.fillStyle = rgba(INK, 0.6 + 0.4 * sin(t * 2)); g.fillText("z", x + 8, base - 26 + sin(t * 1.4) * 1.5); g.restore(); }
  }

  // ------------------------------------------------------------------ armário: chapéus, óculos, pescoço, mão e aura
  // Tudo é desenhado dentro do corpo do Folha (pula, inclina, amassa e estica junto); chapéu gira com o capelo,
  // óculos seguem o olhar, o item da mão segue a mão esquerda.
  function star5(g, x, y, r, sy = 1) {
    const p = new Path2D();
    for (let i = 0; i < 10; i++) { const a = -PI / 2 + i * PI / 5, rr = i % 2 ? r * 0.45 : r; i ? p.lineTo(x + cos(a) * rr, y + sin(a) * rr * sy) : p.moveTo(x + cos(a) * rr, y + sin(a) * rr * sy); }
    p.closePath(); return p;
  }
  function heart(x, y, r) {
    const p = new Path2D(); p.moveTo(x, y + r * 0.9);
    p.bezierCurveTo(x - r * 1.5, y - r * 0.1, x - r * 0.9, y - r * 1.25, x, y - r * 0.45);
    p.bezierCurveTo(x + r * 0.9, y - r * 1.25, x + r * 1.5, y - r * 0.1, x, y + r * 0.9); p.closePath(); return p;
  }
  const GOLD3 = ["#FFF1BF", GOLD, "#C98A12"];
  function drawPencilAt(g, x0, y0, x1, y1, r) {
    // lápis sextavado: três faces de amarelo, ponteira de metal com frisos, borracha e ponta de grafite
    const L = Math.hypot(x1 - x0, y1 - y0);
    g.save(); g.translate(x0, y0); g.rotate(Math.atan2(y1 - y0, x1 - x0));
    const wood = new Path2D(); wood.moveTo(0.2, 0); wood.lineTo(3.4, -r); wood.lineTo(3.4, r); wood.closePath();
    g.fillStyle = hGrad(g, ["#E9C08A", "#F7DDB4"], 0, 3.4); g.fill(wood);
    const lead = new Path2D(); lead.moveTo(0, 0); lead.lineTo(1.3, -r * 0.42); lead.lineTo(1.3, r * 0.42); lead.closePath(); g.fillStyle = "#3A3550"; g.fill(lead);
    const bodyL = L - 7;
    g.fillStyle = "#FFD45A"; g.fillRect(3.4, -r, bodyL, r * 0.68);
    g.fillStyle = "#F5B83D"; g.fillRect(3.4, -r * 0.32, bodyL, r * 0.64);
    g.fillStyle = "#D9921A"; g.fillRect(3.4, r * 0.32, bodyL, r * 0.68);
    line(g, "rgba(255,255,255,0.55)", 3.6, -r * 0.7, 3.4 + bodyL, -r * 0.7, 0.25, false);
    const fx = 3.4 + bodyL;
    g.fillStyle = vGrad(g, ["#F2F3F8", "#B7B9C9", "#8E92A6"], -r, r); g.fillRect(fx, -r * 1.05, 2, r * 2.1);
    for (const k of [0.5, 1, 1.5]) line(g, "rgba(90,90,110,0.55)", fx + k, -r * 1.05, fx + k, r * 1.05, 0.18, false);
    const er = new Path2D(); er.moveTo(fx + 2, -r); er.lineTo(fx + 3.6, -r); er.quadraticCurveTo(fx + 4.6, -r, fx + 4.6, 0); er.quadraticCurveTo(fx + 4.6, r, fx + 3.6, r); er.lineTo(fx + 2, r); er.closePath();
    g.fillStyle = vGrad(g, ["#FFB3C7", "#F07A99"], -r, r); g.fill(er);
    g.restore();
  }
  function drawCap(g, t, tilt, vy, gold) {
    const top = gold ? "#FFE08A" : CAP_TOP, mid = gold ? "#E9AE34" : CAP, low = gold ? "#A86F0C" : "#15104A", edgeC = gold ? "#7A4F05" : "#120D3F";
    const base = new Path2D();
    base.moveTo(38, 32); base.lineTo(62, 32); base.lineTo(60.5, 42.5); base.quadraticCurveTo(50, 46.5, 39.5, 42.5); base.closePath();
    g.fillStyle = vGrad(g, [mid, low], 32, 46); g.fill(base);
    const edge = new Path2D();
    edge.moveTo(23, 29.5); edge.lineTo(50, 38); edge.lineTo(77, 29.5); edge.lineTo(77, 31.3); edge.lineTo(50, 39.8); edge.lineTo(23, 31.3); edge.closePath();
    g.fillStyle = edgeC; g.fill(edge);
    const board = new Path2D();
    board.moveTo(50, 21); board.lineTo(77, 29.5); board.lineTo(50, 38); board.lineTo(23, 29.5); board.closePath();
    const lg = g.createLinearGradient(30, 22, 70, 38); lg.addColorStop(0, top); lg.addColorStop(1, mid);
    g.fillStyle = lg; g.fill(board);
    stroke(g, board, "rgba(255,255,255,0.22)", 0.5);
    line(g, "rgba(255,255,255,0.18)", 33, 27.5, 50, 22.5, 1.2);
    // borla: balança com o corpo e reage aos pulos (atrasada)
    const tas = gold ? "#FFFFFF" : GOLD, tasD = gold ? "#D9D4EE" : "#D89422";
    const swing = sin(t * 2.6) * 4 - tilt * 0.8 + clamp(vy * 0.08, -5, 5);
    const bx = 50, by = 29.5, cx = 74, cy = 30.5;
    line(g, tas, bx, by, cx, cy, 1.3);
    const ex = cx + swing * 0.5, ey = cy + 12 - Math.max(0, -vy) * 0.03;
    const cord = new Path2D(); cord.moveTo(cx, cy); cord.quadraticCurveTo(cx + swing * 0.2, cy + 6, ex, ey);
    stroke(g, cord, tas, 1.3);
    const tassel = new Path2D();
    tassel.moveTo(ex - 1.4, ey); tassel.lineTo(ex + 1.4, ey);
    tassel.lineTo(ex + 2.4 + swing * 0.15, ey + 6.5); tassel.lineTo(ex - 2.4 + swing * 0.15, ey + 6.5); tassel.closePath();
    g.fillStyle = vGrad(g, [tas, tasD], ey, ey + 6.5); g.fill(tassel);
    circle(g, tas, 1.8, bx, by); circle(g, gold ? "#FFFFFF" : "#FFE3A0", 0.7, bx - 0.5, by - 0.5);
  }
  function drawLaurels(g, small) {
    // coroa de louros: dois ramos de folhas com nervura, que se cruzam na frente com um laço vermelho
    for (const side of [-1, 1]) {
      const P0 = [50 + side * 2, 45.6], P1 = [50 + side * 17.5, 46.5], P2 = [50 + side * 17, 30];
      const at = (u) => [(1 - u) ** 2 * P0[0] + 2 * (1 - u) * u * P1[0] + u * u * P2[0], (1 - u) ** 2 * P0[1] + 2 * (1 - u) * u * P1[1] + u * u * P2[1]];
      const stem = new Path2D(); stem.moveTo(...P0); stem.quadraticCurveTo(...P1, ...P2); stroke(g, stem, "#9C6A12", small ? 1 : 0.7);
      const n = small ? 5 : 8;
      for (let i = 1; i <= n; i++) {
        const u = i / n, [x, y] = at(u), [x2, y2] = at(Math.min(1, u + 0.02)), tang = Math.atan2(y2 - y, x2 - x), sz = 1 - u * 0.3;
        for (const k of i === n ? [0] : [-1, 1]) {
          g.save(); g.translate(x, y); g.rotate(tang + k * 0.7); g.scale(sz, sz);
          const leaf = new Path2D(); leaf.moveTo(0, 0); leaf.quadraticCurveTo(1.8, -1.3, 4, 0); leaf.quadraticCurveTo(1.8, 1.3, 0, 0); leaf.closePath();
          g.fillStyle = vGrad(g, ["#FFE9A6", "#F5B83D", "#C98A12"], -1.3, 1.3); g.fill(leaf);
          if (!small) { line(g, "rgba(150,95,10,0.6)", 0.4, 0, 3.4, 0, 0.18); stroke(g, leaf, "rgba(120,70,0,0.35)", 0.18); }
          g.restore();
        }
      }
    }
    // laço na frente
    for (const s of [-1, 1]) { const b = new Path2D(); b.moveTo(50, 45.6); b.quadraticCurveTo(50 + s * 2.4, 43.4, 50 + s * 3.6, 44.4); b.quadraticCurveTo(50 + s * 3.2, 46.6, 50, 45.6); b.closePath(); g.fillStyle = "#D23A55"; g.fill(b); }
    circle(g, "#A8233A", 0.9, 50, 45.6);
  }
  function drawStreakMedal(g, t, days, tilt) {
    // medalha da sequência pendurada na aba do capelo: bronze (7+), prata (30+), ouro (100+)
    const m = days >= 100 ? GOLD3 : days >= 30 ? ["#FFFFFF", "#CDD0DC", "#8E92A6"] : ["#F6C79A", "#D08B4E", "#94562A"];
    const sw = sin(t * 2.2 + 1) * 1.2 - tilt * 0.4, x = 25.5, y = 31, mx = x + sw * 0.4, my = y + 8;
    const rb = new Path2D(); rb.moveTo(x - 1.2, y); rb.lineTo(x + 1.2, y); rb.lineTo(mx + 0.9, my - 2); rb.lineTo(mx - 0.9, my - 2); rb.closePath();
    g.fillStyle = "#E0475E"; g.fill(rb); line(g, "rgba(255,255,255,0.6)", x, y + 0.2, mx, my - 2.2, 0.35);
    circle(g, m[2], 2.9, mx, my + 0.3);
    g.beginPath(); g.arc(mx, my, 2.6, 0, 2 * PI); g.fillStyle = vGrad(g, m, my - 2.6, my + 2.6); g.fill();
    const fl = new Path2D(); fl.moveTo(mx, my - 1.6); fl.quadraticCurveTo(mx + 1.5, my, mx, my + 1.4); fl.quadraticCurveTo(mx - 1.5, my, mx, my - 1.6);
    g.fillStyle = rgba(m[2], 0.9); g.fill(fl);
  }
  function drawNightcap(g, t, tilt, vy) {
    // touca de dormir listrada (à noite, parado ou cochilando), com pompom que balança
    const sw = sin(t * 1.6) * 1.6 - tilt * 0.6 + clamp(vy * 0.05, -3, 3), tx = 79 + sw, ty = 35 + abs(sw) * 0.3;
    const cone = new Path2D(); cone.moveTo(36.5, 41); cone.quadraticCurveTo(38, 23, 55, 21.5); cone.quadraticCurveTo(70 + sw * 0.4, 21, tx, ty);
    cone.quadraticCurveTo(68 + sw * 0.3, 28, 62.5, 31); cone.quadraticCurveTo(63, 36, 63.5, 41); cone.closePath();
    g.save(); g.clip(cone);
    g.fillStyle = "#4B3FD8"; g.fillRect(20, 10, 70, 40);
    for (let i = -6; i < 12; i++) { const b = new Path2D(), x = 20 + i * 6; b.moveTo(x, 50); b.lineTo(x + 3, 50); b.lineTo(x + 23, 10); b.lineTo(x + 20, 10); b.closePath(); g.fillStyle = "#DCD7FA"; g.fill(b); }
    g.fillStyle = vGrad(g, ["rgba(255,255,255,0.18)", "rgba(14,10,69,0.25)"], 20, 42); g.fillRect(20, 10, 70, 40);
    g.restore();
    stroke(g, cone, "rgba(14,10,69,0.35)", 0.4);
    const rim = new Path2D(); rim.moveTo(35, 41.5); rim.quadraticCurveTo(50, 37, 65, 41.5); rim.quadraticCurveTo(66.5, 44, 64.5, 45.4); rim.quadraticCurveTo(50, 41.4, 35.5, 45.4); rim.quadraticCurveTo(33.5, 44, 35, 41.5);
    g.fillStyle = vGrad(g, ["#FFFFFF", "#E4E0F2"], 38, 46); g.fill(rim); stroke(g, rim, "rgba(14,10,69,0.2)", 0.3);
    const pom = g.createRadialGradient(tx - 1, ty, 0.3, tx, ty + 1, 3.2); pom.addColorStop(0, "#FFFFFF"); pom.addColorStop(1, "#D9D4EE");
    g.beginPath(); g.arc(tx, ty + 1, 3, 0, 2 * PI); g.fillStyle = pom; g.fill();
  }
  // Sombra de contato: o chapéu assenta na cabeça (no alto das páginas) em vez de flutuar.
  function hatShadow(g, w) {
    g.save(); g.beginPath(); g.ellipse(50, 45.6, w, 2.4, 0, 0, 2 * PI);
    const rg = g.createRadialGradient(50, 45.6, 0.5, 50, 45.6, w); rg.addColorStop(0, "rgba(26,19,80,0.28)"); rg.addColorStop(1, "rgba(26,19,80,0)");
    g.fillStyle = rg; g.fill(); g.restore();
  }
  function drawBone(g) {
    // boné em três quartos, assentado no alto do livro: copa baixa de seis gomos (o botão puxado para a
    // direita), aba curta para a frente e à esquerda, fora do rosto, e o escudo bordado no gomo da frente
    hatShadow(g, 19);
    const crown = new Path2D();
    crown.moveTo(34.5, 44.6); crown.bezierCurveTo(33.6, 31, 42, 24.2, 52.4, 24.2); crown.bezierCurveTo(62, 24.2, 67.6, 31.4, 66.4, 44.2); crown.quadraticCurveTo(50.5, 46.6, 34.5, 44.6); crown.closePath();
    g.fillStyle = vGrad(g, ["#6E63F2", "#4338D8", "#2A209E"], 24, 46); g.fill(crown);
    g.save(); g.clip(crown);
    // lado de trás na sombra, gomos costurados saindo do botão
    const sh = g.createLinearGradient(52, 0, 68, 0); sh.addColorStop(0, "rgba(14,10,69,0)"); sh.addColorStop(1, "rgba(14,10,69,0.38)"); g.fillStyle = sh; g.fillRect(50, 20, 20, 28);
    const hl = g.createRadialGradient(42, 30, 1, 42, 30, 12); hl.addColorStop(0, "rgba(255,255,255,0.32)"); hl.addColorStop(1, "rgba(255,255,255,0)"); g.fillStyle = hl; g.fillRect(30, 20, 30, 28);
    for (const [cx, cy, x1, y1] of [[40, 30, 37.4, 45], [49.4, 34, 47.6, 46.4], [60, 32, 59.6, 45.6]]) { const p = new Path2D(); p.moveTo(54.2, 24.6); p.quadraticCurveTo(cx, cy, x1, y1); stroke(g, p, "rgba(18,13,63,0.32)", 0.45); stroke(g, p, "rgba(255,255,255,0.12)", 0.2); }
    // faixa de baixo (o carneira aparecendo um tico)
    const band = new Path2D(); band.moveTo(34.3, 42.4); band.quadraticCurveTo(50.5, 44.6, 66.6, 42); band.lineTo(66.4, 44.2); band.quadraticCurveTo(50.5, 46.6, 34.5, 44.6); band.closePath();
    g.fillStyle = "rgba(18,13,63,0.35)"; g.fill(band);
    g.restore();
    stroke(g, crown, "rgba(18,13,63,0.55)", 0.45);
    // escudo bordado no gomo da frente: o livrinho aberto
    g.save(); g.translate(46.2, 35.4); g.rotate(-0.06); g.scale(0.82, 0.86);
    const badge = new Path2D(); badge.roundRect(-5.6, -4, 11.2, 8, 2.2); g.fillStyle = "#FFFDF8"; g.fill(badge); stroke(g, badge, "rgba(18,13,63,0.25)", 0.35);
    const bk = new Path2D(); bk.moveTo(-3.8, -1.6); bk.quadraticCurveTo(-1.9, -2.6, 0, -1.4); bk.quadraticCurveTo(1.9, -2.6, 3.8, -1.6); bk.lineTo(3.8, 2); bk.quadraticCurveTo(1.9, 1, 0, 2.2); bk.quadraticCurveTo(-1.9, 1, -3.8, 2); bk.closePath();
    g.fillStyle = "#3A2FC4"; g.fill(bk); line(g, "#FFFDF8", 0, -1.2, 0, 1.9, 0.35);
    g.restore();
    // aba: sai da frente da copa para a esquerda, vista de cima, com a costura e o avesso escuro na ponta
    g.save(); g.translate(0, -0.9);
    const visor = new Path2D();
    visor.moveTo(35, 42.6); visor.bezierCurveTo(40, 45.4, 49, 46.4, 56.6, 45.4); visor.bezierCurveTo(50, 49.6, 38, 50.6, 29.6, 49.4); visor.bezierCurveTo(24, 48.6, 23.2, 45.4, 26.8, 43.8); visor.bezierCurveTo(29.4, 42.8, 32.4, 42.4, 35, 42.6); visor.closePath();
    const under = new Path2D(); under.moveTo(56.6, 45.4); under.bezierCurveTo(50, 50.4, 38, 51.4, 29.6, 50.2); under.bezierCurveTo(25.6, 49.6, 24, 48.4, 24.4, 47); under.bezierCurveTo(25, 48.6, 26.6, 49.2, 29.6, 49.4); under.bezierCurveTo(38, 50.6, 50, 49.6, 56.6, 45.4); under.closePath();
    g.fillStyle = "#140E52"; g.fill(under);
    const vg = g.createLinearGradient(30, 43, 46, 50); vg.addColorStop(0, "#4B40DC"); vg.addColorStop(1, "#2A209E"); g.fillStyle = vg; g.fill(visor);
    stroke(g, visor, "rgba(18,13,63,0.5)", 0.35);
    g.save(); g.setLineDash([0.8, 0.6]); const st = new Path2D(); st.moveTo(28.4, 46.2); st.bezierCurveTo(36, 48.6, 46, 48.4, 53.4, 46.2); stroke(g, st, "rgba(255,255,255,0.45)", 0.28); g.restore();
    line(g, "rgba(255,255,255,0.35)", 28.6, 44.4, 34, 43.2, 0.5);
    g.restore();
    // botão menta no topo
    circle(g, "#3FAF86", 1.6, 54.2, 24.6); circle(g, MINT, 1.3, 54, 24.3); circle(g, "rgba(255,255,255,0.75)", 0.45, 53.6, 23.9);
  }
  function drawBoinaMilitar(g, t) {
    // boina militar verde-oliva: caída para a direita, debrum de couro preto e o distintivo dourado na frente
    hatShadow(g, 18);
    const band = new Path2D(); band.moveTo(34.6, 41.4); band.quadraticCurveTo(50, 38.8, 65.4, 41.4); band.lineTo(65, 45.2); band.quadraticCurveTo(50, 42.4, 35, 45.2); band.closePath();
    g.fillStyle = vGrad(g, ["#2A2A22", "#121210"], 39, 46); g.fill(band);
    line(g, "rgba(255,255,255,0.18)", 37, 42.4, 63, 42.4, 0.35);
    g.save(); g.translate(53, 37.6); g.rotate(0.13); g.scale(0.94, 0.72);
    const top = new Path2D(); top.moveTo(-21.6, 1.8); top.bezierCurveTo(-24, -6, -12, -10.6, 0, -10.2); top.bezierCurveTo(13, -9.8, 25.4, -4.6, 23.4, 3.4); top.bezierCurveTo(22, 8, 9, 8.6, 0, 8); top.bezierCurveTo(-10, 7.4, -20.4, 6, -21.6, 1.8); top.closePath();
    const rg = g.createRadialGradient(-8, -4, 1, 0, 0, 24); rg.addColorStop(0, "#97A465"); rg.addColorStop(0.55, "#6B7840"); rg.addColorStop(1, "#46512A"); g.fillStyle = rg; g.fill(top);
    // feltro: pontinhos de textura e a dobra caída do lado
    g.save(); g.clip(top); for (let i = 0; i < 40; i++) circle(g, "rgba(30,36,14,0.16)", 0.3, -20 + ((i * 37) % 44), -9 + ((i * 23) % 17)); g.restore();
    const fold = new Path2D(); fold.moveTo(-14, 4.2); fold.quadraticCurveTo(4, 8.4, 21, 2.4); stroke(g, fold, "rgba(30,36,14,0.4)", 0.7);
    const fold2 = new Path2D(); fold2.moveTo(-12, -4.6); fold2.quadraticCurveTo(-3, -7, 9, -6); stroke(g, fold2, "rgba(255,255,255,0.2)", 0.6);
    g.restore();
    // distintivo: escudo dourado com ramos e a estrela, um brilho de vez em quando
    const bx = 41.6, by = 37.2;
    for (const s of [-1, 1]) { const br = new Path2D(); br.moveTo(bx + s * 0.8, by + 3.2); br.quadraticCurveTo(bx + s * 4.4, by + 2, bx + s * 4, by - 2.6); stroke(g, br, "#B57A0C", 0.9); for (let k = 0; k < 3; k++) { g.beginPath(); g.ellipse(bx + s * (2.4 + k * 0.6), by + 2.2 - k * 1.7, 0.9, 0.45, s * (0.9 + k * 0.3), 0, 2 * PI); g.fillStyle = "#F5C451"; g.fill(); } }
    const sh = new Path2D(); sh.moveTo(bx - 2.6, by - 3); sh.lineTo(bx + 2.6, by - 3); sh.lineTo(bx + 2.6, by + 0.6); sh.quadraticCurveTo(bx + 2.4, by + 2.8, bx, by + 3.8); sh.quadraticCurveTo(bx - 2.4, by + 2.8, bx - 2.6, by + 0.6); sh.closePath();
    g.fillStyle = vGrad(g, ["#FFF1BF", "#F5C451", "#B57A0C"], by - 3, by + 3.8); g.fill(sh); stroke(g, sh, "rgba(110,70,0,0.6)", 0.3);
    g.fillStyle = "#2E7D3A"; g.fill(star5(g, bx, by + 0.2, 1.7));
    const k = Math.max(0, sin(t * 1.2)) ** 10; if (k > 0.02) star4(g, bx + 2, by - 2.6, 2.6 * k, `rgba(255,255,255,${k})`);
  }
  function drawQuepe(g, t) {
    // quepe da polícia: copa azul-marinho larga, faixa preta com cordão dourado, pala de verniz e o brasão alado
    hatShadow(g, 18);
    const crown = new Path2D(); crown.moveTo(33, 40); crown.bezierCurveTo(28, 30, 34, 24.5, 50, 24.5); crown.bezierCurveTo(66, 24.5, 72, 30, 67, 40); crown.closePath();
    g.fillStyle = vGrad(g, ["#3A4A8C", "#22306A", "#141C46"], 24.5, 40); g.fill(crown);
    g.beginPath(); g.ellipse(50, 27.2, 17.6, 3.6, 0, 0, 2 * PI); g.fillStyle = vGrad(g, ["#4C5DA6", "#2A3878"], 23.6, 30.8); g.fill();
    line(g, "rgba(255,255,255,0.22)", 38, 26.4, 56, 25.4, 0.6);
    const band = new Path2D(); band.moveTo(34.4, 38.6); band.quadraticCurveTo(50, 36.2, 65.6, 38.6); band.lineTo(65.2, 44.4); band.quadraticCurveTo(50, 41.8, 34.8, 44.4); band.closePath();
    g.fillStyle = vGrad(g, ["#1A1A22", "#060608"], 37, 44.4); g.fill(band);
    const cord = new Path2D(); cord.moveTo(36, 42.6); cord.quadraticCurveTo(50, 40.6, 64, 42.6); stroke(g, cord, "#B57A0C", 0.9); stroke(g, cord, "#FFD877", 0.45);
    for (const x of [36, 64]) circle(g, "#E9AE34", 0.9, x, 42.6);
    const visor = new Path2D(); visor.moveTo(35, 44.2); visor.quadraticCurveTo(50, 41.6, 65, 44.2); visor.quadraticCurveTo(60, 49.4, 50, 49.6); visor.quadraticCurveTo(40, 49.4, 35, 44.2); visor.closePath();
    g.fillStyle = vGrad(g, ["#2C2C36", "#0B0B10"], 42, 49.6); g.fill(visor);
    const vh = new Path2D(); vh.moveTo(39, 45.4); vh.quadraticCurveTo(50, 44, 60, 45.4); stroke(g, vh, "rgba(255,255,255,0.35)", 0.5);
    const bx = 50, by = 32.6;
    for (const sd of [-1, 1]) { const w = new Path2D(); w.moveTo(bx + sd * 2.4, by - 0.6); w.quadraticCurveTo(bx + sd * 6.4, by - 2.6, bx + sd * 7.6, by - 0.4); w.quadraticCurveTo(bx + sd * 5, by + 0.6, bx + sd * 2.4, by + 1.4); w.closePath(); g.fillStyle = vGrad(g, GOLD3, by - 2.6, by + 1.4); g.fill(w); }
    const sh = new Path2D(); sh.moveTo(bx - 2.6, by - 3.2); sh.lineTo(bx + 2.6, by - 3.2); sh.lineTo(bx + 2.6, by + 0.6); sh.quadraticCurveTo(bx + 2.4, by + 2.8, bx, by + 3.8); sh.quadraticCurveTo(bx - 2.4, by + 2.8, bx - 2.6, by + 0.6); sh.closePath();
    g.fillStyle = vGrad(g, ["#FFF1BF", "#F5C451", "#B57A0C"], by - 3.2, by + 3.8); g.fill(sh); stroke(g, sh, "rgba(110,70,0,0.6)", 0.3);
    g.fillStyle = "#22306A"; g.fill(star5(g, bx, by + 0.1, 1.6));
    const k = Math.max(0, sin(t * 1.2 + 0.8)) ** 10; if (k > 0.02) star4(g, bx + 2.4, by - 2.8, 2.6 * k, `rgba(255,255,255,${k})`);
  }
  function drawViseira(g, t) {
    // viseira de contador: faixa escura, aba larga de acetato verde translúcido e um lápis preso do lado
    hatShadow(g, 14);
    const band = new Path2D(); band.moveTo(34.6, 37.4); band.quadraticCurveTo(50, 34.4, 65.4, 37.4); band.lineTo(65, 43.4); band.quadraticCurveTo(50, 40.4, 35, 43.4); band.closePath();
    g.fillStyle = vGrad(g, ["#3A3F4E", "#1E2230"], 37, 43.6); g.fill(band);
    line(g, "rgba(255,255,255,0.2)", 37, 40.2, 63, 40.2, 0.35);
    drawPencilAt(g, 63.5, 41.5, 69, 33, 0.9);
    const brim = new Path2D(); brim.moveTo(33.2, 42.2); brim.quadraticCurveTo(50, 39.2, 66.8, 42.2); brim.quadraticCurveTo(64.4, 47.6, 50, 48); brim.quadraticCurveTo(35.6, 47.6, 33.2, 42.2); brim.closePath();
    g.fillStyle = vGrad(g, ["rgba(70,190,120,0.82)", "rgba(30,140,80,0.62)"], 40, 48); g.fill(brim); stroke(g, brim, "rgba(10,70,40,0.7)", 0.45);
    const hl = new Path2D(); hl.moveTo(38, 43.4); hl.quadraticCurveTo(50, 41.6, 62, 43.4); stroke(g, hl, "rgba(220,255,230,0.6)", 0.5);
    const k = Math.max(0, sin(t * 0.9)) ** 12; if (k > 0.02) star4(g, 40, 45, 2 * k, `rgba(255,255,255,${k})`);
  }
  function drawTouca(g, t) {
    // touca de centro cirúrgico: tecido azul-turquesa estampado, barra dobrada e o laço amarrado do lado
    hatShadow(g, 18);
    const dome = new Path2D(); dome.moveTo(33.6, 44); dome.bezierCurveTo(31.6, 30, 40, 21.6, 50, 21.6); dome.bezierCurveTo(60, 21.6, 68.4, 30, 66.4, 44); dome.quadraticCurveTo(50, 40.6, 33.6, 44); dome.closePath();
    g.fillStyle = vGrad(g, ["#6FD6D0", "#3FB3AD", "#2A8C88"], 21.6, 44); g.fill(dome);
    g.save(); g.clip(dome);
    // estampa: cruzinhas e coraçõezinhos brancos
    for (let i = 0; i < 16; i++) { const x = 36 + ((i * 13) % 28), y = 25 + ((i * 7) % 15); if (i % 2) { g.fillStyle = "rgba(255,255,255,0.75)"; g.fillRect(x - 0.25, y - 0.8, 0.5, 1.6); g.fillRect(x - 0.8, y - 0.25, 1.6, 0.5); } else { g.fillStyle = "rgba(255,170,190,0.85)"; g.fill(heart(x, y, 0.9)); } }
    const hl = g.createRadialGradient(43, 27, 1, 43, 27, 13); hl.addColorStop(0, "rgba(255,255,255,0.35)"); hl.addColorStop(1, "rgba(255,255,255,0)"); g.fillStyle = hl; g.fillRect(30, 18, 40, 28);
    for (const x of [41, 50, 59]) { const p = new Path2D(); p.moveTo(x, 38.6); p.quadraticCurveTo(x - 0.8, 33, x + 0.4, 28); stroke(g, p, "rgba(20,90,88,0.28)", 0.5); }
    g.restore();
    const band = new Path2D(); band.moveTo(33.4, 39.6); band.quadraticCurveTo(50, 36.8, 66.6, 39.6); band.lineTo(66.4, 44.4); band.quadraticCurveTo(50, 41.4, 33.6, 44.4); band.closePath();
    g.fillStyle = vGrad(g, ["#56C8C2", "#2E9C97"], 37, 44.4); g.fill(band); stroke(g, band, "rgba(20,90,88,0.4)", 0.3);
    g.save(); g.setLineDash([0.8, 0.6]); const st = new Path2D(); st.moveTo(34.4, 43.2); st.quadraticCurveTo(50, 40.4, 65.6, 43.2); stroke(g, st, "rgba(255,255,255,0.55)", 0.25); g.restore();
    // laço do lado direito, com as pontas balançando
    const sw = sin(t * 2.2) * 0.8;
    for (const [dx, len] of [[0, 9.6], [1.6, 8]]) { const tl = new Path2D(); tl.moveTo(66 + dx * 0.4, 41.6); tl.quadraticCurveTo(69 + dx + sw * 0.5, 45, 68.6 + dx + sw, 41.6 + len); stroke(g, tl, "#2E9C97", 1.5); stroke(g, tl, "rgba(255,255,255,0.3)", 0.35); }
    for (const s of [-1, 1]) { g.beginPath(); g.ellipse(66.4 + s * 2, 40.6, 2.2, 1.3, s * 0.5, 0, 2 * PI); g.fillStyle = "#3FB3AD"; g.fill(); }
    circle(g, "#2A8C88", 1, 66.4, 40.8);
  }
  function drawBoina(g) {
    // boina de artista, de lado, com a faixa abraçando a cabeça e dobras macias
    hatShadow(g, 18);
    const band = new Path2D(); band.moveTo(34.6, 41.6); band.quadraticCurveTo(50, 39, 65.4, 41.6); band.lineTo(65, 45.4); band.quadraticCurveTo(50, 42.6, 35, 45.4); band.closePath();
    g.fillStyle = vGrad(g, ["#8E1A2E", "#6B1222"], 40, 46); g.fill(band);
    g.save(); g.translate(47.5, 34.6); g.rotate(-0.12);
    const top = new Path2D(); top.moveTo(-21, 2); top.bezierCurveTo(-23, -6, -10, -11, 1, -10.4); top.bezierCurveTo(14, -10, 24, -5, 21.6, 1.6); top.bezierCurveTo(20, 6.6, 8, 8.4, 0, 8); top.bezierCurveTo(-10, 7.6, -20, 6.4, -21, 2); top.closePath();
    const rg = g.createRadialGradient(-7, -4, 1, 0, 0, 23); rg.addColorStop(0, "#F27387"); rg.addColorStop(0.55, "#D23A55"); rg.addColorStop(1, "#9C1F36"); g.fillStyle = rg; g.fill(top);
    const fold = new Path2D(); fold.moveTo(-16, 3.4); fold.quadraticCurveTo(0, 8.6, 17, 3); stroke(g, fold, "rgba(100,10,30,0.35)", 0.6);
    const fold2 = new Path2D(); fold2.moveTo(-10, -4.6); fold2.quadraticCurveTo(-2, -6.6, 8, -5.4); stroke(g, fold2, "rgba(255,255,255,0.25)", 0.6);
    g.restore();
    const stem = new Path2D(); stem.moveTo(43.6, 25.6); stem.quadraticCurveTo(43, 23.6, 44.4, 22.8); stroke(g, stem, "#7A1428", 1.2);
  }
  function drawExplorador(g) {
    // chapéu de explorador: copa redonda, aba larga com costura, fita marrom e o broche de bússola
    hatShadow(g, 24);
    const brim = new Path2D(); brim.ellipse(50, 42.8, 26, 5.6, 0, 0, 2 * PI);
    g.fillStyle = vGrad(g, ["#E2C68C", "#B8955A", "#94743F"], 37, 48.4); g.fill(brim);
    g.save(); g.setLineDash([1, 0.8]); g.beginPath(); g.ellipse(50, 42.8, 23.6, 4.6, 0, 0.05 * PI, 0.95 * PI); g.strokeStyle = "rgba(90,60,25,0.45)"; g.lineWidth = 0.35; g.stroke(); g.restore();
    const dome = new Path2D(); dome.moveTo(35, 42); dome.bezierCurveTo(34, 26.5, 42, 21.5, 50, 21.5); dome.bezierCurveTo(58, 21.5, 66, 26.5, 65, 42); dome.quadraticCurveTo(50, 44.4, 35, 42); dome.closePath();
    g.fillStyle = vGrad(g, ["#EFDCAE", "#CFAE6F", "#A8854B"], 21, 43); g.fill(dome);
    g.save(); g.clip(dome); const hl = g.createRadialGradient(43, 27, 1, 43, 27, 12); hl.addColorStop(0, "rgba(255,255,255,0.35)"); hl.addColorStop(1, "rgba(255,255,255,0)"); g.fillStyle = hl; g.fillRect(30, 18, 40, 28);
    const crease = new Path2D(); crease.moveTo(50, 22); crease.quadraticCurveTo(48.6, 30, 50, 37.5); stroke(g, crease, "rgba(110,80,40,0.35)", 0.6); g.restore();
    const band = new Path2D(); band.moveTo(35.3, 37.4); band.quadraticCurveTo(50, 39.6, 64.7, 37.4); band.lineTo(64.9, 41.4); band.quadraticCurveTo(50, 43.8, 35.1, 41.4); band.closePath();
    g.fillStyle = vGrad(g, ["#7A5530", "#5A3D20"], 37, 42); g.fill(band);
    circle(g, GOLD, 1.6, 59.2, 40); g.fillStyle = "#7A4F05"; g.fill(star5(g, 59.2, 40, 1));
  }
  function drawFones(g, t) {
    // headset: arco acolchoado, conchas grandes nas bordas do livro, luz menta e microfone até a boca
    const arc = new Path2D(); arc.moveTo(11, 52); arc.bezierCurveTo(9.5, 14.5, 90.5, 14.5, 89, 52);
    stroke(g, arc, "#1E1B2E", 4.4); stroke(g, arc, "#3A3550", 2.6);
    const pad = new Path2D(); pad.moveTo(36, 25.4); pad.quadraticCurveTo(50, 20.6, 64, 25.4); stroke(g, pad, "#2A209E", 5.2); stroke(g, pad, "#4B3FD8", 3.6);
    line(g, "rgba(255,255,255,0.35)", 40, 23.8, 49, 21.8, 0.9);
    for (const side of [-1, 1]) {
      const x = side < 0 ? 3.6 : 86.4, cup = new Path2D(); cup.roundRect(x, 46.5, 10, 18.5, 4.6);
      g.fillStyle = hGrad(g, side < 0 ? ["#2A209E", "#4B3FD8"] : ["#4B3FD8", "#2A209E"], x, x + 10); g.fill(cup);
      const cush = new Path2D(); cush.roundRect(side < 0 ? x + 6.6 : x - 0.4, 47.6, 3.8, 16.3, 1.9); g.fillStyle = "#24242E"; g.fill(cush);
      const ring = new Path2D(); ring.ellipse(x + 5, 55.8, 2.6, 4.6, 0, 0, 2 * PI); g.strokeStyle = MINT; g.lineWidth = 0.9; g.stroke(ring);
      circle(g, `rgba(126,224,184,${0.6 + 0.4 * sin(t * 3 + side)})`, 0.8, x + 5, 55.8);
      line(g, "rgba(255,255,255,0.35)", x + (side < 0 ? 1.8 : 7.6), 48.6, x + (side < 0 ? 1.8 : 7.6), 54, 0.8);
    }
    // microfone saindo da concha direita
    const boom = new Path2D(); boom.moveTo(88, 62.5); boom.bezierCurveTo(84, 74, 72, 77.5, 63.5, 76.2); stroke(g, boom, "#1E1B2E", 1.4);
    const mic = new Path2D(); mic.ellipse(62.4, 76, 2.4, 1.7, -0.15, 0, 2 * PI); g.fillStyle = "#3A3550"; g.fill(mic); circle(g, "rgba(255,255,255,0.45)", 0.5, 61.8, 75.4);
  }
  function drawCartola(g) {
    // cartola de gala, levemente inclinada, com faixa dourada e brilho de cetim
    hatShadow(g, 19);
    g.save(); g.translate(50, 43); g.rotate(-0.07); g.translate(-50, -43);
    const brim = new Path2D(); brim.ellipse(50, 42.6, 19.5, 4.2, 0, 0, 2 * PI);
    g.fillStyle = vGrad(g, ["#45405E", "#14121F"], 38, 47); g.fill(brim);
    const body = new Path2D(); body.moveTo(37.8, 42); body.lineTo(39, 13.5); body.quadraticCurveTo(50, 11.6, 61, 13.5); body.lineTo(62.2, 42); body.quadraticCurveTo(50, 44.6, 37.8, 42); body.closePath();
    g.fillStyle = hGrad(g, ["#1A1726", "#423C60", "#2A2640", "#15121F"], 38, 62); g.fill(body);
    const band = new Path2D(); band.moveTo(38.3, 35); band.quadraticCurveTo(50, 36.4, 61.7, 35); band.lineTo(62, 39.4); band.quadraticCurveTo(50, 41.2, 38, 39.4); band.closePath();
    g.fillStyle = vGrad(g, GOLD3, 35, 40); g.fill(band);
    const top = new Path2D(); top.ellipse(50, 13.4, 11, 2.4, 0, 0, 2 * PI); g.fillStyle = vGrad(g, ["#4A4466", "#2A2640"], 11, 16); g.fill(top);
    line(g, "rgba(255,255,255,0.28)", 42.6, 16, 41.8, 33.6, 1.1);
    line(g, "rgba(255,255,255,0.12)", 45, 16, 44.4, 33.6, 0.6);
    g.restore();
  }
  function drawCoroa(g, t) {
    // coroa de ouro com veludo por dentro, pérolas nas pontas e pedras
    hatShadow(g, 19);
    const velvet = new Path2D(); velvet.moveTo(35, 41); velvet.quadraticCurveTo(50, 22, 65, 41); velvet.closePath();
    g.fillStyle = vGrad(g, ["#C42F4B", "#7A1428"], 24, 41); g.fill(velvet);
    const c = new Path2D();
    c.moveTo(33.4, 45); c.lineTo(32.6, 28.5); c.lineTo(39.6, 34.6); c.lineTo(42.6, 23.6); c.lineTo(46.8, 32.6); c.lineTo(50, 18.6); c.lineTo(53.2, 32.6); c.lineTo(57.4, 23.6);
    c.lineTo(60.4, 34.6); c.lineTo(67.4, 28.5); c.lineTo(66.6, 45); c.quadraticCurveTo(50, 41.8, 33.4, 45); c.closePath();
    g.fillStyle = vGrad(g, ["#FFF1BF", "#F5C451", "#D99A1B", "#B57A0C"], 18, 45); g.fill(c); stroke(g, c, "rgba(120,70,0,0.55)", 0.4);
    const rim = new Path2D(); rim.moveTo(33.2, 39.4); rim.quadraticCurveTo(50, 36.6, 66.8, 39.4); rim.lineTo(66.7, 41.6); rim.quadraticCurveTo(50, 38.8, 33.3, 41.6); rim.closePath();
    g.fillStyle = "rgba(160,100,10,0.45)"; g.fill(rim);
    for (const [x, y] of [[32.6, 28.5], [42.6, 23.6], [50, 18.6], [57.4, 23.6], [67.4, 28.5]]) { circle(g, "#FFFDF4", 1.35, x, y); circle(g, "rgba(200,170,120,0.6)", 0.5, x + 0.4, y + 0.4); }
    for (const [x, col, r] of [[50, "#E0475E", 2], [42, "#3D8BEA", 1.4], [58, "#36C897", 1.4]]) { g.beginPath(); g.ellipse(x, 42.4, r, r * 1.2, 0, 0, 2 * PI); g.fillStyle = col; g.fill(); circle(g, "rgba(255,255,255,0.7)", r * 0.35, x - r * 0.35, 42.4 - r * 0.45); }
    const k = Math.max(0, sin(t * 1.4)) ** 8; if (k > 0.02) star4(g, 46.5, 27, 3.4 * k, `rgba(255,255,255,${k})`);
  }
  function drawMago(g, t, tilt, vy) {
    // chapéu de mago de veludo: aba larga, ponta que tomba e balança, lua e estrelas bordadas
    hatShadow(g, 21);
    const sway = sin(t * 1.8) * 1.6 - tilt * 0.5 + clamp(vy * 0.05, -3, 3);
    const brim = new Path2D(); brim.ellipse(50, 42.6, 22.5, 4.4, 0, 0, 2 * PI);
    g.fillStyle = vGrad(g, ["#5A3DC4", "#2A1A78", "#1C1155"], 38, 47); g.fill(brim);
    const tipX = 63 + sway, tipY = 12 + abs(sway) * 0.3;
    const cone = new Path2D(); cone.moveTo(33.4, 42); cone.bezierCurveTo(38.6, 32, 43, 18, 51, 10.6); cone.quadraticCurveTo(57, 6.4, tipX + 2, tipY - 1.6);
    cone.quadraticCurveTo(tipX - 1, tipY + 2.4, 57.6, 16); cone.bezierCurveTo(58.4, 26, 61.6, 34, 66.6, 42); cone.quadraticCurveTo(50, 38.8, 33.4, 42); cone.closePath();
    g.fillStyle = hGrad(g, ["#7B5CE6", "#5A3DC4", "#3A2788"], 34, 66); g.fill(cone);
    g.save(); g.clip(cone);
    const hl = g.createRadialGradient(43, 26, 1, 43, 26, 14); hl.addColorStop(0, "rgba(255,255,255,0.28)"); hl.addColorStop(1, "rgba(255,255,255,0)"); g.fillStyle = hl; g.fillRect(30, 6, 40, 40);
    // lua bordada e estrelas
    const moon = new Path2D(); moon.arc(45.6, 31, 3.2, 0, 2 * PI); g.fillStyle = vGrad(g, GOLD3, 27.8, 34.2); g.fill(moon);
    circle(g, "#5A3DC4", 2.7, 47, 30);
    for (const [x, y, r] of [[54, 25, 1.7], [50.4, 37.2, 1.2], [55.8, 34, 1], [52, 17, 1.1]]) { g.fillStyle = vGrad(g, GOLD3, y - r, y + r); g.fill(star5(g, x, y, r)); }
    g.restore();
    const band = new Path2D(); band.moveTo(34.4, 40.4); band.quadraticCurveTo(50, 37.4, 65.6, 40.4); band.lineTo(66.2, 42.8); band.quadraticCurveTo(50, 39.8, 33.8, 42.8); band.closePath();
    g.fillStyle = vGrad(g, GOLD3, 37.4, 42.8); g.fill(band);
    circle(g, "#B995F5", 1.1, 50, 40.2); circle(g, "rgba(255,255,255,0.7)", 0.4, 49.7, 39.9);
    const k = 0.6 + 0.4 * sin(t * 3); star4(g, tipX + 2.2, tipY - 1.6, 2.4 * k, `rgba(255,240,180,${0.95 * k})`);
  }
  function drawAureola(g, t) {
    // auréola de luz: anel dourado que flutua e brilha, com faíscas girando
    const y = 22.5 + sin(t * 2) * 1.2;
    g.save(); g.shadowColor = "rgba(255,214,110,0.95)"; g.shadowBlur = 4;
    g.beginPath(); g.ellipse(50, y, 13.5, 3.4, 0, 0, 2 * PI); g.strokeStyle = "#FFC94A"; g.lineWidth = 2.4; g.stroke();
    g.restore();
    g.beginPath(); g.ellipse(50, y, 13.5, 3.4, 0, 0, 2 * PI); g.strokeStyle = vGrad(g, ["#FFF6D6", "#FFD877", "#E9A82B"], y - 3.4, y + 3.4); g.lineWidth = 1.6; g.stroke();
    g.beginPath(); g.ellipse(50, y, 13.5, 3.4, 0, PI * 1.05, PI * 1.6); g.strokeStyle = "rgba(255,255,255,0.9)"; g.lineWidth = 0.5; g.stroke();
    for (let i = 0; i < 3; i++) { const a = t * 1.4 + i * 2 * PI / 3, k = 0.5 + 0.5 * sin(t * 4 + i); star4(g, 50 + cos(a) * 13.5, y + sin(a) * 3.4, 1.6 * k, `rgba(255,255,255,${0.9 * k})`); }
  }
  // ---- chapéus do armário 2.0
  // elipse girada preenchida (manchas, pétalas, folhas)
  function hatEll(g, color, x, y, rx, ry, a) { g.beginPath(); g.ellipse(x, y, rx, ry, a || 0, 0, 2 * PI); g.fillStyle = color; g.fill(); }
  // ponto numa curva quadrática (para enfileirar flores e rebites ao longo de um arco)
  function hatQ(u, x0, y0, cx, cy, x1, y1) { const v = 1 - u; return [v * v * x0 + 2 * v * u * cx + u * u * x1, v * v * y0 + 2 * v * u * cy + u * u * y1]; }
  function hatGloss(g, x, y, r, a) { const hl = g.createRadialGradient(x, y, 1, x, y, r); hl.addColorStop(0, `rgba(255,255,255,${a})`); hl.addColorStop(1, "rgba(255,255,255,0)"); g.fillStyle = hl; g.fillRect(x - r, y - r, 2 * r, 2 * r); }
  function drawEspelhoFrontal(g, t) {
    // espelho frontal de médico: faixa elástica clara na testa, articulação e o espelho redondo com o furinho
    // no meio; um reflexo corre pelo espelho e às vezes ele cintila
    hatShadow(g, 16);
    const band = new Path2D(); band.moveTo(33.4, 39.6); band.quadraticCurveTo(50, 36.6, 66.6, 39.6); band.lineTo(66.4, 44.2); band.quadraticCurveTo(50, 41.2, 33.6, 44.2); band.closePath();
    g.fillStyle = vGrad(g, ["#F4F2FA", "#C9C6D8"], 37, 44.2); g.fill(band); stroke(g, band, "rgba(30,27,46,0.35)", 0.35);
    g.save(); g.setLineDash([0.8, 0.6]); const st = new Path2D(); st.moveTo(34.4, 42.8); st.quadraticCurveTo(50, 39.8, 65.6, 42.8); stroke(g, st, "rgba(90,86,120,0.45)", 0.25); g.restore();
    line(g, "#3A3550", 50, 39.4, 50, 36, 1.6); line(g, "#8E8AA6", 50, 39.4, 50, 36, 0.6);
    circle(g, "#5A5670", 1.5, 50, 39.2); circle(g, "#C9C6D8", 0.6, 49.6, 38.8);
    const mx = 50, my = 29.4, r = 7.4;
    circle(g, "#2A2640", r + 1.1, mx, my);
    const mg = g.createRadialGradient(mx - 2.4, my - 2.6, 0.5, mx, my, r); mg.addColorStop(0, "#FFFFFF"); mg.addColorStop(0.45, "#DDE6F2"); mg.addColorStop(1, "#8A97AE");
    g.beginPath(); g.arc(mx, my, r, 0, 2 * PI); g.fillStyle = mg; g.fill();
    g.beginPath(); g.arc(mx, my, r - 1.4, 0, 2 * PI); g.strokeStyle = "rgba(255,255,255,0.55)"; g.lineWidth = 0.4; g.stroke();
    g.save(); g.beginPath(); g.arc(mx, my, r, 0, 2 * PI); g.clip(); const sx = mx - 12 + ((t * 0.3) % 1) * 24; line(g, "rgba(255,255,255,0.6)", sx - 3, my + 8, sx + 3, my - 8, 2); g.restore();
    circle(g, "#2A2640", 1.2, mx, my); circle(g, "rgba(255,255,255,0.5)", 0.35, mx - 0.4, my - 0.4);
    const k = Math.max(0, sin(t * 1.3)) ** 8; if (k > 0.02) star4(g, mx - 3.4, my - 3.6, 3 * k, `rgba(255,255,255,${k})`);
  }
  function drawCapacete(g, t) {
    // capacete militar camuflado: casco verde com manchas, a rede por cima, aba do casco e a jugular com fivela
    hatShadow(g, 20);
    const dome = new Path2D(); dome.moveTo(31.6, 44.4); dome.bezierCurveTo(30.4, 28, 39.4, 20.6, 50, 20.6); dome.bezierCurveTo(60.6, 20.6, 69.6, 28, 68.4, 44.4); dome.quadraticCurveTo(50, 41.6, 31.6, 44.4); dome.closePath();
    g.fillStyle = vGrad(g, ["#8A9A5B", "#5F6E38", "#3E4A22"], 20.6, 44.4); g.fill(dome);
    g.save(); g.clip(dome);
    for (const [x, y, rx, ry, a, c] of [[38, 30, 5, 2.6, 0.4, "#3B4420"], [55, 25.6, 6, 2.4, -0.3, "#7A5A34"], [61.4, 36, 4.6, 2.8, 0.6, "#3B4420"], [44, 38, 6, 2, 0.1, "#7A5A34"], [50, 31.6, 3.4, 2, -0.8, "#C2B78A"], [35, 40, 3, 1.6, 0.3, "#C2B78A"], [64, 28.6, 2.6, 1.6, 0.9, "#C2B78A"], [43.6, 24.4, 2.6, 1.4, 0.2, "#3B4420"]]) hatEll(g, c, x, y, rx, ry, a);
    for (let i = -4; i < 10; i++) { line(g, "rgba(30,34,14,0.38)", 20 + i * 5, 46, 34 + i * 5, 18, 0.3); line(g, "rgba(30,34,14,0.38)", 34 + i * 5, 46, 20 + i * 5, 18, 0.3); }
    hatGloss(g, 42, 27, 13, 0.3);
    g.restore();
    stroke(g, dome, "rgba(30,34,14,0.55)", 0.45);
    const rim = new Path2D(); rim.moveTo(30.4, 43.2); rim.quadraticCurveTo(50, 39.6, 69.6, 43.2); rim.quadraticCurveTo(70.6, 45.4, 69, 46.2); rim.quadraticCurveTo(50, 42.8, 31, 46.2); rim.quadraticCurveTo(29.4, 45.4, 30.4, 43.2); rim.closePath();
    g.fillStyle = vGrad(g, ["#6E7E44", "#38431E"], 41, 46.2); g.fill(rim); stroke(g, rim, "rgba(30,34,14,0.5)", 0.3);
    line(g, "rgba(255,255,255,0.22)", 36, 42.6, 46, 41.4, 0.4);
    for (const s of [-1, 1]) { const x0 = 50 + s * 18.4, p = new Path2D(); p.moveTo(x0, 45); p.quadraticCurveTo(x0 + s * 1.2, 49, x0 + s * 0.4, 52.6); stroke(g, p, "#4A3A22", 1.1); g.fillStyle = "#C9C6B0"; g.fillRect(x0 + s * 0.6 - 0.9, 48.6, 1.8, 1.4); }
  }
  function drawBoneTatico(g, t) {
    // boné tático da polícia: copa azul-marinho quase preta em gomos, aba reta com costura e o patch de velcro
    // com o escudo prateado e a estrela
    hatShadow(g, 18);
    const crown = new Path2D(); crown.moveTo(34, 43.6); crown.bezierCurveTo(32.6, 29.6, 40.6, 23.6, 50, 23.6); crown.bezierCurveTo(59.4, 23.6, 67.4, 29.6, 66, 43.6); crown.quadraticCurveTo(50, 41, 34, 43.6); crown.closePath();
    g.fillStyle = vGrad(g, ["#3A4466", "#1C2340", "#0E1226"], 23.6, 43.6); g.fill(crown);
    g.save(); g.clip(crown);
    hatGloss(g, 42, 29, 12, 0.25);
    for (const [cx, x1] of [[42, 38.6], [50, 50], [58, 61.4]]) { const p = new Path2D(); p.moveTo(50, 24); p.quadraticCurveTo(cx, 32, x1, 43); stroke(g, p, "rgba(0,0,0,0.4)", 0.4); stroke(g, p, "rgba(255,255,255,0.08)", 0.2); }
    g.restore();
    stroke(g, crown, "rgba(5,8,20,0.6)", 0.45);
    circle(g, "#0E1226", 1.3, 50, 24.2); circle(g, "rgba(255,255,255,0.3)", 0.4, 49.6, 23.8);
    const pb = new Path2D(); pb.roundRect(44.2, 29.8, 11.6, 8.8, 1.6); g.fillStyle = "#2B3352"; g.fill(pb); stroke(g, pb, "rgba(0,0,0,0.4)", 0.3);
    g.save(); g.setLineDash([0.6, 0.5]); const pb2 = new Path2D(); pb2.roundRect(44.8, 30.4, 10.4, 7.6, 1.2); stroke(g, pb2, "rgba(255,255,255,0.35)", 0.25); g.restore();
    const bx = 50, by = 34;
    const sh = new Path2D(); sh.moveTo(bx - 2.8, by - 3); sh.lineTo(bx + 2.8, by - 3); sh.lineTo(bx + 2.8, by + 0.4); sh.quadraticCurveTo(bx + 2.6, by + 2.6, bx, by + 3.6); sh.quadraticCurveTo(bx - 2.6, by + 2.6, bx - 2.8, by + 0.4); sh.closePath();
    g.fillStyle = vGrad(g, ["#FFFFFF", "#C9CED8", "#7E8498"], by - 3, by + 3.6); g.fill(sh); stroke(g, sh, "rgba(20,24,40,0.6)", 0.3);
    g.fillStyle = "#1C2340"; g.fill(star5(g, bx, by + 0.1, 1.7));
    const visor = new Path2D(); visor.moveTo(34.4, 42.4); visor.quadraticCurveTo(50, 39.8, 65.6, 42.4); visor.quadraticCurveTo(62, 49.4, 50, 49.8); visor.quadraticCurveTo(38, 49.4, 34.4, 42.4); visor.closePath();
    g.fillStyle = vGrad(g, ["#252C4A", "#0A0D1C"], 41, 49.8); g.fill(visor); stroke(g, visor, "rgba(0,0,0,0.5)", 0.35);
    g.save(); g.setLineDash([0.8, 0.6]); const vs = new Path2D(); vs.moveTo(37.4, 44.4); vs.quadraticCurveTo(50, 46.6, 62.6, 44.4); stroke(g, vs, "rgba(255,255,255,0.3)", 0.25); g.restore();
    line(g, "rgba(255,255,255,0.25)", 39, 43.2, 46, 42.2, 0.5);
    const k = Math.max(0, sin(t * 1.2 + 2)) ** 10; if (k > 0.02) star4(g, bx + 2.6, by - 2.6, 2.6 * k, `rgba(255,255,255,${k})`);
  }
  function drawBarrete(g, t) {
    // barrete de jurista: copa preta macia, quase quadrada, com três cristas em pé e a borla no alto que balança
    hatShadow(g, 18);
    const body = new Path2D(); body.moveTo(34.6, 44); body.lineTo(34, 31); body.quadraticCurveTo(34, 25.4, 40, 25); body.quadraticCurveTo(50, 23.4, 60, 25); body.quadraticCurveTo(66, 25.4, 66, 31); body.lineTo(65.4, 44); body.quadraticCurveTo(50, 41.6, 34.6, 44); body.closePath();
    g.fillStyle = hGrad(g, ["#14121C", "#3A3650", "#24212F", "#0E0D14"], 34, 66); g.fill(body);
    g.save(); g.clip(body);
    for (const x of [42, 50, 58]) { line(g, "rgba(0,0,0,0.45)", x, 26, x + 0.3, 42, 0.8); line(g, "rgba(255,255,255,0.12)", x + 0.7, 26, x + 1, 42, 0.4); }
    hatGloss(g, 41, 30, 10, 0.18);
    g.restore();
    stroke(g, body, "rgba(0,0,0,0.6)", 0.4);
    for (const x of [40.6, 50, 59.4]) {
      const fin = new Path2D(); fin.moveTo(x - 3, 26.2); fin.quadraticCurveTo(x - 0.6, 18.8, x, 18.6); fin.quadraticCurveTo(x + 0.6, 18.8, x + 3, 26.2); fin.closePath();
      g.fillStyle = hGrad(g, ["#2A2640", "#4A4466", "#14121C"], x - 3, x + 3); g.fill(fin); stroke(g, fin, "rgba(0,0,0,0.55)", 0.3);
      line(g, "rgba(255,255,255,0.2)", x - 0.6, 20, x - 1.6, 25.2, 0.35);
    }
    const band = new Path2D(); band.moveTo(34.5, 40.6); band.quadraticCurveTo(50, 38.2, 65.5, 40.6); band.lineTo(65.4, 44); band.quadraticCurveTo(50, 41.6, 34.6, 44); band.closePath();
    g.fillStyle = vGrad(g, ["#2A2640", "#0E0D14"], 39, 44); g.fill(band);
    // borla: botão no alto, cordão e franjas balançando
    const sw = sin(t * 2) * 0.9;
    const cord = new Path2D(); cord.moveTo(50, 19); cord.quadraticCurveTo(54 + sw * 0.4, 18.6, 55.6 + sw, 22.6); stroke(g, cord, "#B57A0C", 0.7);
    circle(g, "#1A1726", 1.8, 50, 18.6); circle(g, "rgba(255,255,255,0.35)", 0.5, 49.5, 18);
    const tx = 55.6 + sw, ty = 22.6;
    const tas = new Path2D(); tas.moveTo(tx - 0.8, ty); tas.lineTo(tx + 0.8, ty); tas.lineTo(tx + 2.2 + sw * 0.3, ty + 6.4); tas.lineTo(tx - 2 + sw * 0.3, ty + 6.4); tas.closePath();
    g.fillStyle = vGrad(g, ["#2A2640", "#0E0D14"], ty, ty + 6.4); g.fill(tas);
    for (let i = 0; i < 5; i++) line(g, "rgba(255,255,255,0.18)", tx - 0.4 + i * 0.2, ty + 1, tx - 1.6 + i * 0.85 + sw * 0.3, ty + 6.2, 0.2);
    circle(g, GOLD, 1, tx, ty + 0.2); circle(g, "rgba(255,255,255,0.6)", 0.3, tx - 0.3, ty);
  }
  function drawFedora(g, t) {
    // fedora de auditor: aba virada (atrás sobe, frente desce), copa cinza com vinco e os dois apertos na frente,
    // fita preta com o lacinho do lado e uma peninha
    hatShadow(g, 22);
    const brim = new Path2D(); brim.moveTo(25.4, 42.4); brim.quadraticCurveTo(50, 35.6, 74.6, 42.4); brim.quadraticCurveTo(77, 45.6, 72, 46.6); brim.quadraticCurveTo(50, 50.2, 28, 46.6); brim.quadraticCurveTo(23, 45.6, 25.4, 42.4); brim.closePath();
    g.fillStyle = vGrad(g, ["#8E92A6", "#5C6074", "#3E4152"], 37, 50); g.fill(brim); stroke(g, brim, "rgba(20,22,34,0.5)", 0.35);
    line(g, "rgba(255,255,255,0.3)", 29, 45.6, 40, 47.6, 0.4);
    const crown = new Path2D(); crown.moveTo(35.6, 42); crown.bezierCurveTo(34.4, 32, 36.4, 24.6, 42, 23.6); crown.quadraticCurveTo(46, 25.6, 50, 24.8); crown.quadraticCurveTo(54, 25.6, 58, 23.6); crown.bezierCurveTo(63.6, 24.6, 65.6, 32, 64.4, 42); crown.quadraticCurveTo(50, 44.4, 35.6, 42); crown.closePath();
    g.fillStyle = hGrad(g, ["#6E7286", "#A6AABC", "#7E8296", "#565A6C"], 35, 65); g.fill(crown);
    g.save(); g.clip(crown);
    const v = new Path2D(); v.moveTo(50, 25.2); v.quadraticCurveTo(48.8, 30, 50, 35); stroke(g, v, "rgba(30,32,46,0.5)", 0.9); const v2 = new Path2D(); v2.moveTo(51, 25.4); v2.quadraticCurveTo(50, 30, 51.2, 34.6); stroke(g, v2, "rgba(255,255,255,0.3)", 0.4);
    hatEll(g, "rgba(30,32,46,0.2)", 43.6, 31, 1.6, 3.8, 0.2); hatEll(g, "rgba(30,32,46,0.2)", 56.4, 31, 1.6, 3.8, -0.2);
    hatGloss(g, 41, 29, 9, 0.25);
    g.restore();
    stroke(g, crown, "rgba(20,22,34,0.5)", 0.4);
    const band = new Path2D(); band.moveTo(35.2, 37); band.quadraticCurveTo(50, 39.4, 64.8, 37); band.lineTo(65, 41.6); band.quadraticCurveTo(50, 44, 35, 41.6); band.closePath();
    g.fillStyle = vGrad(g, ["#2E2C3A", "#0E0D14"], 37, 44); g.fill(band);
    line(g, "rgba(255,255,255,0.18)", 37, 38.2, 63, 38.2, 0.3);
    hatEll(g, "#1A1824", 38.4, 39.6, 1.6, 1.1, 0.3); hatEll(g, "#1A1824", 39.8, 40.4, 1.2, 0.8, -0.4);
    const fe = new Path2D(); fe.moveTo(61.6, 39); fe.quadraticCurveTo(63, 32, 67.4, 29.6); fe.quadraticCurveTo(66.4, 35, 61.6, 39); fe.closePath();
    g.fillStyle = vGrad(g, ["#6FB0FF", "#3D8BEA"], 29.6, 39); g.fill(fe); line(g, "rgba(255,255,255,0.6)", 62, 38.4, 66.6, 30.6, 0.3);
  }
  function drawCoco(g, t) {
    // chapéu-coco de banqueiro: cúpula preta bem redonda e lustrosa, fita de gorgorão vinho e aba curta
    // enroladinha nos lados
    hatShadow(g, 19);
    const brim = new Path2D(); brim.moveTo(30.4, 42.6); brim.quadraticCurveTo(50, 38.4, 69.6, 42.6); brim.quadraticCurveTo(71.6, 41.4, 71.4, 43.4); brim.quadraticCurveTo(69.4, 46.6, 50, 47.2); brim.quadraticCurveTo(30.6, 46.6, 28.6, 43.4); brim.quadraticCurveTo(28.4, 41.4, 30.4, 42.6); brim.closePath();
    g.fillStyle = vGrad(g, ["#3A3650", "#121018"], 40, 47.2); g.fill(brim); stroke(g, brim, "rgba(0,0,0,0.5)", 0.3);
    const dome = new Path2D(); dome.moveTo(35.4, 42.2); dome.bezierCurveTo(34.4, 27, 41, 21.4, 50, 21.4); dome.bezierCurveTo(59, 21.4, 65.6, 27, 64.6, 42.2); dome.quadraticCurveTo(50, 44.4, 35.4, 42.2); dome.closePath();
    const rg = g.createRadialGradient(44, 28, 1, 50, 32, 18); rg.addColorStop(0, "#5A5470"); rg.addColorStop(0.5, "#24212F"); rg.addColorStop(1, "#0E0D14"); g.fillStyle = rg; g.fill(dome);
    const sh = new Path2D(); sh.moveTo(39.6, 33); sh.quadraticCurveTo(40, 25.6, 46.6, 23.6); stroke(g, sh, "rgba(255,255,255,0.4)", 0.9);
    const band = new Path2D(); band.moveTo(35.5, 37.4); band.quadraticCurveTo(50, 39.6, 64.5, 37.4); band.lineTo(64.6, 41.6); band.quadraticCurveTo(50, 43.8, 35.4, 41.6); band.closePath();
    g.fillStyle = vGrad(g, ["#7A2232", "#4A1220"], 37.4, 43.8); g.fill(band);
    for (let i = 0; i < 12; i++) line(g, "rgba(0,0,0,0.18)", 37 + i * 2.4, 38.6, 37 + i * 2.4, 42.4, 0.25);
    hatEll(g, "#5A1626", 61, 39.8, 1.8, 1.2, 0.4); hatEll(g, "#5A1626", 62.6, 40.4, 1.4, 0.9, -0.5); circle(g, "#3A0C16", 0.7, 61.8, 40);
    const k = Math.max(0, sin(t * 1.1 + 1)) ** 10; if (k > 0.02) star4(g, 44, 26, 2.6 * k, `rgba(255,255,255,${k})`);
  }
  function drawChapeuLapis(g, t) {
    // chapéu de lápis: borracha rosa na base, ponteira de metal com frisos, corpo amarelo sextavado afinando em
    // cone, a madeira apontada e a ponta de grafite lá no alto
    hatShadow(g, 17);
    const er = new Path2D(); er.moveTo(34.4, 39.4); er.quadraticCurveTo(50, 37, 65.6, 39.4); er.lineTo(65.4, 44.6); er.quadraticCurveTo(50, 42.2, 34.6, 44.6); er.closePath();
    g.fillStyle = vGrad(g, ["#FFB3C8", "#F27A9C", "#D95A80"], 37.4, 44.6); g.fill(er); stroke(g, er, "rgba(120,30,60,0.35)", 0.3);
    line(g, "rgba(255,255,255,0.4)", 37, 40.8, 46, 39.8, 0.5);
    const fer = new Path2D(); fer.moveTo(35.4, 35.2); fer.quadraticCurveTo(50, 33, 64.6, 35.2); fer.lineTo(65.6, 39.6); fer.quadraticCurveTo(50, 37.2, 34.4, 39.6); fer.closePath();
    g.fillStyle = hGrad(g, ["#9AA0B4", "#EEF0F6", "#B4B8C8", "#7E8498"], 34, 66); g.fill(fer); stroke(g, fer, "rgba(40,44,60,0.4)", 0.3);
    for (const y of [36.6, 38]) { const p = new Path2D(); p.moveTo(35, y); p.quadraticCurveTo(50, y - 2.2, 65, y); stroke(g, p, "rgba(40,44,60,0.35)", 0.3); }
    const cone = new Path2D(); cone.moveTo(35.4, 35.4); cone.lineTo(44.4, 19.8); cone.quadraticCurveTo(50, 19, 55.6, 19.8); cone.lineTo(64.6, 35.4); cone.quadraticCurveTo(50, 33.2, 35.4, 35.4); cone.closePath();
    g.fillStyle = hGrad(g, ["#E9A20F", "#FFD84A", "#FFE98A", "#F5B82A", "#C98A12"], 35, 65); g.fill(cone);
    line(g, "rgba(150,90,0,0.3)", 45.4, 34, 48, 19.6, 0.45); line(g, "rgba(150,90,0,0.3)", 54.6, 34, 52, 19.6, 0.45);
    line(g, "rgba(255,255,255,0.45)", 41.6, 32, 46.4, 21.4, 0.6);
    stroke(g, cone, "rgba(150,90,0,0.45)", 0.35);
    const wood = new Path2D(); wood.moveTo(44.4, 20); wood.quadraticCurveTo(50, 19.2, 55.6, 20); wood.lineTo(51.7, 12.4); wood.lineTo(48.3, 12.4); wood.closePath();
    g.fillStyle = vGrad(g, ["#F6D9A8", "#E0B47A"], 12, 20); g.fill(wood);
    const sc = new Path2D(); sc.moveTo(44.4, 20); for (let i = 0; i < 4; i++) sc.quadraticCurveTo(45.8 + i * 2.8, 18.2, 47.2 + i * 2.8, 19.6); stroke(g, sc, "rgba(150,90,0,0.5)", 0.35);
    stroke(g, wood, "rgba(120,80,30,0.45)", 0.3);
    const tip = new Path2D(); tip.moveTo(48.3, 12.6); tip.lineTo(51.7, 12.6); tip.lineTo(50, 8.4); tip.closePath();
    g.fillStyle = vGrad(g, ["#6A6488", "#2A2640"], 8.4, 12.6); g.fill(tip); line(g, "rgba(255,255,255,0.5)", 49.4, 11.6, 49.9, 9.4, 0.3);
    const k = Math.max(0, sin(t * 1.3)) ** 8; if (k > 0.02) star4(g, 50, 8.6, 2.4 * k, `rgba(255,255,255,${k})`);
  }
  function drawHeadset(g, t) {
    // headset de suporte: arco fino por cima, um fone só (na esquerda) com a luz vermelha de "em atendimento",
    // o apoio do outro lado e o microfone de haste com espuma até a boca
    const arc = new Path2D(); arc.moveTo(13, 50); arc.bezierCurveTo(12, 19, 88, 19, 87, 50);
    stroke(g, arc, "#1E1B2E", 2.2); stroke(g, arc, "#55507A", 1);
    const pad = new Path2D(); pad.moveTo(40, 27.4); pad.quadraticCurveTo(50, 24.2, 60, 27.4); stroke(g, pad, "#1E1B2E", 3.4); stroke(g, pad, "#3A3550", 2.4); line(g, "rgba(255,255,255,0.3)", 43, 25.8, 49, 24.8, 0.6);
    const rp = new Path2D(); rp.roundRect(84.6, 47.4, 4.6, 8, 2); g.fillStyle = vGrad(g, ["#4A4466", "#1E1B2E"], 47.4, 55.4); g.fill(rp);
    const cg = g.createRadialGradient(11.4, 52.6, 0.5, 12.6, 54, 6.2); cg.addColorStop(0, "#6A64A0"); cg.addColorStop(1, "#24212F");
    g.beginPath(); g.arc(12.6, 54, 6, 0, 2 * PI); g.fillStyle = cg; g.fill();
    g.beginPath(); g.arc(12.6, 54, 4.4, 0, 2 * PI); g.strokeStyle = "#1E1B2E"; g.lineWidth = 1; g.stroke();
    g.beginPath(); g.arc(12.6, 54, 3, 0, 2 * PI); g.strokeStyle = MINT; g.lineWidth = 0.5; g.stroke();
    line(g, "rgba(255,255,255,0.35)", 9, 51, 10.6, 49.4, 0.7);
    const on = 0.5 + 0.5 * sin(t * 4); circle(g, `rgba(255,90,110,${0.25 * on})`, 1.6, 12.6, 54); circle(g, `rgba(255,90,110,${0.6 + 0.4 * on})`, 0.8, 12.6, 54);
    const boom = new Path2D(); boom.moveTo(15.4, 58); boom.bezierCurveTo(18, 70, 28, 77.4, 39.6, 77.4); stroke(g, boom, "#1E1B2E", 1.3); stroke(g, boom, "rgba(255,255,255,0.18)", 0.35);
    hatEll(g, "#2A2640", 42, 77.4, 2.8, 2.1, 0);
    for (let i = 0; i < 6; i++) circle(g, "rgba(255,255,255,0.18)", 0.25, 40.6 + (i % 3) * 1.3, 76.6 + Math.floor(i / 3) * 1.4);
  }
  function drawClipe(g, t) {
    // clipe gigante preso no alto do livro: arame de metal com as voltas do clipe, a ponta passando pela frente
    // da página e um brilho que corre pelo metal
    g.save(); g.translate(58, 46); g.rotate(0.16 + sin(t * 1.4) * 0.02);
    const p = new Path2D();
    p.moveTo(1.6, 3); p.lineTo(1.6, -18); p.arc(0, -18, 1.6, 0, PI, true); p.lineTo(-1.6, 4); p.arc(0.6, 4, 2.2, PI, 0, true);
    p.lineTo(2.8, -22); p.arc(-0.4, -22, 3.2, 0, PI, true); p.lineTo(-3.6, 1);
    stroke(g, p, "#4E5468", 1.9); stroke(g, p, "#B4BACA", 1.3); stroke(g, p, "rgba(255,255,255,0.75)", 0.4);
    const k = Math.max(0, sin(t * 1.5)) ** 8; if (k > 0.02) star4(g, -1.6, -24.4, 3 * k, `rgba(255,255,255,${k})`);
    const u = (t * 0.35) % 1; circle(g, `rgba(255,255,255,${0.8 * sin(PI * u)})`, 0.6, 2.8, -2 - u * 18);
    g.restore();
  }
  function drawGorro(g, t, tilt, vy) {
    // gorro de lã: copa vermelha de tricô com listras brancas, barra dobrada canelada e o pompom que balança
    hatShadow(g, 19);
    const sw = sin(t * 1.8) * 1 - tilt * 0.4 + clamp(vy * 0.04, -2, 2);
    const dome = new Path2D(); dome.moveTo(33.4, 41); dome.bezierCurveTo(32, 27, 40, 20, 50 + sw * 0.3, 19.6); dome.bezierCurveTo(60, 20, 68, 27, 66.6, 41); dome.closePath();
    g.fillStyle = "#E0475E"; g.fill(dome);
    g.save(); g.clip(dome);
    for (const y of [24.4, 31.4]) { g.fillStyle = "#FFFDF8"; g.fillRect(28, y, 44, 3.2); }
    for (let r = 0; r < 9; r++) for (let c = 0; c < 12; c++) { const x = 33 + c * 3 + (r % 2) * 1.5, y = 21 + r * 2.4; const v = new Path2D(); v.moveTo(x - 0.8, y - 0.5); v.lineTo(x, y + 0.5); v.lineTo(x + 0.8, y - 0.5); stroke(g, v, "rgba(80,10,30,0.18)", 0.25); }
    g.fillStyle = vGrad(g, ["rgba(255,255,255,0.18)", "rgba(60,0,20,0.25)"], 20, 41); g.fillRect(28, 18, 44, 24);
    g.restore();
    stroke(g, dome, "rgba(90,10,30,0.45)", 0.4);
    const cuff = new Path2D(); cuff.moveTo(32.4, 38.4); cuff.quadraticCurveTo(50, 35.4, 67.6, 38.4); cuff.lineTo(67.2, 45); cuff.quadraticCurveTo(50, 42, 32.8, 45); cuff.closePath();
    g.fillStyle = vGrad(g, ["#F26A80", "#B8304A"], 36, 45); g.fill(cuff);
    g.save(); g.clip(cuff); for (let x = 33.4; x < 67; x += 2.2) line(g, "rgba(90,10,30,0.3)", x, 34, x, 46, 0.45); g.restore();
    stroke(g, cuff, "rgba(90,10,30,0.45)", 0.35);
    const px = 50 + sw, py = 17.4;
    for (let i = 0; i < 8; i++) { const a = i * PI / 4 + 0.3; circle(g, "#EFE6EC", 1.6, px + cos(a) * 3.2, py + sin(a) * 3.2); }
    const pom = g.createRadialGradient(px - 1.2, py - 1.4, 0.4, px, py, 4); pom.addColorStop(0, "#FFFFFF"); pom.addColorStop(1, "#E6DCE4");
    g.beginPath(); g.arc(px, py, 3.6, 0, 2 * PI); g.fillStyle = pom; g.fill();
  }
  function drawOrelhasGato(g, t) {
    // tiara de orelhinhas de gato: arco fino preto e duas orelhas com o miolo rosa; a da direita mexe de vez em quando
    const tw = Math.max(0, sin(t * 1.1)) ** 16 * 0.3;
    const band = new Path2D(); band.moveTo(30.6, 47); band.quadraticCurveTo(50, 33.4, 69.4, 47);
    stroke(g, band, "#1E1B2E", 2); stroke(g, band, "#3A3550", 0.9);
    for (const s of [-1, 1]) {
      g.save(); g.translate(50 + s * 10.4, 40.8); g.rotate(s * 0.3 + (s > 0 ? tw : 0));
      const ear = new Path2D(); ear.moveTo(-5.2, 1.4); ear.quadraticCurveTo(-4.4, -5.4, -0.6, -9.4); ear.quadraticCurveTo(0.3, -10.2, 1.2, -9); ear.quadraticCurveTo(4.6, -4.6, 5.2, 1.4); ear.quadraticCurveTo(0, 2.4, -5.2, 1.4); ear.closePath();
      g.fillStyle = vGrad(g, ["#4A4466", "#1E1B2E"], -10, 2); g.fill(ear); stroke(g, ear, "#14111F", 0.4);
      const inn = new Path2D(); inn.moveTo(-3, 0.6); inn.quadraticCurveTo(-2.4, -4.4, 0, -7); inn.quadraticCurveTo(2.6, -3.8, 3, 0.6); inn.quadraticCurveTo(0, 1.2, -3, 0.6); inn.closePath();
      g.fillStyle = vGrad(g, ["#FFB3C8", "#F27A9C"], -7, 1); g.fill(inn);
      line(g, "rgba(255,255,255,0.3)", -3.6, -1, -1.2, -7.4, 0.4);
      g.restore();
    }
  }
  function drawTiaraFlores(g, t) {
    // tiara de flores: ramo verde em arco no alto do livro, folhinhas e florzinhas de cinco pétalas coloridas
    const vine = new Path2D(); vine.moveTo(31, 45); vine.quadraticCurveTo(50, 34.6, 69, 45);
    stroke(g, vine, "#2E7D3A", 1.3); stroke(g, vine, "#7ACB7E", 0.4);
    for (let i = 0; i < 8; i++) { const u = 0.06 + i * 0.125, [x, y] = hatQ(u, 31, 45, 50, 34.6, 69, 45); hatEll(g, i % 2 ? "#4FAF5E" : "#3E8E4E", x + (i % 2 ? 1.2 : -1.2), y - 0.8, 1.8, 0.8, i % 2 ? -0.7 : 0.7); }
    const cols = ["#FF8FA3", "#FFD84A", "#B995F5", "#6FB0FF", "#FF9F5A", "#FF8FC4", "#7EE0B8"];
    cols.forEach((c, i) => {
      const u = 0.04 + i * 0.153, [x0, y0] = hatQ(u, 31, 45, 50, 34.6, 69, 45), r = i === 3 ? 3 : 2.4, cy = y0 - 0.6 + sin(t * 2 + i) * 0.35;
      for (let k = 0; k < 5; k++) { const a = k * 2 * PI / 5 + i; hatEll(g, c, x0 + cos(a) * r * 0.6, cy + sin(a) * r * 0.6, r * 0.55, r * 0.4, a); }
      circle(g, "rgba(0,0,0,0.12)", r * 0.4, x0 + 0.2, cy + 0.3); circle(g, i === 1 ? "#F5A623" : "#FFE07A", r * 0.36, x0, cy); circle(g, "rgba(255,255,255,0.7)", r * 0.13, x0 - r * 0.12, cy - r * 0.14);
    });
  }
  function drawBandana(g, t) {
    // bandana amarrada: tecido vermelho de bolinhas brancas cobrindo o alto, nó do lado direito e as pontas
    // soltas balançando
    hatShadow(g, 18);
    const cap = new Path2D(); cap.moveTo(33.4, 44.4); cap.bezierCurveTo(32.4, 30, 40.4, 23, 50, 23); cap.bezierCurveTo(59.6, 23, 67.6, 30, 66.6, 44.4); cap.quadraticCurveTo(50, 40.6, 33.4, 44.4); cap.closePath();
    g.fillStyle = vGrad(g, ["#F0566E", "#D23A55", "#9C1F36"], 23, 44); g.fill(cap);
    g.save(); g.clip(cap);
    for (let r = 0; r < 5; r++) for (let c = 0; c < 8; c++) circle(g, "rgba(255,255,255,0.92)", 0.85, 34 + c * 4.6 + (r % 2) * 2.3, 25.6 + r * 4.2);
    for (const [x0, x1] of [[40, 44], [58, 55]]) { const p = new Path2D(); p.moveTo(x0, 25); p.quadraticCurveTo((x0 + x1) / 2 + 1.6, 33, x1, 42); stroke(g, p, "rgba(90,10,30,0.3)", 0.6); }
    hatGloss(g, 42, 28, 11, 0.3);
    g.restore();
    stroke(g, cap, "rgba(90,10,30,0.5)", 0.4);
    g.save(); g.setLineDash([0.8, 0.6]); const hem = new Path2D(); hem.moveTo(34.4, 42.6); hem.quadraticCurveTo(50, 39.2, 65.6, 42.6); stroke(g, hem, "rgba(255,255,255,0.55)", 0.25); g.restore();
    const sw = sin(t * 2.4) * 1.1;
    for (const [len, ang, d] of [[8.4, 0.5, 0], [7, 1.1, 1]]) {
      const ex = 66.4 + Math.sin(ang) * len + sw * (1 - d * 0.4), ey = 41 + Math.cos(ang) * len;
      const tl = new Path2D(); tl.moveTo(65.6, 39.6); tl.quadraticCurveTo(69 + sw * 0.4, 41 + d, ex + 1.2, ey - 1); tl.lineTo(ex - 1, ey + 0.6); tl.quadraticCurveTo(67 + sw * 0.3, 43.4, 65.8, 42.4); tl.closePath();
      g.fillStyle = vGrad(g, ["#E0475E", "#9C1F36"], 39, ey + 1); g.fill(tl); stroke(g, tl, "rgba(90,10,30,0.45)", 0.3);
      circle(g, "rgba(255,255,255,0.9)", 0.6, (65.8 + ex) / 2 + 0.6, (41 + ey) / 2);
    }
    hatEll(g, "#C23250", 66.4, 41, 2.4, 2, 0.3); circle(g, "rgba(255,255,255,0.9)", 0.6, 66, 40.4); line(g, "rgba(90,10,30,0.4)", 65.2, 42.2, 67.6, 40, 0.35);
  }
  function drawChef(g, t) {
    // chapéu de chef: faixa branca pregueada e a copa alta e fofa, feita de gomos estufados (com um leve respirar)
    hatShadow(g, 18);
    const br = 1 + sin(t * 1.6) * 0.015;
    g.save(); g.translate(50, 38); g.scale(br, 2 - br); g.translate(-50, -38);
    const puffs = [[40.4, 27, 7.4], [50, 21.6, 8.6], [59.6, 27, 7.4], [44.4, 32.4, 7], [55.6, 32.4, 7]];
    const top = new Path2D(); for (const [x, y, r] of puffs) { top.moveTo(x + r, y); top.arc(x, y, r, 0, 2 * PI); } top.rect(36, 28, 28, 11);
    stroke(g, top, "rgba(60,56,90,0.4)", 0.9);
    g.fillStyle = vGrad(g, ["#FFFFFF", "#F4F2FA", "#D9D5E8"], 13, 40); g.fill(top);
    for (const [x0, y0, cx, cy, x1, y1] of [[45, 18, 44, 24, 46.6, 29], [55, 18, 56, 24, 53.4, 29], [50, 28, 49.4, 32, 50, 37]]) { const p = new Path2D(); p.moveTo(x0, y0); p.quadraticCurveTo(cx, cy, x1, y1); stroke(g, p, "rgba(140,134,170,0.4)", 0.5); }
    hatGloss(g, 46, 19, 6, 0.8);
    g.restore();
    const band = new Path2D(); band.moveTo(35, 37.2); band.quadraticCurveTo(50, 34.8, 65, 37.2); band.lineTo(65, 44.4); band.quadraticCurveTo(50, 41.8, 35, 44.4); band.closePath();
    g.fillStyle = vGrad(g, ["#FFFFFF", "#E4E0F2"], 36, 44.4); g.fill(band); stroke(g, band, "rgba(60,56,90,0.35)", 0.35);
    for (let x = 38; x < 64; x += 3) line(g, "rgba(120,114,160,0.25)", x, 37.6 - (x - 50) * (x - 50) * 0.008, x, 43.4 - (x - 50) * (x - 50) * 0.008, 0.4);
  }
  function drawPraia(g, t) {
    // chapéu de praia de palha: aba bem larga ondulando, trama trançada, fita coral com laço e as pontas ao vento
    hatShadow(g, 26);
    const wv = sin(t * 1.4) * 0.6;
    const brim = new Path2D(); brim.moveTo(20, 43.6 - wv); brim.quadraticCurveTo(22, 38.6, 50, 38.4); brim.quadraticCurveTo(78, 38.6, 80, 43.6 + wv); brim.quadraticCurveTo(78, 49.4, 50, 49.6); brim.quadraticCurveTo(22, 49.4, 20, 43.6 - wv); brim.closePath();
    const weave = (from, to) => { for (let x = from; x < to; x += 1.8) { line(g, "rgba(140,100,40,0.22)", x, 20, x + 12, 52, 0.35); line(g, "rgba(140,100,40,0.22)", x + 12, 20, x, 52, 0.35); } };
    g.fillStyle = vGrad(g, ["#F6E2A8", "#E0C27A", "#B8975A"], 38, 49.6); g.fill(brim);
    g.save(); g.clip(brim); weave(10, 90); g.restore();
    stroke(g, brim, "rgba(120,80,30,0.5)", 0.4);
    const dome = new Path2D(); dome.moveTo(37, 42.6); dome.bezierCurveTo(36.2, 29, 42, 24.4, 50, 24.4); dome.bezierCurveTo(58, 24.4, 63.8, 29, 63, 42.6); dome.quadraticCurveTo(50, 44.6, 37, 42.6); dome.closePath();
    g.fillStyle = vGrad(g, ["#F8E8B8", "#E2C47E", "#B89656"], 24.4, 43); g.fill(dome);
    g.save(); g.clip(dome); weave(26, 66); hatGloss(g, 44, 29, 10, 0.35); g.restore();
    stroke(g, dome, "rgba(120,80,30,0.45)", 0.35);
    const band = new Path2D(); band.moveTo(36.8, 37.6); band.quadraticCurveTo(50, 39.8, 63.2, 37.6); band.lineTo(63.1, 41.4); band.quadraticCurveTo(50, 43.6, 36.9, 41.4); band.closePath();
    g.fillStyle = vGrad(g, ["#FF8FA3", "#F0566E"], 37.6, 43.6); g.fill(band);
    const ws = new Path2D(); ws.moveTo(37, 39.6); ws.quadraticCurveTo(50, 41.8, 63, 39.6); stroke(g, ws, "rgba(255,255,255,0.75)", 0.45);
    const sw = sin(t * 2.6) * 1.2;
    for (const [ex, ey] of [[31 + sw, 47.6], [34.4 + sw * 0.7, 49.4]]) { const tl = new Path2D(); tl.moveTo(38.4, 40.4); tl.quadraticCurveTo(35, 42, ex, ey); stroke(g, tl, "#F0566E", 1.3); stroke(g, tl, "rgba(255,255,255,0.4)", 0.3); }
    for (const s of [-1, 1]) hatEll(g, "#FF7A90", 38.4 + s * 1.8, 39.8, 2, 1.2, s * 0.4);
    circle(g, "#D63A5E", 0.9, 38.4, 40);
  }
  function drawPirata(g, t) {
    // tricórnio de pirata: copa preta, aba dobrada para cima com as pontas, debrum dourado e a caveirinha fofa
    hatShadow(g, 22);
    const crown = new Path2D(); crown.moveTo(37, 40); crown.bezierCurveTo(37, 28, 43, 24, 50, 24); crown.bezierCurveTo(57, 24, 63, 28, 63, 40); crown.closePath();
    g.fillStyle = vGrad(g, ["#3A3650", "#121018"], 24, 40); g.fill(crown);
    const brim = new Path2D(); brim.moveTo(24.4, 30.6); brim.quadraticCurveTo(36, 36.4, 50, 31.4); brim.quadraticCurveTo(64, 36.4, 75.6, 30.6); brim.quadraticCurveTo(72, 41.4, 66, 44.6); brim.quadraticCurveTo(50, 41.6, 34, 44.6); brim.quadraticCurveTo(28, 41.4, 24.4, 30.6); brim.closePath();
    g.fillStyle = vGrad(g, ["#3A3650", "#1A1726", "#0E0D14"], 30, 45); g.fill(brim); stroke(g, brim, "rgba(0,0,0,0.55)", 0.4);
    const trim = new Path2D(); trim.moveTo(24.4, 30.6); trim.quadraticCurveTo(36, 36.4, 50, 31.4); trim.quadraticCurveTo(64, 36.4, 75.6, 30.6);
    stroke(g, trim, "#B57A0C", 1.2); stroke(g, trim, "#FFD877", 0.5);
    for (const x of [24.4, 75.6]) circle(g, GOLD, 0.9, x, 30.6);
    const sx = 50, sy = 37.4;
    for (const s of [-1, 1]) { line(g, "#E8E2D4", sx - 4.4 * s, sy - 3.4, sx + 4.4 * s, sy + 3.4, 1.1); for (const e of [-1, 1]) { circle(g, "#FFFDF4", 0.8, sx + e * 4.6 * s - 0.4 * s * e, sy + e * 3.6 - 0.5); circle(g, "#FFFDF4", 0.8, sx + e * 4.6 * s + 0.4 * s * e, sy + e * 3.6 + 0.5); } }
    const sk = new Path2D(); sk.arc(sx, sy - 0.6, 3.4, PI * 0.82, PI * 2.18); sk.lineTo(sx + 2, sy + 3); sk.lineTo(sx - 2, sy + 3); sk.closePath();
    g.fillStyle = vGrad(g, ["#FFFFFF", "#E4DFD0"], sy - 4, sy + 3); g.fill(sk);
    circle(g, "#14111F", 1, sx - 1.3, sy - 0.6); circle(g, "#14111F", 1, sx + 1.3, sy - 0.6);
    circle(g, "rgba(255,255,255,0.8)", 0.3, sx - 1.6, sy - 0.9); circle(g, "rgba(255,255,255,0.8)", 0.3, sx + 1, sy - 0.9);
    circle(g, "rgba(244,166,192,0.7)", 0.6, sx - 2.4, sy + 0.8); circle(g, "rgba(244,166,192,0.7)", 0.6, sx + 2.4, sy + 0.8);
    for (const x of [-1, 0, 1]) line(g, "rgba(20,17,31,0.5)", sx + x * 0.8, sy + 1.8, sx + x * 0.8, sy + 2.8, 0.25);
    const k = Math.max(0, sin(t * 1.2 + 0.4)) ** 10; if (k > 0.02) star4(g, 66, 34.4, 2.6 * k, `rgba(255,255,255,${k})`);
  }
  function drawViking(g, t) {
    // capacete viking: dois chifres de marfim curvados para os lados, casco de metal polido, faixa de bronze em
    // cruz com rebites
    hatShadow(g, 19);
    for (const s of [-1, 1]) {
      const h = new Path2D(); h.moveTo(50 + s * 13.4, 33.4); h.bezierCurveTo(50 + s * 20, 33, 50 + s * 24.6, 28, 50 + s * 24.6, 18.6); h.bezierCurveTo(50 + s * 22.6, 25, 50 + s * 19, 28.6, 50 + s * 14.6, 38.4); h.closePath();
      g.fillStyle = vGrad(g, ["#FFFDF4", "#EDE0C0", "#C9B48A"], 18, 38); g.fill(h); stroke(g, h, "rgba(110,80,30,0.5)", 0.35);
      for (let i = 0; i < 3; i++) line(g, "rgba(140,100,40,0.35)", 50 + s * (16.4 + i * 2.2), 37 - i * 1.6, 50 + s * (17.4 + i * 2.4), 32.8 - i * 2, 0.35);
      line(g, "rgba(255,255,255,0.7)", 50 + s * 16, 33.8, 50 + s * 22.6, 24, 0.4);
    }
    const dome = new Path2D(); dome.moveTo(33.4, 43.6); dome.bezierCurveTo(32.4, 28.6, 40.6, 22, 50, 22); dome.bezierCurveTo(59.4, 22, 67.6, 28.6, 66.6, 43.6); dome.quadraticCurveTo(50, 40.6, 33.4, 43.6); dome.closePath();
    g.fillStyle = hGrad(g, ["#7E8498", "#D8DCE6", "#A4AABB", "#6A7084"], 33, 67); g.fill(dome);
    g.save(); g.clip(dome);
    hatGloss(g, 43, 27, 10, 0.5);
    g.fillStyle = hGrad(g, ["#B57A0C", "#F5C451", "#B57A0C"], 47.8, 52.2); g.fillRect(47.8, 20, 4.4, 24);
    g.restore();
    stroke(g, dome, "rgba(40,44,60,0.5)", 0.4);
    const rim = new Path2D(); rim.moveTo(33, 40.2); rim.quadraticCurveTo(50, 37.2, 67, 40.2); rim.lineTo(66.7, 44.2); rim.quadraticCurveTo(50, 41.2, 33.3, 44.2); rim.closePath();
    g.fillStyle = vGrad(g, ["#F5C451", "#D99A1B", "#9C6A12"], 37.6, 44.2); g.fill(rim); stroke(g, rim, "rgba(110,70,0,0.55)", 0.3);
    for (let i = 0; i < 7; i++) { const [x, y] = hatQ(0.08 + i * 0.14, 33.2, 42.2, 50, 39.2, 66.8, 42.2); circle(g, "#9C6A12", 0.65, x, y); circle(g, "#FFF1BF", 0.3, x - 0.2, y - 0.2); }
    for (const y of [26, 31, 36]) { circle(g, "#9C6A12", 0.6, 50, y); circle(g, "#FFF1BF", 0.25, 49.8, y - 0.2); }
    const k = Math.max(0, sin(t * 1.3 + 2)) ** 10; if (k > 0.02) star4(g, 41.6, 27.4, 2.8 * k, `rgba(255,255,255,${k})`);
  }
  function drawAstronauta(g, t) {
    // capacete de astronauta (lendário): bolha de vidro em volta do alto do livro, com reflexo curvo, estrelinhas
    // cintilando no vidro, e a gola branca com as luzinhas piscando
    const bub = new Path2D(); bub.moveTo(12.6, 47); bub.bezierCurveTo(9, 4, 91, 4, 87.4, 47); bub.closePath();
    const gl = g.createRadialGradient(44, 26, 2, 50, 34, 42); gl.addColorStop(0, "rgba(220,240,255,0.08)"); gl.addColorStop(1, "rgba(120,180,240,0.26)");
    g.fillStyle = gl; g.fill(bub);
    stroke(g, bub, "rgba(40,60,110,0.35)", 1); stroke(g, bub, "rgba(220,240,255,0.85)", 0.5);
    const rf = new Path2D(); rf.moveTo(19.4, 38); rf.bezierCurveTo(18.4, 25, 27, 17, 38, 14.6); stroke(g, rf, "rgba(255,255,255,0.55)", 2.2);
    const rf2 = new Path2D(); rf2.moveTo(24, 38.6); rf2.bezierCurveTo(23.6, 31, 26.6, 26, 30.4, 23.2); stroke(g, rf2, "rgba(255,255,255,0.35)", 0.7);
    line(g, "rgba(255,255,255,0.45)", 77, 22, 80.4, 27, 0.9);
    for (let i = 0; i < 5; i++) { const k = 0.5 + 0.5 * sin(t * 2.6 + i * 1.7); const [x, y] = [[28, 30], [72, 34], [64, 20], [36, 22], [81, 40]][i]; star4(g, x, y, 1.4 * k, `rgba(255,255,255,${0.85 * k})`); }
    const u = (t * 0.2) % 1, [gx, gy] = hatQ(u, 14, 40, 50, 0, 86, 40); circle(g, `rgba(255,255,255,${0.7 * sin(PI * u)})`, 0.9, gx, gy);
    const col = new Path2D(); col.moveTo(11, 45); col.quadraticCurveTo(50, 39.4, 89, 45); col.lineTo(89, 50); col.quadraticCurveTo(50, 44.4, 11, 50); col.closePath();
    g.fillStyle = vGrad(g, ["#FFFFFF", "#DCD8EA", "#A8A4BE"], 41, 50); g.fill(col); stroke(g, col, "rgba(40,36,70,0.45)", 0.4);
    const gr = new Path2D(); gr.moveTo(11.4, 45.6); gr.quadraticCurveTo(50, 40, 88.6, 45.6); stroke(g, gr, GOLD, 0.6);
    for (const [x, c, ph] of [[70, "255,90,110", 0], [74.6, "126,224,184", 1.4], [79.2, "111,176,255", 2.8]]) { const k = 0.5 + 0.5 * sin(t * 4 + ph); const y = 46.6 - (x - 50) * (x - 50) * 0.0004 + 0.6; circle(g, `rgba(${c},${0.3 * k})`, 1.4, x, y); circle(g, `rgba(${c},${0.6 + 0.4 * k})`, 0.65, x, y); }
  }
  function drawHat(g, s, look, tilt) {
    const { t, small } = s, vy = s.vy, hat = look.hat;
    if (hat === "bone") drawBone(g);
    else if (hat === "boina") drawBoina(g);
    else if (hat === "boina-militar") drawBoinaMilitar(g, t);
    else if (hat === "touca") drawTouca(g, t);
    else if (hat === "quepe") drawQuepe(g, t);
    else if (hat === "viseira") drawViseira(g, t);
    else if (hat === "explorador") drawExplorador(g);
    else if (hat === "fones") drawFones(g, t);
    else if (hat === "cartola") drawCartola(g);
    else if (hat === "coroa") drawCoroa(g, t);
    else if (hat === "mago") drawMago(g, t, tilt, vy);
    else if (hat === "aureola") drawAureola(g, t);
    else if (hat === "espelho-frontal") drawEspelhoFrontal(g, t);
    else if (hat === "capacete") drawCapacete(g, t);
    else if (hat === "bone-tatico") drawBoneTatico(g, t);
    else if (hat === "barrete") drawBarrete(g, t);
    else if (hat === "fedora") drawFedora(g, t);
    else if (hat === "coco") drawCoco(g, t);
    else if (hat === "chapeu-lapis") drawChapeuLapis(g, t);
    else if (hat === "headset") drawHeadset(g, t);
    else if (hat === "clipe") drawClipe(g, t);
    else if (hat === "gorro") drawGorro(g, t, tilt, vy);
    else if (hat === "orelhas-gato") drawOrelhasGato(g, t);
    else if (hat === "tiara-flores") drawTiaraFlores(g, t);
    else if (hat === "bandana") drawBandana(g, t);
    else if (hat === "chef") drawChef(g, t);
    else if (hat === "praia") drawPraia(g, t);
    else if (hat === "pirata") drawPirata(g, t);
    else if (hat === "viking") drawViking(g, t);
    else if (hat === "astronauta") drawAstronauta(g, t);
    else {
      drawCap(g, t, tilt, vy, hat === "capelo-ouro");
      if (hat === "louros") drawLaurels(g, small);
      if (look.streak >= 7 && !small) drawStreakMedal(g, t, look.streak, tilt);
    }
  }

  // ---- óculos (seguem o rosto, um pouco menos que os olhos)
  function drawEyewear(g, P, small, kind, t) {
    if (!kind || kind === "nenhum") return;
    const dx = P.lookX * 0.7, dy = P.lookY * 0.5, w = small ? 1.5 : 0.95;
    const L = [31 + dx, 61 + dy], R = [69 + dx, 61 + dy];
    const bridge = (color, lw, y0 = 60, y1 = 56.8) => { const b = new Path2D(); b.moveTo(38 + dx, y0 + dy); b.quadraticCurveTo(50 + dx, y1 + dy, 62 + dx, y0 + dy); stroke(g, b, color, lw); };
    const arms = (color, lw) => { line(g, color, 24 + dx, 59.5 + dy, 18.5, 58, lw); line(g, color, 76 + dx, 59.5 + dy, 81.5, 58, lw); };
    const glare = (x, y, rx, ry) => { if (small) return; g.beginPath(); g.ellipse(x, y, rx, ry, 0, PI * 1.08, PI * 1.42); g.strokeStyle = "rgba(255,255,255,0.8)"; g.lineWidth = 0.55; g.lineCap = "round"; g.stroke(); };
    if (kind === "leitura") {
      // armação redonda de tartaruga, lente levemente azulada e plaquetas no nariz
      for (const [x, y] of [L, R]) {
        g.beginPath(); g.ellipse(x, y, 7.2, 7, 0, 0, 2 * PI); g.fillStyle = "rgba(200,230,255,0.16)"; g.fill();
        g.strokeStyle = vGrad(g, ["#C98A4B", "#8A5A2B", "#B87A3F"], y - 7, y + 7); g.lineWidth = small ? 1.8 : 1.4; g.stroke();
        g.beginPath(); g.ellipse(x, y, 6.5, 6.3, 0, PI * 1.1, PI * 1.5); g.strokeStyle = "rgba(255,226,180,0.7)"; g.lineWidth = 0.35; g.stroke();
        glare(x, y, 5.6, 5.4);
      }
      bridge("#8A5A2B", small ? 1.6 : 1.2, 59.6, 57); arms("#8A5A2B", small ? 1.6 : 1.1);
      if (!small) for (const x of [44.6, 55.4]) circle(g, "rgba(255,255,255,0.6)", 0.6, x + dx, 63.4 + dy);
    } else if (kind === "escuros") {
      // óculos escuros de armação grossa: lente em degradê com reflexo; as sobrancelhas continuam falando
      const lw = small ? 2 : 1.5;
      for (const [x, y] of [L, R]) {
        const p = new Path2D();
        p.moveTo(x - 8, y - 5.6); p.lineTo(x + 8, y - 5.6); p.quadraticCurveTo(x + 8.8, y - 5.6, x + 8.5, y - 4.4); p.lineTo(x + 7.2, y + 3.6);
        p.quadraticCurveTo(x + 6.7, y + 6, x + 4.4, y + 6); p.lineTo(x - 4.4, y + 6); p.quadraticCurveTo(x - 6.7, y + 6, x - 7.2, y + 3.6); p.lineTo(x - 8.5, y - 4.4); p.quadraticCurveTo(x - 8.8, y - 5.6, x - 8, y - 5.6); p.closePath();
        g.fillStyle = vGrad(g, ["#463F72", "#221D3A", "#120F1F"], y - 6, y + 6); g.fill(p);
        if (!small) { g.save(); g.clip(p); line(g, "rgba(255,255,255,0.28)", x - 7, y + 7, x + 1, y - 7, 3); line(g, "rgba(255,255,255,0.18)", x - 1, y + 7, x + 4, y - 7, 1); g.restore(); }
        stroke(g, p, "#14111F", lw); line(g, "#14111F", x - 8.2, y - 5.2, x + 8.2, y - 5.2, lw * 1.5);
      }
      bridge("#14111F", lw * 1.2, 56.4, 55.2); arms("#14111F", lw);
    } else if (kind === "nerd") {
      // armação grossa de acetato com brilho e um esparadrapo no meio
      const lw = small ? 2.2 : 1.8;
      for (const [x, y] of [L, R]) {
        const p = new Path2D(); p.roundRect(x - 7.4, y - 5.8, 14.8, 12, 3.2);
        g.fillStyle = "rgba(200,230,255,0.14)"; g.fill(p); stroke(g, p, "#1E1B2E", lw);
        if (!small) { const top = new Path2D(); top.moveTo(x - 6, y - 5.2); top.quadraticCurveTo(x, y - 6.2, x + 6, y - 5.2); stroke(g, top, "rgba(255,255,255,0.4)", 0.4); }
        glare(x, y, 5.2, 4.4);
      }
      bridge("#1E1B2E", lw, 59.2, 58.6); arms("#1E1B2E", lw);
      if (!small) { const tp = new Path2D(); tp.roundRect(48 + dx, 57.4 + dy, 4, 3, 0.5); g.fillStyle = "#FFFDF8"; g.fill(tp); stroke(g, tp, "rgba(0,0,0,0.18)", 0.2); line(g, "rgba(0,0,0,0.15)", 49 + dx, 57.6 + dy, 49 + dx, 60.2 + dy, 0.2); line(g, "rgba(0,0,0,0.15)", 51 + dx, 57.6 + dy, 51 + dx, 60.2 + dy, 0.2); }
    } else if (kind === "estrela") {
      // óculos de festa em estrela: armação rosa em degradê, lente cintilante e brilhinhos
      const lw = small ? 1.8 : 1.3;
      for (const [x, y] of [L, R]) {
        const p = star5(g, x, y, 8.8);
        g.fillStyle = "rgba(255,180,220,0.25)"; g.fill(p);
        g.strokeStyle = vGrad(g, ["#FF8FC4", "#F0559A", "#C2357A"], y - 9, y + 9); g.lineWidth = lw; g.lineJoin = "round"; g.stroke(p);
        if (!small) for (let i = 0; i < 4; i++) { const a = t * 0.8 + i * 1.6; circle(g, `rgba(255,255,255,${0.5 + 0.5 * sin(t * 4 + i)})`, 0.35, x + cos(a) * 4, y + sin(a) * 3.4); }
      }
      bridge("#F0559A", lw, 59, 57.5); arms("#F0559A", lw);
      if (!small) { const k = 0.5 + 0.5 * sin(t * 3); star4(g, R[0] + 7, R[1] - 7.5, 2 * k, `rgba(255,255,255,${0.9 * k})`); star4(g, L[0] - 7, L[1] + 5, 1.4 * (1 - k), `rgba(255,255,255,${0.9 * (1 - k)})`); }
    } else if (kind === "coracao") {
      // óculos de coração: armação vermelha em degradê com lente rosada
      const lw = small ? 1.8 : 1.3;
      for (const [x, y] of [L, R]) {
        const p = heart(x, y + 0.6, 7); g.fillStyle = "rgba(255,90,130,0.30)"; g.fill(p);
        g.strokeStyle = vGrad(g, ["#FF7A95", "#D63A5E", "#A8233A"], y - 7, y + 7); g.lineWidth = lw; g.lineJoin = "round"; g.stroke(p);
        if (!small) { const gl = new Path2D(); gl.moveTo(x - 4.4, y - 1.4); gl.quadraticCurveTo(x - 4, y - 4, x - 1.6, y - 3.4); stroke(g, gl, "rgba(255,255,255,0.75)", 0.5); }
      }
      bridge("#D63A5E", lw, 58.4, 56.6); arms("#D63A5E", lw);
    } else if (kind === "aviador") {
      // óculos de aviador: lentes em gota, espelhadas (verde no alto, dourado embaixo), aro fino de metal
      // e ponte dupla; o reflexo corre de leve
      const lw = small ? 1.4 : 0.85;
      for (const [x, y, s] of [[L[0], L[1], -1], [R[0], R[1], 1]]) {
        const p = new Path2D();
        p.moveTo(x - s * 6.8, y - 5.2); p.quadraticCurveTo(x, y - 6.6, x + s * 7.8, y - 4.8); p.bezierCurveTo(x + s * 9, y + 1, x + s * 6, y + 7.2, x + s * 1.2, y + 7.2);
        p.bezierCurveTo(x - s * 4.6, y + 7.2, x - s * 7.6, y + 1, x - s * 6.8, y - 5.2); p.closePath();
        g.fillStyle = vGrad(g, ["#2F5A4A", "#3E4A3A", "#8A6A2A", "#D9A441"], y - 6, y + 7.2); g.fill(p);
        if (!small) { g.save(); g.clip(p); const k = (t * 0.25) % 1; line(g, "rgba(255,255,255,0.32)", x - 9 + k * 6, y + 8, x - 1 + k * 6, y - 8, 2.6); line(g, "rgba(255,255,255,0.2)", x - 3 + k * 6, y + 8, x + 2 + k * 6, y - 8, 0.9); g.restore(); }
        g.strokeStyle = vGrad(g, ["#FFF1BF", "#C9A040", "#8A6A22"], y - 6, y + 7); g.lineWidth = lw; g.lineJoin = "round"; g.stroke(p);
      }
      bridge("#B8902E", lw, 56.6, 55.8); bridge("#B8902E", lw * 0.8, 59.6, 59.2); arms("#B8902E", lw);
    } else if (kind === "meia-lua") {
      // óculos meia-lua de juiz: só a metade de baixo, mais embaixo no rosto, aro dourado fino; os olhos
      // espiam por cima da armação
      const lw = small ? 1.4 : 0.8, yo = 3.2;
      for (const [x, y] of [L, R]) {
        const p = new Path2D(); p.moveTo(x - 6.6, y + yo - 0.6); p.lineTo(x + 6.6, y + yo - 0.6); p.bezierCurveTo(x + 6.6, y + yo + 4.6, x + 3.6, y + yo + 6, x, y + yo + 6); p.bezierCurveTo(x - 3.6, y + yo + 6, x - 6.6, y + yo + 4.6, x - 6.6, y + yo - 0.6); p.closePath();
        g.fillStyle = "rgba(210,235,255,0.2)"; g.fill(p);
        g.strokeStyle = vGrad(g, ["#FFF1BF", "#E9AE34", "#9C6A12"], y + yo, y + yo + 6); g.lineWidth = lw; g.lineJoin = "round"; g.stroke(p);
        if (!small) { g.beginPath(); g.ellipse(x - 1.6, y + yo + 3, 3.4, 1.8, 0, PI * 1.05, PI * 1.45); g.strokeStyle = "rgba(255,255,255,0.8)"; g.lineWidth = 0.45; g.stroke(); }
      }
      bridge("#C98A12", lw, 63.4, 62.2);
      line(g, "#C98A12", L[0] - 6.6, L[1] + yo - 0.4, 18.5, 58.6, lw * 0.9); line(g, "#C98A12", R[0] + 6.6, R[1] + yo - 0.4, 81.5, 58.6, lw * 0.9);
    } else if (kind === "monoculo") {
      // monóculo de ouro com aro chanfrado e corrente de elos
      const [x, y] = R;
      g.beginPath(); g.ellipse(x, y, 7.3, 7.3, 0, 0, 2 * PI); g.fillStyle = "rgba(220,240,255,0.14)"; g.fill();
      g.strokeStyle = vGrad(g, ["#FFF1BF", "#E9AE34", "#9C6A12"], y - 7.3, y + 7.3); g.lineWidth = small ? 2 : 1.6; g.stroke();
      g.beginPath(); g.ellipse(x, y, 6.4, 6.4, 0, 0, 2 * PI); g.strokeStyle = "rgba(120,70,0,0.4)"; g.lineWidth = 0.3; g.stroke();
      glare(x, y, 5.8, 5.8);
      if (!small) for (let i = 0; i < 9; i++) { const u = i / 8, cx = x + 5.4 + u * 8.6 + sin(u * PI) * 1.4, cy = y + 5.2 + u * 8.4 + sin(u * PI) * 2.6; g.beginPath(); g.ellipse(cx, cy, 0.55, 0.35, u * 2, 0, 2 * PI); g.strokeStyle = GOLD; g.lineWidth = 0.25; g.stroke(); }
    } else if (kind === "protecao") {
      // óculos de proteção: uma lente só, transparente e envolvente, borda de borracha azul, respiros e elástico
      const lw = small ? 1.8 : 1.2;
      line(g, "#2E6FB8", 20 + dx, 59 + dy, 16, 58, small ? 2.6 : 2.2); line(g, "#2E6FB8", 80 + dx, 59 + dy, 84, 58, small ? 2.6 : 2.2);
      g.save(); g.translate(dx, dy);
      const p = new Path2D(); p.moveTo(20.4, 55.4); p.quadraticCurveTo(50, 51.4, 79.6, 55.4); p.quadraticCurveTo(82, 62, 78, 67.6); p.quadraticCurveTo(70, 70.6, 60, 68); p.quadraticCurveTo(55, 63.6, 50, 63.4); p.quadraticCurveTo(45, 63.6, 40, 68); p.quadraticCurveTo(30, 70.6, 22, 67.6); p.quadraticCurveTo(18, 62, 20.4, 55.4); p.closePath();
      g.fillStyle = "rgba(200,240,255,0.24)"; g.fill(p);
      if (!small) { g.save(); g.clip(p); line(g, "rgba(255,255,255,0.4)", 24, 70, 34, 52, 2.4); line(g, "rgba(255,255,255,0.25)", 62, 70, 70, 52, 1.2); g.restore(); }
      stroke(g, p, "#5BA8E6", lw); stroke(g, p, "rgba(255,255,255,0.45)", 0.3);
      if (!small) for (const x of [28, 32, 68, 72]) line(g, "#2E6FB8", x, 55.2 - (x < 50 ? 0 : 0), x + 0.4, 56.8, 0.6);
      g.restore();
    } else if (kind === "tatico") {
      // óculos táticos: viseira envolvente espelhada (azul que vira laranja), barra preta no alto e reflexo correndo
      const lw = small ? 1.8 : 1.2;
      g.save(); g.translate(dx, dy);
      const p = new Path2D(); p.moveTo(19, 56.4); p.quadraticCurveTo(50, 52, 81, 56.4); p.quadraticCurveTo(82, 61.4, 78.6, 65.4); p.quadraticCurveTo(68, 69.4, 58, 65.6); p.quadraticCurveTo(54, 61.6, 50, 61.4); p.quadraticCurveTo(46, 61.6, 42, 65.6); p.quadraticCurveTo(32, 69.4, 21.4, 65.4); p.quadraticCurveTo(18, 61.4, 19, 56.4); p.closePath();
      g.fillStyle = vGrad(g, ["#1E2A5E", "#3A5AC0", "#E07A3A", "#F5C451"], 53, 68); g.fill(p);
      if (!small) { g.save(); g.clip(p); const k = (t * 0.25) % 1; line(g, "rgba(255,255,255,0.35)", 14 + k * 70, 70, 22 + k * 70, 50, 3); line(g, "rgba(255,255,255,0.2)", 20 + k * 70, 70, 25 + k * 70, 50, 1); g.restore(); }
      stroke(g, p, "#14111F", lw);
      const top = new Path2D(); top.moveTo(19.4, 56.6); top.quadraticCurveTo(50, 52.2, 80.6, 56.6); stroke(g, top, "#14111F", lw * 1.8);
      g.restore();
      arms("#14111F", lw * 1.2);
    } else if (kind === "pincene") {
      // pincenê: duas lentes ovais presas no nariz pela mola, sem hastes, com a correntinha dourada pendurada
      const lw = small ? 1.5 : 0.8;
      for (const [x, y] of [L, R]) {
        g.beginPath(); g.ellipse(x, y + 0.6, 6.2, 5.4, 0, 0, 2 * PI); g.fillStyle = "rgba(210,235,255,0.18)"; g.fill();
        g.strokeStyle = vGrad(g, ["#FFF1BF", "#E9AE34", "#9C6A12"], y - 5, y + 6); g.lineWidth = lw; g.stroke();
        glare(x, y + 0.6, 4.8, 4.2);
      }
      const sp = new Path2D(); sp.moveTo(37.2 + dx, 59.6 + dy); sp.bezierCurveTo(42 + dx, 54.4 + dy, 45 + dx, 58.6 + dy, 50 + dx, 56.2 + dy); sp.bezierCurveTo(55 + dx, 58.6 + dy, 58 + dx, 54.4 + dy, 62.8 + dx, 59.6 + dy); stroke(g, sp, "#C98A12", lw);
      if (!small) {
        for (const x of [45, 55]) hatEll(g, "rgba(255,255,255,0.7)", x + dx, 61.4 + dy, 0.8, 1.2, 0);
        const sw = sin(t * 1.6) * 0.8;
        for (let i = 0; i <= 12; i++) { const u = i / 12, cx = L[0] - 6 + (18 - L[0] + 6) * u - sin(u * PI) * 1.6 + sw * sin(u * PI), cy = L[1] + 2 + (84 - L[1] - 2) * u + sin(u * PI) * 3; g.beginPath(); g.ellipse(cx, cy, 0.55, 0.35, u * 3, 0, 2 * PI); g.strokeStyle = GOLD; g.lineWidth = 0.28; g.stroke(); }
      }
    } else if (kind === "lupa-dupla") {
      // óculos de lupa: lentes grossas e redondas de aumento, armação preta pesada, anéis de vidro que deixam o
      // olhar maior e a lupinha extra levantada no canto
      const lw = small ? 2.2 : 1.8;
      for (const [x, y] of [L, R]) {
        const lg = g.createRadialGradient(x - 1, y - 1, 1, x, y, 8.2); lg.addColorStop(0, "rgba(230,245,255,0.05)"); lg.addColorStop(0.7, "rgba(200,230,255,0.18)"); lg.addColorStop(1, "rgba(120,170,230,0.5)");
        g.beginPath(); g.arc(x, y, 8.2, 0, 2 * PI); g.fillStyle = lg; g.fill();
        if (!small) for (const r of [6.6, 4.8]) { g.beginPath(); g.arc(x, y, r, PI * 0.9, PI * 1.6); g.strokeStyle = "rgba(255,255,255,0.4)"; g.lineWidth = 0.35; g.stroke(); }
        g.beginPath(); g.arc(x, y, 8.2, 0, 2 * PI); g.strokeStyle = "#1E1B2E"; g.lineWidth = lw; g.stroke();
        g.beginPath(); g.arc(x, y, 8.2, PI * 1.1, PI * 1.5); g.strokeStyle = "rgba(255,255,255,0.35)"; g.lineWidth = 0.4; g.stroke();
        glare(x, y, 6.4, 6.4);
      }
      bridge("#1E1B2E", lw, 59.4, 57.4); arms("#1E1B2E", lw);
      if (!small) {
        const [x, y] = R, hx = x + 6.4, hy = y - 7;
        line(g, "#3A3550", x + 5.4, y - 5.4, hx, hy, 0.7);
        g.beginPath(); g.arc(hx + 2, hy - 2.2, 2.6, 0, 2 * PI); g.fillStyle = "rgba(200,230,255,0.35)"; g.fill(); g.strokeStyle = "#1E1B2E"; g.lineWidth = 0.9; g.stroke();
        g.beginPath(); g.arc(hx + 2, hy - 2.2, 1.8, PI * 1.1, PI * 1.5); g.strokeStyle = "rgba(255,255,255,0.8)"; g.lineWidth = 0.35; g.stroke();
      }
    } else if (kind === "auditor") {
      // óculos de auditor: retangulares de armação fina de metal grafite, barrinha reforçada em cima
      const lw = small ? 1.5 : 0.75;
      for (const [x, y] of [L, R]) {
        const p = new Path2D(); p.roundRect(x - 7.6, y - 4.4, 15.2, 9, 1.2);
        g.fillStyle = "rgba(210,230,250,0.16)"; g.fill(p); stroke(g, p, "#4A5068", lw);
        line(g, "#2A2E40", x - 7.4, y - 4.2, x + 7.4, y - 4.2, lw * 1.8);
        glare(x, y, 5.6, 3.4);
      }
      bridge("#4A5068", lw, 57.6, 56.6); arms("#4A5068", lw);
      if (!small) for (const x of [44.8, 55.2]) circle(g, "rgba(255,255,255,0.6)", 0.5, x + dx, 62.6 + dy);
    } else if (kind === "dourados") {
      // armação dourada de banqueiro: aro largo de ouro polido, lente levemente âmbar e um brilho que passa
      const lw = small ? 2 : 1.4;
      for (const [x, y] of [L, R]) {
        const p = new Path2D(); p.roundRect(x - 7.4, y - 6, 14.8, 12, 4.8);
        g.fillStyle = "rgba(255,220,150,0.18)"; g.fill(p);
        stroke(g, p, "#9C6A12", lw + 0.5); g.strokeStyle = vGrad(g, ["#FFF1BF", "#F5C451", "#B57A0C"], y - 6, y + 6); g.lineWidth = lw; g.stroke(p);
        glare(x, y, 5.4, 4.4);
      }
      bridge("#D99A1B", lw, 58.6, 57); arms("#D99A1B", lw);
      if (!small) { const k = Math.max(0, sin(t * 1.4)) ** 6; if (k > 0.02) star4(g, R[0] + 6.4, R[1] - 5.6, 2.6 * k, `rgba(255,255,255,${k})`); }
    } else if (kind === "redondos") {
      // óculos redondos de tartaruga: aros grossos bem redondos, pintinhas de casco e ponte em buraco de fechadura
      const lw = small ? 2.4 : 1.9;
      for (const [x, y] of [L, R]) {
        g.beginPath(); g.arc(x, y, 7.4, 0, 2 * PI); g.fillStyle = "rgba(255,235,200,0.14)"; g.fill();
        g.strokeStyle = "#7A4A22"; g.lineWidth = lw; g.stroke();
        if (!small) { g.save(); g.setLineDash([1.2, 1.6]); g.strokeStyle = "#C98A4B"; g.lineWidth = lw * 0.6; g.stroke(); g.setLineDash([0.8, 2.6]); g.lineDashOffset = 1.4; g.strokeStyle = "#3E2410"; g.stroke(); g.restore(); }
        glare(x, y, 5.8, 5.8);
      }
      const kb = new Path2D(); kb.moveTo(38.4 + dx, 59.6 + dy); kb.quadraticCurveTo(41 + dx, 57.2 + dy, 44.6 + dx, 58.2 + dy); kb.quadraticCurveTo(50 + dx, 54.2 + dy, 55.4 + dx, 58.2 + dy); kb.quadraticCurveTo(59 + dx, 57.2 + dy, 61.6 + dx, 59.6 + dy); stroke(g, kb, "#7A4A22", lw * 0.8);
      arms("#7A4A22", lw * 0.7);
    } else if (kind === "gatinho") {
      // óculos gatinho: cantos de fora puxados para cima em ponta, armação vinho-rosa e strass nas pontas
      const lw = small ? 1.9 : 1.4;
      for (const [x, y, o] of [[L[0], L[1], -1], [R[0], R[1], 1]]) {
        const p = new Path2D(); p.moveTo(x - o * 6.6, y - 3.4); p.quadraticCurveTo(x, y - 5.4, x + o * 7.4, y - 5.2); p.lineTo(x + o * 10, y - 8.4); p.quadraticCurveTo(x + o * 9.6, y + 1, x + o * 5, y + 4.8); p.quadraticCurveTo(x, y + 7, x - o * 5, y + 4.4); p.quadraticCurveTo(x - o * 7.6, y + 1.6, x - o * 6.6, y - 3.4); p.closePath();
        g.fillStyle = "rgba(255,190,220,0.2)"; g.fill(p);
        g.strokeStyle = vGrad(g, ["#FF7AB0", "#D63A7A", "#9C1F56"], y - 8, y + 6); g.lineWidth = lw; g.lineJoin = "round"; g.stroke(p);
        glare(x, y, 5, 4.2);
        if (!small) { circle(g, "#FFFFFF", 0.6, x + o * 8.4, y - 6.4); const k = 0.5 + 0.5 * sin(t * 3 + o); star4(g, x + o * 8.4, y - 6.4, 1.8 * k, `rgba(255,255,255,${0.9 * k})`); }
      }
      bridge("#D63A7A", lw, 58.4, 57); arms("#D63A7A", lw);
    } else if (kind === "vr") {
      // óculos de realidade virtual: caixa escura cobrindo os olhos, faixa até os lados, tela da frente com brilho
      // de cores mudando e o sensor piscando
      const sw = small ? 3 : 2.6;
      line(g, "#2A2640", 18 + dx, 60 + dy, 12, 58.6, sw); line(g, "#2A2640", 82 + dx, 60 + dy, 88, 58.6, sw);
      g.save(); g.translate(dx, dy);
      const box = new Path2D(); box.roundRect(18, 52, 64, 18.6, 6);
      g.fillStyle = vGrad(g, ["#5A5480", "#2A2640", "#14111F"], 52, 70.6); g.fill(box); stroke(g, box, "#0E0D14", 0.6);
      const sc = new Path2D(); sc.roundRect(21.4, 54.6, 57.2, 13.4, 4.4);
      const ph = (t * 0.2) % 1, sg = g.createLinearGradient(21 + ph * 40 - 40, 0, 79 + ph * 40, 0);
      sg.addColorStop(0, "#5247E0"); sg.addColorStop(0.35, "#36C8C0"); sg.addColorStop(0.65, "#B995F5"); sg.addColorStop(1, "#5247E0");
      g.fillStyle = sg; g.fill(sc);
      g.save(); g.clip(sc); g.fillStyle = vGrad(g, ["rgba(255,255,255,0.35)", "rgba(255,255,255,0)", "rgba(0,0,0,0.25)"], 54.6, 68); g.fillRect(20, 54, 60, 15); g.restore();
      if (!small) { line(g, "rgba(255,255,255,0.6)", 25, 56.6, 36, 56.2, 0.6); const k = 0.5 + 0.5 * sin(t * 4); circle(g, `rgba(126,224,184,${0.4 + 0.6 * k})`, 0.7, 74.6, 57.6); circle(g, "#14111F", 0.9, 50, 54); }
      g.restore();
    } else if (kind === "neon") {
      // óculos neon: armação que brilha e troca de cor devagar (rosa, laranja, verde, azul...)
      const lw = small ? 1.9 : 1.3, hue = (t * 40) % 360, col = `hsl(${hue},100%,62%)`, glow = `hsla(${hue},100%,62%,0.35)`;
      for (const [x, y] of [L, R]) {
        const p = new Path2D(); p.roundRect(x - 7.6, y - 5.8, 15.2, 11.8, 4.2);
        g.fillStyle = `hsla(${hue},100%,70%,0.14)`; g.fill(p);
        stroke(g, p, glow, lw * 3); stroke(g, p, col, lw); stroke(g, p, "rgba(255,255,255,0.6)", lw * 0.3);
        glare(x, y, 5.4, 4.2);
      }
      bridge(glow, lw * 3, 58.6, 57.4); bridge(col, lw, 58.6, 57.4); arms(col, lw);
    } else if (kind === "3d") {
      // óculos 3D de papelão: armação branca de papel com a dobrinha, lente vermelha na esquerda e azul na direita
      line(g, "#E9E4DA", 20 + dx, 59 + dy, 17, 58, small ? 2.4 : 2); line(g, "#E9E4DA", 80 + dx, 59 + dy, 83, 58, small ? 2.4 : 2);
      g.save(); g.translate(dx, dy);
      const fr = new Path2D(); fr.roundRect(19.4, 54, 61.2, 14.4, 1.6); g.fillStyle = "#FFFDF8"; g.fill(fr); stroke(g, fr, "rgba(80,70,60,0.35)", 0.4);
      const ln = new Path2D(); ln.roundRect(22.6, 56, 18.8, 10.4, 1); g.fillStyle = "rgba(230,40,60,0.72)"; g.fill(ln);
      const rn = new Path2D(); rn.roundRect(58.6, 56, 18.8, 10.4, 1); g.fillStyle = "rgba(40,180,230,0.72)"; g.fill(rn);
      if (!small) { line(g, "rgba(255,255,255,0.5)", 24, 64.6, 28.6, 57.4, 1); line(g, "rgba(255,255,255,0.5)", 60, 64.6, 64.6, 57.4, 1); const nz = new Path2D(); nz.moveTo(45, 68.4); nz.quadraticCurveTo(50, 62.6, 55, 68.4); g.fillStyle = "#FFFDF8"; g.fill(nz); line(g, "rgba(80,70,60,0.25)", 50, 54.4, 50, 61, 0.3); }
      g.restore();
    } else if (kind === "mergulho") {
      // máscara de mergulho: borda de silicone turquesa em volta dos dois olhos com o bolsinho do nariz, vidro
      // com reflexo, alça larga e o snorkel laranja do lado, soltando bolhas
      const lw = small ? 2.6 : 2.2;
      line(g, "#1A8296", 19 + dx, 61 + dy, 14, 59, lw * 1.3); line(g, "#1A8296", 81 + dx, 61 + dy, 86, 59, lw * 1.3);
      if (!small) {
        const sn = new Path2D(); sn.moveTo(80 + dx, 70 + dy); sn.quadraticCurveTo(89, 66, 88.4, 50); sn.lineTo(88, 30); sn.quadraticCurveTo(88, 26, 85, 25);
        stroke(g, sn, "#9C4A10", 3); stroke(g, sn, "#FF8A2B", 2.2); stroke(g, sn, "rgba(255,255,255,0.4)", 0.5);
        const cap = new Path2D(); cap.roundRect(82.6, 22.6, 4.6, 3.6, 1.2); g.fillStyle = "#1A8296"; g.fill(cap);
        for (let i = 0; i < 3; i++) { const ph = (t * 0.6 + i / 3) % 1; g.beginPath(); g.arc(83 + sin(ph * 9 + i) * 1.2, 22 - ph * 10, 0.6 + ph * 0.8, 0, 2 * PI); g.strokeStyle = `rgba(150,220,255,${1 - ph})`; g.lineWidth = 0.35; g.stroke(); }
      }
      g.save(); g.translate(dx, dy);
      const p = new Path2D(); p.moveTo(20, 56); p.quadraticCurveTo(50, 50.6, 80, 56); p.quadraticCurveTo(83, 64, 77, 69.4); p.quadraticCurveTo(66, 72, 58, 68.6); p.quadraticCurveTo(54, 72, 50, 72.6); p.quadraticCurveTo(46, 72, 42, 68.6); p.quadraticCurveTo(34, 72, 23, 69.4); p.quadraticCurveTo(17, 64, 20, 56); p.closePath();
      g.fillStyle = "rgba(170,235,255,0.26)"; g.fill(p);
      if (!small) { g.save(); g.clip(p); line(g, "rgba(255,255,255,0.45)", 26, 72, 36, 52, 2.6); line(g, "rgba(255,255,255,0.25)", 34, 72, 41, 52, 0.9); g.restore(); }
      stroke(g, p, "#1A8296", lw * 1.3); stroke(g, p, "#36C8C0", lw * 0.8); stroke(g, p, "rgba(255,255,255,0.4)", 0.3);
      g.restore();
    }
  }

  // ---- pescoço / peito (embaixo da boca, no pé das páginas)
  // As peças do peito no tamanho certo: os broches e medalhas ficam presos sempre no mesmo lugar (lado
  // esquerdo, abaixo do olho) e maiores, para lerem de longe; gravatas e laços no meio, logo abaixo da boca.
  const NECK_ZOOM = { borboleta: [50, 83.4, 1.35], medalha: [25.6, 73.2, 1.4], roseta: [24.5, 78.4, 1.45], "medalha-militar": [25, 71.6, 1.4], balanca: [24.8, 72.4, 1.5], broche: [23.5, 79.6, 1.55], flor: [76.5, 79.6, 1.45], caneta: [26, 70, 1.3],
    "cruz-saude": [24.5, 77, 1.4], divisas: [24.5, 75.5, 1.35], brasao: [24.5, 76, 1.4], leao: [24.5, 77, 1.4], selo: [24.5, 75.5, 1.35], parlamentar: [24.5, 77, 1.4], estrelinha: [24.5, 77.5, 1.35], postits: [24.5, 76, 1.3], sininho: [50, 83, 2], plaqueta: [50, 83.5, 1.25], perolas: [50, 80, 1.08] };
  function drawNeckwear(g, t, small, kind) {
    const z = NECK_ZOOM[kind];
    if (!z) { drawNeckwearRaw(g, t, small, kind); return; }
    g.save(); g.translate(z[0], z[1]); g.scale(z[2], z[2]); g.translate(-z[0], -z[1]); drawNeckwearRaw(g, t, small, kind); g.restore();
  }
  function drawNeckwearRaw(g, t, small, kind) {
    if (!kind || kind === "nenhum") return;
    if (kind === "borboleta") {
      const x = 50, y = 83.4, red = "#E0475E", dk = "#A8233A";
      for (const side of [-1, 1]) {
        const p = new Path2D(); p.moveTo(x, y); p.quadraticCurveTo(x + side * 3, y - 3.6, x + side * 6, y - 2.6); p.quadraticCurveTo(x + side * 6.8, y, x + side * 6, y + 2.6); p.quadraticCurveTo(x + side * 3, y + 3.6, x, y); p.closePath();
        g.fillStyle = hGrad(g, side < 0 ? [red, dk] : [dk, red], side < 0 ? x - 6 : x, side < 0 ? x : x + 6); g.fill(p);
        if (!small) { const f = new Path2D(); f.moveTo(x + side * 1.5, y - 0.6); f.quadraticCurveTo(x + side * 3.6, y - 1.2, x + side * 5, y - 0.4); stroke(g, f, "rgba(255,255,255,0.35)", 0.35); }
      }
      g.beginPath(); g.ellipse(x, y, 1.6, 1.9, 0, 0, 2 * PI); g.fillStyle = dk; g.fill();
      circle(g, "rgba(255,255,255,0.35)", 0.45, x - 0.5, y - 0.7);
    } else if (kind === "gravata") {
      // gravata listrada de verdade: nó, lâmina larga com ponta e sombra de um lado
      const sw = sin(t * 1.9) * 0.8;
      g.save(); g.translate(50, 80); g.scale(1.3, 1.25); g.translate(-50, -80);
      const blade = new Path2D(); blade.moveTo(48.2, 83.4); blade.lineTo(51.8, 83.4); blade.lineTo(54.6 + sw, 94); blade.lineTo(50 + sw, 98.6); blade.lineTo(45.4 + sw, 94); blade.closePath();
      g.fillStyle = hGrad(g, ["#5A4FE6", "#3A2FC4", "#2A209E"], 45, 55); g.fill(blade);
      g.save(); g.clip(blade); for (let i = 0; i < 7; i++) { const yy = 83 + i * 2.4; line(g, rgba(GOLD, 0.9), 42, yy + 4, 58, yy - 2, 0.8, false); line(g, "rgba(255,255,255,0.18)", 42, yy + 5.2, 58, yy - 0.8, 0.4, false); }
      g.fillStyle = "rgba(10,6,40,0.18)"; g.fillRect(50 + sw * 0.5, 82, 6, 18); g.restore();
      stroke(g, blade, "rgba(18,13,63,0.45)", 0.35);
      const knot = new Path2D(); knot.moveTo(47, 79.8); knot.lineTo(53, 79.8); knot.lineTo(51.8, 83.8); knot.lineTo(48.2, 83.8); knot.closePath();
      g.fillStyle = vGrad(g, ["#4B3FD8", "#2A209E"], 79.8, 83.8); g.fill(knot); stroke(g, knot, "rgba(18,13,63,0.45)", 0.35);
      line(g, "rgba(255,255,255,0.35)", 47.8, 80.6, 52.2, 80.6, 0.5);
      g.restore();
    } else if (kind === "cachecol") {
      // cachecol de tricô: faixa com listras e textura, nó do lado e duas pontas com franja
      const sw = sin(t * 1.7) * 0.8;
      const band = new Path2D(); band.moveTo(26, 79.4); band.quadraticCurveTo(50, 84.4, 74, 79.4); band.quadraticCurveTo(75.6, 82.4, 74.2, 85.4); band.quadraticCurveTo(50, 90, 25.8, 85.4); band.quadraticCurveTo(24.4, 82.4, 26, 79.4); band.closePath();
      const stripes = (path, x0, x1) => { g.save(); g.clip(path); g.fillStyle = "#E0475E"; g.fillRect(x0, 70, x1 - x0, 32); for (let x = x0; x < x1; x += 5) { g.fillStyle = "#FFF4EC"; g.fillRect(x, 70, 2, 32); }
        for (let y = 76; y < 100; y += 1.1) line(g, "rgba(120,20,40,0.12)", x0, y, x1, y, 0.25, false); g.fillStyle = "rgba(60,0,20,0.14)"; g.fillRect(x0, 84, x1 - x0, 20); g.restore(); };
      stripes(band, 22, 80); stroke(g, band, "rgba(140,30,50,0.4)", 0.35);
      for (const [dx0, ang] of [[0, 0], [2.6, 0.16]]) {
        const end = new Path2D(); const x = 60 + dx0; end.moveTo(x - 2.4, 85.6); end.lineTo(x + 2.6, 85); end.lineTo(x + 4.6 + sw + ang * 10, 95.8); end.lineTo(x - 0.6 + sw + ang * 10, 96.4); end.closePath();
        stripes(end, x - 4, x + 10); stroke(g, end, "rgba(140,30,50,0.4)", 0.35);
        for (let i = 0; i < 5; i++) line(g, "#C42F4B", x - 0.2 + sw + ang * 10 + i * 1.05, 96.2, x - 0.4 + sw + ang * 10 + i * 1.05, 98.4, 0.45);
      }
      const knot = new Path2D(); knot.ellipse(61.2, 84.6, 3.4, 2.8, 0, 0, 2 * PI); stripes(knot, 57, 65); stroke(g, knot, "rgba(140,30,50,0.45)", 0.35);
    } else if (kind === "medalha") {
      // medalha de ouro presa no peito, do lado esquerdo: presilha dourada, fita listrada (azul, branco e
      // vermelho) com a ponta em V, e o disco pendurado que balança de leve, com louros, estrela e brilho
      const px = 25.6, py = 73.2, sw = sin(t * 2.1) * 0.07;
      const rib = new Path2D(); rib.moveTo(px - 3.4, py); rib.lineTo(px + 3.4, py); rib.lineTo(px + 3.4, py + 5.4); rib.lineTo(px, py + 6.8); rib.lineTo(px - 3.4, py + 5.4); rib.closePath();
      g.save(); g.clip(rib);
      for (const [x0, w, c] of [[-3.4, 2.4, "#2F6FD0"], [-1, 2, "#FFFFFF"], [1, 2.4, "#D63A55"]]) { g.fillStyle = c; g.fillRect(px + x0, py, w, 7); }
      g.fillStyle = "rgba(0,0,0,0.12)"; g.fillRect(px + 1.6, py, 2, 7); line(g, "rgba(255,255,255,0.4)", px - 3, py + 1, px - 3, py + 5.2, 0.35);
      g.restore(); stroke(g, rib, "rgba(20,20,60,0.35)", 0.3);
      const bar = new Path2D(); bar.roundRect(px - 3.9, py - 1.1, 7.8, 1.6, 0.6); g.fillStyle = vGrad(g, ["#FFF1BF", "#E9AE34", "#9C6A12"], py - 1.1, py + 0.5); g.fill(bar);
      g.save(); g.translate(px, py + 6.4); g.rotate(sw);
      g.beginPath(); g.arc(0, 0.6, 0.9, 0, 2 * PI); g.strokeStyle = "#B57A0C"; g.lineWidth = 0.5; g.stroke();
      const my = 5.6, R = 4.5;
      circle(g, "rgba(60,30,0,0.35)", R + 0.2, 0.3, my + 0.5);
      g.beginPath(); g.arc(0, my, R, 0, 2 * PI); g.fillStyle = vGrad(g, ["#FFF4CC", "#F5C451", "#C98A12", "#9C6A12"], my - R, my + R); g.fill();
      g.beginPath(); g.arc(0, my, R - 0.9, 0, 2 * PI); g.fillStyle = vGrad(g, ["#E9AE34", "#FFE08A"], my - R, my + R); g.fill();
      // louros em volta
      for (let i = 0; i < 10; i++) { if (i === 0 || i === 5) continue; const a = PI / 2 + i * PI / 5, s = i < 5 ? 1 : -1; g.beginPath(); g.ellipse(cos(a) * 2.75, my + sin(a) * 2.75, 0.75, 0.35, a + s * 0.9, 0, 2 * PI); g.fillStyle = "rgba(150,95,10,0.75)"; g.fill(); }
      g.fillStyle = vGrad(g, ["#FFF6D6", "#E9AE34"], my - 1.8, my + 1.8); g.fill(star5(g, 0, my + 0.1, 1.9)); stroke(g, star5(g, 0, my + 0.1, 1.9), "rgba(150,95,10,0.6)", 0.2);
      g.save(); g.beginPath(); g.arc(0, my, R, 0, 2 * PI); g.clip(); const k = (t * 0.35) % 1.6; line(g, "rgba(255,255,255,0.6)", -6 + k * 9, my - 6, -8 + k * 9, my + 6, 1.3); g.restore();
      g.restore();
    } else if (kind === "distintivo") {
      // distintivo da polícia preso no peito: estrela de seis pontas em ouro chanfrado (metade clara, metade
      // sombra), as bolinhas nas pontas, o anel com pontinhos gravados e o centro azul com a estrela; um
      // brilho passa de tempos em tempos
      const px = 25, py = 76.5, R = 6, sw = sin(t * 1.6) * 0.03;
      g.save(); g.translate(px, py); g.rotate(sw);
      const star6 = (r, dx = 0, dy = 0) => { const p = new Path2D(); for (let i = 0; i < 12; i++) { const a = -PI / 2 + i * PI / 6, rr = i % 2 ? r * 0.56 : r; i ? p.lineTo(dx + cos(a) * rr, dy + sin(a) * rr) : p.moveTo(dx + cos(a) * rr, dy + sin(a) * rr); } p.closePath(); return p; };
      g.fillStyle = "rgba(40,25,0,0.35)"; g.fill(star6(R, 0.45, 0.6));
      g.fillStyle = vGrad(g, ["#FFF4CC", "#F5C451", "#B57A0C"], -R, R); g.fill(star6(R));
      // chanfro: cada ponta com o lado da sombra
      for (let i = 0; i < 6; i++) { const a = -PI / 2 + i * PI / 3, b = a + PI / 6; const f = new Path2D(); f.moveTo(0, 0); f.lineTo(cos(a) * R, sin(a) * R); f.lineTo(cos(b) * R * 0.56, sin(b) * R * 0.56); f.closePath(); g.fillStyle = "rgba(150,95,10,0.28)"; g.fill(f); }
      stroke(g, star6(R), "rgba(110,70,0,0.65)", 0.3);
      for (let i = 0; i < 6; i++) { const a = -PI / 2 + i * PI / 3; circle(g, "#B57A0C", 0.75, cos(a) * R, sin(a) * R); circle(g, "#FFE9A8", 0.5, cos(a) * R - 0.12, sin(a) * R - 0.12); }
      circle(g, "#B57A0C", 3.3, 0, 0); circle(g, "#F5C451", 3, 0, 0);
      for (let i = 0; i < 16; i++) { const a = i * PI / 8; circle(g, "rgba(120,75,5,0.75)", 0.18, cos(a) * 2.45, sin(a) * 2.45); }
      circle(g, "#1A2660", 1.95, 0, 0); g.fillStyle = vGrad(g, ["#FFF1BF", "#E9AE34"], -1.2, 1.2); g.fill(star5(g, 0, 0.1, 1.25));
      g.save(); g.clip(star6(R)); const k = (t * 0.3) % 1.8; line(g, "rgba(255,255,255,0.7)", -8 + k * 12, -8, -11 + k * 12, 8, 1.6); g.restore();
      g.restore();

    } else if (kind === "cracha") {
      // crachá de professor: o cordão menta vem dos dois ombros e se encontra na presilha; o cartão pende no
      // meio do peito, com a faixa índigo, a foto do Folha, as linhas do nome e o código de barras
      const sw = sin(t * 1.7) * 0.05, cx = 50, cy = 83;
      for (const p of [(() => { const q = new Path2D(); q.moveTo(14, 66); q.quadraticCurveTo(26, 86, cx - 0.8, cy); return q; })(), (() => { const q = new Path2D(); q.moveTo(86, 66); q.quadraticCurveTo(74, 86, cx + 0.8, cy); return q; })()]) {
        stroke(g, p, "rgba(10,60,40,0.2)", small ? 2.8 : 2.3); stroke(g, p, "#1E8A5A", small ? 2.2 : 1.7); stroke(g, p, "#5FE3B5", small ? 0.9 : 0.6);
      }
      g.save(); g.translate(cx, cy); g.rotate(sw); g.scale(1.25, 1.25);
      const clip = new Path2D(); clip.roundRect(-1.1, -0.8, 2.2, 2.8, 0.5); g.fillStyle = hGrad(g, ["#8E92A6", "#E3E6F0", "#8E92A6"], -1.1, 1.1); g.fill(clip);
      g.fillStyle = "rgba(20,20,60,0.25)"; g.beginPath(); g.roundRect(-4.3, 2.1, 9.2, 11.6, 1.2); g.fill();
      const card = new Path2D(); card.roundRect(-4.6, 1.6, 9.2, 11.6, 1.2); g.fillStyle = "#FFFFFF"; g.fill(card); stroke(g, card, "rgba(40,40,80,0.4)", 0.25);
      g.save(); g.clip(card); g.fillStyle = vGrad(g, ["#3A2FC4", "#2A209E"], 1.6, 4.6); g.fillRect(-5, 1.6, 10, 3); g.restore();
      line(g, "rgba(255,255,255,0.85)", -3.4, 3.1, 0.6, 3.1, 0.45); circle(g, "#5FE3B5", 0.55, 3.2, 3.1);
      const ph = new Path2D(); ph.roundRect(-3.6, 5.4, 3.8, 4.4, 0.6); g.fillStyle = "#FFF6E0"; g.fill(ph); stroke(g, ph, "rgba(40,40,80,0.3)", 0.2);
      g.fillStyle = "#2A209E"; g.fillRect(-3.4, 5.6, 3.4, 1.1);
      circle(g, "#1D1670", 0.32, -2.4, 7.7); circle(g, "#1D1670", 0.32, -0.9, 7.7);
      const sm = new Path2D(); sm.moveTo(-2.4, 8.7); sm.quadraticCurveTo(-1.65, 9.3, -0.9, 8.7); stroke(g, sm, "#1D1670", 0.22);
      for (const [y, w] of [[6, 3.4], [7.3, 2.4], [8.6, 2.9]]) line(g, "rgba(60,60,100,0.5)", 1, y, 1 + w, y, 0.4);
      for (let i = 0; i < 12; i++) g.fillRect(-3.4 + i * 0.6, 10.6, i % 3 ? 0.25 : 0.4, 1.6);
      g.restore();

    } else if (kind === "caneta") {
      // caneta-tinteiro de laca preta com clipe dourado e uma lapiseira menta, num bolsinho de couro
      const px = 26, py = 70;
      g.save(); g.translate(px - 1.2, py); g.rotate(-0.06);
      const body = new Path2D(); body.roundRect(-1.3, -6, 2.6, 14, 1.2); g.fillStyle = hGrad(g, ["#0E0E14", "#3A3A48", "#0E0E14"], -1.3, 1.3); g.fill(body);
      g.fillStyle = hGrad(g, GOLD3, -1.35, 1.35); g.fillRect(-1.35, -0.6, 2.7, 0.9);
      const cl = new Path2D(); cl.roundRect(0.7, -5.6, 0.8, 7, 0.4); g.fillStyle = vGrad(g, GOLD3, -5.6, 1.4); g.fill(cl); circle(g, "#E9AE34", 0.55, 1.1, 1.4);
      circle(g, "#E9AE34", 0.6, 0, -6);
      line(g, "rgba(255,255,255,0.4)", -0.6, -5, -0.6, 7, 0.3);
      g.restore();
      g.save(); g.translate(px + 1.8, py + 1); g.rotate(0.08);
      const b2 = new Path2D(); b2.roundRect(-0.9, -4.6, 1.8, 12, 0.8); g.fillStyle = hGrad(g, ["#2E9E72", "#7EE0B8", "#2E9E72"], -0.9, 0.9); g.fill(b2);
      g.fillStyle = "#C9CCE0"; g.fillRect(0.4, -4.2, 0.5, 5);
      g.restore();
      const pk = new Path2D(); pk.moveTo(px - 4.6, py + 3); pk.lineTo(px + 4.6, py + 3); pk.lineTo(px + 4.2, py + 11); pk.quadraticCurveTo(px, py + 12.6, px - 4.2, py + 11); pk.closePath();
      g.fillStyle = vGrad(g, ["#5A3A26", "#3A2418"], py + 3, py + 12.6); g.fill(pk); stroke(g, pk, "rgba(20,10,5,0.6)", 0.3);
      g.save(); g.setLineDash([0.6, 0.5]); const st = new Path2D(); st.moveTo(px - 3.8, py + 4); st.lineTo(px + 3.8, py + 4); stroke(g, st, "rgba(240,200,150,0.6)", 0.22); g.restore();
      line(g, "rgba(255,255,255,0.15)", px - 3.6, py + 5, px - 3.4, py + 10, 0.4);
    } else if (kind === "medalha-militar") {
      // medalha de honra: barretas no alto (verde e amarelo; azul e branco), a fita verde com a faixa
      // amarela dobrada em pentágono e a estrela de cinco pontas com o centro esmaltado de azul
      const px = 25, py = 71.6, sw = sin(t * 1.8) * 0.05;
      const bars = [[px - 5, ["#1E8A3A", "#FFD23F", "#1E8A3A"]], [px + 0.2, ["#1F4FB0", "#FFFFFF", "#1F4FB0"]]];
      for (const [x, c] of bars) { const w = 4.8, h = 2.2; g.fillStyle = c[0]; g.fillRect(x, py, w, h); g.fillStyle = c[1]; g.fillRect(x + w * 0.36, py, w * 0.28, h); g.fillStyle = "rgba(0,0,0,0.15)"; g.fillRect(x, py + h * 0.6, w, h * 0.4); stroke(g, (() => { const p = new Path2D(); p.rect(x, py, w, h); return p; })(), "rgba(20,30,10,0.5)", 0.25); }
      g.save(); g.translate(px, py + 3); g.rotate(sw);
      const rib = new Path2D(); rib.moveTo(-2.8, 0); rib.lineTo(2.8, 0); rib.lineTo(2.8, 3.2); rib.lineTo(0, 4.6); rib.lineTo(-2.8, 3.2); rib.closePath();
      g.save(); g.clip(rib); g.fillStyle = "#1E8A3A"; g.fillRect(-3, 0, 6, 5); g.fillStyle = "#FFD23F"; g.fillRect(-0.8, 0, 1.6, 5); g.fillStyle = "rgba(0,0,0,0.14)"; g.fillRect(0.8, 0, 2, 5); g.restore();
      stroke(g, rib, "rgba(20,30,10,0.5)", 0.25);
      const sy = 8.6, R = 4.2;
      g.fillStyle = "rgba(40,25,0,0.35)"; g.fill(star5(g, 0.3, sy + 0.4, R));
      g.fillStyle = vGrad(g, ["#FFF4CC", "#F5C451", "#B57A0C"], sy - R, sy + R); g.fill(star5(g, 0, sy, R));
      stroke(g, star5(g, 0, sy, R), "rgba(110,70,0,0.55)", 0.25);
      for (let i = 0; i < 5; i++) { const a = -PI / 2 + i * 2 * PI / 5; line(g, "rgba(150,95,10,0.45)", 0, sy, cos(a) * R * 0.85, sy + sin(a) * R * 0.85, 0.2); }
      circle(g, "#B57A0C", 1.75, 0, sy); circle(g, "#1F4FB0", 1.45, 0, sy); g.fillStyle = "#FFFFFF"; g.fill(star5(g, 0, sy + 0.05, 0.75));
      const k = Math.max(0, sin(t * 1.3 + 1)) ** 8; if (k > 0.02) star4(g, 2.6, sy - 3, 2.2 * k, `rgba(255,255,255,${k})`);
      g.restore();
    } else if (kind === "estetoscopio") {
      // estetoscópio pendurado nos ombros: o tubo desce em U por baixo da boca e as duas pontas caem pelos lados,
      // de um as olivas (as hastes de metal com as pontinhas pretas), do outro o diafragma prateado balançando
      const sw = sin(t * 2) * 0.6, TUBE = "#1F4A66", TUBE_L = "#3B86AE", w = small ? 2.6 : 2.1;
      const tube = (p) => { stroke(g, p, "rgba(10,30,50,0.25)", w + 0.9); stroke(g, p, TUBE, w); stroke(g, p, TUBE_L, w * 0.42); };
      const collar = new Path2D(); collar.moveTo(14, 66); collar.bezierCurveTo(19, 93, 81, 93, 86, 66); tube(collar);
      const left = new Path2D(); left.moveTo(14, 66); left.bezierCurveTo(10.6, 71, 11 + sw * 0.3, 77, 13 + sw * 0.4, 80.6); tube(left);
      const ex = 13 + sw * 0.4, ey = 80.6;
      for (const s of [-1, 1]) { const arm = new Path2D(); arm.moveTo(ex, ey); arm.quadraticCurveTo(ex + s * 2.8, ey + 1.6, ex + s * 2.6, ey + 5.4); stroke(g, arm, "#7C8598", 1.1); stroke(g, arm, "#DDE3EC", 0.45); circle(g, "#1E1B2E", 1, ex + s * 2.6, ey + 5.8); circle(g, "rgba(255,255,255,0.4)", 0.35, ex + s * 2.6 - 0.3, ey + 5.5); }
      circle(g, "#C9D0DC", 1.1, ex, ey); circle(g, "#FFFFFF", 0.4, ex - 0.3, ey - 0.3);
      const right = new Path2D(); right.moveTo(86, 66); right.bezierCurveTo(89.4, 71, 89 + sw, 77, 87 + sw, 80.4); tube(right);
      const dx = 87 + sw, dy = 84.6, R = 3.6;
      line(g, "#9AA3B4", dx, 80.2, dx, 81.4, 1.3);
      circle(g, "rgba(20,30,50,0.3)", R + 0.3, dx + 0.4, dy + 0.5);
      g.beginPath(); g.arc(dx, dy, R, 0, 2 * PI); g.fillStyle = vGrad(g, ["#F3F6FA", "#AEB6C4", "#6E7588"], dy - R, dy + R); g.fill();
      g.beginPath(); g.arc(dx, dy, R * 0.7, 0, 2 * PI); g.fillStyle = vGrad(g, ["#C9D0DA", "#F7F9FC"], dy - R, dy + R); g.fill();
      g.beginPath(); g.arc(dx, dy, R * 0.7, 0, 2 * PI); g.strokeStyle = "rgba(90,100,120,0.5)"; g.lineWidth = 0.3; g.stroke();
      g.beginPath(); g.arc(dx, dy, R, PI * 1.1, PI * 1.55); g.strokeStyle = "rgba(255,255,255,0.95)"; g.lineWidth = 0.5; g.stroke();

    } else if (kind === "balanca") {
      // broche da balança da Justiça, em ouro: coluna, travessão que oscila devagar e os dois pratos
      const x = 24.8, y = 72.4, tilt = sin(t * 1.2) * 0.1;
      const GOLD_G = (y0, y1) => vGrad(g, ["#FFF1BF", "#E9AE34", "#9C6A12"], y0, y1);
      const base = new Path2D(); base.moveTo(x - 2.6, y + 10.2); base.lineTo(x + 2.6, y + 10.2); base.lineTo(x + 1.4, y + 8.8); base.lineTo(x - 1.4, y + 8.8); base.closePath(); g.fillStyle = GOLD_G(y + 8.8, y + 10.2); g.fill(base);
      line(g, "#C98A12", x, y + 0.6, x, y + 9, 0.8); line(g, "rgba(255,255,255,0.5)", x - 0.2, y + 1.4, x - 0.2, y + 8.4, 0.2);
      circle(g, "#E9AE34", 0.9, x, y); circle(g, "rgba(255,255,255,0.7)", 0.3, x - 0.3, y - 0.3);
      g.save(); g.translate(x, y + 1.6); g.rotate(tilt);
      line(g, "#C98A12", -5, 0, 5, 0, 0.7);
      for (const s of [-1, 1]) {
        g.save(); g.translate(s * 4.8, 0); g.rotate(-tilt);
        line(g, "rgba(150,95,10,0.8)", 0, 0, -1.8, 4.2, 0.2); line(g, "rgba(150,95,10,0.8)", 0, 0, 1.8, 4.2, 0.2);
        const pan = new Path2D(); pan.moveTo(-2.2, 4.2); pan.lineTo(2.2, 4.2); pan.quadraticCurveTo(1.8, 5.8, 0, 5.8); pan.quadraticCurveTo(-1.8, 5.8, -2.2, 4.2); pan.closePath(); g.fillStyle = GOLD_G(4.2, 5.8); g.fill(pan);
        g.restore();
      }
      g.restore();
    } else if (kind === "broche") {
      // broche de estrela em metal chanfrado: estrela de fora escura, de dentro clara
      const r = small ? 4.2 : 3.6, x = 23.5, y = 79.6;
      g.fillStyle = vGrad(g, ["#C98A12", "#8A5A0C"], y - r, y + r); g.fill(star5(g, x, y + 0.3, r));
      g.fillStyle = vGrad(g, ["#FFF6D6", "#F5C451", "#D99A1B"], y - r, y + r); g.fill(star5(g, x, y, r * 0.86));
      g.fillStyle = "rgba(255,255,255,0.45)"; g.fill(star5(g, x - 0.4, y - 0.4, r * 0.35));
      if (!small) { const k = Math.max(0, sin(t * 1.7)) ** 6; if (k > 0.02) star4(g, x + 2, y - 2, 2.4 * k, `rgba(255,255,255,${0.95 * k})`); }
    } else if (kind === "flor") {
      // flor na lapela: pétalas em degradê com nervura, miolo de pontinhos e uma folha
      const x = 76.5, y = 79.6, rot = sin(t * 1.5) * 0.15;
      const leaf = new Path2D(); leaf.moveTo(x - 1, y + 2.4); leaf.quadraticCurveTo(x - 6.4, y + 3.4, x - 6.4, y + 7.2); leaf.quadraticCurveTo(x - 2.4, y + 6.4, x - 1, y + 2.4); leaf.closePath();
      g.fillStyle = vGrad(g, ["#5FD3A8", "#2E9E72"], y + 2, y + 7); g.fill(leaf); line(g, "rgba(255,255,255,0.4)", x - 1.4, y + 3, x - 5.4, y + 6.2, 0.25);
      g.save(); g.translate(x, y); g.rotate(rot);
      for (let i = 0; i < 6; i++) {
        const a = i * PI / 3; g.save(); g.rotate(a);
        const p = new Path2D(); p.moveTo(0, 0); p.bezierCurveTo(1.6, -1.6, 4, -1.2, 4.2, 0); p.bezierCurveTo(4, 1.2, 1.6, 1.6, 0, 0); p.closePath();
        g.fillStyle = hGrad(g, ["#FF6F8E", "#FFB3C7", "#FFE3EA"], 0, 4.2); g.fill(p); line(g, "rgba(200,60,90,0.35)", 0.6, 0, 3.4, 0, 0.18);
        g.restore();
      }
      circle(g, "#F5B83D", 1.4, 0, 0);
      for (let i = 0; i < 6; i++) { const a = i * PI / 3 + 0.5; circle(g, "#C98A12", 0.22, cos(a) * 0.8, sin(a) * 0.8); }
      circle(g, "rgba(255,255,255,0.6)", 0.35, -0.4, -0.5);
      g.restore();
    } else if (kind === "roseta") {
      // roseta de campeão: duas camadas de pregas, caudas em V e o "1" no centro
      const x = 24.5, y = 78.4;
      for (const [s, c] of [[-1, "#3A2FC4"], [1, "#2A209E"]]) {
        const tl = new Path2D(); tl.moveTo(x + s * 0.4, y + 2); tl.lineTo(x + s * 3.4, y + 10.6); tl.lineTo(x + s * 2, y + 9.4); tl.lineTo(x + s * 1, y + 11); tl.lineTo(x - s * 0.8, y + 2.6); tl.closePath();
        g.fillStyle = c; g.fill(tl); line(g, "rgba(255,255,255,0.35)", x + s * 0.6, y + 3, x + s * 2.6, y + 9.6, 0.3);
      }
      for (const [rr, n, c1, c2] of [[4.4, 16, "#3A2FC4", "#2A209E"], [3.2, 12, "#F5C451", "#E9AE34"]]) {
        for (let i = 0; i < n; i++) { const a = i * 2 * PI / n; g.beginPath(); g.ellipse(x + cos(a) * rr * 0.72, y + sin(a) * rr * 0.72, rr * 0.42, rr * 0.28, a, 0, 2 * PI); g.fillStyle = i % 2 ? c1 : c2; g.fill(); }
      }
      g.beginPath(); g.arc(x, y, 2.2, 0, 2 * PI); g.fillStyle = vGrad(g, ["#FFFDF4", "#FFE3A0"], y - 2.2, y + 2.2); g.fill();
      g.save(); g.font = "900 3px Nunito, system-ui, sans-serif"; g.textAlign = "center"; g.fillStyle = "#2A209E"; g.fillText("1", x, y + 1.1); g.restore();
    } else if (kind === "cruz-saude") {
      // broche redondo da saúde: aro dourado, fundo branco com filete verde e a cruz vermelha em relevo;
      // de tempos em tempos um brilho atravessa o broche
      const x = 24.5, y = 77, R = 4.6;
      circle(g, "rgba(40,20,20,0.3)", R + 0.2, x + 0.35, y + 0.5);
      circle(g, vGrad(g, ["#FFF1BF", "#E9AE34", "#9C6A12"], y - R, y + R), R, x, y);
      circle(g, vGrad(g, ["#FFFFFF", "#E3E9F0"], y - R, y + R), R - 0.8, x, y);
      g.beginPath(); g.arc(x, y, R - 1.35, 0, 2 * PI); g.strokeStyle = "#2E9E5A"; g.lineWidth = 0.45; g.stroke();
      const cr = new Path2D(); cr.roundRect(x - 0.95, y - 2.5, 1.9, 5, 0.35); cr.roundRect(x - 2.5, y - 0.95, 5, 1.9, 0.35);
      g.save(); g.translate(0.25, 0.3); g.fillStyle = "rgba(120,10,20,0.35)"; g.fill(cr); g.restore();
      g.fillStyle = vGrad(g, ["#FF6B6B", "#E02A3A", "#A8142A"], y - 2.5, y + 2.5); g.fill(cr);
      line(g, "rgba(255,255,255,0.6)", x - 0.35, y - 2, x - 0.35, y - 1.2, 0.3);
      g.save(); g.beginPath(); g.arc(x, y, R, 0, 2 * PI); g.clip(); const k = (t * 0.33) % 1.7; line(g, "rgba(255,255,255,0.55)", x - 6 + k * 9, y - 6, x - 8 + k * 9, y + 6, 1.2); g.restore();
    } else if (kind === "divisas") {
      // divisas de sargento: escudinho verde-oliva com aro dourado e três "V" dourados empilhados
      const x = 24.5, y = 75.5;
      g.save(); g.translate(x, y); g.rotate(sin(t * 1.6) * 0.03);
      const sh = new Path2D(); sh.moveTo(-4.4, -4.6); sh.lineTo(4.4, -4.6); sh.lineTo(4.4, 2); sh.quadraticCurveTo(4, 5.6, 0, 7.4); sh.quadraticCurveTo(-4, 5.6, -4.4, 2); sh.closePath();
      g.save(); g.translate(0.4, 0.5); g.fillStyle = "rgba(20,25,5,0.35)"; g.fill(sh); g.restore();
      g.fillStyle = vGrad(g, ["#7E8B4E", "#5C6934", "#3E4A20"], -4.6, 7.4); g.fill(sh);
      stroke(g, sh, "#C98A12", small ? 1 : 0.7); stroke(g, sh, "rgba(255,241,191,0.7)", 0.25);
      for (let i = 0; i < 3; i++) {
        const yy = -2.6 + i * 2.5, v = new Path2D(); v.moveTo(-3, yy); v.lineTo(0, yy + 2.2); v.lineTo(3, yy);
        stroke(g, v, "rgba(60,40,0,0.55)", 1.5); stroke(g, v, "#E9AE34", 1.05); stroke(g, v, "rgba(255,246,214,0.8)", 0.3);
      }
      const k = Math.max(0, sin(t * 1.4 + 2)) ** 8; if (k > 0.02) star4(g, 3, -3.4, 2 * k, `rgba(255,255,255,${k})`);
      g.restore();
    } else if (kind === "plaqueta") {
      // plaqueta de identificação militar: correntinha de bolinhas vinda dos ombros e as duas plaquetas de
      // metal penduradas no peito, com furo, borda em relevo e linhas gravadas; balançam de leve
      const cx = 50, cy = 83.5;
      for (const [x0, c1x] of [[14, 26], [86, 74]]) {
        const ex = cx + (x0 < 50 ? -0.6 : 0.6);
        if (small) { const p = new Path2D(); p.moveTo(x0, 66); p.quadraticCurveTo(c1x, 86, ex, cy); stroke(g, p, "#3C4054", 1.8); stroke(g, p, "#B9BFCC", 1.1); stroke(g, p, "#F3F6FA", 0.4); }
        else for (let i = 0; i <= 26; i++) { const u = i / 26, a = (1 - u) * (1 - u), b = 2 * u * (1 - u), c = u * u, px = a * x0 + b * c1x + c * ex, py = a * 66 + b * 86 + c * cy; circle(g, "#3C4054", 0.85, px, py); circle(g, "#9AA3B4", 0.62, px, py); circle(g, "#F3F6FA", 0.3, px - 0.2, py - 0.2); }
      }
      g.save(); g.translate(cx, cy); g.rotate(sin(t * 1.7) * 0.07); g.scale(1.55, 1.55);
      const tag = (dx, dy, ang, back) => {
        g.save(); g.translate(dx, dy); g.rotate(ang);
        const p = new Path2D(); p.roundRect(-3.2, 1.2, 6.4, 9.4, 2.2);
        g.save(); g.translate(0.35, 0.45); g.fillStyle = "rgba(20,25,40,0.3)"; g.fill(p); g.restore();
        g.fillStyle = hGrad(g, back ? ["#6E7588", "#C3C9D6", "#8E95A6"] : ["#8E95A6", "#F3F6FA", "#C3C9D6", "#7C8398"], -3.2, 3.2); g.fill(p);
        stroke(g, p, "#2A2E3C", small ? 0.8 : 0.5);
        const inner = new Path2D(); inner.roundRect(-2.5, 1.9, 5, 8, 1.6); stroke(g, inner, "rgba(255,255,255,0.55)", 0.25);
        circle(g, "#3C4054", 0.55, 0, 2.8); circle(g, "rgba(255,255,255,0.6)", 0.2, -0.15, 2.65);
        if (!back) for (const [yy, w] of [[5, 3.4], [6.4, 2.6], [7.8, 3]]) { line(g, "rgba(60,64,84,0.6)", -w / 2, yy, w / 2, yy, 0.4); line(g, "rgba(255,255,255,0.6)", -w / 2, yy + 0.35, w / 2, yy + 0.35, 0.2); }
        g.restore();
      };
      g.beginPath(); g.arc(0, 0.6, 0.8, 0, 2 * PI); g.strokeStyle = "#9AA3B4"; g.lineWidth = 0.4; g.stroke();
      tag(1.2, 0.4, 0.2, true); tag(0, 0, 0, false);
      g.restore();
    } else if (kind === "brasao") {
      // brasão da corporação: escudo de ouro com o campo azul, a faixa dourada de cima com três pontinhos,
      // a estrela de cinco pontas no meio e um brilho que passa de tempos em tempos
      const x = 24.5, y = 76;
      const shield = (k) => { const p = new Path2D(); p.moveTo(-4.6 * k, -4.6 * k); p.quadraticCurveTo(0, -3.4 * k, 4.6 * k, -4.6 * k); p.lineTo(4.6 * k, 0.6 * k); p.quadraticCurveTo(4.3 * k, 4.8 * k, 0, 6.8 * k); p.quadraticCurveTo(-4.3 * k, 4.8 * k, -4.6 * k, 0.6 * k); p.closePath(); return p; };
      g.save(); g.translate(x, y); g.rotate(sin(t * 1.5) * 0.03);
      g.save(); g.translate(0.4, 0.5); g.fillStyle = "rgba(20,20,50,0.35)"; g.fill(shield(1)); g.restore();
      g.fillStyle = vGrad(g, ["#FFF1BF", "#E9AE34", "#9C6A12"], -4.6, 6.8); g.fill(shield(1));
      g.fillStyle = vGrad(g, ["#3D63C8", "#1F3F9A", "#132A6E"], -4, 6); g.fill(shield(0.8));
      g.save(); g.clip(shield(0.8)); g.fillStyle = vGrad(g, ["#FFE08A", "#C98A12"], -4, -1.8); g.fillRect(-5, -5, 10, 3.2); g.restore();
      for (const dx of [-1.8, 0, 1.8]) circle(g, "#1F3F9A", 0.35, dx, -2.7);
      g.fillStyle = vGrad(g, ["#FFF6D6", "#E9AE34"], -0.8, 4); g.fill(star5(g, 0, 1.6, 2.4)); stroke(g, star5(g, 0, 1.6, 2.4), "rgba(110,70,0,0.6)", 0.2);
      stroke(g, shield(1), "rgba(110,70,0,0.65)", small ? 0.8 : 0.35);
      g.save(); g.clip(shield(1)); const k = (t * 0.3) % 1.8; line(g, "rgba(255,255,255,0.6)", -8 + k * 12, -7, -11 + k * 12, 8, 1.4); g.restore();
      g.restore();
    } else if (kind === "jabo") {
      // jabô de renda: três babados brancos em cascata presos num nozinho logo abaixo da boca, com a barra
      // recortada em ondinhas, furinhos de renda e uma sombra suave; balança de leve
      g.save(); g.translate(50, 81.2); g.rotate(sin(t * 1.6) * 0.04); g.scale(1.2, 1.2);
      for (let i = 2; i >= 0; i--) {
        const y0 = 0.6 + i * 3.3, w0 = 1.8 + i * 0.7, w = 4.4 + i * 1.3, h = 4.6, n = 5;
        const p = new Path2D(); p.moveTo(-w0, y0); p.lineTo(w0, y0); p.quadraticCurveTo(w * 0.9, y0 + h * 0.4, w, y0 + h);
        for (let j = n; j > 0; j--) { const xa = -w + (j / n) * 2 * w, xb = -w + ((j - 1) / n) * 2 * w; p.quadraticCurveTo((xa + xb) / 2, y0 + h + 1.6, xb, y0 + h); }
        p.quadraticCurveTo(-w * 0.9, y0 + h * 0.4, -w0, y0); p.closePath();
        g.save(); g.translate(0.3, 0.5); g.fillStyle = "rgba(40,40,90,0.16)"; g.fill(p); g.restore();
        g.fillStyle = vGrad(g, ["#FFFFFF", "#F1F2F9", "#D9DCEA"], y0, y0 + h + 1.6); g.fill(p);
        stroke(g, p, "rgba(90,90,140,0.45)", 0.25);
        if (!small) {
          for (let j = 0; j < n; j++) circle(g, "rgba(140,140,190,0.55)", 0.32, -w + (j + 0.5) / n * 2 * w, y0 + h + 0.3);
          for (const dx of [-0.45, 0.45]) line(g, "rgba(150,150,200,0.35)", dx * w, y0 + 1, dx * w * 1.6, y0 + h - 0.3, 0.2);
        }
      }
      g.beginPath(); g.ellipse(0, 0.4, 1.9, 1.3, 0, 0, 2 * PI); g.fillStyle = vGrad(g, ["#FFFFFF", "#D9DCEA"], -0.9, 1.7); g.fill(); g.strokeStyle = "rgba(90,90,140,0.5)"; g.lineWidth = 0.25; g.stroke();
      circle(g, "rgba(255,255,255,0.9)", 0.4, -0.6, 0);
      g.restore();
    } else if (kind === "leao") {
      // broche do leão (Receita): juba de pétalas douradas em volta, carinha de ouro claro com orelhas,
      // olhos, focinho e bigodinho gravados; de vez em quando um brilho pisca na juba
      const x = 24.5, y = 77;
      circle(g, "rgba(60,30,0,0.35)", 5.2, x + 0.35, y + 0.5);
      for (let i = 0; i < 14; i++) { const a = i * 2 * PI / 14 + 0.1; g.beginPath(); g.ellipse(x + cos(a) * 3.6, y + sin(a) * 3.6, 1.9, 1.15, a, 0, 2 * PI); g.fillStyle = vGrad(g, i % 2 ? ["#F5C451", "#B57A0C"] : ["#E9AE34", "#8A5A0C"], y - 5, y + 5); g.fill(); }
      for (const s of [-1, 1]) { circle(g, "#C98A12", 1.05, x + s * 2.2, y - 2.3); circle(g, "#FFE08A", 0.55, x + s * 2.2, y - 2.2); }
      circle(g, vGrad(g, ["#FFF4CC", "#F5C451", "#D99A1B"], y - 3, y + 3), 3, x, y);
      for (const s of [-1, 1]) circle(g, "#5A3A06", 0.42, x + s * 1.1, y - 0.6);
      circle(g, "#FFF6D6", 0.85, x - 0.55, y + 1.2); circle(g, "#FFF6D6", 0.85, x + 0.55, y + 1.2);
      const nose = new Path2D(); nose.moveTo(x - 0.75, y + 0.2); nose.lineTo(x + 0.75, y + 0.2); nose.lineTo(x, y + 1); nose.closePath(); g.fillStyle = "#7A4A08"; g.fill(nose);
      const m = new Path2D(); m.moveTo(x - 0.9, y + 1.9); m.quadraticCurveTo(x, y + 2.5, x + 0.9, y + 1.9); stroke(g, m, "#7A4A08", 0.25);
      if (!small) for (const s of [-1, 1]) for (const dy of [-0.2, 0.4]) line(g, "rgba(122,74,8,0.6)", x + s * 1.4, y + 1.2 + dy, x + s * 2.6, y + 0.9 + dy * 1.8, 0.15);
      if (!small) { const k = Math.max(0, sin(t * 1.5 + 0.7)) ** 6; if (k > 0.02) star4(g, x + 3.2, y - 3.6, 2.2 * k, `rgba(255,255,255,${0.95 * k})`); }
    } else if (kind === "selo") {
      // selo de conformidade: duas fitas verdes com a ponta em V, o selo serrilhado verde com anel de
      // pontinhos dourados e o "check" branco no meio; um brilho passa de vez em quando
      const x = 24.5, y = 75.5, R = 5;
      for (const [s, c] of [[-1, "#1E8A4A"], [1, "#16703B"]]) {
        const tl = new Path2D(); tl.moveTo(x + s * 0.6, y + 2); tl.lineTo(x + s * 3.6, y + 10.4); tl.lineTo(x + s * 2.2, y + 9.4); tl.lineTo(x + s * 1.2, y + 11); tl.lineTo(x - s * 0.9, y + 2.8); tl.closePath();
        g.fillStyle = c; g.fill(tl); line(g, "rgba(255,255,255,0.35)", x + s * 0.8, y + 3, x + s * 2.8, y + 9.4, 0.3);
      }
      const burst = new Path2D(); for (let i = 0; i < 40; i++) { const a = i * PI / 20, rr = i % 2 ? R * 0.88 : R; i ? burst.lineTo(x + cos(a) * rr, y + sin(a) * rr) : burst.moveTo(x + cos(a) * rr, y + sin(a) * rr); } burst.closePath();
      g.save(); g.translate(0.35, 0.5); g.fillStyle = "rgba(10,40,20,0.35)"; g.fill(burst); g.restore();
      g.fillStyle = vGrad(g, ["#6FE0A6", "#22A35E", "#13703E"], y - R, y + R); g.fill(burst); stroke(g, burst, "rgba(10,60,30,0.55)", 0.25);
      circle(g, vGrad(g, ["#2FB56C", "#178049"], y - 3.4, y + 3.4), 3.4, x, y);
      if (!small) for (let i = 0; i < 18; i++) { const a = i * PI / 9; circle(g, "#F5C451", 0.2, x + cos(a) * 2.9, y + sin(a) * 2.9); }
      const ck = new Path2D(); ck.moveTo(x - 1.6, y + 0.1); ck.lineTo(x - 0.4, y + 1.3); ck.lineTo(x + 1.8, y - 1.2);
      stroke(g, ck, "rgba(10,60,30,0.6)", 1.3); stroke(g, ck, "#FFFFFF", 0.85);
      g.save(); g.clip(burst); const k = (t * 0.32) % 1.8; line(g, "rgba(255,255,255,0.55)", x - 7 + k * 12, y - 7, x - 10 + k * 12, y + 7, 1.3); g.restore();
    } else if (kind === "seda") {
      // gravata de seda azul: lâmina lisa em cetim (o miolo mais claro), um reflexo que corre de cima a
      // baixo, a trama de pontinhos e o nó com a dobrinha
      const sw = sin(t * 1.9) * 0.8;
      g.save(); g.translate(50, 80); g.scale(1.3, 1.25); g.translate(-50, -80);
      const blade = new Path2D(); blade.moveTo(48.2, 83.4); blade.lineTo(51.8, 83.4); blade.lineTo(54.6 + sw, 94); blade.lineTo(50 + sw, 98.6); blade.lineTo(45.4 + sw, 94); blade.closePath();
      g.fillStyle = hGrad(g, ["#1A4A9E", "#4F8FEA", "#9CCBFF", "#3F7FDC", "#163E8A"], 45, 55); g.fill(blade);
      g.save(); g.clip(blade);
      if (!small) for (let i = 0; i < 6; i++) for (let j = 0; j < 3; j++) circle(g, "rgba(255,255,255,0.16)", 0.28, 47.6 + j * 2.4 + (i % 2) * 1.2 + sw * (i / 6), 85 + i * 2.2);
      const k = (t * 0.45) % 1.6, yy = 78 + k * 16; line(g, "rgba(255,255,255,0.55)", 42, yy + 3, 58, yy - 3, 1.4, false);
      g.fillStyle = "rgba(5,15,50,0.2)"; g.fillRect(50.6 + sw * 0.5, 82, 6, 18); g.restore();
      stroke(g, blade, "rgba(10,30,80,0.5)", 0.35);
      const knot = new Path2D(); knot.moveTo(47, 79.8); knot.lineTo(53, 79.8); knot.lineTo(51.8, 83.8); knot.lineTo(48.2, 83.8); knot.closePath();
      g.fillStyle = vGrad(g, ["#7FB4F7", "#1F55B0"], 79.8, 83.8); g.fill(knot); stroke(g, knot, "rgba(10,30,80,0.5)", 0.35);
      line(g, "rgba(255,255,255,0.5)", 47.8, 80.6, 52.2, 80.6, 0.5); line(g, "rgba(10,30,80,0.35)", 49, 82.6, 51, 82.6, 0.3);
      g.restore();
    } else if (kind === "parlamentar") {
      // broche parlamentar: oval de ouro com o esmalte branco, a faixa verde e amarela atravessando na
      // diagonal e o centro azul com a estrelinha; um brilho corre o broche
      const x = 24.5, y = 77, rx = 5, ry = 3.9;
      const ov = (a, b) => { const p = new Path2D(); p.ellipse(x, y, a, b, 0, 0, 2 * PI); return p; };
      g.save(); g.translate(0.35, 0.5); g.fillStyle = "rgba(60,30,0,0.35)"; g.fill(ov(rx + 0.2, ry + 0.2)); g.restore();
      g.fillStyle = vGrad(g, ["#FFF1BF", "#E9AE34", "#9C6A12"], y - ry, y + ry); g.fill(ov(rx, ry));
      g.fillStyle = vGrad(g, ["#FFFFFF", "#ECEFF5"], y - ry, y + ry); g.fill(ov(rx - 0.9, ry - 0.9));
      g.save(); g.clip(ov(rx - 0.9, ry - 0.9)); g.translate(x, y); g.rotate(-0.55);
      g.fillStyle = "#1E9A48"; g.fillRect(-7, -1.7, 14, 3.4); g.fillStyle = "#FFD23F"; g.fillRect(-7, -0.6, 14, 1.2); g.fillStyle = "rgba(0,0,0,0.12)"; g.fillRect(-7, 0.9, 14, 0.8);
      g.restore();
      circle(g, "#9C6A12", 1.35, x, y); circle(g, "#1F4FB0", 1.1, x, y); g.fillStyle = "#FFFFFF"; g.fill(star5(g, x, y + 0.05, 0.6));
      stroke(g, ov(rx, ry), "rgba(110,70,0,0.6)", small ? 0.7 : 0.3);
      g.save(); g.clip(ov(rx, ry)); const k = (t * 0.3) % 1.8; line(g, "rgba(255,255,255,0.6)", x - 8 + k * 12, y - 6, x - 11 + k * 12, y + 6, 1.3); g.restore();
    } else if (kind === "estrelinha") {
      // estrelinha de boa nota: adesivo de estrela dourada com a borda branca do recorte, carinha sorridente
      // e bochechas; ela balança de leve e pisca um brilho
      const x = 24.5, y = 77.5, R = 5;
      g.save(); g.translate(x, y); g.rotate(sin(t * 1.4) * 0.08);
      g.save(); g.translate(0.35, 0.5); g.fillStyle = "rgba(60,30,0,0.3)"; g.fill(star5(g, 0, 0, R + 0.7)); g.restore();
      g.fillStyle = "#FFFFFF"; g.fill(star5(g, 0, 0, R + 0.7)); stroke(g, star5(g, 0, 0, R + 0.7), "rgba(150,140,180,0.45)", 0.25);
      g.fillStyle = vGrad(g, ["#FFF4B8", "#FFD23F", "#E9A21A"], -R, R); g.fill(star5(g, 0, 0, R));
      g.fillStyle = "rgba(255,255,255,0.45)"; g.fill(star5(g, -0.8, -1, R * 0.32));
      for (const s of [-1, 1]) { circle(g, "#5A3A06", 0.45, s * 1.1, 0.1); circle(g, "rgba(255,130,150,0.55)", 0.6, s * 2, 1.2); }
      const m = new Path2D(); m.moveTo(-0.9, 1.1); m.quadraticCurveTo(0, 2.1, 0.9, 1.1); stroke(g, m, "#5A3A06", 0.3);
      g.restore();
      const k = Math.max(0, sin(t * 1.8)) ** 6; if (k > 0.02) star4(g, x + 4, y - 4, 2.2 * k, `rgba(255,255,255,${0.95 * k})`);
    } else if (kind === "pendrive") {
      // pendrive no cordão: cordão azul vindo dos dois ombros até a argola; o pendrive de plástico índigo com
      // a faixa menta, o conector de metal com os furinhos e a luzinha que pisca
      const cx = 50, cy = 83;
      for (const [x0, c1x, ex] of [[14, 26, cx - 0.8], [86, 74, cx + 0.8]]) {
        const p = new Path2D(); p.moveTo(x0, 66); p.quadraticCurveTo(c1x, 86, ex, cy);
        stroke(g, p, "rgba(10,20,60,0.2)", small ? 2.8 : 2.3); stroke(g, p, "#1F4FB0", small ? 2.2 : 1.7); stroke(g, p, "#6FB0FF", small ? 0.9 : 0.6);
      }
      g.save(); g.translate(cx, cy); g.rotate(sin(t * 1.8) * 0.07); g.scale(1.25, 1.25);
      g.beginPath(); g.arc(0, 0.4, 0.9, 0, 2 * PI); g.strokeStyle = "#B9BFCC"; g.lineWidth = 0.45; g.stroke();
      const body = new Path2D(); body.roundRect(-2.3, 1.3, 4.6, 8.4, 1.2);
      g.save(); g.translate(0.3, 0.4); g.fillStyle = "rgba(20,20,60,0.25)"; g.fill(body); g.restore();
      g.fillStyle = hGrad(g, ["#2A209E", "#5247E0", "#3A2FC4", "#1D1670"], -2.3, 2.3); g.fill(body);
      g.save(); g.clip(body); g.fillStyle = MINT; g.fillRect(-2.3, 5, 4.6, 1.3); g.restore();
      stroke(g, body, "rgba(10,8,40,0.6)", 0.25);
      circle(g, "#D7D9E8", 0.45, 0, 2.3);
      line(g, "rgba(255,255,255,0.35)", -1.5, 2.6, -1.5, 8.8, 0.35);
      circle(g, (t * 1.2) % 1 < 0.5 ? MINT : "#2E6B57", 0.4, 1.1, 8.3);
      const plug = new Path2D(); plug.rect(-1.6, 9.7, 3.2, 3.4); g.fillStyle = hGrad(g, ["#8E95A6", "#F3F6FA", "#9AA3B4"], -1.6, 1.6); g.fill(plug); stroke(g, plug, "rgba(40,45,60,0.55)", 0.2);
      g.fillStyle = "#3C4054"; g.fillRect(-1, 10.6, 0.7, 0.7); g.fillRect(0.3, 10.6, 0.7, 0.7);
      g.restore();
    } else if (kind === "postits") {
      // post-its colados no peito: amarelo, rosa e menta, cada um com a faixa de cola, os rabisquinhos e a
      // pontinha levantada; o de baixo está meio solto e balança
      const note = (x, y, ang, c1, c2, ink) => {
        g.save(); g.translate(x, y); g.rotate(ang);
        g.fillStyle = "rgba(40,30,60,0.2)"; g.fillRect(-2.6, 0.5, 6, 6);
        const p = new Path2D(); p.moveTo(-3, 0); p.lineTo(3, 0); p.lineTo(3, 4.6); p.lineTo(1.6, 6); p.lineTo(-3, 6); p.closePath();
        g.fillStyle = vGrad(g, [c1, c2], 0, 6); g.fill(p);
        g.fillStyle = "rgba(0,0,0,0.07)"; g.fillRect(-3, 0, 6, 1.3);
        const curl = new Path2D(); curl.moveTo(3, 4.6); curl.lineTo(1.6, 6); curl.lineTo(1.9, 4.9); curl.closePath(); g.fillStyle = c1; g.fill(curl); stroke(g, curl, "rgba(0,0,0,0.15)", 0.15);
        stroke(g, p, "rgba(60,40,20,0.3)", 0.2);
        if (!small) for (const [yy, w] of [[2.3, 4.2], [3.5, 3.2], [4.7, 2.4]]) line(g, ink, -2.2, yy, -2.2 + w, yy, 0.3);
        g.restore();
      };
      note(21, 72.5, -0.18, "#FFF3A0", "#FFD84A", "rgba(120,90,10,0.55)");
      note(27.6, 74.6, 0.14, "#FFD1E3", "#FF8FB8", "rgba(150,40,80,0.5)");
      note(22.6, 79.4, -0.06 + sin(t * 2.2) * 0.14, "#D2FBE6", "#7EE0B8", "rgba(20,100,70,0.55)");
    } else if (kind === "perolas") {
      // colar de pérolas: o fio desce dos ombros em U por baixo da boca, cada pérola com o reflexo rosado e o
      // pontinho de luz; a do meio é maior, presa num fecho dourado, e um brilho corre o fio devagar
      const bz = (u) => { const a = (1 - u) ** 3, b = 3 * u * (1 - u) ** 2, c = 3 * u * u * (1 - u), d = u ** 3; return [a * 16 + b * 20 + c * 80 + d * 84, a * 66 + b * 92 + c * 92 + d * 66]; };
      const N = small ? 11 : 15, run = (t * 0.25) % 1.4, r = small ? 2.4 : 1.95;
      const pearl = (px, py, rr, k) => {
        circle(g, "rgba(40,30,70,0.22)", rr, px + 0.3, py + 0.4);
        const rg = g.createRadialGradient(px - rr * 0.35, py - rr * 0.4, 0.1, px, py, rr); rg.addColorStop(0, "#FFFFFF"); rg.addColorStop(0.45, "#F6E8F0"); rg.addColorStop(0.8, "#D8C4E0"); rg.addColorStop(1, "#A996BE");
        circle(g, rg, rr, px, py);
        g.beginPath(); g.arc(px, py, rr, 0, 2 * PI); g.strokeStyle = "rgba(70,55,110,0.7)"; g.lineWidth = small ? 0.5 : 0.3; g.stroke();
        g.beginPath(); g.arc(px, py, rr * 0.7, 0.3, 1.6); g.strokeStyle = "rgba(255,200,225,0.7)"; g.lineWidth = 0.25; g.stroke();
        circle(g, `rgba(255,255,255,${0.7 + 0.3 * k})`, rr * (0.28 + 0.15 * k), px - rr * 0.35, py - rr * 0.4);
      };
      for (let i = 0; i <= N; i++) { const u = i / N, [px, py] = bz(u); pearl(px, py, r, Math.max(0, 1 - abs(u - run) * 8)); }
      const [mx, my] = bz(0.5);
      circle(g, "#9C6A12", 1.2, mx, my + 2); circle(g, vGrad(g, GOLD3, my + 0.9, my + 3), 1, mx, my + 2);
      pearl(mx, my + 5.4, small ? 3.6 : 3.2, Math.max(0, 1 - abs(0.5 - run) * 8));
    } else if (kind === "lenco") {
      // lenço de seda amarrado no pescoço: faixa laranja e coral com bolinhas, o nó na frente e as duas
      // pontas soltas que esvoaçam devagar
      const sw = sin(t * 1.7) * 0.9, sw2 = sin(t * 1.7 + 1) * 0.9;
      const silk = (p, x0, x1) => {
        g.fillStyle = hGrad(g, ["#FF6F5C", "#FFA94D", "#FF6F5C"], x0, x1); g.fill(p);
        g.save(); g.clip(p); if (!small) for (let i = 0; i < 30; i++) circle(g, "rgba(255,250,235,0.75)", 0.42, x0 + ((i * 2.7) % (x1 - x0)), 78 + ((i * 3.1) % 20));
        g.fillStyle = "rgba(120,20,10,0.15)"; g.fillRect(x0, 85.5, x1 - x0, 20); g.restore(); stroke(g, p, "rgba(140,40,20,0.45)", 0.3);
      };
      const band = new Path2D(); band.moveTo(28, 79.6); band.quadraticCurveTo(50, 84.2, 72, 79.6); band.quadraticCurveTo(73.2, 81.6, 72.2, 83.4); band.quadraticCurveTo(50, 88, 27.8, 83.4); band.quadraticCurveTo(26.8, 81.6, 28, 79.6); band.closePath();
      silk(band, 26, 74);
      const tl = new Path2D(); tl.moveTo(48.6, 85.4); tl.quadraticCurveTo(45 + sw, 89, 42.4 + sw, 94.6); tl.quadraticCurveTo(45.4 + sw, 93.6, 47.4 + sw * 0.5, 94.8); tl.quadraticCurveTo(48.4, 90, 50.4, 86); tl.closePath();
      const tr = new Path2D(); tr.moveTo(51.4, 85.4); tr.quadraticCurveTo(55 + sw2, 89, 58 + sw2, 93.8); tr.quadraticCurveTo(55 + sw2, 93.2, 53 + sw2 * 0.5, 94.4); tr.quadraticCurveTo(51.6, 90, 49.6, 86); tr.closePath();
      silk(tl, 40, 52); silk(tr, 48, 60);
      const knot = new Path2D(); knot.ellipse(50, 85.2, 2.4, 2, 0, 0, 2 * PI); silk(knot, 47.6, 52.4);
      line(g, "rgba(255,255,255,0.45)", 48.8, 84.4, 50.6, 84.2, 0.35);
    } else if (kind === "sininho") {
      // sininho dourado num lacinho vermelho: o sino balança de um lado para o outro com o badalo atrasado, e
      // quando chega na ponta aparecem um brilho e as ondinhas do "plim"
      const x = 50, y = 82.6, ph = sin(t * 3), ang = ph * 0.3, red = "#E0475E", dk = "#A8233A";
      for (const s of [-1, 1]) {
        const p = new Path2D(); p.moveTo(x, y); p.quadraticCurveTo(x + s * 1.8, y - 2.1, x + s * 3.4, y - 1.5); p.quadraticCurveTo(x + s * 3.9, y, x + s * 3.4, y + 1.5); p.quadraticCurveTo(x + s * 1.8, y + 2.1, x, y); p.closePath();
        g.fillStyle = hGrad(g, s < 0 ? [red, dk] : [dk, red], s < 0 ? x - 3.6 : x, s < 0 ? x : x + 3.6); g.fill(p);
      }
      circle(g, dk, 0.9, x, y);
      g.save(); g.translate(x, y + 0.8); g.rotate(ang);
      g.beginPath(); g.arc(0, 0.6, 0.6, 0, 2 * PI); g.strokeStyle = "#C98A12"; g.lineWidth = 0.35; g.stroke();
      circle(g, "#9C6A12", 0.85, -ang * 2, 6.8);
      const bell = new Path2D(); bell.moveTo(-2.9, 5.6); bell.quadraticCurveTo(-2.6, 1.4, 0, 1.3); bell.quadraticCurveTo(2.6, 1.4, 2.9, 5.6); bell.lineTo(3.5, 6.4); bell.lineTo(-3.5, 6.4); bell.closePath();
      g.fillStyle = vGrad(g, ["#FFF4CC", "#F5C451", "#C98A12"], 1.3, 6.4); g.fill(bell); stroke(g, bell, "rgba(110,70,0,0.6)", small ? 0.6 : 0.3);
      line(g, "rgba(150,95,10,0.7)", -3.1, 5.6, 3.1, 5.6, 0.3);
      line(g, "rgba(255,255,255,0.7)", -1.4, 2.6, -1.9, 4.8, 0.45);
      g.restore();
      const k = Math.max(0, abs(ph) - 0.85) / 0.15;
      if (k > 0 && !small) { const s = ph > 0 ? -1 : 1; for (let j = 0; j < 2; j++) line(g, rgba(GOLD, k), x + s * (4.6 + j * 1.4), y + 4 - j * 0.8, x + s * (5.6 + j * 1.4), y + 3 - j * 0.8, 0.35); star4(g, x + s * 3.4, y + 2.4, 1.6 * k, `rgba(255,255,255,${k})`); }
    }
  }

  // ---- item na mão esquerda (desenhado antes da mão, que fica por cima segurando)
  // Reage ao humor: varinha solta faíscas e o troféu brilha quando ele comemora; o balão escapa quando
  // ele fica triste e volta descendo depois.
  function drawHeld(g, kind, h, ang, t, small, mood, tm, prevMood) {
    const [x, y] = h, tiltA = clamp(ang, -0.6, 0.6) * 0.5, party = /^(happy|ecstatic|proud|present|wave)$/.test(mood || "");
    g.save(); g.translate(x, y); g.rotate(tiltA); g.scale(small ? 1.45 : 1.8, small ? 1.45 : 1.8);
    // o centro do punho fica um pouco para fora e para cima do pulso: leva a pegada até lá
    g.translate(-1.8, -1.2);
    // ponto da pegada: o punho fechado cobre um círculo de raio ~2 em volta de GX, GY; o que se segura
    // termina aqui e o corpo do objeto sai para cima e para fora
    const GX = 0.9, GY = 1.5;
    if (kind === "lapis") drawPencilAt(g, -3.6, -13, GX + 0.3, GY + 0.6, small ? 1.4 : 1.15);
    else if (kind === "marcatexto") {
      // marca-texto gordinho segurado pelo pé: tampinha de trás escondida no punho, corpo amarelo neon com
      // janela de tinta e a tampa verde com clipe em cima; na festa ele risca uma faixa no ar
      g.save(); g.translate(GX, GY); g.rotate(-0.38);
      const OUTL = "rgba(70,60,10,0.85)";
      const end = new Path2D(); end.roundRect(-2, -2.2, 4, 3, 1); g.fillStyle = "#2E9E72"; g.fill(end); stroke(g, end, OUTL, 0.4);
      const body = new Path2D(); body.moveTo(-2.3, -1.6); body.lineTo(-2.3, -11.6); body.quadraticCurveTo(-2.3, -12.4, -1.5, -12.4); body.lineTo(1.5, -12.4); body.quadraticCurveTo(2.3, -12.4, 2.3, -11.6); body.lineTo(2.3, -1.6); body.closePath();
      g.fillStyle = hGrad(g, ["#D9C21F", "#FFF59A", "#F7E436", "#C9B010"], -2.3, 2.3); g.fill(body); stroke(g, body, OUTL, 0.45);
      const win = new Path2D(); win.roundRect(-1, -9.6, 2, 6.4, 1); g.fillStyle = "rgba(255,255,255,0.5)"; g.fill(win);
      if (!small) { g.fillStyle = "rgba(200,170,10,0.55)"; g.fillRect(-0.7, -6.4, 1.4, 2.8); }
      const cap = new Path2D(); cap.moveTo(-2.6, -11.4); cap.lineTo(-2.6, -15.6); cap.quadraticCurveTo(-2.6, -17.6, 0, -17.6); cap.quadraticCurveTo(2.6, -17.6, 2.6, -15.6); cap.lineTo(2.6, -11.4); cap.closePath();
      g.fillStyle = hGrad(g, ["#1F7A5A", "#7EE0B8", "#2E9E72", "#1F7A5A"], -2.6, 2.6); g.fill(cap); stroke(g, cap, "rgba(15,70,50,0.9)", 0.45);
      const clip = new Path2D(); clip.roundRect(2, -16.4, 1.2, 5.4, 0.6); g.fillStyle = "#1F7A5A"; g.fill(clip); stroke(g, clip, "rgba(15,70,50,0.9)", 0.3);
      line(g, "rgba(15,70,50,0.6)", -2.4, -12.2, 2.4, -12.2, 0.3);
      line(g, "rgba(255,255,255,0.75)", -1.3, -16.4, -1.3, -13, 0.45); line(g, "rgba(255,255,255,0.7)", -1.6, -11, -1.6, -2.4, 0.45);
      g.restore();
      if (party) { const k = (t * 0.8) % 1; g.save(); g.globalAlpha = 0.5 * (1 - k); line(g, "#FFE94D", -8, -17, -8 - k * 6, -15 + k * 4, 2.2); g.restore(); }
    } else if (kind === "livro") {
      // livrinho de capa dura: capa, lombada, bloco de páginas, letreiro dourado e a fitinha
      g.save(); g.rotate(-0.14);
      const pages = new Path2D(); pages.moveTo(3.6, -10.6); pages.lineTo(5, -9.6); pages.lineTo(5, 0.2); pages.lineTo(3.6, -0.6); pages.closePath(); g.fillStyle = "#F4EFE4"; g.fill(pages);
      for (let i = 1; i < 4; i++) line(g, "rgba(150,130,100,0.35)", 3.8 + i * 0.3, -10 + i * 0.25, 3.8 + i * 0.3, -0.4 + i * 0.2, 0.12);
      const top = new Path2D(); top.moveTo(-4.6, -11); top.lineTo(3.6, -11); top.lineTo(5, -10); top.lineTo(-3.2, -10); top.closePath(); g.fillStyle = "#F4EFE4"; g.fill(top);
      const cov = new Path2D(); cov.roundRect(-4.8, -11, 8.6, 10.6, 0.9); g.fillStyle = vGrad(g, ["#EE6B80", "#C42F4B", "#961D37"], -11, -0.4); g.fill(cov);
      g.fillStyle = "rgba(0,0,0,0.18)"; g.fillRect(-4.8, -11, 1.4, 10.6);
      const lbl = new Path2D(); lbl.roundRect(-2.6, -8.6, 5, 3, 0.5); g.fillStyle = "rgba(255,235,180,0.95)"; g.fill(lbl);
      line(g, "#C98A12", -2, -7.6, 1.8, -7.6, 0.3); line(g, "#C98A12", -2, -6.6, 1, -6.6, 0.3);
      line(g, "#F5C451", -3.6, -2.4, 3, -2.4, 0.35);
      const rib = new Path2D(); rib.moveTo(0.4, -0.4); rib.lineTo(1.6, -0.4); rib.lineTo(1.6, 2.6 + sin(t * 2) * 0.3); rib.lineTo(1, 2 + sin(t * 2) * 0.3); rib.lineTo(0.4, 2.6 + sin(t * 2) * 0.3); rib.closePath(); g.fillStyle = MINT; g.fill(rib);
      g.restore();
    } else if (kind === "balao") {
      // balão de látex: brilho, nó e barbante cacheado; escapa no triste e volta descendo depois
      let lift = 0, alpha = 1;
      if (mood === "sad") { const u = clamp(tm / 2.2, 0, 1); lift = -u * u * 70; alpha = 1 - clamp((u - 0.7) / 0.3, 0, 1); }
      else if (prevMood === "sad" && tm < 1.2) { const u = 1 - sstep(tm / 1.2); lift = -u * 60; }
      const bx = -5 + sin(t * 1.3) * 2.2, by = -25 + sin(t * 1.7) * 1.4 + lift;
      if (alpha > 0.01) {
        g.save(); g.globalAlpha = alpha;
        const str = new Path2D(); str.moveTo(0, -1);
        if (lift > -1) { for (let i = 1; i <= 10; i++) { const u = i / 10, sx = (bx) * u + sin(u * 12 + t * 2) * 0.7 * (1 - u), sy = -1 + (by + 7 + 1) * u; str.lineTo(sx, sy); } }
        else { str.moveTo(bx, by + 7); str.quadraticCurveTo(bx + 1.5, by + 11, bx - 0.5, by + 15); }
        stroke(g, str, "rgba(60,50,110,0.6)", 0.35);
        g.beginPath(); g.ellipse(bx, by, 5.4, 6.4, sin(t * 1.3) * 0.12, 0, 2 * PI);
        const rg = g.createRadialGradient(bx - 2, by - 2.6, 0.4, bx, by, 7.4); rg.addColorStop(0, "#FFB1C0"); rg.addColorStop(0.5, "#F0476A"); rg.addColorStop(1, "#B81F3E"); g.fillStyle = rg; g.fill();
        const k = new Path2D(); k.moveTo(bx - 1, by + 7.2); k.lineTo(bx + 1, by + 7.2); k.lineTo(bx, by + 6); k.closePath(); g.fillStyle = "#B81F3E"; g.fill(k);
        g.beginPath(); g.ellipse(bx - 2.2, by - 2.8, 1.2, 2.1, -0.5, 0, 2 * PI); g.fillStyle = "rgba(255,255,255,0.6)"; g.fill();
        circle(g, "rgba(255,255,255,0.5)", 0.5, bx + 2.4, by + 2.6);
        g.restore();
      }
    } else if (kind === "varinha") {
      // varinha: haste preta com cabo de anéis dourados, estrela com halo e rastro de faíscas
      g.translate(-1.3, -4.8);
      line(g, "#1E1B2E", 2.2, 6.4, -4.4, -10.8, 1.4); line(g, "#FFFFFF", -3.6, -8.8, -4.4, -10.8, 1.4);
      for (const u of [0.05, 0.16]) line(g, GOLD, 2.2 - 6.6 * u - 0.8, 6.4 - 17.2 * u + 0.3, 2.2 - 6.6 * u + 0.8, 6.4 - 17.2 * u - 0.3, 0.6);
      const sx = -4.8, sy = -12.8, r = 3.2 + 0.4 * sin(t * 6);
      const halo = g.createRadialGradient(sx, sy, 0.5, sx, sy, r * 2.4); halo.addColorStop(0, "rgba(255,230,140,0.7)"); halo.addColorStop(1, "rgba(255,230,140,0)");
      g.fillStyle = halo; g.beginPath(); g.arc(sx, sy, r * 2.4, 0, 2 * PI); g.fill();
      g.save(); g.translate(sx, sy); g.rotate(t * 1.5); g.fillStyle = vGrad(g, GOLD3, -r, r); g.fill(star5(g, 0, 0, r)); g.fillStyle = "rgba(255,255,255,0.55)"; g.fill(star5(g, -0.3, -0.3, r * 0.4)); g.restore();
      const n = party ? 10 : 4;
      for (let i = 0; i < n; i++) { const ph = (t * (party ? 1.6 : 0.9) + i / n) % 1, a = i * 1.7 + t; star4(g, sx + cos(a) * (3 + ph * (party ? 14 : 7)), sy + sin(a) * (3 + ph * (party ? 14 : 7)) - ph * 3, (party ? 1.8 : 1.3) * (1 - ph), i % 3 === 0 ? `rgba(126,224,184,${1 - ph})` : `rgba(255,240,180,${1 - ph})`); }
    } else if (kind === "trofeu") {
      // troféu: taça com alças curvas, nó no pé, base de madeira com plaquinha e estrela; brilha na comemoração
      g.translate(-1.6, 1.4);
      for (const s of [-1, 1]) { const hd = new Path2D(); hd.moveTo(s * 4.4, -12.6); hd.bezierCurveTo(s * 8, -12.6, s * 7.4, -7.6, s * 3.2, -7.4); stroke(g, hd, "#B57A0C", 1.2); stroke(g, hd, "#F5C451", 0.5); }
      const cup = new Path2D(); cup.moveTo(-4.8, -14); cup.lineTo(4.8, -14); cup.bezierCurveTo(4.8, -8, 2.6, -6, 0.9, -5.8); cup.lineTo(0.9, -4.2); cup.lineTo(-0.9, -4.2); cup.lineTo(-0.9, -5.8); cup.bezierCurveTo(-2.6, -6, -4.8, -8, -4.8, -14); cup.closePath();
      g.fillStyle = hGrad(g, ["#C98A12", "#FFF1BF", "#F5C451", "#B57A0C"], -4.8, 4.8); g.fill(cup);
      g.beginPath(); g.ellipse(0, -14, 4.8, 1, 0, 0, 2 * PI); g.fillStyle = "#9C6A12"; g.fill();
      circle(g, "#E9AE34", 1, 0, -4.4);
      const base = new Path2D(); base.roundRect(-3.4, -3.6, 6.8, 2.8, 0.6); g.fillStyle = vGrad(g, ["#8A5A2B", "#5A3D20"], -3.6, -0.8); g.fill(base);
      const plate = new Path2D(); plate.roundRect(-1.8, -3, 3.6, 1.4, 0.3); g.fillStyle = "#F5C451"; g.fill(plate);
      g.fillStyle = "rgba(160,100,10,0.8)"; g.fill(star5(g, 0, -10.4, 2));
      if (party) { const k = abs(sin(t * 3)); star4(g, -3, -12.4, 2.6 * k, `rgba(255,255,255,${k})`); star4(g, 5.5, -15.5, 1.6 * (1 - k), `rgba(255,240,180,${1 - k})`); }
    } else if (kind === "pasta") {
      // pasta executiva de couro: alça na mão, fecho dourado, costura e cantos reforçados
      const handle = new Path2D(); handle.moveTo(-2.6, 0.6); handle.quadraticCurveTo(-2.6, -2, 0, -2); handle.quadraticCurveTo(2.6, -2, 2.6, 0.6); stroke(g, handle, "#3A2410", 1.3);
      const body = new Path2D(); body.roundRect(-7, 0.6, 14, 10, 1.4); g.fillStyle = vGrad(g, ["#8A5A2B", "#6B4320", "#4E2F15"], 0.6, 10.6); g.fill(body); stroke(g, body, "#2E1A08", 0.4);
      const flap = new Path2D(); flap.roundRect(-7, 0.6, 14, 3.6, 1.2); g.fillStyle = vGrad(g, ["#9C6A35", "#7A4E26"], 0.6, 4.2); g.fill(flap);
      g.save(); g.setLineDash([0.6, 0.5]); const st = new Path2D(); st.roundRect(-6.2, 1.4, 12.4, 8.4, 1); stroke(g, st, "rgba(255,226,180,0.55)", 0.2); g.restore();
      for (const x of [-4, 4]) { const c = new Path2D(); c.roundRect(x - 1, 3.2, 2, 1.8, 0.4); g.fillStyle = vGrad(g, GOLD3, 3.2, 5); g.fill(c); }
      for (const [x, y] of [[-6.4, 1.2], [6.4, 1.2], [-6.4, 10], [6.4, 10]]) circle(g, "#E9AE34", 0.5, x, y);
      line(g, "rgba(255,255,255,0.25)", -5.6, 5.6, 2, 5.6, 0.4);
    } else if (kind === "radio") {
      // rádio da viatura: corpo preto emborrachado, antena, alto-falante, visor verde com sinal e a luz vermelha
      // de transmitindo (pisca mais rápido na festa, com as ondinhas saindo da antena)
      g.save(); g.translate(GX - 0.6, GY - 4); g.rotate(-0.12 + (party ? sin(t * 6) * 0.08 : 0));
      line(g, "#2A2C38", 1.6, -9, 1.8, -17, 0.9); circle(g, "#2A2C38", 0.75, 1.8, -17.2);
      const kn = new Path2D(); kn.roundRect(-2.4, -12, 1.8, 2.2, 0.5); g.fillStyle = "#3C4054"; g.fill(kn);
      const body = new Path2D(); body.roundRect(-3.2, -10, 6.4, 14, 1.4); g.fillStyle = hGrad(g, ["#1C1E28", "#3C4054", "#1C1E28"], -3.2, 3.2); g.fill(body); stroke(g, body, "rgba(0,0,0,0.6)", 0.3);
      for (let i = 0; i < 4; i++) line(g, "rgba(140,145,170,0.7)", -2, -7.6 + i * 1.1, 2, -7.6 + i * 1.1, 0.45);
      const sc = new Path2D(); sc.roundRect(-2.2, -2.6, 4.4, 2.4, 0.4); g.fillStyle = "#7EE0B8"; g.fill(sc);
      for (let i = 0; i < 4; i++) { g.fillStyle = "#1D4A3A"; g.fillRect(-1.6 + i * 0.9, -0.6 - (i + 1) * 0.35, 0.6, (i + 1) * 0.35); }
      const on = (t * (party ? 4 : 1.2)) % 1 < 0.5; if (on && !small) circle(g, "rgba(255,92,77,0.3)", 1.4, -1.8, -9); circle(g, on ? "#FF5C4D" : "#6B1E1A", 0.6, -1.8, -9);
      line(g, "rgba(255,255,255,0.3)", -2.6, -9.2, -2.6, 3, 0.35);
      if (party && !small) for (let i = 0; i < 3; i++) { const ph = (t * 1.5 + i / 3) % 1; g.beginPath(); g.arc(1.8, -17.2, 1.5 + ph * 5, -PI * 0.85, -PI * 0.15); g.strokeStyle = `rgba(126,224,184,${0.8 * (1 - ph)})`; g.lineWidth = 0.4; g.stroke(); }
      g.restore();
    } else if (kind === "maca") {
      // maçã do professor: vermelha brilhante, cabinho e folha menta; na festa solta estrelinhas
      g.save(); g.translate(0, -5); g.rotate(sin(t * 1.3) * 0.05);
      const ap = new Path2D(); ap.moveTo(0, -4.2); ap.bezierCurveTo(2.6, -6.4, 6.6, -4.6, 6, 0.6); ap.bezierCurveTo(5.6, 4.8, 2.4, 6.6, 0, 5.2); ap.bezierCurveTo(-2.4, 6.6, -5.6, 4.8, -6, 0.6); ap.bezierCurveTo(-6.6, -4.6, -2.6, -6.4, 0, -4.2); ap.closePath();
      const rg = g.createRadialGradient(-2, -2, 0.5, 0, 0, 7.5); rg.addColorStop(0, "#FF7A7A"); rg.addColorStop(0.55, "#E0303F"); rg.addColorStop(1, "#9C1424"); g.fillStyle = rg; g.fill(ap);
      stroke(g, ap, "rgba(90,10,20,0.5)", 0.3);
      line(g, "#6B4320", 0, -4, 0.6, -7.4, 0.7);
      const lf = new Path2D(); lf.moveTo(0.6, -6.4); lf.quadraticCurveTo(3.4, -9.2, 5.2, -7); lf.quadraticCurveTo(3, -5.2, 0.6, -6.4); lf.closePath(); g.fillStyle = vGrad(g, ["#7EE0B8", "#2E9E72"], -9, -5); g.fill(lf);
      g.beginPath(); g.ellipse(-2.8, -1.6, 1.1, 1.8, -0.4, 0, 2 * PI); g.fillStyle = "rgba(255,255,255,0.55)"; g.fill();
      if (party) for (let i = 0; i < 3; i++) { const ph = (t * 0.9 + i / 3) % 1; star4(g, -6 + i * 6, -9 - ph * 4, 1.4 * sin(PI * ph), `rgba(255,255,255,${sin(PI * ph)})`); }
      g.restore();
    } else if (kind === "calculadora") {
      // calculadora de mesa: visor verde com os dígitos mudando e as teclas (na festa ele aperta sem parar)
      g.save(); g.translate(-2.8, 0); g.rotate(-0.1);
      const body = new Path2D(); body.roundRect(-4.4, -12, 8.8, 13.6, 1.4); g.fillStyle = vGrad(g, ["#4A5068", "#2C3042"], -12, 1.6); g.fill(body); stroke(g, body, "rgba(0,0,0,0.5)", 0.3);
      const sc = new Path2D(); sc.roundRect(-3.4, -10.8, 6.8, 3, 0.5); g.fillStyle = "#C8E6C0"; g.fill(sc);
      const n = Math.floor(t * (party ? 6 : 1.5));
      for (let i = 0; i < 4; i++) { g.fillStyle = ((n + i * 3) % 5) !== 0 ? "#2A3A2A" : "rgba(42,58,42,0.25)"; g.fillRect(-2.6 + i * 1.5, -10, 1, 1.5); }
      for (let r = 0; r < 4; r++) for (let c = 0; c < 4; c++) {
        const key = new Path2D(); key.roundRect(-3.5 + c * 1.85, -6.6 + r * 1.85, 1.45, 1.35, 0.35);
        const press = party && ((Math.floor(t * 6) + r * 4 + c) % 7 === 0);
        g.fillStyle = press ? "#7EE0B8" : c === 3 ? "#F5B83D" : r === 3 && c === 0 ? "#FF8FA3" : "#D7DAE6"; g.fill(key);
      }
      line(g, "rgba(255,255,255,0.3)", -3.8, -11.4, 3.6, -11.4, 0.35);
      g.restore();
    } else if (kind === "bandeira") {
      // bandeira do Brasil num mastro com ponta dourada: tremula para fora (longe do rosto); na festa,
      // ele agita e o pano tremula mais rápido
      const wav = party ? 1 : 0.55, sp = party ? 7.5 : 4.2;
      g.translate(-0.5, -3.4); g.save(); g.rotate(party ? sin(t * 5) * 0.16 : sin(t * 1.4) * 0.04);
      line(g, "#6B4320", 1.4, 5, -1.2, -21.6, 1.1); line(g, "rgba(255,255,255,0.35)", 1.1, 3.6, -1.3, -20.4, 0.3);
      circle(g, "#B57A0C", 1.15, -1.3, -22.6); circle(g, "#FFE08A", 0.7, -1.5, -22.9);
      const X0 = -1.4, Y0 = -21.4, W = 12.4, H = 8.6, N = 14;
      const off = (u) => sin(t * sp - u * 5.2) * 0.9 * wav * u + u * 1.2;   // o pano cai um tico na ponta
      const P = (u, v) => [X0 - u * W, Y0 + v * H + off(u)];
      const flag = new Path2D(); flag.moveTo(...P(0, 0));
      for (let i = 1; i <= N; i++) flag.lineTo(...P(i / N, 0));
      for (let i = N; i >= 0; i--) flag.lineTo(...P(i / N, 1));
      flag.closePath();
      g.save(); g.clip(flag);
      g.fillStyle = "#1E9A48"; g.fillRect(X0 - W - 2, Y0 - 3, W + 3, H + 8);
      const rh = new Path2D(); rh.moveTo(...P(0.08, 0.5)); rh.lineTo(...P(0.5, 0.1)); rh.lineTo(...P(0.92, 0.5)); rh.lineTo(...P(0.5, 0.9)); rh.closePath(); g.fillStyle = "#FFD23F"; g.fill(rh);
      const [cx, cy] = P(0.5, 0.5); circle(g, "#1F3F9A", 2.5, cx, cy);
      g.save(); g.beginPath(); g.arc(cx, cy, 2.5, 0, 2 * PI); g.clip(); const band = new Path2D(); band.arc(cx + 0.6, cy + 4.6, 5, PI * 1.18, PI * 1.82); stroke(g, band, "#FFFFFF", 0.55);
      for (const [dx, dy] of [[-1.2, 0.9], [0.4, 1.4], [1.3, 0.6], [-0.4, 0.5], [0.9, 1.9]]) circle(g, "#FFFFFF", 0.17, cx + dx, cy + dy); g.restore();
      // dobras do pano: faixas de luz e sombra que andam com a onda
      for (let i = 0; i < N; i++) { const u = (i + 0.5) / N, s = cos(t * sp - u * 5.2) * wav; const [x, y] = P(u, 0); g.fillStyle = s > 0 ? `rgba(255,255,255,${0.16 * s})` : `rgba(0,0,0,${-0.16 * s})`; g.fillRect(x - W / N / 2 - 0.05, y - 1, W / N + 0.1, H + 2); }
      g.restore();
      stroke(g, flag, "rgba(10,60,30,0.35)", 0.25);
      g.restore();
    } else if (kind === "prancheta") {
      // prancheta de plantão: placa de madeira, presilha de metal, a ficha com a cruz, linhas e o
      // traçado do batimento que vai se desenhando
      g.save(); g.rotate(-0.18);
      const board = new Path2D(); board.roundRect(-7.4, -15.6, 10.6, 14.2, 1.1); g.fillStyle = vGrad(g, ["#C08453", "#965A31", "#6D3D1F"], -15.6, -1.4); g.fill(board); stroke(g, board, "rgba(60,30,10,0.5)", 0.3);
      const paper = new Path2D(); paper.rect(-6.4, -13.4, 8.6, 11.2); g.fillStyle = "#FFFDF8"; g.fill(paper);
      g.fillStyle = "#E0475E"; g.fillRect(-5.6, -12.4, 0.8, 2.4); g.fillRect(-6.4, -11.6, 2.4, 0.8);
      for (const y of [-7.6, -6.2, -4.8, -3.4]) line(g, "rgba(120,140,170,0.55)", -5.6, y, y === -3.4 ? -1 : 1.4, y, 0.3);
      const k = party ? (t * 0.9) % 1 : 1, ecg = new Path2D(); ecg.moveTo(-3.6, -10.8);
      for (const [x, y] of [[-2.4, -10.8], [-1.8, -12.4], [-1.1, -9.4], [-0.5, -11], [0.1, -10.8], [1.6, -10.8]]) { if ((x + 3.6) / 5.2 <= k) ecg.lineTo(x, y); }
      stroke(g, ecg, "#E0475E", 0.35);
      const clip = new Path2D(); clip.roundRect(-4.2, -16.6, 4.6, 2.4, 0.6); g.fillStyle = vGrad(g, ["#F3F6FA", "#AEB6C4", "#6E7588"], -16.6, -14.2); g.fill(clip);
      circle(g, "#6E7588", 0.45, -1.9, -16.1);
      g.restore();
    } else if (kind === "martelo") {
      // martelo de juiz: cabo torneado de madeira e a cabeça em barril com anéis dourados; na
      // comemoração ele bate ("ordem!") num ritmo animado
      const knock = party ? -abs(sin(t * 6)) * 0.55 : 0;
      g.translate(-0.8, -3.8); g.save(); g.rotate(knock);
      line(g, "#5A3D20", 1.6, 5.2, -2.4, -8, 1.5); line(g, "#9C6A35", 1.4, 4.6, -2.2, -7.4, 0.7);
      circle(g, "#7A4E26", 0.95, 1.8, 5.6);
      g.save(); g.translate(-3, -9.8); g.rotate(-1.27 + PI / 2);
      const head = new Path2D(); head.roundRect(-5, -1.9, 10, 3.8, 1.4); g.fillStyle = vGrad(g, ["#C08453", "#8A5A2B", "#5A3D20"], -1.9, 1.9); g.fill(head);
      for (const x of [-3.6, 3.6]) { g.fillStyle = vGrad(g, ["#FFF1BF", "#E9AE34", "#9C6A12"], -2, 2); g.fillRect(x - 0.45, -2, 0.9, 4); }
      for (const x of [-5, 5]) { g.beginPath(); g.ellipse(x, 0, 0.6, 1.9, 0, 0, 2 * PI); g.fillStyle = "#6D3D1F"; g.fill(); }
      line(g, "rgba(255,255,255,0.4)", -2.6, -1.1, 2.6, -1.1, 0.4);
      g.restore(); g.restore();
      if (party && abs(sin(t * 6)) > 0.96) star4(g, -6, -12, 2.4, "rgba(255,240,180,0.95)");
    } else if (kind === "lupa") {
      // lupa de detetive: aro de latão chanfrado, vidro azulado com reflexos e cabo de madeira
      g.translate(-0.6, -3.8);
      line(g, "#5A3D20", 1.4, 5.4, -2.2, -4.2, 2.2); line(g, "#8A5A2B", 1.2, 5, -2, -4, 1.2);
      line(g, "#C98A12", -1.6, -3.4, -2.6, -5.4, 2.4);
      const lx = -4.8, ly = -10.2;
      g.beginPath(); g.arc(lx, ly, 4.6, 0, 2 * PI); const gl = g.createRadialGradient(lx - 1.4, ly - 1.6, 0.4, lx, ly, 4.6); gl.addColorStop(0, "rgba(230,245,255,0.55)"); gl.addColorStop(1, "rgba(150,200,240,0.35)"); g.fillStyle = gl; g.fill();
      g.strokeStyle = vGrad(g, ["#FFF1BF", "#C98A12", "#7A4F05"], ly - 4.6, ly + 4.6); g.lineWidth = 1.4; g.stroke();
      g.beginPath(); g.arc(lx, ly, 3.4, PI * 1.1, PI * 1.45); g.strokeStyle = "rgba(255,255,255,0.9)"; g.lineWidth = 0.6; g.stroke();
      g.beginPath(); g.arc(lx, ly, 3.4, PI * 1.6, PI * 1.7); g.stroke();
    } else if (kind === "termometro") {
      // termômetro digital: ponta de metal, corpo branco, visor verde com a leitura e o botão azul;
      // na festa o visor pisca e ele apita (estrelinha na ponta)
      g.save(); g.translate(GX, GY - 2.4); g.rotate(-0.12);
      const tip = new Path2D(); tip.roundRect(-1.3, -20, 2.6, 6.4, 1.3); g.fillStyle = hGrad(g, ["#7E8698", "#F3F6FA", "#7E8698"], -1.3, 1.3); g.fill(tip); stroke(g, tip, "rgba(38,33,92,0.7)", 0.4);
      const body = new Path2D(); body.roundRect(-3.4, -15.4, 6.8, 18.4, 3.4); g.fillStyle = hGrad(g, ["#C9D1E0", "#FFFFFF", "#B5BED0"], -3.4, 3.4); g.fill(body);
      g.save(); g.clip(body); g.fillStyle = hGrad(g, ["#2E6FC0", "#6FB0FF", "#2E6FC0"], -3.4, 3.4); g.fillRect(-3.4, -1.4, 6.8, 5); g.restore();
      stroke(g, body, "rgba(38,33,92,0.7)", 0.45);
      circle(g, "#1F4F94", 1.2, 0, 0.9); circle(g, "rgba(255,255,255,0.55)", 0.45, -0.4, 0.5);
      const lcd = new Path2D(); lcd.roundRect(-2.4, -12.8, 4.8, 9, 0.8); g.fillStyle = "#C8E6C0"; g.fill(lcd); stroke(g, lcd, "rgba(30,60,40,0.7)", 0.35);
      if (!(party && (t * 3) % 1 < 0.3)) {
        // leitura grande no visor: dois dígitos de segmento e o "°"
        for (const y of [-12, -8.2]) { const d = new Path2D(); d.roundRect(-1.4, y, 2.8, 3.2, 0.3); stroke(g, d, "#2A3A2A", 0.55); line(g, "#2A3A2A", -1.4, y + 1.6, 1.4, y + 1.6, 0.5); }
        circle(g, "#2A3A2A", 0.4, 1.7, -4.8);
      }
      line(g, "rgba(255,255,255,0.85)", -2.4, -13.6, -2.4, -2, 0.55);
      g.restore();
      if (party) { const k = abs(sin(t * 5)); star4(g, -3.6, -22.6, 2.4 * k, `rgba(255,240,180,${k})`); }
    } else if (kind === "cantil") {
      // cantil militar: corpo de lona verde-oliva com costura, faixa da alça, gargalo e tampa de metal
      // presa na correntinha; balança de leve (na festa espirra uma gotinha da tampa)
      g.save(); g.rotate(sin(t * 1.4) * 0.05);
      const strap = new Path2D(); strap.moveTo(1.4, 2); strap.quadraticCurveTo(3.4, -8, -0.4, -12.4); stroke(g, strap, "#4A5622", 1.2); stroke(g, strap, "rgba(255,255,255,0.18)", 0.3);
      g.beginPath(); g.ellipse(-3, -6.6, 5.2, 6, 0, 0, 2 * PI); g.fillStyle = vGrad(g, ["#A4B45E", "#6E7D35", "#4A5622"], -12.6, -0.6); g.fill(); g.strokeStyle = "rgba(30,36,12,0.6)"; g.lineWidth = 0.35; g.stroke();
      g.save(); g.setLineDash([0.6, 0.5]); g.beginPath(); g.ellipse(-3, -6.6, 4.3, 5.1, 0, 0, 2 * PI); g.strokeStyle = "rgba(235,240,190,0.6)"; g.lineWidth = 0.22; g.stroke(); g.restore();
      const band = new Path2D(); band.roundRect(-8.1, -7.4, 10.2, 1.6, 0.4); g.fillStyle = "#3E4818"; g.fill(band);
      const buckle = new Path2D(); buckle.roundRect(-3.9, -7.8, 1.8, 2.4, 0.3); stroke(g, buckle, "#C2CADB", 0.4);
      g.beginPath(); g.ellipse(-5.2, -9.6, 1.1, 2, -0.4, 0, 2 * PI); g.fillStyle = "rgba(255,255,255,0.3)"; g.fill();
      const neck = new Path2D(); neck.roundRect(-4.4, -14.2, 2.8, 2.4, 0.4); g.fillStyle = hGrad(g, ["#8E96A8", "#E4E8F0", "#8E96A8"], -4.4, -1.6); g.fill(neck);
      const cap = new Path2D(); cap.roundRect(-4.8, -16.2, 3.6, 2.4, 0.7); g.fillStyle = vGrad(g, ["#6E7588", "#3C4054"], -16.2, -13.8); g.fill(cap); stroke(g, cap, "rgba(0,0,0,0.5)", 0.25);
      g.save(); g.setLineDash([0.35, 0.35]); const ch = new Path2D(); ch.moveTo(-1.4, -15); ch.quadraticCurveTo(0, -13.6, -1.2, -12); stroke(g, ch, "#C2CADB", 0.3); g.restore();
      if (party && !small) { const ph = (t * 1.2) % 1; circle(g, `rgba(111,176,255,${1 - ph})`, 0.7 * (1 - ph * 0.5), -3 - ph * 4, -17 - sin(PI * ph) * 4); }
      g.restore();
    } else if (kind === "binoculo") {
      // binóculo militar: dois canhões verde-escuros com estrias de borracha, ponte com a roda de foco,
      // lentes azuis com o reflexo passeando; na festa ele "procura" balançando de um lado para o outro
      g.save(); g.translate(3.4, 4.4); g.rotate(-0.15 + (party ? sin(t * 3) * 0.15 : 0));
      for (const cx of [-5.6, -0.6]) {
        const bar = new Path2D(); bar.roundRect(cx - 2.2, -13, 4.4, 11, 1.4); g.fillStyle = hGrad(g, ["#26301C", "#5C6B42", "#26301C"], cx - 2.2, cx + 2.2); g.fill(bar); stroke(g, bar, "rgba(10,15,5,0.6)", 0.3);
        for (const y of [-10.4, -9.4, -8.4, -7.4]) line(g, "rgba(0,0,0,0.35)", cx - 2, y, cx + 2, y, 0.25);
        const cup = new Path2D(); cup.roundRect(cx - 1.8, -2.8, 3.6, 2.8, 0.9); g.fillStyle = "#15190F"; g.fill(cup);
        g.beginPath(); g.ellipse(cx, -13, 2.2, 0.95, 0, 0, 2 * PI); g.fillStyle = "#15190F"; g.fill();
        g.beginPath(); g.ellipse(cx, -13.1, 1.6, 0.62, 0, 0, 2 * PI); g.fillStyle = vGrad(g, ["#BFE3FF", "#3E8BE0"], -13.7, -12.5); g.fill();
        circle(g, "rgba(255,255,255,0.9)", 0.3, cx - 0.6 + sin(t * 1.2) * 0.6, -13.25);
        line(g, "rgba(255,255,255,0.22)", cx - 1.5, -11.8, cx - 1.5, -3.4, 0.4);
      }
      const br = new Path2D(); br.roundRect(-3.9, -10.4, 1.6, 5.2, 0.5); g.fillStyle = "#3A4628"; g.fill(br);
      const wheel = new Path2D(); wheel.roundRect(-4, -12.6, 1.8, 1.8, 0.6); g.fillStyle = "#1C1E28"; g.fill(wheel);
      for (const x of [-3.6, -3.1, -2.6]) line(g, "rgba(170,175,200,0.7)", x, -12.4, x, -11, 0.15);
    } else if (kind === "apito") {
      // apito de polícia: câmara redonda e bocal de metal cromado, furinho, argola e o cordão trançado
      // vermelho e azul caindo da mão; na festa ele apita (ondinhas saindo do bocal)
      // (grande: câmara de 5 de raio, bocal grosso, bolinha laranja aparecendo pela janela)
      // (apito de árbitro visto de lado: a câmara fica na mão e o bocal aponta para fora e para cima; a
      // bolinha só aparece pela fenda de cima; o cordão sai da argola e pende embaixo do punho)
      const OUTL = "rgba(38,33,92,0.85)";
      const cord = new Path2D(); cord.moveTo(1.8, 0.6); cord.bezierCurveTo(3.4, 7, 0.6, 11, -1.2, 10.6); cord.bezierCurveTo(-3, 10.2, -1.6, 4, 0.2, 1.2);
      stroke(g, cord, "rgba(90,10,20,0.85)", small ? 1.5 : 1.25); stroke(g, cord, "#E0303F", small ? 1 : 0.8);
      if (!small) { g.save(); g.setLineDash([0.6, 0.7]); stroke(g, cord, "#FFFFFF", 0.4); g.restore(); }
      g.save(); g.translate(GX - 0.4, -0.8); g.rotate(0.62 + (party ? sin(t * 14) * 0.05 : 0));
      const metal = (y0, y1) => vGrad(g, ["#FFFFFF", "#DDE2EC", "#9AA2B4", "#6E7588"], y0, y1);
      // câmara: barril grande, tampo reto em cima e o fundo bem redondo
      const ch = new Path2D(); ch.moveTo(-6, -3.4); ch.lineTo(0.6, -3.4); ch.bezierCurveTo(3.6, -3.4, 4.2, 1, 2.6, 3); ch.bezierCurveTo(1, 5, -4.4, 5.2, -6, 2.4); ch.closePath();
      g.fillStyle = metal(-3.4, 4.8); g.fill(ch); stroke(g, ch, OUTL, 0.45);
      // bocal: lâmina achatada e curta que afina até a ponta, com a borda de morder
      const mp = new Path2D(); mp.moveTo(-5.4, -3.4); mp.lineTo(-11, -3.2); mp.quadraticCurveTo(-11.6, -3.2, -11.6, -2.6); mp.lineTo(-11.6, -1.8); mp.quadraticCurveTo(-11.6, -1.2, -11, -1.2); mp.lineTo(-5.8, -0.6); mp.closePath();
      g.fillStyle = metal(-3.4, -0.6); g.fill(mp); stroke(g, mp, OUTL, 0.45);
      line(g, "rgba(38,33,92,0.6)", -10.2, -3.2, -10.2, -1.3, 0.3);
      // fenda de cima com a bolinha de cortiça aparecendo
      const sl = new Path2D(); sl.moveTo(-4.8, -3.4); sl.lineTo(-1.4, -3.4); sl.lineTo(-2.2, -2); sl.lineTo(-4.8, -2); sl.closePath(); g.fillStyle = "#2A2C38"; g.fill(sl);
      g.save(); g.clip(sl); circle(g, party ? "#FFB347" : "#F08A3D", 1.2, -3.1 + (party ? sin(t * 22) * 0.6 : 0), -1.3); g.restore();
      // argola onde o cordão prende (fica dentro do punho)
      g.beginPath(); g.arc(3.4, 2.6, 1, 0, 2 * PI); g.strokeStyle = "#8E96A8"; g.lineWidth = 0.55; g.stroke();
      // brilhos
      line(g, "rgba(255,255,255,0.9)", -10.6, -2.7, -6, -2.8, 0.45); line(g, "rgba(255,255,255,0.8)", -0.6, -2.6, 1.6, -2.4, 0.5);
      if (!small) { g.beginPath(); g.ellipse(-2.6, 1.6, 2.6, 1.4, 0.1, 0.2, PI - 0.2); g.strokeStyle = "rgba(80,85,110,0.35)"; g.lineWidth = 0.35; g.stroke(); }
      if (party && !small) for (let i = 0; i < 3; i++) { const ph = (t * 2 + i / 3) % 1; line(g, `rgba(126,224,184,${1 - ph})`, -12.8 - ph * 3, -2.2 + (i - 1) * 1.6 * (1 + ph), -14.2 - ph * 3, -2.2 + (i - 1) * 2.2 * (1 + ph), 0.5); }
      g.restore();
    } else if (kind === "lanterna") {
      // lanterna tática: corpo preto recartilhado, botão vermelho, cabeça mais larga com a lente acesa e o
      // facho de luz suave que varre devagar; na festa ela pisca
      const lit = !(party && (t * 4) % 1 < 0.25);
      g.save(); g.translate(GX - 0.4, GY - 3.2); g.rotate(-0.22 + sin(t * 0.9) * 0.08);
      if (lit) { const beam = new Path2D(); beam.moveTo(-4, -15.4); beam.lineTo(-8.6, -28); beam.quadraticCurveTo(0, -30.4, 8.6, -28); beam.lineTo(4, -15.4); beam.closePath(); g.fillStyle = vGrad(g, ["rgba(255,236,150,0.75)", "rgba(255,236,150,0)"], -15.4, -29); g.fill(beam); }
      const body = new Path2D(); body.roundRect(-2.6, -9.4, 5.2, 14, 1.4); g.fillStyle = hGrad(g, ["#14161E", "#5C6280", "#14161E"], -2.6, 2.6); g.fill(body); stroke(g, body, "rgba(0,0,0,0.75)", 0.45);
      for (let i = 0; i < 5; i++) line(g, "rgba(150,155,180,0.55)", -2.2, -2.4 + i * 1.3, 2.2, -2.4 + i * 1.3, 0.35);
      const head = new Path2D(); head.moveTo(-2.6, -9); head.lineTo(-4.4, -12.6); head.lineTo(-4.4, -15.6); head.lineTo(4.4, -15.6); head.lineTo(4.4, -12.6); head.lineTo(2.6, -9); head.closePath(); g.fillStyle = hGrad(g, ["#1C1E28", "#6E7588", "#1C1E28"], -4.4, 4.4); g.fill(head); stroke(g, head, "rgba(0,0,0,0.75)", 0.45);
      g.beginPath(); g.ellipse(0, -15.6, 4.1, 1.2, 0, 0, 2 * PI); g.fillStyle = lit ? "#FFF6C0" : "#8A845A"; g.fill(); g.strokeStyle = "#8E96A8"; g.lineWidth = 0.5; g.stroke();
      const btn = new Path2D(); btn.roundRect(-1, -7.4, 2, 2.4, 0.6); g.fillStyle = "#E0303F"; g.fill(btn);
      line(g, "rgba(255,255,255,0.35)", -1.8, -8.6, -1.8, 4, 0.5);
      g.restore();
    } else if (kind === "autos") {
      // autos do processo: calhamaço de folhas com a capa parda, etiqueta com o número, barbante
      // cruzado com laço; na festa ganha o carimbo verde de "ok"
      g.save(); g.rotate(-0.12);
      for (let i = 3; i >= 1; i--) { const pg = new Path2D(); pg.roundRect(-7.6 + i * 0.35, -14.2 + i * 0.45, 11, 13.6, 0.5); g.fillStyle = i % 2 ? "#F4EFE4" : "#FFFDF8"; g.fill(pg); stroke(g, pg, "rgba(150,130,100,0.5)", 0.15); }
      const cov = new Path2D(); cov.roundRect(-7.6, -14.2, 11, 13.6, 0.6); g.fillStyle = vGrad(g, ["#E8C48A", "#D3A869", "#B88A4C"], -14.2, -0.6); g.fill(cov); stroke(g, cov, "rgba(90,60,20,0.55)", 0.3);
      const lbl = new Path2D(); lbl.roundRect(-6.4, -13, 3.8, 3.8, 0.4); g.fillStyle = "#FFFFFF"; g.fill(lbl); stroke(g, lbl, "rgba(90,60,20,0.4)", 0.15);
      line(g, "#C42F4B", -6, -12.2, -3, -12.2, 0.35); line(g, "rgba(60,60,90,0.6)", -6, -11.1, -3.2, -11.1, 0.25); line(g, "rgba(60,60,90,0.6)", -6, -10.1, -3.8, -10.1, 0.25);
      // barbante cruzado e o laço
      line(g, "#A0743E", -2.1, -14.2, -2.1, -0.6, 0.45); line(g, "#A0743E", -7.6, -6, 3.4, -6, 0.45);
      for (const s of [-1, 1]) { g.beginPath(); g.ellipse(-2.1 + s * 1.2, -6.5, 1.2, 0.7, s * 0.4, 0, 2 * PI); g.strokeStyle = "#A0743E"; g.lineWidth = 0.4; g.stroke(); }
      line(g, "#A0743E", -2.1, -6, -3, -4.2, 0.4); line(g, "#A0743E", -2.1, -6, -1, -4.4, 0.4); circle(g, "#8A5A2B", 0.5, -2.1, -6);
      line(g, "rgba(255,255,255,0.35)", -6.8, -1.6, 2.6, -1.6, 0.35);
      if (party) { const k = clamp((t * 0.8) % 1 * 3, 0, 1); g.save(); g.globalAlpha = k; const st = new Path2D(); st.roundRect(-1, -4.4, 3.6, 2.6, 0.5); stroke(g, st, "#2E9E72", 0.35); const ck = new Path2D(); ck.moveTo(-0.2, -3.1); ck.lineTo(0.6, -2.4); ck.lineTo(1.9, -3.9); stroke(g, ck, "#2E9E72", 0.4); g.restore(); }
      g.restore();
    } else if (kind === "leitor") {
      // leitor de código de barras (pistola): cabo emborrachado na mão, gatilho, cabeça amarela com a
      // janela; o laser vermelho pisca e varre (na festa ele "bipa" sem parar)
      g.save(); g.translate(-0.6, -3); g.rotate(-0.12);
      const on = (t * (party ? 5 : 1.4)) % 1 < 0.6;
      if (on) {
        const fan = new Path2D(); fan.moveTo(-11.4, -12); fan.lineTo(-20, -17); fan.lineTo(-20, -5); fan.closePath(); g.fillStyle = hGrad(g, ["rgba(255,60,60,0.55)", "rgba(255,60,60,0.08)"], -11.4, -20); g.fill(fan);
        const jy = sin(t * 9) * 1; line(g, "rgba(255,30,30,0.95)", -20, -16.4 + jy, -20, -5.6 + jy, 0.6);
      }
      const hd = new Path2D(); hd.moveTo(-3.6, -8.6); hd.lineTo(2.6, -8.6); hd.lineTo(3.8, 4); hd.quadraticCurveTo(1.4, 5.4, -1, 4.4); hd.closePath(); g.fillStyle = hGrad(g, ["#1C1E28", "#5C6280", "#2A2C38"], -3.6, 3.8); g.fill(hd); stroke(g, hd, "rgba(0,0,0,0.75)", 0.45);
      for (const y of [-3, -1.2, 0.6, 2.4]) line(g, "rgba(150,155,180,0.5)", -1.4, y, 2.4, y, 0.4);
      const trg = new Path2D(); trg.roundRect(-5.6, -8.4, 2.2, 4.2, 1); g.fillStyle = "#2A2C38"; g.fill(trg); stroke(g, trg, "rgba(0,0,0,0.75)", 0.35);
      const head = new Path2D(); head.roundRect(-11, -15.6, 14.4, 7.6, 2.6); g.fillStyle = vGrad(g, ["#FFE45C", "#F5B83D", "#C98A12"], -15.6, -8); g.fill(head); stroke(g, head, "rgba(90,60,10,0.85)", 0.45);
      const nose = new Path2D(); nose.roundRect(-11.6, -15, 3.2, 6.4, 1); g.fillStyle = "#2A2C38"; g.fill(nose);
      const win = new Path2D(); win.roundRect(-12, -14.2, 1.1, 4.8, 0.3); g.fillStyle = on ? "#FF5C4D" : "#6B1E1A"; g.fill(win);
      line(g, "rgba(255,255,255,0.6)", -7, -14.6, 1.6, -14.6, 0.6);
      for (let i = 0; i < 5; i++) line(g, "#2A2C38", -6 + i * 0.9, -12.6, -6 + i * 0.9, -10.6, i % 2 ? 0.25 : 0.45, false);
      circle(g, on ? "#7EE0B8" : "#2E5A4A", 0.6, 0.8, -11.6);
      g.restore();
    } else if (kind === "planilha") {
      // planilha no tablet: moldura escura, tela branca com cabeçalho verde, grade de células e um
      // gráfico de barras que respira (na festa as barras dançam)
      g.save(); g.rotate(-0.12);
      const tab = new Path2D(); tab.roundRect(-9, -15.2, 12, 15.6, 1.6); g.fillStyle = vGrad(g, ["#4A5068", "#2C3042"], -15.2, 0.4); g.fill(tab); stroke(g, tab, "rgba(0,0,0,0.5)", 0.3);
      g.fillStyle = "#FFFFFF"; g.fillRect(-8.1, -14.2, 10.2, 13.6);
      g.fillStyle = "#2E9E72"; g.fillRect(-8.1, -14.2, 10.2, 1.5);
      g.fillStyle = "rgba(126,224,184,0.45)"; g.fillRect(-5.5, -11.3, 2.6, 1.3);
      for (const x of [-5.5, -2.9, -0.3]) line(g, "rgba(120,140,170,0.6)", x, -14.2, x, -7.4, 0.15);
      for (const y of [-12.7, -11.3, -10, -8.7, -7.4]) line(g, "rgba(120,140,170,0.6)", -8.1, y, 2.1, y, 0.15);
      for (const x of [-7.6, -5, -2.4, 0.2]) line(g, "rgba(255,255,255,0.85)", x, -13.45, x + 1.4, -13.45, 0.3);
      const hs = [2.4, 3.6, 2.8, 4.6], cs = ["#6FB0FF", "#7EE0B8", "#F5B83D", "#FF8FA3"];
      for (let i = 0; i < 4; i++) { const hh = hs[i] * (0.85 + 0.15 * sin(t * (party ? 6 : 1.5) + i * 1.3)); const b = new Path2D(); b.roundRect(-7.2 + i * 2.3, -1.6 - hh, 1.5, hh, 0.3); g.fillStyle = cs[i]; g.fill(b); }
      line(g, "rgba(60,60,90,0.6)", -7.6, -1.5, 1.8, -1.5, 0.2);
      circle(g, "#6E7588", 0.25, -3, -14.7);
      g.save(); g.globalAlpha = 0.18; const gl = new Path2D(); gl.moveTo(-8.1, -8); gl.lineTo(-3, -14.2); gl.lineTo(-1.4, -14.2); gl.lineTo(-8.1, -6); gl.closePath(); g.fillStyle = "#FFFFFF"; g.fill(gl); g.restore();
      g.restore();
    } else if (kind === "maquininha") {
      // maquininha de cartão: corpo verde-água arredondado, visor com o "aprovado", teclado numérico com
      // as teclas coloridas e o papelzinho saindo por cima (na festa sai comprovante sem parar)
      g.save(); g.rotate(-0.14);
      const pl = party ? 2 + ((t * 0.8) % 1) * 6 : 2.6 + sin(t * 1.2) * 0.3;
      const pap = new Path2D(); pap.roundRect(-5.2, -15.4 - pl, 6.4, pl + 1, 0.2); g.fillStyle = "#FFFFFF"; g.fill(pap); stroke(g, pap, "rgba(120,130,160,0.5)", 0.15);
      for (let i = 0; i < 4; i++) { const y = -15.4 - pl + 0.9 + i * 0.9; if (y < -15.6) line(g, "rgba(90,100,130,0.55)", -4.4, y, i % 2 ? -1.4 : -0.2, y, 0.2); }
      const body = new Path2D(); body.roundRect(-6.4, -16, 8.8, 17.6, 1.8); g.fillStyle = vGrad(g, ["#5FD3C0", "#2EA897", "#1F7A6E"], -16, 1.6); g.fill(body); stroke(g, body, "rgba(10,60,50,0.6)", 0.3);
      line(g, "#16564D", -5.4, -15.3, 1.4, -15.3, 0.6);
      const scr = new Path2D(); scr.roundRect(-5.2, -13.8, 6.4, 4, 0.6); g.fillStyle = "#1C2A30"; g.fill(scr);
      const ck = new Path2D(); ck.moveTo(-3.4, -11.9); ck.lineTo(-2.6, -11.1); ck.lineTo(-1.2, -12.8); stroke(g, ck, "#7EE0B8", 0.4);
      line(g, "rgba(126,224,184,0.7)", -0.6, -12.4, 0.6, -12.4, 0.25); line(g, "rgba(126,224,184,0.7)", -0.6, -11.4, 0.2, -11.4, 0.25);
      for (let r = 0; r < 5; r++) for (let c = 0; c < 3; c++) {
        const k = new Path2D(); k.roundRect(-5 + c * 2.15, -8.6 + r * 1.75, 1.75, 1.3, 0.4);
        g.fillStyle = r === 4 ? ["#FF5C4D", "#F5C451", "#3FBF8F"][c] : "#EEF2F6"; g.fill(k);
      }
      line(g, "rgba(255,255,255,0.4)", -5.6, -15.8 + 1.2, -5.6, -1, 0.35);
      g.restore();
    } else if (kind === "cofrinho") {
      // cofrinho de porquinho: rosa com brilho, focinho com narinas, orelha, olhinho, bochecha,
      // perninhas e o rabo em caracol; uma moeda dourada cai na fenda (na festa caem mais rápido)
      g.save(); g.translate(-0.2, -3.4); g.rotate(sin(t * 1.6) * 0.04);
      const ph = (t / (party ? 0.8 : 2.6)) % 1;
      if (ph < 0.5) { const cy = -12 + ph / 0.5 * 7.6; circle(g, "#C98A12", 1.4, 0.7, cy); circle(g, "#F5C451", 1.05, 0.7, cy); line(g, "#C98A12", 0.7, cy - 0.5, 0.7, cy + 0.5, 0.25); }
      for (const x of [-3.2, -1, 1.6, 3.6]) { const lg = new Path2D(); lg.roundRect(x - 0.8, 3, 1.6, 2.4, 0.6); g.fillStyle = "#E0708C"; g.fill(lg); }
      const tail = new Path2D(); tail.moveTo(5.6, 0); tail.bezierCurveTo(7.8, -1.4, 7.6, 1.8, 6.4, 1); tail.quadraticCurveTo(6, 0.2, 7, -0.4); stroke(g, tail, "#E0708C", 0.5);
      g.beginPath(); g.ellipse(0, 0, 6, 4.8, 0, 0, 2 * PI); const rg = g.createRadialGradient(-2, -2, 0.5, 0, 0, 6.6); rg.addColorStop(0, "#FFD0DC"); rg.addColorStop(0.55, "#FF9FB6"); rg.addColorStop(1, "#E0708C"); g.fillStyle = rg; g.fill(); g.strokeStyle = "rgba(140,40,70,0.5)"; g.lineWidth = 0.3; g.stroke();
      const ear = new Path2D(); ear.moveTo(-3.8, -3.4); ear.lineTo(-2.6, -6.6); ear.lineTo(-1.2, -4.2); ear.closePath(); g.fillStyle = "#FF8FA8"; g.fill(ear); stroke(g, ear, "rgba(140,40,70,0.5)", 0.25);
      g.beginPath(); g.ellipse(-6, 0.6, 1.5, 1.9, 0, 0, 2 * PI); g.fillStyle = "#FF8FA8"; g.fill(); g.strokeStyle = "rgba(140,40,70,0.5)"; g.lineWidth = 0.25; g.stroke();
      circle(g, "#B84466", 0.32, -6.2, 0); circle(g, "#B84466", 0.32, -6.2, 1.2);
      circle(g, INK, 0.55, -3.6, -1.2); circle(g, "#FFFFFF", 0.2, -3.75, -1.4);
      circle(g, "rgba(255,110,150,0.45)", 0.8, -3.2, 0.8);
      const slot = new Path2D(); slot.roundRect(-0.6, -4.9, 2.6, 0.7, 0.35); g.fillStyle = "#8C2A48"; g.fill(slot);
      g.beginPath(); g.ellipse(-1.2, -2.6, 1.6, 0.8, -0.3, 0, 2 * PI); g.fillStyle = "rgba(255,255,255,0.55)"; g.fill();
      g.restore();
    } else if (kind === "microfone") {
      // microfone da tribuna: base redonda preta, haste "pescoço de ganso" com anéis, cápsula com grade
      // e o anel vermelho de "no ar" (na festa as ondas de som saem da cápsula)
      // (clássico: cabeça grande e redonda com malha, anel brilhante e cabo grosso com a luz "no ar")
      g.save(); g.translate(GX - 0.2, GY - 4.4); g.rotate(-0.15);
      const live = (t * 0.7) % 1 < 0.85;
      const hdl = new Path2D(); hdl.moveTo(-2.2, -6.4); hdl.lineTo(2.2, -6.4); hdl.lineTo(1.5, 5); hdl.quadraticCurveTo(0, 6, -1.5, 5); hdl.closePath(); g.fillStyle = hGrad(g, ["#14161E", "#5C6280", "#14161E"], -2.2, 2.2); g.fill(hdl); stroke(g, hdl, "rgba(0,0,0,0.75)", 0.45);
      if (live && !small) circle(g, "rgba(255,92,77,0.35)", 1.5, 0, -3);
      circle(g, live ? "#FF5C4D" : "#6B1E1A", 0.75, 0, -3);
      const ring = new Path2D(); ring.roundRect(-3.4, -8.2, 6.8, 2.2, 0.9); g.fillStyle = vGrad(g, GOLD3, -8.2, -6); g.fill(ring); stroke(g, ring, "rgba(110,70,10,0.8)", 0.4);
      const cx = 0, cy = -13, R = 5.4;
      g.beginPath(); g.arc(cx, cy, R, 0, 2 * PI); const rg = g.createRadialGradient(cx - 1.8, cy - 2, 0.4, cx, cy, R); rg.addColorStop(0, "#FFFFFF"); rg.addColorStop(0.5, "#B5BED0"); rg.addColorStop(1, "#5C6280"); g.fillStyle = rg; g.fill();
      g.save(); g.clip(); for (let i = -6; i <= 6; i++) { line(g, "rgba(40,44,60,0.55)", cx + i * 1.2 - 6, cy - 6, cx + i * 1.2 + 6, cy + 6, 0.25); line(g, "rgba(40,44,60,0.55)", cx + i * 1.2 + 6, cy - 6, cx + i * 1.2 - 6, cy + 6, 0.25); } g.restore();
      g.beginPath(); g.arc(cx, cy, R, 0, 2 * PI); g.strokeStyle = "rgba(38,33,92,0.75)"; g.lineWidth = 0.5; g.stroke();
      line(g, "rgba(90,95,120,0.9)", cx - R, cy, cx + R, cy, 0.6);
      g.beginPath(); g.ellipse(cx - 2, cy - 2.4, 1.3, 2, -0.5, 0, 2 * PI); g.fillStyle = "rgba(255,255,255,0.55)"; g.fill();
      if (party && !small) for (let i = 0; i < 3; i++) { const ph = (t * 1.5 + i / 3) % 1, r = R + 1 + ph * 5; g.beginPath(); g.arc(cx, cy, r, -PI * 0.85, -PI * 0.55); g.strokeStyle = `rgba(126,224,184,${0.85 * (1 - ph)})`; g.lineWidth = 0.5; g.stroke(); }
      g.restore();
    } else if (kind === "projeto") {
      // projeto de lei: pergaminho aberto entre dois rolos de madeira, título e linhas de texto e o
      // selo de lacre vermelho com as fitinhas balançando
      g.save(); g.rotate(-0.12);
      const sh = new Path2D(); sh.rect(-7.6, -15.2, 9.6, 12.4); g.fillStyle = vGrad(g, ["#FFF4D6", "#F2DFAE", "#E2C786"], -15.2, -2.8); g.fill(sh);
      line(g, "#7A4E26", -6.2, -13.4, 0.6, -13.4, 0.55);
      for (let i = 0; i < 5; i++) line(g, "rgba(120,80,40,0.5)", -6.4, -11.6 + i * 1.2, [0.8, 0.2, 0.9, -0.6, -2.6][i], -11.6 + i * 1.2, 0.25);
      for (const y of [-16.4, -3.6]) {
        const rl = new Path2D(); rl.roundRect(-8.4, y, 11.2, 2, 1); g.fillStyle = vGrad(g, ["#FFF4D6", "#D9B878", "#A07A40"], y, y + 2); g.fill(rl); stroke(g, rl, "rgba(110,70,30,0.5)", 0.2);
        for (const x of [-8.9, 3.3]) circle(g, "#6D3D1F", 0.8, x, y + 1);
      }
      const sw = sin(t * 2) * 0.5;
      for (const s of [-1, 1]) { const tl = new Path2D(); tl.moveTo(-1.6 + s * 0.4, -4.2); tl.lineTo(-1.6 + s * 1.4 + sw, -0.4); tl.lineTo(-1.6 + s * 0.6 + sw, -0.9); tl.lineTo(-1.6 + s * 0.2 + sw * 0.6, -0.2); tl.closePath(); g.fillStyle = "#C42F4B"; g.fill(tl); }
      g.beginPath(); g.arc(-1.6, -4.6, 1.7, 0, 2 * PI); const rg = g.createRadialGradient(-2.1, -5.1, 0.2, -1.6, -4.6, 1.9); rg.addColorStop(0, "#FF8A8A"); rg.addColorStop(1, "#A8162E"); g.fillStyle = rg; g.fill();
      g.fillStyle = "rgba(120,10,30,0.6)"; g.fill(star5(g, -1.6, -4.6, 0.9));
      g.restore();
    } else if (kind === "regua") {
      // régua escolar amarela: marcações de milímetro e centímetro, os números e o furinho; balança de
      // leve (na festa vira batuta)
      g.save(); g.translate(GX - 0.2, GY - 1.4); g.rotate(-0.42 + (party ? sin(t * 5) * 0.2 : sin(t * 1.2) * 0.03));
      const rl = new Path2D(); rl.roundRect(-1.9, -20.4, 3.8, 23, 0.6); g.fillStyle = hGrad(g, ["#FFE45C", "#FFD23F", "#E0B020"], -1.9, 1.9); g.fill(rl); stroke(g, rl, "rgba(140,100,10,0.6)", 0.3);
      for (let i = 0; i <= 40; i++) {
        const y = 1.6 - i * 0.5, cm = i % 10 === 0, half = i % 5 === 0;
        if (small && !half) continue;
        line(g, "rgba(90,60,10,0.8)", -1.9, y, -1.9 + (cm ? 1.6 : half ? 1.1 : 0.6), y, cm ? 0.2 : 0.12, false);
        if (cm) { g.fillStyle = "rgba(90,60,10,0.75)"; g.fillRect(0, y - 0.35, 0.5, 0.7); }
      }
      g.beginPath(); g.arc(0, -19.4, 0.6, 0, 2 * PI); g.strokeStyle = "rgba(140,100,10,0.7)"; g.lineWidth = 0.25; g.stroke();
      line(g, "rgba(255,255,255,0.6)", 1.3, -19.6, 1.3, 2, 0.35);
      g.restore();
    } else if (kind === "globo") {
      // globo terrestre: pé de madeira na mão, meridiano dourado em meia-lua, esfera azul com os
      // continentes verdes girando devagar e o brilho
      g.translate(0.6, -3);
      const cx = -3.6, cy = -11.4, R = 5.4;
      g.beginPath(); g.ellipse(0.4, 3.6, 3.4, 1.1, 0, 0, 2 * PI); g.fillStyle = vGrad(g, ["#C08453", "#6D3D1F"], 2.5, 4.7); g.fill();
      line(g, "#7A4E26", 0.4, 3, -1.07, -5.4, 0.9);
      g.save(); g.beginPath(); g.arc(cx, cy, R, 0, 2 * PI); g.clip();
      const rg = g.createRadialGradient(cx - 1.8, cy - 2, 0.5, cx, cy, R); rg.addColorStop(0, "#9ED8FF"); rg.addColorStop(0.6, "#3E8BE0"); rg.addColorStop(1, "#1F4F94"); g.fillStyle = rg; g.fillRect(cx - R, cy - R, 2 * R, 2 * R);
      const rot = (t * 1.1) % 14;
      for (const k of [-14, 0]) for (const [x, y, rx, ry] of [[-3, -2.4, 2.2, 1.6], [1, 0.8, 1.6, 2.6], [4.4, -1.6, 1.4, 1.1], [-1.4, 3.4, 1.8, 1], [8, 1.6, 2.4, 1.8], [10.6, -2.6, 1.6, 1.2]]) {
        g.beginPath(); g.ellipse(cx + x + rot + k, cy + y, rx, ry, 0.3, 0, 2 * PI); g.fillStyle = "#4CC38A"; g.fill();
      }
      const sd = g.createRadialGradient(cx - 1.8, cy - 2, R * 0.4, cx, cy, R); sd.addColorStop(0, "rgba(10,30,80,0)"); sd.addColorStop(1, "rgba(10,30,80,0.4)"); g.fillStyle = sd; g.fillRect(cx - R, cy - R, 2 * R, 2 * R);
      g.restore();
      g.beginPath(); g.arc(cx, cy, R, 0, 2 * PI); g.strokeStyle = "rgba(20,40,90,0.55)"; g.lineWidth = 0.3; g.stroke();
      g.beginPath(); g.ellipse(cx - 1.8, cy - 2.4, 1.2, 1.8, -0.5, 0, 2 * PI); g.fillStyle = "rgba(255,255,255,0.45)"; g.fill();
      g.beginPath(); g.arc(cx, cy, 6.5, -PI / 2 - 0.4, PI / 2 - 0.4); g.strokeStyle = vGrad(g, GOLD3, cy - 6.5, cy + 6.5); g.lineWidth = 0.9; g.stroke();
      circle(g, "#C98A12", 0.55, cx - 6.5 * sin(0.4), cy - 6.5 * cos(0.4)); circle(g, "#C98A12", 0.55, cx + 6.5 * sin(0.4), cy + 6.5 * cos(0.4));
    } else if (kind === "notebook") {
      // notebookzinho aberto: tampa prateada, tela escura com linhas de código coloridas e o cursor
      // piscando, teclado em perspectiva com touchpad; na festa o código rola sozinho
      // (segurado pela ponta da base: o punho cobre o canto direito do teclado e a tela abre para cima)
      g.save(); g.translate(-0.6, 0.2); g.rotate(-0.06);
      const OUTL = "rgba(38,33,92,0.85)", hy = -1.8, fy = 1.4;
      // tampa: moldura prateada, borda escura e a tela acesa
      const lid = new Path2D(); lid.moveTo(-11.2, hy); lid.lineTo(-10.4, -13.6); lid.quadraticCurveTo(-10.3, -14.3, -9.6, -14.3); lid.lineTo(-0.6, -14.3); lid.quadraticCurveTo(0.1, -14.3, 0.2, -13.6); lid.lineTo(1, hy); lid.closePath();
      g.fillStyle = vGrad(g, ["#EEF2F8", "#C2CADB", "#8E96A8"], -14.3, hy); g.fill(lid); stroke(g, lid, OUTL, 0.45);
      const bz = new Path2D(); bz.moveTo(-10.4, hy - 0.7); bz.lineTo(-9.8, -13.6); bz.lineTo(-0.4, -13.6); bz.lineTo(0.2, hy - 0.7); bz.closePath(); g.fillStyle = "#23263A"; g.fill(bz);
      const scr = new Path2D(); scr.moveTo(-9.8, hy - 1.3); scr.lineTo(-9.3, -12.9); scr.lineTo(-0.9, -12.9); scr.lineTo(-0.4, hy - 1.3); scr.closePath();
      g.fillStyle = vGrad(g, ["#2B3170", "#1E2150", "#161A3A"], -12.9, hy - 1.3); g.fill(scr);
      g.save(); g.clip(scr);
      if (!small) { g.fillStyle = "#3A417E"; g.fillRect(-10, -12.9, 10, 1.1); for (const [x, c] of [[-8.6, "#FF6B6B"], [-7.8, "#FFD84A"], [-7, "#7EE0B8"]]) circle(g, c, 0.28, x, -12.35); }
      const sc = party ? (t * 3) % 1 : 0, code = [[0, 3.4, "#FF8FA3"], [1, 4.6, "#7EE0B8"], [2, 2.6, "#F5B83D"], [2, 3.8, "#6FB0FF"], [1, 3, "#7EE0B8"], [0, 2.2, "#B995F5"], [0, 4, "#FF8FA3"], [1, 2.8, "#6FB0FF"]];
      const step = small ? 1.6 : 1.15, y0 = small ? -11.2 : -10.9;
      for (let i = 0; i < code.length; i++) { const [ind, len, c] = code[i], y = y0 + i * step - sc * step; line(g, c, -8.6 + ind, y, -8.6 + ind + len, y, small ? 0.6 : 0.45); }
      if ((t * 2) % 1 < 0.5) { g.fillStyle = "#FFFFFF"; g.fillRect(-5.2, -3.6 - sc * step, 0.45, 0.9); }
      // brilho da tela: luz suave e um reflexo na diagonal
      const glow = g.createRadialGradient(-5, -8, 0.5, -5, -8, 7); glow.addColorStop(0, "rgba(140,160,255,0.25)"); glow.addColorStop(1, "rgba(140,160,255,0)"); g.fillStyle = glow; g.fillRect(-10, -13, 10, 12);
      g.save(); g.globalAlpha = 0.14; const gl = new Path2D(); gl.moveTo(-9.6, -6); gl.lineTo(-5, -12.9); gl.lineTo(-3.4, -12.9); gl.lineTo(-9.8, -3.6); gl.closePath(); g.fillStyle = "#FFFFFF"; g.fill(gl); g.restore();
      g.restore();
      circle(g, "#4A5068", 0.25, -5.1, -13.95);
      // base: tampo do teclado em perspectiva, borda da frente e a dobradiça
      const deck = new Path2D(); deck.moveTo(-11.2, hy); deck.lineTo(1, hy); deck.lineTo(2.6, fy); deck.lineTo(-12.8, fy); deck.closePath();
      g.fillStyle = vGrad(g, ["#C9D1E0", "#EEF2F8"], hy, fy); g.fill(deck);
      const edge = new Path2D(); edge.moveTo(-12.8, fy); edge.lineTo(2.6, fy); edge.lineTo(2.4, fy + 1); edge.quadraticCurveTo(2.3, fy + 1.3, 1.9, fy + 1.3); edge.lineTo(-12.2, fy + 1.3); edge.quadraticCurveTo(-12.6, fy + 1.3, -12.7, fy + 1); edge.closePath();
      g.fillStyle = vGrad(g, ["#AEB6C4", "#7E8698"], fy, fy + 1.3); g.fill(edge);
      g.fillStyle = "rgba(140,160,255,0.18)"; g.fill(deck);
      const keyRow = (u, n, h) => { const y = lerp(hy + 0.35, fy - 1.1, u), xl = lerp(-10.6, -11.8, u), xr = lerp(0.2, 1.4, u), w = (xr - xl) / n; for (let i = 0; i < n; i++) { const k = new Path2D(); k.roundRect(xl + i * w + 0.08, y, w - 0.16, h, 0.12); g.fillStyle = "#3C4054"; g.fill(k); } };
      if (small) { keyRow(0, 6, 0.5); keyRow(0.55, 6, 0.55); }
      else { keyRow(0, 12, 0.42); keyRow(0.33, 12, 0.45); keyRow(0.66, 11, 0.48); }
      const tp = new Path2D(); tp.moveTo(-7.4, fy - 0.6); tp.lineTo(-3.2, fy - 0.6); tp.lineTo(-3, fy - 0.1); tp.lineTo(-7.7, fy - 0.1); tp.closePath(); g.fillStyle = "rgba(110,117,136,0.55)"; g.fill(tp);
      const outer = new Path2D(); outer.moveTo(-11.2, hy); outer.lineTo(1, hy); outer.lineTo(2.6, fy); outer.lineTo(2.4, fy + 1.3); outer.lineTo(-12.6, fy + 1.3); outer.lineTo(-12.8, fy); outer.closePath(); stroke(g, outer, OUTL, 0.45);
      line(g, "#2C3042", -10.8, hy, 0.6, hy, 0.5);
      line(g, "rgba(255,255,255,0.7)", -9.8, -13.9, -1, -13.9, 0.3);
      g.restore();
    } else if (kind === "caneca") {
      // caneca de café: cerâmica roxa com coraçãozinho, alça na mão, café escuro por cima e a fumacinha
      // subindo em ondas (na festa sai mais fumaça)
      // (a alça fica dentro do punho e o corpo da caneca sai para o lado de fora, em pé)
      g.save(); g.translate(0.8, 0.3); g.rotate(-0.04);
      const OUTL = "rgba(30,20,90,0.85)";
      const hd = new Path2D(); hd.moveTo(-2.4, -3.4); hd.bezierCurveTo(3.2, -4.6, 4, 3, -2.4, 2.6);
      stroke(g, hd, OUTL, 2.2); stroke(g, hd, "#5247E0", 1.3);
      const mug = new Path2D(); mug.moveTo(-11.6, -5); mug.lineTo(-2.2, -5); mug.lineTo(-2.5, 3.6); mug.quadraticCurveTo(-2.6, 5, -4, 5); mug.lineTo(-9.8, 5); mug.quadraticCurveTo(-11.2, 5, -11.3, 3.6); mug.closePath();
      g.fillStyle = hGrad(g, ["#3F35B5", "#8A80F5", "#5247E0", "#3F35B5"], -11.6, -2.2); g.fill(mug); stroke(g, mug, OUTL, 0.45);
      g.beginPath(); g.ellipse(-6.9, -5, 4.7, 1.1, 0, 0, 2 * PI); g.fillStyle = "#ECEAF8"; g.fill(); g.strokeStyle = OUTL; g.lineWidth = 0.4; g.stroke();
      g.beginPath(); g.ellipse(-6.9, -4.85, 3.9, 0.7, 0, 0, 2 * PI); g.fillStyle = vGrad(g, ["#4A2A12", "#8A5A2B"], -5.5, -4.2); g.fill();
      if (!small) { g.beginPath(); g.ellipse(-7.8, -4.95, 1.4, 0.25, 0, 0, 2 * PI); g.fillStyle = "rgba(255,230,190,0.5)"; g.fill(); }
      g.fillStyle = "#FF8FA3"; g.fill(heart(-6.9, 0.2, 1.9)); if (!small) circle(g, "rgba(255,255,255,0.6)", 0.35, -7.6, -0.4);
      line(g, "rgba(255,255,255,0.5)", -10.3, -3.4, -10.1, 3.2, 0.55);
      const n = party ? 3 : 2;
      for (let i = 0; i < n; i++) {
        const ph = (t * 0.5 + i / n) % 1, bx = -8.6 + i * (3.4 / Math.max(1, n - 1)), y0 = -6.4 - ph * 4, sm = new Path2D();
        for (let j = 0; j <= 6; j++) { const x = bx + sin(j * 0.9 + t * 3 + i) * 0.8, y = y0 - j * 0.9; if (j) sm.lineTo(x, y); else sm.moveTo(x, y); }
        stroke(g, sm, `rgba(170,160,210,${0.8 * sin(PI * ph)})`, small ? 0.9 : 0.65);
      }
      g.restore();
    } else if (kind === "carimbo") {
      // carimbo: pegador de madeira torneado (a mão segura no pescoço), base de madeira com etiqueta e
      // a borracha vermelha de tinta; na festa ele carimba no ar
      // (grande: a folhinha já carimbada com "OK" em vermelho fica ao lado, bem visível)
      // (segurado por cima: o punho fecha na bola do pegador, o cabo torneado desce e a base com a
      // borracha fica embaixo, pronta para carimbar; na festa ele bate e deixa o selo "OK" no ar)
      const ph = party ? (t * 1.2) % 1 : 0, pr = party ? Math.max(0, sin(ph * PI * 2)) * 1.4 : 0;
      const OUTL = "rgba(60,25,8,0.85)";
      g.save(); g.translate(GX - 0.2, 0.2 + pr); g.rotate(0.12);
      g.beginPath(); g.arc(0, -0.6, 2.9, 0, 2 * PI); const rg = g.createRadialGradient(-1, -1.8, 0.3, 0, -0.6, 3.1); rg.addColorStop(0, "#F0A070"); rg.addColorStop(0.6, "#B5582A"); rg.addColorStop(1, "#7A3410"); g.fillStyle = rg; g.fill(); g.strokeStyle = OUTL; g.lineWidth = 0.45; g.stroke();
      // cabo torneado: pescoço fino, anel e a saia que alarga até a base
      const nk = new Path2D(); nk.moveTo(-1.1, 1.8); nk.quadraticCurveTo(-0.7, 3.4, -1.3, 4.2); nk.quadraticCurveTo(-2.1, 4.8, -1.5, 5.4); nk.quadraticCurveTo(-2.6, 6.6, -2.8, 7.6); nk.lineTo(2.8, 7.6); nk.quadraticCurveTo(2.6, 6.6, 1.5, 5.4); nk.quadraticCurveTo(2.1, 4.8, 1.3, 4.2); nk.quadraticCurveTo(0.7, 3.4, 1.1, 1.8); nk.closePath();
      g.fillStyle = hGrad(g, ["#7A3410", "#D98A5A", "#B5582A", "#7A3410"], -2.8, 2.8); g.fill(nk); stroke(g, nk, OUTL, 0.4);
      if (!small) line(g, "rgba(255,220,190,0.6)", -0.9, 5.8, -1.6, 7.2, 0.35);
      const col = new Path2D(); col.roundRect(-3.6, 7.4, 7.2, 1.3, 0.5); g.fillStyle = vGrad(g, GOLD3, 7.4, 8.7); g.fill(col); stroke(g, col, "rgba(110,70,10,0.85)", 0.35);
      const blk = new Path2D(); blk.roundRect(-5.6, 8.6, 11.2, 3.6, 0.9); g.fillStyle = vGrad(g, ["#D9A070", "#A06A3A", "#7A4E26"], 8.6, 12.2); g.fill(blk); stroke(g, blk, OUTL, 0.45);
      const lb = new Path2D(); lb.roundRect(-3, 9.2, 6, 2.4, 0.4); g.fillStyle = "#FFFDF8"; g.fill(lb); stroke(g, lb, "rgba(60,25,8,0.4)", 0.2);
      g.save(); g.font = "bold 2.1px sans-serif"; g.textAlign = "center"; g.textBaseline = "middle"; g.fillStyle = "#E0303F"; g.fillText("OK", 0, 10.5); g.restore();
      const rb = new Path2D(); rb.roundRect(-5.2, 12, 10.4, 1.7, 0.5); g.fillStyle = vGrad(g, ["#FF5C4D", "#B81F3E"], 12, 13.7); g.fill(rb); stroke(g, rb, "rgba(90,10,20,0.85)", 0.35);
      line(g, "rgba(255,255,255,0.45)", -4.8, 9.3, -4.8, 11.4, 0.4);
      g.restore();
      // selo "OK" que fica no ar depois da batida
      if (party && ph > 0.5 && !small) {
        const a = 1 - (ph - 0.5) / 0.5; g.save(); g.globalAlpha = a; g.translate(GX - 2.2, 17); g.rotate(-0.15);
        g.beginPath(); g.ellipse(0, 0, 4.8, 1.6, 0, 0, 2 * PI); g.strokeStyle = "#E0303F"; g.lineWidth = 0.45; g.stroke();
        g.font = "bold 2px sans-serif"; g.textAlign = "center"; g.textBaseline = "middle"; g.fillStyle = "#E0303F"; g.fillText("OK", 0, 0.1); g.restore();
      }
    } else if (kind === "grampeador") {
      // grampeador: base preta comprida, braço vermelho com a ponta cromada e a dobradiça atrás; na festa
      // ele grampeia sem parar (o braço abre e fecha)
      // (grande e quase em pé: base e braço grossos, dobradiça atrás, boca aberta mostrando o grampo)
      const open = party ? 0.06 + abs(sin(t * 5)) * 0.3 : 0.26 + sin(t * 1.3) * 0.03;
      g.save(); g.translate(1.2, 0.8); g.rotate(0.85);
      const base = new Path2D(); base.roundRect(-15, 0, 17, 3.4, 1.4); g.fillStyle = vGrad(g, ["#4A5068", "#1C1E28"], 0, 3.4); g.fill(base); stroke(g, base, "rgba(0,0,0,0.8)", 0.45);
      const plate = new Path2D(); plate.roundRect(-14.4, -0.5, 4, 1, 0.3); g.fillStyle = vGrad(g, ["#FFFFFF", "#8E96A8"], -0.5, 0.5); g.fill(plate);
      g.save(); g.translate(0.4, -0.6); g.rotate(open);
      // grampo prateado pendurado na boca
      line(g, "#E4E8F0", -13.2, 0.2, -13.2, 2.4, 0.5); line(g, "#E4E8F0", -11.4, 0.2, -11.4, 2.4, 0.5); line(g, "#C2CADB", -13.2, 0.2, -11.4, 0.2, 0.5);
      const arm = new Path2D(); arm.roundRect(-15.4, -5.2, 17, 5.2, 2.2); g.fillStyle = vGrad(g, ["#FF7A7A", "#E0303F", "#9C1424"], -5.2, 0); g.fill(arm); stroke(g, arm, "rgba(90,10,20,0.85)", 0.45);
      const nose = new Path2D(); nose.roundRect(-15.8, -5.4, 4.8, 5.6, 2); g.fillStyle = hGrad(g, ["#8E96A8", "#FFFFFF", "#AEB6C4"], -15.8, -11); g.fill(nose); stroke(g, nose, "rgba(38,33,92,0.75)", 0.4);
      line(g, "rgba(255,255,255,0.6)", -10.4, -4.2, -1.4, -4.2, 0.6);
      g.restore();
      circle(g, "#2A2C38", 2, 0.4, -0.6); circle(g, "#AEB6C4", 0.7, 0.4, -0.6);
      if (party && open < 0.1) star4(g, -14.6, -1, 2.4, "rgba(255,240,180,0.95)");
      g.restore();
    } else if (kind === "guarda-chuva") {
      // guarda-chuva colorido aberto: gomos de arco-íris com a borda ondulada, pontinha em cima, haste e
      // o cabo em gancho na mão; balança de leve (na festa gira de um lado para o outro)
      g.save(); g.translate(GX - 0.2, -0.6); g.rotate(-0.22 + (party ? sin(t * 4) * 0.25 : sin(t * 1.3) * 0.06));
      const hk = new Path2D(); hk.moveTo(0, -4); hk.lineTo(0, 2); hk.bezierCurveTo(0, 4.4, 2.8, 4.4, 2.8, 2.2); stroke(g, hk, "#6B4320", 1.2);
      line(g, "#4A5068", 0, -4, 0, -24, 0.6);
      const A = [0, -24.5], cols = ["#FF6B6B", "#FFD84A", "#7EE0B8", "#6FB0FF", "#B995F5", "#FF8FA3"];
      for (let i = 0; i < 6; i++) {
        const x0 = -9 + i * 3, x1 = x0 + 3, gp = new Path2D();
        gp.moveTo(A[0], A[1]); gp.quadraticCurveTo(x0, A[1] + 0.5, x0, -17.5); gp.quadraticCurveTo((x0 + x1) / 2, -19.3, x1, -17.5); gp.quadraticCurveTo(x1, A[1] + 0.5, A[0], A[1]); gp.closePath();
        g.fillStyle = cols[i]; g.fill(gp); stroke(g, gp, "rgba(38,33,92,0.4)", 0.25);
      }
      g.save(); g.globalAlpha = 0.3; g.beginPath(); g.ellipse(-4, -21.6, 2.6, 1, -0.4, 0, 2 * PI); g.fillStyle = "#FFFFFF"; g.fill(); g.restore();
      for (let i = 0; i <= 6; i++) circle(g, "#4A5068", 0.3, -9 + i * 3, -17.5);
      line(g, "#4A5068", 0, -24.5, 0, -26.2, 0.6); circle(g, "#4A5068", 0.45, 0, -26.3);
      g.restore();
    } else if (kind === "sorvete") {
      // sorvete de casquinha: casquinha de biscoito quadriculada, bola de morango embaixo e de menta em
      // cima, granulado colorido e uma gotinha escorrendo
      g.save(); g.translate(GX - 0.2, GY - 2); g.rotate(-0.12 + sin(t * 1.4) * 0.04);
      const cone = new Path2D(); cone.moveTo(-4.2, -7.4); cone.lineTo(4.2, -7.4); cone.lineTo(0.3, 3.4); cone.lineTo(-0.3, 3.4); cone.closePath(); g.fillStyle = vGrad(g, ["#F2C27A", "#D9984A", "#B5752E"], -7.4, 3.4); g.fill(cone);
      g.save(); g.clip(cone); for (let i = -6; i <= 6; i++) { line(g, "rgba(150,90,30,0.55)", i * 1.6 - 6, -8, i * 1.6 + 6, 4, 0.25); line(g, "rgba(150,90,30,0.55)", i * 1.6 + 6, -8, i * 1.6 - 6, 4, 0.25); } g.restore();
      stroke(g, cone, "rgba(120,70,20,0.6)", 0.3);
      const dy = ((t * 0.4) % 1) * 2.2;
      const dr = new Path2D(); dr.moveTo(1.8, -7); dr.lineTo(2.8, -7); dr.lineTo(2.6, -5.6 + dy); dr.quadraticCurveTo(2.2, -4.6 + dy, 1.9, -5.6 + dy); dr.closePath(); g.fillStyle = "#FF8FA8"; g.fill(dr);
      g.beginPath(); g.ellipse(0, -8.8, 4.6, 3.2, 0, 0, 2 * PI); for (let i = 0; i < 5; i++) { g.moveTo(-3.6 + i * 1.8 + 0.9, -7); g.arc(-3.6 + i * 1.8, -7, 0.9, 0, 2 * PI); }
      const r1 = g.createRadialGradient(-1.6, -10, 0.4, 0, -8.6, 5); r1.addColorStop(0, "#FFD6E0"); r1.addColorStop(0.6, "#FF8FA8"); r1.addColorStop(1, "#E0607E"); g.fillStyle = r1; g.fill();
      g.beginPath(); g.ellipse(-0.4, -12.8, 3.8, 3.3, 0, 0, 2 * PI);
      const r2 = g.createRadialGradient(-1.8, -14, 0.4, -0.4, -12.8, 4.2); r2.addColorStop(0, "#E0FFF4"); r2.addColorStop(0.6, "#7EE0B8"); r2.addColorStop(1, "#3FBF8F"); g.fillStyle = r2; g.fill();
      for (const [x, y, a, c] of [[-2, -13.6, 0.6, "#FF5C8A"], [0.8, -14.6, -0.4, "#FFD84A"], [1.8, -12.4, 1.2, "#6FB0FF"], [-1.2, -11.6, -0.9, "#B995F5"], [-2.6, -9.2, 0.3, "#FFD84A"], [1.6, -9.6, -0.6, "#6FB0FF"], [-0.4, -15.4, 1, "#FF5C8A"]]) line(g, c, x - cos(a) * 0.45, y - sin(a) * 0.45, x + cos(a) * 0.45, y + sin(a) * 0.45, 0.35);
      g.beginPath(); g.ellipse(-1.8, -14.2, 1, 0.6, -0.5, 0, 2 * PI); g.fillStyle = "rgba(255,255,255,0.6)"; g.fill();
      g.restore();
    } else if (kind === "pipoca") {
      // pipoca de cinema: balde listrado vermelho e branco, pipocas fofas transbordando e uma pipoca
      // pulando de vez em quando (na festa pulam várias)
      g.save(); g.rotate(-0.08);
      const kernel = (x, y, r) => { for (const [dx, dy] of [[-0.55, 0.2], [0.55, 0.2], [0, -0.45]]) { g.beginPath(); g.arc(x + dx * r, y + dy * r, r * 0.72, 0, 2 * PI); g.fillStyle = "#FFF6D8"; g.fill(); g.strokeStyle = "rgba(200,150,70,0.6)"; g.lineWidth = 0.18; g.stroke(); } circle(g, "#F5C451", r * 0.22, x + r * 0.3, y + r * 0.2); };
      for (const [x, y] of [[-7, -9.8], [-4.8, -10.6], [-2.4, -10.2], [0, -10.8], [2.2, -9.8], [-6, -11.8], [-3.4, -12.4], [-0.8, -12.2], [1.4, -11.8], [-2, -13.8]]) kernel(x, y, 1.3);
      const bk = new Path2D(); bk.moveTo(-8, -9); bk.lineTo(3, -9); bk.lineTo(1.6, 1.6); bk.lineTo(-6.6, 1.6); bk.closePath();
      g.save(); g.clip(bk); g.fillStyle = "#FFFFFF"; g.fillRect(-9, -10, 13, 12);
      for (let i = 0; i < 7; i += 2) { const st = new Path2D(); st.moveTo(-8 + i * 11 / 7, -9); st.lineTo(-8 + (i + 1) * 11 / 7, -9); st.lineTo(-6.6 + (i + 1) * 8.2 / 7, 1.6); st.lineTo(-6.6 + i * 8.2 / 7, 1.6); st.closePath(); g.fillStyle = "#E0303F"; g.fill(st); }
      g.fillStyle = hGrad(g, ["rgba(0,0,0,0.18)", "rgba(0,0,0,0)", "rgba(0,0,0,0)", "rgba(0,0,0,0.2)"], -8, 3); g.fillRect(-9, -10, 13, 12);
      g.restore(); stroke(g, bk, "rgba(110,20,30,0.55)", 0.3);
      const rim = new Path2D(); rim.roundRect(-8.4, -9.6, 11.8, 1.3, 0.6); g.fillStyle = "#FFFFFF"; g.fill(rim); stroke(g, rim, "rgba(110,20,30,0.5)", 0.25);
      const n = party ? 4 : 1;
      for (let i = 0; i < n; i++) { const ph = (t * (party ? 1.1 : 0.45) + i / n) % 1; if (ph < 0.7) { const u = ph / 0.7; kernel(-3 + (i - 1.5) * 2.4 + u * (i % 2 ? 2 : -2), -12 - sin(PI * u) * 8, 1.1); } }
      g.restore();
    } else if (kind === "violao") {
      // violãozinho: corpo de madeira em oito com boca escura e roseta, cavalete, braço com trastes,
      // cabeça com tarraxas e as cordas (na festa ele toca: as cordas vibram e saem notinhas)
      // (grande: corpo em oito largo com contorno escuro, boca com roseta, braço grosso e cordas)
      // (segurado pela cabeça do braço, de ponta-cabeça: o corpo fica em cima, para fora)
      g.save(); g.translate(GX - 0.2, 0.4); g.rotate(PI - 0.42); g.translate(0, 23.2);
      const hd = new Path2D(); hd.moveTo(-1.8, -26); hd.lineTo(1.8, -26); hd.lineTo(1.3, -21.4); hd.lineTo(-1.3, -21.4); hd.closePath(); g.fillStyle = "#3A2410"; g.fill(hd); stroke(g, hd, "rgba(30,15,5,0.85)", 0.4);
      for (const [x, y] of [[-2.2, -25], [2.2, -25], [-2, -22.8], [2, -22.8]]) circle(g, GOLD, 0.6, x, y);
      for (const [cy, rx, ry] of [[-3.4, 6.2, 5.6], [-11, 4.8, 4.4]]) { g.beginPath(); g.ellipse(0, cy, rx, ry, 0, 0, 2 * PI); g.strokeStyle = "rgba(70,30,5,0.9)"; g.lineWidth = 1; g.stroke(); }
      g.beginPath(); g.ellipse(0, -3.4, 6.2, 5.6, 0, 0, 2 * PI); g.ellipse(0, -11, 4.8, 4.4, 0, 0, 2 * PI);
      const rg = g.createRadialGradient(-2, -9, 0.6, 0, -6, 11); rg.addColorStop(0, "#F7C984"); rg.addColorStop(0.6, "#D98A3A"); rg.addColorStop(1, "#A85A1E"); g.fillStyle = rg; g.fill();
      g.beginPath(); g.arc(0, -7.6, 2.6, 0, 2 * PI); g.strokeStyle = GOLD; g.lineWidth = 0.5; g.stroke();
      circle(g, "#2A1608", 2, 0, -7.6);
      const brg = new Path2D(); brg.roundRect(-2.6, -2.6, 5.2, 1.4, 0.4); g.fillStyle = "#3A2410"; g.fill(brg);
      const nk = new Path2D(); nk.roundRect(-1.2, -22, 2.4, 12.2, 0.4); g.fillStyle = vGrad(g, ["#6D3D1F", "#4E2F15"], -22, -9.8); g.fill(nk); stroke(g, nk, "rgba(30,15,5,0.8)", 0.35);
      for (let y = -20.4; y < -10.5; y += 1.8) line(g, "#D7DAE6", -1.2, y, 1.2, y, 0.3, false);
      const vib = party ? sin(t * 40) * 0.25 : 0;
      for (let i = 0; i < 4; i++) { const x = -0.75 + i * 0.5; line(g, "rgba(255,255,255,0.9)", x, -21.6, x + vib * (i % 2 ? 1 : -1), -2, 0.18); }
      g.beginPath(); g.ellipse(-3.4, -4.6, 1.2, 2.4, 0.3, 0, 2 * PI); g.fillStyle = "rgba(255,255,255,0.3)"; g.fill();
      g.restore();
      if (party && !small) for (let i = 0; i < 2; i++) { const ph = (t * 0.8 + i / 2) % 1, x = -9 - i * 3 - ph * 2, y = -8 - ph * 9, a = sin(PI * ph); circle(g, `rgba(82,71,224,${a})`, 0.8, x, y); line(g, `rgba(82,71,224,${a})`, x + 0.7, y, x + 0.7, y - 2.6, 0.3); }
    } else if (kind === "pincel") {
      // pincel de pintor: cabo de madeira vermelho laqueado, virola prateada, cerdas e a ponta molhada de
      // tinta (a cor muda devagar) com a gota brilhando; na festa ele pinta no ar e respinga
      const paint = ["#5247E0", "#FF5C8A", "#2E9E72", "#F5B83D"][Math.floor(t * 0.25) % 4];
      // (grosso: cabo largo, virola larga e cerdas grandes com bastante tinta)
      g.save(); g.translate(GX, GY - 4.4); g.rotate(-0.25 + (party ? sin(t * 5) * 0.25 : 0));
      const hd = new Path2D(); hd.moveTo(-1.8, -9); hd.lineTo(1.8, -9); hd.lineTo(1.1, 5); hd.quadraticCurveTo(0, 6.8, -1.1, 5); hd.closePath(); g.fillStyle = hGrad(g, ["#9C1424", "#F05A5A", "#9C1424"], -1.8, 1.8); g.fill(hd); stroke(g, hd, "rgba(90,10,20,0.85)", 0.45);
      const fr = new Path2D(); fr.roundRect(-2.6, -13.6, 5.2, 5, 0.6); g.fillStyle = hGrad(g, ["#7E8698", "#FFFFFF", "#7E8698"], -2.6, 2.6); g.fill(fr); stroke(g, fr, "rgba(38,33,92,0.7)", 0.4);
      for (const y of [-12, -10.6]) line(g, "rgba(80,85,110,0.7)", -2.6, y, 2.6, y, 0.3);
      const br = new Path2D(); br.moveTo(-2.6, -13.4); br.bezierCurveTo(-3.6, -17, -1.8, -21, 0, -22.6); br.bezierCurveTo(1.8, -21, 3.6, -17, 2.6, -13.4); br.closePath();
      g.fillStyle = vGrad(g, ["#E8C48A", "#C9A060"], -22.6, -13.4); g.fill(br);
      g.save(); g.clip(br); g.fillStyle = paint; g.fillRect(-4, -23, 8, 5.6); g.beginPath(); g.ellipse(0, -17.4, 3.2, 1.2, 0, 0, 2 * PI); g.fill(); g.fillStyle = "rgba(255,255,255,0.35)"; g.fillRect(-1.6, -21.6, 0.7, 3.4); g.restore();
      for (const x of [-1.2, 0, 1.2]) line(g, "rgba(120,80,30,0.5)", x, -13.4, x * 1.3, -16.6, 0.25);
      stroke(g, br, "rgba(90,60,20,0.85)", 0.45);
      circle(g, paint, 0.9 + 0.2 * sin(t * 3), 0, -22.4); circle(g, "rgba(255,255,255,0.6)", 0.3, -0.25, -22.7);
      line(g, "rgba(255,255,255,0.45)", -0.8, -8, -0.5, 3.6, 0.5);
      if (party && !small) for (let i = 0; i < 3; i++) { const ph = (t * 1.3 + i / 3) % 1; circle(g, paint, 0.8 * (1 - ph), -2 - ph * 6 + i, -22 - ph * 3 + i * 1.5); }
      g.restore();
    }
    g.restore();
  }
  // ---- peças das auras novas: folha de bordo (outono), cifrão, moeda de ouro que gira e letras a traço
  function auraMaple() {
    const v = [0, -3, 0.7, -1.6, 1.9, -2.2, 1.5, -0.6, 2.8, -0.2, 1.6, 0.6, 1.8, 1.6, 0.4, 1.2, 0, 1.6, -0.4, 1.2, -1.8, 1.6, -1.6, 0.6, -2.8, -0.2, -1.5, -0.6, -1.9, -2.2, -0.7, -1.6];
    const p = new Path2D(); for (let i = 0; i < v.length; i += 2) i ? p.lineTo(v[i], v[i + 1]) : p.moveTo(v[i], v[i + 1]); p.closePath(); return p;
  }
  function drawAuraLeaf(g, x, y, s, rot, flip, c, al) {
    g.save(); g.translate(x, y); g.rotate(rot); g.scale(s * Math.max(0.2, abs(flip)), s); g.globalAlpha *= al;
    const p = auraMaple(); g.fillStyle = vGrad(g, [c[0], c[1]], -3, 1.6); g.fill(p); g.strokeStyle = "rgba(110,45,15,0.5)"; g.lineWidth = 0.25; g.lineJoin = "round"; g.stroke(p);
    for (const [x1, y1] of [[0, -2.4], [1.9, -0.5], [-1.9, -0.5]]) line(g, "rgba(110,45,15,0.45)", 0, 1.2, x1, y1, 0.2);
    line(g, c[1], 0, 1.2, 0, 3, 0.35);
    g.restore();
  }
  function auraDollar() {
    const p = new Path2D(); p.moveTo(0.95, -1.05); p.quadraticCurveTo(0.65, -1.6, 0, -1.6); p.quadraticCurveTo(-0.9, -1.6, -0.9, -0.8); p.quadraticCurveTo(-0.9, -0.05, 0, 0);
    p.quadraticCurveTo(0.95, 0.05, 0.95, 0.8); p.quadraticCurveTo(0.95, 1.6, 0, 1.6); p.quadraticCurveTo(-0.7, 1.6, -1, 1.05); p.moveTo(0, -2.2); p.lineTo(0, 2.2); return p;
  }
  function drawAuraCoin(g, x, y, r, spin, al) {
    // moeda de ouro girando: achata (cos do giro) e mostra a borda grossa do lado de trás
    const sq = cos(spin), w = Math.max(0.12, abs(sq)), th = r * 0.22 * (1 - w) * (sq > 0 ? 1 : -1);
    g.save(); g.translate(x, y); g.globalAlpha *= al;
    g.beginPath(); g.ellipse(th, 0, r * w, r, 0, 0, 2 * PI); g.fillStyle = "#9A6508"; g.fill();
    g.beginPath(); g.ellipse(0, 0, r * w, r, 0, 0, 2 * PI);
    const fg = g.createLinearGradient(-r, -r, r, r); fg.addColorStop(0, "#FFF1BF"); fg.addColorStop(0.45, "#FFD24A"); fg.addColorStop(1, "#C98A12"); g.fillStyle = fg; g.fill();
    g.strokeStyle = "#A86A08"; g.lineWidth = Math.max(0.2, r * 0.12); g.stroke();
    if (w > 0.4) {
      g.beginPath(); g.ellipse(0, 0, r * 0.7 * w, r * 0.7, 0, 0, 2 * PI); g.strokeStyle = "rgba(168,106,8,0.7)"; g.lineWidth = r * 0.08; g.stroke();
      g.save(); g.scale(w * r / 3.6, r / 3.6); g.strokeStyle = "rgba(150,90,5,0.85)"; g.lineWidth = 0.6; g.lineCap = "round"; g.stroke(auraDollar()); g.restore();
    }
    g.beginPath(); g.ellipse(-r * 0.35 * w, -r * 0.4, r * 0.3 * w, r * 0.16, -0.6, 0, 2 * PI); g.fillStyle = "rgba(255,255,255,0.7)"; g.fill();
    g.restore();
  }
  // letras a traço (cada letra: lista de linhas quebradas numa caixa de -1..1 por -1.4..1.4)
  const AURA_GLYPH = {
    S: [[0.9, -1.4, -0.9, -1.4, -0.9, 0, 0.9, 0, 0.9, 1.4, -0.9, 1.4]], I: [[0, -1.4, 0, 1.4]], M: [[-1, 1.4, -1, -1.4, 0, 0.2, 1, -1.4, 1, 1.4]],
    N: [[-1, 1.4, -1, -1.4, 1, 1.4, 1, -1.4]], A: [[-1, 1.4, 0, -1.4, 1, 1.4], [-0.5, 0.4, 0.5, 0.4], [-0.8, -2.1, -0.3, -2.5, 0.3, -2.1, 0.8, -2.5]], O: [[-1, -1.4, 1, -1.4, 1, 1.4, -1, 1.4, -1, -1.4]],
  };
  function auraWord(g, word, cx, cy, s, color, w) {
    const n = word.length;
    [...word].forEach((ch, j) => {
      const ox = cx + (j - (n - 1) / 2) * 2.9 * s;
      for (const pl of AURA_GLYPH[ch]) { g.beginPath(); for (let i = 0; i < pl.length; i += 2) { const x = ox + pl[i] * s, y = cy + pl[i + 1] * s; i ? g.lineTo(x, y) : g.moveTo(x, y); } g.strokeStyle = color; g.lineWidth = w; g.lineCap = "round"; g.lineJoin = "round"; g.stroke(); }
    });
  }
  // ---- aura: em volta do Folha (fora do corpo); "behind" desenha a parte de trás
  function drawAura(g, t, kind, small, burst, behind) {
    if (!kind || kind === "nenhum") return;
    const boost = burst > 0 ? 1 + burst * 1.5 : 1;
    if (kind === "brilhos" && !behind) {
      // brilhos de quatro pontas com halo, em dourado e branco, acendendo e apagando
      const spots = [[10, 40], [90, 36], [6, 70], [95, 66], [22, 20], [80, 16]];
      for (let i = 0; i < (small ? 3 : 6); i++) {
        const ph = (t * 0.8 + i * 0.19) % 1, a = sin(PI * ph), [x, y] = spots[i], r = (small ? 4.6 : 4.2) * a * boost;
        if (!small) { const halo = g.createRadialGradient(x, y, 0.2, x, y, r * 1.6); halo.addColorStop(0, rgba(i % 2 ? "#FFFFFF" : GOLD, 0.45 * a)); halo.addColorStop(1, rgba(GOLD, 0)); g.fillStyle = halo; g.beginPath(); g.arc(x, y, r * 1.6 + 0.01, 0, 2 * PI); g.fill(); }
        star4(g, x, y, r, i % 2 ? `rgba(255,255,255,${a})` : rgba(GOLD, a));
      }
    } else if (kind === "coracoes" && !behind) {
      for (let i = 0; i < (small ? 2 : 4); i++) {
        const ph = (t * 0.35 + i / 4) % 1, side = i % 2 ? 1 : -1, x = 50 + side * (40 + sin(ph * 6 + i) * 3), y = 80 - ph * 60, r = 3.4 * boost * (0.7 + ph * 0.5), a = sin(PI * ph) * 0.95;
        g.save(); g.translate(x, y); g.rotate(sin(t * 2 + i) * 0.25); g.fillStyle = vGrad(g, [rgba("#FF8FA3", a), rgba("#E0475E", a)], -r, r); g.fill(heart(0, 0, r));
        g.beginPath(); g.ellipse(-r * 0.4, -r * 0.35, r * 0.25, r * 0.16, -0.6, 0, 2 * PI); g.fillStyle = `rgba(255,255,255,${0.7 * a})`; g.fill(); g.restore();
      }
    } else if (kind === "notas" && !behind) {
      // notas musicais (colcheia e semicolcheia ligadas) subindo e balançando
      for (let i = 0; i < (small ? 2 : 4); i++) {
        const ph = (t * 0.32 + i / 4) % 1, side = i % 2 ? 1 : -1, x = 50 + side * (38 + sin(ph * 5 + i) * 4), y = 76 - ph * 56, a = sin(PI * ph);
        const c = i % 2 ? `rgba(58,47,196,${a})` : `rgba(149,99,255,${a})`;
        g.save(); g.translate(x, y); g.rotate(sin(t * 2 + i) * 0.25); g.scale(1.6 * boost, 1.6 * boost);
        g.beginPath(); g.ellipse(0, 0, 1.7, 1.25, -0.45, 0, 2 * PI); g.fillStyle = c; g.fill();
        line(g, c, 1.45, -0.3, 1.45, -6.2, 0.55);
        if (i % 2) {
          g.beginPath(); g.ellipse(4.6, -1, 1.7, 1.25, -0.45, 0, 2 * PI); g.fill(); line(g, c, 6.05, -1.3, 6.05, -7.2, 0.55);
          const beam = new Path2D(); beam.moveTo(1.45, -6.2); beam.lineTo(6.05, -7.2); beam.lineTo(6.05, -6.1); beam.lineTo(1.45, -5.1); beam.closePath(); g.fill(beam);
        } else { const f = new Path2D(); f.moveTo(1.45, -6.2); f.bezierCurveTo(3.6, -5.4, 4.2, -3.6, 3, -2.2); stroke(g, f, c, 0.6); }
        g.beginPath(); g.ellipse(-0.6, -0.4, 0.5, 0.3, -0.45, 0, 2 * PI); g.fillStyle = `rgba(255,255,255,${0.6 * a})`; g.fill();
        g.restore();
      }
    } else if (kind === "avioes") {
      // aviõezinhos de papel (Essencial, iguais ao app): três aviões dobrados de folha pautada voando em volta do
      // Folha. Asa de cima com pauta, margem e um adesivo de estrela; quilha e asa de baixo na sombra; o avião
      // inclina nas curvas, deixa uma fita que afina com brilhinhos e projeta a sombra no chão.
      const pos = (a) => [50 + cos(a) * 50, 54 + sin(a) * 20 + sin(2 * a) * 6];
      for (let i = 0; i < 3; i++) {
        const a = t * 1.25 + i * 2 * PI / 3, front = sin(a) > 0; if (front === behind) continue;
        const [x, y] = pos(a), [ax, ay] = pos(a + 0.04), heading = Math.atan2(ay - y, ax - x);
        const s = (front ? 9 : 6.4) * boost, al = front ? 1 : 0.72, roll = 0.55 + 0.45 * abs(sin(a));
        if (!small) {
          let [px, py] = [x, y];
          for (let k = 1; k <= 14; k++) { const [qx, qy] = pos(a - k * 0.05), f = 1 - k / 15; line(g, `rgba(47,91,211,${0.42 * al * f})`, px, py, qx, qy, 1.7 * f + 0.15); px = qx; py = qy; }
          for (const k of [5, 10]) { const [qx, qy] = pos(a - k * 0.05); star4(g, qx, qy, (0.9 + 0.5 * sin(t * 7 + i + k)) * al, `rgba(255,255,255,${0.9 * al})`); }
          if (front) { g.fillStyle = `rgba(38,33,92,${0.12 * (y / 95)})`; g.beginPath(); g.ellipse(x, 93.6 + s * 0.14, s * 0.7, s * 0.14, 0, 0, 2 * PI); g.fill(); }
        }
        g.save(); g.translate(x, y); g.rotate(heading); g.scale(1, roll); g.globalAlpha *= al;
        const P = (...v) => { const p = new Path2D(); p.moveTo(v[0] * s, v[1] * s); for (let j = 2; j < v.length; j += 2) p.lineTo(v[j] * s, v[j + 1] * s); p.closePath(); return p; };
        const bottom = P(1, 0, -0.55, 0.12, -0.9, 0.5), keel = P(1, 0, -0.45, -0.04, -0.72, 0.24), top = P(1, 0, -0.82, -0.64, -0.45, -0.04), w = small ? 0.8 : 0.42;
        g.fillStyle = vGrad(g, ["#C9D7FF", "#A9BEF5"], 0, s * 0.5); g.fill(bottom); stroke(g, bottom, "#26215C", w);
        g.fillStyle = vGrad(g, ["#8FA8EE", "#6F8FE0"], -s * 0.05, s * 0.25); g.fill(keel); stroke(g, keel, "#26215C", w);
        g.fillStyle = vGrad(g, ["#FFFFFF", "#F4F7FF"], -s * 0.64, 0); g.fill(top);
        if (!small) {
          g.save(); g.clip(top);
          for (let k = 0; k < 4; k++) line(g, "rgba(111,143,224,0.6)", -s, -s * (0.1 + k * 0.14), s, -s * (0.1 + k * 0.14), 0.28);
          line(g, "rgba(242,163,163,0.85)", -s * 0.62, -s, -s * 0.62, s, 0.32);
          g.fillStyle = "#FFD23F"; g.fill(star5(g, -s * 0.3, -s * 0.3, s * 0.13));
          g.restore();
        }
        stroke(g, top, "#26215C", w);
        if (!small) line(g, "rgba(255,255,255,0.9)", s * 0.92, -s * 0.01, -s * 0.4, -s * 0.05, 0.5);
        g.restore();
      }
    } else if (kind === "supernova") {
      // a supernova do Pro: raios de luz girando e um halo que pulsa atrás; seis páginas de ouro em órbita
      // (passam por trás e pela frente) e uma coroa de três estrelas piscando por cima
      if (behind) {
        const pr = 54 * (1 + 0.06 * sin(t * 2.4)) * boost;
        const halo = g.createRadialGradient(50, 56, 0, 50, 56, pr); halo.addColorStop(0, "rgba(255,224,122,0.5)"); halo.addColorStop(0.5, "rgba(255,180,33,0.2)"); halo.addColorStop(1, "rgba(255,180,33,0)");
        g.fillStyle = halo; g.beginPath(); g.arc(50, 56, pr, 0, 2 * PI); g.fill();
        if (!small) {
          g.save(); g.translate(50, 56); g.rotate(t * 12 * PI / 180);
          for (let i = 0; i < 12; i++) {
            const a = i * 2 * PI / 12, r = 74, ray = g.createRadialGradient(0, 0, 0, 0, 0, r);
            ray.addColorStop(0, `rgba(255,224,122,${0.16 + 0.08 * sin(t * 2 + i)})`); ray.addColorStop(1, "rgba(255,224,122,0)");
            g.fillStyle = ray; g.beginPath(); g.moveTo(0, 0); g.lineTo(cos(a - 0.06) * r, sin(a - 0.06) * r); g.lineTo(cos(a + 0.06) * r, sin(a + 0.06) * r); g.closePath(); g.fill();
          }
          g.restore();
        }
      }
      for (let i = 0; i < 6; i++) {
        const a = t * 1.1 + i * 2 * PI / 6, front = sin(a) > 0; if (front === behind) continue;
        const x = 50 + cos(a) * 52, y = 62 + sin(a) * 16, s = (front ? 1 : 0.7) * boost;
        g.save(); g.translate(x, y); g.rotate(cos(a) * 25 * PI / 180);
        const pg = new Path2D(); pg.roundRect(-2.6 * s, -3.4 * s, 5.2 * s, 6.8 * s, 0.6 * s);
        g.fillStyle = front ? vGrad(g, ["#FFFDF2", "#FFE9B0"], -3.4 * s, 3.4 * s) : "rgba(255,233,176,0.6)"; g.fill(pg);
        stroke(g, pg, rgba(GOLD, front ? 1 : 0.6), 0.45);
        if (front && !small) { line(g, "rgba(201,138,18,0.6)", -1.6 * s, -1 * s, 1.6 * s, -1 * s, 0.3); line(g, "rgba(201,138,18,0.6)", -1.6 * s, 0.6 * s, 1.2 * s, 0.6 * s, 0.3); }
        g.restore();
        if (front && !small) star4(g, x - cos(a + 0.35) * 5, y - sin(a + 0.35) * 2, 1.2, "rgba(255,224,122,0.8)");
      }
      if (!behind) [[36, 15], [50, 8], [64, 15]].forEach(([x, y], i) => {
        const r = (i === 1 ? 3.6 : 2.6) * (0.65 + 0.35 * sin(t * 3 + i * 2)) * boost;
        const gl = g.createRadialGradient(x, y, 0, x, y, r * 2.2); gl.addColorStop(0, "rgba(255,224,122,0.6)"); gl.addColorStop(1, "rgba(255,224,122,0)"); g.fillStyle = gl; g.beginPath(); g.arc(x, y, r * 2.2, 0, 2 * PI); g.fill();
        star4(g, x, y, r, i === 1 ? "#FFFFFF" : GOLD);
      });
    } else if (kind === "orbita") {
      // três estrelas em órbita: passam por trás e pela frente do Folha
      for (let i = 0; i < 3; i++) {
        const a = t * 1.6 + i * 2 * PI / 3, front = sin(a) > 0; if (front === behind) continue;
        const x = 50 + cos(a) * 50, y = 80 + sin(a) * 17, r = (front ? 3.8 : 2.6) * boost;
        g.fillStyle = front ? vGrad(g, GOLD3, y - r, y + r) : "rgba(245,184,61,0.55)"; g.fill(star5(g, x, y, r));
      }
    } else if (kind === "chamas" && behind) {
      for (let i = 0; i < (small ? 2 : 4); i++) {
        const side = i % 2 ? 1 : -1, row = Math.floor(i / 2), x = 50 + side * (40 + row * 7), base = 94 - row * 12;
        const hgt = (13 + 3 * sin(t * 9 + i * 1.7)) * boost, wd = 4.2;
        const p = new Path2D(); p.moveTo(x - wd, base); p.bezierCurveTo(x - wd * 1.1, base - hgt * 0.5, x - wd * 0.2 + sin(t * 7 + i) * 0.8, base - hgt * 0.7, x + sin(t * 6 + i) * 1.2, base - hgt);
        p.bezierCurveTo(x + wd * 0.6, base - hgt * 0.6, x + wd * 1.1, base - hgt * 0.45, x + wd, base); p.quadraticCurveTo(x, base + 1.8, x - wd, base);
        g.fillStyle = vGrad(g, ["#FFE07A", "#FF9F3A", "#F2552C"], base - hgt, base); g.fill(p);
      }
    } else if (kind === "confete" && !behind) {
      // confete de verdade: papeizinhos que giram em 3D (achatam e viram), bolinhas e serpentinas
      const colors = [SPARK, MINT, CHEEK, "#6FB0FF", "#B995F5"];
      for (let i = 0; i < (small ? 6 : 14); i++) {
        const ph = (t * 0.26 + i * 0.071) % 1, x = 6 + ((i * 37) % 88) + sin(t * 1.6 + i) * 3.5, y = -4 + ph * 104;
        if (y > 40 && y < 92 && x > 12 && x < 88) continue;   // não cai na frente do rosto
        const c = colors[i % 5], flip = cos(t * 5 + i * 1.3);
        g.save(); g.translate(x, y); g.rotate(t * 2 + i);
        if (i % 3 === 0) { circle(g, rgba(c, 0.95), 1 * boost, 0, 0); }
        else if (i % 3 === 1) { g.scale(flip, 1); g.fillStyle = rgba(c, 0.95); g.fillRect(-2 * boost, -1, 4 * boost, 2); g.fillStyle = "rgba(255,255,255,0.35)"; g.fillRect(-2 * boost, -1, 4 * boost, 0.6); }
        else { const sp = new Path2D(); sp.moveTo(-3, 0); sp.bezierCurveTo(-1.5, -1.6, -0.5, 1.6, 1, 0); sp.bezierCurveTo(2, -1.4, 2.6, 0.8, 3.4, 0); stroke(g, sp, rgba(c, 0.95), 0.7 * boost); }
        g.restore();
      }
    } else if (kind === "runas") {
      // runas mágicas: um anel de símbolos que brilham, gira devagar e passa por trás e pela frente
      const R = [[0, -2, 0, 2, -1.4, -0.6, 1.4, 0.6], [-1.4, -2, 1.4, -2, 0, -2, 0, 2], [-1.2, 2, 0, -2, 0, -2, 1.2, 2], [-1.4, -1, 1.4, -1, -1.4, 1, 1.4, 1], [0, -2, 0, 2, 0, 0, 1.4, -1.4], [-1.4, 2, -1.4, -2, -1.4, 0, 1.4, 0]];
      for (let i = 0; i < 10; i++) {
        const a = t * 0.6 + i * 2 * PI / 10, front = sin(a) > 0; if (front === behind) continue;
        const x = 50 + cos(a) * 52, y = 82 + sin(a) * 15 + sin(t * 1.3) * 0.8, k = (front ? 1 : 0.7) * boost, al = (front ? 0.95 : 0.45) * (0.7 + 0.3 * sin(t * 3 + i));
        const glowG = g.createRadialGradient(x, y, 0.2, x, y, 4.6 * k); glowG.addColorStop(0, `rgba(126,224,184,${0.55 * al})`); glowG.addColorStop(1, "rgba(149,99,255,0)");
        g.fillStyle = glowG; g.beginPath(); g.arc(x, y, 4.6 * k, 0, 2 * PI); g.fill();
        const r = R[i % R.length]; g.save(); g.translate(x, y); g.scale(k, k);
        g.strokeStyle = `rgba(40,150,110,${al})`; g.lineWidth = 0.6; g.lineCap = "round";
        g.beginPath(); g.moveTo(r[0], r[1]); g.lineTo(r[2], r[3]); g.moveTo(r[4], r[5]); g.lineTo(r[6], r[7]); g.stroke(); g.restore();
      }
      // faíscas de luz subindo do círculo do chão
      if (!behind) for (let i = 0; i < (small ? 4 : 9); i++) {
        const ph = (t * 0.45 + i * 0.113) % 1, side = i % 2 ? 1 : -1, x = 50 + side * (20 + ((i * 7) % 24)) + sin(t * 2 + i) * 1.2, y = 94 - ph * 40, a = sin(PI * ph);
        circle(g, `rgba(${i % 3 ? "126,224,184" : "185,149,245"},${0.85 * a})`, (small ? 1.3 : 0.9) * (1 - ph * 0.4), x, y);
      }
    } else if (kind === "fogos" && !behind) {
      // fogos de artifício de verdade: o foguete sobe com rastro de faíscas, estoura em dois anéis de cores
      // (com clarão no centro) e a purpurina cai piscando. Seis pontos em volta, longe do rosto.
      const cols = [["#FFD84A", "#FFFFFF"], ["#FF8FA3", "#FFD84A"], ["#7EE0B8", "#FFFFFF"], ["#6FB0FF", "#B995F5"], ["#B995F5", "#FF8FA3"]];
      const spots = [[10, 20], [90, 14], [22, -2], [80, -6], [-4, 46], [104, 40]];
      for (let i = 0; i < (small ? 3 : 6); i++) {
        const cyc = t * 0.36 + i * 0.37, ph = cyc % 1, [c1, c2] = cols[(i + Math.floor(cyc)) % 5], [cx, cy] = spots[i];
        if (ph < 0.2) {
          const u = ph / 0.2, y = cy + 34 * (1 - u * (2 - u));
          for (let j = 0; j < 4; j++) circle(g, rgba(c1, 0.8 - j * 0.2), 0.6 - j * 0.08, cx + sin(t * 40 + j * 2) * 0.6, y + 1.6 + j * 2);
          circle(g, "#FFFFFF", 0.95, cx, y);
          continue;
        }
        const u = (ph - 0.2) / 0.8, e = 1 - (1 - u) ** 3, rr = (4 + e * 13) * boost * (small ? 0.8 : 1), al = clamp(1.3 - u * 1.3, 0, 1), fall = u * u * 6;
        if (u < 0.14) { const fl = 1 - u / 0.14; const gl = g.createRadialGradient(cx, cy, 0, cx, cy, 7); gl.addColorStop(0, `rgba(255,255,255,${fl})`); gl.addColorStop(1, rgba(c1, 0)); g.fillStyle = gl; g.beginPath(); g.arc(cx, cy, 7, 0, 2 * PI); g.fill(); }
        for (let j = 0; j < 14; j++) {
          const a = j * 2 * PI / 14 + i, px = cx + cos(a) * rr, py = cy + sin(a) * rr + fall;
          line(g, rgba(c1, al * 0.5), cx + cos(a) * rr * 0.62, cy + sin(a) * rr * 0.62 + fall * 0.6, px, py, 0.85);
          circle(g, rgba(j % 2 ? c2 : c1, al), 0.95, px, py);
          if (!small && u > 0.35 && (j + Math.floor(t * 10)) % 3 === 0) star4(g, px + cos(a) * 1.5, py + 2.5 + u * 4, 1.3, rgba("#FFFFFF", al));
        }
        for (let j = 0; j < 8; j++) { const a = j * PI / 4 + i + 0.4, r2 = rr * 0.42; circle(g, rgba(c2, al), 0.7, cx + cos(a) * r2, cy + sin(a) * r2 + fall * 0.5); }
      }
    } else if (kind === "giroflex") {
      // giroflex: atrás dele, a luz vermelha e a azul se revezando, com os fachos varrendo a partir dos cantos de cima
      const redOn = (t * 2.2) % 1 < 0.5, flash = sin(PI * ((t * 4.4) % 1));
      if (behind) for (const [sd, col] of [[-1, "255,60,70"], [1, "60,120,255"]]) {
        const on = sd < 0 ? redOn : !redOn, a = (on ? 0.72 : 0.14) * (0.6 + 0.4 * flash) * boost, x = 50 + sd * 44;
        const gl = g.createRadialGradient(x, 58, 1, x, 58, 40); gl.addColorStop(0, `rgba(${col},${a})`); gl.addColorStop(1, `rgba(${col},0)`); g.fillStyle = gl; g.beginPath(); g.arc(x, 58, 40, 0, 2 * PI); g.fill();
        g.save(); g.translate(50 + sd * 56, -6); g.rotate(PI / 2 - sd * (0.45 + 0.35 * sin(t * 3 + (sd > 0 ? PI : 0))));
        const bm = g.createLinearGradient(0, 0, 90, 0); bm.addColorStop(0, `rgba(${col},${(on ? 0.62 : 0.14) * boost})`); bm.addColorStop(1, `rgba(${col},0)`);
        g.fillStyle = bm; g.beginPath(); g.moveTo(0, 0); g.lineTo(90, -12); g.lineTo(90, 12); g.closePath(); g.fill(); g.restore();
      }
      else if (!small) for (let i = 0; i < 4; i++) { const ph = (t * 0.6 + i / 4) % 1, sd = i % 2 ? 1 : -1; star4(g, 50 + sd * (40 + i * 2), 30 + i * 12, 1.8 * sin(PI * ph), sd < 0 ? `rgba(255,120,130,${sin(PI * ph)})` : `rgba(120,170,255,${sin(PI * ph)})`); }
    } else if (kind === "abc") {
      // letras e contas de giz subindo devagar dos lados (nunca na frente do rosto), com pozinho de giz
      if (!behind) {
        const G = [[[-1.2, 1.6, 0, -1.6], [0, -1.6, 1.2, 1.6], [-0.6, 0.4, 0.6, 0.4]], [[-1.2, 0, 1.2, 0], [0, -1.2, 0, 1.2]], [[-1.2, -0.5, 1.2, -0.5], [-1.2, 0.5, 1.2, 0.5]], [[-0.5, -1, 0, -1.6], [0, -1.6, 0, 1.6]], [[-1, -1, 1, 1], [-1, 1, 1, -1]], [[-1.4, 0.2, -0.6, 1.6], [-0.6, 1.6, 0.2, -1.6], [0.2, -1.6, 1.6, -1.6]], [[-1.4, -1.2, 1.4, -1.2], [-0.6, -1.2, -0.8, 1.6], [0.6, -1.2, 0.8, 1.6]]];
        const C = ["255,255,255", "190,245,220", "255,236,160", "255,200,215"];
        for (let i = 0; i < (small ? 5 : 10); i++) {
          const ph = (t * 0.22 + i * 0.1) % 1, sd = i % 2 ? 1 : -1, x = 50 + sd * (36 + ((i * 7) % 14)) + sin(t * 1.2 + i) * 2, y = 96 - ph * 92;
          if (y > 40 && y < 92 && x > 14 && x < 86) continue;
          const a = sin(PI * ph), sc = (1.2 + (i % 3) * 0.25) * boost;
          g.save(); g.translate(x, y); g.rotate(sin(t + i) * 0.3); g.scale(sc, sc); g.lineCap = "round";
          for (const [w, col] of [[1.1, `rgba(29,22,112,${0.3 * a})`], [0.5, `rgba(${C[i % 4]},${0.95 * a})`]]) { g.strokeStyle = col; g.lineWidth = w; g.beginPath(); for (const [x0, y0, x1, y1] of G[i % G.length]) { g.moveTo(x0, y0); g.lineTo(x1, y1); } g.stroke(); }
          g.restore();
          if (!small) circle(g, `rgba(255,255,255,${0.4 * a})`, 0.4, x + 1.6, y + 2.2);
        }
      }
    } else if (kind === "graficos") {
      // gráficos de controle flutuando dos lados (a última barra sobe e desce) e moedas girando para cima
      if (!behind) {
        for (const [cx, cy, p0] of [[-4, 60, 0], [98, 20, 0.5]]) {
          const y = cy + sin(t * 1.2 + p0 * 6) * 1.4, grow = 0.5 + 0.5 * sin(t * 1.6 + p0 * 6);
          const card = new Path2D(); card.roundRect(cx - 6, y - 6, 12, 10, 1.6); g.fillStyle = "rgba(255,255,255,0.92)"; g.fill(card); stroke(g, card, "rgba(29,22,112,0.35)", 0.3);
          [0.35, 0.55, 0.75, 1].forEach((h, i) => { const bh = 7 * h * (i === 3 ? 0.7 + 0.3 * grow : 1); g.fillStyle = i === 3 ? "#2E9E72" : "#7B8CFF"; g.fillRect(cx - 4.4 + i * 2.4, y + 2.6 - bh, 1.6, bh); });
          const ar = new Path2D(); ar.moveTo(cx - 4.6, y + 0.6); ar.lineTo(cx - 1.6, y - 1.4); ar.lineTo(cx + 0.6, y - 0.6); ar.lineTo(cx + 4, y - 4.2); stroke(g, ar, "#F5B83D", 0.5);
        }
        if (!small) for (let i = 0; i < 6; i++) {
          const ph = (t * 0.3 + i / 6) % 1, sd = i % 2 ? 1 : -1, x = 50 + sd * (40 + ((i * 5) % 10)), y = 94 - ph * 60, a = sin(PI * ph), sq = cos(t * 4 + i);
          g.save(); g.translate(x, y); g.scale(Math.max(0.15, Math.abs(sq)), 1); circle(g, `rgba(181,122,12,${a})`, 1.9 * boost, 0, 0); circle(g, `rgba(255,216,119,${a})`, 1.5 * boost, 0, 0); g.restore();
          if (Math.abs(sq) > 0.6) line(g, `rgba(181,122,12,${a})`, x, y - 0.9 * boost, x, y + 0.9 * boost, 0.35);
        }
      }
    } else if (kind === "batimentos") {
      // batimentos: atrás dele, a linha do monitor cardíaco atravessa a cena com o pulso de luz correndo;
      // na frente, coraçõezinhos que pulsam no ritmo e cruzinhas subindo
      const beat = (t * 1.25) % 1, thump = Math.exp(-((beat - 0.05) ** 2) / 0.002) + 0.6 * Math.exp(-((beat - 0.22) ** 2) / 0.002);
      if (behind) {
        const y0 = 66, ecg = (x) => { const u = ((x + 20) % 40) / 40; return u < 0.38 ? 0 : u < 0.43 ? -(u - 0.38) * 40 : u < 0.5 ? -2 + (u - 0.43) * 200 : u < 0.56 ? 12 - (u - 0.5) * 320 : u < 0.62 ? -7.2 + (u - 0.56) * 120 : u < 0.72 ? sin((u - 0.62) * PI / 0.1) * 2 : 0; };
        const p = new Path2D(); for (let x = -16; x <= 116; x += 0.5) { const y = y0 - ecg(x) * 0.9; if (x === -16) p.moveTo(x, y); else p.lineTo(x, y); }
        stroke(g, p, "rgba(63,191,143,0.25)", small ? 2.6 : 2); stroke(g, p, "rgba(126,224,184,0.85)", small ? 1.2 : 0.7);
        // o pulso de luz correndo pela linha
        const hx = -16 + ((t * 52) % 132), hy = y0 - ecg(hx) * 0.9;
        const gl = g.createRadialGradient(hx, hy, 0.2, hx, hy, 7); gl.addColorStop(0, "rgba(220,255,240,0.95)"); gl.addColorStop(1, "rgba(126,224,184,0)"); g.fillStyle = gl; g.beginPath(); g.arc(hx, hy, 7, 0, 2 * PI); g.fill();
        circle(g, "#FFFFFF", 0.9, hx, hy);
      } else {
        for (const [x, y, r0, ph] of [[8, 46, 3, 0], [92, 40, 3.4, 0.5], [12, 86, 2.4, 0.3], [90, 84, 2.6, 0.8]]) {
          const r = r0 * (1 + 0.28 * thump * boost) * (small ? 1.2 : 1);
          g.save(); g.translate(x, y + sin(t * 1.4 + ph * 6) * 1.2); g.fillStyle = vGrad(g, ["#FF8FA3", "#E0475E"], -r, r); g.fill(heart(0, 0, r));
          g.beginPath(); g.ellipse(-r * 0.4, -r * 0.35, r * 0.25, r * 0.16, -0.6, 0, 2 * PI); g.fillStyle = "rgba(255,255,255,0.7)"; g.fill(); g.restore();
        }
        if (!small) for (let i = 0; i < 5; i++) {
          const ph = (t * 0.3 + i / 5) % 1, side = i % 2 ? 1 : -1, x = 50 + side * (42 + ((i * 5) % 9)), y = 92 - ph * 70, a = sin(PI * ph), s = 1.2 * boost;
          g.fillStyle = `rgba(255,255,255,${0.9 * a})`; g.fillRect(x - 0.45 * s, y - 1.5 * s, 0.9 * s, 3 * s); g.fillRect(x - 1.5 * s, y - 0.45 * s, 3 * s, 0.9 * s);
          g.fillStyle = `rgba(63,191,143,${0.9 * a})`; g.fillRect(x - 0.25 * s, y - 1.2 * s, 0.5 * s, 2.4 * s); g.fillRect(x - 1.2 * s, y - 0.25 * s, 2.4 * s, 0.5 * s);
        }
      }
    } else if (kind === "galaxia") {
      // anel de estrelinhas e poeira cósmica em volta (metade passa por trás)
      for (let i = 0; i < 18; i++) {
        const a = t * 0.7 + i * 2 * PI / 18, front = sin(a) > 0; if (front === behind) continue;
        const x = 50 + cos(a) * 54, y = 80 + sin(a) * 17, c = i % 3 === 0 ? "#FFFFFF" : i % 3 === 1 ? "#B995F5" : "#6FB0FF";
        circle(g, rgba(c, front ? 0.95 : 0.5), (front ? 1.3 : 0.8) * boost, x, y);
        if (i % 6 === 0) star4(g, x, y, (front ? 3.4 : 2.2) * boost, rgba(c, front ? 0.9 : 0.5));
      }
    } else if (kind === "patente") {
      // estrelas da patente (militar): cinco estrelas douradas em arco sobre a cabeça, com sombra, contorno,
      // realce e um brilho que passa de uma para a outra; atrás, um leque suave de luz dourada
      const n = small ? 3 : 5;
      if (behind) {
        const gl = g.createRadialGradient(50, 10, 2, 50, 10, 46); gl.addColorStop(0, "rgba(255,224,122,0.4)"); gl.addColorStop(1, "rgba(255,224,122,0)");
        g.fillStyle = gl; g.beginPath(); g.arc(50, 10, 46, 0, 2 * PI); g.fill();
      } else for (let i = 0; i < n; i++) {
        const u = i / (n - 1), a = PI * (1.15 + u * 0.7), x = 50 + cos(a) * 44, y = 30 + sin(a) * 30;
        const tw = 0.5 + 0.5 * sin(t * 3 - i * 0.9), r = (i === (n - 1) / 2 ? 4.6 : 3.5) * (0.92 + 0.08 * tw) * boost * (small ? 1.2 : 1);
        const gl = g.createRadialGradient(x, y, 0.2, x, y, r * 2); gl.addColorStop(0, `rgba(255,224,122,${0.4 * tw})`); gl.addColorStop(1, "rgba(255,224,122,0)"); g.fillStyle = gl; g.beginPath(); g.arc(x, y, r * 2, 0, 2 * PI); g.fill();
        g.fillStyle = "rgba(14,10,69,0.25)"; g.fill(star5(g, x + 0.5, y + 0.7, r));
        const st = star5(g, x, y, r); g.fillStyle = vGrad(g, GOLD3, y - r, y + r); g.fill(st); g.lineJoin = "round"; stroke(g, st, "#A86A08", small ? 0.8 : 0.45);
        g.fillStyle = "rgba(255,255,255,0.4)"; g.fill(star5(g, x - r * 0.1, y - r * 0.15, r * 0.42));
        if (tw > 0.85 && !small) star4(g, x - r * 0.45, y - r * 0.55, 1.8 * (tw - 0.85) / 0.15, "rgba(255,255,255,0.95)");
      }
    } else if (kind === "colunas") {
      // colunas do fórum (tribunais): atrás dele, duas colunas gregas de mármore (base em degraus, fuste
      // canelado, capitel com volutas) sustentando o frontão; um reflexo de luz desce devagar pelo mármore
      if (behind) {
        const OL = "rgba(70,60,120,0.5)", marble = (x0, x1) => hGrad(g, ["#C9C3DC", "#F7F5FC", "#E4E0F0", "#B9B2D0"], x0, x1);
        // frontão e arquitrave
        const ped = new Path2D(); ped.moveTo(-6, 21.6); ped.lineTo(50, 3); ped.lineTo(106, 21.6); ped.closePath();
        g.fillStyle = vGrad(g, ["#F7F5FC", "#D9D4E8"], 3, 21.6); g.fill(ped); stroke(g, ped, OL, small ? 0.8 : 0.45);
        if (!small) { const inn = new Path2D(); inn.moveTo(2, 20); inn.lineTo(50, 5.6); inn.lineTo(98, 20); inn.closePath(); stroke(g, inn, "rgba(70,60,120,0.3)", 0.35); }
        const arch = new Path2D(); arch.roundRect(-6, 21.4, 112, 4.6, 0.6); g.fillStyle = vGrad(g, ["#EEEBF6", "#C9C3DC"], 21.4, 26); g.fill(arch); stroke(g, arch, OL, small ? 0.8 : 0.4);
        for (const cx of [3, 97]) {
          const w = 9, top = 26, bot = 93;
          const b1 = new Path2D(); b1.roundRect(cx - w / 2 - 2, bot - 3, w + 4, 3, 0.6); g.fillStyle = marble(cx - w / 2 - 2, cx + w / 2 + 2); g.fill(b1); stroke(g, b1, OL, 0.4);
          const b2 = new Path2D(); b2.roundRect(cx - w / 2 - 1, bot - 5, w + 2, 2, 0.5); g.fillStyle = marble(cx - w / 2 - 1, cx + w / 2 + 1); g.fill(b2); stroke(g, b2, OL, 0.4);
          const sh = new Path2D(); sh.rect(cx - w / 2, top + 4, w, bot - 9 - top); g.fillStyle = marble(cx - w / 2, cx + w / 2); g.fill(sh);
          if (!small) {
            for (let k = 1; k <= 3; k++) line(g, "rgba(120,110,160,0.45)", cx - w / 2 + k * w / 4, top + 5, cx - w / 2 + k * w / 4, bot - 6, 0.45);
            const ph = (t * 0.18 + (cx > 50 ? 0.5 : 0)) % 1, sy = top + 4 + ph * (bot - top - 9);
            g.save(); g.clip(sh); g.fillStyle = vGrad(g, ["rgba(255,255,255,0)", "rgba(255,255,255,0.75)", "rgba(255,255,255,0)"], sy - 6, sy + 6); g.fillRect(cx - w / 2, sy - 6, w, 12); g.restore();
          }
          stroke(g, sh, OL, 0.4);
          const ab = new Path2D(); ab.roundRect(cx - w / 2 - 2.4, top, w + 4.8, 1.8, 0.4); g.fillStyle = marble(cx - w / 2 - 2.4, cx + w / 2 + 2.4); g.fill(ab); stroke(g, ab, OL, 0.4);
          const ec = new Path2D(); ec.roundRect(cx - w / 2 - 1, top + 1.8, w + 2, 2.2, 0.6); g.fillStyle = marble(cx - w / 2 - 1, cx + w / 2 + 1); g.fill(ec); stroke(g, ec, OL, 0.4);
          for (const s of [-1, 1]) {   // volutas
            const vx = cx + s * (w / 2 + 0.6), vy = top + 3.2; circle(g, "#F1EEF8", 1.5, vx, vy);
            g.beginPath(); g.arc(vx, vy, 1.5, 0, 2 * PI); g.strokeStyle = OL; g.lineWidth = 0.4; g.stroke();
            g.beginPath(); g.arc(vx, vy, 0.8, 0, 1.6 * PI); g.stroke(); circle(g, "rgba(70,60,120,0.6)", 0.3, vx, vy);
          }
        }
      } else if (!small) for (let i = 0; i < 4; i++) {   // brilhinhos nos capitéis
        const ph = (t * 0.5 + i / 4) % 1, a = sin(PI * ph); star4(g, i < 2 ? 3 + (i ? 4 : -4) : 97 + (i === 3 ? 4 : -4), 28 + (i % 2) * 2, 1.6 * a, `rgba(255,255,255,${a})`);
      }
    } else if (kind === "cifroes" && !behind) {
      // cifrões (fiscal): "R$" verdes de contorno escuro subindo dos lados (nunca na frente do rosto),
      // balançando, com realce claro e brilhinho dourado
      for (let i = 0; i < (small ? 3 : 7); i++) {
        const ph = (t * 0.24 + i / 7) % 1, sd = i % 2 ? 1 : -1, x = 50 + sd * (38 + ((i * 11) % 12)) + sin(t * 1.3 + i) * 2, y = 96 - ph * 92;
        if (y > 40 && y < 92 && x > 14 && x < 86) continue;
        const a = sin(PI * ph), sc = (1.3 + (i % 3) * 0.3) * boost * (small ? 1.3 : 1);
        g.save(); g.translate(x, y); g.rotate(sin(t * 1.5 + i) * 0.25); g.scale(sc, sc);
        const R = new Path2D(); R.moveTo(-2.7, 1.6); R.lineTo(-2.7, -1.6); R.lineTo(-1.5, -1.6); R.quadraticCurveTo(-0.3, -1.6, -0.3, -0.75); R.quadraticCurveTo(-0.3, 0.1, -1.5, 0.1); R.lineTo(-2.7, 0.1); R.moveTo(-1.6, 0.1); R.lineTo(-0.3, 1.6);
        const D = auraDollar();
        for (const [w, c] of [[1.4, `rgba(14,70,45,${0.55 * a})`], [0.75, `rgba(63,191,143,${a})`], [0.25, `rgba(210,255,235,${0.85 * a})`]]) { stroke(g, R, c, w); g.save(); g.translate(1.35, 0); stroke(g, D, c, w); g.restore(); }
        g.restore();
        if (!small && i % 2 === 0) star4(g, x + 3.6 * sc, y - 2.4 * sc, 1.2 * a * abs(sin(t * 3 + i)), rgba(GOLD, a));
      }
    } else if (kind === "chuva-moedas") {
      // chuva de moedas (bancos, LENDÁRIA): atrás, um clarão dourado que pulsa e moedas pequenas caindo ao
      // longe; na frente, moedas de ouro caindo dos lados girando (achatam e mostram a borda), com o cifrão
      // gravado, reflexo, um estalo de brilho quando batem nas pilhas do chão e purpurina dourada piscando
      if (behind) {
        const pulse = 0.75 + 0.25 * sin(t * 2.2);
        const gl = g.createRadialGradient(50, 52, 4, 50, 52, 62); gl.addColorStop(0, `rgba(255,224,122,${0.42 * pulse * boost})`); gl.addColorStop(0.6, `rgba(255,190,60,${0.16 * pulse})`); gl.addColorStop(1, "rgba(255,190,60,0)");
        g.fillStyle = gl; g.beginPath(); g.arc(50, 52, 62, 0, 2 * PI); g.fill();
        if (!small) for (let i = 0; i < 9; i++) { const ph = (t * 0.17 + i / 9) % 1; drawAuraCoin(g, 4 + ((i * 29) % 92), -10 + ph * 104, 1.5, t * 4 + i, 0.5 * sin(PI * ph)); }
      } else {
        for (let i = 0; i < (small ? 5 : 12); i++) {
          const ph = (t * 0.3 + i * 0.083) % 1, sd = i % 2 ? 1 : -1, x = 50 + sd * (36 + ((i * 13) % 18)) + sin(t + i) * 1.5, y = -8 + ph * 94;
          drawAuraCoin(g, x, y, (2.2 + (i % 3) * 0.45) * boost * (small ? 1.2 : 1), t * 5 + i * 1.7, ph > 0.93 ? (1 - ph) / 0.07 : Math.min(1, ph * 8));
          if (ph > 0.9 && !small) { const u = (ph - 0.9) / 0.1; for (let j = 0; j < 4; j++) { const an = -PI * 0.15 - j * PI * 0.23; circle(g, rgba(GOLD, 1 - u), 0.45, x + cos(an) * (1 + u * 4), 87 + sin(an) * (1 + u * 3)); } }
        }
        if (!small) for (let i = 0; i < 8; i++) { const ph = (t * 0.7 + i * 0.13) % 1, a = sin(PI * ph); star4(g, [6, 94, 16, 84, 2, 98, 24, 76][i], [30, 26, 58, 52, 80, 74, 12, 8][i], 1.6 * a, i % 2 ? `rgba(255,255,255,${a})` : rgba(GOLD, a)); }
      }
    } else if (kind === "votos" && !behind) {
      // votos voando (legislativo): cartõezinhos de "SIM" (verde) e "NÃO" (vermelho) subindo dos lados, girando
      // no ar (viram e mostram o verso com o visto ou o xis), com faixa colorida e a palavra a traço
      for (let i = 0; i < (small ? 4 : 8); i++) {
        const ph = (t * 0.2 + i / 8) % 1, sd = i % 2 ? 1 : -1, x = 50 + sd * (39 + ((i * 9) % 12)) + sin(t * 1.1 + i) * 3, y = 96 - ph * 94;
        if (y > 40 && y < 92 && x > 14 && x < 86) continue;
        const a = sin(PI * ph), yes = i % 3 !== 1, flip = cos(t * 2.2 + i * 1.4), sc = boost * (small ? 1.4 : 1.1), col = yes ? "#2E9E72" : "#E0475E";
        g.save(); g.translate(x, y); g.rotate(sin(t * 1.7 + i) * 0.35); g.scale(sc * Math.max(0.12, abs(flip)), sc); g.globalAlpha *= a;
        const card = new Path2D(); card.roundRect(-4, -2.6, 8, 5.2, 0.9);
        g.save(); g.translate(0.4, 0.5); g.fillStyle = "rgba(14,10,69,0.2)"; g.fill(card); g.restore();
        g.fillStyle = vGrad(g, ["#FFFFFF", "#ECEAF6"], -2.6, 2.6); g.fill(card);
        g.save(); g.clip(card); g.fillStyle = col; g.fillRect(-4, -2.6, 1.3, 5.2); g.restore();
        stroke(g, card, "rgba(38,33,92,0.5)", 0.3);
        if (flip > 0) auraWord(g, yes ? "SIM" : "NAO", 0.65, 0.15, 0.5, col, 0.5);
        else if (yes) { const p = new Path2D(); p.moveTo(-0.8, 0); p.lineTo(0.4, 1.2); p.lineTo(2.4, -1.4); stroke(g, p, col, 0.8); }
        else { line(g, col, -0.6, -1.3, 1.8, 1.3, 0.8); line(g, col, 1.8, -1.3, -0.6, 1.3, 0.8); }
        g.restore();
      }
    } else if (kind === "papeis") {
      // papéis voando (administrativo): folhas de ofício que atravessam a cena ao vento, tremulando (viram e
      // dobram), com pauta, margem, clipe ou carimbo; as da frente passam por cima, as outras por trás
      for (let i = 0; i < (small ? 4 : 8); i++) {
        const front = i % 2 === 0; if (front === behind) continue;
        const ph = (t * 0.09 + i / 8) % 1, x = -12 + ph * 124, y = (front ? 2 + ((i * 13) % 26) : 24 + ((i * 29) % 56)) + sin(t * 1.4 + i * 2) * 4 + sin(ph * PI * 2) * 5;
        const al = Math.min(1, ph * 8, (1 - ph) * 8), flut = cos(t * 3 + i * 1.9), k = (front ? 1 : 0.75) * boost * (small ? 1.3 : 1);
        g.save(); g.translate(x, y); g.rotate(sin(t * 1.2 + i) * 0.5 + ph * 1.2); g.scale(k * Math.max(0.15, abs(flut)), k * (0.85 + 0.15 * sin(t * 4 + i))); g.globalAlpha *= al;
        const sh = new Path2D(); sh.moveTo(-3, -4); sh.lineTo(1.8, -4); sh.lineTo(3, -2.8); sh.lineTo(3, 4); sh.lineTo(-3, 4); sh.closePath();
        g.save(); g.translate(0.4, 0.5); g.fillStyle = "rgba(14,10,69,0.15)"; g.fill(sh); g.restore();
        g.fillStyle = flut > 0 ? vGrad(g, ["#FFFFFF", "#EEECF7"], -4, 4) : "#E2DFF0"; g.fill(sh);
        if (flut > 0 && !small) {
          for (let j = 0; j < 5; j++) line(g, "rgba(111,143,224,0.55)", -2.2, -2.2 + j * 1.3, j ? 2.3 : 0.9, -2.2 + j * 1.3, 0.25);
          line(g, "rgba(242,163,163,0.85)", -1.9, -3.7, -1.9, 3.7, 0.25);
          if (i % 4 === 2) { g.beginPath(); g.arc(1.2, 2.2, 1.1, 0, 2 * PI); g.strokeStyle = "rgba(224,71,94,0.8)"; g.lineWidth = 0.3; g.stroke(); line(g, "rgba(224,71,94,0.8)", 0.5, 2.2, 1.9, 2.2, 0.3); }
        }
        stroke(g, sh, "rgba(38,33,92,0.45)", 0.3);
        const cn = new Path2D(); cn.moveTo(1.8, -4); cn.lineTo(1.8, -2.8); cn.lineTo(3, -2.8); cn.closePath(); g.fillStyle = "#D8D4E6"; g.fill(cn); stroke(g, cn, "rgba(38,33,92,0.35)", 0.2);
        if (i % 4 === 0) { const cl = new Path2D(); cl.moveTo(-1.6, -2.4); cl.lineTo(-1.6, -4.6); cl.quadraticCurveTo(-1.6, -5.2, -1, -5.2); cl.quadraticCurveTo(-0.4, -5.2, -0.4, -4.6); cl.lineTo(-0.4, -2.8); stroke(g, cl, "#8E92A6", 0.35); }
        g.restore();
      }
    } else if (kind === "bolhas") {
      // bolhas de sabão: sobem balançando com reflexo furta-cor na borda, janelinha de brilho, e estouram no
      // alto num anel de gotinhas; as da frente ficam dos lados, as de trás sobem por toda a cena
      for (let i = 0; i < (small ? 5 : 11); i++) {
        const front = i % 3 !== 0; if (front === behind) continue;
        const ph = (t * 0.15 + i * 0.091) % 1, sd = i % 2 ? 1 : -1;
        const x = (front ? 50 + sd * (37 + ((i * 7) % 15)) : 12 + ((i * 31) % 76)) + sin(t * 1.3 + i) * 3, y = 98 - ph * 100;
        if (front && y > 40 && y < 92 && x > 14 && x < 86) continue;
        const r = (2 + (i % 4) * 0.8) * boost * (front ? 1 : 0.75) * (small ? 1.2 : 1);
        if (ph > 0.9) { const u = (ph - 0.9) / 0.1; for (let j = 0; j < 6; j++) { const an = j * PI / 3; circle(g, `rgba(150,210,255,${1 - u})`, 0.35, x + cos(an) * r * (1 + u), y + sin(an) * r * (1 + u)); } continue; }
        const wob = 1 + 0.06 * sin(t * 5 + i);
        g.save(); g.translate(x, y); g.scale(wob, 2 - wob);
        const bg = g.createRadialGradient(-r * 0.3, -r * 0.3, 0, 0, 0, r); bg.addColorStop(0, "rgba(255,255,255,0.05)"); bg.addColorStop(0.7, "rgba(180,220,255,0.12)"); bg.addColorStop(0.9, "rgba(255,170,230,0.4)"); bg.addColorStop(1, "rgba(140,220,255,0.6)");
        g.fillStyle = bg; g.beginPath(); g.arc(0, 0, r, 0, 2 * PI); g.fill();
        g.strokeStyle = hGrad(g, ["rgba(255,140,200,0.85)", "rgba(120,200,255,0.85)", "rgba(150,240,190,0.85)"], -r, r); g.lineWidth = small ? 0.6 : 0.35; g.stroke();
        g.beginPath(); g.arc(0, 0, r * 0.68, PI * 1.1, PI * 1.45); g.strokeStyle = "rgba(255,255,255,0.9)"; g.lineWidth = 0.5; g.lineCap = "round"; g.stroke();
        circle(g, "rgba(255,255,255,0.85)", r * 0.12, r * 0.42, -r * 0.42);
        g.restore();
      }
    } else if (kind === "outono") {
      // folhas de outono: folhas de bordo (laranja, vermelha, amarela e marrom) descendo em zigue-zague,
      // girando e virando; as da frente caem dos lados, as de trás por toda a cena
      const C = [["#FF8A5C", "#E0472A"], ["#FFC266", "#F08A24"], ["#FFE27A", "#E0A82A"], ["#D9965A", "#9A5A2A"], ["#FF7A88", "#C8324A"]];
      for (let i = 0; i < (small ? 5 : 12); i++) {
        const front = i % 2 === 0; if (front === behind) continue;
        const ph = (t * 0.11 + i * 0.083) % 1, sd = i % 4 < 2 ? -1 : 1;
        const x = (front ? 50 + sd * (40 + ((i * 7) % 12)) : 10 + ((i * 37) % 80)) + sin(t * 1.3 + i * 1.7) * 6, y = -6 + ph * 100;
        if (front && y > 40 && y < 92 && x > 14 && x < 86) continue;
        drawAuraLeaf(g, x, y, (front ? 1.5 : 1.1) * boost * (small ? 1.3 : 1), sin(t * 1.1 + i) * 0.9 + i, cos(t * 2 + i * 1.3), C[i % 5], Math.min(1, ph * 10, (1 - ph) * 10));
      }
    } else if (kind === "neve") {
      // neve: flocos de seis pontas com raminhos e bolinhas macias, caindo devagar e balançando (os da frente
      // dos lados, os de trás por toda a cena); contorno azulado para aparecer no fundo claro
      for (let i = 0; i < (small ? 6 : 16); i++) {
        const front = i % 2 === 0; if (front === behind) continue;
        const ph = (t * 0.1 + i * 0.0625) % 1, sd = i % 4 < 2 ? -1 : 1;
        const x = (front ? 50 + sd * (39 + ((i * 5) % 13)) : 4 + ((i * 41) % 92)) + sin(t * 0.9 + i * 1.7) * 3, y = -6 + ph * 102;
        if (front && y > 40 && y < 92 && x > 14 && x < 86) continue;
        const al = Math.min(1, ph * 10, (1 - ph) * 10) * (front ? 1 : 0.8);
        if (i % 3 === 0) {
          const s = (front ? 1.3 : 0.9) * boost * (small ? 1.3 : 1);
          g.save(); g.translate(x, y); g.rotate(t * 0.5 + i); g.scale(s, s);
          for (const [w, c] of [[0.95, `rgba(111,143,224,${0.45 * al})`], [0.45, `rgba(255,255,255,${al})`]]) for (let k = 0; k < 6; k++) {
            const an = k * PI / 3, ca = cos(an), sa = sin(an), P = (px, py) => [px * ca - py * sa, px * sa + py * ca];
            line(g, c, 0, 0, ...P(0, -2.4), w); line(g, c, ...P(0, -1.4), ...P(0.7, -2), w); line(g, c, ...P(0, -1.4), ...P(-0.7, -2), w);
          }
          g.restore();
        } else {
          const r = (0.7 + (i % 3) * 0.35) * boost * (front ? 1 : 0.8) * (small ? 1.3 : 1);
          const sg = g.createRadialGradient(x, y, 0, x, y, r); sg.addColorStop(0, `rgba(255,255,255,${al})`); sg.addColorStop(0.7, `rgba(240,246,255,${0.9 * al})`); sg.addColorStop(1, `rgba(150,180,235,${0.6 * al})`);
          g.fillStyle = sg; g.beginPath(); g.arc(x, y, r, 0, 2 * PI); g.fill();
        }
      }
    } else if (kind === "arcoiris") {
      // arco-íris atrás do Folha: sete faixas em arco com um brilho que corre por elas; na frente, nuvenzinhas
      // fofas nas duas pontas e brilhinhos piscando em cima do arco
      const cx = 50, cy = 96, RX = 54, RY = 92, bw = 3.2, cols = ["#FF5C6E", "#FF9F3A", "#FFD84A", "#3FBF8F", "#6FB0FF", "#5A4FE6", "#B995F5"];
      if (behind) {
        g.save(); g.globalAlpha *= 0.9 * (0.9 + 0.1 * sin(t * 1.5));
        cols.forEach((c, k) => { const o = k * bw + bw / 2; g.beginPath(); g.ellipse(cx, cy, RX - o, RY - o, 0, PI, 2 * PI); g.strokeStyle = c; g.lineWidth = bw + 0.25; g.lineCap = "butt"; g.stroke(); });
        if (!small) {
          const a = PI + ((t * 0.22) % 1) * PI, o = 3.5 * bw;
          g.beginPath(); g.ellipse(cx, cy, RX - o, RY - o, 0, a - 0.1, a + 0.1); g.strokeStyle = "rgba(255,255,255,0.35)"; g.lineWidth = 7 * bw; g.stroke();
          g.beginPath(); g.ellipse(cx, cy, RX, RY, 0, PI, 2 * PI); g.strokeStyle = "rgba(255,255,255,0.6)"; g.lineWidth = 0.4; g.stroke();
        }
        g.restore();
      } else {
        const mid = RX - 3.5 * bw;
        for (const [sx, ph] of [[cx - mid, 0], [cx + mid, 1.6]]) {
          const by = 93 + sin(t * 1.4 + ph) * 0.8, k = small ? 1.2 : 1;
          const puffs = [[-4.4, 0.6, 2.8], [-1.4, -1.6, 3.6], [2.2, -0.8, 3.2], [4.8, 0.8, 2.4], [0, 1.2, 3.2]];
          for (const [dx, dy, r] of puffs) circle(g, "rgba(111,143,224,0.45)", (r + 0.4) * k, sx + dx * k, by + dy * k);
          for (const [dx, dy, r] of puffs) { const cg = g.createRadialGradient(sx + dx * k, by + (dy - r * 0.5) * k, 0.2, sx + dx * k, by + dy * k, r * k); cg.addColorStop(0, "#FFFFFF"); cg.addColorStop(1, "#E4ECFA"); g.fillStyle = cg; g.beginPath(); g.arc(sx + dx * k, by + dy * k, r * k, 0, 2 * PI); g.fill(); }
        }
        for (let i = 0; i < (small ? 2 : 5); i++) {
          const ph = (t * 0.5 + i / 5) % 1, a = sin(PI * ph), an = PI + (0.12 + i * 0.19) * PI;
          star4(g, cx + cos(an) * (RX + 3), cy + sin(an) * (RY + 3), 2 * a * boost, i % 2 ? `rgba(255,255,255,${a})` : rgba(GOLD, a));
        }
      }
    } else if (kind === "codigo") {
      // chuva de código (TI, LENDÁRIA): atrás dele, colunas de símbolos verdes caindo (a cabeça de cada coluna
      // brilha quase branca, o rastro vai apagando e os símbolos trocam sozinhos) sobre um brilho verde; na
      // frente, "</>" e "{ }" flutuando dos lados
      const G = [[[0, -1.2, 0, 1.2]], [[-0.7, -1.2, 0.7, -1.2, 0.7, 1.2, -0.7, 1.2, -0.7, -1.2]], [[0.7, -1.2, -0.7, 0, 0.7, 1.2]], [[-0.7, -1.2, 0.7, 0, -0.7, 1.2]], [[0.6, -1.2, -0.6, 1.2]],
        [[-0.7, -0.45, 0.7, -0.45], [-0.7, 0.45, 0.7, 0.45]], [[0.5, -1.2, 0, -1.2, 0, -0.2, -0.5, 0, 0, 0.2, 0, 1.2, 0.5, 1.2]], [[-0.7, -1.2, 0.7, -1.2, -0.2, 1.2]], [[-0.4, -1.2, 0, -1.2, 0, 1.2], [-0.6, 1.2, 0.6, 1.2]]];
      const drawG = (gl, x, y, s, c, w, mx = 1) => { for (const pl of gl) { g.beginPath(); for (let i = 0; i < pl.length; i += 2) { const px = x + pl[i] * s * mx, py = y + pl[i + 1] * s; i ? g.lineTo(px, py) : g.moveTo(px, py); } g.strokeStyle = c; g.lineWidth = w; g.lineCap = "round"; g.lineJoin = "round"; g.stroke(); } };
      if (behind) {
        // brilho verde forte em volta (pulsa), como o clarão da chuva de moedas
        const pulse = 0.8 + 0.2 * sin(t * 2.2);
        const gl = g.createRadialGradient(50, 52, 4, 50, 52, 66); gl.addColorStop(0, `rgba(60,230,140,${Math.min(1, 0.4 * pulse * boost)})`); gl.addColorStop(0.6, `rgba(30,200,110,${0.18 * pulse})`); gl.addColorStop(1, "rgba(30,200,110,0)");
        g.fillStyle = gl; g.beginPath(); g.arc(50, 52, 66, 0, 2 * PI); g.fill();
        // colunas: mais densas nas laterais e por cima do livro; cada símbolo com contorno escuro para aparecer
        const xs = small ? [-4, 4, 12, 50, 88, 96, 104] : [-8, -2, 4, 10, 16, 26, 38, 50, 62, 74, 84, 90, 96, 102, 108];
        const s = small ? 1.9 : 1.5, len = small ? 7 : 12;
        xs.forEach((x, c) => {
          const sp = 0.24 + ((c * 7) % 5) * 0.05, head = ((t * sp + c * 0.37) % 1) * 160 - 28;
          for (let j = 0; j < len; j++) {
            const y = head - j * 4.6; if (y < -8 || y > 98) continue;
            const a = j === 0 ? 1 : Math.max(0.12, 1 - j / len), gi = (c * 7 + j * 3 + Math.floor(t * (j === 0 ? 8 : 2) + c)) % G.length;
            if (j === 0) { const hg = g.createRadialGradient(x, y, 0.2, x, y, 4.4); hg.addColorStop(0, "rgba(170,255,205,0.85)"); hg.addColorStop(1, "rgba(140,255,190,0)"); g.fillStyle = hg; g.beginPath(); g.arc(x, y, 4.4, 0, 2 * PI); g.fill(); }
            drawG(G[gi], x, y, s, `rgba(5,45,25,${0.55 * a})`, small ? 1.6 : 1.2);
            drawG(G[gi], x, y, s, j === 0 ? "#F2FFF7" : j < 3 ? `rgba(120,255,175,${a})` : `rgba(40,225,120,${a})`, small ? 0.9 : 0.65);
          }
        });
      } else if (!small) for (let i = 0; i < 4; i++) {
        const ph = (t * 0.2 + i / 4) % 1, sd = i % 2 ? 1 : -1, x = 50 + sd * (42 + ((i * 5) % 7)), y = 92 - ph * 80, a = sin(PI * ph), k = 1.1 * boost;
        const parts = i % 2 === 0 ? [[G[2], -2.3, 1], [G[4], 0, 1], [G[3], 2.3, 1]] : [[G[6], -1.6, 1], [G[6], 1.6, -1]];
        for (const [w, c] of [[1.2, `rgba(10,60,35,${0.5 * a})`], [0.55, `rgba(90,235,155,${a})`]]) for (const [gl, dx, mx] of parts) drawG(gl, x + dx * k, y, k, c, w, mx);
      }
    }
  }
  // Efeitos no chão (fora do corpo: não pulam com ele). Holofotes: dois canhões de luz nos cantos de cima
  // varrendo o palco, com a poça de luz no chão; runas: um círculo mágico girando debaixo dos pés.
  function drawFloorAura(g, t, kind, small) {
    if (kind === "holofote") {
      for (const side of [-1, 1]) {
        const sx = 50 + side * 56, sy = -6, tx = 50 + sin(t * 0.8 + (side > 0 ? 2.2 : 0)) * 24, ty = 95;
        const ang = Math.atan2(ty - sy, tx - sx), len = Math.hypot(tx - sx, ty - sy), w = len * 0.17;
        g.save(); g.translate(sx, sy); g.rotate(ang);
        const cone = new Path2D(); cone.moveTo(0, -2); cone.lineTo(len, -w); cone.quadraticCurveTo(len + 3, 0, len, w); cone.lineTo(0, 2); cone.closePath();
        const gr = g.createLinearGradient(0, 0, len, 0); gr.addColorStop(0, "rgba(255,226,140,0.55)"); gr.addColorStop(0.7, "rgba(255,226,140,0.2)"); gr.addColorStop(1, "rgba(255,226,140,0.06)");
        g.fillStyle = gr; g.fill(cone);
        const core = new Path2D(); core.moveTo(0, -0.8); core.lineTo(len, -w * 0.35); core.lineTo(len, w * 0.35); core.lineTo(0, 0.8); core.closePath();
        g.fillStyle = "rgba(255,248,220,0.18)"; g.fill(core);
        g.restore();
        g.save(); g.translate(tx, ty); g.scale(1, 0.26);
        const pool = g.createRadialGradient(0, 0, 0, 0, 0, 20); pool.addColorStop(0, "rgba(255,236,170,0.65)"); pool.addColorStop(1, "rgba(255,236,170,0)");
        g.fillStyle = pool; g.beginPath(); g.arc(0, 0, 20, 0, 2 * PI); g.fill(); g.restore();
        // poeira brilhando dentro do facho
        if (!small) for (let i = 0; i < 5; i++) {
          const u = ((t * 0.07 + i * 0.21) % 1), px = lerp(sx, tx, 0.25 + u * 0.7) + sin(t + i * 3) * 2, py = lerp(sy, ty, 0.25 + u * 0.7) + cos(t * 0.7 + i) * 2;
          circle(g, `rgba(255,246,214,${0.7 * sin(PI * u)})`, 0.55, px, py);
        }
      }
    } else if (kind === "runas") {
      // o círculo mágico em perspectiva: anéis, hexagrama e runas girando, pulsando de leve
      const pulse = 0.75 + 0.25 * sin(t * 2.4), rot = t * 0.35, K = 0.22, cx = 50, cy = 94;
      const at = (r, a) => [cx + cos(a) * r, cy + sin(a) * r * K];
      g.save(); g.translate(cx, cy); g.scale(1, K);
      const gl = g.createRadialGradient(0, 0, 8, 0, 0, 56); gl.addColorStop(0, "rgba(126,224,184,0)"); gl.addColorStop(0.72, `rgba(126,224,184,${0.28 * pulse})`); gl.addColorStop(1, "rgba(149,99,255,0)");
      g.fillStyle = gl; g.beginPath(); g.arc(0, 0, 56, 0, 2 * PI); g.fill(); g.restore();
      const ring = (r, c, w) => { g.beginPath(); g.ellipse(cx, cy, r, r * K, 0, 0, 2 * PI); g.strokeStyle = c; g.lineWidth = w; g.stroke(); };
      ring(47, `rgba(126,224,184,${0.9 * pulse})`, small ? 1.4 : 0.9); ring(42, `rgba(185,149,245,${0.8 * pulse})`, small ? 1 : 0.55); ring(29, `rgba(126,224,184,${0.6 * pulse})`, 0.5);
      for (let k = 0; k < 2; k++) {
        const p = new Path2D();
        for (let i = 0; i < 3; i++) { const [x, y] = at(42, rot + k * PI / 3 + i * 2 * PI / 3); i ? p.lineTo(x, y) : p.moveTo(x, y); }
        p.closePath(); stroke(g, p, `rgba(185,149,245,${0.7 * pulse})`, 0.5);
      }
      if (!small) {
        const R = [[0, -2, 0, 2, -1.4, -0.6, 1.4, 0.6], [-1.4, -2, 1.4, -2, 0, -2, 0, 2], [-1.2, 2, 0, -2, 0, -2, 1.2, 2], [-1.4, -1, 1.4, -1, -1.4, 1, 1.4, 1]];
        for (let i = 0; i < 16; i++) {
          const [x, y] = at(44.5, -rot * 0.7 + i * PI / 8), r = R[i % 4];
          g.save(); g.translate(x, y); g.scale(0.7, 0.32); g.strokeStyle = `rgba(200,255,232,${0.85 * pulse})`; g.lineWidth = 0.9; g.lineCap = "round";
          g.beginPath(); g.moveTo(r[0], r[1]); g.lineTo(r[2], r[3]); g.moveTo(r[4], r[5]); g.lineTo(r[6], r[7]); g.stroke(); g.restore();
        }
      }
    } else if (kind === "colunas") {
      // o piso do fórum: dois degraus de mármore em perspectiva debaixo dos pés
      for (const [rx, ry, y, c0, c1] of [[56, 5, 96.4, "#D9D4E8", "#B9B2D0"], [50, 3.8, 94.6, "#F7F5FC", "#DCD7EA"]]) {
        g.beginPath(); g.ellipse(50, y, rx, ry, 0, 0, 2 * PI); g.fillStyle = vGrad(g, [c0, c1], y - ry, y + ry); g.fill();
        g.strokeStyle = "rgba(70,60,120,0.35)"; g.lineWidth = small ? 0.8 : 0.4; g.stroke();
      }
      if (!small) { g.save(); g.beginPath(); g.ellipse(50, 94.6, 50, 3.8, 0, 0, 2 * PI); g.clip(); const sx = -10 + ((t * 14) % 120); g.fillStyle = hGrad(g, ["rgba(255,255,255,0)", "rgba(255,255,255,0.7)", "rgba(255,255,255,0)"], sx - 8, sx + 8); g.fillRect(sx - 8, 90, 16, 9); g.restore(); }
    } else if (kind === "chuva-moedas") {
      // pilhas de moedas no chão dos dois lados (com moedas soltas e brilho correndo pelas bordas)
      const stack = (cx, n) => { for (let k = 0; k < n; k++) { const y = 95 - k * 1.45, dx = sin(k * 2.3 + cx) * 0.4; g.fillStyle = "#A86A08"; g.fillRect(cx + dx - 3.4, y, 6.8, 1.2); g.beginPath(); g.ellipse(cx + dx, y + 1.2, 3.4, 0.9, 0, 0, PI); g.fill(); g.beginPath(); g.ellipse(cx + dx, y, 3.4, 0.9, 0, 0, 2 * PI); g.fillStyle = hGrad(g, ["#C98A12", "#FFE59A", "#F5B83D"], cx - 3.4, cx + 3.4); g.fill(); g.strokeStyle = "rgba(150,90,5,0.7)"; g.lineWidth = 0.25; g.stroke(); } };
      for (const [cx, n] of [[5, 5], [12, 3], [88, 3], [95, 6]]) stack(cx, small ? Math.min(n, 3) : n);
      for (const [cx, cy] of [[19, 96.4], [81, 96.6]]) { g.beginPath(); g.ellipse(cx, cy, 2.6, 0.75, 0, 0, 2 * PI); g.fillStyle = hGrad(g, ["#C98A12", "#FFE59A", "#F5B83D"], cx - 2.6, cx + 2.6); g.fill(); g.strokeStyle = "rgba(150,90,5,0.7)"; g.lineWidth = 0.25; g.stroke(); }
      if (!small) for (let i = 0; i < 4; i++) { const ph = (t * 0.6 + i / 4) % 1, a = sin(PI * ph); star4(g, [5, 12, 88, 95][i] + 2, [88.6, 91.6, 91.6, 87.2][i], 1.5 * a, `rgba(255,255,255,${a})`); }
    } else if (kind === "outono") {
      // folhas caídas no chão, deitadas (achatadas), dos dois lados
      for (const [x, r, ci] of [[4, 0.4, 0], [11, 2.1, 1], [19, -0.8, 3], [80, 1.4, 2], [88, -1.9, 4], [96, 0.6, 1]]) {
        const C = [["#FF8A5C", "#E0472A"], ["#FFC266", "#F08A24"], ["#FFE27A", "#E0A82A"], ["#D9965A", "#9A5A2A"], ["#FF7A88", "#C8324A"]][ci];
        g.save(); g.translate(0, 95.6); g.scale(1, 0.45); drawAuraLeaf(g, x, 0, small ? 1.8 : 1.4, r, 1, C, 1); g.restore();
      }
    } else if (kind === "neve") {
      // camada de neve no chão com dois montinhos dos lados e brilhinhos de gelo
      g.beginPath(); g.ellipse(50, 95.6, 56, 3.2, 0, 0, 2 * PI); g.fillStyle = vGrad(g, ["#FFFFFF", "#DCE6F8"], 92.4, 98.8); g.fill(); g.strokeStyle = "rgba(111,143,224,0.4)"; g.lineWidth = small ? 0.8 : 0.4; g.stroke();
      for (const [cx, rx] of [[8, 11], [92, 10]]) { const p = new Path2D(); p.moveTo(cx - rx, 96); p.bezierCurveTo(cx - rx * 0.6, 90.6, cx + rx * 0.5, 90, cx + rx, 96); p.closePath(); g.fillStyle = vGrad(g, ["#FFFFFF", "#E2EAF8"], 90.6, 96); g.fill(p); stroke(g, p, "rgba(111,143,224,0.45)", small ? 0.8 : 0.4); }
      if (!small) for (let i = 0; i < 5; i++) { const ph = (t * 0.5 + i / 5) % 1, a = sin(PI * ph); star4(g, [5, 12, 30, 70, 90][i], [93, 94, 96.4, 96.6, 93.4][i], 1.3 * a, `rgba(170,210,255,${a})`); }
    }
  }
  // Lâmpadas dos holofotes, na frente de tudo nos cantos de cima.
  function drawSpotLamps(g, t) {
    for (const side of [-1, 1]) {
      const sx = 50 + side * 56, sy = -6, tx = 50 + sin(t * 0.8 + (side > 0 ? 2.2 : 0)) * 24;
      g.save(); g.translate(sx, sy); g.rotate(Math.atan2(95 - sy, tx - sx));
      g.fillStyle = vGrad(g, ["#5A5F75", "#2E3142", "#1B1D29"], -3.4, 3.4);
      const body = new Path2D(); body.moveTo(-6, -2.6); body.lineTo(1, -3.4); body.lineTo(1, 3.4); body.lineTo(-6, 2.6); body.closePath(); g.fill(body);
      g.fillStyle = "#3C4054"; g.fillRect(1, -3.8, 1.4, 7.6);
      const lens = g.createRadialGradient(2.6, 0, 0.2, 2.6, 0, 4); lens.addColorStop(0, "#FFFFFF"); lens.addColorStop(0.5, "rgba(255,236,170,0.9)"); lens.addColorStop(1, "rgba(255,226,140,0)");
      g.fillStyle = lens; g.beginPath(); g.ellipse(2.6, 0, 1.6, 3.6, 0, 0, 2 * PI); g.fill();
      line(g, "rgba(255,255,255,0.35)", -5, -2, 0.5, -2.8, 0.4);
      g.restore();
    }
  }
  // Costas: atrás do livro, presas nos ombros. Mochila de estudos com chaveiro que balança, mochila-foguete
  // com chama que acende quando ele pula, manto real esvoaçante com gola de arminho e barra dourada.
  function drawBack(g, t, kind, mood, small, hop) {
    if (!kind || kind === "nada") return;
    const party = mood === "happy" || mood === "ecstatic", air = clamp((-hop - 1.5) / 4, 0, 1);
    if (kind === "mochila") {
      // mochila de estudos pendurada no ombro esquerdo: lona índigo com bolso da frente, zíper, alça de couro
      // passando por trás, lápis e régua saindo por cima e um chaveirinho de estrela que balança (mais na festa)
      const sw = sin(t * (party ? 7 : 1.6)) * (party ? 0.28 : 0.1) + air * 0.2;
      g.save(); g.translate(2, 60); g.rotate(-0.08 + sin(t * 1.2) * 0.02 - air * 0.06); g.translate(-2, -60);
      // o que sai por cima: régua e lápis
      g.save(); g.translate(-1, 44); g.rotate(-0.22);
      const rg = new Path2D(); rg.roundRect(-1.6, -13, 3.2, 15, 0.4); g.fillStyle = vGrad(g, ["#FFE58A", "#F5C842"], -13, 2); g.fill(rg); stroke(g, rg, "rgba(120,80,10,0.45)", 0.3);
      if (!small) for (let i = 0; i < 6; i++) line(g, "rgba(120,80,10,0.6)", -1.6, -11 + i * 2, -0.4 - (i % 2) * 0.6, -11 + i * 2, 0.25);
      g.restore();
      drawPencilAt(g, 8.5, 46, 12.5, 32, 1.25);
      // corpo
      const body = new Path2D(); body.moveTo(-9, 50); body.quadraticCurveTo(-9.6, 43, -2, 42.4); body.lineTo(10, 42.4); body.quadraticCurveTo(15.6, 43, 15.4, 50); body.lineTo(15, 80); body.quadraticCurveTo(15, 84, 10.6, 84); body.lineTo(-4.6, 84); body.quadraticCurveTo(-9, 84, -9, 80); body.closePath();
      g.fillStyle = hGrad(g, ["#1D1670", "#3A2FC4", "#2A209E", "#16105A"], -9, 15.4); g.fill(body);
      if (!small) { g.save(); g.clip(body); for (let i = 0; i < 14; i++) line(g, "rgba(255,255,255,0.05)", -9 + i * 2, 42, -9 + i * 2 - 6, 84, 0.5); g.restore(); }
      stroke(g, body, "rgba(10,8,40,0.6)", small ? 1 : 0.5);
      // tampa com costura e fivela
      const flap = new Path2D(); flap.moveTo(-8.4, 50); flap.quadraticCurveTo(-8.6, 44, -2, 43.6); flap.lineTo(10, 43.6); flap.quadraticCurveTo(14.8, 44, 14.8, 50); flap.quadraticCurveTo(3, 56, -8.4, 50); flap.closePath();
      g.fillStyle = vGrad(g, ["#5247E0", "#3A2FC4"], 43.6, 54); g.fill(flap);
      if (!small) { const st = new Path2D(); st.moveTo(-7, 49.4); st.quadraticCurveTo(3, 54.2, 13.4, 49.4); g.save(); g.setLineDash([0.9, 0.7]); stroke(g, st, "rgba(255,255,255,0.45)", 0.3); g.restore(); }
      const bk = new Path2D(); bk.roundRect(1.4, 51.2, 3.6, 3.2, 0.6); g.fillStyle = hGrad(g, GOLD3, 1.4, 5); g.fill(bk); stroke(g, bk, "rgba(90,60,5,0.6)", 0.3);
      // bolso da frente com zíper
      const pk = new Path2D(); pk.roundRect(-6.4, 63, 18.8, 16, 3); g.fillStyle = vGrad(g, ["#3A2FC4", "#2A209E"], 63, 79); g.fill(pk); stroke(g, pk, "rgba(10,8,40,0.5)", 0.35);
      line(g, "#C9CCE0", -4.6, 66.2, 10.6, 66.2, 0.55);
      if (!small) { for (let i = 0; i < 15; i++) line(g, "rgba(30,25,90,0.7)", -4.4 + i, 65.8, -4.4 + i, 66.6, 0.2); const pu = new Path2D(); pu.roundRect(8.6, 66, 1.4, 3, 0.5); g.fillStyle = hGrad(g, GOLD3, 8.6, 10); g.fill(pu); }
      // etiqueta menta com o "E" do Estudário
      const tg = new Path2D(); tg.roundRect(-0.6, 70.6, 6.4, 4.4, 1); g.fillStyle = MINT; g.fill(tg);
      if (!small) { g.fillStyle = "#1D1670"; g.font = "900 3.4px Nunito, system-ui, sans-serif"; g.textAlign = "center"; g.textBaseline = "middle"; g.fillText("E", 2.6, 73); }
      // chaveiro de estrela no zíper
      if (!small) {
        g.save(); g.translate(9.3, 69); g.rotate(sw);
        line(g, "#B9BCD0", 0, 0, 0, 4, 0.35); g.beginPath(); g.arc(0, 4.6, 0.8, 0, 2 * PI); g.strokeStyle = "#D7D9E8"; g.lineWidth = 0.35; g.stroke();
        g.fillStyle = hGrad(g, GOLD3, -2.4, 2.4); g.fill(star5(g, 0, 7.8, 2.4)); stroke(g, star5(g, 0, 7.8, 2.4), "rgba(120,80,10,0.55)", 0.25);
        g.restore();
        line(g, "rgba(255,255,255,0.35)", -7.6, 52, -7.8, 80, 0.6);
      }
      g.restore();
      // a alça passando por cima do ombro, por trás do livro
      const sp = new Path2D(); sp.moveTo(-2, 43); sp.bezierCurveTo(2, 36, 10, 35, 16, 40);
      stroke(g, sp, "#16105A", small ? 3.4 : 2.8); stroke(g, sp, "#2A209E", small ? 2.2 : 1.8);
    } else if (kind === "foguete") {
      const thrust = party ? 1 : air;
      for (const side of [-1, 1]) {
        g.save();
        if (side > 0) { g.translate(100, 0); g.scale(-1, 1); }
        // chama: azul piloto parado, laranja forte no pulo e na festa
        const fl = 5 + thrust * 15 + sin(t * 31 + side) * (0.6 + thrust * 1.4), fx = 3.6, fy = 84.5;
        const flame = (w, len, stops) => { const p = new Path2D(); p.moveTo(fx - w, fy); p.bezierCurveTo(fx - w * 1.1, fy + len * 0.45, fx - w * 0.25, fy + len * 0.8, fx, fy + len); p.bezierCurveTo(fx + w * 0.25, fy + len * 0.8, fx + w * 1.1, fy + len * 0.45, fx + w, fy); p.closePath(); g.fillStyle = vGrad(g, stops, fy, fy + len); g.fill(p); };
        if (thrust > 0.05) { flame(3.4, fl, ["#FFE07A", "#FF9F3A", "rgba(242,85,44,0)"]); flame(1.8, fl * 0.6, ["#FFFFFF", "#FFF1BF", "rgba(255,224,122,0)"]); }
        else { flame(2.2, fl, ["#E6F4FF", "#7FC4FF", "rgba(80,140,255,0)"]); flame(1, fl * 0.5, ["#FFFFFF", "rgba(200,232,255,0)"]); }
        if (thrust > 0.3 && !small) for (let i = 0; i < 4; i++) { const ph = (t * 1.6 + i / 4) % 1; circle(g, `rgba(220,220,235,${0.45 * (1 - ph) * thrust})`, 1.4 + ph * 3.4, fx - 2 - ph * 6 + sin(i * 3) * 2, fy + fl * 0.7 + ph * 8); }
        // bocal
        const nz = new Path2D(); nz.moveTo(0.4, 79.5); nz.lineTo(6.8, 79.5); nz.lineTo(8, 85); nz.lineTo(-0.8, 85); nz.closePath();
        g.fillStyle = hGrad(g, ["#2C2F40", "#6B7088", "#2C2F40"], -0.8, 8); g.fill(nz);
        // tanque: cápsula metálica com faixas vermelhas, aleta, rebites e luzinha
        const tank = new Path2D(); tank.moveTo(-3.4, 53); tank.bezierCurveTo(-3.4, 45, 10.6, 45, 10.6, 53); tank.lineTo(10.6, 77); tank.quadraticCurveTo(10.6, 80.5, 3.6, 80.5); tank.quadraticCurveTo(-3.4, 80.5, -3.4, 77); tank.closePath();
        const fin = new Path2D(); fin.moveTo(-3, 64); fin.lineTo(-8, 77); fin.lineTo(-8, 80); fin.lineTo(-3, 76); fin.closePath();
        g.fillStyle = vGrad(g, ["#F05A6E", "#C42F4B"], 64, 80); g.fill(fin); stroke(g, fin, "rgba(60,10,25,0.5)", 0.4);
        g.fillStyle = hGrad(g, ["#7C8398", "#E8ECF4", "#C3C9D6", "#6E7588"], -3.4, 10.6); g.fill(tank);
        g.save(); g.clip(tank);
        g.fillStyle = hGrad(g, ["#B3263F", "#F05A6E", "#C42F4B", "#7A1428"], -3.4, 10.6); g.fillRect(-4, 44, 16, 6.5); g.fillRect(-4, 60, 16, 3.2);
        g.restore();
        stroke(g, tank, "rgba(30,32,48,0.55)", small ? 1 : 0.5);
        if (!small) {
          for (const y of [55, 70]) for (const x of [-1.2, 8.4]) circle(g, "rgba(60,64,84,0.6)", 0.35, x, y);
          line(g, "rgba(255,255,255,0.7)", 0.6, 52, 0.6, 77, 0.7);
          const blink = (t * 1.3 + (side > 0 ? 0.5 : 0)) % 1 < 0.5;
          circle(g, blink ? "#7EE0B8" : "#2E6B57", 0.8, 3.6, 66.5);
        }
        g.restore();
      }
    } else if (kind === "paraquedas") {
      // mochila de paraquedista: as duas bolsas verde-oliva nas laterais e o velame aberto lá em cima, em
      // gomos verdes e amarelos, balançando; no pulo e na festa ele infla e sobe um pouco
      const lift = (party ? 3 : 0) + air * 4, sway = sin(t * 1.1) * 0.045, cy = 6 - lift;
      g.save(); g.translate(50, 60); g.rotate(sway); g.translate(-50, -60);
      // linhas do velame até as bolsas
      if (!small) for (const side of [-1, 1]) for (const k of [0, 1, 2, 3]) {
        const rx = 50 + side * (46 - k * 10), ry = cy + 4 + k * 0.6;
        line(g, "rgba(60,64,40,0.55)", rx, ry, 50 + side * 46, 50, 0.3);
      }
      const W = 48 + (party ? 2 : 0), H = 17 + lift * 0.6, N = 8;
      const dome = new Path2D(); dome.moveTo(50 - W, cy + 4); dome.bezierCurveTo(50 - W, cy - H * 1.25, 50 + W, cy - H * 1.25, 50 + W, cy + 4);
      for (let i = N; i > 0; i--) { const x0 = 50 - W + (i / N) * 2 * W, x1 = 50 - W + ((i - 1) / N) * 2 * W; dome.quadraticCurveTo((x0 + x1) / 2, cy + 1.2, x1, cy + 4); }
      dome.closePath();
      g.save(); g.clip(dome);
      for (let i = 0; i < N; i++) {
        const x0 = 50 - W + (i / N) * 2 * W, x1 = x0 + 2 * W / N;
        // gomos em leque a partir do topo (cobrem o velame inteiro, até as abas)
        const a0 = PI - (i / N) * PI, a1 = PI - ((i + 1) / N) * PI, ax = 50, ay = cy - H * 0.94;
        const gore = new Path2D(); gore.moveTo(ax, ay); gore.lineTo(ax + cos(a0) * 140, ay + sin(a0) * 60 + 2); gore.lineTo(ax + cos(a1) * 140, ay + sin(a1) * 60 + 2); gore.closePath();
        g.fillStyle = i % 2 ? "#FFD23F" : "#1E9A48"; g.fill(gore);
      }
      const sh = g.createLinearGradient(50 - W, 0, 50 + W, 0); sh.addColorStop(0, "rgba(0,0,0,0.22)"); sh.addColorStop(0.4, "rgba(255,255,255,0.18)"); sh.addColorStop(1, "rgba(0,0,0,0.28)"); g.fillStyle = sh; g.fill(dome);
      g.restore();
      stroke(g, dome, "rgba(20,60,30,0.45)", small ? 0.9 : 0.4);
      circle(g, "#1F3F9A", 2.4, 50, cy - H * 0.94); g.fillStyle = "#FFFFFF"; g.fill(star5(g, 50, cy - H * 0.94, 1.3));
      g.restore();
      // as bolsas nas laterais: lona com bolso, alças e fivela
      for (const side of [-1, 1]) {
        g.save(); if (side > 0) { g.translate(100, 0); g.scale(-1, 1); }
        const bag = new Path2D(); bag.roundRect(-5.6, 49, 15, 30, 4); g.fillStyle = hGrad(g, ["#46512A", "#7E8B4E", "#5C6934"], -5.6, 9.4); g.fill(bag); stroke(g, bag, "rgba(30,36,14,0.6)", small ? 1 : 0.45);
        const pk = new Path2D(); pk.roundRect(-4.4, 63, 8.4, 11, 2); g.fillStyle = "#66733D"; g.fill(pk); stroke(g, pk, "rgba(30,36,14,0.5)", 0.35);
        g.fillStyle = "#3E4A20"; g.fillRect(-4.4, 63, 8.4, 2.6);
        const buckle = new Path2D(); buckle.roundRect(-1.4, 64.6, 2.8, 2, 0.4); stroke(g, buckle, "#C9D0DC", 0.45);
        if (!small) { line(g, "rgba(255,255,255,0.2)", -3.8, 52, -3.8, 76, 0.6); g.save(); g.setLineDash([0.8, 0.6]); const st = new Path2D(); st.roundRect(-4.6, 50, 13, 28, 3.2); stroke(g, st, "rgba(214,204,150,0.6)", 0.25); g.restore(); }
        g.restore();
      }
    } else if (kind === "toga") {
      // toga de magistrado: cetim preto com pregas que balançam, brilho que corre, a murça de veludo
      // sobre os ombros com filete dourado e o cordão com borla pendurado
      const wave = sin(t * 1.5), fl = air * 5 + (party ? 2.5 : 0);
      const L = -4 - fl + wave, Rr = 104 + fl - wave;
      const robe = new Path2D(); robe.moveTo(15, 44); robe.bezierCurveTo(6, 60, L + 2, 82, L, 101);
      for (let i = 1; i <= 8; i++) { const x0 = lerp(L, Rr, (i - 0.5) / 8), x1 = lerp(L, Rr, i / 8); robe.quadraticCurveTo(x0, 103.4 + sin(t * 2 + i) * 0.7, x1, 101 + sin(t * 1.8 + i * 1.4) * 0.3); }
      robe.bezierCurveTo(Rr - 2, 82, 94, 60, 85, 44); robe.closePath();
      g.fillStyle = hGrad(g, ["#050508", "#1E1E2A", "#2E2E3E", "#1E1E2A", "#050508"], L, Rr); g.fill(robe);
      if (!small) {
        g.save(); g.clip(robe);
        for (let i = 0; i < 9; i++) { const x = lerp(L + 5, Rr - 5, i / 8) + sin(t * 1.5 + i) * 1; const fold = g.createLinearGradient(x - 2.6, 0, x + 2.6, 0); fold.addColorStop(0, "rgba(255,255,255,0)"); fold.addColorStop(0.5, "rgba(255,255,255,0.09)"); fold.addColorStop(1, "rgba(255,255,255,0)"); g.fillStyle = fold; g.fillRect(x - 2.6, 44, 5.2, 62); }
        const sx = -40 + ((t * 0.3) % 1) * 190, sh = g.createLinearGradient(sx, 40, sx + 16, 104); sh.addColorStop(0, "rgba(255,255,255,0)"); sh.addColorStop(0.5, "rgba(190,200,255,0.18)"); sh.addColorStop(1, "rgba(255,255,255,0)"); g.fillStyle = sh; g.fillRect(-20, 40, 140, 70);
        g.restore();
      }
      stroke(g, robe, "rgba(0,0,0,0.6)", small ? 1 : 0.5);
      // murça: meia-capa de veludo nos ombros (por cima dos cantos do livro), com filete dourado
      for (const side of [-1, 1]) {
        g.save(); g.translate(50 + side * 37, 44.6); g.rotate(side * -0.14);
        const m = new Path2D(); m.moveTo(-11, -2.6); m.quadraticCurveTo(0, -6, 11, -2.6); m.quadraticCurveTo(12.4, 3, 7, 6.4); m.quadraticCurveTo(0, 8.4, -7, 6.4); m.quadraticCurveTo(-12.4, 3, -11, -2.6); m.closePath();
        g.fillStyle = vGrad(g, ["#3A3A4C", "#14141C"], -6, 8); g.fill(m);
        stroke(g, m, "#C98A12", small ? 0.9 : 0.55);
        if (!small) { const hl = new Path2D(); hl.moveTo(-8, -1.8); hl.quadraticCurveTo(-1, -4.4, 6, -2.4); stroke(g, hl, "rgba(255,255,255,0.22)", 0.6); }
        g.restore();
      }
      // cordão dourado com borla, do lado direito
      if (!small) {
        const sw = sin(t * 2) * 1.2, cord = new Path2D(); cord.moveTo(84, 48.6); cord.quadraticCurveTo(95, 52, 95.6 + sw * 0.5, 62); stroke(g, cord, "#9C6A12", 0.9); stroke(g, cord, "#F5C451", 0.45);
        circle(g, "#E9AE34", 1.1, 95.6 + sw * 0.5, 62.4);
        const tas = new Path2D(); tas.moveTo(94.6 + sw * 0.5, 63); tas.lineTo(96.6 + sw * 0.5, 63); tas.lineTo(97.6 + sw, 68.6); tas.lineTo(93.6 + sw, 68.6); tas.closePath(); g.fillStyle = vGrad(g, ["#FFE08A", "#C98A12"], 63, 68.6); g.fill(tas);
        for (let i = 0; i < 4; i++) line(g, "rgba(150,95,10,0.6)", 94.2 + sw + i, 64.4, 94.2 + sw + i, 68.4, 0.2);
      }
    } else if (kind === "asas-fenix") {
      // asas de fênix (Pro, iguais ao app): asas erguidas em V, feitas de penas de fogo. Cada pena nasce vermelha
      // no ombro, passa pelo laranja e acaba numa ponta amarela que tremula como chama; um miolo claro brilha
      // por dentro, chamas pequenas se soltam das pontas e brasas sobem devagar.
      const busy = party || air > 0.1, flap = (sin(t * (busy ? 7 : 1.8)) * (busy ? 10 : 3) - air * 6) * PI / 180;
      if (!small) { const gl = g.createRadialGradient(50, 50, 0, 50, 50, 62); gl.addColorStop(0, "rgba(255,122,26,0.35)"); gl.addColorStop(0.5, "rgba(255,77,26,0.15)"); gl.addColorStop(1, "rgba(255,77,26,0)"); g.fillStyle = gl; g.beginPath(); g.arc(50, 50, 62, 0, 2 * PI); g.fill(); }
      const fireFeather = (len0, w, bend0, ph, core) => {
        const len = len0 * (1 + 0.04 * sin(t * 6 + ph)), bend = bend0 + sin(t * 9 + ph) * 1.1;
        const p = new Path2D(); p.moveTo(0, -w * 0.3);
        p.bezierCurveTo(-len * 0.3, -w * 1.05, -len * 0.62, -w * 0.5 + bend * 0.5, -len * 0.8, -w * 0.32 + bend * 0.8);
        p.quadraticCurveTo(-len * 0.9, -w * 0.5 + bend, -len, bend);
        p.quadraticCurveTo(-len * 0.88, w * 0.15 + bend, -len * 0.8, w * 0.45 + bend * 0.8);
        p.bezierCurveTo(-len * 0.62, w * 0.75 + bend * 0.5, -len * 0.3, w, 0, w * 0.3); p.closePath();
        g.fillStyle = hGrad(g, ["#FFE07A", "#FF9F1A", "#F2552C", "#B3261E", "#8C1A14"], -len, 0); g.fill(p);
        if (core && !small) {
          const c = new Path2D(); c.moveTo(-len * 0.12, 0); c.quadraticCurveTo(-len * 0.5, -w * 0.32 + bend * 0.3, -len * 0.86, bend * 0.9); c.quadraticCurveTo(-len * 0.5, w * 0.32 + bend * 0.3, -len * 0.12, 0); c.closePath();
          g.fillStyle = hGrad(g, ["rgba(255,251,230,0.8)", "rgba(255,224,122,0.4)", "rgba(255,224,122,0)"], -len * 0.86, -len * 0.12); g.fill(c);
        }
        stroke(g, p, "rgba(140,36,16,0.7)", small ? 0.9 : 0.45);
        if (core && !small) for (let k = 0; k < 2; k++) {
          const q = (t * 1.6 + ph * 0.3 + k * 0.5) % 1, x = -len - 1 - q * 9, y = bend + sin(t * 7 + ph + k) * 1.5 - q * 1.5, r = 2.2 * (1 - q);
          const f = new Path2D(); f.moveTo(x + r, y - r * 0.6); f.quadraticCurveTo(x - r * 1.6, y, x + r, y + r * 0.6); f.quadraticCurveTo(x + r * 0.2, y, x + r, y - r * 0.6); f.closePath();
          g.fillStyle = rgba(k === 0 ? "#FFC23D" : "#FF7A1A", 0.85 * (1 - q)); g.fill(f);
        }
      };
      for (const side of [-1, 1]) {
        g.save(); if (side > 0) { g.translate(100, 0); g.scale(-1, 1); }
        g.translate(20, 56); g.rotate(flap);
        for (let i = 4; i >= 0; i--) { const len = 56 - i * 5; g.save(); g.rotate((62 - i * 15) * PI / 180); fireFeather(len, 6.6, len * 0.12, i * 1.3 + side, true); g.restore(); }
        for (let i = 3; i >= 0; i--) { const len = 24 - i * 1.5; g.save(); g.rotate((58 - i * 18) * PI / 180); fireFeather(len, 7, len * 0.08, i * 2 + side, false); g.restore(); }
        g.restore();
      }
      if (!small) for (let i = 0; i < 7; i++) {
        const ph = (t * 0.4 + i / 7) % 1, sx = i % 2 ? 1 : -1;
        g.fillStyle = rgba(i % 3 === 0 ? "#FFE07A" : "#FF9F3A", 0.8 * sin(PI * ph)); g.beginPath(); g.arc(50 + sx * (24 + i * 4) + sin(t * 2.4 + i) * 2, 44 - ph * 40, 0.55 + 0.35 * (1 - ph), 0, 2 * PI); g.fill();
      }
    } else if (kind === "capa-real") {
      const wave = sin(t * 1.7), fl = air * 6 + (party ? 3 : 0);
      const L = -6 - fl + wave * 1.2, Rr = 106 + fl - wave * 1.2;
      const cape = new Path2D(); cape.moveTo(16, 44); cape.bezierCurveTo(6, 58, L + 3, 80, L, 99);
      const hem = [];
      for (let i = 1; i <= 6; i++) { const x0 = lerp(L, Rr, (i - 0.5) / 6), x1 = lerp(L, Rr, i / 6); hem.push([x0, 102 + sin(t * 2.3 + i * 1.3) * 0.9, x1, 99 + sin(t * 2 + i) * 0.4]); }
      hem.forEach(([a, b, c, d]) => cape.quadraticCurveTo(a, b, c, d));
      cape.bezierCurveTo(Rr - 3, 80, 94, 58, 84, 44); cape.closePath();
      g.fillStyle = hGrad(g, ["#6E0726", "#C2183F", "#E2364F", "#C2183F", "#6E0726"], L, Rr); g.fill(cape);
      if (!small) {
        // dobras do veludo
        g.save(); g.clip(cape);
        for (let i = 0; i < 7; i++) { const x = lerp(L + 6, Rr - 6, i / 6) + sin(t * 1.7 + i) * 1.2; const fold = g.createLinearGradient(x - 3, 0, x + 3, 0); fold.addColorStop(0, "rgba(60,0,20,0)"); fold.addColorStop(0.5, "rgba(60,0,20,0.28)"); fold.addColorStop(1, "rgba(60,0,20,0)"); g.fillStyle = fold; g.fillRect(x - 3, 44, 6, 60); }
        g.restore();
      }
      // barra dourada
      const hemP = new Path2D(); hemP.moveTo(L, 99); hem.forEach(([a, b, c, d]) => hemP.quadraticCurveTo(a, b, c, d));
      stroke(g, hemP, "#7A4F05", small ? 2.6 : 2.2); stroke(g, hemP, GOLD, small ? 1.8 : 1.4);
      if (!small) stroke(g, hemP, "rgba(255,241,191,0.8)", 0.4);
      // gola de arminho: branca, com as pintinhas pretas, aparecendo em cima dos cantos do livro
      for (const side of [-1, 1]) {
        const cx = 50 + side * 36;
        g.save(); g.translate(cx, 43); g.rotate(side * -0.12);
        g.fillStyle = vGrad(g, ["#FFFFFF", "#ECE8F6"], -5, 5); g.beginPath(); g.ellipse(0, 0, 11, 5, 0, 0, 2 * PI); g.fill();
        stroke(g, (() => { const p = new Path2D(); p.ellipse(0, 0, 11, 5, 0, 0, 2 * PI); return p; })(), "rgba(140,130,170,0.6)", 0.5);
        if (!small) for (const [x, y] of [[-6, -1.5], [-1, -2.6], [4.5, -1.2], [8, 1], [-8.5, 1.5]]) { const p = new Path2D(); p.moveTo(x, y - 1); p.quadraticCurveTo(x + 0.7, y + 0.6, x, y + 1.3); p.quadraticCurveTo(x - 0.7, y + 0.6, x, y - 1); g.fillStyle = "#1B1B24"; g.fill(p); }
        g.restore();
      }
    } else if (kind === "socorro") {
      // mochila de primeiros socorros no ombro esquerdo: lona vermelha com o zíper prateado em volta, a cruz
      // branca grande, a alça de mão em cima e o puxador do zíper que balança (mais na festa)
      const sw = sin(t * (party ? 7 : 1.6)) * (party ? 0.28 : 0.1) + air * 0.2;
      g.save(); g.translate(2, 60); g.rotate(-0.08 + sin(t * 1.2) * 0.02 - air * 0.06); g.translate(-2, -60);
      const hd = new Path2D(); hd.moveTo(-3, 43); hd.quadraticCurveTo(2, 36.4, 7, 43); stroke(g, hd, "#5A0A14", small ? 2.4 : 2); stroke(g, hd, "#B3182A", small ? 1.4 : 1.1);
      const body = new Path2D(); body.roundRect(-10, 42.4, 26, 41.6, 5);
      g.fillStyle = hGrad(g, ["#7A0F1E", "#E0303F", "#C42032", "#6E0B18"], -10, 16); g.fill(body);
      if (!small) { g.save(); g.clip(body); for (let i = 0; i < 14; i++) line(g, "rgba(255,255,255,0.05)", -10 + i * 2, 42, -16 + i * 2, 84, 0.5); g.restore(); }
      stroke(g, body, "rgba(50,5,10,0.65)", small ? 1 : 0.5);
      const zp = new Path2D(); zp.roundRect(-8, 44.6, 22, 37.2, 3.6); stroke(g, zp, "#C9CCE0", 0.55);
      if (!small) { g.save(); g.setLineDash([0.25, 0.55]); stroke(g, zp, "rgba(60,10,20,0.8)", 0.5); g.restore(); }
      // a cruz branca (fica na parte que aparece, à esquerda do livro)
      const cx = -1, cy = 62, cr = new Path2D(); cr.roundRect(cx - 2.2, cy - 6.4, 4.4, 12.8, 0.8); cr.roundRect(cx - 6.4, cy - 2.2, 12.8, 4.4, 0.8);
      g.save(); g.translate(0.4, 0.5); g.fillStyle = "rgba(60,5,15,0.35)"; g.fill(cr); g.restore();
      g.fillStyle = vGrad(g, ["#FFFFFF", "#F1E6EA"], cy - 6.4, cy + 6.4); g.fill(cr);
      if (!small) {
        line(g, "rgba(255,255,255,0.4)", -8.8, 48, -9, 80, 0.6);
        g.save(); g.translate(-8, 50); g.rotate(sw); line(g, "#B9BCD0", 0, 0, 0, 3, 0.4);
        const pu = new Path2D(); pu.roundRect(-0.9, 3, 1.8, 3.2, 0.6); g.fillStyle = hGrad(g, ["#8E95A6", "#F3F6FA", "#9AA3B4"], -0.9, 0.9); g.fill(pu); g.restore();
      }
      g.restore();
      const sp = new Path2D(); sp.moveTo(-2, 43); sp.bezierCurveTo(2, 36, 10, 35, 16, 40);
      stroke(g, sp, "#5A0A14", small ? 3.4 : 2.8); stroke(g, sp, "#B3182A", small ? 2.2 : 1.8);
    } else if (kind === "mochila-tatica") {
      // mochila tática no ombro direito: lona preta com as faixas de fixação costuradas, duas tiras com fivela
      // de engate, a alça de mão e um patch verde-oliva com a estrela; balança de leve
      g.save(); g.translate(100, 0); g.scale(-1, 1);   // espelhada: fica do lado direito
      g.translate(2, 60); g.rotate(-0.07 + sin(t * 1.1 + 1) * 0.02 - air * 0.06); g.translate(-2, -60);
      const hd = new Path2D(); hd.moveTo(-4, 42.6); hd.quadraticCurveTo(1, 36.4, 6, 42.6); stroke(g, hd, "#0A0B0E", small ? 2.4 : 2); stroke(g, hd, "#3A3F4C", small ? 1.4 : 1.1);
      const body = new Path2D(); body.roundRect(-11, 42, 27, 43, 4.4);
      g.fillStyle = hGrad(g, ["#0E1014", "#343844", "#22252D", "#0A0B0E"], -11, 16); g.fill(body);
      stroke(g, body, "rgba(0,0,0,0.75)", small ? 1 : 0.5);
      for (const y of [52, 58, 64, 70]) {
        g.fillStyle = "#14161C"; g.fillRect(-10, y, 19, 2.4);
        if (!small) { for (let x = -9; x < 9; x += 3.2) line(g, "rgba(150,155,170,0.45)", x, y + 0.2, x, y + 2.2, 0.25); line(g, "rgba(255,255,255,0.08)", -10, y + 0.2, 9, y + 0.2, 0.3); }
      }
      for (const y of [46.6, 77]) {
        g.fillStyle = "#1B1D24"; g.fillRect(-11, y, 21, 1.8);
        const bk = new Path2D(); bk.roundRect(-6.4, y - 0.6, 4.2, 3, 0.6); g.fillStyle = hGrad(g, ["#3A3E4A", "#6B7088", "#3A3E4A"], -6.4, -2.2); g.fill(bk); stroke(g, bk, "rgba(0,0,0,0.6)", 0.25);
        line(g, "#1B1D24", -4.3, y - 0.2, -4.3, y + 2.2, 0.5);
      }
      const pt = new Path2D(); pt.roundRect(-8.4, 55.2, 9.4, 7.2, 1.2); g.fillStyle = vGrad(g, ["#7E8B4E", "#5C6934"], 55.2, 62.4); g.fill(pt); stroke(g, pt, "rgba(20,25,5,0.6)", 0.3);
      if (!small) { g.save(); g.setLineDash([0.6, 0.45]); const st = new Path2D(); st.roundRect(-7.8, 55.8, 8.2, 6, 0.9); stroke(g, st, "rgba(230,225,180,0.7)", 0.2); g.restore(); }
      g.fillStyle = "#2B3214"; g.fill(star5(g, -3.7, 59, 2));
      if (!small) line(g, "rgba(255,255,255,0.18)", -9.8, 46, -9.8, 82, 0.6);
      const sp = new Path2D(); sp.moveTo(-2, 43); sp.bezierCurveTo(2, 36, 10, 35, 16, 40);
      stroke(g, sp, "#0A0B0E", small ? 3.4 : 2.8); stroke(g, sp, "#2E323C", small ? 2.2 : 1.8);
      g.restore();
    } else if (kind === "arquivo") {
      // arquivo de pastas atrás do livro: o gaveteiro de aço cinza-azulado aparece por cima e pelos lados,
      // com a gaveta de cima aberta e as pastas coloridas espetadas (que sobem e descem), o porta-etiqueta,
      // o puxador e os pezinhos embaixo
      const top = 18, cab = new Path2D(); cab.roundRect(5, top, 90, 74, 3);
      g.fillStyle = hGrad(g, ["#5E6A80", "#A9B4C8", "#8A96AC", "#55607A"], 5, 95); g.fill(cab);
      stroke(g, cab, "rgba(20,25,40,0.6)", small ? 1 : 0.5);
      for (const y of [42, 66]) line(g, "rgba(30,35,55,0.55)", 5, y, 95, y, 0.5, false);
      g.fillStyle = "#2E3346"; g.fillRect(8, 92, 6, 2.4); g.fillRect(86, 92, 6, 2.4);
      const cols = ["#FF6F8E", "#FFD23F", "#6FB0FF", "#7EE0B8", "#B995F5", "#FF9F3A", "#5247E0"];
      for (let i = 0; i < 7; i++) {
        const x = 21.8 + i * 9.4, y = top - 3 + (i % 2) * 1.4 + sin(t * 1.6 + i * 1.3) * (party ? 1.6 : 0.6) - air * 1.5;
        const f = new Path2D(); f.moveTo(x - 4, y + 8); f.lineTo(x - 4, y + 1); f.lineTo(x - 1, y + 1); f.lineTo(x, y - 0.8); f.lineTo(x + 3, y - 0.8); f.lineTo(x + 3.6, y + 1); f.lineTo(x + 4, y + 1); f.lineTo(x + 4, y + 8); f.closePath();
        g.fillStyle = cols[i]; g.fill(f); g.fillStyle = "rgba(255,255,255,0.28)"; g.fillRect(x - 4, y + 1, 8, 1.2); stroke(g, f, "rgba(30,30,60,0.45)", 0.3);
        if (!small) { g.fillStyle = "#FFFFFF"; g.fillRect(x + 0.4, y - 0.2, 2.2, 0.8); }
      }
      const dr = new Path2D(); dr.roundRect(12, top + 2, 76, 22, 2);
      g.fillStyle = vGrad(g, ["#C3CCDC", "#8E9AB0", "#6E7A92"], top + 2, top + 24); g.fill(dr); stroke(g, dr, "rgba(20,25,40,0.55)", 0.45);
      line(g, "rgba(255,255,255,0.55)", 14, top + 3.2, 86, top + 3.2, 0.5);
      const lb = new Path2D(); lb.roundRect(42, top + 5.4, 16, 6, 0.8); g.fillStyle = hGrad(g, ["#8E95A6", "#F3F6FA", "#9AA3B4"], 42, 58); g.fill(lb); stroke(g, lb, "rgba(30,35,50,0.5)", 0.3);
      g.fillStyle = "#FFFDF4"; g.fillRect(43.2, top + 6.4, 13.6, 4);
      if (!small) for (const [yy, w] of [[top + 7.6, 9], [top + 9.2, 6]]) line(g, "rgba(60,60,110,0.55)", 44.4, yy, 44.4 + w, yy, 0.4);
      const hb = new Path2D(); hb.roundRect(41, top + 14, 18, 3.2, 1.6); g.fillStyle = hGrad(g, ["#5E6578", "#E8ECF4", "#7C8398"], 41, 59); g.fill(hb); stroke(g, hb, "rgba(20,25,40,0.6)", 0.3);
    } else if (kind === "malote") {
      // malote do banco: bolsa de lona cinza-azulada pendurada no ombro direito pela alça de couro, com o
      // reforço de couro embaixo, o zíper e o cadeado de latão que balança
      const sw = sin(t * (party ? 6 : 1.4)) * (party ? 0.12 : 0.05) + air * 0.08;
      const strap = new Path2D(); strap.moveTo(78, 44); strap.bezierCurveTo(86, 36, 96, 40, 100, 57);
      stroke(g, strap, "#3A2418", small ? 3.2 : 2.6); stroke(g, strap, "#7A5236", small ? 2 : 1.6);
      g.save(); g.translate(100, 57); g.rotate(sw); g.translate(-100, -57);
      const bag = new Path2D(); bag.moveTo(85, 58); bag.lineTo(113, 58); bag.quadraticCurveTo(115, 58, 114.6, 61); bag.lineTo(112.6, 84); bag.quadraticCurveTo(112.4, 86, 110, 86); bag.lineTo(88, 86); bag.quadraticCurveTo(85.6, 86, 85.4, 84); bag.lineTo(83.4, 61); bag.quadraticCurveTo(83, 58, 85, 58); bag.closePath();
      g.fillStyle = hGrad(g, ["#4A5670", "#8C9AB4", "#6E7C98", "#3E4860"], 83, 115); g.fill(bag);
      g.save(); g.clip(bag);
      if (!small) { for (let i = 0; i < 17; i++) line(g, "rgba(255,255,255,0.06)", 83 + i * 2, 58, 83 + i * 2, 86, 0.4); for (let y = 59; y < 86; y += 1.6) line(g, "rgba(0,0,0,0.05)", 83, y, 115, y, 0.3); }
      g.fillStyle = vGrad(g, ["#7A5236", "#4A2E1C"], 80.6, 86); g.fillRect(80, 80.6, 40, 6);
      g.restore();
      stroke(g, bag, "rgba(20,25,40,0.6)", small ? 1 : 0.5);
      line(g, "#C9CCE0", 85, 61.4, 113, 61.4, 0.6);
      if (!small) for (let i = 0; i < 14; i++) line(g, "rgba(40,45,60,0.7)", 85.6 + i * 2, 61, 85.6 + i * 2, 61.8, 0.25);
      g.save(); g.translate(98.5, 62); g.rotate(sin(t * 2.2) * 0.12);
      const sh = new Path2D(); sh.moveTo(-1.6, 3); sh.lineTo(-1.6, 1.4); sh.arc(0, 1.4, 1.6, PI, 0); sh.lineTo(1.6, 3); stroke(g, sh, "#7C8398", 0.75); stroke(g, sh, "#E3E6F0", 0.3);
      const lk = new Path2D(); lk.roundRect(-2.6, 2.6, 5.2, 4.8, 1); g.fillStyle = vGrad(g, ["#FFE58A", "#E9AE34", "#9C6A12"], 2.6, 7.4); g.fill(lk); stroke(g, lk, "rgba(90,60,5,0.7)", 0.3);
      circle(g, "#4A2E06", 0.55, 0, 4.4); line(g, "#4A2E06", 0, 4.6, 0, 6, 0.45); line(g, "rgba(255,255,255,0.55)", -1.8, 3.4, -1.8, 6.6, 0.3);
      g.restore();
      g.restore();
    } else if (kind === "cofre") {
      // cofre de ferro nas costas (lendário): a caixa pesada aparece por cima e pelo lado direito do livro, com
      // chanfro, rebites e filete dourado na porta; a roda de segredo cromada gira para lá e para cá, o volante
      // de três braços gira devagar, e na festa moedas de ouro pulam lá de dentro
      const X = 58, Y = 12, W = 48, H = 50;
      g.save(); g.translate(82, 40); g.rotate(0.07 + sin(t * 1.1) * 0.012 - air * 0.03); g.translate(-82, -40);
      if (party && !small) for (let i = 0; i < 3; i++) {
        const ph = (t * 0.8 + i / 3) % 1, cx = X + 14 + i * 10 + ph * (i - 1) * 6, cy = Y + 2 - sin(PI * ph) * 13, rx = 0.4 + 2 * abs(cos(t * 6 + i));
        g.beginPath(); g.ellipse(cx, cy, rx, 2.2, 0, 0, 2 * PI); g.fillStyle = vGrad(g, GOLD3, cy - 2.2, cy + 2.2); g.fill(); g.strokeStyle = "rgba(120,80,10,0.7)"; g.lineWidth = 0.3; g.stroke();
      }
      g.fillStyle = "rgba(10,12,20,0.3)"; g.beginPath(); g.roundRect(X + 1.2, Y + 1.4, W, H, 3); g.fill();
      const box = new Path2D(); box.roundRect(X, Y, W, H, 3);
      g.fillStyle = hGrad(g, ["#1C1F26", "#4A505E", "#6A7182", "#3A3F4A", "#16181E"], X, X + W); g.fill(box);
      g.save(); g.clip(box); g.fillStyle = vGrad(g, ["#9AA2B4", "#4A505E"], Y, Y + 3.4); g.fillRect(X, Y, W, 3.4); g.restore();
      stroke(g, box, "rgba(5,6,10,0.75)", small ? 1.1 : 0.6);
      const door = new Path2D(); door.roundRect(X + 4, Y + 5, W - 8, H - 9, 2);
      g.fillStyle = vGrad(g, ["#5A6172", "#3A3F4C", "#2A2E38"], Y + 5, Y + H - 4); g.fill(door);
      stroke(g, door, "#C98A12", small ? 0.8 : 0.5); if (!small) stroke(g, door, "rgba(255,241,191,0.35)", 0.2);
      for (const [rx, ry] of [[X + 2, Y + 2], [X + W - 2, Y + 2], [X + 2, Y + H - 2], [X + W - 2, Y + H - 2], [X + W / 2, Y + 2], [X + 2, Y + H / 2], [X + W - 2, Y + H / 2]]) { circle(g, "#16181E", 0.8, rx, ry); circle(g, "#A9B0C0", 0.4, rx - 0.2, ry - 0.2); }
      // roda de segredo
      const dx = X + 20, dy = Y + 17, R = 6.2, spin = sin(t * 0.9) * 1.6 + (party ? t * 3 : 0);
      circle(g, "rgba(5,6,10,0.4)", R + 0.4, dx + 0.5, dy + 0.6);
      circle(g, vGrad(g, ["#FFFFFF", "#C3C9D6", "#6E7588"], dy - R, dy + R), R, dx, dy);
      circle(g, "#1E2028", R - 1.1, dx, dy);
      g.save(); g.translate(dx, dy); g.rotate(spin);
      for (let i = 0; i < 24; i++) { const a = i * PI / 12, r0 = i % 6 ? R - 1.9 : R - 2.6; line(g, "#E3E6F0", cos(a) * r0, sin(a) * r0, cos(a) * (R - 1.3), sin(a) * (R - 1.3), i % 6 ? 0.22 : 0.4); }
      circle(g, vGrad(g, ["#F3F6FA", "#9AA3B4", "#5E6578"], -2.6, 2.6), 2.6, 0, 0);
      line(g, "#3C4054", 0, -2.2, 0, -0.6, 0.5); circle(g, "rgba(255,255,255,0.7)", 0.6, -0.9, -0.9);
      g.restore();
      const pt = new Path2D(); pt.moveTo(dx - 1, dy - R - 1.3); pt.lineTo(dx + 1, dy - R - 1.3); pt.lineTo(dx, dy - R + 0.4); pt.closePath(); g.fillStyle = GOLD; g.fill(pt); stroke(g, pt, "rgba(110,70,0,0.7)", 0.2);
      // volante de três braços
      const wx = X + W - 11, wy = Y + 30, wa = t * 0.6 + (party ? t * 2 : 0);
      for (let i = 0; i < 3; i++) { const a = wa + i * 2 * PI / 3, ex = wx + cos(a) * 5, ey = wy + sin(a) * 5; line(g, "#2A2E38", wx, wy, ex, ey, 1.3); line(g, "#C3C9D6", wx, wy, ex, ey, 0.7); circle(g, vGrad(g, ["#FFFFFF", "#8E95A6"], ey - 1.1, ey + 1.1), 1.1, ex, ey); }
      circle(g, vGrad(g, ["#F3F6FA", "#6E7588"], wy - 1.7, wy + 1.7), 1.7, wx, wy);
      if (!small) { const k = Math.max(0, sin(t * 1.2)) ** 8; if (k > 0.02) star4(g, dx + 4, dy - 4.4, 3 * k, `rgba(255,255,255,${k})`); const k2 = Math.max(0, sin(t * 1.2 + 2.5)) ** 8; if (k2 > 0.02) star4(g, X + W - 3, Y + 3, 2.6 * k2, rgba(GOLD, k2)); }
      g.restore();
    } else if (kind === "cupulas") {
      // Congresso atrás do Folha (lendário): tudo fica acima do livro. As duas torres gêmeas no centro, bem
      // altas, com as janelinhas que acendem e a passarela; a cúpula do Senado (convexa) à esquerda e a tigela
      // da Câmara (côncava) à direita, saindo para fora das laterais; a laje branca com o espelho d'água e um
      // brilho de fim de tarde redondo e suave atrás
      if (!small) {
        const gl = g.createRadialGradient(50, 18, 2, 50, 18, 38); gl.addColorStop(0, "rgba(255,214,140,0.5)"); gl.addColorStop(0.6, "rgba(150,190,255,0.18)"); gl.addColorStop(1, "rgba(150,190,255,0)");
        g.fillStyle = gl; g.beginPath(); g.arc(50, 18, 38, 0, 2 * PI); g.fill();
      }
      const CON = ["#FFFFFF", "#E6E9F2", "#B9C0D2"], ed = "rgba(50,60,95,0.6)", ew = small ? 0.9 : 0.45;
      // torres
      for (const x of [42.8, 51.6]) {
        const tw = new Path2D(); tw.rect(x, -16, 5.6, 56); g.fillStyle = hGrad(g, ["#C9CFDD", "#FFFFFF", "#AEB6C8"], x, x + 5.6); g.fill(tw); stroke(g, tw, ed, ew);
        if (!small) for (let k = 0; k < 17; k++) { const lit = sin(t * 0.7 + k * 1.7 + x) > 0.6; g.fillStyle = lit ? "rgba(255,214,110,0.95)" : "rgba(70,90,140,0.5)"; g.fillRect(x + 0.9, -14.4 + k * 3, 3.8, 1.2); }
      }
      const br = new Path2D(); br.rect(48.4, -3, 3.2, 5); g.fillStyle = vGrad(g, CON, -3, 2); g.fill(br); stroke(g, br, ed, ew);
      // Senado: cúpula convexa à esquerda
      const dm = new Path2D(); dm.moveTo(-17, 38); dm.bezierCurveTo(-17, 19, 19, 19, 19, 38); dm.closePath();
      g.fillStyle = vGrad(g, CON, 24, 38); g.fill(dm); stroke(g, dm, ed, ew);
      if (!small) { const hl = new Path2D(); hl.moveTo(-12, 35); hl.bezierCurveTo(-11, 28, -3, 25.2, 3, 25.6); stroke(g, hl, "rgba(255,255,255,0.95)", 0.8); }
      // Câmara: tigela côncava à direita, num pedestal
      const ped = new Path2D(); ped.moveTo(96, 38); ped.lineTo(98, 32); ped.lineTo(104, 32); ped.lineTo(106, 38); ped.closePath(); g.fillStyle = vGrad(g, CON, 32, 38); g.fill(ped); stroke(g, ped, ed, ew);
      const bw = new Path2D(); bw.moveTo(83, 21); bw.quadraticCurveTo(101, 19, 119, 21); bw.bezierCurveTo(117, 34, 85, 34, 83, 21); bw.closePath();
      g.fillStyle = vGrad(g, CON, 21, 32); g.fill(bw); stroke(g, bw, ed, ew);
      g.beginPath(); g.ellipse(101, 20.8, 17.6, 2, 0, 0, 2 * PI); g.fillStyle = vGrad(g, ["#9AA3B8", "#D7DCE8"], 18.8, 22.8); g.fill(); g.strokeStyle = ed; g.lineWidth = ew; g.stroke();
      // laje e espelho d'água
      const slab = new Path2D(); slab.roundRect(-20, 38, 140, 4, 0.8); g.fillStyle = vGrad(g, CON, 38, 42); g.fill(slab); stroke(g, slab, ed, ew);
      if (!small) {
        for (let i = 0; i < 6; i++) { const x = -20 + ((t * 4 + i * 23) % 140); line(g, "rgba(110,170,240,0.8)", x, 40, x + 3, 40, 0.35); }
        for (const [i, x] of [[0, 45.6], [1, 54.4]]) { const k = Math.max(0, sin(t * 1.1 + i * 2)) ** 8; if (k > 0.02) star4(g, x, -16.5, 2.6 * k, `rgba(255,255,255,${k})`); }
      }
    } else if (kind === "mochila-escolar") {
      // mochila escolar colorida no ombro direito: corpo turquesa, tampa rosa com bolinhas, bolso amarelo com
      // zíper e três chaveirinhos pendurados (coração, pompom e estrelinha) que balançam, mais na festa
      const sw = sin(t * (party ? 7 : 1.6)) * (party ? 0.3 : 0.12) + air * 0.2;
      g.save(); g.translate(100, 0); g.scale(-1, 1);   // espelhada: fica do lado direito
      g.translate(2, 60); g.rotate(-0.08 + sin(t * 1.2 + 2) * 0.02 - air * 0.06); g.translate(-2, -60);
      const body = new Path2D(); body.moveTo(-9, 50); body.quadraticCurveTo(-9.6, 43, -2, 42.4); body.lineTo(10, 42.4); body.quadraticCurveTo(15.6, 43, 15.4, 50); body.lineTo(15, 80); body.quadraticCurveTo(15, 84, 10.6, 84); body.lineTo(-4.6, 84); body.quadraticCurveTo(-9, 84, -9, 80); body.closePath();
      g.fillStyle = hGrad(g, ["#16807F", "#4FD6D0", "#2BB5B0", "#11706F"], -9, 15.4); g.fill(body);
      stroke(g, body, "rgba(5,50,50,0.6)", small ? 1 : 0.5);
      const flap = new Path2D(); flap.moveTo(-8.4, 50); flap.quadraticCurveTo(-8.6, 44, -2, 43.6); flap.lineTo(10, 43.6); flap.quadraticCurveTo(14.8, 44, 14.8, 50); flap.quadraticCurveTo(3, 56, -8.4, 50); flap.closePath();
      g.fillStyle = vGrad(g, ["#FF9EC4", "#E85A92"], 43.6, 54); g.fill(flap);
      if (!small) { g.save(); g.clip(flap); for (const [x, y] of [[-6, 46.4], [-2.4, 48.8], [-5, 50.2], [1.4, 45.8], [5, 48.6], [8.6, 46], [11.6, 49]]) circle(g, "rgba(255,255,255,0.75)", 0.6, x, y); g.restore(); }
      stroke(g, flap, "rgba(120,20,60,0.5)", 0.35);
      const pk = new Path2D(); pk.roundRect(-6.4, 63, 18.8, 16, 3); g.fillStyle = vGrad(g, ["#FFE45C", "#F5B83D"], 63, 79); g.fill(pk); stroke(g, pk, "rgba(120,80,10,0.5)", 0.35);
      line(g, "#C9CCE0", -4.6, 66.2, 10.6, 66.2, 0.55);
      if (!small) {
        line(g, "rgba(255,255,255,0.35)", -7.6, 52, -7.8, 80, 0.6);
        const charm = (x, i, draw) => { g.save(); g.translate(x, 66.4); g.rotate(sw * (1 + i * 0.25) + (i - 1) * 0.08); line(g, "#B9BCD0", 0, 0, 0, 2.6, 0.3); draw(); g.restore(); };
        charm(-5.4, 0, () => { g.fillStyle = vGrad(g, ["#FF8FA3", "#E0475E"], 2.6, 5.6); g.fill(heart(0, 4, 1.6)); });
        charm(-2.8, 1, () => { for (let k = 0; k < 10; k++) { const a = k * PI / 5; circle(g, "#9A6CF0", 0.7, cos(a) * 1.3, 4.6 + sin(a) * 1.3); } circle(g, vGrad(g, ["#D9C6FF", "#8A63E0"], 3, 6.2), 1.4, 0, 4.6); });
        charm(-0.2, 2, () => { g.fillStyle = vGrad(g, GOLD3, 2.6, 6.2); g.fill(star5(g, 0, 4.4, 1.8)); stroke(g, star5(g, 0, 4.4, 1.8), "rgba(120,80,10,0.55)", 0.2); });
      }
      const sp = new Path2D(); sp.moveTo(-2, 43); sp.bezierCurveTo(2, 36, 10, 35, 16, 40);
      stroke(g, sp, "#A8235E", small ? 3.4 : 2.8); stroke(g, sp, "#FF6FA8", small ? 2.2 : 1.8);
      g.restore();
    } else if (kind === "pastas") {
      // torre de pastas (lendário): uma pilha alta de pastas coloridas atrás do ombro esquerdo, cada uma com a
      // aba, a etiqueta na lombada e as folhas aparecendo; a torre balança como gelatina (mais no pulo e na
      // festa) e lá no topo fica a pasta dourada com a estrela, que brilha
      const N = small ? 7 : 10, amp = (party ? 2.2 : 1) + air * 2;
      const cols = [["#FF8FA3", "#E0475E"], ["#FFE58A", "#F5B83D"], ["#8EC5FF", "#3F7FDC"], ["#9FF0C8", "#3FBF8F"], ["#CDB6FF", "#8A63E0"], ["#FFC28A", "#F08A2E"]];
      for (let i = 0; i < N; i++) {
        const k = i / (N - 1), wob = sin(t * 1.5 - i * 0.35) * amp, x = 24 + sin(i * 2.3) * 1.6 + wob * k * k * 3, y = 44 - i * (small ? 7 : 5.2) - 2.6, a = sin(i * 1.7) * 0.05 + wob * k * 0.035;
        const top = i === N - 1, [c1, c2] = top ? ["#FFF1BF", "#C98A12"] : cols[i % cols.length];
        g.save(); g.translate(x, y); g.rotate(a);
        const tab = new Path2D(); tab.roundRect(6, -3.6, 5, 1.8, 0.5); g.fillStyle = c1; g.fill(tab); stroke(g, tab, "rgba(30,25,60,0.4)", 0.25);
        g.fillStyle = "#FFFDF4"; g.fillRect(-11.4, -3.1, 16, 1.2);
        const f = new Path2D(); f.roundRect(-12.5, -2.4, 25, 4.8, 0.9); g.fillStyle = vGrad(g, [c1, c2], -2.4, 2.4); g.fill(f); stroke(g, f, "rgba(30,25,60,0.45)", small ? 0.7 : 0.3);
        if (!small) { g.fillStyle = "rgba(255,255,255,0.85)"; g.fillRect(-10.8, -1.2, 6, 2.4); line(g, "rgba(60,60,110,0.5)", -10, -0.3, -6.4, -0.3, 0.3); line(g, "rgba(60,60,110,0.5)", -10, 0.6, -7.4, 0.6, 0.3); line(g, "rgba(255,255,255,0.35)", -3, -1.7, 10.6, -1.7, 0.4); }
        if (top) {
          g.fillStyle = "#FFFDF4"; g.fill(star5(g, 3, 0, 1.9)); stroke(g, star5(g, 3, 0, 1.9), "rgba(120,80,10,0.6)", 0.2);
          if (!small) { const kk = Math.max(0, sin(t * 1.4)) ** 6; if (kk > 0.02) star4(g, 9, -4.4, 2.8 * kk, `rgba(255,255,255,${kk})`); }
        }
        g.restore();
      }
    } else if (kind === "borboleta") {
      // asas de borboleta: duas asas de cada lado (a de cima grande, a de baixo menor) presas nos ombros, em
      // degradê amarelo, laranja, rosa e roxo, com borda escura de pintinhas brancas, nervuras e um "olho"
      // azul; batem devagar (mais rápido no pulo e na festa)
      const busy = party || air > 0.1, beat = 0.72 + 0.28 * cos(t * (busy ? 6 : 1.7));
      for (const side of [-1, 1]) {
        g.save(); if (side > 0) { g.translate(100, 0); g.scale(-1, 1); }
        g.translate(24, 52); g.scale(beat, 1);
        const up = new Path2D(); up.moveTo(0, -1); up.bezierCurveTo(-6, -24, -34, -38, -44, -26); up.bezierCurveTo(-50, -14, -34, -2, 0, 2); up.closePath();
        const lo = new Path2D(); lo.moveTo(0, 2); lo.bezierCurveTo(-30, 2, -40, 22, -30, 32); lo.bezierCurveTo(-20, 39, -6, 24, 0, 4); lo.closePath();
        const wing = (p, r) => { const rg = g.createRadialGradient(0, 0, 2, 0, 0, r); rg.addColorStop(0, "#FFE680"); rg.addColorStop(0.35, "#FF9F3A"); rg.addColorStop(0.7, "#F0477A"); rg.addColorStop(1, "#7B3FD0"); g.fillStyle = rg; g.fill(p); };
        wing(lo, 40); wing(up, 50);
        if (!small) {
          for (const [x, y] of [[-40, -24], [-30, -30], [-42, -14], [-30, -4]]) line(g, "rgba(70,20,60,0.35)", 0, 0, x, y, 0.4);
          for (const [x, y] of [[-28, 26], [-34, 14], [-14, 30]]) line(g, "rgba(70,20,60,0.35)", 0, 3, x, y, 0.4);
        }
        circle(g, "#2A1840", 3.4, -30, -18); circle(g, "#3FA8FF", 2.4, -30, -18); circle(g, "#FFFFFF", 0.9, -30.6, -18.6);
        stroke(g, up, "#2A1840", small ? 2 : 1.5); stroke(g, lo, "#2A1840", small ? 2 : 1.5);
        if (!small) for (const [x, y] of [[-43, -26], [-39.5, -30.5], [-45.6, -18], [-38, -7.4], [-33.6, 29.6], [-37.4, 21], [-24, 35]]) circle(g, "#FFFFFF", 0.55, x, y);
        g.restore();
      }
    } else if (kind === "heroi") {
      // capa de herói (lendário): vermelha, comprida e esvoaçante, puxada pelo vento para o lado, com o forro
      // dourado aparecendo na barra, a gola alta atrás dos ombros e os dois fechos de estrela; no pulo e na
      // festa ela se abre mais e aparecem as riscas de vento
      const fl = air * 8 + (party ? 4 : 0), wave = sin(t * 1.6), wind = 4 + sin(t * 0.9) * 2.5 + fl * 0.8;
      const L = -8 - fl + wave * 1.6, R = 108 + fl - wave * 1.6, hem = [];
      for (let i = 1; i <= 7; i++) { const u0 = (i - 0.5) / 7, u1 = i / 7; hem.push([lerp(L, R, u0) + wind * u0, 104 + sin(t * 2.6 + i * 1.2) * 2.4 + fl * 0.3, lerp(L, R, u1) + wind * u1, 100 + sin(t * 2.2 + i * 0.9) * 1.2]); }
      const capeP = (dy) => { const p = new Path2D(); p.moveTo(16, 44); p.bezierCurveTo(6, 60, L + 2, 82, L, 100 + dy); hem.forEach(([a, b, c, d]) => p.quadraticCurveTo(a, b + dy, c, d + dy)); p.bezierCurveTo(R + wind - 2, 82, 95, 60, 84, 44); p.closePath(); return p; };
      const lining = capeP(2.2); g.fillStyle = hGrad(g, ["#B57A0C", "#FFD23F", "#F5B83D", "#9C6A12"], L, R + wind); g.fill(lining); stroke(g, lining, "rgba(90,50,0,0.5)", small ? 0.9 : 0.4);
      const cape = capeP(0); g.fillStyle = hGrad(g, ["#7A0A1A", "#D61F2E", "#FF4B4B", "#D61F2E", "#7A0A1A"], L, R + wind); g.fill(cape);
      if (!small) {
        g.save(); g.clip(cape);
        for (let i = 0; i < 7; i++) { const x = lerp(L + 6, R - 6, i / 6) + wind * (i / 6) + sin(t * 1.6 + i) * 1.4; const fold = g.createLinearGradient(x - 3, 0, x + 3, 0); fold.addColorStop(0, "rgba(70,0,10,0)"); fold.addColorStop(0.5, "rgba(70,0,10,0.3)"); fold.addColorStop(1, "rgba(70,0,10,0)"); g.fillStyle = fold; g.fillRect(x - 3, 44, 6, 64); }
        const sx = -40 + ((t * 0.28) % 1) * 190, sh = g.createLinearGradient(sx, 40, sx + 16, 104); sh.addColorStop(0, "rgba(255,255,255,0)"); sh.addColorStop(0.5, "rgba(255,210,210,0.22)"); sh.addColorStop(1, "rgba(255,255,255,0)"); g.fillStyle = sh; g.fillRect(-20, 40, 150, 70);
        g.restore();
      }
      stroke(g, cape, "rgba(60,0,10,0.6)", small ? 1 : 0.5);
      for (const side of [-1, 1]) {
        g.save(); if (side > 0) { g.translate(100, 0); g.scale(-1, 1); }
        const col = new Path2D(); col.moveTo(11, 45); col.lineTo(4, 29 - fl * 0.2); col.quadraticCurveTo(15, 33, 23, 43); col.closePath();
        g.fillStyle = vGrad(g, ["#FFE08A", "#E9A21A"], 29, 45); g.fill(col);
        const out = new Path2D(); out.moveTo(11, 45); out.lineTo(4, 29 - fl * 0.2); out.quadraticCurveTo(7, 38, 16, 44.6); out.closePath(); g.fillStyle = vGrad(g, ["#FF4B4B", "#A8142A"], 29, 45); g.fill(out);
        stroke(g, col, "rgba(60,0,10,0.55)", small ? 0.9 : 0.4);
        circle(g, "rgba(60,30,0,0.35)", 2.4, 14.3, 41.6); circle(g, vGrad(g, GOLD3, 39, 43.4), 2.2, 14, 41.2); g.fillStyle = "#FFFDF4"; g.fill(star5(g, 14, 41.3, 1.3));
        g.restore();
      }
      if ((party || air > 0.1) && !small) for (let i = 0; i < 4; i++) {
        const ph = (t * 1.8 + i / 4) % 1, y = 58 + i * 9, a = 0.55 * sin(PI * ph);
        line(g, `rgba(255,255,255,${a})`, -6 - ph * 14, y, -1 - ph * 14, y, 0.6); line(g, `rgba(255,255,255,${a})`, 106 + ph * 14, y + 4, 111 + ph * 14, y + 4, 0.6);
      }
    }
  }
  // ---- vida: notas do assobio, corações do carinho e o balão de pensamento
  function drawLife(g, t, ex, small) {
    if (ex.whistle > 0.05) for (let i = 0; i < 3; i++) {
      const ph = (t * 0.7 + i / 3) % 1, a = sin(PI * ph) * ex.whistle, x = 62 + ph * 22 + sin(t * 3 + i) * 2, y = 74 - ph * 30;
      g.save(); g.translate(x, y); g.rotate(sin(t * 2 + i) * 0.3); g.scale(1.8, 1.8);
      const c = `rgba(58,47,196,${a})`; g.beginPath(); g.ellipse(0, 0, 1.6, 1.15, -0.45, 0, 2 * PI); g.fillStyle = c; g.fill();
      line(g, c, 1.35, -0.3, 1.35, -5.6, 0.5); const fl = new Path2D(); fl.moveTo(1.35, -5.6); fl.bezierCurveTo(3.4, -4.8, 3.8, -3.2, 2.8, -2); stroke(g, fl, c, 0.55);
      g.restore();
    }
    if (ex.hearts > 0.05) for (let i = 0; i < (small ? 2 : 4); i++) {
      const ph = (t * 0.6 + i / 4) % 1, a = sin(PI * ph) * ex.hearts, side = i % 2 ? 1 : -1, x = 50 + side * (24 + i * 4) + sin(t * 2 + i) * 2, y = 44 - ph * 34, r = 2.4 + ph * 1.4;
      g.save(); g.globalAlpha = a; g.fillStyle = vGrad(g, ex.goldHearts ? GOLD3 : ["#FF8FA3", "#E0475E"], y - r, y + r); g.fill(heart(x, y, r)); g.restore();
    }
    if (ex.sparkle > 0.05) for (let i = 0; i < 8; i++) {
      const a = i * PI / 4 + t, ph = (t * 1.5 + i / 8) % 1, r = 34 + ph * 14;
      star4(g, 50 + cos(a) * r, 58 + sin(a) * r * 0.75, 3 * sin(PI * ph) * ex.sparkle, i % 2 ? `rgba(255,255,255,${ex.sparkle})` : rgba(GOLD, ex.sparkle));
    }
    if (ex.thought && !small) drawThought(g, t, ex.thought);
  }
  // Balão de pensamento (nuvenzinha com um desenho dentro) em cima, à direita do chapéu.
  function drawThought(g, t, th) {
    const u = th.u, pop = u < 0.12 ? outBack(u / 0.12) : u > 0.88 ? 1 - (u - 0.88) / 0.12 : 1;
    if (pop <= 0) return;
    const cx = 84, cy = 0 + sin(t * 2) * 0.8, sc = (0.4 + 0.6 * pop) * 1.4;
    g.save(); g.globalAlpha = clamp(pop * 1.4, 0, 1);
    for (const [x, y, r] of [[70, 22, 1.4], [75, 14.5, 2.3]]) { circle(g, "rgba(38,33,92,0.18)", r + 0.7, x, y); circle(g, "#FFFFFF", r, x, y); }
    g.translate(cx, cy); g.scale(sc, sc);
    const puffs = [[-6, 1.5, 5], [0, -2.5, 6.6], [6, 1, 5.2], [0, 3.6, 5.4], [-3, -1, 5], [3.4, -1.4, 5]];
    for (const [x, y, r] of puffs) circle(g, "rgba(38,33,92,0.22)", r + 0.9, x, y + 0.6);
    for (const [x, y, r] of puffs) circle(g, "#FFFFFF", r, x, y);
    drawThoughtIcon(g, th.kind, t);
    g.restore();
  }
  function drawThoughtIcon(g, kind, t) {
    if (kind === "bulb") {
      const gl = g.createRadialGradient(0, -1.4, 0.4, 0, -1.4, 6); gl.addColorStop(0, "rgba(255,224,122,0.9)"); gl.addColorStop(1, "rgba(255,224,122,0)"); g.fillStyle = gl; g.beginPath(); g.arc(0, -1.4, 6, 0, 2 * PI); g.fill();
      circle(g, "#FFD84A", 3, 0, -1.6); g.fillStyle = "#8E92A6"; g.fillRect(-1.5, 1.2, 3, 2.2); line(g, "#6B7088", -1.5, 2.3, 1.5, 2.3, 0.35);
      circle(g, "rgba(255,255,255,0.8)", 0.8, -1, -2.6);
    } else if (kind === "heart") { g.fillStyle = vGrad(g, ["#FF8FA3", "#E0475E"], -4, 3); g.fill(heart(0, 0, 3.4)); }
    else if (kind === "star") { g.fillStyle = vGrad(g, GOLD3, -4, 4); g.fill(star5(g, 0, 0.3, 4.2)); }
    else if (kind === "book") {
      const l = new Path2D(); l.moveTo(0, -1.6); l.quadraticCurveTo(-2.4, -3, -4.6, -2.2); l.lineTo(-4.6, 2.6); l.quadraticCurveTo(-2.4, 1.8, 0, 3); l.quadraticCurveTo(2.4, 1.8, 4.6, 2.6); l.lineTo(4.6, -2.2); l.quadraticCurveTo(2.4, -3, 0, -1.6); l.closePath();
      g.fillStyle = "#5247E0"; g.fill(l); line(g, "#FFFFFF", 0, -1.4, 0, 2.8, 0.4); line(g, "rgba(255,255,255,0.7)", -3.6, -0.6, -1, -0.2, 0.3); line(g, "rgba(255,255,255,0.7)", 1, -0.2, 3.6, -0.6, 0.3);
    } else if (kind === "coffee") {
      g.fillStyle = "#FFFFFF"; g.strokeStyle = INK; g.lineWidth = 0.45; g.beginPath(); g.moveTo(-3, -1.2); g.lineTo(2.4, -1.2); g.lineTo(2, 3); g.lineTo(-2.6, 3); g.closePath(); g.fill(); g.stroke();
      g.beginPath(); g.arc(2.8, 0.8, 1.2, -PI / 2, PI / 2); g.stroke(); g.fillStyle = "#8A5A2B"; g.fillRect(-2.7, -1, 4.8, 0.9);
      for (let i = 0; i < 2; i++) { const p = new Path2D(), x = -1 + i * 1.8, k = sin(t * 3 + i); p.moveTo(x, -2); p.quadraticCurveTo(x + k, -3.4, x, -4.6); stroke(g, p, "rgba(140,130,170,0.7)", 0.4); }
    } else if (kind === "note") {
      const c = "#3A2FC4"; g.beginPath(); g.ellipse(-1.6, 2.2, 1.5, 1.1, -0.45, 0, 2 * PI); g.fillStyle = c; g.fill(); g.beginPath(); g.ellipse(2.4, 1.4, 1.5, 1.1, -0.45, 0, 2 * PI); g.fill();
      line(g, c, -0.3, 2, -0.3, -3.4, 0.5); line(g, c, 3.7, 1.2, 3.7, -4.2, 0.5); const b = new Path2D(); b.moveTo(-0.3, -3.4); b.lineTo(3.7, -4.2); b.lineTo(3.7, -3.2); b.lineTo(-0.3, -2.4); b.closePath(); g.fill(b);
    } else if (kind === "question") { g.fillStyle = "#5247E0"; g.font = "900 8px system-ui, sans-serif"; g.textAlign = "center"; g.textBaseline = "middle"; g.fillText("?", 0, 0.6); }
    else if (kind === "trophy") {
      g.fillStyle = vGrad(g, GOLD3, -3.4, 3); g.beginPath(); g.moveTo(-2.8, -3); g.lineTo(2.8, -3); g.quadraticCurveTo(2.6, 1, 0, 1.4); g.quadraticCurveTo(-2.6, 1, -2.8, -3); g.fill();
      g.fillRect(-0.6, 1.2, 1.2, 1.4); g.fillRect(-2, 2.5, 4, 1); g.strokeStyle = GOLD; g.lineWidth = 0.5; g.beginPath(); g.arc(-2.8, -1.6, 1.2, PI / 2, PI * 1.5); g.stroke(); g.beginPath(); g.arc(2.8, -1.6, 1.2, -PI / 2, PI / 2); g.stroke();
    } else if (kind === "pizza") {
      g.fillStyle = "#F7C35A"; g.beginPath(); g.moveTo(0, 3.6); g.lineTo(-3.6, -2.6); g.quadraticCurveTo(0, -4.2, 3.6, -2.6); g.closePath(); g.fill();
      g.fillStyle = "#C98A4B"; g.beginPath(); g.moveTo(-3.6, -2.6); g.quadraticCurveTo(0, -4.4, 3.6, -2.6); g.lineTo(3.2, -1.8); g.quadraticCurveTo(0, -3.2, -3.2, -1.8); g.closePath(); g.fill();
      for (const [x, y] of [[-1, -1], [1.2, -0.6], [0, 1.4]]) circle(g, "#E0475E", 0.8, x, y);
    }
  }

  // Toque com aura: um estouro de brilhos rápido.
  function drawBurst(g, burst) {
    if (burst <= 0) return;
    const u = 1 - burst;
    for (let i = 0; i < 8; i++) { const a = i * PI / 4, r = 30 + u * 26; star4(g, 50 + cos(a) * r, 62 + sin(a) * r * 0.7, 3 * burst, `rgba(255,224,122,${burst})`); }
  }

  // ---- cenário do dia
  function drawRelief(g, tm) {
    // "ufa": nuvenzinha de ar saindo da boca e gota de suor voando da testa
    const a = clamp((tm - 0.5) / 1.4, 0, 1);
    if (a > 0 && a < 1) for (let i = 0; i < 3; i++) {
      const u = clamp(a * 1.4 - i * 0.18, 0, 1); if (u <= 0 || u >= 1) continue;
      circle(g, `rgba(255,255,255,${0.85 * (1 - u)})`, 1.4 + u * 2.4 - i * 0.3, 44 - u * 16 - i * 2.5, 75 + u * 4 + i);
      circle(g, `rgba(170,165,215,${0.35 * (1 - u)})`, 1.4 + u * 2.4 - i * 0.3, 44 - u * 16 - i * 2.5, 75 + u * 4 + i + 0.4);
    }
    const d = clamp((tm - 0.9) / 0.8, 0, 1);
    if (d > 0 && d < 1) {
      const x = 74 + d * 12, y = 50 - sin(PI * d) * 7 + d * 6;
      const p = new Path2D(); p.moveTo(x, y - 2); p.quadraticCurveTo(x + 1.6, y + 0.4, x, y + 1.1); p.quadraticCurveTo(x - 1.6, y + 0.4, x, y - 2);
      g.fillStyle = `rgba(126,200,255,${1 - d * 0.6})`; g.fill(p);
    }
  }
  function drawSweat(g, t) {
    // sequência em risco: uma gotinha de suor de vez em quando
    const ph = (t % 4.5) / 4.5; if (ph > 0.55) return;
    const a = ph < 0.1 ? ph / 0.1 : 1 - Math.max(0, ph - 0.4) / 0.15, y = 51 + ph * 6;
    const p = new Path2D(); p.moveTo(80, y - 2.2); p.quadraticCurveTo(81.8, y + 0.4, 80, y + 1.2); p.quadraticCurveTo(78.2, y + 0.4, 80, y - 2.2);
    g.fillStyle = `rgba(126,200,255,${0.9 * a})`; g.fill(p); circle(g, `rgba(255,255,255,${0.8 * a})`, 0.35, 79.6, y - 0.1);
  }
  function drawMug(g, t) {
    // de manhã: a caneca de café do lado do livro, fumegando
    const x = 99, y = 85.5, w = 8.4, h = 9.2;
    g.save(); g.translate(x + w / 2, 95); g.scale(1, 0.22); circle(g, rgba(INK, 0.22), 6, 0, 0); g.restore();
    const hd = new Path2D(); hd.ellipse(x + w + 0.2, y + 4.4, 2.6, 2.8, 0, -PI / 2, PI / 2); stroke(g, hd, "#E9E5F5", 1.5); stroke(g, hd, rgba(INK, 0.25), 0.3);
    const body = new Path2D(); body.moveTo(x, y); body.lineTo(x + w, y); body.lineTo(x + w - 0.3, y + h - 1.6); body.quadraticCurveTo(x + w - 0.5, y + h, x + w - 2, y + h); body.lineTo(x + 2, y + h); body.quadraticCurveTo(x + 0.5, y + h, x + 0.3, y + h - 1.6); body.closePath();
    g.fillStyle = hGrad(g, ["#FFFFFF", "#F2EFFA", "#D9D4EE"], x, x + w); g.fill(body);
    g.save(); g.clip(body); g.fillStyle = "#3A2FC4"; g.fillRect(x, y + 3.6, w, 2.4); g.restore();
    oval(g, "#6B4226", x + 0.5, y - 0.7, w - 1, 1.8); oval(g, "rgba(255,230,200,0.35)", x + 1.6, y - 0.5, 2.4, 0.6);
    stroke(g, body, rgba(INK, 0.25), 0.3);
    for (let i = 0; i < 2; i++) {
      const ph = (t * 0.45 + i * 0.5) % 1, a = sin(PI * ph) * 0.55, sx = x + 2.6 + i * 3;
      const st = new Path2D(); st.moveTo(sx, y - 1.5 - ph * 3); st.bezierCurveTo(sx - 1.5, y - 3 - ph * 4, sx + 1.5, y - 5 - ph * 4, sx, y - 7 - ph * 5);
      stroke(g, st, `rgba(255,255,255,${a})`, 0.8); stroke(g, st, `rgba(120,110,170,${a * 0.35})`, 0.3);
    }
  }
  function drawMoon(g, t) {
    // à noite: lua minguante e duas estrelinhas piscando
    g.save();
    const cut = new Path2D(); cut.rect(-40, -40, 180, 180); cut.arc(13.6, 11.2, 4.6, 0, 2 * PI);
    g.clip(cut, "evenodd");
    g.beginPath(); g.arc(11, 13, 5, 0, 2 * PI); g.fillStyle = vGrad(g, ["#FFF4C8", "#FFD877"], 8, 18); g.fill();
    g.restore();
    star4(g, 21, 7, 1.3 + 0.5 * sin(t * 2.1), "rgba(255,230,150,0.9)");
    star4(g, 4, 22, 1 + 0.4 * sin(t * 1.7 + 2), "rgba(255,230,150,0.8)");
  }
  function drawFlames(g, t, small) {
    // em chamas: labaredas tremulando dos dois lados
    for (let i = 0; i < (small ? 2 : 4); i++) {
      const side = i % 2 ? 1 : -1, row = Math.floor(i / 2);
      const x = 50 + side * (44 + row * 6), base = 92 - row * 14, hgt = 9 + 2.5 * sin(t * 9 + i * 1.7) + (small ? 3 : 0), wd = 3.4 + (small ? 1 : 0);
      const p = new Path2D(); p.moveTo(x - wd, base); p.bezierCurveTo(x - wd * 1.1, base - hgt * 0.5, x - wd * 0.2 + sin(t * 7 + i) * 0.8, base - hgt * 0.7, x + sin(t * 6 + i) * 1.2, base - hgt);
      p.bezierCurveTo(x + wd * 0.6, base - hgt * 0.6, x + wd * 1.1, base - hgt * 0.45, x + wd, base); p.quadraticCurveTo(x, base + 1.8, x - wd, base);
      g.fillStyle = vGrad(g, ["#FFE07A", "#FF9F3A", "#F2552C"], base - hgt, base); g.fill(p);
      const c = new Path2D(); c.ellipse(x, base - hgt * 0.28, wd * 0.45, hgt * 0.26, 0, 0, 2 * PI); g.fillStyle = "rgba(255,248,200,0.85)"; g.fill(c);
    }
  }

  function star4(g, x, y, r, color) {
    const p = new Path2D(); p.moveTo(x, y - r); p.quadraticCurveTo(x, y, x + r, y); p.quadraticCurveTo(x, y, x, y + r); p.quadraticCurveTo(x, y, x - r, y); p.quadraticCurveTo(x, y, x, y - r);
    g.fillStyle = color; g.fill(p);
  }
  function drawSparkles(g, t, small) {
    for (let i = 0; i < (small ? 2 : 4); i++) {
      const ph = (t * 0.9 + i * 0.27) % 1, a = sin(PI * ph);
      const [x, y] = [[14, 30], [86, 26], [8, 52], [93, 50]][i];
      star4(g, x, y, (small ? 3.4 : 2.6) * a, rgba(GOLD, 0.95 * a));
    }
  }
  function drawZzz(g, t, small) {
    g.save(); g.font = "800 7px Nunito, system-ui, sans-serif"; g.textAlign = "center";
    for (let i = 0; i < (small ? 2 : 3); i++) {
      const ph = (t * 0.35 + i / 3) % 1, a = sin(PI * ph);
      g.globalAlpha = a; g.fillStyle = INK;
      g.save(); g.translate(76 + ph * 14 + sin(ph * 6) * 2, 34 - ph * 22); g.scale(0.6 + ph * 0.7, 0.6 + ph * 0.7); g.fillText("z", 0, 0); g.restore();
    }
    g.restore();
  }
  function drawDizzyStars(g, t) {
    for (let i = 0; i < 4; i++) { const a = t * 3.2 + i * PI / 2; star4(g, 50 + cos(a) * 22, 22 + sin(a) * 5, 2.4, i % 2 ? SPARK : MINT); }
  }
  function drawFocusMarks(g, t) {
    // rabisco aparecendo debaixo do lápis
    const k = (t * 0.45) % 1, n = Math.floor(k * 24);
    g.save(); g.strokeStyle = INK; g.globalAlpha = 0.55; g.lineWidth = 0.6; g.lineCap = "round"; g.beginPath();
    for (let i = 0; i <= n; i++) { const x = 60 + i * 0.95, y = 81.5 + sin(i * 1.7) * 0.7; i ? g.lineTo(x, y) : g.moveTo(x, y); }
    g.stroke(); g.restore();
  }
  function drawCelebration(g, t, small) {
    const colors = [SPARK, MINT, CHEEK];
    for (let i = 0; i < (small ? 3 : 6); i++) {
      const p = (t * 0.7 + i * 0.17) % 1, side = i % 2 === 0 ? -1 : 1;
      const x = 50 + side * (30 + i * 3), y = 70 - p * 50, rr = 2.6 * (1 - p) * 0.9;
      if (i % 3 === 2) { g.save(); g.translate(x, y); g.rotate(t * 4 + i); g.fillStyle = rgba(colors[i % 3], 1 - p); g.fillRect(-rr, -rr * 0.5, rr * 2, rr); g.restore(); }
      else circle(g, rgba(colors[i % 3], 1 - p), rr, x, y);
    }
  }

  // ---- braços e mãos
  function drawArms(g, s, which = "LR") {
    const P = s.P;
    const look = s.look || resolveLook(null);
    if (which.includes("L")) drawArm(g, [12, 66], [P.hL0, P.hL1], P.bendL, -1, P.shapeL, P.rotL, s.popL, s.small, look, s.t, s);
    if (which.includes("R")) drawArm(g, [88, 66], [P.hR0, P.hR1], P.bendR, 1, P.shapeR, P.rotR, s.popR, s.small, look, s.t);
  }
  function drawPencil(g, x, y, small) {
    // lápis amarelo inclinado, ponta encostando na página, atrás do punho
    g.save(); g.translate(x, y); g.rotate(0.5);
    const L = 12, R = small ? 1.3 : 1.05;
    g.fillStyle = "#F5B83D"; g.fillRect(-R, -L * 0.75, R * 2, L * 0.75);
    g.fillStyle = "#E39A1E"; g.fillRect(R * 0.2, -L * 0.75, R * 0.8, L * 0.75);
    g.fillStyle = "#C8CAD6"; g.fillRect(-R, -L * 0.75 - 1.4, R * 2, 1.4);
    g.fillStyle = "#F59AB8"; g.beginPath(); g.ellipse(0, -L * 0.75 - 2.2, R, 1.1, 0, 0, 2 * PI); g.fill();
    g.fillStyle = "#F0D3A8"; g.beginPath(); g.moveTo(-R, 0); g.lineTo(R, 0); g.lineTo(0, 3.1); g.closePath(); g.fill();
    g.fillStyle = INK; g.beginPath(); g.moveTo(-0.45, 2.1); g.lineTo(0.45, 2.1); g.lineTo(0, 3.2); g.closePath(); g.fill();
    g.restore();
  }
  function drawArm(g, sh, h, bend, side, shape, rot, pop, small, look, t, ctx) {
    const [armC, armL] = (look && look.arm) || [ARM, ARM_L], glove = (look && look.gloves) || GLOVES.branca;
    // braço de borracha que afina: curva do ombro à mão, com "cotovelo" (bend) e brilho em cima
    const mx = (sh[0] + h[0]) / 2, my = (sh[1] + h[1]) / 2, dx = h[0] - sh[0], dy = h[1] - sh[1], L = Math.hypot(dx, dy) || 1;
    const nx = -dy / L, ny = dx / L;
    const cx = mx + nx * bend * side + side * 1.5, cy = my + ny * bend * side + 2;
    const N = 14, w0 = small ? 2.3 : 1.9, w1 = small ? 1.7 : 1.3, L1 = [], R1 = [];
    for (let i = 0; i <= N; i++) {
      const u = i / N, a = (1 - u) * (1 - u), b = 2 * (1 - u) * u, c = u * u;
      const x = a * sh[0] + b * cx + c * h[0], y = a * sh[1] + b * cy + c * h[1];
      const tx = 2 * (1 - u) * (cx - sh[0]) + 2 * u * (h[0] - cx), ty = 2 * (1 - u) * (cy - sh[1]) + 2 * u * (h[1] - cy), tl = Math.hypot(tx, ty) || 1;
      const w = lerp(w0, w1, u);
      L1.push([x - ty / tl * w, y + tx / tl * w]); R1.push([x + ty / tl * w, y - tx / tl * w]);
    }
    const p = new Path2D(); p.moveTo(...L1[0]); for (const q of L1) p.lineTo(...q); for (let i = R1.length - 1; i >= 0; i--) p.lineTo(...R1[i]); p.closePath();
    g.fillStyle = armC; g.fill(p);
    const edgeSide = side < 0 ? R1 : L1, hl = new Path2D(); hl.moveTo(...edgeSide[1]); for (let i = 2; i < N - 1; i++) hl.lineTo(...edgeSide[i]);
    stroke(g, hl, rgba(armL, 0.7), 0.6);
    // mão: na direção do fim do braço, mais o giro do humor; "pop" quando troca de forma
    if (shape === "pen") drawPencil(g, h[0] - 2.4, h[1] + 3.2, small);
    const ang = Math.atan2(h[1] - cy, h[0] - cx) + rot * PI / 180;
    // o item da mão esquerda vai junto com a mão (some quando a mão abre para pegar a folha ou no susto)
    if (side < 0 && look && look.hand && look.hand !== "nenhum" && (shape === "mitt" || shape === "fist")) drawHeld(g, look.hand, h, ang - PI, t || 0, small, ctx && ctx.mood, ctx && ctx.tm, ctx && ctx.prevMood);
    g.save(); g.translate(h[0], h[1]); g.rotate(ang); if (side < 0) g.scale(1, -1);
    const sc = (small ? 1.35 : 1.25) * (1 + 0.28 * pop); g.scale(sc, sc);
    drawHand(g, shape, small, armC, glove);
    g.restore();
  }
  function drawHand(g, shape, small, armC, glove) {
    glove = glove || GLOVES.branca;
    const OUT = armC || ARM, ow = small ? 1.0 : 0.6, kind = glove[2];
    if (kind === "boxe") {   // luva de boxe: a mão vira a luva, em qualquer gesto
      const cuff = new Path2D(); cuff.roundRect(-2.8, -2.7, 2.6, 5.4, 1); g.fillStyle = "#FFFFFF"; g.fill(cuff); g.strokeStyle = "#8E1A22"; g.lineWidth = ow; g.stroke(cuff);
      line(g, "#E5373F", -1.5, -2.6, -1.5, 2.6, 0.5, false);
      const th = new Path2D(); th.ellipse(1.2, -3, 1.7, 1.3, -0.5, 0, 2 * PI);
      const gl = new Path2D(); gl.ellipse(2.6, 0.1, 4, 3.6, 0, 0, 2 * PI);
      const fillG = g.createRadialGradient(1.6, -1.4, 0.4, 2.6, 0, 4.4); fillG.addColorStop(0, "#FF7A80"); fillG.addColorStop(1, "#C3212B");
      for (const p of [gl, th]) { g.fillStyle = fillG; g.fill(p); g.strokeStyle = "#8E1A22"; g.lineWidth = ow; g.stroke(p); }
      g.beginPath(); g.ellipse(1.6, -1.5, 1.4, 0.8, -0.4, 0, 2 * PI); g.fillStyle = "rgba(255,255,255,0.55)"; g.fill();
      return;
    }
    let fill = glove[0];
    if (kind === "rainbow") { const rg = g.createLinearGradient(-1, -3, 5, 3); ["#FF8FA3", "#FFD84A", "#7EE0B8", "#6FB0FF", "#B995F5"].forEach((c, i) => rg.addColorStop(i / 4, c)); fill = rg; }
    let cuffC = glove[1];
    if (kind === "rgb") {   // luvas RGB: o degradê vai girando de cor devagar
      const h = (performance.now() / 1000 * 30) % 360, rg = g.createLinearGradient(-1, -3, 5, 3);
      for (let i = 0; i <= 2; i++) rg.addColorStop(i / 2, `hsl(${(h + i * 60) % 360},95%,62%)`);
      fill = rg; cuffC = `hsl(${(h + 180) % 360},80%,45%)`;
    } else if (kind === "latex" || kind === "couro") { const lg = g.createLinearGradient(0, -3, 0, 3); lg.addColorStop(0, lerpColor(glove[0], "#FFFFFF", 0.35)); lg.addColorStop(1, glove[0]); fill = lg; }
    // punho da luva e pulseira dourada
    g.beginPath(); g.ellipse(-1.2, 0, 1.6, 2.5, 0, 0, 2 * PI); g.fillStyle = cuffC; g.fill(); g.strokeStyle = OUT; g.lineWidth = ow; g.stroke();
    line(g, GOLD, -1.2, -2.2, -1.2, 2.2, 0.7, false);
    const outline = (path) => { g.fillStyle = fill; g.fill(path); g.strokeStyle = OUT; g.lineWidth = ow; g.lineJoin = "round"; g.stroke(path); };
    const finger = (x, y, a, len, r) => { const p = new Path2D(); p.moveTo(x, y); p.lineTo(x + cos(a) * len, y + sin(a) * len); g.lineCap = "round"; g.strokeStyle = OUT; g.lineWidth = r * 2 + ow * 2; g.stroke(p); g.strokeStyle = fill; g.lineWidth = r * 2; g.stroke(p); };
    if (shape === "open") {
      [-0.95, -0.35, 0.25, 0.85].forEach((a, i) => finger(2.2, 0, a * 0.8, 2.6 + (i === 1 || i === 2 ? 0.5 : 0), 0.95));
      finger(0.9, -1.8, -1.9, 1.9, 0.95);
      const palm = new Path2D(); palm.ellipse(1.6, 0, 2.6, 2.5, 0, 0, 2 * PI); outline(palm);
      if (!small) line(g, rgba(OUT, 0.25), 2.6, -0.8, 3.1, 0.8, 0.35);
    } else if (shape === "fist" || shape === "pen") {
      const th = new Path2D(); th.ellipse(1.2, -2.3, 1.5, 1.0, -0.4, 0, 2 * PI); outline(th);
      const f = new Path2D(); f.ellipse(1.7, 0, 3.1, 2.9, 0, 0, 2 * PI); outline(f);
      if (!small) { const k = new Path2D(); k.moveTo(3.4, -1.1); k.quadraticCurveTo(4.4, 0, 3.4, 1.1); stroke(g, k, rgba(OUT, 0.3), 0.4); }
    } else if (shape === "point") {
      finger(3.2, -0.6, 0, 4.2, 0.95);
      const th = new Path2D(); th.ellipse(1.4, -1.9, 1.4, 0.9, -0.3, 0, 2 * PI); outline(th);
      const f = new Path2D(); f.ellipse(1.6, 0.4, 2.8, 2.5, 0, 0, 2 * PI); outline(f);
    } else { // luva relaxada (a bolinha de sempre, com polegar)
      const th = new Path2D(); th.ellipse(1.0, -2.6, 1.2, 1.6, -0.6, 0, 2 * PI); outline(th);
      const palm = new Path2D(); palm.ellipse(1.7, 0, 3.1, 3.0, 0, 0, 2 * PI); outline(palm);
      if (!small) { const fl = new Path2D(); fl.moveTo(3.4, -1.2); fl.quadraticCurveTo(4.6, 0, 3.6, 1.4); stroke(g, fl, rgba(OUT, 0.3), 0.4); }
    }
    // brilho de luva (dá volume e deixa a cor da luva aparecer)
    if (!small && shape !== "open") { g.beginPath(); g.ellipse(1, -1.1, 1.2, 0.7, -0.4, 0, 2 * PI); g.fillStyle = "rgba(255,255,255,0.45)"; g.fill(); }
    if (!small) drawGloveDetail(g, shape, kind, OUT);
  }
  // Detalhes das luvas especiais, por cima da mão.
  function drawGloveDetail(g, shape, kind, OUT) {
    const closed = shape !== "open" && shape !== "point";
    if (kind === "tatica") {
      // protetor rígido no nó dos dedos (de borracha, com dois gomos) e o velcro no punho
      const kp = new Path2D(); kp.roundRect(2.6, -2.2, 1.6, 4.4, 0.8);
      if (shape !== "open") { g.fillStyle = vGrad(g, ["#5A5E66", "#24262C"], -2.2, 2.2); g.fill(kp); stroke(g, kp, rgba(OUT, 0.8), 0.3); line(g, "rgba(255,255,255,0.25)", 3.4, -1.6, 3.4, 1.6, 0.25); line(g, "rgba(0,0,0,0.4)", 2.7, 0, 4.1, 0, 0.25, false); }
      line(g, "rgba(0,0,0,0.35)", -2, -1.6, -2, 1.6, 0.5, false);
    } else if (kind === "latex") {
      // látex de procedimento: brilho comprido e a borda enrolada do punho
      if (closed) { const s = new Path2D(); s.moveTo(0.4, -2.2); s.quadraticCurveTo(2.6, -2.8, 4, -0.8); stroke(g, s, "rgba(255,255,255,0.75)", 0.35); }
      line(g, "rgba(255,255,255,0.6)", -2.5, -1.8, -2.5, 1.8, 0.4);
    } else if (kind === "gala") {
      // luva de gala: três pespontos no dorso e o botãozinho de pérola no punho
      if (closed) for (const y of [-0.9, 0, 0.9]) line(g, "rgba(150,140,190,0.5)", 0.6, y * 1.1, 2.4, y, 0.2);
      circle(g, "#E8E2F4", 0.55, -1.9, 1.5); circle(g, "#FFFFFF", 0.22, -2.05, 1.35);
    } else if (kind === "couro") {
      // luva de couro: costura pontilhada clara contornando o dorso
      if (closed) { g.save(); g.setLineDash([0.45, 0.35]); const s = new Path2D(); s.ellipse(1.7, 0, 2.3, 2.1, 0, -2.2, 2.2); stroke(g, s, "rgba(255,226,180,0.8)", 0.18); g.restore(); }
    } else if (kind === "giz") {
      // mãos de giz: pó branco grudado e uma nuvenzinha soltando
      for (const [x, y, r] of [[2.6, -1.4, 0.35], [3.6, 0.6, 0.28], [1.2, 1.6, 0.3], [4.2, -0.4, 0.22], [0.4, -1.8, 0.25]]) circle(g, "rgba(200,196,186,0.55)", r, x, y);
      const tt = performance.now() / 1000, k = (tt * 0.6) % 1;
      circle(g, `rgba(255,255,255,${0.5 * (1 - k)})`, 0.5 + k * 1.2, 4.6 + k * 1.5, -1.5 - k * 2);
    }
  }

  // ------------------------------------------------------------------ animação
  const ease = (x) => 1 - (1 - x) * (1 - x);
  const live = new Set();
  // Todos os Folhas na página (desenhando ou não). A cada meio segundo, quem está parado confere se
  // está na tela de verdade: o aviso do IntersectionObserver às vezes não volta (animação, rolagem
  // dentro de painel) e o Folha ficava em branco até um toque.
  const all = new Set();
  setInterval(() => {
    for (const el of all) {
      if (!el.isConnected) { all.delete(el); continue; }
      if (live.has(el) && el._visible) continue;
      const r = el.getBoundingClientRect();
      if (r.width > 0 && r.height > 0 && r.bottom > 0 && r.right > 0 && r.top < innerHeight && r.left < innerWidth && getComputedStyle(el).visibility !== "hidden") { el._visible = true; live.add(el); wake(); }
    }
  }, 500);
  let rafId = 0;
  // Um Folha que dá erro ao desenhar não pode parar os outros: antes o laço inteiro morria e todos
  // ficavam congelados (ou em branco) até alguém tocar num deles.
  function tick(now) { rafId = 0; for (const el of live) { try { el._frame(now); } catch (e) { live.delete(el); } } if (live.size) rafId = requestAnimationFrame(tick); }
  function wake() { if (!rafId && live.size) rafId = requestAnimationFrame(tick); }

  // Reações ao toque (cada toque, uma diferente) e quanto duram.
  const REACTIONS = ["giggle", "spin", "hop", "love", "wink"];
  // gestos que a amizade libera (FolhaBond.kt): dancinha (Parceiros), abraço (Amigos), olhos de estrela (Inseparáveis)
  const BOND_REACTIONS = [[2, "dance"], [3, "hug"], [5, "stars"]];
  const REACT_MS = { giggle: 900, spin: 750, hop: 950, love: 1300, wink: 800, dance: 1700, hug: 1500, stars: 1300 };
  const reactionsNow = () => REACTIONS.concat(BOND_REACTIONS.filter(([b]) => (LOOK.bond || 0) >= b).map(([, k]) => k));
  // O que ele imagina no balão de pensamento.
  const THOUGHTS = ["bulb", "heart", "star", "book", "coffee", "note", "question", "trophy", "pizza"];
  // Frases: tocar nele, fazer carinho e voltar para a aba. Curtas, no jeito dele.
  const TAP_LINES = [
    "Ei! Isso faz cócegas!", "Hehe, de novo!", "Tô aqui, firme e forte!", "Bora mais um tópico?", "Você tá indo bem, viu?",
    "Psiu… revisar amanhã fixa o dobro.", "Já bebeu água hoje?", "Cada questão é um passo pra posse!", "Eu acredito em você. Muito!",
    "Ai, minhas páginas!", "Folha, ao seu dispor!", "Um tópico de cada vez, combinado?", "Errar aqui é acertar na prova.",
    "Tô torcendo por você!", "Quer ver um giro? Toca de novo!", "Uma pausa curtinha também é estudo.", "Hoje a gente vence o edital um pouquinho.",
  ];
  const MORNING_LINES = ["Bom dia! Já tomou café?", "Cedinho assim? Orgulho!", "Manhã é a melhor hora pra memória."];
  const NIGHT_LINES = ["Tá tarde, hein? Mais um e cama!", "Estudo da noite também conta.", "Não esquece de dormir, tá? A memória agradece."];
  const PET_LINES = ["Ahh, que carinho bom!", "Hehe, faz de novo!", "Você é demais!", "Fiquei vermelhinho…", "Agora eu estudo até de cabeça pra baixo!"];
  const BACK_LINES = ["Voltou! Senti sua falta.", "Ei, que bom te ver!", "Bora continuar de onde paramos?", "Eu guardei a página pra você!"];
  // O site manda o banco de falas do app (FolhaFalas) por folhaSetLook; estas listas ficam para o site público.
  const fromLook = (list) => (list === TAP_LINES ? LOOK.tapLines : list === MORNING_LINES ? LOOK.tapMorning : list === NIGHT_LINES ? LOOK.tapNight : list === PET_LINES ? LOOK.petLines : list === BACK_LINES ? LOOK.backLines : null) || list;
  let lastLine = "";
  function pickLine(list) {
    const h = new Date().getHours();
    if (list === TAP_LINES && Math.random() < 0.25) list = h >= 5 && h < 11 ? MORNING_LINES : h >= 22 || h < 5 ? NIGHT_LINES : list;
    else if (list === TAP_LINES && LOOK.bondLines && LOOK.bondLines.length && Math.random() < 0.35) list = LOOK.bondLines;
    list = fromLook(list);
    // nunca a mesma fala duas vezes seguidas
    let line = list[Math.floor(Math.random() * list.length)];
    if (line === lastLine && list.length > 1) line = list[(list.indexOf(line) + 1) % list.length];
    lastLine = line; return line;
  }

  const MOODS = { idle: "idle", thinking: "thinking", talking: "talking", happy: "happy", sad: "sad", wave: "wave", point: "point", shh: "shh", aim: "aim", wink: "idle",
    surprised: "surprised", proud: "proud", focus: "focus", sleepy: "sleepy", dizzy: "dizzy", relieved: "relieved", ecstatic: "ecstatic", present: "present" };

  // Onde está o ponteiro e quando a pessoa mexeu pela última vez (para os olhos seguirem e para ele cochilar).
  const POINTER = { x: 0, y: 0, at: -1e9 };
  let lastActivity = performance.now();
  const touch = (e) => { lastActivity = performance.now(); if (e && e.clientX != null) { POINTER.x = e.clientX; POINTER.y = e.clientY; POINTER.at = lastActivity; } };
  ["pointermove", "pointerdown"].forEach((ev) => window.addEventListener(ev, touch, { passive: true }));
  ["keydown", "scroll", "wheel", "touchstart"].forEach((ev) => window.addEventListener(ev, () => { lastActivity = performance.now(); }, { passive: true }));
  const DOZE_AFTER = 40000;            // parado 40 s sem ninguém mexer: cochila
  const LEAVE_WAVE = 480, LEAVE_MS = 760;  // saída: tchau, depois some subindo

  // Fala: o mesmo ritmo do FolhaTalking (16 ms por letra, pausas em espaço, vírgula e ponto).
  function speechPlan(text) {
    const out = []; let at = 0;
    for (const ch of text) { out.push([at, ch]); at += /[.!?:]/.test(ch) ? 220 : ch === "," ? 110 : ch === " " ? 18 : 16; }
    return { steps: out, end: at };
  }
  function mouthFor(ch) {
    const c = (ch || "").toLowerCase();
    if (/[aáàâã]/.test(c)) return { open: 1, round: 0 };
    if (/[oóôõ]/.test(c)) return { open: 0.8, round: 0.8 };
    if (/[uú]/.test(c)) return { open: 0.45, round: 1 };
    if (/[eéêiíy]/.test(c)) return { open: 0.55, round: 0 };
    if (/[mbp]/.test(c)) return { open: 0, round: 0 };
    if (/[a-zç]/.test(c)) return { open: 0.28, round: 0.1 };
    if (c === " ") return { open: 0.12, round: 0 };
    return { open: 0, round: 0 };
  }

  class EstudarioFolha extends HTMLElement {
    static get observedAttributes() { return ["mood", "size", "say"]; }

    constructor() {
      super();
      this._start = performance.now(); this._last = 0;
      this._blink = 0; this._blinkPlan = []; this._nextBlink = this._start + 1800 + Math.random() * 2800;
      this._look = { x: 0, y: 0, fx: 0, fy: 0, tx: 0, ty: 0, from: 0, dur: 1, next: 0 };
      this._bounceAt = -1; this._wink = false; this._winkUntil = 0;
      this._visible = true;
      this._st = null; this._sq = { x: { s: 0 }, v: {} }; this._mood = null; this._shape = { L: "mitt", R: "mitt" }; this._pop = { L: 0, R: 0 };
      this._prevVy = 0;
    }

    connectedCallback() {
      if (!this._canvas) {
        this._canvas = cpuCanvas();
        this._canvas.style.display = "block";
        this.appendChild(this._canvas);
        this.setAttribute("role", "img");
        if (!this.hasAttribute("aria-label")) this.setAttribute("aria-label", "Folha, do Estudário");
        this.addEventListener("click", () => this.poke());
        // carinho: passar o mouse (ou o dedo) de um lado para o outro em cima dele
        this.addEventListener("pointermove", (e) => this._petMove(e));
        // chega pulando na tela (cai de leve, amassa ao pousar), a não ser que peça para entrar parado
        if (!reduceMotion.matches && !this.hasAttribute("noenter")) this._enterAt = performance.now();
      }
      this._size();
      if ("IntersectionObserver" in window) {
        this._io = new IntersectionObserver((entries) => {
          this._visible = entries.some((e) => e.isIntersecting);
          if (this._visible) { live.add(this); wake(); return; }
          live.delete(this);
          // "Fora da tela" às vezes chega no meio de uma animação (a reação da resposta crescendo) e o
          // aviso de volta não vem: confere de novo logo depois e, se estiver na tela, volta a desenhar.
          clearTimeout(this._recheck);
          this._recheck = setTimeout(() => {
            if (!this.isConnected || this._visible) return;
            const r = this.getBoundingClientRect();
            if (r.width > 0 && r.bottom > 0 && r.right > 0 && r.top < innerHeight && r.left < innerWidth) { this._visible = true; live.add(this); wake(); }
          }, 400);
        });
        this._io.observe(this);
      }
      live.add(this); all.add(this); wake();
    }
    disconnectedCallback() { live.delete(this); all.delete(this); if (this._io) this._io.disconnect(); if (this._quipEl) { this._quipEl.remove(); this._quipEl = null; } }
    attributeChangedCallback(name) {
      if (name === "size") this._size();
      if (name === "say") { const t = this.getAttribute("say"); this._say = t ? speechPlan(t) : null; this._sayAt = performance.now(); }
    }
    get mood() { return MOODS[this.getAttribute("mood") || "idle"] || "idle"; }

    /**
     * Toque: amassa e reage de um jeito diferente a cada vez (risadinha, giro, pulão com o chapéu voando,
     * coração ou piscadela) e, se for grande o bastante, solta uma frase.
     */
    poke() {
      const now = performance.now();
      if (!this.hasAttribute("quiet")) SOM.play("toque", 0.8);
      this._bounceAt = now;
      const all = reactionsNow();
      // o gesto mais novo da amizade aparece mais
      let k = all.length > REACTIONS.length && Math.random() < 0.35 ? all[all.length - 1] : all[Math.floor(Math.random() * all.length)];
      if (k === this._lastReact) k = all[(all.indexOf(k) + 1) % all.length];
      this._react = { kind: k, at: now }; this._lastReact = k;
      if (k === "wink") { this._wink = true; this._winkUntil = now + 110 + 600; }
      this._quip(pickLine(TAP_LINES), now);
      this._taps = (this._taps || []).filter((x) => now - x < 1400); this._taps.push(now);
      // quatro toques rápidos: ele fica tonto, com os passarinhos de desenho animado rodando a cabeça
      if (this._taps.length >= 4) { this._dizzyUntil = now + 2200; this._taps = []; if (!this.hasAttribute("quiet")) SOM.play("tonto"); }
      this._burstAt = now;
      live.add(this); wake();
    }

    /** Saída: acena tchau e sobe sumindo. Quem fecha a tela espera LEAVE_MS (veja folhaLeave). */
    leave() {
      if (reduceMotion.matches) return;
      this._leaveAt = performance.now(); live.add(this); wake();
    }

    /** Carinho: soma o quanto o ponteiro andou em cima dele; esfregando de um lado para o outro, ele derrete. */
    _petMove(e) {
      if (reduceMotion.matches) return;
      const now = performance.now(), last = this._pm;
      if (last && now - last.t < 160) {
        this._petAcc = (this._petAcc || 0) * Math.exp(-(now - last.t) / 600) + Math.hypot(e.clientX - last.x, e.clientY - last.y);
        const size = parseInt(this.getAttribute("size") || "96", 10);
        if (this._petAcc > size * 1.5) {
          if (!(this._petUntil > now)) this._petStart = now;
          this._petUntil = now + 700;
          const r = this.getBoundingClientRect(); this._petSide = e.clientX > r.left + r.width / 2 ? 1 : -1;
          live.add(this); wake();
        }
      } else this._petAcc = 0;
      this._pm = { x: e.clientX, y: e.clientY, t: now };
    }

    /** Uma fala curta num balão em cima dele, com a boca acompanhando. Só nos Folhas grandes e não mais que uma a cada 3,5 s. */
    _quip(text, now) {
      const size = parseInt(this.getAttribute("size") || "96", 10);
      if (!text || size < 80 || this.hasAttribute("quiet") || reduceMotion.matches || (this._quipAt && now - this._quipAt < 3500)) return;
      this._quipAt = now;
      // a tela já tem um balão de conversa com ele (FolhaTalking): a fala vai para lá
      const ev = new CustomEvent("folha-quip", { detail: text, bubbles: true, cancelable: true });
      if (!this.dispatchEvent(ev)) return;
      this._quipSay = speechPlan(text); this._quipUntil = now + this._quipSay.end + 500;
      const b = document.createElement("div");
      b.className = "folha-quip"; b.textContent = text; b.setAttribute("aria-live", "polite");
      // no corpo da página (fixo em cima dele), para nenhum cartão cortar o balão
      const rect = this.getBoundingClientRect();
      b.style.cssText = "position:fixed;left:" + (rect.left + rect.width / 2) + "px;bottom:" + (window.innerHeight - rect.top + 4) + "px;transform:translateX(-50%);z-index:4000;pointer-events:none;white-space:normal;width:max-content;max-width:" + Math.max(170, size * 1.5) + "px;" +
        "background:#fff;color:#26215C;font-family:inherit;font-weight:800;font-size:13px;line-height:1.3;padding:8px 12px;border-radius:16px;box-shadow:0 6px 18px rgba(20,14,70,.22);text-align:center";
      const tail = document.createElement("span");
      tail.style.cssText = "position:absolute;left:50%;top:100%;margin-left:-6px;border:6px solid transparent;border-top-color:#fff";
      b.appendChild(tail);
      if (this._quipEl) this._quipEl.remove();
      this._quipEl = b; document.body.appendChild(b);
      if (b.animate) b.animate([{ opacity: 0, transform: "translateX(-50%) translateY(6px) scale(.8)" }, { opacity: 1, transform: "translateX(-50%) translateY(0) scale(1)" }], { duration: 220, easing: "cubic-bezier(.2,1.4,.4,1)" });
      const ms = this._quipSay.end + 1600;
      setTimeout(() => {
        if (this._quipEl !== b) return;
        const done = () => { b.remove(); if (this._quipEl === b) this._quipEl = null; };
        if (b.animate) b.animate([{ opacity: 1 }, { opacity: 0, transform: "translateX(-50%) translateY(-6px)" }], { duration: 260 }).onfinish = done; else done();
      }, ms);
      live.add(this); wake();
    }

    /** Voltou para a aba depois de um tempo: ele dá um pulo e cumprimenta. */
    greet(now) { this._react = { kind: "hop", at: now }; this._quip(pickLine(BACK_LINES), now); live.add(this); wake(); }

    _size() {
      if (!this._canvas) return;
      const size = parseInt(this.getAttribute("size") || "96", 10);
      this._small = size < SMALL;
      this.style.width = size + "px"; this.style.height = size + "px";
      // O desenho passa da grade 100 x 100 (mãos para o alto, aceno, borla): o canvas sobra MARGIN% para cada
      // lado, fora da caixa do elemento, sem pegar cliques.
      const cs = getComputedStyle(this);
      if (cs.position === "static") this.style.position = "relative";
      // elemento novo nasce "inline" (largura e altura ignoradas): sem CSS da página, vira bloco em linha
      if (cs.display === "inline") this.style.display = "inline-block";
      const c = this._canvas.style, M = MARGIN;
      c.position = "absolute"; c.left = c.top = -M + "%"; c.width = c.height = 100 + 2 * M + "%"; c.pointerEvents = "none";
      const dpr = Math.min(window.devicePixelRatio || 1, 2.5), full = size * (1 + 2 * M / 100);
      this._canvas.width = Math.round(full * dpr); this._canvas.height = Math.round(full * dpr); this._px = full * dpr;
    }

    _updateBlink(now) {
      if (now >= this._nextBlink && !this._blinkPlan.length) {
        const twice = Math.random() < 0.2; let at = now;
        for (let i = 0; i < (twice ? 2 : 1); i++) { this._blinkPlan.push(at); at += 70 + 110 + 90; }
        this._nextBlink = at + 1800 + Math.random() * 2800;
      }
      this._blink = 0;
      this._blinkPlan = this._blinkPlan.filter((start) => {
        const d = now - start;
        if (d < 0) return true;
        if (d < 70) { this._blink = Math.max(this._blink, d / 70); return true; }
        if (d < 180) { this._blink = Math.max(this._blink, 1 - (d - 70) / 110); return true; }
        return false;
      });
    }
    _updateLook(now, mood) {
      const L = this._look;
      // ponteiro mexendo por perto: os olhos acompanham
      if (now - POINTER.at < 2500 && /^(idle|talking|wave|happy|point|proud|surprised)$/.test(mood)) {
        if (!this._rect || now - this._rectAt > 400) { this._rect = this.getBoundingClientRect(); this._rectAt = now; }
        const R = this._rect, cx = R.left + R.width / 2, cy = R.top + R.height * 0.6;
        const tx = clamp((POINTER.x - cx) / 260, -1, 1), ty = clamp((POINTER.y - cy) / 260, -1, 1);
        L.x += (tx - L.x) * 0.18; L.y += (ty - L.y) * 0.18; L.fx = L.x; L.fy = L.y; L.tx = L.x; L.ty = L.y; L.from = now; L.dur = 1; L.next = now + 900;
        return;
      }
      const p = clamp((now - L.from) / L.dur, 0, 1);
      L.x = L.fx + (L.tx - L.fx) * ease(p); L.y = L.fy + (L.ty - L.fy) * ease(p);
      if (p < 1 || now < L.next) return;
      L.fx = L.x; L.fy = L.y; L.from = now;
      if (mood === "thinking") { const toLeft = L.x > 0; L.tx = toLeft ? -1 : 1; L.ty = 0.6; L.dur = toLeft ? 260 : 900; L.next = now + L.dur; }
      else if (mood === "idle") { L.tx = Math.random() * 2 - 1; L.ty = Math.random() * 1.2 - 0.6; L.dur = 380; L.next = now + 380 + 900 + Math.random() * 1700; }
      else if (mood === "sad") { L.tx = (Math.random() - 0.5) * 0.6; L.ty = 0.9; L.dur = 700; L.next = now + 1800; }
      else { L.tx = 0; L.ty = 0; L.dur = 300; L.next = now + 1100; }
    }
    _bounce(now) {
      if (this._bounceAt < 0) return 0;
      const d = now - this._bounceAt;
      if (d < 110) return d / 110;
      const s = (d - 110) / 1000;
      if (s > 1.4) { this._bounceAt = -1; return 0; }
      return Math.exp(-s * 5.5) * cos(s * 17);
    }

    _fidget(now, mood) {
      if (mood !== "idle") { this._fid = null; this._nextFid = now + 5000 + Math.random() * 4000; return null; }
      if (!this._nextFid) this._nextFid = now + 6000 + Math.random() * 5000;
      if (!this._fid && now > this._nextFid) {
        const kinds = [["cap", 1700], ["yawn", 2300], ["glance", 1500], ["bounce", 1100], ["read", 2600], ["whistle", 2400], ["peek", 1800], ["hatflip", 1500]];
        let k = kinds[Math.floor(Math.random() * kinds.length)];
        if (this._lastFid === k[0]) k = kinds[(kinds.indexOf(k) + 1) % kinds.length];
        this._fid = { kind: k[0], start: now, dur: k[1], side: Math.random() < 0.5 ? -1 : 1 }; this._lastFid = k[0];
      }
      if (!this._fid) return null;
      const u = (now - this._fid.start) / this._fid.dur;
      if (u >= 1) { this._fid = null; this._nextFid = now + 7000 + Math.random() * 6000; return null; }
      return { kind: this._fid.kind, u, side: this._fid.side };
    }

    // Avança as molas até a pose do humor. Na troca de humor: antecipação (amassa) e "pop" nas mãos.
    _advance(mood, t, dt, fid, ctx) {
      const T = pose(mood, t, this._look, fid, ctx);
      const flat = { ...T, hL0: T.hL[0], hL1: T.hL[1], hR0: T.hR[0], hR1: T.hR[1] };
      delete flat.hL; delete flat.hR; delete flat.shapeL; delete flat.shapeR;
      if (!this._st) {
        this._st = { x: { ...flat }, v: {} }; this._mood = mood; this._shape = { L: T.shapeL, R: T.shapeR };
        this._enterPending = !!this._enterAt;
      }
      if (mood !== this._mood) {
        this._mood = mood;
        this._sq.v.s = (this._sq.v.s || 0) + 9;                                    // antecipação: agacha antes de mudar
        for (const k of ["hL1", "hR1"]) this._st.v[k] = (this._st.v[k] || 0) + 40;  // o braço volta antes de ir
      }
      for (const side of ["L", "R"]) {
        const want = T["shape" + side];
        if (want !== this._shape[side]) { this._shape[side] = want; this._pop[side] = 1; }
        this._pop[side] = Math.max(0, this._pop[side] - dt * 4);
      }
      if (dt <= 0) { this._st.x = { ...flat }; this._st.v = {}; return; }
      if (this._enterPending) { this._enterPending = false; this._st.x.hop = -16; this._st.x.hL1 -= 10; this._st.x.hR1 -= 10; }   // entrada: vem do alto
      const steps = Math.ceil(dt / (1 / 120)), h = dt / steps;
      const handK = mood === "thinking" ? 700 : mood === "focus" ? 600 : 120;
      for (let i = 0; i < steps; i++) for (const k in flat) springTo(this._st, k, flat[k], h, FAST.has(k) ? 420 : SLOW.has(k) ? handK : 200);
      // amasso de aterrissagem: vinha descendo rápido e chegou no chão
      const vy = this._st.v.hop || 0;
      if (this._prevVy > 18 && vy < this._prevVy * 0.4 && this._st.x.hop > -1) this._sq.v.s = (this._sq.v.s || 0) + this._prevVy * 0.12;
      this._prevVy = vy;
      for (let i = 0; i < steps; i++) springTo(this._sq, "s", 0, h, 260);
    }

    /**
     * A vida por cima da pose: reação ao toque, carinho, chapéu voando na mania "hatflip", assobio e o
     * balão de pensamento quando ele fica parado à toa. Mexe em P (o quadro) e devolve os extras do desenho.
     */
    _life(now, t, dt, mood, P) {
      const ex = {}, env = (u) => sin(PI * clamp(u, 0, 1)) ** 0.6;
      const mix = (k, v, w) => { P[k] = lerp(P[k], v, w); };
      // carinho: olhos fechadinhos de felicidade, inclina para o lado da mão, bochecha vermelha e coraçõezinhos
      const petting = this._petUntil > now;
      this._petW = (this._petW || 0) + ((petting ? 1 : 0) - (this._petW || 0)) * Math.min(1, dt * (petting ? 9 : 5));
      if (this._petW > 0.01) {
        const w = this._petW, sd = this._petSide || 1;
        mix("low", 0.78, w); mix("up", 0.1, w); mix("cheek", 1, w); mix("tilt", sd * 6 + sin(t * 9) * 1.2, w); mix("mW", 5.8, w); mix("mCurve", 4.8, w); mix("mOpen", 0.3, w);
        mix("hop", -0.8 + sin(t * 9) * 0.4, w); mix("browY", -1.2, w);
        ex.hearts = w;
        if (this._wasPet && !petting && w < 0.5) { this._wasPet = false; this._quip(pickLine(PET_LINES), now); }
        if (petting) this._wasPet = true;
      }
      // reação ao toque
      const r = this._react;
      if (r) {
        const dur = REACT_MS[r.kind] || 900, u = (now - r.at) / dur;
        if (u >= 1) this._react = null;
        else {
          const w = env(u);
          if (r.kind === "giggle") { mix("low", 0.8, w); mix("up", 0.1, w); mix("cheek", 1, w); mix("mOpen", 0.85, w); mix("mW", 6, w); mix("mCurve", 5, w); P.tilt += sin(u * 42) * 4 * w; P.hop += -abs(sin(u * 21)) * 1.5 * w; }
          else if (r.kind === "spin") { ex.turn = cos(2 * PI * outC(u)); P.hop += -sin(PI * u) * 6; mix("mOpen", 0.6, w); mix("pupil", 1.3, w); }
          else if (r.kind === "hop") {
            P.hop += -sin(PI * u) * 13; ex.hatUp = Math.max(0, sin(PI * clamp((u - 0.08) / 0.92, 0, 1))) * 9;
            mix("low", 0.6, w); mix("mOpen", 1, w); mix("mW", 6.2, w); mix("mCurve", 5, w); mix("pupil", 1.3, w);
            mix("hL0", 2, w); mix("hL1", 42, w); mix("hR0", 98, w); mix("hR1", 42, w); if (w > 0.4) { P.shapeL = "fist"; P.shapeR = "fist"; }
          } else if (r.kind === "dance") {
            // dancinha: pulinhos no ritmo, balança de um lado para o outro, braços alternando para o alto
            const beat = sin(u * PI * 6), side = beat > 0 ? 1 : -1;
            P.hop += -abs(beat) * 5 * w; P.tilt += beat * 7 * w; ex.whistle = w;
            mix("low", 0.6, w); mix("mOpen", 0.5, w); mix("mW", 5.8, w); mix("mCurve", 4.8, w); mix("cheek", 0.9, w);
            mix("hL0", side > 0 ? 4 : 10, w); mix("hL1", side > 0 ? 40 : 70, w); mix("hR0", side > 0 ? 90 : 96, w); mix("hR1", side > 0 ? 70 : 40, w);
            if (w > 0.4) { P.shapeL = side > 0 ? "fist" : "mitt"; P.shapeR = side > 0 ? "mitt" : "fist"; }
          } else if (r.kind === "hug") {
            // abraço: os braços cruzam na frente do livro, olhinhos fechados, bochecha vermelha e corações
            mix("hL0", 58, w); mix("hL1", 70, w); mix("hR0", 42, w); mix("hR1", 68, w); mix("rotL", -40, w); mix("rotR", 40, w); mix("bendL", 8, w); mix("bendR", 8, w);
            if (w > 0.3) { P.shapeL = "open"; P.shapeR = "open"; ex.armsFront = true; }
            mix("low", 0.8, w); mix("up", 0.1, w); mix("cheek", 1, w); mix("mW", 5.4, w); mix("mCurve", 4.6, w); mix("mOpen", 0.15, w);
            P.tilt += sin(u * PI * 4) * 2.5 * w; ex.hearts = Math.max(ex.hearts || 0, w);
          } else if (r.kind === "stars") {
            ex.starEyes = w > 0.25; ex.sparkle = w; P.hop += -sin(PI * u) * 8; mix("mOpen", 1, w); mix("mW", 6.4, w); mix("mCurve", 5.2, w); mix("cheek", 0.9, w);
          } else if (r.kind === "love") { mix("low", 0.78, w); mix("cheek", 1, w); mix("mW", 5.4, w); mix("mCurve", 4.6, w); mix("mOpen", 0.2, w); ex.hearts = Math.max(ex.hearts || 0, w); }
          else if (r.kind === "wink") { mix("low", 0.5, w); mix("mCurve", 4.4, w); mix("cheek", 0.8, w); }
        }
      }
      // Lendários: ao chegar na tela, corações dourados em volta dele
      if ((LOOK.bond || 0) >= 6 && now - this._start < 3000 && !this._small) { ex.hearts = Math.max(ex.hearts || 0, sin(PI * (now - this._start) / 3000)); ex.goldHearts = true; }
      // mania de jogar o chapéu: voa girando e volta para a cabeça
      if (this._fid && this._fid.kind === "hatflip") { const u = (now - this._fid.start) / this._fid.dur, k = sin(PI * clamp(u, 0, 1)); ex.hatUp = 20 * k; ex.hatSpin = 360 * clamp(u, 0, 1); }
      if (this._fid && this._fid.kind === "whistle") ex.whistle = env((now - this._fid.start) / this._fid.dur);
      // balão de pensamento: de vez em quando, parado e à toa, ele sonha acordado com alguma coisa
      const size = parseInt(this.getAttribute("size") || "96", 10), calm = mood === "idle" && !this._fid && !this._react && !petting && !(this._quipUntil > now);
      if (!this._nextThought) this._nextThought = now + 9000 + Math.random() * 12000;
      if (!this._thought && calm && size >= 90 && now > this._nextThought) {
        let k = THOUGHTS[Math.floor(Math.random() * THOUGHTS.length)]; if (k === this._lastThought) k = THOUGHTS[(THOUGHTS.indexOf(k) + 1) % THOUGHTS.length];
        this._thought = { kind: k, at: now }; this._lastThought = k;
      }
      if (this._thought) {
        const u = (now - this._thought.at) / 3600;
        if (u >= 1 || (mood !== "idle" && u < 0.88)) { this._thought = null; this._nextThought = now + 16000 + Math.random() * 14000; }
        else {
          const w = env(u); mix("lookX", 0.75, w); mix("lookY", -0.9, w); mix("mCurve", 3.8, w); mix("mW", 4.4, w); mix("browY", -0.8, w);
          ex.thought = { kind: this._thought.kind, u };
        }
      }
      return ex;
    }

    // Quadros por segundo: 60 em movimento; 30 quando ele está calmo e parado (respirando, piscando,
    // falando), 20 cochilando e quando há muitos Folhas pequenos na tela. Economiza bateria sem perder
    // fluidez onde ela aparece.
    _fpsCap(now) {
      if (!this._st || this._leaveAt || this._dizzyUntil > now || this._bounceAt >= 0 || this._react || this._petUntil > now || (this._petW || 0) > 0.02 || this._thought || this._quipUntil > now) return 0;
      const m = this._moodName, v = Math.abs(this._st.v.hop || 0) + Math.abs(this._st.v.hR1 || 0) + Math.abs(this._st.v.hL1 || 0);
      if (v > 14 || Math.abs(this._sq.v.s || 0) > 0.5 || now - POINTER.at < 1500) return 0;
      if (m === "sleepy") return 20;
      if (this._small && live.size > 3) return 20;
      if (m === "idle" || m === "focus" || m === "sad" || m === "talking" || m === "proud") return this._fid ? 0 : 30;
      return 0;
    }
    _frame(now) {
      if (!this._visible || !this._canvas) return;
      const cap = reduceMotion.matches ? 0 : this._fpsCap(now);
      if (cap && this._lastDraw && now - this._lastDraw < 1000 / cap - 4) return;
      this._lastDraw = now;
      const g = this._canvas.getContext("2d");
      if (!g) return;
      let mood = this.mood; const still = reduceMotion.matches;
      if (!still && this._dizzyUntil && now < this._dizzyUntil) mood = "dizzy";
      else if (!still && mood === "idle" && now - lastActivity > DOZE_AFTER) mood = "sleepy";
      if (this._wasAsleep && mood !== "sleepy") { this._bounceAt = now; this._lastFid = null; }   // acordou num susto
      this._wasAsleep = mood === "sleepy" && this.mood === "idle";
      // saída: tchau por um instante, depois sobe sumindo
      // A entrada conta do primeiro quadro desenhado, não de quando o elemento nasceu: sem quadros no
      // meio (aba escondida, fora da tela) ele ficava com o canvas quase transparente até um toque.
      if (this._enterAt && !this._entered) { this._enterAt = now; this._entered = true; }
      let fade = this._enterAt ? clamp((now - this._enterAt) / 160, 0, 1) : 1, rise = 0;
      if (this._leaveAt) {
        const el = now - this._leaveAt; mood = "wave";
        if (el > LEAVE_WAVE) { const u = clamp((el - LEAVE_WAVE) / (LEAVE_MS - LEAVE_WAVE), 0, 1); fade = 1 - u; rise = u * u * 30; }
      }
      this._canvas.style.opacity = fade < 1 ? String(fade) : "";
      this._canvas.style.transform = rise ? `translateY(${-rise}%)` : "";
      // falando a frase do balão (se ele estiver tranquilo)
      const quipping = !still && this._quipUntil > now && /^(idle|wave|happy|proud|point)$/.test(mood);
      if (quipping) mood = "talking";
      if (mood !== this._moodName) { this._prevMood = this._moodName; this._moodName = mood; this._moodAt = now; }
      const look = resolveLook(this);
      const t = still ? 0.35 : (now - this._start) / 1000;
      const dt = still || !this._last ? 0 : Math.min(0.05, (now - this._last) / 1000);
      this._last = now;
      if (!still) { this._updateBlink(now); this._updateLook(now, mood); }
      else { this._look.x = 0.3; this._look.y = -0.2; }
      if (this._wink && now > this._winkUntil) this._wink = false;
      const tm = still ? 9 : (now - this._moodAt) / 1000;
      this._advance(mood, t, dt, still ? null : this._fidget(now, mood), { tm, worried: look.atRisk, holding: look.hand && look.hand !== "nenhum" });
      // fala: a boca segue a letra atual do texto (com mola, para não tremer)
      let speech = null;
      const sayPlanNow = quipping ? this._quipSay : this._say, sayAt = quipping ? this._quipAt : this._sayAt;
      if (sayPlanNow && mood === "talking") {
        const el = now - sayAt, st = sayPlanNow.steps;
        let ch = "";
        if (el < sayPlanNow.end) { let lo = 0, hi = st.length - 1; while (lo < hi) { const m = (lo + hi + 1) >> 1; if (st[m][0] <= el) lo = m; else hi = m - 1; } ch = st[lo][1]; }
        const want = mouthFor(ch), k = dt ? 1 - Math.exp(-dt * 28) : 1;
        this._mouth = this._mouth || { open: 0, round: 0 };
        this._mouth.open += (want.open - this._mouth.open) * k; this._mouth.round += (want.round - this._mouth.round) * k;
        speech = this._mouth;
      }
      const winkAttr = this.getAttribute("mood") === "wink";
      g.setTransform(1, 0, 0, 1, 0, 0);
      g.clearRect(0, 0, this._canvas.width, this._canvas.height);
      const k = this._px / (100 + 2 * MARGIN);
      g.setTransform(k, 0, 0, k, MARGIN * k, MARGIN * k);
      const P = { ...this._st.x, shapeL: this._shape.L, shapeR: this._shape.R };
      const ex = still ? null : this._life(now, t, dt, mood, P);
      drawFolha(g, {
        t, mood: ex && ex.starEyes ? "ecstatic" : mood, small: this._small, ex,
        P,
        vy: this._st.v.hop || 0,
        blink: still ? 0 : this._blink,
        wink: this._wink || winkAttr ? 1 : 0,
        popL: this._pop.L, popR: this._pop.R,
        squash: Math.max(this._bounce(now), (this.gulp || 0) * 0.45) + clamp(this._sq.x.s * 0.1, -0.2, 0.25),
        gulp: still ? 0 : (this.gulp || 0),
        speech, look, tm, prevMood: this._prevMood,
        burst: this._burstAt ? clamp(1 - (now - this._burstAt) / 650, 0, 1) : 0,
      });
      if (still && this._bounceAt < 0) live.delete(this);
    }
  }

  customElements.define("estudario-folha", EstudarioFolha);

  // Cores de cada raridade (claro, forte): presente, raios e confete da cena de peça nova.
  const RARITY_GLOW = { comum: ["#BFD6FF", "#7FA6E8"], rara: ["#9CC6FF", "#3D8BEA"], epica: ["#D2B2FF", "#9563FF"], lendaria: ["#FFE59A", "#F5B83D"] };


  // ================================================================== cenas de comemoração
  // <estudario-cena tipo="..." [size="320"]>: o palco das comemorações, com o Folha (no visual escolhido) de
  // protagonista. Cada cena tem um roteiro com tempo marcado (T, em segundos) e depois fica viva em laço.
  // Fundo transparente, pensado para o palco escuro: quem mostra põe o fundo, os textos e os botões.
  //   nivel     nivel="12"                         o lápis mágico risca o nível antigo, vira a página do
  //                                                caderno e escreve o novo à mão; a página brilha e o Folha vibra.
  //   emblema   tier="3" categoria="seq" faixa="Ouro"   a medalha desce girando presa na fita, para de frente,
  //                                                é cunhada com o símbolo da categoria e ganha a faixa.
  //   sequencia dias="12" semana="S1,T1,Q0,Q1,S1,S1,D1"  o varal de lanternas da semana; o Folha lança uma
  //                                                faísca, acende a de hoje e a chama cresce com o número.
  //   peca      piece-slot="hat" piece="coroa" rarity="lendaria"  o presente cai na frente do Folha, se
  //                                                desamarra, a luz sai, dá a volta nele e ele aparece vestindo.
  // Grade de 100 x 110. Sem tamanho, ocupa a largura do pai. Pouco movimento: mostra o quadro final.
  const CENA_H = 110;
  // Qualidade da cena desenhando agora: 1 normal; abaixo de 1 no modo leve (aparelho lento), com menos
  // partículas, menos camadas de luz e sem sombras borradas. Cada cena mede a própria velocidade.
  let Q = 1;
  const segT = (T, a, b) => clamp((T - a) / (b - a), 0, 1);
  const outC = (x) => 1 - Math.pow(1 - clamp(x, 0, 1), 3);
  const outBack = (x) => { x = clamp(x, 0, 1); const c = 1.9; return 1 + (c + 1) * Math.pow(x - 1, 3) + c * Math.pow(x - 1, 2); };
  function seeded(seed) { let v = seed; return () => { v = (v * 1103515245 + 12345) & 0x7fffffff; return v / 0x7fffffff; }; }

  // ---- números escritos à mão: um traço só por algarismo (caixa de 10 x 16), para o lápis escrever de verdade
  const DIGIT_PATHS = {
    0: "M5,0.6 C1.4,0.6 0.6,5 0.6,8 C0.6,12 2,15.4 5,15.4 C8,15.4 9.4,12 9.4,8 C9.4,4 8,0.6 5,0.6 C4.2,0.6 3.5,1 3,1.6",
    1: "M2.2,4 C3.6,3 5,1.8 6.2,0.6 L6.2,15.4",
    2: "M1,4.2 C1.4,1.6 3.4,0.5 5.4,0.6 C8.2,0.7 9.6,3 8.8,5.8 C7.8,9.2 3.4,12 0.8,15.4 L9.6,15.4",
    3: "M1.2,2.6 C3,0 8.4,-0.2 8.8,3.6 C9,6.2 6.6,7.4 4.2,7.6 C7.4,7.6 9.6,9.4 9.4,12 C9.1,15.6 3.4,16.4 0.8,13.6",
    4: "M7.2,15.4 L7.2,0.6 L0.6,11 L9.8,11",
    5: "M8.8,0.6 L2.2,0.6 L1.4,7.2 C3.4,5.6 7.6,5.6 9,8.8 C10,12.2 7.6,15.6 4.6,15.6 C2.6,15.6 1.2,14.8 0.6,13.6",
    6: "M8.2,1.2 C4,-0.2 0.9,4 0.8,9.6 C0.7,13.6 2.6,15.6 5,15.6 C7.8,15.6 9.4,13.6 9.3,11 C9.2,8.4 7.3,7 5,7 C3,7 1.4,8.4 0.9,10",
    7: "M0.8,0.6 L9.4,0.6 C7,5 5.2,10 4,15.4",
    8: "M5,7.6 C2,7 1.2,5.6 1.4,4 C1.6,1.6 3.4,0.5 5,0.5 C6.6,0.5 8.4,1.6 8.6,4 C8.8,5.6 8,7 5,7.6 C1.8,8.4 0.8,10 0.8,12 C0.8,14.4 2.8,15.6 5,15.6 C7.2,15.6 9.2,14.4 9.2,12 C9.2,10 8.2,8.4 5,7.6",
    9: "M9,5.4 C8.6,7.8 7,9 5,9 C2.6,9 1,7.4 1,4.8 C1,2.2 2.8,0.5 5,0.5 C7.4,0.5 9.1,2.4 9.1,5.4 L9,9.6 C8.8,13.4 6.8,15.6 4,15.6 C2.6,15.6 1.6,15 1,14",
  };
  // vira uma lista de pontos com o comprimento acumulado (para desenhar até uma fração do traço)
  const DIGITS = {};
  for (const d in DIGIT_PATHS) {
    const tok = DIGIT_PATHS[d].match(/[MLC]|-?[\d.]+/g), pts = []; let i = 0, cur = null, cmd = "M";
    const num = () => parseFloat(tok[i++]);
    while (i < tok.length) {
      if (/[MLC]/.test(tok[i])) cmd = tok[i++];
      if (cmd === "M" || cmd === "L") { cur = [num(), num()]; pts.push(cur); }
      else { const a = cur, b = [num(), num()], c = [num(), num()], e = [num(), num()];
        for (let k = 1; k <= 14; k++) { const u = k / 14, v = 1 - u; pts.push([v * v * v * a[0] + 3 * v * v * u * b[0] + 3 * v * u * u * c[0] + u * u * u * e[0], v * v * v * a[1] + 3 * v * v * u * b[1] + 3 * v * u * u * c[1] + u * u * u * e[1]]); }
        cur = e; }
    }
    const len = [0]; for (let k = 1; k < pts.length; k++) len.push(len[k - 1] + Math.hypot(pts[k][0] - pts[k - 1][0], pts[k][1] - pts[k - 1][1]));
    DIGITS[d] = { pts, len, total: len[len.length - 1] };
  }
  // Escreve `str` com centro em (cx, cy) e altura h, até a fração `q` do traço total. Devolve a ponta do lápis.
  function handNumber(g, str, cx, cy, h, q, colors, width) {
    const k = h / 16, gap = 3, W = str.length * 10 + (str.length - 1) * gap, x0 = cx - W * k / 2, y0 = cy - h / 2;
    const total = [...str].reduce((a, ch) => a + DIGITS[ch].total, 0);
    let left = q * total, tip = null;
    [...str].forEach((ch, n) => {
      const D = DIGITS[ch], ox = x0 + n * (10 + gap) * k; if (left <= 0) return;
      const upto = Math.min(left, D.total); left -= D.total;
      const p = new Path2D(); p.moveTo(ox + D.pts[0][0] * k, y0 + D.pts[0][1] * k);
      for (let j = 1; j < D.pts.length; j++) {
        if (D.len[j] <= upto) { p.lineTo(ox + D.pts[j][0] * k, y0 + D.pts[j][1] * k); tip = [ox + D.pts[j][0] * k, y0 + D.pts[j][1] * k]; continue; }
        const f = (upto - D.len[j - 1]) / (D.len[j] - D.len[j - 1]), x = ox + lerp(D.pts[j - 1][0], D.pts[j][0], f) * k, y = y0 + lerp(D.pts[j - 1][1], D.pts[j][1], f) * k;
        p.lineTo(x, y); tip = [x, y]; break;
      }
      colors.forEach((c, i) => stroke(g, p, c, width * (1 - i * 0.42)));
    });
    return tip || [x0, y0 + h * 0.3];
  }

  // ---- símbolos das categorias de emblema (caixa de -10 a 10), em traço grosso
  function badgeGlyph(g, cat, color) {
    const p = new Path2D(), F = new Path2D();
    if (cat === "seq") { F.moveTo(0, 9.5); F.bezierCurveTo(-6.5, 9.5, -7.5, 3, -4.2, -1.2); F.bezierCurveTo(-3.6, 1, -2.4, 2.2, -1.2, 2.2); F.bezierCurveTo(-2.4, -3, 0, -7.6, 3.4, -9.6); F.bezierCurveTo(2.4, -5.4, 7.6, -2.4, 6.6, 3.2); F.bezierCurveTo(6.2, 7, 3.8, 9.5, 0, 9.5); F.closePath(); }
    else if (cat === "meta") { p.moveTo(-5.5, 9.5); p.lineTo(-5.5, -9); F.moveTo(-5.5, -8.6); F.bezierCurveTo(-1, -10.6, 2.4, -6, 7.6, -8.2); F.lineTo(7.6, 0.4); F.bezierCurveTo(2.4, 2.6, -1, -2, -5.5, 0); F.closePath(); }
    else if (cat === "plano") { p.moveTo(-8, -5); p.lineTo(8, -5); p.lineTo(8, 8.5); p.lineTo(-8, 8.5); p.closePath(); p.moveTo(-8, -1); p.lineTo(8, -1); p.moveTo(-4, -8.5); p.lineTo(-4, -3); p.moveTo(4, -8.5); p.lineTo(4, -3); p.moveTo(-3.6, 3.6); p.lineTo(-1, 6); p.lineTo(4, 1.2); }
    else if (cat === "quest") { for (const y of [-6, 0, 6]) { p.moveTo(-1.6, y); p.lineTo(8, y); } p.moveTo(-8.4, -6); p.lineTo(-6.6, -4.2); p.lineTo(-3.6, -8.2); p.moveTo(-8.4, 0); p.lineTo(-6.6, 1.8); p.lineTo(-3.6, -2.2); F.arc(-6, 6, 1.6, 0, 2 * PI); }
    else if (cat === "acerto") { p.arc(-1, 1, 8, 0, 2 * PI); p.moveTo(3.6, 1); p.arc(-1, 1, 4.6, 0, 2 * PI); F.arc(-1, 1, 1.8, 0, 2 * PI); p.moveTo(-1, 1); p.lineTo(8.4, -8.4); p.moveTo(8.4, -8.4); p.lineTo(5, -8.6); p.moveTo(8.4, -8.4); p.lineTo(8.6, -5); }
    else if (cat === "topico") { p.moveTo(0, -5); p.bezierCurveTo(-3, -7.4, -7, -7.4, -9.4, -6.2); p.lineTo(-9.4, 7.4); p.bezierCurveTo(-7, 6.2, -3, 6.2, 0, 8.4); p.bezierCurveTo(3, 6.2, 7, 6.2, 9.4, 7.4); p.lineTo(9.4, -6.2); p.bezierCurveTo(7, -7.4, 3, -7.4, 0, -5); p.lineTo(0, 8.4); }
    else if (cat === "edital") { p.moveTo(-6.6, -9); p.lineTo(3, -9); p.lineTo(7, -5); p.lineTo(7, 9); p.lineTo(-6.6, 9); p.closePath(); p.moveTo(3, -9); p.lineTo(3, -5); p.lineTo(7, -5); for (const y of [-2, 2, 6]) { p.moveTo(-3.6, y); p.lineTo(4, y); } }
    else if (cat === "revisao") { p.arc(0, 0, 7.6, -2.6, 0.2); p.moveTo(7.4, 2.4); p.lineTo(7.6, -2.4); p.moveTo(7.6, 1.4); p.lineTo(4, -0.2); p.arc(0, 0, 7.6, 0.54, 3.34); p.moveTo(-7.4, -2.4); p.lineTo(-7.6, 2.4); p.moveTo(-7.6, -1.4); p.lineTo(-4, 0.2); }
    else if (cat === "simulado") { p.moveTo(-7, -6.5); p.lineTo(7, -6.5); p.lineTo(7, 9.4); p.lineTo(-7, 9.4); p.closePath(); F.moveTo(-3.6, -9.6); F.lineTo(3.6, -9.6); F.lineTo(3.6, -4.4); F.lineTo(-3.6, -4.4); F.closePath(); p.moveTo(-3.6, 2.4); p.lineTo(-0.8, 5.2); p.lineTo(4, -0.6); }
    else if (cat === "discursiva") { F.moveTo(7.8, -9.4); F.bezierCurveTo(1, -7.6, -5.6, -1, -6.6, 6.6); F.lineTo(-4.6, 4.8); F.bezierCurveTo(1.4, 3.6, 6.6, -2.6, 7.8, -9.4); F.closePath(); p.moveTo(-6.6, 6.6); p.lineTo(-8.6, 9.4); p.moveTo(-8.6, 9.6); p.lineTo(8, 9.6); }
    else { p.arc(0, 1.4, 7.8, 0, 2 * PI); p.moveTo(0, 1.4); p.lineTo(0, -3.4); p.moveTo(0, 1.4); p.lineTo(3.6, 3.4); F.moveTo(-2.4, -9.6); F.lineTo(2.4, -9.6); F.lineTo(2.4, -7.2); F.lineTo(-2.4, -7.2); F.closePath(); p.moveTo(6.6, -5); p.lineTo(8.2, -6.6); }
    g.lineCap = "round"; g.lineJoin = "round"; g.strokeStyle = color; g.fillStyle = color; g.lineWidth = 2.2;
    g.stroke(p); g.fill(F); g.lineWidth = 1; g.stroke(F);
  }

  // ---- peças comuns de cena
  function glowAt(g, x, y, r, color, a) {
    if (a <= 0) return;
    const gr = g.createRadialGradient(x, y, 0, x, y, r); gr.addColorStop(0, rgba(color, a)); gr.addColorStop(1, rgba(color, 0));
    g.fillStyle = gr; g.beginPath(); g.arc(x, y, r, 0, 2 * PI); g.fill();
  }
  /**
   * Fundo luminoso (raios, brilho, ondas de choque, bolinhas de luz) desenhado numa tela à parte e
   * apagado suave numa elipse em volta de (cx, cy): a luz some no escuro antes de chegar em qualquer
   * borda do quadro, sem o "quadrado" cortando os raios.
   */
  function backdrop(g, el, cx, cy, rx, ry, paint) {
    // A luz do fundo é toda macia: pintada em meia resolução e ampliada, custa um quarto dos pixels por quadro
    // (era a parte mais pesada da cena e o que travava em tablet e celular mais simples).
    const c = el._bgc || (el._bgc = el.hasAttribute("gpu") ? document.createElement("canvas") : cpuCanvas()), S = 0.5;
    const bw = Math.ceil(g.canvas.width * S), bh = Math.ceil(g.canvas.height * S);
    if (c.width !== bw || c.height !== bh) { c.width = bw; c.height = bh; }
    const b = c.getContext("2d"), M = g.getTransform();
    b.setTransform(1, 0, 0, 1, 0, 0); b.clearRect(0, 0, c.width, c.height); b.setTransform(M.a * S, M.b * S, M.c * S, M.d * S, M.e * S, M.f * S);
    paint(b);
    b.save(); b.globalCompositeOperation = "destination-in"; b.translate(cx, cy); b.scale(1, ry / rx);
    const m = b.createRadialGradient(0, 0, 0, 0, 0, rx);
    m.addColorStop(0, "#000"); m.addColorStop(0.45, "rgba(0,0,0,0.92)"); m.addColorStop(0.75, "rgba(0,0,0,0.45)"); m.addColorStop(1, "rgba(0,0,0,0)");
    b.fillStyle = m; b.fillRect(-rx * 1.2, -rx * 1.2, rx * 2.4, rx * 2.4); b.restore();
    g.save(); g.setTransform(1, 0, 0, 1, 0, 0); g.drawImage(c, 0, 0, g.canvas.width, g.canvas.height); g.restore();
  }
  // bolinhas de luz desfocadas, flutuando devagar (dão profundidade ao palco)
  function bokeh(g, t, cx, cy, color, a, n = 10) {
    for (let i = 0, m = Math.round(n * Q); i < m; i++) {
      const ang = i * 2.39996 + t * 0.05 * (i % 2 ? 1 : -1), r = 14 + ((i * 37) % 40), x = cx + cos(ang) * r * 1.3, y = cy + sin(ang) * r * 0.8 + sin(t * 0.6 + i) * 2;
      glowAt(g, x, y, 2.5 + (i % 4) * 1.6, color, a * (0.5 + 0.5 * sin(t * 0.9 + i * 1.3)));
    }
  }
  function floorLight(g, x, y, r, color, a) { g.save(); g.translate(x, y); g.scale(1, 0.2); glowAt(g, 0, 0, r, color, a); g.restore(); }
  function sceneRays(g, x, y, n, len, color, a, rot, wide = 0.11) {
    if (a <= 0) return;
    g.save(); g.translate(x, y); g.rotate(rot);
    for (let i = 0; i < n; i++) {
      g.rotate(2 * PI / n);
      const p = new Path2D(); p.moveTo(0, 0); p.lineTo(-len * wide, -len); p.quadraticCurveTo(0, -len * 1.04, len * wide, -len); p.closePath();
      const gr = g.createLinearGradient(0, 0, 0, -len); gr.addColorStop(0, rgba(color, 0.5 * a)); gr.addColorStop(1, rgba(color, 0)); g.fillStyle = gr; g.fill(p);
    }
    g.restore();
  }
  function shockRing(g, x, y, u, color, maxR, sy = 1) {
    if (u <= 0 || u >= 1) return;
    g.beginPath(); g.ellipse(x, y, maxR * outC(u), maxR * outC(u) * sy, 0, 0, 2 * PI);
    g.strokeStyle = rgba(color, 0.9 * (1 - u)); g.lineWidth = 0.4 + 2.6 * (1 - u); g.stroke();
  }
  // confete saindo de dois canhões nos cantos de baixo, cruzando a cena por cima sem tapar o centro
  function makeConfetti(seed, n, colors) {
    const r = seeded(seed);
    return Array.from({ length: n }, (_, i) => { const side = i % 2 ? 1 : -1; return { side, a: -PI / 2 - side * (0.12 + r() * 0.42), v: 100 + r() * 80, c: colors[i % colors.length], s: 0.8 + r() * 1.3, r: r() * 6, k: i % 3, spin: 4 + r() * 8 }; });
  }
  function drawConfetti(g, list, dt, life = 3) {
    if (dt <= 0 || dt >= life) return;
    for (const c of list) {
      const ox = c.side < 0 ? 2 : 98, oy = 106;
      const drag = 1 - Math.exp(-dt * 2.2), x = ox + Math.cos(c.a) * c.v * drag / 2.2, y = oy + Math.sin(c.a) * c.v * drag / 2.2 + 9 * dt * dt;
      g.save(); g.translate(x + sin(dt * 3 + c.r) * 2, y); g.rotate(c.r + dt * c.spin); g.globalAlpha = clamp((life - dt) / 0.8, 0, 1);
      if (c.k === 0) { g.scale(cos(dt * c.spin + c.r), 1); g.fillStyle = c.c; g.fillRect(-c.s, -c.s * 0.5, c.s * 2, c.s); }
      else if (c.k === 1) circle(g, c.c, c.s * 0.55, 0, 0); else star4(g, 0, 0, c.s * 1.3, c.c);
      g.restore();
    }
  }
  // poeira de estrelas no fundo (piscando, bem de leve)
  const DUST = (() => { const r = seeded(11); return Array.from({ length: 26 }, () => ({ x: r() * 100, y: r() * 92, s: 0.3 + r() * 0.7, p: r() * 6 })); })();
  function starDust(g, t, a) { for (const d of DUST) { const k = 0.5 + 0.5 * sin(t * 1.4 + d.p); circle(g, `rgba(255,255,255,${0.5 * k * a})`, d.s * (0.6 + 0.4 * k), d.x, d.y); } }

  // O Folha numa cena: o roteiro de humores ([T, humor], ...) vira pose, com 0,3 s de transição entre eles.
  function blendPose(A, B, w) {
    const o = {};
    for (const k in A) { const a = A[k], b = B[k]; o[k] = typeof a === "number" && typeof b === "number" ? lerp(b, a, w) : Array.isArray(a) && Array.isArray(b) ? [lerp(b[0], a[0], w), lerp(b[1], a[1], w)] : w > 0.5 ? a : b; }
    return o;
  }
  function sceneFolha(g, o) {
    const sc = o.script; let i = 0; while (i + 1 < sc.length && o.T >= sc[i + 1][0]) i++;
    const cur = sc[i][1], prev = i > 0 ? sc[i - 1][1] : cur, w = i > 0 ? sstep((o.T - sc[i][0]) / 0.3) : 1;
    const ctx = { tm: o.T - sc[i][0], holding: o.look.hand && o.look.hand !== "nenhum" };
    let P = pose(cur, o.t, o.dir, null, ctx);
    if (w < 1) P = blendPose(P, pose(prev, o.t, o.dir, null, { ...ctx, tm: 9 }), w);
    if (o.edit) o.edit(P);   // a cena pode ajustar a pose (a mão do martelo, a cara de concentrado)
    g.save(); g.translate(o.x - 50 * o.scale, o.y - 95 * o.scale); g.scale(o.scale, o.scale);
    drawFolha(g, { t: o.t, mood: w < 0.5 ? prev : cur, small: false, P: { ...P, hL0: P.hL[0], hL1: P.hL[1], hR0: P.hR[0], hR1: P.hR[1] }, vy: o.vy || 0,
      blink: o.blink != null ? o.blink : (o.t % 3.4) < 0.12 ? 1 : 0, wink: 0, popL: 0, popR: 0, squash: o.squash || 0, gulp: 0, look: o.look, tm: ctx.tm, burst: o.burst || 0, ex: o.ex || undefined });
    g.restore();
    return P;
  }
  // um ponto da grade do Folha levado para a cena
  const folhaPt = (o, P, x, y) => [o.x + (x - 50) * o.scale, o.y + (y - 95 + P.hop) * o.scale];

  // ---- cena: nível (a ascensão)
  // 0–1,2 s  orbes de XP vêm de fora em espiral e enchem o anel de energia em volta do nível antigo;
  // 1,2–1,6  o anel completo acelera e brilha, o emblema antigo racha;
  // 1,6      explode em cacos: clarão, coluna de luz, tremor, e o número novo cai em metal dourado no brasão;
  // 1,9–2,6  as asas douradas abrem pena por pena, o anel de runas gira e a faixa "NÍVEL" sobe;
  // depois   tudo vivo: brilho correndo no ouro, asas respirando, fagulhas subindo, o Folha comemorando.
  const LVL = { fillEnd: 1.2, crack: 1.25, boom: 1.6, wings: 1.9, banner: 2.25 };
  const CX = 50, CY = 35, CR = 15;   // centro e raio do brasão
  function goldText(g, txt, x, y, size, k, t, shine) {
    // número em metal: sombra funda, contorno escuro, ouro em degradê, chanfro claro em cima e o brilho correndo
    g.save(); g.font = `900 ${size}px Nunito, "Segoe UI", system-ui, sans-serif`; g.textAlign = "center"; g.textBaseline = "middle";
    g.lineJoin = "round";
    g.fillStyle = "rgba(20,8,40,0.55)"; g.fillText(txt, x + size * 0.03, y + size * 0.07);
    g.strokeStyle = "#5A3803"; g.lineWidth = size * 0.16; g.strokeText(txt, x, y);
    const gr = g.createLinearGradient(0, y - size * 0.5, 0, y + size * 0.5);
    gr.addColorStop(0, "#FFF6D0"); gr.addColorStop(0.35, "#FFD66B"); gr.addColorStop(0.55, "#F5B83D"); gr.addColorStop(0.56, "#E09A1E"); gr.addColorStop(1, "#B07510");
    g.fillStyle = gr; g.fillText(txt, x, y);
    g.strokeStyle = "rgba(255,255,255,0.55)"; g.lineWidth = size * 0.03; g.strokeText(txt, x - size * 0.01, y - size * 0.02);
    if (shine >= 0 && shine <= 1) {
      // o brilho: uma faixa clara atravessando, só onde tem ouro
      const w = g.measureText(txt).width, sx = x - w * 0.8 + shine * w * 1.6;
      g.globalCompositeOperation = "source-atop";
      const sg = g.createLinearGradient(sx - size * 0.3, 0, sx + size * 0.3, 0); sg.addColorStop(0, "rgba(255,255,255,0)"); sg.addColorStop(0.5, "rgba(255,255,255,0.9)"); sg.addColorStop(1, "rgba(255,255,255,0)");
      g.fillStyle = sg; g.save(); g.translate(sx, y); g.rotate(0.35); g.fillRect(-size * 0.3, -size, size * 0.6, size * 2); g.restore();
    }
    g.restore();
  }
  // uma asa (lado -1 esquerda, 1 direita) com 6 penas que abrem em leque; open 0 fechada, 1 aberta
  function drawWing(g, side, open, t) {
    const rootX = CX + side * (CR * 0.82), rootY = CY + 2;
    const breathe = sin(t * 2.2) * 2.5 * open;
    for (let i = 5; i >= 0; i--) {
      const len = 13 + i * 3.2, ang = (-12 - i * 13) * open + (1 - open) * (10 - i * 2) + breathe * (i / 5);
      g.save(); g.translate(rootX, rootY); g.scale(side, 1); g.rotate(ang * PI / 180);
      const f = new Path2D(); f.moveTo(0, -2.2); f.bezierCurveTo(len * 0.4, -4.6, len * 0.85, -3.8, len, -0.6); f.bezierCurveTo(len * 0.86, 1.6, len * 0.4, 2.6, 0, 2.2); f.closePath();
      const gr = g.createLinearGradient(0, 0, len, 0); gr.addColorStop(0, "#C98A12"); gr.addColorStop(0.55, "#FFD66B"); gr.addColorStop(1, "#FFF6D0");
      g.fillStyle = gr; g.fill(f); g.strokeStyle = "rgba(90,56,3,0.75)"; g.lineWidth = 0.4; g.stroke(f);
      line(g, "rgba(255,255,255,0.55)", 1.5, -0.6, len * 0.8, -1.2, 0.35);
      g.restore();
    }
  }
  function drawCrest(g, t, lvl, k, shine) {
    // brasão: anel dourado serrilhado, disco índigo fundo com estrelinhas, anel interno e o número
    const rim = new Path2D(); for (let i = 0; i <= 64; i++) { const a = i * PI / 32, r = i % 2 ? CR : CR - 0.8; i ? rim.lineTo(CX + cos(a) * r, CY + sin(a) * r) : rim.moveTo(CX + cos(a) * r, CY + sin(a) * r); }
    const rg = g.createLinearGradient(CX - CR, CY - CR, CX + CR, CY + CR); rg.addColorStop(0, "#FFF1BF"); rg.addColorStop(0.45, "#F5B83D"); rg.addColorStop(1, "#9A6206");
    g.fillStyle = rg; g.fill(rim);
    const disc = g.createRadialGradient(CX - 4, CY - 6, 1, CX, CY, CR * 0.84); disc.addColorStop(0, "#5247E0"); disc.addColorStop(0.7, "#2A209E"); disc.addColorStop(1, "#160F5E");
    circle(g, disc, CR * 0.84, CX, CY);
    for (let i = 0; i < 9; i++) { const a = i * 0.7 + t * 0.2, r = CR * (0.3 + (i % 3) * 0.17); circle(g, `rgba(255,255,255,${0.25 + 0.25 * sin(t * 2 + i)})`, 0.35, CX + cos(a) * r, CY + sin(a) * r); }
    const ir = new Path2D(); ir.arc(CX, CY, CR * 0.74, 0, 2 * PI); stroke(g, ir, "rgba(255,214,107,0.65)", 0.45);
    const hl = new Path2D(); hl.arc(CX, CY, CR * 0.93, PI * 1.08, PI * 1.5); stroke(g, hl, "rgba(255,255,255,0.75)", 0.8);
    goldText(g, lvl, CX, CY + 1, lvl.length > 2 ? 12 : lvl.length > 1 ? 15 : 18, k, t, shine);
  }
  function cenaNivel(g, T, t, el) {
    const lvl = String(Math.max(1, parseInt(el.getAttribute("nivel") || "2", 10))), old = String(Math.max(1, +lvl - 1));
    const look = el._look(), k = this_k(el);
    const boom = segT(T, LVL.boom, LVL.boom + 0.6), after = T >= LVL.boom;
    // tremor de câmera no impacto
    const shake = T > LVL.boom && T < LVL.boom + 0.45 ? (1 - (T - LVL.boom) / 0.45) * 1.6 : 0;
    g.save(); g.translate(sin(t * 91) * shake, cos(t * 77) * shake);
    // ---- fundo: brilho, coluna de luz, raios e anel de runas (tudo some suave antes da borda)
    const reach = el._reach(), fill = outC(segT(T, 0.15, LVL.fillEnd));
    backdrop(g, el, CX, CY + 6, reach, reach * 0.95, (b) => {
      glowAt(b, CX, CY, 58, after ? "#F5B83D" : "#6158F0", after ? 0.3 + 0.15 * sin(t * 2) : 0.25 + 0.3 * fill);
      bokeh(b, t, CX, CY + 8, after ? "#FFE59A" : "#B9B2FF", 0.35, 12);
      // coluna de luz do impacto
      if (after) { const a = (1 - boom) * 0.9 + 0.12; const p = new Path2D(); p.moveTo(CX - 6, -60); p.lineTo(CX + 6, -60); p.lineTo(CX + 14, 120); p.lineTo(CX - 14, 120); p.closePath(); const gr = b.createLinearGradient(CX - 14, 0, CX + 14, 0); gr.addColorStop(0, "rgba(255,240,190,0)"); gr.addColorStop(0.5, `rgba(255,248,220,${a})`); gr.addColorStop(1, "rgba(255,240,190,0)"); b.fillStyle = gr; b.fill(p); }
      sceneRays(b, CX, CY, 20, 95, "#FFD66B", outC(segT(T, LVL.boom, LVL.boom + 0.5)) * (0.75 + 0.25 * sin(t * 2)), t * 0.16, 0.06);
      sceneRays(b, CX, CY, 10, 75, "#FFFFFF", 0.35 * outC(segT(T, LVL.boom + 0.1, LVL.boom + 0.8)), -t * 0.12, 0.03);
      shockRing(b, CX, CY, segT(T, LVL.boom, LVL.boom + 0.7), "#FFFFFF", 50);
      shockRing(b, CX, CY, segT(T, LVL.boom + 0.12, LVL.boom + 1.1), "#FFD66B", 72);
      // anel de runas girando atrás do brasão
      const ru = outC(segT(T, LVL.boom + 0.1, LVL.boom + 0.8));
      if (ru > 0) {
        const rr = CR + 9; b.save(); b.globalAlpha = ru * 0.85;
        const ring = new Path2D(); ring.arc(CX, CY, rr, 0, 2 * PI); stroke(b, ring, "rgba(255,214,107,0.55)", 0.4);
        const ring2 = new Path2D(); ring2.arc(CX, CY, rr + 3.2, 0, 2 * PI); stroke(b, ring2, "rgba(255,214,107,0.35)", 0.3);
        const RU = [[0, -1.3, 0, 1.3, -0.9, -0.4, 0.9, 0.4], [-0.9, -1.3, 0.9, -1.3, 0, -1.3, 0, 1.3], [-0.8, 1.3, 0, -1.3, 0, -1.3, 0.8, 1.3], [-0.9, -0.6, 0.9, -0.6, -0.9, 0.6, 0.9, 0.6]];
        for (let i = 0; i < 18; i++) { const a = i * 2 * PI / 18 + t * 0.25, x = CX + cos(a) * (rr + 1.6), y = CY + sin(a) * (rr + 1.6), r = RU[i % 4];
          b.save(); b.translate(x, y); b.rotate(a + PI / 2); b.strokeStyle = "rgba(255,236,170,0.9)"; b.lineWidth = 0.35; b.lineCap = "round"; b.beginPath(); b.moveTo(r[0], r[1]); b.lineTo(r[2], r[3]); b.moveTo(r[4], r[5]); b.lineTo(r[6], r[7]); b.stroke(); b.restore(); }
        b.restore();
      }
    });
    // ---- antes do impacto: orbes de XP enchendo o anel, o emblema antigo racha
    if (!after) {
      const pulse = segT(T, LVL.fillEnd, LVL.boom), squeeze = 1 - 0.08 * sin(PI * pulse) - 0.06 * pulse;
      g.save(); g.translate(CX, CY); g.scale(squeeze, squeeze); g.translate(-CX, -CY);
      // disco antigo, prateado
      const rim = new Path2D(); rim.arc(CX, CY, CR, 0, 2 * PI);
      g.fillStyle = vGrad(g, ["#E4E8F0", "#9AA2B5"], CY - CR, CY + CR); g.fill(rim);
      circle(g, vGrad(g, ["#3A3570", "#221E4E"], CY - CR, CY + CR), CR * 0.84, CX, CY);
      g.save(); g.font = `900 ${old.length > 1 ? 14 : 17}px Nunito, "Segoe UI", system-ui, sans-serif`; g.textAlign = "center"; g.textBaseline = "middle"; g.fillStyle = "rgba(220,224,240,0.9)"; g.fillText(old, CX, CY + 1); g.restore();
      // rachaduras
      const cr = segT(T, LVL.crack, LVL.boom);
      if (cr > 0) { g.save(); g.globalAlpha = 0.9; const p = new Path2D(); const rays = 7; for (let i = 0; i < rays; i++) { const a = i * 2 * PI / rays + 0.4; let x = CX, y = CY; p.moveTo(x, y); for (let j = 1; j <= 3; j++) { const rr = CR * cr * j / 3, aj = a + (j % 2 ? 0.18 : -0.12); x = CX + cos(aj) * rr; y = CY + sin(aj) * rr; p.lineTo(x, y); } } stroke(g, p, "rgba(255,240,200,0.95)", 0.5); g.restore(); glowAt(g, CX, CY, CR * 1.4, "#FFE59A", 0.5 * cr); }
      g.restore();
      // o anel de energia
      const track = new Path2D(); track.arc(CX, CY, CR + 3.4, 0, 2 * PI); stroke(g, track, "rgba(255,255,255,0.12)", 1.6);
      if (fill > 0) {
        const arc = new Path2D(); arc.arc(CX, CY, CR + 3.4, -PI / 2, -PI / 2 + 2 * PI * fill);
        g.save(); g.shadowColor = "rgba(126,224,184,0.9)"; g.shadowBlur = 6 * k * (0.5 + pulse); stroke(g, arc, pulse > 0 ? "#FFF6D0" : "#7EE0B8", 1.6 + pulse); g.restore();
        const ex = CX + cos(-PI / 2 + 2 * PI * fill) * (CR + 3.4), ey = CY + sin(-PI / 2 + 2 * PI * fill) * (CR + 3.4);
        glowAt(g, ex, ey, 4, "#FFFFFF", 0.9); circle(g, "#FFFFFF", 0.9, ex, ey);
      }
      // os orbes: saem de fora do quadro em espiral e entram no anel na ponta da carga
      for (let i = 0; i < 30; i++) {
        const start = 0.05 + i * 0.033, u = segT(T, start, start + 0.5); if (u <= 0 || u >= 1) continue;
        const a0 = i * 2.39996, r0 = 75, aT = -PI / 2 + 2 * PI * Math.min(1, (i + 1) / 30), rT = CR + 3.4;
        const e = u * u, a = lerp(a0, aT + 2 * PI * (a0 > aT ? 1 : 0), e) , r = lerp(r0, rT, e);
        const x = CX + cos(a) * r, y = CY + sin(a) * r * 0.9;
        for (let j = 3; j >= 0; j--) { const ue = Math.max(0, u - j * 0.04) ** 2, aa = lerp(a0, aT, ue), rj = lerp(r0, rT, ue); circle(g, `rgba(126,224,184,${0.25 * (1 - j / 4)})`, 0.9 - j * 0.15, CX + cos(aa) * rj, CY + sin(aa) * rj * 0.9); }
        glowAt(g, x, y, 3.2, "#7EE0B8", 0.8); circle(g, "#E9FFF6", 0.7, x, y);
      }
    }
    // ---- o impacto: cacos voando
    if (after && boom < 1) {
      for (let i = 0; i < 16; i++) {
        const a = i * 2 * PI / 16 + 0.2, sp = 26 + (i % 4) * 9, d = outC(boom) * sp, x = CX + cos(a) * (CR * 0.5 + d), y = CY + sin(a) * (CR * 0.5 + d) + boom * boom * 14;
        g.save(); g.globalAlpha = 1 - boom; g.translate(x, y); g.rotate(a + boom * (i % 2 ? 6 : -5));
        const p = new Path2D(); p.moveTo(-2.4, -1.6); p.lineTo(2.6, -0.8); p.lineTo(0.4, 2.2); p.closePath();
        g.fillStyle = i % 3 ? "#C7CDD9" : "#3A3570"; g.fill(p); g.strokeStyle = "rgba(255,255,255,0.6)"; g.lineWidth = 0.25; g.stroke(p); g.restore();
      }
      glowAt(g, CX, CY, 46, "#FFFFFF", 0.95 * (1 - outC(boom)));
    }
    // ---- o brasão novo com as asas
    if (after) {
      const slam = segT(T, LVL.boom, LVL.boom + 0.45), sc = slam < 1 ? 2.3 - 1.3 * outBack(slam) : 1 + 0.015 * sin(t * 2.4);
      const wings = outBack(segT(T, LVL.wings, LVL.wings + 0.7));
      g.save(); g.translate(CX, CY); g.scale(sc, sc); g.rotate((1 - outC(slam)) * -0.25); g.translate(-CX, -CY); g.globalAlpha = clamp(slam * 3, 0, 1);
      if (wings > 0) { drawWing(g, -1, wings, t); drawWing(g, 1, wings, t); }
      g.save(); if (Q === 1) { g.shadowColor = "rgba(255,190,60,0.85)"; g.shadowBlur = 14 * k; } drawCrest(g, t, lvl, k, T > 2.4 ? ((T - 2.4) * 0.4) % 1.4 : -1); g.restore();
      // a faixa "NÍVEL" embaixo
      const ban = outBack(segT(T, LVL.banner, LVL.banner + 0.45));
      if (ban > 0) {
        g.save(); g.translate(CX, CY + CR + 5); g.scale(ban, 1);
        for (const sd of [-1, 1]) { const p = new Path2D(); p.moveTo(sd * 12, -2.6); p.lineTo(sd * 18, -3); p.lineTo(sd * 16, 0.4); p.lineTo(sd * 18, 3.8); p.lineTo(sd * 12, 3.4); p.closePath(); g.fillStyle = "#9A6206"; g.fill(p); }
        const bp = new Path2D(); bp.moveTo(-14, -3.6); bp.quadraticCurveTo(0, -5, 14, -3.6); bp.lineTo(14, 3.2); bp.quadraticCurveTo(0, 1.8, -14, 3.2); bp.closePath();
        g.fillStyle = hGrad(g, ["#B07510", "#F5B83D", "#FFE59A", "#F5B83D", "#B07510"], -14, 14); g.fill(bp); stroke(g, bp, "rgba(90,56,3,0.8)", 0.35);
        g.fillStyle = "#3A2206"; g.font = "900 3.6px Nunito, system-ui, sans-serif"; g.textAlign = "center"; g.textBaseline = "middle"; g.fillText("N Í V E L", 0, -0.4);
        g.restore();
      }
      g.restore();
      // estrelinhas em órbita e fagulhas subindo
      for (let i = 0; i < 6; i++) { const a = t * 0.9 + i * PI / 3, x = CX + cos(a) * (CR + 12), y = CY + sin(a) * (CR + 8), ph = (t * 1.2 + i / 6) % 1; star4(g, x, y, 1.8 * sin(PI * ph) * outC(segT(T, LVL.wings, LVL.wings + 0.6)), "rgba(255,240,190,0.95)"); }
      for (let i = 0; i < 10; i++) { const u = (t * 0.35 + i * 0.1) % 1, x = CX + sin(i * 2.7 + t * 0.8) * 26, y = 70 - u * 70; circle(g, `rgba(255,214,107,${0.8 * (1 - u) * outC(segT(T, LVL.boom, LVL.boom + 0.6))})`, 0.4 + 0.3 * (1 - u), x, y); }
    }
    g.restore();
    // ---- o Folha: olha a carga, leva um susto no impacto e dá um pulão
    const jump = segT(T, LVL.boom, LVL.boom + 0.9), jy = jump > 0 && jump < 1 ? -sin(PI * jump) * 16 : 0;
    const o = { T, t, x: 50, y: 108 + (1 - outC(segT(T, 0, 0.5))) * 30 + jy, scale: 0.5, look, dir: { x: 0, y: -1 },
      script: [[0, "idle"], [LVL.crack, "surprised"], [LVL.boom, "ecstatic"], [3.6, "happy"], [5.2, "proud"]] };
    floorLight(g, 50, 107, 30, after ? "#FFD66B" : "#FFFFFF", 0.25 - (jy ? 0.1 : 0));
    sceneFolha(g, o);
    drawConfetti(g, el._conf, T - LVL.boom);
  }

  // cor do fogo pela idade da labareda: branco-amarelo na base, laranja no meio, vermelho e fumaça no alto
  const FIRE = [[0, [255, 252, 225]], [0.18, [255, 220, 120]], [0.42, [255, 150, 45]], [0.68, [235, 70, 30]], [1, [120, 30, 40]]];
  function fireColor(u) {
    for (let i = 1; i < FIRE.length; i++) if (u <= FIRE[i][0]) { const [a, ca] = FIRE[i - 1], [b, cb] = FIRE[i], k = (u - a) / (b - a); return ca.map((v, j) => Math.round(v + (cb[j] - v) * k)); }
    return FIRE[FIRE.length - 1][1];
  }
  // ---- cena: emblema (a forja)
  const TIERS = { 1: ["#F2C49A", "#C27E45", "#7D4520"], 2: ["#FFFFFF", "#C7CDD9", "#7D8699"], 3: ["#FFF1BF", "#F5B83D", "#B07510"], 4: ["#CFE3FF", "#5E9BFF", "#1F4FB8"], 5: ["#FFD0D8", "#F0506B", "#A0172F"], 6: ["#D4FFEE", "#3FD69F", "#0E7C55"] };
  /**
   * A medalha de emblema, de colecionador (centro em 0,0, raio R). O símbolo da categoria é cunhado no
   * metal (baixo-relevo), a face tem guilhochê e as estrelinhas do nível; a moldura cresce com o nível:
   * Bronze e Prata a moeda limpa, Ouro ganha louros, Safira a gema no topo, Rubi a roseta, Esmeralda as asas.
   * o: { deco 0..1 (decorações crescendo), engrave 0..1 (símbolo cunhado), heat 0..1 (incandescente), shine (-1 sem) }
   */
  function drawMedalArt(g, R, tier, cat, o) {
    const C = TIERS[tier] || TIERS[3], deco = o.deco == null ? 1 : o.deco, eng = o.engrave == null ? 1 : o.engrave, heat = o.heat || 0;
    const metal = (x0, y0, x1, y1, a, b, c) => { const gr = g.createLinearGradient(x0, y0, x1, y1); gr.addColorStop(0, a); gr.addColorStop(0.5, b); gr.addColorStop(1, c); return gr; };
    // ---- moldura de trás
    if (tier >= 5 && deco > 0) {
      // roseta: estrela de 16 pontas, em duas camadas
      for (const [ro, ri, rot, col] of [[1.62, 1.28, 0, C[2]], [1.48, 1.18, PI / 16, C[1]]]) {
        const p = new Path2D(); for (let i = 0; i < 32; i++) { const a = rot + i * PI / 16, r = R * (i % 2 ? ri : ro) * deco; i ? p.lineTo(cos(a) * r, sin(a) * r) : p.moveTo(cos(a) * r, sin(a) * r); } p.closePath();
        g.fillStyle = metal(-R, -R, R, R, col, C[1], C[2]); g.fill(p); g.strokeStyle = "rgba(0,0,0,0.25)"; g.lineWidth = 0.3; g.stroke(p);
      }
    }
    if (tier >= 6 && deco > 0) {
      // asas de metal dos dois lados
      for (const side of [-1, 1]) for (let i = 4; i >= 0; i--) {
        g.save(); g.translate(side * R * 0.85, -R * 0.15); g.scale(side * deco, deco); g.rotate((-20 - i * 14) * PI / 180);
        const len = R * (1.05 + i * 0.16), f = new Path2D(); f.moveTo(0, -R * 0.12); f.bezierCurveTo(len * 0.4, -R * 0.3, len * 0.85, -R * 0.24, len, -R * 0.03); f.bezierCurveTo(len * 0.85, R * 0.1, len * 0.4, R * 0.16, 0, R * 0.12); f.closePath();
        g.fillStyle = metal(0, 0, len, 0, C[2], C[1], C[0]); g.fill(f); g.strokeStyle = "rgba(0,0,0,0.3)"; g.lineWidth = 0.3; g.stroke(f); g.restore();
      }
    }
    if (tier >= 3 && deco > 0) {
      // louros: dois ramos subindo pelos lados
      for (const side of [-1, 1]) {
        const stem = new Path2D(); stem.arc(0, 0, R * 1.13, PI / 2 + side * 0.25, PI / 2 + side * (0.25 + 1.95 * deco), side < 0);
        g.strokeStyle = C[2]; g.lineWidth = R * 0.05; g.stroke(stem);
        const n = Math.round(9 * deco);
        for (let i = 0; i < n; i++) {
          const a = PI / 2 + side * (0.4 + i * 0.205), x = cos(a) * R * 1.13, y = sin(a) * R * 1.13;
          for (const off of [-1, 1]) {
            g.save(); g.translate(x, y); g.rotate(a + side * PI / 2 + off * 0.55 + side * 0.25);
            const leaf = new Path2D(); leaf.ellipse(R * 0.16, 0, R * 0.17, R * 0.07, 0, 0, 2 * PI);
            g.fillStyle = metal(0, -R * 0.07, R * 0.3, R * 0.07, C[0], C[1], C[2]); g.fill(leaf);
            line(g, "rgba(0,0,0,0.22)", R * 0.02, 0, R * 0.3, 0, R * 0.02); g.restore();
          }
        }
      }
    }
    // ---- a moeda
    const rim = new Path2D(); for (let i = 0; i <= 96; i++) { const a = i * PI / 48, r = i % 2 ? R : R * 0.965; i ? rim.lineTo(cos(a) * r, sin(a) * r) : rim.moveTo(cos(a) * r, sin(a) * r); }
    g.save(); if (Q === 1) { g.shadowColor = "rgba(10,4,30,0.5)"; g.shadowBlur = R * 0.4 * (o.k || 1); g.shadowOffsetY = R * 0.06 * (o.k || 1); }
    g.fillStyle = metal(-R, -R, R, R, C[0], C[1], C[2]); g.fill(rim); g.restore();
    g.fillStyle = metal(R, R, -R, -R, C[0], C[1], C[2]); g.beginPath(); g.arc(0, 0, R * 0.87, 0, 2 * PI); g.fill();   // chanfro
    const face = g.createRadialGradient(-R * 0.3, -R * 0.35, R * 0.05, 0, 0, R * 0.82); face.addColorStop(0, C[0]); face.addColorStop(0.65, C[1]); face.addColorStop(1, C[2]);
    g.fillStyle = face; g.beginPath(); g.arc(0, 0, R * 0.8, 0, 2 * PI); g.fill();
    // guilhochê: anéis finos e raios bem leves
    g.save(); g.beginPath(); g.arc(0, 0, R * 0.8, 0, 2 * PI); g.clip();
    for (let i = 1; i <= 7; i++) { g.beginPath(); g.arc(0, 0, R * 0.8 * i / 7.5, 0, 2 * PI); g.strokeStyle = "rgba(255,255,255,0.13)"; g.lineWidth = R * 0.008; g.stroke(); }
    for (let i = 0; i < 36; i++) { const a = i * PI / 18; line(g, "rgba(0,0,0,0.06)", 0, 0, cos(a) * R * 0.8, sin(a) * R * 0.8, R * 0.01); }
    g.restore();
    const ring = new Path2D(); ring.arc(0, 0, R * 0.71, 0, 2 * PI); stroke(g, ring, "rgba(0,0,0,0.22)", R * 0.025); const ring2 = new Path2D(); ring2.arc(0, 0, R * 0.69, 0, 2 * PI); stroke(g, ring2, "rgba(255,255,255,0.35)", R * 0.012);
    // estrelinhas do nível, embaixo dentro da moeda
    for (let i = 0; i < tier; i++) { const a = PI / 2 + (i - (tier - 1) / 2) * 0.27, x = cos(a) * R * 0.58, y = sin(a) * R * 0.58; g.fillStyle = C[2]; g.fill(star5(g, x + R * 0.01, y + R * 0.015, R * 0.075)); g.fillStyle = C[0]; g.fill(star5(g, x, y, R * 0.07)); }
    // o símbolo cunhado: sombra funda, borda clara e o miolo um tom abaixo da face
    if (eng > 0) {
      const s = R / 13.5 * (1 + (1 - eng) * 0.25);
      g.save(); g.globalAlpha = clamp(eng * 1.5, 0, 1); g.translate(0, -R * 0.06); g.scale(s, s);
      g.save(); g.translate(0.55, 0.65); badgeGlyph(g, cat, "rgba(0,0,0,0.38)"); g.restore();
      g.save(); g.translate(-0.4, -0.45); badgeGlyph(g, cat, "rgba(255,255,255,0.75)"); g.restore();
      badgeGlyph(g, cat, C[2]);
      g.restore();
    }
    // brilho de vidro e argola em cima
    const hl = new Path2D(); hl.arc(0, 0, R * 0.92, PI * 1.08, PI * 1.45); stroke(g, hl, "rgba(255,255,255,0.75)", R * 0.05);
    if (tier >= 4 && deco > 0) {
      // gema lapidada no topo da moldura
      const gy = -R * 1.02, gs = R * 0.2 * deco;
      const gem = new Path2D(); gem.moveTo(0, gy - gs); gem.lineTo(gs * 0.9, gy - gs * 0.2); gem.lineTo(0, gy + gs * 0.9); gem.lineTo(-gs * 0.9, gy - gs * 0.2); gem.closePath();
      g.fillStyle = metal(-gs, gy - gs, gs, gy + gs, "#FFFFFF", tier === 4 ? "#5E9BFF" : tier === 5 ? "#F0506B" : "#3FD69F", "#1A1040"); g.fill(gem);
      line(g, "rgba(255,255,255,0.7)", -gs * 0.9, gy - gs * 0.2, gs * 0.9, gy - gs * 0.2, R * 0.015); line(g, "rgba(255,255,255,0.5)", 0, gy - gs, 0, gy + gs * 0.9, R * 0.012);
    }
    if (o.shine >= 0 && o.shine <= 1) {
      g.save(); g.clip(rim); g.rotate(-0.6);
      const x = lerp(-R * 1.6, R * 1.6, o.shine), gr = g.createLinearGradient(x - R * 0.35, 0, x + R * 0.35, 0);
      gr.addColorStop(0, "rgba(255,255,255,0)"); gr.addColorStop(0.5, "rgba(255,255,255,0.8)"); gr.addColorStop(1, "rgba(255,255,255,0)");
      g.fillStyle = gr; g.fillRect(x - R * 0.35, -R * 2, R * 0.7, R * 4); g.restore();
    }
    // incandescente: brasa laranja por cima, sumindo conforme esfria
    if (heat > 0) {
      g.save(); g.clip(rim); g.globalAlpha = heat;
      const hg = g.createRadialGradient(0, 0, 0, 0, 0, R); hg.addColorStop(0, "#FFF6D0"); hg.addColorStop(0.45, "#FFB13B"); hg.addColorStop(1, "#E04A1A");
      g.globalCompositeOperation = "source-atop"; g.fillStyle = hg; g.fillRect(-R, -R, R * 2, R * 2); g.restore();
      glowAt(g, 0, 0, R * 1.5, "#FF8A2A", 0.45 * heat);
    }
  }

  // ---- a forja do Folha: bigorna, martelo de ferreiro e faíscas de verdade
  // O martelo, no referencial da mão: cabo saindo da mão para +x, cabeça de aço atravessada no fim (a face
  // de bater fica para +y, a pena em cunha para -y). Cabo de madeira com veio, empunhadura de couro.
  const HAMMER_LEN = 17, HAMMER_HEAD = 1.35;
  function drawSmithHammer(g, x, y, ang, a = 1, hot = 0) {
    const L = HAMMER_LEN;
    g.save(); g.translate(x, y); g.rotate(ang); g.globalAlpha *= a;
    // cabo, afinando para a cabeça
    const h = new Path2D(); h.moveTo(-3, -0.85); h.quadraticCurveTo(L * 0.5, -0.72, L - 1.6, -0.62); h.lineTo(L - 1.6, 0.62); h.quadraticCurveTo(L * 0.5, 0.72, -3, 0.85); h.quadraticCurveTo(-3.6, 0, -3, -0.85); h.closePath();
    g.fillStyle = vGrad(g, ["#E7B07A", "#B87442", "#7A4422"], -0.85, 0.85); g.fill(h);
    g.save(); g.clip(h);
    for (const [y0, w] of [[-0.3, 0.12], [0.15, 0.1], [0.45, 0.08]]) { const p = new Path2D(); p.moveTo(-3, y0); p.bezierCurveTo(2, y0 + 0.15, 6, y0 - 0.12, L, y0 + 0.05); stroke(g, p, "rgba(90,45,15,0.35)", w); }
    line(g, "rgba(255,240,220,0.45)", -2, -0.5, L - 2, -0.42, 0.18);
    g.restore();
    // empunhadura de couro com as voltas
    const grip = new Path2D(); grip.roundRect(-3.2, -1.02, 6.4, 2.04, 0.8); g.fillStyle = vGrad(g, ["#6B4030", "#3E2218", "#24130C"], -1, 1); g.fill(grip);
    g.save(); g.clip(grip); for (let i = -4; i < 4; i++) line(g, "rgba(255,220,190,0.16)", i * 0.95, -1.1, i * 0.95 + 0.7, 1.1, 0.22); g.restore();
    // cabeça de aço: corpo, face de bater larga embaixo e a pena em cunha em cima
    g.translate(L, 0); g.scale(HAMMER_HEAD, HAMMER_HEAD); g.translate(-L, 0);
    // cabeça: bloco de aço com cintura, face de bater (embaixo) com anel chanfrado e a pena rombuda em cima
    const hx = L, bw = 2.7;
    const body = new Path2D();
    body.moveTo(hx - bw, -3.6); body.lineTo(hx + bw, -3.6); body.quadraticCurveTo(hx + bw * 0.86, 0.4, hx + bw * 1.1, 3.4); body.lineTo(hx + bw * 1.1, 4.8);
    body.lineTo(hx - bw * 1.1, 4.8); body.lineTo(hx - bw * 1.1, 3.4); body.quadraticCurveTo(hx - bw * 0.86, 0.4, hx - bw, -3.6); body.closePath();
    const steel = g.createLinearGradient(hx - bw * 1.2, 0, hx + bw * 1.2, 0);
    steel.addColorStop(0, "#2B2F40"); steel.addColorStop(0.3, "#7E869C"); steel.addColorStop(0.42, "#B8BFD0"); steel.addColorStop(0.6, "#6A7189"); steel.addColorStop(1, "#23263A");
    g.fillStyle = steel; g.fill(body);
    const peen = new Path2D(); peen.moveTo(hx - bw, -3.6); peen.lineTo(hx + bw, -3.6); peen.lineTo(hx + bw * 0.5, -5.6); peen.lineTo(hx - bw * 0.5, -5.6); peen.closePath();
    g.fillStyle = steel; g.fill(peen); g.fillStyle = "rgba(15,16,30,0.35)"; g.fill(peen);
    stroke(g, body, "rgba(12,13,24,0.8)", 0.24); stroke(g, peen, "rgba(12,13,24,0.8)", 0.24);
    const ringF = new Path2D(); ringF.rect(hx - bw * 1.1, 3.4, bw * 2.2, 1.4); g.fillStyle = "rgba(255,255,255,0.12)"; g.fill(ringF);
    line(g, "rgba(12,13,24,0.55)", hx - bw * 1.1, 3.4, hx + bw * 1.1, 3.4, 0.2);
    line(g, "rgba(230,236,255,0.55)", hx - bw * 0.55, -3.2, hx - bw * 0.48, 3, 0.35);
    line(g, "rgba(230,236,255,0.3)", hx - bw * 0.9, -5.3, hx + bw * 0.2, -5.3, 0.2);
    g.beginPath(); g.ellipse(hx, -3.6, 1, 0.38, 0, 0, 2 * PI); g.fillStyle = "#8E5428"; g.fill();
    // a face esquenta ao bater no metal em brasa
    if (hot > 0) {
      const hg = g.createLinearGradient(0, 2.4, 0, 4.8); hg.addColorStop(0, "rgba(255,120,40,0)"); hg.addColorStop(1, `rgba(255,170,70,${0.85 * hot})`);
      g.fillStyle = hg; g.fill(body);
    }
    g.restore();
  }
  // a bigorna, de perfil: mesa com a ponta (chifre) para a direita, cintura e pé largo; o topo pega a luz
  // laranja do metal quente
  const ANVIL = { x0: 59, x1: 85, top: 89.2, horn: 96, floor: 108 };
  function drawAnvil(g, glow, t) {
    const { x0, x1, top, horn, floor } = ANVIL;
    g.save(); g.translate(72, floor); g.scale(1, 0.16); glowAt(g, 0, 0, 22, "#05030F", 0.6); g.restore();
    const p = new Path2D();
    p.moveTo(x0 - 1.4, top); p.lineTo(x1, top);                                     // mesa
    p.bezierCurveTo(x1 + 4, top, horn - 2, top + 0.6, horn, top + 1.2);               // chifre
    p.bezierCurveTo(horn - 3, top + 3.2, x1 + 2, top + 4.6, x1 - 1.2, top + 5.4);
    p.quadraticCurveTo(x1 - 4, top + 7, x1 - 5.4, top + 11.4);                        // cintura
    p.quadraticCurveTo(x1 - 4.4, floor - 3.4, x1 + 1.6, floor - 1.2); p.lineTo(x1 + 1.6, floor);   // pé
    p.lineTo(x0 + 0.4, floor); p.lineTo(x0 + 0.4, floor - 1.2);
    p.quadraticCurveTo(x0 + 6.2, floor - 3.4, x0 + 7.4, top + 11.4);
    p.quadraticCurveTo(x0 + 6.2, top + 7, x0 + 1.2, top + 5.4);
    p.lineTo(x0 - 1.4, top + 4.4); p.closePath();
    const body = g.createLinearGradient(0, top, 0, floor); body.addColorStop(0, "#4B4D66"); body.addColorStop(0.35, "#2D2E44"); body.addColorStop(1, "#15151F");
    g.fillStyle = body; g.fill(p);
    g.save(); g.clip(p);
    // volume: luz fria vindo da esquerda, sombra à direita
    const side = g.createLinearGradient(x0 - 2, 0, horn, 0); side.addColorStop(0, "rgba(160,170,230,0.22)"); side.addColorStop(0.5, "rgba(0,0,0,0)"); side.addColorStop(1, "rgba(0,0,0,0.35)");
    g.fillStyle = side; g.fillRect(x0 - 3, top - 1, horn - x0 + 6, floor - top + 2);
    // a mesa de aço polido e o reflexo quente
    g.fillStyle = vGrad(g, ["#8E93AE", "#555872"], top, top + 1.6); g.fillRect(x0 - 2, top, x1 - x0 + 3, 1.6);
    if (glow > 0) { const gg = g.createRadialGradient(71, top, 0, 71, top, 18); gg.addColorStop(0, `rgba(255,160,70,${0.75 * glow})`); gg.addColorStop(1, "rgba(255,120,40,0)"); g.fillStyle = gg; g.fillRect(x0 - 3, top - 2, horn - x0 + 6, 14); }
    g.restore();
    stroke(g, p, "rgba(5,5,15,0.6)", 0.3);
    // a mesa vista um pouco de cima: faixa de aço polido com o chifre afinando, o furo quadrado e o reflexo quente
    const face = new Path2D(); face.moveTo(x0 - 1.4, top - 3.2); face.lineTo(x1, top - 3.2);
    face.bezierCurveTo(x1 + 4, top - 3.2, horn - 3, top - 0.6, horn, top + 1.2); face.bezierCurveTo(horn - 2.4, top + 0.5, x1 + 4, top, x1, top); face.lineTo(x0 - 1.4, top); face.closePath();
    g.fillStyle = vGrad(g, ["#6E7290", "#A3A8C4", "#7B7F9C"], top - 3.2, top); g.fill(face);
    if (glow > 0) { g.save(); g.clip(face); const gg = g.createRadialGradient(71, top - 1.6, 0, 71, top - 1.6, 16); gg.addColorStop(0, `rgba(255,170,80,${0.9 * glow})`); gg.addColorStop(1, "rgba(255,120,40,0)"); g.fillStyle = gg; g.fillRect(x0 - 2, top - 4, horn - x0 + 4, 6); g.restore(); }
    stroke(g, face, "rgba(5,5,15,0.55)", 0.25);
    line(g, "rgba(230,235,255,0.7)", x0 - 1, top - 0.1, x1 + 2, top - 0.1, 0.28);
    g.fillStyle = "rgba(10,10,20,0.65)"; g.fillRect(x1 - 4.4, top - 2.4, 1.8, 1.2); g.beginPath(); g.ellipse(x1 - 1.4, top - 1.6, 0.6, 0.4, 0, 0, 2 * PI); g.fill();
  }
  // a medalha deitada na bigorna (perspectiva: achatada no y), com a espessura da borda aparecendo
  // a arte ilustrada do emblema como disco da forja: aparece conforme é cunhada (eng) e em brasa enquanto quente.
  // A arte tem a fita embaixo, então o medalhão ocupa uns 68% da largura e fica um pouco acima do centro.
  function drawArteDisc(g, arte, R, eng, heat) {
    if (!arte || eng <= 0) return;
    const D = R * 2 / 0.68, c = arte._hot || (arte._hot = cpuCanvas()), n = 160;
    if (c.width !== n) { c.width = c.height = n; }
    const h = c.getContext("2d"); h.clearRect(0, 0, n, n); h.globalCompositeOperation = "source-over"; h.drawImage(arte, 0, 0, n, n);
    if (heat > 0) { h.globalCompositeOperation = "source-atop"; h.fillStyle = `rgba(255,${Math.round(90 + 60 * (1 - heat))},30,${0.75 * heat})`; h.fillRect(0, 0, n, n); }
    g.save(); g.globalAlpha *= eng; g.drawImage(c, -D / 2, -D * 0.45, D, D); g.restore();
  }
  function drawFlatMedal(g, x, y, R, sy, tier, cat, o) {
    const C = TIERS[tier] || TIERS[3], heat = o.heat || 0, sq = o.squash || 0;
    g.save(); g.translate(x, y); g.scale(1 + sq * 0.08, 1 - sq * 0.1);
    g.save(); g.scale(1, sy);
    g.beginPath(); g.ellipse(0, 3.4, R * 0.99, R * 0.99, 0, 0, 2 * PI);
    g.fillStyle = heat > 0.5 ? lerpColor("#7D2A10", "#E0601A", heat) : lerpColor(C[2], "#B8461A", heat * 2); g.fill();
    if (o.arte) drawArteDisc(g, o.arte, R, o.engrave, heat);
    else drawMedalArt(g, R, tier, cat, { deco: 0, engrave: o.engrave, heat, k: o.k });
    g.restore(); g.restore();
  }
  // faíscas das marteladas: cada uma sai da pancada com velocidade própria, sofre arrasto e gravidade,
  // quica na mesa da bigorna e muda de cor com a idade (branca, amarela, laranja, vermelha)
  function sparkSet(seed, n) {
    const r = seeded(seed);
    return Array.from({ length: n }, () => {
      const side = r() < 0.5 ? -1 : 1, a = -PI / 2 + side * (0.25 + r() * 1.25);
      return { vx: cos(a) * (35 + r() * 75), vy: sin(a) * (35 + r() * 75), life: 0.35 + r() * 0.75, w: 0.25 + r() * 0.4, big: r() < 0.12 };
    });
  }
  function sparkAt(s, u, x0, y0) {
    const d = 3.2, e = (1 - Math.exp(-d * u)) / d;
    let x = x0 + s.vx * e, y = y0 + s.vy * e + 70 * u * u;
    const surf = x > ANVIL.x0 - 1 && x < ANVIL.horn ? ANVIL.top - 1.6 : ANVIL.floor;
    if (y > surf) y = surf - (y - surf) * 0.28;
    return [x, y];
  }
  function drawSparks(g, list, u, x0, y0, q) {
    g.save(); g.globalCompositeOperation = "lighter"; g.lineCap = "round";
    const n = Math.round(list.length * q);
    for (let i = 0; i < n; i++) {
      const s = list[i], age = u / s.life; if (age <= 0 || age >= 1) continue;
      const [x, y] = sparkAt(s, u, x0, y0), [px, py] = sparkAt(s, Math.max(0, u - 0.028), x0, y0), c = fireColor(age * 0.95);
      g.strokeStyle = `rgba(${c},${1 - age * age})`; g.lineWidth = s.w * (1 - age * 0.6) * (s.big ? 1.6 : 1);
      g.beginPath(); g.moveTo(px, py); g.lineTo(x, y); g.stroke();
      if (s.big || age < 0.25) glowAt(g, x, y, s.big ? 2.6 : 1.4, "#FFB860", 0.35 * (1 - age));
    }
    g.restore();
  }
  // brasas subindo no ar da oficina
  function embers(g, t, a, q) {
    if (a <= 0) return;
    g.save(); g.globalCompositeOperation = "lighter";
    for (let i = 0, n = Math.round(22 * q); i < n; i++) {
      const sp = 0.07 + (i % 5) * 0.018, u = (t * sp + i * 0.161) % 1, x = 8 + ((i * 41) % 88) + sin(t * 0.9 + i) * 3 * u, y = 112 - u * 120;
      const c = fireColor(0.25 + u * 0.6), al = a * sin(PI * u) * (0.45 + 0.4 * sin(t * 5 + i));
      glowAt(g, x, y, 1.6, "#FF9A40", al * 0.5); circle(g, `rgba(${c},${al})`, 0.28 + (i % 3) * 0.12, x, y);
    }
    g.restore();
  }

  // Linha do tempo da forja (segundos):
  // 0–0,6  a oficina acende, a bigorna e o Folha (com o martelo) entram;
  // 0,55   o disco em brasa cai na bigorna, quica e solta umas fagulhas;
  // 1,35 / 2,05 / 2,8  três marteladas (com um respiro entre elas para ver a medalha tomando forma): o Folha levanta o martelo, desce rápido (rastro de movimento), clarão
  //        na pancada, chuva de faíscas que quica na bigorna, tremor; o símbolo vai ficando cunhado;
  // 2,85–3,45  o metal esfria (do laranja à cor do nível) soltando vapor;
  // 3,5–4,1 a medalha sobe girando até o centro, a câmera abre, e acontece a revelação: raios, fita, a
  //        moldura do nível crescendo e a faixa; o Folha pula para o centro erguendo o martelo.
  const FORGE = { drop: 0.55, land: 0.85, hits: [1.35, 2.05, 2.8], cool: [2.85, 3.45], rise: 3.5, arrive: 4.1, deco: 4.25, banner: 4.5 };
  // poses do braço do martelo (mão na grade do Folha e ângulo do martelo)
  const HP = { rest: [100, 64, 1.2], raised: [92, 14, -2.1], big: [88, 2, -2.45], strike: [104, 42.1, 0] };
  // depois da última martelada o Folha larga o martelo em pé, encostado na bigorna
  const HAMMER_DOWN = FORGE.hits[2] + 0.27, HAMMER_LEAN = [56.5, 104, PI / 2 + 0.25];
  function hammerState(T) {
    const mix = (A, B, u) => [lerp(A[0], B[0], u), lerp(A[1], B[1], u), lerp(A[2], B[2], u)];
    const H = FORGE.hits;
    let s = HP.rest, blur = 0, hot = 0;
    for (let i = 0; i < H.length; i++) {
      const h = H[i], from = i === 0 ? HP.rest : HP.strike, up = i === H.length - 1 ? HP.big : HP.raised;
      if (T >= h - 0.42 && T < h - 0.085) { s = mix(from, up, sstep((T - (h - 0.42)) / 0.335)); return { s, blur, hot, up }; }
      if (T >= h - 0.085 && T < h) { const u = (T - (h - 0.085)) / 0.085; s = mix(up, HP.strike, u * u); return { s, blur: u, hot, up }; }
      if (T >= h && (i === H.length - 1 || T < H[i + 1] - 0.42)) {
        const d = T - h; hot = 1 - segT(d, 0.05, 0.5);
        // pancada: um repique de leve e fica apoiado; depois do último golpe volta ao descanso
        s = mix(HP.strike, [104, 39, -0.12], sin(PI * segT(d, 0, 0.16)) * 0.6);
        if (i === H.length - 1) s = mix(s, HP.rest, sstep(segT(d, 0.12, 0.27)));
        return { s, blur, hot, up: HP.raised };
      }
    }
    return { s, blur, hot, up: HP.raised };
  }
  function cenaEmblema(g, T, t, el) {
    const tier = parseInt(el.getAttribute("tier") || "3", 10), cat = el.getAttribute("categoria") || "seq", faixa = (el.getAttribute("faixa") || "").toUpperCase();
    const C = TIERS[tier] || TIERS[3], look = el._look(), k = this_k(el), q = Q;
    // a arte ilustrada do emblema (atributo "arte"): é ela que aparece pronta no fim, no lugar da medalha desenhada
    const src = el.getAttribute("arte");
    if (src && el._arteSrc !== src) { el._arteSrc = src; el._arte = null; const im = new Image(); im.onload = () => { if (el._arteSrc === src) el._arte = im; }; im.src = src; }
    const arte = src ? el._arte : null;
    const MX = 50, MY = 38, R = 15, FX0 = 26, FY = 108, FS = 0.52;
    const sparks = el._sparks || (el._sparks = [sparkSet(31, 70), sparkSet(57, 70), sparkSet(83, 90), sparkSet(19, 18)]);
    // onde a cabeça do martelo bate: a mão na pose de golpe mais o comprimento do cabo
    const hitX = FX0 + (HP.strike[0] - 50) * FS + HAMMER_LEN, hitY = ANVIL.top - 1.8;
    let lastHit = -9, hits = 0;
    for (let i = 0; i < FORGE.hits.length; i++) if (T >= FORGE.hits[i]) { hits = i + 1; lastHit = FORGE.hits[i]; }
    const since = T - lastHit, shake = since >= 0 && since < 0.24 ? (1 - since / 0.24) * 1.5 : T >= FORGE.land && T < FORGE.land + 0.15 ? 0.6 : 0;
    const heat = 1 - sstep(segT(T, FORGE.cool[0], FORGE.cool[1]));
    const rise = segT(T, FORGE.rise, FORGE.arrive), arrived = T >= FORGE.arrive, reveal = outC(segT(T, FORGE.arrive, FORGE.arrive + 0.5));
    // câmera: perto da bigorna enquanto forja, abre para o quadro inteiro quando a medalha sobe
    const cam = sstep(segT(T, FORGE.rise - 0.1, FORGE.arrive)), z = lerp(1.1, 1, cam), fx = 50, fy = lerp(79, 55, cam), sy0 = lerp(58, 55, cam);
    g.save(); g.translate(sin(t * 89) * shake, cos(t * 73) * shake);
    // ---- fundo: o calor da oficina antes, a luz do nível depois
    const reach = el._reach();
    backdrop(g, el, MX, MY + 10, reach, reach * 0.95, (b) => {
      glowAt(b, 62, 92, 70, "#FF6A1A", (0.2 + 0.14 * heat + 0.05 * sin(t * 3)) * (1 - reveal));
      glowAt(b, MX, MY, 62, C[1], 0.32 * reveal + 0.08 * sin(t * 2) * reveal);
      bokeh(b, t, MX, MY + 10, reveal > 0.5 ? C[0] : "#FFB13B", 0.28, 12);
      sceneRays(b, MX, MY, 20, 92, C[0], reveal * (0.7 + 0.25 * sin(t * 2.2)), t * 0.2, 0.06);
      if (q > 0.6) sceneRays(b, MX, MY, 10, 74, "#FFFFFF", 0.3 * reveal, -t * 0.15, 0.03);
      shockRing(b, MX, MY, segT(T, FORGE.arrive, FORGE.arrive + 0.8), C[0], 58);
      shockRing(b, MX, MY, segT(T, FORGE.arrive + 0.1, FORGE.arrive + 1.1), "#FFFFFF", 78);
    });
    embers(g, t, 0.9 * (1 - reveal * 0.6), q);
    // ---- o mundo da forja, dentro da câmera
    g.save(); g.translate(50, sy0); g.scale(z, z); g.translate(-fx, -fy);
    const forgeA = 1 - segT(T, FORGE.arrive - 0.1, FORGE.arrive + 0.5), enter = outC(segT(T, 0, 0.55));
    if (forgeA > 0) {
      g.save(); g.globalAlpha = forgeA * enter; g.translate(0, (1 - enter) * 14);
      drawAnvil(g, hits > 0 || T > FORGE.land ? heat * (0.6 + 0.4 * (since < 0.3 ? 1 - since / 0.3 : 0)) : 0, t);
      g.restore();
    }
    // o disco em brasa: cai, quica, apanha, esfria e depois sobe girando
    const RF = 10.5, flatY = hitY;
    if (T >= FORGE.drop && !arrived) {
      let x = hitX, y = flatY, sy = 0.22, spin = 0, scale = 1;
      if (T < FORGE.land) { const u = segT(T, FORGE.drop, FORGE.land); y = lerp(30, flatY, u * u); sy = lerp(0.6, 0.22, u); spin = (1 - u) * 2.4; }
      else if (T < FORGE.land + 0.2) y = flatY - sin(PI * segT(T, FORGE.land, FORGE.land + 0.2)) * 2.2;
      if (rise > 0) {
        const e = sstep(rise); x = lerp(hitX, MX, e); y = lerp(flatY, MY, e) - sin(PI * e) * 8; scale = lerp(RF, R, e) / RF; spin = e * 2 * PI; sy = lerp(0.22, 1, e);
      }
      const squash = since >= 0 && since < 0.2 && rise === 0 ? 1 - since / 0.2 : 0;
      const eng = hits === 0 ? 0 : clamp((hits - 1 + outBack(segT(since, 0, 0.25))) / 3, 0, 1);
      // rastro de luz quando sobe
      if (rise > 0 && rise < 1) for (let j = 1; j <= 6; j++) { const e = sstep(Math.max(0, rise - j * 0.035)); glowAt(g, lerp(hitX, MX, e), lerp(flatY, MY, e) - sin(PI * e) * 8, 5 - j * 0.5, C[0], 0.22 * (1 - j / 7)); }
      g.save(); g.translate(x, y); g.scale(scale, scale);
      if (spin !== 0) {
        // girando: a largura vira cos do giro; de costas mostra o verso liso; a espessura aparece na virada
        const cx = Math.cos(spin), th = Math.sin(spin) * RF * 0.12;
        g.save(); g.scale(Math.max(0.04, Math.abs(cx)), sy);
        g.beginPath(); g.arc(th / Math.max(0.04, Math.abs(cx)), 0, RF, 0, 2 * PI); g.fillStyle = C[2]; g.fill();
        if (arte) drawArteDisc(g, arte, RF, cx > 0 ? eng : 0, heat);
        else drawMedalArt(g, RF, tier, cx > 0 ? cat : "", { deco: 0, engrave: cx > 0 ? eng : 0, heat, k });
        g.restore();
      } else drawFlatMedal(g, 0, 0, RF, sy, tier, cat, { engrave: eng, heat, squash, k, arte });
      g.restore();
      // calor: brilho somado em volta do disco
      if (heat > 0 && rise === 0) { g.save(); g.globalCompositeOperation = "lighter"; g.translate(x, y); g.scale(1, 0.45); glowAt(g, 0, 0, RF * 1.9, "#FF7A28", 0.55 * heat); g.restore(); }
    }
    // pancada da queda: umas fagulhas
    if (T >= FORGE.land && T < FORGE.land + 0.7) drawSparks(g, sparks[3], T - FORGE.land, hitX, flatY, q);
    // vapor do metal esfriando: nuvenzinhas macias subindo e se abrindo
    if (T > FORGE.cool[0] && T < FORGE.cool[1] + 1.2 && rise < 0.6) {
      for (let i = 0, n = Math.round(14 * q); i < n; i++) {
        const st = FORGE.cool[0] + i * 0.06, u = segT(T, st, st + 1.2); if (u <= 0 || u >= 1) continue;
        const x = hitX + (((i * 0.618) % 1) - 0.5) * 16 + sin(t * 1.3 + i) * 2.5 * u, y = flatY - 2 - u * 26;
        glowAt(g, x, y, 3 + u * 8, "#E8EAF6", 0.32 * sin(PI * u) * (1 - rise));
      }
    }
    // ---- o Folha, ferreiro: atua cada martelada
    // Na preparação ele inclina para trás e põe a língua de fora; no golpe joga o corpo para a frente; na pancada
    // fecha os olhos, estufa as bochechas, amassa e o corpo todo vibra com o tranco. Depois de cada uma reage:
    // na 1ª leva um susto com as faíscas, na 2ª sorri satisfeito, na 3ª (a grande) pula, o chapéu voa e ele
    // larga o martelo encostado na bigorna; passa a mão na testa ("ufa"), segue a medalha subindo de olhos
    // arregalados e comemora.
    const { s: hs, blur, hot, up } = hammerState(T);
    const hop = segT(T, FORGE.arrive, FORGE.arrive + 0.6), hopY = hop > 0 && hop < 1 ? -sin(PI * hop) * 14 : 0;
    const fX = lerp(FX0, 50, sstep(hop)), fY = FY + (1 - enter) * 26 + hopY;
    const forging = T < HAMMER_DOWN;
    let squash = 0, hatUp = 0, hatSpin = 0, drops = -1;
    const act = (P) => {
      // rosto de ferreiro concentrado (língua no canto) e olhando a bigorna
      P.hop = 0; P.tilt = 0; P.up = 0.25; P.browIn = -0.55; P.browY = 0.8; P.mW = 2.8; P.mCurve = 0.4; P.mOpen = 0; P.tongue = 1; P.lookX = 0.7; P.lookY = 0.6;
      const H = FORGE.hits;
      for (let i = 0; i < H.length; i++) {
        const h = H[i], big = i === H.length - 1, d = T - h;
        if (T >= h - 0.42 && T < h - 0.085) {             // prepara: inclina para trás (e na grande, pula)
          const w = sstep((T - (h - 0.42)) / 0.335);
          P.tilt = -7 * w * (big ? 1.3 : 1); P.hop = -(big ? 9 : 2.2) * sin(PI / 2 * w); P.lookY = -0.2 * w; P.up = 0.1;
          if (big) { P.browY = 1.4; P.tongue = 0; P.mW = 3.6; P.mOpen = 0.35; P.mCurve = -0.4; }
        } else if (T >= h - 0.085 && T < h) {             // desce o braço jogando o corpo
          const u = (T - (h - 0.085)) / 0.085;
          P.tilt = lerp(-7 * (big ? 1.3 : 1), 10, u); P.hop = lerp(big ? -9 : -2.2, 1.5, u * u); P.up = 0.55;
        } else if (d >= 0 && d < 0.75) {                    // a pancada e o tranco
          const dec = Math.exp(-d * 6.5);
          P.tilt = 10 * dec * cos(d * 40); P.hop = 1.5 * dec + (big ? 2 * dec : 0);
          squash = (big ? 0.6 : 0.35) * Math.max(0, 1 - d / 0.2);
          if (d < 0.14) { P.up = 1; P.mW = 3; P.mCurve = 0; P.mOpen = 0; P.cheek = 0.95; P.tongue = 0; }
          else if (i === 0 && d < 0.55) {                  // as faíscas vêm na direção dele: susto rápido
            P.up = 0; P.eyeScale = 1.2; P.pupil = 1.35; P.browY = -2.6; P.browIn = 0; P.mOpen = 0.45; P.mW = 2.6; P.mCurve = 0; P.tongue = 0; P.lookX = 0.9; P.lookY = -0.6;
            P.hop += -1.5 * sin(PI * segT(d, 0.14, 0.4));
          } else if (i === 1 && d < 0.6) {                 // gostou do resultado
            P.up = 0.35; P.browY = -1; P.browIn = 0; P.mW = 5.4; P.mCurve = 4.2; P.cheek = 0.8; P.tongue = 0;
            P.hop += -1.2 * sin(PI * segT(d, 0.2, 0.45));
          } else if (big) {                                 // a grande: arregala de orgulho
            P.up = 0; P.eyeScale = 1.12; P.browY = -2.2; P.browIn = 0; P.mOpen = 0.75; P.mW = 4.6; P.mCurve = 3; P.cheek = 0.85; P.tongue = 0;
          }
          if (big) { const hu = segT(d, 0.02, 0.62); hatUp = sin(PI * hu) * 16; hatSpin = hu * 360; }
          if (i > 0) drops = d;
        }
      }
    };
    const o = { T, t, x: fX, y: fY, scale: FS, look,
      dir: arrived ? { x: 0, y: -1 } : T >= FORGE.rise ? { x: lerp(1, 0.3, rise), y: -1 } : { x: 1, y: 0.5 },
      script: [[0, "idle"], [HAMMER_DOWN, "relieved"], [FORGE.rise, "surprised"], [FORGE.arrive, "ecstatic"], [FORGE.arrive + 1.55, "happy"], [FORGE.arrive + 2.95, "proud"]],
      edit: (P) => {
        if (!forging) return;
        act(P);
        // a mão do martelo fica presa ao mundo (onde o martelo está), por mais que o corpo incline e pule:
        // leva o ponto do mundo de volta para a grade do Folha já inclinada, amassada e no ar
        const wx = FX0 + (hs[0] - 50) * FS, wy = FY + (hs[1] - 95) * FS;
        let gx = 50 + (wx - fX) / FS, gy = 95 + (wy - fY) / FS - P.hop;
        gx = 50 + (gx - 50) / (1 + 0.10 * squash); gy = 88 + (gy - 88) / (1 - 0.12 * squash);
        const ta = -P.tilt * PI / 180, rx = gx - 50, ry = gy - 80;
        P.hR = [50 + rx * cos(ta) - ry * sin(ta), 80 + rx * sin(ta) + ry * cos(ta)]; P.shapeR = "fist"; P.rotR = 0; P.bendR = 3;
      } };
    floorLight(g, fX, 107, 26, reveal > 0 ? C[0] : "#FF8A2A", 0.24);
    // o amasso e o chapéu voando saem de act(): roda uma vez antes para saber os valores
    if (forging) act(pose("idle", t, { x: 1, y: 0.5 }, null, { tm: 9 }));
    o.squash = squash; o.ex = { hatUp, hatSpin };
    const P = sceneFolha(g, o);
    // gotas de suor voando da testa nas marteladas 2 e 3
    if (drops >= 0 && drops < 0.6) for (const sd of [-1, 1]) {
      const u = drops / 0.6, [x0, y0] = folhaPt(o, P, sd < 0 ? 22 : 80, 48), x = x0 + sd * u * 7, y = y0 - sin(PI * u) * 5 + u * 4;
      const dp = new Path2D(); dp.moveTo(x, y - 1.2); dp.quadraticCurveTo(x + 0.9, y + 0.2, x, y + 0.6); dp.quadraticCurveTo(x - 0.9, y + 0.2, x, y - 1.2);
      g.fillStyle = `rgba(126,200,255,${1 - u})`; g.fill(dp);
    }
    // o martelo: na mão enquanto forja (com rastro na descida), depois em pé encostado na bigorna
    if (forging) {
      const wx = FX0 + (hs[0] - 50) * FS, wy = FY + (hs[1] - 95) * FS;
      if (blur > 0) for (let j = 3; j >= 1; j--) {
        const u = Math.max(0, blur - j * 0.22) ** 2, A = [lerp(up[0], HP.strike[0], u), lerp(up[1], HP.strike[1], u), lerp(up[2], HP.strike[2], u)];
        drawSmithHammer(g, FX0 + (A[0] - 50) * FS, FY + (A[1] - 95) * FS, A[2], 0.16 * (1 - j / 4));
      }
      drawSmithHammer(g, wx, wy, hs[2], 1, hot * heat);
    } else if (forgeA > 0) {
      // larga o martelo: ele escorrega até ficar de pé, cabeça no chão e cabo encostado na bigorna
      const u = outC(segT(T, HAMMER_DOWN, HAMMER_DOWN + 0.25)), r = HP.rest, rx = FX0 + (r[0] - 50) * FS, ry = FY + (r[1] - 95) * FS;
      const hx = rx + cos(r[2]) * HAMMER_LEN, hy = ry + sin(r[2]) * HAMMER_LEN;   // onde a cabeça estava
      const L = HAMMER_LEAN, ang = lerp(r[2], L[2], u), cx = lerp(hx, L[0], u), cy = lerp(hy, L[1], u);
      g.save(); g.globalAlpha = forgeA; drawSmithHammer(g, cx - cos(ang) * HAMMER_LEN, cy - sin(ang) * HAMMER_LEN, ang); g.restore();
    }
    // ---- a pancada: clarão, reflexo horizontal, onda na mesa, faíscas
    for (let i = 0; i < FORGE.hits.length; i++) {
      const u = T - FORGE.hits[i]; if (u < 0 || u > 1.1) continue;
      g.save(); g.globalCompositeOperation = "lighter";
      const f = 1 - segT(u, 0, 0.2);
      if (f > 0) {
        glowAt(g, hitX, flatY - 1, 13, "#FFD27A", 0.8 * f);
        g.save(); g.translate(hitX, flatY - 1); g.scale(1, 0.07); glowAt(g, 0, 0, 30, "#FFE7B0", 0.6 * f); g.restore();
      }
      g.restore();
      shockRing(g, hitX, flatY, segT(u, 0, 0.4), "#FFD27A", 16, 0.28);
      drawSparks(g, sparks[i], u, hitX, flatY - 1.4, q);
    }
    g.restore();   // fim da câmera
    // ---- a medalha pronta no centro: fita, moldura do nível, faixa e estrelas
    if (arrived) {
      const y = MY + sin(t * 1.6) * 0.6 * reveal, pop = outBack(segT(T, FORGE.arrive, FORGE.arrive + 0.35));
      glowAt(g, MX, MY, 30, "#FFFFFF", 0.85 * (1 - segT(T, FORGE.arrive, FORGE.arrive + 0.35)));
      const rib = outC(segT(T, FORGE.arrive + 0.05, FORGE.arrive + 0.55)), top = y - R - 1.4;
      if (rib > 0 && !arte) for (const side of [-1, 1]) {
        const p = new Path2D(), x1 = MX + side * 30, y1 = lerp(top, -60, rib);
        p.moveTo(MX + side * 1.4, top); p.lineTo(x1, y1); p.lineTo(x1 - side * 9, y1); p.lineTo(MX - side * 2.4, top); p.closePath();
        g.fillStyle = side < 0 ? vGrad(g, ["#3A2FC4", "#5247E0"], -60, top) : vGrad(g, ["#2A209E", "#3A2FC4"], -60, top); g.fill(p);
        g.save(); g.clip(p); for (const kk of [0.35, 0.65]) { const ln = new Path2D(); ln.moveTo(lerp(x1, x1 - side * 9, kk), y1); ln.lineTo(MX + side * lerp(1.4, -2.4, kk), top); stroke(g, ln, C[1], 1.1); } g.restore();
      }
      if (!arte) { circle(g, C[2], 1.9, MX, top); circle(g, C[0], 1.1, MX, top); }
      g.save(); g.translate(MX, y); const ps = 1.12 - 0.12 * pop; g.scale(ps, ps);
      if (arte) {
        // a medalha forjada vira a arte: cresce do tamanho do disco até a cena inteira, com um brilho passando
        const AS = lerp(R * 2 / 0.68, 50, outBack(segT(T, FORGE.arrive, FORGE.arrive + 0.6)));
        g.drawImage(arte, -AS / 2, -AS * 0.45, AS, AS);
        const sh = T > FORGE.arrive + 0.85 ? ((T - FORGE.arrive - 0.85) * 0.36) % 1.3 : -1;
        if (sh >= 0 && sh <= 1) { g.save(); g.globalCompositeOperation = "lighter"; const x = lerp(-AS / 2, AS / 2, sh), gr = g.createLinearGradient(x - 6, -AS / 2, x + 6, AS / 2); gr.addColorStop(0, "rgba(255,255,255,0)"); gr.addColorStop(0.5, "rgba(255,255,255,0.22)"); gr.addColorStop(1, "rgba(255,255,255,0)"); g.fillStyle = gr; g.beginPath(); g.arc(0, -2, AS * 0.36, 0, 2 * PI); g.fill(); g.restore(); }
      } else drawMedalArt(g, R, tier, cat, { deco: outBack(segT(T, FORGE.deco, FORGE.deco + 0.6)), engrave: 1, heat: 0, k, shine: T > FORGE.arrive + 0.85 ? ((T - FORGE.arrive - 0.85) * 0.36) % 1.3 : -1 });
      g.restore();
      const ban = outBack(segT(T, FORGE.banner, FORGE.banner + 0.45));
      if (ban > 0 && faixa) {
        g.save(); g.translate(MX, y + (arte ? 26 : R + (tier >= 5 ? 9 : 6.5))); g.scale(ban, 1);
        for (const sd of [-1, 1]) { const p = new Path2D(); p.moveTo(sd * 13, -2.6); p.lineTo(sd * 19, -3); p.lineTo(sd * 17, 0.4); p.lineTo(sd * 19, 3.8); p.lineTo(sd * 13, 3.4); p.closePath(); g.fillStyle = C[2]; g.fill(p); }
        const bp = new Path2D(); bp.moveTo(-15, -3.6); bp.quadraticCurveTo(0, -5, 15, -3.6); bp.lineTo(15, 3.2); bp.quadraticCurveTo(0, 1.8, -15, 3.2); bp.closePath();
        g.fillStyle = hGrad(g, [C[2], C[1], C[0], C[1], C[2]], -15, 15); g.fill(bp); stroke(g, bp, "rgba(0,0,0,0.35)", 0.35);
        g.fillStyle = tier === 2 || tier === 3 || tier === 6 ? "#2A1A05" : "#FFFFFF"; g.font = "900 3.6px Nunito, system-ui, sans-serif"; g.textAlign = "center"; g.textBaseline = "middle";
        g.fillText(faixa.split("").join(" "), 0, -0.4); g.restore();
      }
      // estrelinhas em volta, só em cima e dos lados
      for (let i = 0; i < 7; i++) { const a = i * 2 * PI / 7 + 0.3 + t * 0.2, ph = (t * 0.7 + i * 0.37) % 1; if (sin(a) > 0.45) continue;   // embaixo fica a faixa: nada de estrela por cima do nome
        star4(g, MX + cos(a) * 28, y + sin(a) * 24, 2.4 * sin(PI * ph) * reveal, rgba(i % 2 ? "#FFFFFF" : C[0], sin(PI * ph))); }
    }
    g.restore();
    drawConfetti(g, el._conf, T - FORGE.arrive);
  }

  // ---- cena: sequência (a fênix)
  // 0–0,6 s   escuro com brasas; as marcas da semana aparecem num arco, como braseiros apagados;
  // 0,6–1,65  uma faísca corre o arco acendendo cada dia estudado (no dia que faltou, engasga e solta fumaça);
  // 1,65      hoje pega fogo; 1,75–2,15 um jato de fogo sobe até o centro e estoura (clarão, tremor);
  // 2,25–3    o fogo vira uma fênix que abre as asas; 2,7 o número nasce no peito dela, em fogo branco;
  // depois    a fênix respira e bate as asas devagar, o número pulsa como coração, brasas sobem.
  const SEQ = { fuse: 0.6, hit: 1.65, boom: 2.15, wings: 2.25, number: 2.7 };
  const PX = 50, PY = 30;   // o peito da fênix
  // traço de fogo: várias passadas somadas com luz (vermelho largo por fora, quase branco no meio)
  function fireLine(g, p, w, a) {
    if (a <= 0.01) return;
    g.save(); g.globalCompositeOperation = "lighter"; g.lineCap = "round"; g.lineJoin = "round";
    for (const [mul, col, al] of Q < 1 ? [[2.2, "255,120,40", 0.32], [0.6, "255,240,200", 0.9]] : [[3.4, "255,60,20", 0.1], [2.0, "255,130,40", 0.24], [1.0, "255,205,100", 0.55], [0.38, "255,250,230", 0.9]]) {
      g.strokeStyle = `rgba(${col},${al * a})`; g.lineWidth = w * mul; g.stroke(p);
    }
    g.restore();
  }
  // a asa direita (ombro em 0,0, a esquerda é o espelho): penas longas em leque, curvas, cada uma um traço de fogo
  function phoenixWing(g, t, open, flap, a) {
    const N = Q < 1 ? 7 : 10, tips = [];
    for (let i = 0; i < N; i++) {
      const f = i / (N - 1), spread = -62 + f * 78, closed = 62 + f * 18;
      const ang = (spread * open + closed * (1 - open) + flap * (1 - f * 0.5)) * PI / 180;
      const len = (20 + 18 * Math.sin(PI * (0.15 + f * 0.75))) * (0.35 + 0.65 * open);
      tips.push([2 + f * 3 + cos(ang) * len * 0.92, -3 + f * 4 + sin(ang) * len * 0.92]);
    }
    // a membrana da asa: fogo macio preenchendo entre as penas
    {
      const m = new Path2D(); m.moveTo(0, -1);
      tips.forEach(([x, y], i) => { if (i === 0) m.lineTo(x, y); else { const [px, py] = tips[i - 1]; m.quadraticCurveTo((px + x) / 2 + 2, (py + y) / 2 + 2, x, y); } });
      m.lineTo(6, 5); m.closePath();
      g.save(); g.globalCompositeOperation = "lighter";
      const mg = g.createRadialGradient(2, 0, 1, 2, 0, 40);
      mg.addColorStop(0, "rgba(255,236,170," + 0.7 * a + ")"); mg.addColorStop(0.45, "rgba(255,150,50," + 0.45 * a + ")"); mg.addColorStop(1, "rgba(220,50,30," + 0.06 * a + ")");
      g.fillStyle = mg; g.fill(m); g.restore();
    }
    for (let i = 0; i < N; i++) {
      const f = i / (N - 1);
      // ângulo de cada pena (graus, 0 = para a direita, negativo = para cima): aberta em leque, fechada junto ao corpo
      const spread = -62 + f * 78, closed = 62 + f * 18;
      const ang = (spread * open + closed * (1 - open) + flap * (1 - f * 0.5)) * PI / 180;
      const len = (20 + 18 * Math.sin(PI * (0.15 + f * 0.75))) * (0.35 + 0.65 * open);
      const ox = 2 + f * 3, oy = -3 + f * 4;                         // nascem ao longo do braço da asa
      const ex = ox + cos(ang) * len, ey = oy + sin(ang) * len;
      const bend = 0.22 + f * 0.1, mx = (ox + ex) / 2 - sin(ang) * len * bend, my = (oy + ey) / 2 + cos(ang) * len * bend * -1 + len * 0.12;
      const p = new Path2D(); p.moveTo(ox, oy); p.quadraticCurveTo(mx, my, ex + sin(t * 7 + i) * 0.6, ey + cos(t * 6 + i) * 0.6);
      fireLine(g, p, 1.55 - f * 0.5, a * (0.8 + 0.2 * sin(t * 11 + i * 1.7)));
      if (open > 0.5) glowAt(g, ex, ey, 2.2, "#FF9F3A", 0.45 * a * open);
    }
    // o braço da asa (a borda de cima, mais grossa)
    const armAng = (-48 * open + 50 * (1 - open) + flap * 0.8) * PI / 180;
    const arm = new Path2D(); arm.moveTo(0, -1); arm.quadraticCurveTo(cos(armAng) * 9 - 2, sin(armAng) * 9 - 4, cos(armAng) * 18, sin(armAng) * 18);
    fireLine(g, arm, 1.7, a);
  }
  function drawPhoenix(g, t, open, flap, a, k) {
    if (a <= 0.01) return;
    // calor atrás
    glowAt(g, PX, PY, 40 * (0.5 + 0.5 * open), "#FF6A1A", 0.35 * a);
    glowAt(g, PX, PY + 2, 18, "#FFB13B", 0.45 * a);
    // asas
    for (const side of [-1, 1]) { g.save(); g.translate(PX + side * 3.5, PY - 5); g.scale(side, 1); phoenixWing(g, t, open, flap, a); g.restore(); }
    // cauda: três plumas que descem e enrolam
    for (const [sx, len, curl] of [[-1, 18, -1], [0, 23, 1], [1, 18, 1]]) {
      const p = new Path2D(); p.moveTo(PX + sx * 1.5, PY + 7);
      p.bezierCurveTo(PX + sx * 6, PY + 14, PX + sx * 10 + sin(t * 2 + sx) * 2, PY + len * 0.75, PX + sx * 4 + curl * 5, PY + len);
      p.quadraticCurveTo(PX + sx * 4 + curl * 9, PY + len + 3, PX + sx * 4 + curl * 6, PY + len - 2);
      fireLine(g, p, 1.0, a * open);
    }
    // corpo: gota de fogo
    g.save(); g.globalCompositeOperation = "lighter";
    const body = new Path2D(); body.moveTo(PX, PY - 13); body.bezierCurveTo(PX + 6, PY - 7, PX + 7, PY + 5, PX, PY + 10); body.bezierCurveTo(PX - 7, PY + 5, PX - 6, PY - 7, PX, PY - 13); body.closePath();
    const bg = g.createRadialGradient(PX, PY, 0, PX, PY, 13); bg.addColorStop(0, `rgba(255,250,225,${0.9 * a})`); bg.addColorStop(0.45, `rgba(255,180,70,${0.7 * a})`); bg.addColorStop(1, `rgba(255,70,20,${0.25 * a})`);
    g.fillStyle = bg; g.fill(body); g.restore();
    fireLine(g, body, 0.7, a);
    // cabeça, crista e o bico
    const hy = PY - 16;
    g.save(); g.globalCompositeOperation = "lighter"; glowAt(g, PX, hy, 5, "#FFE07A", 0.9 * a); g.restore();
    for (const [dx, h] of [[-2.2, 7], [0, 9], [2.2, 7]]) { const c = new Path2D(); c.moveTo(PX + dx * 0.4, hy - 1.5); c.quadraticCurveTo(PX + dx * 1.4, hy - h * 0.6, PX + dx * 1.8 + sin(t * 5 + dx) * 0.8, hy - h); fireLine(g, c, 0.55, a); }
    const beak = new Path2D(); beak.moveTo(PX - 1.2, hy + 1); beak.lineTo(PX, hy + 3.4); beak.lineTo(PX + 1.2, hy + 1); fireLine(g, beak, 0.4, a);
    circle(g, `rgba(255,255,255,${a})`, 0.55, PX - 1.3, hy - 0.2); circle(g, `rgba(255,255,255,${a})`, 0.55, PX + 1.3, hy - 0.2);
  }
  // número em fogo: contorno vermelho escuro, brilho somado com luz e miolo quase branco, pulsando
  function fireNumber(g, txt, x, y, size, a, beat) {
    if (a <= 0.01) return;
    g.save(); g.globalAlpha = a; g.font = `900 ${size}px Nunito, "Segoe UI", system-ui, sans-serif`; g.textAlign = "center"; g.textBaseline = "middle"; g.lineJoin = "round";
    g.translate(x, y); g.scale(beat, beat);
    g.globalCompositeOperation = "lighter";
    for (const [w, col] of [[size * 0.5, "rgba(255,80,20,0.18)"], [size * 0.3, "rgba(255,140,40,0.3)"]]) { g.strokeStyle = col; g.lineWidth = w; g.strokeText(txt, 0, 0); }
    g.globalCompositeOperation = "source-over";
    g.strokeStyle = "#7A1A08"; g.lineWidth = size * 0.12; g.strokeText(txt, 0, 0);
    const gr = g.createLinearGradient(0, -size * 0.5, 0, size * 0.5); gr.addColorStop(0, "#FFFFFF"); gr.addColorStop(0.45, "#FFF1BF"); gr.addColorStop(0.75, "#FFC24A"); gr.addColorStop(1, "#FF7A1A");
    g.fillStyle = gr; g.fillText(txt, 0, 0);
    g.restore();
  }
  function cenaSequencia(g, T, t, el) {
    const days = Math.max(1, parseInt(el.getAttribute("dias") || "1", 10)), look = el._look(), k = this_k(el);
    let week = (el.getAttribute("semana") || "").split(",").filter(Boolean).map((w) => [w.slice(0, -1) || w, w.slice(-1) === "1"]);
    if (!week.length) week = [["H", true]];
    const n = week.length;
    // o arco da semana, logo acima do Folha
    const ax = (i) => 50 + (i - (n - 1) / 2) * 12, ay = (x) => 66 - 7 * (1 - ((x - 50) / 42) ** 2);
    const shake = T > SEQ.boom && T < SEQ.boom + 0.5 ? (1 - (T - SEQ.boom) / 0.5) * 1.4 : T > SEQ.wings + 0.3 && T < SEQ.wings + 0.6 ? 0.6 : 0;
    g.save(); g.translate(sin(t * 93) * shake, cos(t * 71) * shake);
    // ---- fundo: brilho quente e brasas (somem suave antes da borda)
    const reach = el._reach(), fire = outC(segT(T, SEQ.boom, SEQ.boom + 0.6));
    backdrop(g, el, PX, PY + 10, reach, reach * 0.95, (b) => {
      glowAt(b, PX, PY + 6, 62, fire > 0 ? "#FF6A1A" : "#3A2A80", fire > 0 ? 0.22 + 0.1 * sin(t * 3) : 0.25);
      glowAt(b, PX, 70, 40, "#FF8A2A", 0.12 + 0.1 * segT(T, SEQ.fuse, SEQ.hit));
      sceneRays(b, PX, PY, 16, 90, "#FFB13B", fire * (0.5 + 0.15 * sin(t * 2)), t * 0.1, 0.05);
      shockRing(b, PX, PY, segT(T, SEQ.boom, SEQ.boom + 0.7), "#FFE07A", 52);
      shockRing(b, PX, PY, segT(T, SEQ.wings + 0.3, SEQ.wings + 1.1), "#FF8A2A", 70);
      for (let i = 0, m = Math.round(26 * Q); i < m; i++) { const u = (t * (0.08 + (i % 5) * 0.015) + i * 0.137) % 1, x = ((i * 37) % 130) - 15 + sin(t * 0.7 + i) * 4, y = 115 - u * 135, c = fireColor(0.3 + (i % 3) * 0.15); glowAt(b, x, y, 1.4, `rgb(${c})`, (1 - u) * (0.35 + 0.4 * fire)); }
    });
    // ---- as marcas da semana: braseiros que acendem com a faísca do pavio
    const fuseU = segT(T, SEQ.fuse, SEQ.hit), fuseX = lerp(ax(0) - 6, ax(n - 1), fuseU);
    // o pavio: linha de brasa ligando os braseiros, que vai queimando
    const fuse = new Path2D(); for (let x = ax(0) - 6; x <= ax(n - 1); x += 1) { const y = ay(x) + 4; x === ax(0) - 6 ? fuse.moveTo(x, y) : fuse.lineTo(x, y); }
    stroke(g, fuse, "rgba(255,255,255,0.12)", 0.5);
    if (fuseU > 0) { g.save(); const cl = new Path2D(); cl.rect(0, 0, fuseX, 110); g.clip(cl); stroke(g, fuse, "rgba(255,150,60,0.55)", 0.6); g.restore(); }
    week.forEach(([letter, done], i) => {
      const today = i === n - 1, x = ax(i), y = ay(x);
      const reached = T > SEQ.fuse && fuseX >= x - 0.5, at = SEQ.fuse + (x - ax(0) + 6) / (ax(n - 1) - ax(0) + 6) * (SEQ.hit - SEQ.fuse);
      const lit = today ? segT(T, SEQ.hit, SEQ.hit + 0.2) : done && reached ? outBack(segT(T, at, at + 0.25)) : 0;
      // braseiro: tigela de pedra
      g.save(); g.translate(x, y);
      drawBrazier(g, lit);
      if (lit > 0) {
        // a chama do dia (hoje maior)
        const fh = (today ? 13 : 7) * lit * (1 + 0.06 * sin(t * 9 + i));
        g.translate(0, 1.2); drawSmallFlame(g, t + i, fh, today ? 3.6 : 2.4);
      } else if (!done && reached && !today) {
        // dia que faltou: a faísca engasga e sai fumaça
        for (let j = 0; j < 3; j++) { const u = (t * 0.6 + j / 3) % 1; circle(g, `rgba(180,175,210,${0.35 * (1 - u)})`, 0.8 + u * 1.6, sin(t + j) * 1.2, -u * 8); }
      }
      g.restore();
      g.fillStyle = lit > 0 ? "rgba(255,225,170,0.95)" : "rgba(255,255,255,0.45)"; g.font = "900 3px Nunito, system-ui, sans-serif"; g.textAlign = "center"; g.textBaseline = "middle";
      g.fillText(today ? "HOJE" : letter, x, y + 10.4);
    });
    // a faísca correndo o pavio
    if (fuseU > 0 && fuseU < 1) { const y = ay(fuseX) + 4; glowAt(g, fuseX, y, 5, "#FFE07A", 0.9); circle(g, "#FFFFFF", 0.8, fuseX, y); for (let j = 0; j < 5; j++) { const u = (t * 4 + j / 5) % 1; circle(g, `rgba(255,${180 + j * 12},80,${1 - u})`, 0.35, fuseX - u * 3 + sin(j * 7) * u * 2, y - u * 3 + cos(j * 5) * u * 2); } }
    // ---- o jato de fogo: de hoje até o peito da fênix, estourando numa bola de fogo
    const lastX = ax(n - 1), lastY = ay(lastX) - 4, jet = segT(T, SEQ.hit + 0.1, SEQ.boom);
    if (jet > 0 && T < SEQ.boom + 0.25) {
      const at = (q) => { const v = 1 - q; return [v * v * lastX + 2 * v * q * (lastX + 6) + q * q * PX, v * v * lastY + 2 * v * q * (PY + 18) + q * q * PY]; };
      const p = new Path2D(); for (let q = 0; q <= outC(jet); q += 0.04) { const [x, y] = at(q); q === 0 ? p.moveTo(x, y) : p.lineTo(x, y); }
      fireLine(g, p, 1.4, 1 - segT(T, SEQ.boom, SEQ.boom + 0.25));
      const [hx, hy] = at(outC(jet)); glowAt(g, hx, hy, 6, "#FFE07A", 0.9); circle(g, "#FFFFFF", 1.2, hx, hy);
    }
    const ball = segT(T, SEQ.boom, SEQ.boom + 0.35);
    if (ball > 0 && ball < 1) { glowAt(g, PX, PY, 10 + 30 * ball, "#FFFFFF", 0.9 * (1 - ball)); glowAt(g, PX, PY, 8 + 20 * ball, "#FFB13B", 1 - ball); }
    // ---- a fênix e o número
    const born = segT(T, SEQ.boom, SEQ.boom + 0.3), open = outBack(segT(T, SEQ.wings, SEQ.wings + 0.75));
    const flap = T < SEQ.wings + 0.75 ? 0 : sin((T - SEQ.wings - 0.75) * 2.4) * 10 + (T < SEQ.wings + 1.6 ? sin(PI * segT(T, SEQ.wings + 0.75, SEQ.wings + 1.6)) * -25 : 0);
    drawPhoenix(g, t, open, flap, born, k);
    const num = segT(T, SEQ.number, SEQ.number + 0.5), beat = 1 + 0.06 * Math.max(0, sin(t * 4.2)) ** 6 + (num < 1 ? (1 - outBack(num)) * -0.4 : 0);
    fireNumber(g, String(days), PX, PY + 1, String(days).length > 2 ? 13 : String(days).length > 1 ? 17 : 20, clamp(num * 2, 0, 1), beat);
    // fagulhas soltas da fênix
    if (born > 0) for (let i = 0, m = Math.round(16 * Q); i < m; i++) { const u = (t * (0.4 + (i % 4) * 0.08) + i * 0.0625) % 1, sd = ((i * 0.618) % 1 - 0.5) * 2, x = PX + sd * 38 * (0.4 + u), y = PY - 6 - u * 34 + sd * sd * 8; const c = fireColor(0.15 + u * 0.6); circle(g, `rgba(${c},${(1 - u) * open})`, 0.3 + 0.3 * (1 - u), x, y); }
    g.restore();
    // ---- o Folha: olha a faísca correndo, leva um susto no estouro e comemora em chamas
    const o = { T, t, x: 50, y: 110 + (1 - outC(segT(T, 0, 0.5))) * 30, scale: 0.46, look, dir: { x: lerp(-0.8, 0.8, fuseU), y: T < SEQ.hit ? 0.2 : -1 },
      script: [[0, "idle"], [SEQ.hit, "surprised"], [SEQ.wings, "ecstatic"], [4.2, "happy"], [5.6, "proud"]] };
    floorLight(g, 50, 109, 30, "#FF8A2A", 0.2 + 0.15 * fire);
    sceneFolha(g, o);
    drawConfetti(g, el._conf, T - SEQ.number);
  }
  // chama pequena dos braseiros (as mesmas labaredas da chama grande, em miniatura)
  // As cores da chama: o fogo da fênix e a chama azul do marca-página do Folha (o dia salvo).
  const FLAME = {
    fire: { glow: "#FF7A1F", outer: ["#FF2E14", "#FF7A1F"], mid: ["#FF9A1F", "#FFD54A"], core: "#FFF6D8", spark: "255,190,90" },
    blue: { glow: "#3D9BFF", outer: ["#1238FF", "#2F8BFF"], mid: ["#3FB4FF", "#9FE6FF"], core: "#F2FCFF", spark: "160,220,255" },
  };
  // forma de chama: gota com a ponta que balança
  function flameShape(h, w, wob) {
    const p = new Path2D();
    p.moveTo(0, 0.6);
    p.bezierCurveTo(w * 0.95, 0.4, w * 0.85, -h * 0.42, wob * 0.6, -h * 0.78);
    p.quadraticCurveTo(wob, -h * 0.9, wob * 1.15, -h);
    p.quadraticCurveTo(wob * 0.2, -h * 0.82, -w * 0.25 + wob * 0.3, -h * 0.7);
    p.bezierCurveTo(-w * 0.9, -h * 0.4, -w * 0.95, 0.4, 0, 0.6);
    p.closePath();
    return p;
  }
  // a chama dos braseiros: três camadas (vermelho por fora, amarelo, miolo quase branco), a ponta dançando,
  // um brilho em volta e fagulhas subindo. palette = "fire" ou "blue".
  function drawSmallFlame(g, t, h, w, palette = "fire") {
    if (h <= 0.3) return;
    const C = FLAME[palette] || FLAME.fire;
    glowAt(g, 0, -h * 0.35, h * 1.15, C.glow, 0.42);
    const wob = sin(t * 7.3) * w * 0.35 + sin(t * 12.1) * w * 0.15;
    const layer = (hh, ww, wb, top, bottom, a) => {
      const p = flameShape(hh, ww, wb);
      const gr = g.createLinearGradient(0, 0.6, 0, -hh);
      gr.addColorStop(0, bottom); gr.addColorStop(1, top);
      g.globalAlpha = a; g.fillStyle = gr; g.fill(p); g.globalAlpha = 1;
    };
    g.save();
    layer(h * (1 + 0.05 * sin(t * 9)), w, wob, C.outer[1], C.outer[0], 0.95);
    g.globalCompositeOperation = "lighter";
    layer(h * 0.72, w * 0.68, wob * 0.7, C.mid[1], C.mid[0], 0.85);
    layer(h * 0.42, w * 0.38, wob * 0.4, C.core, C.mid[1], 0.95);
    // fagulhas
    for (let i = 0, n = Math.round(5 * Q); i < n; i++) {
      const u = (t * (0.7 + (i % 3) * 0.2) + i * 0.27) % 1;
      circle(g, `rgba(${C.spark},${(1 - u) * 0.9})`, 0.28 * (1 - u) + 0.08, sin(t * 3 + i * 2.1) * w * 0.6 * u, -h * (0.7 + u * 0.9));
    }
    g.restore();
  }
  // o braseiro: tigela de metal com borda brilhante, brasas por dentro e o tripé. glow 0..1 acende as brasas.
  function drawBrazier(g, glow, palette = "fire") {
    const C = FLAME[palette] || FLAME.fire;
    // tripé
    g.strokeStyle = "#1E1A3C"; g.lineWidth = 0.9; g.lineCap = "round";
    g.beginPath(); g.moveTo(-2.6, 3.6); g.lineTo(-3.6, 8.2); g.moveTo(2.6, 3.6); g.lineTo(3.6, 8.2); g.moveTo(0, 4.8); g.lineTo(0, 8.4); g.stroke();
    g.strokeStyle = "#4A4478"; g.lineWidth = 0.35;
    g.beginPath(); g.moveTo(-2.4, 3.8); g.lineTo(-3.3, 7.9); g.moveTo(2.4, 3.8); g.lineTo(3.3, 7.9); g.stroke();
    // tigela
    const bowl = new Path2D(); bowl.moveTo(-4.6, 0.6); bowl.bezierCurveTo(-4.2, 4.6, 4.2, 4.6, 4.6, 0.6); bowl.closePath();
    const bg = g.createLinearGradient(-4.6, 0, 4.6, 0); bg.addColorStop(0, "#2A2550"); bg.addColorStop(0.45, "#5E5792"); bg.addColorStop(1, "#231F45");
    g.fillStyle = bg; g.fill(bowl);
    stroke(g, bowl, "rgba(10,8,30,0.6)", 0.35);
    // brasas por dentro (acesas com a chama)
    g.save(); g.beginPath(); g.ellipse(0, 0.7, 4.3, 1.05, 0, 0, 2 * PI); g.clip();
    g.fillStyle = "#15112E"; g.fillRect(-5, -1, 10, 3);
    if (glow > 0) {
      const cg = g.createRadialGradient(0, 0.8, 0, 0, 0.8, 4.3);
      cg.addColorStop(0, palette === "blue" ? `rgba(200,240,255,${glow})` : `rgba(255,230,150,${glow})`);
      cg.addColorStop(0.5, palette === "blue" ? `rgba(60,150,255,${0.8 * glow})` : `rgba(255,120,30,${0.8 * glow})`);
      cg.addColorStop(1, "rgba(0,0,0,0)");
      g.fillStyle = cg; g.fillRect(-5, -1, 10, 3);
      for (let i = 0; i < 4; i++) circle(g, palette === "blue" ? `rgba(220,245,255,${0.7 * glow})` : `rgba(255,220,140,${0.7 * glow})`, 0.35, -2.4 + i * 1.6, 0.9 + (i % 2) * 0.3);
    }
    g.restore();
    // borda de metal com brilho
    g.beginPath(); g.ellipse(0, 0.6, 4.6, 1.15, 0, 0, 2 * PI);
    g.strokeStyle = "#8C86C0"; g.lineWidth = 0.55; g.stroke();
    g.beginPath(); g.ellipse(0, 0.6, 4.6, 1.15, 0, PI * 1.05, PI * 1.6);
    g.strokeStyle = "rgba(255,255,255,0.65)"; g.lineWidth = 0.35; g.stroke();
    if (glow > 0) glowAt(g, 0, 0.4, 6, C.glow, 0.28 * glow);
  }


  // ---- cena: peça nova (o presente)
  // 0–0,45  a caixa cai do alto e quica (poeira no chão); o Folha entra pela direita e leva um susto;
  // 1–1,8   a caixa se mexe sozinha três vezes, com luz vazando pela tampa; o Folha se anima a cada uma;
  // 1,9–2,4 ele puxa a ponta da fita: o laço se desfaz e a fita escorrega;
  // 2,45    a tampa pula longe girando, sai uma coluna de luz e uma fonte de faíscas;
  // 2,55–3,1 a carta da peça sobe da caixa girando (verso, depois frente com o Folha vestindo a peça);
  // 3,1–3,4 a carta flutua com o brilho holográfico correndo; 3,4–3,65 voa até o Folha;
  // 3,65    clarão: ele já está vestindo a peça, pula para o centro e comemora.
  const WRAP = {
    comum: { paper: ["#B9D3FF", "#6E97E6", "#3D63B8"], rib: ["#FFFFFF", "#E4E8F6", "#B9C0DA"], dots: "rgba(255,255,255,0.35)" },
    rara: { paper: ["#9FD0FF", "#3D8BEA", "#1F55A8"], rib: ["#FFF1BF", "#F5B83D", "#B07510"], dots: "rgba(255,240,190,0.4)" },
    epica: { paper: ["#D9C2FF", "#9563FF", "#5A2BC4"], rib: ["#FFF1BF", "#F5B83D", "#B07510"], dots: "rgba(255,240,190,0.4)" },
    lendaria: { paper: ["#FFF0BF", "#F5B83D", "#B87C0E"], rib: ["#FF8DA0", "#E2364F", "#9A1630"], dots: "rgba(255,255,255,0.45)" },
  };
  const RARITY_NAME = { comum: "COMUM", rara: "RARA", epica: "ÉPICA", lendaria: "LENDÁRIA" };
  const GIFT = { W: 30, H: 19, DX: 7, DY: -6, LID: 5 };
  // estampa do papel: estrelinhas e bolinhas alternadas, recortadas na face
  function giftPattern(g, x0, y0, w, h, col) {
    for (let y = y0 + 2.5, r = 0; y < y0 + h; y += 4.2, r++) for (let x = x0 + (r % 2 ? 1.5 : 3.6); x < x0 + w; x += 4.4) {
      if ((r + Math.round(x)) % 3 === 0) star4(g, x, y, 1.1, col); else circle(g, col, 0.42, x, y);
    }
  }
  // a caixa em três quartos: frente, lado e (sem tampa) a boca escura com a luz lá dentro
  function drawGiftBox(g, wrap, open, inner, t) {
    const { W, H, DX, DY } = GIFT, x0 = -W / 2, x1 = W / 2, P = wrap.paper, Rb = wrap.rib;
    // lado direito, mais escuro
    const side = new Path2D(); side.moveTo(x1, 0); side.lineTo(x1 + DX, DY); side.lineTo(x1 + DX, DY - H); side.lineTo(x1, -H); side.closePath();
    g.fillStyle = vGrad(g, [P[1], P[2]], -H, 0); g.fill(side); g.fillStyle = "rgba(10,5,40,0.28)"; g.fill(side);
    // frente com a estampa e um brilho de papel
    const front = new Path2D(); front.rect(x0, -H, W, H);
    g.fillStyle = vGrad(g, [P[0], P[1], P[2]], -H, 0); g.fill(front);
    g.save(); g.clip(front); giftPattern(g, x0, -H, W, H, wrap.dots);
    const sh = g.createLinearGradient(x0, -H, x1, 0); sh.addColorStop(0, "rgba(255,255,255,0.28)"); sh.addColorStop(0.45, "rgba(255,255,255,0)"); sh.addColorStop(1, "rgba(0,0,30,0.18)"); g.fillStyle = sh; g.fillRect(x0, -H, W, H);
    g.restore();
    // fita: vertical na frente e horizontal no lado (some quando desamarra)
    if (inner.ribbon > 0) {
      g.save(); g.globalAlpha = inner.ribbon;
      g.fillStyle = hGrad(g, [Rb[2], Rb[0], Rb[1], Rb[2]], -2.8, 2.8); g.fillRect(-2.8, -H, 5.6, H);
      const sb = new Path2D(); sb.moveTo(x1, -H * 0.55 - 2.4); sb.lineTo(x1 + DX, DY - H * 0.55 - 2.4); sb.lineTo(x1 + DX, DY - H * 0.55 + 2.4); sb.lineTo(x1, -H * 0.55 + 2.4); sb.closePath();
      g.fillStyle = Rb[2]; g.fill(sb);
      line(g, "rgba(255,255,255,0.6)", -1.6, -H, -1.6, 0, 0.35);
      g.restore();
    }
    stroke(g, front, "rgba(10,5,40,0.35)", 0.3); stroke(g, side, "rgba(10,5,40,0.35)", 0.3);
    // aberta: a boca da caixa, funda e cheia de luz
    if (open > 0) {
      const mouth = new Path2D(); mouth.moveTo(x0, -H); mouth.lineTo(x0 + DX, DY - H); mouth.lineTo(x1 + DX, DY - H); mouth.lineTo(x1, -H); mouth.closePath();
      g.fillStyle = "#1A1240"; g.fill(mouth);
      g.save(); g.clip(mouth); glowAt(g, DX / 2, -H + DY / 2, 22, inner.glow, open * (0.85 + 0.15 * sin(t * 14))); g.restore();
    }
  }
  // a tampa: faixa da frente, faixa do lado e o tampo, com a fita cruzada e o laço de cetim
  function drawGiftLid(g, wrap, bow, untie, t) {
    const { W, DX, DY, LID } = GIFT, ow = W + 2, x0 = -ow / 2, x1 = ow / 2, P = wrap.paper, Rb = wrap.rib;
    const top = new Path2D(); top.moveTo(x0, -LID); top.lineTo(x0 + DX, DY - LID); top.lineTo(x1 + DX, DY - LID); top.lineTo(x1, -LID); top.closePath();
    g.fillStyle = vGrad(g, [P[0], P[1]], DY - LID, -LID); g.fill(top); g.save(); g.clip(top); g.fillStyle = "rgba(255,255,255,0.22)"; g.fill(top); g.restore();
    const side = new Path2D(); side.moveTo(x1, 0); side.lineTo(x1 + DX, DY); side.lineTo(x1 + DX, DY - LID); side.lineTo(x1, -LID); side.closePath();
    g.fillStyle = P[2]; g.fill(side); g.fillStyle = "rgba(10,5,40,0.25)"; g.fill(side);
    const front = new Path2D(); front.rect(x0, -LID, ow, LID);
    g.fillStyle = vGrad(g, [P[0], P[1]], -LID, 0); g.fill(front);
    g.save(); g.clip(front); giftPattern(g, x0, -LID, ow, LID, wrap.dots); g.restore();
    line(g, "rgba(10,5,40,0.3)", x0, 0, x1, 0, 0.5);
    if (bow > 0) {
      g.save(); g.globalAlpha = bow;
      // fita cruzada no tampo e descendo na faixa da frente
      g.fillStyle = hGrad(g, [Rb[2], Rb[0], Rb[1], Rb[2]], -2.8, 2.8); g.fillRect(-2.8, -LID, 5.6, LID);
      const cr1 = new Path2D(); cr1.moveTo(-2.8, -LID); cr1.lineTo(-2.8 + DX, DY - LID); cr1.lineTo(2.8 + DX, DY - LID); cr1.lineTo(2.8, -LID); cr1.closePath(); g.fillStyle = Rb[1]; g.fill(cr1);
      const cr2 = new Path2D(); cr2.moveTo(x0 + DX / 2, DY / 2 - LID - 1.2); cr2.lineTo(x1 + DX / 2, DY / 2 - LID - 1.2); cr2.lineTo(x1 + DX / 2, DY / 2 - LID + 1.2); cr2.lineTo(x0 + DX / 2, DY / 2 - LID + 1.2); cr2.closePath(); g.fillStyle = Rb[1]; g.fill(cr2);
      g.restore();
      // o laço no centro do tampo: duas alças com dobra, nó e as pontas; desamarrando, as alças murcham
      const k = 1 - untie, bx = DX / 2, by = DY / 2 - LID;
      if (k > 0.02) {
        g.save(); g.globalAlpha = bow;
        for (const s of [-1, 1]) {
          g.save(); g.translate(bx, by); g.rotate(s * (0.35 + untie * 0.9) + sin(t * 3 + s) * 0.03); g.scale(k, k);
          const loop = new Path2D(); loop.moveTo(0, 0); loop.bezierCurveTo(s * 3, -10, s * 15, -9.5, s * 12.5, -1.6); loop.bezierCurveTo(s * 10.5, 2.6, s * 4, 1.6, 0, 0); loop.closePath();
          const lg = g.createLinearGradient(0, -9, s * 12, 2); lg.addColorStop(0, Rb[0]); lg.addColorStop(0.55, Rb[1]); lg.addColorStop(1, Rb[2]); g.fillStyle = lg; g.fill(loop);
          // dobra interna (sombra) e brilho de cetim
          const fold = new Path2D(); fold.moveTo(s * 1.2, -0.6); fold.bezierCurveTo(s * 4, -5.5, s * 10, -6, s * 10.6, -2.2); stroke(g, fold, "rgba(0,0,0,0.22)", 0.7);
          const gl = new Path2D(); gl.moveTo(s * 2.6, -4.4); gl.bezierCurveTo(s * 5, -8, s * 9, -8, s * 11.2, -5.2); stroke(g, gl, "rgba(255,255,255,0.55)", 0.45);
          stroke(g, loop, "rgba(0,0,0,0.25)", 0.3);
          g.restore();
        }
        g.beginPath(); g.ellipse(bx, by - 0.4, 2.6 * k, 2.2 * k, 0, 0, 2 * PI); g.fillStyle = vGrad(g, [Rb[0], Rb[2]], by - 2.6, by + 2); g.fill();
        g.restore();
      }
    }
  }
  // a carta da peça (centro em 0,0): moldura da raridade, o Folha vestindo a peça, nome e o brilho holográfico.
  // flip: -1..1 (cos do giro); de costas mostra o verso com a marca.
  function drawPieceCard(g, flip, rarity, img, name, t, shine) {
    const w = 26, h = 34, r = 3, glow = RARITY_GLOW[rarity] || RARITY_GLOW.comum, wrap = WRAP[rarity] || WRAP.comum;
    g.save(); g.scale(Math.max(0.03, Math.abs(flip)), 1);
    const card = new Path2D(); card.roundRect(-w / 2, -h / 2, w, h, r);
    if (Q === 1) { g.save(); g.shadowColor = rgba(glow[0], 0.9); g.shadowBlur = 16 * (g.getTransform().a || 1) / 4; g.fillStyle = "#000"; g.fill(card); g.restore(); }
    // moldura de metal da raridade
    const fg = g.createLinearGradient(-w / 2, -h / 2, w / 2, h / 2); fg.addColorStop(0, wrap.paper[0]); fg.addColorStop(0.5, glow[0]); fg.addColorStop(1, wrap.paper[2]); g.fillStyle = fg; g.fill(card);
    const inner = new Path2D(); inner.roundRect(-w / 2 + 1.4, -h / 2 + 1.4, w - 2.8, h - 2.8, r - 1);
    if (flip >= 0) {
      const bg = g.createRadialGradient(0, -3, 1, 0, -2, h * 0.7); bg.addColorStop(0, "#4B3FD8"); bg.addColorStop(0.6, "#241A80"); bg.addColorStop(1, "#110C3E"); g.fillStyle = bg; g.fill(inner);
      g.save(); g.clip(inner);
      sceneRays(g, 0, -2, 12, 26, glow[0], 0.55, t * 0.4, 0.09);
      glowAt(g, 0, -2, 12, glow[0], 0.5);
      if (img && img.complete && img.naturalWidth) g.drawImage(img, -12, -15.5, 24, 24);
      // faixa com o nome embaixo
      g.fillStyle = "rgba(10,6,40,0.72)"; g.fillRect(-w / 2, h / 2 - 8.6, w, 8.6);
      g.restore();
      // o nome cabe na carta: diminui a letra se for comprido
      g.fillStyle = "#FFFFFF"; g.textAlign = "center"; g.textBaseline = "middle";
      let fsz = 3; g.font = `900 ${fsz}px Nunito, system-ui, sans-serif`; const tw = g.measureText(name).width; if (tw > w - 4) { fsz *= (w - 4) / tw; g.font = `900 ${fsz}px Nunito, system-ui, sans-serif`; }
      g.fillText(name, 0, h / 2 - 5.4);
      g.fillStyle = glow[0]; g.font = "900 2.1px Nunito, system-ui, sans-serif"; g.fillText((RARITY_NAME[rarity] || "").split("").join(" "), 0, h / 2 - 2.6);
      // brilho holográfico correndo na diagonal
      if (shine >= 0 && shine <= 1) {
        g.save(); g.clip(card); g.globalCompositeOperation = "lighter"; g.rotate(0.5);
        const x = lerp(-w * 1.2, w * 1.2, shine), hg = g.createLinearGradient(x - 7, 0, x + 7, 0);
        hg.addColorStop(0, "rgba(255,255,255,0)"); hg.addColorStop(0.35, "rgba(160,220,255,0.25)"); hg.addColorStop(0.5, "rgba(255,255,255,0.55)"); hg.addColorStop(0.65, "rgba(255,170,230,0.25)"); hg.addColorStop(1, "rgba(255,255,255,0)");
        g.fillStyle = hg; g.fillRect(x - 7, -h, 14, h * 2); g.restore();
      }
    } else {
      // verso: índigo com um losango de folhinhas e a estrela no centro
      g.fillStyle = vGrad(g, ["#3A2FC4", "#1D1670"], -h / 2, h / 2); g.fill(inner);
      g.save(); g.clip(inner);
      for (let y = -h / 2; y < h / 2; y += 3.2) for (let x = -w / 2 + ((y / 3.2) % 2 ? 1.6 : 0); x < w / 2; x += 3.2) circle(g, "rgba(255,255,255,0.08)", 0.5, x, y);
      g.restore();
      const dm = new Path2D(); dm.moveTo(0, -9); dm.lineTo(7, 0); dm.lineTo(0, 9); dm.lineTo(-7, 0); dm.closePath(); stroke(g, dm, rgba(glow[0], 0.9), 0.6);
      star4(g, 0, 0, 5, glow[0]); circle(g, "#FFFFFF", 0.9, 0, 0);
    }
    stroke(g, card, "rgba(255,255,255,0.55)", 0.35);
    g.restore();
  }
  // As peças do presente: pieces='[{"slot":"hat","piece":"coroa","rarity":"lendaria","name":"Coroa"}, ...]' (várias
  // de uma vez, quando uma conquista libera mais de uma) ou, para uma só, piece-slot/piece/rarity/piece-name.
  const RARITY_ORDER = ["comum", "rara", "epica", "lendaria"];
  function giftPieces(el) {
    if (el._pieces) return el._pieces;
    let list = [];
    try { const raw = el.getAttribute("pieces"); if (raw) list = JSON.parse(raw).filter((x) => x && x.slot && x.piece); } catch (e) { list = []; }
    if (!list.length && el.getAttribute("piece")) list = [{ slot: el.getAttribute("piece-slot"), piece: el.getAttribute("piece"), rarity: el.getAttribute("rarity") || "comum", name: el.getAttribute("piece-name") || "Peça nova" }];
    list = list.map((x) => ({ ...x, rarity: RARITY_ORDER.includes(x.rarity) ? x.rarity : "comum", name: x.name || "Peça nova" }));
    return (el._pieces = list);
  }
  // Onde fica cada carta depois de subir: uma no centro; várias, lado a lado num leque, menores.
  function cardSpot(i, n) {
    if (n <= 1) return { x: 50, y: 46, sc: 1, rot: 0 };
    const sc = Math.min(0.9, 92 / (n * 27)), gap = Math.min(26 * sc + 2.4, 84 / (n - 1)), c = i - (n - 1) / 2;
    return { x: 50 + c * gap, y: 47 + c * c * (n > 3 ? 1.1 : 1.6), sc, rot: c * (n > 3 ? 0.07 : 0.09) };
  }
  function cenaPeca(g, T, t, el) {
    const list = giftPieces(el), n = Math.max(1, list.length);
    const rarity = list.reduce((best, x) => (RARITY_ORDER.indexOf(x.rarity) > RARITY_ORDER.indexOf(best) ? x.rarity : best), list.length ? list[0].rarity : "comum");
    const glow = RARITY_GLOW[rarity] || RARITY_GLOW.comum, wrap = WRAP[rarity] || WRAP.comum, legendary = rarity === "lendaria", q = Q;
    // as cartas sobem uma atrás da outra e ficam um bom tempo paradas para a pessoa ver (mais tempo quando são várias)
    const hold = n > 1 ? 1.55 + 0.12 * Math.min(n, 6) : 1.25, riseEnd = 3.3 + 0.12 * (n - 1);
    const K = { land: 0.45, wig: [1.0, 1.38, 1.74], pull: 1.95, untie: [2.1, 2.4], pop: 2.45, rise: [2.55, riseEnd], fly: [riseEnd + hold, riseEnd + hold + 0.32] }, flashT = K.fly[1], worn = T >= flashT;
    const slot = list.length ? list[0].slot : null;
    const look = el._look(worn && list.length ? Object.fromEntries(list.map((x) => [x.slot, x.piece])) : null);
    if (el._somOK && !el._conqPlayed && T >= flashT - 0.62 && T < flashT) { el._conqPlayed = true; if (SOM.enabled) SOM.play("conquista"); }
    // a imagem de cada carta: o Folha vestindo a peça (fica pronta antes de as cartas aparecerem)
    if (!el._cardImgs && window.folhaPng) el._cardImgs = list.map((x) => { const im = new Image(); im.src = window.folhaPng({ mood: "happy", size: 220, [x.slot]: x.piece }); return im; });
    const BX = 33, BY = 104;
    starDust(g, t, 0.9);
    const reach = el._reach(), opened = T >= K.pop;
    backdrop(g, el, 50, 62, reach, reach * 0.9, (b) => {
      glowAt(b, 50, 66, 60, glow[1], 0.16 + 0.22 * segT(T, K.pop, K.pop + 0.4) + 0.2 * segT(T, flashT, flashT + 0.5));
      bokeh(b, t, 50, 58, glow[0], 0.32);
      sceneRays(b, 50, worn ? 70 : 50, legendary ? 22 : 16, 92, glow[0], (outC(segT(T, K.rise[1], K.rise[1] + 0.4)) * 0.7 + outC(segT(T, flashT, flashT + 0.6)) * 0.3) * (0.8 + 0.2 * sin(t * 2)), t * 0.2, 0.06);
      if (legendary || rarity === "epica") sceneRays(b, 50, worn ? 70 : 50, 10, 76, "#FFFFFF", 0.4 * outC(segT(T, K.rise[1], K.rise[1] + 0.6)), -t * 0.3, 0.035);
      shockRing(b, 50, 50, segT(T, K.rise[1], K.rise[1] + 0.7), glow[0], 46);
      shockRing(b, 50, 72, segT(T, flashT, flashT + 0.8), "#FFFFFF", 64);
    });
    // ---- a caixa
    const fall = segT(T, 0, K.land), land = T - K.land;
    let by = BY - (1 - fall * fall) * 130, rot = 0, sq = 0;
    if (land > 0) sq = Math.exp(-land * 8) * sin(land * 22) * 0.16;
    let leak = 0;
    for (const a of K.wig) { const u = segT(T, a, a + 0.26); if (u > 0 && u < 1) { by -= sin(PI * u) * 3.2; rot += sin(PI * u * 2) * 6; sq -= sin(PI * u) * 0.06; leak = Math.max(leak, sin(PI * u)); } }
    const untie = sstep(segT(T, K.untie[0], K.untie[1])), gone = segT(T, flashT - 0.1, flashT + 0.25);
    // sombra no chão e a poeira da queda
    if (gone < 1) { g.save(); g.translate(BX + 3, BY + 0.6); g.scale(1, 0.18); glowAt(g, 0, 0, 22 * (1 - gone), "#05030F", 0.55 * fall); g.restore(); }
    if (land > 0 && land < 0.7) for (let i = 0; i < 8; i++) { const s = i < 4 ? -1 : 1, u = land / 0.7, x = BX + s * (16 + (i % 4) * 3 + u * 10), y = BY - 1 - sin(PI * u) * (2 + (i % 3)); glowAt(g, x, y, 2 + u * 4, "#C9C4E8", 0.35 * (1 - u)); }
    if (gone < 1) {
      g.save(); g.translate(BX, by); g.rotate(rot * PI / 180); g.scale((1 + sq) * (1 - gone * 0.5), (1 - sq) * (1 - gone * 0.7)); g.globalAlpha = 1 - gone;
      drawGiftBox(g, wrap, opened ? 1 : 0, { ribbon: 1 - untie, glow: glow[0] }, t);
      if (!opened) {
        // luz vazando pela fresta da tampa quando ela se mexe
        if (leak > 0 || T > K.wig[0]) { g.save(); g.globalCompositeOperation = "lighter"; const a = 0.25 + 0.75 * leak; g.fillStyle = rgba(glow[0], a); g.fillRect(-GIFT.W / 2, -GIFT.H - 0.5, GIFT.W, 1); glowAt(g, 0, -GIFT.H, 16, glow[0], 0.45 * a); g.restore(); }
        g.save(); g.translate(0, -GIFT.H - leak * 1.6); drawGiftLid(g, wrap, 1, untie, t); g.restore();
      }
      g.restore();
      // a fita solta escorrega da caixa até o chão
      if (untie > 0 && T < flashT) {
        const u = untie, x0 = BX + 3.5, y0 = by - GIFT.H - 7;
        const p = new Path2D(); p.moveTo(x0, y0 + u * 18); p.bezierCurveTo(x0 + 6, y0 + 6 + u * 12, x0 + 10 + u * 4, BY - 8, x0 + 14 + u * 8, BY - 0.6);
        stroke(g, p, wrap.rib[1], 2.4 * (1 - u * 0.3)); stroke(g, p, "rgba(255,255,255,0.35)", 0.5);
      }
      // a tampa sai voando, girando, e cai fora do quadro
      if (opened) {
        const u = T - K.pop, lx = BX + 4 + u * 40, ly = by - GIFT.H - 6 - u * 70 + u * u * 95;
        if (ly < 140) { g.save(); g.translate(lx, ly); g.rotate(u * 5.5); g.scale(0.9, 0.9); g.translate(0, GIFT.LID / 2); drawGiftLid(g, wrap, 0, 1, t); g.restore(); }
        // coluna de luz e fonte de faíscas saindo da boca
        const a = clamp(u * 4, 0, 1) * (1 - gone), mx = BX + GIFT.DX / 2, my = by - GIFT.H + GIFT.DY / 2;
        g.save(); g.globalCompositeOperation = "lighter";
        // a coluna de luz some antes da borda de cima (senão a borda corta a luz numa linha reta)
        const col = new Path2D(); col.moveTo(mx - 14, my); col.lineTo(mx + 14, my); col.lineTo(mx + 26, 8); col.lineTo(mx - 26, 8); col.closePath();
        const cg = g.createLinearGradient(0, my, 0, 8); cg.addColorStop(0, rgba(glow[0], 0.55 * a)); cg.addColorStop(0.55, rgba(glow[0], 0.18 * a)); cg.addColorStop(1, rgba(glow[0], 0)); g.fillStyle = cg; g.fill(col);
        for (let i = 0, n = Math.round(26 * q); i < n; i++) {
          const life = 0.9 + (i % 5) * 0.12, uu = ((u + i * 0.067) % life) / life, sx = (((i * 0.618) % 1) - 0.5) * 2;
          const x = mx + sx * 6 + sx * 22 * uu, y = my - uu * 46 + uu * uu * 30;
          star4(g, x, y, (1 - uu) * (i % 3 === 0 ? 2 : 1.2), rgba(i % 2 ? "#FFFFFF" : glow[0], a * (1 - uu)));
        }
        g.restore();
      }
    }
    // ---- o Folha: entra pela direita, se anima com a caixa, puxa a fita e, vestido, pula para o centro
    const enter = outC(segT(T, 0.3, 0.8)), jump = segT(T, flashT, flashT + 0.6), arc = sin(PI * jump) * 14;
    const fx = worn ? lerp(80, 50, sstep(jump)) : lerp(120, 80, enter), fs = worn ? lerp(0.44, 0.56, outC(jump)) : 0.44;
    const show = list.some((x) => x.slot === "hand" || x.slot === "gloves") ? "present" : "proud";
    const pullW = sstep(segT(T, K.pull, K.pull + 0.15)) * (1 - sstep(segT(T, K.untie[1], K.untie[1] + 0.2)));
    const o = { T, t, x: fx, y: BY - arc, scale: fs, look,
      dir: T < K.pop ? { x: -1, y: 0.4 } : !worn ? { x: -0.6, y: -0.8 } : { x: 0, y: 0 },
      script: [[0, "idle"], [K.land, "surprised"], [K.land + 0.45, "idle"], [K.pop, "surprised"], [K.rise[1], "happy"], [flashT, "ecstatic"], [flashT + 1.4, "happy"], [flashT + 2.4, show]],
      edit: (P) => {
        if (worn) return;
        // cada mexida da caixa: arregala e dá um pulinho
        for (const a of K.wig) { const u = segT(T, a + 0.05, a + 0.45); if (u > 0 && u < 1) { const e = sin(PI * u); P.eyeScale = 1 + 0.18 * e; P.pupil = 1 + 0.3 * e; P.hop += -2.4 * e; P.browY = -2 * e; P.mOpen = Math.max(P.mOpen, 0.4 * e); } }
        // puxando a fita: a mão esquerda pega a ponta e puxa para trás, corpo inclinado
        if (pullW > 0) {
          const pull = segT(T, K.untie[0], K.untie[1]);
          P.hL = [lerp(P.hL[0], lerp(4, 16, pull), pullW), lerp(P.hL[1], lerp(70, 74, pull), pullW)]; P.shapeL = "fist"; P.rotL = 0;
          P.tilt = lerp(P.tilt, 6 * pull, pullW); P.browIn = -0.5 * pullW; P.mW = lerp(P.mW, 3, pullW); P.mCurve = lerp(P.mCurve, 0.4, pullW); P.tongue = pull > 0.1 ? 1 : 0; P.lookX = -0.8; P.lookY = 0.5;
        }
      } };
    floorLight(g, fx, BY, 26, glow[0], 0.22 + 0.2 * segT(T, flashT, flashT + 0.6));
    const P = sceneFolha(g, o);
    // a ponta da fita na mão dele enquanto puxa
    if (pullW > 0.05 && T < K.untie[1] + 0.2) {
      const [hx, hy] = folhaPt(o, P, P.hL[0], P.hL[1]), x0 = BX + 4, y0 = by - GIFT.H - 7;
      const p = new Path2D(); p.moveTo(x0, y0); p.quadraticCurveTo((x0 + hx) / 2, Math.max(y0, hy) + 4, hx, hy); stroke(g, p, wrap.rib[1], 1.8); stroke(g, p, "rgba(255,255,255,0.35)", 0.4);
    }
    // ---- as cartas: saem da caixa uma atrás da outra girando, abrem em leque, flutuam e voam juntas até o Folha
    if (T >= K.rise[0] && T < flashT) {
      const fly = sstep(segT(T, K.fly[0], K.fly[1])), y0 = by - GIFT.H;
      // de trás para a frente: a do meio fica por cima
      const order = list.map((_, i) => i).sort((a, b) => Math.abs(b - (n - 1) / 2) - Math.abs(a - (n - 1) / 2));
      for (const i of order) {
        const it = list[i], spot = cardSpot(i, n), st = K.rise[0] + i * 0.12, ru = segT(T, st, st + 0.75); if (T < st) continue;
        const spin = (1 - outC(ru)) * 3 * PI, flip = cos(spin), ry = T < st + 0.75 ? lerp(y0, spot.y, outC(ru)) : spot.y + sin(t * 2 + i) * 0.8;
        const cx = lerp(BX + GIFT.DX / 2, spot.x, outC(ru)), x = lerp(cx, fx, fly), y = lerp(ry, BY - 22, fly), sc = lerp(lerp(0.3, spot.sc, outC(ru)), 0.15 * spot.sc, fly);
        if (fly > 0) for (let j = 1; j <= 4; j++) { const f2 = sstep(Math.max(0, segT(T, K.fly[0], K.fly[1]) - j * 0.05)); glowAt(g, lerp(cx, fx, f2), lerp(ry, BY - 22, f2), (5 - j * 0.6) * spot.sc, glow[0], 0.28 * (1 - j / 5)); }
        glowAt(g, x, y, 24 * sc, (RARITY_GLOW[it.rarity] || glow)[0], 0.4);
        g.save(); g.translate(x, y); g.rotate(spot.rot * outC(ru) * (1 - fly) + sin(t * 1.6 + i) * 0.04 * (1 - fly)); g.scale(sc, sc);
        drawPieceCard(g, flip, it.rarity, el._cardImgs && el._cardImgs[i], it.name, t, ru >= 1 ? ((T - st - 0.75) * 0.9 + i * 0.35) % 1.4 : -1);
        g.restore();
      }
      // estrelinhas em volta das cartas paradas
      const settled = segT(T, K.rise[1], K.rise[1] + 0.2);
      if (settled > 0 && fly < 0.3) {
        const rx = n > 1 ? cardSpot(n - 1, n).x - 50 + 16 * cardSpot(0, n).sc : 19, ryy = n > 1 ? 22 * cardSpot(0, n).sc : 21;
        for (let i = 0; i < (n > 1 ? 10 : 6); i++) { const a = i * 2 * PI / (n > 1 ? 10 : 6) + t * 0.5, ph = (t * 0.9 + i / 6) % 1; star4(g, 50 + cos(a) * rx, 47 + sin(a) * ryy, 2 * sin(PI * ph) * settled, rgba(i % 2 ? "#FFFFFF" : glow[0], sin(PI * ph) * settled)); }
      }
    }
    drawConfetti(g, el._conf, T - flashT);
    // clarão da troca
    const fl = segT(T, flashT - 0.08, flashT + 0.55);
    if (fl > 0 && fl < 1) { const a = fl < 0.15 ? fl / 0.15 : 1 - (fl - 0.15) / 0.85; glowAt(g, fx, 78, 60, "#FFFFFF", a); }
    if (worn) for (let i = 0; i < (legendary ? 8 : 5); i++) { const ph = (t * 0.45 + i / 8) % 1; star4(g, 14 + ((i * 29) % 72), 92 - ph * 70, 2.6 * sin(PI * ph), rgba(i % 2 ? "#FFFFFF" : glow[0], sin(PI * ph))); }
  }

  // escala do desenho em pixels de tela (sombras e desfoques do canvas são em pixels, não na grade)
  const this_k = (el) => el._k || 1;


  // ---- cena: o Folha salva a sequência (o marca-página)
  // A mesma montagem da cena da sequência (a fênix em cima, os braseiros da semana em arco, o Folha
  // embaixo no meio). O último braseiro antes de HOJE é o dia que faltou (quem não estudou no domingo
  // e volta na segunda vê o domingo sendo salvo).
  // 0–0,5 s   escuro: a fênix dorme quase apagada ("z" subindo); os dias feitos com chama baixinha,
  //           o dia que faltou soltando fumaça;
  // 0,5–1,5   o Folha entra pela direita na ponta dos pés até o meio, embaixo;
  // 1,6–3,0   para, vira para a gente e pede silêncio com o dedo na boca: "shhh…";
  // 3,0–3,3   olha para a fênix e mira com o braço lá em cima;
  // 3,3–3,95  joga a bola de fogo (azul e dourada) que sobe em arco e acerta o peito da fênix;
  // 3,95      ela acorda num clarão e abre as asas;
  // 4,1–4,4   uma chama azul desce da fênix e acende o dia que faltou (a chama do marca-página);
  // depois    os outros dias acendem, o número arde e o Folha sussurra "sequência salva!".
  const SAV = { walk: 0.5, arrive: 1.5, shh: 1.6, aim: 3.0, throwA: 3.3, boom: 3.95, wings: 4.05, drop: 4.1, saved: 4.4, say: 5.0 };
  function bubbleText(g, txt, x, y, a) {
    if (a <= 0.01) return;
    g.save(); g.globalAlpha = a;
    g.font = "900 4.2px Nunito, system-ui, sans-serif"; g.textAlign = "center"; g.textBaseline = "middle";
    const w = g.measureText(txt).width + 7, h = 8, r = 3;
    const p = new Path2D();
    p.moveTo(x - w / 2 + r, y - h / 2); p.lineTo(x + w / 2 - r, y - h / 2); p.quadraticCurveTo(x + w / 2, y - h / 2, x + w / 2, y - h / 2 + r);
    p.lineTo(x + w / 2, y + h / 2 - r); p.quadraticCurveTo(x + w / 2, y + h / 2, x + w / 2 - r, y + h / 2);
    p.lineTo(x + 3, y + h / 2); p.lineTo(x + 1, y + h / 2 + 2.6); p.lineTo(x - 1, y + h / 2);
    p.lineTo(x - w / 2 + r, y + h / 2); p.quadraticCurveTo(x - w / 2, y + h / 2, x - w / 2, y + h / 2 - r);
    p.lineTo(x - w / 2, y - h / 2 + r); p.quadraticCurveTo(x - w / 2, y - h / 2, x - w / 2 + r, y - h / 2); p.closePath();
    g.fillStyle = "#FFFFFF"; g.fill(p);
    g.fillStyle = "#1D1670"; g.fillText(txt, x, y + 0.3);
    g.restore();
  }
  // a chama do marca-página: azul (o dia que o Folha salvou)
  function drawSavedFlame(g, t, h, w) {
    if (h <= 0.3) return;
    glowAt(g, 0, -h * 0.4, h * 1.4, "#3D9BFF", 0.5);
    drawSmallFlame(g, t, h, w, "blue");
  }
  function cenaSalvou(g, T, t, el) {
    const days = Math.max(1, parseInt(el.getAttribute("dias") || "1", 10)), look = el._look(), k = this_k(el);
    // a semana: letra + estado (1 feito, 2 o dia que o Folha salva, 0 sem estudo); o último é hoje
    let week = (el.getAttribute("semana") || "").split(",").filter(Boolean).map((w) => [w.slice(0, -1) || w, w.slice(-1)]);
    if (!week.length) week = [["T", "1"], ["Q", "1"], ["Q", "1"], ["S", "1"], ["S", "1"], ["D", "2"], ["S", "0"]];
    const n = week.length;
    let savedI = -1; week.forEach(([, st], i) => { if (st === "2") savedI = i; });
    if (savedI < 0) savedI = Math.max(0, n - 2);
    const ax = (i) => 50 + (i - (n - 1) / 2) * 12, ay = (x) => 66 - 7 * (1 - ((x - 50) / 42) ** 2);
    const sx = ax(savedI), sy = ay(sx);
    const woke = outC(segT(T, SAV.boom, SAV.boom + 0.6));
    const shake = T > SAV.boom && T < SAV.boom + 0.5 ? (1 - (T - SAV.boom) / 0.5) * 1.2 : 0;
    g.save(); g.translate(sin(t * 93) * shake, cos(t * 71) * shake);
    // ---- fundo: noite fria que esquenta quando a fênix acorda
    const reach = el._reach();
    backdrop(g, el, PX, PY + 10, reach, reach * 0.95, (b) => {
      glowAt(b, PX, PY + 6, 62, woke > 0 ? "#FF6A1A" : "#2C2A78", woke > 0 ? 0.14 + 0.16 * woke : 0.24);
      glowAt(b, sx, sy, 22, "#3D9BFF", 0.3 * segT(T, SAV.saved, SAV.saved + 0.4));
      if (woke > 0) sceneRays(b, PX, PY, 16, 90, "#FFB13B", woke * (0.5 + 0.15 * sin(t * 2)), t * 0.1, 0.05);
      shockRing(b, PX, PY, segT(T, SAV.boom, SAV.boom + 0.7), "#FFE07A", 52);
      shockRing(b, sx, sy, segT(T, SAV.saved, SAV.saved + 0.6), "#9FE6FF", 22);
      for (let i = 0, m = Math.round(22 * Q); i < m; i++) { const u = (t * (0.07 + (i % 5) * 0.014) + i * 0.137) % 1, x = ((i * 37) % 130) - 15 + sin(t * 0.7 + i) * 4, y = 115 - u * 135, c = fireColor(0.3 + (i % 3) * 0.15); glowAt(b, x, y, 1.4, `rgb(${c})`, (1 - u) * (0.12 + 0.5 * woke)); }
    });
    // ---- os braseiros da semana (iguais aos da cena da sequência)
    const fuse = new Path2D(); for (let x = ax(0) - 6; x <= ax(n - 1); x += 1) { const y = ay(x) + 4; x === ax(0) - 6 ? fuse.moveTo(x, y) : fuse.lineTo(x, y); }
    stroke(g, fuse, "rgba(255,255,255,0.12)", 0.5);
    const savedOn = outBack(segT(T, SAV.saved, SAV.saved + 0.35));
    week.forEach(([letter, st], i) => {
      const today = i === n - 1, x = ax(i), y = ay(x);
      g.save(); g.translate(x, y);
      drawBrazier(g, i === savedI ? savedOn : st === "1" ? 0.45 + 0.55 * woke : 0, i === savedI ? "blue" : "fire");
      if (i === savedI) {
        if (savedOn > 0) { g.translate(0, 1.2); drawSavedFlame(g, t + i, 9 * savedOn * (1 + 0.06 * sin(t * 8)), 3); }
        else for (let j = 0; j < 3; j++) { const u = (t * 0.6 + j / 3) % 1; circle(g, `rgba(180,175,210,${0.35 * (1 - u)})`, 0.8 + u * 1.6, sin(t + j) * 1.2, -u * 8); }
      } else if (st === "1") {
        // dia feito: brasa baixinha enquanto a fênix dorme, chama cheia quando ela acorda
        g.translate(0, 1.2); drawSmallFlame(g, t + i, (today ? 9 : 6.5) * (0.45 + 0.55 * woke) * (1 + 0.06 * sin(t * 9 + i)), today ? 3.2 : 2.4);
      }
      g.restore();
      g.fillStyle = i === savedI && savedOn > 0 ? "rgba(160,225,255,0.98)" : st === "1" ? "rgba(255,225,170,0.9)" : "rgba(255,255,255,0.45)";
      g.font = "900 3px Nunito, system-ui, sans-serif"; g.textAlign = "center"; g.textBaseline = "middle";
      g.fillText(today ? "HOJE" : letter, x, y + 10.4);
    });
    // ---- a chama azul: desce da fênix acordada até o dia que faltou e acende o braseiro dele
    const drop = segT(T, SAV.drop, SAV.saved);
    if (drop > 0 && T < SAV.saved + 0.25) {
      const at = (q) => { const v = 1 - q; return [v * v * PX + 2 * v * q * (PX + (sx - PX) * 0.85) + q * q * sx, v * v * (PY + 6) + 2 * v * q * (PY + 4) + q * q * (sy - 3)]; };
      const p = new Path2D(); for (let q = 0; q <= outC(drop); q += 0.04) { const [x, y] = at(q); q === 0 ? p.moveTo(x, y) : p.lineTo(x, y); }
      g.save(); g.globalAlpha = 1 - segT(T, SAV.saved, SAV.saved + 0.25);
      stroke(g, p, "rgba(61,155,255,0.35)", 3.2); stroke(g, p, "rgba(160,225,255,0.9)", 1.1); g.restore();
      const [hx, hy] = at(outC(drop)); glowAt(g, hx, hy, 6, "#3D9BFF", 0.9); circle(g, "#FFFFFF", 1.1, hx, hy);
    }
    const ball = segT(T, SAV.boom, SAV.boom + 0.35);
    if (ball > 0 && ball < 1) { glowAt(g, PX, PY, 10 + 30 * ball, "#FFFFFF", 0.9 * (1 - ball)); glowAt(g, PX, PY, 8 + 20 * ball, "#FFB13B", 1 - ball); }
    // ---- a fênix: dormindo (asas recolhidas, quase apagada) e acordando em labaredas
    const breath = sin(t * 1.6);
    const open = woke > 0 ? 0.08 + 0.92 * outBack(segT(T, SAV.wings, SAV.wings + 0.75)) : 0.06 + 0.03 * breath;
    const flap = T < SAV.wings + 0.75 ? 0 : sin((T - SAV.wings - 0.75) * 2.4) * 10 + (T < SAV.wings + 1.6 ? sin(PI * segT(T, SAV.wings + 0.75, SAV.wings + 1.6)) * -25 : 0);
    const nova = !!el._fx;
    if (!nova) drawPhoenix(g, t, open, flap, 0.16 + 0.05 * breath + 0.84 * woke, k);
    if (!nova && woke < 0.6) {
      const za = 1 - woke / 0.6;
      for (let j = 0; j < 3; j++) { const u = (t * 0.35 + j / 3) % 1; g.save(); g.globalAlpha = za * (1 - u) * 0.9; g.fillStyle = "#CFC8FF"; g.font = `900 ${3 + u * 3}px Nunito, system-ui, sans-serif`; g.fillText("z", PX + 10 + u * 9 + sin(u * 6 + j) * 1.5, PY - 12 - u * 18); g.restore(); }
    }
    const beat = 1 + 0.06 * Math.max(0, sin(t * 4.2)) ** 6;
    if (!nova) fireNumber(g, String(days), PX, PY + 1, String(days).length > 2 ? 13 : String(days).length > 1 ? 17 : 20, 0.22 + 0.78 * woke, woke > 0.5 ? beat : 1);
    if (woke > 0) for (let i = 0, m = Math.round(16 * Q); i < m; i++) { const u = (t * (0.4 + (i % 4) * 0.08) + i * 0.0625) % 1, sd = ((i * 0.618) % 1 - 0.5) * 2, x = PX + sd * 38 * (0.4 + u), y = PY - 6 - u * 34 + sd * sd * 8; const c = fireColor(0.15 + u * 0.6); circle(g, `rgba(${c},${(1 - u) * open})`, 0.3 + 0.3 * (1 - u), x, y); }
    g.restore();
    // ---- o Folha: entra na ponta dos pés, pede silêncio, mira na fênix e joga a bola de fogo
    const walkU = segT(T, SAV.walk, SAV.arrive), walking = walkU > 0 && walkU < 1;
    const shh = T >= SAV.shh && T < SAV.aim;
    const aimU = outC(segT(T, SAV.aim, SAV.throwA)), throwU = segT(T, SAV.throwA, SAV.throwA + 0.22);
    const fx = lerp(122, 50, outC(walkU)), hop = walking ? -Math.abs(sin(walkU * PI * 6)) * 2.2 : 0;
    const o = {
      T, t, x: fx, y: 101 + hop, scale: 0.4, look,
      dir: { x: T < SAV.walk ? -1 : walking ? -0.9 : shh ? 0 : T < SAV.boom + 0.3 ? 0 : 0, y: shh ? 0 : T >= SAV.aim ? -1 : 0 },
      ex: shh ? { armsFront: true } : undefined,
      script: [[0, "idle"], [SAV.boom + 0.15, "ecstatic"], [SAV.boom + 1.5, "happy"], [SAV.say + 1.2, "proud"]],
      edit: (P) => {
        if (walking) {
          // na ponta dos pés: braços pra trás, olhinho de quem não quer acordar ninguém
          P.hL = [8, 82]; P.hR = [92, 82]; P.lookX = -0.7; P.lookY = -0.4; P.mW = 2.4; P.mCurve = 0.4; P.mOpen = 0; P.browY = -0.5; P.browIn = 0.2; P.tilt = -4 + sin(walkU * PI * 6) * 2;
        } else if (shh) {
          // dedo em pé na frente da boca, boquinha de "shhh" e olhar para a gente
          const sp = outC(segT(T, SAV.shh, SAV.shh + 0.3));
          P.hR = [lerp(92, 54, sp), lerp(80, 76, sp)]; P.shapeR = "point"; P.rotR = lerp(0, 140, sp); P.bendR = lerp(2, 8, sp);
          P.mW = 2; P.mCurve = 0.1; P.mOpen = 0.25; P.browY = -0.5; P.browIn = 0.3; P.cheek = 0.6; P.lookX = 0; P.lookY = 0.05; P.tilt = sin(t * 2) * 1.5;
        } else if (T >= SAV.aim && T < SAV.boom + 0.15) {
          // mira lá em cima, na fênix, e arremessa
          P.hR = [lerp(92, 98, aimU) - throwU * 6, lerp(78, 16, aimU) - throwU * 4]; P.shapeR = throwU > 0.15 ? "open" : "point"; P.rotR = lerp(0, -115, aimU); P.bendR = -6;
          P.hL = [10, 72]; P.lookX = -0.3; P.lookY = -1; P.mW = 3.4; P.mCurve = 1.6; P.mOpen = 0.3; P.browY = -0.9; P.tilt = -3;
        }
      },
    };
    floorLight(g, fx, 101, 26, woke > 0 ? "#FF8A2A" : "#5A50C0", 0.16 + 0.18 * woke);
    const P = sceneFolha(g, o);
    // a faísca na ponta do dedo enquanto mira e a bola de fogo subindo em arco até o peito da fênix
    const [hx, hy] = folhaPt(o, P, P.hR[0], P.hR[1]);
    if (T >= SAV.aim + 0.1 && T < SAV.throwA + 0.1) { const a = segT(T, SAV.aim + 0.1, SAV.aim + 0.3); glowAt(g, hx, hy - 1, 3 + sin(t * 20) * 0.6, "#7EC8FF", 0.8 * a); circle(g, "#FFFFFF", 0.6 * a, hx, hy - 1); }
    if (T >= SAV.throwA + 0.05 && T < SAV.boom + 0.05) {
      const u = segT(T, SAV.throwA + 0.05, SAV.boom), e = u * u * (3 - 2 * u);
      const bx = lerp(hx, PX, e) + sin(PI * u) * 8, by = lerp(hy, PY + 2, e) - sin(PI * u) * 6;
      for (let j = 1; j <= 6; j++) { const v = Math.max(0, u - j * 0.03), ee = v * v * (3 - 2 * v); glowAt(g, lerp(hx, PX, ee) + sin(PI * v) * 8, lerp(hy, PY + 2, ee) - sin(PI * v) * 6, 4 - j * 0.45, j % 2 ? "#3D9BFF" : "#FFB13B", 0.5 * (1 - j / 7)); }
      glowAt(g, bx, by, 7, "#3D9BFF", 0.85); glowAt(g, bx, by, 4, "#FFE07A", 0.9); circle(g, "#FFFFFF", 1.1, bx, by);
    }
    // as falas
    bubbleText(g, "shhh…", fx + 1, 62, segT(T, SAV.shh + 0.2, SAV.shh + 0.5) * (1 - segT(T, SAV.aim - 0.15, SAV.aim)));
    bubbleText(g, "sequência salva!", 50, 62, segT(T, SAV.say, SAV.say + 0.3));
    drawConfetti(g, el._conf, T - SAV.boom);
  }
  const CENAS = { nivel: cenaNivel, emblema: cenaEmblema, sequencia: cenaSequencia, peca: cenaPeca, salvou: cenaSalvou };
  // ------------------------------------------------------------------ som
  // Os sons do Estudário (scripts/som: gravações reais CC0 afinadas juntas em Ré maior), ao lado deste
  // arquivo em sons/. window.estudarioSom.play("acerto"). Vêm desligados: tocam só com localStorage
  // "estudario.som" = "on" (o site, em Ajustes); a cena do app recebe som="0" e toca pelo próprio app.
  const SOM = (() => {
    const here = document.currentScript && document.currentScript.src;
    const base = here ? new URL("sons/", here).href : "sons/";
    let ctx = null; const cache = {};
    // desligado por padrão: só toca depois que a pessoa liga em Ajustes
    const on = () => { try { return localStorage.getItem("estudario.som") === "on"; } catch (e) { return false; } };
    const audio = () => { if (!ctx) { const A = window.AudioContext || window.webkitAudioContext; if (!A) return null; ctx = new A(); } if (ctx.state === "suspended") ctx.resume().catch(() => {}); return ctx; };
    // o navegador só libera o áudio depois de um toque: o primeiro toque já deixa pronto
    const unlock = () => { audio(); window.removeEventListener("pointerdown", unlock, true); };
    window.addEventListener("pointerdown", unlock, true);
    function load(name) {
      const a = audio(); if (!a) return Promise.reject();
      return cache[name] || (cache[name] = fetch(base + name + ".wav").then((r) => r.arrayBuffer()).then((b) => new Promise((ok, err) => a.decodeAudioData(b, ok, err))));
    }
    function play(name, gain = 1) {
      if (!on()) return;
      load(name).then((buf) => { const a = audio(); const src = a.createBufferSource(), g = a.createGain(); g.gain.value = 0.8 * gain; src.buffer = buf; src.connect(g).connect(a.destination); src.start(); }).catch(() => {});
    }
    // as trilhas das comemorações ficam prontas antes (carregar na hora atrasava o som em relação à cena)
    setTimeout(() => { if (on()) ["cena_fenix", "cena_salvou", "cena_emblema", "cena_nivel", "cena_peca", "conquista"].forEach((n) => load(n).catch(() => {})); }, 4000);
    return { play, load, set(v) { try { localStorage.setItem("estudario.som", v ? "on" : "off"); } catch (e) {} }, get enabled() { return on(); } };
  })();
  window.estudarioSom = SOM;

  class EstudarioCena extends HTMLElement {
    static get observedAttributes() { return ["size", "tipo", "nivel", "tier", "categoria", "dias", "semana", "piece", "piece-slot", "rarity", "pieces", "bleed"]; }
    connectedCallback() {
      if (!this._canvas) {
        // gpu: a WebView do app desenha a cena pela placa de vídeo. Pelo processador (o padrão, por causa do
        // quadrado preto em algumas placas do computador) a cena inteira, quadro a quadro, travava em tablet.
        this._canvas = this.hasAttribute("gpu") ? document.createElement("canvas") : cpuCanvas(); this._canvas.style.display = "block"; this._canvas.style.pointerEvents = "none";
        this.appendChild(this._canvas); this.setAttribute("role", "img");
        if (!this.style.display) this.style.display = "block";
        if ("ResizeObserver" in window) { this._ro = new ResizeObserver(() => this._size()); this._ro.observe(this); }
      }
      this._restart(); this._size();
      // No máximo 60 quadros por segundo: em tela de 120 Hz a cena desenhava o dobro, sem ganho visível, e pesava no celular.
      const loop = (now) => { if (!this.isConnected) return; if (!this._last || now - this._last >= 14) { this._last = now; this._frame(now); } this._raf = requestAnimationFrame(loop); };
      cancelAnimationFrame(this._raf); this._raf = requestAnimationFrame(loop);
    }
    disconnectedCallback() { cancelAnimationFrame(this._raf); }
    attributeChangedCallback(n) { if (n === "size" || n === "bleed") this._size(); else if (this._canvas) this._restart(); }
    // até onde a luz do fundo pode ir (na grade): mais longe quando a cena sangra além da caixa
    _reach() { return 50 + (this._bleed || 0) * 0.85; }
    _restart() {
      this._start = performance.now();
      this._pieces = null; this._cardImgs = null; this._conqPlayed = false;
      // no presente, a caixa tem a trilha dela e o "achou!" do Folha entra quando ele aparece vestido (a hora
      // depende de quantas cartas saem); a cena toca esse pedaço sozinha
      this._somOK = !this.hasAttribute("at") && this.getAttribute("som") !== "0";
      if (this._somOK && this.getAttribute("tipo") === "peca") SOM.load("conquista").catch(() => {});
      // a trilha da cena começa junto com ela (a cena recomeça a cada atributo novo: toca uma vez só, no fim)
      clearTimeout(this._somT);
      if (!this.hasAttribute("at") && this.getAttribute("som") !== "0") this._somT = setTimeout(() => {
        if (!this.isConnected || !SOM.enabled) return;
        const name = "cena_" + this.getAttribute("tipo");
        // espera o som carregar e, se a cena mal começou, recomeça junto para a batida cair no quadro certo
        SOM.load(name).then(() => { if (!this.isConnected) return; if (performance.now() - this._start < 450) this._start = performance.now(); SOM.play(name); }).catch(() => {});
      }, 30);
      this._novaFenix();
      const tipo = this.getAttribute("tipo"), glow = RARITY_GLOW[this.getAttribute("rarity")] || RARITY_GLOW.comum;
      const cols = tipo === "peca" ? [glow[0], glow[1], "#FFFFFF", SPARK, CHEEK] : tipo === "emblema" ? [...(TIERS[this.getAttribute("tier")] || TIERS[3]), "#FFFFFF", MINT] : [SPARK, MINT, CHEEK, "#6FB0FF", "#B995F5", "#FFFFFF"];
      const lend = tipo === "peca" && (this.getAttribute("rarity") === "lendaria" || (this.getAttribute("pieces") || "").includes('"lendaria"'));
      this._conf = makeConfetti(7 + (tipo || "").length, lend ? 60 : 44, cols);
    }
    // Na sequência salva, a fênix é a nova (phoenix.js, <estudario-fenix>), por cima da cena no lugar da desenhada:
    // dorme até a bola de fogo do Folha acertar (SAV.boom) e acorda sozinha. Sem phoenix.js, fica a desenhada.
    _novaFenix() {
      if (this._fx) { this._fx.remove(); this._fx = null; }
      if (this.getAttribute("tipo") !== "salvou" || !customElements.get("estudario-fenix")) return;
      const fx = document.createElement("estudario-fenix"), at = this.getAttribute("at");
      if (at != null) fx.setAttribute("state", +at >= SAV.boom ? "awake" : "sleep");
      else { fx.setAttribute("state", "sleep"); fx.setAttribute("wake-at", String(Math.round(SAV.boom * 1000))); }
      fx.style.position = "absolute"; fx.style.pointerEvents = "none";
      this._fx = fx; this.appendChild(fx); this._placeFenix();
    }
    _placeFenix() {
      if (!this._fx) return;
      const w = this.clientWidth || 320, size = w * 0.34;   // o desenho da fênix nova ocupa 1,7x a caixa dela
      Object.assign(this._fx.style, { width: size + "px", height: size + "px", left: (w * PX / 100 - size / 2) + "px", top: (w * PY / 100 - size * 0.52) + "px" });
    }
    // o visual do Folha: o que a pessoa escolheu, em vitrine (sem medalha de sequência nem café da manhã)
    _look(over) { return resolveLook(null, Object.assign({ plain: true }, over || {})); }
    _size() {
      if (!this._canvas) return;
      const attr = parseInt(this.getAttribute("size") || "0", 10), w = attr || this.clientWidth || 320, dpr = Math.min(window.devicePixelRatio || 1, this._lite ? 1.1 : 1.5); // 1,75: a cena é animação; 2,5 desenhava o dobro de pixels por quadro e travava no celular
      if (attr) { this.style.width = w + "px"; }
      const h = Math.round(w * CENA_H / 100);
      this.style.height = h + "px";
      // bleed="30": o desenho passa 30% da largura para cada lado da caixa (a luz se espalha pela tela)
      const bleed = Math.max(0, parseFloat(this.getAttribute("bleed") || "0")), off = Math.round(w * bleed / 100);
      this._bleed = bleed; this._placeFenix && this._placeFenix();
      if (getComputedStyle(this).position === "static") this.style.position = "relative";
      const st = this._canvas.style; st.position = "absolute"; st.left = st.top = -off + "px"; st.width = w + 2 * off + "px"; st.height = h + 2 * off + "px";
      // borda macia (nada encosta na borda do quadro): máscara do navegador, que a placa de vídeo aplica de graça;
      // antes eram dois degradês pintados sobre o canvas inteiro a cada quadro
      { const W = 100 + 2 * bleed, H = CENA_H + 2 * bleed, sx = Math.min(30, 700 / W), st0 = Math.min(30, 500 / H), sb = Math.min(30, 300 / H);
        const mask = "linear-gradient(to right, transparent, #000 " + sx + "%, #000 " + (100 - sx) + "%, transparent), linear-gradient(to bottom, transparent, #000 " + st0 + "%, #000 " + (100 - sb) + "%, transparent)";
        st.webkitMaskImage = st.maskImage = mask; st.webkitMaskComposite = "source-in"; st.maskComposite = "intersect"; }
      const cw = Math.round((w + 2 * off) * dpr), ch = Math.round((h + 2 * off) * dpr);
      if (this._canvas.width !== cw || this._canvas.height !== ch) { this._canvas.width = cw; this._canvas.height = ch; }
      this._k = w * dpr / 100; this._off = off * dpr;
    }
    _frame(now) {
      const g = this._canvas.getContext("2d"), draw = CENAS[this.getAttribute("tipo")];
      if (!g || !draw || !this._k) return;
      // at="2.5": quadro parado naquele instante do roteiro (para conferir o desenho)
      const at = this.getAttribute("at"), still = reduceMotion.matches || at != null, T = at != null ? +at : still ? 9 : (now - this._start) / 1000, t = at != null ? +at : still ? 0.35 : now / 1000;
      // proteção de desempenho: depois de 1,5 s, se a mediana dos últimos 40 quadros passar de 25 ms (menos de
      // 40 por segundo), a cena passa para o modo leve até o fim (menos partículas, menos luz, resolução menor).
      // Mediana, não média: um quadro lento isolado (a explosão) não derruba a qualidade de um celular bom.
      if (!still) {
        if (this._lastNow) { const w = this._win || (this._win = []); w.push(Math.min(200, now - this._lastNow)); if (w.length > 40) w.shift(); }
        this._lastNow = now;
        if (!this._lite && now - this._start > 1500 && this._win && this._win.length >= 40) {
          const med = [...this._win].sort((a, b) => a - b)[20];
          if (med > 25) { this._lite = true; this._size(); }
        }
      }
      Q = this._lite ? 0.5 : 1;
      g.setTransform(1, 0, 0, 1, 0, 0); g.clearRect(0, 0, this._canvas.width, this._canvas.height);
      g.setTransform(this._k, 0, 0, this._k, this._off || 0, this._off || 0);
      draw(g, T, t, this);
      Q = 1;
    }
  }
  customElements.define("estudario-cena", EstudarioCena);

  // Folha parado (miniaturas e botões): o mesmo desenho do 2.0, um quadro só, em PNG. Guarda em cache por
  // humor e tamanho. folhaPng({ mood, size }) -> data URL.
  const STATIC = new Map();
  window.folhaPng = function (o) {
    const mood = MOODS[(o && o.mood) || "happy"] || "idle", size = Math.max(8, Math.round((o && o.size) || 96));
    // peças no pedido ({ hat: "coroa" }...): vitrine do armário (só as peças, sem medalha nem hora do dia)
    const shelf = o && SLOTS.some((k) => o[k]), zoom = o && o.zoom ? (+o.zoom > 1 ? +o.zoom : 1.75) : 0, zx = (o && o.fx) || 50, zy = (o && o.fy) || 63;
    const look = resolveLook(null, shelf ? Object.assign({ plain: true }, o) : null);
    const dpr = Math.min(window.devicePixelRatio || 1, 2.5);
    const key = [mood, size, dpr, shelf ? "shelf" : "", zoom ? "z" + zoom + ":" + zx + ":" + zy : "", ...SLOTS.map((k) => (shelf && o[k]) || LOOK[k]),
      shelf ? 0 : look.streak >= 100 ? 3 : look.streak >= 30 ? 2 : look.streak >= 7 ? 1 : 0, look.night, look.morning].join(":");
    if (STATIC.has(key)) return STATIC.get(key);
    const T = pose(mood === "thinking" ? "idle" : mood, 0.35, { x: 0.3, y: -0.2 }, null, { tm: 9, holding: look.hand && look.hand !== "nenhum" });
    const P = { ...T, hL0: T.hL[0], hL1: T.hL[1], hR0: T.hR[0], hR1: T.hR[1] };
    const c = cpuCanvas(); c.width = c.height = Math.round(size * dpr);
    const pet = look.pet && look.pet !== "nenhum" && size >= SMALL, wide = (look.back && look.back !== "nada") || look.aura === "holofote" || look.aura === "fogos" || look.aura === "supernova";
    // cabe a mão para o alto (e o companheiro) dentro da caixa; com item na mão, mais longe e puxado para a
    // direita, porque o item sai para a esquerda e para cima da mão e era cortado pela borda
    const held = look.hand && look.hand !== "nenhum";
    const g = c.getContext("2d"), k = c.width / 100, fit = held ? (pet || wide ? 0.64 : 0.68) : pet || wide ? 0.76 : mood === "present" ? 0.78 : 0.9;
    g.setTransform(k * fit, 0, 0, k * fit, c.width * (1 - fit) / 2 + (pet ? k * 7 : 0) + (held ? k * 9 : 0), c.width * (1 - fit) / 2 + k * (pet ? 1 : mood === "present" ? 5 : 2) + (held ? k * 4 : 0));
    if (zoom) { g.setTransform(k * zoom, 0, 0, k * zoom, -k * zoom * zx + c.width / 2, -k * zoom * zy + c.width / 2); }
    drawFolha(g, { t: 0.35, mood: mood === "thinking" ? "idle" : mood, small: !zoom && size < SMALL, P, vy: 0, blink: 0, wink: mood === "wink" ? 1 : 0, popL: 0, popR: 0, squash: 0, gulp: 0, look, tm: 9 });
    const url = c.toDataURL("image/png"); STATIC.set(key, url); return url;
  };

  // Medalha viva (tela de conquistas, ao tocar num emblema): <estudario-medalha tier cat earned size>.
  // Gira devagar como uma moeda na mão, com a espessura da borda aparecendo; arrastando gira junto e,
  // solta com força, rodopia e volta a balançar. O brilho passa de tempos em tempos; conquistada ganha
  // luz e estrelinhas, trancada fica em cinza, quieta.
  class EstudarioMedalha extends HTMLElement {
    connectedCallback() {
      if (!this._canvas) {
        this._canvas = cpuCanvas(); this._canvas.style.display = "block"; this._canvas.style.touchAction = "pan-y";
        this.appendChild(this._canvas); this.setAttribute("role", "img");
        this._ang = 0; this._vel = 0; this._drag = null; this._start = performance.now();
        this._canvas.addEventListener("pointerdown", (e) => { this._drag = { x: e.clientX, a: this._ang, t: performance.now(), lx: e.clientX }; this._vel = 0; this._canvas.setPointerCapture(e.pointerId); });
        this._canvas.addEventListener("pointermove", (e) => {
          if (!this._drag) return; const now = performance.now(), dt = Math.max(1, now - this._drag.t);
          this._vel = (e.clientX - this._drag.lx) * 0.018 / (dt / 16); this._drag.lx = e.clientX; this._drag.t = now;
          this._ang = this._drag.a + (e.clientX - this._drag.x) * 0.018;
        });
        const up = () => { if (!this._drag) return; if (Math.abs(this._vel) < 0.02 && Math.abs(this._ang - this._drag.a) < 0.05) this._vel = 0.42; this._drag = null; };
        this._canvas.addEventListener("pointerup", up); this._canvas.addEventListener("pointercancel", up);
      }
      this._size();
      // No máximo 60 quadros por segundo: em tela de 120 Hz a cena desenhava o dobro, sem ganho visível, e pesava no celular.
      const loop = (now) => { if (!this.isConnected) return; if (!this._last || now - this._last >= 14) { this._last = now; this._frame(now); } this._raf = requestAnimationFrame(loop); };
      this._raf = requestAnimationFrame(loop);
    }
    disconnectedCallback() { cancelAnimationFrame(this._raf); }
    static get observedAttributes() { return ["tier", "cat", "earned", "size"]; }
    attributeChangedCallback(n) { if (!this._canvas) return; if (n === "size") this._size(); this._start = performance.now(); }
    _size() {
      const w = parseInt(this.getAttribute("size") || "220", 10), dpr = Math.min(window.devicePixelRatio || 1, 2.5);
      this._canvas.width = this._canvas.height = Math.round(w * dpr); this._canvas.style.width = this._canvas.style.height = w + "px"; this._k = (w * dpr) / 100;
    }
    _frame(now) {
      const g = this._canvas.getContext("2d"), t = (now - this._start) / 1000, k = this._k;
      const tier = Math.max(1, Math.min(6, parseInt(this.getAttribute("tier") || "1", 10))), cat = this.getAttribute("cat") || "seq", earned = this.getAttribute("earned") !== "false";
      const calm = reduceMotion.matches;
      // física do giro: arrastando segue o dedo; solto, gira com atrito e depois volta a balançar
      if (!this._drag) {
        if (Math.abs(this._vel) > 0.004) { this._ang += this._vel; this._vel *= 0.955; }
        else {
          this._vel = 0; const turns = Math.round(this._ang / (2 * PI)) * 2 * PI, sway = calm || !earned ? 0 : sin(t * 0.9) * 0.42;
          this._ang += (turns + sway - this._ang) * 0.08;
        }
      }
      const ang = this._ang, c = cos(ang), C = TIERS[tier] || TIERS[3];
      const R = tier >= 5 ? 24 : tier >= 3 ? 28 : 33;
      g.setTransform(1, 0, 0, 1, 0, 0); g.clearRect(0, 0, this._canvas.width, this._canvas.height);
      g.setTransform(k, 0, 0, k, 0, 0);
      const appear = outBack(segT(t, 0, 0.6));
      if (earned) {
        glowAt(g, 50, 50, 48, C[1], 0.32 + 0.08 * sin(t * 2));
        if (!calm) sceneRays(g, 50, 50, 16, 52, C[0], 0.45 * appear, t * 0.15, 0.07);
      }
      g.save(); g.translate(50, 50); g.scale(appear, appear);
      if (!earned) g.filter = "grayscale(1) brightness(0.62) contrast(0.9)";
      // a espessura da moeda aparece quando ela está de lado
      const th = sin(ang) * R * 0.11;
      g.save(); g.scale(Math.max(0.03, Math.abs(c)), 1);
      g.beginPath(); g.arc(th / Math.max(0.03, Math.abs(c)), 0, R * 0.99, 0, 2 * PI); g.fillStyle = C[2]; g.fill();
      const shine = earned && !calm ? ((t * 0.32) % 1.25) : -1;
      drawMedalArt(g, R, earned ? tier : Math.min(tier, 2), c >= 0 ? cat : "", { deco: 1, engrave: c >= 0 ? 1 : 0, k, shine: c >= 0 ? shine : -1 });
      g.restore();
      g.restore();
      if (earned && !calm) for (let i = 0; i < 7; i++) {
        const a = i * 2 * PI / 7 + t * 0.25, ph = (t * 0.6 + i * 0.37) % 1;
        star4(g, 50 + cos(a) * 42, 50 + sin(a) * 40, 2.6 * sin(PI * ph) * appear, rgba(i % 2 ? "#FFFFFF" : C[0], sin(PI * ph)));
      }
    }
  }
  customElements.define("estudario-medalha", EstudarioMedalha);

  // Medalha de emblema parada (tela de conquistas): o mesmo desenho da forja, em PNG, guardado em cache.
  // folhaMedal({ tier: 1..6, cat: "seq", earned: true, size: 80 }) -> data URL. Trancada sai em cinza.
  const MEDALS = new Map();
  window.folhaMedal = function (o) {
    const size = Math.max(16, Math.round((o && o.size) || 80)), dpr = Math.min(window.devicePixelRatio || 1, 2.5);
    const tier = (o && o.tier) || 1, cat = (o && o.cat) || "seq", earned = !(o && o.earned === false);
    const key = [size, dpr, tier, cat, earned].join(":");
    if (MEDALS.has(key)) return MEDALS.get(key);
    const c = cpuCanvas(); c.width = c.height = Math.round(size * dpr);
    const g = c.getContext("2d"), k = c.width / 100;
    g.setTransform(k, 0, 0, k, 0, 0); g.translate(50, 50);
    // a moldura (louros, roseta, asas) precisa de espaço: quanto mais alto o nível, menor a moeda
    const R = tier >= 5 ? 25 : tier >= 3 ? 30 : 38;
    if (!earned) g.filter = "grayscale(1) brightness(0.62) contrast(0.9)";
    drawMedalArt(g, R, earned ? tier : Math.min(tier, 2), cat, { deco: 1, engrave: 1, k });
    const url = c.toDataURL("image/png"); MEDALS.set(key, url); return url;
  };

  // O site avisa o que a pessoa escolheu no armário e o contexto do dia: { cover, ribbon, hat, eyes, neck, hand, aura, gloves, streak, atRisk }.
  window.folhaSetLook = function (o) {
    let changed = false;
    o = Object.assign({}, o || {});
    if (o.acc && LEGACY_ACC[o.acc]) { const [k, v] = LEGACY_ACC[o.acc]; if (!o[k]) o[k] = v; }
    for (const k in o) if (k in LOOK && LOOK[k] !== o[k]) { LOOK[k] = o[k]; changed = true; }
    if (!changed) return;
    document.querySelectorAll("estudario-folha").forEach((el) => { if (el.isConnected) live.add(el); });
    wake();
    window.dispatchEvent(new Event("folha-look"));
  };
  // Saída das telas que fecham: os Folhas dentro de `root` acenam e somem; `done` roda quando terminam.
  window.folhaLeave = function (root, done) {
    const els = root ? Array.from(root.querySelectorAll("estudario-folha")) : [];
    if (!els.length || reduceMotion.matches) { done(); return; }
    els.forEach((el) => el.leave());
    setTimeout(done, LEAVE_MS);
  };
  // Voltou para a aba depois de mais de um minuto fora: os Folhas grandes cumprimentam (um só fala).
  let hiddenAt = 0;
  document.addEventListener("visibilitychange", () => {
    if (document.visibilityState === "hidden") { hiddenAt = performance.now(); return; }
    if (!hiddenAt || performance.now() - hiddenAt < 60000) return;
    const now = performance.now();
    const big = Array.from(document.querySelectorAll("estudario-folha")).filter((el) => el.isConnected && parseInt(el.getAttribute("size") || "96", 10) >= 80 && !el.hasAttribute("quiet"));
    big.slice(0, 1).forEach((el) => el.greet(now));
  });
  if (reduceMotion.addEventListener) reduceMotion.addEventListener("change", () => {
    document.querySelectorAll("estudario-folha").forEach((el) => { if (el.isConnected) live.add(el); });
    wake();
  });
})();
