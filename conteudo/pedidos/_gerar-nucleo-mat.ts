// Núcleo comum de Matemática e Raciocínio Lógico (serve a PMMG, BB, PF, ESA/EsPCEx, tribunais).
// Matérias gerais, sem banca: não há recorte neste pedido (o recorte da banca é feito pelo app).
// Rodar a partir de conteudo/: deno run --allow-read --allow-write pedidos/_gerar-nucleo-mat.ts
const modelo = Deno.readTextFileSync("PEDIDO_CHAT_modelo.txt");
let instr = modelo.slice(modelo.indexOf("══════", modelo.indexOf("MATÉRIA 2")))
  .replace("Você é professor de Língua Portuguesa e elaborador de questões da banca IDECAN.", "Você é professor de Matemática e Raciocínio Lógico e elaborador de questões das principais bancas de concursos (Cebraspe, FGV, Cesgranrio, Vunesp, FCC, IDECAN).")
  .replace('"subject": "Língua Portuguesa",', '"subject": "(a disciplina indicada na matéria)",')
  .replace("2. As cinco alternativas são frases completas e naturais, do dia a dia de bombeiros e do serviço público,", "2. As alternativas são valores ou conclusões no mesmo formato entre si; os distratores vêm de erros típicos de cálculo ou de raciocínio;")
  .replaceAll("IDECAN", "das bancas de concurso");
// Sem recorte: tira a seção do recorte e ajusta a entrega.
instr = instr.replace(/━━ RECORTE[\s\S]*?(?=━━ CONFERÊNCIA FINAL)/, "━━ RECORTE ━━\nNÃO gere recorte de banca neste pedido: as matérias são gerais e servem a vários concursos.\n\n")
  .replace("Entregue os 4 arquivos", "Entregue os 2 arquivos (um JSON por matéria)")
  .replace(/O capítulo 4 é sempre sobre reescrita e "como a [^"]*cobra"\./, 'O capítulo 4 é sempre "Como as bancas cobram": tipos de questão, pegadinhas e atalhos de resolução.');

const head = "══════════════════════════════════════════════════════════════\nMATÉRIAS DESTE PEDIDO\n══════════════════════════════════════════════════════════════\nPúblico: concursos de nível médio e superior (polícias, tribunais, bancos, carreiras militares). Matérias gerais, sem banca específica.\n\n";
const RULE = "- REGRAS DE MATEMÁTICA: teoria com fórmulas, propriedades e exemplos resolvidos passo a passo. Problemas com enunciado completo e dados suficientes (contextos de concurso: escalas de plantão, compras, salários, estoques, viaturas, agências). Resolva cada questão por inteiro ANTES de definir o gabarito e confira a conta; mostre a resolução na explicação (até 1.200 caracteres). Distratores vêm de erros típicos (somar porcentagens sucessivas, inverter a proporção, esquecer um caso, trocar arranjo por combinação). Certo/Errado no estilo Cebraspe: uma afirmação sobre um problema dado.";

// Recursos visuais: o mesmo formato que o app desenha (copiado do prompt do servidor).
const server = Deno.readTextFileSync("../supabase/functions/_shared/prompts/text-jobs-v1.ts");
const VISUAIS = "━━ RECURSOS VISUAIS (o app desenha) ━━\n" +
  server.slice(server.indexOf("Recursos visuais"), server.indexOf("\nRecorte:")).trim().replace(/^Recursos visuais[^\n]*\n/, "Use quando ajudarem a entender, não para enfeitar.\n") +
  '\n- No JSON, o bloco fica dentro da string (markdown, statement ou explanation) com \\n nas quebras e aspas internas escapadas: "texto\\n\\n```grafico\\n{\\"tipo\\":\\"barras\\", ...}\\n```\\n\\nmais texto".' +
  "\n- Em cada matéria: ao menos 1 gráfico ou figura por capítulo quando o assunto tem função, dado ou forma, e pelo menos 6 questões com gráfico, figura ou tabela no enunciado (em estatística e geometria, pelo menos 12). Confira que os números do desenho batem com o texto e com o gabarito.\n\n";

