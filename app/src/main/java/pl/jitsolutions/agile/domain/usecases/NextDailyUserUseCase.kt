package pl.jitsolutions.agile.domain.usecases

import kotlinx.coroutines.CoroutineDispatcher
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.repository.DailyRepository

class NextDailyUserUseCase(
    private val dailyRepository: DailyRepository,
    dispatcher: CoroutineDispatcher
) : UseCase<NextDailyUserUseCase.Params, Unit>(dispatcher) {

    override suspend fun build(params: Params): Response<Unit> {
        return dailyRepository.nextDailyUser(params.dailyId)
    }

    data class Params(val dailyId: String)
}