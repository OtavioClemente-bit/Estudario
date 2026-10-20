# Pedidos de questões extras, lote 24 de 24 (20/10/2026)

Mesmo esquema dos lotes anteriores. Conversa nova, cole a Mensagem 0 e depois um pedido por mensagem. Salve cada resposta como `<id>.questoes.json`. Matérias deste lote: 33, somando 90 tópicos de edital.

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
- TAMANHO: a correta deve ter o MESMO tamanho das erradas. Ela não pode ser a mais longa em mais de 30% das questões NEM a mais curta em mais de 30% (no lote anterior várias vieram com a correta sempre curtinha, tipo "Crônica.", e foram recusadas). Em questão numérica, varie a quantidade de dígitos dos distratores.
- EXPLICAÇÃO PRÓPRIA: cada explicação fala só da sua questão. PROIBIDO terminar explicações com a mesma frase de fechamento (nenhuma frase pode aparecer em mais de 3 explicações). Em cálculo, mostre a conta.
- Em Certo/Errado, a explicação nunca usa as palavras "alternativa" ou "opção".
- As 5 alternativas têm que ser diferentes entre si (não vale diferir só em maiúscula). Cada alternativa é uma frase única, sem trecho solto depois do ponto final.
- NÃO escreva "Análise das opções", "Gabarito B" nem comentários por letra ("A) Incorreta...") dentro de "explanation": a explicação traz só o fundamento; o comentário de cada alternativa vai no campo "comentario" dela (o app troca as letras na versão A a D, e a letra escrita no texto ficaria errada).
- Item Certo/Errado NÃO pode copiar o texto de uma alternativa de outra questão (nem das já existentes): escreva a afirmação com redação própria.
- Cada "comentario" com pelo menos uma frase explicativa (não só "Errada.").
- Varie o tipo: conceito, caso prático, "assinale a incorreta", assertivas I, II e III, exceção, comparação, letra da lei. Não comece várias questões com a mesma frase de molde.
- Alternativas erradas PLAUSÍVEIS: erros que um candidato que estudou mal cometeria (troca de prazo, de órgão, de conceito vizinho, exceção virando regra). Nada absurdo ou caricato, como "publicar o nome do paciente" ou "eliminar toda fiscalização", que se elimina sem saber a matéria.
- A questão tem que cobrar o assunto DO CAPÍTULO indicado em "section", não de outro capítulo.
- Se não tiver certeza de número de artigo, lei, prazo ou súmula, não cite o número: cobre o conteúdo. Nunca invente norma, jurisprudência ou dado.
- Certo/Errado no estilo Cebraspe: afirmação única, precisa, com o erro (quando houver) num detalhe técnico, não em palavra absurda.
- Português do Brasil, nível de prova de concurso real.
```

## Pedido 1: Imagem da mama: mamografia, ultrassom e ressonância (`medicina.mastologia-mamografia-imagem`, 5 tópicos de edital)

```
Matéria: medicina.mastologia-mamografia-imagem (Imagem da mama: mamografia, ultrassom e ressonância)
Capítulos e quantas questões quero em cada um (total 100):
- "Fundamentos técnicos e posicionamento mamográfico": 17
- "BI-RADS e comunicação diagnóstica": 17
- "Semiologia mamográfica: nódulos, calcificações e distorções": 17
- "Rastreamento, mamografia diagnóstica e tomossíntese": 17
- "Ultrassonografia e ressonância magnética mamárias": 16
- "Biópsias guiadas, concordância e garantia da qualidade": 16

Já existem (não repita):
- Quais são as duas incidências mamográficas básicas realizadas no rastreamento bilateral?
- Na avaliação do posicionamento da incidência MLO, qual achado sugere inclusão adequada de 
- Um exame de rastreamento revelou uma possível assimetria visível em apenas uma projeção e 
- Qual categoria BI-RADS pressupõe comprovação prévia de malignidade por biópsia?
- Qual padrão de calcificação mamográfica é classicamente considerado benigno quando apresen
- Segundo a orientação ministerial de 2025, qual faixa etária integra o rastreamento mamográ
- A ultrassonografia demonstra estrutura anecoica circunscrita, de parede fina e reforço acú
- Uma imagem evidencia microcalcificações suspeitas sem correspondente visível no ultrassom.
- Após estudo diagnóstico completo, uma massa oval e estável reúne critérios de provavelment
- Mamografia mostra lesão com espículas e retração arquitetural, sem cirurgia prévia. Qual c
- Na avaliação de microcalcificações, qual combinação merece investigação por possível distr
- Qual afirmação diferencia corretamente mamografia de rastreamento de exame diagnóstico?
- Qual é o principal ganho físico da tomossíntese digital da mama em relação a uma única pro
- A ultrassonografia mostra massa sólida irregular de orientação não paralela e margens angu
- Uma RM detecta lesão suspeita não encontrada no ultrassom second-look nem na mamografia re
- Por que se faz radiografia dos fragmentos após biópsia de microcalcificações, quando aplic
- Em paciente com nódulo palpável clinicamente suspeito, mamografia e ultrassom direcionado 
- Uma assimetria focal antes discreta aparece maior e mais evidente na comparação longitudin
- Uma mulher de 44 anos, assintomática, solicita mamografia pelo SUS. Qual descrição corresp
- Na RM, há realce não nodular segmentar com padrão interno suspeito e curva de washout. Qua
- Calcificações finas lineares ramificadas e segmentares foram biopsiadas; o laudo reporta a
- Ao revisar uma MLO com mamilo em perfil, mas sulco inframamário fechado e tecido posterior
- Qual proposição integra corretamente normas e prática de controle de qualidade em mamograf
- Na incidência MLO, a inclusão do sulco inframamário e de parte do músculo peitoral contrib
- A compressão mamográfica serve exclusivamente para provocar desconforto, sem influência so
- BI-RADS 2 corresponde a achado com características benignas, em vez de malignidade já conf
- BI-RADS 0 significa que o exame necessariamente detectou carcinoma invasivo.
- Calcificações vasculares típicas acompanham paredes de vasos e geralmente são reconhecidas
- A ultrassonografia de mama utiliza radiação ionizante de baixa dose da mesma forma que a m
- A presença de nódulo palpável suspeito exige investigação diagnóstica, mesmo que a pacient
- Tomossíntese mamária equivale a exame sem radiação ionizante porque suas reconstruções são
- Calcificações finas pleomórficas em distribuição segmentar tendem a merecer mais atenção d
- Uma lesão cístico-sólida complexa com componente mural é automaticamente equivalente a um 
- A escolha de método de biópsia deve considerar em qual modalidade o alvo está identificado
- Depois de uma biópsia percutânea, qualquer laudo benigno encerra a investigação sem necess
- A classificação BI-RADS 3 deve ser reservada a achados selecionados após avaliação diagnós
- A mama classificada como extremamente densa na descrição de composição recebe obrigatoriam
- A Nota Técnica nº 626/2025 do Ministério da Saúde orienta rastreamento populacional bienal
- A ausência de massa definida torna a distorção arquitetural mamográfica nova necessariamen
- O realce não nodular segmentar na RM pode ser suspeito, e um ultrassom second-look negativ
- A radiografia de espécimes de microcalcificações sempre substitui a análise histopatológic
```

## Pedido 2: Lei Orgânica da Polícia Civil do Amapá (Lei 883/2005) (`legislacao.amapa-lei-883-2005-policia-civil`, 5 tópicos de edital)

```
Matéria: legislacao.amapa-lei-883-2005-policia-civil (Lei Orgânica da Polícia Civil do Amapá (Lei 883/2005))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Natureza, carreiras, princípios e atribuições": 17
- "2. Estrutura e órgãos da Polícia Civil": 23
- "3. Investidura, curso de formação, estágio probatório, estabilidade e remoção": 10
- "4. Atribuições das carreiras, progressão e promoção": 17
- "5. Prerrogativas, vedações, recompensas, licenças e remuneração": 17
- "6. Regime disciplinar, penalidades e Código de Ética": 16

Já existem (não repita):
- Segundo a Lei 883/2005, a Polícia Civil do Amapá é órgão autônomo, permanente e essencial 
- Excluída qualquer outra nomenclatura, as carreiras policiais civis do Amapá são exclusivam
- Após a Lei 3.071/2024, o integrante das carreiras da Polícia Civil do Amapá pode exercer c
- Na Lei 883/2005, considera-se Autoridade Policial:
- É permitida a cessão de policiais civis para exercício em outros órgãos da União, do Estad
- Entre as atribuições da Polícia Civil do Amapá previstas na Lei 883/2005, está:
- As atribuições reservadas legalmente à Polícia Civil são indelegáveis e só podem ser exerc
- O Conselho Superior da Polícia Civil do Amapá é presidido pelo:
- Compete ao Conselho Superior da Polícia Civil julgar o estágio probatório do policial civi
- Sobre as Câmaras Disciplinares da Polícia Civil do Amapá, é correto afirmar:
- O cargo de Delegado Geral de Polícia Civil é de livre escolha, nomeação e exoneração pelo 
- Compete ao Delegado Geral avocar, excepcional e fundamentadamente, inquérito policial, ter
- Na estrutura da Polícia Civil do Amapá, cabe à Corregedoria Geral, órgão de controle inter
- São órgãos de funções estratégicas da Delegacia Geral o Departamento de Inteligência e o D
- O Chefe de Gabinete da Delegacia Geral, além de assessorar o Delegado Geral, tem a atribui
- Quanto aos requisitos de escolaridade para ingresso nas carreiras policiais civis do Amapá
- A matrícula no Curso de Formação Policial Profissional está condicionada à aptidão, nesta 
- Durante o Curso de Formação Policial Profissional, o aluno recebe bolsa mensal corresponde
- Na Lei 883/2005, o prazo para o policial empossado entrar em exercício, contado da data da
- A posse do policial civil é solene e presidida pelo Delegado Geral de Polícia Civil, que e
- A declaração de bens e valores apresentada na posse do policial civil deve ser obrigatoria
- O estágio probatório do policial civil dura 36 meses, e, preenchidos todos os requisitos l
- NÃO é requisito apurado no estágio probatório do policial civil do Amapá:
- A remoção a pedido do policial civil para a capital do Estado pode ser concedida a quem es
- A remoção por permuta, na Lei 883/2005:
- Cabe ao Delegado de Polícia instaurar e presidir, com exclusividade, inquéritos policiais,
- É atribuição típica do Oficial de Polícia Civil, segundo a Lei 883/2005:
- Entre as atribuições do Agente de Polícia está efetuar prisões em flagrante e executar as 
- Para ingresso na carreira de Delegado de Polícia, cuja classe inicial é a de Delegado Subs
- Na promoção dos policiais civis do Amapá, as vagas são preenchidas na proporção de:
- Na avaliação da antiguidade para promoção, persistindo o empate entre dois policiais após 
- Com a redação dada pela Lei 2.677/2022, a progressão do policial civil de um padrão para o
- O policial civil que estiver respondendo a processo administrativo disciplinar ou criminal
- Entre as prerrogativas do policial civil do Amapá está ter ingresso e trânsito livres em q
- NÃO é vedação imposta pela Lei 883/2005 aos integrantes da carreira policial civil:
- O desagravo público ao policial civil ofendido no exercício do cargo será promovido de ofí
- A licença para frequentar curso de pós-graduação fora do Estado, com remuneração integral,
- O subsídio dos Agentes de Polícia e dos Oficiais de Polícia Civil corresponde a, no mínimo
- A ajuda de custo do policial civil removido no interesse do serviço para sede situada a ma
- As propostas de elogio ao policial civil podem ser formuladas por qualquer autoridade ou c
- Na Lei 883/2005, a pena aplicável às infrações disciplinares classificadas como gravíssima
- Receber propina, comissão, presente ou vantagem de qualquer espécie em razão do serviço é 
- Nos casos de embriaguez habitual, a Lei 883/2005 determina que:
- Reconhecida, na apreciação da falta disciplinar, uma causa de justificação, como a legítim
- O policial civil será imediatamente afastado do exercício de suas funções, até decisão tra
- Os preceitos do Código de Ética Policial devem ser observados com rigor tanto maior quanto
- Por conveniência do serviço, a pena de suspensão de até 60 dias aplicada ao policial civil
- Nos casos omissos que não colidam com a Lei 883/2005, aplicam-se subsidiariamente ao polic
```

## Pedido 3: Licitações e Contratos na Bahia (Lei 14.634/2023) (`legislacao.licitacoes-bahia-lei-14634`, 5 tópicos de edital)

```
Matéria: legislacao.licitacoes-bahia-lei-14634 (Licitações e Contratos na Bahia (Lei 14.634/2023))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Âmbito, relação com a Lei 14.133/2021 e competência estadual": 20
- "2. Agentes públicos da licitação, do contrato e do controle": 20
- "3. Tecnologia, limites de valor, alienação e outorga de uso de bens": 20
- "4. Convênios": 20
- "5. Sanções, processo sancionatório e faltas disciplinares": 20

