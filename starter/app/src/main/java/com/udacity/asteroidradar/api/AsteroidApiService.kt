package com.udacity.asteroidradar.api

import com.udacity.asteroidradar.Constants
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query

enum class AsteroidFilter(val value: String) {
    SHOW_TODAY(""),
    SHOW_SAVED(""),
    SHOW_WEEK("")
}
/*private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(Constants.BASE_URL)
    .build()*/

interface AsteroidApiService {
    @GET("neo/rest/v1/feed")
    suspend fun getAsteroids(@Query("start_Date") startDate: String,
                             @Query("end_Date")endDate: String,
                             @Query("api_Key")apiKey: String): String

}

object AsteroidApi {

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(ScalarsConverterFactory.create())
        .baseUrl(Constants.BASE_URL)
        .build()

    val retrofitService: AsteroidApiService by lazy {
        retrofit.create(AsteroidApiService::class.java)
    }
    val asteroids = retrofit.create(AsteroidApiService::class.java)
}






