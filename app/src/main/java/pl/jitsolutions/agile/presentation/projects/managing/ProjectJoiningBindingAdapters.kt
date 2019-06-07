package pl.jitsolutions.agile.presentation.projects.managing

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import pl.jitsolutions.agile.common.Error
import pl.jitsolutions.agile.R

@BindingAdapter("bindProjectJoiningNameError")
fun bindProjectJoiningNameError(
    view: TextInputLayout,
    state: ProjectJoiningViewModel.State
) {
    val errorText: String? = when {
        state.isErrorOfType(Error.InvalidName) ->
            view.context.getString(R.string.project_joining_screen_error_invalid_project_name)
        state.isErrorOfType(Error.EmptyName) ->
            view.context.getString(R.string.project_joining_screen_error_empty_project_name)
        state.isErrorOfType(Error.DoesNotExist) ->
            view.context.getString(R.string.project_joining_screen_error_project_not_found)
        else -> null
    }
    view.isErrorEnabled = errorText != null
    view.error = errorText
}

@BindingAdapter("bindProjectJoiningPasswordError")
fun bindProjectJoiningPasswordError(
    view: TextInputLayout,
    state: ProjectJoiningViewModel.State
) {
    val errorText: String? = when {
        state.isErrorOfType(Error.EmptyPassword) ->
            view.context.getString(R.string.project_joining_screen_error_empty_password)
        state.isErrorOfType(Error.InvalidPassword) ->
            view.context.getString(R.string.project_joining_screen_error_invalid_password)
        else -> null
    }

    view.isErrorEnabled = errorText != null
    view.error = errorText
}

@BindingAdapter("bindProjectJoiningProgressVisibility")
fun bindProjectJoiningProgressVisibility(
    progressBar: ProgressBar,
    state: ProjectJoiningViewModel.State
) {
    progressBar.visibility = when (state) {
        ProjectJoiningViewModel.State.None -> View.INVISIBLE
        ProjectJoiningViewModel.State.InProgress -> View.VISIBLE
        is ProjectJoiningViewModel.State.Fail -> View.INVISIBLE
        ProjectJoiningViewModel.State.Success -> View.INVISIBLE
    }
}

@BindingAdapter("bindProjectJoiningEditingEnabled")
fun bindProjectJoiningEditingEnabled(
    view: View,
    state: ProjectJoiningViewModel.State
) {
    view.isEnabled = when (state) {
        ProjectJoiningViewModel.State.None -> true
        ProjectJoiningViewModel.State.InProgress -> false
        is ProjectJoiningViewModel.State.Fail -> true
        ProjectJoiningViewModel.State.Success -> false
    }
}

@BindingAdapter("bindProjectJoiningUnknownErrorVisibility")
fun bindProjectJoiningUnknownErrorVisibility(
    view: TextView,
    state: ProjectJoiningViewModel.State
) {
    val errorText: String? = when {
        state.isErrorOfType(Error.Network) ->
            view.context.getString(R.string.project_joining_screen_error_network)
        state.isErrorOfType(Error.Unknown) ->
            view.context.getString(R.string.project_joining_screen_error_unknown)
        else -> null
    }
    view.text = errorText
    view.visibility = if (errorText != null) View.VISIBLE else View.INVISIBLE
}

@BindingAdapter("bindProjectJoiningViewEnabled")
fun bindProjectJoiningViewEnabled(view: View, state: ProjectJoiningViewModel.State) {
    view.isEnabled = state != ProjectJoiningViewModel.State.InProgress &&
        state != ProjectJoiningViewModel.State.Success
}