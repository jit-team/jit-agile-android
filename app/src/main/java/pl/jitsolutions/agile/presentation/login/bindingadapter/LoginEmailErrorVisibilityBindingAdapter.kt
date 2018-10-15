package pl.jitsolutions.agile.presentation.login.bindingadapter

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import pl.jitsolutions.agile.presentation.login.LoginViewModel

@BindingAdapter("emailErrorVisibility")
fun setEmailErrorVisibility(view: TextInputLayout, loginState: LoginViewModel.LoginState) {
    view.isErrorEnabled = loginState.isErrorOfType(LoginViewModel.LoginErrorType.EMAIL)
}