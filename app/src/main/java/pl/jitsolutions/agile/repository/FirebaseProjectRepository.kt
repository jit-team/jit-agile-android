package pl.jitsolutions.agile.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.async
import pl.jitsolutions.agile.domain.Project
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.errorResponse
import pl.jitsolutions.agile.domain.response
import pl.jitsolutions.agile.repository.firebase.ProjectFb
import pl.jitsolutions.agile.repository.firebase.convertToDomainObject
import kotlin.coroutines.experimental.suspendCoroutine

class FirebaseProjectRepository(val dispatcher: CoroutineDispatcher) : ProjectRepository {

    private val firestore = FirebaseFirestore.getInstance()

    override suspend fun getProjects(userId: String): Response<List<Project>> {
        return CoroutineScope(dispatcher).async {
            suspendCoroutine<Response<List<Project>>> { continuation ->
                firestore.collection("projects")
                    .whereEqualTo("users.$userId", true)
                    .get()
                    .addOnCompleteListener { task ->
                        continuation.resume(handleProjectsResponse(task))
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

    private fun handleProjectResponse(task: Task<DocumentSnapshot>): Response<Project> {
        return when {
            task.isSuccessful -> {
                val project: ProjectFb? = task.result?.toFirebaseObject()
                if (project != null) {
                    response(Project(project.id, project.name))
                } else {
                    errorResponse(error = Exception())
                }
            }
            task.exception != null -> errorResponse(error = task.exception!!)
            else -> {
                errorResponse(error = Exception())
            }
        }
    }

    private fun handleProjectsResponse(task: Task<QuerySnapshot>): Response<List<Project>> {
        return when {
            task.isSuccessful -> {
                if (task.result.isNetworkResponse()) {
                    val projects: List<ProjectFb> = task.result.toFirebaseObjects()
                    response(projects.convertToDomainObject())
                } else {
                    errorResponse(error = ProjectRepository.Error.ServerConnection)
                }
            }
            else -> {
                errorResponse(error = Exception())
            }
        }
    }

    private fun QuerySnapshot?.isNetworkResponse(): Boolean {
        val cachedResponse = this?.metadata?.isFromCache ?: true
        return !cachedResponse
    }

    private fun <T : DocumentSnapshot> Task<T>.isDocumentResultOk(): Boolean {
        val cached = result?.metadata?.isFromCache ?: true
        return isSuccessful && !cached
    }

    private inline fun <reified T> DocumentSnapshot.toFirebaseObject() =
        this.toObject(T::class.java)

    private inline fun <reified T> QuerySnapshot?.toFirebaseObjects() =
        this?.toObjects(T::class.java)
            ?: emptyList()
}

