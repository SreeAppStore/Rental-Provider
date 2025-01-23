package com.sreekanth.rentalprovider

import android.util.Log

private const val TAG = "FirebaseService"
class FirebaseService {
    init {
        // init every dependency to the Firebase
    }

    suspend fun fetchSpeedLimit(onUpdate: (Int) -> Unit): Boolean {
        val status = true
        // Simulate Firebase speed limit fetch dynamically fetch and
        // call onUpdate on change from firebase
        onUpdate(80)
        return status
    }

    suspend fun sendSpeedWarning(currentSpeed: Int) :Boolean {
        // Logic for sending a warning to Firebase.
        Log.d(TAG, "sendSpeedWarning() called with: currentSpeed = $currentSpeed")    }
}
