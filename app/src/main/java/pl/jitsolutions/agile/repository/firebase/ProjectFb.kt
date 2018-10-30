package pl.jitsolutions.agile.repository.firebase

import pl.jitsolutions.agile.domain.Project

data class ProjectFb(var name: String = "", var users: Map<String, Boolean> = emptyMap())

fun List<ProjectFb>.convertToDomainObject(): List<Project> {
    return map { project ->
        Project(name = project.name)
    }
}