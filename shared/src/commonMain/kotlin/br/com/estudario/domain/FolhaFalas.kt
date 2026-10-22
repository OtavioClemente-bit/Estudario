package br.com.estudario.domain

/**
 * Tudo o que o Folha fala, num lugar só (o site e o app usam o mesmo banco). A escolha usa uma semente
 * (o momento em que a tela abriu, o id da questão, o dia): a fala não troca a cada redesenho e também
 * nunca fica igual por muito tempo. Junto das falas gerais entram as do contexto (horário, dia da
 * semana, distância da prova), que aparecem de vez em quando para soar natural, não repetitivo.
 */
object FolhaFalas {
    fun <T> List<T>.pick(seed: Long): T = this[(((seed % size) + size) % size).toInt()]

    // ------------------------------------------------------------ Início, cartão "Agora"

    private val homeStart = listOf(
        "Bora? Seu próximo estudo está aqui.",
        "Quem estuda hoje não corre atrás amanhã.",
        "Um tópico de cada vez. É assim que o edital acaba.",
        "A vaga não tem nome ainda. Vamos colocar o seu?",
        "Constância vence talento que não aparece.",
        "Hoje é um bom dia para ficar mais perto da posse.",
        "Pouco todo dia rende mais que muito de vez em quando.",
        "Abre o tópico comigo? Eu seguro a página.",
        "O concorrente também está estudando. Bora na frente?",
        "Cada tópico fechado é um ponto a mais na prova.",
        "Disciplina é lembrar do que você quer, mesmo cansado.",
        "Sua aprovação está sendo construída agora.",
        "Edital grande se vence assim: começando.",
        "Café, foco e eu. Partiu?",
        "Dez minutos já contam. Começa e vê o tempo passar.",
        "A prova não pergunta se você estava com vontade.",
        "Seu eu do futuro vai agradecer por esse estudo.",
        "Bora transformar esse tópico em acerto na prova?",
        "O mais difícil é abrir. Depois eu te levo.",
        "Já separei o que importa. Só falta você.",
        "Hoje não precisa ser perfeito. Precisa ser feito.",
        "Um passo hoje, outro amanhã. É assim que se chega.",
        "Vamos riscar mais um item do edital?",
        "Esse tópico cai. E quando cair, você vai sorrir.",
        "Foco no próximo tópico, não no edital inteiro.",
        "Celular de lado, cabeça aqui. Cinco minutos e engrena.",
        "O plano está pronto. Você só precisa apertar o play.",
        "Estudar cansado ainda vale mais que não estudar.",
        "Quem passa não é quem sabe tudo. É quem não para.",
        "Me dá meia hora e eu te devolvo um tópico dominado.",
        "Lembra por que você começou? Eu lembro. Bora.",
        "Hoje é dia de aprender uma coisa que vai cair.",
        "Mais um tópico e o edital fica um pouco menor.",
        "Nada de maratona. Só o próximo passo.",
        "Uma boa sessão hoje vale mais que três promessas.",
        "Sua cadeira na sala de posse está esperando.",
        "Teoria, questões e revisão. Hoje é a parte da teoria.",
        "A diferença entre querer e passar é o que você faz agora.",
        "Prometo que esse aqui é mais tranquilo do que parece.",
        "O edital não se estuda sozinho. Ainda bem que você tem a mim.",
        "Começa pelo título. O resto vem.",
        "Estudar com calma também é estudar com força.",
        "Você não precisa de motivação. Precisa de começar.",
        "A banca já escolheu as perguntas. Bora conhecer as respostas?",
        "Mais um dia no caminho certo começa por aqui.",
        "Hoje a gente transforma dúvida em ponto.",
        "Pequeno, constante e bem feito. Esse é o segredo.",
        "Uma sessão de cada vez. Eu conto as vitórias.",
    )
    private val homeContinue = listOf(
        "Bora terminar o que começamos?",
        "Você parou no meio. Eu guardei a página.",
        "Falta pouco para fechar esse tópico.",
        "Começou, agora termina. É assim que se passa.",
        "Voltou! Vamos de onde você parou.",
        "Esse tópico está quase seu. Só mais um pouco.",
        "A parte difícil você já fez: começar.",
        "Marquei onde você parou. É só seguir.",
        "Terminar o que começou é metade da aprovação.",
        "Ficou uma ponta solta aqui. Bora amarrar?",
        "Seu progresso está salvo. Bora completar?",
        "Volta comigo? O fim desse tópico está logo ali.",
        "Missão pela metade não conta ponto. Bora fechar.",
        "Retomando de onde parou: sem perder nada.",
    )
    private val homeGenerate = listOf(
        "Vamos preparar o material deste tópico?",
        "Eu escrevo a teoria, você só estuda. Topa?",
        "Esse tópico ainda está em branco. Bora preencher?",
        "Um toque e eu monto teoria, flashcards e questões.",
        "Deixa comigo: preparo tudo do jeito da sua banca.",
        "Esse aqui ainda não tem material. Em um minuto eu resolvo.",
        "Me dá esse tópico que eu devolvo pronto pra estudar.",
        "Teoria mastigada e questões no estilo da banca. Gero agora?",
    )
    private val morning = listOf(
        "Bom dia! Cabeça descansada aprende mais rápido.",
        "Começar cedo é sair na frente. Bora?",
        "Bom dia! A memória da manhã é das melhores.",
        "Café na mão e um tópico na tela. Combina.",
        "Quem estuda de manhã ganha o resto do dia livre.",
        "Bom dia! Que tal resolver o mais difícil agora?",
        "Acordou cedo pra estudar? Isso é atitude de aprovado.",
        "Manhã silenciosa é o melhor lugar pra aprender.",
        "Bom dia! Hoje eu estou animado. E você?",
        "O sol nem esquentou e você já está aqui. Gosto disso.",
    )
    private val afternoon = listOf(
        "Boa tarde! Bora aproveitar esse pedaço do dia?",
        "Depois do almoço dá sono, eu sei. Questões acordam.",
        "Tarde produtiva começa com um tópico curto.",
        "Boa tarde! Ainda dá tempo de fazer o dia render.",
        "Um intervalo bem usado vale ouro. Bora?",
        "Boa tarde! Que tal umas questões pra animar?",
        "A tarde é longa. Meia hora dela é nossa?",
    )
    private val evening = listOf(
        "Boa noite! Ainda dá pra fechar o dia com estudo.",
        "Chegou cansado? Começa leve, que eu acompanho.",
        "Noite é ótima pra revisar o que já viu.",
        "Fim de dia, mas não fim de foco. Bora?",
        "Boa noite! Uma sessão curta e o dia está ganho.",
        "Depois de um dia cheio, estudar é quase heroísmo.",
        "Quem estuda à noite está construindo outra vida.",
    )
    private val lateNight = listOf(
        "Estudo da noite também conta. Bora fechar o dia bem?",
        "Um último tópico antes de dormir? A memória agradece.",
        "Tá tarde, hein? Algo curto e cama.",
        "Dormir também é estudar: é quando a memória fixa.",
        "Corujão de estudos! Só não esquece de descansar.",
        "Madrugada é silenciosa. Mas o sono também é importante.",
        "Revisão rápida e boa noite. Combinado?",
        "Admiro a garra. Mas amanhã você precisa estar inteiro.",
    )
    private val monday = listOf(
        "Segunda-feira: semana nova, edital menor.",
        "Começar a semana estudando é meio caminho andado.",
        "Segunda chegou. Bora dar o tom da semana?",
        "Nova semana, nova chance de ficar à frente.",
    )
    private val friday = listOf(
        "Sexta! Fecha a semana com chave de ouro?",
        "Sexta-feira também é dia de estudo. Depois, descanso merecido.",
        "Um tópico hoje e o fim de semana fica mais leve.",
        "Sexta de quem vai passar: estudando um pouquinho.",
    )
    private val weekend = listOf(
        "Fim de semana! Um pouco hoje faz diferença na segunda.",
        "Sábado de estudo é sábado de quem quer muito.",
        "Domingo leve: uma revisão e o resto do dia é seu.",
        "Enquanto muitos descansam, você avança. Mas descansa também!",
        "Fim de semana é ótimo pra colocar revisão em dia.",
        "Estudar no fim de semana conta em dobro na motivação.",
    )
    private fun examLines(days: Long): List<String> = when {
        days <= 0 -> listOf("É hoje! Respira fundo. Você se preparou para isso.")
        days == 1L -> listOf("Amanhã é a prova. Revisão leve e cama cedo, combinado?", "Véspera de prova: confia no que você estudou.")
        days <= 7 -> listOf(
            "Faltam $days dias. Agora é revisão e questões, sem conteúdo novo.",
            "Reta final: $days dias. Cada revisão vale muito agora.",
            "$days dias para a prova. Descanso também faz parte do plano.",
        )
        days <= 30 -> listOf(
            "Faltam $days dias. A reta final é onde a vaga se decide.",
            "$days dias para a prova. Foco total no que mais cai.",
            "Menos de um mês! Hora de afiar com muitas questões.",
            "$days dias. Cada sessão agora pesa mais.",
        )
        days <= 90 -> listOf(
            "Faltam $days dias. Dá tempo de fazer bonito com constância.",
            "$days dias para a prova: tempo bom pra fechar o edital.",
            "Ainda são $days dias. Sem pressa, sem pausa.",
        )
        else -> listOf(
            "Faltam $days dias. Quem começa cedo chega tranquilo.",
            "$days dias pela frente. Base forte agora, revisão depois.",
        )
    }

