package it.github.codingbunker.expanse.app.ui.navigation

import android.content.Intent
import androidx.navigation.NavHostController

class IntentNavigationController {
    private var mNavController: NavHostController? = null
    private val listenersList: MutableMap<String, IntentNavigatorDelegate> = linkedMapOf()

    fun addListener(listener: IntentNavigatorDelegate) {
        listenersList[listener.intentAction] = listener
    }

    fun setNavController(navController: NavHostController) {
        mNavController = navController
    }

    fun processIntent(intent: Intent) {
        listenersList[intent.action]
            ?.actionListener?.invoke(mNavController, intent)
    }
}

class IntentNavigatorDelegate(
    val intentAction: String,
    val actionListener: (navController: NavHostController?, intent: Intent) -> Unit
)