package org.mikyegresl.uiscanner.domain.sensor

data class SensorDataState(
    val accelerometerData: SensorCoordinates,
    val gyroscopeData: SensorCoordinates,
    //TODO: other sensors might be supported in future
)

data class SensorCoordinates(val x: Float, val y: Float, val z: Float)