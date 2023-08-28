package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.AsteroidFilter
import com.udacity.asteroidradar.api.PicOfDayApi
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.domain.getDatabase
import com.udacity.asteroidradar.domain.getPictureDatabase
import com.udacity.asteroidradar.network.parseAsteroidsJsonResult
import com.udacity.asteroidradar.repository.AsteroidsRepository
import com.udacity.asteroidradar.repository.PictureRepository
import kotlinx.coroutines.launch
import org.json.JSONObject

enum class AsteroidApiStatus { LOADING, ERROR, DONE }
class MainViewModel(application: Application) : AndroidViewModel(application) {

    //region values
    private val _asteroids = MutableLiveData<ArrayList<Asteroid>>()
    val asteroids: LiveData<ArrayList<Asteroid>>
        get() = _asteroids

    private val _status = MutableLiveData<AsteroidApiStatus>()
    val status: LiveData<AsteroidApiStatus>
        get() = _status

    private val _pictureOfDay = MutableLiveData<PictureOfDay?>()
    val pictureOfDay: LiveData<PictureOfDay?>
        get() = _pictureOfDay

    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid?>()
    val navigateToSelectedAsteroid: MutableLiveData<Asteroid?>
        get() = _navigateToSelectedAsteroid

    private val database = getDatabase(application)
    private val asteroidsRepository = AsteroidsRepository(database)
    private val picDatabase = getPictureDatabase(application)
    private val pictureRepository = PictureRepository(picDatabase)
    //endregion

    init {
        Log.v("find me", "init on mainviewmodel")
        getPictureOfTheDay()
        getAllAsteroids(AsteroidFilter.SHOW_WEEK)
        viewModelScope.launch {
            asteroidsRepository.refreshAsteroids()
            pictureRepository.refreshPicture()

        }
    }

    val asteroidList = asteroidsRepository.asteroids
    val singlePicture = pictureRepository.picture

    var displayPicOfDay = _pictureOfDay.map {
        if (!it?.mediaType.equals("video")) {
            it?.hdurl
            //it?.explanation
        } else {
            //maybe go into database and get the last picture?
            TODO("take a snapshot of video?")
        }
    }

    var picOfDayContentDescription = _pictureOfDay.map {
        it?.explanation
    }

    private fun getPictureOfTheDay() {
        viewModelScope.launch {
            try {
                _status.value = AsteroidApiStatus.LOADING
                val jsonResult = PicOfDayApi.retrofitService.getPicOfDay()
                _pictureOfDay.value = jsonResult
                _status.value = AsteroidApiStatus.DONE
            } catch (e: Exception) {
                _status.value = AsteroidApiStatus.ERROR
            }
        }
    }

    private fun getAllAsteroids(filter: AsteroidFilter) {
        viewModelScope.launch {
            _status.value = AsteroidApiStatus.LOADING
            try {
                val jsonResult: String
                when (filter) {
                    AsteroidFilter.SHOW_SAVED -> {
                        viewModelScope.launch {
                            asteroidsRepository.retrieveAsteroids()

                        }
                    }

                    AsteroidFilter.SHOW_TODAY -> {

                        jsonResult = AsteroidApi.retrofitService
                            .getAsteroids(
                                Constants.START_DATE,
                                Constants.END_DATE,
                                Constants.API_KEY
                            )
                        _asteroids.value = parseAsteroidsJsonResult(
                            JSONObject(jsonResult), true
                        )
                    }

                    else -> {
                        jsonResult = AsteroidApi.retrofitService
                            .getAsteroids(
                                Constants.START_DATE,
                                Constants.END_DATE,
                                Constants.API_KEY
                            )
                        _asteroids.value = parseAsteroidsJsonResult(
                            JSONObject(jsonResult), false
                        )
                    }
                }
                _status.value = AsteroidApiStatus.DONE
            } catch (e: Exception) {
                _status.value = AsteroidApiStatus.ERROR
                _asteroids.value = ArrayList()
            }
        }
    }

    fun updateFilter(filter: AsteroidFilter) {
        getAllAsteroids(filter)
        getPictureOfTheDay()
    }

    fun displayAsteroidDetails(asteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = asteroid
    }

    fun displayAsteroidDetailsComplete() {
        _navigateToSelectedAsteroid.value = null
    }

    fun displayPictureOfDay(pictureOfDay: PictureOfDay) {
        _pictureOfDay.value = pictureOfDay
    }

    fun displayPictureOfDayComplete() {
        _pictureOfDay.value = null
    }
}


