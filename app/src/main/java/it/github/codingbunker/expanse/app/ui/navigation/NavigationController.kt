package it.github.codingbunker.expanse.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import it.github.codingbunker.expanse.app.ui.login.LoginPage
import it.github.codingbunker.expanse.app.viewmodel.login.LoginViewModelImpl
import org.koin.androidx.compose.getViewModel

@Composable
fun NavigationController() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = LoginNavigation.root.destination) {
        composable(LoginNavigation.root.destination) {
            val loginViewModel: LoginViewModelImpl = getViewModel()
            loginViewModel.fetchLogin()
            LoginPage(loginViewModel)
        }
    }
}