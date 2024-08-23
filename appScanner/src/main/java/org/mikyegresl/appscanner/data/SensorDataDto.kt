package org.mikyegresl.appscanner.data

import kotlinx.serialization.Serializable

@Serializable
class SensorDataDto(
    val accelerometer: org.mikyegresl.appscanner.data.SensorCoordinatesDto,
    val gyroscope: org.mikyegresl.appscanner.data.SensorCoordinatesDto
)

@Serializable
class SensorCoordinatesDto(
    val x: Float,
    val y: Float,
    val z: Float,
)