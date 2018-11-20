package pl.jitsolutions.agile.domain.usecases

import androidx.core.util.PatternsCompat
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
        val validate = params.validate()
        if (validate != null) {
            return newErrorResponse(error = validate)
        }
        return userRepository.register(params.userName, params.email, params.password)
    }

    data class Params(val email: String, val userName: String, val password: String) {
        fun validate(): JitError? {
            return when {
                userName.isEmpty() -> JitError.EmptyName
                email.isEmpty() -> JitError.EmptyEmail
                !PatternsCompat.EMAIL_ADDRESS.matcher(email).matches() -> JitError.InvalidEmail
                password.isEmpty() -> JitError.EmptyPassword
                else -> null
            }
        }
    }
}