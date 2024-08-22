package org.mikyegresl.uiscanner.core

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.mikyegresl.uiscanner.domain.display.DisplayRepository
import org.mikyegresl.uiscanner.domain.scanner.ScannerRepository
import org.mikyegresl.uiscanner.domain.sensor.SensorRepository
import org.mikyegresl.uiscanner.domain.user_action.UserActionRepository
import org.mikyegresl.uiscanner.feature.display.DisplayRepositoryImpl
import org.mikyegresl.uiscanner.feature.screen.ScannerInteractor
import org.mikyegresl.uiscanner.feature.screen.UserActionRepositoryImpl
import org.mikyegresl.uiscanner.feature.screen.ViewScannerRepository
import org.mikyegresl.uiscanner.feature.sensor.SensorDataRetrieverImpl
import org.mikyegresl.uiscanner.feature.serializer.SerializerRepository

internal class AppScanner: Application.ActivityLifecycleCallbacks {

    private var scannerInteractor: ScannerInteractor? = null
    private var sensorRepository: SensorRepository? = null
    private var displayRepository: DisplayRepository? = null
    private val serializerRepository = SerializerRepository()

    fun inject(application: Application) {
        application.registerActivityLifecycleCallbacks(this)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        displayRepository = DisplayRepositoryImpl(activity.resources.displayMetrics)
        sensorRepository = SensorDataRetrieverImpl(activity)

        val scannerRepository: ScannerRepository by lazy { ViewScannerRepository() }
        val userActionRepository: UserActionRepository by lazy { UserActionRepositoryImpl(activity.resources) }
        scannerInteractor = ScannerInteractor(scannerRepository, userActionRepository)
    }

    override fun onActivityResumed(activity: Activity) {
        sensorRepository?.initSensors()
        scannerInteractor?.initScreenScanner(activity.window.decorView.rootView)

        val lifecycleScope = (activity as? AppCompatActivity)?.lifecycleScope

        lifecycleScope?.launch {
            scannerInteractor
                ?.observeUserActions()
                ?.collect { screenState ->
                    Log.e(TAG, "screenHeight: ${displayRepository?.screenHeight}, screenWidth: ${displayRepository?.screenWidth}")
                    Log.e(TAG, "uiElements: ${screenState.viewData}")
                    Log.e(TAG, "userActions: ${screenState.userActions}")
                    Log.e(TAG, "sensorData: ${sensorRepository?.sensorDataState?.value}")
                    serializerRepository.serialize(screenState)
                }
        }
    }

    override fun onActivityPaused(activity: Activity) {
        sensorRepository?.releaseSensors()
    }

    override fun onActivityStarted(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    override fun onActivityDestroyed(activity: Activity) {}

    companion object { private const val TAG = "AppScanner" }
}

//TODO: check for sensors accuracy (google how to test)
//TODO: check if Json conversion is proper
//TODO: interface for JsonConverter
//TODO: interface for Interactor
//TODO: wrapper for AppScanner
//TODO: pass text, textColor
//TODO: convert to .aar library
//TODO: test with importing library in release flavor