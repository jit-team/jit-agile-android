package pl.jitsolutions.agile.repository

import android.app.Application
import pl.jitsolutions.agile.domain.Response

interface SystemInfoRepository : Application.ActivityLifecycleCallbacks {
    suspend fun getApplicationVersion(): Response<String>
    suspend fun getApplicationState(): Response<AppState>

    enum class AppState {
        FOREGROUND,
        BACKGROUND
    }
}