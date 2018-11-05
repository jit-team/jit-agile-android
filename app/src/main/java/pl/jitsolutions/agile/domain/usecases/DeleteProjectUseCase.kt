package pl.jitsolutions.agile.domain.usecases

import kotlinx.coroutines.experimental.CoroutineDispatcher
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.Response.Status.ERROR
import pl.jitsolutions.agile.domain.Response.Status.SUCCESS
import pl.jitsolutions.agile.domain.errorResponse
import pl.jitsolutions.agile.repository.ProjectRepository

class DeleteProjectUseCase(
    private val projectRepository: ProjectRepository,
    dispatcher: CoroutineDispatcher
) : UseCase<DeleteProjectUseCase.Params, Unit>(dispatcher) {

    override suspend fun build(params: Params): Response<Unit> {
        val response = projectRepository.deleteProject(params.projectId)
        return when (response.status) {
            SUCCESS -> response
            ERROR -> when (response.error) {
                is ProjectRepository.Error.ProjectNotFound ->
                    errorResponse(error = Error.ProjectNotFound(response.error.projectId))
                ProjectRepository.Error.ServerConnection ->
                    errorResponse(error = Error.ServerConnection)
                else -> throw response.error!!
            }
        }
    }

    data class Params(val projectId: String)

    sealed class Error(message: String? = null) : Throwable(message) {
        data class ProjectNotFound(val projectId: String) :
            Error("Project with id: $projectId not found!")

        object ServerConnection : Error("Server connection error")
    }
}