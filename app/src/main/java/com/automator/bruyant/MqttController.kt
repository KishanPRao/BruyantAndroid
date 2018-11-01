package com.automator.bruyant

import android.content.Context
import android.util.Log
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*
import org.json.JSONArray
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
        fun onManualChange(on: Boolean)
        fun onSensorData(sensorData: SensorData)
    }

    private lateinit var mqttAndroidClient: MqttAndroidClient
    private val serverUri = "tcp://m15.cloudmqtt.com:14033"

    private val clientId = "AndroidClient"
    private val topicRequestLightChange = "requestLightChange"
    private val topicRequestDoorChange = "requestDoorChange"
    private val topicRequestLightStatus = "requestLightStatus"
    private val topicRequestTempHumidData = "requestTempHumidData"
    private val topicRequestManualMode = "requestManualMode"

    private val topicLightStatus = "lightStatus"
    private val topicTempHumidData = "updateTempHumidData"

    private val username = "fjqsowab"
    private val password = "AE8FCQ5a1FfG"

    var callback: StatusCallback? = null

    private val iMqttActionListener = object : IMqttActionListener {
        override fun onSuccess(asyncActionToken: IMqttToken) {
            Log.w(TAG, "Subscribed!")
        }

        override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
            Log.w(TAG, "Subscribed fail!")
        }
    }

    private fun subscribe() {
        try {
            mqttAndroidClient.subscribe(topicLightStatus, 0, null, iMqttActionListener)
            mqttAndroidClient.subscribe(topicTempHumidData, 0, null, iMqttActionListener)
        } catch (ex: MqttException) {
            System.err.println("Exception whilst subscribing")
            ex.printStackTrace()
        }
    }

    fun requestLightStatus() {
        mqttAndroidClient.publish(topicRequestLightStatus, MqttMessage())
    }

    fun requestSensorData() {
        mqttAndroidClient.publish(topicRequestTempHumidData, MqttMessage())
    }

    fun requestDoorChange(on: Boolean) {
        val obj = JSONObject()
        obj.put("on", on)
//        Log.d(TAG, "Pub:" + obj.toString())
        mqttAndroidClient.publish(topicRequestDoorChange, MqttMessage(obj.toString().toByteArray()))
    }

    fun requestManualMode(on: Boolean) {
        val obj = JSONObject()
        obj.put("on", on)
        Log.d(TAG, "Pub:" + obj.toString())
        mqttAndroidClient.publish(topicRequestManualMode, MqttMessage(obj.toString().toByteArray()))
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
                try {
                    handleMessage(topic, message)
                } catch (e: Exception) {
                    e.printStackTrace()
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

    private fun handleMessage(topic: String?, message: MqttMessage?) {
        message?.apply {
            when (topic) {
                topicLightStatus -> {
                    val jsonOuter = JSONObject(message.toString())
                    val on = jsonOuter.getBoolean("on")
                    callback?.onLightChange(on)
                }
                topicTempHumidData -> {
//                    val list = ArrayList<SensorData>()
//                    val jsonArray = JSONArray(message.toString())
//                    Log.d(TAG, "handleMessage: got sensor data: $jsonArray")
//                    for (i in 0 until jsonArray.length()) {
//                        val jsonData = jsonArray.getJSONObject(i)
//                        val sensorData = SensorData(jsonData.getLong("timestamp"), jsonData.getDouble("temperature"), jsonData.getDouble("humidity"))
//                        list.add(sensorData)
////                        Log.v(TAG, "handleMessage: add sensor data: $list, $callback")
//                    }
////                    Log.v(TAG, "handleMessage: list of sensor data: $list, $callback")
//                    callback?.onSensorData(list)

                    val jsonOuter = JSONObject(message.toString())
                    val temp = jsonOuter.getDouble("temp")
                    val humid = jsonOuter.getDouble("humid")
                    callback?.onSensorData(SensorData(-1, temp, humid))
                }
                else -> {
                    Log.w(TAG, "handleMessage: bad topic: $topic")
                }
            }
        }
    }

    fun changeLight(on: Boolean) {
        val obj = JSONObject()
        obj.put("on", on)
        Log.d(TAG, "Pub:" + obj.toString())
        mqttAndroidClient.publish(topicRequestLightChange, MqttMessage(obj.toString().toByteArray()))
    }

    fun end() {
        try {
            mqttAndroidClient.disconnect()
            mqttAndroidClient.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}