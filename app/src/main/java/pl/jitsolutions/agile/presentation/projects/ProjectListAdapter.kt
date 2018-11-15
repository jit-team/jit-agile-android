package pl.jitsolutions.agile.presentation.projects

import android.view.View
import androidx.databinding.ViewDataBinding
import pl.jitsolutions.agile.BR
import pl.jitsolutions.agile.R
import pl.jitsolutions.agile.domain.ProjectWithDaily
import pl.jitsolutions.agile.presentation.common.BaseBindableAdapter

class ProjectListAdapter(
    private val onJoinDailyClick: (ProjectWithDaily) -> Unit,
    onProjectClick: (ProjectWithDaily) -> Unit
) :
    BaseBindableAdapter<ProjectWithDaily>(onProjectClick) {

    var projectsWithDaily: List<ProjectWithDaily> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getObjForPosition(position: Int) = projectsWithDaily[position]

    override fun getLayoutIdForPosition(position: Int) = R.layout.list_item_project_project

    override fun getItemCount() = projectsWithDaily.size

    override fun setupBinding(binding: ViewDataBinding, item: ProjectWithDaily) {
        binding.setVariable(BR.joinDailyClickListener, object : View.OnClickListener {
            override fun onClick(v: View?) {
                onJoinDailyClick.invoke(item)
            }
        })
    }
}