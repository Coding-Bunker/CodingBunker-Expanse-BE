package it.github.codingbunker.tbs.common.repository

import co.touchlab.kermit.Logger
import com.github.kittinunf.result.Result
import com.github.kittinunf.result.onFailure
import it.github.codingbunker.tbs.common.model.LoginRouteDto
import it.github.codingbunker.tbs.common.remote.ExpanseApi
import org.koin.core.component.KoinComponent

interface ExpanseRepository {
    suspend fun fetchLoginMethod(): Result<List<LoginRouteDto>, Exception>
}

class ExpanseRepositoryImpl(
    private val expaseApi: ExpanseApi,
    private val cookieRepository: CookieRepository
) : KoinComponent, ExpanseRepository {
//    @NativeCoroutineScope
//    private val coroutineScope: CoroutineScope = MainScope()

    private val logger = Logger.withTag("ExpanseRepository")

    override suspend fun fetchLoginMethod(): Result<List<LoginRouteDto>, Exception> {
        return Result.of<List<LoginRouteDto>, Exception> {
            expaseApi.getAvailableLogin()
        }.onFailure {
            logger.e("Error fetchLoginMethod", it)
        }
    }

    fun onCookieReceive(cookieKey: String) {
        cookieRepository.saveCookie(cookieKey)
    }
}