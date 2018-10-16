package pl.jitsolutions.agile.presentation.authorization.login.bindingadapter

import android.view.View
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import pl.jitsolutions.agile.presentation.authorization.login.LoginViewModel

@BindingAdapter("loginProgressVisibility")
fun setLoginProgressVisibility(progressBar: ProgressBar, loginState: LoginViewModel.LoginState) {
    progressBar.visibility = when (loginState) {
        LoginViewModel.LoginState.None -> View.INVISIBLE
        LoginViewModel.LoginState.InProgress -> View.VISIBLE
        is LoginViewModel.LoginState.Error -> View.INVISIBLE
        LoginViewModel.LoginState.Success -> View.INVISIBLE
    }
}