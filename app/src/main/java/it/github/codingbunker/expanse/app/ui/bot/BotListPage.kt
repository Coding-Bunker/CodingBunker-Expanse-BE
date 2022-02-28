package it.github.codingbunker.expanse.app.ui.bot

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import it.github.codingbunker.expanse.app.ui.theme.CodingBunkerAppTheme
import it.github.codingbunker.expanse.app.viewmodel.bot.BotListViewModel
import org.koin.androidx.compose.getViewModel

private val ID_BOT_WIDTH = 44.dp
private val CELL_WIDTH = 150.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BotListPage(botListViewModel: BotListViewModel = getViewModel()) {
    val uiState by botListViewModel.uiState.collectAsState()

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (botListRef, refreshButtonRef) = createRefs()

        IconButton(
            modifier = Modifier.constrainAs(refreshButtonRef) {
                top.linkTo(parent.top)
                end.linkTo(parent.end)
            },
            onClick = {
                if (!uiState.loading) {
                    botListViewModel.getBotList(refresh = true)
                }
            }
        ) {
            if (uiState.loading) {
                CircularProgressIndicator()
            } else {
                Icon(imageVector = Icons.Filled.Refresh, contentDescription = "Refresh Table")
            }
        }


        LazyColumn(
            Modifier.constrainAs(botListRef) {
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
                top.linkTo(refreshButtonRef.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            }.horizontalScroll(rememberScrollState())
        ) {
            stickyHeader {
                BotDetailRowHeader()
            }

            for (a in 0..10) {
                item {
                    Spacer(modifier = Modifier.height(10.dp))
                    BotDetailRow()
                }
            }
        }
    }
}

@Composable
fun BotDetailRowHeader() {
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.Start)) {
        CellItem(
            width = ID_BOT_WIDTH,
            text = "Test"
        )

        CellItem(
            text = "Test 2"
        )
    }
}

@Composable
fun BotDetailRow() {
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.Start)) {
        CellItem(
            width = ID_BOT_WIDTH,
            text = "Test 2"
        )

        CellItem(
            text = "Test 2"
        )
    }
}

@Composable
private fun CellItem(
    width: Dp = CELL_WIDTH,
    text: String
) {
    Text(
        modifier = Modifier.width(width)
            .horizontalScroll(rememberScrollState()),
        maxLines = 1,
        text = text
    )
}

@Preview(showBackground = true)
@Composable
fun BotListPagePreview() {
    CodingBunkerAppTheme {
        BotListPage()
    }
}