package pl.jitsolutions.agile.domain.usecases

import kotlinx.coroutines.CoroutineDispatcher
import pl.jitsolutions.agile.common.Error
import pl.jitsolutions.agile.domain.Failure
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.repository.ProjectRepository

class ChangeProjectPasswordUseCase(
    private val projectRepository: ProjectRepository,
    dispatcher: CoroutineDispatcher
) : UseCase<ChangeProjectPasswordUseCase.Params, Unit>(dispatcher) {

    override suspend fun build(params: Params): Response<Unit> {
        val validationError = params.validate()
        if (validationError != null) {
            return Failure(error = validationError)
        }
        return projectRepository.changeProjectPassword(params.projectId, params.newPassword)
    }

    data class Params(val projectId: String, val newPassword: String) {
        fun validate(): Error? = when {
            newPassword.isEmpty() -> Error.EmptyPassword
            else -> null
        }
    }
}