package pl.jitsolutions.agile.presentation.login.bindingadapter

import android.view.View
import androidx.databinding.BindingAdapter
import pl.jitsolutions.agile.presentation.login.LoginViewModel

// TODO: change name
@BindingAdapter("loginButtonVisibility")
fun setLoginButtonVisibility(view: View, loginState: LoginViewModel.LoginState) {
    view.visibility = when (loginState) {
        LoginViewModel.LoginState.None -> View.VISIBLE
        LoginViewModel.LoginState.InProgress -> View.INVISIBLE
        LoginViewModel.LoginState.Error -> View.VISIBLE
        LoginViewModel.LoginState.Success -> View.VISIBLE
    }
}