package com.udacity.asteroidradar.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.DatabaseAsteroids
import com.udacity.asteroidradar.network.asDatabaseModel
import com.udacity.asteroidradar.network.parseAsteroidsJsonResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.UnknownHostException


class AsteroidsRepository(private val database: DatabaseAsteroids) {

    val asteroids: LiveData<List<Asteroid>> = database.asteroidDao
        .getAsteroidsFromCache().map {
            it.asDomainModel()
        }

    suspend fun refreshAsteroids() {
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
                Log.v("find me", "No network")
            }
        }
    }

    suspend fun retrieveAsteroids(): LiveData<List<Asteroid>>  {
        withContext(Dispatchers.IO) {
            try {

                val asteroids: LiveData<List<Asteroid>> = database.asteroidDao
                    .getAsteroidsFromCache().map {
                        it.asDomainModel()
                    }
                Log.e("find me", "asteroids data is $asteroids")

            } catch (exception: Exception) {
                Log.e("find me", "Unable to fetch asteroids from db")
            }
        }
        return asteroids
    }

}