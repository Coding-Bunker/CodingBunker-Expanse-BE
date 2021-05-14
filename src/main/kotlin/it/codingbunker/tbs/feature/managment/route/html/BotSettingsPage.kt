package it.codingbunker.tbs.feature.managment.route.html

import io.ktor.application.*
import io.ktor.html.*
import it.codingbunker.tbs.feature.managment.table.BotDTO
import kotlinx.html.*
import kotlin.reflect.full.primaryConstructor

suspend fun ApplicationCall.getAllBotHtmlPage(botList: List<BotDTO>) {
    respondHtml {
        head {
            title { +"Bot Registrati" }
        }

        body {
            a {
                href = "abc"

                text("Aggiungi un nuovo bot".toUpperCase())

            }

            table {
                style = """
                    width:100%;
                """.trimIndent()
                thead {
                    tr {
                        BotDTO::class.primaryConstructor?.parameters?.forEachIndexed { i, property ->
                            th {
                                +property.name.orEmpty().capitalize()
                            }
                        }
                    }
                }
            }
        }
    }
}