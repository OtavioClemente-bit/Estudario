// Gera pedidos/lote-N.txt: bloco de matérias de cada lote + as instruções comuns do PEDIDO_CHAT_modelo.txt.
// Rodar a partir de conteudo/: deno run --allow-read --allow-write pedidos/_gerar.ts
const modelo = Deno.readTextFileSync("PEDIDO_CHAT_modelo.txt");
const instr = modelo.slice(modelo.indexOf("══════", modelo.indexOf("MATÉRIA 2")))
  .replace("Você é professor de Língua Portuguesa e elaborador de questões da banca IDECAN.", "Você é professor da disciplina indicada em cada matéria e elaborador de questões da banca IDECAN.")
  .replace('"subject": "Língua Portuguesa",', '"subject": "(a disciplina indicada na matéria)",')
  .replace("2. As cinco alternativas são frases completas e naturais, do dia a dia de bombeiros e do serviço público,", "2. Nas matérias de Língua Portuguesa, as cinco alternativas são frases completas e naturais, do dia a dia de bombeiros e do serviço público; nas de Raciocínio Lógico, siga as regras próprias da matéria. Em ambos os casos,");

const head = "══════════════════════════════════════════════════════════════\nMATÉRIAS DESTE PEDIDO\n══════════════════════════════════════════════════════════════\nConcurso: Soldado do Corpo de Bombeiros Militar de Minas Gerais (CBMMG), nível médio. Banca: IDECAN.\n\n";

const RL = "- REGRAS DE RACIOCÍNIO LÓGICO: problemas com enunciado completo e dados suficientes (contexto de bombeiros, escalas, plantões, equipes, viaturas). Alternativas numéricas ou conclusões lógicas; os distratores vêm de erros típicos de cálculo ou de raciocínio (esquecer um caso, inverter a condicional, somar em vez de multiplicar). Resolva cada questão por completo antes de definir o gabarito e mostre a resolução passo a passo na explicação (pode passar de 600 caracteres, até 1.200). Teoria com fórmulas, tabelas-verdade e exemplos resolvidos. Certo/Errado em estilo de afirmação sobre um problema dado.";

