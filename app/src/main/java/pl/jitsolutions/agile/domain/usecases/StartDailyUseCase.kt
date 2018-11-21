package pl.jitsolutions.agile.domain.usecases

import kotlinx.coroutines.CoroutineDispatcher
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.repository.DailyRepository

class StartDailyUseCase(
    private val dailyRepository: DailyRepository,
    dispatcher: CoroutineDispatcher
) : UseCase<StartDailyUseCase.Params, Unit>(dispatcher) {

    override suspend fun build(params: Params): Response<Unit> {
        return dailyRepository.startDaily(params.dailyId)
    }

    data class Params(val dailyId: String)
}