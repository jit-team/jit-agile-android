package pl.jitsolutions.agile.domain.usecases

import pl.jitsolutions.agile.common.Error
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.errorResponse
import pl.jitsolutions.agile.domain.isSuccessfulWithData
import pl.jitsolutions.agile.domain.response
import pl.jitsolutions.agile.repository.NotificationRepository

class CloudMessagingManager(private val notificationRepository: NotificationRepository) {

    internal suspend fun assignDeviceTokenToUser(uid: String): Response<Unit> {
        val tokenResponse = notificationRepository.getDeviceToken()
        return if (tokenResponse.isSuccessfulWithData()) {
            handleSuccess(uid, tokenResponse.data!!)
        } else {
            errorResponse(error = tokenResponse.error ?: Error.Unknown)
        }
    }

    private suspend fun handleSuccess(uid: String, token: String): Response<Unit> {
        val addTokenResponse = notificationRepository.assignDeviceTokenToUser(uid, token)
        return if (addTokenResponse.isSuccessfulWithData()) {
            response(Unit)
        } else
            errorResponse(error = addTokenResponse.error ?: Error.Unknown)
    }
}