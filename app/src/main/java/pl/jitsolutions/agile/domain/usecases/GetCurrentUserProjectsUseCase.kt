package pl.jitsolutions.agile.domain.usecases

import kotlinx.coroutines.experimental.CoroutineDispatcher
import pl.jitsolutions.agile.domain.Project
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.Response.Status.FAILURE
import pl.jitsolutions.agile.domain.Response.Status.SUCCESS
import pl.jitsolutions.agile.domain.User
import pl.jitsolutions.agile.domain.newErrorResponse
import pl.jitsolutions.agile.repository.ProjectRepository
import pl.jitsolutions.agile.repository.UserRepository

class GetCurrentUserProjectsUseCase(
    private val projectRepository: ProjectRepository,
    private val userRepository: UserRepository,
    dispatcher: CoroutineDispatcher
) : UseCase<GetCurrentUserProjectsUseCase.Params, List<Project>>(dispatcher) {

    override suspend fun build(params: Params): Response<List<Project>> {
        val loggedInUserResponse = userRepository.getLoggedInUser()
        return when (loggedInUserResponse.status) {
            SUCCESS -> getProjects(loggedInUserResponse.data!!)
            FAILURE -> newErrorResponse(error = loggedInUserResponse.newError!!)
        }
    }

    private suspend fun getProjects(user: User): Response<List<Project>> {
        return projectRepository.getProjects(user.id)
    }

    object Params
}
