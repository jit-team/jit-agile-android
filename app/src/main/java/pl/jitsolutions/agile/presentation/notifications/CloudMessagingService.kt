package pl.jitsolutions.agile.presentation.notifications

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import pl.jitsolutions.agile.di.Tags
import pl.jitsolutions.agile.domain.isSuccessfulWithData
import pl.jitsolutions.agile.domain.usecases.GetLoggedUserUseCase
import pl.jitsolutions.agile.domain.usecases.AssignDeviceTokenToUserTokenUseCase
import pl.jitsolutions.agile.domain.usecases.ShowStartDailyNotificationUseCase

class CloudMessagingService : FirebaseMessagingService(), KodeinAware {

    private val closestKodein: Kodein by closestKodein()

    override val kodein = Kodein.lazy {
        extend(closestKodein)
    }
    private val getLoggedUserUseCase: GetLoggedUserUseCase by instance()

    private val assignDeviceTokenToUserTokenUseCase: AssignDeviceTokenToUserTokenUseCase by instance()

    private val showStartDailyNotificationUseCase: ShowStartDailyNotificationUseCase by instance()

    private val dispatcher: CoroutineDispatcher by instance(tag = Tags.Dispatchers.USE_CASE)

    override fun onNewToken(token: String?) {
        CoroutineScope(dispatcher).launch {
            token?.let {
                val uid = getUserId()
                if (uid != null) {
                    register(uid, token)
                }
            }
        }
    }

    private suspend fun getUserId(): String? {
        val response = getLoggedUserUseCase.executeAsync(GetLoggedUserUseCase.Params).await()
        if (response.data != null) {
            return response.data.id
        }
        return null
    }

    private suspend fun register(uid: String, token: String) {
        val params = AssignDeviceTokenToUserTokenUseCase.Params()
        val registerResponse = assignDeviceTokenToUserTokenUseCase.executeAsync(params).await()
        if (registerResponse.isSuccessfulWithData()) {
        } else {
            // handle fail?
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        val projectName = remoteMessage.projectName()
        projectName?.let {
            CoroutineScope(dispatcher).launch {
                showStartDailyNotificationUseCase.executeAsync(
                    ShowStartDailyNotificationUseCase.Params(
                        it
                    )
                ).await()
            }
        }
    }

    private fun RemoteMessage?.projectName() = this?.data!!["projectName"]
}