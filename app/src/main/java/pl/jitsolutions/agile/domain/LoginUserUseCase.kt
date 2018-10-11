package pl.jitsolutions.agile.domain

import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.channels.*
import pl.jitsolutions.agile.repository.ProjectRepository
import pl.jitsolutions.agile.repository.UserRepository

class LoginUserUseCase(private val userRepository: UserRepository,
                       private val projectRepository: ProjectRepository,
                       dispatcher: CoroutineDispatcher)
    : UseCase<LoginUserUseCase.Params, String>(dispatcher) {

    override suspend fun ProducerScope<String>.buildLogic(params: Params) {
        userRepository.login(params.email, params.password)
                .flatMap { user -> fullNameChannel(user.name) }
                .consumeEach { fullName -> send(fullName) }
    }

    private fun fullNameChannel(userName: String): ReceiveChannel<String> {
        return projectRepository.getGroups(userName)
                .map { group -> "$userName, groups: $group" }
    }

    data class Params(val email: String, val password: String)
}