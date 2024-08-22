package org.mikyegresl.uiscanner.domain.sensor

import kotlinx.coroutines.flow.StateFlow

interface SensorRepository {

    val sensorDataState: StateFlow<SensorDataState>

    fun initSensors()

    fun releaseSensors()
}