package br.com.estudario.domain

import br.com.estudario.data.local.ReviewScheduleEntity

fun ReviewScheduleEntity.toStageState() = ReviewStageState(id, stage, dueAt, completedAt, ignoredAt, intervalDays)
