package pl.jitsolutions.agile.domain.usecases

import kotlinx.coroutines.CoroutineDispatcher
import pl.jitsolutions.agile.common.Error
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.errorResponse
import pl.jitsolutions.agile.repository.UserRepository

class UserRegistrationUseCase(
    private val userRepository: UserRepository,
    private val cloudMessagingManager: CloudMessagingManager,
    dispatcher: CoroutineDispatcher
) : UseCase<UserRegistrationUseCase.Params, Unit>(dispatcher) {

    override suspend fun build(params: Params): Response<Unit> {
        val validationError = params.validate()
        if (validationError != null) {
            return errorResponse(error = validationError)
        }
        val response = userRepository.register(params.userName, params.email, params.password)
        return when (response.status) {
            Response.Status.SUCCESS -> cloudMessagingManager.assignDeviceTokenToUser(response.data!!)
            Response.Status.FAILURE -> {
                errorResponse(error = response.error ?: Error.Unknown)
            }
        }
    }

    data class Params(val email: String, val userName: String, val password: String) {
        fun validate(): Error? = when {
            userName.isEmpty() -> Error.EmptyName
            email.isEmpty() -> Error.EmptyEmail
            password.isEmpty() -> Error.EmptyPassword
            else -> null
        }
    }
}