Já existem (não repita):
- Quanto à relação entre a Lei estadual 14.634/2023 e a Lei federal 14.133/2021, é correto a
- Não são abrangidas pela Lei 14.634/2023 as empresas públicas, as sociedades de economia mi
- Segundo a Lei 14.634/2023, o Tribunal de Contas dos Municípios, o Ministério Público e a D
- Pela Lei 14.634/2023, o titular de Poder ou de órgão autônomo pode fixar, por ato normativ
- NÃO consta do art. 3º da Lei 14.634/2023 como matéria a ser disciplinada pela legislação e
- Segundo a Lei 14.634/2023, as parcerias entre a Administração e organizações da sociedade 
- Sobre a vigência e a transição da Lei 14.634/2023, assinale a correta.
- Na Lei 14.634/2023, o agente de contratação será auxiliado por equipe de apoio e responder
- Sobre a comissão de contratação na Lei 14.634/2023, é correto afirmar:
- Um membro da comissão de contratação discordou da decisão tomada pela maioria. Para não re
- A banca incumbida de avaliar quesitos de natureza qualitativa terá, no mínimo, três membro
- Pela Lei 14.634/2023, o recebimento provisório do objeto do contrato cabe:
- Quando o fiscal do contrato é assistido por terceiro contratado pela Administração, a Lei 
- A apuração de conduta sujeita à sanção de declaração de inidoneidade será conduzida, segun
- A análise jurídica individualizada pode ser dispensada, por ato da autoridade jurídica máx
- Segundo a Lei 14.634/2023, o órgão de assessoramento jurídico realizará controle prévio de
- A alienação de bem imóvel estadual, segundo a Lei 14.634/2023, depende, em regra, de:
- A doação de bem imóvel a terceiros deve trazer na escritura os encargos do donatário, o pr
- Na licitação para venda de bem imóvel estadual, o arrematante pagará, no ato da sessão púb
- Após leilões de imóvel sucessivamente desertos ou fracassados, a Lei 14.634/2023 admite:
- A investidura, na Lei 14.634/2023, consiste na alienação ao proprietário de imóvel lindeir
- Na alienação de bens móveis pelo Estado da Bahia, exige-se autorização legislativa específ
- O produto da alienação de bens do Estado da Bahia é receita de capital e pode ser livremen
- Entre as formas de outorga de uso de bens públicos estaduais, aquela formalizada por escri
- Sobre a outorga de uso de bens públicos na Lei 14.634/2023, assinale a correta.
- São pressupostos da celebração de convênios na Lei 14.634/2023, EXCETO:
- Na celebração de convênio entre órgãos ou entidades da Administração Pública sem transferê
- Sobre a alteração e a execução dos convênios na lei baiana, é correto afirmar:
- Sempre que a execução do objeto puder ser realizada por mais de um proponente, a celebraçã
- Pela Lei 14.634/2023, os atos lesivos previstos no art. 5º da Lei Anticorrupção (Lei 12.84
- No processo sancionatório simplificado, destinado à apuração de condutas sujeitas apenas a
- No processo de responsabilização da Lei 14.634/2023, poderá haver julgamento antecipado qu
- É competente para aplicar a sanção de declaração de inidoneidade para licitar ou contratar
- Pela Lei 14.634/2023, a prescrição da pretensão sancionatória contra licitantes e contrata
- Sobre a transação administrativa na Lei 14.634/2023, é correto afirmar:
- Transitada em julgado a decisão sancionatória, a sanção deve ser registrada no cadastro de
- A desconsideração da personalidade jurídica, prevista na Lei 14.634/2023, permite estender
- Constitui falta disciplinar do agente público, segundo o art. 61 da Lei 14.634/2023:
- Não havendo disposição contratual diversa, o pedido de restabelecimento do equilíbrio econ
- Segundo a Lei 14.634/2023, os preços da Tabela de Preços Referenciais do Estado da Bahia, 
```

## Pedido 4: Porte de arma da Polícia Judicial (Resolução CNJ 467/2022) (`seguranca-institucional.porte-arma-policia-judicial`, 5 tópicos de edital)

```
Matéria: seguranca-institucional.porte-arma-policia-judicial (Porte de arma da Polícia Judicial (Resolução CNJ 467/2022))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamento, titulares e armas institucionais": 34
- "2. Autorização, requisitos e aquisição": 33
- "3. Uso, controle e responsabilidade": 33

Já existem (não repita):
- Segundo a Resolução CNJ 467/2022, armas de fogo cedidas, emprestadas ou destinadas ao trib
- No gozo de férias ou de licença, o porte de arma do policial judicial, segundo a Resolução
- Quem define o armamento, o calibre e a munição a serem adquiridos pelo tribunal, mediante 
- A Resolução CNJ 467/2022 regulamenta, no âmbito do Poder Judiciário, os arts. 6º, XI, e 7º
- Segundo a Resolução CNJ 467/2022, o porte de arma de fogo em todo o território nacional é 
- As armas de fogo de que trata a Resolução CNJ 467/2022 são de propriedade dos próprios pol
- Pela redação dada pela Resolução CNJ 566/2024, as armas institucionais poderão ser utiliza
- As armas de fogo institucionais devem ser brasonadas e gravadas com inscrição que identifi
- A autorização de extensão do porte de arma funcional para defesa pessoal fora de serviço é
- A autorização de extensão do porte para defesa pessoal é presumida quando o policial judic
- Segundo a Resolução CNJ 467/2022, o porte funcional estendido para defesa pessoal e o port
- Os servidores com porte de arma institucional poderão obter, da Polícia Federal, porte de 
- A autorização para o porte de arma de fogo funcional prevista na Resolução CNJ 467/2022:
- A autorização para o porte de arma de fogo funcional tem prazo de validade indeterminado, 
- Por ter prazo de validade indeterminado, a autorização de porte funcional do policial judi
- Durante atividades de integração e interoperabilidade dos órgãos do SINASPJ, o porte funci
- O porte funcional do policial judicial depende de formação funcional, que pode ser realiza
- Na Resolução CNJ 467/2022, a habilitação em curso específico para utilização de arma de fo
- A capacidade técnica e a aptidão psicológica do policial judicial podem ser atestadas por 
- O armamento, o modelo, o calibre, a munição e os demais equipamentos a serem adquiridos pe
- A Resolução CNJ 467/2022 autoriza os tribunais a adquirir armas de fogo de uso restrito e 
- O Certificado de Registro de Arma de Fogo (CRAF) das armas do acervo pessoal dos integrant
- No controle de utilização das armas institucionais, a unidade de segurança do tribunal dev
- O servidor requisitado ou cedido por outro órgão e que possua porte funcional de arma de f
- Ao portar a arma institucional, o servidor deve estar acompanhado:
- O porte da arma de fogo institucional poderá ser ostensivo quando o policial judicial esti
- Para o embarque armado em aeronaves, a Resolução CNJ 467/2022 exige a apresentação de orde
- Em caso de perda, furto, roubo ou extravio de arma institucional sob sua guarda, o tribuna
- As regras de comunicação de perda, furto ou roubo de arma institucional também se aplicam 
- Segundo o art. 11 da Resolução CNJ 467/2022, NÃO é hipótese de suspensão ou cassação do po
- A suspensão ou cassação do porte de arma do policial judicial afasta a aplicação de sançõe
- A revogação, suspensão ou cassação do porte implica o imediato recolhimento, pela unidade 
- O servidor designado para portar arma institucional responde:
- Pela Resolução CNJ 467/2022, os casos omissos serão resolvidos:
- Fazer uso de substâncias que causem dependência física ou psíquica ou alterem o desempenho
- Cabe à unidade de Polícia Judicial do tribunal a que o servidor está vinculado adotar as p
- A designação dos servidores que poderão portar arma de fogo deve ser informada:
- Na Resolução CNJ 467/2022, a aquisição de arma de fogo institucional e de equipamentos de 
- A Resolução CNJ 467/2022 considera que a segurança institucional é condição para garantir 
- Fora das hipóteses de uso previstas na resolução, a arma institucional e o certificado de 
```

## Pedido 5: História de Minas Gerais (`minas-gerais.historia-minas-gerais`, 4 tópicos de edital)

```
Matéria: minas-gerais.historia-minas-gerais (História de Minas Gerais)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Colonização, ouro e escravidão": 25
- "2. Inconfidência, cidades históricas e café": 25
- "3. República, industrialização e desenvolvimento": 25
- "4. Regiões, cultura, ambiente e como a IDECAN cobra": 25

Já existem (não repita):
- Qual afirmação reconhece corretamente a presença humana anterior à colonização portuguesa 
- Qual transformação se relaciona à expansão da mineração no século XVIII?
- Qual descrição considera a participação de pessoas escravizadas na sociedade mineradora mi
- Qual síntese caracteriza a Inconfidência Mineira de 1789?
- Na transição econômica mineira, que efeito é associado à expansão da cafeicultura no sécul
- Qual leitura da chamada política do Café com Leite é historicamente mais cuidadosa?
- Qual resultado político é associado à Revolução de 1930?
- Qual alternativa situa corretamente o Estado Novo na trajetória política brasileira?
- Qual vínculo caracteriza a Companhia Vale do Rio Doce em sua origem?
- Qual interpretação da cultura popular mineira evita reduzi-la a um único repertório?
- Qual afirmação compara Norte e Sul de Minas sem transformar diferenças regionais em estere
- Que análise equilibra os efeitos econômicos e ambientais da mineração em Minas Gerais?
- Qual formulação descreve melhor a reorganização da economia mineira entre o ouro e a indús
- Uma narrativa afirma que a colonização mineira foi pacífica porque bandeirantes e indígena
- Qual inferência sobre as diferenças entre Norte e Sul de Minas é mais defensável em uma an
- Qual leitura da Inconfidência considera tanto seus objetivos quanto os limites sociais de 
- Qual relação entre cafeicultura e industrialização em Minas é historicamente mais precisa?
- Uma questão diz que Minas e São Paulo alternaram a Presidência em cada mandato por determi
- Como se relacionam a participação mineira em 1930 e o início do Estado Novo?
- Uma análise associa a CVRD, criada em 1942, à mineração e à industrialização. Qual complem
- Uma política estadual amplia indústria e logística, mas persistem desigualdade e diferença
- Que aspecto deve ser incluído ao explicar a economia do ouro em Minas Gerais?
- Qual afirmação sobre as cidades históricas mineiras é mais adequada para uma política de p
- Qual leitura da cultura musical evita atribuir exclusividade estadual ao samba de raiz?
- Qual relação entre mineração e preservação ambiental é mais consistente com os desafios co
- Uma interpretação afirma que o Norte de Minas possui apenas atividades rurais e que o Sul 
- Qual frase mantém a cronologia correta entre a queda da produção aurífera, a Inconfidência
- Ao analisar desenvolvimento econômico no século XX e XXI, qual conclusão sobre diversifica
- A economia mineira do século XX deve ser analisada apenas pela produção industrial, sem co
- A execução de Tiradentes ocorreu depois da denúncia e da devassa que reprimiram a Inconfid
- A cafeicultura do século XIX contribuiu para mudanças nos transportes e na infraestrutura 
- O território mineiro estava vazio de povos nativos até o início da mineração colonial.
- A criação da Companhia Vale do Rio Doce ocorreu em 1942, já durante a República Velha.
- A política do Café com Leite foi uma regra escrita na Constituição que obrigava alternânci
- O Estado Novo instaurado por Vargas foi um regime autoritário e centralizador.
- As cidades históricas de Minas devem ser entendidas como espaços congelados, sem moradores
- A Inconfidência foi uma conspiração de setores da elite colonial, e não um levante vitorio
- A produção de ouro e a agricultura de abastecimento não coexistiram em Minas durante o per
- A Revolução de 1930 encerrou a República Velha, enquanto o Estado Novo foi instaurado em 1
- O rótulo Gerais do Norte e Gerais do Sul autoriza tratar cada região como cultural e econo
- O reconhecimento de uma cidade como patrimônio cultural elimina a necessidade de manutençã
- O samba de raiz pode integrar manifestações culturais em Minas sem ser considerado gênero 
- A extração mineral pode gerar efeitos econômicos e, simultaneamente, exigir atenção a água
```

## Pedido 6: Direito internacional privado: família, alimentos e cooperação (`direito-internacional.privado-familia-cooperacao`, 4 tópicos de edital)

```
Matéria: direito-internacional.privado-familia-cooperacao (Direito internacional privado: família, alimentos e cooperação)
Capítulos e quantas questões quero em cada um (total 100):
- "Elementos de conexão, LINDB e relações familiares internacionais": 17
- "Convenção da Haia de 1980: subtração internacional de crianças": 17
- "Adoção internacional: Convenção da Haia de 1993 e ECA": 17
- "Alimentos no exterior: Nova York de 1956 e Haia de 2007": 17
- "Cooperação jurídica civil: auxílio direto, rogatória e homologação": 16
- "Nacionalidade, migração e proteção de famílias transnacionais": 16

Já existem (não repita):
- Segundo a LINDB, a lei que rege, em regra, a capacidade de pessoa domiciliada no exterior 
- Um casamento entre estrangeiros é celebrado no Brasil. No tocante às formalidades da celeb
- A remissão que uma lei estrangeira aplicável faça a outra lei estrangeira denomina-se reen
- A Convenção da Haia de 1980 deixa de se aplicar à criança quando ela atinge:
- Após viagem temporária autorizada, um responsável decide não devolver o filho ao Estado on
- O objetivo predominante da Convenção da Haia de 1980 é:
- Para distinguir adoção internacional no regime da Convenção da Haia de 1993, o critério ce
- No ECA, a preferência prevista em adoção internacional de criança brasileira, depois dos r
- Passou mais de um ano da retenção ilícita e o processo só então começou. Pela Haia de 1980
- No Brasil, o canal institucional indicado para pedidos ao abrigo da Convenção de Nova York
- O âmbito básico da Convenção da Haia de 2007, quanto a alimentos decorrentes de filiação, 
- A Convenção da Haia de 2007 substitui a Convenção de Nova York de 1956:
- Um órgão estrangeiro solicita, sem decisão jurisdicional a delibar, informações de process
- O divórcio consensual estrangeiro limitado à dissolução do casamento produz efeitos no Bra
- Como regra geral, o Protocolo da Haia de 2007 sobre lei aplicável aos alimentos elege a le
- Dois nubentes tinham domicílios em Estados distintos e passaram a viver juntos em terceiro
- Uma criança se opõe ao retorno e demonstra compreensão madura das consequências. Segundo a
- Uma sentença estrangeira determina partilha de imóvel situado no Brasil. No exame de eficá
- Segundo o art. 26 do CPC, em ausência de tratado, a reciprocidade diplomática para coopera
- Uma decisão estrangeira contém capítulos separáveis, um deles incompatível com a ordem púb
- A certificação convencional de adoção realizada sob a Haia de 1993 gera, nos Estados contr
- A LINDB adota como regra, para capacidade e direitos de família, a lei do país do domicíli
- O local do imóvel é irrelevante para conflitos de leis, pois o direito internacional priva
- Na Convenção da Haia de 1980, o retorno internacional pode ser requerido em caso de retenç
- A Convenção da Haia de 1993 sobre adoção internacional objetiva assegurar garantias, coope
- Na Convenção da Haia de 1980, sequestro internacional significa necessariamente crime de c
- A Convenção de Nova York de 1956 foi promulgada no Brasil pelo Decreto nº 56.826/1965.
- A mera obtenção voluntária de nacionalidade estrangeira ocasiona automaticamente a perda d
- A ultrapassagem do prazo de um ano previsto no art. 12 da Haia de 1980 impede qualquer dec
- O reconhecimento de adoção internacional sob a Convenção da Haia de 1993 não significa que
- Um brasileiro residente habitualmente no exterior não pode participar de adoção internacio
- O arranjo brasileiro para a Convenção da Haia de 2007 sobre alimentos indica o DRCI/SNJ co
- Auxílio direto é exclusivamente administrativo e, portanto, não pode ser submetido a juiz 
- O STJ é constitucionalmente competente para homologar decisões estrangeiras e conceder exe
- A Lei de Migração consagra a não criminalização da migração e a promoção da reunião famili
- Na Haia de 1980, basta um genitor demonstrar que possui nacionalidade do Estado de origem 
- O art. 13(b) da Haia de 1980 admite a consideração de grave risco físico ou psíquico para 
- Pelo Protocolo da Haia de 2007 sobre alimentos, mudança de residência habitual do credor p
- A Convenção da Haia de 2007 substituiu a de Nova York de 1956 de modo universal, inclusive
- O CPC permite homologação parcial de decisão estrangeira e dispensa homologação do STJ par
- Segundo o CPC, homologação de decisão estrangeira exige necessariamente prova de reciproci
- A Constituição brasileira admite perda voluntária da nacionalidade por pedido expresso, ma
```

## Pedido 7: Enfermagem em oncologia, hemoterapia e terapia infusional (`enfermagem.enfermagem-oncologica-hemoterapia`, 4 tópicos de edital)

```
Matéria: enfermagem.enfermagem-oncologica-hemoterapia (Enfermagem em oncologia, hemoterapia e terapia infusional)
Capítulos e quantas questões quero em cada um (total 100):
- "Assistência integral e segurança na enfermagem oncológica": 20
- "Quimioterapia antineoplásica: preparo seguro, administração e extravasamento": 20
- "Radioterapia: modalidades e cuidados de enfermagem": 20
- "Acessos vasculares e terapia infusional: CVC, PICC e dispositivos implantáveis": 20
- "Hemoterapia, reações transfusionais e cuidados paliativos": 20

Já existem (não repita):
- A consulta de enfermagem oncológica deve considerar, além de sintomas físicos, necessidade
- Febre após quimioterapia em paciente potencialmente neutropênico pode ser acompanhada apen
- Dor vertebral progressiva associada a fraqueza recente nos membros inferiores pode indicar
- A ausência de queda capilar durante os ciclos demonstra que o antineoplásico foi ineficaz 
- Uma paciente inicia terapia antineoplásica e relata dificuldade para compreender as orient
- Paciente submetido a quimioterapia chega com febre, calafrios e astenia intensa. A interve
- Durante o seguimento oncológico, qual achado mais sugere síndrome de compressão medular qu
- A equipe identifica neuropatia periférica em paciente sob tratamento sistêmico. Qual plane
- Na assistência oncológica integral, qual situação exemplifica adequadamente a continuidade
- A RDC Anvisa nº 220/2004 contempla, entre outras etapas, preparo, transporte, administraçã
- Na suspeita de extravasamento de antineoplásico, a primeira atitude é irrigar vigorosament
- A aplicação de compressas frias é obrigatória para todos os quimioterápicos vesicantes, in
- A administração de antineoplásicos deve ser acompanhada de conferência da prescrição, iden
- Ao receber uma bolsa de antineoplásico para administração, qual sequência é mais segura?
- Surge queimação e edema junto ao acesso durante a administração de vesicante. Qual respost
- Em relação às competências de enfermagem na quimioterapia antineoplásica, assinale a propo
- Um serviço revisa seu plano de biossegurança para antineoplásicos. Qual medida é coerente 
- Em uma revisão de incidentes, o enfermeiro distingue infiltração de extravasamento. Qual d
- Após uma sessão convencional de radioterapia externa, o paciente permanece emitindo radiaç
- O acompanhamento de radiodermite inclui inspecionar a região tratada e adequar medidas de 
- Os cuidados relativos à radioterapia são iguais para cabeça e pescoço e para pelve porque 
- Na braquiterapia, eventuais medidas de radioproteção dependem da permanência e do tipo de 
- Após teleterapia convencional, uma pessoa teme abraçar sua filha por considerar que seu co
- Durante radioterapia de cabeça e pescoço, o paciente relata dor ao engolir e redução impor
- Qual afirmação sobre radiodermite é a mais correta?
- Em braquiterapia com fonte temporária ainda posicionada, qual atitude é adequada?
- Em uma consulta de radioterapia pélvica, o enfermeiro deve priorizar a pesquisa de que con
- O PICC é um dispositivo de acesso central, apesar de ser introduzido por veia periférica.
- Obter refluxo de sangue no PICC dispensa confirmação apropriada de sua ponta antes da prim
- A ausência de hiperemia na inserção de CVC afasta completamente a possibilidade de infecçã
- Se o curativo do acesso central estiver úmido ou descolado, a troca pode ser necessária an
- Uma paciente tem PICC introduzido por veia basílica. Qual interpretação do dispositivo est
- Na troca de plantão, observa-se cobertura transparente do CVC descolada e úmida. Qual cond
- Durante administração em CVC, surge resistência inesperada ao fluxo. Qual é a escolha mais
- Um enfermeiro revisa o pacote preventivo de infecção relacionado a CVC. Qual item deve est
- Em terapia infusional simultânea, por que se deve avaliar compatibilidade entre soluções a
- Diante de suspeita de reação transfusional, deve-se interromper imediatamente a hemotransf
- As reações transfusionais imediatas são definidas exclusivamente como aquelas que ocorrem 
- O cuidado paliativo é incompatível com quimioterapia ou radioterapia voltada ao controle d
- A hemovigilância deve contemplar suspeitas de reações e incidentes relevantes do ciclo do 
- No leito, a pulseira identifica um paciente e o rótulo do hemocomponente indica outro nome
- Durante transfusão, aparecem febre, calafrios e dispneia novos. Qual a primeira resposta a
- Um paciente apresenta dispneia durante transfusão. Qual achado favorece TACO na avaliação 
- Sobre a notificação de uma possível reação transfusional, assinale a correta.
- Paciente com câncer avançado recebe terapia antineoplásica para controle tumoral e apresen
```

## Pedido 8: Orações reduzidas (`portugues.oracoes-reduzidas`, 4 tópicos de edital)

```
Matéria: portugues.oracoes-reduzidas (Orações reduzidas)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Oração reduzida: conceito e diagnóstico sintático": 20
- "2. Infinitivo reduzido: sujeito, complementos e modificação nominal": 20
- "3. Infinitivo adverbial: tempo, causa, finalidade e contraste": 20
- "4. Gerúndio: simultaneidade, condição, causa e descrição": 20
- "5. Particípio e reescrita: tempo, voz, sujeito e pontuação": 20

Já existem (não repita):
- Considere: 'É prudente revisar as informações antes da publicação'. Qual é a classificação
- Em 'Ao receber a mensagem, Clara respondeu imediatamente', a transformação mais fiel da ex
- No período 'Ela está preparando o material', como deve ser considerado o conjunto 'está pr
- Leia: 'Por estar indisposta, Lúcia cancelou o passeio'. Qual desenvolvimento mantém a rela
- No período 'Concluída a leitura, os participantes conversaram', qual é a forma nominal que
- Considere 'Apesar de conhecer a rota, Marina se perdeu'. Qual conectivo desenvolve a reduz
- Observe: 'Havia visitantes aguardando a abertura'. Que reescrita evidencia o valor de 'agu
- Na frase 'É conveniente nós chegarmos antes', como se classifica a forma verbal 'chegarmos
- Na instrução 'Separe tempo para estudar com tranquilidade', a expressão 'para estudar com 
- Na instrução 'Persistindo a dificuldade, procure outra explicação', a oração reduzida inic
- Compare 'Ao término do evento, todos saíram' e 'Ao terminar o evento, todos saíram'. Qual 
- Leia 'Os materiais, separados ontem pela equipe, chegaram hoje'. Qual desenvolvimento mant
- Em 'Mesmo tendo poucos recursos, a pesquisadora terminou o levantamento', qual leitura dev
- Compare 'É importante os colegas participarem da discussão' e 'É importante que os colegas
- Na frase 'O texto ficou tão confuso a ponto de provocar interpretações opostas', qual valo
- Considere 'Tendo concluído a avaliação, o grupo divulgou as conclusões'. Qual desenvolvime
- Em 'Enviadas as cartas, os destinatários confirmaram o recebimento', qual segmento funcion
- O enunciado 'Por terem recebido a orientação, os alunos corrigiram o procedimento' precisa
- Uma reescrita busca preservar 'Encerradas as inscrições, a organização publicou a lista'. 
- Compare 'Os relatórios examinados pela equipe foram divulgados' com 'Os relatórios, examin
- Em 'Estando as portas trancadas, os visitantes aguardaram no corredor', o desenvolvimento 
- Uma pessoa deve escolher frase equivalente a 'Apesar de os gestores terem sido avisados, a
- Em 'O revisor viu uma pasta contendo documentos antigos', a expressão 'contendo documentos
- Considere 'Os documentos a serem assinados amanhã estão separados'. Qual paráfrase conserv
- Na frase 'Ao receber o texto, Isabel identificou um erro', uma alternativa propõe 'Para qu
- Em 'É recomendável descansar após a viagem', a oração 'descansar após a viagem' apresenta 
- A presença de 'ao' em 'Ao iniciar a leitura, feche a janela' impede classificar o segmento
- Em 'Terminados os trabalhos, os participantes saíram', a forma 'terminados' é particípio q
- O período 'A equipe estava organizando o arquivo' contém obrigatoriamente duas orações por
- Em 'Para reduzir o consumo, apagou as luzes', a oração reduzida de infinitivo exprime fina
- A transformação de 'Apesar de saber a regra, ele se confundiu' em 'Porque sabia a regra, e
- Em 'Havia pessoas aguardando o resultado', o segmento de gerúndio pode caracterizar o nome
- Na expressão 'Antes de nós sairmos, apague as luzes', a flexão do infinitivo na primeira p
- Em 'Persistindo o ruído, interrompa o uso', o gerúndio permite desenvolvimento condicional
- A construção 'Por ter esquecido a senha, não conseguiu entrar' é equivalente, quanto à seq
- Em 'Sabendo que o caminho era longo, escolheram sair cedo', a oração reduzida de gerúndio 
- Em 'Os documentos, assinados ontem, foram enviados', a supressão das duas vírgulas é incap
- O particípio de 'Redigida a ata, os presentes assinaram' estabelece naturalmente que a red
- Em 'Concluídos os testes, os responsáveis divulgaram os resultados', 'os responsáveis' é o
- A oração 'Mesmo conhecendo as condições, concordou com o acordo' pode ser desenvolvida com
- Em 'Tendo divulgado o relatório, a equipe iniciou a revisão', trocar a reduzida por 'Enqua
- Em 'Os arquivos a serem revisados amanhã foram identificados hoje', a relativa 'os arquivo
- Ao reescrever 'Por terem sido interrompidas as atividades, houve atraso' como 'Porque inte
- Na transformação de 'Apesar de os alunos terem estudado, erraram', a redação 'Embora os al
- A oração 'Para evitar dúvidas, o redator acrescentou uma observação' pode ser transformada
```

## Pedido 9: Trovadorismo, Classicismo e Quinhentismo (`literatura.trovadorismo-classicismo-quinhentismo`, 4 tópicos de edital)

```
Matéria: literatura.trovadorismo-classicismo-quinhentismo (Trovadorismo, Classicismo e Quinhentismo)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Trovadorismo: sociedade medieval, oralidade e cancioneiros": 13
- "2. Cantigas de amor, amigo, escárnio e maldizer: análise textual": 13
- "3. Humanismo: crônicas, poesia palaciana e teatro de Gil Vicente": 13
- "4. Classicismo renascentista: medida nova, Camões e Os Lusíadas": 13
- "5. Quinhentismo: relatos de viajantes, informação e catequese no Brasil": 48

Já existem (não repita):
- Em um poema medieval, um homem lamenta não poder aproximar-se da dama que trata respeitosa
- Uma composição representa uma jovem que pergunta às ondas se seu amado regressará, repetin
- Ao analisar manuscritos medievais de cantigas galego-portuguesas, um estudante deve identi
- Um texto medieval elogia aparentemente a prudência de alguém, mas o contexto revela uma zo
- Durante uma revisão histórica, foi apresentada uma obra que dramatiza julgamento moral de 
- Uma estrofe possui sete sílabas poéticas por verso. Em classificação métrica tradicional, 
- Uma coletânea poética portuguesa, organizada por Garcia de Resende e publicada em 1516, re
- Um relato escrito em 1500 descreve a terra e seus habitantes para informar o rei português
- Ao comparar uma cantiga de amigo com um soneto renascentista, um leitor observa que a cant
- Uma composição medieval dirige ao alvo uma acusação explícita de desonestidade, sem constr
- Um fragmento de crônica descreve a crise política e o comportamento de grupos sociais, val
- Um leitor encontra poema composto por dois quartetos e dois tercetos, com versos decassilá
- Uma questão solicita reconhecer a função de Baco em Os Lusíadas. Considerando a ação mitol
- Dois textos do século XVI tratam do Brasil: o primeiro descreve paisagem e recursos para l
- Um estudante atribuiu a Gabriel Soares de Sousa a Carta de 1500 e a Caminha o Tratado Desc
- Um pesquisador compara a organização de Os Lusíadas com a de um soneto de Camões. Qual car
- Uma pesquisadora analisa cantiga em que a voz poética feminina pergunta à mãe pelo namorad
- Ao comparar duas sátiras medievais, o leitor identifica na primeira elogios ambíguos que r
- Uma análise do Auto da Barca do Inferno destaca o juízo moral de personagens alegóricas e 
- Um texto poético contém referências pagãs e exalta a viagem marítima portuguesa, mas també
- Em exercício de cronologia, afirmam-se três datas: 1516 para Cancioneiro Geral, 1527 para 
- Uma edição de poemas identifica a presença de refrão e paralelismo, mas o comentário críti
- Um professor analisa a Carta de Caminha como descrição de costumes indígenas e a compara a
- Um texto missionário do século XVI inclui falas dramatizadas em tupi para transmitir ensin
- Dois autores publicam ou elaboram textos no século XVI: Camões compõe uma epopeia e Gândav
- Uma cantiga medieval apresenta voz masculina que se declara servidor de uma dama distante 
- Na tradição galego-portuguesa, o uso de um eu lírico feminino em cantiga de amigo demonstr
- Em um manuscrito medieval, aparecem canções galego-portuguesas produzidas num contexto de 
- O Cancioneiro Geral, organizado por Garcia de Resende em 1516, é um dos três manuscritos f
- Uma encenação com tipos sociais que são julgados moralmente e expõem vícios por falas satí
- Na métrica tradicional, redondilha menor e redondilha maior têm, respectivamente, sete e d
- A Carta de Pero Vaz de Caminha, dirigida a D. Manuel I e datada de 1500, integra a tradiçã
- Nas cantigas, o refrão e o paralelismo são expressões equivalentes: ambos se definem somen
- Fernão Lopes é associado à prosa cronística portuguesa, enquanto Gil Vicente se destacou p
- O retorno de Sá de Miranda da Itália, adotado como marco de 1527, determinou que a medida 
- Os Lusíadas, publicado em 1572, contém dez cantos em oitavas decassilábicas e combina feit
- A literatura de informação do Quinhentismo corresponde exclusivamente à escrita de autos r
- O Auto da Barca do Inferno pode ser compreendido como obra de transição porque preserva im
- A presença de flora e povos indígenas num texto da época colonial assegura que seu autor d
- A presença de expressões amorosas numa cantiga de amigo permite concluir que o “amigo” des
- O caráter manuscrito dos cancioneiros demonstra que as cantigas de amor e amigo nasceram c
- Um texto lírico de Camões escrito em redondilhas pode coexistir historicamente com a poéti
- Os Lusíadas é exemplo de literatura informativa quinhentista brasileira porque seus dez ca
- No estudo dos textos religiosos do século XVI no Brasil, um auto dramático de José de Anch
- Em uma leitura do Quinhentismo, a descrição de florestas e costumes em Gândavo dispensa co
```

## Pedido 10: Farmacovigilância, tecnovigilância e uso racional de medicamentos (`farmacia.farmacovigilancia-assistencia-hospitalar`, 4 tópicos de edital)

```
Matéria: farmacia.farmacovigilancia-assistencia-hospitalar (Farmacovigilância, tecnovigilância e uso racional de medicamentos)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Farmacovigilância e notificação de suspeitas": 17
- "2. Tecnovigilância e hemovigilância": 17
- "3. Erros de medicação e segurança do paciente": 17
- "4. Farmacoeconomia e avaliação econômica": 17
- "5. Seleção e padronização pela Comissão de Farmácia e Terapêutica": 16
- "6. Uso racional de antimicrobianos e stewardship": 16

Já existem (não repita):
- Uma enfermeira suspeita que um antibiótico esteja relacionado a uma erupção cutânea surgid
- A suspeita fundamentada de evento adverso a medicamento pode ser notificada ao VigiMed mes
- O farmacêutico identifica comprimidos com cor atípica e falha de fechamento de um frasco, 
- Toda ocorrência clínica indesejada durante o uso de um medicamento é, obrigatoriamente, um
- Qual exemplo pertence mais diretamente ao campo da tecnovigilância?
- A tecnovigilância acompanha eventos adversos e queixas técnicas ligados a dispositivos méd
- Na suspeita de reação transfusional durante infusão de hemocomponente, qual medida inicial
- A hemovigilância limita-se às reações do paciente receptor e exclui intercorrências durant
- Um técnico separa a concentração errada de insulina, mas o equívoco é percebido na conferê
- Um erro de dispensação interceptado antes de o medicamento chegar ao paciente pode ser cla
- Um estudo compara dois esquemas pelos custos e por episódios de exacerbação evitados. Esse
- A análise de custo-utilidade pode empregar anos de vida ajustados pela qualidade como medi
- Qual atividade traduz a função essencial de uma Comissão de Farmácia e Terapêutica?
- Um antibiótico deve ser prescrito rotineiramente para qualquer quadro respiratório de prov
- Qual é uma ação típica de um programa de gerenciamento do uso de antimicrobianos?
- A melhora de um paciente após suspender um medicamento suspeito é elemento útil de causali
- Paciente iniciou três medicamentos e apresentou elevação de enzimas hepáticas, mas também 
- Uma contagem elevada de notificações espontâneas relativas a um fármaco permite calcular d
- Após divulgação de alerta, aumentam os relatos espontâneos de um medicamento. Sem dados de
- Em suspeita de defeito de equipamento médico, registrar modelo, número de série e contexto
- Uma cidadã deseja informar um defeito de dispositivo médico pós-comercialização. Qual afir
- A conciliação medicamentosa tem especial importância nas transições do cuidado, nas quais 
- Na classificação de uma reação transfusional, além do diagnóstico e gravidade, o manual na
- A escolha da CFT deve se orientar exclusivamente pelo menor preço unitário, pois eficácia 
- Durante a alta, a lista entregue ao paciente omite um fármaco habitual, sem registro de su
- Uma tecnologia com boa relação de custo-efetividade pode ainda produzir impacto orçamentár
- Um esquema custa R$ 3.000 a mais por paciente que o comparador e evita, em média, 0,15 int
- Uma cultura microbiológica positiva, isoladamente, obriga a iniciar antimicrobiano, ainda 
- A CFT quer selecionar duas formulações concorrentes para uma mesma necessidade clínica. Qu
- A ocorrência de vários relatos semelhantes pode gerar sinal de segurança, mas não estabele
- Paciente com infecção bacteriana e cultura disponível evolui bem; o antibiograma mostra se
- TACO e TRALI são designações equivalentes para qualquer quadro de dispneia surgido após tr
- Um paciente apresenta rash depois de iniciar fármaco; o frasco do mesmo lote tem falha de 
- Uma reação adversa não evitável pode ocorrer com prescrição e administração corretas, ao p
- Após transfusão, paciente desenvolve dispneia e hipoxemia. Há dúvida entre TACO e TRALI. Q
- A FMEA investiga preferencialmente fatos consumados, ao passo que a análise de causa raiz 
- Após série de erros de medicação relacionados à interface de prescrição, a gestão quer tan
- Se um medicamento apresenta menor custo e resultado superior ao comparador, a análise incr
- Uma tecnologia nova tem benefício clínico incremental comprovado e relação de custo-efetiv
- Na seleção institucional, classificar um item como classe A pela curva ABC demonstra que e
- Um medicamento novo custa R$ 5.000 a mais do que o comparador e proporciona 0,25 QALY adic
- O descalonamento de antimicrobianos consiste necessariamente em reduzir a dose do antibiót
- Um município adapta sua lista de medicamentos essenciais ao perfil de saúde local e às res
- Quanto à classificação AWaRe e ao monitoramento de consumo de antimicrobianos, assinale a 
- Após drenagem de abscesso intra-abdominal, paciente estabiliza e cultura identifica micror
```

## Pedido 11: Inteligência de Segurança Institucional do Poder Judiciário (Resolução CNJ 383/2021) (`seguranca-institucional.inteligencia-seguranca-institucional-cnj-383`, 4 tópicos de edital)

```
Matéria: seguranca-institucional.inteligencia-seguranca-institucional-cnj-383 (Inteligência de Segurança Institucional do Poder Judiciário (Resolução CNJ 383/2021))
Capítulos e quantas questões quero em cada um (total 100):
- "1. O SInSIPJ na Resolução CNJ 383/2021": 34
- "2. Composição, coordenação e cooperação": 33
- "3. Atuação prioritária, controle e noções de inteligência": 33

Já existem (não repita):
- A Resolução CNJ 383/2021 instituiu o Sistema de Inteligência de Segurança Institucional do
- A finalidade do SInSIPJ, segundo a Resolução CNJ 383/2021, é:
- Para a resolução, atividade de inteligência é o exercício permanente e sistemático de açõe
- A atividade de inteligência, no conceito da Resolução CNJ 383/2021, caracteriza-se por ser
- Entre os fundamentos da resolução está a necessidade de atuação preventiva e proativa para
- A lógica sistêmica da atividade de inteligência, segundo a resolução, possibilita:
- O Comitê Gestor do Sistema Nacional de Segurança do Poder Judiciário (SINASPJ) tem entre s
- No SInSIPJ, o órgão central e de coordenação é o:
- O Supremo Tribunal Federal integra obrigatoriamente o SInSIPJ como órgão de cúpula.
- São órgãos de cúpula do SInSIPJ, entre outros:
- Os demais órgãos do Poder Judiciário integram o SInSIPJ e devem designar ao menos um servi
- O servidor que coordena os trabalhos do SInSIPJ é designado:
- Integrantes do Sistema Brasileiro de Inteligência (SISBIN) participam automaticamente do S
- Quanto às ligações interinstitucionais, as unidades de inteligência do Poder Judiciário de
- As unidades de inteligência do Judiciário podem eventualmente trocar informações de intere
- Os documentos produzidos pela atividade de inteligência, segundo a resolução, devem ser:
- O Conselho Superior da Justiça do Trabalho e o Tribunal Superior Eleitoral estão entre os 
- O coordenador do SInSIPJ deve ser servidor em exercício no:
- A avaliação de riscos, para subsidiar o planejamento e a implementação de medidas de segur
- Não figura entre as áreas de atuação prioritária do SInSIPJ:
- O SInSIPJ deve identificar vulnerabilidades e ameaças que afetem a segurança dos magistrad
- Os estudos de inteligência sobre cenários criminais, previstos na resolução, visam a ameaç
- O Comitê Gestor do SINASPJ pode solicitar à Presidência do CNJ a constituição de grupo de 
- O grupo de apoio previsto na resolução terá pelo menos um integrante indicado:
- O grupo de apoio pode inspecionar as unidades de inteligência dos órgãos do Judiciário, co
- Aplica-se subsidiariamente à Resolução CNJ 383/2021, no que for cabível e compatível com a
- A atividade de inteligência confunde-se com a investigação criminal, pois ambas têm como o
- A contrainteligência tem como foco:
- O ciclo de produção do conhecimento de inteligência compreende, em sequência, planejamento
- Na Lei de Acesso à Informação, os graus de sigilo de informações classificadas são:
- O acesso a conhecimentos de inteligência classificados deve obedecer ao princípio da neces
- A análise permanente e sistemática de situações de interesse da segurança institucional te
- A Resolução CNJ 383/2021 relaciona-se à Política Nacional de Segurança do Poder Judiciário
- Segundo a resolução, a participação de integrantes do SISBIN no SInSIPJ depende de:
- A Resolução CNJ 383/2021 entrou em vigor na data de sua publicação.
- Os Tribunais de Justiça dos Estados e do Distrito Federal, no SInSIPJ, figuram como:
- A atividade de inteligência prevista na resolução tem caráter preventivo e proativo, volta
- A difusão, no ciclo de produção do conhecimento de inteligência, corresponde:
- O ativo protegido pela atividade de inteligência prevista na resolução é:
- A resolução exige que o servidor designado para a atividade de inteligência nos órgãos do 
```

## Pedido 12: Constituição do Amapá: segurança pública e Polícia Civil (`legislacao.amapa-constituicao-seguranca-publica`, 3 tópicos de edital)

```
Matéria: legislacao.amapa-constituicao-seguranca-publica (Constituição do Amapá: segurança pública e Polícia Civil)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Disposições gerais da segurança pública": 25
- "2. A Polícia Civil na Constituição estadual": 25
- "3. Polícia Militar, Bombeiros, Polícia Técnico-Científica e Polícia Penal": 25
- "4. Dispositivos correlatos e direitos do cidadão": 25

Já existem (não repita):
- Segundo a Constituição do Estado do Amapá, a segurança pública, dever do Estado, direito e
- Na redação atual da Constituição do Amapá, integram o rol de órgãos de segurança pública s
- A Polícia Penal passou a integrar o rol dos órgãos de segurança pública da Constituição do
- Pela Constituição do Amapá, é dever dos órgãos de segurança pública dar aos policiais civi
- A Constituição do Amapá determina que o Poder Executivo instale delegacias especializadas 
- A Constituição do Amapá assegura autonomia administrativa e financeira aos serviços perici
- Segundo a Constituição do Amapá, o policial civil, penal ou militar denunciado por crime d
- Pela Constituição do Amapá, a coordenação da prevenção de eventos desastrosos, do socorro 
- A isonomia de remuneração entre peritos, médicos-legistas e delegados de polícia continua 
- Segundo o art. 79 da Constituição do Amapá, a Polícia Civil é instituição permanente, com 
- De acordo com a Constituição do Amapá, a Polícia Civil é dirigida por:
- Incumbe à Polícia Civil do Amapá, ressalvada a competência da União, exercer com exclusivi
- Sobre os delegados de polícia na Constituição do Amapá, é correto afirmar que:
- Aos delegados de polícia do Amapá aplicam-se as vedações previstas para os membros do Mini
- Pelas vedações que a Constituição do Amapá estende aos delegados, é permitido ao delegado 
- Segundo a Constituição do Amapá, o policial civil condenado à pena privativa de liberdade 
- A demissão imediata prevista no art. 81 da Constituição do Amapá alcança o policial civil 
- Segundo o art. 80 da Constituição do Amapá, a lei orgânica e o estatuto da Polícia Civil d
- Compete ao Estado do Amapá legislar sobre a organização, as garantias, os direitos e os de
- Pela Constituição do Amapá, cabe ao Ministério Público exercer o controle externo da ativi
- Em comparação com a Constituição Federal, a Constituição do Amapá acrescenta, quanto à Pol
- Segundo a Constituição do Amapá, os delegados de polícia de carreira, bacharéis em Direito
- Na Constituição do Amapá, a Polícia Militar e o Corpo de Bombeiros Militar são qualificado
- À Polícia Militar do Amapá incumbe, além de outras atribuições legais, o policiamento oste
- Com a Emenda Constitucional 68/2024, o Comandante-Geral da Polícia Militar do Amapá passou
- O Comando-Geral da Polícia Militar deve programar, no início de cada semestre, cursos e pa
- Entre as atribuições do Corpo de Bombeiros Militar previstas na Constituição do Amapá, est
- Pela Constituição do Amapá, o Corpo de Bombeiros Militar pode embargar e interditar obras,
- Cabe à Polícia Técnico-Científica do Amapá, segundo a Constituição estadual:
- A Polícia Técnico-Científica do Amapá é dirigida por delegado de polícia da classe especia
- O cargo de perito, pela Constituição do Amapá, é privativo de portadores de diploma de nív
- Segundo o art. 90-A da Constituição do Amapá, a Polícia Penal:
- À Polícia Penal do Amapá incumbe a segurança dos estabelecimentos penais.
- Na Constituição do Amapá, gozam expressamente de autonomia administrativa e financeira:
- Pela Constituição do Amapá, qualquer cidadão pode apresentar queixa contra autoridade poli
- A queixa do cidadão contra autoridade policial, prevista na Constituição do Amapá, só pode
- Pela Constituição do Amapá, são isentos de tarifa nos transportes coletivos urbanos, rodov
- Os municípios do Amapá podem constituir guardas municipais destinadas à proteção de seus b
- A Constituição do Amapá determina que o Poder Executivo mantenha, no sistema prisional e n
- Entre as mudanças da Emenda Constitucional 61/2020 na Constituição do Amapá, NÃO está:
- Assinale a alternativa que relaciona corretamente o órgão e quem o dirige, segundo a Const
```

## Pedido 13: Áreas de Risco (`minas-gerais.areas-risco`, 3 tópicos de edital)

```
Matéria: minas-gerais.areas-risco (Áreas de Risco)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Perigo, risco e mapeamento": 25
- "2. Vulnerabilidade e exposição social": 25
- "3. Prevenção, mitigação e ordenamento territorial": 25
- "4. Reescrita, interpretação e cobrança da IDECAN": 25

Já existem (não repita):
- Assinale a afirmação correta sobre uma carta de suscetibilidade a deslizamentos.
- Qual conjunto de elementos oferece uma leitura mais completa do risco em uma área urbana?
- Qual processo pode ser classificado como movimento de massa?
- Em um mapeamento, o termo exposição refere-se a qual situação?
- Qual é uma limitação importante de mapas de risco e suscetibilidade?
- Após chuva prolongada, surgem trincas em uma encosta cortada para abertura de acesso. Qual
- Um mapa municipal delimita setor sujeito a inundação. Qual uso é coerente com esse instrum
- Qual afirmação diferencia adequadamente suscetibilidade e exposição?
- Duas famílias vivem na mesma rua atingível por enxurrada, mas apenas uma tem pessoa com mo
- Uma escola está fora de uma encosta instável, mas sua única via de acesso cruza trecho suj
- Qual formulação descreve vulnerabilidade social sem tratá-la como culpa individual?
- Um plano de ordenamento pretende reduzir risco geológico em bairro ainda em expansão. Qual
- Uma obra de drenagem reduz água acumulada em uma encosta ocupada. Qual afirmação é correta
- Um setor não aparece em carta municipal antiga, mas a ocupação e a drenagem foram alterada
- Uma família permanece em casa, mas perde acesso seguro ao trabalho após interdição de uma 
- Uma chuva intensa atinge duas encostas com inclinação semelhante, mas uma tem drenagem man
- Uma carta identifica suscetibilidade, mas não apresenta o número atual de moradores. Qual 
- O município pretende regularizar serviços em ocupação consolidada situada em setor sujeito
- Um sistema envia alerta por mensagem de texto, mas parte dos moradores não tem celular ou 
- Uma carta de suscetibilidade aponta propensão a movimentos de massa em setor sem construçõ
- Após contenção de uma encosta, o gestor afirma que o bairro está livre de qualquer risco e
- Uma vila recebe aviso de risco, mas o ponto de encontro fica além de uma ponte que costuma
- Um mapa de inundação antiga continua sendo usado após canalização e expansão urbana a mont
- Uma avaliação considera apenas encostas e ignora a rede de água, a unidade de saúde e as v
- Uma notícia atribui todos os danos de um deslizamento à chuva, embora haja cortes sem dren
- Uma campanha ensina moradores a reconhecer sinais e acionar a defesa civil. Qual resultado
- Após a atualização de um mapa, moradores informam que um novo corte de talude surgiu e que
- Uma área suscetível tem baixa ocupação hoje, mas recebe proposta de loteamento e não possu
- Em uma resposta a desastre, o abrigo temporário é acessível, mas fica distante de medicame
- Um loteamento novo é proposto em setor suscetível, embora ainda sem moradores; o projeto p
- Uma carta de suscetibilidade indica propensão espacial, mas não prevê a data exata de um e
- Se uma área não aparece em mapa antigo, não pode apresentar risco geológico atualmente.
- Pessoas, vias e serviços públicos podem ser elementos expostos a um processo perigoso.
- Vulnerabilidade social é sinônimo de pobreza e permanece imutável ao longo do tempo.
- Ordenamento territorial pode evitar que novas ocupações ampliem a exposição a processos pe
- A leitura de um mapa deve considerar escala, método, data de referência e cobertura territ
- A instalação de um muro de contenção elimina necessariamente todos os riscos geológicos do
- Pessoas no mesmo setor exposto podem ter vulnerabilidades e necessidades de resposta difer
- Um sistema de alerta impede a ocorrência física de deslizamentos ou inundações.
- O planejamento urbano pode combinar controle de ocupação futura e intervenção em áreas já 
- A chuva é a única causa de um deslizamento, sendo irrelevantes a ocupação, o terreno e a d
- Relatos de moradores podem complementar o mapeamento técnico ao indicar mudanças recentes 
- A remoção de moradores, isoladamente, resolve necessariamente as causas estruturais da vul
- Obras, ocupação e drenagem podem mudar, por isso um mapa precisa ser lido conforme sua dat
- O atendimento a pessoas em abrigo encerra toda a resposta a um desastre e torna dispensáve
```

## Pedido 14: Estatuto dos Policiais-Militares de Santa Catarina (Lei 6.218/1983) (`legislacao.estatuto-pm-santa-catarina-lei-6218`, 3 tópicos de edital)

```
Matéria: legislacao.estatuto-pm-santa-catarina-lei-6218 (Estatuto dos Policiais-Militares de Santa Catarina (Lei 6.218/1983))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Generalidades, hierarquia e cargo": 20
- "2. Deveres, ética e responsabilização": 20
- "3. Direitos, recursos, remuneração, promoções e licenças": 20
- "4. Prerrogativas e situações especiais": 20
- "5. Exclusão do serviço ativo, tempo de serviço e prorrogação": 20

Já existem (não repita):
- Com a redação dada pela LC 880/2025, a Lei 6.218/1983 passou a denominar os integrantes da
- Segundo o Estatuto da PMSC, os militares estaduais temporários, incorporados por processo 
- Estão na inatividade, segundo o art. 3º da Lei 6.218/1983:
- Na PMSC, posto é o grau hierárquico do Oficial, conferido por ato do Governador do Estado 
- Quanto à precedência, o Estatuto da PMSC estabelece que o aluno do Curso de Formação de Sa
- Na Lei 6.218/1983, as obrigações não catalogadas em quadro de organização são cumpridas co
- Em igualdade de posto ou graduação, os policiais-militares da ativa têm precedência sobre 
- Pelo Estatuto da PMSC, o policial-militar que cumpre ordem recebida:
- Segundo a Lei 6.218/1983, os cabos e soldados são essencialmente elementos de execução.
- Ao ser promovido a 3º Sargento, a praça da PMSC presta, em solenidade especialmente progra
- Sobre a violação das obrigações e deveres na Lei 6.218/1983, assinale a correta.
- Segundo o Estatuto da PMSC, os policiais-militares, nos crimes militares definidos em lei,
- As penas disciplinares de detenção ou prisão não podem ultrapassar trinta dias, e o Consel
- O Estatuto da PMSC proíbe manifestações coletivas sobre atos de superiores e as de caráter
- Pela Lei 6.218/1983, com a redação da LC 749/2019, o pedido de reconsideração, a queixa ou
- Segundo o Estatuto da PMSC, a praça adquire estabilidade com dez ou mais anos de tempo de 
- Entre os direitos dos militares estaduais de Santa Catarina previstos no art. 50 da Lei 6.
- Um policial-militar de SC com 3 anos de efetivo serviço se candidata a vereador. Pelo Esta
- Na contagem das quotas de soldo para a inatividade, a fração de tempo igual ou superior a 
- São critérios de promoção dos militares estaduais previstos no art. 62 da Lei 6.218/1983, 
- A promoção por bravura, no Estatuto da PMSC, independe da existência de vaga e pode ocorre
- Pelo Estatuto da PMSC, o policial-militar é promovido ao posto ou graduação imediatamente 
- Sobre a licença especial na Lei 6.218/1983, é correto afirmar:
- A licença para tratar de interesses particulares na PMSC:
- Os policiais-militares de SC têm direito a afastamento total do serviço de oito dias por n
- Segundo o Estatuto da PMSC, o policial-militar preso em flagrante por autoridade policial 
- Na Lei 6.218/1983, a agregação e a reversão são feitas, respectivamente para oficiais e pa
- Pelo art. 83 da Lei 6.218/1983, será agregado o policial-militar que:
- O policial-militar agregado por ter sido nomeado para cargo público civil temporário, não 
- Sobre o excedente na PMSC, assinale a correta.
- O policial-militar de SC colocado à disposição de órgão fora da Polícia Militar para missã
- O policial-militar que desempenha cargo de assessoria policial-militar na Assembleia Legis
- Segundo o Estatuto da PMSC, é considerado ausente o policial-militar que, por mais de quar
- Pela redação da LC 762/2020, a idade-limite para a transferência de ofício para a reserva 
- O policial-militar reformado por incapacidade definitiva que for julgado apto em inspeção 
- Com a redação da LC 765/2020, o militar estadual julgado definitivamente incapaz por ferim
- Sobre o licenciamento na Lei 6.218/1983, assinale a correta.
- A exclusão a bem da disciplina do Aspirante-a-Oficial e das praças com estabilidade assegu
- No Estatuto da PMSC, a deserção acarreta:
- Os períodos de tempo de serviço das praças, segundo o art. 149 da Lei 6.218/1983, são:
- A praça com estabilidade assegurada serve independentemente de outras formalidades, sujeit
- O Aluno do Curso de Formação de Oficiais que trancar definitivamente a matrícula depois de
- Pela Lei 6.218/1983, não é computado para nenhum efeito o tempo passado em licença para tr
```

## Pedido 15: IBGE: Conhecimentos Técnicos do 12º Censo Agropecuário, Florestal e Aquícola (`estatistica.censo-agropecuario-ibge`, 3 tópicos de edital)

```
Matéria: estatistica.censo-agropecuario-ibge (IBGE: Conhecimentos Técnicos do 12º Censo Agropecuário, Florestal e Aquícola)
Capítulos e quantas questões quero em cada um (total 100):
- "1. O IBGE e o Censo Agropecuário, Florestal e Aquícola": 17
- "2. Estrutura organizacional censitária": 17
- "3. Instrumentos de trabalho e navegação em campo": 17
- "4. Divisão territorial, setores e povos e comunidades tradicionais": 17
- "5. Endereço e coordenadas geográficas": 16
- "6. Estabelecimento agropecuário e situações especiais": 16

Já existem (não repita):
- Segundo a apostila do IBGE, o Censo Agropecuário, Florestal e Aquícola investiga o univers
- A rede nacional de pesquisa e disseminação do IBGE é composta por:
- Sobre o histórico dos Censos Agropecuários no Brasil, é correto afirmar que:
- A concepção do Censo Agropecuário leva em conta as recomendações do Programa Mundial de Ce
- Os resultados do Censo Agropecuário são divulgados apenas no nível das grandes regiões, se
- A apostila cita como exemplo de política pública que pode ser avaliada com os dados do Cen
- Aquícola refere-se à criação e à multiplicação de plantas e animais cujo ciclo de vida, em
- O Centro Nacional de Qualidade (CNQ) é vinculado:
- No posto censitário, a função responsável por gerir o posto e garantir a observância das i
- O Agente Censitário Regional (ACR) é subordinado ao AOR e gerencia o trabalho dos Agentes 
- A função que serve de elo entre quem coleta as informações e quem gerencia o posto censitá
- Entre as atribuições do Agente Censitário Supervisor (ACS) citadas na apostila, está:
- Para o 12º Censo Agropecuário, a apostila prevê a contratação de cerca de:
- O Recenseador, durante a coleta, fica lotado no posto censitário e deve retornar a campo q
- O setor censitário é a menor área geográfica definida pelo IBGE e constitui a unidade terr
- O instrumento fixado no posto que traz a visão geral de todos os setores censitários do mu
- O Recenseador deve visitar apenas os estabelecimentos que constam da lista prévia de ender
- Em um mapa na escala 1:50.000, dois pontos separados por 3 cm estão, no terreno, a uma dis
- Na interpretação de imagens de satélite, a apostila recomenda observar forma, tamanho, loc
- NÃO é uma das partes do descritivo do setor censitário:
- Linhas secas, usadas como limite de setor, são retas não materializadas no terreno, que po
- Se o Recenseador perceber que um limite do setor mudou em campo, ele pode ajustar o períme
- Nos “setores a serem excluídos”, indicados no descritivo, o Recenseador deve:
- Segundo a apostila, os municípios brasileiros:
- Os distritos dividem integralmente os municípios, são criados por legislação municipal e n
- O geocódigo do setor censitário reúne, entre outras informações:
- Setores de tipo especial, como quartéis ou unidades prisionais, só serão visitados no Cens
- O tipo de setor com mais de 50 domicílios associados a projetos de assentamento, apontado 
- Quanto ao pertencimento a povos e comunidades tradicionais, o Recenseador deve:
- As Áreas de Interesse Operacional (AIOs) seguem exatamente os limites dos setores censitár
- A negociação prévia do acesso às áreas de povos e comunidades tradicionais é assegurada, s
- No padrão CNEFE, o logradouro deve ser registrado preferencialmente pelo nome oficial reco
- O componente do endereço que é obrigatório quando não há número nem complemento que permit
- Para facilitar a localização em área rural, o Recenseador pode registrar no complemento do
- Sobre latitude e longitude, assinale a alternativa correta.
- Nas áreas rurais, a coordenada do estabelecimento deve ser capturada manualmente, idealmen
- Uma coordenada com latitude −22,326 e longitude −42,669 indica um ponto situado:
- Para ser estabelecimento agropecuário, a unidade precisa ter área mínima definida em lei e
- Em uma residência rural com horta doméstica, algumas frutíferas e galinhas apenas para laz
- A classificação de uma propriedade como unidade recenseável é feita pelo questionário de a
- Assinale a situação que caracteriza um produtor sem área.
- Um pesque-pague que apenas compra peixes já criados para os clientes pescarem não é consid
- Um estabelecimento cuja área se estende por dois setores censitários e que possui sede dev
- Áreas não contínuas exploradas por um mesmo produtor formam um único estabelecimento quand
- Uma única propriedade rural pode conter mais de um estabelecimento agropecuário quando dif
- Em um estabelecimento sob disputa judicial, deve ser considerado produtor:
- Estabelecimentos que não tiveram produção no período de referência, por perda climática ou
- Em área de exploração comunitária na qual cada família decide o que produzir e o destino d
- Uma unidade de confinamento de gado (boitel) que só engorda animais de terceiros, sem anim
```

## Pedido 16: Ciências atuariais (`previdencia.ciencias-atuariais`, 3 tópicos de edital)

```
Matéria: previdencia.ciencias-atuariais (Ciências atuariais)
Capítulos e quantas questões quero em cada um (total 100):
- "Fundamentos da matemática atuarial e do valor do dinheiro": 20
- "Tábuas biométricas e rendas contingentes": 20
- "Regimes financeiros, métodos de financiamento e provisões": 20
- "Avaliação atuarial em RPPS e previdência complementar": 20
- "Hipóteses atuariais, ALM, riscos e ética profissional": 20

Já existem (não repita):
- O fator de desconto para um período, à taxa efetiva i, é 1/(1+i).
- O valor atual atuarial de um pagamento contingente dispensa a probabilidade de ocorrência 
- Na tábua de mortalidade, l_x designa o número teórico de pessoas sobreviventes à idade exa
- Em tábua de decremento único, as probabilidades anuais p_x e q_x somam um.
- Repartição simples e capitalização são dois nomes do mesmo regime financeiro.
- Provisão matemática é sinônimo de saldo da conta bancária disponível para pagar aposentado
- O ALM procura compatibilizar riscos e fluxos dos ativos com as obrigações previdenciárias.
- Em uma renda vitalícia, a melhora da sobrevivência tende a aumentar o valor esperado dos p
- A taxa anual efetiva de 12% corresponde, necessariamente, à taxa mensal efetiva de 1%.
- Uma renda anual antecipada unitária pode incluir pagamento na própria data de início do co
- O método de financiamento e o regime financeiro são conceitos intercambiáveis na avaliação
- Em um RPPS, a Nota Técnica Atuarial deve dar suporte às formulações e hipóteses empregadas
- Duração do passivo e expectativa de vida dos segurados são, por definição, a mesma grandez
- Um plano pode ser solvente em valor presente e apresentar insuficiência de caixa no próxim
- Um aumento arbitrário da taxa de juros atuarial comprova, por si só, recuperação patrimoni
- Sob hipótese de decremento único, a probabilidade de sobreviver dois anos é, em geral, p_x
- O valor da provisão de benefícios concedidos deve, por definição, ser igual ao valor nomin
- Uma mudança não paralela na curva de juros pode deixar risco residual mesmo quando as dura
- A modalidade contribuição definida afasta em qualquer circunstância a necessidade de model
- Na avaliação profissional, conflitos de interesses e limitações dos dados devem ser comuni
- Um capital de R$ 1.210 será pago com certeza daqui a dois anos à taxa efetiva de 10% ao an
- Em uma tábua, l_40=50.000 e l_41=49.500. Qual é a probabilidade q_40 de morte no intervalo
- Qual afirmação descreve adequadamente a repartição simples?
- Qual documento reúne as bases técnicas, os modelos e as formulações da avaliação atuarial 
- Uma carteira possui muitos ativos de longo prazo, mas deverá pagar benefícios expressivos 
- Um contrato prevê R$ 10.000 em um ano somente se ocorrer um evento com probabilidade de 20
- Sob a mesma tábua de decremento único, qual expressão representa p_x?
- Uma pessoa de 60 anos tem chance de sobrevivência ao próximo ano de 98%. Será paga uma úni
- O gestor adota taxa nominal anual de 10% e inflação esperada de 4%. Qual relação descreve 
- Na renda vitalícia anual antecipada unitária, em comparação à renda postecipada sob a mesm
- No regime de repartição de capitais de cobertura, a descrição mais correta é:
- Um plano apresenta compromisso atuarial e patrimônio suficientes no longo prazo, mas caixa
- Uma equipe alterou simultaneamente mortalidade, juros e crescimento salarial para um cenár
- Na avaliação de RPPS, qual conduta é tecnicamente adequada ao encontrar registros de nasci
- Qual associação diferencia adequadamente as modalidades de previdência complementar?
- Considere l_65=80.000, l_66=79.200 e l_67=78.012. Qual a probabilidade de sobreviver dois 
- Um plano de renda vitalícia usa hipótese de mortalidade menor do que a anterior. Mantidos 
- Um RPPS tem provisão matemática calculada de R$ 40 milhões e ativos garantidores de R$ 35 
- Para imunizar parcialmente a exposição a pequenas variações paralelas de juros, um gestor 
- Um patrocinador pede ao atuário que oculte uma hipótese sem aderência para apresentar resu
```

## Pedido 17: Direito penal: dogmática avançada e bem jurídico (`direito-penal.dogmatica-avancada-bem-juridico`, 3 tópicos de edital)

```
Matéria: direito-penal.dogmatica-avancada-bem-juridico (Direito penal: dogmática avançada e bem jurídico)
Capítulos e quantas questões quero em cada um (total 100):
- "Bem jurídico penal e princípios de contenção do poder punitivo": 20
- "Funcionalismo penal: Roxin, Jakobs e a crítica ao direito penal do inimigo": 20
- "Imputação objetiva: risco proibido, realização do resultado e alcance do tipo": 20
- "Sociedade de risco, expansão penal e crimes de perigo abstrato": 20
- "Autoria e domínio do fato; responsabilidade penal da pessoa jurídica": 20

Já existem (não repita):
- Em uma ação de contaminação de rio, a acusação aponta a integridade do ecossistema como in
- Em dogmática penal, qual elemento corresponde ao interesse juridicamente tutelado e não à 
- Um pesquisador sustenta que a incriminação deve ser usada apenas quando mecanismos regulat
- Qual enunciado apresenta corretamente a fragmentariedade no Direito Penal contemporâneo?
- Ao impugnar tipo penal vago cuja finalidade declarada é preservar a moralidade social, a d
- Um bem jurídico supraindividual pode ser protegido penalmente mesmo sem que se identifique
- Fragmentariedade significa que o Direito Penal deve atuar como resposta inicial a qualquer
- A função crítico-limitadora do bem jurídico fornece critério de discussão da legitimidade 
- Como a ofensividade exige lesão consumada, todos os delitos de perigo abstrato são, por de
- Qual associação entre autor e eixo explicativo do funcionalismo penal está correta?
- Em estudo comparativo, qual noção é especialmente central ao funcionalismo sistêmico-norma
- Após notar que o motorista ao lado dirigia de modo errático, outro condutor continua a agi
- Sobre o modelo denominado direito penal do inimigo, assinale a formulação mais precisa.
- Um prestador vende equipamento de uso ordinário, sem conhecer nem ajustar-se a plano crimi
- O funcionalismo teleológico de Roxin dialoga com finalidades político-criminais, sem trata
- No funcionalismo de Jakobs, a distribuição de papéis sociais prova automaticamente a culpa
- A categoria direito penal do inimigo, por si só, autoriza no sistema brasileiro retirar a 
- A proibição de regresso, nas formulações normativistas, não exclui de modo automático a re
- Segundo a formulação clássica da imputação objetiva do resultado, qual requisito integra o
- Uma conduta é condição causal de uma morte, mas o evento decorre de risco extraordinário e
- Médico habilitado emprega procedimento indicado, dentro da técnica e com deveres observado
- Um agente causa pequena lesão para desviar objeto que atingiria fatalmente terceiro. Sem a
- Uma norma exige proteção contra explosões em laboratório. O laboratório descumpre a regra;
- Na imputação objetiva do resultado, a criação de um risco juridicamente desaprovado é rele
- O princípio da confiança permite que o integrante de equipe ignore falha grave e evidente 
- Na análise do risco permitido, a conformidade literal com uma regra técnica é indício rele
- A imputação objetiva e o domínio do fato são teorias intercambiáveis: ambas existem exclus
- Na classificação usual da estrutura de delitos, o crime de perigo concreto se caracteriza 
- Qual afirmação caracteriza corretamente um crime de perigo abstrato?
- Ao estudar a sociedade de risco e os processos contemporâneos de criminalização, o candida
- Qual crítica corresponde ao conceito de direito penal simbólico no contexto de expansão cr
- Uma lei prevê delito de perigo abstrato relativo à segurança coletiva. O juiz discute se é
- Em crimes de perigo abstrato, a dispensa de demonstração de perigo concreto não significa 
- A sociedade de risco e a expansão do Direito Penal são expressões que significam, necessar
- O princípio da precaução, discutido em matéria ambiental, revoga a exigência de tipo legal
- O uso penal de instrumentos regulatórios pode ser legítimo em certas hipóteses, mas uma in
- Qual é a finalidade dogmática primordial da teoria do domínio do fato no concurso de pesso
- De acordo com o art. 225, § 3º, da Constituição, infrações ambientais podem acarretar sanç
- Uma empresa é investigada por crime ambiental sem denúncia concomitante contra pessoa natu
- Na redação do art. 3º da Lei 9.605/1998, a responsabilização penal ambiental de pessoa jur
- Denúncia por crime tributário afirma apenas que determinado réu era administrador e, por i
- O domínio funcional do fato, associado à coautoria, considera a divisão de tarefas e a con
- A decisão de representante no benefício da entidade, nos termos da lei ambiental, dispensa
- O afastamento da dupla imputação necessária pelo STF significa que pessoa jurídica pode re
- O fato de alguém ser proprietário majoritário de empresa basta para considerá-lo autor med
```

## Pedido 18: Serviço Social na saúde e no hospital (`servico-social.saude-hospitalar`, 2 tópicos de edital)

```
Matéria: servico-social.saude-hospitalar (Serviço Social na saúde e no hospital)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Serviço Social no SUS: projeto profissional, competências e parâmetros": 17
- "2. Trabalho hospitalar, urgências e planejamento da alta": 17
- "3. Rede de proteção, atenção básica, violência e continuidade do cuidado": 17
- "4. Saúde mental, atenção psicossocial e redução de danos": 17
- "5. Cuidados paliativos, família, autonomia e luto": 16
- "6. Orientação previdenciária, benefícios e documentação social": 16

Já existem (não repita):
- A atuação do assistente social no SUS considera condições de vida e barreiras de acesso qu
- Uma usuária com doença crônica perde consultas por não ter transporte. Qual resposta melho
- Em hospital público, a prescrição de medicamentos e a elaboração de laudos clínicos consti
- Conforme a regulamentação profissional, qual atividade corresponde a atribuição privativa 
- Os Parâmetros do CFESS organizam o trabalho na saúde em atendimento direto, mobilização e 
- Qual situação exemplifica o eixo de investigação, planejamento e gestão dos Parâmetros do 
- Na equipe multiprofissional, toda informação íntima narrada pelo usuário pode ser comparti
- Em discussão de caso, qual conduta sobre sigilo profissional é mais adequada?
- A alta médica é uma decisão clínica e a identificação de obstáculos sociais pode exigir ar
- Paciente tem alta médica, mas não há alimentação, acesso domiciliar nem suporte para o cui
- Identificada ausência de cuidador no domicílio, o assistente social pode, por autoridade p
- Qual das tarefas NÃO é considerada atribuição própria do Serviço Social pelos Parâmetros d
- A comunicação formal do óbito aos familiares constitui, por padrão, função própria e obrig
- Após falecimento de paciente, a família pede orientação. Qual ação é apropriada ao Serviço
- Em solicitação de alta a pedido, o assistente social pode contribuir identificando motivos
- Uma pessoa internada manifesta desejo de sair para cuidar dos filhos. Qual iniciativa inic
- A Atenção Primária à Saúde pode ser acionada para favorecer acompanhamento territorial e c
- Para um recém-egresso de internação que precisa de curativos e acompanhamento territorial,
- O encaminhamento ao CRAS equivale à garantia imediata de benefício e encerra qualquer resp
- A equipe identifica criança em possível situação de violência. Qual ação social é mais ade
- A notificação de violência nos fluxos sanitários é juridicamente idêntica à apresentação d
- Qual alternativa distingue corretamente CRAS e CREAS em articulação hospitalar?
- A avaliação de rede familiar deve considerar o que a própria pessoa identifica como apoio,
- Usuário vive em situação de rua e necessita de seguimento após urgência. Qual resposta é m
- A redução de danos no cuidado relacionado a álcool e outras drogas condiciona o primeiro a
- No âmbito da RAPS, o Projeto Terapêutico Singular caracteriza-se principalmente por qual e
- Os CAPS integram a RAPS e podem construir planos de cuidado em conjunto com usuários, fami
- A redução de danos aplicada ao cuidado em álcool e outras drogas envolve:
- A existência de diagnóstico psiquiátrico, isoladamente, autoriza concluir incapacidade civ
- Uma pessoa com transtorno mental está em crise, estabiliza-se e necessita de continuidade 
- Em situações de crise, a articulação social com CAPS e outros pontos da RAPS pode ser plan
- Qual afirmação é correta sobre a natureza dos cuidados paliativos no SUS?
- A Política Nacional de Cuidados Paliativos pressupõe suspensão de tratamentos modificadore
- Em planejamento paliativo domiciliar, qual contribuição é especificamente pertinente ao Se
- O assistente social em cuidados paliativos pode analisar rede de cuidadores, obstáculos ma
- Qual informação sobre o BPC é adequada durante orientação a uma família em hospital públic
- O BPC é prestação assistencial não contributiva e não se confunde com aposentadoria previd
- Trabalhador pede auxílio por incapacidade temporária após cirurgia. Qual orientação é apro
- Ao orientar sobre auxílio por incapacidade temporária, o assistente social hospitalar pode
- Usuária precisa de atendimento fora de seu município e pergunta sobre TFD. Qual resposta d
```

## Pedido 19: Cachoeiras, Grutas e Cavernas (`minas-gerais.cachoeiras-grutas-cavernas`, 2 tópicos de edital)

```
Matéria: minas-gerais.cachoeiras-grutas-cavernas (Cachoeiras, Grutas e Cavernas)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Cachoeiras e paisagens fluviais de Minas Gerais": 25
- "2. Grutas, cavernas e relevo cárstico": 25
- "3. Patrimônio, visitação e conservação": 25
- "4. Reescrita e como a IDECAN cobra": 25

Já existem (não repita):
- Uma cachoeira é melhor compreendida como qual elemento da paisagem?
- Qual atrativo se encontra no Parque Natural Municipal das Andorinhas, em Ouro Preto?
- Qual fator pode aumentar ou reduzir a vazão observada numa cachoeira?
- Qual afirmação relaciona corretamente uma caverna mineira a sua localização?
- Qual unidade de relevo é frequentemente associada à dissolução de rochas solúveis?
- Qual conduta ajuda a proteger uma caverna aberta à visitação?
- Qual descrição explica de modo adequado a formação de um relevo cárstico?
- Sobre a visitação em ambientes naturais, assinale a alternativa INCORRETA.
- A associação entre Gruta Rei do Mato e Monumento Natural Estadual aponta para qual interpr
- Qual relação entre chuva e cachoeira é cientificamente mais cuidadosa?
- Qual exemplo mineiro permite estudar a relação entre caverna, paisagem cárstica e registro
- Qual consequência pode resultar da poluição da água numa área cárstica?
- Uma descrição situa atrativo em Sete Lagoas e o vincula a uma unidade estadual dedicada à 
- Sobre o valor de uma caverna, assinale a alternativa INCORRETA.
- Qual formulação preserva a relação entre estrutura do terreno e queda d’água?
- Um grupo decide ampliar uma trilha para chegar ao interior de uma caverna. Qual medida de 
- Qual alternativa apresenta uma associação geográfica correta entre dois atrativos?
- Sobre cavernas e rochas, assinale a alternativa INCORRETA.
- Qual combinação corresponde a atrativos protegidos ou administrados em contextos distintos
- Se o objetivo é reduzir erosão numa trilha de acesso a uma cachoeira, qual providência atu
- Qual reescrita mantém a precisão da frase: a chuva pode alterar a vazão de uma cachoeira?
- Qual alternativa descreve uma relação correta entre turismo e conservação em ambientes frá
- Uma frase diz que estalactites e estalagmites são obrigatórias em todas as grutas. Qual av
- Uma gestão identifica lixo, ruído e pisoteio em área de visitação. Qual resposta enfrenta 
- Uma alternativa reescreve que a água subterrânea pode afetar ecossistemas cavernícolas. Qu
- Ao comparar duas cachoeiras mineiras, qual conjunto de variáveis permite análise geográfic
- Qual regra de leitura impede que uma alternativa sobre um caso mineiro seja generalizada p
- Uma resposta afirma que a Gruta da Lapinha e a Gruta Rei do Mato são exemplos mineiros, ma
- Qual síntese integra adequadamente o estudo de cachoeiras, grutas e cavernas em Minas Gera
- A pesquisa sobre cavernas mineiras pode apoiar ações de conservação por qual motivo?
- A posição de uma cachoeira pode relacionar-se ao perfil do rio e às características do ter
- A Gruta da Lapinha está associada a Lagoa Santa e ao Parque Estadual do Sumidouro.
- A água subterrânea pode conectar processos da superfície aos ambientes cavernícolas.
- A Cachoeira das Andorinhas é um atrativo do parque municipal localizado em Ouro Preto.
- A proteção de uma caverna exige considerar somente o salão acessível ao visitante.
- A variação da vazão de uma cachoeira pode estar relacionada ao regime de chuvas na bacia.
- O Parque Nacional Cavernas do Peruaçu está associado a paisagens cársticas e a registros a
- Cavernas podem abrigar valores científicos e culturais além de seu interesse turístico.
- Todas as cavernas têm estalactites, estalagmites e um rio permanente.
- A categoria de unidade de conservação garante entrada irrestrita em qualquer setor da área
- A retirada de espeleotemas pode causar perda de patrimônio geológico formado lentamente.
- A Gruta Rei do Mato fica em Lagoa Santa e integra o Parque Estadual do Sumidouro.
- Toda caverna mineira se forma pela dissolução de rocha calcária.
- Na Cachoeira do Tabuleiro, a aparência da água basta para garantir que o acesso seja segur
- Uma descrição de queda d’água em formação rochosa semelhante a gruta prova que a atração é
```

## Pedido 20: Lagos e Represas (`minas-gerais.lagos-represas`, 2 tópicos de edital)

```
Matéria: minas-gerais.lagos-represas (Lagos e Represas)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Lagos naturais: formação e leitura da paisagem": 25
- "2. Lagos cársticos de Minas Gerais": 25
- "3. Identificação, dinâmica e conservação": 25
- "4. Reescrita e como a IDECAN cobra": 25

Já existem (não repita):
- Qual critério melhor distingue um lago natural de um reservatório?
- Qual lago mineiro está associado ao Parque Estadual do Sumidouro e a uma drenagem para gal
- Um corpo d’água surgiu após a construção de uma barragem em um rio. Como deve ser classifi
- Qual processo pode contribuir para a formação de depressões com lagos em terreno cárstico?
- Qual cuidado é adequado ao comparar a Lagoa Central com a Lagoa do Sumidouro?
- Por que um ranking dos maiores lagos naturais mineiros não deve ser criado sem fonte compa
- Após chuvas, o nível de um lago em depressão aumenta. Qual interpretação inicial é mais ca
- Qual conjunto de evidências melhor ajuda a identificar a origem de um corpo d’água pouco d
- O que caracteriza a contribuição hidrológica de um sumidouro em uma lagoa cárstica?
- Uma lagoa fica menor durante a estação seca. Qual investigação é mais adequada antes de at
- Qual associação regional é correta para o Carste de Lagoa Santa?
- Uma equipe encontra turbidez elevada após chuva em um lago natural próximo a solo exposto.
- O que se pode concluir com segurança ao saber que a Lagoa do Sumidouro possui um ponto de 
- Qual medida atua em uma escala apropriada para conservar um lago natural e reduzir aporte 
- Qual é a interpretação mais precisa da presença de lagos em dolinas no Carste de Lagoa San
- Uma área mostra redução da superfície de uma lagoa em dois registros feitos em anos difere
- Qual enunciado relaciona corretamente um lago natural e sua bacia de contribuição?
- Uma lagoa em terreno calcário apresenta nível variável e um sumidouro visível. Qual explic
- Um mapa regional mostra uma lagoa próxima a rochas carbonáticas, mas não há dados de fluxo
- Após chuva, uma lagoa recebe água de uma área com solo exposto e de um curso intermitente.
- Qual caracterização evita atribuir ao conjunto dos lagos mineiros uma característica docum
- Uma equipe quer avaliar se captações próximas afetam um lago sazonal. Qual plano fornece b
- Em uma área cárstica, uma lagoa sazonal ocupa uma ampla depressão de fundo plano. Qual ter
- Uma lagoa natural fica turva depois de chuva e também recebe efluentes de uma ocupação pró
- Um pesquisador chama uma lagoa de natural porque não observou barragem. Qual informação fo
- A Lagoa do Sumidouro integra uma paisagem com lagoa calcária, drenagem subterrânea e cavid
- Qual conclusão não pode ser inferida apenas da existência de uma lagoa em terreno cárstico
- Uma lagoa diminui após anos de ocupação da bacia, mas não há série histórica padronizada. 
- Qual reescrita mantém o sentido da afirmação: alguns poljés cársticos podem ser ocupados p
- A descrição de uma lagoa calcária com sumidouro aponta para um sistema em que superfície e
- Um reservatório artificial torna-se lago natural assim que passa a apresentar vegetação e 
- A Lagoa do Sumidouro é descrita oficialmente como lagoa calcária com um ponto de drenagem 
- O nome lagoa é suficiente para provar que um corpo d’água foi formado por processos natura
- Lagos associados a dolinas são descritos pelo SGB no contexto do Carste de Lagoa Santa.
- A redução do nível de um lago natural durante a estiagem comprova, sem outros dados, uma c
- Um lago natural pode sofrer contaminação ou alteração de margens sem deixar de ter origem 
- A Lagoa Central e a Lagoa do Sumidouro devem ter a mesma dinâmica subterrânea porque estão
- A impermeabilização da bacia pode modificar o escoamento superficial e o transporte de mat
- A descrição de lagoas sazonais em poljés significa que todos esses lagos secam completamen
- Uma variação simultânea de nível e turbidez após chuva, por si só, identifica a fonte de s
- A presença de calcário próximo a um lago torna razoável investigar uma possível relação cá
- Um reservatório artificial pode ter função ecológica sem deixar de ser artificial quanto à
- A existência de uma unidade de conservação em torno de um lago garante que a bacia inteira
- Trocar lagoa do Sumidouro por todas as lagoas mineiras em uma afirmação sobre drenagem sub
```

## Pedido 21: Lei Estadual nº 5.301, de 16 de outubro de 1969 (`direito.estatuto-militares-mg-lei-5301`, 2 tópicos de edital)

```
Matéria: direito.estatuto-militares-mg-lei-5301 (Lei Estadual nº 5.301, de 16 de outubro de 1969)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Prontidão e cumprimento da missão (art. 15)": 25
- "2. Uso privativo de títulos, postos, graduações e uniformes (art. 25)": 25
- "3. Cerimônias, convocação e restrição do uniforme": 25
- "4. Reescrita e cobrança da IDECAN": 25

Já existem (não repita):
- À luz do art. 15, assinale a descrição compatível com o dever de prontidão.
- A respeito da origem da missão prevista no art. 15, assinale a alternativa correta.
- Para as atividades indicadas no § 1º do art. 15, qual carga horária semanal consta do text
- Segundo o § 2º do art. 15, a carga horária semanal dos discentes dos cursos das IMEs é reg
- Qual alternativa reproduz o período de publicação das escalas ordinárias previsto no § 3º?
- Ao fim de qual período o § 5º determina que se apure o cumprimento da carga horária semana
- Um candidato sustenta que a escala ordinária pode ser publicada no próprio dia em que come
- O § 4º garante ao militar acesso, por sistema informatizado específico da instituição em q
- Sobre o prazo indicado no § 4º para disponibilização do sistema informatizado, assinale a 
- Um militar afirma que as escalas ordinárias devem ser inseridas no sistema apenas para arq
- Quanto ao caput do art. 25, qual grupo integra o conjunto de usuários privativos de título
- O art. 25, § 1º, limita o uso do uniforme por militares da reserva e reformados a quais oc
- Uma praça da reserva é convocada para o serviço ativo. Conforme o § 1º do art. 25, qual re
- A proibição de uso de uniformes prevista no art. 25, § 2º, pode atingir:
- Em relação à duração da proibição prevista no § 2º do art. 25, assinale a alternativa corr
- Qual circunstância é indicada no art. 25, § 2º, como fundamento para proibir o uso do unif
- Ao reescrever o art. 15, qual frase mantém o alcance da regra sobre tempo, lugar e origem 
- Um candidato mistura a apuração de carga horária com a publicação de escala. Qual associaç
- A instituição publica escala ordinária em ciclo de sete dias, mas a divulga com seis dias 
- Um item afirma que 160 horas são a jornada semanal e que o cumprimento será revisto a cada
- Qual reescrita do art. 25 preserva o alcance do caput sem estender o uso privativo a terce
- Um reformado usa uniforme em solenidade social; depois, passa a usá-lo em atividade comerc
- Em uma cerimônia, um reservista convocado para o serviço ativo pergunta se pode usar unifo
- Um comandante de unidade decide, sem participação do Comandante-Geral, proibir definitivam
- Um candidato afirma que o militar da reserva e o reformado podem usar uniforme em qualquer
- Uma escala ordinária foi inserida no sistema institucional e publicada com antecedência mí
- Qual interpretação respeita a ressalva do § 1º do art. 15 em relação ao dever previsto no 
- O edital indica o Capítulo II e os arts. 15 e 25 da Lei nº 5.301/1969. No texto consolidad
- Uma militar verifica no sistema sua escala, mas não consegue consultar o banco de horas. Q
- Considerando em conjunto as regras do art. 25 sobre convocação e restrição do uniforme, as
- A proibição temporária do uniforme por decisão do Comandante-Geral é possível, segundo o a
- O art. 15 restringe o dever de prontidão ao horário regular de expediente e à sede da Unid
- O limite de 160 horas previsto no § 5º do art. 15 é mensal, e a apuração do cumprimento da
- O caput do art. 25 torna títulos, postos, graduações e uniformes privativos dos militares 
- A escala ordinária pode ser publicada em ciclos de sete dias, mas o art. 15 dispensa prazo
- A carga dos discentes dos cursos de formação, habilitação, especialização e atualização da
- O § 4º garante acesso informatizado à escala de trabalho e ao respectivo banco de horas do
- Militares reformados podem usar uniforme em cerimônias sociais, militares e cívicas, confo
- O § 1º do art. 15 determina ciclos de sete dias e antecedência máxima de sete dias para a 
- Quando convocado para serviço ativo, o militar da reserva usa uniforme diferente do adotad
- A escala ordinária deve ser publicada em ciclos de sete dias, com antecedência mínima de c
- A proibição do uniforme por atos indignos alcança automaticamente todos os militares, da a
- O § 3º do art. 15 determina que a escala ordinária seja publicada com antecedência mínima 
- O limite mensal do § 5º é de 160 horas, enquanto a carga semanal de 40 horas do § 1º se re
- A lei permite que o Comandante-Geral proíba temporária ou definitivamente o uniforme de re
```

## Pedido 22: Estatuto e Organização Básica da PMDF (Leis 7.289/1984 e 6.450/1977) (`legislacao.estatuto-pm-df-lei-7289`, 2 tópicos de edital)

```
Matéria: legislacao.estatuto-pm-df-lei-7289 (Estatuto e Organização Básica da PMDF (Leis 7.289/1984 e 6.450/1977))
Capítulos e quantas questões quero em cada um (total 100):
- "1. A PMDF, organização básica e ingresso": 20
- "2. Hierarquia, deveres e responsabilização": 20
- "3. Direitos, recursos, promoções, férias e licenças": 20
- "4. Prerrogativas e situações especiais": 20
- "5. Exclusão do serviço ativo, inatividade e tempo de serviço": 20

Já existem (não repita):
- Segundo a Lei 6.450/1977, a Polícia Militar do Distrito Federal é:
- Pela Lei 6.450/1977, a PMDF estrutura-se em Comando-Geral, Órgãos de Apoio e Órgãos de Exe
- Na organização básica da PMDF, a Comissão de Promoção de Praças é presidida:
- O Subcomandante-Geral e o Chefe do Estado-Maior da PMDF são coronéis do QOPM indicados pel
- Pela Lei 7.289/1984, os limites de idade para matrícula nos cursos de formação da PMDF, no
- A altura mínima exigida para matrícula nos cursos de formação da PMDF é de um metro e sess
- A matrícula no Curso de Formação de Oficiais e o ingresso no Quadro de Oficiais Policiais 
- No Estatuto da PMDF, o posto é conferido por ato do Governador do Distrito Federal e confi
- Quanto à precedência entre praças na PMDF, o Estatuto estabelece que:
- Entre os deveres policiais-militares previstos no art. 32 da Lei 7.289/1984, está:
- No concurso de crime militar e transgressão disciplinar, o Estatuto da PMDF determina que 
- Na PMDF, os recursos interpostos nos processos oriundos de Conselho de Disciplina são julg
- São competentes para determinar o imediato afastamento do cargo do policial militar da PMD
- A pena disciplinar de detenção ou prisão aplicada ao policial militar da PMDF não pode ult
- Para o provimento do cargo de Comandante de Organização Policial-Militar Independente priv
- No Estatuto da PMDF, a praça adquire estabilidade com dez ou mais anos de tempo de efetivo
- O direito de recorrer na esfera administrativa, no Estatuto da PMDF, prescreve em:
- Pelo Estatuto da PMDF, o afastamento total do serviço por motivo de instalação é de até:
- A policial militar da PMDF gestante tem direito a quatro meses de afastamento total do ser
- São critérios de promoção previstos no art. 60 da Lei 7.289/1984:
- A licença para tratar de interesse particular, na PMDF, é concedida ao policial militar co
- Sobre a licença especial no Estatuto da PMDF, é correto afirmar:
- O policial militar da PMDF com cinco anos ou mais de efetivo serviço que se candidata a ca
- Pelo Estatuto da PMDF, o policial militar preso em flagrante por autoridade policial deve 
- Na PMDF, o policial militar agregado fica adido, para efeito de alterações e remuneração, 
- Será agregado, pelo Estatuto da PMDF, o policial militar que:
- Pela Lei 7.289/1984, considera-se ausente, desaparecido e extraviado o policial militar ap
- O policial militar em situação de excedente é considerado em efetivo serviço para todos os
- Na redação atual do Estatuto da PMDF, a transferência para a reserva remunerada a pedido s
- Pelo texto atualizado da Lei 7.289/1984, a idade-limite para a transferência de ofício par
- A transferência do policial militar da PMDF para a reserva remunerada pode ser suspensa na
- As idades-limite de permanência na reserva remunerada da PMDF, após as quais o policial mi
- Um Cabo da PMDF foi julgado incapaz definitivamente por ferimento recebido na manutenção d
- O policial militar da PMDF reformado por incapacidade definitiva que for julgado apto por 
- O oficial da PMDF que passar a exercer cargo ou emprego público permanente estranho à carr
- No Estatuto da PMDF, o oficial perderá o posto e a patente se for declarado indigno do ofi
- Pela Lei 7.289/1984, é computado como tempo de efetivo serviço:
- No Estatuto da PMDF, o casamento de policiais militares com estrangeiros somente poderá se
- No Estatuto da PMDF, a deserção do Aspirante-a-Oficial ou da praça com estabilidade assegu
- A reinclusão na PMDF é, em regra, vedada, sendo admitida apenas para cumprir decisão judic
```

## Pedido 23: Estatuto dos Policiais Militares do Rio de Janeiro (Lei 443/1981) (`legislacao.estatuto-pm-rio-de-janeiro-lei-443`, 2 tópicos de edital)

```
Matéria: legislacao.estatuto-pm-rio-de-janeiro-lei-443 (Estatuto dos Policiais Militares do Rio de Janeiro (Lei 443/1981))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Generalidades, ingresso, hierarquia e cargo": 20
- "2. Deveres, ética e responsabilização": 20
- "3. Direitos, recursos, remuneração, promoções e licenças": 20
- "4. Prerrogativas e situações especiais": 20
- "5. Inatividade, exclusão, reabilitação e tempo de serviço": 20

Já existem (não repita):
- Segundo a Lei 443/1981, com a redação da Lei 9.535/2021, estão na ativa:
- Na PMERJ, os militares temporários, incorporados para prestação de serviço militar voluntá
- Desde a Lei 7.858/2018, para ingresso no Curso de Formação de Oficiais da PMERJ, além dos 
- Sobre as classes do Soldado PM na Lei 443/1981, é correto afirmar:
- No Estatuto da PMERJ, posto é o grau hierárquico do oficial, conferido por ato do Governad
- Dois soldados foram nomeados no mesmo ato, como resultado do mesmo concurso. Pela Lei 443/
- Segundo a Lei 443/1981, cargo policial-militar é um conjunto de atribuições, deveres e res
- O compromisso do policial militar, no Estatuto da PMERJ, tem caráter solene e é sempre pre
- Na Lei 443/1981, os subtenentes e sargentos auxiliam e complementam as atividades dos ofic
- Segundo o Estatuto da PMERJ, NÃO está entre as autoridades competentes para determinar o i
- No concurso de crime militar e de contravenção ou de transgressão disciplinar da mesma nat
- As penas disciplinares de detenção ou prisão previstas para os policiais militares do Rio 
- Sobre os conselhos previstos na Lei 443/1981, assinale a correta.
- O Estatuto da PMERJ proíbe quaisquer manifestações sobre atos de superiores e as de caráte
- Entre os direitos incluídos no art. 48 da Lei 443/1981 em 1991, está:
- Pela Lei 443/1981, a praça adquire estabilidade com dez ou mais anos de tempo de efetivo s
- O policial militar que se julgar prejudicado por ato decorrente de composição de Quadro de
- Segundo o art. 50 da Lei 443/1981, são alistáveis como eleitores os policiais militares of
- São critérios de promoção na PMERJ, conforme o art. 58 da Lei 443/1981, com a redação da L
- Não será promovido o policial militar que estiver submetido a Conselho de Justificação, a 
- Pela Lei 443/1981, o afastamento total do serviço por motivo de trânsito é de até:
- No Estatuto da PMERJ, a duração das férias é fixada pelo Poder Executivo, e a regulamentaç
- Com a redação da Lei 7.714/2017, a licença para tratar de interesse particular na PMERJ é 
- À policial militar gestante, a Lei 443/1981 garante licença para tratamento de saúde própr
- A interrupção da licença especial ou da licença para tratar de interesse particular, no Es
- É prerrogativa do policial militar do Rio de Janeiro cumprir pena de prisão, reclusão ou d
- Pelo Estatuto da PMERJ, o policial militar só pode ser preso por autoridade policial:
- Na Lei 443/1981, a agregação, a reversão e a exclusão do serviço ativo são feitas, respect
- Será agregado, segundo o art. 80 da Lei 443/1981, o policial militar que:
- O policial militar em situação de excedente é considerado em efetivo serviço para todos os
- Segundo a Lei 443/1981, considera-se desaparecido e, depois, extraviado o policial militar
- Pela redação dada pela Lei 2.206/1993, a transferência do policial militar do RJ para a re
- Segundo o art. 95 da Lei 443/1981, é facultado ao policial militar, mesmo fora do Quadro d
- No Estatuto da PMERJ, a transferência de ofício para a reserva por idade e a reforma por i
- Na indicação dos oficiais para a quota compulsória da PMERJ, são apreciados primeiro os re
- Um Subtenente da PMERJ foi julgado incapaz definitivamente em razão de ferimento recebido 
- O policial militar reformado por incapacidade definitiva que for julgado apto por Junta Su
- Sobre a demissão na PMERJ, assinale a correta.
- A praça sem estabilidade assegurada que for oficialmente declarada desertora será automati
- Segundo a Lei 443/1981, a concessão de reabilitação ao policial militar implica:
- Quando, por necessidade do serviço, o policial militar mudar a sede de seu domicílio, a Le
```

## Pedido 24: Campo Geral, de João Guimarães Rosa (`literatura.campo-geral`, 2 tópicos de edital)

```
Matéria: literatura.campo-geral (Campo Geral, de João Guimarães Rosa)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Campo Geral: composição, enredo e espaço do Mutum": 20
- "2. Miguilim e Dito: afetos, escolhas e conflitos familiares": 20
- "3. Perdas e violência: os acontecimentos que amadurecem Miguilim": 20
- "4. Foco narrativo, linguagem rosiana e percepção infantil": 20
- "5. Os óculos, a despedida e o sentido da passagem": 20

Já existem (não repita):
- Uma leitora encontra Campo Geral publicado isoladamente e deseja saber sua primeira integr
- Uma criança vive entre morros, roças, animais e parentes, num lugar cuja beleza é discutid
- Um estudante afirma que Dito aconselha o protagonista apesar de ser mais novo. Qual identi
- Tio Terêz pede que Miguilim leve uma mensagem à mãe, em meio a suspeitas e atritos familia
- A morte de uma criança produz profundo luto no protagonista, que perde seu conselheiro afe
- Em análise do foco narrativo de Campo Geral, qual combinação de voz e perspectiva correspo
- No encerramento da narrativa, um visitante identifica a vista curta de Miguilim e lhe empr
- Após experimentar os óculos do doutor, Miguilim se prepara para sair do Mutum. Qual perspe
- Ao descrever o Mutum, um comentário afirma que Nhanina o sente como lugar opressivo, ao pa
- Miguilim hesita diante de mensagem de Tio Terêz à mãe sem conhecer integralmente o conteúd
- Um leitor compara a sabedoria de Dito com as incertezas do irmão protagonista. Qual contra
- Após a morte de Dito, o papagaio Papaco-o-Paco repete o nome do menino. Qual papel express
- Uma análise sustenta que a linguagem de Campo Geral se aproxima da infância por diminutivo
- Uma interpretação diz que só quem narra em primeira pessoa pode ter experiência subjetiva 
- Na cena dos óculos, Miguilim passa a perceber com nitidez objetos e pessoas. Que leitura a
- Ao sair do Mutum, Miguilim recebe promessa de estudo na cidade, mas se despede com emoção 
- Num ensaio sobre Campo Geral, afirma-se que o leitor associa indícios de tensões conjugais
- A cena do bilhete costuma ser lida como passagem de Miguilim a uma consciência ética mais 
- Uma leitura sustenta que Campo Geral idealiza uniformemente o sertão como espaço de harmon
- Uma interpretação aproxima a perda de Pingo-de-Ouro da morte de Dito, mas um colega objeta
- O final faz Miguilim rever a beleza do Mutum por meio dos óculos do doutor. Qual interpret
- Um leitor associa Dito à função de 'mestre' de Miguilim, mas seria incorreto convertê-lo e
- Um comentário afirma que a narração 'fala como a criança' e, por isso, cada diminutivo ser
- No desfecho, Miguilim aceita a possibilidade de ir à cidade estudar, e o narrador encerra 
- Em debate sobre a estrutura, uma pessoa afirma que a morte de Dito, a de Bernardo e a part
- Campo Geral acompanha o menino Miguilim em sua vida familiar no Mutum, espaço sertanejo no
- Dito, irmão mais velho de Miguilim, assume formalmente o trabalho do pai e conduz a famíli
- No episódio do bilhete, Miguilim se vê diante de dúvida moral e acaba devolvendo a mensage
- Dito sobrevive ao ferimento no pé e é o personagem que deixa o Mutum com o doutor José Lou
- O narrador de Campo Geral está em terceira pessoa, mas o leitor acompanha muitos acontecim
- Os óculos usados no final são apenas símbolo abstrato: Miguilim não possui dificuldade vis
- Ao final, a possibilidade de estudar na cidade abre para Miguilim novo horizonte, mas a de
- A tristeza de Nhanina diante do Mutum demonstra que a paisagem não possui beleza possível 
- A decisão de Miguilim sobre o bilhete pode ser lida como amadurecimento ético precisamente
- O luto por Dito constitui acontecimento isolado do mundo de Miguilim, sem relação com o va
- A presença de apelidos, diminutivos e oralidade estilizada em Campo Geral integra uma elab
- Como a narrativa é conduzida em terceira pessoa, todas as tensões entre Nhanina, Bernardo 
- A partida de Pingo-de-Ouro e a morte de Dito são episódios diferentes, mas ambos contribue
- A descoberta da miopia ao final resolve os conflitos morais da família e comprova que a vi
- A combinação de narração em terceira pessoa com focalização próxima de Miguilim possibilit
- A morte de Bernardo é causada pelo mesmo ferimento de pé que atinge Dito, e o episódio est
- A beleza do Mutum percebida com os óculos e a tristeza da mãe podem coexistir, pois a nove
- A linguagem oral e os diminutivos de Campo Geral demonstram que o texto dispensa elaboraçã
- O retorno da questão da beleza do Mutum no encerramento vincula início e fim da novela, e 
- Como Miguilim recebe convite para estudar fora do Mutum, o narrador descreve sua formação 
```

## Pedido 25: Vidas Secas, de Graciliano Ramos (`literatura.vidas-secas`, 2 tópicos de edital)

```
Matéria: literatura.vidas-secas (Vidas Secas, de Graciliano Ramos)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Estrutura, deslocamento e personagens": 34
- "2. Linguagem, focalização e crítica social": 33
- "Como o tema costuma ser cobrado": 33

Já existem (não repita):
- O vínculo entre «Mudança» e «Fuga» ajuda a construir qual aspecto da estrutura?
- A seca em Vidas Secas deve ser entendida como fenômeno ambiental cujos efeitos se articula
- Qual leitura descreve melhor a situação de Fabiano em relação ao trabalho e às contas?
- A cama de couro desejada por sinhá Vitória pode representar um projeto de estabilidade e c
- A focalização de Baleia amplia a empatia e permite considerar a vulnerabilidade animal na 
- Capítulos relativamente autônomos podem compor um romance unitário por meio de recorrência
- A prosa econômica do romance elimina a intensidade emocional das personagens?
- O fato de os meninos não terem nomes próprios pode ser relacionado à vulnerabilidade e à v
- A experiência de Fabiano na cadeia ajuda a mostrar a assimetria de poder e a violência sof
- Certo/Errado: Os dois filhos de Fabiano e sinhá Vitória são nomeados no romance por nomes 
- Certo/Errado: A seca e a pobreza aparecem junto de relações de trabalho e de poder que afe
- Certo/Errado: A dificuldade de Fabiano para se expressar prova que ele não percebe injusti
- Certo/Errado: A focalização de Baleia oferece interioridade narrativa ao animal sem apagar
- Certo/Errado: A repetição de imagens e situações ajuda a ligar os capítulos relativamente 
- Certo/Errado: A estrutura em capítulos relativamente autônomos significa que não há recorr
- Certo/Errado: sinhá Vitória possui desejos e iniciativa, e não deve ser reduzida a figura 
- Certo/Errado: A prosa contida pode comunicar emoção sem explicações psicológicas extensas.
- Certo/Errado: O romance atribui toda a exploração da família ao clima, sem apresentar rela
- Certo/Errado: O encerramento deixa em aberto se a família romperá definitivamente o ciclo 
- Certo/Errado: A narração em terceira pessoa pode aproximar-se da consciência de Fabiano ou
- Certo/Errado: A esperança de uma vida melhor no final elimina a estrutura circular sugerid
- Certo/Errado: Relacionar capítulo, personagem e escolha formal produz análise mais sustent
- Certo/Errado: A crítica social do romance aparece em relações concretas de trabalho, conta
- Qual efeito decorre da alternância entre capítulos centrados em personagens diferentes?
- A imagem da cama desejada por sinhá Vitória caracteriza principalmente
- Em Vidas Secas, a dificuldade de Fabiano com a linguagem permite concluir que
- A repetição de imagens de deslocamento reforça a ideia de que a família
- A inclusão de Baleia entre as personagens permite
- A contenção da prosa em Vidas Secas é compatível com a produção de efeitos emocionais.
- A focalização próxima de uma personagem torna impossível a presença de narrador em terceir
- A autonomia relativa dos capítulos é compatível com a unidade do romance construída por ec
- A presença de sonhos e desejos das personagens impede que o romance trate da precariedade 
- A violência vivida por Fabiano pode ser lida em conjunto com relações de desigualdade e po
- A recorrência da fuga no início e no encerramento garante que a família alcançará uma vida
- A estiagem pode ser lida como elemento ambiental e também como fator que intensifica a vul
- A narração transforma toda percepção das personagens em comentário objetivo e distante.
- A precariedade pode aparecer por meio de detalhes cotidianos, sem ser explicada apenas em 
- A relativa autonomia de cada capítulo exclui qualquer paralelo entre o primeiro e o último
- A caracterização de sinhá Vitória inclui iniciativa e formulação de projetos para a vida f
- O capítulo dedicado a Baleia deixa de participar dos temas de vulnerabilidade presentes no
- A análise da obra fica mais completa quando relaciona escolhas narrativas aos temas, em ve
- A expressão limitada de Fabiano impede que o romance represente qualquer percepção de expl
- O desfecho combina expectativa de futuro com a possibilidade de permanência do ciclo de de
- A alternância de perspectivas em Vidas Secas contribui para mostrar
```

## Pedido 26: Constituição do Estado de Minas Gerais: militares e segurança pública (`direito.constituicao-estado-minas-gerais-militares`, 1 tópicos de edital)

```
Matéria: direito.constituicao-estado-minas-gerais-militares (Constituição do Estado de Minas Gerais: militares e segurança pública)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Art. 13: princípios da administração pública": 25
- "2. Art. 137: subordinação das corporações estaduais": 25
- "3. Art. 142: organização, comando e atribuições": 25
- "4. Reescrita normativa e como a IDECAN cobra": 25

Já existem (não repita):
- À luz do texto vigente do art. 13, assinale a lista completa de princípios que rege a admi
- Uma entidade descentralizada estadual deve observar os princípios do art. 13 da Constituiç
- Segundo o § 1º do art. 13, como se apuram moralidade e razoabilidade dos atos públicos par
- Ao motivar um ato administrativo, qual conjunto deve ser explicitado pelo agente público, 
- De acordo com o art. 137 vigente, a Polícia Militar e o Corpo de Bombeiros Militar subordi
- Qual alternativa reúne as instituições expressamente enumeradas no art. 137 atualizado?
- Uma apostila anterior a 2022 apresenta o art. 137 sem mencionar a Polícia Penal. Qual conc
- Um item afirma que o art. 137 subordina Polícia Civil, PM, Polícia Penal e Bombeiros à Ass
- Qual afirmação distingue corretamente a subordinação institucional do art. 137 da organiza
- A redação anterior do art. 137 mencionava três instituições. Qual efeito da alteração de 2
- À luz do caput do art. 142, qual alternativa descreve a organização constitucional da Polí
- A IDECAN apresenta a frase: o Corpo de Bombeiros coordena e executa ações de defesa civil,
- Sobre as competências do art. 142, assinale a alternativa correta quanto à função de políc
- Uma questão sobre o § 1º do art. 142 deve ser respondida com qual enunciado?
- O caput diz que as corporações são comandadas preferencialmente por oficial da ativa do úl
- Qual hipótese satisfaz a exceção prevista no art. 142, § 2º, para comando por oficial da r
- Em uma operação de patrulhamento rodoviário e preservação da ordem pública, qual atribuiçã
- Durante uma catástrofe, o enunciado atribui ao Bombeiro coordenação e execução da defesa c
- Uma autoridade estadual propõe que a PM exerça o poder de polícia de órgãos públicos nas á
- Ao analisar o comando das corporações, um candidato encontra um oficial da reserva que ocu
- Qual reescrita preserva simultaneamente a regra de comando do caput e a exceção do § 2º do
- Uma questão afirma que a redação atual do art. 13 ainda enumera apenas seis princípios, em
- Um ato de autoridade estadual é questionado por falta de motivação. Qual reescrita é compa
- Uma alternativa combina três proposições: Bombeiros subordinam-se ao Governador; são força
- O art. 142 usa a expressão qualquer tipo de catástrofe ao tratar de normas de segurança de
- Uma banca cobra conjuntamente o art. 137 e o caput do art. 142. Qual afirmação evita confu
- Uma perícia de incêndio é atribuída ao Corpo de Bombeiros, enquanto uma atividade de políc
- Uma reescrita afirma que o Governador pode nomear oficial da reserva para comandar a PM ou
- Uma comissão analisa um ato e pretende invalidá-lo por violação à razoabilidade. Qual proc
- No art. 142, o candidato encontra a expressão órgãos permanentes e, em seguida, a preferên
- O princípio da sustentabilidade foi inserido no caput do art. 13 pela Emenda à Constituiçã
- O § 2º do art. 13 exige fundamento legal, fundamento fático e finalidade somente dos atos 
- O art. 137 vigente subordina a Polícia Civil, a PM, a Polícia Penal e o Corpo de Bombeiros
- O art. 142 qualifica a Polícia Militar e o Corpo de Bombeiros como órgãos permanentes orga
- O inciso II do art. 142 atribui à Polícia Militar a coordenação e a execução de ações de d
- O art. 142, § 1º, define a Polícia Militar e o Corpo de Bombeiros como forças auxiliares e
- O art. 142 determina que o comando das corporações seja sempre exercido por oficial da ati
- A Polícia Civil integra o rol de instituições subordinadas ao Governador no art. 137, mas 
- A função de polícia judiciária militar é atribuída pelo art. 142, III, somente ao Corpo de
- Pelo art. 13, § 1º, moralidade e razoabilidade são apuradas, para controle e invalidação, 
- A Polícia Militar exerce polícia ostensiva de prevenção criminal, segurança e trânsito, en
- O art. 13 submete somente a administração direta do Poder Executivo aos princípios enumera
- Se o Governador fundamentar a decisão, pode designar oficial da reserva que tenha ocupado 
- A atualização de 2022 retirou o Corpo de Bombeiros do art. 137 e passou a subordiná-lo exc
- O inciso I do art. 142 inclui, entre as atividades da PM, a garantia do exercício do poder
```

## Pedido 27: Grutas e Cavernas (`minas-gerais.grutas-cavernas`, 1 tópicos de edital)

```
Matéria: minas-gerais.grutas-cavernas (Grutas e Cavernas)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Cavernas, grutas e processos de formação": 25
- "2. Gruta da Lapinha e o carste de Lagoa Santa": 25
- "3. Gruta Rei do Mato e outros conjuntos cavernícolas": 25
- "4. Reescrita e como a IDECAN cobra": 25

Já existem (não repita):
- Qual processo pode ampliar fraturas e formar galerias em terrenos cársticos?
- Em qual município mineiro fica a Gruta da Lapinha?
- A Gruta Rei do Mato é protegida como qual unidade?
- Qual área mineira combina cavernas, relevo cárstico e registros arqueológicos?
- Que nome recebe uma formação mineral que cresce no teto de uma caverna?
- Qual afirmação sobre a ocorrência de espeleotemas é correta?
- Qual associação entre caverna e município está correta?
- Qual conclusão pode ser feita sobre áreas cársticas e qualidade da água?
- A presença da Gruta da Lapinha no contexto do Parque Estadual do Sumidouro permite relacio
- Qual é uma função associada ao CECAV no tema de cavernas?
- Assinale a alternativa INCORRETA sobre cavidades subterrâneas.
- Uma unidade de conservação permite visitas com regras específicas. Qual interpretação é ma
- Qual conjunto de informações diferencia a Gruta Rei do Mato de outros exemplos?
- Qual leitura é mais adequada para a relação entre Peter Lund e a região de Lagoa Santa?
- Qual comparação entre Lapinha e Rei do Mato está correta?
- Qual par identifica corretamente dois sítios cavernícolas mineiros e seus contextos?
- Um visitante propõe tocar e remover pequenas formações para observá-las melhor. Qual respo
- Uma banca relaciona arte rupestre às cavernas do Peruaçu. Qual conclusão é adequada?
- Qual informação deve ser conferida em canal oficial antes de uma visita à Gruta da Lapinha
- Que estratégia evita confundir exemplos mineiros em uma questão de localização?
- Qual fonte institucional é apropriada para consultar a missão de pesquisa e conservação de
- Qual inferência é autorizada ao ler que o Peruaçu reúne cavernas e sítios arqueológicos?
- Uma frase informa que água subterrânea pode dissolver rocha solúvel. Qual alternativa NÃO 
- Qual informação é mais adequada para compor uma resposta sobre valor patrimonial caverníco
- Qual cuidado é necessário ao comparar dimensões de cavernas descritas em fontes diferentes
- Qual associação completa corretamente: Lapinha–Lagoa Santa; Rei do Mato–___?
- Qual conclusão respeita os limites da afirmação “o Peruaçu possui registros arqueológicos”
- Uma caverna possui trechos secos e condutos com água. Qual conclusão é cientificamente pru
- Uma questão descreve dissolução de rocha e formação de galerias. Que cuidado evita uma con
- Sobre a visitação em grutas, assinale a alternativa INCORRETA.
- A Gruta da Lapinha está localizada em Lagoa Santa.
- A Gruta Rei do Mato situa-se em Sete Lagoas.
- A espeleologia é o estudo científico das cavernas e de fenômenos a elas associados.
- O Parque Nacional Cavernas do Peruaçu fica no norte de Minas Gerais.
- A proteção de uma caverna determina automaticamente o fechamento permanente do local.
- Alterações na área de recarga podem repercutir na circulação de água em sistemas cársticos
- O Peruaçu reúne cavernas e sítios arqueológicos, além de formas de relevo cárstico.
- O CECAV atua em pesquisa e conservação de ambientes cavernícolas e de espécies associadas.
- Todas as cavernas se desenvolvem em calcário e apresentam espeleotemas.
- O nome turístico de uma gruta permite determinar sozinho a composição da rocha e a idade d
- Regras de visitação podem ajudar a conciliar acesso público, segurança e conservação.
- A presença de registros arqueológicos no Peruaçu permite afirmar que toda caverna mineira 
- A Gruta Rei do Mato e a Gruta da Lapinha ficam no mesmo município.
- A descrição de uma caverna com água permite concluir que todos os seus condutos permanecem
- A referência a Peter Lund na região de Lagoa Santa prova que ele criou a Gruta da Lapinha.
```

## Pedido 28: Ecoturismo (`minas-gerais.ecoturismo`, 1 tópicos de edital)

```
Matéria: minas-gerais.ecoturismo (Ecoturismo)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos e potencialidades do ecoturismo em Minas Gerais": 25
- "2. Cachoeiras: oportunidades, riscos e visitação responsável": 25
- "3. Grutas e cavernas: patrimônio, experiência e fragilidade": 25
- "4. Planejamento integrado e cobrança em prova": 25

Já existem (não repita):
- Uma associação local planeja visitas guiadas a uma área natural. Qual proposta se aproxima
- Uma pequena comunidade oferece condução, alimentação e hospedagem a visitantes de uma cach
- Qual medida de rotina reduz uma fonte comum de degradação em trilhas de acesso a cachoeira
- Em relação às formações minerais de uma caverna turística, assinale a conduta mais respons
- O que deve orientar a visitação pública em uma unidade de conservação?
- Uma trilha até uma queda-d’água apresenta atalho informal com solo exposto. Qual interpret
- Uma cavidade recebe grupos numerosos em sequência, e a fauna evita um setor antes utilizad
- Sobre a geração de renda pelo ecoturismo, assinale a afirmativa correta.
- Em uma unidade de conservação, um operador propõe abrir um novo setor de caverna por haver
- Um roteiro apresenta formações rochosas e fauna subterrânea a visitantes. Qual conduta red
- Qual alternativa expressa corretamente uma relação entre visitação, informação e conservaç
- A equipe observa resíduos e vegetação pisoteada junto à margem de uma cachoeira. Qual diag
- Sobre o planejamento da capacidade de visitação, assinale a afirmação correta.
- Uma equipe cogita iluminar continuamente um setor cavernícola para melhorar fotografias. Q
- Uma trilha de cachoeira está erodida após o uso, e sedimentos alcançam um trecho de drenag
- Um roteiro cavernícola inclui um setor com registros arqueológicos e outro com formações f
- Uma unidade avalia abrir trilha para uma caverna. A análise identifica possível renda loca
- Em uma trilha de acesso a cachoeira, a gestão mantém o caminho principal, mas permite atal
- Qual conclusão decorre do exemplo de visitação divulgado pelo ICMBio para o Parque Naciona
- A visitação de uma gruta aumenta após divulgação, e o gestor observa lixo e ruído acima do
- Uma comunidade pretende promover banho em cachoeira e visitas a caverna no mesmo pacote. Q
- Uma análise constata que a renda do turismo se concentra em empresas de fora, embora parte
- Uma empresa anuncia acesso livre a uma cavidade em unidade de conservação, embora o setor 
- Um visitante afirma que só a coleta de resíduos importa para conservar uma caverna. Qual f
- A administração de uma unidade observa que uma trilha de cachoeira mantém erosão mesmo apó
- Uma proposta de visitação em caverna oferece renda a condutores locais, mas prevê contato 
- Um roteiro em unidade de conservação tem boa infraestrutura, mas não considera o plano de 
- Qual afirmativa sintetiza corretamente o desafio de integrar ecoturismo e conservação em á
- Em relação à visitação ecoturística em cachoeiras e cavernas, julgue: a instalação de infr
- Em áreas de cachoeira, todo aumento da visitação necessariamente melhora a qualidade da ág
- A interpretação ambiental torna desnecessários limites de acesso e monitoramento, pois vis
- O simples fato de uma cavidade ser conhecida por visitantes permite presumir que todo o se
- Resíduos deixados em uma área de cachoeira podem afetar a paisagem e os ambientes terrestr
- A participação de moradores no planejamento pode auxiliar a compatibilizar visitação, cons
- Quando há condutor credenciado, a visita a uma caverna pode ignorar restrições de percurso
- A definição de regras de visitação deve considerar características do atrativo e pode ser 
- O ecoturismo sempre distribui a renda de forma equilibrada entre todos os moradores, mesmo
- O uso de atalhos em trilhas pode espalhar o pisoteio e ampliar superfícies vulneráveis à e
- A iluminação artificial contínua em cavidades não exige avaliação porque a ausência de luz
- Se uma chuva forte cria risco de enxurrada em acesso a cachoeira, a gestão responsável pod
- No Parque Nacional Cavernas do Peruaçu, a visitação divulgada pelo ICMBio prevê condução e
- A oferta de serviços por moradores próximos a atrativos naturais garante distribuição igua
- O planejamento de visitação pode precisar distinguir setores de uma mesma cavidade, pois o
```

## Pedido 29: Conservação e Manejo (`minas-gerais.conservacao-manejo`, 1 tópicos de edital)

```
Matéria: minas-gerais.conservacao-manejo (Conservação e Manejo)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Plano de manejo, zoneamento e governança": 25
- "2. Cachoeiras: uso público, água e recuperação": 25
- "3. Grutas e cavernas: fragilidade subterrânea e controle de acesso": 25
- "4. Reescrita e como o tema é cobrado": 25

Já existem (não repita):
- Em uma unidade de conservação, qual instrumento orienta o zoneamento e as normas de uso e 
- Sobre as regras de visitação em unidades de conservação, assinale a afirmativa correta.
- A trilha de acesso a uma cachoeira apresenta erosão e atalhos abertos por visitantes. Qual
- Qual prática é mais diretamente compatível com a proteção de uma área de cachoeira?
- Ao orientar visitantes em uma gruta, qual conduta reduz o risco de dano direto às formaçõe
- A administração de um atrativo subterrâneo observa danos em um setor sensível. Qual medida
- O que melhor caracteriza o manejo adaptativo da visitação?
- Uma comunidade vizinha relata uso tradicional de caminho situado próximo a uma área proteg
- Uma intervenção de drenagem no entorno é seguida por água turva em uma cavidade. Qual anál
- Um gestor pretende fixar a mesma quantidade máxima de visitantes para todas as cachoeiras 
- Ao planejar estruturas de apoio próximas a uma cachoeira, qual critério deve orientar a de
- O Parque Nacional Cavernas do Peruaçu informa procedimentos de agendamento para atrativos.
- Em uma UC, uma área de maior fragilidade foi identificada no zoneamento. Qual consequência
- Uma equipe nota perda de vegetação e alargamento de trilha próximo a uma queda d’água. Qua
- Um atrativo recebe mais visitantes, mas não há indicadores ambientais nem registros de seg
- Uma trilha de visitação em caverna foi proposta para reduzir o pisoteio, mas o projeto pre
- Após chuvas fortes, a equipe decide fechar preventivamente o acesso a uma cachoeira por ri
- Uma rota de caverna atravessa setor com formações frágeis e vestígios culturais. A procura
- Uma gestão pretende criar limite diário de visitantes para um poço de banho. Qual justific
- Uma comunidade propõe atuar como condutora em trilha de acesso a uma gruta. Qual encaminha
- O monitoramento mostra que uma rota desviada reduziu o pisoteio em um setor, mas aumentou 
- A equipe encontra lixo dentro de uma gruta e recebe relatos de descarte na trilha externa.
- Um setor de banho permanece seguro, mas a vegetação de sua margem vem sendo degradada. Qua
- Um pesquisador solicita entrada fora do percurso turístico para registrar fauna subterrâne
- O gestor de uma unidade considera substituir a rota de acesso a um atrativo por outra. Ant
- Em período de chuva, o monitoramento registra aumento do escoamento e de sedimentos em tri
- Um plano permite visitação em determinada zona, mas o gestor identifica risco temporário p
- Uma cavidade possui setores com sensibilidades diferentes. O gestor pretende permitir visi
- Uma administração avalia o efeito de uma nova regra de visitação. Qual conjunto de evidênc
- Durante a revisão do plano de uma UC, há conflito entre a demanda por acesso a uma gruta e
- Uma regra de visitação local é alterada após monitoramento apontar novos riscos. O procedi
- Toda unidade de conservação deve admitir o mesmo número máximo de visitantes por dia, inde
- A participação de comunidades vizinhas pode ser considerada no planejamento da unidade, se
- A instalação de placas em uma trilha torna dispensáveis a manutenção e o controle da circu
- O procedimento de agendamento divulgado para o Parque Nacional Cavernas do Peruaçu constit
- A restauração de margem degradada deve ser acompanhada para verificar se a vegetação e o s
- A proteção de uma caverna pode exigir atenção às atividades na superfície quando houver co
- A existência de uma regra de visitação no plano de manejo impede que o gestor adote restri
- A presença de estruturas como passarelas ou iluminação garante, sem análise adicional, a c
- A gestão pode combinar orientação ao visitante, rota definida, manutenção e controle de ac
- O fechamento preventivo de um setor pode ser temporário e localizado, com reavaliação post
- Em uma cavidade, a ausência de danos visíveis durante uma visita prova que a frequência de
- Se a contagem de visitantes ficar abaixo do limite estabelecido, não é necessário observar
- A consulta a comunidades vizinhas pode ajudar a identificar usos e conflitos que uma equip
- Toda alteração observada na água de uma caverna deve ser atribuída à visitação, ainda que 
```

## Pedido 30: Pesquisa Científica (`minas-gerais.pesquisa-cientifica`, 1 tópicos de edital)

```
Matéria: minas-gerais.pesquisa-cientifica (Pesquisa Científica)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Pesquisa científica e patrimônio natural": 25
- "2. Estudos geológicos em cachoeiras, grutas e cavernas": 25
- "3. Evidências, conservação e manejo": 25
- "4. Reescrita e como a IDECAN cobra": 25

Já existem (não repita):
- Em um inventário de uma cachoeira, qual registro mais contribui para que outra equipe conf
- Qual enunciado descreve melhor a contribuição da pesquisa para conservar uma gruta?
- Ao investigar a queda de água em um trecho rochoso, qual fator geológico deve ser consider
- Qual alternativa apresenta uma relação interdisciplinar adequada para estudar uma caverna?
- Uma medida de sedimentos feita uma única vez permite afirmar que houve tendência de aument
- Qual é a interpretação mais cautelosa diante de uma gruta em calcário com condutos e lagos
- Um ponto de uma caverna apresenta alteração após aumento de visitação. Qual conclusão é ci
- Uma equipe encontra água turva em uma ressurgência após chuva intensa. Qual procedimento i
- Qual descrição é compatível com a formação de uma cavidade cárstica em rocha carbonática?
- Em uma pesquisa de campo, o que distingue a hipótese de que uma estrada alterou uma nascen
- Por que o registro de temperatura e umidade pode ser útil em uma caverna visitada?
- Qual prática reduz o risco de a própria pesquisa degradar um sítio geológico?
- Qual informação ajuda a avaliar se um aumento de sedimentos em um curso d’água é persisten
- Em uma área protegida, pesquisadores pretendem instalar sensores em uma cavidade. Qual enc
- Um fóssil aparece associado a sedimentos no interior de uma gruta. Que interpretação integ
- Após um novo caminho, há mais sedimentos em um riacho que alimenta uma queda d’água. Qual 
- Em uma cavidade calcária, uma equipe observa água que desaparece numa depressão e reaparec
- Uma medição de água em caverna foi feita no período seco; meses depois, um novo valor dife
- Um relatório descreve uma caverna como frágil e recomenda acesso limitado. Qual dado apoia
- Qual afirmação distingue corretamente uma cavidade natural de uma galeria escavada para um
- Uma unidade de conservação autoriza trilhas e recebe projetos científicos. Qual interpreta
- Uma série de registros mostra que a vazão de uma cachoeira diminui em meses secos e retorn
- Uma estudante afirma que a Gruta da Lapinha exemplifica uma cavidade em calcário no contex
- Após observar marcas recentes em espeleotemas, qual sequência preserva melhor evidências e
- Uma equipe conclui que um trecho subterrâneo perdeu fauna porque não encontrou organismos 
- Ao reescrever uma conclusão, qual substituição preserva o grau de certeza de que a chuva p
- Uma equipe encontra uma camada de sedimento sobre uma passagem em uma caverna. Qual aborda
- Um estudo recomenda desviar visitantes de uma área com fauna sensível, embora a trilha est
- Qual alternativa mantém a cautela científica ao converter a observação de uma surgência em
- Uma comparação entre duas cavernas mostra diferenças de umidade, mas foi feita com aparelh
- A pesquisa científica pode reunir geologia, hidrologia e biologia para compreender uma cav
- Uma única medição de vazão demonstra, por si só, a tendência anual de uma cachoeira.
- A Gruta da Lapinha está no Parque Estadual do Sumidouro e se desenvolveu em calcário da Fo
- Se uma alteração ocorre depois de uma obra, a sequência temporal comprova que a obra foi s
- A pesquisa em unidade de conservação estadual pode depender de autorização prévia do IEF.
- A dissolução de rochas carbonáticas pode contribuir para a formação de cavidades cársticas
- A ausência de registro de um impacto em uma campanha prova que ele nunca ocorreu no local.
- Em sistemas cársticos, a relação entre águas superficiais e subterrâneas pode ser relevant
- A proteção legal de uma caverna torna desnecessários o monitoramento e o manejo de visitaç
- A Gruta da Lapinha pode ser usada como exemplo de relação entre geologia, relevo cárstico 
- A pesquisa científica precisa produzir certeza absoluta antes de oferecer qualquer informa
- Cachoeiras, grutas e cavernas devem ser estudadas por métodos idênticos porque todas são f
- Em uma pesquisa, é válido comunicar uma hipótese como hipótese e indicar quais dados ainda
- Quando uma caverna está seca durante o levantamento, depósitos e formas internas deixam de
- A observação de água turva após uma chuva permite excluir de imediato qualquer influência 
```

## Pedido 31: Visão de futuro e cenários de riscos no Brasil (`defesa-civil.visao-futuro-cenarios-riscos-brasil`, 1 tópicos de edital)

```
Matéria: defesa-civil.visao-futuro-cenarios-riscos-brasil (Visão de futuro e cenários de riscos no Brasil)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Risco, futuro e construção de cenários": 25
- "2. Brasil: ameaças, vulnerabilidades e cenários combinados": 25
- "3. Marcos legais e institucionais da visão de risco": 25
- "4. Reescrita e como a prova cobra": 25

Já existem (não repita):
- Em um estudo municipal, qual descrição corresponde a um cenário de risco?
- Em um vale sujeito a cheias, qual elemento representa a exposição?
- Qual distinção entre risco e desastre está tecnicamente correta?
- Uma equipe dispõe de dados incompletos sobre uma encosta ocupada. Qual é a consequência de
- Qual afirmação diferencia suscetibilidade e risco em um mapa territorial?
- Ao revisar os marcos do edital, qual alternativa apresenta corretamente as prioridades do 
- Após uma enxurrada, o município precisa identificar o tipo de desastre no registro oficial
- Um plano diretor municipal evita novas moradias em várzea já identificada como sujeita a i
- Um boletim de alerta informa probabilidade elevada de enxurrada em uma área rural. O que s
- Sobre o Plano Nacional de Proteção e Defesa Civil vigente, qual horizonte se associa ao PN
- Em cenário de estiagem prolongada, uma comunidade rural depende de captação vulnerável e p
- Ao planejar ações para uma bacia compartilhada por diversos municípios, qual unidade a PNP
- Um alerta e rotas de evacuação já foram testados em um distrito serrano, embora a possibil
- Após um deslizamento que bloqueia estrada e interrompe o abastecimento de água, como se de
- Uma análise para dez anos combina aquecimento, adensamento urbano, áreas verdes e dependên
- Uma equipe compara registros de estiagem e de incêndio em vegetação para elaborar cenário 
- Ao explicar a visão de futuro nacional, qual alternativa vincula corretamente os instrumen
- Antes da estação chuvosa, o município recebe dados incertos sobre uma encosta com moradias
- Uma cidade avalia cenário de seca para oito anos, prevendo aumento da demanda urbana, capt
- Um loteamento ainda não ocupado fica em faixa com propensão a movimento de massa, mas não 
- Ao relacionar a PNPDEC, o PN-PDC e Sendai a uma análise de risco com dados incompletos, qu
- Uma sirene é ativada em área sujeita a rompimento de barragem, mas a estrutura permanece e
- Dois municípios vizinhos compartilham uma bacia e um plano de monitoramento aponta cheia a
- Uma barragem de uso privado apresenta indicadores de instabilidade. Qual leitura das obrig
- Em um mesmo evento de chuva, o bairro X tem moradias frágeis e acesso precário a serviços,
- Uma enchente já ocorreu, deixou danos e exigirá reconstrução de moradias em local seguro. 
- O município possui mapa antigo de risco, crescimento recente em encostas e alteração no pa
- Diante de um cenário provável de incêndio, de um código COBRADE de desastre passado e do n
- Na revisão de um texto, a expressão cenário de risco é substituída por desastre inevitável
- Uma afirmação diz que Sendai e o PN-PDC possuem o mesmo horizonte temporal, embora indique
- A lei federal inclui prevenção, mitigação, preparação, resposta e recuperação em uma abord
- Um cenário prospectivo é uma garantia de que a ameaça descrita ocorrerá exatamente como fo
- A COBRADE padroniza a classificação e a codificação brasileira de desastres.
- O Marco de Sendai estabelece quatro prioridades de ação e sete metas globais.
- A Lei nº 12.608/2012 dispensa a participação social quando há mapa técnico de risco já ela
- Em cenário nacional, a mesma ameaça produzirá efeitos idênticos em todos os territórios, m
- Um alerta antecipado pode apoiar decisões de preparação e proteção antes que danos sejam c
- A suscetibilidade física de uma encosta e a vulnerabilidade social de seus moradores são c
- O PN-PDC 2025–2035 contempla prevenção, mitigação, preparação, resposta e recuperação e bu
- Uma classe de risco alto em mapa pode orientar prioridades sem provar que o desastre ocorr
- Se a probabilidade de uma ameaça é incerta, a lei impede qualquer medida de prevenção até 
- Um cenário de calor extremo pode combinar mudança climática, adensamento urbano, pouca arb
- Em prevenção de desastres relacionados a corpos d'água, a PNPDEC utiliza cada município co
- Se a COBRADE permite registrar uma enxurrada, também fornece por si só a vulnerabilidade s
- Uma obra de reconstrução que restaura moradias no mesmo setor suscetível sem corrigir expo
```

## Pedido 32: Plano de carreira dos policiais civis de Minas Gerais (Decreto nº 46.549/2014) (`legislacao.plano-carreira-policiais-civis-mg`, 1 tópicos de edital)

```
Matéria: legislacao.plano-carreira-policiais-civis-mg (Plano de carreira dos policiais civis de Minas Gerais (Decreto nº 46.549/2014))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Estrutura do plano e regras gerais da promoção": 25
- "2. Interstício, período aquisitivo, periodicidade e efeitos": 25
- "3. Antiguidade, merecimento, bravura, invalidez e post mortem": 25
- "4. Processamento, competência, progressão e recursos": 25

Já existem (não repita):
- Segundo o Decreto nº 46.549/2014, no plano de carreira dos policiais civis de Minas Gerais
- O Decreto nº 46.549/2014 prevê como espécies de promoção a antiguidade, o merecimento, a i
- Os limites de vagas por nível previstos para Investigador e Escrivão no Decreto nº 46.549/
- Os limites de vagas por nível para promoção nas carreiras de Delegado de Polícia, Médico-L
- NÃO pode concorrer à promoção, por qualquer critério, segundo o Decreto nº 46.549/2014, o 
- O policial civil que cumpre pena privativa de liberdade por crime doloso, substituída por 
- A ocorrência de qualquer das situações impeditivas da promoção previstas no art. 6º do Dec
- Para a promoção por antiguidade (em regra) e por merecimento, o Decreto nº 46.549/2014 exi
- Durante o estágio probatório, o policial civil somente poderá ser promovido por invalidez 
- A partir de 1º de janeiro de 2015, o período aquisitivo exigido para a promoção especial d
- As promoções por invalidez, post mortem e por antiguidade pelo critério aposentadoria pode
- Segundo o Decreto nº 46.549/2014, a promoção relativa ao processo do primeiro semestre pro
- Ao publicar o quantitativo de servidores por nível, em março e setembro, o Chefe da PCMG d
- As licenças e afastamentos que não sejam considerados efetivo exercício pela legislação:
- Na classificação para promoção por antiguidade em razão do tempo no nível, o primeiro crit
- A promoção especial de Escrivão e Investigador de Polícia retroage seus efeitos à data da 
- A promoção por antiguidade pelo critério aposentadoria, concedida ao ocupante de nível int
- A promoção por merecimento decorrente de mérito profissional compreende, segundo o Decreto
- Na fase de inscrição da promoção por mérito profissional, estará apto para a habilitação o
- É um dos atributos profissionais pontuados na inscrição para promoção por merecimento, seg
- O policial habilitado que deixar de apresentar os documentos comprobatórios de seus atribu
- Na votação da promoção por mérito profissional, será promovido o candidato melhor avaliado
- Na votação da promoção por mérito profissional, o Conselho Superior da PCMG leva em consid
- A promoção por merecimento decorrente de ato de bravura será concedida ao policial que, no
- A ação que resulte na morte do servidor caracteriza ato de bravura para fins de promoção p
- O ato de promoção por invalidez retroage à:
- A promoção post mortem é devida ao policial falecido em decorrência de acidente em serviço
- Não se efetivará a promoção post mortem quando o óbito tiver ocorrido:
- A caracterização e o reconhecimento das situações que asseguram promoção por ato de bravur
- Segundo o Decreto nº 46.549/2014, são de competência do Governador do Estado os atos de pr
- A progressão do policial civil para o grau subsequente depende de requerimento expresso do
- Sobre a progressão no plano de carreira dos policiais civis, o Decreto nº 46.549/2014 esta
- O direito de recorrer de decisões relacionadas a processo de promoção, na esfera administr
- É nula a promoção ou progressão realizada por erro ou fraude, ainda que sem a participação
- As Comissões Permanentes de Promoção, instituídas pelo Chefe da PCMG, têm a função de:
- O desenvolvimento na carreira dos Analistas, Técnicos Assistentes e Auxiliares da Polícia 
- Segundo o Decreto nº 46.549/2014, o ato de declaração de estabilidade do policial civil, d
- Não pode se inscrever à promoção por mérito profissional o policial civil que possuir ante
- Os atos referentes às promoções e progressões dos policiais civis devem ser publicados:
- A promoção é a passagem do policial civil do nível em que se encontra para o nível subsequ
```

## Pedido 33: Estatuto dos Militares de Pernambuco (Lei 6.783/1974) (`legislacao.estatuto-pm-pernambuco-lei-6783`, 1 tópicos de edital)

```
Matéria: legislacao.estatuto-pm-pernambuco-lei-6783 (Estatuto dos Militares de Pernambuco (Lei 6.783/1974))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Generalidades, ingresso, hierarquia e cargo": 20
- "2. Obrigações, deveres, ética e responsabilização": 20
- "3. Direitos, remuneração, promoções, férias e licenças": 20
- "4. Prerrogativas, uniformes e Sistema de Proteção Social": 20
- "5. Situações especiais, inatividade, exclusão e tempo de serviço": 20

Já existem (não repita):
- Segundo a Lei 6.783/1974, a Polícia Militar de Pernambuco é:
- De acordo com o Estatuto dos Militares de Pernambuco, os componentes da reserva remunerada
- Na Lei 6.783/1974, a carreira de Oficial da Polícia Militar é privativa de brasileiro nato
- Segundo o Estatuto da PMPE, posto e graduação são conferidos, respectivamente:
- Quanto à precedência das praças especiais na PMPE, é correto afirmar:
- Dois soldados da PMPE têm a mesma antiguidade na graduação, graus anteriores e data de inc
- Em igualdade de posto ou graduação, os policiais-militares da inatividade têm precedência 
- Segundo a Lei 6.783/1974, consideram-se vagos os cargos policiais-militares cujos ocupante
- O Estatuto da PMPE, ao tratar da atividade comercial, estabelece que o policial-militar da
- São preceitos da ética policial-militar previstos na Lei 6.783/1974, EXCETO:
- O compromisso de honra do policial-militar terá caráter solene e será prestado na presença
- Segundo o Estatuto da PMPE, os cabos e soldados são:
- Um soldado da PMPE praticou, no mesmo fato, crime militar e transgressão disciplinar. Pelo
- Conforme a Lei 6.783/1974, as penas disciplinares de detenção ou prisão não podem ultrapas
- Sobre os conselhos previstos no Estatuto da PMPE, assinale a correta.
- São competentes para determinar o imediato afastamento do cargo de policial-militar que se
- Pela Lei 6.783/1974, a praça adquire estabilidade quando completa:
- O policial-militar que se julgar prejudicado por ato administrativo de superior, não relac
- No Estatuto da PMPE, o pedido de reconsideração, a queixa e a representação podem ser feit
- Um policial-militar com 7 anos de efetivo serviço se candidata a cargo eletivo. Pela Lei 6
- O soldo do policial-militar é irredutível e não está sujeito a penhora, sequestro ou arres
- Sobre as promoções na PMPE, assinale a alternativa correta.
- Pelo art. 62 da Lei 6.783/1974, o policial-militar tem direito a afastamento total do serv
- As férias não gozadas por impossibilidade absoluta ou interrupção legal são computadas em 
- Sobre a licença especial do policial-militar de Pernambuco, é correto afirmar:
- A licença para tratar de interesse particular é concedida ao policial-militar com mais de 
- Segundo o Estatuto da PMPE, o policial-militar somente poderá ser preso por autoridade pol
- É prerrogativa do policial-militar cumprir pena de prisão ou detenção somente em organizaç
- Pelo Estatuto da PMPE, é proibido ao policial-militar o uso de uniforme, EXCETO:
- Sobre o Sistema de Proteção Social dos Militares de Pernambuco (SPSMPE), incluído pela LC 
- Na pensão militar do SPSMPE, os pais que comprovem dependência econômica do militar integr
- A pensão militar requerida até trinta dias depois do óbito do instituidor é devida a parti
- Agregação é a situação na qual o policial-militar da ativa permanece sem número na sua esc
- Pelo Estatuto da PMPE, deve ser agregado o policial-militar que:
- Segundo a Lei 6.783/1974, é considerado ausente o policial-militar que, por mais de vinte 
- Um policial-militar da ativa desapareceu durante operação em área de calamidade, sem indíc
- Com a redação da LC 460/2021, a transferência de ofício para a reserva remunerada por idad
- Não será concedida transferência para a reserva remunerada, a pedido, ao policial-militar 
- Um soldado da PMPE foi julgado incapaz definitivamente em razão de ferimento recebido na m
- O militar do Estado que incorra em situação de reforma por incapacidade definitiva para a 
- No Estatuto da PMPE, a demissão e o licenciamento aplicam-se, respectivamente:
- Sobre a exclusão a bem da disciplina e a deserção na PMPE, assinale a correta.
- O oficial da PMPE perderá o posto e a patente se for declarado indigno do oficialato ou co
- Pela Lei 6.783/1974, NÃO é computável, para efeito algum, o tempo:
- Segundo o Estatuto da PMPE, o Comandante-Geral tem honras, prerrogativas, regalias, direit
```

