package pl.jitsolutions.agile.repository

import kotlinx.coroutines.experimental.channels.ReceiveChannel
import pl.jitsolutions.agile.domain.Daily
import pl.jitsolutions.agile.domain.Response

interface DailyRepository {
    suspend fun endDaily(dailyId: String): Response<Unit>
    suspend fun joinDaily(dailyId: String): Response<Unit>
    suspend fun leaveDaily(dailyId: String): Response<Unit>
    suspend fun startDaily(dailyId: String): Response<Unit>
    suspend fun observeDaily(dailyId: String): ReceiveChannel<Response<Daily?>>
    suspend fun nextDaily(dailyId: String): Response<Unit>
    fun dispose()
}