package br.com.estudario.web.data

import br.com.estudario.time.isoDayOfWeek
import br.com.estudario.time.startOfDay
import br.com.estudario.time.toEpochDay
import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.JsonObject
import kotlin.js.Date
import kotlin.random.Random

/**
 * Demonstração do app web (#demo): o mesmo conteúdo de exemplo do app (demo.json, exportado pelo
 * ExportDemoSnapshotTest) com um plano da semana e duas semanas de atividade, sempre com datas a
 * partir de hoje. Nada é enviado para conta nenhuma.
 */
object Demo {
    suspend fun build(): Snapshot {
        val text = window.fetch("demo.json").await().text().await()
        var data = Snapshot.parse(text)
        val today = Queries.todayDate()
        val todayEpoch = today.toEpochDay()
        val now = Date.now().toLong()
        val random = Random(7)
        fun at(epochDay: Long, hour: Int, minute: Int = 0): Long =
            LocalDate.fromEpochDays(epochDay).startOfDay(Queries.zone).toEpochMilliseconds() + hour * 3_600_000L + minute * 60_000L

        // Mais tópicos nas outras matérias, para o edital ter cara de edital.
        val extraTopics = mapOf(
            1L to listOf("Interpretação de texto", "Concordância verbal e nominal", "Crase", "Pontuação"),
            2L to listOf("Modelo relacional", "SQL: consultas e junções", "Normalização", "Transações e ACID"),
            3L to listOf("Modelo OSI e TCP/IP", "Endereçamento IPv4", "Protocolos de aplicação"),
            5L to listOf("Proposições e conectivos", "Porcentagem", "Análise combinatória"),
        )
        var topicId = data.nextId(Keys.TOPICS)
        val newTopics = mutableListOf<JsonObject>()
        extraTopics.forEach { (subjectId, titles) ->
            titles.forEachIndexed { index, title ->
                val studied = index < 2
                newTopics += jsonOf(
                    "id" to topicId++, "subjectId" to subjectId, "parentTopicId" to null, "title" to title, "description" to "", "position" to index,
                    "status" to if (studied) "ESTUDADO" else "NAO_ESTUDADO", "firstStudiedAt" to if (studied) at(todayEpoch - 10 + index, 9) else null,
                    "lastStudiedAt" to if (studied) at(todayEpoch - 10 + index, 10) else null, "lastReviewedAt" to null, "notes" to "", "priority" to "NORMAL",
                    "externalId" to null, "contentOriginType" to "EDITAL", "scopeCovers" to null, "scopeExcludes" to null,
                )
            }
        }
        data = data.appendAll(mapOf(Keys.TOPICS to newTopics))

        // Plano ativo com prova daqui a ~4 meses.
        val planId = "demo-plano"
        val weekStart = todayEpoch - (today.isoDayOfWeek - 1)
        data = data.appendAll(
            mapOf(
                Keys.PLANS to listOf(
                    jsonOf(
                        "id" to planId, "competitionId" to 1, "name" to "Plano TRT", "objective" to "Analista de TI", "start" to weekStart - 14, "exam" to todayEpoch + 118,
                        "active" to true, "master" to false, "archived" to false, "revision" to 1, "createdAt" to now, "updatedAt" to now, "profile" to "EQUILIBRADO",
                        "block" to 50, "weeklyQuestions" to 100, "topicQuestions" to 15, "simulations" to 2, "discursives" to 0, "interleave" to true, "dailyShare" to 60,
                    ),
                ),
                Keys.AVAILABILITY to (1..7).map { day -> jsonOf("planId" to planId, "day" to day, "minutes" to if (day == 7) 0 else if (day == 6) 180 else 120, "unavailable" to (day == 7), "mode" to "FIXED") },
                Keys.PLAN_SUBJECTS to data.subjects.mapIndexed { index, subject -> jsonOf("planId" to planId, "subjectId" to subject.id, "name" to subject.name, "priority" to "MEDIUM", "paused" to false, "maintenance" to 0, "weight" to null, "position" to index, "personalDifficulty" to "NORMAL", "initialKnowledge" to "NONE") },
            ),
        )
        val topics = data.topics
        val subjects = data.subjects.associateBy { it.id }
        val pattern = listOf("THEORY" to 50, "QUESTIONS" to 40, "REVIEW" to 30)
        val tasks = mutableListOf<JsonObject>()
        val executions = mutableListOf<JsonObject>()
        var topicCursor = 0
        for (offset in 0 until 13) {
            val day = weekStart + offset
            val weekday = offset % 7
            if (weekday == 6) continue
            repeat(if (weekday == 5) 3 else 2) { slot ->
                val topic = topics[(topicCursor++) % topics.size]
                val (type, minutes) = pattern[(offset + slot) % pattern.size]
                val done = day < todayEpoch || (day == todayEpoch && slot == 0)
                val id = "demo-t-$day-$slot"
                tasks += jsonOf(
                    "id" to id, "planId" to planId, "competitionId" to 1, "annualPhaseId" to null, "monthlyPlanId" to null, "weeklyPlanId" to null,
                    "subjectId" to topic.subjectId, "topicId" to topic.id, "subjectName" to (subjects[topic.subjectId]?.name ?: ""), "topicName" to topic.title,
                    "day" to day, "type" to type, "minutes" to minutes, "questions" to if (type == "QUESTIONS") 15 else 0, "priority" to "MEDIUM",
                    "status" to if (done) "CONCLUIDA" else "PLANEJADA", "origin" to "ENGINE", "notes" to "", "locked" to false, "progressNote" to "",
                    "replannedFrom" to null, "createdRevision" to 1, "updatedRevision" to 1, "createdAt" to now, "updatedAt" to now,
                )
                if (done) executions += jsonOf(
                    "id" to "demo-e-$day-$slot", "planId" to planId, "taskId" to id, "competitionId" to 1, "subjectId" to topic.subjectId, "topicId" to topic.id,
                    "startedAt" to at(day, 19 + slot), "completedAt" to at(day, 19 + slot, minutes), "minutes" to minutes, "questions" to if (type == "QUESTIONS") 15 else 0,
                    "correct" to if (type == "QUESTIONS") 9 + random.nextInt(5) else 0, "notes" to "", "difficulty" to "NORMAL", "createdAt" to at(day, 20),
                )
            }
        }
        data = data.appendAll(mapOf(Keys.TASKS to tasks, Keys.EXECUTIONS to executions))

        // Duas semanas de questões respondidas e revisões pendentes.
        val questions = data.questions
        var attemptId = data.nextId(Keys.ATTEMPTS)
        val attempts = mutableListOf<JsonObject>()
        for (back in 13 downTo 1) {
            if (back in setOf(3, 8, 11)) continue
            repeat(4 + random.nextInt(6)) { index ->
                val question = questions[(back * 3 + index) % questions.size]
                val right = random.nextDouble() < 0.5 + (13 - back) * 0.03
                val key = question.options.firstOrNull { it.correct == right }?.key ?: question.options.first().key
                attempts += jsonOf("id" to attemptId++, "questionId" to question.id, "selectedKey" to key, "correct" to right, "answeredAt" to at(todayEpoch - back, 20, index), "sessionId" to null)
            }
        }
        // Sessões de estudo nos mesmos dias, para o gráfico de minutos ter histórico.
        var sessionId = data.nextId(Keys.SESSIONS)
        val sessions = (13 downTo 1).filter { it !in setOf(3, 8, 11) }.map { back ->
            val topic = topics[back % topics.size]
            val minutes = 25 + random.nextInt(70)
            jsonOf(
                "id" to sessionId++, "topicId" to topic.id, "startedAt" to at(todayEpoch - back, 18), "completedAt" to at(todayEpoch - back, 18, minutes),
                "competitionId" to 1, "subjectId" to topic.subjectId, "durationSeconds" to minutes * 60L, "questionCount" to 0, "correctCount" to 0, "wrongCount" to 0,
                "notes" to "", "sourcePackageId" to null,
            )
        }
        data = data.appendAll(mapOf(Keys.ATTEMPTS to attempts, Keys.SESSIONS to sessions))
        var reviewId = data.nextId(Keys.REVIEWS)
        val reviews = topics.filter { it.status == "ESTUDADO" }.take(5).mapIndexed { index, topic ->
            jsonOf("id" to reviewId++, "topicId" to topic.id, "stage" to 1 + index % 2, "dueAt" to at(todayEpoch - (index % 3), 0), "completedAt" to null, "ignoredAt" to null, "perceivedDifficulty" to null, "questionCorrect" to 0, "questionTotal" to 0)
        }
        // Um baralho de flashcards no formato da revisão rápida do app (### frente, verso no corpo).
        val deck = listOf(
            "O que é efeito avalanche?" to "Mudar um único bit da entrada muda boa parte do resumo (hash) gerado.",
            "Hash garante confidencialidade?" to "Não. Hash garante **integridade**; quem esconde o conteúdo é a cifra.",
            "O que é resistência à colisão?" to "É ser inviável encontrar duas entradas diferentes com o mesmo hash.",
            "Por que usar salt ao guardar senhas?" to "Para que senhas iguais gerem hashes diferentes e ataques com tabelas prontas não funcionem.",
            "Dá para \"descriptografar\" um hash?" to "Não existe operação inversa: a banca costuma trocar hash por cifra para pegar o candidato.",
        ).joinToString("\n\n") { (front, back) -> "### $front\n\n$back" }
        val hashTopic = topics.firstOrNull { it.title == "Hash" }?.id ?: topics.first().id
        return data.appendAll(
            mapOf(
                Keys.REVIEWS to reviews,
                Keys.SUMMARIES to listOf(
                    jsonOf("id" to data.nextId(Keys.SUMMARIES), "topicId" to hashTopic, "title" to "Revisão rápida", "markdown" to deck, "favorite" to false, "ownNotes" to "", "externalId" to "demo-flash-hash", "createdAt" to now, "updatedAt" to now, "kind" to "RAPIDO"),
                ),
            ),
        )
    }
}
