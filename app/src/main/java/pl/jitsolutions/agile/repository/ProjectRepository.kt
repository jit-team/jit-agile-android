package pl.jitsolutions.agile.repository

import kotlinx.coroutines.experimental.channels.ReceiveChannel
import pl.jitsolutions.agile.domain.Response

interface ProjectRepository {
    fun getProjectsWithChannel(userId: String): ReceiveChannel<Response<String>>
    suspend fun getProjects(userId: String): Response<String>
}