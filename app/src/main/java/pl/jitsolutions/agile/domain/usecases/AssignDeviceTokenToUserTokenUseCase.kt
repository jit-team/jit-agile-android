package pl.jitsolutions.agile.domain.usecases

import kotlinx.coroutines.CoroutineDispatcher
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.repository.NotificationRepository

class AssignDeviceTokenToUserTokenUseCase(
    private val notificationRepository: NotificationRepository,
    dispatcher: CoroutineDispatcher
) :
    UseCase<AssignDeviceTokenToUserTokenUseCase.Params, Unit>(dispatcher) {

    override suspend fun build(params: Params): Response<Unit> {
        return notificationRepository.assignDeviceTokenToUser(params.userId, params.token)
    }

    data class Params(val userId: String, val token: String)
}