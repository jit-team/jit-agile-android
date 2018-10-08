package pl.jitsolutions.agile.repository

import pl.jitsolutions.agile.domain.User

class MockUserRepository : UserRepository {
    override fun getCurrentUser(): User {
        return User("FakeUser")
    }

    override fun login(email: String, password: String): User {
        return User(email)
    }
}