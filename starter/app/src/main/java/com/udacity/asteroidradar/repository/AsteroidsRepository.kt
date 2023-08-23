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


class AsteroidsRepository(private val database: DatabaseAsteroids) {

    val asteroids: LiveData<List<Asteroid>> = database.asteroidDao
        .getAsteroidsFromCache().map {
            it.asDomainModel()
        }
    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {//see if i can do without withContext
            val asteroidResponse = AsteroidApi.asteroids
                .getAsteroids(
                    Constants.START_DATE,
                    Constants.END_DATE,
                    Constants.API_KEY)
            val asteroidList = parseAsteroidsJsonResult(JSONObject(asteroidResponse))
            database.asteroidDao.insertAll(*asteroidList.asDatabaseModel())
        }
    }

}