    /**
     * Frase do Folha no cartão "Agora". [seed] muda a cada vez que o Início abre. [weekday]: 1 segunda
     * a 7 domingo (0 sem dia). Às vezes ele comenta o horário, o dia ou a prova chegando.
     */
    fun home(continuing: Boolean, needsMaterial: Boolean, hour: Int, seed: Long, weekday: Int = 0, daysToExam: Long? = null): String {
        if (needsMaterial) return homeGenerate.pick(seed)
        if (continuing) return homeContinue.pick(seed)
        val roll = ((seed / 7) % 10 + 10) % 10
        if (daysToExam != null && daysToExam <= 7 && roll < 6) return examLines(daysToExam).pick(seed)
        if (daysToExam != null && roll == 0L) return examLines(daysToExam).pick(seed / 3)
        if (roll in 1L..3L) {
            val time = when (hour) { in 5..11 -> morning; in 12..17 -> afternoon; in 18..21 -> evening; else -> lateNight }
            return time.pick(seed / 11)
        }
        if (roll == 4L) when (weekday) {
            1 -> return monday.pick(seed / 13)
            5 -> return friday.pick(seed / 13)
            6, 7 -> return weekend.pick(seed / 13)
        }
        return homeStart.pick(seed)
    }

    /** Dia sem tarefa pendente: a missão acabou ou é folga. */
    fun dayDone(seed: Long): String = listOf(
        "Missão cumprida! Descansa, que amanhã a gente continua.",
        "Dia fechado. É assim, um dia de cada vez, que se chega na posse.",
        "Tudo feito por hoje. Tô orgulhoso de você!",
        "Plano do dia concluído. A sequência está garantida.",
        "Fechou o dia! Agora é descansar pra memória trabalhar.",
        "Cumpriu tudo. Pode desligar a cabeça sem culpa.",
        "Hoje você fez a sua parte. Amanhã eu te chamo.",
        "Dia vencido! Se quiser, umas questões extras. Se não, merecido descanso.",
        "Tarefa do dia feita. Isso, repetido, vira aprovação.",
        "Mais um dia completo no histórico. Que orgulho!",
        "Você terminou o que planejou. Pouca gente faz isso.",
        "Dia redondo! Vai lá viver um pouco, que eu cuido do plano.",
    ).pick(seed)

