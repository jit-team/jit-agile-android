package pl.jitsolutions.agile.presentation.projects

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.launch
import pl.jitsolutions.agile.domain.Project
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.Response.Status.ERROR
import pl.jitsolutions.agile.domain.Response.Status.SUCCESS
import pl.jitsolutions.agile.domain.User
import pl.jitsolutions.agile.domain.usecases.GetApplicationVersionUseCase
import pl.jitsolutions.agile.domain.usecases.GetCurrentUserProjects
import pl.jitsolutions.agile.domain.usecases.GetLoggedUserUseCase
import pl.jitsolutions.agile.domain.usecases.LogoutCurrentUserUseCase
import pl.jitsolutions.agile.presentation.common.CoroutineViewModel
import pl.jitsolutions.agile.presentation.navigation.Navigator
import pl.jitsolutions.agile.presentation.navigation.Navigator.Destination.*
import pl.jitsolutions.agile.utils.mutableLiveData

class ProjectListViewModel(private val getLoggedUserUseCase: GetLoggedUserUseCase,
                           private val logoutCurrentUserUseCase: LogoutCurrentUserUseCase,
                           private val getApplicationVersionUseCase: GetApplicationVersionUseCase,
                           private val getCurrentUserProjects: GetCurrentUserProjects,
                           private val navigator: Navigator,
                           mainDispatcher: CoroutineDispatcher
) : CoroutineViewModel(mainDispatcher) {
    val user = mutableLiveData<User?>(null)
    val version = mutableLiveData("")
    val projects = MutableLiveData<List<Project>>()
    val projectClick = object : ProjectListItemCallback {
        override fun click(project: Project) {
            //TODO: proceed to details screen
        }
    }

    init {
        executeGetLoggedUser()
        executeGetApplicationVersion()
        executeGetUserProjects()
    }

    fun logout() = launch {
        val params = LogoutCurrentUserUseCase.Params()
        val result = logoutCurrentUserUseCase.executeAsync(params).await()
        when (result.status) {
            SUCCESS -> navigator.navigate(ProjectList, Login)
            ERROR -> throw result.error!!
        }
    }

    fun showProjectDetails(projectId: String) {
        navigator.navigate(from = ProjectList, to = ProjectDetails(projectId))
    }

    private fun executeGetLoggedUser() = launch {
        val params = GetLoggedUserUseCase.Params()
        val result = getLoggedUserUseCase.executeAsync(params).await()
        when (result.status) {
            SUCCESS -> handleGetLoggedUserSuccess(result)
            ERROR -> throw result.error!!
        }
    }

    private fun executeGetUserProjects() = launch {
        val params = GetCurrentUserProjects.Params()
        val result = getCurrentUserProjects.executeAsync(params).await()
        when (result.status) {
            SUCCESS -> projects.value = result.data
        }
    }

    private fun handleGetLoggedUserSuccess(response: Response<User?>) {
        if (response.data != null) {
            user.value = response.data
        } else {
            navigator.navigate(from = ProjectList, to = Login)
        }
    }

    private fun executeGetApplicationVersion() = launch {
        val params = GetApplicationVersionUseCase.Params()
        val result = getApplicationVersionUseCase.executeAsync(params).await()
        when (result.status) {
            SUCCESS -> version.value = result.data!!
            ERROR -> throw result.error!!
        }
    }
}