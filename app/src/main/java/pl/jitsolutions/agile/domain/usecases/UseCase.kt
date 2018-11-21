package pl.jitsolutions.agile.domain.usecases

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import pl.jitsolutions.agile.domain.Response

abstract class UseCase<Params, Result>(private val dispatcher: CoroutineDispatcher) {

    protected abstract suspend fun build(params: Params): Response<Result>

    fun executeAsync(params: Params) = CoroutineScope(dispatcher).async {
        build(params)
    }
}