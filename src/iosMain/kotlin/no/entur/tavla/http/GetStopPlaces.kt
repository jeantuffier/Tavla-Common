package no.entur.tavla.http

fun interface GetStopPlaces {
    suspend operator fun invoke(
        latitude: Float,
        longitude: Float,
    ): List
}