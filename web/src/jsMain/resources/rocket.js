// O foguete do Estudário: desenhado no traço da turma (contorno de tinta, borda de adesivo, rostinho)
// e a decolagem do plano. Ao abrir o plano pela primeira vez, o foguete sai do botão, faz a curva
// subindo pela tela com fumaça, explode em confete e adesivos da turma, e o Folha aparece no meio
// comemorando. Um clique em qualquer lugar pula a cena. Quem pede menos movimento vê só o Folha.
//
// estudarioRocketSvg: o desenho (para o botão).
// estudarioRocketLaunch(elementoDoFoguete, { line, sub }) -> Promise que resolve ao fim da cena.
(function () {
  const INK = "#26234A";
  const S = `stroke="${INK}" stroke-width="4.5" stroke-linecap="round" stroke-linejoin="round"`;
  const W = `stroke="#FFFFFF" stroke-width="13" stroke-linecap="round" stroke-linejoin="round" fill="#FFFFFF"`;
  const body = "M50 7C66 21 71 41 68 64H32C29 41 34 21 50 7Z";
  const finL = "M33 49 19 66 21 77 34 67Z";
  const finR = "M67 49 81 66 79 77 66 67Z";
  const nozzle = "M39 63h22l-3 9H42Z";
  window.estudarioRocketSvg =
    `<svg class="rk" viewBox="0 0 100 100" xmlns="http://www.w3.org/2000/svg" aria-hidden="true">` +
    // Chama atrás de tudo (tremula sozinha no CSS).
    `<g class="rk-flame"><path d="M41 70Q50 99 59 70Z" fill="#FFB421" ${S} stroke-width="3.5"/><path d="M45.5 71Q50 88 54.5 71Z" fill="#FFE07A"/></g>` +
    // Borda branca de adesivo.
    `<g><path d="${body}" ${W}/><path d="${finL}" ${W}/><path d="${finR}" ${W}/><path d="${nozzle}" ${W}/></g>` +
    // Aletas, bico, corpo, bocal, janela com reflexo e o rostinho.
    `<path d="${finL}" fill="#5B4BF5" ${S}/><path d="${finR}" fill="#5B4BF5" ${S}/>` +
    `<path d="${nozzle}" fill="#6B6886" ${S}/>` +
    `<path d="${body}" fill="#FFFDF8" ${S}/>` +
    `<path d="M50 7C57 13 61.5 19 63.5 25H36.5C38.5 19 43 13 50 7Z" fill="#FF5C4D" ${S}/>` +
    `<path d="M36 58h28" ${S} stroke-width="3" opacity=".25"/>` +
    `<circle cx="50" cy="37" r="8.5" fill="#8FD3FF" ${S}/><path d="M46 34a5 5 0 0 1 5-2.5" stroke="#FFFFFF" stroke-width="2.6" stroke-linecap="round" fill="none"/>` +
    `<circle cx="44.5" cy="50" r="2.6" fill="${INK}"/><circle cx="55.5" cy="50" r="2.6" fill="${INK}"/>` +
    `<path d="M46.5 54.5Q50 57.5 53.5 54.5" fill="none" ${S} stroke-width="2.8"/>` +
    `<circle cx="41" cy="54" r="2.2" fill="#F4A6C0" opacity=".8"/><circle cx="59" cy="54" r="2.2" fill="#F4A6C0" opacity=".8"/>` +
    `</svg>`;

  const css = `
.rk-overlay{position:fixed;inset:0;z-index:9999;pointer-events:auto;cursor:pointer;overflow:hidden}
.rk-dim{position:absolute;inset:0;background:radial-gradient(circle at 50% 42%,rgba(91,75,245,.30),rgba(17,15,38,.62));-webkit-backdrop-filter:blur(3px);backdrop-filter:blur(3px);opacity:0}
.rk-fly{position:absolute;left:0;top:0;width:56px;height:56px;will-change:transform;filter:drop-shadow(0 10px 14px rgba(17,15,38,.35))}
.rk-fly .rk{width:100%;height:100%;overflow:visible}
.rk .rk-flame{transform-box:fill-box;transform-origin:50% 0;animation:rkFlame .09s ease-in-out infinite alternate}
.rk-fly .rk .rk-flame{animation-duration:.05s;transform:scaleY(1.9)}
@keyframes rkFlame{from{transform:scaleY(.82) scaleX(.94)}to{transform:scaleY(1.12) scaleX(1.04)}}
.rk-puff{position:absolute;left:0;top:0;border-radius:50%;background:radial-gradient(circle at 35% 35%,#FFFFFF,#E7E3FF 60%,#CFC5F0);pointer-events:none}
.rk-ring{position:absolute;border-radius:50%;pointer-events:none}
.rk-bit{position:absolute;left:0;top:0;pointer-events:none;will-change:transform}
.rk-folha{position:absolute;left:50%;top:44%;translate:-50% -50%;display:flex;flex-direction:column;align-items:center;gap:6px;pointer-events:none}
.rk-folha estudario-folha{width:210px;height:210px;display:block}
.rk-say{background:#FFFDF8;color:${INK};border:2.5px solid ${INK};box-shadow:0 5px 0 ${INK};border-radius:22px;padding:14px 22px;text-align:center;max-width:min(440px,86vw);font-family:Nunito,system-ui,sans-serif}
.rk-say b{display:block;font-size:22px;font-weight:900;letter-spacing:-.01em;line-height:1.2}
.rk-say span{display:block;margin-top:4px;font-size:15.5px;font-weight:700;color:#4E4B6B}
.rk-skip{position:absolute;left:50%;bottom:26px;translate:-50% 0;color:rgba(255,255,255,.75);font:700 13px Nunito,system-ui,sans-serif;letter-spacing:.04em}
`;
  let styled = false;
  function style() { if (styled) return; styled = true; const s = document.createElement("style"); s.textContent = css; document.head.appendChild(s); }
  const reduce = () => window.matchMedia && matchMedia("(prefers-reduced-motion: reduce)").matches;
  const rand = (a, b) => a + Math.random() * (b - a);
  const sleep = (ms) => new Promise((r) => setTimeout(r, ms));

  window.estudarioRocketLaunch = function (from, opts) {
    opts = opts || {};
    style();
    return new Promise((resolve) => {
      const W = innerWidth, H = innerHeight;
      const ov = document.createElement("div");
      ov.className = "rk-overlay";
      ov.innerHTML = `<div class="rk-dim"></div>`;
      document.body.appendChild(ov);
      const dim = ov.firstChild;
      let done = false;
      const finish = () => {
        if (done) return; done = true;
        ov.animate([{ opacity: 1 }, { opacity: 0 }], { duration: 380, easing: "ease-in", fill: "forwards" }).finished.then(() => { ov.remove(); if (from) from.style.opacity = ""; resolve(); });
      };
      ov.addEventListener("click", finish);

      const showFolha = async () => {
        const box = document.createElement("div");
        box.className = "rk-folha";
        box.innerHTML = `<estudario-folha mood="happy" size="210"></estudario-folha><div class="rk-say"><b></b><span></span></div>`;
        ov.appendChild(box);
        box.animate([{ transform: "scale(.2) translateY(60px)", opacity: 0 }, { transform: "scale(1.08) translateY(-6px)", opacity: 1, offset: .7 }, { transform: "scale(1)", opacity: 1 }],
          { duration: 620, easing: "cubic-bezier(.2,.9,.3,1.2)", fill: "both" });
        const b = box.querySelector("b"), sp = box.querySelector("span");
        const line = opts.line || "Decolamos! Seu plano está no ar.";
        sp.textContent = opts.sub || "Primeira parada: o estudo de hoje.";
        sp.style.opacity = "0";
        for (let i = 1; i <= line.length && !done; i++) { b.textContent = line.slice(0, i); await sleep(line[i - 1] === " " ? 18 : 28); }
        sp.animate([{ opacity: 0, transform: "translateY(6px)" }, { opacity: 1, transform: "none" }], { duration: 300, fill: "forwards" });
        const hint = document.createElement("div"); hint.className = "rk-skip"; hint.textContent = "toque para continuar";
        ov.appendChild(hint); hint.animate([{ opacity: 0 }, { opacity: 1 }], { duration: 400, delay: 600, fill: "both" });
        await sleep(2300);
        finish();
      };

      dim.animate([{ opacity: 0 }, { opacity: 1 }], { duration: 500, fill: "forwards" });
      if (reduce() || !from) { showFolha(); return; }

      // ---------- decolagem: o foguete do botão vira o foguete da cena
      const r = from.getBoundingClientRect();
      const sx = r.left + r.width / 2, sy = r.top + r.height / 2;
      const ex = W / 2, ey = H * 0.42;
      const cx = sx + (ex - sx) * 0.15, cy = Math.min(sy, ey) - H * 0.38;
      from.style.opacity = "0";
      const fly = document.createElement("div");
      fly.className = "rk-fly";
      fly.innerHTML = window.estudarioRocketSvg;
      ov.appendChild(fly);
      const size = 56, start = r.width / size;
      const at = (t) => {
        const u = 1 - t;
        return [u * u * sx + 2 * u * t * cx + t * t * ex, u * u * sy + 2 * u * t * cy + t * t * ey];
      };
      const angle = (t) => {
        const u = 1 - t;
        const dx = 2 * u * (cx - sx) + 2 * t * (ex - cx), dy = 2 * u * (cy - sy) + 2 * t * (ey - cy);
        return Math.atan2(dy, dx) * 180 / Math.PI + 90;
      };
      // Ignição: tremida no lugar, a chama cresce.
      const ignite = [];
      for (let i = 0; i <= 8; i++) ignite.push({ transform: `translate(${sx - size / 2 + rand(-2.5, 2.5)}px,${sy - size / 2 + rand(-2, 2)}px) scale(${start * (1 + i * 0.02)}) rotate(${rand(-4, 4)}deg)` });
      const FLIGHT = 1150;
      const frames = [];
      const N = 36;
      for (let i = 0; i <= N; i++) {
        const t = i / N, [x, y] = at(t);
        frames.push({ transform: `translate(${x - size / 2}px,${y - size / 2}px) rotate(${angle(Math.max(t, .02))}deg) scale(${start * 1.16 + t * 1.5})` });
      }
      fly.animate(ignite, { duration: 380, easing: "linear", fill: "forwards" }).finished.then(() => {
        const t0 = performance.now();
        fly.animate(frames, { duration: FLIGHT, easing: "cubic-bezier(.55,0,.75,.55)", fill: "forwards" });
        // Fumaça ficando para trás, no ponto em que o foguete passou.
        // A mesma aceleração do voo (aproximada), para a fumaça sair da cauda.
        const ease = (p) => Math.pow(p, 1.7);
        const puff = () => {
          const p = (performance.now() - t0) / FLIGHT;
          if (p >= 1 || done) return explode();
          const t = ease(p), [x, y] = at(t);
          const s = rand(14, 26) * (1 + t);
          const d = document.createElement("div");
          d.className = "rk-puff";
          d.style.width = d.style.height = s + "px";
          ov.insertBefore(d, fly);
          const back = (angle(Math.max(t, .02)) + 90) * Math.PI / 180;
          const ox = x + Math.cos(back) * s * .9 - s / 2, oy = y + Math.sin(back) * s * .9 - s / 2;
          d.animate([{ transform: `translate(${ox}px,${oy}px) scale(.4)`, opacity: .95 }, { transform: `translate(${ox + rand(-16, 16)}px,${oy + rand(6, 26)}px) scale(${rand(1.6, 2.3)})`, opacity: 0 }],
            { duration: rand(650, 950), easing: "cubic-bezier(.2,.7,.3,1)", fill: "forwards" }).finished.then(() => d.remove());
          setTimeout(puff, 26);
        };
        puff();
      });

      // ---------- explosão: clarão, anéis, confete e a turma voando
      let exploded = false;
      const explode = () => {
        if (exploded || done) return; exploded = true;
        fly.animate([{ opacity: 1 }, { opacity: 0, transform: fly.style.transform }], { duration: 90, fill: "forwards" });
        fly.remove();
        ov.animate([{ transform: "translate(0,0)" }, { transform: "translate(-7px,4px)" }, { transform: "translate(6px,-5px)" }, { transform: "translate(-4px,3px)" }, { transform: "translate(2px,-1px)" }, { transform: "none" }], { duration: 380, easing: "ease-out" });
        const flash = document.createElement("div");
        flash.className = "rk-ring";
        flash.style.cssText = `left:${ex - 60}px;top:${ey - 60}px;width:120px;height:120px;background:radial-gradient(circle,#FFFFFF,#FFE07A 40%,rgba(255,180,33,0) 70%)`;
        ov.appendChild(flash);
        flash.animate([{ transform: "scale(.2)", opacity: 1 }, { transform: "scale(4.2)", opacity: 0 }], { duration: 520, easing: "cubic-bezier(.1,.8,.3,1)", fill: "forwards" });
        [["#FFD23F", 0], ["#5B4BF5", 90], ["#FF5C4D", 170]].forEach(([c, delay]) => {
          const ring = document.createElement("div");
          ring.className = "rk-ring";
          ring.style.cssText = `left:${ex - 50}px;top:${ey - 50}px;width:100px;height:100px;border:5px solid ${c}`;
          ov.appendChild(ring);
          ring.animate([{ transform: "scale(.3)", opacity: 1, borderWidth: "10px" }, { transform: "scale(5.5)", opacity: 0, borderWidth: "1px" }], { duration: 900, delay, easing: "cubic-bezier(.15,.75,.3,1)", fill: "both" });
        });
        const colors = ["#FFD23F", "#FFB421", "#5B4BF5", "#9D93FF", "#22C59A", "#FF5C4D", "#F4A6C0", "#8FD3FF"];
        for (let i = 0; i < 70; i++) {
          const el = document.createElement("div");
          el.className = "rk-bit";
          const w = rand(6, 11), h = rand(10, 18), round = Math.random() < .3;
          el.style.cssText = `width:${round ? w : w}px;height:${round ? w : h}px;background:${colors[i % colors.length]};border-radius:${round ? "50%" : "2px"}`;
          ov.appendChild(el);
          const a = rand(0, Math.PI * 2), dist = rand(140, Math.max(W, H) * .55);
          const mx = ex + Math.cos(a) * dist, my = ey + Math.sin(a) * dist;
          el.animate([
            { transform: `translate(${ex}px,${ey}px) rotate(0deg) scale(.4)`, opacity: 1 },
            { transform: `translate(${mx}px,${my}px) rotate(${rand(-540, 540)}deg) scale(1)`, opacity: 1, offset: .55 },
            { transform: `translate(${mx + rand(-30, 30)}px,${my + rand(120, 260)}px) rotate(${rand(-900, 900)}deg) scale(.9)`, opacity: 0 },
          ], { duration: rand(1300, 2000), easing: "cubic-bezier(.15,.7,.35,1)", fill: "forwards" });
        }
        const names = ["Star", "Trophy", "Bolt", "Heart", "Check", "Fire", "Medal", "Gem", "Books", "Bulb"];
        if (window.estudarioGlyphUri) names.forEach((n, i) => {
          const u = window.estudarioGlyphUri(n); if (!u) return;
          const img = document.createElement("img");
          img.className = "rk-bit"; img.src = u; img.alt = "";
          const s = rand(46, 64);
          img.style.cssText = `width:${s}px;height:${s}px;filter:drop-shadow(0 6px 8px rgba(17,15,38,.3))`;
          ov.appendChild(img);
          const a = (i / names.length) * Math.PI * 2 + rand(-.25, .25), dist = rand(200, Math.min(W, H) * .5);
          const mx = ex + Math.cos(a) * dist - s / 2, my = ey + Math.sin(a) * dist - s / 2;
          img.animate([
            { transform: `translate(${ex - s / 2}px,${ey - s / 2}px) scale(.2) rotate(0deg)`, opacity: 0 },
            { transform: `translate(${mx}px,${my}px) scale(1.15) rotate(${rand(-30, 30)}deg)`, opacity: 1, offset: .45 },
            { transform: `translate(${mx}px,${my + 40}px) scale(1) rotate(${rand(-50, 50)}deg)`, opacity: 0 },
          ], { duration: rand(1600, 2100), easing: "cubic-bezier(.2,.8,.3,1)", fill: "forwards" });
        });
        setTimeout(showFolha, 260);
      };
    });
  };
})();
