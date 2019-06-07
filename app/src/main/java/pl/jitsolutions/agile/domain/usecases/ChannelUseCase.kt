package pl.jitsolutions.agile.domain.usecases

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import pl.jitsolutions.agile.domain.Response

abstract class ChannelUseCase<Params, Result>(private val dispatcher: CoroutineDispatcher) {

    protected abstract suspend fun ProducerScope<Response<Result>>.build(params: Params)

    fun execute(params: Params): ReceiveChannel<Response<Result>> =
        CoroutineScope(dispatcher).produce { build(params) }

    abstract fun dispose()
}