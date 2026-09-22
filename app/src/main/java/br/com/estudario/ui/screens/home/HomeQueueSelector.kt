package br.com.estudario.ui.screens.home

import br.com.estudario.data.local.QueueWithTopic
import br.com.estudario.data.local.TopicStatus

/** Retorna o tópico ativo mais próximo da frente da fila, ignorando pausados e já concluídos. */
internal fun firstEligibleQueueTopic(items: List<QueueWithTopic>): QueueWithTopic? =
    items.asSequence()
        .filterNot { it.item.paused }
        .filter { it.topic.status !in completedQueueTopicStatuses }
        .minByOrNull { it.item.position }

private val completedQueueTopicStatuses = setOf(
    TopicStatus.ESTUDADO,
    TopicStatus.REVISANDO,
    TopicStatus.DOMINADO,
)
