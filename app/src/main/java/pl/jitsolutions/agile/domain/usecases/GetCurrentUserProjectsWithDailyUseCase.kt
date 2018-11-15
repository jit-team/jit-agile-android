package pl.jitsolutions.agile.domain.usecases

import kotlinx.coroutines.experimental.CoroutineDispatcher
import pl.jitsolutions.agile.domain.ProjectWithDaily
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.errorResponse
import pl.jitsolutions.agile.domain.response
import pl.jitsolutions.agile.repository.ProjectRepository
import pl.jitsolutions.agile.repository.UserRepository

class GetCurrentUserProjectsWithDailyUseCase(
    private val projectRepository: ProjectRepository,
    private val userRepository: UserRepository,
    dispatcher: CoroutineDispatcher
) : UseCase<GetCurrentUserProjectsWithDailyUseCase.Params, List<ProjectWithDaily>>(dispatcher) {

    override suspend fun build(params: Params): Response<List<ProjectWithDaily>> {
        return when (userRepository.getLoggedInUser().status) {
            Response.Status.SUCCESS -> {
                getProjectsWithDaily()
            }
            Response.Status.ERROR -> {
                errorResponse(error = Error.UserNotFound)
            }
        }
    }

    private suspend fun getProjectsWithDaily(): Response<List<ProjectWithDaily>> {
        val userId = userRepository.getLoggedInUser().data?.id!!
        val response = projectRepository.getProjectsWithDailyState(userId)
        return when (response.status) {
            Response.Status.SUCCESS -> {
                response(response.data!!)
            }
            else -> {
                resolveErrorResponse(response)
            }
        }
    }

    private fun resolveErrorResponse(response: Response<List<ProjectWithDaily>>): Response<List<ProjectWithDaily>> {
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
