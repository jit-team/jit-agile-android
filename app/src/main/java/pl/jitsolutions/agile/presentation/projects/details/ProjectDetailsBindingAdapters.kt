package pl.jitsolutions.agile.presentation.projects.details

import android.content.ContextWrapper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
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