# Pedidos para o Gemini: 100 questões extras por matéria (19/10/2026, primeiro lote: 50 matérias mais usadas)

**Por quê:** no plano grátis o aluno recebe 10 questões na matéria e pode pedir mais 3 lotes de 10, ou seja, até 40 questões no mesmo tópico, e pode escolher só Certo/Errado ou só A a E. Hoje cada matéria tem cerca de 25 questões de A a E e 20 de C/E, e o recorte de um capítulo tem de 2 a 10. O que faltar a IA completa e isso **gasta dinheiro**. Com estas 100 questões a mais (50 C/E e 50 A–E), repartidas pelos capítulos mais cobrados, esses pedidos passam a sair de graça do banco.

**Como usar:** cole a **Mensagem 0** uma vez e depois um pedido por mensagem. Salve cada resposta como `<id>.questoes.json` na pasta Downloads e me avise. Se ele começar a errar o formato, abra uma conversa nova e cole a Mensagem 0 de novo. A ordem é pelo número de tópicos de edital que usam a matéria.

**Teste primeiro:** faça os 3 primeiros pedidos e me mande antes de seguir, para eu conferir a qualidade e o formato.

## Mensagem 0 (regras: cole uma vez)

```
Você vai escrever QUESTÕES EXTRAS para matérias de concursos públicos brasileiros que já existem no app Estudário. Não escreva teoria. Em cada mensagem eu passo UMA matéria: o id, os capítulos (com quantas questões quero em cada um) e o começo dos enunciados que já existem, para você não repetir. Responda com UM JSON completo e válido, sem nada antes ou depois:

{
  "materia": "<o id que eu passar>",
  "questions": [ ...exatamente 100 questões... ]
}

DISTRIBUIÇÃO (o conferidor recusa fora disso):
- Exatamente 100 questões: 50 TRUE_FALSE (Certo/Errado) e 50 MULTIPLE_CHOICE com 5 alternativas (A a E).
- Em cada capítulo, a quantidade que eu pedir, metade Certo/Errado e metade A a E (se for ímpar, tanto faz qual sobra).
- Dificuldade: em cada capítulo, cerca de 1/3 FACIL, 1/3 MEDIA e 1/3 DIFICIL, nos dois formatos. O aluno pode pedir "só difícil em Certo/Errado" e tem que haver.
- Não cite banca: são questões autorais no estilo das provas (misture os jeitos de cobrar de Cebraspe, FGV, FCC, Vunesp, Cesgranrio, Ibfc, Idecan e bancas regionais).

CADA QUESTÃO:
{
  "statement": "<enunciado, 20 a 4000 caracteres>",
  "format": "MULTIPLE_CHOICE" ou "TRUE_FALSE",
  "difficulty": "FACIL" | "MEDIA" | "DIFICIL",
  "options": [ {"key":"A","text":"...","correct":false,"comentario":"<por que esta alternativa está certa ou errada, 1 a 2 frases>"}, ... ],
  "explanation": "<mínimo 150 caracteres: o fundamento da resposta, ensinando o conceito>",
  "section": "<título EXATO do capítulo, copiado do que eu passar>",
  "sourceType": "AUTHORIAL", "board": null, "agency": null, "year": null, "sourceUrl": null
}
- TRUE_FALSE: opções exatamente {"key":"C","text":"Certo","correct":...} e {"key":"E","text":"Errado","correct":...}, sem "comentario". Metade das C/E de cada capítulo com gabarito Certo e metade Errado, mais ou menos.
- MULTIPLE_CHOICE: 5 alternativas A a E, TODAS com "comentario" (o app usa isso para montar a versão A a D tirando uma errada).
- Exatamente UMA opção correta. Sem alternativas repetidas. Nenhum enunciado igual ou quase igual a outro, nem aos que já existem e eu listar.
- A letra correta espalhada entre A, B, C, D e E (cada letra entre 15% e 25% das vezes). A correta NÃO pode ser a mais longa em mais de 40% das questões.
- Varie o tipo: conceito, caso prático, "assinale a incorreta", assertivas I, II e III, exceção, comparação, letra da lei. Não comece várias questões com a mesma frase de molde.
- Alternativas erradas PLAUSÍVEIS: erros que um candidato que estudou mal cometeria (troca de prazo, de órgão, de conceito vizinho, exceção virando regra). Nada absurdo ou caricato, como "publicar o nome do paciente" ou "eliminar toda fiscalização", que se elimina sem saber a matéria.
- A questão tem que cobrar o assunto DO CAPÍTULO indicado em "section", não de outro capítulo.
- Se não tiver certeza de número de artigo, lei, prazo ou súmula, não cite o número: cobre o conteúdo. Nunca invente norma, jurisprudência ou dado.
- Certo/Errado no estilo Cebraspe: afirmação única, precisa, com o erro (quando houver) num detalhe técnico, não em palavra absurda.
- Português do Brasil, nível de prova de concurso real.
```

## Pedido 1: SUS: legislação, princípios e controle social (`saude-publica.sus-legislacao`, 4812 tópicos de edital)

```
Matéria: saude-publica.sus-legislacao (SUS: legislação, princípios e controle social)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos constitucionais, determinantes da saúde e princípios do SUS": 20
- "2. Lei 8.080: campo de atuação, gestão e participação privada": 20
- "3. Controle social, Lei 8.142 e planejamento participativo": 21
- "4. Decreto 7.508: regiões, redes, portas, RENASES, RENAME e COAP": 21
- "5. Direitos dos usuários, vigilância, informação, avaliação e tutela da saúde": 18

Já existem (não repita):
- Uma pessoa procura atendimento público e informa nunca ter contribuído para a previdência.
- Uma gestão pretende concentrar todos os recursos apenas em prevenção, deixando de atender 
- Uma comunidade apresenta adoecimentos relacionados a água imprópria, baixa renda e moradia
- Um serviço identifica falhas sanitárias em alimentos comercializados e inspeciona sua prod
- Uma rede pública não tem capacidade suficiente de realizar exames e pretende contratar uni
- Uma reunião intersetorial avalia a situação de saúde e propõe diretrizes gerais com partic
- Uma comunidade questiona a composição de um Conselho de Saúde que reservou apenas 25% das 
- Para instituir determinada Região de Saúde, gestores conferem sua carteira mínima de ofert
- Uma região dispõe de atenção primária e regulação, mas precisa ordenar os atendimentos ini
- Uma equipe quer identificar o rol nacional de ações e serviços de saúde ofertados pelo SUS
- Em planejamento farmacêutico, uma gestão procura a lista nacional de medicamentos essencia
- Um estado precisa apoiar tecnicamente municípios, acompanhar redes e executar supletivamen
- Diante de necessidade de pactuar fluxos operacionais da rede regional, gestores consideram
- Um município apresenta Fundo de Saúde, conselho paritário, plano e relatórios de gestão, m
- Uma pessoa atendida pelo SUS recebe explicação técnica incompreensível e não é informada s
- Uma pessoa acompanhada na atenção primária é encaminhada para exame especializado e retorn
- Para produzir relatórios de nascimentos, mortalidade e agravos de notificação, uma equipe 
- Ao distribuir atribuições do SUS, uma equipe observa que o Distrito Federal não possui mun
- Uma pessoa apresenta receita de medicamento para atendimento administrativo da assistência
- Uma revisão de material didático afirma que RENAME, FTN e protocolos clínicos precisam ser
- Municípios limítrofes pertencentes a dois estados planejam integrar serviços em uma única 
- Uma região deseja formalizar entre entes responsabilidades individuais e solidárias, metas
- Um município deixa de cumprir os requisitos previstos no art. 4º da Lei 8.142 para receber
- Um Conselho de Saúde terá 24 assentos e seguirá a distribuição da Resolução CNS nº 453/201
- No planejamento de uma rede, a gestão deve distinguir o instrumento quadrienal que fixa di
- Uma pessoa sem contribuição previdenciária solicita atendimento público. O caráter univers
- Na organização do SUS, a prioridade constitucional conferida às ações preventivas permite 
- A execução de vigilância epidemiológica, de vigilância sanitária, de ações de saúde do tra
- Uma conferência municipal de saúde deve reunir-se mensalmente por possuir caráter permanen
- Um Conselho de Saúde acompanha a execução da política sanitária, inclusive aspectos econôm
- O Decreto 7.508 condiciona a instituição de uma Região de Saúde à existência apenas de ate
- No Decreto 7.508, a RENASES reúne ações e serviços destinados à integralidade da assistênc
- Como a assistência à saúde é livre à iniciativa privada, uma operadora que comercializa pl
- Na repartição gerencial prevista na Lei 8.080, a direção estadual pode apoiar técnica e fi
- De acordo com a paridade da Lei 8.142, um conselho com 20 vagas deve reservar cinco a usuá
- O processo de planejamento da saúde deve ser ascendente e integrado, do nível local até o 
- Por ser a atenção primária a coordenadora usual do cuidado, o art. 9º do Decreto 7.508 exc
- O direito à informação sobre o próprio estado de saúde e o respeito à autonomia e à privac
- A assistência farmacêutica regulada pelo art. 28 do Decreto 7.508 é garantida na via admin
- Com a alteração do art. 26 do Decreto 7.508 pelo Decreto 11.161/2022, a RENAME tem consoli
- O art. 30 do Decreto 7.508 situa a Comissão Intergestores Regional no âmbito nacional e vi
- Um COAP regional pode fixar responsabilidades individuais e solidárias dos entes, metas, i
- A previsão de pelo menos 70% aos municípios no art. 3º, § 2º, da Lei 8.142 significa que e
- Para investigar notificações de agravos, a gestão pode usar o SINAN; para identificar esta
- A universalidade do SUS torna juridicamente obrigatória, por si só, a entrega administrati
```

## Pedido 2: Interpretação de textos (`portugues.interpretacao-textual`, 4451 tópicos de edital)

```
Matéria: portugues.interpretacao-textual (Interpretação de textos)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Compreensão global, informação explícita e hierarquia de ideias": 18
- "2. Inferências, pressupostos, implícitos, fato e opinião": 18
- "3. Coesão, argumentação, vozes e efeitos de sentido": 17
- "4. Contexto histórico-social, intertextualidade e leitura literária": 30
- "5. Textos multimodais, dados, gráficos e leitura crítica": 17

Já existem (não repita):
- Leia: 'O bairro ganhou uma praça, recebeu árvores e passou a organizar feiras semanais. Co
- Um aviso informa: 'As visitas guiadas ocorrerão às quartas e sextas, mediante agendamento 
- Considere: 'Alguns estudantes preferem assistir às aulas gravadas; outros optam pelas tran
- Leia: 'O projeto recebeu financiamento. Foram comprados livros e computadores. A equipe, c
- O texto diz: 'Embora o número de ônibus tenha aumentado, passageiros continuam relatando e
- Em uma narrativa, 'Rita entrou com os sapatos encharcados e deixou pequenas poças no corre
- Ao escrever 'Carlos voltou a frequentar o clube aos domingos', qual pressuposto linguístic
- Leia: 'O teatro recebeu 600 pessoas na estreia, mas a encenação foi cansativa.' Qual segme
- Um relatório afirma: 'A nova ferramenta poderá reduzir erros após a fase de testes.' Qual 
- Uma pessoa afirma, perto da saída de uma sala vazia: 'As janelas ainda estão abertas.' Pel
- Leia: 'As equipes reorganizaram a escala; portanto, os turnos passaram a ter melhor cobert
- Considere: 'A pesquisadora publicou um artigo. O trabalho recebeu comentários positivos.' 
- Leia: 'A feira incentiva pequenos produtores. No último mês, quinze famílias venderam dire
- Uma reportagem registra: 'Segundo uma especialista, o projeto ainda precisa de ajustes.' Q
- Após esperar duas horas para ser atendido, um personagem comenta: 'Nossa, que rapidez exem
- Um cartaz de 1985 convoca moradores a economizar água 'durante o racionamento'. Em uma aná
- Um anúncio escreve 'Quem semeia ideias colhe mudanças', retomando reconhecivelmente o dito
- Um poema antigo glorifica um viajante que 'vence o mundo sem sair de casa'. Décadas depois
- Em um conto, o narrador relata: 'Eu jurava conhecer as intenções de todos, mas a última ca
- Uma crônica afirma: 'A cidade acordou cansada, com os semáforos piscando como olhos sem so
- Um infográfico informa: 'Atendimentos por semana: semana 1 = 80; semana 2 = 100'. O texto 
- Um gráfico apresenta 'livros emprestados: janeiro = 200; fevereiro = 240', acompanhado do 
- Uma tirinha descreve dois quadros. No primeiro, alguém diz 'Vou desligar tudo e descansar'
- Em uma tabela, o grupo X tem 50 participantes, dos quais 30 aprovaram uma proposta; o grup
- Um cartaz reúne a frase 'Talvez a nova horta reduza o calor' e uma fotografia de árvores r
- Leia: 'As portas abrirão às 9h, e o atendimento começará às 9h30.' É correto afirmar que o
- No trecho 'O jardim recebeu mudas de árvores. A iniciativa pretende melhorar as áreas de s
- Em 'Parte dos visitantes elogiou a exposição', a substituição de 'parte' por 'todos' manté
- No texto 'Joana tirou o casaco molhado e o colocou ao lado do aquecedor', é possível infer
- A oração 'Marcos deixou de usar bicicletas compartilhadas' pressupõe que Marcos nunca havi
- Em 'Talvez a biblioteca amplie seu horário', o advérbio sinaliza possibilidade, não a real
- Em 'O parque recebeu 200 árvores, medida considerada insuficiente pelos moradores entrevis
- No período 'A praça recebeu iluminação; entretanto, ainda há trechos escuros', o conectivo
- Em 'O diretor apresentou a proposta. A medida será discutida amanhã', o sintagma 'a medida
- Se um texto afirma 'Segundo Pedro, a sala precisa de reparos', o autor do texto necessaria
- O período 'O bairro precisa de mais árvores, pois a sombra pode tornar os trajetos mais co
- Duas campanhas que incentivam a paciência são necessariamente paródias uma da outra, mesmo
- Um aviso de 1990 que menciona falta de energia durante uma crise local deve ser interpreta
- Em uma narrativa, a frase 'Eu só soube da viagem meses depois' indica uma limitação do con
- Uma frase posicionada como epígrafe antes de um conto não pode simultaneamente ser uma cit
- Uma tabela informa 90 presenças em um conjunto de 120 inscrições. A proporção de presenças
- Se dois textos concordam que houve crescimento das ciclovias, mas um o elogia e o outro o 
- Um gráfico de atendimentos exibe 400 casos no mês A e 480 no mês B. A variação relativa po
- Em uma tirinha, a personagem declara 'Estou completamente desconectada', mas é representad
- Uma pesquisa informa que dois indicadores cresceram no mesmo período. Essa coincidência te
```

## Pedido 3: Classes de palavras (`portugues.classes-de-palavras`, 4081 tópicos de edital)

```
Matéria: portugues.classes-de-palavras (Classes de palavras)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Critérios de classificação e substantivos em contexto": 27
- "2. Adjetivos, flexão nominal e expressões qualificadoras": 27
- "3. Advérbios, locuções adverbiais, interjeições e modalização": 14
- "4. Preposições, conjunções e locuções conectivas": 15
- "5. Morfossintaxe contextual, coesão e leitura crítica": 17

Já existem (não repita):
- Em um comunicado, lê-se: 'A prudência evitou o conflito'. Qual classificação contextual é 
- Uma ata registra 'O estudante apresentou o projeto' e, depois, 'A estudante apresentou o p
- No relatório 'A equipe apresentou conclusões claras', a palavra 'claras' pertence a qual c
- Um aviso diz: 'As pesquisadoras chegaram cedo'. No contexto, 'cedo' é:
- Em 'A reunião ocorreu à noite', a expressão 'à noite' funciona, como conjunto, como:
- Considere: 'O documento foi redigido com cuidado'. Que classe possui a palavra 'com' nesse
- No trecho 'O prazo terminou, mas a equipe prosseguiu', qual o valor da conjunção 'mas'?
- Ao saber que o problema foi resolvido, uma pessoa exclamou: 'Ufa!'. Como classificar 'Ufa!
- Em 'Ele trabalhou, portanto concluiu a tarefa', 'portanto' exprime:
- Uma mensagem informa: 'As alunas estavam meio apreensivas'. A análise gramatical de 'meio'
- Em 'O essencial foi mantido na versão final', o termo 'essencial' apresenta que emprego?
- Uma narrativa distingue 'um velho amigo' de 'um amigo velho'. Qual avaliação reconhece o e
- Compare 'A atividade foi cancelada por causa da chuva' e 'A atividade foi cancelada porque
- Um depoimento afirma: 'As crianças falaram alto durante a apresentação'. No trecho, 'alto'
- Analise os enunciados 'O grupo apresentou bastantes razões' e 'As razões eram bastante cla
- Em 'Embora estivesse chovendo, o encontro ocorreu', que relação a locução introduzida por 
- Um texto apresenta 'A análise de emergência foi concluída' e 'A análise terminou às pressa
- Compare 'Somente Luísa revisou o texto' e 'Luísa somente revisou o texto'. Qual alteração 
- Considere 'Retorne, pois precisamos conversar' e 'Precisamos conversar; retornemos, pois, 
- Leia 'Desde que receba a autorização, iniciará o trabalho' e 'Desde que recebeu a autoriza
- Em 'O amanhã nos preocupa' e 'Amanhã analisaremos a proposta', qual descrição da palavra '
- Uma descrição emprega 'pareceres técnico-científicos', 'camisas azul-marinho' e 'pesquisad
- Em 'Falou franca e cuidadosamente' e 'Apresentou resposta franca e cuidadosa', a forma 'fr
- Um texto altera 'O relatório foi rejeitado, embora estivesse completo' para 'O relatório f
- Compare 'A entrega foi adiada até amanhã' e 'Até a equipe novata percebeu a falha'. Qual l
- No enunciado 'A tristeza desapareceu', 'tristeza' é substantivo abstrato por nomear um est
- Na frase 'A vítima recebeu apoio', o substantivo 'vítima' é comum de dois gêneros porque a
- Em 'O documento claro facilitou a leitura', a palavra 'claro' caracteriza o substantivo 'd
- No trecho 'As participantes chegaram ontem', o advérbio 'ontem' deve receber plural para c
- Em 'A equipe trabalha com atenção', 'com' é preposição e 'com atenção' funciona, no conjun
- No enunciado 'Ele queria continuar, mas desistiu', a conjunção 'mas' estabelece relação co
- No trecho 'A resposta foi dada com serenidade', a expressão 'com serenidade' modifica a aç
- Na frase 'As equipes estavam meias preocupadas', a forma 'meias' está adequada porque o in
- Em 'O inesperado aconteceu', o termo 'inesperado' apresenta emprego substantivado, enquant
- No trecho 'Ela estava só e os colegas estavam sós', a forma 'só' é advérbio invariável nas
- Em 'A reunião começou depois que os convidados chegaram', a expressão 'depois que' introdu
- Em 'O prazo terminou; portanto, será necessária outra etapa', 'portanto' é classificado co
- Em 'Felizmente, a proposta foi aprovada', o advérbio inicial exprime avaliação do enunciad
- No trecho 'A reunião de emergência terminou', a expressão 'de emergência' exerce valor adj
- Em 'Como a estrada estava bloqueada, a equipe voltou', 'como' pode funcionar como conjunçã
- Na expressão 'As pesquisadoras escreveram clara e objetivamente', 'clara' deve necessariam
- Em 'Somente o diretor aprovou a proposta', a posição de 'somente' focaliza quem aprovou; m
- Em 'As equipes possivelmente terminarão hoje', substituir 'possivelmente' por 'certamente'
- Em 'Socorro!', usado como chamado urgente, 'socorro' pode funcionar como interjeição; já e
- Em 'Até os iniciantes entenderam', a palavra 'até' desempenha exatamente o mesmo papel pre
```

## Pedido 4: Significação contextual de palavras e expressões (`portugues.significacao-contextual-palavras-expressoes`, 4029 tópicos de edital)

```
Matéria: portugues.significacao-contextual-palavras-expressoes (Significação contextual de palavras e expressões)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Sentido lexical, contexto, denotação e conotação": 25
- "2. Relações semânticas: sinonímia, antonímia, hierarquia e campos": 28
- "3. Polissemia, homonímia, paronímia, heteronímia e verbetes": 18
- "4. Ambiguidade, implícitos, inferência e pragmática": 16
- "5. Efeitos de sentido: ironia, citação, expressões e substituição": 13

Já existem (não repita):
- Um aviso informa: 'O banco da praça receberá nova pintura'. Considerando o contexto, qual 
- No trecho 'A equipe buscou a raiz do problema antes de propor mudanças', a expressão 'raiz
- Uma notícia informa que 'foram plantados ipês no parque'. Qual substituição por um hiperôn
- Após localizar um erro aritmético, a revisora recebeu a orientação: 'É necessário retifica
- Em uma atividade de vocabulário, pede-se identificar palavras de mesma pronúncia e grafias
- O enunciado 'Marta voltou a frequentar a biblioteca' contém qual informação pressuposta?
- Ao observar um serviço que demorou quatro horas para uma tarefa simples, alguém comentou: 
- Uma descrição compara dois ambientes, chamando um de 'quente' e outro de 'frio'. Esses ant
- No verbete de 'chave', encontra-se a acepção 'elemento decisivo para resolver uma questão'
- Durante uma reunião, foi dito: 'Alguns participantes enviaram sugestões'. Sem contexto adi
- No comunicado 'Foi expedido um mandado para cumprimento da determinação', qual substituiçã
- Em 'A proposta mitigou o impacto financeiro', qual reescrita preserva mais precisamente o 
- Uma mensagem enviada sem registro de data diz: 'A entrega será realizada amanhã'. Qual dad
- No relato 'Após a sessão de debates, foi assinada a cessão do espaço e indicada a seção re
- Considere: 'Segundo Elisa, o novo método é infalível'. Qual conclusão respeita a atribuiçã
- Uma passagem reúne 'palco', 'plateia', 'camarim' e 'roteiro'. Sem que os termos sejam sinô
- Em 'A turma leu Clarice Lispector ao longo do semestre', o nome da escritora pode ser ente
- O texto diz: 'Na decisão, a cabeça do grupo concordou; na imagem, a cabeça do retratado ap
- Uma ata registra: 'Nem todos os presentes aprovaram a proposta'. Assumindo que havia prese
- Um artigo comenta, após sucessivas falhas, que a empresa apresentou uma 'solução brilhante
- No par de frases 'Nina comprou um quadro de Raul' e 'Raul vendeu um quadro a Nina', qual r
- Em uma classificação tradicional do vocabulário, apresenta-se o par 'boi/vaca' como exempl
- Considere a pergunta 'Carlos não voltou a trabalhar naquele local?'. Qual informação tende
- No relato 'Quando a negociação travou, Luiza resolveu pôr panos quentes', a expressão dest
- Em um verbete, a acepção 'conjunto de conexões' vem marcada como figurada e é acompanhada 
- Em 'A ponte sobre o rio foi interditada', o vocábulo 'ponte' aparece em sentido predominan
- Quando alguém solicita 'ratificar um dado incorreto', o verbo destacado indica, por seu si
- Em 'Foram adquiridos computadores', trocar 'computadores' por 'equipamentos' usa uma categ
- A classificação de 'colher' como verbo e 'colher' como utensílio demonstra que homógrafos 
- Na fala 'O projeto morreu antes do lançamento', o verbo 'morreu' pode indicar abandono ou 
- As palavras 'comprar' e 'vender' são antônimos graduais, assim como 'alto' e 'baixo', pois
- A dupla 'coser' e 'cozer' pode ilustrar homofonia, pois as grafias se distinguem e a pronú
- Na frase 'A cessão reuniu vários pesquisadores para discutir o relatório', a grafia de 'ce
- Na sequência 'teclado, monitor, processador, mouse', os itens integram um campo lexical li
- Em 'Apenas quatro estudantes compareceram', o advérbio 'apenas' indica que o número quatro
- O enunciado 'Helena deixou de dirigir à noite' apresenta a direção noturna anterior como i
- O Vocabulário Ortográfico da Língua Portuguesa substitui um dicionário explicativo complet
- Em 'De acordo com o entrevistado, a reforma foi prejudicial', o adjetivo 'prejudicial' com
- Quando uma mensagem afirma 'Estou aqui hoje', é possível estabelecer o lugar e a data exat
- Ao dizer 'Alguns relatórios foram aprovados; na verdade, todos foram aprovados', uma pesso
- O emprego de 'manga' para fruta e para parte de uma camisa é exemplo necessário de polisse
- No relato de uma pane após a instalação de um sistema apresentado como 'perfeito', o uso d
- Quando 'hipotenusa' é empregado tecnicamente na geometria, isso demonstra que o vocábulo n
- A pergunta 'Você consegue fechar a janela?', dita a alguém próximo de uma janela aberta em
- Na frase 'A chefia cortou as asas do projeto', a interpretação contextual adequada precisa
```

## Pedido 5: Pontuação (`portugues.pontuacao`, 3694 tópicos de edital)

```
Matéria: portugues.pontuacao (Pontuação)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Vírgula: termos essenciais, intercalações e coordenação": 20
- "2. Vírgula entre orações: subordinação, restrição e sentido": 20
- "3. Ponto final, ponto e vírgula e dois-pontos na estrutura textual": 20
- "4. Travessão, parênteses, aspas, reticências, interrogação e exclamação": 20
- "5. Ordem direta e inversa, coesão, clareza e revisão de textos": 20

Já existem (não repita):
- Um redator escreveu: “Os relatórios recebidos durante a semana, foram conferidos.” Que cor
- Uma mensagem deve chamar diretamente os integrantes de uma equipe: “Colegas iniciem a conf
- Uma lista simples reúne três itens: atas, ofícios e memorandos. Qual redação utiliza adequ
- Em “A equipe preparou o material mas não conseguiu enviá-lo”, deseja-se demarcar a oposiçã
- Um período começa com a circunstância extensa “Durante a reunião de planejamento realizada
- A frase “Os candidatos que entregaram a documentação receberam a confirmação” deve indicar
- Uma pessoa escreveu: “A coordenadora informou, que a sessão começaria às oito horas.” Cons
- Um autor pretende acrescentar comentário sobre uma pesquisadora já identificada: “Lia que 
- Uma instrução começa pela condição: “Se os anexos estiverem completos o cadastro será conc
- Uma frase precisa explicar a conclusão: “O arquivo estava corrompido portanto foi solicita
- Um relatório organiza dois grupos, cada qual com explicações internas: “A primeira equipe 
- Uma instrução precisa introduzir uma lista depois de uma expressão que já a anuncia. Qual 
- No enunciado “O primeiro grupo analisou contratos; o segundo, fotografias”, que fenômeno j
- O adjunto temporal “Hoje” aparece antes do sujeito em duas versões: “Hoje haverá reunião” 
- Uma versão de texto dizia: “O arquivo estava ilegível. Por isso, solicitamos nova cópia.” 
- Uma citação integral é introduzida por verbo declarativo: “A revisora afirmou: ...”. Qual 
- Uma narrativa apresenta fala direta acompanhada de comentário do narrador: “A porta está a
- No período “Os documentos — recebidos pela manhã — foram encaminhados”, o segmento destaca
- O redator propôs substituir “Os livros que chegaram danificados foram devolvidos” por “Os 
- Ao responder a “Podemos aguardar?”, uma pessoa escreve “Não, podemos aguardar.” Outra vers
- Uma narrativa apresenta “A revisora perguntou: ‘O documento foi enviado?’” e pretende conv
- Para tornar clara uma lista formal com itens que já contêm vírgulas, uma instrução apresen
- Um texto dizia: “Os dados recebidos ontem foram publicados.” Para indicar a fonte da infor
- Um período foi escrito para não confundir “os fornecedores” com o complemento de “comprar”
- Um revisor encontra o período “Na reunião de ontem, os participantes, após examinarem as t
- No período “Os resultados de todos os testes realizados durante a semana foram enviados”, 
- Em “A equipe entregou, os documentos”, a vírgula entre verbo e objeto direto é exigida por
- Na frase “Amigos, confiram o arquivo”, a vírgula isola “Amigos” como vocativo, isto é, ter
- Como “Hoje” é adjunto adverbial anteposto, a redação “Hoje todos compareceram” é necessari
- A sequência “Conferimos mapas, fotos, recibos e assinaturas” constitui enumeração simples 
- A vírgula antes da conjunção “e” é proibida mesmo quando liga duas orações com sujeitos di
- A pergunta direta “O relatório ficou pronto?” pede ponto de interrogação; ao relatar o con
- As frases “Os arquivos que chegaram hoje serão arquivados” e “Os arquivos, que chegaram ho
- Em “Embora houvesse poucos dados, a equipe concluiu a análise”, a vírgula separa uma subor
- A sequência “Precisamos de: atenção, tempo e revisão” exemplifica o emprego obrigatório de
- No período “O projeto — ajustado após a reunião — foi aprovado”, os dois travessões delimi
- No discurso indireto “A diretora afirmou que a reunião havia terminado”, as aspas são obri
- A expressão “Eu ainda esperava que...” pode usar reticências para indicar suspensão intenc
- Como o til e o hífen aparecem graficamente dentro de palavras, ambos desempenham a mesma f
- Em “Chegaram ao depósito três caixas”, o sujeito “três caixas” aparece depois do verbo; es
- No período “A equipe terminou o parecer, e os revisores enviaram os anexos”, a vírgula é i
- A redação “A equipe, segundo a coordenação, concluíra o texto” mantém a relação entre suje
- Em “Faltou uma assinatura. Por isso, o envio foi adiado”, a troca de “por isso” por “contu
- Em uma narrativa, a frase “A reunião terminara. Como contaria a notícia?” pode sugerir pen
- Uma sequência formal pode repetir exclamações e reticências para ampliar intensidade sem q
```

## Pedido 6: Ortografia oficial: emprego das letras (`portugues.ortografia`, 3619 tópicos de edital)

