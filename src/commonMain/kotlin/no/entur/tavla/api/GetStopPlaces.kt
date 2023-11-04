package no.entur.tavla.api

import arrow.core.Either
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import no.entur.tavla.model.api.StopPlaceResponse

internal fun interface GetStopPlaces {
    suspend operator fun invoke(
        latitude: Float,
        longitude: Float,
    ): Either<ApiError.FailedToGetStopPlaces, StopPlaceResponse>
}

internal fun getStopPlaces(httpClient: HttpClient) =
    GetStopPlaces { latitude, longitude ->
        Either.catch<StopPlaceResponse> {
            httpClient.get("/geocoder/v1/reverse") {
                parameter("point.lat", latitude)
                parameter("point.lon", longitude)
                parameter("boundary.circle.radius", 1)
                parameter("size", 5)
                parameter("layers", "venue")
            }.body()
        }.mapLeft {
            ApiError.FailedToGetStopPlaces
        }
    }
