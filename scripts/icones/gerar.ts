// Gera os ícones do Estudário (a turma do Folha) a partir de design/turma/turma.js:
//   web/src/jsMain/resources/turma.js   (site: desenho em SVG, nomes do Material, animação ao passar o mouse)
//   app/src/main/resources/turma.json      (app: as mesmas camadas, desenhadas por ui/brand/BrandGlyphs.kt)
// deno run --allow-read --allow-write scripts/icones/gerar.ts
import { build, VIEWBOX } from "../../design/turma/turma.js";

const icons = build();

// Nomes do Material Symbols (site) → personagem. Mesma tabela que o app usa em BrandIcon.kt.
const NAMES: Record<string, string[]> = {
  House: ["home"],
  Books: ["library_books"],
  OpenBook: ["menu_book", "auto_stories", "article", "toc", "chrome_reader_mode"],
  Calendar: ["calendar_month", "event", "event_available", "event_repeat", "event_busy", "date_range", "today"],
  Timer: ["timer", "schedule", "hourglass_top", "pending_actions", "history", "alarm"],
  Clipboard: ["quiz", "fact_check", "checklist", "assignment", "rule", "list_alt"],
  Notebook: ["bookmarks"],
  Bookmark: ["bookmark", "bookmark_border", "bookmark_added"],
  Cap: ["school"],
  Pencil: ["auto_awesome", "edit_note", "border_color", "edit"],
  Brain: ["psychology"],
  Check: ["check_circle", "task_alt", "verified"],
  Flag: ["flag", "outlined_flag", "directions_run", "sports_score"],
  Lock: ["lock"],
  Star: ["star", "star_border", "grade"],
  Trophy: ["emoji_events"],
  Medal: ["workspace_premium", "military_tech"],
  Bulb: ["lightbulb", "tips_and_updates"],
  Warning: ["warning", "warning_amber", "report_problem", "priority_high"],
  Oops: ["error", "error_med", "error_outline"],
  Fire: ["local_fire_department", "whatshot"],
  Bolt: ["bolt", "offline_bolt", "speed", "electric_bolt"],
  Heart: ["favorite", "favorite_border"],
  Bell: ["notifications", "notifications_active"],
  Chart: ["query_stats", "insights", "pie_chart", "timeline", "trending_up", "trending_down", "bar_chart", "monitoring", "leaderboard"],
  Cycle: ["autorenew", "replay", "restore", "sync", "refresh", "cached"],
  Target: ["gps_fixed", "center_focus_strong", "explore", "track_changes", "my_location"],
  Folder: ["folder_open", "folder", "inbox", "archive", "unarchive"],
  Gear: ["settings", "tune"],
  Help: ["help_outline", "help", "info", "contact_support"],
  Person: ["person", "account_circle"],
  Shield: ["shield", "verified_user", "security"],
  Cards: ["style"],
  Dice: ["shuffle", "casino"],
  Play: ["play_circle", "play_circle_outline", "ondemand_video"],
  Cloud: ["cloud_sync", "cloud_done", "cloud_download", "cloud_upload", "cloud_off", "cloud"],
  Doc: ["description", "upload_file", "file_open", "picture_as_pdf", "summarize", "article_shortcut"],
  Sun: ["light_mode", "wb_sunny"],
  Moon: ["dark_mode", "bedtime", "nights_stay"],
  Coffee: ["coffee", "local_cafe"],
  Hanger: ["checkroom"],
  Phone: ["devices", "phone_android", "smartphone", "computer"],
  Gem: ["fiber_new", "diamond"],
  Trap: ["pest_control_rodent"],
  // Matérias e áreas de concurso (SubjectArt): nomes próprios, para não trocar ícones já usados.
  Idioma: ["art-idioma"], Pena: ["art-pena"], Calculadora: ["art-calculadora"], Laptop: ["art-laptop"],
  Colunas: ["art-colunas"], Martelo: ["art-martelo"], Moedas: ["art-moedas"], Maleta: ["art-maleta"],
  Pergaminho: ["art-pergaminho"], Balanca: ["art-balanca"], Globo: ["art-globo"], KitMedico: ["art-kitmedico"],
  Frasco: ["art-frasco"], Barco: ["art-barco"], Lupa: ["art-lupa"], Recibo: ["art-recibo"], Urna: ["art-urna"],
  Cofrinho: ["art-cofrinho"],
};
for (const k of Object.keys(icons)) if (!NAMES[k]) throw new Error(`sem nomes do Material: ${k}`);

type Prim = { d: string; f?: string | null; s?: string; w?: number; o?: number; dy?: number };
const svgPrim = (p: Prim) =>
  `<path d="${p.d}"${p.dy ? ` transform="translate(0 ${p.dy})"` : ""} fill="${p.f ?? "none"}"` +
  (p.s ? ` stroke="${p.s}" stroke-width="${+(p.w ?? 5).toFixed(2)}" stroke-linecap="round" stroke-linejoin="round"` : "") +
  (p.o != null ? ` opacity="${p.o}"` : "") + "/>";
