package com.sreekanth.rentalprovider

import android.util.Log

private const val TAG = "AwsService"
class AwsService {

    init {
        // init every dependency to the aws
    }

    suspend fun fetchSpeedLimit(onUpdate: (Int) -> Unit) {
        // Simulate AWS speed limit fetch may be api or dynamic call back, and response on onUpdate
        onUpdate(80)
    }

    suspend fun sendSpeedWarning(currentSpeed: Int) {
        // Logic for sending a warning to AWS Api.
        Log.d(TAG, "sendSpeedWarning() called with: currentSpeed = $currentSpeed")
    }
}