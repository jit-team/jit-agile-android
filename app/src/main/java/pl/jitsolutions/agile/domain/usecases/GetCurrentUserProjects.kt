package pl.jitsolutions.agile.domain.usecases

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.experimental.CoroutineDispatcher
import pl.jitsolutions.agile.domain.Project
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.repository.ProjectRepository

class GetCurrentUserProjects(
    private val projectRepository: ProjectRepository,
    dispatcher: CoroutineDispatcher
) : UseCase<GetCurrentUserProjects.Params, List<Project>>(dispatcher) {

    override suspend fun build(params: Params): Response<List<Project>> {
        // TODO we shouldnt hold Firebase dependencies here
        return projectRepository.getProjects(FirebaseAuth.getInstance().currentUser?.uid!!)
    }

    class Params
}
