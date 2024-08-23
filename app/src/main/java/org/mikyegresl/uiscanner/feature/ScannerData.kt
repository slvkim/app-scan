package org.mikyegresl.uiscanner.feature

import org.mikyegresl.uiscanner.data.ScannerDataDto
import org.mikyegresl.uiscanner.data.SensorCoordinatesDto
import org.mikyegresl.uiscanner.data.SensorDataDto
import org.mikyegresl.uiscanner.domain.scanner.UserAction
import org.mikyegresl.uiscanner.domain.scanner.ViewData
import org.mikyegresl.uiscanner.domain.scanner.toDto
import org.mikyegresl.uiscanner.domain.scanner.toUiElementDto
import org.mikyegresl.uiscanner.domain.sensor.SensorData

internal data class ScannerData(
    val screenHeight: Int,
    val screenWidth: Int,
    val sensorData: SensorData,
    val userActions: Set<UserAction>,
    val viewData: Set<ViewData>
)

internal fun ScannerData.toScannerDataDto(): ScannerDataDto {
    return ScannerDataDto(
        screenHeight = screenHeight,
        screenWidth = screenWidth,
        elements = viewData.map { it.toUiElementDto() },
        userActions = userActions.map { it.toDto() },
        sensorData = SensorDataDto(
            accelerometer = SensorCoordinatesDto(0f, 0f, 0f),
            gyroscope = SensorCoordinatesDto(0f, 0f, 0f)
        ),
    )
}
