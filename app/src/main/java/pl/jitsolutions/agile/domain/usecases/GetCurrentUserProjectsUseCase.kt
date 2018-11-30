package pl.jitsolutions.agile.domain.usecases

import kotlinx.coroutines.CoroutineDispatcher
import pl.jitsolutions.agile.common.Error
import pl.jitsolutions.agile.domain.Failure
import pl.jitsolutions.agile.domain.Project
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.Success
import pl.jitsolutions.agile.domain.User
import pl.jitsolutions.agile.repository.ProjectRepository
import pl.jitsolutions.agile.repository.UserRepository

class GetCurrentUserProjectsUseCase(
    private val projectRepository: ProjectRepository,
    private val userRepository: UserRepository,
    dispatcher: CoroutineDispatcher
) : UseCase<GetCurrentUserProjectsUseCase.Params, List<Project>>(dispatcher) {

    override suspend fun build(params: Params): Response<List<Project>> {
        val response = userRepository.getLoggedInUser()
        return when (response) {
            is Success -> getProjects(response.data)
            is Failure -> when (response.error) {
                Error.DoesNotExist -> {
                    Failure(response.error)
                    // TODO: user session not found, move to login screen
                }
                else -> Failure(response.error)
            }
        }
    }

    private suspend fun getProjects(user: User): Response<List<Project>> {
        return projectRepository.getProjects(user.id)
    }

    object Params
}
