package pl.jitsolutions.agile.domain.usecases

import kotlinx.coroutines.CoroutineDispatcher
import pl.jitsolutions.agile.common.Error
import pl.jitsolutions.agile.domain.Failure
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.Success
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
        return when (userResponse) {
            is Success -> if (userResponse.data == null) {
                Failure(Error.Unknown)
            } else {
                notificationRepository.assignDeviceTokenToUser(userResponse.data.id)
            }
            is Failure -> Failure(userResponse.error)
        }
    }

    class Params
}