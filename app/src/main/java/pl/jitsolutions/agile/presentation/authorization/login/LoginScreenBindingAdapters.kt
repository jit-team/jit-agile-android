package pl.jitsolutions.agile.presentation.authorization.login

import android.view.View
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import pl.jitsolutions.agile.R

@BindingAdapter("bindLoginEmailError")
fun bindLoginEmailError(view: TextInputLayout, loginState: LoginViewModel.LoginState) {
    view.error = view.resources.getString(R.string.login_screen_error_invalid_email)
    view.isErrorEnabled = loginState.isErrorOfType(LoginViewModel.LoginErrorType.EMAIL)
}

@BindingAdapter("bindLoginPasswordError")
fun bindLoginPasswordError(view: TextInputLayout, loginState: LoginViewModel.LoginState) {
    view.error = view.resources.getString(R.string.login_screen_error_invalid_password)
    view.isErrorEnabled = loginState.isErrorOfType(LoginViewModel.LoginErrorType.PASSWORD)
}

@BindingAdapter("bindLoginCredentialsEditingEnabled")
fun bindLoginCredentialsEditingEnabled(view: View, loginState: LoginViewModel.LoginState) {
    view.isEnabled = when (loginState) {
        LoginViewModel.LoginState.None -> true
        LoginViewModel.LoginState.InProgress -> false
        is LoginViewModel.LoginState.Error -> true
        LoginViewModel.LoginState.Success -> false
    }
}

@BindingAdapter("bindLoginProgressVisibility")
fun bindLoginProgressVisibility(progressBar: ProgressBar, loginState: LoginViewModel.LoginState) {
    progressBar.visibility = when (loginState) {
        LoginViewModel.LoginState.None -> View.INVISIBLE
        LoginViewModel.LoginState.InProgress -> View.VISIBLE
        is LoginViewModel.LoginState.Error -> View.INVISIBLE
        LoginViewModel.LoginState.Success -> View.INVISIBLE
    }
}

@BindingAdapter("bindLoginUnknownErrorVisibility")
fun bindLoginUnknownErrorVisibility(view: View, loginState: LoginViewModel.LoginState) {
    val showError = loginState.isErrorOfType(LoginViewModel.LoginErrorType.SERVER)
    view.visibility = if (showError) View.VISIBLE else View.INVISIBLE
}
