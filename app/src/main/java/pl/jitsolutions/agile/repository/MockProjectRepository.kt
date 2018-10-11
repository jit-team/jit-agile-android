package pl.jitsolutions.agile.repository

import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.channels.ReceiveChannel
import kotlinx.coroutines.experimental.channels.produce
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.response

class MockProjectRepository(private val dispatcher: CoroutineDispatcher) : ProjectRepository {
    override fun getGroups(userId: String): ReceiveChannel<Response<String>> =
            CoroutineScope(dispatcher).produce { send(response("test groups")) }
}