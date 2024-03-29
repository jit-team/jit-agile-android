package pl.jitsolutions.agile.domain.usecases

import kotlinx.coroutines.CoroutineDispatcher
import pl.jitsolutions.agile.common.Error
import pl.jitsolutions.agile.domain.Failure
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.repository.UserRepository

class UserResetPasswordUseCase(
    private val userRepository: UserRepository,
    dispatcher: CoroutineDispatcher
) : UseCase<UserResetPasswordUseCase.Params, Unit>(dispatcher) {

    override suspend fun build(params: Params): Response<Unit> {
        val validationError = params.validate()
        if (validationError != null) {
            return Failure(error = validationError)
        }
        return userRepository.resetPassword(params.email)
    }

    data class Params(val email: String) {
        fun validate(): Error? = when {
            email.isEmpty() -> Error.EmptyEmail
            else -> null
        }
    }
}
