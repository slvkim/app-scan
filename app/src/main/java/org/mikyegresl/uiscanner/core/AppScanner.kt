package org.mikyegresl.uiscanner.core

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.mikyegresl.uiscanner.domain.scanner.ScannerRepository
import org.mikyegresl.uiscanner.domain.user_action.UserActionRepository
import org.mikyegresl.uiscanner.feature.ScannerInteractor
import org.mikyegresl.uiscanner.feature.display.DisplayRepositoryImpl
import org.mikyegresl.uiscanner.feature.screen.UserActionRepositoryImpl
import org.mikyegresl.uiscanner.feature.screen.ViewScannerRepository
import org.mikyegresl.uiscanner.feature.sensor.SensorDataRetrieverImpl
import org.mikyegresl.uiscanner.feature.serializer.SerializerRepositoryImpl

internal class AppScanner {

    private var scannerInteractor: ScannerInteractor? = null
    private val serializerRepository by lazy { SerializerRepositoryImpl() }

    fun init(activity: Activity) {
        val displayRepository = DisplayRepositoryImpl(activity.resources.displayMetrics)
        val sensorRepository = SensorDataRetrieverImpl(activity)
        val scannerRepository: ScannerRepository by lazy { ViewScannerRepository() }
        val userActionRepository: UserActionRepository by lazy { UserActionRepositoryImpl(activity.resources) }
        scannerInteractor = ScannerInteractor(
            resources = activity.resources,
            displayRepository = displayRepository,
            sensorRepository = sensorRepository,
            scannerRepository = scannerRepository,
            userActionRepository = userActionRepository,
        )
    }

    fun doOnResume(activity: Activity) {
        scannerInteractor?.init(activity.window.decorView.rootView)

        val lifecycleScope = (activity as? AppCompatActivity)?.lifecycleScope

        lifecycleScope?.launch {
            scannerInteractor
                ?.observeUserActions()
                ?.collect { scannerData ->
                    serializerRepository.serialize(scannerData)
                }
        }
    }

    fun doOnPause() {
        scannerInteractor?.release()
    }
}

//TODO: check for sensors accuracy (google how to test)
//TODO: convert to .aar library
//TODO: test with importing library in release flavor