```
Matéria: portugues.ortografia (Ortografia oficial: emprego das letras)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Sistema ortográfico oficial, alfabeto, h e maiúsculas/minúsculas": 23
- "2. S, Z, SS, C, Ç, SC, SÇ e XC: grafia e famílias lexicais": 19
- "3. X, CH, G e J: padrões, etimologia e contrastes": 19
- "4. E/I, O/U e conservação das famílias lexicais": 19
- "5. Homônimos, parônimos e aplicação da ortografia na escrita formal": 20

Já existem (não repita):
- Na revisão de uma nota sobre o Acordo Ortográfico, um redator precisa indicar corretamente
- Uma questão de sistema gráfico pede a quantidade de letras do alfabeto oficial e o tratame
- Em um comunicado, a frase informa que determinado prazo termina em uma unidade de tempo, e
- Uma agenda de eventos será publicada com o mês, a estação do ano e uma festividade. Qual c
- Um relatório descreve uma atitude marcada por muito carinho. Qual adjetivo completa corret
- Em frase sobre qualidade de algo belo, é necessário empregar o substantivo abstrato deriva
- Uma ordem recomenda: 'É importante que a planta ___ vigorosa'. A forma verbal desejada é d
- No comunicado 'A ___ de estudo começa amanhã', o espaço deve ser preenchido por substantiv
- O texto informa que a equipe precisou examinar dados e produzir uma análise detalhada. Qua
- Uma minuta precisa empregar o verbo formado de 'moderno' com sentido de tornar moderno. Qu
- Um revisor encontrou a frase 'A excessão foi registrada como justificativa'. Qual interven
- Ao organizar um inventário, alguém escreve que irá 'enxarcar a roupa em água' e 'encher um
- Em 'Espero que os participantes ___ na próxima semana', deseja-se o presente do subjuntivo
- Uma frase descreve uma característica positiva de alguém: 'Trata-se de pessoa ___ na área 
- Em um documento, a expressão refere-se à medida física de uma sala e não à execução de uma
- Um termo formaliza a transferência de direitos, enquanto outro anuncia o horário de uma re
- Uma instrução trata da grafia dos nomes de disciplinas escolares e afirma que o Acordo imp
- Uma redação descreve a nacionalidade de uma participante e a qualidade de um país próspero
- Em um relato, a pessoa primeiro movimentou um objeto e depois afastou uma porção de cabelo
- Um aviso contém duas construções: 'A ___ foi adiada' e 'Espero que os participantes ___ am
- Após verificar um dado inexato, uma equipe precisa corrigi-lo; em seguida, a autoridade co
- Em um texto informativo, mencionam-se um levantamento estatístico e a capacidade de julgar
- Uma nota diz: 'A análise foi feita ___; ___, os registros foram confrontados'. Pretende-se
- Uma redação descreve deslocamento geográfico com 'o vento veio do ___' e, depois, faz refe
- Um texto diz que 'a sesssão foi transferida para outro setor; a secção de arquivos formali
- Ao conferir a base normativa da ortografia brasileira, um redator informa que o Decreto nº
- Segundo o Acordo Ortográfico, o alfabeto português permanece com 23 letras, porque k, w e 
- Em um aviso sobre horário de atendimento, escrever 'hora' com h está correto, porque o sub
- Em programação de eventos, as formas 'Setembro', 'Outono' e 'Páscoa' obedecem igualmente à
- Os adjetivos 'perigoso' e 'carinhosa' utilizam s na terminação -oso/-osa, enquanto o subst
- Na frase 'Ela é portugueza', a grafia com z está adequada à regra dos adjetivos femininos 
- A relação entre 'nascer' e 'nasça' mostra que o grupo sc do infinitivo pode corresponder a
- A sequência 'enxergar, enxugar, enxame' prova que qualquer vocábulo iniciado por en- e com
- Em 'A viagem ocorrerá em julho' e 'Espero que eles viajem em julho', empregam-se respectiv
- Por terminar com som semelhante ao de viagem, o substantivo 'pajem' deve ser reescrito 'pa
- Em um documento de medição, 'comprimento da ponte' nomeia sua extensão; já 'cumprimento da
- A palavra 'beneficiente' é a grafia recomendada para alguém que faz o bem, porque o segmen
- Os nomes 'Natal' e 'Páscoa' recebem iniciais maiúsculas como festividades, mas a referênci
- Como o substantivo 'análise' contém s, sua família regular autoriza grafar 'analisar' com 
- Na Base XIX, os nomes de disciplinas admitem inicial maiúscula ou minúscula, de modo que a
- Em um termo que apresenta a 'descrição detalhada' de bens, a substituição por 'discrição d
- Um ato que anula licença pode usar o verbo 'cassar', ao passo que procurar ou perseguir um
- Na redação de relato de inspeção, 'ad-hoc' e 'in-loco' são as grafias normais dessas locuç
- As formas 'dormir' e 'durmo' exemplificam alternância vocálica na conjugação; a existência
- Quando uma pessoa confirma expressamente decisão anterior, o verbo mais preciso é 'retific
```

## Pedido 7: Políticas Nacionais de Saúde: Atenção Básica, Promoção, Humanização e Redes (`saude-publica.politicas-nacionais-saude`, 3433 tópicos de edital)

```
Matéria: saude-publica.politicas-nacionais-saude (Políticas Nacionais de Saúde: Atenção Básica, Promoção, Humanização e Redes)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Redes de Atenção à Saúde e Atenção Básica": 20
- "2. Promoção da saúde e determinantes sociais": 20
- "3. Política Nacional de Humanização (HumanizaSUS)": 20
- "4. Pacto pela Saúde e Rede de Urgência e Emergência": 20
- "5. Programa Nacional de Imunizações": 20

Já existem (não repita):
- Nas Redes de Atenção à Saúde, o ponto que funciona como centro de comunicação, coordenando
- As Redes de Atenção à Saúde propõem substituir o modelo hierárquico piramidal por uma rede
- Segundo a PNAB 2017, são princípios da Atenção Básica:
- A equipe de Saúde da Família deve ser composta, no mínimo, por médico, enfermeiro, auxilia
- Pela PNAB 2017, a população adscrita recomendada por equipe de Saúde da Família e o número
- O acompanhamento da pessoa pela mesma equipe ao longo do tempo, com vínculo e responsabili
- A Estratégia Saúde da Família é considerada pela PNAB a estratégia prioritária para expans
- Constituem diretrizes da Política Nacional de Atenção Básica, EXCETO:
- São campos de ação da promoção da saúde definidos pela Carta de Ottawa (1986), EXCETO:
- A Política Nacional de Promoção da Saúde, aprovada em 2006, foi revisada em 2014 e tem a i
- É tema prioritário da Política Nacional de Promoção da Saúde:
- A diferença central entre promoção da saúde e prevenção de doenças é que:
- No modelo de determinantes sociais de Dahlgren e Whitehead, a camada mais interna, de difí
- No modelo de Dahlgren e Whitehead, habitação, saneamento, educação, ambiente de trabalho e
- Iniquidades em saúde são diferenças nas condições de saúde entre grupos que podem ser cons
- A Política Nacional de Humanização, lançada em 2003, caracteriza-se por ser:
- São princípios da Política Nacional de Humanização:
- Na Política Nacional de Humanização, o acolhimento é entendido como etapa de recepção e tr
- O dispositivo da PNH usado nos serviços de urgência para organizar o atendimento de acordo
- O Projeto Terapêutico Singular é um conjunto de propostas de condutas articuladas para um 
- A diretriz da PNH que propõe considerar a singularidade do sujeito e seu contexto, e não a
- O princípio da PNH segundo o qual o modo de cuidar e o modo de gerir não se separam, deven
- O Pacto pela Saúde, instituído pela Portaria 399/2006, é composto por três dimensões:
- Entre as prioridades do Pacto pela Vida estavam a saúde do idoso, o controle do câncer de 
- O instrumento que sucedeu o Pacto pela Saúde na organização das responsabilidades entre os
- São componentes da Rede de Atenção às Urgências e Emergências (Portaria 1.600/2011), EXCET
- Na Rede de Urgência e Emergência, a atenção básica também tem papel de porta de entrada, d
- Entre as redes temáticas prioritárias pactuadas a partir de 2011, a rede voltada ao cuidad
- O Programa Nacional de Imunizações foi criado em 1973 e é responsável, entre outras atribu
- A faixa de temperatura em que, em geral, devem ser conservados os imunobiológicos na rede 
- Os Centros de Referência para Imunobiológicos Especiais (CRIE) destinam-se a:
- A ocorrência de um evento após a vacinação comprova, por si só, que a vacina foi a causa d
- A proteção indireta de pessoas não vacinadas quando grande parte da população está imune é
- A queda das coberturas vacinais na última década aumentou o risco de reintrodução de doenç
- Sobre os valores da Política Nacional de Promoção da Saúde revisada em 2014, assinale a al
- Os atributos derivados da atenção primária, segundo Starfield, são a orientação familiar, 
- No Pacto de Gestão de 2006, a regionalização foi tratada como eixo estruturante, com a cri
- A ambiência, diretriz da Política Nacional de Humanização, refere-se ao tratamento dado ao
- Pela PNAB 2017, a Atenção Básica deve ser a principal porta de entrada e o centro de comun
- A cobertura vacinal expressa a proporção da população-alvo vacinada, enquanto a homogeneid
- De acordo com as diretrizes das Redes de Atenção à Saúde (Portaria nº 4.279/2010), os três
- A PNAB 2017 estabeleceu que a cobertura de 100% da população por Agentes Comunitários de S
- A respeito da carga horária e composição das equipes previstas na PNAB 2017, assinale a al
- Na estrutura operacional das Redes de Atenção à Saúde, o prontuário clínico eletrônico, os
- Para a PNAB 2017, os termos Atenção Básica e Atenção Primária à Saúde são considerados equ
- Quando uma equipe de Saúde da Família adapta sua comunicação e suas práticas assistenciais
- De acordo com a Carta de Ottawa (1986), as ações de promoção da saúde são de responsabilid
- Na Política Nacional de Promoção da Saúde (Portaria nº 2.446/2014), a articulação de saber
- No modelo de determinantes sociais da saúde de Dahlgren e Whitehead, os laços de coesão gr
- Assinale a alternativa que apresenta as quatro etapas (momentos) que compõem a construção 
- Na Política Nacional de Humanização, o apoio matricial consiste na transferência definitiv
- A criação de espaços coletivos de decisão, como colegiados gestores, rodas de conversa e c
- No âmbito do Pacto pela Saúde (Portaria nº 399/2006), a repolitização da saúde, a luta pel
- Na Rede de Atenção às Urgências e Emergências (Portaria nº 1.600/2011), as Unidades de Pro
- No Componente Hospitalar da Rede de Atenção às Urgências e Emergências (RUE), foram instit
- O componente da RUE que consiste em um ambiente vinculado a um equipamento de saúde, com c
- Com o Programa Nacional de Imunizações, o Brasil conquistou a erradicação da varíola e a e
- No âmbito do Programa Nacional de Imunizações (PNI), o atraso em aceitar ou a recusa das v
- Na Rede de Frio do PNI, vacinas inativadas que contêm adjuvante de hidróxido de alumínio —
- No monitoramento das metas do Programa Nacional de Imunizações, o indicador que calcula o 
```

## Pedido 8: Concordância verbal e nominal (`portugues.concordancia`, 3371 tópicos de edital)

```
Matéria: portugues.concordancia (Concordância verbal e nominal)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos da concordância e identificação do sujeito": 20
- "2. Casos especiais de concordância verbal": 19
- "3. Construções impessoais, locuções verbais e o pronome se": 19
- "4. Concordância nominal: determinantes, adjetivos e invariáveis": 25
- "5. Concordância em períodos complexos e revisão estratégica": 17

Já existem (não repita):
- Em uma informação objetiva, assinale a construção em que o verbo haver obedece à concordân
- Na conferência de dados, escolha a frase correta com o verbo existir.
- Considerando sujeito posposto, assinale a redação correta.
- Após a revisão de dois itens coordenados, indique a frase correta.
- Quanto à palavra anexo na remessa de documentos, assinale a opção correta.
- Uma anotação informa tempo decorrido. Escolha a forma adequada.
- A respeito de necessidade de ajustes, escolha a locução verbal correta.
- O texto anuncia divulgação de várias versões; marque a passiva sintética correta.
- O enunciado utiliza precisar de com sujeito indeterminado. Qual redação está correta?
- Em uma oração com pronome relativo que, selecione a concordância correta.
- Em aviso com substantivo acompanhado de artigo, assinale a concordância adequada.
- Uma equipe relata que as participantes estavam um pouco cansadas. Marque a frase correta.
- Quando bastante equivale a muitos, qual frase está de acordo com a norma?
- Ao falar de mais de um elemento sem reciprocidade, escolha a concordância preferida.
- No pretérito composto com ter, assinale a forma adequada da oração.
- Em análise de concordância partitiva, qual afirmação reconhece as formas aceitas?
- Com o pronome relativo quem, assinale a avaliação gramatical precisa.
- Com sujeito composto posposto, indique a alternativa de avaliação correta.
- Uma expressão percentual vem antecedida por artigo plural. Qual forma se sustenta?
- O enunciado afirma que as duas ações, cada qual, podem revelar erros. Assinale a redação c
- Em uma oração com expressão de companhia intercalada, qual redação é adequada?
- Com sujeito oracional na principal e sujeito plural na subordinada, escolha a concordância
- Quanto à construção intensiva com possível, qual alternativa está correta?
- No caso de 1% seguido de substantivo plural sem artigo antecedente, qual julgamento é corr
- A frase deve manter passiva sintética de um sujeito plural e concordância nominal. Qual é 
- Na frase «Os anexos chegaram ao setor», a forma chegaram concorda com o núcleo plural anex
- A frase «A relação das ocorrências foram publicada» está correta porque ocorrências é a pa
- Em «Existem dúvidas sobre a conclusão», o verbo concorda com o sujeito posposto dúvidas.
- No sentido de existir, a forma «Haviam vagas suficientes» está de acordo com a norma-padrã
- Na indicação de tempo decorrido, está adequada a frase «Faz dois anos que a revisão começo
- A expressão «Fazem três meses que se iniciou a análise» é a forma exigida porque meses est
- Em «As planilhas seguem anexas», a palavra anexas concorda em gênero e número com planilha
- A frase «As leitoras estavam meias preocupadas» é correta, pois meio intensificador flexio
- Para indicar o horário, é adequada a expressão «Agora são duas horas».
- Na frase «Podem haver soluções mais simples», o plural do auxiliar é obrigatório por causa
- Em «Revisaram-se os anexos», o termo os anexos é sujeito paciente e justifica o plural ver
- Em «Precisa-se de revisões», o verbo deve flexionar no plural porque revisões vem depois d
- A sequência «A maioria das propostas foi analisada» admite também «A maioria das propostas
- Em «Mais de um participante enviou resposta», o verbo deveria obrigatoriamente estar no pl
- Em «Havia bastantes justificativas», a flexão de bastantes é correta porque o termo signif
- Na oração «Foi um dos textos que apresentou falhas», a relativa exige o singular por conco
- As estruturas «Fui eu quem redigiu a nota» e «Fui eu quem redigi a nota» podem ser aceitas
- O artigo plural em «Os 15% dos documentos» autoriza a oração «Os 15% dos documentos estava
- Na frase «A versão e o anexo foram revisados», o plural do verbo decorre do sujeito compos
- A redação «Os dados ficaram o mais objetivos possíveis» apresenta concordância obrigatória
```

## Pedido 9: Coesão textual (`portugues.coesao-textual`, 3231 tópicos de edital)

```
Matéria: portugues.coesao-textual (Coesão textual)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Referenciação e substituição": 20
- "2. Substituição lexical, elipse e encapsulamento": 20
- "3. Conectores e relações de sentido": 21
- "4. Sequenciação, repetição e coerência": 20
- "5. Reescrita coesa e estratégias de resolução": 19

Já existem (não repita):
- Leia o texto. A equipe de manutenção chegou ao bairro ao amanhecer. Os moradores indicaram
- Leia o texto. A equipe de manutenção chegou ao bairro ao amanhecer. Os moradores indicaram
- Leia o texto. A equipe de manutenção chegou ao bairro ao amanhecer. Os moradores indicaram
- Leia o texto. Antes da vistoria, o abrigo recebeu cobertores. A coordenadora conferiu cada
- Leia o texto. Antes da vistoria, o abrigo recebeu cobertores. A coordenadora conferiu cada
- Leia o texto. Antes da vistoria, o abrigo recebeu cobertores. A coordenadora conferiu cada
- Leia o texto. No distrito, a sirene soou durante a madrugada. A população recebeu orientaç
- Leia o texto. No distrito, a sirene soou durante a madrugada. A população recebeu orientaç
- Leia o texto. No distrito, a sirene soou durante a madrugada. A população recebeu orientaç
- Leia o texto. A ponte rural permaneceu interditada após a chuva. O motorista consultou o m
- Leia o texto. A ponte rural permaneceu interditada após a chuva. O motorista consultou o m
- Leia o texto. A ponte rural permaneceu interditada após a chuva. O motorista consultou o m
- Leia o texto. A escola municipal abriu as salas no fim da tarde. O diretor recebeu família
- Leia o texto. A escola municipal abriu as salas no fim da tarde. O diretor recebeu família
- Leia o texto. A escola municipal abriu as salas no fim da tarde. O diretor recebeu família
- Assinale a alternativa em que o pronome demonstrativo anuncia a informação que será especi
- Assinale a alternativa em que o pronome ‘ele’ tem referente ambíguo, sem contexto que reso
- Assinale a alternativa em que a substituição lexical usa um hiperônimo para retomar o refe
- Assinale a frase em que a elipse permite recuperar, sem ambiguidade, o verbo omitido.
- Assinale a alternativa em que ‘na medida em que’ estabelece relação causal, sem sentido pr
- Assinale a frase em que a oração concessiva está de acordo com a norma-padrão quanto ao mo
- Considere: ‘A ponte estava fechada, mas a equipe encontrou outra rota’. Assinale a reescri
- Assinale a alternativa que apresenta apenas uma relação de conformidade, marcada pelo cone
- Assinale a alternativa em que a repetição do termo é funcional e ajuda a evitar ambiguidad
- Assinale a alternativa cuja sequência de conectores organiza os fatos em ordem temporal co
- Assinale a alternativa em que há ligação coesiva entre as orações, mas a afirmação resulta
- Assinale a alternativa em que o conector ‘pois’ introduz uma explicação para a orientação 
- Considere: ‘A equipe aguardou porque o acesso permanecia bloqueado’. Assinale a reescrita 
- Assinale a frase em que uma expressão definida retoma e resume uma ação anterior, sem intr
- Assinale a frase em que o conector expressa consequência, e não causa, condição ou finalid
- No período ‘O engenheiro e o arquiteto assinaram o laudo; este revisou as plantas hidráuli
- Assinale a alternativa em que o emprego do pronome relativo ‘cujo’ (ou flexões) respeita i
- Assinale a alternativa em que o pronome relativo ‘onde’ foi empregado com precisão referen
- Em ‘Os servidores não abandonaram o posto de triagem, porquanto o gerador de reserva entro
- No período ‘Conquanto as chuvas tenham cessado no início da manhã, o alerta máximo foi man
- Para eliminar a ambiguidade do período ‘A diretora conversou com o chefe do setor sobre o 
- Em ‘O fiscal inspecionou as bombas de recalque e ativou-as antes da tempestade, mas não lh
- Considere o período: ‘O reservatório atingiu a cota de segurança, mas as comportas permane
- No período ‘O hospital de campanha comprou dois respiradores portáteis; o problema é que a
- Assinale a alternativa em que a palavra ‘como’ estabelece a mesma relação lógico-semântica
- Em ‘A equipe isolou a rua; essa providência reduziu o risco’, a expressão ‘essa providênci
- A expressão ‘na medida em que’ é sempre proporcional e pode substituir ‘à medida que’ em q
- Em ‘Conforme determinava o protocolo, a equipe manteve a saída desobstruída’, o conector e
- Se um texto contém pronomes e conectores bem empregados, sua coerência está automaticament
- A construção ‘Embora o acesso permaneça restrito, a equipe continuará a vistoria’ emprega 
- Na catáfora, a expressão referencial retoma obrigatoriamente um termo que já apareceu no p
- Em ‘O supervisor conferiu os computadores; a estagiária, as impressoras’, o verbo omitido 
- A repetição de uma palavra constitui sempre falha de coesão, mesmo quando evita que um pro
- Em ‘A equipe concluiu a vistoria; depois, liberou a entrada’, o advérbio estabelece uma se
- O pronome relativo ‘onde’ pode retomar sem restrição qualquer antecedente abstrato, como ‘
- ‘Ainda que’ pode substituir ‘embora’ em uma oração concessiva, desde que a relação de cont
- Em ‘Portanto, a equipe manteve o acesso bloqueado’, o conector destacado apresenta a causa
- Na sequência ‘o abrigo e o posto foram inspecionados; este recebeu o gerador’, ‘este’ reto
- Se duas orações estão ligadas por um conector, a compatibilidade entre suas ideias deixa d
- Trocar ‘mas’ por ‘contudo’ pode preservar a adversidade quando o contraste entre as oraçõe
- Em ‘O caminhão-pipa abasteceu a cisterna da escola; o veículo seguiu depois para o hospita
- De acordo com a norma-padrão e os manuais de redação oficial, a frase ‘Os fiscais vistoria
- No período ‘À medida que o nível da represa baixava, as bombas auxiliares eram desligadas’
- Em ‘O laudo estrutural revelou fissuras graves nos pilares; o prédio foi, pois, interditad
- No trecho ‘A coordenadora conversou com o irmão da médica, a qual chefia o pronto-socorro’
- A substituição de ‘Embora o vento estivesse forte’ por ‘Apesar de o vento estar forte’ pre
- No período ‘Posto que os recursos financeiros foram liberados ontem, as obras de contenção
- A substituição de ‘Conquanto o solo estivesse encharcado, o trator concluiu a abertura da 
- Em ‘A prefeitura contratou médicos, enfermeiros e motoristas; estes atuarão nas ambulância
- A reescrita de ‘A pista estava coberta de óleo, mas o caminhão conseguiu frear a tempo’ co
```

## Pedido 10: Regência verbal e nominal (`portugues.regencia-verbal-nominal`, 3120 tópicos de edital)

```
Matéria: portugues.regencia-verbal-nominal (Regência verbal e nominal)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos da regência e transitividade": 21
- "2. Regência verbal: sentidos e preposições de alta frequência": 21
- "3. Regência nominal: substantivos, adjetivos e locuções": 24
- "4. Pronomes relativos, crase e reescrita": 17
- "5. Análise avançada e armadilhas de correção": 17

Já existem (não repita):
- Uma pessoa presenciou uma apresentação e, em seguida, aspirou o aroma de flores. Qual fras
- Na frase “Ela aspirou ___ perfume e aspirava ___ reconhecimento”, quais formas completam o
- Uma comparação entre dois hábitos deve conservar a estrutura formal de “preferir X a Y”. A
- A pessoa explicou que havia esquecido determinado compromisso. Qual redação mantém a forma
- Uma pessoa quitou uma dívida perante uma fornecedora. Qual frase distribui corretamente o 
- Depois da orientação, o grupo decidiu acatar as regras determinadas. Complete: “O grupo ob
- Um parecer foi considerado compatível com determinada proposta. Que expressão mantém a reg
- O texto original dizia “Preciso dos dados”. Em “Estes são os dados ___ preciso”, qual comp
- Uma pessoa se referiu a determinada proposta feminina específica. Qual enunciado registra 
- O aumento dos gastos decorre de um atraso. Na formulação tradicional preferida para “acarr
- Ao avaliar uma mudança, alguém registrou opinião favorável. Complete “A equipe mostrou-se 
- A frase inicial é “Ele deseja uma melhoria”. Ao trocar “desejar” por “aspirar” no sentido 
- Transforme “Obedecemos às orientações” em oração relativa, preservando a preposição do ver
- Um documento não contém taxas e, por isso, é descrito como isento. Assinale o complemento 
- A pessoa assistiu a uma apresentação, mas não gostou dela. Assinale a formulação formal qu
- Na acepção de realizar uma verificação, escolha a construção formalmente adequada com o ve
- Considere o adjetivo “preferível” em comparação de dois procedimentos. Qual redação empreg
- Em um relato formal, pretende-se substituir “esqueceu o fato” pela construção pronominal e
- A pessoa explicou que se referiu ao texto de determinada autora. Como construir o relativo
- Uma pessoa deslocou-se para determinada cidade. Ao unir “Visitei a cidade” com “Fui à cida
- Um trecho enuncia “A revisão foi benéfica ___ estudantes”. A escolha exige a preposição do
- Um relatório foi descrito como fiel a determinada versão feminina. Assinale a relação nomi
- A solução era compatível com o sistema. Na reescrita com o substantivo “compatibilidade”, 
- Uma pessoa menciona determinada tese e outra faz referência a essa tese. Qual frase usa co
- O enunciado “Ela se opôs ___ proposta” exige o complemento do verbo pronominal e o artigo 
- Na frase “A leitora confiou na avaliação”, a preposição em, contraída com o artigo a, intr
- Em “A leitora gostou o artigo”, a supressão de de mantém a regência formal do verbo gostar
- Na frase “A autora aspirou o aroma”, aspirar significa inalar e recebe objeto direto sem p
- Em “A equipe obedeceu as instruções”, a ausência de à(s) está de acordo com a regência tra
- Em “A solução é compatível com a regra”, com introduz complemento regido pelo adjetivo com
- A construção “Esqueceu-se do encontro” apresenta o mesmo regime formal de “Esqueceu o enco
- No enunciado “A questão a que se referiu foi esclarecida”, a preposição a decorre do verbo
- Em “O livro que gostei estava esgotado”, o pronome que dispensa de porque retoma um substa
- O segmento “favorável à atualização” contém crase porque favorável exige a e atualização a
- Na comparação “Prefiro o original do que a cópia”, do que é a preposição requerida pelo ve
- Em “Informou o grupo da decisão”, o grupo é o destinatário da informação; o conteúdo é int
- Em “O parecer é isento à taxa”, o adjetivo isento exige a mesma preposição de fiel em “fie
- Em “Assisti à aula e gostei dela”, a primeira oração exige a preposição a e a segunda exig
- A frase “A autora a cujo texto me referi” deve receber acento grave em “à cujo”, porque te
- Em “A regra à qual obedeci”, à qual resulta da preposição a, exigida por obedecer, com o a
- Na frase “Chegou ao local e começou o estudo”, a preposição a de ao indica objeto indireto
- Ao trocar “A solução é compatível com o sistema” por “A solução é favorável ao sistema”, a
- Na frase “A cidade aonde fomos era distante”, aonde exprime direção e se relaciona com a p
- O verbo “assistir” conserva necessariamente a mesma regência em “assistiu à apresentação” 
- Em “A avaliação a que me opus”, o relativo preserva a preposição do verbo opor-se e, por i
```

## Pedido 11: Lógica proposicional e tabela-verdade (`matematica.logica-proposicional`, 2927 tópicos de edital)

```
Matéria: matematica.logica-proposicional (Lógica proposicional e tabela-verdade)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Proposições, sentenças e conectivos básicos": 27
- "2. Condicional, bicondicional e construção de tabelas": 22
- "3. Valor lógico de fórmulas e classificação": 23
- "4. Como o tema costuma ser cobrado": 14
- "5. Método da valoração reversa e problemas de premissas fixadas": 14

Já existem (não repita):
- Qual enunciado é uma proposição?
- Considere p=F e q=V. Qual é o valor de ¬p ∧ q?
- Na tabela abaixo, “ou” é inclusivo. Qual resultado deve ocupar a célula marcada? | p | q |
- Um edital registra: “A equipe entrega o relatório e o chefe o valida”. Se p representa a p
- Uma senha é aceita quando contém exatamente uma entre letra e número. Qual conectivo model
- Uma fórmula contém as variáveis distintas p, q e r. Quantas linhas possui sua tabela-verda
- A regra “se a agência abre, então o caixa funciona” é p→q. Em qual combinação a condiciona
- Qual descrição corresponde à bicondicional p↔q?
- Na linha p=F, q=F, r=V, qual o valor de (p ∨ q) → r?
- A tabela parcial corresponde a p↔q. Qual linha está preenchida incorretamente? | p | q | p
- Uma fórmula contém p três vezes, q duas vezes e r uma vez. Quantas linhas são necessárias 
- Com p=V e q=F, avalie (p ∧ ¬q) → q.
- Qual fórmula é verdadeira em todas as valorações de p?
- Classifique p∧¬p para todas as valorações de p.
- Uma tabela completa usa as quatro letras abaixo, cada qual com dois valores possíveis. Qua
- A tabela mostra a coluna final de (p∨q)∧¬p. Como se classifica a fórmula? | p | q | (p∨q)∧
- Avalie p→(q∨r) quando p=V, q=F e r=F.
- Na linha p=F, q=V, r=F, determine (p↔q)∨r.
- Um órgão define p como “o estoque supera 20 unidades” e q como “a compra foi autorizada”. 
- Com p=V, q=F e r=V, qual o valor de ¬(p∧q)↔r?
- Uma tabela de três letras distintas lista as linhas abaixo, todas com resultado V. Qual co
- Uma expressão contém as letras p, q, r e p novamente. Um candidato afirma que são quatro v
- Para p=F, q=V e r=F, calcule (p→q)∧(q↔¬r).
- Qual linha torna falsa a fórmula (p∧q)→(p∨r)?
- A frase “O plantão terá reforço ou haverá atendimento remoto, mas não ambos” é representad
- Uma fórmula com duas letras distintas tem quatro linhas. Se a coluna final for V, F, V, F,
- Em p→(q∧r), são dados p=V, q=V e r=F. Qual sequência de avaliação está correta?
- Uma sentença “a escala foi publicada” recebe V em determinado dia. Qual afirmação sobre se
- Qual cenário torna verdadeira a bicondicional “o lote é liberado se e somente se a conferê
- Considere F=(p∨q)∧¬q. Qual é a coluna final na tabela abaixo? | p | q | F | |---|---|---| 
- Analise os cinco enunciados a seguir: I. “O número 17 é divisível por 3.” II. “Protocolar 
- Em um relatório de auditoria consta a frase: “O contrato foi assinado, contudo o empenho n
- A diretriz de um edital estabelece: “O candidato toma posse no cargo somente se apresenta 
- Considere a afirmação: “O alvará será renovado, desde que a vistoria técnica seja aprovada
- Sem o uso de parênteses explícitos, qual é a colocação correta de parênteses na fórmula ¬p
- Considere as proposições simples distintas p, q e r. Na tabela-verdade completa da fórmula
- Sabendo que a proposição composta (p → q) ∨ (r ∧ ¬s) tem valor lógico FALSO, pode-se afirm
- Considere verdadeiras as seguintes premissas sobre três servidores (Lucas, Marina e Nádia)
- Sejam P, Q e R as seguintes fórmulas proposicionais: • P: (p → q) ↔ (¬q → ¬p) • Q: (p ∧ q)
- Em uma tabela-verdade completa para quatro proposições simples distintas (p, q, r, s), qua
- Avalie a afirmação: “A condicional p→q é falsa em exatamente uma das quatro linhas de sua 
- Avalie a afirmação: “A sentença ‘Você está de plantão?’ é proposição porque pode receber r
- Avalie a afirmação: “A disjunção inclusiva p∨q é verdadeira quando p e q são ambas verdade
- Avalie a afirmação: “Com três proposições simples distintas, uma tabela-verdade completa p
- Avalie a afirmação: “Uma fórmula cuja coluna final contém somente F é uma contradição.”
- Considere p=F e q=V. Avalie: “p↔q é verdadeira porque pelo menos uma das proposições é ver
- Avalie a afirmação: “Na fórmula (p∧q)∨r, com p=V, q=F e r=V, o valor final é V.”
- Avalie a afirmação: “Toda frase declarativa é proposição, mesmo que contenha variável sem 
- Avalie a afirmação: “Se p é falsa, a condicional p→q é falsa qualquer que seja q.”
- Avalie a afirmação: “Uma fórmula é contingência se sua coluna final contém pelo menos um V
- Avalie a afirmação: “Em uma tabela de p, q e p novamente, as três ocorrências devem recebe
- Considere p=F, q=V e r=F. Avalie: “(p∨q)∧¬r é verdadeira.”
- Avalie a afirmação: “A bicondicional p↔q é falsa quando p e q têm valores distintos.”
- Uma coluna final apresenta F em todas as 16 linhas de uma tabela completa com quatro letra
- Com p=V, q=F e r=V, avalie: “(p∧q)→r é falsa.”
- Avalie a afirmação: “Pelo Princípio do Terceiro Excluído, toda proposição lógica ou é verd
- Avalie a afirmação: “A frase ‘O servidor não assinou o ponto nem justificou a ausência’ po
- Avalie a afirmação: “A sentença autorreferente ‘Esta afirmação é falsa’ classifica-se como
- Avalie a afirmação: “Na condicional p → q, a proposição p é condição necessária para q, en
- Avalie a afirmação: “A proposição composta ‘Se 9 é um número primo, então 4 é um número ím
- Avalie a afirmação: “A negação de uma tautologia é sempre uma contradição, e a negação de 
- Avalie a afirmação: “Na tabela-verdade completa da proposição p → (q ∨ r), onde p, q e r s
- Avalie a afirmação: “A fórmula [(p → q) ∧ q] → p é uma tautologia.”
- Avalie a afirmação: “Sabendo que as proposições p → q e p ∨ q são ambas verdadeiras e que 
- Avalie a afirmação: “A fórmula (p ⊻ q) ↔ ¬(p ↔ q) é uma tautologia.”
```

## Pedido 12: Crase (`portugues.crase`, 2905 tópicos de edital)

