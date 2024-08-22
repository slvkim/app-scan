package org.mikyegresl.uiscanner.feature.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.mikyegresl.uiscanner.data.SensorCoordinatesDto
import org.mikyegresl.uiscanner.domain.sensor.SensorCoordinates
import org.mikyegresl.uiscanner.domain.sensor.SensorDataState
import org.mikyegresl.uiscanner.domain.sensor.SensorRepository
import kotlin.math.round

class SensorDataRetrieverImpl(context: Context) : SensorRepository {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as android.hardware.SensorManager

    private val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private val gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

    private val _sensorDataState = MutableStateFlow(
        SensorDataState(
            accelerometerData = SensorCoordinates(0f, 0f, 0f),
            gyroscopeData = SensorCoordinates(0f, 0f, 0f)
        )
    )

    override val sensorDataState = _sensorDataState.asStateFlow()

    private val coordinateGravity = FloatArray(3)

    private val listener = object: SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            event?.let { sensorEvent ->
                val filteredValues = filterSensorValues(
                    x = sensorEvent.values[0],
                    y = sensorEvent.values[1],
                    z = sensorEvent.values[2]
                )
                when (sensorEvent.sensor.type) {
                    Sensor.TYPE_ACCELEROMETER -> {
                        _sensorDataState.update {
                            it.copy(
                                accelerometerData = SensorCoordinates(
                                    x = filteredValues.x,
                                    y = filteredValues.y,
                                    z = filteredValues.z
                                )
                            )
                        }
                    }
                    Sensor.TYPE_GYROSCOPE -> {
                        _sensorDataState.update {
                            it.copy(
                                gyroscopeData = SensorCoordinates(
                                    x = filteredValues.x,
                                    y = filteredValues.y,
                                    z = filteredValues.z
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

    private fun filterSensorValues(x: Float, y: Float, z: Float): SensorCoordinatesDto {
        coordinateGravity[0] = ACCELEROMETER_ALPHA * coordinateGravity[0] + (1 - ACCELEROMETER_ALPHA) * x
        coordinateGravity[1] = ACCELEROMETER_ALPHA * coordinateGravity[1] + (1 - ACCELEROMETER_ALPHA) * y
        coordinateGravity[2] = ACCELEROMETER_ALPHA * coordinateGravity[2] + (1 - ACCELEROMETER_ALPHA) * z
        return SensorCoordinatesDto(
            x = round(x - coordinateGravity[0]),
            y = round(y - coordinateGravity[1]),
            z = round(z - coordinateGravity[2]),
        )
    }

    companion object {
        private const val ACCELEROMETER_ALPHA = .8f
    }
}
