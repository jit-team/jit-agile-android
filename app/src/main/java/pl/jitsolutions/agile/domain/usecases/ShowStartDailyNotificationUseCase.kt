package pl.jitsolutions.agile.domain.usecases

import kotlinx.coroutines.CoroutineDispatcher
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.response
import pl.jitsolutions.agile.repository.NotificationRepository
import pl.jitsolutions.agile.repository.SystemInfoRepository

class ShowStartDailyNotificationUseCase(
    private val systemInfoRepository: SystemInfoRepository,
    private val notificationRepository: NotificationRepository,
    dispatcher: CoroutineDispatcher
) : UseCase<ShowStartDailyNotificationUseCase.Params, Unit>(dispatcher) {

    override suspend fun build(params: Params): Response<Unit> {
        return response(showNotification(params.projectName))
    }

    private suspend fun showNotification(projectName: String) {
        val response = systemInfoRepository.getApplicationState()
        when (response.status) {
            Response.Status.SUCCESS -> handleSuccess(response.data!!, projectName)
            else -> {
            }
        }
    }

    private suspend fun handleSuccess(
        appState: SystemInfoRepository.AppState,
        projectName: String
    ) {
        if (appState == SystemInfoRepository.AppState.BACKGROUND) {
            notificationRepository.showNotification(projectName)
        }
    }

    data class Params(val projectName: String)
}
