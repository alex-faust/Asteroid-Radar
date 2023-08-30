package com.udacity.asteroidradar.repository


import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.DatabaseAsteroids
import com.udacity.asteroidradar.network.asDatabaseModel
import com.udacity.asteroidradar.network.parseAsteroidsJsonResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import timber.log.Timber
import java.net.UnknownHostException


class AsteroidsRepository(private val database: DatabaseAsteroids) {

    val asteroids: LiveData<List<Asteroid>> = database.asteroidDao
        .getAsteroidsFromDB().map {
            it.asDomainModel()
        }

    val asteroidsToday: LiveData<List<Asteroid>> = database.asteroidDao
        .getTodayAsteroids().map {
            it.asDomainModel()
        }
    suspend fun refreshAsteroids() {
        //TODO("Check to see if I actually need withContext since suspend already shows it's in the off thread")

        withContext(Dispatchers.IO) {
            try {
            //see if i can do without withContext
                val asteroidResponse = AsteroidApi.asteroids
                    .getAsteroids(
                        Constants.START_DATE,
                        Constants.END_DATE,
                        Constants.API_KEY)
                val asteroidList = parseAsteroidsJsonResult(JSONObject(asteroidResponse), false)
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
                database.asteroidDao.getTodayAsteroids()
            } catch (exception: Exception) {
                Timber.tag("find me").e("Could not retrieve asteroids for today")
            }
        }
    }

    suspend fun retrieveAsteroids(): LiveData<List<Asteroid>>  {
        withContext(Dispatchers.IO) {
            try {

                val asteroids: LiveData<List<Asteroid>> = database.asteroidDao
                    .getAsteroidsFromDB().map {
                        it.asDomainModel()
                    }
                Timber.tag("find me").i( "asteroids data is $asteroids")

            } catch (exception: Exception) {
                Timber.tag("find me").e( "Unable to fetch asteroids from db")
            }
        }
        return asteroids
    }

    fun removeAllAsteroids() {
        database.asteroidDao.removeAllAsteroidsFromDB()
    }

}