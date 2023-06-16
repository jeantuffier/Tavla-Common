package no.entur.tavla.http

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import no.entur.tavla.model.api.StopPlaceResponse

fun interface GetStopPlaces {
    suspend operator fun invoke(
        latitude: Float,
        longitude: Float,
    ): StopPlaceResponse
}

fun getStopPlaces(
    httpClient: HttpClient
) = GetStopPlaces { latitude, longitude ->
    httpClient.get("/geocoder/v1/reverse") {
        parameter("point.lat", latitude)
        parameter("point.lon", longitude)
        parameter("boundary.circle.radius", 1)
        parameter("size", 5)
        parameter("layers", "venue")
    }.body()
}
