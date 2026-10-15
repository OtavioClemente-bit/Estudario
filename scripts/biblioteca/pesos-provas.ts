// Grava nos editais (conteudo/editais) o peso real da prova, a partir de conteudo/pesos-provas.json.
//
// Cada registro do arquivo foi lido à mão no quadro de provas do PDF do edital:
//   { "fonte": "url do PDF", "editais": ["arquivo-sem-json", ...],
//     "materias": { "Nome da disciplina no edital": 12, ... },        // questões exatas, ou
//                 { "Nome": { "questoes": 20, "nota": "frase explicando a conta" } } quando é aproximado
//     "peso": { "Nome": 2 },                                           // peso por questão, quando não é 1
//     "blocos": [{ "nome": "Conhecimentos específicos", "questoes": 30, "peso": 2.5, "materias": ["Nome", ...] | "resto" }] }
// "resto" = todas as disciplinas do edital que não estão em "materias" nem em outro bloco. O bloco é
// dividido igualmente entre as matérias dele, e a frase da divisão vai junto (o app mostra assim).
// Edital em que alguma disciplina fica sem número não recebe nada (peso pela metade enganaria).
//
// Uso: deno run --allow-read --allow-write scripts/biblioteca/pesos-provas.ts [--conferir]

type Json = Record<string, unknown>;
interface Block { nome: string; questoes: number; peso?: number; materias: string[] | "resto" }
interface Entry { fonte: string; editais: string[]; materias?: Record<string, number | { questoes: number; nota: string }>; peso?: Record<string, number>; blocos?: Block[] }

const onlyCheck = Deno.args.includes("--conferir");
const norm = (value: string) => value.normalize("NFD").replace(/\p{M}+/gu, "").toLowerCase().replace(/[^a-z0-9]+/g, " ").trim();
const entries = JSON.parse(Deno.readTextFileSync("conteudo/pesos-provas.json")) as Entry[];
const problems: string[] = [];
let applied = 0;
for (const entry of entries) {
  for (const id of entry.editais) {
    const path = `conteudo/editais/${id}.json`;
    let raw: string;
    try {
      raw = Deno.readTextFileSync(path);
    } catch {
      problems.push(`${id}: edital não existe`);
      continue;
    }
    const edital = JSON.parse(raw) as Json;
    const subjects = edital.disciplinas as Json[];
    const exact = new Map(Object.entries(entry.materias ?? {}).map(([name, count]) => [norm(name), typeof count === "number" ? { questoes: count } : { questoes: count.questoes, notaBloco: count.nota }]));
    const weights = new Map(Object.entries(entry.peso ?? {}).map(([name, value]) => [norm(name), value]));
    const blocks = entry.blocos ?? [];
    const named = new Set([...exact.keys(), ...blocks.flatMap((block) => block.materias === "resto" ? [] : block.materias.map(norm))]);
    const values = new Map<Json, { questoes: number; peso?: number; notaBloco?: string }>();
    for (const subject of subjects) {
      const key = norm(String(subject.nome));
      if (exact.has(key)) values.set(subject, { ...exact.get(key)!, ...(weights.has(key) ? { peso: weights.get(key) } : {}) });
    }
    for (const block of blocks) {
      const members = subjects.filter((subject) => {
        const key = norm(String(subject.nome));
        return block.materias === "resto" ? !named.has(key) && !values.has(subject) : block.materias.map(norm).includes(key);
      });
      if (members.length === 0) continue;
      const each = Math.max(1, Math.round(block.questoes / members.length));
      const note = members.length === 1
        ? `${block.nome}: ${block.questoes} questões na prova`
        : `${block.nome}: ${block.questoes} questões para ${members.length} matérias (cerca de ${each} cada)`;
      for (const subject of members) {
        values.set(subject, members.length === 1
          ? { questoes: block.questoes, ...(block.peso && block.peso !== 1 ? { peso: block.peso } : {}) }
          : { questoes: each, ...(block.peso && block.peso !== 1 ? { peso: block.peso } : {}), notaBloco: note });
      }
    }
    const missing = subjects.filter((subject) => !values.has(subject)).map((subject) => String(subject.nome));
    if (missing.length > 0) {
      problems.push(`${id}: sem número para ${missing.join(", ")}`);
      continue;
    }
    for (const subject of subjects) {
      const value = values.get(subject)!;
      delete subject.questoes;
      delete subject.peso;
      delete subject.notaBloco;
      Object.assign(subject, value);
    }
    applied++;
    if (!onlyCheck) Deno.writeTextFileSync(path, JSON.stringify(edital, null, 2) + (raw.endsWith("\n") ? "\n" : ""));
  }
}
console.log(`${onlyCheck ? "Conferido" : "Gravado"}: ${applied} edital(is).`);
if (problems.length > 0) console.log(problems.map((p) => `  - ${p}`).join("\n"));
