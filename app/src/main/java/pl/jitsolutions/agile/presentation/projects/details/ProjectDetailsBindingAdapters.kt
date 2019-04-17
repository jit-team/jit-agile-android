package pl.jitsolutions.agile.presentation.projects.details

import android.content.ContextWrapper
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import pl.jitsolutions.agile.R
import pl.jitsolutions.agile.common.Error
import pl.jitsolutions.agile.domain.User

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
        ProjectDetailsViewModel.State.Success -> View.INVISIBLE
        ProjectDetailsViewModel.State.Idle -> View.INVISIBLE
        ProjectDetailsViewModel.State.InProgress -> View.VISIBLE
        ProjectDetailsViewModel.State.Empty -> View.INVISIBLE
        is ProjectDetailsViewModel.State.Fail -> View.INVISIBLE
    }
}

@BindingAdapter("bindProjectDetailsEmptyState")
fun bindProjectDetailsEmptyState(view: View, state: ProjectDetailsViewModel.State) {
    view.visibility = when (state) {
        ProjectDetailsViewModel.State.Success -> View.INVISIBLE
        ProjectDetailsViewModel.State.Idle -> View.INVISIBLE
        ProjectDetailsViewModel.State.InProgress -> View.INVISIBLE
        ProjectDetailsViewModel.State.Empty -> View.VISIBLE
        is ProjectDetailsViewModel.State.Fail -> View.INVISIBLE
    }
}

@BindingAdapter("bindProjectDetailsErrorState")
fun bindProjectDetailsErrorState(view: View, state: ProjectDetailsViewModel.State) {
    if (state !is ProjectDetailsViewModel.State.Fail) {
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

@BindingAdapter("bindProjectDetailsViewsVisibility")
fun bindProjectDetailsViewsVisibility(view: View, state: ProjectDetailsViewModel.State) {
    view.visibility = when (state) {
        ProjectDetailsViewModel.State.Success -> View.VISIBLE
        else -> View.GONE
    }
}

@BindingAdapter("bindProjectDetailsStateColor")
fun bindProjectDetailsStateColor(view: View, isActive: Boolean) {
    view.setBackgroundColor(
        ContextCompat.getColor(
            view.context,
            if (isActive) R.color.details_active_color else R.color.details_inactive_color
        )
    )
}

@BindingAdapter("bindProjectDetailsToolbarColor")
fun bindProjectDetailsToolbarColor(toolbar: Toolbar, isActive: Boolean) {
    toolbar.setBackgroundColor(
        ContextCompat.getColor(
            toolbar.context,
            if (isActive) R.color.details_toolbar_active_color else R.color.details_toolbar_inactive_color
        )
    )
}

@BindingAdapter("bindProjectDetailsDailyCardTextColor")
fun bindProjectDetailsDailyCardTextColor(textView: TextView, isActive: Boolean) {
    textView.setTextColor(
        ContextCompat.getColor(
            textView.context,
            if (isActive) R.color.light_text_color else R.color.dark_text_color
        )
    )
}

@BindingAdapter("bindProjectDetailsDailyCardColor")
fun bindProjectDetailsDailyCardColor(cardView: CardView, isActive: Boolean) {
    cardView.setCardBackgroundColor(
        ContextCompat.getColor(
            cardView.context,
            if (isActive) R.color.details_card_active_color else R.color.details_card_inactive_color
        )
    )
}

@BindingAdapter("bindProjectDetailsDailyCardIcon")
fun bindProjectDetailsDailyCardIcon(imageView: ImageView, isActive: Boolean) {
    imageView.setImageDrawable(
        ContextCompat.getDrawable(
            imageView.context,
            if (isActive) R.drawable.ic_daily_light else R.drawable.ic_daily_dark
        )
    )
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
        R.id.menu_project_details_change_project_password ->
            viewModel.changePassword()
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