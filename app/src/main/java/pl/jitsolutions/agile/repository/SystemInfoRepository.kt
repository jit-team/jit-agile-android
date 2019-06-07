package pl.jitsolutions.agile.repository

import pl.jitsolutions.agile.domain.Response

interface SystemInfoRepository {
    suspend fun getApplicationVersion(): Response<String>
    fun getApplicationState(): Response<AppState>

    enum class AppState {
        FOREGROUND,
        BACKGROUND
    }
}