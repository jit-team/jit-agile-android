package pl.jitsolutions.agile.domain.usecases

import androidx.core.util.PatternsCompat
import kotlinx.coroutines.experimental.CoroutineDispatcher
import pl.jitsolutions.agile.JitError
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.newErrorResponse
import pl.jitsolutions.agile.repository.UserRepository

class UserLoginUseCase(
    private val userRepository: UserRepository,
    dispatcher: CoroutineDispatcher
) : UseCase<UserLoginUseCase.Params, Unit>(dispatcher) {

    override suspend fun build(params: Params): Response<Unit> {
        val validationError = params.validate()
        if (validationError != null) {
            return newErrorResponse(error = validationError)
        }

        return userRepository.login(params.email, params.password)
    }

    data class Params(val email: String, val password: String) {
        fun validate(): JitError? {
            return when {
                email.isEmpty() -> JitError.EmptyEmail
                !PatternsCompat.EMAIL_ADDRESS.matcher(email).matches() -> JitError.InvalidEmail
                password.isEmpty() -> JitError.EmptyPassword
                else -> null
            }
        }
    }
}