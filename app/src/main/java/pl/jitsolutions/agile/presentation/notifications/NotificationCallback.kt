package pl.jitsolutions.agile.presentation.notifications

import android.app.Activity
import android.app.Application
import android.os.Bundle

class NotificationCallback : Application.ActivityLifecycleCallbacks {

    var foreground: Boolean = false

    fun postMessage(message: Map<String, String>) {
        if (foreground) {
            // show notification
        }
    }

    override fun onActivityPaused(activity: Activity?) {
        foreground = false
    }

    override fun onActivityResumed(activity: Activity?) {
        foreground = true
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
}