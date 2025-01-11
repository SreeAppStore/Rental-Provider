package com.sreekanth.rentalprovider

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    var nameConnected: String = "Alan"
    lateinit var repo: SpeedRepository
    val text = MutableLiveData<String>().apply { value = "Default Text" }
    val loginStatus = MutableLiveData<Boolean>().apply { value = false }

    init {
        text.postValue("Hello there. Please enter credential and login")
    }

    fun login() {
        text.postValue("Loading")
        viewModelScope.launch {
            delay(3000) // Simulate  delay
            // call login
            repo.login(onSuccess = { name ->
                loginStatus.postValue(true)
                text.postValue("Welcome $name, you are logged in.")
                repo.startMonitoring()
            }, onFailure = {
                loginStatus.postValue(false)
                text.postValue("Login Failed. Please enter credential and login")
            })
        }
    }

    fun logout() {
        text.postValue("Loading")
        viewModelScope.launch {
            delay(3000) // Simulate  delay
            // call logout and clear
            text.postValue("Hello there. Please enter credential and login")
        }
    }

    fun setRepo(provideRepo: SpeedRepository) {


    }
}