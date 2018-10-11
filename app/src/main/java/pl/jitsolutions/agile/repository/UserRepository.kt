package pl.jitsolutions.agile.repository

import kotlinx.coroutines.experimental.channels.ReceiveChannel
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.User

interface UserRepository {
    fun login(email: String, password: String): ReceiveChannel<Response<User>>
    fun register(email: String, password: String): ReceiveChannel<Response<User>>
}