const group = (cls: string, prims: Prim[]) => prims.length ? `<g class="${cls}">${prims.map(svgPrim).join("")}</g>` : "";

const svgs: Record<string, { a: string; b: string; f: string; s: string; x: string }> = {};
for (const [name, ic] of Object.entries(icons) as [string, any][]) {
  svgs[name] = {
    a: ic.anim,
    b: group("tu-b", ic.body),
    f: group("tu-f tu-full", ic.face),
    s: group("tu-f tu-small", ic.faceSmall),
    x: group("tu-x", ic.fx),
  };
}

// ------------------------------------------------------------------ site
const css = `
.gl{container-type:inline-size}
.tu{width:100%;height:100%;display:block;overflow:visible}
.tu .tu-small{display:none}
@container (max-width:34px){.tu .tu-full{display:none}.tu .tu-small{display:inline}}
.tu .tu-x{opacity:0}
.tu g{transform-box:view-box;transform-origin:50px 90px}
.tu.muted{filter:grayscale(1) opacity(.55)}
@media (prefers-reduced-motion:no-preference){
:is(a,button,[role=button],[role=tab],.nav-item,.clickable,.tile,.att-tile):hover .tu:not(.muted) .tu-b,
:is(a,button,[role=button],[role=tab],.nav-item,.clickable,.tile,.att-tile):hover .tu:not(.muted) .tu-f{animation:var(--tu-a,tu-hop) .62s cubic-bezier(.3,1.6,.5,1) both}
:is(a,button,[role=button],[role=tab],.nav-item,.clickable,.tile,.att-tile):hover .tu:not(.muted) .tu-f{animation-name:var(--tu-a,tu-hop),tu-blink;animation-duration:.62s,.5s;animation-delay:0s,.35s}
:is(a,button,[role=button],[role=tab],.nav-item,.clickable,.tile,.att-tile):hover .tu:not(.muted) .tu-x{animation:tu-fx .9s ease-out both}
.tu-hover:hover .tu .tu-b,.tu-hover:hover .tu .tu-f{animation:var(--tu-a,tu-hop) .62s cubic-bezier(.3,1.6,.5,1) both}
.tu-hover:hover .tu .tu-x{animation:tu-fx .9s ease-out both}
}
.tu.a-hop{--tu-a:tu-hop}.tu.a-wiggle{--tu-a:tu-wiggle}.tu.a-spin{--tu-a:tu-spin}.tu.a-flicker{--tu-a:tu-flicker}
.tu.a-cheer{--tu-a:tu-cheer}.tu.a-pulse{--tu-a:tu-pulse}.tu.a-zap{--tu-a:tu-zap}.tu.a-sway{--tu-a:tu-sway}
.tu.a-float{--tu-a:tu-float}.tu.a-write{--tu-a:tu-write}.tu.a-ring{--tu-a:tu-ring}.tu.a-roll{--tu-a:tu-roll}
.tu.a-peek{--tu-a:tu-peek}.tu.a-flip{--tu-a:tu-flip}
.tu.a-spin g,.tu.a-pulse g,.tu.a-roll g,.tu.a-flip g{transform-origin:50px 50px}
.tu.a-ring g,.tu.a-sway g{transform-origin:50px 6px}
@keyframes tu-hop{0%,100%{transform:none}35%{transform:translateY(-14px) scale(1.04,.97)}60%{transform:translateY(0) scale(1.06,.92)}80%{transform:translateY(-3px)}}
@keyframes tu-wiggle{0%,100%{transform:none}20%{transform:rotate(-10deg)}40%{transform:rotate(9deg)}60%{transform:rotate(-6deg)}80%{transform:rotate(3deg)}}
@keyframes tu-spin{0%{transform:none}75%{transform:rotate(380deg) scale(1.08)}100%{transform:rotate(360deg)}}
@keyframes tu-flicker{0%,100%{transform:none}20%{transform:scale(.94,1.12)}40%{transform:scale(1.06,.94)}60%{transform:scale(.97,1.08) skewX(-3deg)}80%{transform:scale(1.02,.98) skewX(2deg)}}
@keyframes tu-cheer{0%,100%{transform:none}25%{transform:translateY(-16px) rotate(-8deg)}50%{transform:translateY(-12px) rotate(8deg)}75%{transform:translateY(-4px) rotate(-3deg)}}
@keyframes tu-pulse{0%,100%{transform:none}25%{transform:scale(1.16)}45%{transform:scale(.96)}65%{transform:scale(1.1)}}
@keyframes tu-zap{0%,100%{transform:none}15%{transform:translate(-4px,2px) rotate(-6deg)}30%{transform:translate(4px,-3px) rotate(6deg) scale(1.08)}45%{transform:translate(-3px,1px)}60%{transform:translate(2px,-1px)}}
@keyframes tu-sway{0%,100%{transform:none}30%{transform:rotate(-9deg)}65%{transform:rotate(7deg)}}
@keyframes tu-float{0%,100%{transform:none}50%{transform:translateY(-9px)}}
@keyframes tu-write{0%,100%{transform:none}25%{transform:translate(-5px,4px) rotate(-6deg)}50%{transform:translate(4px,-2px)}75%{transform:translate(-3px,3px) rotate(-3deg)}}
@keyframes tu-ring{0%,100%{transform:none}15%{transform:rotate(16deg)}30%{transform:rotate(-14deg)}45%{transform:rotate(10deg)}60%{transform:rotate(-6deg)}75%{transform:rotate(3deg)}}
@keyframes tu-roll{0%{transform:none}60%{transform:rotate(380deg) translateY(-6px)}100%{transform:rotate(360deg)}}
@keyframes tu-peek{0%,100%{transform:none}40%{transform:translateY(-10px) rotate(-4deg)}70%{transform:translateY(-2px) rotate(2deg)}}
@keyframes tu-flip{0%{transform:none}45%{transform:rotate(180deg) scale(.92)}80%{transform:rotate(370deg)}100%{transform:rotate(360deg)}}
@keyframes tu-blink{0%,100%{transform:none}50%{transform:scaleY(.15)}}
@keyframes tu-fx{0%{opacity:0;transform:translateY(8px) scale(.6)}35%{opacity:1;transform:translateY(-4px) scale(1.1)}100%{opacity:0;transform:translateY(-14px) scale(1)}}
`.trim();

