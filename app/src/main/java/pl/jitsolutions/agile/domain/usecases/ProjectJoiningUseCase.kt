package pl.jitsolutions.agile.domain.usecases

import kotlinx.coroutines.CoroutineDispatcher
import pl.jitsolutions.agile.common.Error
import pl.jitsolutions.agile.domain.Failure
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.usecases.ProjectJoiningUseCase.Params
import pl.jitsolutions.agile.repository.ProjectRepository

class ProjectJoiningUseCase(
    private val projectRepository: ProjectRepository,
    dispatcher: CoroutineDispatcher
) : UseCase<Params, String>(dispatcher) {

    override suspend fun build(params: Params): Response<String> {
        val validationError = params.validate()
        if (validationError != null) {
            return Failure(error = validationError)
        }
        return projectRepository.joinProject(params.name, params.password)
    }

    data class Params(val name: String, val password: String) {
        fun validate(): Error? = when {
            name.isEmpty() -> Error.EmptyName
            password.isEmpty() -> Error.EmptyPassword
            else -> null
        }
    }
}
