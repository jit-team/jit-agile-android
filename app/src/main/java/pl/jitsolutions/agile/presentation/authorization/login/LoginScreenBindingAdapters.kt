package pl.jitsolutions.agile.presentation.authorization.login

import android.view.View
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import pl.jitsolutions.agile.R

@BindingAdapter("bind_loginScreen_emailError")
fun bind_loginScreen_emailError(view: TextInputLayout, loginState: LoginViewModel.LoginState) {
    view.error = view.resources.getString(R.string.login_screen_error_email)
    view.isErrorEnabled = loginState.isErrorOfType(LoginViewModel.LoginErrorType.EMAIL)
}

@BindingAdapter("bind_loginScreen_passwordError")
fun bind_loginScreen_passwordError(view: TextInputLayout, loginState: LoginViewModel.LoginState) {
    view.error = view.resources.getString(R.string.login_screen_error_password)
    view.isErrorEnabled = loginState.isErrorOfType(LoginViewModel.LoginErrorType.PASSWORD)
}

@BindingAdapter("bind_loginScreen_credentialsEditingEnabled")
fun bind_loginScreen_credentialsEditingEnabled(view: View, loginState: LoginViewModel.LoginState) {
    view.isEnabled = when (loginState) {
        LoginViewModel.LoginState.None -> true
        LoginViewModel.LoginState.InProgress -> false
        is LoginViewModel.LoginState.Error -> true
        LoginViewModel.LoginState.Success -> false
    }
}

@BindingAdapter("bind_loginScreen_progressVisibility")
fun bind_loginScreen_progressVisibility(progressBar: ProgressBar, loginState: LoginViewModel.LoginState) {
    progressBar.visibility = when (loginState) {
        LoginViewModel.LoginState.None -> View.INVISIBLE
        LoginViewModel.LoginState.InProgress -> View.VISIBLE
        is LoginViewModel.LoginState.Error -> View.INVISIBLE
        LoginViewModel.LoginState.Success -> View.INVISIBLE
    }
}

@BindingAdapter("bind_loginScreen_unknownErrorVisibility")
fun bind_loginScreen_unknownErrorVisibility(view: View, loginState: LoginViewModel.LoginState) {
    val showError = loginState.isErrorOfType(LoginViewModel.LoginErrorType.SERVER)
    view.visibility = if (showError) View.VISIBLE else View.INVISIBLE
}
