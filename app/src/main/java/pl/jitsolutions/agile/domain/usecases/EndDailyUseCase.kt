package pl.jitsolutions.agile.domain.usecases

import kotlinx.coroutines.experimental.CoroutineDispatcher
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.repository.DailyRepository

class EndDailyUseCase(
    private val dailyRepository: DailyRepository,
    dispatcher: CoroutineDispatcher
) : UseCase<EndDailyUseCase.Params, Unit>(dispatcher) {

    override suspend fun build(params: Params): Response<Unit> {
        return dailyRepository.endDaily(params.dailyId)
    }

    data class Params(val dailyId: String)
}