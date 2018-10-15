package pl.jitsolutions.agile.repository

import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.User

interface UserRepository {
    suspend fun login(email: String, password: String): Response<User>
    suspend fun register(email: String, password: String): Response<User>

    sealed class Error {
        object LoginWrongEmail : Error()
        object LoginWrongPassword : Error()
        object ServerConnection : Error()
    }
}