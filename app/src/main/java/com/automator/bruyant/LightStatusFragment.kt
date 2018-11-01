package com.automator.bruyant

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import kotlinx.android.synthetic.main.fragment_light_status.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Kishan P Rao on 26/09/18.
 */
class LightStatusFragment : Fragment(), MqttController.StatusCallback {
    override fun onManualChange(on: Boolean) {
        toggleLight.isEnabled = on
    }

    override fun onSensorData(sensorData: SensorData) {
        Log.i(TAG, "onSensorData: $sensorData")
    }

    override fun onLightChange(on: Boolean) {
        toggleLight.isChecked = on
    }

    lateinit var mqttController: MqttController
    private var previousState = false

    companion object {
        private val TAG = LightStatusFragment::class.java.simpleName
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_light_status, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mqttController.callback = this
//        mqttController.requestSensorData()
        toggleLight.isEnabled = manualCheck.isChecked
        toggleLight.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(compoundButton: CompoundButton?, isChecked: Boolean) {
                if (previousState != isChecked) {
                    mqttController.changeLight(isChecked)
                    previousState = isChecked
                }
            }
        })

        manualCheck.setOnCheckedChangeListener { buttonView, isChecked ->
            Log.i(TAG, "onViewCreated: manual: $isChecked")
            mqttController.requestManualMode(isChecked)
            toggleLight.isEnabled = isChecked
        }
//        TODO: Get both data!
    }

    override fun onDestroy() {
        mqttController.callback = null
        super.onDestroy()
    }
}