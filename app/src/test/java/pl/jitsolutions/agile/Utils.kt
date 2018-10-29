package pl.jitsolutions.agile

import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import pl.jitsolutions.agile.domain.Project
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.User

inline fun <reified T> assertThat(response: Response<T>, assertion: ResponseAssertion<T>.() -> Unit) {
    ResponseAssertion(response).apply(assertion)
}

class ResponseAssertion<T>(val response: Response<T>) {
    fun isSuccessful() = assertEquals(Response.Status.SUCCESS, response.status)

    fun hasData(data: T?) = assertEquals(data, response.data)

    fun isUnsuccessful() = assertEquals(Response.Status.ERROR, response.status)

    fun hasError(error: Throwable?) = assertEquals(error, response.error)

    fun hasString(string: String?) = assertEquals(string, response.data)
}

class ProjectAssertion(val project: Project) {
    fun hasName(name: String) = assertEquals(name, project.name)

    fun withName(name: String) = hasName(name)

    fun hasUsers(users: List<User>) = assertArrayEquals(users.toTypedArray(), project.users.toTypedArray())

    fun withUsers(users: List<User>) = hasUsers(users)

    fun withNoUsers() = hasUsers(emptyList())
}

fun <T> ResponseAssertion<T>.hasProject(projectAssertion: ProjectAssertion.() -> Unit) {
    ProjectAssertion(response.data!! as Project).apply(projectAssertion)
}