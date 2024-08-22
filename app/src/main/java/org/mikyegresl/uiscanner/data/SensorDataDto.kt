package org.mikyegresl.uiscanner.data

import kotlinx.serialization.Serializable

@Serializable
class SensorDataDto(
    val accelerometer: SensorCoordinatesDto,
    val gyroscope: SensorCoordinatesDto
)

@Serializable
class SensorCoordinatesDto(
    val x: Float,
    val y: Float,
    val z: Float,
)