package pl.jitsolutions.agile.domain

import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.channels.ProducerScope
import kotlinx.coroutines.experimental.channels.ReceiveChannel
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.channels.map
import pl.jitsolutions.agile.repository.ProjectRepository
import pl.jitsolutions.agile.repository.UserRepository

class LoginUserUseCase(private val userRepository: UserRepository,
                       private val projectRepository: ProjectRepository,
                       dispatcher: CoroutineDispatcher)
    : UseCase<LoginUserUseCase.Params, String>(dispatcher) {

    override suspend fun ProducerScope<Response<String>>.buildLogic(params: Params) {
        userRepository.login(params.email, params.password)
                .consumeEach { loginResponse ->
                    when (loginResponse.status) {
                        Response.Status.SUCCESS -> {
                            fullNameChannel(loginResponse.data!!.name)
                                    .consumeEach { fullName -> send(response(fullName)) }
                        }
                        Response.Status.ERROR -> send(errorResponse(error = Unit))
                        Response.Status.IN_PROGRESS -> send(inProgressResponse())
                    }
                }
    }

    private fun fullNameChannel(userName: String): ReceiveChannel<String> {
        return projectRepository.getGroups(userName)
                .map { groupResponse -> "$userName, groups: ${groupResponse.data}" }
    }

    data class Params(val email: String, val password: String)
}