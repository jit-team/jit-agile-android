package pl.jitsolutions.agile.presentation.projects.managing

import android.view.View
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import pl.jitsolutions.agile.R

@BindingAdapter("bindProjectCreationNameError")
fun bindProjectCreationNameError(
    view: TextInputLayout,
    projectCreationCreationState: ProjectCreationViewModel.ProjectCreationState
) {
    val errorText: String? = when {
        projectCreationCreationState.isErrorOfType(ProjectCreationViewModel.ProjectCreationErrorType.PROJECT_NAME) ->
            view.context.getString(R.string.project_creation_screen_error_invalid_project_name)
        projectCreationCreationState.isErrorOfType(ProjectCreationViewModel.ProjectCreationErrorType.PROJECT_ALREADY_EXIST) ->
            view.context.getString(R.string.project_creation_screen_error_project_already_exist)
        else -> null
    }
    view.isErrorEnabled = errorText != null
    view.error = errorText
}

@BindingAdapter("bindProjectCreationPasswordError")
fun bindProjectCreationPasswordError(
    view: TextInputLayout,
    projectCreationCreationState: ProjectCreationViewModel.ProjectCreationState
) {
    view.error = view.resources.getString(R.string.project_creation_screen_error_invalid_password)
    view.isErrorEnabled =
        projectCreationCreationState.isErrorOfType(ProjectCreationViewModel.ProjectCreationErrorType.PASSWORD)
}

@BindingAdapter("bindProjectCreationProgressVisibility")
fun bindProjectCreationProgressVisibility(
    progressBar: ProgressBar,
    projectCreationCreationState: ProjectCreationViewModel.ProjectCreationState
) {
    progressBar.visibility = when (projectCreationCreationState) {
        ProjectCreationViewModel.ProjectCreationState.None -> View.INVISIBLE
        ProjectCreationViewModel.ProjectCreationState.InProgress -> View.VISIBLE
        is ProjectCreationViewModel.ProjectCreationState.Error -> View.INVISIBLE
        ProjectCreationViewModel.ProjectCreationState.Success -> View.INVISIBLE
    }
}

@BindingAdapter("bindProjectCreationEditingEnabled")
fun bindProjectCreationEditingEnabled(view: View, projectCreationCreationState: ProjectCreationViewModel.ProjectCreationState) {
    view.isEnabled = when (projectCreationCreationState) {
        ProjectCreationViewModel.ProjectCreationState.None -> true
        ProjectCreationViewModel.ProjectCreationState.InProgress -> false
        is ProjectCreationViewModel.ProjectCreationState.Error -> true
        ProjectCreationViewModel.ProjectCreationState.Success -> false
    }
}