    // ------------------------------------------------------------ Perfil, desempenho e conquistas

    /** O Folha no perfil: comenta o caminho da pessoa, nunca do mesmo jeito. */
    fun profile(level: Int, streak: Int, xpToday: Int, seed: Long): String {
        val options = buildList {
            if (streak >= 2) {
                add("$streak dias seguidos! Constância vence talento.")
                add("$streak dias sem falhar. Essa sequência é sua, não deixa ela cair!")
                add("Sequência de $streak dias. Isso é hábito, não sorte.")
            } else {
                add("Bora começar uma sequência hoje? O primeiro dia é o mais importante.")
                add("Toda sequência grande começou com um dia. Hoje pode ser o primeiro.")
            }
            if (xpToday > 0) {
                add("+$xpToday XP hoje. Cada ponto é um passo mais perto da posse.")
                add("Já são $xpToday XP hoje. Tô vendo esforço de verdade.")
            } else add("Ainda sem XP hoje. Um tópico já muda isso.")
            add("Nível $level! Quem chegou até aqui não para no meio do caminho.")
            add("Nível $level e subindo. Eu sabia que você ia longe.")
            add("Cada emblema aqui foi conquistado de verdade. Bora pelo próximo?")
            add("Seu histórico conta a história da sua aprovação. E ela está ficando boa.")
            add("Eu guardo tudo o que você estudou. Você só precisa continuar.")
            add("Olha quanta coisa você já fez. Dá orgulho, né?")
            add("Esse perfil é o retrato de quem não desiste.")
            add("Daqui a pouco esse perfil vai ter um emblema novo. Sinto isso.")
        }
        return options.pick(seed)
    }

