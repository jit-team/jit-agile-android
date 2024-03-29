package pl.jitsolutions.agile.repository

import pl.jitsolutions.agile.domain.Response

interface NotificationRepository {
    suspend fun assignDeviceTokenToUser(userId: String): Response<Unit>
    suspend fun unassignDeviceTokenFromUser(userId: String): Response<Unit>
}
