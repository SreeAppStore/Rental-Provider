package com.sreekanth.rentalprovider

import android.car.Car
import android.car.VehiclePropertyIds
import android.car.hardware.CarPropertyValue
import android.car.hardware.property.CarPropertyManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.delay

class SpeedRepository {

    // private val database = FirebaseDatabase.getInstance()
    // private val speedLimitRef = database.getReference("speedLimit")

    private val _speedLimit = MutableLiveData<Int>()
    val speedLimit: LiveData<Int> get() = _speedLimit

    private val _currentSpeed = MutableLiveData<Int>()
    val currentSpeed: LiveData<Int> get() = _currentSpeed

    private val _startMonitoring = MutableLiveData(false)
    val startMonitoring: LiveData<Boolean> get() = _startMonitoring

    private val _sendSpeedViolation = MutableLiveData(false)
    val sendSpeedViolation: LiveData<Boolean> get() = _sendSpeedViolation

    private var car: Car? = null
    private var carPropertyManager: CarPropertyManager? = null

    suspend fun startMonitoringSpeed(monitoringService: MonitoringService) {
        startMonitoringSpeedLimit()
        startMonitoringVehicleSpeed(monitoringService)
    }

    private suspend fun startMonitoringSpeedLimit() {
        // Add listener to firebase node and call onUpdate
        updateSpeedLimit(80)
        delay(500)
        updateSpeedLimit(100)
        onSpeedChanged()
    }

    suspend fun stopListeningSpeedLimit() {
        // remove listener
    }

    fun updateCurrentSpeed(inputCurrentSpeed: Int) {
        _currentSpeed.postValue(inputCurrentSpeed)
    }

    private fun updateSpeedLimit(inputCurrentSpeed: Int) {
        _speedLimit.postValue(inputCurrentSpeed)
    }

    private fun startMonitoringVehicleSpeed(monitoringService: MonitoringService) {
        car = Car.createCar(monitoringService.application)
        carPropertyManager = car?.getCarManager(Car.PROPERTY_SERVICE) as CarPropertyManager
        carPropertyManager?.registerCallback(
            object : CarPropertyManager.CarPropertyEventCallback {
                override fun onChangeEvent(event: CarPropertyValue<*>?) {
                    val speed = event?.value as Int
                    speed.let {
                        updateCurrentSpeed(it)
                        onSpeedChanged()
                    }
                }

                override fun onErrorEvent(propId: Int, zone: Int) {
                    // Handle errors here
                }
            },
            VehiclePropertyIds.PERF_VEHICLE_SPEED,
            CarPropertyManager.SENSOR_RATE_NORMAL
        )
    }

    private fun onSpeedChanged() {
        if (_currentSpeed.value!! > _speedLimit.value!!) {
            _sendSpeedViolation.postValue(true)
        } else {
            _sendSpeedViolation.postValue(false)
        }
    }

    fun login(onSuccess: (String) -> Unit, onFailure: () -> Unit) {
        // APi call to web server for login, call success with user details
        // Save user details in the device
        onSuccess("Alan Walker")
    }

    fun startMonitoring() {
        _startMonitoring.postValue(true)
    }

    fun sendSpeedWarningNotificationToServer() {
        //Update firebase or AWS with user details
    }
}
