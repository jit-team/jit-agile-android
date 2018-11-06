package pl.jitsolutions.agile.presentation.projects.managing

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import pl.jitsolutions.agile.R

@BindingAdapter("bindProjectJoiningNameError")
fun bindProjectJoiningNameError(
    view: TextInputLayout,
    projectJoiningState: ProjectJoiningViewModel.ProjectJoiningState
) {
    val errorText: String? = when {
        projectJoiningState.isErrorOfType(ProjectJoiningViewModel.ProjectJoiningErrorType.PROJECT_NAME) ->
            view.context.getString(R.string.project_joining_screen_error_invalid_project_name)
        projectJoiningState.isErrorOfType(ProjectJoiningViewModel.ProjectJoiningErrorType.PROJECT_NOT_FOUND) ->
            view.context.getString(R.string.project_joining_screen_error_project_not_found)
        else -> null
    }
    view.isErrorEnabled = errorText != null
    view.error = errorText
}

@BindingAdapter("bindProjectJoiningPasswordError")
fun bindProjectJoiningPasswordError(
    view: TextInputLayout,
    projectJoiningState: ProjectJoiningViewModel.ProjectJoiningState
) {
    view.error = view.resources.getString(R.string.project_joining_screen_error_invalid_password)
    view.isErrorEnabled =
        projectJoiningState.isErrorOfType(ProjectJoiningViewModel.ProjectJoiningErrorType.PASSWORD)
}

@BindingAdapter("bindProjectJoiningProgressVisibility")
fun bindProjectJoiningProgressVisibility(
    progressBar: ProgressBar,
    projectJoiningState: ProjectJoiningViewModel.ProjectJoiningState
) {
    progressBar.visibility = when (projectJoiningState) {
        ProjectJoiningViewModel.ProjectJoiningState.None -> View.INVISIBLE
        ProjectJoiningViewModel.ProjectJoiningState.InProgress -> View.VISIBLE
        is ProjectJoiningViewModel.ProjectJoiningState.Error -> View.INVISIBLE
        ProjectJoiningViewModel.ProjectJoiningState.Success -> View.INVISIBLE
    }
}

@BindingAdapter("bindProjectJoiningEditingEnabled")
fun bindProjectJoiningEditingEnabled(
    view: View,
    projectJoiningState: ProjectJoiningViewModel.ProjectJoiningState
) {
    view.isEnabled = when (projectJoiningState) {
        ProjectJoiningViewModel.ProjectJoiningState.None -> true
        ProjectJoiningViewModel.ProjectJoiningState.InProgress -> false
        is ProjectJoiningViewModel.ProjectJoiningState.Error -> true
        ProjectJoiningViewModel.ProjectJoiningState.Success -> false
    }
}

@BindingAdapter("bindProjectJoiningUnknownErrorVisibility")
fun bindProjectJoiningUnknownErrorVisibility(view: TextView, projectJoiningState: ProjectJoiningViewModel.ProjectJoiningState) {
    val errorText: String? = when {
        projectJoiningState.isErrorOfType(ProjectJoiningViewModel.ProjectJoiningErrorType.SERVER) ->
            view.context.getString(R.string.project_joining_screen_error_network)
        projectJoiningState.isErrorOfType(ProjectJoiningViewModel.ProjectJoiningErrorType.UNKNOWN) ->
            view.context.getString(R.string.project_joining_screen_error_unknown)
        else -> null
    }
    view.text = errorText
    view.visibility = if (errorText != null) View.VISIBLE else View.INVISIBLE
}