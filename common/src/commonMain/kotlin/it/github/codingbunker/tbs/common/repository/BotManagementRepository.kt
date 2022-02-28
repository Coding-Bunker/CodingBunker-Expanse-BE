package it.github.codingbunker.tbs.common.repository

import it.github.codingbunker.tbs.common.model.BotDTO
import it.github.codingbunker.tbs.common.remote.ExpanseApi
import org.koin.core.component.KoinComponent

interface BotManagementRepository {
    suspend fun getAllBots(refresh: Boolean = false): Map<String, BotDTO>
    suspend fun clearCacheBots()
}

class BotManagementRepositoryImpl(
    private val expaseApi: ExpanseApi
) : KoinComponent, BotManagementRepository {
    private val botsCache = mutableMapOf<String, BotDTO>()

    override suspend fun getAllBots(refresh: Boolean): Map<String, BotDTO> = if (botsCache.isEmpty() || refresh) {
        expaseApi.getAllBots().run {
            botsCache.putAll(
                associateBy { bot ->
                    bot.id
                }
            )
            botsCache
        }
    } else {
        botsCache
    }

    override suspend fun clearCacheBots() {
        botsCache.clear()
    }
}