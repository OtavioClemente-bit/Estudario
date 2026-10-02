package br.com.estudario.data

import br.com.estudario.BuildConfig
import br.com.estudario.data.local.AppDatabase
import br.com.estudario.data.local.Difficulty
import br.com.estudario.data.local.QuestionEntity
import br.com.estudario.data.local.QuestionOptionEntity
import br.com.estudario.data.local.SnippetKind
import br.com.estudario.data.local.TheoryMarkEntity
import br.com.estudario.data.local.TopicSnippetEntity
import br.com.estudario.data.local.UserNoteEntity

/**
 * Só nas versões de desenvolvimento (debug e "Estudário Teste"): carrega a demonstração e alguns
 * itens salvos, para testar Caderno, treino e simulado sem precisar gerar nada. Disparado por
 * `adb shell am start -n br.com.estudario.teste/br.com.estudario.MainActivity --ez seedDemo true`.
 */
object DebugSeed {
    suspend fun run(db: AppDatabase, repository: StudyRepository) {
        if (!BuildConfig.DEBUG) return
        repository.loadDemoData()
        val dao = db.dao()
        seedActivity(db)
        var theory = dao.theoriesOnce().firstOrNull { it.externalId == "demo-teoria-hash" } ?: return
        // Um capítulo de exemplo com gráficos e fórmulas, para ver o leitor desenhando tudo.
        if (!theory.markdown.contains("```grafico")) {
            theory = theory.copy(markdown = CHART_DEMO + "\n\n" + theory.markdown)
            dao.updateTheory(theory)
        }
        if (!theory.markdown.contains("\"geometria\"")) {
            theory = theory.copy(markdown = GEOMETRY_DEMO + "\n\n" + theory.markdown)
            dao.updateTheory(theory)
        }
        if (dao.theoryMarksOnce().any { it.theoryId == theory.id }) return
        val topicId = theory.topicId
        dao.insertTheoryMark(TheoryMarkEntity(theoryId = theory.id, blockIndex = 2, quote = theory.markdown.split("\n\n").getOrElse(2) { theory.title }, note = "Cai muito: efeito avalanche."))
        dao.insertTheoryMark(TheoryMarkEntity(theoryId = theory.id, blockIndex = 4, quote = theory.markdown.split("\n\n").getOrElse(4) { theory.title }, note = ""))
        listOf(
            "Em relação às funções hash criptográficas, a resistência à colisão garante que é computacionalmente inviável encontrar duas entradas distintas com o mesmo resumo." to "C",
            "Uma função hash pode ser revertida com a chave correta, recuperando a mensagem original." to "E",
        ).forEachIndexed { index, (statement, correct) ->
            val id = dao.insertQuestion(QuestionEntity(topicId = topicId, externalId = "debug-q-$index", board = "Cebraspe", year = 2024, difficulty = Difficulty.MEDIA, statement = statement, explanation = "Hash não cifra.", isFavorite = true))
            dao.insertOptions(listOf(QuestionOptionEntity(questionId = id, key = "C", text = "Certo", isCorrect = correct == "C", position = 0), QuestionOptionEntity(questionId = id, key = "E", text = "Errado", isCorrect = correct == "E", position = 1)))
        }
        dao.insertSnippet(TopicSnippetEntity(topicId = topicId, kind = SnippetKind.BIZU, text = "Hash garante integridade, não confidencialidade.", isFavorite = true))
        dao.insertSnippet(TopicSnippetEntity(topicId = topicId, kind = SnippetKind.PEGADINHA, text = "\"Descriptografar o hash\" não existe: a banca troca hash por cifra.", isFavorite = true))
        dao.insertSnippet(TopicSnippetEntity(topicId = topicId, kind = SnippetKind.RECUPERACAO, text = "O que é efeito avalanche?", answer = "Mudar um bit da entrada muda muito a saída.", isFavorite = true, externalId = "flashcard:debug:1"))
        dao.insertNote(UserNoteEntity(text = "Revisar SHA-256 x MD5 antes da prova.", topicId = topicId))
    }

