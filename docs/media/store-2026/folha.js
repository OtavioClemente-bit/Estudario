// O Folha em SVG, traduzido de app/.../ui/assistant/Folha.kt (mesma geometria, viewBox 0 0 100 100),
// para as artes da loja. folhaSvg({ mood, size, look, tilt, hop, shadow }) devolve o <svg>.
(function () {
  const INK = "#26215C", LINE = "#C9C5F5", STACK = "#D9D6FF", CAP = "#231C6B", CAPTOP = "#2F2789";
  const GOLD = "#F5B83D", MINT = "#7EE0B8", MINTD = "#3FBF8F", CHEEK = "#F4A6C0", SPARK = "#FFE07A";
  let uid = 0;

  const leftPage = (dy = 0, dx = 0) =>
    `M${12 + dx} ${40 + dy}Q${30 + dx} ${36.5 + dy} ${48.5 + dx} ${43 + dy}L${48.5 + dx} ${87 + dy}Q${30 + dx} ${80.5 + dy} ${12 + dx} ${84 + dy}Z`;
  const rightPage = (dy = 0, dx = 0) =>
    `M${51.5 + dx} ${43 + dy}Q${70 + dx} ${36.5 + dy} ${88 + dx} ${40 + dy}L${88 + dx} ${84 + dy}Q${70 + dx} ${80.5 + dy} ${51.5 + dx} ${87 + dy}Z`;
  const star = (x, y, r, fill) =>
    `<path fill="${fill}" d="M${x} ${y - r * 2}Q${x} ${y} ${x + r * 2} ${y}Q${x} ${y} ${x} ${y + r * 2}Q${x} ${y} ${x - r * 2} ${y}Q${x} ${y} ${x} ${y - r * 2}Z"/>`;

  function arms(mood, armColor) {
    const arm = (sx, sy, hx, hy) => {
      const ex = (sx + hx) / 2 + (hx < 50 ? -2 : 2), ey = (sy + hy) / 2 + 2;
      return `<path d="M${sx} ${sy}Q${ex} ${ey} ${hx} ${hy}" stroke="${armColor}" stroke-width="3.2" fill="none" stroke-linecap="round"/>` +
        `<circle cx="${hx}" cy="${hy}" r="3.3" fill="#fff" stroke="${armColor}" stroke-width="1.4"/>`;
    };
    switch (mood) {
      case "talking": return arm(12, 66, 3, 76) + arm(88, 66, 98, 50);
      case "happy": return arm(12, 66, 2, 45) + arm(88, 66, 98, 45);
      case "thinking": return arm(12, 66, 4, 77) + arm(88, 66, 93, 70);
      case "point": return arm(12, 66, 4, 78) + arm(88, 66, 100, 60);
      default: return arm(12, 66, 4, 78) + arm(88, 66, 96, 78);
    }
  }

  function face(mood, look) {
    const [lx, ly] = look;
    const eL = [31 + lx * 1.6, 61 + ly * 1.4], eR = [69 + lx * 1.6, 61 + ly * 1.4];
    const cheekA = mood === "happy" ? 0.7 : 0.45;
    let s = `<ellipse cx="23.5" cy="69.5" rx="4.5" ry="2.5" fill="${CHEEK}" opacity="${cheekA}"/>` +
      `<ellipse cx="76.5" cy="69.5" rx="4.5" ry="2.5" fill="${CHEEK}" opacity="${cheekA}"/>`;
    const eye = ([x, y], wink) => {
      if (mood === "happy" || wink) {
        return `<path d="M${x - 4.5} ${y + 1.5}Q${x} ${y - 5} ${x + 4.5} ${y + 1.5}" stroke="${INK}" stroke-width="2.6" fill="none" stroke-linecap="round"/>`;
      }
      return `<ellipse cx="${x}" cy="${y}" rx="4.4" ry="5.8" fill="${INK}"/>` +
        `<circle cx="${x - 1.4 + lx * 0.6}" cy="${y - 2.2}" r="1.7" fill="#fff"/>` +
        `<circle cx="${x + 1.6}" cy="${y + 1.8}" r="0.8" fill="#fff" opacity="0.7"/>`;
    };
    s += eye(eL, false) + eye(eR, mood === "wink");
    if (mood === "thinking") {
      s += `<path d="M26 51.5L35 50M65 49L74 50.5" stroke="${INK}" stroke-width="1.8" stroke-linecap="round"/>`;
    }
    const mx = 50, my = 73;
    if (mood === "talking") {
      s += `<ellipse cx="${mx}" cy="${my}" rx="4.2" ry="3" fill="${INK}"/><ellipse cx="${mx}" cy="${my + 1.9}" rx="2.4" ry="1" fill="${CHEEK}"/>`;
    } else if (mood === "happy" || mood === "wink" || mood === "point") {
      s += `<path d="M${mx - 6} ${my - 1.5}Q${mx} ${my + 9} ${mx + 6} ${my - 1.5}Z" fill="${INK}"/><ellipse cx="${mx}" cy="${my + 3.3}" rx="3" ry="1.3" fill="${CHEEK}"/>`;
    } else if (mood === "thinking") {
      s += `<path d="M${mx - 3} ${my + 0.5}Q${mx + 1} ${my + 2.5} ${mx + 4} ${my - 0.5}" stroke="${INK}" stroke-width="2.2" fill="none" stroke-linecap="round"/>`;
    } else {
      s += `<path d="M${mx - 5} ${my - 1}Q${mx} ${my + 5} ${mx + 5} ${my - 1}" stroke="${INK}" stroke-width="2.3" fill="none" stroke-linecap="round"/>`;
    }
    return s;
  }

  function folhaSvg({ mood = "idle", size = 200, look = [0.3, -0.2], tilt = 0, hop = 0, shadow = true, sparkles = mood === "thinking", confetti = mood === "happy", armColor = "#3326CE" } = {}) {
    const id = `f${++uid}`;
    const g = (name) => `${id}${name}`;
    const defs = `<defs>
      <linearGradient id="${g("cover")}" x1="0" y1="40" x2="0" y2="92" gradientUnits="userSpaceOnUse"><stop offset="0" stop-color="#4A3FD8"/><stop offset="1" stop-color="#2C22A8"/></linearGradient>
      <linearGradient id="${g("lp")}" x1="12" x2="48.5" y1="0" y2="0" gradientUnits="userSpaceOnUse"><stop offset="0" stop-color="#fff"/><stop offset=".5" stop-color="#F6F5FF"/><stop offset="1" stop-color="#DDDAF9"/></linearGradient>
      <linearGradient id="${g("rp")}" x1="51.5" x2="88" y1="0" y2="0" gradientUnits="userSpaceOnUse"><stop offset="0" stop-color="#DDDAF9"/><stop offset=".5" stop-color="#F6F5FF"/><stop offset="1" stop-color="#fff"/></linearGradient>
      <linearGradient id="${g("fold")}" x1="45" x2="55" y1="0" y2="0" gradientUnits="userSpaceOnUse"><stop offset="0" stop-color="#3326CE" stop-opacity="0"/><stop offset=".5" stop-color="#3326CE" stop-opacity=".25"/><stop offset="1" stop-color="#3326CE" stop-opacity="0"/></linearGradient>
      <linearGradient id="${g("capb")}" x1="0" y1="32" x2="0" y2="46" gradientUnits="userSpaceOnUse"><stop offset="0" stop-color="${CAP}"/><stop offset="1" stop-color="#15104A"/></linearGradient>
      <linearGradient id="${g("capt")}" x1="30" y1="22" x2="70" y2="38" gradientUnits="userSpaceOnUse"><stop offset="0" stop-color="${CAPTOP}"/><stop offset="1" stop-color="${CAP}"/></linearGradient>
      <linearGradient id="${g("rib")}" x1="52" x2="58" y1="0" y2="0" gradientUnits="userSpaceOnUse"><stop offset="0" stop-color="${MINTD}"/><stop offset=".5" stop-color="${MINT}"/><stop offset="1" stop-color="#B4F3D9"/></linearGradient>
      <linearGradient id="${g("tas")}" x1="0" y1="42" x2="0" y2="49" gradientUnits="userSpaceOnUse"><stop offset="0" stop-color="${GOLD}"/><stop offset="1" stop-color="#D89422"/></linearGradient>
      <radialGradient id="${g("sh")}" cx="50" cy="95" r="36" gradientUnits="userSpaceOnUse"><stop offset="0" stop-color="${INK}" stop-opacity=".3"/><stop offset="1" stop-color="${INK}" stop-opacity="0"/></radialGradient>
    </defs>`;
    let stack = "";
    for (let layer = 3; layer >= 1; layer--) {
      const a = 0.6 + 0.13 * (3 - layer);
      stack += `<path d="${leftPage(layer * 1.3, -layer * 0.6)}" fill="${STACK}" opacity="${a}"/><path d="${rightPage(layer * 1.3, layer * 0.6)}" fill="${STACK}" opacity="${a}"/>`;
    }
    const lines = `<g stroke="${LINE}" stroke-width="1.6" stroke-linecap="round">
      <path d="M17 45l20 2M17 49l26 3.1M83 45l-20 2M83 49l-26 3.1M17 79.5l18 -.5M83 79.5l-18 -.5"/></g>`;
    const ribbon = `<path fill="url(#${g("rib")})" d="M53 84L57 84Q57.5 91.8 58.2 97L56 94.6L53.8 97Q53.5 91.8 53 84Z"/>`;
    const cap = `<path fill="url(#${g("capb")})" d="M38 32L62 32L60.5 42.5Q50 46.5 39.5 42.5Z"/>
      <path fill="#120D3F" d="M23 29.5L50 38L77 29.5L77 31.3L50 39.8L23 31.3Z"/>
      <path fill="url(#${g("capt")})" d="M50 21L77 29.5L50 38L23 29.5Z"/>
      <path d="M33 27.5L50 22.5" stroke="#fff" stroke-opacity=".18" stroke-width="1.2" stroke-linecap="round"/>
      <path d="M50 29.5L74 30.5Q74.4 36.5 75 42.5" stroke="${GOLD}" stroke-width="1.3" fill="none" stroke-linecap="round"/>
      <path fill="url(#${g("tas")})" d="M73.6 42.5L76.4 42.5L77.6 49L72.8 49Z"/>
      <circle cx="50" cy="29.5" r="1.8" fill="${GOLD}"/><circle cx="49.5" cy="29" r=".7" fill="#FFE3A0"/>`;
    let behind = "", front = "";
    if (sparkles) {
      behind += star(10, 52, 1.8, MINT) + star(88, 48, 1.6, SPARK);
      front += star(6, 66, 3.2, SPARK) + star(95, 62, 3, MINT) + star(20, 28, 2, SPARK);
    }
    if (confetti) {
      front += star(14, 36, 2.6, SPARK) + star(86, 30, 2.4, MINT) + star(8, 56, 1.8, CHEEK) + star(93, 52, 2, SPARK) + star(24, 18, 1.6, MINT) + star(78, 14, 1.8, CHEEK);
    }
    const body = `<g transform="translate(0 ${hop}) rotate(${tilt} 50 80)">${arms(mood, armColor)}
      <path fill="url(#${g("cover")})" d="M9 43Q30 40 50 46Q70 40 91 43L91 88Q70 85 50 92Q30 85 9 88Z"/>
      ${stack}<path d="${leftPage()}" fill="url(#${g("lp")})"/><path d="${rightPage()}" fill="url(#${g("rp")})"/>
      <rect x="45" y="42" width="10" height="46" fill="url(#${g("fold")})"/>
      ${ribbon}${lines}${face(mood, look)}${cap}</g>`;
    const shadowEl = shadow ? `<ellipse cx="50" cy="95" rx="${36 + Math.min(0, hop) * 1.6}" ry="6.5" fill="url(#${g("sh")})"/>` : "";
    return `<svg xmlns="http://www.w3.org/2000/svg" viewBox="-6 8 112 94" width="${size}" height="${size * 94 / 112}" overflow="visible">${defs}${shadowEl}${behind}${body}${front}</svg>`;
  }

  window.folhaSvg = folhaSvg;
})();
