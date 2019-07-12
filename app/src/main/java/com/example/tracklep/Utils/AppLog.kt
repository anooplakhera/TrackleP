package com.example.tracklep.Utils

object AppLog {
    val TAG = "Trackle_logs"

    val LOG_ENABLED = true

    fun printLog(message: String) {
        if (LOG_ENABLED)
            android.util.Log.i(TAG, message)
    }

    fun printLog(tag: String, message: String) {
        if (LOG_ENABLED)
            android.util.Log.i(tag, message)
    }
}