    fun result(percent: Int, seed: Long): String = when {
        percent >= 90 -> listOf(
            "Que bateria! Com esse desempenho, a aprovação é questão de tempo.",
            "Impressionante. Você está voando nesse conteúdo!",
            "Quase gabaritou! Esse assunto já é seu.",
            "Nível prova de verdade. Pode mandar mais difícil.",
            "Eu fiquei até sem palavras. Que domínio!",
            "Desse jeito a banca vai ter que inventar pergunta nova.",
        ).pick(seed)
        percent >= 70 -> listOf(
            "Muito bom! Acima de 70% é ritmo de aprovado.",
            "Mandou bem! Os erros já foram pro caderno pra gente revisar.",
            "Resultado de quem está no caminho certo.",
            "Bom demais! Revisando os erros, isso vai a 90.",
            "Acima da média de aprovação. Segue firme!",
            "Ótima bateria. Só falta polir os detalhes.",
        ).pick(seed)
        percent >= 50 -> listOf(
            "Bom caminho! Revisando os erros, a próxima sai bem melhor.",
            "Metade já está dominada. Bora atacar o resto juntos?",
            "Deu pra ver onde está o ponto fraco. Isso é ouro.",
            "Nem bom, nem ruim: informativo. Agora a gente sabe o que treinar.",
            "Tá chegando lá. Mais uma rodada e esse número sobe.",
            "Os erros de hoje são os acertos da prova.",
        ).pick(seed)
        else -> listOf(
            "Dia difícil acontece. O importante é que você treinou, e cada erro virou aprendizado.",
            "Não desanima! Os erros estão guardados e vão voltar na hora certa pra você fixar.",
            "Errar treinando é o melhor jeito de não errar na prova.",
            "Assunto novo é assim mesmo. Vamos ler a teoria e voltar mais forte?",
            "Esse conteúdo vai virar seu. Só precisa de mais uma volta.",
            "Respira. Ninguém nasce sabendo. Eu te ajudo a revisar.",
        ).pick(seed)
    }

    fun stats(accuracy: Int?, seed: Long): String = when {
        accuracy == null -> "Resolve umas questões comigo? Aí eu te mostro onde você está forte e onde reforçar."
        accuracy >= 70 -> listOf(
            "Acerto de aprovado! Agora é manter o ritmo.",
            "Acima de 70%. A banca que se prepare!",
            "Esses números contam uma história bonita.",
            "Desempenho firme. Agora é não deixar cair.",
            "Com esse acerto, cada questão nova é chance de confirmar.",
        ).pick(seed)
        accuracy >= 50 -> listOf(
            "No caminho certo. Revisando os pontos fracos, isso sobe rápido.",
            "Metade do caminho andado. Bora reforçar as matérias em vermelho?",
            "O gráfico está subindo. Mais um pouco e passa dos 70.",
            "Dá pra ver evolução. Foco nas matérias mais fracas agora.",
            "Bom ponto de partida. A revisão é o que leva para cima.",
        ).pick(seed)
        else -> listOf(
            "Base em construção. Cada erro agora é um acerto no dia da prova.",
            "Calma: errar treinando é o melhor jeito de não errar na prova.",
            "Todo aprovado já esteve exatamente aqui.",
            "Os números vão subir. A gente começa pela teoria e volta às questões.",
            "Não olha só o número: olha o quanto você já treinou.",
        ).pick(seed)
    }