```
Matéria: portugues.crase (Crase)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Conceito, regência e testes práticos": 20
- "2. Casos obrigatórios e proibidos": 20
- "3. Facultativos, nomes, lugares, horas, casa e terra": 20
- "4. Reescrita e regência": 20
- "5. Paralelismo sintático, pronomes relativos e casos avançados": 20

Já existem (não repita):
- Assinale a alternativa correta quanto ao emprego do acento grave.
- Assinale a alternativa INCORRETA segundo a norma-padrão.
- Em qual alternativa a crase é facultativa, mantidos o sentido e a regência?
- Assinale a justificativa correta para o uso do acento grave na frase: Os funcionários obed
- Assinale a frase em que o emprego da crase está correto.
- Assinale a alternativa correta sobre horas e expressões temporais.
- Assinale a alternativa INCORRETA quanto à regência e à crase.
- Assinale a frase correta segundo o teste tradicional aplicado aos topônimos.
- Assinale a alternativa correta quanto à relação entre artigo e preposição.
- Em qual frase a crase é obrigatória?
- Assinale a alternativa em que a forma está de acordo com a norma-padrão.
- Qual reescrita mantém a correção da frase O fiscal mencionou a irregularidade no relatório
- Assinale a alternativa correta sobre o emprego de crase diante de demonstrativos.
- Assinale a alternativa INCORRETA de acordo com a norma-padrão.
- Assinale a alternativa correta quanto ao uso da crase em construções com casa e terra.
- Assinale a alternativa em que se aplica corretamente o uso facultativo da crase.
- Assinale a alternativa correta quanto a intervalos e expressões de início.
- Qual frase contém crase facultativa, sem alteração de sentido?
- Assinale a reescrita correta, de acordo com a regência do verbo assistir no sentido de pre
- Assinale a alternativa INCORRETA sobre o emprego da crase em locuções e nomes femininos.
- Assinale a alternativa em que a crase está corretamente empregada em expressão de modo.
- Assinale a alternativa correta de acordo com a regência tradicional cobrada em provas.
- Assinale a frase em que o acento grave está corretamente empregado.
- Qual reescrita mantém a correção e o sentido de “Os atendentes cumpriram a instrução da ge
- Assinale a alternativa que apresenta uso obrigatório de crase.
- Assinale a alternativa cuja crase é facultativa.
- Assinale a alternativa correta a respeito de reescrita e regência.
- Assinale a alternativa correta quanto ao emprego de crase em construções de tempo e distân
- Qual construção substitui “citou a regra” por uma locução equivalente e está corretamente 
- Assinale a alternativa correta quanto à regência e ao emprego do acento grave.
- Na frase “À noite, a equipe revisou o plano de trabalho”, a locução inicial recebe crase.
- Está correta a grafia em “O motorista entregou as chaves à esta funcionária”.
- Na construção “Os turistas chegaram àquela embarcação”, o acento grave decorre da preposiç
- A forma “de 7h às 12h” é a única correta para indicar um intervalo sem artigo no início.
- O verbo obedecer rege preposição, por isso a frase “A equipe obedeceu à ordem de isolament
- As duas grafias são possíveis em “A fila avançou até a entrada” e “A fila avançou até à en
- Toda palavra feminina depois do verbo “entregar” exige acento grave, como em “O chefe entr
- Em “O cozinheiro preparou o peixe à capixaba”, a expressão à moda está subentendida e just
- Na frase “O aviso foi emitido à partir das 18h”, a locução inicial exige acento grave.
- A frase “A equipe se referiu àquilo que os moradores relataram” está correta porque referi
- A frase “Os observadores mantiveram-se à distância de cem metros” está incorreta, pois dis
- Em “O diretor se opôs à medida à qual a equipe recorrera”, o relativo recebe crase porque 
- Sem especificação, no sentido de lar próprio, a expressão “Os funcionários voltaram à casa
- Na frase “O documento foi entregue à senhora que chefia o setor”, o acento grave é possíve
- A frase “A ambulância chegou à um posto de saúde” está correta, porque a palavra posto é p
- Em uma minuta de comunicação interna, foram suprimidos os acentos graves: “O setor submete
- Leia: “A chefia respondeu __ servidoras que haviam solicitado orientação; depois, responde
- Em despacho, foram registradas as construções: “o servidor se dirigiu __quela unidade”, “n
- Considere as relações de regência: “A medida __ qual o relatório se opõe foi mantida; a no
- A escala de atendimento foi redigida assim: “A sessão começará __ 14 horas; o plantão func
- Em um relatório de atividades, devem ser empregadas três locuções: “o documento foi protoc
- Em comunicado sobre deslocamentos institucionais, registrou-se: “A equipe viajou __ Bahia,
- No registro de deslocamentos, lê-se: “A servidora voltou __ casa da família, retornou __ c
- Em relatório, a equipe registrou: “O servidor observou o local __ distância; depois, viu a
- Em parecer, uma expressão foi empregada no sentido de “à moda de”; em seguida, registraram
- Na frase “A decisão proferida por esta turma recursal é idêntica à que foi publicada na se
- Em “A auditoria solicitou esclarecimentos à empresa à cuja diretora o contrato fora submet
- Na oração “Até à coordenadora geral elogiou a nova sistemática de controle”, o acento grav
- Em “Os fiscais ficaram frente a frente com os responsáveis pela obra”, a ausência do acent
- Na frase “A falta de manutenção preventiva implicará à paralisação imediata das máquinas”,
- Em “A procuradoria prefere a conciliação extrajudicial à abertura de novo litígio”, o empr
- Na frase “O programa de capacitação é destinado a servidoras em estágio probatório”, a ins
- Em “A sessão extraordinária foi transferida para as 16h, mas os debates se estenderam desd
- Em “O parecer fez alusão ao relatório contábil e a auditoria externa”, o trecho final resp
- A substituição de “saiu a francesa” por “saiu à francesa” mantém a correção gramatical, ma
- Assinale a alternativa em que o paralelismo sintático e o emprego dos pronomes relativos e
- Assinale a alternativa em que a alteração de singular para plural nos pronomes possessivos
- Em qual alternativa a retirada do acento grave mantém a correção gramatical do período, ma
```

## Pedido 13: Reescrita de frases e parágrafos (`portugues.reescrita`, 2798 tópicos de edital)

```
Matéria: portugues.reescrita (Reescrita de frases e parágrafos)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Equivalência de sentido e substituição lexical contextual": 27
- "2. Conectores, tempo e relações entre orações": 20
- "3. Reorganização sintática, voz, pontuação e paralelismo": 22
- "4. Discursos direto, indireto e indireto livre": 14
- "5. Retextualização, registro, gênero e revisão avançada": 17

Já existem (não repita):
- No relatório de uma visita técnica lê-se: 'Apesar da chuva, a equipe concluiu a visita.' Q
- Uma anotação registra: 'O pedido chegou fora do prazo; contudo, foi examinado.' Qual alter
- Considere: 'A equipe revisou os dados na terça-feira.' Qual passagem à voz passiva analíti
- No comunicado consta: 'Como faltavam informações, a análise foi suspensa.' Qual alternativ
- Em um curso com inscritos que estavam e que não estavam de plantão, afirmou-se: 'Os técnic
- Um aviso informa: 'Houve falha no sistema; por isso, a reunião foi adiada.' Qual reescrita
- Em uma lista de participantes, escreveu-se: 'Apenas Marina apresentou a proposta.' Qual al
- O registro de presença afirma: 'Nem todos os convidados compareceram.' Qual alternativa ap
- O texto registra: 'A responsável talvez apresente os documentos amanhã.' Qual versão conse
- Em relato feito na semana seguinte, está a fala de Bia: 'Voltarei amanhã', disse ela na se
- Leia: 'Após dois anos sem publicar, Lia voltou a divulgar seus estudos.' Qual substituição
- Uma ata informa: 'Não é verdade que todos os arquivos foram conferidos.' Qual reescrita co
- A redação diz: 'Por ter recebido o alerta, a pesquisadora retornou ao laboratório.' Qual d
- O registro informa: 'A comissão aprovou o plano na reunião de terça-feira.' Qual nominaliz
- Um parecer declara: 'É possível que o serviço não funcione amanhã.' Qual alternativa manté
- A proposta contém: 'O projeto pretende reduzir desperdícios e ampliar o acesso ao serviço.
- Uma orientação estabelece: 'Caso haja autorização, divulgaremos o resultado.' Qual reescri
- No relato consta: 'Embora tivesse estudado, Sara não concluiu a atividade.' Qual reescrita
- Considere: 'Somente depois de concluída a auditoria, a diretora divulgou os dados.' Qual a
- Em uma narrativa, lê-se: 'Helena fechou o envelope. Quanto tempo mais esperaria? Ninguém l
- Para converter a mensagem 'Oi, manda o arquivo até as 16h, por favor' em uma solicitação f
- O texto registra: 'Depois de examinar as propostas, a comissão anunciou o resultado.' Qual
- Uma narrativa afirma: 'Quando a equipe chegou, o técnico já havia desligado a máquina.' Qu
- Em conversa de equipe, alguém disse: 'A gente talvez consiga concluir hoje.' Para um relat
- Na sexta-feira, um gestor declarou: 'Não pretendo adiar a divulgação, a menos que faltem d
- Em uma nota técnica, a frase 'O parecer fundamenta a decisão' pode ser reescrita, em conte
- Em uma lista de presença, 'Alguns estudantes compareceram' pode ser substituído por 'Todos
- Na justificativa 'A sessão terminou porque houve interrupção de energia', a locução 'em ra
- A transformação 'Os analistas verificaram o arquivo ontem' em 'O arquivo é verificado pelo
- No relato posterior 'Aline disse que retornaria no dia seguinte', uma forma direta compatí
- Ao reescrever 'Chegaram ontem os relatórios' como 'Chegou ontem os relatórios', conserva-s
- Em uma turma composta por concluintes e não concluintes, escrever 'Os alunos que terminara
- A frase 'Vendem-se equipamentos usados' pode ser reescrita como 'Vende-se equipamentos usa
- No trecho 'Mário recolheu as chaves. Que situação absurda! Como poderia sair agora?', o co
- No enunciado 'O aviso já havia sido enviado quando a reunião começou', a troca de 'já havi
- Uma mensagem informal como 'Me envie a resposta até sexta-feira, por favor' pode ser retex
- No período 'Somente se a autorização chegar, a visita ocorrerá', retirar o advérbio 'somen
- Em 'Rosa atravessou o corredor. Por que ninguém compreendia seu receio? Seria tão difícil 
- Uma declaração 'Não é possível que o grupo deixe de receber a mensagem' mantém o sentido c
- No registro 'Ao receber o comunicado, a pesquisadora telefonou', o desenvolvimento 'Quando
- O texto 'A pesquisadora encontrou a guia com uma luneta' é ambíguo quanto a quem portava a
- Na conversão de uma fala informal em comunicado escrito, podem ser eliminadas hesitações e
- Na frase 'A comissão avaliou as sugestões antes de publicar a decisão', a nominalização 'A
- Em um aviso, 'Mais de metade dos inscritos confirmou presença' pode ser reescrito como 'A 
- Em relatório formal, substituir 'Houve diversas falhas no registro' por 'Houveram diversas
```

## Pedido 14: Segurança (`informatica.seguranca`, 2623 tópicos de edital)

```
Matéria: informatica.seguranca (Segurança)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos de segurança, princípios, risco e incidentes": 15
- "2. Códigos maliciosos: famílias, propagação e consequências": 28
- "3. Engenharia social, phishing, pharming e golpes digitais": 17
- "4. Ferramentas de proteção, autenticação e controle de acesso": 24
- "5. Backups, continuidade e resposta básica aos incidentes": 16

Já existem (não repita):
- Um relatório sigiloso permanece sem alterações, mas é enviado por engano a destinatário se
- Uma planilha de pagamentos continua disponível aos usuários autorizados, porém um valor é 
- Uma plataforma mantém seus documentos corretos e restritos, mas fica fora do ar durante o 
- Em estação desatualizada, um criminoso tenta explorar falha conhecida para acessar dados r
- Após alcançar um computador da rede, certo código malicioso cria cópias de si mesmo em out
- Um documento com macro maliciosa é executado e o código insere cópias em outros arquivos c
- Um instalador anunciado como leitor gratuito de processos exibe documentos normalmente, ma
- Arquivos compartilhados tornaram-se ilegíveis após execução de programa que exige pagament
- Uma extensão maliciosa registra os sites visitados e captura credenciais digitadas sem inf
- Uma mensagem que aparenta vir de serviço institucional informa suspensão da conta e encami
- Uma mensagem fraudulenta menciona pelo nome uma pessoa, sua equipe e processo recém-aberto
- Uma pessoa digita corretamente o endereço habitual do portal, mas o computador usa configu
- Uma pessoa recebe SMS com link falso para suposta regularização de cadastro e outra é cham
- O sistema precisa reduzir conexões indevidas a portas de uma estação, conforme regras para
- Para acesso ao serviço, um usuário digita senha e depois código PIN que decorou. Qual aval
- Uma funcionária tem autenticação correta no sistema de documentos, mas recebe permissão ap
- Uma estação possui antivírus atualizado, mas seu sistema operacional permanece com falha p
- Há backup completo no domingo e dois incrementais, de segunda e terça. Na terça foi altera
- Após backup completo no domingo, são feitos diferenciais de segunda e terça. O diferencial
- Um serviço replica imediatamente cada alteração de uma pasta para a nuvem. Depois que rans
- Uma organização mantém três cópias de seus dados, utilizando disco e armazenamento em nuve
- O plano de continuidade prevê perder no máximo quatro horas de atualizações de dados e res
- Uma pessoa aprova várias solicitações inesperadas de aplicativo autenticador, até permitir
- Durante análise introdutória de alertas, são observados comandos suspeitos executados por 
- Um serviço recebe requisições maliciosas simultâneas de centenas de dispositivos compromet
- Um usuário recebe mensagem com arquivo suspeito e o antivírus alerta para código malicioso
- Um vírus tradicional de arquivo e um worm possuem o mesmo requisito de replicação: para pr
- Uma mensagem solicita pagamento para evitar bloqueio de conta e contém link para página fa
- Como o firewall filtra conexões de entrada e saída, sua ativação também substitui integral
- Uma cópia completa criada no domingo e um backup diferencial feito na terça-feira permitem
- O usuário que armazena arquivos apenas em pasta sincronizada na nuvem possui, por definiçã
- Uma mensagem que se apresenta como atendimento técnico pede código temporário por ligação 
- Uma conta utiliza senha e PIN diferentes. Como existem duas etapas de digitação, trata-se 
- Uma máquina comprometida integra rede controlada remotamente para gerar tráfego contra por
- Um sítio fraudulento apresenta cadeado HTTPS válido para seu próprio domínio. Por isso, a 
- Após ocorrência de ransomware, a equipe verifica a existência de cópias externas protegida
- O NIST Cybersecurity Framework 2.0 organiza a gestão de riscos de segurança em apenas cinc
- Um serviço permite autenticação com senha, mas aplica permissões limitadas para impedir qu
- O armazenamento em RAID espelhado substitui todo o plano de backup, pois arquivos apagados
- Um ataque de engenharia social obtém uma senha e, dias depois, usa credencial roubada para
- Um programa modifica sua aparência ou assinatura entre variantes para dificultar detecção 
- No planejamento de recuperação, a meta RPO de uma hora e a meta RTO de seis horas tratam d
- Uma conta autentica-se por passkey corretamente vinculada ao domínio do serviço. Como o mé
- Uma pessoa identifica provável ataque de ransomware em computador de trabalho. A comunicaç
- Um programa malicioso abre uma via oculta para que um invasor retorne ao equipamento, mas 
```

## Pedido 15: Epidemiologia e vigilância em saúde (`saude-publica.epidemiologia-vigilancia`, 2567 tópicos de edital)

```
Matéria: saude-publica.epidemiologia-vigilancia (Epidemiologia e vigilância em saúde)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos da epidemiologia e indicadores de frequência e gravidade": 20
- "2. Métodos epidemiológicos, comparação de riscos e perfil de agravos": 20
- "3. Vigilância epidemiológica: notificação, informação e investigação": 20
- "4. Vigilância sanitária, SNVS, Anvisa e poder de polícia sanitária": 21
- "5. PNI, COVID-19, biossegurança e aplicação integrada da vigilância": 19

Já existem (não repita):
- Em um grupo de 1.000 pessoas sem determinada infecção no início do ano, 50 desenvolveram a
- No primeiro dia de junho, uma cidade de 20.000 habitantes tinha 1.200 pessoas vivendo com 
- Em certo surto, 100 pessoas foram classificadas como casos de uma doença, e 4 delas morrer
- Uma equipe de saúde deseja detectar rapidamente mudanças inesperadas na ocorrência de uma 
- Em inspeção, foram identificados problemas no armazenamento e na comercialização de medica
- Uma unidade identifica suspeita de agravo incluído na lista de notificação compulsória. Nã
- Uma campanha registrou 900 doses finais válidas em crianças de uma população-alvo estimada
- Uma pessoa apresenta manifestação clínica indesejada depois da imunização, sem investigaçã
- Em coorte com seguimento adequado, 24 de 200 pessoas expostas adoeceram, contra 9 de 150 n
- Uma investigação seleciona primeiro pessoas com uma doença e um grupo de controle sem a do
- Um município registra regularmente número esperado de casos de um agravo, mas em certa sem
- Uma localidade teve 12 óbitos de crianças antes de completar um ano e 2.400 nascidos vivos
- Durante análise de produtos e serviços submetidos ao controle sanitário nacional, pergunta
- Para estudar mudanças na mortalidade por doenças cardiovasculares, uma equipe necessita pr
- Uma análise populacional de pessoas previamente infectadas por SARS-CoV-2 utiliza a defini
- Em avaliação de saúde do trabalhador, uma equipe examina exposição ocupacional, padrões de
- Num seguimento de trabalhadores inicialmente livres da doença, ocorreram 12 casos novos du
- Num seguimento comparativo, 20% dos indivíduos expostos desenvolveram agravo, contra 8% do
- Numa população estável, a incidência de determinada doença crônica permaneceu parecida, ma
- Uma pesquisa ecológica encontra mais casos de doença em municípios com maior média de expo
- Após refeição coletiva, 30 de 100 pessoas que ingeriram determinado alimento adoeceram, co
- Uma instituição propõe divulgar nomes completos de pessoas notificadas por doença para fac
- Em um surto de intoxicação alimentar, uma equipe identifica casos e outra fiscaliza o loca
- O painel de um programa informa cobertura vacinal administrativa de 104% para determinada 
- Em atividade assistencial com geração de aerossóis infecciosos, uma instituição pretende c
- Uma avaliação de saúde feita em uma data identifica 300 pessoas com doença crônica entre 6
- Para medir letalidade de doença com cem casos conhecidos e dez mortes atribuídas ao agravo
- Uma investigação acompanha pessoas inicialmente livres de doença, classificadas conforme e
- A notificação compulsória depende necessariamente de confirmação laboratorial prévia de ca
- O controle das condições de fabricação, armazenamento e comercialização de medicamentos es
- Cobertura vacinal de 90% indica que a vacina apresenta eficácia biológica de 90% contra qu
- Precauções padrão, como higiene das mãos e uso de proteção conforme a exposição, são indic
- Como o Sinasc registra nascidos vivos, ele é também o sistema nacional destinado prioritar
- Endemia descreve ocorrência habitual ou persistente de uma doença numa área, enquanto epid
- O risco relativo igual a dois, observado em estudo de coorte, demonstra por si só que a ex
- Os dados de notificação compulsória têm proteção de sigilo, observadas as exceções legais,
- A Lei nº 9.782/1999 concentrou toda fiscalização sanitária municipal diretamente na Anvisa
- Cobertura vacinal administrativa pode ultrapassar 100% por problemas de estimativa do públ
- Para a definição brasileira divulgada pelo Ministério da Saúde, condições pós-COVID soment
- Um evento indesejado observado após vacinação pode ser registrado como ESAVI mesmo antes d
- Ao receber notificação de surto com evidências relevantes de risco, a autoridade sanitária
- Para um indicador de mortalidade infantil expresso por mil nascidos vivos, o denominador u
- Como o Sinan reúne dados de agravos de notificação, seu banco deve conter necessariamente 
- Se o risco assistencial envolve aerossóis infecciosos, as precauções padrão continuam apli
- A saúde do trabalhador, definida no art. 6º, § 3º, da Lei nº 8.080/1990, depende exclusiva
```

## Pedido 16: Licitação na Lei 14.133/2021: conceito, princípios e modalidades (`direito-administrativo.licitacao-principios-modalidades`, 2565 tópicos de edital)

```
Matéria: direito-administrativo.licitacao-principios-modalidades (Licitação na Lei 14.133/2021: conceito, princípios e modalidades)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Conceito, objetivos e princípios da licitação": 26
- "2. Agentes, fases, publicidade e modos de disputa": 15
- "3. Cinco modalidades e seis critérios de julgamento": 30
- "4. Procedimentos auxiliares e registro de preços": 15
- "5. Aplicação integrada: pregão eletrônico, escolha da modalidade e atualização normativa": 14

Já existem (não repita):
- Uma unidade pública pretende comprar equipamentos para uma atividade regular. Qual enuncia
- Após publicar o edital, a Administração decide atribuir pontos adicionais a uma caracterís
- Qual relação apresenta exclusivamente modalidades previstas no art. 28 da Lei nº 14.133/20
- Pretende-se adquirir bens de especificações usuais de mercado, com qualidade e desempenho 
- Uma instituição pretende alienar bens móveis inservíveis mediante competição de ofertas. Q
- Uma compra pública utilizará técnica de registro de preços para futuras aquisições. Como e
- Uma ata de registro de preços foi formalizada em condições válidas. Qual efeito o art. 83 
- Na ordem ordinária do art. 17 da Lei nº 14.133/2021, qual fase ocorre após julgamento e an
- Qual designação é atribuída ao agente responsável pela condução do pregão, conforme art. 8
- Em edital para serviço intelectual com propostas técnicas e preços ponderados, a Administr
- Uma licitação usará julgamento por menor preço e o edital pretende estabelecer somente pro
- No diálogo competitivo, o edital de chamamento fixará prazo para interessados manifestarem
- Um edital prevê julgamento por técnica e preço, atribuindo 80% da nota total à proposta té
- Uma administração promoverá intenção de registro de preços para permitir adesão de outros 
- Uma ata de registro de preços federal está próxima de completar o primeiro ano. Qual condi
- Um item registra 300 unidades para gerenciador e participantes. Sem exceções legais, qual 
- Uma mesma pessoa é designada para desempenhar simultaneamente funções de decisão e control
- Uma concorrência utilizará julgamento por técnica e preço e a minuta do edital prevê modo 
- O objeto pretendido é serviço comum de engenharia cujos padrões de qualidade podem ser def
- Concluído o diálogo competitivo para solução técnica complexa, o edital da fase competitiv
- Uma ata registrou 250 unidades de item para gerenciador e participantes. Quatro interessad
- Uma entidade da Administração Pública federal pretende aderir, como não participante, a at
- Uma ata federal foi divulgada no PNCP em uma terça-feira útil. Considerando o Decreto nº 1
- Uma entidade não participante obtém autorização para aderir a ata federal gerenciada nos t
- Ao preparar contratação complexa, uma unidade sugere somar as modalidades 'concorrência' e
- Ao comparar propostas para adquirir equipamento, a Administração considera custo de manute
- O art. 5º da Lei nº 14.133/2021 menciona eficiência e publicidade, mas não contempla plane
- A contratação de bens comuns de especificações usuais de mercado, sob a Lei nº 14.133/2021
- Como o procedimento de convite era previsto em legislação anterior, ele continua sendo a s
- Um imóvel que deve ser alienado mediante competição de ofertas pode, presentes as condiçõe
- O sistema de registro de preços constitui modalidade de licitação que substitui o pregão e
- Segundo o art. 17 da Lei nº 14.133/2021, no rito comum a fase de julgamento precede a habi
- Uma unidade pode antecipar a habilitação dos interessados em qualquer licitação apenas por
- O art. 8º da Lei nº 14.133/2021 prevê agente de contratação designado entre servidores efe
- A forma eletrônica é juridicamente obrigatória sem ressalvas em qualquer ente ou objeto ab
- Uma avaliação pelo critério de maior desconto parte de referência fixada no edital, enquan
- O critério de técnica e preço permite atribuir 90% da pontuação total à proposta técnica, 
- O credenciamento e a pré-qualificação integram o conjunto de procedimentos auxiliares do a
- A Lei nº 14.133/2021 permite modo exclusivamente fechado em julgamento por menor preço qua
- No diálogo competitivo, observados os requisitos legais, a manifestação de interesse tem p
- O pregão é aplicável aos serviços técnicos especializados de natureza predominantemente in
- Uma ata prevê 400 unidades do item para gerenciador e participantes. Sem exceções legais, 
- A assinatura de ata de registro de preços impõe à Administração adquirir todos os quantita
- A redação do art. 86, § 3º, após a Lei nº 14.770/2023, admite que entidade municipal adira
- Como o registro de preços é procedimento auxiliar, o Decreto nº 11.462/2023 permite livre 
```

## Pedido 17: Emprego e correlação de tempos e modos verbais (`portugues.verbos`, 2481 tópicos de edital)

```
Matéria: portugues.verbos (Emprego e correlação de tempos e modos verbais)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Modos e tempos do indicativo: valor temporal, aspecto e narrativa": 19
- "2. Subjuntivo, imperativo e correlação entre orações": 19
- "3. Formas nominais, locuções, haver e verbos irregulares": 26
- "4. Vozes verbais e transformações sem perda de sentido": 18
- "5. Correlação avançada, períodos complexos e leitura de efeitos de sentido": 18

Já existem (não repita):
- Em 'Quando o telefone tocou, a pesquisadora revisava as notas', qual é o valor predominant
- Em uma apresentação, alguém afirma 'O laboratório abre às sete horas todos os dias'. Qual 
- Para indicar uma condição futura possível, o redator escreveu 'Se a equipe ___ os dados am
- Um aviso destinado a alguém tratado por 'tu' deve dizer, segundo a formação tradicional do
- O redator precisa registrar que ocorreram três interrupções durante um serviço ontem. Qual
- Em 'Organizamos a escala para trabalharmos com mais segurança', como deve ser analisada a 
- Uma frase informa 'A comissão divulgou os resultados ontem'. Qual transformação para a voz
- Em um anúncio, lê-se 'Vendem-se ingressos para o espetáculo'. Qual análise corresponde à c
- Um texto sobre fato encerrado em 1889 utiliza 'Naquele ano, a população acompanha a mudanç
- No período 'Ela prometeu que entregaria o parecer na manhã seguinte', que relação temporal
- Um redator pretende escrever corretamente duas orientações: 'Quando eu ___ o arquivo, avis
- Na narrativa 'A mediadora ___ prontamente e evitou o conflito', o redator precisa empregar
- O comunicado registra 'Tenho recebido mensagens semelhantes ao longo das últimas semanas'.
- Um formulário contém a frase 'Se a organização dispusesse de recursos, ampliaria os serviç
- Uma revisão busca conservar finalidade em contexto passado: 'Distribuíram o material para 
- A forma ativa 'Os servidores tinham organizado os arquivos antes da visita' será transform
- Em uma narrativa histórica, o redator precisa informar, pela norma tradicional, que a empr
- Compare os trechos 'É necessário fazermos a revisão' e 'Quando fizermos a revisão, publica
- Em 'Embora a pesquisadora tenha concluído a análise, o relatório ainda não foi liberado', 
- O revisor compara 'O parecer está sendo elaborado' e 'O parecer tem sido elaborado em etap
- Uma equipe não pôde concluir o trabalho por inexistência de prazo suficiente. Para exprimi
- Em uma instrução condicional, escrevem-se duas situações: 'Se a equipe ___ os dados' (mant
- A frase 'Os assistentes poderiam ter encaminhado os arquivos antes do prazo' será passada 
- No período 'Não sei se os técnicos chegaram cedo', um candidato substitui 'chegaram' por '
- Leia 'O coordenador informou ontem que o procedimento continua válido'. Qual avaliação pre
- No relato 'A porta estava aberta quando alguém entrou', o pretérito imperfeito 'estava' de
- Em 'Se houver tempo, a equipe revisará a ata', 'houver' é pretérito perfeito do indicativo
- Ao substituir 'A equipe revisa os documentos' por 'Os documentos são revisados pela equipe
- Na norma-padrão, o registro 'Houveram várias falhas no sistema' está adequado porque o plu
- Na construção 'Quando a equipe concluir a revisão, comunicará a decisão', a forma 'conclui
- O enunciado 'Não canta durante a filmagem', dirigido a tu, exemplifica o imperativo negati
- Em 'Os técnicos haviam concluído a análise', haver funciona como auxiliar de tempo compost
- Na frase 'Embora a analista conheça a norma, pediu auxílio', o subjuntivo da concessiva de
- Em 'Ela afirmou que a regra continua em vigor', a combinação de afirmou no passado com con
- Uma ação passada contrária aos fatos pode ser expressa coerentemente por 'Se eu fizera a r
- Em 'Foram enviados os documentos', há voz passiva analítica; em 'Enviaram-se os documentos
- Em 'Precisa-se de especialistas', a expressão 'de especialistas' é sujeito paciente e a co
- Em 'Para chegarmos mais cedo, antecipamos a saída', a forma 'chegarmos' é infinitivo pesso
- No período 'Enquanto a equipe lia os relatórios, um alarme tocou', ambos os verbos estão n
- Em 'Se ele intervier a tempo, a situação poderá melhorar', intervier é futuro do subjuntiv
- Na frase 'Os documentos poderiam ter sido entregues antes da reunião', a locução é voz ati
- Em 'Não sei se os revisores terminaram ontem', a conjunção se introduz uma interrogativa i
- A forma 'tinha sido analisado' pode substituir 'analisou' sem qualquer mudança temporal ou
- Ao narrar fato ocorrido muitos anos atrás com 'Em 1930, começa uma nova etapa', o texto po
- A forma 'se eu manter' pertence ao futuro do subjuntivo de manter, enquanto 'se eu mantive
```

## Pedido 18: Gêneros textuais e tipos textuais (`portugues.generos-textuais`, 2453 tópicos de edital)

