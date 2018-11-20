package pl.jitsolutions.agile.presentation.authorization.login

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import pl.jitsolutions.agile.R

@BindingAdapter("bindLoginEmailError")
fun bindLoginEmailError(view: TextInputLayout, loginState: LoginViewModel.LoginState) {
    val errorText: String? = when {
        loginState.isErrorOfType(LoginViewModel.LoginErrorType.EMAIL) ->
            view.context.getString(R.string.login_screen_error_invalid_email)
        loginState.isErrorOfType(LoginViewModel.LoginErrorType.EMAIL_NOT_FOUND) ->
            view.context.getString(R.string.login_screen_error_email_not_found)
        else -> null
    }
    view.isErrorEnabled = errorText != null
    view.error = errorText
}

@BindingAdapter("bindLoginPasswordError")
fun bindLoginPasswordError(view: TextInputLayout, loginState: LoginViewModel.LoginState) {
    view.error = view.resources.getString(R.string.login_screen_error_invalid_password)
    view.isErrorEnabled = loginState.isErrorOfType(LoginViewModel.LoginErrorType.PASSWORD)
}

@BindingAdapter("bindLoginProgressVisibility")
fun bindLoginProgressVisibility(progressBar: ProgressBar, loginState: LoginViewModel.LoginState) {
    progressBar.visibility = when (loginState) {
        LoginViewModel.LoginState.None -> View.INVISIBLE
        LoginViewModel.LoginState.InProgress -> View.VISIBLE
        is LoginViewModel.LoginState.Fail -> View.INVISIBLE
        LoginViewModel.LoginState.Success -> View.INVISIBLE
    }
}

@BindingAdapter("bindLoginUnknownErrorVisibility")
fun bindLoginUnknownErrorVisibility(view: TextView, loginState: LoginViewModel.LoginState) {
    val errorText: String? = when {
        loginState.isErrorOfType(LoginViewModel.LoginErrorType.SERVER) ->
            view.context.getString(R.string.login_screen_error_network)
        loginState.isErrorOfType(LoginViewModel.LoginErrorType.UNKNOWN) ->
            view.context.getString(R.string.login_screen_error_unknown)
        else -> null
    }
    view.text = errorText
    view.visibility = if (errorText != null) View.VISIBLE else View.INVISIBLE
}

@BindingAdapter("bindLoginViewEnabled")
fun bindLoginViewEnabled(view: View, loginState: LoginViewModel.LoginState) {
    view.isEnabled = loginState != LoginViewModel.LoginState.InProgress ||
        loginState != LoginViewModel.LoginState.Success
}