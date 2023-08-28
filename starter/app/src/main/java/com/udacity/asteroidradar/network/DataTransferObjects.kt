package com.udacity.asteroidradar.network

import com.squareup.moshi.JsonClass
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.PictureDatabase
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.PictureOfDay


@JsonClass(generateAdapter = true)
data class NetworkAsteroidContainer(val asteroids: List<NetworkAsteroid>)

@JsonClass(generateAdapter = true)
data class NetworkAsteroid(
    val id: Long,
    val codename: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean
)

//convert data transfer object to database object

fun NetworkAsteroidContainer.asDomainModel(): List<Asteroid> {
    return asteroids.map {
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

fun List<Asteroid>.asDatabaseModel(): Array<AsteroidDatabase> {
    return map {
        AsteroidDatabase(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous)
    }.toTypedArray()
}

@JsonClass(generateAdapter = true)
data class NetworkPictureContainer(val picture: NetworkPicture)

@JsonClass(generateAdapter = true)
data class NetworkPicture(
    val mediaType: String,
    val title: String,
    val explanation: String,
    val hdurl: String
)

fun NetworkPictureContainer.asDomainModel(): PictureOfDay {

    return PictureOfDay(
            mediaType = picture.mediaType,
            title = picture.title,
            explanation = picture.explanation,
            hdurl = picture.hdurl
        )
}

fun PictureOfDay.asDatabaseModel(): PictureDatabase {
    return PictureDatabase(
            mediaType = mediaType,
            title = title,
            explanation = explanation,
            hdurl = hdurl
        )
}




