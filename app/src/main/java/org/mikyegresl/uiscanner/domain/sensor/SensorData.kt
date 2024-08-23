package org.mikyegresl.uiscanner.domain.sensor

import org.mikyegresl.uiscanner.data.SensorCoordinatesDto
import org.mikyegresl.uiscanner.data.SensorDataDto

data class SensorData(
    val accelerometerData: SensorCoordinates,
    val gyroscopeData: SensorCoordinates,
    //TODO: other sensors might be supported in future
)

data class SensorCoordinates(val x: Float, val y: Float, val z: Float)

internal fun SensorData.toDto(): SensorDataDto =
    SensorDataDto(
        accelerometer = accelerometerData.toDto(),
        gyroscope = gyroscopeData.toDto(),
    )

private fun SensorCoordinates.toDto(): SensorCoordinatesDto =
    SensorCoordinatesDto(x = x, y = y, z = z)