package pl.jitsolutions.agile.presentation.register.bindingadapter

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import pl.jitsolutions.agile.R
import pl.jitsolutions.agile.presentation.register.RegisterViewModel

@BindingAdapter("bindRegisterUsernameError")
fun registerUsernameErrorBindingAdapter(view: TextInputLayout, registerState: RegisterViewModel.RegisterState) {
    val error = registerState.isErrorOfType(RegisterViewModel.RegisterTypeError.USERNAME)
    view.isErrorEnabled = error
    view.error = if (error) view.context.getString(R.string.register_invalid_username_text) else null
}

@BindingAdapter("bindRegisterEmailError")
fun registerEmailErrorBindingAdapter(view: TextInputLayout, registerState: RegisterViewModel.RegisterState) {
    val error = registerState.isErrorOfType(RegisterViewModel.RegisterTypeError.EMAIL)
    view.isErrorEnabled = error
    view.error = if (error) view.context.getString(R.string.register_invalid_email_text) else null

}

@BindingAdapter("bindRegisterPasswordError")
fun registerPasswordErrorBindingAdapter(view: TextInputLayout, registerState: RegisterViewModel.RegisterState) {
    val error = registerState.isErrorOfType(RegisterViewModel.RegisterTypeError.PASSWORD)
    view.isErrorEnabled = error
    view.error = if (error) view.context.getString(R.string.register_invalid_username_text) else null
}

