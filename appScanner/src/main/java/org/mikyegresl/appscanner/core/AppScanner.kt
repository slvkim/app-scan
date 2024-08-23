package org.mikyegresl.appscanner.core

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch
import org.mikyegresl.appscanner.domain.scanner.ScannerRepository
import org.mikyegresl.appscanner.domain.user_action.UserActionRepository
import org.mikyegresl.appscanner.feature.ScannerInteractor
import org.mikyegresl.appscanner.feature.display.DisplayRepositoryImpl
import org.mikyegresl.appscanner.feature.screen.UserActionRepositoryImpl
import org.mikyegresl.appscanner.feature.screen.ViewScannerRepository
import org.mikyegresl.appscanner.feature.sensor.SensorDataRetrieverImpl
import org.mikyegresl.appscanner.feature.serializer.SerializerRepositoryImpl

internal class AppScanner {

    private lateinit var scannerInteractor: ScannerInteractor
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

    fun doOnResume(activity: Activity) = (activity as? AppCompatActivity)?.let { appCompatActivity ->
        scannerInteractor.init(appCompatActivity.window.decorView.rootView)
        appCompatActivity.lifecycleScope.launch {
            scannerInteractor
                .observeUserActions()
                .drop(1)
                .collect { scannerData -> serializerRepository.serialize(scannerData) }
        }
    }

    fun doOnPause() {
        scannerInteractor.release()
    }
}