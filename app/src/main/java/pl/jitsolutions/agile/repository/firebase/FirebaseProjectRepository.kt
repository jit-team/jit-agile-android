package pl.jitsolutions.agile.repository.firebase

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.FirebaseFunctionsException
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.async
import pl.jitsolutions.agile.domain.Project
import pl.jitsolutions.agile.domain.ProjectWithDaily
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.User
import pl.jitsolutions.agile.domain.errorResponse
import pl.jitsolutions.agile.domain.response
import pl.jitsolutions.agile.repository.ProjectRepository
import java.net.UnknownHostException
import kotlin.coroutines.experimental.suspendCoroutine

class FirebaseProjectRepository(val dispatcher: CoroutineDispatcher) :
    ProjectRepository {

    private val functions = FirebaseFunctions.getInstance()

    @Suppress("UNCHECKED_CAST")
    override suspend fun getProjectsWithDailyState(userId: String): Response<List<ProjectWithDaily>> {
        return CoroutineScope(dispatcher).async {
            suspendCoroutine<Response<List<ProjectWithDaily>>> { continuation ->
                functions
                    .getHttpsCallable("getProjectsWithDailyState")
                    .call()
                    .addOnSuccessListener { result ->
                        val projectsWithDaily = (result.data as ArrayList<Map<String, Any>>)
                            .map {
                                val project = (it["project"] as Map<String, Any>).toDomainProject()
                                val daily = (it["daily"] as? Map<String, Any>)?.toDomainDaily()
                                val membersCount = it["membersCount"] as Int
                                ProjectWithDaily(project, membersCount, daily)
                            }
                        continuation.resume(response(projectsWithDaily))
                    }
                    .addOnFailureListener {
                        continuation.resume(errorResponse(error = retrieveError(it)))
                    }
            }
        }.await()
    }

    @Suppress("UNCHECKED_CAST")
    override suspend fun getProjects(userId: String): Response<List<Project>> {
        return CoroutineScope(dispatcher).async {
            suspendCoroutine<Response<List<Project>>> { continuation ->
                functions
                    .getHttpsCallable("getProjects")
                    .call()
                    .addOnSuccessListener { result ->
                        val projects = (result.data as ArrayList<HashMap<String, Any>>)
                            .map { it.toDomainProject() }
                        continuation.resume(response(projects))
                    }
                    .addOnFailureListener {
                        continuation.resume(errorResponse(error = retrieveError(it)))
                    }
            }
        }.await()
    }

    private fun Map<String, Any>.toDomainProject(): Project {
        return Project(
            id = this["id"] as String,
            name = this["name"] as String
        )
    }

    private fun Map<String, Any>.toDomainUser(): User {
        return User(
            id = this["uid"] as String,
            name = this["displayName"] as? String ?: "",
            email = this["email"] as String
        )
    }

    @Suppress("UNCHECKED_CAST")
    override suspend fun getProject(projectId: String): Response<Pair<Project, List<User>>> {
        return CoroutineScope(dispatcher).async {
            suspendCoroutine<Response<Pair<Project, List<User>>>> { continuation ->
                val data = mutableMapOf("projectId" to projectId)
                functions
                    .getHttpsCallable("getProject")
                    .call(data)
                    .addOnSuccessListener { result ->
                        val dataMap = result.data as Map<String, Any>
                        val project = dataMap.toDomainProject()
                        val users = (dataMap["users"] as List<Map<String, Any>>)
                            .map { it.toDomainUser() }
                        continuation.resume(response(Pair(project, users)))
                    }
                    .addOnFailureListener {
                        continuation.resume(errorResponse(error = retrieveError(it)))
                    }
            }
        }.await()
    }

    override suspend fun leaveProject(projectId: String): Response<Unit> {
        return CoroutineScope(dispatcher).async {
            suspendCoroutine<Response<Unit>> { continuation ->
                val data = mutableMapOf("projectId" to projectId)
                functions
                    .getHttpsCallable("leaveProject")
                    .call(data)
                    .addOnSuccessListener {
                        continuation.resume(response(Unit))
                    }
                    .addOnFailureListener {
                        continuation.resume(errorResponse(error = retrieveError(it)))
                    }
            }
        }.await()
    }

    override suspend fun deleteProject(projectId: String): Response<Unit> {
        return CoroutineScope(dispatcher).async {
            suspendCoroutine<Response<Unit>> { continuation ->
                val data = mutableMapOf("projectId" to projectId)
                functions
                    .getHttpsCallable("deleteProject")
                    .call(data)
                    .addOnSuccessListener {
                        continuation.resume(response(Unit))
                    }
                    .addOnFailureListener {
                        continuation.resume(errorResponse(error = retrieveError(it)))
                    }
            }
        }.await()
    }

    override suspend fun joinProject(projectName: String, password: String): Response<String> {
        return CoroutineScope(dispatcher).async {
            suspendCoroutine<Response<String>> { continuation ->
                val data = mutableMapOf("projectName" to projectName, "password" to password)
                functions
                    .getHttpsCallable("joinProject")
                    .call(data)
                    .addOnSuccessListener {
                        val projectId = (it.data as Map<String, Any>)["projectId"] as String
                        continuation.resume(response(projectId))
                    }
                    .addOnFailureListener {
                        it.printStackTrace()
                        continuation.resume(errorResponse(error = retrieveError(it)))
                    }
            }
        }.await()
    }

    override suspend fun createNewProject(projectName: String, password: String): Response<String> {
        return CoroutineScope(dispatcher).async {
            suspendCoroutine<Response<String>> { continuation ->
                val data = mutableMapOf("projectName" to projectName, "password" to password)
                functions
                    .getHttpsCallable("newProject")
                    .call(data)
                    .addOnSuccessListener {
                        val projectId = (it.data as Map<String, Any>)["projectId"] as String
                        continuation.resume(response(projectId))
                    }
                    .addOnFailureListener {
                        it.printStackTrace()
                        continuation.resume(errorResponse(error = retrieveError(it)))
                    }
            }
        }.await()
    }

    // TODO need to be changed to ProjectRepository errors
    private fun retrieveError(exception: Exception): ProjectRepository.Error {
        return when (exception) {
            is FirebaseFunctionsException -> retrieveCloudFunctionException(exception)
            is FirebaseNetworkException -> ProjectRepository.Error.ServerConnection
            is UnknownHostException -> ProjectRepository.Error.ServerConnection
            else -> ProjectRepository.Error.UnknownError
        }
    }

    private fun retrieveCloudFunctionException(e: FirebaseFunctionsException): ProjectRepository.Error {
        return when (e.code) {
            FirebaseFunctionsException.Code.ALREADY_EXISTS -> ProjectRepository.Error.ProjectAlreadyExist
            FirebaseFunctionsException.Code.NOT_FOUND -> ProjectRepository.Error.ProjectNotFound(
                ""
            )
            FirebaseFunctionsException.Code.INVALID_ARGUMENT -> retrieveCloudFunctionMessage(e)
            else -> ProjectRepository.Error.UnknownError
        }
    }

    private fun retrieveCloudFunctionMessage(e: FirebaseFunctionsException): ProjectRepository.Error {
        return when (e.message) {
            "invalid password" -> ProjectRepository.Error.InvalidPassword
            else -> ProjectRepository.Error.UnknownError
        }
    }
}