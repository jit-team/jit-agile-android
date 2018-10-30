package pl.jitsolutions.agile.presentation.projects.details

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.launch
import pl.jitsolutions.agile.domain.Project
import pl.jitsolutions.agile.domain.Response.Status.ERROR
import pl.jitsolutions.agile.domain.Response.Status.SUCCESS
import pl.jitsolutions.agile.domain.usecases.GetProjectUseCase
import pl.jitsolutions.agile.presentation.common.CoroutineViewModel
import pl.jitsolutions.agile.presentation.navigation.Navigator

class ProjectDetailsViewModel(
    private val getProjectUseCase: GetProjectUseCase,
    private val navigator: Navigator,
    private val projectId: String,
    mainDispatcher: CoroutineDispatcher
) : CoroutineViewModel(mainDispatcher) {
    val project = MutableLiveData<Project>()

    init {
        executeGetProject()
    }

    private fun executeGetProject() = launch {
        val params = GetProjectUseCase.Params(projectId)
        val result = getProjectUseCase.executeAsync(params).await()
        when (result.status) {
            SUCCESS -> project.value = result.data!!
            ERROR -> throw result.error!!
        }
    }
}