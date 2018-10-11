package pl.jitsolutions.agile.domain

import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.channels.ProducerScope
import kotlinx.coroutines.experimental.channels.ReceiveChannel
import kotlinx.coroutines.experimental.channels.produce

abstract class UseCase<Params, Result>(private val dispatcher: CoroutineDispatcher) {

    abstract suspend fun ProducerScope<Result>.buildLogic(params: Params)

    fun execute(params: Params): ReceiveChannel<Result> =
            CoroutineScope(dispatcher).produce { buildLogic(params) }
}