package it.github.codingbunker.tbs.common.util

import com.github.kittinunf.result.Result

suspend fun <V, E : Exception> Result<V, E>.onFailure(action: suspend (Throwable) -> Unit): Result<V, E> {
    this.fold({}, { action.invoke(it) })
    return this
}

suspend fun <V, E : Exception> Result<V, E>.onSuccess(action: suspend (value: V) -> Unit): Result<V, E> {
    this.fold({ action.invoke(it) }, {})
    return this
}
