package com.udacity.asteroidradar.domain

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.PictureDatabase

@Dao
interface AsteroidDao {
    @Query("select * FROM asteroiddatabase WHERE closeApproachDate > :currentDate ORDER BY closeApproachDate ASC")
    fun getWeeksAsteroids(currentDate: String): LiveData<List<AsteroidDatabase>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids: AsteroidDatabase)

    @Query("delete from asteroiddatabase")
    fun removeAllAsteroidsFromDB()

    @Query("select * from asteroiddatabase where closeApproachDate = :currentDate")
    fun getTodayAsteroids(currentDate: String): LiveData<List<AsteroidDatabase>>

    @Query("select * FROM asteroiddatabase WHERE closeApproachDate >= :currentDate ORDER BY closeApproachDate ASC")
    fun getSavedAsteroids(currentDate: String): LiveData<List<AsteroidDatabase>>
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

@Dao
interface PictureDao {
    @Query("select * from  picturedatabase")
    fun getLastEntryFromCache(): LiveData<PictureDatabase>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPicture(vararg picture: PictureDatabase)
}

@Database(entities = [PictureDatabase::class], version = 1)
abstract class DatabasePicture: RoomDatabase() {
    abstract val pictureDao: PictureDao
}

private lateinit var INSTANCE_PICTURE: DatabasePicture
fun getPictureDatabase(context: Context): DatabasePicture {
    synchronized(DatabasePicture::class.java) {
        if (!::INSTANCE_PICTURE.isInitialized) {
            INSTANCE_PICTURE = Room.databaseBuilder(
                context.applicationContext,
                DatabasePicture::class.java, "pictures"
            ).build()
        }
    }
    return INSTANCE_PICTURE
}
