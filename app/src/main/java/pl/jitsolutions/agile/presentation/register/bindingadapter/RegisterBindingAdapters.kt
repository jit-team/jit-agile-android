package pl.jitsolutions.agile.presentation.register.bindingadapter

import android.view.View
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import pl.jitsolutions.agile.R
import pl.jitsolutions.agile.presentation.authorization.register.RegisterViewModel

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

@BindingAdapter("bindRegisterProgressVisibility")
fun registerProgressVisibilityBindingAdapter(progressBar: ProgressBar, registerState: RegisterViewModel.RegisterState) {
    progressBar.visibility = when (registerState) {
        RegisterViewModel.RegisterState.None -> View.INVISIBLE
        RegisterViewModel.RegisterState.InProgress -> View.VISIBLE
        is RegisterViewModel.RegisterState.Error -> View.INVISIBLE
        RegisterViewModel.RegisterState.Success -> View.INVISIBLE
    }
}

@BindingAdapter("android:enabled")
fun registerCredentialsEditing(view: View, registerState: RegisterViewModel.RegisterState) {
    view.isEnabled = when (registerState) {
        RegisterViewModel.RegisterState.None -> true
        RegisterViewModel.RegisterState.InProgress -> false
        is RegisterViewModel.RegisterState.Error -> true
        RegisterViewModel.RegisterState.Success -> false
    }
}
