package it.codingbunker.tbs.feature.managment.route

import io.ktor.application.*
import io.ktor.util.pipeline.*
import it.codingbunker.tbs.feature.managment.repository.BotRepository

interface BotManagmentController {

    suspend fun PipelineContext<Unit, ApplicationCall>.deleteBot(botId: String)
}

class BotManagmentControllerImpl(private val botRepository: BotRepository) : BotManagmentController {
    override suspend fun PipelineContext<Unit, ApplicationCall>.deleteBot(botId: String) {
    }
}
