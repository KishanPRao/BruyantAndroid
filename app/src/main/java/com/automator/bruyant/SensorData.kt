package com.automator.bruyant

class SensorData(val timestamp: Long = -1, val temperature: Double, val humidity: Double) {
    override fun toString(): String {
        return "Timestamp: $timestamp, Temp: $temperature, Humid: $humidity"
    }
}