```
Matéria: portugues.generos-textuais (Gêneros textuais e tipos textuais)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Gênero, tipo, domínio, suporte e situação comunicativa": 17
- "2. Tipos estruturais e modos funcionais: como distingui-los": 19
- "3. Gêneros literários, não literários, jornalísticos e de estudo": 30
- "4. Gêneros normativos, oficiais e de circulação cotidiana": 17
- "5. Gêneros digitais, intertextualidade, hibridismo e oficina de produção": 17

Já existem (não repita):
- Uma publicação intitulada 'Como plantar manjericão' apresenta materiais necessários e pass
- Um poema foi publicado num livro e posteriormente numa rede social, mantendo versos, organ
- Um mesmo portal publica notícia, carta de leitor, anúncio e tirinha. Nessa situação, a cat
- Uma reportagem relata a sequência da enchente, descreve ruas atingidas e informa índices d
- Um texto escrito em turnos apresenta '— Você concluiu o cadastro? — Sim, terminei ontem'. 
- Uma campanha divulga 'Adquira o novo filtro e ganhe entrega gratuita' junto de preço e con
- Cartaz comunitário informa: 'Reduza o descarte de plástico; preserve o rio e incentive seu
- O regulamento de convivência apresenta: 'Após as 22h, os equipamentos sonoros deverão perm
- Uma apostila explica 'evaporação', oferece exemplos progressivos e propõe exercícios para 
- Num quadro de horóscopo lê-se: 'Uma surpresa surgirá em seus relacionamentos no próximo ci
- Uma peça em versos explora imagens e ritmo para representar a saudade vivida pelo eu poéti
- Um texto contém 'HELENA: Não entrarei. (Afasta-se da janela.) PEDRO: Então partirei sozinh
- Um slogan afirma 'Nossa conexão abraça o futuro', utilizando metáfora para vender acesso à
- Uma narrativa breve apresenta animais que agem como pessoas e termina com ensinamento mora
- Um leitor escreve texto que resume enredo de romance, avalia construção das personagens e 
- Um texto publicado em periódico afirma 'Defendemos que o bairro priorize ônibus' em posici
- Um documento registra: 'Aos cinco dias do mês, reuniram-se os participantes; decidiu-se am
- Uma pessoa precisa convocar moradores para encontro de sábado, informando pauta, horário e
- Um setor deseja comunicar formalmente solicitação entre unidades, com assunto identificáve
- Uma pessoa pede por e-mail acesso ao histórico de inscrições. Para produzir mensagem adequ
- Uma campanha pelo uso consciente de água se apresenta como 'receita': 'Ingredientes: atenç
- Uma ata documenta decisão sobre fechamento temporário de biblioteca. Para transformá-la em
- Uma postagem reproduz de modo reconhecível a estrutura de um conto clássico, alterando seu
- No mesmo aplicativo, uma mensagem serve para confirmar encontro entre amigos e outra comun
- Uma página apresenta duas previsões: 'Chuva de 20 mm, estimada por modelos atmosféricos' e
- Um comunicado apresenta dados verificados sobre a interrupção do abastecimento, identifica
- Uma campanha educativa de vacinação sem oferta comercial deve ser classificada obrigatoria
- Um documento que estabelece proibições, condições e deveres de convivência pode ser normat
- Uma receita publicada em revista permanece obrigatoriamente do tipo narrativo, porque todo
- Um texto destinado a ensinar princípio físico, com explicação, exemplo e exercício, pode s
- O verso 'A noite cobriu a rua com um lençol de silêncio' utiliza linguagem conotativa e, p
- Uma peça teatral apresenta nomes de personagens, rubricas de movimentação e falas alternad
- Uma crônica de linguagem imaginativa publicada em jornal passa automaticamente a ser notíc
- Uma notícia relata acontecimento com dados de apuração, enquanto reportagem pode aprofunda
- Um poema narrativo com personagens e acontecimentos deixa de ser poema, porque na tradição
- Um recado deixado em aplicativo para avisar colega sobre mudança de horário pode manter a 
- Uma sequência de mensagens em chat profissional só pode constituir texto informal e sem fi
- Um fórum eletrônico organiza respostas em tópicos, que podem ser retomados depois, enquant
- Ao escrever e-mail de solicitação para destinatário desconhecido, basta empregar vocabulár
- A transferência do conteúdo de uma ata para aviso público requer seleção de dados úteis ao
- Uma campanha de preservação ambiental adotou título 'Ingredientes' e 'Modo de preparo' par
- Quando uma peça digital reformula conto reconhecível para produzir humor crítico, há possi
- Em uma reportagem, a presença de trechos narrativos e explicativos já caracteriza obrigato
- Uma postagem que apresenta roteiro de vídeo para ensinar reparo e inclui demonstrações vis
- Um documento com título 'Convite' relata minuciosamente deliberações de encontro encerrado
```

## Pedido 19: LDB, BNCC e didática (`educacao.ldb-bncc-didatica`, 2405 tópicos de edital)

```
Matéria: educacao.ldb-bncc-didatica (LDB, BNCC e didática)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Direito à educação, organização do sistema e LDB": 14
- "2. BNCC, currículo e diretrizes para etapas e modalidades": 37
- "3. Correntes pedagógicas, teorias do currículo e formação docente": 16
- "4. PPP, planejamento didático e sala de aula como aprendizagem": 18
- "5. Avaliação da aprendizagem, inclusão e AEE": 15

Já existem (não repita):
- Uma escola revisa a finalidade constitucional do ensino e inclui desenvolvimento da pessoa
- Uma rede discute quem deve atuar prioritariamente na oferta de educação infantil e ensino 
- Uma escola afirma que a BNCC contém todo o currículo e impede temas pertinentes à realidad
- O conselho escolar deseja identificar a norma de diretrizes curriculares para o ensino fun
- Uma aula começa com tema gerador da comunidade e debate crítico em que os estudantes probl
- Uma escola deseja rever o documento coletivo que expressa identidade, princípios, priorida
- Antes de começar uma sequência didática, a professora investiga as hipóteses e conheciment
- Uma estudante com deficiência frequenta turma comum e recebe AEE para superar barreiras de
- Em 2026, uma secretaria atualiza sua referência de planejamento decenal e pretende não rep
- Uma rede elabora currículo de ensino médio em 2026 e compara a resolução de diretrizes de 
- Uma instituição revê material didático que ainda apresenta a Resolução CNE/CEB nº 1/2021 c
- Um estudante cursa ensino médio e curso técnico simultaneamente, com matrículas distintas 
- Uma escola declara participação democrática no currículo, mas concede a palavra apenas aos
- Um coordenador organiza currículo formulando objetivos, escolhendo experiências, ordenando
- Uma escola registra que a turma deve interpretar argumentos com evidências, mas avalia som
- Após tarefa intermediária, a professora observa erros recorrentes, altera a sequência de e
- Uma professora parte de problema da prática social, ensina conceitos científicos sistemati
- Um curso de licenciatura fundamenta sua reforma na Resolução CNE/CP nº 2/2019 como se aind
- Uma rede afirma que pode ofertar itinerários do ensino médio sem resguardar os direitos co
- Uma turma de EJA reúne trabalhadores com experiências diversas. O planejamento aproveita s
- Um estudante com deficiência necessita AEE e recursos pedagógicos específicos, mas não pos
- Um estado e um município debatem o financiamento educacional e concluem que a existência d
- Em estudo sobre conhecimento oficial, uma equipe analisa como escolhas curriculares reforç
- Uma direção fixa a média aritmética de provas finais como único critério e ignora evolução
- Um estudante com altas habilidades participa da turma comum e recebe AEE destinado a aprof
- Uma proposta escolar combina formação cidadã, desenvolvimento integral e preparação para o
- Um município considera que sua prioridade constitucional é ofertar o ensino médio e transf
- A equipe pedagógica elabora coletivamente a proposta institucional e os docentes participa
- Uma escola elimina a parte diversificada de seu currículo, justificando que a BNCC já cont
- Uma professora organiza aulas por exposição sistemática de conteúdos e exercícios de assim
- As expectativas implícitas de comportamento e hierarquias transmitidas na convivência esco
- Uma escola usa atividade inicial para conhecer saberes prévios e reorganizar o percurso de
- Ao revisar normas do ensino médio em 2026, uma rede trata a Resolução CNE/CEB nº 3/2018 co
- Uma professora articula conteúdos socialmente relevantes aos saberes da turma e promove ac
- Uma escola de EJA afirma que a Resolução CNE/CEB nº 1/2021 continua vigente e que as norma
- Um curso técnico e o ensino médio são oferecidos de modo integrado na mesma instituição, c
- Um curso superior de licenciatura fundamenta exclusivamente suas diretrizes atuais na Reso
- Ao reorganizar a sala, a professora estabelece combinados de convivência, cria oportunidad
- Após atividade intermediária revelar dificuldade generalizada, a professora mantém o plane
- Em 2026, um plano municipal novo é articulado ao Plano Nacional de Educação estabelecido p
- Um estudante com deficiência precisa de recursos de acessibilidade e apoio pedagógico. A e
- Uma sequência de ensino médio articula investigação da água de um bairro por ciências, geo
- Uma rede de EJA em 2026 adota a Política Nacional de Alfabetização antiga como único refer
- Uma escola elabora documentos individualizados de natureza pedagógica, como PAEE e PEI, a 
- Uma escola aplica somente prova final numérica e despreza todo o percurso de aprendizagem.
```

## Pedido 20: Poderes administrativos e abuso de poder (`direito-administrativo.poderes-administrativos`, 2385 tópicos de edital)

```
Matéria: direito-administrativo.poderes-administrativos (Poderes administrativos e abuso de poder)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Poderes, deveres, princípios e limites da competência": 16
- "2. Poder hierárquico, disciplinar e regulamentar": 22
- "3. Poder de polícia: ciclo, atributos, taxas e delegação": 21
- "4. Liberdades públicas, polícia edilícia e regulação setorial": 15
- "5. Abuso de poder, enriquecimento ilícito e controle dos deveres": 26

Já existem (não repita):
- Uma autoridade deve conceder autorização legal quando todos os requisitos objetivos estive
- Um setor delega parte de sua competência de vistoria a outro setor não subordinado, havend
- A chefia distribui tarefas, acompanha resultados e revê decisões de unidades subordinadas 
- Uma autoridade pretende punir irregularidade funcional identificada no exercício de ativid
- Norma municipal legalmente fundada exige inspeção de higiene antes do funcionamento de um 
- Uma unidade exige remuneração tributária pelo exercício regular da fiscalização sanitária,
- Em município, a fiscalização ordena paralisação de construção que descumpre exigências leg
- Organizadores pretendem realizar reunião pacífica e sem armas em local aberto ao público, 
- Autoridade competente determina inspeção somente para retaliar pessoa que a criticou, apes
- A Constituição impõe prestação de contas de quem administra recursos públicos em certas co
- Chefia pretende avocar temporariamente decisão atribuída a unidade subordinada, motivando 
- Decreto presidencial, sem aumento de despesa, reorganiza funcionamento de unidades adminis
- Uma pessoa de direito privado integrante da administração indireta recebeu delegação legal
- Uma sanção por infração de trânsito é imposta sem que haja regular notificação da autuação
- Uma entidade reguladora deseja editar norma técnica detalhando padrão de segurança previst
- Uma obra sem licença apresenta irregularidade documental sanável e nenhum risco imediato c
- Encerrada a instrução de processo administrativo federal, a autoridade necessita decidir e
- Fiscal recebe deliberadamente vantagem patrimonial indevida de interessado em providência 
- Uma delegação de competência no processo administrativo federal é publicada, prevê limites
- Autoridade analisa ato favorável com efeitos patrimoniais contínuos. O primeiro pagamento 
- Empresa privada comum, contratada para apoio operacional em fiscalizações, pretende aplica
- O Congresso identifica decreto do Executivo que excedeu limites do poder regulamentar e pr
- Autoridade afirma que decreto regulamentar com fundamento apenas em lei existente pode ser
- Após encerramento da instrução, processo federal fica sem decisão por quarenta e cinco dia
- Uma pessoa responsável por fiscalização usa o trabalho de equipe e bens móveis públicos em
- Os princípios expressos no caput do art. 37 da Constituição são legalidade, impessoalidade
- A Lei nº 9.784/1999 prevê que a competência administrativa é livremente renunciável pela a
- O poder disciplinar pode alcançar a apuração de infrações de agentes sujeitos à disciplina
- O poder de polícia administrativa é exercido apenas para investigar infrações penais, send
- No poder de polícia, ordem, consentimento, fiscalização e sanção são etapas clássicas de u
- O art. 12 da Lei nº 9.784/1999 condiciona toda delegação de competência à existência de re
- O art. 13 da Lei nº 9.784/1999 impede delegar a edição de atos normativos, a decisão de re
- A vinculação de entidade da Administração indireta à supervisão finalística da Administraç
- O art. 84, VI, da Constituição admite extinção de funções ou cargos públicos vagos por dec
- Em toda atuação de polícia administrativa, a Administração pode executar materialmente qua
- O art. 5º, XVI, da Constituição protege reunião pacífica e sem armas em local aberto ao pú
- Diante de irregularidade edilícia documental sanável, a Administração deve demolir imediat
- A tese do STF no Tema 532 admite, mediante lei e condições cumulativas de capital majorita
- A Súmula 312 do STJ determina uma única notificação no processo de multa de trânsito, send
- Na ADI 5.906/DF, reconheceu-se poder normativo técnico de entidade reguladora dentro da le
- Um decreto executivo que extrapola apenas a lei regulamentada deve ser tratado necessariam
- No art. 54, § 1º, da Lei nº 9.784/1999, para atos favoráveis com efeitos patrimoniais cont
- O simples erro culposo em fiscalização, mesmo sem vontade consciente de atingir resultado 
- Segundo o art. 70, parágrafo único, da Constituição, pessoa jurídica privada que administr
- A Lei nº 9.784/1999, art. 14, § 3º, determina que qualquer decisão adotada por delegação s
```

## Pedido 21: Organização administrativa: direta, indireta e órgãos públicos (`direito-administrativo.organizacao-administrativa`, 2372 tópicos de edital)

```
Matéria: direito-administrativo.organizacao-administrativa (Organização administrativa: direta, indireta e órgãos públicos)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Estado, governo e sentidos da Administração Pública": 12
- "2. Centralização, descentralização, concentração e desconcentração": 25
- "3. Órgãos públicos: natureza, teoria do órgão e classificações": 18
- "4. Administração direta e indireta: entidades e reserva legal": 34
- "5. Vinculação, hierarquia e organização por decreto": 11

Já existem (não repita):
- Ao estudar a organização brasileira, uma pessoa afirma que município, por possuir autonomi
- Um estudo lista ministérios, secretarias, autarquias e agentes que desenvolvem funções adm
- Uma secretaria passa a executar diretamente um serviço municipal usando uma coordenação e 
- Uma diretoria de determinada autarquia é dividida em duas gerências especializadas, ambas 
- Uma pessoa foi designada para dirigir unidade administrativa criada como centro de atribui
- Um departamento de uma autarquia federal é descrito como órgão público no relatório organi
- Uma lei específica institui diretamente pessoa jurídica de direito público para executar a
- Para criar nova empresa pública, o ente competente aprovou lei específica autorizando sua 
- Uma autarquia vinculada a determinado ministério possui personalidade e atribuições própri
- Um particular assume a execução de serviço público em colaboração, permanecendo a titulari
- Uma comissão com vários membros delibera por votação, mas não contém qualquer subdivisão d
- A Câmara Municipal quer propor ação judicial para resguardar seu funcionamento e sua auton
- Um texto de reorganização prevê sociedade de economia mista organizada sob forma de socied
- Uma lei específica autoriza a constituição de empresa pública com capital de ente federati
- Uma empresa estatal matriz possui autorização legislativa pertinente para constituir subsi
- Pretende-se expedir decreto presidencial reorganizando atribuições internas da Administraç
- Um dirigente pretende delegar competência administrativa a outro órgão federal que não lhe
- Um regulamento pretende criar autarquia por ato do Executivo e, depois, formalizar lei aut
- A Câmara Municipal pretende atuar judicialmente em nome próprio para anular cobrança tribu
- O chefe do Executivo quer extinguir por decreto um órgão da Administração federal, alegand
- A administração federal pretende delegar a uma unidade subordinada três atividades: edição
- Na revisão de modelo administrativo, foram agrupados em um único órgão três departamentos 
- Uma autarquia passou a exercer atividades próprias por outorga. Sua entidade instituidora 
- Um estudo propõe delegar competência decisória ordinária a órgão sem subordinação hierárqu
- Uma associação privada celebra acordo de colaboração para executar atividade de interesse 
- A Constituição, art. 18, reconhece municípios como componentes autônomos da organização po
- Quando se utiliza a expressão Administração Pública em sentido material, pretende-se desig
- O desmembramento de uma unidade em departamentos integrados à mesma pessoa jurídica pode c
- Uma comissão pública formada por vários integrantes adquire personalidade jurídica própria
- Pelo art. 37, XIX, da Constituição, uma lei específica cria diretamente autarquia, enquant
- O ministério e a autarquia federal a ele vinculada possuem relação hierárquica idêntica à 
- Como a Lei nº 9.784/1999 menciona órgão na Administração direta e indireta, todos esses ór
- Conforme a Súmula nº 525 do STJ, uma Câmara de Vereadores pode atuar judicialmente para pr
- Segundo a Lei nº 13.303/2016, uma sociedade de economia mista pode ser constituída sob qua
- A distinção doutrinária entre órgãos simples e compostos refere-se à estrutura interna; a 
- O art. 37, XX, da Constituição dispensa toda autorização legislativa para subsidiárias de 
- O art. 84, VI, a, da Constituição permite ao Presidente reorganizar o funcionamento da Adm
- Pela Lei nº 9.784/1999, o dirigente de órgão federal pode delegar a competência exclusiva 
- A interpretação do STF na ADI 1.649 admite que autorização legislativa genérica pertinente
- O art. 84, VI, b, da Constituição autoriza extinguir por decreto funções e cargos públicos
- Uma entidade indireta pode ser descentralizada em relação ao ente instituidor e, simultane
- Pela teoria do órgão, o agente que atua em nome de uma secretaria representa juridicamente
- A Lei nº 9.784/1999, art. 12, admite delegação parcial de competência a outro órgão ou tit
- Como o Decreto-Lei nº 200/1967, art. 10, emprega a palavra descentralização para distribui
- O art. 48, XI, da Constituição reserva o tema da criação e extinção de ministérios e órgão
```

## Pedido 22: Ato administrativo (`direito-administrativo.ato-administrativo`, 2365 tópicos de edital)

```
Matéria: direito-administrativo.ato-administrativo (Ato administrativo)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Conceito, atos da Administração, fatos e formação": 22
- "2. Elementos, vícios, motivação, motivos determinantes e pareceres": 21
- "3. Atributos, discricionariedade, mérito, classificações e espécies": 32
- "4. Extinção, anulação, revogação, convalidação e decadência": 12
- "5. Autotutela, controle das concessões iniciais e jurisprudência constitucional": 13

Já existem (não repita):
- Uma autoridade decide interditar um prédio, e posteriormente uma equipe instala as barreir
- Um ato já foi assinado e completou seu procedimento, mas uma regra estabelece que produzir
- Uma autoridade competente usa remoção administrativa para retaliar pessoa que apresentou c
- Um ato produz presunção de legitimidade, mas surge prova sólida de que seus fatos declarad
- Após verificar os requisitos legais de uma licença vinculada, a Administração pretende neg
- Um ato regular deixa de ser conveniente, e a autoridade pretende cessar seus efeitos futur
- Uma decisão administrativa sofreu defeito de competência não exclusiva, sem lesão ao inter
- Uma pessoa recebe vantagem patrimonial contínua concedida em boa-fé. Qual marco o art. 54,
- Uma concessão inicial de aposentadoria chega para registro à Corte de Contas. Segundo o Te
- Um documento técnico é exigido para instruir processo, mas a lei permite à autoridade dive
- Uma autoridade deseja delegar a outro titular a decisão de recurso administrativo, alegand
- Em ação de fiscalização, o agente declara interdição por risco estrutural inexistente. A e
- Um regulamento geral é aprovado por votação em colegiado de um único órgão. Pela classific
- Uma norma posterior torna juridicamente incompatível a manutenção de autorização administr
- Uma decisão discricionária é legal e motivada, mas o julgador considera outra solução mais
- Uma decisão desfavorável à pessoa é fundamentada mediante concordância expressa com parece
- A Administração pretende retirar vantagem individual que já gerou pagamentos, sob alegação
- Uma autoridade superior pretende avocar definitivamente competência de órgão independente,
- Um ato declaratório apenas certifica situação já existente, sem escolha de oportunidade; a
- Um ato federal favorável em boa-fé, com efeitos financeiros contínuos, teve o primeiro pag
- Um ato de competência exclusiva foi editado por autoridade sem atribuição. A titular legít
- O Tribunal de Contas está examinando pela primeira vez a legalidade de concessão inicial d
- Uma autoridade competente expõe motivo falso para negar direito e, após impugnação, tenta 
- A Administração identifica erro de legalidade em benefício individual com efeitos concreto
- Uma autoridade determina a cobrança coercitiva imediata de multa pecuniária pela apreensão
- Uma ordem administrativa que cria obrigação jurídica distingue-se da atuação material que 
- Se um ato administrativo completou sua formação, ele necessariamente possui validade e efi
- Competência, finalidade, forma, motivo e objeto são os elementos usualmente utilizados na 
- Uma ordem de serviço é necessariamente ato complexo porque afeta diversas unidades e seus 
- O art. 53 da Lei 9.784/1999 diferencia anulação por ilegalidade e revogação por conveniênc
- A Súmula 346 do STF retira da Administração o poder de reconhecer nulidade de seus atos, r
- Pela Lei 9.784/1999, a autoridade pode delegar parte de sua competência a outro órgão sem 
- O art. 13 da Lei 9.784/1999 permite delegar a decisão de recursos administrativos, desde q
- Uma decisão administrativa pode cumprir o art. 50, § 1º, da Lei 9.784/1999 mediante concor
- A presunção de legitimidade impede que o Judiciário examine fatos determinantes de ato dis
- A licença que exige preenchimento de requisitos objetivos fixados em lei é ato vinculado n
- Um parecer exigido pela norma para instrução do processo torna-se automaticamente vinculan
- O art. 54 da Lei 9.784/1999 prevê prazo de cinco anos para anular atos favoráveis, ressalv
- Um ato cujo beneficiário deixou de cumprir condição anteriormente exigida sofre caducidade
- O art. 55 da Lei 9.784/1999 admite convalidação de defeito sanável, desde que demonstrada 
- Nos termos da Súmula Vinculante 3, a Corte de Contas deve garantir contraditório e ampla d
- Para o Tema 445, o prazo de cinco anos para análise da legalidade da concessão inicial pel
- Por força da Súmula 473, a Administração pode desfazer ato que já produziu efeitos concret
- A LINDB, art. 28, disciplina a responsabilidade pessoal do agente por suas decisões ou opi
- O Tema 445 e o art. 54, § 1º, da Lei 9.784/1999 adotam o mesmo termo inicial: o primeiro p
```

## Pedido 23: Ética, moral e função pública (`etica.etica-moral-funcao-publica`, 2356 tópicos de edital)

```
Matéria: etica.etica-moral-funcao-publica (Ética, moral e função pública)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Ética, moral, valores, princípios e virtudes": 46
- "2. Ética constitucional e finalidade da função pública": 13
- "3. Democracia, cidadania, transparência e controle social": 13
- "4. Comportamento profissional, atendimento e prioridades": 15
- "5. Integridade, governança e decisões éticas responsáveis": 13

Já existem (não repita):
- Em um setor de atendimento, a equipe sustenta que uma preferência tradicional por conhecid
- Uma pessoa registra espontaneamente a própria falha, mesmo sabendo que poderia ocultá-la s
- Durante capacitação, pede-se a identificação do conjunto expresso de princípios da adminis
- Um responsável utiliza critérios de distribuição de tarefas para facilitar vantagem a fami
- Em rotina de atendimento, uma pessoa mantém aparência formal, mas ridiculariza a dúvida do
- Um grupo discute por que pessoas atendidas podem acompanhar decisões e exigir justificativ
- Uma página oficial divulga espontaneamente despesas gerais e outra equipe responde a pedid
- Uma pessoa apresenta pedido identificado de informação pública. No atendimento, exigem que
- Duas solicitações semelhantes aguardam análise. Uma terceira demanda envolve risco concret
- Uma decisão segue o roteiro documental previsto, mas usa o procedimento para criar privilé
- Uma equipe identifica tarefas simultâneas, algumas com prazo próximo e outras dependentes 
- Um relatório público contém informações sobre despesas e detalhes pessoais de usuários sem
- Após detectar inconsistência em um indicador divulgado, o responsável decide reconhecer a 
- Uma pessoa recebe orientação informal para favorecer solicitação de conhecido da liderança
- Duas organizações redigem códigos de conduta: uma privada e outra submetida ao regime cons
- Em uma reunião, discute-se quem estabelece direções estratégicas e acompanha riscos, em co
- Uma organização identifica risco de favorecimento nos atendimentos, desenvolve orientação 
- Uma comunidade considera normal não comunicar erros administrativos para proteger a reputa
- Um pedido de informação é registrado quando o documento já existe, mas ainda precisa de lo
- Uma análise interna qualifica como improbidade todo atendimento negligente, ainda que sem 
- Uma equipe precisa decidir como publicar dados de produtividade que contêm erros e informa
- Em avaliação de governança, uma equipe compara respostas tempestivas, qualidade das regras
- Uma ação de controle social identifica que relatórios públicos são publicados em formato p
- O exame de uma conduta aponta violação ética e possível atuação enquadrável no art. 11 da 
- Uma decisão sobre reorganização de atendimentos foi tomada sob pressão por resultados rápi
- Em determinado setor, uma prática reiterada de favorecer conhecidos é considerada normal p
- Após concluir corretamente um formulário, uma pessoa favorece deliberadamente parente na d
- Uma equipe divulga espontaneamente dados gerais sobre atividades e despesas de interesse c
- Uma pessoa pede acesso a informação pública e identifica-se adequadamente. Quem a atende p
- Uma demanda apresenta risco imediato comprovado e outra, de mesma natureza, não possui urg
- Uma pessoa demonstra cortesia ao receber usuários, mas comunica dado que sabe ser falso pa
- Ao analisar responsabilidade pelo serviço, uma equipe distingue governança, que avalia, di
- Uma atividade de participação social permite acompanhar metas e despesas, mas deve substit
- Durante reunião, propõe-se publicar relatório de interesse coletivo com informações pessoa
- Em reunião sobre desempenho, conclui-se que moralidade e eficiência são princípios alterna
- Uma iniciativa de integridade institui análise de riscos, capacitação, mecanismos de preve
- Uma chefia solicita que a equipe ignore divergência documental para evitar desgaste. Em no
- Em uma empresa privada e em uma entidade pública, políticas internas podem estimular hones
- Diante de pedido de informação já disponível para fornecimento, o responsável considera qu
- Uma análise de integridade considera falta ética de determinada conduta, mas não encontra 
- Em caso analisado com base no art. 11 da Lei nº 8.429/1992, considera-se que a lesividade 
- Uma equipe reformula regras internas para tornar seu efeito mais claro, presta contas dos 
- Uma organização mantém registros honestos e fiscalização sobre a fila, mas a liderança det
- Uma equipe recebe reclamações fundamentadas sobre falta de clareza de dados públicos. Escl
- Durante avaliação de decisão pública, constata-se que uma alternativa teria baixo custo, p
```

## Pedido 24: Internet e navegadores (`informatica.internet-navegadores`, 2274 tópicos de edital)

```
Matéria: informatica.internet-navegadores (Internet e navegadores)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Internet, web, intranet, extranet, portais e endereçamento": 37
- "2. Navegadores modernos, abas, pesquisas e produtividade": 16
- "3. Internet Explorer legado, Opções da Internet e modo IE": 16
- "4. Dados de navegação, privacidade, downloads, upload e impressão": 16
- "5. Segurança de navegação, pesquisa crítica e serviços não indexados": 15

Já existem (não repita):
- Uma pessoa utiliza o navegador para consultar páginas de um portal, mas também transfere a
- Uma organização disponibiliza um sistema de pessoal somente a usuários internos autenticad
- Um fornecedor externo recebe credenciais para consultar somente pedidos de compra da organ
- Ao digitar o endereço de um site no navegador, uma consulta permite descobrir o endereço I
- Uma pessoa precisa localizar uma palavra dentro de uma página já aberta no navegador, sem 
- Durante a pesquisa, uma guia com documento importante foi fechada por engano. Em navegador
- Uma pessoa clica no ícone de estrela para guardar o endereço de um portal que deseja visit
- Um site institucional antigo exige componentes específicos do Internet Explorer. No contex
- O endereço https://portal.exemplo.org.br/docs/manual.pdf?edicao=4#sumario é analisado para
- Uma pessoa realiza consulta em um portal interno a partir de casa por uma VPN corporativa 
- Uma pessoa apaga apenas o cache do navegador, preservando cookies e senhas. Qual efeito é 
- Depois de acessar uma conta em portal de serviços, o usuário exclui os cookies do site e p
- Um portal permite anexar documentos e pede que a pessoa selecione um arquivo local e confi
- Uma pessoa abre janela de navegação InPrivate no Edge, baixa um PDF e cria um favorito. Ap
- Uma página foi impressa para PDF por meio de Ctrl+P e outra cópia foi criada pelo comando 
- Ao receber um link para autenticação, o usuário observa https://login.portal-seguro.exempl
- Uma página de aparência fraudulenta usa HTTPS e apresenta certificado técnico válido para 
- Uma orientação antiga apresenta o filtro SmartScreen como componente do Internet Explorer.
- Um sistema interno não aparece nos resultados de buscadores e exige login pela web convenc
- No Firefox, uma pessoa navega em janela privativa, faz download de um documento e, ao fech
- Uma extensão solicita permissão para ler dados de páginas visitadas e funcionar também na 
- Uma pessoa não encontra um relatório interno em mecanismo público de busca, mas o acessa n
- Depois de remover a lista de downloads do navegador, uma pessoa abre a pasta Downloads do 
- Um navegador falha ao acessar somente portal.exemplo.net e exibe mensagem de que não conse
- Uma pessoa acessa uma página web a partir de navegador e, em outro programa, utiliza servi
- Uma intranet institucional deve funcionar sem qualquer conexão à internet, porque seu uso 
- Uma página apresenta um botão com texto 'Portal oficial'. Esse texto permite garantir que 
- Em navegador moderno no Windows, pressionar Ctrl+J normalmente abre o gerenciador ou a pág
- Uma aplicação antiga menciona recursos e menus do Internet Explorer 11. Para interpretá-la
- Em janela de navegação anônima do Chrome, o provedor e a rede institucional ficam impossib
- Uma pessoa apaga somente o cache de imagens do navegador e, por esse comando, remove obrig
- Em formulário web, a pessoa seleciona um PDF local no campo de anexos, mas ainda precisa c
- Ao usar Ctrl+P e escolher salvar como PDF, uma pessoa gera uma representação de impressão 
- O cache DNS do sistema e o cache do navegador para imagens são o mesmo mecanismo de armaze
- No Internet Explorer clássico, a seção Geral de Opções da Internet era usada apenas para d
- Uma pessoa marca como favorito um site durante sessão privada do Edge. O encerramento das 
- O SmartScreen do Microsoft Edge moderno pode auxiliar na identificação de sites suspeitos 
- Um relatório não aparece em resultados de pesquisa porque exige autenticação em portal int
- No endereço https://acesso.organizacao.exemplo.com.invalido.net/entrar, o host pertence ao
- Em instalação atual do Windows 11, o SmartScreen continua ativo dentro do modo IE exatamen
- Uma extensão expressamente autorizada para executar em janela privada fica tecnicamente im
- Um firewall aplica regras ao tráfego entre redes, enquanto a autorização no portal delimit
- Um documento baixado em janela privativa do Firefox desaparece necessariamente da pasta lo
- Serviços onion utilizam endereços .onion próprios da rede Tor. Seu uso não é necessariamen
```

## Pedido 25: Lei Geral de Proteção de Dados (Lei 13.709/2018) (`legislacao.lei-13709-2018-lgpd`, 2252 tópicos de edital)

