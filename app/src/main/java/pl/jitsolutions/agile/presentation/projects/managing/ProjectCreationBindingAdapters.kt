package pl.jitsolutions.agile.presentation.projects.managing

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import pl.jitsolutions.agile.common.Error
import pl.jitsolutions.agile.R
import pl.jitsolutions.agile.presentation.authorization.registration.RegistrationViewModel

@BindingAdapter("bindProjectCreationNameError")
fun bindProjectCreationNameError(
    view: TextInputLayout,
    state: ProjectCreationViewModel.State
) {
    val errorText: String? = when {
        state.isErrorOfType(Error.InvalidName) ->
            view.context.getString(R.string.project_creation_screen_error_invalid_project_name)
        state.isErrorOfType(Error.Exists) ->
            view.context.getString(R.string.project_creation_screen_error_project_already_exist)
        state.isErrorOfType(Error.EmptyName) ->
            view.context.getString(R.string.project_creation_screen_error_empty_project_name)
        else -> null
    }
    view.isErrorEnabled = errorText != null
    view.error = errorText
}

@BindingAdapter("bindProjectCreationPasswordError")
fun bindProjectCreationPasswordError(
    view: TextInputLayout,
    state: ProjectCreationViewModel.State
) {
    val errorText: String? = when {
        state.isErrorOfType(Error.InvalidPassword) ->
            view.context.getString(R.string.project_creation_screen_error_invalid_password)
        state.isErrorOfType(Error.EmptyPassword) ->
            view.context.getString(R.string.project_creation_screen_error_empty_password)
        state.isErrorOfType(Error.EmptyPassword) ->
            view.context.getString(R.string.project_creation_screen_error_weak_password)
        else -> null
    }
    view.error = errorText
    view.isErrorEnabled = errorText != null
}

@BindingAdapter("bindProjectCreationProgressVisibility")
fun bindProjectCreationProgressVisibility(
    progressBar: ProgressBar,
    state: ProjectCreationViewModel.State
) {
    progressBar.visibility = when (state) {
        ProjectCreationViewModel.State.None -> View.INVISIBLE
        ProjectCreationViewModel.State.InProgress -> View.VISIBLE
        is ProjectCreationViewModel.State.Fail -> View.INVISIBLE
        ProjectCreationViewModel.State.Success -> View.INVISIBLE
    }
}

@BindingAdapter("bindProjectCreationEditingEnabled")
fun bindProjectCreationEditingEnabled(
    view: View,
    state: ProjectCreationViewModel.State
) {
    view.isEnabled = when (state) {
        ProjectCreationViewModel.State.None -> true
        ProjectCreationViewModel.State.InProgress -> false
        is ProjectCreationViewModel.State.Fail -> true
        ProjectCreationViewModel.State.Success -> false
    }
}

@BindingAdapter("bindProjectCreationUnknownErrorVisibility")
fun bindProjectCreationUnknownErrorVisibility(
    view: TextView,
    state: ProjectCreationViewModel.State
) {
    val errorText: String? = when {
        state.isErrorOfType(Error.Network) ->
            view.context.getString(R.string.project_creation_screen_error_network)
        state.isErrorOfType(Error.Unknown) ->
            view.context.getString(R.string.project_creation_screen_error_unknown)
        else -> null
    }
    view.text = errorText
    view.visibility = if (errorText != null) View.VISIBLE else View.INVISIBLE
}

@BindingAdapter("bindProjectCreationViewEnabled")
fun bindProjectCreationViewEnabled(view: View, state: ProjectCreationViewModel.State) {
    view.isEnabled = state != ProjectCreationViewModel.State.InProgress &&
        state != ProjectCreationViewModel.State.Success
}