package pl.jitsolutions.agile.domain.usecases

import kotlinx.coroutines.experimental.CoroutineDispatcher
import pl.jitsolutions.agile.domain.Project
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.repository.ProjectRepository
import pl.jitsolutions.agile.repository.UserRepository

class GetCurrentUserProjects(
    private val userRepository: UserRepository,
    private val projectRepository: ProjectRepository,
    dispatcher: CoroutineDispatcher
) : UseCase<GetCurrentUserProjects.Params, List<Project>>(dispatcher) {

    override suspend fun build(params: Params): Response<List<Project>> {
        // TODO add error handling
        val user = userRepository.getLoggedInUser()
        return projectRepository.getProjects(user.data?.id!!)
    }

    class Params
}
