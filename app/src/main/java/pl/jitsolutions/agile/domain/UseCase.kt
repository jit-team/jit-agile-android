package pl.jitsolutions.agile.domain

import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.async

abstract class UseCase<Params, Result>(private val dispatcher: CoroutineDispatcher) {

    protected abstract suspend fun build(params: Params): Response<Result>

    fun executeAsync(params: Params) = CoroutineScope(dispatcher).async {
        build(params)
    }
}