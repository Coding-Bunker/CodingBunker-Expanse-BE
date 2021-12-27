package it.github.codingbunker.expanse.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import it.github.codingbunker.expanse.app.ui.navigation.NavigationController
import it.github.codingbunker.expanse.app.ui.theme.CodingBunkerAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CodingBunkerAppTheme {


                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background, modifier = Modifier.fillMaxSize()) {
                    NavigationController()
                }
            }
        }
    }
}