package pl.jitsolutions.agile.presentation.authorization.login

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import pl.jitsolutions.agile.common.Error
import pl.jitsolutions.agile.R

@BindingAdapter("bindLoginEmailError")
fun bindLoginEmailError(view: TextInputLayout, state: LoginViewModel.State) {
    val errorText: String? = when {
        state.isErrorOfType(Error.InvalidEmail) ->
            view.context.getString(R.string.login_screen_error_invalid_email)
        state.isErrorOfType(Error.DoesNotExist) ->
            view.context.getString(R.string.login_screen_error_invalid_email)
        state.isErrorOfType(Error.EmptyEmail) ->
            view.context.getString(R.string.login_screen_error_empty_email)
        else -> null
    }
    view.isErrorEnabled = errorText != null
    view.error = errorText
}

@BindingAdapter("bindLoginPasswordError")
fun bindLoginPasswordError(view: TextInputLayout, state: LoginViewModel.State) {
    val errorText: String? = when {
        state.isErrorOfType(Error.InvalidPassword) ->
            view.context.getString(R.string.login_screen_error_invalid_password)
        state.isErrorOfType(Error.EmptyPassword) ->
            view.context.getString(R.string.login_screen_error_empty_password)
        else -> null
    }

    view.isErrorEnabled = errorText != null
    view.error = errorText
}

@BindingAdapter("bindLoginProgressVisibility")
fun bindLoginProgressVisibility(progressBar: ProgressBar, state: LoginViewModel.State) {
    progressBar.visibility = when (state) {
        LoginViewModel.State.None -> View.INVISIBLE
        LoginViewModel.State.InProgress -> View.VISIBLE
        is LoginViewModel.State.Fail -> View.INVISIBLE
        LoginViewModel.State.Success -> View.INVISIBLE
    }
}

@BindingAdapter("bindLoginUnknownErrorVisibility")
fun bindLoginUnknownErrorVisibility(view: TextView, state: LoginViewModel.State) {
    val errorText: String? = when {
        state.isErrorOfType(Error.Network) ->
            view.context.getString(R.string.login_screen_error_network)
        state.isErrorOfType(Error.Unknown) ->
            view.context.getString(R.string.login_screen_error_unknown)
        else -> null
    }
    view.text = errorText
    view.visibility = if (errorText != null) View.VISIBLE else View.INVISIBLE
}

@BindingAdapter("bindLoginViewEnabled")
fun bindLoginViewEnabled(view: View, state: LoginViewModel.State) {
    view.isEnabled = state != LoginViewModel.State.InProgress &&
        state != LoginViewModel.State.Success
}