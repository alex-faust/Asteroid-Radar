package com.udacity.asteroidradar.repository


import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.DatabaseAsteroids
import com.udacity.asteroidradar.network.asDatabaseModel
import com.udacity.asteroidradar.network.getTodaysFormattedDate
import com.udacity.asteroidradar.network.parseAsteroidsJsonResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import timber.log.Timber
import java.net.UnknownHostException


class AsteroidsRepository(private val database: DatabaseAsteroids) {

    private var currentDate: String = getTodaysFormattedDate()

    val asteroidsWeek: LiveData<List<Asteroid>> = database.asteroidDao
        .getWeeksAsteroids(currentDate).map {
            it.asDomainModel()
        }

    val asteroidsToday: LiveData<List<Asteroid>> = database.asteroidDao
        .getTodayAsteroids(currentDate).map {
            it.asDomainModel()
        }

    val asteroidsSaved: LiveData<List<Asteroid>> = database.asteroidDao
        .getSavedAsteroids(currentDate).map {
            it.asDomainModel()
        }

    suspend fun refreshAsteroids() {
        //TODO("Check to see if I actually need withContext since suspend already shows it's in the off thread")
        withContext(Dispatchers.IO) {
            try {
                val asteroidResponse = AsteroidApi.astRetrofitService.getAsteroids()
                val asteroidList = parseAsteroidsJsonResult(JSONObject(asteroidResponse))
                Timber.tag("find me").i("asteroidList is $asteroidList")
                database.asteroidDao.insertAll(*asteroidList.asDatabaseModel())
            } catch (exception: UnknownHostException) {
                Timber.tag("find me").i("No network")
            }
        }
    }

    fun removeAllAsteroids() {
        database.asteroidDao.removeAllAsteroidsFromDB()
    }
}