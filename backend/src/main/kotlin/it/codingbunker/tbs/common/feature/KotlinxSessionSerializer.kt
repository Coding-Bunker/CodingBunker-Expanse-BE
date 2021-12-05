package it.codingbunker.tbs.common.feature

import io.ktor.sessions.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.StringFormat

fun <T> KSerializer<T>.toSessionSerializer(format: StringFormat) = object : SessionSerializer<T> {
    override fun deserialize(text: String): T = format.decodeFromString(this@toSessionSerializer, text)
    override fun serialize(session: T): String = format.encodeToString(this@toSessionSerializer, session)
}

inline fun <reified T : Any> Sessions.Configuration.cookieX(
    name: String,
    serializer: KSerializer<T>,
    format: StringFormat,
    storage: SessionStorage,
    block: CookieConfiguration.() -> Unit
) {
    val builder = CookieConfiguration().apply(block)

    register(
        provider = SessionProvider(
            name = name, type = T::class,
            tracker = SessionTrackerById(
                T::class,
                serializer = serializer.toSessionSerializer(format),
                storage = storage
            ) { generateSessionId() },
            transport = SessionTransportCookie(name, configuration = builder, transformers = emptyList())
        )
    )
}
