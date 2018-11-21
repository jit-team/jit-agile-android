package pl.jitsolutions.agile.domain.usecases

import kotlinx.coroutines.CoroutineDispatcher
import pl.jitsolutions.agile.common.Error
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.errorResponse
import pl.jitsolutions.agile.repository.ProjectRepository

class ProjectCreationUseCase(
    private val projectRepository: ProjectRepository,
    dispatcher: CoroutineDispatcher
) : UseCase<ProjectCreationUseCase.Params, String>(dispatcher) {

    override suspend fun build(params: Params): Response<String> {
        val validationError = params.validate()
        if (validationError != null) {
            return errorResponse(error = validationError)
        }
        return projectRepository.createNewProject(params.name, params.password)
    }

    data class Params(val name: String, val password: String) {
        fun validate(): Error? = when {
            name.isEmpty() -> Error.EmptyName
            password.isEmpty() -> Error.EmptyPassword
            else -> null
        }
    }
}