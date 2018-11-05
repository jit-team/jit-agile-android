package pl.jitsolutions.agile.presentation.projects.managing

import android.view.View
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import pl.jitsolutions.agile.R

@BindingAdapter("bindNewProjectNameError")
fun bindNewProjectNameError(
    view: TextInputLayout,
    newProjectState: NewProjectViewModel.NewProjectState
) {
    val errorText: String? = when {
        newProjectState.isErrorOfType(NewProjectViewModel.NewProjectErrorType.PROJECT_NAME) ->
            view.context.getString(R.string.new_project_screen_error_invalid_project_name)
        newProjectState.isErrorOfType(NewProjectViewModel.NewProjectErrorType.PROJECT_ALREADY_EXIST) ->
            view.context.getString(R.string.new_project_screen_error_project_exist)
        else -> null
    }
    view.isErrorEnabled = errorText != null
    view.error = errorText
}

@BindingAdapter("bindNewProjectPasswordError")
fun bindNewProjectPasswordError(
    view: TextInputLayout,
    newProjectState: NewProjectViewModel.NewProjectState
) {
    view.error = view.resources.getString(R.string.new_project_screen_error_invalid_password)
    view.isErrorEnabled =
        newProjectState.isErrorOfType(NewProjectViewModel.NewProjectErrorType.PASSWORD)
}

@BindingAdapter("bindNewProjectProgressVisibility")
fun bindNewProjectProgressVisibility(
    progressBar: ProgressBar,
    newProjectState: NewProjectViewModel.NewProjectState
) {
    progressBar.visibility = when (newProjectState) {
        NewProjectViewModel.NewProjectState.None -> View.INVISIBLE
        NewProjectViewModel.NewProjectState.InProgress -> View.VISIBLE
        is NewProjectViewModel.NewProjectState.Error -> View.INVISIBLE
        NewProjectViewModel.NewProjectState.Success -> View.INVISIBLE
    }
}

@BindingAdapter("bindNewProjectEditingEnabled")
fun bindNewProjectEditingEnabled(view: View, newProjectState: NewProjectViewModel.NewProjectState) {
    view.isEnabled = when (newProjectState) {
        NewProjectViewModel.NewProjectState.None -> true
        NewProjectViewModel.NewProjectState.InProgress -> false
        is NewProjectViewModel.NewProjectState.Error -> true
        NewProjectViewModel.NewProjectState.Success -> false
    }
}