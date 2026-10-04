// Pedidos de enriquecimento visual das matérias já publicadas (fórmulas, gráficos, figuras, tabelas).
// O chat devolve só capítulos e explicações reformatadas; questões e gabaritos não saem do arquivo.
// Rodar a partir de conteudo/: deno run --allow-read --allow-write pedidos/_gerar-enriquecer.ts
// Juntar a resposta: deno run --allow-read --allow-write ../scripts/biblioteca/enriquecer.ts <resposta.json>
const server = Deno.readTextFileSync("../supabase/functions/_shared/prompts/text-jobs-v1.ts");
const visuais = server.slice(server.indexOf("- Gráficos:"), server.indexOf("\n- Questões também")).trim();

const alvos: [string, string][] = [
  ["ciencias/atomos-moleculas-ions", "tabela de partículas (próton, nêutron, elétron: carga, massa, local); tabela de isótopos, isóbaros e isótonos com exemplos; figura de camadas eletrônicas (geometria com círculos concêntricos); barras de massa atômica média a partir das abundâncias; fórmulas $$A = Z + N$$ e massa atômica média."],
  ["ciencias/cinetica-quimica", "gráfico de concentração × tempo (reagente caindo, produto subindo); diagrama de energia com e sem catalisador (linha com duas séries mostrando a energia de ativação); fórmulas da velocidade média, da lei de velocidade $$v = k[A]^m[B]^n$$ e de Arrhenius em bloco; tabela de fatores que alteram a velocidade."],
  ["ciencias/eletroquimica", "tabela de regras do Nox; esquema da pilha de Daniell (figura com ânodo, cátodo e ponte salina, ou tabela comparando polos); fórmulas $$\\Delta E = E_{red,maior} - E_{red,menor}$$ e $$Q = i \\cdot t$$, leis de Faraday em bloco; tabela pilha × eletrólise."],
  ["ciencias/equilibrio-quimico", "gráfico de concentrações × tempo chegando ao equilíbrio; gráfico de velocidades direta e inversa se igualando; fórmulas de Kc, Kp, Kw e pH em bloco; tabela do princípio de Le Chatelier (perturbação → deslocamento); escala de pH em barras com exemplos."],
  ["ciencias/estequiometria", "tabela de relações mol–massa–volume–partículas; fórmulas $$n = \\frac{m}{M}$$ e da constante de Avogadro em bloco; exemplos resolvidos com a regra de três montada em tabela; pizza de composição percentual de uma substância; barras de reagente limitante × excesso."],
  ["ciencias/gases", "gráfico funcao da isotérmica (hipérbole p × V, $$pV = k$$), linha da isobárica (V × T) e da isocórica (p × T); fórmulas $$pV = nRT$$ e $$\\frac{p_1V_1}{T_1} = \\frac{p_2V_2}{T_2}$$ em bloco; tabela das transformações (o que é constante, lei, gráfico); conversão de °C para K em fórmula."],
  ["ciencias/leis-basicas-eletricidade", "fórmulas $$U = R \\cdot i$$, $$P = U \\cdot i$$, resistência equivalente em série e em paralelo, em bloco; gráfico funcao de U × i num resistor ôhmico (reta); figura de circuito em série e em paralelo (geometria com segmentos e rótulos R1, R2); tabela série × paralelo; barras de efeitos do choque por faixa de corrente (dados ilustrativos ou com fonte)."],
  ["ciencias/ligacoes-quimicas", "tabela iônica × covalente × metálica (formação, propriedades, exemplos); barras de eletronegatividade de elementos comuns (valores de Pauling); tabela de geometria molecular e polaridade; estruturas de Lewis descritas em texto ou tabela de pares eletrônicos."],
  ["ciencias/oscilacoes-simples-amortecidas-forcadas", "gráfico funcao do MHS ($$x = A\\cos(\\omega t)$$), da oscilação amortecida (envelope exponencial) e curva de ressonância (linha); fórmulas de período do pêndulo e do sistema massa-mola, de onda $$v = \\lambda f$$, de Stevin, de dilatação $$\\Delta L = L_0\\alpha\\Delta T$$ e da 1ª lei da termodinâmica em bloco; figura de vetores no empuxo; tabela de escalas termométricas."],
  ["ciencias/reacoes-quimicas", "tabela de tipos de reação (síntese, decomposição, simples troca, dupla troca) com padrão e exemplo; balanceamento passo a passo; tabela do triângulo do fogo e classes de incêndio quando couber; pizza ou barras da conservação da massa num exemplo."],
  ["ciencias/solucoes", "gráfico de curva de solubilidade × temperatura (linha, com 2 ou 3 sais, dados ilustrativos), marcando insaturada, saturada e supersaturada; fórmulas de concentração comum, molaridade, título, ppm e diluição $$C_1V_1 = C_2V_2$$ em bloco; tabela das propriedades coligativas."],
  ["ciencias/unidades-medidas", "tabela do SI (grandeza, unidade, símbolo) e de prefixos; gráficos funcao de MU (s × t) e MUV (v × t); figura de plano inclinado com decomposição de forças (vetores); fórmulas de cinemática, $$F = m \\cdot a$$, trabalho, energias, impulso e pressão em bloco."],
  ["raciocinio-logico/analise-situacoes", "tabelas de padrões de sequência; tabela de calendário para dias da semana; figura de orientação espacial (pontos cardeais com vetores) quando couber; quadro de associação lógica em tabela."],
  ["raciocinio-logico/conjuntos", "figura de diagrama de Venn com dois e três conjuntos (geometria com círculos e rótulos das regiões); fórmula $$n(A \\cup B) = n(A) + n(B) - n(A \\cap B)$$ e a de três conjuntos em bloco; tabela das operações com símbolo e significado; intervalos na reta (figura com segmentos)."],
  ["raciocinio-logico/problemas-aritmeticos", "fórmulas de porcentagem, média e contagem em bloco; barras e pizza em problemas de porcentagem; figuras de geometria plana (retângulo, triângulo retângulo 3-4-5, círculo) com medidas reais; matrizes escritas em LaTeX (\\begin{pmatrix}) em bloco; tabelas de dados."],
  ["raciocinio-logico/relacoes-arbitrarias", "tabelas-verdade completas em tabela Markdown (p, q, ¬p, p∧q, p∨q, p→q, p↔q); tabela de associação (pessoa × atributo) com ✓ e ✗; símbolos lógicos em LaTeX ($$p \\rightarrow q$$, $$\\neg p$$); tabela de equivalências e negações."],
];

