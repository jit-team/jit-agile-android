package pl.jitsolutions.agile.presentation.projects

import pl.jitsolutions.agile.domain.Project

interface ProjectListItemCallback {
    fun click(project: Project)
}
