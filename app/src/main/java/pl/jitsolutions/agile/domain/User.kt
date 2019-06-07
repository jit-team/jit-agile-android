package pl.jitsolutions.agile.domain

data class User(
    val id: String,
    val name: String,
    val email: String,
    val active: Boolean = false,
    var current: Boolean = false
)