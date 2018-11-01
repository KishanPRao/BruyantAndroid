package com.automator.bruyant

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import kotlinx.android.synthetic.main.fragment_door.*
import kotlinx.android.synthetic.main.fragment_temp_humid.*

class DoorFragment : Fragment(), MqttController.StatusCallback {
    companion object {
        private val TAG = DoorFragment::class.java.simpleName
    }

    override fun onLightChange(on: Boolean) {
    }

    override fun onManualChange(on: Boolean) {
    }

    lateinit var mqttController: MqttController

    override fun onSensorData(sensorData: SensorData) {
        Log.i(TAG, "onSensorData: $sensorData")
//        for (sData in sensorData) {
//            val date = Date(sData.timestamp)
//            val df = SimpleDateFormat("dd/MM/yy HH:mm:ss", Locale.getDefault())
//            Log.v(TAG, "onSensorData: $date, ${df.format(date)}")
//        }
        sensorText.text = "Temperature: ${sensorData.temperature}\nHumidity: ${sensorData.humidity}"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_door, container, false)
    }

    private var previousState = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mqttController.callback = this
        toggleDoor.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(compoundButton: CompoundButton?, isChecked: Boolean) {
                if (previousState != isChecked) {
                    mqttController.requestDoorChange(isChecked)
                    previousState = isChecked
                }
            }
        })
    }

    override fun onDestroy() {
        mqttController.callback = null
        super.onDestroy()
    }
}