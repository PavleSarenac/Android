package rs.ac.bg.etf.sensors

import android.hardware.SensorManager
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.sqrt

const val SHAKING_DETECTION_ACCELERATION_THRESHOLD = 5.0f
const val SHAKING_DETECTION_TIME_THRESHOLD = 1000L

data class UiState(
    val x: String = "",
    val y: String = "",
    val z: String = "",
    val timestamp: String = "",
    val accelerationMagnitude: String = "",
    val lastShakeTime: String = "0",
    val numberOfDetectedShakings: String = "0"
)

class SensorsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    fun updateSensorData(values: FloatArray, timestamp: Long) {
        val x = values[0]
        val y = values[1]
        val z = values[2]
        val accelerationMagnitude = sqrt(x * x + y * y + z * z) - SensorManager.GRAVITY_EARTH
        _uiState.update {
            it.copy(
                x = values[0].toString(),
                y = values[1].toString(),
                z = values[2].toString(),
                timestamp = timestamp.toString(),
                accelerationMagnitude = accelerationMagnitude.toString()
            )
        }

        if (
            accelerationMagnitude > SHAKING_DETECTION_ACCELERATION_THRESHOLD &&
            timestamp - _uiState.value.lastShakeTime.toLong() > SHAKING_DETECTION_TIME_THRESHOLD
            ) {
            val newNumberOfDetectedShakings = _uiState.value.numberOfDetectedShakings.toInt() + 1
            _uiState.update { 
                it.copy(
                    lastShakeTime = timestamp.toString(),
                    numberOfDetectedShakings = newNumberOfDetectedShakings.toString()
                )
            }
        }
    }
}