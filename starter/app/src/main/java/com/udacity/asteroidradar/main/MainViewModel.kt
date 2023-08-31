package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.getDatabase
import com.udacity.asteroidradar.domain.getPictureDatabase
import com.udacity.asteroidradar.repository.AsteroidsRepository
import com.udacity.asteroidradar.repository.PictureRepository
import kotlinx.coroutines.launch

enum class AsteroidApiStatus { LOADING, ERROR, DONE }
enum class AsteroidFilter { SHOW_TODAY, SHOW_WEEK, SHOW_SAVED }
class MainViewModel(application: Application) : AndroidViewModel(application) {

    //region values
    private val _status = MutableLiveData<AsteroidApiStatus>()
    val status: LiveData<AsteroidApiStatus>
        get() = _status

    private val filter = MutableLiveData<AsteroidFilter>()

    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid?>()
    val navigateToSelectedAsteroid: MutableLiveData<Asteroid?>
        get() = _navigateToSelectedAsteroid

    private val database = getDatabase(application)
    private val asteroidsRepository = AsteroidsRepository(database)
    private val picDatabase = getPictureDatabase(application)
    private val pictureRepository = PictureRepository(picDatabase)
    //endregion

    init {
        viewModelScope.launch {
            asteroidsRepository.refreshAsteroids()
            pictureRepository.refreshPicture()
        }
    }

    /*val asteroidList: LiveData<List<Asteroid>> = filter.switchMap { filters ->
        //using switchMap makes it so the value is always updated when value is switched
        when(filters) {
            AsteroidFilter.SHOW_TODAY -> asteroidsRepository.asteroidsWeek
            AsteroidFilter.SHOW_SAVED -> asteroidsRepository.asteroidsWeek
            else -> asteroidsRepository.asteroidsWeek
        }
    }*/

    val asteroidList = asteroidsRepository.asteroidsWeek

    var displayPicOfDay = pictureRepository.pictureRepo.map {
        if (it.mediaType != "video") {
            it.hdurl
        } else {
            //maybe go into database and get the last picture?
            TODO("take a snapshot of video?")
        }
    }
    val singlePicture = displayPicOfDay
    var picOfDayContentDescription = pictureRepository.pictureRepo.map {
        it.explanation
    }

    fun updateFilter(filters: AsteroidFilter) {
        filter.value = filters
    }

    fun displayAsteroidDetails(asteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = asteroid
    }

    fun displayAsteroidDetailsComplete() {
        _navigateToSelectedAsteroid.value = null
    }
}


