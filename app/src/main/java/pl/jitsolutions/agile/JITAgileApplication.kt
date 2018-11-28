package pl.jitsolutions.agile

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import io.fabric.sdk.android.Fabric
import org.kodein.di.KodeinAware
import pl.jitsolutions.agile.di.kodeinBuilder

class JITAgileApplication : Application(), KodeinAware {
    override val kodein = kodeinBuilder(this)

    override fun onCreate() {
        super.onCreate()
        setupCrashlytics()
        setupFirebase()
    }

    private fun setupCrashlytics() {
        val crashlyticsKit = Crashlytics.Builder()
            .core(CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
            .build()

        Fabric.with(this, crashlyticsKit)
    }

    private fun setupFirebase() {
        val settings = FirebaseFirestoreSettings.Builder()
            .setTimestampsInSnapshotsEnabled(true)
            .setPersistenceEnabled(false)
            .build()
        FirebaseFirestore.getInstance().firestoreSettings = settings
    }
}