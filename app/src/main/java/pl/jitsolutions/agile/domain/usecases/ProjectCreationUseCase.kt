package pl.jitsolutions.agile.domain.usecases

import kotlinx.coroutines.experimental.CoroutineDispatcher
import pl.jitsolutions.agile.JitError
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.newErrorResponse
import pl.jitsolutions.agile.repository.ProjectRepository

class ProjectCreationUseCase(
    private val projectRepository: ProjectRepository,
    dispatcher: CoroutineDispatcher
) : UseCase<ProjectCreationUseCase.Params, String>(dispatcher) {

    override suspend fun build(params: Params): Response<String> {
        val validationError = params.validate()
        if (validationError != null) {
            return newErrorResponse(error = validationError)
        }
        return projectRepository.createNewProject(params.name, params.password)
    }

    data class Params(val name: String, val password: String) {
        fun validate(): JitError? = when {
            name.isEmpty() -> JitError.EmptyName
            password.isEmpty() -> JitError.EmptyPassword
            else -> null
        }
    }
}