package it.github.codingbunker.expanse.app.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import it.github.codingbunker.expanse.app.R
import it.github.codingbunker.expanse.app.ui.theme.BlueDiscord
import it.github.codingbunker.expanse.app.ui.theme.CodingBunkerAppTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginPage() {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (logo, button) = createRefs()

        Image(
            modifier = Modifier.constrainAs(logo) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(button.top)
            },
            painter = painterResource(R.drawable.ic_launcher_foreground),
            contentDescription = null
        )

        Button(
            modifier = Modifier.constrainAs(button) {
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }.padding(bottom = 64.dp),
            onClick = {

            },
            colors = ButtonDefaults.buttonColors(backgroundColor = BlueDiscord),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    modifier = Modifier.width(24.dp),
                    painter = painterResource(R.drawable.ic_discord_logo_white),
                    contentDescription = null
                )
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = stringResource(R.string.discord_login)
                )
            }
        }

    }
}

@Composable
@Preview(
    showBackground = true,
//    showSystemUi = true,
)
fun LoginPagePreview() {
    CodingBunkerAppTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            LoginPage()
        }
    }
}