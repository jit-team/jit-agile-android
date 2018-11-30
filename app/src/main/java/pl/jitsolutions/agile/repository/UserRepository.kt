package pl.jitsolutions.agile.repository

import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.User

interface UserRepository {
    suspend fun login(email: String, password: String): Response<Unit>
    suspend fun logout(): Response<User>
    suspend fun register(userName: String, email: String, password: String): Response<Unit>
    suspend fun getLoggedInUser(): Response<User>
    suspend fun resetPassword(email: String): Response<Unit>
}