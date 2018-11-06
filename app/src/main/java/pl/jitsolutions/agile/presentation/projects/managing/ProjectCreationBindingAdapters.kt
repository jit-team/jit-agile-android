package pl.jitsolutions.agile.presentation.projects.managing

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import pl.jitsolutions.agile.R

@BindingAdapter("bindProjectCreationNameError")
fun bindProjectCreationNameError(
    view: TextInputLayout,
    projectCreationState: ProjectCreationViewModel.ProjectCreationState
) {
    val errorText: String? = when {
        projectCreationState.isErrorOfType(ProjectCreationViewModel.ProjectCreationErrorType.PROJECT_NAME) ->
            view.context.getString(R.string.project_creation_screen_error_invalid_project_name)
        projectCreationState.isErrorOfType(ProjectCreationViewModel.ProjectCreationErrorType.PROJECT_ALREADY_EXIST) ->
            view.context.getString(R.string.project_creation_screen_error_project_already_exist)
        else -> null
    }
    view.isErrorEnabled = errorText != null
    view.error = errorText
}

@BindingAdapter("bindProjectCreationPasswordError")
fun bindProjectCreationPasswordError(
    view: TextInputLayout,
    projectCreationState: ProjectCreationViewModel.ProjectCreationState
) {
    view.error = view.resources.getString(R.string.project_creation_screen_error_invalid_password)
    view.isErrorEnabled =
        projectCreationState.isErrorOfType(ProjectCreationViewModel.ProjectCreationErrorType.PASSWORD)
}

@BindingAdapter("bindProjectCreationProgressVisibility")
fun bindProjectCreationProgressVisibility(
    progressBar: ProgressBar,
    projectCreationState: ProjectCreationViewModel.ProjectCreationState
) {
    progressBar.visibility = when (projectCreationState) {
        ProjectCreationViewModel.ProjectCreationState.None -> View.INVISIBLE
        ProjectCreationViewModel.ProjectCreationState.InProgress -> View.VISIBLE
        is ProjectCreationViewModel.ProjectCreationState.Error -> View.INVISIBLE
        ProjectCreationViewModel.ProjectCreationState.Success -> View.INVISIBLE
    }
}

@BindingAdapter("bindProjectCreationEditingEnabled")
fun bindProjectCreationEditingEnabled(
    view: View,
    projectCreationState: ProjectCreationViewModel.ProjectCreationState
) {
    view.isEnabled = when (projectCreationState) {
        ProjectCreationViewModel.ProjectCreationState.None -> true
        ProjectCreationViewModel.ProjectCreationState.InProgress -> false
        is ProjectCreationViewModel.ProjectCreationState.Error -> true
        ProjectCreationViewModel.ProjectCreationState.Success -> false
    }
}

@BindingAdapter("bindProjectCreationUnknownErrorVisibility")
fun bindProjectCreationUnknownErrorVisibility(view: TextView, projectCreationState: ProjectCreationViewModel.ProjectCreationState) {
    val errorText: String? = when {
        projectCreationState.isErrorOfType(ProjectCreationViewModel.ProjectCreationErrorType.SERVER) ->
            view.context.getString(R.string.project_creation_screen_error_network)
        projectCreationState.isErrorOfType(ProjectCreationViewModel.ProjectCreationErrorType.UNKNOWN) ->
            view.context.getString(R.string.project_creation_screen_error_unknown)
        else -> null
    }
    view.text = errorText
    view.visibility = if (errorText != null) View.VISIBLE else View.INVISIBLE
}