package pl.jitsolutions.agile

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import io.fabric.sdk.android.Fabric
import org.kodein.di.KodeinAware
import pl.jitsolutions.agile.di.kodeinBuilder

class JITAgileApplication : Application(), KodeinAware {
    override val kodein = kodeinBuilder

    override fun onCreate() {
        super.onCreate()
        setupCrashlytics()
    }

    private fun setupCrashlytics() {
        val crashlyticsKit = Crashlytics.Builder()
                .core(CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
                .build()

        Fabric.with(this, crashlyticsKit)
    }
}