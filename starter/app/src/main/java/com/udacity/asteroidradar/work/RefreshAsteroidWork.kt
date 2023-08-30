package com.udacity.asteroidradar.work

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.domain.getDatabase
import com.udacity.asteroidradar.domain.getPictureDatabase
import com.udacity.asteroidradar.repository.AsteroidsRepository
import com.udacity.asteroidradar.repository.PictureRepository
import retrofit2.HttpException

class RefreshAsteroidWork(appContext: Context, params: WorkerParameters):
    CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "RefreshAsteroidWorker"
    }
    override suspend fun doWork(): Result {
        val asteroidDatabase = getDatabase(applicationContext)
        val pictureDatabase = getPictureDatabase(applicationContext)
        val asteroidRepository = AsteroidsRepository(asteroidDatabase)
        val pictureRepository = PictureRepository(pictureDatabase)

        return try {
            asteroidRepository.removeAllAsteroids()
            asteroidRepository.refreshAsteroids()
            pictureRepository.refreshPicture()
            Result.success()
        } catch (exception: HttpException) {
            Result.retry()
        }
    }

}