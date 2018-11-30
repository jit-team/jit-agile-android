package pl.jitsolutions.agile

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import pl.jitsolutions.agile.domain.Failure
import pl.jitsolutions.agile.domain.Project
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.Success
import pl.jitsolutions.agile.domain.User

inline fun <reified T> assertThat(
    response: Response<T>,
    assertion: ResponseAssertion<T>.() -> Unit
) {
    ResponseAssertion(response).apply(assertion)
}

class ResponseAssertion<T>(val response: Response<T>) {
    fun isSuccessful() = assertTrue(response is Success)

    fun isUnsuccessful() = assertTrue(response is Failure)

    fun hasString(string: String?) = assertEquals(string, (response as Success).data)
}

class ProjectAssertion(val project: Project) {
    fun hasName(name: String) = assertEquals(name, project.name)

    fun withName(name: String) = hasName(name)
}

fun <T> ResponseAssertion<T>.hasProject(projectAssertion: ProjectAssertion.() -> Unit) {
    val success = response as Success
    ProjectAssertion((success.data as Pair<Project, List<User>>).first).apply(projectAssertion)
}

class UserAssertion(val user: User) {
    fun withName(name: String) = assertEquals(name, user.name)

    fun withEmail(email: String) = assertEquals(email, user.email)
}

fun <T> ResponseAssertion<T>.hasUser(userAssertion: UserAssertion.() -> Unit) {
    val success = response as Success
    UserAssertion(success.data as User).apply(userAssertion)
}