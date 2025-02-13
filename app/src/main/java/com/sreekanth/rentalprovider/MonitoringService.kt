package com.sreekanth.rentalprovider

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

private const val TAG = "MonitoringService"

class MonitoringService : Service() {

    companion object {
        private val repository = SpeedRepository()
        fun provideRepo() = repository
    }

    private var lastNotificationTime: Long = 0
    private val cooldownPeriod = 30_000L

    private val coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())


    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG, "onStartCommand: ")
        repository.startMonitoring.observeForever {
            coroutineScope.launch {
                if (it) {
                    repository.startMonitoringSpeed(this@MonitoringService)
                } else {
                    //repository stop peacefully
                }
            }
            repository.sendSpeedViolation.observeForever {
                coroutineScope.launch {
                    // make a cool down time to send each notification, sample 30 seconds
                    val currentTime = System.currentTimeMillis()
                    if ((currentTime - lastNotificationTime >= cooldownPeriod)) {
                        lastNotificationTime = currentTime
                        sendSpeedWarningNotification(it)
                        repository.sendSpeedWarningNotificationToServer(it)
                    }
                }
            }
        }

        return START_STICKY
    }

    override fun onDestroy() {
        MonitoringService
        super.onDestroy()
        coroutineScope.launch {
            repository.stopListeningSpeedLimit()
        }
        coroutineScope.cancel()
        Log.i(TAG, "onDestroy: ")
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null // This is not a bound service
    }

    private fun sendSpeedWarningNotification(currentSpeed: Int) {
        val notification = NotificationCompat.Builder(this, "speed_channel")
            .setContentTitle("Speed Limit Exceeded!")
            .setContentText("Your current speed is $currentSpeed km/h. Limit: ${repository.speedLimit} km/h.")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        val manager = getSystemService(NotificationManager::class.java)
        manager.notify(2, notification)
    }
}