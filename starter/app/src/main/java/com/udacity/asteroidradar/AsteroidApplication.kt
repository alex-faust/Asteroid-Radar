package com.udacity.asteroidradar


import android.app.Application
import android.os.Build
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.udacity.asteroidradar.work.RefreshAsteroidWork
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * Override application to setup background work via WorkManager
 * */
class AsteroidApplication : Application() {

    private val applicationScope = CoroutineScope(Dispatchers.Default)

    /**
     *      * onCreate is called before the first screen is shown to the user.
     *      *
     *      * Use it to setup any background tasks, running expensive setup operations in a background
     *      * thread to avoid delaying app start.
     *      */
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        delayedInit()
    }

    private fun delayedInit() {
        applicationScope.launch {
            setupRecurringWork()
        }
    }

    private fun setupRecurringWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(true)
            .setRequiresBatteryNotLow(true)
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    setRequiresDeviceIdle(true)
                }
            }.build()


        val repeatRequest = PeriodicWorkRequestBuilder<RefreshAsteroidWork>(1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance().enqueueUniquePeriodicWork(
            RefreshAsteroidWork.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatRequest
        )
    }
}