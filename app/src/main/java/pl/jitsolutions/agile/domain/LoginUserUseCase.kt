package pl.jitsolutions.agile.domain

import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.channels.*
import pl.jitsolutions.agile.repository.ProjectRepository
import pl.jitsolutions.agile.repository.UserRepository

class LoginUserUseCase(private val userRepository: UserRepository,
                       private val projectRepository: ProjectRepository,
                       private val dispatcher: CoroutineDispatcher) {

    fun execute(email: String, password: String): ReceiveChannel<String> = CoroutineScope(dispatcher).produce {
        val userData: ReceiveChannel<User> = userRepository.login(email, password)
        //FIXME: chain this
        userData.consumeEach { user ->
            projectRepository.getGroups(user.name).consumeEach {
                send(user.name.plus(", groups: ").plus(it))
            }
        }
    }
}