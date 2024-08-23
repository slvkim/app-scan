package org.mikyegresl.appscanner.data

import kotlinx.serialization.Serializable

@Serializable
class ScannerDataDto(
    val screenHeight: Int,
    val screenWidth: Int,
    val elements: List<UiElementDto>,
    val userActions: List<UserActionDto>,
    val sensorData: SensorDataDto,
)