```
Matéria: legislacao.lei-13709-2018-lgpd (Lei Geral de Proteção de Dados (Lei 13.709/2018))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos, âmbito de aplicação, conceitos e princípios": 19
- "2. Bases legais, dados sensíveis, direitos e decisões automatizadas": 20
- "3. Poder Público, compartilhamento, serviços digitais e aplicação distrital": 23
- "4. Agentes de tratamento, segurança, privacy by design e governança": 20
- "5. Agência Nacional de Proteção de Dados, fiscalização e sanções": 18

Já existem (não repita):
- Uma empresa mantém fichas impressas com nome, endereço e telefone de clientes e sustenta q
- Um aplicativo guarda identificador persistente que, combinado a uma tabela interna, permit
- Uma plataforma coleta informações sobre diagnóstico individual e impressão digital usada n
- Para efetuar cadastro de entrega, uma loja pede endereço e também religião, sem ligação co
- Uma empresa recebe do cliente pedido de compra e utiliza endereço apenas para entregar o p
- Um hospital utiliza informação clínica indispensável em procedimento realizado por profiss
- Um titular solicita saber se existe tratamento e pede acesso a seus dados, pretendendo dec
- Um prestador armazena folha de pagamentos conforme instruções de empresa que define finali
- Uma repartição utiliza dados previstos em lei para executar serviço público, sem solicitar
- Uma empresa deseja reutilizar informações clínicas sensíveis em campanha comercial alegand
- Uma organização substitui nomes por códigos, mas conserva tabela que permite associar cada
- Um titular retira consentimento de um tratamento, mas certos documentos devem ser guardado
- Um sistema nega crédito por decisão tomada exclusivamente por algoritmo, sem intervenção d
- Dois serviços públicos pretendem compartilhar cadastros para execução de política pública 
- Em outubro de 2026, uma unidade administrativa distrital aplica o Decreto nº 42.036/2021 c
- No Distrito Federal, um titular precisa comunicar preocupação sobre dados mantidos por sua
- Um controlador identifica acesso não autorizado com possível risco relevante a titulares. 
- Uma entidade desenvolve um aplicativo que já inicia com perfil público e coleta informaçõe
- Uma entidade avalia riscos à liberdade dos titulares num projeto de reconhecimento biométr
- Uma nota técnica elaborada em outubro de 2026 descreve a ANPD como mero órgão da administr
- Uma pessoa jurídica privada recebe sanção de multa simples por infração de LGPD. Qual limi
- Uma entidade pública recebe processo sancionador por exposição indevida de dados. Pode-se 
- Um agente de pequeno porte não indica encarregado, mas disponibiliza canal de comunicação 
- Um provedor de aplicações sujeito ao art. 15 do Marco Civil da Internet afirma que seu pra
- Após vazamento de dados cadastrais comuns, sem prova de consequência lesiva, um autor exig
- Uma empresa consulta dados de clientes em arquivos físicos. Mesmo sem usar computadores, a
- Uma entidade considera toda informação financeira pessoal necessariamente sensível segundo
- Uma empresa retém apenas informações estritamente pertinentes ao serviço contratado. Essa 
- O consentimento é a única base admitida para tratar dados pessoais comuns, inclusive nas h
- Uma pessoa pede acesso a seus dados pessoais e o controlador pode fornecer declaração comp
- Uma prestadora executa processamento de dados conforme instruções da contratante controlad
- Uma entidade pública trata dados indispensáveis para política prevista em lei; a LGPD admi
- Um administrador pública dados nominais de saúde em página institucional porque os registr
- Um órgão distrital ainda cita o Decreto nº 42.036/2021 como fundamento vigente; a norma fo
- A substituição de nomes por identificadores codificados, acompanhada de chave de reidentif
- Em atendimento clínico, o dado sensível de saúde pode ser tratado sem consentimento quando
- O art. 20 da LGPD determina, na redação vigente, que toda revisão de decisão unicamente au
- Um incidente de segurança com risco relevante deve ser comunicado pelo controlador à ANPD 
- Pelo art. 46, § 2º, as medidas de proteção de dados pessoais somente precisam ser implanta
- Um agente de pequeno porte pode, nas condições da Resolução CD/ANPD nº 2/2022, ser dispens
- Em outubro de 2026, a ANPD permanece apenas órgão da administração direta, pois a Lei nº 1
- O art. 52, § 3º, da LGPD permite aplicar a entidades e órgãos públicos advertência e outra
- Um programa de governança em privacidade aprovado internamente afasta a possibilidade de f
- Uma empresa pretende transferir dados pessoais a outro país e precisa examinar hipóteses d
- No AREsp 2.130.619/SP, julgado em 2023, foi estabelecido que qualquer vazamento de dados c
```

## Pedido 26: Agentes públicos: classificação, cargo, emprego e função (`direito-administrativo.agentes-publicos`, 2242 tópicos de edital)

```
Matéria: direito-administrativo.agentes-publicos (Agentes públicos: classificação, cargo, emprego e função)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Conceito, espécies de agentes públicos e natureza dos vínculos": 43
- "2. Concurso, contratação temporária, provimento, posse e exercício": 15
- "3. Constituição: remuneração, acumulação, estágio probatório e estabilidade": 14
- "4. Exercício funcional, licenças, afastamentos, deveres e vacância": 14
- "5. Responsabilidades, controle, greve e situações difíceis": 14

Já existem (não repita):
- Uma pessoa é convocada para atuar como jurada durante julgamento e não integra quadro perm
- Uma unidade administrativa designará pessoa para função de confiança prevista no art. 37, 
- Uma empresa pública admite candidato aprovado em concurso sob regime celetista. Qual é a n
- Para atender calamidade prevista em lei, a administração pretende contratar equipe apenas 
- Uma certidão foi emitida por pessoa aparentemente investida em cargo, mas a investidura fo
- Um edital prevê concurso válido por dois anos e pretende prorrogar esse prazo uma única ve
- No regime da Lei nº 8.112/1990, determinado candidato foi nomeado para cargo efetivo. Em q
- Após a publicação de sua nomeação federal, pessoa sem impedimento legal precisa tomar poss
- Uma servidora federal toma posse no prazo, mas, sem causa justificadora, deixa transcorrer
- Uma servidora ascende de classe na mesma carreira, segundo critérios estatutários, sem ing
- Um estatuto pretende permitir que servidor seja transferido para carreira completamente di
- Após inspeção médica, servidora sofre limitação permanente e passa a exercer cargo de atri
- Uma norma local autoriza por prazo indeterminado contratação temporária para suprir falta 
- Um professor efetivo pretende acumular cargo administrativo não classificado como técnico 
- Empregada celetista concursada de empresa estatal sustenta que três anos de trabalho lhe a
- Uma unidade pretende aumentar vencimentos de servidores por mero ato administrativo, sem l
- Profissional com habilitação regulamentada deseja acumular dois empregos privativos de sua
- Durante emergência legalmente prevista, um particular é compulsoriamente convocado para co
- Servidora efetiva obtém licença legalmente prevista para tratamento de interesses particul
- Servidor recebe ordem escrita e manifestamente ilegal para divulgar informação protegida p
- Uma reforma administrativa desloca de um órgão a outro cargo efetivo vago, enquanto, em at
- Uma chefia encerra a ocupação de cargo em comissão por conveniência, e outra apura infraçã
- Servidores com atuação direta em segurança pública anunciam greve e pedem mediação coletiv
- Após apuração funcional, agente é absolvido criminalmente por decisão definitiva que afirm
- Em paralisação de servidores civis não vinculados diretamente à segurança pública, ficou d
- Durante eleição, cidadão convocado como mesário presta colaboração pública episódica sem a
- Uma pessoa sem cargo efetivo pode ser designada diretamente para função de confiança porqu
- Lei que cria cargo em comissão deve destiná-lo a atribuições de direção, chefia ou assesso
- Uma entidade pode contratar por tempo determinado para serviço permanente rotineiro simple
- Uma pessoa nomeada sob a Lei nº 8.112/1990 toma posse no vigésimo dia após a publicação do
- Servidora federal empossada dispõe, como regra, de trinta dias contados da posse para inic
- Servidor de determinada carreira não pode ingressar, pela simples transformação de seu vín
- Um professor concursado não pode acumular seu cargo com outro cargo administrativo não téc
- Profissional da saúde com profissão regulamentada pode acumular dois empregos privativos d
- O estágio probatório federal continua legalmente restrito a vinte e quatro meses, embora o
- O servidor efetivo afastado por licença regular não tem, apenas em razão da licença, seu c
- A requisição de particular para prestar colaboração compulsória em emergência e a cessão d
- Ao receber ordem manifestamente ilegal para divulgar dados protegidos, servidor deve recus
- Quando pessoa em cargo em comissão é exonerada de ofício por decisão administrativa regula
- No regime da Lei nº 8.112/1990, promoção e readaptação podem figurar simultaneamente como 
- Se servidor é absolvido criminalmente apenas por insuficiência de provas, a administração 
- Em greve de servidores civis provocada por atraso ilícito no pagamento imputável ao poder 
- Servidores que atuam diretamente em segurança pública podem realizar greve desde que a cat
- Uma contratação chamada temporária, mas destinada a suprir necessidade estrutural ordinári
- Um agente que, no exercício funcional, causa dano pode responder administrativamente ou pe
```

## Pedido 27: Colocação pronominal (`portugues.colocacao-pronominal`, 2215 tópicos de edital)

```
Matéria: portugues.colocacao-pronominal (Colocação pronominal)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos da colocação e identificação dos pronomes átonos": 20
- "2. Próclise: fatores de atração, subordinadas e pausas": 20
- "3. Ênclise: início da oração, imperativo, gerúndio e infinitivo": 20
- "4. Mesóclise e colocação nas locuções verbais": 20
- "5. Reescrita avançada, adequação de registro e armadilhas de prova": 20

Já existem (não repita):
- Em uma revisão de frases para registro formal, qual construção atende à colocação pronomin
- Um comunicado precisa preservar a posição correta do pronome depois de uma palavra negativ
- Numa pergunta direta sobre a autoria de uma comunicação, selecione a versão formal correta
- Ao incorporar uma oração relativa à redação, qual frase mantém a posição pronominal exigid
- Um aviso foi convertido em ordem afirmativa direta. Qual é a redação formal adequada?
- Ao iniciar uma frase independente com um verbo, qual alternativa segue o padrão formal tra
- A redação empregará futuro do presente sem palavra atrativa anterior. Qual opção respeita 
- Um revisor encontrou estruturas com tempos compostos. Em qual delas o pronome não foi liga
- Ao substituir um objeto direto masculino por pronome átono, qual opção tem grafia regular?
- Uma decisão será comunicada após a revisão. Qual alternativa apresenta próclise compatível
- Uma equipe redige cinco períodos independentes. Qual apresenta colocação pronominal compat
- Uma frase negativa com auxiliar e particípio deve ser corrigida. Qual redação respeita as 
- Uma mensagem informa que decidiram comunicar uma data. Qual alternativa tem colocação form
- Sem termo de atração antecedente, um narrador usa futuro do pretérito em registro solene. 
- Um texto formal combina oração subordinada e declaração no futuro. Qual redação respeita a
- Ao retomar um objeto direto por pronome átono, qual opção apresenta adaptação ortográfica 
- O objeto direto de “arquivaram” será retomado por pronome enclítico. Qual construção é cor
- Uma frase está no futuro composto e é precedida por “já”, sem pausa. Qual posição do prono
- Ao mencionar informação omitida no passado, qual redação negativa respeita o auxiliar e o 
- Compare “Fez isso para não o incomodar” e “Fez isso para não incomodá-lo”. O que conclui a
- Uma revisão deve empregar “o” para retomar complemento direto de “encontrar”. Qual redação
- O verbo “obedecer” é usado com complemento introduzido por “a”. Qual alternativa o substit
- Um compromisso é formulado em futuro do presente sem expressão atrativa. Qual alternativa 
- Uma construção combina futuro do pretérito na principal e uma condição com “se”. Qual alte
- Em uma locução com auxiliar no pretérito e principal no gerúndio, qual frase respeita a co
- Em “Não me entregaram a informação”, o advérbio de negação determina próclise ao verbo fin
- Em uma oração independente, “Me comunicaram a data” é a abertura recomendada pelos manuais
- Na pergunta “Quem lhe contou o fato?”, a posição de “lhe” é explicada pelo pronome interro
- A expressão “Não enviar-lhe-ei” emprega mesóclise aceita, pois o verbo está no futuro do p
- A forma “revisá-lo” pode resultar da junção de “revisar” ao pronome objeto direto “o”.
- Na sequência “Haviam informado-lhe a alteração”, o pronome fica corretamente enclítico ao 
- “Em se tratando de urgência” emprega próclise na construção tradicional com em seguido de 
- A presença de “que” relativo em “O aviso que enviaram-me” autoriza a ênclise ao verbo da o
- Em “Entregaram-no”, a forma “-no” decorre da terminação nasal de “entregaram”.
- As construções “para não o contrariar” e “para não contrariá-lo” são igualmente vedadas na
- A forma “Dir-lhe-ia a verdade” exemplifica mesóclise no futuro do pretérito.
- Em “Não havia-me avisado”, a ênclise ao auxiliar é preferível porque o verbo principal est
- Na frase “A equipe nos consultou”, a próclise pode ser admitida mesmo sem palavra de atraç
- Quando o verbo é precedido por “jamais”, a norma formal conserva mesóclise em “Jamais cont
- Na oração “A decisão que, após a revisão, me comunicaram foi mantida”, a intercalação não 
- Em “Eu o encontrei”, “o” é substituível por “lhe” sem alteração da correção padrão, porque
- A construção “Explique-me o motivo” é uma formulação formal adequada para o imperativo afi
- Em “Vou te explicar a alteração”, a sequência entre auxiliar e infinitivo é considerada a 
- O futuro do pretérito pode comportar mesóclise, mas a construção “Quem lhe diria?” exige p
- A grafia “publicar-o” está de acordo com a Base XVII do Acordo Ortográfico porque basta ac
```

## Pedido 28: Clínica médica: endocrinologia, nefrologia, gastroenterologia e infectologia (`medicina.clinica-endocrino-renal-gastro-infecto`, 2166 tópicos de edital)

```
Matéria: medicina.clinica-endocrino-renal-gastro-infecto (Clínica médica: endocrinologia, nefrologia, gastroenterologia e infectologia)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Endocrinologia: diabetes, tireoide, metabolismo e glândulas": 21
- "2. Nefrologia: lesão renal, glomérulos, litíase e equilíbrio acidobásico": 21
- "3. Gastroenterologia: refluxo, úlcera, intestino, vesícula e pâncreas": 20
- "4. Infectologia e hepatologia: diarreia, hepatites e doença hepática": 20
- "5. Urgências frequentes: tontura, cefaleia, rinossinusopatias e alergia": 18

Já existem (não repita):
- Uma pessoa relata poliúria, polidipsia e emagrecimento há semanas. Sua glicemia plasmática
- Uma paciente apresenta sonolência, constipação e intolerância ao frio. Os exames mostram T
- Um adolescente previamente saudável apresenta náuseas, dor abdominal, respiração profunda,
- Uma mulher chega com hipotensão, fraqueza intensa, hiperpigmentação cutânea, sódio de 127 
- Uma pessoa com nefrolitíase recorrente apresenta cálcio sérico elevado e PTH persistenteme
- A creatinina de um paciente era 0,9 mg/dL há 36 horas e passou a 1,3 mg/dL. Não há dados p
- Uma pessoa com diabetes tem TFGe de 95 mL/min/1,73 m² e relação albumina/creatinina de 160
- Após quadro infeccioso, uma pessoa apresenta urina escura, edema, hipertensão e exame de u
- Paciente com edema generalizado exibe proteinúria de 6 g/dia, albumina sérica reduzida e h
- Um paciente com cólica lombar apresenta febre de 39 °C, calafrios e hidronefrose por cálcu
- Uma pessoa com azia e regurgitação há meses passa a apresentar disfagia progressiva e perd
- Uma adulta com anemia ferropriva e diarreia crônica será investigada para doença celíaca; 
- Uma pessoa com diarreia intermitente e dor abdominal apresenta fístula perianal e estenose
- Uma pessoa com cálculos biliares apresenta febre, dor no hipocôndrio direito, icterícia e 
- Uma paciente apresenta dor epigástrica intensa irradiada para o dorso e lipase 5 vezes aci
- Uma pessoa adulta tem diarreia aquosa há dois dias, mantém boa perfusão e consegue beber l
- Em rastreamento, um paciente assintomático tem anticorpo anti-HCV reagente. Não há dados a
- Uma pessoa vacinada contra hepatite B apresenta HBsAg negativo, anti-HBc total negativo e 
- Uma pessoa com cirrose e ascite interna-se com febre e piora clínica. A paracentese revela
- Um paciente com cirrose avançada tem ascite, encefalopatia e INR elevado, mas transaminase
- Uma paciente apresenta crises vertiginosas de 20 segundos ao virar-se para a direita na ca
- Um adulto desenvolve vertigem contínua abrupta, vômitos e nistagmo, acompanhados de diplop
- Uma pessoa apresenta coriza e congestão viral há seis dias, melhora no sétimo e retorna no
- Uma pessoa em uso de inibidor da enzima conversora desenvolve edema de lábios e língua, se
- Um adulto relata cefaleia que começou subitamente e alcançou intensidade máxima em poucos 
- Uma pessoa assintomática apresenta glicemia plasmática de jejum de 112 mg/dL, sem outros c
- Uma paciente com TSH elevado e T4 livre ainda normal apresenta hipotireoidismo primário ma
- Um paciente com diurese sustentada de 0,3 mL/kg/h por sete horas preenche critério urinári
- Uma pessoa com SIAD apresenta sódio baixo por retenção de água; por essa razão, espera-se 
- Após tratamento de Helicobacter pylori associado a úlcera, pode-se documentar erradicação 
- Uma pessoa adulta apresenta IMC de 31 kg/m², mas glicemia, triglicerídeos, HDL, pressão ar
- Um exame endoscópico identifica inflamação contínua desde o reto até parte do cólon, predo
- Uma pessoa com hepatite A aguda comprovada por anti-HAV IgM apresenta risco habitual de ev
- Em uma pessoa com potássio de 6,7 mEq/L, um eletrocardiograma sem ondas T apiculadas não e
- Na investigação de hepatite C, a positividade do anti-HCV, mesmo sem HCV-RNA detectável, c
- Uma pessoa com insuficiência suprarrenal primária pode apresentar hiperpigmentação, hipote
- Uma pessoa apresenta urticária, dispneia e hipotensão logo após exposição a alérgeno conhe
- Uma pessoa com glicemia de 560 mg/dL e sódio sérico medido de 128 mEq/L pode apresentar hi
- Na acidose metabólica com bicarbonato de 12 mEq/L, uma PaCO2 de 40 mmHg representa compens
- Na avaliação de nódulo tireoidiano com TSH suprimido, pode ser apropriado pesquisar autono
- Uma pessoa apresenta HBsAg negativo e tanto anti-HBs quanto anti-HBc total positivos após 
- Uma pessoa com pancreatite aguda tem necrose pancreática à imagem, mas não apresenta evidê
- Em uma pessoa com suspeita forte de doença celíaca, deficiência de IgA total e anti-transg
- Uma pessoa em uso de inibidor da enzima conversora tem edema de língua sem habões e respos
- Em uma pessoa com crises vertiginosas de segundos provocadas por virar na cama e que está 
```

## Pedido 29: Microsoft Excel 2016 (`informatica.microsoft-excel-2016`, 2135 tópicos de edital)

```
Matéria: informatica.microsoft-excel-2016 (Microsoft Excel 2016)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Ambiente, edição e estrutura do Excel 2016": 23
- "2. Fórmulas e funções por categoria": 20
- "3. Busca, validação e tratamento de dados": 19
- "4. Tabelas Dinâmicas, gráficos e macros": 20
- "5. Impressão, dados externos e revisão prática": 18

Já existem (não repita):
- Uma pessoa precisa salvar uma pasta do Excel 2016 contendo macros gravadas e deseja preser
- Em uma planilha, o endereço D12 identifica qual localização?
- Uma célula guarda o número 0,35. A pessoa aplica formato Percentagem sem alterar seu conte
- Em C2 existe a fórmula =A2*$B$1. Ao copiá-la para C3, qual expressão resulta?
- Uma planilha contém A2=5, A3=7 e A4=9. Qual fórmula calcula a soma dessas três células sem
- A2:A5 contém Sul, Norte, Sul, Norte e B2:B5 contém 10, 20, 30, 40. Qual fórmula do Excel 2
- Para liberar uma solicitação, B2 precisa ser maior ou igual a 60 e C2 deve conter Sim. Qua
- Uma lista contém centenas de registros de atendimento. É necessário exibir apenas as linha
- Uma pessoa quer obter somente a data atual do computador em uma célula, sem mostrar a hora
- Após selecionar B2:B10, alguém pressiona Delete para apagar valores. Qual resultado é espe
- A2:C4 possui código na primeira coluna e preço na terceira. O código informado em F2 deve 
- A célula C2 contém =SEERRO(A2/B2;"Indefinido"). Se A2=24 e B2=0, o que será exibido?
- A equipe deseja contar somente linhas cuja coluna A tenha o valor Sul e cuja coluna B tenh
- Ao trabalhar especificamente no Excel 2016 para Windows, alguém quer uma lista sem duplica
- Uma Tabela Dinâmica precisa mostrar total de vendas por região e permitir filtrar por seto
- Uma equipe mantém um gráfico vinculado a uma Tabela Dinâmica. Ela altera o campo Setor em 
- Uma planilha possui duas linhas idênticas por engano, mas a pessoa deseja antes inspecioná
- Ao importar um CSV com códigos como 0014, a coluna aparece como 14 porque foi reconhecida 
- Em C2 existe a fórmula =A2*B2, com B2=20, e deseja-se encontrar A2 para que C2 passe a val
- Uma pessoa gravou macro para formatar mensalmente relatórios. Qual afirmação sobre sua exe
- Uma Tabela Dinâmica foi construída com fonte fixa A1:D100. Após lançar dados nas linhas 10
- Em D4 foi escrita a fórmula =$A4*B$2. Ao copiá-la duas colunas à direita e três linhas par
- Uma planilha extensa tem cabeçalho na linha 1. É necessário repeti-lo em cada folha impres
- Uma operação de crédito de R$ 1.000 prevê dez parcelas mensais a juros de 1% ao mês. Ao us
- Uma planilha Excel 2016 usa =PROCV(F2;A2:D50;4) para localizar códigos não ordenados. Algu
- No Excel 2016 para Windows, o atalho Ctrl+1 abre a caixa Formatar Células, onde se encontr
- Uma pasta do Excel 2016 com macro gravada conserva o código VBA ao ser salva no formato pa
- Na fórmula =$A$2+B3 copiada uma linha para baixo, $A$2 continua apontando para a mesma cél
- O comando Filtro, aplicado a uma tabela com situações Concluído e Pendente, elimina defini
- Ao editar o conteúdo textual de uma célula no Excel 2016 para Windows, Alt+Enter permite i
- A função AGORA() retorna exclusivamente a data atual, sem qualquer componente de hora em s
- A função CONT.SES pode contar registros que satisfaçam simultaneamente as condições Região
- Na instalação padrão do Excel 2016 para Windows, tanto PROCX quanto ÚNICO estão disponívei
- Após modificar a fonte de dados de uma Tabela Dinâmica no Excel 2016, pode ser necessário 
- Uma regra de Validação de Dados configurada com lista suspensa constitui mecanismo absolut
- Quando o quarto argumento de PROCV é omitido, a pesquisa adota correspondência aproximada;
- A fórmula =DIREITA(A2;3), aplicada a uma célula com o texto Código123, devolve necessariam
- Uma fórmula =$A3*C$2 copiada de D3 para F5 torna-se =$A5*E$2, pois o deslocamento é de dua
- O comando Formatação Condicional > Valores Duplicados elimina automaticamente linhas iguai
- Ao importar de CSV um código 00057 que deve continuar sendo identificador, definir o tipo 
- Uma segmentação ligada à Tabela Dinâmica pode filtrar também o Gráfico Dinâmico associado,
- A visualização de um Gráfico Dinâmico ligado a uma Tabela Dinâmica é independente de seus 
- Atingir Meta pode determinar o valor de uma célula de entrada para que uma célula contendo
- O atalho Ctrl+Shift+L abre o painel de impressão do Excel 2016, ao passo que Ctrl+P ativa 
- No Excel 2016, um cabeçalho ou rodapé de impressão pode conter campo predefinido de número
```

## Pedido 30: Controle da Administração Pública (`direito-administrativo.controle-administracao`, 2107 tópicos de edital)

```
Matéria: direito-administrativo.controle-administracao (Controle da Administração Pública)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos e classificações do controle": 19
- "2. Controle administrativo: autotutela, legalidade e mérito": 21
- "3. Recursos e instrumentos do controle administrativo": 19
- "4. Controle legislativo e fiscalização parlamentar": 26
- "5. Controle externo, tribunais de contas e tomada de contas especial": 15

Já existem (não repita):
- Uma auditoria avaliou gastos, resultados de programas e renúncia de receitas. Qual classif
- Durante a execução de um projeto público, uma unidade revisa medições e despesas antes do 
- Uma autoridade acompanha, com base em lei, os resultados de uma entidade administrativa do
- Uma autoridade detecta que vantagem administrativa foi concedida com vício de legalidade. 
- Um ato apresenta defeito que pode ser corrigido sem atingir terceiros ou interesse público
- Um pagamento continuado deriva de ato administrativo favorável, sem má-fé comprovada. Em r
- Uma pessoa recorre de decisão administrativa na esfera federal. Nos termos do art. 56, § 1
- Após ciência regular de decisão administrativa federal, uma pessoa deseja recorrer, sem ex
- Uma decisão recorrida produzirá efeito imediato, e a pessoa demonstra receio de dano de di
- Uma sanção administrativa definitiva passa a ser questionada por fato novo relevante. Qual
- Um ato normativo do Executivo ultrapassa os limites de seu poder regulamentar. Qual fundam
- A mesa de uma casa legislativa solicita formalmente informações escritas a autoridade cont
- Uma comissão de investigação parlamentar é proposta para examinar fato concreto por períod
- Ao final do exercício, são entregues as contas anuais do chefe do Executivo. Qual é a atri
- Foram encaminhadas contas de quem administra valores públicos em entidade da administração
- O tribunal de contas identifica ilegalidade em ato administrativo e fixa prazo para saneam
- Foi apontada ilegalidade em contrato administrativo. Qual é a regra constitucional inicial
- Após procedimento regular, o tribunal de contas imputou débito e multa a responsável. Qual
- A autoridade detecta que recursos públicos repassados não tiveram aplicação comprovada e h
- Uma tomada de contas especial apurou dano inferior ao limite de remessa imediata estabelec
- Um sistema de controle interno detecta ilegalidade relevante durante auditoria. Qual dever
- Um interessado protocola tempestivamente recurso federal perante órgão incompetente. O que
- Em recurso ordinário, a autoridade competente identifica fundamento para decisão potencial
- Uma comissão de fiscalização detecta indícios de despesa não autorizada e busca informaçõe
- Na fiscalização de contratações públicas, a terceira linha de defesa da Lei nº 14.133/2021
- Uma entidade privada que gerencie dinheiro público nas condições do art. 70, parágrafo úni
- Durante exame de um programa, uma equipe de controle interno identifica baixa eficiência, 
- Constatada ilegalidade de um ato administrativo, a própria Administração deve examinar sua
- Uma decisão discricionária foi praticada validamente, mas deixou de ser conveniente. Por i
- Um ato favorável gera pagamentos mensais contínuos; se não houver má-fé comprovada, o praz
- Após apresentar recurso federal contra obrigação administrativa, o interessado fica automa
- Diante de ato normativo executivo que exorbita do poder regulamentar, o Legislativo dispõe
- No controle das contas anuais do chefe do Executivo federal, o tribunal de contas exerce j
- Após examinar contas de administradores de entidade pública, o tribunal de contas pode exe
- Uma unidade de controle interno toma conhecimento de ilegalidade e informa apenas a chefia
- Em pedido escrito de informações dirigido pela mesa legislativa a autoridade constituciona
- Uma comissão parlamentar de investigação pode impor condenação criminal definitiva ao inve
- Em fiscalização de contrato irregular, a regra constitucional prevê sustação inicial pelo 
- Depois de descumprido prazo de correção de ato ilegal, o tribunal de contas fica impedido 
- Constatada omissão no dever de prestar contas de recursos federais repassados, a autoridad
- Se o dano apurado por tomada de contas especial for inferior ao limite anual para remessa 
- Se a autoridade recursal cogita piorar a situação da pessoa que recorreu, deve cientificá-
- Diante de fato novo relevante, a revisão de processo que resultou em sanção permite elevar
- Em processo no tribunal de contas da União cuja decisão possa anular ato que beneficie o i
- Na Lei nº 14.133/2021, o tribunal de contas compõe a primeira linha de defesa, junto das u
```

## Pedido 31: Números racionais: frações e decimais (`matematica.numeros-racionais`, 2088 tópicos de edital)

```
Matéria: matematica.numeros-racionais (Números racionais: frações e decimais)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Conjuntos numéricos, reta real e relações de ordem": 37
- "2. Frações, equivalência e operações fundamentais": 16
- "3. Representações decimais, dízimas e frações geratrizes": 14
- "4. Potências, raízes e prioridade das operações": 17
- "5. Problemas de frações, contagem e intervalos: estratégias avançadas": 16

Já existem (não repita):
- Uma medição foi registrada como 0,375 unidade. Qual é a classificação mais específica dess
- Um pacote contém 18 itens selecionados entre 24. Qual é a fração irredutível da parte sele
- Uma mistura recebeu 1/4 de litro de um líquido e depois 1/6 de litro de outro. Qual é o vo
- Um recipiente comporta 2/3 de litro. Ao enchê-lo com 9/10 de sua capacidade, quantos litro
- Uma medida de 3/4 de litro será dividida em porções de 9/8 de litro por unidade de referên
- Em uma planilha, a medida 0,125 deve ser expressa como fração irredutível. Qual conversão 
- Uma calculadora exibe 0,7777..., com o algarismo 7 repetindo-se sem fim. Qual fração corre
- Um cálculo usa o quadrado do número -2, com o sinal negativo incluído na base. Qual result
- De um lote com 80 peças, 5/8 foram encaminhadas para conferência. Quantas peças foram enca
- Três registros de medida são 0,3, 1/3 e 0,35. Qual alternativa apresenta a ordem crescente
- De um conjunto com 120 documentos, 1/4 foi retirado e, em seguida, 1/3 do restante também 
- A expansão decimal de uma quantidade é 0,1666..., na qual só o algarismo 6 se repete. Qual
- Em uma expressão de medidas, calcula-se 3/4 menos o quadrado de 1/2. Qual valor exato se o
- Dois marcadores estão nas posições -1,25 e +0,75 da reta real. Qual é a distância entre el
- Em um levantamento, 3/5 de todas as unidades correspondem a 42 registros. Quantas unidades
- Para comparar os registros 0,58, 7/12 e 0,6, sem arredondar o valor fracionário, qual rela
- Uma operação foi registrada sem parênteses como 24 ÷ 6 × 2. Seguindo a ordem convencional,
- Um sensor produz a dízima 0,23454545..., com anteperíodo 23 e período 45. Qual é sua fraçã
- Depois de retirar 1/4 de um estoque e 2/5 do que restou, sobraram 81 peças. Qual era a qua
- Compare os valores de (-3/4)² e de -(3/4)², respeitando os parênteses. Qual afirmação está
- Em um relatório, uma fração foi exibida como 0,083333..., sendo somente o algarismo 3 peri
- Uma sequência de frações precisa conter exatamente uma com expansão decimal finita, após a
- Calcule a expressão (3/4 - (1/2)²) ÷ (5/6), tratando o conteúdo entre parênteses como divi
- Uma marcação deve ser um racional estritamente entre 1/3 e 1/2 e estar escrita em doze avo
- Se 2/3 de uma caixa correspondem a 16 peças indivisíveis, quantas peças há em 3/8 da caixa
- Uma lista contém os números naturais 0, 1, 2 e 3. Todos esses elementos também pertencem a
- Uma medida foi anotada como 0,625. Por apresentar três casas decimais, essa medida pertenc
- Um recipiente está pela metade. As frações 2/4 e 1/2 podem representar exatamente a mesma 
- Para dividir uma medida por 1/3, uma pessoa pode multiplicar a medida por 1/3, pois a divi
- Uma folha apresenta 0,5 como taxa decimal de participação. A mesma taxa pode ser represent
- Ao obter a raiz quadrada principal de 16, um cálculo pode registrar -4 como resultado, poi
- O valor de uma divisão exibido como 0,6666..., com repetição indefinida do algarismo 6, po
- Dois saldos estão expressos por -5/6 e -2/3. O saldo -5/6 é maior porque sua fração tem nu
- Retirou-se 1/4 de um estoque. Se metade da quantidade remanescente também foi retirada, so
- No cálculo de (-3)², o resultado é -9, porque o sinal de menos permanece após a potenciaçã
- Ao somar as parcelas 1/3 e 1/6 de uma mesma unidade, obtém-se exatamente metade da unidade
- Um intervalo foi descrito como [1,3). Uma medição exatamente igual a 3 pertence a esse int
- A fração 1/8 possui representação decimal finita porque, na forma irredutível, seu denomin
- Uma calculadora mostra a expansão decimal infinita 0,999... . Esse número é estritamente m
- Para a dízima 0,1666..., a representação 0,1(6) indica anteperíodo 1 e período 6, e sua ge
- Para a=1/2 e b=1/3, elevar a soma ao quadrado fornece o mesmo resultado que a²+b², pois a 
- Se duas posições distintas a e b são racionais e a é menor que b, a média (a+b)/2 fornece 
- Para todo racional a, a raiz quadrada principal de a² é igual a a, inclusive quando a repr
- Se x=0,23454545..., com 23 antes do período 45, a eliminação da parte periódica pode ser e
- Após retirar 1/4 de um conjunto, selecionar 2/5 do restante equivale a selecionar 2/5 do t
```

