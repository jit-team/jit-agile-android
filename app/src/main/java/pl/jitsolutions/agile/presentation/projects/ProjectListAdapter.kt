package pl.jitsolutions.agile.presentation.projects

import pl.jitsolutions.agile.R
import pl.jitsolutions.agile.domain.Project
import pl.jitsolutions.agile.presentation.common.BaseBindableAdapter

class ProjectListAdapter : BaseBindableAdapter() {

    var projects: List<Project> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var itemClick: ProjectListItemCallback? = null

    override fun getObjForPosition(position: Int) = projects[position]

    override fun getLayoutIdForPosition(position: Int) = R.layout.list_item_project_project

    override fun getItemCount() = projects.size

    override fun itemClicked(position: Int) {
        itemClick?.click(projects[position])
    }
}