    fun streak(days: Int, seed: Long): String = when {
        days <= 1 -> listOf(
            "Primeiro dia da sequência! Todo aprovado começou exatamente assim.",
            "Dia 1. O mais importante de todos.",
            "Começou a sequência! Amanhã a gente faz o dia 2.",
        ).pick(seed)
        days < 7 -> listOf(
            "$days dias seguidos! Constância vence talento.",
            "$days dias sem falhar. Tô orgulhoso de você!",
            "$days dias! A primeira semana está logo ali.",
            "Sequência de $days. O hábito está nascendo.",
        ).pick(seed)
        days < 30 -> listOf(
            "$days dias de sequência! Isso já é hábito de aprovado.",
            "$days dias! A banca que se prepare.",
            "$days dias seguidos. Você está ficando imparável.",
            "Sequência de $days dias. Pouca gente chega aqui.",
        ).pick(seed)
        days < 100 -> listOf(
            "$days dias seguidos. Você é a definição de disciplina!",
            "$days dias! Isso não é fase, é estilo de vida.",
            "Sequência de $days. Eu nem sei mais o que dizer de tanto orgulho.",
        ).pick(seed)
        else -> listOf(
            "$days dias seguidos. Lendário. Simplesmente lendário.",
            "$days dias! Você já é história aqui no Estudário.",
        ).pick(seed)
    }

    fun badge(seed: Long): String = listOf(
        "Olha o que você conquistou!",
        "Emblema novo na coleção! Merecido demais.",
        "Mais uma conquista. Bora pela próxima?",
        "Forjado com esforço. Esse é seu pra sempre.",
        "Medalha nova! Coloca no peito, que você merece.",
        "Essa conquista conta uma parte da sua história.",
        "A coleção está crescendo. E você também.",
    ).pick(seed)

    // ------------------------------------------------------------ questões

    private val correct = listOf(
        "Isso! Mandou muito bem.",
        "Acertou! Esse conteúdo já é seu.",
        "Na mosca! Banca nenhuma te pega nessa.",
        "Boa! É assim que a vaga vai chegando.",
        "Perfeito. Pode marcar esse ponto no placar.",
        "Acertou e com estilo. Próxima!",
        "Exato! Você pensou igual ao gabarito.",
        "Certinho. Essa não te pega mais.",
        "Uau, essa era difícil. E você acertou.",
        "Mais um ponto pra conta da aprovação.",
        "Acertou sem titubear. Gostei!",
        "É isso aí! Raciocínio afiado.",
        "Correto! Esse assunto está ficando fácil, né?",
        "Boa! Quem estuda colhe.",
        "Nem a pegadinha te segurou.",
        "Mandou bem. Segue que tem mais.",
        "Acertou! Já pode ensinar isso pra alguém.",
        "Show! Isso é treino virando resultado.",
    )
    private val wrong = listOf(
        "Errar aqui é melhor que errar na prova. Lê a explicação comigo?",
        "Tudo bem! Essa volta pro seu caderno de erros e na próxima é sua.",
        "Quase! Esse é exatamente o tipo de pegadinha que a banca adora.",
        "Respira. Cada erro agora é um ponto a mais no dia da prova.",
        "Faz parte. Entendendo o porquê, você não erra mais essa.",
        "Opa, essa enganou. Bora entender e virar o jogo?",
        "Errou, mas agora conhece a pegadinha. Isso vale muito.",
        "Essa era traiçoeira. A explicação vai clarear.",
        "Tranquilo. Vou guardar essa pra revisar na hora certa.",
        "Não foi dessa vez. A próxima parecida é sua.",
        "Erro anotado, aprendizado garantido.",
        "Lê com calma a explicação: é ali que mora o ponto.",
        "Banca adora essa armadilha. Agora você já conhece.",
        "Quem erra treinando acerta na prova. Segue!",
    )

    fun answer(isCorrect: Boolean, seed: Long): String = (if (isCorrect) correct else wrong).pick(seed)

    fun relief(seed: Long): String = listOf(
        "Ufa! Errou a anterior, mas essa você virou. É assim que se aprende.",
        "Ufa! Recuperou na hora. Essa sacudida faz bem.",
        "Ufa, voltou pro trilho! Bora manter.",
        "Viu? Um erro não define a bateria. Mandou bem agora.",
        "Recuperação de campeão! Segue assim.",
        "Errou, aprendeu, acertou. O ciclo perfeito.",
    ).pick(seed)

    fun fire(seed: Long): String = listOf(
        "Tá pegando fogo! Ninguém te segura hoje.",
        "Sequência absurda! A banca que se cuide.",
        "Em chamas! Continua nesse ritmo que a vaga é sua.",
        "Isso é constância de quem passa. Próxima!",
        "Acerto atrás de acerto. Que fase!",
        "Você entrou no modo prova. Não para agora!",
        "Eu já parei de contar os acertos. Que bateria!",
    ).pick(seed)

