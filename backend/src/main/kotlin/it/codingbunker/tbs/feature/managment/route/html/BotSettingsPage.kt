package it.codingbunker.tbs.feature.managment.route.html

import io.ktor.application.*
import io.ktor.html.*
import io.ktor.locations.*
import it.codingbunker.tbs.common.html.page.baseHtmlBody
import it.codingbunker.tbs.common.html.page.bulmaHead
import it.codingbunker.tbs.common.html.page.ionicIcon
import it.codingbunker.tbs.feature.managment.route.BotManagmentRoute
import it.codingbunker.tbs.feature.managment.route.html.BotSettingsPageConstant.FORM_BOT_NAME_ID
import it.codingbunker.tbs.feature.managment.route.html.BotSettingsPageConstant.FORM_BOT_PERMISSION_ID
import it.codingbunker.tbs.feature.managment.table.BotDTO
import it.codingbunker.tbs.feature.managment.table.RoleType
import kotlinx.html.*
import java.util.*

object BotSettingsPageConstant {
    const val FORM_BOT_NAME_ID = "FORM_BOT_NAME_ID"
    const val FORM_BOT_PERMISSION_ID = "FORM_BOT_PERMISSION_ID"
}

suspend fun ApplicationCall.getAllBotHtmlPage(botList: List<BotDTO>) {
    respondHtml {
        bulmaHead {
            title("Bot Registrati")
        }

        baseHtmlBody {
            div {
                classes = setOf("columns")
                div {
                    classes = setOf("column", "is-one-fifth")
                    button {
                        classes = setOf("button")
                        a {
                            href = application.locations.href(
                                BotManagmentRoute.Settings.Create(
                                    BotManagmentRoute.Settings(
                                        BotManagmentRoute()
                                    )
                                )
                            )
                            text("Aggiungi un nuovo bot".uppercase())
                        }
                    }
                }
            }

            div {
                classes = setOf("table-container")
                table {
                    classes = setOf("table")
                    thead {
                        tr {
                            th {
                                +BotDTO::id.name
                            }
                            th {
                                +BotDTO::botKey.name
                            }
                            th {
                                +BotDTO::botName.name
                            }
                            th {
                                +BotDTO::botDateCreation.name
                            }
                            th {
                                +BotDTO::botRoles.name
                            }
                        }
                    }

                    tbody {
                        botList.forEach {
                            tr {
                                th {
                                    +it.id
                                }
                                th {
                                    +it.botKey
                                }
                                th {
                                    +it.botName
                                }
                                th {
                                    +it.botDateCreation.toString()
                                }
                                th {
                                    +it.botRoles.joinToString(separator = ", ")
                                }
                                th {
                                    button {
                                        classes = setOf("button")
                                        a {
                                            href = application.locations.href(
                                                BotManagmentRoute.Settings(BotManagmentRoute())
                                            )

                                            ionicIcon {
                                                name("trash")
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

suspend fun ApplicationCall.getConfigureNewBotHtmlPage() {
    val title = "Configura il bot"
    respondHtml {
        bulmaHead {
            title(title)
        }

        body {
            h1 {
                classes = setOf("title", "is-1")
                text(title)
            }

            form {
                autoComplete = false
                method = FormMethod.post

                input {
                    name = FORM_BOT_NAME_ID
                    classes = setOf("input", "block")
                    type = InputType.text
                    placeholder = "Nome Bot"
                }
                br()
                div {
                    classes = setOf("control", "block")
                    label {
                        classes = setOf("radio")
                        radioInput {
                            name = FORM_BOT_PERMISSION_ID
                            value = RoleType.BOT_DISCORD.ordinal.toString()
                            id = RoleType.BOT_DISCORD.ordinal.toString()
                        }
                        text(RoleType.BOT_DISCORD.name)
                    }
                    br()
                    label {
                        classes = setOf("radio")
                        radioInput {
                            name = FORM_BOT_PERMISSION_ID
                            value = RoleType.BOT_TWITCH.ordinal.toString()
                            id = RoleType.BOT_TWITCH.ordinal.toString()
                        }
                        text(RoleType.BOT_TWITCH.name)
                    }
                }
                br()
                button {
                    classes = setOf("button")
                    type = ButtonType.submit
                    text("Crea")
                }
            }
        }
    }
}

suspend fun ApplicationCall.showBotConfigurationResultHtmlPage(botDTO: BotDTO) {
    respondHtml {
        bulmaHead {

        }
        body {
            section {
                classes = setOf("hero", "is-success", "is-medium")
                div {
                    classes = setOf("content")
                    p {
                        classes = setOf("title")
                        text("Usa questo token per loggare con il bot.")
                    }
                    p {
                        style = "user-select: all;"
                        classes = setOf("subtitle", "container")
                        b {
                            text(Base64.getEncoder().encodeToString("${botDTO.id}:${botDTO.botKey}".toByteArray()))
                        }
                    }
                }
            }

            button {
                classes = setOf("button")
                a {
                    href = application.locations.href(
                        BotManagmentRoute.Settings(BotManagmentRoute())
                    )
                    text("Torna alla lista bot")
                }
            }

        }
    }
}
