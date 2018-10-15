package pl.jitsolutions.agile.presentation.login.bindingadapter

import android.view.View
import androidx.databinding.BindingAdapter
import pl.jitsolutions.agile.presentation.login.LoginViewModel

@BindingAdapter("unknownErrorVisibility")
fun setUnknownErrorVisibility(view: View, loginState: LoginViewModel.LoginState) {
    val showError = loginState.isErrorOfType(LoginViewModel.LoginErrorType.SERVER)
    view.visibility = if (showError) View.VISIBLE else View.INVISIBLE
}