package pl.jitsolutions.agile.domain.usecases

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.consumeEach
import pl.jitsolutions.agile.domain.Daily
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.repository.DailyRepository

class ObserveDailyUseCase(
    private val dailyRepository: DailyRepository,
    dispatcher: CoroutineDispatcher
) : ChannelUseCase<ObserveDailyUseCase.Params, Daily?>(dispatcher) {

    override suspend fun ProducerScope<Response<Daily?>>.build(params: Params) {
        dailyRepository.observeDaily(params.dailyId)
            .consumeEach { send(it) }
    }

    override fun dispose() {
        dailyRepository.dispose()
    }

    data class Params(val dailyId: String)
}