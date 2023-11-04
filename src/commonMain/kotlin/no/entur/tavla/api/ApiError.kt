package no.entur.tavla.api

sealed class ApiError: Throwable() {
    data object FailedToGetStopPlaces : ApiError()
    data object FailedToGetDepartureBoard : ApiError()
}