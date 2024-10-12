package hu.bme.aut.android.coapdemo

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import hu.bme.aut.android.coapdemo.databinding.ActivityMainBinding
import org.eclipse.californium.core.CoapClient
import org.eclipse.californium.core.CoapHandler
import org.eclipse.californium.core.CoapObserveRelation
import org.eclipse.californium.core.CoapResponse
import org.eclipse.californium.core.coap.MediaTypeRegistry
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Timer
import java.util.TimerTask

class MainActivity : AppCompatActivity(), SensorEventListener {
    companion object {
        private const val TAG = "COAPDEMO"
        private fun floatToBytes(value: Float): ByteArray? {
            return value.toString().toByteArray()
        }
    }

    private lateinit var binding: ActivityMainBinding

    private var sensorManager: SensorManager? = null
    private val sensors: MutableList<Sensor?> = ArrayList<Sensor?>()
    private val userData = UserData()
    private var timer: Timer? = null
    private var coapClient: CoapClient? = null
    private var observeRelation: CoapObserveRelation? = null
    private var gson: Gson = Gson()
    private val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")

    private var timerTask: TimerTask = object : TimerTask() {
        override fun run() {
            postMeasurement()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStart.setOnClickListener { startMeasuring() }
        binding.btnStop.setOnClickListener { stopMeasuring() }
    }

    override fun onSensorChanged(sensorEvent: SensorEvent) {
        userData.timestamp = df.format(Date())
        when (sensorEvent.sensor.getType()) {
            Sensor.TYPE_LIGHT -> userData.light = sensorEvent.values[0]
            Sensor.TYPE_ACCELEROMETER -> {
                userData.accX = sensorEvent.values[0]
                userData.accX = sensorEvent.values[1]
                userData.accX = sensorEvent.values[2]
            }

            Sensor.TYPE_AMBIENT_TEMPERATURE -> userData.temp = sensorEvent.values[0]
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, i: Int) {
    }

    fun startMeasuring() {
        userData.userId = binding.userID.text.toString()
        sensors.forEach {
            sensorManager?.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }

        coapClient = CoapClient(binding.serverAddress.text.toString())

        observeRelation = coapClient?.observe(object : CoapHandler {
            override fun onLoad(response: CoapResponse) {
                Log.d(
                    TAG,
                    "Observe reply: " + response.getCode() + "\n" + response.getResponseText()
                )
            }

            override fun onError() {
                Log.d(TAG, "Observe failed")
            }
        })

        timer = Timer()
        timer?.scheduleAtFixedRate(timerTask, 0, 5000)
        binding.btnStart.isEnabled = false
        binding.btnStop.isEnabled = true
    }

    fun stopMeasuring() {
        sensors.forEach {
            sensorManager?.unregisterListener(this, it)
        }
        timer?.cancel()
        observeRelation?.proactiveCancel()
        binding.btnStart.isEnabled = true
        binding.btnStop.isEnabled = false
        binding.logText.text.clear()
    }

    fun postMeasurement() {
        // send data to server
        Log.d(TAG, userData.toString())
        val payload = gson?.toJson(userData)

        Log.d(TAG, payload ?: "null payload")

        coapClient?.post(object : CoapHandler {
            override fun onLoad(response: CoapResponse) {
                Log.d(TAG, "POST response: " + response.getCode())
            }

            override fun onError() {
                Log.d(TAG, "POST failed")
            }
        }, payload, MediaTypeRegistry.APPLICATION_JSON)
    }

}