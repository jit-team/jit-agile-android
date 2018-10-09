package pl.jitsolutions.agile.domain

import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.channels.ReceiveChannel
import kotlinx.coroutines.experimental.channels.produce
import pl.jitsolutions.agile.repository.UserRepository

class LoginUserUseCase(private val userRepository: UserRepository, private val dispatcher: CoroutineDispatcher) {

    fun execute(email: String, password: String) :ReceiveChannel<User> = CoroutineScope(dispatcher).produce {
        Thread.sleep(1000) //doing stuff
        send(userRepository.login(email, password).receive())
    }
}