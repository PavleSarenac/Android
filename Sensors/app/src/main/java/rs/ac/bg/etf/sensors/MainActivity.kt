package rs.ac.bg.etf.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import rs.ac.bg.etf.sensors.ui.theme.SensorsTheme

class MainActivity : ComponentActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private lateinit var sensor: Sensor
    private val sensorsViewModel: SensorsViewModel = SensorsViewModel()

    override fun onSensorChanged(event: SensorEvent?) {
        with(event) {
            if ((this != null) && (sensor.type == Sensor.TYPE_ACCELEROMETER)) {
                sensorsViewModel.updateSensorData(values, timestamp / 1_000_000)
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!!

        setContent {
            val uiState by sensorsViewModel.uiState.collectAsState()

            SensorsTheme {
                Column {
                    Text(text = "x: ${uiState.x}")
                    Text(text = "y: ${uiState.y}")
                    Text(text = "z: ${uiState.z}")
                    Text(text = "timestamp: ${uiState.timestamp}")
                    Text(text = "accelerationMagnitude: ${uiState.accelerationMagnitude}")
                    Text(text = "lastShakeTime: ${uiState.lastShakeTime}")
                    Text(text = "numberOfDetectedShakings: ${uiState.numberOfDetectedShakings}")
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(
            this,
            sensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }
}