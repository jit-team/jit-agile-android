package pl.jitsolutions.agile.domain.usecases

import kotlinx.coroutines.CoroutineDispatcher
import pl.jitsolutions.agile.common.Error
import pl.jitsolutions.agile.domain.Failure
import pl.jitsolutions.agile.domain.ProjectWithDaily
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.Success
import pl.jitsolutions.agile.domain.User
import pl.jitsolutions.agile.repository.ProjectRepository
import pl.jitsolutions.agile.repository.UserRepository

class GetCurrentUserProjectsWithDailyUseCase(
    private val projectRepository: ProjectRepository,
    private val userRepository: UserRepository,
    dispatcher: CoroutineDispatcher
) : UseCase<GetCurrentUserProjectsWithDailyUseCase.Params, List<ProjectWithDaily>>(dispatcher) {

    override suspend fun build(params: Params): Response<List<ProjectWithDaily>> {
        val response = userRepository.getLoggedInUser()
        return when (response) {
            is Success -> getProjectsWithDaily(response.data)
            is Failure -> when (response.error) {
                Error.DoesNotExist -> {
                    Failure(response.error)
                    // TODO: user session not found, move to login screen
                }
                else -> Failure(response.error)
            }
        }
    }

    private suspend fun getProjectsWithDaily(user: User): Response<List<ProjectWithDaily>> {
        return projectRepository.getProjectsWithDailyState(user.id)
    }

    object Params
}
