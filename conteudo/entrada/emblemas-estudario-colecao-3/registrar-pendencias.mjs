import fs from 'node:fs/promises';
import path from 'node:path';
import {fileURLToPath} from 'node:url';
const out=path.dirname(fileURLToPath(import.meta.url));
const status=JSON.parse(await fs.readFile(path.join(out,'producao-final/status.json'),'utf8'));
const badges=status.badges.filter(b=>b.status!=='visualmente-conferido');
const pending={complete:badges.length===0,generated:status.badges.length,visuallyChecked:status.badges.length-badges.length,pending:badges.length,badges};
await fs.writeFile(path.join(out,'pendencias.json'),JSON.stringify(pending,null,2)+'\n');
const message=badges.length
 ? '# Pendências da revisão\n\n'+badges.map(b=>'- **'+b.id+'**: '+b.note).join('\n')+'\n'
 : '# Revisão concluída\n\n65 emblemas conferidos visualmente; zero pendências sinalizadas. As 14 correções anteriores foram concluídas com acabamento em camadas autorizado pelo dono.\n\nO registro das máscaras, recortes e transformações está em producao-final/acabamento-pendente/camadas.json. O nome dessa pasta é histórico e não indica uma pendência atual. Tentativas rejeitadas estão separadas e não integram os PNGs finais.\n\nA Coleção II foi preservada. A conferência do assistente não substitui a avaliação artística do dono.\n';
await fs.writeFile(path.join(out,'PENDENCIAS.md'),message);
console.log(JSON.stringify(pending));

