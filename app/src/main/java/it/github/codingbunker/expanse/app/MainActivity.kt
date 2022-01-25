package it.github.codingbunker.expanse.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import it.github.codingbunker.expanse.app.ui.navigation.IntentNavigationController
import it.github.codingbunker.expanse.app.ui.navigation.NavigationController
import it.github.codingbunker.expanse.app.ui.theme.CodingBunkerAppTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val intentNavigationController: IntentNavigationController by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CodingBunkerAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background, modifier = Modifier.fillMaxSize()) {
                    NavigationController(intentNavigationController)
                }
            }
        }
    }


    override fun onNewIntent(intent: Intent?) {
        intent?.let { intentNavigationController.processIntent(it) }
    }
}