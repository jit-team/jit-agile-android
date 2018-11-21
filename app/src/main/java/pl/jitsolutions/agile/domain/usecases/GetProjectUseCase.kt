package pl.jitsolutions.agile.domain.usecases

import kotlinx.coroutines.CoroutineDispatcher
import pl.jitsolutions.agile.domain.ProjectWithUsers
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.repository.ProjectRepository

class GetProjectUseCase(
    private val projectRepository: ProjectRepository,
    dispatcher: CoroutineDispatcher
) : UseCase<GetProjectUseCase.Params, ProjectWithUsers>(dispatcher) {

    override suspend fun build(params: Params): Response<ProjectWithUsers> {
        return projectRepository.getProject(params.projectId)
    }

    data class Params(val projectId: String)
}