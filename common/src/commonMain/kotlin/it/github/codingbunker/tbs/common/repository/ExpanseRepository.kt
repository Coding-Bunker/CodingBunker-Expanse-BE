package it.github.codingbunker.tbs.common.repository

import co.touchlab.kermit.Logger
import com.github.kittinunf.result.Result
import com.rickclephas.kmp.nativecoroutines.NativeCoroutineScope
import it.github.codingbunker.tbs.common.model.LoginRouteDto
import it.github.codingbunker.tbs.common.remote.ExpanseApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface ExpanseRepository {
    suspend fun fetchLoginMethod(): Result<List<LoginRouteDto>, Exception>
}

class ExpanseRepositoryImpl : KoinComponent, ExpanseRepository {
    private val expaseApi: ExpanseApi by inject()

    @NativeCoroutineScope
    private val coroutineScope: CoroutineScope = MainScope()

    val logger = Logger.withTag("ExpanseRepository")

    override suspend fun fetchLoginMethod(): Result<List<LoginRouteDto>, Exception> {
        return Result.of {
            expaseApi.getAvailableLogin()
        }
    }
}