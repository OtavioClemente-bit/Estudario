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

  // marks: [{ block: número do bloco, quote: texto }]; os blocos têm id "tb-<theoryId>-<bloco>".
  window.estudarioHighlight = function (theoryId, marks) {
    if (!window.CSS || !CSS.highlights || typeof Highlight === "undefined") return false;
    const ranges = [];
    for (const m of marks || []) {
      const el = document.getElementById(`tb-${theoryId}-${m.block}`);
      const r = el && rangeFor(el, m.quote);
      if (r) ranges.push(r);
    }
    const key = `${NAME}-${theoryId}`;
    if (ranges.length) CSS.highlights.set(key, new Highlight(...ranges));
    else CSS.highlights.delete(key);
    // Um nome só para o CSS: junta todos os leitores abertos.
    const all = [];
    for (const [k, h] of CSS.highlights) if (k.startsWith(NAME + "-")) for (const r of h) all.push(r);
    if (all.length) CSS.highlights.set(NAME, new Highlight(...all)); else CSS.highlights.delete(NAME);
    return true;
  };

  // A seleção atual dentro de um bloco da teoria: { theoryId, block, quote, x, y } ou null.
  window.estudarioSelection = function () {
    const sel = window.getSelection && window.getSelection();
    if (!sel || sel.isCollapsed || sel.rangeCount === 0) return null;
    const quote = sel.toString().replace(/\s+/g, " ").trim();
    if (quote.length < 3 || quote.length > 1200) return null;
    let node = sel.getRangeAt(0).commonAncestorContainer;
    if (node.nodeType !== 1) node = node.parentElement;
    const block = node && node.closest && node.closest("[id^='tb-']");
    if (!block) return null;
    const m = /^tb-(\d+)-(\d+)$/.exec(block.id);
    if (!m) return null;
    const rect = sel.getRangeAt(0).getBoundingClientRect();
    return { theoryId: Number(m[1]), block: Number(m[2]), quote, x: rect.left + rect.width / 2, y: rect.top };
  };
})();
