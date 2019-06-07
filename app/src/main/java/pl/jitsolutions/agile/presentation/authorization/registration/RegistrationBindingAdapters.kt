package pl.jitsolutions.agile.presentation.authorization.registration

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import pl.jitsolutions.agile.R
import pl.jitsolutions.agile.common.Error

@BindingAdapter("bindRegistrationUsernameError")
fun bindRegistrationUsernameError(
    view: TextInputLayout,
    state: RegistrationViewModel.State
) {
    val errorText: String? = when {
        state.isErrorOfType(Error.InvalidName) ->
            view.context.getString(R.string.registration_screen_error_invalid_username)
        state.isErrorOfType(Error.EmptyName) ->
            view.context.getString(R.string.registration_screen_error_empty_username)
        else -> null
    }

    view.isErrorEnabled = errorText != null
    view.error = errorText
}

@BindingAdapter("bindRegistrationEmailError")
fun bindRegistrationEmailError(
    view: TextInputLayout,
    state: RegistrationViewModel.State
) {
    val errorText: String? = when {
        state.isErrorOfType(Error.InvalidEmail) ->
            view.context.getString(R.string.registration_screen_error_invalid_email)
        state.isErrorOfType(Error.Exists) ->
            view.context.getString(R.string.registration_screen_error_user_exists)
        state.isErrorOfType(Error.EmptyEmail) ->
            view.context.getString(R.string.registration_screen_error_empty_email)
        else -> null
    }
    view.isErrorEnabled = errorText != null
    view.error = errorText
}

@BindingAdapter("bindRegistrationPasswordError")
fun bindRegistrationPasswordError(
    view: TextInputLayout,
    state: RegistrationViewModel.State
) {
    val errorText: String? = when {
        state.isErrorOfType(Error.InvalidPassword) ->
            view.context.getString(R.string.registration_screen_error_invalid_password)
        state.isErrorOfType(Error.EmptyPassword) ->
            view.context.getString(R.string.registration_screen_error_empty_password)
        state.isErrorOfType(Error.WeakPassword) ->
            view.context.getString(R.string.registration_screen_error_weak_password)
        else -> null
    }
    view.isErrorEnabled = errorText != null
    view.error = errorText
}

@BindingAdapter("bindRegistrationProgressVisibility")
fun bindRegistrationProgressVisibility(
    progressBar: ProgressBar,
    state: RegistrationViewModel.State
) {
    progressBar.visibility = when (state) {
        RegistrationViewModel.State.None -> View.INVISIBLE
        RegistrationViewModel.State.InProgress -> View.VISIBLE
        is RegistrationViewModel.State.Fail -> View.INVISIBLE
        RegistrationViewModel.State.Success -> View.INVISIBLE
    }
}

@BindingAdapter("bindRegistrationUnknownErrorVisibility")
fun bindRegistrationUnknownErrorVisibility(
    view: TextView,
    state: RegistrationViewModel.State
) {
    val errorText: String? = when {
        state.isErrorOfType(Error.Network) ->
            view.context.getString(R.string.registration_screen_error_network)
        state.isErrorOfType(Error.Unknown) ->
            view.context.getString(R.string.registration_screen_error_unknown)
        else -> null
    }
    view.text = errorText
    view.visibility = if (errorText != null) View.VISIBLE else View.INVISIBLE
}

@BindingAdapter("bindRegistrationViewEnabled")
fun bindRegistrationViewEnabled(view: View, state: RegistrationViewModel.State) {
    view.isEnabled = state != RegistrationViewModel.State.InProgress &&
        state != RegistrationViewModel.State.Success
}