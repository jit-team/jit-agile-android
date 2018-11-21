package pl.jitsolutions.agile.domain.usecases

import kotlinx.coroutines.CoroutineDispatcher
import pl.jitsolutions.agile.domain.ProjectWithDaily
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.Response.Status.FAILURE
import pl.jitsolutions.agile.domain.Response.Status.SUCCESS
import pl.jitsolutions.agile.domain.User
import pl.jitsolutions.agile.domain.errorResponse
import pl.jitsolutions.agile.repository.ProjectRepository
import pl.jitsolutions.agile.repository.UserRepository

class GetCurrentUserProjectsWithDailyUseCase(
    private val projectRepository: ProjectRepository,
    private val userRepository: UserRepository,
    dispatcher: CoroutineDispatcher
) : UseCase<GetCurrentUserProjectsWithDailyUseCase.Params, List<ProjectWithDaily>>(dispatcher) {

    override suspend fun build(params: Params): Response<List<ProjectWithDaily>> {
        val loggedInUserResponse = userRepository.getLoggedInUser()
        return when (loggedInUserResponse.status) {
            SUCCESS -> getProjectsWithDaily(loggedInUserResponse.data!!)
            FAILURE -> errorResponse(error = loggedInUserResponse.error!!)
        }
    }

    private suspend fun getProjectsWithDaily(user: User): Response<List<ProjectWithDaily>> {
        return projectRepository.getProjectsWithDailyState(user.id)
    }

    object Params
}
