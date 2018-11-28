package pl.jitsolutions.agile.domain.usecases

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pl.jitsolutions.agile.repository.SystemInfoRepository

class NotificationUseCase(
    private val systemInfoRepository: SystemInfoRepository,
    private val dispatcher: CoroutineDispatcher
) {

    fun sendNotification(message: Map<String, String>) {
        CoroutineScope(dispatcher).launch {
            if (systemInfoRepository.getApplicationState().data == SystemInfoRepository.AppState.BACKGROUND) {
            }
        }
    }
}
