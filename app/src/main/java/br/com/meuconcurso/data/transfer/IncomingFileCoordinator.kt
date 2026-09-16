package br.com.meuconcurso.data.transfer

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class IncomingPlanFile(val raw: String, val token: Long = System.nanoTime())

class IncomingFileCoordinator {
    private val _pendingPlan = MutableStateFlow<IncomingPlanFile?>(null)
    val pendingPlan = _pendingPlan.asStateFlow()

    fun publishPlan(raw: String) {
        _pendingPlan.value = IncomingPlanFile(raw)
    }

    fun consumePlan(token: Long) {
        if (_pendingPlan.value?.token == token) _pendingPlan.value = null
    }
}
