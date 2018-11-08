package pl.jitsolutions.agile.domain.usecases

import kotlinx.coroutines.experimental.CoroutineDispatcher
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.errorResponse
import pl.jitsolutions.agile.repository.ProjectRepository
import pl.jitsolutions.agile.domain.usecases.ProjectJoiningUseCase.Params

class ProjectJoiningUseCase(
    private val projectRepository: ProjectRepository,
    dispatcher: CoroutineDispatcher
) : UseCase<Params, Unit>(dispatcher) {

    override suspend fun build(params: Params): Response<Unit> {
        if (params.validate() != null)
            return errorResponse(error = params.validate()!!)
        val response = projectRepository.joinProject(params.name, params.password)

        return when (response.status) {
            Response.Status.SUCCESS -> response
            Response.Status.ERROR -> projectErrorResponse(response)
        }
    }

    private fun projectErrorResponse(response: Response<Unit>): Response<Unit> {
        return when (response.error) {
            is ProjectRepository.Error.ProjectNotFound -> errorResponse(error = Error.ProjectNotFound)
            is ProjectRepository.Error.ServerConnection -> errorResponse(error = Error.ServerConnection)
            is ProjectRepository.Error.InvalidPassword -> errorResponse(error = Error.InvalidPassword)
            else -> errorResponse(error = Error.Unknown)
        }
    }

    data class Params(val name: String, val password: String) {
        fun validate(): Error? {
            return when {
                name.isEmpty() -> Error.EmptyProjectName
                password.isEmpty() -> Error.EmptyPassword
                else -> null
            }
        }
    }

    sealed class Error : Throwable() {
        object EmptyProjectName : Error()
        object EmptyPassword : Error()
        object ProjectNotFound : Error()
        object ServerConnection : Error()
        object InvalidPassword : Error()
        object Unknown : Error()
    }
}