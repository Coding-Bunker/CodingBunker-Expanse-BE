package it.github.codingbunker.expanse.app.ui.navigation

import androidx.navigation.NamedNavArgument

object LoginNavigation {
    val root = object : NavigationCommand {

        override val arguments: List<NamedNavArgument> = emptyList()
        override val destination: String = "login"
    }

}