## Pedido 32: Teoria do crime (`direito-penal.teoria-do-crime`, 2075 tópicos de edital)

```
Matéria: direito-penal.teoria-do-crime (Teoria do crime)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Infração penal, teorias do crime e fato típico": 18
- "2. Causalidade, omissão, dolo, culpa e agravação do resultado": 22
- "3. Consumação, tentativa, desistência e crime impossível": 22
- "4. Erros penais, descriminantes putativas e culpabilidade": 20
- "5. Ilicitude, causas justificantes e excessos puníveis": 18

Já existem (não repita):
- Uma lei prevê para determinada infração a pena de prisão simples, alternativamente à multa
- Em uma análise segundo o finalismo, o julgador precisa identificar em qual elemento do cri
- Uma pessoa provoca resultado material por ação sem a qual ele não teria ocorrido. Que disp
- Quem assume formalmente a proteção de uma pessoa vulnerável deixa de agir quando podia imp
- Uma pessoa pratica ação prevendo o resultado danoso e efetivamente assumindo o risco de pr
- Em situação de trabalho, a pessoa deixa de adotar cautela exigida, produzindo resultado pr
- O agente inicia a execução de delito, mas é contido por terceiro antes do resultado exigid
- Após praticar parte da execução, o agente percebe que poderia prosseguir, mas decide livre
- Um agente tenta matar mediante substância totalmente inofensiva, acreditando ser veneno le
- Por equívoco de fato, alguém leva um objeto idêntico ao seu, acreditando ser de sua propri
- Em crime material, um ferimento inicial é seguido de acidente hospitalar autônomo que, por
- Alguém prevê a possibilidade de resultado lesivo, mas acredita concretamente que suas habi
- O tipo de crime prevê resultado que agrava especialmente a pena. O resultado mais grave oc
- Após executar a conduta que poderia produzir morte, a pessoa providencia socorro decisivo 
- O autor de delito patrimonial cometido sem violência ou grave ameaça à pessoa restitui vol
- O agente conhece todos os fatos relevantes, mas não percebe a proibição da conduta por err
- Por erro sobre a pessoa, o autor agride quem confundiu com a vítima visada. Qual diretriz 
- Uma pessoa supõe, por circunstâncias plenamente justificáveis, agressão armada iminente qu
- Para afastar dano de inundação atual que não provocou, alguém danifica obstáculo alheio qu
- Um ataque injusto está em curso, e a pessoa reage com meios necessários e intensidade mode
- Ao ser atacada, uma pessoa emprega força adequada e neutraliza o agressor. Depois que ele 
- Um agente inicia o ato por ameaça moral irresistível de terceiro. Em outro caso, seu braço
- Uma pessoa age acreditando, de modo evitável, que certa conduta plenamente conhecida é jur
- Durante ação, alguém utiliza meio que falha naquela ocasião, mas que era objetivamente apt
- Alguém deixa de impedir resultado, apesar de saber que havia criado previamente o risco, t
- Uma norma comina prisão simples ou multa para certa infração. De acordo com o critério do 
- Um agente inicia a execução de contravenção, mas é impedido por terceiro. A tentativa deve
- A pessoa inicia execução de crime e é interrompida por intervenção externa, sem que se com
- Se o agente pratica resultado por negligência, é possível puni-lo pela modalidade culposa 
- Quando alguém repele injusta agressão atual usando moderadamente os meios necessários, pod
- Depois de causar lesão, o agente se arrepende, mas o resultado morte ocorre apesar de soco
- O agente que responde pela criação anterior de risco e podia evitar o resultado pode ocupa
- No arrependimento posterior do art. 16, a reparação feita voluntariamente depois do recebi
- No erro de tipo essencial, o desconhecimento de elemento constitutivo do tipo exclui o dol
- A pessoa que sofre ameaça moral irresistível de terceiro terá necessariamente exclusão da 
- Uma pessoa dispara contra alguém pensando tratar-se de outra vítima; o erro sobre identida
- Uma causa superveniente relativamente independente exclui imputação do resultado mesmo qua
- Se uma pessoa pratica delito com dolo inicial e provoca resultado especialmente agravador 
- Em hipótese de erro de proibição evitável, o art. 21 determina isenção de pena exatamente 
- Na descriminante putativa fática, erro plenamente justificado sobre situação que legitimar
- No estado de necessidade, quem tinha dever legal de enfrentar o perigo pode invocar livrem
- Mesmo quando se reconhece legítima defesa no início da reação, quem prossegue dolosamente 
- Conforme a Súmula 145 do STF, a simples vigilância policial de local de delito, sem induzi
- O art. 25, parágrafo único, exige observância dos requisitos do caput ao disciplinar defes
- Uma pessoa pratica conduta formalmente típica em estrito cumprimento de dever legal e dent
```

## Pedido 33: Receita e despesa pública (`afo.receita-despesa-publica`, 1986 tópicos de edital)

```
Matéria: afo.receita-despesa-publica (Receita e despesa pública)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Receita pública: ingressos, categorias econômicas e classificações": 19
- "2. Estágios da receita, fonte de recursos e dívida ativa": 19
- "3. Despesa pública e classificações institucional, funcional, programática e por natureza": 18
- "4. Estágios da despesa, programação e descentralização da execução": 18
- "5. Restos a pagar, exercícios anteriores, suprimento de fundos e controles": 26

Já existem (não repita):
- Uma unidade pública recebe caução em dinheiro de uma empresa, a ser devolvida quando encer
- Uma entidade pública arrecada aluguel de prédio próprio e, em operação separada, vende um 
- Um município cobra imposto predial por imposição legal e recebe pagamento contratual pelo 
- No mesmo mês, um ente contrata empréstimo de longo prazo para obras e operação de crédito 
- Um contribuinte paga imposto em banco arrecadador, mas a transferência à conta central da 
- Um aluguel devido ao ente público venceu, não foi pago e teve a liquidez e certeza apurada
- Uma receita orçamentária foi efetivamente arrecadada no exercício, embora sua estimativa n
- Recursos recebidos para finalidade específica por convênio permanecem em caixa ao terminar
- Uma unidade registra juros de empréstimo público, aquisição de equipamentos novos e amorti
- Um orçamento identifica a aplicação direta de recursos para aquisição de material de consu
- Ao montar relatório, uma equipe deseja identificar qual unidade orçamentária é responsável
- Uma despesa foi registrada como função de saúde e subfunção específica de atuação governam
- O planejamento prevê construir nova unidade pública em dois anos e manter diariamente os s
- Ao analisar o código 3.3.90.30 de uma despesa orçamentária, um estudante precisa compreend
- Um contrato de prestação continuada tem valor anual certo e pagamentos mensais pré-definid
- Uma nota fiscal foi apresentada para pagamento de equipamentos, mas o responsável ainda nã
- No encerramento do ano, uma obrigação legal foi empenhada em dezembro, mas só liquidada e 
- Uma lei orçamentária foi publicada em 2 de janeiro e a equipe prepara a programação financ
- Ao final de um bimestre, a receita prevista para o restante do exercício não comporta as m
- Uma unidade orçamentária descentraliza crédito para que unidade de órgão distinto execute 
- Em 31 de dezembro, uma despesa havia sido empenhada, seu serviço regularmente prestado e l
- Em fevereiro, reconhece-se obrigação por serviço efetivamente prestado no ano anterior, qu
- Um servidor já responde por dois adiantamentos não encerrados e solicita novo suprimento p
- Ao comparar passivos públicos com créditos a receber, uma equipe encontra restos a pagar, 
- No fechamento anual, há R$ 100 mil empenhados: R$ 40 mil liquidados e não pagos, R$ 35 mil
- Um contrato de aluguel de imóvel público gera receita originária e, quanto à categoria eco
- Uma caução devolvível depositada em dinheiro deve constar como receita corrente tributária
- Um tributo previsto e lançado foi recebido por agente arrecadador em novembro, mas recolhi
- Uma dívida de aluguel público vencida e inscrita após apuração de liquidez e certeza const
- Os valores recebidos com destinação legal a um programa específico mantêm essa vinculação 
- O pagamento de juros sobre empréstimo público e a amortização do seu principal são ambos c
- A classificação institucional permite identificar órgão e unidade orçamentária, enquanto a
- Uma receita corrente decorrente de tributo foi lançada em dezembro e paga somente em fever
- A inscrição de crédito exigível em dívida ativa exige apuração de liquidez e certeza, e nã
- O projeto de construção de nova unidade e a atividade de manutenção do atendimento continu
- Um contrato de valor anual conhecido, pago em parcelas mensais, pode ser empenhado globalm
- A modalidade de aplicação da despesa tem a mesma finalidade do elemento de despesa: indivi
- Em uma unidade que recebeu destaque orçamentário para executar ação de outra estrutura, o 
- Quando a arrecadação do primeiro bimestre frustra as metas fiscais, a LRF exige limitação 
- Um empenho à conta de crédito com vigência plurienal ainda não havia sido liquidado ao fim
- Uma obrigação empenhada e liquidada regularmente em dezembro, mas não paga, pode ser autom
- Um servidor utilizou suprimento de fundos até 31 de dezembro na esfera federal. Quanto aos
- Um crédito não tributário de aluguel do patrimônio público, inscrito para cobrança, integr
- Na esfera federal, o suprimento de fundos exige prévio empenho; a restituição de valor não
- Nos casos especiais do art. 60, § 1º, da Lei nº 4.320/1964, a dispensa de emissão da nota 
```

## Pedido 34: Lei de Improbidade Administrativa (Lei 8.429/1992) (`legislacao.lei-8429-1992-improbidade`, 1983 tópicos de edital)

```
Matéria: legislacao.lei-8429-1992-improbidade (Lei de Improbidade Administrativa (Lei 8.429/1992))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Sistema de improbidade: fundamento, dolo e sujeitos": 26
- "2. Tipos legais: enriquecimento, dano ao erário e princípios": 27
- "3. Sanções, dosimetria e efeitos patrimoniais": 15
- "4. Apuração, indisponibilidade, ação e solução consensual": 18
- "5. Prescrição, retroatividade e jurisprudência atual": 14

Já existem (não repita):
- Um responsável assinou ato baseado em orientação jurídica plausível, mas depois o ato foi 
- Uma pessoa exerceu gratuitamente, por designação, funções públicas durante dois meses e pa
- Um empresário combinou deliberadamente com agente público a fraude de pagamentos e fornece
- Um gestor direcionou dolosamente a aquisição de materiais com sobrepreço e os autos compro
- Uma pessoa deixa de prestar contas disponíveis, propositalmente, para encobrir irregularid
- Um agente recebeu comissão de interessado para facilitar a liberação de recursos sob sua r
- Uma autoridade usa designações recíprocas para colocar parentes em funções de confiança, i
- Após licitação irregular, não foi demonstrado que o poder público pagou preço excessivo ou
- Um fiscal praticou ato voluntário que foi posteriormente anulado por violação regulamentar
- Na condução de processo seletivo, agente adulterou intencionalmente critérios para conferi
- Uma condenação por enriquecimento ilícito preenche o art. 9º, e a decisão busca identifica
- Reconheceu-se ato doloso que causou perda material ao erário e se enquadra no art. 10. O j
- O julgamento reconheceu somente improbidade dolosa do art. 11, sem acréscimo patrimonial e
- Um condenado por ato doloso com dano ao erário faleceu, deixando herança inferior ao preju
- Um agente entrega a declaração exigida ao assumir função pública, mas deixa o exercício me
- Uma apuração reúne indícios especialmente fortes de enriquecimento ilícito, mas a urgência
- Em pedido de indisponibilidade, discutem-se R$ 30 mil mantidos em conta-corrente, quantia 
- Um requerimento de indisponibilidade pretende garantir ressarcimento, multa civil e eventu
- Uma pessoa jurídica diretamente prejudicada ajuíza ação de improbidade, embora o art. 17 c
- Os legitimados negociam acordo de não persecução civil após descobrir desvio patrimonial e
- O juiz recebeu petição inicial de ação de improbidade em devida forma e ordenou a citação 
- Durante a ação, decisão motivada afasta cautelarmente um agente de suas funções para prote
- Um ato culposo teria ocorrido em 2020 e era discutido em processo ainda sem trânsito em ju
- Em ação de improbidade instaurada após a reforma, ocorreu marco interruptivo previsto no a
- Uma demanda pretende apenas ressarcimento de desvio de recursos, comprovadamente decorrent
- Uma pessoa designada por curto período, sem remuneração, pode ser agente público para fins
- A mera assinatura voluntária de decisão administrativa posteriormente anulada é suficiente
- O recebimento doloso de vantagem patrimonial indevida, ligada às atribuições do agente, po
- A frustração dolosa de licitação, sem qualquer demonstração de perda material, preenche ne
- O art. 11 pode ser aplicado a condutas tipificadas que causem lesividade relevante, mesmo 
- Se o fato se enquadrar apenas no art. 11, a lei prevê suspensão de direitos políticos até 
- Em caso de herança recebida após ato doloso lesivo ao erário, a obrigação de reparar pode 
- A aprovação das contas por tribunal de contas impede, como regra absoluta, a investigação 
- Quando a pessoa jurídica lesada propõe diretamente a ação de improbidade, há legitimidade 
- A pessoa física que é apenas sócia de empresa investigada por improbidade responde automat
- Uma indisponibilidade de bens pode ser examinada cautelarmente antes do trânsito em julgad
- Após as ADIs 7.156 e 7.236, é obrigatório demonstrar perigo concreto de dano em toda hipót
- Uma pessoa que deixa de prestar contas para ocultar irregularidades, apesar de dispor de c
- O prazo de contestação dos requeridos, na ação de improbidade, corresponde aos noventa dia
- O art. 23, § 1º, prevê que a instauração de inquérito civil ou processo administrativo pod
- O julgamento de julho de 2026 manteve o reinício da prescrição pela metade, de modo que qu
- A pretensão de ressarcimento ao erário por ato doloso tipificado na LIA é imprescritível s
- Segundo o Tema 1.199, a lei de 2021 que suprimiu improbidade culposa desfaz automaticament
- Após o julgamento de junho de 2026, a indisponibilidade pode cobrir valores de multa civil
- Uma absolvição criminal colegiada por insuficiência probatória impede necessariamente o pr
```

## Pedido 35: Microsoft Word 2016 (`informatica.microsoft-word-2016`, 1958 tópicos de edital)

```
Matéria: informatica.microsoft-word-2016 (Microsoft Word 2016)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Estrutura, arquivos e interface": 20
- "2. Edição, formatação e página": 22
- "3. Elementos, referências e campos": 20
- "4. Reescrita de procedimentos e como o tema costuma ser cobrado": 19
- "5. Revisão, controle de alterações, proteção e acessibilidade": 19

Já existem (não repita):
- Qual formato é usado normalmente para salvar um documento do Word 2016 sem macros?
- Qual formato serve como base reutilizável para criar documentos semelhantes sem sobrescrev
- Considerando o padrão internacional (versão em inglês) do Word, qual atalho salva as alter
- Para iniciar a verificação de ortografia e gramática no Word para Windows, qual tecla é us
- Que atalho insere uma quebra de página manual no ponto de inserção?
- Qual comando abre a visualização e as opções de impressão no Word?
- Qual guia contém o comando de colunas para formatar texto como em um boletim?
- Para inserir um cabeçalho no documento do Word 2016, qual caminho é adequado?
- Um relatório deve mudar para paisagem apenas em uma página intermediária. Qual procediment
- Como se cria um sumário automático que acompanha títulos do documento?
- Qual comando insere legenda numerada para uma figura no Word?
- Qual ação atualiza o resultado de um campo selecionado, como um número de página ou sumári
- Qual atalho marca uma entrada para o índice remissivo no Word?
- Na mala direta, qual elemento identifica onde o nome de cada destinatário aparecerá?
- Uma unidade envia comunicados personalizados a partir de uma planilha. Qual combinação com
- Qual recurso impede que o destinatário abra o arquivo sem fornecer a senha configurada?
- Qual afirmação diferencia Controlar Alterações de Restringir Edição?
- Uma servidora precisa localizar clip-art online e inserir o resultado escolhido no documen
- A secretaria recebeu linhas de nomes e matrículas separadas por tabulações e quer convertê
- Uma ordem de serviço contém quatro etapas numeradas e subetapas que devem permanecer hiera
- O texto deve passar de uma seção em duas colunas para uma única coluna na mesma página. Qu
- Em uma citação normativa, o símbolo de parágrafo (§) deve aparecer antes do número, e a ab
- Qual procedimento mantém páginas anteriores sem número e inicia a numeração em parte poste
- Qual afirmação sobre os campos do Word está correta?
- A servidora precisa imprimir apenas as páginas 3 a 5, em duas cópias, e quer conferir o re
- A primeira seção do relatório precisa de margens próprias, sem alterar as páginas seguinte
- Um quadro de lotação exige dados alinhados por servidor e matrícula; a exposição seguinte 
- A capa de um relatório deve ter rodapé distinto do restante da seção. Que opção atende ao 
- Após visualizar os registros personalizados de uma mala direta, qual etapa gera os documen
- Uma circular precisa destacar um aviso lateral, com texto próprio, ao lado de um parágrafo
- No Word 2016 em português (configuração padrão Brasil), qual conjunto de atalhos executa, 
- Ao utilizar o mouse para selecionar conteúdos rapidamente no corpo de um texto do Word 201
- Qual é a diferença de comportamento entre acionar o ícone do Pincel de Formatação com um c
- Durante a revisão de um ofício no Word 2016, o usuário observa palavras grifadas com subli
- Um servidor selecionou um parágrafo que estava em Negrito, Itálico, fonte tamanho 18 e des
- Para formatar referências bibliográficas segundo normas técnicas em que a primeira linha d
- Uma tabela extensa ocupa três páginas consecutivas de um parecer técnico, e o chefe do set
- Dois analistas receberam cópias independentes da mesma minuta de contrato e fizeram altera
- Certo ou errado: Ctrl+S salva as alterações do documento ativo na instalação padrão em ing
- Certo ou errado: um arquivo .dotx é um modelo do Word sem macros.
- Certo ou errado: Ctrl+Enter insere uma quebra de página manual no Word.
- Certo ou errado: Controlar Alterações, por si só, exige senha para abrir o documento.
- Certo ou errado: estilos de título podem servir de base a um sumário automático.
- Certo ou errado: uma quebra de página permite, sozinha, cabeçalho independente no trecho s
- Certo ou errado: F9 pode atualizar o campo selecionado no documento.
- Certo ou errado: cada linha de uma fonte de mala direta costuma representar um registro de
- Certo ou errado: digitar { PAGE } manualmente cria necessariamente um campo funcional de n
- Certo ou errado: em uma seção vinculada à anterior, o cabeçalho pode repetir o conteúdo da
- Certo ou errado: na mala direta, inserir destinatários e inserir campos de mesclagem são a
- Certo ou errado: criptografar com senha e restringir edição cumprem exatamente a mesma fun
- Certo ou errado: o índice de ilustrações pode ser montado a partir de legendas inseridas n
- Certo ou errado: restringir a edição torna dispensável revisar os campos e conferir a visu
- Certo ou errado: uma nova legenda sempre atualiza automaticamente o índice de ilustrações 
- Certo ou errado: alterar o nível de zoom na barra de status de 100% para 200% dobra o tama
- Certo ou errado: quando o comando Mostrar Tudo (¶) está ativado na guia Página Inicial, os
- Certo ou errado: pressionar Shift+Enter insere uma quebra manual de linha, deslocando o cu
- Certo ou errado: na janela de impressão (Ctrl+P), informar o intervalo personalizado '1; 4
- Certo ou errado: a Área de Transferência do Office no Word 2016 armazena no máximo 1 único
- Certo ou errado: ao aplicar o comando Editar Lista de Destinatários na Mala Direta e desma
- Certo ou errado: o comando Marcar como Final criptografa o documento com chave simétrica e
- Certo ou errado: o atalho Ctrl+Shift+F9 desvincula um campo selecionado no Word 2016, conv
- Certo ou errado: se um documento com Controlar Alterações ativo e exibido no modo 'Todas a
```

## Pedido 36: Acentuação gráfica (`portugues.acentuacao`, 1948 tópicos de edital)

```
Matéria: portugues.acentuacao (Acentuação gráfica)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Sílaba tônica, monossílabos e oxítonas": 20
- "2. Paroxítonas, proparoxítonas e encontros vocálicos": 20
- "3. Hiatos com i/u tônicos, exceções e casos de fronteira": 20
- "4. Acentos diferenciais, verbos e mudanças ortográficas": 20
- "5. Exceções aplicadas, derivados e resolução de provas": 20

Já existem (não repita):
- Uma revisão apresenta as palavras 'café', 'lápis' e 'médico' e solicita sua classificação 
- Em uma frase, aparecem 'pá', 'pé' e 'pó', cada uma como palavra de uma sílaba pronunciada 
- Ao revisar uma relação de palavras oxítonas, é necessário justificar o acento de 'também' 
- Em material de estudo, encontram-se as palavras 'lâmpada', 'médico' e 'público'. O que exp
- Uma lista registra o substantivo singular 'hífen' e seu plural 'hifens'. Qual alternativa 
- Um texto emprega as formas 'herói' e 'heroico'. Segundo o Acordo Ortográfico, qual justifi
- Num comunicado, aparecem 'país' e 'pais'. Qual análise da acentuação permite distinguir es
- Ao preencher um registro com as palavras 'saída', 'saúde' e 'baú', identifica-se uma regra
- Uma frase contrapõe 'Ontem ela não pôde participar' e 'Hoje ela pode participar'. Qual é a
- Uma revisão substitui grafias anteriores ao Acordo por formas atuais. Qual conjunto aprese
- Um revisor encontra o substantivo 'caráter' e seu plural 'caracteres' e precisa explicar p
- Em uma atividade, pede-se preencher: 'O pesquisador ___ o tema; os participantes ___ o tem
- Uma correção deve distinguir 'Eles ___ cedo' (verbo vir) e 'Eles ___ o sinal' (verbo ver).
- Após a reforma, um texto utiliza 'voo', 'enjoo' e 'leem'. Qual afirmação descreve a mudanç
- Um relatório utiliza 'rápido', 'rapidamente', 'fácil' e 'facilmente'. Por que as duas pala
- Em uma frase aparece 'A rainha saiu sorrindo'. Um estudante sugere 'raínha' porque o i é p
- Um editor compara 'feiura', 'baiuca' e 'Piauí'. Qual afirmação é compatível com as regras 
- Para completar a frase 'O relatório foi elaborado ___ eles; ontem ela não ___ revisar o te
- A norma admite certa faculdade de acentuação para o substantivo que designa recipiente usa
- Um texto atual contém o diminutivo de 'chapéu' e um derivado de 'café' com sufixo iniciado
- Um parágrafo distingue 'a pessoa sábia', 'ela sabia da notícia' e 'um sabiá cantava'. Em q
- Ao corrigir uma frase, alguém propõe escrever 'rúbrica' em vez de 'rubrica' porque costuma
- Uma transcrição atual apresenta o verbo 'arguir' no presente: 'Ele ___ que houve engano'. 
- Uma frase contém 'água', 'série' e 'ideia'. Por que a última não recebe agudo, embora as o
- Uma revisão encontra 'juiz', 'juízes', 'raiz' e 'raízes'. Qual critério resolve, simultane
- Em 'café', a sílaba tônica é a última e o acento agudo se justifica pela regra das oxítona
- Toda palavra oxítona termina com acento gráfico, motivo pelo qual 'caju', 'tatu' e 'aqui' 
- As palavras 'lâmpada', 'médico' e 'pássaro' são proparoxítonas e conservam acento gráfico 
- O plural correto de 'hífen' é 'hífens', pois as paroxítonas conservam obrigatoriamente no 
- Na norma atual, 'papéis' mantém acento por ser oxítona com ditongo aberto éi, enquanto 'id
- Em 'saída' e 'baú', os acentos gráficos são exemplos de ditongos abertos das oxítonas, e n
- Os pares 'tem/têm' e 'vem/vêm' continuam registrando, pela presença de circunflexo, a dist
- O Acordo Ortográfico aboliu o circunflexo em toda palavra portuguesa, de modo que 'avô', '
- O substantivo 'fôrma' admite circunflexo facultativo para se diferenciar de 'forma' em det
- A reforma preservou o acento em 'pára' para distinguir o verbo da preposição 'para', mas e
- A Base XIII prevê que advérbios em -mente derivados de adjetivos acentuados, como 'fácil' 
- O 'i' tônico de 'raiz' e 'juiz' deve receber agudo, pois a regra dos hiatos ignora as cons
- O Acordo Ortográfico deixou de usar trema em palavras portuguesas como 'linguiça' e 'tranq
- Como o ditongo aberto oi perdeu acento nas paroxítonas, a reforma exige escrever 'heroi' e
- O 'u' tônico após ditongo não recebe agudo em paroxítona como 'feiura', mas pode recebê-lo
- A sequência 'veem, leem, creem, deem' deve recuperar circunflexo pela mesma justificativa 
- A palavra 'série' conserva acento, apesar de a paroxítona 'ideia' ter perdido agudo, porqu
- A grafia 'chapéuzinho' está correta atualmente porque qualquer derivado de oxítona termina
- A dupla 'acadêmico/académico' é mencionada pelo Acordo entre variações de grafia associada
- Na oração 'Os pesquisadores mantêm os registros e o sistema mantém as cópias', o circunfle
```

## Pedido 37: Responsabilidade civil do Estado (`direito-administrativo.responsabilidade-civil-estado`, 1880 tópicos de edital)

```
Matéria: direito-administrativo.responsabilidade-civil-estado (Responsabilidade civil do Estado)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Evolução histórica e bases da responsabilidade estatal": 28
- "2. Pressupostos, causalidade, excludentes e atenuantes": 22
- "3. Omissões estatais e dever específico de proteção": 19
- "4. Quatro Temas do STF: 940, 592, 130 e 777": 13
- "5. Reparação, regresso, prescrição e atos legislativos ou jurisdicionais": 18

Já existem (não repita):
- Durante atividade de serviço, um agente de pessoa jurídica pública danificou o veículo de 
- Uma pessoa jurídica prestadora de serviço público causa dano funcional a terceiro e invoca
- Um veículo oficial, conduzido em atividade funcional, causa colisão. A vítima pretende ind
- Uma pessoa ficou ferida por ônibus de prestadora privada de transporte coletivo, mas não e
- Em colisão com veículo público, a vítima comprovadamente contribuiu para o resultado, mas 
- Um dano ocorre por atuação funcional do Estado. Para instaurar ação regressiva contra o ag
- A morte de pessoa sob custódia suscita discussão sobre o dever estatal de proteção física 
- Um dano é provocado por ato funcional de registrador oficial em serviço delegado. Qual con
- Um particular pretende indenização contra a Fazenda Pública por dano ocorrido há quatro an
- Qual exemplo caracteriza adequadamente a chamada falta do serviço na evolução subjetiva da
- Uma pessoa é lesada apenas por circunstância externa, inteiramente independente da atuação
- Pessoa jurídica pública pratica medida legal que causa prejuízo excepcional e individualiz
- Após uma chuva comum, árvore com risco reiteradamente comunicado cai sobre veículo; a Admi
- Um ato administrativo danoso é praticado por agente público no exercício funcional. A víti
- Um defeito funcional causa prejuízo de R$ 8 mil em reparos e R$ 3 mil em renda razoavelmen
- Um particular sofre dano por falha de registro praticada por delegatário oficial. Segundo 
- A Administração foi condenada a reparar dano por ato funcional. Em pedido regressivo, não 
- Uma pessoa presa morreu em contexto de omissão de medida de proteção exigível, embora o ev
- Um parecer diz que concessionária de serviço público responde objetivamente só aos usuário
- Um terceiro sofre dano material após atuação de concessionária que exerce atividade alheia
- Uma norma legislativa regular produz transtorno indistinto e ordinário; um particular afir
- Um ato funcional danoso foi praticado por pessoa sem vínculo com serviço público, mas o Es
- Uma decisão judicial é reformada por divergência interpretativa e o interessado pede inden
- Uma pessoa custodiada sofre lesão evitável por vigilância específica, mas o Estado alega a
- Após o Estado indenizar dano de registro, comprovou-se culpa de responsável delegatário. O
- Se agente público causa dano a terceiro enquanto atua nessa qualidade, a pessoa jurídica p
- Responsabilidade civil objetiva significa que a pessoa lesada está dispensada de provar ta
- A responsabilidade geral do Estado baseada no risco administrativo admite exame de causas 
- Se a vítima contribuiu parcialmente para o dano juntamente com falha funcional, a culpa co
- Pela regra do art. 37, § 6º, o exercício do direito de regresso contra agente causador dep
- O Tema 130 do STF limita a responsabilidade objetiva da pessoa jurídica prestadora privada
- O art. 5º, LXXV, da Constituição prevê reparação estatal para condenado por erro judiciári
- Em caso de omissão estatal, a simples existência de dever geral de proteger toda a populaç
- O Tema 592 do STF relaciona a responsabilidade por morte de detento à inobservância do dev
- Pelo Tema 940 do STF, a vítima de ato funcional deve propor indenização diretamente contra
- O Tema 777 do STF reconhece responsabilidade objetiva estatal por ato danoso de tabelião o
- Como o Código Civil prevê reparação civil em três anos, o Tema 553 do STJ adota o prazo tr
- A obrigação de indenizar pode abranger danos emergentes e lucros cessantes demonstrados, d
- No regime do risco administrativo, qualquer fenômeno natural imprevisível exclui responsab
- A circunstância de um terceiro ter executado a agressão contra alguém sob guarda estatal n
- Por força do Tema 777, o Estado está dispensado de buscar regresso contra delegatário regi
- Um ato estatal lícito pode gerar dever de indenizar quando produz lesão especial e anormal
- A declaração de inconstitucionalidade de uma lei torna indenizável, sem prova de prejuízo 
- O art. 5º, LXXV, prevê indenização por erro judiciário e por prisão que exceda o tempo fix
- Segundo o Tema 940, a impossibilidade de ação direta da vítima contra o agente público por
```

## Pedido 38: Termos da oração (`portugues.termos-da-oracao`, 1800 tópicos de edital)

