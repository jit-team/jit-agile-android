package pl.jitsolutions.agile.repository

import android.app.Activity
import android.app.Application
import android.os.Bundle
import pl.jitsolutions.agile.BuildConfig
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.Success

class AndroidSystemInfoRepository(application: Application) : SystemInfoRepository,
    Application.ActivityLifecycleCallbacks {

    private var appState: SystemInfoRepository.AppState = SystemInfoRepository.AppState.BACKGROUND

    init {
        application.registerActivityLifecycleCallbacks(this)
    }

    override fun getApplicationState(): Response<SystemInfoRepository.AppState> {
        return Success(appState)
    }

    override fun onActivityPaused(activity: Activity?) {
        appState = SystemInfoRepository.AppState.BACKGROUND
    }

    override fun onActivityResumed(activity: Activity?) {
        appState = SystemInfoRepository.AppState.FOREGROUND
    }

    override fun onActivityStarted(activity: Activity?) {
    }

    override fun onActivityDestroyed(activity: Activity?) {
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
    }

    override fun onActivityStopped(activity: Activity?) {
    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
    }

    override suspend fun getApplicationVersion(): Response<String> {
        return Success(BuildConfig.VERSION_NAME)
    }
}