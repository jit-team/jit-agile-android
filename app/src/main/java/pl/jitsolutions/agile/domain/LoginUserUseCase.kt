package pl.jitsolutions.agile.domain

import kotlinx.coroutines.experimental.CoroutineDispatcher
import pl.jitsolutions.agile.repository.ProjectRepository
import pl.jitsolutions.agile.repository.UserRepository

class LoginUserUseCase(private val userRepository: UserRepository,
                       private val projectRepository: ProjectRepository,
                       dispatcher: CoroutineDispatcher)
    : UseCase<LoginUserUseCase.Params, String>(dispatcher) {

    override suspend fun build(params: Params): Response<String> {
        val response = userRepository.login(params.email, params.password)
        return when (response.status) {
            Response.Status.SUCCESS -> fullNameChannel(response.data!!.name)
            Response.Status.ERROR -> errorResponse(error = Unit)
        }
    }

    private suspend fun fullNameChannel(userName: String): Response<String> {
        val response = projectRepository.getProjects(userName)
        return when (response.status) {
            Response.Status.SUCCESS -> response("$userName, projects: ${response.data!!}")
            Response.Status.ERROR -> errorResponse(error = Unit)
        }
    }

    data class Params(val email: String, val password: String)
}