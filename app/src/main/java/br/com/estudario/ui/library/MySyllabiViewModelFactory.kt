package br.com.estudario.ui.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.estudario.EstudarioApplication

class MySyllabiViewModelFactory(private val application: EstudarioApplication) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass.isAssignableFrom(MySyllabiViewModel::class.java))
        return MySyllabiViewModel(
            localSyllabi = application.repository.competitions,
            syncRows = application.database.dao().observeRemoteSyllabusSync(),
            library = DefaultMySyllabiLibrary(application.repository, application.privateSyllabusRepository),
        ) as T
    }
}
