package pl.jitsolutions.agile.domain.usecases

import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.channels.ProducerScope
import kotlinx.coroutines.experimental.channels.consumeEach
import pl.jitsolutions.agile.domain.Daily
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.repository.DailyRepository

class ObserveDailyUseCase(
    private val dailyRepository: DailyRepository,
    dispatcher: CoroutineDispatcher
) :
    ChannelUseCase<ObserveDailyUseCase.Params, Daily?>(dispatcher) {

    override suspend fun ProducerScope<Response<Daily?>>.build(params: Params) {
        dailyRepository.observeDaily(params.dailyId).consumeEach {
            send(it)
        }
    }

    class Params(val dailyId: String)
}