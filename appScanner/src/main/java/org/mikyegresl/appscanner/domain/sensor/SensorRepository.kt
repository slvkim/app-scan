package org.mikyegresl.appscanner.domain.sensor

import kotlinx.coroutines.flow.Flow

interface SensorRepository {

    val sensorDataState: Flow<SensorData>

    fun initSensors()

    fun releaseSensors()
}