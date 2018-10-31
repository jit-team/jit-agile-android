package pl.jitsolutions.agile.presentation.projects.details

import android.content.ContextWrapper
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import pl.jitsolutions.agile.R
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
    adapter.users = users ?: emptyList()
}

@BindingAdapter("bindProjectDetailsProgressVisibility")
fun bindProjectDetailsProgressVisibility(
    progressBar: ProgressBar,
    state: ProjectDetailsViewModel.State
) {
    progressBar.visibility = when (state) {
        ProjectDetailsViewModel.State.Idle -> View.INVISIBLE
        ProjectDetailsViewModel.State.InProgress -> View.VISIBLE
        ProjectDetailsViewModel.State.Empty -> View.INVISIBLE
        is ProjectDetailsViewModel.State.Error -> View.INVISIBLE
    }
}

@BindingAdapter("bindProjectDetailsEmptyState")
fun bindProjectDetailsEmptyState(view: View, state: ProjectDetailsViewModel.State) {
    view.visibility = when (state) {
        ProjectDetailsViewModel.State.Idle -> View.INVISIBLE
        ProjectDetailsViewModel.State.InProgress -> View.INVISIBLE
        ProjectDetailsViewModel.State.Empty -> View.VISIBLE
        is ProjectDetailsViewModel.State.Error -> View.INVISIBLE
    }
}

@BindingAdapter("bindProjectDetailsErrorState")
fun bindProjectDetailsErrorState(view: View, state: ProjectDetailsViewModel.State) {
    if (state !is ProjectDetailsViewModel.State.Error) {
        return
    }

    val messageResId = when {
        state.isErrorOfType(ProjectDetailsViewModel.ErrorType.CONNECTION) ->
            R.string.project_details_screen_error_connection
        state.isErrorOfType(ProjectDetailsViewModel.ErrorType.PROJECT_NOT_FOUND) ->
            R.string.project_details_screen_error_project_not_found
        else -> R.string.project_details_screen_error_unknown
    }
    Toast.makeText(view.context.applicationContext, messageResId, Toast.LENGTH_SHORT).show()
}