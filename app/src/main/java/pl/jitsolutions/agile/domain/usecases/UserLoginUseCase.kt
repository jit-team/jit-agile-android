package pl.jitsolutions.agile.domain.usecases

import kotlinx.coroutines.CoroutineDispatcher
import pl.jitsolutions.agile.common.Error
import pl.jitsolutions.agile.domain.Failure
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.Success
import pl.jitsolutions.agile.repository.NotificationRepository
import pl.jitsolutions.agile.repository.UserRepository

class UserLoginUseCase(
    private val userRepository: UserRepository,
    private val notificationRepository: NotificationRepository,
    dispatcher: CoroutineDispatcher
) : UseCase<UserLoginUseCase.Params, Unit>(dispatcher) {

    override suspend fun build(params: Params): Response<Unit> {
        val validationError = params.validate()
        if (validationError != null) {
            return Failure(validationError)
        }
        val response = userRepository.login(params.email, params.password)
        return when (response) {
            is Success -> handleSuccess()
            is Failure -> response
        }
    }

    private suspend fun handleSuccess(): Response<Unit> {
        val response = userRepository.getLoggedInUser()
        return when (response) {
            is Success -> if (response.data == null) {
                Failure(Error.Unknown)
            } else {
                notificationRepository.assignDeviceTokenToUser(response.data.id)
            }
            is Failure -> Failure(response.error)
        }
    }

    data class Params(val email: String, val password: String) {
        fun validate(): Error? = when {
            email.isEmpty() -> Error.EmptyEmail
            password.isEmpty() -> Error.EmptyPassword
            else -> null
        }
    }
}