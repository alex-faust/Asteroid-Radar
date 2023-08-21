package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.models.Asteroid
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


private const val BASE_URL = "https://api.nasa.gov/"
private const val API_KEY = "Fpd9WjhRath3pfxJ75ed1uc8gC4wX9pgouO0S0m2"
private const val START_DATE = "2023-08-20"
private const val END_DATE = "2023-08-20"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface AsteroidApiService {
    @GET("neo/rest/v1/feed?")
    suspend fun getAsteroids(@Query("startDate") startDate: String,
                             @Query("endDate")endDate: String,
                             @Query("apiKey")apiKey: String): String

    object AsteroidApi {
        val retrofitService: AsteroidApiService by lazy {
            retrofit.create(AsteroidApiService::class.java)
        }
    }
}




