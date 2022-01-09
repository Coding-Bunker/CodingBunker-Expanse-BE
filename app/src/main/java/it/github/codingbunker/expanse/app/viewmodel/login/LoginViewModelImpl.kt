package it.github.codingbunker.expanse.app.viewmodel.login

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.github.kittinunf.result.Result
import it.github.codingbunker.tbs.common.Constant.Url.SERVER_URL_ENDPOINT
import it.github.codingbunker.tbs.common.model.LoginRouteDto
import it.github.codingbunker.tbs.common.repository.ExpanseRepository
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


//interface LoginViewModel {
//    var uiState: LoginModel
//
//    fun fetchLogin()
//
//    fun openChromeTab(context: Context, url: String)
//}

class LoginViewModelImpl(
    private val expanseRepository: ExpanseRepository,
    private val coroutineDispatcher: CoroutineContext
) : ViewModel() {

    private val logger = Logger.withTag("LoginViewModel")

    var uiState by mutableStateOf(LoginModel())
        private set

    fun fetchLogin() {
        viewModelScope.launch(coroutineDispatcher) {
            try {
                val loginMethodResult = expanseRepository.fetchLoginMethod()
                uiState = uiState.copy(showLoading = false, loginMethodList = loginMethodResult)
            } catch (ex: Exception) {
                logger.e("FetchLoginError on LoginViewModelImpl", ex)
            }
        }
    }

    fun openChromeTab(context: Context, url: String) {
        CustomTabsIntent.Builder()
            .build().launchUrl(context, Uri.parse(SERVER_URL_ENDPOINT + url))
    }

    data class LoginModel(
        val showLoading: Boolean = true,
        val loginMethodList: Result<List<LoginRouteDto>, Exception> = Result.success(listOf())
    )
}