package pl.jitsolutions.agile.presentation.authorization.registration

import android.content.ContextWrapper
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import pl.jitsolutions.agile.R

@BindingAdapter("bindRegistrationUsernameError")
fun registrationUsernameErrorBindingAdapter(view: TextInputLayout, registrationState: RegistrationViewModel.RegistrationState) {
    val error = registrationState.isErrorOfType(RegistrationViewModel.RegisterTypeError.USERNAME)
    view.isErrorEnabled = error
    view.error = if (error) view.context.getString(R.string.registration_screen_error_invalid_username) else null
}

@BindingAdapter("bindRegistrationEmailError")
fun registrationEmailErrorBindingAdapter(view: TextInputLayout, registrationState: RegistrationViewModel.RegistrationState) {
    val error = registrationState.isErrorOfType(RegistrationViewModel.RegisterTypeError.EMAIL)
    view.isErrorEnabled = error
    view.error = if (error) view.context.getString(R.string.registration_screen_error_invalid_email) else null

}

@BindingAdapter("bindRegistrationPasswordError")
fun registrationPasswordErrorBindingAdapter(view: TextInputLayout, registrationState: RegistrationViewModel.RegistrationState) {
    val error = registrationState.isErrorOfType(RegistrationViewModel.RegisterTypeError.PASSWORD)
    view.isErrorEnabled = error
    view.error = if (error) view.context.getString(R.string.registration_screen_error_invalid_password) else null
}

@BindingAdapter("bindRegistrationProgressVisibility")
fun registrationProgressVisibilityBindingAdapter(progressBar: ProgressBar, registrationState: RegistrationViewModel.RegistrationState) {
    progressBar.visibility = when (registrationState) {
        RegistrationViewModel.RegistrationState.None -> View.INVISIBLE
        RegistrationViewModel.RegistrationState.InProgress -> View.VISIBLE
        is RegistrationViewModel.RegistrationState.Error -> View.INVISIBLE
        RegistrationViewModel.RegistrationState.Success -> View.INVISIBLE
    }
}

@BindingAdapter("android:enabled")
fun registrationCredentialsEditing(view: View, registrationState: RegistrationViewModel.RegistrationState) {
    view.isEnabled = when (registrationState) {
        RegistrationViewModel.RegistrationState.None -> true
        RegistrationViewModel.RegistrationState.InProgress -> false
        is RegistrationViewModel.RegistrationState.Error -> true
        RegistrationViewModel.RegistrationState.Success -> false
    }
}

@BindingAdapter("bindRegistrationBackArrowVisibility")
fun registrationBackArrowVisibilityBindingAdapter(view : View, bind: Boolean) {
    if (bind) {
        val wrapper = view.context as? ContextWrapper
        val activity = wrapper?.baseContext as? AppCompatActivity
        activity?.apply {
            setSupportActionBar(view as? Toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }
}