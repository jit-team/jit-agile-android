package pl.jitsolutions.agile.domain.usecases

import kotlinx.coroutines.CoroutineDispatcher
import pl.jitsolutions.agile.common.Error
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.errorResponse
import pl.jitsolutions.agile.domain.isSuccessfulWithData
import pl.jitsolutions.agile.repository.NotificationRepository
import pl.jitsolutions.agile.repository.UserRepository

class AssignDeviceTokenToUserTokenUseCase(
    private val notificationRepository: NotificationRepository,
    private val userRepository: UserRepository,
    dispatcher: CoroutineDispatcher
) :
    UseCase<AssignDeviceTokenToUserTokenUseCase.Params, Unit>(dispatcher) {

    override suspend fun build(params: Params): Response<Unit> {
        val userResponse = userRepository.getLoggedInUser()
        return if (userResponse.isSuccessfulWithData()) {
            notificationRepository.assignDeviceTokenToUser(userResponse.data?.id!!)
        } else {
            errorResponse(error = userResponse.error ?: Error.Unknown)
        }
    }

    class Params
}