const lotes: Record<string, string> = {
  "2": `MATÉRIA 1
- Arquivos: verbos.json e recorte-verbos.json
- id: "portugues.verbos" | title: "Emprego e correlação de tempos e modos verbais" | subject: "Língua Portuguesa"
- aliases: ["Emprego/correlação de tempos e modos verbais."]
- Casos obrigatórios: valores dos tempos do indicativo (presente, pretéritos perfeito, imperfeito e mais-que-perfeito, futuros do presente e do pretérito); subjuntivo e imperativo; correlação verbal (se + imperfeito do subjuntivo → futuro do pretérito; quando/se + futuro do subjuntivo → futuro do presente; pretérito perfeito × mais-que-perfeito); formas nominais (infinitivo pessoal × impessoal, gerúndio, particípio regular e irregular: aceitado/aceito, entregado/entregue, com ter/haver × ser/estar); verbos irregulares cobrados (ver, vir, pôr, ter, intervir, manter, deter, propor, requerer, prover, reaver, precaver-se) e futuro do subjuntivo (se eu vir × se eu ver; quando ele vier; se ele mantiver); voz ativa, passiva analítica e sintética e a transposição entre elas com a correlação de tempo correta; locuções verbais.

MATÉRIA 2
- Arquivos: reescrita.json e recorte-reescrita.json
- id: "portugues.reescrita" | title: "Reescrita de frases e parágrafos" | subject: "Língua Portuguesa"
- aliases: ["Reescritura de frases e parágrafos do texto: substituição de palavras ou de trechos de texto;"]
- Casos obrigatórios: substituição de palavras por sinônimos no contexto (preservando sentido e correção); troca de conectores com mesmo valor e ajuste de modo verbal; mudança de voz verbal; mudança de ordem dos termos e efeito na pontuação; substituição de oração por termo equivalente (oração adjetiva → adjetivo; oração adverbial → locução); transposição de discurso direto para indireto (pessoa, tempo, pronomes, advérbios); nominalização; reescrita que preserva o sentido × que altera o sentido × que cria erro gramatical; paralelismo sintático. Quase todas as questões de múltipla escolha apresentam uma frase ou trecho original e pedem a reescrita correta ou incorreta.`,

  "3": `MATÉRIA 1
- Arquivos: coordenacao.json e recorte-coordenacao.json
- id: "portugues.coordenacao" | title: "Coordenação entre orações e entre termos" | subject: "Língua Portuguesa"
- aliases: ["Domínio da estrutura morfossintática do período: relações de coordenação entre orações e entre termos da oração;"]
- Casos obrigatórios: período simples × composto; orações coordenadas assindéticas e sindéticas (aditivas, adversativas, alternativas, conclusivas, explicativas); valor semântico das conjunções e sua troca; "e" com valor adversativo; "pois" explicativo (antes do verbo) × conclusivo (deslocado, entre vírgulas); pontuação das coordenadas; coordenação entre termos (sujeito composto, complementos coordenados, paralelismo); explicativa coordenada × causal subordinada.

MATÉRIA 2
- Arquivos: subordinacao.json e recorte-subordinacao.json
- id: "portugues.subordinacao" | title: "Subordinação entre orações e entre termos" | subject: "Língua Portuguesa"
- aliases: ["relações de subordinação entre orações e entre termos da oração;"]
- Casos obrigatórios: termos essenciais, integrantes e acessórios (sujeito, predicado, objetos, complemento nominal, agente da passiva, adjuntos, aposto, vocativo); orações subordinadas substantivas (subjetiva, objetiva direta e indireta, completiva nominal, predicativa, apositiva); adjetivas restritivas × explicativas; adverbiais (causal, consecutiva, concessiva, condicional, conformativa, comparativa, final, proporcional, temporal); orações reduzidas de infinitivo, gerúndio e particípio e o seu desenvolvimento; "que" conjunção × pronome relativo; "se" conjunção integrante × condicional.`,

  "4": `MATÉRIA 1
- Arquivos: pronomes.json e recorte-pronomes.json
- id: "portugues.pronomes" | title: "Emprego e colocação de pronomes" | subject: "Língua Portuguesa"
- aliases: ["Emprego e colocação de pronomes", "Emprego de pronomes", "Emprego dos pronomes", "Emprego e colocação dos pronomes", "Pronomes: emprego e colocação"]
- Casos obrigatórios: pessoais retos × oblíquos (para eu fazer × para mim; entre mim e ti; com nós × conosco; si e consigo reflexivos); pronomes de tratamento (concordância em 3ª pessoa: Vossa Excelência está, seu; Vossa × Sua); demonstrativos este/esse/aquele no espaço, no tempo e no texto (anáfora × catáfora); relativos (cujo sem artigo depois e concordando com o possuído; onde só para lugar; o qual/que com a preposição exigida: de que, a que, em que); possessivos e a ambiguidade de "seu"; indefinidos (todo × todo o); um capítulo de síntese da colocação pronominal (próclise, ênclise, mesóclise, locuções), sem a profundidade de uma matéria só de colocação.

MATÉRIA 2
- Arquivos: retextualizacao.json e recorte-retextualizacao.json
- id: "portugues.retextualizacao" | title: "Retextualização de gêneros e níveis de formalidade" | subject: "Língua Portuguesa"
- aliases: ["retextualização de diferentes gêneros e níveis de formalidade."]
- Casos obrigatórios: níveis de formalidade (formal, semiformal, informal) e marcas linguísticas de cada um; adequação ao contexto, ao interlocutor e ao gênero; passagem do informal para o formal (gírias, marcas de oralidade, "a gente", "tá", "pra", colocação pronominal); retextualização entre gêneros (mensagem → ofício, memorando ou e-mail institucional; notícia → resumo; relato oral → relatório de ocorrência); redação oficial: impessoalidade, clareza, concisão, pronomes de tratamento e fechos; preservação do conteúdo ao mudar o gênero. Pelo menos 15 questões de múltipla escolha com um texto curto original para reescrever ou avaliar.`,

  "5": `MATÉRIA 1
- Arquivos: logica-relacoes.json e recorte-logica-relacoes.json
- id: "raciocinio-logico.relacoes-arbitrarias" | title: "Estrutura lógica de relações e dedução" | subject: "Raciocínio Lógico e Matemático"
- aliases: ["Estrutura lógica de relações arbitrárias entre pessoas, lugares, objetos ou eventos fictícios;", "dedução de novas informações das relações fornecidas e avaliação das condições usadas para estabelecer a estrutura daquelas relações."]
- Casos obrigatórios: problemas de associação com tabela (quem é quem: pessoas × funções × lugares); ordenação e posição (fila, mesa circular, andares); verdades e mentiras; proposições simples e compostas, conectivos (e, ou, ou...ou, se...então, se e somente se) e tabelas-verdade; negação de proposições (De Morgan, negação da condicional); equivalências (contrapositiva); quantificadores (todo, algum, nenhum) e diagramas; argumentos válidos e inválidos; dedução a partir de premissas.
${RL}

MATÉRIA 2
- Arquivos: conjuntos.json e recorte-conjuntos.json
- id: "raciocinio-logico.conjuntos" | title: "Operações com conjuntos" | subject: "Raciocínio Lógico e Matemático"
- aliases: ["Operações com conjuntos.", "Teoria dos conjuntos", "Conjuntos e operações"]
- Casos obrigatórios: notação, pertinência e inclusão; conjunto vazio, unitário e universo; subconjuntos e número de subconjuntos (2^n); união, interseção, diferença e complementar; propriedades; diagramas de Venn com 2 e 3 conjuntos; contagem com Venn (n(A∪B) = n(A) + n(B) − n(A∩B) e a fórmula para 3 conjuntos); conjuntos numéricos (N, Z, Q, I, R) e intervalos; problemas contextualizados (pesquisas, cursos, plantões).
${RL}`,

  "6": `MATÉRIA 1
- Arquivos: raciocinio-situacoes.json e recorte-raciocinio-situacoes.json
- id: "raciocinio-logico.analise-situacoes" | title: "Compreensão e análise lógica de situações" | subject: "Raciocínio Lógico e Matemático"
- aliases: ["Compreensão e análise da lógica de uma situação, utilizando as funções intelectuais: raciocínio verbal, raciocínio matemático, raciocínio sequencial, orientação espacial e temporal, formação de conceitos, discriminação de elementos."]
- Casos obrigatórios: sequências numéricas, de letras e de figuras descritas em texto (lei de formação); raciocínio verbal (analogias, classificação de palavras); orientação temporal (calendário, dias da semana, relógios, intervalos de tempo); orientação espacial (direções, rotações, vistas e planificações descritas em texto); formação de conceitos e discriminação de elementos (o que não pertence ao grupo); problemas de lógica do cotidiano.
${RL}

MATÉRIA 2
- Arquivos: raciocinio-aritmetico.json e recorte-raciocinio-aritmetico.json
- id: "raciocinio-logico.problemas-aritmeticos" | title: "Problemas aritméticos, geométricos e matriciais" | subject: "Raciocínio Lógico e Matemático"
- aliases: ["Raciocínio lógico envolvendo problemas aritméticos, geométricos e matriciais."]
- Casos obrigatórios: quatro operações e expressões; MMC e MDC em problemas (escalas e plantões que coincidem); frações e porcentagem (aumentos e descontos sucessivos); razão, proporção, regra de três simples e composta; média aritmética e ponderada; princípio fundamental da contagem; geometria plana (perímetro e área de retângulo, triângulo e círculo; Pitágoras); volume de prismas e cilindros (caixas-d'água, tanques); matrizes: leitura, soma, produto e problemas com tabelas.
${RL}`,
};

for (const [n, bloco] of Object.entries(lotes)) {
  Deno.writeTextFileSync(`pedidos/lote-${n}.txt`, head + bloco + "\n\n\n" + instr);
}
Deno.writeTextFileSync("pedidos/lote-1.txt", modelo);
console.log([...Deno.readDirSync("pedidos")].map((f) => f.name).sort().join(" "));