```
Matéria: portugues.termos-da-oracao (Termos da oração)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Sujeito, predicado e ordem sintática": 20
- "2. Complementos verbais, transitividade e vozes": 20
- "3. Complemento nominal, adjunto adnominal e predicativos": 20
- "4. Adjunto adverbial, aposto, vocativo e pronomes em uso": 20
- "5. Funções no período composto, coordenação, subordinação e foco textual": 20

Já existem (não repita):
- Em uma manchete lê-se: 'Chegaram novos documentos durante a madrugada'. Qual é a função de
- Considere: 'Os corredores estavam silenciosos depois do aviso'. Qual classificação se apli
- Em 'Houve três reclamações ontem', como se analisa o sintagma 'três reclamações' segundo a
- Na oração 'A pesquisadora leu o parecer cuidadosamente', o termo 'o parecer' exerce qual f
- Leia: 'Os participantes confiaram nas informações apresentadas'. O sintagma 'nas informaçõ
- Em 'Na terça-feira, a equipe apresentou os resultados', que função desempenha 'Na terça-fe
- Em 'Luísa, revisora experiente, corrigiu os trechos', a expressão entre vírgulas classific
- Considere a solicitação 'Amigos, enviem suas sugestões hoje'. Qual a função de 'Amigos'?
- Na frase 'Os leitores consideraram a explicação insuficiente', qual a função da palavra 'i
- Aparece em comunicado: 'Precisa-se de colaboradores com experiência'. Qual análise sintáti
- Uma notícia informa: 'A fiscalização dos serviços foi intensificada'. Sabendo que os servi
- Em 'Divulgaram-se os dados revisados', o verbo concorda com 'dados'. Qual interpretação do
- Observe 'Os participantes voltaram animados do encontro'. Qual classificação exprime simul
- Compare 'Obedeceram às orientações' e 'Trabalharam à noite'. Qual distinção é necessária, 
- Em 'Os autores eram favoráveis à publicação do relatório', qual alternativa descreve corre
- Analise 'O texto que chegou ontem foi revisado'. No interior da oração 'que chegou ontem',
- Em 'O parecer que a equipe analisou chegou cedo', que função cabe ao 'que' dentro da oraçã
- No enunciado 'É importante que todos participem', qual função a oração 'que todos particip
- Compare duas placas: I. 'Procuram-se revisores experientes'; II. 'Precisa-se de revisores 
- Uma manchete diz 'Surgem novas dúvidas após a entrevista'. Ao reescrevê-la como 'Novas dúv
- Considere 'A equipe tinha certeza de que o resultado seria divulgado'. Qual é a função da 
- Uma mensagem afirma: 'Aos destinatários, entregaram-lhes os comprovantes'. Na construção, 
- Em correspondência, lê-se 'Vossa Senhoria receberá sua cópia amanhã'. Qual observação asso
- Compare 'Informaram que a reunião terminou' e 'A reunião terminou, mas o relatório não che
- Em relatório, aparece 'A crítica dos estudantes aos regulamentos foi contundente'. O conte
- Na frase 'Chegaram os formulários solicitados', o grupo 'os formulários solicitados' funci
- Na redação 'Houveram muitas dúvidas durante a exposição', a concordância plural é adequada
- Em 'Os atletas chegaram cansados', 'chegaram' exprime acontecimento e 'cansados' atribui e
- Em 'Carla, entregue o relatório', o termo 'Carla' exerce função de sujeito simples, pois i
- Na expressão 'aquelas duas pequenas caixas', as palavras 'aquelas', 'duas' e 'pequenas' ca
- Na frase 'Enviei-lhe o aviso', o pronome 'lhe' é objeto direto e a expressão 'o aviso' con
- Em 'Sofia, minha vizinha, organizou a reunião', 'minha vizinha' é aposto explicativo, por 
- O acento grave é obrigatório em 'Entreguei o documento à ela', porque o verbo 'entregar' s
- Em 'Os relatórios daquela equipe chegaram', a expressão 'daquela equipe' determina o subst
- Na frase 'A defesa do acusado foi convincente', se o contexto explicita que o acusado se d
- No período 'A mensagem que li desapareceu', o relativo 'que' desempenha papel de objeto di
- Em 'Telefonaram antes do encontro', sem antecedente que permita identificar quem telefonou
- Considere a reescrita 'Esses resultados, ninguém os esqueceu'. O pronome 'os' retoma 'esse
- Em 'A ata foi redigida pela equipe', 'pela equipe' é sujeito gramatical porque indica quem
- No período 'Permanece a esperança de que a situação melhore', a oração 'de que a situação 
- Em 'Quando o alarme soou, os visitantes saíram', a oração inicial é coordenada adversativa
- Na frase 'Julgaram o método inadequado', 'inadequado' é predicativo do objeto 'o método', 
- No enunciado 'Sou favorável à decisão e trabalhei à tarde', os dois termos com crase são o
- Nas manchetes 'Crescem reclamações' e 'Reclamações crescem', a mudança de ordem altera a d
- Em toda oração subordinada adjetiva introduzida por 'que', o pronome relativo desempenha f
```

## Pedido 39: Subordinação entre orações e entre termos (`portugues.subordinacao`, 1767 tópicos de edital)

```
Matéria: portugues.subordinacao (Subordinação entre orações e entre termos)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Relações sintáticas e termos da oração": 20
- "2. Orações subordinadas substantivas e conectivos integrantes": 20
- "3. Subordinadas adjetivas e sintaxe dos pronomes relativos": 20
- "4. Subordinadas adverbiais e valores semântico-sintáticos": 20
- "5. Orações reduzidas, desenvolvimento e reescrita em provas": 20

Já existem (não repita):
- Assinale o período em que a oração subordinada exerce função de sujeito.
- Assinale a frase em que a oração iniciada por que completa um verbo como objeto direto.
- Assinale a frase em que a oração subordinada funciona como objeto indireto.
- Assinale a frase em que a oração subordinada completa o sentido de um nome.
- Assinale o período em que a oração adjetiva restringe o grupo indicado pelo antecedente.
- Assinale o período em que a subordinada adverbial expressa tempo.
- Assinale a frase em que a expressão chefe de plantão integra um agente da passiva.
- Assinale a frase em que há vocativo.
- Assinale o período em que a oração introduzida por como expressa a causa da ação principal
- Assinale a frase em que que é pronome relativo.
- Assinale a alternativa em que o termo destacado entre colchetes funciona como complemento 
- Em ‘O relatório que o inspetor redigiu ontem foi enviado à diretoria’, o pronome relativo 
- Assinale o período em que a oração subordinada funciona como predicativo do sujeito.
- Assinale a frase em que a oração subordinada substantiva exerce função de aposto.
- Assinale o período em que o segmento introduzido por que funciona como complemento nominal
- Assinale o período em que a oração adjetiva acrescenta explicação sobre todo o antecedente
- Assinale o período em que a oração subordinada expressa concessão.
- Assinale o período em que a oração subordinada apresenta uma condição para a realização da
- Assinale a frase em que a oração subordinada adverbial expressa consequência.
- Assinale a frase em que a oração subordinada expressa conformidade com uma norma.
- Assinale a frase em que se introduz uma oração subordinada substantiva em pergunta indiret
- Assinale a frase em que se é conjunção integrante, e não conjunção condicional.
- Assinale a alternativa em que o pronome relativo cujo está empregado em total conformidade
- No período ‘Constatou-se que os registros de entrada haviam sido alterados no sistema’, a 
- Assinale a oração reduzida de infinitivo que expressa finalidade.
- Assinale a frase em que a oração reduzida de gerúndio expressa tempo.
- Assinale o período em que uma oração reduzida de particípio expressa uma circunstância tem
- Assinale a frase em que há oração subordinada adverbial comparativa.
- Assinale o período em que duas ações variam proporcionalmente.
- Assinale a alternativa em que a oração desenvolvida expressa finalidade, preservando o sen
- Assinale a alternativa em que a oração reduzida de gerúndio tem valor condicional e pode s
- Assinale a frase em que o pronome relativo, precedido da preposição exigida, exerce função
- Assinale a frase em que a expressão intercalada funciona como aposto explicativo.
- Assinale a frase em que a oração subordinada adverbial expressa uma causa real para uma aç
- Compare os períodos: I. ‘A garantia de que o contrato seria cumprido acalmou os fornecedor
- No período ‘Posto que as provas documentais fossem robustas, a comissão sindicante solicit
- Em ‘É possível que os técnicos retornem antes do fechamento’, a oração iniciada por que ex
- Em ‘O fiscal verificou que o portão estava fechado’, a oração introduzida por que é objeti
- Em ‘A necessidade de que todos assinem foi comunicada aos servidores’, a oração completa o
- Em ‘O agente a quem o supervisor telefonou chegou cedo’, quem é conjunção integrante e não
- Em ‘Os moradores, que receberam o aviso, deixaram o prédio’, as vírgulas apresentam a info
- Em ‘O equipamento que apresentou defeito foi recolhido ao laboratório’, o vocábulo que exe
- No período ‘O expediente foi encerrado mais cedo porque faltou energia elétrica no prédio’
- Na oração ‘Havia muitos processos pendentes na repartição’, o termo muitos processos pende
- Em ‘Caso o ônibus atrase, a equipe iniciará a reunião sem o motorista’, a oração inicial a
- Em ‘A chefia perguntou se a porta lateral permanecia aberta’, se é conjunção condicional p
- Em ‘Ainda que o corredor estivesse escuro, os agentes localizaram a saída’, a oração subor
- A oração reduzida de infinitivo em ‘Para conferir os dados, a servidora abriu o sistema’ e
- Uma oração adjetiva explicativa vem isolada por vírgulas e acrescenta informação sobre o a
- No período ‘A diretoria comunicou aos chefes de seção, que o novo sistema de ponto eletrôn
- Em ‘A construção do novo viaduto pela prefeitura aliviou o trânsito local’, o termo do nov
- No período ‘Mal o presidente da comissão declarou aberta a sessão, os advogados pediram a 
- Toda oração reduzida de gerúndio expressa necessariamente tempo e pode ser desenvolvida ap
- Em ‘Segundo orienta o manual, os formulários devem ser enviados pelo portal’, a oração sub
- Onde pode retomar qualquer antecedente abstrato, mesmo sem noção de lugar, e substituir se
- Em contexto de sucessão de ações, ‘Concluído o atendimento, a servidora fechou o guichê’ p
- O vocativo integra o sujeito da oração porque nomeia a pessoa a quem o enunciado se dirige
- Na frase ‘A decisão da corte deve ser acatada, na medida em que se fundamenta estritamente
- No período ‘Os peritos examinaram todos os computadores da seção, o que permitiu rastrear 
- Em ‘Os auditores é que identificaram a inconsistência contábil no balanço’, há período com
```

## Pedido 40: Direitos e deveres individuais e coletivos (art. 5º) (`direito-constitucional.direitos-individuais-coletivos`, 1755 tópicos de edital)

```
Matéria: direito-constitucional.direitos-individuais-coletivos (Direitos e deveres individuais e coletivos (art. 5º))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos, destinatários, igualdade e tratados de direitos humanos": 21
- "2. Liberdades, privacidade, dados pessoais e inviolabilidades": 17
- "3. Reunião, associação, propriedade e direitos de participação": 18
- "4. Legalidade penal, prisão e garantias do processo": 25
- "5. Remédios constitucionais, assistência jurídica e proteção efetiva": 19

Já existem (não repita):
- Uma pessoa recebe ordem de deixar de exercer atividade lícita por mera vontade de quem fis
- Após manifestação pública ofensiva à imagem de uma pessoa, foi oferecido espaço para respo
- Durante a madrugada, há incêndio em uma residência e risco imediato à vida de seus ocupant
- Organizadores programam manifestação pacífica e sem armas em praça pública. Outra reunião 
- Uma associação regularmente constituída enfrenta pedido de suspensão de atividades, embora
- Em enchente grave, autoridade competente utiliza temporariamente um imóvel particular para
- Uma lei posterior reduz a sanção de fato cometido anteriormente. Qual disposição do art. 5
- Uma pessoa sofre ameaça concreta de prisão ilegal que colocará em risco sua liberdade de i
- Um cadastro privado expõe dados pessoais de consumidores, inclusive armazenados fora de am
- Um tratado sobre direitos humanos é aprovado em cada Casa legislativa, mas apenas por maio
- Uma pessoa invoca convicção filosófica para não cumprir dever legal imposto a todos. Exist
- Em investigação criminal, pretende-se captar conteúdo de ligações telefônicas mediante int
- Autoridade pretende retirar definitivamente bem particular por utilidade pública mediante 
- Questão afirma que racismo, tortura e crimes hediondos são todos imprescritíveis porque sã
- Uma pessoa é presa em flagrante, mas se constata que a prisão foi ilegal desde a origem. Q
- O exercício de direito previsto na Constituição tornou-se inviável pela ausência de norma 
- Uma associação constituída há dois anos impetra mandado de segurança coletivo em defesa de
- Uma família trabalha pequena propriedade rural definida legalmente. Credor pede sua penhor
- Uma pessoa investigada está sujeita a prisão preventiva fundamentada em requisitos legais 
- Uma pessoa teve pedido administrativo para conhecer seus próprios registros negado por ent
- Uma pessoa jurídica pretende propor ação popular para anular ato lesivo à moralidade admin
- Autor de obra literária pede proteção para sua utilização e reprodução; inventor de tecnol
- Uma base guarda nome, identificadores e histórico de consumidores. Argumenta-se que o art.
- Um depositário infiel é ameaçado de prisão civil exclusivamente por descumprir obrigação d
- Em procedimento sancionador administrativo, a parte afetada não teve ciência adequada da a
- As normas definidoras de direitos e garantias fundamentais possuem aplicação imediata, con
- O direito à proteção dos dados pessoais do art. 5º, LXXIX, foi estabelecido apenas para da
- A liberdade constitucional de reunião pacífica e sem armas em local aberto ao público inde
- O art. 5º, XIX, exige decisão judicial transitada em julgado tanto para a suspensão de ati
- A lei penal posterior pode retroagir para beneficiar o réu, embora, como regra, a definiçã
- A Constituição só assegura contraditório e ampla defesa a litigantes de processos judiciai
- Habeas corpus é remédio voltado a coação ilegal atual ou ameaçada sobre liberdade de locom
- Estrangeiro que esteja temporariamente no Brasil, sem residência estabelecida, não pode in
- Invocar crença para descumprir obrigação imposta a todos e recusar prestação alternativa f
- No art. 5º, XXV, a requisição em perigo público iminente obriga pagamento prévio em dinhei
- O direito de resposta proporcional ao agravo do inciso V não afasta eventual indenização p
- Racismo e crimes definidos como hediondos recebem do art. 5º a mesma disciplina explícita 
- Tratado de direitos humanos aprovado, em cada Casa do Congresso, em dois turnos, por três 
- O mandado de segurança individual pode ser requerido a qualquer tempo depois da ciência do
- A garantia de não culpabilidade do inciso LVII não impede toda prisão cautelar antes do tr
- A Súmula Vinculante 25 preserva prisão civil de depositário infiel decorrente de depósito 
- O art. 5º, XXI, exige autorização expressa para representação associativa comum, mas o man
- Uma pessoa jurídica pode propor ação popular por ser titular de direitos fundamentais comp
- O art. 5º, LXXIII, assegura ao autor popular isenção de custas judiciais e ônus da sucumbê
- O habeas data previsto no art. 5º, LXXII, é o instrumento próprio para obter qualquer info
```

## Pedido 41: Ferramentas colaborativas e nuvem para o usuário (`informatica.ferramentas-colaborativas`, 1744 tópicos de edital)

```
Matéria: informatica.ferramentas-colaborativas (Ferramentas colaborativas e nuvem para o usuário)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos da colaboração e arquivos em nuvem": 15
- "2. Google Drive e produção colaborativa em documentos, planilhas e apresentações": 24
- "3. Comunicação, agenda, tarefas e ambiente educacional Google": 23
- "4. Microsoft 365: aplicativos, OneDrive, SharePoint e Teams": 17
- "5. Reuniões online, ferramentas legadas e segurança da colaboração": 21

Já existem (não repita):
- Uma equipe revisa um relatório em horários diferentes: uma pessoa registra comentários às 
- Uma pessoa envia do computador uma apresentação para seu armazenamento remoto e, depois, o
- Um responsável deseja que um colega visualize um documento no Google Drive, mas não altere
- Uma pessoa pretende retirar uma conversa da Caixa de entrada do Gmail, preservando-a na co
- No Microsoft Teams de trabalho, uma apresentação foi enviada como arquivo à conversa priva
- Durante uma reunião no Google Meet, uma pessoa compartilha uma janela que mostra uma plani
- Um arquivo armazenado na nuvem foi alterado por engano ontem. O usuário possui autorização
- Uma pessoa quer organizar um compromisso com três colegas, definir início e término e acre
- Um grupo de trabalho quer disponibilizar relatórios numa biblioteca associada a um canal p
- Em uma turma virtual, cada estudante deve preencher sua própria cópia de um documento, sem
- Uma colaboradora possui permissão de editor em uma pasta do Meu Drive. O proprietário tent
- Um participante recebe por e-mail um link para documento cujo acesso está limitado a quatr
- Uma pessoa pretende acompanhar a data de entrega de uma atividade pessoal e visualizar ess
- Um relatório em Word, salvo no OneDrive organizacional, teve parágrafos excluídos indevida
- Um edital antigo menciona o Skype como plataforma de videoconferência. Ao preparar materia
- O administrador de uma reunião no Cisco Webex quer impedir que participantes apresentem a 
- Duas pessoas editaram uma mesma apresentação em Google Slides e desejam saber quem fez alt
- Uma equipe usa Drive com sincronização ativa no notebook e no desktop. Um arquivo foi apag
- Uma pessoa participa de um canal privado de uma equipe no Teams. Outra, que integra a equi
- Uma reunião foi agendada no Google Agenda com documento do Drive anexado. Uma pessoa convi
- Uma área de trabalho utiliza uma minuta central no Docs. Um revisor deseja propor substitu
- Um usuário mantém no Google Keep uma nota com lista de providências e um lembrete datado. 
- Um responsável cria arquivos em um canal padrão do Teams e depois envia, por chat, um docu
- Antes de uma reunião Zoom sobre documentos reservados, o anfitrião pretende controlar a en
- Após apresentar um documento em videoconferência, um colaborador remove um participante do
- Uma equipe produz um documento com edições simultâneas em um mesmo arquivo online. A ativi
- Ao configurar um arquivo do Meu Drive com o papel de Comentador para um revisor, o proprie
- Uma mensagem arquivada no Gmail pode continuar aparecendo em resultados de pesquisa, embor
- Um arquivo de texto enviado a um canal padrão do Teams de trabalho é armazenado normalment
- Uma pessoa que compartilha sua tela no Zoom para explicar um relatório não concede automat
- Uma professora publica uma atividade individual no Google Sala de Aula com a opção que per
- No Google Agenda, uma pessoa pode criar um evento, adicionar convidados e incluir um link 
- Uma pessoa exclui um documento numa pasta sincronizada com a nuvem. Como a sincronização é
- Uma pessoa com acesso de editor aos Documentos Google pode, quando o histórico pertinente 
- No Microsoft 365, o Word é o editor principal de apresentações, enquanto o PowerPoint orga
- Uma pessoa usa o Google Keep para registrar um checklist e configurar um lembrete; o recur
- Um usuário do Teams envia em um chat empresarial um arquivo do Word; por definição, todo a
- Em reunião Webex, a possibilidade de um participante compartilhar conteúdo pode depender d
- Quando o proprietário de um arquivo do Meu Drive concede a alguém acesso de leitor no próp
- Um professor que quer preservar produções individuais deve distinguir, ao anexar arquivo n
- Uma instituição encerrou a participação de uma pessoa num evento do Google Agenda. Como el
- Uma pessoa que integra a equipe principal do Teams, mas não participa de determinado canal
- Para um usuário comum em outubro de 2026, o Skype de consumo permanece em operação como an
- O histórico de versões disponível a editor autorizado pode auxiliar na recuperação de docu
- Como o compartilhamento de tela em Google Meet ou Zoom permite que todos acompanhem o mesm
```

## Pedido 42: Organização político-administrativa e entes federativos (`direito-constitucional.organizacao-politico-administrativa`, 1696 tópicos de edital)

```
Matéria: direito-constitucional.organizacao-politico-administrativa (Organização político-administrativa e entes federativos)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Federação, autonomia dos entes e limites do art. 19": 20
- "2. União e Estados: patrimônio e reorganização territorial": 26
- "3. Municípios: lei orgânica, fiscalização e reorganização em 2026": 21
- "4. Distrito Federal: natureza federativa e lei orgânica": 17
- "5. Territórios Federais: organização, representação e fiscalização": 16

Já existem (não repita):
- Um governante estadual declara que seu Estado possui soberania internacional por elaborar 
- Uma proposta lista União, Estados, Distrito Federal, Municípios e Territórios como cinco e
- Uma prefeitura rejeita certidão pública estadual apenas porque não foi expedida pelo própr
- Um rio percorre territórios de dois Estados brasileiros. Qual titularidade é prevista no a
- O Estado pretende editar norma própria de organização institucional. Qual instrumento orga
- Um Município prepara sua lei orgânica e pretende respeitar o procedimento formal do art. 2
- Uma proposta estadual institui região metropolitana formada por Municípios limítrofes para
- Um projeto propõe transformar região administrativa do Distrito Federal em Município por l
- Um Território Federal, caso instalado, possui contas anuais do Governo territorial. A quem
- A população diretamente interessada apoia a subdivisão de um Estado para formar outro. Qua
- Após a LC nº 230/2026, dois Municípios vizinhos discutem transferir parte da área de um pa
- Uma comissão propõe plebiscitos separados nos dois Municípios da LC nº 230/2026, contando 
- Para plebiscito da LC nº 230/2026 coincidir com eleição municipal ordinária, em regra, qua
- O órgão competente emitiu parecer prévio sobre as contas anuais de um Prefeito. Que quorum
- Uma lei do Distrito Federal disciplina assunto que, em Estado, seria legislado pelo Municí
- Para finalizar transferência de área entre Municípios limítrofes nos termos da LC nº 230/2
- Uma lei estadual declara próprios todos os minerais de jazidas localizadas em seu territór
- Um Estudo de Viabilidade Municipal da LC nº 230/2026 apresenta apenas projeção de arrecada
- Um desmembramento entre Municípios pela LC nº 230/2026 é aprovado por lei estadual no exer
- Uma futura unidade territorial federal alcança 120 mil habitantes. Qual consequência insti
- Uma norma distrital pretende criar Município para administrar uma região já existente, arg
- A assembleia estadual pretende convocar plebiscito de transferência municipal concomitante
- A fiscalização de Município é contestada porque a Câmara deseja afastar o parecer prévio p
- Uma região territorial será incorporada ao Município vizinho pela LC nº 230/2026. Como se 
- Uma corrente de água atravessa apenas um Estado e não se enquadra nas hipóteses federais; 
- Os Estados-membros são autônomos segundo o art. 18 da Constituição, enquanto a soberania p
- Os Territórios Federais são integrantes autônomos da federação e figuram expressamente ent
- O art. 19, II, da Constituição veda aos entes federativos recusar fé a documentos públicos
- Por força do art. 26, IV, todos os recursos minerais presentes em terras devolutas estadua
- Uma lei orgânica municipal requer dois turnos, intervalo mínimo de dez dias e aprovação de
- O Distrito Federal pode dividir suas regiões administrativas em Municípios mediante lei or
- O art. 33, § 1º, admite que os Territórios Federais sejam divididos em Municípios, aplican
- Uma proposta de desmembramento estadual aprovada em plebiscito pode ser concluída por lei 
- Pelo art. 25, § 3º, Estados podem instituir regiões metropolitanas mediante lei complement
- A LC nº 230/2026 é autorização geral para criar novos Municípios a partir de desmembrament
- O art. 3º da LC nº 230/2026 exige que o estudo de viabilidade examine dimensão econômico-f
- No desmembramento regulado pela LC nº 230/2026, o prazo ordinário de aprovação do decreto 
- Na redação do art. 31 após a EC nº 139/2026, o parecer prévio das contas do Prefeito só de
- As competências legislativas municipais atribuídas ao Distrito Federal pelo art. 32, § 1º,
- O art. 20, § 2º, considera faixa de fronteira a faixa de até 150 quilômetros ao longo das 
- Após aprovação de lei estadual em 2027 transferindo área entre Municípios nos termos da LC
- O procedimento de desmembramento entre Municípios previsto na LC nº 230/2026 fica suspenso
- O art. 33, § 3º, impõe a estrutura diferenciada de órgãos judiciários e defensorias federa
- Segundo o art. 31, § 3º, da Constituição, as contas dos Municípios ficam sessenta dias por
- Como a LC nº 230/2026 prevê um período de quinze anos, ela também regulamenta integralment
```

## Pedido 43: Administração pública (`direito-constitucional.administracao-publica`, 1671 tópicos de edital)

```
Matéria: direito-constitucional.administracao-publica (Administração pública)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Administração pública: princípios, alcance e organização constitucional": 28
- "2. Acesso ao serviço público, remuneração e acumulação": 19
- "3. Responsabilidade, integridade, controle administrativo e regras especiais do art. 37": 16
- "4. Concurso, funções, acumulação e limites remuneratórios: aplicações difíceis": 18
- "5. Direitos dos servidores, RPPS, estabilidade, militares estaduais e regiões": 19

Já existem (não repita):
- Uma secretaria estadual pretende usar verba institucional para publicar um anúncio destaca
- Um tribunal elabora a rotina de atendimento ao público e afirma que apenas o Poder Executi
- Uma unidade administrativa será transformada em autarquia, enquanto outra proposta cria em
- Uma chefia pretende subdividir um departamento em duas diretorias, mantendo tudo dentro da
- Uma entidade pretende condicionar acesso a informação administrativa comum à divulgação pú
- Uma chefia propõe nomear pessoa sem cargo efetivo para exercer função de confiança, sob a 
- Um concurso estabelece validade inicial de dezoito meses. A administração estuda como pror
- Uma pessoa aprovada solicita investidura em cargo efetivo sem prova alguma, alegando possu
- Uma servidora com horários compatíveis foi eleita vereadora. Qual tratamento do art. 38 se
- Uma lei criou muitos cargos em comissão, destinados a atividades rotineiras de conferência
- Dois vínculos públicos apresentam horários compatíveis: um cargo efetivo de professor e ou
- Uma empresa estatal recebe transferências públicas para pagar despesas gerais de pessoal. 
- Após completar três anos de efetivo exercício em cargo efetivo obtido por concurso, um ser
- Um servidor estável sofre demissão que, anos depois, é invalidada judicialmente. Qual inst
- Uma entidade de direito privado presta serviço público e seu agente, nessa condição, causa
- Uma chefia pretende conceder reajuste específico aos ocupantes de cargos efetivos por simp
- Uma sociedade de economia mista passa a receber recursos do estado destinados ao pagamento
- Um candidato consta apenas em cadastro de reserva. Durante a validade do concurso surge ou
- Duas pessoas exercem cargos públicos compatíveis em horários. Uma ocupa cargo efetivo de p
- Na aplicação da EC 135/2024 ao teto remuneratório, uma unidade analisa as parcelas indeniz
- Um servidor efetivo concursado completa três anos de efetivo exercício. A administração de
- Um agente de concessionária, atuando na prestação de serviço público, causa dano comprovad
- Após adquirir limitação física permanente que impede parte das atribuições de origem, um s
- Uma pessoa recebe proventos de aposentadoria vinculados ao art. 40 e pretende assumir carg
- A União busca articular ações em uma região geoeconômica para reduzir desigualdades e pret
- Uma autarquia municipal deve respeitar legalidade, impessoalidade, moralidade, publicidade
- A criação de uma nova secretaria, sem instituir pessoa jurídica distinta, configura descen
- A validade constitucional de um concurso público pode ser fixada em um ano e, respeitadas 
- Todo cargo em comissão deve ser ocupado exclusivamente por servidor efetivo, porque a Cons
- Servidora ocupante de cargo público eleita prefeita deverá afastar-se do vínculo de origem
- A estabilidade do ocupante de cargo efetivo oriundo de concurso é adquirida pelo simples f
- Após a EC 138/2025, admite-se, com horários compatíveis e teto observado, acumular remuner
- A condição de pessoa jurídica privada, por si só, afasta a responsabilidade objetiva do ar
- O art. 41 prevê perda de cargo por servidor estável após sentença judicial transitada em j
- A decisão de mérito da ADI 2.135 reafirmou ser obrigatória, no presente, a adoção de um ún
- Um órgão público que realiza avaliação de uma política pública deve divulgar, na forma da 
- O contrato de metas de desempenho previsto no art. 37, § 8º, permite ampliar a autonomia a
- A vedação do art. 37, § 9º, alcança sociedade de economia mista que recebe recursos públic
- O descumprimento da exigência de concurso prevista no art. 37, II, mantém a investidura vá
- Aos militares dos estados, do Distrito Federal e dos territórios aplica-se o disposto no a
- Se a demissão de servidor estável for invalidada por sentença judicial, o art. 41, § 2º, e
- A concessão de aposentadoria com utilização de tempo de contribuição de um vínculo público
- A Constituição permite que a União articule sua ação em complexo geoeconômico e social par
- Por exercer atividade em entidade integrante da administração indireta, todo empregado púb
- Antes da edição da lei ordinária nacional referida no art. 37, § 11, o art. 3º da EC 135/2
```

## Pedido 44: Argumentação lógica (`matematica.argumentacao-logica`, 1654 tópicos de edital)

```
Matéria: matematica.argumentacao-logica (Argumentação lógica)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Estrutura do argumento, validade e verdade": 17
- "2. Regras de inferência e silogismos categóricos": 19
- "3. Dedução, indução, abdução e analogia": 17
- "4. Falácias formais, informais e argumentos apelativos": 29
- "5. Construção e avaliação crítica de argumentos": 18

Já existem (não repita):
- Uma equipe registrou: 'A análise precisa ser refeita, pois duas medições apresentaram resu
- Em uma discussão, alguém afirma: 'Todos os barcos são feitos de vidro; esta embarcação é u
- Uma regra estabelece: 'Se a solicitação foi aprovada, a confirmação será enviada'. A solic
- Foi estabelecido: 'Se o pacote passou pela inspeção, recebeu o selo verde'. Observou-se qu
- Considere: 'Todo livro raro é catalogado; todo item catalogado consta do inventário'. Qual
- Uma relação informa que 'alguns estudantes participam do laboratório'. Qual leitura preser
- Depois de observar dez reuniões pontuais, uma pessoa prevê que a próxima reunião do grupo 
- As luzes de várias salas se apagaram de uma só vez; alguém sugere falha no quadro elétrico
- Uma técnica de ensino funcionou em uma turma. Pretende-se aplicá-la a outra por analogia. 
- Sabe-se que o relatório está no arquivo físico ou no repositório digital e que não está no
- Um aviso afirma: 'Se o sensor detecta fumaça, a sirene toca'. Como a sirene tocou, alguém 
- Uma norma interna diz: 'Se a etiqueta é vermelha, a peça precisa de revisão'. A etiqueta n
- Em uma pesquisa: 'Alguns participantes são tradutores; todos os tradutores dominam outra l
- Um relatório apresenta: 'Todo programador usa identificador; toda pessoa visitante usa ide
- Ao discutir horários de atendimento, alguém declara: 'Ou ampliamos o expediente para dez h
- Uma pesquisadora apresenta dados sobre consumo de água. Um opositor responde: 'A proposta 
- Uma pessoa defende revisar apenas itens com erros recorrentes. A resposta é: 'Você quer in
- Uma escola pretende repetir em outra unidade um projeto de leitura bem-sucedido. As unidad
- Após falhas de acesso em vários computadores, foi proposta a hipótese de interrupção no se
- Após instalar um aplicativo, uma equipe observou aumento de produtividade e concluiu que a
- Argumenta-se: 'A sala poderá ser utilizada amanhã, pois não consta reserva no sistema'. Qu
- Considere: 'A emissão da credencial é condição necessária para entrar no laboratório; conc
- São verdadeiras as premissas: 'Alguns atletas são músicos' e 'Alguns músicos são professor
- Uma apresentação sustenta: 'Todo objeto da coleção é metálico; esta peça pertence à coleçã
- Um estudo avaliou voluntários selecionados entre frequentadores assíduos e concluiu que to
- Durante uma reunião, a pessoa afirma: 'O relatório precisa de ajustes, porque os totais da
- Uma pessoa conclui que seu argumento é dedutivamente válido porque a conclusão foi confirm
- Uma regra afirma que, se o arquivo foi autorizado, ele será publicado. Confirmada a autori
- Um dispositivo emite alerta quando detecta vibração excessiva. Como o alerta foi emitido, 
- Em um inventário, todo mapa digital é arquivo e todo arquivo pertence ao acervo. A conclus
- Um levantamento informa que 'alguns usuários utilizam o aplicativo'. Em interpretação lógi
- Uma norma estabelece: 'Se o material é inflamável, exige armazenamento especial'. Um mater
- As afirmações 'nenhum pintor da mostra é escultor' e 'alguns participantes da mostra são p
- Um documento foi arquivado na pasta A ou na pasta B, e a verificação descartou a pasta A. 
- Uma coordenadora declara: 'Ou o relatório é apresentado oralmente, ou é entregue por escri
- Após observar grande número de atendimentos bem avaliados, uma pessoa prevê boa avaliação 
- Do êxito de um procedimento em uma unidade, infere-se seu êxito em outra com condições sem
- Várias máquinas param simultaneamente; a hipótese de interrupção da alimentação elétrica c
- Uma pessoa afirma: 'Ou desligamos toda a rede por uma semana ou continuaremos expostos a a
- Para defender uma proposta, o expositor argumenta: 'A decisão é correta porque constitui a
- Uma turma melhorou suas notas após a troca do material de estudo. Sem comparar turmas, háb
- Uma proposta sobre ventilação é rejeitada porque a autora tem gosto musical considerado ru
- São verdadeiras as premissas 'todos os violinistas do grupo são artistas' e 'alguns artist
- A frase 'Só é possível concluir a etapa final se o formulário estiver completo' estabelece
- Diante de lentidão e travamentos, um analista considera superaquecimento a explicação mais
```

