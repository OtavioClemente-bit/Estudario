// Grifos da teoria pintados no texto, como no app. Usa a CSS Custom Highlight API: o navegador pinta
// os trechos sem mexer no HTML (o Compose continua dono da página). Sem suporte, os blocos grifados
// seguem com a marca amarela na margem.
(function () {
  "use strict";
  const NAME = "estudario-grifo";
  const squash = (s) => s.replace(/\s+/g, " ").trim().toLowerCase();

  // Range do trecho [quote] dentro de [root], tolerando quebras e espaços diferentes.
  function rangeFor(root, quote) {
    const want = squash(quote);
    if (!want) return null;
    const nodes = [];
    const walker = document.createTreeWalker(root, NodeFilter.SHOW_TEXT);
    let text = "";
    const map = []; // posição no texto achatado -> [nó, deslocamento]
    for (let n = walker.nextNode(); n; n = walker.nextNode()) {
      nodes.push(n);
      const v = n.nodeValue;
      for (let i = 0; i < v.length; i++) {
        const ch = /\s/.test(v[i]) ? " " : v[i].toLowerCase();
        if (ch === " " && (text.length === 0 || text[text.length - 1] === " ")) continue;
        text += ch;
        map.push([n, i]);
      }
    }
    const at = text.indexOf(want);
    if (at < 0) return null;
    const start = map[at], end = map[at + want.length - 1];
    if (!start || !end) return null;
    const r = document.createRange();
    r.setStart(start[0], start[1]);
    r.setEnd(end[0], end[1] + 1);
    return r;
  }

  // Os trechos pintados de cada teoria, com o id do grifo, para saber em qual a pessoa clicou.
  const marksOf = {};

  // O grifo sob o ponto (x, y) da tela, ou null: é por ele que dá para tirar um grifo no próprio texto.
  window.estudarioMarkAt = function (theoryId, x, y) {
    const list = marksOf[theoryId];
    if (!list || !list.length) return null;
    let node = null, offset = 0;
    if (document.caretPositionFromPoint) {
      const p = document.caretPositionFromPoint(x, y);
      if (p) { node = p.offsetNode; offset = p.offset; }
    } else if (document.caretRangeFromPoint) {
      const r = document.caretRangeFromPoint(x, y);
      if (r) { node = r.startContainer; offset = r.startOffset; }
    }
    if (!node) return null;
    for (const m of list) {
      try {
        if (m.range.isPointInRange(node, offset)) {
          // O ponto pode cair logo depois do fim do trecho: confere se o clique está dentro do desenho.
          for (const rect of m.range.getClientRects()) {
            if (x >= rect.left - 2 && x <= rect.right + 2 && y >= rect.top - 2 && y <= rect.bottom + 2) return m.id;
          }
        }
      } catch (e) { /* trecho fora do documento */ }
    }
    return null;
  };

  // marks: [{ id, block: número do bloco, quote: texto }]; os blocos têm id "tb-<theoryId>-<bloco>".
  window.estudarioHighlight = function (theoryId, marks) {
    if (!window.CSS || !CSS.highlights || typeof Highlight === "undefined") return false;
    const ranges = [];
    const noted = [];
    const byId = [];
    for (const m of marks || []) {
      const el = document.getElementById(`tb-${theoryId}-${m.block}`);
      const r = el && rangeFor(el, m.quote);
      if (r) { ranges.push(r); if (m.note) noted.push(r); if (m.id != null) byId.push({ id: m.id, range: r }); }
    }
    marksOf[theoryId] = byId;
    const key = `${NAME}-${theoryId}`;
    if (ranges.length) CSS.highlights.set(key, new Highlight(...ranges));
    else CSS.highlights.delete(key);
    // Um nome só para o CSS: junta todos os leitores abertos.
    const all = [];
    for (const [k, h] of CSS.highlights) if (k.startsWith(NAME + "-") && !k.startsWith(NAME + "-nota")) for (const r of h) all.push(r);
    if (all.length) CSS.highlights.set(NAME, new Highlight(...all)); else CSS.highlights.delete(NAME);
    // Trechos com nota ganham um sublinhado pontilhado: dá para ver onde há nota antes de passar o mouse.
    const notedKey = NAME + "-nota-" + theoryId;
    if (noted.length) CSS.highlights.set(notedKey, new Highlight(...noted)); else CSS.highlights.delete(notedKey);
    const allNoted = [];
    for (const [k, h] of CSS.highlights) if (k.startsWith(NAME + "-nota-")) for (const r of h) allNoted.push(r);
    if (allNoted.length) CSS.highlights.set(NAME + "-nota", new Highlight(...allNoted)); else CSS.highlights.delete(NAME + "-nota");
    return true;
  };

  // A seleção atual dentro de um bloco da teoria: { theoryId, block, quote, x, y } ou null.
  // A seleção atual na teoria: { theoryId, block, quote, parts: [{ block, quote }], x, y } ou null.
  // Pode atravessar vários parágrafos: cada parágrafo tocado vira uma parte (um grifo em cada um).
  window.estudarioSelection = function () {
    const sel = window.getSelection && window.getSelection();
    if (!sel || sel.isCollapsed || sel.rangeCount === 0) return null;
    const range = sel.getRangeAt(0);
    const blockOf = (node) => {
      const el = node && (node.nodeType === 1 ? node : node.parentElement);
      return el && el.closest ? el.closest("[id^='tb-']") : null;
    };
    const parse = (el) => { const m = el && /^tb-(\d+)-(\d+)$/.exec(el.id); return m ? { theoryId: Number(m[1]), block: Number(m[2]) } : null; };
    // Começo ou fim fora de um bloco (a seleção passou do texto): usa o primeiro/último bloco tocado.
    const all = Array.from(document.querySelectorAll("[id^='tb-']")).filter((el) => range.intersectsNode(el));
    const first = parse(blockOf(range.startContainer) || all[0]);
    const last = parse(blockOf(range.endContainer) || all[all.length - 1]);
    if (!first || !last || first.theoryId !== last.theoryId) return null;
    const parts = [];
    for (let b = first.block; b <= last.block; b++) {
      const el = document.getElementById(`tb-${first.theoryId}-${b}`);
      if (!el || !range.intersectsNode(el)) continue;
      const piece = document.createRange();
      piece.selectNodeContents(el);
      if (b === first.block && el.contains(range.startContainer)) piece.setStart(range.startContainer, range.startOffset);
      if (b === last.block && el.contains(range.endContainer)) piece.setEnd(range.endContainer, range.endOffset);
      const quote = piece.toString().replace(/\s+/g, " ").trim();
      if (quote.length >= 3) parts.push({ block: b, quote: quote.slice(0, 1200) });
    }
    if (parts.length === 0 || parts.length > 12) return null;
    const rect = range.getBoundingClientRect();
    const top = rect.top < 60 ? Math.max(60, rect.top) : rect.top;
    return { theoryId: first.theoryId, block: parts[0].block, quote: parts.map((p) => p.quote).join(" … "), parts, x: rect.left + rect.width / 2, y: top };
  };
})();
