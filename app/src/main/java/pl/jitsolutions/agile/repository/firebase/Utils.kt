package pl.jitsolutions.agile.repository.firebase

import com.google.firebase.auth.FirebaseUser
import pl.jitsolutions.agile.domain.Daily
import pl.jitsolutions.agile.domain.Project
import pl.jitsolutions.agile.domain.User

fun FirebaseUser.toUser(): User {
    return User(id = uid, name = displayName ?: "", email = email ?: "")
}

@Suppress("UNCHECKED_CAST")
fun Any?.toDomainDaily(): Daily {
    val dailyMap = this as Map<String, Any>
    return Daily(
        project = dailyMap["project"].toDomainProject(),
        startTime = dailyMap["startTime"]?.let { time -> time as Long }
            ?: 0,
        state = dailyMap["state"] as String,
        queue = dailyMap["queue"]?.let { queue -> queue as List<String> }
            ?: emptyList(),
        users = dailyMap["users"].toUserList()
    )
}

@Suppress("UNCHECKED_CAST")
fun Any?.toDomainProject(): Project {
    val map = this as Map<String, Any>
    return Project(
        id = map["id"] as String,
        name = map["name"] as String
    )
}

@Suppress("UNCHECKED_CAST")
fun Any?.toUserList(): List<User> {
    val usersMap = this as Map<String, Map<String, Any>>
    return usersMap.map { (_, value) ->
        User(
            id = value["uid"] as String,
            name = value["displayName"] as? String ?: "",
            email = value["email"] as String,
            active = value["active"] as Boolean
        )
    }
}