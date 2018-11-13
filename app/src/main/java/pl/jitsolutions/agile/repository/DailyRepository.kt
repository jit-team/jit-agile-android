package pl.jitsolutions.agile.repository

import pl.jitsolutions.agile.domain.Daily
import pl.jitsolutions.agile.domain.Response

interface DailyRepository {
    suspend fun getDaily(dailyId: String): Response<Daily>
    suspend fun joinDaily(dailyId: String): Response<Unit>
    suspend fun leaveDaily(dailyId: String): Response<Unit>
}