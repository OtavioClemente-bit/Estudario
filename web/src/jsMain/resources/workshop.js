// A oficina do Folha: a espera de toda geração do Estudário mostra o trabalho acontecendo, desenhado
// no traço da turma, em cima de uma mesa de caderno pontilhado. Cada tipo de trabalho tem a sua cena:
//
//   plan      os blocos das matérias caem nos dias da semana e se acomodam (às vezes um muda de dia)
//   material  o caderno se escreve sozinho: o lápis corre as linhas, o marca-texto grifa, a página vira
//   questions as questões chegam como cartas, a alternativa certa acende, o carimbo bate, a carta vai
//             para a pilha de prontas
//   edital    a lupa varre o PDF linha por linha e as matérias saltam para a lista
//
// O Folha trabalha ao lado (folha-live.js) e comemora a cada entrega. Uso:
// <estudario-workshop kind="plan|material|questions|edital"></estudario-workshop>.
// Quem pede menos movimento vê a cena montada e parada.
(function () {
  if (!window.customElements || customElements.get("estudario-workshop")) return;
  const INK = "#26234A";
  const COLORS = ["#5B4BF5", "#22C59A", "#FF5C4D", "#FFB421", "#2EA8F5", "#F472B6", "#8B5CF6"];
  const reduce = () => window.matchMedia && matchMedia("(prefers-reduced-motion: reduce)").matches;
  const rand = (a, b) => a + Math.random() * (b - a);
  const pick = (arr) => arr[(Math.random() * arr.length) | 0];
  const glyph = (n) => (window.estudarioGlyph && window.estudarioGlyph(n)) || "";

  const css = `
estudario-workshop{display:block;width:100%;max-width:580px;margin:0 auto;aspect-ratio:580/330;position:relative}
.ws{position:absolute;left:0;top:0;width:580px;height:330px;transform-origin:0 0}
.ws-desk{position:absolute;inset:18px 6px 6px;border-radius:26px;background:var(--paper,#FFFDF8);border:2px solid var(--line,#E4DCCB);
  box-shadow:0 7px 0 var(--line,#E4DCCB),0 30px 50px -30px rgba(38,35,74,.45);
  background-image:radial-gradient(color-mix(in srgb,var(--ink,#26234A) 12%,transparent) 1.2px,transparent 1.4px);background-size:20px 20px}
.ws-folha{position:absolute;left:14px;bottom:8px;width:150px;height:150px;z-index:6;filter:drop-shadow(0 12px 14px rgba(38,35,74,.22))}
.ws-folha estudario-folha{width:150px;height:150px;display:block}
.ws-piece{position:absolute;left:170px;right:22px;top:36px;bottom:24px}
.ws-gl{position:absolute;display:block}
.ws-gl svg{width:100%;height:100%;overflow:visible}
.ws-spark{position:absolute;width:10px;height:10px;pointer-events:none}
/* ---------- caderno */
.nb{position:absolute;inset:6px 0 0;display:flex;perspective:1200px}
.nb-page{position:relative;flex:1;background:#FFFEFA;border:2.5px solid ${INK};padding:16px 16px 10px;overflow:visible;
  background-image:repeating-linear-gradient(to bottom,transparent 0 21px,#E7E2F5 21px 22px);background-position:0 30px}
.nb-page.l{border-radius:16px 4px 4px 16px;border-right-width:1.2px}
.nb-page.r{border-radius:4px 16px 16px 4px;border-left-width:1.2px}
.nb-page::before{content:"";position:absolute;top:0;bottom:0;left:26px;width:2px;background:#F4A6C0;opacity:.7}
.nb-page.l::before{display:none}
.nb-flip{position:absolute;right:0;top:0;bottom:0;width:50%;transform-origin:0 50%;background:#FFFEFA;border:2.5px solid ${INK};border-radius:4px 16px 16px 4px;
  backface-visibility:hidden;background-image:repeating-linear-gradient(to bottom,transparent 0 21px,#E7E2F5 21px 22px);background-position:0 30px;z-index:3}
.nb-title{position:relative;height:12px;border-radius:6px;background:${INK};margin:0 0 14px;width:0}
.nb-line{position:relative;height:7px;border-radius:4px;background:#8F8AB8;margin:0 0 15px;width:0}
.nb-line.hl::after{content:"";position:absolute;left:-4px;top:-5px;height:17px;width:0;border-radius:3px;background:rgba(255,210,63,.6);mix-blend-mode:multiply;animation:nbHl .6s ease-out forwards}
@keyframes nbHl{to{width:calc(100% + 8px)}}
.nb-pencil{width:40px;height:40px;z-index:5;right:-33px;top:-31px;transform:scaleX(-1) rotate(-6deg);pointer-events:none}
.nb-pencil.writing svg{animation:nbWrite .16s ease-in-out infinite alternate}
@keyframes nbWrite{from{transform:translate(0,0) rotate(-3deg)}to{transform:translate(1px,-3px) rotate(3deg)}}
.nb-card{border:2.5px solid ${INK};border-radius:12px;background:#ECE9FF;padding:10px;margin:2px 0 12px;box-shadow:0 4px 0 ${INK};transform:scale(0);transform-origin:20% 80%}
.nb-card i{display:block;height:6px;border-radius:3px;background:#5B4BF5;margin:4px 0;opacity:.75}
.nb-tip{position:absolute;right:12px;bottom:12px;width:86px;height:62px;background:#FFE36E;border-radius:3px 3px 12px 3px;rotate:4deg;box-shadow:0 8px 12px -8px rgba(38,35,74,.5);padding:8px;transform:translateY(-140px) rotate(-30deg);opacity:0}
.nb-tip i{display:block;height:5px;border-radius:3px;background:#B98A00;margin:5px 0;opacity:.6}
/* ---------- questões */
.qz-card{position:absolute;left:50%;top:6px;width:248px;margin-left:-124px;background:#FFFEFA;border:2.5px solid ${INK};border-radius:18px;box-shadow:0 6px 0 ${INK};padding:14px 14px 10px}
.qz-card .st{height:7px;border-radius:4px;background:#8F8AB8;margin:0 0 8px}
.qz-opt{display:flex;align-items:center;gap:8px;padding:5px 8px;border:2px solid #DDD6F0;border-radius:10px;margin:6px 0;transition:background .2s,border-color .2s,transform .2s}
.qz-opt b{width:18px;height:18px;border-radius:50%;display:grid;place-items:center;background:#ECE9FF;color:${INK};font:900 10px Nunito,system-ui,sans-serif;flex:none}
.qz-opt i{flex:1;height:6px;border-radius:3px;background:#C9C3E3}
.qz-opt.scan{border-color:#9D93FF;transform:translateX(3px)}
.qz-opt.ok{background:#D9F7EA;border-color:#1FA97F}
.qz-opt.ok b{background:#1FA97F;color:#fff}
.qz-stamp{position:absolute;right:-16px;top:-18px;width:62px;height:62px;z-index:4}
.qz-pile{position:absolute;left:4px;bottom:0;width:120px;height:80px}
.qz-mini{position:absolute;left:0;bottom:0;width:108px;height:70px;background:#FFFEFA;border:2px solid ${INK};border-radius:10px;box-shadow:0 3px 0 ${INK}}
.qz-mini::after{content:"";position:absolute;left:10px;right:24px;top:12px;height:5px;border-radius:3px;background:#C9C3E3;box-shadow:0 11px 0 #C9C3E3,0 22px 0 #9BE3C6}
/* ---------- plano */
.pl{position:absolute;inset:4px 4px 0 4px;display:grid;grid-template-columns:repeat(7,1fr);gap:8px}
.pl-col{position:relative;display:flex;flex-direction:column-reverse;gap:4px;border-radius:12px;background:color-mix(in srgb,#ECE9FF 70%,transparent);border:2px dashed #CFC5F0;padding:4px 4px 4px;overflow:visible}
.pl-col.today{background:#FFF3C4;border-color:#FFB421}
.pl-day{position:absolute;top:-24px;left:0;right:0;text-align:center;font:900 12px Nunito,system-ui,sans-serif;color:#6B6886}
.pl-col.today .pl-day{color:#C47F00}
.pl-blk{border-radius:8px;border:2px solid ${INK};box-shadow:0 3px 0 ${INK};flex:none;position:relative}
.pl-blk::after{content:"";position:absolute;left:6px;right:10px;top:6px;height:4px;border-radius:2px;background:rgba(255,255,255,.55)}
/* ---------- edital */
.ed-doc{position:absolute;left:0;top:0;bottom:0;width:190px;background:#FFFEFA;border:2.5px solid ${INK};border-radius:14px;padding:14px 14px;box-shadow:0 5px 0 ${INK};overflow:hidden}
.ed-doc::before{content:"PDF";position:absolute;right:10px;top:8px;font:900 10px Nunito,system-ui,sans-serif;color:#fff;background:#FF5C4D;border-radius:6px;padding:2px 6px}
.ed-ln{height:6px;border-radius:3px;background:#C9C3E3;margin:9px 0;transition:background .25s}
.ed-ln.lit{background:#9D93FF}
.ed-lupa{width:62px;height:62px;z-index:5;left:120px;top:0}
.ed-list{position:absolute;left:214px;right:0;top:0;display:flex;flex-direction:column;gap:7px}
.ed-chip{display:flex;align-items:center;gap:8px;padding:7px 10px;border-radius:12px;background:#FFFEFA;border:2px solid ${INK};box-shadow:0 3px 0 ${INK};font:800 12.5px Nunito,system-ui,sans-serif;color:${INK};white-space:nowrap;overflow:hidden;text-overflow:ellipsis}
.ed-chip span{width:12px;height:12px;border-radius:4px;flex:none}
`;
  let styled = false;
  function style() { if (styled) return; styled = true; const s = document.createElement("style"); s.textContent = css; document.head.appendChild(s); }

  class Workshop extends HTMLElement {
    // A tela em volta (o Compose) às vezes tira e põe o elemento de novo no mesmo instante. A cena
    // não pode recomeçar nem duplicar por isso: ela só para se o elemento sair de vez.
    connectedCallback() {
      style();
      window.__wsConnects = (window.__wsConnects || 0) + 1;
      if (this._stopTimer) { clearTimeout(this._stopTimer); this._stopTimer = 0; }
      if (this.ro) this.ro.observe(this);
      if (this.alive) return;
      this.alive = true;
      this._token = (this._token || 0) + 1;
      this.timers = new Set();
      this.innerHTML = `<div class="ws"><div class="ws-desk"></div><div class="ws-piece"></div><div class="ws-folha"><estudario-folha mood="thinking" size="150"></estudario-folha></div></div>`;
      this.stage = this.querySelector(".ws");
      this.piece = this.querySelector(".ws-piece");
      this.folha = this.querySelector("estudario-folha");
      const fit = () => { const w = this.clientWidth || 580; this.stage.style.transform = `scale(${w / 580})`; };
      if (!this.ro) this.ro = new ResizeObserver(fit);
      this.ro.observe(this); fit();
      const kind = this.getAttribute("kind") || "material";
      const run = { plan: this.plan, material: this.material, questions: this.questions, edital: this.edital }[kind] || this.material;
      window.__wsRuns = (window.__wsRuns || 0) + 1;
      run.call(this);
    }
    disconnectedCallback() {
      this._stopTimer = setTimeout(() => {
        if (this.isConnected) return;
        this.alive = false;
        this.timers.forEach(clearTimeout); this.timers.clear();
        this.ro && this.ro.disconnect();
      }, 300);
    }
    // Uma cena antiga (de antes de o elemento sair e voltar) para no próximo passo: a espera dela não acaba.
    wait(ms) { const tk = this._token; return new Promise((r) => { const t = setTimeout(() => { this.timers.delete(t); if (tk === this._token) r(); }, ms); this.timers.add(t); }); }
    mood(m, back, ms) {
      if (!this.folha) return;
      this.folha.setAttribute("mood", m);
      if (back) this.wait(ms || 1400).then(() => this.alive && this.folha.setAttribute("mood", back));
    }
    sticker(name, cls, parent) {
      const s = document.createElement("span"); s.className = "ws-gl " + (cls || ""); s.innerHTML = glyph(name);
      (parent || this.piece).appendChild(s); return s;
    }
    sparks(x, y, parent) {
      if (reduce()) return;
      for (let i = 0; i < 9; i++) {
        const d = document.createElement("span"); d.className = "ws-spark";
        const c = pick(COLORS), round = i % 3 === 0;
        d.style.cssText = `left:${x}px;top:${y}px;background:${c};border-radius:${round ? "50%" : "2px"};width:${round ? 8 : 6}px;height:${round ? 8 : 12}px`;
        (parent || this.piece).appendChild(d);
        const a = (i / 9) * Math.PI * 2 + rand(-.3, .3), r = rand(34, 70);
        d.animate([{ transform: "translate(0,0) rotate(0) scale(.4)", opacity: 1 }, { transform: `translate(${Math.cos(a) * r}px,${Math.sin(a) * r}px) rotate(${rand(-300, 300)}deg) scale(1)`, opacity: 0 }],
          { duration: rand(650, 950), easing: "cubic-bezier(.15,.7,.3,1)", fill: "forwards" }).finished.then(() => d.remove());
      }
    }

    // ------------------------------------------------------------- matéria: o caderno se escreve
    async material() {
      this.mood("thinking");
      const p = this.piece;
      p.innerHTML = `<div class="nb"><div class="nb-page l"><div class="nb-title"></div><div class="nb-lines"></div></div><div class="nb-page r"><div class="nb-right"></div></div></div>`;
      const left = p.querySelector(".nb-page.l"), lines = p.querySelector(".nb-lines"), right = p.querySelector(".nb-right"), title = p.querySelector(".nb-title");
      const pencil = this.sticker("Pencil", "nb-pencil");
      const still = reduce();
      const pos = (el) => { if (pencil.parentNode !== el) el.appendChild(pencil); };
      const write = async (el, w, ms) => {
        if (still) { el.style.width = w + "%"; return; }
        pencil.classList.add("writing");
        pos(el);
        const steps = Math.max(6, Math.round(ms / 60));
        for (let i = 1; i <= steps && this.alive; i++) {
          el.style.width = (w * i / steps) + "%";
          await this.wait(ms / steps);
        }
        pencil.classList.remove("writing");
      };
      while (this.alive) {
        title.style.width = "0"; lines.innerHTML = ""; right.innerHTML = ""; p.querySelectorAll(".nb-tip").forEach((e) => e.remove());
        pos(title, 0);
        await write(title, rand(55, 75), 700);
        for (let i = 0; i < 7 && this.alive; i++) {
          const ln = document.createElement("div"); ln.className = "nb-line"; lines.appendChild(ln);
          await write(ln, i === 6 ? rand(35, 55) : rand(70, 98), rand(520, 760));
          if (Math.random() < .3) ln.classList.add("hl");
          await this.wait(90);
        }
        if (!this.alive) return;
        // Página da direita: um flashcard aparece e uma dica cai colada.
        const card = document.createElement("div"); card.className = "nb-card"; card.innerHTML = "<i style='width:80%'></i><i style='width:60%'></i><i style='width:70%'></i>";
        right.appendChild(card);
        card.animate([{ transform: "scale(0) rotate(-8deg)" }, { transform: "scale(1.08) rotate(2deg)", offset: .7 }, { transform: "scale(1)" }], { duration: 520, easing: "cubic-bezier(.3,1.4,.5,1)", fill: "forwards" });
        for (let i = 0; i < 3; i++) { const ln = document.createElement("div"); ln.className = "nb-line"; right.appendChild(ln); pos(ln, 0); await write(ln, rand(60, 90), 420); }
        const tip = document.createElement("div"); tip.className = "nb-tip"; tip.innerHTML = "<i style='width:80%'></i><i style='width:65%'></i><i style='width:50%'></i>";
        p.querySelector(".nb-page.r").appendChild(tip);
        tip.animate([{ transform: "translateY(-140px) rotate(-30deg)", opacity: 0 }, { transform: "translateY(6px) rotate(6deg)", opacity: 1, offset: .75 }, { transform: "translateY(0) rotate(4deg)", opacity: 1 }], { duration: 650, easing: "cubic-bezier(.3,1.3,.5,1)", fill: "forwards" });
        this.mood("happy", "thinking", 1300);
        const pr = p.getBoundingClientRect(), tr = tip.getBoundingClientRect(), k = pr.width / p.offsetWidth || 1;
        this.sparks((tr.left - pr.left) / k + 40, (tr.top - pr.top) / k + 30);
        await this.wait(1300);
        if (!this.alive || still) { if (still) return; }
        // Vira a página.
        const flip = document.createElement("div"); flip.className = "nb-flip"; p.querySelector(".nb").appendChild(flip);
        await flip.animate([{ transform: "rotateY(0)" }, { transform: "rotateY(-180deg)" }], { duration: 750, easing: "cubic-bezier(.45,.05,.3,1)" }).finished;
        flip.remove();
      }
    }

    // ------------------------------------------------------------- questões: cartas corrigidas
    async questions() {
      this.mood("talking");
      const p = this.piece;
      p.innerHTML = `<div class="qz-pile"></div>`;
      const pile = p.querySelector(".qz-pile");
      let made = 0;
      const still = reduce();
      while (this.alive) {
        const card = document.createElement("div"); card.className = "qz-card";
        const letters = ["A", "B", "C", "D"];
        card.innerHTML = `<div class="st" style="width:92%"></div><div class="st" style="width:70%"></div>` +
          letters.map((l) => `<div class="qz-opt"><b>${l}</b><i style="width:${rand(45, 90)}%;flex:none"></i></div>`).join("");
        p.appendChild(card);
        if (!still) await card.animate([{ transform: "translateX(260px) rotate(14deg)", opacity: 0 }, { transform: "translateX(-6px) rotate(-2deg)", opacity: 1, offset: .75 }, { transform: "translateX(0) rotate(-1deg)", opacity: 1 }], { duration: 600, easing: "cubic-bezier(.2,.9,.3,1.1)", fill: "forwards" }).finished;
        const opts = [...card.querySelectorAll(".qz-opt")], right = (Math.random() * 4) | 0;
        // O Folha confere uma a uma.
        for (let i = 0; i < opts.length && this.alive && !still; i++) { opts[i].classList.add("scan"); await this.wait(230); opts[i].classList.remove("scan"); }
        opts[right].classList.add("ok");
        const stamp = this.sticker("Check", "qz-stamp", card);
        if (!still) {
          stamp.animate([{ transform: "scale(2.6) rotate(-20deg)", opacity: 0 }, { transform: "scale(.9) rotate(8deg)", opacity: 1, offset: .6 }, { transform: "scale(1) rotate(6deg)", opacity: 1 }], { duration: 420, easing: "cubic-bezier(.3,1.4,.5,1)", fill: "forwards" });
          card.animate([{ transform: "rotate(-1deg)" }, { transform: "rotate(-1deg) translateY(4px) scale(.985)" }, { transform: "rotate(-1deg)" }], { duration: 260, delay: 180 });
          this.sparks(card.offsetLeft + card.offsetWidth - 10, card.offsetTop + 10);
          this.mood("happy", "talking", 900);
        }
        if (still) return;
        await this.wait(800);
        if (!this.alive) return;
        // A carta pronta voa para a pilha.
        const pr = pile.getBoundingClientRect(), cr = card.getBoundingClientRect(), k = p.getBoundingClientRect().width / p.offsetWidth || 1;
        const dx = (pr.left - cr.left) / k - 60, dy = (pr.bottom - cr.bottom) / k + 30;
        await card.animate([{ transform: "rotate(-1deg)", opacity: 1 }, { transform: `translate(${dx}px,${dy}px) rotate(${rand(-14, 8)}deg) scale(.42)`, opacity: .2 }], { duration: 520, easing: "cubic-bezier(.5,0,.3,1)", fill: "forwards" }).finished;
        card.remove();
        const mini = document.createElement("div"); mini.className = "qz-mini";
        mini.style.transform = `translate(${made % 4 * 3}px, ${-(made % 4) * 6}px) rotate(${rand(-10, 8)}deg)`;
        pile.appendChild(mini);
        mini.animate([{ transform: mini.style.transform + " scale(1.2)" }, { transform: mini.style.transform }], { duration: 220 });
        made++;
        if (pile.children.length > 5) pile.firstChild.remove();
        await this.wait(200);
      }
    }

    // ------------------------------------------------------------- plano: a semana se monta
    async plan() {
      this.mood("thinking");
      const p = this.piece;
      const days = ["S", "T", "Q", "Q", "S", "S", "D"];
      const today = (new Date().getDay() + 6) % 7;
      p.innerHTML = `<div class="pl" style="top:26px">${days.map((d, i) => `<div class="pl-col${i === today ? " today" : ""}"><span class="pl-day">${d}</span></div>`).join("")}</div>`;
      const cols = [...p.querySelectorAll(".pl-col")];
      const subjects = COLORS.slice(0, 6);
      const still = reduce();
      const fill = (n) => {
        for (let i = 0; i < n; i++) {
          const col = cols[(Math.random() * 7) | 0];
          const b = document.createElement("div"); b.className = "pl-blk";
          b.style.cssText = `height:${rand(18, 40)}px;background:${pick(subjects)}`;
          col.appendChild(b);
        }
      };
      if (still) { fill(16); return; }
      let drops = 0;
      while (this.alive) {
        // Coluna com menos carga recebe primeiro: o plano equilibra os dias.
        const heights = cols.map((c) => [...c.querySelectorAll(".pl-blk")].reduce((s, b) => s + b.offsetHeight + 4, 0));
        const free = heights.map((h, i) => [h + rand(0, 40), i]).sort((a, b) => a[0] - b[0]);
        const ci = free[0][1], col = cols[ci];
        if (heights[ci] > 190) {
          // Semana cheia: confere, comemora e começa a próxima.
          this.mood("happy", "thinking", 1500);
          cols.forEach((c, i) => c.animate([{ transform: "scale(1)" }, { transform: "scale(1.06)" }, { transform: "scale(1)" }], { duration: 360, delay: i * 60 }));
          this.sparks(p.offsetWidth / 2, 60);
          await this.wait(1100);
          const blocks = [...p.querySelectorAll(".pl-blk")];
          await Promise.all(blocks.map((b, i) => b.animate([{ transform: "none", opacity: 1 }, { transform: "translateY(-14px) scale(.6)", opacity: 0 }], { duration: 320, delay: i * 18, fill: "forwards" }).finished));
          blocks.forEach((b) => b.remove());
          continue;
        }
        const b = document.createElement("div"); b.className = "pl-blk";
        const h = rand(18, 42);
        b.style.cssText = `height:${h}px;background:${pick(subjects)}`;
        col.appendChild(b);
        const fall = 240 - heights[ci];
        await b.animate([{ transform: `translateY(${-fall}px) rotate(${rand(-12, 12)}deg)`, opacity: 0 }, { transform: "translateY(4px) scaleY(.82)", opacity: 1, offset: .78 }, { transform: "translateY(0) scaleY(1)", opacity: 1 }],
          { duration: rand(320, 420), easing: "cubic-bezier(.5,0,.6,1.3)", fill: "both" }).finished;
        drops++;
        // De vez em quando o plano reorganiza: um bloco pula para outro dia.
        if (drops % 6 === 0 && this.alive) {
          const from = cols[(Math.random() * 7) | 0], blk = from.querySelector(".pl-blk:last-child");
          const to = cols[(cols.indexOf(from) + 1 + ((Math.random() * 5) | 0)) % 7];
          if (blk && to) {
            const r1 = blk.getBoundingClientRect();
            to.appendChild(blk);
            const r2 = blk.getBoundingClientRect(), k = p.getBoundingClientRect().width / p.offsetWidth || 1;
            const dx = (r1.left - r2.left) / k, dy = (r1.top - r2.top) / k;
            blk.animate([{ transform: `translate(${dx}px,${dy}px)` }, { transform: `translate(${dx / 2}px,${dy / 2 - 50}px) rotate(10deg)`, offset: .5 }, { transform: "none" }], { duration: 560, easing: "cubic-bezier(.4,0,.3,1)" });
          }
        }
        await this.wait(rand(50, 130));
      }
    }

    // ------------------------------------------------------------- edital: a lupa encontra as matérias
    async edital() {
      this.mood("thinking");
      const p = this.piece;
      p.innerHTML = `<div class="ed-doc">${Array.from({ length: 16 }, () => `<div class="ed-ln" style="width:${rand(55, 100)}%"></div>`).join("")}</div><div class="ed-list"></div>`;
      const lupa = this.sticker("Lupa", "ed-lupa");
      const lines = [...p.querySelectorAll(".ed-ln")], list = p.querySelector(".ed-list");
      const names = ["Português", "Dir. Constitucional", "Dir. Administrativo", "Raciocínio Lógico", "Informática", "Legislação", "Atualidades", "Dir. do Trabalho", "Contabilidade", "Ética"];
      const still = reduce();
      let found = 0;
      if (still) { names.slice(0, 5).forEach((n, i) => { const c = document.createElement("div"); c.className = "ed-chip"; c.innerHTML = `<span style="background:${COLORS[i % COLORS.length]}"></span>${n}`; list.appendChild(c); }); return; }
      while (this.alive) {
        for (let i = 0; i < lines.length && this.alive; i++) {
          const ln = lines[i];
          lupa.style.transition = "top .28s ease, left .28s ease";
          lupa.style.top = ln.offsetTop - 26 + "px";
          lupa.style.left = 40 + Math.sin(i * 1.3) * 60 + "px";
          ln.classList.add("lit");
          await this.wait(300);
          ln.classList.remove("lit");
          if (i % 3 === 2) {
            const c = document.createElement("div"); c.className = "ed-chip";
            const color = COLORS[found % COLORS.length];
            c.innerHTML = `<span style="background:${color}"></span>${names[found % names.length]}`;
            list.appendChild(c);
            const lr = ln.getBoundingClientRect(), cr = c.getBoundingClientRect(), k = p.getBoundingClientRect().width / p.offsetWidth || 1;
            c.animate([{ transform: `translate(${(lr.left - cr.left) / k}px,${(lr.top - cr.top) / k}px) scale(.4)`, opacity: 0 }, { transform: "translate(0,0) scale(1.06)", opacity: 1, offset: .75 }, { transform: "none", opacity: 1 }],
              { duration: 560, easing: "cubic-bezier(.3,1.2,.5,1)", fill: "both" });
            found++;
            if (list.children.length > 6) {
              const old = list.firstChild;
              old.animate([{ opacity: 1, transform: "none" }, { opacity: 0, transform: "translateX(30px)" }], { duration: 250, fill: "forwards" }).finished.then(() => old.remove());
            }
            if (found % 4 === 0) this.mood("happy", "thinking", 1100);
          }
        }
      }
    }
  }
  customElements.define("estudario-workshop", Workshop);
})();
