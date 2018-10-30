package pl.jitsolutions.agile.domain.usecases

import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.channels.ProducerScope
import kotlinx.coroutines.experimental.channels.ReceiveChannel
import kotlinx.coroutines.experimental.channels.produce
import pl.jitsolutions.agile.domain.Response

abstract class ChannelUseCase<Params, Result>(private val dispatcher: CoroutineDispatcher) {

    protected abstract suspend fun ProducerScope<Response<Result>>.build(params: Params)

    fun execute(params: Params): ReceiveChannel<Response<Result>> =
        CoroutineScope(dispatcher).produce { build(params) }
}