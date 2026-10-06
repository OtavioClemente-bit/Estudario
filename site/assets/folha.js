// O Folha em SVG, traduzido de app/.../ui/assistant/Folha.kt (mesma geometria, viewBox 0 0 100 100),
// para as artes da loja. folhaSvg({ mood, size, look, tilt, hop, shadow }) devolve o <svg>.
(function () {
  const INK = "#26215C", LINE = "#D8D4E6", CAP = "#231C6B", CAPTOP = "#2F2789";
  const GOLD = "#F5B83D", MINT = "#7EE0B8", MINTD = "#3FBF8F", CHEEK = "#F4A6C0", SPARK = "#FFE07A";
  let uid = 0;

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
      <linearGradient id="${g("cover")}" x1="0" y1="41" x2="0" y2="93" gradientUnits="userSpaceOnUse"><stop offset="0" stop-color="#5247E0"/><stop offset=".5" stop-color="#3A2FC4"/><stop offset="1" stop-color="#2A209E"/></linearGradient>
      <linearGradient id="${g("gloss")}" x1="8" y1="44" x2="40" y2="70" gradientUnits="userSpaceOnUse"><stop offset="0" stop-color="#fff" stop-opacity=".14"/><stop offset="1" stop-color="#fff" stop-opacity="0"/></linearGradient>
      <linearGradient id="${g("edge")}" x1="0" y1="80" x2="0" y2="91" gradientUnits="userSpaceOnUse"><stop offset="0" stop-color="#E9E4DA"/><stop offset="1" stop-color="#CFC8BC"/></linearGradient>
      <linearGradient id="${g("lp")}" x1="12" x2="50" y1="0" y2="0" gradientUnits="userSpaceOnUse"><stop offset="0" stop-color="#F7F4EE"/><stop offset=".33" stop-color="#FFFDF8"/><stop offset=".67" stop-color="#FFFDF8"/><stop offset="1" stop-color="#F7F4EE"/></linearGradient>
      <linearGradient id="${g("rp")}" x1="50" x2="88" y1="0" y2="0" gradientUnits="userSpaceOnUse"><stop offset="0" stop-color="#F7F4EE"/><stop offset=".33" stop-color="#FFFDF8"/><stop offset=".67" stop-color="#FFFDF8"/><stop offset="1" stop-color="#F1EDE6"/></linearGradient>
      <linearGradient id="${g("gl")}" x1="39" x2="50" y1="0" y2="0" gradientUnits="userSpaceOnUse"><stop offset="0" stop-color="#3A3170" stop-opacity="0"/><stop offset=".55" stop-color="#3A3170" stop-opacity=".035"/><stop offset=".85" stop-color="#3A3170" stop-opacity=".1"/><stop offset="1" stop-color="#3A3170" stop-opacity=".2"/></linearGradient>
      <linearGradient id="${g("gr")}" x1="61" x2="50" y1="0" y2="0" gradientUnits="userSpaceOnUse"><stop offset="0" stop-color="#3A3170" stop-opacity="0"/><stop offset=".55" stop-color="#3A3170" stop-opacity=".035"/><stop offset=".85" stop-color="#3A3170" stop-opacity=".1"/><stop offset="1" stop-color="#3A3170" stop-opacity=".2"/></linearGradient>
      <linearGradient id="${g("ol")}" x1="12" x2="17" y1="0" y2="0" gradientUnits="userSpaceOnUse"><stop offset="0" stop-color="#3A3170" stop-opacity=".06"/><stop offset="1" stop-color="#3A3170" stop-opacity="0"/></linearGradient>
      <linearGradient id="${g("or")}" x1="83" x2="88" y1="0" y2="0" gradientUnits="userSpaceOnUse"><stop offset="0" stop-color="#3A3170" stop-opacity="0"/><stop offset="1" stop-color="#3A3170" stop-opacity=".08"/></linearGradient>
      <linearGradient id="${g("ribin")}" x1="0" y1="88" x2="0" y2="91.5" gradientUnits="userSpaceOnUse"><stop offset="0" stop-color="#0E0A45" stop-opacity=".45"/><stop offset="1" stop-color="#0E0A45" stop-opacity="0"/></linearGradient>
      <linearGradient id="${g("ribfold")}" x1="0" y1="94.6" x2="0" y2="96.4" gradientUnits="userSpaceOnUse"><stop offset="0" stop-color="#1F7A5A" stop-opacity=".55"/><stop offset="1" stop-color="#1F7A5A" stop-opacity="0"/></linearGradient>
      <radialGradient id="${g("sh2")}" cx="50" cy="95" r="26" gradientUnits="userSpaceOnUse" gradientTransform="translate(0 78) scale(1 .18)"><stop offset="0" stop-color="${INK}" stop-opacity=".3"/><stop offset="1" stop-color="${INK}" stop-opacity="0"/></radialGradient>
      <linearGradient id="${g("capb")}" x1="0" y1="32" x2="0" y2="46" gradientUnits="userSpaceOnUse"><stop offset="0" stop-color="${CAP}"/><stop offset="1" stop-color="#15104A"/></linearGradient>
      <linearGradient id="${g("capt")}" x1="30" y1="22" x2="70" y2="38" gradientUnits="userSpaceOnUse"><stop offset="0" stop-color="${CAPTOP}"/><stop offset="1" stop-color="${CAP}"/></linearGradient>
      <linearGradient id="${g("rib")}" x1="51.4" x2="55.4" y1="0" y2="0" gradientUnits="userSpaceOnUse"><stop offset="0" stop-color="${MINTD}"/><stop offset=".33" stop-color="${MINT}"/><stop offset=".67" stop-color="#C6F7E2"/><stop offset="1" stop-color="${MINT}"/></linearGradient>
      <linearGradient id="${g("tas")}" x1="0" y1="42" x2="0" y2="49" gradientUnits="userSpaceOnUse"><stop offset="0" stop-color="${GOLD}"/><stop offset="1" stop-color="#D89422"/></linearGradient>
      <radialGradient id="${g("sh")}" cx="50" cy="95" r="36" gradientUnits="userSpaceOnUse"><stop offset="0" stop-color="${INK}" stop-opacity=".3"/><stop offset="1" stop-color="${INK}" stop-opacity="0"/></radialGradient>
    </defs>`;
    // Livro "de verdade": mesma construção do Folha.kt (drawBook / drawRibbon).
    const X = (s, v) => 50 + s * v;
    const page = (s) => `M50 44C${X(s, 10)} 37.6 ${X(s, 26)} 36.4 ${X(s, 38)} 40.4L${X(s, 38)} 84.4C${X(s, 26)} 80.6 ${X(s, 10)} 81.4 50 87Z`;
    const block = (s, d = 3.6) => `M${X(s, 38)} 40.4L${X(s, 38)} 84.4C${X(s, 26)} 80.6 ${X(s, 10)} 81.4 50 87L50 ${87 + d}C${X(s, 10)} ${81.4 + d} ${X(s, 26.5)} ${80.6 + d} ${X(s, 38 + d * 0.45)} ${84.4 + d}L${X(s, 38 + d * 0.45)} ${41.6 + d * 0.3}Z`;
    const coverD = "M8.5 43.5C22 40.5 38 41.5 50 47.5C62 41.5 78 40.5 91.5 43.5L91.5 89C78 86.4 62 87.4 50 93C38 87.4 22 86.4 8.5 89Z";
    let book = `<path d="${coverD}" fill="#1A1370" transform="translate(0 1.7)"/><path d="${coverD}" fill="url(#${g("cover")})"/><path d="${coverD}" fill="url(#${g("gloss")})"/>`;
    const sad = mood === "sad", rx = 51.4, rw = 3.4, edge = 94.6, hang = sad ? 3.2 : 5.4, tipX = rx + 0.5, tipY = edge + hang;
    const ribD = `M${rx} 86L${rx + rw} 86L${rx + rw + 0.3} ${edge}Q${rx + rw + 0.3} ${edge + hang / 2} ${tipX + rw} ${tipY}L${tipX + rw / 2} ${tipY - 1.6}L${tipX} ${tipY}Q${rx + 0.3} ${edge + hang / 2} ${rx + 0.3} ${edge}Z`;
    book += `<path d="${ribD}" fill="#0E0A45" opacity=".35" transform="translate(.7 .5)"/><path d="${ribD}" fill="url(#${g("rib")})"/>` +
      `<rect x="${rx}" y="88" width="${rw + 0.3}" height="3.5" fill="url(#${g("ribin")})"/>` +
      `<path d="M${rx + 0.5} ${edge - 0.4}H${rx + rw}" stroke="#fff" stroke-opacity=".55" stroke-width=".5"/>` +
      `<rect x="${rx + 0.3}" y="${edge}" width="${rw}" height="1.8" fill="url(#${g("ribfold")})"/>`;
    for (const s of [-1, 1]) {
      book += `<path d="${block(s)}" fill="url(#${g("edge")})"/>`;
      for (let i = 1; i <= 4; i++) {
        const d = i * 0.72;
        book += `<path d="M${X(s, 38 + d * 0.45)} ${84.4 + d}C${X(s, 26.2)} ${80.6 + d} ${X(s, 10)} ${81.4 + d} 50 ${87 + d}" fill="none" stroke="#8C8270" stroke-opacity=".22" stroke-width=".28"/>`;
      }
      for (let i = 1; i <= 3; i++) {
        const x = X(s, 38 + i * 0.42);
        book += `<path d="M${x} ${41 + i * 0.3}L${x} ${84.4 + i * 0.9}" stroke="#8C8270" stroke-opacity=".18" stroke-width=".25"/>`;
      }
    }
    book += `<path d="${page(-1)}" fill="url(#${g("lp")})"/><path d="${page(1)}" fill="url(#${g("rp")})"/>` +
      `<path d="${page(-1)}" fill="url(#${g("gl")})"/><path d="${page(1)}" fill="url(#${g("gr")})"/>` +
      `<path d="${page(-1)}" fill="url(#${g("ol")})"/><path d="${page(1)}" fill="url(#${g("or")})"/>` +
      `<path d="M50 44.3L50 86.8" stroke="#3A3170" stroke-opacity=".16" stroke-width=".35"/>` +
      [-1, 1].map((s) => `<path d="M${X(s, 3)} 41.9C${X(s, 12)} 37.9 ${X(s, 26)} 36.9 ${X(s, 36.5)} 40.2" fill="none" stroke="#fff" stroke-opacity=".9" stroke-width=".5" stroke-linecap="round"/>`).join("");
    const lines = `<g stroke="${LINE}" stroke-width="1.6" stroke-linecap="round">
      <path d="M17 45l20 2M17 49l26 3.1M83 45l-20 2M83 49l-26 3.1M17 79.5l18 -.5M83 79.5l-18 -.5"/></g>`;
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
      ${book}${lines}${face(mood, look)}${cap}</g>`;
    const shadowEl = shadow ? `<ellipse cx="50" cy="95" rx="${36 + Math.min(0, hop) * 1.6}" ry="6.5" fill="url(#${g("sh")})"/><ellipse cx="50" cy="95" rx="26" ry="4.2" fill="url(#${g("sh2")})"/>` : "";
    return `<svg xmlns="http://www.w3.org/2000/svg" viewBox="-6 8 112 94" width="${size}" height="${size * 94 / 112}" overflow="visible">${defs}${shadowEl}${behind}${body}${front}</svg>`;
  }

  window.folhaSvg = folhaSvg;
})();
