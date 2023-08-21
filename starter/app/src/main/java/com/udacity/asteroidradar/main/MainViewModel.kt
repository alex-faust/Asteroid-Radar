package com.udacity.asteroidradar.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.AsteroidApiService
import com.udacity.asteroidradar.models.Asteroid
import com.udacity.asteroidradar.network.parseAsteroidsJsonResult
import kotlinx.coroutines.launch
import org.json.JSONObject

enum class AsteroidApiStatus { LOADING, ERROR, DONE }
class MainViewModel : ViewModel() {

    private val _asteroids = MutableLiveData<ArrayList<Asteroid>>()
    val asteroids: LiveData<ArrayList<Asteroid>>
        get() = _asteroids

    private val _status = MutableLiveData<AsteroidApiStatus>()
    val status: LiveData<AsteroidApiStatus>
        get() = _status

    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid?>()
    val navigateToSelectedAsteroid: MutableLiveData<Asteroid?>
        get() = _navigateToSelectedAsteroid
    init {
        //getAllAsteroids()
        Log.v("find me","init on mainviewmodel")
        fillUpRV()

    }

    private fun fillUpRV() {
        getAllAsteroids()
    }

    private fun getAllAsteroids() {
        viewModelScope.launch {
                _status.value = AsteroidApiStatus.LOADING
                try {
                    Log.v("find me", "getAllAsteroids")
                    val jsonResult = AsteroidApiService.AsteroidApi.retrofitService
                        .getAsteroids(
                            Constants.START_DATE,
                            Constants.END_DATE,
                            Constants.API_KEY)
                    Log.v("find me", "raw data returned is $jsonResult")
                    _asteroids.value = parseAsteroidsJsonResult(JSONObject(jsonResult))
                    _status.value = AsteroidApiStatus.DONE
                } catch (e: Exception) {
                    _status.value = AsteroidApiStatus.ERROR
                    _asteroids.value = ArrayList()

            }
        }
    }

    fun displayAsteroidDetails(asteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = asteroid
    }

    fun displayAsteroidDetailsComplete() {
        _navigateToSelectedAsteroid.value = null
    }


}


