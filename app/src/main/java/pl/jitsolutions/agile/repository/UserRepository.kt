package pl.jitsolutions.agile.repository

import kotlinx.coroutines.experimental.channels.ReceiveChannel
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.User

interface UserRepository {
    fun loginWithChannel(email: String, password: String): ReceiveChannel<Response<User>>
    suspend fun login(email: String, password: String): Response<User>
    fun registerWithChannel(email: String, password: String): ReceiveChannel<Response<User>>
    suspend fun register(email: String, password: String): Response<User>
}