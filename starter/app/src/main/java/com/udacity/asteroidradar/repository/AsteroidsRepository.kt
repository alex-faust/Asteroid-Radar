package com.udacity.asteroidradar.repository


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.udacity.asteroidradar.Constants
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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


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
        .getSavedAsteroids().map {
            it.asDomainModel()
        }

    suspend fun refreshAsteroids() {
        //TODO("Check to see if I actually need withContext since suspend already shows it's in the off thread")
        withContext(Dispatchers.IO) {
            try {
                val asteroidResponse = AsteroidApi.retrofitService.getAsteroids()
                val asteroidList = parseAsteroidsJsonResult(JSONObject(asteroidResponse))
                Log.i("find me", "asteroidList is $asteroidList")
                Timber.tag("find me").i("asteroidList is $asteroidList")
                database.asteroidDao.insertAll(*asteroidList.asDatabaseModel())
            } catch (exception: UnknownHostException) {
                Timber.tag("find me").i("No network")
            }
        }
    }


    suspend fun retrieveTodayAsteroids() {
        //TODO("see above")
        withContext(Dispatchers.IO) {
            try {
                //database.asteroidDao.getTodayAsteroids()
            } catch (exception: Exception) {
                Timber.tag("find me").e("Could not retrieve asteroids for today")
            }
        }
    }

    /*suspend fun retrieveAsteroids(): LiveData<List<Asteroid>> {
        withContext(Dispatchers.IO) {
            try {

                val asteroids: LiveData<List<Asteroid>> = null
                    }
                Timber.tag("find me").i("asteroids data is $asteroids")

            } catch (exception: Exception) {
                Timber.tag("find me").e("Unable to fetch asteroids from db")
            }
        }
        return asteroids
    }*/

    private fun getTodaysDate(): String {
        val calendar = Calendar.getInstance()
        val currentTime = calendar.time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        return dateFormat.format(currentTime)
    }

    fun removeAllAsteroids() {
        database.asteroidDao.removeAllAsteroidsFromDB()
    }
}