package org.mikyegresl.uiscanner.feature.screen

import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.core.view.children
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mikyegresl.uiscanner.data.ScannerDataDto
import org.mikyegresl.uiscanner.data.SensorCoordinatesDto
import org.mikyegresl.uiscanner.data.SensorDataDto
import org.mikyegresl.uiscanner.domain.scanner.ScannerRepository
import org.mikyegresl.uiscanner.domain.scanner.UserAction
import org.mikyegresl.uiscanner.domain.scanner.ViewData
import org.mikyegresl.uiscanner.domain.scanner.ViewType
import org.mikyegresl.uiscanner.domain.scanner.mapToSupportedType
import org.mikyegresl.uiscanner.domain.user_action.UserActionRepository

internal class ScannerInteractor(
    private val scannerRepository: ScannerRepository,
    private val userActionRepository: UserActionRepository
) {
    fun initScreenScanner(rootView: View) {
        scanScreenUi(rootView)
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
        when (view.mapToSupportedType()) {
            ViewType.UNKNOWN -> Unit
            else -> {
                onViewReadyForListeners(view)
                view.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                        ViewData.TextViewData(
                            view.id.toString(),
                            view.x.toInt(),
                            view.y.toInt(),
                            view.height,
                            view.width,
//                            view.text.toString(),
//                            view.currentTextColor.toString()
                        ).let(onViewMeasuresRetrieved)
                    }
                })
            }
        }
    }

    data class ScreenState(
        val userActions: Set<UserAction>,
        val viewData: Set<ViewData>
    )

    fun observeUserActions() : Flow<ScreenState> =
        userActionRepository.userActions.map {
            ScreenState(
                userActions = it,
                viewData = scannerRepository.viewDataState.value
            )
        }
}

internal fun ScannerInteractor.ScreenState.toScannerDataDto(): ScannerDataDto {
    return ScannerDataDto(
        screenHeight = 0,
        screenWidth = 0,
        elements = emptyList(),
        userActions = emptyList(),
        sensorData = SensorDataDto(
            accelerometer = SensorCoordinatesDto(0f,0f,0f),
            gyroscope = SensorCoordinatesDto(0f,0f,0f)
        ),
    )
}