package pl.jitsolutions.agile.domain.usecases

import kotlinx.coroutines.experimental.CoroutineDispatcher
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.errorResponse
import pl.jitsolutions.agile.repository.ProjectRepository

class ProjectCreationUseCase(
    private val projectRepository: ProjectRepository,
    dispatcher: CoroutineDispatcher
) : UseCase<ProjectCreationUseCase.Params, String>(dispatcher) {

    override suspend fun build(params: Params): Response<String> {
        if (params.validate() != null)
            return errorResponse(error = params.validate()!!)
        val response = projectRepository.createNewProject(params.name, params.password)

        return when (response.status) {
            Response.Status.SUCCESS -> response
            Response.Status.ERROR -> projectErrorResponse(response)
        }
    }

    private fun projectErrorResponse(response: Response<String>): Response<String> {
        return when (response.error) {
            is ProjectRepository.Error.ProjectAlreadyExist -> errorResponse(error = Error.ProjectAlreadyExist)
            is ProjectRepository.Error.ServerConnection -> errorResponse(error = Error.ServerConnection)
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
        object InvalidPassword : Error()
        object ProjectAlreadyExist : Error()
        object ServerConnection : Error()
        object Unknown : Error()
    }
}