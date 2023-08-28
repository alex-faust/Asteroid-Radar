package com.udacity.asteroidradar.repository

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.udacity.asteroidradar.api.PicOfDayApi
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.domain.DatabasePicture
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.UnknownHostException

class PictureRepository(private val database: DatabasePicture) {

    val picture: LiveData<PictureOfDay> = database.pictureDao
        .getLastEntryFromCache().map {
            PictureOfDay(
                mediaType = it.mediaType,
                title = it.title,
                explanation = it.explanation,
                hdurl = it.hdurl
            ).asDomainModel()
        }

    suspend fun refreshPicture() {
        withContext(Dispatchers.IO) {
            try {
                val pictureResponse = PicOfDayApi.picture.getPicOfDay()
                Log.v("find me", "picture response is $pictureResponse")
                database.pictureDao.insertPicture(pictureResponse.asDatabaseModel())
            } catch (exception: UnknownHostException) {
                Log.e("find me", "No network for refresh picture")
            }
        }
    }
}


