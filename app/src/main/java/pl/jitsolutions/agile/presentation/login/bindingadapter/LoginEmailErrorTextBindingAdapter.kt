package pl.jitsolutions.agile.presentation.login.bindingadapter

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import pl.jitsolutions.agile.R
import pl.jitsolutions.agile.presentation.login.LoginViewModel

@BindingAdapter("emailErrorText")
fun setEmailErrorText(view: TextInputLayout, loginState: LoginViewModel.LoginState) {
    view.error = view.resources.getString(R.string.login_screen_error_email)
}