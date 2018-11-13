package pl.jitsolutions.agile.domain.usecases

import kotlinx.coroutines.experimental.CoroutineDispatcher
import pl.jitsolutions.agile.domain.Daily
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.repository.DailyRepository

class GetDailyUseCase(
    private val dailyRepository: DailyRepository,
    dispatcher: CoroutineDispatcher
) : UseCase<GetDailyUseCase.Params, Daily>(dispatcher) {

    override suspend fun build(params: Params): Response<Daily> {
        return dailyRepository.getDaily(params.dailyId)
    }

    data class Params(val dailyId: String)
}