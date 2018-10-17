package pl.jitsolutions.agile.repository

import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.User

interface UserRepository {
    suspend fun login(email: String, password: String): Response<User>
    suspend fun register(userName: String, email: String, password: String): Response<User>
    suspend fun getLoggedInUser(): Response<User>

    sealed class Error {
        object InvalidCredentials : Error()
        object WeakPassword: Error()
        object UserAlreadyExist : Error()
        object UnknownError : Error()
        object ServerConnection : Error()
    }
}