package pl.jitsolutions.agile.utils

import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.channels.ReceiveChannel
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.withContext
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.UseCase


// Maybe add as a UseCase method
suspend fun <Params, Result> UseCase<Params, Result>.execute(
        params: Params,
        mainDispatcher: CoroutineDispatcher,
        action: (Response<Result>) -> Unit
): ReceiveChannel<Response<Result>> {
    with(execute(params)) {
        consumeEach { response ->
            withContext(mainDispatcher) {
                action.invoke(response)
            }
        }
        return this
    }
}