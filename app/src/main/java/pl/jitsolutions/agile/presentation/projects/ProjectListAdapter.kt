package pl.jitsolutions.agile.presentation.projects

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import pl.jitsolutions.agile.BR
import pl.jitsolutions.agile.R
import pl.jitsolutions.agile.domain.ProjectWithDaily
import pl.jitsolutions.agile.presentation.common.BaseBindableAdapter

class ProjectListAdapter(
    private val onJoinDailyClick: (ProjectWithDaily) -> Unit,
    onProjectClick: (ProjectWithDaily) -> Unit
) :
    BaseBindableAdapter<ProjectWithDaily>(onProjectClick, DiffCallback()) {

    override fun getItemViewType(position: Int): Int = R.layout.list_item_project_project

    override fun setupBinding(binding: ViewDataBinding, item: ProjectWithDaily) {
        binding.setVariable(BR.joinDailyClickListener,
            View.OnClickListener { onJoinDailyClick.invoke(item) })
    }

    class DiffCallback : DiffUtil.ItemCallback<ProjectWithDaily>() {
        override fun areItemsTheSame(
            oldItem: ProjectWithDaily,
            newItem: ProjectWithDaily
        ): Boolean {
            return oldItem.project.id == newItem.project.id
        }

        override fun areContentsTheSame(
            oldItem: ProjectWithDaily,
            newItem: ProjectWithDaily
        ): Boolean {
            return true
        }
    }
}