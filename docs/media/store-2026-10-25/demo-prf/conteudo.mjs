// Dados de exemplo da PRF para as capturas da Play Store (escritos à mão). montar-demo.mjs só
// acrescenta os campos fixos de cada linha e grava demo.json; não cria nem altera nenhum texto.
export const competicao = "PRF, Policial Rodoviário Federal";

export const materias = [
  [1, "Língua Portuguesa"],
  [2, "Direito Constitucional"],
  [3, "Direito Penal"],
  [4, "Legislação de Trânsito"],
  [5, "Raciocínio Lógico"],
  [6, "Física"],
  [7, "Informática"],
];

// [id, matéria, título, estudado?]
export const topicos = [
  [1, 4, "Sistema Nacional de Trânsito", true],
  [2, 4, "Normas gerais de circulação e conduta", true],
  [3, 4, "Habilitação e CNH", true],
  [4, 4, "Infrações e pontuação", true],
  [5, 4, "Crimes de trânsito", false],
  [6, 4, "Medidas administrativas", false],
  [7, 4, "Lei Seca: álcool e direção", false],
  [8, 1, "Interpretação de texto", true],
  [9, 1, "Crase", true],
  [10, 1, "Concordância verbal e nominal", false],
  [11, 1, "Pontuação", false],
  [12, 2, "Direitos e garantias fundamentais", true],
  [13, 2, "Segurança pública e a PRF", true],
  [14, 2, "Organização do Estado", false],
  [15, 3, "Princípios do Direito Penal", true],
  [16, 3, "Crimes contra a pessoa", false],
  [17, 3, "Crimes contra a administração pública", false],
  [18, 5, "Proposições e conectivos", true],
  [19, 5, "Porcentagem", false],
  [20, 6, "Cinemática", false],
  [21, 6, "Leis de Newton", false],
  [22, 7, "Segurança da informação", false],
  [23, 7, "Redes e internet", false],
];

export const teorias = [
  {
    id: 1, topicId: 4, title: "Infrações e pontuação",
    markdown: `# Infrações de trânsito e pontuação na CNH

## 1. O que é infração

Infração é toda inobservância das regras do Código de Trânsito Brasileiro (CTB), da legislação complementar e das resoluções do Contran. Cada infração tem uma natureza, que define a multa e os pontos lançados no prontuário do condutor.

## 2. As quatro naturezas

| Natureza | Pontos | Multa básica |
|---|---|---|
| Leve | 3 | R$ 88,38 |
| Média | 4 | R$ 130,16 |
| Grave | 5 | R$ 195,23 |
| Gravíssima | 7 | R$ 293,47 |

Algumas gravíssimas têm a multa multiplicada (por 3, 5, 10 ou até 60 vezes). O fator multiplica o valor da multa, não os pontos.

> Pegadinha clássica: a banca afirma que a gravíssima "vale 10 pontos". São 7, em qualquer caso.

## 3. Quando a CNH é suspensa por pontos

A suspensão por pontuação considera o período de 12 meses:

- **20 pontos**, se houver duas ou mais infrações gravíssimas;
- **30 pontos**, se houver uma gravíssima;
- **40 pontos**, se não houver nenhuma gravíssima.

Para o condutor que exerce atividade remunerada, o limite é de 40 pontos, independentemente da natureza das infrações.

## 4. Infrações que suspendem por si só

Algumas infrações levam à suspensão do direito de dirigir sem depender da soma de pontos. Os exemplos mais cobrados:

- dirigir sob a influência de álcool ou recusar o teste do etilômetro;
- exceder a velocidade máxima em mais de 50%;
- participar de racha ou manobra perigosa em via pública.

## 5. Celular ao volante

Usar o celular falando é infração média. **Segurar ou manusear** o aparelho enquanto dirige é infração **gravíssima**. A diferença entre "usar" e "manusear" aparece muito em prova.

## Síntese do capítulo

- Leve 3, média 4, grave 5, gravíssima 7.
- O fator multiplicador mexe na multa, nunca nos pontos.
- Suspensão por pontos: 20, 30 ou 40, conforme as gravíssimas.
- Álcool, velocidade acima de 50% e racha suspendem por si sós.`,
  },
];

export const resumos = [
  {
    id: 1, topicId: 4, title: "Resumo rápido, Infrações e pontuação",
    markdown: `# Infrações em 2 minutos

- **Leve 3 · Média 4 · Grave 5 · Gravíssima 7** pontos.
- Fator multiplicador aumenta a multa, não os pontos.
- Suspensão por pontos em 12 meses: **20** (duas ou mais gravíssimas), **30** (uma), **40** (nenhuma).
- Segurar o celular ao volante: **gravíssima**.

> Álcool ao volante suspende o direito de dirigir por 12 meses, sem depender de pontos.`,
  },
];

