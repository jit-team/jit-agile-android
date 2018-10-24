package pl.jitsolutions.agile.domain

import androidx.core.util.PatternsCompat
import kotlinx.coroutines.experimental.CoroutineDispatcher
import pl.jitsolutions.agile.domain.usecases.UseCase
import pl.jitsolutions.agile.repository.UserRepository

class UserResetPasswordUseCase(private val userRepository: UserRepository,
                               dispatcher: CoroutineDispatcher) : UseCase<UserResetPasswordUseCase.Params, Void?>(dispatcher) {

    override suspend fun build(params: Params): Response<Void?> {
        val validationError = params.validate()
        if (validationError != null) {
            return errorResponse(error = validationError)
        }

        val response = userRepository.resetPassword(params.email)
        return when (response.status) {
            Response.Status.SUCCESS -> response(null)
            Response.Status.ERROR -> errorResponse(response)
        }
    }

    private fun errorResponse(response: Response<Void?>): Response<Void?> {
        return when (response.error) {
            is UserRepository.Error.InvalidEmail -> errorResponse(error = Error.UserEmailNotFound)
            is UserRepository.Error.ServerConnection -> errorResponse(error = Error.ServerConnection)
            else -> errorResponse(error = Error.UnknownError)
        }
    }

    data class Params(val email: String) {
        fun validate(): Error? {
            if (email.isEmpty())
                return Error.EmptyEmail
            if (!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches())
                return Error.InvalidEmail
            return null
        }
    }

    sealed class Error (message: String? = null) : Throwable(message) {
        object UserEmailNotFound : Error()
        object EmptyEmail : Error()
        object InvalidEmail : Error()
        object ServerConnection : Error()
        object UnknownError : Error()
    }
}
