package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.AsteroidFilter
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.getDatabase
import com.udacity.asteroidradar.network.parseAsteroidsJsonResult
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.launch
import org.json.JSONObject

enum class AsteroidApiStatus { LOADING, ERROR, DONE }
class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val _asteroids = MutableLiveData<ArrayList<Asteroid>>()
    val asteroids: LiveData<ArrayList<Asteroid>>
        get() = _asteroids

    private val _status = MutableLiveData<AsteroidApiStatus>()
    val status: LiveData<AsteroidApiStatus>
        get() = _status

    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid?>()
    val navigateToSelectedAsteroid: MutableLiveData<Asteroid?>
        get() = _navigateToSelectedAsteroid

    private val database =  getDatabase(application)
    private val asteroidsRepository = AsteroidsRepository(database)

    init {
        //getAllAsteroids()
        Log.v("find me","init on mainviewmodel")
        fillUpRV()
        viewModelScope.launch {
            asteroidsRepository.refreshAsteroids()
        }

    }

    val asteroidList = asteroidsRepository.asteroids

    private fun fillUpRV() {
        getAllAsteroids(AsteroidFilter.SHOW_WEEK)
    }

    private fun getAllAsteroids(filter: AsteroidFilter) {
        viewModelScope.launch {
                _status.value = AsteroidApiStatus.LOADING
                try {
                    Log.v("find me", "getAllAsteroids")
                    when(filter) {
                        AsteroidFilter.SHOW_SAVED -> ""
                        AsteroidFilter.SHOW_TODAY -> ""
                        else -> ""
                    }
                    val jsonResult = AsteroidApi.retrofitService
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

    fun updateFilter(filter: AsteroidFilter) {
        getAllAsteroids(filter)
    }

    fun displayAsteroidDetails(asteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = asteroid
    }

    fun displayAsteroidDetailsComplete() {
        _navigateToSelectedAsteroid.value = null
    }


}


