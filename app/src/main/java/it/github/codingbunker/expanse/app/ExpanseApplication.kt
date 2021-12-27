package it.github.codingbunker.expanse.app

import android.app.Application
import it.github.codingbunker.expanse.app.di.appModule
import it.github.codingbunker.tbs.common.di.initKoin
import org.koin.android.ext.koin.androidContext

class ExpanseApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin(
            enableNetworkLogs = true,
            baseUrl = ""
        ) {
            androidContext(this@ExpanseApplication)
            modules(appModule)
        }
    }
}