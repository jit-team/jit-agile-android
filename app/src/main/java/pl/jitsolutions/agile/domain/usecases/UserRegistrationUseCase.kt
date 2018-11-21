package pl.jitsolutions.agile.domain.usecases

import kotlinx.coroutines.experimental.CoroutineDispatcher
import pl.jitsolutions.agile.JitError
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.newErrorResponse
import pl.jitsolutions.agile.repository.UserRepository

class UserRegistrationUseCase(
    private val userRepository: UserRepository,
    dispatcher: CoroutineDispatcher
) : UseCase<UserRegistrationUseCase.Params, Unit>(dispatcher) {

    override suspend fun build(params: Params): Response<Unit> {
        val validationError = params.validate()
        if (validationError != null) {
            return newErrorResponse(error = validationError)
        }
        return userRepository.register(params.userName, params.email, params.password)
    }

    data class Params(val email: String, val userName: String, val password: String) {
        fun validate(): JitError? = when {
            userName.isEmpty() -> JitError.EmptyName
            email.isEmpty() -> JitError.EmptyEmail
            password.isEmpty() -> JitError.EmptyPassword
            else -> null
        }
    }
}