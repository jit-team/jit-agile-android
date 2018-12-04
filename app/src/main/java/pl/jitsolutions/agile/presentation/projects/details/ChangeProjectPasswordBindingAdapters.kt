package pl.jitsolutions.agile.presentation.projects.details

import android.content.DialogInterface
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import pl.jitsolutions.agile.R
import pl.jitsolutions.agile.common.Error

@BindingAdapter("bindChangeProjectPasswordProgressVisibility")
fun bindChangeProjectPasswordProgressVisibility(
    progressBar: ProgressBar,
    state: ChangeProjectPasswordViewModel.State
) {
    progressBar.visibility = when (state) {
        is ChangeProjectPasswordViewModel.State.Success -> View.GONE
        ChangeProjectPasswordViewModel.State.Idle -> View.GONE
        ChangeProjectPasswordViewModel.State.InProgress -> View.VISIBLE
        is ChangeProjectPasswordViewModel.State.Fail -> View.GONE
    }
}

@BindingAdapter(
    value = ["bindChangeProjectPasswordChangeListener", "bindChangeProjectPasswordDialog"],
    requireAll = true
)
fun bindChangeProjectPasswordChangeListener(
    ignore: View,
    viewModel: ChangeProjectPasswordViewModel?,
    dialog: AlertDialog?
) {
    dialog?.getButton(DialogInterface.BUTTON_POSITIVE)
        ?.setOnClickListener { viewModel?.changePassword() }
}

@BindingAdapter(
    value = ["bindChangeProjectPasswordInteractionEnabled", "bindChangeProjectPasswordDialog"],
    requireAll = true
)
fun bindChangeProjectPasswordInteractionEnabled(
    view: View,
    state: ChangeProjectPasswordViewModel.State,
    dialog: AlertDialog?
) {
    val enabled = state != ChangeProjectPasswordViewModel.State.InProgress
    view.isEnabled = enabled
    dialog?.getButton(DialogInterface.BUTTON_POSITIVE)?.isEnabled = enabled
    dialog?.getButton(DialogInterface.BUTTON_NEGATIVE)?.isEnabled = enabled
}

@BindingAdapter("bindChangeProjectPasswordError")
fun bindChangeProjectPasswordError(
    inputLayout: TextInputLayout,
    state: ChangeProjectPasswordViewModel.State
) {
    if (state is ChangeProjectPasswordViewModel.State.Fail) {
        val errorTextResId = when (state.type) {
            Error.EmptyPassword -> R.string.project_details_screen_change_password_error_empty_password
            Error.Network -> R.string.project_details_screen_error_connection
            else -> R.string.project_details_screen_error_unknown
        }
        with(inputLayout) {
            isErrorEnabled = true
            error = context.getString(errorTextResId)
        }
    }
}
