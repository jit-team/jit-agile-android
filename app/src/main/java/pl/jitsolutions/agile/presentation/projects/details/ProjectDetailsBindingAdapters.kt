package pl.jitsolutions.agile.presentation.projects.details

import android.content.ContextWrapper
import android.content.DialogInterface
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import pl.jitsolutions.agile.R
import pl.jitsolutions.agile.common.Error
import pl.jitsolutions.agile.domain.User

private var changePasswordDialog: AlertDialog? = null

@BindingAdapter("bindProjectDetailsBackArrowVisibility")
fun bindProjectDetailsBackArrowVisibility(view: View, bind: Boolean) {
    if (bind) {
        val wrapper = view.context as? ContextWrapper
        val activity = wrapper?.baseContext as? AppCompatActivity
        activity?.apply {
            setSupportActionBar(view as? Toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }
}

@BindingAdapter("bindProjectDetailsListOfUsers")
fun bindProjectDetailsListOfUsers(recyclerView: RecyclerView, users: List<User>?) {
    val adapter: ProjectDetailsUserAdapter
    if (recyclerView.adapter == null) {
        adapter = ProjectDetailsUserAdapter()
        recyclerView.adapter = adapter
    } else {
        adapter = recyclerView.adapter as ProjectDetailsUserAdapter
    }
    adapter.submitList(users ?: emptyList())
}

@BindingAdapter("bindProjectDetailsProgressVisibility")
fun bindProjectDetailsProgressVisibility(
    progressBar: ProgressBar,
    state: ProjectDetailsViewModel.State
) {
    progressBar.visibility = when (state) {
        is ProjectDetailsViewModel.State.Success -> View.INVISIBLE
        ProjectDetailsViewModel.State.Idle -> View.INVISIBLE
        ProjectDetailsViewModel.State.InProgress -> View.VISIBLE
        ProjectDetailsViewModel.State.Empty -> View.INVISIBLE
        is ProjectDetailsViewModel.State.Fail -> View.INVISIBLE
    }

    changePasswordDialog?.let {
        val changePasswordProgress =
            changePasswordDialog!!.findViewById<View>(R.id.change_password_progress)
        changePasswordProgress!!.visibility = when (state) {
            is ProjectDetailsViewModel.State.Success -> View.GONE
            ProjectDetailsViewModel.State.Idle -> View.GONE
            ProjectDetailsViewModel.State.InProgress -> View.VISIBLE
            ProjectDetailsViewModel.State.Empty -> View.GONE
            is ProjectDetailsViewModel.State.Fail -> View.GONE
        }

        val changePasswordEditText =
            changePasswordDialog!!.findViewById<TextInputLayout>(R.id.change_password_layout_new_password)!!.editText
        val buttonEnabled = when (state) {
            ProjectDetailsViewModel.State.InProgress -> false
            else -> true
        }

        changePasswordEditText!!.isEnabled = buttonEnabled
        it.getButton(DialogInterface.BUTTON_POSITIVE).isEnabled = buttonEnabled
        it.getButton(DialogInterface.BUTTON_NEGATIVE).isEnabled = buttonEnabled
    }
}

@BindingAdapter("bindProjectDetailsEmptyState")
fun bindProjectDetailsEmptyState(view: View, state: ProjectDetailsViewModel.State) {
    view.visibility = when (state) {
        is ProjectDetailsViewModel.State.Success -> View.INVISIBLE
        ProjectDetailsViewModel.State.Idle -> View.INVISIBLE
        ProjectDetailsViewModel.State.InProgress -> View.INVISIBLE
        ProjectDetailsViewModel.State.Empty -> View.VISIBLE
        is ProjectDetailsViewModel.State.Fail -> View.INVISIBLE
    }
}

@BindingAdapter("bindProjectDetailsErrorState")
fun bindProjectDetailsErrorState(view: View, state: ProjectDetailsViewModel.State) {
    if (changePasswordDialog != null || state !is ProjectDetailsViewModel.State.Fail) {
        return
    }

    val messageResId = when {
        state.isErrorOfType(Error.Network) ->
            R.string.project_details_screen_error_connection
        state.isErrorOfType(Error.Unknown) ->
            R.string.project_details_screen_error_project_not_found
        else -> R.string.project_details_screen_error_unknown
    }
    Toast.makeText(view.context.applicationContext, messageResId, Toast.LENGTH_SHORT).show()
}

@BindingAdapter("bindProjectDetailsMenuItemSelected", "bindProjectDetailsViewModel")
fun bindProjectDetailsMenuItemListener(
    view: View,
    menuItemId: Int,
    viewModel: ProjectDetailsViewModel
) {
    when (menuItemId) {
        R.id.menu_project_details_leave_project ->
            view.showLeaveConfirmation { viewModel.leaveProject() }
        R.id.menu_project_details_delete_project ->
            view.showDeleteConfirmation { viewModel.deleteProject() }
        R.id.menu_project_details_show_project_password ->
            view.showPassword(viewModel.project.value?.password!!)
        R.id.menu_project_details_change_project_password -> {
            view.showChangePasswordDialog { newPassword ->
                viewModel.changePassword(newPassword)
            }
        }
    }
}

@BindingAdapter("bindProjectDetailsFab")
fun bindProjectListFab(fab: FloatingActionButton, state: ProjectDetailsViewModel.State) {
    when (state) {
        is ProjectDetailsViewModel.State.Success -> fab.show()
        ProjectDetailsViewModel.State.Empty -> fab.show()
        else -> fab.hide()
    }
}

@BindingAdapter("bindProjectDetailsChangePasswordError")
fun bindProjectDetailsChangePasswordError(view: View, state: ProjectDetailsViewModel.State) {
    changePasswordDialog?.let {
        when {
            state is ProjectDetailsViewModel.State.Fail -> {
                val errorTextResId = when (state.type) {
                    Error.EmptyPassword -> R.string.project_details_screen_change_password_error_empty_password
                    Error.Network -> R.string.project_details_screen_error_connection
                    else -> R.string.project_details_screen_error_unknown
                }
                changePasswordDialog?.findViewById<TextInputLayout>(R.id.change_password_layout_new_password)
                    ?.apply {
                        isErrorEnabled = true
                        error = context.getString(errorTextResId)
                    }
            }
            state.isSuccessOfType(ProjectDetailsViewModel.SuccessType.CHANGE_PASSWORD) -> {
                changePasswordDialog?.dismiss()
                Toast.makeText(
                    view.context,
                    R.string.project_details_screen_change_password_success_message,
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> {
                //Do nothing
            }
        }

    }
}

private fun View.showLeaveConfirmation(onLeaveListener: () -> Unit) {
    AlertDialog.Builder(context)
        .setTitle(R.string.project_details_screen_leave_confirmation_title)
        .setMessage(R.string.project_details_screen_leave_confirmation_message)
        .setPositiveButton(R.string.project_details_screen_leave_confirmation_positive_button_text) { _, _ ->
            onLeaveListener.invoke()
        }
        .setNegativeButton(
            R.string.project_details_screen_leave_confirmation_negative_button_text,
            null
        )
        .setCancelable(true)
        .show()
}

private fun View.showDeleteConfirmation(onDeleteListener: () -> Unit) {
    AlertDialog.Builder(context)
        .setTitle(R.string.project_details_screen_delete_confirmation_title)
        .setMessage(R.string.project_details_screen_delete_confirmation_message)
        .setPositiveButton(R.string.project_details_screen_delete_confirmation_positive_button_text) { _, _ ->
            onDeleteListener.invoke()
        }
        .setNegativeButton(
            R.string.project_details_screen_delete_confirmation_negative_button_text,
            null
        )
        .setCancelable(true)
        .show()
}

private fun View.showPassword(password: String) {
    val format = context.getString(R.string.project_details_screen_show_password_title)
    val message = String.format(format, password)
    AlertDialog.Builder(context)
        .setTitle(message)
        .setPositiveButton(R.string.project_details_screen_show_password_positive_button_text) { _, _ -> }
        .setCancelable(true)
        .show()
}

private fun View.showChangePasswordDialog(onChangePasswordListener: (String) -> Unit) {
    if (changePasswordDialog != null) {
        return
    }

    changePasswordDialog = AlertDialog.Builder(context)
        .setTitle(R.string.project_details_screen_change_password_title)
        .setView(R.layout.dialog_project_details_change_password)
        .setPositiveButton(
            R.string.project_details_screen_change_password_positive_button_text,
            null
        )
        .setNegativeButton(
            R.string.project_details_screen_change_password_negative_button_text,
            null
        )
        .setCancelable(false)
        .setOnDismissListener { changePasswordDialog = null }
        .show()

    with(changePasswordDialog!!) {
        getButton(DialogInterface.BUTTON_POSITIVE)
            ?.setOnClickListener {
                val newPasswordEditText =
                    changePasswordDialog!!.findViewById<EditText>(R.id.change_password_new_password)
                onChangePasswordListener.invoke(newPasswordEditText?.text.toString())
            }

        findViewById<TextInputLayout>(R.id.change_password_layout_new_password)
            ?.apply {
                editText?.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        isErrorEnabled = false
                        error = ""
                    }

                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                        //ignore
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        //ignore
                    }
                })
            }

        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
    }
}
