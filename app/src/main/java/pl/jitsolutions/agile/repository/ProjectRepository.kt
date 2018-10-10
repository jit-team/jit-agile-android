package pl.jitsolutions.agile.repository

import kotlinx.coroutines.experimental.channels.ReceiveChannel

interface ProjectRepository {
    fun getGroups(userId: String) : ReceiveChannel<String>
}