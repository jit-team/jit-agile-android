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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import pl.jitsolutions.agile.common.Error
import pl.jitsolutions.agile.R
import pl.jitsolutions.agile.domain.ProjectWithDaily
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

@BindingAdapter(
    value = ["bindProjectListProjectsWithDaily", "bindProjectListAdapter"],
    requireAll = true
)
fun bindProjectListProjectsWithDaily(
    recyclerView: RecyclerView,
    projectsWithDaily: List<ProjectWithDaily>?,
    adapter: ProjectListAdapter?
) {
    adapter?.let {
        recyclerView.adapter = it
        it.submitList(projectsWithDaily ?: emptyList())
    }
}

@BindingAdapter("bindProjectListEmptyList")
fun bindProjectListEmptyList(view: View, state: ProjectListViewModel.State) {
    view.visibility = when (state) {
        ProjectListViewModel.State.None -> View.INVISIBLE
        ProjectListViewModel.State.InProgress -> View.INVISIBLE
        is ProjectListViewModel.State.Fail -> View.INVISIBLE
        ProjectListViewModel.State.Success -> View.INVISIBLE
        ProjectListViewModel.State.Empty -> View.VISIBLE
    }
}

@BindingAdapter("bindProjectListError")
fun bindProjectListError(view: TextView, state: ProjectListViewModel.State) {
    if (state !is ProjectListViewModel.State.Fail) {
        return
    }

    val error: String? = when {
        state.isErrorOfType(Error.Network) -> view.context.getString(R.string.project_list_screen_error_connection)
        state.isErrorOfType(Error.DoesNotExist) -> view.context.getString(R.string.project_list_screen_error_user_not_found)
        else -> view.context.getString(R.string.project_list_screen_error_unknown)
    }
    view.text = error
    view.visibility = if (error != null) View.VISIBLE else View.GONE
}

@BindingAdapter("bindProjectDailyVisibility")
fun bindProjectDailyVisibility(view: View, projectWithDaily: ProjectWithDaily) {
    view.visibility = if (projectWithDaily.daily == null) View.INVISIBLE else View.VISIBLE
}

@BindingAdapter("bindProjectListRefreshListener")
fun bindProjectListRefreshListener(
    swipeRefreshLayout: SwipeRefreshLayout,
    viewModel: ProjectListViewModel
) {
    swipeRefreshLayout.setOnRefreshListener { viewModel.refresh() }
}

@BindingAdapter("bindProjectListRefreshState")
fun bindProjectListRefreshState(
    swipeRefreshLayout: SwipeRefreshLayout,
    state: ProjectListViewModel.State
) {
    swipeRefreshLayout.isRefreshing = when (state) {
        ProjectListViewModel.State.None -> false
        ProjectListViewModel.State.InProgress -> true
        is ProjectListViewModel.State.Fail -> false
        ProjectListViewModel.State.Success -> false
        ProjectListViewModel.State.Empty -> false
    }
}

@BindingAdapter("bindProjectListFab")
fun bindProjectListFab(fab: FloatingActionButton, state: ProjectListViewModel.State) {
    when (state) {
        ProjectListViewModel.State.Success -> fab.show()
        ProjectListViewModel.State.Empty -> fab.show()
        else -> fab.hide()
    }
}