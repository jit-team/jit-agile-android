package pl.jitsolutions.agile.repository.firebase

import pl.jitsolutions.agile.domain.User

data class UserFb(var id: String = "", var name: String = "", var email: String = "")

fun List<UserFb>.convertToDomainObjects(): List<User> {
    return map { it.toUser() }
}

fun UserFb.toUser() = User(id, name, email)