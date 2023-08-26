package com.udacity.asteroidradar.domain

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class PictureOfDay(
    @Json(name = "media_type") val mediaType: String,
    @Json(name = "title") val title: String,
    @Json(name = "hdurl") val hdurl: String
) : Parcelable