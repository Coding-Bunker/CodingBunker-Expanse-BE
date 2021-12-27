package it.github.codingbunker.expanse.app.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import it.github.codingbunker.expanse.app.R

@Composable
fun SplashPage() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(painterResource(id = R.drawable.ic_launcher_foreground), contentDescription = null)
    }
}