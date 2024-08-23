package org.mikyegresl.uiscanner.feature

import android.content.res.Resources
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.TextView
import androidx.core.view.children
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mikyegresl.uiscanner.domain.display.DisplayRepository
import org.mikyegresl.uiscanner.domain.scanner.ScannerRepository
import org.mikyegresl.uiscanner.domain.scanner.ViewData
import org.mikyegresl.uiscanner.domain.scanner.ViewType
import org.mikyegresl.uiscanner.domain.scanner.mapToSupportedType
import org.mikyegresl.uiscanner.domain.sensor.SensorRepository
import org.mikyegresl.uiscanner.domain.user_action.UserActionRepository

internal class ScannerInteractor(
    private val resources: Resources,
    private val displayRepository: DisplayRepository,
    private val sensorRepository: SensorRepository,
    private val scannerRepository: ScannerRepository,
    private val userActionRepository: UserActionRepository
) {

    /** do on activity resume */
    fun init(rootView: View) {
        scanScreenUi(rootView)
        sensorRepository.initSensors()
    }

    /** do on activity paused */
    fun release() {
        sensorRepository.releaseSensors()
    }

    private fun scanScreenUi(view: View) {
        retrieveViewData(
            view = view,
            onViewMeasuresRetrieved = { viewData ->
                scannerRepository.updateViews(viewData)
            },
            onViewReadyForListeners = {
                userActionRepository.registerForUserActions(it)
            }
        )
        (view as? ViewGroup)?.let { viewGroup ->
            for (singleView in viewGroup.children) {
                scanScreenUi(singleView)
            }
        }
    }

    private inline fun retrieveViewData(
        view: View,
        crossinline onViewMeasuresRetrieved: (ViewData) -> Unit,
        crossinline onViewReadyForListeners: (View) -> Unit
    ) {
        val viewType = view.mapToSupportedType()
        if (viewType != ViewType.UNKNOWN) {
            onViewReadyForListeners(view)
            view.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    ViewData(
                        id = resources.getResourceName(view.id),
                        viewType = viewType,
                        x = view.x.toInt(),
                        y = view.y.toInt(),
                        height = view.height,
                        width = view.width,
                        //TODO can be optimized in future. Better to create hashMap of attributes for text, textSize, color, etc
                        text = (view as? TextView)?.text.toString(),
                        textColor = (view as? TextView)?.currentTextColor.toString(),
                    ).let(onViewMeasuresRetrieved)
                }
            })
        }
    }

    fun observeUserActions() : Flow<ScannerData> =
        userActionRepository.userActions.map {
            ScannerData(
                screenHeight = displayRepository.screenHeight,
                screenWidth = displayRepository.screenWidth,
                sensorData = sensorRepository.sensorDataState.value,
                userActions = it,
                viewData = scannerRepository.viewDataState.value
            )
        }
}

