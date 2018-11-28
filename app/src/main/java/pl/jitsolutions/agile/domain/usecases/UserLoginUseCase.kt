package pl.jitsolutions.agile.domain.usecases

import kotlinx.coroutines.CoroutineDispatcher
import pl.jitsolutions.agile.common.Error
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.errorResponse
import pl.jitsolutions.agile.repository.UserRepository

class UserLoginUseCase(
    private val userRepository: UserRepository,
    private val cloudMessagingManager: CloudMessagingManager,
    dispatcher: CoroutineDispatcher
) : UseCase<UserLoginUseCase.Params, Unit>(dispatcher) {

    override suspend fun build(params: Params): Response<Unit> {
        val validationError = params.validate()
        if (validationError != null) {
            return errorResponse(error = validationError)
        }
        val response = userRepository.login(params.email, params.password)
        return when (response.status) {
            Response.Status.SUCCESS -> cloudMessagingManager.assignDeviceTokenToUser(response.data!!)
            Response.Status.FAILURE -> {
                errorResponse(error = response.error ?: Error.Unknown)
            }
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