const materias: [string, string, string[], string][] = [
  ["porcentagem", "Porcentagem e variação percentual", ["Porcentagem", "Porcentagem e variação percentual", "Cálculo de porcentagem", "Aumentos e descontos sucessivos"], "cálculo de porcentagem; fração e número decimal equivalentes; aumento e desconto (fator multiplicativo); aumentos e descontos sucessivos; variação percentual e ponto percentual; porcentagem de porcentagem; lucro e prejuízo sobre custo e sobre venda; problemas com juros não incluídos (ficam em matemática financeira)."],
  ["razao-proporcao", "Razão, proporção e divisão proporcional", ["Razão e proporção", "Razões e proporções", "Divisão proporcional", "Grandezas proporcionais"], "razão e proporção; propriedades das proporções; grandezas diretamente e inversamente proporcionais; divisão em partes diretamente e inversamente proporcionais; escala; velocidade média e densidade como razões; misturas."],
  ["regra-de-tres", "Regra de três simples e composta", ["Regra de três simples e composta", "Regra de três", "Regra de três simples", "Regra de três composta"], "regra de três simples direta e inversa; regra de três composta com 3 ou mais grandezas; método de análise das grandezas; problemas de produção, tempo de trabalho, consumo e obras."],
  ["numeros-racionais", "Números racionais: frações e decimais", ["Frações e números decimais", "Números racionais", "Operações com frações", "Conjuntos numéricos e operações"], "conjuntos numéricos (N, Z, Q, I, R); frações: equivalência, simplificação e operações; números decimais e dízimas periódicas (fração geratriz); potenciação e radiciação; expressões numéricas e ordem das operações; comparação e ordenação; problemas com frações de um todo."],
  ["equacoes-primeiro-grau", "Equações e sistemas do 1º grau", ["Equações do 1º grau", "Sistemas de equações do 1º grau", "Equações e sistemas lineares", "Problemas com equações do 1º grau"], "equação do 1º grau e resolução; modelagem de problemas (idades, dinheiro, trabalho); inequações do 1º grau; sistemas lineares 2x2 e 3x3 (substituição, adição); sistema possível, impossível e indeterminado."],
  ["equacoes-segundo-grau", "Equação do 2º grau e problemas", ["Equações do 2º grau", "Equação do 2º grau", "Problemas com equações do 2º grau"], "equação do 2º grau completa e incompleta; fórmula de Bhaskara e discriminante; soma e produto das raízes; fatoração; equações biquadradas; modelagem de problemas de área e de lucro."],
  ["funcoes", "Funções afim e quadrática", ["Funções", "Função afim", "Função quadrática", "Noções de função", "Funções do 1º e 2º graus"], "conceito de função, domínio, imagem e gráfico; função afim: coeficientes, crescimento, raiz e gráfico; função quadrática: concavidade, vértice, máximo e mínimo, raízes; leitura de gráficos; noções de função exponencial e logarítmica com aplicações simples."],
  ["juros", "Juros simples e compostos", ["Juros simples e compostos", "Matemática financeira", "Juros simples", "Juros compostos", "Descontos"], "capital, taxa, tempo e montante; juros simples; juros compostos e fator de capitalização; taxas proporcionais e equivalentes; taxa nominal e efetiva; desconto simples comercial e racional; comparação entre regimes; uso de tabela de potências dada no enunciado."],
  ["logica-proposicional", "Lógica proposicional e tabela-verdade", ["Lógica proposicional", "Lógica sentencial", "Proposições simples e compostas", "Tabelas-verdade", "Tautologia, contradição e contingência"], "proposição, sentença aberta e não proposição; conectivos (negação, conjunção, disjunção inclusiva e exclusiva, condicional, bicondicional); tabelas-verdade e número de linhas; valor lógico de proposições compostas; tautologia, contradição e contingência."],
  ["equivalencias-negacoes", "Equivalências e negações lógicas", ["Equivalências lógicas", "Negação de proposições", "Leis de De Morgan", "Equivalências e implicações lógicas"], "negação de proposições simples e compostas; leis de De Morgan; negação da condicional e da bicondicional; equivalências da condicional (contrapositiva, ~p ∨ q); implicação lógica; negação de quantificadores (todo, algum, nenhum)."],
  ["argumentacao-logica", "Argumentação lógica e silogismos", ["Lógica de argumentação", "Argumentação lógica", "Silogismos", "Lógica de primeira ordem", "Diagramas lógicos"], "argumento, premissas e conclusão; argumento válido e inválido; métodos de verificação (tabela-verdade, conclusão falsa); silogismos categóricos; quantificadores universais e existenciais; diagramas de Venn para argumentos; falácias comuns cobradas."],
  ["sequencias-pa-pg", "Sequências, PA e PG", ["Sequências numéricas", "Progressão aritmética e progressão geométrica", "Progressões aritméticas e geométricas", "Sequências lógicas"], "sequências numéricas e lei de formação; progressão aritmética: termo geral, soma dos termos, interpolação; progressão geométrica: termo geral, soma finita e infinita; sequências recorrentes (Fibonacci) e alternadas; sequências de letras e figuras descritas em texto."],
  ["analise-combinatoria", "Análise combinatória", ["Análise combinatória", "Contagem", "Princípio fundamental da contagem", "Noções básicas de contagem"], "princípio fundamental da contagem (multiplicativo e aditivo); fatorial; permutação simples e com repetição; arranjo; combinação; diferença entre arranjo e combinação; permutação circular; problemas com restrições."],
  ["probabilidade", "Probabilidade", ["Probabilidade", "Noções de probabilidade", "Probabilidade e estatística"], "experimento aleatório, espaço amostral e evento; probabilidade clássica; evento complementar; união de eventos; probabilidade condicional; eventos independentes; problemas com dados, moedas, urnas e sorteios; ligação com análise combinatória."],
  ["estatistica-descritiva", "Estatística descritiva", ["Estatística", "Estatística descritiva", "Medidas de tendência central e de dispersão", "Noções de estatística", "Conceitos e aplicações básicas de estatística"], "população, amostra e variáveis; tabelas e gráficos (barras, setores, linhas, histograma); média aritmética e ponderada, mediana e moda; amplitude, variância e desvio padrão; leitura e interpretação de gráficos e tabelas; frequência absoluta e relativa."],
  ["geometria", "Geometria plana e espacial: áreas e volumes", ["Geometria plana", "Áreas e volumes", "Métrica: áreas e volumes", "Geometria espacial"], "ângulos e polígonos; triângulos (Pitágoras, semelhança); perímetro e área de quadrado, retângulo, triângulo, trapézio, losango e círculo; volume de prismas, cilindros, pirâmides, cones e esferas; conversão de unidades de área e volume (m³ e litros); escalas em plantas e mapas."],
];

const block = ([base, title, aliases, cover]: (typeof materias)[number], n: number) => `MATÉRIA ${n}
- Arquivo: ${base}.json (sem recorte)
- id: "matematica.${base}" | title: "${title}" | subject: "Matemática e Raciocínio Lógico"
- aliases: [${aliases.map((a) => `"${a}"`).join(", ")}] (acrescente outros nomes usuais do assunto)
- Casos obrigatórios: ${cover}
${RULE}`;

for (let i = 0; i < materias.length; i += 2) {
  const n = i / 2 + 1;
  Deno.writeTextFileSync(`pedidos/mat-lote-${String(n).padStart(2, "0")}.txt`, head + materias.slice(i, i + 2).map((m, k) => block(m, k + 1)).join("\n\n") + "\n\n\n" + VISUAIS + instr);
}
console.log(`${materias.length} matérias em ${materias.length / 2} lotes`);
