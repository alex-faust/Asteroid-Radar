package com.udacity.asteroidradar.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.PictureOfDay


@Entity
data class AsteroidDatabase constructor(
    @PrimaryKey
    val id: Long,
    val codename: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean
)

fun List<AsteroidDatabase>.asDomainModel(): List<Asteroid> {
    return map {
        Asteroid(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }
}

@Entity
data class PictureDatabase constructor(
    @PrimaryKey
    val mediaType: String,
    val title: String,
    val explanation: String,
    val hdurl: String
)

fun PictureOfDay.asDomainModel(): PictureOfDay {
    return PictureOfDay(
        mediaType = mediaType,
        title = title,
        explanation = explanation,
        hdurl = hdurl
    )

}

