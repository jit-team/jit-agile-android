package pl.jitsolutions.agile.repository.firebase

import com.google.firebase.functions.FirebaseFunctions
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.async
import pl.jitsolutions.agile.domain.Project
import pl.jitsolutions.agile.domain.ProjectWithDaily
import pl.jitsolutions.agile.domain.ProjectWithUsers
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.response
import pl.jitsolutions.agile.repository.ProjectRepository
import kotlin.coroutines.experimental.suspendCoroutine

class FirebaseProjectRepository(val dispatcher: CoroutineDispatcher) : ProjectRepository {
    private val functions = FirebaseFunctions.getInstance()

    override suspend fun getProjectsWithDailyState(userId: String): Response<List<ProjectWithDaily>> {
        return CoroutineScope(dispatcher).async {
            suspendCoroutine<Response<List<ProjectWithDaily>>> { continuation ->
                functions
                    .getHttpsCallable("getProjectsWithDailyState")
                    .call()
                    .addOnSuccessListener {
                        continuation.resume(response(it.data.toProjectWithDailyList()))
                    }
                    .addOnFailureListener {
                        continuation.resume(FirebaseErrorResolver.parseFunctionException(it))
                    }
            }
        }.await()
    }

    override suspend fun getProjects(userId: String): Response<List<Project>> {
        return CoroutineScope(dispatcher).async {
            suspendCoroutine<Response<List<Project>>> { continuation ->
                functions
                    .getHttpsCallable("getProjects")
                    .call()
                    .addOnSuccessListener {
                        continuation.resume(response(it.data.toProjectList()))
                    }
                    .addOnFailureListener {
                        continuation.resume(FirebaseErrorResolver.parseFunctionException(it))
                    }
            }
        }.await()
    }

    override suspend fun getProject(projectId: String): Response<ProjectWithUsers> {
        return CoroutineScope(dispatcher).async {
            suspendCoroutine<Response<ProjectWithUsers>> { continuation ->
                functions
                    .getHttpsCallable("getProject")
                    .call(getProjectParams(projectId))
                    .addOnSuccessListener {
                        continuation.resume(response(it.data.toProjectWithUsers()))
                    }
                    .addOnFailureListener {
                        continuation.resume(FirebaseErrorResolver.parseFunctionException(it))
                    }
            }
        }.await()
    }

    override suspend fun leaveProject(projectId: String): Response<Unit> {
        return CoroutineScope(dispatcher).async {
            suspendCoroutine<Response<Unit>> { continuation ->
                functions
                    .getHttpsCallable("leaveProject")
                    .call(leaveProjectParams(projectId))
                    .addOnSuccessListener {
                        continuation.resume(response(Unit))
                    }
                    .addOnFailureListener {
                        continuation.resume(FirebaseErrorResolver.parseFunctionException(it))
                    }
            }
        }.await()
    }

    override suspend fun deleteProject(projectId: String): Response<Unit> {
        return CoroutineScope(dispatcher).async {
            suspendCoroutine<Response<Unit>> { continuation ->
                functions
                    .getHttpsCallable("deleteProject")
                    .call(deleteProjectParams(projectId))
                    .addOnSuccessListener {
                        continuation.resume(response(Unit))
                    }
                    .addOnFailureListener {
                        continuation.resume(FirebaseErrorResolver.parseFunctionException(it))
                    }
            }
        }.await()
    }

    override suspend fun joinProject(projectName: String, password: String): Response<String> {
        return CoroutineScope(dispatcher).async {
            suspendCoroutine<Response<String>> { continuation ->
                functions
                    .getHttpsCallable("joinProject")
                    .call(joinProjectParams(projectName, password))
                    .addOnSuccessListener {
                        continuation.resume(response(it.data.toProjectId()))
                    }
                    .addOnFailureListener {
                        continuation.resume(FirebaseErrorResolver.parseFunctionException(it))
                    }
            }
        }.await()
    }

    override suspend fun createNewProject(projectName: String, password: String): Response<String> {
        return CoroutineScope(dispatcher).async {
            suspendCoroutine<Response<String>> { continuation ->
                functions
                    .getHttpsCallable("newProject")
                    .call(newProjectParams(projectName, password))
                    .addOnSuccessListener {
                        continuation.resume(response(it.data.toProjectId()))
                    }
                    .addOnFailureListener {
                        continuation.resume(FirebaseErrorResolver.parseFunctionException(it))
                    }
            }
        }.await()
    }
}