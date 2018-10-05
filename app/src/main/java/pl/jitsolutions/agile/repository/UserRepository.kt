package pl.jitsolutions.agile.repository

import pl.jitsolutions.agile.domain.User

interface UserRepository {
    fun getCurrentUser() : User
}