export const dicas = [
  [1, 4, "BIZU", "Para lembrar os pontos: 3, 4, 5 e pula para 7. A gravíssima nunca é 6 nem 10."],
  [2, 4, "PEGADINHA", "Multa multiplicada por 10 não significa 70 pontos: os pontos da gravíssima continuam 7."],
  [3, 4, "RECUPERACAO", "Sem consultar: com uma única gravíssima no período, a CNH é suspensa com quantos pontos?"],
  [4, 4, "RECUPERACAO", "Qual a diferença entre usar o celular e segurar o celular ao volante?"],
];

// [id, tópico, enunciado, explicação, alternativas: [texto, correta?][]]
export const questoes = [
  [1, 7,
    "Conduzir veículo sob a influência de álcool é infração gravíssima, punida com multa multiplicada por dez e suspensão do direito de dirigir por doze meses.",
    "**Certo.** É a infração da Lei Seca: gravíssima, multa multiplicada por **dez** e suspensão do direito de dirigir por **12 meses**, além do recolhimento do documento de habilitação e da retenção do veículo.\n\n> A recusa ao teste do etilômetro tem exatamente as mesmas penalidades.",
    [["Certo", true], ["Errado", false]]],
  [2, 4,
    "Quantos pontos uma infração gravíssima soma no prontuário do condutor?",
    "A gravíssima soma **7 pontos**.\n\n- **3**: leve.\n- **4**: média.\n- **5**: grave.\n- **10**: não existe; o fator multiplicador muda a multa, não os pontos.",
    [["3 pontos", false], ["4 pontos", false], ["5 pontos", false], ["7 pontos", true], ["10 pontos", false]]],
  [3, 4,
    "Segurar ou manusear o telefone celular enquanto dirige é infração de natureza média.",
    "**Errado.** Segurar ou manusear o celular é infração **gravíssima**. A infração média é usar o aparelho falando, sem segurá-lo.\n\n> A banca troca \"usar\" por \"manusear\" para pegar quem decorou pela metade.",
    [["Certo", false], ["Errado", true]]],
  [4, 4,
    "Transitar em velocidade superior à máxima permitida em mais de 50% é infração:",
    "Acima de **50%** do limite, a infração é **gravíssima**, com multa multiplicada por **três** e suspensão do direito de dirigir.\n\n- Até 20% acima: média.\n- Entre 20% e 50%: grave.",
    [["Média, sem suspensão", false], ["Grave, com multa em dobro", false], ["Gravíssima, com multa ×3 e suspensão", true], ["Leve, com advertência por escrito", false], ["Gravíssima, sem suspensão", false]]],
  [5, 13,
    "A Polícia Rodoviária Federal é órgão permanente, organizado e mantido pela União, destinado, na forma da lei, ao patrulhamento ostensivo das rodovias federais.",
    "**Certo.** É o texto da Constituição sobre a PRF, no capítulo da segurança pública: órgão **permanente**, organizado e mantido pela **União**, estruturado em carreira, para o **patrulhamento ostensivo das rodovias federais**.",
    [["Certo", true], ["Errado", false]]],
  [6, 12,
    "A casa é asilo inviolável do indivíduo, mas nela se pode entrar a qualquer hora do dia ou da noite por determinação judicial.",
    "**Errado.** Por determinação judicial, só **durante o dia**. A qualquer hora, só em flagrante delito, desastre ou para prestar socorro.\n\n> Ordem judicial à noite é a pegadinha mais repetida deste tema.",
    [["Certo", false], ["Errado", true]]],
  [7, 9,
    "Assinale a opção que completa corretamente a frase: \"O agente se referiu ___ normas de segurança da rodovia.\"",
    "**Às**: o verbo *referir-se* pede a preposição **a**, e *normas* pede o artigo **as**. Preposição + artigo = **às**, com crase.\n\n- **a** / **as**: falta a fusão.\n- **há**: é do verbo haver.\n- **à**: está no singular, e *normas* é plural.",
    [["a", false], ["as", false], ["às", true], ["à", false], ["há", false]]],
  [8, 18,
    "A negação da proposição \"O condutor bebeu e dirigiu\" é \"O condutor não bebeu ou não dirigiu\".",
    "**Certo.** Para negar uma conjunção (**e**), nega-se cada parte e troca-se o **e** pelo **ou** (lei de De Morgan).",
    [["Certo", true], ["Errado", false]]],
];

// Baralho de flashcards (### frente, verso no corpo).
export const baralho = [
  ["Quantos pontos tem cada natureza de infração?", "Leve **3**, média **4**, grave **5**, gravíssima **7**."],
  ["Álcool ao volante: quais as penalidades?", "Gravíssima, multa **×10** e suspensão por **12 meses**."],
  ["Recusar o bafômetro muda alguma coisa?", "Não: as penalidades são as mesmas da direção sob efeito de álcool."],
  ["Celular na mão ao volante é de que natureza?", "**Gravíssima**. Usar falando, sem segurar, é média."],
  ["Com quantos pontos a CNH é suspensa?", "**20** com duas ou mais gravíssimas, **30** com uma, **40** sem nenhuma (em 12 meses)."],
];
