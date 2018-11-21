package pl.jitsolutions.agile.repository.firebase

import com.google.firebase.auth.FirebaseUser
import pl.jitsolutions.agile.domain.Daily
import pl.jitsolutions.agile.domain.Project
import pl.jitsolutions.agile.domain.ProjectWithDaily
import pl.jitsolutions.agile.domain.ProjectWithUsers
import pl.jitsolutions.agile.domain.User

fun FirebaseUser.toUser(): User {
    return User(id = uid, name = displayName ?: "", email = email ?: "")
}

@Suppress("UNCHECKED_CAST")
fun Any?.toDaily(): Daily {
    val map = this as Map<String, Any>
    return Daily(
        project = map["project"].toProject(),
        startTime = map["startTime"]?.let { time -> time as Long }
            ?: 0,
        state = map["state"] as String,
        queue = map["queue"]?.let { queue -> queue as List<String> }
            ?: emptyList(),
        users = map["users"].toUserList()
    )
}

@Suppress("UNCHECKED_CAST")
fun Any?.toProject(): Project {
    val map = this as Map<String, Any>
    return Project(
        id = map["id"] as String,
        name = map["name"] as String
    )
}

@Suppress("UNCHECKED_CAST")
fun Any?.toProjectList(): List<Project> {
    return (this as ArrayList<Map<String, Any>>).map { it.toProject() }
}

@Suppress("UNCHECKED_CAST")
fun Any?.toProjectWithDaily(): ProjectWithDaily {
    val map = this as Map<String, Any>
    val project = (map["project"] as Map<String, Any>).toProject()
    val daily = (map["daily"] as? Map<String, Any>)?.toDaily()
    val membersCount = map["membersCount"] as Int
    return ProjectWithDaily(project, membersCount, daily)
}

@Suppress("UNCHECKED_CAST")
fun Any?.toProjectWithDailyList(): List<ProjectWithDaily> {
    return (this as ArrayList<Map<String, Any>>).map { it.toProjectWithDaily() }
}

@Suppress("UNCHECKED_CAST")
fun Any?.toUserList(): List<User> {
    val map = this as Map<String, Map<String, Any>>
    return map.map { (_, value) -> value.toUser() }
}

@Suppress("UNCHECKED_CAST")
fun Any?.toUser(): User {
    val map = this as Map<String, Any>
    return User(
        id = map["uid"] as String,
        name = map["displayName"] as? String ?: "",
        email = map["email"] as String,
        active = map["active"] as? Boolean ?: false
    )
}

@Suppress("UNCHECKED_CAST")
fun Any?.toProjectWithUsers(): ProjectWithUsers {
    val map = this as Map<String, Any>
    val project = map.toProject()
    val users = (map["users"] as List<Map<String, Any>>).map { it.toUser() }
    return ProjectWithUsers(project, users)
}

@Suppress("UNCHECKED_CAST")
fun Any?.toProjectId(): String {
    val map = this as Map<String, Any>
    return map["projectId"] as String
}

fun deleteProjectParams(projectId: String) =
    mapOf("projectId" to projectId)

fun joinProjectParams(projectName: String, password: String) =
    mapOf(
        "projectName" to projectName,
        "password" to password
    )

fun newProjectParams(projectName: String, password: String) =
    mapOf(
        "projectName" to projectName,
        "password" to password
    )

fun leaveProjectParams(projectId: String) =
    mapOf("projectId" to projectId)

fun getProjectParams(projectId: String) =
    mapOf("projectId" to projectId)

fun endDailyParams(dailyId: String) =
    mapOf("projectId" to dailyId)

fun nextDailyUserParams(dailyId: String) =
    mapOf("projectId" to dailyId)

fun joinDailyParams(dailyId: String) =
    mapOf("projectId" to dailyId)

fun leaveDailyParams(dailyId: String) =
    mapOf("projectId" to dailyId)

fun startDailyParams(dailyId: String) =
    mapOf("projectId" to dailyId)