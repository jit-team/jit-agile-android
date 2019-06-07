package pl.jitsolutions.agile.domain.usecases

import kotlinx.coroutines.CoroutineDispatcher
import pl.jitsolutions.agile.domain.Failure
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.Success
import pl.jitsolutions.agile.domain.User
import pl.jitsolutions.agile.repository.NotificationRepository
import pl.jitsolutions.agile.repository.UserRepository

class LogoutCurrentUserUseCase(
    private val userRepository: UserRepository,
    private val notificationRepository: NotificationRepository,
    dispatcher: CoroutineDispatcher
) : UseCase<LogoutCurrentUserUseCase.Params, User>(dispatcher) {

    override suspend fun build(params: Params): Response<User> {
        val response = userRepository.getLoggedInUser()
        return when (response) {
            is Success -> unassignDeviceTokenFromUser(response.data.id)
            is Failure -> Failure(response.error)
        }
    }

    private suspend fun unassignDeviceTokenFromUser(userId: String): Response<User> {
        val response = notificationRepository.unassignDeviceTokenFromUser(userId)
        return when (response) {
            is Success -> userRepository.logout()
            is Failure -> Failure(response.error)
        }
    }

    object Params
}