package pl.jitsolutions.agile.presentation.authorization.resetPassword

import android.content.ContextWrapper
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import pl.jitsolutions.agile.R

@BindingAdapter("bindResetPasswordBackArrowVisibility")
fun resetPasswordBackArrowVisibility(view: View, bind: Boolean) {
    if (bind) {
        val wrapper = view.context as? ContextWrapper
        val activity = wrapper?.baseContext as? AppCompatActivity
        activity?.apply {
            setSupportActionBar(view as? Toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }
}

@BindingAdapter("bindResetPasswordViewEnabled")
fun resetPasswordCredentialsEditingEnabled(
    view: View,
    state: ResetPasswordViewModel.State
) {
    view.isEnabled = state != ResetPasswordViewModel.State.InProgress &&
        state != ResetPasswordViewModel.State.Success
}

@BindingAdapter("bindResetPasswordEmailError")
fun resetPasswordEmailError(
    view: TextInputLayout,
    state: ResetPasswordViewModel.State
) {
    val errorText: String? = when {
        state.isErrorOfType(ResetPasswordViewModel.ResetPasswordTypeError.EMAIL) ->
            view.context.getString(R.string.reset_password_screen_error_invalid_email)
        state.isErrorOfType(ResetPasswordViewModel.ResetPasswordTypeError.EMAIL_NOT_FOUND) ->
            view.context.getString(R.string.reset_password_screen_error_email_not_found)
        else -> null
    }
    view.isErrorEnabled = errorText != null
    view.error = errorText
}

@BindingAdapter("bindResetPasswordProgressVisibility")
fun resetPasswordProgressVisibility(
    progressBar: ProgressBar,
    state: ResetPasswordViewModel.State
) {
    progressBar.visibility = when (state) {
        ResetPasswordViewModel.State.None -> View.INVISIBLE
        ResetPasswordViewModel.State.InProgress -> View.VISIBLE
        is ResetPasswordViewModel.State.Fail -> View.INVISIBLE
        ResetPasswordViewModel.State.Success -> View.INVISIBLE
    }
}

@BindingAdapter("bindResetPasswordUnknownErrorVisibility")
fun bindResetPasswordUnknownErrorVisibility(
    view: TextView,
    state: ResetPasswordViewModel.State
) {
    val errorText: String? = when {
        state.isErrorOfType(ResetPasswordViewModel.ResetPasswordTypeError.SERVER) ->
            view.context.getString(R.string.reset_password_screen_error_network)
        state.isErrorOfType(ResetPasswordViewModel.ResetPasswordTypeError.UNKNOWN) ->
            view.context.getString(R.string.reset_password_screen_error_unknown)
        else -> null
    }
    view.text = errorText
    view.visibility = if (errorText != null) View.VISIBLE else View.INVISIBLE
}
