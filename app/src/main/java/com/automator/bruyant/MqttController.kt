package com.automator.bruyant

import android.content.Context
import android.util.Log
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*
import org.json.JSONObject


/**
 * Created by Kishan P Rao on 26/09/18.
 */
class MqttController {
    companion object {
        private val TAG = MqttController::class.java.simpleName
    }

    interface StatusCallback {
        fun onLightChange(on: Boolean)
    }

    lateinit var mqttAndroidClient: MqttAndroidClient
    val serverUri = "tcp://m15.cloudmqtt.com:14033"

    val clientId = "AndroidClient"
    val topicLightStatus = "lightStatus"
    val topicLightChange = "lightChange"

    val username = "fjqsowab"
    val password = "AE8FCQ5a1FfG"

    var callback: StatusCallback? = null

    private fun subscribe() {
        try {
            mqttAndroidClient.subscribe(topicLightStatus, 0, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken) {
                    Log.w(TAG, "Subscribed!")
                }

                override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                    Log.w(TAG, "Subscribed fail!")
                }
            })
        } catch (ex: MqttException) {
            System.err.println("Exception whilst subscribing")
            ex.printStackTrace()
        }
    }

    private fun connect() {
        val mqttConnectOptions = MqttConnectOptions()
        mqttConnectOptions.isAutomaticReconnect = true
        mqttConnectOptions.isCleanSession = false
        mqttConnectOptions.userName = username
        mqttConnectOptions.password = password.toCharArray()
        try {
            mqttAndroidClient.connect(mqttConnectOptions, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken) {
                    val disconnectedBufferOptions = DisconnectedBufferOptions()
                    disconnectedBufferOptions.isBufferEnabled = true
                    disconnectedBufferOptions.bufferSize = 100
                    disconnectedBufferOptions.isPersistBuffer = false
                    disconnectedBufferOptions.isDeleteOldestMessages = false
                    mqttAndroidClient.setBufferOpts(disconnectedBufferOptions)
                    subscribe()
                }

                override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                    Log.w(TAG, "Failed to connect to: " + serverUri + exception.toString())
                }
            })
        } catch (ex: MqttException) {
            ex.printStackTrace()
        }
    }

    fun begin(context: Context) {
        mqttAndroidClient = MqttAndroidClient(context, serverUri, clientId)
        mqttAndroidClient.setCallback(object : MqttCallbackExtended {
            override fun connectComplete(reconnect: Boolean, serverURI: String?) {
                Log.d(TAG, "connectComplete, ")
            }

            override fun messageArrived(topic: String?, message: MqttMessage?) {
                Log.d(TAG, "messageArrived, " + message?.toString())
                if (topicLightStatus.equals(topic)) {
                    message?.apply {
                        val jsonOuter = JSONObject(message.toString())
                        val on = jsonOuter.getBoolean("on")
                        callback?.onLightChange(on)
                    }
                }
            }

            override fun connectionLost(cause: Throwable?) {
                Log.d(TAG, "connectionLost, ")
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {
                Log.d(TAG, "deliveryComplete, ")
            }
        })
        connect()
    }

    fun changeLight(on: Boolean) {
        val obj = JSONObject()
        obj.put("on", on)
        Log.d(TAG, "Pub:" + obj.toString())
        mqttAndroidClient.publish(topicLightChange, MqttMessage(obj.toString().toByteArray()))
    }

    fun end() {
        mqttAndroidClient.close()
    }
}