## Pedido 45: Tendências pedagógicas, políticas educacionais e coordenação pedagógica (`educacao.tendencias-pedagogicas-politicas-educacionais`, 1607 tópicos de edital)

```
Matéria: educacao.tendencias-pedagogicas-politicas-educacionais (Tendências pedagógicas, políticas educacionais e coordenação pedagógica)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Educação, sociedade e fundamentos da prática pedagógica": 23
- "2. Tendências pedagógicas e concepções de ensino": 29
- "3. Currículo, inclusão, tecnologias e formação integral": 13
- "4. Gestão democrática, PPP, planejamento e coordenação pedagógica": 17
- "5. Políticas educacionais, financiamento, avaliações e integração das ações": 18

Já existem (não repita):
- Uma escola amplia o atendimento em turno alternativo e cria oficinas de debate cívico, inv
- Uma estudante resolve uma tarefa de interpretação com perguntas orientadoras de uma colega
- Em um projeto sobre produção de alimentos, estudantes analisam ferramentas, relações de tr
- Depois de verificar dificuldades de leitura em diferentes turmas, uma equipe revisa ativid
- Uma escola convida estudantes a discutir a reorganização de espaços de convivência. A dire
- Após identificar baixa participação de um grupo nas aulas, uma professora adapta materiais
- Como a Constituição prevê qualificação para o trabalho, uma escola pode considerar encerra
- Uma investigação sobre desigualdade de acesso à biblioteca pode considerar simultaneamente
- Se a turma alcança bom resultado em uma atividade mediada por colegas, esse resultado prov
- Um professor dedica a maior parte das aulas à exposição sistemática de conteúdos, solicita
- Um curso organiza o ensino em objetivos comportamentais, materiais de execução padronizada
- Numa roda de estudos, estudantes identificam problemas de saneamento do bairro, debatem ex
- Ao estudar uma questão de moradia, uma turma parte de percepções cotidianas, analisa conce
- Duas turmas desenvolvem projetos. Na primeira, estudantes conduzem coletivamente regras, r
- Uma aula com exposição de conteúdo e perguntas dirigidas pode integrar sequência de ensino
- Como a tendência renovada progressivista privilegia a experiência, o professor deixa de or
- Uma sequência de estudo com problemas da comunidade, leitura de fontes, confronto de inter
- Em uma proposta libertária, a presença de assembleias e autogestão é um indício secundário
- O documento curricular defende colaboração, mas as rotinas escolares premiam apenas compet
- Uma equipe analisa livros didáticos perguntando por que certos grupos sociais raramente ap
- Para abordar consumo de água, uma equipe planeja coleta de dados, leitura de mapas, explic
- Uma estudante com impedimento de comunicação oral participa de discussões por meio de recu
- Uma proposta de educação midiática solicita que estudantes rastreiem autores, comparem evi
- Ao trabalhar educação financeira em atividades de Matemática e de análise de publicidade, 
- As teorias curriculares pós-críticas examinam diferenças e identidades; por isso, eliminam
- Ao analisar o afastamento recorrente de estudantes com dificuldade de transporte, uma esco
- Quando duas disciplinas trabalham o mesmo tema em semanas próximas, há necessariamente int
- Uma escola precisa atualizar o PPP. Antes de escolher ações, abre escuta com famílias, est
- A equipe registra como prioridade ampliar a leitura autônoma e deseja definir responsáveis
- Um encontro de coordenação analisa produções escritas, identifica dificuldades comuns, est
- A gestão propõe que a comunidade vote entre duas prioridades, mas omite os dados necessári
- Em revisão do PPP, a maioria sugere retirar apoios de acessibilidade para liberar recursos
- O PPP deve expressar orientações construídas coletivamente e ser acompanhado e revisto; su
- A gestão democrática se concretiza quando a direção consulta os segmentos uma vez por ano;
- Um indicador de frequência crescente pode demonstrar melhora da presença dos estudantes, m
- Se docentes têm dificuldades comuns para ensinar resolução de problemas, a coordenação dev
- No planejamento de uma política educacional, um texto fixa como resultado elevar a proporç
- Um relatório combina proficiências de avaliação externa com taxas de aprovação do Censo Es
- Ao examinar a legislação em outubro de 2026, uma equipe precisa indicar o PNE em vigor. Qu
- Durante a revisão de despesas do Fundeb, uma equipe discute a subvinculação legal de recur
- Uma rede identifica crescimento da média de desempenho, mas queda importante das taxas de 
- O Saeb é uma avaliação externa em larga escala, ao passo que o Censo Escolar reúne informa
- Como a Lei nº 13.005/2014 teve sua vigência prorrogada até 2025, suas metas continuam cons
- Ao avaliar a execução do novo PNE, devem-se considerar indicadores e participação social, 
- O percentual de 23% de complementação da União ao Fundeb em 2026 significa que todas as es
```

## Pedido 46: Coordenação entre orações e entre termos (`portugues.coordenacao`, 1588 tópicos de edital)

```
Matéria: portugues.coordenacao (Coordenação entre orações e entre termos)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Período e independência sintática": 20
- "2. Orações coordenadas sindéticas": 20
- "3. Coordenação entre termos e pontuação": 20
- "4. Reescrita e como o tema costuma ser cobrado": 20
- "5. Casos especiais: correlação, elipse e fronteiras com a subordinação": 20

Já existem (não repita):
- Assinale a alternativa que apresenta período simples.
- Assinale o período composto por coordenação assindética.
- No período sobre a vistoria, assinale a classificação correta da segunda oração: A equipe 
- Assinale a alternativa em que a conjunção estabelece oposição entre as orações.
- Assinale a frase em que ou apresenta uma escolha entre procedimentos.
- Assinale a alternativa cuja oração iniciada por portanto expressa conclusão.
- Assinale a frase em que a oração coordenada justifica uma ordem.
- Assinale a alternativa em que e assume valor adversativo no contexto.
- Assinale a alternativa em que a vírgula separa corretamente uma oração adversativa.
- Assinale a opção em que há coordenação entre termos da oração, e não entre orações.
- Assinale a alternativa em que a locução verbal forma um único núcleo oracional, resultando
- Assinale a alternativa que apresenta par correlativo de valor aditivo.
- Assinale o período em que a oração introduzida por porque justifica uma ordem.
- Assinale a alternativa em que pois tem valor conclusivo por estar deslocado entre pausas.
- Assinale a reescrita que mantém o valor semântico de oposição: O acesso era estreito, mas 
- Assinale a alternativa que preserva o paralelismo entre os itens coordenados.
- Assinale a alternativa em que logo apresenta valor conclusivo.
- Assinale a opção em que a coordenação ocorre entre sujeitos compostos.
- Assinale a opção que pontua corretamente uma enumeração simples de complementos verbais.
- Assinale a substituição que preserva o sentido conclusivo: O rádio ficou sem bateria; port
- Assinale a alternativa em que a segunda oração é explicativa, e não causal.
- Assinale a alternativa em que pois tem valor conclusivo.
- Em ‘A coordenação administrativa analisou os recursos financeiros; a diretoria técnica, os
- Assinale a alternativa em que os dois-pontos estabelecem relação de explicação ou causa en
- Em qual alternativa o e pode ser substituído por mas sem alterar a relação expressa no con
- Assinale a frase em que a vírgula antes de e separa duas orações com sujeitos distintos.
- Assinale a reescrita que conserva contraste e pontuação adequada: O sistema estava lento, 
- Assinale a opção em que os dois termos coordenados exercem função de objeto direto.
- Assinale a frase em que o valor do conectivo está corretamente identificado.
- Assinale a reescrita que mantém a relação de justificativa da ordem: Não entrem no galpão,
- Assinale a alternativa em que há duas orações coordenadas e a segunda é aditiva, embora o 
- Assinale a análise correta de ‘O relatório foi concluído e a equipe deixou a sala’.
- Assinale a alternativa em que a conjunção coordena termos da oração, e não predicados ou o
- Assinale a opção em que a pontuação evita separar indevidamente sujeito e verbo.
- Considere o período: ‘O auditor fiscal conferiu os livros contábeis, mas não encontrou ind
- Assinale a alternativa em que o vocábulo ‘que’ atua como conjunção coordenativa explicativ
- Assinale a alternativa correta quanto ao emprego das conjunções ‘portanto’, ‘porquanto’ e 
- Assinale a alternativa impecável quanto ao paralelismo sintático e de regência entre termo
- O enunciado ‘A equipe deixou a base’ é uma oração e, como período, é simples.
- Em ‘O motorista chegou quando a sirene tocou’, as duas orações são coordenadas assindética
- Em ‘O setor revisou o cadastro e enviou a confirmação’, a conjunção e acrescenta uma segun
- Em ‘A equipe isolou o local; os técnicos avaliaram a estrutura’, existe coordenação sindét
- Na frase ‘O pedido foi protocolado, porém a análise ainda não começou’, porém introduz opo
- No período ‘O servidor não assinou o relatório nem apresentou justificativa’, a conjunção 
- Em ‘Atenção redobrada aos prazos processuais!’, há um período composto por coordenação ass
- Em ‘Os fiscais deveriam ter conferido os lacres ontem’, a expressão ‘deveriam ter conferid
- Na frase ‘O depósito foi fechado porque o alarme indicou fumaça’, a oração iniciada por po
- Em ‘Desliguem o equipamento, porque há superaquecimento’, porque introduz necessariamente 
- Em ‘O prazo encerrou; cabe, pois, registrar a pendência’, pois tem valor conclusivo e apar
- Em ‘A agente guardou fichas e carimbos’, fichas e carimbos são orações coordenadas, pois e
- Em ‘A vistoria foi breve, e o laudo ficou pronto’, a vírgula pode marcar a separação de or
- Na frase ‘O pátio externo está inteiramente molhado, porque choveu forte durante a madruga
- A conjunção ‘mas’ possui a mesma mobilidade sintática de ‘porém’ e ‘contudo’, podendo ser 
- Em ‘Os peritos criminais e os delegados plantonistas, assinaram o auto de vistoria’, a vír
- Na frase ‘A equipe foi avisada, pois o portão estava aberto’, pois é conclusivo por aparec
- Em ‘A rua estava alagada e os moradores atravessaram a praça’, e pode adquirir valor adver
- A coordenação entre termos, como em ‘O fiscal recolheu o crachá e o documento’, transforma
- Em ‘O acesso está livre, portanto os técnicos podem entrar’, portanto introduz uma conclus
- Em ‘O setor recebeu o ofício, mas não respondeu ao solicitante’, mas introduz uma oração c
- No período ‘O tribunal analisou as provas documentais; a defesa, porém, requereu nova perí
- Em ‘A comissão técnica exigia pontualidade, que os relatórios fossem detalhados e sigilo a
- Na frase ‘O sistema de segurança travou; porquanto, os portões eletrônicos permaneceram fe
- Em ‘A primeira turma de fiscais vistoriou os galpões industriais; a segunda, os estabeleci
```

## Pedido 47: Análise combinatória (`matematica.analise-combinatoria`, 1569 tópicos de edital)

```
Matéria: matematica.analise-combinatoria (Análise combinatória)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Princípios de contagem e organização de casos": 48
- "2. Fatorial e permutações simples e com repetição": 13
- "3. Arranjos, combinações e repetição permitida": 15
- "4. Contagens com restrições, união, complementar e distribuição": 12
- "5. Binômio de Newton e triângulo de Pascal": 12

Já existem (não repita):
- Uma loja oferece três modelos de camiseta e quatro modelos de calça. O comprador escolherá
- Uma pessoa vai optar por uma única atividade: há cinco passeios culturais ou três trilhas 
- Quatro livros diferentes devem ocupar quatro posições consecutivas de uma prateleira. Quan
- A palavra OVO tem duas letras O idênticas e uma letra V. Quantos anagramas distintos podem
- Cinco pessoas distintas concorrem às funções de coordenador e secretário. Uma mesma pessoa
- Seis estudantes distintos formarão uma comissão de dois membros, sem funções diferenciadas
- Um código usa três posições, cada uma preenchida por um dos cinco símbolos disponíveis. É 
- Um estudo usa os coeficientes da expansão de um binômio elevado ao cubo. Quanto vale a som
- Em um grupo de inscritos, nove praticam corrida, sete praticam ciclismo e quatro praticam 
- Usando os dígitos 0, 1, 2, 3, 4 e 5 sem repetição, quantos números de três algarismos pode
- A palavra BANANA tem seis letras, com três A e dois N. Quantos anagramas diferentes podem 
- Cinco pessoas distintas farão uma fila. Duas delas devem ocupar posições consecutivas. Qua
- Uma equipe de três entre oito pessoas deve conter obrigatoriamente duas pessoas específica
- Sete candidatos distintos disputarão três posições classificatórias ordenadas, sem empates
- Três bolas de sorvete serão escolhidas entre quatro sabores. Um sabor pode ser repetido, e
- Com os algarismos 1, 2, 3 e 4, sem repetição e utilizando os quatro, quantos números pares
- Na expansão de (x+2) elevado à quarta potência, qual é o coeficiente do termo que contém x
- Cinco pessoas distintas devem ocupar lugares em fila. Duas pessoas designadas não podem fi
- Uma comissão de três integrantes será escolhida entre oito pessoas. Pelo menos uma das dua
- Com dígitos de 0 a 5, sem repetir, serão formados números pares de três algarismos. Quanto
- Quatro pessoas distintas serão distribuídas em dois setores identificados, Alfa e Beta, ex
- Uma senha de quatro posições permite os dígitos 0, 1, 2, 3 e 4, com repetição. Exige-se a 
- No desenvolvimento de (x² + 1/x) elevado à sexta potência, qual é o coeficiente do termo i
- Em uma expansão de (x−2) elevado à quinta potência, pretende-se encontrar o coeficiente de
- Seis pessoas distintas serão separadas em dois grupos de três, sem nome para os grupos e s
- Uma loja oferece quatro chapéus e três lenços; quando a pessoa escolhe um chapéu e um lenç
- Se dois conjuntos de possibilidades possuem elementos em comum, a quantidade de elementos 
- O fatorial de zero é um, definição compatível com a contagem de uma única escolha vazia e 
- Ao organizar quatro livros todos distintos, existem dezesseis filas possíveis, porque a pe
- Para selecionar dois membros de um grupo de cinco, sem distribuir funções, o número de dup
- Uma senha de dois caracteres escolhidos entre seis símbolos com repetição permitida admite
- A soma dos coeficientes do desenvolvimento formal da quarta potência de um binômio é dezes
- Com os algarismos 0, 1, 2, 3 e 4, sem repetição, existem sessenta números de três algarism
- A palavra TATU admite doze anagramas distintos usando as quatro letras, pois a repetição d
- Ao selecionar três pessoas entre sete para exercer funções diferentes, a combinação simple
- Uma comissão de três entre seis pessoas, com presença obrigatória de duas pessoas predeter
- No desenvolvimento da quarta potência de (x+3), o coeficiente de x² é seis, pois basta cal
- Entre cinco pessoas distintas, se duas específicas devem ficar lado a lado em fila, o núme
- Em uma fila de cinco pessoas distintas, duas pessoas específicas devem permanecer separada
- Seis pessoas distintas podem ser separadas em dois grupos sem identificação, com três memb
- Senhas de quatro posições, preenchidas pelos símbolos 0, 1, 2, 3 e 4, com repetição, apres
- Se seis pessoas distintas ocupam lugares sem numeração em torno de mesa redonda e rotações
- Na expansão da sexta potência de (x² + 1/x), o termo independente de x tem coeficiente vin
- Se treze participantes escolheram a oficina A, dez escolheram a oficina B e cinco escolher
- Quando se escolhem três unidades entre quatro tipos disponíveis, podendo repetir o tipo e 
```

## Pedido 48: Responsabilidade na gestão fiscal (`afo.lei-responsabilidade-fiscal`, 1552 tópicos de edital)

```
Matéria: afo.lei-responsabilidade-fiscal (Responsabilidade na gestão fiscal)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos, abrangência e planejamento fiscal": 19
- "2. Receita pública, receita corrente líquida e renúncia": 18
- "3. Geração de despesas, pessoal e transferências voluntárias": 24
- "4. Dívida pública, operações de crédito e vedações fiscais": 16
- "5. Transparência, relatórios e fiscalização da gestão fiscal": 23

Já existem (não repita):
- Um ente detecta queda persistente de arrecadação e decide manter integralmente as despesas
- Uma empresa controlada recebe do ente apenas aporte destinado a aumentar a participação ac
- Ao preparar os documentos da LDO, a equipe separa metas de resultado fiscal e levantamento
- Encerrado um bimestre, conclui-se que a receita poderá comprometer a meta de resultado fis
- Após publicar a LOA, um ente prepara a programação financeira e o desdobramento da arrecad
- Um ente deixa de prever e arrecadar imposto de sua competência, embora possa instituí-lo r
- Para verificar um limite fiscal em agosto, a equipe calcula a receita corrente líquida. Qu
- Na proposta orçamentária, pretende-se alterar a receita estimada por discordância política
- Um benefício tributário reduzirá a arrecadação, mas seu efeito foi considerado na LOA e es
- Um projeto propõe prorrogar incentivo tributário a pessoas jurídicas, sem estimar benefici
- Uma nova ação de custeio aumentará despesa no próximo exercício. Qual documento e demonstr
- Uma norma cria despesa corrente obrigatória que se repetirá por quatro exercícios. Qual co
- Um contrato de terceirização substitui diretamente atividades antes exercidas por pessoal 
- A despesa total com pessoal ultrapassou o limite ao encerrar um quadrimestre. Sem regra es
- Um ente solicita cooperação financeira facultativa, mas deixou de prestar contas de repass
- Um ente contratou operação de crédito de curta duração, mas a respectiva receita constou d
- Ao fim de um quadrimestre, a dívida consolidada supera seu limite. Qual trajetória ordinár
- Uma proposta de endividamento prevê operações de crédito superiores às despesas de capital
- Um ente planeja antecipação de receita orçamentária para superar insuficiência de caixa no
- Após alienar um imóvel, o ente pretende usar a receita de capital para despesas correntes 
- Ao fim de um bimestre, a equipe precisa divulgar dados da execução orçamentária de todos o
- Após o encerramento de quadrimestre, pretende-se evidenciar despesa com pessoal, dívida, g
- Um município com população inferior a cinquenta mil habitantes avalia a opção legal de div
- Um portal disponibiliza apenas demonstrativos anuais, sem dados pormenorizados de execução
- Após terminar o exercício, dois entes organizam o encaminhamento das contas anuais e a pos
- Um ente pode tratar a responsabilidade fiscal como simples proibição de déficits, sem demo
- No projeto da LDO, o Anexo de Riscos Fiscais examina passivos contingentes e indica provid
- Após o encerramento de um bimestre com risco às metas, a limitação de empenho só pode ser 
- Após restabelecimento apenas parcial da arrecadação, as dotações anteriormente limitadas p
- A receita corrente líquida é apurada pelo somatório da receita corrente do mês de referênc
- Um ente que não institui imposto de sua competência pode ficar sujeito à restrição de tran
- A concessão de renúncia fiscal condicionada às medidas do art. 14, II, pode entrar em vigo
- Uma proposição de prorrogação de benefício tributário com renúncia para pessoa jurídica es
- Para aumentar despesa decorrente de ação governamental, a declaração do art. 16 trata de a
- Despesa corrente instituída normativamente para durar exatamente dois exercícios é, apenas
- Os pagamentos de terceirização que substitui pessoal próprio são excluídos da despesa com 
- A dispensa de compensação prevista para certas despesas de seguridade social no art. 24 af
- Uma confissão de dívida feita pelo ente pode ser equiparada a operação de crédito, para os
- O limite da dívida consolidada é aferido ordinariamente no encerramento de cada bimestre, 
- A regra de ouro impede toda operação de crédito destinada a financiar despesa corrente, me
- A operação de crédito por antecipação de receita, quitada no prazo do art. 38, II, não int
- O RREO é bimestral e o RGF é quadrimestral como regras gerais, embora certos municípios po
- O demonstrativo de cumprimento da regra de ouro acompanha exclusivamente o RGF do último q
- Um relatório fiscal foi publicado até trinta dias após o quadrimestre, mas não indicou as 
- Para verificar o cumprimento da LRF, o controle fiscal pode examinar simultaneamente o ati
```

## Pedido 49: Princípios da Administração Pública (`direito-administrativo.principios-administracao`, 1550 tópicos de edital)

```
Matéria: direito-administrativo.principios-administracao (Princípios da Administração Pública)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos, regime jurídico e os princípios constitucionais": 19
- "2. Legalidade, impessoalidade, moralidade, publicidade e eficiência": 24
- "3. Princípios e critérios da Lei nº 9.784/1999": 19
- "4. Razoabilidade, proporcionalidade, motivação e continuidade": 19
- "5. Autotutela, segurança jurídica e jurisprudência aplicada": 19

Já existem (não repita):
- Uma unidade administrativa do Poder Legislativo sustenta que o art. 37, caput, da Constitu
- Uma equipe revisa apostila e identifica a introdução expressa da eficiência no rol do art.
- Uma campanha pública apresenta informações úteis sobre atendimento, mas destaca repetidame
- Uma direção pretende acelerar processos, deixando de cumprir etapa legalmente obrigatória 
- Um avaliador compara o rol literal do art. 37, caput, com os princípios enumerados no art.
- Uma comissão registra que a Lei nº 9.784/1999 teria âmbito apenas no Poder Executivo feder
- Uma repartição exige procedimento documental complexo sem ganho de segurança, embora haja 
- Para evitar uso irregular de espaço público, uma regra fixa restrição apta a alcançar o ob
- Uma decisão que nega pretensão individual identifica fatos concretos e incorpora expressam
- Uma decisão administrativa considerada válida tornou-se inadequada por mudança supervenien
- Uma decisão concedeu benefício com efeitos favoráveis e sem má-fé demonstrada. A Administr
- Uma vantagem periódica foi concedida em janeiro e seu primeiro pagamento ocorreu em fevere
- Duas autoridades combinam designar reciprocamente seus parentes para funções gratificadas,
- Uma autoridade aplica novo entendimento restritivo a fatos já concluídos, apesar de a orie
- Uma autoridade usa a expressão “publicidade integra nominalmente o caput do art. 2º da Lei
- Uma decisão administrativa decide um recurso e apenas registra “mantida por interesse públ
- Uma autoridade pretende retirar ato ilegal que produziu vantagens individuais concretas, s
- Um ato contém vício sanável, e há demonstração de que sua correção não prejudicará terceir
- Uma consulta doutrinária afirma que, após a mudança de orientação geral, situações plename
- Um prefeito nomeia o próprio irmão para o cargo de secretário municipal. Um parecer afirma
- Um procedimento passa a cobrar taxa de exame apenas com base em instrução administrativa, 
- Em sessão colegiada, uma decisão oral é tomada após debate e a justificativa é registrada 
- Uma pessoa sustenta que, constatada ilegalidade em ato próprio, a Administração depende ne
- Uma indicação recai sobre parente em terceiro grau da autoridade nomeante para cargo em co
- Para resolver pequeno problema logístico, uma unidade impõe paralisação total de atendimen
- Uma unidade administrativa de entidade integrante da administração indireta estadual deve 
- Uma equipe informa que a moralidade e a publicidade passaram a integrar o art. 37, caput, 
- Ao divulgar calendário de atendimento com conteúdo impessoal e informativo, a Administraçã
- Uma autoridade pode usar símbolos pessoais em campanha institucional sempre que os horário
- Uma unidade que reorganiza atendimento para reduzir desperdício, sem afastar deveres legai
- Ao citar literalmente o art. 2º, caput, da Lei nº 9.784/1999, uma resposta deve inserir “p
- Quando a Administração federal adota formas simples que garantem certeza e respeitam direi
- A repartição que inicia de ofício diligência útil ao processo viola, por esse simples moti
- Uma medida eficaz mas mais gravosa que outra igualmente eficaz pode falhar no subteste de 
- Ao indeferir pedido que afeta direitos, a autoridade pode apenas dizer “por interesse públ
- Em assuntos da mesma natureza, a autoridade pode usar reprodução mecânica de fundamentos, 
- Um ato válido, que passou a ser inconveniente, deve ser anulado por ilegalidade, pois a re
- Se um ato administrativo favorável produziu efeitos patrimoniais contínuos, o prazo do art
- Se a autoridade pretende retirar ato que produziu efeitos concretos individuais, a Súmula 
- A Administração pode convalidar vício sanável sem lesão ao interesse público ou prejuízo a
- A mudança de orientação administrativa autoriza aplicar retroativamente a nova interpretaç
- A nomeação recíproca de parentes para funções de confiança pode configurar nepotismo, conf
- Segundo a jurisprudência do STF, a nomeação de parente para cargo político é sempre válida
- Uma medida pode falhar no exame de proporcionalidade em sentido estrito quando o sacrifíci
- O art. 54 da Lei nº 9.784/1999 estabelece que, comprovada má-fé, o prazo de cinco anos par
```

## Pedido 50: Estatística descritiva (`matematica.estatistica-descritiva`, 1542 tópicos de edital)

```
Matéria: matematica.estatistica-descritiva (Estatística descritiva)
Capítulos e quantas questões quero em cada um (total 100):
- "1. População, variáveis, frequências e representação tabular": 18
- "2. Gráficos, histogramas, curvas de frequência e dados agrupados": 15
- "3. Médias, mediana, moda, quartis e separatrizes": 28
- "4. Medidas de dispersão e comparação de variabilidade": 25
- "5. Análise exploratória: boxplot, assimetria, curtose e relações entre variáveis": 14

Já existem (não repita):
- Uma pesquisa registra os códigos 101, 102 e 103 para identificar três tipos de combustível
- Em uma pesquisa com 40 respostas válidas, 12 pessoas escolheram bicicleta como meio princi
- Os tempos observados, já em ordem crescente, foram 2, 3, 4 e 7 minutos. Qual é a mediana d
- Um grupo de 10 observações tem média 8, e outro de 30 observações tem média 12. Qual é a m
- Uma série de velocidades apresenta mínimo de 3 unidades e máximo de 13 unidades. Quanto va
- Uma categoria ocupa 25% de um gráfico de setores circulares. Qual ângulo central deve repr
- Os valores observados são 2, 2, 3, 4 e 4. Qual descrição da moda é adequada a esses dados?
- Uma tabela de tempos apresenta frequência 3 para um minuto, 5 para dois minutos e 2 para t
- Uma pesquisa agrupa alturas em intervalos contíguos e quer mostrar sua distribuição de fre
- Em uma distribuição agrupada, a classe [0,10) tem frequência 2 e a classe [10,20) tem freq
- Para os valores 2, 4 e 6, um pesquisador calcula o desvio médio absoluto em relação à médi
- Os números 2, 4 e 6 compõem toda a população de interesse. Qual é sua variância populacion
- A mesma série 2, 4 e 6 passa a ser tratada como amostra, e pede-se variância amostral corr
- Para os dados ordenados 1, 2, 3, 4, 5, 6 e 7, adote quartis pela mediana das metades, excl
- Um histograma usa [0,10) com 20 observações e [10,30) com 30 observações. Quais são as alt
- Uma série de medidas positivas apresenta média 20 e desvio padrão 4. Qual é seu coeficient
- O conjunto 2, 2, 3, 4 e 14 tem moda 2, mediana 3 e média 5. Que formato é sugerido por ess
- Para o conjunto ordenado 2, 4, 4, 5, 7, 9, 10, 11, com quartis pela mediana das metades, q
- Em 1, 2, 3, 4, 5, 6, 20, use quartis pela mediana das metades e a cerca superior Q3+1,5 AI
- Uma série tem curtose convencional padronizada igual a 4. Sob a referência usual da distri
- A série 2, 4, 6, tratada como população, é transformada pela adição de 5 a cada elemento. 
- Um diagrama de dispersão contém pares (1,8), (2,6) e (3,4). Qual interpretação descritiva 
- Uma série de razões positivas contém os valores 4 e 9. Qual é a média geométrica desses do
- Três classes [0,10), [10,20) e [20,30) têm frequências 2, 6 e 4. Qual ponto deve represent
- Uma tabela tem 20 observações: [0,10) com frequência 3, [10,20) com 8 e [20,30) com 9. Sem
- Uma pesquisa observa o tempo de deslocamento de 60 pessoas selecionadas entre 600 moradore
- Uma escala de satisfação com categorias baixa, média e alta é qualitativa ordinal; como as
- Em 24 observações válidas, uma categoria aparece seis vezes; sua frequência relativa é 25%
- Em um histograma com classes de larguras distintas, as alturas dos retângulos podem repres
- Uma variável nominal como cor de veículo admite identificação da moda, isto é, da categori
- Em um conjunto com oito observações ordenadas, a mediana é necessariamente a quarta observ
- Para os dados 1, 3, 4, 8 e 9, usando a convenção de quartis pela mediana das metades que e
- Duas turmas têm 10 e 30 observações com médias 8 e 12; a média conjunta é 10 porque corres
- O desvio médio absoluto em relação à média dos valores 2, 4 e 6 é 4/3, porque os afastamen
- Quando todos os elementos de uma população conhecida são observados, a variância populacio
- Uma série com média 20 e desvio padrão 4 apresenta maior dispersão relativa que outra com 
- Qualquer distribuição com média superior à mediana é necessariamente assimétrica à direita
- No conjunto 1, 2, 3, 4, 5, 6, 20, a convenção das medianas das metades dá Q1=2 e Q3=6, de 
- Para uma distribuição mesocúrtica sob a referência normal, a curtose convencional e o exce
- Num histograma, uma classe de largura 10 e frequência 20 tem densidade 2; se a altura da b
- O terceiro quartil de um conjunto finito coincide necessariamente com seu valor máximo, po
- Para classes [0,10), [10,20) e [20,30), um polígono de frequências simples pode unir ponto
- Um diagrama de dispersão com pontos alinhados em direção descendente demonstra, sem inform
- Quando se dispõe apenas de classes de valores e suas frequências, uma média calculada com 
- Um ponto marcado como discrepante pela regra de 1,5 vezes a amplitude interquartil deve se
```

