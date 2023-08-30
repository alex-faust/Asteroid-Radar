package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.domain.PictureOfDay
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query

enum class AsteroidFilter(val value: String) {
    SHOW_TODAY(""),
    SHOW_SAVED(""),
    SHOW_WEEK("select * from asteroiddatabase")
}

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(Constants.BASE_URL)
    .build()

interface AsteroidApiService {
    @GET("neo/rest/v1/feed")
    suspend fun getAsteroids(
        @Query("start_Date") startDate: String,
        @Query("end_Date") endDate: String,
        @Query("api_Key") apiKey: String
    ): String


    @GET("planetary/apod?api_key=${Constants.API_KEY}")
    suspend fun getPicOfDay(): PictureOfDay
}

object AsteroidApi {
    val retrofitService: AsteroidApiService by lazy {
        retrofit.create(AsteroidApiService::class.java)
    }
    val asteroids = retrofit.create(AsteroidApiService::class.java)
}

object PicOfDayApi {
    val retrofitService: AsteroidApiService by lazy {
        retrofit.create(AsteroidApiService::class.java)
    }

    val picture = retrofit.create(AsteroidApiService::class.java)
}






