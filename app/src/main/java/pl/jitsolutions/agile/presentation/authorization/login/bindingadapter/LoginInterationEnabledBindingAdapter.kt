package pl.jitsolutions.agile.presentation.authorization.login.bindingadapter

import android.view.View
import androidx.databinding.BindingAdapter
import pl.jitsolutions.agile.presentation.authorization.login.LoginViewModel

@BindingAdapter("android:enabled")
fun setLoginCredentialsEditing(view: View, loginState: LoginViewModel.LoginState) {
    view.isEnabled = when (loginState) {
        LoginViewModel.LoginState.None -> true
        LoginViewModel.LoginState.InProgress -> false
        is LoginViewModel.LoginState.Error -> true
        LoginViewModel.LoginState.Success -> false
    }
}