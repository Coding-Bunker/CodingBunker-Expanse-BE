package it.github.codingbunker.tbs.common.repository

import it.github.codingbunker.tbs.common.model.BotDTO
import it.github.codingbunker.tbs.common.remote.ExpanseApi
import org.koin.core.component.KoinComponent

interface BotManagementRepository {
    suspend fun getAllBots(): List<BotDTO>
}

class BotManagementRepositoryImpl(
    private val expaseApi: ExpanseApi
) : KoinComponent, BotManagementRepository {
    private val botsCache = mutableMapOf<String, BotDTO>()

    override suspend fun getAllBots(): List<BotDTO> = expaseApi.getAllBots().also {
        it.associateBy { bot ->
            bot.id
        }
    }
}