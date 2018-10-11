package pl.jitsolutions.agile.repository

import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.channels.ReceiveChannel
import kotlinx.coroutines.experimental.channels.produce
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.User
import pl.jitsolutions.agile.domain.response

class MockUserRepository(private val dispatcher: CoroutineDispatcher) : UserRepository {
    override fun login(email: String, password: String): ReceiveChannel<Response<User>> =
            CoroutineScope(dispatcher).produce { send(response(User(email))) }
}