package pl.jitsolutions.agile.repository

import kotlinx.coroutines.experimental.channels.ReceiveChannel
import pl.jitsolutions.agile.domain.Response

interface ProjectRepository {
    fun getGroups(userId: String): ReceiveChannel<Response<String>>
}