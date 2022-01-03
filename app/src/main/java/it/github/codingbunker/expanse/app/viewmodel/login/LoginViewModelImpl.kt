package it.github.codingbunker.expanse.app.viewmodel.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.github.codingbunker.tbs.common.repository.ExpanseRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

interface LoginViewModel {

}

class LoginViewModelImpl(
    private val expanseRepository: ExpanseRepository,
    private val coroutineDispatcher: CoroutineDispatcher
) : ViewModel(), LoginViewModel {

    fun fetchLogin() {
        viewModelScope.launch(coroutineDispatcher) {
            expanseRepository.fetchLoginMethod()
        }
    }
}