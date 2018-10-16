package pl.jitsolutions.agile.presentation.authorization.login.bindingadapter

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import pl.jitsolutions.agile.R
import pl.jitsolutions.agile.presentation.authorization.login.LoginViewModel

@BindingAdapter("passwordErrorText")
fun setPasswordErrorText(view: TextInputLayout, loginState: LoginViewModel.LoginState) {
    view.error = view.resources.getString(R.string.login_screen_error_password)
}