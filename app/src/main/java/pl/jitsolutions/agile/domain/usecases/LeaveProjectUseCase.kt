package pl.jitsolutions.agile.domain.usecases

import kotlinx.coroutines.experimental.CoroutineDispatcher
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.repository.ProjectRepository

class LeaveProjectUseCase(
    private val projectRepository: ProjectRepository,
    dispatcher: CoroutineDispatcher
) : UseCase<LeaveProjectUseCase.Params, Unit>(dispatcher) {

    override suspend fun build(params: Params): Response<Unit> {
        return projectRepository.leaveProject(params.projectId)
    }

    data class Params(val projectId: String)
}