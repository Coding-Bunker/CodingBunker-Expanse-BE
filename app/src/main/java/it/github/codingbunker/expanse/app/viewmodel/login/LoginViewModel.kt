package it.github.codingbunker.expanse.app.viewmodel.login

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.github.kittinunf.result.Result
import it.github.codingbunker.tbs.common.Constant.Url.SERVER_URL_ENDPOINT
import it.github.codingbunker.tbs.common.model.LoginRouteDto
import it.github.codingbunker.tbs.common.repository.LoginRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class LoginViewModel(
    private val loginRepository: LoginRepository,
    private val coroutineDispatcher: CoroutineContext
) : ViewModel() {

    private val logger = Logger.withTag("LoginViewModel")

    private var mUiState = MutableStateFlow(LoginModel())
    var uiState: StateFlow<LoginModel> = mUiState

    fun fetchLogin() {
        viewModelScope.launch(coroutineDispatcher) {
            try {
                val loginMethodResult = loginRepository.fetchLoginMethod()
                mUiState.update {
                    it.copy(showLoading = false, loginMethodList = loginMethodResult)
                }
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