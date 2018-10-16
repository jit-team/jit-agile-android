package pl.jitsolutions.agile.presentation.authorization.login.bindingadapter

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import pl.jitsolutions.agile.presentation.authorization.login.LoginViewModel

@BindingAdapter("passwordErrorVisibility")
fun setPasswordErrorVisibility(view: TextInputLayout, loginState: LoginViewModel.LoginState) {
    view.isErrorEnabled = loginState.isErrorOfType(LoginViewModel.LoginErrorType.PASSWORD)
}