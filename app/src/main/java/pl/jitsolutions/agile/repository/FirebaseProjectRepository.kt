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

    private fun handleProjectResponse(task: Task<DocumentSnapshot>): Response<Project> {
        return when {
            task.isSuccessful -> {
                val project: ProjectFb? = task.result?.toFirebaseObject()
                project?.let {
                    response(Project(project.name))
                }
                errorResponse(error = Exception())
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
                response(projects.convertToDomainObject())
            }
            else -> {
                errorResponse(error = Exception())
            }
        }
    }

    private inline fun <reified T> DocumentSnapshot.toFirebaseObject() = this.toObject(T::class.java)

    private inline fun <reified T> QuerySnapshot?.toFirebaseObjects() = this?.toObjects(T::class.java)
            ?: emptyList()
}