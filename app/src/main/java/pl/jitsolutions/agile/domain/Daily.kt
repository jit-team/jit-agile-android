package pl.jitsolutions.agile.domain

data class Daily(
    val project: Project,
    val queue: List<String>,
    val users: List<User>,
    val state: String,
    val startTime: Long
)