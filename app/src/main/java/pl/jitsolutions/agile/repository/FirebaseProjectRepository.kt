package pl.jitsolutions.agile.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.functions.FirebaseFunctions
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.async
import pl.jitsolutions.agile.domain.Project
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.User
import pl.jitsolutions.agile.domain.errorResponse
import pl.jitsolutions.agile.domain.response
import pl.jitsolutions.agile.repository.firebase.ProjectFb
import pl.jitsolutions.agile.repository.firebase.UserFb
import pl.jitsolutions.agile.repository.firebase.convertToDomainObjects
import pl.jitsolutions.agile.repository.firebase.toFirebaseObject
import pl.jitsolutions.agile.repository.firebase.toFirebaseObjects
import pl.jitsolutions.agile.repository.firebase.toProject
import kotlin.coroutines.experimental.suspendCoroutine

class FirebaseProjectRepository(val dispatcher: CoroutineDispatcher) : ProjectRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val functions = FirebaseFunctions.getInstance()

    override suspend fun getProjects(userId: String): Response<List<Project>> {
        return CoroutineScope(dispatcher).async {
            suspendCoroutine<Response<List<Project>>> { continuation ->
                firestore.collection("projects")
                    .whereEqualTo("users.$userId", true)
                    .get()
                    .addOnCompleteListener { task ->
                        continuation.resume(handleResponse(task))
                    }
            }
        }.await()
    }

    override suspend fun getProject(projectId: String): Response<Project> {
        return CoroutineScope(dispatcher).async {
            suspendCoroutine<Response<Project>> { continuation ->
                firestore.collection("projects")
                    .document(projectId)
                    .get()
                    .addOnCompleteListener { task ->
                        continuation.resume(handleProjectResponse(task))
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
                    .addOnFailureListener {
                        continuation.resume(errorResponse(error = retrieveError(it)))
                    }
                    .addOnSuccessListener {
                        continuation.resume(response(Unit))
                    }
            }
        }.await()
    }

    private fun handleProjectResponse(task: Task<DocumentSnapshot>): Response<Project> {
        return when {
            task.isSuccessful -> {
                val project: ProjectFb? = task.result?.toFirebaseObject()
                if (project != null) {
                    response(project.toProject())
                } else {
                    errorResponse(error = Exception())
                }
            }
            else -> {
                errorResponse(error = Exception())
            }
        }
    }

    private fun handleResponse(task: Task<QuerySnapshot>): Response<List<Project>> {
        return when {
            task.isSuccessful -> {
                val projects: List<ProjectFb> = task.result.toFirebaseObjects()
                response(projects.convertToDomainObjects())
            }
            else -> {
                errorResponse(error = Exception())
            }
        }
    }

    override suspend fun getUsersAssignedToProject(projectId: String): Response<List<User>> {
        return CoroutineScope(dispatcher).async {
            suspendCoroutine<Response<List<User>>> { continuation ->
                firestore.collection("users")
                    .whereEqualTo("projects.$projectId", true)
                    .get()
                    .addOnCompleteListener { continuation.resume(handleUsersResponse(it)) }
                    .addOnFailureListener {
                        continuation.resume(errorResponse(error = retrieveError(it)))
                    }
            }
        }.await()
    }

    private fun handleUsersResponse(task: Task<QuerySnapshot>): Response<List<User>> {
        return when {
            task.isSuccessful -> {
                val users: List<UserFb> = task.result!!.toFirebaseObjects()
                response(users.convertToDomainObjects())
            }
            else -> {
                errorResponse(error = Exception())
            }
        }
    }

    private fun retrieveError(exception: Exception): UserRepository.Error {
        return when (exception) {
            is FirebaseAuthWeakPasswordException -> UserRepository.Error.WeakPassword
            is FirebaseAuthInvalidUserException -> UserRepository.Error.UserNotFound
            is FirebaseAuthInvalidCredentialsException -> UserRepository.Error.InvalidPassword
            is FirebaseAuthUserCollisionException -> UserRepository.Error.UserAlreadyExist
            is FirebaseNetworkException -> UserRepository.Error.ServerConnection
            else -> UserRepository.Error.UnknownError
        }
    }
}