    private val GEOMETRY_DEMO = """
## Figuras (demonstração)

```grafico
{"tipo":"geometria","titulo":"Teorema de Pitágoras","pontos":[{"nome":"A","x":0,"y":0},{"nome":"B","x":4,"y":0},{"nome":"C","x":0,"y":3}],"poligonos":[["A","B","C"]],"segmentos":[{"de":"A","ate":"B","rotulo":"b = 4"},{"de":"A","ate":"C","rotulo":"c = 3"},{"de":"B","ate":"C","rotulo":"a = 5"}],"angulos":[{"vertice":"A","de":"B","ate":"C","reto":true},{"vertice":"B","de":"C","ate":"A","rotulo":"θ"}],"legenda":"a² = b² + c², então 5² = 4² + 3²"}
```

```grafico
{"tipo":"geometria","titulo":"Plano inclinado","pontos":[{"nome":"O","x":0,"y":0},{"nome":"P","x":6,"y":0},{"nome":"Q","x":6,"y":3},{"nome":"M","x":4,"y":2},{"nome":"Pe","x":4,"y":0.4},{"nome":"N","x":3.4,"y":3.2}],"nomesDosPontos":false,"poligonos":[["O","P","Q"]],"vetores":[{"de":"M","ate":"Pe","rotulo":"P"},{"de":"M","ate":"N","rotulo":"N"}],"angulos":[{"vertice":"O","de":"P","ate":"Q","rotulo":"α"}]}
```
""".trimIndent()

    private val CHART_DEMO = """
## Exemplo visual (demonstração)

Juros compostos crescem mais rápido que juros simples porque o juro de cada mês entra na base do mês seguinte: ${'$'}${'$'}M = C(1 + i)^t${'$'}${'$'}

```grafico
{"tipo":"funcao","titulo":"R$ 1.000 a 10% ao mês","funcoes":[{"expr":"1000*(1+0.1)^x","nome":"Compostos"},{"expr":"1000*(1+0.1x)","nome":"Simples"}],"xmin":0,"xmax":12,"pontos":[{"x":12,"y":3138.43,"rotulo":"R$ 3.138"}]}
```

```grafico
{"tipo":"pizza","titulo":"Peso das matérias na prova","itens":[{"rotulo":"Português","valor":30},{"rotulo":"Direito","valor":40},{"rotulo":"Raciocínio lógico","valor":20},{"rotulo":"Informática","valor":10}],"legenda":"dados ilustrativos"}
```

```grafico
{"tipo":"barras","titulo":"Acerto médio por banca","unidade":"%","itens":[{"rotulo":"Cebraspe","valor":58},{"rotulo":"FGV","valor":63},{"rotulo":"FCC","valor":71}],"legenda":"dados ilustrativos"}
```
""".trimIndent()

    /** Duas semanas de respostas e sessões, para os gráficos de Desempenho terem o que mostrar. */
    private suspend fun seedActivity(db: AppDatabase) {
        val dao = db.dao()
        if (dao.attemptsOnce().size >= 20) return
        val questions = dao.questionsPage(50, 0)
        if (questions.isEmpty()) return
        val day = 86_400_000L
        val now = System.currentTimeMillis()
        val random = java.util.Random(7)
        for (back in 13 downTo 0) {
            if (back in setOf(3, 8, 11)) continue // dias sem estudo, para o gráfico ter buracos reais
            val base = now - back * day - 2 * 3_600_000L
            val answers = 4 + random.nextInt(8)
            // Acerto melhora ao longo das duas semanas.
            val chance = 0.45 + (13 - back) * 0.03
            repeat(answers) { index ->
                val row = questions[(back * 7 + index) % questions.size]
                val right = random.nextDouble() < chance
                val key = row.options.firstOrNull { it.isCorrect == right }?.key ?: row.options.first().key
                dao.insertAttempt(br.com.estudario.data.local.QuestionAttemptEntity(questionId = row.question.id, selectedKey = key, correct = right, answeredAt = base + index * 60_000L))
            }
            dao.insertStudySession(br.com.estudario.data.local.StudySessionEntity(topicId = questions.first().question.topicId, startedAt = base - 3_600_000L, completedAt = base, durationSeconds = (20L + random.nextInt(70)) * 60L))
        }
    }
}