const web = `// GERADO por scripts/icones/gerar.ts a partir de design/turma/turma.js. Não edite à mão.
// A turma do Folha: os ícones do Estudário no site. estudarioGlyph(nomeDoMaterial, apagado) devolve o
// <svg> (ou null, e o site usa o símbolo comum: setas, fechar, mais opções continuam traços simples).
// Ao passar o mouse no botão/item que contém o ícone, o personagem se mexe (só CSS, sem script).
(function () {
  const VB = "${VIEWBOX.join(" ")}";
  const ICONS = ${JSON.stringify(svgs)};
  const NAMES = ${JSON.stringify(NAMES)};
  const MAP = {};
  for (const [g, list] of Object.entries(NAMES)) for (const n of list) MAP[n] = g;
  const style = document.createElement("style");
  style.textContent = ${JSON.stringify(css)};
  (document.head || document.documentElement).appendChild(style);
  const resolve = (name) => MAP[name] || (ICONS[name] ? name : null);
  window.estudarioGlyphName = resolve;
  window.estudarioGlyph = function (name, muted) {
    const g = resolve(name);
    if (!g) return null;
    const ic = ICONS[g];
    return '<svg class="tu a-' + ic.a + (muted ? " muted" : "") + '" viewBox="' + VB + '" xmlns="http://www.w3.org/2000/svg" aria-hidden="true">' + ic.b + ic.f + ic.s + ic.x + "</svg>";
  };
  /** Mesmo desenho como data URI, para <img> (parado, rosto completo). */
  const uris = new Map();
  window.estudarioGlyphUri = function (name, muted) {
    const key = name + (muted ? ":m" : "");
    if (uris.has(key)) return uris.get(key);
    const g = resolve(name);
    let uri = null;
    if (g) {
      const ic = ICONS[g];
      const svg = '<svg viewBox="' + VB + '" xmlns="http://www.w3.org/2000/svg"' + (muted ? ' style="filter:grayscale(1) opacity(.55)"' : "") + ">" + ic.b + ic.f + "</svg>";
      uri = "data:image/svg+xml;charset=utf-8," + encodeURIComponent(svg);
    }
    uris.set(key, uri);
    return uri;
  };
  window.estudarioTurma = Object.keys(ICONS);
})();
`;
Deno.writeTextFileSync("web/src/jsMain/resources/turma.js", web);

// ------------------------------------------------------------------ app
const round = (p: Prim) => ({ ...p, w: p.w == null ? undefined : +p.w.toFixed(2) });
const app = {
  viewBox: VIEWBOX,
  icons: Object.fromEntries(Object.entries(icons).map(([k, v]: [string, any]) =>
    [k, { anim: v.anim, body: v.body.map(round), face: v.face.map(round), faceSmall: v.faceSmall.map(round) }])),
};
Deno.mkdirSync("app/src/main/resources", { recursive: true });
Deno.writeTextFileSync("app/src/main/resources/turma.json", JSON.stringify(app));
console.log(`${Object.keys(icons).length} personagens → web/src/jsMain/resources/turma.js e app/src/main/resources/turma.json`);
