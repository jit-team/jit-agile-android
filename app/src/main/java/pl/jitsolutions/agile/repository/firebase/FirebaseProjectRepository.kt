package pl.jitsolutions.agile.repository.firebase

import com.google.android.gms.tasks.Tasks
import com.google.firebase.functions.FirebaseFunctions
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import pl.jitsolutions.agile.domain.Project
import pl.jitsolutions.agile.domain.ProjectWithDaily
import pl.jitsolutions.agile.domain.ProjectWithUsers
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.Success
import pl.jitsolutions.agile.repository.ProjectRepository

class FirebaseProjectRepository(
    private val functions: FirebaseFunctions,
    dispatcher: CoroutineDispatcher
) : ProjectRepository {
    private val scope = CoroutineScope(dispatcher)

    override suspend fun getProjectsWithDailyState(userId: String): Response<List<ProjectWithDaily>> {
        return retryWhenError {
            scope.async {
                try {
                    val task = functions
                        .getHttpsCallable("getProjectsWithDailyState")
                        .call()
                    Tasks.await(task)
                    if (task.isSuccessful) {
                        Success(task.result?.data.toProjectWithDailyList())
                    } else {
                        FirebaseErrorResolver.parseFunctionException<List<ProjectWithDaily>>(
                            task.exception ?: Exception()
                        )
                    }
                } catch (e: Exception) {
                    FirebaseErrorResolver.parseFunctionException<List<ProjectWithDaily>>(e)
                }
            }.await()
        }
    }

    override suspend fun getProjects(userId: String): Response<List<Project>> {
        return retryWhenError {
            scope.async {
                try {
                    val task =
                        functions
                            .getHttpsCallable("getProjects")
                            .call()
                    Tasks.await(task)
                    if (task.isSuccessful) {
                        Success(task.result?.data.toProjectList())
                    } else {
                        FirebaseErrorResolver.parseFunctionException<List<Project>>(
                            task.exception ?: Exception()
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    FirebaseErrorResolver.parseFunctionException<List<Project>>(e)
                }
            }.await()
        }
    }

    override suspend fun getProject(projectId: String): Response<ProjectWithUsers> {
        return retryWhenError {
            scope.async {
                try {
                    val task = functions
                        .getHttpsCallable("getProject")
                        .call(getProjectParams(projectId))
                    Tasks.await(task)
                    if (task.isSuccessful) {
                        Success(task.result?.data.toProjectWithUsers())
                    } else {
                        FirebaseErrorResolver.parseFunctionException<ProjectWithUsers>(
                            task.exception ?: Exception()
                        )
                    }
                } catch (e: Exception) {
                    FirebaseErrorResolver.parseFunctionException<ProjectWithUsers>(e)
                }
            }.await()
        }
    }

    override suspend fun leaveProject(projectId: String): Response<Unit> {
        return retryWhenError {
            scope.async {
                try {
                    val task = functions
                        .getHttpsCallable("leaveProject")
                        .call(leaveProjectParams(projectId))
                    Tasks.await(task)
                    if (task.isSuccessful) {
                        Success(Unit)
                    } else {
                        FirebaseErrorResolver.parseFunctionException<Unit>(
                            task.exception ?: Exception()
                        )
                    }
                } catch (e: Exception) {
                    FirebaseErrorResolver.parseFunctionException<Unit>(e)
                }
            }.await()
        }
    }

    override suspend fun deleteProject(projectId: String): Response<Unit> {
        return retryWhenError {
            scope.async {
                try {
                    val task = functions
                        .getHttpsCallable("deleteProject")
                        .call(deleteProjectParams(projectId))
                    Tasks.await(task)
                    if (task.isSuccessful) {
                        Success(Unit)
                    } else {
                        FirebaseErrorResolver.parseFunctionException<Unit>(
                            task.exception ?: Exception()
                        )
                    }
                } catch (e: Exception) {
                    FirebaseErrorResolver.parseFunctionException<Unit>(e)
                }
            }.await()
        }
    }

    override suspend fun joinProject(projectName: String, password: String): Response<String> {
        return retryWhenError {
            scope.async {
                try {
                    val task = functions
                        .getHttpsCallable("joinProject")
                        .call(joinProjectParams(projectName, password))
                    Tasks.await(task)
                    if (task.isSuccessful) {
                        Success(task.result?.data.toProjectId())
                    } else {
                        FirebaseErrorResolver.parseFunctionException<String>(
                            task.exception ?: Exception()
                        )
                    }
                } catch (e: Exception) {
                    FirebaseErrorResolver.parseFunctionException<String>(e)
                }
            }.await()
        }
    }

    override suspend fun createNewProject(
        projectName: String,
        password: String
    ): Response<String> {
        return retryWhenError {
            scope.async {
                try {
                    val task = functions
                        .getHttpsCallable("newProject")
                        .call(newProjectParams(projectName, password))
                    Tasks.await(task)
                    if (task.isSuccessful) {
                        Success(task.result?.data.toProjectId())
                    } else {
                        FirebaseErrorResolver.parseFunctionException<String>(
                            task.exception ?: Exception()
                        )
                    }
                } catch (e: Exception) {
                    FirebaseErrorResolver.parseFunctionException<String>(e)
                }
            }.await()
        }
    }

    override suspend fun changeProjectPassword(
        projectId: String,
        newPassword: String
    ): Response<Unit> {
        return retryWhenError {
            scope.async {
                try {
                    val task = functions
                        .getHttpsCallable("changeProjectPassword")
                        .call(changeProjectPasswordParams(projectId, newPassword))
                    Tasks.await(task)
                    if (task.isSuccessful) {
                        Success(Unit)
                    } else {
                        FirebaseErrorResolver.parseFunctionException<Unit>(
                            task.exception ?: Exception()
                        )
                    }
                } catch (e: Exception) {
                    FirebaseErrorResolver.parseFunctionException<Unit>(e)
                }
            }.await()
        }
    }
}