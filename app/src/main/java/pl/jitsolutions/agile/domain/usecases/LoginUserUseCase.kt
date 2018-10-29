package pl.jitsolutions.agile.domain.usecases

import androidx.core.util.PatternsCompat
import kotlinx.coroutines.experimental.CoroutineDispatcher
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.User
import pl.jitsolutions.agile.domain.errorResponse
import pl.jitsolutions.agile.domain.response
import pl.jitsolutions.agile.repository.ProjectRepository
import pl.jitsolutions.agile.repository.UserRepository

class LoginUserUseCase(
    private val userRepository: UserRepository,
    private val projectRepository: ProjectRepository,
    dispatcher: CoroutineDispatcher
) : UseCase<LoginUserUseCase.Params, String>(dispatcher) {

    override suspend fun build(params: Params): Response<String> {
        val validationError = params.validate()
        if (validationError != null) {
            return errorResponse(error = validationError)
        }

        val response = userRepository.login(params.email, params.password)
        return when (response.status) {
            Response.Status.SUCCESS -> buildFullName(response.data!!.name)
            Response.Status.ERROR -> userErrorResponse(response)
        }
    }

    private fun userErrorResponse(response: Response<User>): Response<String> {
        return when (response.error) {
            is UserRepository.Error.InvalidEmail -> errorResponse(error = Error.UserEmailNotFound)
            is UserRepository.Error.InvalidPassword -> errorResponse(error = Error.WrongPassword)
            is UserRepository.Error.UserNotFound -> errorResponse(error = Error.UserEmailNotFound)
            is UserRepository.Error.ServerConnection -> errorResponse(error = Error.ServerConnection)
            else -> errorResponse(error = Error.UnknownError)
        }
    }

    private suspend fun buildFullName(userName: String): Response<String> {
        val response = projectRepository.getProjects(userName)
        return when (response.status) {
            Response.Status.SUCCESS -> response("$userName, projects: ${response.data!!}")
            Response.Status.ERROR -> fullNameErrorResponse(response)
        }
    }

    private fun fullNameErrorResponse(response: Response<String>): Response<String> {
        return when (response.error) {
            is ProjectRepository.Error.UserNotFound -> errorResponse(error = Error.ServerConnection)
            is ProjectRepository.Error.ServerConnection -> errorResponse(error = Error.ServerConnection)
            else -> errorResponse(error = Error.UnknownError)
        }
    }

    data class Params(val email: String, val password: String) {
        fun validate(): Error? {
            return when {
                email.isEmpty() -> Error.EmptyEmail
                !PatternsCompat.EMAIL_ADDRESS.matcher(email).matches() -> Error.InvalidEmail
                password.isEmpty() -> Error.EmptyPassword
                else -> null
            }
        }
    }

    sealed class Error(message: String? = null) : Throwable(message) {
        object UserEmailNotFound : Error()
        object WrongPassword : Error()
        object EmptyEmail : Error()
        object InvalidEmail : Error()
        object EmptyPassword : Error()
        object ServerConnection : Error()
        object UnknownError : Error()
    }
}