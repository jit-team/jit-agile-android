package pl.jitsolutions.agile.presentation.projects

import android.content.ContextWrapper
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.databinding.BindingAdapter
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import pl.jitsolutions.agile.R
import pl.jitsolutions.agile.domain.Project
import pl.jitsolutions.agile.domain.User

@BindingAdapter("bindProjectListNavigationIconVisibility")
fun bindProjectListNavigationIconVisibility(view: View, bind: Boolean) {
    if (bind) {
        val wrapper = view.context as? ContextWrapper
        val activity = wrapper?.baseContext as? AppCompatActivity
        activity?.apply {
            setSupportActionBar(view as? Toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
        }
    }
}

@BindingAdapter("bindProjectListLogoutListener")
fun bindProjectListNavigationViewListener(view: NavigationView, onLogoutListener: () -> Unit) {
    view.setNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.project_list_drawer_log_out -> {
                view.showLogoutConfirmation(onLogoutListener)
                true
            }
            else -> false
        }
    }
}

private fun NavigationView.showLogoutConfirmation(onLogoutListener: () -> Unit) {
    AlertDialog.Builder(context)
        .setTitle(R.string.project_list_screen_logout_confirmation_title)
        .setMessage(R.string.project_list_screen_logout_confirmation_message)
        .setPositiveButton(R.string.project_list_screen_logout_confirmation_positive_button_text) { _, _ ->
            onLogoutListener.invoke()
        }
        .setNegativeButton(
            R.string.project_list_screen_logout_confirmation_negative_button_text,
            null
        )
        .setCancelable(true)
        .show()
}

@BindingAdapter("bindProjectListUserProfile")
fun bindProjectListUserProfile(view: NavigationView, user: User?) {
    if (user == null) {
        return
    }

    val profileView = view.getHeaderView(0)
    profileView.findViewById<TextView>(R.id.profile_user_name).text = user.name
    profileView.findViewById<TextView>(R.id.profile_email).text = user.email
}

@BindingAdapter("bindProjectListVersion")
fun bindProjectListVersion(view: TextView, version: String) {
    view.text = view.resources.getString(R.string.project_list_screen_drawer_version, version)
}

@BindingAdapter("bindProjectListMenuItemSelected")
fun bindProjectListMenuItemSelected(view: DrawerLayout, menuItemId: Int) {
    if (android.R.id.home == menuItemId) {
        view.openDrawer(GravityCompat.START)
    }
}

@BindingAdapter(value = ["bindProjectListProjects", "bindProjectListAdapter"], requireAll = true)
fun bindProjectListProjects(
    recyclerView: RecyclerView,
    projects: List<Project>?,
    adapter: ProjectListAdapter?
) {
    adapter?.let {
        recyclerView.adapter = it
        it.projects = projects ?: emptyList()
    }
}

@BindingAdapter("bindProjectListProgressVisibility")
fun bindProjectListProgressVisibility(
    view: View,
    projectListState: ProjectListViewModel.ProjectListState
) {
    view.visibility = when (projectListState) {
        ProjectListViewModel.ProjectListState.None -> View.INVISIBLE
        ProjectListViewModel.ProjectListState.InProgress -> View.VISIBLE
        is ProjectListViewModel.ProjectListState.Error -> View.INVISIBLE
        ProjectListViewModel.ProjectListState.Success -> View.INVISIBLE
        ProjectListViewModel.ProjectListState.EmptyList -> View.INVISIBLE
    }
}

@BindingAdapter("bindProjectListEmptyList")
fun bindProjectListEmptyList(view: View, projectListState: ProjectListViewModel.ProjectListState) {
    view.visibility = when (projectListState) {
        ProjectListViewModel.ProjectListState.None -> View.INVISIBLE
        ProjectListViewModel.ProjectListState.InProgress -> View.INVISIBLE
        is ProjectListViewModel.ProjectListState.Error -> View.INVISIBLE
        ProjectListViewModel.ProjectListState.Success -> View.INVISIBLE
        ProjectListViewModel.ProjectListState.EmptyList -> View.VISIBLE
    }
}

@BindingAdapter("bindProjectListError")
fun bindProjectListError(view: TextView, state: ProjectListViewModel.ProjectListState) {
    if (state !is ProjectListViewModel.ProjectListState.Error)
        return

    val error: String? = when {
        state.isErrorOfType(ProjectListViewModel.ProjectListError.SERVER) -> {
            view.context.getString(R.string.project_list_screen_error_server)
        }
        state.isErrorOfType(ProjectListViewModel.ProjectListError.USER_NOT_FOUND) -> {
            view.context.getString(R.string.project_list_screen_error_user_not_found)
        }
        state.isErrorOfType(ProjectListViewModel.ProjectListError.UNKNOWN) -> {
            view.context.getString(R.string.project_list_screen_error_unknown)
        }
        else -> {
            null
        }
    }
    view.text = error
    view.visibility = if (error != null) View.VISIBLE else View.GONE
}