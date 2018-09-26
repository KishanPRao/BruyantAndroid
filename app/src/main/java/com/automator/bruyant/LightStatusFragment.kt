package com.automator.bruyant

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import kotlinx.android.synthetic.main.fragment_light_status.*

/**
 * Created by Kishan P Rao on 26/09/18.
 */
class LightStatusFragment : Fragment(), MqttController.StatusCallback {
    private var previousState = false

    override fun onLightChange(on: Boolean) {
        toggleLight.isChecked = on
    }

    lateinit var mqttController: MqttController
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_light_status, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mqttController.callback = this
        toggleLight.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                if (previousState != p1) {
                    mqttController.changeLight(p1)
                    previousState = p1
                }
            }
        })
    }
}