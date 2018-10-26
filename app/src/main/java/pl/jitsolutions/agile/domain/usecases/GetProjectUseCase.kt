package pl.jitsolutions.agile.domain.usecases

import kotlinx.coroutines.experimental.CoroutineDispatcher
import pl.jitsolutions.agile.domain.Project
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.repository.ProjectRepository

class GetProjectUseCase(private val projectRepository: ProjectRepository,
                        dispatcher: CoroutineDispatcher
) : UseCase<GetProjectUseCase.Params, Project>(dispatcher) {

    override suspend fun build(params: Params): Response<Project> {
        return projectRepository.getProject(params.projectId)
    }

    data class Params(val projectId: String)
}