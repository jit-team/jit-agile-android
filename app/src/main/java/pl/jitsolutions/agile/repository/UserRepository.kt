package pl.jitsolutions.agile.repository

import pl.jitsolutions.agile.domain.User

interface UserRepository {
    fun login(email: String, password: String): User
}