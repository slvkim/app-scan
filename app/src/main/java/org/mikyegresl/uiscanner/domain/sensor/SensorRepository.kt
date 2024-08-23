package org.mikyegresl.uiscanner.domain.sensor

import kotlinx.coroutines.flow.StateFlow

interface SensorRepository {

    val sensorDataState: StateFlow<SensorData>

    fun initSensors()

    fun releaseSensors()
}