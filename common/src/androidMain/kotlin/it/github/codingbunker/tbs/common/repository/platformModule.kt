package it.github.codingbunker.tbs.common.repository

import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.russhwolf.settings.AndroidSettings
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ExperimentalSettingsImplementation
import io.ktor.client.engine.okhttp.*
import it.github.codingbunker.tbs.common.Constant.Session.COOKIE_STORE
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

//private val Context.cookieDataStore: DataStore<Preferences> by preferencesDataStore(Constant.Session.LOGIN_SESSION_USER)

@OptIn(ExperimentalSettingsImplementation::class, ExperimentalSettingsApi::class)
actual fun platformModule(): Module = module {

    single { OkHttp.create() }

    single(named(COOKIE_STORE)) {
        val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
        val mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)
        val encryptedSharedPreferences = EncryptedSharedPreferences.create(
            COOKIE_STORE,
            mainKeyAlias,
            get(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        AndroidSettings(encryptedSharedPreferences, true)
    }
}