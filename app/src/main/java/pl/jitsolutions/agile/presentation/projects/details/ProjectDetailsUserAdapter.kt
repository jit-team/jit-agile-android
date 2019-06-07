package pl.jitsolutions.agile.presentation.projects.details

import pl.jitsolutions.agile.R
import pl.jitsolutions.agile.domain.User
import pl.jitsolutions.agile.presentation.common.BaseBindableAdapter
import pl.jitsolutions.agile.presentation.common.UserDiffCallback

class ProjectDetailsUserAdapter : BaseBindableAdapter<User>({}, UserDiffCallback()) {
    override fun getItemViewType(position: Int): Int = R.layout.list_item_project_details_user
}