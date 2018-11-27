package pl.jitsolutions.agile.repository

import pl.jitsolutions.agile.domain.Response

interface NotificationRepository {

    suspend fun getDeviceToken(): Response<String?>
    suspend fun assignDeviceTokenToUser(userId: String, token: String): Response<Unit>
}
