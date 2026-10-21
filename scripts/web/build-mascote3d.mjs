// Gera web/src/jsMain/resources/mascote3d.js (o Folha 3D com o three.js embutido; o CSP do app só
// aceita scripts do próprio site). Rodar depois de mexer em web/mascote3d/mascote3d.src.js:
//   node scripts/web/build-mascote3d.mjs
import { build } from "esbuild";
import { fileURLToPath } from "node:url";
import { dirname, join } from "node:path";

const root = join(dirname(fileURLToPath(import.meta.url)), "..", "..");
await build({
  entryPoints: [join(root, "web/mascote3d/mascote3d.src.js")],
  outfile: join(root, "web/src/jsMain/resources/mascote3d.js"),
  bundle: true, minify: true, format: "iife", target: "es2020", legalComments: "none",
  banner: { js: "/* Folha 3D do Estudário. Gerado de web/mascote3d/mascote3d.src.js; inclui three.js (MIT, three.js authors). */" },
});
console.log("mascote3d.js gerado");
