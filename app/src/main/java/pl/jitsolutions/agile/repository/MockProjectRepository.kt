package pl.jitsolutions.agile.repository

import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.channels.ReceiveChannel
import kotlinx.coroutines.experimental.channels.produce

class MockProjectRepository(private val dispatcher: CoroutineDispatcher) : ProjectRepository {
    override fun getGroups(userId: String): ReceiveChannel<String>
            = CoroutineScope(dispatcher).produce { send("test groups") }
}