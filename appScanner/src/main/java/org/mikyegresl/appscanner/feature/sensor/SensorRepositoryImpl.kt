package org.mikyegresl.appscanner.feature.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.mikyegresl.appscanner.domain.sensor.SensorCoordinates
import org.mikyegresl.appscanner.domain.sensor.SensorData
import org.mikyegresl.appscanner.domain.sensor.SensorRepository
import kotlin.math.round
import kotlin.math.sqrt

class SensorDataRetrieverImpl(context: Context) : SensorRepository {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as android.hardware.SensorManager

    private val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private val gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

    private val _sensorDataState = MutableStateFlow(
        SensorData(
            accelerometerData = SensorCoordinates(0f, 0f, 0f),
            gyroscopeData = SensorCoordinates(0f, 0f, 0f)
        )
    )

    override val sensorDataState = _sensorDataState.asStateFlow()

    private val coordinateGravity = FloatArray(3)

    private val listener = object: SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            event?.let { sensorEvent ->
                val filteredValues = filterSensorValues(values = sensorEvent.values)
                val normalizedValues = normalize(filteredValues)
                when (sensorEvent.sensor.type) {
                    Sensor.TYPE_ACCELEROMETER -> {
                        _sensorDataState.update {
                            it.copy(
                                accelerometerData = SensorCoordinates(
                                    x = normalizedValues.x,
                                    y = normalizedValues.y,
                                    z = normalizedValues.z
                                )
                            )
                        }
                    }
                    Sensor.TYPE_GYROSCOPE -> {
                        _sensorDataState.update {
                            it.copy(
                                gyroscopeData = SensorCoordinates(
                                    x = normalizedValues.x,
                                    y = normalizedValues.y,
                                    z = normalizedValues.z
                                )
                            )
                        }
                    }
                    else -> Unit
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    }

    override fun initSensors() {
        sensorManager.registerListener(listener, accelerometer, android.hardware.SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(listener, gyroscope, android.hardware.SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun releaseSensors() {
        sensorManager.unregisterListener(listener, accelerometer)
        sensorManager.unregisterListener(listener, gyroscope)
    }

    private fun filterSensorValues(values: FloatArray): SensorCoordinates {
        coordinateGravity[0] = ACCELEROMETER_ALPHA * coordinateGravity[0] + (1 - ACCELEROMETER_ALPHA) * values[0]
        coordinateGravity[1] = ACCELEROMETER_ALPHA * coordinateGravity[1] + (1 - ACCELEROMETER_ALPHA) * values[1]
        coordinateGravity[2] = ACCELEROMETER_ALPHA * coordinateGravity[2] + (1 - ACCELEROMETER_ALPHA) * values[2]
        return SensorCoordinates(
            x = round(values[0] - coordinateGravity[0]),
            y = round(values[1] - coordinateGravity[1]),
            z = round(values[2] - coordinateGravity[2]),
        )
    }

    private fun normalize(coordinates: SensorCoordinates): SensorCoordinates {
        val magnitude = sqrt(
            (coordinates.x*coordinates.x + coordinates.y*coordinates.y + coordinates.z*coordinates.z).toDouble()
        ).toFloat()
        return SensorCoordinates(
            x = coordinates.x/magnitude,
            y = coordinates.y/magnitude,
            z = coordinates.z/magnitude
        )
    }

    companion object {
        private const val ACCELEROMETER_ALPHA = .8f
    }
}
