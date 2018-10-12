package pl.jitsolutions.agile.presentation.login.bindingadapter

import android.view.View
import androidx.databinding.BindingAdapter
import pl.jitsolutions.agile.presentation.login.LoginViewModel

@BindingAdapter("loginErrorVisibility")
fun setLoginErrorVisibility(view: View, loginState: LoginViewModel.LoginState) {
    view.visibility = when (loginState) {
        LoginViewModel.LoginState.None -> View.INVISIBLE
        LoginViewModel.LoginState.InProgress -> View.INVISIBLE
        LoginViewModel.LoginState.Error -> View.VISIBLE
        LoginViewModel.LoginState.Success -> View.INVISIBLE
    }
}