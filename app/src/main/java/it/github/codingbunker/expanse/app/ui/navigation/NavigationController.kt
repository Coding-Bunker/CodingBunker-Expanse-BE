package it.github.codingbunker.expanse.app.ui.navigation

import android.app.Activity
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import it.github.codingbunker.expanse.app.ui.bot.BotListPage
import it.github.codingbunker.expanse.app.ui.login.LoginPage
import it.github.codingbunker.expanse.app.viewmodel.login.LoginViewModel
import it.github.codingbunker.tbs.common.Constant.Session.LOGIN_SESSION_USER
import it.github.codingbunker.tbs.common.repository.CookieRepository
import org.koin.androidx.compose.getViewModel
import org.koin.androidx.compose.inject

@Composable
fun NavigationController(intentNavigationController: IntentNavigationController) {
    val navController = rememberNavController().also {
        intentNavigationController.apply {
            val cookieRepository by inject<CookieRepository>()
            setNavController(it)
            addActionViewReceiver(cookieRepository)
        }
    }

    NavHost(navController, startDestination = BotNavigation.root.destination) {
        composable(LoginNavigation.root.destination) {
            val loginViewModel: LoginViewModel = getViewModel()
            val intent = (LocalContext.current as Activity).intent
            LaunchedEffect(intent) {
                intent.data
            }

            loginViewModel.fetchLogin()
            LoginPage(loginViewModel)
        }

        composable(BotNavigation.root.destination) {
            BotListPage()
        }
    }
}

private fun IntentNavigationController.addActionViewReceiver(cookieRepository: CookieRepository) {
    addListener(
        IntentNavigatorDelegate(Intent.ACTION_VIEW) { navController, intent ->
            when (intent.data?.path) {
                "/login/success" -> {
                    cookieRepository.cookie = intent.data?.getQueryParameter(LOGIN_SESSION_USER)
//                data.query?.find(Constant.Session.LOGIN_SESSION_USER)
                }
            }
        }
    )
}