const pedido = (path: string, dicas: string) => {
  const m = JSON.parse(Deno.readTextFileSync(`materias/${path}.json`));
  return `ENRIQUECIMENTO VISUAL DE MATÉRIA (anexo: ${path.split("/")[1]}.json)

Você é professor e designer instrucional. O arquivo anexado é uma matéria já revisada e publicada no app Estudário: "${m.title}" (id "${m.id}"). O conteúdo está correto, mas os capítulos estão só em texto corrido: fórmulas soltas na frase, nenhum gráfico, nenhuma figura, poucas tabelas. Sua tarefa é deixá-la com cara de material profissional de cursinho, SEM mudar o conteúdo.

━━ O QUE FAZER ━━
1. Reescreva o "markdown" de cada um dos ${m.chapters.length} capítulos, na mesma ordem e com o mesmo "title":
   - toda fórmula, equação, unidade composta ou símbolo matemático vira LaTeX entre cifrões DUPLOS;
   - fórmula principal fica DESTACADA em bloco, com $$ sozinho na linha antes e depois (o app centraliza):
     $$
     pV = nRT
     $$
     e logo abaixo uma lista dizendo o que é cada letra, com unidade;
   - fórmula curta dentro da frase: $$U = R \\cdot i$$ na própria linha;
   - pelo menos 1 gráfico ou figura por capítulo quando o assunto tem função, dado, processo ou forma (e no máximo 3);
   - comparações e classificações em tabela Markdown;
   - exemplos resolvidos em lista numerada, cada conta em LaTeX, fechando com o resultado em **negrito**;
   - alertas de prova com > no início da linha;
   - mantenha TODOS os fatos, números, exemplos e regras que já existem; pode reorganizar e acrescentar explicação curta, não pode cortar conteúdo nem mudar valores. Cada capítulo fica entre 1.500 e 3.500 caracteres.
2. Nas explicações das questões que têm conta ou fórmula, reescreva só a FORMA: contas em LaTeX, passo a passo numerado. Não mude a resolução, os números, a letra do gabarito nem a conclusão. Questões sem conta ficam de fora.

━━ O QUE DESENHAR NESTA MATÉRIA ━━
${dicas}

━━ FORMATO DOS RECURSOS (o app desenha exatamente isto) ━━
${visuais}
- Nunca use cifrão simples nem \\( \\) para fórmula (o app confunde com R$). Valor em reais fica em texto ("R$ 1.050,00").
- Dado inventado para ensinar leva "dados ilustrativos" na legenda. Os números do gráfico batem com os do texto.
- Na geometria, coordenadas com medidas reais e todo nome usado em segmentos, ângulos, círculos e vetores declarado em "pontos".

━━ ENTREGA ━━
Um único arquivo ${path.split("/")[1]}-visual.json, JSON válido, só com isto:
{
  "id": "${m.id}",
  "chapters": [ { "title": "(o mesmo título)", "markdown": "..." } ],
  "explanations": [ { "index": 12, "explanation": "..." } ]
}
- "index" é a posição da questão no array "questions" do anexo, contando do 0.
- Dentro das strings: quebras de linha como \\n, aspas como \\", e a barra do LaTeX dobrada (\\\\frac, \\\\cdot). O bloco de gráfico fica dentro do markdown assim: "texto\\n\\n\`\`\`grafico\\n{\\"tipo\\":\\"funcao\\", ...}\\n\`\`\`\\n\\nmais texto".
- Não devolva questions, flashcards nem outros campos.

━━ CONFERÊNCIA FINAL (faça antes de entregar) ━━
- O JSON abre sem erro; cada bloco grafico tem JSON válido de uma linha, com "tipo" entre pizza, barras, linha, funcao, geometria.
- Nenhum cifrão simples sobrou em fórmula; toda fórmula em bloco tem $$ sozinho na linha antes e depois.
- Nenhum fato ou número do original sumiu ou mudou; as explicações reescritas chegam à mesma resposta.
- Os títulos dos capítulos são idênticos aos do anexo.
`;
};

alvos.forEach(([path, dicas], i) => {
  Deno.writeTextFileSync(`pedidos/visual-${String(i + 1).padStart(2, "0")}-${path.split("/")[1]}.txt`, pedido(path, dicas));
});
console.log(`${alvos.length} pedidos de enriquecimento`);
