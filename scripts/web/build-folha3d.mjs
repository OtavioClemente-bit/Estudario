// Gera web/src/jsMain/resources/folha3d.js (o Folha 3D com o three.js embutido; o CSP do app
// só aceita scripts do próprio site). Rodar depois de mexer em web/folha3d/folha3d.src.js:
//   node scripts/web/build-folha3d.mjs
import { build } from "esbuild";
import { fileURLToPath } from "node:url";
import { dirname, join } from "node:path";

const root = join(dirname(fileURLToPath(import.meta.url)), "..", "..");
await build({
  entryPoints: [join(root, "web/folha3d/folha3d.src.js")],
  outfile: join(root, "web/src/jsMain/resources/folha3d.js"),
  bundle: true,
  minify: true,
  format: "iife",
  target: "es2020",
  legalComments: "none",
  banner: { js: "/* Folha 3D do Estudário. Gerado de web/folha3d/folha3d.src.js; inclui three.js (MIT, three.js authors). */" },
});
console.log("folha3d.js gerado");
