package org.mikyegresl.appscanner.feature

import org.mikyegresl.appscanner.domain.scanner.ViewData
import org.mikyegresl.appscanner.domain.scanner.toDto
import org.mikyegresl.appscanner.domain.scanner.toUiElementDto
import org.mikyegresl.appscanner.domain.sensor.SensorData
import org.mikyegresl.appscanner.domain.sensor.toDto

internal data class ScannerData(
    val screenHeight: Int,
    val screenWidth: Int,
    val sensorData: SensorData,
    val userActions: Set<org.mikyegresl.appscanner.domain.scanner.UserAction>,
    val viewData: Set<ViewData>
)

internal fun ScannerData.toScannerDataDto(): org.mikyegresl.appscanner.data.ScannerDataDto {
    return org.mikyegresl.appscanner.data.ScannerDataDto(
        screenHeight = screenHeight,
        screenWidth = screenWidth,
        elements = viewData.map { it.toUiElementDto() },
        userActions = userActions.map { it.toDto() },
        sensorData = sensorData.toDto(),
    )
}
