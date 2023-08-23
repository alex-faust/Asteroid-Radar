package com.udacity.asteroidradar.domain

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import com.udacity.asteroidradar.database.AsteroidDatabase


@Dao
interface AsteroidDao {
    //get videos from the cache
    @Query("select * from asteroiddatabase")
    fun getAsteroidsFromCache(): LiveData<List<AsteroidDatabase>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids: AsteroidDatabase)
}
@Database(entities = [AsteroidDatabase::class], version = 1)
abstract class DatabaseAsteroids: RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
}

private lateinit var INSTANCE: DatabaseAsteroids

fun getDatabase(context: Context): DatabaseAsteroids {
    synchronized(DatabaseAsteroids::class.java){
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                DatabaseAsteroids::class.java, "asteroids"
            ).build()
        }
    }
    return INSTANCE
}
