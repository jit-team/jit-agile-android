package pl.jitsolutions.agile.domain.usecases

import androidx.core.util.PatternsCompat
import kotlinx.coroutines.experimental.CoroutineDispatcher
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.User
import pl.jitsolutions.agile.domain.errorResponse
import pl.jitsolutions.agile.domain.response
import pl.jitsolutions.agile.repository.UserRepository

class UserRegistrationUseCase(
    private val userRepository: UserRepository,
    dispatcher: CoroutineDispatcher
) : UseCase<UserRegistrationUseCase.Params, String>(dispatcher) {

    override suspend fun build(params: Params): Response<String> {
        if (params.validate() != null) {
            return errorResponse(error = params.validate()!!)
        }
        val response = userRepository.register(params.userName, params.email, params.password)
        return when (response.status) {
            Response.Status.SUCCESS -> response(response.data?.name!!)
            Response.Status.ERROR -> userErrorResponse(response)
        }
    }

    private fun userErrorResponse(response: Response<User>): Response<String> {
        return when (response.error) {
            is UserRepository.Error.InvalidEmail -> errorResponse(error = Error.InvalidEmail)
            is UserRepository.Error.InvalidPassword -> errorResponse(error = Error.InvalidPassword)
            is UserRepository.Error.WeakPassword -> errorResponse(error = Error.WeakPassword)
            is UserRepository.Error.UserAlreadyExist -> errorResponse(error = Error.UserAlreadyExist)
            is UserRepository.Error.ServerConnection -> errorResponse(error = Error.ServerConnection)
            else -> {
                errorResponse(error = Error.UnknownError)
            }
        }
    }

    data class Params(val email: String, val userName: String, val password: String) {
        fun validate(): Error? {
            return when {
                userName.isEmpty() -> Error.EmptyUserName
                email.isEmpty() -> Error.EmptyEmail
                !PatternsCompat.EMAIL_ADDRESS.matcher(email).matches() -> Error.InvalidEmail
                password.isEmpty() -> Error.EmptyPassword
                else -> null
            }
        }
    }

    sealed class Error(message: String? = null) : Throwable(message) {
        object InvalidEmail : Error()
        object InvalidPassword : Error()
        object UserAlreadyExist : Error()
        object WeakPassword : Error()
        object ServerConnection : Error()
        object UnknownError : Error()
        object EmptyUserName : Error()
        object EmptyEmail : Error()
        object EmptyPassword : Error()
    }
}