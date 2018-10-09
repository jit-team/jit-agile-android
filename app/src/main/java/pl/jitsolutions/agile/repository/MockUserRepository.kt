package pl.jitsolutions.agile.repository

import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.channels.ReceiveChannel
import kotlinx.coroutines.experimental.channels.produce
import pl.jitsolutions.agile.domain.User

class MockUserRepository(private val dispatcher: CoroutineDispatcher) : UserRepository {
    override fun login(email: String, password: String): ReceiveChannel<User>
            = CoroutineScope(dispatcher).produce { send(User(email)) }
}