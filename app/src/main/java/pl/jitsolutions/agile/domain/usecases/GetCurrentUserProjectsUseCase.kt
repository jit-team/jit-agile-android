package pl.jitsolutions.agile.domain.usecases

import kotlinx.coroutines.experimental.CoroutineDispatcher
import pl.jitsolutions.agile.domain.Project
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.errorResponse
import pl.jitsolutions.agile.domain.response
import pl.jitsolutions.agile.repository.ProjectRepository
import pl.jitsolutions.agile.repository.UserRepository

class GetCurrentUserProjectsUseCase(
    private val projectRepository: ProjectRepository,
    private val userRepository: UserRepository,
    dispatcher: CoroutineDispatcher
) : UseCase<GetCurrentUserProjectsUseCase.Params, List<Project>>(dispatcher) {

    override suspend fun build(params: Params): Response<List<Project>> {
        return when (userRepository.getLoggedInUser().status) {
            Response.Status.SUCCESS -> {
                getProjects()
            }
            Response.Status.FAILURE -> {
                errorResponse(error = Error.UserNotFound)
            }
        }
    }

    private suspend fun getProjects(): Response<List<Project>> {
        val response = projectRepository.getProjects(userRepository.getLoggedInUser().data?.id!!)
        return when (response.status) {
            Response.Status.SUCCESS -> {
                response(response.data!!)
            }
            else -> {
                resolveErrorResponse(response)
            }
        }
    }

    private fun resolveErrorResponse(response: Response<List<Project>>): Response<List<Project>> {
        val error = when (response.error) {
            ProjectRepository.Error.ServerConnection -> Error.ServerConnection
            else -> Error.UnknownError
        }
        return errorResponse(error = error)
    }

    class Params

    sealed class Error(message: String? = null) : Throwable(message) {
        object ServerConnection : Error()
        object UserNotFound : Error()
        object UnknownError : Error()
    }
}