    // ------------------------------------------------------------ a missão do ponto fraco

    /** Depois de treinar o ponto fraco: comenta o acerto antes e depois. */
    fun mission(subject: String, before: Int, after: Int, seed: Long): String {
        val d = after - before
        return when {
            d >= 10 -> listOf(
                "Missão cumprida! $subject saiu de $before% pra $after%. Que virada!",
                "$subject subiu $d pontos! Era ponto fraco, agora é ponto de virada.",
                "De $before% pra $after% em $subject. Eu sabia que você conseguia!",
            ).pick(seed)
            d > 0 -> listOf(
                "Missão feita! $subject subiu de $before% pra $after%. Cada ponto conta.",
                "$subject melhorou $d pontos. Mais uma rodada e decola.",
                "Subiu! $subject está em $after% agora. Bora manter o treino.",
            ).pick(seed)
            d == 0 -> listOf(
                "Missão feita! $subject segurou em $after%. Mais uma rodada destrava.",
                "Treinou $subject e ficou firme em $after%. Os erros já estão no caderno.",
            ).pick(seed)
            else -> listOf(
                "Missão feita. $subject oscilou pra $after%, normal em assunto difícil. Bora ler a teoria?",
                "$subject caiu um pouco, mas agora você sabe exatamente onde errou. Isso é progresso.",
            ).pick(seed)
        }
    }

    // ------------------------------------------------------------ toques no Folha

    val tap = listOf(
        "Ei! Isso faz cócegas!", "Hehe, de novo!", "Tô aqui, firme e forte!", "Bora mais um tópico?", "Você tá indo bem, viu?",
        "Psiu… revisar amanhã fixa o dobro.", "Já bebeu água hoje?", "Cada questão é um passo pra posse!", "Eu acredito em você. Muito!",
        "Ai, minhas páginas!", "Folha, ao seu dispor!", "Um tópico de cada vez, combinado?", "Errar aqui é acertar na prova.",
        "Tô torcendo por você!", "Quer ver um giro? Toca de novo!", "Uma pausa curtinha também é estudo.", "Hoje a gente vence o edital um pouquinho.",
        "Sabia que eu sou feito de páginas de aprovados?", "Cutucou, estudou! É a regra.", "Opa! Quase perdi o marcador.",
        "Eu leio rápido, mas você entende melhor.", "Postura! Coluna reta ajuda a concentrar.", "Respira fundo. Pronto, agora vai.",
        "Toca mais uma vez e eu conto uma curiosidade… brincadeira, estuda!", "Tô de olho no seu progresso. Tá lindo.",
        "Meu marcador favorito é o do tópico concluído.", "Psiu, a revisão de hoje já está te esperando.",
        "Sabe o que combina com você? Uma posse.", "Cada clique seu me deixa mais animado!", "Shhh… tô memorizando o edital.",
        "Eu não canso. Mas você pode descansar, viu?", "Hoje eu acordei com cheiro de aprovação.", "Me cutuca depois de uma bateria de questões!",
        "Já fez alongamento hoje? O corpo agradece.", "Um gole de água e mais um tópico. Combinado?", "Folha pronto pra próxima missão!",
    )
    val tapMorning = listOf("Bom dia! Já tomou café?", "Cedinho assim? Orgulho!", "Manhã é a melhor hora pra memória.", "Bom dia! Hoje rende.", "Acordou estudando? Isso é foco!")
    val tapNight = listOf("Tá tarde, hein? Mais um e cama!", "Estudo da noite também conta.", "Não esquece de dormir, tá? A memória agradece.", "Coruja estudiosa detectada!", "Último tópico e boa noite?")
    val pet = listOf("Ahh, que carinho bom!", "Hehe, faz de novo!", "Você é demais!", "Fiquei vermelhinho…", "Agora eu estudo até de cabeça pra baixo!", "Carinho recarrega minhas páginas!", "Assim eu fico mal-acostumado…", "Melhor dupla de estudos, né?")
    val back = listOf("Voltou! Senti sua falta.", "Ei, que bom te ver!", "Bora continuar de onde paramos?", "Eu guardei a página pra você!", "Olha quem chegou! Bora?", "Tava te esperando. Vamos?", "Que bom que voltou. O edital também sentiu falta.")
}
